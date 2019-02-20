
pipeline {
    agent {
        docker { image 'gradle:5.2.1-jdk11' }
    }

    stages{
        stage('Clean') {
            steps{
                deleteDir()
            }
        }
        stage('Build') {
            steps{
                sh "gradle clean build -x test --info --stacktrace"
            }
        }
    }
}
