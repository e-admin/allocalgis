
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

select f_add_col('public.eiel_indicadores_d_rblimpieza','nnuc_rbl_period_di', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nnuc_rbl_period_di numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nhab_rbl_period_di', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nhab_rbl_period_di numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nest_rbl_period_di', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nest_rbl_period_di numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nnuc_rbl_period_al', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nnuc_rbl_period_al numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nhab_rbl_period_al', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nhab_rbl_period_al numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nest_rbl_period_al', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nest_rbl_period_al numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nest_rbl_period_al', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nest_rbl_period_al numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nnuc_rbl_period_ne', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nnuc_rbl_period_ne numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nhab_rbl_period_ne', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nhab_rbl_period_ne numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nest_rbl_period_ne', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nest_rbl_period_ne numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nnuc_rbl_period_ot', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nnuc_rbl_period_ot numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nhab_rbl_period_ot', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nhab_rbl_period_ot numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_rblimpieza','nest_rbl_period_ot', 'ALTER TABLE eiel_indicadores_d_rblimpieza ADD COLUMN nest_rbl_period_ot numeric(6,0);
');


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	-- Asumimos que si la primera columna no esta creada, el resto tampoco
	IF NOT EXISTS (SELECT * FROM COLUMNS WHERE name = 'nnuc_rbl_period_di') THEN	
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nnuc_rbl_period_di',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nnuc_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nnuc_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nnuc_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nnuc_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nnuc_rbl_period_di');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nhab_rbl_period_di',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhab_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nhab_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nhab_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nhab_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nhab_rbl_period_di');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nest_rbl_period_di',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nest_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nest_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nest_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nest_rbl_period_di');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nest_rbl_period_di');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nnuc_rbl_period_al',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nnuc_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nnuc_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nnuc_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nnuc_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nnuc_rbl_period_al');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nhab_rbl_period_al',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhab_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nhab_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nhab_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nhab_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nhab_rbl_period_al');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nest_rbl_period_al',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nest_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nest_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nest_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nest_rbl_period_al');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nest_rbl_period_al');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nnuc_rbl_period_ne',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nnuc_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nnuc_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nnuc_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nnuc_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nnuc_rbl_period_ne');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nhab_rbl_period_ne',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhab_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nhab_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nhab_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nhab_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nhab_rbl_period_ne');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nest_rbl_period_ne',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nest_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nest_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nest_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nest_rbl_period_ne');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nest_rbl_period_ne');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nnuc_rbl_period_ot',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nnuc_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nnuc_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nnuc_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nnuc_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nnuc_rbl_period_ot');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nhab_rbl_period_ot',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nhab_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nhab_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nhab_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nhab_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nhab_rbl_period_ot');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'nest_rbl_period_ot',null,(select id_table from tables where name = 'eiel_indicadores_d_rblimpieza'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','nest_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]nest_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]nest_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]nest_rbl_period_ot');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]nest_rbl_period_ot');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras')),true);
	
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";






UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_indicadores_d_rblimpieza"."GEOMETRY", ?T) AS "GEOMETRY", CASE WHEN ((n_contenedores >= 1) AND (n_contenedores < 3)) THEN ''1'' WHEN ((n_contenedores >= 3) AND (n_contenedores < 4)) THEN ''2'' WHEN ((n_contenedores >= 5) AND (n_contenedores < 6)) THEN ''3'' WHEN ((n_contenedores >= 6) AND (n_contenedores < 49)) THEN ''4'' END AS estilo_sld, CASE WHEN (nnuc_rbl_no >= 1) OR (nnuc_rbl_calidad_in >= 1) THEN ''1'' ELSE ''2'' END AS estilo2_sld, "eiel_indicadores_d_rblimpieza"."id","eiel_indicadores_d_rblimpieza"."id_municipio","eiel_indicadores_d_rblimpieza"."toponimo","eiel_indicadores_d_rblimpieza"."codprov","eiel_indicadores_d_rblimpieza"."codmunic","eiel_indicadores_d_rblimpieza"."codentidad","eiel_indicadores_d_rblimpieza"."codpoblamiento","eiel_indicadores_d_rblimpieza"."nnuc_rbl_no","eiel_indicadores_d_rblimpieza"."nhab_rbl_no","eiel_indicadores_d_rblimpieza"."nest_rbl_no","eiel_indicadores_d_rblimpieza"."nnuc_rbl_period_me","eiel_indicadores_d_rblimpieza"."nhab_rbl_period_me","eiel_indicadores_d_rblimpieza"."nest_rble_period_me","eiel_indicadores_d_rblimpieza"."nnuc_rbl_period_ba","eiel_indicadores_d_rblimpieza"."nhab_rbl_period_ba","eiel_indicadores_d_rblimpieza"."nest_rbl_period_ba", "eiel_indicadores_d_rblimpieza"."nnuc_rbl_period_di","eiel_indicadores_d_rblimpieza"."nhab_rbl_period_di","eiel_indicadores_d_rblimpieza"."nest_rble_period_di", "eiel_indicadores_d_rblimpieza"."nnuc_rbl_period_al","eiel_indicadores_d_rblimpieza"."nhab_rbl_period_al","eiel_indicadores_d_rblimpieza"."nest_rble_period_al", "eiel_indicadores_d_rblimpieza"."nnuc_rbl_period_ne","eiel_indicadores_d_rblimpieza"."nhab_rbl_period_ne","eiel_indicadores_d_rblimpieza"."nest_rble_period_ne", "eiel_indicadores_d_rblimpieza"."nnuc_rbl_period_ot","eiel_indicadores_d_rblimpieza"."nhab_rbl_period_ot","eiel_indicadores_d_rblimpieza"."nest_rble_period_ot", "eiel_indicadores_d_rblimpieza"."nnuc_rbl_calidad_ad","eiel_indicadores_d_rblimpieza"."nhab_rbl_calidad_ad","eiel_indicadores_d_rblimpieza"."nest_rbl_calidad_ad","eiel_indicadores_d_rblimpieza"."nnuc_rbl_calidad_in","eiel_indicadores_d_rblimpieza"."nhab_rbl_calidad_in","eiel_indicadores_d_rblimpieza"."nest_rbl_calidad_in","eiel_indicadores_d_rblimpieza"."nnuc_rb_selectiva_si","eiel_indicadores_d_rblimpieza"."nhab_rb_selectiva_si","eiel_indicadores_d_rblimpieza"."nest_rb_selectiva_si","eiel_indicadores_d_rblimpieza"."nnuc_rb_selectiva_no","eiel_indicadores_d_rblimpieza"."nhab_rb_selectiva_no","eiel_indicadores_d_rblimpieza"."nest_rb_selectiva_no","eiel_indicadores_d_rblimpieza"."nnuc_limp_si","eiel_indicadores_d_rblimpieza"."nhab_limp_si","eiel_indicadores_d_rblimpieza"."nest_limp_si","eiel_indicadores_d_rblimpieza"."nnuc_limp_no","eiel_indicadores_d_rblimpieza"."nhab_limp_no","eiel_indicadores_d_rblimpieza"."nest_limp_no","eiel_indicadores_d_rblimpieza"."nviv_serv_deficit","eiel_indicadores_d_rblimpieza"."nhab_serv_deficit","eiel_indicadores_d_rblimpieza"."nest_serv_deficit","eiel_indicadores_d_rblimpieza"."n_contenedores" FROM "eiel_indicadores_d_rblimpieza" WHERE "eiel_indicadores_d_rblimpieza"."id_municipio" IN (?M)' where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_IV_RecogidaBasuras' LIMIT 1);


select f_add_col('public.eiel_indicadores_d_centrosen','fp2_ncen', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN fp2_ncen numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_centrosen','fp2_plazas', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN fp2_plazas numeric(6,0);;');
select f_add_col('public.eiel_indicadores_d_centrosen','fp2_nalumnos', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN fp2_nalumnos numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_centrosen','otr_ncen', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN otr_ncen numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_centrosen','otr_plazas', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN otr_plazas numeric(6,0);');
select f_add_col('public.eiel_indicadores_d_centrosen','otr_nalumnos', 'ALTER TABLE eiel_indicadores_d_centrosen ADD COLUMN otr_nalumnos numeric(6,0);');


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	-- Asumimos que si la primera columna no esta creada, el resto tampoco
	IF NOT EXISTS (SELECT * FROM COLUMNS WHERE name = 'fp2_ncen') THEN	
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'fp2_ncen',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fp2_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fp2_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fp2_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fp2_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fp2_ncen');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'fp2_plazas',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fp2_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fp2_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fp2_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fp2_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fp2_plazas');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'fp2_nalumnos',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','fp2_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]fp2_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]fp2_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]fp2_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]fp2_nalumnos');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'otr_ncen',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','otr_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]otr_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]otr_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]otr_ncen');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]otr_ncen');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'otr_plazas',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','otr_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]otr_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]otr_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]otr_plazas');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]otr_plazas');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);

		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'otr_nalumnos',null,(select id_table from tables where name = 'eiel_indicadores_d_centrosen'),0,6,0,2);
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','otr_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]otr_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]otr_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]otr_nalumnos');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]otr_nalumnos');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza'),CURRVAL('seq_columns'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza')),true);
	
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";




UPDATE QUERIES SET selectquery = 'SELECT transform("eiel_indicadores_d_centrosen"."GEOMETRY", ?T) AS "GEOMETRY", CASE WHEN ((primaria_nalumnos >= 26) AND (primaria_nalumnos < 86)) THEN ''1'' WHEN ((primaria_nalumnos >= 86) AND (primaria_nalumnos < 214)) THEN ''2'' WHEN ((primaria_nalumnos >= 214) AND (primaria_nalumnos < 446)) THEN ''3'' WHEN ((primaria_nalumnos >= 446) AND (primaria_nalumnos < 725)) THEN ''4'' END AS estilo_sld, CASE WHEN (guarderia_sup_alibre_m > 0) OR (guarderia_sup_cubierta_m > 0) THEN ''1'' ELSE ''2'' END AS estilo2_sld, "eiel_indicadores_d_centrosen"."id","eiel_indicadores_d_centrosen"."toponimo","eiel_indicadores_d_centrosen"."id_municipio","eiel_indicadores_d_centrosen"."codprov","eiel_indicadores_d_centrosen"."codmunic","eiel_indicadores_d_centrosen"."codentidad","eiel_indicadores_d_centrosen"."codpoblamiento","eiel_indicadores_d_centrosen"."einfantil_ncen","eiel_indicadores_d_centrosen"."einfantil_nalumnos","eiel_indicadores_d_centrosen"."einfantil_plazas","eiel_indicadores_d_centrosen"."eso_ncen","eiel_indicadores_d_centrosen"."eso_nalumnos","eiel_indicadores_d_centrosen"."eso_plazas","eiel_indicadores_d_centrosen"."especial_ncen","eiel_indicadores_d_centrosen"."especial_nalumnos","eiel_indicadores_d_centrosen"."especial_plazas","eiel_indicadores_d_centrosen"."fp1_ncen","eiel_indicadores_d_centrosen"."fp1_nalumnos","eiel_indicadores_d_centrosen"."fp1_plazas","eiel_indicadores_d_centrosen"."otr_ncen","eiel_indicadores_d_centrosen"."otr_nalumnos","eiel_indicadores_d_centrosen"."otr_plazas","eiel_indicadores_d_centrosen"."primaria_ncen","eiel_indicadores_d_centrosen"."primaria_nalumnos","eiel_indicadores_d_centrosen"."primaria_plazas","eiel_indicadores_d_centrosen"."fp_ncen","eiel_indicadores_d_centrosen"."fp_nalumnos","eiel_indicadores_d_centrosen"."fp_plazas","eiel_indicadores_d_centrosen"."secundaria_ncen","eiel_indicadores_d_centrosen"."secundaria_nalumnos","eiel_indicadores_d_centrosen"."secundaria_plazas","eiel_indicadores_d_centrosen"."guarderia_ncen","eiel_indicadores_d_centrosen"."guarderia_capacidad","eiel_indicadores_d_centrosen"."guarderia_sup_alibre_b","eiel_indicadores_d_centrosen"."guarderia_sup_alibre_r","eiel_indicadores_d_centrosen"."guarderia_sup_alibre_m","eiel_indicadores_d_centrosen"."guarderia_sup_cubierta_b","eiel_indicadores_d_centrosen"."guarderia_sup_cubierta_r","eiel_indicadores_d_centrosen"."guarderia_sup_cubierta_m" FROM "eiel_indicadores_d_centrosen" WHERE "eiel_indicadores_d_centrosen"."id_municipio" IN (?M)' where id_layer = (SELECT id_layer from layers where name = 'EIEL_Indicadores_V_CEnsenanza' LIMIT 1);


