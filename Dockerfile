#docker build -t rashod:v0.1.1 .
#docker tag rashod:v0.1.1 uvolodia/pets-projects:rashod_v0.1.1
#docker push uvolodia/pets-projects:rashod_v0.1.1
FROM adoptopenjdk/openjdk11:alpine-jre
ADD docker/rashod.jar rashod.jar

# Set default timezone
ENV TZ=Europe/Moscow

ENTRYPOINT java -jar rashod.jar
