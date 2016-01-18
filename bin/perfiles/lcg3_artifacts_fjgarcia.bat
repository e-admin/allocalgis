 @ECHO OFF

REM set OPTIONS=dependency:purge-local-repository
REM set OPTIONS=-DXmx=512M
REM set MAVEN_OPTS="-Xmx512m"
set MAVEN_OPTS="-XX:MaxPermSize=128m"

set OPTIONS_UPDATE=-U %OPTIONS_SIGN%

REM #############################
REM #############################
:CONTINUE_PASO3
echo "Compile artifacts/pom_%USERNAME%.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES%  -s %SETTINGS% -f artifacts/pom_%USERNAME%.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

REM echo "Compile artifacts/pom_instalador.xml"
REM call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES%  -s %SETTINGS% -f artifacts/pom_instalador1.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY%
REM IF ERRORLEVEL 1 GOTO ERROR

REM echo "Compile artifacts/pom_instalador.xml"
REM call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES%  -s %SETTINGS% -f artifacts/pom_instalador2.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY%
REM IF ERRORLEVEL 1 GOTO ERROR


GOTO FIN

:ERROR
ECHO "Error al subir los artifacts"

:FIN
ECHO.