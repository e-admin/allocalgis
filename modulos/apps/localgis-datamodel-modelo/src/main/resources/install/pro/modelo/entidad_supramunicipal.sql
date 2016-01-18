SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET default_with_oids = true;

SET client_min_messages TO WARNING;

create or replace function f_add_col (t_name regclass,c_name text, sql text) returns void AS $$
begin
    IF EXISTS (
		SELECT 1 FROM pg_attribute
			WHERE  attrelid = t_name
			AND    attname = c_name
			AND    NOT attisdropped) THEN
			RAISE NOTICE 'Column % already exists in %', c_name, t_name;
	ELSE
		EXECUTE sql;
	END IF;
end;
$$ language 'plpgsql';


select f_add_col('entidad_supramunicipal','GEOMETRY', 'ALTER TABLE entidad_supramunicipal ADD COLUMN "GEOMETRY" geometry;');
select f_add_col('entidad_supramunicipal','municipiodefault', 'ALTER TABLE entidad_supramunicipal ADD COLUMN "municipiodefault" numeric (5,0);');

UPDATE query_catalog set query = 'select nombreoficial, srid from entidad_supramunicipal where id_entidad=?' where id = 'getEntidad';
UPDATE query_catalog set query = 'select entidad_supramunicipal.id_entidad, entidad_supramunicipal.nombreoficial, entidad_supramunicipal.srid,  entidad_supramunicipal.backup, entidad_supramunicipal.aviso, entidad_supramunicipal.periodicidad, entidad_supramunicipal.intentos, entidades_municipios.*,municipios.nombreoficial as nombremunicipio from entidad_supramunicipal,entidades_municipios,municipios where entidad_supramunicipal.id_entidad=entidades_municipios.id_entidad and entidades_municipios.id_municipio=municipios.id order by entidad_supramunicipal.nombreoficial' where id = 'getEntidadesFullSortedByIdEntidad';
UPDATE query_catalog set query = 'select id_entidad, nombreoficial, srid,  backup, aviso, periodicidad, intentos from entidad_supramunicipal order by id_entidad' where id = 'getEntidadesSortedById';
UPDATE query_catalog set query = '"select entidad_supramunicipal.id_entidad, entidad_supramunicipal.nombreoficial, entidad_supramunicipal.srid,  entidad_supramunicipal.backup, entidad_supramunicipal.aviso, entidad_supramunicipal.periodicidad, entidad_supramunicipal.intentos, entidades_municipios.*,municipios.nombreoficial as nombremunicipio from entidad_supramunicipal,entidades_municipios,municipios where entidad_supramunicipal.id_entidad=entidades_municipios.id_entidad and entidades_municipios.id_municipio=municipios.id order by entidad_supramunicipal.nombreoficial' where id = 'getEntidadesSortedByIdEntidad';
UPDATE query_catalog set query = 'SELECT id_entidad, nombreoficial, srid, backup, aviso, periodicidad, intentos FROM entidad_supramunicipal order by nombreoficial' where id = 'getEntidadesSortedByName';
