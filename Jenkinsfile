pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Setup Maven'){
            steps {
                sh 'curl https://raw.githubusercontent.com/IC-CMS/maven-settings/master/settings.xml | tee /root/.m2/settings.xml > /dev/null'
            }
        }


        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
    }
}