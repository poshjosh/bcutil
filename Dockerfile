# Repo: https://github.com/poshjosh/bcutil
# ----------------------------------------
FROM openjdk:8-jdk-alpine
RUN ls -a && cd .. && ls -a
VOLUME /tmp
ARG SERVER_PORT
RUN test -z "${SERVER_PORT}" || EXPOSE "${SERVER_PORT}" && :
ARG DEPENDENCY_DIR=target/dependency
COPY ${DEPENDENCY_DIR}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY_DIR}/META-INF /app/META-INF
COPY ${DEPENDENCY_DIR}/BOOT-INF/classes /app
COPY start.sh .
ARG JAVA_OPTS
ARG MAIN_CLASS
ENTRYPOINT ["start.sh"]
