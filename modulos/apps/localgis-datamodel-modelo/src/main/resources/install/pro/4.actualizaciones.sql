SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET default_with_oids = true;

SET client_min_messages TO WARNING;

-- Inicio import_export_capas.sql 

INSERT INTO acl (idacl,name) VALUES(nextval('seq_acl'),'Capas Importadas');

INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));


-- Fin import_export_capas.sql


-- Inicio actualizar secuencias
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT * FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_domainnodes') THEN
		PERFORM setval('public.seq_domainnodes', (select max(id)::bigint from domainnodes), true);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'LMseleccionarIdFamiliaCapa') THEN
		insert into query_catalog (id,query,acl,idperm) values ('LMseleccionarIdFamiliaCapa','select id_layerfamily from layerfamilies where id_name=? and id_description=?','1','9205');
	END IF;
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'existeVersion') THEN
		insert into query_catalog (id,query,acl,idperm) values ('existeVersion','select coalesce(count(*),0) from versiones where revision=? and id_table_versionada=?','1','9205');
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO2";

ALTER TABLE "public"."session_app" ALTER COLUMN "id" TYPE varchar(40);
ALTER TABLE "public"."iusercnt" ALTER COLUMN "id" TYPE varchar(40);

