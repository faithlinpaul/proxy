
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
         stage('Test') {
                agent {
                          docker {
                              image 'gradle'
                          }
                 }
           steps{
                sh "test"
                //sh "gradle check --info --stacktrace"
            }

         }
         stage('BootJar') {
              agent {
                 docker {
                     image 'gradle'
                 }
              }
             steps{
                 sh "./gradlew bootJar -x test --info --stacktrace"
             }
          }
         stage('Docker Image') {
            agent {
                docker {
                    image 'gradle'
                }
            }
            steps{
                sh "./gradlew docker -x test --info --stacktrace"
            }

         }

    }
}
