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