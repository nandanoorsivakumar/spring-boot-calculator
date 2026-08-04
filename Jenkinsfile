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

                    echo JAR file created successfully:
                    dir target\\*.jar
                '''
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    fingerprint: true
                )
            }
        }

        stage('Stop Existing Application') {
            steps {
                bat '''
                    echo Checking whether port 9191 is in use...

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :9191 ^| findstr LISTENING') do (
                        echo Stopping process %%a
                        taskkill /PID %%a /F
                    )

                    exit /b 0
                '''
            }
        }

        stage('Deploy Locally') {
            steps {
                bat '''
                    echo Copying JAR to deployment folder...

                    if not exist "C:\\jenkins-deploy\\calculator" (
                        mkdir "C:\\jenkins-deploy\\calculator"
                    )

                    copy /Y "target\\calculator-0.0.1-SNAPSHOT.jar" ^
                            "C:\\jenkins-deploy\\calculator\\calculator.jar"

                    echo Starting Spring Boot application on port 9191...

                    start "calculator-app" /B java -jar ^
                      "C:\\jenkins-deploy\\calculator\\calculator.jar" ^
                      --server.port=9191 ^
                      > "C:\\jenkins-deploy\\calculator\\application.log" 2>&1
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                bat '''
                    timeout /t 10 /nobreak

                    curl --fail ^
                      "http://localhost:9191/api/calculator/add?a=10&b=20"

                    if errorlevel 1 (
                        echo Deployment verification failed
                        exit /b 1
                    )

                    echo Application deployed successfully
                '''
            }
        }
    }

    post {
        success {
            echo 'CI pipeline completed successfully.'
            echo 'The Spring Boot JAR was built and archived.'
        }

        failure {
            echo 'Pipeline failed. Check the Jenkins Console Output.'
        }

        always {
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true
        }
    }
}