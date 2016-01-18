-- PRO -----------------------------------------------
SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;


-- Añadimos a diferentes usuarios su rol correspondiente

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN

	RAISE NOTICE 'CREANDO PERMISOS ESPECIFICOS PARA LA EIEL';
	
	IF NOT EXISTS (SELECT * FROM iusergroupuser WHERE GROUPID = (select id::integer from iuseruserhdr where name='EIEL001_PUB') AND USERID=(select id::integer from iusergrouphdr where name='EIEL_PUB')) THEN
		insert into iusergroupuser(userid,groupid) values ((select id::integer from iuseruserhdr where name='EIEL001_PUB'),(select id::integer from iusergrouphdr where name='EIEL_PUB'));		
	END IF;	
	IF NOT EXISTS (SELECT * FROM iusergroupuser WHERE GROUPID = (select id::integer from iuseruserhdr where name='EIEL001_VAL') AND USERID=(select id::integer from iusergrouphdr where name='EIEL_VAL')) THEN
		insert into iusergroupuser(userid,groupid) values ((select id::integer from iuseruserhdr where name='EIEL001_VAL'),(select id::integer from iusergrouphdr where name='EIEL_VAL'));		
	END IF;	
	IF NOT EXISTS (SELECT * FROM iusergroupuser WHERE GROUPID = (select id::integer from iuseruserhdr where name='SATEC_PRO') AND USERID=(select id::integer from iusergrouphdr where name='EIEL_VAL')) THEN
		insert into iusergroupuser(userid,groupid) values ((select id::integer from iuseruserhdr where name='SATEC_PRO'),(select id::integer from iusergrouphdr where name='EIEL_VAL'));		
	END IF;	
	IF NOT EXISTS (SELECT * FROM iusergroupuser WHERE GROUPID = (select id::integer from iuseruserhdr where name='EIEL001_PUB') AND USERID=(select id::integer from iusergrouphdr where name='EIEL_INDICADORES')) THEN
		insert into iusergroupuser(userid,groupid) values ((select id::integer from iuseruserhdr where name='EIEL001_PUB'),(select id::integer from iusergrouphdr where name='EIEL_INDICADORES'));	
	END IF;	
	IF NOT EXISTS (SELECT * FROM iusergroupuser WHERE GROUPID = (select id::integer from iuseruserhdr where name='EIEL001_VAL') AND USERID=(select id::integer from iusergrouphdr where name='EIEL_INDICADORES')) THEN
		insert into iusergroupuser(userid,groupid) values ((select id::integer from iuseruserhdr where name='EIEL001_VAL'),(select id::integer from iusergrouphdr where name='EIEL_INDICADORES'));		
	END IF;	
	IF NOT EXISTS (SELECT * FROM iusergroupuser WHERE GROUPID = (select id::integer from iuseruserhdr where name='SATEC_PRO') AND USERID=(select id::integer from iusergrouphdr where name='EIEL_INDICADORES')) THEN
		insert into iusergroupuser(userid,groupid) values ((select id::integer from iuseruserhdr where name='SATEC_PRO'),(select id::integer from iusergrouphdr where name='EIEL_INDICADORES'));	
	END IF;		
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PERMISOS_USUARIOS";












