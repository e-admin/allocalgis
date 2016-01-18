-- Actualizacion de dominios para espacio publico
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Espacio Publico');
INSERT INTO DOMAINCATEGORY(ID,ID_DESCRIPTION) VALUES (NEXTVAL('seq_domaincategory'),CURRVAL('seq_dictionary'));
INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Tipo de Actuacion',CURRVAL('seq_domaincategory'));
-- Medio Ambiente
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Limpieza viaria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Limpieza viaria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Limpieza viaria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Limpieza viaria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Limpieza viaria');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'AE1',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Recogida de residuos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Recogida de residuos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Recogida de residuos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Recogida de residuos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Recogida de residuos');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'AE2',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Mantenimiento de Parques y jardines');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Mantenimiento de Parques y jardines');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Mantenimiento de Parques y jardines');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Mantenimiento de Parques y jardines');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Mantenimiento de Parques y jardines');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'AE3',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Ciclo del Agua');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Ciclo del Agua');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Ciclo del Agua');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Ciclo del Agua');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Ciclo del Agua');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'AE4',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Deficiencias higienico sanitarias');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Deficiencias higienico sanitarias');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Deficiencias higienico sanitarias');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Deficiencias higienico sanitarias');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Deficiencias higienico sanitarias');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'AE5',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Medio Ambiente');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Medio Ambiente');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Medio Ambiente');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Medio Ambiente');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Medio Ambiente');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'AE',CURRVAL('seq_dictionary'),7);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('AE1','AE2','AE3','AE4','AE5');
-- Mantenimiento de la via publica
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Reparaciones en via publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Reparaciones en via publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Reparaciones en via publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Reparaciones en via publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Reparaciones en via publica');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'AV1',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Reparaciones en alumbrado publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Reparaciones en alumbrado publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Reparaciones en alumbrado publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Reparaciones en alumbrado publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Reparaciones en alumbrado publico');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'AV2',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Mantenimiento de la via publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Mantenimiento de la via publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Mantenimiento de la via publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Mantenimiento de la via publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Mantenimiento de la via publica');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'AV',CURRVAL('seq_dictionary'),7);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('AV1','AV2','AV3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Actuaciones e intervenciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Actuaciones e intervenciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Actuaciones e intervenciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Actuaciones e intervenciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Actuaciones e intervenciones');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),CURRVAL('seq_dictionary'),2);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('AV','AE');
--CREACION DE MAPS
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Gestion de Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Gestion de Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Gestion de Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Gestion de Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Gestion de Espacio Publico');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(150,CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Gestion de espacio publico</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Gestion de espacio publico</mapName></mapDescriptor>',0);
--CREACION DE LAYERFAMILIES
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Capas de Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Capas de Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Capas de Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Capas de Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Capas de Espacio Publico');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

-- GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO 
-- Creacion de tabla
CREATE TABLE GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO(
		ID NUMERIC(8),
		"GEOMETRY" GEOMETRY,
		NOMBRE VARCHAR(50),
		DESCRIPCION VARCHAR(200),
		TIPO VARCHAR(3),
		AREA NUMERIC(14,2),
		LENGTH NUMERIC(14,2),
		ID_MUNICIPIO NUMERIC(5),
		CONSTRAINT GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO_PK PRIMARY KEY (id)
		)WITH OIDS;
ALTER TABLE GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO OWNER TO geopista;
GRANT ALL ON TABLE GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO TO geopista;
GRANT SELECT ON TABLE GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO TO consultas;
CREATE INDEX GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO_SPAT_IDX on GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO using gist("GEOMETRY");
--CREACION DE LAYERS
-- Creacion de capa - Espacio Publico
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Espacio Publico - Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Espacio Publico - Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Espacio Publico - Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Espacio Publico - Espacio Publico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Espacio Publico - Espacio Publico');
--Insercion de estilos - por defecto
insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?><StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>gestion_espacio_publico_espacio_publico</Name><UserStyle><Name>default:gestion_espacio_publico_espacio_publico</Name><Title>default:gestion_espacio_publico_espacio_publico</Title><Abstract>default:gestion_espacio_publico_espacio_publico</Abstract><FeatureTypeStyle><Name>gestion_espacio_publico_espacio_publico</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_espacio_publico',1,0,0);
--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_espacio_publico',1);
-- insercion de layer-layerfamily
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),1);
insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (150,currval('seq_layerfamilies'),1,0);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (150,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:gestion_espacio_publico_espacio_publico', true,0,0,true,true);
-- Insercion en tablas de sistema
INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'gestion_espacio_publico_espacio_publico',11);
-- Insercion de columna geometria
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);

-- Insercion de columna id
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

-- Insercion de columna nombre
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nombre',10007,CURRVAL('seq_tables'),50,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Nombre');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10007,0);

-- Insercion de columna descripcion
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'descripcion',10036,CURRVAL('seq_tables'),200,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Descripcion');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);

-- Insercion de columna tipo de via
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo',10066,CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Via');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Via');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Via');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Via');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Via');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10066,0);

-- Insercion de columna area
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'area',null,CURRVAL('seq_tables'),NULL,14,2,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]area');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);

-- Insercion de columna longitud
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'length',null,CURRVAL('seq_tables'),NULL,14,2,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);

-- Insercion de columna id_municipio
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10068,CURRVAL('seq_tables'),NULL,5,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id de Municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);

-- Insercion de la tabla queries. Ojo. comprobar que el seq_querys esta bien generado - cambiar las select
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_querys'),CURRVAL('seq_layers'),1,
	'SELECT transform("gestion_espacio_publico_espacio_publico"."GEOMETRY", ?T) AS "GEOMETRY","gestion_espacio_publico_espacio_publico"."id","gestion_espacio_publico_espacio_publico"."nombre","gestion_espacio_publico_espacio_publico"."descripcion","gestion_espacio_publico_espacio_publico"."tipo","gestion_espacio_publico_espacio_publico"."area","gestion_espacio_publico_espacio_publico"."length","gestion_espacio_publico_espacio_publico"."id_municipio" FROM "gestion_espacio_publico_espacio_publico" WHERE "gestion_espacio_publico_espacio_publico"."id_municipio" IN (?M)',
	'UPDATE "gestion_espacio_publico_espacio_publico" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"nombre"=?3,"descripcion"=?4,"tipo"=?5,"area"=?6,"length"=?7,"id_municipio" in (?M) WHERE "id"=?2',
	'INSERT INTO "gestion_espacio_publico_espacio_publico" ("GEOMETRY","id","nombre","descripcion","tipo","area","length","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,?6,?7,?M)',
	'DELETE FROM "gestion_espacio_publico_espacio_publico" WHERE "id"=?2');
-- Creacion de la secuencia
CREATE SEQUENCE SEQ_GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE SEQ_GESTION_ESPACIO_PUBLICO_ESPACIO_PUBLICO OWNER TO geopista;
-- GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS
CREATE TABLE GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS(
		ID NUMERIC(8),
		"GEOMETRY" GEOMETRY,
		NOMBRE VARCHAR(50),
		DESCRIPCION VARCHAR(200),
		TIPO VARCHAR(3),
		AREA NUMERIC(14,2),
		LENGTH NUMERIC(14,2),
		ID_MUNICIPIO NUMERIC(5),
		CONSTRAINT GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS_PK PRIMARY KEY (id)
		)WITH OIDS;
ALTER TABLE GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS OWNER TO geopista;
GRANT ALL ON TABLE GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS TO geopista;
GRANT SELECT ON TABLE GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS TO consultas;
CREATE INDEX GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS_SPAT_IDX on GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS using gist("GEOMETRY");
--CREACION DE LAYERS
-- Creacion de capa - Espacio Publico
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Espacio Publico - Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Espacio Publico - Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Espacio Publico - Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Espacio Publico - Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Espacio Publico - Equipamientos');
--Insercion de estilos - por defecto
insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?><StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>gestion_espacio_publico_equipamientos</Name><UserStyle><Name>default:gestion_espacio_publico_equipamientos</Name><Title>default:gestion_espacio_publico_equipamientos</Title><Abstract>default:gestion_espacio_publico_equipamientos</Abstract><FeatureTypeStyle><Name>gestion_espacio_publico_equipamientos</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_equipamientos',1,0,0);
--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_equipamientos',1);
-- insercion de layer-lf
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),2);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (150,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:gestion_espacio_publico_equipamientos', true,1,0,true,true);
-- Insercion en tablas de sistema
INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'gestion_espacio_publico_equipamientos',11);
-- Insercion de columna geometria

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);


-- Insercion de columna id
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

-- Insercion de columna nombre
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nombre',10007,CURRVAL('seq_tables'),50,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Nombre');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10007,0);

-- Insercion de columna descripcion
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'descripcion',10036,CURRVAL('seq_tables'),200,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Descripcion');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);

-- Insercion de columna tipos de equipamientos

INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Tipos de Equipamientos',CURRVAL('seq_domaincategory'));
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de equipamiento 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de equipamiento 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de equipamiento 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de equipamiento 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de equipamiento 1');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'T1',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de equipamiento 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de equipamiento 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de equipamiento 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de equipamiento 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de equipamiento 2');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'T2',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de equipamiento 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de equipamiento 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de equipamiento 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de equipamiento 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de equipamiento 3');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'T3',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Equipamientos');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),CURRVAL('seq_dictionary'),4);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('T1','T2','T3');

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo',CURRVAL('seq_domains'),CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Equipamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Equipamientos');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),CURRVAL('seq_domains'),0);

-- Insercion de columna area
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'area',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]area');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);

-- Insercion de columna longitud
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'length',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);

-- Insercion de columna id_municipio
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10068,CURRVAL('seq_tables'),NULL,5,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id de Municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);

-- Insercion de la tabla queries. Ojo. comprobar que el seq_querys esta bien generado - cambiar las select
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_querys'),CURRVAL('seq_layers'),1,
	'SELECT transform("gestion_espacio_publico_equipamientos"."GEOMETRY", ?T) AS "GEOMETRY","gestion_espacio_publico_equipamientos"."id","gestion_espacio_publico_equipamientos"."nombre","gestion_espacio_publico_equipamientos"."descripcion","gestion_espacio_publico_equipamientos"."tipo","gestion_espacio_publico_equipamientos"."area","gestion_espacio_publico_equipamientos"."length","gestion_espacio_publico_equipamientos"."id_municipio" FROM "gestion_espacio_publico_equipamientos" WHERE "gestion_espacio_publico_equipamientos"."id_municipio" IN (?M)',
	'UPDATE "gestion_espacio_publico_equipamientos" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"nombre"=?3,"descripcion"=?4,"tipo"=?5,"area"=AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),"length"=PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),"id_municipio" in (?M) WHERE "id"=?2',
	'INSERT INTO "gestion_espacio_publico_equipamientos" ("GEOMETRY","id","nombre","descripcion","tipo","area","length","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),?M)',
	'DELETE FROM "gestion_espacio_publico_equipamientos" WHERE "id"=?2');
-- Creacion de la secuencia
CREATE SEQUENCE SEQ_GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE SEQ_GESTION_ESPACIO_PUBLICO_EQUIPAMIENTOS OWNER TO geopista;
-- GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO
CREATE TABLE GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO(
		ID NUMERIC(8),
		"GEOMETRY" GEOMETRY,
		NOMBRE VARCHAR(50),
		DESCRIPCION VARCHAR(200),
		TIPO VARCHAR(3),
		AREA NUMERIC(14,2),
		LENGTH NUMERIC(14,2),
		ID_MUNICIPIO NUMERIC(5),
		CONSTRAINT GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO_PK PRIMARY KEY (id)
		)WITH OIDS;
ALTER TABLE GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO OWNER TO geopista;
GRANT ALL ON TABLE GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO TO geopista;
GRANT SELECT ON TABLE GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO TO consultas;
CREATE INDEX GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO_SPAT_IDX on GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO using gist("GEOMETRY");
--CREACION DE LAYERS
-- Creacion de capa - Espacio Publico
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Espacio Publico - Redes de servicio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Espacio Publico - Redes de servicio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Espacio Publico - Redes de servicio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Espacio Publico - Redes de servicio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Espacio Publico - Redes de servicio');
--Insercion de estilos - por defecto
insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?><StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>gestion_espacio_publico_redes_de_servicio</Name><UserStyle><Name>default:gestion_espacio_publico_redes_de_servicio</Name><Title>default:gestion_espacio_publico_redes_de_servicio</Title><Abstract>default:gestion_espacio_publico_redes_de_servicio</Abstract><FeatureTypeStyle><Name>gestion_espacio_publico_redes_de_servicio</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_redes_de_servicio',1,0,0);
--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_redes_de_servicio',1);
-- insercion de layer-lf
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),3);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (150,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:gestion_espacio_publico_redes_de_servicio', true,2,0,true,true);
-- Insercion en tablas de sistema
INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'gestion_espacio_publico_redes_de_servicio',11);
-- Insercion de columna geometria

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);


-- Insercion de columna id
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

-- Insercion de columna nombre
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nombre',10007,CURRVAL('seq_tables'),50,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Nombre');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10007,0);

-- Insercion de columna descripcion
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'descripcion',10036,CURRVAL('seq_tables'),200,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Descripcion');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);

-- Insercion de columna tipos de servicios

INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Tipos de Servicios',CURRVAL('seq_domaincategory'));
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Servicio 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Servicio 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Servicio 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Servicio 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Servicio 1');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'S1',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Servicio 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Servicio 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Servicio 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Servicio 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Servicio 2');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'S2',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Servicio 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Servicio 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Servicio 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Servicio 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Servicio 3');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'S3',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Servicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Servicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Servicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Servicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Servicios');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),CURRVAL('seq_dictionary'),4);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('S1','S2','S3');

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo',CURRVAL('seq_domains'),CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Servicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Servicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Servicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Servicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Servicios');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),CURRVAL('seq_domains'),0);

-- Insercion de columna area
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'area',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]area');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);

-- Insercion de columna longitud
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'length',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);

-- Insercion de columna id_municipio
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10068,CURRVAL('seq_tables'),NULL,5,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id de Municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);

-- Insercion de la tabla queries. Ojo. comprobar que el seq_querys esta bien generado - cambiar las select
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_querys'),CURRVAL('seq_layers'),1,
	'SELECT transform("gestion_espacio_publico_redes_de_servicio"."GEOMETRY", ?T) AS "GEOMETRY","gestion_espacio_publico_redes_de_servicio"."id","gestion_espacio_publico_redes_de_servicio"."nombre","gestion_espacio_publico_redes_de_servicio"."descripcion","gestion_espacio_publico_redes_de_servicio"."tipo","gestion_espacio_publico_redes_de_servicio"."area","gestion_espacio_publico_redes_de_servicio"."length","gestion_espacio_publico_redes_de_servicio"."id_municipio" FROM "gestion_espacio_publico_redes_de_servicio" WHERE "gestion_espacio_publico_redes_de_servicio"."id_municipio" IN (?M)',
	'UPDATE "gestion_espacio_publico_redes_de_servicio" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"nombre"=?3,"descripcion"=?4,"tipo"=?5,"area"=AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),"length"=PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),"id_municipio" in (?M) WHERE "id"=?2',
	'INSERT INTO "gestion_espacio_publico_redes_de_servicio" ("GEOMETRY","id","nombre","descripcion","tipo","area","length","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),?M)',
	'DELETE FROM "gestion_espacio_publico_redes_de_servicio" WHERE "id"=?2');
-- Creacion de la secuencia
CREATE SEQUENCE SEQ_GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE SEQ_GESTION_ESPACIO_PUBLICO_REDES_DE_SERVICIO OWNER TO geopista;
-- GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS
CREATE TABLE GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS(
		ID NUMERIC(8),
		"GEOMETRY" GEOMETRY,		
		NOMBRE VARCHAR(50),
		DESCRIPCION VARCHAR(200),
		TIPO VARCHAR(3),
		AREA NUMERIC(14,2),
		LENGTH NUMERIC(14,2),
		ID_MUNICIPIO NUMERIC(5),
		CONSTRAINT GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS_PK PRIMARY KEY (id)
		)WITH OIDS;
ALTER TABLE GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS OWNER TO geopista;
GRANT ALL ON TABLE GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS TO geopista;
GRANT SELECT ON TABLE GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS TO consultas;
CREATE INDEX GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS_SPAT_IDX on GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS using gist("GEOMETRY");
--CREACION DE LAYERS
-- Creacion de capa - Espacio Publico
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Espacio Publico - Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Espacio Publico - Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Espacio Publico - Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Espacio Publico - Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Espacio Publico - Infraestructuras');
--Insercion de estilos - por defecto
insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?><StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>gestion_espacio_publico_infraestructuras</Name><UserStyle><Name>default:gestion_espacio_publico_infraestructuras</Name><Title>default:gestion_espacio_publico_infraestructuras</Title><Abstract>default:gestion_espacio_publico_infraestructuras</Abstract><FeatureTypeStyle><Name>gestion_espacio_publico_infraestructuras</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_infraestructuras',1,0,0);
--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_infraestructuras',1);
-- insercion de layer-lf
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),4);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (150,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:gestion_espacio_publico_infraestructuras', true,3,0,true,true);
-- Insercion en tablas de sistema
INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'gestion_espacio_publico_infraestructuras',11);
-- Insercion de columna geometria

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);


-- Insercion de columna id
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

-- Insercion de columna nombre
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nombre',10007,CURRVAL('seq_tables'),50,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Nombre');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10007,0);

-- Insercion de columna descripcion
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'descripcion',10036,CURRVAL('seq_tables'),200,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Descripcion');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);

-- Insercion de columna tipos de infraestructuras

INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Tipos de Infraestructuras',CURRVAL('seq_domaincategory'));
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Infraestructura 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Infraestructura 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Infraestructura 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Infraestructura 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Infraestructura 1');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'I1',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Infraestructura 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Infraestructura 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Infraestructura 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Infraestructura 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Infraestructura 2');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'I2',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Infraestructura 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Infraestructura 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Infraestructura 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Infraestructura 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Infraestructura 3');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'I3',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Infraestructuras');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),CURRVAL('seq_dictionary'),4);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('I1','I2','I3');

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo',CURRVAL('seq_domains'),CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Infraestructuras');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),CURRVAL('seq_domains'),0);

-- Insercion de columna area
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'area',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]area');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);

-- Insercion de columna longitud
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'length',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);

-- Insercion de columna id_municipio
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10068,CURRVAL('seq_tables'),NULL,5,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id de Municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);

-- Insercion de la tabla queries. Ojo. comprobar que el seq_querys esta bien generado - cambiar las select
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_querys'),CURRVAL('seq_layers'),1,
	'SELECT transform("gestion_espacio_publico_infraestructuras"."GEOMETRY", ?T) AS "GEOMETRY","gestion_espacio_publico_infraestructuras"."id","gestion_espacio_publico_infraestructuras"."nombre","gestion_espacio_publico_infraestructuras"."descripcion","gestion_espacio_publico_infraestructuras"."tipo","gestion_espacio_publico_infraestructuras"."area","gestion_espacio_publico_infraestructuras"."length","gestion_espacio_publico_infraestructuras"."id_municipio" FROM "gestion_espacio_publico_infraestructuras" WHERE "gestion_espacio_publico_infraestructuras"."id_municipio" IN (?M)',
	'UPDATE "gestion_espacio_publico_infraestructuras" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"nombre"=?3,"descripcion"=?4,"tipo"=?5,"area"=AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),"length"=PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),"id_municipio" in (?M) WHERE "id"=?2',
	'INSERT INTO "gestion_espacio_publico_infraestructuras" ("GEOMETRY","id","nombre","descripcion","tipo","area","length","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),?M)',
	'DELETE FROM "gestion_espacio_publico_infraestructuras" WHERE "id"=?2');
-- Creacion de la secuencia
CREATE SEQUENCE SEQ_GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE SEQ_GESTION_ESPACIO_PUBLICO_INFRAESTRUCTURAS OWNER TO geopista;
-- GESTION_ESPACIO_PUBLICO_ZONAS_VERDES
CREATE TABLE GESTION_ESPACIO_PUBLICO_ZONAS_VERDES(
		ID NUMERIC(8),
		"GEOMETRY" GEOMETRY,
		NOMBRE VARCHAR(50),
		DESCRIPCION VARCHAR(200),
		TIPO VARCHAR(3),
		AREA NUMERIC(14,2),
		LENGTH NUMERIC(14,2),
		ID_MUNICIPIO NUMERIC(5),
		CONSTRAINT GESTION_ESPACIO_PUBLICO_ZONAS_VERDES_PK PRIMARY KEY (id)
		)WITH OIDS;
ALTER TABLE GESTION_ESPACIO_PUBLICO_ZONAS_VERDES OWNER TO geopista;
GRANT ALL ON TABLE GESTION_ESPACIO_PUBLICO_ZONAS_VERDES TO geopista;
GRANT SELECT ON TABLE GESTION_ESPACIO_PUBLICO_ZONAS_VERDES TO consultas;
CREATE INDEX GESTION_ESPACIO_PUBLICO_ZONAS_VERDES_SPAT_IDX on GESTION_ESPACIO_PUBLICO_ZONAS_VERDES using gist("GEOMETRY");
--CREACION DE LAYERS
-- Creacion de capa - Espacio Publico
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Espacio Publico - Zonas verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Espacio Publico - Zonas verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Espacio Publico - Zonas verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Espacio Publico - Zonas verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Espacio Publico - Zonas verdes');
--Insercion de estilos - por defecto
insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?><StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>gestion_espacio_publico_zonas_verdes</Name><UserStyle><Name>default:gestion_espacio_publico_zonas_verdes</Name><Title>default:gestion_espacio_publico_zonas_verdes</Title><Abstract>default:gestion_espacio_publico_zonas_verdes</Abstract><FeatureTypeStyle><Name>gestion_espacio_publico_zonas_verdes</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_zonas_verdes',1,0,0);
--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_zonas_verdes',1);
-- insercion de layer-lf
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),5);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (150,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:gestion_espacio_publico_zonas_verdes', true,4,0,true,true);
-- Insercion en tablas de sistema
INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'gestion_espacio_publico_zonas_verdes',11);
-- Insercion de columna geometria

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);


-- Insercion de columna id
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

-- Insercion de columna nombre
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nombre',10007,CURRVAL('seq_tables'),50,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Nombre');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10007,0);

-- Insercion de columna descripcion
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'descripcion',10036,CURRVAL('seq_tables'),200,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Descripcion');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);

-- Insercion de columna tipos de infraestructuras

INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Tipos de Zonas Verdes',CURRVAL('seq_domaincategory'));
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Arbolado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Arbolado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Arbolado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Arbolado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Arbolado');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'Z1',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Alcorques');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Alcorques');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Alcorques');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Alcorques');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Alcorques');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'Z2',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Parques');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Parques');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Parques');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Parques');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Parques');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'Z3',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Infraestructuras');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Zonas Verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Zonas Verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Zonas Verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Zonas Verdes');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),CURRVAL('seq_dictionary'),4);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('Z1','Z2','Z3');

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo',CURRVAL('seq_domains'),CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Zonas Verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Zonas Verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Zonas Verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Zonas Verdes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Zonas Verdes');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),CURRVAL('seq_domains'),0);

-- Insercion de columna area
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'area',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]area');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);

-- Insercion de columna longitud
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'length',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);

-- Insercion de columna id_municipio
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10068,CURRVAL('seq_tables'),NULL,5,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id de Municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);

-- Insercion de la tabla queries. Ojo. comprobar que el seq_querys esta bien generado - cambiar las select
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_querys'),CURRVAL('seq_layers'),1,
	'SELECT transform("gestion_espacio_publico_zonas_verdes"."GEOMETRY", ?T) AS "GEOMETRY","gestion_espacio_publico_zonas_verdes"."id","gestion_espacio_publico_zonas_verdes"."nombre","gestion_espacio_publico_zonas_verdes"."descripcion","gestion_espacio_publico_zonas_verdes"."tipo","gestion_espacio_publico_zonas_verdes"."area","gestion_espacio_publico_zonas_verdes"."length","gestion_espacio_publico_zonas_verdes"."id_municipio" FROM "gestion_espacio_publico_zonas_verdes" WHERE "gestion_espacio_publico_zonas_verdes"."id_municipio" IN (?M)',
	'UPDATE "gestion_espacio_publico_zonas_verdes" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"nombre"=?3,"descripcion"=?4,"tipo"=?5,"area"=AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),"length"=PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),"id_municipio" in (?M) WHERE "id"=?2',
	'INSERT INTO "gestion_espacio_publico_zonas_verdes" ("GEOMETRY","id","nombre","descripcion","tipo","area","length","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),?M)',
	'DELETE FROM "gestion_espacio_publico_zonas_verdes" WHERE "id"=?2');
-- Creacion de la secuencia
CREATE SEQUENCE SEQ_GESTION_ESPACIO_PUBLICO_ZONAS_VERDES
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE SEQ_GESTION_ESPACIO_PUBLICO_ZONAS_VERDES OWNER TO geopista;

-- GESTION_ESPACIO_PUBLICO_APARCAMIENTOS
CREATE TABLE GESTION_ESPACIO_PUBLICO_APARCAMIENTOS(
		ID NUMERIC(8),
		"GEOMETRY" GEOMETRY,
		NOMBRE VARCHAR(50),
		DESCRIPCION VARCHAR(200),
		TIPO VARCHAR(3),
		CLASIFICACION VARCHAR(3),
		AREA NUMERIC(14,2),
		LENGTH NUMERIC(14,2),
		ID_MUNICIPIO NUMERIC(5),
		CONSTRAINT GESTION_ESPACIO_PUBLICO_APARCAMIENTOS_PK PRIMARY KEY (id)
		)WITH OIDS;
ALTER TABLE GESTION_ESPACIO_PUBLICO_APARCAMIENTOS OWNER TO geopista;
GRANT ALL ON TABLE GESTION_ESPACIO_PUBLICO_APARCAMIENTOS TO geopista;
GRANT SELECT ON TABLE GESTION_ESPACIO_PUBLICO_APARCAMIENTOS TO consultas;
CREATE INDEX GESTION_ESPACIO_PUBLICO_APARCAMIENTOS_SPAT_IDX on GESTION_ESPACIO_PUBLICO_APARCAMIENTOS using gist("GEOMETRY");
--CREACION DE LAYERS
-- Creacion de capa - Espacio Publico
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Espacio Publico - Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Espacio Publico - Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Espacio Publico - Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Espacio Publico - Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Espacio Publico - Aparcamientos');
--Insercion de estilos - por defecto
insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?><StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>gestion_espacio_publico_aparcamientos</Name><UserStyle><Name>default:gestion_espacio_publico_aparcamientos</Name><Title>default:gestion_espacio_publico_aparcamientos</Title><Abstract>default:gestion_espacio_publico_aparcamientos</Abstract><FeatureTypeStyle><Name>gestion_espacio_publico_aparcamientos</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_aparcamientos',1,0,0);
--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_aparcamientos',1);
-- insercion de layer-lf
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),6);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (150,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:gestion_espacio_publico_aparcamientos', true,5,0,true,true);
-- Insercion en tablas de sistema
INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'gestion_espacio_publico_aparcamientos',11);
-- Insercion de columna geometria

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);


-- Insercion de columna id
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

-- Insercion de columna nombre
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nombre',10007,CURRVAL('seq_tables'),50,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Nombre');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10007,0);

-- Insercion de columna descripcion
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'descripcion',10036,CURRVAL('seq_tables'),200,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Descripcion');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);

-- Insercion de columna tipos de aparcamientos

INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Tipos de Aparcamientos',CURRVAL('seq_domaincategory'));
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','En Via Publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]En Via Publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]En Via Publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]En Via Publica');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]En Via Publica');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'VP',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','En Edificacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]En Edificacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]En Edificacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]En Edificacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]En Edificacion');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'ED',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','En Subterraneo');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]En Subterraneo');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]En Subterraneo');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]En Subterraneo');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]En Subterraneo');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'SU',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Aparcamientos');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),CURRVAL('seq_dictionary'),4);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('VP','ED','SU');

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo',CURRVAL('seq_domains'),CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Aparcamientos');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),CURRVAL('seq_domains'),0);

-- Insercion de columna clasificacion

INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Clasificacion de Aparcamientos',CURRVAL('seq_domaincategory'));
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Publicos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Publicos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Publicos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Publicos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Publicos');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'PU',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Privados');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Privados');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Privados');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Privados');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Privados');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'PR',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Clasificacion de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Clasificacion de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Clasificacion de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Clasificacion de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Clasificacion de Aparcamientos');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),CURRVAL('seq_dictionary'),4);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('PU','PR');

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'clasificacion',CURRVAL('seq_domains'),CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Clasificacion de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Clasificacion de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Clasificacion de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Clasificacion de Aparcamientos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Clasificacion de Aparcamientos');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),CURRVAL('seq_domains'),0);

-- Insercion de columna area
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'area',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]area');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);

-- Insercion de columna longitud
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'length',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);

-- Insercion de columna id_municipio
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10068,CURRVAL('seq_tables'),NULL,5,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id de Municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),9,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);

-- Insercion de la tabla queries. Ojo. comprobar que el seq_querys esta bien generado - cambiar las select
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_querys'),CURRVAL('seq_layers'),1,
	'SELECT transform("gestion_espacio_publico_aparcamientos"."GEOMETRY", ?T) AS "GEOMETRY","gestion_espacio_publico_aparcamientos"."id","gestion_espacio_publico_aparcamientos"."nombre","gestion_espacio_publico_aparcamientos"."descripcion","gestion_espacio_publico_aparcamientos"."tipo","gestion_espacio_publico_aparcamientos"."clasificacion","gestion_espacio_publico_aparcamientos"."area","gestion_espacio_publico_aparcamientos"."length","gestion_espacio_publico_aparcamientos"."id_municipio" FROM "gestion_espacio_publico_aparcamientos" WHERE "gestion_espacio_publico_aparcamientos"."id_municipio" IN (?M)',
	'UPDATE "gestion_espacio_publico_aparcamientos" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"nombre"=?3,"descripcion"=?4,"tipo"=?5,"clasificacion"=?6,"area"=AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),"length"=PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),"id_municipio" in (?M) WHERE "id"=?2',
	'INSERT INTO "gestion_espacio_publico_aparcamientos" ("GEOMETRY","id","nombre","descripcion","tipo","clasificacion","area","length","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,?6,AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),?M)',
	'DELETE FROM "gestion_espacio_publico_aparcamientos" WHERE "id"=?2');
-- Creacion de la secuencia
CREATE SEQUENCE SEQ_GESTION_ESPACIO_PUBLICO_APARCAMIENTOS
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE SEQ_GESTION_ESPACIO_PUBLICO_APARCAMIENTOS OWNER TO geopista;
-- GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO
CREATE TABLE GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO(
		ID NUMERIC(8),
		"GEOMETRY" GEOMETRY,
		NOMBRE VARCHAR(50),
		DESCRIPCION VARCHAR(200),
		TIPO VARCHAR(3),
		AREA NUMERIC(14,2),
		LENGTH NUMERIC(14,2),
		ID_MUNICIPIO NUMERIC(5),
		CONSTRAINT GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO_PK PRIMARY KEY (id)
		)WITH OIDS;
ALTER TABLE GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO OWNER TO geopista;
GRANT ALL ON TABLE GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO TO geopista;
GRANT SELECT ON TABLE GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO TO consultas;
CREATE INDEX GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO_SPAT_IDX on GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO using gist("GEOMETRY");
--CREACION DE LAYERS
-- Creacion de capa - Espacio Publico
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Espacio Publico - Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Espacio Publico - Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Espacio Publico - Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Espacio Publico - Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Espacio Publico - Mobiliario Urbano');
--Insercion de estilos - por defecto
insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?><StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><NamedLayer><Name>gestion_espacio_publico_mobiliario_urbano</Name><UserStyle><Name>default:gestion_espacio_publico_mobiliario_urbano</Name><Title>default:gestion_espacio_publico_mobiliario_urbano</Title><Abstract>default:gestion_espacio_publico_mobiliario_urbano</Abstract><FeatureTypeStyle><Name>gestion_espacio_publico_mobiliario_urbano</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#339900</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#0066ff</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill''>#ffffff</CssParameter><CssParameter name=''fill-opacity''>1.0</CssParameter></Fill><Stroke><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD,DINAMICA) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_mobiliario_urbano',1,0,0);
--INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),35,'gestion_espacio_publico_mobiliario_urbano',1);
-- insercion de layer-lf
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),7);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (150,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:gestion_espacio_publico_mobiliario_urbano', true,6,0,true,true);
-- Insercion en tablas de sistema
INSERT INTO TABLES(ID_TABLE,NAME,GEOMETRYTYPE) VALUES (NEXTVAL('seq_tables'),'gestion_espacio_publico_mobiliario_urbano',11);
-- Insercion de columna geometria

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'GEOMETRY',NULL,CURRVAL('seq_tables'),NULL,NULL,NULL,1);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Geometria');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Geometria');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);


-- Insercion de columna id
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id',10067,CURRVAL('seq_tables'),NULL,8,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

-- Insercion de columna nombre
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nombre',10007,CURRVAL('seq_tables'),50,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Nombre');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Nombre');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10007,0);

-- Insercion de columna descripcion
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'descripcion',10036,CURRVAL('seq_tables'),200,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Descripcion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Descripcion');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10036,0);

-- Insercion de columna tipos de infraestructuras

INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Tipos de Mobiliario Urbano',CURRVAL('seq_domaincategory'));
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Mobiliario Urbano 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Mobiliario Urbano 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Mobiliario Urbano 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Mobiliario Urbano 1');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Mobiliario Urbano 1');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'I1',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Mobiliario Urbano 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Mobiliario Urbano 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Mobiliario Urbano 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Mobiliario Urbano 2');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Mobiliario Urbano 2');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'I2',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo de Mobiliario Urbano 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo de Mobiliario Urbano 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo de Mobiliario Urbano 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo de Mobiliario Urbano 3');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo de Mobiliario Urbano 3');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'I3',CURRVAL('seq_dictionary'),7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Mobiliario Urbano');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),CURRVAL('seq_dictionary'),4);
UPDATE DOMAINNODES SET PARENTDOMAIN = CURRVAL('seq_domainnodes') WHERE ID_DOMAIN = CURRVAL('seq_domains') AND PATTERN IN ('I1','I2','I3');

INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'tipo',CURRVAL('seq_domains'),CURRVAL('seq_tables'),3,NULL,NULL,3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipos de Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipos de Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipos de Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipos de Mobiliario Urbano');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipos de Mobiliario Urbano');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),CURRVAL('seq_domains'),0);

-- Insercion de columna area
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'area',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]area');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]area');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);

-- Insercion de columna longitud
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'length',null,CURRVAL('seq_tables'),NULL,14,1,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Longitud');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Longitud');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);

-- Insercion de columna id_municipio
INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'id_municipio',10068,CURRVAL('seq_tables'),NULL,5,0,2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Id de Municipio');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Id de Municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);

-- Insercion de la tabla queries. Ojo. comprobar que el seq_querys esta bien generado - cambiar las select
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_querys'),CURRVAL('seq_layers'),1,
	'SELECT transform("gestion_espacio_publico_mobiliario_urbano"."GEOMETRY", ?T) AS "GEOMETRY","gestion_espacio_publico_mobiliario_urbano"."id","gestion_espacio_publico_mobiliario_urbano"."nombre","gestion_espacio_publico_mobiliario_urbano"."descripcion","gestion_espacio_publico_mobiliario_urbano"."tipo","gestion_espacio_publico_mobiliario_urbano"."area","gestion_espacio_publico_mobiliario_urbano"."length","gestion_espacio_publico_mobiliario_urbano"."id_municipio" FROM "gestion_espacio_publico_mobiliario_urbano" WHERE "gestion_espacio_publico_mobiliario_urbano"."id_municipio" IN (?M)',
	'UPDATE "gestion_espacio_publico_mobiliario_urbano" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"nombre"=?3,"descripcion"=?4,"tipo"=?5,"area"=AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),"length"=PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),"id_municipio" in (?M) WHERE "id"=?2',
	'INSERT INTO "gestion_espacio_publico_mobiliario_urbano" ("GEOMETRY","id","nombre","descripcion","tipo","area","length","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,AREA2D(transform(GeometryFromText(text(?1),?S), ?T)),PERIMETER(transform(GeometryFromText(text(?1),?S), ?T)),?M)',
	'DELETE FROM "gestion_espacio_publico_mobiliario_urbano" WHERE "id"=?2');
-- Creacion de la secuencia
CREATE SEQUENCE SEQ_GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE SEQ_GESTION_ESPACIO_PUBLICO_MOBILIARIO_URBANO OWNER TO geopista;
-- Modificacion de la secuencia de mapas
select setval('seq_maps',cast(max(maps.id_map) as bigint),'t');