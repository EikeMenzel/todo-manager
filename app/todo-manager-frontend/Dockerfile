FROM node:latest as builder

WORKDIR /usr/local/app

COPY ./ /usr/local/app/

RUN npm install

RUN npm run build --prod


# Stage 2: Serve app with nginx server

FROM nginx:latest

COPY --from=builder /usr/local/app/dist/todo-list-frontend/browser /usr/share/nginx/html

COPY ./nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
