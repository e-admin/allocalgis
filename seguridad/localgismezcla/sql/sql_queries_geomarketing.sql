-- Consultas para insertar en query_catalog para importador padron (asociacion de domicilo<-->portal
ALTER TABLE domicilio ADD COLUMN id_portal numeric(8);
-- Actualizacion para asignar portal a los domicilios
insert into query_catalog values ('updateDomicilioPortal','UPDATE domicilio SET id_portal=? where id_domicilio=?',1,9205);
insert into query_catalog values ('buscarIdPortalDomicilio','select id from numeros_policia where id_via = ? AND rotulo = ?',1,9205);
-- Actualizacion para encontrar a los habitantes
update query_catalog set query = 'select id_habitante from habitantes where codigoprovincia=? and codigomunicipio=? and upper(nombre)=upper(?) and upper(part_apell1)=upper(?) and upper(apellido1)=upper(?) and upper(part_apell2)=upper(?) and upper(apellido2)=upper(?) and nacprovincia=? and nacmunicipio=? and nacfecha=? and dni=? and upper(letradni)=upper(?) and tipodocumento=?' where id = 'buscarIdHabitante';
