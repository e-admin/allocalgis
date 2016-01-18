-- Estas sentencias tardan tiempo en ejecutarse porque la tabla de parcelas puede tener muchos registros
ALTER TABLE public.parcelas ALTER COLUMN ninterno TYPE numeric(10,0);
ALTER TABLE public.parcelas ALTER COLUMN numsymbol TYPE numeric(2,0);

UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_rs"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, eiel_c_saneam_rs.codprov, eiel_c_saneam_rs.codmunic, eiel_c_saneam_rs.codentidad, eiel_c_saneam_rs.codpoblamiento, material as tramo_rs, sist_impulsion, estado, tipo_red_interior, titular, gestor FROM "eiel_c_saneam_rs",ONLY eiel_t_nucl_encuest_1, eiel_configuracion_shp WHERE "eiel_c_saneam_rs"."id_municipio" in (?M) and eiel_c_saneam_rs.revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = eiel_c_saneam_rs.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_c_saneam_rs.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_c_saneam_rs.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_c_saneam_rs.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)'where id_layer = (SELECT id_layer from layers where name = 'RAMAL_SANEAMIENTO_SHP');

UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_rd"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, eiel_c_abast_rd.codprov, eiel_c_abast_rd.codmunic, eiel_c_abast_rd.codentidad, eiel_c_abast_rd.codpoblamiento, material as tramo_rd, sist_trans, estado, titular, gestor FROM "eiel_c_abast_rd", ONLY eiel_t_nucl_encuest_1,eiel_configuracion_shp WHERE "eiel_c_abast_rd"."id_municipio" in (?M) and eiel_c_abast_rd.revision_expirada = 9999999999  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = eiel_c_abast_rd.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_c_abast_rd.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_c_abast_rd.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_c_abast_rd.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)'where id_layer = (SELECT id_layer from layers where name = 'RED_DISTRIBUCION_SHP');
 

UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_nucleo_poblacion"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, eiel_c_nucleo_poblacion.codprov, eiel_c_nucleo_poblacion.codmunic, eiel_c_nucleo_poblacion.codentidad,eiel_c_nucleo_poblacion.codpoblamiento as  nombre_oficial FROM "eiel_c_nucleo_poblacion", ONLY eiel_t_nucl_encuest_1,eiel_configuracion_shp WHERE "eiel_c_nucleo_poblacion"."id_municipio" in (?M) and eiel_c_nucleo_poblacion.revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = eiel_c_nucleo_poblacion.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_c_nucleo_poblacion.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_c_nucleo_poblacion.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_c_nucleo_poblacion.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)'where id_layer = (SELECT id_layer from layers where name = 'NUCLEO_SHP');


UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_pv"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_saneam_pv.id, eiel_c_saneam_pv.id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_pv.clave, eiel_c_saneam_pv.codprov, eiel_c_saneam_pv.codmunic, eiel_c_saneam_pv.orden_pv as orden_emis FROM eiel_configuracion_shp,"eiel_c_saneam_pv" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_pv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_pv.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_c_saneam_pv.codmunic::text || eiel_c_saneam_pv.orden_pv::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text FROM v_emisario_nucleo ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_pv.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'''where id_layer = (SELECT id_layer from layers where name = 'EMISARIO_ENC_M50_SHP');


UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_pv"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_saneam_pv.id, eiel_c_saneam_pv.id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_pv.clave, eiel_c_saneam_pv.codprov, eiel_c_saneam_pv.codmunic, eiel_c_saneam_pv.orden_pv as orden_emis FROM eiel_configuracion_shp,"eiel_c_saneam_pv" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_pv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_pv.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_c_saneam_pv.codmunic::text || eiel_c_saneam_pv.orden_pv::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text FROM v_emisario_nucleo ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_pv.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'''where id_layer = (SELECT id_layer from layers where name = 'EMISARIO_ENC_SHP');


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	ALTER TABLE eiel_t_abast_ca DROP CONSTRAINT tipochk ;	
	exception when others then
       RAISE NOTICE 'La constrainsts tipochk no existia al borrarla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "DROP CONSTRAINT tipochk";




 
ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT  tipochk CHECK (tipo::text = 'AL'::text OR tipo::text = 'AM'::text OR tipo::text = 'AS'::text OR tipo::text = 'CA'::text OR tipo::text = 'EB'::text OR tipo::text = 'GA'::text OR tipo::text = 'MT'::text OR tipo::text = 'OT'::text OR tipo::text = 'PO'::text OR tipo::text = 'PX'::text OR tipo::text = 'RI'::text OR tipo::text = 'SO'::text) ;

delete from version where id_version = 'MODELO R40';
insert into version (id_version, fecha_version) values('MODELO R40', DATE '2014-03-27');
