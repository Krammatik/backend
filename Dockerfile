FROM gradle:7-jdk11 AS builder
WORKDIR /build
ADD . /build
RUN gradle build

FROM openjdk:11-slim
WORKDIR /app
COPY run.sh /app
COPY --from=builder /build/build/libs/backend.jar /app

EXPOSE 8080
ENTRYPOINT ["bash", "/app/run.sh"]