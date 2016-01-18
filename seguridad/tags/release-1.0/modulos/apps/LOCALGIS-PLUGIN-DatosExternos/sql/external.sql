ALTER TABLE tables ADD COLUMN external numeric(1) DEFAULT 0;
ALTER TABLE columns ALTER "Length" TYPE numeric(5,0);
ALTER TABLE datasource ADD COLUMN id_municipio numeric(5) DEFAULT 0;
ALTER TABLE tables ADD CONSTRAINT tables_pkey PRIMARY KEY (id_table);

CREATE TABLE datasource_tables(
  id numeric(8) NOT NULL,
  id_table numeric(7) NOT NULL,
  id_datasource numeric(3) NOT NULL,
  fetchQuery text NOT NULL,
  CONSTRAINT datasource_tables_pkey PRIMARY KEY (id),
  CONSTRAINT "$1" FOREIGN KEY (id_table)
      REFERENCES tables (id_table) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "$2" FOREIGN KEY (id_datasource)
      REFERENCES datasource (id_datasource) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION

)
WITH OIDS;
ALTER TABLE viasine OWNER TO geopista;

CREATE SEQUENCE seq_datasource_tables
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE seq_datasource OWNER TO geopista;


INSERT INTO query_catalog(id,query) VALUES('LMobtenerDataSourceTables', 'SELECT ds_t.id, ds_t.id_table, ds_t.id_datasource, ds_t.fetchquery FROM datasource_tables AS ds_t INNER JOIN datasource AS ds ON ds_t.id_datasource = ds.id_datasource and ds.id_municipio=?');
INSERT INTO query_catalog(id,query) VALUES('LMobtenerDataSourceTablesNombre', 'SELECT ds_t.id, ds_t.id_table, ds_t.id_datasource, ds_t.fetchquery FROM datasource_tables AS ds_t INNER JOIN datasource AS ds ON ds_t.id_datasource = ds.id_datasource and ds.id_municipio=? INNER JOIN tables AS t ON ds_t.id_table=t.id_table and t.name=?');
INSERT INTO query_catalog(id,query) VALUES('LMobtenerDataSource', 'SELECT nombre, driver, cadena_conexion, username, password FROM datasource where id_datasource=?');
INSERT INTO query_catalog(id,query) VALUES('LMeliminarDataSourceTables', 'delete from datasource_tables where id=?');
INSERT INTO query_catalog(id,query) VALUES('LMobtenerColumnasNotNullTabla', 'SELECT attname FROM pg_class, pg_attribute where pg_attribute.attrelid=pg_class.oid and attnotnull and attstattarget=-1 and relname=?');
INSERT INTO query_catalog(id,query) VALUES('DBElistCapasExtendidas', 'SELECT distinct(l.id_layer), d.traduccion FROM layers l, attributes a, columns c, tables t, dictionary d WHERE l.id_layer = a.id_layer AND a.id_column = c.id AND c.id_table = t.id_table AND d.id_vocablo = l.id_name AND t.external = 1 AND d.locale = ?');
INSERT INTO query_catalog(id,query) VALUES('DBEgetCapaExtendida', 'SELECT distinct(t.id_table), t.name, dt.fetchquery, d.* FROM layers l, attributes a, columns c, tables t, datasource_tables dt, datasource d WHERE l.id_layer = a.id_layer AND a.id_column = c.id AND c.id_table = t.id_table AND t.id_table = dt.id_table AND dt.id_datasource = d.id_datasource AND t.external = 1 AND l.id_layer = ?');
INSERT INTO query_catalog(id,query) VALUES('LMobtenertablasBDNoExternas', 'select pgt.tablename, t.* from pg_tables pgt, tables t where pgt.schemaname=''public'' and pgt.tablename=t.name and t.external=0');
INSERT INTO query_catalog(id,query) VALUES('LMexisteTabla', 'SELECT * FROM information_schema.tables WHERE table_schema = ''public'' AND table_type = ''BASE TABLE'' AND table_name = ?');
INSERT INTO query_catalog(id,query) VALUES('LMinsertarDataSourceTables', 'insert into datasource_tables (id, id_table, id_datasource, fetchquery) values(nextval(''seq_datasource_tables''), ?, ?, ?)')

UPDATE query_catalog SET query='INSERT INTO tables (id_table, name, geometrytype, external) VALUES(nextval(''seq_tables''),?,?,?)' WHERE id='LMcrearTablaSistema';

UPDATE query_catalog SET query = 'SELECT * FROM datasource WHERE id_municipio=?' WHERE id='listQueryExternalDataSource';
UPDATE query_catalog SET query = 'INSERT INTO datasource VALUES (?,?,?,?,?,?,?)' WHERE id='insertQueryExternalDataSource';
UPDATE query_catalog SET query = 'SELECT * FROM datasource WHERE nombre =? and id_municipio=?' WHERE id='findQueryExternalDataSource';
UPDATE query_catalog SET query = 'UPDATE datasource SET nombre=?, driver=?, cadena_conexion=?, username=?, password=?, id_municipio=? WHERE id_datasource=?' WHERE id='updateQueryExternalDataSource';
UPDATE query_catalog SET query = 'SELECT qe.* FROM datasource ds, queries_externos qe WHERE ds.nombre = ? AND ds.id_datasource = qe.id_datasource AND ds.id_municipio = ?' WHERE id='listQueriesByDataSourceNameQuery';

UPDATE query_catalog SET query = 'SELECT qe.* FROM datasource ds, queries_externos qe WHERE qe.nombre = ? AND ds.nombre = ? AND ds.id_datasource = qe.id_datasource AND ds.id_municipio=?' WHERE id='findQueryQuery';
UPDATE query_catalog SET query = 'SELECT ds.nombre, qe.nombre FROM queries_externos AS qe, datasource AS ds WHERE qe.id_datasource = ds.id_datasource AND qe.campo_interno_capa = ? AND ds.id_municipio=? ORDER BY ds.nombre' WHERE id='selectLayerQuery';

-- CORRECCION INCIDENCIA 03-03-2010 ---- 
update query_catalog set query = 'select pgt.tablename, t.* from pg_tables pgt, tables t where pgt.schemaname=''public'' and pgt.tablename=t.name order by tablename' where id = 'LMobtenertablasBD';