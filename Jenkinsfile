pipeline {
  agent none
   stages {

     /*stage('Build') {
		agent {
			docker {
			  image 'maven:3-alpine'
			  args '-v /root/.m2:/root/.m2 -v /tmp/maven_settings:/tmp/maven_settings'
			}
		}
	   steps {
		 sh 'mvn -B -DskipTests -s /tmp/maven_settings/settings.xml clean install'

	   }
	 }*/
     stage('Publish'){
        agent any
        steps{
            sh 'pwd'
            //sh 'docker build target -f Dockerfile -t sredna/gitlab-webhook:latest'
        }
     }
   }
 }