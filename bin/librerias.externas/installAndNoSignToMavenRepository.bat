@ECHO OFF
set REPOSITORYID=LocalGISIII
set SETTINGS=../settings/settings_lcg3_nosign.xml
set TIPO_COMPILACION=install:install-file


REM Definimos el entorno
call ../perfiles/env_generico.bat
call ../perfiles/env_%USERNAME%.bat

set URL=%MAVEN_NO_SIGN_REPO%

set GLOBAL_SIGN=false

call deployArtifacts.bat

pause
