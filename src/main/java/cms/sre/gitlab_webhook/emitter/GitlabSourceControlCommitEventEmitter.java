package cms.sre.gitlab_webhook.emitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cms.sre.dna_common_data_model.sourceControl.SourceControlCommitEvent;
import cms.sre.gitlab_webhook.model.GitLabCommit;
import cms.sre.gitlab_webhook.model.GitlabPushEvent;

@Service
public class GitlabSourceControlCommitEventEmitter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private String classification;

	@Value("${terraform.launcher.host}")
	protected String terraformLauncherHost;

	@Value("${terraform.launcher.port}")
	protected String terraformLauncherPort;

	@Value("${terraform.launcher.jenkins.request}")
	protected String terraformLauncheJenkiinsRequest;

	private Calendar lastModifiedDate(List<GitLabCommit> commits) {
		Calendar ret = null;
		if (commits.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Calendar time;
			try {
				time = Calendar.getInstance();
				time.setTime(sdf.parse(commits.get(0).getTimestamp()));

				for (GitLabCommit c : commits) {
					Calendar possible = Calendar.getInstance();
					possible.setTime(sdf.parse(c.getTimestamp()));
					if (time.before(possible)) {
						time = possible;
					}
				}

				ret = time;
			} catch (ParseException e) {
				e.printStackTrace();
				// This is a best guess.
				ret = Calendar.getInstance();
			}
		} else {
			ret = Calendar.getInstance();
		}
		return ret;
	}

	private int numberOfChanges(List<GitLabCommit> commits) {
		List<String> altersFiles = new ArrayList<String>();
		List<String> emptyStringList = new LinkedList<String>();
		for (GitLabCommit c : commits) {
			List<String> added = c.getAdded() == null ? emptyStringList : c.getAdded();
			altersFiles = ListUtils.union(added, altersFiles);

			List<String> removed = c.getRemoved() == null ? emptyStringList : c.getRemoved();
			altersFiles = ListUtils.union(removed, altersFiles);

			List<String> modified = c.getModified() == null ? emptyStringList : c.getModified();
			altersFiles = ListUtils.union(modified, altersFiles);
		}
		return altersFiles.size();
	}

	public boolean emitEvent(GitlabPushEvent event) {
		
		logger.debug("Object Kind: " + event.getObject_kind());
		logger.debug("Project Name: " + event.getProject());
		logger.debug("Repository: " + event.getRepository());

		String name = event.getProject() == null ? null : event.getProject().getName();

		if (name == null) {
		     name = event.getRepository() == null ? null : event.getRepository().getName();
		}
		
		String sslUrl = event.getProject() == null ? null : event.getProject().getSsh_url();
		
		if (sslUrl == null) {
			
			sslUrl = event.getRepository() == null ? null : event.getRepository().getGit_ssh_url();
		}
		
		if (sslUrl == null || name == null) {
			
			logger.error("Missing properties required for request, unable to fullfil request");
			return false;
		}

		SourceControlCommitEvent emittedEvent = new SourceControlCommitEvent(this.classification, "gitlab-webhook")
				.setBranchName(event.getRef()).setRepositoryName(name).setSshUrl(sslUrl)
				.setUserEmail(event.getUser_email()).setUserName(event.getUser_name());

		emittedEvent.setRevisionNumber(event.getAfter());
		emittedEvent.setPullNumber(event.getBefore());
		emittedEvent.setNumberOfChanges(this.numberOfChanges(event.getCommits()));
		emittedEvent.setTimestamp(this.lastModifiedDate(event.getCommits()));

		JSONObject request = new JSONObject();

		request.put("object_kind", event.getObject_kind());
		request.put("project_name", name);
		request.put("ssl_url", sslUrl);
		request.put("classification", this.classification);
		request.put("branch_name", event.getRef());
		request.put("user_email", event.getUser_email());
		request.put("user_name", event.getUser_name());
		request.put("revsion_number", event.getAfter());
		request.put("pull_number", event.getBefore());
		request.put("timestamp", emittedEvent.getTimestamp().getTime());
		
		logger.debug("Request JSON: " + request.toString(4));

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(request.toString(4), headers);
		
		try {

		    ResponseEntity<String> buildResponse = restTemplate.exchange("http://" + this.terraformLauncherHost + ":" + this.terraformLauncherPort + this.terraformLauncheJenkiinsRequest,
			    	HttpMethod.POST, entity, String.class);

		    if (buildResponse.getStatusCode() == HttpStatus.OK) {
  
	      		logger.info("Successfully processed request");

		    } else if (buildResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {

			logger.error("Unable to process request");

		    }
		    
		} catch (Exception e) {
			
			logger.error("Connection Error: " + e.getMessage());
			
		}
		
		return true;
	}	
}
