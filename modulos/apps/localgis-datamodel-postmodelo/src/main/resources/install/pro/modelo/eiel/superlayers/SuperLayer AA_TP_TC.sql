
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT id_layer FROM layers WHERE name = 'TP_TC') THEN	
		--1 Creacion estilo
		-- select layers.name,id_style,id_map from layers inner join layers_styles on layers.id_layer=layers_styles.id_layer where id_map=50 and name='TP'
		-- select xml from styles where id_style=316;


		INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'), '<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>TP_TC</Name><UserStyle><Name>TP_TC:_:EIEL:TP_TC</Name><Title>TP_TC:_:EIEL:TP_TC</Title><Abstract>TP_TC:_:EIEL:TP_TC</Abstract><FeatureTypeStyle><Name>TP</Name><Rule><Name>Punto de tratamiento</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>x</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#999999</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000066</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>3.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>TP</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-color''>#cc00cc</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-size''>10.0</CssParameter><CssParameter name=''font-style''>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Revision expirada Temporal Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9899999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label>Elemento Temporal</Label><Font><CssParameter name=''font-color''>#6666ff</CssParameter><CssParameter name=''font-family''>Agency FB</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>18.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Revision expirada Publicable Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9799999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label>Elemento Publicable</Label><Font><CssParameter name=''font-color''>#6666ff</CssParameter><CssParameter name=''font-family''>Agency FB</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>18.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');



		-- select id_layer,traduccion from layers inner join dictionary on layers.id_name="dictionary".id_vocablo where name='TP'
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tratamientos de Potabilización TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat] Tratamientos de Potabilización TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va] Tratamientos de Potabilización TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl] Tratamientos de Potabilización TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu] Tratamientos de Potabilización TC');

		-- 
		INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
				VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,(SELECT idacl FROM acl WHERE name='Tratamientos de Potabilizacion'),'TP_TC',currval('seq_styles'),null,1,0,1);
				
				
		/*
		SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||d.traduccion||E'\');\n' 
		|| E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),
		'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_c_abast_tp\' and c.name=\''||c.name||E'\')'||E','||a.position||',true);' as sqlTableColumnsLayerAtributes
		FROM dictionary d, columns c, attributes a, tables t
		WHERE t.name='eiel_c_abast_tp'  and
			c.id_table=t.id_table
			and a.id_layer=(SELECT id_layer FROM layers where name='TP') 
			 and a.id_column=c.id and a.id_alias=d.id_vocablo and d.locale='es_ES'
		 order by a.position;
		*/

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='GEOMETRY'),1,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='id'),2,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','IdMunicip');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='id_municipio'),3,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='clave'),4,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código provincia');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='codprov'),5,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código Municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='codmunic'),6,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código Orden');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='orden_tp'),7,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Cota_z');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='cota_z'),8,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Obra Ejecutada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='obra_ejec'),9,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Precisión digitalización');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='precision_dig'),10,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Observaciones');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='observ'),11,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Revisión actual');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_abast_tp' and c.name='revision_actual'),12,true);
				

				
		/*
		SELECT  E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||c.name||E'\');\n' ||
		 E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'||
		 E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_t_abast_tp\' and c.name=\''||c.name||E'\')'||E','||'POSITION'||',true);' as sqlTableColumnsLayerAtributes
		 FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
		 WHERE lnc.tabla=t.name and t.id_table=c.id_table
		and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
		and t.name='eiel_t_abast_tp';
		*/

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='tipo'),1,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ubicacion');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='ubicacion'),2,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','s_desinf');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='s_desinf'),3,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','categoria_a1');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='categoria_a1'),4,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','categoria_a3');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='categoria_a3'),5,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','desaladora');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='desaladora'),6,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','otros');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='otros'),7,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','desinf_1');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='desinf_1'),8,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','desinf_2');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='desinf_2'),9,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','desinf_3');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='desinf_3'),10,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','periodicidad');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='periodicidad'),11,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','organismo_control');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='organismo_control'),12,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='estado'),13,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fecha_inst');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='fecha_inst'),14,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fecha_revision');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='fecha_revision'),15,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado_revision');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='estado_revision'),16,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','revision_expirada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='revision_expirada'),17,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','categoria_a2');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_abast_tp' and c.name='categoria_a2'),18,true);


				
		/*
		SELECT E'INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (' || c.id || ',' || d.id || ',0);' as sqlInsertColumnDomains
		FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
		WHERE lnc.tabla=t.name and t.id_table=c.id_table
		and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
		and t.name='eiel_t_abast_tp';
		*/
		-- Esto habria que ejcutarlo luego a mano
		/*
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105102,20299,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105103,20298,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105104,20300,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105105,20303,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105106,20305,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105107,20306,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105108,20266,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105109,20307,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105110,20307,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105111,20307,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105112,20301,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105113,20302,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105114,20293,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105115,1125,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105116,1125,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105117,20296,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105121,20416,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105903,20304,0);
		*/		
				

		update columns set id_domain = columns_domains.id_domain where columns.id = columns_domains.id_column and columns.id_table = (select id_table from tables where name = 'eiel_t_abast_tp');
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";


				