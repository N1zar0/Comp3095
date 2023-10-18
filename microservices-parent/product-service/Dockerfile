# --------------------------------
# Dockerfile for product-service
# --------------------------------

# --------
# BUILD Stage
# --------

# Base image
FROM gradle:8-jdk17 AS builder

# Copy the application files from the host machine to the image file system
COPY --chown=gradle:gradle . /home/gradle/src

# Set the working directory for executing future commands
WORKDIR /home/gradle/src

# Run gradle build within the container - NOT the host machine
RUN gradle build -x test

# --------
# PACKAGE Stage
# --------

FROM openjdk:17-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/product-service.jar

ENV MONGO_DB_USERNAME=rootadmin \
    MONGO_DB_PWD=password

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]