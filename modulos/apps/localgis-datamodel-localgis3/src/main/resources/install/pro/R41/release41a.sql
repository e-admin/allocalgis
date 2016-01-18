--Tamo Colector

update attributes set  position='1' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'id') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%id%');

update attributes set  position='2' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'id_municipio') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%id_municipio%');

update attributes set  position='3' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'GEOMETRY') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%GEOMETRY%');

update attributes set  position='4' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%fase%');

update attributes set  position='5' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'clave') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%clave%');

update attributes set  position='6' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'codprov') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%prov%');

update attributes set  position='7' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'codmunic') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%mun%');

update attributes set  position='8' where  id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'tramo_cl') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%orden_colec%');

update attributes set  position='9' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'tipo_red_interior') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%tipo_colec%');

update attributes set position='10' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'sist_impulsion') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%sist_trans%');

update attributes set position='11' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'estado') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%estado%');

update attributes set position='12' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'titular') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%titular%');

update attributes set position='13' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'gestor') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%gestion%');


--TRAMO_COLECTOR_M50_SHP

update attributes set  position='1' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'id') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%id%');

update attributes set  position='2' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'id_municipio') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%id_municipio%');

update attributes set  position='3' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'GEOMETRY') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%GEOMETRY%');

update attributes set  position='4' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%fase%');

update attributes set  position='5' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'clave') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%clave%');

update attributes set  position='6' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'codprov') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%prov%');

update attributes set  position='7' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'codmunic') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%mun%');

update attributes set  position='8' where  id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_tcl' and columns.name = 'tramo_cl') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%orden_colec%');

update attributes set  position='9' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'tipo_red_interior') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%tipo_colec%');

update attributes set position='10' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'sist_impulsion') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%sist_trans%');

update attributes set position='11' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'estado') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%estado%');

update attributes set position='12' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'titular') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%titular%');

update attributes set position='13' where id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'gestor') and id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%gestion%');


update eiel_indicadores_m_indicadores set indicador='III.Infraestructuras-Alumbrado',mapa='EIEL_Indicadores_III_Alumbrado' where indicador='III.Infraestrucutras-Alumbrado';
update eiel_indicadores_m_indicadores set indicador='III.Infraestructuras-Alumbrado',mapa='EIEL_Indicadores_III_Alumbrado' where indicador='III.Infraestructuras-Alumbrado';

-- Problema con los Hospitales


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	ALTER TABLE eiel_t_sa DROP CONSTRAINT tipochk ;
	exception when others then
       RAISE NOTICE 'La constrainsts tipochk no existia al borrarla';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "DROP CONSTRAINT tipochk";

ALTER TABLE eiel_t_sa ADD CONSTRAINT tipochk CHECK (tipo::text = 'AMB'::text OR tipo::text = 'HGS'::text OR tipo::text = 'CDS'::text OR tipo::text = 'CLO'::text OR tipo::text = 'CUR'::text OR tipo::text = 'HGL'::text OR tipo::text = 'HIN'::text OR tipo::text = 'HLE'::text OR tipo::text = 'HOE'::text OR tipo::text = 'HPS'::text OR tipo::text = 'HQY'::text OR tipo::text = 'OTS'::text OR tipo::text = 'HQU'::text);
 

delete from version where id_version = 'MODELO R41';
insert into version (id_version, fecha_version) values('MODELO R41', DATE '2014-04-14');





