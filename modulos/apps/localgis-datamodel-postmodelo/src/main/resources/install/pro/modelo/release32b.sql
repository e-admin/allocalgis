-- Registrar los atributos en la tabla COLUMNS 
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM DICTIONARY WHERE TRADUCCION = 'Mapa EIEL Export SHP') THEN	
		-- Mapa EIEL SHP
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Mapa EIEL Export SHP');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Mapa EIEL Export SHP');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Mapa EIEL Export SHP');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Mapa EIEL Export SHP');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Mapa EIEL Export SHP');
		INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>',0);

		--CREACION DE LAYERFAMILIES
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_EXPORT_SHP');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_EXPORT_SHP');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_EXPORT_SHP');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_EXPORT_SHP');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_EXPORT_SHP');
		INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";



select f_add_col('public.vias','tipo', 'ALTER TABLE vias ADD COLUMN tipo varchar(20);');

/*
DROP TABLE IF EXISTS eiel_configuracion_shp;
CREATE TABLE eiel_configuracion_shp(
		fase VARCHAR(4)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_configuracion_shp OWNER TO geopista;
GRANT ALL ON TABLE eiel_configuracion_shp TO geopista;
GRANT SELECT ON TABLE eiel_configuracion_shp TO consultas;
*/

delete from eiel_configuracion_shp;
INSERT INTO eiel_configuracion_shp(fase) values ('2012');

/*
INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'eiel_configuracion_shp',-1);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'fase',null,CURRVAL('seq_tables'),4,NULL,NULL,3);
*/

-- Creación de tabla de nucleos abandonados: eiel_c_nucleo_aband

/*
CREATE TABLE eiel_c_nucleo_aband(
  id numeric(8,0),
  id_municipio numeric(5,0),
  "GEOMETRY" geometry,
  prov character varying(2),
  mun character varying(3),
  ent character varying(4),
  poblamiento character varying(2),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_c_nucleo_aband_pkey PRIMARY KEY (id , revision_actual )
 )
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_c_nucleo_aband
  OWNER TO geopista;
GRANT ALL ON TABLE eiel_c_nucleo_aband TO geopista;
GRANT SELECT ON TABLE eiel_c_nucleo_aband TO consultas;

INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'eiel_c_nucleo_aband',1);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',(SELECT id from domains where name = 'Autonumérico incremental'),CURRVAL('seq_tables'),8,NULL,NULL,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',(SELECT id from domains where name = 'Municipio obligatorio'),CURRVAL('seq_tables'),5,NULL,NULL,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',null,CURRVAL('seq_tables'),null,NULL,NULL,1);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'prov',null,CURRVAL('seq_tables'),2,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'mun',null,CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'ent',null,CURRVAL('seq_tables'),4,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'poblamiento',null,CURRVAL('seq_tables'),2,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'revision_actual',null,CURRVAL('seq_tables'),null,10,0,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'revision_expirada',null,CURRVAL('seq_tables'),null,10,0,2);
*/
-- Creación de tabla de suelo rural preservado: eiel_c_suelo_rural_preservado

/*
DROP TABLE IF EXISTS eiel_c_suelo_rural_preservado;
CREATE TABLE eiel_c_suelo_rural_preservado(
  id numeric(8,0),
  id_municipio numeric(5,0),
  "GEOMETRY" geometry,
  prov character varying(2),
  mun character varying(3),
  tipo_urba character varying(3),
  estado_tra character varying(2),
  denominaci character varying(40),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_c_suelo_rural_preservado_pkey PRIMARY KEY (id , revision_actual )
 )
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_c_suelo_rural_preservado
  OWNER TO geopista;
GRANT ALL ON TABLE eiel_c_suelo_rural_preservado TO geopista;
GRANT SELECT ON TABLE eiel_c_suelo_rural_preservado TO consultas;

INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'eiel_c_suelo_rural_preservado',11);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',(SELECT id from domains where name = 'Autonumérico incremental'),CURRVAL('seq_tables'),8,NULL,NULL,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',(SELECT id from domains where name = 'Municipio obligatorio'),CURRVAL('seq_tables'),5,NULL,NULL,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',null,CURRVAL('seq_tables'),null,NULL,NULL,1);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'prov',null,CURRVAL('seq_tables'),2,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'mun',null,CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo_urba',null,CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'estado_tra',null,CURRVAL('seq_tables'),2,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'denominaci',null,CURRVAL('seq_tables'),40,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'revision_actual',null,CURRVAL('seq_tables'),null,10,0,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'revision_expirada',null,CURRVAL('seq_tables'),null,10,0,2);

-- Creación de tabla de suelo rural: eiel_c_suelo_rural
*/
/*
DROP TABLE IF EXISTS eiel_c_suelo_rural;
CREATE TABLE eiel_c_suelo_rural(
  id numeric(8,0),
  id_municipio numeric(5,0),
  "GEOMETRY" geometry,
  prov character varying(2),
  mun character varying(3),
  tipo_urba character varying(3),
  estado_tra character varying(2),
  denominaci character varying(40),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_c_suelo_rural_pkey PRIMARY KEY (id , revision_actual )
 )
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_c_suelo_rural
  OWNER TO geopista;
GRANT ALL ON TABLE eiel_c_suelo_rural TO geopista;
GRANT SELECT ON TABLE eiel_c_suelo_rural TO consultas;

INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'eiel_c_suelo_rural',11);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',(SELECT id from domains where name = 'Autonumérico incremental'),CURRVAL('seq_tables'),8,NULL,NULL,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',(SELECT id from domains where name = 'Municipio obligatorio'),CURRVAL('seq_tables'),5,NULL,NULL,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',null,CURRVAL('seq_tables'),null,NULL,NULL,1);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'prov',null,CURRVAL('seq_tables'),2,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'mun',null,CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo_urba',null,CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'estado_tra',null,CURRVAL('seq_tables'),2,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'denominaci',null,CURRVAL('seq_tables'),40,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'revision_actual',null,CURRVAL('seq_tables'),null,10,0,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'revision_expirada',null,CURRVAL('seq_tables'),null,10,0,2);
-- Creación de tabla de suelo urbano: eiel_c_suelo_urbano
*/

/*
DROP TABLE IF EXISTS eiel_c_suelo_urbano;
CREATE TABLE eiel_c_suelo_urbano(
  id numeric(8,0),
  id_municipio numeric(5,0),
  "GEOMETRY" geometry,
  prov character varying(2),
  mun character varying(3),
  tipo_urba character varying(3),
  estado_tra character varying(2),
  denominaci character varying(40),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_c_suelo_urbano_pkey PRIMARY KEY (id , revision_actual )  
 )
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_c_suelo_urbano
  OWNER TO geopista;
GRANT ALL ON TABLE eiel_c_suelo_urbano TO geopista;
GRANT SELECT ON TABLE eiel_c_suelo_urbano TO consultas;

INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'eiel_c_suelo_urbano',11);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',(SELECT id from domains where name = 'Autonumérico incremental'),CURRVAL('seq_tables'),8,NULL,NULL,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',(SELECT id from domains where name = 'Municipio obligatorio'),CURRVAL('seq_tables'),5,NULL,NULL,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',null,CURRVAL('seq_tables'),null,NULL,NULL,1);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'prov',null,CURRVAL('seq_tables'),2,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'mun',null,CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo_urba',null,CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'estado_tra',null,CURRVAL('seq_tables'),2,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'denominaci',null,CURRVAL('seq_tables'),40,NULL,NULL,3);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'revision_actual',null,CURRVAL('seq_tables'),null,10,0,2);
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'revision_expirada',null,CURRVAL('seq_tables'),null,10,0,2);
*/

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Capas EIEL SHP') THEN	
		INSERT INTO acl(idacl,name) values(nextval('seq_acl'),'Capas EIEL SHP');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(871, (select idacl from acl where name = 'Capas EIEL SHP' limit 1));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(872,  (select idacl from acl where name = 'Capas EIEL SHP' limit 1));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(873, (select idacl from acl where name = 'Capas EIEL SHP' limit 1));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(874, (select idacl from acl where name = 'Capas EIEL SHP' limit 1));

		INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES ((select id from iusergrouphdr where name = 'EIEL'),871,(select idacl from acl where name = 'Capas EIEL SHP' limit 1));
		INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES ((select id from iusergrouphdr where name = 'EIEL'),872,(select idacl from acl where name = 'Capas EIEL SHP' limit 1));
		INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES ((select id from iusergrouphdr where name = 'EIEL'),873,(select idacl from acl where name = 'Capas EIEL SHP' limit 1));
		INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES ((select id from iusergrouphdr where name = 'EIEL'),874,(select idacl from acl where name = 'Capas EIEL SHP' limit 1));

		INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES ((select id from iusergrouphdr where name = 'SuperAdministrador'),871,(select idacl from acl where name = 'Capas EIEL SHP' limit 1));
		INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES ((select id from iusergrouphdr where name = 'SuperAdministrador'),872,(select idacl from acl where name = 'Capas EIEL SHP' limit 1));
		INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES ((select id from iusergrouphdr where name = 'SuperAdministrador'),873,(select idacl from acl where name = 'Capas EIEL SHP' limit 1));
		INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES ((select id from iusergrouphdr where name = 'SuperAdministrador'),874,(select idacl from acl where name = 'Capas EIEL SHP' limit 1));
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO2";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'ALUMBRADO_SHP') THEN	

	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO3";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'ALUMBRADO_SHP') THEN	
		-- CAPA ALUMBRADO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','alumbrado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]alumbrado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]alumbrado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]alumbrado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]alumbrado');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>ALUMBRADO_SHP</Name><UserStyle><Name>ALUMBRADO_SHP:_:default:ALUMBRADO_SHP</Name><Title>ALUMBRADO_SHP:_:default:ALUMBRADO_SHP</Title><Abstract>ALUMBRADO_SHP:_:default:ALUMBRADO_SHP</Abstract><FeatureTypeStyle><Name>ALUMBRADO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'ALUMBRADO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'ALUMBRADO_SHP:_:default:ALUMBRADO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'codprov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'codmunic'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'codentidad'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nucleo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'codpoblamiento'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ah_ener_rfl');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ah_ener_rfl');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ah_ener_rfl');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ah_ener_rfl');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ah_ener_rfl');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'ah_ener_rfl'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ah_ener_rfi');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ah_ener_rfi');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ah_ener_rfi');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ah_ener_rfi');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ah_ener_rfi');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'ah_ener_rfi'),10,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','calidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]calidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]calidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]calidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]calidad');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_alum_pl' and columns.name = 'estado'),11,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'ÇSELECT transform("eiel_c_alum_pl"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento, ah_ener_rfl, ah_ener_rfi, estado FROM "eiel_c_alum_pl", eiel_configuracion_shp WHERE "eiel_c_alum_pl"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO4";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'CAPTACION_ENC_SHP') THEN	

		-- CAPA CAPTACION_ENC
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','captacion_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]captacion_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]captacion_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]captacion_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]captacion_enc');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>CAPTACION_ENC_SHP</Name><UserStyle><Name>CAPTACION_ENC_SHP:_:default:CAPTACION_ENC_SHP</Name><Title>CAPTACION_ENC_SHP:_:default:CAPTACION_ENC_SHP</Title><Abstract>CAPTACION_ENC_SHP:_:default:CAPTACION_ENC_SHP</Abstract><FeatureTypeStyle><Name>CAPTACION_ENC_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'CAPTACION_ENC_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'CAPTACION_ENC_SHP:_:default:CAPTACION_ENC_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_capt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_capt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_capt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_capt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_capt');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'orden_ca'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_abast_ca"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_ca FROM "eiel_c_abast_ca", eiel_configuracion_shp WHERE "eiel_c_abast_ca"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 <= 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO5";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'CAPTACION_ENC_M50_SHP') THEN	
		-- CAPA CAPTACION_ENC_M50
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','captacion_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]captacion_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]captacion_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]captacion_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]captacion_enc_m50');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>CAPTACION_ENC_M50_SHP</Name><UserStyle><Name>CAPTACION_ENC_M50_SHP:_:default:CAPTACION_ENC_M50_SHP</Name><Title>CAPTACION_ENC_M50_SHP:_:default:CAPTACION_ENC_M50_SHP</Title><Abstract>CAPTACION_ENC_M50_SHP:_:default:CAPTACION_ENC_M50_SHP</Abstract><FeatureTypeStyle><Name>CAPTACION_ENC_M50_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'CAPTACION_ENC_M50_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'CAPTACION_ENC_M50_SHP:_:default:CAPTACION_ENC_M50_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_capt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_capt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_capt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_capt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_capt');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_ca' and columns.name = 'orden_ca'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_abast_ca"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_ca FROM "eiel_c_abast_ca", eiel_configuracion_shp WHERE "eiel_c_abast_ca"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 > 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');

	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO6";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'CASA_CONSISTORIAL_SHP') THEN	

		-- CAPA CASA_CONSISTORIAL
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','casa_consistorial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]casa_consistorial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]casa_consistorial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]casa_consistorial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]casa_consistorial');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>CASA_CONSISTORIAL_SHP</Name><UserStyle><Name>CASA_CONSISTORIAL_SHP:_:default:CASA_CONSISTORIAL_SHP</Name><Title>CASA_CONSISTORIAL_SHP:_:default:CASA_CONSISTORIAL_SHP</Title><Abstract>CASA_CONSISTORIAL_SHP:_:default:CASA_CONSISTORIAL_SHP</Abstract><FeatureTypeStyle><Name>CASA_CONSISTORIAL_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'CASA_CONSISTORIAL_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'CASA_CONSISTORIAL_SHP:_:default:CASA_CONSISTORIAL_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cc' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cc' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cc' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cc' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cc' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cc' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cc' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cc' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_casa');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_casa');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_casa');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_casa');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_casa');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cc' and columns.name = 'orden_cc'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_cc"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_cc FROM "eiel_c_cc", eiel_configuracion_shp WHERE "eiel_c_cc"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');

	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO7";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'CEMENTERIO_SHP') THEN	

		-- CAPA CEMENTERIO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','cementerio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]cementerio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]cementerio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]cementerio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]cementerio');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>CEMENTERIO_SHP</Name><UserStyle><Name>CEMENTERIO_SHP:_:default:CEMENTERIO_SHP</Name><Title>CEMENTERIO_SHP:_:default:CEMENTERIO_SHP</Title><Abstract>CEMENTERIO_SHP:_:default:CEMENTERIO_SHP</Abstract><FeatureTypeStyle><Name>CEMENTERIO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'CEMENTERIO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'CEMENTERIO_SHP:_:default:CEMENTERIO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ce' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ce' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ce' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ce' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ce' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ce' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ce' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ce' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_cement');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_cement');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_cement');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_cement');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_cement');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ce' and columns.name = 'orden_ce'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_ce"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_ce FROM "eiel_c_ce", eiel_configuracion_shp WHERE "eiel_c_ce"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');

	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO8";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'CENT_CULTURAL_SHP') THEN	

	-- CAPA CENT_CULTURAL
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','cent_cultural');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]cent_cultural');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]cent_cultural');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]cent_cultural');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]cent_cultural');

	--Insercion de estilos - por defecto
	INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
	<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<NamedLayer><Name>CENT_CULTURAL_SHP</Name><UserStyle><Name>CENT_CULTURAL_SHP:_:default:CENT_CULTURAL_SHP</Name><Title>CENT_CULTURAL_SHP:_:default:CENT_CULTURAL_SHP</Title><Abstract>CENT_CULTURAL_SHP:_:default:CENT_CULTURAL_SHP</Abstract><FeatureTypeStyle><Name>CENT_CULTURAL_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
	</StyledLayerDescriptor>');
	INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'CENT_CULTURAL_SHP',1,0,0);

	-- insercion de layer-layerfamily
	insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
	-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
	insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'CENT_CULTURAL_SHP:_:default:CENT_CULTURAL_SHP', true,0,0,true,true);

	-- insercion de atributos
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cu' and columns.name = 'id'),1,true);

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cu' and columns.name = 'id_municipio'),2,true);

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cu' and columns.name = 'GEOMETRY'),3,true);

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cu' and columns.name = 'clave'),5,true);

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cu' and columns.name = 'codprov'),6,true);

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cu' and columns.name = 'codmunic'),7,true);

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cu' and columns.name = 'codentidad'),8,true);

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cu' and columns.name = 'codpoblamiento'),9,true);

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_centro');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_centro');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_centro');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_centro');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_centro');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_cu' and columns.name = 'orden_cu'),10,true);

	-- QUERY
	INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_cu"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_cu FROM "eiel_c_cu", eiel_configuracion_shp WHERE "eiel_c_cu"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO9";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'CENTRO_ASISTENCIAL_SHP') THEN	

		-- CAPA CENTRO_ASISTENCIAL
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','centro_asistencial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]centro_asistencial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]centro_asistencial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]centro_asistencial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]centro_asistencial');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>CENTRO_ASISTENCIAL_SHP</Name><UserStyle><Name>CENTRO_ASISTENCIAL_SHP:_:default:CENTRO_ASISTENCIAL_SHP</Name><Title>CENTRO_ASISTENCIAL_SHP:_:default:CENTRO_ASISTENCIAL_SHP</Title><Abstract>CENTRO_ASISTENCIAL_SHP:_:default:CENTRO_ASISTENCIAL_SHP</Abstract><FeatureTypeStyle><Name>CENTRO_ASISTENCIAL_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'CENTRO_ASISTENCIAL_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'CENTRO_ASISTENCIAL_SHP:_:default:CENTRO_ASISTENCIAL_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_as' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_as' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_as' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_as' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_as' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_as' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_as' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_as' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_casis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_casis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_casis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_casis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_casis');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_as' and columns.name = 'orden_as'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_as"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_as FROM "eiel_c_as", eiel_configuracion_shp WHERE "eiel_c_as"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO10";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'CENTRO_ENSENANZA_SHP') THEN	

		-- CAPA CENTRO_ENSENANZA
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','centro_ensenanza');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]centro_ensenanza');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]centro_ensenanza');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]centro_ensenanza');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]centro_ensenanza');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>CENTRO_ENSENANZA_SHP</Name><UserStyle><Name>CENTRO_ENSENANZA_SHP:_:default:CENTRO_ENSENANZA_SHP</Name><Title>CENTRO_ENSENANZA_SHP:_:default:CENTRO_ENSENANZA_SHP</Title><Abstract>CENTRO_ENSENANZA_SHP:_:default:CENTRO_ENSENANZA_SHP</Abstract><FeatureTypeStyle><Name>CENTRO_ENSENANZA_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'CENTRO_ENSENANZA_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'CENTRO_ENSENANZA_SHP:_:default:CENTRO_ENSENANZA_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_en' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_en' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_en' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_en' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_en' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_en' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_en' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_en' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_cent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_cent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_cent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_cent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_cent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_en' and columns.name = 'orden_en'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_en"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_en FROM "eiel_c_en", eiel_configuracion_shp WHERE "eiel_c_en"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO11";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'CENTRO_SANITARIO_SHP') THEN	

		-- CAPA CENTRO_SANITARIO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','centro_sanitario');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]centro_sanitario');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]centro_sanitario');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]centro_sanitario');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]centro_sanitario');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>CENTRO_SANITARIO_SHP</Name><UserStyle><Name>CENTRO_SANITARIO_SHP:_:default:CENTRO_SANITARIO_SHP</Name><Title>CENTRO_SANITARIO_SHP:_:default:CENTRO_SANITARIO_SHP</Title><Abstract>CENTRO_SANITARIO_SHP:_:default:CENTRO_SANITARIO_SHP</Abstract><FeatureTypeStyle><Name>CENTRO_SANITARIO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'CENTRO_SANITARIO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'CENTRO_SANITARIO_SHP:_:default:CENTRO_SANITARIO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_sa' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_sa' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_sa' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_sa' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_sa' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_sa' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_sa' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_sa' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_csan');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_csan');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_csan');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_csan');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_csan');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_sa' and columns.name = 'orden_sa'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_sa"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_sa FROM "eiel_c_sa", eiel_configuracion_shp WHERE "eiel_c_sa"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO12";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'DEPOSITO_ENC_SHP') THEN	

		-- CAPA DEPOSITO_ENC
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','deposito_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]deposito_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]deposito_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]deposito_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]deposito_enc');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>DEPOSITO_ENC_SHP</Name><UserStyle><Name>DEPOSITO_ENC_SHP:_:default:DEPOSITO_ENC_SHP</Name><Title>DEPOSITO_ENC_SHP:_:default:DEPOSITO_ENC_SHP</Title><Abstract>DEPOSITO_ENC_SHP:_:default:DEPOSITO_ENC_SHP</Abstract><FeatureTypeStyle><Name>DEPOSITO_ENC_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'DEPOSITO_ENC_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'DEPOSITO_ENC_SHP:_:default:DEPOSITO_ENC_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_depo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_depo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_depo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_depo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_depo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'orden_de'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_abast_de"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_de FROM "eiel_c_abast_de", eiel_configuracion_shp WHERE "eiel_c_abast_de"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 <= 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO13";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'DEPOSITO_ENC_M50_SHP') THEN	

		-- CAPA DEPOSITO_ENC_M50
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','deposito_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]deposito_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]deposito_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]deposito_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]deposito_enc_m50');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>DEPOSITO_ENC_M50_SHP</Name><UserStyle><Name>DEPOSITO_ENC_M50_SHP:_:default:DEPOSITO_ENC_M50_SHP</Name><Title>DEPOSITO_ENC_M50_SHP:_:default:DEPOSITO_ENC_M50_SHP</Title><Abstract>DEPOSITO_ENC_M50_SHP:_:default:DEPOSITO_ENC_M50_SHP</Abstract><FeatureTypeStyle><Name>DEPOSITO_ENC_M50_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'DEPOSITO_ENC_M50_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'DEPOSITO_ENC_M50_SHP:_:default:DEPOSITO_ENC_M50_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_depo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_depo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_depo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_depo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_depo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_de' and columns.name = 'orden_de'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, '_SELECT transform("eiel_c_abast_de"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_de FROM "eiel_c_abast_de", eiel_configuracion_shp WHERE "eiel_c_abast_de"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 > 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO14";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'DEPURADORA_ENC_SHP') THEN	

		-- CAPA DEPURADORA_ENC
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','depuradora_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]depuradora_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]depuradora_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]depuradora_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]depuradora_enc');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>DEPURADORA_ENC_SHP</Name><UserStyle><Name>DEPURADORA_ENC_SHP:_:default:DEPURADORA_ENC_SHP</Name><Title>DEPURADORA_ENC_SHP:_:default:DEPURADORA_ENC_SHP</Title><Abstract>DEPURADORA_ENC_SHP:_:default:DEPURADORA_ENC_SHP</Abstract><FeatureTypeStyle><Name>DEPURADORA_ENC_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'DEPURADORA_ENC_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'DEPURADORA_ENC_SHP:_:default:DEPURADORA_ENC_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_depu');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_depu');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_depu');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_depu');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_depu');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'orden_ed'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_saneam_ed"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_ed FROM "eiel_c_saneam_ed", eiel_configuracion_shp WHERE "eiel_c_saneam_ed"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 <= 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO15";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'DEPURADORA_ENC_M50_SHP') THEN	

		-- CAPA DEPURADORA_ENC_M50
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','depuradora_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]depuradora_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]depuradora_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]depuradora_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]depuradora_enc_m50');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>DEPURADORA_ENC_M50_SHP</Name><UserStyle><Name>DEPURADORA_ENC_M50_SHP:_:default:DEPURADORA_ENC_M50_SHP</Name><Title>DEPURADORA_ENC_M50_SHP:_:default:DEPURADORA_ENC_M50_SHP</Title><Abstract>DEPURADORA_ENC_M50_SHP:_:default:DEPURADORA_ENC_M50_SHP</Abstract><FeatureTypeStyle><Name>DEPURADORA_ENC_M50_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'DEPURADORA_ENC_M50_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'DEPURADORA_ENC_M50_SHP:_:default:DEPURADORA_ENC_M50_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_depu');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_depu');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_depu');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_depu');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_depu');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ed' and columns.name = 'orden_ed'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_saneam_ed"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_ed FROM "eiel_c_saneam_ed", eiel_configuracion_shp WHERE "eiel_c_saneam_ed"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 > 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO16";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'EDIF_PUB_SIN_USO_SHP') THEN	

		-- CAPA EDIF_PUB_SIN_USO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','edif_pub_sin_uso');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]edif_pub_sin_uso');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]edif_pub_sin_uso');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]edif_pub_sin_uso');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]edif_pub_sin_uso');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>EDIF_PUB_SIN_USO_SHP</Name><UserStyle><Name>EDIF_PUB_SIN_USO_SHP:_:default:EDIF_PUB_SIN_USO_SHP</Name><Title>EDIF_PUB_SIN_USO_SHP:_:default:EDIF_PUB_SIN_USO_SHP</Title><Abstract>EDIF_PUB_SIN_USO_SHP:_:default:EDIF_PUB_SIN_USO_SHP</Abstract><FeatureTypeStyle><Name>EDIF_PUB_SIN_USO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'EDIF_PUB_SIN_USO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'EDIF_PUB_SIN_USO_SHP:_:default:EDIF_PUB_SIN_USO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_su' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_su' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_su' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_su' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_su' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_su' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_su' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_su' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_edific');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_edific');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_edific');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_edific');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_edific');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_su' and columns.name = 'orden_su'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_su"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_su FROM "eiel_c_su", eiel_configuracion_shp WHERE "eiel_c_su"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO17";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'EMISARIO_ENC_SHP') THEN	

		-- CAPA EMISARIO_ENC
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','emisario_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]emisario_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]emisario_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]emisario_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]emisario_enc');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>EMISARIO_ENC_SHP</Name><UserStyle><Name>EMISARIO_ENC_SHP:_:default:EMISARIO_ENC_SHP</Name><Title>EMISARIO_ENC_SHP:_:default:EMISARIO_ENC_SHP</Title><Abstract>EMISARIO_ENC_SHP:_:default:EMISARIO_ENC_SHP</Abstract><FeatureTypeStyle><Name>EMISARIO_ENC_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'EMISARIO_ENC_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'EMISARIO_ENC_SHP:_:default:EMISARIO_ENC_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_emis');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'tramo_em'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_saneam_tem"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, tramo_em FROM "eiel_c_saneam_tem", eiel_configuracion_shp WHERE "eiel_c_saneam_tem"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 <= 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO18";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'EMISARIO_ENC_M50_SHP') THEN	

		-- CAPA EMISARIO_ENC_M50
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','emisario_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]emisario_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]emisario_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]emisario_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]emisario_enc_m50');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>EMISARIO_ENC_M50_SHP</Name><UserStyle><Name>EMISARIO_ENC_M50_SHP:_:default:EMISARIO_ENC_M50_SHP</Name><Title>EMISARIO_ENC_M50_SHP:_:default:EMISARIO_ENC_M50_SHP</Title><Abstract>EMISARIO_ENC_M50_SHP:_:default:EMISARIO_ENC_M50_SHP</Abstract><FeatureTypeStyle><Name>EMISARIO_ENC_M50_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'EMISARIO_ENC_M50_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'EMISARIO_ENC_M50_SHP:_:default:EMISARIO_ENC_M50_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_emis');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'tramo_em'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_saneam_tem"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, tramo_em FROM "eiel_c_saneam_tem", eiel_configuracion_shp WHERE "eiel_c_saneam_tem"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 > 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO19";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'INFRAESTR_VIARIA_SHP') THEN	

		-- CAPA INFRAESTR_VIARIA
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','infraestr_viaria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]infraestr_viaria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]infraestr_viaria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]infraestr_viaria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]infraestr_viaria');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>INFRAESTR_VIARIA_SHP</Name><UserStyle><Name>INFRAESTR_VIARIA_SHP:_:default:INFRAESTR_VIARIA_SHP</Name><Title>INFRAESTR_VIARIA_SHP:_:default:INFRAESTR_VIARIA_SHP</Title><Abstract>INFRAESTR_VIARIA_SHP:_:default:INFRAESTR_VIARIA_SHP</Abstract><FeatureTypeStyle><Name>INFRAESTR_VIARIA_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'INFRAESTR_VIARIA_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'INFRAESTR_VIARIA_SHP:_:default:INFRAESTR_VIARIA_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_redviaria_tu' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_redviaria_tu' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_redviaria_tu' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_redviaria_tu' and columns.name = 'codprov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_redviaria_tu' and columns.name = 'codmunic'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_redviaria_tu' and columns.name = 'codentidad'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nucleo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_redviaria_tu' and columns.name = 'codpoblamiento'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_infr');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_infr');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_infr');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_infr');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_infr');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_redviaria_tu' and columns.name = 'tipo'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_redviaria_tu' and columns.name = 'estado'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_redviaria_tu"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento, tipo, estado FROM "eiel_c_redviaria_tu", eiel_configuracion_shp WHERE "eiel_c_redviaria_tu"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO20";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'INSTAL_DEPORTIVA_SHP') THEN	


		-- CAPA INSTAL_DEPORTIVA
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','instal_deportiva');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]instal_deportiva');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]instal_deportiva');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]instal_deportiva');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]instal_deportiva');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>INSTAL_DEPORTIVA_SHP</Name><UserStyle><Name>INSTAL_DEPORTIVA_SHP:_:default:INSTAL_DEPORTIVA_SHP</Name><Title>INSTAL_DEPORTIVA_SHP:_:default:INSTAL_DEPORTIVA_SHP</Title><Abstract>INSTAL_DEPORTIVA_SHP:_:default:INSTAL_DEPORTIVA_SHP</Abstract><FeatureTypeStyle><Name>INSTAL_DEPORTIVA_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'INSTAL_DEPORTIVA_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'INSTAL_DEPORTIVA_SHP:_:default:INSTAL_DEPORTIVA_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_id' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_id' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_id' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_id' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_id' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_id' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_id' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_id' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_instal');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_instal');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_instal');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_instal');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_instal');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_id' and columns.name = 'orden_id'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_id"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_id FROM "eiel_c_id", eiel_configuracion_shp WHERE "eiel_c_id"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO21";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'LONJA_MERC_FERIA_SHP') THEN	

		-- CAPA LONJA_MERC_FERIA
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','lonja_merc_feria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]lonja_merc_feria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]lonja_merc_feria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]lonja_merc_feria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]lonja_merc_feria');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>LONJA_MERC_FERIA_SHP</Name><UserStyle><Name>LONJA_MERC_FERIA_SHP:_:default:LONJA_MERC_FERIA_SHP</Name><Title>LONJA_MERC_FERIA_SHP:_:default:LONJA_MERC_FERIA_SHP</Title><Abstract>LONJA_MERC_FERIA_SHP:_:default:LONJA_MERC_FERIA_SHP</Abstract><FeatureTypeStyle><Name>LONJA_MERC_FERIA_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'LONJA_MERC_FERIA_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'LONJA_MERC_FERIA_SHP:_:default:LONJA_MERC_FERIA_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_lm' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_lm' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_lm' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_lm' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_lm' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_lm' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_lm' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_lm' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_lmf');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_lmf');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_lmf');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_lmf');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_lmf');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_lm' and columns.name = 'orden_lm'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_lm"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_lm FROM "eiel_c_lm", eiel_configuracion_shp WHERE "eiel_c_lm"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO22";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'MATADERO_SHP') THEN	

		-- CAPA MATADERO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','matadero');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]matadero');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]matadero');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]matadero');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]matadero');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>MATADERO_SHP</Name><UserStyle><Name>MATADERO_SHP:_:default:MATADERO_SHP</Name><Title>MATADERO_SHP:_:default:MATADERO_SHP</Title><Abstract>MATADERO_SHP:_:default:MATADERO_SHP</Abstract><FeatureTypeStyle><Name>MATADERO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'MATADERO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'MATADERO_SHP:_:default:MATADERO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_mt' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_mt' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_mt' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_mt' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_mt' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_mt' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_mt' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_mt' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_matad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_matad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_matad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_matad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_matad');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_mt' and columns.name = 'orden_mt'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_mt"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_mt FROM "eiel_c_mt", eiel_configuracion_shp WHERE "eiel_c_mt"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO23";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'NUC_ABANDONADO_SHP') THEN	

		-- CAPA NUC_ABANDONADO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nuc_abandonado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nuc_abandonado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nuc_abandonado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nuc_abandonado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nuc_abandonado');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>NUC_ABANDONADO_SHP</Name><UserStyle><Name>NUC_ABANDONADO_SHP:_:default:NUC_ABANDONADO_SHP</Name><Title>NUC_ABANDONADO_SHP:_:default:NUC_ABANDONADO_SHP</Title><Abstract>NUC_ABANDONADO_SHP:_:default:NUC_ABANDONADO_SHP</Abstract><FeatureTypeStyle><Name>NUC_ABANDONADO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'NUC_ABANDONADO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'NUC_ABANDONADO_SHP:_:default:NUC_ABANDONADO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_aband' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_aband' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_aband' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_aband' and columns.name = 'prov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_aband' and columns.name = 'mun'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_aband' and columns.name = 'ent'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_aband' and columns.name = 'poblamiento'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]revision_expirada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_aband' and columns.name = 'revision_expirada'),9,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_nucleo_aband"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, prov, mun, ent, poblamiento FROM "eiel_c_nucleo_aband", eiel_configuracion_shp WHERE "eiel_c_nucleo_aband"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO24";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'NUCLEO_SHP') THEN	

		-- CAPA NUCLEO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nucleo');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>NUCLEO_SHP</Name><UserStyle><Name>NUCLEO_SHP:_:default:NUCLEO_SHP</Name><Title>NUCLEO_SHP:_:default:NUCLEO_SHP</Title><Abstract>NUCLEO_SHP:_:default:NUCLEO_SHP</Abstract><FeatureTypeStyle><Name>NUCLEO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'NUCLEO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'NUCLEO_SHP:_:default:NUCLEO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_poblacion' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_poblacion' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_poblacion' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_poblacion' and columns.name = 'codprov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_poblacion' and columns.name = 'codmunic'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_poblacion' and columns.name = 'codentidad'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nucleo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_poblacion' and columns.name = 'nombre_oficial'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]revision_expirada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_nucleo_poblacion' and columns.name = 'revision_expirada'),9,true);


		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_nucleo_poblacion"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, nombre_oficial FROM "eiel_c_nucleo_poblacion", eiel_configuracion_shp WHERE "eiel_c_nucleo_poblacion"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO25";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'ALIVIADERO_SHP') THEN	

		-- CAPA ALIVIADERO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','aliviadero');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]aliviadero');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]aliviadero');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]aliviadero');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]aliviadero');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>ALIVIADERO_SHP</Name><UserStyle><Name>ALIVIADERO_SHP:_:default:ALIVIADERO_SHP</Name><Title>ALIVIADERO_SHP:_:default:ALIVIADERO_SHP</Title><Abstract>ALIVIADERO_SHP:_:default:ALIVIADERO_SHP</Abstract><FeatureTypeStyle><Name>ALIVIADERO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'ALIVIADERO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'ALIVIADERO_SHP:_:default:ALIVIADERO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'codprov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'codmunic'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'codentidad'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nucleo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'nombre_oficial'),8,true);


		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_saneam_ali"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento FROM "eiel_c_saneam_ali", eiel_configuracion_shp WHERE "eiel_c_saneam_ali"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO26";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'PARQUE_SHP') THEN	

		-- CAPA PARQUE
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','parque');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]parque');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]parque');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]parque');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]parque');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>PARQUE_SHP</Name><UserStyle><Name>PARQUE_SHP:_:default:PARQUE_SHP</Name><Title>PARQUE_SHP:_:default:PARQUE_SHP</Title><Abstract>PARQUE_SHP:_:default:PARQUE_SHP</Abstract><FeatureTypeStyle><Name>PARQUE_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'PARQUE_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'PARQUE_SHP:_:default:PARQUE_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_parq');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_parq');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_parq');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_parq');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_parq');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'orden_pj'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_pj"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_pj.id, eiel_c_pj.id_municipio, fase, eiel_c_pj.clave, eiel_c_pj.codprov, eiel_c_pj.codmunic, eiel_c_pj.codentidad, eiel_c_pj.codpoblamiento, eiel_c_pj.orden_pj FROM eiel_configuracion_shp, "eiel_c_pj" left join eiel_t_pj ON eiel_t_pj.clave = eiel_c_pj.clave AND eiel_t_pj.codprov = eiel_c_pj.codprov AND eiel_t_pj.codmunic = eiel_c_pj.codmunic AND eiel_t_pj.codentidad = eiel_c_pj.codentidad AND eiel_t_pj.codpoblamiento = eiel_c_pj.codpoblamiento AND eiel_t_pj.orden_pj = eiel_c_pj.orden_pj and eiel_t_pj.revision_expirada = 9999999999 and eiel_c_pj.revision_expirada = 9999999999 where tipo = ''JA'' or tipo = ''PI'' or tipo = ''PN'' or tipo = ''PU'' and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and "eiel_c_pj"."id_municipio" in (?M)', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO27";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'ZONAS_NATURALES_SHP') THEN	

		-- CAPA ZONAS_NATURALES
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','zonas_naturales');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]zonas_naturales');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]zonas_naturales');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]zonas_naturales');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]zonas_naturales');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>ZONAS_NATURALES_SHP</Name><UserStyle><Name>ZONAS_NATURALES_SHP:_:default:ZONAS_NATURALES_SHP</Name><Title>ZONAS_NATURALES_SHP:_:default:ZONAS_NATURALES_SHP</Title><Abstract>ZONAS_NATURALES_SHP:_:default:ZONAS_NATURALES_SHP</Abstract><FeatureTypeStyle><Name>ZONAS_NATURALES_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'ZONAS_NATURALES_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'ZONAS_NATURALES_SHP:_:default:ZONAS_NATURALES_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_parq');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_parq');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_parq');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_parq');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_parq');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_pj' and columns.name = 'orden_pj'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_pj"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_pj.id, eiel_c_pj.id_municipio, fase, eiel_c_pj.clave, eiel_c_pj.codprov, eiel_c_pj.codmunic, eiel_c_pj.codentidad, eiel_c_pj.codpoblamiento, eiel_c_pj.orden_pj FROM eiel_configuracion_shp, "eiel_c_pj" left join eiel_t_pj ON eiel_t_pj.clave = eiel_c_pj.clave AND eiel_t_pj.codprov = eiel_c_pj.codprov AND eiel_t_pj.codmunic = eiel_c_pj.codmunic AND eiel_t_pj.codentidad = eiel_c_pj.codentidad AND eiel_t_pj.codpoblamiento = eiel_c_pj.codpoblamiento AND eiel_t_pj.orden_pj = eiel_c_pj.orden_pj and eiel_t_pj.revision_expirada = 9999999999 and eiel_c_pj.revision_expirada = 9999999999 where tipo = ''AN'' or tipo = ''ZR'' and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and "eiel_c_pj"."id_municipio" in (?M)', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO28";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'SUELO_RURAL_PRESERVADO_SHP') THEN	

		-- CAPA SUELO_RURAL_PRESERVADO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','suelo_rural_preservado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]suelo_rural_preservado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]suelo_rural_preservado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]suelo_rural_preservado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]suelo_rural_preservado');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>SUELO_RURAL_PRESERVADO_SHP</Name><UserStyle><Name>SUELO_RURAL_PRESERVADO_SHP:_:default:SUELO_RURAL_PRESERVADO_SHP</Name><Title>SUELO_RURAL_PRESERVADO_SHP:_:default:SUELO_RURAL_PRESERVADO_SHP</Title><Abstract>SUELO_RURAL_PRESERVADO_SHP:_:default:SUELO_RURAL_PRESERVADO_SHP</Abstract><FeatureTypeStyle><Name>SUELO_RURAL_PRESERVADO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'SUELO_RURAL_PRESERVADO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'SUELO_RURAL_PRESERVADO_SHP:_:default:SUELO_RURAL_PRESERVADO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural_preservado' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural_preservado' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural_preservado' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural_preservado' and columns.name = 'prov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural_preservado' and columns.name = 'mun'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_urba');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural_preservado' and columns.name = 'tipo_urba'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado_tra');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural_preservado' and columns.name = 'estado_tra'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]denominaci');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural_preservado' and columns.name = 'denominaci'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]revision_expirada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural_preservado' and columns.name = 'revision_expirada'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_suelo_rural_preservado"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, prov, mun, tipo_urba, estado_tra, denominaci FROM "eiel_c_suelo_rural_preservado", eiel_configuracion_shp WHERE "eiel_c_suelo_rural_preservado"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO29";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'SUELO_RURAL_SHP') THEN	

		-- CAPA SUELO_RURAL
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','suelo_rural');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]suelo_rural');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]suelo_rural');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]suelo_rural');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]suelo_rural');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>SUELO_RURAL_SHP</Name><UserStyle><Name>SUELO_RURAL_SHP:_:default:SUELO_RURAL_SHP</Name><Title>SUELO_RURAL_SHP:_:default:SUELO_RURAL_SHP</Title><Abstract>SUELO_RURAL_SHP:_:default:SUELO_RURAL_SHP</Abstract><FeatureTypeStyle><Name>SUELO_RURAL_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'SUELO_RURAL_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'SUELO_RURAL_SHP:_:default:SUELO_RURAL_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural' and columns.name = 'prov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural' and columns.name = 'mun'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_urba');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural' and columns.name = 'tipo_urba'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado_tra');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural' and columns.name = 'estado_tra'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]denominaci');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural' and columns.name = 'denominaci'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]revision_expirada');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]revision_expirada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_rural' and columns.name = 'revision_expirada'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_suelo_rural"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, prov, mun, tipo_urba, estado_tra, denominaci FROM "eiel_c_suelo_rural", eiel_configuracion_shp WHERE "eiel_c_suelo_rural"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO30";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'SUELO_URBANO_SHP') THEN	

		-- CAPA SUELO_URBANO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','suelo_urbano');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]suelo_urbano');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]suelo_urbano');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]suelo_urbano');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]suelo_urbano');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>SUELO_URBANO_SHP</Name><UserStyle><Name>SUELO_URBANO_SHP:_:default:SUELO_URBANO_SHP</Name><Title>SUELO_URBANO_SHP:_:default:SUELO_URBANO_SHP</Title><Abstract>SUELO_URBANO_SHP:_:default:SUELO_URBANO_SHP</Abstract><FeatureTypeStyle><Name>SUELO_URBANO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'SUELO_URBANO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'SUELO_URBANO_SHP:_:default:SUELO_URBANO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_urbano' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_urbano' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_urbano' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_urbano' and columns.name = 'prov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_urbano' and columns.name = 'mun'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_urba');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_urba');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_urbano' and columns.name = 'tipo_urba'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado_tra');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado_tra');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_urbano' and columns.name = 'estado_tra'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]denominaci');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]denominaci');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_suelo_urbano' and columns.name = 'denominaci'),9,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_suelo_urbano"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, prov, mun, tipo_urba, estado_tra, denominaci FROM "eiel_c_suelo_urbano", eiel_configuracion_shp WHERE "eiel_c_suelo_urbano"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO31";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'PROTECCION_CIVIL_SHP') THEN	

		-- CAPA PROTECCION_CIVIL
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','proteccion_civil');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]proteccion_civil');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]proteccion_civil');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]proteccion_civil');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]proteccion_civil');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>PROTECCION_CIVIL_SHP</Name><UserStyle><Name>PROTECCION_CIVIL_SHP:_:default:PROTECCION_CIVIL_SHP</Name><Title>PROTECCION_CIVIL_SHP:_:default:PROTECCION_CIVIL_SHP</Title><Abstract>PROTECCION_CIVIL_SHP:_:default:PROTECCION_CIVIL_SHP</Abstract><FeatureTypeStyle><Name>PROTECCION_CIVIL_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'PROTECCION_CIVIL_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'PROTECCION_CIVIL_SHP:_:default:PROTECCION_CIVIL_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ip' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ip' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ip' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ip' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ip' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ip' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ip' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ip' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_prot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_prot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_prot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_prot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_prot');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ip' and columns.name = 'orden_ip'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_ip"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_ip FROM "eiel_c_ip", eiel_configuracion_shp WHERE "eiel_c_ip"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO32";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'RAMAL_SANEAMIENTO_SHP') THEN	

		-- CAPA RAMAL_SANEAMIENTO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ramal_saneamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ramal_saneamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ramal_saneamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ramal_saneamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ramal_saneamiento');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>RAMAL_SANEAMIENTO_SHP</Name><UserStyle><Name>RAMAL_SANEAMIENTO_SHP:_:default:RAMAL_SANEAMIENTO_SHP</Name><Title>RAMAL_SANEAMIENTO_SHP:_:default:RAMAL_SANEAMIENTO_SHP</Title><Abstract>RAMAL_SANEAMIENTO_SHP:_:default:RAMAL_SANEAMIENTO_SHP</Abstract><FeatureTypeStyle><Name>RAMAL_SANEAMIENTO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'RAMAL_SANEAMIENTO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'RAMAL_SANEAMIENTO_SHP:_:default:RAMAL_SANEAMIENTO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'codprov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'codmunic'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'codentidad'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nucleo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'codpoblamiento'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_rama');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_rama');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_rama');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_rama');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_rama');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'tramo_rs'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]sist_trans');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'sist_impulsion'),10,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'estado'),11,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_red');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_red');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_red');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_red');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_red');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'tipo_red_interior'),12,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]titular');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'titular'),13,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]gestion');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_rs' and columns.name = 'gestor'),14,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_saneam_rs"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento, tramo_rs, sist_impulsion, estado, tipo_red_interior, titular, gestor FROM "eiel_c_saneam_rs", eiel_configuracion_shp WHERE "eiel_c_saneam_rs"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO33";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'RED_DISTRIBUCION_SHP') THEN	

		-- CAPA RED_DISTRIBUCION
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','red_distribucion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]red_distribucion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]red_distribucion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]red_distribucion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]red_distribucion');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>RED_DISTRIBUCION_SHP</Name><UserStyle><Name>RED_DISTRIBUCION_SHP:_:default:RED_DISTRIBUCION_SHP</Name><Title>RED_DISTRIBUCION_SHP:_:default:RED_DISTRIBUCION_SHP</Title><Abstract>RED_DISTRIBUCION_SHP:_:default:RED_DISTRIBUCION_SHP</Abstract><FeatureTypeStyle><Name>RED_DISTRIBUCION_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'RED_DISTRIBUCION_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'RED_DISTRIBUCION_SHP:_:default:RED_DISTRIBUCION_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'codprov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'codmunic'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'codentidad'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nucleo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nucleo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'codpoblamiento'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_rdis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_rdis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_rdis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_rdis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_rdis');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'tramo_rd'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]sist_trans');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'sist_trans'),10,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'estado'),11,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]titular');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'titular'),12,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]gestion');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_rd' and columns.name = 'gestor'),13,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_abast_rd"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento, tramo_rd, sist_trans, estado, titular, gestor FROM "eiel_c_abast_rd", eiel_configuracion_shp WHERE "eiel_c_abast_rd"."id_municipio" in (?M) and revision_expirada = 9999999999  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO34";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'TANATORIO_SHP') THEN	

		-- CAPA TANATORIO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tanatorio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tanatorio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tanatorio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tanatorio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tanatorio');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>TANATORIO_SHP</Name><UserStyle><Name>TANATORIO_SHP:_:default:TANATORIO_SHP</Name><Title>TANATORIO_SHP:_:default:TANATORIO_SHP</Title><Abstract>TANATORIO_SHP:_:default:TANATORIO_SHP</Abstract><FeatureTypeStyle><Name>TANATORIO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'TANATORIO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'TANATORIO_SHP:_:default:TANATORIO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ta' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ta' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ta' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ta' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ta' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ta' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ent');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ent');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ta' and columns.name = 'codentidad'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]poblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]poblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ta' and columns.name = 'codpoblamiento'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_tanat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_tanat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_tanat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_tanat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_tanat');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_ta' and columns.name = 'orden_ta'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_ta"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_ta FROM "eiel_c_ta", eiel_configuracion_shp WHERE "eiel_c_ta"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO35";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'POTABILIZACION_ENC_SHP') THEN	

		-- CAPA POTABILIZACION_ENC
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','potabilizacion_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]potabilizacion_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]potabilizacion_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]potabilizacion_enc');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]potabilizacion_enc');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>POTABILIZACION_ENC_SHP</Name><UserStyle><Name>POTABILIZACION_ENC_SHP:_:default:POTABILIZACION_ENC_SHP</Name><Title>POTABILIZACION_ENC_SHP:_:default:POTABILIZACION_ENC_SHP</Title><Abstract>POTABILIZACION_ENC_SHP:_:default:POTABILIZACION_ENC_SHP</Abstract><FeatureTypeStyle><Name>POTABILIZACION_ENC_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'POTABILIZACION_ENC_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'POTABILIZACION_ENC_SHP:_:default:POTABILIZACION_ENC_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_trat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_trat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_trat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_trat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_trat');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'orden_tp'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_abast_tp"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_tp FROM "eiel_c_abast_tp", eiel_configuracion_shp WHERE "eiel_c_abast_tp"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 <= 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO36";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'POTABILIZACION_ENC_M50_SHP') THEN	

		-- CAPA POTABILIZACION_ENC_M50
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','potabilizacion_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]potabilizacion_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]potabilizacion_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]potabilizacion_enc_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]potabilizacion_enc_m50');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>POTABILIZACION_ENC_M50_SHP</Name><UserStyle><Name>POTABILIZACION_ENC_M50_SHP:_:default:POTABILIZACION_ENC_M50_SHP</Name><Title>POTABILIZACION_ENC_M50_SHP:_:default:POTABILIZACION_ENC_M50_SHP</Title><Abstract>POTABILIZACION_ENC_M50_SHP:_:default:POTABILIZACION_ENC_M50_SHP</Abstract><FeatureTypeStyle><Name>POTABILIZACION_ENC_M50_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'POTABILIZACION_ENC_M50_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'POTABILIZACION_ENC_M50_SHP:_:default:POTABILIZACION_ENC_M50_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_trat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_trat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_trat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_trat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_trat');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tp' and columns.name = 'orden_tp'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_abast_tp"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_tp FROM "eiel_c_abast_tp", eiel_configuracion_shp WHERE "eiel_c_abast_tp"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 > 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO37";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'TRAMO_CARRETERA_SHP') THEN	

		-- CAPA TRAMO_CARRETERA
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tramo_carretera');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tramo_carretera');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tramo_carretera');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tramo_carretera');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tramo_carretera');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>TRAMO_CARRETERA_SHP</Name><UserStyle><Name>TRAMO_CARRETERA_SHP:_:default:TRAMO_CARRETERA_SHP</Name><Title>TRAMO_CARRETERA_SHP:_:default:TRAMO_CARRETERA_SHP</Title><Abstract>TRAMO_CARRETERA_SHP:_:default:TRAMO_CARRETERA_SHP</Abstract><FeatureTypeStyle><Name>TRAMO_CARRETERA_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'TRAMO_CARRETERA_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'TRAMO_CARRETERA_SHP:_:default:TRAMO_CARRETERA_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_tramos_carreteras' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_tramos_carreteras' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_tramos_carreteras' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_tramos_carreteras' and columns.name = 'codprov'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','cod_carrt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]cod_carrt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]cod_carrt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]cod_carrt');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]cod_carrt');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_tramos_carreteras' and columns.name = 'cod_carrt'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_tramos_carreteras' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','pk_inicial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]pk_inicial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]pk_inicial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]pk_inicial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]pk_inicial');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_tramos_carreteras' and columns.name = 'pki'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_tramos_carreteras"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, cod_carrt, codmunic, pki FROM "eiel_c_tramos_carreteras", eiel_configuracion_shp WHERE "eiel_c_tramos_carreteras"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO38";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'TRAMO_COLECTOR_M50_SHP') THEN	

		-- CAPA TRAMO_COLECTOR_M50
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tramo_colector_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tramo_colector_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tramo_colector_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tramo_colector_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tramo_colector_m50');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>TRAMO_COLECTOR_M50_SHP</Name><UserStyle><Name>TRAMO_COLECTOR_M50_SHP:_:default:TRAMO_COLECTOR_M50_SHP</Name><Title>TRAMO_COLECTOR_M50_SHP:_:default:TRAMO_COLECTOR_M50_SHP</Title><Abstract>TRAMO_COLECTOR_M50_SHP:_:default:TRAMO_COLECTOR_M50_SHP</Abstract><FeatureTypeStyle><Name>TRAMO_COLECTOR_M50_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'TRAMO_COLECTOR_M50_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'TRAMO_COLECTOR_M50_SHP:_:default:TRAMO_COLECTOR_M50_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_colec');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_colec');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_colec');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_colec');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_colec');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'tramo_cl'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_colec');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_colec');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_colec');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_colec');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_colec');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'tipo_red_interior'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]sist_trans');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]sist_trans');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'sist_impulsion'),10,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'estado'),11,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]titular');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'titular'),12,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]gestion');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'gestor'),13,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_saneam_tcl"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, "eiel_c_saneam_tcl".clave, "eiel_c_saneam_tcl".codprov, "eiel_c_saneam_tcl".codmunic, "eiel_c_saneam_tcl".tramo_cl, tipo_red_interior, sist_impulsion, estado, titular, gestor FROM "eiel_c_saneam_tcl", eiel_t_saneam_tcl, eiel_configuracion_shp WHERE "eiel_c_saneam_tcl"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 > 50000) and eiel_c_saneam_tcl.revision_expirada = 9999999999 and eiel_t_saneam_tcl.revision_expirada = 9999999999 and "eiel_c_saneam_tcl".clave="eiel_c_saneam_tcl".clave and "eiel_c_saneam_tcl".codprov="eiel_t_saneam_tcl".codprov and "eiel_c_saneam_tcl".codmunic="eiel_t_saneam_tcl".codmunic and "eiel_c_saneam_tcl".tramo_cl="eiel_t_saneam_tcl".tramo_cl and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO39";


-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'ALUMBRADO_SHP') THEN	

	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO40";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'TRAMO_CONDUCCION_SHP') THEN	

		-- CAPA TRAMO_CONDUCCION
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tramo_conduccion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tramo_conduccion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tramo_conduccion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tramo_conduccion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tramo_conduccion');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>TRAMO_CONDUCCION_SHP</Name><UserStyle><Name>TRAMO_CONDUCCION_SHP:_:default:TRAMO_CONDUCCION_SHP</Name><Title>TRAMO_CONDUCCION_SHP:_:default:TRAMO_CONDUCCION_SHP</Title><Abstract>TRAMO_CONDUCCION_SHP:_:default:TRAMO_CONDUCCION_SHP</Abstract><FeatureTypeStyle><Name>TRAMO_CONDUCCION_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'TRAMO_CONDUCCION_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'TRAMO_CONDUCCION_SHP:_:default:TRAMO_CONDUCCION_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_cond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_cond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_cond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_cond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_cond');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'tramo_cn'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_tcond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_tcond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_tcond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_tcond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_tcond');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'material'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'estado'),10,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]titular');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'titular'),11,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]gestion');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'gestor'),12,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_abast_tcn"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, eiel_c_abast_tcn.clave, eiel_c_abast_tcn.codprov, eiel_c_abast_tcn.codmunic, eiel_c_abast_tcn.tramo_cn, eiel_t_abast_tcn.material, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor FROM "eiel_c_abast_tcn", eiel_t_abast_tcn, eiel_configuracion_shp WHERE "eiel_c_abast_tcn"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 <= 50000) and eiel_c_abast_tcn.revision_expirada = 9999999999 and eiel_c_abast_tcn.revision_expirada = 9999999999 and eiel_c_abast_tcn.clave=eiel_t_abast_tcn.clave and eiel_c_abast_tcn.codprov=eiel_t_abast_tcn.codprov and eiel_c_abast_tcn.codmunic=eiel_t_abast_tcn.codmunic and eiel_c_abast_tcn.tramo_cn=eiel_t_abast_tcn.tramo_cn and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO41";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'TRAMO_CONDUCCION_M50_SHP') THEN	

		-- CAPA TRAMO_CONDUCCION_M50
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tramo_conduccion_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tramo_conduccion_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tramo_conduccion_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tramo_conduccion_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tramo_conduccion_m50');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>TRAMO_CONDUCCION_M50_SHP</Name><UserStyle><Name>TRAMO_CONDUCCION_M50_SHP:_:default:TRAMO_CONDUCCION_M50_SHP</Name><Title>TRAMO_CONDUCCION_M50_SHP:_:default:TRAMO_CONDUCCION_M50_SHP</Title><Abstract>TRAMO_CONDUCCION_M50_SHP:_:default:TRAMO_CONDUCCION_M50_SHP</Abstract><FeatureTypeStyle><Name>TRAMO_CONDUCCION_M50_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'TRAMO_CONDUCCION_M50_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'TRAMO_CONDUCCION_M50_SHP:_:default:TRAMO_CONDUCCION_M50_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_cond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_cond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_cond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_cond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_cond');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'tramo_cn'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_tcond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_tcond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_tcond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_tcond');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_tcond');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'material'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'estado'),10,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]titular');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]titular');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'titular'),11,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]gestion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]gestion');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_abast_tcn' and columns.name = 'gestor'),12,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_abast_tcn"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, eiel_c_abast_tcn.clave, eiel_c_abast_tcn.codprov, eiel_c_abast_tcn.codmunic, eiel_c_abast_tcn.tramo_cn, eiel_t_abast_tcn.material, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor FROM "eiel_c_abast_tcn", eiel_t_abast_tcn, eiel_configuracion_shp WHERE "eiel_c_abast_tcn"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 > 50000) and eiel_c_abast_tcn.revision_expirada = 9999999999 and eiel_c_abast_tcn.revision_expirada = 9999999999 and eiel_c_abast_tcn.clave=eiel_t_abast_tcn.clave and eiel_c_abast_tcn.codprov=eiel_t_abast_tcn.codprov and eiel_c_abast_tcn.codmunic=eiel_t_abast_tcn.codmunic and eiel_c_abast_tcn.tramo_cn=eiel_t_abast_tcn.tramo_cn and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO42";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'TRAMO_EMISARIO_SHP') THEN	

		-- CAPA TRAMO_EMISARIO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tramo_emisario');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tramo_emisario');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tramo_emisario');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tramo_emisario');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tramo_emisario');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>TRAMO_EMISARIO_SHP</Name><UserStyle><Name>TRAMO_EMISARIO_SHP:_:default:TRAMO_EMISARIO_SHP</Name><Title>TRAMO_EMISARIO_SHP:_:default:TRAMO_EMISARIO_SHP</Title><Abstract>TRAMO_EMISARIO_SHP:_:default:TRAMO_EMISARIO_SHP</Abstract><FeatureTypeStyle><Name>TRAMO_EMISARIO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'TRAMO_EMISARIO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'TRAMO_EMISARIO_SHP:_:default:TRAMO_EMISARIO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_emis');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'tramo_em'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_mat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_mat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_mat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_mat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_mat');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tem' and columns.name = 'material'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tem' and columns.name = 'estado'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_saneam_tem"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, eiel_c_saneam_tem.clave, eiel_c_saneam_tem.codprov, eiel_c_saneam_tem.codmunic, eiel_c_saneam_tem.tramo_em, material, estado FROM "eiel_c_saneam_tem", eiel_t_saneam_tem, eiel_configuracion_shp WHERE "eiel_c_saneam_tem"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 <= 50000) and eiel_c_saneam_tem.revision_expirada = 9999999999 and eiel_t_saneam_tem.revision_expirada = 9999999999 and eiel_c_saneam_tem.clave = eiel_t_saneam_tem.clave  and eiel_c_saneam_tem.codprov = eiel_t_saneam_tem.codprov  and eiel_c_saneam_tem.codmunic = eiel_t_saneam_tem.codmunic and eiel_c_saneam_tem.tramo_em = eiel_t_saneam_tem.tramo_em and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO43";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'TRAMO_EMISARIO_M50_SHP') THEN	

		-- CAPA TRAMO_EMISARIO_M50
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tramo_emisario_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tramo_emisario_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tramo_emisario_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tramo_emisario_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tramo_emisario_m50');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>TRAMO_EMISARIO_M50_SHP</Name><UserStyle><Name>TRAMO_EMISARIO_M50_SHP:_:default:TRAMO_EMISARIO_M50_SHP</Name><Title>TRAMO_EMISARIO_M50_SHP:_:default:TRAMO_EMISARIO_M50_SHP</Title><Abstract>TRAMO_EMISARIO_M50_SHP:_:default:TRAMO_EMISARIO_M50_SHP</Abstract><FeatureTypeStyle><Name>TRAMO_EMISARIO_M50_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'TRAMO_EMISARIO_M50_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'TRAMO_EMISARIO_M50_SHP:_:default:TRAMO_EMISARIO_M50_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_emis');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_emis');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tem' and columns.name = 'tramo_em'),8,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo_mat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo_mat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo_mat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo_mat');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo_mat');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tem' and columns.name = 'material'),9,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]estado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tem' and columns.name = 'estado'),10,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_saneam_tem"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, eiel_c_saneam_tem.clave, eiel_c_saneam_tem.codprov, eiel_c_saneam_tem.codmunic, eiel_c_saneam_tem.tramo_em, material, estado FROM "eiel_c_saneam_tem", eiel_t_saneam_tem, eiel_configuracion_shp WHERE "eiel_c_saneam_tem"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 > 50000)  and eiel_c_saneam_tem.revision_expirada = 9999999999 and eiel_t_saneam_tem.revision_expirada = 9999999999 and eiel_c_saneam_tem.clave = eiel_t_saneam_tem.clave  and eiel_c_saneam_tem.codprov = eiel_t_saneam_tem.codprov  and eiel_c_saneam_tem.codmunic = eiel_t_saneam_tem.codmunic and eiel_c_saneam_tem.tramo_em = eiel_t_saneam_tem.tramo_em and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO44";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'VERT_ENCUESTADO_SHP') THEN	
		-- CAPA VERT_ENCUESTADO
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','vert_encuestado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]vert_encuestado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]vert_encuestado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]vert_encuestado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]vert_encuestado');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>VERT_ENCUESTADO_SHP</Name><UserStyle><Name>VERT_ENCUESTADO_SHP:_:default:VERT_ENCUESTADO_SHP</Name><Title>VERT_ENCUESTADO_SHP:_:default:VERT_ENCUESTADO_SHP</Title><Abstract>VERT_ENCUESTADO_SHP:_:default:VERT_ENCUESTADO_SHP</Abstract><FeatureTypeStyle><Name>VERT_ENCUESTADO_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'VERT_ENCUESTADO_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'VERT_ENCUESTADO_SHP:_:default:VERT_ENCUESTADO_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_ver');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_ver');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_ver');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_ver');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_ver');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'orden_vt'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_vt"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_vt FROM "eiel_c_vt", eiel_configuracion_shp WHERE "eiel_c_vt"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 <= 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO45";

-- CREACION DE PERMISOS:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM LAYERS WHERE name = 'VERT_ENCUESTADO_M50_SHP') THEN	
		-- CAPA VERT_ENCUESTADO_M50
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','vert_encuestado_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]vert_encuestado_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]vert_encuestado_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]vert_encuestado_m50');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]vert_encuestado_m50');

		--Insercion de estilos - por defecto
		INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>VERT_ENCUESTADO_M50_SHP</Name><UserStyle><Name>VERT_ENCUESTADO_M50_SHP:_:default:VERT_ENCUESTADO_M50_SHP</Name><Title>VERT_ENCUESTADO_M50_SHP:_:default:VERT_ENCUESTADO_M50_SHP</Title><Abstract>VERT_ENCUESTADO_M50_SHP:_:default:VERT_ENCUESTADO_M50_SHP</Abstract><FeatureTypeStyle><Name>VERT_ENCUESTADO_M50_SHP</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size>5.0</Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,versionable) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'), (select idacl from acl where name = 'Capas EIEL SHP' limit 1),'VERT_ENCUESTADO_M50_SHP',1,0,0);

		-- insercion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),1);
		-- insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps where xml = '<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa EIEL Export SHP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa EIEL Export SHP</mapName></mapDescriptor>' limit 1),(select id_layerfamily from layerfamilies, dictionary where id_name = id_vocablo and locale='es_ES' and traduccion = 'EIEL_EXPORT_SHP' LIMIT 1),currval('seq_layers'),currval('seq_styles'),'VERT_ENCUESTADO_M50_SHP:_:default:VERT_ENCUESTADO_M50_SHP', true,0,0,true,true);

		-- insercion de atributos
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'id'),1,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'id_municipio'),2,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'GEOMETRY'),3,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fase');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fase');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase'),4,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]clave');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'clave'),5,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]prov');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]prov');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'codprov'),6,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]mun');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]mun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'codmunic'),7,true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','orden_ver');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]orden_ver');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]orden_ver');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]orden_ver');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]orden_ver');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_vt' and columns.name = 'orden_vt'),8,true);

		-- QUERY
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1, 'SELECT transform("eiel_c_vt"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_vt FROM "eiel_c_vt", eiel_configuracion_shp WHERE "eiel_c_vt"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 > 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''', '', '', '');

	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO46";











