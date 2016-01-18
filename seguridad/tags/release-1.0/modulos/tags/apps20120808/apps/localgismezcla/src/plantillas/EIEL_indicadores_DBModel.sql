DROP TABLE eiel_indicadores_m_indicadores;
DROP TABLE eiel_indicadores_m_categorias;

DROP TABLE eiel_indicadores_d_poblacion;
DROP TABLE eiel_indicadores_d_viviendas;
DROP TABLE eiel_indicadores_d_planeam;
DROP TABLE eiel_indicadores_d_captaciones;
DROP TABLE eiel_indicadores_d_potabilizacion;
DROP TABLE eiel_indicadores_d_depositos;

DROP TABLE eiel_indicadores_d_rsaneam;
DROP TABLE eiel_indicadores_d_rdistribucion;
DROP TABLE eiel_indicadores_t_rdistribucion_aux;

DROP TABLE eiel_indicadores_t_rsaneam_aux;
DROP TABLE eiel_indicadores_d_vertidos;
DROP TABLE eiel_indicadores_d_accesibilidad;
DROP TABLE eiel_indicadores_d_pavimentacion;
DROP TABLE eiel_indicadores_d_alumbrado;
DROP TABLE eiel_indicadores_d_comunicaciones;
DROP TABLE eiel_indicadores_d_suministros;
DROP TABLE eiel_indicadores_d_centrosen;
DROP TABLE eiel_indicadores_d_ideporte;
DROP TABLE eiel_indicadores_d_ccultura;
DROP TABLE eiel_indicadores_d_zverde;
DROP TABLE eiel_indicadores_d_csanitario;
DROP TABLE eiel_indicadores_d_servotros;
DROP TABLE eiel_indicadores_d_casistencial;

DROP TABLE eiel_indicadores_d_rblimpieza;
DROP TABLE eiel_indicadores_d_tratamresiduos;

DROP TABLE eiel_indicadores_t_depostos_aux;





SELECT 'DELETE FROM ' || tablename || ';' FROM pg_tables where tablename like 'eiel_indicadores_d%'

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
  nhab_calidadred_B NUMERIC(6,0)  NULL,
  nhab_calidadred_R NUMERIC(6,0)  NULL,
  nhab_calidadred_Ma NUMERIC(6,0)  NULL,
  nest_calidadred_B NUMERIC(6,0)  NULL,
  nest_calidadred_R NUMERIC(6,0)  NULL,
  nest_calidadred_M NUMERIC(6,0)  NULL,
  nviv_saneamautonomo NUMERIC(6,0)  NULL,
  nhab_saneamautonomo NUMERIC(6,0)  NULL,
  nest_saneamautonomo NUMERIC(6,0)  NULL,
  nviv_saneamautonomo_in NUMERIC(6,0)  NULL,
  nhab_saneamautonomo_in NUMERIC(6,0)  NULL,
  nest_saneamautonomo_in NUMERIC(6,0)  NULL,
  nviv_saneam_no NUMERIC(6,0)  NULL,
  nhab_saneam_def NUMERIC(6,0)  NULL,
  nest_saneam_def NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_t_rdistribucion_aux
(
  "GEOMETRY" geometry,
  id_municipio character varying(5),
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
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
  npuntosvertido NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_accesibilidad (
  id SERIAL ,
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
  nnuc_accesibilidadD NUMERIC(6,0)  NULL,
  nhab_accesibilidadD NUMERIC(6,0)  NULL,
  nest_accesibilidadD NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_pavimentacion (
  id SERIAL ,
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
  travesias_longitud_B FLOAT NULL,
  travesias_longitud_R FLOAT NULL,
  travesias_longitud_M FLOAT NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_alumbrado (
  id SERIAL ,
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
  npuntosluz NUMERIC(6,0)  NULL,
  npuntosluz_estado_B NUMERIC(6,0)  NULL,
  npuntosluz_estado_R NUMERIC(6,0)  NULL,
  npuntosluz_estado_M NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_comunicaciones (
  id SERIAL ,
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
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
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_suministros (
  id SERIAL ,
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
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
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
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
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
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
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
  nbibliotecas NUMERIC(6,0)  NULL,
  nteatroscinesaudit NUMERIC(6,0)  NULL,
  sup_ccultura_B FLOAT NULL,
  sup_ccultura_R FLOAT NULL,
  sup_ccultura_M FLOAT NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_zverde (
  id SERIAL ,
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
  sup_pn_B FLOAT NULL,
  sup_pn_R FLOAT NULL,
  sup_pn_M FLOAT NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_csanitario (
  id SERIAL ,
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
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
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
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
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
  ncentros NUMERIC(6,0)  NULL,
  nresidenciasancianos NUMERIC(6,0)  NULL,
  nplazasresidenciasancianos NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);




CREATE TABLE eiel_indicadores_d_rblimpieza (
  id SERIAL ,
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
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
  nnuc_serv_deficit NUMERIC(6,0)  NULL,
  nhab_serv_deficit NUMERIC(6,0)  NULL,
  nest_serv_deficit NUMERIC(6,0)  NULL,
  PRIMARY KEY(id)
);


CREATE TABLE eiel_indicadores_d_tratamresiduos (
  id SERIAL ,
  id_municipio VARCHAR(5),
  "GEOMETRY" geometry,
  codprov VARCHAR(2) NOT NULL,
  codmunic VARCHAR(3) NOT NULL,
  codentidad VARCHAR(4) NOT NULL,
  codpoblamiento VARCHAR(2) NULL,
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


GRANT ALL ON TABLE eiel_indicadores_d_accesibilidad TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_alumbrado TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_captaciones TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_t_captaciones_aux TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_casistencial TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_ccultura TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_centrosen TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_comunicaciones TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_csanitario TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_depositos TO postgres,geopista;

GRANT ALL ON TABLE eiel_indicadores_d_ideporte TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_pavimentacion TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_planeam TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_poblacion TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_potabilizacion TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_rblimpieza TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_d_rdistribucion TO postgres,geopista;
GRANT ALL ON TABLE eiel_indicadores_t_rdistribucion_aux TO postgres,geopista;

GRANT ALL ON TABLE eiel_indicadores_d_rsaneam TO postgres,geopista;
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
GRANT SELECT ON TABLE eiel_indicadores_d_csanitario TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_depositos TO consultas;

GRANT SELECT ON TABLE eiel_indicadores_d_ideporte TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_pavimentacion TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_planeam TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_poblacion TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_potabilizacion TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_rblimpieza TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_rdistribucion TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_t_rdistribucion_aux TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_rsaneam TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_servotros TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_suministros TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_tratamresiduos TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_vertidos TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_viviendas TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_d_zverde TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_m_categorias TO consultas;
GRANT SELECT ON TABLE eiel_indicadores_m_indicadores TO consultas;






--SELECT 'GRANT ALL ON TABLE ' || tablename || ' TO postgres,geopista;' FROM pg_tables where tablename like 'eiel_indicadore%'

--select * from tables order by id_table desc

/* GENERADOR INSERTS TABLES A PARTIR DE TABLAS YA EXISTENTES 
--SELECT 'INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval(\'seq_tables\'),\'' || tablename || '\',5,0);' FROM pg_tables where tablename like 'eiel_indicadore%'
--Revisar Tipo geometría
 */

--select * from tables order by id_table desc;
--delete from tables where id_table=10168;


INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_t_captaciones_aux',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_poblacion',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_accesibilidad',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_alumbrado',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_captaciones',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_casistencial',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_ccultura',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_centrosen',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_comunicaciones',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_csanitario',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_depositos',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_ideporte',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_pavimentacion',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_planeam',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_poblacion',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_potabilizacion',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_rblimpieza',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_rdistribucion',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_rsaneam',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_servotros',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_suministros',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_tratamresiduos',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_vertidos',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_viviendas',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_d_zverde',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_m_categorias',5);
INSERT INTO TABLES(id_table,name,geometrytype) VALUES(nextval('seq_tables'),'eiel_indicadores_m_indicadores',5);


/* GENERADOR INSERTS COLUMNS A PARTIR DE TABLAS YA EXISTENTES y YA INSERTADAS EN LA TABLA TABLES
 * REVISAR where c.table_name like 'eiel_indicadores_d_poblaci%'

SELECT 'INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval(\'seq_columns\'),' || tables.id_table || ',null,\'' || c.column_name || '\','|| COALESCE(c.character_maximum_length,0) || ',' || COALESCE(c.numeric_precision,0) || ',' || COALESCE(c.numeric_scale,0) || ',' || CASE WHEN c.data_type='numeric' THEN '2' WHEN c.data_type='integer' THEN '2'  WHEN c.data_type='character varying' THEN '3' WHEN c.data_type='USER-DEFINED' THEN '1' ELSE c.data_type END || '); --' ||   tables.id_table || ' ' || c.table_name || '.' || c.column_name || ' ' || c.data_type 
as sqlQuotedINSERT
FROM information_schema.columns c, tables
where c.table_name like 'eiel_indicadores_d_poblaci%' 
 and c.table_name=tables.name  
order by c.table_name, c.ordinal_position;

select * from information_schema.tables LIMIT 10
*/




select * from columns where id_table=10181
delete from columns where id_table=10181 and id >=101868;

INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'id',null,8,0,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'id_municipio',5,0,0,3);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'GEOMETRY',0,0,0,1);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'codprov',2,0,0,3);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'codmunic',3,0,0,3);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'codentidad',4,0,0,3);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'codpoblamiento',2,0,0,3);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'numnucleos',0,6,0,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'poblaciontotal',0,8,0,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'poblacionderecho',0,8,0,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'poblaciondiseminado',0,8,0,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'poblacionestacional',0,8,0,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'poblaciondensidad',0,8,2,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'superficiemunicipal',0,9,2,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'superficieurbana',0,9,2,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'superficeresidencial',0,9,2,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'superficieagricolaforestalporcent',0,9,2,2);
INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") VALUES(nextval('seq_columns'),10181,null,'toponimo',100,0,0,3);

