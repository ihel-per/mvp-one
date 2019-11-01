FROM openjdk:11-slim as runtime
VOLUME /tmp
COPY /build/libs/imvp.jar app.jar
ENTRYPOINT java -jar app.jar
