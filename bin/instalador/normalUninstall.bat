@ECHO OFF

call ../entornos/entornoLocalgis3.bat

set UPDATE=-U

set MODULE=%2
set GROUPID=-DgroupId=%1

echo "Modulo a desactivar:%MODULE% Groupid: %GROUPID%"
mvn -s %SETTINGS% %UPDATE% com.localgis.maven.plugin:updaterPlugin:3.0:launchUninstall -DartifactId=%MODULE% %GROUPID% -DmoduleVersion=3.0.0 -DinstallationURL=%INSTALLATION_URL% -DfailsIfBadInstallationURL=false -DpropertiesFile=%PROPERTIES_FILE% -DcheckServers=true