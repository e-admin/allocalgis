--Variable Temporal

create table versiones (
	revision numeric(10) not null,
	id_autor numeric(10) not null,
	id_table_versionada numeric(7) not null,
	fecha timestamp,
	geom geometry,
	mensaje varchar(100),
	oficial  int4 DEFAULT 0,
	CONSTRAINT versiones_pkey PRIMARY KEY (revision,id_table_versionada),
	CONSTRAINT id_autor_fkey FOREIGN KEY (id_autor) REFERENCES iuseruserhdr (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "$2" CHECK (geometrytype(geom) = 'POLYGON'::text OR geom IS NULL)	
);


alter table layers add column versionable int4 DEFAULT 0;
alter table layers_styles add column revision numeric(10);

CREATE SEQUENCE revisionesequence
    	INCREMENT BY 1    
	NO MAXVALUE   
	NO MINVALUE    
	CACHE 1;
update query_catalog set query='select * from layers where name=?' where id ='LMexisteLayerId';
update query_catalog set query='update layers set name=?, acl=?, id_entidad=?,dinamica=?,url=?,versionable=?,"validator"=? where id_layer=?' where id='LMactualizarLayers';
update query_catalog set query='insert into layers (id_layer, id_name, name, id_styles, acl, modificable,id_entidad,dinamica,url,versionable,"validator") values (nextval(''seq_layers''),?,?,?,?,1,?,?,?,?,?)' where id= 'LMinsertarLayersSecuencial';
ALTER TABLE inmuebles ALTER ref_catastral TYPE character varying(20);
insert into query_catalog (id,query,acl,idperm) values('deleteRevisionesTabla','delete from versiones where id_table_versionada in (select id_table from tables where name=?)','1','9205');
insert into query_catalog (id,query,acl,idperm) values('getUltimaRevision','select coalesce(max(r.revision),0) from versiones r where r.id_table_versionada=(select id_table from tables where name=?)','1','9205');
insert into query_catalog (id,query,acl,idperm) values('insertaVersionInicial','insert into versiones (revision,id_autor,fecha,id_table_versionada) values (?,(select id from iuseruserhdr where upper(name) = upper(?)),?,(select id_table from tables where name=?))','1','9205');

insert into version (id_version, fecha_version) values('1.3', DATE '2009-12-01');
