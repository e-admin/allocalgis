Update query_catalog set query='select nextval(''seq_entidad_generadora'')' where id='MCnextSecuenciaEntidadGeneradora';

Update query_catalog set query='insert into queries (id, id_layer, databasetype, selectquery, updatequery, insertquery, deletequery) values (nextval(''seq_queries''),?,?,?,?,?,?)' where id='LMinsertarQueriesSecuencial';

Update query_catalog set query='select nextval(''"seq_unidad_constructiva"'')' where id='catfinurbmaxunidadconstructiva';

ALTER TABLE seq_entidadgeneradora RENAME TO seq_entidad_generadora;

ALTER TABLE seq_querys RENAME TO seq_queries;

ALTER TABLE seq_unidades_constructivas  RENAME TO seq_unidad_constructiva;

ALTER TABLE guiaurbana.coveragelayer ALTER id_municipio TYPE numeric(5,0);

ALTER TABLE civil_work_warnings ALTER id_municipio TYPE numeric(5,0);

ALTER TABLE history ALTER municipio TYPE numeric(5,0);

ALTER TABLE visualizador.coveragelayer ALTER id_municipio TYPE numeric(5,0);

ALTER TABLE map_server_layers ADD COLUMN "name" character varying(200);

ALTER TABLE seq_compannias_seguros RENAME TO seq_compannia_seguros;