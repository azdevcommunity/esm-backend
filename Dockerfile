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


# --- Stage 1: Build ---
FROM gradle:8.4.0-jdk17 as build

WORKDIR /app

# Copy only Gradle wrapper and build files first
COPY build.gradle settings.gradle gradlew gradlew.bat /app/

# This step will force Gradle to download and cache dependencies
# (You can run something less than a full build, e.g. `gradle --version`
#  or `gradle dependencies`, but a build with an empty src folder won't do harm.)
RUN gradle build --no-daemon || true

# Now copy the entire source code
COPY src /app/src

# Final build step (this reuses cached dependencies if only src changes)
RUN gradle build --no-daemon

# --- Stage 2: Runtime ---
FROM openjdk:17-jdk

WORKDIR /app
COPY --from=build /app/build/libs/medrese-0.0.1.jar /app/app.jar

VOLUME /app/uploads
EXPOSE 8082

CMD ["sh", "-c", "java -jar app.jar"]