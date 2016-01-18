@ECHO OFF

call entornoLocalgis3.bat

set UPDATE=-U

set MODULE=%2
set GROUPID=-DgroupId=%1

echo "Modulo a instalar:%MODULE% Groupid: %GROUPID%"
mvn -s %SETTINGS% %UPDATE% com.localgis.maven.plugin:updaterPlugin:3.0:launchInstall -DartifactId=%MODULE% %GROUPID% -DmoduleVersion=3.0.0 -DinstallationURL=%INSTALLATION_URL% -DfailsIfBadInstallationURL=false -DregistryType=%REGISTRY_TYPE% -DpropertiesFile=%PROPERTIES_FILE% -DpromptConfirm=%PROMPT_CONFIRM% -DcheckServers=true -DoverWrite=%OVERWRITE% -DcheckPsql=true