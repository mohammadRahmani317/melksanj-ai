# Use an official OpenJDK image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copy the rest of the project
COPY . ./

RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the port Render will use
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "target/*.jar"]
