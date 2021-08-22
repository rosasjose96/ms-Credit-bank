FROM openjdk:8
VOLUME /temp
EXPOSE 8090
ADD ./target/ms-Credit-bank-0.0.1-SNAPSHOT.jar credit-service.jar
ENTRYPOINT ["java","-jar","/credit-service.jar"]