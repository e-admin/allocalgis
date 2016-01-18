CREATE TABLE  systemversion ("version" TEXT);
INSERT INTO systemversion VALUES /* ('');*/

/*
 * Tenemos que buscar una forma de generar esta informacion sin crearla a mano
 * ya que puede variar: version, nombre objeto, etc...
 *
 * */
('<?xml version="1.0" encoding="UTF-8"?><modules>
	<module>
	  <name>Modulo Datamodel RegistryWeb</name>
	  <version>3.0.0</version>
	  <artifact>
	    <groupId>${project.groupId}</groupId>
	    <artifactId>${project.artifactId}</artifactId>
	    <version>${project.version}</version>
	    <install>dataModel</install>
	  </artifact>
	  <dependencyInfo>
	    <dependencies/>
	  </dependencyInfo>
	</module>
</modules>');