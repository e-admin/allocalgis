--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.8
-- Dumped by pg_dump version 9.2.3
-- Started on 2013-04-26 11:54:13

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = true;

--
-- TOC entry 540 (class 1259 OID 77808707)
-- Name: eiel_t1_saneam_ed; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t1_saneam_ed (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_ed character varying(4) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'ED'::text)),
    CONSTRAINT proc_cm_1chk CHECK ((((((proc_cm_1)::text = 'CG'::text) OR ((proc_cm_1)::text = 'DS'::text)) OR ((proc_cm_1)::text = 'OT'::text)) OR ((proc_cm_1)::text = 'NO'::text))),
    CONSTRAINT proc_cm_2chk CHECK ((((((proc_cm_2)::text = 'CG'::text) OR ((proc_cm_2)::text = 'DS'::text)) OR ((proc_cm_2)::text = 'OT'::text)) OR ((proc_cm_2)::text = 'NO'::text))),
    CONSTRAINT proc_cm_3chk CHECK ((((((proc_cm_3)::text = 'CG'::text) OR ((proc_cm_3)::text = 'DS'::text)) OR ((proc_cm_3)::text = 'OT'::text)) OR ((proc_cm_3)::text = 'NO'::text))),
    CONSTRAINT trat_av_1chk CHECK ((((((((((trat_av_1)::text = 'CL'::text) OR ((trat_av_1)::text = 'FA'::text)) OR ((trat_av_1)::text = 'FM'::text)) OR ((trat_av_1)::text = 'OT'::text)) OR ((trat_av_1)::text = 'OZ'::text)) OR ((trat_av_1)::text = 'UN'::text)) OR ((trat_av_1)::text = 'UT'::text)) OR ((trat_av_1)::text = 'NO'::text))),
    CONSTRAINT trat_av_2chk CHECK ((((((((((trat_av_2)::text = 'CL'::text) OR ((trat_av_2)::text = 'FA'::text)) OR ((trat_av_2)::text = 'FM'::text)) OR ((trat_av_2)::text = 'OT'::text)) OR ((trat_av_2)::text = 'OZ'::text)) OR ((trat_av_2)::text = 'UN'::text)) OR ((trat_av_2)::text = 'UT'::text)) OR ((trat_av_2)::text = 'NO'::text))),
    CONSTRAINT trat_av_3chk CHECK ((((((((((trat_av_3)::text = 'CL'::text) OR ((trat_av_3)::text = 'FA'::text)) OR ((trat_av_3)::text = 'FM'::text)) OR ((trat_av_3)::text = 'OT'::text)) OR ((trat_av_3)::text = 'OZ'::text)) OR ((trat_av_3)::text = 'UN'::text)) OR ((trat_av_3)::text = 'UT'::text)) OR ((trat_av_3)::text = 'NO'::text))),
    CONSTRAINT trat_ld_1chk CHECK (((((((((trat_ld_1)::text = 'CO'::text) OR ((trat_ld_1)::text = 'DA'::text)) OR ((trat_ld_1)::text = 'DN'::text)) OR ((trat_ld_1)::text = 'EC'::text)) OR ((trat_ld_1)::text = 'OT'::text)) OR ((trat_ld_1)::text = 'TT'::text)) OR ((trat_ld_1)::text = 'NO'::text))),
    CONSTRAINT trat_ld_2chk CHECK (((((((((trat_ld_2)::text = 'CO'::text) OR ((trat_ld_2)::text = 'DA'::text)) OR ((trat_ld_2)::text = 'DN'::text)) OR ((trat_ld_2)::text = 'EC'::text)) OR ((trat_ld_2)::text = 'OT'::text)) OR ((trat_ld_2)::text = 'TT'::text)) OR ((trat_ld_2)::text = 'NO'::text))),
    CONSTRAINT trat_ld_3chk CHECK (((((((((trat_ld_3)::text = 'CO'::text) OR ((trat_ld_3)::text = 'DA'::text)) OR ((trat_ld_3)::text = 'DN'::text)) OR ((trat_ld_3)::text = 'EC'::text)) OR ((trat_ld_3)::text = 'OT'::text)) OR ((trat_ld_3)::text = 'TT'::text)) OR ((trat_ld_3)::text = 'NO'::text))),
    CONSTRAINT trat_pr_1chk CHECK (((((((((trat_pr_1)::text = 'DC'::text) OR ((trat_pr_1)::text = 'DG'::text)) OR ((trat_pr_1)::text = 'FQ'::text)) OR ((trat_pr_1)::text = 'FS'::text)) OR ((trat_pr_1)::text = 'LN'::text)) OR ((trat_pr_1)::text = 'OT'::text)) OR ((trat_pr_1)::text = 'NO'::text))),
    CONSTRAINT trat_pr_2chk CHECK (((((((((trat_pr_2)::text = 'DC'::text) OR ((trat_pr_2)::text = 'DG'::text)) OR ((trat_pr_2)::text = 'FQ'::text)) OR ((trat_pr_2)::text = 'FS'::text)) OR ((trat_pr_2)::text = 'LN'::text)) OR ((trat_pr_2)::text = 'OT'::text)) OR ((trat_pr_2)::text = 'NO'::text))),
    CONSTRAINT trat_pr_3chk CHECK (((((((((trat_pr_3)::text = 'DC'::text) OR ((trat_pr_3)::text = 'DG'::text)) OR ((trat_pr_3)::text = 'FQ'::text)) OR ((trat_pr_3)::text = 'FS'::text)) OR ((trat_pr_3)::text = 'LN'::text)) OR ((trat_pr_3)::text = 'OT'::text)) OR ((trat_pr_3)::text = 'NO'::text))),
    CONSTRAINT trat_sc_1chk CHECK ((((((((((((((trat_sc_1)::text = 'CB'::text) OR ((trat_sc_1)::text = 'ES'::text)) OR ((trat_sc_1)::text = 'FA'::text)) OR ((trat_sc_1)::text = 'FV'::text)) OR ((trat_sc_1)::text = 'IR'::text)) OR ((trat_sc_1)::text = 'LA'::text)) OR ((trat_sc_1)::text = 'LB'::text)) OR ((trat_sc_1)::text = 'LT'::text)) OR ((trat_sc_1)::text = 'OT'::text)) OR ((trat_sc_1)::text = 'PE'::text)) OR ((trat_sc_1)::text = 'ZF'::text)) OR ((trat_sc_1)::text = 'NO'::text))),
    CONSTRAINT trat_sc_2chk CHECK ((((((((((((((trat_sc_2)::text = 'CB'::text) OR ((trat_sc_2)::text = 'ES'::text)) OR ((trat_sc_2)::text = 'FA'::text)) OR ((trat_sc_2)::text = 'FV'::text)) OR ((trat_sc_2)::text = 'IR'::text)) OR ((trat_sc_2)::text = 'LA'::text)) OR ((trat_sc_2)::text = 'LB'::text)) OR ((trat_sc_2)::text = 'LT'::text)) OR ((trat_sc_2)::text = 'OT'::text)) OR ((trat_sc_2)::text = 'PE'::text)) OR ((trat_sc_2)::text = 'ZF'::text)) OR ((trat_sc_2)::text = 'NO'::text))),
    CONSTRAINT trat_sc_3chk CHECK ((((((((((((((trat_sc_3)::text = 'CB'::text) OR ((trat_sc_3)::text = 'ES'::text)) OR ((trat_sc_3)::text = 'FA'::text)) OR ((trat_sc_3)::text = 'FV'::text)) OR ((trat_sc_3)::text = 'IR'::text)) OR ((trat_sc_3)::text = 'LA'::text)) OR ((trat_sc_3)::text = 'LB'::text)) OR ((trat_sc_3)::text = 'LT'::text)) OR ((trat_sc_3)::text = 'OT'::text)) OR ((trat_sc_3)::text = 'PE'::text)) OR ((trat_sc_3)::text = 'ZF'::text)) OR ((trat_sc_3)::text = 'NO'::text)))
);


ALTER TABLE public.eiel_t1_saneam_ed OWNER TO postgres;

--
-- TOC entry 541 (class 1259 OID 77808728)
-- Name: eiel_t2_saneam_ed; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t2_saneam_ed (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_ed character varying(4) NOT NULL,
    titular character varying(2),
    gestor character varying(2),
    capacidad integer,
    problem_1 character varying(2),
    problem_2 character varying(2),
    problem_3 character varying(2),
    lodo_gest character varying(2),
    lodo_vert integer,
    lodo_inci integer,
    lodo_con_agri integer,
    lodo_sin_agri integer,
    lodo_ot integer,
    fecha_inst date,
    observ character varying(250),
    fecha_revision date,
    estado_revision integer,
    bloqueado character varying(32),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'ED'::text)),
    CONSTRAINT gestorchk CHECK ((((((((((gestor)::text = 'CO'::text) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT lodo_gestchk CHECK (((((((((((lodo_gest)::text = 'CO'::text) OR ((lodo_gest)::text = 'EM'::text)) OR ((lodo_gest)::text = 'EP'::text)) OR ((lodo_gest)::text = 'MA'::text)) OR ((lodo_gest)::text = 'MU'::text)) OR ((lodo_gest)::text = 'NO'::text)) OR ((lodo_gest)::text = 'OT'::text)) OR ((lodo_gest)::text = 'PV'::text)) OR ((lodo_gest)::text = 'VE'::text))),
    CONSTRAINT problem_1chk CHECK ((((((((((((problem_1)::text = 'AB'::text) OR ((problem_1)::text = 'CE'::text)) OR ((problem_1)::text = 'EI'::text)) OR ((problem_1)::text = 'FE'::text)) OR ((problem_1)::text = 'FM'::text)) OR ((problem_1)::text = 'IF'::text)) OR ((problem_1)::text = 'IN'::text)) OR ((problem_1)::text = 'NO'::text)) OR ((problem_1)::text = 'OT'::text)) OR ((problem_1)::text = 'VI'::text))),
    CONSTRAINT problem_2chk CHECK ((((((((((((problem_2)::text = 'AB'::text) OR ((problem_2)::text = 'CE'::text)) OR ((problem_2)::text = 'EI'::text)) OR ((problem_2)::text = 'FE'::text)) OR ((problem_2)::text = 'FM'::text)) OR ((problem_2)::text = 'IF'::text)) OR ((problem_2)::text = 'IN'::text)) OR ((problem_2)::text = 'NO'::text)) OR ((problem_2)::text = 'OT'::text)) OR ((problem_1)::text = 'VI'::text))),
    CONSTRAINT problem_3chk CHECK ((((((((((((problem_3)::text = 'AB'::text) OR ((problem_3)::text = 'CE'::text)) OR ((problem_3)::text = 'EI'::text)) OR ((problem_3)::text = 'FE'::text)) OR ((problem_3)::text = 'FM'::text)) OR ((problem_3)::text = 'IF'::text)) OR ((problem_3)::text = 'IN'::text)) OR ((problem_3)::text = 'NO'::text)) OR ((problem_3)::text = 'OT'::text)) OR ((problem_3)::text = 'VI'::text))),
    CONSTRAINT titularchk CHECK ((((((((((titular)::text = 'CO'::text) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_t2_saneam_ed OWNER TO postgres;

--
-- TOC entry 542 (class 1259 OID 77808740)
-- Name: eiel_t_abast_au; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_abast_au (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT aau_caudalchk CHECK (((((aau_caudal)::text = 'IN'::text) OR ((aau_caudal)::text = 'NO'::text)) OR ((aau_caudal)::text = 'SF'::text)))
);


ALTER TABLE public.eiel_t_abast_au OWNER TO postgres;

--
-- TOC entry 543 (class 1259 OID 77808746)
-- Name: eiel_t_abast_ca; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_abast_ca (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_ca character varying(3) NOT NULL,
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
    max_consumo numeric(12,2),
    bloqueado character varying(32),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'CA'::text)),
    CONSTRAINT contadorchk CHECK ((((contador)::text = 'NO'::text) OR ((contador)::text = 'SI'::text))),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((gestor)::text = 'CO'::text) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT proteccionchk CHECK (((((proteccion)::text = 'IN'::text) OR ((proteccion)::text = 'NO'::text)) OR ((proteccion)::text = 'SF'::text))),
    CONSTRAINT sist_impulsionchk CHECK ((((sist_impulsion)::text = 'GR'::text) OR ((sist_impulsion)::text = 'IF'::text))),
    CONSTRAINT tipochk CHECK (((((((((((((tipo)::text = 'AL'::text) OR ((tipo)::text = 'AM'::text)) OR ((tipo)::text = 'AS'::text)) OR ((tipo)::text = 'CA'::text)) OR ((tipo)::text = 'EB'::text)) OR ((tipo)::text = 'GA'::text)) OR ((tipo)::text = 'MT'::text)) OR ((tipo)::text = 'OT'::text)) OR ((tipo)::text = 'PO'::text)) OR ((tipo)::text = 'PX'::text)) OR ((tipo)::text = 'RI'::text))),
    CONSTRAINT titularchk CHECK ((((((((((titular)::text = 'CO'::text) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text))),
    CONSTRAINT usochk CHECK ((((uso)::text = 'UE'::text) OR ((uso)::text = 'UO'::text)))
);


ALTER TABLE public.eiel_t_abast_ca OWNER TO postgres;

--
-- TOC entry 544 (class 1259 OID 77808760)
-- Name: eiel_t_abast_de; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_abast_de (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_de character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'DE'::text)),
    CONSTRAINT contadorchk CHECK ((((((contador)::text = 'AM'::text) OR ((contador)::text = 'EN'::text)) OR ((contador)::text = 'NO'::text)) OR ((contador)::text = 'SA'::text))),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((gestor)::text = 'CO'::text) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT proteccionchk CHECK (((((proteccion)::text = 'IN'::text) OR ((proteccion)::text = 'NO'::text)) OR ((proteccion)::text = 'SF'::text))),
    CONSTRAINT titularchk CHECK ((((((((((titular)::text = 'CO'::text) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text))),
    CONSTRAINT ubicacionchk CHECK (((((((ubicacion)::text = 'EL'::text) OR ((ubicacion)::text = 'EN'::text)) OR ((ubicacion)::text = 'ES'::text)) OR ((ubicacion)::text = 'OT'::text)) OR ((ubicacion)::text = 'SE'::text)))
);


ALTER TABLE public.eiel_t_abast_de OWNER TO postgres;

--
-- TOC entry 545 (class 1259 OID 77808772)
-- Name: eiel_t_abast_serv; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_abast_serv (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT calidad_servchk CHECK (((((((calidad_serv)::text = 'B'::text) OR ((calidad_serv)::text = 'E'::text)) OR ((calidad_serv)::text = 'M'::text)) OR ((calidad_serv)::text = 'NO'::text)) OR ((calidad_serv)::text = 'R'::text)))
);


ALTER TABLE public.eiel_t_abast_serv OWNER TO postgres;

--
-- TOC entry 546 (class 1259 OID 77808778)
-- Name: eiel_t_abast_tcn; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_abast_tcn (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    tramo_cn character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'CN'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((gestor)::text = 'CO'::text) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT materialchk CHECK (((((((((material)::text = 'FC'::text) OR ((material)::text = 'FU'::text)) OR ((material)::text = 'HO'::text)) OR ((material)::text = 'OT'::text)) OR ((material)::text = 'PC'::text)) OR ((material)::text = 'PE'::text)) OR ((material)::text = 'PV'::text))),
    CONSTRAINT titularchk CHECK ((((((((((titular)::text = 'CO'::text) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_t_abast_tcn OWNER TO postgres;

--
-- TOC entry 547 (class 1259 OID 77808788)
-- Name: eiel_t_abast_tp; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_abast_tp (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_tp character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT categoria_a1chk CHECK ((((categoria_a1)::text = 'SI'::text) OR ((categoria_a1)::text = 'NO'::text))),
    CONSTRAINT categoria_a2chk CHECK ((((categoria_a2)::text = 'SI'::text) OR ((categoria_a2)::text = 'NO'::text))),
    CONSTRAINT categoria_a3chk CHECK ((((categoria_a3)::text = 'SI'::text) OR ((categoria_a3)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'TP'::text)),
    CONSTRAINT desaladorachk CHECK ((((desaladora)::text = 'SI'::text) OR ((desaladora)::text = 'NO'::text))),
    CONSTRAINT desinf_1chk CHECK ((((((((((((((desinf_1)::text = 'CG'::text) OR ((desinf_1)::text = 'CL'::text)) OR ((desinf_1)::text = 'DC'::text)) OR ((desinf_1)::text = 'HP'::text)) OR ((desinf_1)::text = 'MC'::text)) OR ((desinf_1)::text = 'NF'::text)) OR ((desinf_1)::text = 'OS'::text)) OR ((desinf_1)::text = 'OT'::text)) OR ((desinf_1)::text = 'OZ'::text)) OR ((desinf_1)::text = 'UF'::text)) OR ((desinf_1)::text = 'UL'::text)) OR ((desinf_1)::text = 'NO'::text))),
    CONSTRAINT desinf_2chk CHECK ((((((((((((((desinf_2)::text = 'CG'::text) OR ((desinf_2)::text = 'CL'::text)) OR ((desinf_2)::text = 'DC'::text)) OR ((desinf_2)::text = 'HP'::text)) OR ((desinf_2)::text = 'MC'::text)) OR ((desinf_2)::text = 'NF'::text)) OR ((desinf_2)::text = 'OS'::text)) OR ((desinf_2)::text = 'OT'::text)) OR ((desinf_2)::text = 'OZ'::text)) OR ((desinf_2)::text = 'UF'::text)) OR ((desinf_2)::text = 'UL'::text)) OR ((desinf_2)::text = 'NO'::text))),
    CONSTRAINT desinf_3chk CHECK ((((((((((((((desinf_3)::text = 'CG'::text) OR ((desinf_3)::text = 'CL'::text)) OR ((desinf_3)::text = 'DC'::text)) OR ((desinf_3)::text = 'HP'::text)) OR ((desinf_3)::text = 'MC'::text)) OR ((desinf_3)::text = 'NF'::text)) OR ((desinf_3)::text = 'OS'::text)) OR ((desinf_3)::text = 'OT'::text)) OR ((desinf_3)::text = 'OZ'::text)) OR ((desinf_3)::text = 'UF'::text)) OR ((desinf_3)::text = 'UL'::text)) OR ((desinf_3)::text = 'NO'::text))),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT organismo_controlchk CHECK ((((((((organismo_control)::text = 'CA'::text) OR ((organismo_control)::text = 'MA'::text)) OR ((organismo_control)::text = 'MU'::text)) OR ((organismo_control)::text = 'NO'::text)) OR ((organismo_control)::text = 'OT'::text)) OR ((organismo_control)::text = 'PR'::text))),
    CONSTRAINT otroschk CHECK ((((otros)::text = 'SI'::text) OR ((otros)::text = 'NO'::text))),
    CONSTRAINT periodicidadchk CHECK (((((((((periodicidad)::text = 'AL'::text) OR ((periodicidad)::text = 'DI'::text)) OR ((periodicidad)::text = 'ME'::text)) OR ((periodicidad)::text = 'NO'::text)) OR ((periodicidad)::text = 'OT'::text)) OR ((periodicidad)::text = 'QU'::text)) OR ((periodicidad)::text = 'SE'::text))),
    CONSTRAINT s_desinfchk CHECK ((((s_desinf)::text = 'SI'::text) OR ((s_desinf)::text = 'NO'::text))),
    CONSTRAINT tipochk CHECK ((((tipo)::text = 'AU'::text) OR ((tipo)::text = 'MA'::text))),
    CONSTRAINT ubicacionchk CHECK (((((((ubicacion)::text = 'CA'::text) OR ((ubicacion)::text = 'CD'::text)) OR ((ubicacion)::text = 'DE'::text)) OR ((ubicacion)::text = 'OT'::text)) OR ((ubicacion)::text = 'RD'::text)))
);


ALTER TABLE public.eiel_t_abast_tp OWNER TO postgres;

--
-- TOC entry 548 (class 1259 OID 77808808)
-- Name: eiel_t_as; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_as (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_as character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'AS'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorrchk CHECK ((((((((((((gestor)::text = 'CA'::text) OR ((gestor)::text = 'CO'::text)) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PR'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT tipochk CHECK ((((((((((tipo)::text = 'AL'::text) OR ((tipo)::text = 'CA'::text)) OR ((tipo)::text = 'CE'::text)) OR ((tipo)::text = 'CT'::text)) OR ((tipo)::text = 'EX'::text)) OR ((tipo)::text = 'GI'::text)) OR ((tipo)::text = 'IN'::text)) OR ((tipo)::text = 'RA'::text))),
    CONSTRAINT titularchk CHECK ((((((((((((titular)::text = 'CA'::text) OR ((titular)::text = 'CO'::text)) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_t_as OWNER TO postgres;

--
-- TOC entry 549 (class 1259 OID 77808819)
-- Name: eiel_t_cabildo_consejo; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_cabildo_consejo (
    codprov character varying(2) NOT NULL,
    cod_isla character varying(2) NOT NULL,
    denominacion character varying(16),
    bloqueado character varying(32),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_t_cabildo_consejo OWNER TO postgres;

--
-- TOC entry 550 (class 1259 OID 77808824)
-- Name: eiel_t_carreteras; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_carreteras (
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
    pki numeric(8,3),
    pkf numeric(8,3),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT titular_viachk CHECK ((((((((titular_via)::text = 'CA'::text) OR ((titular_via)::text = 'ES'::text)) OR ((titular_via)::text = 'MU'::text)) OR ((titular_via)::text = 'OT'::text)) OR ((titular_via)::text = 'PR'::text)) OR ((titular_via)::text = 'CH'::text)))
);


ALTER TABLE public.eiel_t_carreteras OWNER TO postgres;

--
-- TOC entry 7711 (class 0 OID 0)
-- Dependencies: 550
-- Name: COLUMN eiel_t_carreteras.pki; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN eiel_t_carreteras.pki IS 'Kilometro Inicial';


--
-- TOC entry 7712 (class 0 OID 0)
-- Dependencies: 550
-- Name: COLUMN eiel_t_carreteras.pkf; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN eiel_t_carreteras.pkf IS 'Kilometro Final';


--
-- TOC entry 551 (class 1259 OID 77808830)
-- Name: eiel_t_cc; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_cc (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_cc character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'CC'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT tenenciachk CHECK ((((((tenencia)::text = 'AL'::text) OR ((tenencia)::text = 'CE'::text)) OR ((tenencia)::text = 'MU'::text)) OR ((tenencia)::text = 'OT'::text))),
    CONSTRAINT tipochk CHECK (((((((tipo)::text = 'AE'::text) OR ((tipo)::text = 'AY'::text)) OR ((tipo)::text = 'OT'::text)) OR ((tipo)::text = 'UA'::text)) OR ((tipo)::text = 'VM'::text))),
    CONSTRAINT titularchk CHECK ((((((((titular)::text = 'CA'::text) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OP'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)) OR ((titular)::text = 'PV'::text)))
);


ALTER TABLE public.eiel_t_cc OWNER TO postgres;

--
-- TOC entry 552 (class 1259 OID 77808841)
-- Name: eiel_t_cc_usos; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_cc_usos (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_cc character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    uso character varying(2) NOT NULL,
    s_cubierta integer NOT NULL,
    fecha_ini date,
    observ character varying(50),
    fecha_revision date,
    estado_revision integer,
    orden_uso character varying(3),
    inst_pertenece character varying(20),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'CC'::text)),
    CONSTRAINT usochk CHECK (((((((((((((((uso)::text = 'AA'::text) OR ((uso)::text = 'AM'::text)) OR ((uso)::text = 'AS'::text)) OR ((uso)::text = 'BA'::text)) OR ((uso)::text = 'BR'::text)) OR ((uso)::text = 'CI'::text)) OR ((uso)::text = 'MO'::text)) OR ((uso)::text = 'OA'::text)) OR ((uso)::text = 'OT'::text)) OR ((uso)::text = 'PN'::text)) OR ((uso)::text = 'SA'::text)) OR ((uso)::text = 'SO'::text)) OR ((uso)::text = 'VI'::text)))
);


ALTER TABLE public.eiel_t_cc_usos OWNER TO postgres;

--
-- TOC entry 553 (class 1259 OID 77808848)
-- Name: eiel_t_ce; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_ce (
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT accesochk CHECK ((((((acceso)::text = 'B'::text) OR ((acceso)::text = 'E'::text)) OR ((acceso)::text = 'M'::text)) OR ((acceso)::text = 'R'::text))),
    CONSTRAINT ampliacionchk CHECK ((((ampliacion)::text = 'SI'::text) OR ((ampliacion)::text = 'NO'::text))),
    CONSTRAINT capillachk CHECK (((((capilla)::text = 'EC'::text) OR ((capilla)::text = 'MO'::text)) OR ((capilla)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'CE'::text)),
    CONSTRAINT crematoriochk CHECK ((((crematorio)::text = 'SI'::text) OR ((crematorio)::text = 'NO'::text))),
    CONSTRAINT depositochk CHECK ((((deposito)::text = 'SI'::text) OR ((deposito)::text = 'NO'::text))),
    CONSTRAINT titularchk CHECK ((((((titular)::text = 'CR'::text) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PV'::text)))
);


ALTER TABLE public.eiel_t_ce OWNER TO postgres;

--
-- TOC entry 554 (class 1259 OID 77808861)
-- Name: eiel_t_cu; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_cu (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_cu character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'CU'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((((gestor)::text = 'CA'::text) OR ((gestor)::text = 'CO'::text)) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PR'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT tipochk CHECK ((((((((((((((((tipo)::text = 'AR'::text) OR ((tipo)::text = 'AU'::text)) OR ((tipo)::text = 'BI'::text)) OR ((tipo)::text = 'CA'::text)) OR ((tipo)::text = 'CC'::text)) OR ((tipo)::text = 'CS'::text)) OR ((tipo)::text = 'HP'::text)) OR ((tipo)::text = 'KI'::text)) OR ((tipo)::text = 'LU'::text)) OR ((tipo)::text = 'MS'::text)) OR ((tipo)::text = 'OT'::text)) OR ((tipo)::text = 'PZ'::text)) OR ((tipo)::text = 'SC'::text)) OR ((tipo)::text = 'TC'::text))),
    CONSTRAINT titularchk CHECK ((((((((((((titular)::text = 'CA'::text) OR ((titular)::text = 'CO'::text)) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_t_cu OWNER TO postgres;

--
-- TOC entry 555 (class 1259 OID 77808872)
-- Name: eiel_t_cu_usos; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_cu_usos (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_cu character varying(3) NOT NULL,
    uso character varying(2) NOT NULL,
    s_cubierta integer,
    fecha_ini date,
    observ character varying(250),
    fecha_revision date,
    estado_revision integer,
    orden_uso integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'CU'::text)),
    CONSTRAINT usochk CHECK (((((((((((((((uso)::text = 'AO'::text) OR ((uso)::text = 'BA'::text)) OR ((uso)::text = 'BR'::text)) OR ((uso)::text = 'CI'::text)) OR ((uso)::text = 'CV'::text)) OR ((uso)::text = 'DC'::text)) OR ((uso)::text = 'ED'::text)) OR ((uso)::text = 'MO'::text)) OR ((uso)::text = 'OT'::text)) OR ((uso)::text = 'PN'::text)) OR ((uso)::text = 'PS'::text)) OR ((uso)::text = 'TE'::text)) OR ((uso)::text = 'KM'::text)))
);


ALTER TABLE public.eiel_t_cu_usos OWNER TO postgres;

--
-- TOC entry 556 (class 1259 OID 77808879)
-- Name: eiel_t_en; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_en (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_en character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT ambitochk CHECK (((((ambito)::text = 'A'::text) OR ((ambito)::text = 'C'::text)) OR ((ambito)::text = 'L'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'EN'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT titularchk CHECK ((((((titular)::text = 'CE'::text) OR ((titular)::text = 'CL'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)))
);


ALTER TABLE public.eiel_t_en OWNER TO postgres;

--
-- TOC entry 557 (class 1259 OID 77808889)
-- Name: eiel_t_en_nivel; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_en_nivel (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_en character varying(3) NOT NULL,
    nivel character varying(3) NOT NULL,
    unidades integer,
    plazas integer,
    alumnos integer,
    fecha_curso date,
    observ character varying(50),
    fecha_revision date,
    estado_revision integer,
    orden_en_nivel character varying(3),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'EN'::text)),
    CONSTRAINT nivelchk CHECK ((((((((((nivel)::text = 'BAC'::text) OR ((nivel)::text = 'ESO'::text)) OR ((nivel)::text = 'ESP'::text)) OR ((nivel)::text = 'FP1'::text)) OR ((nivel)::text = 'FP2'::text)) OR ((nivel)::text = 'INF'::text)) OR ((nivel)::text = 'OTR'::text)) OR ((nivel)::text = 'PRI'::text)))
);


ALTER TABLE public.eiel_t_en_nivel OWNER TO postgres;

--
-- TOC entry 558 (class 1259 OID 77808896)
-- Name: eiel_t_entidad_singular; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_entidad_singular (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    denominacion character varying(50),
    observ character varying(50),
    fecha_revision date,
    estado_revision integer,
    bloqueado character varying(32),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_t_entidad_singular OWNER TO postgres;

--
-- TOC entry 559 (class 1259 OID 77808901)
-- Name: eiel_t_entidades_agrupadas; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_entidades_agrupadas (
    codmunicipio character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codnucleo character varying(2) NOT NULL,
    codentidad_agrupada character varying(4) NOT NULL,
    codnucleo_agrupado character varying(2) NOT NULL,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    bloqueado character varying(32)
);


ALTER TABLE public.eiel_t_entidades_agrupadas OWNER TO postgres;

--
-- TOC entry 560 (class 1259 OID 77808906)
-- Name: eiel_t_id; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_id (
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'ID'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((((gestor)::text = 'CA'::text) OR ((gestor)::text = 'CO'::text)) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PR'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT tipochk CHECK (((((((((((((((tipo)::text = 'CP'::text) OR ((tipo)::text = 'EV'::text)) OR ((tipo)::text = 'FC'::text)) OR ((tipo)::text = 'FR'::text)) OR ((tipo)::text = 'GI'::text)) OR ((tipo)::text = 'OT'::text)) OR ((tipo)::text = 'PC'::text)) OR ((tipo)::text = 'PD'::text)) OR ((tipo)::text = 'PI'::text)) OR ((tipo)::text = 'PP'::text)) OR ((tipo)::text = 'PT'::text)) OR ((tipo)::text = 'SK'::text)) OR ((tipo)::text = 'TJ'::text))),
    CONSTRAINT titularchk CHECK ((((((((((((titular)::text = 'CA'::text) OR ((titular)::text = 'CO'::text)) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_t_id OWNER TO postgres;

--
-- TOC entry 561 (class 1259 OID 77808917)
-- Name: eiel_t_id_deportes; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_id_deportes (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_id character varying(3) NOT NULL,
    tipo_deporte character varying(2) NOT NULL,
    fecha_inst date,
    observ character varying(50),
    fecha_revision date,
    estado_revision integer,
    orden_id_deportes character varying(3),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'ID'::text))
);


ALTER TABLE public.eiel_t_id_deportes OWNER TO postgres;

--
-- TOC entry 562 (class 1259 OID 77808923)
-- Name: eiel_t_inf_ttmm; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_inf_ttmm (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT ba_cachk CHECK ((((ba_ca)::text = 'SI'::text) OR ((ba_ca)::text = 'NO'::text))),
    CONSTRAINT ba_rbchk CHECK ((((ba_rb)::text = 'SI'::text) OR ((ba_rb)::text = 'NO'::text))),
    CONSTRAINT ba_rdchk CHECK ((((ba_rd)::text = 'SI'::text) OR ((ba_rd)::text = 'NO'::text))),
    CONSTRAINT ba_stchk CHECK ((((ba_st)::text = 'SI'::text) OR ((ba_st)::text = 'NO'::text))),
    CONSTRAINT ba_wichk CHECK ((((ba_wi)::text = 'SI'::text) OR ((ba_wi)::text = 'NO'::text))),
    CONSTRAINT ba_xdchk CHECK ((((ba_xd)::text = 'SI'::text) OR ((ba_xd)::text = 'NO'::text))),
    CONSTRAINT capichk CHECK ((((capi)::text = 'SI'::text) OR ((capi)::text = 'NO'::text))),
    CONSTRAINT electricchk CHECK (((((((electric)::text = 'B'::text) OR ((electric)::text = 'E'::text)) OR ((electric)::text = 'M'::text)) OR ((electric)::text = 'R'::text)) OR ((electric)::text = 'C'::text))),
    CONSTRAINT gaschk CHECK (((((((gas)::text = 'B'::text) OR ((gas)::text = 'E'::text)) OR ((gas)::text = 'M'::text)) OR ((gas)::text = 'R'::text)) OR ((gas)::text = 'C'::text)))
);


ALTER TABLE public.eiel_t_inf_ttmm OWNER TO postgres;

--
-- TOC entry 563 (class 1259 OID 77808937)
-- Name: eiel_t_ip; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_ip (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_ip character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT ambitochk CHECK (((((ambito)::text = 'CO'::text) OR ((ambito)::text = 'MU'::text)) OR ((ambito)::text = 'PR'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'IP'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK (((((((((((gestor)::text = 'CA'::text) OR ((gestor)::text = 'CO'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OP'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PR'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'XR'::text))),
    CONSTRAINT tipochk CHECK ((((((tipo)::text = 'BO'::text) OR ((tipo)::text = 'CS'::text)) OR ((tipo)::text = 'OT'::text)) OR ((tipo)::text = 'PC'::text))),
    CONSTRAINT titularchk CHECK (((((((((((titular)::text = 'CA'::text) OR ((titular)::text = 'CO'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OP'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'XR'::text)))
);


ALTER TABLE public.eiel_t_ip OWNER TO postgres;

--
-- TOC entry 564 (class 1259 OID 77808949)
-- Name: eiel_t_lm; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_lm (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_lm character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'LM'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK (((((((((((((gestor)::text = 'CA'::text) OR ((gestor)::text = 'CO'::text)) OR ((titular)::text = 'CP'::text)) OR ((titular)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((gestor)::text = 'PR'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT tipochk CHECK (((((tipo)::text = 'FE'::text) OR ((tipo)::text = 'LO'::text)) OR ((tipo)::text = 'ME'::text))),
    CONSTRAINT titularchk CHECK (((((((((((((titular)::text = 'CA'::text) OR ((titular)::text = 'CO'::text)) OR ((titular)::text = 'CP'::text)) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_t_lm OWNER TO postgres;

--
-- TOC entry 565 (class 1259 OID 77808960)
-- Name: eiel_t_mt; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_mt (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_mt character varying(3) NOT NULL,
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
    utilizacion numeric(3,0),
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT bovinochk CHECK ((((bovino)::text = 'SI'::text) OR ((bovino)::text = 'NO'::text))),
    CONSTRAINT clasechk CHECK ((((((clase)::text = 'CO'::text) OR ((clase)::text = 'MU'::text)) OR ((clase)::text = 'OT'::text)) OR ((clase)::text = 'PR'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'MT'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((((gestor)::text = 'CA'::text) OR ((gestor)::text = 'CO'::text)) OR ((titular)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((gestor)::text = 'PR'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT otroschk CHECK ((((otros)::text = 'SI'::text) OR ((otros)::text = 'NO'::text))),
    CONSTRAINT ovinoschk CHECK ((((ovino)::text = 'SI'::text) OR ((ovino)::text = 'NO'::text))),
    CONSTRAINT porcinochk CHECK ((((porcino)::text = 'SI'::text) OR ((porcino)::text = 'NO'::text))),
    CONSTRAINT titularchk CHECK ((((((((((((titular)::text = 'CA'::text) OR ((titular)::text = 'CO'::text)) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text))),
    CONSTRAINT tunelchk CHECK ((((tunel)::text = 'SI'::text) OR ((tunel)::text = 'NO'::text)))
);


ALTER TABLE public.eiel_t_mt OWNER TO postgres;

--
-- TOC entry 566 (class 1259 OID 77808976)
-- Name: eiel_t_mun_diseminados; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_mun_diseminados (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_t_mun_diseminados OWNER TO postgres;

--
-- TOC entry 567 (class 1259 OID 77808981)
-- Name: eiel_t_nucl_encuest_1; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_nucl_encuest_1 (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT accesibilidadchk CHECK (((((accesibilidad)::text = 'IN'::text) OR ((accesibilidad)::text = 'IT'::text)) OR ((accesibilidad)::text = 'NO'::text)))
);


ALTER TABLE public.eiel_t_nucl_encuest_1 OWNER TO postgres;

--
-- TOC entry 568 (class 1259 OID 77808987)
-- Name: eiel_t_nucl_encuest_2; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_nucl_encuest_2 (
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT aag_bocasrchk CHECK (((((aag_bocasr)::text = 'IN'::text) OR ((aag_bocasr)::text = 'NO'::text)) OR ((aag_bocasr)::text = 'SF'::text))),
    CONSTRAINT aag_caudalchk CHECK (((((aag_caudal)::text = 'IN'::text) OR ((aag_caudal)::text = 'NO'::text)) OR ((aag_caudal)::text = 'SF'::text))),
    CONSTRAINT aag_contadchk CHECK ((((aag_contad)::text = 'SI'::text) OR ((aag_contad)::text = 'NO'::text))),
    CONSTRAINT aag_hidranchk CHECK (((((aag_hidran)::text = 'IN'::text) OR ((aag_hidran)::text = 'NO'::text)) OR ((aag_hidran)::text = 'SF'::text))),
    CONSTRAINT aag_restrichk CHECK ((((((aag_restri)::text = 'NO'::text) OR ((aag_restri)::text = 'RF'::text)) OR ((aag_restri)::text = 'RM'::text)) OR ((aag_restri)::text = 'RT'::text))),
    CONSTRAINT aag_tasachk CHECK ((((aag_tasa)::text = 'SI'::text) OR ((aag_tasa)::text = 'NO'::text))),
    CONSTRAINT aag_valvulchk CHECK (((((aag_valvul)::text = 'IN'::text) OR ((aag_valvul)::text = 'NO'::text)) OR ((aag_valvul)::text = 'SF'::text))),
    CONSTRAINT cisternahk CHECK ((((((((((cisterna)::text = 'AC'::text) OR ((cisterna)::text = 'IN'::text)) OR ((cisterna)::text = 'MC'::text)) OR ((cisterna)::text = 'NO'::text)) OR ((cisterna)::text = 'OT'::text)) OR ((cisterna)::text = 'RO'::text)) OR ((cisterna)::text = 'RU'::text)) OR ((cisterna)::text = 'SE'::text)))
);


ALTER TABLE public.eiel_t_nucl_encuest_2 OWNER TO postgres;

--
-- TOC entry 569 (class 1259 OID 77809000)
-- Name: eiel_t_nucleo_aband; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_nucleo_aband (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT accesochk CHECK (((((((acceso)::text = 'CA'::text) OR ((acceso)::text = 'CN'::text)) OR ((acceso)::text = 'IN'::text)) OR ((acceso)::text = 'RG'::text)) OR ((acceso)::text = 'VE'::text))),
    CONSTRAINT causa_abandonochk CHECK ((((((causa_abandono)::text = 'EM'::text) OR ((causa_abandono)::text = 'EX'::text)) OR ((causa_abandono)::text = 'IN'::text)) OR ((causa_abandono)::text = 'OT'::text))),
    CONSTRAINT rehabilitacionchk CHECK ((((rehabilitacion)::text = 'SI'::text) OR ((rehabilitacion)::text = 'NO'::text))),
    CONSTRAINT serv_aguachk CHECK (((((((serv_agua)::text = 'CA'::text) OR ((serv_agua)::text = 'CN'::text)) OR ((serv_agua)::text = 'IN'::text)) OR ((serv_agua)::text = 'RG'::text)) OR ((serv_agua)::text = 'VE'::text))),
    CONSTRAINT serv_electchk CHECK ((((serv_elect)::text = 'SI'::text) OR ((serv_elect)::text = 'NO'::text))),
    CONSTRAINT titular_abandonochk CHECK (((((((titular_abandono)::text = 'AB'::text) OR ((titular_abandono)::text = 'CH'::text)) OR ((titular_abandono)::text = 'OP'::text)) OR ((titular_abandono)::text = 'OT'::text)) OR ((titular_abandono)::text = 'PV'::text)))
);


ALTER TABLE public.eiel_t_nucleo_aband OWNER TO postgres;

--
-- TOC entry 570 (class 1259 OID 77809011)
-- Name: eiel_t_otros_serv_munic; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_otros_serv_munic (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT cob_serv_tlf_mchk CHECK ((((cob_serv_tlf_m)::text = 'NO'::text) OR ((cob_serv_tlf_m)::text = 'SI'::text))),
    CONSTRAINT en_eolicachk CHECK ((((en_eolica)::text = 'NO'::text) OR ((en_eolica)::text = 'SI'::text))),
    CONSTRAINT en_solarchk CHECK ((((en_solar)::text = 'NO'::text) OR ((en_solar)::text = 'SI'::text))),
    CONSTRAINT ord_soterrchk CHECK ((((ord_soterr)::text = 'NO'::text) OR ((ord_soterr)::text = 'SI'::text))),
    CONSTRAINT ot_energchk CHECK ((((ot_energ)::text = 'NO'::text) OR ((ot_energ)::text = 'SI'::text))),
    CONSTRAINT pl_mareochk CHECK ((((pl_mareo)::text = 'NO'::text) OR ((pl_mareo)::text = 'SI'::text))),
    CONSTRAINT sw_gb_elecchk CHECK ((((sw_gb_elec)::text = 'NO'::text) OR ((sw_gb_elec)::text = 'SI'::text))),
    CONSTRAINT sw_inf_grlchk CHECK ((((sw_inf_grl)::text = 'NO'::text) OR ((sw_inf_grl)::text = 'SI'::text))),
    CONSTRAINT sw_inf_turchk CHECK ((((sw_inf_tur)::text = 'NO'::text) OR ((sw_inf_tur)::text = 'SI'::text))),
    CONSTRAINT tv_dig_cablechk CHECK ((((tv_dig_cable)::text = 'NO'::text) OR ((tv_dig_cable)::text = 'SI'::text)))
);


ALTER TABLE public.eiel_t_otros_serv_munic OWNER TO postgres;

--
-- TOC entry 571 (class 1259 OID 77809026)
-- Name: eiel_t_padron_nd; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_padron_nd (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_t_padron_nd OWNER TO postgres;

--
-- TOC entry 572 (class 1259 OID 77809031)
-- Name: eiel_t_padron_ttmm; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_padron_ttmm (
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_t_padron_ttmm OWNER TO postgres;

--
-- TOC entry 573 (class 1259 OID 77809036)
-- Name: eiel_t_pj; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_pj (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_pj character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT aguachk CHECK ((((agua)::text = 'SI'::text) OR ((agua)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'PJ'::text)),
    CONSTRAINT comedorchk CHECK ((((comedor)::text = 'SI'::text) OR ((comedor)::text = 'NO'::text))),
    CONSTRAINT electricidadchk CHECK ((((electricidad)::text = 'SI'::text) OR ((electricidad)::text = 'NO'::text))),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((((gestor)::text = 'CA'::text) OR ((gestor)::text = 'CO'::text)) OR ((titular)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((gestor)::text = 'PR'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT juegos_infchk CHECK ((((juegos_inf)::text = 'SI'::text) OR ((juegos_inf)::text = 'NO'::text))),
    CONSTRAINT otraschk CHECK ((((otras)::text = 'SI'::text) OR ((otras)::text = 'NO'::text))),
    CONSTRAINT saneamientochk CHECK ((((saneamiento)::text = 'SI'::text) OR ((saneamiento)::text = 'NO'::text))),
    CONSTRAINT tipochk CHECK (((((((((((tipo)::text = 'AN'::text) OR ((tipo)::text = 'CA'::text)) OR ((tipo)::text = 'JA'::text)) OR ((tipo)::text = 'OT'::text)) OR ((tipo)::text = 'PI'::text)) OR ((tipo)::text = 'PN'::text)) OR ((tipo)::text = 'PU'::text)) OR ((tipo)::text = 'RF'::text)) OR ((tipo)::text = 'ZR'::text))),
    CONSTRAINT titularchk CHECK ((((((((((((titular)::text = 'CA'::text) OR ((titular)::text = 'CO'::text)) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_t_pj OWNER TO postgres;

--
-- TOC entry 574 (class 1259 OID 77809053)
-- Name: eiel_t_planeam_urban; Type: TABLE; Schema: public; Owner: consultas; Tablespace: 
--

CREATE TABLE eiel_t_planeam_urban (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    tipo_urba character varying(3) NOT NULL,
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
    orden_plan character varying(3) NOT NULL,
    bloqueado character varying(32),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT estado_tramitchk CHECK (((((((estado_tramit)::text = 'AD'::text) OR ((estado_tramit)::text = 'AI'::text)) OR ((estado_tramit)::text = 'AP'::text)) OR ((estado_tramit)::text = 'EL'::text)) OR ((estado_tramit)::text = 'ER'::text))),
    CONSTRAINT tipo_urbanchk CHECK (((((((((tipo_urba)::text = 'D.C'::text) OR ((tipo_urba)::text = 'D.S'::text)) OR ((tipo_urba)::text = 'N.M'::text)) OR ((tipo_urba)::text = 'N.P'::text)) OR ((tipo_urba)::text = 'P.G'::text)) OR ((tipo_urba)::text = 'P.S'::text)) OR ((tipo_urba)::text = 'P.E'::text)))
);


ALTER TABLE public.eiel_t_planeam_urban OWNER TO consultas;

--
-- TOC entry 575 (class 1259 OID 77809060)
-- Name: eiel_t_poblamiento; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_poblamiento (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    observ character varying(250),
    fecha_revision date,
    estado_revision integer,
    bloqueado character varying(32),
    denominacion character varying(50),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_t_poblamiento OWNER TO postgres;

--
-- TOC entry 576 (class 1259 OID 77809065)
-- Name: eiel_t_rb; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_rb (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    tipo character varying(2) NOT NULL,
    gestor character varying(3),
    periodicidad character varying(2),
    calidad character varying(2),
    tm_res_urb numeric(9,3),
    n_contenedores integer,
    observ character varying(250),
    fecha_revision date,
    estado_revision integer,
    bloqueado character varying(32),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT calidadchk CHECK ((((calidad)::text = 'AD'::text) OR ((calidad)::text = 'IN'::text))),
    CONSTRAINT gestorchk CHECK ((((((((gestor)::text = 'CON'::text) OR ((gestor)::text = 'MCC'::text)) OR ((gestor)::text = 'MCD'::text)) OR ((gestor)::text = 'MUC'::text)) OR ((gestor)::text = 'MUD'::text)) OR ((gestor)::text = 'OTS'::text))),
    CONSTRAINT periodicidadchk CHECK ((((((((periodicidad)::text = 'AL'::text) OR ((periodicidad)::text = 'DI'::text)) OR ((periodicidad)::text = 'NO'::text)) OR ((periodicidad)::text = 'OT'::text)) OR ((periodicidad)::text = 'QU'::text)) OR ((periodicidad)::text = 'SE'::text))),
    CONSTRAINT tipochk CHECK (((((((((tipo)::text = 'OG'::text) OR ((tipo)::text = 'OT'::text)) OR ((tipo)::text = 'PA'::text)) OR ((tipo)::text = 'PI'::text)) OR ((tipo)::text = 'PL'::text)) OR ((tipo)::text = 'RN'::text)) OR ((tipo)::text = 'VI'::text)))
);


ALTER TABLE public.eiel_t_rb OWNER TO postgres;

--
-- TOC entry 577 (class 1259 OID 77809074)
-- Name: eiel_t_rb_serv; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_rb_serv (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    srb_viviendas_afec integer,
    srb_pob_res_afect integer,
    srb_pob_est_afect integer,
    serv_limp_calles character varying(2),
    plantilla_serv_limp integer,
    observ character varying(50),
    fecha_revision date,
    estado_revision integer,
    bloqueado character varying(32),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT serv_limp_calleschk CHECK ((((serv_limp_calles)::text = 'SI'::text) OR ((serv_limp_calles)::text = 'NO'::text)))
);


ALTER TABLE public.eiel_t_rb_serv OWNER TO postgres;

--
-- TOC entry 578 (class 1259 OID 77809080)
-- Name: eiel_t_sa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_sa (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_sa character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'SA'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((((((gestor)::text = 'DIP'::text) OR ((gestor)::text = 'FAS'::text)) OR ((gestor)::text = 'INS'::text)) OR ((gestor)::text = 'MAT'::text)) OR ((gestor)::text = 'OEP'::text)) OR ((gestor)::text = 'OPB'::text)) OR ((gestor)::text = 'OTR'::text)) OR ((gestor)::text = 'PCR'::text)) OR ((gestor)::text = 'PIG'::text)) OR ((gestor)::text = 'PNB'::text)) OR ((gestor)::text = 'SAS'::text)) OR ((gestor)::text = 'CAS'::text))),
    CONSTRAINT tipochk CHECK (((((((((((((tipo)::text = 'AMB'::text) OR ((tipo)::text = 'CDS'::text)) OR ((tipo)::text = 'CLO'::text)) OR ((tipo)::text = 'CUR'::text)) OR ((tipo)::text = 'HGL'::text)) OR ((tipo)::text = 'HIN'::text)) OR ((tipo)::text = 'HLE'::text)) OR ((tipo)::text = 'HOE'::text)) OR ((tipo)::text = 'HPS'::text)) OR ((tipo)::text = 'HQY'::text)) OR ((tipo)::text = 'OTS'::text))),
    CONSTRAINT titularchk CHECK (((((((((((((((titular)::text = 'CAU'::text) OR ((titular)::text = 'DIP'::text)) OR ((titular)::text = 'FAS'::text)) OR ((titular)::text = 'MAT'::text)) OR ((titular)::text = 'MUN'::text)) OR ((titular)::text = 'OAC'::text)) OR ((titular)::text = 'OEP'::text)) OR ((titular)::text = 'OTR'::text)) OR ((titular)::text = 'PCR'::text)) OR ((titular)::text = 'PIG'::text)) OR ((titular)::text = 'PNB'::text)) OR ((titular)::text = 'PRB'::text)) OR ((titular)::text = 'TSS'::text))),
    CONSTRAINT ucichk CHECK ((((uci)::text = 'SI'::text) OR ((uci)::text = 'NO'::text)))
);


ALTER TABLE public.eiel_t_sa OWNER TO postgres;

--
-- TOC entry 579 (class 1259 OID 77809092)
-- Name: eiel_t_saneam_au; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_saneam_au (
    clave character varying(3) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT adecuacion_sauchk CHECK ((((adecuacion_sau)::text = 'AD'::text) OR ((adecuacion_sau)::text = 'IN'::text))),
    CONSTRAINT estado_sauchk CHECK ((((((estado_sau)::text = 'B'::text) OR ((estado_sau)::text = 'E'::text)) OR ((estado_sau)::text = 'M'::text)) OR ((estado_sau)::text = 'R'::text))),
    CONSTRAINT tipo_sauchk CHECK (((((tipo_sau)::text = 'FS'::text) OR ((tipo_sau)::text = 'OT'::text)) OR ((tipo_sau)::text = 'PN'::text)))
);


ALTER TABLE public.eiel_t_saneam_au OWNER TO postgres;

--
-- TOC entry 580 (class 1259 OID 77809100)
-- Name: eiel_t_saneam_pv; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_saneam_pv (
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK ((((clave)::text = 'EM'::text) OR ((clave)::text = 'PV'::text))),
    CONSTRAINT tipochk CHECK (((((tipo)::text = 'AD'::text) OR ((tipo)::text = 'AM'::text)) OR ((tipo)::text = 'OT'::text))),
    CONSTRAINT zonachk CHECK (((((zona)::text = 'ZM'::text) OR ((zona)::text = 'ZN'::text)) OR ((zona)::text = 'ZS'::text)))
);


ALTER TABLE public.eiel_t_saneam_pv OWNER TO postgres;

--
-- TOC entry 581 (class 1259 OID 77809108)
-- Name: eiel_t_saneam_serv; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_saneam_serv (
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    pozos_registro character varying(2),
    sumideros character varying(2),
    aliv_c_acum character varying(2),
    aliv_s_acum character varying(2),
    calidad_serv character varying(2),
    viviendas_c_conex integer,
    viviendas_s_conex integer,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT aliv_c_acumchk CHECK ((((aliv_c_acum)::text = 'SI'::text) OR ((aliv_c_acum)::text = 'NO'::text))),
    CONSTRAINT aliv_s_acumchk CHECK ((((aliv_s_acum)::text = 'SI'::text) OR ((aliv_s_acum)::text = 'NO'::text))),
    CONSTRAINT calidad_servchk CHECK (((((((calidad_serv)::text = 'B'::text) OR ((calidad_serv)::text = 'E'::text)) OR ((calidad_serv)::text = 'M'::text)) OR ((calidad_serv)::text = 'NO'::text)) OR ((calidad_serv)::text = 'R'::text))),
    CONSTRAINT pozos_registrochk CHECK (((((pozos_registro)::text = 'IN'::text) OR ((pozos_registro)::text = 'NO'::text)) OR ((pozos_registro)::text = 'SF'::text))),
    CONSTRAINT sumideroschk CHECK (((((sumideros)::text = 'IN'::text) OR ((sumideros)::text = 'NO'::text)) OR ((sumideros)::text = 'SF'::text)))
);


ALTER TABLE public.eiel_t_saneam_serv OWNER TO postgres;

--
-- TOC entry 582 (class 1259 OID 77809118)
-- Name: eiel_t_saneam_tcl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_saneam_tcl (
    clave character varying(3) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    tramo_cl character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'CL'::text)),
    CONSTRAINT estadonchk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((gestor)::text = 'CO'::text) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT materialchk CHECK (((((((((material)::text = 'FC'::text) OR ((material)::text = 'FU'::text)) OR ((material)::text = 'HO'::text)) OR ((material)::text = 'OT'::text)) OR ((material)::text = 'PC'::text)) OR ((material)::text = 'PE'::text)) OR ((material)::text = 'PV'::text))),
    CONSTRAINT sist_impulsionchk CHECK ((((sist_impulsion)::text = 'GR'::text) OR ((sist_impulsion)::text = 'IM'::text))),
    CONSTRAINT titularchk CHECK ((((((((((titular)::text = 'CO'::text) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_t_saneam_tcl OWNER TO postgres;

--
-- TOC entry 584 (class 1259 OID 77809131)
-- Name: eiel_t_saneam_tem; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_saneam_tem (
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    id_eiel integer DEFAULT nextval('seq_eiel_t_saneam_tem_id_eiel'::regclass) NOT NULL,
    fecha_revision date,
    estado_revision integer,
    CONSTRAINT clavechk CHECK (((clave)::text = 'EM'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT materialchk CHECK ((((((((((material)::text = 'FC'::text) OR ((material)::text = 'FU'::text)) OR ((material)::text = 'HO'::text)) OR ((material)::text = 'OT'::text)) OR ((material)::text = 'PC'::text)) OR ((material)::text = 'PE'::text)) OR ((material)::text = 'PV'::text)) OR ((material)::text = 'PL'::text)))
);


ALTER TABLE public.eiel_t_saneam_tem OWNER TO postgres;

--
-- TOC entry 585 (class 1259 OID 77809140)
-- Name: eiel_t_su; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_su (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_su character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'SU'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT titularchk CHECK (((((((titular)::text = 'CA'::text) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OP'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PR'::text))),
    CONSTRAINT uso_anteriorchk CHECK (((((((((((uso_anterior)::text = 'AS'::text) OR ((uso_anterior)::text = 'AY'::text)) OR ((uso_anterior)::text = 'CC'::text)) OR ((uso_anterior)::text = 'CE'::text)) OR ((uso_anterior)::text = 'CR'::text)) OR ((uso_anterior)::text = 'CS'::text)) OR ((uso_anterior)::text = 'JU'::text)) OR ((uso_anterior)::text = 'OT'::text)) OR ((uso_anterior)::text = 'VI'::text)))
);


ALTER TABLE public.eiel_t_su OWNER TO postgres;

--
-- TOC entry 586 (class 1259 OID 77809149)
-- Name: eiel_t_ta; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_ta (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_ta character varying(3) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT acceso_s_ruedaschk CHECK ((((acceso_s_ruedas)::text = 'SI'::text) OR ((acceso_s_ruedas)::text = 'NO'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'TA'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK (((((gestor)::text = 'MU'::text) OR ((gestor)::text = 'PU'::text)) OR ((gestor)::text = 'PV'::text))),
    CONSTRAINT titularchk CHECK (((((titular)::text = 'MU'::text) OR ((titular)::text = 'PU'::text)) OR ((titular)::text = 'PV'::text)))
);


ALTER TABLE public.eiel_t_ta OWNER TO postgres;

--
-- TOC entry 587 (class 1259 OID 77809159)
-- Name: eiel_t_vt; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_t_vt (
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_vt character varying(3) NOT NULL,
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
    capac_tot_porc numeric(8,0),
    capac_transf numeric(8,0),
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT actividadchk CHECK ((((((actividad)::text = 'AB'::text) OR ((actividad)::text = 'EN'::text)) OR ((actividad)::text = 'SE'::text)) OR ((actividad)::text = 'SG'::text))),
    CONSTRAINT capac_amplchk CHECK ((((capac_ampl)::text = 'SI'::text) OR ((capac_ampl)::text = 'NO'::text))),
    CONSTRAINT categoriachk CHECK (((((((categoria)::text = 'VIN'::text) OR ((categoria)::text = 'VMN'::text)) OR ((categoria)::text = 'VMP'::text)) OR ((categoria)::text = 'VRN'::text)) OR ((categoria)::text = 'VRP'::text))),
    CONSTRAINT clavechk CHECK (((clave)::text = 'VT'::text)),
    CONSTRAINT cont_animachk CHECK ((((cont_anima)::text = 'SI'::text) OR ((cont_anima)::text = 'NO'::text))),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'e'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT filtracionchk CHECK ((((filtracion)::text = 'SI'::text) OR ((filtracion)::text = 'NO'::text))),
    CONSTRAINT frec_averiachk CHECK ((((frec_averia)::text = 'SI'::text) OR ((frec_averia)::text = 'NO'::text))),
    CONSTRAINT gestorchk CHECK ((((((((gestor)::text = 'CON'::text) OR ((gestor)::text = 'MCC'::text)) OR ((gestor)::text = 'MCD'::text)) OR ((gestor)::text = 'MUC'::text)) OR ((gestor)::text = 'MUD'::text)) OR ((gestor)::text = 'OTS'::text))),
    CONSTRAINT humoschk CHECK ((((humos)::text = 'SI'::text) OR ((humos)::text = 'NO'::text))),
    CONSTRAINT impacto_vchk CHECK ((((impacto_v)::text = 'SI'::text) OR ((impacto_v)::text = 'NO'::text))),
    CONSTRAINT inestablechk CHECK ((((inestable)::text = 'SI'::text) OR ((inestable)::text = 'NO'::text))),
    CONSTRAINT oloreschk CHECK ((((olores)::text = 'SI'::text) OR ((olores)::text = 'NO'::text))),
    CONSTRAINT otroschk CHECK ((((otros)::text = 'SI'::text) OR ((otros)::text = 'NO'::text))),
    CONSTRAINT r_inunchk CHECK ((((r_inun)::text = 'SI'::text) OR ((r_inun)::text = 'NO'::text))),
    CONSTRAINT saturacion_vchk CHECK ((((saturacion)::text = 'SI'::text) OR ((saturacion)::text = 'NO'::text))),
    CONSTRAINT tipochk CHECK (((((((((((((tipo)::text = 'VCS'::text) OR ((tipo)::text = 'VIN'::text)) OR ((tipo)::text = 'VCC'::text)) OR ((tipo)::text = 'PLV'::text)) OR ((tipo)::text = 'PCE'::text)) OR ((tipo)::text = 'ISA'::text)) OR ((tipo)::text = 'ICA'::text)) OR ((tipo)::text = 'PTC'::text)) OR ((tipo)::text = 'PTI'::text)) OR ((tipo)::text = 'EST'::text)) OR ((tipo)::text = 'OTR'::text))),
    CONSTRAINT titularchk CHECK ((((((((titular)::text = 'CON'::text) OR ((titular)::text = 'MCC'::text)) OR ((titular)::text = 'MCD'::text)) OR ((titular)::text = 'MUC'::text)) OR ((titular)::text = 'MUD'::text)) OR ((titular)::text = 'OTS'::text)))
);


ALTER TABLE public.eiel_t_vt OWNER TO postgres;

--
-- TOC entry 588 (class 1259 OID 77809182)
-- Name: eiel_tr_abast_ca_pobl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_tr_abast_ca_pobl (
    clave_ca character varying(3) NOT NULL,
    codprov_ca character varying(3) NOT NULL,
    codmunic_ca character varying(3) NOT NULL,
    orden_ca character varying(3) NOT NULL,
    codprov_pobl character varying(2) NOT NULL,
    codmunic_pobl character varying(3) NOT NULL,
    codentidad_pobl character varying(4) NOT NULL,
    codpoblamiento_pobl character varying(2) NOT NULL,
    observ character varying(50),
    fecha_inicio_serv date,
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave_ca)::text = 'CA'::text))
);


ALTER TABLE public.eiel_tr_abast_ca_pobl OWNER TO postgres;

--
-- TOC entry 589 (class 1259 OID 77809188)
-- Name: eiel_tr_abast_de_pobl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_tr_abast_de_pobl (
    clave_de character varying(3) NOT NULL,
    codprov_de character varying(3) NOT NULL,
    codmunic_de character varying(3) NOT NULL,
    orden_de character varying(3) NOT NULL,
    codprov_pobl character varying(2) NOT NULL,
    codmunic_pobl character varying(3) NOT NULL,
    codentidad_pobl character varying(4) NOT NULL,
    codpoblamiento_pobl character varying(2) NOT NULL,
    observ character varying(50),
    fecha_inicio_serv date,
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave_de)::text = 'DE'::text))
);


ALTER TABLE public.eiel_tr_abast_de_pobl OWNER TO postgres;

--
-- TOC entry 590 (class 1259 OID 77809194)
-- Name: eiel_tr_abast_tcn_pobl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_tr_abast_tcn_pobl (
    clave_tcn character varying(3) NOT NULL,
    codprov_tcn character varying(3) NOT NULL,
    codmunic_tcn character varying(3) NOT NULL,
    tramo_tcn character varying(3) NOT NULL,
    codprov_pobl character varying(2) NOT NULL,
    codmunic_pobl character varying(3) NOT NULL,
    codentidad_pobl character varying(4) NOT NULL,
    codpoblamiento_pobl character varying(2) NOT NULL,
    observ character varying(250),
    pmi numeric(8,2),
    pmf numeric(8,2),
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave_tcn)::text = 'CN'::text))
);


ALTER TABLE public.eiel_tr_abast_tcn_pobl OWNER TO postgres;

--
-- TOC entry 591 (class 1259 OID 77809200)
-- Name: eiel_tr_abast_tp_pobl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_tr_abast_tp_pobl (
    clave_tp character varying(3) NOT NULL,
    codprov_tp character varying(3) NOT NULL,
    codmunic_tp character varying(3) NOT NULL,
    orden_tp character varying(3) NOT NULL,
    codprov_pobl character varying(2) NOT NULL,
    codmunic_pobl character varying(3) NOT NULL,
    codentidad_pobl character varying(4) NOT NULL,
    codpoblamiento_pobl character varying(2) NOT NULL,
    observ character varying(50),
    fecha_inicio_serv date,
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave_tp)::text = 'TP'::text))
);


ALTER TABLE public.eiel_tr_abast_tp_pobl OWNER TO postgres;

--
-- TOC entry 592 (class 1259 OID 77809206)
-- Name: eiel_tr_saneam_ed_pobl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_tr_saneam_ed_pobl (
    clave_ed character varying(2) NOT NULL,
    codprov_ed character varying(2) NOT NULL,
    codmunic_ed character varying(3) NOT NULL,
    orden_ed character varying(3) NOT NULL,
    codprov_pobl character varying(2) NOT NULL,
    codmunic_pobl character varying(3) NOT NULL,
    codentidad_pobl character varying(4) NOT NULL,
    codpoblamiento_pobl character varying(2) NOT NULL,
    observ character varying(50),
    fecha_inicio_serv date,
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave_ed)::text = 'ED'::text))
);


ALTER TABLE public.eiel_tr_saneam_ed_pobl OWNER TO postgres;

--
-- TOC entry 593 (class 1259 OID 77809212)
-- Name: eiel_tr_saneam_pv_pobl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_tr_saneam_pv_pobl (
    clave_pv character varying(2) NOT NULL,
    codprov_pv character varying(2) NOT NULL,
    codmunic_pv character varying(3) NOT NULL,
    orden_pv character varying(3) NOT NULL,
    codprov_pobl character varying(2) NOT NULL,
    codmunic_pobl character varying(3) NOT NULL,
    codentidad_pobl character varying(4) NOT NULL,
    codpoblamiento_pobl character varying(2) NOT NULL,
    observ character varying(50),
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_tr_saneam_pv_pobl OWNER TO postgres;

--
-- TOC entry 594 (class 1259 OID 77809217)
-- Name: eiel_tr_saneam_tcl_pobl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_tr_saneam_tcl_pobl (
    clave_tcl character varying(2) NOT NULL,
    codprov_tcl character varying(2) NOT NULL,
    codmunic_tcl character varying(3) NOT NULL,
    tramo_cl character varying(3) NOT NULL,
    codprov_pobl character varying(2) NOT NULL,
    codmunic_pobl character varying(3) NOT NULL,
    codentidad_pobl character varying(4) NOT NULL,
    codpoblamiento_pobl character varying(2) NOT NULL,
    pmi numeric(8,2),
    pmf numeric(8,2),
    observ character varying(50),
    fecha_inicio_serv date,
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clave_tclchk CHECK (((clave_tcl)::text = 'CL'::text))
);


ALTER TABLE public.eiel_tr_saneam_tcl_pobl OWNER TO postgres;

--
-- TOC entry 595 (class 1259 OID 77809223)
-- Name: eiel_tr_saneam_tem_pobl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_tr_saneam_tem_pobl (
    clave_tem character varying(2) NOT NULL,
    codprov_tem character varying(2) NOT NULL,
    codmunic_tem character varying(3) NOT NULL,
    tramo_em character varying(3) NOT NULL,
    pmi numeric(8,2),
    pmf numeric(8,2),
    codprov_pobl character varying(2) NOT NULL,
    codmunic_pobl character varying(3) NOT NULL,
    codentidad_pobl character varying(4) NOT NULL,
    codpoblamiento_pobl character varying(2) NOT NULL,
    observ character varying(50),
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clave_temchk CHECK (((clave_tem)::text = 'EM'::text))
);


ALTER TABLE public.eiel_tr_saneam_tem_pobl OWNER TO postgres;

--
-- TOC entry 596 (class 1259 OID 77809229)
-- Name: eiel_tr_saneam_tem_pv; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_tr_saneam_tem_pv (
    clave_tem character varying(2) NOT NULL,
    codprov_tem character varying(2) NOT NULL,
    codmunic_tem character varying(3) NOT NULL,
    tramo_em character varying(3) NOT NULL,
    pmi numeric(8,2),
    pmf numeric(8,2),
    codprov_pv character varying(2) NOT NULL,
    codmunic_pv character varying(3) NOT NULL,
    observ character varying(50),
    fecha_revision date,
    estado_revision integer,
    clave_pv character varying(3),
    orden_pv character varying(3),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_tr_saneam_tem_pv OWNER TO geopista;

--
-- TOC entry 597 (class 1259 OID 77809234)
-- Name: eiel_tr_vt_pobl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_tr_vt_pobl (
    clave_vt character varying(3) NOT NULL,
    codprov_vt character varying(3) NOT NULL,
    codmunic_vt character varying(3) NOT NULL,
    orden_vt character varying(3) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    fecha_alta date,
    observ character varying(250),
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clave_vtchk CHECK (((clave_vt)::text = 'VT'::text))
);


ALTER TABLE public.eiel_tr_vt_pobl OWNER TO postgres;

--
-- TOC entry 7546 (class 2606 OID 97233923)
-- Name: eiel_t1_saneam_ed_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t1_saneam_ed
    ADD CONSTRAINT eiel_t1_saneam_ed_pkey PRIMARY KEY (clave, codprov, codmunic, orden_ed, revision_actual);


--
-- TOC entry 7548 (class 2606 OID 97233925)
-- Name: eiel_t2_saneam_ed_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t2_saneam_ed
    ADD CONSTRAINT eiel_t2_saneam_ed_pkey PRIMARY KEY (clave, codprov, codmunic, orden_ed, revision_actual);


--
-- TOC entry 7554 (class 2606 OID 97233927)
-- Name: eiel_t_abast_au_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_abast_au
    ADD CONSTRAINT eiel_t_abast_au_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7560 (class 2606 OID 97233929)
-- Name: eiel_t_abast_de_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_abast_de
    ADD CONSTRAINT eiel_t_abast_de_pkey PRIMARY KEY (clave, codprov, codmunic, orden_de, revision_actual);


--
-- TOC entry 7562 (class 2606 OID 97233931)
-- Name: eiel_t_abast_serv_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_abast_serv
    ADD CONSTRAINT eiel_t_abast_serv_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7568 (class 2606 OID 97233933)
-- Name: eiel_t_abast_tcn_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_abast_tcn
    ADD CONSTRAINT eiel_t_abast_tcn_pkey PRIMARY KEY (clave, codprov, codmunic, tramo_cn, revision_actual);


--
-- TOC entry 7570 (class 2606 OID 97233935)
-- Name: eiel_t_abast_tp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_abast_tp
    ADD CONSTRAINT eiel_t_abast_tp_pkey PRIMARY KEY (clave, codprov, codmunic, orden_tp, revision_actual);


--
-- TOC entry 7576 (class 2606 OID 97233937)
-- Name: eiel_t_as_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_as
    ADD CONSTRAINT eiel_t_as_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_as, revision_actual);


--
-- TOC entry 7578 (class 2606 OID 97233939)
-- Name: eiel_t_cabildo_consejo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_cabildo_consejo
    ADD CONSTRAINT eiel_t_cabildo_consejo_pkey PRIMARY KEY (codprov, cod_isla, revision_actual);


--
-- TOC entry 7581 (class 2606 OID 97233941)
-- Name: eiel_t_cc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_cc
    ADD CONSTRAINT eiel_t_cc_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_cc, revision_actual);


--
-- TOC entry 7583 (class 2606 OID 97233943)
-- Name: eiel_t_cc_usos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_cc_usos
    ADD CONSTRAINT eiel_t_cc_usos_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_cc, uso, revision_actual);


--
-- TOC entry 7585 (class 2606 OID 97233945)
-- Name: eiel_t_ce_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_ce
    ADD CONSTRAINT eiel_t_ce_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_ce, revision_actual);


--
-- TOC entry 7587 (class 2606 OID 97233947)
-- Name: eiel_t_cu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_cu
    ADD CONSTRAINT eiel_t_cu_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_cu, revision_actual);


--
-- TOC entry 7589 (class 2606 OID 97233949)
-- Name: eiel_t_cu_usos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_cu_usos
    ADD CONSTRAINT eiel_t_cu_usos_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_cu, uso, revision_actual);


--
-- TOC entry 7593 (class 2606 OID 97233951)
-- Name: eiel_t_en_nivel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_en_nivel
    ADD CONSTRAINT eiel_t_en_nivel_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_en, nivel, revision_actual);


--
-- TOC entry 7591 (class 2606 OID 97233953)
-- Name: eiel_t_en_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_en
    ADD CONSTRAINT eiel_t_en_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_en, revision_actual);


--
-- TOC entry 7595 (class 2606 OID 97233955)
-- Name: eiel_t_entidad_singular_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_entidad_singular
    ADD CONSTRAINT eiel_t_entidad_singular_pkey PRIMARY KEY (codprov, codmunic, codentidad, revision_actual);


--
-- TOC entry 7597 (class 2606 OID 97233958)
-- Name: eiel_t_entidades_agrupadas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_entidades_agrupadas
    ADD CONSTRAINT eiel_t_entidades_agrupadas_pkey PRIMARY KEY (codmunicipio, codentidad, codnucleo, revision_actual);


--
-- TOC entry 7601 (class 2606 OID 97233960)
-- Name: eiel_t_id_deportes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_id_deportes
    ADD CONSTRAINT eiel_t_id_deportes_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_id, tipo_deporte, revision_actual);


--
-- TOC entry 7599 (class 2606 OID 97233962)
-- Name: eiel_t_id_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_id
    ADD CONSTRAINT eiel_t_id_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_id, revision_actual);


--
-- TOC entry 7603 (class 2606 OID 97233964)
-- Name: eiel_t_inf_ttmm_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_inf_ttmm
    ADD CONSTRAINT eiel_t_inf_ttmm_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7605 (class 2606 OID 97233966)
-- Name: eiel_t_ip_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_ip
    ADD CONSTRAINT eiel_t_ip_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_ip, revision_actual);


--
-- TOC entry 7607 (class 2606 OID 97233968)
-- Name: eiel_t_lm_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_lm
    ADD CONSTRAINT eiel_t_lm_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_lm, revision_actual);


--
-- TOC entry 7609 (class 2606 OID 97233970)
-- Name: eiel_t_mt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_mt
    ADD CONSTRAINT eiel_t_mt_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_mt, revision_actual);


--
-- TOC entry 7611 (class 2606 OID 97233972)
-- Name: eiel_t_mun_diseminados_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_mun_diseminados
    ADD CONSTRAINT eiel_t_mun_diseminados_pkey PRIMARY KEY (codprov, codmunic, revision_actual);


--
-- TOC entry 7613 (class 2606 OID 97233974)
-- Name: eiel_t_nucl_encuest_1_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_nucl_encuest_1
    ADD CONSTRAINT eiel_t_nucl_encuest_1_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7619 (class 2606 OID 97233976)
-- Name: eiel_t_nucl_encuest_2_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_nucl_encuest_2
    ADD CONSTRAINT eiel_t_nucl_encuest_2_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7625 (class 2606 OID 97233978)
-- Name: eiel_t_nucleo_aband_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_nucleo_aband
    ADD CONSTRAINT eiel_t_nucleo_aband_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7627 (class 2606 OID 97233980)
-- Name: eiel_t_otros_serv_munic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_otros_serv_munic
    ADD CONSTRAINT eiel_t_otros_serv_munic_pkey PRIMARY KEY (codprov, codmunic, revision_actual);


--
-- TOC entry 7629 (class 2606 OID 97233982)
-- Name: eiel_t_padron_nd_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_padron_nd
    ADD CONSTRAINT eiel_t_padron_nd_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7635 (class 2606 OID 97233984)
-- Name: eiel_t_padron_ttmm_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_padron_ttmm
    ADD CONSTRAINT eiel_t_padron_ttmm_pkey PRIMARY KEY (codprov, codmunic, revision_actual);


--
-- TOC entry 7639 (class 2606 OID 97233986)
-- Name: eiel_t_pj_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_pj
    ADD CONSTRAINT eiel_t_pj_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_pj, revision_actual);


--
-- TOC entry 7641 (class 2606 OID 97233988)
-- Name: eiel_t_planeam_urban_pkey; Type: CONSTRAINT; Schema: public; Owner: consultas; Tablespace: 
--

ALTER TABLE ONLY eiel_t_planeam_urban
    ADD CONSTRAINT eiel_t_planeam_urban_pkey PRIMARY KEY (codprov, codmunic, tipo_urba, orden_plan, revision_actual);


--
-- TOC entry 7643 (class 2606 OID 97233990)
-- Name: eiel_t_poblamiento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_poblamiento
    ADD CONSTRAINT eiel_t_poblamiento_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7645 (class 2606 OID 97233992)
-- Name: eiel_t_rb_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_rb
    ADD CONSTRAINT eiel_t_rb_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, tipo, revision_actual);


--
-- TOC entry 7647 (class 2606 OID 97233994)
-- Name: eiel_t_rb_serv_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_rb_serv
    ADD CONSTRAINT eiel_t_rb_serv_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7653 (class 2606 OID 97233999)
-- Name: eiel_t_sa_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_sa
    ADD CONSTRAINT eiel_t_sa_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_sa, revision_actual);


--
-- TOC entry 7655 (class 2606 OID 97234001)
-- Name: eiel_t_saneam_au_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_saneam_au
    ADD CONSTRAINT eiel_t_saneam_au_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7662 (class 2606 OID 97234003)
-- Name: eiel_t_saneam_pv_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_saneam_pv
    ADD CONSTRAINT eiel_t_saneam_pv_pkey PRIMARY KEY (clave, codprov, codmunic, orden_pv, revision_actual);


--
-- TOC entry 7668 (class 2606 OID 97234005)
-- Name: eiel_t_saneam_serv_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_saneam_serv
    ADD CONSTRAINT eiel_t_saneam_serv_pkey PRIMARY KEY (codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7670 (class 2606 OID 97234007)
-- Name: eiel_t_saneam_tcl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_saneam_tcl
    ADD CONSTRAINT eiel_t_saneam_tcl_pkey PRIMARY KEY (clave, codprov, codmunic, tramo_cl, revision_actual);


--
-- TOC entry 7672 (class 2606 OID 97234009)
-- Name: eiel_t_saneam_tem_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_saneam_tem
    ADD CONSTRAINT eiel_t_saneam_tem_pkey PRIMARY KEY (clave, codprov, codmunic, tramo_em, revision_actual);


--
-- TOC entry 7678 (class 2606 OID 97234011)
-- Name: eiel_t_su_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_su
    ADD CONSTRAINT eiel_t_su_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_su, revision_actual);


--
-- TOC entry 7680 (class 2606 OID 97234013)
-- Name: eiel_t_ta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_ta
    ADD CONSTRAINT eiel_t_ta_pkey PRIMARY KEY (clave, codprov, codmunic, codentidad, codpoblamiento, orden_ta, revision_actual);


--
-- TOC entry 7682 (class 2606 OID 97234015)
-- Name: eiel_t_vt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_t_vt
    ADD CONSTRAINT eiel_t_vt_pkey PRIMARY KEY (clave, codprov, codmunic, orden_vt, revision_actual);


--
-- TOC entry 7684 (class 2606 OID 97234017)
-- Name: eiel_tr_abast_ca_pobl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_abast_ca_pobl
    ADD CONSTRAINT eiel_tr_abast_ca_pobl_pkey PRIMARY KEY (clave_ca, codprov_ca, codmunic_ca, orden_ca, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, revision_actual);


--
-- TOC entry 7686 (class 2606 OID 97234019)
-- Name: eiel_tr_abast_de_pobl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_abast_de_pobl
    ADD CONSTRAINT eiel_tr_abast_de_pobl_pkey PRIMARY KEY (clave_de, codprov_de, codmunic_de, orden_de, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, revision_actual);


--
-- TOC entry 7688 (class 2606 OID 97234021)
-- Name: eiel_tr_abast_tcn_pobl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_abast_tcn_pobl
    ADD CONSTRAINT eiel_tr_abast_tcn_pobl_pkey PRIMARY KEY (clave_tcn, codprov_tcn, codmunic_tcn, tramo_tcn, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, revision_actual);


--
-- TOC entry 7694 (class 2606 OID 97234023)
-- Name: eiel_tr_abast_tp_pobl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_abast_tp_pobl
    ADD CONSTRAINT eiel_tr_abast_tp_pobl_pkey PRIMARY KEY (clave_tp, codprov_tp, codmunic_tp, orden_tp, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, revision_actual);


--
-- TOC entry 7696 (class 2606 OID 97234025)
-- Name: eiel_tr_saneam_ed_pobl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_saneam_ed_pobl
    ADD CONSTRAINT eiel_tr_saneam_ed_pobl_pkey PRIMARY KEY (clave_ed, codmunic_ed, codprov_ed, orden_ed, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, revision_actual);


--
-- TOC entry 7698 (class 2606 OID 97234027)
-- Name: eiel_tr_saneam_pv_pobl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_saneam_pv_pobl
    ADD CONSTRAINT eiel_tr_saneam_pv_pobl_pkey PRIMARY KEY (clave_pv, codprov_pv, codmunic_pv, orden_pv, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, revision_actual);


--
-- TOC entry 7700 (class 2606 OID 97234029)
-- Name: eiel_tr_saneam_tcl_pobl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_saneam_tcl_pobl
    ADD CONSTRAINT eiel_tr_saneam_tcl_pobl_pkey PRIMARY KEY (clave_tcl, codprov_tcl, codmunic_tcl, tramo_cl, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, revision_actual);


--
-- TOC entry 7702 (class 2606 OID 97234031)
-- Name: eiel_tr_saneam_tem_pobl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_saneam_tem_pobl
    ADD CONSTRAINT eiel_tr_saneam_tem_pobl_pkey PRIMARY KEY (clave_tem, codprov_tem, codmunic_tem, tramo_em, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, revision_actual);


--
-- TOC entry 7704 (class 2606 OID 97234033)
-- Name: eiel_tr_saneam_tem_pv_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_saneam_tem_pv
    ADD CONSTRAINT eiel_tr_saneam_tem_pv_pkey PRIMARY KEY (clave_tem, codprov_tem, codmunic_tem, tramo_em, codprov_pv, codmunic_pv, revision_actual);


--
-- TOC entry 7706 (class 2606 OID 97234035)
-- Name: eiel_tr_vt_pobl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_tr_vt_pobl
    ADD CONSTRAINT eiel_tr_vt_pobl_pkey PRIMARY KEY (clave_vt, codprov_vt, codmunic_vt, orden_vt, codprov, codmunic, codentidad, codpoblamiento, revision_actual);


--
-- TOC entry 7689 (class 1259 OID 97234903)
-- Name: i_clave_tcn_pobl; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_clave_tcn_pobl ON eiel_tr_abast_tcn_pobl USING btree (clave_tcn);


--
-- TOC entry 7614 (class 1259 OID 97234906)
-- Name: i_codentidad_nucl_encuest_1; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codentidad_nucl_encuest_1 ON eiel_t_nucl_encuest_1 USING btree (codentidad);


--
-- TOC entry 7620 (class 1259 OID 97234907)
-- Name: i_codentidad_nucl_encuest_2; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codentidad_nucl_encuest_2 ON eiel_t_nucl_encuest_2 USING btree (codentidad);


--
-- TOC entry 7630 (class 1259 OID 97234908)
-- Name: i_codentidad_padron_nd; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codentidad_padron_nd ON eiel_t_padron_nd USING btree (codentidad);


--
-- TOC entry 7648 (class 1259 OID 97234910)
-- Name: i_codentidad_rb_serv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codentidad_rb_serv ON eiel_t_rb_serv USING btree (codentidad);


--
-- TOC entry 7563 (class 1259 OID 97234911)
-- Name: i_codentidad_serv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codentidad_serv ON eiel_t_abast_serv USING btree (codentidad);


--
-- TOC entry 7555 (class 1259 OID 97234914)
-- Name: i_codmunic_ca; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_ca ON eiel_t_abast_ca USING btree (codmunic);


--
-- TOC entry 7564 (class 1259 OID 97234919)
-- Name: i_codmunic_serv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_serv ON eiel_t_abast_serv USING btree (codmunic);


--
-- TOC entry 7615 (class 1259 OID 97234920)
-- Name: i_codmunic_t_nucl_encuest_1; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_t_nucl_encuest_1 ON eiel_t_nucl_encuest_1 USING btree (codmunic);


--
-- TOC entry 7621 (class 1259 OID 97234921)
-- Name: i_codmunic_t_nucl_encuest_2; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_t_nucl_encuest_2 ON eiel_t_nucl_encuest_2 USING btree (codmunic);


--
-- TOC entry 7631 (class 1259 OID 97234922)
-- Name: i_codmunic_t_padron_nd; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_t_padron_nd ON eiel_t_padron_nd USING btree (codmunic);


--
-- TOC entry 7636 (class 1259 OID 97234923)
-- Name: i_codmunic_t_padron_ttmm; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_t_padron_ttmm ON eiel_t_padron_ttmm USING btree (codmunic);


--
-- TOC entry 7649 (class 1259 OID 97234924)
-- Name: i_codmunic_t_rb_serv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_t_rb_serv ON eiel_t_rb_serv USING btree (codmunic);


--
-- TOC entry 7690 (class 1259 OID 97234925)
-- Name: i_codmunic_tcn_pobl; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_tcn_pobl ON eiel_tr_abast_tcn_pobl USING btree (codmunic_tcn);


--
-- TOC entry 7673 (class 1259 OID 97234926)
-- Name: i_codmunic_tem; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_tem ON eiel_t_saneam_tem USING btree (codmunic);


--
-- TOC entry 7616 (class 1259 OID 97234929)
-- Name: i_codpoblamiento_nucl_encuest_1; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codpoblamiento_nucl_encuest_1 ON eiel_t_nucl_encuest_1 USING btree (codpoblamiento);


--
-- TOC entry 7622 (class 1259 OID 97234930)
-- Name: i_codpoblamiento_nucl_encuest_2; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codpoblamiento_nucl_encuest_2 ON eiel_t_nucl_encuest_2 USING btree (codpoblamiento);


--
-- TOC entry 7632 (class 1259 OID 97234932)
-- Name: i_codpoblamiento_padron_nd; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codpoblamiento_padron_nd ON eiel_t_padron_nd USING btree (codpoblamiento);


--
-- TOC entry 7650 (class 1259 OID 97234933)
-- Name: i_codpoblamiento_rb_serv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codpoblamiento_rb_serv ON eiel_t_rb_serv USING btree (codpoblamiento);


--
-- TOC entry 7565 (class 1259 OID 97234934)
-- Name: i_codpoblamiento_serv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codpoblamiento_serv ON eiel_t_abast_serv USING btree (codpoblamiento);


--
-- TOC entry 7556 (class 1259 OID 97234936)
-- Name: i_codprov_ca; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_ca ON eiel_t_abast_ca USING btree (codprov);


--
-- TOC entry 7566 (class 1259 OID 97234939)
-- Name: i_codprov_serv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_serv ON eiel_t_abast_serv USING btree (codprov);


--
-- TOC entry 7617 (class 1259 OID 97234940)
-- Name: i_codprov_t_nucl_encuest_1; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_t_nucl_encuest_1 ON eiel_t_nucl_encuest_1 USING btree (codprov);


--
-- TOC entry 7623 (class 1259 OID 97234941)
-- Name: i_codprov_t_nucl_encuest_2; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_t_nucl_encuest_2 ON eiel_t_nucl_encuest_2 USING btree (codprov);


--
-- TOC entry 7633 (class 1259 OID 97234942)
-- Name: i_codprov_t_padron_nd; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_t_padron_nd ON eiel_t_padron_nd USING btree (codprov);


--
-- TOC entry 7637 (class 1259 OID 97234943)
-- Name: i_codprov_t_padron_ttmm; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_t_padron_ttmm ON eiel_t_padron_ttmm USING btree (codprov);


--
-- TOC entry 7651 (class 1259 OID 97234944)
-- Name: i_codprov_t_rb_serv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_t_rb_serv ON eiel_t_rb_serv USING btree (codprov);


--
-- TOC entry 7691 (class 1259 OID 97234945)
-- Name: i_codprov_tcn_pobl; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_tcn_pobl ON eiel_tr_abast_tcn_pobl USING btree (codprov_tcn);


--
-- TOC entry 7674 (class 1259 OID 97234946)
-- Name: i_codprov_tem; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_tem ON eiel_t_saneam_tem USING btree (codprov);


--
-- TOC entry 7579 (class 1259 OID 97234990)
-- Name: i_id_t_carreteras; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_t_carreteras ON eiel_t_carreteras USING btree (id_municipio);


--
-- TOC entry 7557 (class 1259 OID 97234997)
-- Name: i_orden_ca; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_orden_ca ON eiel_t_abast_ca USING btree (orden_ca);


--
-- TOC entry 7549 (class 1259 OID 97234998)
-- Name: i_t2_clave_ed; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t2_clave_ed ON eiel_t2_saneam_ed USING btree (clave);


--
-- TOC entry 7550 (class 1259 OID 97234999)
-- Name: i_t2_codmunic_ed; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t2_codmunic_ed ON eiel_t2_saneam_ed USING btree (codmunic);


--
-- TOC entry 7551 (class 1259 OID 97235000)
-- Name: i_t2_codprov_ed; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t2_codprov_ed ON eiel_t2_saneam_ed USING btree (codprov);


--
-- TOC entry 7552 (class 1259 OID 97235001)
-- Name: i_t2_orden_ed; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t2_orden_ed ON eiel_t2_saneam_ed USING btree (orden_ed);


--
-- TOC entry 7656 (class 1259 OID 97235002)
-- Name: i_t_clave_au; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_clave_au ON eiel_t_saneam_au USING btree (clave);


--
-- TOC entry 7558 (class 1259 OID 97235003)
-- Name: i_t_clave_ca; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_clave_ca ON eiel_t_abast_ca USING btree (clave);


--
-- TOC entry 7675 (class 1259 OID 97235004)
-- Name: i_t_clave_em; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_clave_em ON eiel_t_saneam_tem USING btree (clave);


--
-- TOC entry 7663 (class 1259 OID 97235005)
-- Name: i_t_clave_pv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_clave_pv ON eiel_t_saneam_pv USING btree (clave);


--
-- TOC entry 7571 (class 1259 OID 97235006)
-- Name: i_t_clave_tp; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_clave_tp ON eiel_t_abast_tp USING btree (clave);


--
-- TOC entry 7657 (class 1259 OID 97235007)
-- Name: i_t_codentidad_au; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_codentidad_au ON eiel_t_saneam_au USING btree (codentidad);


--
-- TOC entry 7658 (class 1259 OID 97235008)
-- Name: i_t_codmunic_au; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_codmunic_au ON eiel_t_saneam_au USING btree (codmunic);


--
-- TOC entry 7664 (class 1259 OID 97235009)
-- Name: i_t_codmunic_pv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_codmunic_pv ON eiel_t_saneam_pv USING btree (codmunic);


--
-- TOC entry 7572 (class 1259 OID 97235010)
-- Name: i_t_codmunic_tp; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_codmunic_tp ON eiel_t_abast_tp USING btree (codmunic);


--
-- TOC entry 7659 (class 1259 OID 97235011)
-- Name: i_t_codpoblamiento_au; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_codpoblamiento_au ON eiel_t_saneam_au USING btree (codpoblamiento);


--
-- TOC entry 7660 (class 1259 OID 97235012)
-- Name: i_t_codprov_au; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_codprov_au ON eiel_t_saneam_au USING btree (codprov);


--
-- TOC entry 7665 (class 1259 OID 97235013)
-- Name: i_t_codprov_pv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_codprov_pv ON eiel_t_saneam_pv USING btree (codprov);


--
-- TOC entry 7573 (class 1259 OID 97235014)
-- Name: i_t_codprov_tp; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_codprov_tp ON eiel_t_abast_tp USING btree (codprov);


--
-- TOC entry 7666 (class 1259 OID 97235015)
-- Name: i_t_orden_pv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_orden_pv ON eiel_t_saneam_pv USING btree (orden_pv);


--
-- TOC entry 7574 (class 1259 OID 97235016)
-- Name: i_t_orden_tp; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_orden_tp ON eiel_t_abast_tp USING btree (orden_tp);


--
-- TOC entry 7676 (class 1259 OID 97235017)
-- Name: i_t_tramo_em; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_t_tramo_em ON eiel_t_saneam_tem USING btree (tramo_em);


--
-- TOC entry 7692 (class 1259 OID 97235018)
-- Name: i_tramo_tcn_pobl; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_tramo_tcn_pobl ON eiel_tr_abast_tcn_pobl USING btree (tramo_tcn);


-- Completed on 2013-04-26 11:54:45

--
-- PostgreSQL database dump complete
--

