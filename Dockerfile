FROM maven:3.9.9-eclipse-temurin-23-alpine as build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests -DskipLaunch4j

FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

COPY --from=build /app/target/TbbCoin-1.0-SNAPSHOT.jar /app/TbbCoin.jar

COPY --from=build /app/target/data /app/data

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/TbbCoin.jar", "serve", "--port", "8080", "--dir", "data"]