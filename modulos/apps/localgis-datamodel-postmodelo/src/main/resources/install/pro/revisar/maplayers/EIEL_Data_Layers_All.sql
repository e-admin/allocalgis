
------- ### ACL EIEL_Indicadores ###
--select * from acl where name='Capas EIEL Indicadores'

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Capas EIEL Indicadores') THEN	
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
	END IF;


END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";




-------- ### MAPA DE Poblacion ####
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_I_Poblacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','EIEL_Indicadores_I_Poblacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','EIEL_Indicadores_I_Poblacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','EIEL_Indicadores_I_Poblacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','EIEL_Indicadores_I_Poblacion');

INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?>
<mapDescriptor><description></description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>UTM 29N ED50</mapProjection><mapName>EIEL_Indicadores_I_Poblacion</mapName></mapDescriptor>
',0);

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_I_Poblacion')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_I_Poblacion'),(select id_layer from layers where name='EIEL_Indicadores_I_Poblacion'),(select id_styles from layers where name='EIEL_Indicadores_I_Poblacion'),'"EIEL_Indicadores_I_Poblacion:_:default:EIEL_Indicadores_I_Poblacion"', true,0,0,true,true);
    

------
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_Captaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_Captaciones');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_II_Captaciones</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_II_Captaciones</mapName></mapDescriptor>',0);	

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Captaciones')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Captaciones'),(select id_layer from layers where name='EIEL_Indicadores_II_Captaciones'),(select id_styles from layers where name='EIEL_Indicadores_II_Captaciones'),'"EIEL_Indicadores_II_Captaciones:_:default:EIEL_Indicadores_II_Captaciones"', true,0,0,true,true);

----------

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Depositos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_Depositos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_Depositos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_Depositos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_Depositos');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_II_Depositos</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_II_Depositos</mapName></mapDescriptor>',0);

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Depositos')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Depositos'),(select id_layer from layers where name='EIEL_Indicadores_II_Depositos'),(select id_styles from layers where name='EIEL_Indicadores_II_Depositos'),'"EIEL_Indicadores_II_Depositos:_:default:EIEL_Indicadores_II_Depositos"', true,0,0,true,true);

-------

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Potabilizacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_Potabilizacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_Potabilizacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_Potabilizacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_Potabilizacion');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_II_Potabilizacion</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_II_Potabilizacion</mapName></mapDescriptor>',0);

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Potabilizacion')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Potabilizacion'),(select id_layer from layers where name='EIEL_Indicadores_II_Potabilizacion'),(select id_styles from layers where name='EIEL_Indicadores_II_Potabilizacion'),'"EIEL_Indicadores_II_Potabilizacion:_:default:EIEL_Indicadores_II_Potabilizacion"', true,0,0,true,true);

------
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_RedDistribucion');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_II_RedDistribucion</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_II_RedDistribucion</mapName></mapDescriptor>',0);

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_RedDistribucion')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_RedDistribucion'),(select id_layer from layers where name='EIEL_Indicadores_II_RedDistribucion'),(select id_styles from layers where name='EIEL_Indicadores_II_RedDistribucion'),'"EIEL_Indicadores_II_RedDistribucion:_:default:EIEL_Indicadores_II_RedDistribucion"', true,0,0,true,true);


------

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(nextval('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_II_Red de saneamiento</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa de Cementerios</mapName></mapDescriptor>',0);

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Red de saneamiento')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Red de saneamiento'),(select id_layer from layers where name='EIEL_Indicadores_II_Red de saneamiento'),(select id_styles from layers where name='EIEL_Indicadores_II_Red de saneamiento'),'"EIEL_Indicadores_II_Red de saneamiento:_:default:EIEL_Indicadores_II_Red de saneamiento"', true,0,0,true,true);

------
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(35,CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_II_Red de saneamiento</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>Mapa de Cementerios</mapName></mapDescriptor>',0);


insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Red de saneamiento')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_II_Red de saneamiento'),(select id_layer from layers where name='EIEL_Indicadores_II_Red de saneamiento'),(select id_styles from layers where name='EIEL_Indicadores_II_Red de saneamiento'),'"EIEL_Indicadores_II_Red de saneamiento:_:default:EIEL_Indicadores_II_Red de saneamiento"', true,0,0,true,true);


----
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_III_Alumbrado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_III_Alumbrado');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_III_Alumbrado</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_III_Alumbrado</mapName></mapDescriptor>',0);

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_III_Alumbrado')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_III_Alumbrado'),(select id_layer from layers where name='EIEL_Indicadores_III_Alumbrado'),(select id_styles from layers where name='EIEL_Indicadores_III_Alumbrado'),'"EIEL_Indicadores_III_Alumbrado:_:default:EIEL_Indicadores_III_Alumbrado"', true,0,0,true,true);

-----
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Comunicaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_III_Comunicaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_III_Comunicaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_III_Comunicaciones');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_III_Comunicaciones');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_III_Comunicaciones</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_III_Comunicaciones</mapName></mapDescriptor>',0);

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_III_Comunicaciones')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_III_Comunicaciones'),(select id_layer from layers where name='EIEL_Indicadores_III_Comunicaciones'),(select id_styles from layers where name='EIEL_Indicadores_III_Comunicaciones'),'"EIEL_Indicadores_III_Comunicaciones:_:default:EIEL_Indicadores_III_Comunicaciones"', true,0,0,true,true);


------


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_III_Suministros');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_III_Suministros');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_III_Suministros</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_III_Suministros</mapName></mapDescriptor>',0);

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_III_Suministros')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_III_Suministros'),(select id_layer from layers where name='EIEL_Indicadores_III_Suministros'),(select id_styles from layers where name='EIEL_Indicadores_III_Suministros'),'"EIEL_Indicadores_III_Suministros:_:default:EIEL_Indicadores_III_Suministros"', true,0,0,true,true);

-----

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_IV_RecogidaBasurasLimpieza</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_IV_RecogidaBasurasLimpieza</mapName></mapDescriptor>',0);

insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_IV_RecogidaBasurasLimpieza')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_IV_RecogidaBasurasLimpieza'),(select id_layer from layers where name='EIEL_Indicadores_IV_RecogidaBasurasLimpieza'),(select id_styles from layers where name='EIEL_Indicadores_IV_RecogidaBasurasLimpieza'),'"EIEL_Indicadores_IV_RecogidaBasurasLimpieza:_:default:EIEL_Indicadores_IV_RecogidaBasurasLimpieza"', true,0,0,true,true);


-----

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_V_CEnsenanzaDeporteCultura');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_V_CEnsenanzaDeporteCultura');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_V_CEnsenanzaDeporteCultura');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_V_CEnsenanzaDeporteCultura');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_V_CEnsenanzaDeporteCultura');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_V_CEnsenanzaDeporteCultura</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_V_CEnsenanzaDeporteCultura</mapName></mapDescriptor>',0);



insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_V_CEnsenanzaDeporteCultura')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_V_CEnsenanzaDeporteCultura'),(select id_layer from layers where name='EIEL_Indicadores_V_CEnsenanzaDeporteCultura'),(select id_styles from layers where name='EIEL_Indicadores_V_CEnsenanzaDeporteCultura'),'"EIEL_Indicadores_V_CEnsenanzaDeporteCultura:_:default:EIEL_Indicadores_V_CEnsenanzaDeporteCultura"', true,0,0,true,true);

------

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_VI_SanitarioAsistencial</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_VI_SanitarioAsistencial</mapName></mapDescriptor>',0);


insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_VI_SanitarioAsistencial')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_VI_SanitarioAsistencial'),(select id_layer from layers where name='EIEL_Indicadores_VI_SanitarioAsistencial'),(select id_styles from layers where name='EIEL_Indicadores_VI_SanitarioAsistencial'),'"EIEL_Indicadores_VI_SanitarioAsistencial:_:default:EIEL_Indicadores_VI_SanitarioAsistencial"', true,0,0,true,true);


------


INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','EIEL_Indicadores_VII_OtrosServicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]EIEL_Indicadores_VII_OtrosServicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]EIEL_Indicadores_VII_OtrosServicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]EIEL_Indicadores_VII_OtrosServicios');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]EIEL_Indicadores_VII_OtrosServicios');
INSERT INTO MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(NEXTVAL('seq_maps'),CURRVAL('seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>EIEL_Indicadores_VII_OtrosServicios</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>Unspecified</mapProjection><mapName>EIEL_Indicadores_VII_OtrosServicios</mapName></mapDescriptor>',0);



insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_VII_OtrosServicios')),0,0); 
   

insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) values (currval('seq_maps'),(select id_layerfamily from layerfamilies_layers_relations where id_layer=(select id_layer from layers where name='EIEL_Indicadores_VII_OtrosServicios'),(select id_layer from layers where name='EIEL_Indicadores_VII_OtrosServicios'),(select id_styles from layers where name='EIEL_Indicadores_VII_OtrosServicios'),'"EIEL_Indicadores_VII_OtrosServicios:_:default:EIEL_Indicadores_VII_OtrosServicios"', true,0,0,true,true);

-------


