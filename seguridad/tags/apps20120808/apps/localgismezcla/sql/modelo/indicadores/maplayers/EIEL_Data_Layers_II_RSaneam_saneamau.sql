/*
 LocalGIS Layer Export v0.1 20111122
 SQL para generar los SQL de creación de capas a partir de una instalación existente (Ej. para crear SQL de creación de capas a partir de capas ya creadas en entorno DEV)
 No contempla la creación de mapas ni familias de capas

 
*/

-- 0 Mapa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(35,CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_II_Red de saneamiento</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa de Cementerios</mapName></mapDescriptor>',0);




-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "

INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Ind_II_RSaneam_saneamau</Name><UserStyle><Name>EIEL_Ind_II_RSaneam_saneamau:_:default:EIEL_Ind_II_RSaneam_saneamau</Name><Title>EIEL_Ind_II_RSaneam_saneamau:_:default:EIEL_Ind_II_RSaneam_saneamau</Title><Abstract>EIEL_Ind_II_RSaneam_saneamau:_:default:EIEL_Ind_II_RSaneam_saneamau</Abstract><FeatureTypeStyle><Name>EIEL_Ind_II_RSaneam_saneamau</Name><Rule><Name>EIEL_Ind_R_II_Rsaneam_nhabsaneamau 0</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>nhab_saneamautonomo</ogc:PropertyName><ogc:Literal>1</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>nhab_saneamautonomo</ogc:PropertyName><ogc:Literal>15</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#ffffcc</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>EIEL_Ind_R_II_Rsaneam_nhabsaneamau 1</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>nhab_saneamautonomo</ogc:PropertyName><ogc:Literal>15</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>nhab_saneamautonomo</ogc:PropertyName><ogc:Literal>22</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#ffcc99</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>EIEL_Ind_R_II_Rsaneam_nhabsaneamau 2</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>nhab_saneamautonomo</ogc:PropertyName><ogc:Literal>22</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>nhab_saneamautonomo</ogc:PropertyName><ogc:Literal>30</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#ff9966</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>EIEL_Ind_R_II_Rsaneam_nhabsaneamau 3</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>nhab_saneamautonomo</ogc:PropertyName><ogc:Literal>30</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>nhab_saneamautonomo</ogc:PropertyName><ogc:Literal>90</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#ff3333</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>');

 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Ind_II_RSaneam_saneamau');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) 
   VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Ind_II_RSaneam_saneamau',1,0);

-- 2b Creación layerfamily
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

-- 2c -- Asociacion de layer-layerfamily, map
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),1);


INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps m, dictionary d where m.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Red de saneamiento'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='EIEL PARROQUIAS'),1,0);
INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps m, dictionary d where m.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Red de saneamiento'),currval('seq_layerfamilies'),1,0);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps m, dictionary d where m.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Red de saneamiento'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:EIEL_Indicadores_II_Red de saneamiento', true,0,0,true,true);

-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT "eiel_indicadores_d_rsaneam"."codentidad","eiel_indicadores_d_rsaneam"."codmunic","eiel_indicadores_d_rsaneam"."codpoblamiento","eiel_indicadores_d_rsaneam"."codprov",transform("eiel_indicadores_d_rsaneam"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_rsaneam"."id","eiel_indicadores_d_rsaneam"."id_municipio","eiel_indicadores_d_rsaneam"."nest_saneamautonomo","eiel_indicadores_d_rsaneam"."nest_saneamautonomo_in","eiel_indicadores_d_rsaneam"."nhab_saneamautonomo","eiel_indicadores_d_rsaneam"."nhab_saneamautonomo_in","eiel_indicadores_d_rsaneam"."nviv_saneamautonomo","eiel_indicadores_d_rsaneam"."nviv_saneamautonomo_in" FROM "eiel_indicadores_d_rsaneam" WHERE "eiel_indicadores_d_rsaneam"."id_municipio" IN (?M)',
'UPDATE "eiel_indicadores_d_rsaneam" SET "codentidad"=?1,"codmunic"=?2,"codpoblamiento"=?3,"codprov"=?4,"GEOMETRY"=transform(GeometryFromText(text(?5),?S), ?T),"id"=?6,"id_municipio" = ?M,"nest_saneamautonomo"=?8,"nest_saneamautonomo_in"=?9,"nhab_saneamautonomo"=?10,"nhab_saneamautonomo_in"=?11,"nviv_saneamautonomo"=?12,"nviv_saneamautonomo_in"=?13 WHERE "id"=?6',
'INSERT INTO "eiel_indicadores_d_rsaneam" ("codentidad","codmunic","codpoblamiento","codprov","GEOMETRY","id","id_municipio","nest_saneamautonomo","nest_saneamautonomo_in","nhab_saneamautonomo","nhab_saneamautonomo_in","nviv_saneamautonomo","nviv_saneamautonomo_in") VALUES(?1,?2,?3,?4,transform(GeometryFromText(text(?5),?S), ?T),?PK,?M,?8,?9,?10,?11,?12,?13)',
'DELETE FROM "eiel_indicadores_d_rsaneam" WHERE "id"=?6');


-- 4 Inserccion Table
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_rsaneam',5);

-- 5a Inserccion dictionary, columns y attributes
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'codentidad',4,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'codmunic',3,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'codpoblamiento',2,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'codprov',2,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'GEOMETRY',0,0,0,1);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'id',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'id_municipio',5,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nest_saneamautonomo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'nest_saneamautonomo',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nest_saneamautonomo_in');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'nest_saneamautonomo_in',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhab_saneamautonomo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'nhab_saneamautonomo',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhab_saneamautonomo_in');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'nhab_saneamautonomo_in',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),11,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nviv_saneamautonomo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'nviv_saneamautonomo',0,6,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),12,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nviv_saneamautonomo_in');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'nviv_saneamautonomo_in',0,6,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),13,true);


-- ### Capa "EIEL_Ind_II_RSaneam_EstadoRed" ###


-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "

INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Ind_II_RSaneam_EstadoRed</Name><UserStyle><Name>EIEL_Ind_II_RSaneam_EstadoRed:_:default:EIEL_Ind_II_RSaneam_EstadoRed</Name><Title>EIEL_Ind_II_RSaneam_EstadoRed:_:default:EIEL_Ind_II_RSaneam_EstadoRed</Title><Abstract>EIEL_Ind_II_RSaneam_EstadoRed:_:default:EIEL_Ind_II_RSaneam_EstadoRed</Abstract><FeatureTypeStyle><Name>EIEL_Ind_II_RSaneam_EstadoRed</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsGreaterThan><ogc:PropertyName>rs_longitud_estado_r</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsGreaterThan></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#ffcc66</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThan><ogc:PropertyName>rs_longitud_estado_b</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsGreaterThan><ogc:PropertyIsGreaterThan><ogc:PropertyName>rs_longitud_estado_b</ogc:PropertyName><ogc:PropertyName>rs_longitud_estado_r</ogc:PropertyName></ogc:PropertyIsGreaterThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#009900</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsGreaterThan><ogc:PropertyName>rs_longitud_estado_m</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsGreaterThan></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="file:iconlib/123.gif"/><Format>image/gif</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>16.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Ind_II_RSaneam_EstadoRed');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) 
   VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Ind_II_RSaneam_EstadoRed',1,0);

-- 2b Creación layerfamily
   --N/A
-- 2c -- Asociacion de layer-layerfamily, map
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values ((SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Red de saneamiento'),currval('seq_layers'),1);

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Red de saneamiento'),currval('seq_layers'),currval('seq_styles'),'default:EIEL_Indicadores_II_Red de saneamiento_EstadoRed', true,0,0,true,true);

-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT transform("eiel_indicadores_d_rsaneam"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_rsaneam"."id","eiel_indicadores_d_rsaneam"."toponimo","eiel_indicadores_d_rsaneam"."id_municipio","eiel_indicadores_d_rsaneam"."codprov","eiel_indicadores_d_rsaneam"."codmunic","eiel_indicadores_d_rsaneam"."codpoblamiento","eiel_indicadores_d_rsaneam"."codentidad","eiel_indicadores_d_rsaneam"."nhab_calidadred_b","eiel_indicadores_d_rsaneam"."nhab_calidadred_ma","eiel_indicadores_d_rsaneam"."nhab_calidadred_r","eiel_indicadores_d_rsaneam"."nest_calidadred_b","eiel_indicadores_d_rsaneam"."nest_calidadred_m","eiel_indicadores_d_rsaneam"."nest_calidadred_r","eiel_indicadores_d_rsaneam"."nviv_saneam_no","eiel_indicadores_d_rsaneam"."rs_longitud_estado_b","eiel_indicadores_d_rsaneam"."rs_longitud_estado_m","eiel_indicadores_d_rsaneam"."rs_longitud_estado_r","eiel_indicadores_d_rsaneam"."saneam_viviendas_con_conexion","eiel_indicadores_d_rsaneam"."saneam_viviendas_sin_conexion" FROM "eiel_indicadores_d_rsaneam" WHERE "eiel_indicadores_d_rsaneam"."id_municipio" IN (?M)',
'UPDATE "eiel_indicadores_d_rsaneam" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"toponimo"=?3,"id_municipio" = ?M,"codprov"=?5,"codmunic"=?6,"codpoblamiento"=?7,"codentidad"=?8,"nhab_calidadred_b"=?9,"nhab_calidadred_ma"=?10,"nhab_calidadred_r"=?11,"nest_calidadred_b"=?12,"nest_calidadred_m"=?13,"nest_calidadred_r"=?14,"nviv_saneam_no"=?15,"rs_longitud_estado_b"=?16,"rs_longitud_estado_m"=?17,"rs_longitud_estado_r"=?18,"saneam_viviendas_con_conexion"=?19,"saneam_viviendas_sin_conexion"=?20 WHERE "id"=?2',
'INSERT INTO "eiel_indicadores_d_rsaneam" ("GEOMETRY","id","toponimo","id_municipio","codprov","codmunic","codpoblamiento","codentidad","nhab_calidadred_b","nhab_calidadred_ma","nhab_calidadred_r","nest_calidadred_b","nest_calidadred_m","nest_calidadred_r","nviv_saneam_no","rs_longitud_estado_b","rs_longitud_estado_m","rs_longitud_estado_r","saneam_viviendas_con_conexion","saneam_viviendas_sin_conexion") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?M,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20)',
'DELETE FROM "eiel_indicadores_d_rsaneam" WHERE "id"=?2');

-- 4 Inserccion Table
 --N/A


-- 5b Atributos de la capa si ya se han creado antes las columnas y las entradas de diccionario

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='codentidad'),1,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='codmunic'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='codpoblamiento'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='codprov'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='GEOMETRY'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='id'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='id_municipio'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nest_saneamautonomo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='nest_saneamautonomo'),8,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nest_saneamautonomo_in');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='nest_saneamautonomo_in'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhab_saneamautonomo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='nhab_saneamautonomo'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhab_saneamautonomo_in');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='nhab_saneamautonomo_in'),11,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nviv_saneamautonomo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='nviv_saneamautonomo'),12,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nviv_saneamautonomo_in');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_poblacion' and c.name='nviv_saneamautonomo_in'),13,true);

