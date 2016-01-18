#!/bin/sh

export UPDATE=-U

if [ "$#" -ne 3 ]; then
   echo "Uso ./normalUninstall.sh <entorno> <groupid> <modulo>"
   echo "- ./normalUninstall.sh localgis3 com.localgis.modules.web GuiaUrbanaModule"
   echo "- ./normalUninstall.sh localgis3 com.localgis.modules Demo_LocalGisInstallModule"
   exit;
fi


export ENTORNO=$2
. ./entornos/entorno_$1.sh $1

export MODULE=$3
export GROUPID=-DgroupId=$2
export VERSION=3.0.0

echo "Modulo a desactivar:$MODULE Groupid: $GROUPID"
echo "Modulo excluido: $EXCLUDEMODULES"

$MAVEN_HOME/bin/mvn $SETTINGS $UPDATE com.localgis.maven.plugin:updaterPlugin:3.0:launchUninstall -DartifactId=$MODULE $GROUPID -DmoduleVersion=$VERSION -DinstallationURL=$INSTALLATION_URL -DfailsIfBadInstallationURL=false -DpropertiesFile=$PROPERTIES_FILE -DcheckServers=true

