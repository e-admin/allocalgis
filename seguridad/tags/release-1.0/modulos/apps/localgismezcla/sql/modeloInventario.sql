-- Table: bien_revertible

-- DROP TABLE bien_revertible;

CREATE TABLE bien_revertible
(
  id numeric(10) NOT NULL,
  num_inventario character varying(50),
  fecha_inicio date,
  fecha_vencimiento date,
  fecha_transmision date,
  poseedor character varying(100),
  titulo_posesion character varying(100),
  condiciones_reversion character varying(255),
  detalles character varying(500),
  cat_transmision character varying(10),
  importe numeric(10,2),
  id_cuenta_amortizacion numeric(8),
  id_municipio numeric(5) NOT NULL,
  CONSTRAINT bien_revertible_pkey PRIMARY KEY (id),
  CONSTRAINT fk_amortizacion_br FOREIGN KEY (id_cuenta_amortizacion)
      REFERENCES amortizacion (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE bien_revertible OWNER TO geopista;

-- Index: fki_amortizacion_br

-- DROP INDEX fki_amortizacion_br;

CREATE INDEX fki_amortizacion_br
  ON bien_revertible
  USING btree
  (id_cuenta_amortizacion);
  -- Table: bien_revertible_bien

-- DROP TABLE bien_revertible_bien;

CREATE TABLE bien_revertible_bien
(
  id_bien integer NOT NULL,
  id_bien_revertible numeric(10) NOT NULL,
  CONSTRAINT bien_revertible_bien_fkey FOREIGN KEY (id_bien_revertible)
      REFERENCES bien_revertible (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE bien_revertible_bien OWNER TO geopista;


-- Table: observaciones_bien_revertible

-- DROP TABLE observaciones_bien_revertible;

CREATE TABLE observaciones_bien_revertible
(
  id integer NOT NULL,
  descripcion character varying(1000),
  fecha timestamp without time zone,
  fecha_ultima_modificacion timestamp without time zone,
  id_bien integer NOT NULL,
  CONSTRAINT obs_pkey PRIMARY KEY (id),
  CONSTRAINT obs_inventario_fkey FOREIGN KEY (id_bien)
      REFERENCES bien_revertible (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=TRUE);
ALTER TABLE observaciones_bien_revertible OWNER TO geopista;

-- Table: lote

-- DROP TABLE lote;

CREATE TABLE lote
(
  id_lote numeric(10) NOT NULL,
  nombre_lote character varying(100),
  fecha_alta date,
  fecha_baja date,
  fecha_ultima_modificacion date,
  seguro character varying(100),
  descripcion character varying(1000),
  destino character varying(255),
  id_municipio numeric(5) NOT NULL,
  CONSTRAINT lote_pkey PRIMARY KEY (id_lote)
)
WITH (OIDS=FALSE);
ALTER TABLE lote OWNER TO geopista;

-- Table: lote_bien

-- DROP TABLE lote_bien;

CREATE TABLE lote_bien
(
  id_bien integer NOT NULL,
  id_lote numeric(10) NOT NULL,
  CONSTRAINT lote_bien_pkey PRIMARY KEY (id_lote, id_bien),
  CONSTRAINT lote_bien_fkey2 FOREIGN KEY (id_lote)
      REFERENCES lote (id_lote) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE lote_bien OWNER TO geopista;

-- Table: anexo_lote

-- DROP TABLE anexo_lote;

CREATE TABLE anexo_lote
(
  id_documento integer NOT NULL,
  id_lote numeric(10) NOT NULL,
  CONSTRAINT anexo_lote_pkey PRIMARY KEY (id_documento, id_lote)
)
WITH (OIDS=TRUE);
ALTER TABLE anexo_lote OWNER TO geopista;

ALTER TABLE bienes_inventario
   ADD COLUMN fecha_aprobacion_pleno timestamp without time zone;

INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Lotes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Lotes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Lotes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Lotes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Lotes');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),11150,'4',CURRVAL('seq_dictionary'),7,1,100300);
  
INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Tipo lotes',4);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo lotes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo lotes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo lotes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo lotes');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo lotes');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'',CURRVAL('seq_dictionary'),4);  
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Muebles no comprendidos en los Epigrafes anteriores');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Muebles no comprendidos en los Epigrafes anteriores');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Muebles no comprendidos en los Epigrafes anteriores');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Muebles no comprendidos en los Epigrafes anteriores');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Muebles no comprendidos en los Epigrafes anteriores');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'13',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-1);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Histórico/Artísticos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Histórico/Artísticos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Histórico/Artísticos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Histórico/Artísticos');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Histórico/Artísticos');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'7',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-2);
  
INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Transmision',4);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Transmision');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Transmision');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Transmision');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Transmision');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Transmision');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'',CURRVAL('seq_dictionary'),4);  
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','<Sin asignar>');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]<Sin asignar>');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]<Sin asignar>');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]<Sin asignar>');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]<Sin asignar>');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'0',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-1);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Atribución de la Ley');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Atribución de la Ley');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Atribución de la Ley');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Atribución de la Ley');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Atribución de la Ley');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'1',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-2);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Donación, Herencia o Legado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Donación, Herencia o Legado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Donación, Herencia o Legado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Donación, Herencia o Legado');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Donación, Herencia o Legado');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'2',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-3);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Ocupación');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Ocupacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Ocupacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Ocupacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Ocupacion');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'3',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-4);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Onerosa Expropiación');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Onerosa Expropiacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Onerosa Expropiacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Onerosa Expropiacion');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Onerosa Expropiacion');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'4',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-5);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Onerosa Costo');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Onerosa Costo');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Onerosa Costo');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Onerosa Costo');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Onerosa Costo');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'5',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-6);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Prescripción Adquisitiva');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Prescripcion Adquisitiva');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Prescripcion Adquisitiva');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Prescripcion Adquisitiva');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Prescripcion Adquisitiva');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'6',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-7);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Otros Modos Conforme al Ordenamiento Jurídico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Otros Modos Conforme al Ordenamiento Juridico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Otros Modos Conforme al Ordenamiento Juridico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Otros Modos Conforme al Ordenamiento Juridico');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Otros Modos Conforme al Ordenamiento Juridico');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'7',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-8);
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Inmemorial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Inmemorial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Inmemorial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Inmemorial');
INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Inmemorial');
INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'8',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-9);
        
----Estos cambios los añado por que se ha añadido versionado en la tabla bienes inventario 
----supongo que también estarán en otro sitio


ALTER TABLE bienes_inventario ADD CONSTRAINT bi_revision_actual UNIQUE (id, revision_actual);


ALTER TABLE credito_derecho DROP CONSTRAINT credito_derecho_fkey;

ALTER TABLE credito_derecho
  ADD CONSTRAINT credito_derecho_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE derechos_reales DROP CONSTRAINT derechos_reales_fkey;

ALTER TABLE derechos_reales
  ADD CONSTRAINT derechosreales_fk FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE CASCADE;


ALTER TABLE inmuebles DROP CONSTRAINT inmuebles_fkey;

ALTER TABLE inmuebles
  ADD CONSTRAINT inmuebles_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE mejoras_inventario DROP CONSTRAINT mejoras_inventario_fkey;

ALTER TABLE mejoras_inventario
  ADD CONSTRAINT mejoras_inventario_fkey FOREIGN KEY (id_bien, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE muebles DROP CONSTRAINT muebles_fkey;

ALTER TABLE muebles
  ADD CONSTRAINT muebles_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE observaciones_inventario DROP CONSTRAINT observaciones_inventario_fkey;

ALTER TABLE observaciones_inventario
  ADD CONSTRAINT observaciones_inventario_fkey FOREIGN KEY (id_bien, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE refcatastrales_inventario DROP CONSTRAINT refcatastrales_inventario_fkey;

ALTER TABLE refcatastrales_inventario
  ADD CONSTRAINT refcatastrales_inventario_fkey FOREIGN KEY (id_bien, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE semoviente DROP CONSTRAINT semoviente_fkey;

ALTER TABLE semoviente
  ADD CONSTRAINT semoviente_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE usos_funcionales_inventario DROP CONSTRAINT usos_funcionales_inventario_fkey;

ALTER TABLE usos_funcionales_inventario
  ADD CONSTRAINT usos_funcionales_inventario_fkey FOREIGN KEY (id_bien, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE valor_mobiliario DROP CONSTRAINT valor_mobiliario_fkey;

ALTER TABLE valor_mobiliario
  ADD CONSTRAINT valor_mobiliario_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE vehiculo DROP CONSTRAINT vehiculo_fkey;

ALTER TABLE vehiculo
  ADD CONSTRAINT vehiculo_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE vias_inventario DROP CONSTRAINT vias_inventario_fkey;

ALTER TABLE vias_inventario
  ADD CONSTRAINT vias_inventario_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;




ALTER TABLE bien_revertible_bien DROP CONSTRAINT bien_revertible_bien_fkey;

ALTER TABLE lote_bien DROP CONSTRAINT lote_bien_fkey2;

ALTER TABLE bienes_inventario DROP CONSTRAINT bienes_inventario_pkey cascade;


ALTER TABLE inmuebles_rusticos DROP CONSTRAINT inmuebles_rusticos_pkey;

ALTER TABLE inmuebles_rusticos
  ADD CONSTRAINT inmuebles_rusticos_pkey PRIMARY KEY(id, revision_actual);

ALTER TABLE inmuebles_urbanos DROP CONSTRAINT inmuebles_urbanos_pkey;

-- ALTER TABLE inmuebles_urbanos ADD COLUMN revision_actual numeric(10);
-- ALTER TABLE inmuebles_urbanos ADD COLUMN revision_expirada numeric(10);

ALTER TABLE inmuebles_urbanos
  ADD CONSTRAINT inmuebles_urbanos_pkey PRIMARY KEY(id, revision_actual);


ALTER TABLE refcatastrales_inventario DROP CONSTRAINT refcatastrales_inventario_pkey;

ALTER TABLE refcatastrales_inventario
  ADD CONSTRAINT refcatastrales_inventario_pkey PRIMARY KEY(id, revision_actual);


ALTER TABLE usos_funcionales_inventario DROP CONSTRAINT usos_funcionales_inventario_pkey;

ALTER TABLE usos_funcionales_inventario
  ADD CONSTRAINT usos_funcionales_inventario_pkey PRIMARY KEY(id, revision_actual);

--Versionado de la tabla bien revertible

ALTER TABLE observaciones_bien_revertible drop constraint obs_pkey;
ALTER TABLE observaciones_bien_revertible drop constraint  obs_inventario_fkey;


ALTER TABLE observaciones_bien_revertible ADD COLUMN revision_actual numeric(10)NOT NULL DEFAULT 0;
ALTER TABLE observaciones_bien_revertible ADD COLUMN revision_expirada numeric(10) DEFAULT 9999999999; 

-- TODO. Tiene constraint esta tabla??
--alter table bien_revertible_bien drop constraint bien_revertible_bien_fkey;

ALTER TABLE bien_revertible_bien ADD COLUMN revision numeric(10)NOT NULL DEFAULT 0;

ALTER TABLE bien_revertible ADD COLUMN revision_actual numeric(10)NOT NULL DEFAULT 0;
ALTER TABLE bien_revertible ADD COLUMN revision_expirada numeric(10) DEFAULT 9999999999; 

ALTER TABLE bien_revertible DROP CONSTRAINT bien_revertible_pkey;


ALTER TABLE bien_revertible ADD CONSTRAINT bien_revertible_pkey  primary key (id, revision_actual);

ALTER TABLE observaciones_bien_revertible add constraint obs_pkey primary key ( id, revision_actual);

alter table observaciones_bien_revertible add constraint obs_inventario_fkey 
FOREIGN KEY (id_bien, revision_actual) references bien_revertible(id, revision_actual);

alter table bien_revertible_bien  add constraint bien_revertible_bien_fkey 
FOREIGN KEY (id_bien_revertible, revision) references bien_revertible(id, revision_actual);

CREATE SEQUENCE seq_inventario_bien_revertible
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;  


insert into tables_inventario values (12009, 'bien_revertible');

--Diferencias entre el esquema y las tablas

ALTER TABLE amortizacion ALTER porcentaje TYPE numeric(6,2);
ALTER TABLE amortizacion ALTER total_amortizado TYPE numeric(16,2);

ALTER TABLE credito_derecho ALTER importe TYPE numeric(16,2);

ALTER TABLE derechos_reales ALTER  coste TYPE numeric(16,2);
ALTER TABLE derechos_reales ALTER  valor TYPE numeric(16,2);
ALTER TABLE derechos_reales ALTER  importe_frutos TYPE numeric(16,2);

ALTER TABLE inmuebles ALTER superficie_registral_suelo type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_catastral_suelo type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_real_suelo type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_registral_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_catastral_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_real_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   valor_derechos_favor type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_derechos_contra type numeric(16,2);
ALTER TABLE inmuebles ALTER   superficie_ocupada_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_construida_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_enplanta_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   valor_adquisicion_suelo type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_catastral_suelo type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_actual_suelo type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_adquisicion_construccion type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_catastral_construccion type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_actual_construccion type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_adquisicion_inmueble type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_actual_inmueble type numeric(16,2);
ALTER TABLE inmuebles ALTER   importe_frutos type numeric(16,2);
ALTER TABLE inmuebles ALTER     edificabilidad type numeric(10,2);


ALTER TABLE muebles ALTER  coste_adquisicion type numeric(16,2);
ALTER TABLE muebles ALTER  valor_actual type numeric(16,2);
ALTER TABLE muebles ALTER  importe_frutos type numeric(16,2);

ALTER TABLE semoviente ALTER  importe_frutos type numeric(16,2);
ALTER TABLE semoviente ALTER    coste_adquisicion type numeric(16,2);
ALTER TABLE semoviente ALTER    valor_actual type numeric(16,2);

ALTER TABLE valor_mobiliario ALTER  coste_adquisicion type numeric(16,2);
ALTER TABLE valor_mobiliario ALTER  valor_actual type numeric(16,2);
ALTER TABLE valor_mobiliario ALTER  precio type numeric(16,2);
ALTER TABLE valor_mobiliario ALTER  capital type numeric(16,2);
ALTER TABLE valor_mobiliario ALTER  importe_frutos type numeric(16,2);

ALTER TABLE vehiculo ALTER  importe_frutos  type numeric(16,2);
ALTER TABLE vehiculo ALTER coste_adquisicion  type numeric(16,2);
ALTER TABLE Vehiculo ALTER valor_actual  type numeric(16,2);

ALTER TABLE vias_inventario ALTER  metros_pavimentados  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   metros_no_pavimentados  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   zonas_verdes  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   longitud  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   ancho  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   valor_actual  type numeric(16,2);


ALTER TABLE seguros ALTER   prima type numeric(14,2);
ALTER TABLE seguros ALTER   poliza type numeric(14,2);

ALTER TABLE mejoras_inventario ALTER   importe type numeric(16,2);

ALTER TABLE usos_funcionales_inventario alter  superficie type numeric(10,2);

alter table bien_revertible alter importe type numeric(12,2);




insert into documento_tipos values (1, UPPER('ez'),upper('application/andrew-inset'));
insert into documento_tipos values (2, UPPER('hqx'),upper('application/mac-binhex40'));
insert into documento_tipos values (3, UPPER('mac'),upper('application/mac-compactpro'));
insert into documento_tipos values (4, UPPER('doc'),upper('application/msword'));
insert into documento_tipos values (5, UPPER('bin'),upper('application/octet-stream'));
insert into documento_tipos values (6, UPPER('dms'),upper('application/octet-stream'));
insert into documento_tipos values (7, UPPER('lha'),upper('application/octet-stream'));
insert into documento_tipos values (8, UPPER('lzh'),upper('application/octet-stream'));
insert into documento_tipos values (9, UPPER('exe'),upper('application/octet-stream'));
insert into documento_tipos values (10, UPPER('rar'),upper('application/x-rar-compressed'));
insert into documento_tipos values (11, UPPER('class'),upper('application/octet-stream'));
insert into documento_tipos values (12, UPPER('so'),upper('application/octet-stream'));
insert into documento_tipos values (13, UPPER('dll'),upper('application/octet-stream'));
insert into documento_tipos values (14, UPPER('oda'),upper('application/oda'));
insert into documento_tipos values (15, UPPER('pdf'),upper('application/pdf'));
insert into documento_tipos values (16, UPPER('ai'),upper('application/postscript'));
insert into documento_tipos values (17, UPPER('eps'),upper('application/postscript'));
insert into documento_tipos values (18, UPPER('ps'),upper('application/postscript'));
insert into documento_tipos values (19, UPPER('smi'),upper('application/smil'));
insert into documento_tipos values (20, UPPER('smil'),upper('application/smil'));
insert into documento_tipos values (21, UPPER('mif'),upper('application/vnd.mif'));
insert into documento_tipos values (22, UPPER('xls'),upper('application/vnd.ms-excel'));
insert into documento_tipos values (23, UPPER('ppt'),upper('application/vnd.ms-powerpoint'));
insert into documento_tipos values (24, UPPER('wbxml'),upper('application/vnd.wap.wbxml'));
insert into documento_tipos values (25, UPPER('wmlc'),upper('application/vnd.wap.wmlc'));
insert into documento_tipos values (26, UPPER('wmlsc'),upper('application/vnd.wap.wmlscriptc'));
insert into documento_tipos values (27, UPPER('bcpio'),upper('application/x-bcpio'));
insert into documento_tipos values (28, UPPER('vcd'),upper('application/x-cdlink'));
insert into documento_tipos values (29, UPPER('pgn'),upper('application/x-chess-pgn'));
insert into documento_tipos values (30, UPPER('cpio'),upper('application/x-cpio'));
insert into documento_tipos values (31, UPPER('csh'),upper('application/x-csh'));
insert into documento_tipos values (32, UPPER('dcr'),upper('application/x-director'));
insert into documento_tipos values (33, UPPER('dir'),upper('application/x-director'));
insert into documento_tipos values (34, UPPER('dxr'),upper('application/x-director'));
insert into documento_tipos values (35, UPPER('dvi'),upper('application/x-dvi'));
insert into documento_tipos values (36, UPPER('spl'),upper('application/x-futuresplash'));
insert into documento_tipos values (37, UPPER('gtar'),upper('application/x-gtar'));
insert into documento_tipos values (38, UPPER('hdf'),upper('application/x-hdf'));
insert into documento_tipos values (39, UPPER('js'),upper('application/x-javascript'));
insert into documento_tipos values (40, UPPER('skp'),upper('application/x-koan'));
insert into documento_tipos values (41, UPPER('skd'),upper('application/x-koan'));
insert into documento_tipos values (42, UPPER('skt'),upper('application/x-koan'));
insert into documento_tipos values (43, UPPER('skm'),upper('application/x-koan'));
insert into documento_tipos values (44, UPPER('latex'),upper('application/x-latex'));
insert into documento_tipos values (45, UPPER('nc'),upper('application/x-netcdf'));
insert into documento_tipos values (46, UPPER('cdf'),upper('application/x-netcdf'));
insert into documento_tipos values (47, UPPER('sh'),upper('application/x-sh'));
insert into documento_tipos values (48, UPPER('shar'),upper('application/x-shar'));
insert into documento_tipos values (49, UPPER('swf'),upper('application/x-shockwave-flash'));
insert into documento_tipos values (50, UPPER('sit'),upper('application/x-stuffit'));
insert into documento_tipos values (51, UPPER('sv4cpio'),upper('application/x-sv4cpio'));
insert into documento_tipos values (52, UPPER('sv4crc'),upper('application/x-sv4crc'));
insert into documento_tipos values (53, UPPER('tar'),upper('application/x-tar'));
insert into documento_tipos values (54, UPPER('tcl'),upper('application/x-tcl'));
insert into documento_tipos values (55, UPPER('tex'),upper('application/x-tex'));
insert into documento_tipos values (56, UPPER('texinfo'),upper('application/x-texinfo'));
insert into documento_tipos values (57, UPPER('texi'),upper('application/x-texinfo'));
insert into documento_tipos values (58, UPPER('t'),upper('application/x-troff'));
insert into documento_tipos values (59, UPPER('tr'),upper('application/x-troff'));
insert into documento_tipos values (60, UPPER('roff'),upper('application/x-troff'));
insert into documento_tipos values (61, UPPER('man'),upper('application/x-troff-man'));
insert into documento_tipos values (62, UPPER('me'),upper('application/x-troff-me'));
insert into documento_tipos values (63, UPPER('ms'),upper('application/x-troff-ms'));
insert into documento_tipos values (64, UPPER('ustar'),upper('application/x-ustar'));
insert into documento_tipos values (65, UPPER('src'),upper('application/x-wais-source'));
insert into documento_tipos values (66, UPPER('xhtml'),upper('application/xhtml+xml'));
insert into documento_tipos values (67, UPPER('xht'),upper('application/xhtml+xml'));
insert into documento_tipos values (68, UPPER('zip'),upper('application/zip'));
insert into documento_tipos values (69, UPPER('au'),upper('audio/basic'));
insert into documento_tipos values (70, UPPER('snd'),upper('audio/basic'));
insert into documento_tipos values (71, UPPER('mid'),upper('audio/midi'));
insert into documento_tipos values (72, UPPER('midi'),upper('audio/midi'));
insert into documento_tipos values (73, UPPER('kar'),upper('audio/midi'));
insert into documento_tipos values (74, UPPER('mpga'),upper('audio/mpeg'));
insert into documento_tipos values (75, UPPER('mp2'),upper('audio/mpeg'));
insert into documento_tipos values (76, UPPER('mp3'),upper('audio/mpeg'));
insert into documento_tipos values (77, UPPER('aif'),upper('audio/x-aiff'));
insert into documento_tipos values (78, UPPER('aiff'),upper('audio/x-aiff'));
insert into documento_tipos values (79, UPPER('aifc'),upper('audio/x-aiff'));
insert into documento_tipos values (80, UPPER('m3u'),upper('audio/x-mpegurl'));
insert into documento_tipos values (81, UPPER('ram'),upper('audio/x-pn-realaudio'));
insert into documento_tipos values (82, UPPER('rm'),upper('audio/x-pn-realaudio'));
insert into documento_tipos values (83, UPPER('rpm'),upper('audio/x-pn-realaudio-plugin'));
insert into documento_tipos values (84, UPPER('ra'),upper('audio/x-realaudio'));
insert into documento_tipos values (85, UPPER('wav'),upper('audio/x-wav'));
insert into documento_tipos values (86, UPPER('pdb'),upper('chemical/x-pdb'));
insert into documento_tipos values (87, UPPER('xyz'),upper('chemical/x-xyz'));
insert into documento_tipos values (89, UPPER('bmp'),upper('image/bmp'));
insert into documento_tipos values (90, UPPER('gif'),upper('image/gif'));
insert into documento_tipos values (91, UPPER('ief'),upper('image/ief'));
insert into documento_tipos values (92, UPPER('jpeg'),upper('image/jpeg'));
insert into documento_tipos values (93, UPPER('jpg'),upper('image/jpeg'));
insert into documento_tipos values (94, UPPER('jpe'),upper('image/jpeg'));
insert into documento_tipos values (95, UPPER('png'),upper('image/png'));
insert into documento_tipos values (96, UPPER('tiff'),upper('image/tiff'));
insert into documento_tipos values (97, UPPER('tif'),upper('image/tiff'));
insert into documento_tipos values (98, UPPER('djvu'),upper('image/vnd.djvu'));
insert into documento_tipos values (99, UPPER('djv'),upper('image/vnd.djvu'));
insert into documento_tipos values (100, UPPER('wbmp'),upper('image/vnd.wap.wbmp'));
insert into documento_tipos values (101, UPPER('ras'),upper('image/x-cmu-raster'));
insert into documento_tipos values (102, UPPER('pnm'),upper('image/x-portable-anymap'));
insert into documento_tipos values (103, UPPER('pbm'),upper('image/x-portable-bitmap'));
insert into documento_tipos values (104, UPPER('pgm'),upper('image/x-portable-graymap'));
insert into documento_tipos values (105, UPPER('ppm'),upper('image/x-portable-pixmap'));
insert into documento_tipos values (106, UPPER('rgb'),upper('image/x-rgb'));
insert into documento_tipos values (107, UPPER('xbm'),upper('image/x-xbitmap'));
insert into documento_tipos values (108, UPPER('xpm'),upper('image/x-xpixmap'));
insert into documento_tipos values (109, UPPER('xwd'),upper('image/x-xwindowdump'));
insert into documento_tipos values (110, UPPER('igs'),upper('model/iges'));
insert into documento_tipos values (111, UPPER('iges'),upper('model/iges'));
insert into documento_tipos values (112, UPPER('msh'),upper('model/mesh'));
insert into documento_tipos values (113, UPPER('mesh'),upper('model/mesh'));
insert into documento_tipos values (114, UPPER('silo'),upper('model/mesh'));
insert into documento_tipos values (115, UPPER('wrl'),upper('model/vrml'));
insert into documento_tipos values (116, UPPER('vrml'),upper('model/vrml'));
insert into documento_tipos values (117, UPPER('css'),upper('text/css'));
insert into documento_tipos values (118, UPPER('html'),upper('text/html'));
insert into documento_tipos values (119, UPPER('htm'),upper('text/html'));
insert into documento_tipos values (120, UPPER('asc'),upper('text/plain'));
insert into documento_tipos values (121, UPPER('txt'),upper('text/plain'));
insert into documento_tipos values (122, UPPER('rtx'),upper('text/richtext'));
insert into documento_tipos values (123, UPPER('rtf'),upper('text/rtf'));
insert into documento_tipos values (124, UPPER('sgml'),upper('text/sgml'));
insert into documento_tipos values (125, UPPER('sgm'),upper('text/sgml'));
insert into documento_tipos values (126, UPPER('tsv'),upper('text/tab-separated-values'));
insert into documento_tipos values (127, UPPER('wml'),upper('text/vnd.wap.wml'));
insert into documento_tipos values (128, UPPER('wmls'),upper('text/vnd.wap.wmlscript'));
insert into documento_tipos values (129, UPPER('etx'),upper('text/x-setext'));
insert into documento_tipos values (130, UPPER('xsl'),upper('text/xml'));
insert into documento_tipos values (131, UPPER('xml'),upper('text/xml'));
insert into documento_tipos values (132, UPPER('mpeg'),upper('video/mpeg'));
insert into documento_tipos values (133, UPPER('mpg'),upper('video/mpeg'));
insert into documento_tipos values (134, UPPER('mpe'),upper('video/mpeg'));
insert into documento_tipos values (135, UPPER('qt'),upper('video/quicktime'));
insert into documento_tipos values (136, UPPER('mov'),upper('video/quicktime'));
insert into documento_tipos values (137, UPPER('mxu'),upper('video/vnd.mpegurl'));
insert into documento_tipos values (138, UPPER('avi'),upper('video/x-msvideo'));
insert into documento_tipos values (139, UPPER('movie'),upper('video/x-sgi-movie'));
insert into documento_tipos values (140, UPPER('ice'),upper('x-conference/x-cooltalk'));


--Modificacion para añadir el tipo de arrendamiento
ALTER TABLE credito_derecho  ADD COLUMN arrendamiento numeric(1) default 0;


--Modificaciones versionado bien_revertible
alter table bien_Revertible add borrado character varying(1) DEFAULT '0'::character varying;
alter table bien_Revertible add   fecha_baja timestamp without time zone;

--Modificaciones realizadas al cambiar el id del documento añadiendo el código hash

alter table documento alter column id_documento type character varying(100);
alter table anexofeature alter column id_documento type character varying(100);
alter table anexo_inventario alter column id_documento type character varying(100);
alter table anexo_lote alter column id_documento type character varying(100);

ALTER TABLE civil_work_document_thumbnail DROP CONSTRAINT civil_work_document_thumbnail_fk;
alter table civil_work_document_thumbnail alter column id_document type character varying(100);
alter table civil_work_documents alter column id_document type character varying(100);
ALTER TABLE civil_work_document_thumbnail add constraint civil_work_document_thumbnail_fk FOREIGN KEY (id_document, id_warning) references civil_work_documents (id_document, id_warning) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;

--Modificaciones para que los formularios se realicen a través de una select
GRANT SELECT ON TABLE bienes_inventario to consultas;
GRANT SELECT ON TABLE credito_derecho to consultas;
GRANT SELECT ON TABLE versiones to consultas;
GRANT SELECT ON TABLE tables_inventario to consultas;
GRANT SELECT ON TABLE domains to consultas;
GRANT SELECT ON TABLE domainnodes to consultas;
GRANT SELECT ON TABLE contable to consultas;
GRANT SELECT ON TABLE entidades_municipios to consultas;
GRANT SELECT ON TABLE vehiculo to consultas;
GRANT SELECT ON TABLE semoviente to consultas;
GRANT SELECT ON TABLE valor_mobiliario to consultas;
GRANT SELECT ON TABLE derechos_reales to consultas;
GRANT SELECT ON TABLE muebles to consultas;
GRANT SELECT ON TABLE vias_inventario to consultas;
GRANT SELECT ON TABLE inmuebles to consultas;
GRANT SELECT ON TABLE bien_revertible to consultas;
GRANT SELECT ON TABLE lote to consultas;
GRANT SELECT ON TABLE seguros_inventario to consultas;


--Función que devuelve la traduccion
CREATE OR REPLACE FUNCTION getdictionarydescription(domainname text, pattern text, identidad integer, locale text)
  RETURNS text AS
$BODY$select dictionary.traduccion from Domains inner join DomainNodes on 
domains.id=domainNodes.id_domain inner join dictionary on 
domainNodes.id_description = dictionary.id_vocablo left outer join entidades_municipios on
domainNodes.id_municipio=entidades_municipios.id_entidad where domains.name=$1 and
domainNodes.pattern=$2 and
(domainNodes.id_municipio is null or entidades_municipios.id_municipio=$3) and 
dictionary.locale=$4 order by domainnodes.id_municipio;$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getdictionarydescription(text, text, integer, text) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, integer, text) TO public;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, integer, text) TO postgres;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, integer, text) TO consultas;

ALTER TABLE inmuebles ALTER refcat TYPE character varying(20);
ALTER TABLE refcatastrales_inventario ALTER ref_catastral TYPE character varying(20);


-- Para que vayan mejor las consultas

CREATE INDEX indx_id_tabla_versionada ON versiones (id_table_versionada);
CREATE UNIQUE INDEX INDX_BI_NUMINVENTARIO  ON bienes_inventario (numinventario, id_municipio, revision_actual);

-- Nuevos campos bienes revertibles
alter table bien_Revertible add   fecha_alta timestamp without time zone;
alter table bien_Revertible add   fecha_ultima_modificacion timestamp without time zone;
ALTER TABLE bien_revertible ADD COLUMN nombre character varying(255);
ALTER TABLE bien_revertible ADD COLUMN organizacion character varying(50);
ALTER TABLE bien_revertible ADD COLUMN fecha_aprobacion_pleno timestamp without time zone;
ALTER TABLE bien_revertible ADD COLUMN descripcion_bien character varying(255);
ALTER TABLE bien_revertible ADD COLUMN fecha_adquisicion timestamp without time zone;
ALTER TABLE bien_revertible ADD COLUMN adquisicion character varying(2);
ALTER TABLE bien_revertible ADD COLUMN diagnosis character varying(2);
ALTER TABLE bien_revertible ADD COLUMN patrimonio_municipal_suelo character varying(1);
ALTER TABLE bien_revertible ADD COLUMN clase character varying(2);

CREATE TABLE anexo_bien_revertible
(
  id_documento character varying(100) NOT NULL,
  id_bien_revertible numeric(10) NOT NULL,
  CONSTRAINT anexo_bien_revertible_pkey PRIMARY KEY (id_documento, id_bien_revertible)
);
ALTER TABLE anexo_bien_revertible OWNER TO geopista;


ALTER TABLE seguros_inventario ALTER id TYPE numeric(8,0);
ALTER TABLE bienes_inventario ALTER id_seguro TYPE numeric(8,0);
ALTER TABLE bien_revertible  ADD COLUMN id_seguro numeric(8,0) references seguros_inventario (id) on delete set null;

ALTER TABLE inmuebles ADD COLUMN valor_catastral_inmueble numeric(16,2);
ALTER TABLE inmuebles ADD COLUMN anio_valor_catastral integer;
ALTER TABLE inmuebles ADD COLUMN edificabilidad_descripcion character varying(255);
ALTER TABLE inmuebles ADD COLUMN fecha_adquisicion_obra timestamp without time zone;

ALTER TABLE lote  ADD COLUMN id_seguro numeric(8,0) references seguros_inventario (id) on delete set null;

ALTER TABLE credito_derecho ADD COLUMN clase character varying(3);
ALTER TABLE credito_derecho ADD COLUMN subclase character varying(3);
ALTER TABLE derechos_reales ADD COLUMN clase character varying(3);
ALTER TABLE vias_inventario ADD COLUMN clase character varying(3);
ALTER TABLE muebles ADD COLUMN clase character varying(3);
ALTER TABLE inmuebles ADD COLUMN clase character varying(3);

ALTER TABLE usos_funcionales_inventario ALTER uso TYPE character varying(100);

ALTER TABLE seguros_inventario ALTER poliza TYPE numeric(14,0);
ALTER TABLE seguros ALTER poliza TYPE numeric(14,0);

-- Para almacenar las secuencias de inventario especificas por epigrafe y municipio:

CREATE TABLE sequences_inventario
(
  id_sequence_inventario numeric(8,0) NOT NULL DEFAULT (0)::numeric,
  tablename character varying(250) NOT NULL DEFAULT ' '::character varying,
  field character varying(250) NOT NULL DEFAULT ' '::character varying,
  "value" numeric(10,0) DEFAULT (0)::numeric,
  incrementvalue numeric(10,0) NOT NULL DEFAULT (0)::numeric,
  minimumvalue numeric(10,0) NOT NULL DEFAULT (0)::numeric,
  maximumvalue numeric(10,0) NOT NULL DEFAULT (0)::numeric,
  circular character(1) NOT NULL DEFAULT ' '::bpchar,
  id_municipio numeric(5,0) DEFAULT 0,
  CONSTRAINT sequences_inventario_pkey PRIMARY KEY (id_sequence_inventario),
  CONSTRAINT sequences_inventario_tablename_key UNIQUE (tablename, field, id_municipio)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE sequences_inventario OWNER TO geopista;

--
