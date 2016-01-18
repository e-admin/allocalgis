insert into query_catalog (id,query,acl,idperm) 
values('getAllDynamicLayersEntidad','select * from dynamiclayers where id_entidad=?',1,9205);

insert into query_catalog (id,query,acl,idperm) 
values('LMbuscarAliasAttributes','select * from attributes where id_alias = ?',1,9205);