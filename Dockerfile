FROM gradle:7-jdk11 AS builder
WORKDIR /build
ADD . /build
RUN gradle build

FROM openjdk:11-slim
ENV SSL_CERT=""
ENV SSL_KEY=""
ENV ENCRYPT_SECRET=""
ENV DOCKER="true"
WORKDIR /app
COPY run.sh /app
COPY --from=builder /build/build/libs/backend.jar /app

EXPOSE 8080
EXPOSE 8443
ENTRYPOINT ["bash", "/app/run.sh"]