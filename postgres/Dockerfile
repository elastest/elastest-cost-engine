FROM postgres:9.6.4-alpine

EXPOSE 5432:5432

ADD db_init.sh db_init.sh

RUN chmod +x db_init.sh

CMD ["/bin/ash", "db_init.sh"]
