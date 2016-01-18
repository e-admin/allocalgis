update dictionary set traduccion='Riego Asfáltico' where traduccion='Riego Asíatico';

create or replace function f_add_col (t_name regclass,c_name text, sql text) returns void AS $$
begin
    IF EXISTS (
		SELECT 1 FROM pg_attribute
			WHERE  attrelid = t_name
			AND    attname = c_name
			AND    NOT attisdropped) THEN
			RAISE NOTICE 'Column % already exists in %', c_name, t_name;
	ELSE
		EXECUTE sql;
	END IF;
end;
$$ language 'plpgsql';

select f_add_col('public.usrgrouperm','name', 'alter table usrgrouperm ADD COLUMN name int4');


DROP VIEW v_CAPTACION_ENC_M50;

CREATE OR REPLACE VIEW v_CAPTACION_ENC_M50 AS
SELECT eiel_t_abast_ca.clave, eiel_t_abast_ca.codprov AS provincia,
    eiel_t_abast_ca.codmunic AS municipio, eiel_t_abast_ca.orden_ca AS
    orden_capt, eiel_t_abast_ca.nombre AS denominaci, eiel_t_abast_ca.tipo AS
    tipo_capt, eiel_t_abast_ca.titular, eiel_t_abast_ca.gestor AS gestion,
    eiel_t_abast_ca.sist_impulsion AS sistema_ca, eiel_t_abast_ca.estado,
    eiel_t_abast_ca.uso, eiel_t_abast_ca.proteccion, eiel_t_abast_ca.contador
FROM (eiel_t_abast_ca LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t_abast_ca.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t_abast_ca.codmunic)::text))))
WHERE (((((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
    ((eiel_t_abast_ca.codmunic)::text|| (eiel_t_abast_ca.orden_ca)::text IN (
    select distinct(v_cap_agua_nucleo.c_municip || v_cap_agua_nucleo.orden_capt) from v_cap_agua_nucleo
    )))) AND (eiel_t_abast_ca.revision_expirada =
        (9999999999::bigint)::numeric)) AND
        (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric));


ALTER TABLE v_CAPTACION_ENC_M50 OWNER TO geopista;



DROP VIEW v_captacion_agua;

CREATE OR REPLACE VIEW v_captacion_agua AS
	SELECT eiel_t_abast_ca.clave, eiel_t_abast_ca.codprov AS provincia,
    eiel_t_abast_ca.codmunic AS municipio, eiel_t_abast_ca.orden_ca AS orden_capt
FROM eiel_t_abast_ca
WHERE 	((eiel_t_abast_ca.codmunic)::text|| (eiel_t_abast_ca.orden_ca)::text IN (
    select distinct(v_cap_agua_nucleo.c_municip || v_cap_agua_nucleo.orden_capt) from v_cap_agua_nucleo
    ))  AND (eiel_t_abast_ca.revision_expirada = (9999999999::bigint)::numeric);



ALTER TABLE v_captacion_agua OWNER TO geopista;



DROP VIEW v_conduccion_enc_m50;


CREATE VIEW "public"."v_conduccion_enc_m50" (
    clave,
    provincia,
    municipio,
    orden_cond)
AS
SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia,
    eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
FROM (eiel_t_abast_tcn LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t_abast_tcn.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t_abast_tcn.codmunic)::text))))
WHERE (((((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) 	AND
    ((eiel_t_abast_tcn.codmunic)::text|| (eiel_t_abast_tcn.tramo_cn)::text IN (
    select distinct(v_cond_agua_nucleo.cond_munic || v_cond_agua_nucleo.orden_cond) from v_cond_agua_nucleo
    )))) AND (eiel_t_padron_ttmm.revision_expirada =
        (9999999999::bigint)::numeric)) AND (eiel_t_abast_tcn.revision_expirada
        = (9999999999::bigint)::numeric));
		
ALTER TABLE v_conduccion_enc_m50 OWNER TO geopista;




ALTER TABLE v_conduccion OWNER TO geopista;

CREATE OR REPLACE VIEW "public"."v_conduccion" (
    clave,
    provincia,
    municipio,
    orden_cond)
AS
SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia,
    eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
FROM eiel_t_abast_tcn
WHERE ((eiel_t_abast_tcn.codmunic)::text|| (eiel_t_abast_tcn.tramo_cn)::text IN (
    select distinct(v_cond_agua_nucleo.cond_munic || v_cond_agua_nucleo.orden_cond) from v_cond_agua_nucleo
    ))  AND (eiel_t_abast_tcn.revision_expirada = (9999999999::bigint)::numeric);


ALTER TABLE v_conduccion OWNER TO geopista;


DROP VIEW v_deposito_enc_m50;


CREATE OR REPLACE VIEW "public"."v_deposito_enc_m50" (
    clave,
    provincia,
    municipio,
    orden_depo,
    ubicacion,
    titular,
    gestion,
    capacidad,
    estado,
    proteccion,
    limpieza,
    contador)
AS
SELECT eiel_t_abast_de.clave, eiel_t_abast_de.codprov AS provincia,
    eiel_t_abast_de.codmunic AS municipio, eiel_t_abast_de.orden_de AS
    orden_depo, eiel_t_abast_de.ubicacion, eiel_t_abast_de.titular,
    eiel_t_abast_de.gestor AS gestion, eiel_t_abast_de.capacidad,
    eiel_t_abast_de.estado, eiel_t_abast_de.proteccion,
    eiel_t_abast_de.fecha_limpieza AS limpieza, eiel_t_abast_de.contador
FROM (eiel_t_abast_de LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t_abast_de.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t_abast_de.codmunic)::text))))
WHERE (((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
    ((eiel_t_abast_de.codmunic)::text|| (eiel_t_abast_de.orden_de)::text IN (
    select distinct(v_deposito_agua_nucleo.de_municip || v_deposito_agua_nucleo.orden_depo) from v_deposito_agua_nucleo
    )))) 
	AND (eiel_t_abast_de.revision_expirada = (9999999999::bigint)::numeric) AND
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric);
	
ALTER TABLE v_deposito_enc_m50 OWNER TO geopista;



DROP VIEW v_deposito;

CREATE OR REPLACE VIEW "public"."v_deposito" (
    clave,
    provincia,
    municipio,
    orden_depo)
AS
SELECT eiel_t_abast_de.clave, eiel_t_abast_de.codprov AS provincia,
    eiel_t_abast_de.codmunic AS municipio, eiel_t_abast_de.orden_de AS orden_depo
FROM eiel_t_abast_de
WHERE ((eiel_t_abast_de.codmunic)::text|| (eiel_t_abast_de.orden_de)::text IN (
    select distinct(v_deposito_agua_nucleo.de_municip || v_deposito_agua_nucleo.orden_depo) from v_deposito_agua_nucleo
    ))  AND (eiel_t_abast_de.revision_expirada = (9999999999::bigint)::numeric);

ALTER TABLE v_deposito OWNER TO geopista;

DROP VIEW v_emisario_enc_m50;


CREATE OR REPLACE VIEW "public"."v_emisario_enc_m50" (
    clave,
    provincia,
    municipio,
    orden_emis,
    tipo_vert,
    zona_vert,
    distancia)
AS
SELECT DISTINCT ON (eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov,
    eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em)
    eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia,
    eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS
    orden_emis, eiel_t_saneam_pv.tipo AS tipo_vert, eiel_t_saneam_pv.zona AS
    zona_vert, sum(eiel_t_saneam_pv.distancia_nucleo) AS distancia
FROM (((eiel_t_saneam_tem LEFT JOIN eiel_t_saneam_pv ON
    (((((eiel_t_saneam_pv.codprov)::text = (eiel_t_saneam_tem.codprov)::text)
    AND ((eiel_t_saneam_pv.codmunic)::text =
    (eiel_t_saneam_tem.codmunic)::text)) AND
    (eiel_t_saneam_pv.revision_expirada = (9999999999::bigint)::numeric))))
    JOIN eiel_tr_saneam_tem_pv tempv ON (((((((((((tempv.clave_tem)::text =
    (eiel_t_saneam_tem.clave)::text) AND ((tempv.codprov_tem)::text =
    (eiel_t_saneam_tem.codprov)::text)) AND ((tempv.codmunic_tem)::text =
    (eiel_t_saneam_tem.codmunic)::text)) AND ((tempv.tramo_em)::text =
    (eiel_t_saneam_tem.tramo_em)::text)) AND (tempv.revision_expirada =
    (9999999999::bigint)::numeric)) AND ((tempv.codprov_pv)::text =
    (eiel_t_saneam_pv.codprov)::text)) AND ((tempv.codmunic_pv)::text =
    (eiel_t_saneam_pv.codmunic)::text)) AND ((tempv.clave_pv)::text =
    (eiel_t_saneam_pv.clave)::text)) AND ((tempv.orden_pv)::text =
    (eiel_t_saneam_pv.orden_pv)::text)))) LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t_saneam_tem.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t_saneam_tem.codmunic)::text))))
WHERE (((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND 
	((eiel_t_saneam_tem.codmunic)::text|| (eiel_t_saneam_tem.tramo_em)::text IN (
    select distinct(v_emisario_nucleo.em_municip || v_emisario_nucleo.orden_emis) from v_emisario_nucleo
    ))) 
	AND 
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric))
GROUP BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov,
    eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em,
    eiel_t_saneam_pv.tipo, eiel_t_saneam_pv.zona
ORDER BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov,
    eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;
	
ALTER TABLE v_emisario_enc_m50 OWNER TO geopista;



DROP VIEW v_emisario;


CREATE OR REPLACE VIEW "public"."v_emisario" (
    clave,
    provincia,
    municipio,
    orden_emis)
AS
SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia,
    eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis
FROM eiel_t_saneam_tem
WHERE ((eiel_t_saneam_tem.codmunic)::text|| (eiel_t_saneam_tem.tramo_em)::text IN (
    select distinct(v_emisario_nucleo.em_municip || v_emisario_nucleo.orden_emis) from v_emisario_nucleo
    )) AND
	(eiel_t_saneam_tem.revision_expirada = (9999999999::bigint)::numeric);

ALTER TABLE v_emisario OWNER TO geopista;



DROP VIEW v_colector_enc_m50;

CREATE OR REPLACE VIEW "public"."v_colector_enc_m50" (
    clave,
    provincia,
    municipio,
    orden_cole)
AS
SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia,
    eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
FROM (eiel_t_saneam_tcl LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t_saneam_tcl.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t_saneam_tcl.codmunic)::text))))
WHERE ((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
	((eiel_t_saneam_tcl.codmunic)::text|| (eiel_t_saneam_tcl.tramo_cl)::text IN (
    select distinct(v_colector_nucleo.c_municip || v_colector_nucleo.orden_cole) from v_colector_nucleo
    ))) AND 
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric);
	
ALTER TABLE v_colector_enc_m50 OWNER TO geopista;


DROP VIEW v_colector;

CREATE OR REPLACE VIEW "public"."v_colector" (
    clave,
    provincia,
    municipio,
    orden_cole)
AS
SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia,
    eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
FROM eiel_t_saneam_tcl
WHERE ((eiel_t_saneam_tcl.codmunic)::text|| (eiel_t_saneam_tcl.tramo_cl)::text IN (
    select distinct(v_colector_nucleo.c_municip || v_colector_nucleo.orden_cole) from v_colector_nucleo
    ))  AND (eiel_t_saneam_tcl.revision_expirada = (9999999999::bigint)::numeric);

	
ALTER TABLE v_colector OWNER TO geopista;


DROP VIEW v_depuradora_enc_m50;

CREATE OR REPLACE VIEW "public"."v_depuradora_enc_m50" (
    clave,
    provincia,
    municipio,
    orden_depu,
    trat_pr_1,
    trat_pr_2,
    trat_pr_3,
    trat_sc_1,
    trat_sc_2,
    trat_sc_3,
    trat_av_1,
    trat_av_2,
    trat_av_3,
    proc_cm_1,
    proc_cm_2,
    proc_cm_3,
    trat_ld_1,
    trat_ld_2,
    trat_ld_3)
AS
SELECT eiel_t1_saneam_ed.clave, eiel_t1_saneam_ed.codprov AS provincia,
    eiel_t1_saneam_ed.codmunic AS municipio, eiel_t1_saneam_ed.orden_ed AS
    orden_depu, eiel_t1_saneam_ed.trat_pr_1, eiel_t1_saneam_ed.trat_pr_2,
    eiel_t1_saneam_ed.trat_pr_3, eiel_t1_saneam_ed.trat_sc_1,
    eiel_t1_saneam_ed.trat_sc_2, eiel_t1_saneam_ed.trat_sc_3,
    eiel_t1_saneam_ed.trat_av_1, eiel_t1_saneam_ed.trat_av_2,
    eiel_t1_saneam_ed.trat_av_3, eiel_t1_saneam_ed.proc_cm_1,
    eiel_t1_saneam_ed.proc_cm_2, eiel_t1_saneam_ed.proc_cm_3,
    eiel_t1_saneam_ed.trat_ld_1, eiel_t1_saneam_ed.trat_ld_2,
    eiel_t1_saneam_ed.trat_ld_3
FROM (eiel_t1_saneam_ed LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t1_saneam_ed.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t1_saneam_ed.codmunic)::text))))
WHERE (((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
	((eiel_t1_saneam_ed.codmunic)::text|| (eiel_t1_saneam_ed.orden_ed)::text IN (
    select distinct(v_dep_agua_nucleo.de_municip || v_dep_agua_nucleo.orden_depu) from v_dep_agua_nucleo
    ))) AND
    (eiel_t1_saneam_ed.revision_expirada = (9999999999::bigint)::numeric)) AND
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric);
	
		
ALTER TABLE v_depuradora_enc_m50 OWNER TO geopista;



DROP VIEW v_depuradora;


CREATE OR REPLACE VIEW "public"."v_depuradora" (
    clave,
    provincia,
    municipio,
    orden_depu)
AS
SELECT eiel_t1_saneam_ed.clave, eiel_t1_saneam_ed.codprov AS provincia,
    eiel_t1_saneam_ed.codmunic AS municipio, eiel_t1_saneam_ed.orden_ed AS orden_depu
FROM (eiel_t1_saneam_ed LEFT JOIN eiel_t2_saneam_ed ON
    ((((((eiel_t2_saneam_ed.codprov)::text = (eiel_t1_saneam_ed.codprov)::text)
    AND ((eiel_t2_saneam_ed.codmunic)::text =
    (eiel_t1_saneam_ed.codmunic)::text)) AND ((eiel_t1_saneam_ed.clave)::text =
    (eiel_t2_saneam_ed.clave)::text)) AND ((eiel_t1_saneam_ed.orden_ed)::text =
    (eiel_t2_saneam_ed.orden_ed)::text))))
WHERE ((eiel_t1_saneam_ed.codmunic)::text|| (eiel_t1_saneam_ed.orden_ed)::text IN (
    select distinct(v_dep_agua_nucleo.de_municip || v_dep_agua_nucleo.orden_depu) from v_dep_agua_nucleo
    ))  AND ((eiel_t1_saneam_ed.revision_expirada = (9999999999::bigint)::numeric)
    AND (eiel_t2_saneam_ed.revision_expirada = (9999999999::bigint)::numeric));
	
ALTER TABLE v_depuradora OWNER TO geopista;





DROP VIEW v_depuradora_enc_2_m50;


CREATE VIEW "public"."v_depuradora_enc_2_m50" (
    clave,
    provincia,
    municipio,
    orden_depu,
    titular,
    gestion,
    capacidad,
    problem_1,
    problem_2,
    problem_3,
    lodo_gest,
    lodo_vert,
    lodo_inci,
    lodo_con_a,
    lodo_sin_a,
    lodo_ot)
AS
SELECT eiel_t2_saneam_ed.clave, eiel_t2_saneam_ed.codprov AS provincia,
    eiel_t2_saneam_ed.codmunic AS municipio, eiel_t2_saneam_ed.orden_ed AS
    orden_depu, eiel_t2_saneam_ed.titular, eiel_t2_saneam_ed.gestor AS gestion,
    eiel_t2_saneam_ed.capacidad, eiel_t2_saneam_ed.problem_1,
    eiel_t2_saneam_ed.problem_2, eiel_t2_saneam_ed.problem_3,
    eiel_t2_saneam_ed.lodo_gest, eiel_t2_saneam_ed.lodo_vert,
    eiel_t2_saneam_ed.lodo_inci, eiel_t2_saneam_ed.lodo_con_agri AS lodo_con_a,
    eiel_t2_saneam_ed.lodo_sin_agri AS lodo_sin_a, eiel_t2_saneam_ed.lodo_ot
FROM (eiel_t2_saneam_ed LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t2_saneam_ed.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t2_saneam_ed.codmunic)::text))))
WHERE (((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
	((eiel_t2_saneam_ed.codmunic)::text|| (eiel_t2_saneam_ed.orden_ed)::text IN (
    select distinct(v_dep_agua_nucleo.de_municip || v_dep_agua_nucleo.orden_depu) from v_dep_agua_nucleo
    ))) AND
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric)) AND
    (eiel_t2_saneam_ed.revision_expirada = (9999999999::bigint)::numeric);

	
ALTER TABLE v_depuradora_enc_2_m50 OWNER TO geopista;



DROP VIEW v_potabilizacion_enc_m50;

CREATE OR REPLACE VIEW "public"."v_potabilizacion_enc_m50" (
    clave,
    provincia,
    municipio,
    orden_trat,
    tipo_tra,
    ubicacion,
    s_desinf,
    cat_a1,
    cat_a2,
    cat_a3,
    desaladora,
    otros,
    desinf_1,
    desinf_2,
    desinf_3,
    periodicid,
    organismo,
    estado)
AS
SELECT eiel_t_abast_tp.clave, eiel_t_abast_tp.codprov AS provincia,
    eiel_t_abast_tp.codmunic AS municipio, eiel_t_abast_tp.orden_tp AS
    orden_trat, eiel_t_abast_tp.tipo AS tipo_tra, eiel_t_abast_tp.ubicacion,
    eiel_t_abast_tp.s_desinf, eiel_t_abast_tp.categoria_a1 AS cat_a1,
    eiel_t_abast_tp.categoria_a2 AS cat_a2, eiel_t_abast_tp.categoria_a3 AS
    cat_a3, eiel_t_abast_tp.desaladora, eiel_t_abast_tp.otros,
    eiel_t_abast_tp.desinf_1, eiel_t_abast_tp.desinf_2,
    eiel_t_abast_tp.desinf_3, eiel_t_abast_tp.periodicidad AS periodicid,
    eiel_t_abast_tp.organismo_control AS organismo, eiel_t_abast_tp.estado
FROM (eiel_t_abast_tp LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t_abast_tp.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t_abast_tp.codmunic)::text))))
WHERE (((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
	((eiel_t_abast_tp.codmunic)::text|| (eiel_t_abast_tp.orden_tp)::text IN (
    select distinct(v_trat_pota_nucleo.po_munipi || v_trat_pota_nucleo.orden_trat) from v_trat_pota_nucleo
    ))) AND
    (eiel_t_abast_tp.revision_expirada = (9999999999::bigint)::numeric)) AND
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric);
	
ALTER TABLE v_potabilizacion_enc_m50 OWNER TO geopista;
	
DROP VIEW v_tra_potabilizacion;

CREATE OR REPLACE VIEW "public"."v_tra_potabilizacion" (
    clave,
    provincia,
    municipio,
    orden_trat)
AS
SELECT eiel_t_abast_tp.clave, eiel_t_abast_tp.codprov AS provincia,
    eiel_t_abast_tp.codmunic AS municipio, eiel_t_abast_tp.orden_tp AS orden_trat
FROM eiel_t_abast_tp
WHERE ((eiel_t_abast_tp.codmunic)::text|| (eiel_t_abast_tp.orden_tp)::text IN (
    select distinct(v_trat_pota_nucleo.po_munipi || v_trat_pota_nucleo.orden_trat) from v_trat_pota_nucleo
    ))  AND (eiel_t_abast_tp.revision_expirada = (9999999999::bigint)::numeric)
ORDER BY eiel_t_abast_tp.codmunic, eiel_t_abast_tp.orden_tp;

ALTER TABLE v_tra_potabilizacion OWNER TO geopista;


DROP VIEW v_tramo_colector_m50;

CREATE VIEW "public"."v_tramo_colector_m50" (
    clave,
    provincia,
    municipio,
    orden_cole,
    tipo_colec,
    sist_trans,
    estado,
    titular,
    gestion,
    long_tramo)
AS
SELECT eiel_c_saneam_tcl.clave, eiel_c_saneam_tcl.codprov AS provincia,
    eiel_c_saneam_tcl.codmunic AS municipio, eiel_c_saneam_tcl.tramo_cl AS
    orden_cole, eiel_t_saneam_tcl.material AS tipo_colec,
    eiel_t_saneam_tcl.sist_impulsion AS sist_trans, eiel_t_saneam_tcl.estado,
    eiel_t_saneam_tcl.titular, eiel_t_saneam_tcl.gestor AS gestion,
    sum(eiel_c_saneam_tcl.longitud) AS long_tramo
FROM ((eiel_t_saneam_tcl LEFT JOIN eiel_c_saneam_tcl ON
    (((((((eiel_t_saneam_tcl.clave)::text = (eiel_c_saneam_tcl.clave)::text)
    AND ((eiel_t_saneam_tcl.codprov)::text =
    (eiel_c_saneam_tcl.codprov)::text)) AND ((eiel_t_saneam_tcl.codmunic)::text
    = (eiel_c_saneam_tcl.codmunic)::text)) AND
    ((eiel_t_saneam_tcl.tramo_cl)::text = (eiel_c_saneam_tcl.tramo_cl)::text))
    AND (eiel_c_saneam_tcl.revision_expirada =
    (9999999999::bigint)::numeric)))) LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_c_saneam_tcl.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_c_saneam_tcl.codmunic)::text))))
WHERE ((((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
	((eiel_t_saneam_tcl.codmunic)::text|| (eiel_t_saneam_tcl.tramo_cl)::text IN (
    select distinct(v_colector_nucleo.c_municip || v_colector_nucleo.orden_cole) from v_colector_nucleo
    ))) AND
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric)) AND
    ((eiel_t_saneam_tcl.tramo_cl)::text = (eiel_c_saneam_tcl.tramo_cl)::text))
    AND (eiel_t_saneam_tcl.revision_expirada = (9999999999::bigint)::numeric) 
GROUP BY eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl,
    eiel_c_saneam_tcl.clave, eiel_c_saneam_tcl.codprov,
    eiel_t_saneam_tcl.material, eiel_t_saneam_tcl.sist_impulsion,
    eiel_t_saneam_tcl.estado, eiel_t_saneam_tcl.titular, eiel_t_saneam_tcl.gestor
ORDER BY eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl;

ALTER TABLE v_tramo_colector_m50 OWNER TO geopista;
	
	
DROP VIEW v_tramo_conduccion_m50;

CREATE VIEW "public"."v_tramo_conduccion_m50" (
    clave,
    provincia,
    municipio,
    orden_cond,
    tipo_tcond,
    estado,
    titular,
    gestion,
    longitud)
AS
SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia,
    eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS
    orden_cond, eiel_t_abast_tcn.material AS tipo_tcond,
    eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor
    AS gestion, sum(eiel_c_abast_tcn.longitud) AS longitud
FROM ((eiel_t_abast_tcn LEFT JOIN eiel_c_abast_tcn ON
    (((((((eiel_c_abast_tcn.clave)::text = (eiel_t_abast_tcn.clave)::text) AND
    ((eiel_c_abast_tcn.codprov)::text = (eiel_t_abast_tcn.codprov)::text)) AND
    ((eiel_c_abast_tcn.codmunic)::text = (eiel_t_abast_tcn.codmunic)::text))
    AND ((eiel_c_abast_tcn.tramo_cn)::text =
    (eiel_t_abast_tcn.tramo_cn)::text)) AND (eiel_c_abast_tcn.revision_expirada
    = (9999999999::bigint)::numeric)))) LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t_abast_tcn.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t_abast_tcn.codmunic)::text))))
WHERE (((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
    ((eiel_t_abast_tcn.codmunic)::text|| (eiel_t_abast_tcn.tramo_cn)::text IN (
    select distinct(v_cond_agua_nucleo.cond_munic || v_cond_agua_nucleo.orden_cond) from v_cond_agua_nucleo
    ))))  AND
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric) AND
    (eiel_t_abast_tcn.revision_expirada = (9999999999::bigint)::numeric)
GROUP BY eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov,
    eiel_t_abast_tcn.codmunic, eiel_t_abast_tcn.tramo_cn,
    eiel_t_abast_tcn.material, eiel_t_abast_tcn.estado,
    eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor;
	
ALTER TABLE v_tramo_conduccion_m50 OWNER TO geopista;






DROP VIEW v_tramo_emisario_m50;

CREATE VIEW "public"."v_tramo_emisario_m50" (
    clave,
    provincia,
    municipio,
    orden_emis,
    tipo_mat,
    estado,
    long_terre,
    long_marit)
AS
SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia,
    eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS
    orden_emis, eiel_t_saneam_tem.material AS tipo_mat,
    eiel_t_saneam_tem.estado, sum(eiel_t_saneam_tem.long_terre) AS long_terre,
    sum(eiel_t_saneam_tem.long_marit) AS long_marit
FROM (eiel_t_saneam_tem LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t_saneam_tem.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t_saneam_tem.codmunic)::text))))
WHERE (((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
	
    (((eiel_t_saneam_tem.codmunic)::text || (eiel_t_saneam_tem.tramo_em)::text) IN (
    SELECT DISTINCT ((v_emisario_nucleo.em_municip)::text || (v_emisario_nucleo.orden_emis)::text)
    FROM v_emisario_nucleo)) AND


    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric)) AND
    (eiel_t_saneam_tem.revision_expirada = (9999999999::bigint)::numeric))
GROUP BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em,
    eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov,
    eiel_t_saneam_tem.material, eiel_t_saneam_tem.estado
ORDER BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_tramo_emisario_m50 OWNER TO geopista;

-- se actualiza pra qe cuando un dato n LG sea flotante, se pase al dxf como double
update columns set "Scale"=1 where "Type"=2 and "Precision">0 and "Length">0;
update columns set "Type"=3, "Length"=6  where id_table=(select id_table from tables where name = 'eiel_c_redviaria_tu') and name='ttggss';
