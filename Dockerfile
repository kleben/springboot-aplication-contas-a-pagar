FROM amazoncorretto:17
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} contas-1-0.jar
ENTRYPOINT ["java","-jar","/contas-1-0.jar"]