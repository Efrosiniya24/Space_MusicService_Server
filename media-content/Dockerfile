FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

COPY . .

RUN mvn -pl media-content -am clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# ffprobe (из состава ffmpeg) нужен для POST /track/probe-metadata в AudioMetadataProbeService
RUN apk add --no-cache ffmpeg

COPY --from=builder /app/media-content/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
