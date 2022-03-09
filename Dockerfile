FROM openjdk:11
WORKDIR ./app
COPY . .
ENTRYPOINT ["./mvnw", "spring-boot:run"]