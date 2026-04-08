pipeline {
    agent any

    environment {
        IMAGE_NAME = "backup-app"
        AWS_ACCESS_KEY = credentials('aws-access-key')
        AWS_SECRET_KEY = credentials('aws-secret-key')
    }

    stages {

        stage('Build') {
            steps {
                sh 'cd backend && JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'cd backend && docker build -t $IMAGE_NAME .'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                docker stop backup-app || true
                docker rm backup-app || true
                docker run -d -p 9090:9090 \
                -e AWS_ACCESS_KEY=$AWS_ACCESS_KEY \
                -e AWS_SECRET_KEY=$AWS_SECRET_KEY \
                --name backup-app $IMAGE_NAME
                '''
            }
        }
    }
}
