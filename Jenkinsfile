
pipeline {
    agent any
    stages{
        stage('Clean') {
            steps{
                deleteDir()
            }
        }
        stage('Checkout') {
            steps{
                checkout scm
            }
        }
        stage('Build') {
            steps{
                sh "gradle clean build -x test --info --stacktrace"
            }
        }
    }
}
