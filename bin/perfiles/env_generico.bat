set appModulesPath=../../modulos/apps
set appModulesPath2=../modulos/apps

set appPluginsPath=../../modulos/plugins
set appPluginsPath2=../modulos/plugins
set appInstallerPath=../../instalador/trunk/UpdaterFramework
set appInstallerPath2=../instalador/trunk/UpdaterFramework
set appWebAppsPath=../../webapps
set appWebAppsPath2=../webapps

REM Valores para compilar aplicaciones
REM signPath: Localizacion del path donde se encuentra el keytstore con el certificado de firma
REM MAVEN_SIGN_REPO: URL del repositorio para componentes firmados
REM MAVEN_NO_SIGN_REPO: URL del repositorio para componentes no firmados (Solo para entorno de desarrollo con eclipse)


set signPath=c:\users\fjgarcia\Documents\Satec\Proyectos\LOCALGIS\LOCALGIS.FASEIII\3Codigo\localgis\bin\sign\

set MAVEN_SIGN_REPO=http://srvweblin.grupotecopy.es/nexus/content/repositories/localgis3/
set MAVEN_NO_SIGN_REPO=http://rossi.malab.satec.es:8080/artifactory/localgis3/


REM set TIPO_COMPILACION_DEFAULT=clean deploy
set TIPO_COMPILACION_DEFAULT=clean deploy

set PATH="c:\Program Files (x86)\apache-maven-2.2.1\bin\";%PATH%
set JAVA_HOME=c:\Program Files\Java\jdk1.7.0_21\

REM set OPTIONS_SIGN=-Djarsigner.skip=true