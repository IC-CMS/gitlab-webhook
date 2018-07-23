# gitlab-webhook

gitlab-webhook implements the webhook for version 7 - 9 of Gitlab

## Configuration:
Expected configuration file located at /config/application.properties.  This file must have some
or all of the following properties specified

docker run --network=host --rm --name gitlab-webhook -it -p 8080:8080 -v /data/gitlab-webhook/configuration/:/config/ gitlab-webhook:test

# Service config
server.port=8080

# Logging configuration
logging.level.root=INFO
logging.level.cms.sre=DEBUG

# Application Specific Properties
terraform.launcher.host=123.123.123.123
terraform.launcher.port=8088
terraform.launcher.jenkins.request=terraform/apply
gitlab.classification=WHATEVER

#### Gitlab Settings

- gitlab.classification -- The classification of the data incoming from Gitlab.