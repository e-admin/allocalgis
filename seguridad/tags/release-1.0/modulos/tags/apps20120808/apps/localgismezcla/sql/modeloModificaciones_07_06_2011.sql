
--DROP TABLE valor_datos_valoracion
CREATE TABLE valor_datos_valoracion
(
   valor_urbano numeric(10,3) NOT NULL DEFAULT 0, 
   valor_rustico numeric(10,3) NOT NULL DEFAULT 0, 
   id_entidad integer NOT NULL DEFAULT 0,
  CONSTRAINT pk_id_entidad PRIMARY KEY (id_entidad)
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE valor_datos_valoracion OWNER TO geopista;

--Inserto el valor por defecto

--insert into valor_datos_valoracion (valor_urbano, valor_rustico,id_entidad) VALUES(0,0,0);