set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;all-desktop.jar
set CLASSPATH=%CLASSPATH%;localgis-apps-movilidad.jar
echo $CLASSPATH

set APPPATH=c:\LocalGIS\localgis-apps-movilidad-tablet

REM Hay que utilizar una maquina virtual de 32 bits
set JAVA_HOME_X64=c:\PROGRA~1\Java\jdk1.7.0_21
set JAVA_HOME_X86=c:\PROGRA~2\Java\jdk1.7.0_25
%JAVA_HOME_X86%\bin\java -Dapp_path=%APPPATH% -Ddesktop -Dfile.encoding="UTF-8" -Dautologin -Xmx32m es.satec.localgismobile.core.LocalGISMobile