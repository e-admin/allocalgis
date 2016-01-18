-- Consultas Versionado
insert into version (id_version, fecha_version) values('2.0', DATE '2010-01-01');

-- Consultas Actuaciones
CREATE TABLE civil_work_registry
(
  id_warning integer,
  date_updated date,
  CONSTRAINT civil_work_registry_pkey PRIMARY KEY (id_warning),
  CONSTRAINT civil_work_registry_id_warning_fk FOREIGN KEY (id_warning)
      REFERENCES civil_work_warnings (id_warning) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITHOUT OIDS;
ALTER TABLE civil_work_registry OWNER TO postgres;

-- Actualizaciones tablas

ALTER TABLE CIVIL_WORK_INTERVENTION ADD COLUMN priority integer;
CREATE INDEX PORTALES_SPAT_IDX ON NUMEROS_POLICIA USING GIST("GEOMETRY");
CREATE INDEX TRAMOSVIA_SPAT_IDX ON TRAMOSVIA USING GIST("GEOMETRY");
CREATE INDEX VIAS_SPAT_IDX ON VIAS USING GIST("GEOMETRY");
-- Correccion para prioridades nulas(Actualizaciones anteriores).
UPDATE CIVIL_WORK_INTERVENTION SET PRIORITY = 10 WHERE PRIORITY IS NULL;

ALTER TABLE civil_work_intervention ADD COLUMN ended_work date;

-- Correccion de acentos
update acl set name = 'Gestion de Espacio Publico' where lower(name) like 'gesti%n de espacio p%blico';

-- Agregar tramos del parcelario (parcelas)
-- A través del layerfamili id = 13;
-- layer parcelas=101
insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (150,13,2,0);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, 			isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) 
		values    (150,   13,             101,	   2,      'parcelas:_:default:parcelas', 	true,	7,0,true,false);

-- Agregar tramos de vias y vias al mapa de espacio publico
-- A través del layerfamili id = 8;
-- layer vias=16  ;-;  layer tramosvias = 11
insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (150,8,3,0);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, 			isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) 
		values    (150,   8,             11,	   22,      'tramosvia:_:default:tramosvia', 	true,	8,0,true,false);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, 			isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) 
		values    (150,   8,             16,	   21,      'vias:_:default:vias', 	true,	9,0,true,false);

-- Agregar numeros de policia al mapa de espacio publico
-- A través del layerfamili id = 12;
-- layer numeros de policia = 12
insert into maps_layerfamilies_relations (ID_MAP,ID_LAYERFAMILY,POSITION,ID_ENTIDAD) values (150,12,4,0);
insert into layers_styles (id_map,id_layerfamily,id_layer, id_style, stylename, 			isactive,position,ID_ENTIDAD,ISVISIBLE,ISEDITABLE) 
		values    (150,   12,            12,	   4,      'numeros_policia:_:default:numeros_policia', 	true,	10,0,true,false);