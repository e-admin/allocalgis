CREATE SEQUENCE seq_acl_perm;
CREATE SEQUENCE public.seq_acl INCREMENT 1 MINVALUE 100 START 100 CACHE 1;
ALTER TABLE seq_acl_perm OWNER TO geopista;
select setval('seq_acl_perm',cast(max(usrgrouperm.idperm) as bigint),'t');

CREATE TABLE entidad_supramunicipal
(
  id_entidad numeric(8) NOT NULL,
  nombreoficial varchar(50) NOT NULL,
  CONSTRAINT entidad_supramunicipal_pkey PRIMARY KEY (id_entidad)
) 
WITH OIDS;
ALTER TABLE entidad_supramunicipal OWNER TO geopista;

insert into sequences (id_sequence, tablename, field, value, incrementvalue, minimumvalue, maximumvalue, circular) values 
((SELECT max(id_sequence)+1 FROM SEQUENCES), 'ENTIDAD_SUPRAMUNICIPAL', 'ID_ENTIDAD', 0, 1, 1, 999999999, 'T');

CREATE TABLE entidades_municipios
(
  id_entidad numeric(8) NOT NULL,
  id_municipio numeric(5) NOT NULL,
  CONSTRAINT entidades_municipios_pkey PRIMARY KEY (id_entidad, id_municipio)
) 
WITH OIDS;
ALTER TABLE entidades_municipios OWNER TO geopista;


insert into query_catalog (id,query,acl,idperm) values
('getEntidad','select * from entidad_supramunicipal where id_entidad=?','1','9205');
insert into query_catalog (id,query,acl,idperm) values
('getMunicipiosEntidad','select m.id,m.nombreoficial,astext("GEOMETRY") as geom,m.srid from municipios m, entidades_municipios e where m.id = e.id_municipio and e.id_entidad=?','1','9205');



alter table maps rename column id_municipio to id_entidad;
alter table maps alter column id_entidad  type numeric(8);
alter table iuseruserhdr rename column id_municipio to id_entidad;
alter table iuseruserhdr alter column id_entidad  type numeric(8);
alter table iusergrouphdr rename column id_municipio to id_entidad;
alter table iusergrouphdr alter column id_entidad  type numeric(8);
alter table maps_layerfamilies_relations rename column id_municipio to id_entidad;
alter table maps_layerfamilies_relations alter column id_entidad  type numeric(8);
alter table layers_styles rename column id_municipio to id_entidad;
alter table layers_styles alter column id_entidad  type numeric(8);
alter table maps_wms_relations rename column id_municipio to id_entidad;
alter table maps_wms_relations alter column id_entidad  type numeric(8);
alter table layers add column id_entidad  numeric(8);


update query_catalog set query='select iusergrouphdr.id as id, iusergrouphdr.name as name, iusergrouphdr.remarks as remarks, r_group_perm.idperm as idperm, r_group_perm.idacl as idacl from iusergrouphdr,r_group_perm where iusergrouphdr.id_entidad=? OR iusergrouphdr.id_entidad=0 and iusergrouphdr.id=r_group_perm.groupid order by iusergrouphdr.id_entidad,iusergrouphdr.id' where id='allrolespermisos';
update query_catalog set query='Select id, name, nombrecompleto, password, remarks,mail, deptid, nif, id_entidad FROM IUSERUSERHDR where borrado!=1 and (id_entidad=? or id_entidad is null)' where id='allusuarios';
update query_catalog set query='select iusergrouphdr.id as id, iusergrouphdr.name as name,  iusergrouphdr.remarks as remarks from iusergrouphdr where id_entidad=? OR id_entidad=0' where id='allroles';

insert into query_catalog (id,query,acl,idperm) values
('LMobtenerPermisoLayer','select l.modificable,r.aplica from layers l,r_usr_perm r, usrgrouperm g, iuseruserhdr  i 
where l.name=? and l.acl=r.idacl and r.idperm=g.idperm and g.def=?
and r.userid=i.id and upper(i.name)=upper(?)','1','9205');

update query_catalog set query='insert into layers (id_layer, id_name, name, id_styles, acl, modificable,id_entidad) values (nextval(''seq_layers''),?,?,?,?,1,?)' where id='LMinsertarLayersSecuencial';
update query_catalog set query='update layers set name=?, acl=?, id_entidad=? where id_layer=?' where id='LMactualizarLayers';

update layers set id_entidad=0 where id_entidad is null;

create table srid_properties(
id_municipio numeric(5),
srid numeric(5),
  CONSTRAINT srid_properties_pkey PRIMARY KEY (id_municipio,srid)
) 
WITH OIDS;
ALTER TABLE tramosvia OWNER TO geopista;

insert into query_catalog (id,query,acl,idperm) values 
('getEntidades','select * from entidades_supramunicipales','1','9205');

insert into acl(idacl,name) values (nextval('seq_acl'),'Gestor Capas');
insert into usrgrouperm(idperm,def) values (nextval('seq_acl_perm'),'Geopista.Modificar.CapasGlobales');
insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));
insert into usrgrouperm(idperm,def) values (nextval('seq_acl_perm'),'Geopista.Modificar.CapasLocales');
insert into r_acl_perm(idperm,idacl) values (currval('seq_acl_perm'),currval('seq_acl'));

insert into query_catalog (id,query,acl,idperm) values
('LMobtenerlayersentidad','select layers.*,dictionary.locale, dictionary.traduccion,acl.idacl, acl.name as aclname from acl left join (layers left join dictionary on layers.id_name = dictionary.id_vocablo) on acl.idacl=layers.acl where (layers.modificable=1 or layers.modificable=?) and (layers.id_entidad=0 or layers.id_entidad=?) order by layers.id_layer','1','9205');

insert into query_catalog (id, query, acl, idperm) values ('getEntidadesSortedById', 'select * from entidad_supramunicipal order by id_entidad', '1', '9205');
insert into query_catalog (id, query, acl, idperm) values ('getEntidadesSortedByName', 'select * from entidad_supramunicipal order by nombreoficial', '1', '9205');
insert into query_catalog (id, query, acl, idperm) values ('getMunicipiosSRID', 'select id_municipio, srid from srid', '1', '9205');
insert into query_catalog (id, query, acl, idperm) values ('getMunicipiosPorEntidad', 'select municipios.id as id_municipio, provincias.id as id_provincia, municipios.nombreoficial as nombre_municipio, provincias.nombreoficial as nombre_provincia from entidades_municipios, municipios, provincias where entidades_municipios.id_municipio = municipios.id and municipios.id_provincia = provincias.id and id_entidad = ?', '1', '9205');
insert into query_catalog (id, query, acl, idperm) values ('getMunicipiosAsociadosEntidades', 'select distinct municipios.id as id_municipio, provincias.id as id_provincia, municipios.nombreoficial as nombre_municipio, provincias.nombreoficial as nombre_provincia from entidades_municipios, municipios, provincias where entidades_municipios.id_municipio = municipios.id and municipios.id_provincia = provincias.id order by id_municipio', '1', '9205');

alter table municipios add column srid_proyeccion numeric(5);

create table version (
	id_version varchar(10) NOT NULL,
	fecha_version date NOT NULL,
	CONSTRAINT version_pkey PRIMARY KEY (id_version)
) WITH OIDS;


update layers_styles set position = 1 where id_map = 3 and id_layerfamily = 13 and id_layer = 101;
update layers_styles set position = 2 where id_map = 3 and id_layerfamily = 12 and id_layer = 12;




insert into query_catalog (id, query, acl, idperm) values ('LMobtenerlayertablesEntidad', 'select layers.*,dictionary.locale, dictionary.traduccion,acl.idacl, acl.name as aclname from acl left join (layers left join dictionary on layers.id_name = dictionary.id_vocablo) on acl.idacl=layers.acl where (layers.modificable=1 or layers.modificable=?) and (layers.id_entidad=0 or layers.id_entidad=?) order by layers.id_layer', '1', '9205');
