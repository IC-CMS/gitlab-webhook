package cms.sre.gitlab_webhook.controller;

import cms.sre.gitlab_webhook.TestConfiguration;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class WebhookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebhookController controller;

    @Test
    public void autowiringTest(){
        Assert.assertNotNull(this.controller);
    }



    @Ignore(value = "Not working")
    @Test
    public void gitlabPushEventWithDocumentedData() throws Exception {
        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("/gitlabPushEvent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"object_kind\": \"push\",\n" +
                        "  \"before\": \"95790bf891e76fee5e1747ab589903a6a1f80f22\",\n" +
                        "  \"after\": \"da1560886d4f094c3e6c9ef40349f7d38b5d27d7\",\n" +
                        "  \"ref\": \"refs/heads/master\",\n" +
                        "  \"checkout_sha\": \"da1560886d4f094c3e6c9ef40349f7d38b5d27d7\",\n" +
                        "  \"user_id\": 4,\n" +
                        "  \"user_name\": \"John Smith\",\n" +
                        "  \"user_username\": \"jsmith\",\n" +
                        "  \"user_email\": \"john@example.com\",\n" +
                        "  \"user_avatar\": \"https://s.gravatar.com/avatar/d4c74594d841139328695756648b6bd6?s=8://s.gravatar.com/avatar/d4c74594d841139328695756648b6bd6?s=80\",\n" +
                        "  \"project_id\": 15,\n" +
                        "  \"project\":{\n" +
                        "    \"id\": 15,\n" +
                        "    \"name\":\"Diaspora\",\n" +
                        "    \"description\":\"\",\n" +
                        "    \"web_url\":\"http://example.com/mike/diaspora\",\n" +
                        "    \"avatar_url\":null,\n" +
                        "    \"git_ssh_url\":\"git@example.com:mike/diaspora.git\",\n" +
                        "    \"git_http_url\":\"http://example.com/mike/diaspora.git\",\n" +
                        "    \"namespace\":\"Mike\",\n" +
                        "    \"visibility_level\":0,\n" +
                        "    \"path_with_namespace\":\"mike/diaspora\",\n" +
                        "    \"default_branch\":\"master\",\n" +
                        "    \"homepage\":\"http://example.com/mike/diaspora\",\n" +
                        "    \"url\":\"git@example.com:mike/diaspora.git\",\n" +
                        "    \"ssh_url\":\"git@example.com:mike/diaspora.git\",\n" +
                        "    \"http_url\":\"http://example.com/mike/diaspora.git\"\n" +
                        "  },\n" +
                        "  \"repository\":{\n" +
                        "    \"name\": \"Diaspora\",\n" +
                        "    \"url\": \"git@example.com:mike/diaspora.git\",\n" +
                        "    \"description\": \"\",\n" +
                        "    \"homepage\": \"http://example.com/mike/diaspora\",\n" +
                        "    \"git_http_url\":\"http://example.com/mike/diaspora.git\",\n" +
                        "    \"git_ssh_url\":\"git@example.com:mike/diaspora.git\",\n" +
                        "    \"visibility_level\":0\n" +
                        "  },\n" +
                        "  \"commits\": [\n" +
                        "    {\n" +
                        "      \"id\": \"b6568db1bc1dcd7f8b4d5a946b0b91f9dacd7327\",\n" +
                        "      \"message\": \"Update Catalan translation to e38cb41.\",\n" +
                        "      \"timestamp\": \"2011-12-12T14:27:31+02:00\",\n" +
                        "      \"url\": \"http://example.com/mike/diaspora/commit/b6568db1bc1dcd7f8b4d5a946b0b91f9dacd7327\",\n" +
                        "      \"author\": {\n" +
                        "        \"name\": \"Jordi Mallach\",\n" +
                        "        \"email\": \"jordi@softcatala.org\"\n" +
                        "      },\n" +
                        "      \"added\": [\"CHANGELOG\"],\n" +
                        "      \"modified\": [\"app/controller/application.rb\"],\n" +
                        "      \"removed\": []\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": \"da1560886d4f094c3e6c9ef40349f7d38b5d27d7\",\n" +
                        "      \"message\": \"fixed readme\",\n" +
                        "      \"timestamp\": \"2012-01-03T23:36:29+02:00\",\n" +
                        "      \"url\": \"http://example.com/mike/diaspora/commit/da1560886d4f094c3e6c9ef40349f7d38b5d27d7\",\n" +
                        "      \"author\": {\n" +
                        "        \"name\": \"GitLab dev user\",\n" +
                        "        \"email\": \"gitlabdev@dv6700.(none)\"\n" +
                        "      },\n" +
                        "      \"added\": [\"CHANGELOG\"],\n" +
                        "      \"modified\": [\"app/controller/application.rb\"],\n" +
                        "      \"removed\": []\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"total_commits_count\": 4\n" +
                        "}");

        this.mockMvc.perform(post);
    }

}
