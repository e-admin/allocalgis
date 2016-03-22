@ECHO OFF

REM _deployArtifacts: Deploy en repositorio local sin firma (Equivale: _deployArtifacts nosign local)
REM _deployArtifacts sign/nosign local/remoto: Deploy en repositorio local con/sin firma en local/remoto
REM _deployArtifacts sign remoto: Deploy en nexus con settings firma

setlocal enabledelayedexpansion 

cls 




set MSG_CONFIG=
set SETTINGS=
set ACCION_MAVEN=
set REPOSITORY_MAVEN=
set ID_REPOSITORY_MAVEN=
set DATOS_DEPLOY=

IF "%1"=="" GOTO NOSIGN
IF "%1"=="nosign" GOTO NOSIGN
IF "%1"=="sign" GOTO SIGN
GOTO NOSIGN

:SIGN
set MSG_CONFIG=---SETTINGS::SING---
set SETTINGS=..\..\settings\settings_lcg3.xml
GOTO CONTINUE

:NOSIGN
set MSG_CONFIG=---SETTINGS::UNSING---
set SETTINGS=..\..\settings\settings_lcg3_nosign.xml
GOTO CONTINUE


:CONTINUE
IF "%2"=="" GOTO LOCAL
IF "%2"=="local" GOTO LOCAL
IF "%2"=="remoto" GOTO REMOTO
GOTO LOCAL

:LOCAL
set MSG_CONFIG=%MSG_CONFIG% ---REPOSITORIO::LOCAL---
set ACCION_MAVEN=install:install-file
GOTO PROCESS

:REMOTO
set MSG_CONFIG=%MSG_CONFIG% ---REPOSITORIO::REMOTO---
set ACCION_MAVEN=install:install-file deploy:deploy-file
set REPOSITORY_MAVEN=%URL%
set ID_REPOSITORY_MAVEN=LocalGISIIIlibext
set DATOS_DEPLOY=-Durl=%REPOSITORY_MAVEN% -DrepositoryId=%ID_REPOSITORY_MAVEN%
GOTO PROCESS


:PROCESS
echo .
echo CONFIGURACION:: %MSG_CONFIG%
echo .

set ARRAY[1]=bcprov-jdk16,1.46
set ARRAY[2]=javax.pkcs11,1.0
set ARRAY[3]=jhall,2.0
set ARRAY[4]=localgis-ssl,2.0
set ARRAY[5]=surveyos_main,1.0
set ARRAY[6]=bcmail-jdk14,1.38
set ARRAY[7]=bcmail-jdk14,136
set ARRAY[8]=bcmail-jdk14,138
set ARRAY[9]=bcprov-jdk14,1.38
set ARRAY[10]=bcprov-jdk14,136
set ARRAY[11]=bcprov-jdk14,138
set ARRAY[12]=bcprov-jdk14,140
set ARRAY[13]=bctsp-jdk14,1.38
set ARRAY[14]=bcprov-jdk15,133
set ARRAY[15]=jcmdline,1.0.2
set ARRAY[16]=cifrado,1.0


SET TODAYSDATE=%date:~6,8%-%date:~3,2%-%date:~0,2%
set PATH_LIB_EXTERNAL=%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas2\libreriasExternas.bat
set TIPO_DEPLOY=install:install-file deploy:deploy-file

if exist "%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas2\libreriasExternas.bat" (
	del %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas2\libreriasExternas.bat
)
if not exist "%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas2\" (
	mkdir %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas2\
)
copy ..\..\settings\settings_lcg3_batch_deploy.xml %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas2

echo @ECHO OFF >%PATH_LIB_EXTERNAL%
echo set URL=%URL% >>%PATH_LIB_EXTERNAL%

FOR /F "tokens=1,2,3,* delims=[]=," %%A IN ('SET ARRAY[') DO ( 

	REM Copiamos el archivo sin firmar a la lista de archivos a firmar.
	copy %%C-%%D.jar %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas2\ 
	copy %%C-%%D.pom  %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas2\ 
	
	call mvn -s %SETTINGS% %ACCION_MAVEN% -DartifactId=%%C -Dversion=%%D -Dfile=%%C-%%D.jar -DpomFile=%%C-%%D.pom -Dpackaging=jar %DATOS_DEPLOY%
	
	echo echo Desplegando : %%C-%%D.jar >>%PATH_LIB_EXTERNAL%
	echo call mvn -s settings_lcg3_batch_deploy.xml %TIPO_DEPLOY% -DartifactId=%%C -Dversion=%%D -Dfile=%%C-%%D.jar -DpomFile=%%C-%%D.pom -Dpackaging=jar -DrepositoryId=LocalGISIIIlibext -Durl=%%URL%% >>%PATH_LIB_EXTERNAL%		
	echo IF ERRORLEVEL 1 GOTO ERROR >>%PATH_LIB_EXTERNAL%	
)


echo GOTO OK >>%PATH_LIB_EXTERNAL%		
echo :ERROR >>%PATH_LIB_EXTERNAL%		
echo echo Error al realizar el despliegue >>%PATH_LIB_EXTERNAL%		
echo GOTO END >>%PATH_LIB_EXTERNAL%		
echo :OK >>%PATH_LIB_EXTERNAL%		
echo echo Despliegue finalizado >>%PATH_LIB_EXTERNAL%		
echo :END >>%PATH_LIB_EXTERNAL%	

REM CASOS ESPECIALES
REM call mvn -s %SETTINGS% %ACCION_MAVEN% -DartifactId=axis2 -Dversion=1.5.3 -Dfile=axis2-1.5.3.jar -Dclassifier=jar  -DpomFile=axis2-1.5.3.pom -Dpackaging=jar  %DATOS_DEPLOY%
	
GOTO FIN

:ERROR
ECHO "Error al subir los artifacts"

:FIN
ECHO.

endlocal
