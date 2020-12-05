FROM openjdk:8-jre-alpine as builder
LABEL maintainer="Vitalijus Dobrovolskis vitalijusdobro@gmail.com"
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} commexp.jar
RUN java -Djarmode=layertools -jar commexp.jar extract

FROM openjdk:8-jre-alpine
RUN apk add --no-cache gettext
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=builder dependencies/ ./
RUN true
COPY --from=builder snapshot-dependencies/ ./
RUN true
COPY --from=builder spring-boot-loader/ ./
RUN true
COPY --from=builder application/ ./

USER root
RUN chown spring:spring /BOOT-INF/classes/static/assets/env.js
USER spring:spring

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]