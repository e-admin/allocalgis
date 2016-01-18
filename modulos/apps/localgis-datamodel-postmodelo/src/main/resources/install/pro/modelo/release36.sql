create or replace function f_add_col (t_name regclass,c_name text, sql text) returns void AS $$
begin
    IF EXISTS (
		SELECT 1 FROM pg_attribute
			WHERE  attrelid = t_name
			AND    attname = c_name
			AND    NOT attisdropped) THEN
			RAISE NOTICE 'Column % already exists in %', c_name, t_name;
	ELSE
		EXECUTE sql;
	END IF;
end;
$$ language 'plpgsql';

select f_add_col('public.eiel_indicadores_d_centrosen','eso_ncen', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN eso_ncen numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_centrosen','eso_plazas', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN eso_plazas numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_centrosen','eso_nalumnos', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN eso_nalumnos numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_centrosen','especial_ncen', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN especial_ncen numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_centrosen','especial_plazas', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN especial_plazas numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_centrosen','especial_nalumnos', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN especial_nalumnos numeric(6,0);');

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	-- Asumimos que si la primera columna no esta creada, el resto tampoco
	IF NOT EXISTS (SELECT * FROM COLUMNS WHERE name = 'eso_ncen') THEN	
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'eso_ncen',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','eso_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]eso_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]eso_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]eso_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]eso_ncen');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'eso_plazas',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','eso_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]eso_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]eso_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]eso_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]eso_plazas');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'eso_nalumnos',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','eso_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]eso_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]eso_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]eso_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]eso_nalumnos');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'especial_ncen',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','especial_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]especial_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]especial_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]especial_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]especial_ncen');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'especial_plazas',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','especial_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]especial_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]especial_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]especial_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]especial_plazas');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'especial_nalumnos',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','especial_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]especial_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]especial_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]especial_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]especial_nalumnos');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";




UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_indicadores_d_centrosen"."GEOMETRY", ?T) AS "GEOMETRY", CASE WHEN ((primaria_nalumnos >= 26) AND (primaria_nalumnos < 86)) THEN ''1'' WHEN ((primaria_nalumnos >= 86) AND (primaria_nalumnos < 214)) THEN ''2'' WHEN ((primaria_nalumnos >= 214) AND (primaria_nalumnos < 446)) THEN ''3'' WHEN ((primaria_nalumnos >= 446) AND (primaria_nalumnos < 725)) THEN ''4'' END AS estilo_sld, CASE WHEN (guarderia_sup_alibre_m > 0) OR (guarderia_sup_cubierta_m > 0) THEN ''1'' ELSE ''2'' END AS estilo2_sld, "eiel_indicadores_d_centrosen"."id","eiel_indicadores_d_centrosen"."toponimo","eiel_indicadores_d_centrosen"."id_municipio","eiel_indicadores_d_centrosen"."codprov","eiel_indicadores_d_centrosen"."codmunic","eiel_indicadores_d_centrosen"."codentidad","eiel_indicadores_d_centrosen"."codpoblamiento","eiel_indicadores_d_centrosen"."einfantil_ncen","eiel_indicadores_d_centrosen"."einfantil_nalumnos","eiel_indicadores_d_centrosen"."einfantil_plazas","eiel_indicadores_d_centrosen"."eso_ncen","eiel_indicadores_d_centrosen"."eso_nalumnos","eiel_indicadores_d_centrosen"."eso_plazas","eiel_indicadores_d_centrosen"."especial_ncen","eiel_indicadores_d_centrosen"."especial_nalumnos","eiel_indicadores_d_centrosen"."especial_plazas","eiel_indicadores_d_centrosen"."primaria_ncen","eiel_indicadores_d_centrosen"."primaria_nalumnos","eiel_indicadores_d_centrosen"."primaria_plazas","eiel_indicadores_d_centrosen"."fp_ncen","eiel_indicadores_d_centrosen"."fp_nalumnos","eiel_indicadores_d_centrosen"."fp_plazas","eiel_indicadores_d_centrosen"."secundaria_ncen","eiel_indicadores_d_centrosen"."secundaria_nalumnos","eiel_indicadores_d_centrosen"."secundaria_plazas","eiel_indicadores_d_centrosen"."guarderia_ncen","eiel_indicadores_d_centrosen"."guarderia_capacidad","eiel_indicadores_d_centrosen"."guarderia_sup_alibre_b","eiel_indicadores_d_centrosen"."guarderia_sup_alibre_r","eiel_indicadores_d_centrosen"."guarderia_sup_alibre_m","eiel_indicadores_d_centrosen"."guarderia_sup_cubierta_b","eiel_indicadores_d_centrosen"."guarderia_sup_cubierta_r","eiel_indicadores_d_centrosen"."guarderia_sup_cubierta_m" FROM "eiel_indicadores_d_centrosen" WHERE "eiel_indicadores_d_centrosen"."id_municipio" IN (?M)' where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza' LIMIT 1);


set client_encoding to 'utf8';

-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- VERTEDEROS (eiel_c_vt - eiel_t_vt)
delete from lcg_nodos_capas_campos where clave_capa='ALUM';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Ident.ALUM','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ALUM','Ident.ALUM','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ALUM','Ident.ALUM','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Ident.ALUM','orden_vt','localgiseiel.panels.label.orden',0,null,'CodOrden');	
		

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Info.ALUM','ah_ener_rfi','localgiseiel.tabla.luminaria.columna.reductor_flujo_inicio',4,'eiel_Reductor de flujo en la instalación','reductor_flujo_inicio');	

	insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Info.ALUM','ah_ener_rfl','localgiseiel.tabla.luminaria.columna.reductor_flujo',4,'eiel_Reductor de flujo en la luminaria','reductor_flujo');	

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Info.ALUM','tipo_luminaria','localgiseiel.tabla.luminaria.columna.tipo_vt',4,'eiel_tipo_luminaria','tipo_luminaria');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('ALUM','Esta.ALUM','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
	--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('ALUM','Info.ALUM','eiel_c_alum_pl.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('ALUM','Info.ALUM','eiel_c_alum_pl.codpoblamiento',null,-2,null,null,false,false); 


-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.ALUM';
insert into lcg_nodos_capas_traducciones values ('eiel.ALUM','Alumbrado','es_ES');










-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- VERTEDEROS (eiel_c_vt - eiel_t_vt)
delete from lcg_nodos_capas_campos where clave_capa='ALUM';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Ident.ALUM','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ALUM','Ident.ALUM','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ALUM','Ident.ALUM','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Ident.ALUM','orden_vt','localgiseiel.panels.label.orden',0,null,'CodOrden');	
			
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Info.ALUM','tipo_luminaria','localgiseiel.tabla.luminaria.columna.tipo_vt',4,'eiel_tipo_luminaria','tipo_luminaria');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('ALUM','Esta.ALUM','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
	--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('ALUM','Info.ALUM','eiel_c_alum_pl.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('ALUM','Info.ALUM','eiel_c_alum_pl.codpoblamiento',null,-2,null,null,false,false); 


-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.ALUM';
insert into lcg_nodos_capas_traducciones values ('eiel.ALUM','Alumbrado','es_ES');
