SELECT setval('public.seq_acl_perm', (select max(idperm)::bigint from usrgrouperm), true);
SELECT setval('public.seq_acl', (select max(idacl)::bigint from acl), true);


--DROP SEQUENCE seq_iuseruserhdr;

CREATE SEQUENCE seq_iuseruserhdr
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 400
  CACHE 1;

--DROP SEQUENCE seq_iusergrouphdr;


  CREATE SEQUENCE seq_iusergrouphdr
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 400
  CACHE 1;

--DROP SEQUENCE r_user_perm;

CREATE SEQUENCE seq_r_user_perm
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 400
  CACHE 1;

insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Login',''); ---Debe empezar en 10110
insert into acl(idacl,name) values(nextval('seq_acl'),'EIEL');
insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));

--Crea un usuario EIEL
--delete from iusergroupuser WHERE userid=400
--delete from iusergrouphdr WHERE id=400
--delete from iuseruserhdr where id=400;

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


--insert into r_usr_perm (userid,idperm,idacl,aplica)values ((select id from iuseruserhdr where name ='SATEC_DEV'),(select idperm from usrgrouperm where def='LocalGis.EIEL.Login'),(select idacl from acl where name='EIEL'),1);
--insert into r_usr_perm (userid,idperm,idacl,aplica)values ((select id from iuseruserhdr where name ='SATEC_DEV'),(select idperm from usrgrouperm where def='LocalGis.edicion.EIEL'),(select idacl from acl where name='EIEL'),1);

