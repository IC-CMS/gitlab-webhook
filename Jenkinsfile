pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2 -u root:root'
    }
    
  }
  stages {
    stage('Fetch Settings') {
      agent any
      steps {
        sh 'mkdir -p ~/maven_settings'
        sh 'curl https://raw.githubusercontent.com/IC-CMS/maven-settings/master/settings.xml -o ~/tmp/maven_settings/settings.xml'
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -B -DskipTests clean package -s ~/maven_settings/settings.xml'
      }
    }
    stage('Clean up') {
      agent any
      steps {
        sh 'rm -rf ~/maven_settings'
      }
    }
  }
}