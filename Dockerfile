# Builder image
FROM maven:3.5-jdk-8 as builder
COPY . /home/root/build/
WORKDIR /home/root/build/
RUN mvn package -DskipTests

# Executor image
FROM openjdk:8-jre-alpine
ARG SERVICE_PORT
COPY --from=builder /home/root/build/target/*.jar \
/srv/wikitolearn-course-midtier/app.jar
WORKDIR /srv/wikitolearn-course-midtier/
EXPOSE $SERVICE_PORT
ENTRYPOINT ["java", "-jar", "app.jar"]
