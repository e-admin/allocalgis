@ECHO OFF

setlocal ENABLEDELAYEDEXPANSION 

call perfiles/env_generico.bat
call perfiles/env_%USERNAME%.bat

REM No quitar esta linea
set SETTINGS=settings\settings_lcg3.xml

set TIPO_COMPILACION_DEFAULT=clean

call lcg3_comun.bat %1 %2 %3 %4 %5

