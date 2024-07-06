FROM --platform=$BUILDPLATFORM eclipse-temurin:21.0.3_9-jre-ubi9-minimal

RUN /usr/sbin/adduser stamp-group && /usr/sbin/useradd -g stamp-group -u 10001 stamp-user
RUN mkdir logs && chown -R stamp-user logs

COPY server.jar /app/server.jar
WORKDIR /app

USER stamp-user

EXPOSE 8082