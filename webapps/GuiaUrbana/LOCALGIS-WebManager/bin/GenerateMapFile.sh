LD_LIBRARY_PATH="/usr/local/LocalGIS/mapserver/lib:/usr/local/LocalGIS/mapserver/lib.ext"
export LD_LIBRARY_PATH
export PROJ_LIB=/usr/local/LocalGIS/mapserver/proj

CLASSPATH=/usr/local/LocalGIS/mapserver/apps/bin:/usr/local/LocalGIS/mapserver/apps/bin/mapscript.jar
/usr/local/jdk1.6.0_20/bin/java -cp $CLASSPATH GenerateMapFile $1 $2
