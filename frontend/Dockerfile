FROM nginx:1.25.5

ARG ENVIRONMENT

RUN touch /var/log/nginx/access.log && \
  touch /var/log/nginx/error.log

COPY ./nginx.conf /etc/nginx/nginx.conf

COPY ./dist /usr/share/nginx/dist

EXPOSE 80