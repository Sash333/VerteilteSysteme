FROM openjdk:8-alpine
RUN mkdir -p /opt/washifyService
WORKDIR /opt/washifyService
COPY target/washify-service-0.0.1.jar /opt/washifyService
EXPOSE 8080
CMD ["java", "-jar", "washify-service-0.0.1.jar"]