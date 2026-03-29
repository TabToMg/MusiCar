@echo off
REM Script para ejecutar la aplicación MusiCar2
REM Esta aplicación organiza archivos de música para sistemas de coche

cd /d "%~dp0"

REM Verificar que existen las carpetas necesarias
if not exist "build\classes" (
    echo Error: No existe la carpeta build\classes. Ejecute el compilador primero.
    pause
    exit /b 1
)

REM Ejecutar la aplicación
java -cp "build\classes;src" musicar2.FramePrincipal

pause
