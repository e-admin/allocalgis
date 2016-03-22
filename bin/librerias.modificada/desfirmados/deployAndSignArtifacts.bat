@ECHO OFF



set URL=%MAVEN_SIGN_REPO_EXT%

call deployArtifacts.bat sign remoto
pause