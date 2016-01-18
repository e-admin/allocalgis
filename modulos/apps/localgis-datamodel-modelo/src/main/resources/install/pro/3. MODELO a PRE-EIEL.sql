SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;

SET client_min_messages TO notice;

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


-- Inicio calculateexpresion.sql

DROP TABLE IF EXISTS "public".eiel_calculateexpresion_tables;
CREATE TABLE "public".eiel_calculateexpresion_tables
(
  id numeric(8,0) NOT NULL,
  name character varying(100) NOT NULL,
  CONSTRAINT pk_calculateexpresion_tables PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_calculateexpresion_tables OWNER TO postgres;

DROP SEQUENCE IF EXISTS "public"."seq_eiel_calculateexpresion_tables";
CREATE SEQUENCE "public"."seq_eiel_calculateexpresion_tables"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;



INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_abast_ar');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_abast_ca');
--INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_abast_cb');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_abast_de');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_abast_rd');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_abast_tcn');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_abast_tp');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_alum_cmp');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_alum_eea');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_alum_pl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_as');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_cc');
--INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_ce');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_comarcas');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_cu');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_edificiosing');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_en');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_id');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_ip');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_lm');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_mt');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_municipios');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_municipios_puntos');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_nucleo_poblacion');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_nucleos_puntos');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_parcelas');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_parroquias');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_pj');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_provincia');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_redviaria_tu');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_sa');
--INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_saneam_ali');
--INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_saneam_cb');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_saneam_ed');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_saneam_pr');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_saneam_pv');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_saneam_rs');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_saneam_tcl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_saneam_tem');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_su');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_ta');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_tramos_carreteras');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_c_vt');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t1_saneam_ed');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t2_saneam_ed');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_abast_au');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_abast_ca');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_abast_de');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_abast_serv');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_abast_tcn');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_abast_tp');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_as');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_cabildo_consejo');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_carreteras');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_cc');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_cc_usos');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_ce');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_cu');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_cu_usos');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_en');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_en_nivel');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_entidad_singular');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_entidades_agrupadas');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_id');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_id_deportes');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_inf_ttmm');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_ip');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_lm');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_mt');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_mun_diseminados');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_nucl_encuest_1');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_nucl_encuest_2');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_nucleo_aband ');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_otros_serv_munic');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_padron_nd');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_padron_ttmm');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_pj');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_planeam_urban');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_poblamiento');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_rb');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_rb_serv');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_sa');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_saneam_au');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_saneam_pv');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_saneam_serv');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_saneam_serv');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_saneam_tem');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_su');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_ta');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_t_vt');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_abast_ca_pobl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_abast_de_pobl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_abast_tcn_pobl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_abast_tp_pobl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_saneam_ed_pobl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_saneam_pv_pobl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_saneam_tcl_pobl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_saneam_tem_pobl');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_saneam_tem_pv');
INSERT INTO "public".eiel_calculateexpresion_tables (id,name) VALUES (nextval('seq_eiel_calculateexpresion_tables'),'eiel_tr_vt_pobl');

-- Fin calculateexpresion.sql

-- Inicio entidad_supramunicipal.sql

select f_add_col('public.entidad_supramunicipal','GEOMETRY', 'ALTER TABLE entidad_supramunicipal ADD COLUMN "GEOMETRY" geometry;');
select f_add_col('public.entidad_supramunicipal','municipiodefault', 'ALTER TABLE entidad_supramunicipal ADD COLUMN "municipiodefault" numeric (5,0);');


UPDATE query_catalog set query = 'select nombreoficial, srid from entidad_supramunicipal where id_entidad=?' where id = 'getEntidad';
UPDATE query_catalog set query = 'select entidad_supramunicipal.id_entidad, entidad_supramunicipal.nombreoficial, entidad_supramunicipal.srid,  entidad_supramunicipal.backup, entidad_supramunicipal.aviso, entidad_supramunicipal.periodicidad, entidad_supramunicipal.intentos, entidades_municipios.*,municipios.nombreoficial as nombremunicipio from entidad_supramunicipal,entidades_municipios,municipios where entidad_supramunicipal.id_entidad=entidades_municipios.id_entidad and entidades_municipios.id_municipio=municipios.id order by entidad_supramunicipal.nombreoficial' where id = 'getEntidadesFullSortedByIdEntidad';
UPDATE query_catalog set query = 'select id_entidad, nombreoficial, srid,  backup, aviso, periodicidad, intentos from entidad_supramunicipal order by id_entidad' where id = 'getEntidadesSortedById';
UPDATE query_catalog set query = '"select entidad_supramunicipal.id_entidad, entidad_supramunicipal.nombreoficial, entidad_supramunicipal.srid,  entidad_supramunicipal.backup, entidad_supramunicipal.aviso, entidad_supramunicipal.periodicidad, entidad_supramunicipal.intentos, entidades_municipios.*,municipios.nombreoficial as nombremunicipio from entidad_supramunicipal,entidades_municipios,municipios where entidad_supramunicipal.id_entidad=entidades_municipios.id_entidad and entidades_municipios.id_municipio=municipios.id order by entidad_supramunicipal.nombreoficial' where id = 'getEntidadesSortedByIdEntidad';
UPDATE query_catalog set query = 'SELECT id_entidad, nombreoficial, srid, backup, aviso, periodicidad, intentos FROM entidad_supramunicipal order by nombreoficial' where id = 'getEntidadesSortedByName';

-- Fin entidad_supramunicipal.sql


-- Inicio indexacion.sql

DROP INDEX IF EXISTS localgisguiaurbana.map_mapid_idx;
CREATE INDEX map_mapid_idx ON localgisguiaurbana.map (mapid);

-- Fin indexacion.sql

-- Inicio publicador_guiaurbana.sql

-- La tabla attributes repite atributos para cada capa, si se publica un mapa
-- para todos los municipios los atributos se repinte.
select f_add_col('localgisguiaurbana.attribute','mapid', 'ALTER TABLE localgisguiaurbana.attribute ADD COLUMN mapid integer;');

-- Fin publicador_guiaurbana.sql

-- Inicio Query_catalog_eiel.sql
delete from query_catalog where id='EIELobtenerEntidades';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerEntidades','select codentidad, codprov, codmunic from eiel_t_entidad_singular where codprov=? and codmunic=?',1,9205);

delete from query_catalog where id='EIELgetPanelDiseminados';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelDiseminados','select * from eiel_t_mun_diseminados where codprov=? and codmunic=?',1,9205);

delete from query_catalog where id='EIELgetNucleosCaptacion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosCaptacion','select * from eiel_tr_abast_ca_pobl where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetListaNucleosCaptacion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosCaptacion','select * from eiel_tr_abast_ca_pobl where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=?',1,9205);

delete from query_catalog where id='EIELgetPanelPadronNucleos';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPadronNucleos','select * from eiel_t_padron_nd where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetCaptacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetCaptacion','select clave, codprov,codmunic,orden_ca from eiel_c_abast_ca where id=?',1,9205);

delete from query_catalog where id='EIELupdateNucleosCaptacion'; 
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosCaptacion','update eiel_tr_abast_ca_pobl set clave_ca=?, codprov_ca=?, codmunic_ca=?, orden_ca=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_inicio_serv=?, fecha_revision=?, estado_revision=? where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetPanelPadronMunicipios';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPadronMunicipios','select * from eiel_t_padron_ttmm where codprov=? and codmunic=?',1,9205);

delete from query_catalog where id='EIELinsertNucleosCaptacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosCaptacion','insert into eiel_tr_abast_ca_pobl (clave_ca, codprov_ca, codmunic_ca, orden_ca, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_inicio_serv, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

delete from query_catalog where id='EIELdeleteNucleosCaptacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosCaptacion','delete from eiel_tr_abast_ca_pobl where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetColector';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetColector','select clave,codprov,codmunic,tramo_cl as orden from eiel_c_saneam_tcl where id=?',1,9205);

delete from query_catalog where id='EIELgetDepositos';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetDepositos','select clave, codprov,codmunic,orden_de from eiel_c_abast_de where id=?',1,9205);

delete from query_catalog where id='EIELgetNucleosDepositos';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosDepositos','select * from eiel_tr_abast_de_pobl where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

  
delete from query_catalog where id='EIELinsertNucleosDepositos';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosDepositos','insert into eiel_tr_abast_de_pobl (clave_de, codprov_de, codmunic_de, orden_de, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?)',1,9205);

delete from query_catalog where id='EIELupdateNucleosDepositos';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosDepositos','update eiel_tr_abast_de_pobl set clave_de=?, codprov_de=?, codmunic_de=?, orden_de=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_revision=?, estado_revision=? where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetListaNucleosDepositos';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosDepositos','select * from eiel_tr_abast_de_pobl where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=?',1,9205);

delete from query_catalog where id='EIELdeleteNucleosDepositos';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosDepositos','delete from eiel_tr_abast_de_pobl where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetTratamientosPotabilizacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetTratamientosPotabilizacion','select clave, codprov,codmunic,orden_tp from eiel_c_abast_tp where id=?',1,9205);

delete from query_catalog where id='EIELgetEmisor';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetEmisor','select clave,codprov,codmunic,tramo_em as orden from eiel_c_saneam_tem where id=?',1,9205);

delete from query_catalog where id='EIELgetNucleosTratamientosPotabilizacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosTratamientosPotabilizacion','select * from eiel_tr_abast_tp_pobl where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELinsertNucleosTratamientosPotabilizacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosTratamientosPotabilizacion','insert into eiel_tr_abast_tp_pobl (clave_tp, codprov_tp, codmunic_tp, orden_tp, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?)',1,9205);

delete from query_catalog where id='EIELgetListaNucleosColector';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosColector','select * from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=?',1,9205);

delete from query_catalog where id='EIELupdateNucleosTratamientosPotabilizacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosTratamientosPotabilizacion','update eiel_tr_abast_tp_pobl set clave_tp=?, codprov_tp=?, codmunic_tp=?, orden_tp=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_revision=?, estado_revision=? where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetListaNucleosTratamientosPotabilizacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosTratamientosPotabilizacion','select * from eiel_tr_abast_tp_pobl where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=?',1,9205);

delete from query_catalog where id='EIELdeleteNucleosTratamientosPotabilizacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosTratamientosPotabilizacion','delete from eiel_tr_abast_tp_pobl where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetPuntosVertido';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPuntosVertido','select clave, codprov,codmunic,orden_pv from eiel_c_saneam_pv where id=?',1,9205);

delete from query_catalog where id='EIELgetNucleosPuntosVertido';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosPuntosVertido','select * from eiel_tr_saneam_pv_pobl where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELinsertNucleosPuntosVertido';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosPuntosVertido','insert into eiel_tr_saneam_pv_pobl (clave_pv, codprov_pv, codmunic_pv, orden_pv, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?)',1,9205);

delete from query_catalog where id='EIELupdateNucleosPuntosVertido';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosPuntosVertido','update eiel_tr_saneam_pv_pobl set clave_pv=?, codprov_pv=?, codmunic_pv=?, orden_pv=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_revision=?, estado_revision=? where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetListaNucleosPuntosVertido';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosPuntosVertido','select * from eiel_tr_saneam_pv_pobl where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=?',1,9205);

delete from query_catalog where id='EIELdeleteNucleosPuntosVertido';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosPuntosVertido','delete from eiel_tr_saneam_pv_pobl where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetDepuradora1';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetDepuradora1','select clave, codprov,codmunic,orden_ed from eiel_c_saneam_ed where id=?',1,9205);

delete from query_catalog where id='EIELgetNucleosDepuradora1';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosDepuradora1','select * from eiel_tr_saneam_ed_pobl where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELinsertNucleosDepuradora1';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosDepuradora1','insert into eiel_tr_saneam_ed_pobl (clave_ed, codprov_ed, codmunic_ed, orden_ed, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?)',1,9205);

delete from query_catalog where id='EIELupdateNucleosDepuradora1';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosDepuradora1','update eiel_tr_saneam_ed_pobl set clave_ed=?, codprov_ed=?, codmunic_ed=?, orden_ed=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_revision=?, estado_revision=? where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetListaNucleosDepuradora1';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosDepuradora1','select * from eiel_tr_saneam_ed_pobl where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=?',1,9205);

delete from query_catalog where id='EIELdeleteNucleosDepuradora1';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosDepuradora1','delete from eiel_tr_saneam_ed_pobl where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetVertedero';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetVertedero','select clave, codprov,codmunic,orden_vt from eiel_c_vt where id=?',1,9205);

delete from query_catalog where id='EIELgetNucleosVertedero';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosVertedero','select * from eiel_tr_vt_pobl where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetListaNucleosVertedero';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosVertedero','select * from eiel_tr_vt_pobl where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=?',1,9205);

delete from query_catalog where id='EIELdeleteNucleosVertedero';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosVertedero','delete from eiel_tr_vt_pobl where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelDeposito';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelDeposito','select * from eiel_t_abast_de where clave=? and codprov=? and codmunic=? and orden_de=?',1,9205);

delete from query_catalog where id='EIELgetPanelCentrosEnsenianza';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCentrosEnsenianza','select * from eiel_t_en where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_en=?',1,9205);

delete from query_catalog where id='EIELgetPanelCentrosSanitarios';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCentrosSanitarios','select * from eiel_t_sa where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_sa=?',1,9205);

delete from query_catalog where id='EIELgetPanelEdificiosSinUso';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelEdificiosSinUso','select * from eiel_t_su where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_su=?',1,9205);

delete from query_catalog where id='EIELgetPanelInfoTerminosMunicipales';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelInfoTerminosMunicipales','select * from eiel_t_inf_ttmm where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelInstalacionesDeportivas';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelInstalacionesDeportivas','select * from eiel_t_id where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_id=?',1,9205);

delete from query_catalog where id='EIELgetPanelMataderos';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelMataderos','select * from eiel_t_mt where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_mt=?',1,9205);

delete from query_catalog where id='EIELgetPanelPlaneamientoUrbano';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPlaneamientoUrbano','select * from eiel_t_planeam_urban where codprov=? and codmunic=?',1,9205);

delete from query_catalog where id='EIELgetPanelPoblamiento';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPoblamiento','select * from eiel_t_poblamiento where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelPuntosVertido';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPuntosVertido','select * from eiel_t_saneam_pv where clave=? and codprov=? and codmunic=? and orden_pv=?',1,9205);

delete from query_catalog where id='EIELgetPanelTanatorios';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelTanatorios','select * from eiel_t_ta where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_ta=?',1,9205);

delete from query_catalog where id='EIELgetPanelVertederos';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelVertederos','select * from eiel_t_vt where clave=? and codprov=? and codmunic=? and orden_vt=?',1,9205);

delete from query_catalog where id='EIELgetPanelTratamientosPotabilizacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelTratamientosPotabilizacion','select * from eiel_t_abast_tp where clave=? and codprov=? and codmunic=? and orden_tp=?',1,9205);

delete from query_catalog where id='EIELgetPanelCabildoConsejo';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCabildoConsejo','select * from eiel_t_cabildo_consejo where codprov=? and cod_isla=?',1,9205);

delete from query_catalog where id='EIELgetPanelCarreteras';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCarreteras','select * from eiel_t_cabildo_consejo where codprov=? and cod_carrt=?',1,9205);

delete from query_catalog where id='EIELgetPanelCasasConsistoriales';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCasasConsistoriales','select * from eiel_t_cc where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_cc=?',1,9205);

delete from query_catalog where id='EIELgetPanelCementerios';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCementerios','select * from eiel_t_ce where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_ce=?',1,9205);

delete from query_catalog where id='EIELgetPanelCentrosCulturales';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCentrosCulturales','select * from eiel_t_cu where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_cu=?',1,9205);

delete from query_catalog where id='EIELgetPanelCentrosAsistenciales';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCentrosAsistenciales','select * from eiel_t_as where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_as=?',1,9205);

delete from query_catalog where id='EIELgetCentrosIncendios';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetCentrosIncendios','select * from eiel_t_ip where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_ip=?',1,9205);

delete from query_catalog where id='EIELgetPanelCaptacion';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCaptacion','select * from eiel_t_abast_ca where clave=? and codprov=? and codmunic=? and orden_ca=?',1,9205);

delete from query_catalog where id='EIELgetPanelLonjasMercados';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelLonjasMercados','select * from eiel_t_lm where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_lm=?',1,9205);

delete from query_catalog where id='EIELgetPanelParquesJardines';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelParquesJardines','select * from eiel_t_pj where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_pj=?',1,9205);

delete from query_catalog where id='EIELgetPanelNucleosEncuestados1';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNucleosEncuestados1','select * from eiel_t_nucl_encuest_1 where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelNucleosEncuestados2';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNucleosEncuestados2','select * from eiel_t_nucl_encuest_2 where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelNucleosPoblacion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNucleosPoblacion','select * from eiel_t_nucleos_poblacion where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelServiciosSaneamiento';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelServiciosSaneamiento','select * from eiel_t_saneam_serv where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelServiciosAbastecimiento';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelServiciosAbastecimiento','select * from eiel_t_abast_serv where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelOtrosServiciosMunicipales';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelOtrosServiciosMunicipales','select * from eiel_t_otros_serv_munic where codprov=? and codmunic=?',1,9205);

delete from query_catalog where id='EIELgetPanelAbastecimientoAutonomo';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelAbastecimientoAutonomo','select * from eiel_t_abast_au where codprov=? and codmunic=?  and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelEntidadesSingulares';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelEntidadesSingulares','select * from eiel_t_entidad_singular where codprov=? and codmunic=?',1,9205);

delete from query_catalog where id='EIELgetPanelTipoInstalacionesDeportivas';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelTipoInstalacionesDeportivas','select * from eiel_t_id_deportes where eiel_t_id_deportes.clave=? and eiel_t_id_deportes.codprov=? and eiel_t_id_deportes.codmunic=? and eiel_t_id_deportes.codentidad=? and eiel_t_id_deportes.codpoblamiento=? and eiel_t_id_deportes.orden_id=?',1,9205);

delete from query_catalog where id='EIELgetPanelUsosCentrosCulturales';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelUsosCentrosCulturales','select * from eiel_t_cu_usos where eiel_t_cu_usos.clave=? and eiel_t_cu_usos.codprov=? and eiel_t_cu_usos.codmunic=? and eiel_t_cu_usos.codentidad=? and eiel_t_cu_usos.codpoblamiento=? and eiel_t_cu_usos.orden_cu=?',1,9205);

delete from query_catalog where id='EIELgetPanelUsosCasasConsistoriales';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelUsosCasasConsistoriales','select * from eiel_t_cc_usos where eiel_t_cc_usos.clave=? and eiel_t_cc_usos.codprov=? and eiel_t_cc_usos.codmunic=? and eiel_t_cc_usos.codentidad=? and eiel_t_cc_usos.codpoblamiento=? and eiel_t_cc_usos.orden_cc=?',1,9205);

delete from query_catalog where id='EIELgetPanelNivelCentrosEnsenianza';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNivelCentrosEnsenianza','select * from eiel_t_en_nivel where eiel_t_en_nivel.clase=? and eiel_t_en_nivel.codprov=? and eiel_t_en_nivel.codmunic=? and eiel_t_en_nivel.codentidad=? and eiel_t_en_nivel.codpoblamiento=? and eiel_t_en_nivel.orden_en=?',1,9205);

delete from query_catalog where id='EIELgetPanelServiciosRecogidaBasura';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelServiciosRecogidaBasura','select * from eiel_t_rb_serv where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelRecogidaBasuras';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelRecogidaBasuras','select * from eiel_t_rb where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelSaneamientoAutonomo';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelSaneamientoAutonomo','select * from eiel_t_saneam_au where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELgetPanelNucleosAbandonados';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNucleosAbandonados','select * from eiel_t_nucleo_aband where codprov=? and codmunic=?',1,9205);

delete from query_catalog where id='EIELgetPanelDepuradora1';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelDepuradora1','select * from eiel_t1_saneam_ed where clave=? and codprov=? and codmunic=? and orden_ed=?',1,9205);

delete from query_catalog where id='EIELgetPanelDepuradora2';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelDepuradora2','select * from eiel_t2_saneam_ed where clave=? and codprov=? and codmunic=? and orden_ed=?',1,9205);

delete from query_catalog where id='EIELgetTramosConduccion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetTramosConduccion','select clave, codprov,codmunic,tramo_cn from eiel_c_abast_tcn where id=?',1,9205);

delete from query_catalog where id='EIELgetNucleosTramosConduccion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosTramosConduccion','select * from eiel_tr_abast_tcn_pobl where clave_tcn=? and codprov_tcn=? and codmunic_tcn=? and tramo_tcn=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELinsertNucleosTramosConduccion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosTramosConduccion','insert into eiel_tr_abast_tcn_pobl (clave_tcn,codprov_tcn, codmunic_tcn, tramo_tcn, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, pmi, pmf, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

delete from query_catalog where id='EIELgetListaNucleosTramosConduccion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosTramosConduccion','select * from eiel_tr_abast_tcn_pobl where clave_tcn=? AND codprov_tcn=? and codmunic_tcn=? and tramo_tcn=?',1,9205);

delete from query_catalog where id='EIELdeleteNucleosTramosConduccion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosTramosConduccion','delete from eiel_tr_abast_tcn_pobl where clave_tcn=? and codprov_tcn=? and codmunic_tcn=? and tramo_tcn=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELobtenerEntidades2';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerEntidades2','select codentidad from eiel_c_nucleos_puntos where codprov=? and codmunic=? group by codentidad order by codentidad',1,9205);

delete from query_catalog where id='MCEIELobtenerEntidades';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('MCEIELobtenerEntidades','select codentidad, denominacion from eiel_t_entidad_singular where codprov =? AND codmunic=? group by codentidad,denominacion order by codentidad',1,9205);

delete from query_catalog where id='EIELobtenerNucleos3';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerNucleos3','select codpoblamiento,nombre_oficial from eiel_c_nucleos_puntos where codprov=?',1,9205);

delete from query_catalog where id='EIELobtenerNucleos3';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerNucleos3','select codpoblamiento,nombre_oficial from eiel_c_nucleos_puntos where codprov=? and codmunic=?',1,9205);

delete from query_catalog where id='EIELobtenerNucleos';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerNucleos','select codpoblamiento,nombre_oficial from eiel_c_nucleos_puntos where codprov=? and codmunic=? and codentidad=?',1,9205);

delete from query_catalog where id='EIELgetNucleosColector';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosColector','select * from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELinsertNucleosColector';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosColector','insert into eiel_tr_saneam_tcl_pobl (clave_tcl, codprov_tcl, codmunic_tcl, tramo_cl, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, pmi, pmf, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

delete from query_catalog where id='EIELupdateNucleosColector';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosColector','update eiel_tr_saneam_tcl_pobl set clave_tcl=?, codprov_tcl=?, codmunic_tcl=?, tramo_cl=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, pmi=?, pmf=?, observ=?, fecha_revision=?, estado_revision=? where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELdeleteNucleosColector';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosColector','delete from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELupdateNucleosTramosConduccion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosTramosConduccion','update eiel_tr_abast_tcn_pobl set codprov_tcn=?, codmunic_tcn=?, tramo_tcn=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, pmi=?, pmf=?, fecha_revision=?, estado_revision=? where codprov_tcn=? and codmunic_tcn=? and tramo_cn=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELinsertNucleosVertedero';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosVertedero','insert into eiel_tr_vt_pobl (clave_vt, codprov_vt, codmunic_vt, orden_vt, codprov, codmunic, codentidad, codpoblamiento, observ, fecha_alta, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

delete from query_catalog where id='EIELupdateNucleosVertedero';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosVertedero','update eiel_tr_vt_pobl set clave_vt=?, codprov_vt=?, codmunic_vt=?, orden_vt=?, codprov=?, codmunic=?, codentidad=?, codpoblamiento=?, observ=?, fecha_alta=?, fecha_revision=?, estado_revision=? where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

delete from query_catalog where id='EIELobtenerOrdenPuntosVertido';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerOrdenPuntosVertido','select orden_pv from eiel_t_saneam_pv where clave=? and codprov=? and codmunic=?',1,9205);

delete from query_catalog where id='EIELgetPuntoVertidoEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPuntoVertidoEmisario','select *  from eiel_tr_saneam_tem_pv where clave_tem=? and codprov_tem=? and codmunic_tem=? and  tramo_em=? and codprov_pv=? and codmunic_pv=? and clave_pv=?',1,9205);

delete from query_catalog where id='EIELupdatePuntoVertidoEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdatePuntoVertidoEmisario','update eiel_tr_saneam_tem_pv set clave_tem=?, codprov_tem=?, codmunic_tem=?, tramo_em=?, codprov_pv=?, codmunic_pv=?, clave_pv=?, pmi=?, pmf=?, observ=?, fecha_revision=?, estado_revision=? where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pv=? and codmunic_pv=? and clave_pv=?',1,9205);

delete from query_catalog where id='EIELgetListaPuntoVertidoEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaPuntoVertidoEmisario','select * from eiel_tr_saneam_tem_pv where clave_tem=? and codprov_tem=? and codmunic_tem=? and  tramo_em=?',1,9205);

delete from query_catalog where id='EIELinsertPuntoVertidoEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertPuntoVertidoEmisario','insert into eiel_tr_saneam_tem_pv (clave_tem, codprov_tem, codmunic_tem, tramo_em, codprov_pv, codmunic_pv, clave_pv,orden_pv, pmi, pmf, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

delete from query_catalog where id='EIELdeletePuntoVertidoEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeletePuntoVertidoEmisario','delete from eiel_tr_saneam_tem_pv where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pv=? and codmunic_pv=? and clave_pv=? and orden_pv=?',1,9205);

  
delete from query_catalog where id='EIELgetNucleosEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosEmisario','select *  from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and  tramo_em=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELupdateNucleosEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosEmisario','update eiel_tr_saneam_tem_pobl set clave_tem=?, codprov_tem=?, codmunic_tem=?, tramo_em=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, pmi=?, pmf=?, observ=?, fecha_revision=?, estado_revision=? where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?',1,9205);

delete from query_catalog where id='EIELgetListaNucleosEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosEmisario','select * from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=?',1,9205);

delete from query_catalog where id='EIELinsertNucleosEmisario';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosEmisario','insert into eiel_tr_saneam_tem_pobl (clave_tem, codprov_tem, codmunic_tem, tramo_em, codprov_pv, codmunic_pv, clave_pv, pmi, pmf, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?, ?,?,?)',1,9205);

delete from query_catalog where id='EIELdeleteNucleosEmisario';  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosEmisario','delete from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);


-- Fin Query_catalog_eiel.sql

-- Inicio reports.sql
DROP TABLE IF EXISTS "public".eiel_reports;
CREATE TABLE "public".eiel_reports
(
  id numeric(8,0) NOT NULL,
  namejrxml character varying(5) NOT NULL,
  nombre character varying(100) NOT NULL,
  tabla character varying(100) NOT NULL,
  tablasec character varying(100) NOT NULL,
  CONSTRAINT pk_reports PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_reports OWNER TO postgres;

DROP SEQUENCE IF EXISTS "public"."seq_eiel_reports";
CREATE SEQUENCE "public"."seq_eiel_reports"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

    
-- Inserción de datos
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'NUCL_ENCUESTADO_1','v_NUCL_ENCUESTADO_1' ,'v_NUCL_ENCUESTADO_1');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'PLAN_URBANISTICO','v_PLAN_URBANISTICO','v_PLAN_URBANISTICO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'OT_SERV_MUNICIPAL','v_OT_SERV_MUNICIPAL','v_OT_SERV_MUNICIPAL');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'TRAMO_CARRETERA','v_TRAMO_CARRETERA','v_TRAMO_CARRETERA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'INFRAESTR_VIARIA','v_INFRAESTR_VIARIA','v_INFRAESTR_VIARIA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CAP_AGUA_NUCLEO','v_CAP_AGUA_NUCLEO','v_CAP_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CAPTACION_ENC','v_CAPTACION_ENC','v_CAP_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CAPTACION_ENC_M50','v_CAPTACION_ENC_M50','v_CAP_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'COND_AGUA_NUCLEO','v_COND_AGUA_NUCLEO','v_COND_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CONDUCCION_ENC','v_CONDUCCION_ENC','v_COND_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CONDUCCION_ENC_M50','v_CONDUCCION_ENC_M50','v_COND_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'TRAMO_CONDUCCION','v_TRAMO_CONDUCCION','v_COND_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'TRAMO_CONDUCCION_M50','v_TRAMO_CONDUCCION_M50','v_COND_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'DEPOSITO_AGUA_NUCLEO','v_DEPOSITO_AGUA_NUCLEO','v_DEPOSITO_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'DEPOSITO_ENC','v_DEPOSITO_ENC','v_DEPOSITO_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'DEPOSITO_ENC_M50','v_DEPOSITO_ENC_M50','v_DEPOSITO_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'TRAT_POTA_NUCLEO','v_TRAT_POTA_NUCLEO','v_TRAT_POTA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'POTABILIZACION_ENC','v_POTABILIZACION_ENC','v_TRAT_POTA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'POTABILIZACION_ENC_M50','v_POTABILIZACION_ENC_M50','v_TRAT_POTA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'RED_DISTRIBUCION','v_RED_DISTRIBUCION','v_RED_DISTRIBUCION');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'NUCL_ENCUESTADO_2','v_NUCL_ENCUESTADO_2','v_NUCL_ENCUESTADO_2');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'NUCL_ENCUESTADO_3','v_NUCL_ENCUESTADO_3','v_NUCL_ENCUESTADO_3');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'NUCL_ENCUESTADO_4','v_NUCL_ENCUESTADO_4','v_NUCL_ENCUESTADO_4');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'RAMAL_SANEAMIENTO','v_RAMAL_SANEAMIENTO','v_RAMAL_SANEAMIENTO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'COLECTOR_NUCLEO','v_COLECTOR_NUCLEO','v_COLECTOR_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'COLECTOR_ENC','v_COLECTOR_ENC','v_COLECTOR_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'COLECTOR_ENC_M50','v_COLECTOR_ENC_M50','v_COLECTOR_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'TRAMO_COLECTOR','v_TRAMO_COLECTOR','v_COLECTOR_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'TRAMO_COLECTOR_M50','v_TRAMO_COLECTOR_M50','v_COLECTOR_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'EMISARIO_NUCLEO','v_EMISARIO_NUCLEO','v_EMISARIO_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'EMISARIO_ENC','v_EMISARIO_ENC','v_EMISARIO_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'EMISARIO_ENC_M50','v_EMISARIO_ENC_M50','v_EMISARIO_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'TRAMO_EMISARIO','v_TRAMO_EMISARIO','v_EMISARIO_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'TRAMO_EMISARIO_M50','v_TRAMO_EMISARIO_M50','v_EMISARIO_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'NUCL_ENCUESTADO_5','v_NUCL_ENCUESTADO_5','v_NUCL_ENCUESTADO_5');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'DEP_AGUA_NUCLEO','v_DEP_AGUA_NUCLEO','v_DEP_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'DEPURADORA_ENC','v_DEPURADORA_ENC','v_DEP_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'DEPURADORA_ENC_M50','v_DEPURADORA_ENC_M50','v_DEP_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'DEPURADORA_ENC_2','v_DEPURADORA_ENC_2','v_DEP_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'DEPURADORA_ENC_2_M50','v_DEPURADORA_ENC_2_M50','v_DEP_AGUA_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'SANEA_AUTONOMO','v_SANEA_AUTONOMO','v_SANEA_AUTONOMO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'RECOGIDA_BASURA','v_RECOGIDA_BASURA','v_RECOGIDA_BASURA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'NUCL_ENCUESTADO_6','v_NUCL_ENCUESTADO_6','v_NUCL_ENCUESTADO_6');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'VERTEDERO_NUCLEO','v_VERTEDERO_NUCLEO','v_VERTEDERO_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'VERT_ENCUESTADO','v_VERT_ENCUESTADO','v_VERTEDERO_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'VERT_ENCUESTADO_M50','v_VERT_ENCUESTADO_M50','v_VERTEDERO_NUCLEO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'ALUMBRADO','v_ALUMBRADO','v_ALUMBRADO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'NUCL_ENCUESTADO_7','v_NUCL_ENCUESTADO_7','v_NUCL_ENCUESTADO_7');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'INSTAL_DEPORTIVA','v_INSTAL_DEPORTIVA','v_INSTAL_DEPORTIVA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'INST_DEPOR_DEPORTE','v_INST_DEPOR_DEPORTE','v_INST_DEPOR_DEPORTE');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CENT_CULTURAL','v_CENT_CULTURAL','v_CENT_CULTURAL');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CENT_CULTURAL_USOS','v_CENT_CULTURAL_USOS','v_CENT_CULTURAL_USOS');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'PARQUE','v_PARQUE','v_PARQUE');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'LONJA_MERC_FERIA','v_LONJA_MERC_FERIA','v_LONJA_MERC_FERIA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'MATADERO','v_MATADERO','v_MATADERO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CEMENTERIO','v_CEMENTERIO','v_CEMENTERIO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'TANATORIO','v_TANATORIO','v_TANATORIO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CENTRO_SANITARIO','v_CENTRO_SANITARIO','v_CENTRO_SANITARIO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CENTRO_ASISTENCIAL','v_CENTRO_ASISTENCIAL','v_CENTRO_ASISTENCIAL');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CENTRO_ENSENANZA','v_CENTRO_ENSENANZA','v_CENTRO_ENSENANZA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'NIVEL_ENSENANZA','v_NIVEL_ENSENANZA','v_NIVEL_ENSENANZA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'PROTECCION_CIVIL','v_PROTECCION_CIVIL','v_PROTECCION_CIVIL');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CASA_CONSISTORIAL','v_CASA_CONSISTORIAL','v_CASA_CONSISTORIAL');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'CASA_CON_USO','v_CASA_CON_USO','v_CASA_CON_USO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'EDIFIC_PUB_SIN_USO','v_EDIFIC_PUB_SIN_USO','v_EDIFIC_PUB_SIN_USO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),currval('seq_eiel_reports'),'NUC_ABANDONADO','v_NUC_ABANDONADO','v_NUC_ABANDONADO');

    
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'A','A','v_PROVINCIA','v_PROVINCIA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'C','C','v_MUNICIPIO','v_MUNICIPIO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'D','D','v_POBLAMIENTO','v_POBLAMIENTO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'E','E','v_ENTIDAD_SINGULAR','v_ENTIDAD_SINGULAR');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'F','F','v_NUCLEO_POBLACION','v_NUCLEO_POBLACION');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'G','G','v_CARRETERA','v_CARRETERA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'H','H','v_CAPTACION_AGUA','v_CAPTACION_AGUA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'I','I','v_CONDUCCION','v_CONDUCCION');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'J','J','v_DEPOSITO','v_DEPOSITO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'K','K','v_TRA_POTABILIZACION','v_TRA_POTABILIZACION');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'L','L','v_COLECTOR','v_COLECTOR');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'M','M','v_EMISARIO','v_EMISARIO');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'N','N','v_DEPURADORA','v_DEPURADORA');
INSERT INTO "public".eiel_reports(id,namejrxml,nombre,tabla,tablasec) VALUES (nextval('seq_eiel_reports'),'O','O','v_VERTEDERO','v_VERTEDERO');

-- Fin reports.sql

-- Inicio validacionesMPT.sql

DROP TABLE IF EXISTS "public".eiel_validacionesporcuadompt;
DROP TABLE IF EXISTS "public".eiel_validacionesmpt;
CREATE TABLE "public".eiel_validacionesmpt
(
  id numeric(8,0) NOT NULL,
  nombre character varying(100) NOT NULL,
  tabla character varying(100) NOT NULL,
  CONSTRAINT pk_validacionesmpt PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_validacionesmpt OWNER TO postgres;


DROP TABLE IF EXISTS "public".eiel_validacionesporcuadompt;
CREATE TABLE "public".eiel_validacionesporcuadompt
(
  id numeric(8,0) NOT NULL,
  nombre character varying(100) NOT NULL,
  id_validacionesmpt numeric(8,0) NOT NULL,
  CONSTRAINT pk_eiel_validacionesporcuadompt PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_validacionesporcuadompt OWNER TO postgres;
ALTER TABLE ONLY "public".eiel_validacionesporcuadompt
    ADD CONSTRAINT eiel_validacionesporcuadompt_fk1 FOREIGN KEY (id_validacionesmpt)  REFERENCES "public".eiel_validacionesmpt(id) ;

DROP SEQUENCE IF EXISTS "public"."seq_eiel_validacionesporcuadompt";
CREATE SEQUENCE "public"."seq_eiel_validacionesporcuadompt"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;





INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (0,'Comprobación datos numéricos','');

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (1,'A','eiel_c_provincia');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',1);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (2,'C','eiel_c_municipios');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',2);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (3,'G','eiel_t_carreteras');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',3);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (4,'H','eiel_t_abast_ca');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',4);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (5,'I','eiel_t_abast_tcn');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',5);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (6,'J','eiel_t_abast_de');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',6);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (7,'K','eiel_t_abast_tp');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',7);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (8,'L','eiel_t_saneam_tcl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',8);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (9,'M','eiel_t_saneam_tem');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',9);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (10,'N','eiel_t1_saneam_ed');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',10);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (11,'O','eiel_t_vt');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',11);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (12,'(CUADRO 1) - NUCL_ENCUESTADO_1','eiel_t_nucl_encuest_1');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v08',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v09',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v10',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v11',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v12',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v13',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v14',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v15',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v16',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v17',12);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v18',12);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (13,'(CUADRO 2) - PLAN_URBANISTICO','eiel_t_planeam_urban');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',13);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',13);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',13);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',13);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',13);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',13);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',13);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v08',13);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v09',13);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (14,'(CUADRO 3) - OT_SERV_MUNICIPAL','eiel_t_otros_serv_munic');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',14);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (15,'(CUADRO 4) - TRAMO_CARRETERA','eiel_c_tramos_carreteras');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',15);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',15);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',15);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',15);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',15);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',15);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',15);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (16,'(CUADRO 5) - INFRAESTR_VIARIA','eiel_c_redviaria_tu');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',16);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',16);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',16);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',16);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',16);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',16);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',16);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (17,'(CUADRO 6) - CAP_AGUA_NUCLEO','eiel_tr_abast_ca_pobl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',17);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',17);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',17);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',17);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',17);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (18,'(CUADRO 7) - CAPTACION_ENC','eiel_t_abast_ca');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',18);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (19,'(CUADRO 8) - CAPTACION_ENC_M50','eiel_t_abast_ca');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',19);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',19);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',19);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (20,'(CUADRO 9) - COND_AGUA_NUCLEO','eiel_tr_abast_tcn_pobl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',20);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',20);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (21,'(CUADRO 10) - CONDUCCION_ENC','eiel_t_abast_tcn');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',21);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',21);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (22,'(CUADRO 11) - CONDUCCION_ENC_M50','eiel_t_abast_tcn');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',22);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',22);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',22);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',22);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (23,'(CUADRO 12) - TRAMO_CONDUCCION','eiel_t_abast_tcn');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',23);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (24,'(CUADRO 13) - TRAMO_CONDUCCION_M50','eiel_t_abast_tcn');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',24);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',24);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (25,'(CUADRO 14) - DEPOSITO_AGUA_NUCLEO','eiel_tr_abast_de_pobl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',25);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (26,'(CUADRO 15) - DEPOSITO_ENC','eiel_t_abast_de');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',26);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',26);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (27,'(CUADRO 16) - DEPOSITO_ENC_M50','eiel_t_abast_de');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',27);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',27);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',27);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',27);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (28,'(CUADRO 17) - TRAT_POTA_NUCLEO','eiel_tr_abast_tp_pobl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',28);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',28);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',28);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',28);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (29,'(CUADRO 18) - POTABILIZACION_ENC','eiel_t_abast_tp');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',29);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',29);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (30,'(CUADRO 19) - POTABILIZACION_ENC_M50','eiel_t_abast_tp');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',30);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',30);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',30);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',30);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (31,'(CUADRO 20) - RED_DISTRIBUCION','eiel_c_abast_rd');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v08',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v09',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v10',31);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v11',31);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (32,'(CUADRO 21) - NUCL_ENCUESTADO_2','eiel_t_nucl_encuest_2');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',32);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',32);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',32);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',32);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',32);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',32);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (33,'(CUADRO 22) - NUCL_ENCUESTADO_3','eiel_t_abast_serv');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v08',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v09',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v10',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v11',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v12',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v13',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v14',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v15',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v16',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v17',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v18',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v19',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v20',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v21',33);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v22',33);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (34,'(CUADRO 23) - NUCL_ENCUESTADO_4','eiel_t_abast_au');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v08',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v09',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v10',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v11',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v12',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v13',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v14',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v15',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v16',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v17',34);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v18',34);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (35,'(CUADRO 24) - RAMAL_SANEAMIENTO','eiel_c_saneam_rs');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',35);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',35);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',35);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',35);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',35);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',35);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (36,'(CUADRO 25) - COLECTOR_NUCLEO','eiel_tr_saneam_tcl_pobl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',36);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',36);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',36);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',36);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (37,'(CUADRO 26) - COLECTOR_ENC','eiel_t_saneam_tcl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',37);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',37);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (38,'(CUADRO 27) - COLECTOR_ENC_M50','eiel_t_saneam_tcl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',38);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',38);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',38);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',38);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (39,'(CUADRO 28) - TRAMO_COLECTOR','eiel_c_saneam_tcl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',39);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (40,'(CUADRO 29) - v_TRAMO_COLECTOR_M50','eiel_c_saneam_tcl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',40);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (41,'(CUADRO 30) - EMISARIO_NUCLEO','eiel_tr_saneam_tem_pobl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',41);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (42,'(CUADRO 31) - EMISARIO_ENC','eiel_t_saneam_tem');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',42);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',42);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',42);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (43,'(CUADRO 32) - EMISARIO_ENC_M50','eiel_t_saneam_tem');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',43);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',43);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',43);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',43);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',43);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (44,'(CUADRO 33) - TRAMO_EMISARIO','eiel_c_saneam_tem');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',44);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (45,'(CUADRO 34) - v_TRAMO_EMISARIO_M50','eiel_c_saneam_tem');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',45);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (46,'(CUADRO 35) - NUCL_ENCUESTADO_5','eiel_t_saneam_serv');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v08',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v09',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v10',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v11',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v12',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v13',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v14',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v15',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v16',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v17',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v18',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v19',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v20',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v21',46);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v22',46);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (47,'(CUADRO 36) - DEP_AGUA_NUCLEO','eiel_tr_abast_de_pobl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',47);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (48,'(CUADRO 37) - DEPURADORA_ENC','eiel_t1_saneam_ed');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',48);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',48);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',48);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (49,'(CUADRO 38) - v_DEPURADORA_ENC_M50','eiel_t1_saneam_ed');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',49);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',49);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',49);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',49);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',49);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (50,'(CUADRO 39) - DEPURADORA_ENC_2','eiel_t2_saneam_ed');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',50);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',50);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',50);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',50);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',50);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',50);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',50);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v08',50);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (51,'(CUADRO 40) - DEPURADORA_ENC_2_M50','eiel_t2_saneam_ed');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',51);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',51);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',51);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',51);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',51);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',51);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',51);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (52,'(CUADRO 41) - SANEA_AUTONOMO','eiel_t_saneam_au');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v08',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v09',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v10',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v11',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v12',52);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v13',52);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (53,'(CUADRO 42) - RECOGIDA_BASURA','eiel_t_rb');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',53);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',53);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',53);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',53);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',53);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (54,'(CUADRO 43) - NUCL_ENCUESTADO_6','eiel_t_rb_serv');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',54);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',54);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',54);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',54);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',54);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',54);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',54);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v08',54);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (55,'(CUADRO 44) - VERTEDERO_NUCLEO','eiel_tr_vt_pobl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',55);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (56,'(CUADRO 45) - VERT_ENCUESTADO','eiel_t_vt');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',56);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',56);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',56);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (57,'(CUADRO 46) - VERT_ENCUESTADO_M50','eiel_t_vt');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',57);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',57);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',57);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',57);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',57);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (58,'(CUADRO 47) - ALUMBRADO','eiel_c_alum_pl');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',58);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',58);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',58);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',58);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',58);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',58);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (59,'(CUADRO 48) - NUCL_ENCUESTADO_7','eiel_t_inf_ttmm');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',59);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',59);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',59);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',59);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',59);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (60,'(CUADRO 49) - INSTAL_DEPORTIVA','eiel_t_id');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',60);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',60);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',60);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',60);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (61,'(CUADRO 51) - CENT_CULTURAL','eiel_t_cu');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',61);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',61);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',61);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',61);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',61);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (62,'(CUADRO 52) - CENT_CULTURAL_USOS','eiel_t_cu_usos');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',62);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (63,'(CUADRO 53) - PARQUE','eiel_t_pj');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',63);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',63);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',63);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',63);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (64,'(CUADRO 54) - LONJA_MERC_FERIA','v_lonja_merc_feria');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',64);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',64);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',64);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (65,'(CUADRO 55) - MATADERO','eiel_t_mt');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',65);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',65);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',65);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',65);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',65);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',65);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v07',65);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (66,'(CUADRO 56) - CEMENTERIO','eiel_t_ce');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',66);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (67,'(CUADRO 57) - TANATORIO','eiel_t_ta');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',67);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',67);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',67);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',67);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',67);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (68,'(CUADRO 58) - CENTRO_SANITARIO','eiel_t_sa');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',68);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',68);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',68);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',68);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',68);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (69,'(CUADRO 59) - CENTRO_ASISTENCIAL','eiel_t_as');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',69);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',69);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',69);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',69);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',69);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (70,'(CUADRO 60) - CENTRO_ENSENANZA','eiel_t_en');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',70);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',70);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',70);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',70);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (71,'(CUADRO 61) - NIVEL_ENSENANZA','eiel_t_en_nivel');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',71);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',71);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (72,'(CUADRO 62) - PROTECCION_CIVIL','eiel_t_ip');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',72);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',72);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',72);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',72);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (73,'(CUADRO 63) - CASA_CONSISTORIAL','eiel_t_cc');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',73);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',73);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v03',73);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v04',73);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v05',73);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v06',73);


INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (74,'(CUADRO 65) - EDIFIC_PUB_SIN_USO','eiel_t_su');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',74);
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v02',74);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (75,'(CUADRO 66) - NUC_ABANDONADO','eiel_t_nucleo_aband');
INSERT INTO "public".eiel_validacionesporcuadompt(id ,nombre,id_validacionesmpt ) VALUES (nextval('seq_eiel_validacionesporcuadompt'),'v01',75);

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (76,'Listados','');

INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (77,'Mun_enc_dis','eiel_t_mun_diseminados');

-- Fin validacionesMPT.sql

-- Inicio release31b.sql

delete from r_group_perm where idacl in (select idacl from acl where name like 'App.LocalGIS%');
delete from r_acl_perm where idacl in (select idacl from acl where name like 'App.LocalGIS%');
delete from acl where name like 'App.LocalGIS%';

delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.SSO'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.Editor'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.Planeamiento'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.Infraestructuras'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.GestorCapas'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.Backup'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.InfoReferencia'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.Planeamiento'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.Infraestructuras'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.Administracion'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.Informes'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.Catastro'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.LicenciasObra'));
delete from r_acl_perm where (idacl in (select idacl from acl where name like 'App.LocalGIS.LicenciasActividad'));

delete from usrgrouperm where def in ('LocalGIS.SSO.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.SSO.Test')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.Editor.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.Planeamiento.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.Infraestructuras.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.GestorCapas.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.Backup.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.InfoReferencia.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.Administracion.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.Informes.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.Catastro.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.LicenciasObra.Login')and idperm > 10000;
delete from usrgrouperm where def in ('LocalGIS.LicenciasActividad.Login')and idperm > 10000;

delete from acl where name like 'App.LocalGIS.SSO';
delete from acl where name like 'App.LocalGIS.Editor';
delete from acl where name like 'App.LocalGIS.Planeamiento';
delete from acl where name like 'App.LocalGIS.Infraestructuras';
delete from acl where name like 'App.LocalGIS.GestorCapas';
delete from acl where name like 'App.LocalGIS.Backup';
delete from acl where name like 'App.LocalGIS.InfoReferencia';
delete from acl where name like 'App.LocalGIS.Administracion';
delete from acl where name like 'App.LocalGIS.Informes';
delete from acl where name like 'App.LocalGIS.Catastro';
delete from acl where name like 'App.LocalGIS.LicenciasObra';
delete from acl where name like 'App.LocalGIS.LicenciasActividad';

insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.SSO');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.Editor');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.Planeamiento');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.Infraestructuras');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.GestorCapas');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.Backup');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.InfoReferencia');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.Administracion');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.Informes');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.Catastro');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.LicenciasObra');
insert into acl (idacl, name) values ((nextval('seq_acl')), 'App.LocalGIS.LicenciasActividad');

delete from r_group_perm where groupid=(select id from iusergrouphdr where name like 'ROL_SSO');
delete from iusergrouphdr where name='ROL_SSO';

insert into iusergrouphdr(id, name, mgrid, type, remarks, crtrid, crtndate,id_entidad) values ((select max(id)+1 from iusergrouphdr),'ROL_SSO',100,0,'Rol para SSO',100, '2011-04-14 00:00:00',0);

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.SSO.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.SSO'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.SSO'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.SSO'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.SSO.Test');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.SSO'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.SSO'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.SSO'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.Editor.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.Editor'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Editor'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Editor'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.Planeamiento.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.Planeamiento'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Planeamiento'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Planeamiento'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.Infraestructuras.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.Infraestructuras'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Infraestructuras'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Infraestructuras'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.GestorCapas.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.GestorCapas'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.GestorCapas'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.GestorCapas'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.Backup.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.Backup'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Backup'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Backup'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.InfoReferencia.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.InfoReferencia'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.InfoReferencia'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.InfoReferencia'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.Administracion.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.Administracion'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Administracion'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Administracion'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.Informes.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.Informes'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Informes'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Informes'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.Catastro.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.Catastro'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Catastro'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.Catastro'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.LicenciasObra.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.LicenciasObra'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.LicenciasObra'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.LicenciasObra'));

insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGIS.LicenciasActividad.Login');
insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'App.LocalGIS.LicenciasActividad'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SuperAdministrador'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.LicenciasActividad'));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'ROL_SSO'), (select cast(max(idperm) as int) from usrgrouperm), (select idacl from acl where name like 'App.LocalGIS.LicenciasActividad'));

-- Fin release31b.sql
