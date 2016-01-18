-- Creamos unos permisos especificos
-- Borramos los permisos especificos que huberira de antes

SET @IDACL = select idacl::integer from acl where name ='EIEL';

SET @PERM1='LocalGis.EIEL.Publicador';
SET @PERM2='LocalGis.EIEL.Validador';

delete from r_group_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='@PERM1');
delete from r_group_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='@PERM2');

delete from r_acl_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='@PERM1');
delete from r_acl_perm where idperm in (select r_acl_perm.idperm from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where def='@PERM2');
delete from usrgrouperm where def='@PERM1';
delete from usrgrouperm where def='@PERM2';

insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), '@PERM1','');
insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),@IDACL);
insert into usrgrouperm(idperm,def,type) values (nextval('seq_acl_perm'), '@PERM2','');
insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),@IDACL);

