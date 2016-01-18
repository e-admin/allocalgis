/*
 LocalGIS Layer Export v0.1 20111122
 SQL para generar los SQL de creación de capas a partir de una instalación existente (Ej. para crear SQL de creación de capas a partir de capas ya creadas en entorno DEV)

 

 Sustituciones
 #MAPA#
 #CAPA#
 #LAYERFAMILY#
 #TABLA#
 
*/

-- 0 Mapa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','#MAPA#');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]#MAPA#');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]#MAPA#');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]#MAPA#');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]#MAPA#');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>#MAPA#</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>#MAPA#</mapName></mapDescriptor>',0);




-- 1 Estilo de la capa
--  Posiblemente al resultado xml haya que modificar el caracter ' por "
 --Estilo por defecto
INSERT INTO STYLES(ID_STYLE,XML) VALUES(NEXTVAL('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer><Name>#CAPA#</Name><UserStyle><Name>default</Name><Title>Default user style</Title><Abstract>Default user style</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name="stroke">#007d00</CssParameter><CssParameter name="stroke-linecap">square</CssParameter><CssParameter name="stroke-linejoin">round</CssParameter><CssParameter name="stroke-opacity">1.0</CssParameter><CssParameter name="stroke-width">1.0</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
</StyledLayerDescriptor>'); 

 --Para copiar uno existente
select E'INSERT INTO STYLES (ID_STYLE, XML) values(nextval(\'seq_styles\'),\'' || s.xml || E'\');' as sqlLayerStyle
from layers l, styles s
where  l."name"= '#CAPA#'
  and  s.id_style=l.id_styles;

 
-- 2a Creación de capa
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','#CAPA#');
INSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) VALUES (NEXTVAL('seq_layers'),CURRVAL('seq_dictionary'),CURRVAL('seq_styles'),(SELECT idacl FROM acl WHERE name='Capas EIEL Indicadores'),'#CAPA#',1,0);
/*
SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''|| l.name ||E'\');' || E'\nINSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) \n   VALUES (NEXTVAL(\'seq_layers\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_styles\'),(SELECT idacl FROM acl WHERE name=\'Capas EIEL Indicadores\'),\''||l.name|| E'\',1,0);'   as sqlLayerCreate
FROM layers l
WHERE l.name='#CAPA#';
*/


-- 2b Creación layerfamily
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','#LAYERFAMILY#');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]#LAYERFAMILY#');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]#LAYERFAMILY#');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]#LAYERFAMILY#');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]#LAYERFAMILY#');
INSERT INTO LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),currval('seq_dictionary'),nextval('seq_dictionary') );

-- 2c -- Asociacion de layer-layerfamily, map
      -- Revisar nombre mapa
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),currval('seq_layers'),1);
       --Elemento EIEL relacionado con el indicador 
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name = 'CA'),2);
       --Núcleos de población con topónimo
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='NP'),3);
       --Límites parroquias
INSERT INTO layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (currval('seq_layerfamilies'),(select id_layer from layers where name='parroquias'),4);
   

INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps m, dictionary d where m.id_name=d.id_vocablo and d.traduccion='#MAPA#'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='#LAYERFAMILY#'),2,0);

   --  Incluir estilo en layer_styles por cada capa en la familia

INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps m, dictionary d where m.id_entidad=0 and m.id_name=d.id_vocablo and d.traduccion='#MAPA#'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='#LAYERFAMILY#'),currval('seq_layers'),currval('seq_styles'),'#CAPA#:_:default', true,0,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps m, dictionary d where m.id_entidad=0 and m.id_name=d.id_vocablo and d.traduccion='#MAPA#'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='#LAYERFAMILY#'),(select id_layer from layers where name = 'TP'),(select id_styles from layers where name = 'TP'),'TP:_:EIEL:TP', true,1,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps m, dictionary d where m.id_entidad=0 and m.id_name=d.id_vocablo and d.traduccion='#MAPA#'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='#LAYERFAMILY#'),(select id_layer from layers where name = 'NP'),(select id_styles from layers where name = 'NP'),'NP:_:EIEL: NP', true,2,0,true,true);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps m, dictionary d where m.id_entidad=0 and m.id_name=d.id_vocablo and d.traduccion='#MAPA#'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='#LAYERFAMILY#'),(select id_layer from layers where name = 'parroquias'),(select id_styles from layers where name = 'parroquias'),'parroquias:_:default:parroquias', true,3,0,true,true);


-- 3 Queries de la capa
SELECT E'INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL(\'seq_queries\'),CURRVAL(\'seq_layers\'),1,\n' || E'\''|| q.selectquery || E'\',\n' ||E'\''|| q.updatequery || E'\',\n' ||E'\''|| q.insertquery || E'\',\n' ||E'\''|| q.deletequery ||E'\');' as sqlLayerQueries
FROM layers l, queries q
WHERE l.name='#CAPA#' and l.id_layer=q.id_layer;


-- 4a Inserccion Table
--SELECT E'INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval(\'seq_tables\'),\'#TABLA#\',5)' as sqlTableCreate;
-- 4b Creación columnas
     --Añadir COLUMN_DOMAINS donde corresponda
     --INSERT INTO COLUMNS_DOMAINS(ID_COLUMN,ID_DOMAIN,LEVEL) VALUES (CURRVAL('seq_columns'),10067,0);
     --id_domain 10067:id 10068:id_municipio
     
SELECT 'INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval(\'seq_columns\'),CURRVAL(\'seq_tables\'),null,\'' || c.column_name || '\','|| COALESCE(c.character_maximum_length,0) || ',' || COALESCE(c.numeric_precision,0) || ',' || COALESCE(c.numeric_scale,0) || ',' || CASE WHEN c.data_type='numeric' THEN '2' WHEN c.data_type='integer' THEN '2'  WHEN c.data_type='bigint' THEN '2' WHEN c.data_type='character varying' THEN '3' WHEN c.data_type='USER-DEFINED' THEN '1' ELSE c.data_type END || '); --' ||   tables.id_table || ' ' || c.table_name || '.' || c.column_name || ' ' || c.data_type 
as sqlQuotedINSERT
FROM information_schema.columns c, tables
where c.table_name = '#TABLA#' 
 and c.table_name=tables.name  
order by c.table_name, c.ordinal_position;


SELECT  E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||c.column_name||E'\');\n' || E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'#TABLA#\' and c.name=\''||c.column_name||E'\')'||E','||'POSITION'||',true);' as sqlTableColumnsLayerAtributes
FROM information_schema.columns c, tables
where c.table_name = '#TABLA#' 
 and c.table_name=tables.name  
order by c.table_name, c.ordinal_position;
-- 5 Insercion dictionary, attributes

SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||d.traduccion||E'\');\n' || E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'#TABLA#\' and c.name=\''||c.name||E'\')'||E','||a.position||',true);' as sqlTableColumnsLayerAtributes
FROM dictionary d, columns c, attributes a, tables t
WHERE t.name='#TABLA#'  and
     c.id_table=t.id_table
     and a.id_layer=(SELECT id_layer FROM layers where name='#CAPA#') 
     and a.id_column=c.id and a.id_alias=d.id_vocablo
order by a.position;
