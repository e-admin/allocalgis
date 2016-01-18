CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'eiel_validacionesmpt') 
	THEN
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
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'eiel_validacionesporcuadompt') THEN
		CREATE TABLE "public".eiel_validacionesporcuadompt
		(
		  id numeric(8,0) NOT NULL,
		  nombre character varying(100) NOT NULL,
		  id_validacionesmpt character varying(100) NOT NULL,
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

	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";


DELETE FROM "public".eiel_validacionesporcuadompt;
DELETE FROM "public".eiel_validacionesmpt;

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