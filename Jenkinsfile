
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
                sh "./gradlew clean build -x test --info --stacktrace"
            }

        }
    }
}
