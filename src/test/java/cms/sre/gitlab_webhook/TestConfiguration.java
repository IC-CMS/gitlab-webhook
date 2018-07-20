package cms.sre.gitlab_webhook;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@org.springframework.boot.test.context.TestConfiguration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class TestConfiguration extends App {


}
