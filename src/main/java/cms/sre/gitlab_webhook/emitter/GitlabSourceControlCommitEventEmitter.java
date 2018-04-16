package cms.sre.gitlab_webhook.emitter;

import cms.sre.dna_common_data_model.sourceControl.SourceControlCommit;
import cms.sre.dna_common_data_model.sourceControl.SourceControlCommitEvent;
import cms.sre.gitlab_webhook.model.GitLabCommit;
import cms.sre.gitlab_webhook.model.GitlabPushEvent;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GitlabSourceControlCommitEventEmitter {
    @Autowired
    private MongoTemplate template;

    @Autowired
    private String classification;

    private Calendar lastModifiedDate(List<GitLabCommit> commits){
        Calendar ret = null;
        if(commits.size() > 0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
            Calendar time;
            try{
                time = Calendar.getInstance();
                time.setTime(sdf.parse(commits.get(0).getTimestamp()));

                for(GitLabCommit c : commits){
                    Calendar possible = Calendar.getInstance();
                    possible.setTime(sdf.parse(c.getTimestamp()));
                    if(time.before(possible)){
                        time = possible;
                    }
                }

                ret = time;
            } catch (ParseException e){
                e.printStackTrace();
                //This is a best guess.
                ret  = Calendar.getInstance();
            }
        } else {
            ret = Calendar.getInstance();
        }
        return ret;
    }

    private int numberOfChanges(List<GitLabCommit> commits){
        List<String> altersFiles = new ArrayList<String>();
        List<String> emptyStringList = new LinkedList<String>();
        for(GitLabCommit c : commits){
            List<String> added = c.getAdded() == null ? emptyStringList : c.getAdded();
            altersFiles = ListUtils.union(added, altersFiles);

            List<String> removed = c.getRemoved() == null ? emptyStringList : c.getRemoved();
            altersFiles = ListUtils.union(removed, altersFiles);

            List<String> modified = c.getModified() == null ? emptyStringList : c.getModified();
            altersFiles = ListUtils.union(modified, altersFiles);
        }
        return altersFiles.size();
    }

    public void emitEvent(GitlabPushEvent event){
        String name = event.getProject() == null ? event.getRepository().getName() : event.getProject().getName();
        String sslUrl = event.getProject() == null ? event.getRepository().getGit_ssh_url() : event.getProject().getSsh_url();
        
        SourceControlCommitEvent emittedEvent = new SourceControlCommitEvent(this.classification, "gitlab-webhook")
                .setBranchName(event.getRef())
                .setRepositoryName(name)
                .setSshUrl(sslUrl)
                .setUserEmail(event.getUser_email())
                .setUserName(event.getUser_name());

        emittedEvent.setRevisionNumber(event.getAfter());
        emittedEvent.setPullNumber(event.getBefore());
        emittedEvent.setNumberOfChanges(this.numberOfChanges(event.getCommits()));
        emittedEvent.setTimestamp(this.lastModifiedDate(event.getCommits()));


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
