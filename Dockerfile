# ---- build stage ----
FROM gradle:8.4.0-jdk21 AS build
WORKDIR /app

# Gradle cache için önce sadece tanımlar
COPY settings.gradle build.gradle gradlew gradlew.bat /app/
COPY gradle /app/gradle

# Bağımlılıkları indir (cache)
RUN ./gradlew dependencies --no-daemon || true

# Kaynak kod
COPY src /app/src

# Build
RUN ./gradlew clean build -x test --no-daemon && ls -l /app/build/libs

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# JAR'ı kopyala (adını kontrol et)
COPY --from=build /app/build/libs/medrese-0.0.1.jar /app/app.jar

# Uploads için volume
VOLUME /app/uploads

# Uygulama varsayılan 8080
EXPOSE 8080

# Basit healthcheck (actuator/health varsa orayı kullan)
HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health || exit 1

# Port'u env ile değiştirebilir: SERVER_PORT
ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75 -Dserver.port=${SERVER_PORT:-8080} -jar /app/app.jar"]
