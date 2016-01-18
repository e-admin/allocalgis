--
-- PostgreSQL database dump
--

-- Dumped from database version 8.4.7
-- Dumped by pg_dump version 9.0.1
-- Started on 2011-07-28 10:21:59

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 11 (class 2615 OID 110160)
-- Name: cementerio; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA cementerio;


ALTER SCHEMA cementerio OWNER TO postgres;

SET search_path = cementerio, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = true;

--
-- TOC entry 4076 (class 1259 OID 131358)
-- Dependencies: 11
-- Name: anexo_cementerio; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE anexo_cementerio (
    id_documento integer NOT NULL,
    id_elemcementerio integer NOT NULL,
    tipo character varying NOT NULL,
    subtipo character varying NOT NULL
);


ALTER TABLE cementerio.anexo_cementerio OWNER TO geopista;

--
-- TOC entry 4077 (class 1259 OID 131363)
-- Dependencies: 11
-- Name: anexo_feature; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE anexo_feature (
    id_documento integer NOT NULL,
    id_layer integer NOT NULL,
    id_feature numeric(8,0) NOT NULL
);


ALTER TABLE cementerio.anexo_feature OWNER TO geopista;

--
-- TOC entry 4044 (class 1259 OID 110167)
-- Dependencies: 11
-- Name: bloque; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE bloque (
    id integer NOT NULL,
    tipo_bloque integer,
    num_filas integer,
    num_columnas integer,
    descripcion character varying,
    id_feature numeric(8,0) NOT NULL,
    id_elemcementerio integer
);


ALTER TABLE cementerio.bloque OWNER TO geopista;

--
-- TOC entry 4074 (class 1259 OID 130518)
-- Dependencies: 11
-- Name: bloque_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE bloque_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.bloque_id_seq OWNER TO geopista;

--
-- TOC entry 4073 (class 1259 OID 130508)
-- Dependencies: 11
-- Name: cementerio; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE cementerio (
    id integer NOT NULL,
    nombre character varying(255)
);


ALTER TABLE cementerio.cementerio OWNER TO geopista;

--
-- TOC entry 4055 (class 1259 OID 110234)
-- Dependencies: 11
-- Name: cementerio_feature; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE cementerio_feature (
    id_elem integer NOT NULL,
    id_feature numeric(8,0) NOT NULL,
    id_layer character varying(50) NOT NULL
);


ALTER TABLE cementerio.cementerio_feature OWNER TO geopista;

--
-- TOC entry 4051 (class 1259 OID 110200)
-- Dependencies: 11
-- Name: concesion; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE concesion (
    localizado character varying(50),
    fecha_ini date,
    fecha_fin date,
    ultima_renova date,
    ultimo_titular character varying(50),
    id_tarifa integer NOT NULL,
    id_unidad integer NOT NULL,
    estado integer,
    id integer NOT NULL,
    tipo_concesion integer,
    descripcion character varying,
    codigo character varying,
    precio_final character varying,
    bonificacion character varying
);


ALTER TABLE cementerio.concesion OWNER TO geopista;

--
-- TOC entry 4061 (class 1259 OID 120234)
-- Dependencies: 11
-- Name: concesion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE concesion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.concesion_id_seq OWNER TO geopista;

--
-- TOC entry 4050 (class 1259 OID 110197)
-- Dependencies: 11
-- Name: datosfallecimiento; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE datosfallecimiento (
    lugar character varying(50),
    poblacion character varying(50),
    causa_fundamental character varying(50),
    causa_inmediata character varying(50),
    fecha timestamp without time zone,
    id integer NOT NULL,
    medico character varying(50),
    num_colegiado character varying,
    registro_civill character varying,
    referencia character varying
);


ALTER TABLE cementerio.datosfallecimiento OWNER TO geopista;

--
-- TOC entry 4049 (class 1259 OID 110194)
-- Dependencies: 11
-- Name: difunto; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE difunto (
    fecha_defuncion timestamp without time zone,
    edad_difunto integer,
    id integer NOT NULL,
    id_datosfallecimiento integer NOT NULL,
    id_plaza integer NOT NULL,
    dni_persona character varying(15) NOT NULL,
    grupo integer,
    codigo character varying
);


ALTER TABLE cementerio.difunto OWNER TO geopista;

--
-- TOC entry 4071 (class 1259 OID 129868)
-- Dependencies: 11
-- Name: difunto_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE difunto_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.difunto_id_seq OWNER TO geopista;

--
-- TOC entry 4075 (class 1259 OID 131338)
-- Dependencies: 4397 4398 4399 4400 11
-- Name: documento; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE documento (
    id_documento integer NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    nombre character varying(255),
    fecha_alta timestamp without time zone,
    fecha_modificacion timestamp without time zone,
    tipo numeric(4,0),
    publico numeric(1,0) DEFAULT 1,
    tamanio numeric(12,0),
    comentario character varying(1000),
    thumbnail bytea,
    is_imgdoctext character(1) DEFAULT 'D'::bpchar,
    oculto numeric(1,0) DEFAULT 0,
    CONSTRAINT check_is_imgdoctext CHECK ((((is_imgdoctext = 'D'::bpchar) OR (is_imgdoctext = 'I'::bpchar)) OR (is_imgdoctext = 'T'::bpchar)))
);


ALTER TABLE cementerio.documento OWNER TO geopista;

--
-- TOC entry 4078 (class 1259 OID 131373)
-- Dependencies: 11
-- Name: documento_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE documento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.documento_id_seq OWNER TO geopista;

--
-- TOC entry 4079 (class 1259 OID 131705)
-- Dependencies: 11
-- Name: documento_tipos; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE documento_tipos (
    tipo numeric(4,0),
    extension character varying(25),
    mime_type character varying(100)
);


ALTER TABLE cementerio.documento_tipos OWNER TO geopista;

--
-- TOC entry 4043 (class 1259 OID 110161)
-- Dependencies: 11
-- Name: elem_cementerio; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE elem_cementerio (
    id integer NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    tipo character varying(2),
    nombre character varying(255),
    entidad character varying(255),
    id_cementerio integer,
    subtipo character varying
);


ALTER TABLE cementerio.elem_cementerio OWNER TO geopista;

--
-- TOC entry 4058 (class 1259 OID 120014)
-- Dependencies: 11
-- Name: elem_cementerio_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE elem_cementerio_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.elem_cementerio_id_seq OWNER TO geopista;

SET default_with_oids = false;

--
-- TOC entry 4083 (class 1259 OID 135845)
-- Dependencies: 11
-- Name: elem_feature; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE elem_feature (
    id integer NOT NULL,
    id_elem integer NOT NULL,
    id_layer character varying,
    id_cementerio integer,
    tipo character varying(2),
    subtipo character varying(2),
    nombre character varying,
    entidad character varying,
    id_feature numeric(8,0),
    id_municipio numeric(5,0)
);


ALTER TABLE cementerio.elem_feature OWNER TO geopista;

--
-- TOC entry 4084 (class 1259 OID 136603)
-- Dependencies: 11
-- Name: elem_feature_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE elem_feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.elem_feature_id_seq OWNER TO geopista;

SET default_with_oids = true;

--
-- TOC entry 4086 (class 1259 OID 136863)
-- Dependencies: 11
-- Name: exhumacion; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE exhumacion (
    informe character varying,
    id integer NOT NULL,
    contenedor integer,
    codigo character varying,
    fecha_exhumacion date,
    id_difunto integer,
    id_tarifa integer,
    tipo_exhumacion integer,
    red_restos integer,
    traslado integer,
    precio_final character varying,
    bonificacion character varying
);


ALTER TABLE cementerio.exhumacion OWNER TO geopista;

--
-- TOC entry 4085 (class 1259 OID 136763)
-- Dependencies: 11
-- Name: exhumacion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE exhumacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.exhumacion_id_seq OWNER TO geopista;

--
-- TOC entry 4072 (class 1259 OID 129870)
-- Dependencies: 11
-- Name: fallecimiento_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE fallecimiento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.fallecimiento_id_seq OWNER TO geopista;

SET default_with_oids = false;

--
-- TOC entry 4087 (class 1259 OID 136906)
-- Dependencies: 11
-- Name: historico_difuntos; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE historico_difuntos (
    id integer NOT NULL,
    id_difunto integer,
    tipo integer,
    id_elem integer,
    fecha_operacion date,
    comentario character varying
);


ALTER TABLE cementerio.historico_difuntos OWNER TO geopista;

--
-- TOC entry 4088 (class 1259 OID 136914)
-- Dependencies: 11
-- Name: historico_difuntos_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE historico_difuntos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.historico_difuntos_id_seq OWNER TO geopista;

--
-- TOC entry 4089 (class 1259 OID 137184)
-- Dependencies: 11
-- Name: historico_propiedad; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE historico_propiedad (
    id integer NOT NULL,
    dni_titular character varying,
    tipo integer,
    id_elem integer,
    fecha_operacion date,
    comentario character varying
);


ALTER TABLE cementerio.historico_propiedad OWNER TO geopista;

--
-- TOC entry 4090 (class 1259 OID 137192)
-- Dependencies: 11
-- Name: historico_propiedad_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE historico_propiedad_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.historico_propiedad_id_seq OWNER TO geopista;

SET default_with_oids = true;

--
-- TOC entry 4053 (class 1259 OID 110218)
-- Dependencies: 11
-- Name: inhumacion; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE inhumacion (
    informe character varying,
    id integer NOT NULL,
    contenedor integer,
    codigo character varying,
    fecha_inhumacion date,
    id_difunto integer,
    id_tarifa integer,
    tipo_inhumacion integer,
    precio_final character varying,
    bonificacion character varying
);


ALTER TABLE cementerio.inhumacion OWNER TO geopista;

--
-- TOC entry 4080 (class 1259 OID 132731)
-- Dependencies: 11
-- Name: inhumacion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE inhumacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.inhumacion_id_seq OWNER TO geopista;

--
-- TOC entry 4047 (class 1259 OID 110182)
-- Dependencies: 11
-- Name: intervencion; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE intervencion (
    id integer NOT NULL,
    informe character varying(50),
    fecha_fin date,
    fecha_inicio date,
    localizacion character varying,
    codigo character varying,
    estado character varying
);


ALTER TABLE cementerio.intervencion OWNER TO geopista;

--
-- TOC entry 4064 (class 1259 OID 128162)
-- Dependencies: 11
-- Name: intervencion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE intervencion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.intervencion_id_seq OWNER TO geopista;

--
-- TOC entry 4054 (class 1259 OID 110231)
-- Dependencies: 11
-- Name: localizacion; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE localizacion (
    descripcion character varying(50),
    valor real,
    id integer NOT NULL
);


ALTER TABLE cementerio.localizacion OWNER TO geopista;

--
-- TOC entry 4059 (class 1259 OID 120018)
-- Dependencies: 11
-- Name: localizacion_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE localizacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.localizacion_id_seq OWNER TO geopista;

--
-- TOC entry 4045 (class 1259 OID 110170)
-- Dependencies: 11
-- Name: persona; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE persona (
    nombre character varying(255),
    apellido1 character varying(255),
    apellido2 character varying(255),
    dni character varying(15) NOT NULL,
    sexo character varying(255),
    estado_civil character varying(255),
    fecha_nacimiento timestamp without time zone,
    domicilio character varying(255),
    poblacion character varying(255),
    telefono character varying(255)
);


ALTER TABLE cementerio.persona OWNER TO geopista;

--
-- TOC entry 4048 (class 1259 OID 110188)
-- Dependencies: 11
-- Name: plaza; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE plaza (
    situacion character varying(50),
    modicado timestamp without time zone,
    id_unidadenterramiento integer NOT NULL,
    id integer NOT NULL,
    estado integer
);


ALTER TABLE cementerio.plaza OWNER TO geopista;

--
-- TOC entry 4069 (class 1259 OID 128652)
-- Dependencies: 11
-- Name: plaza_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE plaza_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.plaza_id_seq OWNER TO geopista;

SET default_with_oids = false;

--
-- TOC entry 4063 (class 1259 OID 120276)
-- Dependencies: 4396 11
-- Name: reltitular; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE reltitular (
    dni_persona character varying(15) NOT NULL,
    id_concesion integer NOT NULL,
    esprincipal boolean DEFAULT false
);


ALTER TABLE cementerio.reltitular OWNER TO geopista;

SET default_with_oids = true;

--
-- TOC entry 4052 (class 1259 OID 110209)
-- Dependencies: 11
-- Name: tarifa; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE tarifa (
    concepto character varying(50),
    tipo_tarifa integer,
    tipo_calculo integer,
    id integer NOT NULL,
    id_cementerio integer,
    categoria integer,
    precio character varying
);


ALTER TABLE cementerio.tarifa OWNER TO geopista;

--
-- TOC entry 4062 (class 1259 OID 120265)
-- Dependencies: 11
-- Name: tarifa_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE tarifa_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.tarifa_id_seq OWNER TO geopista;

--
-- TOC entry 4046 (class 1259 OID 110176)
-- Dependencies: 11
-- Name: unidadenterramiento; Type: TABLE; Schema: cementerio; Owner: geopista; Tablespace: 
--

CREATE TABLE unidadenterramiento (
    id integer NOT NULL,
    columna integer,
    fila integer,
    estado integer,
    numplazas integer,
    fult_construccion date,
    fult_modificacion date,
    id_elemcementerio integer NOT NULL,
    id_localizacion integer NOT NULL,
    tipo_unidad integer,
    descripcion character varying(250),
    codigo character varying(250),
    freforma date
);


ALTER TABLE cementerio.unidadenterramiento OWNER TO geopista;

--
-- TOC entry 4060 (class 1259 OID 120020)
-- Dependencies: 11
-- Name: unidadenterramiento_id_seq; Type: SEQUENCE; Schema: cementerio; Owner: geopista
--

CREATE SEQUENCE unidadenterramiento_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cementerio.unidadenterramiento_id_seq OWNER TO geopista;

--
-- TOC entry 4434 (class 2606 OID 137370)
-- Dependencies: 4076 4076 4076 4076 4076
-- Name: anexo_cementerio_pkey; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY anexo_cementerio
    ADD CONSTRAINT anexo_cementerio_pkey PRIMARY KEY (id_elemcementerio, id_documento, tipo, subtipo);


--
-- TOC entry 4426 (class 2606 OID 120077)
-- Dependencies: 4055 4055 4055 4055
-- Name: cementerio_feature_pkey; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY cementerio_feature
    ADD CONSTRAINT cementerio_feature_pkey PRIMARY KEY (id_elem, id_layer, id_feature);


--
-- TOC entry 4418 (class 2606 OID 120291)
-- Dependencies: 4051 4051
-- Name: concesion_pk; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY concesion
    ADD CONSTRAINT concesion_pk PRIMARY KEY (id);


--
-- TOC entry 4442 (class 2606 OID 136913)
-- Dependencies: 4087 4087
-- Name: historicodifuntos_pk; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY historico_difuntos
    ADD CONSTRAINT historicodifuntos_pk PRIMARY KEY (id);


--
-- TOC entry 4444 (class 2606 OID 137191)
-- Dependencies: 4089 4089
-- Name: historicopropiedad_pk; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY historico_propiedad
    ADD CONSTRAINT historicopropiedad_pk PRIMARY KEY (id);


--
-- TOC entry 4404 (class 2606 OID 110240)
-- Dependencies: 4044 4044
-- Name: pk_bloque; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY bloque
    ADD CONSTRAINT pk_bloque PRIMARY KEY (id);


--
-- TOC entry 4430 (class 2606 OID 130512)
-- Dependencies: 4073 4073
-- Name: pk_cementerio; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY cementerio
    ADD CONSTRAINT pk_cementerio PRIMARY KEY (id);


--
-- TOC entry 4416 (class 2606 OID 110248)
-- Dependencies: 4050 4050
-- Name: pk_datosfallecimiento; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY datosfallecimiento
    ADD CONSTRAINT pk_datosfallecimiento PRIMARY KEY (id);


--
-- TOC entry 4414 (class 2606 OID 110252)
-- Dependencies: 4049 4049
-- Name: pk_difunto; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY difunto
    ADD CONSTRAINT pk_difunto PRIMARY KEY (id);


--
-- TOC entry 4402 (class 2606 OID 110266)
-- Dependencies: 4043 4043
-- Name: pk_elem_cementerio; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY elem_cementerio
    ADD CONSTRAINT pk_elem_cementerio PRIMARY KEY (id);


--
-- TOC entry 4438 (class 2606 OID 136602)
-- Dependencies: 4083 4083
-- Name: pk_elemfeature; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY elem_feature
    ADD CONSTRAINT pk_elemfeature PRIMARY KEY (id);


--
-- TOC entry 4440 (class 2606 OID 136870)
-- Dependencies: 4086 4086
-- Name: pk_exhumacion; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY exhumacion
    ADD CONSTRAINT pk_exhumacion PRIMARY KEY (id);


--
-- TOC entry 4422 (class 2606 OID 110256)
-- Dependencies: 4053 4053
-- Name: pk_inhumacion; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY inhumacion
    ADD CONSTRAINT pk_inhumacion PRIMARY KEY (id);


--
-- TOC entry 4410 (class 2606 OID 110244)
-- Dependencies: 4047 4047
-- Name: pk_intervencion; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY intervencion
    ADD CONSTRAINT pk_intervencion PRIMARY KEY (id);


--
-- TOC entry 4424 (class 2606 OID 120044)
-- Dependencies: 4054 4054
-- Name: pk_localizacion; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY localizacion
    ADD CONSTRAINT pk_localizacion PRIMARY KEY (id);


--
-- TOC entry 4406 (class 2606 OID 120254)
-- Dependencies: 4045 4045
-- Name: pk_persona; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY persona
    ADD CONSTRAINT pk_persona PRIMARY KEY (dni);


--
-- TOC entry 4412 (class 2606 OID 110246)
-- Dependencies: 4048 4048
-- Name: pk_plaza; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY plaza
    ADD CONSTRAINT pk_plaza PRIMARY KEY (id);


--
-- TOC entry 4420 (class 2606 OID 110260)
-- Dependencies: 4052 4052
-- Name: pk_tarifa; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY tarifa
    ADD CONSTRAINT pk_tarifa PRIMARY KEY (id);


--
-- TOC entry 4408 (class 2606 OID 110242)
-- Dependencies: 4046 4046
-- Name: pk_unidadenterramiento; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY unidadenterramiento
    ADD CONSTRAINT pk_unidadenterramiento PRIMARY KEY (id);


--
-- TOC entry 4428 (class 2606 OID 120281)
-- Dependencies: 4063 4063 4063
-- Name: reltitular_pkey; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY reltitular
    ADD CONSTRAINT reltitular_pkey PRIMARY KEY (dni_persona, id_concesion);


--
-- TOC entry 4436 (class 2606 OID 131367)
-- Dependencies: 4077 4077 4077 4077
-- Name: tabla_anexofeature_pkey; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY anexo_feature
    ADD CONSTRAINT tabla_anexofeature_pkey PRIMARY KEY (id_documento, id_layer, id_feature);


--
-- TOC entry 4432 (class 2606 OID 131349)
-- Dependencies: 4075 4075
-- Name: tabla_documento_pkey; Type: CONSTRAINT; Schema: cementerio; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY documento
    ADD CONSTRAINT tabla_documento_pkey PRIMARY KEY (id_documento);


--
-- TOC entry 4455 (class 2606 OID 132315)
-- Dependencies: 4429 4073 4052
-- Name: cementerio_fk; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY tarifa
    ADD CONSTRAINT cementerio_fk FOREIGN KEY (id_cementerio) REFERENCES cementerio(id);


--
-- TOC entry 4448 (class 2606 OID 110272)
-- Dependencies: 4401 4043 4046
-- Name: cementerio_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY unidadenterramiento
    ADD CONSTRAINT cementerio_fkey FOREIGN KEY (id_elemcementerio) REFERENCES elem_cementerio(id);


--
-- TOC entry 4458 (class 2606 OID 120344)
-- Dependencies: 4417 4051 4063
-- Name: concesion_fk; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY reltitular
    ADD CONSTRAINT concesion_fk FOREIGN KEY (id_concesion) REFERENCES concesion(id);


--
-- TOC entry 4452 (class 2606 OID 110297)
-- Dependencies: 4415 4050 4049
-- Name: difuntodatosfall_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY difunto
    ADD CONSTRAINT difuntodatosfall_fkey FOREIGN KEY (id_datosfallecimiento) REFERENCES datosfallecimiento(id);


--
-- TOC entry 4446 (class 2606 OID 130533)
-- Dependencies: 4401 4043 4044
-- Name: elemcementerio_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY bloque
    ADD CONSTRAINT elemcementerio_fkey FOREIGN KEY (id_elemcementerio) REFERENCES elem_cementerio(id);


--
-- TOC entry 4445 (class 2606 OID 130513)
-- Dependencies: 4429 4073 4043
-- Name: fk_cementerio; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY elem_cementerio
    ADD CONSTRAINT fk_cementerio FOREIGN KEY (id_cementerio) REFERENCES cementerio(id);


--
-- TOC entry 4457 (class 2606 OID 132726)
-- Dependencies: 4413 4049 4053
-- Name: fk_difunto; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY inhumacion
    ADD CONSTRAINT fk_difunto FOREIGN KEY (id_difunto) REFERENCES difunto(id);


--
-- TOC entry 4461 (class 2606 OID 136871)
-- Dependencies: 4413 4049 4086
-- Name: fk_difunto; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY exhumacion
    ADD CONSTRAINT fk_difunto FOREIGN KEY (id_difunto) REFERENCES difunto(id);


--
-- TOC entry 4447 (class 2606 OID 128488)
-- Dependencies: 4423 4054 4046
-- Name: localizacion_fk; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY unidadenterramiento
    ADD CONSTRAINT localizacion_fk FOREIGN KEY (id_localizacion) REFERENCES localizacion(id);


--
-- TOC entry 4459 (class 2606 OID 120339)
-- Dependencies: 4405 4045 4063
-- Name: persona_fk; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY reltitular
    ADD CONSTRAINT persona_fk FOREIGN KEY (dni_persona) REFERENCES persona(dni);


--
-- TOC entry 4450 (class 2606 OID 120260)
-- Dependencies: 4405 4045 4049
-- Name: persona_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY difunto
    ADD CONSTRAINT persona_fkey FOREIGN KEY (dni_persona) REFERENCES persona(dni);


--
-- TOC entry 4451 (class 2606 OID 120238)
-- Dependencies: 4411 4048 4049
-- Name: plaza_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY difunto
    ADD CONSTRAINT plaza_fkey FOREIGN KEY (id_plaza) REFERENCES plaza(id);


--
-- TOC entry 4456 (class 2606 OID 132900)
-- Dependencies: 4419 4052 4053
-- Name: tarifa_fk; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY inhumacion
    ADD CONSTRAINT tarifa_fk FOREIGN KEY (id_tarifa) REFERENCES tarifa(id);


--
-- TOC entry 4460 (class 2606 OID 136876)
-- Dependencies: 4419 4052 4086
-- Name: tarifa_fk; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY exhumacion
    ADD CONSTRAINT tarifa_fk FOREIGN KEY (id_tarifa) REFERENCES tarifa(id);


--
-- TOC entry 4454 (class 2606 OID 110307)
-- Dependencies: 4419 4052 4051
-- Name: tarifa_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY concesion
    ADD CONSTRAINT tarifa_fkey FOREIGN KEY (id_tarifa) REFERENCES tarifa(id);


--
-- TOC entry 4449 (class 2606 OID 120050)
-- Dependencies: 4407 4046 4048
-- Name: unidadenterramiento_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY plaza
    ADD CONSTRAINT unidadenterramiento_fkey FOREIGN KEY (id_unidadenterramiento) REFERENCES unidadenterramiento(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4453 (class 2606 OID 120229)
-- Dependencies: 4407 4046 4051
-- Name: unidadenterramiento_fkey; Type: FK CONSTRAINT; Schema: cementerio; Owner: geopista
--

ALTER TABLE ONLY concesion
    ADD CONSTRAINT unidadenterramiento_fkey FOREIGN KEY (id_unidad) REFERENCES unidadenterramiento(id);


--
-- TOC entry 4464 (class 0 OID 0)
-- Dependencies: 4044
-- Name: bloque; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE bloque FROM PUBLIC;
REVOKE ALL ON TABLE bloque FROM geopista;
GRANT ALL ON TABLE bloque TO geopista;
GRANT ALL ON TABLE bloque TO postgres;


--
-- TOC entry 4465 (class 0 OID 0)
-- Dependencies: 4073
-- Name: cementerio; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE cementerio FROM PUBLIC;
REVOKE ALL ON TABLE cementerio FROM geopista;
GRANT ALL ON TABLE cementerio TO geopista;
GRANT ALL ON TABLE cementerio TO postgres;


--
-- TOC entry 4466 (class 0 OID 0)
-- Dependencies: 4055
-- Name: cementerio_feature; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE cementerio_feature FROM PUBLIC;
REVOKE ALL ON TABLE cementerio_feature FROM geopista;
GRANT ALL ON TABLE cementerio_feature TO geopista;
GRANT ALL ON TABLE cementerio_feature TO postgres;


--
-- TOC entry 4467 (class 0 OID 0)
-- Dependencies: 4050
-- Name: datosfallecimiento; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE datosfallecimiento FROM PUBLIC;
REVOKE ALL ON TABLE datosfallecimiento FROM geopista;
GRANT ALL ON TABLE datosfallecimiento TO geopista;
GRANT ALL ON TABLE datosfallecimiento TO postgres;


--
-- TOC entry 4468 (class 0 OID 0)
-- Dependencies: 4049
-- Name: difunto; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE difunto FROM PUBLIC;
REVOKE ALL ON TABLE difunto FROM geopista;
GRANT ALL ON TABLE difunto TO geopista;
GRANT ALL ON TABLE difunto TO postgres;


--
-- TOC entry 4469 (class 0 OID 0)
-- Dependencies: 4043
-- Name: elem_cementerio; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE elem_cementerio FROM PUBLIC;
REVOKE ALL ON TABLE elem_cementerio FROM geopista;
GRANT ALL ON TABLE elem_cementerio TO geopista;
GRANT ALL ON TABLE elem_cementerio TO postgres;


--
-- TOC entry 4470 (class 0 OID 0)
-- Dependencies: 4058
-- Name: elem_cementerio_id_seq; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON SEQUENCE elem_cementerio_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE elem_cementerio_id_seq FROM geopista;
GRANT ALL ON SEQUENCE elem_cementerio_id_seq TO geopista;
GRANT ALL ON SEQUENCE elem_cementerio_id_seq TO postgres;


--
-- TOC entry 4471 (class 0 OID 0)
-- Dependencies: 4083
-- Name: elem_feature; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE elem_feature FROM PUBLIC;
REVOKE ALL ON TABLE elem_feature FROM geopista;
GRANT ALL ON TABLE elem_feature TO geopista;
GRANT ALL ON TABLE elem_feature TO postgres;


--
-- TOC entry 4472 (class 0 OID 0)
-- Dependencies: 4087
-- Name: historico_difuntos; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE historico_difuntos FROM PUBLIC;
REVOKE ALL ON TABLE historico_difuntos FROM geopista;
GRANT ALL ON TABLE historico_difuntos TO geopista;
GRANT ALL ON TABLE historico_difuntos TO postgres;


--
-- TOC entry 4473 (class 0 OID 0)
-- Dependencies: 4089
-- Name: historico_propiedad; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE historico_propiedad FROM PUBLIC;
REVOKE ALL ON TABLE historico_propiedad FROM geopista;
GRANT ALL ON TABLE historico_propiedad TO geopista;
GRANT ALL ON TABLE historico_propiedad TO postgres;


--
-- TOC entry 4474 (class 0 OID 0)
-- Dependencies: 4047
-- Name: intervencion; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE intervencion FROM PUBLIC;
REVOKE ALL ON TABLE intervencion FROM geopista;
GRANT ALL ON TABLE intervencion TO geopista;
GRANT ALL ON TABLE intervencion TO postgres;


--
-- TOC entry 4475 (class 0 OID 0)
-- Dependencies: 4054
-- Name: localizacion; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE localizacion FROM PUBLIC;
REVOKE ALL ON TABLE localizacion FROM geopista;
GRANT ALL ON TABLE localizacion TO geopista;
GRANT ALL ON TABLE localizacion TO postgres;


--
-- TOC entry 4476 (class 0 OID 0)
-- Dependencies: 4059
-- Name: localizacion_id_seq; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON SEQUENCE localizacion_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE localizacion_id_seq FROM geopista;
GRANT ALL ON SEQUENCE localizacion_id_seq TO geopista;
GRANT ALL ON SEQUENCE localizacion_id_seq TO postgres;


--
-- TOC entry 4477 (class 0 OID 0)
-- Dependencies: 4045
-- Name: persona; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE persona FROM PUBLIC;
REVOKE ALL ON TABLE persona FROM geopista;
GRANT ALL ON TABLE persona TO geopista;
GRANT ALL ON TABLE persona TO postgres;


--
-- TOC entry 4478 (class 0 OID 0)
-- Dependencies: 4048
-- Name: plaza; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE plaza FROM PUBLIC;
REVOKE ALL ON TABLE plaza FROM geopista;
GRANT ALL ON TABLE plaza TO geopista;
GRANT ALL ON TABLE plaza TO postgres;


--
-- TOC entry 4479 (class 0 OID 0)
-- Dependencies: 4063
-- Name: reltitular; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE reltitular FROM PUBLIC;
REVOKE ALL ON TABLE reltitular FROM geopista;
GRANT ALL ON TABLE reltitular TO geopista;
GRANT ALL ON TABLE reltitular TO postgres;


--
-- TOC entry 4480 (class 0 OID 0)
-- Dependencies: 4052
-- Name: tarifa; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE tarifa FROM PUBLIC;
REVOKE ALL ON TABLE tarifa FROM geopista;
GRANT ALL ON TABLE tarifa TO geopista;
GRANT ALL ON TABLE tarifa TO postgres;


--
-- TOC entry 4481 (class 0 OID 0)
-- Dependencies: 4046
-- Name: unidadenterramiento; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON TABLE unidadenterramiento FROM PUBLIC;
REVOKE ALL ON TABLE unidadenterramiento FROM geopista;
GRANT ALL ON TABLE unidadenterramiento TO geopista;
GRANT ALL ON TABLE unidadenterramiento TO postgres;


--
-- TOC entry 4482 (class 0 OID 0)
-- Dependencies: 4060
-- Name: unidadenterramiento_id_seq; Type: ACL; Schema: cementerio; Owner: geopista
--

REVOKE ALL ON SEQUENCE unidadenterramiento_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE unidadenterramiento_id_seq FROM geopista;
GRANT ALL ON SEQUENCE unidadenterramiento_id_seq TO geopista;
GRANT ALL ON SEQUENCE unidadenterramiento_id_seq TO postgres;


-- Completed on 2011-07-28 10:22:01

--
-- PostgreSQL database dump complete
--

