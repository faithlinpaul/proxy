
node('ecs') {
        try {
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
        catch (error) {
            throw error
        }
    }
}