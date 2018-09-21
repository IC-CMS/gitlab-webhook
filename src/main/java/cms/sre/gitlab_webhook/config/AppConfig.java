package cms.sre.gitlab_webhook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="terraform.launcher")
public class AppConfig {

    private String host;

    private String port;

    private String jenkinsRequest;

    private String gitlabClassification;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getJenkinsRequest() {
        return jenkinsRequest;
    }

    public void setJenkinsRequest(String jenkinsRequest) {
        this.jenkinsRequest = jenkinsRequest;
    }

    public String getGitlabClassification() {
        return gitlabClassification;
    }

    public void setGitlabClassification(String gitlabClassification) {
        this.gitlabClassification = gitlabClassification;
    }

}
