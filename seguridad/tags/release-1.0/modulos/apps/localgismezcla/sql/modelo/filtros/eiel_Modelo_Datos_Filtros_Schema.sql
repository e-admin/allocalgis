--
--alter user geopista set log_min_messages=error;

set client_min_messages='warning';
set client_encoding to 'utf8';

DROP TABLE lcg_nodos_capas_traducciones;
DROP TABLE lcg_nodos_capas_campos;
DROP TABLE lcg_nodos_app;
DROP TABLE lcg_nodos_capas;
DROP TABLE lcg_nodos_capas_grupos;

-- NOTA: Los id son un poco complicados porque tenemos que seguir
-- las secuencias y al final hay huecos, vamos a optar por
-- poner nombres mas significativos que nos permitan insertar datos
-- mas facilmente.



---
---
---
CREATE TABLE lcg_nodos_capas
(
  clave character varying(30) NOT NULL,
  categoria character varying(20) NOT NULL,
  nodo character varying(10) NOT NULL,
  tag_traduccion character varying(100) NOT NULL,
  tabla character varying(50),
  nombre_filtro character varying(50),
  bean character varying(100),
  activo	boolean DEFAULT  true,
  layer character varying(100),
  conectividad boolean DEFAULT false,
  CONSTRAINT pk_lcg_nodos_capas PRIMARY KEY (clave)
)
WITH (OIDS=TRUE);
ALTER TABLE lcg_nodos_capas OWNER TO postgres;


---
---
---
CREATE TABLE lcg_nodos_app(

  app character varying(10) NOT NULL,
  clave_capa character varying(30) NOT NULL,
  CONSTRAINT pk_lcg_nodos_app PRIMARY KEY (app,clave_capa),
  CONSTRAINT fk_lcg_nodos_app1 FOREIGN KEY (clave_capa)
      REFERENCES lcg_nodos_capas (clave) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=TRUE);
ALTER TABLE lcg_nodos_app OWNER TO postgres;

--
--
--

CREATE TABLE lcg_nodos_capas_grupos
(
  clave character varying(30) NOT NULL,
  orden numeric NOT NULL,
  tag_traduccion character varying(100) NOT NULL,
  bean	character varying(40),
  CONSTRAINT pk_lcg_nodos_capas_grupos PRIMARY KEY (clave)
)
WITH (OIDS=TRUE);
ALTER TABLE lcg_nodos_capas_grupos OWNER TO postgres;

--
--
--

CREATE TABLE lcg_nodos_capas_campos
(
  clave_capa character varying(30) NOT NULL, 
  clave_grupo  character varying(30) NOT NULL,
  campo_bd	character varying(50) NOT NULL,
  tag_traduccion character varying(100),
  tipo_bd	numeric NOT NULL,
  dominio	character varying(100),
  metodo	character varying(50),
  aplicaInformes	boolean DEFAULT  true,
  aplicaMovilidad	boolean DEFAULT true,
  tabla character varying(50) DEFAULT NULL, -- Este dato solo se rellena para datos especiales como depuradoras
  bean character varying(100) DEFAULT NULL, -- Este dato solo se rellena para datos especiales como depuradoras
  CONSTRAINT pk_lcg_nodos_capas_campos PRIMARY KEY (clave_capa,campo_bd),
  CONSTRAINT fk_nodos_capas_campos1 FOREIGN KEY (clave_capa)
      REFERENCES lcg_nodos_capas (clave) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_nodos_capas_campos2 FOREIGN KEY (clave_grupo)
      REFERENCES lcg_nodos_capas_grupos (clave) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=TRUE);
ALTER TABLE lcg_nodos_capas_campos  OWNER TO postgres;
 

----
----
----  
  
CREATE TABLE lcg_nodos_capas_traducciones
(
  tag_traduccion character varying(100) NOT NULL,
  traduccion character varying(150) NOT NULL,
  locale character varying(10) NOT NULL,
  CONSTRAINT pk_lcg_nodos_capas_traducciones PRIMARY KEY (tag_traduccion)
)
WITH (OIDS=TRUE);
ALTER TABLE lcg_nodos_capas_traducciones OWNER TO postgres;
  

 
--psql -f eiel_Modelo_Filtros_Capa_CA.sql;
--psql -f eiel_Modelo_Filtros_Capa_DE.sql;
--psql -f eiel_Modelo_Filtros_Capa_TP.sql;
--psql -f eiel_Modelo_Filtros_Permisos.sql: