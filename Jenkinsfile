pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        APP_NAME = 'calculator'
        JAR_FILE = 'target/calculator-0.0.1-SNAPSHOT.jar'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/nandanoorsivakumar/spring-boot-calculator.git'
            }
        }

        stage('Verify Tools') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
                bat 'git --version'
            }
        }

        stage('Docker Check') {
            steps {
                bat 'docker --version'
                bat 'docker ps'
            }
        }

        stage('Clean') {
            steps {
                bat 'mvn clean'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('Verify JAR') {
            steps {
                bat '''
                    if not exist "%JAR_FILE%" (
                        echo JAR file was not created
                        exit /b 1
                    )

                    echo JAR file created successfully
                    dir target\\*.jar
                '''
            }
        }

        stage('Archive JAR') {
            steps {
                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    fingerprint: true
                )
            }
        }
    }

    post {

        success {
            echo 'Jenkins pipeline completed successfully.'
            echo 'Java, Maven, Git and Docker are available.'
            echo 'Spring Boot application was built successfully.'
        }

        failure {
            echo 'Pipeline failed. Check Jenkins Console Output.'
        }

        always {
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true
        }
    }
}