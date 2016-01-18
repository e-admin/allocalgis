
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT id_layer FROM layers WHERE name = 'PJ_TC') THEN	
		-- Generador de supercapas a partir de tablas eiel_c_ y eiel_t
		-- v2 Los atributos de correspondientes a la tabla eiel_c_ se toman de los ya existentes (#LAYER_ORIGINAL#), asð¬±¥ mantiene el orden, y lo mâ³ importante, el valor de diccionario

		--0 Sustituir parâ®¥tros
		--#SUPERCAPA_ID_TC# - EJEMPLO: PJ_TC
		--#SUPERCAPA_NAME_TC# - EJEMPLO: Parques y Jardines TC
		--#LAYER_ORIGINAL# - EJEMPLO: PJ
		--#ACLNAME# - EJEMPLO: Parques jardines y â³¥as naturales
		--#TABLA_C# - EJEMPLO: eiel_c_pj
		--#TABLA_T# - EJEMPLO: eiel_t_pj


		--1 Creacion estilo
		INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'), '<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>PJ_TC</Name><UserStyle><Name>PJ_TC:_:EIEL: PJ_TC </Name><Title>PJ_TC:_:EIEL: PJ_TC </Title><Abstract>PJ_TC:_:EIEL: PJ_TC </Abstract><FeatureTypeStyle><Name>PJ_TC </Name><Rule><Name>Depósito de agua</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#3333ff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>DE</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>DE</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');

		--2 Crear supercapa

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Parques y Jardines TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat] Parques y Jardines TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va] Parques y Jardines TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl] Parques y Jardines TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu] Parques y Jardines TC');

		INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
				VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,(SELECT idacl FROM acl WHERE name='Parques jardines y â³¥as naturales'),'PJ_TC',currval('seq_styles'),null,1,0,1);

		-- Quito el layerfamily para no crear un layerfamily para cada capa, lo suyo es crear desde el propio gestor de capas un layerfamily
		-- al final y asociarle todas las capas a el.
		--3 Crear layerfamily
		--INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','#SUPERCAPA# TC ');
		--INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat] #SUPERCAPA# TC ');
		--INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl] #SUPERCAPA# TC ');
		--INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va] #SUPERCAPA# TC ');
		--INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu] #SUPERCAPA# TC ');
		--INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

		--INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 1);


		--4a Atributos tabla eiel_c_
		--Ejecutar sentencia y lanzar sql generados

		-- SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||d.traduccion||E'\');\n' || E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_c_pj\' and c.name=\''||c.name||E'\')'||E','||a.position||',true);' as sqlTableColumnsLayerAtributes
		-- FROM dictionary d, columns c, attributes a, tables t
		-- WHERE t.name='eiel_c_pj'  and
		--     c.id_table=t.id_table
		--     and a.id_layer=(SELECT id_layer FROM layers where name='PJ') 
		--     and a.id_column=c.id and a.id_alias=d.id_vocablo and d.locale='es_ES'
		-- order by a.position;

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='GEOMETRY'),1,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='id'),2,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','IdMunicip');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='id_municipio'),3,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='clave'),4,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código provincia');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='codprov'),5,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código Municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='codmunic'),6,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código INE Entidad');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='codentidad'),7,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código INE ');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='codpoblamiento'),8,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código Orden');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='orden_pj'),9,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Precisión Digitalización';
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='precision_dig'),10,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Observaciones');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='observ'),11,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Revisión  actual');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_pj' and c.name='revision_actual'),12,true);



		--5a Atributos tabla eiel_t_
		--Ejecutar sentencia y modificar SQL generado (hay que cambiar 'POSITION' por la posicion luego lanzar los SQL generados:

		-- SELECT  E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||c.name||E'\');\n' || E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_t_pj\' and c.name=\''||c.name||E'\')'||E','||'POSITION'||',true);' as sqlTableColumnsLayerAtributes
		-- FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
		-- WHERE lnc.tabla=t.name and t.id_table=c.id_table
		-- and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
		-- and t.name='eiel_t_pj';

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','titular');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='titular'),1,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','gestor');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='gestor'),2,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','agua');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='agua'),3,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','saneamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='saneamiento'),4,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','electricidad');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='electricidad'),5,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','comedor');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='comedor'),6,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','juegos_inf');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='juegos_inf'),7,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','otras');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='otras'),8,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='estado'),9,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','acceso_s_ruedas');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='acceso_s_ruedas'),10,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','obra_ejec');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='obra_ejec'),11,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fecha_revision');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='fecha_revision'),12,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado_revision');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='estado_revision'),13,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','revision_expirada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_pj' and c.name='revision_expirada'),14,true);


		--6 Asociar un dominio existente a las columnas de la table eiel_t_

		--Ejecutar las sentencias generadas por:

		-- SELECT E'INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (' || c.id || ',' || d.id || ',0);' as sqlInsertColumnDomains
		-- FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
		-- WHERE lnc.tabla=t.name and t.id_table=c.id_table
		-- and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
		-- and t.name='eiel_t_pj';

		-- Esto habria que ejcutarlo luego a mano
		/*
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105606,20242,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105607,20245,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105610,20214,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105611,20265,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105612,20270,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105613,20269,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105614,20268,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105615,20351,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105616,20293,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105617,20243,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105618,20244,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105620,1125,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105621,20296,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105624,20416,0);
		*/

		-- 7. Asociar el id_domain en la tabla columns
		update columns set id_domain = columns_domains.id_domain where columns.id = columns_domains.id_column and columns.id_table = (select id_table from tables where name = 'eiel_t_pj');
	END IF;	

	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";


