FROM --platform=$BUILDPLATFORM nginx:1.27.0

ARG CONF_PATH

RUN touch /var/log/nginx/access.log && \
  touch /var/log/nginx/error.log

COPY $CONF_PATH /etc/nginx/nginx.conf

COPY ./dist /usr/share/nginx/dist

EXPOSE 80