
 
CREATE OR REPLACE VIEW v_PROVINCIA AS 
  SELECT 
 --fase AS FASE,
 eiel_c_provincia.codprov AS PROVINCIA,
 eiel_c_provincia.nombre AS DENOMINACI
  FROM eiel_c_provincia
  WHERE eiel_c_provincia.revision_expirada=9999999999;
 		ALTER TABLE v_PROVINCIA OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_MUNICIPIO AS 
 SELECT 
 --fase AS FASE,
 eiel_c_municipios.codprov AS PROVINCIA,
 eiel_c_municipios.codmunic AS MUNICIPIO,
 eiel_c_municipios.nombre_oficial AS DENOMINACI
  FROM eiel_c_municipios
   WHERE eiel_c_municipios.revision_expirada=9999999999;
 ALTER TABLE v_MUNICIPIO OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_NUCLEO_POBLACION AS 
 SELECT 
 --fase AS FASE,
 eiel_c_nucleo_poblacion.codprov AS PROVINCIA, eiel_c_nucleo_poblacion.codmunic AS MUNICIPIO, eiel_c_nucleo_poblacion.codentidad AS ENTIDAD ,
 eiel_c_nucleo_poblacion.codpoblamiento AS POBLAMIENT,
 eiel_c_nucleo_poblacion.nombre_oficial AS DENOMINACI
 FROM eiel_c_nucleo_poblacion
   WHERE eiel_c_nucleo_poblacion.revision_expirada=9999999999;
 ALTER TABLE v_NUCLEO_POBLACION OWNER TO geopista;
 
--ABASTECIMIENTO C
    
    
CREATE OR REPLACE VIEW v_CAPTACION_AGUA AS 
 SELECT 
 --fase AS FASE,
 eiel_t_abast_ca.clave AS CLAVE,eiel_t_abast_ca.codprov AS PROVINCIA, eiel_t_abast_ca.codmunic AS MUNICIPIO, eiel_t_abast_ca.orden_ca AS ORDEN_CAPT
  FROM eiel_t_abast_ca
  WHERE eiel_t_abast_ca.revision_expirada=9999999999;

 ALTER TABLE v_CAPTACION_AGUA OWNER TO geopista;
 
 
 CREATE OR REPLACE VIEW v_DEPOSITO AS 
 SELECT 
 --fase AS FASE
 eiel_t_abast_de.clave AS CLAVE,
 eiel_t_abast_de.codprov AS PROVINCIA,
 eiel_t_abast_de.codmunic AS MUNICIPIO,
 eiel_t_abast_de.orden_de AS ORDEN_DEPO
  FROM eiel_t_abast_de
 WHERE eiel_t_abast_de.revision_expirada=9999999999;

 ALTER TABLE v_DEPOSITO OWNER TO geopista;

CREATE OR REPLACE VIEW v_RED_DISTRIBUCION AS 
 SELECT 
 		--fase AS FASE,
 		eiel_c_abast_rd.codprov AS PROVINCIA, eiel_c_abast_rd.codmunic AS MUNICIPIO, eiel_c_abast_rd.codentidad AS ENTIDAD,
 		eiel_c_abast_rd.codpoblamiento AS NUCLEO, eiel_c_abast_rd.material AS TIPO_RDIS, eiel_c_abast_rd.sist_trans AS SIST_TRANS, eiel_c_abast_rd.estado AS ESTADO ,
 		eiel_c_abast_rd.titular AS TITULAR, eiel_c_abast_rd.gestor AS GESTION ,sum(eiel_c_abast_rd.longitud) AS LONGITUD
 		FROM eiel_c_abast_rd
 		  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_c_abast_rd.codprov AND eiel_t_padron_ttmm.codmunic=eiel_c_abast_rd.codmunic 
	 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_c_abast_rd.revision_expirada=9999999999
   GROUP BY provincia,municipio,entidad,nucleo,tipo_rdis,sist_trans,estado,titular,gestion;

 ALTER TABLE v_RED_DISTRIBUCION OWNER TO geopista;


CREATE OR REPLACE VIEW v_CONDUCCION AS 
 SELECT 
 --fase AS FASE,
 eiel_t_abast_tcn.clave AS CLAVE,eiel_t_abast_tcn.codprov AS PROVINCIA, eiel_t_abast_tcn.codmunic AS MUNICIPIO, eiel_t_abast_tcn.tramo_cn AS ORDEN_COND
  FROM eiel_t_abast_tcn
  WHERE eiel_t_abast_tcn.revision_expirada=9999999999;
 ALTER TABLE v_CONDUCCION OWNER TO geopista;

 CREATE OR REPLACE VIEW v_CONDUCCION_ENC AS 
 SELECT 
 --fase AS FASE,
 eiel_t_abast_tcn.clave AS CLAVE,eiel_t_abast_tcn.codprov AS PROVINCIA, eiel_t_abast_tcn.codmunic AS MUNICIPIO, eiel_t_abast_tcn.tramo_cn AS ORDEN_COND
 FROM eiel_t_abast_tcn
 left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_tcn.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_tcn.codmunic 
 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_abast_tcn.revision_expirada=9999999999;
 ALTER TABLE v_CONDUCCION_ENC OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_CONDUCCION_ENC_M50 AS 
SELECT 
 --fase AS FASE,
	eiel_t_abast_tcn.clave AS CLAVE,eiel_t_abast_tcn.codprov AS PROVINCIA, eiel_t_abast_tcn.codmunic AS MUNICIPIO, eiel_t_abast_tcn.tramo_cn AS ORDEN_COND 
FROM eiel_t_abast_tcn
left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_tcn.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_tcn.codmunic 
WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_abast_tcn.revision_expirada=9999999999;
ALTER TABLE v_CONDUCCION_ENC_M50 OWNER TO geopista;

CREATE OR REPLACE VIEW v_TRAMO_CONDUCCION AS 
SELECT 
 		--fase AS FASE,
 		eiel_t_abast_tcn.clave AS CLAVE,eiel_t_abast_tcn.codprov AS PROVINCIA, eiel_t_abast_tcn.codmunic AS MUNICIPIO, eiel_t_abast_tcn.tramo_cn AS ORDEN_COND,
 		eiel_t_abast_tcn.material AS TIPO_TCOND, eiel_t_abast_tcn.estado AS ESTADO ,
 		eiel_t_abast_tcn.titular AS TITULAR, eiel_t_abast_tcn.gestor AS GESTION ,sum(eiel_c_abast_tcn.longitud) AS LONGITUD
 		FROM eiel_t_abast_tcn
 		left join eiel_c_abast_tcn  on eiel_c_abast_tcn.clave=eiel_t_abast_tcn.clave  AND eiel_c_abast_tcn.codprov=eiel_t_abast_tcn.codprov AND eiel_c_abast_tcn.codmunic=eiel_t_abast_tcn.codmunic AND eiel_c_abast_tcn.tramo_cn=eiel_t_abast_tcn.tramo_cn AND eiel_c_abast_tcn.revision_expirada=9999999999
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_tcn.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_tcn.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_abast_tcn.revision_expirada=9999999999
 		GROUP BY eiel_t_abast_tcn.clave,PROVINCIA,MUNICIPIO,ORDEN_COND,TIPO_TCOND,eiel_t_abast_tcn.estado,eiel_t_abast_tcn.titular,eiel_t_abast_tcn.gestor;
 ALTER TABLE v_TRAMO_CONDUCCION OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_TRAMO_CONDUCCION_M50 AS 
SELECT 
 		--fase AS FASE,
 		eiel_t_abast_tcn.clave AS CLAVE,eiel_t_abast_tcn.codprov AS PROVINCIA, eiel_t_abast_tcn.codmunic AS MUNICIPIO, eiel_t_abast_tcn.tramo_cn AS ORDEN_COND,
 		eiel_t_abast_tcn.material AS TIPO_TCOND, eiel_t_abast_tcn.estado AS ESTADO ,
 		eiel_t_abast_tcn.titular AS TITULAR, eiel_t_abast_tcn.gestor AS GESTION ,sum(eiel_c_abast_tcn.longitud) AS LONGITUD
 		FROM eiel_t_abast_tcn
 		left join eiel_c_abast_tcn  on eiel_c_abast_tcn.clave=eiel_t_abast_tcn.clave  AND eiel_c_abast_tcn.codprov=eiel_t_abast_tcn.codprov AND eiel_c_abast_tcn.codmunic=eiel_t_abast_tcn.codmunic AND eiel_c_abast_tcn.tramo_cn=eiel_t_abast_tcn.tramo_cn AND eiel_c_abast_tcn.revision_expirada=9999999999
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_tcn.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_tcn.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_abast_tcn.revision_expirada=9999999999
 		GROUP BY eiel_t_abast_tcn.clave,PROVINCIA,MUNICIPIO,ORDEN_COND,TIPO_TCOND,eiel_t_abast_tcn.estado,eiel_t_abast_tcn.titular,eiel_t_abast_tcn.gestor;
 ALTER TABLE v_TRAMO_CONDUCCION_M50 OWNER TO geopista;
 
---ABASTECIMIENTO T

CREATE OR REPLACE VIEW v_NUCL_ENCUESTADO_4 AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_abast_au.codprov AS PROVINCIA, eiel_t_abast_au.codmunic AS MUNICIPIO,eiel_t_abast_au.codentidad AS ENTIDAD,eiel_t_abast_au.codpoblamiento AS NUCLEO,
 		eiel_t_abast_au.aau_vivien AS AAU_VIVIEN,eiel_t_abast_au.aau_pob_re AS AAU_POB_RE,eiel_t_abast_au.aau_pob_es AS AAU_POB_ES,
 		eiel_t_abast_au.aau_def_vi AS AAU_DEF_VI,eiel_t_abast_au.aau_def_re AS AAU_DEF_RE ,eiel_t_abast_au.aau_def_es AS AAU_DEF_ES,
 		eiel_t_abast_au.aau_fecont AS AAU_FECONT,eiel_t_abast_au.aau_fencon AS AAU_FENCON, eiel_t_abast_au.aau_caudal AS AAU_CAUDAL
 		FROM eiel_t_abast_au
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_au.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_au.codmunic 
  		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_abast_au.revision_expirada=9999999999;
 ALTER TABLE v_NUCL_ENCUESTADO_4 OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_CAPTACION_ENC AS 
 SELECT eiel_t_abast_ca.clave AS CLAVE,eiel_t_abast_ca.codprov AS PROVINCIA, eiel_t_abast_ca.codmunic AS MUNICIPIO, eiel_t_abast_ca.orden_ca AS ORDEN_CAPT,
 		eiel_t_abast_ca.nombre AS DENOMINACI, eiel_t_abast_ca.tipo AS TIPO_CAPT, eiel_t_abast_ca.titular AS TITULAR, eiel_t_abast_ca.gestor AS GESTION ,eiel_t_abast_ca.sist_impulsion AS SISTEMA_CA,
 		eiel_t_abast_ca.estado AS ESTADO, eiel_t_abast_ca.uso AS USO ,eiel_t_abast_ca.proteccion AS PROTECCION,eiel_t_abast_ca.contador AS CONTADOR
 		FROM eiel_t_abast_ca
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_ca.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_ca.codmunic WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000
 		AND eiel_t_abast_ca.revision_expirada=9999999999 AND eiel_t_padron_ttmm.revision_expirada=9999999999;

 ALTER TABLE v_CAPTACION_ENC OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_CAPTACION_ENC_M50 AS 
SELECT 
	--fase AS FASE,
	 eiel_t_abast_ca.clave AS CLAVE,eiel_t_abast_ca.codprov AS PROVINCIA, eiel_t_abast_ca.codmunic AS MUNICIPIO, eiel_t_abast_ca.orden_ca AS ORDEN_CAPT,
 		eiel_t_abast_ca.nombre AS DENOMINACI, eiel_t_abast_ca.tipo AS TIPO_CAPT, eiel_t_abast_ca.titular AS TITULAR, eiel_t_abast_ca.gestor AS GESTION ,eiel_t_abast_ca.sist_impulsion AS SISTEMA_CA,
 		eiel_t_abast_ca.estado AS ESTADO, eiel_t_abast_ca.uso AS USO ,eiel_t_abast_ca.proteccion AS PROTECCION,eiel_t_abast_ca.contador AS CONTADOR
 		FROM eiel_t_abast_ca
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_ca.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_ca.codmunic WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000
 		AND eiel_t_abast_ca.revision_expirada=9999999999 AND eiel_t_padron_ttmm.revision_expirada=9999999999;

ALTER TABLE v_CAPTACION_ENC_M50 OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_DEPOSITO_ENC AS 
 SELECT 
 --fase AS FASE,
 eiel_t_abast_de.clave AS CLAVE,
 eiel_t_abast_de.codprov AS PROVINCIA, 
 eiel_t_abast_de.codmunic AS MUNICIPIO,
 eiel_t_abast_de.orden_de AS ORDEN_DEPO,
 		eiel_t_abast_de.ubicacion AS UBICACION,
 		eiel_t_abast_de.titular AS TITULAR,
 		eiel_t_abast_de.gestor AS GESTION ,
 		eiel_t_abast_de.capacidad AS CAPACIDAD,
 		eiel_t_abast_de.estado AS ESTADO,
 		eiel_t_abast_de.proteccion AS PROTECCION ,
 		eiel_t_abast_de.fecha_limpieza AS LIMPIEZA,
 		eiel_t_abast_de.contador AS CONTADOR
 		FROM eiel_t_abast_de	 
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_de.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_de.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_abast_de.revision_expirada=9999999999 AND eiel_t_padron_ttmm.revision_expirada=9999999999;
 		
 ALTER TABLE v_DEPOSITO_ENC OWNER TO geopista;

CREATE OR REPLACE VIEW v_DEPOSITO_ENC_M50 AS 
 SELECT 
 --fase AS FASE,
 eiel_t_abast_de.clave AS CLAVE,eiel_t_abast_de.codprov AS PROVINCIA, eiel_t_abast_de.codmunic AS MUNICIPIO, eiel_t_abast_de.orden_de AS ORDEN_DEPO,
 		eiel_t_abast_de.ubicacion AS UBICACION, eiel_t_abast_de.titular AS TITULAR, eiel_t_abast_de.gestor AS GESTION ,eiel_t_abast_de.capacidad AS CAPACIDAD,
 		eiel_t_abast_de.estado AS ESTADO, eiel_t_abast_de.proteccion AS PROTECCION ,eiel_t_abast_de.fecha_limpieza AS LIMPIEZA,eiel_t_abast_de.contador AS CONTADOR
 		FROM eiel_t_abast_de
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_de.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_de.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000 AND eiel_t_abast_de.revision_expirada=9999999999 AND eiel_t_padron_ttmm.revision_expirada=9999999999;

ALTER TABLE v_DEPOSITO_ENC_M50 OWNER TO geopista;


CREATE OR REPLACE VIEW v_NUCL_ENCUESTADO_3 AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_abast_serv.codprov AS PROVINCIA, eiel_t_abast_serv.codmunic AS MUNICIPIO, eiel_t_abast_serv.codentidad AS ENTIDAD,
 		eiel_t_abast_serv.codpoblamiento AS NUCLEO,eiel_t_abast_serv.viviendas_c_conex AS AAG_V_CONE,eiel_t_abast_serv.viviendas_s_conexion AS AAG_V_NCON,
 		eiel_t_abast_serv.consumo_inv AS AAG_C_INVI,eiel_t_abast_serv.consumo_verano AS AAG_C_VERA,eiel_t_abast_serv.viv_exceso_pres AS AAG_V_EXPR ,
 		eiel_t_abast_serv.viv_defic_presion AS AAG_V_DEPR,eiel_t_abast_serv.perdidas_agua AS AAG_PERDID,
 		eiel_t_abast_serv.calidad_serv AS AAG_CALIDA, eiel_t_abast_serv.long_deficit AS AAG_L_DEFI,eiel_t_abast_serv.viv_deficitarias AS AAG_V_DEFI,
 		eiel_t_abast_serv.pobl_res_afect AS AAG_PR_DEF, eiel_t_abast_serv.pobl_est_afect AS AAG_PE_DEF
 		FROM eiel_t_abast_serv
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_serv.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_serv.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_abast_serv.revision_expirada=9999999999;		  
 ALTER TABLE v_NUCL_ENCUESTADO_3 OWNER TO geopista;

 
CREATE OR REPLACE VIEW v_TRA_POTABILIZACION AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_abast_tp.clave AS CLAVE, eiel_t_abast_tp.codprov AS PROVINCIA, eiel_t_abast_tp.codmunic AS MUNICIPIO, 
 		eiel_t_abast_tp.orden_tp AS ORDEN_TRAT
 		FROM eiel_t_abast_tp
 		WHERE eiel_t_abast_tp.revision_expirada=9999999999
 		ORDER BY municipio,orden_trat ASC;
		  
 ALTER TABLE v_TRA_POTABILIZACION OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_POTABILIZACION_ENC AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_abast_tp.clave AS CLAVE, eiel_t_abast_tp.codprov AS PROVINCIA, eiel_t_abast_tp.codmunic AS MUNICIPIO, 
 		eiel_t_abast_tp.orden_tp AS ORDEN_TRAT,
 		eiel_t_abast_tp.tipo AS TIPO_TRA,eiel_t_abast_tp.ubicacion AS UBICACION,eiel_t_abast_tp.s_desinf AS S_DESINF,eiel_t_abast_tp.categoria_a1 AS CAT_A1,
 		eiel_t_abast_tp.categoria_a2 AS CAT_A2, eiel_t_abast_tp.categoria_a3 AS CAT_A3,eiel_t_abast_tp.desaladora AS DESALADORA,
 		eiel_t_abast_tp.otros AS OTROS,eiel_t_abast_tp.desinf_1 AS DESINF_1,eiel_t_abast_tp.desinf_2 AS DESINF_2,
 		eiel_t_abast_tp.desinf_3 AS DESINF_3,eiel_t_abast_tp.periodicidad AS PERIODICID,eiel_t_abast_tp.organismo_control AS ORGANISMO,
 		eiel_t_abast_tp.estado AS ESTADO
 		FROM eiel_t_abast_tp
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_tp.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_tp.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000
 		AND eiel_t_abast_tp.revision_expirada=9999999999 AND eiel_t_padron_ttmm.revision_expirada=9999999999;
	  
 ALTER TABLE v_POTABILIZACION_ENC OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_POTABILIZACION_ENC_M50 AS 
 SELECT 
--fase AS FASE,
 	 		eiel_t_abast_tp.clave AS CLAVE, eiel_t_abast_tp.codprov AS PROVINCIA, eiel_t_abast_tp.codmunic AS MUNICIPIO, 
 		eiel_t_abast_tp.orden_tp AS ORDEN_TRAT,
 		eiel_t_abast_tp.tipo AS TIPO_TRA,eiel_t_abast_tp.ubicacion AS UBICACION,eiel_t_abast_tp.s_desinf AS S_DESINF,eiel_t_abast_tp.categoria_a1 AS CAT_A1,
 		eiel_t_abast_tp.categoria_a2 AS CAT_A2, eiel_t_abast_tp.categoria_a3 AS CAT_A3,eiel_t_abast_tp.desaladora AS DESALADORA,
 		eiel_t_abast_tp.otros AS OTROS,eiel_t_abast_tp.desinf_1 AS DESINF_1,eiel_t_abast_tp.desinf_2 AS DESINF_2,
 		eiel_t_abast_tp.desinf_3 AS DESINF_3,eiel_t_abast_tp.periodicidad AS PERIODICID,eiel_t_abast_tp.organismo_control AS ORGANISMO,
 		eiel_t_abast_tp.estado AS ESTADO
 FROM eiel_t_abast_tp
 left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_tp.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_tp.codmunic WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000
 AND eiel_t_abast_tp.revision_expirada=9999999999 AND eiel_t_padron_ttmm.revision_expirada=9999999999;

ALTER TABLE v_POTABILIZACION_ENC_M50 OWNER TO geopista;

---ABASTECIMIENTO TR

CREATE OR REPLACE VIEW v_CAP_AGUA_NUCLEO AS 
  SELECT
  --fase AS FASE,
 eiel_tr_abast_ca_pobl.codprov_pobl AS PROVINCIA, eiel_tr_abast_ca_pobl.codmunic_pobl AS MUNICIPIO, eiel_tr_abast_ca_pobl.codentidad_pobl AS ENTIDAD,eiel_tr_abast_ca_pobl.codpoblamiento_pobl AS NUCLEO,
 		eiel_tr_abast_ca_pobl.clave_ca AS CLAVE, eiel_tr_abast_ca_pobl.codprov_ca AS C_PROVINC, eiel_tr_abast_ca_pobl.codmunic_ca AS C_MUNICIP,
 		eiel_tr_abast_ca_pobl.orden_ca AS ORDEN_CAPT
  FROM eiel_tr_abast_ca_pobl
  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_tr_abast_ca_pobl.codprov_pobl AND eiel_t_padron_ttmm.codmunic=eiel_tr_abast_ca_pobl.codmunic_pobl 
  WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_tr_abast_ca_pobl.revision_expirada=9999999999;
  
 ALTER TABLE v_CAP_AGUA_NUCLEO OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_DEPOSITO_AGUA_NUCLEO AS 
  SELECT
  --fase AS FASE,
 eiel_tr_abast_de_pobl.codprov_pobl AS PROVINCIA, 
 eiel_tr_abast_de_pobl.codmunic_pobl AS MUNICIPIO, 
 eiel_tr_abast_de_pobl.codentidad_pobl AS ENTIDAD,
 eiel_tr_abast_de_pobl.codpoblamiento_pobl AS NUCLEO,
 		eiel_tr_abast_de_pobl.clave_de AS CLAVE, 
 		eiel_tr_abast_de_pobl.codprov_de AS DE_PROVINC,
 		eiel_tr_abast_de_pobl.codmunic_de AS DE_MUNICIP,
 		eiel_tr_abast_de_pobl.orden_de AS ORDEN_DEPO
  FROM eiel_tr_abast_de_pobl
   left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_tr_abast_de_pobl.codprov_pobl AND eiel_t_padron_ttmm.codmunic=eiel_tr_abast_de_pobl.codmunic_pobl 
  WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_tr_abast_de_pobl.revision_expirada=9999999999;
  
 ALTER TABLE v_DEPOSITO_AGUA_NUCLEO OWNER TO geopista;
 


CREATE OR REPLACE VIEW v_TRAT_POTA_NUCLEO AS 
  SELECT
  --fase AS FASE,
 eiel_tr_abast_tp_pobl.codprov_pobl AS PROVINCIA, eiel_tr_abast_tp_pobl.codmunic_pobl AS MUNICIPIO, eiel_tr_abast_tp_pobl.codentidad_pobl AS ENTIDAD,eiel_tr_abast_tp_pobl.codpoblamiento_pobl AS NUCLEO,
 		eiel_tr_abast_tp_pobl.clave_tp AS CLAVE, eiel_tr_abast_tp_pobl.codprov_tp AS PO_PROVIN, eiel_tr_abast_tp_pobl.codmunic_tp AS PO_MUNIPI,
 		eiel_tr_abast_tp_pobl.orden_tp AS ORDEN_TRAT
  FROM eiel_tr_abast_tp_pobl
  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_tr_abast_tp_pobl.codprov_pobl AND eiel_t_padron_ttmm.codmunic=eiel_tr_abast_tp_pobl.codmunic_pobl 
  WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_tr_abast_tp_pobl.revision_expirada=9999999999;
  
 ALTER TABLE v_TRAT_POTA_NUCLEO OWNER TO geopista;



CREATE OR REPLACE VIEW v_COND_AGUA_NUCLEO AS 
  SELECT
  --fase AS FASE,
 eiel_tr_abast_tcn_pobl.codprov_pobl AS PROVINCIA,
 eiel_tr_abast_tcn_pobl.codmunic_pobl AS MUNICIPIO,
 eiel_tr_abast_tcn_pobl.codentidad_pobl AS ENTIDAD,
 eiel_tr_abast_tcn_pobl.codpoblamiento_pobl AS NUCLEO,
 		eiel_tr_abast_tcn_pobl.clave_tcn AS CLAVE,
 		eiel_tr_abast_tcn_pobl.codprov_tcn AS COND_PROVI,
 		eiel_tr_abast_tcn_pobl.codmunic_tcn AS COND_MUNIC,
 		eiel_tr_abast_tcn_pobl.tramo_tcn AS ORDEN_COND
  FROM eiel_tr_abast_tcn_pobl
  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_tr_abast_tcn_pobl.codprov_pobl AND eiel_t_padron_ttmm.codmunic=eiel_tr_abast_tcn_pobl.codmunic_pobl 
  WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_tr_abast_tcn_pobl.revision_expirada=9999999999;
  
 ALTER TABLE v_COND_AGUA_NUCLEO OWNER TO geopista;
 



-----SANEAMIENTO C-----------

CREATE OR REPLACE VIEW v_DEPURADORA AS 
 SELECT 
 --fase AS FASE,
 eiel_t1_saneam_ed.clave AS CLAVE,eiel_t1_saneam_ed.codprov AS PROVINCIA, eiel_t1_saneam_ed.codmunic AS MUNICIPIO, eiel_t1_saneam_ed.orden_ed AS ORDEN_DEPU
  FROM eiel_t1_saneam_ed
  left join eiel_t2_saneam_ed on eiel_t2_saneam_ed.codprov=eiel_t1_saneam_ed.codprov AND eiel_t2_saneam_ed.codmunic=eiel_t1_saneam_ed.codmunic AND  eiel_t1_saneam_ed.clave= eiel_t2_saneam_ed.clave AND eiel_t1_saneam_ed.orden_ed=eiel_t2_saneam_ed.orden_ed
  WHERE eiel_t1_saneam_ed.revision_expirada=9999999999 AND eiel_t2_saneam_ed.revision_expirada=9999999999;
  
 ALTER TABLE v_DEPURADORA OWNER TO geopista;


CREATE OR REPLACE VIEW v_EMISARIO AS 
 SELECT 
 --fase AS FASE,
 eiel_t_saneam_tem.clave AS CLAVE,eiel_t_saneam_tem.codprov AS PROVINCIA, eiel_t_saneam_tem.codmunic AS MUNICIPIO, eiel_t_saneam_tem.tramo_em AS ORDEN_EMIS
  FROM eiel_t_saneam_tem
  WHERE eiel_t_saneam_tem.revision_expirada=9999999999;
  
 ALTER TABLE v_EMISARIO OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_EMISARIO_ENC AS 
 SELECT DISTINCT ON( eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em) eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_pv.tipo AS tipo_vert, eiel_t_saneam_pv.zona AS zona_vert, sum(eiel_t_saneam_pv.distancia_nucleo) AS distancia
  FROM eiel_t_saneam_tem 
  LEFT JOIN eiel_t_saneam_pv ON eiel_t_saneam_pv.codprov=eiel_t_saneam_tem.codprov AND eiel_t_saneam_pv.codmunic=eiel_t_saneam_tem.codmunic AND eiel_t_saneam_pv.revision_expirada = 9999999999::bigint::numeric 
  INNER JOIN eiel_tr_saneam_tem_pv tempv ON tempv.clave_tem = eiel_t_saneam_tem.clave AND tempv.codprov_tem= eiel_t_saneam_tem.codprov::text 
		AND tempv.codmunic_tem::text = eiel_t_saneam_tem.codmunic::text AND tempv.tramo_em::text = eiel_t_saneam_tem.tramo_em::text AND tempv.revision_expirada = 9999999999::bigint::numeric
		AND tempv.codprov_pv=eiel_t_saneam_pv.codprov AND tempv.codmunic_pv=eiel_t_saneam_pv.codmunic AND tempv.clave_pv = eiel_t_saneam_pv.clave
		AND tempv.orden_pv=eiel_t_saneam_pv.orden_pv 		
  LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_pv.tipo, eiel_t_saneam_pv.zona
  ORDER BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov,eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_emisario_enc OWNER TO geopista;
GRANT ALL ON TABLE v_emisario_enc TO geopista;
GRANT SELECT ON TABLE v_emisario_enc TO consultas;
 
 
CREATE OR REPLACE VIEW v_EMISARIO_ENC_M50 AS 
 SELECT DISTINCT ON( eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em) eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_pv.tipo AS tipo_vert, eiel_t_saneam_pv.zona AS zona_vert, sum(eiel_t_saneam_pv.distancia_nucleo) AS distancia
  FROM eiel_t_saneam_tem 
  LEFT JOIN eiel_t_saneam_pv ON eiel_t_saneam_pv.codprov=eiel_t_saneam_tem.codprov AND eiel_t_saneam_pv.codmunic=eiel_t_saneam_tem.codmunic AND eiel_t_saneam_pv.revision_expirada = 9999999999::bigint::numeric 
  INNER JOIN eiel_tr_saneam_tem_pv tempv ON tempv.clave_tem = eiel_t_saneam_tem.clave AND tempv.codprov_tem= eiel_t_saneam_tem.codprov::text 
		AND tempv.codmunic_tem::text = eiel_t_saneam_tem.codmunic::text AND tempv.tramo_em::text = eiel_t_saneam_tem.tramo_em::text AND tempv.revision_expirada = 9999999999::bigint::numeric
		AND tempv.codprov_pv=eiel_t_saneam_pv.codprov AND tempv.codmunic_pv=eiel_t_saneam_pv.codmunic AND tempv.clave_pv = eiel_t_saneam_pv.clave
		AND tempv.orden_pv=eiel_t_saneam_pv.orden_pv 
		
  LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >=50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_pv.tipo, eiel_t_saneam_pv.zona
  ORDER BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov,eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_emisario_enc_m50 OWNER TO geopista;
GRANT ALL ON TABLE v_emisario_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_emisario_enc_m50 TO consultas;
 

 
  CREATE OR REPLACE VIEW v_TRAMO_EMISARIO AS 
	 SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_tem.material AS tipo_mat, eiel_t_saneam_tem.estado, sum(eiel_t_saneam_tem.long_terre) AS long_terre, sum(eiel_t_saneam_tem.long_marit) AS long_marit
	   FROM eiel_t_saneam_tem
	  LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
	  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric
	  GROUP BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.material, eiel_t_saneam_tem.estado
	  ORDER BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;
 ALTER TABLE v_TRAMO_EMISARIO OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_TRAMO_EMISARIO_M50 AS 
 SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_tem.material AS tipo_mat, eiel_t_saneam_tem.estado, sum(eiel_t_saneam_tem.long_terre) AS long_terre, sum(eiel_t_saneam_tem.long_marit) AS long_marit
   FROM eiel_t_saneam_tem
  LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.material, eiel_t_saneam_tem.estado
  ORDER BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;
 ALTER TABLE v_TRAMO_EMISARIO_M50 OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_COLECTOR AS 
 SELECT
 --fase AS FASE,
 eiel_t_saneam_tcl.clave AS CLAVE,eiel_t_saneam_tcl.codprov AS PROVINCIA, eiel_t_saneam_tcl.codmunic AS MUNICIPIO, eiel_t_saneam_tcl.tramo_cl AS ORDEN_COLE
  FROM eiel_t_saneam_tcl
 WHERE eiel_t_saneam_tcl.revision_expirada=9999999999;
 ALTER TABLE v_COLECTOR OWNER TO geopista;

 CREATE OR REPLACE VIEW v_COLECTOR_ENC AS 
 SELECT 
 --fase AS FASE,
 eiel_t_saneam_tcl.clave AS CLAVE,eiel_t_saneam_tcl.codprov AS PROVINCIA, eiel_t_saneam_tcl.codmunic AS MUNICIPIO, eiel_t_saneam_tcl.tramo_cl AS ORDEN_COLE
  FROM eiel_t_saneam_tcl
  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_saneam_tcl.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_saneam_tcl.codmunic 
  WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999;

 ALTER TABLE v_COLECTOR_ENC OWNER TO geopista;
 
  CREATE OR REPLACE VIEW v_COLECTOR_ENC_M50 AS 
 SELECT 
 --fase AS FASE,
 eiel_t_saneam_tcl.clave AS CLAVE,eiel_t_saneam_tcl.codprov AS PROVINCIA, eiel_t_saneam_tcl.codmunic AS MUNICIPIO, eiel_t_saneam_tcl.tramo_cl AS ORDEN_COLE
  FROM eiel_t_saneam_tcl
  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_saneam_tcl.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_saneam_tcl.codmunic 
  WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999;
 ALTER TABLE v_COLECTOR_ENC_M50 OWNER TO geopista;
 

 CREATE OR REPLACE VIEW v_TRAMO_COLECTOR AS 
	   SELECT 
 --fase AS FASE,
 eiel_c_saneam_tcl.clave AS CLAVE,eiel_c_saneam_tcl.codprov AS PROVINCIA, eiel_c_saneam_tcl.codmunic AS MUNICIPIO, eiel_c_saneam_tcl.tramo_cl AS ORDEN_COLE,
 eiel_t_saneam_tcl.material AS TIPO_COLEC, eiel_t_saneam_tcl.sist_impulsion AS SIST_TRANS, eiel_t_saneam_tcl.estado AS ESTADO,
 eiel_t_saneam_tcl.titular AS TITULAR, eiel_t_saneam_tcl.gestor AS GESTION , sum(eiel_c_saneam_tcl.longitud) AS LONG_TRAMO
 FROM eiel_t_saneam_tcl
 LEFT JOIN eiel_c_saneam_tcl on eiel_t_saneam_tcl.clave=eiel_c_saneam_tcl.clave AND eiel_t_saneam_tcl.codprov=eiel_c_saneam_tcl.codprov AND eiel_t_saneam_tcl.codmunic=eiel_c_saneam_tcl.codmunic AND eiel_t_saneam_tcl.tramo_cl=eiel_c_saneam_tcl.tramo_cl AND eiel_c_saneam_tcl.revision_expirada=9999999999
 left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_c_saneam_tcl.codprov AND eiel_t_padron_ttmm.codmunic=eiel_c_saneam_tcl.codmunic 
 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_saneam_tcl.tramo_cl=eiel_c_saneam_tcl.tramo_cl AND eiel_t_saneam_tcl.revision_expirada=9999999999
GROUP BY  eiel_c_saneam_tcl.clave,eiel_c_saneam_tcl.codprov, eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl,eiel_t_saneam_tcl.material, 
	  eiel_t_saneam_tcl.sist_impulsion, eiel_t_saneam_tcl.estado,eiel_t_saneam_tcl.titular,eiel_t_saneam_tcl.gestor
	  ORDER BY municipio, orden_cole ASC;
 ALTER TABLE v_TRAMO_COLECTOR OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_TRAMO_COLECTOR_M50 AS 
	   SELECT 
 --fase AS FASE,
 eiel_c_saneam_tcl.clave AS CLAVE,eiel_c_saneam_tcl.codprov AS PROVINCIA, eiel_c_saneam_tcl.codmunic AS MUNICIPIO, eiel_c_saneam_tcl.tramo_cl AS ORDEN_COLE,
 eiel_t_saneam_tcl.material AS TIPO_COLEC, eiel_t_saneam_tcl.sist_impulsion AS SIST_TRANS, eiel_t_saneam_tcl.estado AS ESTADO,
 eiel_t_saneam_tcl.titular AS TITULAR, eiel_t_saneam_tcl.gestor AS GESTION , sum(eiel_c_saneam_tcl.longitud) AS LONG_TRAMO
 FROM eiel_t_saneam_tcl
 LEFT JOIN eiel_c_saneam_tcl on eiel_t_saneam_tcl.clave=eiel_c_saneam_tcl.clave AND eiel_t_saneam_tcl.codprov=eiel_c_saneam_tcl.codprov AND eiel_t_saneam_tcl.codmunic=eiel_c_saneam_tcl.codmunic AND eiel_t_saneam_tcl.tramo_cl=eiel_c_saneam_tcl.tramo_cl AND eiel_c_saneam_tcl.revision_expirada=9999999999
 left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_c_saneam_tcl.codprov AND eiel_t_padron_ttmm.codmunic=eiel_c_saneam_tcl.codmunic 
 WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_saneam_tcl.tramo_cl=eiel_c_saneam_tcl.tramo_cl AND eiel_t_saneam_tcl.revision_expirada=9999999999
GROUP BY  eiel_c_saneam_tcl.clave,eiel_c_saneam_tcl.codprov, eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl,eiel_t_saneam_tcl.material, 
	  eiel_t_saneam_tcl.sist_impulsion, eiel_t_saneam_tcl.estado,eiel_t_saneam_tcl.titular,eiel_t_saneam_tcl.gestor
	  ORDER BY municipio, orden_cole ASC;

ALTER TABLE v_TRAMO_COLECTOR_M50 OWNER TO geopista;
 
    
CREATE OR REPLACE VIEW v_RAMAL_SANEAMIENTO AS 
 SELECT eiel_c_saneam_rs.codprov AS provincia, eiel_c_saneam_rs.codmunic AS municipio, eiel_c_saneam_rs.codentidad AS entidad, 
		eiel_c_saneam_rs.codpoblamiento AS nucleo, eiel_c_saneam_rs.material AS tipo_rama, eiel_c_saneam_rs.sist_impulsion AS sist_trans,
		eiel_c_saneam_rs.estado, eiel_c_saneam_rs.tipo_red_interior AS tipo_red,
		eiel_c_saneam_rs.titular, eiel_c_saneam_rs.gestor AS gestion, sum(eiel_c_saneam_rs.longitud) AS longit_ram
 FROM eiel_c_saneam_rs
 left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_c_saneam_rs.codprov AND eiel_t_padron_ttmm.codmunic=eiel_c_saneam_rs.codmunic 
 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_c_saneam_rs.revision_expirada=9999999999
 GROUP BY provincia,municipio,entidad,nucleo,tipo_rama,sist_trans,estado,tipo_red,titular,gestion
 ORDER BY municipio,entidad,nucleo ASC; 
 ALTER TABLE v_RAMAL_SANEAMIENTO OWNER TO geopista;
 


------SANEAMIENTO T

CREATE OR REPLACE VIEW v_DEPURADORA_ENC AS 
 SELECT 
 --fase AS FASE,
eiel_t1_saneam_ed.clave AS CLAVE,eiel_t1_saneam_ed.codprov AS PROVINCIA,eiel_t1_saneam_ed.codmunic AS MUNICIPIO,eiel_t1_saneam_ed.orden_ed AS ORDEN_DEPU,
 		eiel_t1_saneam_ed.trat_pr_1 AS TRAT_PR_1, eiel_t1_saneam_ed.trat_pr_2 AS TRAT_PR_2, eiel_t1_saneam_ed.trat_pr_3 AS TRAT_PR_3,
 		eiel_t1_saneam_ed.trat_sc_1 AS TRAT_SC_1, eiel_t1_saneam_ed.trat_sc_2 AS TRAT_SC_2 ,eiel_t1_saneam_ed.trat_sc_3 AS TRAT_SC_3,
 		eiel_t1_saneam_ed.trat_av_1 AS TRAT_AV_1, eiel_t1_saneam_ed.trat_av_2 AS TRAT_AV_2, eiel_t1_saneam_ed.trat_av_3 AS TRAT_AV_3,
 		eiel_t1_saneam_ed.proc_cm_1 AS PROC_CM_1, eiel_t1_saneam_ed.proc_cm_2 AS PROC_CM_2 ,eiel_t1_saneam_ed.proc_cm_3 AS PROC_CM_3,
 		eiel_t1_saneam_ed.trat_ld_1 AS TRAT_LD_1, eiel_t1_saneam_ed.trat_ld_2 AS TRAT_LD_2, eiel_t1_saneam_ed.trat_ld_3 AS TRAT_LD_3 
 		FROM eiel_t1_saneam_ed  
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t1_saneam_ed.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t1_saneam_ed.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t1_saneam_ed.revision_expirada=9999999999;

 ALTER TABLE v_DEPURADORA_ENC OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_DEPURADORA_ENC_M50 AS 
 SELECT 
  --fase AS FASE,
eiel_t1_saneam_ed.clave AS CLAVE,eiel_t1_saneam_ed.codprov AS PROVINCIA,eiel_t1_saneam_ed.codmunic AS MUNICIPIO,eiel_t1_saneam_ed.orden_ed AS ORDEN_DEPU,
 		eiel_t1_saneam_ed.trat_pr_1 AS TRAT_PR_1, eiel_t1_saneam_ed.trat_pr_2 AS TRAT_PR_2, eiel_t1_saneam_ed.trat_pr_3 AS TRAT_PR_3,
 		eiel_t1_saneam_ed.trat_sc_1 AS TRAT_SC_1, eiel_t1_saneam_ed.trat_sc_2 AS TRAT_SC_2 ,eiel_t1_saneam_ed.trat_sc_3 AS TRAT_SC_3,
 		eiel_t1_saneam_ed.trat_av_1 AS TRAT_AV_1, eiel_t1_saneam_ed.trat_av_2 AS TRAT_AV_2, eiel_t1_saneam_ed.trat_av_3 AS TRAT_AV_3,
 		eiel_t1_saneam_ed.proc_cm_1 AS PROC_CM_1, eiel_t1_saneam_ed.proc_cm_2 AS PROC_CM_2 ,eiel_t1_saneam_ed.proc_cm_3 AS PROC_CM_3,
 		eiel_t1_saneam_ed.trat_ld_1 AS TRAT_LD_1, eiel_t1_saneam_ed.trat_ld_2 AS TRAT_LD_2, eiel_t1_saneam_ed.trat_ld_3 AS TRAT_LD_3 
 		FROM eiel_t1_saneam_ed
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t1_saneam_ed.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t1_saneam_ed.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000	AND eiel_t1_saneam_ed.revision_expirada=9999999999 AND eiel_t_padron_ttmm.revision_expirada=9999999999;
ALTER TABLE v_DEPURADORA_ENC_M50 OWNER TO geopista;

CREATE OR REPLACE VIEW v_DEPURADORA_ENC_2 AS 
 SELECT 
 --fase AS FASE,
eiel_t2_saneam_ed.clave AS CLAVE,eiel_t2_saneam_ed.codprov AS PROVINCIA,eiel_t2_saneam_ed.codmunic AS MUNICIPIO,eiel_t2_saneam_ed.orden_ed AS ORDEN_DEPU,
 		eiel_t2_saneam_ed.titular AS TITULAR, eiel_t2_saneam_ed.gestor AS GESTION,eiel_t2_saneam_ed.capacidad AS CAPACIDAD, eiel_t2_saneam_ed.problem_1 AS PROBLEM_1,
 		eiel_t2_saneam_ed.problem_2 AS PROBLEM_2, eiel_t2_saneam_ed.problem_3 AS PROBLEM_3 ,eiel_t2_saneam_ed.lodo_gest AS LODO_GEST,
 		eiel_t2_saneam_ed.lodo_vert AS LODO_VERT, eiel_t2_saneam_ed.lodo_inci AS LODO_INCI, eiel_t2_saneam_ed.lodo_con_agri AS LODO_CON_A ,
 		eiel_t2_saneam_ed.lodo_sin_agri AS LODO_SIN_A, eiel_t2_saneam_ed.lodo_ot AS LODO_OT
 		FROM eiel_t2_saneam_ed
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t2_saneam_ed.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t2_saneam_ed.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t2_saneam_ed.revision_expirada=9999999999;

 		
 ALTER TABLE v_DEPURADORA_ENC_2 OWNER TO geopista;
 
 CREATE OR REPLACE VIEW v_DEPURADORA_ENC_2_M50 AS 
 SELECT 
--fase AS FASE,
eiel_t2_saneam_ed.clave AS CLAVE,eiel_t2_saneam_ed.codprov AS PROVINCIA,eiel_t2_saneam_ed.codmunic AS MUNICIPIO,eiel_t2_saneam_ed.orden_ed AS ORDEN_DEPU,
 		eiel_t2_saneam_ed.titular AS TITULAR, eiel_t2_saneam_ed.gestor AS GESTION,eiel_t2_saneam_ed.capacidad AS CAPACIDAD, eiel_t2_saneam_ed.problem_1 AS PROBLEM_1,
 		eiel_t2_saneam_ed.problem_2 AS PROBLEM_2, eiel_t2_saneam_ed.problem_3 AS PROBLEM_3 ,eiel_t2_saneam_ed.lodo_gest AS LODO_GEST,
 		eiel_t2_saneam_ed.lodo_vert AS LODO_VERT, eiel_t2_saneam_ed.lodo_inci AS LODO_INCI, eiel_t2_saneam_ed.lodo_con_agri AS LODO_CON_A ,
 		eiel_t2_saneam_ed.lodo_sin_agri AS LODO_SIN_A, eiel_t2_saneam_ed.lodo_ot AS LODO_OT
 		FROM eiel_t2_saneam_ed
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t2_saneam_ed.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t2_saneam_ed.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t2_saneam_ed.revision_expirada=9999999999;;

 ALTER TABLE v_DEPURADORA_ENC_2_M50 OWNER TO geopista;


CREATE OR REPLACE VIEW v_SANEA_AUTONOMO AS 
 SELECT 
 --fase AS FASE,
 eiel_t_saneam_au.clave AS CLAVE,eiel_t_saneam_au.codprov AS PROVINCIA,eiel_t_saneam_au.codmunic AS MUNICIPIO,eiel_t_saneam_au.codentidad AS ENTIDAD,eiel_t_saneam_au.codpoblamiento AS NUCLEO,
 		eiel_t_saneam_au.tipo_sau AS TIPO_SANEA, eiel_t_saneam_au.estado_sau AS ESTADO,eiel_t_saneam_au.adecuacion_sau AS ADECUACION,
 		eiel_t_saneam_au.sau_vivien AS SAU_VIVIEN,eiel_t_saneam_au.sau_pob_re AS SAU_POB_RE,eiel_t_saneam_au.sau_pob_es AS SAU_POB_ES,
 		eiel_t_saneam_au.sau_vi_def AS SAU_VI_DEF,eiel_t_saneam_au.sau_pob_re_def AS SAU_RE_DEF,eiel_t_saneam_au.sau_pob_es_def AS SAU_ES_DEF
 		FROM eiel_t_saneam_au
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_saneam_au.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_saneam_au.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_saneam_au.revision_expirada=9999999999;

 ALTER TABLE v_SANEA_AUTONOMO OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_NUCL_ENCUESTADO_5 AS 
 SELECT 
 --fase AS FASE,
		eiel_t_saneam_serv.codprov AS PROVINCIA,eiel_t_saneam_serv.codmunic AS MUNICIPIO,eiel_t_saneam_serv.codentidad AS ENTIDAD,eiel_t_saneam_serv.codpoblamiento AS NUCLEO,
 		eiel_t_saneam_serv.pozos_registro AS SYD_POZOS, eiel_t_saneam_serv.sumideros AS SYD_SUMIDE,eiel_t_saneam_serv.aliv_c_acum AS SYD_ALI_CO,
 		eiel_t_saneam_serv.aliv_s_acum AS SYD_ALI_SI,eiel_t_saneam_serv.calidad_serv AS SYD_CALIDA,
 		eiel_t_saneam_serv.viviendas_s_conex AS SYD_V_CONE, eiel_t_saneam_serv.viviendas_c_conex AS SYD_V_NCON,
 		eiel_t_saneam_serv.long_rs_deficit AS SYD_L_DEFI,eiel_t_saneam_serv.viviendas_def_conex AS SYD_V_DEFI,
 		eiel_t_saneam_serv.pobl_res_def_afect AS SYD_PR_DEF,	eiel_t_saneam_serv.pobl_est_def_afect AS SYD_PE_DEF,
 		eiel_t_saneam_serv.caudal_total AS SYD_C_DESA,eiel_t_saneam_serv.caudal_tratado AS SYD_C_TRAT,
 		eiel_t_saneam_serv.c_reutilizado_urb AS SYD_RE_URB,	eiel_t_saneam_serv.c_reutilizado_rust AS SYD_RE_RUS,
 		eiel_t_saneam_serv.c_reutilizado_ind AS SYD_RE_IND
 		FROM eiel_t_saneam_serv
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_saneam_serv.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_saneam_serv.codmunic 
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_saneam_serv.revision_expirada=9999999999;
  
 ALTER TABLE v_NUCL_ENCUESTADO_5 OWNER TO geopista;
 

------SANEAMIENTO TR

CREATE OR REPLACE VIEW v_DEP_AGUA_NUCLEO AS 
  SELECT
  --fase AS FASE,
 eiel_tr_saneam_ed_pobl.codprov_pobl AS PROVINCIA, 
 eiel_tr_saneam_ed_pobl.codmunic_pobl AS MUNICIPIO,
 eiel_tr_saneam_ed_pobl.codentidad_pobl AS ENTIDAD,
 eiel_tr_saneam_ed_pobl.codpoblamiento_pobl AS NUCLEO,
 		eiel_tr_saneam_ed_pobl.clave_ed AS CLAVE,
 		eiel_tr_saneam_ed_pobl.codprov_ed AS DE_PROVINC,
 		eiel_tr_saneam_ed_pobl.codmunic_ed AS DE_MUNICIP,
 		eiel_tr_saneam_ed_pobl.orden_ed AS ORDEN_DEPU
  FROM eiel_tr_saneam_ed_pobl
  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_tr_saneam_ed_pobl.codprov_pobl AND eiel_t_padron_ttmm.codmunic=eiel_tr_saneam_ed_pobl.codmunic_pobl 
 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_tr_saneam_ed_pobl.revision_expirada=9999999999;
 
 ALTER TABLE v_DEP_AGUA_NUCLEO OWNER TO geopista;


CREATE OR REPLACE VIEW v_COLECTOR_NUCLEO AS 
  SELECT
  --fase AS FASE,
 eiel_tr_saneam_tcl_pobl.codprov_pobl AS PROVINCIA, eiel_tr_saneam_tcl_pobl.codmunic_pobl AS MUNICIPIO, eiel_tr_saneam_tcl_pobl.codentidad_pobl AS ENTIDAD,eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl AS NUCLEO,
 		eiel_tr_saneam_tcl_pobl.clave_tcl AS CLAVE, eiel_tr_saneam_tcl_pobl.codprov_tcl AS C_PROVINC, eiel_tr_saneam_tcl_pobl.codmunic_tcl AS C_MUNICIP,
 		eiel_tr_saneam_tcl_pobl.tramo_cl AS ORDEN_COLE
  FROM eiel_tr_saneam_tcl_pobl
   left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_tr_saneam_tcl_pobl.codprov_pobl AND eiel_t_padron_ttmm.codmunic=eiel_tr_saneam_tcl_pobl.codmunic_pobl 
 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_tr_saneam_tcl_pobl.revision_expirada=9999999999;
  
 ALTER TABLE v_COLECTOR_NUCLEO OWNER TO geopista;
 


CREATE OR REPLACE VIEW v_EMISARIO_NUCLEO AS 
  SELECT
  --fase AS FASE,
eiel_tr_saneam_tem_pobl.codprov_pobl AS PROVINCIA,eiel_tr_saneam_tem_pobl.codmunic_pobl AS MUNICIPIO,eiel_tr_saneam_tem_pobl.codentidad_pobl AS ENTIDAD,eiel_tr_saneam_tem_pobl.codpoblamiento_pobl AS NUCLEO,
 		eiel_tr_saneam_tem_pobl.clave_tem AS CLAVE,eiel_tr_saneam_tem_pobl.codprov_tem AS EM_PROVINC,eiel_tr_saneam_tem_pobl.codmunic_tem AS EM_MUNICIP,
 		eiel_tr_saneam_tem_pobl.tramo_em AS ORDEN_EMIS
  FROM eiel_tr_saneam_tem_pobl
   left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_tr_saneam_tem_pobl.codprov_pobl AND eiel_t_padron_ttmm.codmunic=eiel_tr_saneam_tem_pobl.codmunic_pobl 
 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_tr_saneam_tem_pobl.revision_expirada=9999999999;
    
 ALTER TABLE v_EMISARIO_NUCLEO OWNER TO geopista;
 

------EQUIPAMIENTO T

 CREATE OR REPLACE VIEW v_CENTRO_ASISTENCIAL AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_as.clave AS CLAVE,eiel_t_as.codprov AS PROVINCIA, eiel_t_as.codmunic AS MUNICIPIO, eiel_t_as.codentidad AS ENTIDAD,
 		eiel_t_as.codpoblamiento AS POBLAMIENT,eiel_t_as.orden_as AS ORDEN_CASI,eiel_t_as.nombre AS NOMBRE,
 		eiel_t_as.tipo AS TIPO_CASIS,eiel_t_as.titular AS TITULAR ,eiel_t_as.gestor AS GESTION,eiel_t_as.plazas AS PLAZAS, eiel_t_as.s_cubierta AS S_CUBI,
 		eiel_t_as.s_aire AS S_AIRE, eiel_t_as.s_solar AS S_SOLA,eiel_t_as.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_as.estado AS ESTADO
 		FROM eiel_t_as
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_as.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_as.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_as.revision_expirada=9999999999;
		  
 ALTER TABLE v_CENTRO_ASISTENCIAL OWNER TO geopista;


    CREATE OR REPLACE VIEW v_CASA_CONSISTORIAL AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_cc.clave AS CLAVE,eiel_t_cc.codprov AS PROVINCIA, eiel_t_cc.codmunic AS MUNICIPIO, eiel_t_cc.codentidad AS ENTIDAD,eiel_t_cc.codpoblamiento AS POBLAMIENT,
 		eiel_t_cc.orden_cc AS ORDEN_CASA ,eiel_t_cc.nombre AS NOMBRE, eiel_t_cc.tipo AS TIPO, eiel_t_cc.titular AS TITULAR, eiel_t_cc.tenencia AS TENENCIA ,eiel_t_cc.s_cubierta AS S_CUBI,eiel_t_cc.s_aire AS S_AIRE ,eiel_t_cc.s_solar AS S_SOLA,
 		eiel_t_cc.acceso_s_ruedas AS ACCESO_S_RUEDAS, eiel_t_cc.estado AS ESTADO
 		FROM eiel_t_cc 
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_cc.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_cc.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_cc.revision_expirada=9999999999;

 ALTER TABLE v_CASA_CONSISTORIAL OWNER TO geopista;


     CREATE OR REPLACE VIEW v_CASA_CON_USO AS 
	 SELECT DISTINCT ON (eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov, eiel_t_cc_usos.codmunic, eiel_t_cc_usos.codentidad, eiel_t_cc_usos.codpoblamiento , eiel_t_cc_usos.orden_cc, eiel_t_cc_usos.uso)
	 eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov AS provincia, eiel_t_cc_usos.codmunic AS municipio, eiel_t_cc_usos.codentidad AS entidad, eiel_t_cc_usos.codpoblamiento AS poblamient, eiel_t_cc_usos.orden_cc AS orden_casa, eiel_t_cc_usos.uso, eiel_t_cc_usos.s_cubierta AS s_cubi
	   FROM eiel_t_cc_usos
	   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cc_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cc_usos.codmunic::text
	  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cc_usos.revision_expirada = 9999999999::bigint::numeric;

 ALTER TABLE v_CASA_CON_USO OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_CEMENTERIO AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_ce.clave AS CLAVE,eiel_t_ce.codprov AS PROVINCIA, eiel_t_ce.codmunic AS MUNICIPIO, eiel_t_ce.codentidad AS ENTIDAD,
 		eiel_t_ce.codpoblamiento AS POBLAMIENT,eiel_t_ce.orden_ce AS ORDEN_CEME,eiel_t_ce.nombre AS NOMBRE,
 		eiel_t_ce.titular AS TITULAR ,eiel_t_ce.distancia AS DISTANCIA,eiel_t_ce.acceso AS ACCESO,eiel_t_ce.capilla AS CAPILLA,
 		eiel_t_ce.deposito AS DEPOSITO,eiel_t_ce.ampliacion AS AMPLIACION,eiel_t_ce.saturacion AS SATURACION,eiel_t_ce.superficie AS SUPERFICIE,
 		eiel_t_ce.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_ce.crematorio AS CREMATORIO	
 		FROM eiel_t_ce
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_ce.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_ce.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_ce.revision_expirada=9999999999;

 ALTER TABLE v_CEMENTERIO OWNER TO geopista;

 
 CREATE OR REPLACE VIEW v_CENT_CULTURAL AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_cu.clave AS CLAVE,eiel_t_cu.codprov AS PROVINCIA, eiel_t_cu.codmunic AS MUNICIPIO, eiel_t_cu.codentidad AS ENTIDAD,
 		eiel_t_cu.codpoblamiento AS POBLAMIENT,eiel_t_cu.orden_cu AS ORDEN_CENT,eiel_t_cu.nombre AS NOMBRE,
 		eiel_t_cu.tipo AS TIPO_CENT,eiel_t_cu.titular AS TITULAR ,eiel_t_cu.gestor AS GESTION, eiel_t_cu.s_cubierta AS S_CUBI,
 		eiel_t_cu.s_aire AS S_AIRE, eiel_t_cu.s_solar AS S_SOLA,eiel_t_cu.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_cu.estado AS ESTADO
 		FROM eiel_t_cu 
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_cu.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_cu.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND  eiel_t_cu.revision_expirada=9999999999;
		
 ALTER TABLE v_CENT_CULTURAL OWNER TO geopista;


 CREATE OR REPLACE VIEW v_CENT_CULTURAL_USOS AS 
 SELECT DISTINCT ON (eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov, eiel_t_cu_usos.codmunic, eiel_t_cu_usos.codentidad, eiel_t_cu_usos.codpoblamiento, eiel_t_cu_usos.orden_cu, eiel_t_cu_usos.uso) 
 	eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov AS provincia, eiel_t_cu_usos.codmunic AS municipio, eiel_t_cu_usos.codentidad AS entidad, eiel_t_cu_usos.codpoblamiento AS poblamient, eiel_t_cu_usos.orden_cu AS orden_cent, eiel_t_cu_usos.uso, eiel_t_cu_usos.s_cubierta AS s_cubi
   FROM eiel_t_cu_usos
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cu_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cu_usos.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cu_usos.revision_expirada = 9999999999::bigint::numeric
  ORDER BY eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov, eiel_t_cu_usos.codmunic, eiel_t_cu_usos.codentidad, eiel_t_cu_usos.codpoblamiento, eiel_t_cu_usos.orden_cu, eiel_t_cu_usos.uso;
 ALTER TABLE v_CENT_CULTURAL_USOS OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_CENTRO_ENSENANZA AS 
SELECT 
 		--fase AS FASE,
 		eiel_t_en.clave AS CLAVE,eiel_t_en.codprov AS PROVINCIA, eiel_t_en.codmunic AS MUNICIPIO, eiel_t_en.codentidad AS ENTIDAD,
 		eiel_t_en.codpoblamiento AS POBLAMIENT,eiel_t_en.orden_en AS ORDEN_CENT,eiel_t_en.nombre AS NOMBRE,
 		eiel_t_en.ambito AS AMBITO,eiel_t_en.titular AS TITULAR , eiel_t_en.s_cubierta AS S_CUBI,
 		eiel_t_en.s_aire AS S_AIRE, eiel_t_en.s_solar AS S_SOLA,eiel_t_en.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_en.estado AS ESTADO
 		FROM eiel_t_en
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_en.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_en.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_en.revision_expirada=9999999999;

		  
 ALTER TABLE v_CENTRO_ENSENANZA OWNER TO geopista;

CREATE OR REPLACE VIEW v_NIVEL_ENSENANZA AS 
 SELECT DISTINCT ON (eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov , eiel_t_en_nivel.codmunic, eiel_t_en_nivel.codentidad ,eiel_t_en_nivel.codpoblamiento, eiel_t_en_nivel.orden_en, eiel_t_en_nivel.nivel)
 eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov AS provincia, eiel_t_en_nivel.codmunic AS municipio, eiel_t_en_nivel.codentidad AS entidad, eiel_t_en_nivel.codpoblamiento AS poblamient, eiel_t_en_nivel.orden_en AS orden_cent, eiel_t_en_nivel.nivel, eiel_t_en_nivel.unidades, eiel_t_en_nivel.plazas, eiel_t_en_nivel.alumnos
   FROM eiel_t_en_nivel
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_en_nivel.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_en_nivel.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_en_nivel.revision_expirada = 9999999999::bigint::numeric;

 ALTER TABLE v_NIVEL_ENSENANZA OWNER TO geopista;


CREATE OR REPLACE VIEW v_INSTAL_DEPORTIVA AS 
SELECT 
 		--fase AS FASE,
 		eiel_t_id.clave AS CLAVE,eiel_t_id.codprov AS PROVINCIA, eiel_t_id.codmunic AS MUNICIPIO, eiel_t_id.codentidad AS ENTIDAD,
 		eiel_t_id.codpoblamiento AS POBLAMIENT,eiel_t_id.orden_id AS ORDEN_INST,eiel_t_id.nombre AS NOMBRE,
 		eiel_t_id.tipo AS TIPO_INSDE,eiel_t_id.titular AS TITULAR,eiel_t_id.gestor AS GESTION , eiel_t_id.s_cubierta AS S_CUBI,
 		eiel_t_id.s_aire AS S_AIRE, eiel_t_id.s_solar AS S_SOLA,eiel_t_id.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_id.estado AS ESTADO
 		FROM eiel_t_id
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_id.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_id.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND  eiel_t_id.revision_expirada=9999999999;
		  
 ALTER TABLE v_INSTAL_DEPORTIVA OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_INST_DEPOR_DEPORTE AS 
 SELECT  DISTINCT ON (eiel_t_id_deportes.clave, eiel_t_id_deportes.codprov, eiel_t_id_deportes.codmunic, eiel_t_id_deportes.codentidad, eiel_t_id_deportes.codpoblamiento, eiel_t_id_deportes.orden_id, eiel_t_id_deportes.tipo_deporte)
 		--fase AS FASE,
 		eiel_t_id_deportes.clave AS CLAVE,eiel_t_id_deportes.codprov AS PROVINCIA, eiel_t_id_deportes.codmunic AS MUNICIPIO, eiel_t_id_deportes.codentidad AS ENTIDAD,
 		eiel_t_id_deportes.codpoblamiento AS POBLAMIENT,eiel_t_id_deportes.orden_id AS ORDEN_INST,
 		eiel_t_id_deportes.tipo_deporte AS TIPO_DEPOR
 		FROM eiel_t_id_deportes
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_id_deportes.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_id_deportes.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND  eiel_t_id_deportes.revision_expirada=9999999999;

 ALTER TABLE v_INST_DEPOR_DEPORTE OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_PROTECCION_CIVIL AS 
SELECT 
 		--fase AS FASE,
 		eiel_t_ip.clave AS CLAVE,eiel_t_ip.codprov AS PROVINCIA, eiel_t_ip.codmunic AS MUNICIPIO, eiel_t_ip.codentidad AS ENTIDAD,
 		eiel_t_ip.codpoblamiento AS POBLAMIENT,eiel_t_ip.orden_ip AS ORDEN_PROT,eiel_t_ip.nombre AS NOMBRE,
 		eiel_t_ip.tipo AS TIPO_PCIV,eiel_t_ip.titular AS TITULAR,eiel_t_ip.gestor AS GESTION ,eiel_t_ip.ambito AS AMBITO, 
 		eiel_t_ip.plan_profe AS PLAN_PROFE,eiel_t_ip.plan_volun AS PLAN_VOLUN,eiel_t_ip.s_cubierta AS S_CUBI,
 		eiel_t_ip.s_aire AS S_AIRE, eiel_t_ip.s_solar AS S_SOLA,eiel_t_ip.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_ip.estado AS ESTADO,
 		eiel_t_ip.vehic_incendio AS VEHIC_INCE,eiel_t_ip.vehic_rescate AS VEHIC_RESC,eiel_t_ip.ambulancia AS AMBULANCIA,
 		eiel_t_ip.medios_aereos AS MEDIOS_AER,eiel_t_ip.otros_vehc AS OTROS_VEHI,eiel_t_ip.quitanieves AS QUITANIEVE,
 		eiel_t_ip.detec_ince AS DETEC_INCE,eiel_t_ip.otros AS OTROS 		
 		FROM eiel_t_ip
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_ip.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_ip.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_ip.revision_expirada=9999999999;
		  
 ALTER TABLE v_PROTECCION_CIVIL OWNER TO geopista;


CREATE OR REPLACE VIEW v_LONJA_MERC_FERIA AS 
SELECT 
 		--fase AS FASE,
 		eiel_t_lm.clave AS CLAVE,eiel_t_lm.codprov AS PROVINCIA, eiel_t_lm.codmunic AS MUNICIPIO, eiel_t_lm.codentidad AS ENTIDAD,
 		eiel_t_lm.codpoblamiento AS POBLAMIENT,eiel_t_lm.orden_lm AS ORDEN_LMF,eiel_t_lm.nombre AS NOMBRE,
 		eiel_t_lm.tipo AS TIPO_LONJ,eiel_t_lm.titular AS TITULAR,eiel_t_lm.gestor AS GESTION , eiel_t_lm.s_cubierta AS S_CUBI,
 		eiel_t_lm.s_aire AS S_AIRE, eiel_t_lm.s_solar AS S_SOLA,eiel_t_lm.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_lm.estado AS ESTADO
 		FROM eiel_t_lm
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_lm.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_lm.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_lm.revision_expirada=9999999999;
	  
 ALTER TABLE v_LONJA_MERC_FERIA OWNER TO geopista;


CREATE OR REPLACE VIEW v_MATADERO AS 
SELECT 
 		--fase AS FASE,
 		eiel_t_mt.clave AS CLAVE,eiel_t_mt.codprov AS PROVINCIA, eiel_t_mt.codmunic AS MUNICIPIO, eiel_t_mt.codentidad AS ENTIDAD,
 		eiel_t_mt.codpoblamiento AS POBLAMIENT,eiel_t_mt.orden_mt AS ORDEN_MATA,eiel_t_mt.nombre AS NOMBRE,eiel_t_mt.clase AS CLASE_MAT,
 		eiel_t_mt.titular AS TITULAR,eiel_t_mt.gestor AS GESTION , eiel_t_mt.s_cubierta AS S_CUBI,
 		eiel_t_mt.s_aire AS S_AIRE, eiel_t_mt.s_solar AS S_SOLA,eiel_t_mt.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_mt.estado AS ESTADO,
 		eiel_t_mt.capacidad AS CAPACIDAD,eiel_t_mt.utilizacion AS UTILIZACIO,eiel_t_mt.tunel AS TUNEL,
 		eiel_t_mt.bovino AS BOVINO,eiel_t_mt.ovino AS OVINO,eiel_t_mt.porcino AS PORCINO,eiel_t_mt.otros AS OTROS
 		FROM eiel_t_mt
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_mt.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_mt.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_mt.revision_expirada=9999999999;

 ALTER TABLE v_MATADERO OWNER TO geopista;

CREATE OR REPLACE VIEW v_PARQUE AS 
SELECT 
 		--fase AS FASE,
 		eiel_t_pj.clave AS CLAVE,eiel_t_pj.codprov AS PROVINCIA, eiel_t_pj.codmunic AS MUNICIPIO, eiel_t_pj.codentidad AS ENTIDAD,
 		eiel_t_pj.codpoblamiento AS POBLAMIENT,eiel_t_pj.orden_pj AS ORDEN_PARQ,eiel_t_pj.nombre AS NOMBRE,
 		eiel_t_pj.tipo AS TIPO_PARQ,eiel_t_pj.titular AS TITULAR,eiel_t_pj.gestor AS GESTION , eiel_t_pj.s_cubierta AS S_CUBI,
 		eiel_t_pj.s_aire AS S_AIRE, eiel_t_pj.s_solar AS S_SOLA,eiel_t_pj.agua AS AGUA,eiel_t_pj.saneamiento AS SANEAMIENT,
 		eiel_t_pj.electricidad AS ELECTRICID,eiel_t_pj.comedor AS COMEDOR,eiel_t_pj.juegos_inf AS JUEGOS_INF,
 		eiel_t_pj.otras AS OTRAS,eiel_t_pj.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_pj.estado AS ESTADO
 		FROM eiel_t_pj	
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_pj.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_pj.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_pj.revision_expirada=9999999999;
	  
 ALTER TABLE v_PARQUE OWNER TO geopista;

CREATE OR REPLACE VIEW v_CENTRO_SANITARIO AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_sa.clave AS CLAVE,eiel_t_sa.codprov AS PROVINCIA, eiel_t_sa.codmunic AS MUNICIPIO, eiel_t_sa.codentidad AS ENTIDAD,
 		eiel_t_sa.codpoblamiento AS POBLAMIENT,eiel_t_sa.orden_sa AS ORDEN_CSAN,eiel_t_sa.nombre AS NOMBRE,
 		eiel_t_sa.tipo AS TIPO_CSAN,eiel_t_sa.titular AS TITULAR , eiel_t_sa.gestor AS GESTION, eiel_t_sa.s_cubierta AS S_CUBI,
 		eiel_t_sa.s_aire AS S_AIRE, eiel_t_sa.s_solar AS S_SOLA,eiel_t_sa.uci AS UCI,eiel_t_sa.n_camas AS CAMAS,
 		eiel_t_sa.acceso_s_ruedas AS ACCESO_S_RUEDAS,eiel_t_sa.estado AS ESTADO
 		FROM eiel_t_sa
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_sa.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_sa.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_sa.revision_expirada=9999999999;
	  
 ALTER TABLE v_CENTRO_SANITARIO OWNER TO geopista;


CREATE OR REPLACE VIEW v_EDIFIC_PUB_SIN_USO AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_su.clave AS CLAVE,eiel_t_su.codprov AS PROVINCIA, eiel_t_su.codmunic AS MUNICIPIO, eiel_t_su.codentidad AS ENTIDAD,
 		eiel_t_su.codpoblamiento AS POBLAMIENT,eiel_t_su.orden_su AS ORDEN_EDIF,eiel_t_su.nombre AS NOMBRE,
 		eiel_t_su.titular AS TITULAR , eiel_t_su.s_cubierta AS S_CUBI,
 		eiel_t_su.s_aire AS S_AIRE, eiel_t_su.s_solar AS S_SOLA,
 		eiel_t_su.estado AS ESTADO,eiel_t_su.uso_anterior AS USOANT
 		FROM eiel_t_su
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_su.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_su.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_su.revision_expirada=9999999999;
		  
 ALTER TABLE v_EDIFIC_PUB_SIN_USO OWNER TO geopista;


CREATE OR REPLACE VIEW v_TANATORIO AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_ta.clave AS CLAVE,eiel_t_ta.codprov AS PROVINCIA, eiel_t_ta.codmunic AS MUNICIPIO, eiel_t_ta.codentidad AS ENTIDAD,
 		eiel_t_ta.codpoblamiento AS POBLAMIENT,eiel_t_ta.orden_ta AS ORDEN_TANA,eiel_t_ta.nombre AS NOMBRE,
 		eiel_t_ta.titular AS TITULAR, eiel_t_ta.gestor AS GESTION, eiel_t_ta.s_cubierta AS S_CUBI,
 		eiel_t_ta.s_aire AS S_AIRE, eiel_t_ta.s_solar AS S_SOLA,eiel_t_ta.salas AS SALAS,
 		eiel_t_ta.acceso_s_ruedas AS ACCESO_S_RUEDAS,
 		eiel_t_ta.estado AS ESTADO
 		FROM eiel_t_ta
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_ta.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_ta.codmunic
		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_ta.revision_expirada=9999999999;
		  
 ALTER TABLE v_TANATORIO OWNER TO geopista;
 

-------INFRAESTRUCTURA VIARIA C

---Ojo!!!las viviendas afectadas deben ser la suma si cada tramo contiene las viviendas afectadas.

CREATE OR REPLACE VIEW v_INFRAESTR_VIARIA AS 
    SELECT distinct on(eiel_c_redviaria_tu.codprov , eiel_c_redviaria_tu.codmunic , eiel_c_redviaria_tu.codentidad , eiel_c_redviaria_tu.codpoblamiento , eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado)
 eiel_c_redviaria_tu.codprov AS provincia, eiel_c_redviaria_tu.codmunic AS municipio, eiel_c_redviaria_tu.codentidad AS entidad, eiel_c_redviaria_tu.codpoblamiento AS poblamient, eiel_c_redviaria_tu.tipo AS tipo_infr, eiel_c_redviaria_tu.estado, sum(eiel_c_redviaria_tu.longitud) AS longitud, sum(eiel_c_redviaria_tu.superficie) AS superficie, eiel_c_redviaria_tu.viviendas_afec AS viv_afecta
   FROM eiel_c_redviaria_tu
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_redviaria_tu.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_redviaria_tu.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND  eiel_c_redviaria_tu.revision_expirada = 9999999999
  GROUP BY eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado,eiel_c_redviaria_tu.viviendas_afec;
		  
 ALTER TABLE v_INFRAESTR_VIARIA OWNER TO geopista;
 

---------RED CARRETERAS C

CREATE OR REPLACE VIEW v_CARRETERA AS 
 SELECT DISTINCT ON (eiel_t_carreteras.codprov , eiel_t_carreteras.cod_carrt) eiel_t_carreteras.codprov AS provincia, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion AS denominaci
   FROM eiel_t_carreteras
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_carreteras.codprov::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999
  AND eiel_t_carreteras.revision_expirada = 9999999999
  GROUP BY eiel_t_carreteras.codprov , eiel_t_carreteras.cod_carrt ,eiel_t_carreteras.denominacion 
  ORDER BY eiel_t_carreteras.codprov, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion;
  
 ALTER TABLE v_CARRETERA OWNER TO geopista;


CREATE OR REPLACE VIEW v_TRAMO_CARRETERA AS 
  SELECT distinct on(eiel_c_tramos_carreteras.codprov, eiel_c_tramos_carreteras.codmunic, eiel_c_tramos_carreteras.cod_carrt,
 	eiel_c_tramos_carreteras.pki)
 	--fase AS FASE,
 	eiel_c_tramos_carreteras.codprov AS PROVINCIA, eiel_c_tramos_carreteras.codmunic AS MUNICIPIO, eiel_c_tramos_carreteras.cod_carrt AS COD_CARRT,
 	eiel_c_tramos_carreteras.pki AS PK_INICIAL,eiel_c_tramos_carreteras.pkf AS PK_FINAL, carreteras.titular_via AS TITULAR , eiel_c_tramos_carreteras.gestor AS GESTION,
 	eiel_c_tramos_carreteras.senaliza AS SENALIZA,eiel_c_tramos_carreteras.firme AS FIRME,eiel_c_tramos_carreteras.estado AS ESTADO,
 	eiel_c_tramos_carreteras.ancho AS ANCHO , eiel_c_tramos_carreteras.longitud AS LONGITUD,eiel_c_tramos_carreteras.paso_nivel AS PASOS_NIVE,
 	eiel_c_tramos_carreteras.dimensiona AS DIMENSIONA,eiel_c_tramos_carreteras.muy_sinuoso AS MUY_SINUOS,eiel_c_tramos_carreteras.pte_excesiva AS PTE_EXCESI
 	,eiel_c_tramos_carreteras.fre_estrech AS FRE_ESTREC
 	FROM eiel_c_tramos_carreteras
	LEFT JOIN eiel_t_carreteras carreteras ON eiel_c_tramos_carreteras.codprov=carreteras.codprov AND eiel_c_tramos_carreteras.cod_carrt=carreteras.cod_carrt  AND  carreteras.revision_expirada=9999999999
	left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_c_tramos_carreteras.codprov AND eiel_t_padron_ttmm.codmunic=eiel_c_tramos_carreteras.codmunic
    WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_c_tramos_carreteras.revision_expirada=9999999999;
    
 ALTER TABLE v_TRAMO_CARRETERA OWNER TO geopista;


-------ALUMBRADO VIEW

CREATE OR REPLACE VIEW v_ALUMBRADO AS 
  SELECT eiel_c_alum_pl.codprov AS PROVINCIA, eiel_c_alum_pl.codmunic AS MUNICIPIO, 
  		eiel_c_alum_pl.codentidad AS ENTIDAD,eiel_c_alum_pl.codpoblamiento AS NUCLEO,
 		eiel_c_alum_pl.ah_ener_rfl AS AH_ENER_RL, eiel_c_alum_pl.ah_ener_rfi AS AH_ENER_RI, eiel_c_alum_pl.estado AS CALIDAD,
 		eiel_c_alum_pl.potencia AS POT_INSTAL,
 		count(*)AS N_PUNTOS
  FROM eiel_c_alum_pl
  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_c_alum_pl.codprov AND eiel_t_padron_ttmm.codmunic=eiel_c_alum_pl.codmunic
  WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_c_alum_pl.revision_expirada=9999999999
  GROUP BY eiel_c_alum_pl.codprov,eiel_c_alum_pl.codmunic,eiel_c_alum_pl.codentidad,eiel_c_alum_pl.codpoblamiento,eiel_c_alum_pl.ah_ener_rfl,eiel_c_alum_pl.ah_ener_rfi,
  eiel_c_alum_pl.estado,eiel_c_alum_pl.potencia
  ORDER BY municipio,entidad,nucleo ASC;
 ALTER TABLE v_ALUMBRADO OWNER TO geopista;


-----VERTED

CREATE OR REPLACE VIEW v_VERTEDERO AS 
 SELECT 
 	--fase AS FASE,
 	eiel_t_vt.clave,eiel_t_vt.codprov AS PROVINCIA, eiel_t_vt.codmunic AS MUNICIPIO, eiel_t_vt.orden_vt AS ORDEN_VER
 	FROM eiel_t_vt
    WHERE eiel_t_vt.revision_expirada=9999999999;
    ALTER TABLE v_VERTEDERO OWNER TO geopista;

CREATE OR REPLACE VIEW v_VERT_ENCUESTADO AS 
 SELECT 
 	--fase AS FASE,
 	eiel_t_vt.clave AS CLAVE,eiel_t_vt.codprov AS PROVINCIA, eiel_t_vt.codmunic AS MUNICIPIO, eiel_t_vt.orden_vt AS ORDEN_VER,
 	eiel_t_vt.tipo AS TIPO_VER, eiel_t_vt.titular AS TITULAR,eiel_t_vt.gestor AS GESTION,eiel_t_vt.olores AS OLORES,
 	eiel_t_vt.humos AS HUMOS,eiel_t_vt.cont_anima AS CONT_ANIMA,eiel_t_vt.r_inun AS R_INUN,
 	eiel_t_vt.filtracion AS FILTRACION,eiel_t_vt.impacto_v AS IMPACTO_V,
 	eiel_t_vt.frec_averia AS FREC_AVERI,eiel_t_vt.saturacion AS SATURACION,
 	eiel_t_vt.inestable AS INESTABLE,eiel_t_vt.otros AS OTROS,
 	eiel_t_vt.capac_tot AS CAPAC_TOT,eiel_t_vt.capac_tot_porc AS CAPAC_PORC, 
 	eiel_t_vt.capac_ampl AS CAPAC_AMPL,eiel_t_vt.capac_transf AS CAPAC_TRAN, eiel_t_vt.estado AS ESTADO,
 	eiel_t_vt.vida_util AS VIDA_UTIL,eiel_t_vt.categoria AS CATEGORIA,eiel_t_vt.actividad AS ACTIVIDAD 
 	FROM eiel_t_vt
 	left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_vt.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_vt.codmunic 
 	WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_vt.revision_expirada=9999999999;	  
		  
 ALTER TABLE v_VERT_ENCUESTADO OWNER TO geopista;
 
 CREATE OR REPLACE VIEW v_VERT_ENCUESTADO_M50 AS 
 SELECT 
	--fase AS FASE,
 	eiel_t_vt.clave AS CLAVE,eiel_t_vt.codprov AS PROVINCIA, eiel_t_vt.codmunic AS MUNICIPIO, eiel_t_vt.orden_vt AS ORDEN_VER,
eiel_t_vt.tipo AS TIPO_VER, eiel_t_vt.titular AS TITULAR,eiel_t_vt.gestor AS GESTION,eiel_t_vt.olores AS OLORES,
	eiel_t_vt.humos AS HUMOS,eiel_t_vt.cont_anima AS CONT_ANIMA,eiel_t_vt.r_inun AS R_INUN,
 	eiel_t_vt.filtracion AS FILTRACION,eiel_t_vt.impacto_v AS IMPACTO_V,
	eiel_t_vt.frec_averia AS FREC_AVERI,eiel_t_vt.saturacion AS SATURACION,
	eiel_t_vt.inestable AS INESTABLE,eiel_t_vt.otros AS OTROS,
	eiel_t_vt.capac_tot AS CAPAC_TOT,eiel_t_vt.capac_tot_porc AS CAPAC_PORC, 
	eiel_t_vt.capac_ampl AS CAPAC_AMPL,eiel_t_vt.capac_transf AS CAPAC_TRAN, eiel_t_vt.estado AS ESTADO,
	eiel_t_vt.vida_util AS VIDA_UTIL,eiel_t_vt.categoria AS CATEGORIA,eiel_t_vt.actividad AS ACTIVIDAD 
 	FROM eiel_t_vt
 	left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_vt.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_vt.codmunic 
 	WHERE eiel_t_padron_ttmm.total_poblacion_a1>=50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_vt.revision_expirada=9999999999;	  
ALTER TABLE v_VERT_ENCUESTADO_M50 OWNER TO geopista;

----VERTEDERO TR

CREATE OR REPLACE VIEW v_VERTEDERO_NUCLEO AS 
  SELECT distinct
  --fase AS FASE,
 eiel_tr_vt_pobl.codprov AS PROVINCIA, eiel_tr_vt_pobl.codmunic AS MUNICIPIO, eiel_tr_vt_pobl.codentidad AS ENTIDAD,eiel_tr_vt_pobl.codpoblamiento AS NUCLEO,
 		eiel_tr_vt_pobl.clave_vt AS CLAVE, eiel_tr_vt_pobl.codprov_vt AS VER_PROVIN, eiel_tr_vt_pobl.codmunic_vt AS VER_MUNICI,
 		eiel_tr_vt_pobl.orden_vt AS VER_CODIGO
  FROM eiel_tr_vt_pobl
  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_tr_vt_pobl.codprov AND eiel_t_padron_ttmm.codmunic=eiel_tr_vt_pobl.codmunic
 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999  AND eiel_tr_vt_pobl.revision_expirada=9999999999;

 ALTER TABLE v_VERTEDERO_NUCLEO OWNER TO geopista;


------RECOGIDA DE BASURAS T

CREATE OR REPLACE VIEW v_RECOGIDA_BASURA AS 
 SELECT 
 --fase AS FASE,
 		eiel_t_rb.codprov AS PROVINCIA, eiel_t_rb.codmunic AS MUNICIPIO, eiel_t_rb.codentidad AS ENTIDAD,eiel_t_rb.codpoblamiento AS NUCLEO,
 		eiel_t_rb.tipo AS TIPO_RBAS,eiel_t_rb.gestor AS GESTION,eiel_t_rb.periodicidad AS PERIODICID,eiel_t_rb.calidad AS CALIDAD,
 		eiel_t_rb.tm_res_urb AS PRODU_BASU, eiel_t_rb.n_contenedores AS CONTENEDOR
 		FROM eiel_t_rb
 		 left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_rb.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_rb.codmunic
		 WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_rb.revision_expirada=9999999999;

 ALTER TABLE v_RECOGIDA_BASURA OWNER TO geopista;


CREATE OR REPLACE VIEW v_NUCL_ENCUESTADO_6 AS 
  SELECT 
  --fase AS FASE,
  eiel_t_rb_serv.codprov AS PROVINCIA, eiel_t_rb_serv.codmunic AS MUNICIPIO, eiel_t_rb_serv.codentidad AS ENTIDAD,eiel_t_rb_serv.codpoblamiento AS NUCLEO,
  eiel_t_rb_serv.srb_viviendas_afec AS RBA_V_SSER,eiel_t_rb_serv.srb_pob_res_afect AS RBA_PR_SSE,
  eiel_t_rb_serv.srb_pob_est_afect AS RBA_PE_SSE,eiel_t_rb_serv.serv_limp_calles AS RBA_SERLIM,eiel_t_rb_serv.plantilla_serv_limp AS RBA_PLALIM
  FROM eiel_t_rb_serv
  left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_rb_serv.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_rb_serv.codmunic
  WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_rb_serv.revision_expirada=9999999999;

ALTER TABLE v_NUCL_ENCUESTADO_6 OWNER TO geopista;


---NUCLEO DE POBLACION

CREATE OR REPLACE VIEW v_NUCL_ENCUESTADO_1 AS 
 SELECT 
 --fase AS FASE,
 		eiel_t_nucl_encuest_1.codprov AS PROVINCIA, eiel_t_nucl_encuest_1.codmunic AS MUNICIPIO, eiel_t_nucl_encuest_1.codentidad AS ENTIDAD,eiel_t_nucl_encuest_1.codpoblamiento AS NUCLEO,
 		eiel_t_nucl_encuest_1.padron AS PADRON, eiel_t_nucl_encuest_1.pob_estacional AS POB_ESTACI, eiel_t_nucl_encuest_1.altitud AS ALTITUD,
 		eiel_t_nucl_encuest_1.viviendas_total AS VIV_TOTAL ,eiel_t_nucl_encuest_1.hoteles AS HOTELES,
 		eiel_t_nucl_encuest_1.casas_rural AS CASAS_RURA, eiel_t_nucl_encuest_1.accesibilidad AS ACCESIB
 		FROM eiel_t_nucl_encuest_1
 		 left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_nucl_encuest_1.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_nucl_encuest_1.codmunic
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_nucl_encuest_1.revision_expirada=9999999999;
	  
 ALTER TABLE v_NUCL_ENCUESTADO_1 OWNER TO geopista;

 
 CREATE OR REPLACE VIEW v_NUCL_ENCUESTADO_2 AS 
 SELECT 
 --fase AS FASE,
 		eiel_t_nucl_encuest_2.codprov AS PROVINCIA, eiel_t_nucl_encuest_2.codmunic AS MUNICIPIO, eiel_t_nucl_encuest_2.codentidad AS ENTIDAD,eiel_t_nucl_encuest_2.codpoblamiento AS NUCLEO,
		eiel_t_nucl_encuest_2.aag_caudal AS AAG_CAUDAL,eiel_t_nucl_encuest_2.aag_restri AS AAG_RESTRI,
		eiel_t_nucl_encuest_2.aag_contad AS AAG_CONTAD,eiel_t_nucl_encuest_2.aag_tasa AS AAG_TASA,eiel_t_nucl_encuest_2.aag_instal AS AAG_INSTAL,
		eiel_t_nucl_encuest_2.aag_hidran AS AAG_HIDRAN,eiel_t_nucl_encuest_2.aag_est_hi AS AAG_EST_HI,eiel_t_nucl_encuest_2.aag_valvul AS AAG_VALVUL,
		eiel_t_nucl_encuest_2.aag_est_va AS AAG_EST_VA,
		eiel_t_nucl_encuest_2.aag_bocasr AS AAG_BOCASR,eiel_t_nucl_encuest_2.aag_est_bo AS AAG_EST_BO,eiel_t_nucl_encuest_2.cisterna AS CISTERNA
 		FROM eiel_t_nucl_encuest_2
 		 left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_nucl_encuest_2.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_nucl_encuest_2.codmunic
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_nucl_encuest_2.revision_expirada=9999999999;

 ALTER TABLE v_NUCL_ENCUESTADO_2 OWNER TO geopista;


----TABLA PADRON

 CREATE OR REPLACE VIEW v_PADRON AS 
 SELECT 
 --fase AS FASE,
 		eiel_t_padron_ttmm.codprov AS PROVINCIA, eiel_t_padron_ttmm.codmunic AS MUNICIPIO,
		eiel_t_padron_ttmm.n_hombres_a1 AS HOMBRES,eiel_t_padron_ttmm.n_mujeres_a1 AS MUJERES,eiel_t_padron_ttmm.total_poblacion_a1 AS TOTAL_POB
		FROM eiel_t_padron_ttmm
		WHERE eiel_t_padron_ttmm.revision_expirada=9999999999;

 		
 ALTER TABLE v_PADRON OWNER TO geopista;


-----INFORMACION GENERAL DEL NUCLEO
CREATE OR REPLACE VIEW v_NUCL_ENCUESTADO_7 AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_inf_ttmm.codprov AS PROVINCIA, eiel_t_inf_ttmm.codmunic AS MUNICIPIO, eiel_t_inf_ttmm.codentidad AS ENTIDAD,
 		eiel_t_inf_ttmm.codpoblamiento AS POBLAMIENT,
 		eiel_t_inf_ttmm.tv_ant AS TV_ANT , eiel_t_inf_ttmm.tv_ca AS TV_CA, eiel_t_inf_ttmm.tm_gsm AS TM_GSM, eiel_t_inf_ttmm.tm_umts AS TM_UMTS,
 		eiel_t_inf_ttmm.tm_gprs AS TM_GPRS,
 		eiel_t_inf_ttmm.correo AS CORREO, eiel_t_inf_ttmm.ba_rd AS BA_RD, eiel_t_inf_ttmm.ba_xd AS BA_XD, eiel_t_inf_ttmm.ba_wi AS BA_WI,
 		eiel_t_inf_ttmm.ba_ca AS BA_CA,eiel_t_inf_ttmm.ba_rb AS BA_RB, eiel_t_inf_ttmm.ba_st AS BA_ST, eiel_t_inf_ttmm.capi AS CAPI,
 		eiel_t_inf_ttmm.electric AS ELECTRICID,eiel_t_inf_ttmm.gas AS GAS,
 		eiel_t_inf_ttmm.alu_v_sin AS ALU_V_SIN,eiel_t_inf_ttmm.alu_l_sin AS ALU_L_SIN
 		FROM eiel_t_inf_ttmm
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_inf_ttmm.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_inf_ttmm.codmunic
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_inf_ttmm.revision_expirada=9999999999;

 ALTER TABLE v_NUCL_ENCUESTADO_7 OWNER TO geopista;
 


---TRATAMIENTO DE PLAN URBANISTICO
CREATE OR REPLACE VIEW v_PLAN_URBANISTICO AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_planeam_urban.codprov AS PROVINCIA, eiel_t_planeam_urban.codmunic AS MUNICIPIO,
 		eiel_t_planeam_urban.tipo_urba AS TIPO_URBA , eiel_t_planeam_urban.estado_tramit AS ESTADO_TRA, eiel_t_planeam_urban.denominacion AS DENOMINACI,
 		eiel_t_planeam_urban.sup_muni AS SUPERFICIE,
 		eiel_t_planeam_urban.fecha_bo AS BO, eiel_t_planeam_urban.s_urbano AS URBAN, eiel_t_planeam_urban.s_no_urbanizable AS NO_URBABLE,
 		eiel_t_planeam_urban.s_no_urban_especial AS NOURBABLE_
 		FROM eiel_t_planeam_urban
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_planeam_urban.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_planeam_urban.codmunic
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_planeam_urban.revision_expirada=9999999999;
  
		  
 ALTER TABLE v_PLAN_URBANISTICO OWNER TO geopista;


----OTROS SERVICIOS MUNICIPALES

CREATE OR REPLACE VIEW v_OT_SERV_MUNICIPAL AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_otros_serv_munic.codprov AS PROVINCIA, eiel_t_otros_serv_munic.codmunic AS MUNICIPIO, 
 		eiel_t_otros_serv_munic.sw_inf_grl AS SW_INF_GRL , eiel_t_otros_serv_munic.sw_inf_tur AS SW_INF_TUR,
 		eiel_t_otros_serv_munic.sw_gb_elec AS SW_GB_ELEC, eiel_t_otros_serv_munic.ord_soterr AS ORD_SOTERR,
 		eiel_t_otros_serv_munic.en_eolica AS EN_EOLICA,eiel_t_otros_serv_munic.kw_eolica AS KW_EOLICA, eiel_t_otros_serv_munic.en_solar AS EN_SOLAR,
 		eiel_t_otros_serv_munic.kw_eolica AS KW_SOLAR,eiel_t_otros_serv_munic.pl_mareo AS PL_MAREO,eiel_t_otros_serv_munic.kw_mareo AS KW_MAREO,
 		eiel_t_otros_serv_munic.ot_energ AS OT_ENERG,eiel_t_otros_serv_munic.kw_ot_energ AS KW_ENERG, eiel_t_otros_serv_munic.cob_serv_tlf_m AS COB_SERV_TELF_M,
 		eiel_t_otros_serv_munic.tv_dig_cable AS TV_DIG_CABLE
 		FROM eiel_t_otros_serv_munic
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_otros_serv_munic.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_otros_serv_munic.codmunic
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_otros_serv_munic.revision_expirada=9999999999;
	  
 ALTER TABLE v_OT_SERV_MUNICIPAL OWNER TO geopista;
 


CREATE OR REPLACE VIEW v_MUN_ENC_DIS AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_mun_diseminados.codprov AS PROVINCIA, eiel_t_mun_diseminados.codmunic AS MUNICIPIO, eiel_t_mun_diseminados.padron_dis AS PADRON,
 		eiel_t_mun_diseminados.pob_estaci AS POB_ESTACI,eiel_t_mun_diseminados.viv_total AS VIV_TOTAL,eiel_t_mun_diseminados.hoteles AS HOTELES,
 		eiel_t_mun_diseminados.casas_rural AS CASAS_RURA,eiel_t_mun_diseminados.longitud AS LONGITUD,eiel_t_mun_diseminados.aag_v_cone AS AAG_V_CONE,
 		eiel_t_mun_diseminados.aag_v_ncon AS AAG_V_NCON,eiel_t_mun_diseminados.aag_c_invi AS AAG_C_INVI,eiel_t_mun_diseminados.aag_c_vera AS AAG_C_VERA,
 		eiel_t_mun_diseminados.aag_v_expr AS AAG_V_EXPR,eiel_t_mun_diseminados.aag_v_depr AS AAG_V_DEPR,eiel_t_mun_diseminados.aag_l_defi AS AAG_L_DEFI,
 		eiel_t_mun_diseminados.aag_v_defi AS AAG_V_DEFI,eiel_t_mun_diseminados.aag_pr_def AS AAG_PR_DEF,eiel_t_mun_diseminados.aag_pe_def AS AAG_PE_DEF,
 		eiel_t_mun_diseminados.aau_vivien AS AAU_VIVIEN,eiel_t_mun_diseminados.aau_pob_re AS AAU_POB_RE,eiel_t_mun_diseminados.aau_pob_es AS AAU_POB_ES,
 		eiel_t_mun_diseminados.aau_def_vi AS AAU_DEF_VI,eiel_t_mun_diseminados.aau_def_re AS AAU_DEF_RE,eiel_t_mun_diseminados.aau_def_es AS AAU_DEF_ES,
 		eiel_t_mun_diseminados.aau_fecont AS AAU_FECONT,eiel_t_mun_diseminados.aau_fencon AS AAU_FENCON,eiel_t_mun_diseminados.longi_ramal AS LONGIT_RAM,
 		eiel_t_mun_diseminados.syd_v_cone AS SYD_V_CONE,eiel_t_mun_diseminados.syd_v_ncon AS SYD_V_NCON,eiel_t_mun_diseminados.syd_l_defi AS SYD_L_DEFI,
 		eiel_t_mun_diseminados.syd_v_defi AS SYD_V_DEFI,eiel_t_mun_diseminados.syd_pr_def AS SYD_PR_DEF,eiel_t_mun_diseminados.syd_pe_def AS SYD_PE_DEF,
 		eiel_t_mun_diseminados.syd_c_desa AS SYD_C_DESA,eiel_t_mun_diseminados.syd_c_trat AS SYD_C_TRAT,eiel_t_mun_diseminados.sau_vivien AS SAU_VIVIEN,
 		eiel_t_mun_diseminados.sau_pob_re AS SAU_POB_RE,eiel_t_mun_diseminados.sau_pob_es AS SAU_POB_ES,eiel_t_mun_diseminados.sau_vi_def AS SAU_VI_DEF,
 		eiel_t_mun_diseminados.sau_pob_re_def AS SAU_RE_DEF,eiel_t_mun_diseminados.sau_pob_es_def AS SAU_ES_DEF,eiel_t_mun_diseminados.produ_basu AS PRODU_BASU,
 		eiel_t_mun_diseminados.contenedores AS CONTENEDOR,eiel_t_mun_diseminados.rba_v_sser AS RBA_V_SSER,eiel_t_mun_diseminados.rba_pr_sse AS RBA_PR_SSE,
 		eiel_t_mun_diseminados.rba_pe_sse AS RBA_PE_SSE,eiel_t_mun_diseminados.rba_plalim AS RBA_PLALIM,eiel_t_mun_diseminados.puntos_luz AS PUNTOS_LUZ,
 		eiel_t_mun_diseminados.alu_v_sin AS ALU_V_SIN,eiel_t_mun_diseminados.alu_l_sin AS ALU_L_SIN
 		FROM eiel_t_mun_diseminados
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_mun_diseminados.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_mun_diseminados.codmunic
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_mun_diseminados.revision_expirada=9999999999
  		ORDER BY eiel_t_mun_diseminados.codprov,eiel_t_mun_diseminados.codmunic;

		  
 ALTER TABLE v_MUN_ENC_DIS OWNER TO geopista;


------TABLA DE CABILDO, ENTIDAD SINGULAR, POBLAMIENTO Y NÚCLEO ABANDONADO


CREATE OR REPLACE VIEW v_CABILDO_CONSEJO AS 
 SELECT codprov AS PROVINCIA,cod_isla AS ISLA,denominacion AS DENOMINACI
 FROM eiel_t_cabildo_consejo
WHERE eiel_t_cabildo_consejo.revision_expirada=9999999999;

 ALTER TABLE v_CABILDO_CONSEJO OWNER TO geopista;
 

CREATE OR REPLACE VIEW v_ENTIDAD_SINGULAR AS 
 SELECT 
 --fase AS FASE,
 eiel_t_entidad_singular.codprov AS PROVINCIA,eiel_t_entidad_singular.codmunic AS MUNICIPIO,eiel_t_entidad_singular.codentidad AS ENTIDAD,eiel_t_entidad_singular.denominacion AS DENOMINACI
 FROM eiel_t_entidad_singular
 WHERE eiel_t_entidad_singular.revision_expirada=9999999999;

 ALTER TABLE v_ENTIDAD_SINGULAR OWNER TO geopista;
 
 CREATE OR REPLACE VIEW v_POBLAMIENTO AS 
 SELECT eiel_t_poblamiento.codprov AS provincia, eiel_t_poblamiento.codmunic AS municipio, eiel_t_poblamiento.codentidad AS entidad, eiel_t_poblamiento.codpoblamiento AS poblamiento
   FROM eiel_t_poblamiento
  WHERE eiel_t_poblamiento.revision_expirada = 9999999999::bigint::numeric
	ORDER BY provincia,municipio, entidad, poblamiento;
 ALTER TABLE v_POBLAMIENTO OWNER TO geopista;


CREATE OR REPLACE VIEW v_NUC_ABANDONADO AS 
 SELECT 
 		--fase AS FASE,
 		eiel_t_nucleo_aband.codprov AS PROVINCIA, eiel_t_nucleo_aband.codmunic AS MUNICIPIO, eiel_t_nucleo_aband.codentidad AS ENTIDAD,
 		eiel_t_nucleo_aband.codpoblamiento AS POBLAMIENT,
 		eiel_t_nucleo_aband.a_abandono AS A_ABANDONO,eiel_t_nucleo_aband.causa_abandono AS CAUSA_ABAN , eiel_t_nucleo_aband.titular_abandono AS TITULAR_AB,
 		eiel_t_nucleo_aband.rehabilitacion AS REHABILITA,
 		eiel_t_nucleo_aband.acceso AS ACCESO_NUC, eiel_t_nucleo_aband.serv_agua AS SERV_AGUA,eiel_t_nucleo_aband.serv_elect AS SERV_ELECT
 		FROM eiel_t_nucleo_aband
		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_nucleo_aband.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_nucleo_aband.codmunic
 		WHERE eiel_t_padron_ttmm.total_poblacion_a1<50000 AND eiel_t_padron_ttmm.revision_expirada=9999999999 AND eiel_t_nucleo_aband.revision_expirada=9999999999;
		  
 ALTER TABLE v_NUC_ABANDONADO OWNER TO geopista;


 
  --Asignacion de permisos al usuario consultas para la generacion de cuadros MPT
grant select on v_alumbrado to consultas ;
grant select on v_cabildo_consejo to consultas ;
grant select on v_cap_agua_nucleo to consultas ;
grant select on v_captacion_agua to consultas ;
grant select on v_captacion_enc to consultas ;
grant select on v_captacion_enc_m50 to consultas ;
grant select on v_carretera to consultas ;
grant select on v_casa_con_uso to consultas ;
grant select on v_casa_consistorial to consultas ;
grant select on v_cementerio to consultas ;
grant select on v_cent_cultural to consultas ;
grant select on v_cent_cultural_usos to consultas ;
grant select on v_centro_asistencial to consultas ;
grant select on v_centro_ensenanza to consultas ;
grant select on v_centro_sanitario to consultas ;
grant select on v_colector to consultas ;
grant select on v_colector_enc to consultas ;
grant select on v_colector_enc_m50 to consultas ;
grant select on v_colector_nucleo to consultas ;
grant select on v_cond_agua_nucleo to consultas ;
grant select on v_conduccion to consultas ;
grant select on v_conduccion_enc to consultas ;
grant select on v_conduccion_enc_m50 to consultas ;
grant select on v_dep_agua_nucleo to consultas ;
grant select on v_deposito to consultas ;
grant select on v_deposito_agua_nucleo to consultas ;
grant select on v_deposito_enc to consultas ;
grant select on v_deposito_enc_m50 to consultas ;
grant select on v_depuradora to consultas ;
grant select on v_depuradora_enc to consultas ;
grant select on v_depuradora_enc_2 to consultas ;
grant select on v_depuradora_enc_2_m50 to consultas ;
grant select on v_depuradora_enc_m50 to consultas ;
grant select on v_edific_pub_sin_uso to consultas ;
grant select on v_emisario to consultas ;
grant select on v_emisario_enc to consultas ;
grant select on v_emisario_enc_m50 to consultas ;
grant select on v_emisario_nucleo to consultas ;
grant select on v_entidad_singular to consultas ;
grant select on v_infraestr_viaria to consultas ;
grant select on v_inst_depor_deporte to consultas ;
grant select on v_instal_deportiva to consultas ;
grant select on v_lonja_merc_feria to consultas ;
grant select on v_matadero to consultas ;
grant select on v_mun_enc_dis to consultas ;
grant select on v_municipio to consultas ;
grant select on v_nivel_ensenanza to consultas ;
grant select on v_nuc_abandonado to consultas ;
grant select on v_nucl_encuestado_1 to consultas ;
grant select on v_nucl_encuestado_2 to consultas ;
grant select on v_nucl_encuestado_3 to consultas ;
grant select on v_nucl_encuestado_4 to consultas ;
grant select on v_nucl_encuestado_5 to consultas ;
grant select on v_nucl_encuestado_6 to consultas ;
grant select on v_nucl_encuestado_7 to consultas ;
grant select on v_nucleo_poblacion to consultas ;
grant select on v_ot_serv_municipal to consultas ;
grant select on v_padron to consultas ;
grant select on v_parque to consultas ;
grant select on v_plan_urbanistico to consultas ;
grant select on v_poblamiento to consultas ;
grant select on v_potabilizacion_enc to consultas ;
grant select on v_potabilizacion_enc_m50 to consultas ;
grant select on v_proteccion_civil to consultas ;
grant select on v_provincia to consultas ;
grant select on v_ramal_saneamiento to consultas ;
grant select on v_recogida_basura to consultas ;
grant select on v_red_distribucion to consultas ;
grant select on v_sanea_autonomo to consultas ;
grant select on v_tanatorio to consultas ;
grant select on v_tra_potabilizacion to consultas ;
grant select on v_tramo_carretera to consultas ;
grant select on v_tramo_colector to consultas ;
grant select on v_tramo_colector_m50 to consultas ;
grant select on v_tramo_conduccion to consultas ;
grant select on v_tramo_conduccion_m50 to consultas ;
grant select on v_tramo_emisario to consultas ;
grant select on v_tramo_emisario_m50 to consultas ;
grant select on v_trat_pota_nucleo to consultas ;
grant select on v_vert_encuestado to consultas ;
grant select on v_vert_encuestado_m50 to consultas ;
grant select on v_vertedero to consultas ;
grant select on v_vertedero_nucleo to consultas ;