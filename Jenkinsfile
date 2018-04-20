pipeline {
    agent {
        docker {
            image 'maven:3'
            args '-v /root/.m2:/root/.m2 -v /tmp/maven_settings:/tmp/maven_settings -v /usr/bin/docker:/usr/bin/docker -v /var/run/docker.sock:/var/run/docker.sock:ro -v /usr/lib64/libltdl.so.7:/usr/lib/x86_64-linux-gnu/libltdl.so.7 -v /var/lib/jenkins/tmp/docker_settings:/.docker'
        }
    }
    environment {
        docker_pass = credentials('Docker-Password')
    }
    stages {
        stage('Build') {
            steps {
                sh "/usr/bin/docker login -u dhessler -p $docker_pass"
                sh "mvn -B -DskipTests -s /tmp/maven_settings/settings.xml clean package"
                sh "cp ./Dockerfile target/"
                sh "/usr/bin/docker build target -f target/Dockerfile -t sredna/gitlab-webhook:DEV"
                //sh "/usr/bin/docker tag sredna/gitlab-webhook:latest sredna/gitlab-webhook:1.$BUILD_NUMBER"
                //sh "/usr/bin/docker push sredna/gitlab-webhook:latest"
                //sh "/usr/bin/docker push sredna/gitlab-webhook:1.$BUILD_NUMBER"
                sh "rm -rf /.docker/config.json"
            }
        }
    }
}
