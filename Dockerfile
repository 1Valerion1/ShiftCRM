FROM openjdk:17-jdk-slim
COPY ./build/libs/ShiftCRM-1.0.jar /opt/service.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://database:5430/crm
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
EXPOSE 8081
CMD java -jar /opt/service.jar