--Se crea la secuencia para el inventario de features
DROP SEQUENCE IF EXISTS seq_inventario_feature;
CREATE SEQUENCE seq_inventario_feature
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_inventario_feature OWNER TO postgres;
--Fijamos el valor al maximo de la persona juridico fisica
SELECT setval('seq_inventario_feature', (select cast(max(REVISION_ACTUAL) as bigint) from INVENTARIO_FEATURE)+1); 

--Se añaden los campos de revisiones al inventario_feature
alter table INVENTARIO_FEATURE add revision_Actual numeric(10,0) default 0.0;
--alter table INVENTARIO_FEATURE drop revision_Actual;

alter table INVENTARIO_FEATURE add revision_expirada numeric(10,0) default 9999999999;
--alter table INVENTARIO_FEATURE drop revision_expirada;

alter table inmuebles add bic boolean;
--alter table inmuebles drop bic;

alter table inmuebles add catalogado boolean;
--alter table inmuebles drop catalogado;

alter table inmuebles_urbanos add anchoSuperf numeric(10,2) default 0.0;
--alter table inmuebles_urbanos drop anchoSuperf;
alter table inmuebles_rusticos add anchoSuperf numeric(10,2) default 0.0;
--alter table inmuebles_rusticos drop anchoSuperf;

alter table inmuebles_urbanos add longSuperf numeric(10,2) default 0.0;
--alter table inmuebles_urbanos drop longSuperf;
alter table inmuebles_rusticos add longSuperf numeric(10,2) default 0.0;
--alter table inmuebles_rusticos drop longSuperf;

alter table inmuebles_urbanos add metrPavSuperf numeric(10,2) default 0.0;
--alter table inmuebles_urbanos drop metrPavSuperf;
alter table inmuebles_rusticos add metrPavSuperf numeric(10,2) default 0.0;
--alter table inmuebles_rusticos drop metrPavSuperf;

alter table inmuebles_urbanos add metrNoPavSuperf numeric(10,2) default 0.0;
--alter table inmuebles_urbanos drop metrNoPavSuperf;
alter table inmuebles_rusticos add metrNoPavSuperf numeric(10,2) default 0.0;
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
SET @nomSecDom = 'seq_domainnodes';
SET @nomSecDic = 'seq_dictionary';
SET @numPatAmor = 0;
SET @numTipo = 7;

SET @nomDomAmor='Tipo de Amortizacion';
SET @nomAmortizacionAnos = 'por a�os';
SET @nomAmortizacionValor = 'por porcentaje';
SET @numMun = 77;

--delete from domains where name like('@nomAmor');
delete from domainnodes where id_description in (select id_vocablo from dictionary where traduccion in ('@nomAmortizacionAnos', '@nomAmortizacionValor'));
delete from dictionary where traduccion in ('@nomAmortizacionAnos', '[va]@nomAmortizacionAnos', '[gl]@nomAmortizacionAnos', '[eu]@nomAmortizacionAnos', '[cat]@nomAmortizacionAnos', '@nomAmortizacionValor', '[va]@nomAmortizacionValor', '[gl]@nomAmortizacionValor', '[eu]@nomAmortizacionValor', '[cat]@nomAmortizacionValor');


--(select * from domainnodes dn join dictionary dy on dn.id_description= dy.id_vocablo where dy.traduccion='Tipo de Amortizacion')

-- Insertamos las descripciones en la tabla dictionary
SET @idVoc = select nextval('@nomSecDic') from seq_dictionary;
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'es_ES' , '@nomAmortizacionAnos');
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'va_ES' , '[va]@nomAmortizacionAnos');
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'gl_ES' , '[gl]@nomAmortizacionAnos');
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'eu_ES' , '[eu]@nomAmortizacionAnos');
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'ca_ES' , '[cat]@nomAmortizacionAnos');

SET @idVoc = select nextval('@nomSecDic') from seq_dictionary;
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'es_ES' , '@nomAmortizacionValor');
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'va_ES' , '[va]@nomAmortizacionValor');
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'gl_ES' , '[gl]@nomAmortizacionValor');
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'eu_ES' , '[eu]@nomAmortizacionValor');
insert into dictionary (id_vocablo, locale, traduccion)values(@idVoc ,'ca_ES' , '[cat]@nomAmortizacionValor');


--insert into domains (id, name, idacl, id_category) values(nextval('seq_domains'),'@nomAmor',6,4);
-- Insertamos los nodos en la tabla domainnodes
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain)
values(
nextval('@nomSecDom'),
(select id from domains where name='@nomDomAmor'),
@numPatAmor,
(select id_vocablo from dictionary where traduccion in ('@nomAmortizacionAnos')),
@numTipo,
(select id from domainnodes dn join dictionary dy on dn.id_description= dy.id_vocablo where dn.id_municipio ='@numMun' and  dy.traduccion='@nomDomAmor'));

insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain)
values(
nextval('@nomSecDom'),
(select id from domains where name='@nomDomAmor'),
@numPatAmor,
(select id_vocablo from dictionary where traduccion in ('@nomAmortizacionValor')),
@numTipo,
(select id from domainnodes dn join dictionary dy on dn.id_description= dy.id_vocablo where dn.id_municipio ='@numMun' and dy.traduccion='@nomDomAmor'));



--select id_vocablo from dictionary where traduccion in ('Por Años');
--select * from dictionary where traduccion like'%Por Valor' or traduccion like'%Por Años' order by id_vocablo desc ;
--select * from domainnodes order by id desc;

--select * from domainnodes dn left join dictionary dc on dn.id_description=dc.id_vocablo where traduccion like'%Por Valor' or traduccion like'%Por Años'order by dn.id  desc;


--Bienes PreAlta
--Le damos al usuario Syssuperuser los permisos de conversion y consulta de bienes en prealta, y de loginn de inventario
SET @usuario = 'SYSSUPERUSER';
SET @esquema = 'Inventario';
SET @secuenciaACL = 'seq_acl';
--Insertamos un nuevo usuario, SICALWIN(a copia el usuario SIGEM), con permisos de insercion de bienes en PreAlta y de loginn de inventario y de LocalGis
SET @usuarioSICALWIN = 'SICALWIN';
SET @grupoSICALWIN = 'SICALWIN';
SET @contraSICALWIN = 'PggP3/BOfCFsFDgX6iL9gA==';
SET @esquemaSICALWIN = 'SICALWIN';

--Nuevos Permisos que se crean
SET @permPreAltaConsulta = 'Geopista.Inventario.BienesPreAlta.Conversion';
SET @permPreAltaInsercion = 'Geopista.Inventario.BienesPreAlta.Insercion';
SET @permLoginInventario = 'Geopista.Inventario.Login';
SET @permLogin = 'LocalGis.Login';
--Permisos para SYCALWIN
SET @permPreAltaInsercionSICALWIN = 'LocalGis.SICALWIN.BienesPreAlta.Insercion';
SET @permLoginSICALWIN = 'LocalGis.Login';


--Borramos SICALWIN
--Borramos la union por usuarios
delete from r_usr_perm where (userid = (select id from iuseruserhdr where name like '@usuarioSICALWIN')) and (idacl in(select idacl from acl where name like '@esquemaSICALWIN'));
--Borramos la union por grupos
delete from r_group_perm where (groupid = (select ID from iusergrouphdr where name like '@grupoSICALWIN')) and (idacl in(select idacl from acl where name like '@esquemaSICALWIN'));

delete from iusergroupuser WHERE (groupid=(select id from iusergrouphdr  where name like '@usuarioSICALWIN')) AND  (userid=(select id from iuseruserhdr  where name like '@usuarioSICALWIN'));
delete from iuseruserhdr  where name like '@usuarioSICALWIN';
delete from iusergrouphdr where name like'@grupoSICALWIN';
--Borramos SICALWIN
delete from r_acl_perm where (idacl in (select idacl from acl where name like '@esquemaSICALWIN'));
delete from usrgrouperm where def in ('@permLoginSICALWIN', '@permPreAltaInsercionSICALWIN')and idperm > 10000;
delete from acl where name like '@esquemaSICALWIN';



delete from R_USR_PERM where (userid=((select id from iuseruserhdr where name like '@usuario' ))) and idperm in (select idperm from USRGROUPERM where def in ('@permPreAltaConsulta', '@permPreAltaInsercion', '@permLoginInventario'));
delete from R_ACL_PERM where idperm in (select idperm from USRGROUPERM where def in ('@permPreAltaConsulta', '@permPreAltaInsercion'));
delete from USRGROUPERM where def in ('@permPreAltaConsulta', '@permPreAltaInsercion');

--No deberian existir permisos para los usuarios y esquemas marcados
--select u.id as USERID, u.name AS USER, a.idacl, a.name as ACL , ugp.idperm as IDPERMISO, ugp.def as PERMISO from iuseruserhdr u join r_usr_perm rup on (u.id=rup.userid) join r_acl_perm rap on (rup.idacl=rap.idacl) and (rup.idperm=rap.idperm) join usrgrouperm ugp on (rap.idperm=ugp.idperm)join acl a on (rap.idacl=a.idacl) where (u.name in ('SYSSUPERUSER', 'SICALWIN'))  and (def in ('Geopista.Inventario.BienesPreAlta.Conversion','Geopista.Inventario.BienesPreAlta.Insercion','Geopista.Inventario.Login','LocalGis.Login', 'LocalGis.SICALWIN.BienesPreAlta.Insercion'))order by rap.idperm desc;

-------------------------
----SICALWIN
-------------------------

--Insertamos el usuario SICALWIN
insert into iuseruserhdr (id, name, nombrecompleto, password, flags, stat, numbadcnts, remarks, crtrid, borrado, id_entidad, upddate, crtndate, fecha_proxima_modificacion, bloqueado) values ((select max(id)+1 from iuseruserhdr), '@usuarioSICALWIN', '@usuarioSICALWIN', '@contraSICALWIN', 0, 0, 0,'Usuario @usuarioSICALWIN', 100, 0, 0,'2011-04-14 00:00:00','2011-04-14 00:00:00','2011-04-14', false);
--Insertamos un Grupo de SICALWIN
insert into iusergrouphdr(id, name, mgrid, type, remarks, crtrid, crtndate) values ((select max(id)+1 from iusergrouphdr),'@usuarioSICALWIN',100,0,'Rol para Módulo @usuarioSICALWIN',100, '2011-04-14 00:00:00');
--Relacionamos el usuario y el grupo
insert into iusergroupuser (groupid, userid) values ((select id from iusergrouphdr  where name like '@usuarioSICALWIN'), (select id from iuseruserhdr  where name like '@usuarioSICALWIN'));
--Comprobar que todo lo de los usuarios es correcto
--select u.*, g.* from iuseruserhdr u join iusergroupuser j on u.id=j.userid join iusergrouphdr g on j.groupid=g.id where u.name like 'SICALWIN'

--Insertamos la ACL SICALWIN
insert into acl (idacl, name) values ((nextval('@secuenciaACL')), '@esquemaSICALWIN');
--Insertamos los permisos
SET @idPerm1 = select cast(max(idperm)+1 as int) from usrgrouperm;
insert into usrgrouperm (idperm, def) values(@idPerm1,'@permLoginSICALWIN');
SET @idPerm2 = select cast(max(idperm)+1 as int) from usrgrouperm;
insert into usrgrouperm (idperm, def) values (@idPerm2,'@permPreAltaInsercionSICALWIN');
--Relacionamos el permiso y la ACL
insert into r_acl_perm (idperm, idacl) values (@idPerm1,(select idacl from acl where name like 'SICALWIN'));
insert into r_acl_perm (idperm, idacl) values (@idPerm2,(select idacl from acl where name like 'SICALWIN'));
--Comprobar que todo lo de los permisos es correcto
--select a.*, g.* from acl a join r_acl_perm r on a.idacl=r.idacl join usrgrouperm g on r.idperm=g.idperm where a.name in ('SICALWIN', 'SIGEM');

--Insertamos la relacion entre los permisos creados y los usuarios
insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like '@usuarioSICALWIN'), @idPerm1,(select idacl from acl where name like ('@esquemaSICALWIN')), 1);
insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like '@usuarioSICALWIN'), @idPerm2,(select idacl from acl where name like ('@esquemaSICALWIN')), 1);
--Insertamos la relacion entre los permisos creados y los grupos
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like '@grupoSICALWIN'), @idPerm1,(select idacl from acl where name like ('@esquemaSICALWIN')));
insert into r_group_perm (groupid, idperm, idacl) values ((select id from iusergrouphdr where name like '@grupoSICALWIN'), @idPerm2,(select idacl from acl where name like ('@esquemaSICALWIN')));

--Insertamos el permiso de PreAltaConsulta para el usuario syssuperuser
insert into usrgrouperm (idperm, def) values (((select max(idperm) from usrgrouperm)+1), '@permPreAltaConsulta');
--Asociamos el permiso con la ACL 14 -- Inventario
insert into r_acl_perm (idperm, idacl) values ((select max(idperm) from usrgrouperm), (select idacl from acl where name like ('@esquema')));
--Asociamos el permiso con la ACL 14 y el Usuario 100 -- SysSuperUser
insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like '@usuario'), (select idperm from usrgrouperm where def like '@permPreAltaConsulta'),(select idacl from acl where name like ('@esquema')), 1);

--Insertamos el permiso de PreAltaInsercion para el usuario syssuperuser
insert into usrgrouperm (idperm, def) values (((select max(idperm) from usrgrouperm)+1), '@permPreAltaInsercion');
--Asociamos el permiso con la ACL 14 -- Inventario
insert into r_acl_perm (idperm, idacl) values ((select max(idperm) from usrgrouperm), (select idacl from acl where name like ('@esquema')));
--Asociamos el permiso con la ACL 14 y el Usuario 100 -- SysSuperUser
insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like '@usuario'), (select idperm from usrgrouperm where def like '@permPreAltaInsercion'),(select idacl from acl where name like ('@esquema')), 1);


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

--Añadimos cuentas contables de prueba
delete from contable where cuenta like '%Prueba%';
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(220,4567,'Terrenos y Bienes Naturales', 'Cuenta Prueba3');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(221,456,'Construcciones', 'Cuenta Prueba4');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(201,5446,'Infraestructuras y Bienes DEstinados al Uso General', 'Cuenta Prueba5');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(222,1231,'Instalaciones T�cnicas', 'Cuenta Prueba6');
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
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(252,4567,'Cr�ditos a largo Plazo', 'Cuenta Prueba17');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(250,456,'Inversiones Financieras Permanentes en Capital', 'Cuenta Prueba18');
--------------
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(240,78987,'Terrenos', 'Cuenta Prueba19');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(241,45645,'Construcciones', 'Cuenta Prueba20');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(242,5645,'Aprovechamientos Urban�sticos', 'Cuenta Prueba21');
insert into contable(id_clasificacion, cuenta_contable, descripcion, cuenta) values(249,3214,'Otros Bienes y Derechos del Patrimonio Publico del Suelo', 'Cuenta Prueba22');
--select * from contable where cuenta like '%Prueba%'

DROP INDEX IF EXISTS borrado;
CREATE INDEX borrado ON bienes_inventario(borrado);

DROP INDEX IF EXISTS indx_cultivosgeom;
DROP INDEX IF EXISTS indice indice_geomcultivos;
create index indice_geomcultivos on cultivosgeom using gist ( "GEOMETRY" );