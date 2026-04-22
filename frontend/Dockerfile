FROM node:20 AS build
WORKDIR /app
COPY . .
RUN npm install && npm run build --configuration=production

FROM nginx:alpine
COPY --from=build /app/dist/angular-app/browser /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
