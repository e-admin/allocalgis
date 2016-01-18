
------------------------------------------------------
-- GESTOR FIP ----------------------------------------
------------------------------------------------------

CREATE SCHEMA gestorfip
  AUTHORIZATION postgres;

SET search_path = gestorfip, pg_catalog;

DROP TABLE IF EXISTS gestorfip.tramite_determinacion_valoresreferencia CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinacion_gruposaplicacion CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinacion_regulacionesespecificas CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinacion_determinacionesreguladoras CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinacion_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_entidad_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_adscripciones CASCADE;
DROP TABLE IF EXISTS gestorfip.planeamientoencargado CASCADE;
DROP TABLE IF EXISTS gestorfip.planeamientovigente CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_aplicacionambitos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_unidades CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_operacionesentidades CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_operacionesdeterminaciones CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_caso_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_caso_vinculos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_caso_regimenes CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_casos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionesurbanisticas CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinaciones CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_entidades CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_documento_hojas CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite CASCADE;
DROP TABLE IF EXISTS gestorfip.catalogosistematizado CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_instrumentos CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tipostramite CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_gruposdocumento CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposdocumento CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_operacionescaracteres CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposoperaciondeterminacion CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposoperacionentidad CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposentidad CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_caracteresdeterminacion CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_organigramaambitos CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_ambitos CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposambito CASCADE;
DROP TABLE IF EXISTS gestorfip.fip CASCADE;
DROP TABLE IF EXISTS gestorfip.ccaa_path_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.ccaa CASCADE;
DROP TABLE IF EXISTS gestorfip.config CASCADE;
DROP TABLE IF EXISTS gestorfip.versiones_uer CASCADE;
DROP TABLE IF EXISTS gestorfip.relacionambitomunicipio CASCADE;

DROP SEQUENCE IF EXISTS gestorfip.seq_catalogosistematizado CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_ambitos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_caracteresdeterminacion CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_gruposdocumento CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_instrumentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_operacionescaracteres CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_organigramaambitos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposambito CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposdocumento CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposentidad CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposoperaciondeterminacion CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposoperacionentidad CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tipostramite CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_fip CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_planeamientoencargado CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_planeamientovigente CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_adscripciones CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_aplicacionambitos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionesurbanisticas CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_caso_documentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_caso_regimen_regimenesespecifi CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_caso_regimenes CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_caso_vinculos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_casos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_determinacionesreguladoras CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_documentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_gruposaplicacion CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_regulacionesespecificas CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_valoresreferencia CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinaciones CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_documento_hojas CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_documentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_entidad_documentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_entidades CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_operacionesdeterminaciones CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_operacionesentidades CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_unidades CASCADE;

/*

DROP TABLE IF EXISTS public.planeamiento_acciones_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_afecciones_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_alineacion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_ambito_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_categoria_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_clasificacion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_desarrollo_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_equidistribucion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_gestion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_otras_indicaciones_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_proteccion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_sistemas_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_zona_gen CASCADE;

DROP SEQUENCE IF EXISTS public.seq_planeamiento_acciones_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_afecciones_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_alineacion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_ambito_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_categoria_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_clasificacion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_desarrollo_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_equidistribucion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_gestion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_otras_indicaciones_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_proteccion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_sistemas_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_zona_gen CASCADE;

*/

--
-- Disable validation of the function body :
--
SET check_function_bodies = false;

--
-- Definition for sequence seq_fip (OID = 58746) : 
--
CREATE SEQUENCE gestorfip.seq_fip
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Structure for table fip (OID = 58748) : 
--
CREATE TABLE gestorfip.fip (
    id integer NOT NULL,
    pais character varying(2),
    fecha date,
    "version" character varying(3),
    srs character varying(20),
	idambito INTEGER, 
	fechaconsolidacion DATE
	) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.fip ALTER COLUMN pais SET STATISTICS 0;
--
-- Structure for table diccionario_tiposambito (OID = 58759) : 
--
CREATE TABLE gestorfip.diccionario_tiposambito (
    id integer NOT NULL,
    codigo character varying(6),
    nombre character varying(255)
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_tiposambito ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposambito ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposambito ALTER COLUMN nombre SET STATISTICS 0;
--
-- Structure for table diccionario_ambitos (OID = 58764) : 
--
CREATE TABLE gestorfip.diccionario_ambitos (
    id integer NOT NULL,
    nombre character varying(255),
    codigo character varying(6),
    ine character varying(6),
    tipoambito integer,
    geometria public.geometry
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_ambitos ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_ambitos ALTER COLUMN nombre SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_ambitos ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_ambitos ALTER COLUMN ine SET STATISTICS 0;
--
-- Structure for table diccionario_organigramaambitos (OID = 58784) : 
--
CREATE TABLE gestorfip.diccionario_organigramaambitos (
    id integer NOT NULL,
    padre integer,
    hijo integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_organigramaambitos ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_organigramaambitos ALTER COLUMN padre SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_organigramaambitos ALTER COLUMN hijo SET STATISTICS 0;
--
-- Structure for table diccionario_caracteresdeterminacion (OID = 58797) : 
--
CREATE TABLE gestorfip.diccionario_caracteresdeterminacion (
    id integer NOT NULL,
    codigo character varying(6),
    nombre character varying(255),
    aplicaciones_max integer,
    aplicaciones_min integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_caracteresdeterminacion ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_caracteresdeterminacion ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_caracteresdeterminacion ALTER COLUMN nombre SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_caracteresdeterminacion ALTER COLUMN aplicaciones_max SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_caracteresdeterminacion ALTER COLUMN aplicaciones_min SET STATISTICS 0;
--
-- Structure for table diccionario_tiposentidad (OID = 58809) : 
--
CREATE TABLE gestorfip.diccionario_tiposentidad (
    id integer NOT NULL,
    codigo character varying(6),
    nombre character varying(255)
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_tiposentidad ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposentidad ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposentidad ALTER COLUMN nombre SET STATISTICS 0;

--
-- Structure for table diccionario_tiposoperacionentidad (OID = 58819) : 
--
CREATE TABLE gestorfip.diccionario_tiposoperacionentidad (
    id integer NOT NULL,
    codigo character varying(6),
    nombre character varying(255),
    tipoentidad integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_tiposoperacionentidad ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposoperacionentidad ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposoperacionentidad ALTER COLUMN nombre SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposoperacionentidad ALTER COLUMN tipoentidad SET STATISTICS 0;
--
-- Structure for table diccionario_tiposoperaciondeterminacion (OID = 58829) : 
--
CREATE TABLE gestorfip.diccionario_tiposoperaciondeterminacion (
    id integer NOT NULL,
    codigo character varying(6),
    nombre character varying(255)
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_tiposoperaciondeterminacion ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposoperaciondeterminacion ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposoperaciondeterminacion ALTER COLUMN nombre SET STATISTICS 0;
--
-- Structure for table diccionario_operacionescaracteres (OID = 58839) : 
--
CREATE TABLE gestorfip.diccionario_operacionescaracteres (
    id integer NOT NULL,
    tipooperaciondeterminacion integer,
    caracteroperadora integer,
    caracteroperada integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_operacionescaracteres ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_operacionescaracteres ALTER COLUMN tipooperaciondeterminacion SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_operacionescaracteres ALTER COLUMN caracteroperadora SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_operacionescaracteres ALTER COLUMN caracteroperada SET STATISTICS 0;
--
-- Structure for table diccionario_tiposdocumento (OID = 58859) : 
--
CREATE TABLE gestorfip.diccionario_tiposdocumento (
    id integer NOT NULL,
    codigo character varying(6),
    nombre character varying(255)
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_tiposdocumento ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposdocumento ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tiposdocumento ALTER COLUMN nombre SET STATISTICS 0;
--
-- Structure for table diccionario_gruposdocumento (OID = 58869) : 
--
CREATE TABLE gestorfip.diccionario_gruposdocumento (
    id integer NOT NULL,
    codigo character varying(6),
    nombre character varying(255)
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_gruposdocumento ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_gruposdocumento ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_gruposdocumento ALTER COLUMN nombre SET STATISTICS 0;
--
-- Structure for table diccionario_tipostramite (OID = 58879) : 
--
CREATE TABLE gestorfip.diccionario_tipostramite (
    id integer NOT NULL,
    codigo character varying(6),
    nombre character varying(255)
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_tipostramite ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tipostramite ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_tipostramite ALTER COLUMN nombre SET STATISTICS 0;
--
-- Structure for table diccionario_instrumentos (OID = 58889) : 
--
CREATE TABLE gestorfip.diccionario_instrumentos (
    id integer NOT NULL,
    codigo character varying(6),
    nombre character varying(255)
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.diccionario_instrumentos ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_instrumentos ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.diccionario_instrumentos ALTER COLUMN nombre SET STATISTICS 0;
--
-- Definition for sequence seq_diccionario_tiposambito (OID = 58899) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_tiposambito
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_organigramaambitos (OID = 58901) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_organigramaambitos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_tipostramite (OID = 58903) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_tipostramite
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_tiposoperacionentidad (OID = 58905) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_tiposoperacionentidad
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_tiposoperaciondeterminacion (OID = 58907) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_tiposoperaciondeterminacion
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_tiposentidad (OID = 58909) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_tiposentidad
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_tiposdocumento (OID = 58911) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_tiposdocumento
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_operacionescaracteres (OID = 58913) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_operacionescaracteres
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_instrumentos (OID = 58915) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_instrumentos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_gruposdocumento (OID = 58917) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_gruposdocumento
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_caracteresdeterminacion (OID = 58919) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_caracteresdeterminacion
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_diccionario_ambitos (OID = 58921) : 
--
CREATE SEQUENCE gestorfip.seq_diccionario_ambitos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Structure for table catalogosistematizado (OID = 60077) : 
--
CREATE TABLE gestorfip.catalogosistematizado (
    id integer NOT NULL,
    ambito integer,
    nombre character varying(255),
    tramite integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.catalogosistematizado ALTER COLUMN id SET STATISTICS 0;
--
-- Structure for table tramite (OID = 60082) : 
--
CREATE TABLE gestorfip.tramite (
    id integer NOT NULL,
    tipotramite integer,
    codigo character varying(32),
    texto character varying,
    fip integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite ALTER COLUMN id SET STATISTICS 0;
--
-- Structure for table tramite_documentos (OID = 60100) : 
--
CREATE TABLE gestorfip.tramite_documentos (
    id integer NOT NULL,
    grupo integer,
    tipo integer,
    escala bigint,
    archivo character varying(255),
    nombre character varying(255),
    codigo character varying(10),
    comentario character varying,
    tramite integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_documentos ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_documentos ALTER COLUMN grupo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_documentos ALTER COLUMN tipo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_documentos ALTER COLUMN escala SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_documentos ALTER COLUMN archivo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_documentos ALTER COLUMN nombre SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_documentos ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_documentos ALTER COLUMN comentario SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_documentos ALTER COLUMN tramite SET STATISTICS 0;
--
-- Structure for table tramite_documento_hojas (OID = 60113) : 
--
CREATE TABLE gestorfip.tramite_documento_hojas (
    id integer NOT NULL,
    nombre character varying(255),
    geometria public.geometry,
    documento integer,
    idfeature integer default -1,
    idlayer integer default -1
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_documento_hojas ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_documento_hojas ALTER COLUMN nombre SET STATISTICS 0;
--
-- Structure for table tramite_entidades (OID = 60136) : 
--
CREATE TABLE gestorfip.tramite_entidades (
    id integer NOT NULL,
    codigo character varying(10),
    etiqueta character varying(255),
    clave character varying(255),
    nombre character varying(255),
    suspendida boolean,
    geometria public.geometry,
    base_entidadid integer,
    madre integer,
    tramite integer,
    idfeature integer default -1,
    idlayer integer default -1
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_entidades ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_entidades ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_entidades ALTER COLUMN etiqueta SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_entidades ALTER COLUMN clave SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_entidades ALTER COLUMN nombre SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_entidades ALTER COLUMN suspendida SET STATISTICS 0;
--
-- Structure for table tramite_determinaciones (OID = 60169) : 
--
CREATE TABLE gestorfip.tramite_determinaciones (
    id integer NOT NULL,
    codigo character varying(10),
    apartado character varying(255),
    nombre character varying(255),
    etiqueta character varying(255),
    suspendida boolean,
    texto character varying,
    unidad_determinacionid integer,
    base_determinacionid integer,
    madre integer,
    tramite integer,
    caracterid integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_determinaciones ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_determinaciones ALTER COLUMN codigo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_determinaciones ALTER COLUMN apartado SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_determinaciones ALTER COLUMN nombre SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_determinaciones ALTER COLUMN etiqueta SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_determinaciones ALTER COLUMN suspendida SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_determinaciones ALTER COLUMN texto SET STATISTICS 0;
--
-- Structure for table tramite_condicionesurbanisticas (OID = 60325) : 
--
CREATE TABLE gestorfip.tramite_condicionesurbanisticas (
    id integer NOT NULL,
    codigoentidad_entidadid integer,
    codigodeterminacion_determinacionid integer,
    tramite integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_condicionesurbanisticas ALTER COLUMN codigodeterminacion_determinacionid SET STATISTICS 0;
--
-- Structure for table tramite_condicionurbanistica_casos (OID = 60345) : 
--
CREATE TABLE gestorfip.tramite_condicionurbanistica_casos (
    id integer NOT NULL,
    nombre character varying(255),
    codigo character varying(10),
    condicionurbanistica integer
) WITHOUT OIDS;
--
-- Structure for table tramite_condicionurbanistica_caso_regimenes (OID = 60355) : 
--
CREATE TABLE gestorfip.tramite_condicionurbanistica_caso_regimenes (
    id integer NOT NULL,
    comentario character varying,
    valor character varying,
    superposicion integer,
    valorreferencia_determinacionid integer,
    determinacionregimen_determinacionid integer,
    casoaplicacion_casoid integer,
    caso integer
) WITHOUT OIDS;
--
-- Structure for table tramite_condicionurbanistica_caso_regimen_regimenesespecificos (OID = 60383) : 
--
CREATE TABLE gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos (
    id integer NOT NULL,
    orden integer,
    nombre character varying(255),
    texto character varying,
    padre integer,
    regimen integer
) WITHOUT OIDS;
--
-- Structure for table tramite_condicionurbanistica_caso_vinculos (OID = 60436) : 
--
CREATE TABLE gestorfip.tramite_condicionurbanistica_caso_vinculos (
    id integer NOT NULL,
    casoid integer,
    caso integer
) WITHOUT OIDS;
--
-- Structure for table tramite_condicionurbanistica_caso_documentos (OID = 60451) : 
--
CREATE TABLE gestorfip.tramite_condicionurbanistica_caso_documentos (
    id integer NOT NULL,
    documentoid integer,
    caso integer
) WITHOUT OIDS;
--
-- Structure for table tramite_operacionesdeterminaciones (OID = 60466) : 
--
CREATE TABLE gestorfip.tramite_operacionesdeterminaciones (
    id integer NOT NULL,
    tipo integer,
    orden bigint,
    texto character varying,
    operada_determinacionid integer,
    operadora_determinacionid integer,
    tramite integer
) WITHOUT OIDS;
--
-- Structure for table tramite_operacionesentidades (OID = 60509) : 
--
CREATE TABLE gestorfip.tramite_operacionesentidades (
    id integer NOT NULL,
    tipo integer,
    orden bigint,
    texto character varying,
    operada_entidadid integer,
    operadora_entidadid integer,
    propiedadesadscripcion_cuantia double precision,
    propiedadesadscripcion_texto character varying,
    propiedadesadscripcion_unidad_determinacionid integer,
    propiedadesadscripcion_tipo_determinacionid integer,
    tramite integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_operacionesentidades ALTER COLUMN tipo SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_operacionesentidades ALTER COLUMN orden SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_operacionesentidades ALTER COLUMN texto SET STATISTICS 0;
--
-- Structure for table tramite_unidades (OID = 60598) : 
--
CREATE TABLE gestorfip.tramite_unidades (
    id integer NOT NULL,
    abreviatura character varying(255),
    definicion character varying,
    determinacionid integer,
    tramite integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_unidades ALTER COLUMN abreviatura SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_unidades ALTER COLUMN definicion SET STATISTICS 0;
--
-- Structure for table tramite_aplicacionambitos (OID = 60616) : 
--
CREATE TABLE gestorfip.tramite_aplicacionambitos (
    id integer NOT NULL,
    ambitoid integer,
    entidadid integer,
    tramite integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_aplicacionambitos ALTER COLUMN ambitoid SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_aplicacionambitos ALTER COLUMN entidadid SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_aplicacionambitos ALTER COLUMN tramite SET STATISTICS 0;
--
-- Definition for sequence seq_catalogosistematizado (OID = 60812) : 
--
CREATE SEQUENCE gestorfip.seq_catalogosistematizado
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_determinacion_regulacionesespecificas (OID = 60814) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_determinacion_regulacionesespecificas
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite (OID = 60816) : 
--
CREATE SEQUENCE gestorfip.seq_tramite
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_adscripciones (OID = 60818) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_adscripciones
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_aplicacionambitos (OID = 60820) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_aplicacionambitos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_condicionesurbanisticas (OID = 60822) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_condicionesurbanisticas
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_condicionurbanistica_caso_documentos (OID = 60824) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_condicionurbanistica_caso_documentos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_condicionurbanistica_caso_regimen_regimenesespecifi (OID = 60826) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_condicionurbanistica_caso_regimen_regimenesespecifi
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_condicionurbanistica_caso_regimenes (OID = 60828) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_condicionurbanistica_caso_regimenes
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_condicionurbanistica_caso_vinculos (OID = 60830) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_condicionurbanistica_caso_vinculos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_condicionurbanistica_casos (OID = 60832) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_condicionurbanistica_casos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_determinacion_determinacionesreguladoras (OID = 60834) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_determinacion_determinacionesreguladoras
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_determinacion_documentos (OID = 60836) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_determinacion_documentos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_determinacion_gruposaplicacion (OID = 60838) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_determinacion_gruposaplicacion
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_determinacion_valoresreferencia (OID = 60840) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_determinacion_valoresreferencia
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_determinaciones (OID = 60842) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_determinaciones
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_documento_hojas (OID = 60844) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_documento_hojas
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_documentos (OID = 60846) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_documentos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_entidad_documentos (OID = 60848) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_entidad_documentos
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_entidades (OID = 60850) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_entidades
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_operacionesdeterminaciones (OID = 60852) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_operacionesdeterminaciones
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_operacionesentidades (OID = 60854) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_operacionesentidades
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_tramite_unidades (OID = 60856) : 
--
CREATE SEQUENCE gestorfip.seq_tramite_unidades
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Structure for table planeamientovigente (OID = 75641) : 
--
CREATE TABLE gestorfip.planeamientovigente (
    id integer NOT NULL,
    ambito integer,
    nombre character varying(255),
    tramite integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.planeamientovigente ALTER COLUMN id SET STATISTICS 0;
--
-- Structure for table planeamientoencargado (OID = 75656) : 
--
CREATE TABLE gestorfip.planeamientoencargado (
    id integer NOT NULL,
    ambito integer,
    nombre character varying(255),
    instrumento integer,
    iteracion integer,
    tipotramite integer,
    codigotramite character varying(32),
    ambitoaplicacion public.geometry,
    fip integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.planeamientoencargado ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.planeamientoencargado ALTER COLUMN ambito SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.planeamientoencargado ALTER COLUMN nombre SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.planeamientoencargado ALTER COLUMN instrumento SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.planeamientoencargado ALTER COLUMN iteracion SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.planeamientoencargado ALTER COLUMN tipotramite SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.planeamientoencargado ALTER COLUMN codigotramite SET STATISTICS 0;
--
-- Definition for sequence seq_planeamientovigente (OID = 75826) : 
--
CREATE SEQUENCE gestorfip.seq_planeamientovigente
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Definition for sequence seq_planeamientoencargado (OID = 75828) : 
--
CREATE SEQUENCE gestorfip.seq_planeamientoencargado
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 999999999999999999
    NO MINVALUE
    CACHE 1;
--
-- Structure for table tramite_adscripciones (OID = 100555) : 
--
CREATE TABLE gestorfip.tramite_adscripciones (
    id integer NOT NULL,
    entidadorigenid integer,
    entidaddestinoid integer,
    propiedades_cuantia double precision,
    propiedades_texto character varying,
    propiedades_unidad_determinacionid integer,
    propiedades_tipo_determinacionid integer,
    tramite integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_adscripciones ALTER COLUMN entidaddestinoid SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_adscripciones ALTER COLUMN propiedades_cuantia SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_adscripciones ALTER COLUMN propiedades_unidad_determinacionid SET STATISTICS 0;
--
-- Structure for table tramite_entidad_documentos (OID = 113947) : 
--
CREATE TABLE gestorfip.tramite_entidad_documentos (
    id integer NOT NULL,
    documentoid integer,
    entidad integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_entidad_documentos ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_entidad_documentos ALTER COLUMN documentoid SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_entidad_documentos ALTER COLUMN entidad SET STATISTICS 0;
--
-- Structure for table tramite_determinacion_documentos (OID = 113962) : 
--
CREATE TABLE gestorfip.tramite_determinacion_documentos (
    id integer NOT NULL,
    documentoid integer,
    determinacion integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_determinacion_documentos ALTER COLUMN id SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_determinacion_documentos ALTER COLUMN documentoid SET STATISTICS 0;
--
-- Structure for table tramite_determinacion_determinacionesreguladoras (OID = 113977) : 
--
CREATE TABLE gestorfip.tramite_determinacion_determinacionesreguladoras (
    id integer NOT NULL,
    determinacionid integer,
    determinacion integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_determinacion_determinacionesreguladoras ALTER COLUMN determinacionid SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_determinacion_determinacionesreguladoras ALTER COLUMN determinacion SET STATISTICS 0;
--
-- Structure for table tramite_determinacion_regulacionesespecificas (OID = 113992) : 
--
CREATE TABLE gestorfip.tramite_determinacion_regulacionesespecificas (
    id integer NOT NULL,
    orden integer,
    nombre character varying(255),
    texto character varying,
    madre integer,
    determinacion integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_determinacion_regulacionesespecificas ALTER COLUMN madre SET STATISTICS 0;
--
-- Structure for table tramite_determinacion_gruposaplicacion (OID = 114010) : 
--
CREATE TABLE gestorfip.tramite_determinacion_gruposaplicacion (
    id integer NOT NULL,
    determinacionid integer,
    determinacion integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_determinacion_gruposaplicacion ALTER COLUMN determinacionid SET STATISTICS 0;
ALTER TABLE ONLY gestorfip.tramite_determinacion_gruposaplicacion ALTER COLUMN determinacion SET STATISTICS 0;
--
-- Structure for table tramite_determinacion_valoresreferencia (OID = 114025) : 
--
CREATE TABLE gestorfip.tramite_determinacion_valoresreferencia (
    id integer NOT NULL,
    determinacionid integer,
    determinacion integer
) WITHOUT OIDS;
ALTER TABLE ONLY gestorfip.tramite_determinacion_valoresreferencia ALTER COLUMN id SET STATISTICS 0;

--
-- Structure for table relacionambitomunicipio  : 
--
CREATE TABLE gestorfip.relacionambitomunicipio (
  codambito VARCHAR(6), 
  idmunicipio NUMERIC(5,0) NOT NULL
) WITHOUT OIDS;

--
-- Structure for table versiones_uer  : 
--
CREATE TABLE gestorfip.versiones_uer (
  id INTEGER NOT NULL, 
  version NUMERIC(5,2)
) WITHOUT OIDS;

--
-- Structure for table relacionambitomunicipio  : 
--

CREATE TABLE gestorfip.config (
  id INTEGER NOT NULL, 
  id_version INTEGER NOT NULL,
  id_crs INTEGER NOT NULL
) WITHOUT OIDS;


CREATE TABLE gestorfip.crs_system (
  "id" INTEGER NOT NULL,
  "crs" INTEGER NOT NULL
) WITHOUT OIDS;


ALTER TABLE ONLY gestorfip.relacionambitomunicipio
    ADD CONSTRAINT relacionambitomunicipio_pkey PRIMARY KEY (codambito, idmunicipio);
    
-- Definition for index fip_pkey (OID = 58752) : 
--
ALTER TABLE ONLY gestorfip.fip
    ADD CONSTRAINT fip_pkey PRIMARY KEY (id);
--
-- Definition for index tiposambitos_pkey (OID = 58762) : 
--
ALTER TABLE ONLY gestorfip.diccionario_tiposambito
    ADD CONSTRAINT tiposambitos_pkey PRIMARY KEY (id);
--
-- Definition for index ambito_pkey (OID = 58767) : 
--
ALTER TABLE ONLY gestorfip.diccionario_ambitos
    ADD CONSTRAINT ambito_pkey PRIMARY KEY (id);
--
-- Definition for index ambito_fk1 (OID = 58779) : 
--
ALTER TABLE ONLY gestorfip.diccionario_ambitos
    ADD CONSTRAINT ambito_fk1 FOREIGN KEY (tipoambito) REFERENCES gestorfip.diccionario_tiposambito(id) ON DELETE CASCADE;
	--
-- Definition for index organigramaambitos_pkey (OID = 58807) : 
--
ALTER TABLE ONLY gestorfip.diccionario_organigramaambitos
    ADD CONSTRAINT organigramaambitos_pkey PRIMARY KEY (id);
--
-- Definition for index organigramaambitos_fk (OID = 58787) : 
--
ALTER TABLE ONLY gestorfip.diccionario_organigramaambitos
    ADD CONSTRAINT organigramaambitos_fk FOREIGN KEY (padre) REFERENCES gestorfip.diccionario_ambitos(id) ON DELETE CASCADE;
--
-- Definition for index organigramaambitos_fk1 (OID = 58792) : 
--
ALTER TABLE ONLY gestorfip.diccionario_organigramaambitos
    ADD CONSTRAINT organigramaambitos_fk1 FOREIGN KEY (hijo) REFERENCES gestorfip.diccionario_ambitos(id) ON DELETE CASCADE;
--
-- Definition for index caracterdeterminacion_pkey (OID = 58800) : 
--
ALTER TABLE ONLY gestorfip.diccionario_caracteresdeterminacion
    ADD CONSTRAINT caracterdeterminacion_pkey PRIMARY KEY (id);
--
-- Definition for index diccionario_tiposentidad_pkey (OID = 58812) : 
--
ALTER TABLE ONLY gestorfip.diccionario_tiposentidad
    ADD CONSTRAINT diccionario_tiposentidad_pkey PRIMARY KEY (id);
--
-- Definition for index diccionario_tiposoperacionentidad_pkey (OID = 58822) : 
--
ALTER TABLE ONLY gestorfip.diccionario_tiposoperacionentidad
    ADD CONSTRAINT diccionario_tiposoperacionentidad_pkey PRIMARY KEY (id);
--
-- Definition for index diccionario_tiposoperacionentidad_fk (OID = 58824) : 
--
ALTER TABLE ONLY gestorfip.diccionario_tiposoperacionentidad
    ADD CONSTRAINT diccionario_tiposoperacionentidad_fk FOREIGN KEY (tipoentidad) REFERENCES gestorfip.diccionario_tiposentidad(id) ON DELETE CASCADE;
--
-- Definition for index diccionario_tiposoperaciondeterminacion_pkey (OID = 58832) : 
--
ALTER TABLE ONLY gestorfip.diccionario_tiposoperaciondeterminacion
    ADD CONSTRAINT diccionario_tiposoperaciondeterminacion_pkey PRIMARY KEY (id);
--
-- Definition for index diccionario_operacionescaracteres_pkey (OID = 58842) : 
--
ALTER TABLE ONLY gestorfip.diccionario_operacionescaracteres
    ADD CONSTRAINT diccionario_operacionescaracteres_pkey PRIMARY KEY (id);
--
-- Definition for index diccionario_operacionescaracteres_fk (OID = 58844) : 
--
ALTER TABLE ONLY gestorfip.diccionario_operacionescaracteres
    ADD CONSTRAINT diccionario_operacionescaracteres_fk FOREIGN KEY (tipooperaciondeterminacion) REFERENCES gestorfip.diccionario_tiposoperaciondeterminacion(id) ON DELETE CASCADE;
--
-- Definition for index diccionario_operacionescaracteres_fk1 (OID = 58849) : 
--
ALTER TABLE ONLY gestorfip.diccionario_operacionescaracteres
    ADD CONSTRAINT diccionario_operacionescaracteres_fk1 FOREIGN KEY (caracteroperadora) REFERENCES gestorfip.diccionario_caracteresdeterminacion(id) ON DELETE CASCADE;
--
-- Definition for index diccionario_operacionescaracteres_fk2 (OID = 58854) : 
--
ALTER TABLE ONLY gestorfip.diccionario_operacionescaracteres
    ADD CONSTRAINT diccionario_operacionescaracteres_fk2 FOREIGN KEY (caracteroperada) REFERENCES gestorfip.diccionario_caracteresdeterminacion(id) ON DELETE CASCADE;
--
-- Definition for index diccionario_tiposdocumento_pkey (OID = 58862) : 
--
ALTER TABLE ONLY gestorfip.diccionario_tiposdocumento
    ADD CONSTRAINT diccionario_tiposdocumento_pkey PRIMARY KEY (id);
--
-- Definition for index diccionario_gruposdocumento_pkey (OID = 58872) : 
--
ALTER TABLE ONLY gestorfip.diccionario_gruposdocumento
    ADD CONSTRAINT diccionario_gruposdocumento_pkey PRIMARY KEY (id);
--
-- Definition for index diccionario_tipostramite_pkey (OID = 58882) : 
--
ALTER TABLE ONLY gestorfip.diccionario_tipostramite
    ADD CONSTRAINT diccionario_tipostramite_pkey PRIMARY KEY (id);
--
-- Definition for index diccionario_instrumentos_pkey (OID = 58892) : 
--
ALTER TABLE ONLY gestorfip.diccionario_instrumentos
    ADD CONSTRAINT diccionario_instrumentos_pkey PRIMARY KEY (id);
--
-- Definition for index catalogosistematizado_pkey (OID = 60080) : 
--
ALTER TABLE ONLY gestorfip.catalogosistematizado
    ADD CONSTRAINT catalogosistematizado_pkey PRIMARY KEY (id);
--
-- Definition for index catalogosistematizado_fk (OID = 75631) : 
--
ALTER TABLE ONLY gestorfip.catalogosistematizado
    ADD CONSTRAINT catalogosistematizado_fk FOREIGN KEY (ambito) REFERENCES gestorfip.diccionario_ambitos(id) ON DELETE CASCADE;

--
-- Definition for index tramite_pkey (OID = 60088) : 
--
ALTER TABLE ONLY gestorfip.tramite
    ADD CONSTRAINT tramite_pkey PRIMARY KEY (id);	
    
--
-- Definition for index catalogosistematizado_fk1 (OID = 75636) : 
--
ALTER TABLE ONLY gestorfip.catalogosistematizado
    ADD CONSTRAINT catalogosistematizado_fk1 FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;
    
--
-- Definition for index tramite_fk (OID = 77501) : 
--
ALTER TABLE ONLY gestorfip.tramite
    ADD CONSTRAINT tramite_fk FOREIGN KEY (tipotramite) REFERENCES gestorfip.diccionario_tipostramite(id) ON DELETE CASCADE;
--
-- Definition for index tramite_fk1 (OID = 63460) : 
--
ALTER TABLE ONLY gestorfip.tramite
    ADD CONSTRAINT tramite_fk1 FOREIGN KEY (fip) REFERENCES gestorfip.fip(id) ON DELETE CASCADE;
--
-- Definition for index tramite_documentos_pkey (OID = 60106) : 
--
ALTER TABLE ONLY gestorfip.tramite_documentos
    ADD CONSTRAINT tramite_documentos_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_documentos_fk (OID = 60108) : 
--
ALTER TABLE ONLY gestorfip.tramite_documentos
    ADD CONSTRAINT tramite_documentos_fk FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;
--
-- Definition for index tramite_documentos_fk1 (OID = 60126) : 
--
ALTER TABLE ONLY gestorfip.tramite_documentos
    ADD CONSTRAINT tramite_documentos_fk1 FOREIGN KEY (grupo) REFERENCES gestorfip.diccionario_gruposdocumento(id) ON DELETE CASCADE;
--
-- Definition for index tramite_documentos_fk2 (OID = 60131) : 
--
ALTER TABLE ONLY gestorfip.tramite_documentos
    ADD CONSTRAINT tramite_documentos_fk2 FOREIGN KEY (tipo) REFERENCES gestorfip.diccionario_tiposdocumento(id) ON DELETE CASCADE;
--
-- Definition for index tramite_hojas_pkey (OID = 60119) : 
--
ALTER TABLE ONLY gestorfip.tramite_documento_hojas
    ADD CONSTRAINT tramite_hojas_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_documento_hojas_fk (OID = 60777) : 
--
ALTER TABLE ONLY gestorfip.tramite_documento_hojas
    ADD CONSTRAINT tramite_documento_hojas_fk FOREIGN KEY (documento) REFERENCES gestorfip.tramite_documentos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_entidades_pkey (OID = 60142) : 
--
ALTER TABLE ONLY gestorfip.tramite_entidades
    ADD CONSTRAINT tramite_entidades_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_entidades_fk (OID = 60787) : 
--
ALTER TABLE ONLY gestorfip.tramite_entidades
    ADD CONSTRAINT tramite_entidades_fk FOREIGN KEY (base_entidadid) REFERENCES gestorfip.tramite_entidades(id) ON DELETE CASCADE;
--
-- Definition for index tramite_entidades_fk1 (OID = 66538) : 
--
ALTER TABLE ONLY gestorfip.tramite_entidades
    ADD CONSTRAINT tramite_entidades_fk1 FOREIGN KEY (madre) REFERENCES gestorfip.tramite_entidades(id) ON DELETE CASCADE;
--
-- Definition for index tramite_entidades_fk2 (OID = 60797) : 
--
ALTER TABLE ONLY gestorfip.tramite_entidades
    ADD CONSTRAINT tramite_entidades_fk2 FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;	
--
-- Definition for index tramite_determinaciones_pkey (OID = 60175) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinaciones
    ADD CONSTRAINT tramite_determinaciones_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_determinaciones_fk2 (OID = 71712) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinaciones
    ADD CONSTRAINT tramite_determinaciones_fk2 FOREIGN KEY (madre) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_determinaciones_fk3 (OID = 71707) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinaciones
    ADD CONSTRAINT tramite_determinaciones_fk3 FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;	
--
-- Definition for index tramite_determinaciones_fk4 (OID = 114050) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinaciones
    ADD CONSTRAINT tramite_determinaciones_fk4 FOREIGN KEY (caracterid) REFERENCES gestorfip.diccionario_caracteresdeterminacion(id) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED;
--
-- Definition for index tramite_condicionesurbanisticas_pkey (OID = 60328) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionesurbanisticas
    ADD CONSTRAINT tramite_condicionesurbanisticas_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_condicionesurbanisticas_fk (OID = 60330) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionesurbanisticas
    ADD CONSTRAINT tramite_condicionesurbanisticas_fk FOREIGN KEY (codigoentidad_entidadid) REFERENCES gestorfip.tramite_entidades(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionesurbanisticas_fk1 (OID = 60335) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionesurbanisticas
    ADD CONSTRAINT tramite_condicionesurbanisticas_fk1 FOREIGN KEY (codigodeterminacion_determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionesurbanisticas_fk2 (OID = 73216) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionesurbanisticas
    ADD CONSTRAINT tramite_condicionesurbanisticas_fk2 FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_casos_pkey (OID = 60348) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_casos
    ADD CONSTRAINT tramite_condicionurbanistica_casos_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_condicionurbanistica_casos_fk (OID = 60350) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_casos
    ADD CONSTRAINT tramite_condicionurbanistica_casos_fk FOREIGN KEY (condicionurbanistica) REFERENCES gestorfip.tramite_condicionesurbanisticas(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_regimenes_pkey (OID = 60361) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_regimenes
    ADD CONSTRAINT tramite_condicionurbanistica_caso_regimenes_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_condicionurbanistica_caso_regimenes_fk (OID = 60697) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_regimenes
    ADD CONSTRAINT tramite_condicionurbanistica_caso_regimenes_fk FOREIGN KEY (valorreferencia_determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_regimenes_fk1 (OID = 60702) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_regimenes
    ADD CONSTRAINT tramite_condicionurbanistica_caso_regimenes_fk1 FOREIGN KEY (determinacionregimen_determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_regimenes_fk2 (OID = 60707) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_regimenes
    ADD CONSTRAINT tramite_condicionurbanistica_caso_regimenes_fk2 FOREIGN KEY (casoaplicacion_casoid) REFERENCES gestorfip.tramite_condicionurbanistica_casos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_regimenes_fk3 (OID = 60712) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_regimenes
    ADD CONSTRAINT tramite_condicionurbanistica_caso_regimenes_fk3 FOREIGN KEY (caso) REFERENCES gestorfip.tramite_condicionurbanistica_casos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_regimen_regimenesespecif_pkey (OID = 60389) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos
    ADD CONSTRAINT tramite_condicionurbanistica_caso_regimen_regimenesespecif_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_condicionurbanistica_caso_regimen_regimenesespecifi_fk (OID = 60802) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos
    ADD CONSTRAINT tramite_condicionurbanistica_caso_regimen_regimenesespecifi_fk FOREIGN KEY (padre) REFERENCES gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_regimen_regimenesespecifi_fk1 (OID = 60807) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos
    ADD CONSTRAINT tramite_condicionurbanistica_caso_regimen_regimenesespecifi_fk1 FOREIGN KEY (regimen) REFERENCES gestorfip.tramite_condicionurbanistica_caso_regimenes(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_vinculos_pkey (OID = 60439) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_vinculos
    ADD CONSTRAINT tramite_condicionurbanistica_caso_vinculos_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_condicionurbanistica_caso_vinculos_fk (OID = 60441) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_vinculos
    ADD CONSTRAINT tramite_condicionurbanistica_caso_vinculos_fk FOREIGN KEY (casoid) REFERENCES gestorfip.tramite_condicionurbanistica_casos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_vinculos_fk1 (OID = 60446) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_vinculos
    ADD CONSTRAINT tramite_condicionurbanistica_caso_vinculos_fk1 FOREIGN KEY (caso) REFERENCES gestorfip.tramite_condicionurbanistica_casos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_documentos_pkey (OID = 60454) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_documentos
    ADD CONSTRAINT tramite_condicionurbanistica_caso_documentos_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_condicionurbanistica_caso_documentos_fk (OID = 60456) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_documentos
    ADD CONSTRAINT tramite_condicionurbanistica_caso_documentos_fk FOREIGN KEY (documentoid) REFERENCES gestorfip.tramite_documentos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_condicionurbanistica_caso_documentos_fk1 (OID = 60461) : 
--
ALTER TABLE ONLY gestorfip.tramite_condicionurbanistica_caso_documentos
    ADD CONSTRAINT tramite_condicionurbanistica_caso_documentos_fk1 FOREIGN KEY (caso) REFERENCES gestorfip.tramite_condicionurbanistica_casos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_operacionesdeterminaciones_pkey (OID = 60472) : 
--
ALTER TABLE ONLY gestorfip.tramite_operacionesdeterminaciones
    ADD CONSTRAINT tramite_operacionesdeterminaciones_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_operacionesdeterminaciones_fk (OID = 60717) : 
--
ALTER TABLE ONLY gestorfip.tramite_operacionesdeterminaciones
    ADD CONSTRAINT tramite_operacionesdeterminaciones_fk FOREIGN KEY (operada_determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_operacionesdeterminaciones_fk1 (OID = 60722) : 
--
ALTER TABLE ONLY gestorfip.tramite_operacionesdeterminaciones
    ADD CONSTRAINT tramite_operacionesdeterminaciones_fk1 FOREIGN KEY (operadora_determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_operacionesdeterminaciones_fk2 (OID = 60727) : 
--
ALTER TABLE ONLY gestorfip.tramite_operacionesdeterminaciones
    ADD CONSTRAINT tramite_operacionesdeterminaciones_fk2 FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;
--
-- Definition for index tramite_operacionesdeterminaciones_fk3 (OID = 60717) : 
--
ALTER TABLE ONLY gestorfip.tramite_operacionesdeterminaciones
    ADD CONSTRAINT tramite_operacionesdeterminaciones_fk3 FOREIGN KEY (tipo) REFERENCES gestorfip.diccionario_tiposoperaciondeterminacion(id) ON DELETE CASCADE;	
	
	
--
-- Definition for index tramite_operacionesentidades_pkey (OID = 60515) : 
--
ALTER TABLE ONLY gestorfip.tramite_operacionesentidades
    ADD CONSTRAINT tramite_operacionesentidades_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_operacionesentidades_fk (OID = 60732) : 
--
ALTER TABLE ONLY gestorfip.tramite_operacionesentidades
    ADD CONSTRAINT tramite_operacionesentidades_fk FOREIGN KEY (operada_entidadid) REFERENCES gestorfip.tramite_entidades(id) ON DELETE CASCADE;
--
-- Definition for index tramite_operacionesentidades_fk1 (OID = 60737) : 
--
ALTER TABLE ONLY gestorfip.tramite_operacionesentidades
    ADD CONSTRAINT tramite_operacionesentidades_fk1 FOREIGN KEY (operadora_entidadid) REFERENCES gestorfip.tramite_entidades(id) ON DELETE CASCADE;
--
-- Definition for index tramite_operacionesentidades_fk2 (OID = 60762) : 
--
ALTER TABLE ONLY gestorfip.tramite_operacionesentidades
    ADD CONSTRAINT tramite_operacionesentidades_fk2 FOREIGN KEY (propiedadesadscripcion_unidad_determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_unidades_pkey (OID = 60604) : 
--
ALTER TABLE ONLY gestorfip.tramite_unidades
    ADD CONSTRAINT tramite_unidades_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_unidades_fk (OID = 60606) : 
--
ALTER TABLE ONLY gestorfip.tramite_unidades
    ADD CONSTRAINT tramite_unidades_fk FOREIGN KEY (determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_unidades_fk1 (OID = 60611) : 
--
ALTER TABLE ONLY gestorfip.tramite_unidades
    ADD CONSTRAINT tramite_unidades_fk1 FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;
--
-- Definition for index tramite_aplicacionambitos_pkey (OID = 60619) : 
--
ALTER TABLE ONLY gestorfip.tramite_aplicacionambitos
    ADD CONSTRAINT tramite_aplicacionambitos_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_aplicacionambitos_fk (OID = 60621) : 
--
ALTER TABLE ONLY gestorfip.tramite_aplicacionambitos
    ADD CONSTRAINT tramite_aplicacionambitos_fk FOREIGN KEY (ambitoid) REFERENCES gestorfip.diccionario_ambitos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_aplicacionambitos_fk1 (OID = 60626) : 
--
ALTER TABLE ONLY gestorfip.tramite_aplicacionambitos
    ADD CONSTRAINT tramite_aplicacionambitos_fk1 FOREIGN KEY (entidadid) REFERENCES gestorfip.tramite_entidades(id) ON DELETE CASCADE;
--
-- Definition for index tramite_aplicacionambitos_fk2 (OID = 60631) : 
--
ALTER TABLE ONLY gestorfip.tramite_aplicacionambitos
    ADD CONSTRAINT tramite_aplicacionambitos_fk2 FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;
--
-- Definition for index planeamientovigente_pkey (OID = 75644) : 
--
ALTER TABLE ONLY gestorfip.planeamientovigente
    ADD CONSTRAINT planeamientovigente_pkey PRIMARY KEY (id);
--
-- Definition for index planeamientovigente_fk (OID = 75646) : 
--
ALTER TABLE ONLY gestorfip.planeamientovigente
    ADD CONSTRAINT planeamientovigente_fk FOREIGN KEY (ambito) REFERENCES gestorfip.diccionario_ambitos(id) ON DELETE CASCADE;
--
-- Definition for index planeamientovigente_fk1 (OID = 75651) : 
--
ALTER TABLE ONLY gestorfip.planeamientovigente
    ADD CONSTRAINT planeamientovigente_fk1 FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;
--
-- Definition for index planeamientoencargado_pkey (OID = 75662) : 
--
ALTER TABLE ONLY gestorfip.planeamientoencargado
    ADD CONSTRAINT planeamientoencargado_pkey PRIMARY KEY (id);
--
-- Definition for index planeamientoencargado_fk (OID = 75664) : 
--
ALTER TABLE ONLY gestorfip.planeamientoencargado
    ADD CONSTRAINT planeamientoencargado_fk FOREIGN KEY (ambito) REFERENCES gestorfip.diccionario_ambitos(id) ON DELETE CASCADE;
--
-- Definition for index planeamientoencargado_fk1 (OID = 75670) : 
--
ALTER TABLE ONLY gestorfip.planeamientoencargado
    ADD CONSTRAINT planeamientoencargado_fk1 FOREIGN KEY (instrumento) REFERENCES gestorfip.diccionario_instrumentos(id) ON DELETE CASCADE;
--
-- Definition for index planeamientoencargado_fk2 (OID = 75675) : 
--
ALTER TABLE ONLY gestorfip.planeamientoencargado
    ADD CONSTRAINT planeamientoencargado_fk2 FOREIGN KEY (tipotramite) REFERENCES gestorfip.diccionario_tipostramite(id) ON DELETE CASCADE;
--
-- Definition for index planeamientoencargado_fk3 (OID = 75680) : 
--
ALTER TABLE ONLY gestorfip.planeamientoencargado
    ADD CONSTRAINT planeamientoencargado_fk3 FOREIGN KEY (fip) REFERENCES gestorfip.fip(id) ON DELETE CASCADE;
--
-- Definition for index tramite_adscripciones_pkey (OID = 100561) : 
--
ALTER TABLE ONLY gestorfip.tramite_adscripciones
    ADD CONSTRAINT tramite_adscripciones_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_adscripciones_fk (OID = 100563) : 
--
ALTER TABLE ONLY gestorfip.tramite_adscripciones
    ADD CONSTRAINT tramite_adscripciones_fk FOREIGN KEY (entidadorigenid) REFERENCES gestorfip.tramite_entidades(id) ON DELETE CASCADE;
--
-- Definition for index tramite_adscripciones_fk1 (OID = 100568) : 
--
ALTER TABLE ONLY gestorfip.tramite_adscripciones
    ADD CONSTRAINT tramite_adscripciones_fk1 FOREIGN KEY (entidaddestinoid) REFERENCES gestorfip.tramite_entidades(id) ON DELETE CASCADE;
--
-- Definition for index tramite_adscripciones_fk2 (OID = 100573) : 
--
ALTER TABLE ONLY gestorfip.tramite_adscripciones
    ADD CONSTRAINT tramite_adscripciones_fk2 FOREIGN KEY (propiedades_unidad_determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_adscripciones_fk3 (OID = 100578) : 
--
ALTER TABLE ONLY gestorfip.tramite_adscripciones
    ADD CONSTRAINT tramite_adscripciones_fk3 FOREIGN KEY (propiedades_tipo_determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_adscripciones_fk4 (OID = 100583) : 
--
ALTER TABLE ONLY gestorfip.tramite_adscripciones
    ADD CONSTRAINT tramite_adscripciones_fk4 FOREIGN KEY (tramite) REFERENCES gestorfip.tramite(id) ON DELETE CASCADE;
--
-- Definition for index tramite_entidad_documentos_pkey (OID = 113950) : 
--
ALTER TABLE ONLY gestorfip.tramite_entidad_documentos
    ADD CONSTRAINT tramite_entidad_documentos_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_entidad_documentos_fk (OID = 113952) : 
--
ALTER TABLE ONLY gestorfip.tramite_entidad_documentos
    ADD CONSTRAINT tramite_entidad_documentos_fk FOREIGN KEY (documentoid) REFERENCES gestorfip.tramite_documentos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_entidad_documentos_fk1 (OID = 113957) : 
--
ALTER TABLE ONLY gestorfip.tramite_entidad_documentos
    ADD CONSTRAINT tramite_entidad_documentos_fk1 FOREIGN KEY (entidad) REFERENCES gestorfip.tramite_entidades(id) ON DELETE CASCADE;
--
-- Definition for index tramite_determinacion_documentos_pkey (OID = 113965) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_documentos
    ADD CONSTRAINT tramite_determinacion_documentos_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_determinacion_documentos_fk (OID = 113967) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_documentos
    ADD CONSTRAINT tramite_determinacion_documentos_fk FOREIGN KEY (documentoid) REFERENCES gestorfip.tramite_documentos(id) ON DELETE CASCADE;
--
-- Definition for index tramite_determinacion_documentos_fk1 (OID = 113972) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_documentos
    ADD CONSTRAINT tramite_determinacion_documentos_fk1 FOREIGN KEY (determinacion) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_determinacion_determinacionesreguladoras_pkey (OID = 113980) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_determinacionesreguladoras
    ADD CONSTRAINT tramite_determinacion_determinacionesreguladoras_pkey PRIMARY KEY (id);
-- Definition for index tramite_determinacion_determinacionesreguladoras_fk1 (OID = 113987) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_determinacionesreguladoras
    ADD CONSTRAINT tramite_determinacion_determinacionesreguladoras_fk1 FOREIGN KEY (determinacion) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index determinacion_regulacionesespecificas_pkey (OID = 113998) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_regulacionesespecificas
    ADD CONSTRAINT determinacion_regulacionesespecificas_pkey PRIMARY KEY (id);
--
-- Definition for index determinacion_regulacionesespecificas_fk1 (OID = 114005) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_regulacionesespecificas
    ADD CONSTRAINT determinacion_regulacionesespecificas_fk1 FOREIGN KEY (determinacion) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_determinacion_gruposaplicacion_pkey (OID = 114013) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_gruposaplicacion
    ADD CONSTRAINT tramite_determinacion_gruposaplicacion_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_determinacion_gruposaplicacion_fk (OID = 114015) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_gruposaplicacion
    ADD CONSTRAINT tramite_determinacion_gruposaplicacion_fk FOREIGN KEY (determinacionid) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_determinacion_gruposaplicacion_fk1 (OID = 114020) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_gruposaplicacion
    ADD CONSTRAINT tramite_determinacion_gruposaplicacion_fk1 FOREIGN KEY (determinacion) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;
--
-- Definition for index tramite_determinacion_valoresreferencia_pkey (OID = 114028) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_valoresreferencia
    ADD CONSTRAINT tramite_determinacion_valoresreferencia_pkey PRIMARY KEY (id);
--
-- Definition for index tramite_determinacion_valoresreferencia_fk1 (OID = 114035) : 
--
ALTER TABLE ONLY gestorfip.tramite_determinacion_valoresreferencia
    ADD CONSTRAINT tramite_determinacion_valoresreferencia_fk1 FOREIGN KEY (determinacion) REFERENCES gestorfip.tramite_determinaciones(id) ON DELETE CASCADE;

    
ALTER TABLE ONLY gestorfip.crs_system
    ADD CONSTRAINT pk_crs_system PRIMARY KEY (id);
    
ALTER TABLE ONLY gestorfip.versiones_uer
    ADD CONSTRAINT pk_versiones_uer PRIMARY KEY (id);	
    
ALTER TABLE ONLY gestorfip.config
    ADD CONSTRAINT pk_config PRIMARY KEY (id);	

ALTER TABLE ONLY gestorfip.config
    ADD CONSTRAINT config_fk FOREIGN KEY (id_version) REFERENCES gestorfip.versiones_uer(id) ON DELETE CASCADE;

ALTER TABLE ONLY gestorfip.config
    ADD CONSTRAINT crs_fk FOREIGN KEY (id_crs) REFERENCES gestorfip.crs_system(id) ON DELETE CASCADE;


--
--
-- Comments
--
COMMENT ON SCHEMA public IS 'standard public schema';


------------------------------------------------------
-- DATOS GESTOR FIP ----------------------------------
------------------------------------------------------

INSERT INTO gestorfip.versiones_uer(id,version) VALUES (0,1.86);
INSERT INTO gestorfip.versiones_uer(id,version) VALUES (1,2.00);

INSERT INTO gestorfip.crs_system(id , crs) VALUES (0,23030);
INSERT INTO gestorfip.crs_system(id , crs) VALUES (1,25830);


INSERT INTO gestorfip.config(id , id_version, id_crs) VALUES (0,1,1);

------------------------------------------------------
-- UPDATE TABLAS GIS ---------------------------------
------------------------------------------------------
-- se ejecutan estas sentecias para que no se limite las layers y los estilos a 999
ALTER TABLE public.layers ALTER COLUMN id_styles TYPE NUMERIC(8,0);
ALTER TABLE public.styles ALTER COLUMN id_style TYPE NUMERIC(8,0);
ALTER TABLE public.layers_styles ALTER COLUMN id_style TYPE NUMERIC(8,0);

------------------------------------------------------
-- USERS ---------------------------------------------
------------------------------------------------------

--******************************************************
-- ACTUALIZACION DE SECUENCIAS
--******************************************************
select setval('public.seq_acl',(SELECT cast(max(idacl)as bigint) FROM public.acl));
select setval('public.seq_r_user_perm',(SELECT cast(max(userid)as bigint) FROM public.r_usr_perm));
select setval('public.seq_iuseruserhdr',(SELECT cast(max(id)as bigint) FROM public.iuseruserhdr));
select setval('public.seq_iusergrouphdr',(SELECT cast(max(id)as bigint) FROM public.iusergrouphdr));

--******************************************************
-- INSERCION ACL
--******************************************************
insert into public.acl (idacl, name) values (nextval('public.seq_acl'), 'GestorFip');

--******************************************************
-- INSERCION PERMISOS, RELACION PERMISO - ACL
--******************************************************

-- Permisos usuarios
insert into public.usrgrouperm (idperm, def) values ((SELECT cast(max((idperm) + 1 )as bigint) FROM public.usrgrouperm), 'LocalGIS.GestorFip.Login');
insert into public.r_acl_perm (idperm, idacl) values ((SELECT cast(max((idperm))as bigint) FROM public.usrgrouperm), currval('public.seq_acl'));

insert into public.usrgrouperm (idperm, def) values ((SELECT cast(max((idperm) + 1 )as bigint) FROM public.usrgrouperm), 'LocalGIS.GestorFip.Importador');
insert into public.r_acl_perm (idperm, idacl) values ((SELECT cast(max((idperm))as bigint) FROM public.usrgrouperm), currval('public.seq_acl'));

insert into public.usrgrouperm (idperm, def) values ((SELECT cast(max((idperm) + 1 )as bigint) FROM public.usrgrouperm), 'LocalGIS.GestorFip.Consulta');
insert into public.r_acl_perm (idperm, idacl) values ((SELECT cast(max((idperm))as bigint) FROM public.usrgrouperm), currval('public.seq_acl'));


-- Insercion permisos de grupo/roles: Gestor Fip
insert into public.iusergrouphdr (id, name, mgrid, type, remarks, crtrid, crtndate) values (nextval ('public.seq_iusergrouphdr'), 'Gestor Fip', 99, 0,'Rol del modulo Gestor Fip', 99,'2010-06-29');
insert into public.r_group_perm (groupid, idperm, idacl) values (currval ('public.seq_iusergrouphdr'), (SELECT idperm FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Login'), currval('public.seq_acl'));
insert into public.r_group_perm (groupid, idperm, idacl) values (currval ('public.seq_iusergrouphdr'), (SELECT idperm FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Importador'), currval('public.seq_acl'));
insert into public.r_group_perm (groupid, idperm, idacl) values (currval ('public.seq_iusergrouphdr'), (SELECT idperm FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Consulta'), currval('public.seq_acl'));

-- Insercion permisos de grupo/roles: ConsultaGestorFip
insert into public.iusergrouphdr (id, name, mgrid, type, remarks, crtrid, crtndate) values (nextval('public.seq_iusergrouphdr'), 'ConsultaGestorFip', 99, 0,'Rol consulta del modulo validador Fip', 99,'2010-06-29');
insert into public.r_group_perm (groupid, idperm, idacl) values (currval ('public.seq_iusergrouphdr'), (SELECT idperm FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Login'), currval('public.seq_acl'));
insert into public.r_group_perm (groupid, idperm, idacl) values (currval ('public.seq_iusergrouphdr'), (SELECT idperm FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Consulta'), currval('public.seq_acl'));


--******************************************************
-- INSERCION USUARIOS, RELACION USUARIO-PERMISO
--******************************************************

-- USUARIO SYSSUPERUSER 
--Insercion permisos para usuario: syssuperuser (fijo id=100)
insert into public.r_usr_perm (userid, idperm, idacl, aplica) values (100, (SELECT idperm FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Login'), currval('public.seq_acl'), 1);
insert into public.r_usr_perm (userid, idperm, idacl, aplica) values (100, (SELECT idperm FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Importador'), currval('public.seq_acl'), 1);
insert into public.r_usr_perm (userid, idperm, idacl, aplica) values (100, (SELECT idperm FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Consulta'), currval('public.seq_acl'), 1);

--******************************************************
-- INSERCION DATOS APLICACION
--******************************************************
select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));
-- Descripcion aplicacion
delete from public.dictionary where id_vocablo = (select id_dictionary from public.apps WHERE path = '/software/localgis-apps-gestorfip.jnlp');
delete from public.apps WHERE path = '/software/localgis-apps-gestorfip.jnlp';

INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('public.seq_dictionary'),'es_ES','Gestor FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'ca_ES','[cat]Gestor FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'gl_ES','[gl]Gestor FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'va_ES','[va]Gestor');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'eu_ES','[eu]Gestor FIP');

INSERT INTO public.apps (app, id_dictionary, acl, perm, app_type, path, active, install_name) VALUES ('Fip', CURRVAL('public.seq_dictionary'), (SELECT name FROM public.acl WHERE idacl=currval('public.seq_acl')), 'LocalGIS.GestorFip.Login', 'DESKTOP', '/software/localgis-apps-gestorfip.jnlp', true, 'GestorFipModule');

-- Permisos aplicacion
INSERT INTO public.appgeopista (appid, def, param1, param2) 
	SELECT (select cast(max((appid) + 1 )as bigint) from public.appgeopista), 'Gestor Fip', null, null WHERE NOT EXISTS (SELECT a.appid FROM public.appgeopista a WHERE a.def = 'Gestor Fip');
INSERT INTO public.app_acl(app, acl) VALUES ((SELECT a.appid FROM public.appgeopista a WHERE a.def = 'Gestor Fip'), currval('public.seq_acl'));

------------------------------------------------------
-- CAPAS ---------------------------------------------
------------------------------------------------------

-- Dominio padre
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('public.seq_dictionary'),'es_ES','Gestor Planeamiento FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'ca_ES','[cat]Gestor Planeamiento FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'gl_ES','[gl]Gestor Planeamiento FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'va_ES','[va]Gestor PlaneamientoFIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'eu_ES','[eu]Gestor Planeamiento FIP');
INSERT INTO public.DOMAINCATEGORY(ID,ID_DESCRIPTION) VALUES (NEXTVAL('public.seq_domaincategory'),CURRVAL('public.seq_dictionary'));
INSERT INTO public.DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('public.seq_domains'),'Gestor Planeamiento FIP',CURRVAL('public.seq_domaincategory'));

--CREACION DE LAYERFAMILIES - solo una vez para todas las capas
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('public.seq_dictionary'),'es_ES','Familia Capas Gestor de Planeamiento - FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'ca_ES','[cat]Familia Capas Gestor de Planeamiento - FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'gl_ES','[gl]Familia Capas Gestor de Planeamiento - FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'va_ES','[va]Familia Capas Gestor de Planeamiento - FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'eu_ES','[eu]Familia Capas Gestor de Planeamiento - FIP');
INSERT INTO public.LAYERFAMILIES(id_layerfamily,id_name,id_description) values (nextval('public.seq_layerfamilies'),currval('public.seq_dictionary'),currval('public.seq_dictionary') );

--CREACION DE MAPS
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('public.seq_dictionary'),'es_ES','Map Gestor Planeamiento FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'ca_ES','[cat]Map Gestor Planeamiento FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'gl_ES','[gl]Map Gestor Planeamiento FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'va_ES','[va]Map Gestor Planeamiento FIP');
INSERT INTO public.DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('public.seq_dictionary'),'eu_ES','[eu]Map Gestor Planeamiento FIP');
INSERT INTO public.MAPS(ID_MAP,ID_NAME,XML,ID_ENTIDAD) VALUES(151,CURRVAL('public.seq_dictionary'),'<?xml version="1.0" encoding="UTF-8"?><mapDescriptor><description>Mapa para Gestor FIP</description><mapUnits></mapUnits><mapScale></mapScale><mapProjection>UTM 30N ETRS89</mapProjection><mapName>Gestion de Planeamiento</mapName></mapDescriptor>',0);

insert into public.maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (151,currval('public.seq_layerfamilies'),0,0);



CREATE TABLE "public"."planeamiento_acciones_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_acciones_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_acciones_gen_spat_idx" ON "public"."planeamiento_acciones_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_afecciones_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_afecciones_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_afecciones_gen_spat_idx" ON "public"."planeamiento_afecciones_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_alineacion_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_alineacion_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_alineacion_gen_spat_idx" ON "public"."planeamiento_alineacion_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_ambito_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_ambito_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_ambito_gen_spat_idx" ON "public"."planeamiento_ambito_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_categoria_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_categoria_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_categoria_gen_spat_idx" ON "public"."planeamiento_categoria_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_clasificacion_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_clasificacion_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_clasificacion_gen_spat_idx" ON "public"."planeamiento_clasificacion_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_desarrollo_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_desarrollo_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_desarrollo_gen_spat_idx" ON "public"."planeamiento_desarrollo_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_equidistribucion_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_equidistribucion_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_equidistribucion_gen_spat_idx" ON "public"."planeamiento_equidistribucion_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_gestion_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_gestion_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_gestion_gen_spat_idx" ON "public"."planeamiento_gestion_gen"
USING gist ("GEOMETRY");



CREATE TABLE "public"."planeamiento_otras_indicaciones_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_otras_indicaciones_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_otras_indicaciones_gen_spat_idx" ON "public"."planeamiento_otras_indicaciones_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_proteccion_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_proteccion_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_proteccion_gen_spat_idx" ON "public"."planeamiento_proteccion_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_sistemas_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_sistemas_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_sistemas_gen_spat_idx" ON "public"."planeamiento_sistemas_gen"
USING gist ("GEOMETRY");


CREATE TABLE "public"."planeamiento_zona_gen" (
  "id" NUMERIC(8,0) NOT NULL,
  "GEOMETRY" "public"."geometry",
  "clave" VARCHAR(255),
  "codigo" VARCHAR(10),
  "etiqueta" VARCHAR(255),
  "nombre" VARCHAR(255),
  "grupoaplicacion"  VARCHAR(255),
  "id_municipio" NUMERIC(5,0),
  CONSTRAINT "planeamiento_zona_gen_pk" PRIMARY KEY("id")
) WITH OIDS;

CREATE INDEX "planeamiento_zona_gen_spat_idx" ON "public"."planeamiento_zona_gen"
USING gist ("GEOMETRY");


-- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_acciones_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  
-- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_afecciones_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_alineacion_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_ambito_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_categoria_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_clasificacion_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_desarrollo_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_equidistribucion_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_gestion_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_proteccion_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_sistemas_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
  
  -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_zona_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

   -- Creacion de la secuencia
CREATE SEQUENCE public.seq_planeamiento_otras_indicaciones_gen
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


------------------------------------------------------
-- INSERCION RELACION MUNCIPIO - AMBITO LOURED -------
------------------------------------------------------
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40001,'000053');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9001,'000058');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26001,'000059');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20001,'000060');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30001,'000062');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50001,'000063');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15001,'000070');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42001,'000071');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44002,'000072');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25001,'000073');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2001,'000074');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31005,'000076');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10002,'000077');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49002,'000078');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34003,'000080');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22001,'000081');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22002,'000082');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4001,'000083');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19002,'000084');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31006,'000085');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8001,'000086');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4002,'000087');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37001,'000088');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24001,'000090');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10003,'000091');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6001,'000093');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10004,'000094');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10005,'000095');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50002,'000096');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6002,'000097');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47001,'000099');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14001,'000100');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5001,'000101');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38001,'000102');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46001,'000103');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19003,'000105');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4003,'000107');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9003,'000109');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42003,'000111');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40003,'000112');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3001,'000113');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20002,'000114');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35001,'000115');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37002,'000116');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25002,'000117');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36020,'000118');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26002,'000120');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3002,'000121');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42004,'000123');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3003,'000124');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41001,'000126');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47002,'000129');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44004,'000131');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13002,'000132');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22004,'000133');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40004,'000134');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9007,'000135');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47003,'000137');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44005,'000141');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8002,'000140');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30003,'000143');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35002,'000145');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17001,'000146');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46004,'000147');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38002,'000148');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10006,'000149');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37003,'000150');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37004,'000151');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20016,'000153');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31009,'000154');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46042,'000155');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8014,'000156');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17002,'000158');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3004,'000159');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22006,'000163');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25038,'000164');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20003,'000165');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28002,'000166');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26004,'000167');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48911,'000168');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47004,'000172');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7002,'000174');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29001,'000177');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45002,'000179');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28003,'000180');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18002,'000181');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13003,'000183');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19004,'000184');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6004,'000187');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37007,'000191');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50009,'000192');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34005,'000190');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19005,'000194');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2002,'000197');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44007,'000198');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2003,'000202');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34006,'000199');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37008,'000200');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37009,'000201');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46006,'000204');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41003,'000205');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46007,'000206');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13004,'000208');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16004,'000209');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46008,'000210');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46009,'000211');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46010,'000212');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22007,'000213');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44008,'000216');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16005,'000214');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19006,'000215');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22008,'000217');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4004,'000218');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23001,'000219');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19007,'000221');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45003,'000223');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2004,'000224');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3005,'000226');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22009,'000227');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26005,'000228');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16006,'000229');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19008,'000230');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44010,'000231');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26006,'000236');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50010,'000237');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22011,'000238');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22012,'000239');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22013,'000240');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25008,'000241');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50011,'000242');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9009,'000244');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43002,'000245');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20004,'000247');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4005,'000249');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18003,'000250');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17004,'000252');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46012,'000253');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46013,'000254');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2005,'000255');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50012,'000256');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5005,'000257');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4006,'000258');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30004,'000259');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46014,'000261');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18006,'000263');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18007,'000264');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6006,'000265');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2006,'000268');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44011,'000269');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22016,'000285');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26007,'000286');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43004,'000287');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49003,'000293');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44013,'000294');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45005,'000295');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30005,'000290');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16009,'000292');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14003,'000296');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23003,'000300');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45006,'000301');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13006,'000306');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28006,'000307');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19009,'000308');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3007,'000309');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9010,'000310');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16011,'000311');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4007,'000312');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13007,'000313');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22017,'000314');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19010,'000315');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19011,'000317');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45007,'000316');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25012,'000320');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42006,'000322');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37012,'000323');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40005,'000324');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6007,'000325');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50015,'000326');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16012,'000327');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6008,'000328');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12005,'000330');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44014,'000332');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19013,'000333');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43005,'000334');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22018,'000336');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42007,'000337');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42008,'000338');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49004,'000339');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13008,'000340');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46018,'000341');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7003,'000342');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4009,'000345');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12006,'000346');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46021,'000348');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10011,'000358');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37013,'000359');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10012,'000352');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28008,'000353');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37015,'000354');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13009,'000355');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47006,'000350');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10013,'000351');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45008,'000356');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42009,'000361');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40006,'000362');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37016,'000363');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40007,'000364');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42011,'000367');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42012,'000368');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45009,'000370');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26008,'000371');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37017,'000372');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19015,'000373');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40009,'000374');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37018,'000375');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10014,'000376');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10015,'000379');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40010,'000380');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5007,'000378');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23004,'000381');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40012,'000357');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37019,'000382');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37020,'000383');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5008,'000384');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37021,'000385');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37022,'000386');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40013,'000387');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37023,'000388');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37024,'000389');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40014,'000390');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40015,'000396');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50016,'000393');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10016,'000397');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37026,'000395');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40016,'000400');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43006,'000401');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30006,'000402');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20005,'000403');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8003,'000406');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42015,'000407');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22019,'000408');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26009,'000409');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18011,'000411');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46022,'000412');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3010,'000413');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44016,'000415');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46024,'000418');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43008,'000419');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46025,'000420');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49005,'000421');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29003,'000422');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29004,'000423');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26011,'000424');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46026,'000425');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46023,'000429');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12007,'000431');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43009,'000432');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50019,'000433');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27002,'000434');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9011,'000435');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39001,'000436');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9907,'000437');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9012,'000438');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24002,'000440');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7004,'000441');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11003,'000443');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19016,'000444');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46028,'000445');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18012,'000446');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16013,'000447');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29005,'000448');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11004,'000450');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25015,'000452');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28009,'000453');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46030,'000454');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12008,'000455');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46031,'000456');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11005,'000457');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49006,'000458');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19017,'000459');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3012,'000460');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25016,'000461');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30007,'000462');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4010,'000464');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30008,'000468');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13010,'000469');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44017,'000475');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16014,'000476');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24003,'000480');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25017,'000481');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19019,'000483');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10018,'000484');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42016,'000485');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21002,'000486');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20006,'000488');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33001,'000489');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32001,'000490');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44021,'000491');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33002,'000492');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31012,'000494');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44022,'000495');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44023,'000496');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25019,'000497');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13012,'000501');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19020,'000502');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13013,'000503');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42017,'000504');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42018,'000505');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2009,'000506');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24004,'000507');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10019,'000508');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49007,'000509');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29010,'000511');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42019,'000512');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26012,'000513');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25020,'000515');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42021,'000518');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12010,'000519');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13014,'000520');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14004,'000521');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25021,'000524');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12011,'000526');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47008,'000527');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37027,'000528');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42022,'000525');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37028,'000529');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6010,'000530');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45011,'000531');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6011,'000532');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16016,'000534');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41010,'000535');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50021,'000538');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44018,'000545');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46034,'000547');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50023,'000549');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50024,'000550');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16018,'000553');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45012,'000551');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19022,'000552');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21004,'000554');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21005,'000555');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45013,'000557');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43011,'000558');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3016,'000559');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22022,'000562');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22023,'000563');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13016,'000565');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46035,'000566');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44019,'000567');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29012,'000570');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21006,'000572');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19024,'000573');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29013,'000574');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17006,'000575');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29014,'000576');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42023,'000577');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50026,'000578');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28010,'000579');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8004,'000580');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2010,'000582');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46036,'000584');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18904,'000585');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9013,'000595');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43012,'000596');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25024,'000594');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16019,'000597');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3018,'000598');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12012,'000601');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20906,'000602');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20007,'000603');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19027,'000604');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5012,'000606');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34009,'000607');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50027,'000608');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28011,'000609');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17007,'000610');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15002,'000611');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9016,'000615');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20008,'000616');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33003,'000617');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32002,'000618');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48003,'000619');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48004,'000620');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43014,'000622');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34010,'000623');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39002,'000624');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1002,'000625');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34011,'000626');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47009,'000627');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40017,'000629');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37029,'000630');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28012,'000631');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13017,'000632');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46038,'000635');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20009,'000636');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44025,'000637');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31015,'000638');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7005,'000639');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40018,'000668');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50028,'000641');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25027,'000643');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26013,'000645');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26014,'000647');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19032,'000648');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9017,'000649');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39003,'000650');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46039,'000652');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20010,'000653');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14006,'000670');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31018,'000671');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45014,'000672');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37030,'000673');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19033,'000654');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19034,'000655');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31016,'000657');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4016,'000658');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27003,'000659');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46040,'000660');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29015,'000661');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35003,'000662');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34012,'000664');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20011,'000666');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31019,'000674');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37031,'000675');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48005,'000676');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21007,'000677');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38004,'000678');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41011,'000680');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40019,'000681');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31020,'000682');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31025,'000683');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20012,'000684');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1003,'000685');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31021,'000686');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9018,'000688');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50031,'000689');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9019,'000691');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16020,'000692');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15003,'000693');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31023,'000694');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28013,'000695');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31024,'000696');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31022,'000697');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48006,'000698');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12013,'000700');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19036,'000699');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37032,'000701');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31026,'000702');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46041,'000721');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9020,'000722');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9021,'000723');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9022,'000724');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25029,'000726');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19038,'000727');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31027,'000728');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36001,'000729');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4017,'000730');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16905,'000734');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37033,'000736');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49010,'000737');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48008,'000738');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30009,'000739');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29016,'000740');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29017,'000741');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34015,'000744');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40020,'000745');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9023,'000746');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11006,'000749');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49011,'000750');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16022,'000751');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44026,'000752');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29018,'000753');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50033,'000754');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48093,'000756');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31030,'000757');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29019,'000760');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39004,'000761');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18020,'000764');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13018,'000762');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5014,'000763');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42026,'000765');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9024,'000766');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8006,'000768');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8007,'000769');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26015,'000770');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26016,'000771');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15004,'000772');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12014,'000773');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31031,'000774');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20013,'000775');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5015,'000776');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40021,'000777');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13019,'000780');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13020,'000781');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24007,'000783');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22036,'000785');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19039,'000786');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17010,'000787');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12015,'000788');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44028,'000790');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8009,'000792');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39005,'000794');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31032,'000795');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22037,'000796');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16024,'000797');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49013,'000798');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31033,'000799');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7901,'000800');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31034,'000801');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38005,'000802');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9025,'001399');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44029,'001400');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50034,'001401');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23006,'001402');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23007,'001403');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19040,'001405');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31035,'001406');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37035,'001408');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18021,'001409');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40022,'001411');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4018,'001412');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19041,'001413');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26017,'001414');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26018,'001415');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43018,'001416');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39006,'001418');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21008,'001419');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38006,'001420');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49014,'001421');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23008,'001422');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49015,'001423');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1037,'001424');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16025,'001425');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48009,'001426');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9027,'001429');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1008,'001430');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35004,'001431');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39007,'001432');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25031,'001433');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29020,'001434');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48010,'001435');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48011,'001436');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13021,'001437');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10021,'001440');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19042,'001441');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10022,'001445');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31037,'001447');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31038,'001451');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12016,'001452');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31039,'001453');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15005,'001455');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35005,'001456');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25033,'001458');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25034,'001459');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50035,'001460');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1004,'001461');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35006,'001462');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25036,'000704');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49016,'000705');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1009,'000706');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3019,'000707');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20014,'000708');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20903,'000709');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24008,'000711');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34017,'000712');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49017,'000713');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29021,'000714');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6013,'000715');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16026,'000717');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9029,'000719');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47011,'000720');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18022,'001311');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20015,'001312');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50037,'001314');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50038,'001315');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31040,'001316');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19044,'001317');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48091,'001318');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48070,'001321');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26020,'001323');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42028,'001324');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34018,'001326');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34019,'001327');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26021,'001328');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5017,'001329');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5018,'001330');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5019,'001334');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1010,'001340');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21010,'001341');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31041,'001342');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46043,'001343');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22039,'001344');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2011,'001346');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46044,'001349');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34020,'001350');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31042,'001351');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44031,'001352');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22041,'001354');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20017,'001355');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22042,'001356');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26022,'001359');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20018,'001360');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6014,'001361');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50039,'001362');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31043,'001364');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19046,'001365');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37038,'001367');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4019,'001368');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6015,'001369');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8015,'001370');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41014,'001374');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50040,'001375');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14007,'001377');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23009,'001378');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19047,'001384');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22044,'001386');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36003,'001387');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25039,'001388');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48012,'001390');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2012,'001392');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24009,'001394');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22045,'001396');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27004,'001397');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20904,'001233');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13022,'001235');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22046,'001236');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48090,'001237');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3020,'001238');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2013,'001239');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8018,'001240');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32005,'001242');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7007,'001249');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26024,'001250');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32006,'001244');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1011,'001254');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23011,'001255');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32007,'001256');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26025,'001259');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19048,'001260');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9035,'001261');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19049,'001262');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9036,'001263');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17015,'001247');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16027,'001265');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27901,'001267');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31901,'001268');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42029,'001269');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37040,'001272');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9037,'001273');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9038,'001274');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9039,'001275');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37041,'001276');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31046,'001277');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22048,'001278');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40025,'001284');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22050,'001286');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42030,'001287');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6016,'001289');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8019,'001290');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37042,'001294');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47013,'001296');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49019,'001297');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45018,'001298');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42031,'001301');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50044,'001302');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39011,'001303');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31047,'001305');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31048,'001306');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24011,'001307');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38007,'001308');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12020,'001310');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44035,'001149');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10025,'001151');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2015,'001152');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27005,'001153');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48014,'001154');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19050,'001156');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9044,'001159');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36002,'001161');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37044,'001163');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1013,'001166');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46046,'001167');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46045,'001168');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40026,'001170');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48015,'001171');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9045,'001173');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9046,'001175');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16030,'001176');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25044,'001177');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43022,'001179');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6017,'001180');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28017,'001181');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25045,'001182');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4021,'001184');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42032,'001185');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42033,'001186');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18023,'001187');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44036,'001189');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32010,'001190');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16031,'001191');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32011,'001192');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21011,'001193');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20019,'001197');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18024,'001194');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18025,'001195');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23012,'001196');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5024,'001198');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5025,'001199');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44037,'001200');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28018,'001203');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47015,'001204');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48092,'001206');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27007,'001209');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8020,'001210');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17013,'001211');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31051,'001213');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4023,'001214');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20020,'001215');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20021,'001220');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9047,'001221');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50045,'001222');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37047,'001223');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25046,'001225');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25170,'001227');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43023,'001231');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44039,'001068');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8021,'001069');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25050,'001070');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43024,'001073');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25051,'001074');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14009,'001076');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16033,'001078');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34031,'001079');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33005,'001081');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28019,'001083');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16034,'001084');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9048,'001085');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22052,'001086');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49020,'001087');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24014,'001090');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22053,'001091');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29022,'001093');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47016,'001094');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12024,'001095');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12025,'001096');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46051,'001098');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4024,'001099');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29026,'001106');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18029,'001107');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29027,'001109');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11009,'001110');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12026,'001113');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3022,'001114');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22054,'001115');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23016,'001116');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49021,'001118');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24015,'001119');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46052,'001120');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49022,'001121');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46053,'001122');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3023,'001123');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3025,'001126');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3026,'001127');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46056,'001131');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3029,'001134');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46057,'001135');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3030,'001136');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3031,'001137');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30010,'001138');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43025,'001142');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3032,'001143');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3033,'001144');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3035,'001148');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3036,'000986');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3037,'000987');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3038,'000988');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3040,'000990');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46063,'000991');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46064,'000992');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46065,'000993');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46068,'000996');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3041,'000997');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43026,'000998');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46069,'000999');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3042,'001000');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4026,'001001');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12029,'001003');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10027,'001004');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6018,'001005');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4028,'001006');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24016,'001007');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48016,'001008');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1014,'001009');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22055,'001012');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9050,'001013');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31053,'001014');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26027,'001015');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47017,'001016');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47018,'001017');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40028,'001019');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5026,'001020');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24018,'001022');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40029,'001023');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50047,'001024');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8022,'001025');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20074,'001026');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26028,'001027');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26029,'001028');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44040,'001029');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15008,'001030');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4029,'001032');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6019,'001034');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42035,'001035');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24019,'001036');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9051,'001037');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37049,'001038');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48017,'001039');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49023,'001040');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40030,'001041');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1016,'001042');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19051,'001043');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5029,'001045');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48018,'001046');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48019,'001049');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20023,'001050');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21012,'001051');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37050,'001052');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37051,'001053');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10028,'001054');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5030,'001055');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47019,'001056');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50048,'001057');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10029,'001060');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9052,'001061');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28020,'001062');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34032,'001063');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35007,'001066');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15009,'001067');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31055,'000903');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17021,'000907');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26030,'000908');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44041,'000909');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3043,'000910');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46071,'000911');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20024,'000912');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50901,'000913');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22057,'000914');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2016,'000915');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6020,'000916');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22058,'000917');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22059,'000918');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3044,'000919');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8023,'000920');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50050,'000921');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48020,'000922');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33006,'000923');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22060,'000924');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25055,'000927');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22062,'000929');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50052,'000934');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17234,'000935');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31056,'000936');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42036,'000937');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30011,'000938');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43029,'000939');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44042,'000940');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17023,'000942');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5034,'000944');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5035,'000945');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22064,'000947');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44043,'000948');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42037,'000949');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37052,'000950');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34033,'000951');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34034,'000954');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28022,'000955');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34035,'000953');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33007,'000956');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26031,'000958');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47020,'000959');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46072,'000962');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40032,'000963');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47021,'000964');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47022,'000965');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6021,'000968');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47023,'000969');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37055,'000970');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10030,'000973');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5037,'000974');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15010,'000975');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15011,'000976');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13023,'000978');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47024,'000979');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46073,'000980');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21013,'000982');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22066,'000984');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3045,'000985');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17024,'000816');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22067,'000817');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24021,'000825');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21014,'000818');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43030,'000819');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2018,'000820');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16036,'000821');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5038,'000822');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50053,'000827');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22068,'000828');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50054,'000829');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17025,'000831');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50055,'000836');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42038,'000837');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41017,'000838');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11010,'000839');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42039,'000840');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45021,'000841');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24022,'000844');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12031,'000845');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43032,'000848');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43033,'000849');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10031,'000850');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50056,'000851');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25056,'000856');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5039,'000858');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47025,'000860');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34036,'000861');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28024,'000862');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9055,'000863');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13024,'000864');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24023,'000865');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28025,'000867');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17027,'000868');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38008,'000870');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38009,'000871');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41018,'000869');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49026,'000873');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40033,'000874');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26032,'000875');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19053,'000876');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49027,'000877');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49028,'000878');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26033,'000880');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37058,'000879');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26034,'000882');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44045,'000884');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22069,'000885');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10032,'000886');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28026,'000889');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17028,'000890');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42041,'000891');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50058,'000892');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16038,'000894');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19054,'000895');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44046,'002001');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16040,'000897');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45022,'000899');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37060,'000900');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38010,'000902');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34037,'000901');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36004,'002002');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46076,'002004');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9057,'002005');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42042,'002007');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28027,'002008');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14012,'002009');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19055,'002010');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50059,'002011');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5040,'002012');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50060,'002013');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30012,'002014');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9058,'002015');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46077,'002017');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31057,'002018');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7010,'002016');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50061,'002021');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49029,'002022');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5041,'002027');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9059,'002028');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31059,'002030');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41019,'002031');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6022,'002033');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45023,'002032');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46078,'002034');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31060,'002035');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12032,'002037');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3046,'002039');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19057,'002041');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28028,'002042');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47026,'002044');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34038,'002045');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49030,'002046');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9060,'002050');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48021,'002051');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40034,'002054');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25060,'002056');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15015,'002057');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50064,'002065');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45025,'000803');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10033,'000807');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40035,'000804');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49031,'000805');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45026,'000806');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24027,'000808');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17031,'002058');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9061,'000809');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31062,'002061');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28029,'002062');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19058,'002063');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10034,'000814');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37062,'000815');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6023,'000811');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37065,'000812');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6024,'000813');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45027,'001924');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13025,'001925');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13026,'001926');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5042,'001927');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5043,'001929');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5044,'001930');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21015,'001931');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40036,'001938');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10035,'001939');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9063,'001940');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5045,'001941');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50065,'001943');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14013,'001944');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43036,'001946');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44048,'001945');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33008,'001948');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33009,'001949');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31063,'001950');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42044,'001951');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42045,'001952');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8029,'001954');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37067,'001956');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10036,'001957');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47029,'001958');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24029,'001960');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37068,'001961');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8030,'001962');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24030,'001964');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10038,'001966');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28031,'001969');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31064,'001973');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50066,'001974');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21016,'001976');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44049,'001978');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8031,'001979');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43037,'001980');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26036,'001981');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34041,'001983');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44050,'001984');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6025,'001985');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21017,'001987');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44051,'001986');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30013,'001988');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42046,'001989');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50067,'001990');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50068,'001991');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50069,'001992');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36005,'001993');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22072,'001994');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8034,'001995');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17033,'001996');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8033,'001997');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8035,'001999');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45028,'001851');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9064,'001852');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45029,'001853');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18037,'001854');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8037,'001856');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46079,'001857');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50070,'001861');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44052,'001862');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17034,'001863');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8036,'001864');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3047,'001865');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42048,'001866');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37069,'001867');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37070,'001868');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13027,'001872');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37072,'001873');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24031,'001877');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34042,'001874');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37073,'001876');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10040,'001878');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6027,'001879');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49032,'001880');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39015,'001881');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44053,'001882');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45031,'001884');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44054,'001885');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45032,'001886');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39016,'001887');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44055,'001888');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15016,'001889');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43903,'001890');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28032,'001891');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49033,'001892');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41021,'001893');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36006,'001894');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23018,'001895');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15017,'001896');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43038,'001897');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10041,'001898');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44056,'001899');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6028,'001902');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7012,'001903');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47030,'001904');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24032,'001905');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17037,'001907');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1017,'001909');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16042,'001912');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9065,'001914');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23019,'001915');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37074,'001916');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10042,'001917');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19059,'001918');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6029,'001920');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19060,'001921');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29032,'001922');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16043,'001923');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16044,'001773');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8039,'001774');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17038,'001776');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22074,'001777');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13028,'001778');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3051,'001779');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40039,'001781');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24033,'001782');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36007,'001783');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9066,'001787');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10043,'001784');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24034,'001788');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28033,'001785');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47032,'001791');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46080,'001793');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7013,'001794');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17039,'001798');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45034,'001800');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3052,'001691');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44060,'001692');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13029,'001693');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16046,'001695');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16047,'001698');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41901,'001696');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44062,'001697');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16045,'001803');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47033,'001802');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5046,'001804');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26038,'001805');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46081,'001806');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42050,'001699');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16048,'001700');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10044,'001701');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26040,'001703');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10045,'001705');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16050,'001707');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16051,'001708');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33010,'001807');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22077,'001808');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38011,'001809');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37078,'001810');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5047,'001811');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42049,'001812');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25063,'001814');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23020,'001815');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28034,'001816');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8040,'001818');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16052,'001709');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14014,'001710');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29035,'001711');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12036,'001820');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22078,'001821');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36008,'001822');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9067,'001825');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18039,'001826');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37079,'001827');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29033,'001828');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29034,'001829');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47034,'001830');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49034,'001713');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19066,'001714');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44063,'001715');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16053,'001716');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49035,'001717');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8041,'001833');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19064,'001835');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9068,'001836');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37080,'001837');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37081,'001838');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40040,'001839');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17041,'001840');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19065,'001841');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37082,'001842');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37083,'001843');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44059,'001844');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41023,'001845');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40041,'001846');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5048,'001847');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4031,'001848');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8043,'001849');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43039,'001718');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31065,'001719');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7014,'001720');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22079,'001721');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22080,'001723');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8044,'001724');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18042,'001725');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6030,'001726');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34045,'001727');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17042,'001728');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8045,'001729');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28035,'001732');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42051,'001731');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42052,'001733');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13030,'001734');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18043,'001735');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30015,'001736');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33013,'001737');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9070,'001738');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49036,'001739');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10046,'001740');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37085,'001741');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32018,'001742');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27009,'001744');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15019,'001746');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49037,'001747');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4032,'001748');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40043,'001750');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23021,'001751');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10047,'001752');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14015,'001753');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46083,'001754');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31067,'001756');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9071,'001757');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9072,'001758');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8046,'001762');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14016,'001765');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9073,'001766');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9074,'001767');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16056,'001764');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5049,'001768');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34046,'001769');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45035,'001771');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8047,'001772');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50072,'001617');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50073,'001618');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15901,'001619');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46085,'001620');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8048,'001622');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45036,'001623');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41024,'001625');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6031,'001626');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15020,'001627');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47035,'001629');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37086,'001631');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24038,'001633');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15021,'001634');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45038,'001635');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48022,'001636');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37087,'001637');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10048,'001640');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16057,'001642');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42053,'001643');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16058,'001644');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42054,'001645');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29036,'001646');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33014,'001647');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9076,'001649');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45039,'001650');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24039,'001655');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13032,'001656');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24040,'001657');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30016,'001658');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29037,'001659');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21021,'001661');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32020,'001662');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39018,'001663');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24041,'001664');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29039,'001666');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19070,'001665');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37089,'001667');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26042,'001668');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29040,'001673');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10050,'001672');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42055,'001674');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29041,'001675');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10051,'001676');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41026,'001677');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45041,'001678');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28036,'001679');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46087,'001680');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46088,'001681');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45042,'001558');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10052,'001683');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6033,'001685');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16061,'001686');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16062,'001687');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16063,'001688');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16064,'001689');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10054,'001554');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10055,'001556');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16065,'001548');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5052,'001557');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10057,'001550');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6034,'001551');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19073,'001552');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2023,'001553');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49039,'001560');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16066,'001562');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5053,'001563');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10058,'001565');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5054,'001566');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22081,'001567');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9077,'001568');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9078,'001569');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31068,'001570');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5055,'001574');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10059,'001575');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37091,'001576');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46089,'001577');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40045,'001578');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33015,'001579');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50074,'001580');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19074,'001581');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8049,'001583');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3053,'001584');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10060,'001585');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26043,'001586');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39019,'001587');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21022,'001588');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44066,'001600');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22085,'001601');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9079,'001607');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37092,'001608');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37185,'001609');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5056,'001610');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23025,'001611');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11013,'001613');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19076,'001614');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25064,'001615');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8050,'001484');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8051,'001485');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13033,'001483');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8053,'001486');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8054,'001487');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8055,'001488');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25067,'001489');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12037,'001603');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3054,'001604');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8056,'001490');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25904,'001606');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8058,'001491');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17046,'001492');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8059,'001494');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12038,'001495');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8062,'001497');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25068,'001498');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12039,'001499');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44071,'001507');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43042,'001511');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44067,'001514');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46092,'001516');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22086,'001517');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22087,'001518');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6035,'001521');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41027,'001522');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9082,'001523');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9083,'001519');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34048,'001520');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19078,'001525');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50078,'001527');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22088,'001528');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41029,'001530');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41030,'001531');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16068,'001533');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42058,'001536');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16070,'001537');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45043,'001538');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16072,'001539');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12041,'001542');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22089,'001544');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19079,'001546');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42057,'002645');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37097,'002646');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32022,'002649');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32021,'002650');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18046,'002651');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24043,'002652');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34050,'002653');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47038,'002654');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49040,'002655');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9084,'002656');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24044,'002657');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9085,'002658');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9086,'002662');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34051,'002659');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9088,'002660');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34052,'002661');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47039,'002665');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47040,'002671');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32023,'002666');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24047,'002673');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4033,'002667');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40047,'002668');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27010,'002669');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47041,'002674');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49041,'002675');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9091,'002676');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40048,'002677');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47042,'002678');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34053,'002679');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47043,'002680');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49042,'002681');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47044,'002682');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47045,'002683');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24049,'002684');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33017,'002685');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47046,'002686');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40049,'002687');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39020,'002690');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27011,'002691');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49043,'002692');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47047,'002693');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26044,'002694');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6036,'002695');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46093,'002696');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46094,'002697');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36010,'002700');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3055,'001465');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2025,'001466');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46095,'001467');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12043,'001468');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25071,'001469');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9093,'001470');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45045,'001471');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23027,'001472');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41032,'001473');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23028,'001474');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49044,'001475');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24051,'001476');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24052,'001477');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45046,'001478');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9094,'001479');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5057,'001480');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15022,'002572');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10062,'002573');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40052,'002574');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45047,'002575');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44074,'002576');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15023,'002577');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9095,'002580');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44075,'002581');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32024,'002582');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44076,'002583');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26045,'002585');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19080,'002587');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19081,'002588');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18047,'002589');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26046,'002590');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28037,'002591');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2026,'002592');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32025,'002593');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8067,'002594');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19082,'002595');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42059,'002596');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37098,'002597');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5058,'002598');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15024,'002600');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28038,'002601');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8268,'002602');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36011,'002605');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15025,'002606');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37099,'002607');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49046,'002608');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49047,'002609');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37100,'002610');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10063,'002611');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40053,'002612');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40054,'002613');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49048,'002615');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37101,'002617');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27012,'002622');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34055,'002623');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25072,'002625');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28039,'002626');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50079,'002627');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16073,'002630');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12044,'002631');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45049,'002628');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34056,'002629');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50080,'002633');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47049,'002636');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27013,'002637');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37103,'002638');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15026,'002639');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50081,'002640');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34057,'002643');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22094,'002489');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32029,'002491');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27016,'002492');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18059,'002495');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19103,'002496');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6042,'002497');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46107,'002498');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46106,'002499');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19104,'002500');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46108,'002501');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4036,'002502');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12052,'002503');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46109,'002504');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11015,'002506');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23029,'002507');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12053,'002508');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19105,'002513');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18061,'002514');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22096,'002515');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11016,'002518');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50092,'002519');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4037,'002520');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46111,'002521');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50093,'002522');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12055,'002523');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24065,'002525');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45056,'002526');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21030,'002527');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45057,'002528');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46112,'002529');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18062,'002531');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9101,'002532');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42061,'002534');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28040,'002535');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19086,'002539');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47050,'002540');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42062,'002541');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47051,'002542');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42063,'002543');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26049,'002544');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18048,'002545');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9102,'002547');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10064,'002548');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37104,'002549');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9103,'002550');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9104,'002551');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24054,'002554');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24055,'002555');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50082,'002556');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50083,'002557');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19087,'002558');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12045,'002559');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12046,'002562');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31074,'002563');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42064,'002564');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31075,'002565');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19088,'002566');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45050,'002567');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9105,'002568');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19089,'002569');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26050,'002570');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5060,'002409');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34059,'002410');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17051,'002411');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24056,'002413');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13034,'002414');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7015,'002416');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25074,'002417');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31076,'002418');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25075,'002420');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26051,'002421');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33018,'002422');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45051,'002424');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28041,'002425');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19090,'002426');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45052,'002427');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34060,'002428');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40056,'002429');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49050,'002430');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40057,'002431');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37108,'002432');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3056,'002433');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50085,'002434');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40058,'002436');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50086,'002437');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46097,'002439');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47053,'002440');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47054,'002441');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19091,'002442');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9108,'002443');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18049,'002444');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18050,'002445');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19092,'002446');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17054,'002450');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32026,'002451');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39023,'002453');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10065,'002455');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5062,'002456');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40059,'002458');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28046,'002459');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28047,'002460');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34061,'002461');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43045,'002463');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8070,'002464');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29043,'002465');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28042,'002468');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37109,'002466');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28043,'002467');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28044,'002470');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28045,'002469');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18051,'002471');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17055,'002472');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33019,'002473');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22090,'002474');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29044,'002476');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39024,'002477');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40060,'002480');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9109,'002481');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19095,'002482');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19096,'002483');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43046,'002484');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3057,'002485');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24057,'002486');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34062,'002487');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19097,'002330');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11014,'002331');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14020,'002332');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10066,'002333');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7016,'002334');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41033,'002336');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5064,'002337');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45053,'002338');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50087,'002339');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9110,'002340');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49052,'002341');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19098,'002342');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8071,'002343');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46098,'002345');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8072,'002346');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25078,'002349');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47055,'002350');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37110,'002354');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34063,'002355');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19099,'002357');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31077,'002358');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26053,'002359');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49053,'002360');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10067,'002362');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41035,'002364');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15029,'002365');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26054,'002366');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43049,'002369');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28048,'002372');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26055,'002373');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45054,'002374');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13035,'002376');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49054,'002377');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47056,'002380');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2027,'002381');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6040,'002383');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32027,'002385');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21025,'002386');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21026,'002387');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31078,'002388');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12048,'002390');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18053,'002391');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29046,'002392');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18054,'002394');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9112,'002398');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33020,'002399');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39026,'002400');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44085,'002401');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42068,'002402');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28049,'002403');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27015,'002404');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7017,'002405');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12049,'002406');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50088,'002407');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49055,'002253');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46100,'002254');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2028,'002255');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36012,'002256');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42069,'002257');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9113,'002258');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3058,'002261');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40902,'002263');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36014,'002264');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43050,'002265');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5065,'002268');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44086,'002269');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3059,'002270');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6041,'002272');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10068,'002276');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24061,'002277');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32028,'002278');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1020,'002279');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50089,'002280');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50090,'002283');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8074,'002284');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25079,'002285');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42070,'002286');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34066,'002287');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24062,'002288');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24063,'002289');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47057,'002290');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40062,'002291');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9114,'002293');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49056,'002294');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24064,'002295');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44089,'002296');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49057,'002297');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9115,'002298');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42071,'002300');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33021,'002303');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49059,'002304');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16078,'002306');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47058,'002307');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45055,'002309');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42073,'002312');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16079,'002314');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29047,'002315');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4035,'002320');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29048,'002321');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18912,'002322');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5066,'002323');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40905,'002317');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9119,'002318');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29049,'002319');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44094,'002324');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12051,'002325');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46105,'002328');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15031,'002329');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21027,'002169');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21029,'002171');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43051,'002172');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36015,'002173');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15032,'002175');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28053,'002178');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13039,'002179');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17060,'002182');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50094,'002183');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26057,'002184');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18063,'002186');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17061,'002187');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3061,'002188');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3062,'002189');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20029,'002190');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33022,'002191');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34067,'002192');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34068,'002193');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10070,'002196');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43901,'002197');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3063,'002198');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48901,'002199');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31079,'002201');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24066,'002202');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42076,'002205');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31080,'002206');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5903,'002207');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18067,'002208');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48026,'002210');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37115,'002211');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15033,'002212');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3064,'002214');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46114,'002215');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6043,'002218');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40069,'002221');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37116,'002226');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5069,'002222');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5070,'002224');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46115,'002228');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45059,'002231');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41038,'002229');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8075,'002232');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14023,'002230');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19107,'002234');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34069,'002236');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43053,'002237');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48027,'002239');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40070,'002242');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42078,'002243');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48028,'002244');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31083,'002245');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20030,'002248');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7026,'002249');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50095,'002250');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37118,'002251');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44096,'002085');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48031,'002086');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2030,'002089');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1022,'002090');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3066,'002091');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20031,'002092');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20033,'002093');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20032,'002094');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31087,'002095');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10072,'002097');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48032,'002098');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1023,'002099');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19109,'002100');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50096,'002101');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46117,'002102');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50098,'002105');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37120,'002104');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40071,'002106');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37121,'002107');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37122,'002108');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47060,'002109');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21031,'002111');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37123,'002112');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14024,'002110');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24067,'002113');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40072,'002114');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26058,'002116');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37124,'002117');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46118,'002119');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4041,'002121');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49061,'002123');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39028,'002124');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26059,'002125');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32030,'002126');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50099,'002128');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48902,'002129');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31090,'002130');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48033,'002132');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50100,'002134');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48034,'002135');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20066,'002136');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48079,'002137');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31092,'002138');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45060,'002139');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21032,'002140');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39029,'002142');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45061,'002143');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40073,'002144');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45062,'002145');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19110,'002146');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23031,'002147');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40074,'002148');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19111,'002149');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24069,'002151');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40075,'002152');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19112,'002154');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7019,'002155');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44097,'002157');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44099,'002158');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10073,'002159');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37125,'002160');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32031,'002162');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47061,'002163');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20034,'002164');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31094,'002165');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12057,'002166');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37126,'002167');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49062,'003272');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12058,'003273');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6046,'003274');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6048,'003276');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6047,'003275');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8076,'003277');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37127,'003280');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42080,'003281');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14025,'003282');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11017,'003285');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14026,'003286');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17063,'003288');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37128,'003289');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34070,'003290');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9122,'003291');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19113,'003292');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9123,'003296');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5072,'003293');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9124,'003294');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34071,'003295');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45063,'003297');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40077,'003298');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19114,'003299');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8077,'003302');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17064,'003304');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7020,'003306');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25082,'003307');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31096,'003308');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45064,'003310');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22102,'003312');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22103,'003313');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25088,'003314');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31097,'003317');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7021,'003318');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42082,'003320');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29051,'003322');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44100,'003323');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46120,'003327');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26060,'003328');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28055,'003332');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46121,'002067');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31099,'002068');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31082,'002069');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31084,'002070');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31100,'002074');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31101,'002075');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26061,'002076');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20035,'002078');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31102,'002079');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31103,'002080');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50102,'002081');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24070,'002082');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3067,'002083');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22106,'002084');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31104,'003190');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43055,'003191');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3068,'003192');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22107,'003193');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12059,'003194');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49064,'003198');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50104,'003199');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25089,'003200');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38012,'003201');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46122,'003203');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46123,'003204');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7022,'003208');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4043,'003209');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15035,'003210');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6049,'003212');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49065,'003213');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13040,'003215');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49066,'003217');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49067,'003218');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49068,'003220');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44101,'003221');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15036,'003222');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17066,'003227');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43059,'003228');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12060,'003229');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49069,'003230');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50107,'003231');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4045,'003234');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4044,'003232');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3069,'003233');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35008,'003235');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22109,'003236');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15037,'003237');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31105,'003238');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43060,'003240');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5073,'003241');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8082,'003245');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46126,'003246');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24071,'003248');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27017,'003249');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8083,'003250');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47062,'003251');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50108,'003252');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47063,'003253');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26062,'003254');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25093,'003255');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18076,'003257');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8084,'003260');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17069,'003264');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19117,'003265');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13041,'003266');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17070,'003268');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17071,'003269');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31106,'003270');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47064,'003271');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9127,'003114');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5074,'003115');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22110,'003117');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26063,'003118');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25094,'003119');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17902,'003121');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12061,'003122');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36018,'003123');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37130,'003125');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7024,'003126');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3070,'003127');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44103,'003128');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7025,'003129');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17073,'003130');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46125,'003133');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44106,'003134');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30020,'003136');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48906,'003137');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27019,'003138');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44107,'003139');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15038,'003140');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37131,'003141');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22112,'003142');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23033,'003144');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34072,'003149');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6050,'003151');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43062,'003153');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18078,'003154');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16083,'003157');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16084,'003159');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5075,'003161');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28056,'003162');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37133,'003163');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10075,'003164');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9130,'003165');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9131,'003166');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40079,'003169');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42084,'003170');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40080,'003171');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49075,'003172');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49076,'003173');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24073,'003174');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9133,'003176');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49077,'003177');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28057,'003178');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47065,'003180');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49078,'003183');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29053,'003184');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27020,'003185');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38013,'003187');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40081,'003045');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19118,'003046');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13042,'003047');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38014,'003048');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50114,'003051');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44110,'003052');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29054,'003053');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28058,'003054');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6051,'003055');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13043,'003056');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26064,'003057');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47066,'003058');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45066,'003059');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2032,'003060');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23034,'003061');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2033,'003082');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2034,'003083');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42085,'003084');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9135,'003085');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42087,'003087');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6052,'003063');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6053,'003068');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6054,'003069');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16086,'003064');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29055,'003065');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40082,'003067');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13044,'003070');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40083,'003071');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40084,'003072');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5077,'003073');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28059,'003074');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47067,'003075');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49079,'003076');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37136,'003089');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21033,'003090');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19120,'003091');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14028,'003077');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49080,'003092');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12063,'003078');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19121,'003094');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16087,'003095');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16088,'003096');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37137,'003097');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9138,'003098');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42088,'003099');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19122,'003100');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42089,'003101');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19123,'003102');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9139,'003103');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9140,'003105');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19124,'003106');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14029,'003079');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47068,'003107');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40086,'003108');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40087,'003110');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42090,'003109');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40088,'003111');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37138,'003112');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46129,'003113');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16089,'002967');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5078,'002971');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24074,'002974');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50115,'002975');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50116,'002976');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42092,'002978');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34076,'002979');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37140,'002980');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49082,'002981');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44113,'002982');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34077,'002983');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49083,'002986');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40091,'002987');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44114,'002988');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9141,'002989');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49084,'002990');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18079,'003081');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40092,'002993');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28060,'002994');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23035,'002995');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16091,'002996');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25097,'002999');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31107,'003000');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31108,'003001');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20038,'003004');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12065,'003008');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20037,'003009');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19125,'003010');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37141,'003011');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28061,'003012');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31109,'003014');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21034,'003015');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9143,'003016');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48036,'003018');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48037,'003019');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49085,'003021');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18082,'003022');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26066,'003024');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37143,'003026');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37144,'003027');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10076,'003028');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40093,'003031');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5079,'003032');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47069,'003034');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49086,'003037');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5080,'003035');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8087,'003039');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26067,'003040');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31110,'003041');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50117,'003042');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50118,'003044');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44115,'002881');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19127,'002882');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48038,'002884');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49088,'002885');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43064,'002886');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16093,'002888');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38015,'002889');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31112,'002891');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48039,'002892');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6056,'002893');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43065,'002894');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10077,'002895');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37147,'002896');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37149,'002900');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31113,'002901');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44116,'002902');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28062,'002904');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5081,'002905');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10079,'002906');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10080,'002907');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28063,'002908');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10081,'002909');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6057,'002912');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31115,'002914');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42094,'002915');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17076,'002918');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17077,'002919');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10082,'002922');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4049,'002923');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28064,'002925');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16094,'002926');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19129,'002927');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10084,'002929');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3071,'002930');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48041,'002935');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46130,'002937');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25098,'002938');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5082,'002939');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37150,'002942');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12067,'002943');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8091,'002944');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50119,'002945');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41044,'002946');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49090,'002947');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5083,'002948');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29057,'002949');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31116,'002951');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17078,'002953');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41045,'002954');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47071,'002956');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45069,'002957');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48046,'002958');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46133,'002960');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28065,'002961');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20039,'002962');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48044,'002963');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5084,'002799');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41046,'002800');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46134,'002801');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26068,'002804');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41047,'002806');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43067,'002807');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8092,'002809');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8093,'002810');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18083,'002812');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43068,'002813');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46135,'002814');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46136,'002815');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50120,'002816');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31117,'002817');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42095,'002819');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2036,'002821');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37151,'002822');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37152,'002825');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32033,'002826');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36021,'002828');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31118,'002829');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18085,'002830');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18086,'002831');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24077,'002832');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48042,'002833');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24078,'002835');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3073,'002836');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48043,'002837');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42097,'002838');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5087,'002840');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50121,'002841');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24079,'002843');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33026,'002844');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16095,'002846');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16096,'002847');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24080,'002848');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40097,'002849');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18087,'002850');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38017,'002854');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33027,'002857');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8095,'002859');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49091,'002862');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3074,'002863');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6059,'002864');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8096,'002867');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49092,'002868');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25103,'002869');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25105,'002870');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25104,'002871');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43069,'002874');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22117,'002875');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11019,'002877');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44119,'002878');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9148,'002879');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34079,'002880');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9149,'002718');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50122,'002719');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19130,'002723');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44120,'002724');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41048,'002725');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3075,'002727');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28067,'002728');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13046,'002729');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10087,'002730');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28068,'002732');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46138,'002733');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37154,'002736');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8097,'002738');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18093,'002739');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17081,'002740');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46140,'002743');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3076,'002744');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34080,'002748');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6060,'002749');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29058,'002750');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49093,'002751');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34081,'002753');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48045,'002757');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31119,'002758');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37155,'002764');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10088,'002765');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10089,'002766');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10090,'002767');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37156,'002769');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41049,'002770');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17082,'002771');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31121,'002775');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5089,'002776');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25110,'002777');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27022,'002778');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25111,'002779');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48047,'002780');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9152,'002782');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8100,'002784');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39030,'002785');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24081,'002787');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9154,'002790');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26071,'002792');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9155,'002793');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39031,'002794');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16097,'002797');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19132,'002798');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26072,'003944');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13047,'003945');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10092,'003947');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37158,'003949');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37159,'003950');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38021,'003953');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49094,'003954');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20040,'003955');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5092,'003957');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20041,'003958');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41050,'003961');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6063,'003968');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50124,'003964');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34083,'003965');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42098,'003966');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34084,'003967');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5094,'003971');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10095,'003972');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45073,'003973');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19135,'003978');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10097,'003979');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23041,'003980');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5095,'003983');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6064,'003981');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21038,'003982');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6065,'003984');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6066,'003985');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6067,'003986');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12069,'003987');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2039,'003988');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46141,'003989');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19136,'003992');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10098,'003994');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21039,'003995');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23042,'003996');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21040,'002701');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37160,'002703');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44123,'002704');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42100,'002706');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14035,'002707');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6068,'002708');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45074,'002705');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13048,'002709');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19138,'002712');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10099,'002714');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19139,'002715');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20036,'002716');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16102,'003872');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40099,'003873');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40100,'003874');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45075,'003875');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40101,'003876');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9159,'003877');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16103,'003878');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9160,'003879');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16104,'003880');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19142,'003881');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34086,'003882');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9162,'003883');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9163,'003885');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9164,'003884');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28070,'003887');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5099,'003888');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13049,'003889');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37161,'003890');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16106,'003891');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37162,'003892');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28071,'003893');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19143,'003894');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45076,'003896');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26075,'003897');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26076,'003898');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6069,'003899');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14036,'003900');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26077,'003902');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34087,'003903');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9167,'003905');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23043,'003906');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26078,'003907');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43071,'003909');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9169,'003911');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24082,'003912');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17083,'003915');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2040,'003917');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9170,'003918');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5101,'003921');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28072,'003919');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5103,'003922');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10100,'003923');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5104,'003925');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5105,'003926');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5106,'003924');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22122,'003927');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31122,'003930');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45077,'003931');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23044,'003936');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21041,'003937');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16108,'003938');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37164,'003789');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9173,'003790');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16111,'003793');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9174,'003794');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19148,'003795');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22124,'003796');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23045,'003798');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22125,'003800');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16112,'003802');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19150,'003806');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9175,'003808');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19151,'003809');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28073,'003810');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29059,'003811');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9176,'003812');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5107,'003813');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34088,'003814');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10102,'003815');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31124,'003816');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20042,'003817');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50125,'003819');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9177,'003820');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3079,'003821');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33028,'003822');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22126,'003823');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9178,'003824');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23046,'003825');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38022,'003826');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20043,'003827');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31259,'003828');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26080,'003829');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9179,'003830');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9180,'003831');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48094,'003834');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8102,'003836');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29060,'003837');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24083,'003838');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20044,'003840');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22128,'003841');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19152,'003843');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33029,'003844');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4054,'003845');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33030,'003846');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45081,'003847');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18102,'003848');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50126,'003849');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31126,'003850');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7027,'003851');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35011,'003853');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16113,'003854');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31127,'003858');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15039,'003860');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37165,'003862');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19155,'003863');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20045,'003864');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1901,'003865');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20046,'003866');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1027,'003867');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31128,'003869');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9181,'003698');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47075,'003699');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21042,'003700');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48049,'003703');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50128,'003705');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34089,'003706');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9182,'003707');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18103,'003708');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20047,'003709');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37166,'003710');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40104,'003711');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31129,'003712');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31130,'003713');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48910,'003714');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25112,'003715');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25114,'003717');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31131,'003718');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31132,'003719');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24084,'003720');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31133,'003721');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18105,'003723');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29062,'003724');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23048,'003725');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48050,'003726');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44127,'003727');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23049,'003728');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21043,'003729');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22130,'003730');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3080,'003731');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19156,'003732');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17085,'003734');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46142,'003735');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49096,'003738');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23051,'003739');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50129,'003741');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46144,'003742');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10103,'003743');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9183,'003745');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9184,'003746');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10105,'003747');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10106,'003748');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50130,'003749');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44128,'003750');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22131,'003751');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44129,'003752');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31134,'003754');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18107,'003757');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11020,'003758');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18108,'003760');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6070,'003759');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10107,'003762');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18109,'003763');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23052,'003765');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11021,'003766');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19157,'003768');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24086,'003769');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8103,'003772');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44130,'003773');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44131,'003775');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40105,'003778');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40106,'003779');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29064,'003780');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30022,'003782');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18111,'003783');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5108,'003612');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25118,'003613');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9189,'003615');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9190,'003616');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31136,'003619');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49097,'003620');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18112,'003621');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37167,'003622');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48907,'003624');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40107,'003625');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1028,'003626');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22133,'003628');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10108,'003630');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45082,'003631');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34091,'003632');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50133,'003633');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44132,'003636');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24087,'003638');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26082,'003639');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40108,'003640');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47076,'003641');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16115,'003643');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24088,'003642');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16116,'003644');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37168,'003645');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26083,'003646');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31138,'003648');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22135,'003650');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22136,'003651');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31139,'003654');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22137,'003655');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1032,'003657');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16117,'003658');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48051,'003659');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5109,'003660');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42103,'003661');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50134,'003662');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47077,'003663');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33031,'003664');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40109,'003665');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34092,'003667');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18117,'003669');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31140,'003671');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5110,'003672');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44133,'003673');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22139,'003675');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1033,'003677');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15041,'003678');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26084,'003679');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39035,'003680');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32038,'003681');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4056,'003682');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48052,'003683');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31142,'003684');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31143,'003685');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20048,'003686');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37169,'003688');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23054,'003689');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20902,'003690');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22141,'003691');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22142,'003692');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22144,'003694');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40111,'003696');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4057,'003520');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48053,'003521');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33032,'003522');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15040,'003523');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50135,'003524');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45083,'003525');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32039,'003526');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20049,'003528');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31146,'003530');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41053,'003531');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50137,'003534');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16118,'003537');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19159,'003536');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37170,'003538');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26086,'003539');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34094,'003540');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37171,'003541');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16119,'003543');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31147,'003544');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31148,'003545');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20052,'003547');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1058,'003548');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20068,'003549');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48054,'003550');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32040,'003551');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31149,'003552');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26087,'003553');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48057,'003554');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48055,'003556');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48056,'003557');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33033,'003558');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31150,'003561');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21044,'003562');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31151,'003563');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25121,'003567');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31153,'003568');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2042,'003569');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50139,'003570');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1034,'003571');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48081,'003573');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20053,'003575');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2043,'003576');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30023,'003577');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44135,'003578');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42105,'003579');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39036,'003582');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45084,'003586');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39038,'003587');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23055,'003588');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21045,'003589');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44137,'003590');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25122,'003593');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50140,'003594');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20054,'003596');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25123,'003600');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25124,'003601');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17089,'003603');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24092,'003604');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17090,'003605');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17091,'003606');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33035,'003607');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46154,'003608');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33036,'003609');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9195,'003610');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47079,'003611');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25125,'003437');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6073,'003441');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6074,'003442');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17093,'003443');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25128,'003446');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25129,'003453');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46153,'003454');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1036,'003455');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46156,'003456');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43073,'003457');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17095,'003459');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7028,'003460');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46157,'003462');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7029,'003463');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7031,'003466');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46150,'003468');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22149,'003469');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32041,'003470');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50142,'003471');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32042,'003472');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18121,'003474');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31157,'003475');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28075,'003476');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26089,'003477');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48903,'003479');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18122,'003480');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34903,'003481');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34096,'003482');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45085,'003483');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47078,'003484');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50143,'003485');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23056,'003488');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22150,'003489');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41054,'003490');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19160,'003492');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30024,'003493');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46148,'003495');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49098,'003499');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49099,'003500');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46149,'003498');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10110,'003501');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22151,'003503');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44138,'003504');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15042,'003507');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28076,'003508');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28901,'003509');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4060,'003512');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14038,'003514');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12072,'003516');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21046,'003517');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50147,'003518');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13051,'003349');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24090,'003350');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45086,'003351');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12073,'003352');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49101,'003353');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39039,'003354');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50148,'003355');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50149,'003356');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46151,'003357');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46152,'003358');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27028,'003359');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18123,'003360');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31159,'003363');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37173,'003364');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26091,'003365');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50150,'003366');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50151,'003367');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19161,'003368');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14039,'003371');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31160,'003372');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24091,'003373');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19162,'003374');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4062,'003376');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46158,'003377');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32043,'003378');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29066,'003380');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37174,'003381');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28078,'003382');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40115,'003384');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17097,'003385');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28079,'003386');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49103,'003387');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45087,'003388');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5114,'003390');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10111,'003389');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9196,'003391');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10112,'003392');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9197,'003393');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2045,'003394');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37176,'003395');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10113,'003396');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50152,'003398');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5115,'003399');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6075,'003400');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42107,'003403');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24093,'003404');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6076,'003406');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9198,'003407');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49104,'003408');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2046,'003410');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25133,'003412');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44142,'003413');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50154,'003415');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49105,'003416');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41058,'003417');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41059,'003418');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28080,'003419');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10114,'003420');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19165,'003422');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19167,'003427');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50155,'003429');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6077,'003430');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8110,'003433');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8111,'004554');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48058,'004555');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37178,'004558');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5116,'004560');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6078,'004561');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10116,'004562');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15043,'004563');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45089,'004564');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50159,'004565');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49107,'004566');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5117,'004567');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9200,'004569');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9201,'004570');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7033,'004571');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48059,'004607');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37179,'004572');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5118,'004573');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23058,'004574');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6079,'004575');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50161,'004576');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9202,'004577');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7034,'004578');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19168,'004579');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31161,'004608');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49108,'004580');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49109,'004581');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29068,'004582');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46159,'004583');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8112,'004586');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34099,'004587');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8113,'004588');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24094,'004590');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24095,'004591');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19169,'004592');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34100,'004593');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46160,'004594');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49110,'004595');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49111,'004597');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49112,'004596');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13053,'004598');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26094,'004599');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28082,'004600');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32044,'004601');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45090,'004602');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44143,'004603');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21047,'004604');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47080,'004605');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45091,'004610');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50162,'004611');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18127,'004612');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24096,'004614');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40903,'003333');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40118,'003334');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29069,'003335');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10117,'003336');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18128,'003337');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41060,'003338');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31163,'003339');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34101,'003340');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43075,'003341');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8242,'003342');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16122,'003346');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39040,'003348');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41061,'004478');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46161,'004479');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45092,'004481');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48060,'004482');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23059,'004484');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45093,'004486');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37181,'004488');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5121,'004489');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37182,'004496');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8114,'004498');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8115,'004499');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23060,'004500');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48061,'004502');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47081,'004503');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17100,'004509');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45094,'004510');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43077,'004504');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44145,'004505');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43078,'004511');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16123,'004512');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2047,'004513');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19172,'004514');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32045,'004515');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43081,'004521');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8119,'004522');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25131,'004524');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46163,'004525');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46164,'004526');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46165,'004527');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17101,'004528');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25132,'004529');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37184,'004530');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40123,'004537');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8120,'004539');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42110,'004540');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24099,'004543');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47082,'004545');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19173,'004547');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12076,'004548');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47083,'004551');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49114,'004553');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19174,'004399');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26095,'004400');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49115,'004402');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45096,'004405');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19175,'004406');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15045,'004407');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34102,'004408');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39041,'004410');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19176,'004411');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34103,'004412');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9206,'004413');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36027,'004414');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9208,'004415');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28083,'004416');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5123,'004419');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42113,'004424');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6081,'004420');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47085,'004423');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9209,'004421');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47086,'004422');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11023,'004425');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5124,'004426');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39042,'004427');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8122,'004428');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19177,'004429');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26096,'004430');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47087,'004431');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19178,'004432');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27029,'004433');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36028,'004434');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45097,'004435');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28084,'004436');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47088,'004437');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47089,'004438');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9211,'004439');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49116,'004440');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34104,'004441');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46166,'004442');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15046,'004444');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40126,'004447');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40127,'004448');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13054,'004450');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19179,'004451');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48064,'004466');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45098,'004454');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20901,'004455');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48062,'004456');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31165,'004457');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31166,'004458');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48063,'004459');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34106,'004461');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6082,'004462');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5125,'004463');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50165,'004467');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17099,'004468');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9214,'004473');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9215,'004475');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9216,'004476');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9217,'004477');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39043,'004318');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10120,'004320');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5126,'004321');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50166,'004324');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13055,'004325');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31168,'004326');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50167,'004327');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44148,'004329');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10121,'004330');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50168,'004331');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49117,'004332');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34107,'004333');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19181,'004335');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39044,'004336');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39045,'004337');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17105,'004338');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17105,'004339');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37190,'004341');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45101,'004343');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13056,'004345');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5127,'004346');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29070,'004347');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31169,'004349');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9218,'004350');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19184,'004352');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10122,'004353');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46167,'004354');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3086,'004355');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49118,'004356');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19183,'004357');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21049,'004358');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2048,'004359');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16125,'004360');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15048,'004362');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42115,'004363');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42116,'004364');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16126,'004366');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10123,'004367');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19186,'004368');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28085,'004369');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31170,'004370');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25135,'004371');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46168,'004373');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44149,'004374');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31171,'004375');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9219,'004377');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37193,'004378');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6084,'004379');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9220,'004380');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43084,'004381');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44150,'004382');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5130,'004384');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5131,'004385');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46169,'004386');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36029,'004387');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19188,'004389');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29071,'004391');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15049,'004393');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37194,'004395');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21050,'004397');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10124,'004398');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45103,'004241');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19189,'004242');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47090,'004245');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49119,'004247');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49120,'004251');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30027,'004253');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24100,'004254');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2049,'004255');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37195,'004256');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44151,'004257');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42117,'004259');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8123,'004260');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39046,'004261');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25137,'004262');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17106,'004263');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29072,'004265');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42118,'004270');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49121,'004271');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18134,'004272');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19191,'004273');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9223,'004274');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9224,'004275');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47091,'004276');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46171,'004277');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9225,'004278');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29073,'004280');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36030,'004281');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36031,'004282');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27030,'004284');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50170,'004285');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22155,'004286');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6085,'004287');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50171,'004288');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49122,'004289');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15050,'004290');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22156,'004291');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37196,'004292');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3088,'004295');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27031,'004293');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44152,'004294');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8128,'004296');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8127,'004297');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37198,'004299');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31172,'004301');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50172,'004302');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44153,'004303');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16128,'004304');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44154,'004306');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37199,'004307');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5133,'004308');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46172,'004309');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17109,'004310');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16129,'004313');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16130,'004314');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2050,'004315');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49123,'004316');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12079,'004171');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22157,'004172');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43086,'004175');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8125,'004177');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31173,'004179');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16131,'004180');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44156,'004182');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2051,'004184');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32049,'004186');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10127,'004188');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29074,'004189');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37200,'004191');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28088,'004193');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40130,'004194');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42120,'004195');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41064,'004197');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14041,'004198');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47093,'004199');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42121,'004202');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50173,'004203');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32050,'004205');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27032,'004206');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40131,'004207');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37202,'004208');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37203,'004211');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46174,'004212');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45105,'004213');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8131,'004215');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43089,'004217');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25138,'004218');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8126,'004219');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46175,'004220');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13057,'004221');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6088,'004222');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18137,'004224');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8133,'004227');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25142,'004230');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25141,'004231');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9227,'004233');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14043,'004236');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43091,'004237');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17110,'004238');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43092,'004239');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46176,'004240');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8137,'004095');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7038,'004096');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14044,'004097');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45106,'004100');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44158,'004101');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9228,'004104');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13058,'004105');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40132,'004106');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47094,'004107');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49001,'004108');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18138,'004109');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10128,'004110');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28089,'004111');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47095,'004112');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49125,'004115');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5134,'004113');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49126,'004114');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47096,'004116');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49127,'004120');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49128,'004117');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49129,'004118');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49130,'004119');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49131,'004121');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28090,'004122');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36032,'004123');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37204,'004124');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50176,'004126');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28091,'004127');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30028,'004128');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19194,'004129');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34109,'004130');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10129,'004131');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1039,'004133');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12080,'004136');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19195,'004137');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31174,'004138');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49132,'004141');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48066,'004144');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14045,'004145');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37205,'004146');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37206,'004147');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37207,'004148');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37208,'004151');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50178,'004152');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36033,'004153');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44160,'004155');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16132,'004157');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16133,'004158');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16134,'004160');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2052,'004161');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18140,'004162');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50179,'004165');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40134,'004167');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50180,'004168');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47098,'004169');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19196,'004012');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50181,'004013');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49134,'004015');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49135,'004016');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49136,'004018');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15051,'004019');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30029,'004021');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5135,'004030');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48068,'004022');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2053,'004024');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5136,'004031');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44161,'004026');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26098,'004027');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48007,'004028');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5138,'004032');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5139,'004033');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5140,'004034');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40135,'004035');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5141,'004036');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5142,'004037');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5143,'004038');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40136,'004039');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8139,'004040');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27033,'004041');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31176,'004042');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30030,'004043');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50184,'004044');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24101,'004045');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47100,'004046');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42124,'004047');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42125,'004048');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31177,'004049');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31178,'004052');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31179,'004053');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3091,'004054');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7039,'004055');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26100,'004056');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26101,'004058');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15053,'004059');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18141,'004061');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48908,'004062');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46177,'004064');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48071,'004065');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20057,'004066');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20056,'004067');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3090,'004068');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48067,'004070');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48909,'004071');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4065,'004072');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26103,'004075');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25145,'004076');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45107,'004077');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16137,'004079');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5144,'004081');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5145,'004082');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42128,'004083');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5147,'004086');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5148,'004087');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5149,'004085');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25025,'004088');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33040,'004089');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37212,'005198');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5151,'005199');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28093,'005200');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10130,'005201');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37214,'004093');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5153,'005196');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47101,'005197');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9229,'005193');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37215,'005195');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5154,'005202');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5155,'005203');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45109,'005205');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5156,'005206');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12081,'005207');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22160,'005209');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5157,'005210');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28094,'005211');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28095,'005212');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28096,'005214');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42129,'005215');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37216,'005216');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40140,'005217');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40141,'005218');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5158,'005219');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10131,'005221');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45111,'005222');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5159,'005224');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5160,'005225');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5161,'005226');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5162,'005227');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13059,'005228');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5163,'005230');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10132,'005231');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6091,'005232');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37218,'005233');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45114,'005234');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5164,'005235');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8140,'005236');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40142,'005238');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40144,'005240');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1041,'005241');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28097,'005242');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5165,'005243');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37219,'005244');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5166,'005245');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26105,'005247');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5167,'005248');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9230,'005250');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13060,'003997');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2054,'003999');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10133,'004005');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28099,'004007');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40145,'004001');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40146,'004003');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23063,'004004');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17111,'005102');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5169,'005103');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5170,'005104');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10134,'005106');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33041,'005107');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27034,'005108');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49137,'005109');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31182,'005110');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9231,'005111');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15055,'005112');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19198,'005113');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15056,'005114');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37222,'005115');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27035,'005116');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9232,'005117');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5171,'005118');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42130,'005119');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29075,'005120');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2055,'005121');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21052,'005122');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26106,'005123');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18903,'005124');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21053,'005126');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40148,'005127');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26107,'005128');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18143,'005130');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50187,'005131');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5172,'005132');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23064,'005136');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45115,'005137');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24102,'005138');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45116,'005139');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34112,'005141');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6092,'005142');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44163,'005144');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44164,'005145');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44165,'005146');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15057,'005147');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39047,'005148');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42131,'005149');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45117,'005150');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50188,'005151');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50189,'005152');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33042,'005153');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22162,'005156');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50190,'005157');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3093,'005158');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42132,'005161');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50191,'005162');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22163,'005164');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14046,'005165');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47102,'005166');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50193,'005169');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12082,'005170');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43098,'005171');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45119,'005172');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10135,'005174');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31183,'005175');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14047,'005176');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19199,'005179');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31184,'005182');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8143,'005185');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31186,'005186');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24103,'005188');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17112,'005189');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4067,'005191');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36036,'005019');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20063,'005020');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32053,'005021');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31187,'005022');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26110,'005023');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5173,'005027');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44169,'005026');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1042,'005028');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20058,'005029');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44171,'005032');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34113,'005033');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15058,'005034');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31190,'005035');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8146,'005037');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8147,'005038');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25149,'005039');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44172,'005041');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25150,'005042');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31191,'005043');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46181,'005045');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6093,'005047');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10136,'005049');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41067,'005051');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47103,'005052');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8148,'005054');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6095,'005055');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26111,'005056');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31194,'005058');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19201,'005059');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16140,'005061');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28101,'005062');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16143,'005065');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9235,'005066');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47104,'005067');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37223,'005068');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49138,'005069');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47105,'005072');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47106,'005074');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46182,'005075');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12083,'005076');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40149,'005077');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8149,'005079');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17114,'005080');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4068,'005082');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8144,'005084');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42134,'005085');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22164,'005086');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11024,'005087');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31193,'005089');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9238,'004932');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20059,'004933');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42135,'005093');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12084,'005094');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3095,'005095');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48073,'005096');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3096,'005097');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22165,'005101');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46184,'005100');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2056,'004930');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24105,'004931');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9239,'004934');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3097,'004935');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31195,'004937');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31196,'004939');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5174,'004940');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50195,'004941');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18146,'004942');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23065,'004943');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15059,'004945');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17115,'004947');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20076,'004948');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48074,'004949');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19204,'004950');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40150,'004951');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6096,'004952');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6097,'004953');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50196,'004956');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20060,'004958');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45124,'004960');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18147,'004961');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4070,'004962');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44174,'004964');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20061,'004965');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20062,'004969');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31198,'004970');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12085,'004971');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15060,'004973');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31199,'004975');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48075,'004976');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44175,'004978');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8153,'004979');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40901,'004982');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40151,'004981');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15061,'004983');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48083,'004984');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16145,'004988');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25156,'004987');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50198,'004989');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24106,'004990');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50199,'004991');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17116,'004993');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34116,'004994');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34901,'004995');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2057,'004996');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22167,'004997');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41068,'004999');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31200,'005000');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45126,'005001');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49139,'005002');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40152,'005003');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46185,'005005');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18149,'005006');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48072,'005007');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27038,'005008');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27039,'005009');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15062,'005010');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33044,'005011');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15064,'005015');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32055,'005016');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5176,'005017');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9242,'005018');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9243,'004859');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32056,'004860');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9244,'004862');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18150,'004863');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4071,'004864');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46186,'004865');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40154,'004867');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5177,'004868');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37225,'004869');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49141,'004870');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24107,'004871');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16147,'004873');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5178,'004874');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37226,'004879');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9246,'004875');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24108,'004876');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49142,'004880');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24109,'004881');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9247,'004877');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49143,'004878');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37228,'004883');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8155,'004884');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17117,'004885');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12087,'004887');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27040,'004888');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8156,'004890');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17121,'004892');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17120,'004893');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47109,'004894');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40155,'004895');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9248,'004896');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34120,'004898');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37229,'004899');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14048,'004900');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34121,'004901');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7040,'004905');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46188,'004911');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22168,'004912');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17123,'004913');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46189,'004914');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44176,'004915');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16148,'004916');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6098,'004918');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45127,'004919');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16149,'004920');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10137,'004921');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21055,'004922');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17124,'004923');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18151,'004924');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9250,'004925');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9251,'004927');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44177,'004928');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50200,'004929');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22170,'004783');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45128,'004784');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5179,'004786');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16150,'004788');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28104,'004789');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50201,'004790');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50202,'004791');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37230,'004792');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37231,'004793');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32057,'004794');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41071,'004795');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27042,'004796');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37232,'004797');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29077,'004801');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3100,'004802');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9253,'004803');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17125,'004804');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19209,'004805');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16151,'004806');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45129,'004807');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34123,'004808');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19210,'004809');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19211,'004810');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28106,'004812');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33045,'004818');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45130,'004820');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4072,'004821');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20064,'004823');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5181,'004825');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43101,'004827');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37233,'004828');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19212,'004830');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50203,'004831');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46190,'004832');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21056,'004834');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2058,'004835');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11025,'004833');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28107,'004837');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17128,'004838');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21057,'004841');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34124,'004843');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26113,'004845');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23066,'004846');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4074,'004847');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27045,'004849');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47112,'004851');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46191,'004852');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40156,'004854');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37235,'004855');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34125,'004856');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3101,'004858');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41072,'004707');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28108,'004709');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14050,'004710');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5182,'004711');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14051,'004714');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50204,'004715');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13061,'004713');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9256,'004718');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34126,'004719');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47113,'004723');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24111,'004723');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37236,'004725');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37237,'004726');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37238,'004727');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26114,'004728');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10139,'004730');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23067,'004732');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3102,'004733');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5184,'004735');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37240,'004736');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37242,'004739');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28109,'004740');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40157,'004741');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49147,'004742');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49148,'004743');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18153,'004744');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37244,'004749');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47114,'004751');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41074,'004752');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47115,'004753');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39048,'004745');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22172,'004754');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5185,'004755');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6100,'004757');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19215,'004758');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33046,'004759');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33047,'004760');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37245,'004761');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37246,'004762');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9261,'004763');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37247,'004764');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14052,'004766');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39049,'004767');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2059,'004770');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2060,'004769');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49149,'004771');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25164,'004747');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45132,'004773');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49150,'004774');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44180,'004776');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8160,'004777');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17132,'004781');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9262,'004780');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10140,'004782');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6101,'004630');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44181,'004632');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37248,'004633');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37249,'004634');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19216,'004635');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34127,'004636');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44182,'004638');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10142,'004639');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28110,'004637');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31202,'004640');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22174,'004641');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22175,'004642');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22176,'004643');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19217,'004644');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25165,'004645');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24112,'004646');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50206,'004649');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49152,'004653');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29079,'004654');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49153,'004655');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37251,'004657');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40158,'004658');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22178,'004660');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39050,'004661');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10143,'004662');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33048,'004664');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39051,'004665');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47116,'004667');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7041,'004670');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3104,'004671');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28111,'004675');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46193,'004677');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46194,'004678');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13063,'004681');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5186,'004682');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49155,'004683');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5187,'004684');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31204,'004685');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10145,'004686');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50207,'004687');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8161,'004689');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3902,'004691');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41075,'004692');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46195,'004693');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33049,'004695');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34130,'005815');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50208,'004696');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47117,'005816');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12090,'004697');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16159,'004699');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40159,'004700');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40160,'004701');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16160,'004702');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9266,'004703');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8163,'004704');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37252,'004705');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9267,'004706');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47118,'005818');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47119,'005819');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46196,'005793');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19218,'005794');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42139,'005799');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9268,'005795');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9269,'005796');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28112,'005800');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19219,'005797');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49156,'005798');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9270,'005801');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26115,'005802');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49157,'005803');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10146,'005807');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32061,'005821');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3105,'005811');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50209,'005812');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28113,'005814');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10147,'005823');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19220,'005824');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19221,'005825');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16161,'005826');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43107,'005827');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44183,'005829');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37254,'005830');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31205,'005831');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29080,'005832');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22182,'005837');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3106,'005838');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17134,'005840');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10148,'005842');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10149,'005844');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50212,'005845');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50213,'005846');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48077,'005847');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30032,'005848');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44184,'005849');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49159,'005715');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49160,'005714');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13064,'005716');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43112,'005720');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36041,'005724');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27046,'005725');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39053,'005727');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39054,'005729');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22184,'005731');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34134,'005730');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47121,'005736');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3107,'005737');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18162,'005738');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34135,'005739');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50214,'005740');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24115,'005741');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33050,'005742');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17135,'005745');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36043,'005748');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15068,'005749');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36044,'005750');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15069,'005751');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32064,'005752');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36038,'005755');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8168,'005757');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25172,'005759');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23069,'005760');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32062,'005761');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17137,'005762');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43114,'005763');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7043,'005764');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10150,'005767');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16162,'005768');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36040,'005769');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17138,'005770');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12091,'005771');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10151,'005774');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16163,'005775');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47122,'005776');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42140,'005777');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45134,'005778');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49162,'005779');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15071,'005780');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48078,'005782');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13065,'005784');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14053,'005786');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39055,'005787');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5188,'005789');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37256,'005639');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19223,'005790');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2062,'005641');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5189,'005642');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16165,'005643');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9272,'005644');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34136,'005645');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47123,'005646');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47124,'005647');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42142,'005648');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5190,'005650');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16166,'005655');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49163,'005656');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14054,'005657');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19224,'005652');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19225,'005653');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34137,'005654');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2063,'005658');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2064,'005659');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16908,'005661');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16167,'005662');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37257,'005663');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50215,'005664');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44190,'005665');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2065,'005666');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13066,'005670');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47125,'005671');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28116,'005675');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40161,'005677');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43115,'005681');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43116,'005685');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26118,'005687');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49165,'005688');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24118,'005689');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11026,'005690');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9274,'005691');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5191,'005693');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19227,'005692');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43117,'005694');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43118,'005696');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25175,'005699');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33051,'005700');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25176,'005701');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25177,'005702');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9275,'005706');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24119,'005708');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16170,'005709');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24120,'005711');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33052,'005576');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25179,'005578');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41076,'005579');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16172,'005583');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12092,'005585');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37258,'005587');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19228,'005588');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16173,'005592');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13068,'005593');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6103,'005596');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6104,'005597');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28118,'005598');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24121,'005599');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6105,'005612');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6106,'005614');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16174,'005616');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6107,'005602');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40163,'005603');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49166,'005606');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37259,'005604');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46201,'005605');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19229,'005609');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37260,'005611');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49167,'005618');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50220,'005619');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37261,'005624');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22188,'005622');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9277,'005630');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14056,'005625');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31206,'005626');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22902,'005627');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28902,'005631');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39056,'005628');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37262,'005633');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5192,'005634');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38028,'005636');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35017,'005500');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10153,'005638');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45139,'005637');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13071,'005508');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30033,'005502');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44193,'005509');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21059,'005503');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11028,'005504');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37264,'005505');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11029,'005506');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31207,'005510');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22193,'005512');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46204,'005513');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43119,'005517');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7045,'005518');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8175,'005519');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25182,'005521');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8176,'005522');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29081,'005523');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45140,'005524');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18165,'005525');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38029,'005528');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38030,'005529');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47126,'005531');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50221,'005533');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18167,'005534');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17142,'005537');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46101,'005538');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46102,'005539');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46103,'005540');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46104,'005541');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26120,'005543');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9279,'005544');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19230,'005546');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17043,'005547');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45141,'005548');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43120,'005549');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46206,'005550');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23073,'005551');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22195,'005552');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28119,'005553');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9280,'005561');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6109,'005554');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24123,'005555');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24124,'005556');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9281,'005557');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34141,'005558');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9287,'005563');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9288,'005564');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45142,'005565');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9289,'005566');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16175,'005567');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42144,'005559');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42145,'005568');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9292,'005569');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24125,'005560');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47127,'005570');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9294,'005571');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9295,'005422');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47128,'005423');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49168,'005424');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49169,'005425');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34143,'005573');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47130,'005574');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49170,'005575');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9901,'005429');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32066,'005430');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50222,'005431');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27050,'005433');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49171,'005435');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45143,'005436');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49172,'005438');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26121,'005439');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9302,'005440');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16176,'005447');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3109,'005448');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46208,'005451');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46209,'005452');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32067,'005457');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8178,'005458');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39057,'005459');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47132,'005462');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39058,'005466');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43121,'005467');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5193,'005468');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9306,'005476');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40165,'005477');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19231,'005478');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45145,'005479');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42152,'005482');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9307,'005484');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9308,'005485');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36045,'005487');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28121,'005489');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24127,'005492');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9309,'005493');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6110,'005495');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39059,'005496');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9310,'005497');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34146,'005498');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3112,'005342');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8179,'005343');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42153,'005344');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50223,'005345');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40166,'005346');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6111,'005347');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43122,'005348');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34147,'005350');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19233,'005351');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42154,'005352');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49174,'005355');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46213,'005356');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34149,'005357');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34151,'005358');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45146,'005360');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19234,'005362');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37267,'005363');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42155,'005364');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9311,'005365');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13072,'005366');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43123,'005367');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49175,'005368');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34152,'005369');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34154,'005371');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9314,'005372');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9315,'005373');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9316,'005374');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24129,'005375');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9317,'005376');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42156,'005377');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25183,'005379');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24130,'005381');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15072,'005380');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32069,'005386');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33055,'005387');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27051,'005388');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19235,'005385');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33056,'005389');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36046,'005390');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31208,'005391');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26124,'005392');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34155,'005397');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27052,'005398');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28122,'005399');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15073,'005400');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1046,'005402');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1047,'005403');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33057,'005404');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6113,'005405');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34156,'005408');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12095,'005410');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17145,'005409');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40171,'005411');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50225,'005412');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30034,'005413');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24131,'005414');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24132,'005415');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17146,'005416');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45147,'005417');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44195,'005419');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19237,'005420');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25186,'005268');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5194,'005269');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9318,'005270');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44196,'005271');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29083,'005276');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4078,'005277');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46215,'005278');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10155,'005279');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39063,'005280');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49177,'005281');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2067,'005282');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33058,'005284');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42158,'005285');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24133,'005286');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27054,'005287');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39064,'005288');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17147,'005289');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8180,'005290');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6114,'005291');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17148,'005292');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17149,'005293');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43127,'005294');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43128,'005295');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17150,'005296');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43129,'005297');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17151,'005298');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28123,'005299');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5196,'005300');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9321,'005301');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49178,'005302');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47134,'005302');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47135,'005305');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37269,'005306');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49179,'005307');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10156,'005308');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28124,'005309');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10157,'005310');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19239,'005311');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10158,'005312');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2068,'005313');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28125,'005315');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19240,'005316');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45148,'005317');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10159,'005318');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37270,'005319');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28126,'005320');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22197,'005321');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26126,'005322');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46216,'005325');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43130,'005326');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21061,'005327');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40173,'005331');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8183,'005332');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36047,'005333');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26127,'005335');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49180,'005337');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15074,'005338');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3113,'005339');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9323,'005340');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42159,'005341');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10160,'006297');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19241,'006298');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19242,'006299');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50227,'006300');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31209,'006301');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31210,'006303');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29084,'006305');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4079,'006308');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43133,'006309');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21062,'006311');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17152,'006314');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49181,'006315');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11030,'006318');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47137,'006321');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44198,'006324');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28128,'006328');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10161,'006331');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9326,'006332');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44199,'006336');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44201,'006338');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18170,'006341');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9327,'006342');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9328,'006343');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47139,'006344');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19243,'006346');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39066,'006347');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50229,'006348');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39067,'006349');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46219,'006350');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13902,'006351');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39068,'006352');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8901,'006354');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23074,'006355');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14058,'006356');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8187,'006357');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24137,'006358');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23075,'006361');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12097,'006362');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19244,'006363');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16185,'005251');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13073,'005253');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40174,'005254');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15075,'005256');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15075,'005255');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16186,'005258');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19246,'005259');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47140,'005260');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37272,'005261');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3115,'005263');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37274,'006224');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18171,'006225');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29085,'006226');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33059,'006227');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22201,'006228');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22202,'006229');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9329,'006230');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49183,'006233');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36049,'006234');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44203,'006235');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34157,'006236');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9332,'006237');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37275,'006238');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8190,'006239');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42161,'006242');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46221,'006243');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17154,'006244');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22203,'006245');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3116,'006247');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1049,'006248');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16187,'006251');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31214,'006249');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34158,'006250');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9334,'006253');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8191,'006254');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16188,'006257');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37276,'006258');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5197,'006259');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2070,'006260');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18173,'006261');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10162,'006263');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43905,'006264');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17155,'006265');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41085,'006266');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16189,'006267');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36050,'006271');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50232,'006272');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6117,'006273');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10163,'006274');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37277,'006275');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1052,'006278');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40176,'006279');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49184,'006280');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27055,'006281');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44205,'006282');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50233,'006283');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32074,'006290');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26129,'006159');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13074,'006167');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24143,'006075');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40179,'006079');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5205,'006080');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37282,'006081');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32075,'006172');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16190,'006173');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32076,'006180');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37283,'006085');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24145,'006181');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42162,'006182');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37284,'006183');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22205,'006184');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49188,'006188');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5206,'006185');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5207,'006189');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24146,'006186');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5208,'006187');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3117,'006086');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42163,'006190');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39069,'006191');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37285,'006192');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11031,'006193');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28130,'006194');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3118,'006195');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31216,'006089');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30035,'006199');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12099,'006200');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3119,'006203');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41086,'006204');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46222,'006205');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5901,'006206');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5210,'006207');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5211,'006208');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38034,'006209');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5212,'006211');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9337,'006212');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5213,'006213');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21064,'006214');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22207,'006210');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49189,'006215');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24148,'006216');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42164,'006217');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47143,'006218');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13075,'006102');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28131,'006103');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16191,'006104');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5214,'006105');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38035,'006127');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39070,'006128');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5217,'006130');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49191,'006131');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47145,'006135');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22903,'006136');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47146,'006137');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37036,'006138');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49192,'006139');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3120,'006132');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5218,'006133');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37287,'006134');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37288,'006145');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37289,'006146');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47147,'006149');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45153,'006150');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5219,'006151');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2071,'006152');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24150,'006153');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49193,'006154');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49194,'006156');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5220,'006041');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47148,'006157');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30036,'006042');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39071,'006043');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37290,'006044');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37291,'006040');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42165,'006045');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16193,'006046');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47149,'006047');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37292,'006048');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11033,'006054');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39072,'006055');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15076,'006056');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47151,'006057');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31219,'006093');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6120,'005948');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10165,'005949');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45155,'005950');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21067,'005951');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31220,'006968');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9343,'005955');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34167,'005957');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22208,'005958');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49197,'005959');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26134,'005960');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17180,'005962');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8245,'005963');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43139,'005964');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24151,'005965');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49199,'005966');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24152,'005967');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15077,'005968');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49200,'005970');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24153,'005971');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49201,'005972');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39073,'005973');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34168,'005974');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38037,'005976');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9345,'005977');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10166,'005979');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45156,'005980');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18174,'005876');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45157,'005877');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5221,'005878');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50237,'005868');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16194,'005869');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13077,'005870');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44208,'005871');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10167,'005872');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5222,'005873');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38038,'005874');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42166,'005875');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23076,'005880');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14060,'006969');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26135,'005882');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14061,'005883');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47152,'005884');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49202,'005885');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7053,'005886');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44209,'005888');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26136,'005889');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33062,'005891');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9347,'005898');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12102,'005902');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8250,'005904');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8251,'005905');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8253,'005906');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8256,'005918');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8257,'005920');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8259,'006936');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24159,'006954');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6121,'006955');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40186,'006958');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10168,'006956');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37294,'006957');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39075,'006971');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8196,'006096');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8197,'006097');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17157,'006098');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17183,'006099');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7046,'006100');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8198,'006101');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45158,'006959');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9354,'006960');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21069,'006961');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17184,'006963');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3121,'006965');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24160,'006973');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8261,'006966');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38039,'006967');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8199,'005981');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8200,'005982');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8202,'005986');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8204,'005987');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17158,'005988');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8206,'005990');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50239,'006974');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25196,'005991');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8207,'005992');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8208,'005993');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17159,'005994');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8210,'005995');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8211,'005997');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17161,'005998');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17162,'006000');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8209,'006001');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8213,'006002');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17163,'006003');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25192,'006004');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25197,'006005');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17164,'006006');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23077,'006979');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15078,'006980');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37296,'006981');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10170,'006983');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5228,'006984');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38040,'006985');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5904,'006982');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24161,'005854');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23904,'005855');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39076,'006863');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41089,'006864');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33063,'006062');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8193,'006008');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15079,'006865');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23079,'006866');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39077,'006867');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39078,'006868');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19250,'006869');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40188,'006870');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40189,'006871');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37299,'006872');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17165,'006010');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43137,'006011');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7049,'006013');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7050,'006014');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17167,'006015');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17168,'006016');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8218,'006018');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17185,'006020');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17166,'006021');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8221,'006026');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7052,'006032');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12100,'005923');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8229,'005924');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17174,'005925');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17176,'005927');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33064,'006873');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45901,'006878');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26138,'006874');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5229,'006875');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9358,'006877');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30901,'006882');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39079,'006883');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28136,'006884');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26139,'006063');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49207,'006888');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24162,'006889');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47155,'006890');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34174,'006891');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17177,'005928');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8192,'006892');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8231,'005929');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8232,'005930');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8234,'005932');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17178,'005933');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8189,'005934');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8235,'005935');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8237,'005937');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8239,'005939');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25194,'005940');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8098,'005943');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26141,'006894');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48082,'006895');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39080,'006066');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49208,'006067');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26142,'006068');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47156,'006069');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3122,'006070');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9360,'006071');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49209,'006072');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36051,'006896');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49210,'006897');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9361,'006900');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33065,'006901');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24163,'006902');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22213,'006903');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39081,'006904');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43142,'006906');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12103,'006907');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32078,'006908');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27057,'006909');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25201,'006913');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25200,'006914');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31223,'006915');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45159,'006916');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19251,'006919');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10173,'006920');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37302,'006922');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40192,'006923');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17187,'006924');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3123,'006928');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29086,'006929');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22214,'006933');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29087,'006780');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50242,'006781');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46224,'006782');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12104,'006783');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40194,'006784');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20070,'006785');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44211,'006788');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10174,'006789');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45160,'006790');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22215,'006791');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19254,'006792');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39082,'006793');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3124,'006794');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46225,'006795');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7058,'006796');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19901,'006799');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46226,'006800');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22217,'006801');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24164,'006802');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43146,'006803');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7047,'006804');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3125,'006808');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44212,'006809');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25202,'006810');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8267,'006812');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46227,'006813');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37304,'006814');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40196,'006816');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37305,'006818');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46228,'006825');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47159,'006827');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10175,'006829');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37306,'006830');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37307,'006831');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5233,'006832');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28140,'006833');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22220,'006835');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45161,'006836');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31224,'006837');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48084,'006838');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50243,'006839');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17192,'006841');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19255,'006843');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8269,'006845');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28141,'006847');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45162,'006848');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25205,'006849');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19256,'006850');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33066,'006851');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10177,'006853');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50244,'006854');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29088,'006855');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4084,'006857');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46229,'006859');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37310,'006861');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47160,'006860');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5234,'006696');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19257,'006697');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46230,'006700');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36052,'006701');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17193,'006703');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47161,'006704');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46231,'006705');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46232,'006706');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7060,'006707');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44213,'006708');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5235,'006709');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6125,'006710');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16198,'006712');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8270,'006713');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17052,'006714');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39083,'006715');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27059,'006716');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50247,'006717');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37311,'006718');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8271,'006721');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33067,'006722');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2072,'006723');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26143,'006725');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5236,'006727');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6126,'006728');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13080,'006730');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5237,'006729');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19258,'006731');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9366,'006732');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42172,'006735');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43147,'006736');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46233,'006737');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5238,'006740');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25207,'006741');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33068,'006742');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19259,'006743');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28143,'006745');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48904,'006748');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12106,'006749');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45163,'006750');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7062,'006747');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22223,'006751');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48085,'006752');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48086,'006754');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8272,'006755');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4086,'006756');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9368,'006757');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42173,'006758');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25208,'006759');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37312,'006760');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23084,'006761');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31225,'006762');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25209,'006763');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26144,'006765');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25210,'006767');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5239,'006770');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46234,'006768');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12107,'006769');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40198,'006772');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5240,'006774');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9369,'006775');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45164,'006776');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34176,'006619');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34177,'006778');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24166,'006779');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33069,'006615');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28144,'006616');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19261,'006620');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26146,'006617');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16909,'006622');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40199,'006623');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37313,'006624');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9372,'006625');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9373,'006626');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36053,'006627');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39085,'006628');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8273,'006629');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25211,'006630');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46235,'006631');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42175,'006632');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12108,'006633');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48076,'006635');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31226,'006637');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25212,'006638');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17194,'006641');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34178,'006642');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34179,'006643');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40200,'006644');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37314,'006646');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4088,'006647');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4089,'006648');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27060,'006649');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32079,'006650');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50249,'006651');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38043,'006652');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31227,'006653');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8276,'006654');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4090,'006656');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42176,'006657');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42177,'006658');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8277,'006660');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28145,'006661');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50250,'006662');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25215,'006663');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25216,'006666');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45165,'006667');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6128,'006668');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10179,'006669');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10180,'006670');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16202,'006671');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12109,'006672');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42178,'006675');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37316,'006677');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22225,'006679');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47162,'006680');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6130,'006682');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33070,'006684');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49216,'006685');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8278,'006686');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33071,'006688');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19264,'006690');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37317,'006692');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2073,'006693');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9377,'006527');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42181,'006528');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22226,'006529');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11035,'006531');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42182,'006532');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43148,'006533');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25219,'006536');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19265,'006537');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50252,'006538');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46237,'006539');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46238,'006540');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8280,'006542');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38045,'006543');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29089,'006544');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38046,'006546');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35024,'006547');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9378,'006550');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16205,'006551');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42183,'006552');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35025,'006554');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37320,'006556');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35026,'006557');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22227,'006558');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45166,'006559');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19266,'006560');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15082,'006562');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12110,'006563');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46239,'006564');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35027,'006566');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4091,'006567');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17196,'006568');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37322,'006569');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9380,'006570');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8279,'006571');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46240,'006572');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50253,'006573');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44215,'006574');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13081,'006575');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26147,'006576');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44216,'006577');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19267,'006578');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3128,'006579');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33072,'006580');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8282,'006581');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3129,'006583');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31228,'006584');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47163,'006585');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28146,'006586');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50254,'006588');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22228,'006589');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19268,'006590');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38047,'006591');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16206,'006593');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35029,'006594');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33073,'006595');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5242,'006597');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31229,'006598');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26148,'006599');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46241,'006602');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28147,'006603');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25222,'006604');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43149,'006605');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43150,'006606');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9382,'006608');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2074,'006609');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50255,'006610');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41092,'006613');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12112,'006614');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12113,'006451');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5243,'006453');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45168,'006454');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3130,'006455');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40201,'006456');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29090,'006458');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22229,'006459');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41093,'006460');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13082,'006461');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36054,'006462');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8283,'006463');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37323,'006464');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15083,'006465');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24168,'006467');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47164,'006469');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19271,'006470');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8284,'006472');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47165,'006473');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19272,'006474');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37324,'006475');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15084,'006476');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24169,'006479');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19274,'006480');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10182,'006481');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44217,'006482');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22230,'006483');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42184,'006484');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26150,'006485');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5244,'006486');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3131,'006488');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5245,'006492');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10183,'006493');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44219,'006495');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49219,'006496');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34182,'006498');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16209,'006499');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13083,'006501');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50256,'006502');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44220,'006503');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12116,'006506');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45169,'006504');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50257,'006505');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50258,'006508');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40202,'006388');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25226,'006391');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42185,'006392');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12117,'006393');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23085,'006394');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40203,'006395');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14062,'006396');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18178,'006397');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12118,'006398');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44221,'006399');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47166,'006400');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45170,'006401');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47167,'006402');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47168,'006403');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9386,'006405');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10185,'006404');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40204,'006406');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44222,'006407');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26151,'006408');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10186,'006410');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26152,'006409');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19277,'006411');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19278,'006412');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44223,'006510');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10187,'006513');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13084,'006517');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44224,'006518');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24170,'006523');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19279,'006524');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23086,'006413');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44225,'006525');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43153,'006414');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6131,'006520');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23087,'006415');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26153,'006383');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9387,'006418');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49221,'006419');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25230,'006420');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50259,'006421');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40205,'006422');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10189,'006428');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16211,'006429');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28151,'006431');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25231,'006432');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50260,'006433');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9388,'006434');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22233,'006385');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39087,'006435');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8287,'006436');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46243,'006437');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50261,'006438');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8288,'006439');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8289,'006440');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28152,'006442');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44227,'006386');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3132,'006443');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6132,'006444');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10191,'006446');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10192,'006447');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19281,'006448');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28153,'006449');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44228,'007536');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19282,'007537');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19283,'007538');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19284,'007539');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29901,'007540');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26154,'007541');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22234,'007545');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13085,'007546');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10193,'007547');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30037,'007548');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9389,'007549');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23088,'007550');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10194,'007551');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23090,'007552');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9390,'007564');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23091,'007553');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22235,'007555');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28154,'007559');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49222,'007561');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25232,'007560');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25233,'007566');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37327,'007567');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40206,'006387');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44230,'007568');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3133,'007569');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45172,'007570');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44231,'007571');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50263,'007572');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44232,'007573');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45173,'007574');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43154,'007577');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29091,'007578');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19285,'007579');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16212,'007581');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16213,'007582');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42187,'007580');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19287,'007588');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19288,'007589');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9392,'007591');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17201,'007592');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50264,'007593');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17202,'006364');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30039,'006366');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15085,'006368');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46246,'006369');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27061,'006370');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24171,'006371');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37328,'006372');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49223,'006373');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16215,'006374');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19289,'006375');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12121,'006376');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44234,'006377');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44235,'006378');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22239,'006379');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6134,'006380');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32082,'006381');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50266,'007448');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47173,'007449');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15086,'007450');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11037,'007451');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49224,'007452');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37329,'007453');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40207,'007457');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16216,'007458');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9394,'007459');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39088,'007460');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26155,'007463');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27062,'007464');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16217,'007465');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26157,'007466');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21070,'007467');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47174,'007468');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19290,'007469');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19291,'007470');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34185,'007471');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24172,'007473');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48087,'007474');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6135,'007475');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10195,'007476');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9395,'007477');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9396,'007478');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39089,'007479');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31232,'007480');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47175,'007481');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26158,'007482');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36055,'007484');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35030,'007485');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31233,'007486');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24173,'007487');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45175,'007490');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4093,'007492');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4094,'007493');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23092,'007494');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11038,'007496');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31234,'007497');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19293,'007498');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42189,'007499');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45176,'007503');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31123,'007505');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19294,'007506');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30040,'007508');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4095,'007509');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8290,'007511');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17205,'007512');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43156,'007513');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43157,'007514');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17203,'007515');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31236,'007516');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16219,'007525');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49225,'007526');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50267,'007519');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31237,'007520');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9398,'007527');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45177,'007528');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48089,'007532');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20072,'007534');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47177,'007535');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31241,'007368');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31242,'007369');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20077,'007372');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31244,'007374');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31243,'007375');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47178,'007376');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40210,'007377');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26160,'007378');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31245,'007380');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6136,'007381');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50271,'007382');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20073,'007384');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19296,'007385');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50272,'007386');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31246,'007387');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46249,'007388');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41095,'007389');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44238,'007390');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8291,'007392');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42190,'007393');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49226,'007394');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5251,'007395');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9400,'007396');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44239,'007402');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44240,'007404');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47179,'007405');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34186,'007406');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49227,'007407');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28155,'007410');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19297,'007411');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10196,'007412');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44241,'007413');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9403,'007414');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19298,'007416');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19299,'007417');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42191,'007418');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28156,'007419');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19300,'007420');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6137,'007421');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10197,'007422');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37330,'007423');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5252,'007424');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19301,'007426');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44243,'007427');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49228,'007428');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24175,'007429');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10198,'007430');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2075,'007433');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42192,'007434');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19302,'007436');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37332,'007437');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50273,'007438');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37333,'007441');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10200,'007442');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37334,'007443');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42193,'007444');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28157,'007445');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21071,'007446');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19303,'007293');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44244,'007294');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37335,'007295');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44245,'007296');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24177,'007297');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26161,'007298');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42194,'007299');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28158,'007300');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13086,'007301');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28159,'007302');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16224,'007303');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37336,'007304');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24178,'007305');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10201,'007306');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28160,'007307');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16225,'007308');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28161,'007309');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16227,'007310');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42195,'007311');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47181,'007312');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10202,'007314');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39092,'007315');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16228,'007316');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34189,'007317');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13087,'007319');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19305,'007321');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24180,'007324');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42196,'007325');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40211,'007327');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24181,'007328');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19306,'007330');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39094,'007331');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24182,'007332');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44246,'007333');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42197,'007334');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37337,'007335');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24183,'007336');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24184,'007338');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24185,'007398');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39095,'007400');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49229,'007339');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19307,'007340');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47182,'007341');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6138,'007342');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28164,'007343');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34192,'007345');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40212,'007346');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40213,'007347');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45179,'007348');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24187,'007349');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9405,'007350');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28165,'007351');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15088,'007401');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9406,'007352');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15087,'007353');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37338,'007354');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47183,'007355');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46250,'007356');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24188,'007358');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6139,'007359');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6141,'007361');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14063,'007363');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13088,'007364');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37339,'007366');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22242,'007222');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19308,'007223');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36056,'007224');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19309,'007226');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16231,'007227');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44247,'007228');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46251,'007240');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46252,'007242');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9408,'007243');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12123,'007244');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25238,'007246');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8293,'007247');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43158,'007248');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12125,'007231');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3135,'007235');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3136,'007236');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3137,'007237');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (7063,'007249');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24191,'007277');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1056,'007252');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34196,'007254');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22901,'007255');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6146,'007256');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9904,'007257');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22244,'007258');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9908,'007259');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34902,'007274');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9409,'007260');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6147,'007261');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9410,'007262');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9411,'007263');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6148,'007264');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9905,'007266');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40218,'007267');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9412,'007268');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9413,'007270');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9414,'007271');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9415,'007272');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9416,'007273');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9417,'007279');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38049,'007276');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38050,'007280');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40219,'007282');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40220,'007283');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49230,'007287');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9418,'007286');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35032,'007288');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25240,'007289');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43159,'007291');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8294,'007292');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12127,'007151');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8295,'007152');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17209,'007153');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43160,'007154');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8296,'007155');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43161,'007156');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50275,'007160');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9407,'007161');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45180,'007162');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47184,'007164');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50276,'007165');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37340,'007166');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16234,'007167');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40214,'007168');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14064,'007169');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35031,'007170');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42198,'007172');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40215,'007173');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31249,'007174');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50277,'007175');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38048,'007176');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6142,'007178');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47185,'007179');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10204,'007181');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24189,'007182');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21072,'007188');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10205,'007189');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6144,'007184');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40216,'007190');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19311,'007185');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37341,'007187');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24190,'007191');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16237,'007192');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16238,'007196');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8297,'007197');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37344,'007199');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15089,'007200');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24194,'007214');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24196,'007201');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24197,'007202');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16239,'007213');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33074,'007215');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39097,'007204');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47187,'007205');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35033,'007206');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49231,'007208');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37345,'007209');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24198,'007210');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47188,'007211');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49232,'007212');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49233,'007216');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40222,'007217');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24199,'007218');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24201,'007220');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18911,'007221');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40223,'007219');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44250,'007076');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11039,'007078');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45181,'007079');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5254,'007082');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4097,'007083');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47190,'007088');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22245,'007089');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50278,'007090');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50279,'007091');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42201,'007092');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42202,'007093');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28167,'007094');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16240,'007097');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47191,'007098');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49234,'007102');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34023,'007103');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46254,'007104');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18185,'007107');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26163,'007110');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47192,'007111');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40224,'007113');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26164,'007114');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28169,'007115');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4100,'007116');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22246,'007119');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50280,'007118');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32084,'007121');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17211,'007123');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34201,'007125');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22247,'007128');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31251,'007129');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47193,'007130');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42204,'007131');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19314,'007132');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32086,'007133');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10206,'007134');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2076,'007135');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4101,'007136');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8298,'007137');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31253,'007147');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49236,'007148');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49237,'007149');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17213,'007000');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25243,'007001');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50281,'007002');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36057,'007003');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26165,'007004');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43165,'007006');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17214,'007007');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17215,'007008');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36058,'007009');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8299,'007010');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17217,'007011');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17216,'007012');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8301,'007013');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8300,'007014');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36059,'007005');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17218,'007015');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17220,'007016');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17221,'007018');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38052,'007019');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25244,'007023');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43175,'007026');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8306,'007027');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25245,'007028');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43166,'007030');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17224,'007029');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17225,'007031');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17226,'007032');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17227,'007033');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46256,'007036');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17228,'007038');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (36061,'007040');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25248,'007041');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25254,'007042');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43168,'007045');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8303,'007046');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43169,'007052');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32089,'007053');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32090,'007055');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32092,'007057');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15091,'007058');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43170,'007059');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17230,'007060');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25252,'007061');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15090,'007062');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8214,'007064');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8219,'007065');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43172,'007067');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23094,'007068');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9423,'007071');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47196,'008101');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21073,'008103');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24202,'008104');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20075,'008105');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24203,'008107');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1057,'008109');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49239,'008110');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45185,'008111');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39098,'008113');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23095,'008114');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34204,'008117');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47199,'008116');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42205,'008118');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47200,'008119');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34205,'008120');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28170,'008121');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16242,'008122');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34206,'008123');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24206,'008125');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10207,'008096');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28171,'008097');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10208,'008098');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (38053,'007073');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24207,'008126');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49240,'008127');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2077,'007074');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9427,'008128');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50283,'008129');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34208,'008130');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16243,'008133');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9428,'008134');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9430,'008136');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50284,'008138');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49243,'008139');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5256,'008140');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37351,'008141');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47203,'008142');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31254,'008143');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47204,'008145');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50285,'008146');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5257,'008147');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24209,'006986');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44251,'006987');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12129,'006988');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6149,'008148');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45187,'008149');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9432,'006992');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47206,'006993');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39100,'006994');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9433,'006995');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49244,'008030');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6151,'008032');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37352,'008033');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14068,'008036');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13089,'008037');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44252,'008038');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34211,'008040');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9437,'008041');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34213,'008043');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47210,'008045');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9438,'008047');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34214,'008048');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49246,'008049');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21074,'008057');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47211,'008050');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16245,'008051');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47212,'008052');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6152,'008053');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37353,'008054');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16246,'008058');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50286,'008055');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26166,'008056');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47213,'008059');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28172,'008060');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9439,'008061');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9440,'008062');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49247,'008063');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9441,'008066');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50287,'008067');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16247,'008069');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9442,'008070');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26167,'008071');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49248,'008073');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46255,'008075');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49249,'008076');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49250,'008077');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16248,'008078');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49251,'008079');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45188,'008080');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34218,'008081');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11040,'008082');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2079,'008083');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12131,'008084');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24211,'008085');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13090,'008087');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41097,'008088');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28173,'008089');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28174,'008090');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28175,'008091');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37354,'007967');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13091,'007968');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49252,'007969');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9443,'007970');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16249,'007972');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9444,'007973');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9445,'007974');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34221,'007975');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26168,'007976');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9446,'007977');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24214,'007978');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18908,'007979');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10210,'007982');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9447,'007983');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45189,'007984');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45190,'007985');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24215,'007986');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24217,'007989');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49255,'007988');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34223,'007990');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45191,'007991');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34224,'007992');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47215,'007993');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34225,'007994');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22249,'007998');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47217,'008000');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45192,'008002');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29095,'008004');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9449,'008005');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19318,'008006');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5905,'008007');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49257,'008008');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45193,'008009');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26169,'008010');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9450,'008012');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47218,'008014');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42206,'008017');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16250,'008018');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9451,'008019');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50290,'008020');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50289,'008021');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28176,'008022');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5259,'007930');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47219,'008023');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13092,'008024');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16251,'008025');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23096,'008026');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41098,'007931');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23097,'007932');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21075,'007913');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6153,'008027');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10211,'008028');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24218,'007914');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49259,'007915');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18187,'007916');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19319,'007911');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10212,'007912');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5260,'007933');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49260,'007934');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37355,'007935');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14070,'007936');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6154,'007937');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47220,'007917');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21076,'007918');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28177,'007938');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34227,'007939');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44256,'007940');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14071,'007941');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29096,'007944');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29097,'007945');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33075,'007921');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28178,'007922');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13094,'007923');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (41100,'007924');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47222,'007925');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22251,'007926');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29098,'007927');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9454,'007928');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12133,'007929');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34228,'007947');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24902,'007949');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2080,'007950');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34229,'007951');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24221,'007952');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24222,'007953');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49261,'007864');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14072,'007865');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16253,'007958');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37357,'007959');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49262,'007867');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5261,'007960');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49263,'007868');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47223,'007869');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37358,'007962');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16255,'007963');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42207,'007850');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46258,'007851');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37359,'007964');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49264,'007852');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42208,'007853');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44257,'007854');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16258,'007855');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16259,'007856');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28179,'007857');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50291,'007965');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10213,'007858');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13095,'007859');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6155,'007860');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44258,'007862');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16263,'007966');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37360,'007845');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10214,'007846');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6156,'007847');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37361,'007848');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26171,'007849');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49265,'007870');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23098,'007872');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49267,'007873');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26172,'007874');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16264,'007875');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16265,'007876');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5262,'007880');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24223,'007878');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16266,'007881');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19321,'007883');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37362,'007884');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16269,'007888');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24224,'007885');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37363,'007887');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46259,'007889');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9458,'007890');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44260,'007892');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37365,'007893');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34230,'007894');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47224,'007895');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37366,'007896');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44261,'007897');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34232,'007899');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21077,'007900');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50292,'007902');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2081,'007904');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23101,'007905');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26173,'007906');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50293,'007907');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50294,'007909');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44262,'007908');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13096,'007910');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45195,'007778');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16270,'007779');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16271,'007780');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6157,'007781');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (13097,'007782');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26174,'007783');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16910,'007863');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24225,'007785');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9460,'007786');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34233,'007787');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42212,'007788');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37367,'007789');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10215,'007790');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37368,'007791');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16272,'007784');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42213,'007792');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19322,'007793');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45196,'007794');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19323,'007795');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49269,'007796');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37369,'007797');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37370,'007798');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45197,'007800');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47225,'007801');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34234,'007802');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37371,'007803');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44263,'007804');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9463,'007805');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45198,'007806');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5263,'007807');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (23903,'007808');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2082,'007809');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9464,'007810');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31257,'007811');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34236,'007812');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24227,'007813');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34237,'007814');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31258,'007815');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2083,'007816');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26175,'007818');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47227,'007819');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49270,'007820');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2084,'007821');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37372,'007822');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40228,'007823');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9466,'007828');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47228,'007824');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40229,'007825');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26176,'007826');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9467,'007831');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16273,'007830');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49271,'007833');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49272,'007832');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47229,'007834');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33076,'007835');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12136,'007838');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28182,'007840');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37373,'007839');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34238,'007841');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9471,'007842');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24228,'007844');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (24229,'007697');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9472,'007698');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9473,'007699');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40230,'007700');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44264,'007701');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19324,'007702');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (3140,'007703');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34241,'007705');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34242,'007706');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34243,'007707');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16274,'007708');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12137,'007709');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37374,'007710');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9476,'007711');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37375,'007712');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26177,'007713');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (34246,'007715');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (17232,'007718');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47194,'007719');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9424,'007720');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37350,'007723');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9425,'007724');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15092,'007726');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44265,'007727');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (25255,'007728');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46260,'007729');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49273,'007737');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16275,'007731');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43177,'007732');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5264,'007738');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26178,'007733');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26179,'007734');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29099,'007739');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19325,'007740');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42215,'007735');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43178,'007736');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (44266,'007741');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50295,'007746');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12139,'007747');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5265,'007748');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37376,'007749');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1059,'007750');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27066,'007751');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12140,'007753');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (8308,'007754');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2085,'007755');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42216,'007757');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (39102,'007759');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42217,'007760');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (47230,'007761');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46143,'007763');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46146,'007764');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27021,'007765');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (43052,'007766');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32032,'007767');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46110,'007768');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (27025,'007769');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (32037,'007771');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (35034,'007772');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42218,'007773');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40231,'007774');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19327,'007612');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (22252,'007613');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (30043,'007614');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37377,'007615');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45201,'007619');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (42219,'007620');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45202,'007622');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33078,'007623');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31260,'007624');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31261,'007625');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (2086,'007628');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45203,'007629');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45204,'007630');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (45205,'007631');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (29100,'007632');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19331,'007633');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31262,'007635');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9480,'007636');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18192,'007637');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6158,'007638');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16278,'007640');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (18913,'007641');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (11042,'007642');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6160,'007646');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21078,'007647');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48095,'007648');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20078,'007649');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1061,'007650');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48096,'007651');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37378,'007652');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (1062,'007654');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (49275,'007655');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48905,'007656');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19333,'007657');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5266,'007658');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (5267,'007659');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50297,'007660');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37380,'007661');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48097,'007662');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20079,'007664');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (46263,'007665');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (6161,'007674');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10216,'007669');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16279,'007672');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10218,'007673');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (28183,'007675');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (26181,'007676');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (16280,'007678');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19334,'007679');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40233,'007680');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (40234,'007681');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (15093,'007682');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9483,'007683');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (48025,'007685');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20025,'007686');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20026,'007687');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20027,'007688');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31073,'007691');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20028,'007693');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (10219,'007695');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (37382,'007696');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12141,'007595');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (19335,'007594');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31263,'007599');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (12142,'007600');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (50298,'007601');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (21079,'007602');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (31264,'007603');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (14075,'007604');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (20081,'007607');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (9485,'007609');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (4103,'007611');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33004,'001335');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33011,'001824');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33012,'001823');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33016,'002664');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33023,'003145');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33024,'002965');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33025,'002842');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33034,'007337');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33037,'004338');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33038,'004132');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33039,'004060');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33043,'005098');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33053,'005434');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33054,'005491');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33060,'006123');
INSERT INTO "gestorfip"."relacionambitomunicipio" ("idmunicipio", "codambito") VALUES (33061,'006113');

GRANT SELECT ON ALL TABLES IN SCHEMA public TO localgisbackup;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO geopista;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;