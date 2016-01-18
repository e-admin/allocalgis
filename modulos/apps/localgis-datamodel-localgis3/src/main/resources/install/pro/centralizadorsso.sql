SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;


--****************************************************************************************************************
--****************************************************************************************************************
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'apps') THEN
		delete  from dictionary where id_vocablo in (select id_dictionary from apps);	
		delete from apps; 
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";


/* Crear tabla app_acl */
DROP TABLE IF EXISTS apps;
CREATE TABLE apps
(
  app character varying(30) NOT NULL,
  id_dictionary numeric,
  acl character varying(50) NOT NULL,
  perm character varying(150) NOT NULL,
  app_type character varying(10) NOT NULL, 
  path character varying(200) NOT NULL,
  install_name character varying(50) NOT NULL,
  active boolean DEFAULT true NOT NULL,
  CONSTRAINT app_pk PRIMARY KEY (app)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE apps OWNER TO postgres;
  

delete from dictionary where id_vocablo in (select id_dictionary from apps);

delete from apps; 
 
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Editor GIS');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Editor GIS');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Editor GIS');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', 'Editor GIS');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Editor GIS');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Editor GIS', currval('seq_dictionary'), 'App.LocalGIS.Editor', 'LocalGIS.Editor.Login', 'DESKTOP', '/software/localgis-apps-editor.jnlp', 'EditorModule');

/*INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Información de Referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Informació de Referència');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Información de Referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Información de Referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Informació de Referència');
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path) VALUES 
('Información de referencia', currval('seq_dictionary'), 'App.LocalGIS.InfoReferencia', 'LocalGIS.InfoReferencia.Login', 'DESKTOP', '/software/localgis-informacion_de_referencia.jnlp');*/

--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Planeamiento');
--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Planejament');
--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Planeamiento');
--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Planeamiento');
--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Planejament');  
--INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path) VALUES 
--('Planeamiento', currval('seq_dictionary'), 'App.LocalGIS.Planeamiento', 'LocalGIS.Planeamiento.Login', 'DESKTOP', '/software/localgis-apps-planeamiento.jnlp');

--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Infraestructuras');
--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul d´Infraestructures');
--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Infraestructuras');
--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Infraestructuras');
--INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul d´Infraestructures');  
--INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path) VALUES 
--('Infraestructuras', currval('seq_dictionary'), 'App.LocalGIS.Infraestructuras', 'LocalGIS.Infraestructuras.Login', 'DESKTOP', '/software/localgis_infraestructuras.jnlp');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Administración');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul d´Administració');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Administración');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Administración');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul d´Administració');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Administración', currval('seq_dictionary'), 'App.LocalGIS.Administracion', 'LocalGIS.Administracion.Login', 'DESKTOP', '/software/localgis-apps-administracion.jnlp', 'AdministracionModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Gestión Medioambiental');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Gestió Mediambiental');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Xestión Medioambiental');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Gestión Medioambiental');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Gestió Mediambiental');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Gestor Medioambiental', currval('seq_dictionary'), 'Administracion', 'Geopista.Contaminantes.Login', 'DESKTOP', '/software/localgis-apps-contaminantes.jnlp', 'ContaminantesModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Licencias de Obra Mayor y Obra Menor');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Llicències d´Obra Major i Obra Menor');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Licencias de Obra Mayor y Obra Menor');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Licencias de Obra Mayor y Obra Menor');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Llicències d´Obra Major i Obra Menor');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Licencias de Obra', currval('seq_dictionary'), 'App.LocalGIS.LicenciasObra', 'LocalGIS.LicenciasObra.Login', 'DESKTOP', '/software/localgis-apps-licenciasobra.jnlp', 'LicenciasObraModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Licencias de Actividad');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Llicències d´Activitat');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Licencias de Actividade');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Licencias de Actividad');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Llicències d´Activitat');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Licencias de Actividad', currval('seq_dictionary'), 'App.LocalGIS.LicenciasActividad', 'LocalGIS.LicenciasActividad.Login', 'DESKTOP', '/software/localgis-apps-licenciasactividad.jnlp', 'LicenciasActividadModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Metadatos');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Metadades');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Metadatos');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Metadatos');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Metadades');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Metadatos', currval('seq_dictionary'), 'Metadatos', 'Geopista.Metadatos.Login', 'DESKTOP', '/software/localgis-apps-metadatos.jnlp', 'MetadatosModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Ocupaciones');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul d´Ocupacions');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Ocupacións');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Ocupaciones');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul d´Ocupacions');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Ocupaciones', currval('seq_dictionary'), 'Ocupaciones', 'Geopista.Ocupaciones.Login', 'DESKTOP', '/software/localgis-apps-ocupaciones.jnlp', 'OcupacionesModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Catastro');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Cadastre');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Catastro');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Catastro');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Cadastre');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Catastro', currval('seq_dictionary'), 'App.LocalGIS.Catastro', 'LocalGIS.Catastro.Login', 'DESKTOP', '/software/localgis-apps-catastro.jnlp', 'CatastroModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Gestión de Capas');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul Gestor de Capes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Xestión de Capas');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Gestión de Capas');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul Gestor de Capes');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Gestor de Capas', currval('seq_dictionary'), 'App.LocalGIS.GestorCapas', 'LocalGIS.GestorCapas.Login', 'DESKTOP', '/software/localgis-apps-layerutil.jnlp', 'GestorCapasModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Backup');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Backup');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Backup');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Backup');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Backup');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Backup', currval('seq_dictionary'), 'App.LocalGIS.Backup', 'LocalGIS.Backup.Login', 'DESKTOP', '/software/localgis-apps-backup.jnlp', 'BackupModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Generación de Informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Generació d´Informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Xeración de Informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Generación de Informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Generació d´Informes');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Generador de Informes', currval('seq_dictionary'), 'App.LocalGIS.Informes', 'LocalGIS.Informes.Login', 'DESKTOP', '/software/localgis-apps-ireport.jnlp', 'IReportModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Inventario de Patrimonio');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul d´Inventari de Patrimoni');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Inventario de Patrimonio');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Inventario de Patrimonio');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul d´Inventari de Patrimoni');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Inventario', currval('seq_dictionary'), 'Inventario', 'Geopista.Inventario.Login', 'DESKTOP', '/software/localgis-apps-inventario.jnlp', 'InventarioModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Gestión del Espacio Público');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul d´Espai Públic');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Gestión del Espacio Público');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Gestión del Espacio Público');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul d´Espai Públic');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Gestión del Espacio Público', currval('seq_dictionary'), 'Gestion de Espacio Publico', 'localgis.espaciopublico.login', 'DESKTOP', '/software/localgis-apps-espaciopublico.jnlp', 'EspacioPublicoModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de la EIEL');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de la EIEL');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo da EIEL');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de la EIEL');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de la EIEL');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('EIEL', currval('seq_dictionary'), 'EIEL', 'LocalGis.EIEL.Login', 'DESKTOP', '/software/localgis-apps-eiel.jnlp', 'EIELModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Cementerios');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Cementiris');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Cementerios');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Cementerios');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Cementiris');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Cementerios', currval('seq_dictionary'), 'Cementerios', 'Geopista.Cementerios.Login', 'DESKTOP', '/software/localgis-apps-cementerios.jnlp', 'CementeriosModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Módulo de Contaminantes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Mòdul de Contaminants');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Módulo de Contaminantes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Módulo de Contaminantes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Mòdul de Contaminants');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Contaminantes', currval('seq_dictionary'), 'Contaminantes', 'Geopista.Contaminantes.Login', 'DESKTOP', '/software/localgis-apps-contaminantes.jnlp', 'ContaminantesModule');



INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Información de referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Información de referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Información de referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', 'Información de referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Información de referencia');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Información de referencia', currval('seq_dictionary'), 'App.LocalGIS.InfoReferencia', 'App.LocalGIS.InfoReferencia', 'DESKTOP', '/software/localgis-apps-inicio.jnlp', ' ');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Asistente para generación de informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Asistente para generación de informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Asistente para generación de informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', 'Asistente para generación de informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Asistente para generación de informes');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Asistente Informes', currval('seq_dictionary'), 'App.LocalGIS.Informes', 'LocalGIS.Informes.Login', 'DESKTOP', '/software/localgis-apps-asistenteinformes.jnlp', ' ');


INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'LocalGIS WMS Manager');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]LocalGIS WMS Manager');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]LocalGIS WMS Manager');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]LocalGIS WMS Manager');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', '[va]LocalGIS WMS Manager');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('WMS Manager', currval('seq_dictionary'), 'Visualizador', 'Geopista.Visualizador.Login', 'WEB', '/localgis-wmsmanager/admin/index.jsp', 'LocalgisWmsManagerModule');

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'LocalGIS Guía Urbana Privada');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]LocalGIS Guía Urbana Privada');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]LocalGIS Guía Urbana Privada');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]LocalGIS Guía Urbana Privada');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', '[va]LocalGIS Guía Urbana Privada');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Guía Urbana', currval('seq_dictionary'), 'Visualizador', 'Geopista.Visualizador.Login', 'WEB', '/localgis-guiaurbana/private/', 'GuiaUrbanaModule');


INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestor Fip');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestor Fip');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestor Fip');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestor Fip');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestor Fip');  
INSERT INTO apps (app, id_dictionary, acl, perm, app_type, path, install_name) VALUES 
('Fip', currval('seq_dictionary'), '"GestorFip"', '"LocalGIS.GestorFip.Login"', 'DESKTOP', '/software/localgis-apps-gestorfip.jnlp', 'GestorFipModule');


