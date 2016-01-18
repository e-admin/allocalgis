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

select E'INSERT INTO STYLES (ID_STYLE, XML) values(nextval(\'seq_styles\'),\'' || s.xml || E'\');' as sqlLayerStyle
from layers l, styles s
where  l."name"= 'EIEL_Ind_II_RSaneam_saneamau'
  and  s.id_style=l.id_styles;

 
-- 2a Creación de capa
SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''|| l.name ||E'\');' || E'\nINSERT INTO LAYERS(ID_LAYER,ID_NAME,ID_STYLES,ACL,NAME,MODIFICABLE,ID_ENTIDAD) \n   VALUES (NEXTVAL(\'seq_layers\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_styles\'),(SELECT idacl FROM acl WHERE name=\'Capas EIEL Indicadores\'),\''||l.name|| E'\',1,0);'   as sqlLayerCreate
FROM layers l
WHERE l.name='EIEL_Ind_II_RSaneam_saneamau';

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
INSERT INTO maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values ((select id_map from maps m, dictionary d where m.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Red de saneamiento'),(SELECT id_layerfamily FROM layerfamilies lf, dictionary d WHERE lf.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Red de saneamiento'),1,0);
INSERT INTO layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values ((select id_map from maps m, dictionary d where m.id_name=d.id_vocablo and d.traduccion='EIEL_Indicadores_II_Red de saneamiento'),currval('seq_layerfamilies'),currval('seq_layers'),currval('seq_styles'),'default:EIEL_Indicadores_II_Red de saneamiento', true,0,0,true,true);

-- 3 Queries de la capa
SELECT E'INSERT INTO QUERIES(ID,ID_LAYER,DATABASETYPE,SELECTQUERY,UPDATEQUERY,INSERTQUERY,DELETEQUERY) VALUES (NEXTVAL(\'seq_queries\'),CURRVAL(\'seq_layers\'),1,\n' || E'\''|| q.selectquery || E'\',\n' ||E'\''|| q.updatequery || E'\',\n' ||E'\''|| q.insertquery || E'\',\n' ||E'\''|| q.deletequery ||E'\');' as sqlLayerQueries
FROM layers l, queries q
WHERE l.name='EIEL_Ind_II_RSaneam_saneamau' and l.id_layer=q.id_layer;


-- 4 Inserccion Table
SELECT E'INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval(\'seq_tables\'),\'eiel_indicadores_d_rsaneam\',5)' as sqlTableCreate;

-- 5a Inserccion dictionary, columns y attributes

SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||d.traduccion||E'\');\n' ||E'INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval(\'seq_columns\'),CURRVAL(\'seq_tables\'),'||COALESCE(c.id_domain,0)||E',\''||c.name||E'\','||COALESCE(c."Length",0)||','||COALESCE(c."Precision",0)||','||COALESCE(c."Scale",0)||','||c."Type"||');\n'||E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),CURRVAL(\'seq_columns\'),'||a.position||',true);' as sqlTableColumnsLayerAtributes

from dictionary d, columns c, attributes a
where    c.id_table=(SELECT id_table FROM tables WHERE NAME='eiel_indicadores_d_rsaneam') 
     and a.id_layer=(SELECT id_layer FROM layers where name='EIEL_Ind_II_RSaneam_saneamau') 
     and a.id_column=c.id and a.id_alias=d.id_vocablo
order by a.position;

-- ¡ADEVERTENCIA! Ejecutar en lugar de 5a para la segunda capa en adelante sobre la misma table

-- 5b Atributos de la capa si ya se han creado antes las columnas y las entradas de diccionario
/*
SELECT E'INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL(\'seq_dictionary\'),\'es_ES\',\''||d.traduccion||E'\');\n' || E'INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL(\'seq_attributes\'),CURRVAL(\'seq_dictionary\'),CURRVAL(\'seq_layers\'),'|| E'(SELECT c.id FROM columns c, tables t WHERE c.id_table=t.id_table and t.name=\'eiel_indicadores_d_poblacion\' and c.name=\''||c.name||E'\')'||E','||a.position||',true);' as sqlTableColumnsLayerAtributes
FROM dictionary d, columns c, attributes a, tables t
WHERE t.name='eiel_indicadores_d_rsaneam'  and
     c.id_table=t.id_table
     and a.id_layer=(SELECT id_layer FROM layers where name='EIEL_Ind_II_RSaneam_saneamau') 
     and a.id_column=c.id and a.id_alias=d.id_vocablo
order by a.position;
*/