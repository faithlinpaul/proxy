
pipeline {
    agent none

    stages{
        stage('Clean') {
            steps{
                deleteDir()
            }
        }
        stage('Build') {
            agent {
                docker {
                    image 'gradle:5.2.1-jdk11-slim'
                }
            }
            steps{
                sh "gradle clean build -x test --info --stacktrace"
            }
        }
    }
}
