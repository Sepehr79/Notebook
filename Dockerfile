FROM openjdk:11
WORKDIR ./
COPY . .
CMD ["./mvnw", "spring-boot:run"]