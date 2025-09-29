pipeline {
    agent any

    environment {
        IMAGE_NAME = "portfolio-frontend"
    }

    stages {
        stage('Determine Environment') {
            steps {
                script {
                    sh 'git fetch --tags'

                    def tag = sh(script: "git describe --exact-match --tags HEAD || echo ''", returnStdout: true).trim()
                    def branch = env.GIT_BRANCH?.replaceFirst(/^origin\//, '') ?: 'develop'

                    echo "Branch: ${branch}"
                    echo "Tag: ${tag}"

                    def environment = 'develop'
                    def version = 'latest'

                    if (tag) {
                        if (tag ==~ ~/^staging-v\d+\.\d+\.\d+$/) {
                            environment = 'staging'
                            version = tag.replaceFirst(/^staging-v/, '')
                        } else if (tag ==~ ~/^v\d+\.\d+\.\d+$/) {
                            environment = 'production'
                            version = tag.replaceFirst(/^v/, '')
                        } else {
                            error("Unknown tag format: ${tag}")
                        }
                    } else if (branch == 'main' || branch == 'master') {
                        environment = 'develop'
                    } else {
                        echo "Skipping build: Not a relevant branch or tag"
                        currentBuild.result = 'SUCCESS'
                        return
                    }

                    env.ENVIRONMENT = environment
                    env.VERSION = version
                    env.CONTAINER_NAME = "${IMAGE_NAME}-${environment}"

                    env.EXPOSED_PORT = environment == 'production' ? '2022' :
                                       environment == 'staging' ? '2021' : '2020'

                    env.BASE_URL = environment == 'production' ? 'https://cb-connect-it.com' :
                                   environment == 'staging' ? 'https://stag.cb-connect-it.com' :
                                   'https://dev.cb-connect-it.com'

                    echo "Version: ${VERSION}"
                    echo "Exposed port: ${EXPOSED_PORT}"
                    echo "Environment: ${ENVIRONMENT}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                // Provide the BASE_URL as a build argument to the Dockerfile so it's available during the build
                sh "docker build --build-arg BASE_URL=${BASE_URL} -t ${IMAGE_NAME}-${ENVIRONMENT}:${VERSION} ."
            }
        }

        stage('Deploy to Docker') {
            steps {
                script {
                    sh "docker stop ${CONTAINER_NAME} || true"
                    sh "docker rm ${CONTAINER_NAME} || true"
                    sh "docker run -d --name ${CONTAINER_NAME} -p ${EXPOSED_PORT}:8081 ${IMAGE_NAME}-${ENVIRONMENT}:${VERSION}"
                }
            }
        }
    }
}
