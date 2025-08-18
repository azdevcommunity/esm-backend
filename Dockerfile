# ---- build stage ----
FROM gradle:8.4.0-jdk21 AS build
WORKDIR /app

COPY settings.gradle build.gradle gradlew gradlew.bat /app/
COPY gradle /app/gradle

# gradlew'ye çalıştırma izni ver + olası CRLF satır sonlarını düzelt
RUN chmod +x /app/gradlew && sed -i 's/\r$//' /app/gradlew

# (opsiyonel ama faydalı) bağımlılıkları önceden indir, cache ısınsın
RUN ./gradlew --no-daemon dependencies || true

# kaynak   kod
COPY src /app/src

# build
RUN ./gradlew clean build -x test --no-daemon && ls -l /app/build/libs

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/medrese-0.0.1.jar /app/app.jar
VOLUME /app/uploads
EXPOSE 8080
HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["sh","-c","java -XX:MaxRAMPercentage=75 -Dserver.port=${SERVER_PORT:-8080} -jar /app/app.jar"]
