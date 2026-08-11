@echo off
setlocal
REM Requires: JDK 17+, Maven, MySQL with sql/schema.sql applied
REM Optional: set ESPORTS_DB_PASSWORD before running

where mvn >nul 2>nul
if errorlevel 1 (
  echo Maven not found. Install Maven and ensure "mvn" is on PATH.
  exit /b 1
)

echo Running EsportsApp via javafx-maven-plugin...
mvn -q clean javafx:run
endlocal
