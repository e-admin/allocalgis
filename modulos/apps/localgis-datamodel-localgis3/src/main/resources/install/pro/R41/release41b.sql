CREATE OR REPLACE VIEW v_deposito_enc_sin_nucleo AS 
 SELECT eiel_t_abast_de.clave, eiel_t_abast_de.codprov AS provincia, eiel_t_abast_de.codmunic AS municipio, eiel_t_abast_de.orden_de AS orden_depo, eiel_t_abast_de.ubicacion, eiel_t_abast_de.titular, eiel_t_abast_de.gestor AS gestion, eiel_t_abast_de.capacidad, eiel_t_abast_de.estado, eiel_t_abast_de.proteccion, eiel_t_abast_de.fecha_limpieza AS limpieza, eiel_t_abast_de.contador
   FROM eiel_t_abast_de
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_de.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_de.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_de.codmunic::text || eiel_t_abast_de.orden_de::text NOT IN ( SELECT DISTINCT v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text
      FROM v_deposito_agua_nucleo
     ORDER BY v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_de.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_deposito_enc_sin_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_deposito_enc_sin_nucleo TO geopista;
GRANT SELECT ON TABLE v_deposito_enc_sin_nucleo TO consultas;


CREATE OR REPLACE VIEW v_captacion_enc_sin_nucleo AS 
 SELECT eiel_t_abast_ca.clave, eiel_t_abast_ca.codprov AS provincia, eiel_t_abast_ca.codmunic AS municipio, eiel_t_abast_ca.orden_ca AS orden_capt, eiel_t_abast_ca.nombre AS denominaci, eiel_t_abast_ca.tipo AS tipo_capt, eiel_t_abast_ca.titular, eiel_t_abast_ca.gestor AS gestion, eiel_t_abast_ca.sist_impulsion AS sistema_ca, eiel_t_abast_ca.estado, eiel_t_abast_ca.uso, eiel_t_abast_ca.proteccion, eiel_t_abast_ca.contador
   FROM eiel_t_abast_ca
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_ca.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_ca.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_ca.codmunic::text || eiel_t_abast_ca.orden_ca::text NOT IN ( SELECT DISTINCT v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text
      FROM v_cap_agua_nucleo
     ORDER BY v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text)) AND eiel_t_abast_ca.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_captacion_enc_sin_nucleo  OWNER TO geopista;
GRANT ALL ON TABLE v_captacion_enc_sin_nucleo TO geopista;
GRANT SELECT ON TABLE v_captacion_enc_sin_nucleo TO consultas;


CREATE OR REPLACE VIEW v_conduccion_enc_sin_nucleo AS 
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
   FROM eiel_t_abast_tcn
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tcn.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text NOT IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text
      FROM v_cond_agua_nucleo
     ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_conduccion_enc_sin_nucleo  OWNER TO geopista;
GRANT ALL ON TABLE v_conduccion_enc_sin_nucleo TO geopista;
GRANT SELECT ON TABLE v_conduccion_enc_sin_nucleo TO consultas;


CREATE OR REPLACE VIEW v_potabilizacion_enc_sin_nucleo AS 
 SELECT eiel_t_abast_tp.clave, eiel_t_abast_tp.codprov AS provincia, eiel_t_abast_tp.codmunic AS municipio, eiel_t_abast_tp.orden_tp AS orden_trat, eiel_t_abast_tp.tipo AS tipo_tra, eiel_t_abast_tp.ubicacion, eiel_t_abast_tp.s_desinf, eiel_t_abast_tp.categoria_a1 AS cat_a1, eiel_t_abast_tp.categoria_a2 AS cat_a2, eiel_t_abast_tp.categoria_a3 AS cat_a3, eiel_t_abast_tp.desaladora, eiel_t_abast_tp.otros, eiel_t_abast_tp.desinf_1, eiel_t_abast_tp.desinf_2, eiel_t_abast_tp.desinf_3, eiel_t_abast_tp.periodicidad AS periodicid, eiel_t_abast_tp.organismo_control AS organismo, eiel_t_abast_tp.estado
   FROM eiel_t_abast_tp
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tp.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tp.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_tp.codmunic::text || eiel_t_abast_tp.orden_tp::text NOT IN ( SELECT DISTINCT v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text
      FROM v_trat_pota_nucleo
     ORDER BY v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text)) AND eiel_t_abast_tp.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_potabilizacion_enc_sin_nucleo OWNER TO geopista;
GRANT ALL ON TABLE v_potabilizacion_enc_sin_nucleo TO geopista;
GRANT SELECT ON TABLE v_potabilizacion_enc_sin_nucleo TO consultas;


CREATE OR REPLACE VIEW v_colector_enc_sin_nucleo AS 
 SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia, eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
   FROM eiel_t_saneam_tcl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tcl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text NOT IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text
      FROM v_colector_nucleo
     ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_colector_enc_sin_nucleo  OWNER TO geopista;
GRANT ALL ON TABLE v_colector_enc_sin_nucleo TO geopista;
GRANT SELECT ON TABLE v_colector_enc_sin_nucleo TO consultas;


CREATE OR REPLACE VIEW v_depuradora_enc_sin_nucleo AS 
 SELECT eiel_t1_saneam_ed.clave, eiel_t1_saneam_ed.codprov AS provincia, eiel_t1_saneam_ed.codmunic AS municipio, eiel_t1_saneam_ed.orden_ed AS orden_depu, eiel_t1_saneam_ed.trat_pr_1, eiel_t1_saneam_ed.trat_pr_2, eiel_t1_saneam_ed.trat_pr_3, eiel_t1_saneam_ed.trat_sc_1, eiel_t1_saneam_ed.trat_sc_2, eiel_t1_saneam_ed.trat_sc_3, eiel_t1_saneam_ed.trat_av_1, eiel_t1_saneam_ed.trat_av_2, eiel_t1_saneam_ed.trat_av_3, eiel_t1_saneam_ed.proc_cm_1, eiel_t1_saneam_ed.proc_cm_2, eiel_t1_saneam_ed.proc_cm_3, eiel_t1_saneam_ed.trat_ld_1, eiel_t1_saneam_ed.trat_ld_2, eiel_t1_saneam_ed.trat_ld_3
   FROM eiel_t1_saneam_ed
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t1_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t1_saneam_ed.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t1_saneam_ed.codmunic::text || eiel_t1_saneam_ed.orden_ed::text NOT IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text
      FROM v_dep_agua_nucleo
     ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_t1_saneam_ed.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_depuradora_enc_sin_nucleo  OWNER TO geopista;
GRANT ALL ON TABLE v_depuradora_enc_sin_nucleo TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc_sin_nucleo TO consultas;

CREATE OR REPLACE VIEW v_depuradora_enc_2_sin_nucleo AS 
 SELECT eiel_t2_saneam_ed.clave, eiel_t2_saneam_ed.codprov AS provincia, eiel_t2_saneam_ed.codmunic AS municipio, eiel_t2_saneam_ed.orden_ed AS orden_depu, eiel_t2_saneam_ed.titular, eiel_t2_saneam_ed.gestor AS gestion, eiel_t2_saneam_ed.capacidad, eiel_t2_saneam_ed.problem_1, eiel_t2_saneam_ed.problem_2, eiel_t2_saneam_ed.problem_3, eiel_t2_saneam_ed.lodo_gest, eiel_t2_saneam_ed.lodo_vert, eiel_t2_saneam_ed.lodo_inci, eiel_t2_saneam_ed.lodo_con_agri AS lodo_con_a, eiel_t2_saneam_ed.lodo_sin_agri AS lodo_sin_a, eiel_t2_saneam_ed.lodo_ot
   FROM eiel_t2_saneam_ed
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t2_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t2_saneam_ed.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t2_saneam_ed.codmunic::text || eiel_t2_saneam_ed.orden_ed::text NOT IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text
      FROM v_dep_agua_nucleo
     ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t2_saneam_ed.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_depuradora_enc_2_sin_nucleo  OWNER TO geopista;
GRANT ALL ON TABLE v_depuradora_enc_2_sin_nucleo TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc_2_sin_nucleo TO consultas;

CREATE OR REPLACE VIEW v_emisario_enc_sin_nucleo AS 
 SELECT DISTINCT ON (eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em) eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_pv.tipo AS tipo_vert, eiel_t_saneam_pv.zona AS zona_vert, sum(eiel_t_saneam_pv.distancia_nucleo) AS distancia
   FROM eiel_t_saneam_tem
   LEFT JOIN eiel_t_saneam_pv ON eiel_t_saneam_pv.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_saneam_pv.codmunic::text = eiel_t_saneam_tem.codmunic::text AND eiel_t_saneam_pv.revision_expirada = 9999999999::bigint::numeric
   JOIN eiel_tr_saneam_tem_pv tempv ON tempv.clave_tem::text = eiel_t_saneam_tem.clave::text AND tempv.codprov_tem::text = eiel_t_saneam_tem.codprov::text AND tempv.codmunic_tem::text = eiel_t_saneam_tem.codmunic::text AND tempv.tramo_em::text = eiel_t_saneam_tem.tramo_em::text AND tempv.revision_expirada = 9999999999::bigint::numeric AND tempv.codprov_pv::text = eiel_t_saneam_pv.codprov::text AND tempv.codmunic_pv::text = eiel_t_saneam_pv.codmunic::text AND tempv.clave_pv::text = eiel_t_saneam_pv.clave::text AND tempv.orden_pv::text = eiel_t_saneam_pv.orden_pv::text
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text NOT IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
   FROM v_emisario_nucleo
  ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_pv.tipo, eiel_t_saneam_pv.zona
  ORDER BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_emisario_enc_sin_nucleo  OWNER TO geopista;
GRANT ALL ON TABLE v_emisario_enc_sin_nucleo TO geopista;
GRANT SELECT ON TABLE v_emisario_enc_sin_nucleo TO consultas;