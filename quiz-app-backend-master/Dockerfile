FROM maven:3-openjdk-17-slim

RUN mkdir -p /opt/app
WORKDIR /opt/app
COPY . /opt/app
COPY ./src/main/resources/application.properties.example /opt/app/src/main/resources/application.properties
RUN mvn -B package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "/opt/app/target/quiz-app-backend-0.0.1-SNAPSHOT.jar"]
