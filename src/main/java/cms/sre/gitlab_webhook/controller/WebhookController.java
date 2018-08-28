package cms.sre.gitlab_webhook.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import cms.sre.gitlab_webhook.emitter.GitlabSourceControlCommitEventEmitter;
import cms.sre.gitlab_webhook.model.GitlabPushEvent;

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
        
        boolean result = gitlabSourceControlCommitEventEmitter.emitEvent(event);
        
        if (result == true) {
        	return event;
        } else {
        	
        	throw new IllegalArgumentException();
        	
        }
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    void handleBadRequests(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing required Web Hook values to fullfil request");
    }
    
}
