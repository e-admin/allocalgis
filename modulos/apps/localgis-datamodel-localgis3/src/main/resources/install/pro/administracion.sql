SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;


/* Crear tabla app_acl */
DROP TABLE IF EXISTS app_acl;
CREATE TABLE app_acl
(
  app numeric(10,0) NOT NULL,
  acl numeric(10,0) NOT NULL,
  CONSTRAINT app_acl_pk PRIMARY KEY (app, acl),
  CONSTRAINT acl_fk FOREIGN KEY (acl)
      REFERENCES acl (idacl) MATCH FULL
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT app_fk FOREIGN KEY (app)
      REFERENCES appgeopista (appid) MATCH FULL
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE app_acl
  OWNER TO postgres;

/* Completar tabla Apps */

alter table iusercnt DROP CONSTRAINT fk_iusercnt_reference_appgeopi;
alter table session_app DROP CONSTRAINT fk_sessionapp_reference_appgeopi;

delete from app_acl;

DELETE FROM appgeopista;
INSERT INTO appgeopista (appid, def) VALUES 
(0, 'Capas'), 
(1,'Licencias Obra'),
(2,'Administracion'),
(3,'Metadatos'),
(4,'Geopista'),
(5,'Planeamiento'),
(6,'Catastro'),
(7,'Contaminantes'),
(8,'Inventario'),
(9,'Ocupaciones'),
(10,'RegistroExpedientes'),
(11,'Single Sign On'),
(12,'EIEL'),
(13,'Web'),
(14,'Movilidad'),
(15,'Gestor Capas'),
(16,'Gestion Espacio Publico'),
(17,'Aplicaciones Integracion'),
(18,'Cementerios'),
(19,'Licencias Actividad'),
(20,'Backup'),
(21,'Informacion de Referencia'),
(22,'Infraestructuras'),
(23,'ALP'),
(24,'Informes');

/*  */
  
/* Listado de asociacion App-Acl */
/* select app.def, acl.name, appid, idacl from app_acl JOIN appgeopista app ON(app_acl.app=app.appid) JOIN acl ON(app_acl.acl=acl.idacl) */

INSERT INTO app_acl (app, acl) VALUES 
((SELECT appid FROM appgeopista WHERE def='Licencias Obra'),(SELECT idacl FROM acl WHERE name='Licencias de Obra')),
((SELECT appid FROM appgeopista WHERE def='Licencias Obra'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.LicenciasObra')),
((SELECT appid FROM appgeopista WHERE def='Licencias Actividad'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.LicenciasActividad')),
((SELECT appid FROM appgeopista WHERE def='Ocupaciones'),(SELECT idacl FROM acl WHERE name='Ocupaciones')),
((SELECT appid FROM appgeopista WHERE def='Backup'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.Backup')),
((SELECT appid FROM appgeopista WHERE def='Administracion'),(SELECT idacl FROM acl WHERE name='Administracion')),
((SELECT appid FROM appgeopista WHERE def='Administracion'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.Administracion')),
((SELECT appid FROM appgeopista WHERE def='Informacion de Referencia'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.InfoReferencia')),
((SELECT appid FROM appgeopista WHERE def='Metadatos'),(SELECT idacl FROM acl WHERE name='Metadatos')),
((SELECT appid FROM appgeopista WHERE def='Planeamiento'),(SELECT idacl FROM acl WHERE name='Planeamiento')),
((SELECT appid FROM appgeopista WHERE def='Planeamiento'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.Planeamiento')),
((SELECT appid FROM appgeopista WHERE def='Informes'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.Informes')),
((SELECT appid FROM appgeopista WHERE def='ALP'),(SELECT idacl FROM acl WHERE name='ALP')),
((SELECT appid FROM appgeopista WHERE def='Infraestructuras'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.Infraestructuras')),
((SELECT appid FROM appgeopista WHERE def='Geopista'),(SELECT idacl FROM acl WHERE name='General')),
((SELECT appid FROM appgeopista WHERE def='Geopista'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.Editor')),
((SELECT appid FROM appgeopista WHERE def='Catastro'),(SELECT idacl FROM acl WHERE name='Catastro')),
((SELECT appid FROM appgeopista WHERE def='Catastro'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.Catastro')),
((SELECT appid FROM appgeopista WHERE def='Contaminantes'),(SELECT idacl FROM acl WHERE name='Actividades Contaminantes')),
((SELECT appid FROM appgeopista WHERE def='Contaminantes'),(SELECT idacl FROM acl WHERE name='Capas Inventario Patrimonio')),
((SELECT appid FROM appgeopista WHERE def='RegistroExpedientes'),(SELECT idacl FROM acl WHERE name='RegistroExpedientes')),
((SELECT appid FROM appgeopista WHERE def='Inventario'),(SELECT idacl FROM acl WHERE name='Patrimonio')),
((SELECT appid FROM appgeopista WHERE def='Inventario'),(SELECT idacl FROM acl WHERE name='Inventario'));


INSERT INTO app_acl (app, acl) VALUES 
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='EIEL')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='EIEL_PMR_Enrutamiento')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Captacion')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Deposito')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Tramos de Conduccion')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Tratamientos de Potabilizacion')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Red de distribucion domiciliaria')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Elemento puntual de abastecimiento'));

INSERT INTO app_acl (app, acl) VALUES 
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Emisario')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Colector')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Depuradora')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Red de ramales')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Punto de vertido')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Elementos puntuales')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Instalacion deportiva'));

INSERT INTO app_acl (app, acl) VALUES 
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Centros culturales o de esparcimiento')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Parques jardines y áreas naturales')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Lonjas, mercados y recintos feriales')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Cementerios' ORDER BY idacl desc LIMIT 1)),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Tanatorios')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Centros Sanitarios')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Centros Asistenciales')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Centros de enseñanza')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Centro de protección civil')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Casas consistoriales')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Edificios de titularidad publica sin uso')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Infraestructura viaria'));

INSERT INTO app_acl (app, acl) VALUES 
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Tramos de carretera')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Cuadro de mando')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Estabilizador')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Punto de luz')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Vertedero')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Parroquias')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Edificaciones singulares')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Comarcas EIEL')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Municipios EIEL')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Municipios EIEL puntos')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Nucleos Poblacion EIEL')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Nucleos Poblacion EIEL Puntos')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Parcelas EIEL')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Provincias EIEL')),
((SELECT appid FROM appgeopista WHERE def='EIEL'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'));


INSERT INTO app_acl (app, acl) VALUES 
((SELECT appid FROM appgeopista WHERE def='Web'),(SELECT idacl FROM acl WHERE name='Visualizador')),
((SELECT appid FROM appgeopista WHERE def='Movilidad'),(SELECT idacl FROM acl WHERE name='Movilidad')),
((SELECT appid FROM appgeopista WHERE def='Gestor Capas'),(SELECT idacl FROM acl WHERE name='Gestor Capas')),
((SELECT appid FROM appgeopista WHERE def='Gestor Capas'),(SELECT idacl FROM acl WHERE name='App.LocalGIS.GestorCapas')),
((SELECT appid FROM appgeopista WHERE def='Gestor Capas'),(SELECT idacl FROM acl WHERE name='Capas Importadas')),
((SELECT appid FROM appgeopista WHERE def='Gestion Espacio Publico'),(SELECT idacl FROM acl WHERE name='Gestion de Espacio Publico')),
((SELECT appid FROM appgeopista WHERE def='Aplicaciones Integracion'),(SELECT idacl FROM acl WHERE name='LocalWeb')),
((SELECT appid FROM appgeopista WHERE def='Aplicaciones Integracion'),(SELECT idacl FROM acl WHERE name='SIGEM')),
((SELECT appid FROM appgeopista WHERE def='Aplicaciones Integracion'),(SELECT idacl FROM acl WHERE name='SICALWIN')),
((SELECT appid FROM appgeopista WHERE def='Aplicaciones Integracion'),(SELECT idacl FROM acl WHERE name='ALP')),
((SELECT appid FROM appgeopista WHERE def='Cementerios'),(SELECT idacl FROM acl WHERE name='Cementerios' ORDER BY idacl asc LIMIT 1)),
((SELECT appid FROM appgeopista WHERE def='Cementerios'),(SELECT idacl FROM acl WHERE name='Capas Cementerios'));

 
/* Inserta capas en el listado de asociacion App-Acl */
INSERT INTO app_acl (app, acl) SELECT DISTINCT (SELECT  app.appid FROM appgeopista app WHERE def='Capas'), idacl FROM acl JOIN layers ON (acl.idacl=layers.acl);

alter table iusercnt ADD CONSTRAINT fk_iusercnt_reference_appgeopi FOREIGN KEY (appid) REFERENCES appgeopista (appid) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE NO ACTION;

alter table session_app ADD CONSTRAINT fk_sessionapp_reference_appgeopi FOREIGN KEY (appid) REFERENCES appgeopista (appid) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

/* Apps-ACLs-Permisos */
delete from query_catalog where id='aclPermisosDeApp';
INSERT INTO query_catalog (id, query, acl, idperm) VALUES ('aclPermisosDeApp', 'select acl.idacl as idacl, acl.name as name, usrgrouperm.def as def, r_acl_perm.idperm as idperm from acl, r_acl_perm, usrgrouperm, app_acl where acl.idacl=r_acl_perm.idacl and usrgrouperm.idperm=r_acl_perm.idperm and app_acl.acl=acl.idacl and app_acl.app=? order by idacl', 1, 9205);

/* Todas las Apps */
delete from query_catalog where id='allapps';
INSERT INTO query_catalog (id, query, acl, idperm) VALUES ('allapps', 'select appgeopista.appid as idapp, appgeopista.def as nameapp, acl.idacl as idacl, acl.name as nameacl, usrgrouperm.def as def, r_acl_perm.idperm as idperm from appgeopista, app_acl, acl, r_acl_perm, usrgrouperm where appgeopista.appid=app_acl.app and app_acl.acl=acl.idacl and acl.idacl=r_acl_perm.idacl and usrgrouperm.idperm=r_acl_perm.idperm order by appgeopista.def, acl.name, usrgrouperm.def ', 1, 9205);

/* Insertar App-ACL */
delete from query_catalog where id='LMInsertarAppAcl';
INSERT INTO query_catalog (id, query, acl, idperm) VALUES ('LMInsertarAppAcl', 'INSERT INTO app_acl (app,acl) VALUES (?, ?)', 1, 9205);

/* Recupera entidades ordenadas por nombre para la asociación con entidad SiGM */
delete from query_catalog where id='getEntidadesWithExtSortedByName';
INSERT INTO query_catalog (id, query, acl, idperm) VALUES ('getEntidadesWithExtSortedByName', 'SELECT es.id_entidad as id_entidad, nombreoficial, srid, backup, aviso, periodicidad, intentos, id_entidadext FROM public.entidad_supramunicipal es LEFT JOIN geowfst.entidadlocalgis_entidadext ee ON(es.id_entidad=ee.id_entidad) ORDER BY nombreoficial', 1, 9205);

