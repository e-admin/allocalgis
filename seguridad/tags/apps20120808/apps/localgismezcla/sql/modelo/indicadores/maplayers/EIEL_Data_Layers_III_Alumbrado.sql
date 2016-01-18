/*
 LocalGIS Layer Export v0.1 20111122
 SQL para generar los SQL de creaciÃ³n de capas a partir de una instalaciÃ³n existente (Ej. para crear SQL de creaciÃ³n de capas a partir de capas ya creadas en entorno DEV)
 No contempla la creaciÃ³n de mapas ni familias de capas

 
*/

-- 0 Mapa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_III_Alumbrado');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_III_Alumbrado</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_III_Alumbrado</mapName></mapDescriptor>',0);



-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "

INSERT INTO STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_III_Alumbrado_Potencia</Name><UserStyle><Name>EIEL_Indicadores_III_Alumbrado_Potencia:_:default:EIEL_Indicadores_III_Alumbrado_Potencia</Name><Title>EIEL_Indicadores_III_Alumbrado_Potencia:_:default:EIEL_Indicadores_III_Alumbrado_Potencia</Title><Abstract>EIEL_Indicadores_III_Alumbrado_Potencia:_:default:EIEL_Indicadores_III_Alumbrado_Potencia</Abstract><FeatureTypeStyle><Name>EIEL_Indicadores_III_Alumbrado_Potencia</Name><Rule><Name>EIEL_Ind_R_III_Alumbrado_potencia 0</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>potenciapuntosluz</ogc:PropertyName><ogc:Literal>0.00</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>potenciapuntosluz</ogc:PropertyName><ogc:Literal>0.02</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#b9c040</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>EIEL_Ind_R_III_Alumbrado_potencia 1</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>potenciapuntosluz</ogc:PropertyName><ogc:Literal>0.02</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>potenciapuntosluz</ogc:PropertyName><ogc:Literal>0.06</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#abc075</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>EIEL_Ind_R_III_Alumbrado_potencia 2</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>potenciapuntosluz</ogc:PropertyName><ogc:Literal>0.06</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>potenciapuntosluz</ogc:PropertyName><ogc:Literal>0.40</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#9dc0aa</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>EIEL_Ind_R_III_Alumbrado_potencia 3</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>potenciapuntosluz</ogc:PropertyName><ogc:Literal>0.40</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>potenciapuntosluz</ogc:PropertyName><ogc:Literal>5.71</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#8fc0df</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
');

 
-- 2 CreaciÃ³n de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Alumbrado_Potencia');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) 
   VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Indicadores_III_Alumbrado_Potencia',1,0);

-- Creacion de layerfamily 
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Infraestructuras_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_III_Infraestructuras_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_III_Infraestructuras_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_III_Infraestructuras_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_III_Infraestructuras_Alumbrado');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );



-- 2c -- Asociacion de layer-layerfamily, map
INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),currval('seq_layerfamilies'),0,0);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),0);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) 
values (currval('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'), 
'EIEL_Indicadores_III_Alumbrado_Potencia:_:default:EIEL_Indicadores_III_Alumbrado_Potencia', true,0,0,true,true);

-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT transform("eiel_indicadores_d_poblacion"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_poblacion"."id","eiel_indicadores_d_poblacion"."toponimo","eiel_indicadores_d_poblacion"."id_municipio","eiel_indicadores_d_poblacion"."codprov","eiel_indicadores_d_poblacion"."codmunic","eiel_indicadores_d_poblacion"."codentidad","eiel_indicadores_d_poblacion"."codpoblamiento","eiel_indicadores_d_poblacion"."poblaciondensidad" FROM "eiel_indicadores_d_poblacion" WHERE "eiel_indicadores_d_poblacion"."id_municipio" IN (?M)',
'UPDATE "eiel_indicadores_d_poblacion" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"toponimo"=?3,"id_municipio" = ?M,"codprov"=?5,"codmunic"=?6,"codentidad"=?7,"codpoblamiento"=?8,"poblaciondensidad"=?9 WHERE "id"=?2',
'INSERT INTO "eiel_indicadores_d_poblacion" ("GEOMETRY","id","toponimo","id_municipio","codprov","codmunic","codentidad","codpoblamiento","poblaciondensidad") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?M,?5,?6,?7,?8,?9)',
'DELETE FROM "eiel_indicadores_d_poblacion" WHERE "id"=?2');


-- 4 Inserccion Table
--SELECT E'INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval(\'seq_tables\'),\'eiel_indicadores_d_poblacion\',5)' as sqlTableCreate;
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_alumbrado',5);
-- 5a Inserccion dictionary, columns y attributes

INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'GEOMETRY',0,0,0,1);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'id',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','toponimo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'toponimo',50,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'id_municipio',5,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'codprov',2,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'codmunic',3,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'codentidad',4,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'codpoblamiento',2,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','npuntosluz_ahorro_rlf');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'npuntosluz_ahorro_rlf',0,6,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),9,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','npuntosluz_estado_b');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'npuntosluz_estado_b',0,6,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),10,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','npuntosluz_estado_m');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'npuntosluz_estado_m',0,6,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),11,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','npuntosluz_estado_r');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'npuntosluz_estado_r',0,6,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),12,true);
INSERT  INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','potenciapuntosluz');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),0,'potenciapuntosluz',0,9,2,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),13,true);

INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name = 'PL'),1);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='NP'),2);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='parroquias'),3);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'PL'),(select id_styles from layers where name = 'PL'),'PL:_:EIEL: PL', true,1,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'NP'),(select id_styles from layers where name = 'NP'),'NP:_:EIEL: NP', true,2,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'parroquias'),(select id_styles from layers where name = 'parroquias'),'default:parroquias', true,3,0,true,true);
