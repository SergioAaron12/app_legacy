@echo off
setlocal
cd /d "%~dp0"
echo === Iniciando Microservicios Legacy Frame ===
echo.

echo [*] Iniciando Auth en puerto 8085...
start "Auth Service" cmd /k "cd auth_movil && mvnw.cmd spring-boot:run"
timeout /t 3 >nul

echo [*] Iniciando Productos en puerto 8083...
start "Productos Service" cmd /k "cd productos && mvnw.cmd spring-boot:run"
timeout /t 3 >nul

echo [*] Iniciando Pedidos en puerto 8084...
start "Pedidos Service" cmd /k "cd pedidos && mvnw.cmd spring-boot:run"
timeout /t 3 >nul

echo [*] Iniciando Contacto en puerto 8081...
start "Contacto Service" cmd /k "cd contacto && mvnw.cmd spring-boot:run"
timeout /t 3 >nul

echo.
echo === Todos los servicios estan iniciandose ===
echo Puertos asignados:
echo   - Auth:      http://localhost:8085
echo   - Pedidos:   http://localhost:8084
echo   - Productos: http://localhost:8083
echo   - Contacto:  http://localhost:8081
echo.
echo Para detener, cierra cada ventana CMD o ejecuta stop-services.bat
pause

endlocal
