DROP TABLE eiel_indicadores_m_indicadores;
DROP TABLE eiel_indicadores_m_categorias;

DROP TABLE eiel_indicadores_d_poblacion;
DROP TABLE eiel_indicadores_d_viviendas;
DROP TABLE eiel_indicadores_d_planeam;
DROP TABLE eiel_indicadores_d_captaciones;
DROP TABLE eiel_indicadores_d_potabilizacion;
DROP TABLE eiel_indicadores_d_depositos;

DROP TABLE eiel_indicadores_d_rsaneam;
DROP TABLE eiel_indicadores_t_rsaneam_aux;

DROP TABLE eiel_indicadores_d_rdistribucion;
DROP TABLE eiel_indicadores_t_rdistribucion_aux;
DROP TABLE eiel_indicadores_d_vertidos;


DROP TABLE eiel_indicadores_d_accesibilidad;
DROP TABLE eiel_indicadores_d_pavimentacion;
DROP TABLE eiel_indicadores_d_alumbrado;
DROP TABLE eiel_indicadores_d_comunicaciones;
DROP TABLE eiel_indicadores_d_comunicaciones_aux;
DROP TABLE eiel_indicadores_d_suministros;

DROP TABLE eiel_indicadores_d_centrosen;
DROP TABLE eiel_indicadores_d_ideporte;
DROP TABLE eiel_indicadores_d_ccultura;
DROP TABLE eiel_indicadores_d_zverde;
DROP TABLE eiel_indicadores_d_csanitario;
DROP TABLE eiel_indicadores_d_servotros;
DROP TABLE eiel_indicadores_d_casistencial;

DROP TABLE eiel_indicadores_d_rblimpieza;
drop table eiel_indicadores_t_rblimpieza_aux;
DROP TABLE eiel_indicadores_d_tratamresiduos;



--SELECT 'DELETE FROM ' || tablename || ';' FROM pg_tables where tablename like 'eiel_indicadores_d%'

SELECT 'SELECT COUNT(*) FROM ' || tablename || ';' FROM pg_tables where tablename like 'eiel_indicadores_d%'

CREATE TABLE eiel_indicadores_m_categorias (
  idcategoria NUMERIC(6,0)  NOT NULL ,
  idCategoriaPadre NUMERIC(6,0)   NULL,
  categoria VARCHAR(50) NOT NULL,
  descripcion VARCHAR(255) NULL,
  PRIMARY KEY(idcategoria)
);


CREATE TABLE eiel_indicadores_m_indicadores (
  idindicador SERIAL,
  idcategoria NUMERIC(6,0)  NOT NULL,
  indicador VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255) NULL,
  valorMin VARCHAR(50) NULL,
  valorMax VARCHAR(100) NULL,
  codDocIndicador VARCHAR(15) NULL,
  tabla VARCHAR(50) NULL,
  columna VARCHAR(25) NULL,
  mapa character varying(100),
  PRIMARY KEY(idindicador),
  CONSTRAINT eiel_indicadores_m_indicadores_FKIndex1 FOREIGN KEY (idcategoria) REFERENCES eiel_indicadores_m_categorias(idcategoria) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE eiel_indicadores_d_poblacion (
  id SERIAL ,
  toponimo VARCHAR(100),
  id_municipio VARCHAR(5) NOT NULL,
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) ,
  codpoblamiento VARCHAR(2) NULL,
  numnucleos NUMERIC(6,0) NULL,
  poblaciontotal NUMERIC(8,0) NULL,
  poblacionDerecho NUMERIC(8,0) NULL,
  poblacionDiseminado NUMERIC(8,0) NULL,
  poblacionEstacional NUMERIC(8,0) NULL,
  poblacionDensidad NUMERIC(8,2) NULL,
  superficieMunicipal NUMERIC(9,2) NULL,
  superficieUrbana NUMERIC(9,2) NULL,
  superficeResidencial NUMERIC(9,2) NULL,
  superficieAgricolaForestalPorcent NUMERIC(9,2) NULL,
  PRIMARY KEY(id)
);



CREATE TABLE eiel_indicadores_d_viviendas (
  id SERIAL ,
  toponimo VARCHAR(100),
  id_municipio VARCHAR(5) NOT NULL,
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4)NULL,
  codpoblamiento VARCHAR(2) NULL,
  nviviendastotal NUMERIC(8,0) NULL,
  nviviendasnucleos NUMERIC(8,0) NULL,
  nviviendasdiseminado NUMERIC(8,0) NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_planeam (
  id SERIAL ,
  toponimo VARCHAR(100),
  id_municipio VARCHAR(5) NOT NULL,
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4)  NULL,
  codpoblamiento VARCHAR(2) NULL,
  fplan_supramunicipal VARCHAR(12) NULL,
  fplan_pgom VARCHAR(12) NULL,
  normassubsidiariasprov VARCHAR(255) NULL,
  sup_municipal numeric(8,2) NULL,
  sup_urbana NUMERIC(8,2) NULL,
  sup_urbanizable NUMERIC(8,2) NULL,
  sup_nourbanizable NUMERIC(8,2) NULL,
  PRIMARY KEY(id)
);



CREATE TABLE eiel_indicadores_t_captaciones_aux
(
  "GEOMETRY" geometry,
  id_municipio numeric(5,0),
  nombre_oficial character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  aag_caudal character varying(2),
  aag_restri character varying(2),
  habitantes bigint,
  pobestacional bigint
);

CREATE TABLE eiel_indicadores_d_captaciones (
  id SERIAL ,
  toponimo VARCHAR(100),
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4)  NULL,
  codpoblamiento VARCHAR(2) NULL,
  numn_caudalsuf NUMERIC(6,0)  NULL,
  numn_caudalins NUMERIC(6,0)  NULL,
  numhab_cauldalsuf NUMERIC(6,0)  NULL,
  numhab_caudalins NUMERIC(6,0)  NULL,
  numest_caudalsuf NUMERIC(6,0)  NULL,
  numest_caudalins NUMERIC(6,0)  NULL,
  numn_restricF NUMERIC(6,0)  NULL,
  numn_restricM NUMERIC(6,0)  NULL,
  numn_restricN NUMERIC(6,0)  NULL,
  numhab_restricF NUMERIC(6,0)  NULL,
  numhab_restricM NUMERIC(6,0)  NULL,
  numhab_resticN NUMERIC(6,0)  NULL,
  numest_restricF NUMERIC(6,0)  NULL,
  numest_restricM NUMERIC(6,0)  NULL,
  numest_restricN NUMERIC(6,0)  NULL,
  ncaptaciones NUMERIC(6,0)  NULL,
  ncaptacionesB NUMERIC(6,0)  NULL,
  ncaptacionesR NUMERIC(6,0)  NULL,
  ncaptacionesM NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_potabilizacion (
  id SERIAL ,
  toponimo VARCHAR(100),
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4)  NULL,
  codpoblamiento VARCHAR(2) NULL,
  npotab NUMERIC(6,0)  NULL,
  npotab_clorac NUMERIC(6,0)  NULL,
  npotab_basica NUMERIC(6,0)  NULL,
  npotab_otros NUMERIC(6,0)  NULL,
  npercontrol_A NUMERIC(6,0)  NULL,
  npercontrol_M NUMERIC(6,0)  NULL,
  npercontrol_B NUMERIC(6,0)  NULL,
  npercontrol_N NUMERIC(6,0)  NULL,
  npotab_estadoB NUMERIC(6,0)  NULL,
  npotab_estadoR NUMERIC(6,0)  NULL,  
  npotab_estadoM NUMERIC(6,0)  NULL,  
  PRIMARY KEY(id)
);




CREATE TABLE eiel_indicadores_d_depositos (
  id SERIAL ,
  toponimo VARCHAR(100),
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4)  NULL,
  codpoblamiento VARCHAR(2) NULL,
  capacidadTotal FLOAT NULL,
  capacidad_B FLOAT NULL,
  capacidad_R FLOAT NULL,
  capacidad_M FLOAT NULL,
  ndepositosTotal NUMERIC(6,0)  NULL ,
  ndepositos_B NUMERIC(6,0)  NULL ,
  ndepositos_R NUMERIC(6,0)  NULL,
  ndepositos_M NUMERIC(6,0)  NULL,

  PRIMARY KEY(id)
);

/*
CREATE TABLE eiel_indicadores_d_rsaneam_aux
(
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  nombre_oficial character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  saneam_calidad character varying(2),
  saneam_viviendas_sin_conexion integer,
  saneam_viviendas_con_conexion integer,
  rs_longitud_estado_b numeric,
  rs_longitud_estado_r numeric,
  rs_longitud_estado_m numeric,
  rs_longitud_def bigint,
  rsaneam_au_nviviendas bigint,
  habitantessaneamientoautonomo bigint,
  pobestsaneamientoautnomo bigint,
  rsaneam_au_nviviendas_insuficiente bigint,
  habitantessaneamientoautonomo_insuficiente bigint,
  pobestsaneamientoautonomo_insuficiente bigint,
  habitantes bigint,
  pobestacional bigint
);
*/


CREATE TABLE eiel_indicadores_d_rsaneam_aux
(
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  nombre_oficial character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  saneam_calidad character varying(2),
  saneam_viviendas_sin_conexion NUMERIC(6,0),
  saneam_viviendas_con_conexion NUMERIC(6,0),
  rs_longitud_estado_b NUMERIC(9,2),
  rs_longitud_estado_r NUMERIC(9,2),
  rs_longitud_estado_m NUMERIC(9,2),
  rs_longitud_def NUMERIC(9,2),
  rsaneam_au_nviviendas NUMERIC(6,0),
  habitantessaneamientoautonomo NUMERIC(8,0),
  pobestsaneamientoautnomo NUMERIC(8,0),
  rsaneam_au_nviviendas_insuficiente NUMERIC(6,0),
  habitantessaneamientoautonomo_insuficiente NUMERIC(8,0),
  pobestsaneamientoautonomo_insuficiente NUMERIC(8,0),
  habitantes NUMERIC(8,0),
  pobestacional NUMERIC(8,0)
);


CREATE TABLE eiel_indicadores_d_rsaneam (
  id SERIAL ,
  toponimo VARCHAR(100),
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4)  NULL,
  codpoblamiento VARCHAR(2) NULL,
  nnucleos_calidadred_B NUMERIC(6,0)  NULL,
  nnucleos_calidadred_R NUMERIC(6,0)  NULL,
  nnucleos_calidadred_M NUMERIC(6,0)  NULL,
  nhab_calidadred_B NUMERIC(8,0)  NULL,
  nhab_calidadred_R NUMERIC(8,0)  NULL,
  nhab_calidadred_Ma NUMERIC(8,0)  NULL,
  nest_calidadred_B NUMERIC(8,0)  NULL,
  nest_calidadred_R NUMERIC(8,0)  NULL,
  nest_calidadred_M NUMERIC(8,0)  NULL,
  nviv_saneamautonomo NUMERIC(6,0)  NULL,
  nhab_saneamautonomo NUMERIC(8,0)  NULL,
  nest_saneamautonomo NUMERIC(8,0)  NULL,
  nviv_saneamautonomo_in NUMERIC(6,0)  NULL,
  nhab_saneamautonomo_in NUMERIC(8,0)  NULL,
  nest_saneamautonomo_in NUMERIC(8,0)  NULL,

  saneam_viviendas_con_conexion NUMERIC(6,0)  NULL,
  saneam_viviendas_sin_conexion NUMERIC(6,0)  NULL,
  rs_longitud_estado_B NUMERIC(8,2)  NULL,
  rs_longitud_estado_R NUMERIC(8,2)  NULL,
  rs_longitud_estado_M NUMERIC(8,2)  NULL,
  rs_longitud_def NUMERIC(8,2)  NULL,

  nviv_saneam_no NUMERIC(6,0)  NULL,
  nhab_saneam_def NUMERIC(8,0)  NULL,
  nest_saneam_def NUMERIC(8,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_t_rdistribucion_aux
(
  "GEOMETRY" geometry,
  id_municipio character varying (5),
  nombre_oficial character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  rdistrib_calidad character varying(2),
  rdistrib_viviendas_sin_conexion integer,
  rd_longitud_estado_B numeric,
  rd_longitud_estado_R numeric,
  rd_longitud_estado_M numeric,
  consumoinvierno bigint,
  consumoverano bigint,
  rdistrib_au_nviviendas bigint,
  habitantesabastecimientoautonomo bigint,
  pobestabastecimientoautnomo bigint,
  habitantes bigint,
  pobestacional bigint
);


CREATE TABLE eiel_indicadores_d_rdistribucion
(
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  numnucleos bigint,
  numnucleoscalidadrd_b bigint,
  numnucleoscalidadrd_r bigint,
  numnucleoscalidadrd_m bigint,
  habitantescalidadrd_b numeric,
  habitantescalidadrd_r numeric,
  habitantescalidadrd_m numeric,
  pobestacionalcalidadrd_b numeric,
  pobestacionalcalidadrd_r numeric,
  pobestacionalcalidadrd_m numeric,
  rd_longitud_estado_b numeric,
  rd_longitud_estado_r numeric,
  rd_longitud_estado_m numeric,
  consumoinvierno numeric,
  consumoverano numeric,
  rdistrib_viviendas_sin_conexion bigint,
  rdistrib_viviendas_au numeric,
  rdistrib_habitantes_au numeric,
  rdistrib_pobest_au numeric,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_vertidos (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  npuntosvertido NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_accesibilidad (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  nnuc_accesibilidadD NUMERIC(6,0)  NULL,
  nhab_accesibilidadD NUMERIC(6,0)  NULL,
  nest_accesibilidadD NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_pavimentacion
(
  id serial NOT NULL,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  travesias_longitud_b double precision,
  travesias_longitud_r double precision,
  travesias_longitud_m double precision,
  travesias_longitud_np double precision,
  travesias_longitud_e double precision,
  carreteras_longitud_b double precision,
  carreteras_longitud_r double precision,
  carreteras_longitud_m double precision,
  carreteras_longitud_np double precision,
  carreteras_longitud_e double precision,
  calles_longitud_b double precision,
  calles_longitud_r double precision,
  calles_longitud_m double precision,
  calles_longitud_np double precision,
  calles_longitud_e double precision,
  plazas_longitud_b double precision,
  plazas_longitud_r double precision,
  plazas_longitud_m double precision,
  plazas_longitud_np double precision,
  plazas_longitud_e double precision,
  otros_longitud_b double precision,
  otros_longitud_r double precision,
  otros_longitud_m double precision,
  otros_longitud_np double precision,
  otros_longitud_e double precision,
  CONSTRAINT eiel_indicadores_d_pavimentacion_pkey PRIMARY KEY (id)
);


CREATE TABLE eiel_indicadores_d_alumbrado (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  potenciaPuntosLuz NUMERIC(9,2)  NULL,
  npuntosluz_ahorro_rlf NUMERIC(6,0)  NULL,
  npuntosluz_estado_B NUMERIC(6,0)  NULL,
  npuntosluz_estado_R NUMERIC(6,0)  NULL,
  npuntosluz_estado_M NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_comunicaciones (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  nnuc_serv_telefono NUMERIC(6,0)  NULL,
  nhab_serv_telefono NUMERIC(6,0)  NULL,
  nest_serv_telefono NUMERIC(6,0)  NULL,
  nnuc_serv_telefono_NO NUMERIC(6,0)  NULL,
  nhab_serv_telefono_NO NUMERIC(6,0)  NULL,
  nest_serv_telefono_NO NUMERIC(6,0)  NULL,
  nnuc_tv_B NUMERIC(6,0)  NULL,
  nhab_tv_B NUMERIC(6,0)  NULL,
  nest_tv_B NUMERIC(6,0)  NULL,
  nnuc_tv_R NUMERIC(6,0)  NULL,
  nhab_tv_R NUMERIC(6,0)  NULL,
  nest_tv_R NUMERIC(6,0)  NULL,
  nnuc_tv_M NUMERIC(6,0)  NULL,
  nhab_tv_M NUMERIC(6,0)  NULL,
  nest_tv_M NUMERIC(6,0)  NULL,
  nnuc_tv_N NUMERIC(6,0)  NULL,
  nhab_tv_N NUMERIC(6,0)  NULL,
  nest_tv_N NUMERIC(6,0)  NULL,
  nnuc_internet_ba_S NUMERIC(6,0)  NULL, 
  PRIMARY KEY(id)
);

CREATE TABLE eiel_indicadores_d_comunicaciones_aux
(
  "GEOMETRY" geometry,
  id_municipio numeric(5,0),
  nombre_oficial character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  tv_ant character varying(1),
  tv_ca character varying(1),
  tm_gsm character varying(1),
  tm_umts character varying(1),
  tm_gprs character varying(1),
  internet_ba CHARACTER varying(1),
  electric character varying(1),
  gas character varying(1),
  habitantes integer,
  pobestacional integer
);

CREATE TABLE eiel_indicadores_d_suministros (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  nnuc_serv_elec_B NUMERIC(6,0)  NULL,
  nhab_serv_elec_B NUMERIC(6,0)  NULL,
  nest_serv_elec_B NUMERIC(6,0)  NULL,
  nnuc_serv_elec_R NUMERIC(6,0)  NULL,
  nhab_serv_elec_R NUMERIC(6,0)  NULL,
  nest_serv_elect_R NUMERIC(6,0)  NULL,
  nnuc_serv_elec_M NUMERIC(6,0)  NULL,
  nhab_serv_elec_M NUMERIC(6,0)  NULL,
  nest_serv_elec_M NUMERIC(6,0)  NULL,
  nnuc_serv_elec_N NUMERIC(6,0)  NULL,
  nhab_serv_elec_N NUMERIC(6,0)  NULL,
  nest_serv_elec_N NUMERIC(6,0)  NULL,
  nnuc_serv_gas_B NUMERIC(6,0)  NULL,
  nhab_serv_gas_B NUMERIC(6,0)  NULL,
  nest_serv_gas_B NUMERIC(6,0)  NULL,
  nnuc_serv_gas_R NUMERIC(6,0)  NULL,
  nhab_serv_gas_R NUMERIC(6,0)  NULL,
  nest_serv_gas_R NUMERIC(6,0)  NULL,
  nnuc_serv_gas_M NUMERIC(6,0)  NULL,
  nhab_serv_gas_M NUMERIC(6,0)  NULL,
  nest_serv_gas_M NUMERIC(6,0)  NULL,
  nnuc_serv_gas_N NUMERIC(6,0)  NULL,
  nhab_serv_gas_N NUMERIC(6,0)  NULL,
  nest_serv_gas_N NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_centrosen (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  guarderia_ncen NUMERIC(6,0)  NULL,
  guarderia_capacidad NUMERIC(6,0)  NULL,
  einfantil_ncen NUMERIC(6,0)  NULL,
  einfantil_plazas NUMERIC(6,0)  NULL,
  einfantil_nalumnos NUMERIC(6,0)  NULL,
  primaria_ncen NUMERIC(6,0)  NULL,
  primaria_plazas NUMERIC(6,0)  NULL,
  primaria_nalumnos NUMERIC(6,0)  NULL,
  secundaria_ncen NUMERIC(6,0)  NULL,
  secundaria_plazas NUMERIC(6,0)  NULL,
  secundaria_nalumnos NUMERIC(6,0)  NULL,
  fp_ncen NUMERIC(6,0)  NULL,
  fp_plazas NUMERIC(6,0)  NULL,
  fp_nalumnos NUMERIC(6,0)  NULL,
  guarderia_sup_cubierta_B FLOAT NULL,
  guarderia_sup_cubierta_R FLOAT NULL,
  guarderia_sup_cubierta_M FLOAT NULL,
  guarderia_sup_alibre_B FLOAT NULL,
  guarderia_sup_alibre_R FLOAT NULL,
  guarderia_sup_alibre_M FLOAT NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_ideporte (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  npolideportiv NUMERIC(6,0)  NULL,
  npolideportiv_alibre NUMERIC(6,0)  NULL,
  npiscinas NUMERIC(6,0)  NULL,
  sup_alibre_B FLOAT NULL,
  sup_alibre_R FLOAT NULL,
  sup_alibre_M FLOAT NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_ccultura (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  nbibliotecas NUMERIC(6,0)  NULL,
  nteatroscinesaudit NUMERIC(6,0)  NULL,
  sup_ccultura_B FLOAT NULL,
  sup_ccultura_R FLOAT NULL,
  sup_ccultura_M FLOAT NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_zverde (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  sup_pn_B FLOAT NULL,
  sup_pn_R FLOAT NULL,
  sup_pn_M FLOAT NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_csanitario (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  nconsultorioslocal NUMERIC(6,0)  NULL,
  ncentrossalud NUMERIC(6,0)  NULL,
  ncentrosurg NUMERIC(6,0)  NULL,
  nhospital NUMERIC(6,0)  NULL,
  nhospitalgeri NUMERIC(6,0)  NULL,
  npsiquiatricos NUMERIC(6,0)  NULL,
  ncamashospital NUMERIC(6,0)  NULL,
  nnuc_sincsanitario NUMERIC(6,0)  NULL,
  nhab_sincsanitario NUMERIC(6,0)  NULL,
  nest_sincsanitario NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);



CREATE TABLE eiel_indicadores_d_servotros (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  nrecintosferiales NUMERIC(6,0)  NULL,
  nmataderos NUMERIC(6,0)  NULL,
  ncementarios NUMERIC(6,0)  NULL,
  ncementerios_saturacionelevada NUMERIC(6,0)  NULL,
  bomberos_plantilla NUMERIC(6,0)  NULL,
  bomberos_nvehiculos NUMERIC(6,0)  NULL,
  pcivil_plantilla NUMERIC(6,0)  NULL,
  pcivil_nvehiculos NUMERIC(6,0)  NULL,
  cconsistorial_sup_cubierta_B NUMERIC(6,0)  NULL,
  cconsistorial_sup_cubierta_R NUMERIC(6,0)  NULL,
  cconsistorial_sup_cubierta_M NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_casistencial (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),  ncentros NUMERIC(6,0)  NULL,
  nresidenciasancianos NUMERIC(6,0)  NULL,
  nplazasresidenciasancianos NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);




CREATE TABLE eiel_indicadores_d_rblimpieza (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  nnuc_rbl_NO NUMERIC(6,0)  NULL,
  nhab_rbl_NO NUMERIC(6,0)  NULL,
  nest_rbl_NO NUMERIC(6,0)  NULL,
  nnuc_rbl_period_ME NUMERIC(6,0)  NULL,
  nhab_rbl_period_ME NUMERIC(6,0)  NULL,
  nest_rble_period_ME NUMERIC(6,0)  NULL,
  nnuc_rbl_period_BA NUMERIC(6,0)  NULL,
  nhab_rbl_period_BA NUMERIC(6,0)  NULL,
  nest_rbl_period_BA NUMERIC(6,0)  NULL,
  nnuc_rbl_calidad_AD NUMERIC(6,0)  NULL,
  nhab_rbl_calidad_AD NUMERIC(6,0)  NULL,
  nest_rbl_calidad_AD NUMERIC(6,0)  NULL,
  nnuc_rbl_calidad_IN NUMERIC(6,0)  NULL,
  nhab_rbl_calidad_IN NUMERIC(6,0)  NULL,
  nest_rbl_calidad_IN NUMERIC(6,0)  NULL,
  nnuc_rb_selectiva_SI NUMERIC(6,0)  NULL,
  nhab_rb_selectiva_SI NUMERIC(6,0)  NULL,
  nest_rb_selectiva_SI NUMERIC(6,0)  NULL,
  nnuc_rb_selectiva_NO NUMERIC(6,0)  NULL,
  nhab_rb_selectiva_NO NUMERIC(6,0)  NULL,
  nest_rb_selectiva_NO NUMERIC(6,0)  NULL,
  nnuc_limp_SI NUMERIC(6,0)  NULL,
  nhab_limp_SI NUMERIC(6,0)  NULL,
  nest_limp_SI NUMERIC(6,0)  NULL,
  nnuc_limp_NO NUMERIC(6,0)  NULL,
  nhab_limp_NO NUMERIC(6,0)  NULL,
  nest_limp_NO NUMERIC(6,0)  NULL,
  nviv_serv_deficit NUMERIC(6,0)  NULL,
  nhab_serv_deficit NUMERIC(6,0)  NULL,
  nest_serv_deficit NUMERIC(6,0)  NULL,
  n_contenedores  NUMERIC(6,0)  NULL,
  nnuc_rbl_period_A NUMERIC(6,0)  NULL,
  nhab_rbl_period_A NUMERIC(6,0)  NULL,
  nest_rbl_period_A NUMERIC(6,0)  NULL,  
  PRIMARY KEY(id)
);



CREATE TABLE eiel_indicadores_t_rblimpieza_aux
(
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  nombre_oficial character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  serv_limp_calles character varying(2),
  srb_viviendas_afec integer,
  srb_pob_res_afect integer,
  srb_pob_est_afect integer,
  tipo character varying(2),
  periodicidad character varying(2),
  calidad character varying(2),
  n_contenedores  NUMERIC(6,0)  NULL,
  habitantes integer,
  pobestacional integer
);

CREATE TABLE eiel_indicadores_d_tratamresiduos (
  id SERIAL ,
  "GEOMETRY" geometry,
  id_municipio character varying(5),
  toponimo character varying(50),
  codprov character varying(2),
  codmunic character varying(3),
  codentidad character varying(4),
  codpoblamiento character varying(2),
  nvert_controlado_B NUMERIC(6,0)  NULL,
  nvert_controlado_R NUMERIC(6,0)  NULL,
  nvert_controlado_M NUMERIC(6,0)  NULL,
  nvert_controlado_NO NUMERIC(6,0)  NULL,
  nvert_indust_B NUMERIC(6,0)  NULL,
  nvert_indust_R NUMERIC(6,0)  NULL,
  nvert_indust_M NUMERIC(6,0)  NULL,
  nvert_controlado_gest_P NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);

--SELECT 'GRANT ALL ON TABLE ' || tablename || ' TO postgres,geopista;' FROM pg_tables where tablename like 'eiel_indicadores_%'

GRANT ALL ON TABLE eiel_indicadores_d_accesibilidad TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_alumbrado TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_captaciones TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_t_captaciones_aux TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_casistencial TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_ccultura TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_centrosen TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_comunicaciones TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_comunicaciones_aux TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_csanitario TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_depositos TO postgres,geopista;

GRANT ALL ON TABLE eiel_indicadores_d_ideporte TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_pavimentacion TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_planeam TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_poblacion TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_potabilizacion TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_rblimpieza TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_t_rblimpieza_aux TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_rdistribucion TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_t_rdistribucion_aux TO postgres,geopista;

GRANT ALL ON TABLE eiel_indicadores_d_rsaneam TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_rsaneam_aux TO postgres,geopista;

GRANT ALL ON TABLE eiel_indicadores_d_servotros TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_suministros TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_tratamresiduos TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_vertidos TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_viviendas TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_zverde TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_m_categorias TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_m_indicadores TO postgres,geopista;
GRANT SELECT ON TABLE eiel_indicadores_d_accesibilidad TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_alumbrado TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_captaciones TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_t_captaciones_aux TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_casistencial TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_ccultura TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_centrosen TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_comunicaciones TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_comunicaciones_aux TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_csanitario TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_depositos TO consultas;

GRANT SELECT ON TABLE eiel_indicadores_d_ideporte TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_pavimentacion TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_planeam TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_poblacion TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_potabilizacion TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_rblimpieza TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_t_rblimpieza_aux TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_rdistribucion TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_t_rdistribucion_aux TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_rsaneam TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_rsaneam_aux TO consultas;

GRANT SELECT ON TABLE eiel_indicadores_d_servotros TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_suministros TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_tratamresiduos TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_vertidos TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_viviendas TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_zverde TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_m_categorias TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_m_indicadores TO consultas;



--Índices

--SELECT 'CREATE INDEX ' || tablename || '_i_codprovmunic ON ' || tablename ||'(codprov, codmunic);' FROM pg_tables where tablename like 'eiel_indicadores_d%';

CREATE INDEX eiel_indicadores_d_accesibilidad_i_codprovmunic ON eiel_indicadores_d_accesibilidad(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_alumbrado_i_codprovmunic ON eiel_indicadores_d_alumbrado(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_captaciones_i_codprovmunic ON eiel_indicadores_d_captaciones(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_casistencial_i_codprovmunic ON eiel_indicadores_d_casistencial(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_ccultura_i_codprovmunic ON eiel_indicadores_d_ccultura(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_centrosen_i_codprovmunic ON eiel_indicadores_d_centrosen(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_comunicaciones_i_codprovmunic ON eiel_indicadores_d_comunicaciones(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_csanitario_i_codprovmunic ON eiel_indicadores_d_csanitario(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_depositos_i_codprovmunic ON eiel_indicadores_d_depositos(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_ideporte_i_codprovmunic ON eiel_indicadores_d_ideporte(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_pavimentacion_i_codprovmunic ON eiel_indicadores_d_pavimentacion(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_planeam_i_codprovmunic ON eiel_indicadores_d_planeam(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_poblacion_i_codprovmunic ON eiel_indicadores_d_poblacion(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_potabilizacion_i_codprovmunic ON eiel_indicadores_d_potabilizacion(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_rblimpieza_i_codprovmunic ON eiel_indicadores_d_rblimpieza(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_rdistribucion_i_codprovmunic ON eiel_indicadores_d_rdistribucion(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_rsaneam_i_codprovmunic ON eiel_indicadores_d_rsaneam(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_rsaneam_aux_i_codprovmunic ON eiel_indicadores_d_rsaneam_aux(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_servotros_i_codprovmunic ON eiel_indicadores_d_servotros(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_suministros_i_codprovmunic ON eiel_indicadores_d_suministros(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_tratamresiduos_i_codprovmunic ON eiel_indicadores_d_tratamresiduos(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_vertidos_i_codprovmunic ON eiel_indicadores_d_vertidos(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_viviendas_i_codprovmunic ON eiel_indicadores_d_viviendas(codprov, codmunic);
CREATE INDEX eiel_indicadores_d_zverde_i_codprovmunic ON eiel_indicadores_d_zverde(codprov, codmunic);


INSERT INTO eiel_indicadores_m_categorias VALUES (1, NULL, 'I. Población, vivienda y planeamiento', NULL);
INSERT INTO eiel_indicadores_m_categorias VALUES (2, NULL, 'II. Ciclo del agua', NULL);
INSERT INTO eiel_indicadores_m_categorias VALUES (3, NULL, 'III. Infraestructuras básicas', NULL);
INSERT INTO eiel_indicadores_m_categorias VALUES (4, NULL, 'IV. Recogida y tratamiento de residuos urbanos', NULL);
INSERT INTO eiel_indicadores_m_categorias VALUES (5, NULL, 'V. Equipamientos educativos y culturales', NULL);
INSERT INTO eiel_indicadores_m_categorias VALUES (6, NULL, 'VI. Equipamientos sanitarios y asistenciales', NULL);
INSERT INTO eiel_indicadores_m_categorias VALUES (7, NULL, 'VII. Otros servicios', NULL);

INSERT INTO eiel_indicadores_m_indicadores VALUES (1, 1, 'I. Población, vivienda y planeamiento', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_poblacion', NULL, 'EIEL_Indicadores_I_Poblacion');
INSERT INTO eiel_indicadores_m_indicadores VALUES (2, 3, 'III.Infraestrucutras-Alumbrado', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_alumbrado', NULL, 'EIEL_Indicadores III_I Alumbrado');
INSERT INTO eiel_indicadores_m_indicadores VALUES (3, 2, 'II. Red de saneamiento', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_rsaneam', NULL, 'EIEL_Indicadores_II_Red de saneamiento');
INSERT INTO eiel_indicadores_m_indicadores VALUES (6, 2, 'II. Captaciones', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_captaciones', NULL, 'EIEL_Indicadores_II_Captaciones');
INSERT INTO eiel_indicadores_m_indicadores VALUES (7, 2, 'II. Depósitos', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_depositos', NULL, 'EIEL_Indicadores_II_Depositos');
INSERT INTO eiel_indicadores_m_indicadores VALUES (8, 2, 'II. Potabilización', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_potabilizacion', NULL, 'EIEL_Indicadores_II_Potabilizacion');
INSERT INTO eiel_indicadores_m_indicadores VALUES (9, 2, 'II. Red de distribución', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_rdistribucion', NULL, 'EIEL_Indicadores_II_RedDistribucion');
INSERT INTO eiel_indicadores_m_indicadores VALUES (10, 3, 'III. Comunicaciones', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_comunicaciones', NULL, 'EIEL_Indicadores_III_Comunicaciones');


INSERT INTO eiel_indicadores_m_indicadores VALUES (11, 3, 'III. Suministros', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_suministros', NULL, 'EIEL_Indicadores_III_Suministros');
INSERT INTO eiel_indicadores_m_indicadores VALUES (13, 4, 'IV. Recogida de basuras y limpieza', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_rblimpieza', NULL, 'EIEL_Indicadores_IV_RecogidaBasurasLimpieza');
INSERT INTO eiel_indicadores_m_indicadores VALUES (14, 5, 'V. Equipamientos educativos, deportivos, culturales y recreativos', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_centrosen', NULL, 'EIEL_Indicadores_V_CEnsenanzaDeporteCultura');
INSERT INTO eiel_indicadores_m_indicadores VALUES (15, 6, 'VI. Equipamientos sanitarios y asistenciales', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_csanitario', NULL, 'EIEL_Indicadores_VI_SanitarioAsistencial');
INSERT INTO eiel_indicadores_m_indicadores VALUES (16, 7, 'VII. Otros servicios', NULL, NULL, NULL, NULL, 'eiel_indicadores_d_servotros', NULL, 'EIEL_Indicadores_VII_OtrosServicios');


ALTER TABLE eiel_indicadores_m_indicadores_idindicador_seq RENAME TO seq_eiel_indicadores_m_indicadores;
ALTER TABLE eiel_indicadores_d_poblacion_id_seq RENAME TO seq_eiel_indicadores_d_poblacion;
ALTER TABLE eiel_indicadores_d_viviendas_id_seq RENAME TO seq_eiel_indicadores_d_viviendas;
ALTER TABLE eiel_indicadores_d_planeam_id_seq RENAME TO seq_eiel_indicadores_d_planeam;
ALTER TABLE eiel_indicadores_d_captaciones_id_seq RENAME TO seq_eiel_indicadores_d_captaciones;
ALTER TABLE eiel_indicadores_d_potabilizacion_id_seq RENAME TO seq_eiel_indicadores_d_potabilizacion;
ALTER TABLE eiel_indicadores_d_depositos_id_seq RENAME TO seq_eiel_indicadores_d_depositos;
ALTER TABLE eiel_indicadores_d_rsaneam_id_seq RENAME TO seq_eiel_indicadores_d_rsaneam;
ALTER TABLE eiel_indicadores_d_rdistribucion_id_seq RENAME TO seq_eiel_indicadores_d_rdistribucion;
ALTER TABLE eiel_indicadores_d_vertidos_id_seq RENAME TO seq_eiel_indicadores_d_vertidos;
ALTER TABLE eiel_indicadores_d_accesibilidad_id_seq RENAME TO seq_eiel_indicadores_d_accesibilidad;
ALTER TABLE eiel_indicadores_d_pavimentacion_id_seq RENAME TO seq_eiel_indicadores_d_pavimentacion;
ALTER TABLE eiel_indicadores_d_alumbrado_id_seq RENAME TO seq_eiel_indicadores_d_alumbrado;
ALTER TABLE eiel_indicadores_d_comunicaciones_id_seq RENAME TO seq_eiel_indicadores_d_comunicaciones;
ALTER TABLE eiel_indicadores_d_suministros_id_seq RENAME TO seq_eiel_indicadores_d_suministros;
ALTER TABLE eiel_indicadores_d_centrosen_id_seq RENAME TO seq_eiel_indicadores_d_centrosen;
ALTER TABLE eiel_indicadores_d_ideporte_id_seq RENAME TO seq_eiel_indicadores_d_ideporte;
ALTER TABLE eiel_indicadores_d_ccultura_id_seq RENAME TO seq_eiel_indicadores_d_ccultura;
ALTER TABLE eiel_indicadores_d_zverde_id_seq RENAME TO seq_eiel_indicadores_d_zverde;
ALTER TABLE eiel_indicadores_d_csanitario_id_seq RENAME TO seq_eiel_indicadores_d_csanitario;
ALTER TABLE eiel_indicadores_d_servotros_id_seq RENAME TO seq_eiel_indicadores_d_servotros;
ALTER TABLE eiel_indicadores_d_casistencial_id_seq RENAME TO seq_eiel_indicadores_d_casistencial;
ALTER TABLE eiel_indicadores_d_rblimpieza_id_seq RENAME TO seq_eiel_indicadores_d_rblimpieza;
ALTER TABLE eiel_indicadores_d_tratamresiduos_id_seq RENAME TO seq_eiel_indicadores_d_tratamresiduos;