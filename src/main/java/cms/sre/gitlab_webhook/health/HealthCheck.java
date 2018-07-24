package cms.sre.gitlab_webhook.health;

import cms.sre.gitlab_webhook.config.AppConfig;
import cms.sre.gitlab_webhook.controller.WebhookController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ComponentScan("cms.sre")
public class HealthCheck implements HealthIndicator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    AppConfig appConfig;

    @Autowired
    WebhookController webhookController;

    @Autowired
    public HealthCheck(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public Health health() {

        int errorCode = checkWebHookController(); // perform some specific health check

        if (errorCode != 0) {
            return Health.down()
                    .withDetail("WebhookController Error", errorCode).build();
        }

        errorCode = this.checkApplicationConfig();

        if (errorCode != 0) {
            return Health.down()
                    .withDetail("ApplicationConfig Error", errorCode).build();
        }
        return Health.up().build();
    }


    private int checkWebHookController() {

        if (webhookController == null) {
            return 1;
        }

        return 0;
    }

    private int checkApplicationConfig() {

        if (((appConfig.getGitlabClassification() == null || appConfig.getGitlabClassification().equals("")) ||
                (appConfig.getHost() == null || appConfig.getHost().equals("")) ||
                (appConfig.getPort() == null || appConfig.getPort().equals("")) ||
                (appConfig.getJenkinsRequest() == null || appConfig.getJenkinsRequest().equals("")))) {

            return 1;

        } else {

            return 0;
        }
    }
}
