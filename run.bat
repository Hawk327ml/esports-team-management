@echo off
setlocal
REM Requires: JDK 17+, Maven, MySQL with sql/schema.sql applied
REM Optional demo data: mysql -u root -p < sql/seed_demo.sql
REM Optional: set ESPORTS_DB_PASSWORD / ESPORTS_DB_USER / ESPORTS_DB_URL

where mvn >nul 2>nul
if errorlevel 1 (
  echo Maven not found. Install Maven and ensure "mvn" is on PATH.
  exit /b 1
)

if not defined ESPORTS_DB_PASSWORD (
  echo Warning: ESPORTS_DB_PASSWORD not set. Using DatabaseConnection local fallback.
)

echo Running EsportsApp via javafx-maven-plugin...
mvn -q clean javafx:run
endlocal
