 
--Super Layer Cementerios TC (tablas t y c)


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT id_layer FROM layers WHERE name = 'CE_TC') THEN	
	INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
	<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<NamedLayer><Name>CE_TC</Name><UserStyle><Name>CE_TC:_:default:CE_TC</Name><Title>CE_TC:_:default:CE_TC</Title><Abstract>CE_TC:_:default:CE_TC</Abstract><FeatureTypeStyle><Name>CE</Name><Rule><Name>CE_TC</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name="fill-opacity">1.0</CssParameter><CssParameter name="fill">#ece9d8</CssParameter></Fill><Stroke><CssParameter name="stroke">#ece9d8</CssParameter><CssParameter name="stroke-linejoin">mitre</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name="stroke-linecap">butt</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>25.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Revision expirada Temporal Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9899999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
	<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label>Elemento Temporal</Label><Font><CssParameter name="font-color">#6666ff</CssParameter><CssParameter name="font-family">Agency FB</CssParameter><CssParameter name="font-weight">normal</CssParameter><CssParameter name="font-style">normal</CssParameter><CssParameter name="font-size">18.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Revision expirada Publicable Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9799999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
	<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label>Elemento Publicable</Label><Font><CssParameter name="font-color">#6666ff</CssParameter><CssParameter name="font-family">Agency FB</CssParameter><CssParameter name="font-weight">normal</CssParameter><CssParameter name="font-style">normal</CssParameter><CssParameter name="font-size">18.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>CE:_:EIEL: CE</Name><Title>CE:_:EIEL: CE</Title><Abstract>CE:_:EIEL: CE</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill-opacity">1.0</CssParameter><CssParameter name="fill">#33ff66</CssParameter></Fill><Stroke><CssParameter name="stroke">#ff0000</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-width"><ogc:Literal>2.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>Cementerio</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name="fill-opacity">1.0</CssParameter><CssParameter name="fill">#9a9292</CssParameter></Fill><Stroke><CssParameter name="stroke">#908787</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>5.0</ogc:Literal></CssParameter><CssParameter name="stroke-linecap">round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>CE</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>CE</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
	<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name="font-family">Arial</CssParameter><CssParameter name="font-color">#817a7a</CssParameter><CssParameter name="font-weight">bold</CssParameter><CssParameter name="font-size">10.0</CssParameter><CssParameter name="font-style">normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>2.0</DisplacementX><DisplacementY>2.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
	</StyledLayerDescriptor>');


	INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Cementerios TC');
	INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Cementerios TC');
	INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Cementerios TC');
	INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Cementerios TC');
	INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Cementerios TC');

	INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
			VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,(SELECT idacl FROM acl WHERE name='Cementerios'),'CE_TC',currval('seq_styles'),null,1,0,1);

	-- Creació® ¬ayerfamily nueva para no interferir
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Cementerios TC');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Cementerios TC');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Cementerios TC');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Cementerios TC');
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Cementerios TC');
	INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

	INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),currval('seq_layerfamilies'), 1);


		


	--Suponemos que los insert en tables y columns ya estâ® creados previamente

	--Primero añ¡¤©mos los atributos de la tabla c. ATENCION HAY QUE SUSTITUIR MANUALMENTE LA CLAVE POSITION EN CADA SENTENCIA POR EL ORDEN DEL ATRIBUTO, EMPEZANDO POR 1
	/* EJECUTAR
	SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||d.traduccion||E'\');\n' || E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_c_ce\' and c.name=\''||c.name||E'\')'||E','||a.position||',true);' as sqlTableColumnsLayerAtributes
	FROM dictionary d, columns c, attributes a, tables t
	WHERE t.name='eiel_c_ce'  and
		 c.id_table=t.id_table
		 and a.id_layer=(SELECT id_layer FROM layers where name='CE') 
		 and a.id_column=c.id and a.id_alias=d.id_vocablo and d.locale='es_ES'
	order by a.position;
	*/

	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='GEOMETRY'),1,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='id'),2,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','IdMunicip');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='id_municipio'),3,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Clave');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='clave'),4,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código provincia');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='codprov'),5,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código Municipio');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='codmunic'),6,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código INE Entidad');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='codentidad'),7,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código INE ');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='codpoblamiento'),8,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código Orden');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='orden_ce'),9,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Precisión de Digitalización';
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='precision_dig'),10,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Observaciones');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='observ'),11,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Revisión actual');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_ce' and c.name='revision_actual'),12,true);


	--A continuació®¬ los atributos de la tabla t. . ATENCION HAY QUE SUSTITUIR MANUALMENTE LA CLAVE POSITION EN CADA SENTENCIA POR EL ORDEN DEL ATRIBUTO, EMPEZANDO POR EL ÃšLTIMO+1 DEL BLOQUE ANTERIOR
	/* EJECUTAR
	SELECT  E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||c.name||E'\');\n' || E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_t_ce\' and c.name=\''||c.name||E'\')'||E','||'POSITION'||',true);' as sqlTableColumnsLayerAtributes
	FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
	WHERE lnc.tabla=t.name and t.id_table=c.id_table
	and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
	and t.name='eiel_t_ce';
	*/


	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','titular');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='titular'),13,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','acceso');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='acceso'),14,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','capilla');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='capilla'),15,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ampliacion');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='ampliacion'),16,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','saturacion');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='saturacion'),17,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','crematorio');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='crematorio'),18,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','acceso_s_ruedas');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='acceso_s_ruedas'),19,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','obra_ejec');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='obra_ejec'),20,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fecha_inst');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='fecha_inst'),21,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fecha_revision');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='fecha_revision'),22,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado_revision');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='estado_revision'),23,true);
	INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','revision_expirada');
	INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_ce' and c.name='revision_expirada'),24,true);


	--Asignar dominio a las columnas de la tabla t
	/* EJECUTAR
	SELECT E'INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (' || c.id || ',' || d.id || ',0);' as sqlInsertColumnDomains
	FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
	WHERE lnc.tabla=t.name and t.id_table=c.id_table
	and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
	and t.name='eiel_t_ce';
	*/
	-- Esto habria que ejcutarlo luego a mano
	/*
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105185,20222,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105186,20223,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105187,20225,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105189,20224,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105190,20259,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105192,20226,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105193,20243,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105194,20244,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105195,1125,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105197,1125,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105198,20296,0);
	INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105202,20416,0);
	*/
	/*

	SELECT "eiel_c_ce"."id","eiel_c_ce"."id_municipio",transform("eiel_c_ce"."GEOMETRY", ?T) AS "GEOMETRY","eiel_c_ce"."clave","eiel_c_ce"."codprov","eiel_c_ce"."codmunic","eiel_c_ce"."codentidad","eiel_c_ce"."codpoblamiento","eiel_c_ce"."orden_ce","eiel_c_ce"."observ","eiel_c_ce"."precision_dig","eiel_c_ce"."revision_actual","eiel_t_ce"."titular","eiel_t_ce"."acceso","eiel_t_ce"."capilla","eiel_t_ce"."ampliacion","eiel_t_ce"."saturacion","eiel_t_ce"."crematorio","eiel_t_ce"."acceso_s_ruedas","eiel_t_ce"."obra_ejec","eiel_t_ce"."fecha_inst","eiel_t_ce"."fecha_revision","eiel_t_ce"."estado_revision","eiel_t_ce"."revision_expirada" FROM "eiel_c_ce","eiel_t_ce" WHERE "eiel_c_ce"."id_municipio" IN (?M) AND "eiel_c_ce"."codprov"= "eiel_t_ce"."codprov" AND "eiel_c_ce"."codmunic"= "eiel_t_ce"."codmunic" AND "eiel_c_ce"."codentidad"= "eiel_t_ce"."codentidad" AND "eiel_c_ce"."codpoblamiento"= "eiel_t_ce"."codpoblamiento" AND "eiel_c_ce"."orden_ce"= "eiel_t_ce"."orden_ce" AND "eiel_c_ce"."clave"= "eiel_t_ce"."clave"
	*/	
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";









