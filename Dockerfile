FROM amazoncorretto:21-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw clean package -DskipTests -B
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Xmx300m -jar target/*.jar"]