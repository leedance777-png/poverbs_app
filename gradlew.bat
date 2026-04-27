@echo off
REM Lightweight Gradle launcher for Windows
where gradle >nul 2>nul
if errorlevel 1 (
  echo Gradle command not found. Please install Gradle or use GitHub Actions.
  exit /b 1
)
gradle %*
