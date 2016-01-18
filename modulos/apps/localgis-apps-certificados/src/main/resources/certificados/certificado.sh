#/bin/sh	
if [ -f /usr/local/LocalGIS/certificados/certificatestore ]; then
	rm -f /usr/local/LocalGIS/certificados/certificatestore
fi
/usr/local/java-jdk/bin/keytool -keystore /usr/local/LocalGIS/certificados/certificatestore -validity 1095 -alias jetty -genkey -keyalg RSA -keypass ******* -storepass ******* -dname "cn=PAMOD-APP1, ou=MODELO, o=MODELO, l=PRINCIPADO DE ASTURIAS, st=ASTURIAS, c=es"

if [ -f /usr/local/LocalGIS/certificados/keystore ]; then
	rm -f /usr/local/LocalGIS/certificados/keystore
fi
/usr/local/java-jdk/bin/keytool -keystore /usr/local/LocalGIS/certificados/keystore -validity 1095 -alias jetty -genkey -keyalg RSA -keypass ******* -storepass ******* -dname "cn=PAMOD-APP1, ou=MODELO, o=MODELO, l=PRINCIPADO DE ASTURIAS, st=ASTURIAS, c=es"

if [ -f /usr/local/LocalGIS/certificados/truststore ]; then
	rm -f /usr/local/LocalGIS/certificados/truststore
fi
/usr/local/java-jdk/bin/keytool -keystore /usr/local/LocalGIS/certificados/truststore -validity 1095 -alias jetty -genkey -keyalg RSA -keypass ******* -storepass ******* -dname "cn=PAMOD-APP1, ou=MODELO, o=MODELO, l=PRINCIPADO DE ASTURIAS, st=ASTURIAS, c=es"


