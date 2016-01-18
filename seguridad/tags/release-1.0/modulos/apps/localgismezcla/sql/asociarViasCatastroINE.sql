INSERT INTO query_catalog(id, query) VALUES('selectViasIne', 'SELECT id_via, nombrecortoine, nombreine, posiciontipovia, tipovia, codigoviaine, id_municipio FROM viasine WHERE id_municipio = ? ORDER BY nombreine');
INSERT INTO query_catalog(id, query) VALUES('selectViasCatastro', 'SELECT nombrecatastro, id, codigoine, nombreviaine, posiciontipovia, tipoviaine FROM vias WHERE nombrecatastro != '''' AND id_municipio = ? ORDER BY nombrecatastro');
INSERT INTO query_catalog(id, query) VALUES('viasIneeliminarAsociaciones', 'UPDATE viasine SET id_via = null WHERE id_municipio = ?');
INSERT INTO query_catalog(id, query) VALUES('viasCatastroeliminarAsociaciones', 'UPDATE vias SET codigoine = null,nombreviaine=null WHERE id_municipio = ?');
INSERT INTO query_catalog(id, query) VALUES('viasCatastroActualizar', 'UPDATE vias SET codigoine = ? WHERE id = ? AND id_municipio = ?');
INSERT INTO query_catalog(id, query) VALUES('viasCatastroActualizar2', 'UPDATE vias SET codigoine = ?,nombreviaine=? WHERE id = ? AND id_municipio = ?');

--Actualizacion para contemplar el codigo de la entidad poblacional. CUN . El ultimo campo
ALTER TABLE tramosviaine add column cun varchar(7);
update query_catalog set query='insert into tramosviaine(id,id_via,id_seccion,id_subseccion,id_pseudovia,TipoNumeracion,ein,cein,esn,cesn,id_municipio,cun,denominacion) values (nextval(''seq_tramosviaine''),?,?,?,?,?,?,?,?,?,?,?,?)' where id='tramosViasIneInsertar';

INSERT INTO query_catalog(id, query) VALUES('selectViasIne2', 'SELECT  distinct(viasine.id_via),nombrecortoine, nombreine, posiciontipovia, tipovia,codigoviaine,viasine.id_municipio,tramosviaine.denominacion,cun,substring(cun,1,2) as entidadcolectiva,entidadescolectivas.nombreoficial from viasine inner join tramosviaine on (viasine.codigoviaine=tramosviaine.id_via and viasine.id_municipio=tramosviaine.id_municipio) left outer join entidadescolectivas on (substring(tramosviaine.cun,1,2)=entidadescolectivas.codigoine and tramosviaine.id_municipio=entidadescolectivas.id_municipio) WHERE viasine.id_municipio=? ORDER BY nombreine');
update query_catalog set query='UPDATE vias SET codigoine = null,nombreviaine=null WHERE id_municipio = ?' where id='viasCatastroeliminarAsociaciones';


update query_catalog set query='UPDATE vias SET codigoine = ?,nombreviaine=?, nombreviacortoine=? WHERE id = ? AND id_municipio = ?' where id='viasCatastroActualizar2';
