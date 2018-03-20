pipeline {
    agent {
        docker {
            image 'maven:3'
            args '-v /root/.m2:/root/.m2 -v /tmp/maven_settings:/tmp/maven_settings -v /usr/bin/docker:/host/bin/docker'
        }
    }
    stages {
        stage('Build') {
            steps {
                //sh 'mvn -B -DskipTests -s /tmp/maven_settings/settings.xml clean package'
                sh 'apt-get install -y whoami'
                sh 'ls -lart /host/bin'
                //sh '/host/bin/docker build target -f Dockerfile -t sredna/gitlab-webhook:latest'
            }
        }
    }
}