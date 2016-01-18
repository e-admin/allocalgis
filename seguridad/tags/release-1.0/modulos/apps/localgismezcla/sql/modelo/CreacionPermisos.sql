SET @usuario = 'SYSSUPERUSER';
SET @esquema = 'EIEL';
SET @secuenciaACL = 'seq_acl';
SET @usuarioEIEL = 'EIEL';
SET @esquemaEIEL = 'EIEL';

--Nuevos Permisos que se crean
SET @permEIELVista = 'Geopista.EIEL.versionado.visualizacion';
SET @permEIELModificacion = 'Geopista.EIEL.versionado.modificacion';


--Insertamos el permiso de permEIELVista para el usuario syssuperuser
insert into usrgrouperm (idperm, def) values (((select max(idperm) from usrgrouperm)+1), '@permEIELVista');
--Asociamos el permiso con la ACL EIEL
insert into r_acl_perm (idperm, idacl) values ((select max(idperm) from usrgrouperm), (select idacl from acl where name like ('@esquema')));
--Asociamos el permiso con la ACL 14 y el Usuario 100 -- SysSuperUser
insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like '@usuario'), (select idperm from usrgrouperm where def like '@permEIELVista'),(select idacl from acl where name like ('@esquema')), 1);

insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like '@usuarioEIEL'), (select idperm from usrgrouperm where def like '@permEIELVista'),(select idacl from acl where name like ('@esquema')), 1);

--Insertamos el permiso de permEIELModificacion para el usuario syssuperuser
insert into usrgrouperm (idperm, def) values (((select max(idperm) from usrgrouperm)+1), '@permEIELModificacion');
--Asociamos el permiso con la ACL 14 EIEL
insert into r_acl_perm (idperm, idacl) values ((select max(idperm) from usrgrouperm), (select idacl from acl where name like ('@esquema')));
--Asociamos el permiso con la ACL 14 y el Usuario 100 -- SysSuperUser
insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like '@usuario'), (select idperm from usrgrouperm where def like '@permEIELModificacion'),(select idacl from acl where name like ('@esquema')), 1);

insert into r_usr_perm (userid, idperm, idacl, aplica) values ((select id from iuseruserhdr where name like '@usuarioEIEL'), (select idperm from usrgrouperm where def like '@permEIELModificacion'),(select idacl from acl where name like ('@esquema')), 1);




--Permisos Creados
select u.id as USERID, u.name AS USER, a.idacl, a.name as ACL , ugp.idperm as IDPERMISO, ugp.def as PERMISO 
from iuseruserhdr u 
join r_usr_perm rup on (u.id=rup.userid) 
join r_acl_perm rap on (rup.idacl=rap.idacl) and (rup.idperm=rap.idperm) 
join usrgrouperm ugp on (rap.idperm=ugp.idperm)
join acl a on (rap.idacl=a.idacl) 
where (u.name in ('EIEL'))  
order by rap.idperm desc;
--Comprobar que se ha crado bien el usuario EIEL, con su grupo...
select u.*, g.* 
from iuseruserhdr u 
join iusergroupuser j on u.id=j.userid 
join iusergrouphdr g on j.groupid=g.id 
where u.name in ('EIEL');
--Comprobar que se han creado los permisos para EIEL
select a.*, g.* from acl a 
join r_acl_perm r on a.idacl=r.idacl 
join usrgrouperm g on r.idperm=g.idperm 
where a.name in ('EIEL');
--Consulta Permisos del Usuario EIEL
select u.id as IDUSER, u.name as USER, g.id as IDGROUP, g.name as GROUP, ugp.idperm as IDPERMISO, ugp.def as PERMISO, a.idacl as IDACL, a.name as ACL 
from iuseruserhdr u  
join iusergroupuser ug on u.id=ug.userid  
join iusergrouphdr g on ug.groupid=g.id  
join r_usr_perm rup on u.id=rup.userid 
join r_acl_perm rap on rup.idacl=rap.idacl and rup.idperm=rap.idperm 
join usrgrouperm ugp on (rap.idperm=ugp.idperm) 
join acl a on (rap.idacl=a.idacl) 
where u.name in ('EIEL');
--Consulta Permisos del GRUPO EIEL
select u.id as IDUSER, u.name as USER, g.id as IDGROUP, g.name as GROUP, ugp.idperm as IDPERMISO, ugp.def as PERMISO, a.idacl as IDACL, a.name as ACL 
from iuseruserhdr u 
join iusergroupuser ug on u.id=ug.userid  
join iusergrouphdr g on ug.groupid=g.id  
join r_group_perm rgp on ug.groupid=rgp.groupid 
join r_acl_perm rap on rgp.idacl=rap.idacl and rgp.idperm=rap.idperm 
join usrgrouperm ugp on (rap.idperm=ugp.idperm) 
join acl a on (rap.idacl=a.idacl) 
where u.name in ('EIEL');


