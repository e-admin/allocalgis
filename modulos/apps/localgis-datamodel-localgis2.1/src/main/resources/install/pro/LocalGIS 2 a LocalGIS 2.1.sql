SET client_min_messages = warning;
--SIN TERMINAR COMPROBACIONES!!!!
SET client_min_messages TO WARNING;
-- Start Transaction block


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'session_app') THEN	
		ALTER TABLE "public"."session_app" ALTER COLUMN "id" TYPE varchar(40);
	END IF;
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'iusercnt') THEN	
		ALTER TABLE "public"."iusercnt" ALTER COLUMN "id" TYPE varchar(40);
	END IF;
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO0";





DROP FUNCTION IF EXISTS localgisDatamodelInstall();

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'entidad_supramunicipal') THEN	
		ALTER TABLE entidad_supramunicipal ADD COLUMN backup numeric(1,0) DEFAULT 1;
	END IF;
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";
-- Fin  add_backup_entidad.sql

-- Inicio capas_dinamicas_entidad.sql
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'dynamiclayers') THEN	
		CREATE TABLE dynamiclayers
		(
		  id int4 NOT NULL,
		  url varchar(400),
		  id_entidad numeric(8),
		  CONSTRAINT dynamiclayers_pkey PRIMARY KEY (id, id_entidad)
		); 
		IF NOT EXISTS (SELECT table_name FROM dynamiclayers WHERE table_schema = 'public' AND table_name = 'layers') THEN	
			IF NOT EXISTS (SELECT table_name FROM dynamiclayers WHERE table_schema = 'public' AND table_name = 'entidad_supramunicipal') THEN
				insert into dynamiclayers(id,url,id_entidad) select l.id_layer,l.url,e.id_entidad from layers l, entidad_supramunicipal e where l.dinamica=1;
			END IF;
		END IF;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO2";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'layers' AND column_name = 'dinamica') THEN	
		alter table layers drop column dinamica;
	END IF;	
	IF EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'layers' AND column_name = 'url') THEN	
		alter table layers drop column url;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO3";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'isDynamicLayer') THEN
		insert into query_catalog (id,query,acl,idperm) values('isDynamicLayer','select * from dynamiclayers where id=? and id_entidad=?',1,9205);
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO4";	
	
update query_catalog set query ='insert into layers (id_layer, id_name, name, id_styles, acl, modificable,id_entidad,versionable,"validator") values (nextval(''seq_layers''),?,?,?,?,1,?,?,?)' where id='LMinsertarLayersSecuencial';
update query_catalog set query ='update layers set name=?, acl=?, id_entidad=?,versionable=?,"validator"=? where id_layer=?' where id ='LMactualizarLayers';

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'LMinsertarDynamicLayers') THEN
		insert into query_catalog (id,query,acl,idperm) values('LMinsertarDynamicLayers','insert into dynamiclayers(id,url,id_entidad) values(?,?,?)',1,9205);
	END IF;
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'LMborrarDynamicLayers') THEN
		insert into query_catalog (id,query,acl,idperm) values('LMborrarDynamicLayers','delete from dynamiclayers where id=? and id_entidad=?',1,9205);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO5";
-- Fin  capas_dinamicas_entidad.sql
	
-- Inicio 01-09-2010.sql
update query_catalog set query='select iusergrouphdr.id as id, iusergrouphdr.name as name, iusergrouphdr.remarks as remarks, r_group_perm.idperm as idperm, r_group_perm.idacl as idacl from iusergrouphdr,r_group_perm where (iusergrouphdr.id_entidad=? OR iusergrouphdr.id_entidad=0) and iusergrouphdr.id=r_group_perm.groupid order by iusergrouphdr.id_entidad,iusergrouphdr.id' where id='allrolespermisos';
-- Fin  01-09-2010.sql		
		
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	-- Inicio 27-10-2010.sql
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'getListaUsuarios') THEN
		INSERT INTO query_catalog(id, query, acl, idperm) VALUES('getListaUsuarios','SELECT id, name, nombrecompleto, password, remarks,mail, deptid, nif, id_entidad FROM iuseruserhdr WHERE borrado!=1',1,9205);
	END IF;
	-- Fin 27-10-2010.sql
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO6";


-- Inicio actualizar_capa_Parcelas.sql
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'parcelas' AND column_name = 'ninterno') THEN
		ALTER TABLE parcelas ADD COLUMN ninterno numeric(7,0);
	END IF;	
	IF NOT EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'parcelas' AND column_name = 'tipo') THEN
		ALTER TABLE parcelas ADD COLUMN tipo character varying(1);
	END IF;
	IF NOT EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'parcelas' AND column_name = 'numerodup') THEN
		ALTER TABLE parcelas ADD COLUMN numerodup character varying(1);
	END IF;
	IF NOT EXISTS (SELECT column_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'parcelas' AND column_name = 'numsymbol') THEN
		ALTER TABLE parcelas ADD COLUMN numsymbol numeric(1,0);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO7";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT id FROM columns WHERE id_table = 100 AND name = 'ninterno') THEN
		INSERT INTO columns (id, name, id_domain, id_table, "Length", "Precision", "Scale", "Type") VALUES (nextval('seq_columns'), 'ninterno', null, 100, null, 7, 0, 2);
	END IF;
	IF NOT EXISTS (SELECT id_vocablo FROM dictionary WHERE traduccion = 'ninterno' AND locale = 'es_ES') THEN
		insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','ninterno');
	--END IF;
	
	--IF NOT EXISTS (SELECT id FROM attributes WHERE id_layer = 101 AND id_column = currval('seq_columns')) THEN
		insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),101,currval('seq_columns'),23,TRUE);
	END IF;
		
	IF NOT EXISTS (SELECT id FROM columns WHERE id_table = 100 AND name = 'tipo') THEN
		INSERT INTO columns (id, name, id_domain, id_table, "Length", "Precision", "Scale", "Type") VALUES (nextval('seq_columns'), 'tipo', null, 100, 1, null, null, 3);
	END IF;
	IF NOT EXISTS (SELECT id_vocablo FROM dictionary WHERE traduccion = 'tipo' AND locale = 'es_ES') THEN
		insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','tipo');
	--END IF;	
	--IF NOT EXISTS (SELECT id FROM attributes WHERE id_layer = 101 AND id_column = currval('seq_columns')) THEN
		insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),101,currval('seq_columns'),24,TRUE);
	END IF;
	
	IF NOT EXISTS (SELECT id FROM columns WHERE id_table = 100 AND name = 'numerodup') THEN
		INSERT INTO columns (id, name, id_domain, id_table, "Length", "Precision", "Scale", "Type") VALUES (nextval('seq_columns'), 'numerodup', null, 100, 1, null, null, 3);
	END IF;	
	IF NOT EXISTS (SELECT id_vocablo FROM dictionary WHERE traduccion = 'numerodup' AND locale = 'es_ES') THEN
		insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','numerodup');
	--END IF;	
	--IF NOT EXISTS (SELECT id FROM attributes WHERE id_layer = 101 AND id_column = currval('seq_columns')) THEN
		insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),101,currval('seq_columns'),25,TRUE);
	END IF;
		
	IF NOT EXISTS (SELECT id FROM columns WHERE id_table = 100 AND name = 'numsymbol') THEN
		INSERT INTO columns (id, name, id_domain, id_table, "Length", "Precision", "Scale", "Type") VALUES (nextval('seq_columns'), 'numsymbol', null, 100, null, 1, 0, 2);
	END IF;
	IF NOT EXISTS (SELECT id_vocablo FROM dictionary WHERE traduccion = 'numsymbol' AND locale = 'es_ES') THEN
		insert into dictionary (id_vocablo,locale, traduccion) values (nextval('seq_dictionary'),'es_ES','numsymbol');
	--END IF;
	--IF NOT EXISTS (SELECT id FROM attributes WHERE id_layer = 101 AND id_column = currval('seq_columns')) THEN
		insert into attributes (id, id_alias, id_layer, id_column, position, editable) values (nextval('seq_attributes'),currval('seq_dictionary'),101,currval('seq_columns'),26,TRUE);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO8";

UPDATE queries SET selectquery='SELECT Parcelas.id, Parcelas.referencia_catastral, Parcelas.id_municipio, Parcelas.primer_numero, Parcelas.primera_letra, Parcelas.segundo_numero, Parcelas.segunda_letra, Parcelas.kilometro, Parcelas.bloque, Parcelas.direccion_no_estructurada, Parcelas.codigo_postal, Parcelas.fecha_alta, Parcelas.fecha_baja, transform(Parcelas."GEOMETRY", ?T) as "GEOMETRY", Parcelas.codigodelegacionmeh, Parcelas.nombreentidadmenor, Parcelas.id_distrito, Parcelas.codigozonaconcentracion, Parcelas.codigopoligono, Parcelas.codigoparcela, Parcelas.nombreparaje, Parcelas.id_via, Parcelas.area, Parcelas.length, Parcelas.anno_expediente, Parcelas.referencia_expediente, Parcelas.codigo_entidad_colaboradora, Parcelas.tipo_movimiento, Parcelas.codigo_municipiodgc, Parcelas.bice, Parcelas.codigo_provinciaine, Parcelas.codigo_municipio_localizaciondgc, Parcelas.codigo_municipio_localizacionine, Parcelas.codigo_municipio_origendgc, Parcelas.codigo_paraje, Parcelas.superficie_finca, Parcelas.superficie_construida_total, Parcelas.superficie_const_sobrerasante, Parcelas.superficie_const_bajorasante, Parcelas.superficie_cubierta, Parcelas.anio_aprobacion, Parcelas.codigo_calculo_valor, Parcelas.poligono_catastral_valoracion, Parcelas.modalidad_reparto, Parcelas.indicador_infraedificabilidad, Parcelas.movimiento_baja, Parcelas.coordenada_x, Parcelas.coordenada_y, Parcelas.modificado, Municipios.NombreOficial, Parcelas.ninterno, Parcelas.tipo, Parcelas.numerodup, Parcelas.numsymbol FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) WHERE Municipios.ID in (?M) AND Fecha_baja IS NULL', updatequery='UPDATE parcelas SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),id=?2,referencia_catastral=?3,id_municipio=?M,id_distrito=?5,codigoparcela=?6,codigopoligono=?7,id_via=?8,primer_numero=?9,primera_letra=?10,segundo_numero=?11,segunda_letra=?12,kilometro=?13,bloque=?14,direccion_no_estructurada=?15,codigo_postal=?16,codigodelegacionmeh=?17,length=perimeter(GeometryFromText(text(?1),?S)),Area=area2d(GeometryFromText(text(?1),?S)), fecha_alta=?20,fecha_baja=?21,modificado=?22,ninterno=?23, tipo=?24, numerodup=?25, numsymbol=?26 WHERE ID=?2', insertquery='INSERT INTO parcelas ("GEOMETRY",id,referencia_catastral,id_municipio,id_distrito,codigoparcela,codigopoligono,id_via,primer_numero,primera_letra,segundo_numero,segunda_letra,kilometro,bloque,direccion_no_estructurada,codigo_postal,codigodelegacionmeh,length,area,fecha_alta,fecha_baja,modificado,ninterno, tipo, numerodup, numsymbol) VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?M,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,?15,?16,?17,perimeter(GeometryFromText(text(?1),?S)),area2d(GeometryFromText(text(?1),?S)),?20,?21,?22,?23,?24,?25,?26)' WHERE id_layer = 101;

-- Fin actualizar_capa_Parcelas.sql

-- Inicio 05-01-2011.sql
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'getAllDynamicLayersEntidad') THEN
		insert into query_catalog (id,query,acl,idperm) 
		values('getAllDynamicLayersEntidad','select * from dynamiclayers where id_entidad=?',1,9205);
	END IF;
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'LMbuscarAliasAttributes') THEN
		insert into query_catalog (id,query,acl,idperm) 
		values('LMbuscarAliasAttributes','SELECT * from attributes where id_alias = ?',1,9205);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO9";
-- Fin 05-01-2011.sql

-- Inicio 16-03-2011.sql

update columns set name ='fechaalta' where name='FechaAlta';
update columns set name ='fechabaja' where name='FechaBaja';
update columns set name ='fechainstalacion' where name='FechaInstalacion';
update columns set name ='codigodelegacionmeh' where name='codigodelegacionMEH';
update columns set name ='codigoparcela' where name='CodigoParcela';
update columns set name ='codigozonaconcentracion' where name='codigoZonaConcentracion';
update columns set name ='nombreentidadmenor' where name='nombreEntidadMenor';
update columns set name ='nombreparaje' where name='nombreParaje';



update query_catalog set query = 'select distinct id_habitante, nombre, part_apell1, apellido1, part_apell2, apellido2, dni, letradni  from habitantes h, domicilio d, numeros_policia p where h.id_domicilio = d.id_domicilio and d.id_portal = p.id and p.id=? order by dni,letradni, apellido1, part_apell1, apellido2, part_apell2, nombre' where id ='padronHabitantesPoliciaOrdendni';

update query_catalog set query = 'select distinct id_habitante, nombre, part_apell1, apellido1, part_apell2, apellido2, dni, letradni  from habitantes h, domicilio d, numeros_policia p where h.id_domicilio = d.id_domicilio and d.id_portal = p.id and p.id=? order by apellido1, part_apell1, apellido2, part_apell2, nombre, dni,letradni' where id ='padronHabitantesPoliciaOrdenapellidos';


update layers set extended_form ='com.geopista.app.inventario.InventarioExtendedForm' where name ='inventario_parcelas';
update layers set extended_form ='com.geopista.app.inventario.InventarioExtendedForm' where name ='inventario_vias';

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'loadGeometryViasNumPolicia') THEN
		insert into query_catalog (id,query,acl,idperm) values ('loadGeometryViasNumPolicia','select vias.id as id_vias, vias.id_municipio,tipoviaine, nombreviaine, nombrecatastro, numeros_policia.id as id_numeros_policia, numeros_policia.rotulo, astext(numeros_policia."GEOMETRY") as geometria, srid(numeros_policia."GEOMETRY") as srid  from vias, numeros_policia where numeros_policia.id_via=vias.id and vias.id_municipio=? order by vias.id',1,9205);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO10";

-- Fin 16-03-2011.sql

-- Inicio 4-05-2011.sql

Update query_catalog set query='select nextval(''seq_entidad_generadora'')' where id='MCnextSecuenciaEntidadGeneradora';

Update query_catalog set query='insert into queries (id, id_layer, databasetype, selectquery, updatequery, insertquery, deletequery) values (nextval(''seq_queries'')' where id='LMinsertarQueriesSecuencial';

Update query_catalog set query='select nextval(''"seq_unidad_constructiva"'')' where id='catfinurbmaxunidadconstructiva';

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'seq_entidadgeneradora') THEN	
		ALTER TABLE seq_entidadgeneradora RENAME TO seq_entidad_generadora;
	END IF;
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'seq_querys') THEN	
		ALTER TABLE seq_querys RENAME TO seq_queries;
	END IF;
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'seq_unidades_constructivas') THEN	
		ALTER TABLE seq_unidades_constructivas RENAME TO seq_unidad_constructiva;
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO11";

-- Fin 4-05-2011.sql

-- Inicio asociarViasCatastroINE.sql
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'selectViasIne') THEN
		INSERT INTO query_catalog(id, query) VALUES('selectViasIne', 'SELECT id_via, nombrecortoine, nombreine, posiciontipovia, tipovia, codigoviaine, id_municipio FROM viasine WHERE id_municipio = ? ORDER BY nombreine');
	END IF;
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'selectViasCatastro') THEN
		INSERT INTO query_catalog(id, query) VALUES('selectViasCatastro', 'SELECT nombrecatastro, id, codigoine, nombreviaine, posiciontipovia, tipoviaine FROM vias WHERE nombrecatastro != '''' AND id_municipio = ? ORDER BY nombrecatastro');
	END IF;
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'viasIneeliminarAsociaciones') THEN
		INSERT INTO query_catalog(id, query) VALUES('viasIneeliminarAsociaciones', 'UPDATE viasine SET id_via = null WHERE id_municipio = ?');
	END IF;
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'viasCatastroeliminarAsociaciones') THEN
		INSERT INTO query_catalog(id, query) VALUES('viasCatastroeliminarAsociaciones', 'UPDATE vias SET codigoine = null,nombreviaine=null WHERE id_municipio = ?');
	END IF;
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'viasCatastroActualizar') THEN
		INSERT INTO query_catalog(id, query) VALUES('viasCatastroActualizar', 'UPDATE vias SET codigoine = ? WHERE id = ? AND id_municipio = ?');
	END IF;
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'viasCatastroActualizar2') THEN
		INSERT INTO query_catalog(id, query) VALUES('viasCatastroActualizar2', 'UPDATE vias SET codigoine = ?,nombreviaine=? WHERE id = ? AND id_municipio = ?');
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO12";


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'tramosviaine' AND column_name = 'cun') THEN	
		ALTER TABLE tramosviaine add column cun varchar(7);
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO13";

update query_catalog set query='insert into tramosviaine(id,id_via,id_seccion,id_subseccion,id_pseudovia,TipoNumeracion,ein,cein,esn,cesn,id_municipio,cun,denominacion) values (nextval(''seq_tramosviaine''),?,?,?,?,?,?,?,?,?,?,?,?)' where id='tramosViasIneInsertar';

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT id FROM query_catalog WHERE id = 'selectViasIne2') THEN
		INSERT INTO query_catalog(id, query) VALUES('selectViasIne2', 'SELECT  distinct(viasine.id_via),nombrecortoine, nombreine, posiciontipovia, tipovia,codigoviaine,viasine.id_municipio,tramosviaine.denominacion,cun,substring(cun,1,2) as entidadcolectiva,entidadescolectivas.nombreoficial from viasine inner join tramosviaine on (viasine.codigoviaine=tramosviaine.id_via and viasine.id_municipio=tramosviaine.id_municipio) left outer join entidadescolectivas on (substring(tramosviaine.cun,1,2)=entidadescolectivas.codigoine and tramosviaine.id_municipio=entidadescolectivas.id_municipio) WHERE viasine.id_municipio=? ORDER BY nombreine');
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO14";

update query_catalog set query='UPDATE vias SET codigoine = null,nombreviaine=null WHERE id_municipio = ?' where id='viasCatastroeliminarAsociaciones';


update query_catalog set query='UPDATE vias SET codigoine = ?,nombreviaine=?, nombreviacortoine=? WHERE id = ? AND id_municipio = ?' where id='viasCatastroActualizar2';

-- Fin asociarViasCatastroINE.sql


-- Inicio Modificaciones para evitar el problema de Foreign Key de bienes_inventario
DROP TABLE IF EXISTS _tmp_lcg3_bienes_inventario;
DROP TABLE IF EXISTS _tmp_lcg3_semoviente;
DROP TABLE IF EXISTS _tmp_lcg3_vehiculo;
DROP TABLE IF EXISTS _tmp_lcg3_vias_inventario;
DROP TABLE IF EXISTS _tmp_lcg3_credito_derecho;
DROP TABLE IF EXISTS _tmp_lcg3_valor_mobiliario;
DROP TABLE IF EXISTS _tmp_lcg3_derechos_reales;
DROP TABLE IF EXISTS _tmp_lcg3_muebles;
DROP TABLE IF EXISTS _tmp_lcg3_inmuebles;
DROP TABLE IF EXISTS _tmp_lcg3_usos_funcionales_inventario;
DROP TABLE IF EXISTS _tmp_lcg3_inmuebles_rusticos;
DROP TABLE IF EXISTS _tmp_lcg3_inmuebles_urbanos;
DROP TABLE IF EXISTS _tmp_lcg3_refcatastrales_inventario;
DROP TABLE IF EXISTS _tmp_lcg3_mejoras_inventario;
DROP TABLE IF EXISTS _tmp_lcg3_observaciones_inventario;


SELECT * INTO _tmp_lcg3_bienes_inventario FROM bienes_inventario;
SELECT * INTO _tmp_lcg3_semoviente FROM semoviente;
SELECT * INTO _tmp_lcg3_vehiculo FROM vehiculo;
SELECT * INTO _tmp_lcg3_vias_inventario FROM vias_inventario;
SELECT * INTO _tmp_lcg3_credito_derecho FROM credito_derecho;
SELECT * INTO _tmp_lcg3_valor_mobiliario FROM valor_mobiliario;
SELECT * INTO _tmp_lcg3_derechos_reales FROM derechos_reales;
SELECT * INTO _tmp_lcg3_muebles FROM muebles;
SELECT * INTO _tmp_lcg3_inmuebles FROM inmuebles;
SELECT * INTO _tmp_lcg3_usos_funcionales_inventario FROM usos_funcionales_inventario;
SELECT * INTO _tmp_lcg3_inmuebles_rusticos FROM inmuebles_rusticos;
SELECT * INTO _tmp_lcg3_inmuebles_urbanos FROM inmuebles_urbanos;
SELECT * INTO _tmp_lcg3_refcatastrales_inventario FROM refcatastrales_inventario;
SELECT * INTO _tmp_lcg3_mejoras_inventario FROM mejoras_inventario;
SELECT * INTO _tmp_lcg3_observaciones_inventario FROM observaciones_inventario;

DELETE FROM semoviente WHERE revision_expirada != 9999999999;
DELETE FROM vehiculo WHERE revision_expirada != 9999999999;
DELETE FROM vias_inventario WHERE revision_expirada != 9999999999;
DELETE FROM credito_derecho WHERE revision_expirada != 9999999999;
DELETE FROM valor_mobiliario WHERE revision_expirada != 9999999999;
DELETE FROM derechos_reales WHERE revision_expirada != 9999999999;
DELETE FROM muebles WHERE revision_expirada != 9999999999;
DELETE FROM inmuebles WHERE revision_expirada != 9999999999;

UPDATE semoviente SET revision_actual = 0;
UPDATE vehiculo SET revision_actual = 0;
UPDATE vias_inventario SET revision_actual = 0;
UPDATE credito_derecho SET revision_actual = 0;
UPDATE valor_mobiliario SET revision_actual = 0;
UPDATE derechos_reales SET revision_actual = 0;
UPDATE muebles SET revision_actual = 0;
UPDATE inmuebles SET revision_actual = 0;

-- Fin Modificaciones para evitar el problema de Foreign Key de bienes_inventario


-- Inicio inventario_patrimonio.sql

--CREATE TABLE tables_inventario
--(
-- id_table numeric(7) NOT NULL,
--  name varchar(70) NOT NULL,
--  CONSTRAINT tables_inventario_pkey PRIMARY KEY (id_table)
--); 
-- insert into tables_inventario(id_table,name) values(12001,'credito_derecho');
-- alter table credito_derecho add revision_actual numeric(10) default 0;
-- alter table credito_derecho add revision_expirada numeric(10) default 9999999999;
-- alter table credito_derecho drop constraint credito_derecho_pkey;
-- alter table credito_derecho add constraint credito_derecho_pkey primary key (id, revision_actual);
-- insert into versiones (revision,id_autor,fecha,id_table_versionada) values (0,100,localtimestamp,12001);
-- CREATE SEQUENCE seq_inventario_credito_derecho INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

-- insert into tables_inventario(id_table,name) values(12002,'derechos_reales');
-- alter table derechos_reales add revision_actual numeric(10) default 0;
-- alter table derechos_reales add revision_expirada numeric(10) default 9999999999;
-- alter table derechos_reales drop constraint derechosreales_pkey;
-- alter table derechos_reales add constraint derechosreales_pkey primary key (id, revision_actual);
-- insert into versiones (revision,id_autor,fecha,id_table_versionada) values (0,100,localtimestamp,12002);
-- CREATE SEQUENCE seq_inventario_derechos_reales INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

-- insert into tables_inventario(id_table,name) values(12003,'inmuebles');
-- alter table inmuebles add revision_actual numeric(10) default 0;
-- alter table inmuebles add revision_expirada numeric(10) default 9999999999;
-- alter table inmuebles_rusticos drop constraint inmuebles_rusticos_fkey;
-- alter table inmuebles_urbanos drop constraint inmuebles_urbanos_fkey;
-- alter table inmuebles drop constraint inmuebles_pkey;
-- alter table inmuebles add constraint inmuebles_pkey primary key (id, revision_actual);
-- insert into versiones (revision,id_autor,fecha,id_table_versionada) values (0,100,localtimestamp,12003);
-- CREATE SEQUENCE seq_inventario_inmuebles INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

-- insert into tables_inventario(id_table,name) values(12004,'muebles');
-- alter table muebles add revision_actual numeric(10) default 0;
-- alter table muebles add revision_expirada numeric(10) default 9999999999;
-- alter table muebles drop constraint muebles_pkey;
-- alter table muebles add constraint muebles_pkey primary key (id, revision_actual);
-- insert into versiones (revision,id_autor,fecha,id_table_versionada) values (0,100,localtimestamp,12004);
-- CREATE SEQUENCE seq_inventario_muebles INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

-- insert into tables_inventario(id_table,name) values(12005,'semoviente');
-- alter table semoviente add revision_actual numeric(10) default 0;
-- alter table semoviente add revision_expirada numeric(10) default 9999999999;
-- alter table semoviente drop constraint semoviente_pkey;
-- alter table semoviente add constraint semoviente_pkey primary key (id, revision_actual);
-- insert into versiones (revision,id_autor,fecha,id_table_versionada) values (0,100,localtimestamp,12005);
-- CREATE SEQUENCE seq_inventario_semoviente INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

-- insert into tables_inventario(id_table,name) values(12006,'valor_mobiliario');
-- alter table valor_mobiliario add revision_actual numeric(10) default 0;
-- alter table valor_mobiliario add revision_expirada numeric(10) default 9999999999;
-- alter table valor_mobiliario drop constraint valor_mobiliario_pkey;
-- alter table valor_mobiliario add constraint valor_mobiliario_pkey primary key (id, revision_actual);
-- insert into versiones (revision,id_autor,fecha,id_table_versionada) values (0,100,localtimestamp,12006);
-- CREATE SEQUENCE seq_inventario_valor_mobiliario INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

-- insert into tables_inventario(id_table,name) values(12007,'vehiculo');
-- alter table vehiculo add revision_actual numeric(10) default 0;
-- alter table vehiculo add revision_expirada numeric(10) default 9999999999;
-- alter table vehiculo drop constraint vehiculo_pkey;
-- alter table vehiculo add constraint vehiculo_pkey primary key (id, revision_actual);
-- insert into versiones (revision,id_autor,fecha,id_table_versionada) values (0,100,localtimestamp,12007);
-- CREATE SEQUENCE seq_inventario_vehiculo INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

-- insert into tables_inventario(id_table,name) values(12008,'vias_inventario');
-- alter table vias_inventario add revision_actual numeric(10) default 0;
-- alter table vias_inventario add revision_expirada numeric(10) default 9999999999;
-- alter table vias_inventario drop constraint vias_inventario_pkey;
-- alter table vias_inventario add constraint vias_inventario_pkey primary key (id, revision_actual);
-- insert into versiones (revision,id_autor,fecha,id_table_versionada) values (0,100,localtimestamp,12008);
-- CREATE SEQUENCE seq_inventario_vias_inventario INCREMENT 1 MINVALUE 1 START 1 CACHE 1;

-- Todo lo anterior ya está ejecutado en LocalGIS 2.0
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN


	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'bienes_inventario' AND column_name = 'revision_actual') THEN	
		alter table bienes_inventario add revision_actual numeric(10) default 0;
	END IF;
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'bienes_inventario' AND column_name = 'revision_expirada') THEN	
		alter table bienes_inventario add revision_expirada numeric(10) default 9999999999;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'semoviente_fkey' ) THEN	
		alter table semoviente drop constraint semoviente_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'vehiculo_fkey') THEN	
		alter table vehiculo drop constraint vehiculo_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'vias_inventario_fkey') THEN	
		alter table vias_inventario drop constraint vias_inventario_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'credito_derecho_fkey') THEN	
		alter table credito_derecho drop constraint credito_derecho_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'valor_mobiliario_fkey') THEN	
		alter table valor_mobiliario drop constraint valor_mobiliario_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'derechosreales_fk') THEN	
		alter table derechos_reales drop constraint derechosreales_fk;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'derechos_reales_fkey') THEN	
		alter table derechos_reales drop constraint derechos_reales_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'muebles_fkey') THEN	
		alter table muebles drop constraint muebles_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'inmuebles_fkey') THEN	
		alter table inmuebles drop constraint inmuebles_fkey;
	END IF;	
	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'usos_funcionales_inventario_fkey') THEN	
		alter table usos_funcionales_inventario drop constraint usos_funcionales_inventario_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'refcatastrales_inventario_fkey') THEN	
		alter table refcatastrales_inventario drop constraint refcatastrales_inventario_fkey;
	END IF;	
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'mejoras_inventario_fkey') THEN	
		alter table mejoras_inventario drop constraint mejoras_inventario_fkey;
	END IF;		
	IF EXISTS (SELECT conname FROM pg_constraint WHERE conname = 'observaciones_inventario_fkey') THEN	
		alter table observaciones_inventario drop constraint observaciones_inventario_fkey;
	END IF;	
	
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'usos_funcionales_inventario' AND column_name = 'revision_actual') THEN	
		alter table usos_funcionales_inventario add revision_actual numeric(10) default 0;
	END IF;
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'usos_funcionales_inventario' AND column_name = 'revision_expirada') THEN	
		alter table usos_funcionales_inventario add revision_expirada numeric(10) default 9999999999;
	END IF;	
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'inmuebles_rusticos' AND column_name = 'revision_actual') THEN	
		alter table inmuebles_rusticos add revision_actual numeric(10) default 0;
	END IF;
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'inmuebles_rusticos' AND column_name = 'revision_expirada') THEN	
		alter table inmuebles_rusticos add revision_expirada numeric(10) default 9999999999;
	END IF;	
	IF EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'inmuebles_rusticos' AND constraint_name = 'inmuebles_rusticos_pkey') THEN	
		alter table inmuebles_rusticos drop constraint inmuebles_rusticos_pkey;
	END IF;		
	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'inmuebles_rusticos' AND constraint_name = 'inmuebles_rusticos_pkey') THEN	
		alter table inmuebles_rusticos add constraint inmuebles_rusticos_pkey primary key (id, revision_actual);
	END IF;	
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'inmuebles_urbanos' AND column_name = 'revision_actual') THEN	
		alter table inmuebles_urbanos add revision_actual numeric(10) default 0;
	END IF;
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'inmuebles_urbanos' AND column_name = 'revision_expirada') THEN	
		alter table inmuebles_urbanos add revision_expirada numeric(10) default 9999999999;
	END IF;	
	IF EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'inmuebles_urbanos' AND constraint_name = 'inmuebles_urbanos_pkey') THEN	
		alter table inmuebles_urbanos drop constraint inmuebles_urbanos_pkey;
	END IF;	
	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'inmuebles_urbanos' AND constraint_name = 'inmuebles_urbanos_pkey') THEN	
		alter table inmuebles_urbanos add constraint inmuebles_urbanos_pkey primary key (id, revision_actual);
	END IF;	
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'refcatastrales_inventario' AND column_name = 'revision_actual') THEN	
		alter table refcatastrales_inventario add revision_actual numeric(10) default 0;
	END IF;
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'refcatastrales_inventario' AND column_name = 'revision_expirada') THEN	
		alter table refcatastrales_inventario add revision_expirada numeric(10) default 9999999999;
	END IF;	
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'mejoras_inventario' AND column_name = 'revision_actual') THEN	
		alter table mejoras_inventario add revision_actual numeric(10) default 0;
	END IF;
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'mejoras_inventario' AND column_name = 'revision_expirada') THEN	
		alter table mejoras_inventario add revision_expirada numeric(10) default 9999999999;
	END IF;	
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'observaciones_inventario' AND column_name = 'revision_actual') THEN	
		alter table observaciones_inventario add revision_actual numeric(10) default 0;
	END IF;
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'observaciones_inventario' AND column_name = 'revision_expirada') THEN	
		alter table observaciones_inventario add revision_expirada numeric(10) default 9999999999;
	END IF;	
	IF EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'bienes_inventario' AND constraint_name = 'bienes_inventario_pkey') THEN	
		alter table bienes_inventario drop constraint bienes_inventario_pkey cascade;
	END IF;	
	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'bienes_inventario' AND constraint_name = 'bienes_inventario_pkey') THEN	
		alter table bienes_inventario add constraint bienes_inventario_pkey primary key (id, revision_actual);
	END IF;		
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO15";



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF EXISTS (SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_amortizacion') THEN	
		alter sequence seq_amortizacion RESTART WITH 3;
	END IF;	
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.1";	


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'semoviente' AND constraint_name = 'semoviente_fkey') THEN			
		alter table semoviente add constraint semoviente_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para semoviente';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.1";	

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'vehiculo' AND column_name = 'vehiculo_fkey') THEN	
		alter table vehiculo add constraint vehiculo_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para vehiculo';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.2";	

	
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'vias_inventario' AND constraint_name = 'vias_inventario_fkey') THEN	
		alter table vias_inventario add constraint vias_inventario_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para vias_inventario';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.3";		
	
	
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'credito_derecho' AND constraint_name = 'credito_derecho_fkey') THEN	
		alter table credito_derecho add constraint credito_derecho_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para credito_derecho';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.4";	
	

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'valor_mobiliario' AND constraint_name = 'valor_mobiliario_fkey') THEN	
		alter table valor_mobiliario add constraint valor_mobiliario_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para valor_mobiliario';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.5";	

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN


	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'derechos_reales' AND constraint_name = 'derechos_reales_fkey') THEN	
		alter table derechos_reales add constraint derechos_reales_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para derechos_reales';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.6";
	
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'muebles' AND constraint_name = 'muebles_fkey') THEN	
		alter table muebles add constraint muebles_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para muebles';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.7";	

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'inmuebles' AND constraint_name = 'inmuebles_fkey') THEN	
		alter table inmuebles add constraint inmuebles_fkey FOREIGN KEY (id,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para inmuebles';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.8";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'usos_funcionales_inventario' AND constraint_name = 'usos_funcionales_inventario_fkey') THEN		
		alter table usos_funcionales_inventario add constraint usos_funcionales_inventario_fkey FOREIGN KEY (id_bien,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para usos_funciones_inventario';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.9";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'refcatastrales_inventario' AND constraint_name = 'refcatastrales_inventario_fkey') THEN	
		alter table refcatastrales_inventario add constraint refcatastrales_inventario_fkey FOREIGN KEY (id_bien,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para refcatastrales_inventario';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.10";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'mejoras_inventario' AND constraint_name = 'mejoras_inventario_fkey') THEN		
		alter table mejoras_inventario add constraint mejoras_inventario_fkey FOREIGN KEY (id_bien,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para mejoras_inventario';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.11";
	
	
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN

	IF NOT EXISTS (SELECT * FROM information_schema.constraint_column_usage WHERE table_schema = 'public' AND table_name = 'observaciones_inventario' AND constraint_name = 'observaciones_inventario_fkey') THEN	
		alter table observaciones_inventario add constraint observaciones_inventario_fkey FOREIGN KEY (id_bien,revision_actual) REFERENCES bienes_inventario (id,revision_actual);
	END IF;	
	exception when others then
		RAISE NOTICE 'No se puede fijar la foreign key para observaciones_inventario';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "PASO15.12";	


-- Fin inventario_patrimonio.sql

-- Inicio 22-03-2011.sql

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'vias' AND column_name = 'tipo') THEN	
		ALTER TABLE vias ADD COLUMN tipo varchar(20);
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO16";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	DROP INDEX IF EXISTS "indice_tipo";
	CREATE INDEX "indice_tipo"
		ON bienes_inventario USING btree(tipo);
  
  
	DROP INDEX IF EXISTS "indice_numinventario";
	CREATE INDEX "indice_numinventario"
	  ON bienes_inventario
	  USING btree (numinventario);
  
  DROP INDEX IF EXISTS "indice_id_bienes_inventario" ;
 CREATE INDEX "indice_id_bienes_inventario"
  ON bienes_inventario
  USING btree (id);
  
  DROP INDEX IF EXISTS "indice_id_vias_inventario" ;
   CREATE INDEX "indice_id_vias_inventario"
  ON vias_inventario
  USING btree(id);
  
  DROP INDEX IF EXISTS "indice_revision_expirada" ;
  CREATE INDEX "indice_revision_expirada"
  ON bienes_inventario
  USING btree(revision_expirada);
  
  DROP INDEX IF EXISTS "indice_borrado" ;
  CREATE INDEX "indice_borrado"
  ON bienes_inventario
  USING btree (borrado);

  DROP INDEX IF EXISTS "indice_inventario_idmunicipio" ;
  CREATE INDEX "indice_inventario_idmunicipio"
  ON bienes_inventario
  USING btree
  (id_municipio);
  
  DROP INDEX IF EXISTS "indice_observaciones_id_bien" ;
  CREATE INDEX "indice_observaciones_id_bien"
  ON observaciones_inventario
  USING btree
  (id_bien);
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO17";
  
-- Fin 22-03-2011.sql

-- Inicio actualizaciones.sql
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
DROP INDEX IF EXISTS "indice_inventario_feature_id_feature" ;
CREATE INDEX "indice_inventario_feature_id_feature"
  ON inventario_feature
  USING btree
  (id_feature);  
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO18";
  
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'map_server_layers' AND column_name = 'name') THEN	
		ALTER TABLE map_server_layers ADD COLUMN "name" character varying(200);
	END IF;	
	IF EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'guiaurbana' AND table_name = 'coveragelayer' AND column_name = 'id_municipio') THEN	
		ALTER TABLE guiaurbana.coveragelayer ALTER id_municipio TYPE numeric(5,0);
	END IF;	
	IF EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'civil_work_warnings' AND column_name = 'id_municipio') THEN	
		ALTER TABLE civil_work_warnings ALTER id_municipio TYPE numeric(5,0);
	END IF;		
	IF EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'history' AND column_name = 'municipio') THEN	
		ALTER TABLE history ALTER municipio TYPE numeric(5,0);
	END IF;	
	IF EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'visualizador' AND table_name = 'coveragelayer' AND column_name = 'id_municipio') THEN	
		ALTER TABLE visualizador.coveragelayer ALTER id_municipio TYPE numeric(5,0);
	END IF;
	IF EXISTS (SELECT sequence_name FROM information_schema.sequences WHERE sequence_schema = 'public' AND sequence_name = 'seq_compannias_seguros') THEN	
		ALTER TABLE seq_compannias_seguros RENAME TO seq_compannia_seguros;
	END IF;	
	IF EXISTS (SELECT table_name FROM information_schema.columns WHERE table_schema = 'public' AND table_name = 'seguros_inventario' AND column_name = 'poliza') THEN	
		ALTER TABLE seguros_inventario ALTER poliza TYPE numeric(14,0);
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO19";
  
Update query_catalog set query='insert into queries (id, id_layer, databasetype, selectquery, updatequery, insertquery, deletequery) values (nextval(''seq_queries''),?,?,?,?,?,?)' where id='LMinsertarQueriesSecuencial';


-- Fin actualizaciones.sql

