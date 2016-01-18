/*
 LocalGIS Layer Export v0.1 20111122
 SQL para generar los SQL de creación de capas a partir de una instalación existente (Ej. para crear SQL de creación de capas a partir de capas ya creadas en entorno DEV)

 

 Sustituciones
 EIEL_Indicadores_III_Suministros
 EIEL_Indicadores_III_Suministros
 EIEL_Indicadores_III_Suministros
 eiel_indicadores_d_suministros
 
*/

-- 0 Mapa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_III_Suministros');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_III_Suministros</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_III_Suministros</mapName></mapDescriptor>',0);




-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "
 --Estilo por defecto
INSERT INTO STYLES(ID_STYLE,XML) VALUES(NEXTVAL('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_III_Suministros</Name><UserStyle><Name>EIEL_Indicadores_III_Suministros:_:default</Name><Title>EIEL_Indicadores_III_Suministros:_:default</Title><Abstract>EIEL_Indicadores_III_Suministros:_:default</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsEqualTo><ogc:PropertyName>N nucleos SIN servicio electricidad</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsEqualTo><ogc:PropertyIsEqualTo><ogc:PropertyName>N nucleos servicio electricidad Regular</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsEqualTo><ogc:PropertyIsEqualTo><ogc:PropertyName>N nucleos servicio electricidad Malo</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsEqualTo><ogc:PropertyIsEqualTo><ogc:PropertyName>N nucleos sin servicio gas</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsEqualTo><ogc:PropertyIsEqualTo><ogc:PropertyName>N nucleos servicio gas Regular</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsEqualTo><ogc:PropertyIsEqualTo><ogc:PropertyName>N nucleos servicio gas Malo</ogc:PropertyName><ogc:Literal>0</ogc:Literal></ogc:PropertyIsEqualTo></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#00cc00</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>'); 

 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Suministros');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Indicadores_III_Suministros',1,0);



-- 2b Creación layerfamily
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_III_Suministros');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );



-- 2c -- Asociacion de layer-layerfamily, map
      -- Revisar nombre mapa
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),0);
   

INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),currval('seq_layerfamilies'),0,0);

   --  Incluir estilo en layer_styles por cada capa en la familia

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'EIEL_Indicadores_III_Suministros:_:default', true,0,0,true,true);


-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT "eiel_indicadores_d_suministros"."id",transform("eiel_indicadores_d_suministros"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_suministros"."id_municipio","eiel_indicadores_d_suministros"."toponimo","eiel_indicadores_d_suministros"."codprov","eiel_indicadores_d_suministros"."codmunic","eiel_indicadores_d_suministros"."codentidad","eiel_indicadores_d_suministros"."codpoblamiento","eiel_indicadores_d_suministros"."nnuc_serv_elec_b","eiel_indicadores_d_suministros"."nnuc_serv_elec_r","eiel_indicadores_d_suministros"."nnuc_serv_elec_m","eiel_indicadores_d_suministros"."nnuc_serv_elec_n","eiel_indicadores_d_suministros"."nhab_serv_elec_b","eiel_indicadores_d_suministros"."nhab_serv_elec_r","eiel_indicadores_d_suministros"."nhab_serv_elec_m","eiel_indicadores_d_suministros"."nhab_serv_elec_n","eiel_indicadores_d_suministros"."nest_serv_elec_b","eiel_indicadores_d_suministros"."nest_serv_elect_r","eiel_indicadores_d_suministros"."nest_serv_elec_m","eiel_indicadores_d_suministros"."nest_serv_elec_n","eiel_indicadores_d_suministros"."nnuc_serv_gas_b","eiel_indicadores_d_suministros"."nnuc_serv_gas_r","eiel_indicadores_d_suministros"."nnuc_serv_gas_m","eiel_indicadores_d_suministros"."nnuc_serv_gas_n","eiel_indicadores_d_suministros"."nhab_serv_gas_b","eiel_indicadores_d_suministros"."nhab_serv_gas_r","eiel_indicadores_d_suministros"."nhab_serv_gas_m","eiel_indicadores_d_suministros"."nhab_serv_gas_n","eiel_indicadores_d_suministros"."nest_serv_gas_b","eiel_indicadores_d_suministros"."nest_serv_gas_r","eiel_indicadores_d_suministros"."nest_serv_gas_m","eiel_indicadores_d_suministros"."nest_serv_gas_n" FROM "eiel_indicadores_d_suministros" WHERE "eiel_indicadores_d_suministros"."id_municipio" IN (?M)',
'UPDATE "eiel_indicadores_d_suministros" SET "id"=?1,"GEOMETRY"=transform(GeometryFromText(text(?2),?S), ?T),"id_municipio" = ?M,"toponimo"=?4,"codprov"=?5,"codmunic"=?6,"codentidad"=?7,"codpoblamiento"=?8,"nnuc_serv_elec_b"=?9,"nnuc_serv_elec_r"=?10,"nnuc_serv_elec_m"=?11,"nnuc_serv_elec_n"=?12,"nhab_serv_elec_b"=?13,"nhab_serv_elec_r"=?14,"nhab_serv_elec_m"=?15,"nhab_serv_elec_n"=?16,"nest_serv_elec_b"=?17,"nest_serv_elect_r"=?18,"nest_serv_elec_m"=?19,"nest_serv_elec_n"=?20,"nnuc_serv_gas_b"=?21,"nnuc_serv_gas_r"=?22,"nnuc_serv_gas_m"=?23,"nnuc_serv_gas_n"=?24,"nhab_serv_gas_b"=?25,"nhab_serv_gas_r"=?26,"nhab_serv_gas_m"=?27,"nhab_serv_gas_n"=?28,"nest_serv_gas_b"=?29,"nest_serv_gas_r"=?30,"nest_serv_gas_m"=?31,"nest_serv_gas_n"=?32 WHERE "id"=?1',
'INSERT INTO "eiel_indicadores_d_suministros" ("id","GEOMETRY","id_municipio","toponimo","codprov","codmunic","codentidad","codpoblamiento","nnuc_serv_elec_b","nnuc_serv_elec_r","nnuc_serv_elec_m","nnuc_serv_elec_n","nhab_serv_elec_b","nhab_serv_elec_r","nhab_serv_elec_m","nhab_serv_elec_n","nest_serv_elec_b","nest_serv_elect_r","nest_serv_elec_m","nest_serv_elec_n","nnuc_serv_gas_b","nnuc_serv_gas_r","nnuc_serv_gas_m","nnuc_serv_gas_n","nhab_serv_gas_b","nhab_serv_gas_r","nhab_serv_gas_m","nhab_serv_gas_n","nest_serv_gas_b","nest_serv_gas_r","nest_serv_gas_m","nest_serv_gas_n") VALUES(?PK,transform(GeometryFromText(text(?2),?S), ?T),?M,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?25,?26,?27,?28,?29,?30,?31,?32)',
'DELETE FROM "eiel_indicadores_d_suministros" WHERE "id"=?1');


-- 4a Inserccion Table
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_suministros',5);
-- 4b Creación columnas
     --Añadir COLUMN_DOMAINS donde corresponda
     --INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
     --id_domain 10067:id 10068:id_municipio


     
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'id',0,32,0,2); --10201 eiel_indicadores_d_suministros.id integer
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'GEOMETRY',0,0,0,1); --10201 eiel_indicadores_d_suministros.GEOMETRY USER-DEFINED
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'id_municipio',5,0,0,3); --10201 eiel_indicadores_d_suministros.id_municipio character varying
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'toponimo',50,0,0,3); --10201 eiel_indicadores_d_suministros.toponimo character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codprov',2,0,0,3); --10201 eiel_indicadores_d_suministros.codprov character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codmunic',3,0,0,3); --10201 eiel_indicadores_d_suministros.codmunic character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codentidad',4,0,0,3); --10201 eiel_indicadores_d_suministros.codentidad character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codpoblamiento',2,0,0,3); --10201 eiel_indicadores_d_suministros.codpoblamiento character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_serv_elec_b',0,6,0,2); --10201 eiel_indicadores_d_suministros.nnuc_serv_elec_b numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_serv_elec_b',0,6,0,2); --10201 eiel_indicadores_d_suministros.nhab_serv_elec_b numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_serv_elec_b',0,6,0,2); --10201 eiel_indicadores_d_suministros.nest_serv_elec_b numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_serv_elec_r',0,6,0,2); --10201 eiel_indicadores_d_suministros.nnuc_serv_elec_r numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_serv_elec_r',0,6,0,2); --10201 eiel_indicadores_d_suministros.nhab_serv_elec_r numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_serv_elect_r',0,6,0,2); --10201 eiel_indicadores_d_suministros.nest_serv_elect_r numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_serv_elec_m',0,6,0,2); --10201 eiel_indicadores_d_suministros.nnuc_serv_elec_m numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_serv_elec_m',0,6,0,2); --10201 eiel_indicadores_d_suministros.nhab_serv_elec_m numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_serv_elec_m',0,6,0,2); --10201 eiel_indicadores_d_suministros.nest_serv_elec_m numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_serv_elec_n',0,6,0,2); --10201 eiel_indicadores_d_suministros.nnuc_serv_elec_n numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_serv_elec_n',0,6,0,2); --10201 eiel_indicadores_d_suministros.nhab_serv_elec_n numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_serv_elec_n',0,6,0,2); --10201 eiel_indicadores_d_suministros.nest_serv_elec_n numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_serv_gas_b',0,6,0,2); --10201 eiel_indicadores_d_suministros.nnuc_serv_gas_b numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_serv_gas_b',0,6,0,2); --10201 eiel_indicadores_d_suministros.nhab_serv_gas_b numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_serv_gas_b',0,6,0,2); --10201 eiel_indicadores_d_suministros.nest_serv_gas_b numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_serv_gas_r',0,6,0,2); --10201 eiel_indicadores_d_suministros.nnuc_serv_gas_r numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_serv_gas_r',0,6,0,2); --10201 eiel_indicadores_d_suministros.nhab_serv_gas_r numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_serv_gas_r',0,6,0,2); --10201 eiel_indicadores_d_suministros.nest_serv_gas_r numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_serv_gas_m',0,6,0,2); --10201 eiel_indicadores_d_suministros.nnuc_serv_gas_m numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_serv_gas_m',0,6,0,2); --10201 eiel_indicadores_d_suministros.nhab_serv_gas_m numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_serv_gas_m',0,6,0,2); --10201 eiel_indicadores_d_suministros.nest_serv_gas_m numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_serv_gas_n',0,6,0,2); --10201 eiel_indicadores_d_suministros.nnuc_serv_gas_n numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_serv_gas_n',0,6,0,2); --10201 eiel_indicadores_d_suministros.nhab_serv_gas_n numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_serv_gas_n',0,6,0,2); --10201 eiel_indicadores_d_suministros.nest_serv_gas_n numeric




-- 5 Insercion dictionary, attributes

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='id'),1,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='GEOMETRY'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='id_municipio'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','toponimo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='toponimo'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='codprov'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='codmunic'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='codentidad'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');

INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='codpoblamiento'),8,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos servicio electricidad Bueno');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nnuc_serv_elec_b'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos servicio electricidad Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nnuc_serv_elec_r'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos servicio electricidad Malo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nnuc_serv_elec_m'),11,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos SIN servicio electricidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nnuc_serv_elec_n'),12,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. servicio electricidad Bueno');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nhab_serv_elec_b'),13,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. servicio electricidad Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nhab_serv_elec_r'),14,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. servicio electricidad Malo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nhab_serv_elec_m'),15,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. SIN servicio electricidad ');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nhab_serv_elec_n'),16,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional servicio electricidad Bueno');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nest_serv_elec_b'),17,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional servicio electricidad REgular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nest_serv_elect_r'),18,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional servicio electricidad Malo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nest_serv_elec_m'),19,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. SIN estacional servicio electricidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nest_serv_elec_n'),20,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos servicio gas Bueno');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nnuc_serv_gas_b'),21,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos servicio gas Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nnuc_serv_gas_r'),22,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos servicio gas Malo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nnuc_serv_gas_m'),23,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos sin servicio gas');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nnuc_serv_gas_n'),24,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. servicio gas Bueno');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nhab_serv_gas_b'),25,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. servicio gas Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nhab_serv_gas_r'),26,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. servicio gas Malo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nhab_serv_gas_m'),27,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. SIN servicio gas');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nhab_serv_gas_n'),28,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional con servicio gas Bueno');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nest_serv_gas_b'),29,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional con servicio gas Regular');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nest_serv_gas_r'),30,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional con servicio gas Malo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nest_serv_gas_m'),31,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional sin servicio gas');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_suministros' and c.name='nest_serv_gas_n'),32,true);

INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='NP'),1);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='parroquias'),2);

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'NP'),(select id_styles from layers where name = 'NP'),'NP:_:EIEL: NP', true,1,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'parroquias'),(select id_styles from layers where name = 'parroquias'),'default:parroquias', true,2,0,true,true);
