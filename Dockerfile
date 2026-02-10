# --- Étape 1 : Build ---
# On utilise l'image Gradle officielle avec JDK 21
FROM gradle:8.6-jdk21 AS build
WORKDIR /app

# Optimisation du cache : on copie les fichiers de config d'abord
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# Compilation du projet
COPY src ./src
RUN gradle bootJar --no-daemon -x test

# --- Étape 2 : Run ---
# On utilise le JRE 21 (plus léger que le JDK complet)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Sécurité : utilisateur non-root 'spring'
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Récupération du JAR optimisé
COPY --from=build /app/build/libs/*.jar app.jar

# Port d'écoute par défaut
EXPOSE 8080

# Configuration pour Java 21 et Kubernetes
# On active le support des conteneurs et on limite la mémoire
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Xmx512m", "-jar", "app.jar"]