FROM openjdk:8-jdk-alpine
LABEL maintainer ="s3843790@student.rmit.edu.au"

EXPOSE 8080

ADD target/appmicroservice-0.0.1-SNAPSHOT.jar appmicroservice-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/appmicroservice-0.0.1-SNAPSHOT.jar"]