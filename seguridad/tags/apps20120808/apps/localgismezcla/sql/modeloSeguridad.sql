ALTER TABLE entidad_supramunicipal ADD COLUMN aviso numeric(3,0);
ALTER TABLE entidad_supramunicipal ADD COLUMN periodicidad numeric(3,0);
ALTER TABLE entidad_supramunicipal ADD COLUMN intentos numeric(2,0);

ALTER TABLE iuseruserhdr ADD COLUMN fecha_proxima_modificacion date;
ALTER TABLE iuseruserhdr ADD COLUMN bloqueado boolean;

update entidad_supramunicipal set aviso = 15, periodicidad = 365, intentos = 5;
update iuseruserhdr set bloqueado = false, fecha_proxima_modificacion = current_date-1;

insert into query_catalog (id,query,acl,idperm) values ('seguridadContrasenia','select bloqueado, aviso, fecha_proxima_modificacion, intentos from iuseruserhdr,entidad_supramunicipal where name = ? and iuseruserhdr.id_entidad = entidad_supramunicipal.id_entidad','1','9205');
insert into query_catalog (id,query,acl,idperm) values ('bloqueaUsuario','update iuseruserhdr set bloqueado = true where name = ?','1','9205');
update query_catalog set query='SELECT id, name, nombrecompleto, password, remarks,mail, deptid, nif, id_entidad, bloqueado FROM iuseruserhdr WHERE borrado!=1' where id = 'getListaUsuarios';

ALTER TABLE entidad_supramunicipal ALTER periodicidad TYPE integer;

ALTER TABLE iuseruserhdr ADD COLUMN intentos_reiterados numeric(2,0);
update query_catalog set query='select bloqueado, aviso, fecha_proxima_modificacion, intentos, intentos_reiterados from iuseruserhdr,entidad_supramunicipal where name = ? and iuseruserhdr.id_entidad = entidad_supramunicipal.id_entidad' where id = 'seguridadContrasenia';
