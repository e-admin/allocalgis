
-- CAPA PARCELAS

-- Column_domains eliminados
delete from columns_domains where id_column = 2006;
delete from columns_domains where id_column = 2007;
delete from columns_domains where id_column = 2018;
delete from columns_domains where id_column = 2020;
delete from columns_domains where id_column = 2019;
delete from columns_domains where id_column = 2021;
delete from columns_domains where id_column = 2026;
delete from columns_domains where id_column = 2027;

-- Columnas eliminadas
-- name tipo
delete from columns where id = 2006;
-- name codigo_entidad_menor
delete from columns where id = 2007;
-- name CodigoGerencia
delete from columns where id = 2018;
-- name CodigoMunicipioMEH
delete from columns where id = 2020;
-- name CodigoNaturalezaBien
delete from columns where id = 2019;
-- name numeroCargo
delete from columns where id = 2021;
-- name primerCaracterControl
delete from columns where id = 2026;
-- name SegundoCaracterControl
delete from columns where id = 2027;


--Atributos eliminados
delete from attributes where id_column = 2006;
delete from attributes where id_column = 2007;
delete from attributes where id_column = 2018;
delete from attributes where id_column = 2020;
delete from attributes where id_column = 2019;
delete from attributes where id_column = 2021;
delete from attributes where id_column = 2026;
delete from attributes where id_column = 2027;


-- Modifica Orden atributos
update attributes set position = 6 where id_column = 2041 and id_layer = 101;
update attributes set position = 7 where id_column = 2040 and id_layer = 101;
update attributes set position = 18 where id_column = 2031 and id_layer = 101;
update attributes set position = 19 where id_column = 2030 and id_layer = 101;
update attributes set position = 20 where id_column = 2028 and id_layer = 101;
update attributes set position = 21 where id_column = 2029 and id_layer = 101;


update queries set selectquery = 'SELECT Parcelas.*, Municipios.NombreOficial FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) WHERE Municipios.ID=?M AND Fecha_baja IS NULL', updatequery = 'UPDATE parcelas SET "GEOMETRY"=GeometryFromText(?1,?S),id=?2,referencia_catastral=?3,id_municipio=?M,id_distrito=?5,codigoparcela=?6,codigopoligono=?7,id_via=?8,primer_numero=?9,primera_letra=?10,segundo_numero=?11,segunda_letra=?12,kilometro=?13,bloque=?14,direccion_no_estructurada=?15,codigo_postal=?16,codigodelegacionmeh=?17,length=perimeter(GeometryFromText(?1,?S)),area=area2d(GeometryFromText(?1,?S)),fecha_alta=?20,fecha_baja=?21 WHERE ID=?2', insertquery = 'INSERT INTO parcelas ("GEOMETRY",id,referencia_catastral,id_municipio,id_distrito,codigoparcela,codigopoligono,id_via,primer_numero,primera_letra,segundo_numero,segunda_letra,kilometro,bloque,direccion_no_estructurada,codigo_postal,codigodelegacionmeh,length,area,fecha_alta,fecha_baja,) VALUES(GeometryFromText(?1,?S),?PK,?3,?M,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,perimeter(GeometryFromText(?1,?S)),area2d(GeometryFromText(?1,?S)),?20,?21)', deletequery = 'DELETE FROM parcelas WHERE ID=?2' where id_layer = '101';





--insertamos la entidad generadora de prueba
insert into entidad_generadora(codigo, tipo, descripcion,nombre, id_entidad_generadora) values(991,'st','nosotros','satec', 1);
insert into entidad_generadora(codigo, tipo, descripcion,nombre, id_entidad_generadora) values(998,'ct','catastro','catastro', 2);


-- Insertamos las traducciones del dominio Estados de Expediente 
insert into dictionary (id_vocablo, locale, traduccion)
values (224000, 'es_ES', 'Estados de Expediente');
insert into dictionary (id_vocablo, locale, traduccion)
values (224000, 'ca_ES', '[ca]Estados de Expediente');
insert into dictionary (id_vocablo, locale, traduccion)
values (224000, 'gl_ES', '[gl]Estados de Expediente');
insert into dictionary (id_vocablo, locale, traduccion)
values (224000, 'va_ES', '[va]Estados de Expediente');

insert into dictionary (id_vocablo, locale, traduccion)
values (224001, 'es_ES', 'Registrado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224001, 'ca_ES', '[ca]Registrado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224001, 'gl_ES', '[gl]Registrado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224001, 'va_ES', '[va]Registrado');

insert into dictionary (id_vocablo, locale, traduccion)
values (224002, 'es_ES', 'Asociado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224002, 'ca_ES', '[ca]Asociado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224002, 'gl_ES', '[gl]Asociado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224002, 'va_ES', '[va]Asociado');

insert into dictionary (id_vocablo, locale, traduccion)
values (224003, 'es_ES', 'Rellenado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224003, 'ca_ES', '[ca]Rellenado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224003, 'gl_ES', '[gl]Rellenado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224003, 'va_ES', '[va]Rellenado');

insert into dictionary (id_vocablo, locale, traduccion)
values (224004, 'es_ES', 'Sincronizado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224004, 'ca_ES', '[ca]Sincronizado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224004, 'gl_ES', '[gl]Sincronizado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224004, 'va_ES', '[va]Sincronizado');

insert into dictionary (id_vocablo, locale, traduccion)
values (224005, 'es_ES', 'Modificado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224005, 'ca_ES', '[ca]Modificado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224005, 'gl_ES', '[gl]Modificado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224005, 'va_ES', '[va]Modificado');

insert into dictionary (id_vocablo, locale, traduccion)
values (224006, 'es_ES', 'Finalizado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224006, 'ca_ES', '[ca]Finalizado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224006, 'gl_ES', '[gl]Finalizado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224006, 'va_ES', '[va]Finalizado');

insert into dictionary (id_vocablo, locale, traduccion)
values (224007, 'es_ES', 'Generado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224007, 'ca_ES', '[ca]Generado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224007, 'gl_ES', '[gl]Generado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224007, 'va_ES', '[va]Generado');

insert into dictionary (id_vocablo, locale, traduccion)
values (224008, 'es_ES', 'Enviado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224008, 'ca_ES', '[ca]Enviado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224008, 'gl_ES', '[gl]Enviado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224008, 'va_ES', '[va]Enviado');

insert into dictionary (id_vocablo, locale, traduccion)
values (224009, 'es_ES', 'Cerrado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224009, 'ca_ES', '[ca]Cerrado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224009, 'gl_ES', '[gl]Cerrado');
insert into dictionary (id_vocablo, locale, traduccion)
values (224009, 'va_ES', '[va]Cerrado');


-- Creamos el dominio Estados_de_expediente
insert into domains (id, name, idacl, id_category) values (20050, 'Estados de Expediente', 6, 4);

-- Insertamos los nuevos domainnodes referentes al dominio Estados_de_expediente
insert into domainnodes (id, id_domain, id_description, type, permissionlevel)
values (200300, 20050, 224000, 4, 1);
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain)
values (200301, 20050, '1', 224001, 7, 1, 200300);
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain)
values (200302, 20050, '2', 224002, 7, 1, 200300);
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain)
values (200303, 20050, '3', 224003, 7, 1, 200300);
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain)
values (200304, 20050, '4', 224004, 7, 1, 200300);
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain)
values (200305, 20050, '5', 224005, 7, 1, 200300);
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain)
values (200306, 20050, '6', 224006, 7, 1, 200300);
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain)
values (200307, 20050, '7', 224007, 7, 1, 200300);
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain)
values (200308, 20050, '8', 224008, 7, 1, 200300);
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain)
values (200309, 20050, '9', 224009, 7, 1, 200300);

--insertamos los estados
insert into estado_expediente(ID_Estado, Nombre_Estado, Descripcion)values(1,'Registrado','Primer paso del expediente');
insert into estado_expediente(ID_Estado, Nombre_Estado, Descripcion)values(2,'Asociado','Primer paso del expediente');
insert into estado_expediente(ID_Estado, Nombre_Estado, Descripcion)values(3,'Rellenado','Primer paso del expediente');
insert into estado_expediente(ID_Estado, Nombre_Estado, Descripcion)values(4,'Sincronizado','Primer paso del expediente');
insert into estado_expediente(ID_Estado, Nombre_Estado, Descripcion)values(5,'Modificado','Primer paso del expediente');
insert into estado_expediente(ID_Estado, Nombre_Estado, Descripcion)values(6,'Finalizado','Primer paso del expediente');
insert into estado_expediente(ID_Estado, Nombre_Estado, Descripcion)values(7,'Generado','Primer paso del expediente');
insert into estado_expediente(ID_Estado, Nombre_Estado, Descripcion)values(8,'Enviado','Primer paso del expediente');
insert into estado_expediente(ID_Estado, Nombre_Estado, Descripcion)values(9,'Cerrado','Primer paso del expediente');

--insertamos los estados siguientes
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(1,2,'A');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(1,3,'D');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(2,3,'A');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(3,4,'A');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(3,5,'D');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(4,5,'A');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(4,3,'A');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(5,6,'A');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(5,3,'A');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(5,6,'D');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(6,8,'A');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(6,7,'D');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(7,8,'D');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(8,9,'A');
insert into estado_siguiente(ID_Estado, ID_Estado_Siguiente , Modo)values(8,9,'D');

-- Insertamos el acl y el permiso de Login para la aplicacion de Registro Expediente
insert into appgeopista (appid, def) values (10, 'RegistroExpedientes');
insert into acl (idacl, name) values (15, 'RegistroExpedientes'); 
insert into usrgrouperm (idperm, def) values (10060, 'Catastro.RegistroExpediente.Login');
insert into usrgrouperm (idperm, def) values (10070, 'Catastro.RegistroExpediente.Tecnico');
insert into usrgrouperm (idperm, def) values (10080, 'Catastro.RegistroExpediente.Admin');
insert into r_acl_perm (idperm, idacl) values (10060, 15);
insert into r_acl_perm (idperm, idacl) values (10070, 15);
insert into r_acl_perm (idperm, idacl) values (10080, 15);


--PERMISOS AL USUARIO SYSSUPERUSER
insert into r_usr_perm(userid,idperm,idacl,aplica) values ('100','10060','15','1');
insert into r_usr_perm(userid,idperm,idacl,aplica) values ('100','10080','15','1');


-- Inserta una configuracion inicial.
insert into configuracion(Frecuencia_Actualizacion,Frecuencia_Envio,Convenio,Modo_Trabajo,Tipo_Convenio,Mostrar_Aviso_Act,Mostrar_Aviso_Envio,ultima_fecha_actualizacion,ultima_fecha_envio)values(10,10,'902','A','D',1,1,current_date,current_date);


-- Inserta los tipos de Expetiente
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (1,'901N','Cambio de dominio','901');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (2,'CIBI','Correcciones al sujeto pasivo del IBI','901');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (3,'COCM','Corrección de errores en cargo convenio','901');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (4,'CPAC','Correcciones durante procedimientos de valoración colectiva','901');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (5,'MOBD','Subsanación de discrepancias','901');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (6,'RECJ','Recursos con consecuencias jurídicas','901');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (7,'SITM','Solicitud de incorporación de titulares','901');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (8,'902C','Comunicacion Altas, Ampliaciones y Construcciones','902');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (9,'903C','Comunicacion Segregaciones y Divisiones','902');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (10,'904C','Comunicacion Cambios de Uso y Demoliciones','902');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (11,'902N','Altas, Ampliaciones y Construcciones','902');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (12,'903N','Segregaciones y Divisiones','902');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (13,'904N','Cambios de Uso y Demoliciones','902');
insert into Tipo_Expediente (ID_Tipo_Expediente,Codigo_Tipo_Expediente,Descripcion,Convenio) VALUES (14,'RECF','Recursos Fisicos Economicos','902');


               
