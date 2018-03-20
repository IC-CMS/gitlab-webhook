package cms.sre.gitlab_webhook;

import cms.sre.mongo_connection_helper.MongoClientFactory;
import cms.sre.mongo_connection_helper.MongoClientParameters;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

//If resource not found.  Value defaults to the empty string.
@PropertySource(value = "file:/data/gitlab-webhook/configuration/application.properties", ignoreResourceNotFound = true)
@SpringBootApplication()
public class App extends AbstractMongoConfiguration{

    @Value("${mongodb.databaseName:null}")
    private String mongoDatabaseName;

    @Value("${mongodb.keyStoreKeyPassword:null}")
    private String mongoKeyStoreKeyPassword;

    @Value("${mongodb.keyStoreLocation:null}")
    private String mongoKeyStoreLocation;

    @Value("${mongodb.keyStorePassword:null}")
    private String mongoKeyStorePassword;

    @Value("${mongodb.trustStoreLocation:null}")
    private String mongoTrustStoreLocation;

    @Value("${mongodb.trustStorePassword:null}")
    private String mongoTrustStorePassword;

    @Value("${mongodb.username:null}")
    private String mongoUsername;

    @Value("${mongodb.password:null}")
    private String mongoPassword;

    @Value("${mongodb.replicaSetLocation:null}")
    private String[] mongoReplicaSetLocation;

    @Value("${mongodb.mongoReplicaSetName:null}")
    private String mongoReplicaSetName;

    @Value("${gitlab.classification:UNKNOWN}")
    private String gitlabClassification;

    @Bean
    public String classification(){
        return this.gitlabClassification;
    }

    @Override
    public MongoClient mongoClient() {
        MongoClientParameters params = new MongoClientParameters()
                .setKeyStoreKeyPassword(this.mongoKeyStoreKeyPassword)
                .setKeyStoreLocation(this.mongoKeyStoreLocation)
                .setKeyStorePassword(this.mongoKeyStorePassword)
                .setTrustStoreLocation(this.mongoTrustStoreLocation)
                .setTrustStorePassword(this.mongoTrustStorePassword)
                .setDatabaseName(this.mongoDatabaseName)
                .setMongoUsername(this.mongoUsername)
                .setMongoPassword(this.mongoPassword)
                .setReplicaSetLocations(this.mongoReplicaSetLocation)
                .setReplicaSetName(this.mongoReplicaSetName);

//        return MongoClientFactory.getLocalhostMongoClient();
        return MongoClientFactory.getMongoClient(params);
    }

    @Override
    protected String getDatabaseName() {
        return this.mongoDatabaseName;
    }

    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
}
