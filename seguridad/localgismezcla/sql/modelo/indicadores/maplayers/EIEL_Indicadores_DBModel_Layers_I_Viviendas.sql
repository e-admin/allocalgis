/*
------- ### ACL EIEL_Indicadores ###
insert into acl values (NEXTVAL('seq_acl'), 'Capas EIEL Indicadores');


-- Se asocia el ACL con los permisos de leer, escribir, etc
insert into r_acl_perm values (871, CURRVAL('seq_acl'));
insert into r_acl_perm values (872, CURRVAL('seq_acl'));
insert into r_acl_perm values (873, CURRVAL('seq_acl'));
insert into r_acl_perm values (874, CURRVAL('seq_acl'));

-- Se le dan permisos al superuser
insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 871, CURRVAL('seq_acl'), 1); 
insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 872, CURRVAL('seq_acl'), 1); 
insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 873, CURRVAL('seq_acl'), 1); 
insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (100, 874, CURRVAL('seq_acl'), 1); 

-- Se le dan permisos a satec_pre
insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (325, 871, CURRVAL('seq_acl'), 1); 
insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (325, 872, CURRVAL('seq_acl'), 1); 
insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (325, 873, CURRVAL('seq_acl'), 1); 
insert into r_usr_perm (userid, idperm, idacl, aplica) VALUES (325, 874, CURRVAL('seq_acl'), 1); 


-------- ### MAPA DE Poblacion ####
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','EIEL_Indicadores_III_Alumbrado');

INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(999,CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?>
<mapDescriptor><description></description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>UTM 29N ED50</mapProjection><mapName>EIEL_Indicadores_III_Alumbrado</mapName></mapDescriptor>
',0);

select * from maps order by id_map desc limit 5;
*/
-- Estilos por defecto
insert into STYLES (ID_STYLE, XML) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_I_Vivienda</Name><UserStyle><Name>EIEL_Indicadores_I_Vivienda:_:default:EIEL_Indicadores_I_Vivienda</Name><Title>EIEL_Indicadores_I_Vivienda:_:default:EIEL_Indicadores_I_Vivienda</Title><Abstract>EIEL_Indicadores_I_Vivienda:_:default:EIEL_Indicadores_I_Vivienda</Abstract><FeatureTypeStyle><Name>EIEL_Indicadores_I_Vivienda</Name><Rule><Name>0</Name><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>nviviendasnucleos</ogc:PropertyName><ogc:Literal>2</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>nviviendasnucleos</ogc:PropertyName><ogc:Literal>26</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#ffffcc</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>1</Name><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>nviviendasnucleos</ogc:PropertyName><ogc:Literal>26</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>nviviendasnucleos</ogc:PropertyName><ogc:Literal>57</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#ffcc99</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>2</Name><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>nviviendasnucleos</ogc:PropertyName><ogc:Literal>57</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>nviviendasnucleos</ogc:PropertyName><ogc:Literal>110</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#ff9966</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>3</Name><ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'><ogc:And><ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyName>nviviendasnucleos</ogc:PropertyName><ogc:Literal>110</ogc:Literal></ogc:PropertyIsGreaterThanOrEqualTo><ogc:PropertyIsLessThan><ogc:PropertyName>nviviendasnucleos</ogc:PropertyName><ogc:Literal>4973</ogc:Literal></ogc:PropertyIsLessThan></ogc:And></ogc:Filter>
<MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill-opacity'>1.0</CssParameter><CssParameter name='fill'>#ff3333</CssParameter></Fill><Stroke><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>');

/*
-- Creacion de layerfamily 
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_I_Poblacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_I_Poblacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_I_Poblacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_I_Poblacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_I_Poblacion');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );
*/



-- Creacion de las capas
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_I_Vivienda');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),CURRVAL('seq_acl'),'EIEL_Indicadores_I_Vivienda',1,0);

--INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
   'SELECT "eiel_indicadores_d_poblacion"."codentidad","eiel_indicadores_d_poblacion"."codmunic","eiel_indicadores_d_poblacion"."codpoblamiento","eiel_indicadores_d_poblacion"."codprov",transform("eiel_indicadores_d_poblacion"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_poblacion"."id","eiel_indicadores_d_poblacion"."id_municipio","eiel_indicadores_d_poblacion"."numnucleos","eiel_indicadores_d_poblacion"."poblaciondensidad","eiel_indicadores_d_poblacion"."poblacionderecho","eiel_indicadores_d_poblacion"."poblaciondiseminado","eiel_indicadores_d_poblacion"."poblacionestacional","eiel_indicadores_d_poblacion"."poblaciontotal","eiel_indicadores_d_poblacion"."superficeresidencial","eiel_indicadores_d_poblacion"."superficieagricolaforestalporcent","eiel_indicadores_d_poblacion"."superficiemunicipal","eiel_indicadores_d_poblacion"."superficieurbana","eiel_indicadores_d_poblacion"."toponimo" FROM "eiel_indicadores_d_poblacion" WHERE "eiel_indicadores_d_poblacion"."id_municipio" in (?M)',
   'UPDATE "eiel_indicadores_d_poblacion" SET "codentidad"=?1,"codmunic"=?2,"codpoblamiento"=?3,"codprov"=?4,"GEOMETRY"=transform(GeometryFromText(text(?5),?S), ?T),"id"=?6,"id_municipio"=?7,"numnucleos"=?8,"poblaciondensidad"=?9,"poblacionderecho"=?10,"poblaciondiseminado"=?11,"poblacionestacional"=?12,"poblaciontotal"=?13,"superficeresidencial"=?14,"superficieagricolaforestalporcent"=?15,"superficiemunicipal"=?16,"superficieurbana"=?17,"toponimo"=?18 WHERE "id_municipio"=?7',
   'INSERT INTO "eiel_indicadores_d_poblacion" ("codentidad","codmunic","codpoblamiento","codprov","GEOMETRY","id","id_municipio","numnucleos","poblaciondensidad","poblacionderecho","poblaciondiseminado","poblacionestacional","poblaciontotal","superficeresidencial","superficieagricolaforestalporcent","superficiemunicipal","superficieurbana","toponimo") VALUES(?1,?2,?3,?4,transform(GeometryFromText(text(?5),?S), ?T),?6,?PK,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18)',
   'DELETE FROM "eiel_indicadores_d_poblacion" WHERE "id_municipio"=?7'
); 




   
-- Asociacion de layer-layerfamily
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),2);
insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (999,currval('seq_layerfamilies'),1,0); 
    
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (999,currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:EIEL_Indicadores_S_I_Poblacion', true,0,0,true,true);



INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_poblacion',5);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Geometria');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'GEOMETRY',0,0,0,1);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),1,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ID');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),10067,'id',null,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),2,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Toponimo');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'toponimo',100,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),3,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ID de Municipio');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),10068,'id_municipio',5,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),4,true);
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codprov',2,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),5,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codmunic',3,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),6,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codentidad',4,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),7,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codpoblamiento',2,0,0,3);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),8,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','numnucleos');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'numnucleos',0,6,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),9,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Poblacion total');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'poblaciontotal',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),10,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','padron');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'poblacionderecho',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),11,true);


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Poblacion diseminado');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'poblaciondiseminado',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),12,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Poblacion estacional');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'poblacionestacional',0,8,0,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),13,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Poblacion densidad');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'poblaciondensidad',0,8,2,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),14,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Superficie municipal');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'superficiemunicipal',0,9,2,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),15,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Superficie urbana');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'superficieurbana',0,9,2,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),16,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Superficie residencial');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'superficeresidencial',0,9,2,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),17,true);

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Porcentaje Superficie agricola forestal');
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'superficieagricolaforestalporcent',0,9,2,2);
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),CURRVAL('seq_columns'),18,true);


