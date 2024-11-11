FROM gradle:8.4.0-jdk17 as build

WORKDIR /app

COPY build.gradle settings.gradle /app/      
COPY gradlew* /app/
COPY src /app/src

RUN gradle build --no-daemon && ls -l /app/build/libs

FROM openjdk:17-jdk

WORKDIR /app

COPY --from=build /app/build/libs/medrese-0.0.1.jar /app/app.jar

VOLUME /app/uploads

EXPOSE 8082

CMD ["sh", "-c", "java -jar app.jar "]
