SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;


--Corrección error parcelas rusticas y urbanas


UPDATE queries SET selectquery='SELECT Parcelas.id, Parcelas.referencia_catastral, Parcelas.id_municipio, Parcelas.primer_numero, Parcelas.primera_letra, Parcelas.segundo_numero, Parcelas.segunda_letra, Parcelas.kilometro, Parcelas.bloque, Parcelas.direccion_no_estructurada, Parcelas.codigo_postal, Parcelas.fecha_alta, Parcelas.fecha_baja, transform(Parcelas."GEOMETRY", ?T) as "GEOMETRY", Parcelas.codigodelegacionmeh, Parcelas.nombreentidadmenor, Parcelas.id_distrito, Parcelas.codigozonaconcentracion, Parcelas.codigopoligono, Parcelas.codigoparcela, Parcelas.nombreparaje, Parcelas.id_via, Parcelas.area, Parcelas.length, Parcelas.anno_expediente, Parcelas.referencia_expediente, Parcelas.codigo_entidad_colaboradora, Parcelas.tipo_movimiento, Parcelas.codigo_municipiodgc, Parcelas.bice, Parcelas.codigo_provinciaine, Parcelas.codigo_municipio_localizaciondgc, Parcelas.codigo_municipio_localizacionine, Parcelas.codigo_municipio_origendgc, Parcelas.codigo_paraje, Parcelas.superficie_finca, Parcelas.superficie_construida_total, Parcelas.superficie_const_sobrerasante, Parcelas.superficie_const_bajorasante, Parcelas.superficie_cubierta, Parcelas.anio_aprobacion, Parcelas.codigo_calculo_valor, Parcelas.poligono_catastral_valoracion, Parcelas.modalidad_reparto, Parcelas.indicador_infraedificabilidad, Parcelas.movimiento_baja, Parcelas.coordenada_x, Parcelas.coordenada_y, Parcelas.modificado, Municipios.NombreOficial FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) WHERE Municipios.ID in (?M) AND Fecha_baja IS NULL AND SUBSTRING(referencia_catastral,0,6)<>(Municipios.ID::varchar)' WHERE id_layer=104;


UPDATE queries SET selectquery='SELECT Parcelas.id, Parcelas.referencia_catastral, Parcelas.id_municipio, Parcelas.primer_numero, Parcelas.primera_letra, Parcelas.segundo_numero, Parcelas.segunda_letra, Parcelas.kilometro, Parcelas.bloque, Parcelas.direccion_no_estructurada, Parcelas.codigo_postal, Parcelas.fecha_alta, Parcelas.fecha_baja, transform(Parcelas."GEOMETRY", ?T) as "GEOMETRY", Parcelas.codigodelegacionmeh, Parcelas.nombreentidadmenor, Parcelas.id_distrito, Parcelas.codigozonaconcentracion, Parcelas.codigopoligono, Parcelas.codigoparcela, Parcelas.nombreparaje, Parcelas.id_via, Parcelas.area, Parcelas.length, Parcelas.anno_expediente, Parcelas.referencia_expediente, Parcelas.codigo_entidad_colaboradora, Parcelas.tipo_movimiento, Parcelas.codigo_municipiodgc, Parcelas.bice, Parcelas.codigo_provinciaine, Parcelas.codigo_municipio_localizaciondgc, Parcelas.codigo_municipio_localizacionine, Parcelas.codigo_municipio_origendgc, Parcelas.codigo_paraje, Parcelas.superficie_finca, Parcelas.superficie_construida_total, Parcelas.superficie_const_sobrerasante, Parcelas.superficie_const_bajorasante, Parcelas.superficie_cubierta, Parcelas.anio_aprobacion, Parcelas.codigo_calculo_valor, Parcelas.poligono_catastral_valoracion, Parcelas.modalidad_reparto, Parcelas.indicador_infraedificabilidad, Parcelas.movimiento_baja, Parcelas.coordenada_x, Parcelas.coordenada_y, Parcelas.modificado, Municipios.NombreOficial FROM Municipios INNER JOIN Parcelas ON (Municipios.ID=Parcelas.ID_Municipio) WHERE Municipios.ID in (?M) AND Fecha_baja IS NULL AND SUBSTRING(referencia_catastral,0,6)=(Municipios.ID::varchar)' WHERE id_layer=105;

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
		CREATE OR REPLACE FUNCTION perimeter(geometry)
		  RETURNS double precision AS
		'$libdir/postgis-2.0', 'LWGEOM_perimeter2d_poly'
		  LANGUAGE c IMMUTABLE STRICT
		  COST 1;
		ALTER FUNCTION perimeter(geometry)
		  OWNER TO postgres;
		  
exception when others then
       RAISE NOTICE 'Excepcion al crear la funcion perimeter. Ya estaria creada';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PERIMETER";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
		ALTER DATABASE geopista SET bytea_output TO 'escape';
		  
exception when others then
       RAISE NOTICE 'Excepcion al alterar la codificacion de la base de datos. No es necesario';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PERIMETER";

 


-- Para permitir URL's en el nombre
ALTER TABLE documento ALTER COLUMN nombre TYPE character varying(1000);





CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN

	CREATE OR REPLACE FUNCTION update_srid() RETURNS trigger AS $update_srid$ BEGIN
	   UPDATE sigm_vados SET "GEOMETRY"=ST_setSRID("GEOMETRY",4258) WHERE ST_SRID("GEOMETRY")!=4258;
		RETURN NEW;
	END;
	$update_srid$
	LANGUAGE plpgsql;

		--DROP TRIGGER update_srid_trigger;
	create trigger update_srid_trigger
	AFTER INSERT ON sigm_vados FOR EACH ROW	EXECUTE PROCEDURE update_srid();
		  
exception when others then
       RAISE NOTICE 'Excepcion al crear el trigger de vados';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "Trigger Vados";






