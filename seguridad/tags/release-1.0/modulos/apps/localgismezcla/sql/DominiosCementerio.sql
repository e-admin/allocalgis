-- Insertamos el acl y el permiso de Login para la aplicacion de Cementerio
insert into appgeopista (appid, def) values (13, 'Cementerios');  --> El 13 es el siguiente identificador libre
insert into acl (idacl, name) values (71, 'Cementerios'); --> El 71 es un hueco
insert into usrgrouperm (idperm, def) values (2000, 'Geopista.Cementerios.Login');  --> El 2000 es un hueco
insert into r_acl_perm (idperm, idacl) values (2000, 71);

-- Insertamos en el rol del superuser el permiso de login
insert into r_group_perm (groupid, idperm, idacl) values (110,2000,71);
insert into r_usr_perm (userid, idperm, idacl) values (100, 2000, 71);

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de Cementerios');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de Cementerios');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de Cementerios');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de Cementerios');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de Cementerios');

insert into domaincategory (id, id_description) values (11, CURRVAL('seq_dictionary')); --> El 11 es un hueco
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Gestión de cementerios', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);
SET @idDomainNodeGestCement= select currval('seq_domainnodes') from seq_domainnodes; --210000

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de Elementos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de Elementos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de Elementos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de Elementos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de Elementos');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestCement);
SET @idDomainNodeGestElem= select currval('seq_domainnodes') from seq_domainnodes; --210001

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de la Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de la Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de la Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de la Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de la Propiedad');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestCement);
SET @idDomainNodeGestProp= select currval('seq_domainnodes') from seq_domainnodes; --210002

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Históricos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Históricos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Históricos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Históricos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Históricos');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestCement);
SET @idDomainNodeHisto= select currval('seq_domainnodes') from seq_domainnodes; --210003

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de Difuntos');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '4', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestCement);
SET @idDomainNodeGestDifun= select currval('seq_domainnodes') from seq_domainnodes; --210004

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Gestión de Intervenciones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Gestión de Intervenciones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Gestión de Intervenciones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Gestión de Intervenciones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Gestión de Intervenciones');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '5', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestCement);
SET @idDomainNodeGestInter= select currval('seq_domainnodes') from seq_domainnodes; --210005


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo de Elementos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo de Elementos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo de Elementos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo de Elementos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo de Elementos');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo de Elementos', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1, @idDomainNodeGestElem);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Bloque');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Bloque');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Bloque');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Bloque');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Bloque');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestElem);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'U.Enterramiento');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]U.Enterramiento');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]U.Enterramiento');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]U.Enterramiento');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]U.Enterramiento');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestElem);


insert into query_catalog (id,query,acl,idperm) values ('getestructuraCem', 'select domains.id as id_domain ,domainnodes.id as id_node, domainnodes.pattern as pattern, dictionary.locale as locale, 
dictionary.id_vocablo as id_descripcion, dictionary.traduccion 
as traduccion, domainnodes.parentdomain as parentdomain   
from domains,domainnodes,dictionary where upper(domains.name)=upper(?) and domainnodes.type=?
and domainnodes.id_domain=domains.id and domainnodes.id_description= dictionary.id_vocablo
and domainnodes.id_municipio is null order by domainnodes.id', 1, 9205);

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo G.Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo G.Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo G.Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo G.Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo G.Propiedad');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo G.Propiedad', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1, @idDomainNodeGestProp);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Titular');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Titular');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Titular');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Titular');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Titular');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestProp);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Concesiones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Concesiones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Concesiones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Concesiones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Concesiones');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestProp);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tarifas');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tarifas');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tarifas');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tarifas');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tarifas');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '4', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestProp);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Servicios');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Servicios');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Servicios');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Servicios');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Servicios');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Servicios', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1, @idDomainNodeHisto);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Histórico Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Histórico Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Histórico Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Histórico Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Histórico Difuntos');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeHisto);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Histórico Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Histórico Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Histórico Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Histórico Propiedad');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Histórico Propiedad');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeHisto);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Gestión Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Gestión Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Gestión Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Gestión Difuntos');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Gestión Difuntos');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Gestion Difuntos', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1, @idDomainNodeGestDifun);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Defunción');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Defunción');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Defunción');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Defunción');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Defunción');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestDifun);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Inhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Inhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Inhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Inhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Inhumación');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestDifun);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Exhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Exhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Exhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Exhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Exhumación');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestDifun);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tarifas');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tarifas');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tarifas');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tarifas');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tarifas');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '4', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestDifun);


--*****************************************************************
--TIPO INTERVENCION--
--*****************************************************************
insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Intervenciones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Intervenciones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Intervenciones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Intervenciones');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Intervenciones');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Intervenciones', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1, @idDomainNodeGestInter);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Intervención');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Intervención');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Intervención');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Intervención');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Intervención');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeGestInter);


--*****************************************************************
--TIPO UNIDADES ENTERRAMIENTO--
--*****************************************************************
insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Unidad Enterramiento');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipus Unitat enterrament');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo de Unidade enterro');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Unitate mota ehorzketa');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]ipus Unitat enterrament');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Unidades', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel)values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);
SET @idDomainNodeTipoUE= select currval('seq_domainnodes') from seq_domainnodes; --210040


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Panteón');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Panteó');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Panteón');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Panteoia');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Panteó');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoUE);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Mausoleo');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Mausoleu');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Mausoleo');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Mausoleoa');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Mausoleu');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoUE);

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Sepultura o fosa');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Sepultura o fossa');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Cava ou pit');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Ehorzketa edo hobia');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Sepultura o fossa');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoUE);

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Nicho');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Nínxol');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Nicho');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Nitxo');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Nínxol');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '4', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoUE);

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Columbario');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Columbari');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Columbário');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Kolunbarioak');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Columbari');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '5', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoUE);

--*****************************************************************
--TIPO CONCESIONES--
--*****************************************************************
insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Concesion');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipus Concessio');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo de Concesión');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Emakida-mota');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipus Concessio');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Concesiones', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);
SET @idDomainNodeTipoConce= select currval('seq_domainnodes') from seq_domainnodes; --210046


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Concesión renovable');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Concessió renovable');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Renovables Grant');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Berriztagarrien Grant');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Concessió renovable');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoConce);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Concesión no renovable');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Concessió no renovable');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]concesión non renovables');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Ez-berriztagarrien beka');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Concessió no renovable');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoConce);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Alquiler');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Lloguer');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Aparcamento');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Parking');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Lloguer');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoConce);

--*****************************************************************
--TIPO CONTENEDORES--
--*****************************************************************
insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Contenedor Difunto');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Contenedor Difunto');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Contenedor Difunto');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Contenedor Difunto');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Contenedor Difunto');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo contenedores difunto', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel)values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);
SET @idDomainNodeTipoConten= select currval('seq_domainnodes') from seq_domainnodes; --210050


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Ataúd');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Ataúd');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Ataúd');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Ataúd');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Ataúd');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoConten);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Urna');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Urna');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Urna');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Urna');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Urna');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoConten);


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Caja Cremación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Caja Cremación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Caja Cremación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Caja Cremación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Caja Cremación');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoConten);


--*****************************************************************
--TIPO INHUMACIONES--
--*****************************************************************
insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Inhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Inhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Inhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Inhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Inhumación');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Inhumacion', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);
SET @idDomainNodeTipoInhum= select currval('seq_domainnodes') from seq_domainnodes; --210055

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tierra');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tierra');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tierra');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tierra');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tierra');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoInhum);

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Bóveda');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Bóveda');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Bóveda');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Bóveda');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Bóveda');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoInhum);

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Cremación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Cremación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Cremación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Cremación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Cremación'); 
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '3', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoInhum);


--*****************************************************************
--TIPO EXHUMACIONES--
--*****************************************************************
insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Tipo Exhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Tipo Exhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Tipo Exhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Tipo Exhumación');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Tipo Exhumación');
insert into domains (id, name, idacl, id_category) values (NEXTVAL('seq_domains'), 'Tipo Exhumación', 71, 11);
insert into domainnodes (id, id_domain, id_description, type, permissionlevel) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), CURRVAL('seq_dictionary'), 4, 1);
SET @idDomainNodeTipoExhum= select currval('seq_domainnodes') from seq_domainnodes; --210060


insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Ordinaria');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Ordinaria');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Ordinaria');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Ordinaria');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Ordinaria');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '1', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoExhum);

insert into dictionary (id_vocablo, locale, traduccion) values (NEXTVAL('seq_dictionary'), 'es_ES', 'Extraordinaria');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]Extraordinaria');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]Extraordinaria');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]Extraordinaria');
insert into dictionary (id_vocablo, locale, traduccion) values (CURRVAL('seq_dictionary'), 'va_ES', '[va]Extraordinaria');
insert into domainnodes (id, id_domain, pattern, id_description, type, permissionlevel, parentdomain) values (NEXTVAL('seq_domainnodes'), CURRVAL('seq_domains'), '2', CURRVAL('seq_dictionary'), 7, 1, @idDomainNodeTipoExhum);

-- Rellenar los tipos de los documentos
insert into cementerio.documento_tipos (tipo, extension, mime_type) (select tipo, extension, mime_type from documento_tipos);

