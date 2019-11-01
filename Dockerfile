FROM openjdk:11-slim as runtime
VOLUME /tmp
COPY /build/libs/imvp.jar app.jar
COPY ./package.json package.json
RUN apt-get update
RUN apt-get -y install curl gnupg
RUN curl -sL https://deb.nodesource.com/setup_11.x  | bash -
RUN apt-get -y install nodejs
RUN npm install
ENTRYPOINT java -jar app.jar