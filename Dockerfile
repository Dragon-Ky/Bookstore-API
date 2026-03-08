# Giai đoạn 1: Build mã nguồn (Sử dụng JDK 21 hoặc bản mới nhất 2026)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy file cấu hình trước để cache các thư viện (dependencies)
COPY pom.xml .
RUN mvc dependency:go-offline

# Copy toàn bộ code và đóng gói thành file .jar
COPY src ./src
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy ứng dụng (Chỉ cần JRE siêu nhẹ)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy file .jar đã build từ Giai đoạn 1 sang
COPY --from=build /app/target/*.jar app.jar

# Mở cổng 8080 (cổng mặc định của Spring Boot)
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]