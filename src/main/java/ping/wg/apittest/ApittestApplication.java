package ping.wg.apittest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

//注解取消spring boot的自动配置
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class ApittestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApittestApplication.class, args);
    }
}
