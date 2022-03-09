FROM openjdk:11
WORKDIR /app
COPY . .
ENTRYPOINT ["app/mvnw", "spring-boot:run"]