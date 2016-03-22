@ECHO OFF

set URL=%MAVEN_NO_SIGN_REPO_EXT%

call deployArtifacts.bat nosign remoto
pause