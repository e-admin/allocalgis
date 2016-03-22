@ECHO OFF
set REPOSITORYID=LocalGISIIIlibext
set SETTINGS=../settings/settings_lcg3.xml
set TIPO_COMPILACION=install:install-file

set GLOBAL_SIGN=true

set URL=%MAVEN_SIGN_REPO_EXT%

call deployArtifacts.bat
pause