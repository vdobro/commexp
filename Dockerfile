FROM openjdk:11-jre-buster as builder
WORKDIR application
COPY setup_env.sh .
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11-jre-buster
LABEL maintainer="Vitalijus Dobrovolskis vitalijusdobro@gmail.com"
EXPOSE 8080

VOLUME /tmp
RUN apt-get update && apt-get install -y gettext-base && apt-get clean && rm -rf /var/cache/apt/* && rm -rf /var/lib/apt/lists/* && rm -rf /tmp/*
RUN addgroup --system spring && useradd -g spring spring
USER spring:spring

WORKDIR /application/work
WORKDIR /application

COPY --from=builder application/setup_env.sh ./
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

USER root
RUN chown spring:spring BOOT-INF/classes/static/assets/env.js
RUN mkdir -p /application/work/index && chown -R spring:spring /application/work
VOLUME /application/work
USER spring:spring

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
