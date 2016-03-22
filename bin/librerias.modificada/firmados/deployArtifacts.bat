@ECHO OFF

REM _deployArtifacts: Deploy en repositorio local sin firma (_deployArtifacts nosign local)
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
set MSG_CONFIG=---SETTINGS::SIGN---
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


set ARRAY[1]=AbsoluteLayout,1.0.0
set ARRAY[2]=agile_core,1.0.0
set ARRAY[3]=alfresco-web-service-client,3.4.e
set ARRAY[4]=axiom-api,1.2.8
set ARRAY[5]=axiom-api,1.2.9
set ARRAY[6]=axiom-impl,1.2.8
set ARRAY[7]=axiom-impl,1.2.9
set ARRAY[8]=axis2,1.4.1
set ARRAY[9]=axis2,1.4
set ARRAY[10]=axis2,1.5.3
set ARRAY[11]=axis2-kernel,1.4.1
set ARRAY[12]=bsh,2.0b4
set ARRAY[13]=buoy,1.0.0
set ARRAY[14]=castor,0.9.5.3
set ARRAY[15]=castor-core,1.3.2
set ARRAY[16]=castor-xml,1.3.2
set ARRAY[17]=commons-discovery,0.2
set ARRAY[18]=commons-discovery,0.5
set ARRAY[19]=commons-httpclient,3.1
set ARRAY[20]=commons-ssl,1.0
set ARRAY[21]=customizedGeotoolsGraph,0.0.2
set ARRAY[22]=DataVision,1.0.0
set ARRAY[23]=datavision,1.0
set ARRAY[24]=deegree,1.0.1
set ARRAY[25]=flexdock,0.4
set ARRAY[26]=gt-epsg-extension,2.5.7
set ARRAY[27]=gt-epsg-hsql,2.5.5
set ARRAY[28]=gt-epsg-hsql,2.7.4
set ARRAY[29]=gt-epsg-wkt,2.5.5
set ARRAY[30]=gt-metadata,2.5.5
set ARRAY[31]=gt-metadata,2.7.4
set ARRAY[32]=gt-referencing,2.5.5
set ARRAY[33]=gt-referencing,2.7.4
set ARRAY[34]=hsqldb,1.7.1
set ARRAY[35]=hsqldb,1.8.0.7
set ARRAY[36]=jama,1.0.1
set ARRAY[37]=jasperreports,2.0.1
set ARRAY[38]=jasperreports,5.0.1
set ARRAY[39]=javax.servlet,5.1.12
set ARRAY[40]=jcalendar,1.3.2
set ARRAY[41]=jdom,1.1
set ARRAY[42]=jdtcore,3.1.0
set ARRAY[43]=jts,1.10
set ARRAY[44]=jts,1.12
set ARRAY[45]=localgis-sigem,1.0
set ARRAY[46]=log4j,1.2.16
set ARRAY[47]=mantisconnect,1.0
set ARRAY[48]=minml2,1.0.0
set ARRAY[49]=mysql-connector-java,5.1.20
set ARRAY[50]=mysql-connector-java,5.1.23
set ARRAY[51]=ojdbc14,10.2.0.4.0
set ARRAY[52]=routeEngine,0.0.2
set ARRAY[53]=sizeof,1.0.0
set ARRAY[54]=xercesImpl,2.4.0
set ARRAY[55]=xercesImpl,2.8.1
set ARRAY[56]=xercesImpl,2.9.1
set ARRAY[57]=xmlsec,1.4.1
set ARRAY[58]=axiom-api,1.2.7
set ARRAY[59]=axiom-impl,1.2.7
set ARRAY[60]=xfire-core,1.2.4
set ARRAY[61]=xstream,1.4.3
set ARRAY[62]=ibatis2-dao-localgis,2.2.0
set ARRAY[63]=ibatis2-sqlmap-localgis,2.2.0
set ARRAY[64]=ibatis2-common-localgis,2.2.0
REM Este fichero no hace falta.
REM set ARRAY2[65]=javax.pkcs11-dnie,1.0

SET TODAYSDATE=%date:~6,8%-%date:~3,2%-%date:~0,2%
set PATH_LIB_EXTERNAL=%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\libreriasExternas.bat
set TIPO_DEPLOY=install:install-file deploy:deploy-file

if exist "%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\libreriasExternas.bat" (
	del %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\libreriasExternas.bat
)
if not exist "%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\" (
	mkdir %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\
)
copy ..\..\settings\settings_lcg3_batch_deploy.xml %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3

echo @ECHO OFF >%PATH_LIB_EXTERNAL%
echo set URL=%URL% >>%PATH_LIB_EXTERNAL%


FOR /F "tokens=1,2,3,* delims=[]=," %%A IN ('SET ARRAY[') DO ( 

	REM Copiamos el archivo sin firmar a la lista de archivos a firmar.
	copy %%C-%%D.jar %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\ 
	copy %%C-%%D.pom  %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\ 

	
	echo call mvn -s %SETTINGS% %ACCION_MAVEN% -DartifactId=%%C -Dversion=%%D -Dfile=%%C-%%D.jar -DpomFile=%%C-%%D.pom -Dpackaging=jar %DATOS_DEPLOY%
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


REM call mvn -s %SETTINGS% %ACCION_MAVEN% -DartifactId=axis2 -Dversion=1.5.3 -Dfile=axis2-1.5.3.jar -Dclassifier=jar  -DpomFile=axis2-1.5.3.pom -Dpackaging=jar %DATOS_DEPLOY%
echo echo Desplegando : axis2-1.5.3.jar.jar >>%PATH_LIB_EXTERNAL%
echo call mvn -s settings_lcg3_batch_deploy.xml %TIPO_DEPLOY% -DartifactId=axis2 -Dversion=1.5.3 -Dfile=axis2-1.5.3.jar -DpomFile=axis2-1.5.3.pom -Dpackaging=jar -DrepositoryId=LocalGISIIIlibext -Durl=%%URL%% >>%PATH_LIB_EXTERNAL%	
copy axis2-1.5.3.jar %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\ 
copy axis2-1.5.3.pom  %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\ 
IF ERRORLEVEL 1 GOTO ERROR

call mvn -s %SETTINGS% %ACCION_MAVEN% -DartifactId=axis -Dversion=1.4 -Dfile=./axis.axis.1.4/axis-1.4.jar -DpomFile=./axis.axis.1.4/axis-1.4.pom -Dpackaging=jar %DATOS_DEPLOY%
echo echo Desplegando : axis-1.4.jar >>%PATH_LIB_EXTERNAL%
echo call mvn -s settings_lcg3_batch_deploy.xml %TIPO_DEPLOY% -DartifactId=axis2 -Dversion=1.4 -Dfile=./axis.axis.1.4/axis-1.4.jar -DpomFile=./axis.axis.1.4/axis-1.4.pom -Dpackaging=jar -DrepositoryId=LocalGISIIIlibext -Durl=%%URL%% >>%PATH_LIB_EXTERNAL%	
copy axis.axis.1.4\axis-1.4.jar %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\ 
copy axis.axis.1.4\axis-1.4.pom  %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\ 


IF ERRORLEVEL 1 GOTO ERROR

call mvn -s %SETTINGS% %ACCION_MAVEN% -DartifactId=axis -Dversion=1.4 -Dfile=./org.apache.axis.axis.1.4/axis-1.4.jar -DpomFile=./org.apache.axis.axis.1.4/axis-1.4.pom -Dpackaging=jar %DATOS_DEPLOY%
echo echo Desplegando : axis-1.4.jar >>%PATH_LIB_EXTERNAL%
echo call mvn -s settings_lcg3_batch_deploy.xml %TIPO_DEPLOY% -DartifactId=axis2 -Dversion=1.4 -Dfile=./org.apache.axis.axis.1.4/axis-1.4.jar -DpomFile=./org.apache.axis.axis.1.4/axis-1.4.pom -Dpackaging=jar -DrepositoryId=LocalGISIIIlibext -Durl=%%URL%% >>%PATH_LIB_EXTERNAL%
copy org.apache.axis.axis.1.4\axis-1.4.jar %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\ 
copy org.apache.axis.axis.1.4\axis-1.4.pom  %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas3\ 
IF ERRORLEVEL 1 GOTO ERROR

GOTO FIN

:ERROR
ECHO "Error al subir los artifacts"

:FIN
ECHO.

endlocal
