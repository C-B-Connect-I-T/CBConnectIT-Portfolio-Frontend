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

        stage('Check if Build is Needed') {
            steps {
                script {
                    // Get the list of changed files in the last commit
                    def changedFiles = sh(
                        script: "git diff --name-only HEAD~1 HEAD || echo ''",
                        returnStdout: true
                    ).trim()

                    echo "Changed files in last commit:"
                    echo changedFiles

                    if (changedFiles) {
                        def files = changedFiles.split('\n')
                        def exportChanges = files.findAll { file ->
                            // Only look for changes in .kobweb-export directories
                            file.startsWith('.kobweb-export/')
                        }

                        if (exportChanges.isEmpty()) {
                            echo "=========================================="
                            echo "Skipping build: No .kobweb-export files"
                            echo "were changed. This pipeline only runs when"
                            echo "pre-built exports are updated."
                            echo "=========================================="
                            currentBuild.result = 'SUCCESS'
                            currentBuild.description = 'Skipped: No export changes'
                            env.SKIP_BUILD = 'true'
                            return
                        } else {
                            echo "Found ${exportChanges.size()} export file(s) changed"
                            echo "Proceeding with deployment..."
                            exportChanges.each { file -> echo "  - ${file}" }
                        }
                    } else {
                        echo "No files changed, skipping build"
                        currentBuild.result = 'SUCCESS'
                        currentBuild.description = 'Skipped: No changes'
                        env.SKIP_BUILD = 'true'
                        return
                    }

                    env.SKIP_BUILD = 'false'
                }
            }
        }

        stage('Determine Environment') {
            when {
                expression { env.SKIP_BUILD != 'true' }
            }
            steps {
                script {
                    // Check for tags first (staging/production require tags)
                    def tag = sh(script: "git describe --exact-match --tags HEAD || echo ''", returnStdout: true).trim()

                    def environment = null
                    def version = 'latest'

                    if (tag) {
                        echo "Tag found: ${tag}"
                        // Staging/Production ONLY deploy on tags
                        if (tag ==~ /^staging-v\d+\.\d+\.\d+$/) {
                            environment = 'staging'
                            version = tag.replaceFirst(/^staging-v/, '')
                            echo "Deploying STAGING from tag: ${tag}"
                        } else if (tag ==~ /^v\d+\.\d+\.\d+$/) {
                            environment = 'production'
                            version = tag.replaceFirst(/^v/, '')
                            echo "Deploying PRODUCTION from tag: ${tag}"
                        } else {
                            echo "Unknown tag format: ${tag}"
                            echo "Expected: 'staging-v*.*..*' or 'v*.*.*'"
                            currentBuild.result = 'SUCCESS'
                            currentBuild.description = 'Skipped: Invalid tag format'
                            env.SKIP_BUILD = 'true'
                            return
                        }
                    } else {
                        // No tag - check if develop export changed
                        def changedFiles = sh(
                            script: "git diff --name-only HEAD~1 HEAD",
                            returnStdout: true
                        ).trim()

                        if (changedFiles.contains('.kobweb-export/develop/')) {
                            environment = 'develop'
                            version = 'latest'
                            echo "Deploying DEVELOP from export changes"
                        } else {
                            echo "=========================================="
                            echo "Skipping build: No tag found and develop export was not changed."
                            echo ""
                            echo "To deploy:"
                            echo "  - DEVELOP: Push changes to .kobweb-export/develop/"
                            echo "  - STAGING: Create tag 'staging-v*.*..*'"
                            echo "  - PRODUCTION: Create tag 'v*.*.*'"
                            echo "=========================================="
                            currentBuild.result = 'SUCCESS'
                            currentBuild.description = 'Skipped: No deploy trigger'
                            env.SKIP_BUILD = 'true'
                            return
                        }
                    }

                    env.ENVIRONMENT = environment
                    env.VERSION = version
                    env.CONTAINER_NAME = "${PROJECT_NAME}-${environment}"

                    // Port mapping (adjust per project)
                    def ports = [develop: '2020', staging: '2021', production: '2022']
                    env.EXPOSED_PORT = ports[environment]

                    echo "=========================================="
                    echo "Environment: ${ENVIRONMENT}"
                    echo "Version: ${VERSION}"
                    echo "Container: ${CONTAINER_NAME}"
                    echo "Port: ${EXPOSED_PORT}"
                    echo "=========================================="
                }
            }
        }

        stage('Verify Pre-built Export') {
            when {
                expression { env.SKIP_BUILD != 'true' }
            }
            steps {
                script {
                    // Verify the export exists for this environment
                    def exportPath = ".kobweb-export/${ENVIRONMENT}"
                    def startShPath = "${exportPath}/.kobweb/server/start.sh"

                    echo "Verifying export at: ${exportPath}"
                    echo "Checking for: ${startShPath}"

                    if (!fileExists(startShPath)) {
                        // Enhanced error message with diagnostics
                        def errorMsg = """
                            ================================================================================
                            ERROR: Pre-built Kobweb export not found or incorrectly structured!
                            ================================================================================

                            Expected file: ${startShPath}
                            Environment: ${ENVIRONMENT}

                            Directory structure check:
                        """

                        echo errorMsg

                        // Show what actually exists
                        if (fileExists(exportPath)) {
                            echo "Export directory exists, but structure is wrong:"
                            sh "ls -laR ${exportPath}"
                        } else {
                            echo "Export directory does NOT exist: ${exportPath}"
                            sh "ls -la .kobweb-export/ || echo 'No .kobweb-export directory found!'"
                        }

                        def solution = """

                            Required directory structure:
                            .kobweb-export/
                              ${ENVIRONMENT}/
                                .kobweb/          ← This parent directory is REQUIRED
                                  server/
                                    start.sh
                                  site/
                                  conf.yaml
                                BUILD_INFO.txt

                            Troubleshooting steps:
                            1. Check GitHub Actions workflow ran successfully
                            2. Verify the export was committed to the repository
                            3. Ensure you're on the correct branch/tag for ${ENVIRONMENT}
                            4. Verify GitHub Actions uses: cp -r site/.kobweb \${{ env.EXPORT_DIR }}/
                               (NOT: cp -r site/.kobweb/* which loses the parent folder)

                            ================================================================================
                        """

                        error(solution)
                    }

                    // Verify additional critical files
                    def requiredFiles = [
                        "${exportPath}/.kobweb/server/start.sh",
                        "${exportPath}/.kobweb/server/server.jar",
                        "${exportPath}/.kobweb/site/system/index.html"
                    ]

                    requiredFiles.each { file ->
                        if (!fileExists(file)) {
                            echo "WARNING: Expected file missing: ${file}"
                        }
                    }

                    // Display build info if available
                    if (fileExists("${exportPath}/BUILD_INFO.txt")) {
                        def buildInfo = readFile("${exportPath}/BUILD_INFO.txt")
                        echo """
                            ================================================================================
                            Pre-built Export Information:
                            ================================================================================
                            ${buildInfo}
                            ================================================================================
                        """
                    }

                    echo "✓ Pre-built export structure verified for ${ENVIRONMENT}"
                }
            }
        }

        stage('Build Docker Image') {
            when {
                expression { env.SKIP_BUILD != 'true' }
            }
            steps {
                script {
                    echo "Building lightweight Docker image using pre-built export..."

                    // Build using the optimized Dockerfile
                    // No heavy build process - just copying pre-built files
                    sh """
                        docker build \
                          --build-arg ENVIRONMENT=${ENVIRONMENT} \
                          --build-arg KOBWEB_EXPORT_DIR=.kobweb-export \
                          -t ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION} \
                          .
                    """

                    // Tag with 'latest' for the environment
                    sh "docker tag ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION} ${PROJECT_NAME}-${ENVIRONMENT}:latest"

                    echo "✓ Docker image built in seconds (no compilation needed)"
                }
            }
        }

        stage('Stop Old Container') {
            when {
                expression { env.SKIP_BUILD != 'true' }
            }
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
            when {
                expression { env.SKIP_BUILD != 'true' }
            }
            steps {
                script {
                    sh """
                        docker run -d \\
                          --name ${CONTAINER_NAME} \\
                          --network ${DOCKER_NETWORK} \\
                          -p ${EXPOSED_PORT}:8081 \\
                          --restart unless-stopped \\
                          --memory="512m" \\
                          --memory-reservation="256m" \\
                          --cpus="0.3" \\
                          ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION}
                    """

                    echo "✓ Container deployed with minimal resource footprint"
                }
            }
        }

        stage('Cleanup Old Images') {
            when {
                expression { env.SKIP_BUILD != 'true' }
            }
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
            script {
                if (env.SKIP_BUILD == 'true') {
                    echo "✓ Build skipped successfully"
                    echo "Reason: ${currentBuild.description ?: 'No deployment trigger'}"
                } else if (env.ENVIRONMENT) {
                    echo "✓ Deployment successful for ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION}"
                    echo "Access at: http://localhost:${EXPOSED_PORT}"
                    echo ""
                    echo "Build time dramatically reduced - no Gradle/Kobweb compilation on server!"
                } else {
                    echo "✓ Build completed successfully"
                }
            }
        }
        failure {
            script {
                if (env.ENVIRONMENT) {
                    echo "✗ Deployment failed for ${PROJECT_NAME}-${ENVIRONMENT}:${VERSION}"
                    // Optionally rollback to previous image
                    try {
                        sh """
                            # Try to restart the previous container
                            PREVIOUS_IMAGE=\$(docker images ${PROJECT_NAME}-${ENVIRONMENT} --format '{{.ID}}' | sed -n '2p')
                            if [ -n "\$PREVIOUS_IMAGE" ]; then
                                echo "Attempting rollback to previous image..."
                                docker run -d \\
                                  --name ${CONTAINER_NAME} \\
                                  --network ${DOCKER_NETWORK} \\
                                  -p ${EXPOSED_PORT}:8081 \\
                                  --restart unless-stopped \\
                                  --memory="512m" \\
                                  --memory-reservation="256m" \\
                                  --cpus="0.3" \\
                                  \$PREVIOUS_IMAGE
                            fi
                        """
                    } catch (Exception e) {
                        echo "Rollback failed: ${e.message}"
                    }
                } else {
                    echo "✗ Build failed"
                }
            }
        }
        always {
            // Clean workspace but preserve .git for future builds
            cleanWs(deleteDirs: true, patterns: [[pattern: '.git/**', type: 'EXCLUDE']])
        }
    }
}
