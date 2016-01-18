LIB=../lib
CONFIG=./config
PATH=$PATH:.
CLASSPATH=.:$LIB/server.jar:$LIB/geopista_protocol.jar:
$LIB/jumppatched.jar:$LIB/geopista_security.jar:productos/xfire/xmlsec-1.3.0.jar
export CLASSPATH
%JDK15%/bin/java -Xmx1024M -Djava.awt.headless=true com.geopista.cargador.CargarCallejero
