@echo off
REM Script de compilación para MusiCar2
REM Compila todos los archivos Java del proyecto

echo.
echo ================================================
echo Compilando MusiCar2...
echo ================================================
echo.

cd /d "%~dp0"

REM Crear carpeta build si no existe
if not exist "build\classes" (
    echo Creando carpeta build\classes...
    mkdir build\classes
)

REM Limpiar archivos compilados anteriores
echo Limpiando compilaciones anteriores...
del /S /Q build\classes\* 2>nul

REM Compilar
echo Compilando...
if defined JAVA_HOME (
    "%JAVA_HOME%\bin\javac" -d build\classes -cp src ^
        src\musicar2\FramePrincipal.java ^
        src\org\netbeans\lib\awtextra\AbsoluteLayout.java ^
        src\org\netbeans\lib\awtextra\AbsoluteConstraints.java
) else (
    javac -d build\classes -cp src ^
        src\musicar2\FramePrincipal.java ^
        src\org\netbeans\lib\awtextra\AbsoluteLayout.java ^
        src\org\netbeans\lib\awtextra\AbsoluteConstraints.java
)

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ================================================
    echo Compilación exitosa!
    echo ================================================
    echo.
    echo La aplicación está lista para ejecutar.
    echo Haz doble clic en "run.bat" para iniciar.
    echo.
) else (
    echo.
    echo ================================================
    echo Error durante la compilación!
    echo ================================================
    echo.
)

pause
