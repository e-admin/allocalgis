/*
 LocalGIS Layer Export v0.1 20111122
 SQL para generar los SQL de creaci�n de capas a partir de una instalaci�n existente (Ej. para crear SQL de creaci�n de capas a partir de capas ya creadas en entorno DEV)

 

 Sustituciones
 EIEL_Indicadores_II_RedDistribucion
 EIEL_Indicadores_II_RedDistribucion
 EIEL_Indicadores_II_RedDistribucion
 eiel_indicadores_d_rdistribucion
 
*/

-- 0 Mapa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_II_RedDistribucion</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_II_RedDistribucion</mapName></mapDescriptor>',0);

/*
select * from maps where xml like '%RedDistribucion%'
select * from layers_styles where id_map=535

*/

-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "
 --Estilo por defecto
INSERT INTO STYLES(ID_STYLE,XML) VALUES(NEXTVAL('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_II_RedDistribucion</Name><UserStyle><Name>EIEL_Indicadores_II_RedDistribucion:_:default:EIEL_Indicadores_II_RedDistribucion</Name><Title>EIEL_Indicadores_II_RedDistribucion:_:default:EIEL_Indicadores_II_RedDistribucion</Title><Abstract>EIEL_Indicadores_II_RedDistribucion:_:default:EIEL_Indicadores_II_RedDistribucion</Abstract><FeatureTypeStyle><Name>EIEL_Indicadores_II_RedDistribucion</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsGreaterThan><ogc:PropertyName>Longitud red estado Regular</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsGreaterThan></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill-opacity">0.6</CssParameter><CssParameter name="fill">#ffcc33</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:PropertyIsGreaterThan><ogc:PropertyName>Longitud red estado Malo</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsGreaterThan></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill-opacity">0.6</CssParameter><CssParameter name="fill">#ff3300</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThan><ogc:PropertyName>Longitud red estado Bueno</ogc:PropertyName><ogc:PropertyName>Longitud red estado Regular</ogc:PropertyName></ogc:PropertyIsGreaterThan><ogc:PropertyIsGreaterThan><ogc:PropertyName>Longitud red estado Bueno</ogc:PropertyName><ogc:PropertyName>Longitud red estado Malo</ogc:PropertyName></ogc:PropertyIsGreaterThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill-opacity">0.61</CssParameter><CssParameter name="fill">#33cc00</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name="fill-opacity">1.0</CssParameter><CssParameter name="fill">#ffffff</CssParameter></Fill><Stroke><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name="stroke-linecap">round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>'); 
-- 2a Creaci�n de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_RedDistribucion');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Indicadores_II_RedDistribucion',1,0);


-- 2b Creaci�n layerfamily
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

-- 2c -- Asociacion de layer-layerfamily, map
      -- Revisar nombre mapa
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),0);
       --Elementos EIEL relacionado con el indicador 

   

INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps m, dictionary d where m.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_RedDistribucion'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_RedDistribucion'),1,0);

   --  Incluir estilo en layer_styles por cada capa en la familia

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps m, dictionary d where m.id_entidad=0 and m.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_RedDistribucion'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_RedDistribucion'),currval('seq_layers'),currval('seq_styles'),'EIEL_Indicadores_II_RedDistribucion:_:default:EIEL_Indicadores_II_RedDistribucion', true,0,0,true,true);


-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT transform("eiel_indicadores_d_rdistribucion"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_rdistribucion"."id","eiel_indicadores_d_rdistribucion"."toponimo","eiel_indicadores_d_rdistribucion"."id_municipio","eiel_indicadores_d_rdistribucion"."codprov","eiel_indicadores_d_rdistribucion"."codmunic","eiel_indicadores_d_rdistribucion"."codentidad","eiel_indicadores_d_rdistribucion"."codpoblamiento","eiel_indicadores_d_rdistribucion"."habitantescalidadrd_b","eiel_indicadores_d_rdistribucion"."habitantescalidadrd_m","eiel_indicadores_d_rdistribucion"."habitantescalidadrd_r","eiel_indicadores_d_rdistribucion"."numnucleos","eiel_indicadores_d_rdistribucion"."numnucleoscalidadrd_b","eiel_indicadores_d_rdistribucion"."numnucleoscalidadrd_m","eiel_indicadores_d_rdistribucion"."numnucleoscalidadrd_r","eiel_indicadores_d_rdistribucion"."pobestacionalcalidadrd_b","eiel_indicadores_d_rdistribucion"."pobestacionalcalidadrd_m","eiel_indicadores_d_rdistribucion"."pobestacionalcalidadrd_r","eiel_indicadores_d_rdistribucion"."rdistrib_habitantes_au","eiel_indicadores_d_rdistribucion"."rdistrib_pobest_au","eiel_indicadores_d_rdistribucion"."rdistrib_viviendas_au","eiel_indicadores_d_rdistribucion"."rdistrib_viviendas_sin_conexion","eiel_indicadores_d_rdistribucion"."rd_longitud_estado_b","eiel_indicadores_d_rdistribucion"."rd_longitud_estado_m","eiel_indicadores_d_rdistribucion"."rd_longitud_estado_r","eiel_indicadores_d_rdistribucion"."consumoinvierno","eiel_indicadores_d_rdistribucion"."consumoverano" FROM "eiel_indicadores_d_rdistribucion" WHERE "eiel_indicadores_d_rdistribucion"."id_municipio" IN (?M)',
'UPDATE "eiel_indicadores_d_rdistribucion" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"toponimo"=?3,"id_municipio" = ?M,"codprov"=?5,"codmunic"=?6,"codentidad"=?7,"codpoblamiento"=?8,"habitantescalidadrd_b"=?9,"habitantescalidadrd_m"=?10,"habitantescalidadrd_r"=?11,"numnucleos"=?12,"numnucleoscalidadrd_b"=?13,"numnucleoscalidadrd_m"=?14,"numnucleoscalidadrd_r"=?15,"pobestacionalcalidadrd_b"=?16,"pobestacionalcalidadrd_m"=?17,"pobestacionalcalidadrd_r"=?18,"rdistrib_habitantes_au"=?19,"rdistrib_pobest_au"=?20,"rdistrib_viviendas_au"=?21,"rdistrib_viviendas_sin_conexion"=?22,"rd_longitud_estado_b"=?23,"rd_longitud_estado_m"=?24,"rd_longitud_estado_r"=?25,"consumoinvierno"=?26,"consumoverano"=?27 WHERE "id"=?2',
'INSERT INTO "eiel_indicadores_d_rdistribucion" ("GEOMETRY","id","toponimo","id_municipio","codprov","codmunic","codentidad","codpoblamiento","habitantescalidadrd_b","habitantescalidadrd_m","habitantescalidadrd_r","numnucleos","numnucleoscalidadrd_b","numnucleoscalidadrd_m","numnucleoscalidadrd_r","pobestacionalcalidadrd_b","pobestacionalcalidadrd_m","pobestacionalcalidadrd_r","rdistrib_habitantes_au","rdistrib_pobest_au","rdistrib_viviendas_au","rdistrib_viviendas_sin_conexion","rd_longitud_estado_b","rd_longitud_estado_m","rd_longitud_estado_r","consumoinvierno","consumoverano") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?M,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?25,?26,?27)',
'DELETE FROM "eiel_indicadores_d_rdistribucion" WHERE "id"=?2');


-- 4a Inserccion Table
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_rdistribucion',5);
-- 4b Creaci�n columnas
     --A�adir COLUMN_DOMAINS donde corresponda
     --INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
     --id_domain 10067:id 10068:id_municipio
     
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'id',0,32,0,2); --10184 eiel_indicadores_d_rdistribucion.id integer"
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'GEOMETRY',0,0,0,1); --10184 eiel_indicadores_d_rdistribucion.GEOMETRY USER-DEFINED"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'id_municipio',5,0,0,3); --10184 eiel_indicadores_d_rdistribucion.id_municipio character varying"
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'toponimo',50,0,0,3); --10184 eiel_indicadores_d_rdistribucion.toponimo character varying"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codprov',2,0,0,3); --10184 eiel_indicadores_d_rdistribucion.codprov character varying"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codmunic',3,0,0,3); --10184 eiel_indicadores_d_rdistribucion.codmunic character varying"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codentidad',4,0,0,3); --10184 eiel_indicadores_d_rdistribucion.codentidad character varying"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codpoblamiento',2,0,0,3); --10184 eiel_indicadores_d_rdistribucion.codpoblamiento character varying"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'numnucleos',0,64,0,2); --10184 eiel_indicadores_d_rdistribucion.numnucleos bigint"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'numnucleoscalidadrd_b',0,64,0,2); --10184 eiel_indicadores_d_rdistribucion.numnucleoscalidadrd_b bigint"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'numnucleoscalidadrd_r',0,64,0,2); --10184 eiel_indicadores_d_rdistribucion.numnucleoscalidadrd_r bigint"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'numnucleoscalidadrd_m',0,64,0,2); --10184 eiel_indicadores_d_rdistribucion.numnucleoscalidadrd_m bigint"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'habitantescalidadrd_b',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.habitantescalidadrd_b numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'habitantescalidadrd_r',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.habitantescalidadrd_r numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'habitantescalidadrd_m',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.habitantescalidadrd_m numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'pobestacionalcalidadrd_b',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.pobestacionalcalidadrd_b numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'pobestacionalcalidadrd_r',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.pobestacionalcalidadrd_r numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'pobestacionalcalidadrd_m',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.pobestacionalcalidadrd_m numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'rd_longitud_estado_b',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.rd_longitud_estado_b numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'rd_longitud_estado_r',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.rd_longitud_estado_r numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'rd_longitud_estado_m',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.rd_longitud_estado_m numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'consumoinvierno',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.consumoinvierno numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'consumoverano',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.consumoverano numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'rdistrib_viviendas_sin_conexion',0,64,0,2); --10184 eiel_indicadores_d_rdistribucion.rdistrib_viviendas_sin_conexion bigint"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'rdistrib_viviendas_au',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.rdistrib_viviendas_au numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'rdistrib_habitantes_au',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.rdistrib_habitantes_au numeric"
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'rdistrib_pobest_au',0,0,0,2); --10184 eiel_indicadores_d_rdistribucion.rdistrib_pobest_au numeric"



INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='GEOMETRY'),1,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='id'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','toponimo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='toponimo'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='id_municipio'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='codprov'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='codmunic'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='codentidad'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='codpoblamiento'),8,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Hab. calidad red Buena');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='habitantescalidadrd_b'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Hab. calidad red Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='habitantescalidadrd_r'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Hab. calidad red Mala');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='habitantescalidadrd_m'),11,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='numnucleos'),12,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos calidad red Buena');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='numnucleoscalidadrd_b'),13,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos calidad red Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='numnucleoscalidadrd_r'),14,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos calidad red Mala');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='numnucleoscalidadrd_m'),15,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional calidad red Buena');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='pobestacionalcalidadrd_b'),16,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional calidad red Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='pobestacionalcalidadrd_r'),17,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional calidad red Mala');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='pobestacionalcalidadrd_m'),18,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. red distribucion autonoma');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='rdistrib_habitantes_au'),19,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional red distribucion autonoma');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='rdistrib_pobest_au'),20,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N viviendas red distribucion autonoma');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='rdistrib_viviendas_au'),21,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Viviendas sin conexion a red');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='rdistrib_viviendas_sin_conexion'),22,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud red estado Bueno');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='rd_longitud_estado_b'),23,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud red estado Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='rd_longitud_estado_r'),24,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Longitud red estado Malo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='rd_longitud_estado_m'),25,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Consumo en invierno');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='consumoinvierno'),26,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Consumo en verano');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rdistribucion' and c.name='consumoverano'),27,true);


INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name = 'CA'),1);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name = 'TCN'),2);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name = 'RD'),3);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name = 'AR'),4);
       --N?cleos de poblaci?n con top?nimo
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='NP'),5);
       --L?mites parroquias
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='parroquias'),6);

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'CA'),(select id_styles from layers where name = 'CA'),'CA:_:EIEL: CA', true,1,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'TCN'),(select id_styles from layers where name ='TCN'),'TCN:_:EIEL:TCN', true,2,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'RD'),(select id_styles from layers where name = 'RD'),'RD:_:EIEL: RD', true,3,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'AR'),(select id_styles from layers where name = 'AR'),'AR:_:EIEL: AR', true,4,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'NP'),(select id_styles from layers where name = 'NP'),'NP:_:EIEL: NP', true,5,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'parroquias'),(select id_styles from layers where name = 'parroquias'),'default:parroquias', true,6,0,true,true);

