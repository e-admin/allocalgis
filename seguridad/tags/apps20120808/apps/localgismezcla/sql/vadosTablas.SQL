--------TABLAS DEL SISTEMA-----------

--****************************************************************************************************************
-- Table: entidadlocalgis_entidadext
--****************************************************************************************************************

CREATE TABLE localgisguiaurbana.entidadlocalgis_entidadext
(
  id_entidad numeric NOT NULL,
  id_entidadext numeric NOT NULL,
  CONSTRAINT entidadext_key UNIQUE (id_entidadext),
  CONSTRAINT entidadlocalgis_key UNIQUE (id_entidad)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE localgisguiaurbana.entidadlocalgis_entidadext OWNER TO geopista;
GRANT ALL ON TABLE localgisguiaurbana.entidadlocalgis_entidadext TO geopista;

--****************************************************************************************************************
-- Table: entidad_tipo_map
--****************************************************************************************************************

CREATE TABLE localgisguiaurbana.entidad_tipo_map
(
  id_entidad numeric NOT NULL,
  tipo text NOT NULL,
  mapid numeric NOT NULL,
  CONSTRAINT entidad_tipo_key UNIQUE (id_entidad, tipo)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE localgisguiaurbana.entidad_tipo_map OWNER TO geopista;
GRANT ALL ON TABLE localgisguiaurbana.entidad_tipo_map TO geopista;

--****************************************************************************************************************
--------FOREIGN KEYS-----------
--****************************************************************************************************************

ALTER TABLE localgisguiaurbana.entidad_tipo_map ADD CONSTRAINT entidad_fkey FOREIGN KEY (id_entidad)
REFERENCES localgisguiaurbana.entidadlocalgis_entidadext (id_entidad) MATCH SIMPLE
ON UPDATE CASCADE ON DELETE RESTRICT;
