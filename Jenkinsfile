
pipeline {
    agent any
    stages{
        stage('Clean') {
            deleteDir()
        }
        stage('Checkout') {
            checkout scm
        }
        stage('Build') {
            sh "gradle clean build -x test --info --stacktrace"
        }
    }
}
