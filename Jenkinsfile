
pipeline {
    agent 'docker-slaves'{
        image 'gradle'
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
