package cms.sre.gitlab_webhook;


import cms.sre.gitlab_webhook.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication()
public class App {

    @Autowired
    private AppConfig appConfig;

    @Bean
    public String classification() {
        return appConfig.getGitlabClassification();
    }

    private static String nullWrapper(String value){
        return value == null ? "(null)" : value;
    }

    private static String nullWrapper(String[] values){
        String ret = null;
        if(values.length > 0){
            ret = "";
            for (String value : values) {
                ret += nullWrapper(value);
            }
        }
        return ret;
    }

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
}