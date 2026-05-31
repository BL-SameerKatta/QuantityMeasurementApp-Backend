@echo off
echo Building Eureka Server...
cd eureka-server
call mvn clean package -DskipTests
cd ..

echo Building API Gateway...
cd api-gateway
call mvn clean package -DskipTests
cd ..

echo Building Auth Service...
cd auth-service
call mvn clean package -DskipTests
cd ..

echo Building Conversion Service...
cd conversion-service
call mvn clean package -DskipTests
cd ..

echo Build complete!
