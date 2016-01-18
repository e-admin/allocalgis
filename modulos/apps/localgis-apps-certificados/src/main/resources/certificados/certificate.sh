#/bin/sh
if [ -f /usr/local/LocalGIS.MODELO/certificados/certificatestore ]; then
      rm -f /usr/local/LocalGIS.MODELO/certificados/certificatestore
fi
/usr/java/jdk1.6.0_21/bin/keytool -keystore /usr/local/LocalGIS.MODELO/certificados/certificatestore -validity 1095 -alias jetty -genkey -keyalg RSA -keypass K45zgD66 -storepass K45zgD66 -dname "cn=PAMOD-APP1, ou=MODELO, o=MODELO, l=PRINCIPADO DE ASTURIAS, st=ASTURIAS, c=es"
 
if [ -f /usr/local/LocalGIS.MODELO/certificados/keystore ]; then
      rm -f /usr/local/LocalGIS.MODELO/certificados/keystore
fi
/usr/java/jdk1.6.0_21/bin/keytool -keystore /usr/local/LocalGIS.MODELO/certificados/keystore -validity 1095 -alias jetty -genkey -keyalg RSA -keypass password -storepass password -dname "cn=PAMOD-APP1, ou=MODELO, o=MODELO, l=PRINCIPADO DE ASTURIAS, st=ASTURIAS, c=es"
 
if [ -f /usr/local/LocalGIS.MODELO/certificados/truststore ]; then
      rm -f /usr/local/LocalGIS.MODELO/certificados/truststore
fi
/usr/java/jdk1.6.0_21/bin/keytool -keystore /usr/local/LocalGIS.MODELO/certificados/truststore -validity 1095 -alias jetty -genkey -keyalg RSA -keypass password -storepass password -dname "cn=PAMOD-APP1, ou=MODELO, o=MODELO, l=PRINCIPADO DE ASTURIAS, st=ASTURIAS, c=es"
