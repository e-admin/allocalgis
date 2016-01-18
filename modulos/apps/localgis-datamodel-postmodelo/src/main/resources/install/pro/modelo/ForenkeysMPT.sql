

CREATE OR REPLACE VIEW check_mpt_infraestr_viaria AS 
 SELECT DISTINCT ON (eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado) eiel_c_redviaria_tu.codprov AS provincia, eiel_c_redviaria_tu.codmunic AS municipio, eiel_c_redviaria_tu.codentidad AS entidad, eiel_c_redviaria_tu.codpoblamiento AS poblamient, eiel_c_redviaria_tu.tipo AS tipo_infr, eiel_c_redviaria_tu.estado, sum(eiel_c_redviaria_tu.longitud) AS longitud, sum(eiel_c_redviaria_tu.superficie) AS superficie, eiel_c_redviaria_tu.viviendas_afec AS viv_afecta
   FROM eiel_c_redviaria_tu
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_redviaria_tu.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_redviaria_tu.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_redviaria_tu.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado, eiel_c_redviaria_tu.viviendas_afec
  EXCEPT
  SELECT DISTINCT ON (eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado) eiel_c_redviaria_tu.codprov AS provincia, eiel_c_redviaria_tu.codmunic AS municipio, eiel_c_redviaria_tu.codentidad AS entidad, eiel_c_redviaria_tu.codpoblamiento AS poblamient, eiel_c_redviaria_tu.tipo AS tipo_infr, eiel_c_redviaria_tu.estado, sum(eiel_c_redviaria_tu.longitud) AS longitud, sum(eiel_c_redviaria_tu.superficie) AS superficie, eiel_c_redviaria_tu.viviendas_afec AS viv_afecta
   FROM eiel_c_redviaria_tu
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_redviaria_tu.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_redviaria_tu.codmunic::text
   RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_c_redviaria_tu.codprov AND eiel_t_nucl_encuest_1.codmunic=eiel_c_redviaria_tu.codmunic AND eiel_t_nucl_encuest_1.codentidad=eiel_c_redviaria_tu.codentidad AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_c_redviaria_tu.codpoblamiento
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_c_redviaria_tu.codprov AND eiel_t_nucl_encuest_2.codmunic=eiel_c_redviaria_tu.codmunic AND eiel_t_nucl_encuest_2.codentidad=eiel_c_redviaria_tu.codentidad AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_c_redviaria_tu.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_redviaria_tu.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado, eiel_c_redviaria_tu.viviendas_afec;
 
ALTER TABLE check_mpt_infraestr_viaria OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_infraestr_viaria TO geopista;
GRANT SELECT ON TABLE check_mpt_infraestr_viaria TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Infraestructuras viarias no referenciadas con ningún núcleo encuestado') THEN	
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Infraestructuras viarias no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Infraestructuras viarias no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Infraestructuras viarias no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Infraestructuras viarias no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Infraestructuras viarias no referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ALUM_RECO_INFR_RE','check_infraestr_viaria_reference_nucl_encuestado','check_mpt_infraestr_viaria',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO1";





CREATE OR REPLACE VIEW check_mpt_alumbrado AS 
 SELECT eiel_c_alum_pl.codprov AS provincia, eiel_c_alum_pl.codmunic AS municipio, eiel_c_alum_pl.codentidad AS entidad, eiel_c_alum_pl.codpoblamiento AS nucleo, eiel_c_alum_pl.ah_ener_rfl AS ah_ener_rl, eiel_c_alum_pl.ah_ener_rfi AS ah_ener_ri, eiel_c_alum_pl.estado AS calidad, eiel_c_alum_pl.potencia AS pot_instal, count(*) AS n_puntos
   FROM eiel_c_alum_pl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_alum_pl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_alum_pl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_alum_pl.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_alum_pl.codmunic, eiel_c_alum_pl.codentidad, eiel_c_alum_pl.codpoblamiento, eiel_c_alum_pl.codprov, eiel_c_alum_pl.ah_ener_rfl, eiel_c_alum_pl.ah_ener_rfi, eiel_c_alum_pl.estado, eiel_c_alum_pl.potencia
  EXCEPT
  SELECT eiel_c_alum_pl.codprov AS provincia, eiel_c_alum_pl.codmunic AS municipio, eiel_c_alum_pl.codentidad AS entidad, eiel_c_alum_pl.codpoblamiento AS nucleo, eiel_c_alum_pl.ah_ener_rfl AS ah_ener_rl, eiel_c_alum_pl.ah_ener_rfi AS ah_ener_ri, eiel_c_alum_pl.estado AS calidad, eiel_c_alum_pl.potencia AS pot_instal, count(*) AS n_puntos
   FROM eiel_c_alum_pl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_alum_pl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_alum_pl.codmunic::text
   RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_c_alum_pl.codprov AND eiel_t_nucl_encuest_1.codmunic=eiel_c_alum_pl.codmunic AND eiel_t_nucl_encuest_1.codentidad=eiel_c_alum_pl.codentidad AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_c_alum_pl.codpoblamiento
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_c_alum_pl.codprov AND eiel_t_nucl_encuest_2.codmunic=eiel_c_alum_pl.codmunic AND eiel_t_nucl_encuest_2.codentidad=eiel_c_alum_pl.codentidad AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_c_alum_pl.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_alum_pl.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_alum_pl.codmunic, eiel_c_alum_pl.codentidad, eiel_c_alum_pl.codpoblamiento, eiel_c_alum_pl.codprov, eiel_c_alum_pl.ah_ener_rfl, eiel_c_alum_pl.ah_ener_rfi, eiel_c_alum_pl.estado, eiel_c_alum_pl.potencia;
 
ALTER TABLE check_mpt_alumbrado OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_alumbrado TO geopista;
GRANT SELECT ON TABLE check_mpt_alumbrado TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Alumbrados no referenciados con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Alumbrados no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Alumbrados no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Alumbrados no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Alumbrados no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Alumbrados no referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ALUM_RECO_INFR_RE','check_alumbrado_reference_nucl_encuestado','check_mpt_alumbrado',currval('seq_dictionary'));

	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO2";



CREATE OR REPLACE VIEW check_mpt_recogida_basura AS 
 SELECT eiel_t_rb.codprov AS provincia, eiel_t_rb.codmunic AS municipio, eiel_t_rb.codentidad AS entidad, eiel_t_rb.codpoblamiento AS nucleo, eiel_t_rb.tipo AS tipo_rbas, eiel_t_rb.gestor AS gestion, eiel_t_rb.periodicidad AS periodicid, eiel_t_rb.calidad, eiel_t_rb.tm_res_urb AS produ_basu, eiel_t_rb.n_contenedores AS contenedor
   FROM eiel_t_rb
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_rb.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_rb.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_rb.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_rb.codprov AS provincia, eiel_t_rb.codmunic AS municipio, eiel_t_rb.codentidad AS entidad, eiel_t_rb.codpoblamiento AS nucleo, eiel_t_rb.tipo AS tipo_rbas, eiel_t_rb.gestor AS gestion, eiel_t_rb.periodicidad AS periodicid, eiel_t_rb.calidad, eiel_t_rb.tm_res_urb AS produ_basu, eiel_t_rb.n_contenedores AS contenedor
   FROM eiel_t_rb
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_rb.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_rb.codmunic::text
   RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_t_rb.codprov AND eiel_t_nucl_encuest_1.codmunic=eiel_t_rb.codmunic AND eiel_t_nucl_encuest_1.codentidad=eiel_t_rb.codentidad AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_t_rb.codpoblamiento
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_t_rb.codprov AND eiel_t_nucl_encuest_2.codmunic=eiel_t_rb.codmunic AND eiel_t_nucl_encuest_2.codentidad=eiel_t_rb.codentidad AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_t_rb.codpoblamiento
   WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_rb.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_recogida_basura OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_recogida_basura TO geopista;
GRANT SELECT ON TABLE check_mpt_recogida_basura TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Recogidas de Basura no referenciadas con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Recogidas de Basura no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Recogidas de Basura no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Recogidas de Basura no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Recogidas de Basura no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Recogidas de Basura no referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ALUM_RECO_INFR_RE','check_recogida_basura_reference_nucl_encuestado','check_mpt_recogida_basura',currval('seq_dictionary'));

	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO3";



CREATE OR REPLACE VIEW check_mpt_red_distribucion AS 
 SELECT eiel_c_abast_rd.codprov AS provincia, eiel_c_abast_rd.codmunic AS municipio, eiel_c_abast_rd.codentidad AS entidad, eiel_c_abast_rd.codpoblamiento AS nucleo, eiel_c_abast_rd.material AS tipo_rdis, eiel_c_abast_rd.sist_trans, eiel_c_abast_rd.estado, eiel_c_abast_rd.titular, eiel_c_abast_rd.gestor AS gestion, sum(eiel_c_abast_rd.longitud) AS longitud
   FROM eiel_c_abast_rd
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_rd.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_rd.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_abast_rd.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_abast_rd.codprov, eiel_c_abast_rd.codmunic, eiel_c_abast_rd.codentidad, eiel_c_abast_rd.codpoblamiento, eiel_c_abast_rd.material, eiel_c_abast_rd.sist_trans, eiel_c_abast_rd.estado, eiel_c_abast_rd.titular, eiel_c_abast_rd.gestor
EXCEPT
 SELECT eiel_c_abast_rd.codprov AS provincia, eiel_c_abast_rd.codmunic AS municipio, eiel_c_abast_rd.codentidad AS entidad, eiel_c_abast_rd.codpoblamiento AS nucleo, eiel_c_abast_rd.material AS tipo_rdis, eiel_c_abast_rd.sist_trans, eiel_c_abast_rd.estado, eiel_c_abast_rd.titular, eiel_c_abast_rd.gestor AS gestion, sum(eiel_c_abast_rd.longitud) AS longitud
   FROM eiel_c_abast_rd
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_rd.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_rd.codmunic::text
  RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_c_abast_rd.codprov AND eiel_t_nucl_encuest_1.codmunic=eiel_c_abast_rd.codmunic AND eiel_t_nucl_encuest_1.codentidad=eiel_c_abast_rd.codentidad AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_c_abast_rd.codpoblamiento
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_c_abast_rd.codprov AND eiel_t_nucl_encuest_2.codmunic=eiel_c_abast_rd.codmunic AND eiel_t_nucl_encuest_2.codentidad=eiel_c_abast_rd.codentidad AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_c_abast_rd.codpoblamiento
   WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_abast_rd.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_abast_rd.codprov, eiel_c_abast_rd.codmunic, eiel_c_abast_rd.codentidad, eiel_c_abast_rd.codpoblamiento, eiel_c_abast_rd.material, eiel_c_abast_rd.sist_trans, eiel_c_abast_rd.estado, eiel_c_abast_rd.titular, eiel_c_abast_rd.gestor;

ALTER TABLE check_mpt_red_distribucion OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_red_distribucion TO geopista;
GRANT SELECT ON TABLE check_mpt_red_distribucion TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Redes de Distribución no referenciadas con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Redes de Distribución no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Redes de Distribución no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Redes de Distribución no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Redes de Distribución no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Redes de Distribución no referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ALUM_RECO_INFR_RE','check_red_distribucion_reference_nucl_encuestado','check_mpt_red_distribucion',currval('seq_dictionary'));

	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO3";




CREATE OR REPLACE VIEW check_mpt_cap_agua AS 
 SELECT eiel_t_abast_ca.clave, eiel_t_abast_ca.codprov AS provincia, eiel_t_abast_ca.codmunic AS municipio, eiel_t_abast_ca.orden_ca AS orden_capt
   FROM eiel_t_abast_ca
  WHERE eiel_t_abast_ca.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_abast_ca.clave, eiel_t_abast_ca.codprov AS provincia, eiel_t_abast_ca.codmunic AS municipio, eiel_t_abast_ca.orden_ca AS orden_capt
   FROM eiel_t_abast_ca
   RIGHT JOIN eiel_c_municipios on eiel_t_abast_ca.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t_abast_ca.codmunic
  WHERE eiel_t_abast_ca.revision_expirada = 9999999999::bigint::numeric;
  
ALTER TABLE check_mpt_cap_agua OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_cap_agua TO geopista;
GRANT SELECT ON TABLE check_mpt_cap_agua TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Captaciones no referenciadas con ninguna provincia ni municipio') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Captaciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Captaciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Captaciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Captaciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Captaciones no referenciadas con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CAPTACIONES','check_cap_agua_reference_provincia','check_mpt_cap_agua',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO4";



CREATE OR REPLACE VIEW check_mpt_cap_agua_nucleo AS 
 SELECT eiel_tr_abast_ca_pobl.codprov_pobl AS provincia, eiel_tr_abast_ca_pobl.codmunic_pobl AS municipio, eiel_tr_abast_ca_pobl.codentidad_pobl AS entidad, eiel_tr_abast_ca_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_ca_pobl.clave_ca AS clave, eiel_tr_abast_ca_pobl.codprov_ca AS c_provinc, eiel_tr_abast_ca_pobl.codmunic_ca AS c_municip, eiel_tr_abast_ca_pobl.orden_ca AS orden_capt
   FROM eiel_tr_abast_ca_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_ca_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_ca_pobl.codmunic_pobl::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_ca_pobl.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_tr_abast_ca_pobl.codprov_pobl AS provincia, eiel_tr_abast_ca_pobl.codmunic_pobl AS municipio, eiel_tr_abast_ca_pobl.codentidad_pobl AS entidad, eiel_tr_abast_ca_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_ca_pobl.clave_ca AS clave, eiel_tr_abast_ca_pobl.codprov_ca AS c_provinc, eiel_tr_abast_ca_pobl.codmunic_ca AS c_municip, eiel_tr_abast_ca_pobl.orden_ca AS orden_capt
   FROM eiel_tr_abast_ca_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_ca_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_ca_pobl.codmunic_pobl::text
   RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_tr_abast_ca_pobl.codprov_pobl AND eiel_t_nucl_encuest_1.codmunic=eiel_tr_abast_ca_pobl.codmunic_pobl AND eiel_t_nucl_encuest_1.codentidad=eiel_tr_abast_ca_pobl.codentidad_pobl AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_tr_abast_ca_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_tr_abast_ca_pobl.codprov_pobl AND eiel_t_nucl_encuest_2.codmunic=eiel_tr_abast_ca_pobl.codmunic_pobl AND eiel_t_nucl_encuest_2.codentidad=eiel_tr_abast_ca_pobl.codentidad_pobl AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_tr_abast_ca_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_abast_ca on eiel_t_abast_ca.codprov=eiel_tr_abast_ca_pobl.codprov_ca AND eiel_t_abast_ca.codmunic=eiel_tr_abast_ca_pobl.codmunic_ca AND eiel_t_abast_ca.orden_ca=eiel_tr_abast_ca_pobl.orden_ca
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_ca_pobl.revision_expirada = 9999999999::bigint::numeric;


  
ALTER TABLE check_mpt_cap_agua_nucleo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_cap_agua_nucleo TO geopista;
GRANT SELECT ON TABLE check_mpt_cap_agua_nucleo TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'No existen como captaciones o no están referenciadas con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como captaciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como captaciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como captaciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como captaciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como captaciones o no están referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CAPTACIONES','check_cap_agua_nucleo_reference_nuc_encuestado','check_mpt_cap_agua_nucleo',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO5";






CREATE OR REPLACE VIEW check_mpt_mun_enc_dis AS 
 SELECT eiel_t_mun_diseminados.codprov AS provincia, eiel_t_mun_diseminados.codmunic AS municipio, eiel_t_mun_diseminados.padron_dis AS padron, eiel_t_mun_diseminados.pob_estaci, eiel_t_mun_diseminados.viv_total, eiel_t_mun_diseminados.hoteles, eiel_t_mun_diseminados.casas_rural AS casas_rura, eiel_t_mun_diseminados.longitud, eiel_t_mun_diseminados.aag_v_cone, eiel_t_mun_diseminados.aag_v_ncon, eiel_t_mun_diseminados.aag_c_invi, eiel_t_mun_diseminados.aag_c_vera, eiel_t_mun_diseminados.aag_v_expr, eiel_t_mun_diseminados.aag_v_depr, eiel_t_mun_diseminados.aag_l_defi, eiel_t_mun_diseminados.aag_v_defi, eiel_t_mun_diseminados.aag_pr_def, eiel_t_mun_diseminados.aag_pe_def, eiel_t_mun_diseminados.aau_vivien, eiel_t_mun_diseminados.aau_pob_re, eiel_t_mun_diseminados.aau_pob_es, eiel_t_mun_diseminados.aau_def_vi, eiel_t_mun_diseminados.aau_def_re, eiel_t_mun_diseminados.aau_def_es, eiel_t_mun_diseminados.aau_fecont, eiel_t_mun_diseminados.aau_fencon, eiel_t_mun_diseminados.longi_ramal AS longit_ram, eiel_t_mun_diseminados.syd_v_cone, eiel_t_mun_diseminados.syd_v_ncon, eiel_t_mun_diseminados.syd_l_defi, eiel_t_mun_diseminados.syd_v_defi, eiel_t_mun_diseminados.syd_pr_def, eiel_t_mun_diseminados.syd_pe_def, eiel_t_mun_diseminados.syd_c_desa, eiel_t_mun_diseminados.syd_c_trat, eiel_t_mun_diseminados.sau_vivien, eiel_t_mun_diseminados.sau_pob_re, eiel_t_mun_diseminados.sau_pob_es, eiel_t_mun_diseminados.sau_vi_def, eiel_t_mun_diseminados.sau_pob_re_def AS sau_re_def, eiel_t_mun_diseminados.sau_pob_es_def AS sau_es_def, eiel_t_mun_diseminados.produ_basu, eiel_t_mun_diseminados.contenedores AS contenedor, eiel_t_mun_diseminados.rba_v_sser, eiel_t_mun_diseminados.rba_pr_sse, eiel_t_mun_diseminados.rba_pe_sse, eiel_t_mun_diseminados.rba_plalim, eiel_t_mun_diseminados.puntos_luz, eiel_t_mun_diseminados.alu_v_sin, eiel_t_mun_diseminados.alu_l_sin
   FROM eiel_t_mun_diseminados
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_mun_diseminados.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_mun_diseminados.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_mun_diseminados.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_mun_diseminados.codprov AS provincia, eiel_t_mun_diseminados.codmunic AS municipio, eiel_t_mun_diseminados.padron_dis AS padron, eiel_t_mun_diseminados.pob_estaci, eiel_t_mun_diseminados.viv_total, eiel_t_mun_diseminados.hoteles, eiel_t_mun_diseminados.casas_rural AS casas_rura, eiel_t_mun_diseminados.longitud, eiel_t_mun_diseminados.aag_v_cone, eiel_t_mun_diseminados.aag_v_ncon, eiel_t_mun_diseminados.aag_c_invi, eiel_t_mun_diseminados.aag_c_vera, eiel_t_mun_diseminados.aag_v_expr, eiel_t_mun_diseminados.aag_v_depr, eiel_t_mun_diseminados.aag_l_defi, eiel_t_mun_diseminados.aag_v_defi, eiel_t_mun_diseminados.aag_pr_def, eiel_t_mun_diseminados.aag_pe_def, eiel_t_mun_diseminados.aau_vivien, eiel_t_mun_diseminados.aau_pob_re, eiel_t_mun_diseminados.aau_pob_es, eiel_t_mun_diseminados.aau_def_vi, eiel_t_mun_diseminados.aau_def_re, eiel_t_mun_diseminados.aau_def_es, eiel_t_mun_diseminados.aau_fecont, eiel_t_mun_diseminados.aau_fencon, eiel_t_mun_diseminados.longi_ramal AS longit_ram, eiel_t_mun_diseminados.syd_v_cone, eiel_t_mun_diseminados.syd_v_ncon, eiel_t_mun_diseminados.syd_l_defi, eiel_t_mun_diseminados.syd_v_defi, eiel_t_mun_diseminados.syd_pr_def, eiel_t_mun_diseminados.syd_pe_def, eiel_t_mun_diseminados.syd_c_desa, eiel_t_mun_diseminados.syd_c_trat, eiel_t_mun_diseminados.sau_vivien, eiel_t_mun_diseminados.sau_pob_re, eiel_t_mun_diseminados.sau_pob_es, eiel_t_mun_diseminados.sau_vi_def, eiel_t_mun_diseminados.sau_pob_re_def AS sau_re_def, eiel_t_mun_diseminados.sau_pob_es_def AS sau_es_def, eiel_t_mun_diseminados.produ_basu, eiel_t_mun_diseminados.contenedores AS contenedor, eiel_t_mun_diseminados.rba_v_sser, eiel_t_mun_diseminados.rba_pr_sse, eiel_t_mun_diseminados.rba_pe_sse, eiel_t_mun_diseminados.rba_plalim, eiel_t_mun_diseminados.puntos_luz, eiel_t_mun_diseminados.alu_v_sin, eiel_t_mun_diseminados.alu_l_sin
   FROM eiel_t_mun_diseminados
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_mun_diseminados.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_mun_diseminados.codmunic::text
   LEFT JOIN eiel_c_municipios on eiel_t_mun_diseminados.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t_mun_diseminados.codmunic
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_mun_diseminados.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_mun_enc_dis OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_mun_enc_dis TO geopista;
GRANT SELECT ON TABLE check_mpt_mun_enc_dis TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Municipios encuestados no referenciados con ningún municipio') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Municipios encuestados no referenciados con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Municipios encuestados no referenciados con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Municipios encuestados no referenciados con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Municipios encuestados no referenciados con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Municipios encuestados no referenciados con ningún municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CARRETERAS','check_mun_enc_dis_reference_municipio','check_mpt_mun_enc_dis',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO6";




CREATE OR REPLACE VIEW check_mpt_carretera AS 
 SELECT DISTINCT ON (eiel_t_carreteras.codprov , eiel_t_carreteras.cod_carrt) eiel_t_carreteras.codprov AS provincia, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion AS denominaci
   FROM eiel_t_carreteras
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_carreteras.codprov::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999
  AND eiel_t_carreteras.revision_expirada = 9999999999
  GROUP BY eiel_t_carreteras.codprov , eiel_t_carreteras.cod_carrt ,eiel_t_carreteras.denominacion 
  EXCEPT
 SELECT DISTINCT ON (eiel_t_carreteras.codprov , eiel_t_carreteras.cod_carrt)eiel_t_carreteras.codprov AS provincia, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion AS denominaci
   FROM eiel_t_carreteras
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_carreteras.codprov::text
  LEFT JOIN eiel_c_provincia on eiel_t_carreteras.codprov=eiel_c_provincia.codprov 
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_carreteras.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_carreteras.codprov , eiel_t_carreteras.cod_carrt ,eiel_t_carreteras.denominacion ;

ALTER TABLE check_mpt_carretera OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_carretera TO geopista;
GRANT SELECT ON TABLE check_mpt_carretera TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Carreteras no referenciadas con ninguna provincia') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Carreteras no referenciadas con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Carreteras no referenciadas con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Carreteras no referenciadas con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Carreteras no referenciadas con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Carreteras no referenciadas con ninguna provincia');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CARRETERAS','check_carretera_reference_provincia','check_mpt_carretera',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO7";





CREATE OR REPLACE VIEW check_mpt_tramo_carretera AS 
 SELECT DISTINCT ON (eiel_c_tramos_carreteras.codprov, eiel_c_tramos_carreteras.codmunic, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki) eiel_c_tramos_carreteras.codprov AS provincia, eiel_c_tramos_carreteras.codmunic AS municipio, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki AS pk_inicial, eiel_c_tramos_carreteras.pkf AS pk_final, carreteras.titular_via AS titular, eiel_c_tramos_carreteras.gestor AS gestion, eiel_c_tramos_carreteras.senaliza, eiel_c_tramos_carreteras.firme, eiel_c_tramos_carreteras.estado, eiel_c_tramos_carreteras.ancho, eiel_c_tramos_carreteras.longitud, eiel_c_tramos_carreteras.paso_nivel AS pasos_nive, eiel_c_tramos_carreteras.dimensiona, eiel_c_tramos_carreteras.muy_sinuoso AS muy_sinuos, eiel_c_tramos_carreteras.pte_excesiva AS pte_excesi, eiel_c_tramos_carreteras.fre_estrech AS fre_estrec
   FROM eiel_c_tramos_carreteras
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_tramos_carreteras.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_tramos_carreteras.codmunic::text
   RIGHT JOIN eiel_t_carreteras carreteras ON eiel_c_tramos_carreteras.codprov::text = carreteras.codprov::text AND eiel_c_tramos_carreteras.cod_carrt::text = carreteras.cod_carrt::text AND carreteras.revision_expirada = 9999999999::bigint::numeric
  WHERE 
  eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND 
  eiel_c_tramos_carreteras.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT DISTINCT ON (eiel_c_tramos_carreteras.codprov, eiel_c_tramos_carreteras.codmunic, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki) eiel_c_tramos_carreteras.codprov AS provincia, eiel_c_tramos_carreteras.codmunic AS municipio, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki AS pk_inicial, eiel_c_tramos_carreteras.pkf AS pk_final, carreteras.titular_via AS titular, eiel_c_tramos_carreteras.gestor AS gestion, eiel_c_tramos_carreteras.senaliza, eiel_c_tramos_carreteras.firme, eiel_c_tramos_carreteras.estado, eiel_c_tramos_carreteras.ancho, eiel_c_tramos_carreteras.longitud, eiel_c_tramos_carreteras.paso_nivel AS pasos_nive, eiel_c_tramos_carreteras.dimensiona, eiel_c_tramos_carreteras.muy_sinuoso AS muy_sinuos, eiel_c_tramos_carreteras.pte_excesiva AS pte_excesi, eiel_c_tramos_carreteras.fre_estrech AS fre_estrec
   FROM eiel_c_tramos_carreteras
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_tramos_carreteras.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_tramos_carreteras.codmunic::text
   RIGHT JOIN eiel_t_carreteras carreteras ON eiel_c_tramos_carreteras.codprov::text = carreteras.codprov::text AND eiel_c_tramos_carreteras.cod_carrt::text = carreteras.cod_carrt::text AND carreteras.revision_expirada = 9999999999::bigint::numeric
   RIGHT JOIN eiel_t_mun_diseminados on eiel_t_mun_diseminados.codmunic=eiel_c_tramos_carreteras.codmunic
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric 
	AND eiel_c_tramos_carreteras.revision_expirada = 9999999999::bigint::numeric;
ALTER TABLE check_mpt_tramo_carretera OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_tramo_carretera TO geopista;
GRANT SELECT ON TABLE check_mpt_tramo_carretera TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Tramos de Carretera no referenciados con ninguna carretera o municipio') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tramos de Carretera no referenciados con ninguna carretera o municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tramos de Carretera no referenciados con ninguna carretera o municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tramos de Carretera no referenciados con ninguna carretera o municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tramos de Carretera no referenciados con ninguna carretera o municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tramos de Carretera no referenciados con ninguna carretera o municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CARRETERAS','check_tramo_carretera_reference_provincia','check_mpt_tramo_carretera',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO8";





CREATE OR REPLACE VIEW check_mpt_centro_asistencial AS 
 SELECT eiel_t_as.clave, eiel_t_as.codprov AS provincia, eiel_t_as.codmunic AS municipio, eiel_t_as.codentidad AS entidad, eiel_t_as.codpoblamiento AS poblamient, eiel_t_as.orden_as AS orden_casi, eiel_t_as.nombre, eiel_t_as.tipo AS tipo_casis, eiel_t_as.titular, eiel_t_as.gestor AS gestion, eiel_t_as.plazas, eiel_t_as.s_cubierta AS s_cubi, eiel_t_as.s_aire, eiel_t_as.s_solar AS s_sola, eiel_t_as.acceso_s_ruedas, eiel_t_as.estado
   FROM eiel_t_as
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_as.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_as.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_as.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_as.clave, eiel_t_as.codprov AS provincia, eiel_t_as.codmunic AS municipio, eiel_t_as.codentidad AS entidad, eiel_t_as.codpoblamiento AS poblamient, eiel_t_as.orden_as AS orden_casi, eiel_t_as.nombre, eiel_t_as.tipo AS tipo_casis, eiel_t_as.titular, eiel_t_as.gestor AS gestion, eiel_t_as.plazas, eiel_t_as.s_cubierta AS s_cubi, eiel_t_as.s_aire, eiel_t_as.s_solar AS s_sola, eiel_t_as.acceso_s_ruedas, eiel_t_as.estado
   FROM eiel_t_as
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_as.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_as.codmunic::text
   RIGHT JOIN eiel_t_poblamiento ON   eiel_t_as.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_as.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_as.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_as.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_as.revision_expirada = 9999999999::bigint::numeric;
ALTER TABLE check_mpt_centro_asistencial OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_centro_asistencial TO geopista;
GRANT SELECT ON TABLE check_mpt_centro_asistencial TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Centros asistenciales no referenciados con ningún poblamiento') THEN	
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros asistenciales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros asistenciales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros asistenciales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros asistenciales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros asistenciales no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_centro_asistencial_reference_poblamiento','check_mpt_centro_asistencial',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO9";



CREATE OR REPLACE VIEW check_mpt_centro_sanitario AS 
 SELECT eiel_t_sa.clave, eiel_t_sa.codprov AS provincia, eiel_t_sa.codmunic AS municipio, eiel_t_sa.codentidad AS entidad, eiel_t_sa.codpoblamiento AS poblamient, eiel_t_sa.orden_sa AS orden_csan, eiel_t_sa.nombre, eiel_t_sa.tipo AS tipo_csan, eiel_t_sa.titular, eiel_t_sa.gestor AS gestion, eiel_t_sa.s_cubierta AS s_cubi, eiel_t_sa.s_aire, eiel_t_sa.s_solar AS s_sola, eiel_t_sa.uci, eiel_t_sa.n_camas AS camas, eiel_t_sa.acceso_s_ruedas, eiel_t_sa.estado
   FROM eiel_t_sa
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_sa.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_sa.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_sa.revision_expirada = 9999999999::bigint::numeric
 EXCEPT
 SELECT eiel_t_sa.clave, eiel_t_sa.codprov AS provincia, eiel_t_sa.codmunic AS municipio, eiel_t_sa.codentidad AS entidad, eiel_t_sa.codpoblamiento AS poblamient, eiel_t_sa.orden_sa AS orden_csan, eiel_t_sa.nombre, eiel_t_sa.tipo AS tipo_csan, eiel_t_sa.titular, eiel_t_sa.gestor AS gestion, eiel_t_sa.s_cubierta AS s_cubi, eiel_t_sa.s_aire, eiel_t_sa.s_solar AS s_sola, eiel_t_sa.uci, eiel_t_sa.n_camas AS camas, eiel_t_sa.acceso_s_ruedas, eiel_t_sa.estado
   FROM eiel_t_sa
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_sa.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_sa.codmunic::text
 RIGHT JOIN eiel_t_poblamiento ON   eiel_t_sa.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_sa.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_sa.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_sa.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_sa.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_centro_sanitario OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_centro_sanitario TO geopista;
GRANT SELECT ON TABLE check_mpt_centro_sanitario TO consultas;



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Centros sanitarios no referenciados con ningún poblamiento') THEN	
	
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros sanitarios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros sanitarios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros sanitarios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros sanitarios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros sanitarios no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_centro_sanitario_reference_poblamiento','check_mpt_centro_sanitario',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO10";




CREATE OR REPLACE VIEW check_mpt_matadero AS 
 SELECT eiel_t_mt.clave, eiel_t_mt.codprov AS provincia, eiel_t_mt.codmunic AS municipio, eiel_t_mt.codentidad AS entidad, eiel_t_mt.codpoblamiento AS poblamient, eiel_t_mt.orden_mt AS orden_mata, eiel_t_mt.nombre, eiel_t_mt.clase AS clase_mat, eiel_t_mt.titular, eiel_t_mt.gestor AS gestion, eiel_t_mt.s_cubierta AS s_cubi, eiel_t_mt.s_aire, eiel_t_mt.s_solar AS s_sola, eiel_t_mt.acceso_s_ruedas, eiel_t_mt.estado, eiel_t_mt.capacidad, eiel_t_mt.utilizacion AS utilizacio, eiel_t_mt.tunel, eiel_t_mt.bovino, eiel_t_mt.ovino, eiel_t_mt.porcino, eiel_t_mt.otros
   FROM eiel_t_mt
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_mt.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_mt.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_mt.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_mt.clave, eiel_t_mt.codprov AS provincia, eiel_t_mt.codmunic AS municipio, eiel_t_mt.codentidad AS entidad, eiel_t_mt.codpoblamiento AS poblamient, eiel_t_mt.orden_mt AS orden_mata, eiel_t_mt.nombre, eiel_t_mt.clase AS clase_mat, eiel_t_mt.titular, eiel_t_mt.gestor AS gestion, eiel_t_mt.s_cubierta AS s_cubi, eiel_t_mt.s_aire, eiel_t_mt.s_solar AS s_sola, eiel_t_mt.acceso_s_ruedas, eiel_t_mt.estado, eiel_t_mt.capacidad, eiel_t_mt.utilizacion AS utilizacio, eiel_t_mt.tunel, eiel_t_mt.bovino, eiel_t_mt.ovino, eiel_t_mt.porcino, eiel_t_mt.otros
   FROM eiel_t_mt
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_mt.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_mt.codmunic::text
 RIGHT JOIN eiel_t_poblamiento ON   eiel_t_mt.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_mt.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_mt.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_mt.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_mt.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_matadero OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_matadero TO geopista;
GRANT SELECT ON TABLE check_mpt_matadero TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Mataderos no referenciados con ningún poblamiento') THEN	
	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Mataderos no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Mataderos no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Mataderos no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Mataderos no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Mataderos no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_matadero_reference_poblamiento','check_mpt_matadero',currval('seq_dictionary'));



	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO11";


CREATE OR REPLACE VIEW check_mpt_cementerio AS 
 SELECT eiel_t_ce.clave, eiel_t_ce.codprov AS provincia, eiel_t_ce.codmunic AS municipio, eiel_t_ce.codentidad AS entidad, eiel_t_ce.codpoblamiento AS poblamient, eiel_t_ce.orden_ce AS orden_ceme, eiel_t_ce.nombre, eiel_t_ce.titular, eiel_t_ce.distancia, eiel_t_ce.acceso, eiel_t_ce.capilla, eiel_t_ce.deposito, eiel_t_ce.ampliacion, eiel_t_ce.saturacion, eiel_t_ce.superficie, eiel_t_ce.acceso_s_ruedas, eiel_t_ce.crematorio
   FROM eiel_t_ce
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ce.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ce.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ce.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_ce.clave, eiel_t_ce.codprov AS provincia, eiel_t_ce.codmunic AS municipio, eiel_t_ce.codentidad AS entidad, eiel_t_ce.codpoblamiento AS poblamient, eiel_t_ce.orden_ce AS orden_ceme, eiel_t_ce.nombre, eiel_t_ce.titular, eiel_t_ce.distancia, eiel_t_ce.acceso, eiel_t_ce.capilla, eiel_t_ce.deposito, eiel_t_ce.ampliacion, eiel_t_ce.saturacion, eiel_t_ce.superficie, eiel_t_ce.acceso_s_ruedas, eiel_t_ce.crematorio
   FROM eiel_t_ce
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ce.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ce.codmunic::text
 RIGHT JOIN eiel_t_poblamiento ON   eiel_t_ce.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_ce.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_ce.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_ce.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ce.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_cementerio OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_cementerio TO geopista;
GRANT SELECT ON TABLE check_mpt_cementerio TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Cementerios no referenciados con ningún poblamiento') THEN	
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Cementerios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Cementerios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Cementerios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Cementerios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Cementerios no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_cementerio_reference_poblamiento','check_mpt_cementerio',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO12";






CREATE OR REPLACE VIEW check_mpt_tanatorio AS 
 SELECT eiel_t_ta.clave, eiel_t_ta.codprov AS provincia, eiel_t_ta.codmunic AS municipio, eiel_t_ta.codentidad AS entidad, eiel_t_ta.codpoblamiento AS poblamient, eiel_t_ta.orden_ta AS orden_tana, eiel_t_ta.nombre, eiel_t_ta.titular, eiel_t_ta.gestor AS gestion, eiel_t_ta.s_cubierta AS s_cubi, eiel_t_ta.s_aire, eiel_t_ta.s_solar AS s_sola, eiel_t_ta.salas, eiel_t_ta.acceso_s_ruedas, eiel_t_ta.estado
   FROM eiel_t_ta
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ta.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ta.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ta.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_ta.clave, eiel_t_ta.codprov AS provincia, eiel_t_ta.codmunic AS municipio, eiel_t_ta.codentidad AS entidad, eiel_t_ta.codpoblamiento AS poblamient, eiel_t_ta.orden_ta AS orden_tana, eiel_t_ta.nombre, eiel_t_ta.titular, eiel_t_ta.gestor AS gestion, eiel_t_ta.s_cubierta AS s_cubi, eiel_t_ta.s_aire, eiel_t_ta.s_solar AS s_sola, eiel_t_ta.salas, eiel_t_ta.acceso_s_ruedas, eiel_t_ta.estado
   FROM eiel_t_ta
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ta.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ta.codmunic::text
 RIGHT JOIN eiel_t_poblamiento ON   eiel_t_ta.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_ta.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_ta.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_ta.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ta.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_tanatorio OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_tanatorio TO geopista;
GRANT SELECT ON TABLE check_mpt_tanatorio TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Tanatorios no referenciados con ningún poblamiento') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tanatorios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tanatorios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tanatorios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tanatorios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tanatorios no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_tanatorio_reference_poblamiento','check_mpt_tanatorio',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO13";






CREATE OR REPLACE VIEW check_mpt_colector AS 
 SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia, eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
   FROM eiel_t_saneam_tcl
  WHERE eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia, eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
   FROM eiel_t_saneam_tcl
   RIGHT JOIN eiel_c_municipios on eiel_t_saneam_tcl.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t_saneam_tcl.codmunic
  WHERE eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric;
  
ALTER TABLE check_mpt_colector OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_colector TO geopista;
GRANT SELECT ON TABLE check_mpt_colector TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Colectores no referenciados con ninguna provincia ni municipio') THEN	
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Colectores no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Colectores no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Colectores no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Colectores no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Colectores no referenciados con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('COLECTOR','check_colector_reference_provincia','check_mpt_colector',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO14";





CREATE OR REPLACE VIEW check_mpt_colector_nucleo AS 
 SELECT eiel_tr_saneam_tcl_pobl.codprov_pobl AS provincia, eiel_tr_saneam_tcl_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_tcl_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_tcl_pobl.clave_tcl AS clave, eiel_tr_saneam_tcl_pobl.codprov_tcl AS c_provinc, eiel_tr_saneam_tcl_pobl.codmunic_tcl AS c_municip, eiel_tr_saneam_tcl_pobl.tramo_cl AS orden_cole
   FROM eiel_tr_saneam_tcl_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_tcl_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_tcl_pobl.codmunic_pobl::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_tcl_pobl.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_tr_saneam_tcl_pobl.codprov_pobl AS provincia, eiel_tr_saneam_tcl_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_tcl_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_tcl_pobl.clave_tcl AS clave, eiel_tr_saneam_tcl_pobl.codprov_tcl AS c_provinc, eiel_tr_saneam_tcl_pobl.codmunic_tcl AS c_municip, eiel_tr_saneam_tcl_pobl.tramo_cl AS orden_cole
   FROM eiel_tr_saneam_tcl_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_tcl_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_tcl_pobl.codmunic_pobl::text
RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_tr_saneam_tcl_pobl.codprov_pobl AND eiel_t_nucl_encuest_1.codmunic=eiel_tr_saneam_tcl_pobl.codmunic_pobl AND eiel_t_nucl_encuest_1.codentidad=eiel_tr_saneam_tcl_pobl.codentidad_pobl AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_tr_saneam_tcl_pobl.codprov_pobl AND eiel_t_nucl_encuest_2.codmunic=eiel_tr_saneam_tcl_pobl.codmunic_pobl AND eiel_t_nucl_encuest_2.codentidad=eiel_tr_saneam_tcl_pobl.codentidad_pobl AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_saneam_tcl on eiel_t_saneam_tcl.codprov=eiel_tr_saneam_tcl_pobl.codprov_tcl AND eiel_t_saneam_tcl.codmunic=eiel_tr_saneam_tcl_pobl.codmunic_tcl AND eiel_t_saneam_tcl.tramo_cl=eiel_tr_saneam_tcl_pobl.tramo_cl
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_tcl_pobl.revision_expirada = 9999999999::bigint::numeric;
  
ALTER TABLE check_mpt_colector_nucleo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_colector_nucleo TO geopista;
GRANT SELECT ON TABLE check_mpt_colector_nucleo TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'No existen como Colectores o no están referenciados con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Colectores o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Colectores o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Colectores o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Colectores o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Colectores o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('COLECTOR','check_colector_nucleo_reference_nuc_encuestado','check_mpt_colector_nucleo',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15";






CREATE OR REPLACE VIEW check_mpt_conduccion AS 
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
   FROM eiel_t_abast_tcn
  WHERE eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
   FROM eiel_t_abast_tcn
   RIGHT JOIN eiel_c_municipios on eiel_t_abast_tcn.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t_abast_tcn.codmunic
  WHERE eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric;
  
ALTER TABLE check_mpt_conduccion OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_conduccion TO geopista;
GRANT SELECT ON TABLE check_mpt_conduccion TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Conducciones no referenciadas con ninguna provincia ni municipio') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Conducciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Conducciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Conducciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Conducciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Conducciones no referenciadas con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CONDUCCIONES','check_conduccion_reference_provincia','check_mpt_conduccion',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO16";





CREATE OR REPLACE VIEW check_mpt_conducccion_nucleo AS 
   SELECT eiel_tr_abast_tcn_pobl.codprov_pobl AS provincia, eiel_tr_abast_tcn_pobl.codmunic_pobl AS municipio, eiel_tr_abast_tcn_pobl.codentidad_pobl AS entidad, eiel_tr_abast_tcn_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_tcn_pobl.clave_tcn AS clave, eiel_tr_abast_tcn_pobl.codprov_tcn AS cond_provi, eiel_tr_abast_tcn_pobl.codmunic_tcn AS cond_munic, eiel_tr_abast_tcn_pobl.tramo_tcn AS orden_cond
   FROM eiel_tr_abast_tcn_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_tcn_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_tcn_pobl.codmunic_pobl::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_tcn_pobl.revision_expirada = 9999999999::bigint::numeric
EXCEPT
   SELECT eiel_tr_abast_tcn_pobl.codprov_pobl AS provincia, eiel_tr_abast_tcn_pobl.codmunic_pobl AS municipio, eiel_tr_abast_tcn_pobl.codentidad_pobl AS entidad, eiel_tr_abast_tcn_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_tcn_pobl.clave_tcn AS clave, eiel_tr_abast_tcn_pobl.codprov_tcn AS cond_provi, eiel_tr_abast_tcn_pobl.codmunic_tcn AS cond_munic, eiel_tr_abast_tcn_pobl.tramo_tcn AS orden_cond
   FROM eiel_tr_abast_tcn_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_tcn_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_tcn_pobl.codmunic_pobl::text
RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_tr_abast_tcn_pobl.codprov_pobl AND eiel_t_nucl_encuest_1.codmunic=eiel_tr_abast_tcn_pobl.codmunic_pobl AND eiel_t_nucl_encuest_1.codentidad=eiel_tr_abast_tcn_pobl.codentidad_pobl AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_tr_abast_tcn_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_tr_abast_tcn_pobl.codprov_pobl AND eiel_t_nucl_encuest_2.codmunic=eiel_tr_abast_tcn_pobl.codmunic_pobl AND eiel_t_nucl_encuest_2.codentidad=eiel_tr_abast_tcn_pobl.codentidad_pobl AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_tr_abast_tcn_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_abast_tcn on eiel_t_abast_tcn.codprov=eiel_tr_abast_tcn_pobl.codprov_tcn AND eiel_t_abast_tcn.codmunic=eiel_tr_abast_tcn_pobl.codmunic_tcn AND eiel_t_abast_tcn.tramo_cn=eiel_tr_abast_tcn_pobl.tramo_tcn
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_tcn_pobl.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_conducccion_nucleo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_conducccion_nucleo TO geopista;
GRANT SELECT ON TABLE check_mpt_conducccion_nucleo TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'No existen como Conducciones o no están referenciadas con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CONDUCCIONES','check_conduccion_nucleo_reference_nuc_encuestado','check_mpt_conducccion_nucleo',currval('seq_dictionary'));

	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO17";



CREATE OR REPLACE VIEW check_mpt_depositos AS 
 SELECT eiel_t_abast_de.clave, eiel_t_abast_de.codprov AS provincia, eiel_t_abast_de.codmunic AS municipio, eiel_t_abast_de.orden_de AS orden_depo
   FROM eiel_t_abast_de
  WHERE eiel_t_abast_de.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_abast_de.clave, eiel_t_abast_de.codprov AS provincia, eiel_t_abast_de.codmunic AS municipio, eiel_t_abast_de.orden_de AS orden_depo
   FROM eiel_t_abast_de
   RIGHT JOIN eiel_c_municipios on eiel_t_abast_de.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t_abast_de.codmunic
  WHERE eiel_t_abast_de.revision_expirada = 9999999999::bigint::numeric;
  
ALTER TABLE check_mpt_depositos OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_depositos TO geopista;
GRANT SELECT ON TABLE check_mpt_depositos TO consultas;



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Depósitos no referenciados con ninguna provincia ni municipio') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Depósitos no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Depósitos no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Depósitos no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Depósitos no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Depósitos no referenciados con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('DEPOSITOS','check_depositos_reference_provincia','check_mpt_depositos',currval('seq_dictionary'));

	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO18";




CREATE OR REPLACE VIEW check_mpt_depositos_nucleo AS 
 SELECT eiel_tr_abast_de_pobl.codprov_pobl AS provincia, eiel_tr_abast_de_pobl.codmunic_pobl AS municipio, eiel_tr_abast_de_pobl.codentidad_pobl AS entidad, eiel_tr_abast_de_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_de_pobl.clave_de AS clave, eiel_tr_abast_de_pobl.codprov_de AS de_provinc, eiel_tr_abast_de_pobl.codmunic_de AS de_municip, eiel_tr_abast_de_pobl.orden_de AS orden_depo
   FROM eiel_tr_abast_de_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_de_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_de_pobl.codmunic_pobl::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_de_pobl.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_tr_abast_de_pobl.codprov_pobl AS provincia, eiel_tr_abast_de_pobl.codmunic_pobl AS municipio, eiel_tr_abast_de_pobl.codentidad_pobl AS entidad, eiel_tr_abast_de_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_de_pobl.clave_de AS clave, eiel_tr_abast_de_pobl.codprov_de AS de_provinc, eiel_tr_abast_de_pobl.codmunic_de AS de_municip, eiel_tr_abast_de_pobl.orden_de AS orden_depo
   FROM eiel_tr_abast_de_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_de_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_de_pobl.codmunic_pobl::text
RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_tr_abast_de_pobl.codprov_pobl AND eiel_t_nucl_encuest_1.codmunic=eiel_tr_abast_de_pobl.codmunic_pobl AND eiel_t_nucl_encuest_1.codentidad=eiel_tr_abast_de_pobl.codentidad_pobl AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_tr_abast_de_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_tr_abast_de_pobl.codprov_pobl AND eiel_t_nucl_encuest_2.codmunic=eiel_tr_abast_de_pobl.codmunic_pobl AND eiel_t_nucl_encuest_2.codentidad=eiel_tr_abast_de_pobl.codentidad_pobl AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_tr_abast_de_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_abast_de on eiel_t_abast_de.codprov=eiel_tr_abast_de_pobl.codprov_de AND eiel_t_abast_de.codmunic=eiel_tr_abast_de_pobl.codmunic_de AND eiel_t_abast_de.orden_de=eiel_tr_abast_de_pobl.orden_de
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_de_pobl.revision_expirada = 9999999999::bigint::numeric;


ALTER TABLE check_mpt_depositos_nucleo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_depositos_nucleo TO geopista;
GRANT SELECT ON TABLE check_mpt_depositos_nucleo TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'No existen como Dépositos o no están referenciados con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Dépositos o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Dépositos o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Dépositos o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Dépositos o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Dépositos o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('DEPOSITOS','check_depositos_nucleo_reference_nuc_encuestado','check_mpt_depositos_nucleo',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO19";


CREATE OR REPLACE VIEW check_mpt_depuradora AS 
 SELECT eiel_t1_saneam_ed.clave, eiel_t1_saneam_ed.codprov AS provincia, eiel_t1_saneam_ed.codmunic AS municipio, eiel_t1_saneam_ed.orden_ed AS orden_depu
   FROM eiel_t1_saneam_ed
   LEFT JOIN eiel_t2_saneam_ed ON eiel_t2_saneam_ed.codprov::text = eiel_t1_saneam_ed.codprov::text AND eiel_t2_saneam_ed.codmunic::text = eiel_t1_saneam_ed.codmunic::text AND eiel_t1_saneam_ed.clave::text = eiel_t2_saneam_ed.clave::text AND eiel_t1_saneam_ed.orden_ed::text = eiel_t2_saneam_ed.orden_ed::text
  WHERE eiel_t1_saneam_ed.revision_expirada = 9999999999::bigint::numeric AND eiel_t2_saneam_ed.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t1_saneam_ed.clave, eiel_t1_saneam_ed.codprov AS provincia, eiel_t1_saneam_ed.codmunic AS municipio, eiel_t1_saneam_ed.orden_ed AS orden_depu
   FROM eiel_t1_saneam_ed
   LEFT JOIN eiel_t2_saneam_ed ON eiel_t2_saneam_ed.codprov::text = eiel_t1_saneam_ed.codprov::text AND eiel_t2_saneam_ed.codmunic::text = eiel_t1_saneam_ed.codmunic::text AND eiel_t1_saneam_ed.clave::text = eiel_t2_saneam_ed.clave::text AND eiel_t1_saneam_ed.orden_ed::text = eiel_t2_saneam_ed.orden_ed::text
   RIGHT JOIN eiel_c_municipios on eiel_t1_saneam_ed.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t1_saneam_ed.codmunic
			AND eiel_t2_saneam_ed.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t2_saneam_ed.codmunic
  WHERE eiel_t1_saneam_ed.revision_expirada = 9999999999::bigint::numeric AND eiel_t2_saneam_ed.revision_expirada = 9999999999::bigint::numeric;

  
ALTER TABLE check_mpt_depuradora OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_depuradora TO geopista;
GRANT SELECT ON TABLE check_mpt_depuradora TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Depuradoras no referenciadas con ninguna provincia ni municipio') THEN	
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Depuradoras no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Depuradoras no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Depuradoras no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Depuradoras no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Depuradoras no referenciadas con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('DEPURADORAS','check_depuradora_reference_provincia','check_mpt_depuradora',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO20";




CREATE OR REPLACE VIEW check_mpt_depuradora_nucleo AS 
 SELECT eiel_tr_saneam_ed_pobl.codprov_pobl AS provincia, eiel_tr_saneam_ed_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_ed_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_ed_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_ed_pobl.clave_ed AS clave, eiel_tr_saneam_ed_pobl.codprov_ed AS de_provinc, eiel_tr_saneam_ed_pobl.codmunic_ed AS de_municip, eiel_tr_saneam_ed_pobl.orden_ed AS orden_depu
   FROM eiel_tr_saneam_ed_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_ed_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_ed_pobl.codmunic_pobl::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_ed_pobl.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_tr_saneam_ed_pobl.codprov_pobl AS provincia, eiel_tr_saneam_ed_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_ed_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_ed_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_ed_pobl.clave_ed AS clave, eiel_tr_saneam_ed_pobl.codprov_ed AS de_provinc, eiel_tr_saneam_ed_pobl.codmunic_ed AS de_municip, eiel_tr_saneam_ed_pobl.orden_ed AS orden_depu
   FROM eiel_tr_saneam_ed_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_ed_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_ed_pobl.codmunic_pobl::text
   RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_tr_saneam_ed_pobl.codprov_pobl AND eiel_t_nucl_encuest_1.codmunic=eiel_tr_saneam_ed_pobl.codmunic_pobl AND eiel_t_nucl_encuest_1.codentidad=eiel_tr_saneam_ed_pobl.codentidad_pobl AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_tr_saneam_ed_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_tr_saneam_ed_pobl.codprov_pobl AND eiel_t_nucl_encuest_2.codmunic=eiel_tr_saneam_ed_pobl.codmunic_pobl AND eiel_t_nucl_encuest_2.codentidad=eiel_tr_saneam_ed_pobl.codentidad_pobl AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_tr_saneam_ed_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t1_saneam_ed on eiel_t1_saneam_ed.codprov=eiel_tr_saneam_ed_pobl.codprov_ed AND eiel_t1_saneam_ed.codmunic=eiel_tr_saneam_ed_pobl.codmunic_ed AND eiel_t1_saneam_ed.orden_ed=eiel_tr_saneam_ed_pobl.orden_ed
   RIGHT JOIN eiel_t2_saneam_ed on eiel_t2_saneam_ed.codprov=eiel_tr_saneam_ed_pobl.codprov_ed AND eiel_t2_saneam_ed.codmunic=eiel_tr_saneam_ed_pobl.codmunic_ed AND eiel_t2_saneam_ed.orden_ed=eiel_tr_saneam_ed_pobl.orden_ed
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_ed_pobl.revision_expirada = 9999999999::bigint::numeric;


ALTER TABLE check_mpt_depuradora_nucleo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_depuradora_nucleo TO geopista;
GRANT SELECT ON TABLE check_mpt_depuradora_nucleo TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'No existen como Depuradoras o no están referenciada con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('DEPURADORAS','check_depuradora_nucleo_reference_nuc_encuestado','check_mpt_depuradora_nucleo',currval('seq_dictionary'));

	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO21";





CREATE OR REPLACE VIEW check_mpt_emisario AS 
 SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis
   FROM eiel_t_saneam_tem
  WHERE eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis
   FROM eiel_t_saneam_tem
   RIGHT JOIN eiel_c_municipios on eiel_t_saneam_tem.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t_saneam_tem.codmunic
  WHERE eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric;
  
ALTER TABLE check_mpt_emisario OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_emisario TO geopista;
GRANT SELECT ON TABLE check_mpt_emisario TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Emisarios no referenciados con ninguna provincia ni municipio') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Emisarios no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Emisarios no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Emisarios no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Emisarios no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Emisarios no referenciados con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('EMISARIOS','check_emisario_reference_provincia','check_mpt_emisario',currval('seq_dictionary'));

	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO22";




CREATE OR REPLACE VIEW check_mpt_emisario_nucleo AS 
 SELECT eiel_tr_saneam_tem_pobl.codprov_pobl AS provincia, eiel_tr_saneam_tem_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_tem_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_tem_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_tem_pobl.clave_tem AS clave, eiel_tr_saneam_tem_pobl.codprov_tem AS em_provinc, eiel_tr_saneam_tem_pobl.codmunic_tem AS em_municip, eiel_tr_saneam_tem_pobl.tramo_em AS orden_emis
   FROM eiel_tr_saneam_tem_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_tem_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_tem_pobl.codmunic_pobl::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_tem_pobl.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_tr_saneam_tem_pobl.codprov_pobl AS provincia, eiel_tr_saneam_tem_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_tem_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_tem_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_tem_pobl.clave_tem AS clave, eiel_tr_saneam_tem_pobl.codprov_tem AS em_provinc, eiel_tr_saneam_tem_pobl.codmunic_tem AS em_municip, eiel_tr_saneam_tem_pobl.tramo_em AS orden_emis
   FROM eiel_tr_saneam_tem_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_tem_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_tem_pobl.codmunic_pobl::text
   RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_tr_saneam_tem_pobl.codprov_pobl AND eiel_t_nucl_encuest_1.codmunic=eiel_tr_saneam_tem_pobl.codmunic_pobl AND eiel_t_nucl_encuest_1.codentidad=eiel_tr_saneam_tem_pobl.codentidad_pobl AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_tr_saneam_tem_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_tr_saneam_tem_pobl.codprov_pobl AND eiel_t_nucl_encuest_2.codmunic=eiel_tr_saneam_tem_pobl.codmunic_pobl AND eiel_t_nucl_encuest_2.codentidad=eiel_tr_saneam_tem_pobl.codentidad_pobl AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_tr_saneam_tem_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_saneam_tem on eiel_t_saneam_tem.codprov=eiel_tr_saneam_tem_pobl.codprov_tem AND eiel_t_saneam_tem.codmunic=eiel_tr_saneam_tem_pobl.codmunic_tem AND eiel_t_saneam_tem.tramo_em=eiel_tr_saneam_tem_pobl.tramo_em
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_tem_pobl.revision_expirada = 9999999999::bigint::numeric;


ALTER TABLE check_mpt_emisario_nucleo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_emisario_nucleo TO geopista;
GRANT SELECT ON TABLE check_mpt_emisario_nucleo TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'No existen como Emisario o no están referenciados con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Emisario o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Emisario o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Emisario o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Emisario o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Emisario o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('EMISARIOS','check_emisario_nucleo_reference_nuc_encuestado','check_mpt_emisario_nucleo',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO23";




CREATE OR REPLACE VIEW check_mpt_proteccion AS 
 SELECT eiel_t_ip.clave, eiel_t_ip.codprov AS provincia, eiel_t_ip.codmunic AS municipio, eiel_t_ip.codentidad AS entidad, eiel_t_ip.codpoblamiento AS poblamient, eiel_t_ip.orden_ip AS orden_prot, eiel_t_ip.nombre, eiel_t_ip.tipo AS tipo_pciv, eiel_t_ip.titular, eiel_t_ip.gestor AS gestion, eiel_t_ip.ambito, eiel_t_ip.plan_profe, eiel_t_ip.plan_volun, eiel_t_ip.s_cubierta AS s_cubi, eiel_t_ip.s_aire, eiel_t_ip.s_solar AS s_sola, eiel_t_ip.acceso_s_ruedas, eiel_t_ip.estado, eiel_t_ip.vehic_incendio AS vehic_ince, eiel_t_ip.vehic_rescate AS vehic_resc, eiel_t_ip.ambulancia, eiel_t_ip.medios_aereos AS medios_aer, eiel_t_ip.otros_vehc AS otros_vehi, eiel_t_ip.quitanieves AS quitanieve, eiel_t_ip.detec_ince, eiel_t_ip.otros
   FROM eiel_t_ip
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ip.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ip.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ip.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_ip.clave, eiel_t_ip.codprov AS provincia, eiel_t_ip.codmunic AS municipio, eiel_t_ip.codentidad AS entidad, eiel_t_ip.codpoblamiento AS poblamient, eiel_t_ip.orden_ip AS orden_prot, eiel_t_ip.nombre, eiel_t_ip.tipo AS tipo_pciv, eiel_t_ip.titular, eiel_t_ip.gestor AS gestion, eiel_t_ip.ambito, eiel_t_ip.plan_profe, eiel_t_ip.plan_volun, eiel_t_ip.s_cubierta AS s_cubi, eiel_t_ip.s_aire, eiel_t_ip.s_solar AS s_sola, eiel_t_ip.acceso_s_ruedas, eiel_t_ip.estado, eiel_t_ip.vehic_incendio AS vehic_ince, eiel_t_ip.vehic_rescate AS vehic_resc, eiel_t_ip.ambulancia, eiel_t_ip.medios_aereos AS medios_aer, eiel_t_ip.otros_vehc AS otros_vehi, eiel_t_ip.quitanieves AS quitanieve, eiel_t_ip.detec_ince, eiel_t_ip.otros
   FROM eiel_t_ip
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ip.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ip.codmunic::text
RIGHT JOIN eiel_t_poblamiento ON   eiel_t_ip.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_ip.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_ip.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_ip.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ip.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_proteccion OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_proteccion TO geopista;
GRANT SELECT ON TABLE check_mpt_proteccion TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Protecciones civíles no referenciadas con ningún poblamiento') THEN	


		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Protecciones civíles no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Protecciones civíles no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Protecciones civíles no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Protecciones civíles no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Protecciones civíles no referenciadas con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_proteccion_reference_poblamiento','check_mpt_proteccion',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO24";



CREATE OR REPLACE VIEW check_mpt_centro_ensenanza AS 
 SELECT eiel_t_en.clave, eiel_t_en.codprov AS provincia, eiel_t_en.codmunic AS municipio, eiel_t_en.codentidad AS entidad, eiel_t_en.codpoblamiento AS poblamient, eiel_t_en.orden_en AS orden_cent, eiel_t_en.nombre, eiel_t_en.ambito, eiel_t_en.titular, eiel_t_en.s_cubierta AS s_cubi, eiel_t_en.s_aire, eiel_t_en.s_solar AS s_sola, eiel_t_en.acceso_s_ruedas, eiel_t_en.estado
   FROM eiel_t_en
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_en.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_en.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_en.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_en.clave, eiel_t_en.codprov AS provincia, eiel_t_en.codmunic AS municipio, eiel_t_en.codentidad AS entidad, eiel_t_en.codpoblamiento AS poblamient, eiel_t_en.orden_en AS orden_cent, eiel_t_en.nombre, eiel_t_en.ambito, eiel_t_en.titular, eiel_t_en.s_cubierta AS s_cubi, eiel_t_en.s_aire, eiel_t_en.s_solar AS s_sola, eiel_t_en.acceso_s_ruedas, eiel_t_en.estado
   FROM eiel_t_en
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_en.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_en.codmunic::text
RIGHT JOIN eiel_t_poblamiento ON   eiel_t_en.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_en.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_en.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_en.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_en.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_centro_ensenanza OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_centro_ensenanza TO geopista;
GRANT SELECT ON TABLE check_mpt_centro_ensenanza TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Centros de enseñanza no referenciados con ningún poblamiento') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros de enseñanza no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros de enseñanza no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros de enseñanza no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros de enseñanza no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros de enseñanza no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_centro_ensenanza_reference_poblamiento','check_mpt_centro_ensenanza',currval('seq_dictionary'));

	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO25";





CREATE OR REPLACE VIEW check_mpt_nivel_ensenanza AS 
 SELECT DISTINCT ON (eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov, eiel_t_en_nivel.codmunic, eiel_t_en_nivel.codentidad, eiel_t_en_nivel.codpoblamiento, eiel_t_en_nivel.orden_en, eiel_t_en_nivel.nivel) eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov AS provincia, eiel_t_en_nivel.codmunic AS municipio, eiel_t_en_nivel.codentidad AS entidad, eiel_t_en_nivel.codpoblamiento AS poblamient, eiel_t_en_nivel.orden_en AS orden_cent, eiel_t_en_nivel.nivel, eiel_t_en_nivel.unidades, eiel_t_en_nivel.plazas, eiel_t_en_nivel.alumnos
   FROM eiel_t_en_nivel
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_en_nivel.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_en_nivel.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_en_nivel.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT DISTINCT ON (eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov, eiel_t_en_nivel.codmunic, eiel_t_en_nivel.codentidad, eiel_t_en_nivel.codpoblamiento, eiel_t_en_nivel.orden_en, eiel_t_en_nivel.nivel) eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov AS provincia, eiel_t_en_nivel.codmunic AS municipio, eiel_t_en_nivel.codentidad AS entidad, eiel_t_en_nivel.codpoblamiento AS poblamient, eiel_t_en_nivel.orden_en AS orden_cent, eiel_t_en_nivel.nivel, eiel_t_en_nivel.unidades, eiel_t_en_nivel.plazas, eiel_t_en_nivel.alumnos
   FROM eiel_t_en_nivel
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_en_nivel.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_en_nivel.codmunic::text
RIGHT JOIN eiel_t_en ON   eiel_t_en.codprov=eiel_t_en_nivel.codprov AND eiel_t_en_nivel.codmunic=eiel_t_en.codmunic AND eiel_t_en_nivel.codentidad=eiel_t_en.codentidad AND eiel_t_en_nivel.codpoblamiento= eiel_t_en.codpoblamiento AND eiel_t_en_nivel.orden_en= eiel_t_en.orden_en
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_en_nivel.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_nivel_ensenanza OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_nivel_ensenanza TO geopista;
GRANT SELECT ON TABLE check_mpt_nivel_ensenanza TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Niveles de enseñanza no referenciados con ningún centro de enseñanza') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Niveles de enseñanza no referenciados con ningún centro de enseñanza');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Niveles de enseñanza no referenciados con ningún centro de enseñanza');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Niveles de enseñanza no referenciados con ningún centro de enseñanza');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Niveles de enseñanza no referenciados con ningún centro de enseñanza');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Niveles de enseñanza no referenciados con ningún centro de enseñanza');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_nivel_ensenanza_reference_poblamiento','check_mpt_nivel_ensenanza',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO26";





CREATE OR REPLACE VIEW check_mpt_edific AS 
 SELECT eiel_t_su.clave, eiel_t_su.codprov AS provincia, eiel_t_su.codmunic AS municipio, eiel_t_su.codentidad AS entidad, eiel_t_su.codpoblamiento AS poblamient, eiel_t_su.orden_su AS orden_edif, eiel_t_su.nombre, eiel_t_su.titular, eiel_t_su.s_cubierta AS s_cubi, eiel_t_su.s_aire, eiel_t_su.s_solar AS s_sola, eiel_t_su.estado, eiel_t_su.uso_anterior AS usoant
   FROM eiel_t_su
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_su.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_su.codmunic::text
   WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_su.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_su.clave, eiel_t_su.codprov AS provincia, eiel_t_su.codmunic AS municipio, eiel_t_su.codentidad AS entidad, eiel_t_su.codpoblamiento AS poblamient, eiel_t_su.orden_su AS orden_edif, eiel_t_su.nombre, eiel_t_su.titular, eiel_t_su.s_cubierta AS s_cubi, eiel_t_su.s_aire, eiel_t_su.s_solar AS s_sola, eiel_t_su.estado, eiel_t_su.uso_anterior AS usoant
  FROM eiel_t_su
  LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_su.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_su.codmunic::text
  RIGHT JOIN eiel_t_poblamiento ON   eiel_t_su.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_su.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_su.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_su.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_su.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_edific OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_edific TO geopista;
GRANT SELECT ON TABLE check_mpt_edific TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Edificios públicos sin uso no referenciados con ningún poblamiento') THEN	


		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Edificios públicos sin uso no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Edificios públicos sin uso no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Edificios públicos sin uso no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Edificios públicos sin uso no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Edificios públicos sin uso no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_edific_reference_poblamiento','check_mpt_edific',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO27";



CREATE OR REPLACE VIEW check_mpt_casa_consistorial AS 
 SELECT eiel_t_cc.clave, eiel_t_cc.codprov AS provincia, eiel_t_cc.codmunic AS municipio, eiel_t_cc.codentidad AS entidad, eiel_t_cc.codpoblamiento AS poblamient, eiel_t_cc.orden_cc AS orden_casa, eiel_t_cc.nombre, eiel_t_cc.tipo, eiel_t_cc.titular, eiel_t_cc.tenencia, eiel_t_cc.s_cubierta AS s_cubi, eiel_t_cc.s_aire, eiel_t_cc.s_solar AS s_sola, eiel_t_cc.acceso_s_ruedas, eiel_t_cc.estado
   FROM eiel_t_cc
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cc.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cc.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cc.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_cc.clave, eiel_t_cc.codprov AS provincia, eiel_t_cc.codmunic AS municipio, eiel_t_cc.codentidad AS entidad, eiel_t_cc.codpoblamiento AS poblamient, eiel_t_cc.orden_cc AS orden_casa, eiel_t_cc.nombre, eiel_t_cc.tipo, eiel_t_cc.titular, eiel_t_cc.tenencia, eiel_t_cc.s_cubierta AS s_cubi, eiel_t_cc.s_aire, eiel_t_cc.s_solar AS s_sola, eiel_t_cc.acceso_s_ruedas, eiel_t_cc.estado
   FROM eiel_t_cc
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cc.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cc.codmunic::text
RIGHT JOIN eiel_t_poblamiento ON   eiel_t_cc.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_cc.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_cc.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_cc.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cc.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_casa_consistorial OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_casa_consistorial TO geopista;
GRANT SELECT ON TABLE check_mpt_casa_consistorial TO consultas;



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Casas Consistoriales no referenciadas con ningún poblamiento') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Casas Consistoriales no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Casas Consistoriales no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Casas Consistoriales no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Casas Consistoriales no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Casas Consistoriales no referenciadas con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_casa_consistorial_reference_poblamiento','check_mpt_casa_consistorial',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO28";


CREATE OR REPLACE VIEW check_mpt_casa_uso AS 
  SELECT DISTINCT ON (eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov, eiel_t_cc_usos.codmunic, eiel_t_cc_usos.codentidad, eiel_t_cc_usos.codpoblamiento, eiel_t_cc_usos.orden_cc, eiel_t_cc_usos.uso) eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov AS provincia, eiel_t_cc_usos.codmunic AS municipio, eiel_t_cc_usos.codentidad AS entidad, eiel_t_cc_usos.codpoblamiento AS poblamient, eiel_t_cc_usos.orden_cc AS orden_casa, eiel_t_cc_usos.uso, eiel_t_cc_usos.s_cubierta AS s_cubi
   FROM eiel_t_cc_usos
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cc_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cc_usos.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cc_usos.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT DISTINCT ON (eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov, eiel_t_cc_usos.codmunic, eiel_t_cc_usos.codentidad, eiel_t_cc_usos.codpoblamiento, eiel_t_cc_usos.orden_cc, eiel_t_cc_usos.uso) eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov AS provincia, eiel_t_cc_usos.codmunic AS municipio, eiel_t_cc_usos.codentidad AS entidad, eiel_t_cc_usos.codpoblamiento AS poblamient, eiel_t_cc_usos.orden_cc AS orden_casa, eiel_t_cc_usos.uso, eiel_t_cc_usos.s_cubierta AS s_cubi
   FROM eiel_t_cc_usos
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cc_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cc_usos.codmunic::text
RIGHT JOIN eiel_t_cc ON   eiel_t_cc.codprov=eiel_t_cc_usos.codprov AND eiel_t_cc_usos.codmunic=eiel_t_cc.codmunic AND eiel_t_cc_usos.codentidad=eiel_t_cc.codentidad AND eiel_t_cc_usos.codpoblamiento= eiel_t_cc.codpoblamiento AND eiel_t_cc_usos.orden_cc= eiel_t_cc.orden_cc
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cc_usos.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_casa_uso OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_casa_uso TO geopista;
GRANT SELECT ON TABLE check_mpt_casa_uso TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Casas con usos no referenciados con ninguna casa consistorial') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Casas con usos no referenciados con ninguna casa consistorial');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Casas con usos no referenciados con ninguna casa consistorial');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Casas con usos no referenciados con ninguna casa consistorial');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Casas con usos no referenciados con ninguna casa consistorial');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Casas con usos no referenciados con ninguna casa consistorial');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_casa_uso_reference_poblamiento','check_mpt_casa_uso',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO29";





CREATE OR REPLACE VIEW check_mpt_cent_cultural AS 
 SELECT eiel_t_cu.clave, eiel_t_cu.codprov AS provincia, eiel_t_cu.codmunic AS municipio, eiel_t_cu.codentidad AS entidad, eiel_t_cu.codpoblamiento AS poblamient, eiel_t_cu.orden_cu AS orden_cent, eiel_t_cu.nombre, eiel_t_cu.tipo AS tipo_cent, eiel_t_cu.titular, eiel_t_cu.gestor AS gestion, eiel_t_cu.s_cubierta AS s_cubi, eiel_t_cu.s_aire, eiel_t_cu.s_solar AS s_sola, eiel_t_cu.acceso_s_ruedas, eiel_t_cu.estado
   FROM eiel_t_cu
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cu.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cu.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cu.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_cu.clave, eiel_t_cu.codprov AS provincia, eiel_t_cu.codmunic AS municipio, eiel_t_cu.codentidad AS entidad, eiel_t_cu.codpoblamiento AS poblamient, eiel_t_cu.orden_cu AS orden_cent, eiel_t_cu.nombre, eiel_t_cu.tipo AS tipo_cent, eiel_t_cu.titular, eiel_t_cu.gestor AS gestion, eiel_t_cu.s_cubierta AS s_cubi, eiel_t_cu.s_aire, eiel_t_cu.s_solar AS s_sola, eiel_t_cu.acceso_s_ruedas, eiel_t_cu.estado
   FROM eiel_t_cu
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cu.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cu.codmunic::text
RIGHT JOIN eiel_t_poblamiento ON   eiel_t_cu.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_cu.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_cu.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_cu.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cu.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_cent_cultural OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_cent_cultural TO geopista;
GRANT SELECT ON TABLE check_mpt_cent_cultural TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Centros Culturales no referenciados con ningún poblamiento') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros Culturales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros Culturales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros Culturales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros Culturales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros Culturales no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('INST_DEPORT_PARQUES_LONJAS','check_cent_cultural_reference_poblamiento','check_mpt_cent_cultural',currval('seq_dictionary'));


	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO30";




CREATE OR REPLACE VIEW check_mpt_cent_cultural_usos AS 
 SELECT DISTINCT ON (eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov, eiel_t_cu_usos.codmunic, eiel_t_cu_usos.codentidad, eiel_t_cu_usos.codpoblamiento, eiel_t_cu_usos.orden_cu, eiel_t_cu_usos.uso) eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov AS provincia, eiel_t_cu_usos.codmunic AS municipio, eiel_t_cu_usos.codentidad AS entidad, eiel_t_cu_usos.codpoblamiento AS poblamient, eiel_t_cu_usos.orden_cu AS orden_cent, eiel_t_cu_usos.uso, eiel_t_cu_usos.s_cubierta AS s_cubi
   FROM eiel_t_cu_usos
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cu_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cu_usos.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cu_usos.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT DISTINCT ON (eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov, eiel_t_cu_usos.codmunic, eiel_t_cu_usos.codentidad, eiel_t_cu_usos.codpoblamiento, eiel_t_cu_usos.orden_cu, eiel_t_cu_usos.uso) eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov AS provincia, eiel_t_cu_usos.codmunic AS municipio, eiel_t_cu_usos.codentidad AS entidad, eiel_t_cu_usos.codpoblamiento AS poblamient, eiel_t_cu_usos.orden_cu AS orden_cent, eiel_t_cu_usos.uso, eiel_t_cu_usos.s_cubierta AS s_cubi
   FROM eiel_t_cu_usos
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cu_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cu_usos.codmunic::text
RIGHT JOIN eiel_t_cu ON  eiel_t_cu.codprov=eiel_t_cu_usos.codprov AND eiel_t_cu_usos.codmunic=eiel_t_cu.codmunic AND eiel_t_cu_usos.codentidad=eiel_t_cu.codentidad AND eiel_t_cu_usos.codpoblamiento= eiel_t_cu.codpoblamiento AND eiel_t_cu_usos.orden_cu= eiel_t_cu.orden_cu
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cu_usos.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_cent_cultural_usos OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_cent_cultural_usos TO geopista;
GRANT SELECT ON TABLE check_mpt_cent_cultural_usos TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Centros culturales con usos no referenciados con ningún centro cultural') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros culturales con usos no referenciados con ningún centro cultural');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros culturales con usos no referenciados con ningún centro cultural');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros culturales con usos no referenciados con ningún centro cultural');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros culturales con usos no referenciados con ningún centro cultural');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros culturales con usos no referenciados con ningún centro cultural');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('INST_DEPORT_PARQUES_LONJAS','check_cent_cultural_usos_reference_poblamiento','check_mpt_cent_cultural_usos',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO31";




CREATE OR REPLACE VIEW check_mpt_instal_deportiva AS 
 SELECT eiel_t_id.clave, eiel_t_id.codprov AS provincia, eiel_t_id.codmunic AS municipio, eiel_t_id.codentidad AS entidad, eiel_t_id.codpoblamiento AS poblamient, eiel_t_id.orden_id AS orden_inst, eiel_t_id.nombre, eiel_t_id.tipo AS tipo_insde, eiel_t_id.titular, eiel_t_id.gestor AS gestion, eiel_t_id.s_cubierta AS s_cubi, eiel_t_id.s_aire, eiel_t_id.s_solar AS s_sola, eiel_t_id.acceso_s_ruedas, eiel_t_id.estado
   FROM eiel_t_id
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_id.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_id.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_id.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_id.clave, eiel_t_id.codprov AS provincia, eiel_t_id.codmunic AS municipio, eiel_t_id.codentidad AS entidad, eiel_t_id.codpoblamiento AS poblamient, eiel_t_id.orden_id AS orden_inst, eiel_t_id.nombre, eiel_t_id.tipo AS tipo_insde, eiel_t_id.titular, eiel_t_id.gestor AS gestion, eiel_t_id.s_cubierta AS s_cubi, eiel_t_id.s_aire, eiel_t_id.s_solar AS s_sola, eiel_t_id.acceso_s_ruedas, eiel_t_id.estado
   FROM eiel_t_id
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_id.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_id.codmunic::text
RIGHT JOIN eiel_t_poblamiento ON   eiel_t_id.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_id.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_id.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_id.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_id.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_instal_deportiva OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_instal_deportiva TO geopista;
GRANT SELECT ON TABLE check_mpt_instal_deportiva TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Instalaciones deportivas no referenciadas con ningún poblamiento') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Instalaciones deportivas no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Instalaciones deportivas no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Instalaciones deportivas no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Instalaciones deportivas no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Instalaciones deportivas no referenciadas con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('INST_DEPORT_PARQUES_LONJAS','check_instal_deportiva_reference_poblamiento','check_mpt_instal_deportiva',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO32";



CREATE OR REPLACE VIEW check_mpt_inst_depor_deporte AS 
 SELECT DISTINCT ON (eiel_t_id_deportes.clave, eiel_t_id_deportes.codprov, eiel_t_id_deportes.codmunic, eiel_t_id_deportes.codentidad, eiel_t_id_deportes.codpoblamiento, eiel_t_id_deportes.orden_id, eiel_t_id_deportes.tipo_deporte)
 eiel_t_id_deportes.clave, eiel_t_id_deportes.codprov AS provincia, eiel_t_id_deportes.codmunic AS municipio, eiel_t_id_deportes.codentidad AS entidad, eiel_t_id_deportes.codpoblamiento AS poblamient, eiel_t_id_deportes.orden_id AS orden_inst, eiel_t_id_deportes.tipo_deporte AS tipo_depor
   FROM eiel_t_id_deportes
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_id_deportes.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_id_deportes.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_id_deportes.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT DISTINCT ON (eiel_t_id_deportes.clave, eiel_t_id_deportes.codprov, eiel_t_id_deportes.codmunic, eiel_t_id_deportes.codentidad, eiel_t_id_deportes.codpoblamiento, eiel_t_id_deportes.orden_id, eiel_t_id_deportes.tipo_deporte)
 eiel_t_id_deportes.clave, eiel_t_id_deportes.codprov AS provincia, eiel_t_id_deportes.codmunic AS municipio, eiel_t_id_deportes.codentidad AS entidad, eiel_t_id_deportes.codpoblamiento AS poblamient, eiel_t_id_deportes.orden_id AS orden_inst, eiel_t_id_deportes.tipo_deporte AS tipo_depor
   FROM eiel_t_id_deportes
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_id_deportes.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_id_deportes.codmunic::text
RIGHT JOIN eiel_t_id ON  eiel_t_id.codprov=eiel_t_id_deportes.codprov AND eiel_t_id_deportes.codmunic=eiel_t_id.codmunic AND eiel_t_id_deportes.codentidad=eiel_t_id.codentidad AND eiel_t_id_deportes.codpoblamiento= eiel_t_id.codpoblamiento AND eiel_t_id_deportes.orden_id= eiel_t_id.orden_id
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_id_deportes.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_inst_depor_deporte OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_inst_depor_deporte TO geopista;
GRANT SELECT ON TABLE check_mpt_inst_depor_deporte TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Deportes no referenciados con ninguna instalación deportiva') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Deportes no referenciados con ninguna instalación deportiva');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Deportes no referenciados con ninguna instalación deportiva');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Deportes no referenciados con ninguna instalación deportiva');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Deportes no referenciados con ninguna instalación deportiva');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Deportes no referenciados con ninguna instalación deportiva');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('INST_DEPORT_PARQUES_LONJAS','check_inst_depor_deporte_reference_poblamiento','check_mpt_inst_depor_deporte',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO32";



CREATE OR REPLACE VIEW check_mpt_parque AS 
 SELECT eiel_t_pj.clave, eiel_t_pj.codprov AS provincia, eiel_t_pj.codmunic AS municipio, eiel_t_pj.codentidad AS entidad, eiel_t_pj.codpoblamiento AS poblamient, eiel_t_pj.orden_pj AS orden_parq, eiel_t_pj.nombre, eiel_t_pj.tipo AS tipo_parq, eiel_t_pj.titular, eiel_t_pj.gestor AS gestion, eiel_t_pj.s_cubierta AS s_cubi, eiel_t_pj.s_aire, eiel_t_pj.s_solar AS s_sola, eiel_t_pj.agua, eiel_t_pj.saneamiento AS saneamient, eiel_t_pj.electricidad AS electricid, eiel_t_pj.comedor, eiel_t_pj.juegos_inf, eiel_t_pj.otras, eiel_t_pj.acceso_s_ruedas, eiel_t_pj.estado
   FROM eiel_t_pj
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_pj.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_pj.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_pj.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_pj.clave, eiel_t_pj.codprov AS provincia, eiel_t_pj.codmunic AS municipio, eiel_t_pj.codentidad AS entidad, eiel_t_pj.codpoblamiento AS poblamient, eiel_t_pj.orden_pj AS orden_parq, eiel_t_pj.nombre, eiel_t_pj.tipo AS tipo_parq, eiel_t_pj.titular, eiel_t_pj.gestor AS gestion, eiel_t_pj.s_cubierta AS s_cubi, eiel_t_pj.s_aire, eiel_t_pj.s_solar AS s_sola, eiel_t_pj.agua, eiel_t_pj.saneamiento AS saneamient, eiel_t_pj.electricidad AS electricid, eiel_t_pj.comedor, eiel_t_pj.juegos_inf, eiel_t_pj.otras, eiel_t_pj.acceso_s_ruedas, eiel_t_pj.estado
   FROM eiel_t_pj
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_pj.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_pj.codmunic::text
  RIGHT JOIN eiel_t_poblamiento ON   eiel_t_pj.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_pj.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_pj.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_pj.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_pj.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_parque OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_parque TO geopista;
GRANT SELECT ON TABLE check_mpt_parque TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Parques no referenciados con ningún poblamiento') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Parques no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Parques no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Parques no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Parques no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Parques no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_parque_reference_poblamiento','check_mpt_parque',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO33";





CREATE OR REPLACE VIEW check_mpt_lonja_merc_feria AS 
 SELECT eiel_t_lm.clave, eiel_t_lm.codprov AS provincia, eiel_t_lm.codmunic AS municipio, eiel_t_lm.codentidad AS entidad, eiel_t_lm.codpoblamiento AS poblamient, eiel_t_lm.orden_lm AS orden_lmf, eiel_t_lm.nombre, eiel_t_lm.tipo AS tipo_lonj, eiel_t_lm.titular, eiel_t_lm.gestor AS gestion, eiel_t_lm.s_cubierta AS s_cubi, eiel_t_lm.s_aire, eiel_t_lm.s_solar AS s_sola, eiel_t_lm.acceso_s_ruedas, eiel_t_lm.estado
   FROM eiel_t_lm
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_lm.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_lm.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_lm.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_lm.clave, eiel_t_lm.codprov AS provincia, eiel_t_lm.codmunic AS municipio, eiel_t_lm.codentidad AS entidad, eiel_t_lm.codpoblamiento AS poblamient, eiel_t_lm.orden_lm AS orden_lmf, eiel_t_lm.nombre, eiel_t_lm.tipo AS tipo_lonj, eiel_t_lm.titular, eiel_t_lm.gestor AS gestion, eiel_t_lm.s_cubierta AS s_cubi, eiel_t_lm.s_aire, eiel_t_lm.s_solar AS s_sola, eiel_t_lm.acceso_s_ruedas, eiel_t_lm.estado
   FROM eiel_t_lm
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_lm.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_lm.codmunic::text
  RIGHT JOIN eiel_t_poblamiento ON eiel_t_lm.codprov=eiel_t_poblamiento.codprov AND eiel_t_poblamiento.codmunic=eiel_t_lm.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_lm.codentidad AND eiel_t_poblamiento.codpoblamiento= eiel_t_lm.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_lm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_lonja_merc_feria OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_lonja_merc_feria TO geopista;
GRANT SELECT ON TABLE check_mpt_lonja_merc_feria TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Lonjas Mercados Ferias no referenciados con ningún poblamiento') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Lonjas Mercados Ferias no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Lonjas Mercados Ferias no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Lonjas Mercados Ferias no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Lonjas Mercados Ferias no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Lonjas Mercados Ferias no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_lonja_merc_feria_reference_poblamiento','check_mpt_lonja_merc_feria',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO34";



CREATE OR REPLACE VIEW check_mpt_plan_urbanistico AS 
 SELECT eiel_t_planeam_urban.codprov AS provincia, eiel_t_planeam_urban.codmunic AS municipio, eiel_t_planeam_urban.tipo_urba, eiel_t_planeam_urban.estado_tramit AS estado_tra, eiel_t_planeam_urban.denominacion AS denominaci, eiel_t_planeam_urban.sup_muni AS superficie, eiel_t_planeam_urban.fecha_bo AS bo, eiel_t_planeam_urban.s_urbano AS urban, eiel_t_planeam_urban.s_no_urbanizable AS no_urbable, eiel_t_planeam_urban.s_no_urban_especial AS nourbable_
   FROM eiel_t_planeam_urban
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_planeam_urban.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_planeam_urban.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_planeam_urban.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_planeam_urban.codprov AS provincia, eiel_t_planeam_urban.codmunic AS municipio, eiel_t_planeam_urban.tipo_urba, eiel_t_planeam_urban.estado_tramit AS estado_tra, eiel_t_planeam_urban.denominacion AS denominaci, eiel_t_planeam_urban.sup_muni AS superficie, eiel_t_planeam_urban.fecha_bo AS bo, eiel_t_planeam_urban.s_urbano AS urban, eiel_t_planeam_urban.s_no_urbanizable AS no_urbable, eiel_t_planeam_urban.s_no_urban_especial AS nourbable_
   FROM eiel_t_planeam_urban
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_planeam_urban.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_planeam_urban.codmunic::text
  RIGHT JOIN eiel_t_mun_diseminados on eiel_t_mun_diseminados.codprov=eiel_t_planeam_urban.codprov AND eiel_t_planeam_urban.codmunic=eiel_t_mun_diseminados.codmunic
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_planeam_urban.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_plan_urbanistico OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_plan_urbanistico TO geopista;
GRANT SELECT ON TABLE check_mpt_plan_urbanistico TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Lonjas Mercados Ferias no referenciados con ningún poblamiento') THEN	


		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Lonjas Mercados Ferias no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Planeamientos Urbanístico no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Planeamientos Urbanístico no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Planeamientos Urbanístico no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Planeamientos Urbanístico no referenciados con ningún municipio encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('PLAN_URBANISTICO_OTROS','check_plan_urbanistico_reference_mun_enc_dis','check_mpt_plan_urbanistico',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO35";




CREATE OR REPLACE VIEW check_mpt_ot_serv_municipal AS 
 SELECT eiel_t_otros_serv_munic.codprov AS provincia, eiel_t_otros_serv_munic.codmunic AS municipio, eiel_t_otros_serv_munic.sw_inf_grl, eiel_t_otros_serv_munic.sw_inf_tur, eiel_t_otros_serv_munic.sw_gb_elec, eiel_t_otros_serv_munic.ord_soterr, eiel_t_otros_serv_munic.en_eolica, eiel_t_otros_serv_munic.kw_eolica, eiel_t_otros_serv_munic.en_solar, eiel_t_otros_serv_munic.kw_eolica AS kw_solar, eiel_t_otros_serv_munic.pl_mareo, eiel_t_otros_serv_munic.kw_mareo, eiel_t_otros_serv_munic.ot_energ, eiel_t_otros_serv_munic.kw_ot_energ AS kw_energ, eiel_t_otros_serv_munic.cob_serv_tlf_m AS cob_serv_telf_m, eiel_t_otros_serv_munic.tv_dig_cable
   FROM eiel_t_otros_serv_munic
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_otros_serv_munic.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_otros_serv_munic.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_otros_serv_munic.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_otros_serv_munic.codprov AS provincia, eiel_t_otros_serv_munic.codmunic AS municipio, eiel_t_otros_serv_munic.sw_inf_grl, eiel_t_otros_serv_munic.sw_inf_tur, eiel_t_otros_serv_munic.sw_gb_elec, eiel_t_otros_serv_munic.ord_soterr, eiel_t_otros_serv_munic.en_eolica, eiel_t_otros_serv_munic.kw_eolica, eiel_t_otros_serv_munic.en_solar, eiel_t_otros_serv_munic.kw_eolica AS kw_solar, eiel_t_otros_serv_munic.pl_mareo, eiel_t_otros_serv_munic.kw_mareo, eiel_t_otros_serv_munic.ot_energ, eiel_t_otros_serv_munic.kw_ot_energ AS kw_energ, eiel_t_otros_serv_munic.cob_serv_tlf_m AS cob_serv_telf_m, eiel_t_otros_serv_munic.tv_dig_cable
   FROM eiel_t_otros_serv_munic
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_otros_serv_munic.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_otros_serv_munic.codmunic::text
 RIGHT JOIN eiel_t_mun_diseminados on eiel_t_mun_diseminados.codprov=eiel_t_otros_serv_munic.codprov AND eiel_t_otros_serv_munic.codmunic=eiel_t_mun_diseminados.codmunic
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_otros_serv_munic.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_ot_serv_municipal OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_ot_serv_municipal TO geopista;
GRANT SELECT ON TABLE check_mpt_ot_serv_municipal TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Otros servicios municipales no referenciados con ningún municipio encuestado') THEN	
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Otros servicios municipales no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Otros servicios municipales no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Otros servicios municipales no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Otros servicios municipales no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Otros servicios municipales no referenciados con ningún municipio encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('PLAN_URBANISTICO_OTROS','check_ot_serv_municipal_reference_mun_enc_dis','check_mpt_ot_serv_municipal',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO36";





CREATE OR REPLACE VIEW check_mpt_ramal_sanemaiento AS 
 SELECT eiel_c_saneam_rs.codprov AS provincia, eiel_c_saneam_rs.codmunic AS municipio, eiel_c_saneam_rs.codentidad AS entidad, eiel_c_saneam_rs.codpoblamiento AS nucleo, eiel_c_saneam_rs.material AS tipo_rama, eiel_c_saneam_rs.sist_impulsion AS sist_trans, eiel_c_saneam_rs.estado, eiel_c_saneam_rs.tipo_red_interior AS tipo_red, eiel_c_saneam_rs.titular, eiel_c_saneam_rs.gestor AS gestion, sum(eiel_c_saneam_rs.longitud) AS longit_ram
   FROM eiel_c_saneam_rs
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_rs.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_rs.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_rs.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_saneam_rs.codmunic, eiel_c_saneam_rs.codentidad, eiel_c_saneam_rs.codpoblamiento, eiel_c_saneam_rs.codprov, eiel_c_saneam_rs.material, eiel_c_saneam_rs.sist_impulsion, eiel_c_saneam_rs.estado, eiel_c_saneam_rs.tipo_red_interior, eiel_c_saneam_rs.titular, eiel_c_saneam_rs.gestor
  EXCEPT
 SELECT eiel_c_saneam_rs.codprov AS provincia, eiel_c_saneam_rs.codmunic AS municipio, eiel_c_saneam_rs.codentidad AS entidad, eiel_c_saneam_rs.codpoblamiento AS nucleo, eiel_c_saneam_rs.material AS tipo_rama, eiel_c_saneam_rs.sist_impulsion AS sist_trans, eiel_c_saneam_rs.estado, eiel_c_saneam_rs.tipo_red_interior AS tipo_red, eiel_c_saneam_rs.titular, eiel_c_saneam_rs.gestor AS gestion, sum(eiel_c_saneam_rs.longitud) AS longit_ram
   FROM eiel_c_saneam_rs
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_rs.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_rs.codmunic::text
  RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_c_saneam_rs.codprov AND eiel_t_nucl_encuest_1.codmunic=eiel_c_saneam_rs.codmunic AND eiel_t_nucl_encuest_1.codentidad=eiel_c_saneam_rs.codentidad AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_c_saneam_rs.codpoblamiento
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_c_saneam_rs.codprov AND eiel_t_nucl_encuest_2.codmunic=eiel_c_saneam_rs.codmunic AND eiel_t_nucl_encuest_2.codentidad=eiel_c_saneam_rs.codentidad AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_c_saneam_rs.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_rs.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_saneam_rs.codmunic, eiel_c_saneam_rs.codentidad, eiel_c_saneam_rs.codpoblamiento, eiel_c_saneam_rs.codprov, eiel_c_saneam_rs.material, eiel_c_saneam_rs.sist_impulsion, eiel_c_saneam_rs.estado, eiel_c_saneam_rs.tipo_red_interior, eiel_c_saneam_rs.titular, eiel_c_saneam_rs.gestor;
  
ALTER TABLE check_mpt_ramal_sanemaiento OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_ramal_sanemaiento TO geopista;
GRANT SELECT ON TABLE check_mpt_ramal_sanemaiento TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Ramales de Saneamiento no referenciados con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Ramales de Saneamiento no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Ramales de Saneamiento no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Ramales de Saneamiento no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Ramales de Saneamiento no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Ramales de Saneamiento no referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('SANEAMIENTO','check_ramal_sanemaiento_reference_nucl_encuestado','check_mpt_ramal_sanemaiento',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO37";






CREATE OR REPLACE VIEW check_mpt_sanea_autonomo AS 
 SELECT eiel_t_saneam_au.clave, eiel_t_saneam_au.codprov AS provincia, eiel_t_saneam_au.codmunic AS municipio, eiel_t_saneam_au.codentidad AS entidad, eiel_t_saneam_au.codpoblamiento AS nucleo, eiel_t_saneam_au.tipo_sau AS tipo_sanea, eiel_t_saneam_au.estado_sau AS estado, eiel_t_saneam_au.adecuacion_sau AS adecuacion, eiel_t_saneam_au.sau_vivien, eiel_t_saneam_au.sau_pob_re, eiel_t_saneam_au.sau_pob_es, eiel_t_saneam_au.sau_vi_def, eiel_t_saneam_au.sau_pob_re_def AS sau_re_def, eiel_t_saneam_au.sau_pob_es_def AS sau_es_def
   FROM eiel_t_saneam_au
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_au.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_au.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_au.revision_expirada = 9999999999::bigint::numeric
 EXCEPT
 SELECT eiel_t_saneam_au.clave, eiel_t_saneam_au.codprov AS provincia, eiel_t_saneam_au.codmunic AS municipio, eiel_t_saneam_au.codentidad AS entidad, eiel_t_saneam_au.codpoblamiento AS nucleo, eiel_t_saneam_au.tipo_sau AS tipo_sanea, eiel_t_saneam_au.estado_sau AS estado, eiel_t_saneam_au.adecuacion_sau AS adecuacion, eiel_t_saneam_au.sau_vivien, eiel_t_saneam_au.sau_pob_re, eiel_t_saneam_au.sau_pob_es, eiel_t_saneam_au.sau_vi_def, eiel_t_saneam_au.sau_pob_re_def AS sau_re_def, eiel_t_saneam_au.sau_pob_es_def AS sau_es_def
   FROM eiel_t_saneam_au
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_au.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_au.codmunic::text
  RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_t_saneam_au.codprov AND eiel_t_nucl_encuest_1.codmunic=eiel_t_saneam_au.codmunic AND eiel_t_nucl_encuest_1.codentidad=eiel_t_saneam_au.codentidad AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_t_saneam_au.codpoblamiento
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_t_saneam_au.codprov AND eiel_t_nucl_encuest_2.codmunic=eiel_t_saneam_au.codmunic AND eiel_t_nucl_encuest_2.codentidad=eiel_t_saneam_au.codentidad AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_t_saneam_au.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_au.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_sanea_autonomo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_sanea_autonomo TO geopista;
GRANT SELECT ON TABLE check_mpt_sanea_autonomo TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Saneamientos autónomos no referenciados con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Saneamientos autónomos no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Saneamientos autónomos no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Saneamientos autónomos no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Saneamientos autónomos no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Saneamientos autónomos no referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('SANEAMIENTO','check_sanea_autonomo_reference_nucl_encuestado','check_mpt_sanea_autonomo',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO38";





CREATE OR REPLACE VIEW check_mpt_trat_potabilizacion AS 
 SELECT eiel_t_abast_tp.clave, eiel_t_abast_tp.codprov AS provincia, eiel_t_abast_tp.codmunic AS municipio, eiel_t_abast_tp.orden_tp AS orden_trat
   FROM eiel_t_abast_tp
  WHERE eiel_t_abast_tp.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_abast_tp.clave, eiel_t_abast_tp.codprov AS provincia, eiel_t_abast_tp.codmunic AS municipio, eiel_t_abast_tp.orden_tp AS orden_trat
   FROM eiel_t_abast_tp
  RIGHT JOIN eiel_c_municipios on eiel_t_abast_tp.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t_abast_tp.codmunic
  WHERE eiel_t_abast_tp.revision_expirada = 9999999999::bigint::numeric;
  
ALTER TABLE check_mpt_trat_potabilizacion OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_trat_potabilizacion TO geopista;
GRANT SELECT ON TABLE check_mpt_trat_potabilizacion TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('TRAT_POTABILIZACION','check_trat_potabilizacion_reference_provincia','check_mpt_trat_potabilizacion',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO39";




CREATE OR REPLACE VIEW check_mpt_trat_pota_nucleo AS 
 SELECT eiel_tr_abast_tp_pobl.codprov_pobl AS provincia, eiel_tr_abast_tp_pobl.codmunic_pobl AS municipio, eiel_tr_abast_tp_pobl.codentidad_pobl AS entidad, eiel_tr_abast_tp_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_tp_pobl.clave_tp AS clave, eiel_tr_abast_tp_pobl.codprov_tp AS po_provin, eiel_tr_abast_tp_pobl.codmunic_tp AS po_munipi, eiel_tr_abast_tp_pobl.orden_tp AS orden_trat
   FROM eiel_tr_abast_tp_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_tp_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_tp_pobl.codmunic_pobl::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_tp_pobl.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_tr_abast_tp_pobl.codprov_pobl AS provincia, eiel_tr_abast_tp_pobl.codmunic_pobl AS municipio, eiel_tr_abast_tp_pobl.codentidad_pobl AS entidad, eiel_tr_abast_tp_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_tp_pobl.clave_tp AS clave, eiel_tr_abast_tp_pobl.codprov_tp AS po_provin, eiel_tr_abast_tp_pobl.codmunic_tp AS po_munipi, eiel_tr_abast_tp_pobl.orden_tp AS orden_trat
   FROM eiel_tr_abast_tp_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_tp_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_tp_pobl.codmunic_pobl::text
  RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_tr_abast_tp_pobl.codprov_pobl AND eiel_t_nucl_encuest_1.codmunic=eiel_tr_abast_tp_pobl.codmunic_pobl AND eiel_t_nucl_encuest_1.codentidad=eiel_tr_abast_tp_pobl.codentidad_pobl AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_tr_abast_tp_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_tr_abast_tp_pobl.codprov_pobl AND eiel_t_nucl_encuest_2.codmunic=eiel_tr_abast_tp_pobl.codmunic_pobl AND eiel_t_nucl_encuest_2.codentidad=eiel_tr_abast_tp_pobl.codentidad_pobl AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_tr_abast_tp_pobl.codpoblamiento_pobl
   RIGHT JOIN eiel_t_abast_tp on eiel_t_abast_tp.codprov=eiel_tr_abast_tp_pobl.codprov_tp AND eiel_t_abast_tp.codmunic=eiel_tr_abast_tp_pobl.codmunic_tp AND eiel_t_abast_tp.orden_tp=eiel_tr_abast_tp_pobl.orden_tp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_tp_pobl.revision_expirada = 9999999999::bigint::numeric;


ALTER TABLE check_mpt_trat_pota_nucleo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_trat_pota_nucleo TO geopista;
GRANT SELECT ON TABLE check_mpt_trat_pota_nucleo TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('TRAT_POTABILIZACION','check_trat_pota_nucleo_reference_nuc_encuestado','check_mpt_trat_pota_nucleo',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO39";







CREATE OR REPLACE VIEW check_mpt_vertedero AS 
 SELECT eiel_t_vt.clave, eiel_t_vt.codprov AS provincia, eiel_t_vt.codmunic AS municipio, eiel_t_vt.orden_vt AS orden_ver
   FROM eiel_t_vt
  WHERE eiel_t_vt.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_vt.clave, eiel_t_vt.codprov AS provincia, eiel_t_vt.codmunic AS municipio, eiel_t_vt.orden_vt AS orden_ver
   FROM eiel_t_vt
RIGHT JOIN eiel_c_municipios on eiel_t_vt.codprov=eiel_c_municipios.codprov AND eiel_c_municipios.codmunic=eiel_t_vt.codmunic
  WHERE eiel_t_vt.revision_expirada = 9999999999::bigint::numeric;
  
ALTER TABLE check_mpt_vertedero OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_vertedero TO geopista;
GRANT SELECT ON TABLE check_mpt_vertedero TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Vertederos no referenciados con ninguna Provincia ni Municipio') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Vertederos no referenciados con ninguna Provincia ni Municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Vertederos no referenciados con ninguna Provincia ni Municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Vertederos no referenciados con ninguna Provincia ni Municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Vertederos no referenciados con ninguna Provincia ni Municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Vertederos no referenciados con ninguna Provincia ni Municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('VERTEDERO','check_vertedero_reference_provincia','check_mpt_vertedero',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO40";



CREATE OR REPLACE VIEW check_mpt_vertedero_nucleo AS 
 SELECT DISTINCT eiel_tr_vt_pobl.codprov AS provincia, eiel_tr_vt_pobl.codmunic AS municipio, eiel_tr_vt_pobl.codentidad AS entidad, eiel_tr_vt_pobl.codpoblamiento AS nucleo, eiel_tr_vt_pobl.clave_vt AS clave, eiel_tr_vt_pobl.codprov_vt AS ver_provin, eiel_tr_vt_pobl.codmunic_vt AS ver_munici, eiel_tr_vt_pobl.orden_vt AS ver_codigo
   FROM eiel_tr_vt_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_vt_pobl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_vt_pobl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_vt_pobl.revision_expirada = 9999999999::bigint::numeric
  EXCEPT
 SELECT DISTINCT eiel_tr_vt_pobl.codprov AS provincia, eiel_tr_vt_pobl.codmunic AS municipio, eiel_tr_vt_pobl.codentidad AS entidad, eiel_tr_vt_pobl.codpoblamiento AS nucleo, eiel_tr_vt_pobl.clave_vt AS clave, eiel_tr_vt_pobl.codprov_vt AS ver_provin, eiel_tr_vt_pobl.codmunic_vt AS ver_munici, eiel_tr_vt_pobl.orden_vt AS ver_codigo
   FROM eiel_tr_vt_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_vt_pobl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_vt_pobl.codmunic::text
  RIGHT JOIN eiel_t_nucl_encuest_1 on eiel_t_nucl_encuest_1.codprov=eiel_tr_vt_pobl.codprov AND eiel_t_nucl_encuest_1.codmunic=eiel_tr_vt_pobl.codmunic AND eiel_t_nucl_encuest_1.codentidad=eiel_tr_vt_pobl.codentidad AND eiel_t_nucl_encuest_1.codpoblamiento=eiel_tr_vt_pobl.codpoblamiento
   RIGHT JOIN eiel_t_nucl_encuest_2 on eiel_t_nucl_encuest_2.codprov=eiel_tr_vt_pobl.codprov AND eiel_t_nucl_encuest_2.codmunic=eiel_tr_vt_pobl.codmunic AND eiel_t_nucl_encuest_2.codentidad=eiel_tr_vt_pobl.codentidad AND eiel_t_nucl_encuest_2.codpoblamiento=eiel_tr_vt_pobl.codpoblamiento
   RIGHT JOIN eiel_t_vt on eiel_t_vt.codprov=eiel_tr_vt_pobl.codprov_vt AND eiel_t_vt.codmunic=eiel_tr_vt_pobl.codmunic_vt AND eiel_t_vt.orden_vt=eiel_tr_vt_pobl.orden_vt
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_vt_pobl.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE check_mpt_vertedero_nucleo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_vertedero_nucleo TO geopista;
GRANT SELECT ON TABLE check_mpt_vertedero_nucleo TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'No existen como Vertedero o no están referenciados con ningún núcleo encuestado') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Vertedero o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Vertedero o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Vertedero o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Vertedero o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Vertedero o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('VERTEDERO','check_vertedero_nucleo_reference_nuc_encuestado','check_mpt_vertedero_nucleo',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO41";






CREATE OR REPLACE VIEW check_mpt_municipio AS 
 SELECT eiel_c_municipios.codprov AS provincia, eiel_c_municipios.codmunic AS municipio, eiel_c_municipios.nombre_oficial AS denominaci
   FROM eiel_c_municipios
  WHERE eiel_c_municipios.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_c_municipios.codprov AS provincia, eiel_c_municipios.codmunic AS municipio, eiel_c_municipios.nombre_oficial AS denominaci
   FROM eiel_c_municipios
   RIGHT JOIN eiel_c_provincia on eiel_c_provincia.codprov=eiel_c_municipios.codprov
  WHERE eiel_c_municipios.revision_expirada = 9999999999::bigint::numeric;
 
ALTER TABLE check_mpt_municipio OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_municipio TO geopista;
GRANT SELECT ON TABLE check_mpt_municipio TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Municipios no referenciados con ninguna provincia') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Municipios no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Municipios no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Municipios no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Municipios no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Municipios no referenciados con ninguna provincia');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_municipio_reference_provincia','check_mpt_municipio',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO42";




CREATE OR REPLACE VIEW check_mpt_cabildo_consejo AS 
 SELECT eiel_t_cabildo_consejo.codprov AS provincia, eiel_t_cabildo_consejo.cod_isla AS isla, eiel_t_cabildo_consejo.denominacion AS denominaci
   FROM eiel_t_cabildo_consejo
  WHERE eiel_t_cabildo_consejo.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_cabildo_consejo.codprov AS provincia, eiel_t_cabildo_consejo.cod_isla AS isla, eiel_t_cabildo_consejo.denominacion AS denominaci
   FROM eiel_t_cabildo_consejo
RIGHT JOIN eiel_c_provincia on eiel_c_provincia.codprov=eiel_t_cabildo_consejo.codprov
  WHERE eiel_t_cabildo_consejo.revision_expirada = 9999999999::bigint::numeric;
 
ALTER TABLE check_mpt_cabildo_consejo OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_cabildo_consejo TO geopista;
GRANT SELECT ON TABLE check_mpt_cabildo_consejo TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Cabildos no referenciados con ninguna provincia') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Cabildos no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Cabildos no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Cabildos no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Cabildos no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Cabildos no referenciados con ninguna provincia');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_cabildo_reference_provincia','check_mpt_cabildo_consejo',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO43";




CREATE OR REPLACE VIEW check_mpt_entidad_sing AS 
SELECT eiel_t_entidad_singular.codprov AS provincia, eiel_t_entidad_singular.codmunic AS municipio, eiel_t_entidad_singular.codentidad AS entidad, eiel_t_entidad_singular.denominacion AS denominaci
   FROM eiel_t_entidad_singular
  WHERE eiel_t_entidad_singular.revision_expirada = 9999999999::bigint::numeric
  EXCEPT
SELECT eiel_t_entidad_singular.codprov AS provincia, eiel_t_entidad_singular.codmunic AS municipio, eiel_t_entidad_singular.codentidad AS entidad, eiel_t_entidad_singular.denominacion AS denominaci
   FROM eiel_t_entidad_singular
  RIGHT JOIN eiel_c_municipios on eiel_c_municipios.codprov=eiel_t_entidad_singular.codprov AND eiel_c_municipios.codmunic=eiel_t_entidad_singular.codmunic
  WHERE eiel_t_entidad_singular.revision_expirada = 9999999999::bigint::numeric;
ALTER TABLE check_mpt_entidad_sing OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_entidad_sing TO geopista;
GRANT SELECT ON TABLE check_mpt_entidad_sing TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Entidades Singulares no referenciadas con ningún municipio') THEN	
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Entidades Singulares no referenciadas con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Entidades Singulares no referenciadas con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Entidades Singulares no referenciadas con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Entidades Singulares no referenciadas con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Entidades Singulares no referenciadas con ningún municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_entidad_sing_reference_municipio','check_mpt_entidad_sing',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO44";




CREATE OR REPLACE VIEW check_mpt_poblamiento AS 
 SELECT eiel_t_poblamiento.codprov AS provincia, eiel_t_poblamiento.codmunic AS municipio, eiel_t_poblamiento.codentidad AS entidad, eiel_t_poblamiento.codpoblamiento AS poblamiento
   FROM eiel_t_poblamiento
  WHERE eiel_t_poblamiento.revision_expirada = 9999999999::bigint::numeric
  EXCEPT
 SELECT eiel_t_poblamiento.codprov AS provincia, eiel_t_poblamiento.codmunic AS municipio, eiel_t_poblamiento.codentidad AS entidad, eiel_t_poblamiento.codpoblamiento AS poblamiento
   FROM eiel_t_poblamiento
  RIGHT JOIN eiel_t_entidad_singular on eiel_t_poblamiento.codprov=eiel_t_entidad_singular.codprov AND eiel_t_poblamiento.codmunic=eiel_t_entidad_singular.codmunic AND eiel_t_poblamiento.codentidad=eiel_t_entidad_singular.codentidad
  WHERE eiel_t_poblamiento.revision_expirada = 9999999999::bigint::numeric;
ALTER TABLE check_mpt_poblamiento OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_poblamiento TO geopista;
GRANT SELECT ON TABLE check_mpt_poblamiento TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Poblamientos no referenciados con ningún entidad singular') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Poblamientos no referenciados con ningún entidad singular');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Poblamientos no referenciados con ningún entidad singular');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Poblamientos no referenciados con ningún entidad singular');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Poblamientos no referenciados con ningún entidad singular');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Poblamientos no referenciados con ningún entidad singular');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_poblamiento_reference_entidad_sing','check_mpt_poblamiento',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO45";




CREATE OR REPLACE VIEW check_mpt_nucleo_poblacion AS 
 SELECT eiel_c_nucleo_poblacion.codprov AS provincia, eiel_c_nucleo_poblacion.codmunic AS municipio, eiel_c_nucleo_poblacion.codentidad AS entidad, eiel_c_nucleo_poblacion.codpoblamiento AS poblamient, eiel_c_nucleo_poblacion.nombre_oficial AS denominaci
   FROM eiel_c_nucleo_poblacion
  WHERE eiel_c_nucleo_poblacion.revision_expirada = 9999999999::bigint::numeric
 EXCEPT
 SELECT eiel_c_nucleo_poblacion.codprov AS provincia, eiel_c_nucleo_poblacion.codmunic AS municipio, eiel_c_nucleo_poblacion.codentidad AS entidad, eiel_c_nucleo_poblacion.codpoblamiento AS poblamient, eiel_c_nucleo_poblacion.nombre_oficial AS denominaci
 FROM eiel_c_nucleo_poblacion
 RIGHT JOIN eiel_t_poblamiento on eiel_t_poblamiento.codprov=eiel_c_nucleo_poblacion.codprov AND eiel_t_poblamiento.codmunic=eiel_c_nucleo_poblacion.codmunic AND eiel_t_poblamiento.codentidad=eiel_c_nucleo_poblacion.codentidad AND eiel_c_nucleo_poblacion.codpoblamiento=eiel_t_poblamiento.codpoblamiento
  WHERE eiel_c_nucleo_poblacion.revision_expirada = 9999999999::bigint::numeric;
  
ALTER TABLE check_mpt_nucleo_poblacion OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_nucleo_poblacion TO geopista;
GRANT SELECT ON TABLE check_mpt_nucleo_poblacion TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Núcleos de población no referenciados con ningún poblamiento') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos de población no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos de población no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos de población no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos de población no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos de población no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_nucleo_poblacion_reference_poblamiento','check_mpt_nucleo_poblacion',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO46";




CREATE OR REPLACE VIEW check_mpt_nucleo_abandonado AS 
 SELECT eiel_t_nucleo_aband.codprov AS provincia, eiel_t_nucleo_aband.codmunic AS municipio, eiel_t_nucleo_aband.codentidad AS entidad, eiel_t_nucleo_aband.codpoblamiento AS poblamient, eiel_t_nucleo_aband.a_abandono, eiel_t_nucleo_aband.causa_abandono AS causa_aban, eiel_t_nucleo_aband.titular_abandono AS titular_ab, eiel_t_nucleo_aband.rehabilitacion AS rehabilita, eiel_t_nucleo_aband.acceso AS acceso_nuc, eiel_t_nucleo_aband.serv_agua, eiel_t_nucleo_aband.serv_elect
   FROM eiel_t_nucleo_aband
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucleo_aband.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucleo_aband.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucleo_aband.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_nucleo_aband.codprov AS provincia, eiel_t_nucleo_aband.codmunic AS municipio, eiel_t_nucleo_aband.codentidad AS entidad, eiel_t_nucleo_aband.codpoblamiento AS poblamient, eiel_t_nucleo_aband.a_abandono, eiel_t_nucleo_aband.causa_abandono AS causa_aban, eiel_t_nucleo_aband.titular_abandono AS titular_ab, eiel_t_nucleo_aband.rehabilitacion AS rehabilita, eiel_t_nucleo_aband.acceso AS acceso_nuc, eiel_t_nucleo_aband.serv_agua, eiel_t_nucleo_aband.serv_elect
   FROM eiel_t_nucleo_aband
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucleo_aband.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucleo_aband.codmunic::text
RIGHT JOIN eiel_c_nucleo_poblacion on eiel_t_nucleo_aband.codprov=eiel_c_nucleo_poblacion.codprov AND eiel_t_nucleo_aband.codmunic=eiel_c_nucleo_poblacion.codmunic AND eiel_t_nucleo_aband.codentidad=eiel_c_nucleo_poblacion.codentidad AND eiel_c_nucleo_poblacion.codpoblamiento=eiel_t_nucleo_aband.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucleo_aband.revision_expirada = 9999999999::bigint::numeric;

  
ALTER TABLE check_mpt_nucleo_abandonado OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_nucleo_abandonado TO geopista;
GRANT SELECT ON TABLE check_mpt_nucleo_abandonado TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Núcleos abandonados no referenciados con ningún núcleo de población') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos abandonados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos abandonados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos abandonados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos abandonados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos abandonados no referenciados con ningún núcleo de población');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_nucleo_abandonado_reference_poblamiento','check_mpt_nucleo_abandonado',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO47";





CREATE OR REPLACE VIEW check_mpt_nucleo_encuestado AS 
 SELECT eiel_t_nucl_encuest_1.codprov AS provincia, eiel_t_nucl_encuest_1.codmunic AS municipio, eiel_t_nucl_encuest_1.codentidad AS entidad, eiel_t_nucl_encuest_1.codpoblamiento AS nucleo, eiel_t_nucl_encuest_1.padron, eiel_t_nucl_encuest_1.pob_estacional AS pob_estaci, eiel_t_nucl_encuest_1.altitud, eiel_t_nucl_encuest_1.viviendas_total AS viv_total, eiel_t_nucl_encuest_1.hoteles, eiel_t_nucl_encuest_1.casas_rural AS casas_rura, eiel_t_nucl_encuest_1.accesibilidad AS accesib
   FROM eiel_t_nucl_encuest_1
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucl_encuest_1.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucl_encuest_1.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_nucl_encuest_1.codprov AS provincia, eiel_t_nucl_encuest_1.codmunic AS municipio, eiel_t_nucl_encuest_1.codentidad AS entidad, eiel_t_nucl_encuest_1.codpoblamiento AS nucleo, eiel_t_nucl_encuest_1.padron, eiel_t_nucl_encuest_1.pob_estacional AS pob_estaci, eiel_t_nucl_encuest_1.altitud, eiel_t_nucl_encuest_1.viviendas_total AS viv_total, eiel_t_nucl_encuest_1.hoteles, eiel_t_nucl_encuest_1.casas_rural AS casas_rura, eiel_t_nucl_encuest_1.accesibilidad AS accesib
   FROM eiel_t_nucl_encuest_1
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucl_encuest_1.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucl_encuest_1.codmunic::text
RIGHT JOIN eiel_c_nucleo_poblacion on eiel_t_nucl_encuest_1.codprov=eiel_c_nucleo_poblacion.codprov AND eiel_t_nucl_encuest_1.codmunic=eiel_c_nucleo_poblacion.codmunic AND eiel_t_nucl_encuest_1.codentidad=eiel_c_nucleo_poblacion.codentidad AND eiel_c_nucleo_poblacion.codpoblamiento=eiel_t_nucl_encuest_1.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric;

  
ALTER TABLE check_mpt_nucleo_encuestado OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_nucleo_encuestado TO geopista;
GRANT SELECT ON TABLE check_mpt_nucleo_encuestado TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Núcleos encuestados no referenciados con ningún núcleo de población') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos encuestados no referenciados con ningún núcleo de población');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_nucleo_encuestado_reference_nucleo_poblacion','check_mpt_nucleo_encuestado',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO47";





CREATE OR REPLACE VIEW check_mpt_nucleo_encuestado_2 AS 
 SELECT eiel_t_nucl_encuest_2.codprov AS provincia, eiel_t_nucl_encuest_2.codmunic AS municipio, eiel_t_nucl_encuest_2.codentidad AS entidad, eiel_t_nucl_encuest_2.codpoblamiento AS nucleo, eiel_t_nucl_encuest_2.aag_caudal, eiel_t_nucl_encuest_2.aag_restri, eiel_t_nucl_encuest_2.aag_contad, eiel_t_nucl_encuest_2.aag_tasa, eiel_t_nucl_encuest_2.aag_instal, eiel_t_nucl_encuest_2.aag_hidran, eiel_t_nucl_encuest_2.aag_est_hi, eiel_t_nucl_encuest_2.aag_valvul, eiel_t_nucl_encuest_2.aag_est_va, eiel_t_nucl_encuest_2.aag_bocasr, eiel_t_nucl_encuest_2.aag_est_bo, eiel_t_nucl_encuest_2.cisterna
   FROM eiel_t_nucl_encuest_2
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucl_encuest_2.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucl_encuest_2.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_2.revision_expirada = 9999999999::bigint::numeric
EXCEPT
 SELECT eiel_t_nucl_encuest_2.codprov AS provincia, eiel_t_nucl_encuest_2.codmunic AS municipio, eiel_t_nucl_encuest_2.codentidad AS entidad, eiel_t_nucl_encuest_2.codpoblamiento AS nucleo, eiel_t_nucl_encuest_2.aag_caudal, eiel_t_nucl_encuest_2.aag_restri, eiel_t_nucl_encuest_2.aag_contad, eiel_t_nucl_encuest_2.aag_tasa, eiel_t_nucl_encuest_2.aag_instal, eiel_t_nucl_encuest_2.aag_hidran, eiel_t_nucl_encuest_2.aag_est_hi, eiel_t_nucl_encuest_2.aag_valvul, eiel_t_nucl_encuest_2.aag_est_va, eiel_t_nucl_encuest_2.aag_bocasr, eiel_t_nucl_encuest_2.aag_est_bo, eiel_t_nucl_encuest_2.cisterna
   FROM eiel_t_nucl_encuest_2
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucl_encuest_2.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucl_encuest_2.codmunic::text
RIGHT JOIN eiel_c_nucleo_poblacion on eiel_t_nucl_encuest_2.codprov=eiel_c_nucleo_poblacion.codprov AND eiel_t_nucl_encuest_2.codmunic=eiel_c_nucleo_poblacion.codmunic AND eiel_t_nucl_encuest_2.codentidad=eiel_c_nucleo_poblacion.codentidad AND eiel_c_nucleo_poblacion.codpoblamiento=eiel_t_nucl_encuest_2.codpoblamiento
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_2.revision_expirada = 9999999999::bigint::numeric;

  
ALTER TABLE check_mpt_nucleo_encuestado_2 OWNER TO geopista;
GRANT ALL ON TABLE check_mpt_nucleo_encuestado_2 TO geopista;
GRANT SELECT ON TABLE check_mpt_nucleo_encuestado_2 TO consultas;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Núcleos encuestados no referenciados con ningún núcleo de población') THEN	

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos encuestados no referenciados con ningún núcleo de población');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_nucleo_encuestado_2_reference_nucleo_poblacion','check_mpt_nucleo_encuestado_2',currval('seq_dictionary'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO47";




--Inserta el valor para el panel de las validaciones
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM eiel_validacionesmpt WHERE nombre = 'Integridad Referencial') THEN	
		INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (78,'Integridad Referencial','');	
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO47";

