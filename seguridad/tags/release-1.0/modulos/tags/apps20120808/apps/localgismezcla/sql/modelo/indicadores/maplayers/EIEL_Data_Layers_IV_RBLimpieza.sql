/*
 LocalGIS Layer Export v0.1 20111122
 SQL para generar los SQL de creación de capas a partir de una instalación existente (Ej. para crear SQL de creación de capas a partir de capas ya creadas en entorno DEV)

 

 Sustituciones
 EIEL_Indicadores_IV_RecogidaBasurasLimpieza
 EIEL_Indicadores_IV_RecogidaBasuras
 EIEL_Indicadores_IV_RecogidaBasurasLimpieza
 eiel_indicadores_d_rblimpieza
 
*/

-- 0 Mapa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_IV_RecogidaBasurasLimpieza</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_IV_RecogidaBasurasLimpieza</mapName></mapDescriptor>',0);




-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "
 --Estilo por defecto
INSERT INTO STYLES(ID_STYLE,XML) VALUES(NEXTVAL('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_IV_RecogidaBasuras</Name><UserStyle><Name>EIEL_Indicadores_IV_RecogidaBasuras:_:default</Name><Title>EIEL_Indicadores_IV_RecogidaBasuras:_:default</Title><Abstract>EIEL_Indicadores_IV_RecogidaBasuras:_:default</Abstract><FeatureTypeStyle><Rule><Name>EIEL_Indicadores_IV_RecogidaBasuras_NContenedores 0</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>N contenedores</ogc:PropertyName><ogc:Literal>1</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>N contenedores</ogc:PropertyName><ogc:Literal>3</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#ff6666</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>EIEL_Indicadores_IV_RecogidaBasuras_NContenedores 1</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>N contenedores</ogc:PropertyName><ogc:Literal>3</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>N contenedores</ogc:PropertyName><ogc:Literal>5</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#ffffcc</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>EIEL_Indicadores_IV_RecogidaBasuras_NContenedores 2</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>N contenedores</ogc:PropertyName><ogc:Literal>5</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>N contenedores</ogc:PropertyName><ogc:Literal>6</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#99ff66</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>EIEL_Indicadores_IV_RecogidaBasuras_NContenedores 3</Name><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>N contenedores</ogc:PropertyName><ogc:Literal>6</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>N contenedores</ogc:PropertyName><ogc:Literal>49</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name="fill">#33ff00</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><ogc:Filter xmlns:ogc="http://www.opengis.net/ogc"><ogc:Or><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>N nucleos sin recogida de basuras y limpieza</ogc:PropertyName><ogc:Literal>1</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>N nucleos con calidad servicio Inadecuado</ogc:PropertyName><ogc:Literal>1</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo></ogc:Or></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PointSymbolizer><Graphic><ExternalGraphic><OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="file:iconlib/123.gif"/><Format>image/gif</Format></ExternalGraphic><Opacity>1.0</Opacity><Size><ogc:Literal>16.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>'); 



 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_IV_RecogidaBasuras');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Indicadores_IV_RecogidaBasuras',1,0);



-- 2b Creación layerfamily
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

-- 2c -- Asociacion de layer-layerfamily, map
      -- Revisar nombre mapa
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),0);
       --Núcleos de población con topónimo

   


INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),currval('seq_layerfamilies'),0,0);

   --  Incluir estilo en layer_styles por cada capa en la familia

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'EIEL_Indicadores_IV_RecogidaBasuras:_:default', true,0,0,true,true);


-- 3 Queries de la capa


INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT transform("eiel_indicadores_d_rblimpieza"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_rblimpieza"."id","eiel_indicadores_d_rblimpieza"."id_municipio","eiel_indicadores_d_rblimpieza"."toponimo","eiel_indicadores_d_rblimpieza"."codprov","eiel_indicadores_d_rblimpieza"."codmunic","eiel_indicadores_d_rblimpieza"."codentidad","eiel_indicadores_d_rblimpieza"."codpoblamiento","eiel_indicadores_d_rblimpieza"."nnuc_rbl_no","eiel_indicadores_d_rblimpieza"."nhab_rbl_no","eiel_indicadores_d_rblimpieza"."nest_rbl_no","eiel_indicadores_d_rblimpieza"."nnuc_rbl_period_me","eiel_indicadores_d_rblimpieza"."nhab_rbl_period_me","eiel_indicadores_d_rblimpieza"."nest_rble_period_me","eiel_indicadores_d_rblimpieza"."nnuc_rbl_period_ba","eiel_indicadores_d_rblimpieza"."nhab_rbl_period_ba","eiel_indicadores_d_rblimpieza"."nest_rbl_period_ba","eiel_indicadores_d_rblimpieza"."nnuc_rbl_calidad_ad","eiel_indicadores_d_rblimpieza"."nhab_rbl_calidad_ad","eiel_indicadores_d_rblimpieza"."nest_rbl_calidad_ad","eiel_indicadores_d_rblimpieza"."nnuc_rbl_calidad_in","eiel_indicadores_d_rblimpieza"."nhab_rbl_calidad_in","eiel_indicadores_d_rblimpieza"."nest_rbl_calidad_in","eiel_indicadores_d_rblimpieza"."nnuc_rb_selectiva_si","eiel_indicadores_d_rblimpieza"."nhab_rb_selectiva_si","eiel_indicadores_d_rblimpieza"."nest_rb_selectiva_si","eiel_indicadores_d_rblimpieza"."nnuc_rb_selectiva_no","eiel_indicadores_d_rblimpieza"."nhab_rb_selectiva_no","eiel_indicadores_d_rblimpieza"."nest_rb_selectiva_no","eiel_indicadores_d_rblimpieza"."nnuc_limp_si","eiel_indicadores_d_rblimpieza"."nhab_limp_si","eiel_indicadores_d_rblimpieza"."nest_limp_si","eiel_indicadores_d_rblimpieza"."nnuc_limp_no","eiel_indicadores_d_rblimpieza"."nhab_limp_no","eiel_indicadores_d_rblimpieza"."nest_limp_no","eiel_indicadores_d_rblimpieza"."nviv_serv_deficit","eiel_indicadores_d_rblimpieza"."nhab_serv_deficit","eiel_indicadores_d_rblimpieza"."nest_serv_deficit","eiel_indicadores_d_rblimpieza"."n_contenedores" FROM "eiel_indicadores_d_rblimpieza" WHERE "eiel_indicadores_d_rblimpieza"."id_municipio" IN (?M)',
'UPDATE "eiel_indicadores_d_rblimpieza" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio" = ?M,"toponimo"=?4,"codprov"=?5,"codmunic"=?6,"codentidad"=?7,"codpoblamiento"=?8,"nnuc_rbl_no"=?9,"nhab_rbl_no"=?10,"nest_rbl_no"=?11,"nnuc_rbl_period_me"=?12,"nhab_rbl_period_me"=?13,"nest_rble_period_me"=?14,"nnuc_rbl_period_ba"=?15,"nhab_rbl_period_ba"=?16,"nest_rbl_period_ba"=?17,"nnuc_rbl_calidad_ad"=?18,"nhab_rbl_calidad_ad"=?19,"nest_rbl_calidad_ad"=?20,"nnuc_rbl_calidad_in"=?21,"nhab_rbl_calidad_in"=?22,"nest_rbl_calidad_in"=?23,"nnuc_rb_selectiva_si"=?24,"nhab_rb_selectiva_si"=?25,"nest_rb_selectiva_si"=?26,"nnuc_rb_selectiva_no"=?27,"nhab_rb_selectiva_no"=?28,"nest_rb_selectiva_no"=?29,"nnuc_limp_si"=?30,"nhab_limp_si"=?31,"nest_limp_si"=?32,"nnuc_limp_no"=?33,"nhab_limp_no"=?34,"nest_limp_no"=?35,"nviv_serv_deficit"=?36,"nhab_serv_deficit"=?37,"nest_serv_deficit"=?38,"n_contenedores"=?39 WHERE "id"=?2',
'INSERT INTO "eiel_indicadores_d_rblimpieza" ("GEOMETRY","id","id_municipio","toponimo","codprov","codmunic","codentidad","codpoblamiento","nnuc_rbl_no","nhab_rbl_no","nest_rbl_no","nnuc_rbl_period_me","nhab_rbl_period_me","nest_rble_period_me","nnuc_rbl_period_ba","nhab_rbl_period_ba","nest_rbl_period_ba","nnuc_rbl_calidad_ad","nhab_rbl_calidad_ad","nest_rbl_calidad_ad","nnuc_rbl_calidad_in","nhab_rbl_calidad_in","nest_rbl_calidad_in","nnuc_rb_selectiva_si","nhab_rb_selectiva_si","nest_rb_selectiva_si","nnuc_rb_selectiva_no","nhab_rb_selectiva_no","nest_rb_selectiva_no","nnuc_limp_si","nhab_limp_si","nest_limp_si","nnuc_limp_no","nhab_limp_no","nest_limp_no","nviv_serv_deficit","nhab_serv_deficit","nest_serv_deficit","n_contenedores") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?25,?26,?27,?28,?29,?30,?31,?32,?33,?34,?35,?36,?37,?38,?39)',
'DELETE FROM "eiel_indicadores_d_rblimpieza" WHERE "id"=?2');


-- 4a Inserccion Table
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_rblimpieza',5);


-- 4b Creación columnas
     --Añadir COLUMN_DOMAINS donde corresponda
     --INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
     --id_domain 10067:id 10068:id_municipio
     
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'id',0,32,0,2); --10204 eiel_indicadores_d_rblimpieza.id integer
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'GEOMETRY',0,0,0,1); --10204 eiel_indicadores_d_rblimpieza.GEOMETRY USER-DEFINED
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'id_municipio',5,0,0,3); --10204 eiel_indicadores_d_rblimpieza.id_municipio character varying
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'toponimo',50,0,0,3); --10204 eiel_indicadores_d_rblimpieza.toponimo character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codprov',2,0,0,3); --10204 eiel_indicadores_d_rblimpieza.codprov character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codmunic',3,0,0,3); --10204 eiel_indicadores_d_rblimpieza.codmunic character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codentidad',4,0,0,3); --10204 eiel_indicadores_d_rblimpieza.codentidad character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codpoblamiento',2,0,0,3); --10204 eiel_indicadores_d_rblimpieza.codpoblamiento character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_rbl_no',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nnuc_rbl_no numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_rbl_no',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_rbl_no numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_rbl_no',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_rbl_no numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_rbl_period_me',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nnuc_rbl_period_me numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_rbl_period_me',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_rbl_period_me numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_rble_period_me',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_rble_period_me numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_rbl_period_ba',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nnuc_rbl_period_ba numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_rbl_period_ba',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_rbl_period_ba numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_rbl_period_ba',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_rbl_period_ba numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_rbl_calidad_ad',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nnuc_rbl_calidad_ad numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_rbl_calidad_ad',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_rbl_calidad_ad numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_rbl_calidad_ad',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_rbl_calidad_ad numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_rbl_calidad_in',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nnuc_rbl_calidad_in numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_rbl_calidad_in',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_rbl_calidad_in numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_rbl_calidad_in',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_rbl_calidad_in numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_rb_selectiva_si',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nnuc_rb_selectiva_si numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_rb_selectiva_si',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_rb_selectiva_si numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_rb_selectiva_si',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_rb_selectiva_si numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_rb_selectiva_no',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nnuc_rb_selectiva_no numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_rb_selectiva_no',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_rb_selectiva_no numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_rb_selectiva_no',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_rb_selectiva_no numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_limp_si',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nnuc_limp_si numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_limp_si',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_limp_si numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_limp_si',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_limp_si numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_limp_no',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nnuc_limp_no numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_limp_no',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_limp_no numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_limp_no',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_limp_no numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nviv_serv_deficit',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nviv_serv_deficit numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_serv_deficit',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nhab_serv_deficit numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_serv_deficit',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.nest_serv_deficit numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'n_contenedores',0,6,0,2); --10204 eiel_indicadores_d_rblimpieza.n_contenedores numeric


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='id'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='GEOMETRY'),1,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='id_municipio'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','toponimo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='toponimo'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='codprov'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='codmunic'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='codentidad'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='codpoblamiento'),8,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos sin recogida de basuras y limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_rbl_no'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. sin recogida de basuras y limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_rbl_no'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob. estacional sin recogida de basuras y limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_rbl_no'),11,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos con peridiocidad de recogida y limpieza Media');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_rbl_period_me'),12,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob con peridiocidad de recogida y limpieza Media');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_rbl_period_me'),13,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional con peridiocidad de recogida y limpieza Media');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_rble_period_me'),14,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos con peridiocidad de recogida y limpieza Baja');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_rbl_period_ba'),15,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob con peridiocidad de recogida y limpieza Baja');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_rbl_period_ba'),16,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional con peridiocidad de recogida y limpieza Baja');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_rbl_period_ba'),17,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos con calidad servicio Adecuado');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_rbl_calidad_ad'),18,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob con calidad servicio Adecuado');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_rbl_calidad_ad'),19,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional con calidad servicio Adecuado');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_rbl_calidad_ad'),20,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos con calidad servicio Inadecuado');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_rbl_calidad_in'),21,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob con calidad servicio Inadecuado');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_rbl_calidad_in'),22,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional con calidad servicio Inadecuado');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_rbl_calidad_in'),23,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos con recogida selectiva SI');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_rb_selectiva_si'),24,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob con recogida selectiva SI');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_rb_selectiva_si'),25,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional con recogida selectiva SI');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_rb_selectiva_si'),26,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos con recogida selectiva NO');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_rb_selectiva_no'),27,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob con recogida selectiva NO');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_rb_selectiva_no'),28,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional con recogida selectiva NO');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_rb_selectiva_no'),29,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos con servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_limp_si'),30,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob con servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_limp_si'),31,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional con servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_limp_si'),32,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos SIN servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_limp_no'),33,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob SIN servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_limp_no'),34,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional SIN servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_limp_no'),35,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N viviendas con deficit de servicio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nviv_serv_deficit'),36,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob con deficit de servicio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_serv_deficit'),37,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional con deficit de servicio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_serv_deficit'),38,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N contenedores');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='n_contenedores'),39,true);

--- Capa Limpieza ---------------------------------------------------------------------------------------------------------------------


-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "
 --Estilo por defecto
INSERT INTO STYLES(ID_STYLE,XML) VALUES(NEXTVAL('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_IV_Limpieza</Name><UserStyle><Name>default</Name><Title>Default user style</Title><Abstract>Default user style</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name="stroke">#007d00</CssParameter><CssParameter name="stroke-linecap">square</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width">1.0</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>'); 

 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_IV_Limpieza');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Indicadores_IV_Limpieza',1,0);
/*
SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''|| l.name ||E'\');' || E'\nINSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) \n   VALUES (NEXTVAL(\'seq_layers\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_styles\'),(SELECT idacl FROM acl WHERE name=\'Capas EIEL Indicadores\'),\''||l.name|| E'\',1,0);'   as sqlLayerCreate
FROM layers l
WHERE l.name='EIEL_Indicadores_IV_Limpieza';
*/



-- 2c -- Asociacion de layer-layerfamily, map
      -- Revisar nombre mapa
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),1);
  

   --  Incluir estilo en layer_styles por cada capa en la familia

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default', true,1,0,true,true);


-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT transform("eiel_indicadores_d_rblimpieza"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_rblimpieza"."id","eiel_indicadores_d_rblimpieza"."id_municipio","eiel_indicadores_d_rblimpieza"."toponimo","eiel_indicadores_d_rblimpieza"."codprov","eiel_indicadores_d_rblimpieza"."codmunic","eiel_indicadores_d_rblimpieza"."codentidad","eiel_indicadores_d_rblimpieza"."codpoblamiento","eiel_indicadores_d_rblimpieza"."nnuc_limp_si","eiel_indicadores_d_rblimpieza"."nhab_limp_si","eiel_indicadores_d_rblimpieza"."nest_limp_si","eiel_indicadores_d_rblimpieza"."nnuc_limp_no","eiel_indicadores_d_rblimpieza"."nhab_limp_no","eiel_indicadores_d_rblimpieza"."nest_limp_no" FROM "eiel_indicadores_d_rblimpieza" WHERE "eiel_indicadores_d_rblimpieza"."id_municipio" IN (?M)',
'UPDATE "eiel_indicadores_d_rblimpieza" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio" = ?M,"toponimo"=?4,"codprov"=?5,"codmunic"=?6,"codentidad"=?7,"codpoblamiento"=?8,"nnuc_limp_si"=?9,"nhab_limp_si"=?10,"nest_limp_si"=?11,"nnuc_limp_no"=?12,"nhab_limp_no"=?13,"nest_limp_no"=?14 WHERE "id"=?2',
'INSERT INTO "eiel_indicadores_d_rblimpieza" ("GEOMETRY","id","id_municipio","toponimo","codprov","codmunic","codentidad","codpoblamiento","nnuc_limp_si","nhab_limp_si","nest_limp_si","nnuc_limp_no","nhab_limp_no","nest_limp_no") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14)',
'DELETE FROM "eiel_indicadores_d_rblimpieza" WHERE "id"=?2');


-- 4a Inserccion Table
--n/a
-- 4b Creación columnas
--n/a

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='GEOMETRY'),1,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='id'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='id_municipio'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','toponimo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='toponimo'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='codprov'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='codmunic'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='codentidad'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='codpoblamiento'),8,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos con servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_limp_si'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob con servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_limp_si'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional con servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_limp_si'),11,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N nucleos SIN servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nnuc_limp_no'),12,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob SIN servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nhab_limp_no'),13,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pob estacional SIN servicio de limpieza');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_rblimpieza' and c.name='nest_limp_no'),14,true);



INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='NP'),2);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='parroquias'),3);

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'NP'),(select id_styles from layers where name = 'NP'),'NP:_:EIEL: NP', true,2,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'parroquias'),(select id_styles from layers where name = 'parroquias'),'default:parroquias', true,3,0,true,true);
