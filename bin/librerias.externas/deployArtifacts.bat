@ECHO OFF

REM Esto hace que las variables no se creen en el entorno y que sea necesario
REM utiliar variables de tipo !variable! en lugar de %variable%
REM http://stackoverflow.com/questions/3650146/variable-assignment-problem-in-dos-batch-file-for-loop


setlocal ENABLEDELAYEDEXPANSION 

REM set ARRAY[3]=com.netscape,ldap,1.0,jar,false,ldap-1.0.jar
REM set ARRAY[5]=postgresql,postgresql,8.0-311.jdbc3,jar,false,postgresql-8.0-311.jdbc3.jar
REM set ARRAY[10]=com.localgis,agile_core,1.0.0,jar,false,agile_core-1.0.0.jar
REM set ARRAY[14]=org.alfresco,alfresco-web-service-client,3.4.e,jar,false,alfresco-web-service-client-3.4.e.jar
REM set ARRAY[15]=opensaml,opensaml,1.1,jar,false,opensaml-1.1.jar
REM set ARRAY[17]=ojdbc,ojdbc,10.1.0.2.0,jar,false,ojdbc-10.1.0.2.0.jar
REM set ARRAY[23]=com.jcraft,jsch,0.1.44,jar,false,jsch-0.1.44.jar
REM set ARRAY[33]=org.pdfbox,pdfbox,0.7.2,jar,false,pdfbox-0.7.2.jar
REM set ARRAY[36]=org.pdfbox,pdfbox,0.7.2,jar,false,pdfbox-0.7.2.jar
REM set ARRAY[38]=org.puzzled,puzzled,1.3,jar,false,puzzled-1.3.jar
REM set ARRAY[39]=org.objectweb,rmijdbc,1.0,jar,false,rmijdbc-1.0.jar
REM set ARRAY[46]=commons-javaflow,commons-javaflow,20060411,jar,false,commons-javaflow-20060411.jar
REM set ARRAY[47]=jcmdline,jcmdline,1.0.2,jar,false,jcmdline-1.0.2.jar
REM set ARRAY[45]=javax,javaws,1.1,jar,false,javaws-1.1.jar
REM set ARRAY[50]=eigenbase,eigenbase-xom,1.3.0.11999,jar,false,eigenbase-xom-1.3.0.11999.jar
REM set ARRAY[51]=eigenbase,eigenbase-resgen,1.3.0.11873,jar,false,eigenbase-resgen-1.3.0.11873.jar
REM set ARRAY[61]=eigenbase,eigenbase-properties,1.1.0.10924,jar,false,eigenbase-properties-1.1.0.10924.jar
REM set ARRAY[62]=org.flexdock,flexdock,0.4.1,jar,false,flexdock-0.4.1.jar
REM set ARRAY[63]=jdt-compiler,jdt-compiler,3.1.1,jar,false,jdt-compiler-3.1.1.jar
REM set ARRAY[64]=javax.xml,saaj-api,1.3,jar,false,saaj-api-1.3.jar
REM set ARRAY[65]=dalma-lib,jakarta-bcel,20050813,jar,false,jakarta-bcel-20050813.jar

REM  El mondrian lo he cambiado por la 3.0 ya que la 2.3 no esta en el repositorio
REM set ARRAY[66]=mondrian,mondrian,2.3.2.8944,jar,false,mondrian-2.3.2.8944.jar
REM set ARRAY[71]=javax.persistence,jpa,1.0,jar,false,jpa-1.0.jar
REM set ARRAY[72]=javax.persistence,ejb,3.0-public-draft-20060118,jar,false,ejb-3.0-public-draft-20060118.jar
REM set ARRAY[73]=org.bouncycastle,lbc-jdk,1.18,jar,false,lbc-jdk-1.18.jar
REM set ARRAY[76]=org.exolab,castor,0.9.5.3,jar,false,castor-0.9.5.3.jar
REM set ARRAY[82]=org.mortbay,jetty,1.0,jar,false,jetty-1.0.jar
REM set ARRAY[90]=itext,itext,1.4,jar,false,itext-1.4.jar
REM set ARRAY[91]=xml-security,xmlsec,1.4.1,jar,false,xmlsec-1.4.1.jar
REM set ARRAY[92]=acme,acme,1.0,jar,false,acme-1.0.jar
REM set ARRAY[96]=mail,mail,1.0,jar,false,mail-1.0.jar
REM set ARRAY[98]=javax.media.jai,mlibwrapper-jai,1.1.3,jar,false,mlibwrapper-jai-1.1.3.jar
REM set ARRAY[100]=msutil,msutil,1.0,jar,false,msutil-1.0.jar
REM set ARRAY[101]=postgis100,postgis100,1.0,jar,false,postgis100-1.0.jar
REM set ARRAY[102]=xerces,xerces,2.5.0,jar,false,xerces-2.5.0.jar

REM set ARRAY[18]=com.ibatis,ibatis2-common,2.2.0,jar,false,ibatis2-common-2.2.0.jar
REM set ARRAY[19]=com.ibatis,ibatis2-dao,2.2.0,jar,false,ibatis2-dao-2.2.0.jar
REM set ARRAY[20]=com.ibatis,ibatis2-sqlmap,2.2.0,jar,false,ibatis2-sqlmap-2.2.0.jar
REM set ARRAY[24]=nickyb,sqleonardo,2009.03.rc1,jar,false,sqleonardo-2009.03.rc1.jar
REM set ARRAY[25]=nickyb,sqleonardo,2009.01,jar,false,sqleonardo-2009.01.jar
REM set ARRAY[97]=msbase,msbase,1.0,jar,false,msbase-1.0.jar
REM set ARRAY[99]=mssqlserver,mssqlserver,1.0,jar,false,mssqlserver-1.0.jar



set ARRAY[0]=com.localgis,sizeof,1.0.0,jar,true,sizeof-1.0.0.jar
set ARRAY[1]=com.localgis,AbsoluteLayout,1.0.0,jar,true,AbsoluteLayout-1.0.0.jar
set ARRAY[2]=com.localgis,jcalendar,1.3.2,jar,true,jcalendar-1.3.2.jar
set ARRAY[3]=log4j,log4j,1.2.16,jar,true,log4j-1.2.16.jar
set ARRAY[4]=javax.pkcs11,javax.pkcs11,1.0,jar,false,javax.pkcs11-1.0.jar
set ARRAY[5]=mysql,mysql-connector-java,5.1.20,jar,true,mysql-connector-java-5.1.20.jar
set ARRAY[6]=com.localgis,mantisconnect,1.0,jar,true,mantisconnect-1.0.jar
set ARRAY[7]=com.localgis,jama,1.0.1,jar,true,jama-1.0.1.jar
set ARRAY[8]=com.localgis,deegree,1.0.1,jar,true,deegree-1.0.1.jar
set ARRAY[9]=com.localgis,agile_core,1.0.0,jar,true,agile_core-1.0.0.jar
set ARRAY[10]=commons-httpclient,commons-httpclient,3.1,jar,true,commons-httpclient-3.1.jar
set ARRAY[11]=org.apache,localgis-ssl,2.0,jar,false,localgis-ssl-2.0.jar
set ARRAY[12]=org.datavision,datavision,1.0,jar,true,datavision-1.0.jar
set ARRAY[13]=com.localgis,minml2,1.0.0,jar,false,minml2-1.0.0.jar
set ARRAY[14]=commons-ssl,commons-ssl,1.0,jar,true,commons-ssl-1.0.jar
set ARRAY[15]=org.apache.ftpserver,ftpserver-dev,1.0,jar,false,ftpserver-dev-1.0.jar
set ARRAY[16]=com.localgis,buoy,1.0.0,jar,true,buoy-1.0.0.jar
set ARRAY[17]=net.sf.jasperreports,jasperreports,5.0.1,jar,true,jasperreports-5.0.1.jar
set ARRAY[18]=org.alfresco,alfresco-web-service-client,3.4.e,jar,true,alfresco-web-service-client-3.4.e.jar
set ARRAY[19]=org.codehaus.castor,castor-core,1.3.2,jar,true,castor-core-1.3.2.jar
set ARRAY[20]=commons-discovery,commons-discovery,0.5,jar,true,commons-discovery-0.5.jar
set ARRAY[21]=mapscript,mapscript,1.0,jar,false,mapscript-1.0.jar
REM Esta libreria es propietaria y habría que dejar que la gente se la descargara de un sitio para desplegarla.
set ARRAY[22]=com.oracle,ojdbc14,10.2.0.4.0,jar,true,ojdbc14-10.2.0.4.0.jar
set ARRAY[23]=org.codehaus.castor,castor-xml,1.3.2,jar,true,castor-xml-1.3.2.jar
set ARRAY[24]=xerces,xercesImpl,2.9.1,jar,true,xercesImpl-2.9.1.jar
set ARRAY[25]=eclipse,jdtcore,3.1.0,jar,true,jdtcore-3.1.0.jar
set ARRAY[26]=com.tinyline,tinyline4pp,1.0,zip,false,tinyline4pp-1.0.zip
set ARRAY[27]=org.apache,log4jME,1.3,jar,false,log4jME-1.3.jar
set ARRAY[28]=com.japisoft,japisoft,1.0,jar,false,japisoft-1.0.jar
set ARRAY[29]=it.cnr.isti.domoware.tinyos,tinyos-core-rxtx,1.1.15.1,jar,true,tinyos-core-rxtx-1.1.15.1.jar
set ARRAY[30]=org.gnu,gnu-crypto,2.0.1,jar,false,gnu-crypto-2.0.1.jar
set ARRAY[31]=com.localgis,xmlsec,1.4.1,jar,true,xmlsec-1.4.1.jar
set ARRAY[32]=net.java.dev.flexdock,flexdock,0.4,jar,true,flexdock-0.4.jar
set ARRAY[33]=jasperreports,jasperreports,2.0.1,jar,true,jasperreports-2.0.1.jar


REM Libreria propietaria de Oracle
set ARRAY[34]=oracle,sdoapi,1.0,jar,false,sdoapi-1.0.jar
set ARRAY[35]=org.geotools,oracle-spatial,2.0,jar,false,oracle-spatial-2.0.jar

set ARRAY[37]=com.localgis,localgis-sigem,1.0,jar,true,localgis-sigem-1.0.jar
set ARRAY[40]=uk.co.wilson,MinML2,1.0,jar,false,MinML2-1.0.jar
set ARRAY[41]=org.eclipse,swt-3349-3448,1.0,zip,false,swt-3349-3448-1.0.zip
set ARRAY[42]=org.eclipse,swt,1.0,jar,false,swt-1.0.jar
set ARRAY[43]=org.eclipse,swt-Desktop,1.0,jar,false,swt-Desktop-1.0.jar
set ARRAY[44]=org.deegreewfs,deegree2wfs,2.0,jar,false,deegree2wfs-2.0.jar

REM para el apps-ireport
set ARRAY[48]=com.jaspersoft,babylon,1.0.0,jar,false,babylon-1.0.0.jar
set ARRAY[49]=com.jaspersoft,jasperreports-extensions,1.3.1,jar,false,jasperreports-extensions-1.3.1.jar

set ARRAY[67]=org.apache,cifrado,1.0,jar,false,cifrado-1.0.jar
set ARRAY[68]=rex,rex,20070125,jar,false,rex-20070125.jar
set ARRAY[69]=ireport,workdir,1.0,jar,false,workdir-1.0.jar
set ARRAY[70]=net.sf.jasperreports,cincom-jr-xmla,1.2.4,jar,false,cincom-jr-xmla-1.2.4.jar

set ARRAY[74]=iaik.me,iaik_ssl_me4se,1.0,jar,false,iaik_ssl_me4se-1.0.jar
set ARRAY[75]=iaik.me,iaik_jce_me4se,1.0,jar,false,iaik_jce_me4se-1.0.jar
set ARRAY[76]=org.exolab,castor,0.9.5.3,jar,true,castor-0.9.5.3.jar

set ARRAY[77]=org.mityc,LibreriaOCSP,0.9,jar,false,LibreriaOCSP-0.9.jar
set ARRAY[78]=org.mityc,LibreriaPolicy,0.9,jar,false,LibreriaPolicy-0.9.jar
set ARRAY[79]=org.mityc,LibreriaTSA,0.9,jar,false,LibreriaTSA-0.9.jar
set ARRAY[80]=org.mityc,LibreriaXADES,0.9,jar,false,LibreriaXADES-0.9.jar
set ARRAY[81]=org.mityc,LibreriaConfiguracion,0.9,jar,false,LibreriaConfiguracion-0.9.jar



REM http://stackoverflow.com/questions/5310477/how-do-i-get-maven-to-download-javax-comm-dependency
REM Dependencia con Licencia privativa
set ARRAY[84]=javax,comm,1.0,jar,false,comm-1.0.jar

REM La version que se descarga del swinglab no tiene todas las dependencias que la que tenemos nosotros.
set ARRAY[85]=org.swinglabs,swingx,1.6,jar,false,swingx-1.6.jar
set ARRAY[86]=surveyos,surveyos_utilties,1.0,jar,false,surveyos_utilties-1.0.jar
set ARRAY[87]=surveyos,gpx2,1.0,jar,false,gpx2-1.0.jar
set ARRAY[88]=surveyos,surveyos_main,1.0,jar,false,surveyos_main-1.0.jar

set ARRAY[93]=activation,activation,1.0,jar,false,activation-1.0.jar
set ARRAY[94]=org.deegree2,deegree2,2.1-rc1,jar,false,deegree2-2.1-rc1.jar

set ARRAY[95]=net.sf.datavision,DataVision,1.0.0,jar,true,DataVision-1.0.0.jar
set ARRAY[96]=org.apache.axis,axis,1.4,jar,true,axis-1.4.jar
set ARRAY[97]=org.apache.axis2,axis2,1.4.1,jar,true,axis2-1.4.1.jar

REM Este fichero axis2-1.5.3-jar.jar va con clasificador por lo que se mete al final de este fichero automaticamente.
REM set ARRAY[98]=org.apache.axis2,axis2,1.5.3,jar,true,axis2-1.5.3-jar.jar

set ARRAY[99]=org.apache.axis2,axis2-kernel,1.4.1,jar,true,axis2-kernel-1.4.1.jar
set ARRAY[100]=org.apache.ws.commons.axiom,axiom-api,1.2.8,jar,true,axiom-api-1.2.8.jar
set ARRAY[101]=org.apache.ws.commons.axiom,axiom-api,1.2.9,jar,true,axiom-api-1.2.9.jar
set ARRAY[102]=org.apache.ws.commons.axiom,axiom-impl,1.2.8,jar,true,axiom-impl-1.2.8.jar
set ARRAY[103]=org.apache.ws.commons.axiom,axiom-impl,1.2.9,jar,true,axiom-impl-1.2.9.jar
set ARRAY[104]=org.uva.itast,customizedGeotoolsGraph,0.0.2,jar,true,customizedGeotoolsGraph-0.0.2.jar
set ARRAY[105]=org.uva.itast,routeEngine,0.0.2,jar,true,routeEngine-0.0.2.jar
set ARRAY[106]=xerces,xercesImpl,2.4.0,jar,true,xercesImpl-2.4.0.jar
set ARRAY[107]=org.jdom,jdom,1.1,jar,true,jdom-1.1.jar
set ARRAY[108]=org.beanshell,bsh,2.0b4,jar,true,bsh-2.0b4.jar
set ARRAY[109]=jetty,javax.servlet,5.1.12,jar,true,javax.servlet-5.1.12.jar
set ARRAY[110]=third.byttersoft.main,jcmdline,1.0.2,jar,true,jcmdline-1.0.2.jar
set ARRAY[111]=com.thoughtworks.xstream,xstream,1.4.3,jar,false,xstream-1.4.3.jar
set ARRAY[112]=com.ibatis,ibatis2-dao-localgis,2.2.0,jar,true,ibatis2-dao-localgis-2.2.0.jar
set ARRAY[113]=com.ibatis,ibatis2-sqlmap-localgis,2.2.0,jar,true,ibatis2-sqlmap-localgis-2.2.0.jar
set ARRAY[114]=com.ibatis,ibatis2-common-localgis,2.2.0,jar,true,ibatis2-common-localgis-2.2.0.jar
set ARRAY[115]=org.objectweb,rmijdbc,3.3,jar,false,rmijdbc-3.3.jar
set ARRAY[116]=org.forgerock.openam,openam-clientsdk,10.1.0-Xpress,jar,true,openam-clientsdk-10.1.0-Xpress.jar
set ARRAY[117]=org.forgerock.openam,openam,10.1.0-Xpress,pom,false,openam-10.1.0-Xpress.pom


set ARRAY[118]=javax.javaws,javaws,1.6.0,jar,false,javaws-1.6.0.jar
set ARRAY[119]=org.uva.itast,IDELabRoute,0.0.2,pom,false,IDELabRoute-0.0.2.pom
set ARRAY[120]=org.uva.itast,featureStore,0.0.2,jar,true,featureStore-0.0.2.jar
set ARRAY[121]=es.uva.idelab.route,blocksAlgorithm,0.0.3,jar,true,blocksAlgorithm-0.0.3.jar
set ARRAY[122]=org.uva.itast,customizedGeotoolsGraph,0.0.2,jar,true,customizedGeotoolsGraph-0.0.2.jar
set ARRAY[123]=it.geosolutions,geoserver-manager,1.4.0,jar,true,geoserver-manager-1.4.0.jar


set ARRAY[124]=com.routeengine,routeEngine,1.0.0,jar,true,routeEngine-0.0.1-SNAPSHOT.jar
REM set ARRAY2[124]=com.routeengine,routeEngine,1.0.0,jar,false,routeEngine-1.0.0.jar


set ARRAY[125]=com.ibatis,ibatis2-common,2.2.0,jar,false,ibatis2-common-2.2.0.jar
set ARRAY[126]=com.ibatis,ibatis2-dao,2.2.0,jar,false,ibatis2-dao-2.2.0.jar
set ARRAY[127]=com.ibatis,ibatis2-sqlmap,2.2.0,jar,false,ibatis2-sqlmap-2.2.0.jar

set ARRAY[129]=pentaho,mondrian,3.0.3,jar,false,mondrian-3.0.3.jar


REM Libreria de ESRI
set ARRAY[130]=jsde90_sdk,jsde90_sdk,1.0,jar,false,jsde90_sdk-1.0.jar


REM Librerias que estaban en repositorios que han desaparecido. Las colgamos en el de localgis porque sino no funcionaria
set ARRAY[131]=com.ermapper,ermapper,2.3,jar,false,ermapper-2.3.jar
set ARRAY[132]=org.deegree.j3d,j3d_deegreeversion,1.0.0,jar,false,j3d_deegreeversion-1.0.0.jar
set ARRAY[133]=javax.media.jai,mlibwrapper-jai,1.1.3,jar,false,mlibwrapper-jai-1.1.3.jar
REM set ARRAY[134]=javax.media,jai-core,1.1.3,jar,false,jai-core-1.1.3.jar
REM set ARRAY[135]=javax.media,jai_codec,1.1.3.jar,false,jai_codec-1.1.3.jar
REM set ARRAY[136]=javax.media,jai_core,1.1.3.jar,false,jai_core-1.1.3.jar
REM set ARRAY[137]=javax.media,jai_imageio,1.1,jar,false,jai_imageio-1.1.jar

REM set ARRAY2[0[]=11

SET TODAYSDATE=%date:~6,8%-%date:~3,2%-%date:~0,2%
set PATH_LIB_EXTERNAL=%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1\libreriasExternas.bat
set TIPO_DEPLOY=install:install-file deploy:deploy-file

if exist "%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1\libreriasExternas.bat" (
	del %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1\libreriasExternas.bat
)

if not exist "%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1\" (
	mkdir %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1\
)
if not exist "%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1.nohayquefirmar\" (
	mkdir %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1.nohayquefirmar\
)

copy ..\settings\settings_lcg3_batch_deploy.xml %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1

echo @ECHO OFF >%PATH_LIB_EXTERNAL%
echo set URL=%URL% >>%PATH_LIB_EXTERNAL%

FOR /F "tokens=2,3,4,5,6,7,8 delims=[]=," %%A IN ('SET ARRAY[') DO ( 
	REM echo %%A -- %%B -- %%C -- %%D -- %%E -- %%F
	set groupId=%%B
	set artifactId=%%C
	set version=%%D
	set type=%%E
	set sign=%%F
	set file=%%G
	echo !artifactId!
	
	
	
	IF %GLOBAL_SIGN% == true (
	
		IF !sign! == true (
			echo "Firmando !file!"
			"%JAVA_HOME%bin\jarsigner" -keystore ..\sign\GeopistaKeyStore !file! -signedjar !file!.signed -storepass geopista99 -keypass geopista99 geopista
			IF ERRORLEVEL 1 GOTO ERROR
		)	
	)
	echo "Desplegando !file!"	
	IF %GLOBAL_SIGN% == true (
		IF !sign! == true (
			REM Copiamos el archivo sin firmar a la lista de archivos a firmar.
			copy !file! %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1\ 
	
			call mvn -s %SETTINGS% %TIPO_COMPILACION% -DgroupId=!groupId! -DartifactId=!artifactId! -Dversion=!version! -Dpackaging=!type! -Dfile=!file!.signed  -DrepositoryId=%REPOSITORYID% -Durl=%URL%	
			IF ERRORLEVEL 1 GOTO ERROR
			echo echo Desplegando : !file! >>%PATH_LIB_EXTERNAL%
			echo call mvn -s settings_lcg3_batch_deploy.xml %TIPO_DEPLOY% -DgroupId=!groupId! -DartifactId=!artifactId! -Dversion=!version! -Dpackaging=!type! -Dfile=!file!  -DrepositoryId=%REPOSITORYID% -Durl=%%URL%% >>%PATH_LIB_EXTERNAL%	
			echo IF ERRORLEVEL 1 GOTO ERROR >>%PATH_LIB_EXTERNAL%	
		)
		IF !sign! == false (		
			copy !file! %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1.nohayquefirmar\ 
			call mvn -s %SETTINGS% %TIPO_COMPILACION% -DgroupId=!groupId! -DartifactId=!artifactId! -Dversion=!version! -Dpackaging=!type! -Dfile=!file!  -DrepositoryId=%REPOSITORYID% -Durl=%URL%	
			IF ERRORLEVEL 1 GOTO ERROR
			REM echo echo Desplegando : !file! >>%PATH_LIB_EXTERNAL%
			REM echo call mvn -s settings_lcg3_batch_deploy.xml %TIPO_DEPLOY% -DgroupId=!groupId! -DartifactId=!artifactId! -Dversion=!version! -Dpackaging=!type! -Dfile=!file!  -DrepositoryId=%REPOSITORYID% -Durl=%%URL%% >>%PATH_LIB_EXTERNAL%	
			REM echo IF ERRORLEVEL 1 GOTO ERROR >>%PATH_LIB_EXTERNAL%	

		)
	)	
	IF %GLOBAL_SIGN% == false (
			copy !file! %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1.nohayquefirmar\ 
			call mvn -s %SETTINGS% %TIPO_COMPILACION% -DgroupId=!groupId! -DartifactId=!artifactId! -Dversion=!version! -Dpackaging=!type! -Dfile=!file!  -DrepositoryId=%REPOSITORYID% -Durl=%URL%	
			IF ERRORLEVEL 1 GOTO ERROR
			REM echo echo Desplegando : !file! >>%PATH_LIB_EXTERNAL%
			REM echo call mvn -s settings_lcg3_batch_deploy.xml %TIPO_DEPLOY% -DgroupId=!groupId! -DartifactId=!artifactId! -Dversion=!version! -Dpackaging=!type! -Dfile=!file!  -DrepositoryId=%REPOSITORYID% -Durl=%%URL%% >>%PATH_LIB_EXTERNAL%		
			REM echo IF ERRORLEVEL 1 GOTO ERROR >>%PATH_LIB_EXTERNAL%	
	)	
	
)

REM Este son casos especiales porque hay que incluir el classifier
REM call mvn -s %SETTINGS% %TIPO_COMPILACION% -DgroupId=com.michaelbaranov -DartifactId=microba -Dversion=0.4.1 -Dpackaging=jar -Dfile=microba-0.4.1-bin.jar  -DrepositoryId=%REPOSITORYID% -Dclassifier=bin -Durl=%URL%
REM call mvn -s %SETTINGS% %TIPO_COMPILACION% -DgroupId=org.apache.axis2 -DartifactId=axis2 -Dversion=1.5.3 -Dpackaging=jar -Dfile=axis2-1.5.3-jar.jar.signed  -DrepositoryId=%REPOSITORYID% -Dclassifier=jar -Durl=%URL%

REM Copiamos el archivo sin firmar a la lista de archivos a firmar.


IF %GLOBAL_SIGN% == true (
		copy axis2-1.5.3-jar.jar %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1\ 
		"%JAVA_HOME%bin\jarsigner" -keystore ..\sign\GeopistaKeyStore axis2-1.5.3-jar.jar -signedjar axis2-1.5.3-jar.jar.signed -storepass geopista99 -keypass geopista99 geopista
		call mvn -s %SETTINGS% %TIPO_COMPILACION% -DgroupId=org.apache.axis2 -DartifactId=axis2 -Dversion=1.5.3 -Dpackaging=jar -Dfile=axis2-1.5.3-jar.jar.signed  -DrepositoryId=%REPOSITORYID% -Dclassifier=jar -Durl=%URL%	
		IF ERRORLEVEL 1 GOTO ERROR
		echo echo Desplegando : axis2-1.5.3-jar.jar >>%PATH_LIB_EXTERNAL%
		echo mvn -s settings_lcg3_batch_deploy.xml %TIPO_DEPLOY% -DgroupId=org.apache.axis2 -DartifactId=axis2 -Dversion=1.5.3 -Dpackaging=jar -Dfile=axis2-1.5.3-jar.jar  -DrepositoryId=%REPOSITORYID% -Dclassifier=jar -Durl=%%URL%%  >>%PATH_LIB_EXTERNAL%
		echo IF ERRORLEVEL 1 GOTO ERROR >>%PATH_LIB_EXTERNAL%
)	

IF %GLOBAL_SIGN% == false (
		copy !file! %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.externas1.nohayquefirmar\ 
		call mvn -s %SETTINGS% %TIPO_COMPILACION% -DgroupId=org.apache.axis2 -DartifactId=axis2 -Dversion=1.5.3 -Dpackaging=jar -Dfile=axis2-1.5.3-jar.jar  -DrepositoryId=%REPOSITORYID% -Dclassifier=jar -Durl=%URL%
		IF ERRORLEVEL 1 GOTO ERROR
		REM echo echo Desplegando : axis2-1.5.3-jar.jar >>%PATH_LIB_EXTERNAL%
		REM echo mvn -s settings_lcg3_batch_deploy.xml %TIPO_DEPLOY% -DgroupId=org.apache.axis2 -DartifactId=axis2 -Dversion=1.5.3 -Dpackaging=jar -Dfile=axis2-1.5.3-jar.jar  -DrepositoryId=%REPOSITORYID% -Dclassifier=jar -Durl=%%URL%%  >>%PATH_LIB_EXTERNAL%
		REM echo IF ERRORLEVEL 1 GOTO ERROR >>%PATH_LIB_EXTERNAL%
)	

echo GOTO OK >>%PATH_LIB_EXTERNAL%		
echo :ERROR >>%PATH_LIB_EXTERNAL%		
echo echo Error al realizar el despliegue >>%PATH_LIB_EXTERNAL%		
echo GOTO END >>%PATH_LIB_EXTERNAL%		
echo :OK >>%PATH_LIB_EXTERNAL%		
echo echo Despliegue finalizado >>%PATH_LIB_EXTERNAL%		
echo :END >>%PATH_LIB_EXTERNAL%		


endlocal



GOTO FIN

:ERROR
ECHO "Error al subir los artifacts"

:FIN
ECHO Finalizando.
