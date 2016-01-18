@ECHO OFF

echo "Ejecutando echo %1"
call psql -f %1 -q -d %DATABASE% -U %USER% -h %HOST% -p %PORT%
set ERROR=%errorlevel%
if "%ERROR%" == "1" goto error
goto ok

:error
echo.
echo.
echo - ERROR!!!!! Se ha producido un error al generar el despliegue.
echo.
PAUSE
goto end

:ok
REM echo.
REM echo.
REM echo.
REM echo - OK... Despliegue generado correctamente.
echo.

