-- Consultas preparadas para el Gestor de Capas 

insert into query_catalog (id,query,acl,idperm) values('LMseleccionarIdFamiliaCapa','select id_layerfamily from layerfamilies where id_name=? and id_description=?',1,9205);

insert into query_catalog (id,query,acl,idperm) values('LMbuscaratributosnombrecapa','select * from attributes inner join layers on(attributes.id_layer=layers.id_layer) where name=? order by position',1,9205);

insert into query_catalog (id,query,acl,idperm) values('existeVersion','select coalesce(count(*),0) from versiones where revision=? and id_table_versionada=?',1,9205);


--Cambios en Base de Datos para Catastro             
ALTER TABLE parcelas ALTER ninterno TYPE numeric(8,0);

