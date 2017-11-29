FROM openjdk:8-jre-alpine
EXPOSE 8888

LABEL version="0.5.0"

ADD conf/ece.conf ece.conf
ADD target/elastest-cost-engine-0.0.2-SNAPSHOT.jar ece.jar
ADD run.sh run.sh

RUN chmod +x run.sh

CMD ["/bin/ash", "run.sh"]
