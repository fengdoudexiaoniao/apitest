package ping.wg.apittest.demo01;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ping.wg.apittest.dao.ApiRepository;
import ping.wg.apittest.entity.ApiEntity;
import ping.wg.apittest.utils.ReportUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
* 测试从数据库中读取数据测试
* */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDemo extends TestBase {

@Autowired
private ApiRepository apiRepository;
private List<ApiEntity> datalist = new ArrayList<ApiEntity>();


    @Test
    public void testApi () throws Exception {
        //datalist = apiRepository.findAll();
    }


    @DataProvider(name = "apiDatas")
    public Iterator<Object[]> getTestData(){
        datalist = apiRepository.findAll();
        List<Object[]> tempdataprovider = new ArrayList<>();
        for (ApiEntity entity:datalist){
           if (entity.getApirun().equalsIgnoreCase("y")){
               tempdataprovider.add(new Object[]{entity});
           }
        }

        return  tempdataprovider.iterator();

    }

    @Test(dataProvider = "apiDatas")
    public  void testDataBase(ApiEntity apiEntity) throws Exception {

        //初始化数据
        init();
        ReportUtil.log("--- test start ---");
        // 封装请求方法
        HttpRequestBase method = parseHttpRequest(apiEntity.getApiurl(),apiEntity.getApimethod(),apiEntity.getApiparam());
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
        verifyResult(responseData, apiEntity.getApiverify());

        //保存返回数据
        saveDatas(responseData,apiEntity.getApisave());
    }

}
