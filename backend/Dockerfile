FROM --platform=$BUILDPLATFORM eclipse-temurin:21-jre

RUN /usr/sbin/adduser stamp-group && \
  /usr/sbin/useradd -g stamp-group -u 10001 stamp-user

USER stamp-user

COPY server.jar /app/server.jar
WORKDIR /app

EXPOSE 8082