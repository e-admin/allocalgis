DROP TABLE IF EXISTS eiel_inventario;
CREATE TABLE eiel_inventario
(
   union_clave_eiel character varying(100) NOT NULL, 
   vista_eiel character varying(100) NOT NULL, 
   id_inventario integer NOT NULL,
   epig_inventario numeric(1,0),
   id_municipio numeric(5,0),
   titularidad_municipal character varying(2) NOT NULL
) 
WITH (
  OIDS = TRUE
);
ALTER TABLE eiel_inventario OWNER TO geopista;

DROP INDEX IF EXISTS i_eiel_inventario;
CREATE INDEX i_eiel_inventario
  ON eiel_inventario
  USING btree
  (id_inventario );

DROP INDEX IF EXISTS i_eiel_inventario2;
CREATE INDEX i_eiel_inventario2
  ON eiel_inventario
  USING btree
  (union_clave_eiel );

DROP INDEX IF EXISTS i_eiel_inventario3;
CREATE INDEX i_eiel_inventario3
  ON eiel_inventario
  USING btree
  (union_clave_eiel , vista_eiel , id_municipio );

CREATE OR REPLACE VIEW v_integ_depositos AS 
 SELECT ((eiel_t_abast_de.clave::text || eiel_t_abast_de.codprov::text) || eiel_t_abast_de.codmunic::text) || eiel_t_abast_de.orden_de::text AS union_clave_eiel, ''::text as nombre, eiel_t_abast_de.estado, eiel_t_abast_de.gestor, eiel_c_abast_de.id as id_c
   FROM eiel_t_abast_de left join eiel_c_abast_de on eiel_c_abast_de.clave = eiel_t_abast_de.clave and eiel_c_abast_de.codprov = eiel_t_abast_de.codprov and eiel_t_abast_de.codmunic = eiel_c_abast_de.codmunic and eiel_c_abast_de.orden_de = eiel_t_abast_de.orden_de
  WHERE eiel_t_abast_de.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_depositos OWNER TO geopista;

CREATE OR REPLACE VIEW v_integ_centroasistencial AS 
 SELECT (eiel_t_as.clave || eiel_t_as.codprov || eiel_t_as.codmunic || eiel_t_as.codentidad || eiel_t_as.codpoblamiento || eiel_t_as.orden_as) AS union_clave_eiel, eiel_t_as.nombre, eiel_t_as.estado, eiel_t_as.gestor, eiel_c_as.id as id_c
   FROM eiel_t_as left join eiel_c_as on eiel_t_as.clave = eiel_c_as.clave and eiel_t_as.codprov = eiel_c_as.codprov and eiel_t_as.codmunic = eiel_c_as.codmunic and eiel_t_as.codentidad = eiel_c_as.codentidad and eiel_t_as.codpoblamiento = eiel_c_as.codpoblamiento and eiel_t_as.orden_as = eiel_c_as.orden_as
  WHERE eiel_t_as.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_centroasistencial OWNER TO geopista;

CREATE OR REPLACE VIEW v_integ_casaconsistorial AS 
 SELECT (eiel_t_cc.clave || eiel_t_cc.codprov || eiel_t_cc.codmunic || eiel_t_cc.codentidad || eiel_t_cc.codpoblamiento || eiel_t_cc.orden_cc) AS union_clave_eiel, eiel_t_cc.nombre, eiel_t_cc.estado, ''::text as gestor, eiel_c_cc.id as id_c
   FROM eiel_t_cc left join eiel_c_cc on eiel_t_cc.clave = eiel_c_cc.clave and eiel_t_cc.codprov = eiel_c_cc.codprov and eiel_t_cc.codmunic = eiel_c_cc.codmunic and eiel_t_cc.codentidad = eiel_c_cc.codentidad and eiel_t_cc.codpoblamiento = eiel_c_cc.codpoblamiento  and eiel_t_cc.orden_cc = eiel_c_cc.orden_cc
  WHERE eiel_t_cc.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_casaconsistorial OWNER TO geopista;  
  
CREATE OR REPLACE VIEW v_integ_cementerio AS 
 SELECT (eiel_t_ce.clave || eiel_t_ce.codprov || eiel_t_ce.codmunic || eiel_t_ce.codentidad || eiel_t_ce.codpoblamiento || eiel_t_ce.orden_ce) AS union_clave_eiel, eiel_t_ce.nombre, ''::text as estado, ''::text as gestor, eiel_c_ce.id as id_c
   FROM eiel_t_ce left join eiel_c_ce on eiel_t_ce.clave = eiel_c_ce.clave and eiel_t_ce.codprov = eiel_c_ce.codprov and eiel_t_ce.codmunic = eiel_c_ce.codmunic and eiel_t_ce.codentidad = eiel_c_ce.codentidad and eiel_t_ce.codpoblamiento = eiel_c_ce.codpoblamiento  and eiel_t_ce.orden_ce = eiel_c_ce.orden_ce
  WHERE eiel_t_ce.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_cementerio OWNER TO geopista;
  
CREATE OR REPLACE VIEW v_integ_centrocultural AS 
 SELECT (eiel_t_cu.clave || eiel_t_cu.codprov || eiel_t_cu.codmunic || eiel_t_cu.codentidad || eiel_t_cu.codpoblamiento || eiel_t_cu.orden_cu) AS union_clave_eiel, eiel_t_cu.nombre, eiel_t_cu.estado, eiel_t_cu.gestor, eiel_c_cu.id as id_c
   FROM eiel_t_cu left join eiel_c_cu on eiel_t_cu.clave = eiel_c_cu.clave and eiel_t_cu.codprov = eiel_c_cu.codprov and eiel_t_cu.codmunic = eiel_c_cu.codmunic and eiel_t_cu.codentidad = eiel_c_cu.codentidad and eiel_t_cu.codpoblamiento = eiel_c_cu.codpoblamiento  and eiel_t_cu.orden_cu = eiel_c_cu.orden_cu
  WHERE eiel_t_cu.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_centrocultural OWNER TO geopista;
  
CREATE OR REPLACE VIEW v_integ_centroensenianza AS 
 SELECT (eiel_t_en.clave || eiel_t_en.codprov || eiel_t_en.codmunic || eiel_t_en.codentidad || eiel_t_en.codpoblamiento || eiel_t_en.orden_en) AS union_clave_eiel, eiel_t_en.nombre, eiel_t_en.estado, ''::text as gestor, eiel_c_en.id as id_c
   FROM eiel_t_en left join eiel_c_en on eiel_t_en.clave = eiel_c_en.clave and eiel_t_en.codprov = eiel_c_en.codprov and eiel_t_en.codmunic = eiel_c_en.codmunic and eiel_t_en.codentidad = eiel_c_en.codentidad and eiel_t_en.codpoblamiento = eiel_c_en.codpoblamiento  and eiel_t_en.orden_en = eiel_c_en.orden_en
  WHERE eiel_t_en.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_centroensenianza OWNER TO geopista;

CREATE OR REPLACE VIEW v_integ_instalaciondeportiva AS 
 SELECT (eiel_t_id.clave || eiel_t_id.codprov || eiel_t_id.codmunic || eiel_t_id.codentidad || eiel_t_id.codpoblamiento || eiel_t_id.orden_id) AS union_clave_eiel, eiel_t_id.nombre, eiel_t_id.estado, eiel_t_id.gestor, eiel_c_id.id as id_c
   FROM eiel_t_id left join eiel_c_id on eiel_t_id.clave = eiel_c_id.clave and eiel_t_id.codprov = eiel_c_id.codprov and eiel_t_id.codmunic = eiel_c_id.codmunic and eiel_t_id.codentidad = eiel_c_id.codentidad and eiel_t_id.codpoblamiento = eiel_c_id.codpoblamiento  and eiel_t_id.orden_id = eiel_c_id.orden_id
  WHERE eiel_t_id.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_instalaciondeportiva OWNER TO geopista;

CREATE OR REPLACE VIEW v_integ_incendiosproteccion AS 
 SELECT (eiel_t_ip.clave || eiel_t_ip.codprov || eiel_t_ip.codmunic || eiel_t_ip.codentidad || eiel_t_ip.codpoblamiento || eiel_t_ip.orden_ip) AS union_clave_eiel, eiel_t_ip.nombre, eiel_t_ip.estado, eiel_t_ip.gestor, eiel_c_ip.id as id_c
   FROM eiel_t_ip left join eiel_c_ip on eiel_t_ip.clave = eiel_c_ip.clave and eiel_t_ip.codprov = eiel_c_ip.codprov and eiel_t_ip.codmunic = eiel_c_ip.codmunic and eiel_t_ip.codentidad = eiel_c_ip.codentidad and eiel_t_ip.codpoblamiento = eiel_c_ip.codpoblamiento  and eiel_t_ip.orden_ip = eiel_c_ip.orden_ip
  WHERE eiel_t_ip.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_incendiosproteccion OWNER TO geopista;

CREATE OR REPLACE VIEW v_integ_lonjasmercados AS 
 SELECT (eiel_t_lm.clave || eiel_t_lm.codprov || eiel_t_lm.codmunic || eiel_t_lm.codentidad || eiel_t_lm.codpoblamiento || eiel_t_lm.orden_lm) AS union_clave_eiel, eiel_t_lm.nombre, eiel_t_lm.estado, eiel_t_lm.gestor, eiel_c_lm.id as id_c
   FROM eiel_t_lm left join eiel_c_lm on eiel_t_lm.clave = eiel_c_lm.clave and eiel_t_lm.codprov = eiel_c_lm.codprov and eiel_t_lm.codmunic = eiel_c_lm.codmunic and eiel_t_lm.codentidad = eiel_c_lm.codentidad and eiel_t_lm.codpoblamiento = eiel_c_lm.codpoblamiento  and eiel_t_lm.orden_lm = eiel_c_lm.orden_lm
  WHERE eiel_t_lm.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_lonjasmercados OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_integ_mataderos AS 
 SELECT (eiel_t_mt.clave || eiel_t_mt.codprov || eiel_t_mt.codmunic || eiel_t_mt.codentidad || eiel_t_mt.codpoblamiento || eiel_t_mt.orden_mt) AS union_clave_eiel, eiel_t_mt.nombre, eiel_t_mt.estado, eiel_t_mt.gestor, eiel_c_mt.id as id_c
   FROM eiel_t_mt left join eiel_c_mt on eiel_t_mt.clave = eiel_c_mt.clave and eiel_t_mt.codprov = eiel_c_mt.codprov and eiel_t_mt.codmunic = eiel_c_mt.codmunic and eiel_t_mt.codentidad = eiel_c_mt.codentidad and eiel_t_mt.codpoblamiento = eiel_c_mt.codpoblamiento  and eiel_t_mt.orden_mt = eiel_c_mt.orden_mt
  WHERE eiel_t_mt.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_mataderos OWNER TO geopista;
 
CREATE OR REPLACE VIEW v_integ_parquesjardines AS 
 SELECT (eiel_t_pj.clave || eiel_t_pj.codprov || eiel_t_pj.codmunic || eiel_t_pj.codentidad || eiel_t_pj.codpoblamiento || eiel_t_pj.orden_pj) AS union_clave_eiel, eiel_t_pj.nombre, eiel_t_pj.estado, eiel_t_pj.gestor, eiel_c_pj.id as id_c
   FROM eiel_t_pj left join eiel_c_pj on eiel_t_pj.clave = eiel_c_pj.clave and eiel_t_pj.codprov = eiel_c_pj.codprov and eiel_t_pj.codmunic = eiel_c_pj.codmunic and eiel_t_pj.codentidad = eiel_c_pj.codentidad and eiel_t_pj.codpoblamiento = eiel_c_pj.codpoblamiento  and eiel_t_pj.orden_pj = eiel_c_pj.orden_pj
  WHERE eiel_t_pj.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_parquesjardines OWNER TO geopista;

CREATE OR REPLACE VIEW v_integ_centrosanitario AS 
 SELECT (eiel_t_sa.clave || eiel_t_sa.codprov || eiel_t_sa.codmunic || eiel_t_sa.codentidad || eiel_t_sa.codpoblamiento || eiel_t_sa.orden_sa) AS union_clave_eiel, eiel_t_sa.nombre, eiel_t_sa.estado, eiel_t_sa.gestor, eiel_c_sa.id as id_c
   FROM eiel_t_sa left join eiel_c_sa on eiel_t_sa.clave = eiel_c_sa.clave and eiel_t_sa.codprov = eiel_c_sa.codprov and eiel_t_sa.codmunic = eiel_c_sa.codmunic and eiel_t_sa.codentidad = eiel_c_sa.codentidad and eiel_t_sa.codpoblamiento = eiel_c_sa.codpoblamiento  and eiel_t_sa.orden_sa = eiel_c_sa.orden_sa
  WHERE eiel_t_sa.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_centrosanitario OWNER TO geopista;

CREATE OR REPLACE VIEW v_integ_edificiosinuso AS 
 SELECT (eiel_t_su.clave || eiel_t_su.codprov || eiel_t_su.codmunic || eiel_t_su.codentidad || eiel_t_su.codpoblamiento || eiel_t_su.orden_su) AS union_clave_eiel, eiel_t_su.nombre, eiel_t_su.estado, ''::text as gestor, eiel_c_su.id as id_c
   FROM eiel_t_su left join eiel_c_su on eiel_t_su.clave = eiel_c_su.clave and eiel_t_su.codprov = eiel_c_su.codprov and eiel_t_su.codmunic = eiel_c_su.codmunic and eiel_t_su.codentidad = eiel_c_su.codentidad and eiel_t_su.codpoblamiento = eiel_c_su.codpoblamiento  and eiel_t_su.orden_su = eiel_c_su.orden_su
  WHERE eiel_t_su.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_edificiosinuso OWNER TO geopista;
  
CREATE OR REPLACE VIEW v_integ_tanatorio AS 
 SELECT (eiel_t_ta.clave || eiel_t_ta.codprov || eiel_t_ta.codmunic || eiel_t_ta.codentidad || eiel_t_ta.codpoblamiento || eiel_t_ta.orden_ta) AS union_clave_eiel, eiel_t_ta.nombre, eiel_t_ta.estado,  eiel_t_ta.gestor, eiel_c_ta.id as id_c
   FROM eiel_t_ta left join eiel_c_ta on eiel_t_ta.clave = eiel_c_ta.clave and eiel_t_ta.codprov = eiel_c_ta.codprov and eiel_t_ta.codmunic = eiel_c_ta.codmunic and eiel_t_ta.codentidad = eiel_c_ta.codentidad and eiel_t_ta.codpoblamiento = eiel_c_ta.codpoblamiento  and eiel_t_ta.orden_ta = eiel_c_ta.orden_ta
  WHERE eiel_t_ta.revision_expirada = 9999999999::bigint::numeric;

  ALTER TABLE v_integ_tanatorio OWNER TO geopista;

CREATE OR REPLACE VIEW v_integ_carreteras AS 
 SELECT (eiel_t_carreteras.codprov || eiel_t_carreteras.cod_carrt) AS union_clave_eiel, eiel_t_carreteras.denominacion as nombre, ''::text as estado, ''::text as gestor, eiel_c_tramos_carreteras.id as id_c
   FROM eiel_t_carreteras left join eiel_c_tramos_carreteras on eiel_t_carreteras.codprov = eiel_c_tramos_carreteras.codprov and eiel_t_carreteras.cod_carrt = eiel_c_tramos_carreteras.cod_carrt
  WHERE eiel_t_carreteras.revision_expirada = 9999999999::bigint::numeric;
  
  ALTER TABLE v_integ_carreteras OWNER TO geopista;  

CREATE OR REPLACE VIEW v_integ_depuradoras AS 
 SELECT (eiel_t2_saneam_ed.clave || eiel_t2_saneam_ed.codprov || eiel_t2_saneam_ed.codmunic || eiel_t2_saneam_ed.orden_ed) AS union_clave_eiel, ''::text as nombre, ''::text as estado, eiel_t2_saneam_ed.gestor, eiel_c_saneam_ed.id as id_c
   FROM eiel_t2_saneam_ed left join eiel_c_saneam_ed on eiel_t2_saneam_ed.clave = eiel_c_saneam_ed.clave and eiel_t2_saneam_ed.clave = eiel_c_saneam_ed.clave and eiel_t2_saneam_ed.clave = eiel_c_saneam_ed.clave and eiel_t2_saneam_ed.clave = eiel_c_saneam_ed.clave
  WHERE eiel_t2_saneam_ed.revision_expirada = 9999999999::bigint::numeric;
  
  ALTER TABLE v_integ_depuradoras OWNER TO geopista;
  
CREATE OR REPLACE VIEW v_integ_eiel AS 
 (SELECT 'v_integ_depositos'::text as vista, 'eiel_c_abast_de'::text as tabla_c,* from v_integ_depositos) UNION
 (SELECT 'v_integ_centroasistencial'::text as vista, 'eiel_c_as'::text as tabla_c, *  from v_integ_centroasistencial) UNION
 (SELECT 'v_integ_casaconsistorial'::text as vista, 'eiel_c_cc'::text as tabla_c, *  from v_integ_casaconsistorial) UNION
 (SELECT 'v_integ_cementerio'::text as vista, 'eiel_c_ce'::text as tabla_c, *  from v_integ_cementerio) UNION
 (SELECT 'v_integ_centrocultural'::text as vista, 'eiel_c_cu'::text as tabla_c, *  from v_integ_centrocultural) UNION
 (SELECT 'v_integ_centroensenianza'::text as vista, 'eiel_c_en'::text as tabla_c, *  from v_integ_centroensenianza) UNION
 (SELECT 'v_integ_instalaciondeportiva'::text as vista, 'eiel_c_id'::text as tabla_c, *  from v_integ_instalaciondeportiva) UNION
 (SELECT 'v_integ_incendiosproteccion'::text as vista, 'eiel_c_ip'::text as tabla_c, *  from v_integ_incendiosproteccion) UNION
 (SELECT 'v_integ_lonjasmercados'::text as vista, 'eiel_c_lm'::text as tabla_c, *  from v_integ_lonjasmercados) UNION
 (SELECT 'v_integ_mataderos'::text as vista, 'eiel_c_ma'::text as tabla_c, *  from v_integ_mataderos) UNION
 (SELECT 'v_integ_parquesjardines'::text as vista, 'eiel_c_pj'::text as tabla_c, *  from v_integ_parquesjardines) UNION
 (SELECT 'v_integ_centrosanitario'::text as vista, 'eiel_c_sa'::text as tabla_c, *  from v_integ_centrosanitario) UNION
 (SELECT 'v_integ_edificiosinuso'::text as vista, 'eiel_c_su'::text as tabla_c, *  from v_integ_edificiosinuso) UNION
 (SELECT 'v_integ_tanatorio'::text as vista, 'eiel_c_ta'::text as tabla_c, *  from v_integ_tanatorio) UNION
 (SELECT 'v_integ_carreteras'::text as vista, 'eiel_c_tramos_carreteras'::text as tabla_c, *  from v_integ_carreteras) UNION
 (SELECT 'v_integ_depuradoras'::text as vista, 'eiel_c_saneam_ed'::text as tabla_c, *  from v_integ_depuradoras);

  ALTER TABLE v_integ_eiel OWNER TO geopista;
  
  
  
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM domains WHERE NAME = 'eiel_Titularidad Municipal') THEN
	
			insert into domains (ID, NAME,idacl,ID_CATEGORY) values(nextval('seq_domains'),'eiel_Titularidad Municipal',(select idacl FROM acl WHERE name='EIEL'),5);

			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Titularidad Municipal');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Titularidad Municipal');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Titularidad Municipal');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Titularidad Municipal');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Titularidad Municipal');
			
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
				values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
				
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','No');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','No');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','No');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','No');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','No');
			
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
						values(nextval('seq_domainnodes'),currval('seq_domains'),'NO',currval('seq_dictionary'),7,null,currval('seq_domainnodes'),null);

			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Si');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Si');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Si');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Si');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Si');
			
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
						values(nextval('seq_domainnodes'),currval('seq_domains'),'SI',currval('seq_dictionary'),7,null,currval('seq_domainnodes'),null);			
		 
		  
		 
		 insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Tipo de bien patrimonial EIEL',(select idacl FROM acl WHERE name='Patrimonio'),5);

			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Tipo de bien patrimonial EIEL');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Tipo de bien patrimonial EIEL');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Tipo de bien patrimonial EIEL');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Tipo de bien patrimonial EIEL');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Tipo de bien patrimonial EIEL');
			
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
				values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
			SET currval('seq_domainnodes')= currval('seq_domainnodes')			
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Inmuebles Rusticos');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Inmuebles Rusticos');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Inmuebles Rusticos');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Inmuebles Rusticos');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Inmuebles Rusticos');
			
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
						values(nextval('seq_domainnodes'),currval('seq_domains'),'3',currval('seq_dictionary'),7,null,currval('seq_domainnodes'),null);

			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Inmuebles Urbanos');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Inmuebles Urbanos');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Inmuebles Urbanos');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Inmuebles Urbanos');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Inmuebles Urbanos');
			
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
						values(nextval('seq_domainnodes'),currval('seq_domains'),'2',currval('seq_dictionary'),7,null,currval('seq_domainnodes'),null);	
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Vías Públicas Rústicas');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Vías Públicas Rústicas');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Vías Públicas Rústicas');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Vías Públicas Rústicas');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Vías Públicas Rústicas');
			
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
						values(nextval('seq_domainnodes'),currval('seq_domains'),'5',currval('seq_dictionary'),7,null,currval('seq_domainnodes'),null);

			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Vías Públicas Urbanas');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Vías Públicas Urbanas');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Vías Públicas Urbanas');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Vías Públicas Urbanas');
			insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Vías Públicas Urbanas');
			
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
						values(nextval('seq_domainnodes'),currval('seq_domains'),'4',currval('seq_dictionary'),7,null,currval('seq_domainnodes'),null);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";
  			  				
 