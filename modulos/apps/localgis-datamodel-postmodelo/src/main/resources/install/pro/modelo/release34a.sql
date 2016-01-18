/*DROP VIEW IF EXISTS v_alumbrado;
DROP VIEW IF EXISTS v_cap_agua_nucleo;
DROP VIEW IF EXISTS v_casa_con_uso;
DROP VIEW IF EXISTS v_casa_consistorial;
DROP VIEW IF EXISTS v_cementerio;
DROP VIEW IF EXISTS v_cent_cultural;
DROP VIEW IF EXISTS v_cent_cultural_usos;
DROP VIEW IF EXISTS v_centro_asistencial;
DROP VIEW IF EXISTS v_centro_ensenanza;
DROP VIEW IF EXISTS v_centro_sanitario;
DROP VIEW IF EXISTS v_colector_nucleo;
DROP VIEW IF EXISTS v_cond_agua_nucleo;
DROP VIEW IF EXISTS v_dep_agua_nucleo;
DROP VIEW IF EXISTS v_deposito_agua_nucleo;
DROP VIEW IF EXISTS v_edific_pub_sin_uso;
DROP VIEW IF EXISTS v_emisario_nucleo;
DROP VIEW IF EXISTS v_infraestr_viaria;
DROP VIEW IF EXISTS v_inst_depor_deporte;
DROP VIEW IF EXISTS v_instal_deportiva;
DROP VIEW IF EXISTS v_lonja_merc_feria;
DROP VIEW IF EXISTS v_matadero;
DROP VIEW IF EXISTS v_nivel_ensenanza;
DROP VIEW IF EXISTS v_nucleo_poblacion;
DROP VIEW IF EXISTS v_parque;
DROP VIEW IF EXISTS v_poblamiento;
DROP VIEW IF EXISTS v_proteccion_civil;
DROP VIEW IF EXISTS v_ramal_saneamiento;
DROP VIEW IF EXISTS v_recogida_basura;
DROP VIEW IF EXISTS v_red_distribucion;
DROP VIEW IF EXISTS v_sanea_autonomo;
DROP VIEW IF EXISTS v_tanatorio;
DROP VIEW IF EXISTS v_trat_pota_nucleo;
DROP VIEW IF EXISTS v_vertedero_nucleo;
DROP VIEW IF EXISTS v_deposito_enc;
DROP VIEW IF EXISTS v_captacion_enc;
DROP VIEW IF EXISTS v_colector_enc_m50;
DROP VIEW IF EXISTS v_colector_enc;
DROP VIEW IF EXISTS v_conduccion_enc;
DROP VIEW IF EXISTS v_depuradora_enc;
DROP VIEW IF EXISTS v_depuradora_enc_2;
DROP VIEW IF EXISTS v_emisario_enc;
DROP VIEW IF EXISTS v_potabilizacion_enc;
DROP VIEW IF EXISTS v_tramo_colector;
DROP VIEW IF EXISTS v_tramo_conduccion;
DROP VIEW IF EXISTS v_tramo_emisario;
DROP VIEW IF EXISTS v_nucl_encuestado_1;
DROP VIEW IF EXISTS v_nucl_encuestado_2;
DROP VIEW IF EXISTS v_nucl_encuestado_3;
DROP VIEW IF EXISTS v_nucl_encuestado_4;
DROP VIEW IF EXISTS v_nucl_encuestado_5;
DROP VIEW IF EXISTS v_nucl_encuestado_6;
DROP VIEW IF EXISTS v_nucl_encuestado_7;
DROP VIEW IF EXISTS v_carretera;*/


-- Esta funcion no hace falta ejecutarla porque las columnas
-- en el proceso de creacion de la capa ya se crean correctamente
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	/*IF EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'eiel_configuracion_shp' AND column_name = 'padron') THEN	
		ALTER TABLE eiel_configuracion_shp DROP COLUMN 	padron;
	END IF;	
	IF NOT EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'eiel_configuracion_shp' AND column_name = 'padron') THEN	
		ALTER TABLE eiel_configuracion_shp ADD COLUMN padron numeric(2,0);
	END IF;	
	IF EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'eiel_configuracion_shp' AND column_name = 'viviendas') THEN	
		ALTER TABLE eiel_configuracion_shp DROP COLUMN viviendas;
	END IF;
	IF NOT EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'eiel_configuracion_shp' AND column_name = 'viviendas') THEN	
		ALTER TABLE eiel_configuracion_shp ADD COLUMN viviendas numeric(2,0);
	END IF;*/
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";




UPDATE eiel_configuracion_shp set padron = 0, viviendas = 5;

UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_alum_pl"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento, ah_ener_rfl, ah_ener_rfi, estado FROM "eiel_c_alum_pl", eiel_configuracion_shp WHERE "eiel_c_alum_pl"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''  and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'ALUMBRADO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_cc"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_cc FROM "eiel_c_cc", eiel_configuracion_shp WHERE "eiel_c_cc"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'CASA_CONSISTORIAL_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_ce"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_ce FROM "eiel_c_ce", eiel_configuracion_shp WHERE "eiel_c_ce"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'CEMENTERIO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_cu"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_cu FROM "eiel_c_cu", eiel_configuracion_shp WHERE "eiel_c_cu"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'CENT_CULTURAL.SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_as"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_as FROM "eiel_c_as", eiel_configuracion_shp WHERE "eiel_c_as"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'CENTRO_ASISTENCIAL_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_en"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_en FROM "eiel_c_en", eiel_configuracion_shp WHERE "eiel_c_en"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'CENTRO_ENSENANZA_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_sa"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_sa FROM "eiel_c_sa", eiel_configuracion_shp WHERE "eiel_c_sa"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'CENTRO_SANITARIO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_su"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_su FROM "eiel_c_su", eiel_configuracion_shp WHERE "eiel_c_su"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'EDIF_PUB_SIN_USO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_redviaria_tu"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento, tipo, estado FROM "eiel_c_redviaria_tu", eiel_configuracion_shp WHERE "eiel_c_redviaria_tu"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'INFRAESTR_VIARIA_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_id"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_id FROM "eiel_c_id", eiel_configuracion_shp WHERE "eiel_c_id"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'INSTAL_DEPORTIVA_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_lm"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_lm FROM "eiel_c_lm", eiel_configuracion_shp WHERE "eiel_c_lm"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'LONJA_MERC_FERIA_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_mt"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_mt FROM "eiel_c_mt", eiel_configuracion_shp WHERE "eiel_c_mt"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'MATADERO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_nucleo_poblacion"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, nombre_oficial FROM "eiel_c_nucleo_poblacion", eiel_configuracion_shp WHERE "eiel_c_nucleo_poblacion"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'NUCLEO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_ali"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento FROM "eiel_c_saneam_ali", eiel_configuracion_shp WHERE "eiel_c_saneam_ali"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'ALIVIADERO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_pj"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_pj.id, eiel_c_pj.id_municipio, fase, eiel_c_pj.clave, eiel_c_pj.codprov, eiel_c_pj.codmunic, eiel_c_pj.codentidad, eiel_c_pj.codpoblamiento, eiel_c_pj.orden_pj FROM eiel_configuracion_shp, "eiel_c_pj", eiel_t_pj  where (tipo = ''JA'' or tipo = ''PI'' or tipo = ''PN'' or tipo = ''PU'') and eiel_t_pj.clave = eiel_c_pj.clave AND eiel_t_pj.codprov = eiel_c_pj.codprov AND eiel_t_pj.codmunic = eiel_c_pj.codmunic AND eiel_t_pj.codentidad = eiel_c_pj.codentidad AND eiel_t_pj.codpoblamiento = eiel_c_pj.codpoblamiento AND eiel_t_pj.orden_pj = eiel_c_pj.orden_pj and eiel_t_pj.revision_expirada = 9999999999 and eiel_c_pj.revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and "eiel_c_pj"."id_municipio" in (?M) and (eiel_t_nucl_encuest_1.codprov = eiel_c_pj.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_c_pj.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_c_pj.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_c_pj.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'PARQUE_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_pj"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_pj.id, eiel_c_pj.id_municipio, fase, eiel_c_pj.clave, eiel_c_pj.codprov, eiel_c_pj.codmunic, eiel_c_pj.codentidad, eiel_c_pj.codpoblamiento, eiel_c_pj.orden_pj FROM eiel_configuracion_shp, "eiel_c_pj", eiel_t_pj where (tipo = ''AN'' or tipo = ''ZR'') and eiel_t_pj.clave = eiel_c_pj.clave AND eiel_t_pj.codprov = eiel_c_pj.codprov AND eiel_t_pj.codmunic = eiel_c_pj.codmunic AND eiel_t_pj.codentidad = eiel_c_pj.codentidad AND eiel_t_pj.codpoblamiento = eiel_c_pj.codpoblamiento AND eiel_t_pj.orden_pj = eiel_c_pj.orden_pj and eiel_t_pj.revision_expirada = 9999999999 and eiel_c_pj.revision_expirada = 9999999999  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and "eiel_c_pj"."id_municipio" in (?M) and (eiel_t_nucl_encuest_1.codprov = eiel_c_pj.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_c_pj.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_c_pj.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_c_pj.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'ZONAS_NATURALES_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_ip"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_ip FROM "eiel_c_ip", eiel_configuracion_shp WHERE "eiel_c_ip"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'PROTECCION_CIVIL_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_rs"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento, tramo_rs, sist_impulsion, estado, tipo_red_interior, titular, gestor FROM "eiel_c_saneam_rs", eiel_configuracion_shp WHERE "eiel_c_saneam_rs"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'RAMAL_SANEAMIENTO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_rd"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, codmunic, codentidad, codpoblamiento, tramo_rd, sist_trans, estado, titular, gestor FROM "eiel_c_abast_rd", eiel_configuracion_shp WHERE "eiel_c_abast_rd"."id_municipio" in (?M) and revision_expirada = 9999999999  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'RED_DISTRIBUCION_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_ta"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, codentidad, codpoblamiento, orden_ta FROM "eiel_c_ta", eiel_configuracion_shp WHERE "eiel_c_ta"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and (eiel_t_nucl_encuest_1.codprov = codprov and eiel_t_nucl_encuest_1.codmunic = codmunic and eiel_t_nucl_encuest_1.codentidad = codentidad and eiel_t_nucl_encuest_1.codpoblamiento = codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)' where id_layer = (SELECT id_layer from layers where name = 'TANATORIO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_ca"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_abast_ca.id, eiel_c_abast_ca.id_municipio, eiel_configuracion_shp.fase, eiel_c_abast_ca.clave, eiel_c_abast_ca.codprov, eiel_c_abast_ca.codmunic, eiel_c_abast_ca.orden_ca FROM "eiel_c_abast_ca" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_ca.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_ca.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_c_abast_ca.codmunic::text || eiel_c_abast_ca.orden_ca::text IN ( SELECT DISTINCT v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text FROM v_cap_agua_nucleo ORDER BY v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text))  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' AND eiel_c_abast_ca.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)' where id_layer = (SELECT id_layer from layers where name = 'CAPTACION_ENC_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_ca"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_abast_ca.id, eiel_c_abast_ca.id_municipio, eiel_configuracion_shp.fase, eiel_c_abast_ca.clave, eiel_c_abast_ca.codprov, eiel_c_abast_ca.codmunic, eiel_c_abast_ca.orden_ca FROM "eiel_c_abast_ca" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_ca.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_ca.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_c_abast_ca.codmunic::text || eiel_c_abast_ca.orden_ca::text IN ( SELECT DISTINCT v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text FROM v_cap_agua_nucleo ORDER BY v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text))  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' AND eiel_c_abast_ca.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)' where id_layer = (SELECT id_layer from layers where name = 'CAPTACION_ENC_M50_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_de"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_abast_de.id, eiel_c_abast_de.id_municipio, eiel_configuracion_shp.fase, eiel_c_abast_de.clave, eiel_c_abast_de.codprov, eiel_c_abast_de.codmunic, eiel_c_abast_de.orden_de FROM "eiel_c_abast_de" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_de.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_de.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_c_abast_de.codmunic::text || eiel_c_abast_de.orden_de::text IN ( SELECT DISTINCT v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text FROM v_deposito_agua_nucleo ORDER BY v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_abast_de.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'DEPOSITO_ENC_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_de"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_abast_de.id, eiel_c_abast_de.id_municipio, eiel_configuracion_shp.fase, eiel_c_abast_de.clave, eiel_c_abast_de.codprov, eiel_c_abast_de.codmunic, eiel_c_abast_de.orden_de FROM "eiel_c_abast_de" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_de.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_de.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_c_abast_de.codmunic::text || eiel_c_abast_de.orden_de::text IN ( SELECT DISTINCT v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text FROM v_deposito_agua_nucleo ORDER BY v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_abast_de.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'DEPOSITO_ENC_M50_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_ed"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_saneam_ed.id, eiel_c_saneam_ed.id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_ed.clave, eiel_c_saneam_ed.codprov, eiel_c_saneam_ed.codmunic, eiel_c_saneam_ed.orden_ed FROM "eiel_c_saneam_ed" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_ed.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_c_saneam_ed.codmunic::text || eiel_c_saneam_ed.orden_ed::text IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text FROM v_dep_agua_nucleo ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_c_saneam_ed.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'DEPURADORA_ENC_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_ed"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_saneam_ed.id, eiel_c_saneam_ed.id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_ed.clave, eiel_c_saneam_ed.codprov, eiel_c_saneam_ed.codmunic, eiel_c_saneam_ed.orden_ed FROM "eiel_c_saneam_ed" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_ed.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_c_saneam_ed.codmunic::text || eiel_c_saneam_ed.orden_ed::text IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text FROM v_dep_agua_nucleo ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_c_saneam_ed.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'DEPURADORA_ENC_M50_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_tem"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_saneam_tem.id, eiel_c_saneam_tem.id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_tem.clave, eiel_c_saneam_tem.codprov, eiel_c_saneam_tem.codmunic, eiel_c_saneam_tem.tramo_em FROM "eiel_c_saneam_tem" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_tem.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_c_saneam_tem.codmunic::text || eiel_c_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text FROM v_emisario_nucleo ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_tem.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'EMISARIO_ENC_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_tem"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_saneam_tem.id, eiel_c_saneam_tem.id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_tem.clave, eiel_c_saneam_tem.codprov, eiel_c_saneam_tem.codmunic, eiel_c_saneam_tem.tramo_em FROM "eiel_c_saneam_tem" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_tem.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_c_saneam_tem.codmunic::text || eiel_c_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text FROM v_emisario_nucleo ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_tem.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'EMISARIO_ENC_M50_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_tp"."GEOMETRY" , ?T) as "GEOMETRY", "eiel_c_abast_tp".id, "eiel_c_abast_tp".id_municipio, eiel_configuracion_shp.fase, "eiel_c_abast_tp".clave, "eiel_c_abast_tp".codprov, "eiel_c_abast_tp".codmunic, "eiel_c_abast_tp".orden_tp FROM "eiel_c_abast_tp" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_tp.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_tp.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_c_abast_tp.codmunic::text || eiel_c_abast_tp.orden_tp::text IN ( SELECT DISTINCT v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text FROM v_trat_pota_nucleo ORDER BY v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text)) AND eiel_c_abast_tp.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'POTABILIZACION_ENC_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_tp"."GEOMETRY" , ?T) as "GEOMETRY", "eiel_c_abast_tp".id, "eiel_c_abast_tp".id_municipio, eiel_configuracion_shp.fase, "eiel_c_abast_tp".clave, "eiel_c_abast_tp".codprov, "eiel_c_abast_tp".codmunic, "eiel_c_abast_tp".orden_tp FROM "eiel_c_abast_tp" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_tp.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_tp.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_c_abast_tp.codmunic::text || eiel_c_abast_tp.orden_tp::text IN ( SELECT DISTINCT v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text FROM v_trat_pota_nucleo ORDER BY v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text)) AND eiel_c_abast_tp.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'POTABILIZACION_ENC_M50_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_tcl"."GEOMETRY" , ?T) as "GEOMETRY", "eiel_c_saneam_tcl".id, "eiel_c_saneam_tcl".id_municipio, eiel_configuracion_shp.fase, "eiel_c_saneam_tcl".clave, "eiel_c_saneam_tcl".codprov, "eiel_c_saneam_tcl".codmunic, "eiel_c_saneam_tcl".tramo_cl, tipo_red_interior, sist_impulsion, estado, titular, gestor FROM "eiel_c_saneam_tcl", eiel_t_saneam_tcl LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tcl.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text FROM v_colector_nucleo  ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric and eiel_t_saneam_tcl.clave::text = eiel_c_saneam_tcl.clave::text AND eiel_t_saneam_tcl.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_saneam_tcl.codmunic::text = eiel_c_saneam_tcl.codmunic::text AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_c_saneam_tcl.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'TRAMO_COLECTOR_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_tcl"."GEOMETRY" , ?T) as "GEOMETRY", "eiel_c_saneam_tcl".id, "eiel_c_saneam_tcl".id_municipio, eiel_configuracion_shp.fase, "eiel_c_saneam_tcl".clave, "eiel_c_saneam_tcl".codprov, "eiel_c_saneam_tcl".codmunic, "eiel_c_saneam_tcl".tramo_cl, tipo_red_interior, sist_impulsion, estado, titular, gestor FROM "eiel_c_saneam_tcl", eiel_t_saneam_tcl LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tcl.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text FROM v_colector_nucleo  ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric and eiel_t_saneam_tcl.clave::text = eiel_c_saneam_tcl.clave::text AND eiel_t_saneam_tcl.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_saneam_tcl.codmunic::text = eiel_c_saneam_tcl.codmunic::text AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_c_saneam_tcl.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'TRAMO_COLECTOR_M50_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_tcn"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, eiel_configuracion_shp.fase, eiel_c_abast_tcn.clave, eiel_c_abast_tcn.codprov, eiel_c_abast_tcn.codmunic, eiel_c_abast_tcn.tramo_cn, eiel_t_abast_tcn.material, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor FROM "eiel_c_abast_tcn", eiel_t_abast_tcn LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tcn.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text  FROM v_cond_agua_nucleo  ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric and eiel_c_abast_tcn.revision_expirada = 9999999999 and eiel_c_abast_tcn.clave::text = eiel_t_abast_tcn.clave::text AND eiel_c_abast_tcn.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_c_abast_tcn.codmunic::text = eiel_t_abast_tcn.codmunic::text AND eiel_c_abast_tcn.tramo_cn::text = eiel_t_abast_tcn.tramo_cn::text   AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'TRAMO_CONDUCCION_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_abast_tcn"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, eiel_configuracion_shp.fase, eiel_c_abast_tcn.clave, eiel_c_abast_tcn.codprov, eiel_c_abast_tcn.codmunic, eiel_c_abast_tcn.tramo_cn, eiel_t_abast_tcn.material, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor FROM "eiel_c_abast_tcn", eiel_t_abast_tcn LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tcn.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text  FROM v_cond_agua_nucleo  ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric and eiel_c_abast_tcn.revision_expirada = 9999999999 and eiel_c_abast_tcn.clave::text = eiel_t_abast_tcn.clave::text AND eiel_c_abast_tcn.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_c_abast_tcn.codmunic::text = eiel_t_abast_tcn.codmunic::text AND eiel_c_abast_tcn.tramo_cn::text = eiel_t_abast_tcn.tramo_cn::text   AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'TRAMO_CONDUCCION_M50_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_tem"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_tem.clave, eiel_c_saneam_tem.codprov, eiel_c_saneam_tem.codmunic, eiel_c_saneam_tem.tramo_em, material, estado FROM "eiel_c_saneam_tem", eiel_t_saneam_tem LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text FROM v_emisario_nucleo ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tem.tramo_em::text = eiel_c_saneam_tem.tramo_em::text AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric and eiel_t_saneam_tem.clave::text = eiel_c_saneam_tem.clave::text AND eiel_t_saneam_tem.codprov::text = eiel_c_saneam_tem.codprov::text AND eiel_t_saneam_tem.codmunic::text = eiel_c_saneam_tem.codmunic::text AND eiel_c_saneam_tem.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'TRAMO_EMISARIO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_tem"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_tem.clave, eiel_c_saneam_tem.codprov, eiel_c_saneam_tem.codmunic, eiel_c_saneam_tem.tramo_em, material, estado FROM "eiel_c_saneam_tem", eiel_t_saneam_tem LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text FROM v_emisario_nucleo ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tem.tramo_em::text = eiel_c_saneam_tem.tramo_em::text AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric and eiel_t_saneam_tem.clave::text = eiel_c_saneam_tem.clave::text AND eiel_t_saneam_tem.codprov::text = eiel_c_saneam_tem.codprov::text AND eiel_t_saneam_tem.codmunic::text = eiel_c_saneam_tem.codmunic::text AND eiel_c_saneam_tem.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'TRAMO_EMISARIO_M50_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_vt"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_vt FROM "eiel_c_vt", eiel_configuracion_shp WHERE "eiel_c_vt"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 < 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and id_municipio IN (?M)' where id_layer = (SELECT id_layer from layers where name = 'VERT_ENCUESTADO_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_vt"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, clave, codprov, codmunic, orden_vt FROM "eiel_c_vt", eiel_configuracion_shp WHERE "eiel_c_vt"."id_municipio" in (SELECT distinct eiel_t_padron_ttmm.codprov||eiel_t_padron_ttmm.codmunic FROM public.eiel_t_padron_ttmm WHERE TOTAL_POBLACION_A1 >= 50000) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'' and id_municipio IN (?M)' where id_layer = (SELECT id_layer from layers where name = 'VERT_ENCUESTADO_M50_SHP');
--UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_tramos_carreteras"."GEOMETRY", ?T) as "GEOMETRY", id, id_municipio, fase, eiel_c_tramos_carreteras.codprov, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.codmunic, pki FROM eiel_configuracion_shp, "eiel_c_tramos_carreteras" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_tramos_carreteras.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_tramos_carreteras.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_tramos_carreteras.revision_expirada = 9999999999::bigint::numeric and "eiel_c_tramos_carreteras"."id_municipio" in (?M) and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'TRAMO_CARRETERA_SHP');
UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_tramos_carreteras"."GEOMETRY" , ?T) as "GEOMETRY", id, id_municipio, fase, codprov, cod_carrt, codmunic, pki FROM "eiel_c_tramos_carreteras", eiel_configuracion_shp WHERE "eiel_c_tramos_carreteras"."id_municipio" in (?M) and revision_expirada = 9999999999 and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer = (SELECT id_layer from layers where name = 'TRAMO_CARRETERA_SHP');



CREATE OR REPLACE VIEW v_alumbrado AS 
 SELECT eiel_c_alum_pl.codprov AS provincia, eiel_c_alum_pl.codmunic AS municipio, eiel_c_alum_pl.codentidad AS entidad, eiel_c_alum_pl.codpoblamiento AS nucleo, eiel_c_alum_pl.ah_ener_rfl AS ah_ener_rl, eiel_c_alum_pl.ah_ener_rfi AS ah_ener_ri, eiel_c_alum_pl.estado AS calidad, eiel_c_alum_pl.potencia*count(*) AS pot_instal, count(*) AS n_puntos
   FROM eiel_c_alum_pl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_alum_pl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_alum_pl.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_alum_pl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_c_alum_pl.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_c_alum_pl.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_c_alum_pl.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_c_alum_pl.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  GROUP BY eiel_c_alum_pl.codmunic, eiel_c_alum_pl.codentidad, eiel_c_alum_pl.codpoblamiento, eiel_c_alum_pl.codprov, eiel_c_alum_pl.ah_ener_rfl, eiel_c_alum_pl.ah_ener_rfi, eiel_c_alum_pl.estado, eiel_c_alum_pl.potencia
  ORDER BY eiel_c_alum_pl.codmunic, eiel_c_alum_pl.codentidad, eiel_c_alum_pl.codpoblamiento;

ALTER TABLE v_alumbrado
  OWNER TO geopista;
GRANT ALL ON TABLE v_alumbrado TO geopista;
GRANT SELECT ON TABLE v_alumbrado TO consultas;

CREATE OR REPLACE VIEW v_cap_agua_nucleo AS 
 SELECT eiel_tr_abast_ca_pobl.codprov_pobl AS provincia, eiel_tr_abast_ca_pobl.codmunic_pobl AS municipio, eiel_tr_abast_ca_pobl.codentidad_pobl AS entidad, eiel_tr_abast_ca_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_ca_pobl.clave_ca AS clave, eiel_tr_abast_ca_pobl.codprov_ca AS c_provinc, eiel_tr_abast_ca_pobl.codmunic_ca AS c_municip, eiel_tr_abast_ca_pobl.orden_ca AS orden_capt
   FROM eiel_tr_abast_ca_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_ca_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_ca_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp 
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_ca_pobl.revision_expirada = 9999999999::bigint::numeric
    and (eiel_t_nucl_encuest_1.codprov = eiel_tr_abast_ca_pobl.codprov_pobl and eiel_t_nucl_encuest_1.codmunic = eiel_tr_abast_ca_pobl.codmunic_pobl and eiel_t_nucl_encuest_1.codentidad = eiel_tr_abast_ca_pobl.codentidad_pobl and eiel_t_nucl_encuest_1.codpoblamiento = eiel_tr_abast_ca_pobl.codpoblamiento_pobl and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);
ALTER TABLE v_cap_agua_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_cap_agua_nucleo TO geopista;
GRANT SELECT ON TABLE v_cap_agua_nucleo TO consultas;


CREATE OR REPLACE VIEW v_casa_con_uso AS 
 SELECT DISTINCT ON (eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov, eiel_t_cc_usos.codmunic, eiel_t_cc_usos.codentidad, eiel_t_cc_usos.codpoblamiento, eiel_t_cc_usos.orden_cc, eiel_t_cc_usos.uso) eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov AS provincia, eiel_t_cc_usos.codmunic AS municipio, eiel_t_cc_usos.codentidad AS entidad, eiel_t_cc_usos.codpoblamiento AS poblamient, eiel_t_cc_usos.orden_cc AS orden_casa, eiel_t_cc_usos.uso, eiel_t_cc_usos.s_cubierta AS s_cubi
   FROM eiel_t_cc_usos
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cc_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cc_usos.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cc_usos.revision_expirada = 9999999999::bigint::numeric
      and (eiel_t_nucl_encuest_1.codprov = eiel_t_cc_usos.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_cc_usos.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_cc_usos.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_cc_usos.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)
  ORDER BY eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov, eiel_t_cc_usos.codmunic, eiel_t_cc_usos.codentidad, eiel_t_cc_usos.codpoblamiento, eiel_t_cc_usos.orden_cc, eiel_t_cc_usos.uso;

ALTER TABLE v_casa_con_uso
  OWNER TO geopista;
GRANT ALL ON TABLE v_casa_con_uso TO geopista;
GRANT SELECT ON TABLE v_casa_con_uso TO consultas;


CREATE OR REPLACE VIEW v_casa_consistorial AS 
 SELECT eiel_t_cc.clave, eiel_t_cc.codprov AS provincia, eiel_t_cc.codmunic AS municipio, eiel_t_cc.codentidad AS entidad, eiel_t_cc.codpoblamiento AS poblamient, eiel_t_cc.orden_cc AS orden_casa, eiel_t_cc.nombre, eiel_t_cc.tipo, eiel_t_cc.titular, eiel_t_cc.tenencia, eiel_t_cc.s_cubierta AS s_cubi, eiel_t_cc.s_aire, eiel_t_cc.s_solar AS s_sola, eiel_t_cc.acceso_s_ruedas, eiel_t_cc.estado
   FROM eiel_t_cc
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cc.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cc.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cc.revision_expirada = 9999999999::bigint::numeric
      and (eiel_t_nucl_encuest_1.codprov = eiel_t_cc.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_cc.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_cc.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_cc.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_casa_consistorial
  OWNER TO geopista;
GRANT ALL ON TABLE v_casa_consistorial TO geopista;
GRANT SELECT ON TABLE v_casa_consistorial TO consultas;



CREATE OR REPLACE VIEW v_cementerio AS 
 SELECT eiel_t_ce.clave, eiel_t_ce.codprov AS provincia, eiel_t_ce.codmunic AS municipio, eiel_t_ce.codentidad AS entidad, eiel_t_ce.codpoblamiento AS poblamient, eiel_t_ce.orden_ce AS orden_ceme, eiel_t_ce.nombre, eiel_t_ce.titular, eiel_t_ce.distancia, eiel_t_ce.acceso, eiel_t_ce.capilla, eiel_t_ce.deposito, eiel_t_ce.ampliacion, eiel_t_ce.saturacion, eiel_t_ce.superficie, eiel_t_ce.acceso_s_ruedas, eiel_t_ce.crematorio
   FROM eiel_t_ce
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ce.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ce.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ce.revision_expirada = 9999999999::bigint::numeric
      and (eiel_t_nucl_encuest_1.codprov = eiel_t_ce.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_ce.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_ce.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_ce.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_cementerio
  OWNER TO geopista;
GRANT ALL ON TABLE v_cementerio TO geopista;
GRANT SELECT ON TABLE v_cementerio TO consultas;


CREATE OR REPLACE VIEW v_cent_cultural AS 
 SELECT eiel_t_cu.clave, eiel_t_cu.codprov AS provincia, eiel_t_cu.codmunic AS municipio, eiel_t_cu.codentidad AS entidad, eiel_t_cu.codpoblamiento AS poblamient, eiel_t_cu.orden_cu AS orden_cent, eiel_t_cu.nombre, eiel_t_cu.tipo AS tipo_cent, eiel_t_cu.titular, eiel_t_cu.gestor AS gestion, eiel_t_cu.s_cubierta AS s_cubi, eiel_t_cu.s_aire, eiel_t_cu.s_solar AS s_sola, eiel_t_cu.acceso_s_ruedas, eiel_t_cu.estado
   FROM eiel_t_cu
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cu.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cu.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cu.revision_expirada = 9999999999::bigint::numeric
      and (eiel_t_nucl_encuest_1.codprov = eiel_t_cu.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_cu.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_cu.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_cu.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_cent_cultural
  OWNER TO geopista;
GRANT ALL ON TABLE v_cent_cultural TO geopista;
GRANT SELECT ON TABLE v_cent_cultural TO consultas;


CREATE OR REPLACE VIEW v_cent_cultural_usos AS 
 SELECT DISTINCT ON (eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov, eiel_t_cu_usos.codmunic, eiel_t_cu_usos.codentidad, eiel_t_cu_usos.codpoblamiento, eiel_t_cu_usos.orden_cu, eiel_t_cu_usos.uso) eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov AS provincia, eiel_t_cu_usos.codmunic AS municipio, eiel_t_cu_usos.codentidad AS entidad, eiel_t_cu_usos.codpoblamiento AS poblamient, eiel_t_cu_usos.orden_cu AS orden_cent, eiel_t_cu_usos.uso, eiel_t_cu_usos.s_cubierta AS s_cubi
   FROM eiel_t_cu_usos
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cu_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cu_usos.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cu_usos.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_t_cu_usos.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_cu_usos.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_cu_usos.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_cu_usos.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)
  ORDER BY eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov, eiel_t_cu_usos.codmunic, eiel_t_cu_usos.codentidad, eiel_t_cu_usos.codpoblamiento, eiel_t_cu_usos.orden_cu, eiel_t_cu_usos.uso;

ALTER TABLE v_cent_cultural_usos
  OWNER TO geopista;
GRANT ALL ON TABLE v_cent_cultural_usos TO geopista;
GRANT SELECT ON TABLE v_cent_cultural_usos TO consultas;


CREATE OR REPLACE VIEW v_centro_asistencial AS 
 SELECT eiel_t_as.clave, eiel_t_as.codprov AS provincia, eiel_t_as.codmunic AS municipio, eiel_t_as.codentidad AS entidad, eiel_t_as.codpoblamiento AS poblamient, eiel_t_as.orden_as AS orden_casi, eiel_t_as.nombre, eiel_t_as.tipo AS tipo_casis, eiel_t_as.titular, eiel_t_as.gestor AS gestion, eiel_t_as.plazas, eiel_t_as.s_cubierta AS s_cubi, eiel_t_as.s_aire, eiel_t_as.s_solar AS s_sola, eiel_t_as.acceso_s_ruedas, eiel_t_as.estado
   FROM eiel_t_as
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_as.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_as.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_as.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_t_as.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_as.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_as.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_as.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_centro_asistencial
  OWNER TO geopista;
GRANT ALL ON TABLE v_centro_asistencial TO geopista;
GRANT SELECT ON TABLE v_centro_asistencial TO consultas;


CREATE OR REPLACE VIEW v_centro_ensenanza AS 
 SELECT eiel_t_en.clave, eiel_t_en.codprov AS provincia, eiel_t_en.codmunic AS municipio, eiel_t_en.codentidad AS entidad, eiel_t_en.codpoblamiento AS poblamient, eiel_t_en.orden_en AS orden_cent, eiel_t_en.nombre, eiel_t_en.ambito, eiel_t_en.titular, eiel_t_en.s_cubierta AS s_cubi, eiel_t_en.s_aire, eiel_t_en.s_solar AS s_sola, eiel_t_en.acceso_s_ruedas, eiel_t_en.estado
   FROM eiel_t_en
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_en.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_en.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_en.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_t_en.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_en.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_en.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_en.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_centro_ensenanza
  OWNER TO geopista;
GRANT ALL ON TABLE v_centro_ensenanza TO geopista;
GRANT SELECT ON TABLE v_centro_ensenanza TO consultas;


CREATE OR REPLACE VIEW v_centro_sanitario AS 
 SELECT eiel_t_sa.clave, eiel_t_sa.codprov AS provincia, eiel_t_sa.codmunic AS municipio, eiel_t_sa.codentidad AS entidad, eiel_t_sa.codpoblamiento AS poblamient, eiel_t_sa.orden_sa AS orden_csan, eiel_t_sa.nombre, eiel_t_sa.tipo AS tipo_csan, eiel_t_sa.titular, eiel_t_sa.gestor AS gestion, eiel_t_sa.s_cubierta AS s_cubi, eiel_t_sa.s_aire, eiel_t_sa.s_solar AS s_sola, eiel_t_sa.uci, eiel_t_sa.n_camas AS camas, eiel_t_sa.acceso_s_ruedas, eiel_t_sa.estado
   FROM eiel_t_sa
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_sa.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_sa.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_sa.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_t_sa.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_sa.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_sa.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_sa.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_centro_sanitario
  OWNER TO geopista;
GRANT ALL ON TABLE v_centro_sanitario TO geopista;
GRANT SELECT ON TABLE v_centro_sanitario TO consultas;


CREATE OR REPLACE VIEW v_colector_nucleo AS 
 SELECT eiel_tr_saneam_tcl_pobl.codprov_pobl AS provincia, eiel_tr_saneam_tcl_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_tcl_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_tcl_pobl.clave_tcl AS clave, eiel_tr_saneam_tcl_pobl.codprov_tcl AS c_provinc, eiel_tr_saneam_tcl_pobl.codmunic_tcl AS c_municip, eiel_tr_saneam_tcl_pobl.tramo_cl AS orden_cole
   FROM eiel_tr_saneam_tcl_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_tcl_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_tcl_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_tcl_pobl.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_tr_saneam_tcl_pobl.codprov_pobl and eiel_t_nucl_encuest_1.codmunic = eiel_tr_saneam_tcl_pobl.codmunic_pobl and eiel_t_nucl_encuest_1.codentidad = eiel_tr_saneam_tcl_pobl.codentidad_pobl and eiel_t_nucl_encuest_1.codpoblamiento = eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_colector_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_colector_nucleo TO geopista;
GRANT SELECT ON TABLE v_colector_nucleo TO consultas;


CREATE OR REPLACE VIEW v_cond_agua_nucleo AS 
 SELECT eiel_tr_abast_tcn_pobl.codprov_pobl AS provincia, eiel_tr_abast_tcn_pobl.codmunic_pobl AS municipio, eiel_tr_abast_tcn_pobl.codentidad_pobl AS entidad, eiel_tr_abast_tcn_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_tcn_pobl.clave_tcn AS clave, eiel_tr_abast_tcn_pobl.codprov_tcn AS cond_provi, eiel_tr_abast_tcn_pobl.codmunic_tcn AS cond_munic, eiel_tr_abast_tcn_pobl.tramo_tcn AS orden_cond
   FROM eiel_tr_abast_tcn_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_tcn_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_tcn_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_tcn_pobl.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_tr_abast_tcn_pobl.codprov_pobl and eiel_t_nucl_encuest_1.codmunic = eiel_tr_abast_tcn_pobl.codmunic_pobl and eiel_t_nucl_encuest_1.codentidad = eiel_tr_abast_tcn_pobl.codentidad_pobl and eiel_t_nucl_encuest_1.codpoblamiento = eiel_tr_abast_tcn_pobl.codpoblamiento_pobl and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_cond_agua_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_cond_agua_nucleo TO geopista;
GRANT SELECT ON TABLE v_cond_agua_nucleo TO consultas;


CREATE OR REPLACE VIEW v_dep_agua_nucleo AS 
 SELECT eiel_tr_saneam_ed_pobl.codprov_pobl AS provincia, eiel_tr_saneam_ed_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_ed_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_ed_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_ed_pobl.clave_ed AS clave, eiel_tr_saneam_ed_pobl.codprov_ed AS de_provinc, eiel_tr_saneam_ed_pobl.codmunic_ed AS de_municip, eiel_tr_saneam_ed_pobl.orden_ed AS orden_depu
   FROM eiel_tr_saneam_ed_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_ed_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_ed_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_ed_pobl.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_tr_saneam_ed_pobl.codprov_pobl and eiel_t_nucl_encuest_1.codmunic = eiel_tr_saneam_ed_pobl.codmunic_pobl and eiel_t_nucl_encuest_1.codentidad = eiel_tr_saneam_ed_pobl.codentidad_pobl and eiel_t_nucl_encuest_1.codpoblamiento = eiel_tr_saneam_ed_pobl.codpoblamiento_pobl and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_dep_agua_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_dep_agua_nucleo TO geopista;
GRANT SELECT ON TABLE v_dep_agua_nucleo TO consultas;


CREATE OR REPLACE VIEW v_deposito_agua_nucleo AS 
 SELECT eiel_tr_abast_de_pobl.codprov_pobl AS provincia, eiel_tr_abast_de_pobl.codmunic_pobl AS municipio, eiel_tr_abast_de_pobl.codentidad_pobl AS entidad, eiel_tr_abast_de_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_de_pobl.clave_de AS clave, eiel_tr_abast_de_pobl.codprov_de AS de_provinc, eiel_tr_abast_de_pobl.codmunic_de AS de_municip, eiel_tr_abast_de_pobl.orden_de AS orden_depo
   FROM eiel_tr_abast_de_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_de_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_de_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_de_pobl.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_tr_abast_de_pobl.codprov_pobl and eiel_t_nucl_encuest_1.codmunic = eiel_tr_abast_de_pobl.codmunic_pobl and eiel_t_nucl_encuest_1.codentidad = eiel_tr_abast_de_pobl.codentidad_pobl and eiel_t_nucl_encuest_1.codpoblamiento = eiel_tr_abast_de_pobl.codpoblamiento_pobl and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_deposito_agua_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_deposito_agua_nucleo TO geopista;
GRANT SELECT ON TABLE v_deposito_agua_nucleo TO consultas;


CREATE OR REPLACE VIEW v_edific_pub_sin_uso AS 
 SELECT eiel_t_su.clave, eiel_t_su.codprov AS provincia, eiel_t_su.codmunic AS municipio, eiel_t_su.codentidad AS entidad, eiel_t_su.codpoblamiento AS poblamient, eiel_t_su.orden_su AS orden_edif, eiel_t_su.nombre, eiel_t_su.titular, eiel_t_su.s_cubierta AS s_cubi, eiel_t_su.s_aire, eiel_t_su.s_solar AS s_sola, eiel_t_su.estado, eiel_t_su.uso_anterior AS usoant
   FROM eiel_t_su
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_su.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_su.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_su.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_t_su.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_su.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_su.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_su.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_edific_pub_sin_uso
  OWNER TO geopista;
GRANT ALL ON TABLE v_edific_pub_sin_uso TO geopista;
GRANT SELECT ON TABLE v_edific_pub_sin_uso TO consultas;


CREATE OR REPLACE VIEW v_emisario_nucleo AS 
 SELECT eiel_tr_saneam_tem_pobl.codprov_pobl AS provincia, eiel_tr_saneam_tem_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_tem_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_tem_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_tem_pobl.clave_tem AS clave, eiel_tr_saneam_tem_pobl.codprov_tem AS em_provinc, eiel_tr_saneam_tem_pobl.codmunic_tem AS em_municip, eiel_tr_saneam_tem_pobl.tramo_em AS orden_emis
   FROM eiel_tr_saneam_tem_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_tem_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_tem_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_tem_pobl.revision_expirada = 9999999999::bigint::numeric
        and (eiel_t_nucl_encuest_1.codprov = eiel_tr_saneam_tem_pobl.codprov_pobl and eiel_t_nucl_encuest_1.codmunic = eiel_tr_saneam_tem_pobl.codmunic_pobl and eiel_t_nucl_encuest_1.codentidad = eiel_tr_saneam_tem_pobl.codentidad_pobl and eiel_t_nucl_encuest_1.codpoblamiento = eiel_tr_saneam_tem_pobl.codpoblamiento_pobl and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_emisario_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_emisario_nucleo TO geopista;
GRANT SELECT ON TABLE v_emisario_nucleo TO consultas;


CREATE OR REPLACE VIEW v_infraestr_viaria AS 
 SELECT DISTINCT ON (eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado) eiel_c_redviaria_tu.codprov AS provincia, eiel_c_redviaria_tu.codmunic AS municipio, eiel_c_redviaria_tu.codentidad AS entidad, eiel_c_redviaria_tu.codpoblamiento AS poblamient, eiel_c_redviaria_tu.tipo AS tipo_infr, eiel_c_redviaria_tu.estado, sum(eiel_c_redviaria_tu.longitud) AS longitud, sum(eiel_c_redviaria_tu.superficie) AS superficie, eiel_c_redviaria_tu.viviendas_afec AS viv_afecta
   FROM eiel_c_redviaria_tu
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_redviaria_tu.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_redviaria_tu.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_redviaria_tu.revision_expirada = 9999999999::bigint::numeric
          and (eiel_t_nucl_encuest_1.codprov = eiel_c_redviaria_tu.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_c_redviaria_tu.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_c_redviaria_tu.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_c_redviaria_tu.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)
  GROUP BY eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado, eiel_c_redviaria_tu.viviendas_afec
  ORDER BY eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado;

ALTER TABLE v_infraestr_viaria
  OWNER TO geopista;
GRANT ALL ON TABLE v_infraestr_viaria TO geopista;
GRANT SELECT ON TABLE v_infraestr_viaria TO consultas;



CREATE OR REPLACE VIEW v_inst_depor_deporte AS 
 SELECT DISTINCT eiel_t_id_deportes.clave, eiel_t_id_deportes.codprov AS provincia, eiel_t_id_deportes.codmunic AS municipio, eiel_t_id_deportes.codentidad AS entidad, eiel_t_id_deportes.codpoblamiento AS poblamient, eiel_t_id_deportes.orden_id AS orden_inst, eiel_t_id_deportes.tipo_deporte AS tipo_depor
   FROM eiel_t_id_deportes
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_id_deportes.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_id_deportes.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_id_deportes.revision_expirada = 9999999999::bigint::numeric
            and (eiel_t_nucl_encuest_1.codprov = eiel_t_id_deportes.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_id_deportes.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_id_deportes.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_id_deportes.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)
  ORDER BY eiel_t_id_deportes.clave, eiel_t_id_deportes.codprov, eiel_t_id_deportes.codmunic, eiel_t_id_deportes.codentidad, eiel_t_id_deportes.codpoblamiento, eiel_t_id_deportes.orden_id, eiel_t_id_deportes.tipo_deporte;

ALTER TABLE v_inst_depor_deporte
  OWNER TO geopista;
GRANT ALL ON TABLE v_inst_depor_deporte TO geopista;
GRANT SELECT ON TABLE v_inst_depor_deporte TO consultas;



CREATE OR REPLACE VIEW v_instal_deportiva AS 
 SELECT eiel_t_id.clave, eiel_t_id.codprov AS provincia, eiel_t_id.codmunic AS municipio, eiel_t_id.codentidad AS entidad, eiel_t_id.codpoblamiento AS poblamient, eiel_t_id.orden_id AS orden_inst, eiel_t_id.nombre, eiel_t_id.tipo AS tipo_insde, eiel_t_id.titular, eiel_t_id.gestor AS gestion, eiel_t_id.s_cubierta AS s_cubi, eiel_t_id.s_aire, eiel_t_id.s_solar AS s_sola, eiel_t_id.acceso_s_ruedas, eiel_t_id.estado
   FROM eiel_t_id
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_id.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_id.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_id.revision_expirada = 9999999999::bigint::numeric
            and (eiel_t_nucl_encuest_1.codprov = eiel_t_id.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_id.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_id.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_id.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_instal_deportiva
  OWNER TO geopista;
GRANT ALL ON TABLE v_instal_deportiva TO geopista;
GRANT SELECT ON TABLE v_instal_deportiva TO consultas;


CREATE OR REPLACE VIEW v_lonja_merc_feria AS 
 SELECT eiel_t_lm.clave, eiel_t_lm.codprov AS provincia, eiel_t_lm.codmunic AS municipio, eiel_t_lm.codentidad AS entidad, eiel_t_lm.codpoblamiento AS poblamient, eiel_t_lm.orden_lm AS orden_lmf, eiel_t_lm.nombre, eiel_t_lm.tipo AS tipo_lonj, eiel_t_lm.titular, eiel_t_lm.gestor AS gestion, eiel_t_lm.s_cubierta AS s_cubi, eiel_t_lm.s_aire, eiel_t_lm.s_solar AS s_sola, eiel_t_lm.acceso_s_ruedas, eiel_t_lm.estado
   FROM eiel_t_lm
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_lm.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_lm.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_lm.revision_expirada = 9999999999::bigint::numeric
            and (eiel_t_nucl_encuest_1.codprov = eiel_t_lm.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_lm.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_lm.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_lm.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_lonja_merc_feria
  OWNER TO geopista;
GRANT ALL ON TABLE v_lonja_merc_feria TO geopista;
GRANT SELECT ON TABLE v_lonja_merc_feria TO consultas;



CREATE OR REPLACE VIEW v_matadero AS 
 SELECT eiel_t_mt.clave, eiel_t_mt.codprov AS provincia, eiel_t_mt.codmunic AS municipio, eiel_t_mt.codentidad AS entidad, eiel_t_mt.codpoblamiento AS poblamient, eiel_t_mt.orden_mt AS orden_mata, eiel_t_mt.nombre, eiel_t_mt.clase AS clase_mat, eiel_t_mt.titular, eiel_t_mt.gestor AS gestion, eiel_t_mt.s_cubierta AS s_cubi, eiel_t_mt.s_aire, eiel_t_mt.s_solar AS s_sola, eiel_t_mt.acceso_s_ruedas, eiel_t_mt.estado, eiel_t_mt.capacidad, eiel_t_mt.utilizacion AS utilizacio, eiel_t_mt.tunel, eiel_t_mt.bovino, eiel_t_mt.ovino, eiel_t_mt.porcino, eiel_t_mt.otros
   FROM eiel_t_mt
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_mt.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_mt.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_mt.revision_expirada = 9999999999::bigint::numeric
            and (eiel_t_nucl_encuest_1.codprov = eiel_t_mt.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_mt.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_mt.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_mt.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_matadero
  OWNER TO geopista;
GRANT ALL ON TABLE v_matadero TO geopista;
GRANT SELECT ON TABLE v_matadero TO consultas;


CREATE OR REPLACE VIEW v_nivel_ensenanza AS 
 SELECT DISTINCT ON (eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov, eiel_t_en_nivel.codmunic, eiel_t_en_nivel.codentidad, eiel_t_en_nivel.codpoblamiento, eiel_t_en_nivel.orden_en, eiel_t_en_nivel.nivel) eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov AS provincia, eiel_t_en_nivel.codmunic AS municipio, eiel_t_en_nivel.codentidad AS entidad, eiel_t_en_nivel.codpoblamiento AS poblamient, eiel_t_en_nivel.orden_en AS orden_cent, eiel_t_en_nivel.nivel, eiel_t_en_nivel.unidades, eiel_t_en_nivel.plazas, eiel_t_en_nivel.alumnos
   FROM eiel_t_en_nivel
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_en_nivel.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_en_nivel.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_en_nivel.revision_expirada = 9999999999::bigint::numeric
              and (eiel_t_nucl_encuest_1.codprov = eiel_t_en_nivel.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_en_nivel.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_en_nivel.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_en_nivel.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)
  ORDER BY eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov, eiel_t_en_nivel.codmunic, eiel_t_en_nivel.codentidad, eiel_t_en_nivel.codpoblamiento, eiel_t_en_nivel.orden_en, eiel_t_en_nivel.nivel;

ALTER TABLE v_nivel_ensenanza
  OWNER TO geopista;
GRANT ALL ON TABLE v_nivel_ensenanza TO geopista;
GRANT SELECT ON TABLE v_nivel_ensenanza TO consultas;


CREATE OR REPLACE VIEW v_nucleo_poblacion AS 
 SELECT eiel_c_nucleo_poblacion.codprov AS provincia, eiel_c_nucleo_poblacion.codmunic AS municipio, eiel_c_nucleo_poblacion.codentidad AS entidad, eiel_c_nucleo_poblacion.codpoblamiento AS poblamient, eiel_c_nucleo_poblacion.nombre_oficial AS denominaci
   FROM eiel_c_nucleo_poblacion --, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_c_nucleo_poblacion.revision_expirada = 9999999999::bigint::numeric; 
  -- AND eiel_t_nucl_encuest_1.codprov::text = eiel_c_nucleo_poblacion.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_c_nucleo_poblacion.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_c_nucleo_poblacion.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_c_nucleo_poblacion.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_nucleo_poblacion
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucleo_poblacion TO geopista;
GRANT SELECT ON TABLE v_nucleo_poblacion TO consultas;


CREATE OR REPLACE VIEW v_parque AS 
 SELECT eiel_t_pj.clave, eiel_t_pj.codprov AS provincia, eiel_t_pj.codmunic AS municipio, eiel_t_pj.codentidad AS entidad, eiel_t_pj.codpoblamiento AS poblamient, eiel_t_pj.orden_pj AS orden_parq, eiel_t_pj.nombre, eiel_t_pj.tipo AS tipo_parq, eiel_t_pj.titular, eiel_t_pj.gestor AS gestion, eiel_t_pj.s_cubierta AS s_cubi, eiel_t_pj.s_aire, eiel_t_pj.s_solar AS s_sola, eiel_t_pj.agua, eiel_t_pj.saneamiento AS saneamient, eiel_t_pj.electricidad AS electricid, eiel_t_pj.comedor, eiel_t_pj.juegos_inf, eiel_t_pj.otras, eiel_t_pj.acceso_s_ruedas, eiel_t_pj.estado
   FROM eiel_t_pj
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_pj.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_pj.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_pj.revision_expirada = 9999999999::bigint::numeric
              and (eiel_t_nucl_encuest_1.codprov = eiel_t_pj.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_pj.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_pj.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_pj.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_parque
  OWNER TO geopista;
GRANT ALL ON TABLE v_parque TO geopista;
GRANT SELECT ON TABLE v_parque TO consultas;


CREATE OR REPLACE VIEW v_poblamiento AS 
 SELECT eiel_t_poblamiento.codprov AS provincia, eiel_t_poblamiento.codmunic AS municipio, eiel_t_poblamiento.codentidad AS entidad, eiel_t_poblamiento.codpoblamiento AS poblamiento
   FROM eiel_t_poblamiento -- , ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_poblamiento.revision_expirada = 9999999999::bigint::numeric -- AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_poblamiento.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_poblamiento.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_poblamiento.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_poblamiento.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  ORDER BY eiel_t_poblamiento.codprov, eiel_t_poblamiento.codmunic, eiel_t_poblamiento.codentidad, eiel_t_poblamiento.codpoblamiento;

ALTER TABLE v_poblamiento
  OWNER TO geopista;
GRANT ALL ON TABLE v_poblamiento TO geopista;
GRANT SELECT ON TABLE v_poblamiento TO consultas;



CREATE OR REPLACE VIEW v_proteccion_civil AS 
 SELECT eiel_t_ip.clave, eiel_t_ip.codprov AS provincia, eiel_t_ip.codmunic AS municipio, eiel_t_ip.codentidad AS entidad, eiel_t_ip.codpoblamiento AS poblamient, eiel_t_ip.orden_ip AS orden_prot, eiel_t_ip.nombre, eiel_t_ip.tipo AS tipo_pciv, eiel_t_ip.titular, eiel_t_ip.gestor AS gestion, eiel_t_ip.ambito, eiel_t_ip.plan_profe, eiel_t_ip.plan_volun, eiel_t_ip.s_cubierta AS s_cubi, eiel_t_ip.s_aire, eiel_t_ip.s_solar AS s_sola, eiel_t_ip.acceso_s_ruedas, eiel_t_ip.estado, eiel_t_ip.vehic_incendio AS vehic_ince, eiel_t_ip.vehic_rescate AS vehic_resc, eiel_t_ip.ambulancia, eiel_t_ip.medios_aereos AS medios_aer, eiel_t_ip.otros_vehc AS otros_vehi, eiel_t_ip.quitanieves AS quitanieve, eiel_t_ip.detec_ince, eiel_t_ip.otros
   FROM eiel_t_ip
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ip.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ip.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ip.revision_expirada = 9999999999::bigint::numeric
                and (eiel_t_nucl_encuest_1.codprov = eiel_t_ip.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_ip.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_ip.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_ip.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_proteccion_civil
  OWNER TO geopista;
GRANT ALL ON TABLE v_proteccion_civil TO geopista;
GRANT SELECT ON TABLE v_proteccion_civil TO consultas;



CREATE OR REPLACE VIEW v_ramal_saneamiento AS 
 SELECT eiel_c_saneam_rs.codprov AS provincia, eiel_c_saneam_rs.codmunic AS municipio, eiel_c_saneam_rs.codentidad AS entidad, eiel_c_saneam_rs.codpoblamiento AS nucleo, eiel_c_saneam_rs.material AS tipo_rama, eiel_c_saneam_rs.sist_impulsion AS sist_trans, eiel_c_saneam_rs.estado, eiel_c_saneam_rs.tipo_red_interior AS tipo_red, eiel_c_saneam_rs.titular, eiel_c_saneam_rs.gestor AS gestion, sum(eiel_c_saneam_rs.longitud) AS longit_ram
   FROM eiel_c_saneam_rs
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_rs.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_rs.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_rs.revision_expirada = 9999999999::bigint::numeric
                  and (eiel_t_nucl_encuest_1.codprov = eiel_c_saneam_rs.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_c_saneam_rs.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_c_saneam_rs.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_c_saneam_rs.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)

  GROUP BY eiel_c_saneam_rs.codmunic, eiel_c_saneam_rs.codentidad, eiel_c_saneam_rs.codpoblamiento, eiel_c_saneam_rs.codprov, eiel_c_saneam_rs.material, eiel_c_saneam_rs.sist_impulsion, eiel_c_saneam_rs.estado, eiel_c_saneam_rs.tipo_red_interior, eiel_c_saneam_rs.titular, eiel_c_saneam_rs.gestor
  ORDER BY eiel_c_saneam_rs.codmunic, eiel_c_saneam_rs.codentidad, eiel_c_saneam_rs.codpoblamiento;

ALTER TABLE v_ramal_saneamiento
  OWNER TO geopista;
GRANT ALL ON TABLE v_ramal_saneamiento TO geopista;
GRANT SELECT ON TABLE v_ramal_saneamiento TO consultas;


CREATE OR REPLACE VIEW v_recogida_basura AS 
 SELECT eiel_t_rb.codprov AS provincia, eiel_t_rb.codmunic AS municipio, eiel_t_rb.codentidad AS entidad, eiel_t_rb.codpoblamiento AS nucleo, eiel_t_rb.tipo AS tipo_rbas, eiel_t_rb.gestor AS gestion, eiel_t_rb.periodicidad AS periodicid, eiel_t_rb.calidad, eiel_t_rb.tm_res_urb AS produ_basu, eiel_t_rb.n_contenedores AS contenedor
   FROM eiel_t_rb
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_rb.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_rb.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_rb.revision_expirada = 9999999999::bigint::numeric
                  and (eiel_t_nucl_encuest_1.codprov = eiel_t_rb.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_rb.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_rb.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_rb.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_recogida_basura
  OWNER TO geopista;
GRANT ALL ON TABLE v_recogida_basura TO geopista;
GRANT SELECT ON TABLE v_recogida_basura TO consultas;


CREATE OR REPLACE VIEW v_red_distribucion AS 
 SELECT eiel_c_abast_rd.codprov AS provincia, eiel_c_abast_rd.codmunic AS municipio, eiel_c_abast_rd.codentidad AS entidad, eiel_c_abast_rd.codpoblamiento AS nucleo, eiel_c_abast_rd.material AS tipo_rdis, eiel_c_abast_rd.sist_trans, eiel_c_abast_rd.estado, eiel_c_abast_rd.titular, eiel_c_abast_rd.gestor AS gestion, sum(eiel_c_abast_rd.longitud) AS longitud
   FROM eiel_c_abast_rd
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_rd.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_rd.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_abast_rd.revision_expirada = 9999999999::bigint::numeric
                    and (eiel_t_nucl_encuest_1.codprov = eiel_c_abast_rd.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_c_abast_rd.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_c_abast_rd.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_c_abast_rd.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)
  GROUP BY eiel_c_abast_rd.codprov, eiel_c_abast_rd.codmunic, eiel_c_abast_rd.codentidad, eiel_c_abast_rd.codpoblamiento, eiel_c_abast_rd.material, eiel_c_abast_rd.sist_trans, eiel_c_abast_rd.estado, eiel_c_abast_rd.titular, eiel_c_abast_rd.gestor;

ALTER TABLE v_red_distribucion
  OWNER TO geopista;
GRANT ALL ON TABLE v_red_distribucion TO geopista;
GRANT SELECT ON TABLE v_red_distribucion TO consultas;


CREATE OR REPLACE VIEW v_sanea_autonomo AS 
 SELECT eiel_t_saneam_au.clave, eiel_t_saneam_au.codprov AS provincia, eiel_t_saneam_au.codmunic AS municipio, eiel_t_saneam_au.codentidad AS entidad, eiel_t_saneam_au.codpoblamiento AS nucleo, eiel_t_saneam_au.tipo_sau AS tipo_sanea, eiel_t_saneam_au.estado_sau AS estado, eiel_t_saneam_au.adecuacion_sau AS adecuacion, eiel_t_saneam_au.sau_vivien, eiel_t_saneam_au.sau_pob_re, eiel_t_saneam_au.sau_pob_es, eiel_t_saneam_au.sau_vi_def, eiel_t_saneam_au.sau_pob_re_def AS sau_re_def, eiel_t_saneam_au.sau_pob_es_def AS sau_es_def
   FROM eiel_t_saneam_au
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_au.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_au.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_au.revision_expirada = 9999999999::bigint::numeric
                    and (eiel_t_nucl_encuest_1.codprov = eiel_t_saneam_au.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_saneam_au.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_saneam_au.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_saneam_au.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_sanea_autonomo
  OWNER TO geopista;
GRANT ALL ON TABLE v_sanea_autonomo TO geopista;
GRANT SELECT ON TABLE v_sanea_autonomo TO consultas;



CREATE OR REPLACE VIEW v_tanatorio AS 
 SELECT eiel_t_ta.clave, eiel_t_ta.codprov AS provincia, eiel_t_ta.codmunic AS municipio, eiel_t_ta.codentidad AS entidad, eiel_t_ta.codpoblamiento AS poblamient, eiel_t_ta.orden_ta AS orden_tana, eiel_t_ta.nombre, eiel_t_ta.titular, eiel_t_ta.gestor AS gestion, eiel_t_ta.s_cubierta AS s_cubi, eiel_t_ta.s_aire, eiel_t_ta.s_solar AS s_sola, eiel_t_ta.salas, eiel_t_ta.acceso_s_ruedas, eiel_t_ta.estado
   FROM eiel_t_ta
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ta.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ta.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ta.revision_expirada = 9999999999::bigint::numeric
                    and (eiel_t_nucl_encuest_1.codprov = eiel_t_ta.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_ta.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_ta.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_ta.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_tanatorio
  OWNER TO geopista;
GRANT ALL ON TABLE v_tanatorio TO geopista;
GRANT SELECT ON TABLE v_tanatorio TO consultas;

CREATE OR REPLACE VIEW v_trat_pota_nucleo AS 
 SELECT eiel_tr_abast_tp_pobl.codprov_pobl AS provincia, eiel_tr_abast_tp_pobl.codmunic_pobl AS municipio, eiel_tr_abast_tp_pobl.codentidad_pobl AS entidad, eiel_tr_abast_tp_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_tp_pobl.clave_tp AS clave, eiel_tr_abast_tp_pobl.codprov_tp AS po_provin, eiel_tr_abast_tp_pobl.codmunic_tp AS po_munipi, eiel_tr_abast_tp_pobl.orden_tp AS orden_trat
   FROM eiel_tr_abast_tp_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_tp_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_tp_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_tp_pobl.revision_expirada = 9999999999::bigint::numeric
                    and (eiel_t_nucl_encuest_1.codprov = eiel_tr_abast_tp_pobl.codprov_pobl and eiel_t_nucl_encuest_1.codmunic = eiel_tr_abast_tp_pobl.codmunic_pobl and eiel_t_nucl_encuest_1.codentidad = eiel_tr_abast_tp_pobl.codentidad_pobl and eiel_t_nucl_encuest_1.codpoblamiento = eiel_tr_abast_tp_pobl.codpoblamiento_pobl and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_trat_pota_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_trat_pota_nucleo TO geopista;
GRANT SELECT ON TABLE v_trat_pota_nucleo TO consultas;

CREATE OR REPLACE VIEW v_vertedero_nucleo AS 
 SELECT DISTINCT eiel_tr_vt_pobl.codprov AS provincia, eiel_tr_vt_pobl.codmunic AS municipio, eiel_tr_vt_pobl.codentidad AS entidad, eiel_tr_vt_pobl.codpoblamiento AS nucleo, eiel_tr_vt_pobl.clave_vt AS clave, eiel_tr_vt_pobl.codprov_vt AS ver_provin, eiel_tr_vt_pobl.codmunic_vt AS ver_munici, eiel_tr_vt_pobl.orden_vt AS ver_codigo
   FROM eiel_tr_vt_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_vt_pobl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_vt_pobl.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_vt_pobl.revision_expirada = 9999999999::bigint::numeric
                      and (eiel_t_nucl_encuest_1.codprov = eiel_tr_vt_pobl.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_tr_vt_pobl.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_tr_vt_pobl.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_tr_vt_pobl.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas)
  ORDER BY eiel_tr_vt_pobl.codprov, eiel_tr_vt_pobl.codmunic, eiel_tr_vt_pobl.codentidad, eiel_tr_vt_pobl.codpoblamiento, eiel_tr_vt_pobl.clave_vt, eiel_tr_vt_pobl.codprov_vt, eiel_tr_vt_pobl.codmunic_vt, eiel_tr_vt_pobl.orden_vt;

ALTER TABLE v_vertedero_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_vertedero_nucleo TO geopista;
GRANT SELECT ON TABLE v_vertedero_nucleo TO consultas;


CREATE OR REPLACE VIEW v_deposito_enc AS 
 SELECT eiel_t_abast_de.clave, eiel_t_abast_de.codprov AS provincia, eiel_t_abast_de.codmunic AS municipio, eiel_t_abast_de.orden_de AS orden_depo, eiel_t_abast_de.ubicacion, eiel_t_abast_de.titular, eiel_t_abast_de.gestor AS gestion, eiel_t_abast_de.capacidad, eiel_t_abast_de.estado, eiel_t_abast_de.proteccion, eiel_t_abast_de.fecha_limpieza AS limpieza, eiel_t_abast_de.contador
   FROM eiel_t_abast_de
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_de.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_de.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 and (eiel_t_abast_de.codmunic::text || eiel_t_abast_de.orden_de::text IN ( SELECT DISTINCT v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text
           FROM v_deposito_agua_nucleo
          ORDER BY v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_de.revision_expirada = 9999999999::bigint::numeric;
ALTER TABLE v_deposito_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_deposito_enc TO geopista;
GRANT SELECT ON TABLE v_deposito_enc TO consultas;

  
  CREATE OR REPLACE VIEW v_captacion_enc AS 
 SELECT eiel_t_abast_ca.clave, eiel_t_abast_ca.codprov AS provincia, eiel_t_abast_ca.codmunic AS municipio, eiel_t_abast_ca.orden_ca AS orden_capt, eiel_t_abast_ca.nombre AS denominaci, eiel_t_abast_ca.tipo AS tipo_capt, eiel_t_abast_ca.titular, eiel_t_abast_ca.gestor AS gestion, eiel_t_abast_ca.sist_impulsion AS sistema_ca, eiel_t_abast_ca.estado, eiel_t_abast_ca.uso, eiel_t_abast_ca.proteccion, eiel_t_abast_ca.contador
   FROM eiel_t_abast_ca
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_ca.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_ca.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_ca.codmunic::text || eiel_t_abast_ca.orden_ca::text IN ( SELECT DISTINCT v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text
      FROM v_cap_agua_nucleo
     ORDER BY v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text)) AND eiel_t_abast_ca.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;
ALTER TABLE v_captacion_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_captacion_enc TO geopista;
GRANT SELECT ON TABLE v_captacion_enc TO consultas;

CREATE OR REPLACE VIEW v_colector_enc_m50 AS 
 SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia, eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
   FROM eiel_t_saneam_tcl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tcl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text
      FROM v_colector_nucleo
     ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric  AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_colector_enc_m50
  OWNER TO geopista;


CREATE OR REPLACE VIEW v_colector_enc AS 
 SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia, eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
   FROM eiel_t_saneam_tcl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tcl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text
      FROM v_colector_nucleo
     ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;


ALTER TABLE v_colector_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_colector_enc TO geopista;
GRANT SELECT ON TABLE v_colector_enc TO consultas;

CREATE OR REPLACE VIEW v_conduccion_enc AS 
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
   FROM eiel_t_abast_tcn
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tcn.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000  AND (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text
      FROM v_cond_agua_nucleo
     ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_conduccion_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_conduccion_enc TO geopista;
GRANT SELECT ON TABLE v_conduccion_enc TO consultas;


CREATE OR REPLACE VIEW v_depuradora_enc AS 
 SELECT eiel_t1_saneam_ed.clave, eiel_t1_saneam_ed.codprov AS provincia, eiel_t1_saneam_ed.codmunic AS municipio, eiel_t1_saneam_ed.orden_ed AS orden_depu, eiel_t1_saneam_ed.trat_pr_1, eiel_t1_saneam_ed.trat_pr_2, eiel_t1_saneam_ed.trat_pr_3, eiel_t1_saneam_ed.trat_sc_1, eiel_t1_saneam_ed.trat_sc_2, eiel_t1_saneam_ed.trat_sc_3, eiel_t1_saneam_ed.trat_av_1, eiel_t1_saneam_ed.trat_av_2, eiel_t1_saneam_ed.trat_av_3, eiel_t1_saneam_ed.proc_cm_1, eiel_t1_saneam_ed.proc_cm_2, eiel_t1_saneam_ed.proc_cm_3, eiel_t1_saneam_ed.trat_ld_1, eiel_t1_saneam_ed.trat_ld_2, eiel_t1_saneam_ed.trat_ld_3
   FROM eiel_t1_saneam_ed
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t1_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t1_saneam_ed.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t1_saneam_ed.codmunic::text || eiel_t1_saneam_ed.orden_ed::text IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text
      FROM v_dep_agua_nucleo
     ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_t1_saneam_ed.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_depuradora_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_depuradora_enc TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc TO consultas;

CREATE OR REPLACE VIEW v_depuradora_enc_2 AS 
 SELECT eiel_t2_saneam_ed.clave, eiel_t2_saneam_ed.codprov AS provincia, eiel_t2_saneam_ed.codmunic AS municipio, eiel_t2_saneam_ed.orden_ed AS orden_depu, eiel_t2_saneam_ed.titular, eiel_t2_saneam_ed.gestor AS gestion, eiel_t2_saneam_ed.capacidad, eiel_t2_saneam_ed.problem_1, eiel_t2_saneam_ed.problem_2, eiel_t2_saneam_ed.problem_3, eiel_t2_saneam_ed.lodo_gest, eiel_t2_saneam_ed.lodo_vert, eiel_t2_saneam_ed.lodo_inci, eiel_t2_saneam_ed.lodo_con_agri AS lodo_con_a, eiel_t2_saneam_ed.lodo_sin_agri AS lodo_sin_a, eiel_t2_saneam_ed.lodo_ot
   FROM eiel_t2_saneam_ed
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t2_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t2_saneam_ed.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000  AND (eiel_t2_saneam_ed.codmunic::text || eiel_t2_saneam_ed.orden_ed::text IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text
      FROM v_dep_agua_nucleo
     ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t2_saneam_ed.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_depuradora_enc_2
  OWNER TO geopista;
GRANT ALL ON TABLE v_depuradora_enc_2 TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc_2 TO consultas;


CREATE OR REPLACE VIEW v_emisario_enc AS 
 SELECT DISTINCT ON (eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em) eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_pv.tipo AS tipo_vert, eiel_t_saneam_pv.zona AS zona_vert, sum(eiel_t_saneam_pv.distancia_nucleo) AS distancia
   FROM eiel_t_saneam_tem
   LEFT JOIN eiel_t_saneam_pv ON eiel_t_saneam_pv.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_saneam_pv.codmunic::text = eiel_t_saneam_tem.codmunic::text AND eiel_t_saneam_pv.revision_expirada = 9999999999::bigint::numeric
   JOIN eiel_tr_saneam_tem_pv tempv ON tempv.clave_tem::text = eiel_t_saneam_tem.clave::text AND tempv.codprov_tem::text = eiel_t_saneam_tem.codprov::text AND tempv.codmunic_tem::text = eiel_t_saneam_tem.codmunic::text AND tempv.tramo_em::text = eiel_t_saneam_tem.tramo_em::text AND tempv.revision_expirada = 9999999999::bigint::numeric AND tempv.codprov_pv::text = eiel_t_saneam_pv.codprov::text AND tempv.codmunic_pv::text = eiel_t_saneam_pv.codmunic::text AND tempv.clave_pv::text = eiel_t_saneam_pv.clave::text AND tempv.orden_pv::text = eiel_t_saneam_pv.orden_pv::text
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
   FROM v_emisario_nucleo
  ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_pv.tipo, eiel_t_saneam_pv.zona
  ORDER BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_emisario_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_emisario_enc TO geopista;
GRANT SELECT ON TABLE v_emisario_enc TO consultas;

CREATE OR REPLACE VIEW v_potabilizacion_enc AS 
 SELECT eiel_t_abast_tp.clave, eiel_t_abast_tp.codprov AS provincia, eiel_t_abast_tp.codmunic AS municipio, eiel_t_abast_tp.orden_tp AS orden_trat, eiel_t_abast_tp.tipo AS tipo_tra, eiel_t_abast_tp.ubicacion, eiel_t_abast_tp.s_desinf, eiel_t_abast_tp.categoria_a1 AS cat_a1, eiel_t_abast_tp.categoria_a2 AS cat_a2, eiel_t_abast_tp.categoria_a3 AS cat_a3, eiel_t_abast_tp.desaladora, eiel_t_abast_tp.otros, eiel_t_abast_tp.desinf_1, eiel_t_abast_tp.desinf_2, eiel_t_abast_tp.desinf_3, eiel_t_abast_tp.periodicidad AS periodicid, eiel_t_abast_tp.organismo_control AS organismo, eiel_t_abast_tp.estado
   FROM eiel_t_abast_tp
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tp.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tp.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_tp.codmunic::text || eiel_t_abast_tp.orden_tp::text IN ( SELECT DISTINCT v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text
      FROM v_trat_pota_nucleo
     ORDER BY v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text)) AND eiel_t_abast_tp.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_potabilizacion_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_potabilizacion_enc TO geopista;
GRANT SELECT ON TABLE v_potabilizacion_enc TO consultas;

CREATE OR REPLACE VIEW v_tramo_colector AS 
 SELECT eiel_c_saneam_tcl.clave, eiel_c_saneam_tcl.codprov AS provincia, eiel_c_saneam_tcl.codmunic AS municipio, eiel_c_saneam_tcl.tramo_cl AS orden_cole, eiel_t_saneam_tcl.material AS tipo_colec, eiel_t_saneam_tcl.sist_impulsion AS sist_trans, eiel_t_saneam_tcl.estado, eiel_t_saneam_tcl.titular, eiel_t_saneam_tcl.gestor AS gestion, sum(eiel_c_saneam_tcl.longitud) AS long_tramo
   FROM eiel_t_saneam_tcl
   LEFT JOIN eiel_c_saneam_tcl ON eiel_t_saneam_tcl.clave::text = eiel_c_saneam_tcl.clave::text AND eiel_t_saneam_tcl.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_saneam_tcl.codmunic::text = eiel_c_saneam_tcl.codmunic::text AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_c_saneam_tcl.revision_expirada = 9999999999::bigint::numeric
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_tcl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text
   FROM v_colector_nucleo
  ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl, eiel_c_saneam_tcl.clave, eiel_c_saneam_tcl.codprov, eiel_t_saneam_tcl.material, eiel_t_saneam_tcl.sist_impulsion, eiel_t_saneam_tcl.estado, eiel_t_saneam_tcl.titular, eiel_t_saneam_tcl.gestor
  ORDER BY eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl;


ALTER TABLE v_tramo_colector
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_colector TO geopista;
GRANT SELECT ON TABLE v_tramo_colector TO consultas;


CREATE OR REPLACE VIEW v_tramo_conduccion AS 
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond, eiel_t_abast_tcn.material AS tipo_tcond, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor AS gestion, sum(eiel_c_abast_tcn.longitud) AS longitud
   FROM eiel_t_abast_tcn
   LEFT JOIN eiel_c_abast_tcn ON eiel_c_abast_tcn.clave::text = eiel_t_abast_tcn.clave::text AND eiel_c_abast_tcn.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_c_abast_tcn.codmunic::text = eiel_t_abast_tcn.codmunic::text AND eiel_c_abast_tcn.tramo_cn::text = eiel_t_abast_tcn.tramo_cn::text AND eiel_c_abast_tcn.revision_expirada = 9999999999::bigint::numeric
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tcn.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text
   FROM v_cond_agua_nucleo
  ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov, eiel_t_abast_tcn.codmunic, eiel_t_abast_tcn.tramo_cn, eiel_t_abast_tcn.material, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor;

ALTER TABLE v_tramo_conduccion
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_conduccion TO geopista;
GRANT SELECT ON TABLE v_tramo_conduccion TO consultas;

CREATE OR REPLACE VIEW v_tramo_emisario AS 
 SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_tem.material AS tipo_mat, eiel_t_saneam_tem.estado, sum(eiel_t_saneam_tem.long_terre) AS long_terre, sum(eiel_t_saneam_tem.long_marit) AS long_marit
   FROM eiel_t_saneam_tem
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
      FROM v_emisario_nucleo
     ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.material, eiel_t_saneam_tem.estado
  ORDER BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_tramo_emisario
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_emisario TO geopista;
GRANT SELECT ON TABLE v_tramo_emisario TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_1 AS 
 SELECT eiel_t_nucl_encuest_1.codprov AS provincia, eiel_t_nucl_encuest_1.codmunic AS municipio, eiel_t_nucl_encuest_1.codentidad AS entidad, eiel_t_nucl_encuest_1.codpoblamiento AS nucleo, eiel_t_nucl_encuest_1.padron, eiel_t_nucl_encuest_1.pob_estacional AS pob_estaci, eiel_t_nucl_encuest_1.altitud, eiel_t_nucl_encuest_1.viviendas_total AS viv_total, eiel_t_nucl_encuest_1.hoteles, eiel_t_nucl_encuest_1.casas_rural AS casas_rura, eiel_t_nucl_encuest_1.accesibilidad AS accesib
   FROM eiel_t_nucl_encuest_1
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucl_encuest_1.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucl_encuest_1.codmunic::text, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric
  and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_nucl_encuestado_1
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_1 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_1 TO consultas;


CREATE OR REPLACE VIEW v_nucl_encuestado_2 AS 
 SELECT eiel_t_nucl_encuest_2.codprov AS provincia, eiel_t_nucl_encuest_2.codmunic AS municipio, eiel_t_nucl_encuest_2.codentidad AS entidad, eiel_t_nucl_encuest_2.codpoblamiento AS nucleo, eiel_t_nucl_encuest_2.aag_caudal, eiel_t_nucl_encuest_2.aag_restri, eiel_t_nucl_encuest_2.aag_contad, eiel_t_nucl_encuest_2.aag_tasa, eiel_t_nucl_encuest_2.aag_instal, eiel_t_nucl_encuest_2.aag_hidran, eiel_t_nucl_encuest_2.aag_est_hi, eiel_t_nucl_encuest_2.aag_valvul, eiel_t_nucl_encuest_2.aag_est_va, eiel_t_nucl_encuest_2.aag_bocasr, eiel_t_nucl_encuest_2.aag_est_bo, eiel_t_nucl_encuest_2.cisterna
   FROM eiel_t_nucl_encuest_2
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucl_encuest_2.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucl_encuest_2.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_2.revision_expirada = 9999999999::bigint::numeric
                and (eiel_t_nucl_encuest_1.codprov = eiel_t_nucl_encuest_2.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_nucl_encuest_2.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_nucl_encuest_2.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_nucl_encuest_2.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_nucl_encuestado_2
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_2 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_2 TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_3 AS 
 SELECT eiel_t_abast_serv.codprov AS provincia, eiel_t_abast_serv.codmunic AS municipio, eiel_t_abast_serv.codentidad AS entidad, eiel_t_abast_serv.codpoblamiento AS nucleo, eiel_t_abast_serv.viviendas_c_conex AS aag_v_cone, eiel_t_abast_serv.viviendas_s_conexion AS aag_v_ncon, eiel_t_abast_serv.consumo_inv AS aag_c_invi, eiel_t_abast_serv.consumo_verano AS aag_c_vera, eiel_t_abast_serv.viv_exceso_pres AS aag_v_expr, eiel_t_abast_serv.viv_defic_presion AS aag_v_depr, eiel_t_abast_serv.perdidas_agua AS aag_perdid, eiel_t_abast_serv.calidad_serv AS aag_calida, eiel_t_abast_serv.long_deficit AS aag_l_defi, eiel_t_abast_serv.viv_deficitarias AS aag_v_defi, eiel_t_abast_serv.pobl_res_afect AS aag_pr_def, eiel_t_abast_serv.pobl_est_afect AS aag_pe_def
   FROM eiel_t_abast_serv
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_serv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_serv.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_serv.revision_expirada = 9999999999::bigint::numeric
                and (eiel_t_nucl_encuest_1.codprov = eiel_t_abast_serv.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_abast_serv.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_abast_serv.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_abast_serv.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_nucl_encuestado_3
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_3 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_3 TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_4 AS 
 SELECT eiel_t_abast_au.codprov AS provincia, eiel_t_abast_au.codmunic AS municipio, eiel_t_abast_au.codentidad AS entidad, eiel_t_abast_au.codpoblamiento AS nucleo, eiel_t_abast_au.aau_vivien, eiel_t_abast_au.aau_pob_re, eiel_t_abast_au.aau_pob_es, eiel_t_abast_au.aau_def_vi, eiel_t_abast_au.aau_def_re, eiel_t_abast_au.aau_def_es, eiel_t_abast_au.aau_fecont, eiel_t_abast_au.aau_fencon, eiel_t_abast_au.aau_caudal
   FROM eiel_t_abast_au
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_au.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_au.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_au.revision_expirada = 9999999999::bigint::numeric
                and (eiel_t_nucl_encuest_1.codprov = eiel_t_abast_au.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_abast_au.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_abast_au.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_abast_au.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_nucl_encuestado_4
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_4 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_4 TO consultas;


CREATE OR REPLACE VIEW v_nucl_encuestado_5 AS 
 SELECT eiel_t_saneam_serv.codprov AS provincia, eiel_t_saneam_serv.codmunic AS municipio, eiel_t_saneam_serv.codentidad AS entidad, eiel_t_saneam_serv.codpoblamiento AS nucleo, eiel_t_saneam_serv.pozos_registro AS syd_pozos, eiel_t_saneam_serv.sumideros AS syd_sumide, eiel_t_saneam_serv.aliv_c_acum AS syd_ali_co, eiel_t_saneam_serv.aliv_s_acum AS syd_ali_si, eiel_t_saneam_serv.calidad_serv AS syd_calida, eiel_t_saneam_serv.viviendas_c_conex AS syd_v_cone, eiel_t_saneam_serv.viviendas_s_conex AS syd_v_ncon, eiel_t_saneam_serv.long_rs_deficit AS syd_l_defi, eiel_t_saneam_serv.viviendas_def_conex AS syd_v_defi, eiel_t_saneam_serv.pobl_res_def_afect AS syd_pr_def, eiel_t_saneam_serv.pobl_est_def_afect AS syd_pe_def, eiel_t_saneam_serv.caudal_total AS syd_c_desa, eiel_t_saneam_serv.caudal_tratado AS syd_c_trat, eiel_t_saneam_serv.c_reutilizado_urb AS syd_re_urb, eiel_t_saneam_serv.c_reutilizado_rust AS syd_re_rus, eiel_t_saneam_serv.c_reutilizado_ind AS syd_re_ind
   FROM eiel_t_saneam_serv
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_serv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_serv.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_serv.revision_expirada = 9999999999::bigint::numeric
                and (eiel_t_nucl_encuest_1.codprov = eiel_t_saneam_serv.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_saneam_serv.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_saneam_serv.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_saneam_serv.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_nucl_encuestado_5
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_5 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_5 TO consultas;


CREATE OR REPLACE VIEW v_nucl_encuestado_6 AS 
 SELECT eiel_t_rb_serv.codprov AS provincia, eiel_t_rb_serv.codmunic AS municipio, eiel_t_rb_serv.codentidad AS entidad, eiel_t_rb_serv.codpoblamiento AS nucleo, eiel_t_rb_serv.srb_viviendas_afec AS rba_v_sser, eiel_t_rb_serv.srb_pob_res_afect AS rba_pr_sse, eiel_t_rb_serv.srb_pob_est_afect AS rba_pe_sse, eiel_t_rb_serv.serv_limp_calles AS rba_serlim, eiel_t_rb_serv.plantilla_serv_limp AS rba_plalim
   FROM eiel_t_rb_serv
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_rb_serv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_rb_serv.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_rb_serv.revision_expirada = 9999999999::bigint::numeric
                and (eiel_t_nucl_encuest_1.codprov = eiel_t_rb_serv.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_rb_serv.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_rb_serv.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_rb_serv.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_nucl_encuestado_6
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_6 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_6 TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_7 AS 
 SELECT eiel_t_inf_ttmm.codprov AS provincia, eiel_t_inf_ttmm.codmunic AS municipio, eiel_t_inf_ttmm.codentidad AS entidad, eiel_t_inf_ttmm.codpoblamiento AS poblamient, eiel_t_inf_ttmm.tv_ant, eiel_t_inf_ttmm.tv_ca, eiel_t_inf_ttmm.tm_gsm, eiel_t_inf_ttmm.tm_umts, eiel_t_inf_ttmm.tm_gprs, eiel_t_inf_ttmm.correo, eiel_t_inf_ttmm.ba_rd, eiel_t_inf_ttmm.ba_xd, eiel_t_inf_ttmm.ba_wi, eiel_t_inf_ttmm.ba_ca, eiel_t_inf_ttmm.ba_rb, eiel_t_inf_ttmm.ba_st, eiel_t_inf_ttmm.capi, eiel_t_inf_ttmm.electric AS electricid, eiel_t_inf_ttmm.gas, eiel_t_inf_ttmm.alu_v_sin, eiel_t_inf_ttmm.alu_l_sin
   FROM eiel_t_inf_ttmm
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_inf_ttmm.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_inf_ttmm.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_inf_ttmm.revision_expirada = 9999999999::bigint::numeric
                and (eiel_t_nucl_encuest_1.codprov = eiel_t_inf_ttmm.codprov and eiel_t_nucl_encuest_1.codmunic = eiel_t_inf_ttmm.codmunic and eiel_t_nucl_encuest_1.codentidad = eiel_t_inf_ttmm.codentidad and eiel_t_nucl_encuest_1.codpoblamiento = eiel_t_inf_ttmm.codpoblamiento and eiel_t_nucl_encuest_1.revision_expirada  = 9999999999 and eiel_t_nucl_encuest_1.padron > eiel_configuracion_shp.padron and eiel_t_nucl_encuest_1.viviendas_total >= eiel_configuracion_shp.viviendas);

ALTER TABLE v_nucl_encuestado_7
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_7 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_7 TO consultas;

CREATE OR REPLACE VIEW v_carretera AS 
 SELECT DISTINCT ON (eiel_t_carreteras.codprov, eiel_t_carreteras.cod_carrt) eiel_t_carreteras.codprov AS provincia, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion AS denominaci
   FROM eiel_c_tramos_carreteras, eiel_t_carreteras
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_carreteras.codprov::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_carreteras.revision_expirada = 9999999999::bigint::numeric AND eiel_c_tramos_carreteras.id_municipio::text = (eiel_t_padron_ttmm.codprov::text || eiel_t_padron_ttmm.codmunic::text) AND eiel_t_carreteras.cod_carrt::text = eiel_c_tramos_carreteras.cod_carrt::text AND eiel_c_tramos_carreteras.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_carreteras.codprov, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion
  ORDER BY eiel_t_carreteras.codprov, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion;

ALTER TABLE v_carretera
  OWNER TO geopista;
GRANT ALL ON TABLE v_carretera TO geopista;
GRANT SELECT ON TABLE v_carretera TO consultas;

