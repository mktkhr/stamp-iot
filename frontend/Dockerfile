# 開発環境用
FROM --platform=$BUILDPLATFORM nginx:1.27.0

COPY ./nginx.conf /etc/nginx/nginx.conf
COPY ./dist /usr/share/nginx/dist

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]