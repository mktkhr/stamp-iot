FROM --platform=linux/arm64 amazoncorretto:21-alpine AS build

COPY ./ /
RUN ./gradlew build -x test

FROM --platform=linux/arm64 eclipse-temurin:21.0.3_9-jre-ubi9-minimal

ARG POSTGRES_USER
ARG POSTGRES_PASSWORD
ARG REDIS_HOST
ARG REDIS_PORT

ENV POSTGRES_USER=${POSTGRES_USER}
ENV POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
ENV REDIS_HOST=${REDIS_HOST}
ENV REDIS_PORT=${REDIS_PORT}

COPY --from=build /build/libs/server.jar /app/server.jar

WORKDIR /

EXPOSE 8082
CMD [ "/bin/sh","-c","POSTGRES_DB_USER=${POSTGRES_USER} POSTGRES_DB_PASS=${POSTGRES_PASSWORD} REDIS_HOST=${REDIS_HOST} REDIS_PORT=${REDIS_PORT} java -jar /app/server.jar --spring.profiles.active=prod" ]