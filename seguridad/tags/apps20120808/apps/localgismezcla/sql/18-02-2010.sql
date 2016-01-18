update queries set selectquery='SELECT Parcelas.id, Parcelas.referencia_catastral, Parcelas.id_municipio, Parcelas.primer_numero, Parcelas.primera_letra, Parcelas.segundo_numero, Parcelas.segunda_letra, Parcelas.kilometro, Parcelas.bloque,Parcelas.direccion_no_estructurada, Parcelas.codigo_postal, Parcelas.fecha_alta, Parcelas.fecha_baja, transform(Parcelas."GEOMETRY", ?T) as "GEOMETRY", Parcelas.codigodelegacionmeh, Parcelas.nombreentidadmenor, Parcelas.id_distrito, Parcelas.codigozonaconcentracion, Parcelas.codigopoligono, Parcelas.codigoparcela, Parcelas.nombreparaje, Parcelas.id_via, Parcelas.area, Parcelas.length, Parcelas.anno_expediente, Parcelas.referencia_expediente, Parcelas.codigo_entidad_colaboradora, Parcelas.tipo_movimiento, Parcelas.codigo_municipiodgc, Parcelas.bice, Parcelas.codigo_provinciaine, Parcelas.codigo_municipio_localizaciondgc, Parcelas.codigo_municipio_localizacionine, Parcelas.codigo_municipio_origendgc, Parcelas.codigo_paraje, Parcelas.superficie_finca, Parcelas.superficie_construida_total, Parcelas.superficie_const_sobrerasante, Parcelas.superficie_const_bajorasante, Parcelas.superficie_cubierta, Parcelas.anio_aprobacion, Parcelas.codigo_calculo_valor, Parcelas.poligono_catastral_valoracion, Parcelas.modalidad_reparto, Parcelas.indicador_infraedificabilidad, Parcelas.movimiento_baja, Parcelas.coordenada_x, Parcelas.coordenada_y, Parcelas.modificado, Municipios.NombreOficial, count(inventario_feature.id_bien) as id2 FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) LEFT JOIN inventario_feature ON (parcelas.id=inventario_feature.id_feature and inventario_feature.id_layer=(select id_layer from layers where name = ''inventario_parcelas'')) WHERE Municipios.ID in (?M) AND Parcelas.Fecha_baja IS NULL GROUP BY Parcelas.id, Parcelas.anio_aprobacion, Parcelas.anno_expediente, Parcelas.area, Parcelas.bice, Parcelas.bloque, Parcelas.codigodelegacionmeh, Parcelas.codigoparcela, Parcelas.codigopoligono, Parcelas.codigozonaconcentracion, Parcelas.codigo_calculo_valor, Parcelas.codigo_entidad_colaboradora, Parcelas.codigo_municipiodgc, Parcelas.codigo_municipio_localizaciondgc, Parcelas.codigo_municipio_localizacionine, Parcelas.codigo_municipio_origendgc, Parcelas.codigo_paraje, Parcelas.codigo_postal, Parcelas.codigo_provinciaine, Parcelas.coordenada_x, Parcelas.coordenada_y, Parcelas.direccion_no_estructurada, Parcelas.fecha_alta, Parcelas.fecha_baja, Parcelas."GEOMETRY", Parcelas.id_distrito, Parcelas.id_municipio, Parcelas.id_via, Parcelas.indicador_infraedificabilidad, Parcelas.kilometro, Parcelas.length, Parcelas.modalidad_reparto, Parcelas.movimiento_baja, Parcelas.nombreentidadmenor, Parcelas.nombreparaje, Parcelas.poligono_catastral_valoracion, Parcelas.primera_letra, Parcelas.primer_numero, Parcelas.referencia_catastral, Parcelas.referencia_expediente, Parcelas.segunda_letra, Parcelas.segundo_numero,Parcelas.superficie_construida_total, Parcelas.superficie_const_bajorasante, Parcelas.superficie_const_sobrerasante, Parcelas.superficie_cubierta, Parcelas.superficie_finca, Parcelas.tipo_movimiento, Parcelas.modificado,Municipios.NombreOficial' 
where id_layer=(select id_layer from layers where name = 'inventario_parcelas');
update queries set selectquery='SELECT Vias.id, Vias.codigoine, Vias.codigocatastro, Vias.tipovianormalizadocatastro, Vias.tipoviaine, Vias.posiciontipovia, Vias.nombreviaine, Vias.nombreviacortoine, Vias.nombrecatastro, Vias.id_municipio, transform(Vias."GEOMETRY", ?T) as "GEOMETRY", Vias.length, Vias.idalp, Vias.fechagrabacionayto, Vias.fechagrabacioncierre, Vias.fechaejecucion, Municipios.NombreOficial, count(inventario_feature.id_bien) as id2 FROM Municipios INNER JOIN (Vias LEFT JOIN TiposViaINE ON Vias.TipoViaINE=TiposViaINE.ID::text) ON (Municipios.ID=Vias.ID_Municipio) LEFT JOIN inventario_feature ON (vias.id=inventario_feature.id_feature and inventario_feature.id_layer=(select id_layer from layers where name = ''inventario_vias'')) WHERE Municipios.ID in (?M) GROUP BY Vias.codigocatastro, Vias.codigoine, Vias.fechaejecucion, Vias.fechagrabacionayto, Vias.fechagrabacioncierre, Vias."GEOMETRY", Vias.id, Vias.idalp, Vias.id_municipio, Vias.length, Vias.nombrecatastro, Vias.nombreviacortoine, Vias.nombreviaine, Vias.posiciontipovia, Vias.tipoviaine, Vias.tipovianormalizadocatastro,Municipios.nombreoficial'  
where id_layer=(select id_layer from layers where name = 'inventario_vias');
update queries set selectquery = 'SELECT Vias.id, Vias.codigoine, Vias.codigocatastro, Vias.tipovianormalizadocatastro, Vias.tipoviaine, Vias.posiciontipovia, Vias.nombreviaine, Vias.nombreviacortoine, Vias.nombrecatastro, Vias.id_municipio, transform(Vias."GEOMETRY", ?T) as "GEOMETRY", Vias.length, Vias.idalp, Vias.fechagrabacionayto, Vias.fechagrabacioncierre, Vias.fechaejecucion, Municipios.NombreOficial FROM Municipios INNER JOIN (Vias LEFT JOIN TiposViaINE ON Vias.TipoViaINE=TiposViaINE.ID::text) ON (Municipios.ID=Vias.ID_Municipio) WHERE Municipios.ID in (?M)' where id = 13;
update queries set selectquery = 'SELECT TramosViaINE.id, TramosViaINE.id_via, TramosViaINE.id_seccion, TramosViaINE.id_subseccion, TramosViaINE.id_pseudovia, TramosViaINE.ein, TramosViaINE.cein, TramosViaINE.esn, TramosViaINE.cesn, TramosViaINE.tiponumeracion, TramosViaINE.denominacion, TramosViaINE.id_municipio, transform(TramosViaINE."GEOMETRY", ?T) as "GEOMETRY", TramosViaINE.length, Vias.NombreViaINE, Vias.NombreViaCortoINE, TiposViaINE.ID_TipoNormalizado FROM (Vias LEFT JOIN TiposViaINE ON Vias.TipoViaINE=TiposViaINE.ID::text) INNER JOIN TramosViaINE ON (Vias.ID=TramosViaINE.ID_Via) WHERE TramosViaINE.ID_Municipio in (?M)' where id = 15;
update queries set selectquery = 'select solicitud_licencia.id_solicitud as ID, solicitud_licencia.id_tipo_licencia, solicitud_licencia.id_tipo_obra, solicitud_licencia.motivo, expediente_licencia.num_expediente, expediente_licencia.id_estado, expediente_licencia.servicio_encargado, expediente_licencia.asunto, parcelario.ref_catastral, transform(parcelas."GEOMETRY", ?T) as "GEOMETRY" from parcelas, parcelario, solicitud_licencia, expediente_licencia where (solicitud_licencia.id_tipo_licencia=0) and expediente_licencia.id_estado<> 13 and solicitud_licencia.id_municipio in (?M) and solicitud_licencia.id_solicitud=expediente_licencia.id_solicitud and parcelario.id_solicitud=expediente_licencia.id_solicitud::text and parcelario.ref_catastral= parcelas.referencia_catastral' where id = 10002;
update queries set selectquery = 'select solicitud_licencia.id_solicitud as ID, solicitud_licencia.id_tipo_licencia, solicitud_licencia.id_tipo_obra, solicitud_licencia.motivo, expediente_licencia.num_expediente, expediente_licencia.id_estado, expediente_licencia.servicio_encargado, expediente_licencia.asunto, parcelario.ref_catastral, transform(parcelas."GEOMETRY", ?T) as "GEOMETRY" from parcelas, parcelario, solicitud_licencia, expediente_licencia where (solicitud_licencia.id_tipo_licencia=2 or solicitud_licencia.id_tipo_licencia=3) and expediente_licencia.id_estado<>13 and solicitud_licencia.id_municipio in (?M) and solicitud_licencia.id_solicitud=expediente_licencia.id_solicitud and parcelario.id_solicitud=expediente_licencia.id_solicitud::text and parcelario.ref_catastral= parcelas.referencia_catastral' where id = 10003;
update queries set selectquery = 'select solicitud_licencia.id_solicitud as ID, solicitud_licencia.id_tipo_licencia, solicitud_licencia.id_tipo_obra, solicitud_licencia.motivo, expediente_licencia.num_expediente, expediente_licencia.id_estado, expediente_licencia.servicio_encargado, expediente_licencia.asunto, parcelario.ref_catastral, transform(parcelas."GEOMETRY", ?T) as "GEOMETRY" from parcelas, parcelario, solicitud_licencia, expediente_licencia where (solicitud_licencia.id_tipo_licencia=1) and expediente_licencia.id_estado<> 13 and solicitud_licencia.id_municipio in (?M) and solicitud_licencia.id_solicitud=expediente_licencia.id_solicitud and parcelario.id_solicitud=expediente_licencia.id_solicitud::text and parcelario.ref_catastral= parcelas.referencia_catastral' where id = 10001;
--Para almacenar las versiones de los modulos
CREATE TABLE versiones_modulos
(
  modulo varchar(40) NOT NULL,
  fecha varchar(10) NOT NULL,
  id_version varchar(10) NOT NULL
); 
--Asociacion incidencias de red con espacio publico
ALTER TABLE NETWORK_INCIDENTS ADD COLUMN ID_WARNING NUMERIC;

-- 04-03-2010 -----
update query_catalog set query = 'select l.layeridgeopista, l.layername, traduccion from localgisguiaurbana.map m, localgisguiaurbana.layer l, localgisguiaurbana.maplayer ml, layers, dictionary where l.layerid=ml.layerid and layers.id_layer=l.layeridgeopista and layers.id_name=id_vocablo and locale=? and ml.mapid=m.mapid and m.mapidgeopista=? GROUP BY l.layeridgeopista, l.layername, traduccion' where id='GIGetCapasMapa';

-- Permisos del usuario consultas para los informes de actuciones del modulo Espacio Público
GRANT SELECT ON TABLE civil_work_warnings TO consultas;
GRANT SELECT ON TABLE municipios TO consultas;
GRANT SELECT ON TABLE civil_work_intervention TO consultas;

-- Se cambian a minúsculas las capas ConstruccionesGeom y CultivosGeom
CREATE TABLE construccionesgeom (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    mapa numeric(5,0),
    hoja character varying(7),
    masa character varying(7),
    tipo character varying(5),
    parcela character varying(7),
    constru character varying(50),
    coorx numeric,
    coory numeric,
    numsymbol numeric(2,0),
    area numeric,
    fechaalta date,
    fechabaja date
);

CREATE TABLE cultivosgeom (
    id numeric(8,0) NOT NULL,
    id_municipio numeric(5,0) NOT NULL,
    "GEOMETRY" geometry,
    mapa numeric(5,0),
    hoja character varying(7),
    masa character varying(7),
    tipo character varying(5),
    parcela character varying(7),
    subparce character varying(4),
    coorx numeric,
    coory numeric,
    numsymbol numeric(2,0),
    area numeric,
    fechaalta date,
    fechabaja date
);

INSERT INTO geometry_columns (f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, "type") VALUES ('', 'public', 'construccionesgeom', 'GEOMETRY', 2, 23030, 'GEOMETRY');
INSERT INTO geometry_columns (f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, "type") VALUES ('', 'public', 'cultivosgeom', 'GEOMETRY', 2, 23030, 'GEOMETRY');
ALTER TABLE ONLY construccionesgeom
    ADD CONSTRAINT construccionesgeom_pkey UNIQUE (id);
ALTER TABLE ONLY cultivosgeom
    ADD CONSTRAINT cultivosgeom_pkey UNIQUE (id);

update query_catalog set query=replace(query,'ConstruccionesGeom','construccionesgeom') where query like '%ConstruccionesGeom%';
update query_catalog set query=replace(query,'CultivosGeom','cultivosgeom') where query like '%CultivosGeom%';
delete from queries where id=10007;
delete from queries where id=10006;
INSERT INTO queries (id, id_layer, databasetype, selectquery, updatequery, insertquery, deletequery) VALUES (10007, 11005, 1, 'SELECT cultivosgeom."GEOMETRY",cultivosgeom.id,cultivosgeom.mapa,cultivosgeom.masa,cultivosgeom.parcela,cultivosgeom.subparce,cultivosgeom.hoja,cultivosgeom.tipo,cultivosgeom.numsymbol,cultivosgeom.fechaalta,cultivosgeom.fechabaja,cultivosgeom.coorx,cultivosgeom.coory,cultivosgeom.id_municipio,cultivosgeom.area FROM cultivosgeom WHERE cultivosgeom.id_municipio IN (?M)', 'UPDATE cultivosgeom SET "GEOMETRY"=GeometryFromText(?1,?S),id=?2,mapa=?3,masa=?4,parcela=?5,subparce=?6,hoja=?7,tipo=?8,numsymbol=?9,fechaalta=?10,fechabaja=?11,coorx=?12,coory=?13,id_municipio=?M,area=?15 WHERE id=?2', 'INSERT INTO cultivosgeom ("GEOMETRY",id,mapa,masa,parcela,subparce,hoja,tipo,numsymbol,fechaalta,fechabaja,coorx,coory,id_municipio,area) VALUES(GeometryFromText(?1,?S),?PK,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?M,?15)', 'DELETE FROM cultivosgeom WHERE id=?2');
INSERT INTO queries (id, id_layer, databasetype, selectquery, updatequery, insertquery, deletequery) VALUES (10006, 11004, 1, 'SELECT construccionesgeom."GEOMETRY",construccionesgeom.id,construccionesgeom.constru,construccionesgeom.coorx,construccionesgeom.coory,construccionesgeom.fechaalta,construccionesgeom.fechabaja,construccionesgeom.hoja,construccionesgeom.mapa,construccionesgeom.masa,construccionesgeom.numsymbol,construccionesgeom.parcela,construccionesgeom.tipo,construccionesgeom.id_municipio,construccionesgeom.area FROM construccionesgeom WHERE construccionesgeom.id_municipio IN (?M)', 'UPDATE construccionesgeom SET "GEOMETRY"=GeometryFromText(?1,?S),id=?2,mapa=?3,masa=?4,parcela=?5,constru=?6,hoja=?7,tipo=?8,numsymbol=?9,fechaalta=?10,fechabaja=?11,coorx=?12,coory=?13,id_municipio=?M,area=?15 WHERE id=?2', 'INSERT INTO construccionesgeom ("GEOMETRY",id,mapa,masa,parcela,constru,hoja,tipo,numsymbol,fechaalta,fechabaja,coorx,coory,id_municipio,area) VALUES(GeometryFromText(?1,?S),?PK,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?M,?15)', 'DELETE FROM construccionesgeom WHERE id=?2');

delete from tables where id_table=10005;
delete from tables where id_table=10006;
INSERT INTO tables (id_table, name, geometrytype) VALUES (10005, 'construccionesgeom', 0);
INSERT INTO tables (id_table, name, geometrytype) VALUES (10006, 'cultivosgeom', 0);

update columns set name = lower(name) where id_table=10005 and name <> 'GEOMETRY';
update columns set name = lower(name) where id_table=10006 and name <> 'GEOMETRY';

update query_catalog set query=replace(query,'ConstruccionesGeom','construccionesgeom') where query like '%ConstruccionesGeom%';
update query_catalog set query=replace(query,'CultivosGeom','cultivosgeom') where query like '%CultivosGeom%';

INSERT INTO construccionesgeom (id, id_municipio, "GEOMETRY",mapa, hoja, masa, tipo, parcela, constru, coorx, coory, numsymbol,
area, fechaalta, fechabaja) SELECT id, id_municipio, "GEOMETRY", "Mapa", "Hoja", "Masa", "Tipo", "Parcela", "Constru", "CoorX", "CoorY",
"NumSymbol", "Area", "FechaAlta", "FechaBaja" FROM "ConstruccionesGeom";

INSERT INTO cultivosgeom (id, id_municipio, "GEOMETRY",mapa, hoja, masa, tipo, parcela, subparce, coorx, coory, numsymbol,
area, fechaalta, fechabaja) SELECT id, id_municipio, "GEOMETRY", "Mapa", "Hoja", "Masa", "Tipo", "Parcela", "Subparce", "CoorX", "CoorY",
"NumSymbol", "Area", "FechaAlta", "FechaBaja" FROM "CultivosGeom";

insert into query_catalog (id,query,acl,idperm) values ('obtenerIdTabla','select id_table from tables where name=?',1,9205);


update query_catalog set query='insert into versiones (revision,id_autor,fecha,id_table_versionada) values 
(?,(select id from iuseruserhdr where upper(name) = upper(?)),?,?)' where id='insertaVersionInicial';


update query_catalog set query='delete from versiones where id_table_versionada=?' where id='deleteRevisionesTabla';

--Actualizacion GeoMarketing. Optimizacion de consultas.
create index habitantes_id_habitante on habitantes(id_habitante);
create index habitantes_id_domicilio on habitantes(id_domicilio);
create index habitantes_sexo on habitantes(sexo);
create index domicilio_id_domicilio on domicilio(id_domicilio);
create index domicilio_id_portal on domicilio(id_portal);
create index numeros_policia_id on numeros_policia(id);
create index numeros_policia_id_municipio on numeros_policia(id_municipio);
create index numeros_policia_id_via on numeros_policia(id_via);
create index queries_id_layer on queries(id_layer);
create index tramosvia_id_via on tramosvia(id_via);
create index tramosvia_id_municipio on tramosvia(id_municipio);

--Actualización Gestor de capas, corrección problemas importador de vías
update query_catalog set query='delete from versiones where id_table_versionada=?' where id='deleteRevisionesTabla';

update queries set
	updatequery = 'UPDATE Vias SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?L),ID=?2,CodigoINE=?3,CodigoCatastro=?4,TipoViaNormalizadoCatastro=?5,TipoViaINE=?6,PosicionTipoVia=?7,NombreViaINE=?8,NombreViaCortoINE=?9,NombreCatastro=?10,ID_Municipio=?M,idalp=?13,fechagrabacionayto=?14,fechagrabacioncierre=?15,fechaejecucion=?16,Length=length(transform(GeometryFromText(text(?1),?S),?L)) WHERE ID=?2',
	insertquery = 'INSERT INTO Vias("GEOMETRY",ID,CodigoINE,CodigoCatastro,TipoViaNormalizadoCatastro,TipoViaINE,PosicionTipoVia,NombreViaINE,NombreViaCortoINE,NombreCatastro,ID_Municipio,Length,idalp,fechagrabacionayto,fechagrabacioncierre,fechaejecucion) VALUES(transform(GeometryFromText(text(?1),?S), ?L),?PK,?3,?4,?5,?6,?7,?8,?9,?10,?M,length(transform(GeometryFromText(text(?1),?S),?L)),?13,?14,?15,?16)'
where id = 13;
update queries set 
	updatequery = 'UPDATE TramosVia SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?L),ID_Via=?3,NumVia=?4,Denominacion=?5,FechaAlta=?6,FechaBaja=?7,Length=length(transform(GeometryFromText(text(?1),?S),?L)) WHERE ID=?2',
	insertquery = 'INSERT INTO TramosVia("GEOMETRY",ID,ID_Via,NumVia,Denominacion,FechaAlta,FechaBaja,ID_Municipio,Length) VALUES(transform(GeometryFromText(text(?1),?S), ?L),?PK,?3,?4,?5,?6,?7,?M,length(transform(GeometryFromText(text(?1),?S),?L)))'
where id = 14;

--Actualizo en queries las tablas de construcciones y cultivos
update queries set insertquery='INSERT INTO construccionesgeom ("GEOMETRY",id,mapa,masa,parcela,constru,hoja,tipo,numsymbol,fechaalta,fechabaja,coorx,coory,id_municipio,area) VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?M,?15)' 
where id=10006;
update queries set insertquery='INSERT INTO cultivosgeom ("GEOMETRY",id,mapa,masa,parcela,subparce,hoja,tipo,numsymbol,fechaalta,fechabaja,coorx,coory,id_municipio,area) VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?M,?15)' 
where id=10007;

update queries set updatequery='UPDATE construccionesgeom SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),id=?2,mapa=?3,masa=?4,parcela=?5,constru=?6,hoja=?7,tipo=?8,numsymbol=?9,fechaalta=?10,fechabaja=?11,coorx=?12,coory=?13,id_municipio=?M,area=?15 WHERE id=?2' 
where id=10006;
update queries set updatequery='UPDATE cultivosgeom SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),id=?2,mapa=?3,masa=?4,parcela=?5,subparce=?6,hoja=?7,tipo=?8,numsymbol=?9,fechaalta=?10,fechabaja=?11,coorx=?12,coory=?13,id_municipio=?M,area=?15 WHERE id=?2' 
where id=10007;

update queries set selectquery='SELECT transform(construccionesgeom."GEOMETRY", ?T) as "GEOMETRY",construccionesgeom.id,construccionesgeom.constru,construccionesgeom.coorx,construccionesgeom.coory,construccionesgeom.fechaalta,construccionesgeom.fechabaja,construccionesgeom.hoja,construccionesgeom.mapa,construccionesgeom.masa,construccionesgeom.numsymbol,construccionesgeom.parcela,construccionesgeom.tipo,construccionesgeom.id_municipio,construccionesgeom.area FROM construccionesgeom WHERE construccionesgeom.id_municipio IN (?M)' 
where id=10006;
update queries set selectquery='SELECT transform(cultivosgeom."GEOMETRY", ?T) as "GEOMETRY",cultivosgeom.id,cultivosgeom.mapa,cultivosgeom.masa,cultivosgeom.parcela,cultivosgeom.subparce,cultivosgeom.hoja,cultivosgeom.tipo,cultivosgeom.numsymbol,cultivosgeom.fechaalta,cultivosgeom.fechabaja,cultivosgeom.coorx,cultivosgeom.coory,cultivosgeom.id_municipio,cultivosgeom.area FROM cultivosgeom WHERE cultivosgeom.id_municipio IN (?M)' 
where id=10007;
