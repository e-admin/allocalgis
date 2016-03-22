export MAVEN_HOME=/usr/local/LocalGIS.III/apache-maven-2.2.1 
export JAVA_HOME=/usr/local/LocalGIS.III/jdk1.7.0_17
export SETTINGS=../settings/settings_lcg3_simple_sign_linux.xml
export PROPERTIES_FILE=install_linux_$1.properties


# Datos de registro
#export INSTALLATION_URL=http://localhost:9090/RegistryWS/services/RegistryService
export INSTALLATION_URL=file:/tmp/installation.xml
export REGISTRY_TYPE=ReadOnly
#export REGISTRY_TYPE=WebService

export PROMPT_CONFIRM=false
export OVERWRITE=true

export CATALINA_HOME=/usr/local/LocalGIS.III/apache-tomcat-7.0.37/
