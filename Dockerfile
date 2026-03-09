# Bước 1: Build file JAR (Dùng Maven)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copy toàn bộ code vào trong image
COPY . .
# Build ra file .jar và bỏ qua chạy Unit Test để tiết kiệm thời gian/tài nguyên
RUN mvn clean package -DskipTests

# Bước 2: Chạy ứng dụng (Dùng JRE nhẹ hơn JDK để tiết kiệm dung lượng)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy file .jar đã build từ Bước 1 sang
COPY --from=build /app/target/*.jar app.jar
# Mở cổng 8080 (cổng mặc định của Spring Boot)
EXPOSE 8080
# Bước 3: Cấu hình chạy Java tối ưu cho gói Free 512MB RAM
# -Xmx300m: Giới hạn RAM tối đa cho Java là 300MB
# -Xss512k: Giảm dung lượng mỗi luồng để tránh tràn bộ nhớ
ENTRYPOINT ["java", "-Xmx300m", "-Xss512k", "-jar", "app.jar"]



