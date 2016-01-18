--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.8
-- Dumped by pg_dump version 9.2.3
-- Started on 2013-04-26 11:51:21

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = true;

--
-- TOC entry 439 (class 1259 OID 77807997)
-- Name: eiel_c_abast_ar; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_ar (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_ar character varying(4) NOT NULL,
    estado character varying(1),
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_abast_ar OWNER TO postgres;

--
-- TOC entry 440 (class 1259 OID 77808005)
-- Name: eiel_c_abast_ca; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_ca (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_ca character varying(3) NOT NULL,
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    observ character varying(100),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    red_asociada character varying
);


ALTER TABLE public.eiel_c_abast_ca OWNER TO postgres;

--
-- TOC entry 441 (class 1259 OID 77808013)
-- Name: eiel_c_abast_cb; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_cb (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_cb character varying(4) NOT NULL,
    pot_motor numeric(8,0),
    estado character varying(2),
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    observ character varying(250),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_abast_cb OWNER TO postgres;

--
-- TOC entry 442 (class 1259 OID 77808021)
-- Name: eiel_c_abast_de; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_de (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_de character varying(3) NOT NULL,
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    observ character varying(100),
    precision_dig character varying(2),
    red_asociada character varying,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'DE'::text))
);


ALTER TABLE public.eiel_c_abast_de OWNER TO postgres;

--
-- TOC entry 443 (class 1259 OID 77808030)
-- Name: eiel_c_abast_rd; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_rd (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    tramo_rd character varying(5),
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    red_asociada character varying,
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((gestor)::text = 'CO'::text) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT materialchk CHECK ((((((((((material)::text = 'FC'::text) OR ((material)::text = 'FU'::text)) OR ((material)::text = 'HO'::text)) OR ((material)::text = 'OT'::text)) OR ((material)::text = 'PC'::text)) OR ((material)::text = 'PE'::text)) OR ((material)::text = 'PL'::text)) OR ((material)::text = 'PV'::text))),
    CONSTRAINT titularchk CHECK ((((((((((titular)::text = 'CO'::text) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_c_abast_rd OWNER TO postgres;

--
-- TOC entry 444 (class 1259 OID 77808042)
-- Name: eiel_c_abast_tcn; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_tcn (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    tramo_cn character varying(3) NOT NULL,
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
    observ character varying(100),
    precision_dig character varying(2),
    pmi numeric(8,2),
    pmf numeric(8,2),
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    revision_actual numeric(10,0) NOT NULL,
    CONSTRAINT clavechk CHECK (((clave)::text = 'CN'::text)),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((gestor)::text = 'CO'::text) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT materialchk CHECK (((((((((material)::text = 'FC'::text) OR ((material)::text = 'FU'::text)) OR ((material)::text = 'HO'::text)) OR ((material)::text = 'OT'::text)) OR ((material)::text = 'PC'::text)) OR ((material)::text = 'PE'::text)) OR ((material)::text = 'PV'::text))),
    CONSTRAINT titularchk CHECK ((((((((((titular)::text = 'CO'::text) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_c_abast_tcn OWNER TO postgres;

--
-- TOC entry 445 (class 1259 OID 77808054)
-- Name: eiel_c_abast_tcn_backup; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_tcn_backup (
    id numeric(8,0),
    id_municipio numeric(5,0),
    "GEOMETRY" geometry,
    clave character varying(2),
    codprov character varying(2),
    codmunic character varying(3),
    tramo_cn character varying(3),
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
    observ character varying(100),
    precision_dig character varying(2),
    pmi numeric(8,2),
    pmf numeric(8,2),
    revision_expirada numeric(10,0),
    revision_actual numeric(10,0)
);


ALTER TABLE public.eiel_c_abast_tcn_backup OWNER TO postgres;

--
-- TOC entry 446 (class 1259 OID 77808060)
-- Name: eiel_c_abast_tcn_new; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_tcn_new (
    id_municipio numeric(5,0),
    clave character varying(2),
    codprov character varying(2),
    codmunic character varying(3),
    tramo_cn character varying(3),
    longitud integer,
    "GEOMETRY" geometry
);


ALTER TABLE public.eiel_c_abast_tcn_new OWNER TO postgres;

--
-- TOC entry 447 (class 1259 OID 77808066)
-- Name: eiel_c_abast_tcn_new2; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_tcn_new2 (
    id_municipio numeric(5,0),
    clave character varying(2),
    codprov character varying(2),
    codmunic character varying(3),
    tramo_cn character varying(3),
    longitud numeric(8,2)
);


ALTER TABLE public.eiel_c_abast_tcn_new2 OWNER TO postgres;

--
-- TOC entry 448 (class 1259 OID 77808069)
-- Name: eiel_c_abast_tp; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_abast_tp (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_tp character varying(4) NOT NULL,
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    observ character varying(100),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'TP'::text))
);


ALTER TABLE public.eiel_c_abast_tp OWNER TO postgres;

--
-- TOC entry 449 (class 1259 OID 77808078)
-- Name: eiel_c_alum_cmp; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_alum_cmp (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    potencia_inst integer,
    potencia_contratada integer,
    n_circuitos numeric(2,0),
    contador character varying(2),
    estado character varying(1),
    obra_ejec character varying(2),
    observ character varying(50),
    precision_dig character varying(2),
    orden_cmp character varying(3),
    fecha_inst date,
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_alum_cmp OWNER TO postgres;

--
-- TOC entry 450 (class 1259 OID 77808086)
-- Name: eiel_c_alum_eea; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_alum_eea (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_eea character varying(3),
    n_circuitos integer,
    estabilizador integer,
    estado character varying(1),
    potencia integer,
    obra_ejec character varying(2),
    precision_dig character varying(2),
    observ character varying(250),
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_alum_eea OWNER TO postgres;

--
-- TOC entry 451 (class 1259 OID 77808094)
-- Name: eiel_c_alum_pl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_alum_pl (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_pl character varying(4),
    sist_eficiencia_pl character varying(3),
    ah_ener_rfl character varying(2),
    ah_ener_rfi character varying(2),
    estado character varying(1),
    potencia double precision,
    tipo_lampara character varying(2),
    tipo_apoyo character varying(2),
    fecha_inst date,
    observ character varying(100),
    precision_dig character varying(2),
    n_lamparas integer,
    altura integer,
    tipo_luminaria character varying(2),
    obra_ejec character varying(2),
    orden_eea character varying(3),
    orden_cmp character varying(3),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT ah_ener_rfichk CHECK ((((ah_ener_rfi)::text = 'SI'::text) OR ((ah_ener_rfi)::text = 'NO'::text))),
    CONSTRAINT ah_ener_rflchk CHECK ((((ah_ener_rfl)::text = 'SI'::text) OR ((ah_ener_rfl)::text = 'NO'::text))),
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text)))
);


ALTER TABLE public.eiel_c_alum_pl OWNER TO postgres;

--
-- TOC entry 452 (class 1259 OID 77808105)
-- Name: eiel_c_alum_pl2; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_alum_pl2 (
    count bigint,
    codmunic character varying(3),
    codentidad character varying(4),
    codpoblamiento character varying(2),
    codprov character varying(2),
    ah_ener_rfl character varying(2),
    ah_ener_rfi character varying(2),
    estado character varying(1),
    potencia double precision,
    potencia_real numeric(4,1)
);


ALTER TABLE public.eiel_c_alum_pl2 OWNER TO postgres;

--
-- TOC entry 453 (class 1259 OID 77808108)
-- Name: eiel_c_alum_pl3; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_alum_pl3 (
    count bigint,
    codmunic character varying(3),
    codentidad character varying(4),
    codpoblamiento character varying(2),
    codprov character varying(2),
    ah_ener_rfl character varying(2),
    ah_ener_rfi character varying(2),
    estado character varying(1),
    potencia double precision,
    potencia_real numeric(4,1),
    revision_expirada numeric(10,0)
);


ALTER TABLE public.eiel_c_alum_pl3 OWNER TO postgres;

--
-- TOC entry 455 (class 1259 OID 77808117)
-- Name: eiel_c_as; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_as (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_as character varying(4) NOT NULL,
    observ character varying(250),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_as OWNER TO postgres;

--
-- TOC entry 456 (class 1259 OID 77808125)
-- Name: eiel_c_cc; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_cc (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_cc character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_cc OWNER TO postgres;

--
-- TOC entry 457 (class 1259 OID 77808133)
-- Name: eiel_c_ce; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_ce (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_ce character varying(3) NOT NULL,
    observ character varying(250),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_ce OWNER TO postgres;

--
-- TOC entry 458 (class 1259 OID 77808141)
-- Name: eiel_c_comarcas; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_comarcas (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codcomarca character varying(2) NOT NULL,
    nombre_comarca character varying(50),
    hectareas numeric(10,2),
    perimetro numeric(10,2),
    codmunic_capital1 character varying(3),
    codmunic_capital2 character varying(3),
    fecha_revision date,
    observ character varying(60),
    uso_utm character varying(5),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_comarcas OWNER TO postgres;

--
-- TOC entry 459 (class 1259 OID 77808149)
-- Name: eiel_c_cu; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_cu (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_cu character varying(3) NOT NULL,
    observ character varying(100),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_cu OWNER TO postgres;

--
-- TOC entry 460 (class 1259 OID 77808157)
-- Name: eiel_c_edificiosing; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_edificiosing (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    id_elemsing integer NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_el character varying(3) NOT NULL,
    tipo character varying(2),
    nombre character varying(150),
    obra_ejec character varying(2),
    fecha_revision date,
    estado_revision integer,
    observ character varying(250),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_edificiosing OWNER TO postgres;

--
-- TOC entry 461 (class 1259 OID 77808165)
-- Name: eiel_c_en; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_en (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_en character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_en OWNER TO postgres;

--
-- TOC entry 462 (class 1259 OID 77808173)
-- Name: eiel_c_id; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_id (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_id character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_id OWNER TO postgres;

--
-- TOC entry 463 (class 1259 OID 77808181)
-- Name: eiel_c_ip; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_ip (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_ip character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_ip OWNER TO postgres;

--
-- TOC entry 464 (class 1259 OID 77808189)
-- Name: eiel_c_lm; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_lm (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_lm character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_lm OWNER TO postgres;

--
-- TOC entry 465 (class 1259 OID 77808197)
-- Name: eiel_c_mt; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_mt (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_mt character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_mt OWNER TO postgres;

--
-- TOC entry 466 (class 1259 OID 77808205)
-- Name: eiel_c_municipios; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_municipios (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codprov character varying(2) NOT NULL,
    nombre_oficial character varying(50),
    cod_catastro character varying(3),
    codmunic character varying(3) NOT NULL,
    uso_utm character varying(5),
    id_comarca integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_municipios OWNER TO postgres;

--
-- TOC entry 467 (class 1259 OID 77808213)
-- Name: eiel_c_municipios_puntos; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_municipios_puntos (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    nombre_oficial character varying(50),
    cod_catastro character varying(3),
    id_comarca integer,
    huso_utm character varying(5),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_municipios_puntos OWNER TO postgres;

--
-- TOC entry 468 (class 1259 OID 77808221)
-- Name: eiel_c_nucleo_aband; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_c_nucleo_aband (
    id numeric(8,0),
    id_municipio numeric(5,0),
    "GEOMETRY" geometry,
    prov character varying(2),
    mun character varying(3),
    ent character varying(4),
    poblamiento character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_nucleo_aband OWNER TO geopista;

--
-- TOC entry 469 (class 1259 OID 77808229)
-- Name: eiel_c_nucleo_poblacion; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_nucleo_poblacion (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    nombre_oficial character varying(50) NOT NULL,
    fecha_alta date,
    observ character varying(250),
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_nucleo_poblacion OWNER TO postgres;

--
-- TOC entry 470 (class 1259 OID 77808237)
-- Name: eiel_c_nucleos_puntos; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_nucleos_puntos (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    nombre_oficial character varying(50) NOT NULL,
    fecha_alta date,
    observ character varying(50),
    fecha_revision date,
    estado_revision integer,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_nucleos_puntos OWNER TO postgres;

--
-- TOC entry 471 (class 1259 OID 77808245)
-- Name: eiel_c_parcelas; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_parcelas (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codmasa character varying(3) NOT NULL,
    codparcela character varying(3) NOT NULL,
    num_policia integer,
    num_pol_dup character varying(1),
    clave_infr character varying(2),
    orden_infr character varying(3) NOT NULL,
    nombre_oficial character varying(150) NOT NULL,
    observ character varying(100),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_parcelas OWNER TO postgres;

--
-- TOC entry 472 (class 1259 OID 77808253)
-- Name: eiel_c_parroquias; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_parroquias (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    id_parroquia integer NOT NULL,
    "GEOMETRY" geometry,
    nombre_parroquia character varying(150),
    codmunic character varying(3) NOT NULL,
    codprov character varying(2) NOT NULL,
    fecha_revision date,
    observ character varying(250),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_parroquias OWNER TO postgres;

--
-- TOC entry 473 (class 1259 OID 77808261)
-- Name: eiel_c_pj; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_pj (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_pj character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_pj OWNER TO postgres;

--
-- TOC entry 474 (class 1259 OID 77808269)
-- Name: eiel_c_pj_temp; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_pj_temp (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_pj character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_pj_temp OWNER TO postgres;

--
-- TOC entry 475 (class 1259 OID 77808277)
-- Name: eiel_c_provincia; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_provincia (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codprov character varying(2) NOT NULL,
    nombre character varying(35),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_provincia OWNER TO postgres;

--
-- TOC entry 476 (class 1259 OID 77808285)
-- Name: eiel_c_redviaria_tu; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_redviaria_tu (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT estadochk CHECK (((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'NP'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT tipochk CHECK (((((tipo)::text = 'CA'::text) OR ((tipo)::text = 'OT'::text)) OR ((tipo)::text = 'TR'::text)))
);


ALTER TABLE public.eiel_c_redviaria_tu OWNER TO postgres;

--
-- TOC entry 477 (class 1259 OID 77808295)
-- Name: eiel_c_sa; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_sa (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_sa character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_sa OWNER TO postgres;

--
-- TOC entry 478 (class 1259 OID 77808303)
-- Name: eiel_c_saneam_ali; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_ali (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    acumulacion character varying(3) NOT NULL,
    pot_motor numeric(8,0),
    estado character varying(2),
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    observ character varying(250),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    codentidad character varying(4),
    codpoblamiento character varying(2)
);


ALTER TABLE public.eiel_c_saneam_ali OWNER TO postgres;

--
-- TOC entry 479 (class 1259 OID 77808311)
-- Name: eiel_c_saneam_cb; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_cb (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_cb character varying(4) NOT NULL,
    pot_motor numeric(8,0),
    estado character varying(2),
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    observ character varying(250),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_saneam_cb OWNER TO postgres;

--
-- TOC entry 480 (class 1259 OID 77808319)
-- Name: eiel_c_saneam_ed; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_ed (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2),
    codprov character varying(2),
    codmunic character varying(3),
    orden_ed character varying(3),
    observ character varying(100),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'ED'::text))
);


ALTER TABLE public.eiel_c_saneam_ed OWNER TO postgres;

--
-- TOC entry 481 (class 1259 OID 77808328)
-- Name: eiel_c_saneam_pr; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_pr (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_pr character varying(4) NOT NULL,
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    estado character varying(1),
    precision_dig character varying(2),
    fecha_inst date,
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL
);


ALTER TABLE public.eiel_c_saneam_pr OWNER TO postgres;

--
-- TOC entry 482 (class 1259 OID 77808336)
-- Name: eiel_c_saneam_pv; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_pv (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_pv character varying(4) NOT NULL,
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_saneam_pv OWNER TO postgres;

--
-- TOC entry 483 (class 1259 OID 77808344)
-- Name: eiel_c_saneam_rs; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_rs (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    tramo_rs character varying(10),
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT estadochk CHECK ((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text))),
    CONSTRAINT gestorchk CHECK ((((((((((gestor)::text = 'CO'::text) OR ((gestor)::text = 'EM'::text)) OR ((gestor)::text = 'EP'::text)) OR ((gestor)::text = 'MA'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PV'::text)) OR ((gestor)::text = 'VE'::text))),
    CONSTRAINT materialchk CHECK (((((((((material)::text = 'FC'::text) OR ((material)::text = 'FU'::text)) OR ((material)::text = 'HO'::text)) OR ((material)::text = 'OT'::text)) OR ((material)::text = 'PC'::text)) OR ((material)::text = 'PE'::text)) OR ((material)::text = 'PV'::text))),
    CONSTRAINT sist_impulsionchk CHECK ((((sist_impulsion)::text = 'GR'::text) OR ((sist_impulsion)::text = 'IM'::text))),
    CONSTRAINT tipo_red_interiorchk CHECK (((((tipo_red_interior)::text = 'MX'::text) OR ((tipo_red_interior)::text = 'PL'::text)) OR ((tipo_red_interior)::text = 'RE'::text))),
    CONSTRAINT titularchk CHECK ((((((((((titular)::text = 'CO'::text) OR ((titular)::text = 'EM'::text)) OR ((titular)::text = 'EP'::text)) OR ((titular)::text = 'MA'::text)) OR ((titular)::text = 'MU'::text)) OR ((titular)::text = 'OT'::text)) OR ((titular)::text = 'PV'::text)) OR ((titular)::text = 'VE'::text)))
);


ALTER TABLE public.eiel_c_saneam_rs OWNER TO postgres;

--
-- TOC entry 484 (class 1259 OID 77808358)
-- Name: eiel_c_saneam_tcl; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_tcl (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(3) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    tramo_cl character varying(3) NOT NULL,
    longitud numeric(8,2),
    diametro integer,
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    observ character varying(250),
    pmi numeric(8,2),
    pmf numeric(8,2),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'CL'::text))
);


ALTER TABLE public.eiel_c_saneam_tcl OWNER TO postgres;

--
-- TOC entry 485 (class 1259 OID 77808367)
-- Name: eiel_c_saneam_tcl_new; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_tcl_new (
    id_municipio numeric(5,0),
    clave character varying(3),
    codprov character varying(2),
    codmunic character varying(3),
    tramo_cl character varying(3),
    longitud integer,
    "GEOMETRY" geometry
);


ALTER TABLE public.eiel_c_saneam_tcl_new OWNER TO postgres;

--
-- TOC entry 486 (class 1259 OID 77808373)
-- Name: eiel_c_saneam_tcl_new2; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_tcl_new2 (
    id_municipio numeric(5,0),
    clave character varying(3),
    codprov character varying(2),
    codmunic character varying(3),
    tramo_cl character varying(3),
    longitud numeric(8,2)
);


ALTER TABLE public.eiel_c_saneam_tcl_new2 OWNER TO postgres;

--
-- TOC entry 487 (class 1259 OID 77808376)
-- Name: eiel_c_saneam_tem; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_saneam_tem (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    tramo_em character varying(3) NOT NULL,
    longitud numeric(8,2),
    long_terre numeric(8,2),
    long_marit numeric(8,2),
    diametro integer,
    cota_z numeric(8,0),
    obra_ejec character varying(2),
    observ character varying(100),
    pmi numeric(8,2),
    pmf numeric(8,2),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'EM'::text))
);


ALTER TABLE public.eiel_c_saneam_tem OWNER TO postgres;

--
-- TOC entry 488 (class 1259 OID 77808385)
-- Name: eiel_c_su; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_su (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_su character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_su OWNER TO postgres;

--
-- TOC entry 489 (class 1259 OID 77808393)
-- Name: eiel_c_suelo_rural; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_c_suelo_rural (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0),
    "GEOMETRY" geometry,
    prov character varying(2),
    mun character varying(3),
    tipo_urba character varying(3),
    estado_tra character varying(2),
    denominaci character varying(40),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_suelo_rural OWNER TO geopista;

--
-- TOC entry 490 (class 1259 OID 77808401)
-- Name: eiel_c_suelo_rural_preservado; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_c_suelo_rural_preservado (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0),
    "GEOMETRY" geometry,
    prov character varying(2),
    mun character varying(3),
    tipo_urba character varying(3),
    estado_tra character varying(2),
    denominaci character varying(40),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_suelo_rural_preservado OWNER TO geopista;

--
-- TOC entry 491 (class 1259 OID 77808409)
-- Name: eiel_c_suelo_urbano; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_c_suelo_urbano (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0),
    "GEOMETRY" geometry,
    prov character varying(2),
    mun character varying(3),
    tipo_urba character varying(3),
    estado_tra character varying(2),
    denominaci character varying(40),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_suelo_urbano OWNER TO geopista;

--
-- TOC entry 492 (class 1259 OID 77808417)
-- Name: eiel_c_ta; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_ta (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    codentidad character varying(4) NOT NULL,
    codpoblamiento character varying(2) NOT NULL,
    orden_ta character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint
);


ALTER TABLE public.eiel_c_ta OWNER TO postgres;

--
-- TOC entry 493 (class 1259 OID 77808425)
-- Name: eiel_c_tramos_carreteras; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_tramos_carreteras (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    cod_carrt character varying(9) NOT NULL,
    pki numeric(8,3),
    pkf numeric(8,3),
    gestor character varying(2),
    senaliza character varying(1),
    firme character varying(2),
    estado character varying(1),
    ancho numeric(4,1),
    longitud numeric(8,1),
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
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    longitud2 numeric(8,1),
    CONSTRAINT dimensionachk CHECK (((((dimensiona)::text = 'BD'::text) OR ((dimensiona)::text = 'MD'::text)) OR ((dimensiona)::text = 'RD'::text))),
    CONSTRAINT estadchk CHECK (((((((estado)::text = 'B'::text) OR ((estado)::text = 'E'::text)) OR ((estado)::text = 'M'::text)) OR ((estado)::text = 'R'::text)) OR ((estado)::text = 'N'::text))),
    CONSTRAINT firmechk CHECK ((((((((((firme)::text = 'AD'::text) OR ((firme)::text = 'HR'::text)) OR ((firme)::text = 'MB'::text)) OR ((firme)::text = 'MC'::text)) OR ((firme)::text = 'OT'::text)) OR ((firme)::text = 'RA'::text)) OR ((firme)::text = 'TI'::text)) OR ((firme)::text = 'ZE'::text))),
    CONSTRAINT fre_estrechchk CHECK ((((fre_estrech)::text = 'SI'::text) OR ((fre_estrech)::text = 'NO'::text))),
    CONSTRAINT gestorchk CHECK ((((((((gestor)::text = 'CA'::text) OR ((gestor)::text = 'ES'::text)) OR ((gestor)::text = 'MU'::text)) OR ((gestor)::text = 'OT'::text)) OR ((gestor)::text = 'PR'::text)) OR ((gestor)::text = 'NO'::text))),
    CONSTRAINT muy_sinuosochk CHECK ((((muy_sinuoso)::text = 'SI'::text) OR ((muy_sinuoso)::text = 'NO'::text))),
    CONSTRAINT pte_excesivachk CHECK ((((pte_excesiva)::text = 'SI'::text) OR ((pte_excesiva)::text = 'NO'::text))),
    CONSTRAINT senalizachk CHECK ((((((senaliza)::text = 'A'::text) OR ((senaliza)::text = 'H'::text)) OR ((senaliza)::text = 'N'::text)) OR ((senaliza)::text = 'V'::text)))
);


ALTER TABLE public.eiel_c_tramos_carreteras OWNER TO postgres;

--
-- TOC entry 494 (class 1259 OID 77808441)
-- Name: eiel_c_tramos_carreteras_new; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_tramos_carreteras_new (
    id_municipio numeric(5,0),
    pki numeric(8,3),
    pkf numeric(8,3),
    cod_carrt character varying(9),
    gestor character varying(2),
    senaliza character varying(1),
    firme character varying(2),
    estado character varying(1),
    paso_nivel integer,
    dimensiona character varying(2),
    muy_sinuoso character varying(2),
    pte_excesiva character varying(2),
    fre_estrech character varying(2),
    ancho numeric(4,1),
    longitud numeric,
    superficie numeric(8,0),
    "GEOMETRY" geometry
);


ALTER TABLE public.eiel_c_tramos_carreteras_new OWNER TO postgres;

--
-- TOC entry 495 (class 1259 OID 77808447)
-- Name: eiel_c_vt; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE eiel_c_vt (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    clave character varying(2) NOT NULL,
    codprov character varying(2) NOT NULL,
    codmunic character varying(3) NOT NULL,
    orden_vt character varying(3) NOT NULL,
    observ character varying(50),
    precision_dig character varying(2),
    revision_actual numeric(10,0) DEFAULT 0 NOT NULL,
    revision_expirada numeric(10,0) DEFAULT 9999999999::bigint,
    CONSTRAINT clavechk CHECK (((clave)::text = 'VT'::text))
);


ALTER TABLE public.eiel_c_vt OWNER TO postgres;

--
-- TOC entry 7320 (class 2606 OID 97233792)
-- Name: eiel_c_abast_cb_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_abast_cb
    ADD CONSTRAINT eiel_c_abast_cb_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7331 (class 2606 OID 97233795)
-- Name: eiel_c_alum_cmp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_alum_cmp
    ADD CONSTRAINT eiel_c_alum_cmp_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7336 (class 2606 OID 97233797)
-- Name: eiel_c_alum_eea_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_alum_eea
    ADD CONSTRAINT eiel_c_alum_eea_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7344 (class 2606 OID 97233799)
-- Name: eiel_c_comarcas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_comarcas
    ADD CONSTRAINT eiel_c_comarcas_pkey PRIMARY KEY (id, codcomarca, revision_actual);


--
-- TOC entry 7348 (class 2606 OID 97233801)
-- Name: eiel_c_edificiosing_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_edificiosing
    ADD CONSTRAINT eiel_c_edificiosing_pkey PRIMARY KEY (id_elemsing, revision_actual);


--
-- TOC entry 7356 (class 2606 OID 97233803)
-- Name: eiel_c_municipios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_municipios
    ADD CONSTRAINT eiel_c_municipios_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7361 (class 2606 OID 97233805)
-- Name: eiel_c_municipios_puntos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_municipios_puntos
    ADD CONSTRAINT eiel_c_municipios_puntos_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7370 (class 2606 OID 97233807)
-- Name: eiel_c_nucleos_puntos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_nucleos_puntos
    ADD CONSTRAINT eiel_c_nucleos_puntos_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7373 (class 2606 OID 97233809)
-- Name: eiel_c_parcelas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_parcelas
    ADD CONSTRAINT eiel_c_parcelas_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7381 (class 2606 OID 97233811)
-- Name: eiel_c_parroquias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_parroquias
    ADD CONSTRAINT eiel_c_parroquias_pkey PRIMARY KEY (id, id_parroquia, codmunic, revision_actual);


--
-- TOC entry 7387 (class 2606 OID 97233813)
-- Name: eiel_c_pj_temp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_pj_temp
    ADD CONSTRAINT eiel_c_pj_temp_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7389 (class 2606 OID 97233815)
-- Name: eiel_c_provincia_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_provincia
    ADD CONSTRAINT eiel_c_provincia_pkey PRIMARY KEY (id, codprov, revision_actual);


--
-- TOC entry 7395 (class 2606 OID 97233817)
-- Name: eiel_c_saneam_ali_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_saneam_ali
    ADD CONSTRAINT eiel_c_saneam_ali_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7398 (class 2606 OID 97233819)
-- Name: eiel_c_saneam_cb_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_saneam_cb
    ADD CONSTRAINT eiel_c_saneam_cb_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7409 (class 2606 OID 97233821)
-- Name: eiel_c_suelo_rural_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_c_suelo_rural
    ADD CONSTRAINT eiel_c_suelo_rural_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7411 (class 2606 OID 97233823)
-- Name: eiel_c_suelo_rural_preservado_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_c_suelo_rural_preservado
    ADD CONSTRAINT eiel_c_suelo_rural_preservado_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7413 (class 2606 OID 97233825)
-- Name: eiel_c_suelo_urbano_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_c_suelo_urbano
    ADD CONSTRAINT eiel_c_suelo_urbano_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7416 (class 2606 OID 97233827)
-- Name: eiel_c_tramos_carreteras_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY eiel_c_tramos_carreteras
    ADD CONSTRAINT eiel_c_tramos_carreteras_pkey PRIMARY KEY (id, revision_actual);


--
-- TOC entry 7314 (class 1259 OID 97234849)
-- Name: eiel_c_abast_ar_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_abast_ar_idmunicipio_key ON eiel_c_abast_ar USING btree (id_municipio);


--
-- TOC entry 7316 (class 1259 OID 97234850)
-- Name: eiel_c_abast_ca_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_abast_ca_idmunicipio_key ON eiel_c_abast_ca USING btree (id_municipio);


--
-- TOC entry 7318 (class 1259 OID 97234851)
-- Name: eiel_c_abast_cb_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_abast_cb_idmunicipio_key ON eiel_c_abast_cb USING btree (id_municipio);


--
-- TOC entry 7322 (class 1259 OID 97234852)
-- Name: eiel_c_abast_de_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_abast_de_idmunicipio_key ON eiel_c_abast_de USING btree (id_municipio);


--
-- TOC entry 7324 (class 1259 OID 97234853)
-- Name: eiel_c_abast_rd_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_abast_rd_idmunicipio_key ON eiel_c_abast_rd USING btree (id_municipio);


--
-- TOC entry 7326 (class 1259 OID 97234854)
-- Name: eiel_c_abast_tcn_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_abast_tcn_idmunicipio_key ON eiel_c_abast_tcn USING btree (id_municipio);


--
-- TOC entry 7328 (class 1259 OID 97234855)
-- Name: eiel_c_abast_tp_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_abast_tp_idmunicipio_key ON eiel_c_abast_tp USING btree (id_municipio);


--
-- TOC entry 7342 (class 1259 OID 97234856)
-- Name: eiel_c_comarcas_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_comarcas_idmunicipio_key ON eiel_c_comarcas USING btree (id);


--
-- TOC entry 7365 (class 1259 OID 97234857)
-- Name: eiel_c_nucleo_poblacion_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_nucleo_poblacion_idmunicipio_key ON eiel_c_nucleo_poblacion USING btree (id_municipio);


--
-- TOC entry 7371 (class 1259 OID 97234858)
-- Name: eiel_c_parcelas_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_parcelas_idmunicipio_key ON eiel_c_parcelas USING btree (id_municipio);


--
-- TOC entry 7400 (class 1259 OID 97234859)
-- Name: eiel_c_saneam_ed_idmunicipio_key; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX eiel_c_saneam_ed_idmunicipio_key ON eiel_c_saneam_ed USING btree (id_municipio);


--
-- TOC entry 7399 (class 1259 OID 97234900)
-- Name: i_c_id_cb; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_c_id_cb ON eiel_c_saneam_cb USING btree (id_municipio);


--
-- TOC entry 7401 (class 1259 OID 97234901)
-- Name: i_c_id_pr; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_c_id_pr ON eiel_c_saneam_pr USING btree (id_municipio);


--
-- TOC entry 7329 (class 1259 OID 97234902)
-- Name: i_c_id_tp; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_c_id_tp ON eiel_c_abast_tp USING btree (id);


--
-- TOC entry 7417 (class 1259 OID 97234904)
-- Name: i_cod_carrt_tramos_carreteras; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_cod_carrt_tramos_carreteras ON eiel_c_tramos_carreteras USING btree (cod_carrt);


--
-- TOC entry 7345 (class 1259 OID 97234905)
-- Name: i_codcomarca; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codcomarca ON eiel_c_comarcas USING btree (codcomarca);


--
-- TOC entry 7374 (class 1259 OID 97234909)
-- Name: i_codentidad_parcelas; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codentidad_parcelas ON eiel_c_parcelas USING btree (codentidad);


--
-- TOC entry 7375 (class 1259 OID 97234912)
-- Name: i_codmasa_parcelas; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmasa_parcelas ON eiel_c_parcelas USING btree (codmasa);


--
-- TOC entry 7332 (class 1259 OID 97234913)
-- Name: i_codmunic_alum_cmp; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_alum_cmp ON eiel_c_alum_cmp USING btree (codmunic);


--
-- TOC entry 7357 (class 1259 OID 97234915)
-- Name: i_codmunic_municipios; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_municipios ON eiel_c_municipios USING btree (codmunic);


--
-- TOC entry 7362 (class 1259 OID 97234916)
-- Name: i_codmunic_municipios_puntos; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_municipios_puntos ON eiel_c_municipios_puntos USING btree (codmunic);


--
-- TOC entry 7376 (class 1259 OID 97234917)
-- Name: i_codmunic_parcelas; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_parcelas ON eiel_c_parcelas USING btree (codmunic);


--
-- TOC entry 7382 (class 1259 OID 97234918)
-- Name: i_codmunic_parroquia; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_parroquia ON eiel_c_parroquias USING btree (codmunic);


--
-- TOC entry 7418 (class 1259 OID 97234927)
-- Name: i_codmunic_tramos_carreteras; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codmunic_tramos_carreteras ON eiel_c_tramos_carreteras USING btree (codmunic);


--
-- TOC entry 7377 (class 1259 OID 97234928)
-- Name: i_codparcela_parcelas; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codparcela_parcelas ON eiel_c_parcelas USING btree (codparcela);


--
-- TOC entry 7366 (class 1259 OID 97234931)
-- Name: i_codpoblamiento_nucleo_poblacion; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codpoblamiento_nucleo_poblacion ON eiel_c_nucleo_poblacion USING btree (codpoblamiento);


--
-- TOC entry 7333 (class 1259 OID 97234935)
-- Name: i_codprov_alum_cmp; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_alum_cmp ON eiel_c_alum_cmp USING btree (codprov);


--
-- TOC entry 7378 (class 1259 OID 97234937)
-- Name: i_codprov_parcelas; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_parcelas ON eiel_c_parcelas USING btree (codprov);


--
-- TOC entry 7390 (class 1259 OID 97234938)
-- Name: i_codprov_provincias; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_provincias ON eiel_c_provincia USING btree (codprov);


--
-- TOC entry 7419 (class 1259 OID 97234947)
-- Name: i_codprov_tramos_carreteras; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_codprov_tramos_carreteras ON eiel_c_tramos_carreteras USING btree (codprov);


--
-- TOC entry 7383 (class 1259 OID 97234951)
-- Name: i_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id ON eiel_c_parroquias USING btree (id);


--
-- TOC entry 7396 (class 1259 OID 97234952)
-- Name: i_id_ali; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_ali ON eiel_c_saneam_ali USING btree (id_municipio);


--
-- TOC entry 7334 (class 1259 OID 97234953)
-- Name: i_id_alum_cmp; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_alum_cmp ON eiel_c_alum_cmp USING btree (id_municipio);


--
-- TOC entry 7337 (class 1259 OID 97234954)
-- Name: i_id_alum_eea; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_alum_eea ON eiel_c_alum_eea USING btree (id_municipio);


--
-- TOC entry 7338 (class 1259 OID 97234955)
-- Name: i_id_alum_pl; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_alum_pl ON eiel_c_alum_pl USING btree (id_municipio);


--
-- TOC entry 7315 (class 1259 OID 97234959)
-- Name: i_id_ar; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_ar ON eiel_c_abast_ar USING btree (id);


--
-- TOC entry 7339 (class 1259 OID 97234960)
-- Name: i_id_as; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_as ON eiel_c_as USING btree (id_municipio);


--
-- TOC entry 7317 (class 1259 OID 97234961)
-- Name: i_id_ca; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_ca ON eiel_c_abast_ca USING btree (id);


--
-- TOC entry 7321 (class 1259 OID 97234962)
-- Name: i_id_cb; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_cb ON eiel_c_abast_cb USING btree (id);


--
-- TOC entry 7340 (class 1259 OID 97234963)
-- Name: i_id_cc; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_cc ON eiel_c_cc USING btree (id_municipio);


--
-- TOC entry 7341 (class 1259 OID 97234964)
-- Name: i_id_ce; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_ce ON eiel_c_ce USING btree (id_municipio);


--
-- TOC entry 7346 (class 1259 OID 97234965)
-- Name: i_id_cu; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_cu ON eiel_c_cu USING btree (id_municipio);


--
-- TOC entry 7323 (class 1259 OID 97234966)
-- Name: i_id_de; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_de ON eiel_c_abast_de USING btree (id);


--
-- TOC entry 7349 (class 1259 OID 97234967)
-- Name: i_id_edificiosing; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_edificiosing ON eiel_c_edificiosing USING btree (id_elemsing);


--
-- TOC entry 7350 (class 1259 OID 97234968)
-- Name: i_id_en; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_en ON eiel_c_en USING btree (id_municipio);


--
-- TOC entry 7351 (class 1259 OID 97234969)
-- Name: i_id_id; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_id ON eiel_c_id USING btree (id_municipio);


--
-- TOC entry 7352 (class 1259 OID 97234970)
-- Name: i_id_ip; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_ip ON eiel_c_ip USING btree (id_municipio);


--
-- TOC entry 7353 (class 1259 OID 97234971)
-- Name: i_id_lm; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_lm ON eiel_c_lm USING btree (id_municipio);


--
-- TOC entry 7354 (class 1259 OID 97234972)
-- Name: i_id_mt; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_mt ON eiel_c_mt USING btree (id_municipio);


--
-- TOC entry 7405 (class 1259 OID 97234973)
-- Name: i_id_mun_tem; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_mun_tem ON eiel_c_saneam_tem USING btree (id_municipio);


--
-- TOC entry 7358 (class 1259 OID 97234974)
-- Name: i_id_municipio_municipios; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_municipio_municipios ON eiel_c_municipios USING btree (id_municipio);


--
-- TOC entry 7363 (class 1259 OID 97234975)
-- Name: i_id_municipio_municipios_puntos; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_municipio_municipios_puntos ON eiel_c_municipios_puntos USING btree (id_municipio);


--
-- TOC entry 7359 (class 1259 OID 97234976)
-- Name: i_id_municipios; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_municipios ON eiel_c_municipios USING btree (id);


--
-- TOC entry 7364 (class 1259 OID 97234977)
-- Name: i_id_municipios_puntos; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_municipios_puntos ON eiel_c_municipios_puntos USING btree (id);


--
-- TOC entry 7367 (class 1259 OID 97234978)
-- Name: i_id_nucleo_poblacion; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_nucleo_poblacion ON eiel_c_nucleo_poblacion USING btree (id);


--
-- TOC entry 7368 (class 1259 OID 97234979)
-- Name: i_id_nucleo_puntos; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_nucleo_puntos ON eiel_c_nucleo_poblacion USING btree (id);


--
-- TOC entry 7379 (class 1259 OID 97234980)
-- Name: i_id_parcelas; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_parcelas ON eiel_c_parcelas USING btree (id);


--
-- TOC entry 7384 (class 1259 OID 97234981)
-- Name: i_id_parroquia; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_parroquia ON eiel_c_parroquias USING btree (id_parroquia);


--
-- TOC entry 7385 (class 1259 OID 97234982)
-- Name: i_id_pj; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_pj ON eiel_c_pj USING btree (id_municipio);


--
-- TOC entry 7391 (class 1259 OID 97234983)
-- Name: i_id_provincias; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_provincias ON eiel_c_provincia USING btree (id);


--
-- TOC entry 7402 (class 1259 OID 97234984)
-- Name: i_id_pv; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_pv ON eiel_c_saneam_pv USING btree (id_municipio);


--
-- TOC entry 7325 (class 1259 OID 97234985)
-- Name: i_id_rd; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_rd ON eiel_c_abast_rd USING btree (id);


--
-- TOC entry 7392 (class 1259 OID 97234986)
-- Name: i_id_red; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_red ON eiel_c_redviaria_tu USING btree (id_municipio);


--
-- TOC entry 7403 (class 1259 OID 97234987)
-- Name: i_id_rs; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_rs ON eiel_c_saneam_rs USING btree (id_municipio);


--
-- TOC entry 7393 (class 1259 OID 97234988)
-- Name: i_id_sa; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_sa ON eiel_c_sa USING btree (id_municipio);


--
-- TOC entry 7407 (class 1259 OID 97234989)
-- Name: i_id_su; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_su ON eiel_c_su USING btree (id_municipio);


--
-- TOC entry 7414 (class 1259 OID 97234991)
-- Name: i_id_ta; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_ta ON eiel_c_ta USING btree (id_municipio);


--
-- TOC entry 7404 (class 1259 OID 97234992)
-- Name: i_id_tcl; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_tcl ON eiel_c_saneam_tcl USING btree (id_municipio);


--
-- TOC entry 7327 (class 1259 OID 97234993)
-- Name: i_id_tcn; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_tcn ON eiel_c_abast_tcn USING btree (id);


--
-- TOC entry 7406 (class 1259 OID 97234994)
-- Name: i_id_tem; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_tem ON eiel_c_saneam_tem USING btree (id);


--
-- TOC entry 7420 (class 1259 OID 97234995)
-- Name: i_id_tramos_carreteras; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_tramos_carreteras ON eiel_c_tramos_carreteras USING btree (id_municipio);


--
-- TOC entry 7421 (class 1259 OID 97234996)
-- Name: i_id_vt; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX i_id_vt ON eiel_c_vt USING btree (id_municipio);


-- Completed on 2013-04-26 11:51:49

--
-- PostgreSQL database dump complete
--

