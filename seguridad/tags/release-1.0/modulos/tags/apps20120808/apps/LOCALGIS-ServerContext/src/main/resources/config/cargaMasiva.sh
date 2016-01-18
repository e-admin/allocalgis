#!/bin/sh

CLASSPATH="c:\LocalGIS.CargadorMasivo\lib\cargador_masivo.jar"
CLASSPATH=$CLASSPATH";c:\LocalGIS.CargadorMasivo\lib\geopista_workbench.jar"
CLASSPATH=$CLASSPATH";c:\LocalGIS.CargadorMasivo\lib\jumppatched.jar"
CLASSPATH=$CLASSPATH";c:\LocalGIS.CargadorMasivo\lib\pluginDatosExternos.jar"
CLASSPATH=$CLASSPATH";c:\LocalGIS.CargadorMasivo\lib\jts-1.9.jar"
CLASSPATH=$CLASSPATH";c:\LocalGIS.CargadorMasivo\lib\commons-logging-1.1.1.jar"
CLASSPATH=$CLASSPATH";c:\LocalGIS.CargadorMasivo\lib\postgresql-8.0-311.jdbc3.jar"

COMANDO_EIEL="java -classpath $CLASSPATH com.geopista.app.loadEIELData.EIELLoader"

echo "COMANDO_EIEL: $COMANDO_EIEL $1 $2"

$COMANDO_EIEL $1 $2