SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET default_with_oids = true;

SET client_min_messages TO WARNING;

ALTER TABLE "public"."session_app" ALTER COLUMN "id" TYPE varchar(40);
ALTER TABLE "public"."iusercnt" ALTER COLUMN "id" TYPE varchar(40);

-- Inicio actualizar secuencias
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_domainnodes') THEN
		PERFORM setval('public.seq_domainnodes', (select max(id)::bigint from domainnodes), true);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";


-- Fin actualizar secuencias

-- Inicio vadosTablas.sql -------------------------------------------------------------------------------------------------------

--****************************************************************************************************************
-- Schema: geowfst schema
--****************************************************************************************************************
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'geowfst') THEN
		CREATE SCHEMA geowfst;
		ALTER SCHEMA geowfst OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO2";



--****************************************************************************************************************
-- Table: entidadlocalgis_entidadext
--****************************************************************************************************************
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'geowfst' AND table_name = 'entidadlocalgis_entidadext') THEN
		CREATE TABLE geowfst.entidadlocalgis_entidadext (
			id_entidad numeric NOT NULL,
			id_entidadext character varying(4) NOT NULL
		);
		ALTER TABLE geowfst.entidadlocalgis_entidadext OWNER TO postgres;
		ALTER TABLE ONLY geowfst.entidadlocalgis_entidadext ADD CONSTRAINT entidadlocalgis_key UNIQUE (id_entidad);
		REVOKE ALL ON TABLE geowfst.entidadlocalgis_entidadext FROM PUBLIC;
		REVOKE ALL ON TABLE geowfst.entidadlocalgis_entidadext FROM postgres;
		GRANT ALL ON TABLE geowfst.entidadlocalgis_entidadext TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO3";



--****************************************************************************************************************
-- Table: procedures
--****************************************************************************************************************
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'geowfst' AND table_name = 'procedures') THEN
		CREATE TABLE geowfst.procedures (
			id text NOT NULL,
			table_name text,
			layer_name text,
			map_name text,
			procedure_type text
		);
		ALTER TABLE geowfst.procedures OWNER TO postgres;
		ALTER TABLE ONLY geowfst.procedures ADD CONSTRAINT id_key UNIQUE (id);
		REVOKE ALL ON TABLE geowfst.procedures FROM PUBLIC;
		REVOKE ALL ON TABLE geowfst.procedures FROM postgres;
		GRANT ALL ON TABLE geowfst.procedures TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO4";


--****************************************************************************************************************
-- Table: entidad_tipo_map
--****************************************************************************************************************
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'geowfst' AND table_name = 'entidad_tipo_map') THEN
		CREATE TABLE geowfst.entidad_tipo_map (
			id_entidad numeric NOT NULL,
			tipo text NOT NULL,
			mapid numeric NOT NULL
		);
		ALTER TABLE geowfst.entidad_tipo_map OWNER TO postgres;
		ALTER TABLE ONLY geowfst.entidad_tipo_map ADD CONSTRAINT entidad_tipo_key UNIQUE (id_entidad, tipo);
		REVOKE ALL ON TABLE geowfst.entidad_tipo_map FROM PUBLIC;
		REVOKE ALL ON TABLE geowfst.entidad_tipo_map FROM postgres;
		GRANT ALL ON TABLE geowfst.entidad_tipo_map TO postgres;
		ALTER TABLE ONLY geowfst.entidad_tipo_map ADD CONSTRAINT entidad_fkey FOREIGN KEY (id_entidad) REFERENCES geowfst.entidadlocalgis_entidadext(id_entidad) ON UPDATE CASCADE ON DELETE RESTRICT;
		ALTER TABLE ONLY geowfst.entidad_tipo_map ADD CONSTRAINT tipo_fkey FOREIGN KEY (tipo) REFERENCES geowfst.procedures(id) ON UPDATE CASCADE ON DELETE RESTRICT;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO5";


--****************************************************************************************************************
-- Table: procedure_defaults
--****************************************************************************************************************
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'geowfst' AND table_name = 'procedure_defaults') THEN
		CREATE TABLE geowfst.procedure_defaults (
			id text NOT NULL,
			feature_name text,
			feature_toolbarname text,
			style_property text,
			address_property text,
			onfeatureunselecttext text,
			onnotfeatureinfotext text,
			onnotfeaturesearchtext text
		);
		ALTER TABLE geowfst.procedure_defaults OWNER TO postgres;
		ALTER TABLE ONLY geowfst.procedure_defaults ADD CONSTRAINT procedure_defaults_id_key PRIMARY KEY (id);
		REVOKE ALL ON TABLE geowfst.procedure_defaults FROM PUBLIC;
		REVOKE ALL ON TABLE geowfst.procedure_defaults FROM postgres;
		GRANT ALL ON TABLE geowfst.procedure_defaults TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO6";

	
--****************************************************************************************************************
-- Table: procedure_properties
--****************************************************************************************************************
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'geowfst' AND table_name = 'procedure_properties') THEN
		CREATE TABLE geowfst.procedure_properties (
			id text NOT NULL,
			grouptitle text,
			property text NOT NULL,
			name text,
			type text,
			searchactive boolean,
			active boolean
		);
		ALTER TABLE geowfst.procedure_properties OWNER TO postgres;
		ALTER TABLE ONLY geowfst.procedure_properties ADD CONSTRAINT procedure_properties_id_property_key PRIMARY KEY (id, property);
		REVOKE ALL ON TABLE geowfst.procedure_properties FROM PUBLIC;
		REVOKE ALL ON TABLE geowfst.procedure_properties FROM postgres;
		GRANT ALL ON TABLE geowfst.procedure_properties TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO7";


-- Fin vadosTablas.sql ------------------------------------------------------------------------------------------------



-- Inicio capa_vados.sql ------------------------------------------------------------------------------------------------

------- ### ACL sigm_vados ###

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT idacl FROM acl WHERE name = 'sigm_vados') THEN
		insert into acl values (NEXTVAL('seq_acl'), 'sigm_vados');
	
		-- Se asocia el ACL con los permisos de leer, escribir, etc
		insert into r_acl_perm values (871, CURRVAL('seq_acl'));
		insert into r_acl_perm values (872, CURRVAL('seq_acl'));
		insert into r_acl_perm values (873, CURRVAL('seq_acl'));
		insert into r_acl_perm values (874, CURRVAL('seq_acl'));
		
		-- Se le dan permisos al superuser
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 871, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 872, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 873, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 874, CURRVAL('seq_acl'), 1); 
		
		
		-------- ### MAPA DE sigm_vados ####
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]sigm_vados');
		----INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>sigm_vados</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>sigm_vados</mapName></mapDescriptor>',0);
		INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>sigm_vados</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>sigm_vados</mapName></mapDescriptor>',0);
		
		-- Creacion de la capa
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]sigm_vados');
		
		
		-- Estilos por defecto
		insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>sigm_vados</Name><UserStyle><Name>sigm_vados:_:default:sigm_vados</Name><Title>sigm_vados:_:default:sigm_vados</Title><Abstract>sigm_vados:_:default:sigm_vados</Abstract><FeatureTypeStyle><Name>sigm_vados</Name><Rule><Name>Otro</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#999999</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>Permanente</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsEqualTo><ogc:PropertyName>Tipo de Estilo</ogc:PropertyName><ogc:Literal>Permanente</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#0033cc</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>Laboral</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsEqualTo><ogc:PropertyName>Tipo de Estilo</ogc:PropertyName><ogc:Literal>Laboral</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#009900</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');
		
		-- PARA LOCALGIS DOS METER LA SIGUIENTE SENTENCIA
		--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),CURRVAL('seq_acl'),'sigm_vados',1,0,0);
		-- PARA LOCALGIS 2.1 METER LA SIGUIENTE SENTENCIA
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),CURRVAL('seq_acl'),'sigm_vados',1,0);

		-- Creacion de layerfamily 
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]sigm_vados');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]sigm_vados');
		INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );
		-- Asociacion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),1);


		-- Asociacion de map-layerfamily vados
		insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (CURRVAL('seq_maps'),currval('seq_layerfamilies'),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (CURRVAL('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'sigm_vados:_:default:sigm_vados', true,0,0,true,true);


		-- Asociacion de map-layerfamily Numeros Policia
		insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (CURRVAL('seq_maps'),12,2,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (CURRVAL('seq_maps'),12,12,4,'numeros_policia:_:default:numeros_policia', true,1,0,true,true);


		-- Asociacion de map-layerfamily Vias
		insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (CURRVAL('seq_maps'),8,3,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (CURRVAL('seq_maps'),8,16,21,'vias:_:default:vias', true,2,0,true,true);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (CURRVAL('seq_maps'),8,NEXTVAL('seq_domaincategory'),22,'tramosvia:_:default:tramosvia', true,3,0,true,true);

		-- Asociacion de map-layerfamily Parcelas
		insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (CURRVAL('seq_maps'),13,4,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (CURRVAL('seq_maps'),13,101,2,'parcelas:_:default:parcelas', true,4,0,true,true);
		
		
		CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
		$Q$
		BEGIN
			
		CREATE ROLE visualizador LOGIN NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
			exception when others then
				RAISE NOTICE 'No se puede crear el role de consultas';
		END;
		$Q$
		LANGUAGE plpgsql;
		select localgisDatamodelInstall() as "PASO1.5";

		IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'sigm_vados') THEN
			CREATE TABLE sigm_vados
			(
			  id numeric(8,0) NOT NULL,
			  id_municipio numeric(5,0) NOT NULL,
			  "GEOMETRY" geometry,
			  id_feature character varying(100) NOT NULL,
			  style_type character varying(100),
			  CONSTRAINT sigm_vados_pk PRIMARY KEY (id)
			);
			ALTER TABLE sigm_vados OWNER TO postgres;
			GRANT ALL ON TABLE sigm_vados TO postgres;
			GRANT ALL ON TABLE sigm_vados TO visualizador;
			GRANT SELECT ON TABLE sigm_vados TO guiaurbana;
			GRANT SELECT ON TABLE sigm_vados TO consultas;
			
			INSERT INTO geometry_columns (f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, "type")  VALUES ('', 'public', 'sigm_vados', 'GEOMETRY', 2, '4230', 'POLYGON');
		END IF;
	
	
		-- Creacion en tablas de sistema
		INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'sigm_vados',CURRVAL('seq_domaincategory'));

		-- Creacion de columna geometria
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);

		-- Creacion de columna id (con dominio autonumerico incremental - 10067)
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ID');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

		-- Creacion de columna id_municipio
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10069,CURRVAL('seq_tables'),NULL,5,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ID de Municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

		-- Creacion de columna nombre (con dominio )
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_feature',10014,CURRVAL('seq_tables'),100,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ID de Vado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ID de Vado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ID de Vado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ID de Vado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ID de Vado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),99,0);

		-- Creacion de columna area
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'style_type',10014,CURRVAL('seq_tables'),100,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Estilo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Estilo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Estilo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Estilo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Estilo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),99,0);
						
	END IF;
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO8";






-- ESTILO MODIFICADO POR DAVID
--<?xml version="1.0" encoding="ISO-8859-1"?>
--<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
--<NamedLayer><Name>sigm_vados</Name><UserStyle><Name>sigm_vados:_:default</Name><Title>sigm_vados:_:default</Title><Abstract>sigm_vados:_:default</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#0000ff</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke'>#ffff00</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>sigm_vados 0</Name><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:PropertyIsEqualTo><ogc:PropertyName>Tipo de Estilo</ogc:PropertyName><ogc:Literal>Laboral</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
--<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#c31b2d</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>sigm_vados 1</Name><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:PropertyIsEqualTo><ogc:PropertyName>Tipo de Estilo</ogc:PropertyName><ogc:Literal>Permanente</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
--<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#8f03d7</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
--</StyledLayerDescriptor>






-- Asociacion de queries. 
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'sigm_vados') THEN
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
		'SELECT transform("sigm_vados"."GEOMETRY", ?T) AS "GEOMETRY","sigm_vados"."id","sigm_vados"."id_municipio","sigm_vados"."id_feature","sigm_vados"."style_type" FROM "sigm_vados" WHERE "sigm_vados"."id_municipio" IN (?M)',
		'UPDATE "sigm_vados" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio" = ?M,"id_feature"=?4,"style_type"=?5 WHERE "id"=?2',
		'INSERT INTO "sigm_vados" ("GEOMETRY","id","id_municipio","id_feature","style_type") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,?4,?5)',
		'DELETE FROM "sigm_vados" WHERE "id"=?2');
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO10";

-- Creacion de la secuencia
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_sigm_vados') THEN
		CREATE SEQUENCE seq_sigm_vados
		  INCREMENT 1
		  MINVALUE 1
		  MAXVALUE 9223372036854775807
		  START 1
		  CACHE 1;
		 ALTER TABLE seq_sigm_vados OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO11";

--insert into r_usr_perm (userid,idperm,idacl,aplica) values
--(560, 871, 204, 1),
--(560, 872, 204, 1),
--(560, 873, 204, 1),
--(560, 874, 204, 1);

-- Fin capa_vados.sql -------------------------------------------------------------------------------------------------------


-- Inicio config_vados.sql ------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM geowfst.procedures WHERE id = 'sigm_vados') THEN
		INSERT INTO geowfst.procedures (id, table_name, layer_name, map_name, procedure_type) VALUES 
		('sigm_vados', 'sigm_vados', 'sigm_vados','sigm_vados', 'VADO');

		INSERT INTO geowfst.procedure_defaults (id, feature_name, feature_toolbarname, style_property, address_property, onfeatureunselecttext, onnotfeatureinfotext, onnotfeaturesearchtext) VALUES 
		('sigm_vados', 'vado', 'ToolbarWfstPolygonLocalgis', 'VADO.TIPO', 'VADO.UBICACION', 'Seleccione un vado',	'Vado sin datos', 'No se encontró ningún vado con estas características');

		INSERT INTO geowfst.procedure_properties (id, grouptitle, property, name, type, searchactive, active) VALUES
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.CIUDAD',	'Ciudad',	'string',	false,	true),
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.CPOSTAL',	'Código Postal',	'string',	true,	true),
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.DIRECCIONTELEMATICA',	'Dirección Telemática',	'string',	true,	true),
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.DOMICILIO',	'Domicilio',	'string',	true,	true),
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.IDENTIDADTITULAR',	'Nombre',	'string',	true,	true),
		('sigm_vados',	'expediente',	'SPAC_EXPEDIENTES.LOCALIZACION',	'Localización',	'string',	false,	true),
		('sigm_vados',	'expediente',	'SPAC_EXPEDIENTES.MUNICIPIO',	'Municipio',	'string',	false,	true),
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.NIFCIFTITULAR',	'NIF/CIF',	'string',	true,	true),
		('sigm_vados',	'expediente',	'SPAC_EXPEDIENTES.NUMEXP',	'Nº de Expediente',	'string',	true,	true),
		('sigm_vados',	'expediente',	'SPAC_EXPEDIENTES.POBLACION',	'Población',	'string',	false,	true),
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.REGIONPAIS',	'Región/País',	'string',	false,	true),
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.TFNOFIJO',	'Teléfono Fijo',	'string',	true,	true),
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.TFNOMOVIL',	'Teléfono Movil',	'string',	true,	true),
		('sigm_vados',	'interesado',	'SPAC_EXPEDIENTES.TIPOPERSONA',	'Tipo de Persona',	'string',	true,	true),
		('sigm_vados',	'vado',	'VADO.ACTIVIDAD',	'Actividad del Local',	'string',	true,	true),
		('sigm_vados',	'vado',	'VADO.ANCHOACERA',	'Ancho de la Acera',	'decimal',	true,	true),
		('sigm_vados',	'vado',	'VADO.LARGOENTRADA',	'Largo de la Entrada',	'decimal',	true,	true),
		('sigm_vados',	'vado',	'VADO.NUMLICENCIA',	'Número de Licencia',	'string',	true,	true),
		('sigm_vados',	'vado',	'VADO.NUMVEHICULOS',	'Nº de Plazas para Vehículos',	'integer', true, true),
		('sigm_vados',	'vado',	'VADO.REBAJE',	'Rebaje de la Acera',	'string',	true,	true),
		('sigm_vados',	'vado',	'VADO.SUPERFICIELOCAL',	'Superficie Local',	'decimal',	true,	true),
		('sigm_vados',	'vado',	'VADO.TASAS',	'Tasas de la Licencia',	'decimal',	true,	true),
		('sigm_vados',	'vado',	'VADO.TIPO',	'Tipo de Vado',	'string',	true,	true),
		('sigm_vados',	'vado',	'VADO.UBICACION',	'Ubicación',	'string',	true,	true);

-- Fin config_vados.sql ------------------------------------------------------------------------------------------------
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO11.1";



-- Inicio capas_aguas.sql ---------------------------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Capas de Red') THEN
		
		------- ### ACL Aguas ###
		insert into acl values (NEXTVAL('seq_acl'), 'Capas de Red');

		-- Se asocia el ACL con los permisos de leer, escribir, etc
		insert into r_acl_perm values (871, CURRVAL('seq_acl'));
		insert into r_acl_perm values (872, CURRVAL('seq_acl'));
		insert into r_acl_perm values (873, CURRVAL('seq_acl'));
		insert into r_acl_perm values (874, CURRVAL('seq_acl'));

		-- Se le dan permisos al superuser
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 871, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 872, CURRVAL('seq_acl'), 1);
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 873, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 874, CURRVAL('seq_acl'), 1); 


		-------- ### MAPA DE AGUAS ####
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Mapa de Aguas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Mapa de Aguas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Mapa de Aguas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Mapa de Aguas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Mapa de Aguas');
		INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(30,CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa de Aguas</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa de Aguas</mapName></mapDescriptor>',0);

		-- Creacion de la capa
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','base_red_abastecimiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]base_red_abastecimiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]base_red_abastecimiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]base_red_abastecimiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]base_red_abastecimiento');
		-- PARA LOCALGIS DOS METER LA SIGUIENTE SENTENCIA
		--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),CURRVAL('seq_acl'),'base_red_abastecimiento',1,0,0);
		-- PARA LOCALGIS 2.1 METER LA SIGUIENTE SENTENCIA
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),CURRVAL('seq_acl'),'base_red_abastecimiento',1,0);

		-- Creacion de layerfamily 
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','familia_abastecimiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]familia_abastecimiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]familia_abastecimiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]familia_abastecimiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]familia_abastecimiento');
		INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

		-- Asociacion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),1);
		insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (30,currval('seq_layerfamilies'),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (30,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:base_red_abastecimiento', true,0,0,true,true);

		-- Creacion en tablas de sistema
		INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'red_abastecimiento',3);

		-- Creacion de columna id (con dominio autonumerico incremental - 10067)
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

		-- Creacion de columna id_municipio (con dominio municipio obligatorio - 10069)
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10069,CURRVAL('seq_tables'),NULL,5,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]id_municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]id_municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

		-- Creacion de columna geometry
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]GEOMETRY');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]GEOMETRY');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);

		-- Creacion de columna tipo
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo',NULL,CURRVAL('seq_tables'),1,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]tipo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]tipo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]tipo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]tipo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);

		-- Creacion de columna diameter
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'diameter',NULL,CURRVAL('seq_tables'),NULL,5,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','diameter');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]diameter');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]diameter');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]diameter');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]diameter');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);

		-- Creacion de columna roughness
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'roughness',NULL,CURRVAL('seq_tables'),NULL,5,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','roughness');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]roughness');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]roughness');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]roughness');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]roughness');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);

		-- Creacion de columna minorloss
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'minorloss',NULL,CURRVAL('seq_tables'),30,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','minorloss');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]minorloss');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]minorloss');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]minorloss');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]minorloss');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);

		-- Creacion de columna status
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'status',NULL,CURRVAL('seq_tables'),30,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','status');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]status');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]status');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]status');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]status');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);

		-- Creacion de columna type
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'type',NULL,CURRVAL('seq_tables'),30,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','type');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]type');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]type');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]type');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]type');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),9,true);

		-- Creacion de columna setting
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'setting',NULL,CURRVAL('seq_tables'),NULL,5,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','setting');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]setting');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]setting');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]setting');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]setting');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),10,true);

		-- Creacion de columna node1
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'node1',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','node1');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]node1');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]node1');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]node1');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]node1');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),CURRVAL('seq_domaincategory'),true);

		-- Creacion de columna node2
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'node2',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','node2');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]node2');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]node2');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]node2');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]node2');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),12,true);

		-- Creacion de columna length
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'length',NULL,CURRVAL('seq_tables'),30,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','length');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]length');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]length');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]length');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]length');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),13,true);

		-- Creacion de columna numero_policia
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'numero_policia',NULL,CURRVAL('seq_tables'),100,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','numero_policia');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]numero_policia');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]numero_policia');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]numero_policia');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]numero_policia');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),14,true);

		-- Asociacion de queries. 
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
			'SELECT "red_abastecimiento"."id","red_abastecimiento"."id_municipio",transform("red_abastecimiento"."GEOMETRY", ?T) AS "GEOMETRY","red_abastecimiento"."tipo","red_abastecimiento"."diameter","red_abastecimiento"."roughness","red_abastecimiento"."minorloss","red_abastecimiento"."status","red_abastecimiento"."type","red_abastecimiento"."setting","red_abastecimiento"."node1","red_abastecimiento"."node2","red_abastecimiento"."length","red_abastecimiento"."numero_policia" FROM "red_abastecimiento" WHERE "red_abastecimiento"."id_municipio" IN (?M)',
			'UPDATE "red_abastecimiento" SET "id"=?1,"id_municipio" = ?M,"GEOMETRY"=transform(GeometryFromText(text(?3),?S), ?T),"tipo"=?4,"diameter"=?5,"roughness"=?6,"minorloss"=?7,"status"=?8,"type"=?9,"setting"=?10,"node1"=?11,"node2"=?12,"length"=?13,"numero_policia"=?14 WHERE "id"=?1',
			'INSERT INTO "red_abastecimiento" ("id","id_municipio","GEOMETRY","tipo","diameter","roughness","minorloss","status","type","setting","node1","node2","length","numero_policia") VALUES(?PK,?M,transform(GeometryFromText(text(?3),?S), ?T),?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14)',
			'DELETE FROM "red_abastecimiento" WHERE "id"=?1');
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO11.2";

------- ### CAPA AGUAS ###
-- Creacion de la tabla
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'red_abastecimiento') THEN
		CREATE TABLE red_abastecimiento
		(
		  id numeric(8) NOT NULL,
		  id_municipio numeric(5) NOT NULL,
		  "GEOMETRY" geometry,
		  tipo character varying(1),
		  diameter numeric(5),
		  roughness numeric(5),
		  minorloss character varying(30),
		  status character varying(30),
		  "type" character varying(30),
		  setting numeric(5),
		  node1 integer,
		  node2 integer,
		  "length" character varying(30),
		  numero_policia character varying(100),
		  CONSTRAINT red_abastecimiento_pkey PRIMARY KEY (id),
		  CONSTRAINT red_abastecimiento_id_key UNIQUE (id)
		)
		WITH (OIDS=TRUE);
		ALTER TABLE red_abastecimiento OWNER TO postgres;
		GRANT ALL ON TABLE red_abastecimiento TO postgres;
		GRANT ALL ON TABLE red_abastecimiento TO visualizador;
		GRANT SELECT ON TABLE red_abastecimiento TO guiaurbana;
		GRANT SELECT ON TABLE red_abastecimiento TO consultas;
		CREATE INDEX red_abastecimientoO_idx on red_abastecimiento using gist("GEOMETRY");
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO12";


-- Estilos por defecto
insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>base_red_abastecimiento</Name><UserStyle><Name>default:base_red_abastecimiento</Name><Title>default:base_red_abastecimiento</Title><Abstract>default:base_red_abastecimiento</Abstract><FeatureTypeStyle><Name>base_red_abastecimiento</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>a 0</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>tipo</ogc:PropertyName><ogc:Literal>a</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#00ffff</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>numberPolice</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>tipo</ogc:PropertyName><ogc:Literal>h</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ffff00</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>5.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>square</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>pipe</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>tipo</ogc:PropertyName><ogc:Literal>p</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#0033ff</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>valve</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>tipo</ogc:PropertyName><ogc:Literal>v</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#0fc724</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>5.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>square</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>reservoir</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>tipo</ogc:PropertyName><ogc:Literal>r</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>5.0</ogc:Literal></CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');



-- Creacion de la secuencia
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_red_abastecimiento') THEN
		CREATE SEQUENCE seq_red_abastecimiento
		  INCREMENT 1
		  MINVALUE 1
		  MAXVALUE 9223372036854775807
		  START 1
		  CACHE 1;
		ALTER TABLE seq_red_abastecimiento OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO13";

-- Fin capas_aguas.sql -------------------------------------------------------------------------------------------------------



-- Inicio EsquemaCementerio.sql -------------------------------------------------------------------------------------------------------

-- Dumped from database version 8.4.7
-- Dumped by pg_dump version 9.0.1
-- Started on 2011-07-28 10:21:59

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 11 (class 2615 OID 110160)
-- Name: cementerio; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'cementerio') THEN
		CREATE SCHEMA cementerio;
		ALTER SCHEMA cementerio OWNER TO postgres;
		SET search_path = cementerio, pg_catalog;
		SET default_tablespace = '';
		SET default_with_oids = true;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO14";

--
-- TOC entry 4076 (class 1259 OID 131358)
-- Dependencies: 11
-- Name: anexo_cementerio; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'anexo_cementerio') THEN
		CREATE TABLE cementerio.anexo_cementerio (
			id_documento integer NOT NULL,
			id_elemcementerio integer NOT NULL,
			tipo character varying NOT NULL,
			subtipo character varying NOT NULL
		);
		ALTER TABLE cementerio.anexo_cementerio OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO15";



--
-- TOC entry 4077 (class 1259 OID 131363)
-- Dependencies: 11
-- Name: anexo_feature; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'anexo_feature') THEN
		CREATE TABLE cementerio.anexo_feature (
			id_documento integer NOT NULL,
			id_layer integer NOT NULL,
			id_feature numeric(8,0) NOT NULL
		);
		ALTER TABLE cementerio.anexo_feature OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO16";

--
-- TOC entry 4044 (class 1259 OID 110167)
-- Dependencies: 11
-- Name: bloque; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'bloque') THEN
		CREATE TABLE cementerio.bloque (
			id integer NOT NULL,
			tipo_bloque integer,
			num_filas integer,
			num_columnas integer,
			descripcion character varying,
			id_feature numeric(8,0) NOT NULL,
			id_elemcementerio integer
		);
		ALTER TABLE cementerio.bloque OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO17";

--
-- TOC entry 4074 (class 1259 OID 130518)
-- Dependencies: 11
-- Name: bloque_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'bloque_id_seq') THEN
		CREATE SEQUENCE bloque_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.bloque_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO18";

--
-- TOC entry 4073 (class 1259 OID 130508)
-- Dependencies: 11
-- Name: cementerio; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'cementerio') THEN
		CREATE TABLE cementerio.cementerio (
			id integer NOT NULL,
			nombre character varying(255)
		);
		ALTER TABLE cementerio.cementerio OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO19";
	
--
-- TOC entry 4055 (class 1259 OID 110234)
-- Dependencies: 11
-- Name: cementerio_feature; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'cementerio_feature') THEN
		CREATE TABLE cementerio.cementerio_feature (
			id_elem integer NOT NULL,
			id_feature numeric(8,0) NOT NULL,
			id_layer character varying(50) NOT NULL
		);
		ALTER TABLE cementerio.cementerio_feature OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO20";

--
-- TOC entry 4051 (class 1259 OID 110200)
-- Dependencies: 11
-- Name: concesion; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'concesion') THEN
		CREATE TABLE cementerio.concesion (
			localizado character varying(50),
			fecha_ini date,
			fecha_fin date,
			ultima_renova date,
			ultimo_titular character varying(50),
			id_tarifa integer NOT NULL,
			id_unidad integer NOT NULL,
			estado integer,
			id integer NOT NULL,
			tipo_concesion integer,
			descripcion character varying,
			codigo character varying,
			precio_final character varying,
			bonificacion character varying
		);
		ALTER TABLE cementerio.concesion OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO21";

--
-- TOC entry 4061 (class 1259 OID 120234)
-- Dependencies: 11
-- Name: concesion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'concesion_id_seq') THEN
		CREATE SEQUENCE cementerio.concesion_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.concesion_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO22";
	
--
-- TOC entry 4050 (class 1259 OID 110197)
-- Dependencies: 11
-- Name: datosfallecimiento; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'datosfallecimiento') THEN
		CREATE TABLE cementerio.datosfallecimiento (
			lugar character varying(50),
			poblacion character varying(50),
			causa_fundamental character varying(50),
			causa_inmediata character varying(50),
			fecha timestamp without time zone,
			id integer NOT NULL,
			medico character varying(50),
			num_colegiado character varying,
			registro_civill character varying,
			referencia character varying
		);
		ALTER TABLE cementerio.datosfallecimiento OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO23";

--
-- TOC entry 4049 (class 1259 OID 110194)
-- Dependencies: 11
-- Name: difunto; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'difunto') THEN
		CREATE TABLE cementerio.difunto (
			fecha_defuncion timestamp without time zone,
			edad_difunto integer,
			id integer NOT NULL,
			id_datosfallecimiento integer NOT NULL,
			id_plaza integer NOT NULL,
			dni_persona character varying(15) NOT NULL,
			grupo integer,
			codigo character varying
		);
		ALTER TABLE cementerio.difunto OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO24";

--
-- TOC entry 4071 (class 1259 OID 129868)
-- Dependencies: 11
-- Name: difunto_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'difunto_id_seq') THEN
		CREATE SEQUENCE cementerio.difunto_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.difunto_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO25";

--
-- TOC entry 4075 (class 1259 OID 131338)
-- Dependencies: 4397 4398 4399 4400 11
-- Name: documento; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'documento') THEN
		CREATE TABLE cementerio.documento (
			id_documento integer NOT NULL,
			id_municipio numeric(5,0) NOT NULL,
			nombre character varying(255),
			fecha_alta timestamp without time zone,
			fecha_modificacion timestamp without time zone,
			tipo numeric(4,0),
			publico numeric(1,0) DEFAULT 1,
			tamanio numeric(12,0),
			comentario character varying(1000),
			thumbnail bytea,
			is_imgdoctext character(1) DEFAULT 'D'::bpchar,
			oculto numeric(1,0) DEFAULT 0,
			CONSTRAINT check_is_imgdoctext CHECK ((((is_imgdoctext = 'D'::bpchar) OR (is_imgdoctext = 'I'::bpchar)) OR (is_imgdoctext = 'T'::bpchar)))
		);
		ALTER TABLE cementerio.documento OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO26";

--
-- TOC entry 4078 (class 1259 OID 131373)
-- Dependencies: 11
-- Name: documento_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'documento_id_seq') THEN
		CREATE SEQUENCE cementerio.documento_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.documento_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO27";

--
-- TOC entry 4079 (class 1259 OID 131705)
-- Dependencies: 11
-- Name: documento_tipos; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'documento_tipos') THEN
		CREATE TABLE cementerio.documento_tipos (
			tipo numeric(4,0),
			extension character varying(25),
			mime_type character varying(100)
		);
		ALTER TABLE cementerio.documento_tipos OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO28";
--
-- TOC entry 4043 (class 1259 OID 110161)
-- Dependencies: 11
-- Name: elem_cementerio; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'elem_cementerio') THEN
		CREATE TABLE cementerio.elem_cementerio (
			id integer NOT NULL,
			id_municipio numeric(5,0) NOT NULL,
			tipo character varying(2),
			nombre character varying(255),
			entidad character varying(255),
			id_cementerio integer,
			subtipo character varying
		);
		ALTER TABLE cementerio.elem_cementerio OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO29";
--
-- TOC entry 4058 (class 1259 OID 120014)
-- Dependencies: 11
-- Name: elem_cementerio_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'elem_cementerio_id_seq') THEN
		CREATE SEQUENCE cementerio.elem_cementerio_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.elem_cementerio_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO30";
SET default_with_oids = false;

--
-- TOC entry 4083 (class 1259 OID 135845)
-- Dependencies: 11
-- Name: elem_feature; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'elem_feature') THEN
		CREATE TABLE cementerio.elem_feature (
			id integer NOT NULL,
			id_elem integer NOT NULL,
			id_layer character varying,
			id_cementerio integer,
			tipo character varying(5),
			subtipo character varying(5),
			nombre character varying,
			entidad character varying,
			id_feature numeric(8,0),
			id_municipio numeric(5,0)
		);
		ALTER TABLE cementerio.elem_feature OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO31";
--
-- TOC entry 4084 (class 1259 OID 136603)
-- Dependencies: 11
-- Name: elem_feature_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'elem_feature_id_seq') THEN
		CREATE SEQUENCE cementerio.elem_feature_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.elem_feature_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO32";
SET default_with_oids = true;

--
-- TOC entry 4086 (class 1259 OID 136863)
-- Dependencies: 11
-- Name: exhumacion; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'exhumacion') THEN
		CREATE TABLE cementerio.exhumacion (
			informe character varying,
			id integer NOT NULL,
			contenedor integer,
			codigo character varying,
			fecha_exhumacion date,
			id_difunto integer,
			id_tarifa integer,
			tipo_exhumacion integer,
			red_restos integer,
			traslado integer,
			precio_final character varying,
			bonificacion character varying
		);
		ALTER TABLE cementerio.exhumacion OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO33";
--
-- TOC entry 4085 (class 1259 OID 136763)
-- Dependencies: 11
-- Name: exhumacion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'exhumacion_id_seq') THEN
		CREATE SEQUENCE cementerio.exhumacion_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.exhumacion_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO34";
--
-- TOC entry 4072 (class 1259 OID 129870)
-- Dependencies: 11
-- Name: fallecimiento_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'fallecimiento_id_seq') THEN
		CREATE SEQUENCE cementerio.fallecimiento_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.fallecimiento_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO35";
SET default_with_oids = false;

--
-- TOC entry 4087 (class 1259 OID 136906)
-- Dependencies: 11
-- Name: historico_difuntos; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'historico_difuntos') THEN
		CREATE TABLE cementerio.historico_difuntos (
			id integer NOT NULL,
			id_difunto integer,
			tipo character varying,
			id_elem integer,
			fecha_operacion date,
			comentario character varying
		);
		ALTER TABLE cementerio.historico_difuntos OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO36";
--
-- TOC entry 4088 (class 1259 OID 136914)
-- Dependencies: 11
-- Name: historico_difuntos_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'historico_difuntos_id_seq') THEN
		CREATE SEQUENCE cementerio.historico_difuntos_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.historico_difuntos_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO37";
--
-- TOC entry 4089 (class 1259 OID 137184)
-- Dependencies: 11
-- Name: historico_propiedad; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'historico_propiedad') THEN
		CREATE TABLE cementerio.historico_propiedad (
			id integer NOT NULL,
			dni_titular character varying,
			tipo character varying,
			id_elem integer,
			fecha_operacion date,
			comentario character varying
		);
		ALTER TABLE cementerio.historico_propiedad OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO38";
--
-- TOC entry 4090 (class 1259 OID 137192)
-- Dependencies: 11
-- Name: historico_propiedad_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'historico_propiedad_id_seq') THEN
		CREATE SEQUENCE cementerio.historico_propiedad_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.historico_propiedad_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO36";
SET default_with_oids = true;

--
-- TOC entry 4053 (class 1259 OID 110218)
-- Dependencies: 11
-- Name: inhumacion; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'inhumacion') THEN
		CREATE TABLE cementerio.inhumacion (
			informe character varying,
			id integer NOT NULL,
			contenedor integer,
			codigo character varying,
			fecha_inhumacion date,
			id_difunto integer,
			id_tarifa integer,
			tipo_inhumacion integer,
			precio_final character varying,
			bonificacion character varying
		);
		ALTER TABLE cementerio.inhumacion OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO40";
--
-- TOC entry 4080 (class 1259 OID 132731)
-- Dependencies: 11
-- Name: inhumacion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'inhumacion_id_seq') THEN
		CREATE SEQUENCE cementerio.inhumacion_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.inhumacion_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO41";
--
-- TOC entry 4047 (class 1259 OID 110182)
-- Dependencies: 11
-- Name: intervencion; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'intervencion') THEN
		CREATE TABLE cementerio.intervencion (
			id integer NOT NULL,
			informe character varying(50),
			fecha_fin date,
			fecha_inicio date,
			localizacion character varying,
			codigo character varying,
			estado character varying
		);
		ALTER TABLE cementerio.intervencion OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO42";
--
-- TOC entry 4064 (class 1259 OID 128162)
-- Dependencies: 11
-- Name: intervencion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'intervencion_id_seq') THEN
		CREATE SEQUENCE cementerio.intervencion_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.intervencion_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO43";
--
-- TOC entry 4054 (class 1259 OID 110231)
-- Dependencies: 11
-- Name: localizacion; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'localizacion') THEN
		CREATE TABLE cementerio.localizacion (
			descripcion character varying(50),
			valor real,
			id integer NOT NULL
		);
		ALTER TABLE cementerio.localizacion OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO44";
--
-- TOC entry 4059 (class 1259 OID 120018)
-- Dependencies: 11
-- Name: localizacion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'localizacion_id_seq') THEN
		CREATE SEQUENCE cementerio.localizacion_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.localizacion_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO45";
--
-- TOC entry 4045 (class 1259 OID 110170)
-- Dependencies: 11
-- Name: persona; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'persona') THEN
		CREATE TABLE cementerio.persona (
			nombre character varying(255),
			apellido1 character varying(255),
			apellido2 character varying(255),
			dni character varying(15) NOT NULL,
			sexo character varying(255),
			estado_civil character varying(255),
			fecha_nacimiento timestamp without time zone,
			domicilio character varying(255),
			poblacion character varying(255),
			telefono character varying(255)
		);
		ALTER TABLE cementerio.persona OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO46";
--
-- TOC entry 4048 (class 1259 OID 110188)
-- Dependencies: 11
-- Name: plaza; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'plaza') THEN
		CREATE TABLE cementerio.plaza (
			situacion character varying(50),
			modicado timestamp without time zone,
			id_unidadenterramiento integer NOT NULL,
			id integer NOT NULL,
			estado integer
		);
		ALTER TABLE cementerio.plaza OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO47";
--
-- TOC entry 4069 (class 1259 OID 128652)
-- Dependencies: 11
-- Name: plaza_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'plaza_id_seq') THEN
		CREATE SEQUENCE cementerio.plaza_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.plaza_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO48";
SET default_with_oids = false;

--
-- TOC entry 4063 (class 1259 OID 120276)
-- Dependencies: 4396 11
-- Name: reltitular; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'reltitular') THEN
		CREATE TABLE cementerio.reltitular (
			dni_persona character varying(15) NOT NULL,
			id_concesion integer NOT NULL,
			esprincipal boolean DEFAULT false
		);
		ALTER TABLE cementerio.reltitular OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO49";
SET default_with_oids = true;

--
-- TOC entry 4052 (class 1259 OID 110209)
-- Dependencies: 11
-- Name: tarifa; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'tarifa') THEN
		CREATE TABLE cementerio.tarifa (
			concepto character varying(50),
			tipo_tarifa integer,
			tipo_calculo integer,
			id integer NOT NULL,
			id_cementerio integer,
			categoria integer,
			precio character varying
		);
		ALTER TABLE cementerio.tarifa OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO50";
--
-- TOC entry 4062 (class 1259 OID 120265)
-- Dependencies: 11
-- Name: tarifa_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'tarifa_id_seq') THEN
		CREATE SEQUENCE cementerio.tarifa_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.tarifa_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO51";
--
-- TOC entry 4046 (class 1259 OID 110176)
-- Dependencies: 11
-- Name: unidadenterramiento; Type: TABLE; Schema: cementerio; Owner: postgres; Tablespace: 
--

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'unidadenterramiento') THEN
		CREATE TABLE cementerio.unidadenterramiento (
			id integer NOT NULL,
			columna integer,
			fila integer,
			estado integer,
			numplazas integer,
			fult_construccion date,
			fult_modificacion date,
			id_elemcementerio integer NOT NULL,
			id_localizacion integer NOT NULL,
			tipo_unidad integer,
			descripcion character varying(250),
			codigo character varying(250),
			freforma date
		);
		ALTER TABLE cementerio.unidadenterramiento OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO52";
--
-- TOC entry 4060 (class 1259 OID 120020)
-- Dependencies: 11
-- Name: unidadenterramiento_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: postgres
--
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'unidadenterramiento_id_seq') THEN
		CREATE SEQUENCE cementerio.unidadenterramiento_id_seq
			START WITH 1
			INCREMENT BY 1
			NO MINVALUE
			NO MAXVALUE
			CACHE 1;
		ALTER TABLE cementerio.unidadenterramiento_id_seq OWNER TO postgres;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO53";
--
-- TOC entry 4434 (class 2606 OID 137370)
-- Dependencies: 4076 4076 4076 4076 4076
-- Name: anexo_cementerio_pkey; Type: CONSTRAINT; Schema: cementerio; Owner: postgres; Tablespace: 
--
create or replace function create_constraint_if_not_exists (
    c_name text, constraint_sql text
) 
returns void AS
$$
begin
    -- Look for our constraint
    if not exists (SELECT conname FROM pg_constraint WHERE conname = c_name) then
        execute constraint_sql;
    end if;
end;
$$ language 'plpgsql';

SELECT create_constraint_if_not_exists(
        'anexo_cementerio_pkey',
        'ALTER TABLE ONLY cementerio.anexo_cementerio ADD CONSTRAINT anexo_cementerio_pkey PRIMARY KEY (id_elemcementerio, id_documento, tipo, subtipo);');


--
-- TOC entry 4426 (class 2606 OID 120077)
-- Dependencies: 4055 4055 4055 4055
-- Name: cementerio_feature_pkey; Type: CONSTRAINT; Schema: cementerio; Owner: postgres; Tablespace: 
--

SELECT create_constraint_if_not_exists(
        'cementerio_feature_pkey',
        'ALTER TABLE ONLY cementerio.cementerio_feature ADD CONSTRAINT cementerio_feature_pkey PRIMARY KEY (id_elem, id_layer, id_feature);');


--
-- TOC entry 4418 (class 2606 OID 120291)
-- Dependencies: 4051 4051
-- Name: concesion_pk; Type: CONSTRAINT; Schema: cementerio; Owner: postgres; Tablespace: 
--

SELECT create_constraint_if_not_exists(
        'cementerio_feature_pkey',
        'ALTER TABLE ONLY cementerio.cementerio_feature ADD CONSTRAINT cementerio_feature_pkey PRIMARY KEY (id_elem, id_layer, id_feature);');

		
SELECT create_constraint_if_not_exists(
        'concesion_pk',
        'ALTER TABLE ONLY cementerio.concesion ADD CONSTRAINT concesion_pk PRIMARY KEY (id);');

				
SELECT create_constraint_if_not_exists(
        'historicodifuntos_pk',
        'ALTER TABLE ONLY cementerio.historico_difuntos ADD CONSTRAINT historicodifuntos_pk PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
        'historicopropiedad_pk',
        'ALTER TABLE ONLY cementerio.historico_propiedad ADD CONSTRAINT historicopropiedad_pk PRIMARY KEY (id);');		

SELECT create_constraint_if_not_exists(
        'pk_bloque',
        'ALTER TABLE ONLY cementerio.bloque ADD CONSTRAINT pk_bloque PRIMARY KEY (id);');	

SELECT create_constraint_if_not_exists(
        'pk_cementerio',
        'ALTER TABLE ONLY cementerio.cementerio.cementerio ADD CONSTRAINT pk_cementerio PRIMARY KEY (id);');	

SELECT create_constraint_if_not_exists(
        'pk_datosfallecimiento',
        'ALTER TABLE ONLY cementerio.datosfallecimiento ADD CONSTRAINT pk_datosfallecimiento PRIMARY KEY (id);');	

SELECT create_constraint_if_not_exists(
        'pk_difunto',
        'ALTER TABLE ONLY cementerio.difunto ADD CONSTRAINT pk_difunto PRIMARY KEY (id);');
		
SELECT create_constraint_if_not_exists(
        'pk_elem_cementerio',
        'ALTER TABLE ONLY cementerio.elem_cementerio ADD CONSTRAINT pk_elem_cementerio PRIMARY KEY (id);');
		
SELECT create_constraint_if_not_exists(
        'pk_elemfeature',
        'ALTER TABLE ONLY cementerio.elem_feature ADD CONSTRAINT pk_elemfeature PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
        'pk_exhumacion',
        'ALTER TABLE ONLY cementerio.exhumacion ADD CONSTRAINT pk_exhumacion PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
        'pk_inhumacion',
        'ALTER TABLE ONLY cementerio.inhumacion ADD CONSTRAINT pk_inhumacion PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
        'pk_intervencion',
        'ALTER TABLE ONLY cementerio.intervencion ADD CONSTRAINT pk_intervencion PRIMARY KEY (id);');


SELECT create_constraint_if_not_exists(
        'pk_localizacion',
        'ALTER TABLE ONLY cementerio.localizacion ADD CONSTRAINT pk_localizacion PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
        'pk_persona',
        'ALTER TABLE ONLY cementerio.persona ADD CONSTRAINT pk_persona PRIMARY KEY (dni);');

SELECT create_constraint_if_not_exists(
        'pk_plaza',
        'ALTER TABLE ONLY cementerio.plaza ADD CONSTRAINT pk_plaza PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
        'pk_tarifa',
        'ALTER TABLE ONLY cementerio.tarifa ADD CONSTRAINT pk_tarifa PRIMARY KEY (id);');

SELECT create_constraint_if_not_exists(
        'pk_unidadenterramiento',
        'ALTER TABLE ONLY cementerio.unidadenterramiento ADD CONSTRAINT pk_unidadenterramiento PRIMARY KEY (id);');
		
SELECT create_constraint_if_not_exists(
        'reltitular_pkey',
        'ALTER TABLE ONLY cementerio.reltitular ADD CONSTRAINT reltitular_pkey PRIMARY KEY (dni_persona, id_concesion);');

SELECT create_constraint_if_not_exists(
        'tabla_anexofeature_pkey',
        'ALTER TABLE ONLY cementerio.anexo_feature ADD CONSTRAINT tabla_anexofeature_pkey PRIMARY KEY (id_documento, id_layer, id_feature);');

SELECT create_constraint_if_not_exists(
        'tabla_documento_pkey',
        'ALTER TABLE ONLY cementerio.documento ADD CONSTRAINT tabla_documento_pkey PRIMARY KEY (id_documento);');


SELECT create_constraint_if_not_exists(
        'cementerio_fk',
        'ALTER TABLE ONLY cementerio.tarifa ADD CONSTRAINT cementerio_fk FOREIGN KEY (id_cementerio) REFERENCES cementerio.cementerio(id);');
	

SELECT create_constraint_if_not_exists(
        'cementerio_fkey',
        'ALTER TABLE ONLY cementerio.unidadenterramiento ADD CONSTRAINT cementerio_fkey FOREIGN KEY (id_elemcementerio) REFERENCES cementerio.elem_cementerio(id);');	

SELECT create_constraint_if_not_exists(
        'concesion_fk',
        'ALTER TABLE ONLY cementerio.reltitular ADD CONSTRAINT concesion_fk FOREIGN KEY (id_concesion) REFERENCES cementerio.concesion(id);');	
		
SELECT create_constraint_if_not_exists(
        'difuntodatosfall_fkey',
        'ALTER TABLE ONLY cementerio.difunto ADD CONSTRAINT difuntodatosfall_fkey FOREIGN KEY (id_datosfallecimiento) REFERENCES cementerio.datosfallecimiento(id);');



SELECT create_constraint_if_not_exists(
        'elemcementerio_fkey',
        'ALTER TABLE ONLY cementerio.bloque ADD CONSTRAINT elemcementerio_fkey FOREIGN KEY (id_elemcementerio) REFERENCES cementerio.elem_cementerio(id);');	
	
	
SELECT create_constraint_if_not_exists(
        'fk_cementerio',
        'ALTER TABLE ONLY cementerio.elem_cementerio ADD CONSTRAINT fk_cementerio FOREIGN KEY (id_cementerio) REFERENCES cementerio.cementerio(id);');	
	

SELECT create_constraint_if_not_exists(
        'fk_difunto1',
        'ALTER TABLE ONLY cementerio.inhumacion ADD CONSTRAINT fk_difunto1 FOREIGN KEY (id_difunto) REFERENCES cementerio.difunto(id);');	


SELECT create_constraint_if_not_exists(
        'fk_difunto2',
        'ALTER TABLE ONLY cementerio.exhumacion ADD CONSTRAINT fk_difunto2 FOREIGN KEY (id_difunto) REFERENCES cementerio.difunto(id);');	

SELECT create_constraint_if_not_exists(
        'localizacion_fk',
        'ALTER TABLE ONLY cementerio.unidadenterramiento ADD CONSTRAINT localizacion_fk FOREIGN KEY (id_localizacion) REFERENCES cementerio.localizacion(id);');	
		
SELECT create_constraint_if_not_exists(
        'persona_fk',
        'ALTER TABLE ONLY cementerio.reltitular ADD CONSTRAINT persona_fk FOREIGN KEY (dni_persona) REFERENCES cementerio.persona(dni);');


SELECT create_constraint_if_not_exists(
        'persona_fkey',
        'ALTER TABLE ONLY cementerio.difunto ADD CONSTRAINT persona_fkey FOREIGN KEY (dni_persona) REFERENCES cementerio.persona(dni);');

SELECT create_constraint_if_not_exists(
        'plaza_fkey',
        'ALTER TABLE ONLY cementerio.difunto ADD CONSTRAINT plaza_fkey FOREIGN KEY (id_plaza) REFERENCES cementerio.plaza(id);');

SELECT create_constraint_if_not_exists(
        'tarifa_fk1',
        'ALTER TABLE ONLY cementerio.inhumacion ADD CONSTRAINT tarifa_fk1 FOREIGN KEY (id_tarifa) REFERENCES cementerio.tarifa(id);');

SELECT create_constraint_if_not_exists(
        'tarifa_fk2',
        'ALTER TABLE ONLY cementerio.exhumacion ADD CONSTRAINT tarifa_fk2 FOREIGN KEY (id_tarifa) REFERENCES cementerio.tarifa(id);');

SELECT create_constraint_if_not_exists(
        'tarifa_fkey',
        'ALTER TABLE ONLY cementerio.concesion ADD CONSTRAINT tarifa_fkey FOREIGN KEY (id_tarifa) REFERENCES cementerio.tarifa(id);');



--
-- TOC entry 4449 (class 2606 OID 120050)
-- Dependencies: 4407 4046 4048
-- Name: unidadenterramiento_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: postgres
--



SELECT create_constraint_if_not_exists(
        'unidadenterramiento_fkey1',
        'ALTER TABLE ONLY cementerio.plaza ADD CONSTRAINT unidadenterramiento_fkey1 FOREIGN KEY (id_unidadenterramiento) REFERENCES cementerio.unidadenterramiento(id) ON UPDATE CASCADE ON DELETE CASCADE;');

--
-- TOC entry 4453 (class 2606 OID 120229)
-- Dependencies: 4407 4046 4051
-- Name: unidadenterramiento_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: postgres
--

SELECT create_constraint_if_not_exists(
        'unidadenterramiento_fkey',
        'ALTER TABLE ONLY cementerio.concesion ADD CONSTRAINT unidadenterramiento_fkey FOREIGN KEY (id_unidad) REFERENCES cementerio.unidadenterramiento(id);');



--
-- TOC entry 4464 (class 0 OID 0)
-- Dependencies: 4044
-- Name: bloque; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.bloque FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.bloque FROM geopista;
GRANT ALL ON TABLE cementerio.bloque TO geopista;
GRANT ALL ON TABLE cementerio.bloque TO postgres;


--
-- TOC entry 4465 (class 0 OID 0)
-- Dependencies: 4073
-- Name: cementerio; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.cementerio FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.cementerio FROM geopista;
GRANT ALL ON TABLE cementerio.cementerio TO geopista;
GRANT ALL ON TABLE cementerio.cementerio TO postgres;


--
-- TOC entry 4466 (class 0 OID 0)
-- Dependencies: 4055
-- Name: cementerio_feature; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.cementerio_feature FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.cementerio_feature FROM geopista;
GRANT ALL ON TABLE cementerio.cementerio_feature TO geopista;
GRANT ALL ON TABLE cementerio.cementerio_feature TO postgres;


--
-- TOC entry 4467 (class 0 OID 0)
-- Dependencies: 4050
-- Name: datosfallecimiento; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.datosfallecimiento FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.datosfallecimiento FROM geopista;
GRANT ALL ON TABLE cementerio.datosfallecimiento TO geopista;
GRANT ALL ON TABLE cementerio.datosfallecimiento TO postgres;


--
-- TOC entry 4468 (class 0 OID 0)
-- Dependencies: 4049
-- Name: difunto; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.difunto FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.difunto FROM geopista;
GRANT ALL ON TABLE cementerio.difunto TO geopista;
GRANT ALL ON TABLE cementerio.difunto TO postgres;


--
-- TOC entry 4469 (class 0 OID 0)
-- Dependencies: 4043
-- Name: elem_cementerio; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.elem_cementerio FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.elem_cementerio FROM geopista;
GRANT ALL ON TABLE cementerio.elem_cementerio TO geopista;
GRANT ALL ON TABLE cementerio.elem_cementerio TO postgres;


--
-- TOC entry 4470 (class 0 OID 0)
-- Dependencies: 4058
-- Name: elem_cementerio_id_seq; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON SEQUENCE cementerio.elem_cementerio_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE cementerio.elem_cementerio_id_seq FROM geopista;
GRANT ALL ON SEQUENCE cementerio.elem_cementerio_id_seq TO geopista;
GRANT ALL ON SEQUENCE cementerio.elem_cementerio_id_seq TO postgres;


--
-- TOC entry 4471 (class 0 OID 0)
-- Dependencies: 4083
-- Name: elem_feature; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.elem_feature FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.elem_feature FROM geopista;
GRANT ALL ON TABLE cementerio.elem_feature TO geopista;
GRANT ALL ON TABLE cementerio.elem_feature TO postgres;


--
-- TOC entry 4472 (class 0 OID 0)
-- Dependencies: 4087
-- Name: historico_difuntos; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.historico_difuntos FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.historico_difuntos FROM geopista;
GRANT ALL ON TABLE cementerio.historico_difuntos TO geopista;
GRANT ALL ON TABLE cementerio.historico_difuntos TO postgres;


--
-- TOC entry 4473 (class 0 OID 0)
-- Dependencies: 4089
-- Name: historico_propiedad; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.historico_propiedad FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.historico_propiedad FROM geopista;
GRANT ALL ON TABLE cementerio.historico_propiedad TO geopista;
GRANT ALL ON TABLE cementerio.historico_propiedad TO postgres;


--
-- TOC entry 4474 (class 0 OID 0)
-- Dependencies: 4047
-- Name: intervencion; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.intervencion FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.intervencion FROM geopista;
GRANT ALL ON TABLE cementerio.intervencion TO geopista;
GRANT ALL ON TABLE cementerio.intervencion TO postgres;


--
-- TOC entry 4475 (class 0 OID 0)
-- Dependencies: 4054
-- Name: localizacion; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.localizacion FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.localizacion FROM geopista;
GRANT ALL ON TABLE cementerio.localizacion TO geopista;
GRANT ALL ON TABLE cementerio.localizacion TO postgres;


--
-- TOC entry 4476 (class 0 OID 0)
-- Dependencies: 4059
-- Name: localizacion_id_seq; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON SEQUENCE cementerio.localizacion_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE cementerio.localizacion_id_seq FROM geopista;
GRANT ALL ON SEQUENCE cementerio.localizacion_id_seq TO geopista;
GRANT ALL ON SEQUENCE cementerio.localizacion_id_seq TO postgres;


--
-- TOC entry 4477 (class 0 OID 0)
-- Dependencies: 4045
-- Name: persona; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.persona FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.persona FROM geopista;
GRANT ALL ON TABLE cementerio.persona TO geopista;
GRANT ALL ON TABLE cementerio.persona TO postgres;


--
-- TOC entry 4478 (class 0 OID 0)
-- Dependencies: 4048
-- Name: plaza; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.plaza FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.plaza FROM geopista;
GRANT ALL ON TABLE cementerio.plaza TO geopista;
GRANT ALL ON TABLE cementerio.plaza TO postgres;


--
-- TOC entry 4479 (class 0 OID 0)
-- Dependencies: 4063
-- Name: reltitular; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.reltitular FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.reltitular FROM geopista;
GRANT ALL ON TABLE cementerio.reltitular TO geopista;
GRANT ALL ON TABLE cementerio.reltitular TO postgres;


--
-- TOC entry 4480 (class 0 OID 0)
-- Dependencies: 4052
-- Name: tarifa; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.tarifa FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.tarifa FROM geopista;
GRANT ALL ON TABLE cementerio.tarifa TO geopista;
GRANT ALL ON TABLE cementerio.tarifa TO postgres;


--
-- TOC entry 4481 (class 0 OID 0)
-- Dependencies: 4046
-- Name: unidadenterramiento; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON TABLE cementerio.unidadenterramiento FROM PUBLIC;
REVOKE ALL ON TABLE cementerio.unidadenterramiento FROM geopista;
GRANT ALL ON TABLE cementerio.unidadenterramiento TO geopista;
GRANT ALL ON TABLE cementerio.unidadenterramiento TO postgres;


--
-- TOC entry 4482 (class 0 OID 0)
-- Dependencies: 4060
-- Name: unidadenterramiento_id_seq; Type: ACL; Schema: cementerio; Owner: postgres
--

REVOKE ALL ON SEQUENCE cementerio.unidadenterramiento_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE cementerio.unidadenterramiento_id_seq FROM geopista;
GRANT ALL ON SEQUENCE cementerio.unidadenterramiento_id_seq TO geopista;
GRANT ALL ON SEQUENCE cementerio.unidadenterramiento_id_seq TO postgres;


-- Completed on 2011-07-28 10:22:01

--
-- PostgreSQL database dump complete
--

-- Fin EsquemaCementerio.sql -------------------------------------------------------------------------------------------------------


-- Inicio DominiosCementerio.sql -------------------------------------------------------------------------------------------------------

SET search_path = public, pg_catalog;

-- actualizamos las sequences
select setval('public.seq_domains', (select max(id) from public.domains)::bigint); 
select setval('public.seq_domainnodes', (select max(id) from public.domainnodes)::bigint); 


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM appgeopista WHERE def = 'Cementerios' ) THEN		
		-- Insertamos el acl y el permiso de Login para la aplicacion de Cementerio
		insert into appgeopista (appid, def) values (13, 'Cementerios');  --> El 13 es el siguiente identificador libre
		insert into acl (idacl, name) values (NEXTVAL('seq_acl'), 'Cementerios'); --> El 71 es un hueco
		insert into usrgrouperm (idperm, def) values (2000, 'Geopista.Cementerios.Login');  --> El 2000 es un hueco
		insert into r_acl_perm (idperm, idacl) values (2000, CURRVAL('seq_acl'));

		-- Insertamos en el rol del superuser el permiso de login
		insert into r_group_perm (groupid, idperm, idacl) values (110, 2000, CURRVAL('seq_acl'));
		insert into r_usr_perm (userid, idperm, idacl) values (100, 2000, CURRVAL('seq_acl'));

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de Cementerios');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de Cementerios');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de Cementerios');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de Cementerios');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de Cementerios');

		insert into domaincategory (id, id_description) values (NEXTVAL('seq_domaincategory'), CURRVAL('seq_dictionary')); --> El 11 es un hueco
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Gestión de cementerios', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de Elementos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de Elementos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de Elementos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de Elementos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de Elementos');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GE', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de la Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de la Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de la Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de la Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de la Propiedad');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GP', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Históricos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Históricos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Históricos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Históricos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Históricos');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'H', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-3);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de Difuntos');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GD', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-4);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de Intervenciones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de Intervenciones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de Intervenciones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de Intervenciones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de Intervenciones');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GI', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-5);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo de Elementos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo de Elementos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo de Elementos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo de Elementos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo de Elementos');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo de Elementos', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Bloque');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Bloque');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Bloque');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Bloque');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Bloque');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GE-1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'U.Enterramiento');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]U.Enterramiento');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]U.Enterramiento');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]U.Enterramiento');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]U.Enterramiento');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GE-2', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo G.Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo G.Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo G.Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo G.Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo G.Propiedad');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo G.Propiedad', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Titular');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Titular');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Titular');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Titular');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Titular');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GP-1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Concesiones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Concesiones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Concesiones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Concesiones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Concesiones');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GP-2', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tarifas');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tarifas');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tarifas');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tarifas');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tarifas');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GP-4', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-3);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Servicios');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Servicios');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Servicios');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Servicios');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Servicios');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Servicios', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Histórico Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Histórico Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Histórico Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Histórico Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Histórico Difuntos');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'H-1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Histórico Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Histórico Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Histórico Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Histórico Propiedad');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Histórico Propiedad');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'H-2', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Gestión Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Gestión Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Gestión Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Gestión Difuntos');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Gestión Difuntos');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Gestion Difuntos', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Defunción');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Defunción');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Defunción');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Defunción');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Defunción');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GD-1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Inhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Inhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Inhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Inhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Inhumación');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GD-2', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Exhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Exhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Exhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Exhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Exhumación');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GD-3', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-3);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tarifas');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tarifas');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tarifas');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tarifas');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tarifas');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GD-4', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-4);


		--*****************************************************************
		--TIPO INTERVENCION--
		--*****************************************************************
		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Intervenciones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Intervenciones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Intervenciones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Intervenciones');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Intervenciones');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Intervenciones', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Intervención');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Intervención');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Intervención');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Intervención');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Intervención');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), 'GI-1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);


		--*****************************************************************
		--TIPO UNIDADES ENTERRAMIENTO--
		--*****************************************************************
		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Unidad Enterramiento');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipus Unitat enterrament');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo de Unidade enterro');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Unitate mota ehorzketa');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]ipus Unitat enterrament');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Unidades', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel)values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Panteón');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Panteó');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Panteón');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Panteoia');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Panteó');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Mausoleo');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Mausoleu');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Mausoleo');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Mausoleoa');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Mausoleu');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Sepultura o fosa');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Sepultura o fossa');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Cava ou pit');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Ehorzketa edo hobia');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Sepultura o fossa');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-3);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Nicho');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]NÃ­nxol');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Nicho');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Nitxo');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]NÃ­nxol');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '4', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-4);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Columbario');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Columbari');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]ColumbÃ¡rio');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Kolunbarioak');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Columbari');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '5', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-5);

		--*****************************************************************
		--TIPO CONCESIONES--
		--*****************************************************************
		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Concesion');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipus Concessio');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo de Concesión');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Emakida-mota');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipus Concessio');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Concesiones', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Concesión renovable');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Concessió renovable');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Renovables Grant');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Berriztagarrien Grant');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Concessió renovable');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Concesión no renovable');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Concessió no renovable');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]concesión non renovables');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Ez-berriztagarrien beka');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Concessió no renovable');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Alquiler');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Lloguer');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Aparcamento');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Parking');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Lloguer');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-3);

		--*****************************************************************
		--TIPO CONTENEDORES--
		--*****************************************************************
		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Contenedor Difunto');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Contenedor Difunto');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Contenedor Difunto');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Contenedor Difunto');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Contenedor Difunto');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo contenedores difunto', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel)values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Ataúd');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Ataúd');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Ataúd');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Ataúd');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Ataúd');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Urna');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Urna');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Urna');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Urna');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Urna');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);


		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Caja Cremación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Caja Cremación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Caja Cremación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Caja Cremación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Caja Cremación');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-3);


		--*****************************************************************
		--TIPO INHUMACIONES--
		--*****************************************************************
		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Inhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Inhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Inhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Inhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Inhumación');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Inhumacion', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tierra');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tierra');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tierra');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tierra');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tierra');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Bóveda');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Bóveda');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Bóveda');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Bóveda');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Bóveda');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Cremación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Cremación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Cremación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Cremación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Cremación'); 
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-3);


		--*****************************************************************
		--TIPO EXHUMACIONES--
		--*****************************************************************
		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Exhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Exhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Exhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Exhumación');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Exhumación');
		insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Exhumación', CURRVAL('seq_acl'), CURRVAL('seq_domaincategory'));
		insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Ordinaria');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Ordinaria');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Ordinaria');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Ordinaria');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Ordinaria');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-1);

		insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Extraordinaria');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Extraordinaria');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Extraordinaria');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Extraordinaria');
		insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Extraordinaria');
		insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, currval('seq_domainnodes')-2);


		insert into query_catalog (id,query,acl,idperm) values ('getestructuraWithParent', 'select domains.id as id_domain ,domainnodes.id as id_node, domainnodes.pattern as pattern, dictionary.locale as locale, dictionary.id_vocablo as id_descripcion, dictionary.traduccion as traduccion, domainnodes.parentdomain as parentdomain from domains,domainnodes,dictionary where upper(domains.name)=upper(?) and domainnodes.type=? and domainnodes.id_domain=domains.id and domainnodes.id_description= dictionary.id_vocablo and domainnodes.id_municipio is null order by domainnodes.id', 1, 9205);

		-- Rellenar los tipos de los documentos
		insert into cementerio.documento_tipos (tipo, extension, mime_type) (select tipo, extension, mime_type from documento_tipos);

		-- Fin DominiosCementerio.sql

		-- Inicio capas_cementerios.sql
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO54";


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Capas Cementerios' ) THEN		
			
		------- ### ACL Cementerios ###
		insert into acl values (NEXTVAL('seq_acl'), 'Capas Cementerios');

		-- Se asocia el ACL con los permisos de leer, escribir, etc
		insert into r_acl_perm values (871, CURRVAL('seq_acl'));
		insert into r_acl_perm values (872, CURRVAL('seq_acl'));
		insert into r_acl_perm values (873, CURRVAL('seq_acl'));
		insert into r_acl_perm values (874, CURRVAL('seq_acl'));

		-- Se le dan permisos al superuser
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 871, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 872, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 873, CURRVAL('seq_acl'), 1); 
		insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 874, CURRVAL('seq_acl'), 1); 


		-------- ### MAPA DE CEMENTERIOS ####
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Mapa de Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Mapa de Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Mapa de Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Mapa de Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Mapa de Cementerios');
		INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa de Cementerios</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa de Cementerios</mapName></mapDescriptor>',0);
		
		
		-- Creacion de la capa
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Cementerios');
		-- PARA LOCALGIS DOS METER LA SIGUIENTE SENTENCIA
		--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),CURRVAL('seq_acl'),'cementerios',1,0,0);
		-- PARA LOCALGIS 2.1 METER LA SIGUIENTE SENTENCIA
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),CURRVAL('seq_acl'),'cementerios',1,0);

		-- Creacion de layerfamily 
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Capas de Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Capas de Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Capas de Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Capas de Cementerios');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Capas de Cementerios');
		INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

		-- Asociacion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),2);
		insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (5,currval('seq_layerfamilies'),1,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (CURRVAL('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:cementerios', true,0,0,true,true);

		-- Creacion en tablas de sistema
		INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'cementerios',11);

		-- Creacion de columna geometria
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);

		-- Creacion de columna id (con dominio autonumerico incremental - 10067)
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ID');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

		-- Creacion de columna nombre (con dominio )
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nombre',10014,CURRVAL('seq_tables'),100,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Nombre');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Nombre');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Nombre');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Nombre');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Nombre');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),99,0);

		-- Creacion de columna area
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'area',null,CURRVAL('seq_tables'),NULL,14,2,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','area');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]area');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]area');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]area');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]area');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);

		-- Creacion de columna longitud
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'length',null,CURRVAL('seq_tables'),NULL,14,2,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Longitud');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Longitud');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Longitud');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Longitud');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);

		-- Creacion de columna id_municipio
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10069,CURRVAL('seq_tables'),NULL,5,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ID de Municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

		-- Asociacion de queries. 
		-- PARA PGSQL 9 NO SIRVE LA FUNCION PERIMETER --> HAY QEU PONER PERIMETER2D
		--INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
		--	'SELECT transform("cementerios"."GEOMETRY", ?T) AS "GEOMETRY","cementerios"."id","cementerios"."nombre","cementerios"."area","cementerios"."length","cementerios"."id_municipio" FROM "cementerios" WHERE "cementerios"."id_municipio" IN (?M)',
		--	'UPDATE "cementerios" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T), "nombre"=?3, "area"=AREA2D(GeometryFromText(text(?1),?S)), "length"=PERIMETER(GeometryFromText(text(?1),?S)), "id_municipio"=?M WHERE "id"=?2; UPDATE "cementerio"."cementerio" SET "nombre"=?3 WHERE "id"=?2;',
		--	'INSERT INTO "cementerios" ("GEOMETRY","id","nombre","area","length","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK, ?3, AREA2D(GeometryFromText(text(?1),?S)),PERIMETER(GeometryFromText(text(?1),?S)),?M); INSERT INTO "cementerio"."cementerio" ("id", "nombre") (select id, nombre from public.cementerios where nombre=?3);',
		--	'DELETE FROM "cementerios" WHERE "id"=?2; DELETE FROM "cementerio"."cementerio" WHERE "id"=?2;');

		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
			'SELECT transform("cementerios"."GEOMETRY", ?T) AS "GEOMETRY","cementerios"."id","cementerios"."nombre","cementerios"."area","cementerios"."length","cementerios"."id_municipio" FROM "cementerios" WHERE "cementerios"."id_municipio" IN (?M)',
			'UPDATE "cementerios" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T), "nombre"=?3, "area"=AREA2D(GeometryFromText(text(?1),?S)), "length"=PERIMETER2D(GeometryFromText(text(?1),?S)), "id_municipio"=?M WHERE "id"=?2; UPDATE "cementerio"."cementerio" SET "nombre"=?3 WHERE "id"=?2;',
			'INSERT INTO "cementerios" ("GEOMETRY","id","nombre","area","length","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK, ?3, AREA2D(GeometryFromText(text(?1),?S)),PERIMETER2D(GeometryFromText(text(?1),?S)),?M); INSERT INTO "cementerio"."cementerio" ("id", "nombre") (select id, nombre from public.cementerios where nombre=?3);',
			'DELETE FROM "cementerios" WHERE "id"=?2; DELETE FROM "cementerio"."cementerio" WHERE "id"=?2;');
			
		
		-- Estilos por defecto
		insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?><StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>cementerios</Name><UserStyle><Name>default:cementerios</Name><Title>default:cementerios</Title><Abstract>default:cementerios</Abstract><FeatureTypeStyle><Name>cementerios</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>0.5</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
	
		IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'CEMENTERIOS') THEN
		  ------- ### CAPA CEMENTERIOS ###
			-- Creacion de la tabla
			CREATE TABLE cementerio.CEMENTERIOS(
					ID NUMERIC(8),
					"GEOMETRY" GEOMETRY,
					NOMBRE VARCHAR(100),
					AREA NUMERIC(14,2),
					LENGTH NUMERIC(14,2),
					ID_MUNICIPIO NUMERIC(5),
					CONSTRAINT CEMENTERIOS_PK PRIMARY KEY (id)
					)WITH OIDS;
			ALTER TABLE cementerio.CEMENTERIOS OWNER TO geopista;
			GRANT ALL ON TABLE cementerio.CEMENTERIOS TO geopista;
			GRANT SELECT ON TABLE cementerio.CEMENTERIOS TO consultas;
			CREATE INDEX CEMENTERIOS_SPAT_IDX on cementerio.CEMENTERIOS using gist("GEOMETRY");
		END IF;
				-- Creacion de la secuencia
		CREATE SEQUENCE cementerio.SEQ_CEMENTERIOS
		  INCREMENT 1
		  MINVALUE 1
		  MAXVALUE 9223372036854775807
		  START 1
		  CACHE 1;
		ALTER TABLE SEQ_CEMENTERIOS OWNER TO geopista;
		
		IF NOT EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'cementerio' AND sequence_name = 'UNIDAD_ENTERRAMIENTO') THEN
		------- ### CAPA UNIDADES DE ENTERRAMIENTO ###
		-- Creacion de la tabla
		CREATE TABLE cementerio.UNIDAD_ENTERRAMIENTO(
				ID NUMERIC(8),
				"GEOMETRY" GEOMETRY,
				VOLUMEN NUMERIC(5,2),
				AREA NUMERIC(14,2),
				TIPO VARCHAR(5),
				ID_MUNICIPIO NUMERIC(5),
				CONSTRAINT UNIDAD_ENTERRAMIENTO_PK PRIMARY KEY (id)
				)WITH OIDS;
		ALTER TABLE cementerio.UNIDAD_ENTERRAMIENTO OWNER TO geopista;
		GRANT ALL ON TABLE cementerio.UNIDAD_ENTERRAMIENTO TO geopista;
		GRANT SELECT ON TABLE cementerio.UNIDAD_ENTERRAMIENTO TO consultas;
		CREATE INDEX UNIDAD_ENTERRAMIENTO_SPAT_IDX on cementerio.UNIDAD_ENTERRAMIENTO using gist("GEOMETRY");


		-- Estilos por defecto
		insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>unidad_enterramiento</Name><UserStyle><Name>default:unidad_enterramiento</Name><Title>default:unidad_enterramiento</Title><Abstract>unidad_enterramiento:_:default:unidad_enterramiento</Abstract><FeatureTypeStyle><Name>unidad_enterramiento</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill>
		<CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffd4</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>tipo_unidad_enterramiento 0</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Tipo</ogc:PropertyName><ogc:Literal>1</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffff99</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>tipo_unidad_enterramiento 1</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Tipo</ogc:PropertyName><ogc:Literal>2</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#e5b410</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>tipo_unidad_enterramiento 2</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Tipo</ogc:PropertyName><ogc:Literal>3</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ccffcc</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>tipo_unidad_enterramiento 3</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Tipo</ogc:PropertyName><ogc:Literal>4</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#cccccc</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>tipo_unidad_enterramiento 4</Name><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Tipo</ogc:PropertyName><ogc:Literal>5</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#6666ff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>
		');

		-- Creacion de la capa
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Unidad de Enterramiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Unidad de Enterramiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Unidad de Enterramiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Unidad de Enterramiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Unidad de Enterramiento');
		-- PARA LOCALGIS DOS METER LA SIGUIENTE SENTENCIA
		--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),CURRVAL('seq_acl'),'unidad_enterramiento',1,0,0);
		-- PARA LOCALGIS 2.1 METER LA SIGUIENTE SENTENCIA
		INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),CURRVAL('seq_acl'),'unidad_enterramiento',1,0);


		-- Asociacion de layer-layerfamily
		insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),1);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (CURRVAL('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:unidad_enterramiento', true,1,0,true,true);

		-- Creacion en tablas de sistema
		INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'unidad_enterramiento',11);

		-- Creacion de columna geometria
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);

		-- Creacion de columna id (con dominio autonumerico incremental - 10067)
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ID');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ID');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

		-- Creacion de columna tipo (con dominio basico 5 caracteres no obligatorio)
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo',10003,CURRVAL('seq_tables'),5,NULL,NULL,3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
		-- Antes deben haberse creado los dominios de cementerio
		--INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),22006,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) (select CURRVAL('seq_columns'), id, 0 from domains where name = 'Tipo Unidades' and idacl = CURRVAL('seq_acl') and id_category=11);

		-- Creacion de columna area
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'area',null,CURRVAL('seq_tables'),NULL,14,2,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','area');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]area');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]area');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]area');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]area');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);

		-- Creacion de columna volumen
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'volumen',null,CURRVAL('seq_tables'),NULL,5,2,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Volumen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Volumen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Volumen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Volumen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Volumen');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);

		-- Creacion de columna id_municipio
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10069,CURRVAL('seq_tables'),NULL,5,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]ID de Municipio');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]ID de Municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);

		-- Asociacion de queries. 
		INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
			'SELECT transform("unidad_enterramiento"."GEOMETRY", ?T) AS "GEOMETRY","unidad_enterramiento"."id", "unidad_enterramiento"."tipo", "unidad_enterramiento"."area", "unidad_enterramiento"."volumen", "unidad_enterramiento"."id_municipio" FROM "unidad_enterramiento" WHERE "unidad_enterramiento"."id_municipio" IN (?M)',
			'UPDATE "unidad_enterramiento" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T), "tipo"=?3, "area"=AREA2D(GeometryFromText(text(?1),?S)), "volumen"=?5, "id_municipio"=?M WHERE "id"=?2',
			'INSERT INTO "unidad_enterramiento" ("GEOMETRY","id","tipo","area","volumen","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK, ?3, AREA2D(GeometryFromText(text(?1),?S)), ?5, ?M)',
			'DELETE FROM "unidad_enterramiento" WHERE "id"=?2');

		-- Creacion de la secuencia
		CREATE SEQUENCE cementerio.SEQ_UNIDAD_ENTERRAMIENTO
		  INCREMENT 1
		  MINVALUE 1
		  MAXVALUE 9223372036854775807
		  START 1
		  CACHE 1;
		ALTER TABLE cementerio.SEQ_UNIDAD_ENTERRAMIENTO OWNER TO geopista;


		-- Asociacion capa de Municipio al Mapa de Cementerios
		insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (CURRVAL('seq_maps'),3,2,0);
		insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (CURRVAL('seq_maps'),3,3,13,'municipios:_:default:municipios', true,2,0,true,true);
	END IF;
	
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO55";

-- Fin capas_cementerios.sql

