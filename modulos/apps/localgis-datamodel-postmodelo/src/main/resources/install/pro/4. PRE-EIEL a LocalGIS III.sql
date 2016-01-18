SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;

SET client_min_messages TO notice;

-- Previo

-- Inicio release33.sql
UPDATE columns SET "Type" = 3 WHERE id_table = (SELECT id_table FROM tables where name = 'eiel_c_parroquias') and name = 'nombre_parroquia';
update columns set "Length"=6, "Precision"=6, "Scale"=1  where id_table=(select id_table from tables where name = 'eiel_c_alum_pl') and name='potencia';
update tables set geometrytype = 11 where name = 'eiel_pmr_accesibilidad_edificios_publicos';
-- Fin release33.sql

--Crear secuencias eiel

-- Inicio release29.sql

-- Fin release29.sql

-- Inicio createEIELUser.sql

SELECT setval('public.seq_acl_perm', (select max(idperm)::bigint from usrgrouperm), true);
SELECT setval('public.seq_acl', (select max(idacl)::bigint from acl), true);


DROP SEQUENCE IF EXISTS seq_iuseruserhdr;
CREATE SEQUENCE seq_iuseruserhdr
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 400
  CACHE 1;

DROP SEQUENCE IF EXISTS  seq_iusergrouphdr;
CREATE SEQUENCE seq_iusergrouphdr
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 400
  CACHE 1;

DROP SEQUENCE IF EXISTS  seq_r_user_perm;
CREATE SEQUENCE seq_r_user_perm
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 400
  CACHE 1;

  
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'LocalGis.EIEL.Login') THEN
		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Login',''); ---Debe empezar en 10110
		insert into acl(idacl,name) values(nextval('seq_acl'),'EIEL');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO1";
  

--Crea un usuario EIEL
--delete from iusergroupuser WHERE userid=400
--delete from iusergrouphdr WHERE id=400
--delete from iuseruserhdr where id=400;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM iuseruserhdr WHERE name = 'EIEL') THEN
		insert into iuseruserhdr(id,name,nombrecompleto,password,flags,stat,numbadcnts,remarks,crtrid,updrid,mail,deptid,borrado,id_entidad,upddate, crtndate,nif ,fecha_proxima_modificacion, bloqueado,intentos_reiterados) 
		values (nextval('seq_iuseruserhdr'),'EIEL','EIEL','PggP3/BOfCFsFDgX6iL9gA==',0,0,0,'Este usuario solo puede trabajar con EIEL',nextval('seq_r_user_perm'),currval('seq_r_user_perm'),'',0,0,0,'2011-07-18 00:00:00','2011-07-18 00:00:00','','2012-07-18','0',0);
		   
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (currval('seq_r_user_perm'),currval('seq_acl_perm'),currval('seq_acl'),1);

		insert into iusergrouphdr(id,name,mgrid,type,remarks,crtrid,crtndate,updrid,upddate,id_entidad) 
		values (nextval('seq_iusergrouphdr'),'EIEL',currval('seq_r_user_perm'),0,'Rol para Módulo de EIEL',currval('seq_r_user_perm'),'2011-07-18',currval('seq_r_user_perm'),'2011-07-18',0);
		 
		insert into iusergroupuser(groupid,userid) values (currval('seq_iusergrouphdr'),currval('seq_r_user_perm'));
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (100,currval('seq_acl_perm'),currval('seq_acl'),1);-- PERMISO a syssuperuser 


		--insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.edicion.EIEL','');
		--insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
		--insert into r_usr_perm(userid,idperm,idacl,aplica) values (currval('seq_r_user_perm'),currval('seq_acl_perm'),currval('seq_acl'),1);


		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Modifica','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (currval('seq_r_user_perm'),currval('seq_acl_perm'),currval('seq_acl'),1);
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (100,currval('seq_acl_perm'),currval('seq_acl'),1);-- PERMISO a syssuperuser 


		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Consulta','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (currval('seq_r_user_perm'),currval('seq_acl_perm'),currval('seq_acl'),1);
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (100,currval('seq_acl_perm'),currval('seq_acl'),1);-- PERMISO a syssuperuser 


		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.GeneraMPT','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (currval('seq_r_user_perm'),currval('seq_acl_perm'),currval('seq_acl'),1);
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (100,currval('seq_acl_perm'),currval('seq_acl'),1);-- PERMISO a syssuperuser 

		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.ValidarMPT','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (currval('seq_r_user_perm'),currval('seq_acl_perm'),currval('seq_acl'),1);
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (100,currval('seq_acl_perm'),currval('seq_acl'),1);-- PERMISO a syssuperuser 


		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.CuadrosResumenMPT','');
		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (currval('seq_r_user_perm'),currval('seq_acl_perm'),currval('seq_acl'),1);
		insert into r_usr_perm(userid,idperm,idacl,aplica) values (100,currval('seq_acl_perm'),currval('seq_acl'),1);-- PERMISO a syssuperuser 
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO2";




--insert into r_usr_perm (userid,idperm,idacl,aplica)values ((select id from iuseruserhdr where name ='SATEC_DEV'),(select idperm from usrgrouperm where def='LocalGis.EIEL.Login'),(select idacl from acl where name='EIEL'),1);
--insert into r_usr_perm (userid,idperm,idacl,aplica)values ((select id from iuseruserhdr where name ='SATEC_DEV'),(select idperm from usrgrouperm where def='LocalGis.edicion.EIEL'),(select idacl from acl where name='EIEL'),1);

-- Fin createEIELUser.sql

-- Inicio CreacionPermisos.sql



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'Geopista.EIEL.versionado.visualizacion') THEN

		--Insertamos el permiso de permEIELVista para el usuario syssuperuser
		insert into usrgrouperm (idperm, def) values (((select max(idperm) from usrgrouperm)+1), 'Geopista.EIEL.versionado.visualizacion');
		--Asociamos el permiso con la ACL EIEL
		insert into r_acl_perm (idperm, idacl) values ((select max(idperm) from usrgrouperm), (select idacl from acl where name like ('EIEL')));
		--Asociamos el permiso con la ACL 14 y el Usuario 100 -- SysSuperUser
		insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like 'SYSSUPERUSER'), (select idperm from usrgrouperm where def like 'Geopista.EIEL.versionado.visualizacion'),(select idacl from acl where name like ('EIEL')), 1);

		insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like 'EIEL'), (select idperm from usrgrouperm where def like 'Geopista.EIEL.versionado.visualizacion'),(select idacl from acl where name like ('EIEL')), 1);

		--Insertamos el permiso de permEIELModificacion para el usuario syssuperuser
		insert into usrgrouperm (idperm, def) values (((select max(idperm) from usrgrouperm)+1), 'Geopista.EIEL.versionado.modificacion');
		--Asociamos el permiso con la ACL 14 EIEL
		insert into r_acl_perm (idperm, idacl) values ((select max(idperm) from usrgrouperm), (select idacl from acl where name like ('EIEL')));
		--Asociamos el permiso con la ACL 14 y el Usuario 100 -- SysSuperUser
		insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like 'SYSSUPERUSER'), (select idperm from usrgrouperm where def like 'Geopista.EIEL.versionado.modificacion'),(select idacl from acl where name like ('EIEL')), 1);

		insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like 'EIEL'), (select idperm from usrgrouperm where def like 'Geopista.EIEL.versionado.modificacion'),(select idacl from acl where name like ('EIEL')), 1);
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO3";



-- Fin CreacionPermisos.sql

-- Inicio import_export_capas.sql 

-- El permiso lo hemos movido a pre-modelo

--INSERT INTO acl (idacl,name) VALUES(nextval('seq_acl'),'Capas Importadas');

--INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
--INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
--INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
--INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM r_usr_perm WHERE idperm = '4000') THEN
		INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4000,currval('seq_acl'),1);
		INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4010,currval('seq_acl'),1);
		INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4020,currval('seq_acl'),1);
		INSERT INTO r_usr_perm (userid, idperm, idacl, aplica) VALUES (400,4030,currval('seq_acl'),1);
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO4";


-- Fin import_export_capas.sql


-- Inicio getValueUsos.sql

DROP FUNCTION getdictionarydescription(text, text, integer, text);
CREATE OR REPLACE FUNCTION getdictionarydescription(domainname text, pattern text, identidad integer, locale text)
  RETURNS text AS
$BODY$select dictionary.traduccion from Domains inner join DomainNodes on 
domains.id=domainNodes.id_domain inner join dictionary on 
domainNodes.id_description = dictionary.id_vocablo left outer join entidades_municipios on
domainNodes.id_municipio=entidades_municipios.id_entidad where domains.name=$1 and
domainNodes.pattern=$2 and
(domainNodes.id_municipio is null or entidades_municipios.id_municipio=$3) and 
dictionary.locale=$4 order by domainnodes.id_municipio;$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getdictionarydescription(text, text, integer, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, integer, text) TO postgres;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, integer, text) TO public;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, integer, text) TO consultas;

CREATE OR REPLACE FUNCTION getdictionarydescription(domainname text, pattern text, identidad text, locale text)
  RETURNS text AS
$BODY$select dictionary.traduccion from Domains inner join DomainNodes on 
domains.id=domainNodes.id_domain inner join dictionary on 
domainNodes.id_description = dictionary.id_vocablo left outer join entidades_municipios on
domainNodes.id_municipio=entidades_municipios.id_entidad where domains.name=$1 and
domainNodes.pattern=$2 and
(domainNodes.id_municipio is null or entidades_municipios.id_municipio=$3) and 
dictionary.locale=$4 order by domainnodes.id_municipio;$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getdictionarydescription(text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, text, text) TO consultas;


CREATE OR REPLACE FUNCTION getvaluesdeportes_id(domainname text, idmunicipio text, locale text, orden text, codentidad text, codpoblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,tipo_deporte,$2,$3) from eiel_t_id_deportes where orden_id=$4 and codprov=substring($2,1,2) and codmunic=substring($2,3,5) and codentidad=$5 and codpoblamiento=$6 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getvaluesdeportes_id(text, text, text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesdeportes_id(text, text, text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesdeportes_id(text, text, text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesdeportes_id(text, text, text, text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getvaluesnivel_en(domainname text, idmunicipio text, locale text, orden text, codentidad text, codpoblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,nivel,$2,$3) || ' - Unidades: ' || unidades || ' - Plazas: ' || plazas || ' - Alumnos: ' || alumnos from eiel_t_en_nivel where orden_en=$4 and codprov=substring($2,1,2) and codmunic=substring($2,3,5) and codentidad=$5 and codpoblamiento=$6 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getvaluesnivel_en(text, text, text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesnivel_en(text, text, text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesnivel_en(text, text, text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesnivel_en(text, text, text, text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getvaluesusos(domainname text, identidad text, locale text, orden text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,uso,$2,$3) from eiel_t_cc_usos where orden_cc=$4 and codmunic=$2 and revision_expirada=9999999999),' , ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getvaluesusos(text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos(text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos(text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesusos(text, text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getvaluesusos(domainname text, identidad text, locale text, orden text, codmunic text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,uso,$2,$3) from eiel_t_cc_usos where orden_cc=$4 and codmunic=$5 and revision_expirada=9999999999),' , ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getvaluesusos(text, text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos(text, text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos(text, text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesusos(text, text, text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getvaluesusos_cc(domainname text, idmunicipio text, locale text, orden text, codentidad text, codpoblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,uso,$2,$3) from eiel_t_cc_usos where orden_cc=$4 and codprov=substring($2,1,2) and codmunic=substring($2,3,5) and codentidad=$5 and codpoblamiento=$6 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getvaluesusos_cc(text, text, text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos_cc(text, text, text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos_cc(text, text, text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesusos_cc(text, text, text, text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getvaluesusos_cc(domainname text, identidad text, locale text, orden text, codmunic text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,uso,$2,$3) from eiel_t_cc_usos where orden_cc=$4 and codmunic=$5 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getvaluesusos_cc(text, text, text, text, text)
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION getvaluesusos_cu(domainname text, idmunicipio text, locale text, orden text, codentidad text, codpoblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,uso,$2,$3) from eiel_t_cu_usos where orden_cu=$4 and codprov=substring($2,1,2) and codmunic=substring($2,3,5) and codentidad=$5 and codpoblamiento=$6 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getvaluesusos_cu(text, text, text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos_cu(text, text, text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos_cu(text, text, text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesusos_cu(text, text, text, text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getvaluesusos_cu(domainname text, identidad text, locale text, orden text, codmunic text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,uso,$2,$3) from eiel_t_cu_usos where orden_cu=$4 and codmunic=$5 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getvaluesusos_cu(text, text, text, text, text)
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION getnucleo_ar(idmunicipio text, entidad text, poblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion WHERE eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_c_nucleo_poblacion.codentidad = $2 AND eiel_c_nucleo_poblacion.codpoblamiento = $3 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_ar(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ar(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ar(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_ar(text, text, text) TO consultas;


CREATE OR REPLACE FUNCTION getnucleo_ca(idmunicipio text, orden_ed text, clave_ed text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_abast_ca_pobl WHERE eiel_tr_abast_ca_pobl."clave_ca" = $3 AND 
eiel_tr_abast_ca_pobl."codmunic_ca" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_tr_abast_ca_pobl."orden_ca" = $2 AND  
eiel_tr_abast_ca_pobl."codprov_ca" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_tr_abast_ca_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_abast_ca_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_abast_ca_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_ca(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ca(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ca(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_ca(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getnucleo_de(idmunicipio text, orden_de text, clave_de text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_abast_de_pobl WHERE eiel_tr_abast_de_pobl."clave_de" = $3 AND 
eiel_tr_abast_de_pobl."codmunic_de" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_tr_abast_de_pobl."orden_de" = $2 AND  
eiel_tr_abast_de_pobl."codprov_de" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_tr_abast_de_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_abast_de_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_abast_de_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_de(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_de(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_de(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_de(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getnucleo_ed(idmunicipio text, orden_ed text, clave_ed text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_saneam_ed_pobl WHERE eiel_tr_saneam_ed_pobl."clave_ed" = $3 AND 
eiel_tr_saneam_ed_pobl."codmunic_ed" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_tr_saneam_ed_pobl."orden_ed" = $2 AND  
eiel_tr_saneam_ed_pobl."codprov_ed" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_tr_saneam_ed_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_saneam_ed_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_saneam_ed_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_ed(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ed(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ed(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_ed(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getnucleo_pv(idmunicipio text, orden_pv text, clave_pv text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_saneam_pv_pobl WHERE eiel_tr_saneam_pv_pobl."clave_pv" = $3 AND 
eiel_tr_saneam_pv_pobl."codmunic_pv" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_tr_saneam_pv_pobl."orden_pv" = $2 AND  
eiel_tr_saneam_pv_pobl."codprov_pv" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_tr_saneam_pv_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_saneam_pv_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_saneam_pv_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_pv(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_pv(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_pv(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_pv(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getnucleo_tcl(idmunicipio text, tramo_cl text, clave text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_saneam_tcl_pobl WHERE eiel_tr_saneam_tcl_pobl."clave_tcl" = $3 AND 
eiel_tr_saneam_tcl_pobl."codmunic_tcl" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_tr_saneam_tcl_pobl."tramo_cl" = $2 AND  
eiel_tr_saneam_tcl_pobl."codprov_tcl" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_tr_saneam_tcl_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_saneam_tcl_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_tcl(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tcl(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tcl(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_tcl(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getnucleo_tcn(idmunicipio text, tramo_tcn text, clave_tcn text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_abast_tcn_pobl WHERE eiel_tr_abast_tcn_pobl."clave_tcn" = $3 AND 
eiel_tr_abast_tcn_pobl."codmunic_tcn" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_tr_abast_tcn_pobl."tramo_tcn" = $2 AND  
eiel_tr_abast_tcn_pobl."codprov_tcn" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_tr_abast_tcn_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_abast_tcn_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_abast_tcn_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_tcn(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tcn(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tcn(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_tcn(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getnucleo_tem(idmunicipio text, tramo_em text, clave_tem text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_saneam_tem_pobl WHERE eiel_tr_saneam_tem_pobl."clave_tem" = $3 AND 
eiel_tr_saneam_tem_pobl."codmunic_tem" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_tr_saneam_tem_pobl."tramo_em" = $2 AND  
eiel_tr_saneam_tem_pobl."codprov_tem" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_tr_saneam_tem_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_saneam_tem_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_saneam_tem_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_tem(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tem(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tem(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_tem(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getnucleo_tp(idmunicipio text, orden_tp text, clave text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_abast_tp_pobl WHERE eiel_tr_abast_tp_pobl."clave_tp" = $3 AND 
eiel_tr_abast_tp_pobl."codmunic_tp" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_tr_abast_tp_pobl."orden_tp" = $2 AND  
eiel_tr_abast_tp_pobl."codprov_tp" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_tr_abast_tp_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_abast_tp_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_abast_tp_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_tp(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tp(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tp(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_tp(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getnucleo_vt(idmunicipio text, orden_vt text, clave_vt text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_vt_pobl WHERE eiel_tr_vt_pobl."clave_vt" = $3 AND 
eiel_tr_vt_pobl."codmunic_vt" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1,3,5) AND eiel_tr_vt_pobl."orden_vt" = $2 AND  
eiel_tr_vt_pobl."codprov_vt" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1,1,2) AND eiel_tr_vt_pobl.codentidad = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_vt_pobl.codpoblamiento = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_vt_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getnucleo_vt(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_vt(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_vt(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_vt(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getdomainvalues(domainname text, identidad text, locale text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select dictionary.traduccion from Domains inner join DomainNodes on 
domains.id=domainNodes.id_domain inner join dictionary on 
domainNodes.id_description = dictionary.id_vocablo left outer join entidades_municipios on
domainNodes.id_municipio=entidades_municipios.id_entidad where domains.name=$1 and
--domainNodes.pattern=$2 and
domainNodes.type=7 and 
(domainNodes.id_municipio is null or entidades_municipios.id_municipio=$2) and 
dictionary.locale=$3 order by domainnodes.id_municipio),' , ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getdomainvalues(text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, text, text) TO consultas;

CREATE OR REPLACE FUNCTION getdomainvalues(domainname text, identidad integer, locale text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select dictionary.traduccion from Domains inner join DomainNodes on 
domains.id=domainNodes.id_domain inner join dictionary on 
domainNodes.id_description = dictionary.id_vocablo left outer join entidades_municipios on
domainNodes.id_municipio=entidades_municipios.id_entidad where domains.name=$1 and
--domainNodes.pattern=$2 and
domainNodes.type=7 and 
(domainNodes.id_municipio is null or entidades_municipios.id_municipio=$2) and 
dictionary.locale=$3 order by domainnodes.id_municipio),' , ' );$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION getdomainvalues(text, integer, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, integer, text) TO postgres;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, integer, text) TO public;
GRANT EXECUTE ON FUNCTION getdomainvalues(text, integer, text) TO consultas;


-- Fin getValueUsos.sql


-- Inicio .sql





-- Fin .sql


-- Inicio .sql



-- Fin .sql


-- Inicio ForenkeysMPT.sql + VistasForMPT.sql + integracionEIEL_Inventario.sql

DROP TABLE IF EXISTS query_validate_mpt;
CREATE TABLE query_validate_mpt
(
  tipo character varying(80) NOT NULL,
  nombre character varying(80) NOT NULL,
  query character varying(200),
  id_descripcion integer ,
  CONSTRAINT pk_query_validate_mpt PRIMARY KEY (tipo,nombre)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE query_validate_mpt OWNER TO geopista;

CREATE OR REPLACE VIEW parcelas_view AS 
 SELECT parcelas.id AS gid, parcelas.referencia_catastral AS geographicidentifier, centroid(parcelas."GEOMETRY") AS "position", entidades_municipios.id_entidad AS entidadidentifier, parcelas."GEOMETRY" AS geographicextent
   FROM parcelas
   JOIN entidades_municipios ON entidades_municipios.id_municipio = parcelas.id_municipio;

ALTER TABLE parcelas_view
  OWNER TO geopista;

CREATE OR REPLACE VIEW v_alumbrado AS 
 SELECT eiel_c_alum_pl.codprov AS provincia, eiel_c_alum_pl.codmunic AS municipio, eiel_c_alum_pl.codentidad AS entidad, eiel_c_alum_pl.codpoblamiento AS nucleo, eiel_c_alum_pl.ah_ener_rfl AS ah_ener_rl, eiel_c_alum_pl.ah_ener_rfi AS ah_ener_ri, eiel_c_alum_pl.estado AS calidad, eiel_c_alum_pl.potencia * count(*)::double precision AS pot_instal, count(*) AS n_puntos
   FROM eiel_c_alum_pl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_alum_pl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_alum_pl.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_alum_pl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_c_alum_pl.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_c_alum_pl.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_c_alum_pl.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_c_alum_pl.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  GROUP BY eiel_c_alum_pl.codmunic, eiel_c_alum_pl.codentidad, eiel_c_alum_pl.codpoblamiento, eiel_c_alum_pl.codprov, eiel_c_alum_pl.ah_ener_rfl, eiel_c_alum_pl.ah_ener_rfi, eiel_c_alum_pl.estado, eiel_c_alum_pl.potencia
  ORDER BY eiel_c_alum_pl.codmunic, eiel_c_alum_pl.codentidad, eiel_c_alum_pl.codpoblamiento;
  

ALTER TABLE v_alumbrado
  OWNER TO geopista;
GRANT ALL ON TABLE v_alumbrado TO geopista;
GRANT SELECT ON TABLE v_alumbrado TO consultas;

CREATE OR REPLACE VIEW v_cabildo_consejo AS 
 SELECT eiel_t_cabildo_consejo.codprov AS provincia, eiel_t_cabildo_consejo.cod_isla AS isla, eiel_t_cabildo_consejo.denominacion AS denominaci
   FROM eiel_t_cabildo_consejo
  WHERE eiel_t_cabildo_consejo.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_cabildo_consejo
  OWNER TO geopista;
GRANT ALL ON TABLE v_cabildo_consejo TO geopista;
GRANT SELECT ON TABLE v_cabildo_consejo TO consultas;

CREATE OR REPLACE VIEW v_cap_agua_nucleo AS 
 SELECT eiel_tr_abast_ca_pobl.codprov_pobl AS provincia, eiel_tr_abast_ca_pobl.codmunic_pobl AS municipio, eiel_tr_abast_ca_pobl.codentidad_pobl AS entidad, eiel_tr_abast_ca_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_ca_pobl.clave_ca AS clave, eiel_tr_abast_ca_pobl.codprov_ca AS c_provinc, eiel_tr_abast_ca_pobl.codmunic_ca AS c_municip, eiel_tr_abast_ca_pobl.orden_ca AS orden_capt
   FROM eiel_tr_abast_ca_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_ca_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_ca_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_ca_pobl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_tr_abast_ca_pobl.codprov_pobl::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_tr_abast_ca_pobl.codmunic_pobl::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_tr_abast_ca_pobl.codentidad_pobl::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_tr_abast_ca_pobl.codpoblamiento_pobl::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_cap_agua_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_cap_agua_nucleo TO geopista;
GRANT SELECT ON TABLE v_cap_agua_nucleo TO consultas;

CREATE OR REPLACE VIEW v_captacion_agua AS 
 SELECT eiel_t_abast_ca.clave, eiel_t_abast_ca.codprov AS provincia, eiel_t_abast_ca.codmunic AS municipio, eiel_t_abast_ca.orden_ca AS orden_capt
   FROM eiel_t_abast_ca
  WHERE (eiel_t_abast_ca.codmunic::text || eiel_t_abast_ca.orden_ca::text IN ( SELECT DISTINCT v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text
           FROM v_cap_agua_nucleo
          ORDER BY v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text)) AND eiel_t_abast_ca.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_captacion_agua
  OWNER TO geopista;
GRANT ALL ON TABLE v_captacion_agua TO geopista;
GRANT SELECT ON TABLE v_captacion_agua TO consultas;

CREATE OR REPLACE VIEW v_captacion_enc AS 
 SELECT eiel_t_abast_ca.clave, eiel_t_abast_ca.codprov AS provincia, eiel_t_abast_ca.codmunic AS municipio, eiel_t_abast_ca.orden_ca AS orden_capt, eiel_t_abast_ca.nombre AS denominaci, eiel_t_abast_ca.tipo AS tipo_capt, eiel_t_abast_ca.titular, eiel_t_abast_ca.gestor AS gestion, eiel_t_abast_ca.sist_impulsion AS sistema_ca, eiel_t_abast_ca.estado, eiel_t_abast_ca.uso, eiel_t_abast_ca.proteccion, eiel_t_abast_ca.contador
   FROM eiel_t_abast_ca
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_ca.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_ca.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_ca.codmunic::text || eiel_t_abast_ca.orden_ca::text IN ( SELECT DISTINCT v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text
      FROM v_cap_agua_nucleo
     ORDER BY v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text)) AND eiel_t_abast_ca.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_captacion_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_captacion_enc TO geopista;
GRANT SELECT ON TABLE v_captacion_enc TO consultas;

CREATE OR REPLACE VIEW v_captacion_enc_m50 AS 
 SELECT eiel_t_abast_ca.clave, eiel_t_abast_ca.codprov AS provincia, eiel_t_abast_ca.codmunic AS municipio, eiel_t_abast_ca.orden_ca AS orden_capt, eiel_t_abast_ca.nombre AS denominaci, eiel_t_abast_ca.tipo AS tipo_capt, eiel_t_abast_ca.titular, eiel_t_abast_ca.gestor AS gestion, eiel_t_abast_ca.sist_impulsion AS sistema_ca, eiel_t_abast_ca.estado, eiel_t_abast_ca.uso, eiel_t_abast_ca.proteccion, eiel_t_abast_ca.contador
   FROM eiel_t_abast_ca
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_ca.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_ca.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_abast_ca.codmunic::text || eiel_t_abast_ca.orden_ca::text IN ( SELECT DISTINCT v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text
      FROM v_cap_agua_nucleo
     ORDER BY v_cap_agua_nucleo.c_municip::text || v_cap_agua_nucleo.orden_capt::text)) AND eiel_t_abast_ca.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_captacion_enc_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_captacion_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_captacion_enc_m50 TO consultas;

CREATE OR REPLACE VIEW v_carretera AS 
 SELECT DISTINCT ON (eiel_t_carreteras.codprov, eiel_t_carreteras.cod_carrt) eiel_t_carreteras.codprov AS provincia, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion AS denominaci
   FROM eiel_c_tramos_carreteras, eiel_t_carreteras
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_carreteras.codprov::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_carreteras.revision_expirada = 9999999999::bigint::numeric AND eiel_c_tramos_carreteras.id_municipio::text = (eiel_t_padron_ttmm.codprov::text || eiel_t_padron_ttmm.codmunic::text) AND eiel_t_carreteras.cod_carrt::text = eiel_c_tramos_carreteras.cod_carrt::text AND eiel_c_tramos_carreteras.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_carreteras.codprov, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion
  ORDER BY eiel_t_carreteras.codprov, eiel_t_carreteras.cod_carrt, eiel_t_carreteras.denominacion;

ALTER TABLE v_carretera
  OWNER TO geopista;
GRANT ALL ON TABLE v_carretera TO geopista;
GRANT SELECT ON TABLE v_carretera TO consultas;

CREATE OR REPLACE VIEW v_casa_con_uso AS 
 SELECT DISTINCT ON (eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov, eiel_t_cc_usos.codmunic, eiel_t_cc_usos.codentidad, eiel_t_cc_usos.codpoblamiento, eiel_t_cc_usos.orden_cc, eiel_t_cc_usos.uso) eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov AS provincia, eiel_t_cc_usos.codmunic AS municipio, eiel_t_cc_usos.codentidad AS entidad, eiel_t_cc_usos.codpoblamiento AS poblamient, eiel_t_cc_usos.orden_cc AS orden_casa, eiel_t_cc_usos.uso, eiel_t_cc_usos.s_cubierta AS s_cubi
   FROM eiel_t_cc_usos
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cc_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cc_usos.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cc_usos.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_cc_usos.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_cc_usos.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_cc_usos.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_cc_usos.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  ORDER BY eiel_t_cc_usos.clave, eiel_t_cc_usos.codprov, eiel_t_cc_usos.codmunic, eiel_t_cc_usos.codentidad, eiel_t_cc_usos.codpoblamiento, eiel_t_cc_usos.orden_cc, eiel_t_cc_usos.uso;

ALTER TABLE v_casa_con_uso
  OWNER TO geopista;
GRANT ALL ON TABLE v_casa_con_uso TO geopista;
GRANT SELECT ON TABLE v_casa_con_uso TO consultas;

CREATE OR REPLACE VIEW v_casa_consistorial AS 
 SELECT eiel_t_cc.clave, eiel_t_cc.codprov AS provincia, eiel_t_cc.codmunic AS municipio, eiel_t_cc.codentidad AS entidad, eiel_t_cc.codpoblamiento AS poblamient, eiel_t_cc.orden_cc AS orden_casa, eiel_t_cc.nombre, eiel_t_cc.tipo, eiel_t_cc.titular, eiel_t_cc.tenencia, eiel_t_cc.s_cubierta AS s_cubi, eiel_t_cc.s_aire, eiel_t_cc.s_solar AS s_sola, eiel_t_cc.acceso_s_ruedas, eiel_t_cc.estado
   FROM eiel_t_cc
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cc.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cc.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cc.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_cc.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_cc.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_cc.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_cc.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_casa_consistorial
  OWNER TO geopista;
GRANT ALL ON TABLE v_casa_consistorial TO geopista;
GRANT SELECT ON TABLE v_casa_consistorial TO consultas;

CREATE OR REPLACE VIEW v_cementerio AS 
 SELECT eiel_t_ce.clave, eiel_t_ce.codprov AS provincia, eiel_t_ce.codmunic AS municipio, eiel_t_ce.codentidad AS entidad, eiel_t_ce.codpoblamiento AS poblamient, eiel_t_ce.orden_ce AS orden_ceme, eiel_t_ce.nombre, eiel_t_ce.titular, eiel_t_ce.distancia, eiel_t_ce.acceso, eiel_t_ce.capilla, eiel_t_ce.deposito, eiel_t_ce.ampliacion, eiel_t_ce.saturacion, eiel_t_ce.superficie, eiel_t_ce.acceso_s_ruedas, eiel_t_ce.crematorio
   FROM eiel_t_ce
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ce.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ce.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ce.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_ce.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_ce.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_ce.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_ce.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_cementerio
  OWNER TO geopista;
GRANT ALL ON TABLE v_cementerio TO geopista;
GRANT SELECT ON TABLE v_cementerio TO consultas;

CREATE OR REPLACE VIEW v_cent_cultural AS 
 SELECT eiel_t_cu.clave, eiel_t_cu.codprov AS provincia, eiel_t_cu.codmunic AS municipio, eiel_t_cu.codentidad AS entidad, eiel_t_cu.codpoblamiento AS poblamient, eiel_t_cu.orden_cu AS orden_cent, eiel_t_cu.nombre, eiel_t_cu.tipo AS tipo_cent, eiel_t_cu.titular, eiel_t_cu.gestor AS gestion, eiel_t_cu.s_cubierta AS s_cubi, eiel_t_cu.s_aire, eiel_t_cu.s_solar AS s_sola, eiel_t_cu.acceso_s_ruedas, eiel_t_cu.estado
   FROM eiel_t_cu
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cu.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cu.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cu.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_cu.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_cu.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_cu.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_cu.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_cent_cultural
  OWNER TO geopista;
GRANT ALL ON TABLE v_cent_cultural TO geopista;
GRANT SELECT ON TABLE v_cent_cultural TO consultas;

CREATE OR REPLACE VIEW v_cent_cultural_usos AS 
 SELECT DISTINCT ON (eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov, eiel_t_cu_usos.codmunic, eiel_t_cu_usos.codentidad, eiel_t_cu_usos.codpoblamiento, eiel_t_cu_usos.orden_cu, eiel_t_cu_usos.uso) eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov AS provincia, eiel_t_cu_usos.codmunic AS municipio, eiel_t_cu_usos.codentidad AS entidad, eiel_t_cu_usos.codpoblamiento AS poblamient, eiel_t_cu_usos.orden_cu AS orden_cent, eiel_t_cu_usos.uso, eiel_t_cu_usos.s_cubierta AS s_cubi
   FROM eiel_t_cu_usos
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_cu_usos.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_cu_usos.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_cu_usos.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_cu_usos.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_cu_usos.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_cu_usos.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_cu_usos.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  ORDER BY eiel_t_cu_usos.clave, eiel_t_cu_usos.codprov, eiel_t_cu_usos.codmunic, eiel_t_cu_usos.codentidad, eiel_t_cu_usos.codpoblamiento, eiel_t_cu_usos.orden_cu, eiel_t_cu_usos.uso;

ALTER TABLE v_cent_cultural_usos
  OWNER TO geopista;
GRANT ALL ON TABLE v_cent_cultural_usos TO geopista;
GRANT SELECT ON TABLE v_cent_cultural_usos TO consultas;

CREATE OR REPLACE VIEW v_centro_asistencial AS 
 SELECT eiel_t_as.clave, eiel_t_as.codprov AS provincia, eiel_t_as.codmunic AS municipio, eiel_t_as.codentidad AS entidad, eiel_t_as.codpoblamiento AS poblamient, eiel_t_as.orden_as AS orden_casi, eiel_t_as.nombre, eiel_t_as.tipo AS tipo_casis, eiel_t_as.titular, eiel_t_as.gestor AS gestion, eiel_t_as.plazas, eiel_t_as.s_cubierta AS s_cubi, eiel_t_as.s_aire, eiel_t_as.s_solar AS s_sola, eiel_t_as.acceso_s_ruedas, eiel_t_as.estado
   FROM eiel_t_as
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_as.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_as.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_as.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_as.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_as.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_as.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_as.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_centro_asistencial
  OWNER TO geopista;
GRANT ALL ON TABLE v_centro_asistencial TO geopista;
GRANT SELECT ON TABLE v_centro_asistencial TO consultas;

CREATE OR REPLACE VIEW v_centro_ensenanza AS 
 SELECT eiel_t_en.clave, eiel_t_en.codprov AS provincia, eiel_t_en.codmunic AS municipio, eiel_t_en.codentidad AS entidad, eiel_t_en.codpoblamiento AS poblamient, eiel_t_en.orden_en AS orden_cent, eiel_t_en.nombre, eiel_t_en.ambito, eiel_t_en.titular, eiel_t_en.s_cubierta AS s_cubi, eiel_t_en.s_aire, eiel_t_en.s_solar AS s_sola, eiel_t_en.acceso_s_ruedas, eiel_t_en.estado
   FROM eiel_t_en
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_en.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_en.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_en.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_en.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_en.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_en.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_en.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_centro_ensenanza
  OWNER TO geopista;
GRANT ALL ON TABLE v_centro_ensenanza TO geopista;
GRANT SELECT ON TABLE v_centro_ensenanza TO consultas;

CREATE OR REPLACE VIEW v_centro_sanitario AS 
 SELECT eiel_t_sa.clave, eiel_t_sa.codprov AS provincia, eiel_t_sa.codmunic AS municipio, eiel_t_sa.codentidad AS entidad, eiel_t_sa.codpoblamiento AS poblamient, eiel_t_sa.orden_sa AS orden_csan, eiel_t_sa.nombre, eiel_t_sa.tipo AS tipo_csan, eiel_t_sa.titular, eiel_t_sa.gestor AS gestion, eiel_t_sa.s_cubierta AS s_cubi, eiel_t_sa.s_aire, eiel_t_sa.s_solar AS s_sola, eiel_t_sa.uci, eiel_t_sa.n_camas AS camas, eiel_t_sa.acceso_s_ruedas, eiel_t_sa.estado
   FROM eiel_t_sa
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_sa.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_sa.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_sa.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_sa.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_sa.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_sa.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_sa.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_centro_sanitario
  OWNER TO geopista;
GRANT ALL ON TABLE v_centro_sanitario TO geopista;
GRANT SELECT ON TABLE v_centro_sanitario TO consultas;

CREATE OR REPLACE VIEW v_colector_nucleo AS 
 SELECT eiel_tr_saneam_tcl_pobl.codprov_pobl AS provincia, eiel_tr_saneam_tcl_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_tcl_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_tcl_pobl.clave_tcl AS clave, eiel_tr_saneam_tcl_pobl.codprov_tcl AS c_provinc, eiel_tr_saneam_tcl_pobl.codmunic_tcl AS c_municip, eiel_tr_saneam_tcl_pobl.tramo_cl AS orden_cole
   FROM eiel_tr_saneam_tcl_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_tcl_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_tcl_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_tcl_pobl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_tr_saneam_tcl_pobl.codprov_pobl::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_tr_saneam_tcl_pobl.codmunic_pobl::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_tr_saneam_tcl_pobl.codentidad_pobl::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_colector_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_colector_nucleo TO geopista;
GRANT SELECT ON TABLE v_colector_nucleo TO consultas;

CREATE OR REPLACE VIEW v_colector AS 
 SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia, eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
   FROM eiel_t_saneam_tcl
  WHERE (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text
           FROM v_colector_nucleo
          ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_colector
  OWNER TO geopista;
GRANT ALL ON TABLE v_colector TO geopista;
GRANT SELECT ON TABLE v_colector TO consultas;

CREATE OR REPLACE VIEW v_colector_enc AS 
 SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia, eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
   FROM eiel_t_saneam_tcl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tcl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text
      FROM v_colector_nucleo
     ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_colector_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_colector_enc TO geopista;
GRANT SELECT ON TABLE v_colector_enc TO consultas;

CREATE OR REPLACE VIEW v_colector_enc_m50 AS 
 SELECT eiel_t_saneam_tcl.clave, eiel_t_saneam_tcl.codprov AS provincia, eiel_t_saneam_tcl.codmunic AS municipio, eiel_t_saneam_tcl.tramo_cl AS orden_cole
   FROM eiel_t_saneam_tcl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tcl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text
      FROM v_colector_nucleo
     ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_colector_enc_m50
  OWNER TO geopista;

;

CREATE OR REPLACE VIEW v_cond_agua_nucleo AS 
 SELECT eiel_tr_abast_tcn_pobl.codprov_pobl AS provincia, eiel_tr_abast_tcn_pobl.codmunic_pobl AS municipio, eiel_tr_abast_tcn_pobl.codentidad_pobl AS entidad, eiel_tr_abast_tcn_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_tcn_pobl.clave_tcn AS clave, eiel_tr_abast_tcn_pobl.codprov_tcn AS cond_provi, eiel_tr_abast_tcn_pobl.codmunic_tcn AS cond_munic, eiel_tr_abast_tcn_pobl.tramo_tcn AS orden_cond
   FROM eiel_tr_abast_tcn_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_tcn_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_tcn_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_tcn_pobl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_tr_abast_tcn_pobl.codprov_pobl::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_tr_abast_tcn_pobl.codmunic_pobl::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_tr_abast_tcn_pobl.codentidad_pobl::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_tr_abast_tcn_pobl.codpoblamiento_pobl::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_cond_agua_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_cond_agua_nucleo TO geopista;
GRANT SELECT ON TABLE v_cond_agua_nucleo TO consultas;

CREATE OR REPLACE VIEW v_conduccion AS 
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
   FROM eiel_t_abast_tcn
  WHERE (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text
           FROM v_cond_agua_nucleo
          ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_conduccion
  OWNER TO geopista;
GRANT ALL ON TABLE v_conduccion TO geopista;
GRANT SELECT ON TABLE v_conduccion TO consultas;

CREATE OR REPLACE VIEW v_conduccion_enc AS 
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
   FROM eiel_t_abast_tcn
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tcn.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text
      FROM v_cond_agua_nucleo
     ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_conduccion_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_conduccion_enc TO geopista;
GRANT SELECT ON TABLE v_conduccion_enc TO consultas;

CREATE OR REPLACE VIEW v_conduccion_enc_m50 AS 
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond
   FROM eiel_t_abast_tcn
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tcn.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text
      FROM v_cond_agua_nucleo
     ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_conduccion_enc_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_conduccion_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_conduccion_enc_m50 TO consultas;

CREATE OR REPLACE VIEW v_dep_agua_nucleo AS 
 SELECT eiel_tr_saneam_ed_pobl.codprov_pobl AS provincia, eiel_tr_saneam_ed_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_ed_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_ed_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_ed_pobl.clave_ed AS clave, eiel_tr_saneam_ed_pobl.codprov_ed AS de_provinc, eiel_tr_saneam_ed_pobl.codmunic_ed AS de_municip, eiel_tr_saneam_ed_pobl.orden_ed AS orden_depu
   FROM eiel_tr_saneam_ed_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_ed_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_ed_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_ed_pobl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_tr_saneam_ed_pobl.codprov_pobl::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_tr_saneam_ed_pobl.codmunic_pobl::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_tr_saneam_ed_pobl.codentidad_pobl::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_tr_saneam_ed_pobl.codpoblamiento_pobl::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_dep_agua_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_dep_agua_nucleo TO geopista;
GRANT SELECT ON TABLE v_dep_agua_nucleo TO consultas;

CREATE OR REPLACE VIEW v_deposito_agua_nucleo AS 
 SELECT eiel_tr_abast_de_pobl.codprov_pobl AS provincia, eiel_tr_abast_de_pobl.codmunic_pobl AS municipio, eiel_tr_abast_de_pobl.codentidad_pobl AS entidad, eiel_tr_abast_de_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_de_pobl.clave_de AS clave, eiel_tr_abast_de_pobl.codprov_de AS de_provinc, eiel_tr_abast_de_pobl.codmunic_de AS de_municip, eiel_tr_abast_de_pobl.orden_de AS orden_depo
   FROM eiel_tr_abast_de_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_de_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_de_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_de_pobl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_tr_abast_de_pobl.codprov_pobl::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_tr_abast_de_pobl.codmunic_pobl::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_tr_abast_de_pobl.codentidad_pobl::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_tr_abast_de_pobl.codpoblamiento_pobl::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_deposito_agua_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_deposito_agua_nucleo TO geopista;
GRANT SELECT ON TABLE v_deposito_agua_nucleo TO consultas;


CREATE OR REPLACE VIEW v_deposito AS 
 SELECT eiel_t_abast_de.clave, eiel_t_abast_de.codprov AS provincia, eiel_t_abast_de.codmunic AS municipio, eiel_t_abast_de.orden_de AS orden_depo
   FROM eiel_t_abast_de
  WHERE (eiel_t_abast_de.codmunic::text || eiel_t_abast_de.orden_de::text IN ( SELECT DISTINCT v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text
           FROM v_deposito_agua_nucleo
          ORDER BY v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text)) AND eiel_t_abast_de.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_deposito
  OWNER TO geopista;
GRANT ALL ON TABLE v_deposito TO geopista;
GRANT SELECT ON TABLE v_deposito TO consultas;


CREATE OR REPLACE VIEW v_deposito_enc AS 
 SELECT eiel_t_abast_de.clave, eiel_t_abast_de.codprov AS provincia, eiel_t_abast_de.codmunic AS municipio, eiel_t_abast_de.orden_de AS orden_depo, eiel_t_abast_de.ubicacion, eiel_t_abast_de.titular, eiel_t_abast_de.gestor AS gestion, eiel_t_abast_de.capacidad, eiel_t_abast_de.estado, eiel_t_abast_de.proteccion, eiel_t_abast_de.fecha_limpieza AS limpieza, eiel_t_abast_de.contador
   FROM eiel_t_abast_de
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_de.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_de.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_de.codmunic::text || eiel_t_abast_de.orden_de::text IN ( SELECT DISTINCT v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text
      FROM v_deposito_agua_nucleo
     ORDER BY v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_de.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_deposito_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_deposito_enc TO geopista;
GRANT SELECT ON TABLE v_deposito_enc TO consultas;

CREATE OR REPLACE VIEW v_deposito_enc_m50 AS 
 SELECT eiel_t_abast_de.clave, eiel_t_abast_de.codprov AS provincia, eiel_t_abast_de.codmunic AS municipio, eiel_t_abast_de.orden_de AS orden_depo, eiel_t_abast_de.ubicacion, eiel_t_abast_de.titular, eiel_t_abast_de.gestor AS gestion, eiel_t_abast_de.capacidad, eiel_t_abast_de.estado, eiel_t_abast_de.proteccion, eiel_t_abast_de.fecha_limpieza AS limpieza, eiel_t_abast_de.contador
   FROM eiel_t_abast_de
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_de.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_de.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_abast_de.codmunic::text || eiel_t_abast_de.orden_de::text IN ( SELECT DISTINCT v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text
      FROM v_deposito_agua_nucleo
     ORDER BY v_deposito_agua_nucleo.de_municip::text || v_deposito_agua_nucleo.orden_depo::text)) AND eiel_t_abast_de.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_deposito_enc_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_deposito_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_deposito_enc_m50 TO consultas;

CREATE OR REPLACE VIEW v_depuradora AS 
 SELECT eiel_t1_saneam_ed.clave, eiel_t1_saneam_ed.codprov AS provincia, eiel_t1_saneam_ed.codmunic AS municipio, eiel_t1_saneam_ed.orden_ed AS orden_depu
   FROM eiel_t1_saneam_ed
   LEFT JOIN eiel_t2_saneam_ed ON eiel_t2_saneam_ed.codprov::text = eiel_t1_saneam_ed.codprov::text AND eiel_t2_saneam_ed.codmunic::text = eiel_t1_saneam_ed.codmunic::text AND eiel_t1_saneam_ed.clave::text = eiel_t2_saneam_ed.clave::text AND eiel_t1_saneam_ed.orden_ed::text = eiel_t2_saneam_ed.orden_ed::text
  WHERE (eiel_t1_saneam_ed.codmunic::text || eiel_t1_saneam_ed.orden_ed::text IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text
      FROM v_dep_agua_nucleo
     ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_t1_saneam_ed.revision_expirada = 9999999999::bigint::numeric AND eiel_t2_saneam_ed.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_depuradora
  OWNER TO geopista;
GRANT ALL ON TABLE v_depuradora TO geopista;
GRANT SELECT ON TABLE v_depuradora TO consultas;

CREATE OR REPLACE VIEW v_depuradora_enc AS 
 SELECT eiel_t1_saneam_ed.clave, eiel_t1_saneam_ed.codprov AS provincia, eiel_t1_saneam_ed.codmunic AS municipio, eiel_t1_saneam_ed.orden_ed AS orden_depu, eiel_t1_saneam_ed.trat_pr_1, eiel_t1_saneam_ed.trat_pr_2, eiel_t1_saneam_ed.trat_pr_3, eiel_t1_saneam_ed.trat_sc_1, eiel_t1_saneam_ed.trat_sc_2, eiel_t1_saneam_ed.trat_sc_3, eiel_t1_saneam_ed.trat_av_1, eiel_t1_saneam_ed.trat_av_2, eiel_t1_saneam_ed.trat_av_3, eiel_t1_saneam_ed.proc_cm_1, eiel_t1_saneam_ed.proc_cm_2, eiel_t1_saneam_ed.proc_cm_3, eiel_t1_saneam_ed.trat_ld_1, eiel_t1_saneam_ed.trat_ld_2, eiel_t1_saneam_ed.trat_ld_3
   FROM eiel_t1_saneam_ed
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t1_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t1_saneam_ed.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t1_saneam_ed.codmunic::text || eiel_t1_saneam_ed.orden_ed::text IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text
      FROM v_dep_agua_nucleo
     ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_t1_saneam_ed.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_depuradora_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_depuradora_enc TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc TO consultas;

CREATE OR REPLACE VIEW v_depuradora_enc_2 AS 
 SELECT eiel_t2_saneam_ed.clave, eiel_t2_saneam_ed.codprov AS provincia, eiel_t2_saneam_ed.codmunic AS municipio, eiel_t2_saneam_ed.orden_ed AS orden_depu, eiel_t2_saneam_ed.titular, eiel_t2_saneam_ed.gestor AS gestion, eiel_t2_saneam_ed.capacidad, eiel_t2_saneam_ed.problem_1, eiel_t2_saneam_ed.problem_2, eiel_t2_saneam_ed.problem_3, eiel_t2_saneam_ed.lodo_gest, eiel_t2_saneam_ed.lodo_vert, eiel_t2_saneam_ed.lodo_inci, eiel_t2_saneam_ed.lodo_con_agri AS lodo_con_a, eiel_t2_saneam_ed.lodo_sin_agri AS lodo_sin_a, eiel_t2_saneam_ed.lodo_ot
   FROM eiel_t2_saneam_ed
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t2_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t2_saneam_ed.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t2_saneam_ed.codmunic::text || eiel_t2_saneam_ed.orden_ed::text IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text
      FROM v_dep_agua_nucleo
     ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t2_saneam_ed.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_depuradora_enc_2
  OWNER TO geopista;
GRANT ALL ON TABLE v_depuradora_enc_2 TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc_2 TO consultas;

CREATE OR REPLACE VIEW v_depuradora_enc_2_m50 AS 
 SELECT eiel_t2_saneam_ed.clave, eiel_t2_saneam_ed.codprov AS provincia, eiel_t2_saneam_ed.codmunic AS municipio, eiel_t2_saneam_ed.orden_ed AS orden_depu, eiel_t2_saneam_ed.titular, eiel_t2_saneam_ed.gestor AS gestion, eiel_t2_saneam_ed.capacidad, eiel_t2_saneam_ed.problem_1, eiel_t2_saneam_ed.problem_2, eiel_t2_saneam_ed.problem_3, eiel_t2_saneam_ed.lodo_gest, eiel_t2_saneam_ed.lodo_vert, eiel_t2_saneam_ed.lodo_inci, eiel_t2_saneam_ed.lodo_con_agri AS lodo_con_a, eiel_t2_saneam_ed.lodo_sin_agri AS lodo_sin_a, eiel_t2_saneam_ed.lodo_ot
   FROM eiel_t2_saneam_ed
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t2_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t2_saneam_ed.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t2_saneam_ed.codmunic::text || eiel_t2_saneam_ed.orden_ed::text IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text
      FROM v_dep_agua_nucleo
     ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t2_saneam_ed.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_depuradora_enc_2_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_depuradora_enc_2_m50 TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc_2_m50 TO consultas;

CREATE OR REPLACE VIEW v_depuradora_enc_m50 AS 
 SELECT eiel_t1_saneam_ed.clave, eiel_t1_saneam_ed.codprov AS provincia, eiel_t1_saneam_ed.codmunic AS municipio, eiel_t1_saneam_ed.orden_ed AS orden_depu, eiel_t1_saneam_ed.trat_pr_1, eiel_t1_saneam_ed.trat_pr_2, eiel_t1_saneam_ed.trat_pr_3, eiel_t1_saneam_ed.trat_sc_1, eiel_t1_saneam_ed.trat_sc_2, eiel_t1_saneam_ed.trat_sc_3, eiel_t1_saneam_ed.trat_av_1, eiel_t1_saneam_ed.trat_av_2, eiel_t1_saneam_ed.trat_av_3, eiel_t1_saneam_ed.proc_cm_1, eiel_t1_saneam_ed.proc_cm_2, eiel_t1_saneam_ed.proc_cm_3, eiel_t1_saneam_ed.trat_ld_1, eiel_t1_saneam_ed.trat_ld_2, eiel_t1_saneam_ed.trat_ld_3
   FROM eiel_t1_saneam_ed
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t1_saneam_ed.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t1_saneam_ed.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t1_saneam_ed.codmunic::text || eiel_t1_saneam_ed.orden_ed::text IN ( SELECT DISTINCT v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text
      FROM v_dep_agua_nucleo
     ORDER BY v_dep_agua_nucleo.de_municip::text || v_dep_agua_nucleo.orden_depu::text)) AND eiel_t1_saneam_ed.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_depuradora_enc_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_depuradora_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc_m50 TO consultas;

CREATE OR REPLACE VIEW v_edific_pub_sin_uso AS 
 SELECT eiel_t_su.clave, eiel_t_su.codprov AS provincia, eiel_t_su.codmunic AS municipio, eiel_t_su.codentidad AS entidad, eiel_t_su.codpoblamiento AS poblamient, eiel_t_su.orden_su AS orden_edif, eiel_t_su.nombre, eiel_t_su.titular, eiel_t_su.s_cubierta AS s_cubi, eiel_t_su.s_aire, eiel_t_su.s_solar AS s_sola, eiel_t_su.estado, eiel_t_su.uso_anterior AS usoant
   FROM eiel_t_su
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_su.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_su.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_su.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_su.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_su.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_su.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_su.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_edific_pub_sin_uso
  OWNER TO geopista;
GRANT ALL ON TABLE v_edific_pub_sin_uso TO geopista;
GRANT SELECT ON TABLE v_edific_pub_sin_uso TO consultas;

CREATE OR REPLACE VIEW v_emisario_nucleo AS 
 SELECT eiel_tr_saneam_tem_pobl.codprov_pobl AS provincia, eiel_tr_saneam_tem_pobl.codmunic_pobl AS municipio, eiel_tr_saneam_tem_pobl.codentidad_pobl AS entidad, eiel_tr_saneam_tem_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_saneam_tem_pobl.clave_tem AS clave, eiel_tr_saneam_tem_pobl.codprov_tem AS em_provinc, eiel_tr_saneam_tem_pobl.codmunic_tem AS em_municip, eiel_tr_saneam_tem_pobl.tramo_em AS orden_emis
   FROM eiel_tr_saneam_tem_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_saneam_tem_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_saneam_tem_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_saneam_tem_pobl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_tr_saneam_tem_pobl.codprov_pobl::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_tr_saneam_tem_pobl.codmunic_pobl::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_tr_saneam_tem_pobl.codentidad_pobl::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_tr_saneam_tem_pobl.codpoblamiento_pobl::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_emisario_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_emisario_nucleo TO geopista;
GRANT SELECT ON TABLE v_emisario_nucleo TO consultas;


CREATE OR REPLACE VIEW v_emisario AS 
 SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis
   FROM eiel_t_saneam_tem
  WHERE (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
           FROM v_emisario_nucleo
          ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_emisario
  OWNER TO geopista;
GRANT ALL ON TABLE v_emisario TO geopista;
GRANT SELECT ON TABLE v_emisario TO consultas;

CREATE OR REPLACE VIEW v_emisario_enc AS 
 SELECT DISTINCT ON (eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em) eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_pv.tipo AS tipo_vert, eiel_t_saneam_pv.zona AS zona_vert, sum(eiel_t_saneam_pv.distancia_nucleo) AS distancia
   FROM eiel_t_saneam_tem
   LEFT JOIN eiel_t_saneam_pv ON eiel_t_saneam_pv.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_saneam_pv.codmunic::text = eiel_t_saneam_tem.codmunic::text AND eiel_t_saneam_pv.revision_expirada = 9999999999::bigint::numeric
   JOIN eiel_tr_saneam_tem_pv tempv ON tempv.clave_tem::text = eiel_t_saneam_tem.clave::text AND tempv.codprov_tem::text = eiel_t_saneam_tem.codprov::text AND tempv.codmunic_tem::text = eiel_t_saneam_tem.codmunic::text AND tempv.tramo_em::text = eiel_t_saneam_tem.tramo_em::text AND tempv.revision_expirada = 9999999999::bigint::numeric AND tempv.codprov_pv::text = eiel_t_saneam_pv.codprov::text AND tempv.codmunic_pv::text = eiel_t_saneam_pv.codmunic::text AND tempv.clave_pv::text = eiel_t_saneam_pv.clave::text AND tempv.orden_pv::text = eiel_t_saneam_pv.orden_pv::text
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
   FROM v_emisario_nucleo
  ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_pv.tipo, eiel_t_saneam_pv.zona
  ORDER BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_emisario_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_emisario_enc TO geopista;
GRANT SELECT ON TABLE v_emisario_enc TO consultas;

CREATE OR REPLACE VIEW v_emisario_enc_m50 AS 
 SELECT DISTINCT ON (eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em) eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_pv.tipo AS tipo_vert, eiel_t_saneam_pv.zona AS zona_vert, sum(eiel_t_saneam_pv.distancia_nucleo) AS distancia
   FROM eiel_t_saneam_tem
   LEFT JOIN eiel_t_saneam_pv ON eiel_t_saneam_pv.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_saneam_pv.codmunic::text = eiel_t_saneam_tem.codmunic::text AND eiel_t_saneam_pv.revision_expirada = 9999999999::bigint::numeric
   JOIN eiel_tr_saneam_tem_pv tempv ON tempv.clave_tem::text = eiel_t_saneam_tem.clave::text AND tempv.codprov_tem::text = eiel_t_saneam_tem.codprov::text AND tempv.codmunic_tem::text = eiel_t_saneam_tem.codmunic::text AND tempv.tramo_em::text = eiel_t_saneam_tem.tramo_em::text AND tempv.revision_expirada = 9999999999::bigint::numeric AND tempv.codprov_pv::text = eiel_t_saneam_pv.codprov::text AND tempv.codmunic_pv::text = eiel_t_saneam_pv.codmunic::text AND tempv.clave_pv::text = eiel_t_saneam_pv.clave::text AND tempv.orden_pv::text = eiel_t_saneam_pv.orden_pv::text
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
   FROM v_emisario_nucleo
  ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_pv.tipo, eiel_t_saneam_pv.zona
  ORDER BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_emisario_enc_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_emisario_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_emisario_enc_m50 TO consultas;



CREATE OR REPLACE VIEW v_entidad_singular AS 
 SELECT eiel_t_entidad_singular.codprov AS provincia, eiel_t_entidad_singular.codmunic AS municipio, eiel_t_entidad_singular.codentidad AS entidad, eiel_t_entidad_singular.denominacion AS denominaci
   FROM eiel_t_entidad_singular
  WHERE eiel_t_entidad_singular.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_entidad_singular
  OWNER TO geopista;
GRANT ALL ON TABLE v_entidad_singular TO geopista;
GRANT SELECT ON TABLE v_entidad_singular TO consultas;

CREATE OR REPLACE VIEW v_infraestr_viaria AS 
 SELECT DISTINCT ON (eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado) eiel_c_redviaria_tu.codprov AS provincia, eiel_c_redviaria_tu.codmunic AS municipio, eiel_c_redviaria_tu.codentidad AS entidad, eiel_c_redviaria_tu.codpoblamiento AS poblamient, eiel_c_redviaria_tu.tipo AS tipo_infr, eiel_c_redviaria_tu.estado, sum(eiel_c_redviaria_tu.longitud) AS longitud, sum(eiel_c_redviaria_tu.superficie) AS superficie, eiel_c_redviaria_tu.viviendas_afec AS viv_afecta
   FROM eiel_c_redviaria_tu
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_redviaria_tu.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_redviaria_tu.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_redviaria_tu.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_c_redviaria_tu.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_c_redviaria_tu.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_c_redviaria_tu.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_c_redviaria_tu.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  GROUP BY eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado, eiel_c_redviaria_tu.viviendas_afec
  ORDER BY eiel_c_redviaria_tu.codprov, eiel_c_redviaria_tu.codmunic, eiel_c_redviaria_tu.codentidad, eiel_c_redviaria_tu.codpoblamiento, eiel_c_redviaria_tu.tipo, eiel_c_redviaria_tu.estado;

ALTER TABLE v_infraestr_viaria
  OWNER TO geopista;
GRANT ALL ON TABLE v_infraestr_viaria TO geopista;
GRANT SELECT ON TABLE v_infraestr_viaria TO consultas;

CREATE OR REPLACE VIEW v_inst_depor_deporte AS 
 SELECT DISTINCT eiel_t_id_deportes.clave, eiel_t_id_deportes.codprov AS provincia, eiel_t_id_deportes.codmunic AS municipio, eiel_t_id_deportes.codentidad AS entidad, eiel_t_id_deportes.codpoblamiento AS poblamient, eiel_t_id_deportes.orden_id AS orden_inst, eiel_t_id_deportes.tipo_deporte AS tipo_depor
   FROM eiel_t_id_deportes
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_id_deportes.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_id_deportes.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_id_deportes.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_id_deportes.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_id_deportes.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_id_deportes.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_id_deportes.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  ORDER BY eiel_t_id_deportes.clave, eiel_t_id_deportes.codprov, eiel_t_id_deportes.codmunic, eiel_t_id_deportes.codentidad, eiel_t_id_deportes.codpoblamiento, eiel_t_id_deportes.orden_id, eiel_t_id_deportes.tipo_deporte;

ALTER TABLE v_inst_depor_deporte
  OWNER TO geopista;
GRANT ALL ON TABLE v_inst_depor_deporte TO geopista;
GRANT SELECT ON TABLE v_inst_depor_deporte TO consultas;

CREATE OR REPLACE VIEW v_instal_deportiva AS 
 SELECT eiel_t_id.clave, eiel_t_id.codprov AS provincia, eiel_t_id.codmunic AS municipio, eiel_t_id.codentidad AS entidad, eiel_t_id.codpoblamiento AS poblamient, eiel_t_id.orden_id AS orden_inst, eiel_t_id.nombre, eiel_t_id.tipo AS tipo_insde, eiel_t_id.titular, eiel_t_id.gestor AS gestion, eiel_t_id.s_cubierta AS s_cubi, eiel_t_id.s_aire, eiel_t_id.s_solar AS s_sola, eiel_t_id.acceso_s_ruedas, eiel_t_id.estado
   FROM eiel_t_id
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_id.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_id.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_id.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_id.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_id.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_id.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_id.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_instal_deportiva
  OWNER TO geopista;
GRANT ALL ON TABLE v_instal_deportiva TO geopista;
GRANT SELECT ON TABLE v_instal_deportiva TO consultas;

CREATE OR REPLACE VIEW v_integ_depositos AS 
 SELECT ((eiel_t_abast_de.clave::text || eiel_t_abast_de.codprov::text) || eiel_t_abast_de.codmunic::text) || eiel_t_abast_de.orden_de::text AS union_clave_eiel, ''::text AS nombre, eiel_t_abast_de.estado, eiel_t_abast_de.gestor, eiel_c_abast_de.id AS id_c
   FROM eiel_t_abast_de
   LEFT JOIN eiel_c_abast_de ON eiel_c_abast_de.clave::text = eiel_t_abast_de.clave::text AND eiel_c_abast_de.codprov::text = eiel_t_abast_de.codprov::text AND eiel_t_abast_de.codmunic::text = eiel_c_abast_de.codmunic::text AND eiel_c_abast_de.orden_de::text = eiel_t_abast_de.orden_de::text
  WHERE eiel_t_abast_de.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_depositos
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_depositos TO geopista;

CREATE OR REPLACE VIEW v_integ_carreteras AS 
 SELECT eiel_t_carreteras.codprov::text || eiel_t_carreteras.cod_carrt::text AS union_clave_eiel, eiel_t_carreteras.denominacion AS nombre, ''::text AS estado, ''::text AS gestor, eiel_c_tramos_carreteras.id AS id_c
   FROM eiel_t_carreteras
   LEFT JOIN eiel_c_tramos_carreteras ON eiel_t_carreteras.codprov::text = eiel_c_tramos_carreteras.codprov::text AND eiel_t_carreteras.cod_carrt::text = eiel_c_tramos_carreteras.cod_carrt::text
  WHERE eiel_t_carreteras.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_carreteras
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_carreteras TO geopista;

CREATE OR REPLACE VIEW v_integ_casaconsistorial AS 
 SELECT ((((eiel_t_cc.clave::text || eiel_t_cc.codprov::text) || eiel_t_cc.codmunic::text) || eiel_t_cc.codentidad::text) || eiel_t_cc.codpoblamiento::text) || eiel_t_cc.orden_cc::text AS union_clave_eiel, eiel_t_cc.nombre, eiel_t_cc.estado, ''::text AS gestor, eiel_c_cc.id AS id_c
   FROM eiel_t_cc
   LEFT JOIN eiel_c_cc ON eiel_t_cc.clave::text = eiel_c_cc.clave::text AND eiel_t_cc.codprov::text = eiel_c_cc.codprov::text AND eiel_t_cc.codmunic::text = eiel_c_cc.codmunic::text AND eiel_t_cc.codentidad::text = eiel_c_cc.codentidad::text AND eiel_t_cc.codpoblamiento::text = eiel_c_cc.codpoblamiento::text AND eiel_t_cc.orden_cc::text = eiel_c_cc.orden_cc::text
  WHERE eiel_t_cc.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_casaconsistorial
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_casaconsistorial TO geopista;
GRANT SELECT ON TABLE v_integ_casaconsistorial TO consultas;

CREATE OR REPLACE VIEW v_integ_cementerio AS 
 SELECT ((((eiel_t_ce.clave::text || eiel_t_ce.codprov::text) || eiel_t_ce.codmunic::text) || eiel_t_ce.codentidad::text) || eiel_t_ce.codpoblamiento::text) || eiel_t_ce.orden_ce::text AS union_clave_eiel, eiel_t_ce.nombre, ''::text AS estado, ''::text AS gestor, eiel_c_ce.id AS id_c
   FROM eiel_t_ce
   LEFT JOIN eiel_c_ce ON eiel_t_ce.clave::text = eiel_c_ce.clave::text AND eiel_t_ce.codprov::text = eiel_c_ce.codprov::text AND eiel_t_ce.codmunic::text = eiel_c_ce.codmunic::text AND eiel_t_ce.codentidad::text = eiel_c_ce.codentidad::text AND eiel_t_ce.codpoblamiento::text = eiel_c_ce.codpoblamiento::text AND eiel_t_ce.orden_ce::text = eiel_c_ce.orden_ce::text
  WHERE eiel_t_ce.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_cementerio
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_cementerio TO geopista;
GRANT SELECT ON TABLE v_integ_cementerio TO consultas;

CREATE OR REPLACE VIEW v_integ_centroasistencial AS 
 SELECT ((((eiel_t_as.clave::text || eiel_t_as.codprov::text) || eiel_t_as.codmunic::text) || eiel_t_as.codentidad::text) || eiel_t_as.codpoblamiento::text) || eiel_t_as.orden_as::text AS union_clave_eiel, eiel_t_as.nombre, eiel_t_as.estado, eiel_t_as.gestor, eiel_c_as.id AS id_c
   FROM eiel_t_as
   LEFT JOIN eiel_c_as ON eiel_t_as.clave::text = eiel_c_as.clave::text AND eiel_t_as.codprov::text = eiel_c_as.codprov::text AND eiel_t_as.codmunic::text = eiel_c_as.codmunic::text AND eiel_t_as.codentidad::text = eiel_c_as.codentidad::text AND eiel_t_as.codpoblamiento::text = eiel_c_as.codpoblamiento::text AND eiel_t_as.orden_as::text = eiel_c_as.orden_as::text
  WHERE eiel_t_as.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_centroasistencial
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_centroasistencial TO geopista;
GRANT SELECT ON TABLE v_integ_centroasistencial TO consultas;

CREATE OR REPLACE VIEW v_integ_centrocultural AS 
 SELECT ((((eiel_t_cu.clave::text || eiel_t_cu.codprov::text) || eiel_t_cu.codmunic::text) || eiel_t_cu.codentidad::text) || eiel_t_cu.codpoblamiento::text) || eiel_t_cu.orden_cu::text AS union_clave_eiel, eiel_t_cu.nombre, eiel_t_cu.estado, eiel_t_cu.gestor, eiel_c_cu.id AS id_c
   FROM eiel_t_cu
   LEFT JOIN eiel_c_cu ON eiel_t_cu.clave::text = eiel_c_cu.clave::text AND eiel_t_cu.codprov::text = eiel_c_cu.codprov::text AND eiel_t_cu.codmunic::text = eiel_c_cu.codmunic::text AND eiel_t_cu.codentidad::text = eiel_c_cu.codentidad::text AND eiel_t_cu.codpoblamiento::text = eiel_c_cu.codpoblamiento::text AND eiel_t_cu.orden_cu::text = eiel_c_cu.orden_cu::text
  WHERE eiel_t_cu.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_centrocultural
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_centrocultural TO geopista;
GRANT SELECT ON TABLE v_integ_centrocultural TO consultas;

CREATE OR REPLACE VIEW v_integ_centroensenianza AS 
 SELECT ((((eiel_t_en.clave::text || eiel_t_en.codprov::text) || eiel_t_en.codmunic::text) || eiel_t_en.codentidad::text) || eiel_t_en.codpoblamiento::text) || eiel_t_en.orden_en::text AS union_clave_eiel, eiel_t_en.nombre, eiel_t_en.estado, ''::text AS gestor, eiel_c_en.id AS id_c
   FROM eiel_t_en
   LEFT JOIN eiel_c_en ON eiel_t_en.clave::text = eiel_c_en.clave::text AND eiel_t_en.codprov::text = eiel_c_en.codprov::text AND eiel_t_en.codmunic::text = eiel_c_en.codmunic::text AND eiel_t_en.codentidad::text = eiel_c_en.codentidad::text AND eiel_t_en.codpoblamiento::text = eiel_c_en.codpoblamiento::text AND eiel_t_en.orden_en::text = eiel_c_en.orden_en::text
  WHERE eiel_t_en.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_centroensenianza
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_centroensenianza TO geopista;
GRANT SELECT ON TABLE v_integ_centroensenianza TO consultas;

CREATE OR REPLACE VIEW v_integ_centrosanitario AS 
 SELECT ((((eiel_t_sa.clave::text || eiel_t_sa.codprov::text) || eiel_t_sa.codmunic::text) || eiel_t_sa.codentidad::text) || eiel_t_sa.codpoblamiento::text) || eiel_t_sa.orden_sa::text AS union_clave_eiel, eiel_t_sa.nombre, eiel_t_sa.estado, eiel_t_sa.gestor, eiel_c_sa.id AS id_c
   FROM eiel_t_sa
   LEFT JOIN eiel_c_sa ON eiel_t_sa.clave::text = eiel_c_sa.clave::text AND eiel_t_sa.codprov::text = eiel_c_sa.codprov::text AND eiel_t_sa.codmunic::text = eiel_c_sa.codmunic::text AND eiel_t_sa.codentidad::text = eiel_c_sa.codentidad::text AND eiel_t_sa.codpoblamiento::text = eiel_c_sa.codpoblamiento::text AND eiel_t_sa.orden_sa::text = eiel_c_sa.orden_sa::text
  WHERE eiel_t_sa.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_centrosanitario
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_centrosanitario TO geopista;
GRANT SELECT ON TABLE v_integ_centrosanitario TO consultas;

CREATE OR REPLACE VIEW v_integ_depuradoras AS 
 SELECT ((eiel_t2_saneam_ed.clave::text || eiel_t2_saneam_ed.codprov::text) || eiel_t2_saneam_ed.codmunic::text) || eiel_t2_saneam_ed.orden_ed::text AS union_clave_eiel, ''::text AS nombre, ''::text AS estado, eiel_t2_saneam_ed.gestor, eiel_c_saneam_ed.id AS id_c
   FROM eiel_t2_saneam_ed
   LEFT JOIN eiel_c_saneam_ed ON eiel_t2_saneam_ed.clave::text = eiel_c_saneam_ed.clave::text AND eiel_t2_saneam_ed.clave::text = eiel_c_saneam_ed.clave::text AND eiel_t2_saneam_ed.clave::text = eiel_c_saneam_ed.clave::text AND eiel_t2_saneam_ed.clave::text = eiel_c_saneam_ed.clave::text
  WHERE eiel_t2_saneam_ed.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_depuradoras
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_depuradoras TO geopista;
GRANT SELECT ON TABLE v_integ_depuradoras TO consultas;

CREATE OR REPLACE VIEW v_integ_edificiosinuso AS 
 SELECT ((((eiel_t_su.clave::text || eiel_t_su.codprov::text) || eiel_t_su.codmunic::text) || eiel_t_su.codentidad::text) || eiel_t_su.codpoblamiento::text) || eiel_t_su.orden_su::text AS union_clave_eiel, eiel_t_su.nombre, eiel_t_su.estado, ''::text AS gestor, eiel_c_su.id AS id_c
   FROM eiel_t_su
   LEFT JOIN eiel_c_su ON eiel_t_su.clave::text = eiel_c_su.clave::text AND eiel_t_su.codprov::text = eiel_c_su.codprov::text AND eiel_t_su.codmunic::text = eiel_c_su.codmunic::text AND eiel_t_su.codentidad::text = eiel_c_su.codentidad::text AND eiel_t_su.codpoblamiento::text = eiel_c_su.codpoblamiento::text AND eiel_t_su.orden_su::text = eiel_c_su.orden_su::text
  WHERE eiel_t_su.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_edificiosinuso
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_edificiosinuso TO geopista;
GRANT SELECT ON TABLE v_integ_edificiosinuso TO consultas;

CREATE OR REPLACE VIEW v_integ_incendiosproteccion AS 
 SELECT ((((eiel_t_ip.clave::text || eiel_t_ip.codprov::text) || eiel_t_ip.codmunic::text) || eiel_t_ip.codentidad::text) || eiel_t_ip.codpoblamiento::text) || eiel_t_ip.orden_ip::text AS union_clave_eiel, eiel_t_ip.nombre, eiel_t_ip.estado, eiel_t_ip.gestor, eiel_c_ip.id AS id_c
   FROM eiel_t_ip
   LEFT JOIN eiel_c_ip ON eiel_t_ip.clave::text = eiel_c_ip.clave::text AND eiel_t_ip.codprov::text = eiel_c_ip.codprov::text AND eiel_t_ip.codmunic::text = eiel_c_ip.codmunic::text AND eiel_t_ip.codentidad::text = eiel_c_ip.codentidad::text AND eiel_t_ip.codpoblamiento::text = eiel_c_ip.codpoblamiento::text AND eiel_t_ip.orden_ip::text = eiel_c_ip.orden_ip::text
  WHERE eiel_t_ip.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_incendiosproteccion
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_incendiosproteccion TO geopista;
GRANT SELECT ON TABLE v_integ_incendiosproteccion TO consultas;

CREATE OR REPLACE VIEW v_integ_instalaciondeportiva AS 
 SELECT ((((eiel_t_id.clave::text || eiel_t_id.codprov::text) || eiel_t_id.codmunic::text) || eiel_t_id.codentidad::text) || eiel_t_id.codpoblamiento::text) || eiel_t_id.orden_id::text AS union_clave_eiel, eiel_t_id.nombre, eiel_t_id.estado, eiel_t_id.gestor, eiel_c_id.id AS id_c
   FROM eiel_t_id
   LEFT JOIN eiel_c_id ON eiel_t_id.clave::text = eiel_c_id.clave::text AND eiel_t_id.codprov::text = eiel_c_id.codprov::text AND eiel_t_id.codmunic::text = eiel_c_id.codmunic::text AND eiel_t_id.codentidad::text = eiel_c_id.codentidad::text AND eiel_t_id.codpoblamiento::text = eiel_c_id.codpoblamiento::text AND eiel_t_id.orden_id::text = eiel_c_id.orden_id::text
  WHERE eiel_t_id.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_instalaciondeportiva
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_instalaciondeportiva TO geopista;
GRANT SELECT ON TABLE v_integ_instalaciondeportiva TO consultas;

CREATE OR REPLACE VIEW v_integ_lonjasmercados AS 
 SELECT ((((eiel_t_lm.clave::text || eiel_t_lm.codprov::text) || eiel_t_lm.codmunic::text) || eiel_t_lm.codentidad::text) || eiel_t_lm.codpoblamiento::text) || eiel_t_lm.orden_lm::text AS union_clave_eiel, eiel_t_lm.nombre, eiel_t_lm.estado, eiel_t_lm.gestor, eiel_c_lm.id AS id_c
   FROM eiel_t_lm
   LEFT JOIN eiel_c_lm ON eiel_t_lm.clave::text = eiel_c_lm.clave::text AND eiel_t_lm.codprov::text = eiel_c_lm.codprov::text AND eiel_t_lm.codmunic::text = eiel_c_lm.codmunic::text AND eiel_t_lm.codentidad::text = eiel_c_lm.codentidad::text AND eiel_t_lm.codpoblamiento::text = eiel_c_lm.codpoblamiento::text AND eiel_t_lm.orden_lm::text = eiel_c_lm.orden_lm::text
  WHERE eiel_t_lm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_lonjasmercados
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_lonjasmercados TO geopista;
GRANT SELECT ON TABLE v_integ_lonjasmercados TO consultas;

CREATE OR REPLACE VIEW v_integ_mataderos AS 
 SELECT ((((eiel_t_mt.clave::text || eiel_t_mt.codprov::text) || eiel_t_mt.codmunic::text) || eiel_t_mt.codentidad::text) || eiel_t_mt.codpoblamiento::text) || eiel_t_mt.orden_mt::text AS union_clave_eiel, eiel_t_mt.nombre, eiel_t_mt.estado, eiel_t_mt.gestor, eiel_c_mt.id AS id_c
   FROM eiel_t_mt
   LEFT JOIN eiel_c_mt ON eiel_t_mt.clave::text = eiel_c_mt.clave::text AND eiel_t_mt.codprov::text = eiel_c_mt.codprov::text AND eiel_t_mt.codmunic::text = eiel_c_mt.codmunic::text AND eiel_t_mt.codentidad::text = eiel_c_mt.codentidad::text AND eiel_t_mt.codpoblamiento::text = eiel_c_mt.codpoblamiento::text AND eiel_t_mt.orden_mt::text = eiel_c_mt.orden_mt::text
  WHERE eiel_t_mt.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_mataderos
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_mataderos TO geopista;
GRANT SELECT ON TABLE v_integ_mataderos TO consultas;

CREATE OR REPLACE VIEW v_integ_parquesjardines AS 
 SELECT ((((eiel_t_pj.clave::text || eiel_t_pj.codprov::text) || eiel_t_pj.codmunic::text) || eiel_t_pj.codentidad::text) || eiel_t_pj.codpoblamiento::text) || eiel_t_pj.orden_pj::text AS union_clave_eiel, eiel_t_pj.nombre, eiel_t_pj.estado, eiel_t_pj.gestor, eiel_c_pj.id AS id_c
   FROM eiel_t_pj
   LEFT JOIN eiel_c_pj ON eiel_t_pj.clave::text = eiel_c_pj.clave::text AND eiel_t_pj.codprov::text = eiel_c_pj.codprov::text AND eiel_t_pj.codmunic::text = eiel_c_pj.codmunic::text AND eiel_t_pj.codentidad::text = eiel_c_pj.codentidad::text AND eiel_t_pj.codpoblamiento::text = eiel_c_pj.codpoblamiento::text AND eiel_t_pj.orden_pj::text = eiel_c_pj.orden_pj::text
  WHERE eiel_t_pj.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_parquesjardines
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_parquesjardines TO geopista;
GRANT SELECT ON TABLE v_integ_parquesjardines TO consultas;

CREATE OR REPLACE VIEW v_integ_tanatorio AS 
 SELECT ((((eiel_t_ta.clave::text || eiel_t_ta.codprov::text) || eiel_t_ta.codmunic::text) || eiel_t_ta.codentidad::text) || eiel_t_ta.codpoblamiento::text) || eiel_t_ta.orden_ta::text AS union_clave_eiel, eiel_t_ta.nombre, eiel_t_ta.estado, eiel_t_ta.gestor, eiel_c_ta.id AS id_c
   FROM eiel_t_ta
   LEFT JOIN eiel_c_ta ON eiel_t_ta.clave::text = eiel_c_ta.clave::text AND eiel_t_ta.codprov::text = eiel_c_ta.codprov::text AND eiel_t_ta.codmunic::text = eiel_c_ta.codmunic::text AND eiel_t_ta.codentidad::text = eiel_c_ta.codentidad::text AND eiel_t_ta.codpoblamiento::text = eiel_c_ta.codpoblamiento::text AND eiel_t_ta.orden_ta::text = eiel_c_ta.orden_ta::text
  WHERE eiel_t_ta.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_integ_tanatorio
  OWNER TO geopista;
GRANT ALL ON TABLE v_integ_tanatorio TO geopista;
GRANT SELECT ON TABLE v_integ_tanatorio TO consultas;

CREATE OR REPLACE VIEW v_lonja_merc_feria AS 
 SELECT eiel_t_lm.clave, eiel_t_lm.codprov AS provincia, eiel_t_lm.codmunic AS municipio, eiel_t_lm.codentidad AS entidad, eiel_t_lm.codpoblamiento AS poblamient, eiel_t_lm.orden_lm AS orden_lmf, eiel_t_lm.nombre, eiel_t_lm.tipo AS tipo_lonj, eiel_t_lm.titular, eiel_t_lm.gestor AS gestion, eiel_t_lm.s_cubierta AS s_cubi, eiel_t_lm.s_aire, eiel_t_lm.s_solar AS s_sola, eiel_t_lm.acceso_s_ruedas, eiel_t_lm.estado
   FROM eiel_t_lm
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_lm.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_lm.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_lm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_lm.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_lm.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_lm.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_lm.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_lonja_merc_feria
  OWNER TO geopista;
GRANT ALL ON TABLE v_lonja_merc_feria TO geopista;
GRANT SELECT ON TABLE v_lonja_merc_feria TO consultas;

CREATE OR REPLACE VIEW v_matadero AS 
 SELECT eiel_t_mt.clave, eiel_t_mt.codprov AS provincia, eiel_t_mt.codmunic AS municipio, eiel_t_mt.codentidad AS entidad, eiel_t_mt.codpoblamiento AS poblamient, eiel_t_mt.orden_mt AS orden_mata, eiel_t_mt.nombre, eiel_t_mt.clase AS clase_mat, eiel_t_mt.titular, eiel_t_mt.gestor AS gestion, eiel_t_mt.s_cubierta AS s_cubi, eiel_t_mt.s_aire, eiel_t_mt.s_solar AS s_sola, eiel_t_mt.acceso_s_ruedas, eiel_t_mt.estado, eiel_t_mt.capacidad, eiel_t_mt.utilizacion AS utilizacio, eiel_t_mt.tunel, eiel_t_mt.bovino, eiel_t_mt.ovino, eiel_t_mt.porcino, eiel_t_mt.otros
   FROM eiel_t_mt
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_mt.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_mt.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_mt.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_mt.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_mt.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_mt.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_mt.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_matadero
  OWNER TO geopista;
GRANT ALL ON TABLE v_matadero TO geopista;
GRANT SELECT ON TABLE v_matadero TO consultas;

CREATE OR REPLACE VIEW v_mun_enc_dis AS 
 SELECT eiel_t_mun_diseminados.codprov AS provincia, eiel_t_mun_diseminados.codmunic AS municipio, eiel_t_mun_diseminados.padron_dis AS padron, eiel_t_mun_diseminados.pob_estaci, eiel_t_mun_diseminados.viv_total, eiel_t_mun_diseminados.hoteles, eiel_t_mun_diseminados.casas_rural AS casas_rura, eiel_t_mun_diseminados.longitud, eiel_t_mun_diseminados.aag_v_cone, eiel_t_mun_diseminados.aag_v_ncon, eiel_t_mun_diseminados.aag_c_invi, eiel_t_mun_diseminados.aag_c_vera, eiel_t_mun_diseminados.aag_v_expr, eiel_t_mun_diseminados.aag_v_depr, eiel_t_mun_diseminados.aag_l_defi, eiel_t_mun_diseminados.aag_v_defi, eiel_t_mun_diseminados.aag_pr_def, eiel_t_mun_diseminados.aag_pe_def, eiel_t_mun_diseminados.aau_vivien, eiel_t_mun_diseminados.aau_pob_re, eiel_t_mun_diseminados.aau_pob_es, eiel_t_mun_diseminados.aau_def_vi, eiel_t_mun_diseminados.aau_def_re, eiel_t_mun_diseminados.aau_def_es, eiel_t_mun_diseminados.aau_fecont, eiel_t_mun_diseminados.aau_fencon, eiel_t_mun_diseminados.longi_ramal AS longit_ram, eiel_t_mun_diseminados.syd_v_cone, eiel_t_mun_diseminados.syd_v_ncon, eiel_t_mun_diseminados.syd_l_defi, eiel_t_mun_diseminados.syd_v_defi, eiel_t_mun_diseminados.syd_pr_def, eiel_t_mun_diseminados.syd_pe_def, eiel_t_mun_diseminados.syd_c_desa, eiel_t_mun_diseminados.syd_c_trat, eiel_t_mun_diseminados.sau_vivien, eiel_t_mun_diseminados.sau_pob_re, eiel_t_mun_diseminados.sau_pob_es, eiel_t_mun_diseminados.sau_vi_def, eiel_t_mun_diseminados.sau_pob_re_def AS sau_re_def, eiel_t_mun_diseminados.sau_pob_es_def AS sau_es_def, eiel_t_mun_diseminados.produ_basu, eiel_t_mun_diseminados.contenedores AS contenedor, eiel_t_mun_diseminados.rba_v_sser, eiel_t_mun_diseminados.rba_pr_sse, eiel_t_mun_diseminados.rba_pe_sse, eiel_t_mun_diseminados.rba_plalim, eiel_t_mun_diseminados.puntos_luz, eiel_t_mun_diseminados.alu_v_sin, eiel_t_mun_diseminados.alu_l_sin
   FROM eiel_t_mun_diseminados
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_mun_diseminados.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_mun_diseminados.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_mun_diseminados.revision_expirada = 9999999999::bigint::numeric
  ORDER BY eiel_t_mun_diseminados.codprov, eiel_t_mun_diseminados.codmunic;

ALTER TABLE v_mun_enc_dis
  OWNER TO geopista;
GRANT ALL ON TABLE v_mun_enc_dis TO geopista;
GRANT SELECT ON TABLE v_mun_enc_dis TO consultas;

CREATE OR REPLACE VIEW v_municipio AS 
 SELECT eiel_c_municipios.codprov AS provincia, eiel_c_municipios.codmunic AS municipio, eiel_c_municipios.nombre_oficial AS denominaci
   FROM eiel_c_municipios
  WHERE eiel_c_municipios.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_municipio
  OWNER TO geopista;
GRANT ALL ON TABLE v_municipio TO geopista;
GRANT SELECT ON TABLE v_municipio TO consultas;

CREATE OR REPLACE VIEW v_nivel_ensenanza AS 
 SELECT DISTINCT ON (eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov, eiel_t_en_nivel.codmunic, eiel_t_en_nivel.codentidad, eiel_t_en_nivel.codpoblamiento, eiel_t_en_nivel.orden_en, eiel_t_en_nivel.nivel) eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov AS provincia, eiel_t_en_nivel.codmunic AS municipio, eiel_t_en_nivel.codentidad AS entidad, eiel_t_en_nivel.codpoblamiento AS poblamient, eiel_t_en_nivel.orden_en AS orden_cent, eiel_t_en_nivel.nivel, eiel_t_en_nivel.unidades, eiel_t_en_nivel.plazas, eiel_t_en_nivel.alumnos
   FROM eiel_t_en_nivel
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_en_nivel.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_en_nivel.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_en_nivel.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_en_nivel.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_en_nivel.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_en_nivel.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_en_nivel.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  ORDER BY eiel_t_en_nivel.clave, eiel_t_en_nivel.codprov, eiel_t_en_nivel.codmunic, eiel_t_en_nivel.codentidad, eiel_t_en_nivel.codpoblamiento, eiel_t_en_nivel.orden_en, eiel_t_en_nivel.nivel;

ALTER TABLE v_nivel_ensenanza
  OWNER TO geopista;
GRANT ALL ON TABLE v_nivel_ensenanza TO geopista;
GRANT SELECT ON TABLE v_nivel_ensenanza TO consultas;

CREATE OR REPLACE VIEW v_nuc_abandonado AS 
 SELECT eiel_t_nucleo_aband.codprov AS provincia, eiel_t_nucleo_aband.codmunic AS municipio, eiel_t_nucleo_aband.codentidad AS entidad, eiel_t_nucleo_aband.codpoblamiento AS poblamient, eiel_t_nucleo_aband.a_abandono, eiel_t_nucleo_aband.causa_abandono AS causa_aban, eiel_t_nucleo_aband.titular_abandono AS titular_ab, eiel_t_nucleo_aband.rehabilitacion AS rehabilita, eiel_t_nucleo_aband.acceso AS acceso_nuc, eiel_t_nucleo_aband.serv_agua, eiel_t_nucleo_aband.serv_elect
   FROM eiel_t_nucleo_aband
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucleo_aband.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucleo_aband.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucleo_aband.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_nuc_abandonado
  OWNER TO geopista;
GRANT ALL ON TABLE v_nuc_abandonado TO geopista;
GRANT SELECT ON TABLE v_nuc_abandonado TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_1 AS 
 SELECT eiel_t_nucl_encuest_1.codprov AS provincia, eiel_t_nucl_encuest_1.codmunic AS municipio, eiel_t_nucl_encuest_1.codentidad AS entidad, eiel_t_nucl_encuest_1.codpoblamiento AS nucleo, eiel_t_nucl_encuest_1.padron, eiel_t_nucl_encuest_1.pob_estacional AS pob_estaci, eiel_t_nucl_encuest_1.altitud, eiel_t_nucl_encuest_1.viviendas_total AS viv_total, eiel_t_nucl_encuest_1.hoteles, eiel_t_nucl_encuest_1.casas_rural AS casas_rura, eiel_t_nucl_encuest_1.accesibilidad AS accesib
   FROM eiel_t_nucl_encuest_1
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucl_encuest_1.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucl_encuest_1.codmunic::text, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_nucl_encuestado_1
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_1 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_1 TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_2 AS 
 SELECT eiel_t_nucl_encuest_2.codprov AS provincia, eiel_t_nucl_encuest_2.codmunic AS municipio, eiel_t_nucl_encuest_2.codentidad AS entidad, eiel_t_nucl_encuest_2.codpoblamiento AS nucleo, eiel_t_nucl_encuest_2.aag_caudal, eiel_t_nucl_encuest_2.aag_restri, eiel_t_nucl_encuest_2.aag_contad, eiel_t_nucl_encuest_2.aag_tasa, eiel_t_nucl_encuest_2.aag_instal, eiel_t_nucl_encuest_2.aag_hidran, eiel_t_nucl_encuest_2.aag_est_hi, eiel_t_nucl_encuest_2.aag_valvul, eiel_t_nucl_encuest_2.aag_est_va, eiel_t_nucl_encuest_2.aag_bocasr, eiel_t_nucl_encuest_2.aag_est_bo, eiel_t_nucl_encuest_2.cisterna
   FROM eiel_t_nucl_encuest_2
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_nucl_encuest_2.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_nucl_encuest_2.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_2.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_nucl_encuest_2.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_nucl_encuest_2.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_nucl_encuest_2.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_nucl_encuest_2.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_nucl_encuestado_2
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_2 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_2 TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_3 AS 
 SELECT eiel_t_abast_serv.codprov AS provincia, eiel_t_abast_serv.codmunic AS municipio, eiel_t_abast_serv.codentidad AS entidad, eiel_t_abast_serv.codpoblamiento AS nucleo, eiel_t_abast_serv.viviendas_c_conex AS aag_v_cone, eiel_t_abast_serv.viviendas_s_conexion AS aag_v_ncon, eiel_t_abast_serv.consumo_inv AS aag_c_invi, eiel_t_abast_serv.consumo_verano AS aag_c_vera, eiel_t_abast_serv.viv_exceso_pres AS aag_v_expr, eiel_t_abast_serv.viv_defic_presion AS aag_v_depr, eiel_t_abast_serv.perdidas_agua AS aag_perdid, eiel_t_abast_serv.calidad_serv AS aag_calida, eiel_t_abast_serv.long_deficit AS aag_l_defi, eiel_t_abast_serv.viv_deficitarias AS aag_v_defi, eiel_t_abast_serv.pobl_res_afect AS aag_pr_def, eiel_t_abast_serv.pobl_est_afect AS aag_pe_def
   FROM eiel_t_abast_serv
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_serv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_serv.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_serv.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_abast_serv.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_abast_serv.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_abast_serv.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_abast_serv.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_nucl_encuestado_3
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_3 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_3 TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_4 AS 
 SELECT eiel_t_abast_au.codprov AS provincia, eiel_t_abast_au.codmunic AS municipio, eiel_t_abast_au.codentidad AS entidad, eiel_t_abast_au.codpoblamiento AS nucleo, eiel_t_abast_au.aau_vivien, eiel_t_abast_au.aau_pob_re, eiel_t_abast_au.aau_pob_es, eiel_t_abast_au.aau_def_vi, eiel_t_abast_au.aau_def_re, eiel_t_abast_au.aau_def_es, eiel_t_abast_au.aau_fecont, eiel_t_abast_au.aau_fencon, eiel_t_abast_au.aau_caudal
   FROM eiel_t_abast_au
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_au.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_au.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_au.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_abast_au.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_abast_au.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_abast_au.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_abast_au.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_nucl_encuestado_4
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_4 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_4 TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_5 AS 
 SELECT eiel_t_saneam_serv.codprov AS provincia, eiel_t_saneam_serv.codmunic AS municipio, eiel_t_saneam_serv.codentidad AS entidad, eiel_t_saneam_serv.codpoblamiento AS nucleo, eiel_t_saneam_serv.pozos_registro AS syd_pozos, eiel_t_saneam_serv.sumideros AS syd_sumide, eiel_t_saneam_serv.aliv_c_acum AS syd_ali_co, eiel_t_saneam_serv.aliv_s_acum AS syd_ali_si, eiel_t_saneam_serv.calidad_serv AS syd_calida, eiel_t_saneam_serv.viviendas_c_conex AS syd_v_cone, eiel_t_saneam_serv.viviendas_s_conex AS syd_v_ncon, eiel_t_saneam_serv.long_rs_deficit AS syd_l_defi, eiel_t_saneam_serv.viviendas_def_conex AS syd_v_defi, eiel_t_saneam_serv.pobl_res_def_afect AS syd_pr_def, eiel_t_saneam_serv.pobl_est_def_afect AS syd_pe_def, eiel_t_saneam_serv.caudal_total AS syd_c_desa, eiel_t_saneam_serv.caudal_tratado AS syd_c_trat, eiel_t_saneam_serv.c_reutilizado_urb AS syd_re_urb, eiel_t_saneam_serv.c_reutilizado_rust AS syd_re_rus, eiel_t_saneam_serv.c_reutilizado_ind AS syd_re_ind
   FROM eiel_t_saneam_serv
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_serv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_serv.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_serv.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_saneam_serv.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_saneam_serv.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_saneam_serv.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_saneam_serv.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_nucl_encuestado_5
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_5 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_5 TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_6 AS 
 SELECT eiel_t_rb_serv.codprov AS provincia, eiel_t_rb_serv.codmunic AS municipio, eiel_t_rb_serv.codentidad AS entidad, eiel_t_rb_serv.codpoblamiento AS nucleo, eiel_t_rb_serv.srb_viviendas_afec AS rba_v_sser, eiel_t_rb_serv.srb_pob_res_afect AS rba_pr_sse, eiel_t_rb_serv.srb_pob_est_afect AS rba_pe_sse, eiel_t_rb_serv.serv_limp_calles AS rba_serlim, eiel_t_rb_serv.plantilla_serv_limp AS rba_plalim
   FROM eiel_t_rb_serv
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_rb_serv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_rb_serv.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_rb_serv.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_rb_serv.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_rb_serv.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_rb_serv.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_rb_serv.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_nucl_encuestado_6
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_6 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_6 TO consultas;

CREATE OR REPLACE VIEW v_nucl_encuestado_7 AS 
 SELECT eiel_t_inf_ttmm.codprov AS provincia, eiel_t_inf_ttmm.codmunic AS municipio, eiel_t_inf_ttmm.codentidad AS entidad, eiel_t_inf_ttmm.codpoblamiento AS poblamient, eiel_t_inf_ttmm.tv_ant, eiel_t_inf_ttmm.tv_ca, eiel_t_inf_ttmm.tm_gsm, eiel_t_inf_ttmm.tm_umts, eiel_t_inf_ttmm.tm_gprs, eiel_t_inf_ttmm.correo, eiel_t_inf_ttmm.ba_rd, eiel_t_inf_ttmm.ba_xd, eiel_t_inf_ttmm.ba_wi, eiel_t_inf_ttmm.ba_ca, eiel_t_inf_ttmm.ba_rb, eiel_t_inf_ttmm.ba_st, eiel_t_inf_ttmm.capi, eiel_t_inf_ttmm.electric AS electricid, eiel_t_inf_ttmm.gas, eiel_t_inf_ttmm.alu_v_sin, eiel_t_inf_ttmm.alu_l_sin
   FROM eiel_t_inf_ttmm
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_inf_ttmm.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_inf_ttmm.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_inf_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_inf_ttmm.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_inf_ttmm.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_inf_ttmm.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_inf_ttmm.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_nucl_encuestado_7
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucl_encuestado_7 TO geopista;
GRANT SELECT ON TABLE v_nucl_encuestado_7 TO consultas;

CREATE OR REPLACE VIEW v_nucleo_poblacion AS 
 SELECT eiel_c_nucleo_poblacion.codprov AS provincia, eiel_c_nucleo_poblacion.codmunic AS municipio, eiel_c_nucleo_poblacion.codentidad AS entidad, eiel_c_nucleo_poblacion.codpoblamiento AS poblamient, eiel_c_nucleo_poblacion.nombre_oficial AS denominaci
   FROM eiel_c_nucleo_poblacion
  WHERE eiel_c_nucleo_poblacion.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_nucleo_poblacion
  OWNER TO geopista;
GRANT ALL ON TABLE v_nucleo_poblacion TO geopista;
GRANT SELECT ON TABLE v_nucleo_poblacion TO consultas;

CREATE OR REPLACE VIEW v_ot_serv_municipal AS 
 SELECT eiel_t_otros_serv_munic.codprov AS provincia, eiel_t_otros_serv_munic.codmunic AS municipio, eiel_t_otros_serv_munic.sw_inf_grl, eiel_t_otros_serv_munic.sw_inf_tur, eiel_t_otros_serv_munic.sw_gb_elec, eiel_t_otros_serv_munic.ord_soterr, eiel_t_otros_serv_munic.en_eolica, eiel_t_otros_serv_munic.kw_eolica, eiel_t_otros_serv_munic.en_solar, eiel_t_otros_serv_munic.kw_eolica AS kw_solar, eiel_t_otros_serv_munic.pl_mareo, eiel_t_otros_serv_munic.kw_mareo, eiel_t_otros_serv_munic.ot_energ, eiel_t_otros_serv_munic.kw_ot_energ AS kw_energ, eiel_t_otros_serv_munic.cob_serv_tlf_m AS cob_serv_telf_m, eiel_t_otros_serv_munic.tv_dig_cable
   FROM eiel_t_otros_serv_munic
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_otros_serv_munic.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_otros_serv_munic.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_otros_serv_munic.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_ot_serv_municipal
  OWNER TO geopista;
GRANT ALL ON TABLE v_ot_serv_municipal TO geopista;
GRANT SELECT ON TABLE v_ot_serv_municipal TO consultas;

CREATE OR REPLACE VIEW v_padron AS 
 SELECT eiel_t_padron_ttmm.codprov AS provincia, eiel_t_padron_ttmm.codmunic AS municipio, eiel_t_padron_ttmm.n_hombres_a1 AS hombres, eiel_t_padron_ttmm.n_mujeres_a1 AS mujeres, eiel_t_padron_ttmm.total_poblacion_a1 AS total_pob
   FROM eiel_t_padron_ttmm
  WHERE eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_padron
  OWNER TO geopista;
GRANT ALL ON TABLE v_padron TO geopista;
GRANT SELECT ON TABLE v_padron TO consultas;

CREATE OR REPLACE VIEW v_parque AS 
 SELECT eiel_t_pj.clave, eiel_t_pj.codprov AS provincia, eiel_t_pj.codmunic AS municipio, eiel_t_pj.codentidad AS entidad, eiel_t_pj.codpoblamiento AS poblamient, eiel_t_pj.orden_pj AS orden_parq, eiel_t_pj.nombre, eiel_t_pj.tipo AS tipo_parq, eiel_t_pj.titular, eiel_t_pj.gestor AS gestion, eiel_t_pj.s_cubierta AS s_cubi, eiel_t_pj.s_aire, eiel_t_pj.s_solar AS s_sola, eiel_t_pj.agua, eiel_t_pj.saneamiento AS saneamient, eiel_t_pj.electricidad AS electricid, eiel_t_pj.comedor, eiel_t_pj.juegos_inf, eiel_t_pj.otras, eiel_t_pj.acceso_s_ruedas, eiel_t_pj.estado
   FROM eiel_t_pj
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_pj.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_pj.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_pj.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_pj.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_pj.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_pj.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_pj.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_parque
  OWNER TO geopista;
GRANT ALL ON TABLE v_parque TO geopista;
GRANT SELECT ON TABLE v_parque TO consultas;

CREATE OR REPLACE VIEW v_plan_urbanistico AS 
 SELECT eiel_t_planeam_urban.codprov AS provincia, eiel_t_planeam_urban.codmunic AS municipio, eiel_t_planeam_urban.tipo_urba, eiel_t_planeam_urban.estado_tramit AS estado_tra, eiel_t_planeam_urban.denominacion AS denominaci, eiel_t_planeam_urban.sup_muni AS superficie, eiel_t_planeam_urban.fecha_bo AS bo, eiel_t_planeam_urban.s_urbano AS urban, eiel_t_planeam_urban.s_no_urbanizable AS no_urbable, eiel_t_planeam_urban.s_no_urban_especial AS nourbable_
   FROM eiel_t_planeam_urban
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_planeam_urban.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_planeam_urban.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_planeam_urban.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_plan_urbanistico
  OWNER TO geopista;
GRANT ALL ON TABLE v_plan_urbanistico TO geopista;
GRANT SELECT ON TABLE v_plan_urbanistico TO consultas;

CREATE OR REPLACE VIEW v_poblamiento AS 
 SELECT eiel_t_poblamiento.codprov AS provincia, eiel_t_poblamiento.codmunic AS municipio, eiel_t_poblamiento.codentidad AS entidad, eiel_t_poblamiento.codpoblamiento AS poblamiento
   FROM eiel_t_poblamiento
  WHERE eiel_t_poblamiento.revision_expirada = 9999999999::bigint::numeric
  ORDER BY eiel_t_poblamiento.codprov, eiel_t_poblamiento.codmunic, eiel_t_poblamiento.codentidad, eiel_t_poblamiento.codpoblamiento;

ALTER TABLE v_poblamiento
  OWNER TO geopista;
GRANT ALL ON TABLE v_poblamiento TO geopista;
GRANT SELECT ON TABLE v_poblamiento TO consultas;

CREATE OR REPLACE VIEW v_trat_pota_nucleo AS 
 SELECT eiel_tr_abast_tp_pobl.codprov_pobl AS provincia, eiel_tr_abast_tp_pobl.codmunic_pobl AS municipio, eiel_tr_abast_tp_pobl.codentidad_pobl AS entidad, eiel_tr_abast_tp_pobl.codpoblamiento_pobl AS nucleo, eiel_tr_abast_tp_pobl.clave_tp AS clave, eiel_tr_abast_tp_pobl.codprov_tp AS po_provin, eiel_tr_abast_tp_pobl.codmunic_tp AS po_munipi, eiel_tr_abast_tp_pobl.orden_tp AS orden_trat
   FROM eiel_tr_abast_tp_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_abast_tp_pobl.codprov_pobl::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_abast_tp_pobl.codmunic_pobl::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_abast_tp_pobl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_tr_abast_tp_pobl.codprov_pobl::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_tr_abast_tp_pobl.codmunic_pobl::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_tr_abast_tp_pobl.codentidad_pobl::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_tr_abast_tp_pobl.codpoblamiento_pobl::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_trat_pota_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_trat_pota_nucleo TO geopista;
GRANT SELECT ON TABLE v_trat_pota_nucleo TO consultas;

CREATE OR REPLACE VIEW v_potabilizacion_enc AS 
 SELECT eiel_t_abast_tp.clave, eiel_t_abast_tp.codprov AS provincia, eiel_t_abast_tp.codmunic AS municipio, eiel_t_abast_tp.orden_tp AS orden_trat, eiel_t_abast_tp.tipo AS tipo_tra, eiel_t_abast_tp.ubicacion, eiel_t_abast_tp.s_desinf, eiel_t_abast_tp.categoria_a1 AS cat_a1, eiel_t_abast_tp.categoria_a2 AS cat_a2, eiel_t_abast_tp.categoria_a3 AS cat_a3, eiel_t_abast_tp.desaladora, eiel_t_abast_tp.otros, eiel_t_abast_tp.desinf_1, eiel_t_abast_tp.desinf_2, eiel_t_abast_tp.desinf_3, eiel_t_abast_tp.periodicidad AS periodicid, eiel_t_abast_tp.organismo_control AS organismo, eiel_t_abast_tp.estado
   FROM eiel_t_abast_tp
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tp.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tp.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_tp.codmunic::text || eiel_t_abast_tp.orden_tp::text IN ( SELECT DISTINCT v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text
      FROM v_trat_pota_nucleo
     ORDER BY v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text)) AND eiel_t_abast_tp.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_potabilizacion_enc
  OWNER TO geopista;
GRANT ALL ON TABLE v_potabilizacion_enc TO geopista;
GRANT SELECT ON TABLE v_potabilizacion_enc TO consultas;

CREATE OR REPLACE VIEW v_potabilizacion_enc_m50 AS 
 SELECT eiel_t_abast_tp.clave, eiel_t_abast_tp.codprov AS provincia, eiel_t_abast_tp.codmunic AS municipio, eiel_t_abast_tp.orden_tp AS orden_trat, eiel_t_abast_tp.tipo AS tipo_tra, eiel_t_abast_tp.ubicacion, eiel_t_abast_tp.s_desinf, eiel_t_abast_tp.categoria_a1 AS cat_a1, eiel_t_abast_tp.categoria_a2 AS cat_a2, eiel_t_abast_tp.categoria_a3 AS cat_a3, eiel_t_abast_tp.desaladora, eiel_t_abast_tp.otros, eiel_t_abast_tp.desinf_1, eiel_t_abast_tp.desinf_2, eiel_t_abast_tp.desinf_3, eiel_t_abast_tp.periodicidad AS periodicid, eiel_t_abast_tp.organismo_control AS organismo, eiel_t_abast_tp.estado
   FROM eiel_t_abast_tp
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tp.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tp.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_abast_tp.codmunic::text || eiel_t_abast_tp.orden_tp::text IN ( SELECT DISTINCT v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text
      FROM v_trat_pota_nucleo
     ORDER BY v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text)) AND eiel_t_abast_tp.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_potabilizacion_enc_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_potabilizacion_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_potabilizacion_enc_m50 TO consultas;

CREATE OR REPLACE VIEW v_proteccion_civil AS 
 SELECT eiel_t_ip.clave, eiel_t_ip.codprov AS provincia, eiel_t_ip.codmunic AS municipio, eiel_t_ip.codentidad AS entidad, eiel_t_ip.codpoblamiento AS poblamient, eiel_t_ip.orden_ip AS orden_prot, eiel_t_ip.nombre, eiel_t_ip.tipo AS tipo_pciv, eiel_t_ip.titular, eiel_t_ip.gestor AS gestion, eiel_t_ip.ambito, eiel_t_ip.plan_profe, eiel_t_ip.plan_volun, eiel_t_ip.s_cubierta AS s_cubi, eiel_t_ip.s_aire, eiel_t_ip.s_solar AS s_sola, eiel_t_ip.acceso_s_ruedas, eiel_t_ip.estado, eiel_t_ip.vehic_incendio AS vehic_ince, eiel_t_ip.vehic_rescate AS vehic_resc, eiel_t_ip.ambulancia, eiel_t_ip.medios_aereos AS medios_aer, eiel_t_ip.otros_vehc AS otros_vehi, eiel_t_ip.quitanieves AS quitanieve, eiel_t_ip.detec_ince, eiel_t_ip.otros
   FROM eiel_t_ip
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ip.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ip.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ip.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_ip.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_ip.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_ip.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_ip.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_proteccion_civil
  OWNER TO geopista;
GRANT ALL ON TABLE v_proteccion_civil TO geopista;
GRANT SELECT ON TABLE v_proteccion_civil TO consultas;

CREATE OR REPLACE VIEW v_provincia AS 
 SELECT DISTINCT ON (eiel_c_provincia.codprov, eiel_c_provincia.nombre) eiel_c_provincia.codprov AS provincia, eiel_c_provincia.nombre AS denominaci
   FROM eiel_c_provincia
  WHERE eiel_c_provincia.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_provincia
  OWNER TO geopista;
GRANT ALL ON TABLE v_provincia TO geopista;
GRANT SELECT ON TABLE v_provincia TO consultas;

CREATE OR REPLACE VIEW v_ramal_saneamiento AS 
 SELECT eiel_c_saneam_rs.codprov AS provincia, eiel_c_saneam_rs.codmunic AS municipio, eiel_c_saneam_rs.codentidad AS entidad, eiel_c_saneam_rs.codpoblamiento AS nucleo, eiel_c_saneam_rs.material AS tipo_rama, eiel_c_saneam_rs.sist_impulsion AS sist_trans, eiel_c_saneam_rs.estado, eiel_c_saneam_rs.tipo_red_interior AS tipo_red, eiel_c_saneam_rs.titular, eiel_c_saneam_rs.gestor AS gestion, sum(eiel_c_saneam_rs.longitud) AS longit_ram
   FROM eiel_c_saneam_rs
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_rs.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_rs.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_rs.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_c_saneam_rs.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_c_saneam_rs.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_c_saneam_rs.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_c_saneam_rs.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  GROUP BY eiel_c_saneam_rs.codmunic, eiel_c_saneam_rs.codentidad, eiel_c_saneam_rs.codpoblamiento, eiel_c_saneam_rs.codprov, eiel_c_saneam_rs.material, eiel_c_saneam_rs.sist_impulsion, eiel_c_saneam_rs.estado, eiel_c_saneam_rs.tipo_red_interior, eiel_c_saneam_rs.titular, eiel_c_saneam_rs.gestor
  ORDER BY eiel_c_saneam_rs.codmunic, eiel_c_saneam_rs.codentidad, eiel_c_saneam_rs.codpoblamiento;

ALTER TABLE v_ramal_saneamiento
  OWNER TO geopista;
GRANT ALL ON TABLE v_ramal_saneamiento TO geopista;
GRANT SELECT ON TABLE v_ramal_saneamiento TO consultas;

CREATE OR REPLACE VIEW v_recogida_basura AS 
 SELECT eiel_t_rb.codprov AS provincia, eiel_t_rb.codmunic AS municipio, eiel_t_rb.codentidad AS entidad, eiel_t_rb.codpoblamiento AS nucleo, eiel_t_rb.tipo AS tipo_rbas, eiel_t_rb.gestor AS gestion, eiel_t_rb.periodicidad AS periodicid, eiel_t_rb.calidad, eiel_t_rb.tm_res_urb AS produ_basu, eiel_t_rb.n_contenedores AS contenedor
   FROM eiel_t_rb
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_rb.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_rb.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_rb.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_rb.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_rb.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_rb.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_rb.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_recogida_basura
  OWNER TO geopista;
GRANT ALL ON TABLE v_recogida_basura TO geopista;
GRANT SELECT ON TABLE v_recogida_basura TO consultas;

CREATE OR REPLACE VIEW v_red_distribucion AS 
 SELECT eiel_c_abast_rd.codprov AS provincia, eiel_c_abast_rd.codmunic AS municipio, eiel_c_abast_rd.codentidad AS entidad, eiel_c_abast_rd.codpoblamiento AS nucleo, eiel_c_abast_rd.material AS tipo_rdis, eiel_c_abast_rd.sist_trans, eiel_c_abast_rd.estado, eiel_c_abast_rd.titular, eiel_c_abast_rd.gestor AS gestion, sum(eiel_c_abast_rd.longitud) AS longitud
   FROM eiel_c_abast_rd
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_abast_rd.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_abast_rd.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_abast_rd.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_c_abast_rd.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_c_abast_rd.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_c_abast_rd.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_c_abast_rd.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  GROUP BY eiel_c_abast_rd.codprov, eiel_c_abast_rd.codmunic, eiel_c_abast_rd.codentidad, eiel_c_abast_rd.codpoblamiento, eiel_c_abast_rd.material, eiel_c_abast_rd.sist_trans, eiel_c_abast_rd.estado, eiel_c_abast_rd.titular, eiel_c_abast_rd.gestor;

ALTER TABLE v_red_distribucion
  OWNER TO geopista;
GRANT ALL ON TABLE v_red_distribucion TO geopista;
GRANT SELECT ON TABLE v_red_distribucion TO consultas;

CREATE OR REPLACE VIEW v_sanea_autonomo AS 
 SELECT eiel_t_saneam_au.clave, eiel_t_saneam_au.codprov AS provincia, eiel_t_saneam_au.codmunic AS municipio, eiel_t_saneam_au.codentidad AS entidad, eiel_t_saneam_au.codpoblamiento AS nucleo, eiel_t_saneam_au.tipo_sau AS tipo_sanea, eiel_t_saneam_au.estado_sau AS estado, eiel_t_saneam_au.adecuacion_sau AS adecuacion, eiel_t_saneam_au.sau_vivien, eiel_t_saneam_au.sau_pob_re, eiel_t_saneam_au.sau_pob_es, eiel_t_saneam_au.sau_vi_def, eiel_t_saneam_au.sau_pob_re_def AS sau_re_def, eiel_t_saneam_au.sau_pob_es_def AS sau_es_def
   FROM eiel_t_saneam_au
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_au.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_au.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_au.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_saneam_au.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_saneam_au.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_saneam_au.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_saneam_au.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_sanea_autonomo
  OWNER TO geopista;
GRANT ALL ON TABLE v_sanea_autonomo TO geopista;
GRANT SELECT ON TABLE v_sanea_autonomo TO consultas;

CREATE OR REPLACE VIEW v_tanatorio AS 
 SELECT eiel_t_ta.clave, eiel_t_ta.codprov AS provincia, eiel_t_ta.codmunic AS municipio, eiel_t_ta.codentidad AS entidad, eiel_t_ta.codpoblamiento AS poblamient, eiel_t_ta.orden_ta AS orden_tana, eiel_t_ta.nombre, eiel_t_ta.titular, eiel_t_ta.gestor AS gestion, eiel_t_ta.s_cubierta AS s_cubi, eiel_t_ta.s_aire, eiel_t_ta.s_solar AS s_sola, eiel_t_ta.salas, eiel_t_ta.acceso_s_ruedas, eiel_t_ta.estado
   FROM eiel_t_ta
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_ta.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_ta.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_ta.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_t_ta.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_t_ta.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_t_ta.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_t_ta.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas;

ALTER TABLE v_tanatorio
  OWNER TO geopista;
GRANT ALL ON TABLE v_tanatorio TO geopista;
GRANT SELECT ON TABLE v_tanatorio TO consultas;

CREATE OR REPLACE VIEW v_tra_potabilizacion AS 
 SELECT eiel_t_abast_tp.clave, eiel_t_abast_tp.codprov AS provincia, eiel_t_abast_tp.codmunic AS municipio, eiel_t_abast_tp.orden_tp AS orden_trat
   FROM eiel_t_abast_tp
  WHERE (eiel_t_abast_tp.codmunic::text || eiel_t_abast_tp.orden_tp::text IN ( SELECT DISTINCT v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text
           FROM v_trat_pota_nucleo
          ORDER BY v_trat_pota_nucleo.po_munipi::text || v_trat_pota_nucleo.orden_trat::text)) AND eiel_t_abast_tp.revision_expirada = 9999999999::bigint::numeric
  ORDER BY eiel_t_abast_tp.codmunic, eiel_t_abast_tp.orden_tp;

ALTER TABLE v_tra_potabilizacion
  OWNER TO geopista;
GRANT ALL ON TABLE v_tra_potabilizacion TO geopista;
GRANT SELECT ON TABLE v_tra_potabilizacion TO consultas;

CREATE OR REPLACE VIEW v_tramo_carretera AS 
 SELECT DISTINCT ON (eiel_c_tramos_carreteras.codprov, eiel_c_tramos_carreteras.codmunic, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki) eiel_c_tramos_carreteras.codprov AS provincia, eiel_c_tramos_carreteras.codmunic AS municipio, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki AS pk_inicial, eiel_c_tramos_carreteras.pkf AS pk_final, carreteras.titular_via AS titular, eiel_c_tramos_carreteras.gestor AS gestion, eiel_c_tramos_carreteras.senaliza, eiel_c_tramos_carreteras.firme, eiel_c_tramos_carreteras.estado, eiel_c_tramos_carreteras.ancho, eiel_c_tramos_carreteras.longitud, eiel_c_tramos_carreteras.paso_nivel AS pasos_nive, eiel_c_tramos_carreteras.dimensiona, eiel_c_tramos_carreteras.muy_sinuoso AS muy_sinuos, eiel_c_tramos_carreteras.pte_excesiva AS pte_excesi, eiel_c_tramos_carreteras.fre_estrech AS fre_estrec
   FROM eiel_c_tramos_carreteras
   LEFT JOIN eiel_t_carreteras carreteras ON eiel_c_tramos_carreteras.codprov::text = carreteras.codprov::text AND eiel_c_tramos_carreteras.cod_carrt::text = carreteras.cod_carrt::text AND carreteras.revision_expirada = 9999999999::bigint::numeric
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_tramos_carreteras.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_tramos_carreteras.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_tramos_carreteras.revision_expirada = 9999999999::bigint::numeric
  ORDER BY eiel_c_tramos_carreteras.codprov, eiel_c_tramos_carreteras.codmunic, eiel_c_tramos_carreteras.cod_carrt, eiel_c_tramos_carreteras.pki;

ALTER TABLE v_tramo_carretera
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_carretera TO geopista;
GRANT SELECT ON TABLE v_tramo_carretera TO consultas;

CREATE OR REPLACE VIEW v_tramo_colector AS 
 SELECT eiel_c_saneam_tcl.clave, eiel_c_saneam_tcl.codprov AS provincia, eiel_c_saneam_tcl.codmunic AS municipio, eiel_c_saneam_tcl.tramo_cl AS orden_cole, eiel_t_saneam_tcl.material AS tipo_colec, eiel_t_saneam_tcl.sist_impulsion AS sist_trans, eiel_t_saneam_tcl.estado, eiel_t_saneam_tcl.titular, eiel_t_saneam_tcl.gestor AS gestion, sum(eiel_c_saneam_tcl.longitud) AS long_tramo
   FROM eiel_t_saneam_tcl
   LEFT JOIN eiel_c_saneam_tcl ON eiel_t_saneam_tcl.clave::text = eiel_c_saneam_tcl.clave::text AND eiel_t_saneam_tcl.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_saneam_tcl.codmunic::text = eiel_c_saneam_tcl.codmunic::text AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_c_saneam_tcl.revision_expirada = 9999999999::bigint::numeric
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_tcl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text
   FROM v_colector_nucleo
  ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl, eiel_c_saneam_tcl.clave, eiel_c_saneam_tcl.codprov, eiel_t_saneam_tcl.material, eiel_t_saneam_tcl.sist_impulsion, eiel_t_saneam_tcl.estado, eiel_t_saneam_tcl.titular, eiel_t_saneam_tcl.gestor
  ORDER BY eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl;

ALTER TABLE v_tramo_colector
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_colector TO geopista;
GRANT SELECT ON TABLE v_tramo_colector TO consultas;

CREATE OR REPLACE VIEW v_tramo_colector_m50 AS 
 SELECT eiel_c_saneam_tcl.clave, eiel_c_saneam_tcl.codprov AS provincia, eiel_c_saneam_tcl.codmunic AS municipio, eiel_c_saneam_tcl.tramo_cl AS orden_cole, eiel_t_saneam_tcl.material AS tipo_colec, eiel_t_saneam_tcl.sist_impulsion AS sist_trans, eiel_t_saneam_tcl.estado, eiel_t_saneam_tcl.titular, eiel_t_saneam_tcl.gestor AS gestion, sum(eiel_c_saneam_tcl.longitud) AS long_tramo
   FROM eiel_t_saneam_tcl
   LEFT JOIN eiel_c_saneam_tcl ON eiel_t_saneam_tcl.clave::text = eiel_c_saneam_tcl.clave::text AND eiel_t_saneam_tcl.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_saneam_tcl.codmunic::text = eiel_c_saneam_tcl.codmunic::text AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_c_saneam_tcl.revision_expirada = 9999999999::bigint::numeric
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_tcl.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text
   FROM v_colector_nucleo
  ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl, eiel_c_saneam_tcl.clave, eiel_c_saneam_tcl.codprov, eiel_t_saneam_tcl.material, eiel_t_saneam_tcl.sist_impulsion, eiel_t_saneam_tcl.estado, eiel_t_saneam_tcl.titular, eiel_t_saneam_tcl.gestor
  ORDER BY eiel_c_saneam_tcl.codmunic, eiel_c_saneam_tcl.tramo_cl;

ALTER TABLE v_tramo_colector_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_colector_m50 TO geopista;
GRANT SELECT ON TABLE v_tramo_colector_m50 TO consultas;

CREATE OR REPLACE VIEW v_tramo_conduccion AS 
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond, eiel_t_abast_tcn.material AS tipo_tcond, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor AS gestion, sum(eiel_c_abast_tcn.longitud) AS longitud
   FROM eiel_t_abast_tcn
   LEFT JOIN eiel_c_abast_tcn ON eiel_c_abast_tcn.clave::text = eiel_t_abast_tcn.clave::text AND eiel_c_abast_tcn.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_c_abast_tcn.codmunic::text = eiel_t_abast_tcn.codmunic::text AND eiel_c_abast_tcn.tramo_cn::text = eiel_t_abast_tcn.tramo_cn::text AND eiel_c_abast_tcn.revision_expirada = 9999999999::bigint::numeric
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tcn.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text
   FROM v_cond_agua_nucleo
  ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov, eiel_t_abast_tcn.codmunic, eiel_t_abast_tcn.tramo_cn, eiel_t_abast_tcn.material, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor;

ALTER TABLE v_tramo_conduccion
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_conduccion TO geopista;
GRANT SELECT ON TABLE v_tramo_conduccion TO consultas;

CREATE OR REPLACE VIEW v_tramo_conduccion_m50 AS 
 SELECT eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov AS provincia, eiel_t_abast_tcn.codmunic AS municipio, eiel_t_abast_tcn.tramo_cn AS orden_cond, eiel_t_abast_tcn.material AS tipo_tcond, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor AS gestion, sum(eiel_c_abast_tcn.longitud) AS longitud
   FROM eiel_t_abast_tcn
   LEFT JOIN eiel_c_abast_tcn ON eiel_c_abast_tcn.clave::text = eiel_t_abast_tcn.clave::text AND eiel_c_abast_tcn.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_c_abast_tcn.codmunic::text = eiel_t_abast_tcn.codmunic::text AND eiel_c_abast_tcn.tramo_cn::text = eiel_t_abast_tcn.tramo_cn::text AND eiel_c_abast_tcn.revision_expirada = 9999999999::bigint::numeric
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_abast_tcn.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_abast_tcn.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_abast_tcn.codmunic::text || eiel_t_abast_tcn.tramo_cn::text IN ( SELECT DISTINCT v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text
   FROM v_cond_agua_nucleo
  ORDER BY v_cond_agua_nucleo.cond_munic::text || v_cond_agua_nucleo.orden_cond::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_abast_tcn.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_abast_tcn.clave, eiel_t_abast_tcn.codprov, eiel_t_abast_tcn.codmunic, eiel_t_abast_tcn.tramo_cn, eiel_t_abast_tcn.material, eiel_t_abast_tcn.estado, eiel_t_abast_tcn.titular, eiel_t_abast_tcn.gestor;

ALTER TABLE v_tramo_conduccion_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_conduccion_m50 TO geopista;
GRANT SELECT ON TABLE v_tramo_conduccion_m50 TO consultas;

CREATE OR REPLACE VIEW v_tramo_emisario AS 
 SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_tem.material AS tipo_mat, eiel_t_saneam_tem.estado, sum(eiel_t_saneam_tem.long_terre) AS long_terre, sum(eiel_t_saneam_tem.long_marit) AS long_marit
   FROM eiel_t_saneam_tem
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
      FROM v_emisario_nucleo
     ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.material, eiel_t_saneam_tem.estado
  ORDER BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_tramo_emisario
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_emisario TO geopista;
GRANT SELECT ON TABLE v_tramo_emisario TO consultas;

CREATE OR REPLACE VIEW v_tramo_emisario_m50 AS 
 SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_tem.material AS tipo_mat, eiel_t_saneam_tem.estado, sum(eiel_t_saneam_tem.long_terre) AS long_terre, sum(eiel_t_saneam_tem.long_marit) AS long_marit
   FROM eiel_t_saneam_tem
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
      FROM v_emisario_nucleo
     ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.material, eiel_t_saneam_tem.estado
  ORDER BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_tramo_emisario_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_tramo_emisario_m50 TO geopista;
GRANT SELECT ON TABLE v_tramo_emisario_m50 TO consultas;



CREATE OR REPLACE VIEW v_vert_encuestado AS 
 SELECT eiel_t_vt.clave, eiel_t_vt.codprov AS provincia, eiel_t_vt.codmunic AS municipio, eiel_t_vt.orden_vt AS orden_ver, eiel_t_vt.tipo AS tipo_ver, eiel_t_vt.titular, eiel_t_vt.gestor AS gestion, eiel_t_vt.olores, eiel_t_vt.humos, eiel_t_vt.cont_anima, eiel_t_vt.r_inun, eiel_t_vt.filtracion, eiel_t_vt.impacto_v, eiel_t_vt.frec_averia AS frec_averi, eiel_t_vt.saturacion, eiel_t_vt.inestable, eiel_t_vt.otros, eiel_t_vt.capac_tot, eiel_t_vt.capac_tot_porc AS capac_porc, eiel_t_vt.capac_ampl, eiel_t_vt.capac_transf AS capac_tran, eiel_t_vt.estado, eiel_t_vt.vida_util, eiel_t_vt.categoria, eiel_t_vt.actividad
   FROM eiel_t_vt
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_vt.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_vt.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_vt.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_vert_encuestado
  OWNER TO geopista;
GRANT ALL ON TABLE v_vert_encuestado TO geopista;
GRANT SELECT ON TABLE v_vert_encuestado TO consultas;

CREATE OR REPLACE VIEW v_vert_encuestado_m50 AS 
 SELECT eiel_t_vt.clave, eiel_t_vt.codprov AS provincia, eiel_t_vt.codmunic AS municipio, eiel_t_vt.orden_vt AS orden_ver, eiel_t_vt.tipo AS tipo_ver, eiel_t_vt.titular, eiel_t_vt.gestor AS gestion, eiel_t_vt.olores, eiel_t_vt.humos, eiel_t_vt.cont_anima, eiel_t_vt.r_inun, eiel_t_vt.filtracion, eiel_t_vt.impacto_v, eiel_t_vt.frec_averia AS frec_averi, eiel_t_vt.saturacion, eiel_t_vt.inestable, eiel_t_vt.otros, eiel_t_vt.capac_tot, eiel_t_vt.capac_tot_porc AS capac_porc, eiel_t_vt.capac_ampl, eiel_t_vt.capac_transf AS capac_tran, eiel_t_vt.estado, eiel_t_vt.vida_util, eiel_t_vt.categoria, eiel_t_vt.actividad
   FROM eiel_t_vt
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_vt.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_vt.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_vt.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_vert_encuestado_m50
  OWNER TO geopista;
GRANT ALL ON TABLE v_vert_encuestado_m50 TO geopista;
GRANT SELECT ON TABLE v_vert_encuestado_m50 TO consultas;

CREATE OR REPLACE VIEW v_vertedero AS 
 SELECT eiel_t_vt.clave, eiel_t_vt.codprov AS provincia, eiel_t_vt.codmunic AS municipio, eiel_t_vt.orden_vt AS orden_ver
   FROM eiel_t_vt
  WHERE eiel_t_vt.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_vertedero
  OWNER TO geopista;
GRANT ALL ON TABLE v_vertedero TO geopista;
GRANT SELECT ON TABLE v_vertedero TO consultas;

CREATE OR REPLACE VIEW v_vertedero_nucleo AS 
 SELECT DISTINCT eiel_tr_vt_pobl.codprov AS provincia, eiel_tr_vt_pobl.codmunic AS municipio, eiel_tr_vt_pobl.codentidad AS entidad, eiel_tr_vt_pobl.codpoblamiento AS nucleo, eiel_tr_vt_pobl.clave_vt AS clave, eiel_tr_vt_pobl.codprov_vt AS ver_provin, eiel_tr_vt_pobl.codmunic_vt AS ver_munici, eiel_tr_vt_pobl.orden_vt AS ver_codigo
   FROM eiel_tr_vt_pobl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_tr_vt_pobl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_tr_vt_pobl.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_tr_vt_pobl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_tr_vt_pobl.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_tr_vt_pobl.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_tr_vt_pobl.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_tr_vt_pobl.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  ORDER BY eiel_tr_vt_pobl.codprov, eiel_tr_vt_pobl.codmunic, eiel_tr_vt_pobl.codentidad, eiel_tr_vt_pobl.codpoblamiento, eiel_tr_vt_pobl.clave_vt, eiel_tr_vt_pobl.codprov_vt, eiel_tr_vt_pobl.codmunic_vt, eiel_tr_vt_pobl.orden_vt;

ALTER TABLE v_vertedero_nucleo
  OWNER TO geopista;
GRANT ALL ON TABLE v_vertedero_nucleo TO geopista;
GRANT SELECT ON TABLE v_vertedero_nucleo TO consultas;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM dictionary WHERE traduccion = 'Infraestructuras viarias no referenciadas con ningún núcleo encuestado') THEN
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Infraestructuras viarias no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Infraestructuras viarias no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Infraestructuras viarias no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Infraestructuras viarias no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Infraestructuras viarias no referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ALUM_RECO_INFR_RE','check_infraestr_viaria_reference_nucl_encuestado','check_mpt_infraestr_viaria',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Alumbrados no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Alumbrados no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Alumbrados no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Alumbrados no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Alumbrados no referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ALUM_RECO_INFR_RE','check_alumbrado_reference_nucl_encuestado','check_mpt_alumbrado',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Recogidas de Basura no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Recogidas de Basura no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Recogidas de Basura no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Recogidas de Basura no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Recogidas de Basura no referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ALUM_RECO_INFR_RE','check_recogida_basura_reference_nucl_encuestado','check_mpt_recogida_basura',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Redes de Distribución no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Redes de Distribución no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Redes de Distribución no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Redes de Distribución no referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Redes de Distribución no referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ALUM_RECO_INFR_RE','check_red_distribucion_reference_nucl_encuestado','check_mpt_red_distribucion',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Captaciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Captaciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Captaciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Captaciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Captaciones no referenciadas con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CAPTACIONES','check_cap_agua_reference_provincia','check_mpt_cap_agua',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como captaciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como captaciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como captaciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como captaciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como captaciones o no están referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CAPTACIONES','check_cap_agua_nucleo_reference_nuc_encuestado','check_mpt_cap_agua_nucleo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Municipios encuestados no referenciados con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Municipios encuestados no referenciados con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Municipios encuestados no referenciados con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Municipios encuestados no referenciados con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Municipios encuestados no referenciados con ningún municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CARRETERAS','check_mun_enc_dis_reference_municipio','check_mpt_mun_enc_dis',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Carreteras no referenciadas con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Carreteras no referenciadas con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Carreteras no referenciadas con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Carreteras no referenciadas con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Carreteras no referenciadas con ninguna provincia');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CARRETERAS','check_carretera_reference_provincia','check_mpt_carretera',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tramos de Carretera no referenciados con ninguna carretera o municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tramos de Carretera no referenciados con ninguna carretera o municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tramos de Carretera no referenciados con ninguna carretera o municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tramos de Carretera no referenciados con ninguna carretera o municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tramos de Carretera no referenciados con ninguna carretera o municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CARRETERAS','check_tramo_carretera_reference_provincia','check_mpt_tramo_carretera',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros asistenciales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros asistenciales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros asistenciales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros asistenciales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros asistenciales no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_centro_asistencial_reference_poblamiento','check_mpt_centro_asistencial',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros sanitarios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros sanitarios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros sanitarios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros sanitarios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros sanitarios no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_centro_sanitario_reference_poblamiento','check_mpt_centro_sanitario',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Mataderos no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Mataderos no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Mataderos no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Mataderos no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Mataderos no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_matadero_reference_poblamiento','check_mpt_matadero',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Cementerios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Cementerios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Cementerios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Cementerios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Cementerios no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_cementerio_reference_poblamiento','check_mpt_cementerio',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tanatorios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tanatorios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tanatorios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tanatorios no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tanatorios no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CENT_ASIS_SANIT_MATADERO','check_tanatorio_reference_poblamiento','check_mpt_tanatorio',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Colectores no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Colectores no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Colectores no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Colectores no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Colectores no referenciados con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('COLECTOR','check_colector_reference_provincia','check_mpt_colector',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Colectores o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Colectores o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Colectores o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Colectores o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Colectores o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('COLECTOR','check_colector_nucleo_reference_nuc_encuestado','check_mpt_colector_nucleo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Conducciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Conducciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Conducciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Conducciones no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Conducciones no referenciadas con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CONDUCCIONES','check_conduccion_reference_provincia','check_mpt_conduccion',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Conducciones o no están referenciadas con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('CONDUCCIONES','check_conduccion_nucleo_reference_nuc_encuestado','check_mpt_conducccion_nucleo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Depósitos no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Depósitos no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Depósitos no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Depósitos no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Depósitos no referenciados con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('DEPOSITOS','check_depositos_reference_provincia','check_mpt_depositos',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Dépositos o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Dépositos o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Dépositos o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Dépositos o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Dépositos o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('DEPOSITOS','check_depositos_nucleo_reference_nuc_encuestado','check_mpt_depositos_nucleo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Depuradoras no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Depuradoras no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Depuradoras no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Depuradoras no referenciadas con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Depuradoras no referenciadas con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('DEPURADORAS','check_depuradora_reference_provincia','check_mpt_depuradora',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Depuradoras o no están referenciada con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('DEPURADORAS','check_depuradora_nucleo_reference_nuc_encuestado','check_mpt_depuradora_nucleo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Emisarios no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Emisarios no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Emisarios no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Emisarios no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Emisarios no referenciados con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('EMISARIOS','check_emisario_reference_provincia','check_mpt_emisario',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Emisario o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Emisario o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Emisario o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Emisario o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Emisario o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('EMISARIOS','check_emisario_nucleo_reference_nuc_encuestado','check_mpt_emisario_nucleo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Protecciones civíles no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Protecciones civíles no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Protecciones civíles no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Protecciones civíles no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Protecciones civíles no referenciadas con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_proteccion_reference_poblamiento','check_mpt_proteccion',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros de enseñanza no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros de enseñanza no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros de enseñanza no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros de enseñanza no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros de enseñanza no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_centro_ensenanza_reference_poblamiento','check_mpt_centro_ensenanza',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Niveles de enseñanza no referenciados con ningún centro de enseñanza');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Niveles de enseñanza no referenciados con ningún centro de enseñanza');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Niveles de enseñanza no referenciados con ningún centro de enseñanza');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Niveles de enseñanza no referenciados con ningún centro de enseñanza');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Niveles de enseñanza no referenciados con ningún centro de enseñanza');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_nivel_ensenanza_reference_poblamiento','check_mpt_nivel_ensenanza',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Edificios públicos sin uso no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Edificios públicos sin uso no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Edificios públicos sin uso no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Edificios públicos sin uso no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Edificios públicos sin uso no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_edific_reference_poblamiento','check_mpt_edific',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Casas Consistoriales no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Casas Consistoriales no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Casas Consistoriales no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Casas Consistoriales no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Casas Consistoriales no referenciadas con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_casa_consistorial_reference_poblamiento','check_mpt_casa_consistorial',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Casas con usos no referenciados con ninguna casa consistorial');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Casas con usos no referenciados con ninguna casa consistorial');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Casas con usos no referenciados con ninguna casa consistorial');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Casas con usos no referenciados con ninguna casa consistorial');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Casas con usos no referenciados con ninguna casa consistorial');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_casa_uso_reference_poblamiento','check_mpt_casa_uso',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros Culturales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros Culturales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros Culturales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros Culturales no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros Culturales no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('INST_DEPORT_PARQUES_LONJAS','check_cent_cultural_reference_poblamiento','check_mpt_cent_cultural',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Parques no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Parques no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Parques no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Parques no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Parques no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_parque_reference_poblamiento','check_mpt_parque',currval('seq_dictionary'));


		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Centros culturales con usos no referenciados con ningún centro cultural');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Centros culturales con usos no referenciados con ningún centro cultural');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Centros culturales con usos no referenciados con ningún centro cultural');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Centros culturales con usos no referenciados con ningún centro cultural');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Centros culturales con usos no referenciados con ningún centro cultural');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('INST_DEPORT_PARQUES_LONJAS','check_cent_cultural_usos_reference_poblamiento','check_mpt_cent_cultural_usos',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Instalaciones deportivas no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Instalaciones deportivas no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Instalaciones deportivas no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Instalaciones deportivas no referenciadas con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Instalaciones deportivas no referenciadas con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('INST_DEPORT_PARQUES_LONJAS','check_instal_deportiva_reference_poblamiento','check_mpt_instal_deportiva',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Deportes no referenciados con ninguna instalación deportiva');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Deportes no referenciados con ninguna instalación deportiva');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Deportes no referenciados con ninguna instalación deportiva');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Deportes no referenciados con ninguna instalación deportiva');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Deportes no referenciados con ninguna instalación deportiva');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('INST_DEPORT_PARQUES_LONJAS','check_inst_depor_deporte_reference_poblamiento','check_mpt_inst_depor_deporte',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Lonjas Mercados Ferias no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Lonjas Mercados Ferias no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Lonjas Mercados Ferias no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Lonjas Mercados Ferias no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Lonjas Mercados Ferias no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ENSEÑAZA_PROT_EDIFICIOS','check_lonja_merc_feria_reference_poblamiento','check_mpt_lonja_merc_feria',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Planeamientos Urbanístico no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Planeamientos Urbanístico no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Planeamientos Urbanístico no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Planeamientos Urbanístico no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Planeamientos Urbanístico no referenciados con ningún municipio encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('PLAN_URBANISTICO_OTROS','check_plan_urbanistico_reference_mun_enc_dis','check_mpt_plan_urbanistico',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Otros servicios municipales no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Otros servicios municipales no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Otros servicios municipales no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Otros servicios municipales no referenciados con ningún municipio encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Otros servicios municipales no referenciados con ningún municipio encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('PLAN_URBANISTICO_OTROS','check_ot_serv_municipal_reference_mun_enc_dis','check_mpt_ot_serv_municipal',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Ramales de Saneamiento no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Ramales de Saneamiento no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Ramales de Saneamiento no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Ramales de Saneamiento no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Ramales de Saneamiento no referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('SANEAMIENTO','check_ramal_sanemaiento_reference_nucl_encuestado','check_mpt_ramal_sanemaiento',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Saneamientos autónomos no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Saneamientos autónomos no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Saneamientos autónomos no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Saneamientos autónomos no referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Saneamientos autónomos no referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('SANEAMIENTO','check_sanea_autonomo_reference_nucl_encuestado','check_mpt_sanea_autonomo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Tratamientos de Potabilización no referenciados con ninguna provincia ni municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('TRAT_POTABILIZACION','check_trat_potabilizacion_reference_provincia','check_mpt_trat_potabilizacion',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Tratamientos de Potabilización o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('TRAT_POTABILIZACION','check_trat_pota_nucleo_reference_nuc_encuestado','check_mpt_trat_pota_nucleo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Vertederos no referenciados con ninguna Provincia ni Municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Vertederos no referenciados con ninguna Provincia ni Municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Vertederos no referenciados con ninguna Provincia ni Municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Vertederos no referenciados con ninguna Provincia ni Municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Vertederos no referenciados con ninguna Provincia ni Municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('VERTEDERO','check_vertedero_reference_provincia','check_mpt_vertedero',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','No existen como Vertedero o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]No existen como Vertedero o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]No existen como Vertedero o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]No existen como Vertedero o no están referenciados con ningún núcleo encuestado');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]No existen como Vertedero o no están referenciados con ningún núcleo encuestado');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('VERTEDERO','check_vertedero_nucleo_reference_nuc_encuestado','check_mpt_vertedero_nucleo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Municipios no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Municipios no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Municipios no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Municipios no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Municipios no referenciados con ninguna provincia');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_municipio_reference_provincia','check_mpt_municipio',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Cabildos no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Cabildos no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Cabildos no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Cabildos no referenciados con ninguna provincia');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Cabildos no referenciados con ninguna provincia');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_cabildo_reference_provincia','check_mpt_cabildo_consejo',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Entidades Singulares no referenciadas con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Entidades Singulares no referenciadas con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Entidades Singulares no referenciadas con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Entidades Singulares no referenciadas con ningún municipio');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Entidades Singulares no referenciadas con ningún municipio');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_entidad_sing_reference_municipio','check_mpt_entidad_sing',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Poblamientos no referenciados con ningún entidad singular');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Poblamientos no referenciados con ningún entidad singular');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Poblamientos no referenciados con ningún entidad singular');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Poblamientos no referenciados con ningún entidad singular');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Poblamientos no referenciados con ningún entidad singular');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_poblamiento_reference_entidad_sing','check_mpt_poblamiento',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos de población no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos de población no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos de población no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos de población no referenciados con ningún poblamiento');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos de población no referenciados con ningún poblamiento');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_nucleo_poblacion_reference_poblamiento','check_mpt_nucleo_poblacion',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos abandonados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos abandonados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos abandonados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos abandonados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos abandonados no referenciados con ningún núcleo de población');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_nucleo_abandonado_reference_poblamiento','check_mpt_nucleo_abandonado',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos encuestados no referenciados con ningún núcleo de población');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_nucleo_encuestado_reference_nucleo_poblacion','check_mpt_nucleo_encuestado',currval('seq_dictionary'));

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Núcleos encuestados no referenciados con ningún núcleo de población');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Núcleos encuestados no referenciados con ningún núcleo de población');

		insert into query_validate_mpt (tipo,nombre,query,id_descripcion) VALUES ('ESTRUCTURA TERRITORIAL','check_nucleo_encuestado_2_reference_nucleo_poblacion','check_mpt_nucleo_encuestado_2',currval('seq_dictionary'));

		INSERT INTO "public".eiel_validacionesmpt(id ,nombre,tabla ) VALUES (78,'Integridad Referencial','');
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO4";


DROP TABLE IF EXISTS eiel_inventario;
CREATE TABLE eiel_inventario
(
   union_clave_eiel character varying(100) NOT NULL, 
   vista_eiel character varying(100) NOT NULL, 
   id_inventario integer NOT NULL,
   epig_inventario numeric(1,0),
   id_municipio numeric(5,0),
   titularidad_municipal character varying(2) NOT NULL
) 
WITH (
  OIDS = TRUE
);
ALTER TABLE eiel_inventario OWNER TO geopista;

DROP INDEX IF EXISTS i_eiel_inventario;
CREATE INDEX i_eiel_inventario
  ON eiel_inventario
  USING btree
  (id_inventario );

DROP INDEX IF EXISTS i_eiel_inventario2;
CREATE INDEX i_eiel_inventario2
  ON eiel_inventario
  USING btree
  (union_clave_eiel );

DROP INDEX IF EXISTS i_eiel_inventario3;  
CREATE INDEX i_eiel_inventario3
  ON eiel_inventario
  USING btree
  (union_clave_eiel , vista_eiel , id_municipio );

CREATE OR REPLACE VIEW v_integ_eiel AS 
 (SELECT 'v_integ_depositos'::text as vista, 'eiel_c_abast_de'::text as tabla_c,* from v_integ_depositos) UNION
 (SELECT 'v_integ_centroasistencial'::text as vista, 'eiel_c_as'::text as tabla_c, *  from v_integ_centroasistencial) UNION
 (SELECT 'v_integ_casaconsistorial'::text as vista, 'eiel_c_cc'::text as tabla_c, *  from v_integ_casaconsistorial) UNION
 (SELECT 'v_integ_cementerio'::text as vista, 'eiel_c_ce'::text as tabla_c, *  from v_integ_cementerio) UNION
 (SELECT 'v_integ_centrocultural'::text as vista, 'eiel_c_cu'::text as tabla_c, *  from v_integ_centrocultural) UNION
 (SELECT 'v_integ_centroensenianza'::text as vista, 'eiel_c_en'::text as tabla_c, *  from v_integ_centroensenianza) UNION
 (SELECT 'v_integ_instalaciondeportiva'::text as vista, 'eiel_c_id'::text as tabla_c, *  from v_integ_instalaciondeportiva) UNION
 (SELECT 'v_integ_incendiosproteccion'::text as vista, 'eiel_c_ip'::text as tabla_c, *  from v_integ_incendiosproteccion) UNION
 (SELECT 'v_integ_lonjasmercados'::text as vista, 'eiel_c_lm'::text as tabla_c, *  from v_integ_lonjasmercados) UNION
 (SELECT 'v_integ_mataderos'::text as vista, 'eiel_c_ma'::text as tabla_c, *  from v_integ_mataderos) UNION
 (SELECT 'v_integ_parquesjardines'::text as vista, 'eiel_c_pj'::text as tabla_c, *  from v_integ_parquesjardines) UNION
 (SELECT 'v_integ_centrosanitario'::text as vista, 'eiel_c_sa'::text as tabla_c, *  from v_integ_centrosanitario) UNION
 (SELECT 'v_integ_edificiosinuso'::text as vista, 'eiel_c_su'::text as tabla_c, *  from v_integ_edificiosinuso) UNION
 (SELECT 'v_integ_tanatorio'::text as vista, 'eiel_c_ta'::text as tabla_c, *  from v_integ_tanatorio) UNION
 (SELECT 'v_integ_carreteras'::text as vista, 'eiel_c_tramos_carreteras'::text as tabla_c, *  from v_integ_carreteras) UNION
 (SELECT 'v_integ_depuradoras'::text as vista, 'eiel_c_saneam_ed'::text as tabla_c, *  from v_integ_depuradoras);

  ALTER TABLE v_integ_eiel OWNER TO geopista;

  
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM domains WHERE NAME = 'eiel_Titularidad Municipal') THEN
		PERFORM setval('public.seq_domains', (select max(ID)::bigint from domains), true);
		PERFORM setval('public.seq_dictionary', (select max(ID_VOCABLO)::bigint from DICTIONARY), true);
		PERFORM setval('public.seq_domainnodes', (select max(id)::bigint from domainnodes), true);
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'eiel_Titularidad Municipal',(select idacl FROM acl WHERE name='EIEL'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Titularidad Municipal');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Titularidad Municipal');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Titularidad Municipal');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Titularidad Municipal');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Titularidad Municipal');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','No');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'NO',currval('seq_dictionary'),7,null,currval('seq_domainnodes')-1,null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Si');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'SI',currval('seq_dictionary'),7,null,currval('seq_domainnodes')-2,null);			
	 
	  
	 
	 insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Tipo de bien patrimonial EIEL',(select idacl FROM acl WHERE name='Patrimonio'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Tipo de bien patrimonial EIEL');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Tipo de bien patrimonial EIEL');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Tipo de bien patrimonial EIEL');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Tipo de bien patrimonial EIEL');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Tipo de bien patrimonial EIEL');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Inmuebles Rusticos');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Inmuebles Rusticos');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Inmuebles Rusticos');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Inmuebles Rusticos');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Inmuebles Rusticos');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'3',currval('seq_dictionary'),7,null,currval('seq_domainnodes')-1,null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Inmuebles Urbanos');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Inmuebles Urbanos');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Inmuebles Urbanos');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Inmuebles Urbanos');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Inmuebles Urbanos');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'2',currval('seq_dictionary'),7,null,currval('seq_domainnodes')-2,null);	
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Vías Públicas Rústicas');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Vías Públicas Rústicas');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Vías Públicas Rústicas');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Vías Públicas Rústicas');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Vías Públicas Rústicas');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'5',currval('seq_dictionary'),7,null,currval('seq_domainnodes')-3,null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Vías Públicas Urbanas');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Vías Públicas Urbanas');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Vías Públicas Urbanas');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Vías Públicas Urbanas');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Vías Públicas Urbanas');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'4',currval('seq_dictionary'),7,null,currval('seq_domainnodes')-4,null);
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO1"; 
 
 --Asignacion de permisos al usuario consultas para la generacion de cuadros MPT
grant select on v_alumbrado to consultas ;
grant select on v_cabildo_consejo to consultas ;
grant select on v_cap_agua_nucleo to consultas ;
grant select on v_captacion_agua to consultas ;
grant select on v_captacion_enc to consultas ;
grant select on v_captacion_enc_m50 to consultas ;
grant select on v_carretera to consultas ;
grant select on v_casa_con_uso to consultas ;
grant select on v_casa_consistorial to consultas ;
grant select on v_cementerio to consultas ;
grant select on v_cent_cultural to consultas ;
grant select on v_cent_cultural_usos to consultas ;
grant select on v_centro_asistencial to consultas ;
grant select on v_centro_ensenanza to consultas ;
grant select on v_centro_sanitario to consultas ;
grant select on v_colector to consultas ;
grant select on v_colector_enc to consultas ;
grant select on v_colector_enc_m50 to consultas ;
grant select on v_colector_nucleo to consultas ;
grant select on v_cond_agua_nucleo to consultas ;
grant select on v_conduccion to consultas ;
grant select on v_conduccion_enc to consultas ;
grant select on v_conduccion_enc_m50 to consultas ;
grant select on v_dep_agua_nucleo to consultas ;
grant select on v_deposito to consultas ;
grant select on v_deposito_agua_nucleo to consultas ;
grant select on v_deposito_enc to consultas ;
grant select on v_deposito_enc_m50 to consultas ;
grant select on v_depuradora to consultas ;
grant select on v_depuradora_enc to consultas ;
grant select on v_depuradora_enc_2 to consultas ;
grant select on v_depuradora_enc_2_m50 to consultas ;
grant select on v_depuradora_enc_m50 to consultas ;
grant select on v_edific_pub_sin_uso to consultas ;
grant select on v_emisario to consultas ;
grant select on v_emisario_enc to consultas ;
grant select on v_emisario_enc_m50 to consultas ;
grant select on v_emisario_nucleo to consultas ;
grant select on v_entidad_singular to consultas ;
grant select on v_infraestr_viaria to consultas ;
grant select on v_inst_depor_deporte to consultas ;
grant select on v_instal_deportiva to consultas ;
grant select on v_lonja_merc_feria to consultas ;
grant select on v_matadero to consultas ;
grant select on v_mun_enc_dis to consultas ;
grant select on v_municipio to consultas ;
grant select on v_nivel_ensenanza to consultas ;
grant select on v_nuc_abandonado to consultas ;
grant select on v_nucl_encuestado_1 to consultas ;
grant select on v_nucl_encuestado_2 to consultas ;
grant select on v_nucl_encuestado_3 to consultas ;
grant select on v_nucl_encuestado_4 to consultas ;
grant select on v_nucl_encuestado_5 to consultas ;
grant select on v_nucl_encuestado_6 to consultas ;
grant select on v_nucl_encuestado_7 to consultas ;
grant select on v_nucleo_poblacion to consultas ;
grant select on v_ot_serv_municipal to consultas ;
grant select on v_padron to consultas ;
grant select on v_parque to consultas ;
grant select on v_plan_urbanistico to consultas ;
grant select on v_poblamiento to consultas ;
grant select on v_potabilizacion_enc to consultas ;
grant select on v_potabilizacion_enc_m50 to consultas ;
grant select on v_proteccion_civil to consultas ;
grant select on v_provincia to consultas ;
grant select on v_ramal_saneamiento to consultas ;
grant select on v_recogida_basura to consultas ;
grant select on v_red_distribucion to consultas ;
grant select on v_sanea_autonomo to consultas ;
grant select on v_tanatorio to consultas ;
grant select on v_tra_potabilizacion to consultas ;
grant select on v_tramo_carretera to consultas ;
grant select on v_tramo_colector to consultas ;
grant select on v_tramo_colector_m50 to consultas ;
grant select on v_tramo_conduccion to consultas ;
grant select on v_tramo_conduccion_m50 to consultas ;
grant select on v_tramo_emisario to consultas ;
grant select on v_tramo_emisario_m50 to consultas ;
grant select on v_trat_pota_nucleo to consultas ;
grant select on v_vert_encuestado to consultas ;
grant select on v_vert_encuestado_m50 to consultas ;
grant select on v_vertedero to consultas ;
grant select on v_vertedero_nucleo to consultas ; 			  				
   
   

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


select f_add_col('public.eiel_c_abast_ca','red_asociada', 'ALTER TABLE eiel_c_abast_ca ADD COLUMN red_asociada character varying;');
select f_add_col('public.eiel_c_abast_de','red_asociada', 'ALTER TABLE ALTER TABLE eiel_c_abast_de ADD COLUMN red_asociada character varying;');
select f_add_col('public.eiel_c_abast_rd','red_asociada', 'ALTER TABLE eiel_c_abast_rd ADD COLUMN red_asociada character varying;');
   

   
   
   
-- Fin ForenkeysMPT.sql + VistasForMPT.sql + integracionEIEL_Inventario.sql




