-- Table: proyectos_extraccion

-- DROP TABLE proyectos_extraccion;

CREATE TABLE proyectos_extraccion
(
  id_proyecto varchar(255) NOT NULL,
  nombre_proyecto varchar(255),
  pos_esquina_x float8,
  pos_esquina_y float8,
  ancho_celdas float4,
  alto_celdas float4,
  celdas_x int4,
  celdas_y int4,
  fecha_extraccion timestamp,
  id_map varchar(255),
  id_entidad numeric(5),
  CONSTRAINT proyectos_extraccion_pkey PRIMARY KEY (id_proyecto)
) 
WITHOUT OIDS;
ALTER TABLE proyectos_extraccion OWNER TO geopista;

-- Table: capas_extraccion

-- DROP TABLE capas_extraccion;

CREATE TABLE capas_extraccion
(
  id_proyecto varchar(255) NOT NULL,
  id_layer_extract int4 NOT NULL,
  CONSTRAINT capas_extraccion_pkey PRIMARY KEY (id_proyecto, id_layer_extract),
  CONSTRAINT capas_extraccion_id_proyecto_fkey FOREIGN KEY (id_proyecto) REFERENCES proyectos_extraccion (id_proyecto) ON UPDATE RESTRICT ON DELETE RESTRICT
) 
WITHOUT OIDS;
ALTER TABLE capas_extraccion OWNER TO geopista;


-- Table: celdas_extraccion

-- DROP TABLE celdas_extraccion;

CREATE TABLE celdas_extraccion
(
  id_proyecto varchar(255) NOT NULL,
  num_celda int4 NOT NULL,
  id_usuario_asign numeric(10),
  CONSTRAINT celdas_extraccion_pkey PRIMARY KEY (id_proyecto, num_celda),
  CONSTRAINT celdas_extraccion_id_proyecto_fkey FOREIGN KEY (id_proyecto) REFERENCES proyectos_extraccion (id_proyecto) ON UPDATE RESTRICT ON DELETE RESTRICT
) 
WITHOUT OIDS;
ALTER TABLE celdas_extraccion OWNER TO geopista;




