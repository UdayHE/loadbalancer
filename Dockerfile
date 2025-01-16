FROM openjdk:21-jdk

RUN  mkdir -p /etc/udayhegde/load-balancer

WORKDIR /etc/udayhegde/load-balancer

ADD build/libs/*.jar ./

CMD java $JAVA_OPTS -Dserver.port=8081 -Dmicronaut.environments=test -jar *.jar

EXPOSE 8081