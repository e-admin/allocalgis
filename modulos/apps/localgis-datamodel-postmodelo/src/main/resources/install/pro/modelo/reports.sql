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
    
    
