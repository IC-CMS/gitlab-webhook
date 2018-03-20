pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2 -v /tmp/maven_settings:/tmp/maven_settings'
    }
    
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn -B -DskipTests clean package -s /tmp/maven_settings/settings.xml'
      }
    }
  }
}