pipeline {
    agent {
        docker {
            image 'maven:3'
            args '-v /root/.m2:/root/.m2 -v /tmp/maven_settings:/tmp/maven_settings -v /usr/bin/docker:/usr/bin/docker -v /var/run/docker.sock:/var/run/docker.sock:ro -v /usr/lib64/libltdl.so.7:/usr/lib/x86_64-linux-gnu/libltdl.so.7'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests -s /tmp/maven_settings/settings.xml clean package'
                sh '/usr/bin/docker build target -f Dockerfile -t sredna/gitlab-webhook:latest'
            }
        }
    }
}