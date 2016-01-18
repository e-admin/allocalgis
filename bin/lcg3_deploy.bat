@ECHO OFF


setlocal ENABLEDELAYEDEXPANSION 

REM Definimos el entorno
call perfiles/env_generico.bat
call perfiles/env_%USERNAME%.bat


set TIPO_COMPILACION_DEFAULT=clean deploy

call lcg3_comun.bat %1 %2 %3 %4 %5