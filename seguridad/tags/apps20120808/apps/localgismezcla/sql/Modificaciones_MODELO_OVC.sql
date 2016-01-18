ALTER TABLE configuracion ADD modo_generacion character varying(1);


-- Se añade campo Modod_Acoplado (Boolean) para indicar que el expediente fue
-- generado en modo acoplado y debe sincronizarse con la DGC
ALTER TABLE expediente ADD modo_acoplado BOOLEAN DEFAULT FALSE;

-- Se modifca el tamaño del camo id_dialogo de la tabla expediente_finca_catastro
ALTER TABLE expediente_finca_catastro DROP id_dialogo;
ALTER TABLE expediente_finca_catastro ADD id_dialogo character varying(64);

-- Se añade un nuevo campo en la tabla expediente_finca_catastro para controlar que la finca no esta actualizada
ALTER TABLE expediente_finca_catastro ADD COLUMN actualizado boolean DEFAULT false;

-- Se modifca el tamaño del camo id_dialogo de la tabla expediente_bieninmueble
ALTER TABLE expediente_bieninmueble DROP id_dialogo;
ALTER TABLE expediente_bieninmueble ADD id_dialogo character varying(64);

-- Se añade un nuevo campo en la tabla expediente_bieninmueble para controlar que el bien no esta actualzado
ALTER TABLE expediente_bieninmueble ADD COLUMN actualizado boolean DEFAULT false;


-- Modificacion de los campos valor_catastral, valor_catastral_suelo, valor_catastral_construccion
-- para aceptar valores con decimales.
ALTER TABLE bien_inmueble ALTER COLUMN valor_catastral TYPE NUMERIC(12,2);
ALTER TABLE bien_inmueble ALTER COLUMN valor_catastral_suelo TYPE NUMERIC(12,2);
ALTER TABLE bien_inmueble ALTER COLUMN valor_catastral_construccion TYPE NUMERIC(12,2);


update query_catalog set query='select vias.id, vias.tipovianormalizadocatastro,vias.nombrecatastro,vias.codigocatastro, municipios.nombreoficial, vias.codigoine, vias.id_municipio from vias left join municipios on vias.id_municipio=municipios.id where upper(nombrecatastro) like upper(?) and tipovianormalizadocatastro=? AND vias.id_municipio = ?' where id='MCobtenerViasPorTipoCatastro';
update query_catalog set query='select vias.id, vias.tipovianormalizadocatastro, vias.nombrecatastro, vias.codigocatastro, municipios.nombreoficial, vias.codigoine, vias.id_municipio from vias left join municipios on vias.id_municipio=municipios.id where upper(nombrecatastro) like upper(?) AND vias.id_municipio = ?' where id='MCobtenerViasCatastro';

update query_catalog set query='select tipo_ponencia,anno_normas,anno_efectostotal,propiedad_vertical1,antiguedad_infraedificada,estado_ponencia,finca_infraedificada,anno_cuadromarco,aplicacion_formula,suelo_sindesarrollar,ruinosa,fecha_cierrerevision from cntponurb where anno_aprobacion=?' where id='MCgetDatosPonencia';
