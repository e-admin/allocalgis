cd classes
LANG=es_ES.ISO-8859-1
export LANG
LIB=../lib
CONFIG=./config
PATH=$PATH:.
#CLASSPATH=$LIB/productos/postgre/postgresql-8.0-311.jdbc3.jar
CLASSPATH=$LIB/productos/postgre/pg74.213.jdbc2.jar

CLASSPATH=$CLASSPATH:.:$LIB/geopista_administrador.jar:$LIB/server.jar:$LIB/geopista_protocol.jar:$LIB/jumppatched.jar:$LIB/localgis_cementerios.jar:$LIB/geopista_security.jar:$LIB/geobench.jar:$LIB/geopista_intercambio.jar:$LIB/core/satecNet.jar:$LIB/core/satec.jar:$LIB/productos/castor/castor-0.9.5.3.jar:$LIB/productos/jetty/start.jar:$LIB/productos/jetty/stop.jar

CLASSPATH=$CLASSPATH:$LIB/productos/jetty/ext/jasper-compiler.jar:$LIB/productos/jetty/ext/xercesImpl.jar:$LIB/productos/vividsolutions/jts-1.5.jar:$LIB/productos/jetty/ext/jasper-runtime.jar:$LIB/productos/xml/xml-apis.jar:$LIB/productos/xml/jdom.jar:$LIB/productos/jetty/extra/ext/p6spy.jar:$LIB/productos/jetty/extra/ext/carol.jar

CLASSPATH=$CLASSPATH:$LIB/productos/jetty/extra/ext/activation.jar:$LIB/productos/jetty/extra/ext/hsqldb.jar:$LIB/productos/jetty/extra/ext/log4j.jar:$LIB/productos/jetty/extra/ext/ow_util_log_wrp_log4j.jar:$LIB/productos/jetty/extra/ext/commons-cli.jar:$LIB/productos/jetty/extra/ext/objectweb-datasource.jar:$LIB/productos/jetty/extra/ext/mail.jar:$LIB/productos/jetty/extra/ext/jotm_iiop_stubs.jar:$LIB/productos/jetty/extra/ext/jotm.jar:$LIB/productos/apache-ftp/ftpserver-dev.jar:$LIB/productos/deegree/deegree2004-05-07.jar:$LIB/productos/jai/jai_codec.jar:$LIB/productos/jai/jai_core.jar:$LIB/productos/jai/mlibwrapper_jai.jar

CLASSPATH=$CLASSPATH:$LIB/productos/jetty/extra/ext/jotm.jar:$LIB/productos/pdf/PDFBox.jar:$LIB/productos/jakarta/commons.jar:$LIB/productos/jetty/extra/ext/ow_util_log_file.jar:$LIB/productos/jetty/extra/ext/jotm_jrmp_stubs.jar:$LIB/productos/jetty/extra/ext/commons-logging.jar:$LIB/productos/jetty/extra/ext/jonas_timer.jar:$LIB/productos/jetty/extra/ext/jts1_0.jar:$LIB/productos/jetty/extra/ext/jta-spec1_0_1.jar:$LIB/productos/jetty/extra/ext/xapool.jar:$LIB/productos/jetty/extra/lib/org.mortbay.jetty.plus.resource.jar:$LIB/productos/jetty/extra/lib/org.mortbay.jetty.plus.jar:$LIB/productos/jetty/lib/org.mortbay.jetty.jar

CLASSPATH=$CLASSPATH:$LIB/productos/jetty/lib/javax.servlet.jar:$LIB/productos/jetty/lib/org.mortbay.jmx.jar:$LIB/productos/1postgre/pg74.213.jdbc2.jar:$LIB/productos/jetty/extra/lib/org.mortbay.jaas.jar:$LIB/productos/1postgres/pg74.214.jdbc3.jar:$LIB/productos/puzzle/puzzled-classes-1.3.zip:$LIB/geopista_sql.jar:$LIB/geopista_patrimonio.jar:$LIB/catastro_client.jar:$LIB/catastro_server.jar:$LIB/alp_server.jar:$LIB/alp_client.jar:$LIB/productos/xfire/activation-1.1.jar:$LIB/productos/xfire/bcprov-jdk15-133.jar:$LIB/productos/xfire/commons-attributes-api-2.1.jar:$LIB/productos/xfire/commons-beanutils-1.7.0.jar:$LIB/productos/xfire/commons-codec-1.3.jar:$LIB/productos/xfire/commons-discovery-0.2.jar:$LIB/productos/xfire/commons-httpclient-3.0.jar

CLASSPATH=$CLASSPATH:$LIB/productos/xfire/commons-logging-1.0.4.jar:$LIB/productos/xfire/jaxb-api-2.0.jar:$LIB/productos/xfire/jaxb-api-1.0.jar:$LIB/productos/xfire/jaxb-impl-2.0.1.jar:$LIB/productos/xfire/jaxb-impl-1.0.5.jar:$LIB/productos/xfire/jaxb-xjc-2.0.1.jar:$LIB/productos/xfire/jaxen-1.1-beta-9.jar:$LIB/productos/xfire/jaxws-api-2.0.jar:$LIB/productos/xfire/jdom-1.0.jar:$LIB/productos/xfire/jsr173_api-1.0.jar:$LIB/productos/xfire/mail-1.4.jar:$LIB/productos/xfire/opensaml-1.0.1.jar:$LIB/productos/xfire/relaxngDatatype-20050913.jar:$LIB/productos/xfire/saaj-api-1.3.jar:$LIB/productos/xfire/saaj-impl-1.3.jar:$LIB/productos/xfire/spring-1.2.6.jar:$LIB/productos/xfire/stax-api-1.0.1.jar:$LIB/productos/xfire/stax-utils-20040917.jar

CLASSPATH=$CLASSPATH:$LIB/productos/xfire/wsdl4j-1.6.1.jar:$LIB/productos/xfire/wss4j-1.5.1.jar:$LIB/productos/xfire/wstx-asl-3.2.0.jar:$LIB/productos/xfire/xbean-2.2.0.jar:$LIB/productos/xfire/xbean-spring-2.8.jar:$LIB/productos/xfire/xercesImpl-2.6.2.jar:$LIB/productos/xfire/xfire-aegis-1.2.6.jar:$LIB/productos/xfire/xfire-annotations-1.2.6.jar:$LIB/productos/xfire/xfire-core-1.2.6.jar:$LIB/productos/xfire/xfire-generator-1.2.6.jar:$LIB/productos/xfire/xfire-java5-1.2.6.jar:$LIB/productos/xfire/xfire-jaxb-1.1.2.jar:$LIB/productos/xfire/xfire-jaxb2-1.2.6.jar:$LIB/productos/xfire/xfire-jaxws-1.2.6.jar:$LIB/productos/xfire/xfire-jsr181-api-1.0-M1.jar:$LIB/productos/xfire/xfire-spring-1.2.6.jar:$LIB/productos/xfire/xfire-ws-security-1.2.6.jar:$LIB/productos/xfire/xfire-xmlbeans-1.2.6.jar

CLASSPATH=$CLASSPATH:$LIB/productos/xfire/xml-apis-1.0.b2.jar:$LIB/productos/xfire/XmlSchema-1.1.jar:$LIB/productos/xfire/xmlsec-1.3.0.jar:$LIB/productos/xfire/xsdlib-20050913.jar:$LIB/productos/core/localgis-core.jar:$LIB/productos/sigem/localgis_sigem.jar:$LIB/productos/rmijdbc/RmiJdbc.jar:$LIB/productos/cifrado/cifrado.jar:$LIB/productos/cifrado/contrib.jar:$LIB/productos/core/pluginMobileExtract.jar:$LIB/productos/sizeof/SizeOf.jar

export CLASSPATH

#/usr/local/java-jdk/bin/java -DCONFIG=./config -Xmx1024M -Djava.security.auth.login.config=./config/jaasconfig.conf -Djava.awt.headless=true -classpath $CLASSPATH admcarApp.PasarelaAdmcar >> /usr/local/LocalGIS/admcar/classes/logs/admcar.log 2>&1 &

JVM_ARGUMENTS="-DmostrarSizeOf=false -javaagent:/usr/local/LocalGIS/admcar/lib/productos/sizeof/SizeOf.jar"
/usr/local/java-jdk/bin/java -DCONFIG=./config -Xmx1024M -Djava.security.auth.login.config=./config/jaasconfig.conf -Djava.awt.headless=true $JVM_ARGUMENTS admcarApp.PasarelaAdmcar >> /usr/local/LocalGIS/admcar/classes/logs/admcar.log 2>&1 &



#Se guarda el PID del proceso que se arranca
if [ ! -z "$ADMCAR_PID" ]; then
   echo $! > $ADMCAR_PID
fi

