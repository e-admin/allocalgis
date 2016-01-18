@ECHO OFF
set REPOSITORYID=LocalGISIII
set SETTINGS=../settings/settings_lcg3.xml
set TIPO_COMPILACION=install:install-file deploy:deploy-file


REM Definimos el entorno
call ../perfiles/env_generico.bat
call ../perfiles/env_%USERNAME%.bat

set GLOBAL_SIGN=true

set URL=%MAVEN_SIGN_REPO%

call deployArtifacts.bat
pause