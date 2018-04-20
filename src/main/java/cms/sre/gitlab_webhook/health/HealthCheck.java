package cms.sre.gitlab_webhook.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.mongo.MongoHealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class HealthCheck implements HealthIndicator {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Health health() {
        Health health = null;
        if (this.mongoTemplate != null) {
            health = new MongoHealthIndicator(this.mongoTemplate)
                    .health();
        } else {
            health = Health.down()
                    .withDetail("MongoTemplate-Autowired", false)
                    .build();
        }
        return health;
    }
}
