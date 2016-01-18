------------------------------------------------------
-- GEORREFERENCIA EXTERNA ----------------------------
------------------------------------------------------

-- Tabla consultasgeorreferenciaexterna para almacenar
-- las consultas de georreferenciacionExterna
CREATE TABLE consultasgeorreferenciaexterna (
  id NUMERIC(8,0) NOT NULL,
  nombreconsulta VARCHAR(25) NOT NULL,
  descripcion TEXT NOT NULL,
  usuario VARCHAR(25) NOT NULL,
  nombre_bbdd_ext VARCHAR(50) NOT NULL,
  nombre_tabla_ext VARCHAR(50) NOT NULL,
  metodo_georeferencia VARCHAR(30) NOT NULL,
  tipo_geometria VARCHAR(30) NOT NULL,
  tabla_cruce VARCHAR(50),
  campo_georeferencia VARCHAR(200) NOT NULL,
  campos_mostrar TEXT,
  campo_etiqueta VARCHAR(50) NOT NULL,
  filtro_operador TEXT,
  filtro_valor TEXT,
  portal varchar(50) NOT NULL,
  CONSTRAINT consultasgeorreferenciaexterna_pkey PRIMARY KEY("id")
) WITH OIDS;

-- Secuencia de la tabla consultasgeorreferenciaexterna
create sequence seq_consultasgeorreferenciaexterna;

GRANT SELECT ON ALL TABLES IN SCHEMA public TO localgisbackup;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO geopista;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;