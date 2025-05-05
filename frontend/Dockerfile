FROM nginxinc/nginx-unprivileged:1.27.5

USER nginx

COPY ./nginx.conf /etc/nginx/nginx.conf
COPY ./dist /usr/share/nginx/dist

EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]