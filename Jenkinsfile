pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }
    
  }
  stages {
    stage('Log whoami'){
        steps{
            sh 'whoami'
        }
    }

    stage('Setup Maven') {
      steps {
        sh 'curl -o  /root/.m2/settings.xml https://raw.githubusercontent.com/IC-CMS/maven-settings/master/settings.xml '
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -B -DskipTests clean package'
      }
    }
  }
}