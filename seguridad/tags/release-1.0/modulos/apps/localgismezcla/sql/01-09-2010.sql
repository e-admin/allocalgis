update query_catalog set query='select iusergrouphdr.id as id,
iusergrouphdr.name as name, iusergrouphdr.remarks as remarks,
r_group_perm.idperm as idperm, r_group_perm.idacl as idacl from
iusergrouphdr,r_group_perm where (iusergrouphdr.id_entidad=? OR
iusergrouphdr.id_entidad=0) and iusergrouphdr.id=r_group_perm.groupid order
by iusergrouphdr.id_entidad,iusergrouphdr.id' where id='allrolespermisos';