
delete from lcg_nodos_capas_campos where clave_capa='ALUM' and campo_bd='ah_ener_rfi';
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Info.ALUM','ah_ener_rfi','localgiseiel.tabla.luminaria.columna.reductor_flujo_inicio',4,'eiel_Reductor de flujo en la instalación','reductor_flujo_inicio');	

delete from lcg_nodos_capas_campos where clave_capa='ALUM' and campo_bd='ah_ener_rfl';
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Info.ALUM','ah_ener_rfl','localgiseiel.tabla.luminaria.columna.reductor_flujo',4,'eiel_Reductor de flujo en la luminaria','reductor_flujo');	

	
	
delete from lcg_nodos_capas where clave='CMP';
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer)
	values ('CMP','ALUM','CMP','eiel.CMP','eiel_c_alum_cmp','filtro_cmp','com.geopista.app.eiel.beans.CuadroMandoEIEL','CMP');	

delete from lcg_nodos_capas where clave='EEA';
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer)
	values ('EEA','ALUM','EEA','eiel.EEA','eiel_c_alum_eea','filtro_eea','com.geopista.app.eiel.beans.EstabilizadorEIEL','EEA');	
	
-- Para que en el informe de tramos de conduccion el campo gestor salga correctamente.
update lcg_nodos_capas_campos set dominio='eiel_Gestión' where clave_capa='CN' and campo_bd='gestor';


update lcg_nodos_capas_campos set dominio='eiel_uso_anterior' where clave_capa='SU' and campo_bd='uso_anterior';


	
CREATE OR REPLACE FUNCTION createindex() RETURNS VOID AS
$Q$
BEGIN
	CREATE INDEX indx_columns_id_table  ON columns  USING btree (id_table);		  
	exception when others then
		RAISE NOTICE 'El indice indx_columns_id_table ya existia';
END;
$Q$
LANGUAGE plpgsql;
select createindex() as "CREATE INDEX indx_columns_id_table";


CREATE OR REPLACE FUNCTION createindex() RETURNS VOID AS
$Q$
BEGIN
	CREATE INDEX indx_attributes_id_column  ON attributes  USING btree (id_column);	  
	exception when others then
		RAISE NOTICE 'El indiceindx_attributes_id_column ya existia';
END;
$Q$
LANGUAGE plpgsql;
select createindex() as "CREATE INDEX indx_attributes_id_column";


CREATE OR REPLACE FUNCTION createindex() RETURNS VOID AS
$Q$
BEGIN
	CREATE INDEX indx_attributes_id_layer  ON attributes  USING btree (id_layer);	  
	exception when others then
		RAISE NOTICE 'El indx_attributes_id_layer ya existia';
END;
$Q$
LANGUAGE plpgsql;
select createindex() as "CREATE INDEX indx_attributes_id_layer";



update queries set selectquery='SELECT transform("eiel_c_saneam_tcl"."GEOMETRY" , ?T) as "GEOMETRY", "eiel_c_saneam_tcl".id, "eiel_c_saneam_tcl".id_municipio, eiel_configuracion_shp.fase, "eiel_c_saneam_tcl".clave, "eiel_c_saneam_tcl".codprov, "eiel_c_saneam_tcl".codmunic, "eiel_c_saneam_tcl".tramo_cl, material, sist_impulsion, estado, titular, gestor FROM eiel_configuracion_shp,"eiel_c_saneam_tcl", eiel_t_saneam_tcl LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tcl.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text FROM v_colector_nucleo  ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric and eiel_t_saneam_tcl.clave::text = eiel_c_saneam_tcl.clave::text AND eiel_t_saneam_tcl.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_saneam_tcl.codmunic::text = eiel_c_saneam_tcl.codmunic::text AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_c_saneam_tcl.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer=(select id_layer from layers where name ilike '%TRAMO_COLECTOR_SHP%');


update queries set selectquery='SELECT transform("eiel_c_saneam_tcl"."GEOMETRY" , ?T) as "GEOMETRY", "eiel_c_saneam_tcl".id, "eiel_c_saneam_tcl".id_municipio, eiel_configuracion_shp.fase, "eiel_c_saneam_tcl".clave, "eiel_c_saneam_tcl".codprov, "eiel_c_saneam_tcl".codmunic, "eiel_c_saneam_tcl".tramo_cl, material, sist_impulsion, estado, titular, gestor FROM eiel_configuracion_shp,"eiel_c_saneam_tcl", eiel_t_saneam_tcl LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tcl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tcl.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_t_saneam_tcl.codmunic::text || eiel_t_saneam_tcl.tramo_cl::text IN ( SELECT DISTINCT v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text FROM v_colector_nucleo  ORDER BY v_colector_nucleo.c_municip::text || v_colector_nucleo.orden_cole::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_t_saneam_tcl.revision_expirada = 9999999999::bigint::numeric and eiel_t_saneam_tcl.clave::text = eiel_c_saneam_tcl.clave::text AND eiel_t_saneam_tcl.codprov::text = eiel_c_saneam_tcl.codprov::text AND eiel_t_saneam_tcl.codmunic::text = eiel_c_saneam_tcl.codmunic::text AND eiel_t_saneam_tcl.tramo_cl::text = eiel_c_saneam_tcl.tramo_cl::text AND eiel_c_saneam_tcl.revision_expirada = 9999999999::bigint::numeric AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY''' where id_layer=(select id_layer from layers where name ilike '%TRAMO_COLECTOR_M50_SHP%');


update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'material') where id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%tipo_colec%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'material') where id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%tipo_colec%');

DROP VIEW v_nucleo_poblacion;
CREATE OR REPLACE VIEW v_nucleo_poblacion AS 
SELECT eiel_c_nucleo_poblacion.codprov AS provincia, eiel_c_nucleo_poblacion.codmunic AS municipio, eiel_c_nucleo_poblacion.codentidad AS entidad, eiel_c_nucleo_poblacion.codpoblamiento AS poblamient, eiel_c_nucleo_poblacion.nombre_oficial AS denominaci
           FROM eiel_c_nucleo_poblacion
          WHERE eiel_c_nucleo_poblacion.revision_expirada = 9999999999::bigint::numeric AND (eiel_c_nucleo_poblacion.codmunic::text IN ( SELECT eiel_t_padron_ttmm.codmunic
                   FROM eiel_t_padron_ttmm
                  WHERE eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.total_poblacion_a1 < 50000)) and
        eiel_c_nucleo_poblacion.codentidad not in (select codentidad from eiel_t_entidades_agrupadas where revision_expirada = 9999999999::bigint::numeric and codmunicipio=eiel_c_nucleo_poblacion.codmunic and codentidad=eiel_c_nucleo_poblacion.codentidad and codnucleo=eiel_c_nucleo_poblacion.codpoblamiento and codentidad_agrupada!=eiel_c_nucleo_poblacion.codentidad)             
UNION 
         SELECT v_entidades_agrupadas.codprov AS provincia, v_entidades_agrupadas.codmunicipio AS municipio, v_entidades_agrupadas.codentidad AS entidad, v_entidades_agrupadas.codnucleo AS poblamient, v_entidades_agrupadas.denominacion AS denominaci
           FROM v_entidades_agrupadas
          WHERE (v_entidades_agrupadas.codmunicipio::text IN ( SELECT eiel_t_padron_ttmm.codmunic
                   FROM eiel_t_padron_ttmm
                  WHERE eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_padron_ttmm.total_poblacion_a1 < 50000)) order by municipio,entidad,poblamient;

delete from version where id_version = 'MODELO R44';
insert into version (id_version, fecha_version) values('MODELO R44', DATE '2014-12-20');





