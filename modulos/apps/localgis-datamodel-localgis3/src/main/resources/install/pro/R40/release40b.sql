CREATE OR REPLACE VIEW v_tramo_emisario AS 
 SELECT eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov AS provincia, eiel_t_saneam_tem.codmunic AS municipio, eiel_t_saneam_tem.tramo_em AS orden_emis, eiel_t_saneam_tem.material AS tipo_mat, eiel_t_saneam_tem.estado, sum(eiel_c_saneam_tem.long_terre) AS long_terre, sum(eiel_c_saneam_tem.long_marit) AS long_marit
   FROM eiel_t_saneam_tem
   left JOIN eiel_c_saneam_tem ON eiel_c_saneam_tem.clave=eiel_t_saneam_tem.clave AND eiel_c_saneam_tem.codprov=eiel_t_saneam_tem.codprov and eiel_c_saneam_tem.codmunic=eiel_t_saneam_tem.codmunic and eiel_c_saneam_tem.tramo_em=eiel_t_saneam_tem.tramo_em
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_t_saneam_tem.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_t_saneam_tem.codmunic::text
  WHERE 
  eiel_t_padron_ttmm.total_poblacion_a1 < 50000
  AND (eiel_t_saneam_tem.codmunic::text || eiel_t_saneam_tem.tramo_em::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text
      FROM v_emisario_nucleo
     ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) 
     AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND 
     eiel_t_saneam_tem.revision_expirada = 9999999999::bigint::numeric AND  eiel_c_saneam_tem.revision_expirada = 9999999999::bigint::numeric
  GROUP BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em, eiel_t_saneam_tem.clave, eiel_t_saneam_tem.codprov, eiel_t_saneam_tem.material, eiel_t_saneam_tem.estado
  ORDER BY eiel_t_saneam_tem.codmunic, eiel_t_saneam_tem.tramo_em;

delete from columns_domains where id_column in (select id from columns where id_table=(select id_table from tables where name ilike 'eiel_indicadores_d_centrosen') and name='estilo2_sld');
delete from columns where id_table=(select id_table from tables where name ilike 'eiel_indicadores_d_centrosen') and name='estilo2_sld';
insert into columns (id,id_table,id_domain,name,"Length","Precision","Scale","Type") values (nextval('seq_columns'),(select id_table from tables where name ilike 'eiel_indicadores_d_centrosen'),(select id from domains where name='eiel_Indicadores_Estilo'),'estilo2_sld',2,NULL,NULL,3);
insert into columns_domains (id_column,id_domain,level) values (currval('seq_columns'),(select id from domains where name='eiel_Indicadores_Estilo'),0);

update attributes set id_column=currval('seq_columns') where id_layer=(select id_layer from layers where name='EIEL_Indicadores_V_CEnsenanza') and id_alias in (select id_vocablo from dictionary where traduccion like '%estilo2_sld%');

  
update query_catalog set query='SELECT relname, attname, atttypmod,format_type(atttypid, atttypmod),attnotnull, typname, adsrc, description, attnum FROM ((((pg_class LEFT JOIN pg_attribute ON ((pg_attribute.attrelid=pg_class.oid))) LEFT JOIN pg_type ON ((atttypid = pg_type.oid))) LEFT JOIN pg_attrdef ON (((attrelid = pg_attrdef.adrelid) AND (attnum = pg_attrdef.adnum))))  LEFT JOIN pg_description ON (((attrelid = pg_description.objoid)   AND (attnum = pg_description.objsubid))))   WHERE attnum>=0 and relname=?   AND   (typname=''varchar'' or typname=''numeric'' or typname=''date'' or typname=''int4'' or typname=''bpchar'' or typname=''timestamp''   or typname=''geometry'' or typname=''bool'' or typname=''bytea'' or typname=''float8'' )    ORDER BY attnum' where id='LMobtenercolumnasBD';

UPDATE QUERIES SET selectquery = 'select transform("eiel_c_ce"."GEOMETRY" , ?T) AS "GEOMETRY",
 "eiel_c_ce"."id", "eiel_c_ce"."id_municipio",eiel_c_nucleo_poblacion.nombre_oficial,
 "eiel_c_ce"."orden_ce", "eiel_t_ce"."nombre", "eiel_t_ce"."distancia", "eiel_t_ce"."acceso",
 "eiel_t_ce"."capilla", "eiel_t_ce"."deposito", "eiel_t_ce"."ampliacion", "eiel_t_ce"."saturacion",
 "eiel_t_ce"."superficie", "eiel_t_ce"."crematorio", "eiel_c_ce"."revision_expirada"
 FROM "eiel_c_ce"
 INNER JOIN "public"."eiel_t_ce" eiel_t_ce ON eiel_c_ce."codprov" = eiel_t_ce."codprov" 
AND eiel_c_ce."clave" = eiel_t_ce."clave" AND eiel_c_ce."codpoblamiento" = eiel_t_ce."codpoblamiento"
 AND eiel_c_ce."orden_ce" = eiel_t_ce."orden_ce" AND eiel_c_ce."codmunic" = eiel_t_ce."codmunic"
 AND eiel_c_ce."codentidad" = eiel_t_ce."codentidad"
 RIGHT JOIN "public"."eiel_c_nucleo_poblacion" eiel_c_nucleo_poblacion ON eiel_c_nucleo_poblacion."codprov" = eiel_t_ce."codprov"
	AND eiel_c_nucleo_poblacion."codpoblamiento" = eiel_t_ce."codpoblamiento"	
			AND eiel_c_nucleo_poblacion."codmunic" = eiel_t_ce."codmunic"	
			AND eiel_c_nucleo_poblacion."codentidad" = eiel_t_ce."codentidad"
 WHERE "eiel_c_ce"."id_municipio" in (?M) AND "eiel_c_ce"."clave"<>''AN'' AND "eiel_t_ce"."revision_expirada" = 9999999999 AND "eiel_c_ce"."revision_expirada" = 9999999999 AND "eiel_c_ce"."GEOMETRY" IS NOT NULL' where id_layer = (SELECT id_layer from layers where name = 'CE_TC');
 
 
 -- Vista alumbrado (Potencias)
 
 CREATE OR REPLACE VIEW v_alumbrado AS 
 SELECT eiel_c_alum_pl.codprov AS provincia, eiel_c_alum_pl.codmunic AS municipio, eiel_c_alum_pl.codentidad AS entidad, eiel_c_alum_pl.codpoblamiento AS nucleo, eiel_c_alum_pl.ah_ener_rfl AS ah_ener_rl, eiel_c_alum_pl.ah_ener_rfi AS ah_ener_ri, eiel_c_alum_pl.estado AS calidad, Round(SUM(eiel_c_alum_pl.potencia::numeric),1)::double precision AS pot_instal, count(*) AS n_puntos
   FROM eiel_c_alum_pl
   LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_alum_pl.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_alum_pl.codmunic::text, ONLY eiel_t_nucl_encuest_1, ONLY eiel_configuracion_shp
  WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_alum_pl.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.codprov::text = eiel_c_alum_pl.codprov::text AND eiel_t_nucl_encuest_1.codmunic::text = eiel_c_alum_pl.codmunic::text AND eiel_t_nucl_encuest_1.codentidad::text = eiel_c_alum_pl.codentidad::text AND eiel_t_nucl_encuest_1.codpoblamiento::text = eiel_c_alum_pl.codpoblamiento::text AND eiel_t_nucl_encuest_1.revision_expirada = 9999999999::bigint::numeric AND eiel_t_nucl_encuest_1.padron::numeric > eiel_configuracion_shp.padron AND eiel_t_nucl_encuest_1.viviendas_total::numeric >= eiel_configuracion_shp.viviendas
  GROUP BY eiel_c_alum_pl.codmunic, eiel_c_alum_pl.codentidad, eiel_c_alum_pl.codpoblamiento, eiel_c_alum_pl.codprov, eiel_c_alum_pl.ah_ener_rfl, eiel_c_alum_pl.ah_ener_rfi, eiel_c_alum_pl.estado
  ORDER BY eiel_c_alum_pl.codmunic, eiel_c_alum_pl.codentidad, eiel_c_alum_pl.codpoblamiento;

ALTER TABLE v_alumbrado OWNER TO geopista;
GRANT ALL ON TABLE v_alumbrado TO geopista;
GRANT SELECT ON TABLE v_alumbrado TO consultas;

-- Para arreglar que en la tabla de aliviaderos venga el atributo nucleo
update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'codpoblamiento') where id_layer=(select id_layer from layers where name='ALIVIADERO_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%nucleo%');

-- Para arreglar que en la tabla de aliviaderos venga el atributo nucleo
update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'codpoblamiento') where id_layer=(select id_layer from layers where name='ALIVIADERO_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%nucleo%');

 
 -- tramo_conduccion.shp y tramo_conduccion_m50.shp tienen que contener los atributos: tipo_tcond; estado; titular; gestion
 
update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'material') where id_layer=(select id_layer from layers where name='TRAMO_CONDUCCION_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%tipo_tcond%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'estado') where id_layer=(select id_layer from layers where name='TRAMO_CONDUCCION_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%estado%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'titular') where id_layer=(select id_layer from layers where name='TRAMO_CONDUCCION_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%titular%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'gestor') where id_layer=(select id_layer from layers where name='TRAMO_CONDUCCION_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%gestion%');

--Tramos conduccion
update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'material') where id_layer=(select id_layer from layers where name='TRAMO_CONDUCCION_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%tipo_tcond%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'estado') where id_layer=(select id_layer from layers where name='TRAMO_CONDUCCION_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%estado%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'titular') where id_layer=(select id_layer from layers where name='TRAMO_CONDUCCION_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%titular%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_abast_tcn' and columns.name = 'gestor') where id_layer=(select id_layer from layers where name='TRAMO_CONDUCCION_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%gestion%');
 
--Tramo Colector
update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'tipo_red_interior') where id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%tipo_colec%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_t_saneam_tcl' and columns.name = 'tipo_red_interior') where id_layer=(select id_layer from layers where name='TRAMO_COLECTOR_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%tipo_colec%');

--Para que puntos de vertido no salga vacio
--Punto de Vertido encuestado y Punto de vertido encuestado M50



UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_pv"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_saneam_pv.id, eiel_c_saneam_pv.id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_pv.clave, eiel_c_saneam_pv.codprov, eiel_c_saneam_pv.codmunic, eiel_c_saneam_pv.orden_pv FROM eiel_configuracion_shp,"eiel_c_saneam_pv" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_pv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_pv.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 >= 50000 AND (eiel_c_saneam_pv.codmunic::text || eiel_c_saneam_pv.orden_pv::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text FROM v_emisario_nucleo ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_pv.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'''where id_layer = (SELECT id_layer from layers where name = 'EMISARIO_ENC_M50_SHP');


UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_c_saneam_pv"."GEOMETRY" , ?T) as "GEOMETRY", eiel_c_saneam_pv.id, eiel_c_saneam_pv.id_municipio, eiel_configuracion_shp.fase, eiel_c_saneam_pv.clave, eiel_c_saneam_pv.codprov, eiel_c_saneam_pv.codmunic, eiel_c_saneam_pv.orden_pv FROM eiel_configuracion_shp,"eiel_c_saneam_pv" LEFT JOIN eiel_t_padron_ttmm ON eiel_t_padron_ttmm.codprov::text = eiel_c_saneam_pv.codprov::text AND eiel_t_padron_ttmm.codmunic::text = eiel_c_saneam_pv.codmunic::text WHERE eiel_t_padron_ttmm.total_poblacion_a1 < 50000 AND (eiel_c_saneam_pv.codmunic::text || eiel_c_saneam_pv.orden_pv::text IN ( SELECT DISTINCT v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text FROM v_emisario_nucleo ORDER BY v_emisario_nucleo.em_municip::text || v_emisario_nucleo.orden_emis::text)) AND eiel_t_padron_ttmm.revision_expirada = 9999999999::bigint::numeric AND eiel_c_saneam_pv.revision_expirada = 9999999999::bigint::numeric  AND id_municipio IN (?M)  and "GEOMETRY" is not null AND astext("GEOMETRY") != ''GEOMETRYCOLLECTION EMPTY'''where id_layer = (SELECT id_layer from layers where name = 'EMISARIO_ENC_SHP');


--EMISARIO_ENC_SHP

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'id') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%id%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'id_municipio') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%id_municipio%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'GEOMETRY') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%GEOMETRY%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%fase%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'clave') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%clave%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'codprov') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%prov%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'codmunic') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%mun%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'orden_pv') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%orden_emis%');


--EMISARIO_ENC_M50_SHP

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'id') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%id%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'id_municipio') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%id_municipio%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'GEOMETRY') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%GEOMETRY%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_configuracion_shp' and columns.name = 'fase') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%fase%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'clave') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%clave%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'codprov') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%prov%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'codmunic') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%mun%');

update attributes set id_column=(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_pv' and columns.name = 'orden_pv') where id_layer=(select id_layer from layers where name='EMISARIO_ENC_M50_SHP') and id_alias in (select id_vocablo from dictionary where traduccion like '%orden_emis%');

--Incidencia constraint tabla eiel_t_en

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	ALTER TABLE eiel_t_en DROP CONSTRAINT titularchk ;	
	exception when others then
       RAISE NOTICE 'La constrainsts titularchk no existia al borrarla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "DROP CONSTRAINT titularchk";


 
ALTER TABLE eiel_t_en ADD CONSTRAINT  titularchk CHECK (titular::text = 'CE'::text OR titular::text = 'CL'::text OR titular::text = 'OT'::text OR titular::text = 'PR'::text OR titular::text = 'UP'::text OR titular::text = 'UV'::text);
 
-- Para actualizar los numeros de orden de la tabla planeamiento y que no haya colisiones
DROP SEQUENCE IF EXISTS seq_eiel_t_planeam_urban;
-- cuando se insertan nuevos planeamientos
CREATE SEQUENCE seq_eiel_t_planeam_urban
  INCREMENT 1
  MINVALUE 0
  START 10
  CACHE 1;
ALTER TABLE seq_eiel_t_planeam_urban  OWNER TO geopista;

update eiel_t_planeam_urban set orden_plan=nextval('seq_eiel_t_planeam_urban') where orden_plan='01';

DROP SEQUENCE seq_eiel_t_planeam_urban;


update query_catalog set query='select distinct id_habitante, h.nombre, part_apell1, apellido1, part_apell2, apellido2, dni, letradni  from habitantes h, domicilio d, numeros_policia p where h.id_domicilio = d.id_domicilio and d.id_portal = p.id and p.id=154715 order by apellido1, part_apell1, apellido2, part_apell2, nombre, dni,letradni' where id='padronHabitantesPoliciaOrdenapellidos';


delete from version where id_version = 'MODELO R41';
insert into version (id_version, fecha_version) values('MODELO R41', DATE '2014-04-14');





