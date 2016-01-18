

-- Se intruduce el nuevo ACL
insert into acl values (17, 'Capas Inventario Patrimonio');

-- Se asocia el ACL con los permisos de leer, escribir, etc
insert into r_acl_perm values (871, 17);
insert into r_acl_perm values (872, 17);
insert into r_acl_perm values (873, 17);
insert into r_acl_perm values (874, 17);

-- Se asocian las capas de patrimonio con el nuevo acl
update layers set acl=17 where id_layer=11006;
update layers set acl=17 where id_layer=11007;


-- Se actualizan los permisos de los usuarios para que sigan teniendo los mismos permisos sobre la capa, aunqeu estos sean de otro ACL.
insert into r_usr_perm (userid,idacl,idperm,aplica) 
select userid,17,871,aplica from r_usr_perm where idacl=12 and idperm=4000;
insert into r_usr_perm (userid,idacl,idperm,aplica) 
select userid,17,872,aplica from r_usr_perm where idacl=12 and idperm=4010;
insert into r_usr_perm (userid,idacl,idperm,aplica) 
select userid,17,873,aplica from r_usr_perm where idacl=12 and idperm=4020;
insert into r_usr_perm (userid,idacl,idperm,aplica) 
select userid,17,874,aplica from r_usr_perm where idacl=12 and idperm=4030;
