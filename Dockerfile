FROM frolvlad/alpine-oraclejdk8:8.161.12-slim
LABEL maintainer="elastest-users@googlegroups.com"
LABEL version="0.5.0"
LABEL description="Builds the ece docker image."

EXPOSE 8888
COPY ece/target/cost-engine-0.1.0.jar /ece.jar
COPY ece/application.properties /application.properties
ADD start.sh start.sh
RUN chmod +x start.sh
CMD ["/bin/ash", "start.sh"]
