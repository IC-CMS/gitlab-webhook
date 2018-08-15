package cms.sre.gitlab_webhook.controller;

import cms.sre.gitlab_webhook.App;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AutowiredWebhookControllerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(15352); // No-args constructor defaults to port 8080

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
    public void getGitlabPushEvent() {

        logger.debug("Request: " + stringBuilder.toString());

        stubFor(post(urlEqualTo("/terraform/apply")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json"))
                .withRequestBody(equalToJson("{" +
                              "\"user_email\": \"john@example.com\"," +
                              "\"ssl_url\": \"git@example.com:mike/diaspora.git\"," +
                              "\"revision_number\": \"da1560886d4f094c3e6c9ef40349f7d38b5d27d7\"," +
                              "\"user_name\": \"John Smith\", \"pull_number\": \"95790bf891e76fee5e1747ab589903a6a1f80f22\"," +
                              "\"branch_name\": \"refs/heads/master\", \"project_name\": \"docker-jenkins\"," +
                              "\"classification\": \"WHATEVER\"," +
                              "\"object_kind\": \"push\", \"timestamp\": \"Tue Jan 03 23:36:29 UTC 2012\"" +
                              "}"))
                .withRequestBody(notMatching("<status>ERROR</status>"))
                .willReturn(aResponse().withStatus(200)));

        int statusCodeValue = this.testRestTemplate.postForEntity("http://localhost:" + this.port + "/gitlabPushEvent", stringBuilder.toString(), String.class).getStatusCodeValue();

        Assert.assertEquals(200, statusCodeValue);
    }

    @Test
    public void exactUrlOnly() {

        stubFor(post(urlEqualTo("/terraform/apply"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("")));

        assertThat(this.testRestTemplate.postForEntity("http://localhost:" + this.port + "/gitlabPushEvent", stringBuilder.toString(), String.class).getStatusCodeValue(), is(200));

    }

    @Test
    public void bodyMatching() {
        stubFor(post(urlEqualTo("/terraform/apply")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json"))
                .withRequestBody(matching("<status>OK</status>"))
                .withRequestBody(notMatching("<status>ERROR</status>"))
                .willReturn(aResponse().withStatus(200)));
    }

}
