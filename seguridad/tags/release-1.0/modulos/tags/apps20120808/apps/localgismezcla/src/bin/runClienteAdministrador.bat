setlocal
cd ..
set LIB=..\..\lib
set CONFIG=.\config
set CLASS=.
attrib -r %CONFIG%\configAdministracion.ini
java -Dfile.encoding=windows-1252 -classpath "%CLASS%;%LIB%\productos\netbeans\AbsoluteLayout.jar;%LIB%\core\satec.jar;%LIB%\productos\log4j\log4j-1.2.6.jar;%LIB%\productos\jetty\ext\xercesImpl.jar;%LIB%\productos\castor\castor-0.9.5.3.jar;%LIB%\productos\httpclient\commons-httpclient-2.0.jar;%LIB%\productos\httpclient\commons-logging.jar;%LIB%\productos\puzzle\puzzled-classes-1.3.zip;%LIB%\productos\calendar\jcalendar.jar;config;" com.geopista.app.administrador.CMainAdministrador
endlocal
cd bin 
 pause
