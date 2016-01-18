SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;

SET client_min_messages TO NOTICE;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'session_app') THEN	
		ALTER TABLE "public"."session_app" ALTER COLUMN "id" TYPE varchar(40);
	END IF;
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'iusercnt') THEN	
		ALTER TABLE "public"."iusercnt" ALTER COLUMN "id" TYPE varchar(40);
	END IF;
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO0";

-- Inicio actualizar secuencias

SELECT setval('public.seq_domainnodes', (select max(id)::bigint from domainnodes), true);
SELECT setval('public.seq_domains', (select max(id)::bigint from domains), true);

-- Fin actualizar secuencias

-- Inicio modeloSeguridad.sql

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

create or replace function create_constraint_if_not_exists (
    c_name text, constraint_sql text
) 
returns void AS
$$
begin
    -- Look for our constraint
    if not exists (SELECT conname FROM pg_constraint WHERE conname = c_name) then
        execute constraint_sql;
    end if;
end;
$$ language 'plpgsql';




select f_add_col('public.entidad_supramunicipal','aviso', 'ALTER TABLE entidad_supramunicipal ADD COLUMN aviso numeric(3,0);');

select f_add_col('public.entidad_supramunicipal','periodicidad', 'ALTER TABLE entidad_supramunicipal ADD COLUMN periodicidad numeric(3,0);');
select f_add_col('public.entidad_supramunicipal','intentos', 'ALTER TABLE entidad_supramunicipal ADD COLUMN intentos numeric(2,0);');

select f_add_col('public.iuseruserhdr','fecha_proxima_modificacion', 'ALTER TABLE iuseruserhdr ADD COLUMN fecha_proxima_modificacion date;');

select f_add_col('public.iuseruserhdr','bloqueado', 'ALTER TABLE iuseruserhdr ADD COLUMN bloqueado boolean;');


update entidad_supramunicipal set aviso = 15, periodicidad = 365, intentos = 5;
update iuseruserhdr set bloqueado = false, fecha_proxima_modificacion = current_date-1;


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'seguridadContrasenia') THEN
		insert into query_catalog (id,query,acl,idperm) values ('seguridadContrasenia','select bloqueado, aviso, fecha_proxima_modificacion, intentos from iuseruserhdr,entidad_supramunicipal where name = ? and iuseruserhdr.id_entidad = entidad_supramunicipal.id_entidad','1','9205');
	END IF;
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'bloqueaUsuario') THEN
		insert into query_catalog (id,query,acl,idperm) values ('bloqueaUsuario','update iuseruserhdr set bloqueado = true where name = ?','1','9205');
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";

update query_catalog set query='SELECT id, name, nombrecompleto, password, remarks,mail, deptid, nif, id_entidad, bloqueado FROM iuseruserhdr WHERE borrado!=1' where id = 'getListaUsuarios';



ALTER TABLE entidad_supramunicipal ALTER periodicidad TYPE integer;

select f_add_col('public.iuseruserhdr','intentos_reiterados', 'ALTER TABLE iuseruserhdr ADD COLUMN intentos_reiterados numeric(2,0);');

update query_catalog set query='select bloqueado, aviso, fecha_proxima_modificacion, intentos, intentos_reiterados from iuseruserhdr,entidad_supramunicipal where name = ? and iuseruserhdr.id_entidad = entidad_supramunicipal.id_entidad' where id = 'seguridadContrasenia';

-- Fin modeloSeguridad.sql

-- Inicio modeloModificaciones 2011-04-28.sql

--Se crea la secuencia para el inventario de features
DROP SEQUENCE IF EXISTS seq_inventario_feature;
CREATE SEQUENCE seq_inventario_feature
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_inventario_feature OWNER TO postgres;

--Se añaden los campos de revisiones al inventario_feature
select f_add_col('public.INVENTARIO_FEATURE','revision_actual', 'alter table INVENTARIO_FEATURE add revision_actual numeric(10,0) default 0.0;');
--alter table INVENTARIO_FEATURE drop revision_Actual;

select f_add_col('public.INVENTARIO_FEATURE','revision_expirada', 'alter table INVENTARIO_FEATURE add revision_expirada numeric(10,0) default 9999999999;');
--alter table INVENTARIO_FEATURE drop revision_expirada;

SELECT setval('seq_inventario_feature', (select cast(max(REVISION_ACTUAL) as bigint) from INVENTARIO_FEATURE)+1); 

select f_add_col('public.inmuebles','bic', 'alter table inmuebles add bic boolean;');

--alter table inmuebles drop bic;

select f_add_col('public.inmuebles','catalogado', 'alter table inmuebles add catalogado boolean;');
--alter table inmuebles drop catalogado;

select f_add_col('public.inmuebles_urbanos','anchosuperf', 'alter table inmuebles_urbanos add anchosuperf numeric(10,2) default 0.0;');

--alter table inmuebles_urbanos drop anchoSuperf;
select f_add_col('public.inmuebles_rusticos','anchosuperf', 'alter table inmuebles_rusticos add anchosuperf numeric(10,2) default 0.0;');

--alter table inmuebles_rusticos drop anchoSuperf;

select f_add_col('public.inmuebles_urbanos','longsuperf', 'alter table inmuebles_urbanos add longsuperf numeric(10,2) default 0.0;');

--alter table inmuebles_urbanos drop longSuperf;
select f_add_col('public.inmuebles_rusticos','longsuperf', 'alter table inmuebles_rusticos add longsuperf numeric(10,2) default 0.0;');
--alter table inmuebles_rusticos drop longSuperf;

select f_add_col('public.inmuebles_urbanos','metrpavsuperf', 'alter table inmuebles_urbanos add metrpavsuperf numeric(10,2) default 0.0');
--alter table inmuebles_urbanos drop metrPavSuperf;
select f_add_col('public.inmuebles_rusticos','metrpavsuperf', 'alter table inmuebles_rusticos add metrpavsuperf numeric(10,2) default 0.0;');

--alter table inmuebles_rusticos drop metrPavSuperf;

select f_add_col('public.inmuebles_urbanos','metrnopavsuperf', 'alter table inmuebles_urbanos add metrnopavsuperf numeric(10,2) default 0.0;');

--alter table inmuebles_urbanos drop metrNoPavSuperf;
select f_add_col('public.inmuebles_rusticos','metrnopavsuperf', 'alter table inmuebles_rusticos add metrnopavsuperf numeric(10,2) default 0.0;');

--alter table inmuebles_rusticos drop metrNoPavSuperf;


--Se modifica la clave primaria de la tabla INVENTARIO_PARCELA
ALTER TABLE INVENTARIO_FEATURE DROP CONSTRAINT inventario_feature_pkey ;
ALTER TABLE INVENTARIO_FEATURE ADD CONSTRAINT inventario_feature_pkey PRIMARY KEY (id_bien, id_layer, id_feature, revision_actual);

--select * from inmuebles;
--select * from inmuebles_urbanos;
--select * from inmuebles_rusticos;
--select * from INVENTARIO_FEATURE;

--alter table inmuebles drop bic;
--alter table inmuebles drop catalogado;
--alter table inmuebles drop anchoSuperf;
--alter table inmuebles drop longSuperf;
--alter table inmuebles drop metrPavSuperf;
--alter table inmuebles drop metrNoPavSuperf;
--alter table INVENTARIO_FEATURE drop revision_Actual;
--alter table INVENTARIO_FEATURE drop revision_expirada;


--Insertamos los nuevos tipos de Amortizacion
--

--delete from domains where name like('@nomAmor');
delete from domainnodes where id_description in (select id_vocablo from dictionary where traduccion in ('por años', 'por porcentaje'));
delete from dictionary where traduccion in ('por años', '[va]por años', '[gl]por años', '[eu]por años', '[cat]por años', 'por porcentaje', '[va]por porcentaje', '[gl]por porcentaje', '[eu]por porcentaje', '[cat]por porcentaje');


--(select * from domainnodes dn join dictionary dy on dn.id_description= dy.id_vocablo where dy.traduccion='Tipo de Amortizacion')

-- Insertamos las descripciones en la tabla dictionary
insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'es_ES' , 'por años');
insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'va_ES' , '[va]por años');
insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'gl_ES' , '[gl]por años');
insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'eu_ES' , '[eu]por años');
insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'ca_ES' , '[cat]por años');

insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'es_ES' , 'por porcentaje');
insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'va_ES' , '[va]por porcentaje');
insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'gl_ES' , '[gl]por porcentaje');
insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'eu_ES' , '[eu]por porcentaje');
insert into dictionary (id_vocablo, locale, traduccion)values(nextval('seq_dictionary') ,'ca_ES' , '[cat]por porcentaje');


-- Insertamos los nodos en la tabla domainnodes
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain)
values(nextval('seq_domainnodes'),(select id from domains where name='Tipo de Amortizacion'),0,(select id_vocablo from dictionary where traduccion in ('por años')),7,(select id from domainnodes dn join dictionary dy on dn.id_description= dy.id_vocablo where dy.traduccion='Tipo de Amortizacion' and id_municipio is null));

insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain)
values(nextval('seq_domainnodes'),(select id from domains where name='Tipo de Amortizacion'),0,(select id_vocablo from dictionary where traduccion in ('por porcentaje')),7,(select id from domainnodes dn join dictionary dy on dn.id_description= dy.id_vocablo where dy.traduccion='Tipo de Amortizacion' and id_municipio is null ));



--select id_vocablo from dictionary where traduccion in ('Por Años');
--select * from dictionary where traduccion like'%Por Valor' or traduccion like'%Por Años' order by id_vocablo desc ;
--select * from domainnodes order by id desc;

--select * from domainnodes dn left join dictionary dc on dn.id_description=dc.id_vocablo where traduccion like'%Por Valor' or traduccion like'%Por Años'order by dn.id  desc;


--Bienes PreAlta

--Borramos SICALWIN
--Borramos la union por usuarios
delete from r_usr_perm where  (idacl in(select idacl from acl where name like 'SICALWIN'));
--Borramos la union por grupos
delete from r_group_perm where (groupid = (select ID from iusergrouphdr where name like 'SICALWIN')) and (idacl in(select idacl from acl where name like 'SICALWIN'));

delete from iusergroupuser WHERE (groupid=(select id from iusergrouphdr  where name like 'SICALWIN')) AND  (userid=(select id from iuseruserhdr  where name like 'SICALWIN'));
delete from iuseruserhdr  where name like 'SICALWIN';
delete from iusergrouphdr where name like'SICALWIN';
--Borramos SICALWIN

delete from r_acl_perm where (idacl in (select idacl from acl where name like 'SICALWIN'));
delete from usrgrouperm where def in ('LocalGis.Login', 'LocalGis.SICALWIN.BienesPreAlta.Insercion')and idperm > 10000;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	delete from app_acl where acl=(select idacl from acl where name like 'SICALWIN');
	exception when others then
		RAISE NOTICE 'No se puede borrar el acl de la tabla app_acl';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO1.5";

delete from acl where name like 'SICALWIN';



delete from R_USR_PERM where (userid=((select id from iuseruserhdr where name like 'SYSSUPERUSER' ))) and idperm in (select idperm from USRGROUPERM where def in ('Geopista.Inventario.BienesPreAlta.Conversion', 'Geopista.Inventario.BienesPreAlta.Insercion', 'Geopista.Inventario.Login'));
delete from R_USR_PERM where  idperm in (select idperm from USRGROUPERM where def in ('Geopista.Inventario.BienesPreAlta.Conversion', 'Geopista.Inventario.BienesPreAlta.Insercion', 'Geopista.Inventario.Login'));
delete from R_GROUP_PERM where  idperm in (select idperm from USRGROUPERM where def in ('Geopista.Inventario.BienesPreAlta.Conversion', 'Geopista.Inventario.BienesPreAlta.Insercion', 'Geopista.Inventario.Login'));
delete from R_ACL_PERM where idperm in (select idperm from USRGROUPERM where def in ('Geopista.Inventario.BienesPreAlta.Conversion', 'Geopista.Inventario.BienesPreAlta.Insercion'));
delete from USRGROUPERM where def in ('Geopista.Inventario.BienesPreAlta.Conversion', 'Geopista.Inventario.BienesPreAlta.Insercion');

--No deberian existir permisos para los usuarios y esquemas marcados
--select u.id as USERID, u.name AS USER, a.idacl, a.name as ACL , ugp.idperm as IDPERMISO, ugp.def as PERMISO from iuseruserhdr u join r_usr_perm rup on (u.id=rup.userid) join r_acl_perm rap on (rup.idacl=rap.idacl) and (rup.idperm=rap.idperm) join usrgrouperm ugp on (rap.idperm=ugp.idperm)join acl a on (rap.idacl=a.idacl) where (u.name in ('SYSSUPERUSER', 'SICALWIN'))  and (def in ('Geopista.Inventario.BienesPreAlta.Conversion','Geopista.Inventario.BienesPreAlta.Insercion','Geopista.Inventario.Login','LocalGis.Login', 'LocalGis.SICALWIN.BienesPreAlta.Insercion'))order by rap.idperm desc;

-------------------------
----SICALWIN
-------------------------
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM iuseruserhdr WHERE name = 'SICALWIN') THEN
		--Insertamos el usuario SICALWIN
		insert into iuseruserhdr (id, name, nombrecompleto, password, flags, stat, numbadcnts, remarks, crtrid, borrado, id_entidad, upddate, crtndate, fecha_proxima_modificacion, bloqueado) values ((select max(id)+1 from iuseruserhdr), 'SICALWIN', 'SICALWIN', 'PggP3/BOfCFsFDgX6iL9gA==', 0, 0, 0,'Usuario SICALWIN', 100, 0, 0,'2011-04-14 00:00:00','2011-04-14 00:00:00','2011-04-14', false);
		--Insertamos un Grupo de SICALWIN
		insert into iusergrouphdr(id, name, mgrid, type, remarks, crtrid, crtndate) values ((select max(id)+1 from iusergrouphdr),'SICALWIN',100,0,'Rol para Módulo SICALWIN',100, '2011-04-14 00:00:00');
		--Relacionamos el usuario y el grupo
		insert into iusergroupuser (groupid, userid) values ((select id from iusergrouphdr  where name like 'SICALWIN'), (select id from iuseruserhdr  where name like 'SICALWIN'));
		--Comprobar que todo lo de los usuarios es correcto
		--select u.*, g.* from iuseruserhdr u join iusergroupuser j on u.id=j.userid join iusergrouphdr g on j.groupid=g.id where u.name like 'SICALWIN'

		--Insertamos la ACL SICALWIN
		insert into acl (idacl, name) values ((nextval('seq_acl')), 'SICALWIN');
		--Insertamos los permisos
		insert into usrgrouperm (idperm, def) values((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGis.Login');
		insert into usrgrouperm (idperm, def) values ((select cast(max(idperm)+1 as int) from usrgrouperm),'LocalGis.SICALWIN.BienesPreAlta.Insercion');
		--Relacionamos el permiso y la ACL
		insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like 'SICALWIN'));
		insert into r_acl_perm (idperm, idacl) values ((select cast(max(idperm)-1 as int) from usrgrouperm),(select idacl from acl where name like 'SICALWIN'));
		--Comprobar que todo lo de los permisos es correcto
		--select a.*, g.* from acl a join r_acl_perm r on a.idacl=r.idacl join usrgrouperm g on r.idperm=g.idperm where a.name in ('SICALWIN', 'SIGEM');

		--Insertamos la relacion entre los permisos creados y los usuarios
		insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like 'SICALWIN'), (select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like ('SICALWIN')), 1);
		insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like 'SICALWIN'), (select cast(max(idperm)-1 as int) from usrgrouperm),(select idacl from acl where name like ('SICALWIN')), 1);
		--Insertamos la relacion entre los permisos creados y los grupos
		insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SICALWIN'), (select cast(max(idperm) as int) from usrgrouperm),(select idacl from acl where name like ('SICALWIN')));
		insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like 'SICALWIN'), (select cast(max(idperm)-1 as int) from usrgrouperm),(select idacl from acl where name like ('SICALWIN')));

		--Insertamos el permiso de PreAltaConsulta para el usuario syssuperuser
		insert into usrgrouperm (idperm, def) values (((select max(idperm) from usrgrouperm)+1), 'Geopista.Inventario.BienesPreAlta.Conversion');
		--Asociamos el permiso con la ACL 14 -- Inventario
		insert into r_acl_perm (idperm, idacl) values ((select max(idperm) from usrgrouperm), (select idacl from acl where name like ('Inventario')));
		--Asociamos el permiso con la ACL 14 y el Usuario 100 -- SysSuperUser
		insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like 'SYSSUPERUSER'), (select idperm from usrgrouperm where def like 'Geopista.Inventario.BienesPreAlta.Conversion'),(select idacl from acl where name like ('Inventario')), 1);

		--Insertamos el permiso de PreAltaInsercion para el usuario syssuperuser
		insert into usrgrouperm (idperm, def) values (((select max(idperm) from usrgrouperm)+1), 'Geopista.Inventario.BienesPreAlta.Insercion');
		--Asociamos el permiso con la ACL 14 -- Inventario
		insert into r_acl_perm (idperm, idacl) values ((select max(idperm) from usrgrouperm), (select idacl from acl where name like ('Inventario')));
		--Asociamos el permiso con la ACL 14 y el Usuario 100 -- SysSuperUser
		insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like 'SYSSUPERUSER'), (select idperm from usrgrouperm where def like 'Geopista.Inventario.BienesPreAlta.Insercion'),(select idacl from acl where name like ('Inventario')), 1);


	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO2";


--Permisos Creados
--select u.id as USERID, u.name AS USER, a.idacl, a.name as ACL , ugp.idperm as IDPERMISO, ugp.def as PERMISO from iuseruserhdr u join r_usr_perm rup on (u.id=rup.userid) join r_acl_perm rap on (rup.idacl=rap.idacl) and (rup.idperm=rap.idperm) join usrgrouperm ugp on (rap.idperm=ugp.idperm)join acl a on (rap.idacl=a.idacl) where (u.name in ('SYSSUPERUSER', 'SICALWIN'))  and (def in ('Geopista.Inventario.BienesPreAlta.Conversion','Geopista.Inventario.BienesPreAlta.Insercion','Geopista.Inventario.Login','LocalGis.Login', 'LocalGis.SICALWIN.BienesPreAlta.Insercion'))order by rap.idperm desc;
--Comprobar que se ha crado bien el usuario SICALWIN, con su grupo...
--select u.*, g.* from iuseruserhdr u join iusergroupuser j on u.id=j.userid join iusergrouphdr g on j.groupid=g.id where u.name in ('SICALWIN');
--Comprobar que se han creado los permisos para SICALWIN
--select a.*, g.* from acl a join r_acl_perm r on a.idacl=r.idacl join usrgrouperm g on r.idperm=g.idperm where a.name in ('SICALWIN');
--Consulta Permisos del Usuario SICALWIN
--select u.id as IDUSER, u.name as USER, g.id as IDGROUP, g.name as GROUP, ugp.idperm as IDPERMISO, ugp.def as PERMISO, a.idacl as IDACL, a.name as ACL from iuseruserhdr u  join iusergroupuser ug on u.id=ug.userid  join iusergrouphdr g on ug.groupid=g.id  join r_usr_perm rup on u.id=rup.userid join r_acl_perm rap on rup.idacl=rap.idacl and rup.idperm=rap.idperm join usrgrouperm ugp on (rap.idperm=ugp.idperm) join acl a on (rap.idacl=a.idacl) where u.name in ('SICALWIN');
--Consulta Permisos del GRUPO SICALWIN
--select u.id as IDUSER, u.name as USER, g.id as IDGROUP, g.name as GROUP, ugp.idperm as IDPERMISO, ugp.def as PERMISO, a.idacl as IDACL, a.name as ACL from iuseruserhdr u join iusergroupuser ug on u.id=ug.userid  join iusergrouphdr g on ug.groupid=g.id  join r_group_perm rgp on ug.groupid=rgp.groupid join r_acl_perm rap on rgp.idacl=rap.idacl and rgp.idperm=rap.idperm join usrgrouperm ugp on (rap.idperm=ugp.idperm) join acl a on (rap.idacl=a.idacl) where u.name in ('SICALWIN');


DROP TABLE IF EXISTS bien_prealta ;
CREATE TABLE bien_prealta
(
  id numeric(8,0) NOT NULL,
  nombre character varying(100),
  descripcion character varying(500),
  id_municipio numeric(10,0),
  tipo_bien numeric(4,0),
  fecha_adquisicion timestamp without time zone,
  coste_adquisicion numeric(10,2),
  esperaAlta  boolean default true, 
  CONSTRAINT bienes_prealta_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE bien_prealta OWNER TO geopista;

DROP SEQUENCE IF EXISTS seq_bien_preAlta;
CREATE SEQUENCE seq_bien_preAlta
  INCREMENT 1
  MINVALUE 0
  MAXVALUE 9223372036854775807
  START 0
  CACHE 1;
ALTER TABLE seq_bien_preAlta OWNER TO postgres;
--Fijamos el valor al maximo de la persona juridico fisica
--SELECT setval('seq_bien_preAlta', (select cast(max(id) as bigint) from bien_PreAlta)+1); 

--Añadimos cuentas contables de prueba primero borramos  todas
delete from contable where cuenta like '%Prueba%';
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(220,4567,'Terrenos y Bienes Naturales', 'Cuenta Prueba3');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(221,456,'Construcciones', 'Cuenta Prueba4');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(201,5446,'Infraestructuras y Bienes Destinados al Uso General', 'Cuenta Prueba5');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(222,1231,'Instalaciones Técnicas', 'Cuenta Prueba6');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(226,6546,'Mobiliario', 'Cuenta Prueba7');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(251,12312,'Valores en Renta Fija', 'Cuenta Prueba8');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(228,4564,'Elementos de Transporte', 'Cuenta Prueba9');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(223,1231,'Maquinaria', 'Cuenta Prueba10');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(224,6894,'Utillaje', 'Cuenta Prueba11');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(227,45645,'Equipos para Procesos de Informacion', 'Cuenta Prueba12');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(219,4684,'Otro Inmovilizado Inmaterial', 'Cuenta Prueba13');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(229,21356,'Otro Inmovilidado Material', 'Cuenta Prueba14');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(108,7897,'Patrimonio entregado en Cesion', 'Cuenta Prueba15');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(208,634,'Bienes del Patrimonio Historico, Artistico y Cultural', 'Cuenta Prueba16');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(252,4567,'Créditos a largo Plazo', 'Cuenta Prueba17');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(250,456,'Inversiones Financieras Permanentes en Capital', 'Cuenta Prueba18');
--------------
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(240,78987,'Terrenos', 'Cuenta Prueba19');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(241,45645,'Construcciones', 'Cuenta Prueba20');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(242,5645,'Aprovechamientos Urbanísticos', 'Cuenta Prueba21');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(249,3214,'Otros Bienes y Derechos del Patrimonio Publico del Suelo', 'Cuenta Prueba22');
--select * from contable where cuenta like '%Prueba%'


DROP INDEX IF EXISTS borrado;
CREATE INDEX borrado ON bienes_inventario(borrado);

DROP INDEX IF EXISTS indx_cultivosgeom;
DROP INDEX IF EXISTS indice_geomcultivos;
create index indice_geomcultivos on cultivosgeom using gist ( "GEOMETRY" );

-- Fin modeloModificaciones 2011-04-28.sql

-- Inicio modeloModificaciones 2011-05-05.sql

DELETE FROM domainnodes where id_domain=11153 and pattern='0';

-- Fin modeloModificaciones 2011-05-05.sql

-- Inicio modeloModificaciones_07_06_2011.sql

--DROP TABLE valor_datos_valoracion
DROP TABLE IF EXISTS valor_datos_valoracion ;
CREATE TABLE valor_datos_valoracion
(
   valor_urbano numeric(10,3) NOT NULL DEFAULT 0, 
   valor_rustico numeric(10,3) NOT NULL DEFAULT 0, 
   id_entidad integer NOT NULL DEFAULT 0,
  CONSTRAINT pk_id_entidad PRIMARY KEY (id_entidad)
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE valor_datos_valoracion OWNER TO geopista;

--Inserto el valor por defecto

--insert into valor_datos_valoracion (valor_urbano, valor_rustico,id_entidad) VALUES(0,0,0);

-- Fin modeloModificaciones_07_06_2011.sql

-- Inicio modeloInventario.sql

-- Table: bien_revertible

-- DROP TABLE bien_revertible;

DROP TABLE IF EXISTS bien_revertible_bien ;
DROP TABLE IF EXISTS observaciones_bien_revertible ;
DROP TABLE IF EXISTS bien_revertible ;

CREATE TABLE bien_revertible
(
  id numeric(10) NOT NULL,
  num_inventario character varying(50),
  fecha_inicio date,
  fecha_vencimiento date,
  fecha_transmision date,
  poseedor character varying(100),
  titulo_posesion character varying(100),
  condiciones_reversion character varying(255),
  detalles character varying(500),
  cat_transmision character varying(10),
  importe numeric(10,2),
  id_cuenta_amortizacion numeric(8),
  id_municipio numeric(5) NOT NULL,
  CONSTRAINT bien_revertible_pkey PRIMARY KEY (id),
  CONSTRAINT fk_amortizacion_br FOREIGN KEY (id_cuenta_amortizacion)
      REFERENCES amortizacion (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE bien_revertible OWNER TO geopista;

-- Index: fki_amortizacion_br

-- DROP INDEX fki_amortizacion_br;
DROP INDEX IF EXISTS fki_amortizacion_br;
CREATE INDEX fki_amortizacion_br
  ON bien_revertible
  USING btree
  (id_cuenta_amortizacion);
  -- Table: bien_revertible_bien

-- DROP TABLE bien_revertible_bien;

DROP TABLE IF EXISTS bien_revertible_bien ;
CREATE TABLE bien_revertible_bien
(
  id_bien integer NOT NULL,
  id_bien_revertible numeric(10) NOT NULL,
  CONSTRAINT bien_revertible_bien_fkey FOREIGN KEY (id_bien_revertible)
      REFERENCES bien_revertible (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE bien_revertible_bien OWNER TO geopista;


-- Table: observaciones_bien_revertible

-- DROP TABLE observaciones_bien_revertible;
DROP TABLE IF EXISTS observaciones_bien_revertible ;
CREATE TABLE observaciones_bien_revertible
(
  id integer NOT NULL,
  descripcion character varying(1000),
  fecha timestamp without time zone,
  fecha_ultima_modificacion timestamp without time zone,
  id_bien integer NOT NULL,
  CONSTRAINT obs_pkey PRIMARY KEY (id),
  CONSTRAINT obs_inventario_fkey FOREIGN KEY (id_bien)
      REFERENCES bien_revertible (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=TRUE);
ALTER TABLE observaciones_bien_revertible OWNER TO geopista;

-- Table: lote

-- DROP TABLE lote;
DROP TABLE IF EXISTS lote_bien ;
DROP TABLE IF EXISTS lote ;
CREATE TABLE lote
(
  id_lote numeric(10) NOT NULL,
  nombre_lote character varying(100),
  fecha_alta date,
  fecha_baja date,
  fecha_ultima_modificacion date,
  seguro character varying(100),
  descripcion character varying(1000),
  destino character varying(255),
  id_municipio numeric(5) NOT NULL,
  CONSTRAINT lote_pkey PRIMARY KEY (id_lote)
)
WITH (OIDS=FALSE);
ALTER TABLE lote OWNER TO geopista;

-- Table: lote_bien

-- DROP TABLE lote_bien;

DROP TABLE IF EXISTS lote_bien ;
CREATE TABLE lote_bien
(
  id_bien integer NOT NULL,
  id_lote numeric(10) NOT NULL,
  CONSTRAINT lote_bien_pkey PRIMARY KEY (id_lote, id_bien),
  CONSTRAINT lote_bien_fkey2 FOREIGN KEY (id_lote)
      REFERENCES lote (id_lote) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=FALSE);
ALTER TABLE lote_bien OWNER TO geopista;

-- Table: anexo_lote

-- DROP TABLE anexo_lote;
DROP TABLE IF EXISTS anexo_lote ;
CREATE TABLE anexo_lote
(
  id_documento integer NOT NULL,
  id_lote numeric(10) NOT NULL,
  CONSTRAINT anexo_lote_pkey PRIMARY KEY (id_documento, id_lote)
)
WITH (OIDS=TRUE);
ALTER TABLE anexo_lote OWNER TO geopista;


select f_add_col('public.bienes_inventario','fecha_aprobacion_pleno', 'ALTER TABLE bienes_inventario ADD COLUMN fecha_aprobacion_pleno timestamp without time zone;');

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT TRADUCCION FROM DICTIONARY WHERE TRADUCCION = 'Lotes') THEN
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Lotes');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Lotes');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Lotes');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Lotes');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Lotes');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),11150,'4',CURRVAL('seq_dictionary'),7,1,100300);
		  
		INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Tipo lotes',4);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Tipo lotes');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Tipo lotes');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Tipo lotes');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Tipo lotes');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Tipo lotes');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'',CURRVAL('seq_dictionary'),4);  
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Muebles no comprendidos en los Epigrafes anteriores');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Muebles no comprendidos en los Epigrafes anteriores');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Muebles no comprendidos en los Epigrafes anteriores');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Muebles no comprendidos en los Epigrafes anteriores');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Muebles no comprendidos en los Epigrafes anteriores');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'13',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-1);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Histórico/Artísticos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Histórico/Artísticos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Histórico/Artísticos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Histórico/Artísticos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Histórico/Artísticos');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'7',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-2);
		  
		INSERT INTO DOMAINS(ID,NAME,ID_CATEGORY) VALUES (NEXTVAL('seq_domains'),'Transmision',4);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Transmision');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Transmision');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Transmision');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Transmision');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Transmision');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'',CURRVAL('seq_dictionary'),4);  
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','<Sin asignar>');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]<Sin asignar>');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]<Sin asignar>');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]<Sin asignar>');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]<Sin asignar>');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'0',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-1);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Atribución de la Ley');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Atribución de la Ley');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Atribución de la Ley');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Atribución de la Ley');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Atribución de la Ley');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'1',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Donación, Herencia o Legado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Donación, Herencia o Legado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Donación, Herencia o Legado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Donación, Herencia o Legado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Donación, Herencia o Legado');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'2',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-3);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Ocupación');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Ocupacion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Ocupacion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Ocupacion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Ocupacion');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'3',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-4);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Onerosa Expropiación');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Onerosa Expropiacion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Onerosa Expropiacion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Onerosa Expropiacion');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Onerosa Expropiacion');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'4',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-5);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Onerosa Costo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Onerosa Costo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Onerosa Costo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Onerosa Costo');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Onerosa Costo');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'5',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-6);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Prescripción Adquisitiva');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Prescripcion Adquisitiva');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Prescripcion Adquisitiva');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Prescripcion Adquisitiva');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Prescripcion Adquisitiva');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'6',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-7);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Otros Modos Conforme al Ordenamiento Jurídico');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Otros Modos Conforme al Ordenamiento Juridico');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Otros Modos Conforme al Ordenamiento Juridico');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Otros Modos Conforme al Ordenamiento Juridico');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Otros Modos Conforme al Ordenamiento Juridico');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'7',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-8);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Inmemorial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Inmemorial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Inmemorial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Inmemorial');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Inmemorial');
		INSERT INTO DOMAINNODES(ID,ID_DOMAIN,PATTERN,ID_DESCRIPTION,TYPE, permissionlevel, parentdomain) VALUES (NEXTVAL('seq_domainnodes'),CURRVAL('seq_domains'),'8',CURRVAL('seq_dictionary'),7,1,CURRVAL('seq_domainnodes')-9);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO3";

SELECT create_constraint_if_not_exists(
        'bi_revision_actual',
        'ALTER TABLE bienes_inventario ADD CONSTRAINT bi_revision_actual UNIQUE (id, revision_actual);');

        
----Estos cambios los añado por que se ha añadido versionado en la tabla bienes inventario 
----supongo que también estarán en otro sitio

-- Estas tablas vienen de serie con Localgis no es necesario ninguna comprobacion

-- Todo lo anterior ya está ejecutado en LocalGIS 2.0
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'credito_derecho_fkey' ) THEN	
		ALTER TABLE credito_derecho DROP CONSTRAINT credito_derecho_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'derechosreales_fk') THEN	
		ALTER TABLE derechos_reales DROP CONSTRAINT  derechosreales_fk;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'inmuebles_fkey') THEN	
		ALTER TABLE inmuebles DROP CONSTRAINT inmuebles_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'mejoras_inventario_fkey') THEN	
		ALTER TABLE mejoras_inventario DROP CONSTRAINT mejoras_inventario_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'muebles_fkey') THEN	
		ALTER TABLE muebles DROP CONSTRAINT muebles_fkey;
	END IF;	
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'observaciones_inventario_fkey') THEN	
		ALTER TABLE observaciones_inventario DROP CONSTRAINT observaciones_inventario_fkey;
	END IF;	
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'refcatastrales_inventario_fkey') THEN	
		ALTER TABLE refcatastrales_inventario DROP CONSTRAINT refcatastrales_inventario_fkey;
	END IF;	
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'refcatastrales_inventario_fkey') THEN	
		ALTER TABLE refcatastrales_inventario DROP CONSTRAINT refcatastrales_inventario_fkey;
	END IF;	
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'semoviente_fkey') THEN	
		ALTER TABLE semoviente DROP CONSTRAINT semoviente_fkey;
	END IF;	
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'usos_funcionales_inventario_fkey') THEN	
		ALTER TABLE usos_funcionales_inventario DROP CONSTRAINT usos_funcionales_inventario_fkey;
	END IF;	
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'valor_mobiliario_fkey') THEN	
		ALTER TABLE valor_mobiliario DROP CONSTRAINT valor_mobiliario_fkey;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'vehiculo_fkey') THEN	
		ALTER TABLE vehiculo DROP CONSTRAINT vehiculo_fkey;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'vias_inventario_fkey') THEN	
		ALTER TABLE vias_inventario DROP CONSTRAINT vias_inventario_fkey;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'bien_revertible_bien_fkey') THEN	
		ALTER TABLE bien_revertible_bien DROP CONSTRAINT bien_revertible_bien_fkey;
	END IF;
	
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'lote_bien_fkey2') THEN	
		ALTER TABLE lote_bien DROP CONSTRAINT lote_bien_fkey2;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'inmuebles_rusticos_pkey') THEN	
		ALTER TABLE inmuebles_rusticos DROP CONSTRAINT inmuebles_rusticos_pkey;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'refcatastrales_inventario_pkey') THEN	
		ALTER TABLE refcatastrales_inventario DROP CONSTRAINT refcatastrales_inventario_pkey;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'usos_funcionales_inventario_pkey') THEN	
		ALTER TABLE usos_funcionales_inventario DROP CONSTRAINT usos_funcionales_inventario_pkey;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'inmuebles_urbanos_pkey') THEN	
		ALTER TABLE inmuebles_urbanos DROP CONSTRAINT inmuebles_urbanos_pkey;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'obs_pkey') THEN	
		ALTER TABLE observaciones_bien_revertible drop constraint obs_pkey;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'obs_inventario_fkey') THEN	
		ALTER TABLE observaciones_bien_revertible drop constraint  obs_inventario_fkey;
	END IF;
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'bien_revertible_pkey') THEN	
		ALTER TABLE bien_revertible DROP CONSTRAINT bien_revertible_pkey;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	
	ALTER TABLE credito_derecho
  ADD CONSTRAINT credito_derecho_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;



	ALTER TABLE derechos_reales
  ADD CONSTRAINT derechosreales_fk FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE CASCADE;



	ALTER TABLE inmuebles
  ADD CONSTRAINT inmuebles_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;



	ALTER TABLE mejoras_inventario
  ADD CONSTRAINT mejoras_inventario_fkey FOREIGN KEY (id_bien, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


	ALTER TABLE muebles
  ADD CONSTRAINT muebles_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


	ALTER TABLE observaciones_inventario
  ADD CONSTRAINT observaciones_inventario_fkey FOREIGN KEY (id_bien, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE refcatastrales_inventario
  ADD CONSTRAINT refcatastrales_inventario_fkey FOREIGN KEY (id_bien, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

	ALTER TABLE semoviente
  ADD CONSTRAINT semoviente_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

	ALTER TABLE usos_funcionales_inventario
  ADD CONSTRAINT usos_funcionales_inventario_fkey FOREIGN KEY (id_bien, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

	ALTER TABLE valor_mobiliario
  ADD CONSTRAINT valor_mobiliario_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

	ALTER TABLE vehiculo
  ADD CONSTRAINT vehiculo_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	  
	ALTER TABLE vias_inventario
  ADD CONSTRAINT vias_inventario_fkey FOREIGN KEY (id, revision_actual)
      REFERENCES bienes_inventario (id, revision_actual) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	exception when others then
		RAISE NOTICE 'No se puede crear la foreign key';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO1";


--ALTER TABLE lote_bien DROP CONSTRAINT lote_bien_fkey2;
--ALTER TABLE bienes_inventario DROP CONSTRAINT bienes_inventario_pkey cascade;

ALTER TABLE inmuebles_rusticos
  ADD CONSTRAINT inmuebles_rusticos_pkey PRIMARY KEY(id, revision_actual);

-- ALTER TABLE inmuebles_urbanos ADD COLUMN revision_actual numeric(10);
-- ALTER TABLE inmuebles_urbanos ADD COLUMN revision_expirada numeric(10);

ALTER TABLE inmuebles_urbanos
  ADD CONSTRAINT inmuebles_urbanos_pkey PRIMARY KEY(id, revision_actual);

ALTER TABLE refcatastrales_inventario
  ADD CONSTRAINT refcatastrales_inventario_pkey PRIMARY KEY(id, revision_actual);


ALTER TABLE usos_funcionales_inventario
  ADD CONSTRAINT usos_funcionales_inventario_pkey PRIMARY KEY(id, revision_actual);

--Versionado de la tabla bien revertible





ALTER TABLE observaciones_bien_revertible ADD COLUMN revision_actual numeric(10)NOT NULL DEFAULT 0;
ALTER TABLE observaciones_bien_revertible ADD COLUMN revision_expirada numeric(10) DEFAULT 9999999999; 

-- TODO. Tiene constraint esta tabla??
--alter table bien_revertible_bien drop constraint bien_revertible_bien_fkey;

ALTER TABLE bien_revertible_bien ADD COLUMN revision numeric(10)NOT NULL DEFAULT 0;

ALTER TABLE bien_revertible ADD COLUMN revision_actual numeric(10)NOT NULL DEFAULT 0;
ALTER TABLE bien_revertible ADD COLUMN revision_expirada numeric(10) DEFAULT 9999999999; 




ALTER TABLE bien_revertible ADD CONSTRAINT bien_revertible_pkey  primary key (id, revision_actual);

ALTER TABLE observaciones_bien_revertible add constraint obs_pkey primary key ( id, revision_actual);

alter table observaciones_bien_revertible add constraint obs_inventario_fkey 
FOREIGN KEY (id_bien, revision_actual) references bien_revertible(id, revision_actual);

alter table bien_revertible_bien  add constraint bien_revertible_bien_fkey 
FOREIGN KEY (id_bien_revertible, revision) references bien_revertible(id, revision_actual);

DROP SEQUENCE IF EXISTS seq_inventario_bien_revertible;
CREATE SEQUENCE seq_inventario_bien_revertible
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;  

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM tables_inventario WHERE name = 'bien_revertible') THEN
		insert into tables_inventario values (12009, 'bien_revertible');
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO4";


--Diferencias entre el esquema y las tablas

ALTER TABLE amortizacion ALTER porcentaje TYPE numeric(6,2);
ALTER TABLE amortizacion ALTER total_amortizado TYPE numeric(16,2);

ALTER TABLE credito_derecho ALTER importe TYPE numeric(16,2);

ALTER TABLE derechos_reales ALTER  coste TYPE numeric(16,2);
ALTER TABLE derechos_reales ALTER  valor TYPE numeric(16,2);
ALTER TABLE derechos_reales ALTER  importe_frutos TYPE numeric(16,2);

ALTER TABLE inmuebles ALTER superficie_registral_suelo type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_catastral_suelo type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_real_suelo type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_registral_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_catastral_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_real_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   valor_derechos_favor type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_derechos_contra type numeric(16,2);
ALTER TABLE inmuebles ALTER   superficie_ocupada_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_construida_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   superficie_enplanta_construccion type numeric(10,2);
ALTER TABLE inmuebles ALTER   valor_adquisicion_suelo type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_catastral_suelo type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_actual_suelo type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_adquisicion_construccion type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_catastral_construccion type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_actual_construccion type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_adquisicion_inmueble type numeric(16,2);
ALTER TABLE inmuebles ALTER   valor_actual_inmueble type numeric(16,2);
ALTER TABLE inmuebles ALTER   importe_frutos type numeric(16,2);
ALTER TABLE inmuebles ALTER     edificabilidad type numeric(10,2);


ALTER TABLE muebles ALTER  coste_adquisicion type numeric(16,2);
ALTER TABLE muebles ALTER  valor_actual type numeric(16,2);
ALTER TABLE muebles ALTER  importe_frutos type numeric(16,2);

ALTER TABLE semoviente ALTER  importe_frutos type numeric(16,2);
ALTER TABLE semoviente ALTER    coste_adquisicion type numeric(16,2);
ALTER TABLE semoviente ALTER    valor_actual type numeric(16,2);

ALTER TABLE valor_mobiliario ALTER  coste_adquisicion type numeric(16,2);
ALTER TABLE valor_mobiliario ALTER  valor_actual type numeric(16,2);
ALTER TABLE valor_mobiliario ALTER  precio type numeric(16,2);
ALTER TABLE valor_mobiliario ALTER  capital type numeric(16,2);
ALTER TABLE valor_mobiliario ALTER  importe_frutos type numeric(16,2);

ALTER TABLE vehiculo ALTER  importe_frutos  type numeric(16,2);
ALTER TABLE vehiculo ALTER coste_adquisicion  type numeric(16,2);
ALTER TABLE Vehiculo ALTER valor_actual  type numeric(16,2);

ALTER TABLE vias_inventario ALTER  metros_pavimentados  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   metros_no_pavimentados  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   zonas_verdes  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   longitud  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   ancho  type numeric(16,2);
ALTER TABLE vias_inventario ALTER   valor_actual  type numeric(16,2);


ALTER TABLE seguros ALTER   prima type numeric(14,2);
ALTER TABLE seguros ALTER   poliza type numeric(14,2);

ALTER TABLE mejoras_inventario ALTER   importe type numeric(16,2);

ALTER TABLE usos_funcionales_inventario alter  superficie type numeric(10,2);

alter table bien_revertible alter importe type numeric(12,2);


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM documento_tipos WHERE mime_type ilike '%application/andrew-inset%') THEN
		insert into documento_tipos values (1, UPPER('ez'),upper('application/andrew-inset'));
		insert into documento_tipos values (2, UPPER('hqx'),upper('application/mac-binhex40'));
		insert into documento_tipos values (3, UPPER('mac'),upper('application/mac-compactpro'));
		insert into documento_tipos values (4, UPPER('doc'),upper('application/msword'));
		insert into documento_tipos values (5, UPPER('bin'),upper('application/octet-stream'));
		insert into documento_tipos values (6, UPPER('dms'),upper('application/octet-stream'));
		insert into documento_tipos values (7, UPPER('lha'),upper('application/octet-stream'));
		insert into documento_tipos values (8, UPPER('lzh'),upper('application/octet-stream'));
		insert into documento_tipos values (9, UPPER('exe'),upper('application/octet-stream'));
		insert into documento_tipos values (10, UPPER('rar'),upper('application/x-rar-compressed'));
		insert into documento_tipos values (11, UPPER('class'),upper('application/octet-stream'));
		insert into documento_tipos values (12, UPPER('so'),upper('application/octet-stream'));
		insert into documento_tipos values (13, UPPER('dll'),upper('application/octet-stream'));
		insert into documento_tipos values (14, UPPER('oda'),upper('application/oda'));
		insert into documento_tipos values (15, UPPER('pdf'),upper('application/pdf'));
		insert into documento_tipos values (16, UPPER('ai'),upper('application/postscript'));
		insert into documento_tipos values (17, UPPER('eps'),upper('application/postscript'));
		insert into documento_tipos values (18, UPPER('ps'),upper('application/postscript'));
		insert into documento_tipos values (19, UPPER('smi'),upper('application/smil'));
		insert into documento_tipos values (20, UPPER('smil'),upper('application/smil'));
		insert into documento_tipos values (21, UPPER('mif'),upper('application/vnd.mif'));
		insert into documento_tipos values (22, UPPER('xls'),upper('application/vnd.ms-excel'));
		insert into documento_tipos values (23, UPPER('ppt'),upper('application/vnd.ms-powerpoint'));
		insert into documento_tipos values (24, UPPER('wbxml'),upper('application/vnd.wap.wbxml'));
		insert into documento_tipos values (25, UPPER('wmlc'),upper('application/vnd.wap.wmlc'));
		insert into documento_tipos values (26, UPPER('wmlsc'),upper('application/vnd.wap.wmlscriptc'));
		insert into documento_tipos values (27, UPPER('bcpio'),upper('application/x-bcpio'));
		insert into documento_tipos values (28, UPPER('vcd'),upper('application/x-cdlink'));
		insert into documento_tipos values (29, UPPER('pgn'),upper('application/x-chess-pgn'));
		insert into documento_tipos values (30, UPPER('cpio'),upper('application/x-cpio'));
		insert into documento_tipos values (31, UPPER('csh'),upper('application/x-csh'));
		insert into documento_tipos values (32, UPPER('dcr'),upper('application/x-director'));
		insert into documento_tipos values (33, UPPER('dir'),upper('application/x-director'));
		insert into documento_tipos values (34, UPPER('dxr'),upper('application/x-director'));
		insert into documento_tipos values (35, UPPER('dvi'),upper('application/x-dvi'));
		insert into documento_tipos values (36, UPPER('spl'),upper('application/x-futuresplash'));
		insert into documento_tipos values (37, UPPER('gtar'),upper('application/x-gtar'));
		insert into documento_tipos values (38, UPPER('hdf'),upper('application/x-hdf'));
		insert into documento_tipos values (39, UPPER('js'),upper('application/x-javascript'));
		insert into documento_tipos values (40, UPPER('skp'),upper('application/x-koan'));
		insert into documento_tipos values (41, UPPER('skd'),upper('application/x-koan'));
		insert into documento_tipos values (42, UPPER('skt'),upper('application/x-koan'));
		insert into documento_tipos values (43, UPPER('skm'),upper('application/x-koan'));
		insert into documento_tipos values (44, UPPER('latex'),upper('application/x-latex'));
		insert into documento_tipos values (45, UPPER('nc'),upper('application/x-netcdf'));
		insert into documento_tipos values (46, UPPER('cdf'),upper('application/x-netcdf'));
		insert into documento_tipos values (47, UPPER('sh'),upper('application/x-sh'));
		insert into documento_tipos values (48, UPPER('shar'),upper('application/x-shar'));
		insert into documento_tipos values (49, UPPER('swf'),upper('application/x-shockwave-flash'));
		insert into documento_tipos values (50, UPPER('sit'),upper('application/x-stuffit'));
		insert into documento_tipos values (51, UPPER('sv4cpio'),upper('application/x-sv4cpio'));
		insert into documento_tipos values (52, UPPER('sv4crc'),upper('application/x-sv4crc'));
		insert into documento_tipos values (53, UPPER('tar'),upper('application/x-tar'));
		insert into documento_tipos values (54, UPPER('tcl'),upper('application/x-tcl'));
		insert into documento_tipos values (55, UPPER('tex'),upper('application/x-tex'));
		insert into documento_tipos values (56, UPPER('texinfo'),upper('application/x-texinfo'));
		insert into documento_tipos values (57, UPPER('texi'),upper('application/x-texinfo'));
		insert into documento_tipos values (58, UPPER('t'),upper('application/x-troff'));
		insert into documento_tipos values (59, UPPER('tr'),upper('application/x-troff'));
		insert into documento_tipos values (60, UPPER('roff'),upper('application/x-troff'));
		insert into documento_tipos values (61, UPPER('man'),upper('application/x-troff-man'));
		insert into documento_tipos values (62, UPPER('me'),upper('application/x-troff-me'));
		insert into documento_tipos values (63, UPPER('ms'),upper('application/x-troff-ms'));
		insert into documento_tipos values (64, UPPER('ustar'),upper('application/x-ustar'));
		insert into documento_tipos values (65, UPPER('src'),upper('application/x-wais-source'));
		insert into documento_tipos values (66, UPPER('xhtml'),upper('application/xhtml+xml'));
		insert into documento_tipos values (67, UPPER('xht'),upper('application/xhtml+xml'));
		insert into documento_tipos values (68, UPPER('zip'),upper('application/zip'));
		insert into documento_tipos values (69, UPPER('au'),upper('audio/basic'));
		insert into documento_tipos values (70, UPPER('snd'),upper('audio/basic'));
		insert into documento_tipos values (71, UPPER('mid'),upper('audio/midi'));
		insert into documento_tipos values (72, UPPER('midi'),upper('audio/midi'));
		insert into documento_tipos values (73, UPPER('kar'),upper('audio/midi'));
		insert into documento_tipos values (74, UPPER('mpga'),upper('audio/mpeg'));
		insert into documento_tipos values (75, UPPER('mp2'),upper('audio/mpeg'));
		insert into documento_tipos values (76, UPPER('mp3'),upper('audio/mpeg'));
		insert into documento_tipos values (77, UPPER('aif'),upper('audio/x-aiff'));
		insert into documento_tipos values (78, UPPER('aiff'),upper('audio/x-aiff'));
		insert into documento_tipos values (79, UPPER('aifc'),upper('audio/x-aiff'));
		insert into documento_tipos values (80, UPPER('m3u'),upper('audio/x-mpegurl'));
		insert into documento_tipos values (81, UPPER('ram'),upper('audio/x-pn-realaudio'));
		insert into documento_tipos values (82, UPPER('rm'),upper('audio/x-pn-realaudio'));
		insert into documento_tipos values (83, UPPER('rpm'),upper('audio/x-pn-realaudio-plugin'));
		insert into documento_tipos values (84, UPPER('ra'),upper('audio/x-realaudio'));
		insert into documento_tipos values (85, UPPER('wav'),upper('audio/x-wav'));
		insert into documento_tipos values (86, UPPER('pdb'),upper('chemical/x-pdb'));
		insert into documento_tipos values (87, UPPER('xyz'),upper('chemical/x-xyz'));
		insert into documento_tipos values (89, UPPER('bmp'),upper('image/bmp'));
		insert into documento_tipos values (90, UPPER('gif'),upper('image/gif'));
		insert into documento_tipos values (91, UPPER('ief'),upper('image/ief'));
		insert into documento_tipos values (92, UPPER('jpeg'),upper('image/jpeg'));
		insert into documento_tipos values (93, UPPER('jpg'),upper('image/jpeg'));
		insert into documento_tipos values (94, UPPER('jpe'),upper('image/jpeg'));
		insert into documento_tipos values (95, UPPER('png'),upper('image/png'));
		insert into documento_tipos values (96, UPPER('tiff'),upper('image/tiff'));
		insert into documento_tipos values (97, UPPER('tif'),upper('image/tiff'));
		insert into documento_tipos values (98, UPPER('djvu'),upper('image/vnd.djvu'));
		insert into documento_tipos values (99, UPPER('djv'),upper('image/vnd.djvu'));
		insert into documento_tipos values (100, UPPER('wbmp'),upper('image/vnd.wap.wbmp'));
		insert into documento_tipos values (101, UPPER('ras'),upper('image/x-cmu-raster'));
		insert into documento_tipos values (102, UPPER('pnm'),upper('image/x-portable-anymap'));
		insert into documento_tipos values (103, UPPER('pbm'),upper('image/x-portable-bitmap'));
		insert into documento_tipos values (104, UPPER('pgm'),upper('image/x-portable-graymap'));
		insert into documento_tipos values (105, UPPER('ppm'),upper('image/x-portable-pixmap'));
		insert into documento_tipos values (106, UPPER('rgb'),upper('image/x-rgb'));
		insert into documento_tipos values (107, UPPER('xbm'),upper('image/x-xbitmap'));
		insert into documento_tipos values (108, UPPER('xpm'),upper('image/x-xpixmap'));
		insert into documento_tipos values (109, UPPER('xwd'),upper('image/x-xwindowdump'));
		insert into documento_tipos values (110, UPPER('igs'),upper('model/iges'));
		insert into documento_tipos values (111, UPPER('iges'),upper('model/iges'));
		insert into documento_tipos values (112, UPPER('msh'),upper('model/mesh'));
		insert into documento_tipos values (113, UPPER('mesh'),upper('model/mesh'));
		insert into documento_tipos values (114, UPPER('silo'),upper('model/mesh'));
		insert into documento_tipos values (115, UPPER('wrl'),upper('model/vrml'));
		insert into documento_tipos values (116, UPPER('vrml'),upper('model/vrml'));
		insert into documento_tipos values (117, UPPER('css'),upper('text/css'));
		insert into documento_tipos values (118, UPPER('html'),upper('text/html'));
		insert into documento_tipos values (119, UPPER('htm'),upper('text/html'));
		insert into documento_tipos values (120, UPPER('asc'),upper('text/plain'));
		insert into documento_tipos values (121, UPPER('txt'),upper('text/plain'));
		insert into documento_tipos values (122, UPPER('rtx'),upper('text/richtext'));
		insert into documento_tipos values (123, UPPER('rtf'),upper('text/rtf'));
		insert into documento_tipos values (124, UPPER('sgml'),upper('text/sgml'));
		insert into documento_tipos values (125, UPPER('sgm'),upper('text/sgml'));
		insert into documento_tipos values (126, UPPER('tsv'),upper('text/tab-separated-values'));
		insert into documento_tipos values (127, UPPER('wml'),upper('text/vnd.wap.wml'));
		insert into documento_tipos values (128, UPPER('wmls'),upper('text/vnd.wap.wmlscript'));
		insert into documento_tipos values (129, UPPER('etx'),upper('text/x-setext'));
		insert into documento_tipos values (130, UPPER('xsl'),upper('text/xml'));
		insert into documento_tipos values (131, UPPER('xml'),upper('text/xml'));
		insert into documento_tipos values (132, UPPER('mpeg'),upper('video/mpeg'));
		insert into documento_tipos values (133, UPPER('mpg'),upper('video/mpeg'));
		insert into documento_tipos values (134, UPPER('mpe'),upper('video/mpeg'));
		insert into documento_tipos values (135, UPPER('qt'),upper('video/quicktime'));
		insert into documento_tipos values (136, UPPER('mov'),upper('video/quicktime'));
		insert into documento_tipos values (137, UPPER('mxu'),upper('video/vnd.mpegurl'));
		insert into documento_tipos values (138, UPPER('avi'),upper('video/x-msvideo'));
		insert into documento_tipos values (139, UPPER('movie'),upper('video/x-sgi-movie'));
		insert into documento_tipos values (140, UPPER('ice'),upper('x-conference/x-cooltalk'));
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO5";


--Modificacion para añadir el tipo de arrendamiento
select f_add_col('public.credito_derecho','arrendamiento', 'ALTER TABLE credito_derecho  ADD COLUMN arrendamiento numeric(1) default 0;');




--Modificaciones versionado bien_revertible
select f_add_col('public.bien_Revertible','borrado', 'alter table bien_Revertible add borrado character varying(1) DEFAULT ''0''::character varying;');


select f_add_col('public.bien_Revertible','fecha_baja', 'alter table bien_Revertible add   fecha_baja timestamp without time zone;');


--Modificaciones realizadas al cambiar el id del documento añadiendo el código hash

alter table documento alter column id_documento type character varying(100);
alter table anexofeature alter column id_documento type character varying(100);
alter table anexo_inventario alter column id_documento type character varying(100);
alter table anexo_lote alter column id_documento type character varying(100);



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	
ALTER TABLE civil_work_document_thumbnail DROP CONSTRAINT civil_work_document_thumbnail_fk;
	exception when others then
		RAISE NOTICE 'No se puede borrar la constraint civil_work_document_thumbnail_fk.';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO1.5";



alter table civil_work_document_thumbnail alter column id_document type character varying(100);
alter table civil_work_documents alter column id_document type character varying(100);
ALTER TABLE civil_work_document_thumbnail add constraint civil_work_document_thumbnail_fk FOREIGN KEY (id_document, id_warning) references civil_work_documents (id_document, id_warning) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	
CREATE ROLE consultas LOGIN NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
	exception when others then
		RAISE NOTICE 'No se puede crear el role de consultas';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO1.5";


  
  

--Modificaciones para que los formularios se realicen a través de una select
GRANT SELECT ON TABLE bienes_inventario to consultas;
GRANT SELECT ON TABLE credito_derecho to consultas;
GRANT SELECT ON TABLE versiones to consultas;
GRANT SELECT ON TABLE tables_inventario to consultas;
GRANT SELECT ON TABLE domains to consultas;
GRANT SELECT ON TABLE domainnodes to consultas;
GRANT SELECT ON TABLE contable to consultas;
GRANT SELECT ON TABLE entidades_municipios to consultas;
GRANT SELECT ON TABLE vehiculo to consultas;
GRANT SELECT ON TABLE semoviente to consultas;
GRANT SELECT ON TABLE valor_mobiliario to consultas;
GRANT SELECT ON TABLE derechos_reales to consultas;
GRANT SELECT ON TABLE muebles to consultas;
GRANT SELECT ON TABLE vias_inventario to consultas;
GRANT SELECT ON TABLE inmuebles to consultas;
GRANT SELECT ON TABLE bien_revertible to consultas;
GRANT SELECT ON TABLE lote to consultas;
GRANT SELECT ON TABLE seguros_inventario to consultas;


--Función que devuelve la traduccion
CREATE OR REPLACE FUNCTION getdictionarydescription(domainname text, pattern text, identidad integer, locale text)
  RETURNS text AS
$BODY$select dictionary.traduccion from Domains inner join DomainNodes on 
domains.id=domainNodes.id_domain inner join dictionary on 
domainNodes.id_description = dictionary.id_vocablo left outer join entidades_municipios on
domainNodes.id_municipio=entidades_municipios.id_entidad where domains.name=$1 and
domainNodes.pattern=$2 and
(domainNodes.id_municipio is null or entidades_municipios.id_municipio=$3) and 
dictionary.locale=$4 order by domainnodes.id_municipio;$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getdictionarydescription(text, text, integer, text) OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, integer, text) TO public;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, integer, text) TO postgres;
GRANT EXECUTE ON FUNCTION getdictionarydescription(text, text, integer, text) TO consultas;

ALTER TABLE inmuebles ALTER refcat TYPE character varying(20);
ALTER TABLE refcatastrales_inventario ALTER ref_catastral TYPE character varying(20);


-- Para que vayan mejor las consultas

DROP INDEX IF EXISTS indx_id_tabla_versionada;
CREATE INDEX indx_id_tabla_versionada ON versiones (id_table_versionada);

DROP INDEX IF EXISTS INDX_BI_NUMINVENTARIO;
CREATE UNIQUE INDEX INDX_BI_NUMINVENTARIO  ON bienes_inventario (numinventario, id_municipio, revision_actual);

-- Nuevos campos bienes revertibles
select f_add_col('public.bien_Revertible','fecha_alta', 'alter table bien_Revertible add   fecha_alta timestamp without time zone;');
select f_add_col('public.bien_Revertible','fecha_ultima_modificacion', 'alter table bien_Revertible add   fecha_ultima_modificacion timestamp without time zone;');
select f_add_col('public.bien_Revertible','nombre', 'ALTER TABLE bien_revertible ADD COLUMN nombre character varying(255);');
select f_add_col('public.bien_Revertible','organizacion', 'ALTER TABLE bien_revertible ADD COLUMN organizacion character varying(50);');
select f_add_col('public.bien_Revertible','fecha_aprobacion_pleno', 'ALTER TABLE bien_revertible ADD COLUMN fecha_aprobacion_pleno timestamp without time zone;');
select f_add_col('public.bien_Revertible','descripcion_bien', 'ALTER TABLE bien_revertible ADD COLUMN descripcion_bien character varying(255);');
select f_add_col('public.bien_Revertible','fecha_adquisicion', 'ALTER TABLE bien_revertible ADD COLUMN fecha_adquisicion timestamp without time zone;');
select f_add_col('public.bien_Revertible','adquisicion', 'ALTER TABLE bien_revertible ADD COLUMN adquisicion character varying(2);');
select f_add_col('public.bien_Revertible','diagnosis', 'ALTER TABLE bien_revertible ADD COLUMN diagnosis character varying(2);');
select f_add_col('public.bien_Revertible','patrimonio_municipal_suelo', 'ALTER TABLE bien_revertible ADD COLUMN patrimonio_municipal_suelo character varying(1);');
select f_add_col('public.bien_Revertible','clase', 'ALTER TABLE bien_revertible ADD COLUMN clase character varying(2);');


DROP TABLE IF EXISTS anexo_bien_revertible;
CREATE TABLE anexo_bien_revertible
(
  id_documento character varying(100) NOT NULL,
  id_bien_revertible numeric(10) NOT NULL,
  CONSTRAINT anexo_bien_revertible_pkey PRIMARY KEY (id_documento, id_bien_revertible)
);
ALTER TABLE anexo_bien_revertible OWNER TO geopista;


ALTER TABLE seguros_inventario ALTER id TYPE numeric(8,0);
ALTER TABLE bienes_inventario ALTER id_seguro TYPE numeric(8,0);

select f_add_col('public.bien_revertible','id_seguro', 'ALTER TABLE bien_revertible  ADD COLUMN id_seguro numeric(8,0) references seguros_inventario (id) on delete set null;');

select f_add_col('public.inmuebles','valor_catastral_inmueble', 'ALTER TABLE inmuebles ADD COLUMN valor_catastral_inmueble numeric(16,2);');
select f_add_col('public.inmuebles','anio_valor_catastral', 'ALTER TABLE inmuebles ADD COLUMN anio_valor_catastral integer;');
select f_add_col('public.inmuebles','edificabilidad_descripcion', 'ALTER TABLE inmuebles ADD COLUMN edificabilidad_descripcion character varying(255);');
select f_add_col('public.inmuebles','fecha_adquisicion_obra', 'ALTER TABLE inmuebles ADD COLUMN fecha_adquisicion_obra timestamp without time zone;
');

select f_add_col('public.lote','id_seguro', 'ALTER TABLE lote  ADD COLUMN id_seguro numeric(8,0) references seguros_inventario (id) on delete set null;');


select f_add_col('public.credito_derecho','clase', 'ALTER TABLE credito_derecho ADD COLUMN clase character varying(3);');
select f_add_col('public.credito_derecho','subclase', 'ALTER TABLE credito_derecho ADD COLUMN subclase character varying(3);');
select f_add_col('public.derechos_reales','clase', 'ALTER TABLE derechos_reales ADD COLUMN clase character varying(3);');
select f_add_col('public.vias_inventario','clase', 'ALTER TABLE vias_inventario ADD COLUMN clase character varying(3);');
select f_add_col('public.muebles','clase', 'ALTER TABLE muebles ADD COLUMN clase character varying(3);');
select f_add_col('public.inmuebles','clase', 'ALTER TABLE inmuebles ADD COLUMN clase character varying(3);');


ALTER TABLE usos_funcionales_inventario ALTER uso TYPE character varying(100);

ALTER TABLE seguros_inventario ALTER poliza TYPE numeric(14,0);
ALTER TABLE seguros ALTER poliza TYPE numeric(14,0);

-- Para almacenar las secuencias de inventario especificas por epigrafe y municipio:


DROP TABLE IF EXISTS sequences_inventario;
CREATE TABLE sequences_inventario
(
  id_sequence_inventario numeric(8,0) NOT NULL DEFAULT (0)::numeric,
  tablename character varying(250) NOT NULL DEFAULT ' '::character varying,
  field character varying(250) NOT NULL DEFAULT ' '::character varying,
  "value" numeric(10,0) DEFAULT (0)::numeric,
  incrementvalue numeric(10,0) NOT NULL DEFAULT (0)::numeric,
  minimumvalue numeric(10,0) NOT NULL DEFAULT (0)::numeric,
  maximumvalue numeric(10,0) NOT NULL DEFAULT (0)::numeric,
  circular character(1) NOT NULL DEFAULT ' '::bpchar,
  id_municipio numeric(5,0) DEFAULT 0,
  CONSTRAINT sequences_inventario_pkey PRIMARY KEY (id_sequence_inventario),
  CONSTRAINT sequences_inventario_tablename_key UNIQUE (tablename, field, id_municipio)
)
WITH (
  OIDS=TRUE
);
ALTER TABLE sequences_inventario OWNER TO geopista;

-- Fin modeloInventario.sql

-- Inicio sso.sql

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM iuseruserhdr WHERE name = 'SSOADMIN') THEN	
		INSERT INTO iuseruserhdr (id,name,password,flags,stat,numbadcnts,crtrid,id_entidad,fecha_proxima_modificacion) 
		VALUES ((SELECT max(id)+1 FROM iuseruserhdr),'SSOADMIN','jc/KYmLdqMWTBtEaBuHi2w==',0,0,0,325,0,null);

		INSERT INTO r_usr_perm (userid,idperm,idacl,aplica) 
		VALUES ((SELECT max(id) FROM iuseruserhdr),10,1,1);
	END IF;
	-- En Toledo el id 11 ya existe con la aplicacion cementerios
	-- Luego se borran asi que incluimos un identificador unico para no tener problema
	IF NOT EXISTS (SELECT * FROM appgeopista WHERE def = 'Single Sign On') THEN	
		insert into appgeopista (appid,def) values (21,'Single Sign On');
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO6";


-- Fin sso.sql

-- Inicio administracion_postgres.sql



-- Fin administracion_postgres.sql

-- Inicio historicoamortizacion.sql


-- Fin historicoamortizacion.sql

-- Inicio modeloModificaciones_2011-08-11.sql

-- Vacio

-- Fin modeloModificaciones_2011-08-11.sql

-- Inicio modelo_Gestion_Sesiones.sql

DROP TABLE IF EXISTS session_app;
CREATE TABLE session_app(
  id character varying(40) NOT NULL,
  access_order numeric(3,0),
  appid numeric(10,0) NOT NULL,
  CONSTRAINT pk_session_app PRIMARY KEY (id, access_order),
  CONSTRAINT fk_sessionapp_reference_appgeopi FOREIGN KEY (appid)
      REFERENCES appgeopista (appid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_sessionapp_reference_iusercnt FOREIGN KEY (id)
      REFERENCES iusercnt(id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=TRUE
);
ALTER TABLE session_app OWNER TO geopista;


-- queries para insertar en la BD
update query_catalog set query = 'select distinct(iusercnt.id), appgeopista.def, iusercnt.userid, iusercnt.timestamp , iusercnt.timeclose, 
array_to_string (ARRAY(select distinct(appgeopista.def) from appgeopista, session_app where appgeopista.appid = session_app.appid and session_app.id=iusercnt.id ), '', '')
from iusercnt LEFT JOIN session_app ON iusercnt.id = session_app.id, appgeopista
where iusercnt.appid = appgeopista.appid and iusercnt.userid=?
order by iusercnt.timeclose desc' 
where id  = 'conexiones';



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'SM_LayerOperationsBySession') THEN
		insert into query_catalog (id, query, acl, idperm) values ('SM_LayerOperationsBySession', 'select h.* from iusercnt c, appgeopista a, history h where a.appid=c.appid and c.userid = h.user_id and c.id=? and h.user_id=? and h.ts BETWEEN c.timestamp AND c.timeclose', 1, 9205);
	END IF;
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'SM_TableOperationsBySession') THEN
		insert into query_catalog (id, query, acl, idperm) 
		values ('SM_TableOperationsBySession', 'select v.* from iusercnt c, versionesalfa v 
		where c.userid = v.id_autor and c.id = ? and v.id_autor = ? and v.fecha BETWEEN c.timestamp AND c.timeclose', 1, 9205);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO7";



DROP INDEX IF EXISTS history_by_user;
CREATE INDEX history_by_user ON history(user_id);


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM usrgrouperm WHERE def = 'Geopista.Map.CrearMapaGlobal') THEN
		insert into usrgrouperm (idperm,def) values ('4031',	'Geopista.Map.CrearMapaGlobal');	
		insert into r_acl_perm (idperm,	idacl) values (4031,	12);

		insert into usrgrouperm (idperm,def) values ('4032',	'Geopista.Map.PublicarGlobal');	
		insert into r_acl_perm (idperm,	idacl) values (4032,	12);

		insert into usrgrouperm (idperm,def) values ('4033',	'Geopista.Map.BorrarMapasGlobales');	
		insert into r_acl_perm (idperm,	idacl) values (4033,	12);

		insert into usrgrouperm (idperm,def) values ('4034',	'Geopista.Administracion.VerSesiones');	
		insert into r_acl_perm (idperm,	idacl) values (4034,	1);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO8";



DROP INDEX IF EXISTS parcelas_ref_catastral_lower;
CREATE INDEX parcelas_ref_catastral_lower ON parcelas(lower(referencia_catastral));

-- Fin modelo_Gestion_Sesiones.sql


-- Inicio release22.sql

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'updatePassword') THEN
		INSERT INTO query_catalog(id, query, acl, idperm) VALUES('updatePassword','UPDATE iuseruserhdr set password = ?, fecha_proxima_modificacion = current_date + (select cast (periodicidad as integer) from entidad_supramunicipal where id_entidad = ? ) where name = ?',1,9205);
		INSERT INTO query_catalog(id, query, acl, idperm) VALUES('existeUsuario','SELECT id_entidad FROM iuseruserhdr WHERE borrado!=1 and name = ?  and password = ?',1,9205);

		insert into version (id_version, fecha_version) values('MODELO R22', DATE '2012-05-03');
		
		-- Fin release22.sql
	END IF;
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'updatePasswordEntidadCero') THEN
		-- Inicio release23.sql

		INSERT INTO query_catalog(id, query, acl, idperm) VALUES('updatePasswordEntidadCero','UPDATE iuseruserhdr set password = ?, fecha_proxima_modificacion = ? where name = ?',1,9205);
		insert into query_catalog (id,query,acl,idperm) values('getEntidadesSortedByIdEntidadNotAssigned','select entidad_supramunicipal.id_entidad, entidad_supramunicipal.nombreoficial, entidad_supramunicipal.srid,  entidad_supramunicipal.backup, entidad_supramunicipal.aviso, entidad_supramunicipal.periodicidad, entidad_supramunicipal.intentos, entidades_municipios.*,municipios.nombreoficial as nombremunicipio from entidad_supramunicipal left join entidades_municipios on entidad_supramunicipal.id_entidad=entidades_municipios.id_entidad left join municipios on entidades_municipios.id_municipio=municipios.id order by entidad_supramunicipal.nombreoficial',1,9025);

		insert into version (id_version, fecha_version) values('MODELO R23', DATE '2012-06-05');	

		-- Inicio release24.sql

		insert into version (id_version, fecha_version) values('MODELO R24', DATE '2012-06-13');

		-- Fin release23.sql	
	END IF;
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO9";



-- Fin release24.sql

-- Inicio release25.sql

--- solucion problemas ocupaciones
delete from columns_domains where id_column = 5503;  -- tenia el 121 "Texto de 255 caracteres"
delete from columns_domains where id_column = 5502;  -- tenia el 121 "Texto de 255 caracteres"
delete from columns_domains where id_column = 5511;  -- tenia el 121 "Texto de 255 caracteres"
update columns_domains set id_domain = 10067  where id_column = 5501; -- tenia el 121 "Texto de 255 caracteres" ahora se asigna el 10067 "Autonumerico incremental"


update queries set updatequery = 'UPDATE DistritosCensales SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),ID=?2,Nombre=?3,CodigoINE=?4,ID_Municipio=?M,Area=area2d(GeometryFromText(text(?1),?S)),Length=perimeter(GeometryFromText(text(?1),?S)) WHERE ID=?2' where id = 9;
update queries set selectquery = 'SELECT Parcelas.id, Parcelas.referencia_catastral, Parcelas.id_municipio, Parcelas.primer_numero, Parcelas.primera_letra, Parcelas.segundo_numero, Parcelas.segunda_letra, Parcelas.kilometro, Parcelas.bloque, Parcelas.direccion_no_estructurada, Parcelas.codigo_postal, Parcelas.fecha_alta, Parcelas.fecha_baja, transform(Parcelas."GEOMETRY", ?T) as "GEOMETRY", Parcelas.codigodelegacionmeh, Parcelas.nombreentidadmenor, Parcelas.id_distrito, Parcelas.codigozonaconcentracion, Parcelas.codigopoligono, Parcelas.codigoparcela, Parcelas.nombreparaje, Parcelas.id_via, Parcelas.area, Parcelas.length, Parcelas.anno_expediente, Parcelas.referencia_expediente, Parcelas.codigo_entidad_colaboradora, Parcelas.tipo_movimiento, Parcelas.codigo_municipiodgc, Parcelas.bice, Parcelas.codigo_provinciaine, Parcelas.codigo_municipio_localizaciondgc, Parcelas.codigo_municipio_localizacionine, Parcelas.codigo_municipio_origendgc, Parcelas.codigo_paraje, Parcelas.superficie_finca, Parcelas.superficie_construida_total, Parcelas.superficie_const_sobrerasante, Parcelas.superficie_const_bajorasante, Parcelas.superficie_cubierta, Parcelas.anio_aprobacion, Parcelas.codigo_calculo_valor, Parcelas.poligono_catastral_valoracion, Parcelas.modalidad_reparto, Parcelas.indicador_infraedificabilidad, Parcelas.movimiento_baja, Parcelas.coordenada_x, Parcelas.coordenada_y, Parcelas.modificado, Municipios.NombreOficial FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) WHERE Municipios.ID in (?M) AND Fecha_baja IS NULL AND SUBSTRING(referencia_catastral,6,7) <= ''9''' where id = 104;

update queries set selectquery = 'SELECT Parcelas.id, Parcelas.referencia_catastral, Parcelas.id_municipio, Parcelas.primer_numero, Parcelas.primera_letra, Parcelas.segundo_numero, Parcelas.segunda_letra, Parcelas.kilometro, Parcelas.bloque, Parcelas.direccion_no_estructurada, Parcelas.codigo_postal, Parcelas.fecha_alta, Parcelas.fecha_baja, transform(Parcelas."GEOMETRY", ?T) as "GEOMETRY", Parcelas.codigodelegacionmeh, Parcelas.nombreentidadmenor, Parcelas.id_distrito, Parcelas.codigozonaconcentracion, Parcelas.codigopoligono, Parcelas.codigoparcela, Parcelas.nombreparaje, Parcelas.id_via, Parcelas.area, Parcelas.length, Parcelas.anno_expediente, Parcelas.referencia_expediente, Parcelas.codigo_entidad_colaboradora, Parcelas.tipo_movimiento, Parcelas.codigo_municipiodgc, Parcelas.bice, Parcelas.codigo_provinciaine, Parcelas.codigo_municipio_localizaciondgc, Parcelas.codigo_municipio_localizacionine, Parcelas.codigo_municipio_origendgc, Parcelas.codigo_paraje, Parcelas.superficie_finca, Parcelas.superficie_construida_total, Parcelas.superficie_const_sobrerasante, Parcelas.superficie_const_bajorasante, Parcelas.superficie_cubierta, Parcelas.anio_aprobacion, Parcelas.codigo_calculo_valor, Parcelas.poligono_catastral_valoracion, Parcelas.modalidad_reparto, Parcelas.indicador_infraedificabilidad, Parcelas.movimiento_baja, Parcelas.coordenada_x, Parcelas.coordenada_y, Parcelas.modificado, Municipios.NombreOficial FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) WHERE Municipios.ID in (?M) AND Fecha_baja IS NULL AND SUBSTRING(referencia_catastral,6,7) > ''9''' where id = 105;



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM version WHERE id_version = 'MODELO R25') THEN
		insert into version (id_version, fecha_version) values('MODELO R25', DATE '2012-06-27');
	END IF;
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO10";



-- Fin release26.sql

-- Inicio release27.sql
DROP TABLE IF EXISTS historico_amortizacion;
CREATE TABLE historico_amortizacion
(
  id_bien integer NOT NULL,
  anio numeric(4,0) NOT NULL,
  total_amortizado_porcentaje numeric(10,2),
  id_cuenta_contable numeric(8,0),
  id_cuenta_amortizacion numeric(8,0),
  total_amortizado_anio numeric(10,2),
  CONSTRAINT historico_amortizacion_pkey PRIMARY KEY (id_bien , anio ),
  CONSTRAINT fk_amortizacion_pk FOREIGN KEY (id_cuenta_amortizacion)
      REFERENCES amortizacion (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_contable_pk FOREIGN KEY (id_cuenta_contable)
      REFERENCES contable (id_clasificacion) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE historico_amortizacion
  OWNER TO geopista;
  
  
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM version WHERE id_version = 'MODELO R27') THEN
		insert into version (id_version, fecha_version) values('MODELO R27', DATE '2012-07-06');
	END IF;
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO11";


-- Fin release27.sql

-- Inicio release28.sql

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'LMobtenertablaPorNombre') THEN
		insert into query_catalog (id,query,acl,idperm) 
			values('LMobtenertablaPorNombre','select  pgt.tablename, t.* from pg_tables pgt left join tables t on pgt.tablename=t.name where schemaname=''public'' and t.name = ?',1,9205);
			insert into version (id_version, fecha_version) values('MODELO R28', DATE '2012-07-11');

		-- Fin release28.sql
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO12";


-- Inicio release29.sql

update query_catalog set query='select * from tables order by ascii(name)' where id='LMobtenertablas';

DELETE FROM version where id_version='MODELO R29';
insert into version (id_version, fecha_version) values('MODELO R29', DATE '2012-07-26');

-- Fin release29.sql

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'LMexisteDiccionario') THEN
		-- Inicio release30.sql

		insert into query_catalog (id,query,acl,idperm) 
		values('LMexisteDiccionario','select Coalesce(count(*), 0) from dictionary where id_vocablo=? and locale=?',1,9205);

		DELETE FROM version where id_version='MODELO R30';
		insert into version (id_version, fecha_version) values('MODELO R30', DATE '2012-08-20');

		-- Fin release30.sql
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO13";



-- Inicio release31.sql
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM version WHERE id_version = 'MODELO R31') THEN
		insert into version (id_version, fecha_version) values('MODELO R31', DATE '2012-09-18');
	END IF;
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO14";


-- Fin release31.sql

-- Inicio release32.sql

update columns set id_domain = columns_domains.id_domain from columns_domains where columns.id = columns_domains.id_column and columns.id_domain is null;

update query_catalog set query = 'update columns_domains set id_domain=?, level=? where id_domain=? and id_column=?; update columns set id_domain=? where id = ?' where id = 'LMactualizardominiocolumna';
update query_catalog set query = 'insert into columns_domains (id_domain, id_column, level) values (?,?,?); update columns set id_domain=? where id = ?' where id = 'LMinsertardominiocolumna';
update query_catalog set query = 'delete from columns_domains where id_column=? and id_domain=?; update columns set id_domain=null where id = ?' where id = 'LMdesasociardominiocolumna';

update columns set id_domain = columns_domains.id_domain from columns_domains where columns.id = columns_domains.id_column and columns.id_domain != columns_domains.id_domain;

delete from version where id_version='MODELO R32';
insert into version (id_version, fecha_version) values('MODELO R32', DATE '2012-10-24');

-- Fin release32.sql

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'LMcomprobarlayerfamilies') THEN
		-- Inicio release33.sql

		insert into query_catalog (id,query,acl,idperm) 
		values('LMcomprobarlayerfamilies','select Coalesce(count(*), 0)  from layerfamilies, dictionary where layerfamilies.id_name = dictionary.id_vocablo and (locale =? or locale =''es_ES'') and dictionary.traduccion=?',1,9205);
	END IF;
	
	-- INICIO ETRS89
	update spatial_ref_sys set proj4text = '+proj=utm +zone=29 +ellps=GRS80 +units=m +no_defs +nadgrids=null' where srid =25829;
	update spatial_ref_sys set proj4text = '+proj=utm +zone=30 +ellps=GRS80 +units=m +no_defs +nadgrids=null' where srid =25830;
	update spatial_ref_sys set proj4text = '+proj=utm +zone=29 +ellps=intl +units=m +no_defs +nadgrids=PENR2009.gsb' where srid = 23029;
	update spatial_ref_sys set proj4text = '+proj=utm +zone=30 +ellps=intl +units=m +no_defs +nadgrids=PENR2009.gsb' where srid = 23030;
	update spatial_ref_sys set proj4text = '+proj=utm +zone=31 +ellps=intl +units=m +no_defs +nadgrids=PENR2009.gsb' where srid = 23031;
	update spatial_ref_sys set proj4text = '+proj=longlat +ellps=intl +units=m +nadgrids=PENR2009.gsb +no_defs' where srid = 4230;
	update spatial_ref_sys set proj4text = '+proj=longlat +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +no_defs' where srid = 4258;
	-- Habría que copiar el fichero PENR2009.gsb (http://www.01.ign.es/ign/layoutIn/herramientas.do) en el directorio del proj (/usr/share/proj/)
	-- FIN ETRS89

	-- Modificaciones en las capas por problemas con la exportacion/importacion de autocad/microstation
	-- Estoy hay que hacerlo a posteriori porque estas tablas todavía no estarían creadas
	--UPDATE columns SET "Type" = 3 WHERE id_table = (SELECT id_table FROM tables where name = 'eiel_c_parroquias') and name = 'nombre_parroquia';
	--update columns set "Length"=6, "Precision"=6, "Scale"=1  where id_table=(select id_table from tables where name = 'eiel_c_alum_pl') and name='potencia';


	IF NOT EXISTS (SELECT * FROM version WHERE id_version = 'MODELO R33') THEN
		insert into version (id_version, fecha_version) values('MODELO R33', DATE '2012-11-09');
	END IF;


	-- Fin release33.sql

END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO15";





-- Inicio release34.sql

ALTER TABLE "public"."representante" ALTER COLUMN "puerta" TYPE varchar(3);
ALTER TABLE "public"."representante" ALTER COLUMN "planta" TYPE varchar(3);

delete from version where id_version='MODELO R34';
insert into version (id_version, fecha_version) values('MODELO R34', DATE '2012-11-28');

-- Fin release34.sql

-- Inicio release35.sql
delete from version where id_version='MODELO R35';
insert into version (id_version, fecha_version) values('MODELO R35', DATE '2012-12-26');

-- Fin release35.sql

-- Inicio release36.sql

update tables set geometrytype = 1 where name = 'incidencias';
--update tables set geometrytype = 11 where name = 'eiel_pmr_accesibilidad_edificios_publicos';
delete from version where id_version = 'MODELO R36';
insert into version (id_version, fecha_version) values('MODELO R36', DATE '2013-01-10');

-- Fin release36.sql

-- Inicio CATASTRO:
-- Inicio Modificaciones_MODELO.sql
select f_add_col('public.expediente','modo_acoplado', 'ALTER TABLE expediente ADD modo_acoplado BOOLEAN DEFAULT FALSE;');

-- Fin Modificaciones_MODELO.sql

-- Inicio ModificacionesCatastro.sql

insert into dictionary (id_vocablo, locale, traduccion) values (nextval('seq_dictionary'),'es_ES','S6');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'va_ES','S6');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'eu_ES','S6');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'gl_ES','S6');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'ca_ES','S6');
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain) values (nextval('seq_domainnodes'), 10080, 'S6', currval('seq_dictionary'), 7, 1022);

insert into dictionary (id_vocablo, locale, traduccion) values (nextval('seq_dictionary'),'es_ES','S7');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'va_ES','S7');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'eu_ES','S7');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'gl_ES','S7');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'ca_ES','S7');
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain) values (nextval('seq_domainnodes'), 10080, 'S7', currval('seq_dictionary'), 7, 1022);

--Correcciones del modelo de datos



-- Create a temporary table

DROP TABLE IF EXISTS representante0xpfue;
CREATE LOCAL TEMPORARY TABLE "representante0xpfue" (
  "anno_expediente" NUMERIC(4,0), 
  "referencia_expediente" VARCHAR(13), 
  "codigo_entidad_colaboradora" NUMERIC(3,0), 
  "id_bieninmueble" VARCHAR(20), 
  "nifrepresentante" VARCHAR(9), 
  "razonsocial_representante" VARCHAR(60), 
  "codigo_provincia_ine" NUMERIC(2,0), 
  "codigo_municipio_dgc" NUMERIC(3,0), 
  "codigo_municipio_ine" NUMERIC(3,0), 
  "entidad_menor" VARCHAR(30), 
  "id_via" NUMERIC(8,0), 
  "primer_numero" NUMERIC(4,0), 
  "primera_letra" VARCHAR(1), 
  "segundo_numero" NUMERIC(4,0), 
  "kilometro" NUMERIC(5,2), 
  "segunda_letra" VARCHAR(1), 
  "bloque" VARCHAR(4), 
  "escalera" VARCHAR(2), 
  "planta" VARCHAR(2), 
  "puerta" VARCHAR(2), 
  "direccion_no_estructurada" VARCHAR(25), 
  "codigo_postal" VARCHAR(5), 
  "apartado_correos" NUMERIC(5,0)
) WITH OIDS;

-- Copy the source table's data to the temporary table

INSERT INTO "representante0xpfue" ("anno_expediente", "referencia_expediente", "codigo_entidad_colaboradora", "id_bieninmueble", "nifrepresentante", "razonsocial_representante", "codigo_provincia_ine", "codigo_municipio_dgc", "codigo_municipio_ine", "entidad_menor", "id_via", "primer_numero", "primera_letra", "segundo_numero", "kilometro", "segunda_letra", "bloque", "escalera", "planta", "puerta", "direccion_no_estructurada", "codigo_postal", "apartado_correos")
SELECT "anno_expediente", "referencia_expediente", "codigo_entidad_colaboradora", "id_bieninmueble", "nifrepresentante", "razonsocial_representante", "codigo_provincia_ine", "codigo_municipio_dgc", "codigo_municipio_ine", "entidad_menor", "id_via", "primer_numero", "primera_letra", "segundo_numero", "kilometro", "segunda_letra", "bloque", "escalera", "planta", "puerta", "direccion_no_estructurada", "codigo_postal", "apartado_correos" FROM "public"."representante";

-- Drop the source table

DROP TABLE IF EXISTS "public"."representante";

-- Create the destination table

CREATE TABLE "public"."representante" (
  "anno_expediente" NUMERIC(4,0), 
  "referencia_expediente" VARCHAR(13), 
  "codigo_entidad_colaboradora" NUMERIC(3,0), 
  "id_bieninmueble" VARCHAR(20) NOT NULL, 
  "nifrepresentante" VARCHAR(9) NOT NULL, 
  "razonsocial_representante" VARCHAR(60), 
  "codigo_provincia_ine" NUMERIC(2,0), 
  "codigo_municipio_dgc" NUMERIC(3,0), 
  "codigo_municipio_ine" NUMERIC(3,0), 
  "entidad_menor" VARCHAR(30), 
  "id_via" NUMERIC(8,0), 
  "primer_numero" NUMERIC(4,0), 
  "primera_letra" VARCHAR(1), 
  "segundo_numero" NUMERIC(4,0), 
  "kilometro" NUMERIC(5,2), 
  "segunda_letra" VARCHAR(1), 
  "bloque" VARCHAR(4), 
  "escalera" VARCHAR(2), 
  "planta" VARCHAR(3), 
  "puerta" VARCHAR(3),
  "direccion_no_estructurada" VARCHAR(25), 
  "codigo_postal" VARCHAR(5), 
  "apartado_correos" NUMERIC(5,0), 
  CONSTRAINT "representante_pkey" PRIMARY KEY("id_bieninmueble", "nifrepresentante")
) WITH OIDS;


-- Copy the temporary table's data to the destination table

INSERT INTO "public"."representante" ("anno_expediente", "referencia_expediente", "codigo_entidad_colaboradora", "id_bieninmueble", "nifrepresentante", "razonsocial_representante", "codigo_provincia_ine", "codigo_municipio_dgc", "codigo_municipio_ine", "entidad_menor", "id_via", "primer_numero", "primera_letra", "segundo_numero", "kilometro", "segunda_letra", "bloque", "escalera", "planta", "puerta", "direccion_no_estructurada", "codigo_postal", "apartado_correos")
SELECT "anno_expediente", "referencia_expediente", "codigo_entidad_colaboradora", "id_bieninmueble", "nifrepresentante", "razonsocial_representante", "codigo_provincia_ine", "codigo_municipio_dgc", "codigo_municipio_ine", "entidad_menor", "id_via", "primer_numero", "primera_letra", "segundo_numero", "kilometro", "segunda_letra", "bloque", "escalera", CAST("planta" AS varchar(3)), "puerta", "direccion_no_estructurada", "codigo_postal", "apartado_correos" FROM "representante0xpfue";



-- Modificaciones en el modelo de datos de la tabla vias, se modifica la longitud del tipodevianormalizadocatastro de 2 a 5
ALTER TABLE vias ALTER tipovianormalizadocatastro TYPE varchar(5);


update municipios set id_catastro=municipios.id_ine;
update municipios set id_catastro=900 where id='16078';
update query_catalog set query='select distinct substring(bien_inmueble.numero_fincaregistral,3,3) as numero_fincaregistral from bien_inmueble inner join municipios on (bien_inmueble.codigo_municipiodgc=municipios.id_catastro and numero_fincaregistral is not null) where municipios.id_provincia=?' where id='MCobtenerListaCodigoRegistro';

--Elimina las limitaciones de rutas al generar el fin de entrada y el varpad
ALTER TABLE ficheros ALTER nombre TYPE varchar(200);
ALTER TABLE ficheros ALTER url TYPE varchar(250);

-- Correcciones en las validaciones

--INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCobtenerDuplicadosConstruccion','select numero_orden_construccion from construccion where numero_orden_construccion=? and parcela_catastral=?,1,9025);

--INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCexisteDuplicadosUC','select codigo_unidad_constructiva from unidad_constructiva where codigo_unidad_constructiva=? and parcela_catastral=?',1,9025);

-- Sentencias de Miriam revisar y corregir

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM query_catalog WHERE id = 'MCgetCodPoligono') THEN

		INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCgetCodPoligono','select * from ponencia_poligono where codigo_poligono=?',1,9025);

		INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCgetCoefCoordPlan','select coef_coordplan from ponencia_poligono where codigo_poligono=?',1,9025);

		INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCbuscaridvia','select id from vias where tipovianormalizadocatastro=? and nombrecatastro=? and codigocatastro=?',1,9025);

		INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCobtenerCodViaTramo','select codigo_via from ponencia_tramos where codigo_tramo=?',1,9025);

		INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCcompruebaCodVia','select codigo_via from ponencia_tramos where codigo_via=?',1,9025);

	END IF;

	-- Fin release33.sql

END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO16";

UPDATE QUERY_CATALOG SET query='select id, tipovianormalizadocatastro,nombrecatastro,codigocatastro from vias where id_municipio=? and upper(nombrecatastro) like upper(?)', acl=1,idperm=9025 WHERE id='MCobtenerViasCatastro';

UPDATE QUERY_CATALOG SET query='select id, tipovianormalizadocatastro,nombrecatastro,codigocatastro from vias where id_municipio=? and upper(nombrecatastro) like upper(?) and tipovianormalizadocatastro=?',acl=1,idperm=9025 WHERE id='MCobtenerViasPorTipoCatastro';
-------------------------------------

-- Modificar esta sentencia en base de datos
update query_catalog set query='select * from conservacion where aanormas=?', acl=1, idperm=9025 where id='MCobtenerCoefCorrectorEstadoConservacion';

update query_catalog set query='select anno_normas from cntponurb where anno_aprobacion=? and id_municipio=?', acl=1, idperm=9025 where id='MCobtenerAnnoDeNormasPonencia';

update query_catalog set query='select * from parcelas where codigopoligono=?', acl=1, idperm=9025 where id='MCgetCodigoPoligono';


-- Create a temporary table

DROP TABLE IF EXISTS ruevaluatorio0vetur;
CREATE LOCAL TEMPORARY TABLE "ruevaluatorio0vetur" (
  "id_municipio" NUMERIC(5,0), 
  "codigo_delegacionmeh" VARCHAR(2), 
  "codigo_municipio_meh" VARCHAR(3), 
  "codigo_municipio_agregado" VARCHAR(3), 
  "cc" VARCHAR(2), 
  "ip" VARCHAR(2), 
  "tipo" NUMERIC(12,6), 
  "aatipo" NUMERIC(4,0), 
  "ptsconc1" NUMERIC(12,6), 
  "ejeconc1" VARCHAR(1), 
  "ptsconc2" NUMERIC(12,6), 
  "ejeconc2" NUMERIC(4,0), 
  "ptsconc3" NUMERIC(12,6), 
  "ejeconc3" NUMERIC(4,0), 
  "j_teoricas" NUMERIC(8,2), 
  "exento" VARCHAR(1), 
  "condonado" VARCHAR(1), 
  "condonado_jt" VARCHAR(1), 
  "vuelo" VARCHAR(1), 
  "usuario" VARCHAR(12), 
  "modificacion" DATE, 
  "desde" DATE, 
  "tipo_movimiento" VARCHAR(1), 
  "motivo_movimiento" VARCHAR(4), 
  "estado" VARCHAR(1), 
  "ipp" NUMERIC(2,0), 
  "tipo_ant" NUMERIC(12,6)
) WITH OIDS;

-- Copy the source table's data to the temporary table

INSERT INTO "ruevaluatorio0vetur" ("id_municipio", "codigo_delegacionmeh", "codigo_municipio_meh", "codigo_municipio_agregado", "cc", "ip", "tipo", "aatipo", "ptsconc1", "ejeconc1", "ptsconc2", "ejeconc2", "ptsconc3", "ejeconc3", "j_teoricas", "exento", "condonado", "condonado_jt", "vuelo", "usuario", "modificacion", "desde", "tipo_movimiento", "motivo_movimiento", "estado", "ipp", "tipo_ant")
SELECT "id_municipio", "codigo_delegacionmeh", "codigo_municipio_meh", "codigo_municipio_agregado", "cc", "ip", "tipo", "aatipo", "ptsconc1", "ejeconc1", "ptsconc2", "ejeconc2", "ptsconc3", "ejeconc3", "j_teoricas", "exento", "condonado", "condonado_jt", "vuelo", "usuario", "modificacion", "desde", "tipo_movimiento", "motivo_movimiento", "estado", "ipp", "tipo_ant" FROM "public"."ruevaluatorio";

-- Drop the source table

DROP TABLE IF EXISTS "public"."ruevaluatorio";

-- Create the destination table

CREATE TABLE "public"."ruevaluatorio" (
  "id_municipio" NUMERIC(5,0) NOT NULL, 
  "codigo_delegacionmeh" VARCHAR(2), 
  "codigo_municipio_meh" VARCHAR(3), 
  "codigo_municipio_agregado" VARCHAR(3), 
  "cc" VARCHAR(2), 
  "ip" NUMERIC(2,0), 
  "tipo" NUMERIC(12,6), 
  "aatipo" NUMERIC(4,0), 
  "ptsconc1" NUMERIC(12,6), 
  "ejeconc1" VARCHAR(1), 
  "ptsconc2" NUMERIC(12,6), 
  "ejeconc2" NUMERIC(4,0), 
  "ptsconc3" NUMERIC(12,6), 
  "ejeconc3" NUMERIC(4,0), 
  "j_teoricas" NUMERIC(8,2), 
  "exento" VARCHAR(1), 
  "condonado" VARCHAR(1), 
  "condonado_jt" VARCHAR(1), 
  "vuelo" VARCHAR(1), 
  "usuario" VARCHAR(12), 
  "modificacion" DATE, 
  "desde" DATE, 
  "tipo_movimiento" VARCHAR(1), 
  "motivo_movimiento" VARCHAR(4), 
  "estado" VARCHAR(1), 
  "ipp" NUMERIC(2,0), 
  "tipo_ant" NUMERIC(12,6)
) WITH OIDS;


-- Copy the temporary table's data to the destination table

INSERT INTO "public"."ruevaluatorio" ("id_municipio", "codigo_delegacionmeh", "codigo_municipio_meh", "codigo_municipio_agregado", "cc", "ip", "tipo", "aatipo", "ptsconc1", "ejeconc1", "ptsconc2", "ejeconc2", "ptsconc3", "ejeconc3", "j_teoricas", "exento", "condonado", "condonado_jt", "vuelo", "usuario", "modificacion", "desde", "tipo_movimiento", "motivo_movimiento", "estado", "ipp", "tipo_ant")
SELECT "id_municipio", "codigo_delegacionmeh", "codigo_municipio_meh", "codigo_municipio_agregado", "cc", CAST(TRIM("ip") AS numeric(2,0)), "tipo", "aatipo", "ptsconc1", "ejeconc1", "ptsconc2", "ejeconc2", "ptsconc3", "ejeconc3", "j_teoricas", "exento", "condonado", "condonado_jt", "vuelo", "usuario", "modificacion", "desde", "tipo_movimiento", "motivo_movimiento", "estado", "ipp", "tipo_ant" FROM "ruevaluatorio0vetur";

-- Modificacion de obtención de ruevaluatorio

update query_catalog set query='select id_municipio from ruevaluatorio where codigo_delegacionmeh=? AND codigo_municipio_meh=? AND cc=? AND ipp=?', acl=1,idperm=9025 where id ='MCgetTipoEvaluatorio'; 

select f_add_col('public.configuracion','modo_generacion', 'ALTER TABLE configuracion ADD COLUMN modo_generacion varchar(1);   ');


update query_catalog set query='select codigo_tramo, denominacion from Ponencia_Tramos  order by codigo_tramo asc' where id='MCobtenerCodigoTramo';
update query_catalog set query='select distinct codigo_urbanistica, denominacion from Ponencia_Tramos where codigo_urbanistica is not null order by codigo_urbanistica asc' where id='MCobtenerZonaUrbanistica';
update query_catalog set query='select distinct codigo_zona, denominacion from ponencia_urbanistica where codigo_zona is not null order by codigo_zona asc' where id='MCobtenerZonaUrbanistica';

update query_catalog set query='select vias.id, vias.tipovianormalizadocatastro,vias.nombrecatastro,vias.codigocatastro, municipios.nombreoficial from vias left join municipios on vias.id_municipio=municipios.id where upper(nombrecatastro) like upper(?) and tipovianormalizadocatastro=?' where id='MCobtenerViasPorTipoCatastro';

update query_catalog set query='select vias.id, vias.tipovianormalizadocatastro, vias.nombrecatastro, vias.codigocatastro, municipios.nombreoficial from vias left join municipios on vias.id_municipio=municipios.id where id_municipio=''?'' and upper(nombrecatastro) like upper(?) and tipovianormalizadocatastro=?' where id=' MCobtenerViasPorTipoCatastroMunic';

update query_catalog set query='select vias.id, vias.tipoviaine, vias.nombrecatastro, municipios.nombreoficial from vias left join municipios on vias.id_municipio=municipios.id where id_municipio=? and upper(nombrecatastro) like upper(?)' where id='MCobtenerViasINE';
--update query_catalog set query='select vias.id, vias.tipovianormalizadocatastro,  vias.nombrecatastro, vias.codigocatastro, municipios.nombreoficial from vias left join municipios on vias.id_municipio=municipios.id where id_municipio=? and upper(nombrecatastro) like upper(?)' where id='MCobtenerViasINE';

update query_catalog set query='select vias.id, vias.tipovianormalizadocatastro, vias.nombrecatastro, vias.codigocatastro, municipios.nombreoficial from vias left join municipios on vias.id_municipio=municipios.id where upper(nombrecatastro) like upper(?)' where id='MCobtenerViasCatastro';


delete from query_catalog where id='MCobtenerViasPorMunicipio';
insert into query_catalog (id,query) values ('MCobtenerViasPorMunicipio', 'select vias.id, vias.tipovianormalizadocatastro, vias.nombrecatastro, vias.codigocatastro, municipios.nombreoficial from vias left join municipios on vias.id_municipio=municipios.id where upper(nombrecatastro) like upper(?) and id_municipio=?');

-- Modificaiones del dominio tipos de expdiente
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM tipo_expediente WHERE codigo_tipo_expediente = 'CCCP') THEN
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (16,'CCCP','Comunicación concentración parcelaria','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (17,'CCDL','Comunicación deslindes','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (18,'CCEX','Comunicación expropiaciones','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (19,'CCGU','Comunicación actuación gestión urbanística','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (20,'SITC','Solicitud de incorporación de titulares','901');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (21,'SDTG','Solicitud de división de trasteros y garajes','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (22,'SBAJ','Solicitud de baja como titular','901');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (23,'SIDF','Solicitud de incorporación de derechos de disfrute','901');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (24,'SCRP','Solicitud de cambio de representante','901');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (25,'COEF','Corrección de errores materiales físico económicos y posiblemente jurídicos','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (26,'COEJ','Corrección de errores materiales jurídicos','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (27,'DICF','Subsanación de discrepancias físico económicas y posiblemente jurídicas','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (28,'DICJ','Subsanación de discrepancias jurídicas','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (29,'CBIC','Suministro de la información relativa a los bienes inmuebles de características especiales','901');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (30,'INSP','Inspección conjunta','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (31,'REVU','Apoyo técnico en procedimientos de valoración colectiva.','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (32,'PVCR','Apoyo técnico en procedimientos de valoración de construcciones rústicas','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (33,'RENR','Apoyo técnico en procedimientos de renovación del catastro rústico','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (34,'REVB','Apoyo técnico en procedimientos de valoración colectiva especiales (BICE)','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (35,'ALCN','Alteraciones por cambio de naturaleza','902');
		insert into tipo_expediente (id_tipo_expediente, codigo_tipo_expediente, descripcion,convenio) values (36,'ALCA','Alteraciones por cambio de uso o aprovechamiento','902');
	END IF;

	-- Fin release33.sql

END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO17";



-- Ordenación de los tipos de uso
update query_catalog set query='select id, patron, denominacion from tipo_destino order by patron' where id='MCobtenerTipoDestino';


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM tipo_destino WHERE patron = 'PTO') THEN
		-- Nuevos valores del dominio destino de locales
		insert into tipo_destino (id, patron,denominacion) values (313, 'PTO', 'Patio');
		insert into tipo_destino (id, patron,denominacion) values (314, 'TZA', 'Terraza');
		insert into tipo_destino (id, patron,denominacion) values (315, 'SOP', 'Soportal');

	END IF;

	-- Fin release33.sql

END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO18";

-- Fin ModificacionesCatastro.sql

-- Inicio Modificaciones_MODELO_OVC.sql

-- Se modifca el tamaño del camo id_dialogo de la tabla expediente_finca_catastro
ALTER TABLE expediente_finca_catastro DROP id_dialogo;
ALTER TABLE expediente_finca_catastro ADD id_dialogo character varying(64);

-- Se añade un nuevo campo en la tabla expediente_finca_catastro para controlar que la finca no esta actualizada
select f_add_col('public.expediente_finca_catastro','actualizado', 'ALTER TABLE expediente_finca_catastro ADD COLUMN actualizado boolean DEFAULT false;');

-- Se modifca el tamaño del camo id_dialogo de la tabla expediente_bieninmueble
ALTER TABLE expediente_bieninmueble DROP id_dialogo;
ALTER TABLE expediente_bieninmueble ADD id_dialogo character varying(64);

-- Se añade un nuevo campo en la tabla expediente_bieninmueble para controlar que el bien no esta actualzado
select f_add_col('public.expediente_bieninmueble','actualizado', 'ALTER TABLE expediente_bieninmueble ADD COLUMN actualizado boolean DEFAULT false;');


-- Modificacion de los campos valor_catastral, valor_catastral_suelo, valor_catastral_construccion
-- para aceptar valores con decimales.
ALTER TABLE bien_inmueble ALTER COLUMN valor_catastral TYPE NUMERIC(12,2);
ALTER TABLE bien_inmueble ALTER COLUMN valor_catastral_suelo TYPE NUMERIC(12,2);
ALTER TABLE bien_inmueble ALTER COLUMN valor_catastral_construccion TYPE NUMERIC(12,2);


update query_catalog set query='select vias.id, vias.tipovianormalizadocatastro,vias.nombrecatastro,vias.codigocatastro, municipios.nombreoficial, vias.codigoine, vias.id_municipio from vias left join municipios on vias.id_municipio=municipios.id where upper(nombrecatastro) like upper(?) and tipovianormalizadocatastro=? AND vias.id_municipio = ?' where id='MCobtenerViasPorTipoCatastro';
update query_catalog set query='select vias.id, vias.tipovianormalizadocatastro, vias.nombrecatastro, vias.codigocatastro, municipios.nombreoficial, vias.codigoine, vias.id_municipio from vias left join municipios on vias.id_municipio=municipios.id where upper(nombrecatastro) like upper(?) AND vias.id_municipio = ?' where id='MCobtenerViasCatastro';

update query_catalog set query='select tipo_ponencia,anno_normas,anno_efectostotal,propiedad_vertical1,antiguedad_infraedificada,estado_ponencia,finca_infraedificada,anno_cuadromarco,aplicacion_formula,suelo_sindesarrollar,ruinosa,fecha_cierrerevision from cntponurb where anno_aprobacion=?' where id='MCgetDatosPonencia';

-- Fin Modificaciones_MODELO_OVC.sql
-- Fin CATASTRO

-- Inicio ESPACIO PUBLICO:
-- Fin modelo.sql

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM civil_work_warnings_types WHERE description = 'Planes de Obra y Servicios') THEN
		INSERT INTO civil_work_warnings_types(id_type, description) VALUES (3, 'Planes de Obra y Servicios');
	END IF;

	-- Fin release33.sql

END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO19";


DROP TABLE IF EXISTS civil_work_planes_envio;
CREATE TABLE civil_work_planes_envio(
  id_warning integer NOT NULL,
  plan character varying(2),
  anios character varying(50),
  nombre character varying(100),
  paraje character varying(100),
  presupuesto_estimado numeric(15,2),
  presupuesto_definitivo numeric(15,2),
  existe_proyecto character varying(2),
  infraestructura character varying(2),
  obra_nueva character varying(2),
  estudio_ambiental character varying(2),
  datos_eiel character varying(1),
  permisos character varying(1),
  persona_contacto character varying(100),
  telefono_contacto character varying(20),
  fecha_aprobacion date,
  CONSTRAINT civil_work_planes_envio_pkey PRIMARY KEY (id_warning ),
  CONSTRAINT civil_work_planes_envio_id_warning_fk FOREIGN KEY (id_warning)
      REFERENCES civil_work_warnings (id_warning) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE civil_work_planes_envio  OWNER TO postgres;
GRANT ALL ON TABLE civil_work_planes_envio TO postgres;
GRANT SELECT ON TABLE civil_work_planes_envio TO consultas;

DROP TABLE IF EXISTS civil_work_planes_respuesta;
CREATE TABLE civil_work_planes_respuesta(
  id_warning integer NOT NULL,
  destinatario character varying(20),
  recibi character varying(2),
  fecha_recibi date,
  supervision character varying(2),
  director_proyecto character varying(100),
  autor_proyecto character varying(100),
  director_obra character varying(100),
  empresa_adjudicataria character varying(100),
  fecha_resolucion date,
  presupuesto_adjudicacion numeric(15,2),
  coordinador_seg_salud character varying(100),
  acta_replanteo date,
  fecha_comienzo date,
  fecha_finalizacion date,
  prorrogas date,
  acta_recepcion date,
  certificacion_final numeric(15,2),
  resolucion_certificacion date,
  informacionCambiosEIEL date,
  reformados date,
  liquidacion numeric(15,2),
  fecha_liquidacion date,
  detalles text,
  fecha_aprobacion_principado date,
  fecha_solicitud date,
  CONSTRAINT civil_work_planes_respuesta_pkey PRIMARY KEY (id_warning ),
  CONSTRAINT civil_work_planes_respuesta_id_warning_fk FOREIGN KEY (id_warning)
      REFERENCES civil_work_warnings (id_warning) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE civil_work_planes_respuesta  OWNER TO postgres;
GRANT ALL ON TABLE civil_work_planes_respuesta TO postgres;
GRANT SELECT ON TABLE civil_work_planes_respuesta TO consultas;

DROP TABLE IF EXISTS civil_work_planes_adjunta;
CREATE TABLE civil_work_planes_adjunta(
  id_warning integer NOT NULL,
  tipo character varying(20),
  CONSTRAINT civil_work_planes_adjunta_id_warning_fk FOREIGN KEY (id_warning)
      REFERENCES civil_work_warnings (id_warning) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=TRUE
);
ALTER TABLE civil_work_planes_adjunta  OWNER TO postgres;
GRANT ALL ON TABLE civil_work_planes_adjunta TO postgres;
GRANT SELECT ON TABLE civil_work_planes_adjunta TO consultas;

DROP TABLE IF EXISTS civil_work_planes_afectados;
CREATE TABLE civil_work_planes_afectados(
  id_warning integer NOT NULL,
  servicio character varying(100),
  CONSTRAINT civil_work_planes_afectados_id_warning_fk FOREIGN KEY (id_warning)
      REFERENCES civil_work_warnings (id_warning) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=TRUE
);
ALTER TABLE civil_work_planes_afectados  OWNER TO postgres;
GRANT ALL ON TABLE civil_work_planes_afectados TO postgres;
GRANT SELECT ON TABLE civil_work_planes_afectados TO consultas;

delete from query_catalog where id='GestionCiudadObtenerTraduccionDeDominioPlanesObra';
insert into query_catalog values ('GestionCiudadObtenerTraduccionDeDominioPlanesObra','select parentdescription from  (select d1.traduccion as parentdescription,dn.pattern as parentpattern,d2.traduccion as description,dn2.pattern as pattern from domainnodes dn left join domainnodes dn2 on dn.id = dn2.parentdomain left join dictionary d1 on d1.id_vocablo = dn.id_description and d1.locale = ? left join dictionary d2 on d2.id_vocablo = dn2.id_description and d2.locale = ? where dn.parentdomain = (SELECT id FROM DOMAINNODES,DICTIONARY WHERE DOMAINNODES.ID_DESCRIPTION = DICTIONARY.ID_VOCABLO AND DICTIONARY.LOCALE = ? and DICTIONARY.traduccion = ?)) d  where parentpattern=? group by parentdescription,parentpattern',1,9205);
delete from query_catalog where id='GestionCiudadObtenerTiposDeDominioPlanesObra';
insert into query_catalog values ('GestionCiudadObtenerTiposDeDominioPlanesObra','select id_node,id_description,parentdescription,parentpattern from  (select dn.id as id_node,dn.id_description as id_description,d1.traduccion as parentdescription,dn.pattern as parentpattern,d2.traduccion as description,dn2.pattern as pattern from domainnodes dn left join domainnodes dn2 on dn.id = dn2.parentdomain left join dictionary d1 on d1.id_vocablo = dn.id_description and d1.locale = ? left join dictionary d2 on d2.id_vocablo = dn2.id_description and d2.locale = ? where dn.parentdomain = (SELECT id FROM DOMAINNODES,DICTIONARY WHERE DOMAINNODES.ID_DESCRIPTION = DICTIONARY.ID_VOCABLO AND DICTIONARY.LOCALE =? and DICTIONARY.traduccion = ?)) d group by parentdescription,parentpattern,id_description,id_node',1,9205);

-- Dominio Destinatario Planes de Obra:

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM domains WHERE NAME = 'Destinatario Planes de Obra') THEN
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Destinatario Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Destinatario Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Destinatario Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Destinatario Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Destinatario Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Destinatario Planes de Obra');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Principado de Asturias');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Principado de Asturias');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Principado de Asturias');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Principado de Asturias');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Principado de Asturias');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'principado',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Gobierno General');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Gobierno General');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Gobierno General');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Gobierno General');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Gobierno General');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'gobierno',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);		

		-- Dominio Tipos de Planes de Obra:
					
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Tipos de Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Tipos de Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Tipos de Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Tipos de Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Tipos de Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Tipos de Planes de Obra');
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
		
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Obras y Servicios');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Obras y Servicios');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Obras y Servicios');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Obras y Servicios');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Obras y Servicios');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'1',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		-- Dominio Existe proyecto Planes de Obra:
					
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Existe proyecto Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Existe proyecto Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Existe proyecto Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Existe proyecto Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Existe proyecto Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Existe proyecto Planes de Obra');
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
		
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Si');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'SI',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','No');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'NO',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);
			
		-- Dominio Infraestructura Planes de Obra:
					
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Infraestructura Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Infraestructura Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Infraestructura Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Infraestructura Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Infraestructura Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Infraestructura Planes de Obra');
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
		
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Vial');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Vial');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Vial');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Vial');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Vial');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'1',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Abastecimiento');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Abastecimiento');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Abastecimiento');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Abastecimiento');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Abastecimiento');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'2',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Sanemamiento');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Sanemamiento');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Sanemamiento');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Sanemamiento');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Sanemamiento');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'3',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Alumbrado');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Alumbrado');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Alumbrado');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Alumbrado');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Alumbrado');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'4',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Otros');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Otros');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Otros');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Otros');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Otros');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'5',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);
																						
		-- Dominio Existe Estudio Planes de Obra:
					
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Existe Estudio Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Existe Estudio Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Existe Estudio Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Existe Estudio Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Existe Estudio Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Existe Estudio Planes de Obra');
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
		
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Si');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'SI',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','No');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'NO',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);
			
		-- Dominio Datos EIEL Planes de Obra:
					
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Datos EIEL Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Datos EIEL Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Datos EIEL Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Datos EIEL Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Datos EIEL Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Datos EIEL Planes de Obra');
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
		
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Buen estado');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Buen estado');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Buen estado');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Buen estado');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Buen estado');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'1',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Regular');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Regular');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Regular');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Regular');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Regular');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'2',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);
					
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Malo');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Malo');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Malo');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Malo');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Malo');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'3',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);
					
		-- Dominio Permisos Planes de Obra:
					
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Permisos Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Permisos Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Permisos Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Permisos Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Permisos Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Permisos Planes de Obra');
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
		
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Disponibles');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Disponibles');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Disponibles');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Disponibles');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Disponibles');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'1',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','No disponibles (en fase de tramitación)');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','No disponibles (en fase de tramitación)');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','No disponibles (en fase de tramitación)');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','No disponibles (en fase de tramitación)');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','No disponibles (en fase de tramitación)');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'2',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);
					
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','No disponibles (en proceso de expropiación)');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','No disponibles (en proceso de expropiación)');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','No disponibles (en proceso de expropiación)');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','No disponibles (en proceso de expropiación)');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','No disponibles (en proceso de expropiación)');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'3',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);
				
		-- Dominio Certificado ocupaciones Planes de Obra:
					
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Certificado ocupaciones Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Certificado ocupaciones Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Certificado ocupaciones Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Certificado ocupaciones Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Certificado ocupaciones Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Certificado ocupaciones Planes de Obra');
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
		
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Si');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'SI',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','No');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'NO',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);
			
		-- Dominio Supervision de proyectos Planes de Obra:
					
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Supervision de proyectos Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Supervision de proyectos Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Supervision de proyectos Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Supervision de proyectos Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Supervision de proyectos Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Supervision de proyectos Planes de Obra');
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
		
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Si');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'SI',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','No');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'NO',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);
							

		-- Dominio Obra nueva
					
		insert into domains (ID, NAME,idacl,ID_CATEGORY)values(nextval('seq_domains'),'Obra nueva Planes de Obra',(select idacl FROM acl WHERE name='"Gestion de Espacio Publico"'),5);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Obra nueva Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Obra nueva Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Obra nueva Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Obra nueva Planes de Obra');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Obra nueva Planes de Obra');
			insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
			values(nextval('seq_domainnodes'),currval('seq_domains'),null,currval('seq_dictionary'),4,null,null,null);			
		
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','Si');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','Si');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'SI',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);

		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(nextval('seq_dictionary'),'es_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ga_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'va_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'eu_ES','No');
		insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(currval('seq_dictionary'),'ca_ES','No');
		
		insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",permissionlevel, parentdomain,id_municipio)
					values(nextval('seq_domainnodes'),currval('seq_domains'),'NO',currval('seq_dictionary'),7,null,(select currval('seq_domainnodes') from seq_domainnodes),null);  				
					
		--Creo la capa de Planes de Obra y Servicios
		INSERT INTO styles (id_style,xml) VALUES (nextval('seq_styles'),'"<?xml version="1.0" encoding="ISO-8859-1"?>
		<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<NamedLayer><Name>planesobra</Name><UserStyle><Name>planesobra:_:default</Name><Title>planesobra:_:default</Title><Abstract>planesobra:_:default</Abstract><FeatureTypeStyle><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>1.7976931348623157E308</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name=''fill''>#cccc00</CssParameter><CssParameter name=''fill-opacity''>0.51</CssParameter></Fill><Stroke><CssParameter name=''stroke''>#000000</CssParameter><CssParameter name=''stroke-linecap''>round</CssParameter><CssParameter name=''stroke-linejoin''>round</CssParameter><CssParameter name=''stroke-opacity''>1.0</CssParameter><CssParameter name=''stroke-width''><ogc:Literal>1.0</ogc:Literal></CssParameter></Stroke></PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer>
		</StyledLayerDescriptor>
		"');

		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (nextval('seq_dictionary'),'es_ES','planesobra');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'ca_ES','[cat]planesobra');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'va_ES','[va]planesobra');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'gl_ES','[gl]planesobra');
		INSERT INTO dictionary (id_vocablo,locale,traduccion) VALUES (currval('seq_dictionary'),'eu_ES','[eu]planesobra');

		INSERT INTO layers (id_layer,id_name,validator,acl,name,id_styles,extended_form,modificable,id_entidad,versionable) 
				VALUES (nextval('seq_layers'),currval('seq_dictionary'),null,(select acl from layers where name='"gestion_espacio_publico_espacio_publico"'),'planesobra',currval('seq_styles'),null,1,0,1);

		 INSERT INTO layerfamilies_layers_relations (id_layer, id_layerfamily, position) VALUES (currval('seq_layers'),(select l.id_layerfamily from layerfamilies l,dictionary d  where l.id_name=d.id_vocablo and traduccion='Capas de Espacio Publico'),  (select MAX(position)+1 from layerfamilies_layers_relations where id_layerfamily = (select l.id_layerfamily from layerfamilies l,dictionary d  where l.id_name=d.id_vocablo and traduccion='Capas de Espacio Publico')));
		 INSERT INTO layers_styles (id_map,id_layerfamily,id_layer,id_style,stylename,isactive,position,id_entidad,isvisible,iseditable) 
			VALUES ((select id_map from maps where "xml" like '%<mapName>Gestion de espacio publico</mapName>%' limit 1),(select l.id_layerfamily from layerfamilies l,dictionary d  where l.id_name=d.id_vocablo and traduccion='Capas de Espacio Publico'),currval('seq_layers'),currval('seq_styles'),'planesobra:_:default',true,0,0,true,true);	
	END IF;

	-- Fin release33.sql

END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO20";

DROP TABLE IF EXISTS planesobra;	
CREATE TABLE planesobra
(
  id numeric(8,0) NOT NULL,
  "GEOMETRY" geometry,
  id_municipio numeric(5,0) NOT NULL,
  CONSTRAINT planesobra_pkey PRIMARY KEY (id),
  CONSTRAINT "$1" FOREIGN KEY (id_municipio)
      REFERENCES municipios (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP SEQUENCE IF EXISTS seq_planesobra;
CREATE SEQUENCE seq_planesobra
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_planesobra
  OWNER TO geopista;
  
  
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM tables WHERE name = 'planesobra' ) THEN
		INSERT INTO tables (id_table,name,geometrytype) VALUES (nextval('seq_tables'),'planesobra',11);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (nextval('seq_dictionary'),'es_ES','Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'ca_ES','[cat]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'gl_ES','[gl]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'va_ES','[va]Geometria');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (currval('seq_dictionary'),'eu_ES','[eu]Geometria');
		INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),null,'GEOMETRY',null,null,null,1);
		INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),1,true);

		INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
		INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10067,'id',null,8,0,2);
		INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),2,true);
		INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (currval('seq_columns'),10067,0);

		INSERT INTO dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Municipio');
		INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'ca_ES','[cat]Municipio');
		INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'va_ES','[va]Municipio');
		INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'gl_ES','[gl]Municipio');
		INSERT INTO dictionary (id_vocablo,locale, traduccion) values (currval('seq_dictionary'),'eu_ES','[eu]Municipio');
		INSERT INTO columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),currval('seq_tables'),10069,'id_municipio',null,5,0,2);
		INSERT INTO attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),currval('seq_layers'),currval('seq_columns'),5,true);
		INSERT INTO columns_domains(id_column,id_domain,LEVEL) VALUES (CURRVAL('seq_columns'),10069,0);
				
			
		INSERT INTO "public"."queries" ("id", "id_layer", "databasetype", "selectquery", "updatequery", "insertquery", "deletequery")
		VALUES 
		  (nextval('seq_queries'),currval('seq_layers'),'1','SELECT transform("planesobra"."GEOMETRY", ?T) AS "GEOMETRY","planesobra"."id","planesobra"."id_municipio" FROM "planesobra" WHERE "planesobra"."id_municipio" IN (?M)
		','UPDATE "planesobra" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio" = ?M WHERE "id"=?2
		','INSERT INTO "planesobra" ("GEOMETRY","id","id_municipio") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M)
		','DELETE FROM "planesobra" WHERE "id"=?2');


		delete from r_group_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='localgis.espaciopublico.principado');

		delete from r_acl_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='localgis.espaciopublico.principado');
		delete from usrgrouperm where def='localgis.espaciopublico.principado';

		PERFORM setval('public.seq_acl_perm', (select max(idperm)::bigint from r_acl_perm), true);

		insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'localgis.espaciopublico.principado','');

		insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),(select idacl::integer from acl where name ='Gestion de Espacio Publico'));
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO21";



-- 2012/01/25:
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'civil_work_id_document_sequence') THEN
		ALTER TABLE civil_work_id_document_sequence RENAME TO seq_civil_work_documents;
		ALTER TABLE civil_work_id_warning_sequence RENAME TO seq_civil_work_warnings;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO22";

-- Fin modelo.sql
-- Fin ESPACIO PUBLICO

--- Inicio administracion_postgres.sql

delete from appgeopista where def='EIEL';
insert into appgeopista (appid,def) values (22,'EIEL');
--- Fin administracion_postgres.sql

