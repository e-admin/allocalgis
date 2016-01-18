update query_catalog set query='Select id, name, nombrecompleto, password, remarks,mail, deptid FROM IUSERUSERHDR where borrado!=1 and (id_municipio=? or id_municipio is null)'
where id='allusuarios';
update iuseruserhdr set id_municipio=null where name='SYSSUPERUSER';

insert into acl (idacl,	name) values (12,	'Capas de parcelas');
insert into acl (idacl,	name) values (13,	'Capa planes urbanisticos');

insert into usrgrouperm (idperm,def) values ('7',	'Geopista.Licencias.View');
insert into usrgrouperm (idperm,def) values ('200',	'Geopista.Licencias.Login');
insert into usrgrouperm (idperm,def) values ('201',	'Geopista.Licencias.Consulta');
insert into usrgrouperm (idperm,def) values ('202',	'Geopista.Licencias.Creacion');
insert into usrgrouperm (idperm,def) values ('203',	'Geopista.Licencias.Modificacion');
insert into usrgrouperm (idperm,def) values ('204',	'Geopista.Licencias.Informes');
insert into usrgrouperm (idperm,def) values ('205',	'Geopista.Licencias.Planos');
insert into usrgrouperm (idperm,def) values ('206',	'Geopista.Licencias.Historico');
insert into usrgrouperm (idperm,def) values ('207',	'Geopista.Licencias.Idiomas');
insert into usrgrouperm (idperm,def) values ('3000',	'Geopista.Licencias.Obra_Mayor.Abrir_Expediente');	
insert into usrgrouperm (idperm,def) values ('3010',	'Geopista.Licencias.Obra_Mayor.Solicitar_Informes');	
insert into usrgrouperm (idperm,def) values ('3020',	'Geopista.Licencias.Obra_Mayor.Mejorar_Datos');	
insert into usrgrouperm (idperm,def) values ('3030',	'Geopista.Licencias.Obra_Mayor.Esperar_Informes');	
insert into usrgrouperm (idperm,def) values ('3040',	'Geopista.Licencias.Obra_Mayor.Emitir_Informe_Resolución');	
insert into usrgrouperm (idperm,def) values ('3050',	'Geopista.Licencias.Obra_Mayor.Emitir_Propuesta_Resolución');	
insert into usrgrouperm (idperm,def) values ('3060',	'Geopista.Licencias.Obra_Mayor.Esperar_Alegaciones');	
insert into usrgrouperm (idperm,def) values ('3070',	'Geopista.Licencias.Obra_Mayor.Actualizar_Resolución');	
insert into usrgrouperm (idperm,def) values ('3080',	'Geopista.Licencias.Obra_Mayor.Formalizar_Licencia');	
insert into usrgrouperm (idperm,def) values ('3090',	'Geopista.Licencias.Obra_Mayor.Notificar_Aprobación');	
insert into usrgrouperm (idperm,def) values ('3100',	'Geopista.Licencias.Obra_Mayor.Notificar_Denegación');	
insert into usrgrouperm (idperm,def) values ('3110',	'Geopista.Licencias.Obra_Mayor.Seguimiento_Ejecución');	
insert into usrgrouperm (idperm,def) values ('3120',	'Geopista.Licencias.Obra_Mayor.Actualizar_Expedientes_Durmientes');	
insert into usrgrouperm (idperm,def) values ('4000',	'Geopista.Layer.Leer');	
insert into usrgrouperm (idperm,def) values ('5030',	'Geopista.Licencias.Eventos');
insert into usrgrouperm (idperm,def) values ('5040',	'Geopista.Licencias.Notificacion');

insert into usrgrouperm (idperm,def) values ('4010',	'Geopista.Layer.Escribir');	
insert into usrgrouperm (idperm,def) values ('4020',	'Geopista.Layer.Añadir');	
insert into usrgrouperm (idperm,def) values ('4030',	'Geopista.Layer.ModificarSLD');	

insert into iuseruserhdr (id,name,nombrecompleto,password,flags,stat,numbadcnts,crtrid,crtndate)
values ('111',	'PRUEBAS','Usuario de pruebas',	'BWqLoIpsnsk=',0,0,0,0, TO_Date( '06/21/2004 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'));	

update acl set name='Licencias de Obra' where idacl=2;
insert into r_acl_perm (idperm,	idacl) values (7,2);
insert into r_acl_perm (idperm,	idacl) values (200,2);
insert into r_acl_perm (idperm,	idacl) values (201,2);
insert into r_acl_perm (idperm,	idacl) values (202,2);
insert into r_acl_perm (idperm,	idacl) values (203,2);
insert into r_acl_perm (idperm,	idacl) values (204,2);
insert into r_acl_perm (idperm,	idacl) values (205,2);
insert into r_acl_perm (idperm,	idacl) values (206,2);
insert into r_acl_perm (idperm,	idacl) values (207,2);
insert into r_acl_perm (idperm,	idacl) values (3000,	2);
insert into r_acl_perm (idperm,	idacl) values (3010,	2);
insert into r_acl_perm (idperm,	idacl) values (3020,	2);
insert into r_acl_perm (idperm,	idacl) values (3030,	2);
insert into r_acl_perm (idperm,	idacl) values (3040,	2);
insert into r_acl_perm (idperm,	idacl) values (3050,	2);
insert into r_acl_perm (idperm,	idacl) values (3060,	2);
insert into r_acl_perm (idperm,	idacl) values (3070,	2);
insert into r_acl_perm (idperm,	idacl) values (3080,	2);
insert into r_acl_perm (idperm,	idacl) values (3090,	2);
insert into r_acl_perm (idperm,	idacl) values (3100,	2);
insert into r_acl_perm (idperm,	idacl) values (3110,	2);
insert into r_acl_perm (idperm,	idacl) values (3120,	2);
insert into r_acl_perm (idperm,	idacl) values (5030,	2);
insert into r_acl_perm (idperm,	idacl) values (5040,	2);

insert into r_acl_perm (idperm,	idacl) values (4000,	12);
insert into r_acl_perm (idperm,	idacl) values (4000,	13);
insert into r_acl_perm (idperm,	idacl) values (4010,	12);
insert into r_acl_perm (idperm,	idacl) values (4010,	13);
insert into r_acl_perm (idperm,	idacl) values (4020,	12);
insert into r_acl_perm (idperm,	idacl) values (4020,	13);
insert into r_acl_perm (idperm,	idacl) values (4030,	13);
insert into r_acl_perm (idperm,	idacl) values (4030,	12);

insert into r_usr_perm (userid,idperm,idacl,aplica) values (99	,206,	2	,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	869,	8,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	867,	8,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99	,866,	8,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99	,865,	8	,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	864,8,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	863,8,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	862,8,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	200,2,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	201,2,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	202,2,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	203,2,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	855,8,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	4010,13,0);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	205,2,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	870,8,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	7,2,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	20,2,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	450,4,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	453,4,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	10,1,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	11,1,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	12,1,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	303,3,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	413,4,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	416,4,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	801,8,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	401,4,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	204,2,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	4000,13,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	868,8	,1);


insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	'200',	'2',	'1');
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	7,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	206,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	205,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	204,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	203,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	207,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	201,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	10,	1,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	202,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3040,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3120,	2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	11	,1,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	12	,1,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3090	,2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3060	,2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3030	,2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3000	,2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3110	,2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3080	,2,	1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',3050,2,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3020,	2	,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3100,	2	,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values ('111',	3010,	2	,1);
insert into r_usr_perm (userid,idperm,idacl,aplica) values (99,	4000,	13,	1);






-- Permisos para metadatos
insert into acl values (11, 'Metadatos');
insert into usrgrouperm (idperm,def) values ('5000',	'Geopista.Metadatos.Login');	
insert into usrgrouperm (idperm,def) values ('5010',	'Geopista.Metadatos.View');	
insert into usrgrouperm (idperm,def) values ('5020',	'Geopista.Metadatos.Edit');	
insert into r_acl_perm (idperm,	idacl) values (5000,	11);
insert into r_acl_perm (idperm,	idacl) values (5010,	11);
insert into r_acl_perm (idperm,	idacl) values (5020,	11);

-- Permisos para actividades contaminantes
insert into acl values (20, 'Contaminantes');
insert into usrgrouperm (idperm,def) values ('6000',	'Geopista.Contaminantes.Login');	
insert into usrgrouperm (idperm,def) values ('6010',	'Geopista.Contaminantes.View');	
insert into usrgrouperm (idperm,def) values ('6020',	'Geopista.Contaminantes.Edit');	
insert into r_acl_perm (idperm,	idacl) values (6000,	20);
insert into r_acl_perm (idperm,	idacl) values (6010,	20);
insert into r_acl_perm (idperm,	idacl) values (6020,	20);



-- Permisos para Ocupaciones
insert into acl values (21, 'Ocupaciones');
insert into usrgrouperm (idperm,def) values ('7001',	'Geopista.Ocupaciones.Login');	
insert into usrgrouperm (idperm,def) values ('7002',	'Geopista.Ocupaciones.View');	
insert into usrgrouperm (idperm,def) values ('7003',	'Geopista.Ocupaciones.Edit');
insert into usrgrouperm (idperm,def) values ('7004',	'Geopista.Ocupaciones.Consulta');
insert into usrgrouperm (idperm,def) values ('7005',	'Geopista.Ocupaciones.Creacion');
insert into usrgrouperm (idperm,def) values ('7006',	'Geopista.Ocupaciones.Modificacion');
insert into usrgrouperm (idperm,def) values ('7007',	'Geopista.Ocupaciones.Informes');
insert into usrgrouperm (idperm,def) values ('7008',	'Geopista.Ocupaciones.Planos');
insert into usrgrouperm (idperm,def) values ('7009',	'Geopista.Ocupaciones.Historico');
insert into usrgrouperm (idperm,def) values ('7010',	'Geopista.Ocupaciones.Idiomas');
insert into usrgrouperm (idperm,def) values ('7011',	'Geopista.Ocupaciones.Eventos');
insert into usrgrouperm (idperm,def) values ('7012',	'Geopista.Ocupaciones.Notificacion');
insert into usrgrouperm (idperm,def) values ('7050',	'Geopista.Ocupaciones.Apertura de expediente de ocupación');	
insert into usrgrouperm (idperm,def) values ('7051',	'Geopista.Ocupaciones.Tramitación de expediente de ocupación');	
insert into usrgrouperm (idperm,def) values ('7052',	'Geopista.Ocupaciones.Cierre de expediente de ocupación');	
insert into r_acl_perm (idperm,	idacl) values (7001,	21);
insert into r_acl_perm (idperm,	idacl) values (7002,	21);
insert into r_acl_perm (idperm,	idacl) values (7003,	21);
insert into r_acl_perm (idperm,	idacl) values (7004,	21);
insert into r_acl_perm (idperm,	idacl) values (7005,	21);
insert into r_acl_perm (idperm,	idacl) values (7006,	21);
insert into r_acl_perm (idperm,	idacl) values (7007,	21);
insert into r_acl_perm (idperm,	idacl) values (7008,	21);
insert into r_acl_perm (idperm,	idacl) values (7009,	21);
insert into r_acl_perm (idperm,	idacl) values (7010,	21);
insert into r_acl_perm (idperm,	idacl) values (7011,	21);
insert into r_acl_perm (idperm,	idacl) values (7012,	21);
insert into r_acl_perm (idperm,	idacl) values (7050,	21);
insert into r_acl_perm (idperm,	idacl) values (7051,	21);
insert into r_acl_perm (idperm,	idacl) values (7052,	21);



