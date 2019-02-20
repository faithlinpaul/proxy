
pipeline {
    agent none

    stages{

        stage('Build') {
            agent {
                docker {
                    image 'gradle'
                }
            }
            steps{
                sh "ls -lta"
                sh "./gradlew clean build -x test --info --stacktrace"
            }
        }
    }
}
