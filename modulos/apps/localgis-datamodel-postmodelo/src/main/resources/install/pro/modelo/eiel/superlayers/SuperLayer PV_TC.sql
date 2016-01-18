

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT id_layer FROM layers WHERE name = 'VT_TC') THEN	
		--1 Creacion estilo
		-- select layers.name,id_style,id_map from layers inner join layers_styles on layers.id_layer=layers_styles.id_layer where id_map=50 and name='VT'
		-- select xml from styles where id_style=343;

		INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'), '<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>VT_TC</Name><UserStyle><Name>VT_TC:_:default:VT_TC</Name><Title>VT_TC:_:default:VT_TC</Title><Abstract>VT_TC:_:default:VT_TC</Abstract><FeatureTypeStyle><Name>VT</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>50000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ece9d8</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#ece9d8</CssParameter><CssParameter name=''stroke-linejoin''>mitre</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>butt</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>25.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Revision expirada Temporal Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9899999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label>Elemento Temporal</Label><Font><CssParameter name=''font-color''>#6666ff</CssParameter><CssParameter name=''font-family''>Agency FB</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>18.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Revision expirada Publicable Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9799999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label>Elemento Publicable</Label><Font><CssParameter name=''font-color''>#6666ff</CssParameter><CssParameter name=''font-family''>Agency FB</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>18.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle><UserStyle><Name>VT:_:EIEL: VT</Name><Title>VT:_:EIEL: VT</Title><Abstract>VT:_:EIEL: VT</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ffff33</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>0 a 15000</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name=''fill-opacity''>1.0</CssParameter><CssParameter name=''fill''>#ffffff</CssParameter></Fill><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>9</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>Texto</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Clave</ogc:PropertyName><ogc:Literal>VT</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>15000.0</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Clave</ogc:PropertyName></Label><Font><CssParameter name=''font-color''>#000000</CssParameter><CssParameter name=''font-family''>Arial</CssParameter><CssParameter name=''font-weight''>bold</CssParameter><CssParameter name=''font-style''>normal</CssParameter><CssParameter name=''font-size''>10.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.5</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');




		-- select id_layer,traduccion from layers inner join dictionary on layers.id_name="dictionary".id_vocablo where name='VT'
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Vertedero TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat] Vertedero TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va] Vertedero TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl] Vertedero TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu] Vertedero TC');

		-- 
		INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
				VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,(SELECT idacl FROM acl WHERE name='Vertedero'),'VT_TC',currval('seq_styles'),null,1,0,1);
				
				
		/*
		SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||d.traduccion||E'\');\n' 
		|| E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),
		'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_c_vt\' and c.name=\''||c.name||E'\')'||E','||a.position||',true);' as sqlTableColumnsLayerAtributes
		FROM dictionary d, columns c, attributes a, tables t
		WHERE t.name='eiel_c_vt'  and
			c.id_table=t.id_table
			and a.id_layer=(SELECT id_layer FROM layers where name='VT') 
			 and a.id_column=c.id and a.id_alias=d.id_vocablo and d.locale='es_ES'
		 order by a.position;


		*/

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='GEOMETRY'),1,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='id'),2,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','IdMunicip');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='id_municipio'),3,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Clave');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='clave'),4,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código provincia');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='codprov'),5,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código Municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='codmunic'),6,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código Orden');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='orden_vt'),7,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Precisión digitalización');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='precision_dig'),8,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Observaciones');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='observ'),9,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Revisión actual');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_vt' and c.name='revision_actual'),10,true);
				
				
		/*
		SELECT  E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||c.name||E'\');\n' ||
		 E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'||
		 E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_t_vt\' and c.name=\''||c.name||E'\')'||E','||'POSITION'||',true);' as sqlTableColumnsLayerAtributes
		 FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
		 WHERE lnc.tabla=t.name and t.id_table=c.id_table
		and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
		and t.name='eiel_t_vt'
		*/

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','tipo');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='tipo'),1,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','titular');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='titular'),2,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','gestor');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='gestor'),3,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','olores');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='olores'),4,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','humos');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='humos'),5,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','cont_anima');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='cont_anima'),6,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','r_inun');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='r_inun'),7,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','filtracion');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='filtracion'),8,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','impacto_v');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='impacto_v'),9,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','frec_averia');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='frec_averia'),10,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','saturacion');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='saturacion'),11,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','inestable');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='inestable'),12,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','categoria');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='categoria'),13,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','actividad');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='actividad'),14,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='estado'),15,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','obra_ejec');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='obra_ejec'),16,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','capac_ampl');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='capac_ampl'),17,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fecha_revision');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='fecha_revision'),18,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','estado_revision');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='estado_revision'),19,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','revision_expirada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_vt' and c.name='revision_expirada'),20,true);


		/*
		SELECT E'INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (' || c.id || ',' || d.id || ',0);' as sqlInsertColumnDomains
		FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
		WHERE lnc.tabla=t.name and t.id_table=c.id_table
		and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
		and t.name='eiel_t_vt';
		*/

		-- Esto habria que ejcutarlo luego a mano
		/*
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105860,20246,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105861,20248,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105862,20250,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105863,20252,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105864,20253,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105865,20254,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105866,20255,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105867,20256,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105868,20257,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105869,20258,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105870,20259,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105871,20260,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105877,20249,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105878,20247,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105880,20293,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105881,20244,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105883,20224,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105884,1125,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105885,20296,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105888,20416,0);
		*/
				
				
		update columns set id_domain = columns_domains.id_domain where columns.id = columns_domains.id_column and columns.id_table = (select id_table from tables where name = 'eiel_t_vt');			
			
			
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";

