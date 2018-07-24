FROM openjdk:8-jre-alpine
MAINTAINER David Hessler <davidh.092705@gmail.com>
ADD ./docker_entrypoint.sh /usr/local/bin/
RUN chmod u+x /usr/local/bin/docker_entrypoint.sh
ENV JAVA_OPTS="-Dspring.config.location=/config/"
ENTRYPOINT ["/usr/local/bin/docker_entrypoint.sh"]
# Add Maven dependencies (not shaded into the artifact; Docker-cached)
ADD ./target/gitlab-webhook.jar /usr/share/myservice/myservice.jar
