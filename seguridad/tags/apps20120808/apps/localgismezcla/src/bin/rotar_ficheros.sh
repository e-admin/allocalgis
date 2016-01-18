#!/bin/sh
###########################################################################
# Rotacion y borrado de Ficheros de Logs de las aplicaciones
#
# USO:  Realiza la rotacion deseada y ademas borra rotaciones antiguas
#       rotar_ficheros.sh      -----> Rota los ficheros de logs
#       rotar_ficheros.sh borrar  --> Borra los ficheros antiguos
#       rotar_ficheros.sh bysize  --> Rota los ficheros de logs que han alcanzado el tamaño definido MAX_FILE_SIZE
###########################################################################

# Rotacion de Ficheros de Logs de las aplicaciones

DATE=`date +%d%h-%I%M%p`
DATE=`date '+[%d/%m/%Y_%H_%M_%S]'`
DATE=`date '+%Y-%m-%d'`

#
# Valores que debe cambiar el administrador
# Antiguedad de los ficheros rotados. Los más viejos serán borrados
DIAS_ROTADO=+5
DIAS_BORRADO=+30

#
# Valores que debe cambiar el administrador
# Antiguedad de los ficheros rotados. Los más viejos serán borrados
MAX_FILE_SIZE=300000

#Estos directorios varian en pamod-app1 y pamod-app2
TOMCAT_LOGS=/datos/tomcat/logs
ADMCAR_LOGS=/datos/admcar/logs

#Directorio donde se colocan los logs agrupados por directorios
#Estos directorios varian en pamod-app1 y pamod-app2
APPS_SPOOL=/datos/logs-rotados
APPS_SPOOL_TOMCAT=$APPS_SPOOL/tomcat/diarios
APPS_SPOOL_ADMCAR=$APPS_SPOOL/admcar/diarios



DATE_FULL=`date +%Y-%m-%d_%H_%M`
DATE_SIMPLE=`date +%Y-%m-%d`

GZIP=/bin/gzip


ejecuta()
{
 #echo $*
 $*
}

##
## Creacion de directorios
##
crear_directorio_rotacion(){
        ejecuta mkdir -p $APPS_SPOOL_TOMCAT/$DATE_SIMPLE
        ejecuta mkdir -p $APPS_SPOOL_ADMCAR/$DATE_SIMPLE
}

##
## Rotacion de ficheros
##
rotar_ficheros()
{
  # Logs del Tomcat
  ejecuta find $TOMCAT_LOGS -name "*.log" -mtime $DIAS_ROTADO -follow -exec mv {} $APPS_SPOOL_TOMCAT/$DATE_SIMPLE \; 2>/dev/null
  ejecuta find $TOMCAT_LOGS -name "*.txt" -mtime $DIAS_ROTADO -follow -exec mv {} $APPS_SPOOL_TOMCAT/$DATE_SIMPLE \; 2>/dev/null
  ejecuta find $APPS_SPOOL_TOMCAT/$DATE_SIMPLE -name "*.log" -follow -exec $GZIP -9 -f {} \; 2>/dev/null
  ejecuta find $APPS_SPOOL_TOMCAT/$DATE_SIMPLE -name "*.txt" -follow -exec $GZIP -9 -f {} \; 2>/dev/null

  # Logs del Admcar (Diversos tipos)
  ejecuta find $ADMCAR_LOGS -name "*.log" -mtime $DIAS_ROTADO -follow -exec mv {} $APPS_SPOOL_ADMCAR/$DATE_SIMPLE \; 2>/dev/null
  ejecuta find $ADMCAR_LOGS -name "administracion.log.*" -mtime $DIAS_ROTADO -follow -exec mv {} $APPS_SPOOL_ADMCAR/$DATE_SIMPLE \; 2>/dev/null
  ejecuta cp $ADMCAR_LOGS/admcar.log $APPS_SPOOL_ADMCAR/$DATE_SIMPLE/admcar.log.$DATE_FULL.log 2>/dev/null
  ejecuta cp /dev/null $ADMCAR_LOGS/admcar.log 2>/dev/null

  ejecuta find $APPS_SPOOL_ADMCAR/$DATE_SIMPLE -name "*.log" -follow -exec $GZIP -9 -f {} \; 2>/dev/null
  ejecuta find $APPS_SPOOL_ADMCAR/$DATE_SIMPLE -name "administracion.log.*" -follow -exec $GZIP -9 -f {} \; 2>/dev/null

}

#
#
# Borrado de los ficheros
#
borrar_logs()
{
  ejecuta find $APPS_SPOOL_TOMCAT -name "200*" -type d -follow -mtime $DIAS_BORRADO -exec rm -rf {} \; 2>/dev/null
  ejecuta find $APPS_SPOOL_ADMCAR -name "200*" -type d -follow -mtime $DIAS_BORRADO -exec rm -rf {} \; 2>/dev/null
}

#Para rotar un fichero concreto dependiendo del tamaño
#$1 es el nombre del fichero ;; $2 el tamaño limite en Kb
rotar_logs_bysize()
{
  nLimite=$2

  nSize=`du -ks $TOMCAT_LOGS/$1 | awk '{print $1}'`

  if [ $nSize -ge $nLimite ]; then
    rotar_logs $1
  fi

}

##################
## MENU
##################

case "$1" in
        'borrar')
                borrar_logs
                ;;
        -bysize)
                ejecuta mkdir -p $APPS_SPOOL/$DATE_SIMPLE 2>/dev/null
                rotar_logs_bysize <NombreFichero> $MAX_FILE_SIZE
                ;;
        'rotar')
                crear_directorio_rotacion
                rotar_ficheros 
                borrar_logs
                ;;
        *)

				;;
esac