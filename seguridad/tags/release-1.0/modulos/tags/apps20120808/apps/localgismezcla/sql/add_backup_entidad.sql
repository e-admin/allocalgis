insert into version (id_version, fecha_version) values('1.4', DATE '2011-06-01');
ALTER TABLE entidad_supramunicipal
   ADD COLUMN backup numeric(1,0) DEFAULT 1;