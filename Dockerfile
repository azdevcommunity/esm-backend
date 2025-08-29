# ---- build stage ----
FROM gradle:8.4.0-jdk21 AS build
WORKDIR /app

COPY settings.gradle build.gradle gradlew gradlew.bat /app/
COPY gradle /app/gradle
RUN chmod +x /app/gradlew && sed -i 's/\r$//' /app/gradlew
RUN ./gradlew --no-daemon dependencies || true
COPY src /app/src
RUN ./gradlew clean build -x test --no-daemon && ls -l /app/build/libs

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# (Opsiyonel) Healthcheck için curl ekleyelim
RUN apt-get update && apt-get install -y --no-install-recommends curl \
  && rm -rf /var/lib/apt/lists/*

# Varsayılan portu ENV ile tanımla (Compose/K8S ile override edilebilir)
ENV SERVER_PORT=8088

COPY --from=build /app/build/libs/medrese-0.0.1.jar /app/app.jar
VOLUME /app/uploads

# EXPOSE zorunlu değil; sadece dokümantasyon için tutabilirsin:
# EXPOSE 8088

# Healthcheck'i env'e bağlı yap (CMD-SHELL ile değişken genişler)
HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -fsS "http://localhost:${SERVER_PORT}/actuator/health" || exit 1

# Portu dinamik veriyoruz (SERVER_PORT override edilebilir)
ENTRYPOINT ["sh","-c","java -XX:MaxRAMPercentage=75 -Dserver.port=${SERVER_PORT} -jar /app/app.jar"]
