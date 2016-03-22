@ECHO OFF

setlocal ENABLEDELAYEDEXPANSION 

set SETTINGS=settings\settings_lcg3.xml

set TIPO_COMPILACION_DEFAULT=clean

call lcg3_comun.bat %1 %2 %3 %4 %5

