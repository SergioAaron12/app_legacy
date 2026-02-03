@echo off
setlocal
cd /d "%~dp0"
echo === Deteniendo Microservicios Legacy Frame ===
echo.

echo Buscando procesos Java de Spring Boot...
taskkill /F /FI "WINDOWTITLE eq Auth Service*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Productos Service*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Pedidos Service*" 2>nul
taskkill /F /FI "WINDOWTITLE eq Contacto Service*" 2>nul

echo.
echo Deteniendo procesos en puertos 8081, 8083, 8084, 8085...
for %%p in (8081 8083 8084 8085) do (
    for /f "tokens=5" %%a in ('netstat -aon ^| find ":%%p" ^| find "LISTENING"') do (
        taskkill /F /PID %%a 2>nul
        if not errorlevel 1 echo Puerto %%p liberado
    )
)

echo.
echo === Todos los servicios han sido detenidos ===
pause

endlocal
