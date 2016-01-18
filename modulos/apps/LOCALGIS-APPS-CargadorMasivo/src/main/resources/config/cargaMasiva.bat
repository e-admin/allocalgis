@echo off

set HOME=.
:: Librerias
set CLASSPATH=%HOME%\lib\cargador_masivo.jar
set CLASSPATH=%CLASSPATH%;%HOME%\lib\geopista_workbench.jar
set CLASSPATH=%CLASSPATH%;%HOME%\lib\jumppatched.jar
set CLASSPATH=%CLASSPATH%;%HOME%\lib\pluginDatosExternos.jar
set CLASSPATH=%CLASSPATH%;%HOME%\lib\jts-1.9.jar
set CLASSPATH=%CLASSPATH%;%HOME%\lib\commons-logging-1.1.1.jar
set CLASSPATH=%CLASSPATH%;%HOME%\lib\postgresql-8.0-311.jdbc3.jar

set COMANDO_EIEL=java -classpath "%CLASSPATH%" -Xmx1500M com.geopista.app.loadEIELData.EIELLoader
::echo %COMANDO_EIEL% %1 %2 
%COMANDO_EIEL% %1 %2
