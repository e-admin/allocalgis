@ECHO OFF
set LIB=..\webapps\software
set PATH=%PATH%:.
set CLASSPATH=.;%LIB%\cargadorAutomatico.jar;%LIB%\jumppatched.jar
set CLASSPATH=%CLASSPATH%;%LIB%\geopista_workbench.jar
set CLASSPATH=%CLASSPATH%;%LIB%\geopista_server.jar
set CLASSPATH=%CLASSPATH%;%LIB%\geopista_plugin.jar
set CLASSPATH=%CLASSPATH%;%LIB%\geopista_images.jar
set CLASSPATH=%CLASSPATH%;%LIB%\geopista_apps.jar
set CLASSPATH=%CLASSPATH%;%LIB%\pirolplugin.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos\httpclient\commons-logging.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/log4j/log4j-1.2.6.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/xfire/commons-httpclient-3.0.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/xfire/commons-codec-1.3.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/vividsolutions/jts-1.5.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/xml/jdom.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/vividsolutions/Jama-1.0.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/castor/castor-0.9.5.3.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/xml/xerces.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/xfire/xercesImpl-2.6.2.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/degree/deegree2004-05-07.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/jakarta/commons-collections-3.2.1.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/jakarta/commons-configuration-1.5.jar
set CLASSPATH=%CLASSPATH%;%LIB%\productos/jakarta/commons-lang-2.4.jar

REM SET OPTIONS=-Xmx1024M -Djava.awt.headless=true
SET OPTIONS=-Xmx1024M 
java -classpath %CLASSPATH% %OPTIONS% com.geopista.cargador.CargarCallejero
