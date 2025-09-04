# ---------- Build stage ----------
FROM amazoncorretto:21-alpine AS build
WORKDIR /workspace/app

# Copy build files
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle gradle

# Ensure wrapper is executable; print versions (debug-friendly)
RUN chmod +x gradlew \
 && ./gradlew --version || (cat gradle/wrapper/gradle-wrapper.properties && exit 1)

# Warm dependency cache for faster subsequent builds
RUN ./gradlew --no-daemon dependencies || true

# Copy source code
COPY src src

# Debug: List the source directory to verify files are copied correctly
RUN ls -la src/main/java/com/readstack/library/

# Build the application
RUN ./gradlew build --no-daemon -x test

# ---------- Runtime stage ----------
# Corretto doesn't ship a separate JRE; using JDK as runtime is fine.
FROM amazoncorretto:21-alpine AS runtime
VOLUME /tmp
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /workspace/app/build/libs/*.jar app.jar

# Debug: List the app directory to verify JAR is copied
RUN ls -la /app/

# Run the application with explicit main class
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.main.allow-bean-definition-overriding=true"]
