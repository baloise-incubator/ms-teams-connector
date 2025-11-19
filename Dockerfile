ARG BASE_IMAGE="quay.balgroupit.com/library/openjdk:17-jdk-alpine"
FROM ${BASE_IMAGE}
LABEL org.opencontainers.image.source="https://github.com/baloise-incubator/ms-teams-connector"

WORKDIR /application
COPY /target/ms-teams-connector*app.jar application.jar
RUN chgrp -R 0 /application && chmod -R g+rwX /application

ENTRYPOINT ["java", "-jar", "application.jar"]
