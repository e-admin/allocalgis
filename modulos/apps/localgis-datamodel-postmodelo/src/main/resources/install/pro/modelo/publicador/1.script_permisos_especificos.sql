-- PRO -----------------------------------------------
SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;


-- Creamos unos permisos especificos
-- Borramos los permisos especificos que huberira de antes


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	RAISE NOTICE 'Creando permisos especificos para usuarios';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PERMISOS_EIEL";

delete from r_group_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='LocalGis.EIEL.Publicador');
delete from r_group_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='LocalGis.EIEL.Validador');

delete from r_acl_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='LocalGis.EIEL.Publicador');
delete from r_acl_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='LocalGis.EIEL.Validador');
delete from usrgrouperm where def='LocalGis.EIEL.Publicador';
delete from usrgrouperm where def='LocalGis.EIEL.Validador';

insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Publicador','');
insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));
insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Validador','');
insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN

	RAISE NOTICE 'CREANDO PERMISOS ESPECIFICOS PARA LA EIEL';
	
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'LocalGis.EIEL.Login') THEN
		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Login','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));	
	END IF;	
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'LocalGis.edicion.EIEL') THEN
		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.edicion.EIEL','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));	
	END IF;	
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'LocalGis.EIEL.Consulta') THEN
		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Consulta','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));	
	END IF;	
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'LocalGis.EIEL.Modifica') THEN
		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Modifica','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));	
	END IF;	
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'Geopista.Administracion.Login') THEN
		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'Geopista.Administracion.Login','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));	
	END IF;	
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'Geopista.EIEL.versionado.visualizacion') THEN
		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'Geopista.EIEL.versionado.visualizacion','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));	
	END IF;	
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'Geopista.EIEL.versionado.modificacion') THEN
		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'Geopista.EIEL.versionado.modificacion','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='EIEL'));	
	END IF;	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PERMISOS_EIEL";
