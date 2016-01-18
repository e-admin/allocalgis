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

-- La tabla attributes repite atributos para cada capa, si se publica un mapa
-- para todos los municipios los atributos se repinte.

select f_add_col('localgisguiaurbana.attribute','mapid', 'ALTER TABLE localgisguiaurbana.attribute ADD COLUMN mapid integer;');

DROP INDEX IF EXISTS localgisguiaurbana.map_mapid_idx;
CREATE INDEX map_mapid_idx ON localgisguiaurbana.map (mapid);
