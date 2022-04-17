FROM openjdk:8-jdk-alpine
LABEL maintainer ="s3843790@student.rmit.edu.au"

EXPOSE 8081

ADD target/transactionmicroservice-0.0.1-SNAPSHOT.jar transactionmicroservice-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/transactionmicroservice-0.0.1-SNAPSHOT.jar"]