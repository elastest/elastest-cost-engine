FROM openjdk:8-jre-alpine
COPY target/elastest-cost-engine-0.0.1-SNAPSHOT.jar /ece.jar
CMD java -jar /ece.jar