pipeline {
    agent any

    environment {
        IMAGE_NAME = "backup-app"
    }

    stages {

        stage('Clone') {
            steps {
                git 'git@github.com:yadakrishna245/backup-app.git'
            }
        }

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
                sh 'docker stop backup-app || true'
                sh 'docker rm backup-app || true'
                sh 'docker run -d -p 9090:9090 --name backup-app $IMAGE_NAME'
            }
        }
    }
}
