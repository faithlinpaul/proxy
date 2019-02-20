
pipeline {
    agent ecs
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
