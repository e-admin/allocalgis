El proyecto está desarrollado utilizando un Tomcat versión 6.0.14.

Para que la aplicación funcione correctamente desde el Tomcat integrado en el
Eclipse hay que eliminar la dependencia del jsp-api del pom.xml. Por alguna
razón, si está esa dependencia, aún estando como scope provided, interfiere
con algo y la aplicación no funciona. Es importante volver a ponerla para
hacer el "mvn package"


Para generar para diferentes entornos con sustitucion de cadenas por los valores reales hay que
ejecutar

mvn package -P MODELO_PRO (Para el entorno de produccion de MODELO)

Los diferentes entornos se configuran en la parte final del pom.xml y los parametros a sustituir estan en el fichero
localgis.properties (al final del todo para cada entorno) y se ejecutan desde el fichero build.xml (que se llama desde el 
propio pom.xml)


Para compilar y empezar a llenar el repositorio desde cero y verificar dependencias, realizaremos las siguientes operaciones
mvn package -P MODELO_PRO -s c:\Users\fjgarcia\.m2\settings_localgis_windows.xml
