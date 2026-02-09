pipeline {
    agent any

    environment {
        PROJECT_NAME = "portfolio-frontend"
        DOCKER_NETWORK = "infrastructure_app-network"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Determine Environment') {
            steps {
                script {
                    def tag = sh(script: "git describe --exact-match --tags HEAD || echo ''", returnStdout: true).trim()
                    def branch = env.GIT_BRANCH?.replaceFirst(/^origin\//, '') ?: 'main'

                    echo "Branch: ${branch}"
                    echo "Tag: ${tag}"

                    def environment = 'develop'
                    def version = 'latest'

                    if (tag) {
                        if (tag ==~ /^staging-v\d+\.\d+\.\d+$/) {
                            environment = 'staging'
                            version = tag.replaceFirst(/^staging-v/, '')
                        } else if (tag ==~ /^v\d+\.\d+\.\d+$/) {
                            environment = 'production'
                            version = tag.replaceFirst(/^v/, '')
                        } else {
                            error("Unknown tag format: ${tag}")
                        }
                    } else if (branch == 'main' || branch == 'master') {
                        environment = 'develop'
                        version = 'latest'
                    } else {
                        echo "Skipping build: Not a relevant branch or tag"
                        currentBuild.result = 'SUCCESS'
                        return
                    }

                    env.ENVIRONMENT = environment
                    env.VERSION = version
                    env.CONTAINER_NAME = "${PROJECT_NAME}-${environment}"

                    // Port mapping (adjust per project)
                    def ports = [develop: '2020', staging: '2021', production: '2022']
                    env.EXPOSED_PORT = ports[environment]

                    // Base URL configuration for each environment
                    def baseUrls = [
                        develop: 'https://dev.cb-connect-it.com',
                        staging: 'https://stag.cb-connect-it.com',
                        production: 'https://cb-connect-it.com'
                    ]
                    env.BASE_URL = baseUrls[environment]

                    echo "Environment: ${ENVIRONMENT}"
                    echo "Version: ${VERSION}"
                    echo "Container: ${CONTAINER_NAME}"
                    echo "Port: ${EXPOSED_PORT}"
                    echo "Base URL: ${BASE_URL}"
                }
            }
        }

        stage('Run Detekt Analysis') {
            steps {
                script {
                    sh './gradlew detekt --no-daemon'
                }
            }
            post {
                always {
                    recordIssues(
                        tools: [detekt(pattern: '**/build/reports/detekt/detekt.xml')]
                    )
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image with BASE_URL: ${BASE_URL}"

                    // Build the Docker image with BASE_URL as a build argument
                    // The Dockerfile will handle the Kobweb export internally
                    sh """
                        docker build \
                          --build-arg BASE_URL=${BASE_URL} \
                          --memory="3g" \
                          --memory-swap="3g" \
                          -t ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION} \
                          .
                    """

                    // Tag with 'latest' for the environment
                    sh "docker tag ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION} ${PROJECT_NAME}-${ENVIRONMENT}:latest"
                }
            }
        }

        stage('Stop Old Container') {
            steps {
                script {
                    sh """
                        docker stop ${CONTAINER_NAME} || true
                        docker rm ${CONTAINER_NAME} || true
                    """
                }
            }
        }

        stage('Deploy Container') {
            steps {
                script {
                    sh """
                        docker run -d \\
                          --name ${CONTAINER_NAME} \\
                          --network ${DOCKER_NETWORK} \\
                          -p ${EXPOSED_PORT}:8081 \\
                          --restart unless-stopped \\
                          --memory="512m" \\
                          --cpus="0.5" \\
                          ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION}
                    """
                }
            }
        }

        stage('Cleanup Old Images') {
            steps {
                script {
                    // Keep only the last 3 images per environment
                    sh """
                        docker images ${PROJECT_NAME}-${ENVIRONMENT} --format '{{.ID}} {{.CreatedAt}}' | \\
                        sort -k2 -r | \\
                        tail -n +4 | \\
                        awk '{print \$1}' | \\
                        xargs -r docker rmi || true
                    """
                }
            }
        }
    }

    post {
        success {
            echo "✓ Deployment successful for ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION}"
            echo "Access at: http://localhost:${EXPOSED_PORT}"
        }
        failure {
            echo "✗ Deployment failed for ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION}"
            // TODO: Implement rollback logic
        }
        always {
            // Clean workspace
            cleanWs()
        }
    }
}

