FROM gradle:7-jdk11 AS builder
WORKDIR /build
ADD . /build
RUN gradle build

FROM openjdk:11-slim
ENV ENCRYPT_SECRET=""
WORKDIR /app
COPY --from=builder /build/build/libs/backend.jar /app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]