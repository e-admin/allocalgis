set client_encoding to 'utf8';

--ABASTECIMIENTO (9)
grant select on eiel_t_abast_ca to consultas;
grant select on eiel_c_abast_ca to consultas;
grant select on eiel_tr_abast_ca_pobl to consultas;

grant select on eiel_c_abast_de to consultas;
grant select on eiel_t_abast_de to consultas;
grant select on eiel_tr_abast_de_pobl to consultas;

grant select on eiel_c_abast_tp to consultas;
grant select on eiel_t_abast_tp to consultas;
grant select on eiel_tr_abast_tp_pobl to consultas;


--grant select on eiel_c_nucl_encuest_2 to consultas;
grant select on eiel_t_nucl_encuest_2 to consultas;

--grant select on eiel_c_abast_serv to consultas;
grant select on eiel_t_abast_serv to consultas;

--grant select on eiel_c_abast_serv to consultas;
grant select on eiel_t_abast_au to consultas;

grant select on eiel_c_abast_ar to consultas;
--grant select on eiel_t_abast_ar to consultas;

grant select on eiel_c_abast_tcn to consultas;
--grant select on eiel_t_abast_tcn to consultas;
grant select on eiel_tr_abast_tcn_pobl to consultas;

grant select on eiel_c_abast_rd to consultas;
--grant select on eiel_t_abast_rd to consultas;




--SANEAMIENTO
grant select on eiel_t1_saneam_ed to consultas;
grant select on eiel_t2_saneam_ed to consultas;
grant select on eiel_c_saneam_ed to consultas;
--grant select on eiel_t_abast_rd to consultas;


grant select on eiel_c_saneam_rs to consultas;
grant select on eiel_c_saneam_tcl to consultas;

grant select on eiel_c_saneam_tcl to consultas;
grant select on eiel_c_saneam_tem to consultas;
grant select on eiel_t_saneam_pv to consultas;
grant select on eiel_t_saneam_serv to consultas;



--GENERALES
grant select on eiel_t_poblamiento to consultas;
grant select on lcg_nodos_capas_campos to consultas;
grant select on eiel_c_nucleo_poblacion to consultas;


--ALUMBRADO
grant select on eiel_c_alum_pl to consultas;
grant select on eiel_t_en to consultas;
grant select on eiel_t_en_nivel to consultas;

grant select on eiel_c_redviaria_tu to consultas;

grant select on eiel_c_tramos_carreteras to consultas;

grant select on eiel_t_carreteras to consultas;


-- Equipamientos Locales
grant select on eiel_c_cc to consultas;
grant select on eiel_c_ce to consultas;
grant select on eiel_c_as to consultas;
grant select on eiel_c_cu to consultas;

grant select on eiel_c_abast_ar to consultas;
grant select on eiel_c_abast_ca to consultas;
grant select on eiel_c_abast_cb to consultas;
grant select on eiel_c_abast_de to consultas;
grant select on eiel_c_abast_rd to consultas;
grant select on eiel_c_abast_tcn to consultas;
grant select on eiel_c_abast_tp	to consultas;
grant select on eiel_c_alum_cmp to consultas;
grant select on eiel_c_alum_pl to consultas;
grant select on eiel_c_cc to consultas;
grant select on eiel_c_alum_eea to consultas;
grant select on eiel_c_ce to consultas;
grant select on eiel_c_comarcas to consultas;


grant select on eiel_c_cu to consultas;
grant select on eiel_c_edificiosing to consultas;
grant select on eiel_c_en to consultas;
grant select on eiel_c_id to consultas;
grant select on eiel_c_ip to consultas;
grant select on eiel_c_lm to consultas;
grant select on eiel_c_mt to consultas;
grant select on eiel_c_municipios to consultas;
grant select on eiel_c_parcelas to consultas;
grant select on eiel_c_parroquias to consultas;
grant select on eiel_c_pj to consultas;
grant select on eiel_c_redviaria_tu to consultas;
grant select on eiel_c_sa to consultas;
grant select on eiel_c_saneam_ali to consultas;
grant select on eiel_c_saneam_cb to consultas;
grant select on eiel_c_saneam_ed to consultas;
grant select on eiel_c_saneam_pr to consultas;
grant select on eiel_c_saneam_pv to consultas;
grant select on eiel_c_saneam_rs to consultas;
grant select on eiel_c_saneam_tcl to consultas;
grant select on eiel_c_saneam_tem to consultas;
grant select on eiel_c_su to consultas;
grant select on eiel_c_ta to consultas;
grant select on eiel_c_tramos_carreteras to consultas;
grant select on eiel_c_vt to consultas;

grant select on eiel_t_id to consultas;
grant select on eiel_t_cu to consultas;
grant select on eiel_t_pj to consultas;
grant select on eiel_t_lm to consultas;
grant select on eiel_t_as to consultas;
grant select on eiel_t_cc to consultas;
grant select on eiel_t_ce to consultas;
grant select on eiel_t_su to consultas;
grant select on eiel_t_id_deportes to consultas;
grant select on eiel_t_cc_usos to consultas;
grant select on eiel_t_cu_usos to consultas;
grant select on eiel_t_ip to consultas;
grant select on eiel_t_sa to consultas;
grant select on eiel_t_mt to consultas;
grant select on eiel_t_ta to consultas;
grant select on eiel_t_abast_tcn to consultas;
grant select on eiel_t_saneam_tcl to consultas;
grant select on eiel_tr_saneam_tcl_pobl to consultas;
grant select on eiel_tr_saneam_ed_pobl to consultas;
grant select on eiel_tr_saneam_tem_pobl to consultas;
grant select on eiel_t_saneam_au to consultas;
grant select on eiel_tr_saneam_pv_pobl to consultas;

grant select on eiel_t_vt to consultas;
grant select on eiel_c_vt to consultas;
grant select on eiel_tr_vt_pobl to consultas;

grant select on eiel_tr_saneam_tem_pv to consultas;