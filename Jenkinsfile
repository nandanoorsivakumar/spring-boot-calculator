pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        APP_NAME = 'calculator'
        IMAGE_NAME = 'spring-boot-calculator'
        IMAGE_TAG = '1.0'
        CONTAINER_NAME = 'calculator-container'
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
                bat 'docker --version'
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

                    echo JAR created successfully
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

        stage('Docker Build') {
            steps {
                bat '''
                    echo Building Docker image...
                    docker build -t %IMAGE_NAME%:%IMAGE_TAG% .
                '''
            }
        }

        stage('Stop Old Container') {
            steps {
                bat '''
                    docker stop %CONTAINER_NAME% 2>nul || exit /b 0
                '''
            }
        }

        stage('Remove Old Container') {
            steps {
                bat '''
                    docker rm %CONTAINER_NAME% 2>nul || exit /b 0
                '''
            }
        }

        stage('Run Docker Container') {
            steps {
                bat '''
                    docker run -d ^
                      --name %CONTAINER_NAME% ^
                      -p 9191:8080 ^
                      %IMAGE_NAME%:%IMAGE_TAG%
                '''
            }
        }

        stage('Verify Docker Deployment') {
            steps {
                bat '''
                    echo Waiting for application to start...

                    ping 127.0.0.1 -n 11 > nul

                    curl --fail "http://localhost:9191/api/calculator/add?a=10&b=20"

                    if errorlevel 1 (
                        echo Docker deployment verification failed
                        docker logs %CONTAINER_NAME%
                        exit /b 1
                    )

                    echo Docker deployment successful
                '''
            }
        }
    }

    post {

        success {
            echo 'CI/CD pipeline completed successfully.'
            echo 'Spring Boot application deployed using Docker.'
        }

        failure {
            echo 'Pipeline failed. Check Console Output.'
        }

        always {
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true
        }
    }
}