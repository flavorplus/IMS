FROM maven:3.5.4-jdk-10-slim as intermediate

WORKDIR /usr/src/java-code/
COPY ./pom.xml /usr/src/java-code/pom.xml
RUN mvn dependency:go-offline

FROM maven:3.5.4-jdk-10-slim

LABEL maintainer="Jonathan Vermeulen"

COPY --from=intermediate /root/.m2 /root/.m2

WORKDIR /usr/src/java-code/
COPY ./ /usr/src/java-code/
RUN mvn package

WORKDIR /usr/src/java-app
RUN cp /usr/src/java-code/target/*.jar ./app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
