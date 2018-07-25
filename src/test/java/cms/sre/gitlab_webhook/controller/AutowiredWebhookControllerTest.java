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
import java.util.stream.Stream;

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

        Path path = Paths.get("src/test/resources/gitlab_webhook.json");

        logger.info("Webhook Test file path found located at " + path);

        if(Files.exists(path)){

            try (Stream<String> input = Files.lines(path)) {
                input.forEach(stringBuilder::append);

            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
        else {
            logger.error("File not found at: " + path.toString());
            System.exit(-1);
        }

    }

    @Test
    public void contextLoadsTest(){
        Assertions.assertThat(this.controller)
                .isNotNull();
    }

    @Test
    public void getGitlabPushEvent(){

        logger.debug("Request: "  + stringBuilder.toString());

        String response = this.testRestTemplate.postForEntity("http://localhost:"+this.port+"/gitlabPushEvent", stringBuilder.toString(), String.class)
                .getBody();

        Assertions.assertThat(response)
                .isNotNull()
                .isNotEmpty()
                .contains("{\"object_kind\":\"push\",\"before\":\"95790bf891e76fee5e1747ab589903a6a1f80f22\",\"after\":\"da1560886d4f094c3e6c9ef40349f7d38b5d27d7\"," +
                        "\"ref\":\"refs/heads/master\",\"checkout_sha\":null,\"user_id\":4,\"user_name\":\"John Smith\"," +
                        "\"user_username\":null,\"user_email\":\"john@example.com\",\"user_avatar\":null,\"project_id\":15," +
                        "\"project\":null,\"repository\":{\"name\":\"docker-jenkins\",\"url\":\"git@example.com:mike/diasporadiaspora.git\"," +
                        "\"description\":\"\",\"homepage\":\"http://example.com/mike/diaspora\",\"git_http_url\":\"http://example.com/mike/diaspora.git\"," +
                        "\"git_ssh_url\":\"git@example.com:mike/diaspora.git\",\"visibility_level\":0},\"commits\":[{\"id\":\"b6568db1bc1dcd7f8b4d5a946b0b91f9dacd7327\"," +
                        "\"message\":\"Update Catalan translation to e38cb41.\",\"timestamp\":\"2011-12-12T14:27:31+02:00\"," +
                        "\"url\":\"http://example.com/mike/diaspora/commit/b6568db1bc1dcd7f8b4d5a946b0b91f9dacd7327\",\"author\":{\"name\":\"Joe Schmoe\"," +
                        "\"email\":\"Joe Schmoe\"},\"added\":null,\"modified\":null,\"removed\":null},{\"id\":\"da1560886d4f094c3e6c9ef40349f7d38b5d27d7\"," +
                        "\"message\":\"fixed readme\",\"timestamp\":\"2012-01-03T23:36:29+02:00\"," +
                        "\"url\":\"http://example.com/mike/diaspora/commit/da1560886d4f094c3e6c9ef40349f7d38b5d27d7\",\"author\":{\"name\":\"GitLab dev user\"," +
                        "\"email\":\"gitlabdev@dv6700.(none)\"},\"added\":null,\"modified\":null,\"removed\":null}],\"total_commits_count\":4}");
    }

}
