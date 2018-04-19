package cms.sre.gitlab_webhook.controller;

import cms.sre.gitlab_webhook.emitter.GitlabSourceControlCommitEventEmitter;
import cms.sre.gitlab_webhook.model.GitlabPushEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.ws.RequestWrapper;

@Controller
public class WebhookController {

    @Autowired
    private GitlabSourceControlCommitEventEmitter gitlabSourceControlCommitEventEmitter;

    @RequestMapping("/gitlabPushEvent")
    public GitlabPushEvent gitlabPushEvent(GitlabPushEvent event){
        this.gitlabSourceControlCommitEventEmitter.emitEvent(event);
        return event;
    }
}
