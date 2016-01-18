--Permisos y roles para la autenticación de los usuarios en el módulo de LocalGIS EIEL 

--insert into usrgrouperm(idperm,def) values (nextval('seq_acl_perm'), 'LocalGis.EIEL.Login'); 
--insert into acl(idacl,name) values(nextval('seq_acl','EIEL');
--insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
--insert into iuseruserhdr(id,name,nombrecompleto,password,flags,stat,numbadcnts,crtrid) values (204,'EIEL','EIEL','PggP3/BOfCFsFDgX6iL9gA==',0,0,0,100);
--insert into r_usr_perm(userid,idperm,idacl,aplica) values (204,currval('seq_acl_perm'),currval('seq_acl'),1);
--insert into iusergrouphdr(id,name,mgrid,type,remarks,crtrid,crtndate) values (116,'EIEL',100,0,'Rol para Módulo de Planeamiento',100,'27/11/2007');
--insert into iusergroupuser(groupid,userid) values (116,204);
--insert into r_usr_perm(userid,idperm,idacl,aplica) values (100,currval('seq_acl_perm'),currval('seq_acl'),1);


--insert into usrgrouperm(idperm,def) values (nextval('seq_acl_perm'), 'LocalGis.edicion.EIEL');
--insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
--insert into r_usr_perm(userid,idperm,idacl,aplica) values (204,currval('seq_acl_perm'),currval('seq_acl'),1);
--insert into r_usr_perm(userid,idperm,idacl,aplica) values (100,currval('seq_acl_perm'),currval('seq_acl'),1);


insert into Domains (ID, NAME,ID_CATEGORY)values(MAX(Domains.id)+1,'Tablas EIEL',null);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1651,'es_ES','Tablas EIEL');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1840,'10178',null,1651,2,null,null);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'es_ES','Abastecimiento de Agua');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'ga_ES','Abastecimiento de Agua');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'va_ES','Abastecimiento de Agua');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'eu_ES','Abastecimiento de Agua');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'ca_ES','Abastecimiento de Agua');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1842,'10178','AA',1653,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1654,'es_ES','Captaciones de Agua');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1843,'10178','eiel_t_abast_ca',1654,7,null,'1842');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1655,'es_ES','Depósitos');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1844,'10178','eiel_t_abast_de',1655,7,null,'1842');
  
 -- Generar dominio con las tablas
 insert into Domains (ID, NAME,ID_CATEGORY)values(10179,'EIEL',5);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1656,'es_ES','EIEL');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1656,'ga_ES','EIEL');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1656,'va_ES','EIEL');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1656,'eu_ES','EIEL');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1656,'ca_ES','EIEL');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1845,'10179',null,1656,2,null,null);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1657,'es_ES','Abastecimiento de Agua');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1657,'ga_ES','Abastecimiento de Agua');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1657,'va_ES','Abastecimiento de Agua');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1657,'eu_ES','Abastecimiento de Agua');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1657,'ca_ES','Abastecimiento de Agua');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1846,'10179','AA',1657,7,null,'1845');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1658,'es_ES','Saneamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1658,'ga_ES','Saneamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1658,'va_ES','Saneamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1658,'eu_ES','Saneamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1658,'ca_ES','Saneamiento');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1847,'10179','SA',1658,7,null,'1845');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1659,'es_ES','Equipamientos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1659,'ga_ES','Equipamientos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1659,'va_ES','Equipamientos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1659,'eu_ES','Equipamientos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1659,'ca_ES','Equipamientos');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1848,'10179','EQ',1659,7,null,'1845');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1660,'es_ES','Infraestructura Viaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1660,'ga_ES','Infraestructura Viaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1660,'va_ES','Infraestructura Viaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1660,'eu_ES','Infraestructura Viaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1660,'ca_ES','Infraestructura Viaria');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1849,'10179','IV',1660,7,null,'1845');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1661,'es_ES','Depuradoras 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1661,'ga_ES','Depuradoras 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1661,'va_ES','Depuradoras 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1661,'eu_ES','Depuradoras 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1661,'ca_ES','Depuradoras 1');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1850,'10179','D1',1661,7,null,'1846');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1662,'es_ES','Depuradoras2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1662,'ga_ES','Depuradoras2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1662,'va_ES','Depuradoras2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1662,'eu_ES','Depuradoras2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1662,'ca_ES','Depuradoras2');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1851,'10179','D2',1662,7,null,'1846');
delete from dictionary where id_vocablo='1661';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','ga_ES','Abastecimiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','es_ES','Abastecimiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','va_ES','Abastecimiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','eu_ES','Abastecimiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','ca_ES','Abastecimiento Autónomo');
update domainnodes set pattern='AA', id_description='1661' where id='1850';
delete from dictionary where id_vocablo='1662';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1662','ga_ES','Captaciones');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1662','es_ES','Captaciones');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1662','va_ES','Captaciones');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1662','eu_ES','Captaciones');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1662','ca_ES','Captaciones');
update domainnodes set pattern='CA', id_description='1662' where id='1851';
delete from dictionary where id_vocablo='1661';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','ga_ES','Abastecimiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','es_ES','Abastecimiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','va_ES','Abastecimiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','eu_ES','Abastecimiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1661','ca_ES','Abastecimiento Autónomo');
update domainnodes set pattern='AB', id_description='1661' where id='1850';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1663,'es_ES','Depósitos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1663,'ga_ES','Depósitos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1663,'va_ES','Depósitos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1663,'eu_ES','Depósitos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1663,'ca_ES','Depósitos');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1852,'10179','DE',1663,7,null,'1846');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1664,'es_ES','Servicios de Abastecimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1664,'ga_ES','Servicios de Abastecimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1664,'va_ES','Servicios de Abastecimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1664,'eu_ES','Servicios de Abastecimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1664,'ca_ES','Servicios de Abastecimiento');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1853,'10179','SA',1664,7,null,'1846');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1665,'es_ES','Tratamientos de Potabilización');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1665,'ga_ES','Tratamientos de Potabilización');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1665,'va_ES','Tratamientos de Potabilización');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1665,'eu_ES','Tratamientos de Potabilización');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1665,'ca_ES','Tratamientos de Potabilización');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1854,'10179','TP',1665,7,null,'1846');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1666,'es_ES','Depuradoras 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1666,'ga_ES','Depuradoras 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1666,'va_ES','Depuradoras 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1666,'eu_ES','Depuradoras 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1666,'ca_ES','Depuradoras 1');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1855,'10179','D1',1666,7,null,'1847');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1667,'es_ES','Depuradoras 2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1667,'ga_ES','Depuradoras 2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1667,'va_ES','Depuradoras 2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1667,'eu_ES','Depuradoras 2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1667,'ca_ES','Depuradoras 2');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1856,'10179','D2',1667,7,null,'1847');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1668,'es_ES','Saneamiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1668,'ga_ES','Saneamiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1668,'va_ES','Saneamiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1668,'eu_ES','Saneamiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1668,'ca_ES','Saneamiento Autónomo');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1857,'10179','SA',1668,7,null,'1847');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1669,'es_ES','Puntos de Vertido');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1669,'ga_ES','Puntos de Vertido');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1669,'va_ES','Puntos de Vertido');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1669,'eu_ES','Puntos de Vertido');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1669,'ca_ES','Puntos de Vertido');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1858,'10179','PV',1669,7,null,'1847');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1670,'es_ES','Servicios de Saneamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1670,'ga_ES','Servicios de Saneamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1670,'va_ES','Servicios de Saneamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1670,'eu_ES','Servicios de Saneamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1670,'ca_ES','Servicios de Saneamiento');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1859,'10179','SS',1670,7,null,'1847');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1671,'es_ES','Centros Asistenciales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1671,'ga_ES','Centros Asistenciales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1671,'va_ES','Centros Asistenciales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1671,'eu_ES','Centros Asistenciales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1671,'ca_ES','Centros Asistenciales');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1860,'10179','CA',1671,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1672,'es_ES','Cabildo o Consejo Insular');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1672,'ga_ES','Cabildo o Consejo Insular');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1672,'va_ES','Cabildo o Consejo Insular');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1672,'eu_ES','Cabildo o Consejo Insular');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1672,'ca_ES','Cabildo o Consejo Insular');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1861,'10179','CI',1672,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1673,'es_ES','Casas Consistoriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1673,'ga_ES','Casas Consistoriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1673,'va_ES','Casas Consistoriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1673,'eu_ES','Casas Consistoriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1673,'ca_ES','Casas Consistoriales');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1862,'10179','CC',1673,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1674,'es_ES','Cementerios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1674,'ga_ES','Cementerios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1674,'va_ES','Cementerios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1674,'eu_ES','Cementerios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1674,'ca_ES','Cementerios');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1863,'10179','CE',1674,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1675,'es_ES','Centros Culturales y de Esparcimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1675,'ga_ES','Centros Culturales y de Esparcimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1675,'va_ES','Centros Culturales y de Esparcimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1675,'eu_ES','Centros Culturales y de Esparcimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1675,'ca_ES','Centros Culturales y de Esparcimiento');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1864,'10179','CU',1675,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1676,'es_ES','Centros de Enseñanza');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1676,'ga_ES','Centros de Enseñanza');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1676,'va_ES','Centros de Enseñanza');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1676,'eu_ES','Centros de Enseñanza');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1676,'ca_ES','Centros de Enseñanza');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1865,'10179','EN',1676,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1677,'es_ES','Instalaciones Deportivas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1677,'ga_ES','Instalaciones Deportivas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1677,'va_ES','Instalaciones Deportivas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1677,'eu_ES','Instalaciones Deportivas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1677,'ca_ES','Instalaciones Deportivas');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1866,'10179','ID',1677,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1678,'es_ES','Centros de Extinción de Incendios y Protección Civil');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1678,'ga_ES','Centros de Extinción de Incendios y Protección Civil');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1678,'va_ES','Centros de Extinción de Incendios y Protección Civil');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1678,'eu_ES','Centros de Extinción de Incendios y Protección Civil');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1678,'ca_ES','Centros de Extinción de Incendios y Protección Civil');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1867,'10179','PI',1678,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1679,'es_ES','Lonjas, Mercados y Recintos Feriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1679,'ga_ES','Lonjas, Mercados y Recintos Feriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1679,'va_ES','Lonjas, Mercados y Recintos Feriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1679,'eu_ES','Lonjas, Mercados y Recintos Feriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1679,'ca_ES','Lonjas, Mercados y Recintos Feriales');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1868,'10179','LO',1679,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1680,'es_ES','Mataderos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1680,'ga_ES','Mataderos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1680,'va_ES','Mataderos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1680,'eu_ES','Mataderos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1680,'ca_ES','Mataderos');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1869,'10179','MA',1680,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1681,'es_ES','Parques, Jardines y Áreas Naturales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1681,'ga_ES','Parques, Jardines y Áreas Naturales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1681,'va_ES','Parques, Jardines y Áreas Naturales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1681,'eu_ES','Parques, Jardines y Áreas Naturales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1681,'ca_ES','Parques, Jardines y Áreas Naturales');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1870,'10179','PA',1681,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1682,'es_ES','Centros Sanitarios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1682,'ga_ES','Centros Sanitarios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1682,'va_ES','Centros Sanitarios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1682,'eu_ES','Centros Sanitarios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1682,'ca_ES','Centros Sanitarios');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1871,'10179','SA',1682,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1683,'es_ES','Edificios sin Uso');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1683,'ga_ES','Edificios sin Uso');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1683,'va_ES','Edificios sin Uso');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1683,'eu_ES','Edificios sin Uso');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1683,'ca_ES','Edificios sin Uso');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1872,'10179','ES',1683,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1684,'es_ES','Tanatorios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1684,'ga_ES','Tanatorios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1684,'va_ES','Tanatorios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1684,'eu_ES','Tanatorios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1684,'ca_ES','Tanatorios');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1873,'10179','TA',1684,7,null,'1848');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1685,'es_ES','Otras Dotaciones');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1685,'ga_ES','Otras Dotaciones');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1685,'va_ES','Otras Dotaciones');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1685,'eu_ES','Otras Dotaciones');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1685,'ca_ES','Otras Dotaciones');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1874,'10179','OD',1685,7,null,'1845');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1686,'es_ES','Vertederos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1686,'ga_ES','Vertederos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1686,'va_ES','Vertederos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1686,'eu_ES','Vertederos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1686,'ca_ES','Vertederos');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1875,'10179','VE',1686,7,null,'1874');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1687,'es_ES','Servicios de Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1687,'ga_ES','Servicios de Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1687,'va_ES','Servicios de Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1687,'eu_ES','Servicios de Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1687,'ca_ES','Servicios de Recogida de Basuras');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1876,'10179','RB',1687,7,null,'1874');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1688,'es_ES','Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1688,'ga_ES','Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1688,'va_ES','Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1688,'eu_ES','Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1688,'ca_ES','Recogida de Basuras');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1877,'10179','RB',1688,7,null,'1874');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1689,'es_ES','Planeamiento Urbano');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1689,'ga_ES','Planeamiento Urbano');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1689,'va_ES','Planeamiento Urbano');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1689,'eu_ES','Planeamiento Urbano');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1689,'ca_ES','Planeamiento Urbano');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1878,'10179','PU',1689,7,null,'1874');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1690,'es_ES','Datos de Núcleo y Municipios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1690,'ga_ES','Datos de Núcleo y Municipios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1690,'va_ES','Datos de Núcleo y Municipios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1690,'eu_ES','Datos de Núcleo y Municipios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1690,'ca_ES','Datos de Núcleo y Municipios');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1879,'10179','NM',1690,7,null,'1845');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1691,'es_ES','Información Términos Municipales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1691,'ga_ES','Información Términos Municipales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1691,'va_ES','Información Términos Municipales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1691,'eu_ES','Información Términos Municipales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1691,'ca_ES','Información Términos Municipales');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1880,'10179','TM',1691,7,null,'1879');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1692,'es_ES','Diseminados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1692,'ga_ES','Diseminados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1692,'va_ES','Diseminados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1692,'eu_ES','Diseminados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1692,'ca_ES','Diseminados');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1881,'10179','DI',1692,7,null,'1879');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1693,'es_ES','Propiedades de Núcleos Encuestados 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1693,'ga_ES','Propiedades de Núcleos Encuestados 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1693,'va_ES','Propiedades de Núcleos Encuestados 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1693,'eu_ES','Propiedades de Núcleos Encuestados 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1693,'ca_ES','Propiedades de Núcleos Encuestados 1');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1882,'10179','P1',1693,7,null,'1879');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1694,'es_ES','Propiedades de Núcleos Encuestados 2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1694,'ga_ES','Propiedades de Núcleos Encuestados 2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1694,'va_ES','Propiedades de Núcleos Encuestados 2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1694,'eu_ES','Propiedades de Núcleos Encuestados 2');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1694,'ca_ES','Propiedades de Núcleos Encuestados 2');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1883,'10179','P2',1694,7,null,'1879');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1695,'es_ES','Núcleos Abandonados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1695,'ga_ES','Núcleos Abandonados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1695,'va_ES','Núcleos Abandonados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1695,'eu_ES','Núcleos Abandonados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1695,'ca_ES','Núcleos Abandonados');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1884,'10179','NA',1695,7,null,'1879');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1696,'es_ES','Núcleos de Población');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1696,'ga_ES','Núcleos de Población');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1696,'va_ES','Núcleos de Población');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1696,'eu_ES','Núcleos de Población');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1696,'ca_ES','Núcleos de Población');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1885,'10179','NP',1696,7,null,'1879');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1697,'es_ES','Otros Servicios Municipales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1697,'ga_ES','Otros Servicios Municipales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1697,'va_ES','Otros Servicios Municipales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1697,'eu_ES','Otros Servicios Municipales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1697,'ca_ES','Otros Servicios Municipales');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1886,'10179','OS',1697,7,null,'1879');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1698,'es_ES','Padrón por Núcleos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1698,'ga_ES','Padrón por Núcleos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1698,'va_ES','Padrón por Núcleos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1698,'eu_ES','Padrón por Núcleos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1698,'ca_ES','Padrón por Núcleos');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1887,'10179','PN',1698,7,null,'1879');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1699,'es_ES','Padrón por Municipios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1699,'ga_ES','Padrón por Municipios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1699,'va_ES','Padrón por Municipios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1699,'eu_ES','Padrón por Municipios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1699,'ca_ES','Padrón por Municipios');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1888,'10179','PM',1699,7,null,'1879');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1700,'es_ES','Poblamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1700,'ga_ES','Poblamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1700,'va_ES','Poblamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1700,'eu_ES','Poblamiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1700,'ca_ES','Poblamiento');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1889,'10179','PO',1700,7,null,'1879');
  
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'es_ES','Carreteras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'ga_ES','Carreteras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'va_ES','Carreteras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'eu_ES','Carreteras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'ca_ES','Carreteras');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1890,'10179','CR',1701,7,null,'1849');
  
  
delete from dictionary where id_vocablo='1664';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1664','es_ES','Servicios de Abastecimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1664','ga_ES','Servicios de Abastecimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1664','va_ES','Servicios de Abastecimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1664','eu_ES','Servicios de Abastecimiento');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1664','ca_ES','Servicios de Abastecimiento');
update domainnodes set pattern='SE', id_description='1664' where id='1853';
delete from dictionary where id_vocablo='1668';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1668','es_ES','Saneamiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1668','ga_ES','Saneamiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1668','va_ES','Saneamiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1668','eu_ES','Saneamiento Autónomo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1668','ca_ES','Saneamiento Autónomo');
update domainnodes set pattern='SN', id_description='1668' where id='1857';
delete from dictionary where id_vocablo='1671';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1671','es_ES','Centros Asistenciales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1671','ga_ES','Centros Asistenciales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1671','va_ES','Centros Asistenciales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1671','eu_ES','Centros Asistenciales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1671','ca_ES','Centros Asistenciales');
update domainnodes set pattern='AS', id_description='1671' where id='1860';
delete from dictionary where id_vocablo='1682';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1682','es_ES','Centros Sanitarios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1682','ga_ES','Centros Sanitarios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1682','va_ES','Centros Sanitarios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1682','eu_ES','Centros Sanitarios');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1682','ca_ES','Centros Sanitarios');
update domainnodes set pattern='CS', id_description='1682' where id='1871';
delete from dictionary where id_vocablo='1687';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1687','es_ES','Servicios de Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1687','ga_ES','Servicios de Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1687','va_ES','Servicios de Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1687','eu_ES','Servicios de Recogida de Basuras');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1687','ca_ES','Servicios de Recogida de Basuras');
update domainnodes set pattern='SR', id_description='1687' where id='1876';
delete from dictionary where id_vocablo='1656';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1656','ga_ES','EIEL');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1656','es_ES','EIEL');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1656','va_ES','EIEL');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1656','eu_ES','EIEL');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1656','ca_ES','EIEL');
update domainnodes set pattern=null, id_description='1656' where id='1845';
 
 
 -- Añadir campo bloqueado en cada una de las tablas de infromación alfanumérica 
  
  ALTER TABLE eiel_t_abast_au ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_abast_ca ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t1_saneam_ed ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t2_saneam_ed ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_cc ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_cu ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_en ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_id ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_nucleos_poblacion ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_otros_serv_munic ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_padron_nd ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_padron_ttmm ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_pj ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_planeam_urban ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_poblamiento ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_rb ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_sa ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_saneam_au ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_saneam_serv ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_srb ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_su ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_ta ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_vt ADD COLUMN bloqueado varchar(32);
  ALTER TABLE eiel_t_carreteras ADD COLUMN bloqueado varchar(32);
    
  ALTER TABLE eiel_t_abast_de ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_saneam_pv ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_abast_serv ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_as ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_cabildo_consejo ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_ce ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_entidad_singular ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_inf_ttmm ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_ip ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_lm ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_mt ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_abast_tp ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_mun_diseminados ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_nucl_encuest_1 ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_nucl_encuest_2 ADD COLUMN bloqueado varchar(32);
ALTER TABLE eiel_t_nucleo_aband ADD COLUMN bloqueado varchar(32);

ALTER TABLE eiel_t_id_deportes ADD COLUMN bloqueado varchar(32);
  
  -- Modificación  y añadidos para la carga de municipios y provincias
 update query_catalog set query='select id, id_provincia, id_catastro, id_ine, nombreoficial from municipios where id_provincia=? order by id_ine' where id='MCobtenerListaZonaValor';
 insert into query_catalog (id, query, acl, idperm) values ('EobtenerEntidades','select codentidad, codprov, codmunic from eiel_t_entidad_singular where codprov=? and codmunic=?',1,9205);
 insert into query_catalog (id, query, acl, idperm) values ('EobtenerNucleos','select codpoblamiento from eiel_c_nucleo_poblacion where codprov=? and codmunic=? and codentidad=?',1,9205);

 ALTER TABLE maps ADD COLUMN fecha_ins timestamp;
 
 -- Creación de la tabla para los indicadores 

CREATE TABLE "indicadores" () WITH OIDS;
 ALTER TABLE "indicadores" OWNER TO geopista;
 GRANT ALL ON TABLE "indicadores" TO visualizador;
 GRANT SELECT ON TABLE "indicadores" TO guiaurbana;
 GRANT SELECT ON TABLE "indicadores" TO consultas;
insert into tables (id_table, name, geometrytype) values(nextval('seq_tables'),'indicadores',0);
ALTER TABLE "indicadores" ADD COLUMN "id" NUMERIC(8)  UNIQUE;
 ALTER TABLE "indicadores" ALTER COLUMN "id" SET NOT NULL;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'id',null,8,0,2);
ALTER TABLE "indicadores" ADD COLUMN "id_municipio" NUMERIC(5) ;
ALTER TABLE "indicadores" ALTER COLUMN "id_municipio" SET NOT NULL;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'id_municipio',null,5,0,2);
ALTER TABLE "indicadores" ADD COLUMN "GEOMETRY" GEOMETRY;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'GEOMETRY',null,0,0,1);
ALTER TABLE "indicadores" ADD COLUMN "num_hab_viv" NUMERIC(10,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'num_hab_viv',null,10,3,2);
ALTER TABLE "indicadores" ADD COLUMN "planeam_urban" VARCHAR(2) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'planeam_urban',2,null,null,3);
ALTER TABLE "indicadores" ADD COLUMN "cap_reg_der" NUMERIC(10,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'cap_reg_der',null,10,3,2);
ALTER TABLE "indicadores" ADD COLUMN "cap_reg_est" NUMERIC(10,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'cap_reg_est',null,10,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_puntos_luz" NUMERIC(10,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_puntos_luz',null,10,3,2);
ALTER TABLE "indicadores" ADD COLUMN "pob_def_der" INTEGER;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'pob_def_der',null,0,0,2);
ALTER TABLE "indicadores" ADD COLUMN "pob_def_est" INTEGER;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'pob_def_est',null,0,0,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_capt" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_capt',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_pot" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_pot',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_dep" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_dep',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_dist" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_dist',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_saneam" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_saneam',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_depu" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_depu',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_pav" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_pav',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_alum" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_alum',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_bas" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_bas',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_res" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_res',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_cul" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_cul',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_depo" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_depo',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_ver" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_ver',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_adm" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_adm',null,6,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_global" NUMERIC(6,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_global',null,6,3,2);
insert into columns_domains (id_domain, id_column, level) values (10067,43135,0);
insert into columns_domains (id_domain, id_column, level) values (10068,43136,0);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Indicadores EIEL');
insert into dictionary (id_vocablo,locale, traduccion) values (227347,'es_ES','Indicadores EIEL');
insert into dictionary (id_vocablo,locale, traduccion) values (227347,'va_ES','Indicadores EIEL');
insert into dictionary (id_vocablo,locale, traduccion) values (227347,'eu_ES','Indicadores EIEL');
insert into dictionary (id_vocablo,locale, traduccion) values (227347,'ca_ES','Indicadores EIEL');
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Indicadores EIEL');
insert into layerfamilies (id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),227347,227348 );
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Indicadores Municipales  EIEL');
insert into dictionary (id_vocablo,locale, traduccion) values (227349,'ga_ES','Indicadores Municipales EIEL');
insert into dictionary (id_vocablo,locale, traduccion) values (227349,'va_ES','Indicadores Municipales EIEL');
insert into dictionary (id_vocablo,locale, traduccion) values (227349,'eu_ES','Indicadores Municipales EIEL');
insert into dictionary (id_vocablo,locale, traduccion) values (227349,'ca_ES','Indicadores Municipales EIEL');
insert into styles (id_style, xml) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?> <StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"> <NamedLayer><Name>indicadoresmunicipales</Name><UserStyle><Name>default:indicadoresmunicipales</Name><Title>default:indicadoresmunicipales</Title><Abstract>default:indicadoresmunicipales</Abstract><FeatureTypeStyle><Name>indicadoresmunicipales</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill'>#ffffff</CssParameter><CssParameter name='fill-opacity'>1.0</CssParameter></Fill><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#339900</CssParameter><CssParameter name='stroke-linejoin'>mitre</CssParameter><CssParameter name='stroke-linecap'>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#0066ff</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name='fill'>#ffffff</CssParameter><CssParameter name='fill-opacity'>1.0</CssParameter></Fill><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
insert into layers (id_layer, id_name, name, id_styles, acl, modificable) values (nextval('seq_layers'),227349,'indicadoresmunicipales',269,'12',1);
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (315,11208,1);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','GEOMETRY');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227350,11208,43137,1,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227351,11208,43135,2,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Nº de habitantes por vivienda principal');
insert into dictionary (id_vocablo,locale, traduccion) values (227352,'es_ES','Nº de habitantes por vivienda principal');
insert into dictionary (id_vocablo,locale, traduccion) values (227352,'va_ES','Nº de habitantes por vivienda principal');
insert into dictionary (id_vocablo,locale, traduccion) values (227352,'eu_ES','Nº de habitantes por vivienda principal');
insert into dictionary (id_vocablo,locale, traduccion) values (227352,'ca_ES','Nº de habitantes por vivienda principal');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227352,11208,43138,3,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Adecuación del planeamiento urabanístico al tamaño poblacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227353,'es_ES','Adecuación del planeamiento urabanístico al tamaño poblacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227353,'va_ES','Adecuación del planeamiento urabanístico al tamaño poblacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227353,'eu_ES','Adecuación del planeamiento urabanístico al tamaño poblacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227353,'ca_ES','Adecuación del planeamiento urabanístico al tamaño poblacional');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227353,11208,43139,4,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Capacidad de regulación. Población de derecho');
insert into dictionary (id_vocablo,locale, traduccion) values (227354,'es_ES','Capacidad de regulación. Población de derecho');
insert into dictionary (id_vocablo,locale, traduccion) values (227354,'va_ES','Capacidad de regulación. Población de derecho');
insert into dictionary (id_vocablo,locale, traduccion) values (227354,'eu_ES','Capacidad de regulación. Población de derecho');
insert into dictionary (id_vocablo,locale, traduccion) values (227354,'ca_ES','Capacidad de regulación. Población de derecho');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227354,11208,43140,5,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Capacidad de regulación. Población estacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227355,'es_ES','Capacidad de regulación. Población estacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227355,'va_ES','Capacidad de regulación. Población estacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227355,'eu_ES','Capacidad de regulación. Población estacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227355,'ca_ES','Capacidad de regulación. Población estacional');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227355,11208,43141,6,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de puntos de luz');
insert into dictionary (id_vocablo,locale, traduccion) values (227356,'es_ES','Índice de puntos de luz');
insert into dictionary (id_vocablo,locale, traduccion) values (227356,'va_ES','Índice de puntos de luz');
insert into dictionary (id_vocablo,locale, traduccion) values (227356,'eu_ES','Índice de puntos de luz');
insert into dictionary (id_vocablo,locale, traduccion) values (227356,'ca_ES','Índice de puntos de luz');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227356,11208,43142,7,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Población de derecho afectada del déficit');
insert into dictionary (id_vocablo,locale, traduccion) values (227357,'es_ES','Población de derecho afectada del déficit');
insert into dictionary (id_vocablo,locale, traduccion) values (227357,'va_ES','Población de derecho afectada del déficit');
insert into dictionary (id_vocablo,locale, traduccion) values (227357,'eu_ES','Población de derecho afectada del déficit');
insert into dictionary (id_vocablo,locale, traduccion) values (227357,'ca_ES','Población de derecho afectada del déficit');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227357,11208,43143,8,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Población estacional afectada del déficit');
insert into dictionary (id_vocablo,locale, traduccion) values (227358,'es_ES','Población estacional afectada del déficit');
insert into dictionary (id_vocablo,locale, traduccion) values (227358,'va_ES','Población estacional afectada del déficit');
insert into dictionary (id_vocablo,locale, traduccion) values (227358,'eu_ES','Población estacional afectada del déficit');
insert into dictionary (id_vocablo,locale, traduccion) values (227358,'ca_ES','Población estacional afectada del déficit');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227358,11208,43144,9,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de captaciones');
insert into dictionary (id_vocablo,locale, traduccion) values (227359,'es_ES','Índice de captaciones');
insert into dictionary (id_vocablo,locale, traduccion) values (227359,'va_ES','Índice de captaciones');
insert into dictionary (id_vocablo,locale, traduccion) values (227359,'eu_ES','Índice de captaciones');
insert into dictionary (id_vocablo,locale, traduccion) values (227359,'ca_ES','Índice de captaciones');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227359,11208,43145,10,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de potabilización');
insert into dictionary (id_vocablo,locale, traduccion) values (227360,'es_ES','Índice de potabilización');
insert into dictionary (id_vocablo,locale, traduccion) values (227360,'va_ES','Índice de potabilización');
insert into dictionary (id_vocablo,locale, traduccion) values (227360,'eu_ES','Índice de potabilización');
insert into dictionary (id_vocablo,locale, traduccion) values (227360,'ca_ES','Índice de potabilización');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227360,11208,43146,11,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de depósitos');
insert into dictionary (id_vocablo,locale, traduccion) values (227361,'es_ES','Índice de depósitos');
insert into dictionary (id_vocablo,locale, traduccion) values (227361,'va_ES','Índice de depósitos');
insert into dictionary (id_vocablo,locale, traduccion) values (227361,'eu_ES','Índice de depósitos');
insert into dictionary (id_vocablo,locale, traduccion) values (227361,'ca_ES','Índice de depósitos');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227361,11208,43147,12,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de distribución');
insert into dictionary (id_vocablo,locale, traduccion) values (227362,'es_ES','Índice de distribución');
insert into dictionary (id_vocablo,locale, traduccion) values (227362,'va_ES','Índice de distribución');
insert into dictionary (id_vocablo,locale, traduccion) values (227362,'eu_ES','Índice de distribución');
insert into dictionary (id_vocablo,locale, traduccion) values (227362,'ca_ES','Índice de distribución');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227362,11208,43148,13,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice red saneamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227363,'es_ES','Índice red saneamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227363,'va_ES','Índice red saneamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227363,'eu_ES','Índice red saneamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227363,'ca_ES','Índice red saneamiento');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227363,11208,43149,14,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de depuración');
insert into dictionary (id_vocablo,locale, traduccion) values (227364,'es_ES','Índice de depuración');
insert into dictionary (id_vocablo,locale, traduccion) values (227364,'va_ES','Índice de depuración');
insert into dictionary (id_vocablo,locale, traduccion) values (227364,'eu_ES','Índice de depuración');
insert into dictionary (id_vocablo,locale, traduccion) values (227364,'ca_ES','Índice de depuración');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227364,11208,43150,15,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de pavimentación');
insert into dictionary (id_vocablo,locale, traduccion) values (227365,'es_ES','Índice de pavimentación');
insert into dictionary (id_vocablo,locale, traduccion) values (227365,'va_ES','Índice de pavimentación');
insert into dictionary (id_vocablo,locale, traduccion) values (227365,'eu_ES','Índice de pavimentación');
insert into dictionary (id_vocablo,locale, traduccion) values (227365,'ca_ES','Índice de pavimentación');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227365,11208,43151,16,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de alumbrado');
insert into dictionary (id_vocablo,locale, traduccion) values (227366,'es_ES','Índice de alumbrado');
insert into dictionary (id_vocablo,locale, traduccion) values (227366,'va_ES','Índice de alumbrado');
insert into dictionary (id_vocablo,locale, traduccion) values (227366,'eu_ES','Índice de alumbrado');
insert into dictionary (id_vocablo,locale, traduccion) values (227366,'ca_ES','Índice de alumbrado');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227366,11208,43152,17,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de recogida de basuras');
insert into dictionary (id_vocablo,locale, traduccion) values (227367,'es_ES','Índice de recogida de basuras');
insert into dictionary (id_vocablo,locale, traduccion) values (227367,'va_ES','Índice de recogida de basuras');
insert into dictionary (id_vocablo,locale, traduccion) values (227367,'eu_ES','Índice de recogida de basuras');
insert into dictionary (id_vocablo,locale, traduccion) values (227367,'ca_ES','Índice de recogida de basuras');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227367,11208,43153,18,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de tratamiento de residuos');
insert into dictionary (id_vocablo,locale, traduccion) values (227368,'es_ES','Índice de tratamiento de residuos');
insert into dictionary (id_vocablo,locale, traduccion) values (227368,'va_ES','Índice de tratamiento de residuos');
insert into dictionary (id_vocablo,locale, traduccion) values (227368,'eu_ES','Índice de tratamiento de residuos');
insert into dictionary (id_vocablo,locale, traduccion) values (227368,'ca_ES','Índice de tratamiento de residuos');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227368,11208,43154,19,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de cultura');
insert into dictionary (id_vocablo,locale, traduccion) values (227369,'es_ES','Índice de cultura');
insert into dictionary (id_vocablo,locale, traduccion) values (227369,'va_ES','Índice de cultura');
insert into dictionary (id_vocablo,locale, traduccion) values (227369,'eu_ES','Índice de cultura');
insert into dictionary (id_vocablo,locale, traduccion) values (227369,'ca_ES','Índice de cultura');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227369,11208,43155,20,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Índice de deportes');
insert into dictionary (id_vocablo,locale, traduccion) values (227370,'ga_ES','Índice de deportes');
insert into dictionary (id_vocablo,locale, traduccion) values (227370,'va_ES','Índice de deportes');
insert into dictionary (id_vocablo,locale, traduccion) values (227370,'eu_ES','Índice de deportes');
insert into dictionary (id_vocablo,locale, traduccion) values (227370,'ca_ES','Índice de deportes');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227370,11208,43156,21,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice de zonas verdes');
insert into dictionary (id_vocablo,locale, traduccion) values (227371,'es_ES','Índice de zonas verdes');
insert into dictionary (id_vocablo,locale, traduccion) values (227371,'va_ES','Índice de zonas verdes');
insert into dictionary (id_vocablo,locale, traduccion) values (227371,'eu_ES','Índice de zonas verdes');
insert into dictionary (id_vocablo,locale, traduccion) values (227371,'ca_ES','Índice de zonas verdes');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227371,11208,43157,22,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice administrativo');
insert into dictionary (id_vocablo,locale, traduccion) values (227372,'es_ES','Índice administrativo');
insert into dictionary (id_vocablo,locale, traduccion) values (227372,'va_ES','Índice administrativo');
insert into dictionary (id_vocablo,locale, traduccion) values (227372,'eu_ES','Índice administrativo');
insert into dictionary (id_vocablo,locale, traduccion) values (227372,'ca_ES','Índice administrativo');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227372,11208,43158,23,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Indicador Sintético Global');
insert into dictionary (id_vocablo,locale, traduccion) values (227373,'es_ES','Indicador Sintético Global');
insert into dictionary (id_vocablo,locale, traduccion) values (227373,'va_ES','Indicador Sintético Global');
insert into dictionary (id_vocablo,locale, traduccion) values (227373,'eu_ES','Indicador Sintético Global');
insert into dictionary (id_vocablo,locale, traduccion) values (227373,'ca_ES','Indicador Sintético Global');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227373,11208,43159,24,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','ID del municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227374,'es_ES','ID del municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227374,'va_ES','ID del municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227374,'eu_ES','ID del municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227374,'ca_ES','ID del municipio');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227374,11208,43136,25,true);
insert into queries (id, id_layer, databasetype, selectquery, updatequery, insertquery, deletequery) values (nextval('seq_querys'),11208,1,'SELECT "indicadores"."GEOMETRY","indicadores"."id","indicadores"."num_hab_viv","indicadores"."planeam_urban","indicadores"."cap_reg_der","indicadores"."cap_reg_est","indicadores"."ind_puntos_luz","indicadores"."pob_def_der","indicadores"."pob_def_est","indicadores"."ind_capt","indicadores"."ind_pot","indicadores"."ind_dep","indicadores"."ind_dist","indicadores"."ind_saneam","indicadores"."ind_depu","indicadores"."ind_pav","indicadores"."ind_alum","indicadores"."ind_bas","indicadores"."ind_res","indicadores"."ind_cul","indicadores"."ind_depo","indicadores"."ind_ver","indicadores"."ind_adm","indicadores"."ind_global","indicadores"."id_municipio" FROM "indicadores" WHERE "indicadores"."id_municipio"=?M','UPDATE "indicadores" SET "GEOMETRY"=GeometryFromText(?1,?S),"id"=?2,"num_hab_viv"=?3,"planeam_urban"=?4,"cap_reg_der"=?5,"cap_reg_est"=?6,"ind_puntos_luz"=?7,"pob_def_der"=?8,"pob_def_est"=?9,"ind_capt"=?10,"ind_pot"=?11,"ind_dep"=?12,"ind_dist"=?13,"ind_saneam"=?14,"ind_depu"=?15,"ind_pav"=?16,"ind_alum"=?17,"ind_bas"=?18,"ind_res"=?19,"ind_cul"=?20,"ind_depo"=?21,"ind_ver"=?22,"ind_adm"=?23,"ind_global"=?24,"id_municipio"=?M WHERE "id"=?2','INSERT INTO "indicadores" ("GEOMETRY","id","num_hab_viv","planeam_urban","cap_reg_der","cap_reg_est","ind_puntos_luz","pob_def_der","pob_def_est","ind_capt","ind_pot","ind_dep","ind_dist","ind_saneam","ind_depu","ind_pav","ind_alum","ind_bas","ind_res","ind_cul","ind_depo","ind_ver","ind_adm","ind_global","id_municipio") VALUES(GeometryFromText(?1,?S),?PK,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?M)','DELETE FROM "indicadores" WHERE "id"=?2');
CREATE SEQUENCE "public"."seq_indicadores" INCREMENT 1 MINVALUE 1 START 1 CACHE 1;
ALTER TABLE "eiel_c_redviaria_t_autovia" ADD COLUMN "ind_sup_der" NUMERIC(10,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10192,'ind_sup_der',null,10,3,2);
ALTER TABLE "eiel_c_redviaria_t_autovia" ADD COLUMN "ind_sup_est" NUMERIC(10,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10192,'ind_sup_est',null,10,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_sup_der" NUMERIC(10,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_sup_der',null,10,3,2);
ALTER TABLE "indicadores" ADD COLUMN "ind_sup_est" NUMERIC(10,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10196,'ind_sup_est',null,10,3,2);
update dictionary set traduccion='Indicadores Municipales EIEL' where id_vocablo=227349 and locale='ga_ES';
update dictionary set traduccion='Indicadores Municipales  EIEL' where id_vocablo=227349 and locale='es_ES';
update dictionary set traduccion='Indicadores Municipales EIEL' where id_vocablo=227349 and locale='va_ES';
update dictionary set traduccion='Indicadores Municipales EIEL' where id_vocablo=227349 and locale='eu_ES';
update dictionary set traduccion='Indicadores Municipales EIEL' where id_vocablo=227349 and locale='ca_ES';
update dictionary set traduccion='GEOMETRY' where id_vocablo=227350 and locale='ga_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (227350,'ga_ES','GEOMETRY');
update dictionary set traduccion='GEOMETRY' where id_vocablo=227350 and locale='es_ES';
update dictionary set traduccion='GEOMETRY' where id_vocablo=227350 and locale='va_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (227350,'va_ES','GEOMETRY');
update dictionary set traduccion='GEOMETRY' where id_vocablo=227350 and locale='eu_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (227350,'eu_ES','GEOMETRY');
update dictionary set traduccion='GEOMETRY' where id_vocablo=227350 and locale='ca_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (227350,'ca_ES','GEOMETRY');
update attributes set id_alias =227350, id_layer=11208, id_column=43137, position=1, editable=true where id=102989;
update dictionary set traduccion='id' where id_vocablo=227351 and locale='ga_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (227351,'ga_ES','id');
update dictionary set traduccion='id' where id_vocablo=227351 and locale='es_ES';
update dictionary set traduccion='id' where id_vocablo=227351 and locale='va_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (227351,'va_ES','id');
update dictionary set traduccion='id' where id_vocablo=227351 and locale='eu_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (227351,'eu_ES','id');
update dictionary set traduccion='id' where id_vocablo=227351 and locale='ca_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (227351,'ca_ES','id');
update attributes set id_alias =227351, id_layer=11208, id_column=43135, position=2, editable=true where id=102990;
update dictionary set traduccion='Nº de habitantes por vivienda principal' where id_vocablo=227352 and locale='es_ES';
update dictionary set traduccion='Nº de habitantes por vivienda principal' where id_vocablo=227352 and locale='ga_ES';
update dictionary set traduccion='Nº de habitantes por vivienda principal' where id_vocablo=227352 and locale='va_ES';
update dictionary set traduccion='Nº de habitantes por vivienda principal' where id_vocablo=227352 and locale='eu_ES';
update dictionary set traduccion='Nº de habitantes por vivienda principal' where id_vocablo=227352 and locale='ca_ES';
update attributes set id_alias =227352, id_layer=11208, id_column=43138, position=3, editable=true where id=102991;
update dictionary set traduccion='Adec. planeam. urabanístico tamaño poblacional' where id_vocablo=227353 and locale='ga_ES';
update dictionary set traduccion='Adec. planeam. urabanístico tamaño poblacional' where id_vocablo=227353 and locale='es_ES';
update dictionary set traduccion='Adec. planeam. urabanístico tamaño poblacional' where id_vocablo=227353 and locale='va_ES';
update dictionary set traduccion='Adec. planeam. urabanístico tamaño poblacional' where id_vocablo=227353 and locale='eu_ES';
update dictionary set traduccion='Adec. planeam. urabanístico tamaño poblacional' where id_vocablo=227353 and locale='ca_ES';
update attributes set id_alias =227353, id_layer=11208, id_column=43139, position=4, editable=true where id=102992;
update dictionary set traduccion='Capacidad de regulación. Población de derecho' where id_vocablo=227354 and locale='es_ES';
update dictionary set traduccion='Capacidad de regulación. Población de derecho' where id_vocablo=227354 and locale='ga_ES';
update dictionary set traduccion='Capacidad de regulación. Población de derecho' where id_vocablo=227354 and locale='va_ES';
update dictionary set traduccion='Capacidad de regulación. Población de derecho' where id_vocablo=227354 and locale='eu_ES';
update dictionary set traduccion='Capacidad de regulación. Población de derecho' where id_vocablo=227354 and locale='ca_ES';
update attributes set id_alias =227354, id_layer=11208, id_column=43140, position=5, editable=true where id=102993;
update dictionary set traduccion='Capacidad de regulación. Población estacional' where id_vocablo=227355 and locale='es_ES';
update dictionary set traduccion='Capacidad de regulación. Población estacional' where id_vocablo=227355 and locale='ga_ES';
update dictionary set traduccion='Capacidad de regulación. Población estacional' where id_vocablo=227355 and locale='va_ES';
update dictionary set traduccion='Capacidad de regulación. Población estacional' where id_vocablo=227355 and locale='eu_ES';
update dictionary set traduccion='Capacidad de regulación. Población estacional' where id_vocablo=227355 and locale='ca_ES';
update attributes set id_alias =227355, id_layer=11208, id_column=43141, position=6, editable=true where id=102994;
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice superficie de viarios. Pobl. de derecho');
insert into dictionary (id_vocablo,locale, traduccion) values (227375,'es_ES','Índice superficie de viarios. Pobl. de derecho');
insert into dictionary (id_vocablo,locale, traduccion) values (227375,'va_ES','Índice superficie de viarios. Pobl. de derecho');
insert into dictionary (id_vocablo,locale, traduccion) values (227375,'eu_ES','Índice superficie de viarios. Pobl. de derecho');
insert into dictionary (id_vocablo,locale, traduccion) values (227375,'ca_ES','Índice superficie de viarios. Pobl. de derecho');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227375,11208,43162,7,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Índice superficie de viarios. Pob. estacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227376,'es_ES','Índice superficie de viarios. Pob. estacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227376,'va_ES','Índice superficie de viarios. Pob. estacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227376,'eu_ES','Índice superficie de viarios. Pob. estacional');
insert into dictionary (id_vocablo,locale, traduccion) values (227376,'ca_ES','Índice superficie de viarios. Pob. estacional');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227376,11208,43163,8,true);
update dictionary set traduccion='Índice de puntos de luz' where id_vocablo=227356 and locale='es_ES';
update dictionary set traduccion='Índice de puntos de luz' where id_vocablo=227356 and locale='ga_ES';
update dictionary set traduccion='Índice de puntos de luz' where id_vocablo=227356 and locale='va_ES';
update dictionary set traduccion='Índice de puntos de luz' where id_vocablo=227356 and locale='eu_ES';
update dictionary set traduccion='Índice de puntos de luz' where id_vocablo=227356 and locale='ca_ES';
update attributes set id_alias =227356, id_layer=11208, id_column=43142, position=9, editable=true where id=102995;
update dictionary set traduccion='Población de derecho afectada del déficit' where id_vocablo=227357 and locale='es_ES';
update dictionary set traduccion='Población de derecho afectada del déficit' where id_vocablo=227357 and locale='ga_ES';
update dictionary set traduccion='Población de derecho afectada del déficit' where id_vocablo=227357 and locale='va_ES';
update dictionary set traduccion='Población de derecho afectada del déficit' where id_vocablo=227357 and locale='eu_ES';
update dictionary set traduccion='Población de derecho afectada del déficit' where id_vocablo=227357 and locale='ca_ES';
update attributes set id_alias =227357, id_layer=11208, id_column=43143, position=10, editable=true where id=102996;
update dictionary set traduccion='Población estacional afectada del déficit' where id_vocablo=227358 and locale='es_ES';
update dictionary set traduccion='Población estacional afectada del déficit' where id_vocablo=227358 and locale='ga_ES';
update dictionary set traduccion='Población estacional afectada del déficit' where id_vocablo=227358 and locale='va_ES';
update dictionary set traduccion='Población estacional afectada del déficit' where id_vocablo=227358 and locale='eu_ES';
update dictionary set traduccion='Población estacional afectada del déficit' where id_vocablo=227358 and locale='ca_ES';
update attributes set id_alias =227358, id_layer=11208, id_column=43144, position=11, editable=true where id=102997;
update dictionary set traduccion='Índice de captaciones' where id_vocablo=227359 and locale='es_ES';
update dictionary set traduccion='Índice de captaciones' where id_vocablo=227359 and locale='ga_ES';
update dictionary set traduccion='Índice de captaciones' where id_vocablo=227359 and locale='va_ES';
update dictionary set traduccion='Índice de captaciones' where id_vocablo=227359 and locale='eu_ES';
update dictionary set traduccion='Índice de captaciones' where id_vocablo=227359 and locale='ca_ES';
update attributes set id_alias =227359, id_layer=11208, id_column=43145, position=12, editable=true where id=102998;
update dictionary set traduccion='Índice de potabilización' where id_vocablo=227360 and locale='es_ES';
update dictionary set traduccion='Índice de potabilización' where id_vocablo=227360 and locale='ga_ES';
update dictionary set traduccion='Índice de potabilización' where id_vocablo=227360 and locale='va_ES';
update dictionary set traduccion='Índice de potabilización' where id_vocablo=227360 and locale='eu_ES';
update dictionary set traduccion='Índice de potabilización' where id_vocablo=227360 and locale='ca_ES';
update attributes set id_alias =227360, id_layer=11208, id_column=43146, position=13, editable=true where id=102999;
update dictionary set traduccion='Índice de depósitos' where id_vocablo=227361 and locale='es_ES';
update dictionary set traduccion='Índice de depósitos' where id_vocablo=227361 and locale='ga_ES';
update dictionary set traduccion='Índice de depósitos' where id_vocablo=227361 and locale='va_ES';
update dictionary set traduccion='Índice de depósitos' where id_vocablo=227361 and locale='eu_ES';
update dictionary set traduccion='Índice de depósitos' where id_vocablo=227361 and locale='ca_ES';
update attributes set id_alias =227361, id_layer=11208, id_column=43147, position=14, editable=true where id=103000;
update dictionary set traduccion='Índice de distribución' where id_vocablo=227362 and locale='es_ES';
update dictionary set traduccion='Índice de distribución' where id_vocablo=227362 and locale='ga_ES';
update dictionary set traduccion='Índice de distribución' where id_vocablo=227362 and locale='va_ES';
update dictionary set traduccion='Índice de distribución' where id_vocablo=227362 and locale='eu_ES';
update dictionary set traduccion='Índice de distribución' where id_vocablo=227362 and locale='ca_ES';
update attributes set id_alias =227362, id_layer=11208, id_column=43148, position=15, editable=true where id=103001;
update dictionary set traduccion='Índice red saneamiento' where id_vocablo=227363 and locale='es_ES';
update dictionary set traduccion='Índice red saneamiento' where id_vocablo=227363 and locale='ga_ES';
update dictionary set traduccion='Índice red saneamiento' where id_vocablo=227363 and locale='va_ES';
update dictionary set traduccion='Índice red saneamiento' where id_vocablo=227363 and locale='eu_ES';
update dictionary set traduccion='Índice red saneamiento' where id_vocablo=227363 and locale='ca_ES';
update attributes set id_alias =227363, id_layer=11208, id_column=43149, position=16, editable=true where id=103002;
update dictionary set traduccion='Índice de depuración' where id_vocablo=227364 and locale='es_ES';
update dictionary set traduccion='Índice de depuración' where id_vocablo=227364 and locale='ga_ES';
update dictionary set traduccion='Índice de depuración' where id_vocablo=227364 and locale='va_ES';
update dictionary set traduccion='Índice de depuración' where id_vocablo=227364 and locale='eu_ES';
update dictionary set traduccion='Índice de depuración' where id_vocablo=227364 and locale='ca_ES';
update attributes set id_alias =227364, id_layer=11208, id_column=43150, position=17, editable=true where id=103003;
update dictionary set traduccion='Índice de pavimentación' where id_vocablo=227365 and locale='es_ES';
update dictionary set traduccion='Índice de pavimentación' where id_vocablo=227365 and locale='ga_ES';
update dictionary set traduccion='Índice de pavimentación' where id_vocablo=227365 and locale='va_ES';
update dictionary set traduccion='Índice de pavimentación' where id_vocablo=227365 and locale='eu_ES';
update dictionary set traduccion='Índice de pavimentación' where id_vocablo=227365 and locale='ca_ES';
update attributes set id_alias =227365, id_layer=11208, id_column=43151, position=18, editable=true where id=103004;
update dictionary set traduccion='Índice de alumbrado' where id_vocablo=227366 and locale='es_ES';
update dictionary set traduccion='Índice de alumbrado' where id_vocablo=227366 and locale='ga_ES';
update dictionary set traduccion='Índice de alumbrado' where id_vocablo=227366 and locale='va_ES';
update dictionary set traduccion='Índice de alumbrado' where id_vocablo=227366 and locale='eu_ES';
update dictionary set traduccion='Índice de alumbrado' where id_vocablo=227366 and locale='ca_ES';
update attributes set id_alias =227366, id_layer=11208, id_column=43152, position=19, editable=true where id=103005;
update dictionary set traduccion='Índice de recogida de basuras' where id_vocablo=227367 and locale='es_ES';
update dictionary set traduccion='Índice de recogida de basuras' where id_vocablo=227367 and locale='ga_ES';
update dictionary set traduccion='Índice de recogida de basuras' where id_vocablo=227367 and locale='va_ES';
update dictionary set traduccion='Índice de recogida de basuras' where id_vocablo=227367 and locale='eu_ES';
update dictionary set traduccion='Índice de recogida de basuras' where id_vocablo=227367 and locale='ca_ES';
update attributes set id_alias =227367, id_layer=11208, id_column=43153, position=20, editable=true where id=103006;
update dictionary set traduccion='Índice de tratamiento de residuos' where id_vocablo=227368 and locale='es_ES';
update dictionary set traduccion='Índice de tratamiento de residuos' where id_vocablo=227368 and locale='ga_ES';
update dictionary set traduccion='Índice de tratamiento de residuos' where id_vocablo=227368 and locale='va_ES';
update dictionary set traduccion='Índice de tratamiento de residuos' where id_vocablo=227368 and locale='eu_ES';
update dictionary set traduccion='Índice de tratamiento de residuos' where id_vocablo=227368 and locale='ca_ES';
update attributes set id_alias =227368, id_layer=11208, id_column=43154, position=21, editable=true where id=103007;
update dictionary set traduccion='Índice de cultura' where id_vocablo=227369 and locale='es_ES';
update dictionary set traduccion='Índice de cultura' where id_vocablo=227369 and locale='ga_ES';
update dictionary set traduccion='Índice de cultura' where id_vocablo=227369 and locale='va_ES';
update dictionary set traduccion='Índice de cultura' where id_vocablo=227369 and locale='eu_ES';
update dictionary set traduccion='Índice de cultura' where id_vocablo=227369 and locale='ca_ES';
update attributes set id_alias =227369, id_layer=11208, id_column=43155, position=22, editable=true where id=103008;
update dictionary set traduccion='Índice de deportes' where id_vocablo=227370 and locale='ga_ES';
update dictionary set traduccion='Índice de deportes' where id_vocablo=227370 and locale='es_ES';
update dictionary set traduccion='Índice de deportes' where id_vocablo=227370 and locale='va_ES';
update dictionary set traduccion='Índice de deportes' where id_vocablo=227370 and locale='eu_ES';
update dictionary set traduccion='Índice de deportes' where id_vocablo=227370 and locale='ca_ES';
update attributes set id_alias =227370, id_layer=11208, id_column=43156, position=23, editable=true where id=103009;
update dictionary set traduccion='Índice de zonas verdes' where id_vocablo=227371 and locale='es_ES';
update dictionary set traduccion='Índice de zonas verdes' where id_vocablo=227371 and locale='ga_ES';
update dictionary set traduccion='Índice de zonas verdes' where id_vocablo=227371 and locale='va_ES';
update dictionary set traduccion='Índice de zonas verdes' where id_vocablo=227371 and locale='eu_ES';
update dictionary set traduccion='Índice de zonas verdes' where id_vocablo=227371 and locale='ca_ES';
update attributes set id_alias =227371, id_layer=11208, id_column=43157, position=24, editable=true where id=103010;
update dictionary set traduccion='Índice administrativo' where id_vocablo=227372 and locale='es_ES';
update dictionary set traduccion='Índice administrativo' where id_vocablo=227372 and locale='ga_ES';
update dictionary set traduccion='Índice administrativo' where id_vocablo=227372 and locale='va_ES';
update dictionary set traduccion='Índice administrativo' where id_vocablo=227372 and locale='eu_ES';
update dictionary set traduccion='Índice administrativo' where id_vocablo=227372 and locale='ca_ES';
update attributes set id_alias =227372, id_layer=11208, id_column=43158, position=25, editable=true where id=103011;
update dictionary set traduccion='Indicador Sintético Global' where id_vocablo=227373 and locale='es_ES';
update dictionary set traduccion='Indicador Sintético Global' where id_vocablo=227373 and locale='ga_ES';
update dictionary set traduccion='Indicador Sintético Global' where id_vocablo=227373 and locale='va_ES';
update dictionary set traduccion='Indicador Sintético Global' where id_vocablo=227373 and locale='eu_ES';
update dictionary set traduccion='Indicador Sintético Global' where id_vocablo=227373 and locale='ca_ES';
update attributes set id_alias =227373, id_layer=11208, id_column=43159, position=26, editable=true where id=103012;
update dictionary set traduccion='ID del municipio' where id_vocablo=227374 and locale='es_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227374 and locale='ga_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227374 and locale='va_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227374 and locale='eu_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227374 and locale='ca_ES';
update attributes set id_alias =227374, id_layer=11208, id_column=43136, position=27, editable=true where id=103013;
update layers set name='indicadoresmunicipales', acl='12' where id_layer=11208;
update queries set selectquery='SELECT "indicadores"."GEOMETRY","indicadores"."id","indicadores"."num_hab_viv","indicadores"."planeam_urban","indicadores"."cap_reg_der","indicadores"."cap_reg_est","indicadores"."ind_puntos_luz","indicadores"."pob_def_der","indicadores"."pob_def_est","indicadores"."ind_capt","indicadores"."ind_pot","indicadores"."ind_dep","indicadores"."ind_dist","indicadores"."ind_saneam","indicadores"."ind_depu","indicadores"."ind_pav","indicadores"."ind_alum","indicadores"."ind_bas","indicadores"."ind_res","indicadores"."ind_cul","indicadores"."ind_depo","indicadores"."ind_ver","indicadores"."ind_adm","indicadores"."ind_global","indicadores"."id_municipio" FROM "indicadores" WHERE "indicadores"."id_municipio"=?M', updatequery='UPDATE "indicadores" SET "GEOMETRY"=GeometryFromText(?1,?S),"id"=?2,"num_hab_viv"=?3,"planeam_urban"=?4,"cap_reg_der"=?5,"cap_reg_est"=?6,"ind_puntos_luz"=?7,"pob_def_der"=?8,"pob_def_est"=?9,"ind_capt"=?10,"ind_pot"=?11,"ind_dep"=?12,"ind_dist"=?13,"ind_saneam"=?14,"ind_depu"=?15,"ind_pav"=?16,"ind_alum"=?17,"ind_bas"=?18,"ind_res"=?19,"ind_cul"=?20,"ind_depo"=?21,"ind_ver"=?22,"ind_adm"=?23,"ind_global"=?24,"id_municipio"=?M WHERE "id"=?2', insertquery='INSERT INTO "indicadores" ("GEOMETRY","id","num_hab_viv","planeam_urban","cap_reg_der","cap_reg_est","ind_puntos_luz","pob_def_der","pob_def_est","ind_capt","ind_pot","ind_dep","ind_dist","ind_saneam","ind_depu","ind_pav","ind_alum","ind_bas","ind_res","ind_cul","ind_depo","ind_ver","ind_adm","ind_global","id_municipio") VALUES(GeometryFromText(?1,?S),?PK,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,?18,?19,?20,?21,?22,?23,?24,?M)', deletequery='DELETE FROM "indicadores" WHERE "id"=?2' where id=10212;

 
 -- Funciones para obtener los valores para calcular los indicadores

-- Obtiene el valor de población de derecho total
CREATE OR REPLACE FUNCTION "public"."obtenerpadron" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_nucl_encuest_1.padron is null then 0 else eiel_t_nucl_encuest_1.padron end) from eiel_t_nucl_encuest_1 where eiel_t_nucl_encuest_1.codprov=substr(a,1,2) and eiel_t_nucl_encuest_1.codmunic=substr(a,3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

-- obtiene el valor de viviendas principales totales
CREATE OR REPLACE FUNCTION "public"."obtenerviviendas" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(CASE WHEN eiel_t_nucl_encuest_1.viviendas_total IS NULL THEN 0 ELSE eiel_t_nucl_encuest_1.viviendas_total END) from eiel_t_nucl_encuest_1 where eiel_t_nucl_encuest_1.codprov=substr(a,1,2) and eiel_t_nucl_encuest_1.codmunic=substr(a,3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

-- Obtiene el indicador de planeamiento

create or replace function obtenerplaneamiento(varchar)
returns varchar as '
declare
a varchar;
b varchar;
begin
a = $1;
select case
       when ((select total_poblacion07 from eiel_t_padron_ttmm where codprov=substr(a,1,2) and codmunic=substr(a,3,5)) < 500)
            then ''SI''
       when ((select total_poblacion07 from eiel_t_padron_ttmm where codprov=substr(a,1,2) and codmunic=substr(a,3,5)) <= 5000 )
            
            then case when ((''N.P'') IN (select tipo_urba from eiel_t_planeam_urban where codprov=substr(a,1,2) and codmunic=substr(a,3,5)) or (''P.G'') IN (select tipo_urba from eiel_t_planeam_urban where codprov=substr(a,1,2) and codmunic=substr(a,3,5))) then ''SI''
            else ''NO''
            end
       when ((select total_poblacion07 from eiel_t_padron_ttmm where codprov=substr(a,1,2) and codmunic=substr(a,3,5)) > 5000 )
            then case when ((''P.G'') IN (select tipo_urba from eiel_t_planeam_urban where codprov=substr(a,1,2) and codmunic=substr(a,3,5))) then ''SI''
                      else ''NO''
                end
       else ''NO''
       end into b;
return b;
end;
'
Language 'plpgsql';

CREATE OR REPLACE FUNCTION "public"."obtenercapacidaddepo" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_abast_de.capacidad_de is null then 0 else eiel_t_abast_de.capacidad_de end) from eiel_t_abast_de where eiel_t_abast_de.codprov=substr(a,1,2) and eiel_t_abast_de.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerpoblacionderecho" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_nucl_encuest_1.padron is null then 0 else eiel_t_nucl_encuest_1.padron end) from eiel_t_nucl_encuest_1 where eiel_t_nucl_encuest_1.codprov=substr(a,1,2) and eiel_t_nucl_encuest_1.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerpoblacionestacional" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_nucl_encuest_1.pob_estacional is null then 0 else eiel_t_nucl_encuest_1.pob_estacional end) from eiel_t_nucl_encuest_1 where eiel_t_nucl_encuest_1.codprov=substr(a,1,2) and eiel_t_nucl_encuest_1.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenersuperficiepav" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_redviaria_tu.superficie_tvu is null then 0 else eiel_c_redviaria_tu.superficie_tvu end) from eiel_c_redviaria_tu where eiel_c_redviaria_tu.codprov=substr(a,1,2) and eiel_c_redviaria_tu.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenernumpuntosluz" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_pl.n_bombillas is null then 0 else eiel_c_pl.n_bombillas end) from eiel_c_pl where eiel_c_pl.codprov=substr(a,1,2) and eiel_c_pl.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerlongitudviarios" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_redviaria_tu.longitud_tvu is null then 0 else eiel_c_redviaria_tu.longitud_tvu end) from eiel_c_redviaria_tu where eiel_c_redviaria_tu.codprov=substr(a,1,2) and eiel_c_redviaria_tu.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerpobsincentrosanit" (varchar) RETURNS varchar AS'
declare
a varchar;
b varchar;
begin
a = $1;
select case
       when ((select count(eiel_t_sa.*) from eiel_t_sa where codprov=substr(a,1,2) and codmunic=substr(a,3,5)) > 0)
            then obtenerpoblacionderecho(a)
       else 0
       end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerpobestsincentrosanit" (varchar) RETURNS varchar AS'
declare
a varchar;
b varchar;
begin
a = $1;
select case
       when ((select count(eiel_t_sa.*) from eiel_t_sa where codprov=substr(a,1,2) and codmunic=substr(a,3,5)) > 0)
            then obtenerpoblacionestacional(a)
       else 0
       end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerpobestcaudalins" (varchar) RETURNS varchar AS'
declare
a varchar;
b varchar;
begin
a = $1;
select case
       when ((''IN'') IN (select aag_caudal from eiel_t_nucl_encuest_2 where codprov=substr(a,1,2) and codmunic=substr(a,3,5))) then obtenerpoblacionestacional(a)
            else 0
            end
       into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerpobestcaudalins" (varchar) RETURNS varchar AS'
declare
a varchar;
b varchar;
begin
a = $1;
select case
       when ((''IN'') IN (select aag_caudal from eiel_t_nucl_encuest_2 where codprov=substr(a,1,2) and codmunic=substr(a,3,5))) then obtenerpoblacionestacional(a)
            else 0
            end
       into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerpobestinsper" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select case
       when ((''NO'') IN (select periodicidad from eiel_t_abast_tp where codprov=substr(a,1,2) and codmunic=substr(a,3,5))) then obtenerpoblacionestacional(a)
            else 0
            end
       into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;


CREATE OR REPLACE FUNCTION "public"."obtenercapacidaddepo" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_abast_de.capacidad_de is null then 0 else eiel_t_abast_de.capacidad_de end) from eiel_t_abast_de where eiel_t_abast_de.codprov=substr(a,1,2) and eiel_t_abast_de.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerlongreddefict" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_abast_serv.long_deficit is null then 0 else eiel_t_abast_serv.long_deficit end) from eiel_t_abast_serv where eiel_t_abast_serv.codprov=substr(a,1,2) and eiel_t_abast_serv.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerlongestadomalo" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_abast_rd.longitud_rd is null then 0 else eiel_c_abast_rd.longitud_rd end) from eiel_c_abast_rd where eiel_c_abast_rd.codprov=substr(a,1,2) and eiel_c_abast_rd.codmunic=substr(a, 3,5) and eiel_c_abast_rd.estado_rd=''M'' into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerlongitudtotal" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_abast_rd.longitud_rd is null then 0 else eiel_c_abast_rd.longitud_rd end) from eiel_c_abast_rd where eiel_c_abast_rd.codprov=substr(a,1,2) and eiel_c_abast_rd.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerlongestadomalo" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
c numeric;
begin
a = $1;
select sum(eiel_c_abast_rd.longitud_rd) from eiel_c_abast_rd where eiel_c_abast_rd.codprov=substr(a,1,2) and eiel_c_abast_rd.codmunic=substr(a, 3,5) and eiel_c_abast_rd.estado_rd=''M'' into c;
select case when (c >0 and c <> null) then  c
else 0
end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerlongdefsaneam" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(CASE WHEN eiel_t_saneam_serv.long_rs_deficit IS NULL THEN 0 ELSE eiel_t_saneam_serv.long_rs_deficit END) from eiel_t_saneam_serv where eiel_t_saneam_serv.codprov=substr(a,1,2) and eiel_t_saneam_serv.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerlongestadomalosaneam" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_saneam_rs.longitud_rs is null then 0 else eiel_c_saneam_rs.longitud_rs end) from eiel_c_saneam_rs where eiel_c_saneam_rs.codprov=substr(a,1,2) and eiel_c_saneam_rs.codmunic=substr(a, 3,5) and eiel_c_saneam_rs.estado_rs=''M'' into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerlongtotalsaneam" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_saneam_rs.longitud_rs is null then 0 else eiel_c_saneam_rs.longitud_rs end) from eiel_c_saneam_rs where eiel_c_saneam_rs.codprov=substr(a,1,2) and eiel_c_saneam_rs.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerpobestsindep" (varchar) RETURNS numeric AS'
declare
a varchar;
b varchar;
begin
a = $1;
select case
       when ((select count(eiel_tr_saneam_ed_pobl.*) from eiel_tr_saneam_ed_pobl where codprov_ed=substr(a,1,2) and codmunic_ed=substr(a,3,5)) = 0)
            then obtenerpoblacionestacional(a)
       else 0
       end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenersupurbsinpav" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_redviaria_tu.superficie_tvu is null then 0 else eiel_c_redviaria_tu.superficie_tvu end) from eiel_c_redviaria_tu where codprov=substr(a,1,2) and codmunic=substr(a,3,5) and eiel_c_redviaria_tu.estado_tvu=''N'' into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenersupurbestadomalo" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_redviaria_tu.superficie_tvu is null then 0 else eiel_c_redviaria_tu.superficie_tvu end) from eiel_c_redviaria_tu where codprov=substr(a,1,2) and codmunic=substr(a,3,5) and eiel_c_redviaria_tu.estado_tvu=''M'' into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenersupurbtotal" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_c_redviaria_tu.superficie_tvu is null then 0 else eiel_c_redviaria_tu.superficie_tvu end) from eiel_c_redviaria_tu where codprov=substr(a,1,2) and codmunic=substr(a,3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenerpobestsinbas" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_srb.srb_pob_est_afect is null then 0 else eiel_t_srb.srb_pob_est_afect end) from eiel_t_srb where eiel_t_srb.codprov=substr(a,1,2) and eiel_t_srb.codmunic=substr(a, 3,5) into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;


CREATE OR REPLACE FUNCTION obtenerpobestvert (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select case
       when ((''VIN'') IN (select tipo_vt from eiel_t_vt where codprov=substr(a,1,2) and codmunic=substr(a,3,5))) then obtenerpoblacionestacional(a)
            else 0
            end
       into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenersupcubestadobr" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_cu.s_cubierta_cu is null then 0 else eiel_t_cu.s_cubierta_cu end) from eiel_t_cu where codprov=substr(a,1,2) and codmunic=substr(a,3,5) and (estado_cu=''B'' or estado_cu=''R'') into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenersupcubdepoestadobr" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_id.s_cubierta_id is null then 0 else eiel_t_id.s_cubierta_id end) from eiel_t_id where codprov=substr(a,1,2) and codmunic=substr(a,3,5) and (eiel_t_id.estado_id=''B'' or eiel_t_id.estado_id=''R'') into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenersupcubverestadobr" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_pj.s_aire is null then 0 else eiel_t_pj.s_aire end) from eiel_t_pj where codprov=substr(a,1,2) and codmunic=substr(a,3,5) and (eiel_t_pj.estado_pj=''B'' or eiel_t_pj.estado_pj=''R'') into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE FUNCTION "public"."obtenersupcubadmestadobr" (varchar) RETURNS numeric AS'
declare
a varchar;
b numeric;
begin
a = $1;
select sum(case when eiel_t_cc.s_cubierta_cc is null then 0 else eiel_t_cc.s_cubierta_cc end) from eiel_t_cc left join eiel_t_cc_uso on eiel_t_cc_uso.uso_cc=''AM'' where eiel_t_cc.codprov=substr(a,1,2) and eiel_t_cc.codmunic=substr(a,3,5) and (eiel_t_cc.estado_cc=''B'' or eiel_t_cc.estado_cc=''R'') into b;
select case when (b is null) then 0 else b end into b;
return b;
end;
'LANGUAGE 'plpgsql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER;

CREATE OR REPLACE VIEW "public"."indicadoresmunicipales" (
    "GEOMETRY",
    id,
    num_hab_viv,
    planeam_urban,
    cap_reg_der,
    cap_reg_est,
    ind_sup_der,
    ind_sup_est,
    ind_puntos_luz,
    pob_def_der,
    pob_def_est,
    ind_capt,
    ind_pot,
    ind_dep,
    ind_dist,
    ind_saneam,
    ind_depu,
    ind_pav,
    ind_alum,
    ind_bas,
    ind_res,
    ind_cul,
    ind_depo,
    ind_ver,
    ind_adm,
    ind_global)
AS
SELECT municipios."GEOMETRY", municipios.id, (
    SELECT CASE WHEN (obtenerviviendas((municipios.id)::character varying) <>
        (0)::numeric(10,3)) THEN (obtenerpadron((municipios.id)::character varying) /
        obtenerviviendas((municipios.id)::character varying)) ELSE (0)::numeric(10,3)
        END AS "case"
    ) AS num_hab_viv, (
    SELECT obtenerplaneamiento((municipios.id)::character varying) AS
        obtenerplaneamiento
    ) AS planeam_urban, (
    SELECT CASE WHEN (obtenerpoblacionderecho((municipios.id)::character
        varying) <> (0)::numeric(10,3)) THEN
        ((obtenercapacidaddepo((municipios.id)::character varying) /
        obtenerpoblacionderecho((municipios.id)::character varying)) * 0.2)
        ELSE (0)::numeric(10,3) END AS "case"
    ) AS cap_reg_der, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(10,3)) THEN
        ((obtenercapacidaddepo((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) * 0.2)
        ELSE (0)::numeric(10,3) END AS "case"
    ) AS cap_reg_est, (
    SELECT CASE WHEN (obtenerpoblacionderecho((municipios.id)::character
        varying) <> (0)::numeric(10,3)) THEN
        (obtenersuperficiepav((municipios.id)::character varying) /
        obtenerpoblacionderecho((municipios.id)::character varying)) ELSE
        (0)::numeric(10,3) END AS "case"
    ) AS ind_sup_der, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(10,3)) THEN
        (obtenersuperficiepav((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(10,3) END AS "case"
    ) AS ind_sup_est, (
    SELECT CASE WHEN (obtenerlongitudviarios((municipios.id)::character
        varying) <> (0)::numeric(10,3)) THEN
        ((obtenernumpuntosluz((municipios.id)::character varying) *
        (25)::numeric) / obtenerlongitudviarios((municipios.id)::character
        varying)) ELSE (0)::numeric(10,3) END AS "case"
    ) AS ind_puntos_luz, (
    SELECT obtenerpobsincentrosanit((municipios.id)::character varying) AS
        obtenerpobsincentrosanit
    ) AS pob_def_der, (
    SELECT obtenerpobestsincentrosanit((municipios.id)::character varying) AS
        obtenerpobestsincentrosanit
    ) AS pob_def_est, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        ((obtenerpobestcaudalins((municipios.id)::character varying) *
        (100)::numeric(6,3)) / obtenerpoblacionestacional((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) AS ind_capt, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        ((obtenerpobestinsper((municipios.id)::character varying) *
        (100)::numeric(6,3)) / obtenerpoblacionestacional((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) AS ind_pot, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN ((100)::numeric(6,3) - (((100)::numeric(6,3) *
        (obtenercapacidaddepo((municipios.id)::character varying) /
        (obtenerpoblacionestacional((municipios.id)::character varying) *
        (200)::numeric(6,3)))) / (3)::numeric(6,3))) ELSE (0)::numeric(6,3) END AS "case"
    ) AS ind_dep, (
    SELECT CASE WHEN (obtenerlongitudtotal((municipios.id)::character varying)
        <> (0)::numeric(6,3)) THEN
        (((obtenerlongreddefict((municipios.id)::character varying) +
        obtenerlongestadomalo((municipios.id)::character varying)) *
        (100)::numeric(6,3)) / obtenerlongitudtotal((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) AS ind_dist, (
    SELECT CASE WHEN (obtenerlongtotalsaneam((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (((obtenerlongdefsaneam((municipios.id)::character varying) +
        obtenerlongestadomalosaneam((municipios.id)::character varying)) *
        (100)::numeric(6,3)) / obtenerlongtotalsaneam((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) AS ind_saneam, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        ((obtenerpobestsindep((municipios.id)::character varying) *
        (100)::numeric(6,3)) / obtenerpoblacionestacional((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) AS ind_depu, (
    SELECT CASE WHEN (obtenersupurbtotal((municipios.id)::character varying) <>
        (0)::numeric(6,3)) THEN ((obtenersupurbsinpav((municipios.id)::character
        varying) + obtenersupurbestadomalo((municipios.id)::character varying))
        / obtenersupurbtotal((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) AS ind_pav, (
    SELECT CASE WHEN (obtenerlongitudviarios((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN ((100)::numeric(6,3) - (((((100)::numeric(6,3) *
        obtenernumpuntosluz((municipios.id)::character varying)) *
        (25)::numeric(6,3)) / obtenerlongitudviarios((municipios.id)::character
        varying)) / (2)::numeric(6,3))) ELSE (0)::numeric(6,3) END AS "case"
    ) AS ind_alum, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        ((obtenerpobestsinbas((municipios.id)::character varying) *
        (100)::numeric(6,3)) / obtenerpoblacionestacional((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) AS ind_bas, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenerpobestvert((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) AS ind_res, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenersupcubestadobr((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) AS ind_cul, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenersupcubdepoestadobr((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) AS ind_depo, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenersupcubverestadobr((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) AS ind_ver, (
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenersupcubadmestadobr((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) AS ind_adm, ((((((((((((((((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        ((obtenerpobestcaudalins((municipios.id)::character varying) *
        (100)::numeric(6,3)) / obtenerpoblacionestacional((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) * (14)::numeric(6,3)) + ((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        ((obtenerpobestinsper((municipios.id)::character varying) *
        (100)::numeric(6,3)) / obtenerpoblacionestacional((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) * (10)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN ((100)::numeric(6,3) - (((100)::numeric(6,3) *
        (obtenercapacidaddepo((municipios.id)::character varying) /
        (obtenerpoblacionestacional((municipios.id)::character varying) *
        (200)::numeric(6,3)))) / (3)::numeric(6,3))) ELSE (0)::numeric(6,3) END AS "case"
    ) * (8)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerlongitudtotal((municipios.id)::character varying)
        <> (0)::numeric(6,3)) THEN
        (((obtenerlongreddefict((municipios.id)::character varying) +
        obtenerlongestadomalo((municipios.id)::character varying)) *
        (100)::numeric(6,3)) / obtenerlongitudtotal((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) * (8)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerlongtotalsaneam((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (((obtenerlongdefsaneam((municipios.id)::character varying) +
        obtenerlongestadomalosaneam((municipios.id)::character varying)) *
        (100)::numeric(6,3)) / obtenerlongtotalsaneam((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) * (8)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        ((obtenerpobestsindep((municipios.id)::character varying) *
        (100)::numeric(6,3)) / obtenerpoblacionestacional((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) * (10)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenersupurbtotal((municipios.id)::character varying) <>
        (0)::numeric(6,3)) THEN ((obtenersupurbsinpav((municipios.id)::character
        varying) + obtenersupurbestadomalo((municipios.id)::character varying))
        / obtenersupurbtotal((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) * (6)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerlongitudviarios((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN ((100)::numeric(6,3) - (((((100)::numeric(6,3) *
        obtenernumpuntosluz((municipios.id)::character varying)) *
        (25)::numeric(6,3)) / obtenerlongitudviarios((municipios.id)::character
        varying)) / (2)::numeric(6,3))) ELSE (0)::numeric(6,3) END AS "case"
    ) * (6)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        ((obtenerpobestsinbas((municipios.id)::character varying) *
        (100)::numeric(6,3)) / obtenerpoblacionestacional((municipios.id)::character
        varying)) ELSE (0)::numeric(6,3) END AS "case"
    ) * (6)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenerpobestvert((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) * (6)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenersupcubestadobr((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) * (6)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenersupcubdepoestadobr((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) * (6)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenersupcubverestadobr((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) * (3)::numeric(6,3))) + ((
    SELECT CASE WHEN (obtenerpoblacionestacional((municipios.id)::character
        varying) <> (0)::numeric(6,3)) THEN
        (obtenersupcubadmestadobr((municipios.id)::character varying) /
        obtenerpoblacionestacional((municipios.id)::character varying)) ELSE
        (0)::numeric(6,3) END AS "case"
    ) * (3)::numeric(6,3))) / (100)::numeric(6,3)) AS ind_global
FROM municipios
WHERE (municipios.id IN (
    SELECT municipios.id
    FROM municipios
    WHERE ((municipios.id_provincia)::text = '22'::text)
    ));
    
    update layers set extended_form='com.geopista.app.eiel.panels.CaptacionesNucleosPanel' where name='CA';
    
    CREATE TABLE indicadores_comarcales
(
  id numeric(8) NOT NULL,
  id_comarca numeric(5) NOT NULL,
  num_hab_viv numeric(10,3),
  planeam_urban varchar(2),
  cap_reg_der numeric(10,3),
  cap_reg_est numeric(10,3),
  ind_puntos_luz numeric(10,3),
  pob_def_der int4,
  pob_def_est int4,
  ind_capt numeric(6,3),
  ind_pot numeric(6,3),
  ind_dep numeric(6,3),
  ind_dist numeric(6,3),
  ind_saneam numeric(6,3),
  ind_depu numeric(6,3),
  ind_pav numeric(6,3),
  ind_alum numeric(6,3),
  ind_bas numeric(6,3),
  ind_res numeric(6,3),
  ind_cul numeric(6,3),
  ind_depo numeric(6,3),
  ind_ver numeric(6,3),
  ind_adm numeric(6,3),
  ind_global numeric(6,3),
  ind_sup_der numeric(10,3),
  ind_sup_est numeric(10,3),
  CONSTRAINT indicadores_comarcales_id_key UNIQUE (id)
) 
WITH OIDS;
ALTER TABLE indicadores_comarcales OWNER TO geopista;
GRANT SELECT, UPDATE, INSERT, DELETE, REFERENCES, TRIGGER ON TABLE indicadores_comarcales TO geopista;
GRANT SELECT, UPDATE, INSERT, DELETE, REFERENCES, TRIGGER ON TABLE indicadores_comarcales TO visualizador;
GRANT SELECT ON TABLE indicadores_comarcales TO guiaurbana;
GRANT SELECT ON TABLE indicadores_comarcales TO consultas;


ALTER TABLE indicadores DROP CONSTRAINT indicadores_id_key;
ALTER TABLE indicadores ADD CONSTRAINT indicadores_id_municpio_key PRIMARY KEY (id_municipio);

ALTER TABLE indicadores DROP COLUMN id;
ALTER TABLE indicadores DROP COLUMN "GEOMETRY";

ALTER TABLE indicadores_comarcales DROP CONSTRAINT indicadores_comarcales_id_key;
ALTER TABLE indicadores_comarcales ADD CONSTRAINT indicadores_id_comarca_key PRIMARY KEY (id_comarca);
ALTER TABLE indicadores_comarcales DROP COLUMN id;


-- Creacion de los dominios

insert into Domains (ID, NAME,ID_CATEGORY)values(10177,'eiel_tipo_instalaciones_deportivas',5);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1651,'es_ES','Tipo de Instalaciones Deportivas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1651,'ga_ES','Tipo de Instalaciones Deportivas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1651,'va_ES','Tipo de Instalaciones Deportivas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1651,'eu_ES','Tipo de Instalaciones Deportivas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1651,'ca_ES','Tipo de Instalaciones Deportivas');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1840,'10177','?',1651,4,null,null);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1652,'es_ES','Tenis');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1652,'ga_ES','Tenis');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1652,'va_ES','Tenis');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1652,'eu_ES','Tenis');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1652,'ca_ES','Tenis');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1841,'10177','TE',1652,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'es_ES','Otros no especificados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'ga_ES','Otros no especificados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'va_ES','Otros no especificados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'eu_ES','Otros no especificados');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1653,'ca_ES','Otros no especificados');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1842,'10177','OT',1653,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1654,'es_ES','Balonmano');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1654,'ga_ES','Balonmano');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1654,'va_ES','Balonmano');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1654,'eu_ES','Balonmano');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1654,'ca_ES','Balonmano');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1843,'10177','BM',1654,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1655,'es_ES','Esquí Naútico');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1655,'ga_ES','Esquí Naútico');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1655,'va_ES','Esquí Naútico');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1655,'eu_ES','Esquí Naútico');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1655,'ca_ES','Esquí Naútico');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1844,'10177','EN',1655,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'es_ES','Patinaje');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'ga_ES','Patinaje');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'va_ES','Patinaje');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'eu_ES','Patinaje');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1701,'ca_ES','Patinaje');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1890,'10177','PT',1701,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1702,'es_ES','Padel');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1702,'ga_ES','Padel');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1702,'va_ES','Padel');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1702,'eu_ES','Padel');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1702,'ca_ES','Padel');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1891,'10177','PD',1702,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1703,'es_ES','Beisbol');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1703,'ga_ES','Beisbol');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1703,'va_ES','Beisbol');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1703,'eu_ES','Beisbol');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1703,'ca_ES','Beisbol');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1892,'10177','BB',1703,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1704,'es_ES','Judo/Karate');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1704,'ga_ES','Judo/Karate');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1704,'va_ES','Judo/Karate');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1704,'eu_ES','Judo/Karate');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1704,'ca_ES','Judo/Karate');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1893,'10177','JU',1704,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1705,'es_ES','Baloncesto');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1705,'ga_ES','Baloncesto');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1705,'va_ES','Baloncesto');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1705,'eu_ES','Baloncesto');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1705,'ca_ES','Baloncesto');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1894,'10177','BC',1705,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1706,'es_ES','Bolos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1706,'ga_ES','Bolos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1706,'va_ES','Bolos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1706,'eu_ES','Bolos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1706,'ca_ES','Bolos');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1895,'10177','BO',1706,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1707,'es_ES','Tiro al Plato');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1707,'ga_ES','Tiro al Plato');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1707,'va_ES','Tiro al Plato');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1707,'eu_ES','Tiro al Plato');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1707,'ca_ES','Tiro al Plato');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1896,'10177','TP',1707,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1708,'es_ES','Vela');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1708,'ga_ES','Vela');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1708,'va_ES','Vela');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1708,'eu_ES','Vela');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1708,'ca_ES','Vela');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1897,'10177','VE',1708,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1709,'es_ES','Lucha');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1709,'ga_ES','Lucha');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1709,'va_ES','Lucha');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1709,'eu_ES','Lucha');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1709,'ca_ES','Lucha');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1898,'10177','LU',1709,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1710,'es_ES','Natación');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1710,'ga_ES','Natación');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1710,'va_ES','Natación');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1710,'eu_ES','Natación');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1710,'ca_ES','Natación');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1899,'10177','NA',1710,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1711,'es_ES','Golf');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1711,'ga_ES','Golf');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1711,'va_ES','Golf');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1711,'eu_ES','Golf');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1711,'ca_ES','Golf');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1900,'10177','GF',1711,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1712,'es_ES','Fútbol');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1712,'ga_ES','Fútbol');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1712,'va_ES','Fútbol');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1712,'eu_ES','Fútbol');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1712,'ca_ES','Fútbol');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1901,'10177','FB',1712,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1713,'es_ES','Atletismo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1713,'ga_ES','Atletismo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1713,'va_ES','Atletismo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1713,'eu_ES','Atletismo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1713,'ca_ES','Atletismo');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1902,'10177','AT',1713,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1714,'es_ES','Gimnasia');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1714,'ga_ES','Gimnasia');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1714,'va_ES','Gimnasia');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1714,'eu_ES','Gimnasia');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1714,'ca_ES','Gimnasia');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1903,'10177','GI',1714,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1715,'es_ES','Pesca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1715,'ga_ES','Pesca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1715,'va_ES','Pesca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1715,'eu_ES','Pesca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1715,'ca_ES','Pesca');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1904,'10177','PE',1715,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1716,'es_ES','Actividades Subacuáticas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1716,'ga_ES','Actividades Subacuáticas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1716,'va_ES','Actividades Subacuáticas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1716,'eu_ES','Actividades Subacuáticas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1716,'ca_ES','Actividades Subacuáticas');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1905,'10177','AS',1716,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1717,'es_ES','Deportes de Invierno');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1717,'ga_ES','Deportes de Invierno');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1717,'va_ES','Deportes de Invierno');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1717,'eu_ES','Deportes de Invierno');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1717,'ca_ES','Deportes de Invierno');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1906,'10177','DI',1717,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1718,'es_ES','Balón-Volea');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1718,'ga_ES','Balón-Volea');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1718,'va_ES','Balón-Volea');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1718,'eu_ES','Balón-Volea');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1718,'ca_ES','Balón-Volea');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1907,'10177','BV',1718,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1719,'es_ES','Remo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1719,'ga_ES','Remo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1719,'va_ES','Remo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1719,'eu_ES','Remo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1719,'ca_ES','Remo');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1908,'10177','RE',1719,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1720,'es_ES','Pelota');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1720,'ga_ES','Pelota');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1720,'va_ES','Pelota');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1720,'eu_ES','Pelota');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1720,'ca_ES','Pelota');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1909,'10177','PL',1720,7,null,'1840');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1721,'es_ES','Piragüismo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1721,'ga_ES','Piragüismo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1721,'va_ES','Piragüismo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1721,'eu_ES','Piragüismo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1721,'ca_ES','Piragüismo');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1910,'10177','PR',1721,7,null,'1840');

-- Dominio de usos culturales

insert into Domains (ID, NAME,ID_CATEGORY)values(10178,'eiel_uso_centro_cultural',5);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1723,'es_ES','Uso de los centros culturales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1723,'ga_ES','Uso de los centros culturales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1723,'va_ES','Uso de los centros culturales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1723,'eu_ES','Uso de los centros culturales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1723,'ca_ES','Uso de los centros culturales');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1912,'10178','?',1723,4,null,null);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1724,'es_ES','Otros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1724,'ga_ES','Otros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1724,'va_ES','Otros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1724,'eu_ES','Otros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1724,'ca_ES','Otros');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1913,'10178','OT',1724,7,null,'1912');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1725,'es_ES','Auditorio');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1725,'ga_ES','Auditorio');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1725,'va_ES','Auditorio');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1725,'eu_ES','Auditorio');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1725,'ca_ES','Auditorio');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1914,'10178','AO',1725,7,null,'1912');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1726,'es_ES','Biblioteca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1726,'ga_ES','Biblioteca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1726,'va_ES','Biblioteca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1726,'eu_ES','Biblioteca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1726,'ca_ES','Biblioteca');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1915,'10178','BA',1726,7,null,'1912');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1727,'es_ES','Cívico Social');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1727,'ga_ES','Cívico Social');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1727,'va_ES','Cívico Social');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1727,'eu_ES','Cívico Social');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1727,'ca_ES','Cívico Social');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1916,'10178','CV',1727,7,null,'1912');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1728,'es_ES','Bar,Restaurante');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1728,'ga_ES','Bar,Restaurante');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1728,'va_ES','Bar,Restaurante');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1728,'eu_ES','Bar,Restaurante');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1728,'ca_ES','Bar,Restaurante');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1917,'10178','BR',1728,7,null,'1912');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1729,'es_ES','Documental');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1729,'ga_ES','Documental');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1729,'va_ES','Documental');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1729,'eu_ES','Documental');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1729,'ca_ES','Documental');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1918,'10178','DC',1729,7,null,'1912');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1730,'es_ES','Teatro');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1730,'ga_ES','Teatro');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1730,'va_ES','Teatro');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1730,'eu_ES','Teatro');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1730,'ca_ES','Teatro');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1919,'10178','TE',1730,7,null,'1912');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1731,'es_ES','Plaza de Toros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1731,'ga_ES','Plaza de Toros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1731,'va_ES','Plaza de Toros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1731,'eu_ES','Plaza de Toros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1731,'ca_ES','Plaza de Toros');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1920,'10178','PS',1731,7,null,'1912');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1732,'es_ES','Cine');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1732,'ga_ES','Cine');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1732,'va_ES','Cine');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1732,'eu_ES','Cine');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1732,'ca_ES','Cine');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1921,'10178','CI',1732,7,null,'1912');

  
  -- CREACIÓN DEL DOMINIO DE USOS PARA LAS CASAS CONSISTORIALES y los niveles de ensenianza

insert into Domains (ID, NAME,ID_CATEGORY)values(10180,'eiel_usos_casas_consistoriales',5);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1736,'es_ES','Usos casas consistoriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1736,'ga_ES','Usos casas consistoriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1736,'va_ES','Usos casas consistoriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1736,'eu_ES','Usos casas consistoriales');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1736,'ca_ES','Usos casas consistoriales');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1925,'10180','?',1736,4,null,null);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1737,'es_ES','Otros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1737,'ga_ES','Otros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1737,'va_ES','Otros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1737,'eu_ES','Otros');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1737,'ca_ES','Otros');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1926,'10180','OT',1737,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1738,'es_ES','Viviendas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1738,'ga_ES','Viviendas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1738,'va_ES','Viviendas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1738,'eu_ES','Viviendas');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1738,'ca_ES','Viviendas');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1927,'10180','VI',1738,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1739,'es_ES','Biblioteca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1739,'ga_ES','Biblioteca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1739,'va_ES','Biblioteca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1739,'eu_ES','Biblioteca');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1739,'ca_ES','Biblioteca');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1928,'10180','BA',1739,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1740,'es_ES','Sanitario');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1740,'ga_ES','Sanitario');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1740,'va_ES','Sanitario');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1740,'eu_ES','Sanitario');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1740,'ca_ES','Sanitario');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1929,'10180','SA',1740,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1741,'es_ES','Cívico-Social');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1741,'ga_ES','Cívico-Social');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1741,'va_ES','Cívico-Social');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1741,'eu_ES','Cívico-Social');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1741,'ca_ES','Cívico-Social');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1930,'10180','SO',1741,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1742,'es_ES','Hogar pensionista');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1742,'ga_ES','Hogar pensionista');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1742,'va_ES','Hogar pensionista');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1742,'eu_ES','Hogar pensionista');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1742,'ca_ES','Hogar pensionista');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1931,'10180','PN',1742,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1743,'es_ES','Almacenes');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1743,'ga_ES','Almacenes');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1743,'va_ES','Almacenes');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1743,'eu_ES','Almacenes');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1743,'ca_ES','Almacenes');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1932,'10180','AA',1743,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1744,'es_ES','Administración municipal');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1744,'ga_ES','Administración municipal');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1744,'va_ES','Administración municipal');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1744,'eu_ES','Administración municipal');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1744,'ca_ES','Administración municipal');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1933,'10180','AM',1744,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1745,'es_ES','Cine o teatro');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1745,'ga_ES','Cine o teatro');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1745,'va_ES','Cine o teatro');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1745,'eu_ES','Cine o teatro');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1745,'ca_ES','Cine o teatro');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1934,'10180','CI',1745,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1746,'es_ES','Museo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1746,'ga_ES','Museo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1746,'va_ES','Museo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1746,'eu_ES','Museo');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1746,'ca_ES','Museo');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1935,'10180','MO',1746,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1747,'es_ES','Ocio y cafetería');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1747,'ga_ES','Ocio y cafetería');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1747,'va_ES','Ocio y cafetería');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1747,'eu_ES','Ocio y cafetería');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1747,'ca_ES','Ocio y cafetería');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1936,'10180','BR',1747,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1748,'es_ES','Otros usos administrativos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1748,'ga_ES','Otros usos administrativos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1748,'va_ES','Otros usos administrativos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1748,'eu_ES','Otros usos administrativos');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1748,'ca_ES','Otros usos administrativos');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1937,'10180','OA',1748,7,null,'1925');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1749,'es_ES','Asistencial');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1749,'ga_ES','Asistencial');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1749,'va_ES','Asistencial');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1749,'eu_ES','Asistencial');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1749,'ca_ES','Asistencial');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1938,'10180','AS',1749,7,null,'1925');
insert into Domains (ID, NAME,ID_CATEGORY)values(10181,'eiel_niveles_centros_ensenianza',5);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1750,'es_ES','Niveles centros enseñanza');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1750,'ga_ES','Niveles centros enseñanza');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1750,'va_ES','Niveles centros enseñanza');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1750,'eu_ES','Niveles centros enseñanza');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1750,'ca_ES','Niveles centros enseñanza');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1939,'10181','?',1750,4,null,null);
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1751,'es_ES','Educación especial');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1751,'ga_ES','Educación especial');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1751,'va_ES','Educación especial');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1751,'eu_ES','Educación especial');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1751,'ca_ES','Educación especial');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1940,'10181','ESP',1751,7,null,'1939');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1752,'es_ES','Bachillerato');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1752,'ga_ES','Bachillerato');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1752,'va_ES','Bachillerato');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1752,'eu_ES','Bachillerato');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1752,'ca_ES','Bachillerato');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1941,'10181','BAC',1752,7,null,'1939');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1753,'es_ES','Secundaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1753,'ga_ES','Secundaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1753,'va_ES','Secundaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1753,'eu_ES','Secundaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1753,'ca_ES','Secundaria');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1942,'10181','ESO',1753,7,null,'1939');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1754,'es_ES','Educación infantil');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1754,'ga_ES','Educación infantil');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1754,'va_ES','Educación infantil');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1754,'eu_ES','Educación infantil');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1754,'ca_ES','Educación infantil');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1943,'10181','INF',1754,7,null,'1939');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1755,'es_ES','Formación profesional grado 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1755,'ga_ES','Formación profesional grado 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1755,'va_ES','Formación profesional grado 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1755,'eu_ES','Formación profesional grado 1');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1755,'ca_ES','Formación profesional grado 1');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1944,'10181','FP1',1755,7,null,'1939');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1756,'es_ES','Otras enseñanzas no universitarias');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1756,'ga_ES','Otras enseñanzas no universitarias');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1756,'va_ES','Otras enseñanzas no universitarias');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1756,'eu_ES','Otras enseñanzas no universitarias');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1756,'ca_ES','Otras enseñanzas no universitarias');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1945,'10181','OTR',1756,7,null,'1939');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1757,'es_ES','Primaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1757,'ga_ES','Primaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1757,'va_ES','Primaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1757,'eu_ES','Primaria');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1757,'ca_ES','Primaria');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1946,'10181','PRI',1757,7,null,'1939');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1758,'es_ES','Formación profesional grado superior');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1758,'ga_ES','Formación profesional grado superior');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1758,'va_ES','Formación profesional grado superior');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1758,'eu_ES','Formación profesional grado superior');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values(1758,'ca_ES','Formación profesional grado superior');
insert into domainnodes (id,id_domain, pattern,  ID_DESCRIPTION, "type",id_municipio, parentdomain)
  values(1947,'10181','FP2',1758,7,null,'1939');
delete from dictionary where id_vocablo='1755';
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1755','ga_ES','Formación profesional grado medio');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1755','es_ES','Formación profesional grado medio');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1755','va_ES','Formación profesional grado medio');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1755','eu_ES','Formación profesional grado medio');
insert into DICTIONARY (ID_VOCABLO,LOCALE,TRADUCCION)values('1755','ca_ES','Formación profesional grado medio');
update domainnodes set pattern='FP1', id_description='1755' where id='1944';


-- Modificaciones para los usuarios provinciales

<-- Supresión del campo " id_municipio" de la tabla" isueruserhdr" y creación de la tabla relacional "r_user_municipio" -->
ALTER TABLE iuseruserhdr DROP id_municipio;
  CREATE TABLE r_user_municipio(
  userid numeric(10) NOT NULL,
  id_municipio numeric(5) NOT NULL,
  CONSTRAINT pk_user_municipio PRIMARY KEY (userid, id_municipio),
  CONSTRAINT fk_r_user_mun_reference_iuseruser FOREIGN KEY (userid) REFERENCES iuseruserhdr (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT pk_key_r_user_municipio UNIQUE (userid, id_municipio));  
  	<-- Idem para la tabla que maneja ROLES en vez de USUARIOS -->
ALTER TABLE iusergrouphdr DROP id_municipio;
CREATE TABLE r_group_municipio(
  groupid numeric(10) NOT NULL,
  id_municipio numeric(5) NOT NULL,
  CONSTRAINT fk_r_group_mun_reference_iusergroup FOREIGN KEY (groupid) REFERENCES iusergrouphdr (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT pk_key_r_group_municipio UNIQUE (groupid, id_municipio)); 
	<--  Consultas del "query_catalog"  ("allroles", "allrolespermisos", "allusuarios", "allrolespermisos")  que actúen sobre la clave ID_MUNICIPIO -->    
UPDATE query_catalog SET query='select id, name,  remarks from iusergrouphdr where iusergrouphdr.id=(select groupid from r_group_municipio where r_group_municipio.id_municipio=? or id_municipio=0)' WHERE id='allroles';
UPDATE query_catalog SET query='select iusergrouphdr.id, iusergrouphdr.name, iusergrouphdr.remarks, r_group_perm.idperm, r_group_perm.idacl from iusergrouphdr left join r_group_perm on iusergrouphdr.id=r_group_perm.groupid where iusergrouphdr.id=(select groupid from r_group_municipio where id_municipio=? or id_municipio=0) ' WHERE id='allrolespermisos';
UPDATE query_catalog SET query='select iuseruserhdr.id as id, iuseruserhdr.name as name, iuseruserhdr.nombrecompleto as nombrecompleto, iuseruserhdr.password as password, iuseruserhdr.remarks as remarks, iuseruserhdr.mail as mail, iuseruserhdr.deptid as deptid, iuseruserhdr.nif as nif FROM iuseruserhdr, r_user_municipio WHERE borrado!=1 and iuseruserhdr.id=r_user_municipio.userid AND r_user_municipio.id_municipio=?'  WHERE id='allusuarios';
UPDATE query_catalog SET query='select iusergrouphdr.name, iusergrouphdr.remarks, r_group_perm.idperm, r_group_perm.idacl from iusergrouphdr left join r_group_perm on iusergrouphdr.id=r_group_perm.groupid where iusergrouphdr.id=(select groupid from r_group_municipio where id_municipio=? or id_municipio=0) '  WHERE id='allrolespermisos';
	<-- Creamos la entrada "allusuariosmunicipios" en el query_catalog -->
INSERT INTO query_catalog (id, query, acl, idperm) VALUES ('allusuariosmunicipios','select userid , id_municipio from r_user_municipio order by userid', 1, 9205);
	<-- Creamos la entrada "getRolesUsuario" en el query_catalog (nos da todos los Roles para el Usuario especificado ) -->
INSERT INTO query_catalog (id, query, acl, idperm) VALUES ('getRolesUsuario','select id,name,remarks from iusergrouphdr where id=(select groupid  from iusergroupuser where userid=?)', 1, 9205);
	<-- Creamos la entrada "getMunicipiosUsuario" en el query_catalog(nos da todos los Municipios para el Usuario especificado ) -->
INSERT INTO query_catalog (id, query, acl, idperm) VALUES ('getMunicipiosUsuario','select municipios.id as id, municipios.id_provincia as id_provincia ,municipios.nombreoficial as nombre, (select nombreoficial as nombre_provincia from provincias where provincias.id=municipios.id_provincia) as provincia from municipios, r_user_municipio where  r_user_municipio.userid=? and (r_user_municipio.id_municipio=0 or municipios.id=r_user_municipio.id_municipio)', 1, 9205);
<-- Hay que definir TODOS los Municipios relativos a un Usuario  (INSERT en " r_user_municipio" y " r_group_municipio")  -->
--
--
--
<-- Cuando se queire dar acceso a TODOS los Municipios, se pone como id_municipio el "0" (cero) --> 
INSERT INTO r_group_municipio (groupid,id_municipio) VALUES(110,0);
INSERT INTO r_user_municipio (userid,id_municipio) VALUES(100,0);


-- Creación de las capas y tablas de obras

CREATE TABLE "inv_media_anual" () WITH OIDS;
 ALTER TABLE "inv_media_anual" OWNER TO geopista;
 GRANT ALL ON TABLE "inv_media_anual" TO visualizador;
 GRANT SELECT ON TABLE "inv_media_anual" TO guiaurbana;
 GRANT SELECT ON TABLE "inv_media_anual" TO consultas;
insert into tables (id_table, name, geometrytype) values(nextval('seq_tables'),'inv_media_anual',null);
ALTER TABLE "inv_media_anual" ADD COLUMN "id" NUMERIC(8)  UNIQUE;
 ALTER TABLE "inv_media_anual" ALTER COLUMN "id" SET NOT NULL;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'id',null,8,0,2);
ALTER TABLE "inv_media_anual" ADD COLUMN "id_municipio" NUMERIC(5) ;
ALTER TABLE "inv_media_anual" ALTER COLUMN "id_municipio" SET NOT NULL;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'id_municipio',null,5,0,2);
ALTER TABLE "inv_media_anual" ADD COLUMN "nomayto" VARCHAR(255) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'nomayto',255,null,null,3);
ALTER TABLE "inv_media_anual" ADD COLUMN "prto2005" NUMERIC(15,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'prto2005',null,15,3,2);
ALTER TABLE "inv_media_anual" ADD COLUMN "prto2006" NUMERIC(15,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'prto2006',null,15,3,2);
ALTER TABLE "inv_media_anual" ADD COLUMN "prto2007" NUMERIC(15,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'prto2007',null,15,3,2);
ALTER TABLE "inv_media_anual" ADD COLUMN "prto2008" NUMERIC(15,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'prto2008',null,15,3,2);
ALTER TABLE "inv_media_anual" ADD COLUMN "prto2009" NUMERIC(15,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'prto2009',null,15,3,2);
ALTER TABLE "inv_media_anual" ADD COLUMN "totalinv" NUMERIC(15,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'totalinv',null,15,3,2);
ALTER TABLE "inv_media_anual" ADD COLUMN "invmediaanual" NUMERIC(15,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'invmediaanual',null,15,3,2);
ALTER TABLE "inv_media_anual" ADD COLUMN "invhab" NUMERIC(15,3) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10198,'invhab',null,15,3,2);
ALTER TABLE "inv_media_anual" DROP COLUMN "id";
delete from columns_domains where id_column=43189;delete from columns where id=43189;
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Obras');
insert into dictionary (id_vocablo,locale, traduccion) values (227648,'es_ES','Obras');
insert into dictionary (id_vocablo,locale, traduccion) values (227648,'va_ES','Obras');
insert into dictionary (id_vocablo,locale, traduccion) values (227648,'eu_ES','Obras');
insert into dictionary (id_vocablo,locale, traduccion) values (227648,'ca_ES','Obras');
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','Obras');
insert into layerfamilies (id_layerfamily,id_name,id_description) values (nextval('seq_layerfamilies'),227648,227649 );
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Inversión Media Anual');
insert into dictionary (id_vocablo,locale, traduccion) values (227650,'es_ES','Inversión Media Anual');
insert into dictionary (id_vocablo,locale, traduccion) values (227650,'va_ES','Inversión Media Anual');
insert into dictionary (id_vocablo,locale, traduccion) values (227650,'eu_ES','Inversión Media Anual');
insert into dictionary (id_vocablo,locale, traduccion) values (227650,'ca_ES','Inversión Media Anual');
insert into styles (id_style, xml) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?> <StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"> <NamedLayer><Name>inv_media_anual</Name><UserStyle><Name>default:inv_media_anual</Name><Title>default:inv_media_anual</Title><Abstract>default:inv_media_anual</Abstract><FeatureTypeStyle><Name>inv_media_anual</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill'>#ffffff</CssParameter><CssParameter name='fill-opacity'>1.0</CssParameter></Fill><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#339900</CssParameter><CssParameter name='stroke-linejoin'>mitre</CssParameter><CssParameter name='stroke-linecap'>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#0066ff</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name='fill'>#ffffff</CssParameter><CssParameter name='fill-opacity'>1.0</CssParameter></Fill><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
insert into layers (id_layer, id_name, name, id_styles, acl, modificable) values (nextval('seq_layers'),227650,'inv_media_anual',274,'12',1);
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (318,11213,1);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','ID del Municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227651,'es_ES','ID del Municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227651,'va_ES','ID del Municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227651,'eu_ES','ID del Municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227651,'ca_ES','ID del Municipio');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227651,11213,43190,1,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Nombre Ayuntamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227652,'es_ES','Nombre Ayuntamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227652,'va_ES','Nombre Ayuntamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227652,'eu_ES','Nombre Ayuntamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227652,'ca_ES','Nombre Ayuntamiento');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227652,11213,43191,2,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Presupuesto 2005');
insert into dictionary (id_vocablo,locale, traduccion) values (227653,'es_ES','Presupuesto 2005');
insert into dictionary (id_vocablo,locale, traduccion) values (227653,'va_ES','Presupuesto 2005');
insert into dictionary (id_vocablo,locale, traduccion) values (227653,'eu_ES','Presupuesto 2005');
insert into dictionary (id_vocablo,locale, traduccion) values (227653,'ca_ES','Presupuesto 2005');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227653,11213,43192,3,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Presupuesto 2006');
insert into dictionary (id_vocablo,locale, traduccion) values (227654,'es_ES','Presupuesto 2006');
insert into dictionary (id_vocablo,locale, traduccion) values (227654,'va_ES','Presupuesto 2006');
insert into dictionary (id_vocablo,locale, traduccion) values (227654,'eu_ES','Presupuesto 2006');
insert into dictionary (id_vocablo,locale, traduccion) values (227654,'ca_ES','Presupuesto 2006');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227654,11213,43193,4,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Presupuesto 2007');
insert into dictionary (id_vocablo,locale, traduccion) values (227655,'es_ES','Presupuesto 2007');
insert into dictionary (id_vocablo,locale, traduccion) values (227655,'va_ES','Presupuesto 2007');
insert into dictionary (id_vocablo,locale, traduccion) values (227655,'eu_ES','Presupuesto 2007');
insert into dictionary (id_vocablo,locale, traduccion) values (227655,'ca_ES','Presupuesto 2007');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227655,11213,43194,5,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Presupuesto 2008');
insert into dictionary (id_vocablo,locale, traduccion) values (227656,'es_ES','Presupuesto 2008');
insert into dictionary (id_vocablo,locale, traduccion) values (227656,'va_ES','Presupuesto 2008');
insert into dictionary (id_vocablo,locale, traduccion) values (227656,'eu_ES','Presupuesto 2008');
insert into dictionary (id_vocablo,locale, traduccion) values (227656,'ca_ES','Presupuesto 2008');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227656,11213,43195,6,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Presupuesto 2009');
insert into dictionary (id_vocablo,locale, traduccion) values (227657,'es_ES','Presupuesto 2009');
insert into dictionary (id_vocablo,locale, traduccion) values (227657,'va_ES','Presupuesto 2009');
insert into dictionary (id_vocablo,locale, traduccion) values (227657,'eu_ES','Presupuesto 2009');
insert into dictionary (id_vocablo,locale, traduccion) values (227657,'ca_ES','Presupuesto 2009');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227657,11213,43196,7,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Inversión media anual');
insert into dictionary (id_vocablo,locale, traduccion) values (227658,'es_ES','Inversión media anual');
insert into dictionary (id_vocablo,locale, traduccion) values (227658,'va_ES','Inversión media anual');
insert into dictionary (id_vocablo,locale, traduccion) values (227658,'eu_ES','Inversión media anual');
insert into dictionary (id_vocablo,locale, traduccion) values (227658,'ca_ES','Inversión media anual');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227658,11213,43198,8,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Inversión media por habitante');
insert into dictionary (id_vocablo,locale, traduccion) values (227659,'es_ES','Inversión media por habitante');
insert into dictionary (id_vocablo,locale, traduccion) values (227659,'va_ES','Inversión media por habitante');
insert into dictionary (id_vocablo,locale, traduccion) values (227659,'eu_ES','Inversión media por habitante');
insert into dictionary (id_vocablo,locale, traduccion) values (227659,'ca_ES','Inversión media por habitante');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227659,11213,43199,9,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Inversión total');
insert into dictionary (id_vocablo,locale, traduccion) values (227660,'es_ES','Inversión total');
insert into dictionary (id_vocablo,locale, traduccion) values (227660,'va_ES','Inversión total');
insert into dictionary (id_vocablo,locale, traduccion) values (227660,'eu_ES','Inversión total');
insert into dictionary (id_vocablo,locale, traduccion) values (227660,'ca_ES','Inversión total');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227660,11213,43197,10,true);
insert into queries (id, id_layer, databasetype, selectquery, updatequery, insertquery, deletequery) values (nextval('seq_querys'),11213,1,'SELECT transform("eiel_c_municipios"."GEOMETRY",23030) as "GEOMETRY", "inv_media_anual"."id_municipio","inv_media_anual"."nomayto","inv_media_anual"."prto2005","inv_media_anual"."prto2006","inv_media_anual"."prto2007","inv_media_anual"."prto2008","inv_media_anual"."prto2009","inv_media_anual"."invmediaanual","inv_media_anual"."invhab","inv_media_anual"."totalinv" FROM "inv_media_anual" left join "eiel_c_municipios" on "eiel_c_municipios"."id_municipio"="inv_media_anual"."id_municipio"WHERE substr("inv_media_anual"."id_municipio",1,2)=substr(?M,1,2) and "inv_media_anual"."id_municipio" is not null',null,null,null);
CREATE TABLE "estado_obras" () WITH OIDS;
 ALTER TABLE "estado_obras" OWNER TO geopista;
 GRANT ALL ON TABLE "estado_obras" TO visualizador;
 GRANT SELECT ON TABLE "estado_obras" TO guiaurbana;
 GRANT SELECT ON TABLE "estado_obras" TO consultas;
insert into tables (id_table, name, geometrytype) values(nextval('seq_tables'),'estado_obras',null);
ALTER TABLE "estado_obras" ADD COLUMN "id" NUMERIC(8)  UNIQUE;
 ALTER TABLE "estado_obras" ALTER COLUMN "id" SET NOT NULL;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'id',null,8,0,2);
ALTER TABLE "estado_obras" ADD COLUMN "id_municipio" NUMERIC(5) ;
ALTER TABLE "estado_obras" ALTER COLUMN "id_municipio" SET NOT NULL;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'id_municipio',null,5,0,2);
ALTER TABLE "estado_obras" ADD COLUMN "nomayto" VARCHAR(255) ;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'nomayto',255,null,null,3);
ALTER TABLE "estado_obras" ADD COLUMN "autorizadas" NUMERIC;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'autorizadas',null,0,0,2);
ALTER TABLE "estado_obras" ADD COLUMN "anuladas" INTEGER;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'anuladas',null,0,0,2);
CREATE LOCAL TEMPORARY TABLE "estado_obras1241620830310" ("id" numeric ,"id_municipio" numeric ,"nomayto" varchar ,"autorizadas" numeric ,"anuladas" int4 ) WITH OIDS;
 INSERT INTO "estado_obras1241620830310" ("id","id_municipio","nomayto","autorizadas","anuladas") SELECT "id","id_municipio","nomayto","autorizadas","anuladas" FROM "estado_obras";
DROP TABLE "estado_obras";
CREATE TABLE "estado_obras" () WITH OIDS;
 ALTER TABLE "estado_obras" OWNER TO geopista;
 GRANT ALL ON TABLE "estado_obras" TO visualizador;
 GRANT SELECT ON TABLE "estado_obras" TO guiaurbana;
 GRANT SELECT ON TABLE "estado_obras" TO consultas;
ALTER TABLE "estado_obras" ADD COLUMN "id" NUMERIC(8)  UNIQUE;
 ALTER TABLE "estado_obras" ALTER COLUMN "id" SET NOT NULL;
ALTER TABLE "estado_obras" ADD COLUMN "id_municipio" NUMERIC(5) ;
 ALTER TABLE "estado_obras" ALTER COLUMN "id_municipio" SET NOT NULL;
ALTER TABLE "estado_obras" ADD COLUMN "nomayto" VARCHAR(255) ;
 ALTER TABLE "estado_obras" ADD COLUMN "autorizadas" INTEGER;
 ALTER TABLE "estado_obras" ADD COLUMN "anuladas" INTEGER;
 INSERT INTO "estado_obras" ("id","id_municipio","nomayto","autorizadas","anuladas") SELECT "id","id_municipio","nomayto", CAST("autorizadas" AS INTEGER),"anuladas" FROM "estado_obras1241620830310";
update columns set name='autorizadas', "Length"=null, "Precision"=0, "Scale"=0, "Type"=0 where id=43203;
ALTER TABLE "estado_obras" ADD COLUMN "revocadas" INTEGER;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'revocadas',null,0,0,2);
ALTER TABLE "estado_obras" ADD COLUMN "contratadas" INTEGER;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'contratadas',null,0,0,2);
ALTER TABLE "estado_obras" ADD COLUMN "enejecucion" INTEGER;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'enejecucion',null,0,0,2);
ALTER TABLE "estado_obras" ADD COLUMN "liquidadas" INTEGER;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'liquidadas',null,0,0,2);
ALTER TABLE "estado_obras" ADD COLUMN "total" INTEGER;
insert into columns (id,id_table,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),10199,'total',null,0,0,2);
ALTER TABLE "estado_obras" DROP COLUMN "id";
delete from columns_domains where id_column=43200;delete from columns where id=43200;
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Estado de las Obras');
insert into dictionary (id_vocablo,locale, traduccion) values (227661,'es_ES','Estado de las Obras');
insert into dictionary (id_vocablo,locale, traduccion) values (227661,'va_ES','Estado de las Obras');
insert into dictionary (id_vocablo,locale, traduccion) values (227661,'eu_ES','Estado de las Obras');
insert into dictionary (id_vocablo,locale, traduccion) values (227661,'ca_ES','Estado de las Obras');
insert into styles (id_style, xml) values(nextval('seq_styles'),'<?xml version="1.0" encoding="ISO-8859-1"?> <StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"> <NamedLayer><Name>estado_obras</Name><UserStyle><Name>default:estado_obras</Name><Title>default:estado_obras</Title><Abstract>default:estado_obras</Abstract><FeatureTypeStyle><Name>estado_obras</Name><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PolygonSymbolizer><Fill><CssParameter name='fill'>#ffffff</CssParameter><CssParameter name='fill-opacity'>1.0</CssParameter></Fill><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#339900</CssParameter><CssParameter name='stroke-linejoin'>mitre</CssParameter><CssParameter name='stroke-linecap'>butt</CssParameter></Stroke></PolygonSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#0066ff</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></LineSymbolizer></Rule><Rule><Name>default</Name><Title>default</Title><Abstract>default</Abstract><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><PointSymbolizer><Graphic><Mark><WellKnownName>square</WellKnownName><Fill><CssParameter name='fill'>#ffffff</CssParameter><CssParameter name='fill-opacity'>1.0</CssParameter></Fill><Stroke><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke'>#000000</CssParameter><CssParameter name='stroke-linejoin'>round</CssParameter><CssParameter name='stroke-linecap'>round</CssParameter></Stroke></Mark><Opacity>1.0</Opacity><Size><ogc:Literal>5.0</ogc:Literal></Size><Rotation>0.0</Rotation></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>');
insert into layers (id_layer, id_name, name, id_styles, acl, modificable) values (nextval('seq_layers'),227661,'estado_obras',275,'12',1);
insert into layerfamilies_layers_relations (id_layerfamily, id_layer, position) values (318,11214,2);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','ID del municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227662,'es_ES','ID del municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227662,'va_ES','ID del municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227662,'eu_ES','ID del municipio');
insert into dictionary (id_vocablo,locale, traduccion) values (227662,'ca_ES','ID del municipio');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227662,11214,43201,1,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Nombre Ayuntamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227663,'es_ES','Nombre Ayuntamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227663,'va_ES','Nombre Ayuntamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227663,'eu_ES','Nombre Ayuntamiento');
insert into dictionary (id_vocablo,locale, traduccion) values (227663,'ca_ES','Nombre Ayuntamiento');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227663,11214,43202,2,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Anuladas');
insert into dictionary (id_vocablo,locale, traduccion) values (227664,'es_ES','Anuladas');
insert into dictionary (id_vocablo,locale, traduccion) values (227664,'va_ES','Anuladas');
insert into dictionary (id_vocablo,locale, traduccion) values (227664,'eu_ES','Anuladas');
insert into dictionary (id_vocablo,locale, traduccion) values (227664,'ca_ES','Anuladas');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227664,11214,43204,3,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Autorizadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227665,'es_ES','Autorizadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227665,'va_ES','Autorizadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227665,'eu_ES','Autorizadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227665,'ca_ES','Autorizadas');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227665,11214,43203,4,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Contratadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227666,'es_ES','Contratadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227666,'va_ES','Contratadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227666,'eu_ES','Contratadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227666,'ca_ES','Contratadas');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227666,11214,43206,5,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','En ejecución');
insert into dictionary (id_vocablo,locale, traduccion) values (227667,'es_ES','En ejecución');
insert into dictionary (id_vocablo,locale, traduccion) values (227667,'va_ES','En ejecución');
insert into dictionary (id_vocablo,locale, traduccion) values (227667,'eu_ES','En ejecución');
insert into dictionary (id_vocablo,locale, traduccion) values (227667,'ca_ES','En ejecución');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227667,11214,43207,6,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Liquidadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227668,'es_ES','Liquidadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227668,'va_ES','Liquidadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227668,'eu_ES','Liquidadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227668,'ca_ES','Liquidadas');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227668,11214,43208,7,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Revocadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227669,'es_ES','Revocadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227669,'va_ES','Revocadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227669,'eu_ES','Revocadas');
insert into dictionary (id_vocablo,locale, traduccion) values (227669,'ca_ES','Revocadas');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227669,11214,43205,8,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'ga_ES','Totales');
insert into dictionary (id_vocablo,locale, traduccion) values (227670,'es_ES','Totales');
insert into dictionary (id_vocablo,locale, traduccion) values (227670,'va_ES','Totales');
insert into dictionary (id_vocablo,locale, traduccion) values (227670,'eu_ES','Totales');
insert into dictionary (id_vocablo,locale, traduccion) values (227670,'ca_ES','Totales');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227670,11214,43209,9,true);
insert into queries (id, id_layer, databasetype, selectquery, updatequery, insertquery, deletequery) values (nextval('seq_querys'),11214,1,'SELECT transform("eiel_c_municipios"."GEOMETRY",23030) as "GEOMETRY", "estado_obras"."id_municipio","estado_obras"."nomayto","estado_obras"."anuladas","estado_obras"."autorizadas","estado_obras"."contratadas","estado_obras"."enejecucion","estado_obras"."liquidadas","estado_obras"."revocadas","estado_obras"."total" FROM "estado_obras" left join "eiel_c_municipios" on "eiel_c_municipios"."id_municipio"="estado_obras"."id_municipio"WHERE substr("estado_obras"."id_municipio",1,2)=substr(?M,1,2) and "estado_obras"."id_municipio" is not null',null,null,null);


update query_catalog set query='select id, name,  remarks from iusergrouphdr where iusergrouphdr.id IN (select groupid from r_group_municipio where r_group_municipio.id_municipio=22226 or id_municipio=0)' where id='allroles';


update query_catalog set query='select iusergrouphdr.id as id, iusergrouphdr.name as name, iusergrouphdr.remarks as remarks, r_group_perm.idperm as idperm, r_group_perm.idacl as idacl from iusergrouphdr left join r_group_perm on iusergrouphdr.id=r_group_perm.groupid where iusergrouphdr.id IN (select groupid from r_group_municipio where id_municipio=? or id_municipio=0)' where id='allrolespermisos';

update query_catalog set query='select iuseruserhdr.id as id, iuseruserhdr.name as name, iuseruserhdr.nombrecompleto as nombrecompleto, iuseruserhdr.password as password, iuseruserhdr.remarks as remarks, iuseruserhdr.mail as mail, iuseruserhdr.deptid as deptid, iuseruserhdr.nif as nif FROM iuseruserhdr left join r_user_municipio on iuseruserhdr.id=r_user_municipio.userid WHERE borrado!=1 and (r_user_municipio.id_municipio=? or r_user_municipio.id_municipio=0)' where id='allusuarios';

insert into r_user_municipio (userid, id_municipio) values (154,22226);
insert into r_user_municipio (userid, id_municipio) values (155,22001);
insert into r_user_municipio (userid, id_municipio) values (156,22001);
insert into r_user_municipio (userid, id_municipio) values (200,0);
insert into r_user_municipio (userid, id_municipio) values (201,0);
insert into r_user_municipio (userid, id_municipio) values (202,0);
insert into r_user_municipio (userid, id_municipio) values (204,0);
insert into r_user_municipio (userid, id_municipio) values (101,22226);

-- Corrección de las capas de obras

update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='es_ES';
update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='ga_ES';
update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='va_ES';
update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='eu_ES';
update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='ca_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='es_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='ga_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='va_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='eu_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='ca_ES';
update attributes set id_alias =227662, id_layer=11214, id_column=43201, position=1, editable=true where id=103086;
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='es_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='ga_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='va_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='eu_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='ca_ES';
update attributes set id_alias =227663, id_layer=11214, id_column=43202, position=2, editable=true where id=103087;
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='es_ES';
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='ga_ES';
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='va_ES';
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='eu_ES';
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='ca_ES';
update attributes set id_alias =227664, id_layer=11214, id_column=43204, position=3, editable=true where id=103088;
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='es_ES';
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='ga_ES';
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='va_ES';
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='eu_ES';
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='ca_ES';
update attributes set id_alias =227665, id_layer=11214, id_column=43203, position=4, editable=true where id=103089;
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='es_ES';
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='ga_ES';
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='va_ES';
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='eu_ES';
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='ca_ES';
update attributes set id_alias =227666, id_layer=11214, id_column=43206, position=5, editable=true where id=103090;
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='es_ES';
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='ga_ES';
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='va_ES';
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='eu_ES';
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='ca_ES';
update attributes set id_alias =227667, id_layer=11214, id_column=43207, position=6, editable=true where id=103091;
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='es_ES';
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='ga_ES';
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='va_ES';
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='eu_ES';
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='ca_ES';
update attributes set id_alias =227668, id_layer=11214, id_column=43208, position=7, editable=true where id=103092;
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='es_ES';
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='ga_ES';
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='va_ES';
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='eu_ES';
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='ca_ES';
update attributes set id_alias =227669, id_layer=11214, id_column=43205, position=8, editable=true where id=103093;
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='es_ES';
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='ga_ES';
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='va_ES';
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='eu_ES';
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='ca_ES';
update attributes set id_alias =227670, id_layer=11214, id_column=43209, position=9, editable=true where id=103094;
update layers set name='estado_obras', acl='12' where id_layer=11214;
update queries set selectquery='SELECT transform("eiel_c_municipios"."GEOMETRY",23030) as "GEOMETRY", "estado_obras"."id_municipio","estado_obras"."nomayto","estado_obras"."anuladas","estado_obras"."autorizadas","estado_obras"."contratadas","estado_obras"."enejecucion","estado_obras"."liquidadas","estado_obras"."revocadas","estado_obras"."total" FROM "estado_obras" left join "eiel_c_municipios" on "eiel_c_municipios"."id_municipio"="estado_obras"."id_municipio" WHERE substr("estado_obras"."id_municipio",1,2)=substr(?M,1,2) and "estado_obras"."id_municipio" is not null', updatequery=null, insertquery=null, deletequery=null where id=10218;
update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='ga_ES';
update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='es_ES';
update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='va_ES';
update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='eu_ES';
update dictionary set traduccion='Estado de las Obras' where id_vocablo=227661 and locale='ca_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','GEOMETRY');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227671,11214,41370,1,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227672,11214,41368,2,true);
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='ga_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='es_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='va_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='eu_ES';
update dictionary set traduccion='ID del municipio' where id_vocablo=227662 and locale='ca_ES';
update attributes set id_alias =227662, id_layer=11214, id_column=43201, position=3, editable=true where id=103086;
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='ga_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='es_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='va_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='eu_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227663 and locale='ca_ES';
update attributes set id_alias =227663, id_layer=11214, id_column=43202, position=4, editable=true where id=103087;
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='ga_ES';
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='es_ES';
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='va_ES';
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='eu_ES';
update dictionary set traduccion='Anuladas' where id_vocablo=227664 and locale='ca_ES';
update attributes set id_alias =227664, id_layer=11214, id_column=43204, position=5, editable=true where id=103088;
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='ga_ES';
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='es_ES';
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='va_ES';
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='eu_ES';
update dictionary set traduccion='Autorizadas' where id_vocablo=227665 and locale='ca_ES';
update attributes set id_alias =227665, id_layer=11214, id_column=43203, position=6, editable=true where id=103089;
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='ga_ES';
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='es_ES';
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='va_ES';
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='eu_ES';
update dictionary set traduccion='Contratadas' where id_vocablo=227666 and locale='ca_ES';
update attributes set id_alias =227666, id_layer=11214, id_column=43206, position=7, editable=true where id=103090;
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='ga_ES';
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='es_ES';
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='va_ES';
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='eu_ES';
update dictionary set traduccion='En ejecución' where id_vocablo=227667 and locale='ca_ES';
update attributes set id_alias =227667, id_layer=11214, id_column=43207, position=8, editable=true where id=103091;
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='ga_ES';
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='es_ES';
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='va_ES';
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='eu_ES';
update dictionary set traduccion='Liquidadas' where id_vocablo=227668 and locale='ca_ES';
update attributes set id_alias =227668, id_layer=11214, id_column=43208, position=9, editable=true where id=103092;
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='ga_ES';
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='es_ES';
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='va_ES';
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='eu_ES';
update dictionary set traduccion='Revocadas' where id_vocablo=227669 and locale='ca_ES';
update attributes set id_alias =227669, id_layer=11214, id_column=43205, position=10, editable=true where id=103093;
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='ga_ES';
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='es_ES';
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='va_ES';
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='eu_ES';
update dictionary set traduccion='Totales' where id_vocablo=227670 and locale='ca_ES';
update attributes set id_alias =227670, id_layer=11214, id_column=43209, position=11, editable=true where id=103094;
update layers set name='estado_obras', acl='12' where id_layer=11214;
update queries set selectquery='SELECT transform("eiel_c_municipios"."GEOMETRY",23030) as "GEOMETRY", "eiel_c_municipios"."id" as id, "estado_obras"."id_municipio","estado_obras"."nomayto","estado_obras"."anuladas","estado_obras"."autorizadas","estado_obras"."contratadas","estado_obras"."enejecucion","estado_obras"."liquidadas","estado_obras"."revocadas","estado_obras"."total" FROM "estado_obras" left join "eiel_c_municipios" on "eiel_c_municipios"."id_municipio"="estado_obras"."id_municipio" WHERE substr("estado_obras"."id_municipio",1,2)=substr(?M,1,2) and "estado_obras"."id_municipio" is not null', updatequery=null, insertquery=null, deletequery=null where id=10218;
update dictionary set traduccion='Inversión Media Anual' where id_vocablo=227650 and locale='es_ES';
update dictionary set traduccion='Inversión Media Anual' where id_vocablo=227650 and locale='ga_ES';
update dictionary set traduccion='Inversión Media Anual' where id_vocablo=227650 and locale='va_ES';
update dictionary set traduccion='Inversión Media Anual' where id_vocablo=227650 and locale='eu_ES';
update dictionary set traduccion='Inversión Media Anual' where id_vocablo=227650 and locale='ca_ES';
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','GEOMETRY');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227673,11213,41370,1,true);
insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','id');
insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),227674,11213,41368,2,true);
update dictionary set traduccion='ID del Municipio' where id_vocablo=227651 and locale='es_ES';
update dictionary set traduccion='ID del Municipio' where id_vocablo=227651 and locale='ga_ES';
update dictionary set traduccion='ID del Municipio' where id_vocablo=227651 and locale='va_ES';
update dictionary set traduccion='ID del Municipio' where id_vocablo=227651 and locale='eu_ES';
update dictionary set traduccion='ID del Municipio' where id_vocablo=227651 and locale='ca_ES';
update attributes set id_alias =227651, id_layer=11213, id_column=43190, position=3, editable=true where id=103076;
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227652 and locale='es_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227652 and locale='ga_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227652 and locale='va_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227652 and locale='eu_ES';
update dictionary set traduccion='Nombre Ayuntamiento' where id_vocablo=227652 and locale='ca_ES';
update attributes set id_alias =227652, id_layer=11213, id_column=43191, position=4, editable=true where id=103077;
update dictionary set traduccion='Presupuesto 2005' where id_vocablo=227653 and locale='es_ES';
update dictionary set traduccion='Presupuesto 2005' where id_vocablo=227653 and locale='ga_ES';
update dictionary set traduccion='Presupuesto 2005' where id_vocablo=227653 and locale='va_ES';
update dictionary set traduccion='Presupuesto 2005' where id_vocablo=227653 and locale='eu_ES';
update dictionary set traduccion='Presupuesto 2005' where id_vocablo=227653 and locale='ca_ES';
update attributes set id_alias =227653, id_layer=11213, id_column=43192, position=5, editable=true where id=103078;
update dictionary set traduccion='Presupuesto 2006' where id_vocablo=227654 and locale='es_ES';
update dictionary set traduccion='Presupuesto 2006' where id_vocablo=227654 and locale='ga_ES';
update dictionary set traduccion='Presupuesto 2006' where id_vocablo=227654 and locale='va_ES';
update dictionary set traduccion='Presupuesto 2006' where id_vocablo=227654 and locale='eu_ES';
update dictionary set traduccion='Presupuesto 2006' where id_vocablo=227654 and locale='ca_ES';
update attributes set id_alias =227654, id_layer=11213, id_column=43193, position=6, editable=true where id=103079;
update dictionary set traduccion='Presupuesto 2007' where id_vocablo=227655 and locale='es_ES';
update dictionary set traduccion='Presupuesto 2007' where id_vocablo=227655 and locale='ga_ES';
update dictionary set traduccion='Presupuesto 2007' where id_vocablo=227655 and locale='va_ES';
update dictionary set traduccion='Presupuesto 2007' where id_vocablo=227655 and locale='eu_ES';
update dictionary set traduccion='Presupuesto 2007' where id_vocablo=227655 and locale='ca_ES';
update attributes set id_alias =227655, id_layer=11213, id_column=43194, position=7, editable=true where id=103080;
update dictionary set traduccion='Presupuesto 2008' where id_vocablo=227656 and locale='es_ES';
update dictionary set traduccion='Presupuesto 2008' where id_vocablo=227656 and locale='ga_ES';
update dictionary set traduccion='Presupuesto 2008' where id_vocablo=227656 and locale='va_ES';
update dictionary set traduccion='Presupuesto 2008' where id_vocablo=227656 and locale='eu_ES';
update dictionary set traduccion='Presupuesto 2008' where id_vocablo=227656 and locale='ca_ES';
update attributes set id_alias =227656, id_layer=11213, id_column=43195, position=8, editable=true where id=103081;
update dictionary set traduccion='Presupuesto 2009' where id_vocablo=227657 and locale='es_ES';
update dictionary set traduccion='Presupuesto 2009' where id_vocablo=227657 and locale='ga_ES';
update dictionary set traduccion='Presupuesto 2009' where id_vocablo=227657 and locale='va_ES';
update dictionary set traduccion='Presupuesto 2009' where id_vocablo=227657 and locale='eu_ES';
update dictionary set traduccion='Presupuesto 2009' where id_vocablo=227657 and locale='ca_ES';
update attributes set id_alias =227657, id_layer=11213, id_column=43196, position=9, editable=true where id=103082;
update dictionary set traduccion='Inversión media anual' where id_vocablo=227658 and locale='es_ES';
update dictionary set traduccion='Inversión media anual' where id_vocablo=227658 and locale='ga_ES';
update dictionary set traduccion='Inversión media anual' where id_vocablo=227658 and locale='va_ES';
update dictionary set traduccion='Inversión media anual' where id_vocablo=227658 and locale='eu_ES';
update dictionary set traduccion='Inversión media anual' where id_vocablo=227658 and locale='ca_ES';
update attributes set id_alias =227658, id_layer=11213, id_column=43198, position=10, editable=true where id=103083;
update dictionary set traduccion='Inversión media por habitante' where id_vocablo=227659 and locale='es_ES';
update dictionary set traduccion='Inversión media por habitante' where id_vocablo=227659 and locale='ga_ES';
update dictionary set traduccion='Inversión media por habitante' where id_vocablo=227659 and locale='va_ES';
update dictionary set traduccion='Inversión media por habitante' where id_vocablo=227659 and locale='eu_ES';
update dictionary set traduccion='Inversión media por habitante' where id_vocablo=227659 and locale='ca_ES';
update attributes set id_alias =227659, id_layer=11213, id_column=43199, position=11, editable=true where id=103084;
update dictionary set traduccion='Inversión total' where id_vocablo=227660 and locale='es_ES';
update dictionary set traduccion='Inversión total' where id_vocablo=227660 and locale='ga_ES';
update dictionary set traduccion='Inversión total' where id_vocablo=227660 and locale='va_ES';
update dictionary set traduccion='Inversión total' where id_vocablo=227660 and locale='eu_ES';
update dictionary set traduccion='Inversión total' where id_vocablo=227660 and locale='ca_ES';
update attributes set id_alias =227660, id_layer=11213, id_column=43197, position=12, editable=true where id=103085;
update layers set name='inv_media_anual', acl='12' where id_layer=11213;
update queries set selectquery='SELECT transform("eiel_c_municipios"."GEOMETRY",23030) as "GEOMETRY", "eiel_c_municipios"."id" as id, "inv_media_anual"."id_municipio","inv_media_anual"."nomayto","inv_media_anual"."prto2005","inv_media_anual"."prto2006","inv_media_anual"."prto2007","inv_media_anual"."prto2008","inv_media_anual"."prto2009","inv_media_anual"."invmediaanual","inv_media_anual"."invhab","inv_media_anual"."totalinv" FROM "inv_media_anual" left join "eiel_c_municipios" on "eiel_c_municipios"."id_municipio"="inv_media_anual"."id_municipio"WHERE substr("inv_media_anual"."id_municipio",1,2)=substr(?M,1,2) and "inv_media_anual"."id_municipio" is not null', updatequery=null, insertquery=null, deletequery=null where id=10217;

-- Correcciones para Base de Datos Remota  (hay tablas donde el campo orden, lo denominan tramo) --
update query_catalog set query='select * from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=?' where id='EIELgetListaNucleosColector';
 update query_catalog set query='select * from eiel_tr_abast_tcn_pobl where codprov_tcn=? and codmunic_tcn=? and tramo_cn=?' where id='EIELgetListaNucleosTramosConduccion';
 update query_catalog set query='select * from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=?' where id='EIELgetListaNucleosEmisario';
 
-- Nuevos Formularios
ALTER TABLE layers ALTER COLUMN extended_form type varchar(200);

UPDATE layers SET extended_form='
com.geopista.app.eiel.forms.Depuradora1PanelExtendedForm,
com.geopista.app.eiel.forms.Depuradora2PanelExtendedForm' 
WHERE name='ED';


UPDATE layers SET extended_form='
com.geopista.app.eiel.forms.NucleosPoblacionPanelExtendedForm,
com.geopista.app.eiel.forms.ServiciosAbastecimientoPanelExtendedForm,
com.geopista.app.eiel.forms.ServiciosSaneamientoPanelExtendedForm,
com.geopista.app.eiel.forms.PropiedadesNucleosEncuestados1PanelExtendedForm,
com.geopista.app.eiel.forms.PropiedadesNucleosEncuestados2PanelExtendedForm,
com.geopista.app.eiel.forms.PadronNucleosPanelExtendedForm,
com.geopista.app.eiel.forms.ServiciosRecogidaBasuraPanelExtendedForm,
com.geopista.app.eiel.forms.NucleosAbandonadosPanelExtendedForm,
com.geopista.app.eiel.forms.PoblamientoPanelExtendedForm' 
WHERE name='NP';


UPDATE layers SET extended_form='
com.geopista.app.eiel.forms.PlaneamientoUrbanoPanelExtendedForm,
com.geopista.app.eiel.forms.DiseminadosPanelExtendedForm,
com.geopista.app.eiel.forms.OtrosServMunicipalesPanelExtendedForm,
com.geopista.app.eiel.forms.PadronMunicipiosPanelExtendedForm' 
WHERE name='TTMM';








--UPDATE layers SET extended_form='com.geopista.app.eiel.forms.Depuradora2PanelExtendedForm' WHERE name='ED';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.CaptacionesExtendedForm,com.geopista.app.eiel.forms.CaptacionesPanelExtendedForm' WHERE name='CA'
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.DepositosPanelExtendedForm' WHERE name='DE';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.ServiciosAbastecimientoPanelExtendedForm' WHERE name='NP';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.TratamientosPotabilizacionPanelExtendedForm' WHERE name='TP';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.CentrosAsistencialesPanelExtendedForm' WHERE name='AS';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.CasasConsistorialesPanelExtendedForm' WHERE name='CC';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.CementeriosPanelExtendedForm' WHERE name='CE';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.CentrosCulturalesPanelExtendedForm' WHERE name='CU';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.CentrosEnsenianzaPanelExtendedForm' WHERE name='EN';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.InstalacionesDeportivasPanelExtendedForm' WHERE name='ID';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.CentrosExtincionIncendiosPanelExtendedForm' WHERE name='IP';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.LonjasMercadosPanelExtendedForm' WHERE name='LM';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.MataderosPanelExtendedForm' WHERE name='MT';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.SaneamientoAutonomoPanelExtendedForm' WHERE name='SN';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.AbastecimientoAutonomoPanelExtendedForm' WHERE name='AB';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.ParquesJardinesPanelExtendedForm' WHERE name='PJ';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.CentrosSanitariosPanelExtendedForm' WHERE name='SA';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.PuntosVertidoPanelExtendedForm' WHERE name='PV';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.EdificiosSinUsoPanelExtendedForm' WHERE name='SU';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.TanatoriosPanelExtendedForm' WHERE name='TN';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.VertederosPanelExtendedForm' WHERE name='VT';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.RecogidaBasurasPanelExtendedForm' WHERE name='RB';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.CabildoPanelExtendedForm' WHERE name='Provincia';

insert into query_catalog (id, query,acl,idperm) values ('EIELgetPanelTipoInstalacionesDeportivas','select * from eiel_t_id_deportes where eiel_t_id_deportes.clave=? and eiel_t_id_deportes.codprov=? and eiel_t_id_deportes.codmunic=? and eiel_t_id_deportes.codentidad=? and eiel_t_id_deportes.codpoblamiento=? and eiel_t_id_deportes.orden_id=?',1,9205);
insert into query_catalog (id,query,acl,idperm) values ('EIELgetPanelUsosCentrosCulturales','select * from eiel_t_cu_usos where eiel_t_cu_usos.clave=? and eiel_t_cu_usos.codprov=? and eiel_t_cu_usos.codmunic=? and eiel_t_cu_usos.codentidad=? and eiel_t_cu_usos.codpoblamiento=? and eiel_t_cu_usos.orden_cu=?',1,9205);
insert into query_catalog (id,query,acl,idperm) values ('EIELgetPanelUsosCasasConsistoriales','select * from eiel_t_cc_usos where eiel_t_cc_usos.clave=? and eiel_t_cc_usos.codprov=? and eiel_t_cc_usos.codmunic=? and eiel_t_cc_usos.codentidad=? and eiel_t_cc_usos.codpoblamiento=? and eiel_t_cc_usos.orden_cc=?',1,9205);
insert into query_catalog (id,query,acl,idperm) values ('EIELgetPanelNivelCentrosEnsenianza','select * from eiel_t_en_nivel where eiel_t_en_nivel.clase=? and eiel_t_en_nivel.codprov=? and eiel_t_en_nivel.codmunic=? and eiel_t_en_nivel.codentidad=? and eiel_t_en_nivel.codpoblamiento=? and eiel_t_en_nivel.orden_en=?',1,9205);