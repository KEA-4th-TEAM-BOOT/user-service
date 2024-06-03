pipeline {
    agent any

    tools {
        gradle 'gradle-7.6.1'
        jdk 'jdk-17'
    }

    environment {
        DOCKERHUB_USERNAME = 'codrin2'
        GITHUB_URL = 'https://github.com/KEA-4th-TEAM-BOOT/user-service.git'
        APP_VERSION = '1.1.1'
        BUILD_DATE = sh(script: "echo `date +%y%m%d.%d%H%M`", returnStdout: true).trim()
        TAG = "${APP_VERSION}"
        IMAGE_NAME = 'voda-user'
        SERVICE_NAME = 'user'
        ECR_REPOSITORY = 'voda-user' // AWS ECR 리포지토리 이름
        AWS_REGION = 'ap-northeast-2' // AWS 리전
        AWS_ACCOUNT_ID = '981883772993' // AWS 계정 ID
    }

    stages {
        stage('소스파일 체크아웃') {
            steps {
                script {
                    // BRANCH_NAME 환경 변수가 설정되어 있지 않으면 'main'을 사용
                    env.BRANCH_NAME = env.BRANCH_NAME ?: 'main'
                    checkout([$class: 'GitSCM', branches: [[name: "*/${env.BRANCH_NAME}"]], userRemoteConfigs: [[url: GITHUB_URL, credentialsId: 'github-signin']]])
                }
            }
        }

        stage('소스 빌드') {
            steps {
                sh "chmod +x ./gradlew"
                sh "./gradlew clean build -x test"
            }
        }

        stage('컨테이너 빌드 및 업로드') {
            steps {
                script {
                    sh "cp ./build/libs/${SERVICE_NAME}-0.0.1-SNAPSHOT.jar ./app-0.0.1-SNAPSHOT.jar"

                    if (env.BRANCH_NAME == 'main') {
                        // DockerHub 로그인
                        withCredentials([usernamePassword(credentialsId: 'docker_password', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                            sh '''
                                echo $PASSWORD | docker login -u $USERNAME --password-stdin
                            '''
                        }

                        // DockerHub에 이미지 빌드 및 푸시
                        sh "docker build -t ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG} ."
                        sh "docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}"

                        // 로컬 Docker 이미지 삭제
                        sh "docker rmi ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}"
                    } else if (env.BRANCH_NAME == 'prod') {
                        // AWS ECR 로그인
                        withCredentials([string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'), string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY')]) {
                            sh '''
                                aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                                aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                                aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com
                            '''
                        }

                        // AWS ECR에 이미지 빌드 및 푸시
                        sh "docker build -t ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:${TAG} ."
                        sh "docker tag ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:${TAG} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:latest"
                        sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:${TAG}"
                        sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:latest"

                        // 로컬 Docker 이미지 삭제
                        sh "docker rmi ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:${TAG}"
                        sh "docker rmi ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPOSITORY}:latest"
                    }
                }
            }
        }
    }

    post {
        always {
            sh "docker logout"
            // AWS 자격 증명 환경 변수 지우기
            sh "unset AWS_ACCESS_KEY_ID"
            sh "unset AWS_SECRET_ACCESS_KEY"
        }
    }
}
