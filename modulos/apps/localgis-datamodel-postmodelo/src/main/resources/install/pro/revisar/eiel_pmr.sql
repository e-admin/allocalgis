--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.8
-- Dumped by pg_dump version 9.2.3
-- Started on 2013-04-26 11:55:40

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = true;

--
-- TOC entry 529 (class 1259 OID 77808644)
-- Name: eiel_pmr_accesibilidad_edificios_publicos; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_accesibilidad_edificios_publicos (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    "Tipo_de_accesibilidad" character varying(1),
    "Rampa_de_acceso" character varying(1),
    "Observaciones" character varying(500)
);


ALTER TABLE public.eiel_pmr_accesibilidad_edificios_publicos OWNER TO geopista;

--
-- TOC entry 530 (class 1259 OID 77808650)
-- Name: eiel_pmr_barreras_urbanisticas; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_barreras_urbanisticas (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "Tipo_de_barrera" character varying(1),
    "GEOMETRY" geometry,
    "Observaciones" character varying(500),
    "Clase_de_barrera" character varying(1)
);


ALTER TABLE public.eiel_pmr_barreras_urbanisticas OWNER TO geopista;

--
-- TOC entry 531 (class 1259 OID 77808656)
-- Name: eiel_pmr_bord_sin_rebaje_p_peatones; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_bord_sin_rebaje_p_peatones (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    lado character varying(1) NOT NULL,
    "Observaciones" character varying(500)
);


ALTER TABLE public.eiel_pmr_bord_sin_rebaje_p_peatones OWNER TO geopista;

--
-- TOC entry 532 (class 1259 OID 77808662)
-- Name: eiel_pmr_obstaculos_en_altura_paso; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_obstaculos_en_altura_paso (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    "Altura" numeric(3,2),
    "Observaciones" character varying(500)
);


ALTER TABLE public.eiel_pmr_obstaculos_en_altura_paso OWNER TO geopista;

--
-- TOC entry 533 (class 1259 OID 77808668)
-- Name: eiel_pmr_parada_transporte_publico; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_parada_transporte_publico (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    "Tipo_de_parada" character varying(1),
    "Tipo_de_transporte" character varying(1),
    "Accesibilidad" character varying(1),
    "Observaciones" character varying(500)
);


ALTER TABLE public.eiel_pmr_parada_transporte_publico OWNER TO geopista;

--
-- TOC entry 534 (class 1259 OID 77808674)
-- Name: eiel_pmr_paso_de_peatones; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_paso_de_peatones (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    "Observaciones" character varying(500),
    "Bordillo_no_rebajado" character varying(1),
    "Pavimento_no_diferenciado" character varying(2)
);


ALTER TABLE public.eiel_pmr_paso_de_peatones OWNER TO geopista;

--
-- TOC entry 535 (class 1259 OID 77808680)
-- Name: eiel_pmr_pav_no_dif_p_peatones; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_pav_no_dif_p_peatones (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    lado character varying(1) NOT NULL,
    "Observaciones" character varying(500)
);


ALTER TABLE public.eiel_pmr_pav_no_dif_p_peatones OWNER TO geopista;

--
-- TOC entry 536 (class 1259 OID 77808686)
-- Name: eiel_pmr_pavimento_irregular; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_pavimento_irregular (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    lado character varying(1) NOT NULL,
    "GEOMETRY" geometry,
    "Observaciones" character varying(500)
);


ALTER TABLE public.eiel_pmr_pavimento_irregular OWNER TO geopista;

--
-- TOC entry 537 (class 1259 OID 77808692)
-- Name: eiel_pmr_plaza_aparcamiento_reservada; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_plaza_aparcamiento_reservada (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    "Observaciones" character varying(500)
);


ALTER TABLE public.eiel_pmr_plaza_aparcamiento_reservada OWNER TO geopista;

--
-- TOC entry 538 (class 1259 OID 77808698)
-- Name: eiel_pmr_tramos_calle; Type: TABLE; Schema: public; Owner: geopista; Tablespace: 
--

CREATE TABLE eiel_pmr_tramos_calle (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    "Ancho_minimo_lado_derecho" character varying(5),
    "Ancho_maximo_lado_derecho" character varying(5),
    "Pend_trans_mayor_2_por_ciento_lado_derecho" character varying(1),
    "Pend_long_mayor_6_por_ciento_lado_derecho" character varying(1),
    "Ancho_minimo_lado_izquierdo" character varying(5),
    "Ancho_maximo_lado_izquierdo" character varying(5),
    "Pend_trans_mayor_2_por_ciento_lado_izquierdo" character varying(1),
    "Pend_long_mayor_6_por_ciento_lado_izquierdo" character varying(1),
    "Observaciones" character varying(500)
);


ALTER TABLE public.eiel_pmr_tramos_calle OWNER TO geopista;

--
-- TOC entry 7206 (class 2606 OID 97233354)
-- Name: EIEL_Paso_de_peatones_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_paso_de_peatones
    ADD CONSTRAINT "EIEL_Paso_de_peatones_id_key" UNIQUE (id);


--
-- TOC entry 7186 (class 2606 OID 97233879)
-- Name: eiel_pmr_accesibilidad_edificios_publicos_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_accesibilidad_edificios_publicos
    ADD CONSTRAINT eiel_pmr_accesibilidad_edificios_publicos_id_key UNIQUE (id);


--
-- TOC entry 7188 (class 2606 OID 97233881)
-- Name: eiel_pmr_accesibilidad_edificios_publicos_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_accesibilidad_edificios_publicos
    ADD CONSTRAINT eiel_pmr_accesibilidad_edificios_publicos_pkey PRIMARY KEY (id);


--
-- TOC entry 7190 (class 2606 OID 97233883)
-- Name: eiel_pmr_barreras_urbanisticas_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_barreras_urbanisticas
    ADD CONSTRAINT eiel_pmr_barreras_urbanisticas_id_key UNIQUE (id);


--
-- TOC entry 7192 (class 2606 OID 97233885)
-- Name: eiel_pmr_barreras_urbanisticas_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_barreras_urbanisticas
    ADD CONSTRAINT eiel_pmr_barreras_urbanisticas_pkey PRIMARY KEY (id);


--
-- TOC entry 7194 (class 2606 OID 97233887)
-- Name: eiel_pmr_bord_sin_rebaje_p_peatones_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_bord_sin_rebaje_p_peatones
    ADD CONSTRAINT eiel_pmr_bord_sin_rebaje_p_peatones_id_key UNIQUE (id);


--
-- TOC entry 7196 (class 2606 OID 97233889)
-- Name: eiel_pmr_bord_sin_rebaje_p_peatones_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_bord_sin_rebaje_p_peatones
    ADD CONSTRAINT eiel_pmr_bord_sin_rebaje_p_peatones_pkey PRIMARY KEY (id);


--
-- TOC entry 7198 (class 2606 OID 97233891)
-- Name: eiel_pmr_obstaculos_en_altura_paso_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_obstaculos_en_altura_paso
    ADD CONSTRAINT eiel_pmr_obstaculos_en_altura_paso_id_key UNIQUE (id);


--
-- TOC entry 7200 (class 2606 OID 97233893)
-- Name: eiel_pmr_obstaculos_en_altura_paso_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_obstaculos_en_altura_paso
    ADD CONSTRAINT eiel_pmr_obstaculos_en_altura_paso_pkey PRIMARY KEY (id);


--
-- TOC entry 7202 (class 2606 OID 97233896)
-- Name: eiel_pmr_parada_transporte_publico_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_parada_transporte_publico
    ADD CONSTRAINT eiel_pmr_parada_transporte_publico_id_key UNIQUE (id);


--
-- TOC entry 7204 (class 2606 OID 97233901)
-- Name: eiel_pmr_parada_transporte_publico_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_parada_transporte_publico
    ADD CONSTRAINT eiel_pmr_parada_transporte_publico_pkey PRIMARY KEY (id);


--
-- TOC entry 7208 (class 2606 OID 97233903)
-- Name: eiel_pmr_paso_de_peatones_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_paso_de_peatones
    ADD CONSTRAINT eiel_pmr_paso_de_peatones_id_key UNIQUE (id);


--
-- TOC entry 7210 (class 2606 OID 97233905)
-- Name: eiel_pmr_paso_de_peatones_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_paso_de_peatones
    ADD CONSTRAINT eiel_pmr_paso_de_peatones_pkey PRIMARY KEY (id);


--
-- TOC entry 7212 (class 2606 OID 97233907)
-- Name: eiel_pmr_pav_no_dif_p_peatones_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_pav_no_dif_p_peatones
    ADD CONSTRAINT eiel_pmr_pav_no_dif_p_peatones_id_key UNIQUE (id);


--
-- TOC entry 7214 (class 2606 OID 97233909)
-- Name: eiel_pmr_pav_no_dif_p_peatones_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_pav_no_dif_p_peatones
    ADD CONSTRAINT eiel_pmr_pav_no_dif_p_peatones_pkey PRIMARY KEY (id);


--
-- TOC entry 7216 (class 2606 OID 97233911)
-- Name: eiel_pmr_pavimento_irregular_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_pavimento_irregular
    ADD CONSTRAINT eiel_pmr_pavimento_irregular_id_key UNIQUE (id);


--
-- TOC entry 7218 (class 2606 OID 97233913)
-- Name: eiel_pmr_pavimento_irregular_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_pavimento_irregular
    ADD CONSTRAINT eiel_pmr_pavimento_irregular_pkey PRIMARY KEY (id);


--
-- TOC entry 7220 (class 2606 OID 97233915)
-- Name: eiel_pmr_plaza_aparcamiento_reservada_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_plaza_aparcamiento_reservada
    ADD CONSTRAINT eiel_pmr_plaza_aparcamiento_reservada_id_key UNIQUE (id);


--
-- TOC entry 7222 (class 2606 OID 97233917)
-- Name: eiel_pmr_plaza_aparcamiento_reservada_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_plaza_aparcamiento_reservada
    ADD CONSTRAINT eiel_pmr_plaza_aparcamiento_reservada_pkey PRIMARY KEY (id);


--
-- TOC entry 7224 (class 2606 OID 97233919)
-- Name: eiel_pmr_tramos_calle_id_key; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_tramos_calle
    ADD CONSTRAINT eiel_pmr_tramos_calle_id_key UNIQUE (id);


--
-- TOC entry 7226 (class 2606 OID 97233921)
-- Name: eiel_pmr_tramos_calle_pkey; Type: CONSTRAINT; Schema: public; Owner: geopista; Tablespace: 
--

ALTER TABLE ONLY eiel_pmr_tramos_calle
    ADD CONSTRAINT eiel_pmr_tramos_calle_pkey PRIMARY KEY (id);


-- Completed on 2013-04-26 11:55:56

--
-- PostgreSQL database dump complete
--

