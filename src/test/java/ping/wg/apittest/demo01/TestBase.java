package ping.wg.apittest.demo01;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import ping.wg.apittest.config.ApiConfig;
import ping.wg.apittest.exlbeans.BaseBean;
import ping.wg.apittest.utils.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestBase extends AbstractTestNGSpringContextTests {

	@Autowired
	protected ApiConfig apiConfig;//配置文件中数据
	protected static Header[] publicHeaders;
	protected static String rootUrl;
	protected static boolean rooUrlEndWithSlash = false;
	protected static CloseableHttpClient client;
	public Map<String,String> saveParams= new HashMap<String,String>();//保存返回的数据

	/*
	* 验证返回值
	* */
	protected void verifyResult(String sourchData, String verifyStr) {
		if (StringUtil.isNotEmpty(sourchData)&&
				StringUtil.isNotEmpty(verifyStr)){
			//验证数据为json格式
			Map<String, String> verifyMap = JSON.parseObject(verifyStr,HashMap.class);
			for (Map.Entry<String, String> entry : verifyMap.entrySet()) {
			      String key=entry.getKey();
			      String exceptValue = entry.getValue();
				  String actualValue=JSONPath.read(sourchData,key).toString();
					//判断是否是从保存返回数据池中获取的值
				if (exceptValue.startsWith("$.")){
					exceptValue=saveParams.get(exceptValue.substring(2,exceptValue.length()));
				}
				ReportUtil.log(String.format("验证转换后的值:\n%s=%s", actualValue,
						exceptValue));
				Assert.assertEquals(actualValue, exceptValue, "验证预期结果失败。");

				}
		}
	}
	/*
	* 保存返回值，返回值之间用分号隔开
	* */
	public void saveDatas(String sourceData,String saveStr){

		if (StringUtil.isNotEmpty(saveStr)&&
				StringUtil.isNotEmpty(sourceData)){
			String[]tempsParams = saveStr.split(";");

			for (int i=0;i<tempsParams.length;i++){

				saveParams.put(tempsParams[i],
						JSONPath.read(sourceData,"$."+tempsParams[i]).toString());
			}

		}

	}

	/**
	 * 读取单个excel中指定sheet中的数据
	 */
	protected <T extends BaseBean> List<T> readExcelData(Class<T> clz,
														 String excelPathArr, String[] sheetNameArr)
			throws DocumentException {

		//所有的excel测试文件
		List<T> allExcelData = new ArrayList<T>();
		List<T> temArrayList = new ArrayList<T>();
		File file = Paths.get(System.getProperty("user.dir"),
				excelPathArr).toFile();
		//遍历所有的测试excel文件路径
		for (String sheetName : sheetNameArr) {

			//添加指定sheet名称
			temArrayList.addAll(ExcelUtil.readExcel(clz,
					file.getAbsolutePath(), sheetName));
		}
		allExcelData.addAll(temArrayList); // 将excel数据添加至list
		return allExcelData;
	}

	//读取多个excel文件中的数据
	protected <T extends BaseBean> List<T> readExcelDatas(Class<T> clz, String[] excelPathArr)
			throws DocumentException {

		//所有的excel测试文件
		List<T> allExcelData = new ArrayList<T>();
		List<T> temArrayList = new ArrayList<T>();
		//遍历所有的测试excel文件路径
		for (String excelPath : excelPathArr) {
			File file = Paths.get(System.getProperty("user.dir"),
					excelPath).toFile();
			//获取excel表所有sheet数据
			temArrayList = ExcelUtil.readExcel(clz, file.getAbsolutePath());

			allExcelData.addAll(temArrayList); // 将excel数据添加至list
		}
		return allExcelData;
	}


	/**
	 * 封装请求方法
	 *
	 */
	protected HttpRequestBase parseHttpRequest(String url, String method, String param) throws UnsupportedEncodingException, URISyntaxException {
		// 处理url
		url = parseUrl(url).replace("\n","");
		ReportUtil.log("method:" + method);
		ReportUtil.log("url:" + url.replace("\n",""));
		ReportUtil.log("param:" + param);

		URIBuilder uriBuilder = new URIBuilder(url);
		//设置参数-----配置文件中参数
		Map<String,String> commonParams = apiConfig.getParams();

		if (commonParams!=null&&commonParams.size()>0){
			commonParams.forEach((key, value) -> {
				uriBuilder.setParameter(key,value);
			});
		}
		//请求url
		URI uri = null;

		//upload表示上传，也是使用post进行请求
		if ("post".equalsIgnoreCase(method) || "upload".equalsIgnoreCase(method)) {

			uri = uriBuilder.build();
			// 封装post方法
			HttpPost postMethod = new HttpPost(uri);

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();

			if (param!=null&&param!=""){
				Map<String, String> paramMap = JSON.parseObject(param,
						HashMap.class);
				paramMap.forEach((key,value)->{

					parameters.add(new BasicNameValuePair(key,value));
				});

				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
				postMethod.setEntity(formEntity);

			}

			postMethod.setHeaders(publicHeaders);

			return postMethod;
		} else {
			// 封装get方法
			//设置参数----测试用例中参数
			if (StringUtil.isNotEmpty(param)){
				Map<String, String> paramMap = JSON.parseObject(param,
						HashMap.class);
				for (Map.Entry<String, String> entry : paramMap.entrySet()){
					String key = entry.getKey();
					String value = entry.getValue();
					if (value.startsWith("$.")){
						value = saveParams.get(value.substring(2,value.length()));
					}
					uriBuilder.setParameter(key,value);
				}
			}
			uri = uriBuilder.build();
			HttpGet getMethod = new HttpGet(uri);
			getMethod.setHeaders(publicHeaders);
			return getMethod;
		}
	}


	/**
	 * 格式化url,替换路径参数等。
	 *
	 * @param shortUrl
	 * @return
	 */
	private String parseUrl(String shortUrl) {
		if (shortUrl.startsWith("https")) {
			return shortUrl;
		}
		if (rooUrlEndWithSlash == shortUrl.startsWith("/")) {
			if (rooUrlEndWithSlash) {
				shortUrl = shortUrl.replaceFirst("/", "");
			} else {
				shortUrl = "/" + shortUrl;
			}
		}
		return rootUrl + shortUrl;
	}


	//初始化数据
	public void init() throws Exception {
		// 获取基础数据
		rootUrl = apiConfig.getRootUrl();
		rooUrlEndWithSlash = rootUrl.endsWith("/");
		// 读取 param，并将值保存到公共数据map
		Map<String, String> params = apiConfig.getParams();
		List<Header> headers = new ArrayList<Header>();
		apiConfig.getHeaders().forEach((key, value) -> {
			Header header = new BasicHeader(key, value);
			headers.add(header);
		});

		//将list转化为array的时候
		publicHeaders = headers.toArray(new Header[headers.size()]);

		client = HttpClients.createDefault();
		ReportUtil.setReportName(apiConfig.getProject_name());

	}


}
