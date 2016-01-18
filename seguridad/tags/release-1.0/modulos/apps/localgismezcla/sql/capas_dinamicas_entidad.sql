CREATE TABLE dynamiclayers
(
  id int4 NOT NULL,
  url varchar(400),
  id_entidad numeric(8),
  CONSTRAINT dynamiclayers_pkey PRIMARY KEY (id, id_entidad)
); 
insert into dynamiclayers(id,url,id_entidad) 
select l.id_layer,l.url,e.id_entidad from layers l, entidad_supramunicipal e where l.dinamica=1;

alter table layers drop column dinamica;
alter table layers drop column url;

insert into query_catalog (id,query,acl,idperm) values('isDynamicLayer','select * from dynamiclayers where id=? and id_entidad=?',1,9205);
update query_catalog set query ='insert into layers (id_layer, id_name, name, id_styles, acl, modificable,id_entidad,versionable,"validator") values (nextval(''seq_layers''),?,?,?,?,1,?,?,?)' where id='LMinsertarLayersSecuencial';
update query_catalog set query ='update layers set name=?, acl=?, id_entidad=?,versionable=?,"validator"=? where id_layer=?' where id ='LMactualizarLayers';
insert into query_catalog (id,query,acl,idperm) values('LMinsertarDynamicLayers','insert into dynamiclayers(id,url,id_entidad) values(?,?,?)',1,9205);
insert into query_catalog (id,query,acl,idperm) values('LMborrarDynamicLayers','delete from dynamiclayers where id=? and id_entidad=?',1,9205);

