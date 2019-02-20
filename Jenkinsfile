
pipeline {
    agent none
    checkout scm

    stages{

        stage('Build') {
            agent {
                docker {
                    image 'gradle:5.2.1-jdk11-slim'
                }
            }
            steps{
                sh "ls -lta"
                sh "gradle clean build -x test --info --stacktrace"
            }
        }
    }
}
