@echo off
echo ===================================================
echo Starting QMA Full Stack Local Development Environment
echo ===================================================

echo [1/5] Starting Eureka Server...
start "Eureka Server" cmd /k "cd /d "%~dp0eureka-server" && mvn spring-boot:run"
echo Waiting 25 seconds for Eureka to start...
timeout /t 25 /nobreak > nul

echo [2/5] Starting API Gateway...
start "API Gateway" cmd /k "cd /d "%~dp0api-gateway" && mvn spring-boot:run"
echo Waiting 20 seconds for API Gateway...
timeout /t 20 /nobreak > nul

echo [3/5] Starting Auth Service...
start "Auth Service" cmd /k "cd /d "%~dp0auth-service" && mvn spring-boot:run"
echo Waiting 15 seconds for Auth Service...
timeout /t 15 /nobreak > nul

echo [4/5] Starting Conversion Service...
start "Conversion Service" cmd /k "cd /d "%~dp0conversion-service" && mvn spring-boot:run"
echo Waiting 15 seconds for Conversion Service...
timeout /t 15 /nobreak > nul

echo [5/5] Starting React Frontend...
cd /d "%~dp0quantity-measurement-react"
if not exist "node_modules\" (
    echo node_modules not found. Installing dependencies...
    call npm install
)
start "React Frontend" cmd /k "npm run dev"
cd /d "%~dp0"

echo ===================================================
echo All services are launching in separate windows!
echo.
echo URLs:
echo   Eureka Dashboard  : http://localhost:8761
echo   API Gateway       : http://localhost:8080
echo   Auth Service      : http://localhost:8082
echo   Conversion Service: http://localhost:8081
echo   React Frontend    : http://localhost:5173
echo.
echo It may take 1-2 minutes for all services to fully start.
echo ===================================================
pause
