pipeline {
  agent {
    docker {
      image 'maven:3-jdk-8'
      args '-v /root/.m2:/root/.m2 -v /tmp/maven_settings:/tmp/maven_settings'
    }
    
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn -B -DskipTests -s /tmp/maven_settings/settings.xml clean deploy'
      }
    }
  }
}