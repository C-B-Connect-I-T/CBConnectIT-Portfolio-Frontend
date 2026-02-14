#-----------------------------------------------------------------------------
# Optimized Dockerfile for pre-built Kobweb exports
# This approach assumes the .kobweb directory is already built and committed
# to the repository, dramatically reducing build time and resource usage.
#-----------------------------------------------------------------------------

ARG KOBWEB_EXPORT_DIR=".kobweb-export"
# ^ Directory where pre-built exports are stored in the repo

FROM eclipse-temurin:21-jre-alpine AS run

ARG KOBWEB_EXPORT_DIR
ARG ENVIRONMENT=develop

# Install only what's needed to run (not build)
RUN apk add --no-cache curl

# Copy the pre-built export for the specific environment
# This copies the .kobweb directory from the repo to /.kobweb in the container
COPY ${KOBWEB_EXPORT_DIR}/${ENVIRONMENT}/.kobweb .kobweb

# Verify the export exists and show detailed diagnostics if not
RUN if [ ! -f ".kobweb/server/start.sh" ]; then \
      echo "==========================================="; \
      echo "ERROR: start.sh not found at expected path!"; \
      echo "==========================================="; \
      echo ""; \
      echo "Expected path: .kobweb/server/start.sh"; \
      echo "Environment: ${ENVIRONMENT}"; \
      echo "Export dir: ${KOBWEB_EXPORT_DIR}"; \
      echo ""; \
      echo "Current directory structure:"; \
      ls -laR .kobweb || echo "Failed to list .kobweb directory"; \
      echo ""; \
      echo "Possible causes:"; \
      echo "1. GitHub Actions didn't run or failed"; \
      echo "2. Export wasn't committed to ${KOBWEB_EXPORT_DIR}/${ENVIRONMENT}/"; \
      echo "3. Directory structure is wrong (missing .kobweb parent folder)"; \
      echo "4. Wrong ENVIRONMENT value: '${ENVIRONMENT}'"; \
      echo ""; \
      echo "Expected repository structure:"; \
      echo "${KOBWEB_EXPORT_DIR}/"; \
      echo "  ${ENVIRONMENT}/"; \
      echo "    .kobweb/"; \
      echo "      server/"; \
      echo "        start.sh"; \
      echo ""; \
      exit 1; \
    fi

# Make start script executable
RUN chmod +x .kobweb/server/start.sh

# Show success message with details
RUN echo "✓ Kobweb export validated successfully for ${ENVIRONMENT}" && \
    echo "  Entry point: .kobweb/server/start.sh" && \
    ls -lh .kobweb/server/start.sh

EXPOSE 8081:8081

# Use JRE with minimal memory footprint
ENV JAVA_TOOL_OPTIONS="-Xmx512m -Xms256m -XX:MaxMetaspaceSize=128m"

# Healthcheck on port 8081 (Kobweb's default port, verified in conf.yaml)
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8081/ || exit 1

ENTRYPOINT ["/bin/sh", ".kobweb/server/start.sh"]
