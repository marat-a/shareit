FROM amazoncorretto:11-alpine-jdk
COPY target/shareit-gateway-0.0.1-SNAPSHOT-exec.jar shareit-server.jar
ENTRYPOINT ["java","-jar","/shareit-server.jar"]