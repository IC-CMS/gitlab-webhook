package cms.sre.gitlab_webhook.controller;

import cms.sre.gitlab_webhook.App;
import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AutowiredWebhookControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private WebhookController controller;

    private static final Logger logger = LoggerFactory.getLogger(AutowiredWebhookControllerTest.class);

    private static StringBuilder stringBuilder = new StringBuilder();


    @BeforeClass
    public static void beforeClass(){
        String appPath = System.getenv("user.dir");
        Path path = Paths.get(appPath + "/src/test/resources/gitlab_webhook.json");
        logger.debug("Webhook Test file path found located at " + path);

        if(Files.exists(path)){
            try {
                Files.lines(path)
                        .forEach(stringBuilder::append);
            } catch (IOException e) {
                logger.error(e.getStackTrace().toString());
            }
        }
    }

    @Test
    public void contextLoadsTest(){
        Assertions.assertThat(this.controller)
                .isNotNull();
    }

    @Test
    public void getGitlabPushEvent(){
        String response = this.testRestTemplate.postForEntity("http://localhost:"+this.port+"/gitlabPushEvent", stringBuilder.toString(), String.class)
                .getBody();

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty();
                //TODO .contains("")
    }

}
