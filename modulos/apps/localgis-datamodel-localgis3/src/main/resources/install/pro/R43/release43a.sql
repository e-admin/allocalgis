update queries set selectquery='SELECT Parcelas.id, Parcelas.referencia_catastral, Parcelas.id_municipio, Parcelas.primer_numero, Parcelas.primera_letra, Parcelas.segundo_numero, Parcelas.segunda_letra, Parcelas.kilometro, Parcelas.bloque,Parcelas.direccion_no_estructurada, Parcelas.codigo_postal, Parcelas.fecha_alta, Parcelas.fecha_baja, transform(Parcelas."GEOMETRY", ?T) as "GEOMETRY", Parcelas.codigodelegacionmeh, Parcelas.nombreentidadmenor, Parcelas.id_distrito, Parcelas.codigozonaconcentracion, Parcelas.codigopoligono, Parcelas.codigoparcela, Parcelas.nombreparaje, Parcelas.id_via, Parcelas.area, Parcelas.length, Parcelas.anno_expediente, Parcelas.referencia_expediente, Parcelas.codigo_entidad_colaboradora, Parcelas.tipo_movimiento, Parcelas.codigo_municipiodgc, Parcelas.bice, Parcelas.codigo_provinciaine, Parcelas.codigo_municipio_localizaciondgc, Parcelas.codigo_municipio_localizacionine, Parcelas.codigo_municipio_origendgc, Parcelas.codigo_paraje, Parcelas.superficie_finca, Parcelas.superficie_construida_total, Parcelas.superficie_const_sobrerasante, Parcelas.superficie_const_bajorasante, Parcelas.superficie_cubierta, Parcelas.anio_aprobacion, Parcelas.codigo_calculo_valor, Parcelas.poligono_catastral_valoracion, Parcelas.modalidad_reparto, Parcelas.indicador_infraedificabilidad, Parcelas.movimiento_baja, Parcelas.coordenada_x, Parcelas.coordenada_y, Parcelas.modificado, Municipios.NombreOficial, count(inventario_feature.id_bien) as id2 FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) LEFT JOIN inventario_feature ON (parcelas.id=inventario_feature.id_feature and inventario_feature.id_layer=(select id_layer from layers where name = ''inventario_parcelas'') and inventario_feature.revision_expirada=9999999999) WHERE Municipios.ID in (?M) AND Parcelas.Fecha_baja IS NULL GROUP BY Parcelas.id, Parcelas.anio_aprobacion, Parcelas.anno_expediente, Parcelas.area, Parcelas.bice, Parcelas.bloque, Parcelas.codigodelegacionmeh, Parcelas.codigoparcela, Parcelas.codigopoligono, Parcelas.codigozonaconcentracion, Parcelas.codigo_calculo_valor, Parcelas.codigo_entidad_colaboradora, Parcelas.codigo_municipiodgc, Parcelas.codigo_municipio_localizaciondgc, Parcelas.codigo_municipio_localizacionine, Parcelas.codigo_municipio_origendgc, Parcelas.codigo_paraje, Parcelas.codigo_postal, Parcelas.codigo_provinciaine, Parcelas.coordenada_x, Parcelas.coordenada_y, Parcelas.direccion_no_estructurada, Parcelas.fecha_alta, Parcelas.fecha_baja, Parcelas."GEOMETRY", Parcelas.id_distrito, Parcelas.id_municipio, Parcelas.id_via, Parcelas.indicador_infraedificabilidad, Parcelas.kilometro, Parcelas.length, Parcelas.modalidad_reparto, Parcelas.movimiento_baja, Parcelas.nombreentidadmenor, Parcelas.nombreparaje, Parcelas.poligono_catastral_valoracion, Parcelas.primera_letra, Parcelas.primer_numero, Parcelas.referencia_catastral, Parcelas.referencia_expediente, Parcelas.segunda_letra, Parcelas.segundo_numero,Parcelas.superficie_construida_total, Parcelas.superficie_const_bajorasante, Parcelas.superficie_const_sobrerasante, Parcelas.superficie_cubierta, Parcelas.superficie_finca, Parcelas.tipo_movimiento, Parcelas.modificado,Municipios.NombreOficial' where id_layer = (SELECT id_layer from layers where name = 'inventario_parcelas');

--Actualizamos la tabla eiel_c_provincia para que no aparezca como expirada ya que sino no sale en los informes de MPT
update eiel_c_provincia set revision_expirada=9999999999 where id=33 and revision_actual=revision_expirada;


CREATE OR REPLACE VIEW v_entidades_agrupadas AS 
 select eiel_t_entidades_agrupadas.*,denominacion,codprov from eiel_t_entidades_agrupadas inner join eiel_t_entidad_singular on eiel_t_entidades_agrupadas.codmunicipio=eiel_t_entidad_singular.codmunic and eiel_t_entidades_agrupadas.codentidad=eiel_t_entidad_singular.codentidad where (eiel_t_entidades_agrupadas.codmunicipio,eiel_t_entidades_agrupadas.codentidad) not in (select codmunic,codentidad from eiel_t_nucl_encuest_1)
 order by codmunicipio;
 
DROP VIEW v_nucleo_poblacion;
CREATE OR REPLACE VIEW v_nucleo_poblacion AS 
SELECT eiel_c_nucleo_poblacion.codprov AS provincia, eiel_c_nucleo_poblacion.codmunic AS municipio, eiel_c_nucleo_poblacion.codentidad AS entidad, eiel_c_nucleo_poblacion.codpoblamiento AS poblamient, eiel_c_nucleo_poblacion.nombre_oficial AS denominaci
   FROM eiel_c_nucleo_poblacion
  WHERE eiel_c_nucleo_poblacion.revision_expirada = 9999999999::bigint::numeric and eiel_c_nucleo_poblacion.codmunic in (select codmunic from eiel_t_padron_ttmm where  revision_expirada=9999999999  and total_poblacion_a1<50000)
union select v_entidades_agrupadas.codprov as provincia,v_entidades_agrupadas.codmunicipio as codmunic,v_entidades_agrupadas.codentidad as entidad,v_entidades_agrupadas.codnucleo as codpoblamiento,v_entidades_agrupadas.denominacion
 from v_entidades_agrupadas where v_entidades_agrupadas.codmunicipio in (select codmunic from eiel_t_padron_ttmm where  revision_expirada=9999999999  and total_poblacion_a1<50000);


CREATE OR REPLACE FUNCTION createindex() RETURNS VOID AS
$Q$
BEGIN
	CREATE INDEX seguros_inventario_id  ON seguros_inventario  USING btree  (id );		  
	exception when others then
		RAISE NOTICE 'El indice seguros_inventario_id ya existia';
END;
$Q$
LANGUAGE plpgsql;
select createindex() as "CREATE INDEX seguros_inventario_id";



 delete from version where id_version = 'MODELO R43';
insert into version (id_version, fecha_version) values('MODELO R43', DATE '2014-06-20');





