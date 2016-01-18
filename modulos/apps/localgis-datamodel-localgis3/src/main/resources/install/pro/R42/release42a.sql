DROP VIEW v_emisario_enc_sin_nucleo;
CREATE OR REPLACE VIEW v_emisario_enc_sin_nucleo AS 
 SELECT DISTINCT ON (eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em) eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis
   FROM eiel_t_saneam_tem
    LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text NOT IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
   FROM v_emisario_nucleo
  ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em
  ORDER BY eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

ALTER TABLE v_emisario_enc_sin_nucleo  OWNER TO geopista;
GRANT ALL ON TABLE v_emisario_enc_sin_nucleo TO geopista;
GRANT SELECT ON TABLE v_emisario_enc_sin_nucleo TO consultas;


delete from lcg_nodos_capas_campos where clave_capa='PV' and clave_grupo='Info.PV' and campo_bd='estado';
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Info.PV','estado','localgiseiel.panels.label.estado_conserv',4,'eiel_Estado de conservación','Estado'); 	

delete from lcg_nodos_capas_campos where clave_capa='TC' and clave_grupo='Info.TC' and campo_bd='estado';
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','estado','localgiseiel.panels.label.estado_conserv',4,'eiel_Estado de conservación','Estado');	

delete from lcg_nodos_capas_campos where clave_capa='IV' and clave_grupo='Info.PV' and campo_bd='estado';	
delete from lcg_nodos_capas_campos where clave_capa='IV' and clave_grupo='Info.IV' and campo_bd='estado';	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IV','Info.IV','estado','localgiseiel.panels.label.estado_conserv',4,'eiel_Estado de conservación','Estado');	
		
update map_server_layers set srs='EPSG:25828' where srs='EPSG:23028';
update map_server_layers set srs='EPSG:25829' where srs='EPSG:23029';
update map_server_layers set srs='EPSG:25830' where srs='EPSG:23030';
update map_server_layers set srs='EPSG:25831' where srs='EPSG:23031';
update map_server_layers set srs='EPSG:4258' where srs='EPSG:4230';	

-- Para que no salgan tablas repetidas en el listado de tablas del gestor de capas.
update query_catalog set query='select  distinct on (pgt.tablename) pgt.tablename,t.* from pg_tables pgt left join tables t on pgt.tablename=t.name where schemaname=''public'' order by tablename' where id='LMobtenertablasBD';

update query_catalog set query='select distinct on (tables.name) tables.* from tables order by tables.name' where id='LMobtenertablas';

update query_catalog set query='select id, nombreoficial from provincias order by nombreoficial' where id='MCobtenerProvincias';

delete from version where id_version = 'MODELO R42';
insert into version (id_version, fecha_version) values('MODELO R42', DATE '2014-06-20');





