/*
 LocalGIS Layer Export v0.1 20111122
 SQL para generar los SQL de creación de capas a partir de una instalación existente (Ej. para crear SQL de creación de capas a partir de capas ya creadas en entorno DEV)

 

 Sustituciones
 EIEL_Indicadores_VI_SanitarioAsistencial
 EIEL_Indicadores_VI_Sanidad
 EIEL_Indicadores_VI_SanitarioAsistencial
 eiel_indicadores_d_csanitario
 
*/



-- 0 Mapa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_VI_SanitarioAsistencial</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_VI_SanitarioAsistencial</mapName></mapDescriptor>',0);




-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "
 --Estilo por defecto
INSERT INTO STYLES(ID_STYLE,XML) VALUES(NEXTVAL('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_VI_Sanidad</Name><UserStyle><Name>EIEL_Indicadores_VI_Sanidad:_:default</Name><Title>EIEL_Indicadores_VI_Sanidad:_:default</Title><Abstract>EIEL_Indicadores_VI_Sanidad:_:default</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name="fill">#ffffff</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>'); 



 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_VI_Sanidad');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Indicadores_VI_Sanidad',1,0);


-- 2b Creación layerfamily
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

-- 2c -- Asociacion de layer-layerfamily, map
      -- Revisar nombre mapa
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),0);

   

INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),currval('seq_layerfamilies'),0,0);

   --  Incluir estilo en layer_styles por cada capa en la familia

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'EIEL_Indicadores_VI_Sanidad:_:default', true,0,0,true,true);


-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT transform("eiel_indicadores_d_csanitario"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_csanitario"."id","eiel_indicadores_d_csanitario"."toponimo","eiel_indicadores_d_csanitario"."id_municipio","eiel_indicadores_d_csanitario"."codprov","eiel_indicadores_d_csanitario"."codmunic","eiel_indicadores_d_csanitario"."codentidad","eiel_indicadores_d_csanitario"."codpoblamiento","eiel_indicadores_d_csanitario"."ncamashospital","eiel_indicadores_d_csanitario"."ncentrossalud","eiel_indicadores_d_csanitario"."ncentrosurg","eiel_indicadores_d_csanitario"."nconsultorioslocal","eiel_indicadores_d_csanitario"."nest_sincsanitario","eiel_indicadores_d_csanitario"."nhab_sincsanitario","eiel_indicadores_d_csanitario"."nhospital","eiel_indicadores_d_csanitario"."nhospitalgeri","eiel_indicadores_d_csanitario"."nnuc_sincsanitario","eiel_indicadores_d_csanitario"."npsiquiatricos" FROM "eiel_indicadores_d_csanitario" WHERE "eiel_indicadores_d_csanitario"."id_municipio" IN (?M)',
'UPDATE "eiel_indicadores_d_csanitario" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"toponimo"=?3,"id_municipio" = ?M,"codprov"=?5,"codmunic"=?6,"codentidad"=?7,"codpoblamiento"=?8,"ncamashospital"=?9,"ncentrossalud"=?10,"ncentrosurg"=?11,"nconsultorioslocal"=?12,"nest_sincsanitario"=?13,"nhab_sincsanitario"=?14,"nhospital"=?15,"nhospitalgeri"=?16,"nnuc_sincsanitario"=?17,"npsiquiatricos"=?18 WHERE "id"=?2',
'INSERT INTO "eiel_indicadores_d_csanitario" ("GEOMETRY","id","toponimo","id_municipio","codprov","codmunic","codentidad","codpoblamiento","ncamashospital","ncentrossalud","ncentrosurg","nconsultorioslocal","nest_sincsanitario","nhab_sincsanitario","nhospital","nhospitalgeri","nnuc_sincsanitario","npsiquiatricos") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?M,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18)',
'DELETE FROM "eiel_indicadores_d_csanitario" WHERE "id"=?2');



-- 4a Inserccion Table

INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_csanitario',5);
-- 4b Creación columnas
     --Añadir COLUMN_DOMAINS donde corresponda
     --INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
     --id_domain 10067:id 10068:id_municipio
     
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),10067,'id',0,32,0,2); --10209 eiel_indicadores_d_csanitario.id integer
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'GEOMETRY',0,0,0,1); --10209 eiel_indicadores_d_csanitario.GEOMETRY USER-DEFINED
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),10068,'id_municipio',5,0,0,3); --10209 eiel_indicadores_d_csanitario.id_municipio character varying
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'toponimo',50,0,0,3); --10209 eiel_indicadores_d_csanitario.toponimo character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codprov',2,0,0,3); --10209 eiel_indicadores_d_csanitario.codprov character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codmunic',3,0,0,3); --10209 eiel_indicadores_d_csanitario.codmunic character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codentidad',4,0,0,3); --10209 eiel_indicadores_d_csanitario.codentidad character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'codpoblamiento',2,0,0,3); --10209 eiel_indicadores_d_csanitario.codpoblamiento character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nconsultorioslocal',0,6,0,2); --10209 eiel_indicadores_d_csanitario.nconsultorioslocal numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'ncentrossalud',0,6,0,2); --10209 eiel_indicadores_d_csanitario.ncentrossalud numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'ncentrosurg',0,6,0,2); --10209 eiel_indicadores_d_csanitario.ncentrosurg numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhospital',0,6,0,2); --10209 eiel_indicadores_d_csanitario.nhospital numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhospitalgeri',0,6,0,2); --10209 eiel_indicadores_d_csanitario.nhospitalgeri numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'npsiquiatricos',0,6,0,2); --10209 eiel_indicadores_d_csanitario.npsiquiatricos numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'ncamashospital',0,6,0,2); --10209 eiel_indicadores_d_csanitario.ncamashospital numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nnuc_sincsanitario',0,6,0,2); --10209 eiel_indicadores_d_csanitario.nnuc_sincsanitario numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nhab_sincsanitario',0,6,0,2); --10209 eiel_indicadores_d_csanitario.nhab_sincsanitario numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),CURRVAL('seq_tables'),null,'nest_sincsanitario',0,6,0,2); --10209 eiel_indicadores_d_csanitario.nest_sincsanitario numeric



-- 5 Insercion dictionary, attributes

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='GEOMETRY'),1,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='id'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','toponimo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='toponimo'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='id_municipio'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='codprov'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='codmunic'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='codentidad'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='codpoblamiento'),8,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ncamashospital');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='ncamashospital'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ncentrossalud');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='ncentrossalud'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','ncentrosurg');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='ncentrosurg'),11,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nconsultorioslocal');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='nconsultorioslocal'),12,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nest_sincsanitario');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='nest_sincsanitario'),13,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhab_sincsanitario');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='nhab_sincsanitario'),14,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhospital');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='nhospital'),15,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhospitalgeri');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='nhospitalgeri'),16,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nnuc_sincsanitario');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='nnuc_sincsanitario'),17,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','npsiquiatricos');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_csanitario' and c.name='npsiquiatricos'),18,true);


-- ######### CAPA CENTROS ASISTENCIALES #############################################




-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "
 --Estilo por defecto
INSERT INTO STYLES(ID_STYLE,XML) VALUES(NEXTVAL('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>EIEL_Indicadores_VI_Asistencial</Name><UserStyle><Name>EIEL_Indicadores_VI_Asistencial:_:default</Name><Title>EIEL_Indicadores_VI_Asistencial:_:default</Title><Abstract>EIEL_Indicadores_VI_Asistencial:_:default</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name="fill">#ffffff</CssParameter><CssParameter name="fill-opacity">1.0</CssParameter></Fill><Stroke><CssParameter name="stroke">#000000</CssParameter><CssParameter name="stroke-linecap">round</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width"><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>
'); 

 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_VI_Asistencial');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'EIEL_Indicadores_VI_Asistencial',1,0);


-- 2b Creación layerfamily

-- 2c -- Asociacion de layer-layerfamily, map
      -- Revisar nombre mapa
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),1);


   --  Incluir estilo en layer_styles por cada capa en la familia

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'EIEL_Indicadores_VI_Asistencial:_:default', true,1,0,true,true);


-- 3 Queries de la capa
INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL('seq_queries'),CURRVAL('seq_layers'),1,
'SELECT transform("eiel_indicadores_d_casistencial"."GEOMETRY", ?T) AS "GEOMETRY","eiel_indicadores_d_casistencial"."id","eiel_indicadores_d_casistencial"."toponimo","eiel_indicadores_d_casistencial"."id_municipio","eiel_indicadores_d_casistencial"."codprov","eiel_indicadores_d_casistencial"."codmunic","eiel_indicadores_d_casistencial"."codentidad","eiel_indicadores_d_casistencial"."codpoblamiento","eiel_indicadores_d_casistencial"."ncentros","eiel_indicadores_d_casistencial"."nplazasresidenciasancianos","eiel_indicadores_d_casistencial"."nresidenciasancianos" FROM "eiel_indicadores_d_casistencial" WHERE "eiel_indicadores_d_casistencial"."id_municipio" IN (?M)',
'UPDATE "eiel_indicadores_d_casistencial" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"toponimo"=?3,"id_municipio" = ?M,"codprov"=?5,"codmunic"=?6,"codentidad"=?7,"codpoblamiento"=?8,"ncentros"=?9,"nplazasresidenciasancianos"=?10,"nresidenciasancianos"=?11 WHERE "id"=?2',
'INSERT INTO "eiel_indicadores_d_casistencial" ("GEOMETRY","id","toponimo","id_municipio","codprov","codmunic","codentidad","codpoblamiento","ncentros","nplazasresidenciasancianos","nresidenciasancianos") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?M,?5,?6,?7,?8,?9,?10,?11)',
'DELETE FROM "eiel_indicadores_d_casistencial" WHERE "id"=?2');


-- 4a Inserccion Table
--DELETE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_casistencial',5);
-- 4b Creación columnas
     --Añadir COLUMN_DOMAINS donde corresponda
     --INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
     --id_domain 10067:id 10068:id_municipio
     
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'id',0,32,0,2); --10210 eiel_indicadores_d_casistencial.id integer
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'GEOMETRY',0,0,0,1); --10210 eiel_indicadores_d_casistencial.GEOMETRY USER-DEFINED
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'id_municipio',5,0,0,3); --10210 eiel_indicadores_d_casistencial.id_municipio character varying
INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10068,0);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'toponimo',50,0,0,3); --10210 eiel_indicadores_d_casistencial.toponimo character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'codprov',2,0,0,3); --10210 eiel_indicadores_d_casistencial.codprov character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'codmunic',3,0,0,3); --10210 eiel_indicadores_d_casistencial.codmunic character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'codentidad',4,0,0,3); --10210 eiel_indicadores_d_casistencial.codentidad character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'codpoblamiento',2,0,0,3); --10210 eiel_indicadores_d_casistencial.codpoblamiento character varying
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'ncentros',0,6,0,2); --10210 eiel_indicadores_d_casistencial.ncentros numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'nresidenciasancianos',0,6,0,2); --10210 eiel_indicadores_d_casistencial.nresidenciasancianos numeric
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),(SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'),null,'nplazasresidenciasancianos',0,6,0,2); --10210 eiel_indicadores_d_casistencial.nplazasresidenciasancianos numeric

/*
SELECT * FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial'
SELECT * FROM COLUMNS WHERE ID_TABLE IN (SELECT ID_TABLE FROM TABLES WHERE NAME='eiel_indicadores_d_casistencial')
*/

-- 5 Insercion dictionary, attributes
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','GEOMETRY');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='GEOMETRY'),1,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='id'),2,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','toponimo');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='toponimo'),3,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','id_municipio');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='id_municipio'),4,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codprov');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='codprov'),5,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codmunic');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='codmunic'),6,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='codentidad'),7,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='codpoblamiento'),8,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N centros');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='ncentros'),9,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N Plazas residencias ancianos');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='nplazasresidenciasancianos'),10,true);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','N residencias ancianos');
INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),CURRVAL('seq_layers'),(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name='eiel_indicadores_d_casistencial' and c.name='nresidenciasancianos'),11,true);




INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='NP'),2);
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='parroquias'),3);

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'NP'),(select id_styles from layers where name = 'NP'),'NP:_:EIEL: NP', true,2,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),currval('seq_layerfamilies'),(select id_layer from layers where name = 'parroquias'),(select id_styles from layers where name = 'parroquias'),'default:parroquias', true,3,0,true,true);
