#!/bin/sh

export UPDATE=-U

if [ "$#" -ne 3 ] && [ "$#" -ne 4 ] ; then
   echo "Uso ./normalInstall.sh <entorno> <groupid> <modulo> <exclude>"
   echo "- ./normalInstall.sh localgis3 com.localgis.modules.web GuiaUrbanaModule"
   echo "- ./normalInstall.sh localgis3 com.localgis.modules Demo_LocalGisInstallModule"
   echo "- ./normalInstall.sh com.localgis.modules.app AdministracionModule \"-DexcludeModules=Modulo Software;stuff\""
   exit;
fi


export ENTORNO=$2
. ./entornos/entorno_$1.sh $1

export MODULE=$3
export GROUPID=-DgroupId=$2
export VERSION=3.0.0
export EXCLUDEMODULES=$4
export OVERWRITE=false

echo "Modulo a instalar:$MODULE Groupid: $GROUPID"
echo "Modulo excluido: $EXCLUDEMODULES"

$MAVEN_HOME/bin/mvn -s $SETTINGS $UPDATE com.localgis.maven.plugin:updaterPlugin:3.0:launchInstall -DartifactId=$MODULE $GROUPID -DmoduleVersion=$VERSION -DinstallationURL=$INSTALLATION_URL -DfailsIfBadInstallationURL=false -DregistryType=$REGISTRY_TYPE -DpropertiesFile=$PROPERTIES_FILE -DpromptConfirm=$PROMPT_CONFIRM -DcheckServers=true -DoverWrite=$OVERWRITE -DcheckPsql=true $EXCLUDEMODULES

