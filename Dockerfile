FROM openjdk:21-jdk-slim

WORKDIR /etc/udayhegde/load-balancer

COPY build/libs/*.jar app.jar

ENV JAVA_OPTS="-Xmx512m -Xms256m"

CMD ["java", "$JAVA_OPTS", "-Dserver.port=8081", "-Dmicronaut.environments=test", "-jar", "app.jar"]

EXPOSE 8081
