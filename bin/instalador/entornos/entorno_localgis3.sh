export MAVEN_HOME=/usr 
export JAVA_HOME=/usr/lib/jvm/java-7-oracle

export SETTINGS=../settings/settings_lcg3_sign_linux.xml
export PROPERTIES_FILE=entornos/install_linux_$1.properties

# Datos de registro
#export INSTALLATION_URL=http://localhost:9090/RegistryWS/services/RegistryService
export INSTALLATION_URL=file:/tmp/installation.xml
export REGISTRY_TYPE=ReadOnly
#export REGISTRY_TYPE=WebService

export PROMPT_CONFIRM=true
export OVERWRITE=false

export CATALINA_HOME=/var/lib/tomcat7/
