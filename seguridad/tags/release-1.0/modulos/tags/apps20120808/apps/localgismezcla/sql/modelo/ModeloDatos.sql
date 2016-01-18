--CREATE SCHEMA "public" AUTHORIZATION postgres;


-----OTRAS CAPAS DE GEOMETRIAS

CREATE TABLE "public".eiel_c_provincia
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
"GEOMETRY" geometry,
  codprov character varying (2) NOT NULL,
  nombre character varying(35),
  CONSTRAINT eiel_c_provincia_pkey PRIMARY KEY (id,codprov)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_provincia OWNER TO postgres;

create index i_codprov_provincias on "public".eiel_c_provincia(codprov);
create index i_id_provincias on "public".eiel_c_provincia(id);

CREATE INDEX provincias_spat_idx
  ON eiel_c_provincia
  USING gist
  ("GEOMETRY");

CREATE SEQUENCE "public"."seq_eiel_c_provincia"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;
    
CREATE TABLE "public".eiel_c_comarcas
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
"GEOMETRY" geometry,
  codcomarca character varying (2) NOT NULL,
  nombre_comarca character varying(50),
  hectareas numeric(10,2),
  perimetro numeric(10,2),
  codmunic_capital1 character varying(3),
  codmunic_capital2 character varying(3),
  fecha_revision date,
  observ character varying(60),
  uso_utm character varying(5),
  CONSTRAINT eiel_c_comarcas_pkey PRIMARY KEY (id,codcomarca)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_comarcas OWNER TO postgres;

CREATE INDEX eiel_c_comarcas_idmunicipio_key ON eiel_c_comarcas (id);
create index i_codcomarca on "public".eiel_c_comarcas(codcomarca);

CREATE SEQUENCE "public"."seq_eiel_c_comarcas"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_municipios
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
"GEOMETRY" geometry,
  codprov character varying (2) NOT NULL,
  nombre_oficial character varying(50),
  cod_catastro character varying (3),
  codmunic character varying (3) NOT NULL,
  uso_utm character varying (5),
  id_comarca integer,
  CONSTRAINT eiel_c_municipios_pkey PRIMARY KEY (id,codprov,codmunic)

)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_municipios OWNER TO postgres;

create index i_id_municipios on "public".eiel_c_municipios(id) ;
create index i_id_municipio_municipios on "public".eiel_c_municipios(id_municipio) ;
create index i_codmunic_municipios on "public".eiel_c_municipios(codmunic);

CREATE INDEX municipios_spat_idx
  ON eiel_c_municipios
  USING gist
  ("GEOMETRY");

CREATE SEQUENCE "public"."seq_eiel_c_municipios"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_municipios_puntos
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
"GEOMETRY" geometry,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  nombre_oficial character varying(50),
  cod_catastro character varying (3),
  id_comarca integer,
  huso_utm character varying(5),
  CONSTRAINT eiel_c_municipios_puntos_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_municipios_puntos OWNER TO postgres;

create index i_id_municipios_puntos on "public".eiel_c_municipios_puntos(id);
create index i_id_municipio_municipios_puntos on "public".eiel_c_municipios_puntos(id_municipio) ;
create index i_codmunic_municipios_puntos on "public".eiel_c_municipios_puntos(codmunic);

  CREATE INDEX municipios_puntos_spat_idx
  ON eiel_c_municipios_puntos
  USING gist
  ("GEOMETRY");
  
CREATE SEQUENCE "public"."seq_eiel_c_municipios_puntos"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_nucleo_poblacion
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
"GEOMETRY" geometry,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  nombre_oficial character varying(50) NOT NULL,
  fecha_alta date,
  observ character varying(250),	
  fecha_revision date,
  estado_revision integer,
  CONSTRAINT eiel_c_nucleo_poblacion_pkey PRIMARY KEY (id)

)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_nucleo_poblacion OWNER TO postgres;

create index i_id_nucleo_poblacion on "public".eiel_c_nucleo_poblacion(id);
CREATE INDEX eiel_c_nucleo_poblacion_idmunicipio_key on "public".eiel_c_nucleo_poblacion (id_municipio);
create index i_codpoblamiento_nucleo_poblacion on "public".eiel_c_nucleo_poblacion(codpoblamiento);

CREATE SEQUENCE "public"."seq_eiel_c_nucleo_poblacion"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;
    
CREATE TABLE "public".eiel_c_nucleos_puntos
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
"GEOMETRY" geometry,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  nombre_oficial character varying(50) NOT NULL,
  fecha_alta date,
  observ character varying(50),	
  fecha_revision date,
  estado_revision integer,
  CONSTRAINT eiel_c_nucleos_puntos_pkey PRIMARY KEY (id)
 
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_nucleos_puntos OWNER TO postgres;

create index i_id_nucleo_puntos on "public".eiel_c_nucleo_poblacion(id);


CREATE SEQUENCE "public"."seq_eiel_c_nucleos_puntos"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_parcelas
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
"GEOMETRY" geometry,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codmasa character varying (3) NOT NULL,
  codparcela character varying (3) NOT NULL,
  num_policia integer,
  num_pol_dup character varying(1),
  clave_infr character varying(2),
  orden_infr character varying (3) NOT NULL,
  nombre_oficial character varying(150) NOT NULL,
  observ character varying(100),	
  CONSTRAINT eiel_c_parcelas_pkey PRIMARY KEY (id) 
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_parcelas OWNER TO postgres;

create index i_id_parcelas on "public".eiel_c_parcelas(id);
create index i_codprov_parcelas on "public".eiel_c_parcelas(codprov);
create index i_codmunic_parcelas on "public".eiel_c_parcelas(codmunic);
create index i_codentidad_parcelas on "public".eiel_c_parcelas(codentidad);
create index i_codmasa_parcelas on "public".eiel_c_parcelas(codmasa);
create index i_codparcela_parcelas on "public".eiel_c_parcelas(codparcela);
CREATE INDEX eiel_c_parcelas_idmunicipio_key ON "public".eiel_c_parcelas( id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_parcelas"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

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



--ABASTECIMIENTO C

CREATE TABLE "public".eiel_c_abast_ar
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
"GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL, 
  codpoblamiento character varying (2) NOT NULL,
  orden_ar character varying (4) NOT NULL,
  estado character varying(1),
  cota_z numeric(8,0),
  obra_ejec character varying(2),
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_abast_ar_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_abast_ar OWNER TO postgres;

create index i_id_ar on "public".eiel_c_abast_ar(id);
CREATE INDEX eiel_c_abast_ar_idmunicipio_key ON "public".eiel_c_abast_ar(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_abast_ar"  
	INCREMENT 1  MINVALUE 1
    MAXVALUE 99999999  START 1
    CACHE 1;
    

CREATE TABLE "public".eiel_c_abast_ca
(
  id numeric(8,0) NOT NULL ,
   id_municipio numeric(5,0) NOT NULL,
"GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_ca character varying (3) NOT NULL,
  cota_z numeric(8,0),
  obra_ejec character varying (2),
  observ character varying(100),
  precision_dig character varying(2),
  CONSTRAINT eiel_c_abast_ca_pkey PRIMARY KEY (id)
)WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_abast_ca OWNER TO postgres;
ALTER TABLE eiel_c_abast_ca ADD CONSTRAINT clavechk CHECK (eiel_c_abast_ca.clave = 'CA');

create index i_id_ca on "public".eiel_c_abast_ca(id);
CREATE INDEX eiel_c_abast_ca_idmunicipio_key ON "public".eiel_c_abast_ca(id_municipio);
  
CREATE SEQUENCE "public"."seq_eiel_c_abast_ca"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_abast_de
(
  id numeric(8,0) NOT NULL ,
   id_municipio numeric(5,0) NOT NULL ,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_de character varying (3) NOT NULL,
  cota_z numeric(8,0),
  obra_ejec character varying(2),
  observ character varying(100),
  precision_dig character varying(2),
  CONSTRAINT eiel_c_abast_de_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_abast_de OWNER TO postgres;

ALTER TABLE eiel_c_abast_de ADD CONSTRAINT clavechk CHECK (eiel_c_abast_de.clave = 'DE' );

create index i_id_de on "public".eiel_c_abast_de(id);
CREATE INDEX eiel_c_abast_de_idmunicipio_key ON "public".eiel_c_abast_de(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_abast_de"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


CREATE TABLE "public".eiel_c_abast_rd
(
   id numeric(8,0) NOT NULL, 
    id_municipio numeric(5,0) NOT NULL, 
   "GEOMETRY" geometry,
   clave character varying (2) NOT NULL, 
   codprov character varying (2) NOT NULL, 
   codmunic character varying (3) NOT NULL, 
   codentidad character varying (4) NOT NULL, 
   codpoblamiento character varying (2) NOT NULL, 
   tramo_rd character varying (5), 
  titular character varying(2),
  gestor character varying(2),
  estado character varying(1),
  material character varying(2),
   longitud numeric(8,2),
   diametro integer,
   sist_trans character varying(2),
   cota_z numeric(8,0),
   obra_ejec character varying(2),
   fecha_inst date,
   observ character varying(250),
   precision_dig character varying(2),
   CONSTRAINT eiel_c_abast_rd_pkey PRIMARY KEY (id)
  
) 
WITH (
  OIDS = TRUE
)
;
ALTER TABLE "public".eiel_c_abast_rd OWNER TO postgres;
ALTER TABLE eiel_c_abast_rd ADD CONSTRAINT materialchk CHECK (eiel_c_abast_rd.material = 'FC' OR
eiel_c_abast_rd.material = 'FU' OR eiel_c_abast_rd.material = 'HO' OR eiel_c_abast_rd.material = 'OT'
OR eiel_c_abast_rd.material = 'PC' OR eiel_c_abast_rd.material = 'PE' OR eiel_c_abast_rd.material = 'PL'
OR eiel_c_abast_rd.material = 'PV');
ALTER TABLE eiel_c_abast_rd ADD CONSTRAINT estadochk CHECK (eiel_c_abast_rd.estado = 'B' OR
eiel_c_abast_rd.estado = 'E' OR eiel_c_abast_rd.estado = 'M' OR eiel_c_abast_rd.estado = 'R');
ALTER TABLE eiel_c_abast_rd ADD CONSTRAINT titularchk CHECK (eiel_c_abast_rd.titular = 'CO' OR
eiel_c_abast_rd.titular = 'EM' OR eiel_c_abast_rd.titular = 'EP' OR eiel_c_abast_rd.titular = 'MA'
OR eiel_c_abast_rd.titular = 'MU' OR eiel_c_abast_rd.titular = 'OT' OR eiel_c_abast_rd.titular = 'PV'
OR eiel_c_abast_rd.titular = 'VE');
ALTER TABLE eiel_c_abast_rd ADD CONSTRAINT gestorchk CHECK (eiel_c_abast_rd.gestor = 'CO' OR
eiel_c_abast_rd.gestor = 'EM' OR eiel_c_abast_rd.gestor = 'EP' OR eiel_c_abast_rd.gestor = 'MA'
OR eiel_c_abast_rd.gestor = 'MU' OR eiel_c_abast_rd.gestor = 'OT' OR eiel_c_abast_rd.gestor = 'PV'
OR eiel_c_abast_rd.gestor = 'VE');

create index i_id_rd on "public".eiel_c_abast_rd(id);
CREATE INDEX eiel_c_abast_rd_idmunicipio_key ON "public".eiel_c_abast_rd(id_municipio);


CREATE SEQUENCE "public"."seq_eiel_c_abast_rd"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;
  

CREATE TABLE "public".eiel_c_abast_tcn
(
   id numeric(8,0) NOT NULL, 
    id_municipio numeric(5,0) NOT NULL, 
   "GEOMETRY" geometry,
   clave character varying (2) NOT NULL, 
   codprov character varying (2) NOT NULL, 
   codmunic character varying (3) NOT NULL, 
   tramo_cn character varying (3) NOT NULL, 
   longitud numeric(8,2),
   diametro integer,
   cota_z numeric(8,0),
   obra_ejec character varying(2),
   observ character varying(100),
   precision_dig character varying(2),
   pmi numeric(8,2),
   pmf numeric(8,2),
   CONSTRAINT eiel_c_abast_tcn_pkey PRIMARY KEY (id)
) 
WITH (
  OIDS = TRUE
);

ALTER TABLE "public".eiel_c_abast_tcn OWNER TO postgres;

ALTER TABLE eiel_c_abast_tcn ADD CONSTRAINT clavechk CHECK (eiel_c_abast_tcn.clave = 'CN' );


create index i_id_tcn on "public".eiel_c_abast_tcn(id);
CREATE INDEX eiel_c_abast_tcn_idmunicipio_key ON "public".eiel_c_abast_tcn(id_municipio);
  
  CREATE SEQUENCE "public"."seq_eiel_c_abast_tcn"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;



CREATE TABLE "public".eiel_c_abast_tp
(
  id numeric(8,0) NOT NULL ,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_tp character varying (4) NOT NULL,
  cota_z numeric(8,0),
  obra_ejec character varying(2),
  observ character varying(100),
  precision_dig character varying(2),
  CONSTRAINT eiel_c_abast_tp_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_abast_tp OWNER TO postgres;

ALTER TABLE eiel_c_abast_tp ADD CONSTRAINT clavechk CHECK (eiel_c_abast_tp.clave= 'TP');

create index i_c_id_tp on "public".eiel_c_abast_tp(id);
CREATE INDEX eiel_c_abast_tp_idmunicipio_key
  ON "public".eiel_c_abast_tp(id_municipio);

  CREATE SEQUENCE "public"."seq_eiel_c_abast_tp"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_abast_cb
(
  id numeric(8,0) NOT NULL ,
   id_municipio numeric(5,0) NOT NULL ,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_cb character varying (4) NOT NULL,
  pot_motor numeric (8,0),
  estado character varying(2),
  cota_z numeric(8,0),
  obra_ejec character varying(2),
  observ character varying(250),
  precision_dig character varying(2),
  CONSTRAINT eiel_c_abast_cb_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_abast_cb OWNER TO postgres;


create index i_id_cb on "public".eiel_c_abast_cb(id);
CREATE INDEX eiel_c_abast_cb_idmunicipio_key
  ON "public".eiel_c_abast_cb(id_municipio);


---ABASTECIMIENTO T

  
CREATE TABLE "public".eiel_t_abast_tcn
(
   clave character varying (2) NOT NULL, 
   codprov character varying (2) NOT NULL, 
   codmunic character varying (3) NOT NULL, 
   tramo_cn character varying (3) NOT NULL, 
  titular character varying(2),
  gestor character varying(2),
  estado character varying(1),
  material character varying(2),
   sist_trans character varying(2),
   fecha_inst date,
  observ character varying(255),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),   
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
   CONSTRAINT eiel_t_abast_tcn_pkey PRIMARY KEY (clave, codprov, codmunic, tramo_cn, revision_actual)
) 
WITH (
  OIDS = TRUE
);
ALTER TABLE "public".eiel_t_abast_tcn OWNER TO postgres;
  
ALTER TABLE eiel_t_abast_tcn ADD CONSTRAINT clavechk CHECK (eiel_t_abast_tcn.clave = 'CN' );
ALTER TABLE eiel_t_abast_tcn ADD CONSTRAINT materialchk CHECK (eiel_t_abast_tcn.material = 'FC' OR
eiel_t_abast_tcn.material = 'FU' OR eiel_t_abast_tcn.material = 'HO' OR eiel_t_abast_tcn.material = 'OT' OR
eiel_t_abast_tcn.material = 'PC' OR eiel_t_abast_tcn.material = 'PE' OR eiel_t_abast_tcn.material = 'PV');
ALTER TABLE eiel_t_abast_tcn ADD CONSTRAINT estadochk CHECK (eiel_t_abast_tcn.estado = 'B'  OR
eiel_t_abast_tcn.estado = 'E'  OR eiel_t_abast_tcn.estado = 'M'  OR  eiel_t_abast_tcn.estado = 'R' );
ALTER TABLE eiel_t_abast_tcn ADD CONSTRAINT titularchk CHECK (eiel_t_abast_tcn.titular = 'CO' OR
eiel_t_abast_tcn.titular = 'EM' OR eiel_t_abast_tcn.titular = 'EP' OR eiel_t_abast_tcn.titular = 'MA' OR
eiel_t_abast_tcn.titular = 'MU' OR eiel_t_abast_tcn.titular = 'OT' OR eiel_t_abast_tcn.titular = 'PV' OR
eiel_t_abast_tcn.titular = 'VE');
ALTER TABLE eiel_t_abast_tcn ADD CONSTRAINT gestorchk CHECK (eiel_t_abast_tcn.gestor = 'CO' OR
eiel_t_abast_tcn.gestor = 'EM' OR eiel_t_abast_tcn.gestor = 'EP' OR eiel_t_abast_tcn.gestor = 'MA' OR
eiel_t_abast_tcn.gestor = 'MU' OR eiel_t_abast_tcn.gestor = 'OT' OR eiel_t_abast_tcn.gestor = 'PV' OR
eiel_t_abast_tcn.gestor = 'VE');
CREATE TABLE "public".eiel_t_abast_au
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL, 
  codpoblamiento character varying (2) NOT NULL, 
  aau_vivien integer,
  aau_pob_re integer,
  aau_pob_es integer,
  aau_def_vi integer,
  aau_def_re integer,
  aau_def_es integer,
  aau_fecont integer,
  aau_fencon integer,
  aau_caudal character varying(2),
  observ character varying(255),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_abast_au_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, revision_actual )
  
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_abast_au OWNER TO postgres;
ALTER TABLE eiel_t_abast_au ADD CONSTRAINT aau_caudalchk CHECK (eiel_t_abast_au.aau_caudal = 'IN' OR
eiel_t_abast_au.aau_caudal = 'NO' OR eiel_t_abast_au.aau_caudal = 'SF' );

CREATE TABLE "public".eiel_t_abast_ca
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_ca character varying (3) NOT NULL, 
  nombre character varying(60), 
  tipo character varying(2),
  titular character varying(2),
  gestor character varying(2),
   sist_impulsion character varying(2),
  estado character varying(2),
  uso character varying(2),
  proteccion character varying(2),
  contador character varying(2),
  observ character varying(256),
  fecha_revision date,
  estado_revision integer,
  fecha_inst date,
  cuenca character varying(30),
  n_expediente character varying(21),
  n_inventario character varying(21),
  cota integer,
  profundidad integer,
  max_consumo numeric (12,2),
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_abast_ca_pkey PRIMARY KEY (clave,codprov,codmunic,orden_ca, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_abast_ca OWNER TO postgres;

ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT clavechk CHECK (eiel_t_abast_ca.clave = 'CA');
ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT tipochk CHECK (eiel_t_abast_ca.tipo = 'AL' OR eiel_t_abast_ca.tipo = 'AM' OR eiel_t_abast_ca.tipo = 'AS' OR
eiel_t_abast_ca.tipo = 'CA' OR eiel_t_abast_ca.tipo = 'EB' OR eiel_t_abast_ca.tipo = 'GA' OR eiel_t_abast_ca.tipo = 'MT' OR
eiel_t_abast_ca.tipo = 'OT' OR eiel_t_abast_ca.tipo = 'PO' OR eiel_t_abast_ca.tipo = 'PX' OR  eiel_t_abast_ca.tipo = 'RI' );
ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT titularchk CHECK (eiel_t_abast_ca.titular = 'CO' OR eiel_t_abast_ca.titular = 'EM' OR
eiel_t_abast_ca.titular = 'EP' OR eiel_t_abast_ca.titular = 'MA' OR eiel_t_abast_ca.titular = 'MU' OR eiel_t_abast_ca.titular = 'OT' OR
eiel_t_abast_ca.titular = 'PV' OR eiel_t_abast_ca.titular = 'VE' );
ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT gestorchk CHECK (eiel_t_abast_ca.gestor = 'CO' OR eiel_t_abast_ca.gestor = 'EM' OR
eiel_t_abast_ca.gestor = 'EP' OR eiel_t_abast_ca.gestor = 'MA' OR eiel_t_abast_ca.gestor = 'MU' OR eiel_t_abast_ca.gestor = 'OT' OR
eiel_t_abast_ca.gestor = 'PV' OR eiel_t_abast_ca.gestor = 'VE' );
ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT sist_impulsionchk CHECK (eiel_t_abast_ca.sist_impulsion = 'GR' OR
eiel_t_abast_ca.sist_impulsion = 'IF');
ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT estadochk CHECK (eiel_t_abast_ca.estado = 'B' OR
eiel_t_abast_ca.estado = 'E' OR eiel_t_abast_ca.estado = 'M' OR eiel_t_abast_ca.estado = 'R');
ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT usochk CHECK (eiel_t_abast_ca.uso = 'UE' OR
eiel_t_abast_ca.uso = 'UO' );
ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT proteccionchk CHECK (eiel_t_abast_ca.proteccion = 'IN' OR
eiel_t_abast_ca.proteccion = 'NO'  OR eiel_t_abast_ca.proteccion = 'SF' );
ALTER TABLE eiel_t_abast_ca ADD CONSTRAINT contadorchk CHECK (eiel_t_abast_ca.contador = 'NO' OR
eiel_t_abast_ca.contador = 'SI' );

create index i_t_clave_ca on "public".eiel_t_abast_ca(clave);
create index i_codprov_ca on "public".eiel_t_abast_ca(codprov);
create index i_codmunic_ca on "public".eiel_t_abast_ca(codmunic);
create index i_orden_ca on "public".eiel_t_abast_ca (orden_ca);



CREATE TABLE "public".eiel_t_abast_de
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_de character varying (3) NOT NULL, 
  ubicacion character varying(2),
  titular character varying(2),
  gestor character varying(2),
  capacidad integer,
 estado character varying(1),
  proteccion character varying(2),
  fecha_limpieza character varying(4),
  contador character varying(2),
  fecha_inst date,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_abast_de_pkey PRIMARY KEY (clave, codprov, codmunic, orden_de, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_abast_de OWNER TO postgres;

ALTER TABLE eiel_t_abast_de ADD CONSTRAINT clavechk CHECK (eiel_t_abast_de.clave = 'DE' );
ALTER TABLE eiel_t_abast_de ADD CONSTRAINT ubicacionchk CHECK (eiel_t_abast_de.ubicacion = 'EL'  OR
eiel_t_abast_de.ubicacion = 'EN'  OR eiel_t_abast_de.ubicacion = 'ES'  OR
eiel_t_abast_de.ubicacion = 'OT'  OR eiel_t_abast_de.ubicacion = 'SE');
ALTER TABLE eiel_t_abast_de ADD CONSTRAINT titularchk CHECK (eiel_t_abast_de.titular = 'CO' OR
eiel_t_abast_de.titular = 'EM' OR eiel_t_abast_de.titular = 'EP' OR eiel_t_abast_de.titular = 'MA' OR
eiel_t_abast_de.titular = 'MU' OR eiel_t_abast_de.titular = 'OT' OR eiel_t_abast_de.titular = 'PV' OR
eiel_t_abast_de.titular = 'VE');
ALTER TABLE eiel_t_abast_de ADD CONSTRAINT gestorchk CHECK (eiel_t_abast_de.gestor = 'CO' OR
eiel_t_abast_de.gestor = 'EM' OR eiel_t_abast_de.gestor = 'EP' OR eiel_t_abast_de.gestor = 'MA' OR
eiel_t_abast_de.gestor = 'MU' OR eiel_t_abast_de.gestor = 'OT' OR eiel_t_abast_de.gestor = 'PV' OR
eiel_t_abast_de.gestor = 'VE');
ALTER TABLE eiel_t_abast_de ADD CONSTRAINT estadochk CHECK (eiel_t_abast_de.estado = 'B' OR
eiel_t_abast_de.estado = 'E' OR eiel_t_abast_de.estado = 'M' OR eiel_t_abast_de.estado = 'R');
ALTER TABLE eiel_t_abast_de ADD CONSTRAINT proteccionchk CHECK (eiel_t_abast_de.proteccion = 'IN' OR
eiel_t_abast_de.proteccion = 'NO' OR eiel_t_abast_de.proteccion = 'SF' );
ALTER TABLE eiel_t_abast_de ADD CONSTRAINT contadorchk CHECK (eiel_t_abast_de.contador = 'AM' OR
eiel_t_abast_de.contador = 'EN' OR eiel_t_abast_de.contador = 'NO' OR eiel_t_abast_de.contador = 'SA');



CREATE TABLE "public".eiel_t_abast_serv
(
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL, 
  codpoblamiento character varying (2) NOT NULL, 
  viviendas_c_conex integer,
  viviendas_s_conexion integer,
  consumo_inv integer,
  consumo_verano integer,
  viv_exceso_pres integer,
  viv_defic_presion integer,
  perdidas_agua integer,
  calidad_serv character varying(2),
  long_deficit integer,
  viv_deficitarias integer,
  pobl_res_afect integer,
  pobl_est_afect integer,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_abast_serv_pkey PRIMARY KEY  (codprov, codmunic, codentidad, codpoblamiento, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_abast_serv OWNER TO postgres;

ALTER TABLE eiel_t_abast_serv ADD CONSTRAINT calidad_servchk CHECK (eiel_t_abast_serv.calidad_serv = 'B' OR
eiel_t_abast_serv.calidad_serv = 'E' OR eiel_t_abast_serv.calidad_serv = 'M' OR eiel_t_abast_serv.calidad_serv = 'NO'
 OR eiel_t_abast_serv.calidad_serv = 'R');

create index i_codprov_serv on "public".eiel_t_abast_serv(codprov);
create index i_codmunic_serv on "public".eiel_t_abast_serv(codmunic);
create index i_codentidad_serv on "public".eiel_t_abast_serv (codentidad);
create index i_codpoblamiento_serv on "public".eiel_t_abast_serv (codpoblamiento);


CREATE TABLE "public".eiel_t_abast_tp
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_tp character varying (3) NOT NULL, 
  tipo character varying(2),
  ubicacion character varying(2),
  s_desinf character varying(2),
  categoria_a1 character varying(2),
  categoria_a2 character varying(2),
  categoria_a3 character varying(2),
  desaladora character varying(2),
  otros character varying(2),
  desinf_1 character varying(2),
  desinf_2 character varying(2),
  desinf_3 character varying(2),
  periodicidad character varying(2),
  organismo_control character varying(2),
  estado character varying(1),
  fecha_inst date,
  fecha_revision date,
  estado_revision integer,
  observ character varying(256),
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_abast_tp_pkey PRIMARY KEY (clave, codprov, codmunic, orden_tp, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_abast_tp OWNER TO postgres;
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT clavechk CHECK (eiel_t_abast_tp.clave = 'TP' );
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT tipochk CHECK (eiel_t_abast_tp.tipo = 'AU' OR eiel_t_abast_tp.tipo = 'MA');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT ubicacionchk CHECK (eiel_t_abast_tp.ubicacion = 'CA' OR eiel_t_abast_tp.ubicacion = 'CD' OR
eiel_t_abast_tp.ubicacion = 'DE' OR eiel_t_abast_tp.ubicacion = 'OT' OR eiel_t_abast_tp.ubicacion = 'RD');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT s_desinfchk CHECK (eiel_t_abast_tp.s_desinf = 'SI' OR eiel_t_abast_tp.s_desinf = 'NO');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT categoria_a1chk CHECK (eiel_t_abast_tp.categoria_a1 = 'SI' OR eiel_t_abast_tp.categoria_a1 = 'NO');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT categoria_a2chk CHECK (eiel_t_abast_tp.categoria_a2 = 'SI' OR eiel_t_abast_tp.categoria_a2 = 'NO');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT categoria_a3chk CHECK (eiel_t_abast_tp.categoria_a3 = 'SI' OR eiel_t_abast_tp.categoria_a3 = 'NO');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT desaladorachk CHECK (eiel_t_abast_tp.desaladora = 'SI' OR eiel_t_abast_tp.desaladora = 'NO');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT otroschk CHECK (eiel_t_abast_tp.otros = 'SI' OR eiel_t_abast_tp.otros = 'NO');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT desinf_1chk CHECK (eiel_t_abast_tp.desinf_1 = 'CG' OR eiel_t_abast_tp.desinf_1 = 'CL' OR
eiel_t_abast_tp.desinf_1 = 'DC' OR eiel_t_abast_tp.desinf_1 = 'HP' OR eiel_t_abast_tp.desinf_1 = 'MC'
OR eiel_t_abast_tp.desinf_1 = 'NF' OR eiel_t_abast_tp.desinf_1 = 'OS'  OR eiel_t_abast_tp.desinf_1 = 'OT'
OR eiel_t_abast_tp.desinf_1 = 'OZ'  OR eiel_t_abast_tp.desinf_1 = 'UF'  OR eiel_t_abast_tp.desinf_1 = 'UL'
OR eiel_t_abast_tp.desinf_1 = 'NO');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT desinf_2chk CHECK (eiel_t_abast_tp.desinf_2 = 'CG' OR eiel_t_abast_tp.desinf_2 = 'CL' OR
eiel_t_abast_tp.desinf_2 = 'DC' OR eiel_t_abast_tp.desinf_2 = 'HP' OR eiel_t_abast_tp.desinf_2 = 'MC'
OR eiel_t_abast_tp.desinf_2 = 'NF' OR eiel_t_abast_tp.desinf_2 = 'OS'  OR eiel_t_abast_tp.desinf_2 = 'OT'
OR eiel_t_abast_tp.desinf_2 = 'OZ'  OR eiel_t_abast_tp.desinf_2 = 'UF'  OR eiel_t_abast_tp.desinf_2 = 'UL'
OR eiel_t_abast_tp.desinf_2 = 'NO');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT desinf_3chk CHECK (eiel_t_abast_tp.desinf_3 = 'CG' OR eiel_t_abast_tp.desinf_3 = 'CL' OR
eiel_t_abast_tp.desinf_3 = 'DC' OR eiel_t_abast_tp.desinf_3 = 'HP' OR eiel_t_abast_tp.desinf_3 = 'MC'
OR eiel_t_abast_tp.desinf_3 = 'NF' OR eiel_t_abast_tp.desinf_3 = 'OS'  OR eiel_t_abast_tp.desinf_3 = 'OT'
OR eiel_t_abast_tp.desinf_3 = 'OZ'  OR eiel_t_abast_tp.desinf_3 = 'UF'  OR eiel_t_abast_tp.desinf_3 = 'UL'
OR eiel_t_abast_tp.desinf_3 = 'NO');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT periodicidadchk CHECK (eiel_t_abast_tp.periodicidad = 'AL' OR
eiel_t_abast_tp.periodicidad = 'DI' OR eiel_t_abast_tp.periodicidad = 'ME' OR eiel_t_abast_tp.periodicidad = 'NO'
OR eiel_t_abast_tp.periodicidad = 'OT' OR eiel_t_abast_tp.periodicidad = 'QU'  OR eiel_t_abast_tp.periodicidad = 'SE');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT organismo_controlchk CHECK (eiel_t_abast_tp.organismo_control = 'CA' OR
eiel_t_abast_tp.organismo_control = 'MA' OR eiel_t_abast_tp.organismo_control = 'MU' OR eiel_t_abast_tp.organismo_control = 'NO'
OR eiel_t_abast_tp.organismo_control = 'OT' OR eiel_t_abast_tp.organismo_control = 'PR');
ALTER TABLE eiel_t_abast_tp ADD CONSTRAINT estadochk CHECK (eiel_t_abast_tp.estado = 'B' OR
eiel_t_abast_tp.estado = 'E' OR eiel_t_abast_tp.estado = 'M' OR eiel_t_abast_tp.estado = 'R');

create index i_t_clave_tp on "public".eiel_t_abast_tp(clave);
create index i_t_codprov_tp on "public".eiel_t_abast_tp(codprov);
create index i_t_codmunic_tp on "public".eiel_t_abast_tp(codmunic);
create index i_t_orden_tp on "public".eiel_t_abast_tp (orden_tp);



---ABASTECIMIENTO TR


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
ALTER TABLE eiel_tr_abast_ca_pobl ADD CONSTRAINT clavechk CHECK (eiel_tr_abast_ca_pobl.clave_ca = 'CA');



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

ALTER TABLE eiel_tr_abast_de_pobl ADD CONSTRAINT clavechk CHECK (eiel_tr_abast_de_pobl.clave_de = 'DE' );

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
ALTER TABLE eiel_tr_abast_tp_pobl ADD CONSTRAINT clavechk CHECK (eiel_tr_abast_tp_pobl.clave_tp = 'TP' );


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
ALTER TABLE eiel_tr_abast_tcn_pobl ADD CONSTRAINT clavechk CHECK (eiel_tr_abast_tcn_pobl.clave_tcn = 'CN' );


create index i_clave_tcn_pobl on "public".eiel_tr_abast_tcn_pobl(clave_tcn);
create index i_codprov_tcn_pobl on "public".eiel_tr_abast_tcn_pobl(codprov_tcn);
create index i_codmunic_tcn_pobl on "public".eiel_tr_abast_tcn_pobl(codmunic_tcn);
create index i_tramo_tcn_pobl on "public".eiel_tr_abast_tcn_pobl(tramo_tcn);




-----SANEAMIENTO C-----------

CREATE TABLE "public".eiel_c_saneam_ed
(
  id numeric(8,0) NOT NULL NOT NULL,
   id_municipio numeric(5,0) NOT NULL NOT NULL,
  "GEOMETRY" geometry,
  clave character varying(2),
  codprov character varying(2),
  codmunic character varying(3),
  orden_ed character varying(3),
  observ character varying(100),
  precision_dig character varying(2),
  CONSTRAINT eiel_c_saneam_ed_pkey PRIMARY KEY (id),
  CONSTRAINT saneam_ed_id_key UNIQUE (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_c_saneam_ed OWNER TO postgres;
ALTER TABLE eiel_c_saneam_ed ADD CONSTRAINT clavechk CHECK (eiel_c_saneam_ed.clave= 'ED');
CREATE INDEX eiel_c_saneam_ed_idmunicipio_key ON "public".eiel_c_saneam_ed(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_saneam_ed"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_saneam_tem
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  tramo_em character varying (3) NOT NULL,
  longitud numeric(8,2),
  long_terre numeric(8,2),
  long_marit numeric(8,2),
  diametro integer,
  cota_z numeric(8,0),
  obra_ejec character varying(2),
  observ character varying(100),
  pmi numeric(8,2),
  pmf numeric (8,2),
  precision_dig character varying(2),
  CONSTRAINT eiel_c_saneam_tem_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_saneam_tem OWNER TO postgres;
ALTER TABLE eiel_c_saneam_tem ADD CONSTRAINT clavechk CHECK (eiel_c_saneam_tem.clave= 'EM');

create index i_id_tem on "public".eiel_c_saneam_tem(id);
create index i_id_mun_tem on "public".eiel_c_saneam_tem(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_saneam_tem"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;
    


CREATE TABLE "public".eiel_c_saneam_pv
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_pv character varying (4) NOT NULL,
  cota_z numeric(8,0),
  obra_ejec character varying(2),
  precision_dig character varying(2),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_c_saneam_pv_pkey PRIMARY KEY (id , revision_actual )
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_saneam_pv OWNER TO postgres;

create index i_id_pv on "public".eiel_c_saneam_pv(id_municipio);


CREATE SEQUENCE "public"."seq_eiel_c_saneam_pv"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


CREATE TABLE "public".eiel_c_saneam_tcl
(
   id numeric(8,0) NOT NULL, 
    id_municipio numeric(5,0) NOT NULL, 
   "GEOMETRY" geometry,
   clave character varying (3) NOT NULL, 
   codprov character varying (2) NOT NULL, 
   codmunic character varying (3) NOT NULL, 
   tramo_cl character varying (3) NOT NULL, 
   longitud numeric(8,2),
   diametro integer,
   cota_z numeric(8,0),
   obra_ejec character varying(2),
   observ character varying(250),
   pmi  numeric(8,2),
   pmf numeric(8,2),
   precision_dig character varying(2),
   CONSTRAINT eiel_c_saneam_tcl_pkey PRIMARY KEY (id)
) 
WITH (
  OIDS = TRUE
)
;
ALTER TABLE "public".eiel_c_saneam_tcl OWNER TO postgres;
ALTER TABLE eiel_c_saneam_tcl ADD CONSTRAINT clavechk CHECK (eiel_c_saneam_tcl.clave = 'CL');

create index i_id_tcl on "public".eiel_c_saneam_tcl(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_saneam_tcl"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;
    

CREATE TABLE "public".eiel_c_saneam_pr
(
   id numeric(8,0) NOT NULL, 
    id_municipio numeric(5,0) NOT NULL, 
   "GEOMETRY" geometry,
   clave character varying (2) NOT NULL, 
   codprov character varying (2) NOT NULL, 
   codmunic character varying (3) NOT NULL,
   codentidad character varying(4) NOT NULL,
   codpoblamiento character varying(2) NOT NULL,
   orden_pr character varying (4) NOT NULL, 
   cota_z numeric(8,0),
   obra_ejec character varying(2),
   estado character varying(1),
   precision_dig character varying(2),
   fecha_inst date,
   CONSTRAINT eiel_c_saneam_pr_pkey PRIMARY KEY (id)

) 
WITH (
  OIDS = TRUE
)
;
ALTER TABLE "public".eiel_c_saneam_pr OWNER TO postgres;

create index i_c_id_pr on "public".eiel_c_saneam_pr(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_saneam_pr"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_saneam_rs
(
   id numeric(8,0) NOT NULL, 
    id_municipio numeric(5,0) NOT NULL, 
   "GEOMETRY" geometry,
   clave character varying (2) NOT NULL, 
   codprov character varying (2) NOT NULL, 
   codmunic character varying (3) NOT NULL, 
   codentidad character varying (4) NOT NULL,
   codpoblamiento character varying (2) NOT NULL,
   tramo_rs character varying (10), 
  titular character varying(2),
  gestor character varying(2),
  estado character varying(1),
  material character varying(2),
  sist_impulsion character varying(2),
  tipo_red_interior character varying(2),
   longitud numeric(8,2),
   diametro integer,
   cota_z numeric(8,0),
   obra_ejec character varying(2),
   fecha_inst date,
   observ character varying(250),
   precision_dig character varying(2),
   CONSTRAINT eiel_c_saneam_rs_pkey PRIMARY KEY (id)
) 
WITH (
  OIDS = TRUE
)
;
ALTER TABLE "public".eiel_c_saneam_rs OWNER TO postgres;

ALTER TABLE eiel_c_saneam_rs ADD CONSTRAINT materialchk CHECK (eiel_c_saneam_rs.material = 'FC' OR
eiel_c_saneam_rs.material = 'FU' OR eiel_c_saneam_rs.material = 'HO' OR
eiel_c_saneam_rs.material = 'OT' OR eiel_c_saneam_rs.material = 'PC' OR
eiel_c_saneam_rs.material = 'PE' OR eiel_c_saneam_rs.material = 'PV' );
ALTER TABLE eiel_c_saneam_rs ADD CONSTRAINT sist_impulsionchk CHECK (eiel_c_saneam_rs.sist_impulsion = 'GR' OR
eiel_c_saneam_rs.sist_impulsion = 'IM');
ALTER TABLE eiel_c_saneam_rs ADD CONSTRAINT estadochk CHECK (eiel_c_saneam_rs.estado = 'B' OR
eiel_c_saneam_rs.estado = 'E' OR eiel_c_saneam_rs.estado = 'M' OR
eiel_c_saneam_rs.estado = 'R');
ALTER TABLE eiel_c_saneam_rs ADD CONSTRAINT tipo_red_interiorchk CHECK (eiel_c_saneam_rs.tipo_red_interior = 'MX' OR
eiel_c_saneam_rs.tipo_red_interior = 'PL' OR eiel_c_saneam_rs.tipo_red_interior = 'RE');
ALTER TABLE eiel_c_saneam_rs ADD CONSTRAINT titularchk CHECK (eiel_c_saneam_rs.titular = 'CO' OR
eiel_c_saneam_rs.titular = 'EM' OR eiel_c_saneam_rs.titular = 'EP' OR
eiel_c_saneam_rs.titular = 'MA' OR eiel_c_saneam_rs.titular = 'MU' OR
eiel_c_saneam_rs.titular = 'OT' OR eiel_c_saneam_rs.titular = 'PV' OR eiel_c_saneam_rs.titular = 'VE' );
ALTER TABLE eiel_c_saneam_rs ADD CONSTRAINT gestorchk CHECK (eiel_c_saneam_rs.gestor = 'CO' OR
eiel_c_saneam_rs.gestor = 'EM' OR eiel_c_saneam_rs.gestor = 'EP' OR
eiel_c_saneam_rs.gestor = 'MA' OR eiel_c_saneam_rs.gestor = 'MU' OR
eiel_c_saneam_rs.gestor = 'OT' OR eiel_c_saneam_rs.gestor = 'PV' OR eiel_c_saneam_rs.gestor = 'VE' );

create index i_id_rs on "public".eiel_c_saneam_rs(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_saneam_rs"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_saneam_cb
(
  id numeric(8,0) NOT NULL, 
   id_municipio numeric(5,0) NOT NULL, 
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL, 
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  orden_cb character varying (4) NOT NULL,
  pot_motor numeric (8,0),
  estado character varying (2) ,
  cota_z numeric(8,0),
  obra_ejec character varying (2) ,
  observ character varying(250),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_saneam_cb_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_saneam_cb OWNER TO postgres;

create index i_c_id_cb on "public".eiel_c_saneam_cb(id_municipio);



CREATE SEQUENCE "public"."seq_eiel_c_saneam_ali"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_saneam_ali
(
  id numeric(8,0) NOT NULL, 
   id_municipio numeric(5,0) NOT NULL, 
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL, 
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  acumulacion character varying (3) NOT NULL,
  pot_motor numeric (8,0),
  estado character varying (2) ,
  cota_z numeric(8,0),
  obra_ejec character varying (2) ,
  observ character varying(250),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_saneam_ali_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_saneam_ali OWNER TO postgres;


create index i_id_ali on "public".eiel_c_saneam_ali(id_municipio);

------SANEAMIENTO T
CREATE SEQUENCE seq_eiel_t_saneam_tem_id_eiel
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_eiel_t_saneam_tem_id_eiel
  OWNER TO postgres;


CREATE TABLE eiel_t_saneam_tem
(
  clave character varying(2) NOT NULL,
  codprov character varying(2) NOT NULL,
  codmunic character varying(3) NOT NULL,
  tramo_em character varying(3) NOT NULL,
  titular character varying(2),
  gestor character varying(2),
  estado character varying(1),
  material character varying(2),
  sist_impulsion character varying(2),
  tipo_red_interior character varying(2),
  longitud numeric(8,2),
  long_terre numeric(8,2),
  long_marit numeric(8,2),
  diametro integer,
  cota_z numeric(8,0),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(100),
  pmi numeric(8,2),
  pmf numeric(8,2),
  precision_dig character varying(2),
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  id_eiel integer NOT NULL DEFAULT nextval('seq_eiel_t_saneam_tem_id_eiel'::regclass),
  fecha_revision date,
  estado_revision integer,
  CONSTRAINT eiel_t_saneam_tem_pkey PRIMARY KEY (clave , codprov , codmunic , tramo_em , revision_actual ),
  CONSTRAINT clavechk CHECK (clave::text = 'EM'::text),
  CONSTRAINT estadochk CHECK (estado::text = 'B'::text OR estado::text = 'E'::text OR estado::text = 'M'::text OR estado::text = 'R'::text),
  CONSTRAINT materialchk CHECK (material::text = 'FC'::text OR material::text = 'FU'::text OR material::text = 'HO'::text OR material::text = 'OT'::text OR material::text = 'PC'::text OR material::text = 'PE'::text OR material::text = 'PV'::text)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_t_saneam_tem
  OWNER TO postgres;
GRANT ALL ON TABLE eiel_t_saneam_tem TO postgres;
GRANT SELECT ON TABLE eiel_t_saneam_tem TO consultas;

-- Index: i_codmunic_tem

-- DROP INDEX i_codmunic_tem;

CREATE INDEX i_codmunic_tem
  ON eiel_t_saneam_tem
  USING btree
  (codmunic );

-- Index: i_codprov_tem

-- DROP INDEX i_codprov_tem;

CREATE INDEX i_codprov_tem
  ON eiel_t_saneam_tem
  USING btree
  (codprov );

-- Index: i_t_clave_em

-- DROP INDEX i_t_clave_em;

CREATE INDEX i_t_clave_em
  ON eiel_t_saneam_tem
  USING btree
  (clave );

-- Index: i_t_tramo_em

-- DROP INDEX i_t_tramo_em;

CREATE INDEX i_t_tramo_em
  ON eiel_t_saneam_tem
  USING btree
  (tramo_em );





CREATE TABLE "public".eiel_t_saneam_tcl
(
 clave character varying (3) NOT NULL, 
 codprov character varying (2) NOT NULL, 
 codmunic character varying (3) NOT NULL, 
 tramo_cl character varying (3) NOT NULL, 
 titular character varying(2),
 gestor character varying(2),
 estado character varying(1),
 material character varying(2),
 sist_impulsion character varying(2),
 tipo_red_interior character varying(2),
 tip_interceptor character varying(2),
 fecha_inst date,
 observ character varying(250),
 fecha_revision date,
 estado_revision integer,
 bloqueado character varying(32),
 revision_actual numeric(10,0) NOT NULL DEFAULT 0,
 revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
CONSTRAINT eiel_t_saneam_tcl_pkey PRIMARY KEY (clave,codprov,codmunic,tramo_cl, revision_actual)
) 
WITH (
  OIDS = TRUE
)
;
ALTER TABLE "public".eiel_t_saneam_tcl OWNER TO postgres;

ALTER TABLE eiel_t_saneam_tcl ADD CONSTRAINT clavechk CHECK (eiel_t_saneam_tcl.clave = 'CL');
ALTER TABLE eiel_t_saneam_tcl ADD CONSTRAINT materialchk CHECK (eiel_t_saneam_tcl.material = 'FC' OR
eiel_t_saneam_tcl.material = 'FU' OR eiel_t_saneam_tcl.material = 'HO' OR eiel_t_saneam_tcl.material = 'OT' OR
eiel_t_saneam_tcl.material = 'PC' OR eiel_t_saneam_tcl.material = 'PE' OR eiel_t_saneam_tcl.material = 'PV');
ALTER TABLE eiel_t_saneam_tcl ADD CONSTRAINT sist_impulsionchk CHECK (eiel_t_saneam_tcl.sist_impulsion = 'GR' OR
eiel_t_saneam_tcl.sist_impulsion = 'IM');
ALTER TABLE eiel_t_saneam_tcl ADD CONSTRAINT estadonchk CHECK (eiel_t_saneam_tcl.estado = 'B' OR
eiel_t_saneam_tcl.estado = 'E' OR eiel_t_saneam_tcl.estado = 'M' OR
eiel_t_saneam_tcl.estado = 'R');
ALTER TABLE eiel_t_saneam_tcl ADD CONSTRAINT titularchk CHECK (eiel_t_saneam_tcl.titular = 'CO' OR
eiel_t_saneam_tcl.titular = 'EM' OR eiel_t_saneam_tcl.titular = 'EP' OR eiel_t_saneam_tcl.titular = 'MA' OR
eiel_t_saneam_tcl.titular = 'MU' OR eiel_t_saneam_tcl.titular = 'OT' OR eiel_t_saneam_tcl.titular = 'PV' OR
eiel_t_saneam_tcl.titular = 'VE');
ALTER TABLE eiel_t_saneam_tcl ADD CONSTRAINT gestorchk CHECK (eiel_t_saneam_tcl.gestor = 'CO' OR
eiel_t_saneam_tcl.gestor = 'EM' OR eiel_t_saneam_tcl.gestor = 'EP' OR eiel_t_saneam_tcl.gestor = 'MA' OR
eiel_t_saneam_tcl.gestor = 'MU' OR eiel_t_saneam_tcl.gestor = 'OT' OR eiel_t_saneam_tcl.gestor = 'PV' OR
eiel_t_saneam_tcl.gestor = 'VE');

CREATE TABLE "public".eiel_t1_saneam_ed
(
  clave character varying (2) NOT NULL, 
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  orden_ed character varying (4) NOT NULL,
	trat_pr_1 character varying(2),
  trat_pr_2 character varying(2),
  trat_pr_3 character varying(2),
  trat_sc_1 character varying(2),
  trat_sc_2 character varying(2),
  trat_sc_3 character varying(2),
  trat_av_1 character varying(2),
  trat_av_2 character varying(2),
  trat_av_3 character varying(2),
  proc_cm_1 character varying(2),
  proc_cm_2 character varying(2),
  proc_cm_3 character varying(2),
  trat_ld_1 character varying(2),
  trat_ld_2 character varying(2),
  trat_ld_3 character varying(2),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t1_saneam_ed_pkey PRIMARY KEY (clave, codprov, codmunic, orden_ed, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t1_saneam_ed OWNER TO postgres;

ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT clavechk CHECK (eiel_t1_saneam_ed.clave = 'ED');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_pr_1chk CHECK (eiel_t1_saneam_ed.trat_pr_1 = 'DC' OR
eiel_t1_saneam_ed.trat_pr_1 = 'DG' OR eiel_t1_saneam_ed.trat_pr_1 = 'FQ' OR
eiel_t1_saneam_ed.trat_pr_1 = 'FS' OR eiel_t1_saneam_ed.trat_pr_1 = 'LN' OR
eiel_t1_saneam_ed.trat_pr_1 = 'OT' OR eiel_t1_saneam_ed.trat_pr_1 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_pr_2chk CHECK (eiel_t1_saneam_ed.trat_pr_2 = 'DC' OR
eiel_t1_saneam_ed.trat_pr_2 = 'DG' OR eiel_t1_saneam_ed.trat_pr_2 = 'FQ' OR
eiel_t1_saneam_ed.trat_pr_2 = 'FS' OR eiel_t1_saneam_ed.trat_pr_2 = 'LN' OR
eiel_t1_saneam_ed.trat_pr_2 = 'OT' OR eiel_t1_saneam_ed.trat_pr_2 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_pr_3chk CHECK (eiel_t1_saneam_ed.trat_pr_3 = 'DC' OR
eiel_t1_saneam_ed.trat_pr_3 = 'DG' OR eiel_t1_saneam_ed.trat_pr_3 = 'FQ' OR
eiel_t1_saneam_ed.trat_pr_3 = 'FS' OR eiel_t1_saneam_ed.trat_pr_3 = 'LN' OR
eiel_t1_saneam_ed.trat_pr_3 = 'OT' OR eiel_t1_saneam_ed.trat_pr_3 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_sc_1chk CHECK (eiel_t1_saneam_ed.trat_sc_1 = 'CB' OR
eiel_t1_saneam_ed.trat_sc_1 = 'ES' OR eiel_t1_saneam_ed.trat_sc_1 = 'FA' OR
eiel_t1_saneam_ed.trat_sc_1 = 'FV' OR eiel_t1_saneam_ed.trat_sc_1 = 'IR' OR
eiel_t1_saneam_ed.trat_sc_1 = 'LA' OR eiel_t1_saneam_ed.trat_sc_1 = 'LB' OR
eiel_t1_saneam_ed.trat_sc_1 = 'LT' OR eiel_t1_saneam_ed.trat_sc_1 = 'OT' OR
eiel_t1_saneam_ed.trat_sc_1 = 'PE' OR eiel_t1_saneam_ed.trat_sc_1 = 'ZF' OR eiel_t1_saneam_ed.trat_sc_1 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_sc_2chk CHECK (eiel_t1_saneam_ed.trat_sc_2 = 'CB' OR
eiel_t1_saneam_ed.trat_sc_2 = 'ES' OR eiel_t1_saneam_ed.trat_sc_2 = 'FA' OR
eiel_t1_saneam_ed.trat_sc_2 = 'FV' OR eiel_t1_saneam_ed.trat_sc_2 = 'IR' OR
eiel_t1_saneam_ed.trat_sc_2 = 'LA' OR eiel_t1_saneam_ed.trat_sc_2 = 'LB' OR
eiel_t1_saneam_ed.trat_sc_2 = 'LT' OR eiel_t1_saneam_ed.trat_sc_2 = 'OT' OR
eiel_t1_saneam_ed.trat_sc_2 = 'PE' OR eiel_t1_saneam_ed.trat_sc_2 = 'ZF' OR eiel_t1_saneam_ed.trat_sc_2 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_sc_3chk CHECK (eiel_t1_saneam_ed.trat_sc_3 = 'CB' OR
eiel_t1_saneam_ed.trat_sc_3 = 'ES' OR eiel_t1_saneam_ed.trat_sc_3 = 'FA' OR
eiel_t1_saneam_ed.trat_sc_3 = 'FV' OR eiel_t1_saneam_ed.trat_sc_3 = 'IR' OR
eiel_t1_saneam_ed.trat_sc_3 = 'LA' OR eiel_t1_saneam_ed.trat_sc_3 = 'LB' OR
eiel_t1_saneam_ed.trat_sc_3 = 'LT' OR eiel_t1_saneam_ed.trat_sc_3 = 'OT' OR
eiel_t1_saneam_ed.trat_sc_3 = 'PE' OR eiel_t1_saneam_ed.trat_sc_3 = 'ZF' OR eiel_t1_saneam_ed.trat_sc_3 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_av_1chk CHECK (eiel_t1_saneam_ed.trat_av_1 = 'CL' OR
eiel_t1_saneam_ed.trat_av_1 = 'FA' OR eiel_t1_saneam_ed.trat_av_1 = 'FM' OR
eiel_t1_saneam_ed.trat_av_1 = 'OT' OR eiel_t1_saneam_ed.trat_av_1 = 'OZ' OR
eiel_t1_saneam_ed.trat_av_1 = 'UN' OR eiel_t1_saneam_ed.trat_av_1 = 'UT' OR
eiel_t1_saneam_ed.trat_av_1 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_av_2chk CHECK (eiel_t1_saneam_ed.trat_av_2 = 'CL' OR
eiel_t1_saneam_ed.trat_av_2 = 'FA' OR eiel_t1_saneam_ed.trat_av_2 = 'FM' OR
eiel_t1_saneam_ed.trat_av_2 = 'OT' OR eiel_t1_saneam_ed.trat_av_2 = 'OZ' OR
eiel_t1_saneam_ed.trat_av_2 = 'UN' OR eiel_t1_saneam_ed.trat_av_2 = 'UT' OR
eiel_t1_saneam_ed.trat_av_2 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_av_3chk CHECK (eiel_t1_saneam_ed.trat_av_3 = 'CL' OR
eiel_t1_saneam_ed.trat_av_3 = 'FA' OR eiel_t1_saneam_ed.trat_av_3 = 'FM' OR
eiel_t1_saneam_ed.trat_av_3 = 'OT' OR eiel_t1_saneam_ed.trat_av_3 = 'OZ' OR
eiel_t1_saneam_ed.trat_av_3 = 'UN' OR eiel_t1_saneam_ed.trat_av_3 = 'UT' OR
eiel_t1_saneam_ed.trat_av_3 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT proc_cm_1chk CHECK (eiel_t1_saneam_ed.proc_cm_1 = 'CG' OR
eiel_t1_saneam_ed.proc_cm_1 = 'DS' OR eiel_t1_saneam_ed.proc_cm_1 = 'OT' OR
eiel_t1_saneam_ed.proc_cm_1 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT proc_cm_2chk CHECK (eiel_t1_saneam_ed.proc_cm_2 = 'CG' OR
eiel_t1_saneam_ed.proc_cm_2 = 'DS' OR eiel_t1_saneam_ed.proc_cm_2 = 'OT' OR
eiel_t1_saneam_ed.proc_cm_2 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT proc_cm_3chk CHECK (eiel_t1_saneam_ed.proc_cm_3 = 'CG' OR
eiel_t1_saneam_ed.proc_cm_3 = 'DS' OR eiel_t1_saneam_ed.proc_cm_3 = 'OT' OR
eiel_t1_saneam_ed.proc_cm_3 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_ld_1chk CHECK (eiel_t1_saneam_ed.trat_ld_1 = 'CO' OR
eiel_t1_saneam_ed.trat_ld_1 = 'DA' OR eiel_t1_saneam_ed.trat_ld_1 = 'DN' OR
eiel_t1_saneam_ed.trat_ld_1 = 'EC' OR eiel_t1_saneam_ed.trat_ld_1 = 'OT' OR
eiel_t1_saneam_ed.trat_ld_1 = 'TT' OR eiel_t1_saneam_ed.trat_ld_1 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_ld_2chk CHECK (eiel_t1_saneam_ed.trat_ld_2 = 'CO' OR
eiel_t1_saneam_ed.trat_ld_2 = 'DA' OR eiel_t1_saneam_ed.trat_ld_2 = 'DN' OR
eiel_t1_saneam_ed.trat_ld_2 = 'EC' OR eiel_t1_saneam_ed.trat_ld_2 = 'OT' OR
eiel_t1_saneam_ed.trat_ld_2 = 'TT' OR eiel_t1_saneam_ed.trat_ld_2 = 'NO');
ALTER TABLE eiel_t1_saneam_ed ADD CONSTRAINT trat_ld_3chk CHECK (eiel_t1_saneam_ed.trat_ld_3 = 'CO' OR
eiel_t1_saneam_ed.trat_ld_3 = 'DA' OR eiel_t1_saneam_ed.trat_ld_3 = 'DN' OR
eiel_t1_saneam_ed.trat_ld_3 = 'EC' OR eiel_t1_saneam_ed.trat_ld_3 = 'OT' OR
eiel_t1_saneam_ed.trat_ld_3 = 'TT' OR eiel_t1_saneam_ed.trat_ld_3 = 'NO');


CREATE TABLE "public".eiel_t2_saneam_ed
(
  clave character varying (2) NOT NULL, 
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  orden_ed character varying (4) NOT NULL,
  titular character varying(2),
  gestor character varying(2),
  capacidad integer,
 problem_1 character varying(2),
  problem_2 character varying(2),
  problem_3 character varying(2),
  lodo_gest character varying(2),
  lodo_vert integer, -- porcentaje
  lodo_inci integer, -- porcentaje
  lodo_con_agri integer, -- porcentaje
  lodo_sin_agri integer, -- porcentaje
  lodo_ot integer, -- porcentaje
  fecha_inst date,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t2_saneam_ed_pkey PRIMARY KEY (clave, codprov, codmunic, orden_ed, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t2_saneam_ed OWNER TO postgres;

ALTER TABLE eiel_t2_saneam_ed ADD CONSTRAINT clavechk CHECK (eiel_t2_saneam_ed.clave = 'ED');
ALTER TABLE eiel_t2_saneam_ed ADD CONSTRAINT titularchk CHECK (eiel_t2_saneam_ed.titular = 'CO' OR
eiel_t2_saneam_ed.titular = 'EM' OR eiel_t2_saneam_ed.titular = 'EP' OR
eiel_t2_saneam_ed.titular = 'MA' OR eiel_t2_saneam_ed.titular = 'MU' OR
eiel_t2_saneam_ed.titular = 'OT' OR eiel_t2_saneam_ed.titular = 'PV' OR eiel_t2_saneam_ed.titular = 'VE');
ALTER TABLE eiel_t2_saneam_ed ADD CONSTRAINT gestorchk CHECK (eiel_t2_saneam_ed.gestor = 'CO' OR
eiel_t2_saneam_ed.gestor = 'EM' OR eiel_t2_saneam_ed.gestor = 'EP' OR
eiel_t2_saneam_ed.gestor = 'MA' OR eiel_t2_saneam_ed.gestor = 'MU' OR
eiel_t2_saneam_ed.gestor = 'OT' OR eiel_t2_saneam_ed.gestor = 'PV' OR eiel_t2_saneam_ed.gestor = 'VE');
ALTER TABLE eiel_t2_saneam_ed ADD CONSTRAINT problem_1chk CHECK (eiel_t2_saneam_ed.problem_1 = 'AB' OR
eiel_t2_saneam_ed.problem_1 = 'CE' OR eiel_t2_saneam_ed.problem_1 = 'EI' OR
eiel_t2_saneam_ed.problem_1 = 'FE' OR eiel_t2_saneam_ed.problem_1 = 'FM' OR
eiel_t2_saneam_ed.problem_1 = 'IF' OR eiel_t2_saneam_ed.problem_1 = 'IN' OR eiel_t2_saneam_ed.problem_1 = 'NO'
OR eiel_t2_saneam_ed.problem_1 = 'OT' OR eiel_t2_saneam_ed.problem_1 = 'VI');
ALTER TABLE eiel_t2_saneam_ed ADD CONSTRAINT problem_2chk CHECK (eiel_t2_saneam_ed.problem_2 = 'AB' OR
eiel_t2_saneam_ed.problem_2 = 'CE' OR eiel_t2_saneam_ed.problem_2 = 'EI' OR
eiel_t2_saneam_ed.problem_2 = 'FE' OR eiel_t2_saneam_ed.problem_2 = 'FM' OR
eiel_t2_saneam_ed.problem_2 = 'IF' OR eiel_t2_saneam_ed.problem_2 = 'IN' OR eiel_t2_saneam_ed.problem_2 = 'NO'
OR eiel_t2_saneam_ed.problem_2 = 'OT' OR eiel_t2_saneam_ed.problem_1 = 'VI');
ALTER TABLE eiel_t2_saneam_ed ADD CONSTRAINT problem_3chk CHECK (eiel_t2_saneam_ed.problem_3 = 'AB' OR
eiel_t2_saneam_ed.problem_3 = 'CE' OR eiel_t2_saneam_ed.problem_3 = 'EI' OR
eiel_t2_saneam_ed.problem_3 = 'FE' OR eiel_t2_saneam_ed.problem_3 = 'FM' OR
eiel_t2_saneam_ed.problem_3 = 'IF' OR eiel_t2_saneam_ed.problem_3 = 'IN' OR eiel_t2_saneam_ed.problem_3 = 'NO'
OR eiel_t2_saneam_ed.problem_3 = 'OT' OR eiel_t2_saneam_ed.problem_3 = 'VI');
ALTER TABLE eiel_t2_saneam_ed ADD CONSTRAINT lodo_gestchk CHECK (eiel_t2_saneam_ed.lodo_gest = 'CO' OR
eiel_t2_saneam_ed.lodo_gest = 'EM' OR eiel_t2_saneam_ed.lodo_gest = 'EP' OR
eiel_t2_saneam_ed.lodo_gest = 'MA' OR eiel_t2_saneam_ed.lodo_gest = 'MU' OR
eiel_t2_saneam_ed.lodo_gest = 'NO' OR eiel_t2_saneam_ed.lodo_gest = 'OT' OR eiel_t2_saneam_ed.lodo_gest = 'PV'
OR eiel_t2_saneam_ed.lodo_gest = 'VE');

create index i_t2_clave_ed on "public".eiel_t2_saneam_ed(clave);
create index i_t2_codprov_ed on "public".eiel_t2_saneam_ed(codprov);
create index i_t2_codmunic_ed on "public".eiel_t2_saneam_ed(codmunic);
create index i_t2_orden_ed on "public".eiel_t2_saneam_ed(orden_ed);


CREATE TABLE "public".eiel_t_saneam_au
(
  clave character varying (3) NOT NULL, 
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  tipo_sau character varying(2),
  estado_sau character varying(1),
  adecuacion_sau character varying(2),
  sau_vivien integer,
  sau_pob_re integer,
  sau_pob_es integer,
  sau_vi_def integer,
  sau_pob_re_def integer,
  sau_pob_es_def integer,
  fecha_inst date,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_saneam_au_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_saneam_au OWNER TO postgres;
ALTER TABLE eiel_t_saneam_au ADD CONSTRAINT tipo_sauchk CHECK (eiel_t_saneam_au.tipo_sau = 'FS'
OR eiel_t_saneam_au.tipo_sau = 'OT' OR eiel_t_saneam_au.tipo_sau = 'PN');
ALTER TABLE eiel_t_saneam_au ADD CONSTRAINT estado_sauchk CHECK (eiel_t_saneam_au.estado_sau = 'B'
OR eiel_t_saneam_au.estado_sau = 'E' OR eiel_t_saneam_au.estado_sau = 'M' OR eiel_t_saneam_au.estado_sau = 'R');
ALTER TABLE eiel_t_saneam_au ADD CONSTRAINT adecuacion_sauchk CHECK (eiel_t_saneam_au.adecuacion_sau = 'AD'
OR eiel_t_saneam_au.adecuacion_sau = 'IN');

create index i_t_clave_au on "public".eiel_t_saneam_au(clave);
create index i_t_codprov_au on "public".eiel_t_saneam_au(codprov);
create index i_t_codmunic_au on "public".eiel_t_saneam_au(codmunic);
create index i_t_codentidad_au on "public".eiel_t_saneam_au(codentidad);
create index i_t_codpoblamiento_au on "public".eiel_t_saneam_au(codpoblamiento);



-- Table: eiel_t_saneam_pv

-- DROP TABLE eiel_t_saneam_pv;

CREATE TABLE eiel_t_saneam_pv
(
  clave character varying(2) NOT NULL,
  codprov character varying(2) NOT NULL,
  codmunic character varying(3) NOT NULL,
  orden_pv character varying(3) NOT NULL,
  tipo character varying(2),
  zona character varying(2),
  distancia_nucleo integer,
  fecha_ini_vertido date,
  observ character varying(255),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_saneam_pv_pkey PRIMARY KEY (clave , codprov , codmunic , orden_pv , revision_actual ),
  CONSTRAINT clavechk CHECK (clave::text = 'EM'::text OR clave::text = 'PV'::text),
  CONSTRAINT tipochk CHECK (tipo::text = 'AD'::text OR tipo::text = 'AM'::text OR tipo::text = 'OT'::text),
  CONSTRAINT zonachk CHECK (zona::text = 'ZM'::text OR zona::text = 'ZN'::text OR zona::text = 'ZS'::text)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_t_saneam_pv
  OWNER TO postgres;
GRANT ALL ON TABLE eiel_t_saneam_pv TO postgres;
GRANT SELECT ON TABLE eiel_t_saneam_pv TO consultas;

-- Index: i_t_clave_pv

-- DROP INDEX i_t_clave_pv;

CREATE INDEX i_t_clave_pv
  ON eiel_t_saneam_pv
  USING btree
  (clave );

-- Index: i_t_codmunic_pv

-- DROP INDEX i_t_codmunic_pv;

CREATE INDEX i_t_codmunic_pv
  ON eiel_t_saneam_pv
  USING btree
  (codmunic );

-- Index: i_t_codprov_pv

-- DROP INDEX i_t_codprov_pv;

CREATE INDEX i_t_codprov_pv
  ON eiel_t_saneam_pv
  USING btree
  (codprov );

-- Index: i_t_orden_pv

-- DROP INDEX i_t_orden_pv;

CREATE INDEX i_t_orden_pv
  ON eiel_t_saneam_pv
  USING btree
  (orden_pv );



CREATE TABLE "public".eiel_t_saneam_serv
(
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  pozos_registro character varying(2),
  sumideros character varying(2),
  aliv_c_acum character varying(2),
  aliv_s_acum character varying(2),
  calidad_serv character varying(2),
  viviendas_s_conex integer,
  viviendas_c_conex integer,
  long_rs_deficit integer,
  viviendas_def_conex integer,
  pobl_res_def_afect integer,
  pobl_est_def_afect integer,
  caudal_total integer,
  caudal_tratado integer,
  c_reutilizado_urb integer,
  c_reutilizado_rust integer,
  c_reutilizado_ind integer,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_saneam_serv_pkey PRIMARY KEY (codprov, codmunic, codentidad,codpoblamiento, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_saneam_serv OWNER TO postgres;
ALTER TABLE eiel_t_saneam_serv ADD CONSTRAINT pozos_registrochk CHECK (eiel_t_saneam_serv.pozos_registro = 'IN' OR
eiel_t_saneam_serv.pozos_registro = 'NO' OR eiel_t_saneam_serv.pozos_registro = 'SF');
ALTER TABLE eiel_t_saneam_serv ADD CONSTRAINT sumideroschk CHECK (eiel_t_saneam_serv.sumideros = 'IN' OR
eiel_t_saneam_serv.sumideros = 'NO' OR eiel_t_saneam_serv.sumideros = 'SF');
ALTER TABLE eiel_t_saneam_serv ADD CONSTRAINT aliv_c_acumchk CHECK (eiel_t_saneam_serv.aliv_c_acum = 'SI' OR
eiel_t_saneam_serv.aliv_c_acum = 'NO');
ALTER TABLE eiel_t_saneam_serv ADD CONSTRAINT aliv_s_acumchk CHECK (eiel_t_saneam_serv.aliv_s_acum = 'SI' OR
eiel_t_saneam_serv.aliv_s_acum = 'NO');
ALTER TABLE eiel_t_saneam_serv ADD CONSTRAINT calidad_servchk CHECK (eiel_t_saneam_serv.calidad_serv = 'B' OR
eiel_t_saneam_serv.calidad_serv = 'E' OR eiel_t_saneam_serv.calidad_serv = 'M' OR
eiel_t_saneam_serv.calidad_serv = 'NO' OR eiel_t_saneam_serv.calidad_serv = 'R');


create index i_t_codprov_serv on "public".eiel_t_saneam_serv(codprov);
create index i_t_codmunic_serv on "public".eiel_t_saneam_serv(codmunic);
create index i_t_codentidad_serv on "public".eiel_t_saneam_serv(codentidad);
create index i_t_codpoblamiento_serv on "public".eiel_t_saneam_serv(codpoblamiento);



------SANEAMIENTO TR

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
ALTER TABLE eiel_tr_saneam_ed_pobl ADD CONSTRAINT clavechk CHECK (eiel_tr_saneam_ed_pobl.clave_ed = 'ED');


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

ALTER TABLE eiel_tr_saneam_tcl_pobl ADD CONSTRAINT clave_tclchk CHECK (eiel_tr_saneam_tcl_pobl.clave_tcl = 'CL');

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
ALTER TABLE eiel_tr_saneam_tem_pobl ADD CONSTRAINT clave_temchk CHECK (eiel_tr_saneam_tem_pobl.clave_tem = 'EM');


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




------EQUIPAMIENTO C-------

CREATE TABLE "public".eiel_c_as
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_as character varying (4) NOT NULL,
  observ character varying(250),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_as_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_as OWNER TO postgres;

create index i_id_as on "public".eiel_c_as(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_as"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;
    
CREATE TABLE "public".eiel_c_cc
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_cc character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_cc_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_cc OWNER TO postgres;

create index i_id_cc on "public".eiel_c_cc(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_cc"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


CREATE TABLE "public".eiel_c_ce
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_ce character varying (3) NOT NULL,
  observ character varying(250),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_ce_pkey PRIMARY KEY (id) 
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_ce OWNER TO postgres;

create index i_id_ce on "public".eiel_c_ce(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_ce"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;
  
CREATE TABLE "public".eiel_c_cu
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_cu character varying (3) NOT NULL,
  observ character varying(100),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_cu_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_cu OWNER TO postgres;

create index i_id_cu on "public".eiel_c_cu(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_cu"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_en
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_en character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_en_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_en OWNER TO postgres;

create index i_id_en on "public".eiel_c_en(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_en"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_id
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_id character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_id_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_id OWNER TO postgres;

create index i_id_id on "public".eiel_c_id(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_id"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_ip
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_ip character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_ip_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_ip OWNER TO postgres;

create index i_id_ip on "public".eiel_c_ip(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_ip"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_lm
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_lm character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_lm_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_lm OWNER TO postgres;

create index i_id_lm on "public".eiel_c_lm(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_lm"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_mt
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_mt character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_mt_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_mt OWNER TO postgres;

create index i_id_mt on "public".eiel_c_mt(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_mt"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;
    
CREATE TABLE "public".eiel_c_pj
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_pj character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_pj_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_pj OWNER TO postgres;

create index i_id_pj on "public".eiel_c_pj(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_pj"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_sa
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_sa character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_sa_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_sa OWNER TO postgres;

create index i_id_sa on "public".eiel_c_sa(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_sa"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


CREATE TABLE "public".eiel_c_su
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_su character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_su_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_su OWNER TO postgres;

create index i_id_su on "public".eiel_c_su(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_su"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


CREATE TABLE "public".eiel_c_ta
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_ta character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_ta_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_ta OWNER TO postgres;

create index i_id_ta on "public".eiel_c_ta(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_ta"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


------EQUIPAMIENTO T

CREATE TABLE "public".eiel_t_as
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_as character varying (3) NOT NULL,
  nombre character varying(40),
  tipo character varying(2),
  titular character varying(2),
  gestor character varying(2),
  plazas integer,
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_as_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_as, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_as OWNER TO postgres;
ALTER TABLE eiel_t_as ADD CONSTRAINT clavechk CHECK (eiel_t_as.clave= 'AS');
ALTER TABLE eiel_t_as ADD CONSTRAINT tipochk CHECK (eiel_t_as.tipo= 'AL' OR  eiel_t_as.tipo= 'CA' OR
eiel_t_as.tipo= 'CE' OR eiel_t_as.tipo= 'CT' OR eiel_t_as.tipo= 'EX' OR eiel_t_as.tipo= 'GI' OR
eiel_t_as.tipo= 'IN' OR eiel_t_as.tipo= 'RA');
ALTER TABLE eiel_t_as ADD CONSTRAINT titularchk CHECK (eiel_t_as.titular= 'CA' OR  eiel_t_as.titular= 'CO' OR
eiel_t_as.titular= 'EM' OR eiel_t_as.titular= 'EP' OR eiel_t_as.titular= 'MA' OR eiel_t_as.titular= 'MU' OR
eiel_t_as.titular= 'OT' OR eiel_t_as.titular= 'PR' OR eiel_t_as.titular= 'PV' OR eiel_t_as.titular= 'VE' );
ALTER TABLE eiel_t_as ADD CONSTRAINT gestorrchk CHECK (eiel_t_as.gestor= 'CA' OR  eiel_t_as.gestor= 'CO' OR
eiel_t_as.gestor= 'EM' OR eiel_t_as.gestor= 'EP' OR eiel_t_as.gestor= 'MA' OR eiel_t_as.gestor= 'MU' OR
eiel_t_as.gestor= 'OT' OR eiel_t_as.gestor= 'PR' OR eiel_t_as.gestor= 'PV' OR eiel_t_as.gestor= 'VE' );
 ALTER TABLE eiel_t_as ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_as.acceso_s_ruedas= 'SI' OR eiel_t_as.acceso_s_ruedas= 'NO');
 ALTER TABLE eiel_t_as ADD CONSTRAINT estadochk CHECK (eiel_t_as.estado= 'B' OR eiel_t_as.estado= 'E' OR
eiel_t_as.estado= 'M' OR eiel_t_as.estado= 'R');


CREATE TABLE "public".eiel_t_cc
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_cc character varying (3) NOT NULL,
  nombre character varying(50),
  tipo character varying(2),
  titular character varying(2),
  tenencia character varying(2),
  s_cubierta integer,
  s_solar integer,
  s_aire integer,
   estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_cc_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_cc, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_cc OWNER TO postgres;
ALTER TABLE eiel_t_cc ADD CONSTRAINT clavechk CHECK (eiel_t_cc.clave= 'CC');
ALTER TABLE eiel_t_cc ADD CONSTRAINT tipochk CHECK (eiel_t_cc.tipo= 'AE' OR eiel_t_cc.tipo= 'AY' OR
eiel_t_cc.tipo= 'OT' OR eiel_t_cc.tipo= 'UA' OR eiel_t_cc.tipo= 'VM');
ALTER TABLE eiel_t_cc ADD CONSTRAINT titularchk CHECK (eiel_t_cc.titular= 'CA' OR eiel_t_cc.titular= 'MU' OR
eiel_t_cc.titular= 'OP' OR eiel_t_cc.titular= 'OT' OR eiel_t_cc.titular= 'PR' OR
eiel_t_cc.titular= 'PV');
ALTER TABLE eiel_t_cc ADD CONSTRAINT tenenciachk CHECK (eiel_t_cc.tenencia= 'AL' OR eiel_t_cc.tenencia= 'CE' OR
eiel_t_cc.tenencia= 'MU' OR eiel_t_cc.tenencia= 'OT');
ALTER TABLE eiel_t_cc ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_cc.acceso_s_ruedas= 'SI' OR eiel_t_cc.acceso_s_ruedas= 'NO');
 ALTER TABLE eiel_t_cc ADD CONSTRAINT estadochk CHECK (eiel_t_cc.estado= 'B' OR eiel_t_cc.estado= 'E' OR
eiel_t_cc.estado= 'M' OR eiel_t_cc.estado= 'R');


CREATE TABLE "public".eiel_t_cc_usos
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  orden_cc character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
 uso character varying(2) NOT NULL,
  s_cubierta integer NOT NULL,
  fecha_ini date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  orden_uso character varying (3),
  inst_pertenece character varying (20),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,  
  CONSTRAINT eiel_t_cc_usos_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento, orden_cc, uso, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_cc_usos OWNER TO postgres;
ALTER TABLE eiel_t_cc_usos ADD CONSTRAINT clavechk CHECK (eiel_t_cc_usos.clave= 'CC');
ALTER TABLE eiel_t_cc_usos ADD CONSTRAINT usochk CHECK (eiel_t_cc_usos.uso= 'AA' OR eiel_t_cc_usos.uso= 'AM' OR
eiel_t_cc_usos.uso= 'AS' OR eiel_t_cc_usos.uso= 'BA' OR eiel_t_cc_usos.uso= 'BR' OR
eiel_t_cc_usos.uso= 'CI' OR eiel_t_cc_usos.uso= 'MO' OR eiel_t_cc_usos.uso= 'OA' OR
eiel_t_cc_usos.uso= 'OT' OR eiel_t_cc_usos.uso= 'PN' OR eiel_t_cc_usos.uso= 'SA' OR
eiel_t_cc_usos.uso= 'SO' OR eiel_t_cc_usos.uso= 'VI');




CREATE TABLE eiel_t_ce
(
  clave character varying(2) NOT NULL,
  codprov character varying(2) NOT NULL,
  codmunic character varying(3) NOT NULL,
  codentidad character varying(4) NOT NULL,
  codpoblamiento character varying(2) NOT NULL,
  orden_ce character varying(3) NOT NULL,
  nombre character varying(40),
  titular character varying(2),
  distancia numeric(5,1),
  acceso character varying(1),
  capilla character varying(2),
  deposito character varying(2),
  ampliacion character varying(2),
  saturacion numeric(8,2),
  superficie integer,
  crematorio character varying(2),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  inst_pertenece character varying(10),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_ce_pkey PRIMARY KEY (clave , codprov , codmunic , codentidad , codpoblamiento , orden_ce , revision_actual ),
  CONSTRAINT acceso_s_ruedaschk CHECK (acceso_s_ruedas::text = 'SI'::text OR acceso_s_ruedas::text = 'NO'::text),
  CONSTRAINT accesochk CHECK (acceso::text = 'B'::text OR acceso::text = 'E'::text OR acceso::text = 'M'::text OR acceso::text = 'R'::text),
  CONSTRAINT ampliacionchk CHECK (ampliacion::text = 'SI'::text OR ampliacion::text = 'NO'::text),
  CONSTRAINT capillachk CHECK (capilla::text = 'EC'::text OR capilla::text = 'MO'::text OR capilla::text = 'NO'::text),
  CONSTRAINT clavechk CHECK (clave::text = 'CE'::text),
  CONSTRAINT crematoriochk CHECK (crematorio::text = 'SI'::text OR crematorio::text = 'NO'::text),
  CONSTRAINT depositochk CHECK (deposito::text = 'SI'::text OR deposito::text = 'NO'::text),
  CONSTRAINT titularchk CHECK (titular::text = 'CR'::text OR titular::text = 'MU'::text OR titular::text = 'OT'::text OR titular::text = 'PV'::text)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_t_ce
  OWNER TO postgres;
GRANT ALL ON TABLE eiel_t_ce TO postgres;
GRANT SELECT ON TABLE eiel_t_ce TO consultas;


CREATE TABLE "public".eiel_t_cu
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_cu character varying (3) NOT NULL,
  nombre character varying(50),
  tipo character varying(2),
  titular character varying(2),
  gestor character varying(2),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(100),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  inst_pertenece character varying(20),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_cu_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_cu, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_cu OWNER TO postgres;

ALTER TABLE eiel_t_cu ADD CONSTRAINT clavechk CHECK (eiel_t_cu.clave= 'CU');
ALTER TABLE eiel_t_cu ADD CONSTRAINT tipochk CHECK (eiel_t_cu.tipo= 'AR' OR  eiel_t_cu.tipo= 'AU' OR
eiel_t_cu.tipo= 'BI' OR eiel_t_cu.tipo= 'CA' OR eiel_t_cu.tipo= 'CC' OR eiel_t_cu.tipo= 'CS' OR
eiel_t_cu.tipo= 'HP' OR eiel_t_cu.tipo= 'KI' OR eiel_t_cu.tipo= 'LU' OR eiel_t_cu.tipo= 'MS' OR eiel_t_cu.tipo= 'OT'
OR eiel_t_cu.tipo= 'PZ' OR eiel_t_cu.tipo= 'SC' OR eiel_t_cu.tipo= 'TC');
ALTER TABLE eiel_t_cu ADD CONSTRAINT titularchk CHECK ( eiel_t_cu.titular= 'CA' OR eiel_t_cu.titular= 'CO' OR
eiel_t_cu.titular= 'EM' OR eiel_t_cu.titular= 'EP' OR eiel_t_cu.titular= 'MA' OR eiel_t_cu.titular= 'MU' OR
eiel_t_cu.titular= 'OT' OR eiel_t_cu.titular= 'PR' OR eiel_t_cu.titular= 'PV'  OR eiel_t_cu.titular= 'VE');
ALTER TABLE eiel_t_cu ADD CONSTRAINT gestorchk CHECK ( eiel_t_cu.gestor= 'CA' OR eiel_t_cu.gestor= 'CO' OR
eiel_t_cu.gestor= 'EM' OR eiel_t_cu.gestor= 'EP' OR eiel_t_cu.gestor= 'MA' OR eiel_t_cu.gestor= 'MU' OR
eiel_t_cu.gestor= 'OT' OR eiel_t_cu.gestor= 'PR' OR eiel_t_cu.gestor= 'PV'  OR eiel_t_cu.gestor= 'VE');
ALTER TABLE eiel_t_cu ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_cu.acceso_s_ruedas= 'SI' OR eiel_t_cu.acceso_s_ruedas= 'NO');
ALTER TABLE eiel_t_cu ADD CONSTRAINT estadochk CHECK (eiel_t_cu.estado= 'B' OR eiel_t_cu.estado= 'E' OR
eiel_t_cu.estado= 'M' OR eiel_t_cu.estado= 'R');

CREATE TABLE "public".eiel_t_cu_usos
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_cu character varying (3) NOT NULL,
  uso character varying(2) NOT NULL,
  s_cubierta integer,
  fecha_ini date,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  orden_uso integer,
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_cu_usos_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_cu, uso, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_cu_usos OWNER TO postgres;
ALTER TABLE eiel_t_cu_usos ADD CONSTRAINT clavechk CHECK (eiel_t_cu_usos.clave= 'CU');
ALTER TABLE eiel_t_cu_usos ADD CONSTRAINT usochk CHECK (eiel_t_cu_usos.uso= 'AO' OR  eiel_t_cu_usos.uso= 'BA' OR
eiel_t_cu_usos.uso= 'BR' OR  eiel_t_cu_usos.uso= 'CI' OR eiel_t_cu_usos.uso= 'CV' OR  eiel_t_cu_usos.uso= 'DC' OR
eiel_t_cu_usos.uso= 'ED' OR  eiel_t_cu_usos.uso= 'MO' OR eiel_t_cu_usos.uso= 'OT' OR  eiel_t_cu_usos.uso= 'PN' OR
eiel_t_cu_usos.uso= 'PS' OR  eiel_t_cu_usos.uso= 'TE' OR eiel_t_cu_usos.uso= 'KM' );







CREATE TABLE "public".eiel_t_en
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_en character varying (3) NOT NULL,
  nombre character varying(50),
  ambito character varying(2),
  titular character varying(2),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_en_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_en, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_en OWNER TO postgres;

ALTER TABLE eiel_t_en ADD CONSTRAINT clavechk CHECK (eiel_t_en.clave= 'EN');
ALTER TABLE eiel_t_en ADD CONSTRAINT ambitochk CHECK (eiel_t_en.ambito= 'A' OR eiel_t_en.ambito= 'C' OR
eiel_t_en.ambito= 'L');
ALTER TABLE eiel_t_en ADD CONSTRAINT titularchk CHECK (eiel_t_en.titular= 'CE' OR eiel_t_en.titular= 'CL' OR
eiel_t_en.titular= 'OT' OR eiel_t_en.titular= 'PR');
ALTER TABLE eiel_t_en ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_en.acceso_s_ruedas= 'SI' OR eiel_t_en.acceso_s_ruedas= 'NO');
 ALTER TABLE eiel_t_en ADD CONSTRAINT estadochk CHECK (eiel_t_en.estado= 'B' OR eiel_t_en.estado= 'E' OR
eiel_t_en.estado= 'M' OR eiel_t_en.estado= 'R');


CREATE TABLE "public".eiel_t_en_nivel
(

  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_en character varying (3) NOT NULL,
  nivel character varying(3) NOT NULL,
  unidades integer,
  plazas integer,
  alumnos integer,
  fecha_curso date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  orden_en_nivel character varying (3),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_en_nivel_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_en,nivel, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_en_nivel OWNER TO postgres;
ALTER TABLE eiel_t_en_nivel ADD CONSTRAINT clavechk CHECK (eiel_t_en_nivel.clave= 'EN');
ALTER TABLE eiel_t_en_nivel ADD CONSTRAINT nivelchk CHECK (eiel_t_en_nivel.nivel= 'BAC' OR eiel_t_en_nivel.nivel= 'ESO'
OR eiel_t_en_nivel.nivel= 'ESP' OR eiel_t_en_nivel.nivel= 'FP1' OR eiel_t_en_nivel.nivel= 'FP2' OR
eiel_t_en_nivel.nivel= 'INF' OR eiel_t_en_nivel.nivel= 'OTR' OR eiel_t_en_nivel.nivel= 'PRI');



CREATE TABLE eiel_t_id
(
  clave character varying(2) NOT NULL,
  codprov character varying(2) NOT NULL,
  codmunic character varying(3) NOT NULL,
  codentidad character varying(4) NOT NULL,
  codpoblamiento character varying(2) NOT NULL,
  orden_id character varying(3) NOT NULL,
  nombre character varying(50),
  tipo character varying(2),
  titular character varying(2),
  gestor character varying(2),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  estado character varying(2),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  inst_pertenece character varying(20),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_id_pkey PRIMARY KEY (clave , codprov , codmunic , codentidad , codpoblamiento , orden_id , revision_actual ),
  CONSTRAINT acceso_s_ruedaschk CHECK (acceso_s_ruedas::text = 'SI'::text OR acceso_s_ruedas::text = 'NO'::text),
  CONSTRAINT clavechk CHECK (clave::text = 'ID'::text),
  CONSTRAINT estadochk CHECK (estado::text = 'B'::text OR estado::text = 'E'::text OR estado::text = 'M'::text OR estado::text = 'R'::text),
  CONSTRAINT gestorchk CHECK (gestor::text = 'CA'::text OR gestor::text = 'CO'::text OR gestor::text = 'EM'::text OR gestor::text = 'EP'::text OR gestor::text = 'MA'::text OR gestor::text = 'MU'::text OR gestor::text = 'OT'::text OR gestor::text = 'PR'::text OR gestor::text = 'PV'::text OR gestor::text = 'VE'::text),
  CONSTRAINT tipochk CHECK (tipo::text = 'CP'::text OR tipo::text = 'EV'::text OR tipo::text = 'FC'::text OR tipo::text = 'FR'::text OR tipo::text = 'GI'::text OR tipo::text = 'OT'::text OR tipo::text = 'PC'::text OR tipo::text = 'PD'::text OR tipo::text = 'PI'::text OR tipo::text = 'PP'::text OR tipo::text = 'PT'::text OR tipo::text = 'SK'::text OR tipo::text = 'TJ'::text),
  CONSTRAINT titularchk CHECK (titular::text = 'CA'::text OR titular::text = 'CO'::text OR titular::text = 'EM'::text OR titular::text = 'EP'::text OR titular::text = 'MA'::text OR titular::text = 'MU'::text OR titular::text = 'OT'::text OR titular::text = 'PR'::text OR titular::text = 'PV'::text OR titular::text = 'VE'::text)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_t_id
  OWNER TO postgres;
GRANT ALL ON TABLE eiel_t_id TO postgres;
GRANT SELECT ON TABLE eiel_t_id TO consultas;


CREATE TABLE "public".eiel_t_id_deportes
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_id character varying (3) NOT NULL,
  tipo_deporte character varying(2) NOT NULL,
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  orden_id_deportes character varying(3),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_id_deportes_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento, orden_id,tipo_deporte, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_id_deportes OWNER TO postgres;

ALTER TABLE eiel_t_id_deportes ADD CONSTRAINT clavechk CHECK (eiel_t_id_deportes.clave= 'ID');
ALTER TABLE eiel_t_id ADD CONSTRAINT tipochk CHECK (eiel_t_id.tipo= 'AS' OR  eiel_t_id.tipo= 'AT' OR
eiel_t_id.tipo= 'BB' OR eiel_t_id.tipo= 'BC' OR eiel_t_id.tipo= 'BM' OR eiel_t_id.tipo= 'BO' OR eiel_t_id.tipo= 'BV' OR
eiel_t_id.tipo= 'DI' OR eiel_t_id.tipo= 'EN' OR eiel_t_id.tipo= 'FB' OR eiel_t_id.tipo= 'GF' OR eiel_t_id.tipo= 'GI' OR
eiel_t_id.tipo= 'JU' OR eiel_t_id.tipo= 'LU' OR eiel_t_id.tipo= 'NA' OR eiel_t_id.tipo= 'OT'
OR eiel_t_id.tipo= 'PD' OR eiel_t_id.tipo= 'PE' OR eiel_t_id.tipo= 'PL'
OR eiel_t_id.tipo= 'PR' OR eiel_t_id.tipo= 'PT' OR eiel_t_id.tipo= 'RE'
OR eiel_t_id.tipo= 'TE' OR eiel_t_id.tipo= 'TP' OR eiel_t_id.tipo= 'VE');



CREATE TABLE "public".eiel_t_ip
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_ip character varying (3) NOT NULL,
  nombre character varying(150),
  tipo character varying(2),
  titular character varying(2),
  gestor character varying(2),
  ambito character varying(2),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  plan_profe integer,
  plan_volun integer,
  estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  vehic_incendio integer,
  vehic_rescate integer,
  ambulancia integer,
  medios_aereos integer,
  otros_vehc integer,
  quitanieves integer,
  detec_ince integer,
  otros integer,
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_ip_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_ip, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_ip OWNER TO postgres;
ALTER TABLE eiel_t_ip ADD CONSTRAINT clavechk CHECK (eiel_t_ip.clave= 'IP');
ALTER TABLE eiel_t_ip ADD CONSTRAINT tipochk CHECK (eiel_t_ip.tipo= 'BO' OR eiel_t_ip.tipo= 'CS' OR
eiel_t_ip.tipo= 'OT' OR eiel_t_ip.tipo= 'PC');
ALTER TABLE eiel_t_ip ADD CONSTRAINT titularchk CHECK (eiel_t_ip.titular= 'CA' OR eiel_t_ip.titular= 'CO' OR
eiel_t_ip.titular= 'MA' OR eiel_t_ip.titular= 'MU' OR eiel_t_ip.titular= 'OP' OR
eiel_t_ip.titular= 'OT' OR eiel_t_ip.titular= 'PR' OR eiel_t_ip.titular= 'PV' OR eiel_t_ip.titular= 'XR');
ALTER TABLE eiel_t_ip ADD CONSTRAINT gestorchk CHECK (eiel_t_ip.gestor= 'CA' OR eiel_t_ip.gestor= 'CO' OR
eiel_t_ip.gestor= 'MA' OR eiel_t_ip.gestor= 'MU' OR eiel_t_ip.gestor= 'OP' OR
eiel_t_ip.gestor= 'OT' OR eiel_t_ip.gestor= 'PR' OR eiel_t_ip.gestor= 'PV' OR eiel_t_ip.gestor= 'XR');
ALTER TABLE eiel_t_ip ADD CONSTRAINT ambitochk CHECK (eiel_t_ip.ambito= 'CO' OR eiel_t_ip.ambito= 'MU' OR
eiel_t_ip.ambito= 'PR');
ALTER TABLE eiel_t_ip ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_ip.acceso_s_ruedas= 'SI' OR eiel_t_ip.acceso_s_ruedas= 'NO');
 ALTER TABLE eiel_t_ip ADD CONSTRAINT estadochk CHECK (eiel_t_ip.estado= 'B' OR eiel_t_ip.estado= 'E' OR
eiel_t_ip.estado= 'M' OR eiel_t_ip.estado= 'R');




CREATE TABLE "public".eiel_t_lm
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_lm character varying (3) NOT NULL,
  nombre character varying(50),
  tipo character varying(2),
  titular character varying(2),
  gestor character varying(2),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_lm_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_lm, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_lm OWNER TO postgres;
ALTER TABLE eiel_t_lm ADD CONSTRAINT clavechk CHECK (eiel_t_lm.clave= 'LM');
ALTER TABLE eiel_t_lm ADD CONSTRAINT tipochk CHECK (eiel_t_lm.tipo= 'FE' OR  eiel_t_lm.tipo= 'LO' OR
eiel_t_lm.tipo= 'ME');
ALTER TABLE eiel_t_lm ADD CONSTRAINT titularchk CHECK (eiel_t_lm.titular= 'CA' OR  eiel_t_lm.titular= 'CO' OR
eiel_t_lm.titular= 'CP' OR
eiel_t_lm.titular= 'EM' OR eiel_t_lm.titular= 'EP' OR eiel_t_lm.titular= 'MA' OR eiel_t_lm.titular= 'MU' OR
eiel_t_lm.titular= 'OT' OR eiel_t_lm.titular= 'PR' OR eiel_t_lm.titular= 'PV'  OR eiel_t_lm.titular= 'VE');
ALTER TABLE eiel_t_lm ADD CONSTRAINT gestorchk CHECK (eiel_t_lm.gestor= 'CA' OR  eiel_t_lm.gestor= 'CO' OR
eiel_t_lm.titular= 'CP' OR
eiel_t_lm.titular= 'EM' OR eiel_t_lm.gestor= 'EP' OR eiel_t_lm.gestor= 'MA' OR eiel_t_lm.gestor= 'MU' OR
eiel_t_lm.titular= 'OT' OR eiel_t_lm.gestor= 'PR' OR eiel_t_lm.gestor= 'PV'  OR eiel_t_lm.gestor= 'VE');
ALTER TABLE eiel_t_lm ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_lm.acceso_s_ruedas= 'SI' OR eiel_t_lm.acceso_s_ruedas= 'NO');
ALTER TABLE eiel_t_lm ADD CONSTRAINT estadochk CHECK (eiel_t_lm.estado= 'B' OR eiel_t_lm.estado= 'E'
OR eiel_t_lm.estado= 'M' OR eiel_t_lm.estado= 'R');



CREATE TABLE "public".eiel_t_mt
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_mt character varying (3) NOT NULL,
  nombre character varying(50),
  clase character varying(2),
  titular character varying(3),
  gestor character varying(2),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  capacidad numeric(8,0),
  utilizacion numeric (3,0),
   tunel character varying(2),
  bovino character varying(2),
  ovino character varying(2),
  porcino character varying(2),
  otros character varying(2),
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_mt_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_mt, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_mt OWNER TO postgres;
ALTER TABLE eiel_t_mt ADD CONSTRAINT clavechk CHECK (eiel_t_mt.clave= 'MT');
ALTER TABLE eiel_t_mt ADD CONSTRAINT clasechk CHECK (eiel_t_mt.clase= 'CO' OR  eiel_t_mt.clase= 'MU' OR
eiel_t_mt.clase= 'OT' OR eiel_t_mt.clase= 'PR');
ALTER TABLE eiel_t_mt ADD CONSTRAINT titularchk CHECK (eiel_t_mt.titular= 'CA' OR  eiel_t_mt.titular= 'CO' OR
eiel_t_mt.titular= 'EM' OR eiel_t_mt.titular= 'EP' OR eiel_t_mt.titular= 'MA' OR eiel_t_mt.titular= 'MU' OR
eiel_t_mt.titular= 'OT' OR eiel_t_mt.titular= 'PR' OR eiel_t_mt.titular= 'PV'  OR eiel_t_mt.titular= 'VE');
ALTER TABLE eiel_t_mt ADD CONSTRAINT gestorchk CHECK (eiel_t_mt.gestor= 'CA' OR  eiel_t_mt.gestor= 'CO' OR
eiel_t_mt.titular= 'EM' OR eiel_t_mt.gestor= 'EP' OR eiel_t_mt.gestor= 'MA' OR eiel_t_mt.gestor= 'MU' OR
eiel_t_mt.titular= 'OT' OR eiel_t_mt.gestor= 'PR' OR eiel_t_mt.gestor= 'PV'  OR eiel_t_mt.gestor= 'VE');
ALTER TABLE eiel_t_mt ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_mt.acceso_s_ruedas= 'SI' OR eiel_t_mt.acceso_s_ruedas= 'NO');
ALTER TABLE eiel_t_mt ADD CONSTRAINT estadochk CHECK (eiel_t_mt.estado= 'B' OR eiel_t_mt.estado= 'E'
OR eiel_t_mt.estado= 'M' OR eiel_t_mt.estado= 'R');
ALTER TABLE eiel_t_mt ADD CONSTRAINT bovinochk CHECK (eiel_t_mt.bovino= 'SI' OR eiel_t_mt.bovino= 'NO');
ALTER TABLE eiel_t_mt ADD CONSTRAINT ovinoschk CHECK (eiel_t_mt.ovino = 'SI' OR eiel_t_mt.ovino= 'NO');
ALTER TABLE eiel_t_mt ADD CONSTRAINT porcinochk CHECK (eiel_t_mt.porcino= 'SI' OR eiel_t_mt.porcino= 'NO');
ALTER TABLE eiel_t_mt ADD CONSTRAINT otroschk CHECK (eiel_t_mt.otros= 'SI' OR eiel_t_mt.otros= 'NO');
ALTER TABLE eiel_t_mt ADD CONSTRAINT tunelchk CHECK (eiel_t_mt.tunel= 'SI' OR eiel_t_mt.tunel= 'NO');



CREATE TABLE "public".eiel_t_pj
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_pj character varying (3) NOT NULL,
  nombre character varying(40),
 tipo character varying(2),
  titular character varying(2),
  gestor character varying(2),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  agua character varying(2),
  saneamiento character varying(2),
  electricidad character varying(2),
  comedor character varying(2),
  juegos_inf character varying(2),
  otras character varying(2),
  estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_pj_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_pj, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_pj OWNER TO postgres;
ALTER TABLE eiel_t_pj ADD CONSTRAINT clavechk CHECK (eiel_t_pj.clave= 'PJ');
ALTER TABLE eiel_t_pj ADD CONSTRAINT tipochk CHECK (eiel_t_pj.tipo= 'AN' OR  eiel_t_pj.tipo= 'CA' OR
eiel_t_pj.tipo= 'JA' OR eiel_t_pj.tipo= 'OT' OR eiel_t_pj.tipo= 'PI' OR eiel_t_pj.tipo= 'PN' OR
eiel_t_pj.tipo= 'PU' OR eiel_t_pj.tipo= 'RF' OR eiel_t_pj.tipo= 'ZR');
ALTER TABLE eiel_t_pj ADD CONSTRAINT titularchk CHECK (eiel_t_pj.titular= 'CA' OR  eiel_t_pj.titular= 'CO' OR
eiel_t_pj.titular= 'EM' OR eiel_t_pj.titular= 'EP' OR eiel_t_pj.titular= 'MA' OR eiel_t_pj.titular= 'MU' OR
eiel_t_pj.titular= 'OT' OR eiel_t_pj.titular= 'PR' OR eiel_t_pj.titular= 'PV'  OR eiel_t_pj.titular= 'VE');
ALTER TABLE eiel_t_pj ADD CONSTRAINT gestorchk CHECK (eiel_t_pj.gestor= 'CA' OR  eiel_t_pj.gestor= 'CO' OR
eiel_t_pj.titular= 'EM' OR eiel_t_pj.gestor= 'EP' OR eiel_t_pj.gestor= 'MA' OR eiel_t_pj.gestor= 'MU' OR
eiel_t_pj.titular= 'OT' OR eiel_t_pj.gestor= 'PR' OR eiel_t_pj.gestor= 'PV'  OR eiel_t_pj.gestor= 'VE');
ALTER TABLE eiel_t_pj ADD CONSTRAINT aguachk CHECK (eiel_t_pj.agua= 'SI' OR eiel_t_pj.agua= 'NO');
ALTER TABLE eiel_t_pj ADD CONSTRAINT saneamientochk CHECK (eiel_t_pj.saneamiento= 'SI' OR eiel_t_pj.saneamiento= 'NO');
ALTER TABLE eiel_t_pj ADD CONSTRAINT electricidadchk CHECK (eiel_t_pj.electricidad= 'SI' OR eiel_t_pj.electricidad= 'NO');
ALTER TABLE eiel_t_pj ADD CONSTRAINT comedorchk CHECK (eiel_t_pj.comedor= 'SI' OR eiel_t_pj.comedor= 'NO');
ALTER TABLE eiel_t_pj ADD CONSTRAINT juegos_infchk CHECK (eiel_t_pj.juegos_inf= 'SI' OR eiel_t_pj.juegos_inf= 'NO');
ALTER TABLE eiel_t_pj ADD CONSTRAINT otraschk CHECK (eiel_t_pj.otras= 'SI' OR eiel_t_pj.otras= 'NO');
ALTER TABLE eiel_t_pj ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_pj.acceso_s_ruedas= 'SI' OR eiel_t_pj.acceso_s_ruedas= 'NO');
ALTER TABLE eiel_t_pj ADD CONSTRAINT estadochk CHECK (eiel_t_pj.estado= 'B' OR eiel_t_pj.estado= 'E'
OR eiel_t_pj.estado= 'M' OR eiel_t_pj.estado= 'R');


CREATE TABLE "public".eiel_t_sa
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_sa character varying (3) NOT NULL,
  nombre character varying(40),
  tipo character varying(3),
  titular character varying(3),
  gestor character varying(3),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  uci character varying(2),
  n_camas integer,
  estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_sa_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_sa, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_sa OWNER TO postgres;

ALTER TABLE eiel_t_sa ADD CONSTRAINT clavechk CHECK (eiel_t_sa.clave= 'SA');
ALTER TABLE eiel_t_sa ADD CONSTRAINT tipochk CHECK (eiel_t_sa.tipo= 'AMB' OR  eiel_t_sa.tipo= 'CDS' OR
eiel_t_sa.tipo= 'CLO' OR eiel_t_sa.tipo= 'CUR' OR eiel_t_sa.tipo= 'HGL' OR eiel_t_sa.tipo= 'HIN' OR
eiel_t_sa.tipo= 'HLE' OR eiel_t_sa.tipo= 'HOE' OR eiel_t_sa.tipo= 'HPS' OR eiel_t_sa.tipo= 'HQY' OR
 eiel_t_sa.tipo= 'OTS');
ALTER TABLE eiel_t_sa ADD CONSTRAINT titularchk CHECK (eiel_t_sa.titular= 'CAU' OR  eiel_t_sa.titular= 'DIP' OR
eiel_t_sa.titular= 'FAS' OR eiel_t_sa.titular= 'MAT' OR eiel_t_sa.titular= 'MUN' OR eiel_t_sa.titular= 'OAC' OR
eiel_t_sa.titular= 'OEP' OR eiel_t_sa.titular= 'OTR' OR eiel_t_sa.titular= 'PCR' OR eiel_t_sa.titular= 'PIG' OR
 eiel_t_sa.titular= 'PNB' OR eiel_t_sa.titular= 'PRB' OR eiel_t_sa.titular= 'TSS');
ALTER TABLE eiel_t_sa ADD CONSTRAINT gestorchk CHECK (eiel_t_sa.gestor= 'DIP' OR
eiel_t_sa.gestor= 'FAS' OR eiel_t_sa.gestor= 'INS' OR eiel_t_sa.gestor= 'MAT'  OR
eiel_t_sa.gestor= 'OEP' OR eiel_t_sa.gestor= 'OPB' OR eiel_t_sa.gestor= 'OTR' OR eiel_t_sa.gestor= 'PCR' OR
 eiel_t_sa.gestor= 'PIG' OR eiel_t_sa.gestor= 'PNB' OR eiel_t_sa.gestor= 'SAS' OR eiel_t_sa.gestor= 'CAS');
 ALTER TABLE eiel_t_sa ADD CONSTRAINT ucichk CHECK (eiel_t_sa.uci= 'SI' OR eiel_t_sa.uci= 'NO');
 ALTER TABLE eiel_t_sa ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_sa.acceso_s_ruedas= 'SI' OR eiel_t_sa.acceso_s_ruedas= 'NO');
 ALTER TABLE eiel_t_sa ADD CONSTRAINT estadochk CHECK (eiel_t_sa.estado= 'B' OR eiel_t_sa.estado= 'E' OR
eiel_t_sa.estado= 'M' OR eiel_t_sa.estado= 'R');

CREATE TABLE "public".eiel_t_su
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_su character varying (3) NOT NULL,
  nombre character varying(40),
  titular character varying(2),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  estado character varying(1),
  uso_anterior character varying(2),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  inst_pertenece character varying(20),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_su_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_su, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_su OWNER TO postgres;

ALTER TABLE eiel_t_su ADD CONSTRAINT clavechk CHECK (eiel_t_su.clave= 'SU');
ALTER TABLE eiel_t_su ADD CONSTRAINT titularchk CHECK (eiel_t_su.titular= 'CA' OR eiel_t_su.titular= 'MU' OR
eiel_t_su.titular= 'OP' OR eiel_t_su.titular= 'OT' OR eiel_t_su.titular= 'PR' );
ALTER TABLE eiel_t_su ADD CONSTRAINT estadochk CHECK (eiel_t_su.estado= 'B' OR eiel_t_su.estado= 'E' OR
eiel_t_su.estado= 'M' OR eiel_t_su.estado= 'R');
ALTER TABLE eiel_t_su ADD CONSTRAINT uso_anteriorchk CHECK (eiel_t_su.uso_anterior= 'AS' OR eiel_t_su.uso_anterior= 'AY' OR
eiel_t_su.uso_anterior= 'CC' OR eiel_t_su.uso_anterior= 'CE' OR eiel_t_su.uso_anterior= 'CR' OR
eiel_t_su.uso_anterior= 'CS' OR eiel_t_su.uso_anterior= 'JU' OR eiel_t_su.uso_anterior= 'OT' OR eiel_t_su.uso_anterior= 'VI');






CREATE TABLE "public".eiel_t_ta
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL, 
  codmunic character varying (3) NOT NULL, 
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_ta character varying (3) NOT NULL,
  nombre character varying(40),
  titular character varying(2),
  gestor character varying(2),
  s_cubierta integer,
  s_aire integer,
  s_solar integer,
  salas integer,
  estado character varying(1),
  acceso_s_ruedas character varying(2),
  obra_ejec character varying(2),
  fecha_inst date,
  observ character varying(50),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_ta_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad,codpoblamiento,orden_ta, revision_actual)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_ta OWNER TO postgres;
ALTER TABLE eiel_t_ta ADD CONSTRAINT clavechk CHECK (eiel_t_ta.clave= 'TA');
ALTER TABLE eiel_t_ta ADD CONSTRAINT titularchk CHECK (eiel_t_ta.titular= 'MU' OR 
eiel_t_ta.titular= 'PU' OR eiel_t_ta.titular= 'PV' );
ALTER TABLE eiel_t_ta ADD CONSTRAINT gestorchk CHECK (eiel_t_ta.gestor= 'MU' OR
eiel_t_ta.gestor= 'PU' OR eiel_t_ta.gestor= 'PV' );
ALTER TABLE eiel_t_ta ADD CONSTRAINT acceso_s_ruedaschk CHECK (eiel_t_ta.acceso_s_ruedas= 'SI' OR eiel_t_ta.acceso_s_ruedas= 'NO');
ALTER TABLE eiel_t_ta ADD CONSTRAINT estadochk CHECK (eiel_t_ta.estado= 'B' OR eiel_t_ta.estado= 'E' OR
eiel_t_ta.estado= 'M' OR eiel_t_ta.estado= 'R');





-------INFRAESTRUCTURA VIARIA C

CREATE TABLE "public".eiel_c_redviaria_tu
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  tramo_vu character varying(5),
  tipo character varying(2),
  denominacion character varying(200),
  ancho numeric(8,1),
  estado character varying(2),
  firme character varying(2),
  longitud integer,
  superficie integer,
  viviendas_afec integer,
  fecha_actuacion date,
  obra_ejec character varying(2),
  observ character varying(100),
  ttggss character varying(6),
  via integer,
  numsymbol integer,
  mapa integer,
  CONSTRAINT eiel_c_redviaria_tu_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_redviaria_tu OWNER TO postgres;


ALTER TABLE eiel_c_redviaria_tu ADD CONSTRAINT tipochk CHECK (eiel_c_redviaria_tu.tipo = 'CA' OR
eiel_c_redviaria_tu.tipo = 'OT' OR eiel_c_redviaria_tu.tipo = 'TR');
ALTER TABLE eiel_c_redviaria_tu ADD CONSTRAINT estadochk CHECK (eiel_c_redviaria_tu.estado = 'B' OR
eiel_c_redviaria_tu.estado = 'E' OR eiel_c_redviaria_tu.estado = 'M' OR eiel_c_redviaria_tu.estado = 'NP' OR
eiel_c_redviaria_tu.estado = 'R');

create index i_id_red on "public".eiel_c_redviaria_tu(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_redviaria_tu"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;
    
---------RED CARRETERAS C


CREATE TABLE "public".eiel_c_tramos_carreteras
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  cod_carrt character varying (9) NOT NULL,
  pki numeric(8,3),
  pkf numeric(8,3),
   gestor character varying(2),
  senaliza character varying(1),
  firme character varying(2),
  estado character varying(1),
  ancho numeric(4,1),
  longitud numeric(8,3),
  paso_nivel integer,
  dimensiona character varying(2),
  muy_sinuoso character varying(2),
  pte_excesiva character varying(2),
  fre_estrech character varying(2),
  superficie numeric(8,0),
  obra_ejec character varying(2),
  toma_dato character varying(3),
  fecha_act date,
  fecha_desuso date,
  observ character varying(50),
  CONSTRAINT eiel_c_tramos_carreteras_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_tramos_carreteras OWNER TO postgres;

ALTER TABLE eiel_c_tramos_carreteras ADD CONSTRAINT gestorchk CHECK (eiel_c_tramos_carreteras.gestor = 'CA' OR
eiel_c_tramos_carreteras.gestor = 'ES' OR eiel_c_tramos_carreteras.gestor = 'MU' OR eiel_c_tramos_carreteras.gestor = 'OT' OR
eiel_c_tramos_carreteras.gestor = 'PR' OR eiel_c_tramos_carreteras.gestor = 'NO');
ALTER TABLE eiel_c_tramos_carreteras ADD CONSTRAINT senalizachk CHECK (eiel_c_tramos_carreteras.senaliza = 'A' OR
eiel_c_tramos_carreteras.senaliza = 'H' OR eiel_c_tramos_carreteras.senaliza = 'N' OR eiel_c_tramos_carreteras.senaliza = 'V');
ALTER TABLE eiel_c_tramos_carreteras ADD CONSTRAINT firmechk CHECK (eiel_c_tramos_carreteras.firme = 'AD' OR
eiel_c_tramos_carreteras.firme = 'HR' OR eiel_c_tramos_carreteras.firme = 'MB' OR eiel_c_tramos_carreteras.firme = 'MC'
OR eiel_c_tramos_carreteras.firme = 'OT' OR eiel_c_tramos_carreteras.firme = 'RA' OR eiel_c_tramos_carreteras.firme = 'TI'
OR eiel_c_tramos_carreteras.firme = 'ZE');
ALTER TABLE eiel_c_tramos_carreteras ADD CONSTRAINT estadchk CHECK (eiel_c_tramos_carreteras.estado = 'B' OR
eiel_c_tramos_carreteras.estado = 'E' OR eiel_c_tramos_carreteras.estado = 'M' OR eiel_c_tramos_carreteras.estado = 'R');
ALTER TABLE eiel_c_tramos_carreteras ADD CONSTRAINT dimensionachk CHECK (eiel_c_tramos_carreteras.dimensiona = 'BD' OR
eiel_c_tramos_carreteras.dimensiona = 'MD' OR eiel_c_tramos_carreteras.dimensiona = 'RD');
ALTER TABLE eiel_c_tramos_carreteras ADD CONSTRAINT muy_sinuosochk CHECK (eiel_c_tramos_carreteras.muy_sinuoso = 'SI' OR
eiel_c_tramos_carreteras.muy_sinuoso = 'NO' );
ALTER TABLE eiel_c_tramos_carreteras ADD CONSTRAINT pte_excesivachk CHECK (eiel_c_tramos_carreteras.pte_excesiva = 'SI' OR
eiel_c_tramos_carreteras.pte_excesiva = 'NO' );
ALTER TABLE eiel_c_tramos_carreteras ADD CONSTRAINT fre_estrechchk CHECK (eiel_c_tramos_carreteras.fre_estrech = 'SI' OR
eiel_c_tramos_carreteras.fre_estrech = 'NO' );

create index i_id_tramos_carreteras on "public".eiel_c_tramos_carreteras(id_municipio);

create index i_codprov_tramos_carreteras on "public".eiel_c_tramos_carreteras(codprov);
create index i_codmunic_tramos_carreteras on "public".eiel_c_tramos_carreteras(codmunic);
create index i_cod_carrt_tramos_carreteras on "public".eiel_c_tramos_carreteras(cod_carrt);

CREATE SEQUENCE "public"."seq_eiel_c_tramos_carreteras"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


CREATE TABLE eiel_t_carreteras
(
  id numeric(8,0) NOT NULL,
  id_municipio numeric(5,0) NOT NULL,
  codprov character varying(2) NOT NULL,
  cod_carrt character varying(8) NOT NULL,
  clase_via character varying(3),
  denominacion character varying(100),
  titular_via character varying(2),
  fecha_revision date,
  observ character varying(50),
  bloqueado character varying(32),
  pki numeric(8,3) NOT NULL, -- Kilometro Inicial
  pkf numeric(8,3) NOT NULL, -- Kilometro Final
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_carreteras_pkey PRIMARY KEY (id , revision_actual ),
  CONSTRAINT titular_viachk CHECK (titular_via::text = 'CA'::text OR titular_via::text = 'ES'::text OR titular_via::text = 'MU'::text OR titular_via::text = 'OT'::text OR titular_via::text = 'PR'::text)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_t_carreteras
  OWNER TO postgres;
GRANT ALL ON TABLE eiel_t_carreteras TO postgres;
GRANT SELECT ON TABLE eiel_t_carreteras TO consultas;
COMMENT ON COLUMN eiel_t_carreteras.pki IS 'Kilometro Inicial';
COMMENT ON COLUMN eiel_t_carreteras.pkf IS 'Kilometro Final';


CREATE INDEX i_id_t_carreteras
  ON eiel_t_carreteras
  USING btree
  (id_municipio );


CREATE SEQUENCE "public"."seq_eiel_t_carreteras"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;



-------ALUMBRADO C

CREATE TABLE "public".eiel_c_alum_pl
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,  
  orden_pl character varying(4) ,
  sist_eficiencia_pl character varying(3),
  ah_ener_rfl character varying (2),
  ah_ener_rfi character varying (2),
  estado character varying(1),
  potencia double precision,
   tipo_lampara character varying(2),
  tipo_apoyo character varying(2),
  fecha_inst date,
  observ character varying(100),
  precision_dig character varying (2) ,
  n_lamparas integer,
  altura integer,
  tipo_luminaria character varying (2) ,
  obra_ejec character varying (2) ,
  orden_eea character varying(3),
  orden_cmp character varying(3),
  CONSTRAINT eiel_c_alum_pl_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_alum_pl OWNER TO postgres;
ALTER TABLE eiel_c_alum_pl ADD CONSTRAINT ah_ener_rfichk CHECK (eiel_c_alum_pl.ah_ener_rfi= 'SI' OR eiel_c_alum_pl.ah_ener_rfi = 'NO');
ALTER TABLE eiel_c_alum_pl ADD CONSTRAINT ah_ener_rflchk CHECK (eiel_c_alum_pl.ah_ener_rfl= 'SI' OR eiel_c_alum_pl.ah_ener_rfl = 'NO');
ALTER TABLE eiel_c_alum_pl ADD CONSTRAINT estadochk CHECK (eiel_c_alum_pl.estado= 'B' OR eiel_c_alum_pl.estado = 'E'
 OR eiel_c_alum_pl.estado = 'M'  OR eiel_c_alum_pl.estado = 'R');
create index i_id_alum_pl on "public".eiel_c_alum_pl(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_alum_pl"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


CREATE TABLE "public".eiel_c_alum_eea
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL, 
  orden_eea character varying(3),
  n_circuitos integer,
  estabilizador integer,
  estado character varying(1),
  potencia integer,
  obra_ejec character varying (2) ,
  precision_dig character varying (2) ,
  observ character varying(250),
  fecha_revision date,
  estado_revision integer,
  CONSTRAINT eiel_c_alum_eea_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_alum_eea OWNER TO postgres;


create index i_id_alum_eea on "public".eiel_c_alum_eea(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_alum_eea"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_alum_cmp
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL, 
  potencia_inst integer ,
  potencia_contratada integer,
   n_circuitos numeric(2,0),
 contador character varying(2),
  estado character varying(1),
  obra_ejec character varying (2) ,
  observ character varying(50),
  precision_dig character varying (2) ,
  orden_cmp character varying (3),
  fecha_inst date,
  fecha_revision date,
  estado_revision integer,
  CONSTRAINT eiel_c_alum_cmp_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_alum_cmp OWNER TO postgres;

create index i_id_alum_cmp on "public".eiel_c_alum_cmp(id_municipio);
create index i_codprov_alum_cmp on "public".eiel_c_alum_cmp(codprov);
create index i_codmunic_alum_cmp on "public".eiel_c_alum_cmp(codmunic);

CREATE SEQUENCE "public"."seq_eiel_c_alum_cmp"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


-----VERTEDERO C

CREATE TABLE "public".eiel_c_vt
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_vt character varying (3) NOT NULL,
  observ character varying(50),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_vt_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_vt OWNER TO postgres;
ALTER TABLE eiel_c_vt ADD CONSTRAINT clavechk CHECK (eiel_c_vt.clave= 'VT');

create index i_id_vt on "public".eiel_c_vt(id_municipio);

CREATE SEQUENCE "public"."seq_eiel_c_vt"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;


-----VERTEDERO T

CREATE TABLE "public".eiel_t_vt
(
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  orden_vt character varying (3) NOT NULL,
  tipo character varying(3),
  titular character varying(3),
  gestor character varying(3),
  olores character varying(2),
  humos character varying(2),
  cont_anima character varying(2),
  r_inun character varying(2),
  filtracion character varying(2),
  impacto_v character varying(2),
  frec_averia character varying(2),
  saturacion character varying(2),
 inestable character varying(2),
  otros character varying(2),
  capac_tot numeric(8,0),
  capac_tot_porc numeric(4,0),
  capac_transf numeric(4,0),
  vida_util integer,
  categoria character varying(3),
  actividad character varying(2),
  fecha_apertura numeric(4,0),
  estado character varying(1),
  obra_ejec character varying(2),
  observ character varying(50),
  capac_ampl character varying(2),
  fecha_revision date,
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint, 
  CONSTRAINT eiel_t_vt_pkey PRIMARY KEY (clave, codprov, codmunic, orden_vt, revision_actual)
  
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_t_vt OWNER TO postgres;
ALTER TABLE eiel_t_vt ADD CONSTRAINT clavechk CHECK (eiel_t_vt.clave= 'VT');
ALTER TABLE eiel_t_vt ADD CONSTRAINT tipochk CHECK (eiel_t_vt.tipo= 'VCS' OR eiel_t_vt.tipo= 'VIN' OR
eiel_t_vt.tipo= 'VCC' OR eiel_t_vt.tipo= 'PLV' OR eiel_t_vt.tipo= 'PCE' OR
eiel_t_vt.tipo= 'ISA' OR eiel_t_vt.tipo= 'ICA' OR eiel_t_vt.tipo= 'PTC' OR eiel_t_vt.tipo= 'PTI' OR
eiel_t_vt.tipo= 'EST' OR eiel_t_vt.tipo= 'OTR' );
ALTER TABLE eiel_t_vt ADD CONSTRAINT titularchk CHECK (eiel_t_vt.titular= 'CON' OR eiel_t_vt.titular= 'MCC' OR
eiel_t_vt.titular= 'MCD' OR eiel_t_vt.titular= 'MUC' OR eiel_t_vt.titular= 'MUD' OR
eiel_t_vt.titular= 'OTS');
ALTER TABLE eiel_t_vt ADD CONSTRAINT gestorchk CHECK (eiel_t_vt.gestor= 'CON' OR eiel_t_vt.gestor= 'MCC' OR
eiel_t_vt.gestor= 'MCD' OR eiel_t_vt.gestor= 'MUC' OR eiel_t_vt.gestor= 'MUD' OR
eiel_t_vt.gestor= 'OTS');
ALTER TABLE eiel_t_vt ADD CONSTRAINT oloreschk CHECK (eiel_t_vt.olores= 'SI' OR eiel_t_vt.olores= 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT humoschk CHECK (eiel_t_vt.humos= 'SI' OR eiel_t_vt.humos= 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT cont_animachk CHECK (eiel_t_vt.cont_anima= 'SI' OR eiel_t_vt.cont_anima= 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT r_inunchk CHECK (eiel_t_vt.r_inun= 'SI' OR eiel_t_vt.r_inun= 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT filtracionchk CHECK (eiel_t_vt.filtracion= 'SI' OR eiel_t_vt.filtracion= 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT impacto_vchk CHECK (eiel_t_vt.impacto_v= 'SI' OR eiel_t_vt.impacto_v= 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT frec_averiachk CHECK (eiel_t_vt.frec_averia= 'SI' OR eiel_t_vt.frec_averia= 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT inestablechk CHECK (eiel_t_vt.inestable= 'SI' OR eiel_t_vt.inestable= 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT otroschk CHECK (eiel_t_vt.otros= 'SI' OR eiel_t_vt.otros = 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT capac_amplchk CHECK (eiel_t_vt.capac_ampl= 'SI' OR eiel_t_vt.capac_ampl= 'NO');
ALTER TABLE eiel_t_vt ADD CONSTRAINT estadochk CHECK (eiel_t_vt.estado= 'B' OR eiel_t_vt.estado= 'e' OR
eiel_t_vt.estado= 'M' OR eiel_t_vt.estado= 'R');
ALTER TABLE eiel_t_vt ADD CONSTRAINT categoriachk CHECK (eiel_t_vt.categoria= 'VIN' OR eiel_t_vt.categoria= 'VMN' OR
eiel_t_vt.categoria= 'VMP' OR eiel_t_vt.categoria= 'VRN' OR eiel_t_vt.categoria= 'VRP');
ALTER TABLE eiel_t_vt ADD CONSTRAINT actividadchk CHECK (eiel_t_vt.actividad= 'AB' OR eiel_t_vt.actividad= 'EN' OR
eiel_t_vt.actividad= 'SE' OR eiel_t_vt.actividad= 'SG');
ALTER TABLE eiel_t_vt ADD CONSTRAINT saturacion_vchk CHECK (eiel_t_vt.saturacion= 'SI' OR eiel_t_vt.saturacion= 'NO');


----VERTEDERO TR

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
ALTER TABLE eiel_tr_vt_pobl ADD CONSTRAINT clave_vtchk CHECK (eiel_tr_vt_pobl.clave_vt= 'VT');



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


ALTER TABLE eiel_t_rb ADD CONSTRAINT tipochk CHECK (eiel_t_rb.tipo = 'OG'
OR eiel_t_rb.tipo = 'OT' OR eiel_t_rb.tipo = 'PA' OR eiel_t_rb.tipo = 'PI' OR eiel_t_rb.tipo = 'PL'
OR eiel_t_rb.tipo = 'RN' OR eiel_t_rb.tipo = 'VI');
ALTER TABLE eiel_t_rb ADD CONSTRAINT gestorchk CHECK (eiel_t_rb.gestor = 'CON'
OR eiel_t_rb.gestor = 'MCC' OR eiel_t_rb.gestor = 'MCD' OR eiel_t_rb.gestor = 'MUC' OR eiel_t_rb.gestor = 'MUD'
OR eiel_t_rb.gestor = 'OTS');
ALTER TABLE eiel_t_rb ADD CONSTRAINT periodicidadchk CHECK (eiel_t_rb.periodicidad = 'AL'
OR eiel_t_rb.periodicidad = 'DI' OR eiel_t_rb.periodicidad = 'NO' OR eiel_t_rb.periodicidad = 'OT' OR
eiel_t_rb.periodicidad = 'QU' OR eiel_t_rb.periodicidad = 'SE');
ALTER TABLE eiel_t_rb ADD CONSTRAINT calidadchk CHECK (eiel_t_rb.calidad = 'AD'
OR eiel_t_rb.calidad = 'IN');


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

ALTER TABLE eiel_t_rb_serv ADD CONSTRAINT serv_limp_calleschk CHECK (eiel_t_rb_serv.serv_limp_calles = 'SI'
OR eiel_t_rb_serv.serv_limp_calles = 'NO');

create index i_codprov_t_rb_serv on "public".eiel_t_rb_serv(codprov);
create index i_codmunic_t_rb_serv on "public".eiel_t_rb_serv(codmunic);
create index i_codentidad_rb_serv on "public".eiel_t_rb_serv(codentidad);
create index i_codpoblamiento_rb_serv on "public".eiel_t_rb_serv(codpoblamiento);





---NUCLEO DE POBLACION


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

ALTER TABLE eiel_t_nucl_encuest_1 ADD CONSTRAINT accesibilidadchk CHECK (eiel_t_nucl_encuest_1.accesibilidad = 'IN' OR
eiel_t_nucl_encuest_1.accesibilidad = 'IT' OR eiel_t_nucl_encuest_1.accesibilidad = 'NO');

create index i_codprov_t_nucl_encuest_1 on "public".eiel_t_nucl_encuest_1(codprov);
create index i_codmunic_t_nucl_encuest_1 on "public".eiel_t_nucl_encuest_1(codmunic);
create index i_codentidad_nucl_encuest_1 on "public".eiel_t_nucl_encuest_1(codentidad);
create index i_codpoblamiento_nucl_encuest_1 on "public".eiel_t_nucl_encuest_1(codpoblamiento);



-- Table: eiel_t_nucl_encuest_2

-- DROP TABLE eiel_t_nucl_encuest_2;

CREATE TABLE eiel_t_nucl_encuest_2
(
  codprov character varying(2) NOT NULL,
  codmunic character varying(3) NOT NULL,
  codentidad character varying(4) NOT NULL,
  codpoblamiento character varying(2) NOT NULL,
  aag_caudal character varying(2),
  aag_restri character varying(2),
  aag_contad character varying(2),
  aag_tasa character varying(2),
  aag_instal character varying(4),
  aag_hidran character varying(2),
  aag_est_hi character varying(1),
  aag_valvul character varying(2),
  aag_est_va character varying(1),
  aag_bocasr character varying(2),
  aag_est_bo character varying(1),
  cisterna character varying(2),
  fecha_revision date,
  observ character varying(100),
  estado_revision integer,
  bloqueado character varying(32),
  revision_actual numeric(10,0) NOT NULL DEFAULT 0,
  revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
  CONSTRAINT eiel_t_nucl_encuest_2_pkey PRIMARY KEY (codprov , codmunic , codentidad , codpoblamiento , revision_actual ),
  CONSTRAINT aag_bocasrchk CHECK (aag_bocasr::text = 'IN'::text OR aag_bocasr::text = 'NO'::text OR aag_bocasr::text = 'SF'::text),
  CONSTRAINT aag_caudalchk CHECK (aag_caudal::text = 'IN'::text OR aag_caudal::text = 'NO'::text OR aag_caudal::text = 'SF'::text),
  CONSTRAINT aag_contadchk CHECK (aag_contad::text = 'SI'::text OR aag_contad::text = 'NO'::text),
  CONSTRAINT aag_hidranchk CHECK (aag_hidran::text = 'IN'::text OR aag_hidran::text = 'NO'::text OR aag_hidran::text = 'SF'::text),
  CONSTRAINT aag_restrichk CHECK (aag_restri::text = 'NO'::text OR aag_restri::text = 'RF'::text OR aag_restri::text = 'RM'::text OR aag_restri::text = 'RT'::text),
  CONSTRAINT aag_tasachk CHECK (aag_tasa::text = 'SI'::text OR aag_tasa::text = 'NO'::text),
  CONSTRAINT aag_valvulchk CHECK (aag_valvul::text = 'IN'::text OR aag_valvul::text = 'NO'::text OR aag_valvul::text = 'SF'::text),
  CONSTRAINT cisternahk CHECK (cisterna::text = 'AC'::text OR cisterna::text = 'IN'::text OR cisterna::text = 'MC'::text OR cisterna::text = 'NO'::text OR cisterna::text = 'OT'::text OR cisterna::text = 'RO'::text OR cisterna::text = 'RU'::text OR cisterna::text = 'SE'::text)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE eiel_t_nucl_encuest_2
  OWNER TO postgres;
GRANT ALL ON TABLE eiel_t_nucl_encuest_2 TO postgres;
GRANT SELECT ON TABLE eiel_t_nucl_encuest_2 TO consultas;

-- Index: i_codentidad_nucl_encuest_2

-- DROP INDEX i_codentidad_nucl_encuest_2;

CREATE INDEX i_codentidad_nucl_encuest_2
  ON eiel_t_nucl_encuest_2
  USING btree
  (codentidad );

-- Index: i_codmunic_t_nucl_encuest_2

-- DROP INDEX i_codmunic_t_nucl_encuest_2;

CREATE INDEX i_codmunic_t_nucl_encuest_2
  ON eiel_t_nucl_encuest_2
  USING btree
  (codmunic );

-- Index: i_codpoblamiento_nucl_encuest_2

-- DROP INDEX i_codpoblamiento_nucl_encuest_2;

CREATE INDEX i_codpoblamiento_nucl_encuest_2
  ON eiel_t_nucl_encuest_2
  USING btree
  (codpoblamiento );

-- Index: i_codprov_t_nucl_encuest_2

-- DROP INDEX i_codprov_t_nucl_encuest_2;

CREATE INDEX i_codprov_t_nucl_encuest_2
  ON eiel_t_nucl_encuest_2
  USING btree
  (codprov );



----TABLA PADRON

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




----OTROS SERVICIOS MUNICIPALES

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




------TABLA DE CABILDO, ENTIDAD SINGULAR, POBLAMIENTO Y NÚCLEO ABANDONADO


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


-----NUEVAS CAPAS

CREATE TABLE "public".eiel_c_parroquias
(
  id numeric(8,0) NOT NULL,
   id_municipio numeric(5,0) NOT NULL,
  id_parroquia integer NOT NULL,
  "GEOMETRY" geometry,
  nombre_parroquia character varying(150),
  codmunic character varying (3) NOT NULL,
  codprov character varying (2) NOT NULL,
  fecha_revision date,
  observ character varying(250),
  CONSTRAINT eiel_c_parroquias_pkey PRIMARY KEY (id,id_parroquia,codmunic)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_parroquias OWNER TO postgres;

create index i_id on "public".eiel_c_parroquias(id);
create index i_id_parroquia on "public".eiel_c_parroquias(id_parroquia);
create index i_codmunic_parroquia on "public".eiel_c_parroquias(codmunic);

CREATE SEQUENCE "public"."seq_eiel_c_parroquias"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

CREATE TABLE "public".eiel_c_edificiosing
(
  id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
  id_elemsing integer,
  "GEOMETRY" geometry,
  clave character varying (2) NOT NULL,
  codprov character varying (2) NOT NULL,
  codmunic character varying (3) NOT NULL,
  codentidad character varying (4) NOT NULL,
  codpoblamiento character varying (2) NOT NULL,
  orden_el character varying (3) NOT NULL,
  tipo character varying (2),
  nombre character varying (150),
  obra_ejec character varying (2) ,
  fecha_revision date,
  estado_revision integer,
  observ character varying(250),
  precision_dig character varying (2) ,
  CONSTRAINT eiel_c_edificiosing_pkey PRIMARY KEY (id_elemsing)
     )
WITH (
  OIDS=TRUE
);
ALTER TABLE "public".eiel_c_edificiosing OWNER TO postgres;

create index i_id_edificiosing on "public".eiel_c_edificiosing(id_elemsing);

CREATE SEQUENCE "public"."seq_eiel_c_edificiosing"
    INCREMENT 1  MINVALUE 1
    MAXVALUE 9223372036854775807  START 1
    CACHE 1;

    
-----DROP SEQUENCE "public"."seq_versionAlfa";
CREATE SEQUENCE "public"."seq_versionAlfa"
  INCREMENT 1 MINVALUE 1
  MAXVALUE 9223372036854775807 START 1
  CACHE 1;
ALTER TABLE "public"."seq_versionAlfa" OWNER TO geopista;	

-- Table: versionesalfa

-- DROP TABLE versionesalfa;

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



