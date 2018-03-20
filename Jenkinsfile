pipeline {
  agent none
   stages {
     stage('Build') {
		agent {
			docker {
			  image 'maven:3-alpine'
			  args '-v /root/.m2:/root/.m2 -v /tmp/maven_settings:/tmp/maven_settings -v /usr/bin/docker:/host/bin/docker'
			}
		}
	   steps {
		 //sh 'mvn -B -DskipTests -s /tmp/maven_settings/settings.xml clean package'
		 sh '/host/bin/docker'
		 //sh '/host/bin/docker build target -f Dockerfile -t sredna/gitlab-webhook:latest'
	   }
	 }
   }
 }