@ECHO OFF

REM set OPTIONS=dependency:purge-local-repository
REM set OPTIONS=-DXmx=512M
REM set MAVEN_OPTS="-Xmx512m"
REM set MAVEN_OPTS="-XX:MaxPermSize=512m"
set MAVEN_OPTS="-Xmx4096m"


set OPTIONS_UPDATE=-U %OPTIONS_SIGN%
set OPTIONS_UPDATE_NO_SIGN=-U -Djarsigner.skip=true

REM GOTO POM_PASO2

echo "Compile artifacts/pom_init0.xml"
echo %VARIABLES%
echo call mvn -e %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_init0.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
call mvn -e %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_init0.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR
rem IF "%TIPO_COMPILACION_DEFAULT%" == clean GOTO INSTALL_PASO0
rem GOTO CONTINUE_PASO0

REM Instalamos los artefactos para que el resto los puedan conocer. Solo vale para cuando ejecutamos clean
:INSTALL_PASO0
echo "Install artifacts/pom_init0.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_init0.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR


:CONTINUE_PASO0
echo "Compile artifacts/pom_init1.xml"
echo %VARIABLES%
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_init1.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR
IF "%TIPO_COMPILACION_DEFAULT%" == clean GOTO INSTALL_PASO1
GOTO CONTINUE_PASO1

REM Instalamos los artefactos para que el resto los puedan conocer. Solo vale para cuando ejecutamos clean
:INSTALL_PASO1
echo "Install artifacts/pom_init1.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_init1.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

REM #############################
REM #############################

:CONTINUE_PASO1
echo "Compile artifacts/pom_init2.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_init2.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR
IF "%TIPO_COMPILACION_DEFAULT%" == clean GOTO INSTALL_PASO2
GOTO CONTINUE_PASO2

REM Instalamos los artefactos para que el resto los puedan conocer.Solo vale para cuando ejecutamos clean
:INSTALL_PASO2
echo "Install artifacts/pom_init2.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_init2.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR


REM #############################
REM #############################


:CONTINUE_PASO2
echo "Compile artifacts/pom_init3.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_init3.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR
IF "%TIPO_COMPILACION_DEFAULT%" == clean GOTO INSTALL_PASO3
GOTO CONTINUE_PASO3

REM Instalamos los artefactos para que el resto los puedan conocer.Solo vale para cuando ejecutamos clean
:INSTALL_PASO3
echo "Install artifacts/pom_init3.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_init3.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR


REM #############################
REM #############################
:CONTINUE_PASO3
echo "Compile artifacts/pom_paso1.xml  (Modulos)"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_paso1.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR


:POM_PASO1.1
echo "Compile artifacts/pom_paso1.1.xml  (Software)"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_paso1.1.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

:POM_PASO1.5
echo "Compile artifacts/pom_paso1.5.xml  (Rutas)"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_paso1.5.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

:POM_PASO2
echo "Compile artifacts/pom_paso2.xml (Servidores)"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_paso2.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

:POM_PASO3
echo "Compile artifacts/pom_paso3.xml (Aplicaciones)"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_paso3.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

:POM_PASO4
echo "Compile artifacts/pom_paso4.xml (Extras)"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_paso4.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

:POM_PASO5
echo "Compile artifacts/pom_paso5.xml (Aplicaciones Web)"
call mvn %OPTIONS% %OPTIONS_UPDATE_NO_SIGN% %VARIABLES% -s %SETTINGS% -f artifacts/pom_paso5.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

:POM_PASO6
echo "Compile artifacts/pom_paso6.xml  (Modelos de datos)"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_paso6.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

:INSTALADOR1
echo "Compile artifacts/pom_instalador1.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_instalador1.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR

:INSTALADOR2
echo "Compile artifacts/pom_instalador2.xml"
call mvn %OPTIONS% %OPTIONS_UPDATE% %VARIABLES% -s %SETTINGS% -f artifacts/pom_instalador2.xml %TIPO_COMPILACION_DEFAULT% %DATOS_DEPLOY% %PROFILE%
IF ERRORLEVEL 1 GOTO ERROR


GOTO FIN

:ERROR
ECHO "Error al subir los artifacts"

:FIN
ECHO.