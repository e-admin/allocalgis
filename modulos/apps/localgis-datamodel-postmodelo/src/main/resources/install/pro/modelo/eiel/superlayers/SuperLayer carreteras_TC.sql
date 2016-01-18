

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT id_layer FROM layers WHERE name = 'carreteras_TC') THEN	
		--1 Creacion estilo
		-- select layers.name,id_style,id_map from layers inner join layers_styles on layers.id_layer=layers_styles.id_layer where id_map=50 and name='carreteras'
		-- select xml from styles where id_style=339;

		INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'), '<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>carreteras_TC</Name><UserStyle><Name>carreteras_TC:_:default:carreteras_TC</Name><Title>carreteras_TC:_:default:carreteras_TC</Title><Abstract>carreteras:_:default:carreteras</Abstract><FeatureTypeStyle><Name>carreteras</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke''>#ff0000</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>2.0</ogc:Literal></CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>Revision expirada Temporal Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9899999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label>Elemento Temporal</Label><Font><CssParameter name=''font-family''>Agency FB</CssParameter><CssParameter name=''font-color''>#6666ff</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-size''>18.0</CssParameter><CssParameter name=''font-style''>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule><Rule><Name>Revision expirada Publicable Textos</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc=''http://www.opengis.net/ogc''><ogc:PropertyIsEqualTo><ogc:PropertyName>Revision Expirada</ogc:PropertyName><ogc:Literal>9799999999</ogc:Literal></ogc:PropertyIsEqualTo></ogc:Filter>
		<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>25000.0</MaxScaleDenominator><TextSymbolizer><Label>Elemento Publicable</Label><Font><CssParameter name=''font-family''>Agency FB</CssParameter><CssParameter name=''font-color''>#6666ff</CssParameter><CssParameter name=''font-weight''>normal</CssParameter><CssParameter name=''font-size''>18.0</CssParameter><CssParameter name=''font-style''>normal</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>0.0</AnchorPointX><AnchorPointY>0.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>0.0</DisplacementX><DisplacementY>0.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>');


		-- select id_layer,traduccion from layers inner join dictionary on layers.id_name="dictionary".id_vocablo where name='carreteras'
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tramos de carretera TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat] Tramos de carretera TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va] Tramos de carretera TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl] Tramos de carretera TC');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu] Tramos de carretera TC');

		-- 
		INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
				VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,(SELECT idacl FROM acl WHERE name='Tramos de carretera'),'carreteras_TC',currval('seq_styles'),null,1,0,1);
			
		/*
		SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||d.traduccion||E'\');\n' 
		|| E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),
		'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_c_tramos_carreteras\' and c.name=\''||c.name||E'\')'||E','||a.position||',true);' as sqlTableColumnsLayerAtributes
		FROM dictionary d, columns c, attributes a, tables t
		WHERE t.name='eiel_c_tramos_carreteras'  and
			c.id_table=t.id_table
			and a.id_layer=(SELECT id_layer FROM layers where name='carreteras') 
			 and a.id_column=c.id and a.id_alias=d.id_vocablo and d.locale='es_ES'
		 order by a.position;


		*/
			
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='GEOMETRY'),1,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='id'),2,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','IdMunicip');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='id_municipio'),3,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código provincia');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='codprov'),4,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código Municipio');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='codmunic'),5,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Código carretera');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='cod_carrt'),6,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Punto kilometríco inicial');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='pki'),7,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Punto kilometríco final');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='pkf'),8,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Gestor');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='gestor'),9,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Señaliza');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='senaliza'),10,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Firme');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='firme'),11,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Estado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='estado'),12,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Ancho');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='ancho'),13,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='longitud'),14,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Paso a nivel');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='paso_nivel'),15,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Dimensionamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='dimensiona'),16,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Sinuosidad');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='muy_sinuoso'),17,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pendiente excesiva');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='pte_excesiva'),18,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Frecuente extrechamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='fre_estrech'),19,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Superficie');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='superficie'),20,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Obra Ejecutada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='obra_ejec'),21,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Toma de datos');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='toma_dato'),22,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Fecha de actualización');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='fecha_act'),23,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Fecha desuso');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='fecha_desuso'),24,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Observaciones');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='observ'),25,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Revisión actual');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),
		(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_c_tramos_carreteras' and c.name='revision_actual'),26,true);
			

			
		/*
		SELECT  E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||c.name||E'\');\n' ||
		 E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'||
		 E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_t_carreteras\' and c.name=\''||c.name||E'\')'||E','||'POSITION'||',true);' as sqlTableColumnsLayerAtributes
		 FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
		 WHERE lnc.tabla=t.name and t.id_table=c.id_table
		and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
		and t.name='eiel_t_carreteras'
		*/

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','titular_via');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_carreteras' and c.name='titular_via'),1,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fecha_revision');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_carreteras' and c.name='fecha_revision'),2,true);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','revision_expirada');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,"position",EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_t_carreteras' and c.name='revision_expirada'),3,true);


		/*
		SELECT E'INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (' || c.id || ',' || d.id || ',0);' as sqlInsertColumnDomains
		FROM lcg_nodos_capas lnc, tables t, columns c, lcg_nodos_capas_campos lncc, domains d
		WHERE lnc.tabla=t.name and t.id_table=c.id_table
		and lncc.clave_capa=lnc.clave and lncc.campo_bd=c.name and lncc.dominio=d.name
		and t.name='eiel_t_carreteras';
		*/

		-- Esto habria que ejcutarlo luego a mano
		/*
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105155,20376,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105156,1125,0);
		INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (105162,20416,0);
		*/	
			
		update columns set id_domain = columns_domains.id_domain where columns.id = columns_domains.id_column and columns.id_table = (select id_table from tables where name = 'eiel_t_carreteras');	
			
			
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";

