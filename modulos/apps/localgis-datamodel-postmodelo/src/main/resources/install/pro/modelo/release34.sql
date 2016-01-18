
DROP VIEW IF EXISTS v_tramo_carretera;
DROP VIEW IF EXISTS check_mpt_tramo_carretera;

ALTER TABLE eiel_c_tramos_carreteras ALTER COLUMN longitud TYPE numeric(8,1);
ALTER TABLE eiel_c_tramos_carreteras ALTER COLUMN longitud2 TYPE numeric(8,1);

CREATE OR REPLACE VIEW v_tramo_carretera AS 
 SELECT DISTINCT ON (eiel_c_tramos_carreteras.codprov, eiel_c_tramos_carreteras.codmunic, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki) eiel_c_tramos_carreteras.codprov AS provincia, eiel_c_tramos_carreteras.codmunic AS municipio, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki AS pk_inicial, eiel_c_tramos_carreteras.pkf AS pk_final, carreteras.titular_via AS titular, eiel_c_tramos_carreteras.gestor AS gestion, eiel_c_tramos_carreteras.senaliza, eiel_c_tramos_carreteras.firme, eiel_c_tramos_carreteras.estado, eiel_c_tramos_carreteras.ancho, eiel_c_tramos_carreteras.longitud, eiel_c_tramos_carreteras.paso_nivel AS pasos_nive, eiel_c_tramos_carreteras.dimensiona, eiel_c_tramos_carreteras.muy_sinuoso AS muy_sinuos, eiel_c_tramos_carreteras.pte_excesiva AS pte_excesi, eiel_c_tramos_carreteras.fre_estrech AS fre_estrec
   FROM eiel_c_tramos_carreteras
   LEFT JOIN eiel_t_carreteras carreteras ON eiel_c_tramos_carreteras.codprov::text = carreteras.codprov::text AND eiel_c_tramos_carreteras.cod_carrt::text = carreteras.cod_carrt::text AND carreteras.revision_expirada = 9999999999::bigint::numeric
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_tramos_carreteras.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_tramos_carreteras.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_tramos_carreteras.revision_expirada = 9999999999::bigint::numeric
  ORDER BY eiel_c_tramos_carreteras.codprov, eiel_c_tramos_carreteras.codmunic, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki;

ALTER TABLE v_tramo_carretera
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_carretera TO geopista;
GRANT SELECT ON TABLE v_tramo_carretera TO consultas;
