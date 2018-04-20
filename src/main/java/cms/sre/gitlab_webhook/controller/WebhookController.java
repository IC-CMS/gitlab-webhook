package cms.sre.gitlab_webhook.controller;

import cms.sre.gitlab_webhook.emitter.GitlabSourceControlCommitEventEmitter;
import cms.sre.gitlab_webhook.model.GitlabPushEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/gitlabPushEvent")
public class WebhookController {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private GitlabSourceControlCommitEventEmitter gitlabSourceControlCommitEventEmitter;

    @RequestMapping(method = RequestMethod.GET)
    public String get(){
        return "{status: 'ok'}";
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody GitlabPushEvent post(@RequestBody String requestBody) throws IOException {
        GitlabPushEvent event = mapper.readValue(requestBody, GitlabPushEvent.class);
        this.gitlabSourceControlCommitEventEmitter.emitEvent(event);
        return event;
    }
}
