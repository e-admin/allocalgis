@ECHO OFF
set PATH=C:\Program Files\apache-maven\apache-maven-3.0.5\bin\;%PATH%
set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_80\

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
REM MAVEN_NO_SIGN_REPO: URL del repositorio para componentes no firmados (Solo para entorno de desarrollo)
REM MAVEN_SIGN_REPO_EXT: URL del repositorio para de librerias firmadas
REM MAVEN_NO_SIGN_REPO_EXT: URL del repositorio de librerías no firmadas(Solo para entorno de desarrollo)

set signPath=C:\LocalGIS3\allocalgis\bin\sign

set MAVEN_SIGN_REPO=http://srvproges.grupotecopy.es/nexus/content/repositories/localgis3firm/
set MAVEN_NO_SIGN_REPO=http://srvproges.grupotecopy.es/nexus/content/repositories/localgis3/

set MAVEN_SIGN_REPO_EXT=http://srvproges.grupotecopy.es/nexus/content/repositories/localgis3libextfirm/
set MAVEN_NO_SIGN_REPO_EXT=http://srvproges.grupotecopy.es/nexus/content/repositories/locagis3libext/



