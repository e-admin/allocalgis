
delete from r_group_perm where groupid=(select id from iusergrouphdr where name='EIEL_VAL');
delete from iusergrouphdr where name='EIEL_VAL';

SELECT setval('public.seq_iusergrouphdr', (select max(id)::bigint from iusergrouphdr), true);
insert into iusergrouphdr(id,name,mgrid,type,remarks,crtrid,crtndate,updrid,upddate,id_entidad) 
values (nextval('seq_iusergrouphdr'),'EIEL_VAL',nextval('seq_r_user_perm'),0,'Rol para Usuario Validador EIEL',currval('seq_r_user_perm'),'2011-11-17',currval('seq_r_user_perm'),'2011-11-17',0);
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Captacion') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Captacion'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Captacion') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Captacion'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Captacion') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Captacion'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Captacion') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Captacion'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Deposito') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Deposito'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Deposito') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Deposito'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Deposito') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Deposito'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Deposito') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Deposito'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Depuradora') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Depuradora'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Depuradora') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Depuradora'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Depuradora') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Depuradora'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Depuradora') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Depuradora'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Red de ramales') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Red de ramales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Red de ramales') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Red de ramales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Red de ramales') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Red de ramales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Red de ramales') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Red de ramales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Punto de vertido') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Punto de vertido'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Punto de vertido') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Punto de vertido'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Punto de vertido') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Punto de vertido'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Punto de vertido') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Punto de vertido'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Instalacion deportiva') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Instalacion deportiva'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Instalacion deportiva') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Instalacion deportiva'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Instalacion deportiva') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Instalacion deportiva'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Instalacion deportiva') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Instalacion deportiva'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parques jardines y áreas naturales') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Parques jardines y áreas naturales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parques jardines y áreas naturales') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Parques jardines y áreas naturales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parques jardines y áreas naturales') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Parques jardines y áreas naturales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parques jardines y áreas naturales') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Parques jardines y áreas naturales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Lonjas, mercados y recintos feriales') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Lonjas, mercados y recintos feriales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Lonjas, mercados y recintos feriales') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Lonjas, mercados y recintos feriales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Lonjas, mercados y recintos feriales') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Lonjas, mercados y recintos feriales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Lonjas, mercados y recintos feriales') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Lonjas, mercados y recintos feriales'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Mataderos') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Mataderos'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Mataderos') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Mataderos'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Mataderos') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Mataderos'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Mataderos') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Mataderos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Cementerios') and def='Geopista.Layer.Escribir'  LIMIT 1),(select idacl::integer from acl where name ='Cementerios' LIMIT 1));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Cementerios') and def='Geopista.Layer.Leer'  LIMIT 1),(select idacl::integer from acl where name ='Cementerios' LIMIT 1));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Cementerios') and def='Geopista.Layer.ModificarSLD'  LIMIT 1),(select idacl::integer from acl where name ='Cementerios' LIMIT 1));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Cementerios') and def='Geopista.Layer.Añadir'  LIMIT 1),(select idacl::integer from acl where name ='Cementerios' LIMIT 1));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tanatorios' ) and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Tanatorios'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tanatorios') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Tanatorios'));;
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tanatorios') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Tanatorios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tanatorios') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Tanatorios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros Sanitarios') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Centros Sanitarios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros Sanitarios') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Centros Sanitarios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros Sanitarios') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Centros Sanitarios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros Sanitarios') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Centros Sanitarios'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros Asistenciales') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Centros Asistenciales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros Asistenciales') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Centros Asistenciales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros Asistenciales') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Centros Asistenciales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros Asistenciales') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Centros Asistenciales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros de enseñanza') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Centros de enseñanza'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros de enseñanza') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Centros de enseñanza'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros de enseñanza') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Centros de enseñanza'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros de enseñanza') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Centros de enseñanza'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centro de protección civil') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Centro de protección civil'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centro de protección civil') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Centro de protección civil'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centro de protección civil') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Centro de protección civil'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centro de protección civil') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Centro de protección civil'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Casas consistoriales') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Casas consistoriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Casas consistoriales') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Casas consistoriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Casas consistoriales') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Casas consistoriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Casas consistoriales') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Casas consistoriales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Edificios de titularidad publica sin uso') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Edificios de titularidad publica sin uso'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Edificios de titularidad publica sin uso') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Edificios de titularidad publica sin uso'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Edificios de titularidad publica sin uso') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Edificios de titularidad publica sin uso'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Edificios de titularidad publica sin uso') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Edificios de titularidad publica sin uso'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Infraestructura viaria') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Infraestructura viaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Infraestructura viaria') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Infraestructura viaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Infraestructura viaria') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Infraestructura viaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Infraestructura viaria') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Infraestructura viaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tramos de carretera') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Tramos de carretera'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tramos de carretera') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Tramos de carretera'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tramos de carretera') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Tramos de carretera'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tramos de carretera') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Tramos de carretera'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Cuadro de mando') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Cuadro de mando'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Cuadro de mando') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Cuadro de mando'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Cuadro de mando') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Cuadro de mando'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Cuadro de mando') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Cuadro de mando'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Estabilizador') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Estabilizador'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Estabilizador') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Estabilizador'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Estabilizador') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Estabilizador'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Estabilizador') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Estabilizador'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Punto de luz') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Punto de luz'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Punto de luz') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Punto de luz'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Punto de luz') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Punto de luz'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Punto de luz') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Punto de luz'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Vertedero') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Vertedero'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Vertedero') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Vertedero'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Vertedero') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Vertedero'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Vertedero') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Vertedero'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parroquias') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Parroquias'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parroquias') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Parroquias'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parroquias') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Parroquias'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parroquias') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Parroquias'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Edificaciones singulares') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Edificaciones singulares'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Edificaciones singulares') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Edificaciones singulares'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Edificaciones singulares') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Edificaciones singulares'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Edificaciones singulares') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Edificaciones singulares'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Comarcas EIEL') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Comarcas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Comarcas EIEL') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Comarcas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Comarcas EIEL') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Comarcas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Comarcas EIEL') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Comarcas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Municipios EIEL') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Municipios EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Municipios EIEL') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Municipios EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Municipios EIEL') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Municipios EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Municipios EIEL') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Municipios EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Municipios EIEL puntos') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Municipios EIEL puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Municipios EIEL puntos') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Municipios EIEL puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Municipios EIEL puntos') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Municipios EIEL puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Municipios EIEL puntos') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Municipios EIEL puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Nucleos Poblacion EIEL') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Nucleos Poblacion EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Nucleos Poblacion EIEL') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Nucleos Poblacion EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Nucleos Poblacion EIEL') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Nucleos Poblacion EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Nucleos Poblacion EIEL') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Nucleos Poblacion EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Nucleos Poblacion EIEL Puntos') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Nucleos Poblacion EIEL Puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Nucleos Poblacion EIEL Puntos') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Nucleos Poblacion EIEL Puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Nucleos Poblacion EIEL Puntos') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Nucleos Poblacion EIEL Puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Nucleos Poblacion EIEL Puntos') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Nucleos Poblacion EIEL Puntos'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parcelas EIEL') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Parcelas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parcelas EIEL') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Parcelas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parcelas EIEL') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Parcelas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Parcelas EIEL') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Parcelas EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Provincias EIEL') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Provincias EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Provincias EIEL') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Provincias EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Provincias EIEL') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Provincias EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Provincias EIEL') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Provincias EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tramos de Conduccion') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Tramos de Conduccion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tramos de Conduccion') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Tramos de Conduccion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tramos de Conduccion') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Tramos de Conduccion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tramos de Conduccion') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Tramos de Conduccion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tratamientos de Potabilizacion') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Tratamientos de Potabilizacion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tratamientos de Potabilizacion') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Tratamientos de Potabilizacion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tratamientos de Potabilizacion') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Tratamientos de Potabilizacion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Tratamientos de Potabilizacion') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Tratamientos de Potabilizacion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Elemento puntual de abastecimiento') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Elemento puntual de abastecimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Elemento puntual de abastecimiento') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Elemento puntual de abastecimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Elemento puntual de abastecimiento') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Elemento puntual de abastecimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Elemento puntual de abastecimiento') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Elemento puntual de abastecimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Emisario') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Emisario'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Emisario') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Emisario'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Emisario') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Emisario'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Emisario') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Emisario'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Colector') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Colector'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Colector') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Colector'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Colector') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Colector'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Colector') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Colector'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Elementos puntuales') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Elementos puntuales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Elementos puntuales') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Elementos puntuales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Elementos puntuales') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Elementos puntuales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Elementos puntuales') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Elementos puntuales'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros culturales o de esparcimiento') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Centros culturales o de esparcimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros culturales o de esparcimiento') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Centros culturales o de esparcimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros culturales o de esparcimiento') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Centros culturales o de esparcimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Centros culturales o de esparcimiento') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Centros culturales o de esparcimiento'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Red de distribucion domiciliaria') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Red de distribucion domiciliaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Red de distribucion domiciliaria') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Red de distribucion domiciliaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Red de distribucion domiciliaria') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Red de distribucion domiciliaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Red de distribucion domiciliaria') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Red de distribucion domiciliaria'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select usrgrouperm.idperm::integer from usrgrouperm where def='LocalGis.EIEL.Login'),(select idacl::integer from acl where name ='EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select usrgrouperm.idperm::integer from usrgrouperm where def='LocalGis.edicion.EIEL'),(select idacl::integer from acl where name ='EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select usrgrouperm.idperm::integer from usrgrouperm where def='LocalGis.EIEL.Consulta'),(select idacl::integer from acl where name ='EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select usrgrouperm.idperm::integer from usrgrouperm where def='LocalGis.EIEL.Modifica'),(select idacl::integer from acl where name ='EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select usrgrouperm.idperm::integer from usrgrouperm where def='LocalGis.EIEL.Validador'),(select idacl::integer from acl where name ='EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select usrgrouperm.idperm::integer from usrgrouperm where def='Geopista.Administracion.Login'),(select idacl::integer from acl where name ='Administracion'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select usrgrouperm.idperm::integer from usrgrouperm where def='Geopista.EIEL.versionado.visualizacion'),(select idacl::integer from acl where name ='EIEL'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select usrgrouperm.idperm::integer from usrgrouperm where def='Geopista.EIEL.versionado.modificacion'),(select idacl::integer from acl where name ='EIEL'));


/*SET @ROL = 'EIEL_VAL';


--Borramos los permisos que tuviera el role
delete from r_group_perm where groupid=(select id from iusergrouphdr where name='@ROL');
-- Borramos el rol
delete from iusergrouphdr where name='@ROL';

-- Creamos el rol
insert into iusergrouphdr(id,name,mgrid,type,remarks,crtrid,crtndate,updrid,upddate,id_entidad) 
values (nextval('seq_iusergrouphdr'),'@ROL',nextval('seq_r_user_perm'),0,'Rol para Usuario Publicador EIEL',currval('seq_r_user_perm'),'2011-11-17',currval('seq_r_user_perm'),'2011-11-17',0);

-- Añadimos permisos al rol
DECLARE @LAYERS { @1,@2,@3,@4,@5,@6,@7,@8,@9,@10,@11,@12,@13,@14,@15,@16,@17,@18,@19,@20,@21,@22,@23,@24,@25,@26,@27,@28,@29,@30,@31,@32,@33,@34,@35,@36,@37,@38,@39,@40}; -- Una columna por cada capa
SET @LAYERS[0][0]= 'Captacion';
SET @LAYERS[0][1]= 'Deposito';
SET @LAYERS[0][2]= 'Depuradora';
SET @LAYERS[0][3]= 'Red de ramales';
SET @LAYERS[0][4]= 'Punto de vertido';
SET @LAYERS[0][5]= 'Instalacion deportiva';
SET @LAYERS[0][6]= 'Parques jardines y áreas naturales';
SET @LAYERS[0][7]= 'Lonjas, mercados y recintos feriales';
SET @LAYERS[0][8]= 'Mataderos';
SET @LAYERS[0][9]= 'Cementerios';
SET @LAYERS[0][10]= 'Tanatorios';
SET @LAYERS[0][11]= 'Centros Sanitarios';
SET @LAYERS[0][12]= 'Centros Asistenciales';
SET @LAYERS[0][13]= 'Centros de enseñanza';
SET @LAYERS[0][14]= 'Centro de protección civil';
SET @LAYERS[0][15]= 'Casas consistoriales';
SET @LAYERS[0][16]= 'Edificios de titularidad publica sin uso';
SET @LAYERS[0][17]= 'Infraestructura viaria';
SET @LAYERS[0][18]= 'Tramos de carretera';
SET @LAYERS[0][19]= 'Cuadro de mando';
SET @LAYERS[0][20]= 'Estabilizador';
SET @LAYERS[0][21]= 'Punto de luz';
SET @LAYERS[0][22]= 'Vertedero';
SET @LAYERS[0][23]= 'Parroquias';
SET @LAYERS[0][24]= 'Edificaciones singulares';
SET @LAYERS[0][25]= 'Comarcas EIEL';
SET @LAYERS[0][26]= 'Municipios EIEL';
SET @LAYERS[0][27]= 'Municipios EIEL puntos';
SET @LAYERS[0][28]= 'Nucleos Poblacion EIEL';
SET @LAYERS[0][29]= 'Nucleos Poblacion EIEL Puntos';
SET @LAYERS[0][30]= 'Parcelas EIEL';
SET @LAYERS[0][31]= 'Provincias EIEL';
SET @LAYERS[0][32]= 'Tramos de Conduccion';
SET @LAYERS[0][33]= 'Tratamientos de Potabilizacion';
SET @LAYERS[0][34]= 'Elemento puntual de abastecimiento';
SET @LAYERS[0][35]= 'Emisario';
SET @LAYERS[0][36]= 'Colector';
SET @LAYERS[0][37]= 'Elementos puntuales';
SET @LAYERS[0][38]= 'Centros culturales o de esparcimiento';
SET @LAYERS[0][39]= 'Red de distribucion domiciliaria';



--- PERMISOS

DECLARE @PERMISOS { @1,@2,@3,@4 }; -- Un columna por permiso
SET @PERMISOS[0][0]= 'Geopista.Layer.Escribir';
SET @PERMISOS[0][1]= 'Geopista.Layer.Leer';
SET @PERMISOS[0][2]= 'Geopista.Layer.ModificarSLD';
SET @PERMISOS[0][3]= 'Geopista.Layer.Añadir';

--PRINT COLUMNS(@PERMISOS);



SET @I = 0; -- @I is an integer
WHILE @I < COLUMNS(@LAYERS)
BEGIN
	set @LAYER= @LAYERS[0][@I];
	PRINT @LAYER;
	SET @IDACL = select idacl::integer from acl where name ='@LAYER';
	SET @J = 0; -- @J is an integer
	WHILE @J < COLUMNS(@PERMISOS)
	BEGIN
	   set @PERM=@PERMISOS[0][@J];
	   SET @IDPERM = select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= @IDACL and def='@PERM';
	   INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),@IDPERM,@IDACL);
	   SET @J = @J + 1;
	END
SET @I = @I + 1;
END


-- Añadimos el permiso de login al rol EIEL_PUB
DECLARE @PERMISOS_EXTRAS { @1,@2,@3,@4,@5,@6,@7,@8},@ACL_EXTRAS{@1,@2,@3,@4,@5,@6,@7,@8}; -- Un columna por permiso
SET @PERMISOS_EXTRAS[0][0]= 'LocalGis.EIEL.Login';
SET @ACL_EXTRAS[0][0]= 'EIEL';
SET @PERMISOS_EXTRAS[0][1]= 'LocalGis.edicion.EIEL';
SET @ACL_EXTRAS[0][1]= 'EIEL';
SET @PERMISOS_EXTRAS[0][2]= 'LocalGis.EIEL.Consulta';
SET @ACL_EXTRAS[0][2]= 'EIEL';
SET @PERMISOS_EXTRAS[0][3]= 'LocalGis.EIEL.Modifica';
SET @ACL_EXTRAS[0][3]= 'EIEL';
--SET @PERMISOS_EXTRAS[0][4]= 'LocalGis.EIEL.Publicador';
--SET @ACL_EXTRAS[0][4]= 'EIEL';
SET @PERMISOS_EXTRAS[0][4]= 'LocalGis.EIEL.Validador';
SET @ACL_EXTRAS[0][4]= 'EIEL';
SET @PERMISOS_EXTRAS[0][5]= 'Geopista.Administracion.Login';
SET @ACL_EXTRAS[0][5]= 'Administracion';
SET @PERMISOS_EXTRAS[0][6]= 'Geopista.EIEL.versionado.visualizacion';
SET @ACL_EXTRAS[0][6]= 'EIEL';
SET @PERMISOS_EXTRAS[0][7]= 'Geopista.EIEL.versionado.modificacion';
SET @ACL_EXTRAS[0][7]= 'EIEL';


SET @J = 0; -- @J is an integer
WHILE @J < COLUMNS(@PERMISOS_EXTRAS)
BEGIN
   set @PERM=@PERMISOS_EXTRAS[0][@J];
   set @ACL=@ACL_EXTRAS[0][@J];
   SET @IDACL = select idacl::integer from acl where name ='@ACL';
   print @IDACL;
   SET @IDPERM = select usrgrouperm.idperm::integer from usrgrouperm where def='@PERM';
   INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),@IDPERM,@IDACL);
   SET @J = @J + 1;
END

--delete from r_group_perm where groupid=(select id from iusergrouphdr where name='EIEL_PUB');
--select * from usrgrouperm where def='LocalGis.EIEL.Validador';
*/
/*--Borramos los permisos que tuviera el role
delete from r_group_perm where groupid=(select id from iusergrouphdr where name='EIEL_VAL');
-- Borramos el rol
delete from iusergrouphdr where name='EIEL_VAL';

-- Creamos el rol
insert into iusergrouphdr(id,name,mgrid,type,remarks,crtrid,crtndate,updrid,upddate,id_entidad) 
values (nextval('seq_iusergrouphdr'),'EIEL_VAL',nextval('seq_r_user_perm'),0,'Rol para Usuario Publicador EIEL',currval('seq_r_user_perm'),'2011-11-17',currval('seq_r_user_perm'),'2011-11-17',0);

-- Añadimos permisos al rol
DECLARE @LAYERS { @1,@2,@3,@4,@5,@6,@7,@8,@9,@10,@11,@12,@13,@14,@15,@16,@17,@18,@19,@20,@21,@22,@23,@24,@25,@26,@27,@28,@29,@30,@31,@32,@33,@34,@35,@36,@37,@38,@39,@40}; -- Una columna por cada capa
SET @LAYERS[0][0]= 'Captacion';
SET @LAYERS[0][1]= 'Deposito';
SET @LAYERS[0][2]= 'Depuradora';
SET @LAYERS[0][3]= 'Red de ramales';
SET @LAYERS[0][4]= 'Punto de vertido';
SET @LAYERS[0][5]= 'Instalacion deportiva';
SET @LAYERS[0][6]= 'Parques jardines y áreas naturales';
SET @LAYERS[0][7]= 'Lonjas, mercados y recintos feriales';
SET @LAYERS[0][8]= 'Mataderos';
SET @LAYERS[0][9]= 'Cementerios';
SET @LAYERS[0][10]= 'Tanatorios';
SET @LAYERS[0][11]= 'Centros Sanitarios';
SET @LAYERS[0][12]= 'Centros Asistenciales';
SET @LAYERS[0][13]= 'Centros de enseñanza';
SET @LAYERS[0][14]= 'Centro de protección civil';
SET @LAYERS[0][15]= 'Casas consistoriales';
SET @LAYERS[0][16]= 'Edificios de titularidad publica sin uso';
SET @LAYERS[0][17]= 'Infraestructura viaria';
SET @LAYERS[0][18]= 'Tramos de carretera';
SET @LAYERS[0][19]= 'Cuadro de mando';
SET @LAYERS[0][20]= 'Estabilizador';
SET @LAYERS[0][21]= 'Punto de luz';
SET @LAYERS[0][22]= 'Vertedero';
SET @LAYERS[0][23]= 'Parroquias';
SET @LAYERS[0][24]= 'Edificaciones singulares';
SET @LAYERS[0][25]= 'Comarcas EIEL';
SET @LAYERS[0][26]= 'Municipios EIEL';
SET @LAYERS[0][27]= 'Municipios EIEL puntos';
SET @LAYERS[0][28]= 'Nucleos Poblacion EIEL';
SET @LAYERS[0][29]= 'Nucleos Poblacion EIEL Puntos';
SET @LAYERS[0][30]= 'Parcelas EIEL';
SET @LAYERS[0][31]= 'Provincias EIEL';
SET @LAYERS[0][32]= 'Tramos de Conduccion';
SET @LAYERS[0][33]= 'Tratamientos de Potabilizacion';
SET @LAYERS[0][34]= 'Elemento puntual de abastecimiento';
SET @LAYERS[0][35]= 'Emisario';
SET @LAYERS[0][36]= 'Colector';
SET @LAYERS[0][37]= 'Elementos puntuales';
SET @LAYERS[0][38]= 'Centros culturales o de esparcimiento';
SET @LAYERS[0][39]= 'Red de distribucion domiciliaria';



--- PERMISOS

DECLARE @PERMISOS { @1,@2,@3,@4 }; -- Un columna por permiso
SET @PERMISOS[0][0]= 'Geopista.Layer.Escribir';
SET @PERMISOS[0][1]= 'Geopista.Layer.Leer';
SET @PERMISOS[0][2]= 'Geopista.Layer.ModificarSLD';
SET @PERMISOS[0][3]= 'Geopista.Layer.Añadir';

--PRINT COLUMNS(@PERMISOS);



SET @I = 0; -- @I is an integer
WHILE @I < COLUMNS(@LAYERS)
BEGIN
	set @LAYER= @LAYERS[0][@I];
	PRINT @LAYER;
	SET @IDACL = select idacl::integer from acl where name ='@LAYER';
	SET @J = 0; -- @J is an integer
	WHILE @J < COLUMNS(@PERMISOS)
	BEGIN
	   set @PERM=@PERMISOS[0][@J];
	   SET @IDPERM = select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= @IDACL and def='@PERM';
	   INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),@IDPERM,@IDACL);
	   SET @J = @J + 1;
	END
SET @I = @I + 1;
END


-- Añadimos el permiso de login al rol EIEL_PUB
DECLARE @PERMISOS_EXTRAS { @1,@2,@3,@4,@5,@6,@7,@8},@ACL_EXTRAS{@1,@2,@3,@4,@5,@6,@7,@8}; -- Un columna por permiso
SET @PERMISOS_EXTRAS[0][0]= 'LocalGis.EIEL.Login';
SET @ACL_EXTRAS[0][0]= 'EIEL';
SET @PERMISOS_EXTRAS[0][1]= 'LocalGis.edicion.EIEL';
SET @ACL_EXTRAS[0][1]= 'EIEL';
SET @PERMISOS_EXTRAS[0][2]= 'LocalGis.EIEL.Consulta';
SET @ACL_EXTRAS[0][2]= 'EIEL';
SET @PERMISOS_EXTRAS[0][3]= 'LocalGis.EIEL.Modifica';
SET @ACL_EXTRAS[0][3]= 'EIEL';
--SET @PERMISOS_EXTRAS[0][4]= 'LocalGis.EIEL.Publicador';
--SET @ACL_EXTRAS[0][4]= 'EIEL';
SET @PERMISOS_EXTRAS[0][4]= 'LocalGis.EIEL.Validador';
SET @ACL_EXTRAS[0][4]= 'EIEL';
SET @PERMISOS_EXTRAS[0][5]= 'Geopista.Administracion.Login';
SET @ACL_EXTRAS[0][5]= 'Administracion';
SET @PERMISOS_EXTRAS[0][6]= 'Geopista.EIEL.versionado.visualizacion';
SET @ACL_EXTRAS[0][6]= 'EIEL';
SET @PERMISOS_EXTRAS[0][7]= 'Geopista.EIEL.versionado.modificacion';
SET @ACL_EXTRAS[0][7]= 'EIEL';


SET @J = 0; -- @J is an integer
WHILE @J < COLUMNS(@PERMISOS_EXTRAS)
BEGIN
   set @PERM=@PERMISOS_EXTRAS[0][@J];
   set @ACL=@ACL_EXTRAS[0][@J];
   SET @IDACL = select idacl::integer from acl where name ='@ACL';
   print @IDACL;
   SET @IDPERM = select usrgrouperm.idperm::integer from usrgrouperm where def='@PERM';
   INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),@IDPERM,@IDACL);
   SET @J = @J + 1;
END

--delete from r_group_perm where groupid=(select id from iusergrouphdr where name='EIEL_PUB');
--select * from usrgrouperm where def='LocalGis.EIEL.Validador';
*/