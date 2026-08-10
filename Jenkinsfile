pipeline {

    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }

    environment {
        APP_NAME = 'calculator'
        IMAGE_NAME = 'spring-boot-calculator'
        IMAGE_TAG = "${BUILD_NUMBER}"
        JAR_FILE = 'target/calculator-0.0.1-SNAPSHOT.jar'

        DEPLOYMENT_NAME = 'calculator-deployment'
        CONTAINER_NAME = 'calculator-container'
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
                bat 'kubectl version --client'
                bat 'kubectl config current-context'
                bat 'kubectl get nodes'
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

        stage('Verify Kubernetes') {
            steps {
                bat '''
                    kubectl config current-context
                    kubectl get nodes
                    kubectl get deployment %DEPLOYMENT_NAME%
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                bat '''
                    echo Deploying image %IMAGE_NAME%:%IMAGE_TAG%

                    kubectl set image deployment/%DEPLOYMENT_NAME% ^
                    %CONTAINER_NAME%=%IMAGE_NAME%:%IMAGE_TAG%
                '''
            }
        }

        stage('Wait for Kubernetes Rollout') {
            steps {
                bat '''
                    kubectl rollout status deployment/%DEPLOYMENT_NAME% --timeout=120s
                '''
            }
        }

        stage('Verify Kubernetes Deployment') {
            steps {
                bat '''
                    echo Current Kubernetes image:

                    kubectl get deployment %DEPLOYMENT_NAME% ^
                    -o=jsonpath="{.spec.template.spec.containers[0].image}"

                    echo.
                    echo Kubernetes Pods:
                    kubectl get pods -l app=calculator -o wide
                '''
            }
        }
    }

    post {

        success {
            echo 'CI/CD pipeline completed successfully.'
            echo 'Spring Boot application deployed to Kubernetes.'
        }

        failure {
            echo 'Pipeline failed. Check Console Output.'

            bat '''
                echo Kubernetes deployment status:
                kubectl get deployment %DEPLOYMENT_NAME%

                echo Pods:
                kubectl get pods -l app=calculator

                echo Recent Kubernetes events:
                kubectl get events --sort-by=.metadata.creationTimestamp
            '''
        }

        always {
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true
        }
    }
}