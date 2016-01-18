/*
 LocalGIS Layer Export v0.1 20111122
 SQL para generar los SQL de creación de capas a partir de una instalación existente (Ej. para crear SQL de creación de capas a partir de capas ya creadas en entorno DEV)

 

 
*/

-- 0 Mapa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_Captaciones');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_II_Captaciones</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_II_Captaciones</mapName></mapDescriptor>',0);




-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "

INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_II_Captaciones</Name><UserStyle><Name>EIEL_Indicadores_II_Captaciones:_:default:EIEL_Indicadores_II_Captaciones</Name><Title>EIEL_Indicadores_II_Captaciones:_:default:EIEL_Indicadores_II_Captaciones</Title><Abstract>EIEL_Indicadores_II_Captaciones:_:default:EIEL_Indicadores_II_Captaciones</Abstract><FeatureTypeStyle><Name>EIEL_Indicadores_II_Captaciones</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsGreaterThan><ogc:PropertyName>Núm. captaciones estado Malo</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsGreaterThan></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="file:iconlib/123.gif"/><Format>image/gif</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>16.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><TextSymbolizer><Label><ogc:PropertyName>Núm. de captaciones</ogc:PropertyName></Label><Font><CssParameter name="font-color">#000000</CssParameter><CssParameter name="font-family">Agency FB</CssParameter><CssParameter name="font-weight">normal</CssParameter><CssParameter name="font-style">normal</CssParameter><CssParameter name="font-size">12.0</CssParameter></Font><LabelPlacement><PointPlacement><AnchorPoint><AnchorPointX>1.0</AnchorPointX><AnchorPointY>1.0</AnchorPointY></AnchorPoint><Displacement><DisplacementX>10.0</DisplacementX><DisplacementY>15.0</DisplacementY></Displacement><Rotation>0.0</Rotation></PointPlacement></LabelPlacement></TextSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>');

 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Captaciones');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) 
   VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Indicadores_II_Captaciones',1,0);

-- 2b Creación layerfamily
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_Captaciones');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

-- 2c -- Asociacion de layer-layerfamily, map
      -- Revisar nombre mapa

INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),0);


--INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps m, dictionary d where m.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Captaciones'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='EIEL PARROQUIAS'),1,0);
INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps m, dictionary d where m.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Captaciones'),currval('seq_layerfamilies'),1,0);

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'EIEL_Indicadores_II_Captaciones:_:default:EIEL_Indicadores_II_Captaciones', true,0,0,true,true);


-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT transform("eiel_indicadores_d_captaciones"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_captaciones"."id","eiel_indicadores_d_captaciones"."toponimo","eiel_indicadores_d_captaciones"."id_municipio","eiel_indicadores_d_captaciones"."codprov","eiel_indicadores_d_captaciones"."codentidad","eiel_indicadores_d_captaciones"."codmunic","eiel_indicadores_d_captaciones"."codpoblamiento","eiel_indicadores_d_captaciones"."ncaptaciones","eiel_indicadores_d_captaciones"."ncaptacionesb","eiel_indicadores_d_captaciones"."ncaptacionesm","eiel_indicadores_d_captaciones"."ncaptacionesr","eiel_indicadores_d_captaciones"."numest_caudalins","eiel_indicadores_d_captaciones"."numest_caudalsuf","eiel_indicadores_d_captaciones"."numest_restricf","eiel_indicadores_d_captaciones"."numest_restricm","eiel_indicadores_d_captaciones"."numest_restricn","eiel_indicadores_d_captaciones"."numhab_caudalins","eiel_indicadores_d_captaciones"."numhab_cauldalsuf","eiel_indicadores_d_captaciones"."numhab_resticn","eiel_indicadores_d_captaciones"."numhab_restricf","eiel_indicadores_d_captaciones"."numhab_restricm","eiel_indicadores_d_captaciones"."numn_caudalins","eiel_indicadores_d_captaciones"."numn_caudalsuf","eiel_indicadores_d_captaciones"."numn_restricf","eiel_indicadores_d_captaciones"."numn_restricm","eiel_indicadores_d_captaciones"."numn_restricn" FROM "eiel_indicadores_d_captaciones" WHERE "eiel_indicadores_d_captaciones"."id_municipio"  in (?M)',
'UPDATE "eiel_indicadores_d_captaciones" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"toponimo"=?3,"id_municipio"=?4,"codprov"=?5,"codentidad"=?6,"codmunic"=?7,"codpoblamiento"=?8,"ncaptaciones"=?9,"ncaptacionesb"=?10,"ncaptacionesm"=?11,"ncaptacionesr"=?12,"numest_caudalins"=?13,"numest_caudalsuf"=?14,"numest_restricf"=?15,"numest_restricm"=?16,"numest_restricn"=?17,"numhab_caudalins"=?18,"numhab_cauldalsuf"=?19,"numhab_resticn"=?20,"numhab_restricf"=?21,"numhab_restricm"=?22,"numn_caudalins"=?23,"numn_caudalsuf"=?24,"numn_restricf"=?25,"numn_restricm"=?26,"numn_restricn"=?27 WHERE "id_municipio"=?4',
'INSERT INTO "eiel_indicadores_d_captaciones" ("GEOMETRY","id","toponimo","id_municipio","codprov","codentidad","codmunic","codpoblamiento","ncaptaciones","ncaptacionesb","ncaptacionesm","ncaptacionesr","numest_caudalins","numest_caudalsuf","numest_restricf","numest_restricm","numest_restricn","numhab_caudalins","numhab_cauldalsuf","numhab_resticn","numhab_restricf","numhab_restricm","numn_caudalins","numn_caudalsuf","numn_restricf","numn_restricm","numn_restricn") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?2,?3,?PK,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?25,?26,?27)',
'DELETE FROM "eiel_indicadores_d_captaciones" WHERE "id_municipio"=?4');


-- 4a Insercion Table
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_captaciones',5);
-- 4b Creación columnas

INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),10067,'id',0,32,0,2); --10354 eiel_indicadores_d_captaciones.id integer
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'toponimo',100,0,0,3); --10354 eiel_indicadores_d_captaciones.toponimo character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),10068,'id_municipio',5,0,0,3); --10354 eiel_indicadores_d_captaciones.id_municipio character varying
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',0,0,0,1); --10354 eiel_indicadores_d_captaciones.GEOMETRY USER-DEFINED
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'codprov',2,0,0,3); --10354 eiel_indicadores_d_captaciones.codprov character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'codmunic',3,0,0,3); --10354 eiel_indicadores_d_captaciones.codmunic character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'codentidad',4,0,0,3); --10354 eiel_indicadores_d_captaciones.codentidad character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'codpoblamiento',2,0,0,3); --10354 eiel_indicadores_d_captaciones.codpoblamiento character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numn_caudalsuf',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numn_caudalsuf numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numn_caudalins',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numn_caudalins numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numhab_cauldalsuf',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numhab_cauldalsuf numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numhab_caudalins',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numhab_caudalins numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numest_caudalsuf',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numest_caudalsuf numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numest_caudalins',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numest_caudalins numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numn_restricf',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numn_restricf numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numn_restricm',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numn_restricm numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numn_restricn',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numn_restricn numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numhab_restricf',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numhab_restricf numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numhab_restricm',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numhab_restricm numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numhab_resticn',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numhab_resticn numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numest_restricf',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numest_restricf numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numest_restricm',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numest_restricm numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'numest_restricn',0,6,0,2); --10354 eiel_indicadores_d_captaciones.numest_restricn numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'ncaptaciones',0,6,0,2); --10354 eiel_indicadores_d_captaciones.ncaptaciones numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'ncaptacionesb',0,6,0,2); --10354 eiel_indicadores_d_captaciones.ncaptacionesb numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'ncaptacionesr',0,6,0,2); --10354 eiel_indicadores_d_captaciones.ncaptacionesr numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),currval('seq_tables'),null,'ncaptacionesm',0,6,0,2); --10354 eiel_indicadores_d_captaciones.ncaptacionesm numeric



-- 5 Insercion dictionary, attributes
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='GEOMETRY'),1,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='id'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','toponimo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='toponimo'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='id_municipio'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='codprov'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='codentidad'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='codmunic'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='codpoblamiento'),8,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Núm. de captaciones');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='ncaptaciones'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Núm captaciones estado Bueno');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='ncaptacionesb'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Núm. captaciones estado Malo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='ncaptacionesm'),11,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Núm captaciones estado Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='ncaptacionesr'),12,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional caudal insuf.');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_caudalins'),13,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional caudal suf.');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_caudalsuf'),14,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional restricciones Fuertes');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_restricf'),15,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional restricciones Medias');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_restricm'),16,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional sin restricciones');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_restricn'),17,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. caudal insuficiente');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_caudalins'),18,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. caudal suficiente');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_cauldalsuf'),19,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. sin restricciones');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_resticn'),20,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. con restricciones fuertes');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_restricf'),21,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. con restricciones medias');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_restricm'),22,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Núm. núcleos caudal insuficiente');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numn_caudalins'),23,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Núm. núcleos caudal suficiente');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numn_caudalsuf'),24,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Núm. núcleos restricciones fuertes');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numn_restricf'),25,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Núm. núcleos restricciones medias');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numn_restricm'),26,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Núm. núcleos sin restricciones');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numn_restricn'),27,true);


-- ## Capa Restricciones ##


-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "

insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_II_Captaciones_Restricciones</Name><UserStyle><Name>EIEL_Indicadores_II_Captaciones_Restricciones:_:default:EIEL_Indicadores_II_Captaciones_Restricciones</Name><Title>EIEL_Indicadores_II_Captaciones_Restricciones:_:default:EIEL_Indicadores_II_Captaciones_Restricciones</Title><Abstract>EIEL_Indicadores_II_Captaciones_Restricciones:_:default:EIEL_Indicadores_II_Captaciones_Restricciones</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsGreaterThan><ogc:PropertyName>Pob. sin restricciones</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsGreaterThan></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.99999999999E11</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill-opacity">1.0</CssParameter><CssParameter name="fill">#66ff00</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>');

 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Captaciones_Restricciones');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) 
   VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Indicadores_II_Captaciones_Restricciones',1,0);

-- 2b Creación layerfamily
-- N/A

-- 2c -- Asociacion de layer-layerfamily, map
      -- Revisar nombre mapa
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (
(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Captaciones'),
currval('seq_layers'),1);

--aqui
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) 
values (currval('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'EIEL_Indicadores_II_Captaciones_Restricciones:_:default:EIEL_Indicadores_II_Captaciones_Restricciones', true,1,0,true,true);


-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT transform("eiel_indicadores_d_captaciones"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_captaciones"."id","eiel_indicadores_d_captaciones"."toponimo","eiel_indicadores_d_captaciones"."id_municipio","eiel_indicadores_d_captaciones"."codprov","eiel_indicadores_d_captaciones"."codentidad","eiel_indicadores_d_captaciones"."codmunic","eiel_indicadores_d_captaciones"."codpoblamiento","eiel_indicadores_d_captaciones"."numhab_caudalins","eiel_indicadores_d_captaciones"."numhab_cauldalsuf","eiel_indicadores_d_captaciones"."numhab_resticn","eiel_indicadores_d_captaciones"."numhab_restricf","eiel_indicadores_d_captaciones"."numhab_restricm","eiel_indicadores_d_captaciones"."numest_caudalins","eiel_indicadores_d_captaciones"."numest_caudalsuf","eiel_indicadores_d_captaciones"."numest_restricn","eiel_indicadores_d_captaciones"."numest_restricf","eiel_indicadores_d_captaciones"."numest_restricm" FROM "eiel_indicadores_d_captaciones" WHERE "eiel_indicadores_d_captaciones"."id_municipio"  in (?M)',
'UPDATE "eiel_indicadores_d_captaciones" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"toponimo"=?3,"id_municipio"=?4,"codprov"=?5,"codentidad"=?6,"codmunic"=?7,"codpoblamiento"=?8,"numhab_caudalins"=?9,"numhab_cauldalsuf"=?10,"numhab_resticn"=?11,"numhab_restricf"=?12,"numhab_restricm"=?13,"numest_caudalins"=?14,"numest_caudalsuf"=?15,"numest_restricn"=?16,"numest_restricf"=?17,"numest_restricm"=?18 WHERE "id_municipio"=?4',
'INSERT INTO "eiel_indicadores_d_captaciones" ("GEOMETRY","id","toponimo","id_municipio","codprov","codentidad","codmunic","codpoblamiento","numhab_caudalins","numhab_cauldalsuf","numhab_resticn","numhab_restricf","numhab_restricm","numest_caudalins","numest_caudalsuf","numest_restricn","numest_restricf","numest_restricm") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?2,?3,?PK,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18)',
'DELETE FROM "eiel_indicadores_d_captaciones" WHERE "id_municipio"=?4');


-- 4a Insercion Table
--N/A
-- 4b Creación columnas
--N/A

-- 5 Insercion dictionary, attributes
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='GEOMETRY'),1,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='id'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','toponimo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='toponimo'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='id_municipio'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='codprov'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='codentidad'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='codmunic'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='codpoblamiento'),8,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. caudal insuficiente');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_caudalins'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. caudal suficiente');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_cauldalsuf'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. sin restricciones');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_resticn'),11,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. restricciones fuertes');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_restricf'),12,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. restricciones medias');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numhab_restricm'),13,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional caudal insuficiente');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_caudalins'),14,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional caudal suficiente');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_caudalsuf'),15,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional sin restricciones');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_restricn'),16,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional con restricciones fuertes');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_restricf'),17,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional con restricciones medias');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_captaciones' and c.name='numest_restricm'),18,true);


INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name = 'CA'),2);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='NP'),3);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='parroquias'),4);

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'CA'),(select id_styles from layers where name = 'CA'),'CA:_:EIEL: CA', true,2,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'NP'),(select id_styles from layers where name = 'NP'),'NP:_:EIEL: NP', true,3,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'parroquias'),(select id_styles from layers where name = 'parroquias'),'default:parroquias', true,4,0,true,true);
