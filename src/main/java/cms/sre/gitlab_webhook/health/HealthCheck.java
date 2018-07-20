package cms.sre.gitlab_webhook.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class HealthCheck implements HealthIndicator {
   

    @Override
    public Health health() {
    	
        Health health = null;
        
        return health;
    }
}
