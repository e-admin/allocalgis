@ECHO OFF

REM set OPTIONS=dependency:purge-local-repository
REM set OPTIONS=-DXmx=512M
REM set MAVEN_OPTS="-Xmx512m"
set MAVEN_OPTS="-XX:MaxPermSize=128m"

set OPTIONS_UPDATE=-U %OPTIONS_SIGN%

REM #############################
REM #############################
echo "Compile artifacts/pom_instalador2.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES%  -s %SETTINGS% -f artifacts/pom_instalador2.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR


GOTO FIN

:ERROR
ECHO "Error al subir los artifacts"

:FIN
ECHO.