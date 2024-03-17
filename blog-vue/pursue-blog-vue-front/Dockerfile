FROM nginx:1.24-alpine3.17 as production-stage
COPY nginx.conf /etc/nginx/nginx.conf
COPY dist /usr/share/nginx/html
 
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
RUN echo 'init ok!!'

