ARG BASE_IMAGE
FROM ${BASE_IMAGE}
LABEL org.opencontainers.image.source="https://github.com/baloise-incubator/ms-teams-connector"

ARG HTTP_PROXY
ARG HTTPS_PROXY

ENV HTTP_PROXY=${HTTP_PROXY} \
    HTTPS_PROXY=${HTTPS_PROXY} \
    http_proxy=${HTTP_PROXY} \
    https_proxy=${HTTPS_PROXY}

WORKDIR /application
COPY /target/ms-teams-connector*app.jar application.jar
RUN chgrp -R 0 /application && chmod -R g+rwX /application

ENTRYPOINT ["java", "-jar", "application.jar"]