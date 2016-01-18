@ECHO OFF

REM Definimos el entorno
call ../../perfiles/env_generico.bat
call ../../perfiles/env_%USERNAME%.bat

set URL=%MAVEN_NO_SIGN_REPO%

call _deployArtifacts.bat nosign remoto
pause