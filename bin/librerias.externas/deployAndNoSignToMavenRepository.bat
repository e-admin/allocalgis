@ECHO OFF
set REPOSITORYID=LocalGISIIIlibext
set SETTINGS=../settings/settings_lcg3_nosign.xml
set TIPO_COMPILACION=install:install-file deploy:deploy-file


set URL=%MAVEN_NO_SIGN_REPO_EXT%

set GLOBAL_SIGN=false

call deployArtifacts.bat

pause
