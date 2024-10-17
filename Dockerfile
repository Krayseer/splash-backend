FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY ../pom.xml .

COPY splash-api/pom.xml splash-api/
COPY config-server/pom.xml config-server/
COPY discovery/pom.xml discovery/
COPY gateway/pom.xml gateway/

COPY services/business-order/pom.xml services/business-order/
COPY services/configuration/pom.xml services/configuration/
COPY services/notification/pom.xml services/notification/
COPY services/order/pom.xml services/order/
COPY services/service/pom.xml services/service/
COPY services/statistics/pom.xml services/statistics/
COPY services/storage/pom.xml services/storage/
COPY services/user/pom.xml services/user/

RUN mvn dependency:go-offline

COPY splash-api/src splash-api/src
COPY config-server/src services/config-server/src
COPY discovery/src services/discovery/src
COPY gateway/src services/gateway/src

COPY services/business-order/src services/business-order/src
COPY services/configuration/src services/configuration/src
COPY services/notification/src services/notification/src
COPY services/order/src services/order/src
COPY services/service/src services/service/src
COPY services/statistics/src services/statistics/src
COPY services/storage/src services/storage/src
COPY services/user/src services/user/src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /app/core-service/target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
