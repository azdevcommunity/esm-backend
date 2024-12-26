#FROM gradle:8.4.0-jdk17 as build
#
#WORKDIR /app
#
#COPY build.gradle settings.gradle /app/
#COPY gradlew* /app/
#COPY src /app/src
#
#RUN gradle build --no-daemon && ls -l /app/build/libs
#
#FROM openjdk:17-jdk
#
#WORKDIR /app
#
#COPY --from=build /app/build/libs/medrese-0.0.1.jar /app/app.jar
#
#VOLUME /app/uploads
#
#EXPOSE 8082
#
#CMD ["sh", "-c", "java -jar app.jar "]
FROM gradle:8.4.0-jdk17 as build

WORKDIR /app

# Step 1: Copy only the files necessary for dependency resolution
COPY build.gradle settings.gradle gradlew* /app/

# Step 2: Download dependencies
RUN gradle dependencies --no-daemon

# Step 3: Copy the remaining source files
COPY src /app/src

# Step 4: Build the application
RUN gradle build --no-daemon && ls -l /app/build/libs

# Step 5: Create the runtime image
FROM openjdk:17-jdk

WORKDIR /app

# Step 6: Copy the built JAR from the build stage
COPY --from=build /app/build/libs/medrese-0.0.1.jar /app/app.jar

# Define volume for uploads
VOLUME /app/uploads

# Expose the application port
EXPOSE 8082

# Define the default command
CMD ["sh", "-c", "java -jar app.jar"]
