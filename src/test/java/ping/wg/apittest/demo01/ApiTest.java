package ping.wg.apittest.demo01;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.testng.annotations.Optional;
import ping.wg.apittest.exlbeans.ApiDataBean;
import ping.wg.apittest.utils.ReportUtil;
import java.util.*;

/**
 * 测试从excel中读取数据测试
 * */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest extends TestBase {

	private List<ApiDataBean> dataList = new ArrayList<ApiDataBean>();

	@Parameters({ "excelPath", "sheetName" })
	@BeforeTest
	public void readDatas(@Optional("case/api-data.xls") String excelPath, @Optional("Sheet1") String sheetName) throws DocumentException {
		dataList = readExcelDatas(ApiDataBean.class,excelPath.split(";"));
	}
	@DataProvider(name = "apiDatas")
	public Iterator<Object[]> getApiData(ITestContext context)
			throws DocumentException {
		List<Object[]> dataProvider = new ArrayList<Object[]>();
		for (ApiDataBean data : dataList) {
			if (data.isRun()) {
				dataProvider.add(new Object[] { data });
			}
		}
		return dataProvider.iterator();
	}


	@Test(dataProvider = "apiDatas")
	public void apiTest(ApiDataBean apiDataBean) throws Exception {

		//初始化数据
		init();
		ReportUtil.log("--- test start ---");
		// 封装请求方法
		HttpRequestBase method = parseHttpRequest(apiDataBean.getUrl(),apiDataBean.getMethod(),apiDataBean.getParam());
		String responseData;
		try {
			// 执行
			CloseableHttpResponse response = client.execute(method);
			int responseStatus = response.getStatusLine().getStatusCode();
			ReportUtil.log("返回状态码："+responseStatus);
			HttpEntity respEntity = response.getEntity();
			responseData=EntityUtils.toString(respEntity, "UTF-8");

		} catch (Exception e) {
			throw e;
		} finally {
			method.abort();
		}
		// 输出返回数据log
		ReportUtil.log("resp:" + responseData);
		// 验证预期信息
		verifyResult(responseData, apiDataBean.getVerify());

		//保存返回数据
		saveDatas(responseData,apiDataBean.getSave());


	}




}
