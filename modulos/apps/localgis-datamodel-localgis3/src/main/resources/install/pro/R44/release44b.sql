-- Creacion de indices

DROP INDEX IF EXISTS eiel_c_redviaria_tu_codprov;
CREATE INDEX eiel_c_redviaria_tu_codprov  ON eiel_c_redviaria_tu  USING btree (codprov);
DROP INDEX IF EXISTS eiel_c_redviaria_tu_codmunic;
CREATE INDEX eiel_c_redviaria_tu_codmunic  ON eiel_c_redviaria_tu  USING btree (codmunic);
DROP INDEX IF EXISTS eiel_c_redviaria_tu_codentidad;
CREATE INDEX eiel_c_redviaria_tu_codentidad  ON eiel_c_redviaria_tu  USING btree (codentidad);
DROP INDEX IF EXISTS eiel_c_redviaria_tu_codpoblamiento;
CREATE INDEX eiel_c_redviaria_tu_codpoblamiento  ON eiel_c_redviaria_tu  USING btree (codpoblamiento);
DROP INDEX IF EXISTS eiel_c_redviaria_tu_tipo;
CREATE INDEX eiel_c_redviaria_tu_tipo  ON eiel_c_redviaria_tu  USING btree (tipo);
DROP INDEX IF EXISTS eiel_c_redviaria_tu_estado;
CREATE INDEX eiel_c_redviaria_tu_estado  ON eiel_c_redviaria_tu  USING btree (estado);
DROP INDEX IF EXISTS eiel_c_redviaria_tu_revision_expirada;
CREATE INDEX eiel_c_redviaria_tu_revision_expirada  ON eiel_c_redviaria_tu  USING btree (revision_expirada);

DROP INDEX IF EXISTS eiel_c_abast_rd_codprov;
CREATE INDEX eiel_c_abast_rd_codprov  ON eiel_c_abast_rd  USING btree (codprov);
DROP INDEX IF EXISTS eiel_c_abast_rd_codmunic;
CREATE INDEX eiel_c_abast_rd_codmunic  ON eiel_c_abast_rd  USING btree (codmunic);
DROP INDEX IF EXISTS eiel_c_abast_rd_codentidad;
CREATE INDEX eiel_c_abast_rd_codentidad  ON eiel_c_abast_rd  USING btree (codentidad);
DROP INDEX IF EXISTS eiel_c_abast_rd_codpoblamiento;
CREATE INDEX eiel_c_abast_rd_codpoblamiento  ON eiel_c_abast_rd  USING btree (codpoblamiento);
DROP INDEX IF EXISTS eiel_c_abast_rd_estado;
CREATE INDEX eiel_c_abast_rd_estado  ON eiel_c_abast_rd  USING btree (estado);
DROP INDEX IF EXISTS eiel_c_abast_rd_revision_expirada;
CREATE INDEX eiel_c_abast_rd_revision_expirada  ON eiel_c_abast_rd  USING btree (revision_expirada);

DROP INDEX IF EXISTS eiel_c_saneam_rs_codprov;
CREATE INDEX eiel_c_saneam_rs_codprov  ON eiel_c_saneam_rs  USING btree (codprov);
DROP INDEX IF EXISTS eiel_c_saneam_rs_codmunic;
CREATE INDEX eiel_c_saneam_rs_codmunic  ON eiel_c_saneam_rs  USING btree (codmunic);
DROP INDEX IF EXISTS eiel_c_saneam_rs_codentidad;
CREATE INDEX eiel_c_saneam_rs_codentidad  ON eiel_c_saneam_rs  USING btree (codentidad);
DROP INDEX IF EXISTS eiel_c_saneam_rs_codpoblamiento;
CREATE INDEX eiel_c_saneam_rs_codpoblamiento  ON eiel_c_saneam_rs  USING btree (codpoblamiento);
DROP INDEX IF EXISTS eiel_c_saneam_rs_estado;
CREATE INDEX eiel_c_saneam_rs_estado  ON eiel_c_saneam_rs  USING btree (estado);
DROP INDEX IF EXISTS eiel_c_saneam_rs_revision_expirada;
CREATE INDEX eiel_c_saneam_rs_revision_expirada  ON eiel_c_saneam_rs  USING btree (revision_expirada);


DROP INDEX IF EXISTS eiel_c_alum_pl_codprov;
CREATE INDEX eiel_c_alum_pl_codprov  ON eiel_c_alum_pl  USING btree (codprov);
DROP INDEX IF EXISTS eiel_c_alum_pl_codmunic;
CREATE INDEX eiel_c_alum_pl_codmunic  ON eiel_c_alum_pl  USING btree (codmunic);
DROP INDEX IF EXISTS eiel_c_alum_pl_codentidad;
CREATE INDEX eiel_c_alum_pl_codentidad  ON eiel_c_alum_pl  USING btree (codentidad);
DROP INDEX IF EXISTS eiel_c_alum_pl_codpoblamiento;
CREATE INDEX eiel_c_alum_pl_codpoblamiento  ON eiel_c_alum_pl  USING btree (codpoblamiento);
DROP INDEX IF EXISTS eiel_c_alum_pl_estado;
CREATE INDEX eiel_c_alum_pl_estado  ON eiel_c_alum_pl  USING btree (estado);
DROP INDEX IF EXISTS eiel_c_alum_pl_revision_expirada;
CREATE INDEX eiel_c_alum_pl_revision_expirada  ON eiel_c_alum_pl  USING btree (revision_expirada);


DROP INDEX IF EXISTS eiel_c_tramos_carreteras_codprov;
CREATE INDEX eiel_c_tramos_carreteras_codprov  ON eiel_c_tramos_carreteras  USING btree (codprov);
DROP INDEX IF EXISTS eiel_c_tramos_carreteras_codmunic;
CREATE INDEX eiel_c_tramos_carreteras_codmunic  ON eiel_c_tramos_carreteras  USING btree (codmunic);
DROP INDEX IF EXISTS eiel_c_tramos_carreteras_estado;
CREATE INDEX eiel_c_tramos_carreteras_estado  ON eiel_c_tramos_carreteras  USING btree (estado);
DROP INDEX IF EXISTS eiel_c_tramos_carreteras_revision_expirada;
CREATE INDEX eiel_c_tramos_carreteras_revision_expirada  ON eiel_c_tramos_carreteras  USING btree (revision_expirada);




DROP INDEX IF EXISTS eiel_indicadores_t_captaciones_aux_codprov;
CREATE INDEX eiel_indicadores_t_captaciones_aux_codprov  ON eiel_indicadores_t_captaciones_aux  USING btree (codprov);
DROP INDEX IF EXISTS eiel_indicadores_t_captaciones_aux_codmunic;
CREATE INDEX eiel_indicadores_t_captaciones_aux_codmunic  ON eiel_indicadores_t_captaciones_aux  USING btree (codmunic);
DROP INDEX IF EXISTS eiel_indicadores_t_captaciones_aux_codentidad;
CREATE INDEX eiel_indicadores_t_captaciones_aux_codentidad  ON eiel_indicadores_t_captaciones_aux  USING btree (codentidad);
DROP INDEX IF EXISTS eiel_indicadores_t_captaciones_aux_codpoblamiento;
CREATE INDEX eiel_indicadores_t_captaciones_aux_codpoblamiento  ON eiel_indicadores_t_captaciones_aux  USING btree (codpoblamiento);
DROP INDEX IF EXISTS eiel_indicadores_t_captaciones_aux_aag_caudal;
CREATE INDEX eiel_indicadores_t_captaciones_aux_aag_caudal  ON eiel_indicadores_t_captaciones_aux  USING btree (aag_caudal);
DROP INDEX IF EXISTS eiel_indicadores_t_captaciones_aux_aag_restri;
CREATE INDEX eiel_indicadores_t_captaciones_aux_aag_restri  ON eiel_indicadores_t_captaciones_aux  USING btree (aag_restri);



delete from version where id_version = 'MODELO R44';
insert into version (id_version, fecha_version) values('MODELO R44', DATE '2014-12-20');






