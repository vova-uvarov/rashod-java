#docker build -t rashod:v0.1.0 .
#docker tag rashod:v0.1.0 uvolodia/pets-projects:rashod_v0.1.0
#docker push uvolodia/pets-projects:rashod_v0.1.0
FROM adoptopenjdk/openjdk11:alpine-jre
ADD docker/rashod.jar rashod.jar

# Set default timezone
ENV TZ=Europe/Moscow

ENTRYPOINT java -jar rashod.jar
