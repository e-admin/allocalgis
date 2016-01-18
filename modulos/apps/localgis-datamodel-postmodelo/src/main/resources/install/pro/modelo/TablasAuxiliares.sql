DROP TABLE IF EXISTS eiel_t_entidades_agrupadas;
CREATE TABLE eiel_t_entidades_agrupadas
(
  codmunicipio character varying(3) NOT NULL,
  codentidad character varying(4) NOT NULL,
  codnucleo character varying(2) NOT NULL,
  codentidad_agrupada character varying(4) NOT NULL,
  codnucleo_agrupado character varying(2) NOT NULL,
  bloqueado character varying(32), 
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  bloqueado VARCHAR(32), 
  CONSTRAINT eiel_t_entidades_agrupadas_pkey PRIMARY KEY (codmunicipio,codentidad , codnucleo , revision_actual )
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_t_entidades_agrupadas
  OWNER TO postgres;
GRANT ALL ON TABLE eiel_t_entidades_agrupadas TO postgres;
GRANT SELECT ON TABLE eiel_t_entidades_agrupadas TO consultas;


DROP TABLE IF EXISTS eiel_tr_abast_ca_pobl;
CREATE TABLE "public".eiel_tr_abast_ca_pobl
(
  clave_ca character varying (3) NOT NULL,
  codprov_ca character varying (3) NOT NULL,
  codmunic_ca character varying (3) NOT NULL,
  orden_ca character varying (3) NOT NULL, 
  codprov_pobl character varying (2) NOT NULL,
  codmunic_pobl character varying (3) NOT NULL,
  codentidad_pobl character varying (4) NOT NULL,
  codpoblamiento_pobl character varying (2) NOT NULL,
  observ character varying(50),
  fecha_inicio_serv date,
  fecha_revision date,
  estado_revision integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_tr_abast_ca_pobl_pkey PRIMARY KEY (clave_ca, codprov_ca, codmunic_ca, orden_ca,codprov_pobl,codmunic_pobl,codentidad_pobl,codpoblamiento_pobl, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_abast_ca_pobl OWNER TO postgres;



DROP TABLE IF EXISTS eiel_tr_abast_de_pobl;
CREATE TABLE "public".eiel_tr_abast_de_pobl
(
  clave_de character varying (3) NOT NULL,
  codprov_de character varying (3) NOT NULL,
  codmunic_de character varying (3) NOT NULL,
  orden_de character varying (3) NOT NULL, 
  codprov_pobl character varying (2) NOT NULL,
  codmunic_pobl character varying (3) NOT NULL ,
  codentidad_pobl character varying (4) NOT NULL ,
  codpoblamiento_pobl character varying (2) NOT NULL,
  observ character varying(50),
  fecha_inicio_serv date,
  fecha_revision date,
  estado_revision integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_tr_abast_de_pobl_pkey PRIMARY KEY (clave_de, codprov_de, codmunic_de, orden_de ,codprov_pobl,codmunic_pobl,codentidad_pobl,codpoblamiento_pobl, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_abast_de_pobl OWNER TO postgres;




DROP TABLE IF EXISTS eiel_tr_abast_tp_pobl;
CREATE TABLE "public".eiel_tr_abast_tp_pobl
(
  clave_tp character varying (3) NOT NULL,
  codprov_tp character varying (3) NOT NULL,
  codmunic_tp character varying (3) NOT NULL,
  orden_tp character varying (3) NOT NULL, 
  codprov_pobl character varying (2) NOT NULL,
  codmunic_pobl character varying (3) NOT NULL ,
  codentidad_pobl character varying (4) NOT NULL ,
  codpoblamiento_pobl character varying (2) NOT NULL,
  observ character varying(50),
  fecha_inicio_serv date,
  fecha_revision date,
  estado_revision integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_tr_abast_tp_pobl_pkey PRIMARY KEY (clave_tp, codprov_tp, codmunic_tp, orden_tp,codprov_pobl,codmunic_pobl,codentidad_pobl,codpoblamiento_pobl, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_abast_tp_pobl OWNER TO postgres;


DROP TABLE IF EXISTS eiel_tr_abast_tcn_pobl;
CREATE TABLE "public".eiel_tr_abast_tcn_pobl
(
  clave_tcn character varying (3) NOT NULL,
  codprov_tcn character varying (3) NOT NULL,
  codmunic_tcn character varying (3) NOT NULL,
  tramo_tcn character varying (3) NOT NULL, 
  codprov_pobl character varying (2) NOT NULL,
  codmunic_pobl character varying (3) NOT NULL,
  codentidad_pobl character varying (4) NOT NULL ,
  codpoblamiento_pobl character varying (2) NOT NULL,
  observ character varying(250),
  pmi numeric (8,2),
  pmf numeric (8,2),
  fecha_revision date,
  estado_revision integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_tr_abast_tcn_pobl_pkey PRIMARY KEY (clave_tcn,codprov_tcn, codmunic_tcn, tramo_tcn, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, revision_actual)
)WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_abast_tcn_pobl OWNER TO postgres;



DROP TABLE IF EXISTS eiel_tr_saneam_ed_pobl;
CREATE TABLE "public".eiel_tr_saneam_ed_pobl
(  
  clave_ed character varying (2) NOT NULL,
  codprov_ed character varying (2) NOT NULL,
  codmunic_ed character varying (3) NOT NULL,
  orden_ed character varying (3) NOT NULL, 
  codprov_pobl character varying (2) NOT NULL,
  codmunic_pobl character varying (3) NOT NULL ,
  codentidad_pobl character varying (4) NOT NULL ,
  codpoblamiento_pobl character varying (2) NOT NULL,
  observ character varying(50),
  fecha_inicio_serv date,
  fecha_revision date,
  estado_revision integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_tr_saneam_ed_pobl_pkey PRIMARY KEY (clave_ed, codmunic_ed, codprov_ed,orden_ed,codprov_pobl,codmunic_pobl,codentidad_pobl,codpoblamiento_pobl, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_saneam_ed_pobl OWNER TO postgres;


DROP TABLE IF EXISTS eiel_tr_saneam_pv_pobl;
CREATE TABLE "public".eiel_tr_saneam_pv_pobl
(
  clave_pv character varying (2) NOT NULL,
  codprov_pv character varying (2) NOT NULL,
  codmunic_pv character varying (3) NOT NULL,
  orden_pv character varying (3) NOT NULL,
  codprov_pobl character varying (2) NOT NULL,
  codmunic_pobl character varying (3) NOT NULL,
  codentidad_pobl character varying (4) NOT NULL ,
  codpoblamiento_pobl character varying (2) NOT NULL,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_tr_saneam_pv_pobl_pkey PRIMARY KEY (clave_pv, codprov_pv, codmunic_pv, orden_pv,codprov_pobl,codmunic_pobl,codentidad_pobl,codpoblamiento_pobl, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_saneam_pv_pobl OWNER TO postgres;

DROP TABLE IF EXISTS eiel_tr_saneam_tcl_pobl;
CREATE TABLE "public".eiel_tr_saneam_tcl_pobl
(
  clave_tcl character varying (2) NOT NULL,
  codprov_tcl character varying (2) NOT NULL,
  codmunic_tcl character varying (3) NOT NULL,
  tramo_cl character varying (3) NOT NULL ,
  codprov_pobl character varying (2) NOT NULL,
  codmunic_pobl character varying (3) NOT NULL ,
  codentidad_pobl character varying (4) NOT NULL ,
  codpoblamiento_pobl character varying (2) NOT NULL,
  pmi numeric(8,2),
  pmf numeric (8,2),
  observ character varying(50),
  fecha_inicio_serv date,
  fecha_revision date,
  estado_revision integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_tr_saneam_tcl_pobl_pkey PRIMARY KEY (clave_tcl, codprov_tcl, codmunic_tcl, tramo_cl,codprov_pobl,codmunic_pobl,codentidad_pobl,codpoblamiento_pobl, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_saneam_tcl_pobl OWNER TO postgres;



DROP TABLE IF EXISTS eiel_tr_saneam_tem_pobl;
CREATE TABLE "public".eiel_tr_saneam_tem_pobl
(
  clave_tem character varying (2) NOT NULL,
  codprov_tem character varying (2) NOT NULL,
  codmunic_tem character varying (3) NOT NULL,
  tramo_em character varying (3) NOT NULL,
  pmi numeric(8,2),
  pmf numeric(8,2),
  codprov_pobl character varying (2) NOT NULL,
  codmunic_pobl character varying (3) NOT NULL ,
  codentidad_pobl character varying (4) NOT NULL ,
  codpoblamiento_pobl character varying (2) NOT NULL,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_tr_saneam_tem_pobl_pkey PRIMARY KEY (clave_tem, codprov_tem, codmunic_tem, tramo_em,codprov_pobl,codmunic_pobl,codentidad_pobl,codpoblamiento_pobl, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_saneam_tem_pobl OWNER TO postgres;


DROP TABLE IF EXISTS eiel_tr_saneam_tem_pv;
CREATE TABLE "public".eiel_tr_saneam_tem_pv
(
  clave_tem character varying (2) NOT NULL,
  codprov_tem character varying (2) NOT NULL,
  codmunic_tem character varying (3) NOT NULL,
  tramo_em character varying (3) NOT NULL,
  pmi numeric(8,2),
  pmf numeric(8,2),
  codprov_pv character varying (2) NOT NULL,
  codmunic_pv character varying (3) NOT NULL ,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  clave_pv character varying (3),
  orden_pv character varying (3),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_tr_saneam_tem_pv_pkey PRIMARY KEY (clave_tem, codprov_tem, codmunic_tem, tramo_em,codprov_pv,codmunic_pv, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_saneam_ed_pobl OWNER TO postgres;


CREATE TABLE "public".eiel_tr_vt_pobl
(
  clave_vt character varying (3) NOT NULL,
  codprov_vt character varying (3) NOT NULL,
  codmunic_vt character varying (3) NOT NULL,
  orden_vt character varying (3) NOT NULL, 
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL ,
  codentidad character varying (4) NOT NULL ,
  codpoblamiento character varying (2) NOT NULL,
  fecha_alta date,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_tr_vt_pobl_pkey PRIMARY KEY (clave_vt, codprov_vt, codmunic_vt, orden_vt, codprov, codmunic, codentidad, codpoblamiento, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_tr_vt_pobl OWNER TO postgres;




------RECOGIDA DE BASURAS T


CREATE TABLE "public".eiel_t_rb
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
 tipo character varying(2),
  gestor character varying(3),
  periodicidad character varying(2),
  calidad character varying(2),
  tm_res_urb numeric(9,3),
  n_contenedores integer,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_rb_pkey PRIMARY KEY (codprov,codmunic,codentidad,codpoblamiento, tipo, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_rb OWNER TO postgres;



CREATE TABLE "public".eiel_t_rb_serv
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  srb_viviendas_afec integer,
  srb_pob_res_afect integer,
  srb_pob_est_afect integer,
  serv_limp_calles character varying (2),
  plantilla_serv_limp integer,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_rb_serv_pkey PRIMARY KEY (codprov, codmunic,codentidad,codpoblamiento, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_rb_serv OWNER TO postgres;


CREATE TABLE "public".eiel_t_nucl_encuest_1
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  padron integer,
  pob_estacional integer,
  altitud integer,
  viviendas_total integer,
  hoteles integer,
  casas_rural integer,
  accesibilidad character varying(2),
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_nucl_encuest_1_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_nucl_encuest_1 OWNER TO postgres;






CREATE TABLE "public".eiel_t_padron_nd
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  n_hombres_a1 integer,
  n_mujeres_a1 integer,
  total_poblacion_a1 integer,
  n_hombres_a2 integer,
  n_mujeres_a2 integer,
  total_poblacion_a2 integer,
  fecha_revision date,
  observ character varying(50),
  fecha_a1 integer,--solo anio
  fecha_a2 integer,--solo anio
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_padron_nd_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_padron_nd OWNER TO postgres;


create index i_codprov_t_padron_nd on "public".eiel_t_padron_nd(codprov);
create index i_codmunic_t_padron_nd on "public".eiel_t_padron_nd(codmunic);
create index i_codentidad_padron_nd on "public".eiel_t_padron_nd(codentidad);
create index i_codpoblamiento_padron_nd on "public".eiel_t_padron_nd(codpoblamiento);


-- Table: eiel_t_padron_ttmm

-- DROP TABLE eiel_t_padron_ttmm;

CREATE TABLE eiel_t_padron_ttmm
(
  codprov character varying(2) NOT NULL,
  codmunic character varying(3) NOT NULL,
  n_hombres_a1 integer,
  n_mujeres_a1 integer,
  total_poblacion_a1 integer,
  n_hombres_a2 integer,
  n_mujeres_a2 integer,
  total_poblacion_a2 integer,
  fecha_revision date,
  observ character varying(50),
  fecha_a1 integer,
  fecha_a2 integer,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_padron_ttmm_pkey PRIMARY KEY (codprov , codmunic , revision_actual )
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_t_padron_ttmm
  OWNER TO postgres;
GRANT ALL ON TABLE eiel_t_padron_ttmm TO postgres;
GRANT SELECT ON TABLE eiel_t_padron_ttmm TO consultas;

-- Index: i_codmunic_t_padron_ttmm

-- DROP INDEX i_codmunic_t_padron_ttmm;

CREATE INDEX i_codmunic_t_padron_ttmm
  ON eiel_t_padron_ttmm
  USING btree
  (codmunic );

-- Index: i_codprov_t_padron_ttmm

-- DROP INDEX i_codprov_t_padron_ttmm;

CREATE INDEX i_codprov_t_padron_ttmm
  ON eiel_t_padron_ttmm
  USING btree
  (codprov );






-----INFORMACION GENERAL DEL NUCLEO

CREATE TABLE "public".eiel_t_inf_ttmm
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  tv_ant character varying(1),
  tv_ca character varying(1),
  tm_gsm character varying(1),
  tm_umts character varying(1),
  tm_gprs character varying(1),
 correo character varying(2),
  ba_rd character varying(2),
  ba_xd character varying(2),
  ba_wi character varying(2),
  ba_ca character varying(2),
  ba_rb character varying(2),
  ba_st character varying(2),
  capi character varying(2),
  electric character varying(1),
  gas character varying(1),
  alu_v_sin integer,
  alu_l_sin integer,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_inf_ttmm_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_inf_ttmm OWNER TO postgres;

ALTER TABLE eiel_t_inf_ttmm ADD CONSTRAINT ba_rdchk CHECK (eiel_t_inf_ttmm.ba_rd= 'SI' OR eiel_t_inf_ttmm.ba_rd = 'NO' );
ALTER TABLE eiel_t_inf_ttmm ADD CONSTRAINT ba_xdchk CHECK (eiel_t_inf_ttmm.ba_xd= 'SI' OR eiel_t_inf_ttmm.ba_xd = 'NO');
ALTER TABLE eiel_t_inf_ttmm ADD CONSTRAINT ba_wichk CHECK (eiel_t_inf_ttmm.ba_wi= 'SI' OR eiel_t_inf_ttmm.ba_wi = 'NO');
ALTER TABLE eiel_t_inf_ttmm ADD CONSTRAINT ba_cachk CHECK (eiel_t_inf_ttmm.ba_ca= 'SI' OR eiel_t_inf_ttmm.ba_ca = 'NO');
ALTER TABLE eiel_t_inf_ttmm ADD CONSTRAINT ba_rbchk CHECK (eiel_t_inf_ttmm.ba_rb= 'SI' OR eiel_t_inf_ttmm.ba_rb = 'NO');
ALTER TABLE eiel_t_inf_ttmm ADD CONSTRAINT ba_stchk CHECK (eiel_t_inf_ttmm.ba_st= 'SI' OR eiel_t_inf_ttmm.ba_st= 'NO');
ALTER TABLE eiel_t_inf_ttmm ADD CONSTRAINT capichk CHECK (eiel_t_inf_ttmm.capi= 'SI' OR eiel_t_inf_ttmm.capi = 'NO');
ALTER TABLE eiel_t_inf_ttmm ADD CONSTRAINT electricchk CHECK (eiel_t_inf_ttmm.electric= 'B' OR eiel_t_inf_ttmm.electric = 'E'
OR eiel_t_inf_ttmm.electric = 'M' OR eiel_t_inf_ttmm.electric = 'R' OR eiel_t_inf_ttmm.electric = 'C');
ALTER TABLE eiel_t_inf_ttmm ADD CONSTRAINT gaschk CHECK (eiel_t_inf_ttmm.gas= 'B' OR eiel_t_inf_ttmm.gas = 'E'
OR eiel_t_inf_ttmm.gas = 'M' OR eiel_t_inf_ttmm.gas = 'R' OR eiel_t_inf_ttmm.gas = 'C');



---TRATAMIENTO DE PLAN URBANISTICO

CREATE TABLE "public".eiel_t_planeam_urban
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  tipo_urba character varying(3) ,
 estado_tramit character varying(2),
  denominacion character varying(40),
  sup_muni numeric(9,2),
  fecha_bo date,
  s_urbano numeric(9,2),
  s_urbanizable numeric(9,2),
  s_no_urbanizable numeric(9,2),
  s_no_urban_especial numeric(9,2),
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  orden_plan  character varying (3) NOT NULL,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_planeam_urban_pkey PRIMARY KEY (codprov, codmunic, tipo_urba, orden_plan, revision_actual)
)WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_planeam_urban OWNER TO postgres;

ALTER TABLE eiel_t_planeam_urban ADD CONSTRAINT tipo_urbanchk CHECK (eiel_t_planeam_urban.tipo_urba = 'D.C' OR
eiel_t_planeam_urban.tipo_urba = 'D.S' OR eiel_t_planeam_urban.tipo_urba = 'N.M' OR eiel_t_planeam_urban.tipo_urba = 'N.P'
OR eiel_t_planeam_urban.tipo_urba = 'P.G' OR eiel_t_planeam_urban.tipo_urba = 'P.S' OR eiel_t_planeam_urban.tipo_urba = 'P.E');

ALTER TABLE eiel_t_planeam_urban ADD CONSTRAINT estado_tramitchk CHECK (
eiel_t_planeam_urban.estado_tramit = 'AD' OR eiel_t_planeam_urban.estado_tramit = 'AI' OR eiel_t_planeam_urban.estado_tramit = 'AP' OR
eiel_t_planeam_urban.estado_tramit = 'EL' OR eiel_t_planeam_urban.estado_tramit = 'ER');



CREATE TABLE "public".eiel_t_otros_serv_munic
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
 sw_inf_grl character varying(2),
  sw_inf_tur character varying(2),
  sw_gb_elec character varying(2),
  ord_soterr character varying(2),
  en_eolica character varying(2),
  kw_eolica integer,
  en_solar character varying(2),
  kw_solar integer,
  pl_mareo character varying(2),
  kw_mareo integer,
  ot_energ character varying(2),
  kw_ot_energ integer,
  cob_serv_tlf_m character varying(2),
  tv_dig_cable character varying(2),
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_otros_serv_munic_pkey PRIMARY KEY (codprov, codmunic, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_otros_serv_munic OWNER TO postgres;

ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT sw_inf_grlchk CHECK (eiel_t_otros_serv_munic.sw_inf_grl = 'NO' OR
eiel_t_otros_serv_munic.sw_inf_grl = 'SI' );
ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT sw_inf_turchk CHECK (eiel_t_otros_serv_munic.sw_inf_tur = 'NO' OR
eiel_t_otros_serv_munic.sw_inf_tur = 'SI' );
ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT sw_gb_elecchk CHECK (eiel_t_otros_serv_munic.sw_gb_elec = 'NO' OR
eiel_t_otros_serv_munic.sw_gb_elec = 'SI' );
ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT ord_soterrchk CHECK (eiel_t_otros_serv_munic.ord_soterr = 'NO' OR
eiel_t_otros_serv_munic.ord_soterr = 'SI' );
ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT en_eolicachk CHECK (eiel_t_otros_serv_munic.en_eolica = 'NO' OR
eiel_t_otros_serv_munic.en_eolica = 'SI' );
ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT en_solarchk CHECK (eiel_t_otros_serv_munic.en_solar = 'NO' OR
eiel_t_otros_serv_munic.en_solar = 'SI' );
ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT pl_mareochk CHECK (eiel_t_otros_serv_munic.pl_mareo = 'NO' OR
eiel_t_otros_serv_munic.pl_mareo = 'SI' );
ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT ot_energchk CHECK (eiel_t_otros_serv_munic.ot_energ = 'NO' OR
eiel_t_otros_serv_munic.ot_energ = 'SI' );
ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT cob_serv_tlf_mchk CHECK (eiel_t_otros_serv_munic.cob_serv_tlf_m = 'NO' OR
eiel_t_otros_serv_munic.cob_serv_tlf_m = 'SI' );
ALTER TABLE eiel_t_otros_serv_munic ADD CONSTRAINT tv_dig_cablechk CHECK (eiel_t_otros_serv_munic.tv_dig_cable = 'NO' OR
eiel_t_otros_serv_munic.tv_dig_cable = 'SI' );




-----TABLA DE DISEMINADO


CREATE TABLE "public".eiel_t_mun_diseminados
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  padron_dis numeric(6,0),
  pob_estaci numeric(6,0),
  viv_total numeric(8,0),
  hoteles numeric(8,0),
  casas_rural numeric(8,0),
  longitud numeric(8,0),
  aag_v_cone numeric(8,0),
  aag_v_ncon numeric(8,0),
  aag_c_invi numeric(8,0),
  aag_c_vera numeric(8,0),
  aag_v_expr numeric(8,0),
  aag_v_depr numeric(8,0),
  aag_l_defi numeric(8,0),
  aag_v_defi numeric(8,0),
  aag_pr_def numeric(8,0),
  aag_pe_def numeric(8,0),
  aau_pob_re numeric(8,0),
  aau_pob_es numeric(8,0),
  aau_def_vi numeric(8,0),
  aau_def_re numeric(8,0),
  aau_def_es numeric(8,0),
  aau_fencon numeric(8,0),
  longi_ramal numeric(8,0),
  syd_v_cone numeric(8,0),
  syd_v_ncon numeric(8,0),
  syd_l_defi numeric(8,0),
  syd_v_defi numeric(8,0),
  syd_pr_def numeric(8,0),
  syd_pe_def numeric(8,0),
  syd_c_desa numeric(8,0),
  syd_c_trat numeric(8,0),
  sau_vivien numeric(8,0),
  sau_pob_es numeric(8,0),
  sau_vi_def numeric(8,0),
  sau_pob_re_def numeric(8,0),
  sau_pob_es_def numeric(8,0),
  produ_basu numeric(8,0),
  contenedores numeric(8,0),
  rba_v_sser numeric(8,0),
  rba_pr_sse numeric(8,0),
  rba_pe_sse numeric(8,0),
  rba_plalim numeric(8,0),
  puntos_luz numeric(8,0),
  alu_v_sin numeric(8,0),
  alu_l_sin numeric(8,0),
  aau_vivien numeric(8,0),
  aau_fecont numeric(8,0),
  sau_pob_re numeric(8,0),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_mun_diseminados_pkey PRIMARY KEY (codprov, codmunic, revision_actual)
)WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_mun_diseminados OWNER TO postgres;




CREATE TABLE "public".eiel_t_cabildo_consejo
(
  codprov character varying(2) NOT NULL,
  cod_isla character varying(2) NOT NULL,
  denominacion character varying(16),
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_cabildo_consejo_pkey PRIMARY KEY (codprov,cod_isla, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_cabildo_consejo OWNER TO postgres;



CREATE TABLE "public".eiel_t_entidad_singular
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  denominacion character varying(50),
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_entidad_singular_pkey PRIMARY KEY (codprov, codmunic, codentidad, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_entidad_singular OWNER TO postgres;



CREATE TABLE "public".eiel_t_poblamiento
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  denominacion character varying(50),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_poblamiento_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual)
)WITH(
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_poblamiento OWNER TO postgres;



CREATE TABLE "public".eiel_t_nucleo_aband
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  a_abandono character varying(4),
  causa_abandono character varying(2),
  titular_abandono character varying(2),
  rehabilitacion character varying(2),
  acceso character varying(2),
  serv_agua character varying(2),
  serv_elect character varying(2),
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_nucleo_aband_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_nucleo_aband OWNER TO postgres;

ALTER TABLE eiel_t_nucleo_aband ADD CONSTRAINT causa_abandonochk CHECK (eiel_t_nucleo_aband.causa_abandono= 'EM' OR
eiel_t_nucleo_aband.causa_abandono= 'EX' OR eiel_t_nucleo_aband.causa_abandono= 'IN' OR
eiel_t_nucleo_aband.causa_abandono= 'OT');
ALTER TABLE eiel_t_nucleo_aband ADD CONSTRAINT titular_abandonochk CHECK (eiel_t_nucleo_aband.titular_abandono= 'AB' OR
eiel_t_nucleo_aband.titular_abandono= 'CH' OR eiel_t_nucleo_aband.titular_abandono= 'OP' OR
eiel_t_nucleo_aband.titular_abandono= 'OT' OR eiel_t_nucleo_aband.titular_abandono= 'PV');
ALTER TABLE eiel_t_nucleo_aband ADD CONSTRAINT rehabilitacionchk CHECK (eiel_t_nucleo_aband.rehabilitacion= 'SI' OR
eiel_t_nucleo_aband.rehabilitacion= 'NO' );
ALTER TABLE eiel_t_nucleo_aband ADD CONSTRAINT accesochk CHECK (eiel_t_nucleo_aband.acceso= 'CA' OR
eiel_t_nucleo_aband.acceso= 'CN' OR eiel_t_nucleo_aband.acceso= 'IN' OR
eiel_t_nucleo_aband.acceso= 'RG' OR eiel_t_nucleo_aband.acceso= 'VE');
ALTER TABLE eiel_t_nucleo_aband ADD CONSTRAINT serv_aguachk CHECK (eiel_t_nucleo_aband.serv_agua= 'CA' OR
eiel_t_nucleo_aband.serv_agua= 'CN' OR eiel_t_nucleo_aband.serv_agua= 'IN' OR
eiel_t_nucleo_aband.serv_agua= 'RG' OR eiel_t_nucleo_aband.serv_agua= 'VE');
ALTER TABLE eiel_t_nucleo_aband ADD CONSTRAINT serv_electchk CHECK (eiel_t_nucleo_aband.serv_elect= 'SI' OR
eiel_t_nucleo_aband.serv_elect= 'NO');




CREATE TABLE versionesalfa
(
  revision numeric(10,0) NOT NULL,
  id_autor numeric(10,0) NOT NULL,
  id_table_versionada character varying(100) NOT NULL,
  fecha timestamp without time zone,
  tipocambio character varying(100),
  CONSTRAINT pk_versionesalfa PRIMARY KEY (revision , id_table_versionada ),
  CONSTRAINT id_autor_fkey FOREIGN KEY (id_autor)
      REFERENCES iuseruserhdr (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=TRUE
);
ALTER TABLE versionesalfa
  OWNER TO geopista;
GRANT ALL ON TABLE versionesalfa TO geopista;
GRANT SELECT ON TABLE versionesalfa TO consultas;

-- Index: indx_id_tabla_versionadaalfa

-- DROP INDEX indx_id_tabla_versionadaalfa;

CREATE INDEX indx_id_tabla_versionadaalfa
  ON versionesalfa
  USING btree
  (id_table_versionada );

-- Index: versionesalfa_by_user

-- DROP INDEX versionesalfa_by_user;

CREATE INDEX versionesalfa_by_user
  ON versionesalfa
  USING btree
  (id_autor );