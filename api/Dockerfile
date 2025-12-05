# ThingsBoard API - Multi-stage Dockerfile
# Supports: development (hot reload) and production

ARG JDK_IMAGE=eclipse-temurin:jdk-alpine
ARG JRE_IMAGE=eclipse-temurin:jre-alpine

# ============================================
# Stage 1: Dependencies Cache
# ============================================
FROM ${JDK_IMAGE} AS deps

WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .
COPY application/pom.xml application/
COPY common/ common/
COPY dao/pom.xml dao/
COPY rule-engine/pom.xml rule-engine/
COPY transport/ transport/
COPY msa/ msa/
COPY netty-mqtt/pom.xml netty-mqtt/
COPY rest-client/pom.xml rest-client/
COPY tools/pom.xml tools/
COPY edqs/pom.xml edqs/
COPY monitoring/pom.xml monitoring/

RUN mvn dependency:go-offline -B -q

# ============================================
# Stage 2: Builder
# ============================================
FROM deps AS builder

COPY . .

RUN mvn clean package -DskipTests -pl application -am -q \
    && mv application/target/*.jar /app/app.jar \
    && rm -rf ~/.m2

# ============================================
# Stage 3: Extractor (for layered jar)
# ============================================
FROM ${JDK_IMAGE} AS extractor

WORKDIR /app
COPY --from=builder /app/app.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract

# ============================================
# Stage 4: Development Runtime
# ============================================
FROM ${JDK_IMAGE} AS development

RUN apk add --no-cache maven openssl

WORKDIR /app

# Source code and .env mounted via docker-compose volumes

EXPOSE 8080 5005

CMD ["mvn", "spring-boot:run", \
     "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", \
     "-Dspring-boot.run.profiles=dev", \
     "-pl", "application", "-am"]

# ============================================
# Stage 5: Production Runtime
# ============================================
FROM ${JRE_IMAGE} AS production

RUN apk add --no-cache curl openssl tzdata \
    && rm -rf /var/cache/apk/*

ENV TZ=UTC

WORKDIR /app

RUN addgroup -g 1000 -S thingsboard && \
    adduser -u 1000 -S thingsboard -G thingsboard

COPY --from=extractor /app/dependencies/ ./
COPY --from=extractor /app/spring-boot-loader/ ./
COPY --from=extractor /app/snapshot-dependencies/ ./
COPY --from=extractor /app/application/ ./

COPY .env.production /app/.env
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

RUN chown -R thingsboard:thingsboard /app

USER thingsboard

ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
    CMD curl -sf http://localhost:8080/api/actuator/health || exit 1

ENTRYPOINT ["/app/entrypoint.sh"]

