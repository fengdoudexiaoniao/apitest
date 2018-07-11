package ping.wg.apittest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 对应上方配置文件中的第一段配置
 * @author pingshuixiangfeng
 * @date 2018年6月29日
 * @version V1.0
 * @since JDK ： 1.8
 */
@Component
@ConfigurationProperties(prefix = "wg.ping",ignoreUnknownFields = false)
@PropertySource(value = {"classpath:apitest-config.properties"},encoding="utf-8")
public class ApiConfig {

    //请求根url
    private String rootUrl;
    //定义参数集合，存放请求头
    private Map<String,String> params = new HashMap<String, String>();
    //定义参数集合，存放请求头
    private Map<String,String> headers = new HashMap<String, String>();
    //测试报告名称
    private String project_name;

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    @Override
    public String toString() {
        return "ApiConfig{" +
                "rootUrl='" + rootUrl + '\'' +
                ", params=" + params +
                ", headers=" + headers +
                ", project_name='" + project_name + '\'' +
                '}';
    }
}
