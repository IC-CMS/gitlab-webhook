pipeline {
  agent none
  
  
  stages {
    stage('Fetch Settings') {
      agent any
      steps {
        sh 'mkdir -p ~/tmp/maven_settings'
        sh 'curl https://raw.githubusercontent.com/IC-CMS/maven-settings/master/settings.xml -o ~/tmp/maven_settings/settings.xml'
      }
    }
    stage('Build') {
      agent {
        docker {
          image 'maven:3-alpine'
          args '-v /root/.m2:/root/.m2 ~/tmp/maven_settings:/usr/share/maven/ref/'
        }
    
      }
      steps {
        sh 'mvn -B -DskipTests clean package -s /usr/share/maven/ref/settings.xml'
      }
    }
    stage('Clean up'){
      agent any
      steps {
        sh 'rm -rf ~/tmp'
      }
    }
  }
}
