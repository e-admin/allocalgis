-- Este script crea en la tabla eiel_c_saneam_ali dos atributos que no venían incluidos en el modelo de datos de la EIEL.
 
-- Crear atributos codentidad y codpoblamiento (no obligatorios).

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

select f_add_col('public.eiel_c_saneam_ali','codentidad', 'ALTER TABLE eiel_c_saneam_ali ADD COLUMN codentidad character varying(4)');
select f_add_col('public.eiel_c_saneam_ali','codpoblamiento', 'ALTER TABLE eiel_c_saneam_ali ADD COLUMN codpoblamiento character varying(2)');


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF NOT EXISTS (SELECT * FROM COLUMNS WHERE NAME = 'codentidad' AND ID_TABLE = (select id_table from tables where name = 'eiel_c_saneam_ali')) THEN	
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'codentidad',null,(select id_table from tables where name = 'eiel_c_saneam_ali'),4,NULL,NULL,3);
	
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'codpoblamiento',null,(select id_table from tables where name = 'eiel_c_saneam_ali'),2,NULL,NULL,3);
				
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]codentidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]codentidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]codentidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]codentidad');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'LI'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'codentidad'),(select max(position) from attributes where id_layer = (SELECT id_layer from layers where name = 'LI')),true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]codpoblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]codpoblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]codpoblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]codpoblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'LI'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'codpoblamiento'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'LI')),true);

		UPDATE ATTRIBUTES SET position = (select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'LI')) WHERE id_layer = (SELECT id_layer from layers where name = 'LI') and id_column = (SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'revision_actual');

		UPDATE queries set 
		selectquery = 'SELECT transform("eiel_c_saneam_ali"."GEOMETRY", ?T) AS "GEOMETRY","eiel_c_saneam_ali"."id","eiel_c_saneam_ali"."id_municipio","eiel_c_saneam_ali"."acumulacion","eiel_c_saneam_ali"."pot_motor","eiel_c_saneam_ali"."estado","eiel_c_saneam_ali"."cota_z","eiel_c_saneam_ali"."obra_ejec","eiel_c_saneam_ali"."precision_dig","eiel_c_saneam_ali"."observ","eiel_c_saneam_ali"."codentidad","eiel_c_saneam_ali"."codpoblamiento","eiel_c_saneam_ali"."revision_actual" FROM "eiel_c_saneam_ali" WHERE "eiel_c_saneam_ali"."id_municipio" IN (?M)',
		insertquery = 'INSERT INTO "eiel_c_saneam_ali" ("GEOMETRY","id","id_municipio","acumulacion","pot_motor","estado","cota_z","obra_ejec","precision_dig","observ","codentidad","codpoblamiento","revision_actual") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13)',
		updatequery = 'UPDATE "eiel_c_saneam_ali" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio" = ?M,"acumulacion"=?4,"pot_motor"=?5,"estado"=?6,"cota_z"=?7,"obra_ejec"=?8,"precision_dig"=?9,"observ"=?10,"codentidad"=?11,"codpoblamiento"=?12,"revision_actual"=?13 WHERE "id"=?2'
		where id_layer = (SELECT id_layer from layers where name = 'LI');

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codentidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]codentidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]codentidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]codentidad');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]codentidad');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'LI_TC'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'codentidad'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'LI_TC')),true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','codpoblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]codpoblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]codpoblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]codpoblamiento');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]codpoblamiento');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'LI_TC'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_c_saneam_ali' and columns.name = 'codpoblamiento'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'LI_TC')),true);

		UPDATE queries set 
		selectquery = 'SELECT "eiel_c_saneam_ali"."id","eiel_c_saneam_ali"."id_municipio",transform("eiel_c_saneam_ali"."GEOMETRY", ?T) AS "GEOMETRY","eiel_c_saneam_ali"."codentidad","eiel_c_saneam_ali"."codpoblamiento" FROM "eiel_c_saneam_ali" WHERE "eiel_c_saneam_ali"."id_municipio" IN (?M)' where id_layer = (SELECT id_layer from layers where name = 'LI_TC');
	END IF;		
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO1";







-- Este script crea en la tabla eiel_pmr_paso_de_peatones dos atributos que no estaban contemplados en el modelo de datos inicial.
 
-- Crear atributos Bordillo_no_rebajado y Pavimento_no_diferenciado (no obligatorios).






select f_add_col('public.eiel_pmr_paso_de_peatones','Bordillo_no_rebajado', 'ALTER TABLE "eiel_pmr_paso_de_peatones" ADD COLUMN "Bordillo_no_rebajado" character varying(1);');
select f_add_col('public.eiel_pmr_paso_de_peatones','Pavimento_no_diferenciado', 'ALTER TABLE "eiel_pmr_paso_de_peatones" ADD COLUMN "Pavimento_no_diferenciado" character varying(2);');


-- Registrar los atributos en la tabla COLUMNS 
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
DECLARE
	result   NUMERIC DEFAULT 0;
BEGIN
	IF NOT EXISTS (SELECT * FROM COLUMNS WHERE NAME = 'Bordillo_no_rebajado' AND ID_TABLE = (select id_table from tables where name = 'eiel_pmr_paso_de_peatones')) THEN	
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'Bordillo_no_rebajado',(SELECT id from domains where name = 'EIEL_PMR_Lado'),(select id_table from tables where name = 'eiel_pmr_paso_de_peatones'),1,NULL,NULL,3);
		INSERT INTO COLUMNS(ID,NAME,ID_DOMAIN,ID_TABLE,"Length","Precision","Scale","Type") VALUES (NEXTVAL('seq_columns'),'Pavimento_no_diferenciado',(SELECT id from domains where name = 'EIEL_PMR_Lado'),(select id_table from tables where name = 'eiel_pmr_paso_de_peatones'),1,NULL,NULL,3);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Bordillo_no_rebajado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Bordillo_no_rebajado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Bordillo_no_rebajado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Bordillo_no_rebajado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Bordillo_no_rebajado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'Paso_de_peatones'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_pmr_paso_de_peatones' and columns.name = 'Bordillo_no_rebajado'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'Paso_de_peatones')),true);

		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (NEXTVAL('seq_dictionary'),'es_ES','Pavimento_no_diferenciado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'ca_ES','[cat]Pavimento_no_diferenciado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'gl_ES','[gl]Pavimento_no_diferenciado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'va_ES','[va]Pavimento_no_diferenciado');
		INSERT INTO DICTIONARY(ID_VOCABLO,LOCALE,TRADUCCION) VALUES (CURRVAL('seq_dictionary'),'eu_ES','[eu]Pavimento_no_diferenciado');
		INSERT INTO ATTRIBUTES(ID,ID_ALIAS,ID_LAYER,ID_COLUMN,POSITION,EDITABLE) VALUES (NEXTVAL('seq_attributes'),CURRVAL('seq_dictionary'),(SELECT id_layer from layers where name = 'Paso_de_peatones'),(SELECT id FROM columns,tables where columns.id_table = tables.id_table and tables.name = 'eiel_pmr_paso_de_peatones' and columns.name = 'Pavimento_no_diferenciado'),(select max(position)+1 from attributes where id_layer = (SELECT id_layer from layers where name = 'Paso_de_peatones')),true);
	END IF;	
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "PASO2";




UPDATE queries set 
selectquery = 'SELECT transform("eiel_pmr_paso_de_peatones"."GEOMETRY", ?T) AS "GEOMETRY","eiel_pmr_paso_de_peatones"."id","eiel_pmr_paso_de_peatones"."id_municipio","eiel_pmr_paso_de_peatones"."Observaciones","eiel_pmr_paso_de_peatones"."Bordillo_no_rebajado","eiel_pmr_paso_de_peatones"."Pavimento_no_diferenciado" FROM "eiel_pmr_paso_de_peatones" WHERE "eiel_pmr_paso_de_peatones"."id_municipio" IN (?M)',
insertquery = 'INSERT INTO "eiel_pmr_paso_de_peatones" ("GEOMETRY","id","id_municipio","Observaciones","Bordillo_no_rebajado","Pavimento_no_diferenciado") VALUES(transform(GeometryFromText(text(?1),?S), ?T),?PK,?M,?4,?5,?6)',
updatequery = 'UPDATE "eiel_pmr_paso_de_peatones" SET "GEOMETRY"=transform(GeometryFromText(text(?1),?S), ?T),"id"=?2,"id_municipio" = ?M,"Observaciones"=?4,"Bordillo_no_rebajado"=?5,"Pavimento_no_diferenciado"=?6 WHERE "id"=?2'
where id_layer = (SELECT id_layer from layers where name = 'Paso_de_peatones');