DROP TABLE ESTADO;
DROP TABLE ESTADO_NOTIFICACION;
DROP TABLE ESTADO_RESOLUCION;
DROP TABLE EVENTOS;
DROP TABLE PARCELA;
DROP TABLE PERSONA_JURIDICO_FISICA;
DROP TABLE TIPO_ANEXO;
DROP TABLE TIPO_FINALIZACION;
DROP TABLE TIPO_INFORME;
DROP TABLE TIPO_LICENCIA;
DROP TABLE TIPO_NOTIFICACION;
DROP TABLE TIPO_OBRA;
DROP TABLE TIPO_TRAMITACION;
DROP TABLE VIA_NOTIFICACION;
DROP TABLE WORKFLOW;
DROP TABLE SOLICITUD_LICENCIA;
DROP TABLE ANEXO;
DROP TABLE DATOS_NOTIFICACION;
DROP TABLE EXPEDIENTE_LICENCIA;
DROP TABLE HISTORICO;
DROP TABLE INFORME_PRECEPTIVO;
DROP TABLE MEJORA;
DROP TABLE PARCELARIO;
DROP TABLE RESOLUCION;
DROP TABLE ACTIVIDAD;
DROP TABLE ALEGACION;
DROP TABLE ANEXOS_ALEGACION;
DROP TABLE ANEXOS_MEJORA;
DROP TABLE DOC_FORMALIZACION;
DROP TABLE NOTIFICACION;


CREATE TABLE ESTADO
(
  ID_ESTADO    NUMERIC(10)                       NOT NULL,
  DESCRIPCION  VARCHAR(64),
  OBSERVACION  VARCHAR(254),
  STEP         NUMERIC(1),
  CONSTRAINT id_estado_pkey PRIMARY KEY (ID_ESTADO)
);

CREATE TABLE ESTADO_OCUPACION
(
  ID_ESTADO    NUMERIC(10)                       NOT NULL,
  DESCRIPCION  VARCHAR(64),
  OBSERVACION  VARCHAR(254),
  STEP         NUMERIC(1),
  CONSTRAINT id_estado_ocupacion_pkey PRIMARY KEY (ID_ESTADO)
);

CREATE TABLE ESTADO_NOTIFICACION
(
  ID_ESTADO    NUMERIC(10)                       NOT NULL,
  DESCRIPCION  VARCHAR(64),
  OBSERVACION  VARCHAR(254),
  CONSTRAINT estado_notificacion_id_estado_pkey PRIMARY KEY (ID_ESTADO)
);


CREATE TABLE ESTADO_RESOLUCION
(
  ID_ESTADO    NUMERIC(10)                       NOT NULL,
  DESCRIPCION  VARCHAR(64),
  OBSERVACION  VARCHAR(254),
  CONSTRAINT estado_resolucion_id_estado_pkey PRIMARY KEY (ID_ESTADO)
);


CREATE TABLE EVENTOS
(
  ID_EVENTO       NUMERIC(14)                    NOT NULL,
  ID_SOLICITUD    NUMERIC(14)                    NOT NULL,
  NUM_EXPEDIENTE  VARCHAR(20)             NOT NULL,
  REVISADO        VARCHAR(1),
  REVISADO_POR    VARCHAR(164),
  CONTENT         VARCHAR(254),
  FECHA           TIMESTAMP,
  ID_ESTADO       numeric(10) NOT NULL,
  CONSTRAINT pk_id_evento PRIMARY KEY (ID_EVENTO)
);


--CREATE TABLE PARCELA
--(
--  ID_PARCELA     NUMERIC(14)                     NOT NULL,
--  REF_CATASTRAL  VARCHAR(128),
--  TIPO_VIA       VARCHAR(20),
--  NOMBRE_VIA     VARCHAR(20),
--  NUMERO_VIA     VARCHAR(20)
--);


CREATE TABLE PERSONA_JURIDICO_FISICA
(
  ID_PERSONA  NUMERIC(14)                        NOT NULL,
  DNI_CIF     VARCHAR(10)                 NOT NULL,
  NOMBRE      VARCHAR(32)                 NOT NULL,
  APELLIDO1   VARCHAR(32),
  APELLIDO2   VARCHAR(32),
  COLEGIO     VARCHAR(32),
  VISADO      VARCHAR(32),
  TITULACION  VARCHAR(128),
  CONSTRAINT id_persona_pkey PRIMARY KEY (ID_PERSONA)
);


CREATE TABLE tipo_anexo
(
  id_tipo_anexo numeric(10) NOT NULL,
  descripcion varchar(64),
  observacion varchar(254),
  CONSTRAINT id_tipo_anexo_pkey PRIMARY KEY (id_tipo_anexo)
);



CREATE TABLE tipo_finalizacion
(
  id_finalizacion numeric(10) NOT NULL,
  descripcion varchar(64),
  observacion varchar(254),
  CONSTRAINT id_finalizacion_pkey PRIMARY KEY (id_finalizacion)
);


CREATE TABLE tipo_informe
(
  id_tipo_informe numeric(10) NOT NULL,
  observacion varchar(254),
  descripcion varchar(64),
  CONSTRAINT id_tipo_informe_pkey PRIMARY KEY (id_tipo_informe)
);


CREATE TABLE tipo_licencia
(
  id_tipo_licencia numeric(10) NOT NULL,
  descripcion varchar(64),
  observacion varchar(254),
  CONSTRAINT id_tipo_licencia_pkey PRIMARY KEY (id_tipo_licencia)
);


CREATE TABLE tipo_notificacion
(
  id_tipo_notificacion numeric(10) NOT NULL,
  descripcion varchar(64),
  observacion varchar(254),
  CONSTRAINT id_tipo_notificacion_pkey PRIMARY KEY (id_tipo_notificacion)
);


CREATE TABLE tipo_obra
(
  id_tipo_obra numeric(10) NOT NULL,
  descripcion varchar(64),
  observacion varchar(254),
  CONSTRAINT id_tipo_obra_pkey PRIMARY KEY (id_tipo_obra)
);


CREATE TABLE tipo_tramitacion
(
  id_tramitacion numeric(10) NOT NULL,
  descripcion varchar(64),
  observacion varchar(254),
  plazo_entrega varchar(128),
  CONSTRAINT id_tramitacion_pkey PRIMARY KEY (id_tramitacion)
);


CREATE TABLE VIA_NOTIFICACION
(
  ID_VIA_NOTIFICACION  NUMERIC(10)               NOT NULL,
  OBSERVACION          VARCHAR(254),
  CONSTRAINT id_via_notificacion_pkey PRIMARY KEY (ID_VIA_NOTIFICACION)    
);


CREATE TABLE WORKFLOW
(
  ID_ESTADO       NUMERIC(10),
  ID_NEXTESTADO   NUMERIC(10),
  PLAZO           NUMERIC,
  ID_PLAZOESTADO  NUMERIC(10),
  EVENT_TEXT      VARCHAR(512),
  HITO_TEXT      VARCHAR(512),
  NOTIF_TEXT      VARCHAR(512),
  CRUTAACC        VARCHAR(254),
  CNOM            VARCHAR(254) 
);


CREATE TABLE SOLICITUD_LICENCIA
(
  ID_SOLICITUD        NUMERIC(14)                NOT NULL,
  ID_TIPO_LICENCIA    NUMERIC(10),
  ID_TIPO_OBRA        NUMERIC(10),
  PROPIETARIO         NUMERIC(14),
  REPRESENTANTE       NUMERIC(14),
  TECNICO             NUMERIC(14),
  PROMOTOR            NUMERIC(14),
  NUM_ADMINISTRATIVO  VARCHAR(32),
  CODIGO_ENTRADA      VARCHAR(32),
  UNIDAD_TRAMITADORA  VARCHAR(32),
  UNIDAD_DE_REGISTRO  VARCHAR(32),
  MOTIVO              VARCHAR(254),
  NOMBRE_COMERCIAL       VARCHAR(254),
  ASUNTO              VARCHAR(254),
  FECHA               TIMESTAMP,
  FECHA_ENTRADA       TIMESTAMP,
  TASA                NUMERIC(10,2),
  TIPO_VIA_AFECTA     VARCHAR(20),
  NOMBRE_VIA_AFECTA   VARCHAR(128),
  NUMERO_VIA_AFECTA   VARCHAR(10),
  PORTAL_AFECTA       VARCHAR(10),
  PLANTA_AFECTA       VARCHAR(10),
  LETRA_AFECTA        VARCHAR(5),
  CPOSTAL_AFECTA      VARCHAR(5),
  MUNICIPIO_AFECTA    VARCHAR(64),
  PROVINCIA_AFECTA    VARCHAR(32),
  OBSERVACIONES       VARCHAR(254),
  id_municipio numeric(5),
  CONSTRAINT id_solicitud_pkey PRIMARY KEY (ID_SOLICITUD)    
);



CREATE TABLE ANEXO
(
  id_anexo numeric(14) NOT NULL,
  id_solicitud numeric(14),
  id_inspeccion numeric(10),
  id_tipo_anexo numeric(10),
  url_fichero varchar(254),
  observacion varchar(254),
  CONSTRAINT id_anexo_pkey PRIMARY KEY (ID_ANEXO)    
);



CREATE TABLE DATOS_NOTIFICACION
(
  ID_PERSONA           NUMERIC(14)               NOT NULL,
  ID_SOLICITUD         NUMERIC(14)               NOT NULL,
  ID_VIA_NOTIFICACION  NUMERIC(14),
  FAX                  VARCHAR(20),
  TELEFONO             VARCHAR(20),
  MOVIL                VARCHAR(20),
  EMAIL                VARCHAR(32),
  TIPO_VIA             VARCHAR(20),
  NOMBRE_VIA           VARCHAR(128),
  NUMERO_VIA           VARCHAR(10),
  PORTAL               VARCHAR(10),
  PLANTA               VARCHAR(10),
  ESCALERA             VARCHAR(10),
  LETRA                VARCHAR(10),
  CPOSTAL              VARCHAR(5),
  MUNICIPIO            VARCHAR(64),
  PROVINCIA            VARCHAR(32),
  NOTIFICAR            VARCHAR(1),
  CONSTRAINT datos_notificacion_pkey PRIMARY KEY (ID_PERSONA,ID_SOLICITUD)   
);




CREATE TABLE EXPEDIENTE_LICENCIA
(
  ID_SOLICITUD             NUMERIC(14)           NOT NULL,
  NUM_EXPEDIENTE           VARCHAR(20)    NOT NULL,
  ID_TRAMITACION           NUMERIC(10),
  ID_FINALIZACION          NUMERIC(10),
  ID_ESTADO                NUMERIC(10),
  SERVICIO_ENCARGADO       VARCHAR(128),
  ASUNTO                   VARCHAR(128),
  SILENCIO_ADMINISTRATIVO  VARCHAR(1),
  FORMA_INICIO             VARCHAR(64),
  NUM_FOLIOS               NUMERIC(4),
  FECHA_APERTURA           TIMESTAMP,
  RESPONSABLE              VARCHAR(128),
  PLAZO_RESOLUCION         TIMESTAMP,
  HABILES                  VARCHAR(1),
  NUM_DIAS                 NUMERIC(3),
  OBSERVACIONES            VARCHAR(254),
  SILENCIO_EVENT           VARCHAR(1),
  PLAZO_EVENT              VARCHAR(1),  
  SILENCIO_TRIGGERED       VARCHAR(1),
  FECHA_CAMBIO_ESTADO      TIMESTAMP,
  VU       VARCHAR(1),
  CNAE       VARCHAR(16)
);




CREATE TABLE DATOS_OCUPACION
(
  ID_OCUPACION             NUMERIC(14)           NOT NULL,
  ID_SOLICITUD             NUMERIC(14)           NOT NULL,
  TIPO_OCUPACION           NUMERIC(10),
  NECESITA_OBRA       VARCHAR(1),
  NUM_EXPEDIENTE           VARCHAR(20),
  HORA_INICIO              TIMESTAMP,
  HORA_FIN              TIMESTAMP,
  NUM_MESAS               NUMERIC(6),
  NUM_SILLAS              NUMERIC(6),
  AREA_OCUPACION           VARCHAR(20),
  AFECTA_ACERA             VARCHAR(1),
  AFECTA_CALZADA             VARCHAR(1),
  AFECTA_APARCAMIENTO             VARCHAR(1),
  M2_ACERA               NUMERIC(6),
  M2_CALZADA               NUMERIC(6),
  M2_APARCAMIENTO               NUMERIC(6),
  CONSTRAINT datos_ocupacion_pkey PRIMARY KEY (ID_OCUPACION)   

);







CREATE TABLE historico
(
  id_historico numeric(14) NOT NULL,
  id_solicitud numeric(14) NOT NULL,
  num_expediente varchar(20) NOT NULL,
  id_estado numeric(10),
  fecha timestamp,
  usuario varchar(128),
  apunte varchar(254),
  sistema varchar(1) DEFAULT 1 NOT NULL,
  CONSTRAINT datos_historico_pkey PRIMARY KEY (ID_HISTORICO)   
);




CREATE TABLE INFORME_PRECEPTIVO
(
  ID_INFORME              NUMERIC(14)            NOT NULL,
  ID_TIPO_INFORME         NUMERIC(10),
  ID_SOLICITUD            NUMERIC(14)            NOT NULL,
  NUM_EXPEDIENTE          VARCHAR(20)     NOT NULL,
  DESC_SOLICITUD_INFORME  VARCHAR(128),
  FECHA_PETICION          TIMESTAMP,
  URL_FICHERO             VARCHAR(254),
  ACUERDO                 VARCHAR(1),
  FECHA_LLEGADA           TIMESTAMP,
  PLAZO_VENCIMIENTO       TIMESTAMP,
  HABILES                 VARCHAR(1),
  NUM_DIAS                NUMERIC(3),
  OBSERVACION             VARCHAR(254),
  VENCIMIENTO_FAVORABLE   VARCHAR(1),
  CONSTRAINT datos_informe_preceptivo_pkey PRIMARY KEY (ID_INFORME)   
);




CREATE TABLE MEJORA
(
  ID_MEJORA          NUMERIC(14)                 NOT NULL,
  ID_SOLICITUD       NUMERIC(14)                 NOT NULL,
  NUM_EXPEDIENTE     VARCHAR(20)          NOT NULL,
  OBSERVACION        VARCHAR(254),
  FECHA              TIMESTAMP,
  PLAZO_VENCIMIENTO  TIMESTAMP,
  HABILES            VARCHAR(1),
  NUM_DIAS           NUMERIC(3),
  CONSTRAINT datos_mejora_pkey PRIMARY KEY (ID_MEJORA)   
);


CREATE TABLE parcelario(ref_catastral varchar(32) NOT NULL,id_solicitud varchar(14) NOT NULL,tipo_via varchar(16),  nombre_via varchar(128),  numero varchar(16),  letra varchar(16),  bloque varchar(16),  escalera varchar(16),  planta varchar(16),  puerta varchar(16)) 


CREATE TABLE RESOLUCION
(
  ID_SOLICITUD       NUMERIC(14)                 NOT NULL,
  NUM_EXPEDIENTE     VARCHAR(20)          NOT NULL,
  ASUNTO             VARCHAR(128),
  ORGANO_APROBACION  VARCHAR(64),
  COLETILLA          VARCHAR(128),
  A_FAVOR            VARCHAR(1),
  FECHA_RESOLUCION   TIMESTAMP,
  CONFORME           VARCHAR(64),
  PLAZO_VENCIMIENTO  TIMESTAMP,
  HABILES            VARCHAR(1),
  NUM_DIAS           NUMERIC(3),
  VENCIMIENTO_OBRA   TIMESTAMP,
  ID_ESTADO          NUMERIC(10)
);




CREATE TABLE ACTIVIDAD
(
  ID_ACTIVIDAD          NUMERIC(14)              NOT NULL,
  ID_SOLICITUD          NUMERIC(14),
  ALQUILER              VARCHAR(1),
  ESCRITURA_PROPIEDAD   VARCHAR(1),
  ALTURA_TECHOS         NUMERIC(3),
  NUM_OPERARIOS         NUMERIC(3),
  AFORO                 NUMERIC(10),
  VENTILACION           VARCHAR(254),
  DESC_ELEM_ALMACENAJE  VARCHAR(254),
  ANTECEDENTE_OBRA      VARCHAR(20),
  CONSTRAINT pk_actividad PRIMARY KEY (id_actividad)
);



CREATE TABLE ALEGACION
(
  ID_ALEGACION       NUMERIC(14)                 NOT NULL,
  ID_SOLICITUD       NUMERIC(14)                 NOT NULL,
  NUM_EXPEDIENTE     VARCHAR(20)          NOT NULL,
  OBSERVACION        VARCHAR(254),
  FECHA              TIMESTAMP,
  PLAZO_VENCIMIENTO  TIMESTAMP,
  HABILES            VARCHAR(1),
  NUM_DIAS           NUMERIC(3),
  CONSTRAINT pk_alegacion PRIMARY KEY (id_alegacion)
);




CREATE TABLE ANEXOS_ALEGACION
(
  NUM_EXPEDIENTE  VARCHAR(20)             NOT NULL,
  ID_ALEGACION    NUMERIC(14)                    NOT NULL,
  ID_SOLICITUD    NUMERIC(14)                    NOT NULL,
  ID_ANEXO        NUMERIC(14)                    NOT NULL
);




CREATE TABLE ANEXOS_MEJORA
(
  NUM_EXPEDIENTE  VARCHAR(20)             NOT NULL,
  ID_MEJORA       NUMERIC(14)                    NOT NULL,
  ID_SOLICITUD    NUMERIC(14)                    NOT NULL,
  ID_ANEXO        NUMERIC(14)                    NOT NULL
);




CREATE TABLE DOC_FORMALIZACION
(
  ID_SOLICITUD      NUMERIC(14)                  NOT NULL,
  NUM_EXPEDIENTE    VARCHAR(20)           NOT NULL,
  ID_DOCUMENTO      NUMERIC(14)                  NOT NULL,
  URL_FICHERO       VARCHAR(254),
  FECHA_APROBACION  TIMESTAMP,
  CONDICIONES       VARCHAR(128),
  OBSERVACION       VARCHAR(254)
);


CREATE TABLE NOTIFICACION
(
  ID_NOTIFICACION       NUMERIC(14)              NOT NULL,
  ID_TIPO_NOTIFICACION  NUMERIC(10),
  NUM_EXPEDIENTE        VARCHAR(20),
  ID_ALEGACION          NUMERIC(14),
  ID_MEJORA             NUMERIC(14),
  ID_PERSONA            NUMERIC(14),
  ID_SOLICITUD          NUMERIC(14),
  NOTIFICADA_POR        NUMERIC(10),
  FECHA_CREACION        TIMESTAMP,
  FECHA_NOTIFICACION    TIMESTAMP,
  RESPONSABLE           VARCHAR(128),
  PLAZO_VENCIMIENTO     TIMESTAMP,
  HABILES               VARCHAR(1),
  NUM_DIAS              NUMERIC(3),
  OBSERVACIONES         VARCHAR(254),
  ID_ESTADO             NUMERIC(10)              NOT NULL,
  FECHA_REENVIO         TIMESTAMP,
  PROCEDENCIA           VARCHAR(64),
  CONSTRAINT pk_notificacion PRIMARY KEY (ID_NOTIFICACION)
);









