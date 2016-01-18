DROP SEQUENCE IF EXISTS seq_version_10237;
CREATE SEQUENCE seq_version_10237
  INCREMENT 1
  MINVALUE 0
  START 1
  CACHE 1;
ALTER TABLE seq_version_10237
  OWNER TO geopista;

SELECT setval('public.seq_version_10237', (select max(revision_actual)::bigint from eiel_c_saneam_ali), true);

DROP SEQUENCE IF EXISTS seq_version_10235;
CREATE SEQUENCE seq_version_10235
  INCREMENT 1
  MINVALUE 0
  START 1
  CACHE 1;
ALTER TABLE seq_version_10235
  OWNER TO geopista;

SELECT setval('public.seq_version_10235', (select max(revision_actual)::bigint from eiel_c_saneam_cb), true);

DROP SEQUENCE IF EXISTS seq_version_10236;
CREATE SEQUENCE seq_version_10236
  INCREMENT 1
  MINVALUE 0
  START 1
  CACHE 1;
ALTER TABLE seq_version_10236
  OWNER TO geopista;

SELECT setval('public.seq_version_10236', (select max(revision_actual)::bigint from eiel_c_abast_cb), true);


--DROP VIEW v_CONDUCCION_ENC_M50;
CREATE OR REPLACE VIEW v_CONDUCCION_ENC_M50 AS 
SELECT
	--fase AS FASE,
	 eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia,
    eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
FROM (eiel_t_abast_tcn LEFT JOIN eiel_t_padron_ttmm ON
    ((((eiel_t_padron_ttmm.codprov)::text = (eiel_t_abast_tcn.codprov)::text)
    AND ((eiel_t_padron_ttmm.codmunic)::text = (eiel_t_abast_tcn.codmunic)::text))))
WHERE (((((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
      eiel_t_abast_tcn.codmunic IN (SELECT cond_munic FROM "public"."v_cond_agua_nucleo")) AND
      eiel_t_abast_tcn.tramo_cn IN (SELECT orden_cond FROM "public"."v_cond_agua_nucleo")) AND
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric)) AND
    (eiel_t_abast_tcn.revision_expirada = (9999999999::bigint)::numeric));

ALTER TABLE v_CONDUCCION_ENC_M50 OWNER TO geopista;


--DROP VIEW v_CAPTACION_ENC_M50;
CREATE OR REPLACE VIEW v_CAPTACION_ENC_M50 AS
SELECT
	--fase AS FASE,
	 eiel_t_abast_ca.clave AS CLAVE,eiel_t_abast_ca.codprov AS PROVINCIA, eiel_t_abast_ca.codmunic AS MUNICIPIO, eiel_t_abast_ca.orden_ca AS ORDEN_CAPT,
 		eiel_t_abast_ca.nombre AS DENOMINACI, eiel_t_abast_ca.tipo AS TIPO_CAPT, eiel_t_abast_ca.titular AS TITULAR, eiel_t_abast_ca.gestor AS GESTION ,eiel_t_abast_ca.sist_impulsion AS SISTEMA_CA,
 		eiel_t_abast_ca.estado AS ESTADO, eiel_t_abast_ca.uso AS USO ,eiel_t_abast_ca.proteccion AS PROTECCION,eiel_t_abast_ca.contador AS CONTADOR
 		FROM eiel_t_abast_ca
 		left join eiel_t_padron_ttmm on eiel_t_padron_ttmm.codprov=eiel_t_abast_ca.codprov AND eiel_t_padron_ttmm.codmunic=eiel_t_abast_ca.codmunic WHERE (((((eiel_t_padron_ttmm.total_poblacion_a1 >= 50000) AND
      eiel_t_abast_ca.codmunic IN (SELECT c_municip FROM "public"."v_cap_agua_nucleo")) AND
	  eiel_t_abast_ca.orden_ca IN (SELECT orden_capt FROM "public"."v_cap_agua_nucleo")) AND
    (eiel_t_abast_ca.revision_expirada = (9999999999::bigint)::numeric)) AND
    (eiel_t_padron_ttmm.revision_expirada = (9999999999::bigint)::numeric));


ALTER TABLE v_CAPTACION_ENC_M50 OWNER TO geopista;
