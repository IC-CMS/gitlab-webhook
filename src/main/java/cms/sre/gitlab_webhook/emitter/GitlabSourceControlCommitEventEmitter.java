package cms.sre.gitlab_webhook.emitter;

import cms.sre.dna_common_data_model.sourceControl.SourceControlCommit;
import cms.sre.dna_common_data_model.sourceControl.SourceControlCommitEvent;
import cms.sre.gitlab_webhook.model.GitLabCommit;
import cms.sre.gitlab_webhook.model.GitlabPushEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GitlabSourceControlCommitEventEmitter {
    @Autowired
    private MongoTemplate template;

    @Value("${gitlab.classification:UNKNOWN}")
    private String classification;

    //TODO: Constructors!!!
    
    public void emitEvent(GitlabPushEvent event){
        String name = event.getProject() == null ? event.getRepository().getName() : event.getProject().getName();
        String sslUrl = event.getProject() == null ? event.getRepository().getGit_ssh_url() : event.getProject().getSsh_url();
        
        SourceControlCommitEvent emittedEvent = new SourceControlCommitEvent(this.classification, "gitlab-webhook")
                .setBranchName(event.getRef())
                .setRepositoryName(name)
                .setSshUrl(sslUrl)
                .setUserEmail(event.getUser_email())
                .setUserName(event.getUser_name());

        List<SourceControlCommit> commits = new LinkedList<SourceControlCommit>();
        event.getCommits().forEach(gitLabCommit -> {
            SourceControlCommit sourceControlCommit = new SourceControlCommit()
                    .setAdded(gitLabCommit.getAdded())
                    .setCommitor(gitLabCommit.getAuthor().getName())
                    .setMessage(gitLabCommit.getMessage())
                    .setModified(gitLabCommit.getModified())
                    .setRemoved(gitLabCommit.getRemoved())
                    .setTimestamp(gitLabCommit.getTimestamp());

            commits.add(sourceControlCommit);
        });

        this.template.save(emittedEvent);
    }
}
