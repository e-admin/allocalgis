update lcg_nodos_capas_campos set dominio='eiel_Sistema de impulsión' where dominio='eiel_sist_impulsion' and clave_capa='CL';
update lcg_nodos_capas_campos set dominio='eiel_tipo_lampara' where dominio='eiel_tipo_luminaria' and clave_capa='ALUM';

ALTER TABLE "public"."columns" ALTER COLUMN "name" TYPE varchar(150);

CREATE OR REPLACE FUNCTION createrole() RETURNS VOID AS
$Q$
BEGIN
	CREATE ROLE MODELO LOGIN SUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION;	  
	exception when others then
		RAISE NOTICE 'El role modelo ya existia';
END;
$Q$
LANGUAGE plpgsql;
select createrole() as "CREATE ROLE MODELO";


delete from version where id_version = 'MODELO R45';
insert into version (id_version, fecha_version) values('MODELO R45', DATE '2015-02-13');






