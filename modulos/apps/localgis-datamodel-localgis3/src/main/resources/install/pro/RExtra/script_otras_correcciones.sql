
CREATE OR REPLACE FUNCTION ecolectivah_function()
  RETURNS trigger AS
$BODY$
  BEGIN
    update ecolectivah set fbaj=NEW.fdoc where ecolectivah.niden_centco=NEW.niden_centco and ecolectivah.fbaj=99999999;
    
    insert into ecolectivah values (new.niden_centco, new.id_municipio, new.centco, new.d_centco, new.dc_centco, new.nombrecooficial, new.descripcion, 
    new."GEOMETRY", new.area, new.length, new.niden_cmun, new.niden_centco_ine, new.cpro, new.cmun, new.dfi_centco, new.dn_centco, new.cod_entidad,  
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION ecolectivah_function() OWNER TO postgres;


/*
DELETE FROM localgisguiaurbana.attribute;
DELETE FROM localgisguiaurbana.css;
DELETE FROM localgisguiaurbana.layer;
DELETE FROM localgisguiaurbana.legend;
DELETE FROM localgisguiaurbana.map;
DELETE FROM localgisguiaurbana.map_server_layer;
DELETE FROM localgisguiaurbana.maplayer;
DELETE FROM localgisguiaurbana.marker;
DELETE FROM localgisguiaurbana.restricted_attribute;
DELETE FROM localgisguiaurbana.style;
*/


-- elimina el problema con los dos dominios del tipo Sin Categoria
update domains set id_category=0 where id_category is null;

-- Elimina un domain category porque no se usa y no tiene traduccion en dictionary
--delete from domaincategory where id=52;

-- Eliminamos la restriccion NOT NULL sobre la columna niden_cvia para poder importar los datos
ALTER TABLE app ALTER COLUMN niden_cvia  DROP  NOT NULL;


--***********************************************************************************************
-- BORRAR CON EL GESTOR DE CAPAS LA FAMILIA DE CAPAS MUNI.
--***********************************************************************************************

--Renombrar la capa y la familia de capas Municipios que ahora se llamaban Concejos
/*
update dictionary set traduccion='Municipios' where id_vocablo=303;
update dictionary set traduccion='Municipios' where id_vocablo=103;
*/

-------------------------------------------------------------------------------------------------
-- Borrado de layerfamilies que estan vac, sin layers, y que se refieren a Modelo
-------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------------------
-- Borrar la tabla del sistema lcg_vados
/*delete from columns_domains where id_column in (select id from columns where id_table in (select id_table from tables where name = 'lcg_vados'));
delete from columns where id_table in (select id_table from tables where name = 'lcg_vados');
delete from tables where name = 'lcg_vados';
delete from attributes where id_column in (select id from columns where id_table in (select id_table from tables where name = 'lcg_vados'));
*/

-- Borrar tablas del editor FIP que son restos de pruebas con un municipio
/*
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN

drop table planeamiento_affecciones_16078;
drop table planeamiento_categoria_16078;
drop table planeamiento_clasificacion_16078;
drop table planeamiento_desarrollo_16078;
drop table planeamiento_equidistribucion_16078;
drop table planeamiento_gestion_16078;
drop table planeamiento_sistemas_16078;
drop table planeamiento_zona_16078;


	exception when others then
       RAISE NOTICE 'Error al borrar las tablas';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "Error al borrar las tablas";
*/


delete from columns_domains where id_column in (select id from columns where id_table in (select id_table from tables where name like '%16078'));
delete from attributes where id_column in (select id from columns where id_table in (select id_table from tables where name like '%16078'));
delete from columns where id_table in (select id_table from tables where name like '%16078');
delete from tables where name like '%16078';
delete from attributes where id_column in (select id from columns where id_table in (select id_table from tables where name like '%16078'));


-- Borrar tablas de sistema con sus columna del editor FIP que son restos de pruebas de creacion de capas
delete from columns_domains where id_column in (select id from columns where id_table in (select id_table from tables where name like 'planeamiento_%_gen' and id_table not in (select id_table from tables where id_table in (select id_table from columns where id in (select id_column from attributes where id_layer in (select id_layer from queries where selectquery like '%\"planeamiento_%'))))));
delete from attributes where id_column in (select id from columns where id_table in (select id_table from tables where name like 'planeamiento_%_gen' and id_table not in (select id_table from tables where id_table in (select id_table from columns where id in (select id_column from attributes where id_layer in (select id_layer from queries where selectquery like '%\"planeamiento_%'))))));
delete from columns where id_table in (select id_table from tables where name like 'planeamiento_%_gen' and id_table not in (select id_table from tables where id_table in (select id_table from columns where id in (select id_column from attributes where id_layer in (select id_layer from queries where selectquery like '%\"planeamiento_%')))));
delete from tables where name like 'planeamiento_%_gen' and id_table not in (select id_table from tables where id_table in (select id_table from columns where id in (select id_column from attributes where id_layer in (select id_layer from queries where selectquery like '%\"planeamiento_%'))));


-- Borrado de datos de la tabla coverage_layers
delete from coverage_layers;


--Borrado de ACL's que sobran
/*
delete from r_usr_perm where idacl in (select idacl from acl where name in ('MDL_Planeamiento', 'MDL_Turismo', 'MDL_Callejero', '33066_Callejero Siero', 'SICALWIN', '_prueba', 'Borrar', 'MANCOSI_Turismo', '<sin definir>'));
delete from r_group_perm where idacl in (select idacl from acl where name in ('MDL_Planeamiento', 'MDL_Turismo', 'MDL_Callejero', '33066_Callejero Siero', 'SICALWIN', '_prueba', 'Borrar', 'MANCOSI_Turismo', '<sin definir>'));
delete from r_acl_perm where idacl in (select idacl from acl where name in ('MDL_Planeamiento', 'MDL_Turismo', 'MDL_Callejero', '33066_Callejero Siero', 'SICALWIN', '_prueba', 'Borrar', 'MANCOSI_Turismo', '<sin definir>'));
delete from app_acl where acl in (select idacl from acl where name in ('MDL_Planeamiento', 'MDL_Turismo', 'MDL_Callejero', '33066_Callejero Siero', 'SICALWIN', '_prueba', 'Borrar', 'MANCOSI_Turismo', '<sin definir>'));
delete from acl where name in ('MDL_Planeamiento', 'MDL_Turismo', 'MDL_Callejero', '33066_Callejero Siero', 'SICALWIN', '_prueba', 'Borrar', 'MANCOSI_Turismo', '<sin definir>');
*/

--Borrar roles que sobran
/*
delete from r_group_perm where groupid in (select id from iusergrouphdr where name in ('TUR_MANCOSI', 'SICALWIN'));
delete from iusergroupuser where groupid in (select id  from iusergrouphdr where name in ('TUR_MANCOSI', 'SICALWIN'));
delete from iusergrouphdr where name in ('TUR_MANCOSI', 'SICALWIN');
*/

-- Correccion orden capas callejero
update layers_styles set position=4 where id_map=330 and id_layer=3;
update layers_styles set position=1 where id_map=330 and id_layer=11;
update layers_styles set position=2 where id_map=330 and id_layer=16;
update layers_styles set position=0 where id_map=330 and id_layer=12;
update layers_styles set position=3 where id_map=330 and id_layer=104;

-- Correcciara que se carguen las capas del callejero
/*insert into maps_layerfamilies_relations values(330,12,0,0);
insert into maps_layerfamilies_relations values(330,8,1,0);
insert into maps_layerfamilies_relations values(330,135,2,0);
insert into maps_layerfamilies_relations values(330,3,3,0);
*/

--Borrado de mapas de la entidad 77 de modelo
/*
delete from maps_wms_relations where id_entidad<>0 and id_map in(select id_map from maps where id_entidad<>0);
delete from layers_styles where id_entidad<>0 and id_map in(select id_map from maps where id_entidad<>0);
delete from maps_layerfamilies_relations where id_entidad<>0 and id_map in(select id_map from maps where id_entidad<>0);
delete from maps where id_entidad<>0;

-- Borrado de licencias de mapas de modelo o de pruebas
delete from maps_wms_relations where id_map in(select id_map from maps where id_map in (365,436,441,457,459,462,547,554,470,556,573,580,596,650,662,668,687,690,691,692,694,696));
delete from layers_styles where id_map in(select id_map from maps where id_map in (365,436,441,457,459,462,547,554,470,556,573,580,596,650,662,668,687,690,691,692,694,696));
delete from maps_layerfamilies_relations where id_map in(select id_map from maps where id_map in (365,436,441,457,459,462,547,554,470,556,573,580,596,650,662,668,687,690,691,692,694,696));
delete from maps where id_map in (365,436,441,457,459,462,547,554,470,556,573,580,596,650,662,668,687,690,691,692,694,696);

delete from maps_wms_relations where id_map in(select id_map from maps where id_map in (283,571));
delete from layers_styles where id_map in(select id_map from maps where id_map in (283,571));
delete from maps_layerfamilies_relations where id_map in(select id_map from maps where id_map in (283,571));
delete from maps where id_map in (283,571);


--Borrar familias de capas que sobran
delete from dictionary where id_vocablo in (select d.id_vocablo from layerfamilies lf, dictionary d where lf.id_name=d.id_vocablo and d.locale='es_ES' and d.traduccion in ('_TOLEDO_LG3', 'Almeria', 'Aprovechamiento_ganadero_agricola_y_otros', 'Aprovechamientos_forestales', 'CAMINOS_CNarcea', 'Caminos_Nava', 'Ferrocarril_3', 'MDL_Callejero', 'MDL_Planeamiento_TEST', 'MDL_Turismo MANCOSI', 'Solo TCN', 'Turismo Manc. Sidra'));

delete from layers_styles ls where ls.id_map in (select m.id_map from maps_layerfamilies_relations m where m.id_layerfamily in (select id_layerfamily from layerfamilies where id_layerfamily not in (select lf.id_layerfamily from layerfamilies lf, dictionary d where lf.id_name=d.id_vocablo and d.locale='es_ES')));
delete from maps_layerfamilies_relations m where m.id_layerfamily in (select id_layerfamily from layerfamilies where id_layerfamily not in (select lf.id_layerfamily from layerfamilies lf, dictionary d where lf.id_name=d.id_vocablo and d.locale='es_ES'));
delete from layers_styles s where s.id_layerfamily in (select id_layerfamily from layerfamilies where id_layerfamily not in (select lf.id_layerfamily from layerfamilies lf, dictionary d where lf.id_name=d.id_vocablo and d.locale='es_ES'));
delete from layerfamilies_layers_relations r where r.id_layerfamily in (select id_layerfamily from layerfamilies where id_layerfamily not in (select lf.id_layerfamily from layerfamilies lf, dictionary d where lf.id_name=d.id_vocablo and d.locale='es_ES'));
delete from layerfamilies where id_layerfamily not in (select lf.id_layerfamily from layerfamilies lf, dictionary d where lf.id_name=d.id_vocablo and d.locale='es_ES');


-- Borrar capas que sobran
delete from tables where id_table in (select id_table from columns where id in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%_16078%')));
delete from columns_domains where id_column in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%_16078%'));
delete from columns where id in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%_16078%'));
delete from attributes where id_layer in (select id_layer from layers where name like'%_16078%');
delete from layerfamilies_layers_relations where id_layer in (select id_layer from layers where name like'%_16078%');
delete from layers_styles where id_layer in (select id_layer from layers where name like'%_16078%');
delete from queries where id_layer in (select id_layer from layers where name like'%_16078%');
delete from layers where name like'%_16078%';

-- Borrar capas que sobran
delete from tables where id_table in (select id_table from columns where id in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%AAA%')));
delete from columns_domains where id_column in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%AAA%'));
delete from columns where id in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%AAA%'));
delete from attributes where id_layer in (select id_layer from layers where name like'%AAA%');
delete from layerfamilies_layers_relations where id_layer in (select id_layer from layers where name like'%AAA%');
delete from layers_styles where id_layer in (select id_layer from layers where name like'%AAA%');
delete from queries where id_layer in (select id_layer from layers where name like'%AAA%');
delete from layers where name like'%AAA%';

-- Borrar capas que sobran
delete from tables where id_table in (select id_table from columns where id in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%Asturias%')));
delete from columns_domains where id_column in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%Asturias%'));
delete from columns where id in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%Asturias%'));
delete from attributes where id_layer in (select id_layer from layers where name like'%Asturias%');
delete from layerfamilies_layers_relations where id_layer in (select id_layer from layers where name like'%Asturias%');
delete from layers_styles where id_layer in (select id_layer from layers where name like'%Asturias%');
delete from queries where id_layer in (select id_layer from layers where name like'%Asturias%');
delete from layers where name like'%Asturias%';



-- Borrado de capas
delete from tables where id_table in (select id_table from columns where id in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%caminos_cn5%'  or name like'%L_interes_LIC%' or name like'%Vias_de_saca%' or name like'%Corta%' or name like'%Cargaderos%' or name like'%Playas%' or name like'%Aprovechamiento_ganadero_agricola_y_otros%' or name like'%puntos%')));
delete from columns_domains where id_column in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%caminos_cn5%'  or name like'%L_interes_LIC%' or name like'%Vias_de_saca%' or name like'%Corta%' or name like'%Cargaderos%' or name like'%Playas%' or name like'%Aprovechamiento_ganadero_agricola_y_otros%' or name like'%puntos%'));
delete from columns where id in (select id_column from attributes where id_layer in (select id_layer from layers where name like'%caminos_cn5%'  or name like'%L_interes_LIC%' or name like'%Vias_de_saca%' or name like'%Corta%' or name like'%Cargaderos%' or name like'%Playas%' or name like'%Aprovechamiento_ganadero_agricola_y_otros%' or name like'%puntos%'));
delete from attributes where id_layer in (select id_layer from layers where name like'%caminos_cn5%'  or name like'%L_interes_LIC%' or name like'%Vias_de_saca%' or name like'%Corta%' or name like'%Cargaderos%' or name like'%Playas%' or name like'%Aprovechamiento_ganadero_agricola_y_otros%' or name like'%puntos%');
delete from layerfamilies_layers_relations where id_layer in (select id_layer from layers where name like'%caminos_cn5%' or name like'%L_interes_LIC%' or name like'%Vias_de_saca%' or name like'%Corta%' or name like'%Cargaderos%' or name like'%Playas%' or name like'%Aprovechamiento_ganadero_agricola_y_otros%' or name like'%puntos%');
delete from layers_styles where id_layer in (select id_layer from layers where name like'%caminos_cn5%'  or name like'%L_interes_LIC%' or name like'%Vias_de_saca%' or name like'%Corta%' or name like'%Cargaderos%' or name like'%Playas%' or name like'%Aprovechamiento_ganadero_agricola_y_otros%' or name like'%puntos%');
delete from queries where id_layer in (select id_layer from layers where name like'%caminos_cn5%'  or name like'%L_interes_LIC%' or name like'%Vias_de_saca%' or name like'%Corta%' or name like'%Cargaderos%' or name like'%Playas%' or name like'%Aprovechamiento_ganadero_agricola_y_otros%' or name like'%puntos%');
delete from layers where name like'%caminos_cn5%' or name like'%L_interes_LIC%' or name like'%Vias_de_saca%' or name like'%Corta%' or name like'%Cargaderos%' or name like'%Playas%' or name like'%Aprovechamiento_ganadero_agricola_y_otros%' or name like'%puntos%';
*/

--Borrado de tablas no necesarias
-- Borrar tablas del editor FIP que son restos de pruebas con un municipio
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN

drop table sadim_eiel_t_abast_au;
drop table sadim_eiel_t_abast_serv;
drop table sadim_eiel_t_as;
drop table sadim_eiel_t_en;
drop table sadim_eiel_t_en_nivel;
drop table sadim_eiel_t_mun_diseminados;
drop table sadim_eiel_t_nucl_encuest_1;
drop table sadim_eiel_t_padron_nd;
drop table sadim_eiel_t_padron_ttmm;
drop table sadim_eiel_t_sa;
drop table sadim_eiel_t_saneam_au;
drop table sadim_eiel_t_saneam_serv;

	exception when others then
       RAISE NOTICE 'Error al borrar las tablas';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "Error al borrar las tablas";



-- Renombrar estilos
/*
update styles set xml = replace(xml, 'MODELO', 'LCG3') where upper(xml) like upper('%modelo%') or upper(xml) like upper('%asturias%');
update styles set xml = replace(xml, 'Asturias', 'LCG3') where upper(xml) like upper('%modelo%') or upper(xml) like upper('%asturias%');

update layers_styles set stylename = replace(stylename, 'MODELO', 'LCG3') where upper(stylename) like upper('%modelo%') or upper(stylename) like upper('%asturias%');
update layers_styles set stylename = replace(stylename, 'Asturias', 'LCG3') where upper(stylename) like upper('%modelo%') or upper(stylename) like upper('%asturias%');
*/

-- Dar permisos al usuario localgisbackup para que funcione el modulo de backup
GRANT all ON SCHEMA localgisguiaurbana TO localgisbackup WITH GRANT OPTION;
GRANT all ON SCHEMA cementerio TO localgisbackup WITH GRANT OPTION;

CREATE OR REPLACE FUNCTION createrole() RETURNS VOID AS
$Q$
BEGIN
GRANT all ON SCHEMA geowfst TO localgisbackup WITH GRANT OPTION; 
	exception when others then
		RAISE NOTICE 'El esquema padron no existe';
END;
$Q$
LANGUAGE plpgsql;
select createrole() as "geowfst";


CREATE OR REPLACE FUNCTION createrole() RETURNS VOID AS
$Q$
BEGIN
GRANT all ON SCHEMA padron TO localgisbackup WITH GRANT OPTION;
	exception when others then
		RAISE NOTICE 'El esquema padron no existe';
END;
$Q$
LANGUAGE plpgsql;
select createrole() as "padron";



GRANT all ON SCHEMA public TO localgisbackup WITH GRANT OPTION;
GRANT all ON SCHEMA visualizador TO localgisbackup WITH GRANT OPTION;
GRANT all ON SCHEMA wfsg_sample TO localgisbackup WITH GRANT OPTION;

CREATE OR REPLACE FUNCTION createrole() RETURNS VOID AS
$Q$
BEGIN
	GRANT ALL ON TABLE padron.nomenclator_2011 TO localgisbackup;
GRANT ALL ON TABLE padron.nomenclator_2012 TO localgisbackup;
 
	exception when others then
		RAISE NOTICE 'El esquema padron no existe';
END;
$Q$
LANGUAGE plpgsql;
select createrole() as "GRANT ALL ON TABLE";



---------------------------------------------------------------------------------------------------------------------------------------------------
--SENTENCIA PARA OBTENER LA INSTRUCCIONES PARA DAR PERMISOS AL USUARIO SOBRE TODAS LAS TABLAS DEL SISTEMA------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------
--select 'GRANT SELECT ON TABLE '|| table_name ||' TO localgisbackup;' from information_schema.tables where table_schema in ('localgisguiaurbana', 'cementerio', 'geowfst', 'gestorfip', 'padron', 'public', 'visualizador', 'wfsg_sample')


---------------------------------------------------------------------------------------------------------------------------------------------------
--SENTENCIA PARA poner bien las secuencias---------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------------------
--select 'Select setval('''|| sequence_name ||''', (select coalesce(max(id),0)+1 from '|| substring(sequence_name, 5, length(sequence_name)) ||')::bigint);' from information_schema.sequences where sequence_schema in ('public') and sequence_name like 'seq_%' order by sequence_name asc




-- Se anaden los permisos que faltan al rol superadministrador
insert into r_group_perm select '110', idperm, idacl from r_acl_perm where idperm in (select idperm from usrgrouperm where idperm not in (select distinct idperm from r_group_perm where groupid=110));

--Se anaden los permisos que faltan al administrador
insert into r_usr_perm select '100', idperm, idacl, '1' from r_acl_perm where idperm in (select idperm from usrgrouperm where idperm not in (select distinct idperm from r_usr_perm where userid=100));



-- Anadir los acls que faltan a las aplicaciones
/*insert into app_acl values (22,8);
insert into app_acl values (21,3);
insert into app_acl values (0,13);
insert into app_acl values (7,20);
insert into app_acl values (0,26);
insert into app_acl values (0,50);
insert into app_acl values (0,102);
insert into app_acl values (0,156);
insert into app_acl values (11,165);
insert into app_acl values (0,183);
insert into app_acl values (0,198);
*/


-- Borrar acl
delete from r_usr_perm where idacl in (select idacl from acl where name in ('Terysos'));
delete from r_group_perm where idacl in (select idacl from acl where name in ('Terysos'));
delete from r_acl_perm where idacl in (select idacl from acl where name in ('Terysos'));
delete from app_acl where acl in (select idacl from acl where name in ('Terysos'));
delete from acl where name in ('Terysos');

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN

--Creacion de la funcion perimeter
CREATE OR REPLACE FUNCTION perimeter(geometry)
  RETURNS double precision AS
'$libdir/postgis-2.0', 'LWGEOM_perimeter2d_poly'
  LANGUAGE c IMMUTABLE STRICT
  COST 1;
ALTER FUNCTION perimeter(geometry)
  OWNER TO postgres;
	exception when others then
       RAISE NOTICE 'La FUNCION PERIMETER NO SE PUEDE CREAR';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "CREATE FUNCTION PERIMETER";




  
  
--Actualizacion del nombre corto de municipio porque es obligatorio en el modelo de direcciones del AGE
update municipio set dc_cmun=d_cmun;














