package cms.sre.gitlab_webhook;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication()
public class App {

    @Value("${gitlab.classification:UNKNOWN}")
    protected String gitlabClassification;

    @Bean
    public String classification(){
        return this.gitlabClassification;
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