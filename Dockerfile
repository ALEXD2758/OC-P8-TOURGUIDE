FROM openjdk:8-alpine
WORKDIR /usr/app
COPY build/libs/tourGuide-1.0.0.jar tourguide-main.jar
CMD ["java", "-jar", "tourguide-main.jar"]