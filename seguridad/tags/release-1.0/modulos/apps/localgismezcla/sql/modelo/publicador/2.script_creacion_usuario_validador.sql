SET @USER1 = 'EIEL001_VAL';
SET @IDMUNICIPIO = 33001;

-- Quitamos al usuario el rol que tenga
delete from iusergroupuser where userid=(select id from iuseruserhdr where name ilike '%@USER1%');

delete from iusercnt where userid=(select id from iuseruserhdr where name ilike '%@USER1%');

-- Borramos al usuario
delete from iuseruserhdr where name ilike '%@USER1%';



SET @IDENTIDAD= select id_entidad from (select id_entidad,(select count(*) from entidades_municipios where entidades_municipios.id_entidad=innerquery.id_entidad) as total  from (select entidad_supramunicipal.id_entidad::integer from entidad_supramunicipal left join entidades_municipios on entidad_supramunicipal.id_entidad=entidades_municipios.id_entidad where id_municipio=@IDMUNICIPIO) as innerquery ) as outerquery where total=1;
PRINT @IDENTIDAD;

-- Creamos el usuario
insert into iuseruserhdr(id,name,nombrecompleto,password,flags,stat,numbadcnts,remarks,crtrid,updrid,mail,deptid,borrado,id_entidad,upddate, crtndate,nif ,fecha_proxima_modificacion, bloqueado,intentos_reiterados) 
values (nextval('seq_iuseruserhdr'),'@USER1','@USER1','6qLrl3aAfohvTmHGh4zl5g==',0,0,0,'Este usuario solo puede trabajar con EIEL',nextval('seq_r_user_perm'),currval('seq_r_user_perm'),'',0,0,@IDENTIDAD,'2011-07-18 00:00:00','2011-07-18 00:00:00','','2012-07-18','0',0);

