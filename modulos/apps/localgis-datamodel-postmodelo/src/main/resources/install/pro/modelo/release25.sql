
DROP SEQUENCE IF EXISTS seq_eiel_c_saneam_cb;
CREATE SEQUENCE seq_eiel_c_saneam_cb
  INCREMENT 1
  MINVALUE 1
  START 1
  CACHE 1;
ALTER TABLE seq_eiel_c_saneam_cb
  OWNER TO geopista;

DROP SEQUENCE IF EXISTS seq_eiel_c_abast_cb;
CREATE SEQUENCE seq_eiel_c_abast_cb
  INCREMENT 1
  MINVALUE 1
  START 1
  CACHE 1;
ALTER TABLE seq_eiel_c_abast_cb
  OWNER TO geopista;
 
  

SELECT setval('public.seq_eiel_c_abast_ar', (select max(id)::bigint from eiel_c_abast_ar), true);
SELECT setval('public.seq_eiel_c_abast_ca', (select max(id)::bigint from eiel_c_abast_ca), true);
SELECT setval('public.seq_eiel_c_abast_cb', (select max(id)::bigint from eiel_c_abast_cb), true);
SELECT setval('public.seq_eiel_c_abast_de', (select max(id)::bigint from eiel_c_abast_de), true);
SELECT setval('public.seq_eiel_c_abast_rd', (select max(id)::bigint from eiel_c_abast_rd), true);
SELECT setval('public.seq_eiel_c_abast_tcn', (select max(id)::bigint from eiel_c_abast_tcn), true);
SELECT setval('public.seq_eiel_c_abast_tp', (select max(id)::bigint from eiel_c_abast_tp), true);
SELECT setval('public.seq_eiel_c_alum_cmp', (select max(id)::bigint from eiel_c_alum_cmp), true);
SELECT setval('public.seq_eiel_c_alum_eea', (select max(id)::bigint from eiel_c_alum_eea), true);
SELECT setval('public.seq_eiel_c_alum_pl', (select max(id)::bigint from eiel_c_alum_pl), true);
SELECT setval('public.seq_eiel_c_as', (select max(id)::bigint from eiel_c_as), true);
SELECT setval('public.seq_eiel_c_cc', (select max(id)::bigint from eiel_c_cc), true);
SELECT setval('public.seq_eiel_c_ce', (select max(id)::bigint from eiel_c_ce), true);
SELECT setval('public.seq_eiel_c_comarcas', (select max(id)::bigint from eiel_c_comarcas), true);
SELECT setval('public.seq_eiel_c_cu', (select max(id)::bigint from eiel_c_cu), true);
SELECT setval('public.seq_eiel_c_edificiosing', (select max(id)::bigint from eiel_c_edificiosing), true);
SELECT setval('public.seq_eiel_c_en', (select max(id)::bigint from eiel_c_en), true);
SELECT setval('public.seq_eiel_c_id', (select max(id)::bigint from eiel_c_id), true);
SELECT setval('public.seq_eiel_c_ip', (select max(id)::bigint from eiel_c_ip), true);
SELECT setval('public.seq_eiel_c_lm', (select max(id)::bigint from eiel_c_lm), true);
SELECT setval('public.seq_eiel_c_mt', (select max(id)::bigint from eiel_c_mt), true);
SELECT setval('public.seq_eiel_c_municipios', (select max(id)::bigint from eiel_c_municipios), true);
SELECT setval('public.seq_eiel_c_municipios_puntos', (select max(id)::bigint from eiel_c_municipios_puntos), true);
SELECT setval('public.seq_eiel_c_nucleo_poblacion', (select max(id)::bigint from eiel_c_nucleo_poblacion), true);
SELECT setval('public.seq_eiel_c_nucleos_puntos', (select max(id)::bigint from eiel_c_nucleos_puntos), true);
SELECT setval('public.seq_eiel_c_parcelas', (select max(id)::bigint from eiel_c_parcelas), true);
SELECT setval('public.seq_eiel_c_parroquias', (select max(id)::bigint from eiel_c_parroquias), true);
SELECT setval('public.seq_eiel_c_pj', (select max(id)::bigint from eiel_c_pj), true);
SELECT setval('public.seq_eiel_c_provincia', (select max(id)::bigint from eiel_c_provincia), true);
SELECT setval('public.seq_eiel_c_redviaria_tu', (select max(id)::bigint from eiel_c_redviaria_tu), true);
SELECT setval('public.seq_eiel_c_sa', (select max(id)::bigint from eiel_c_sa), true);
SELECT setval('public.seq_eiel_c_saneam_ali', (select max(id)::bigint from eiel_c_saneam_ali), true);
SELECT setval('public.seq_eiel_c_saneam_cb', (select max(id)::bigint from eiel_c_saneam_cb), true);
SELECT setval('public.seq_eiel_c_saneam_ed', (select max(id)::bigint from eiel_c_saneam_ed), true);
SELECT setval('public.seq_eiel_c_saneam_pr', (select max(id)::bigint from eiel_c_saneam_pr), true);
SELECT setval('public.seq_eiel_c_saneam_pv', (select max(id)::bigint from eiel_c_saneam_pv), true);
SELECT setval('public.seq_eiel_c_saneam_pr', (select max(id)::bigint from eiel_c_saneam_pr), true);
SELECT setval('public.seq_eiel_c_saneam_rs', (select max(id)::bigint from eiel_c_saneam_rs), true);
SELECT setval('public.seq_eiel_c_saneam_tcl', (select max(id)::bigint from eiel_c_saneam_tcl), true);
SELECT setval('public.seq_eiel_c_saneam_tem', (select max(id)::bigint from eiel_c_saneam_tem), true);
SELECT setval('public.seq_eiel_c_su', (select max(id)::bigint from eiel_c_su), true);
SELECT setval('public.seq_eiel_c_ta', (select max(id)::bigint from eiel_c_ta), true);
SELECT setval('public.seq_eiel_c_tramos_carreteras', (select max(id)::bigint from eiel_c_tramos_carreteras), true);
SELECT setval('public.seq_eiel_c_vt', (select max(id)::bigint from eiel_c_vt), true);


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

select f_add_col('public.eiel_indicadores_d_rsaneam_aux','ndepuradoras', 'ALTER TABLE eiel_indicadores_d_rsaneam_aux ADD COLUMN ndepuradoras numeric(8,0) DEFAULT 0; COMMENT ON COLUMN eiel_indicadores_d_rsaneam_aux.ndepuradoras IS ''Numero de depuradoras''');
select f_add_col('public.eiel_indicadores_d_rsaneam','ndepuradoras', 'ALTER TABLE eiel_indicadores_d_rsaneam ADD COLUMN ndepuradoras numeric(8,0) DEFAULT 0; COMMENT ON COLUMN eiel_indicadores_d_rsaneam.ndepuradoras IS ''Numero de depuradoras''');


UPDATE queries SET selectquery = replace(selectquery, 'substr(?M', 'substr(?M::text'), updatequery = replace(updatequery, 'substr(?M', 'substr(?M::text'), insertquery = replace(insertquery, 'substr(?M', 'substr(?M::text');

ALTER TABLE eiel_t_planeam_urban  OWNER TO postgres;