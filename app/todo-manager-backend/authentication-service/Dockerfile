FROM eclipse-temurin:17-jdk-jammy AS builder
ENV JAVA_HOME=/opt/java/openjdk
WORKDIR /opt/app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ls -la
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

FROM eclipse-temurin:17-jre-jammy

WORKDIR /opt/app
EXPOSE 8081
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/opt/app/*.jar" ]