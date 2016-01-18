-- Correccion Longitud de Campos
ALTER TABLE eiel_tr_saneam_tcl_pobl ALTER COLUMN tramo_cl type varchar(3);
ALTER TABLE eiel_tr_saneam_tem_pobl ALTER COLUMN tramo_em type varchar(3);

-- EIELgetColector
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELgetColector','select clave,codprov,codmunic,tramo_cl as orden from eiel_c_saneam_tcl where id=?',1,9205);
-- EIELgetListaNucleosColector
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELgetListaNucleosColector','select * from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and orden_tcl=?',1,9205);
-- EIELgetNucleosColector
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELgetNucleosColector','select * from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and orden_tcl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);
-- EIELupdateNucleosColector
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELupdateNucleosColector','update eiel_tr_saneam_tcl_pobl set clave_tcl=?, codprov_tcl=?, codmunic_tcl=?, orden_tcl=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, pmi=?, pmf=?, observ=?, fecha_revision=?, estado_revision=? where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and orden_tcl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);
-- EIELinsertNucleosColector
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELinsertNucleosColector','insert into eiel_tr_saneam_tcl_pobl (clave_tcl, codprov_tcl, codmunic_tcl, orden_tcl, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, pmi, pmf, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);
-- EIELdeleteNucleosColector
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELdeleteNucleosColector','delete from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and orden_tcl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);
-- ExtendedForm
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.ColectoresExtendedForm' WHERE name='TCL';
UPDATE layers SET extended_form='com.geopista.app.eiel.forms.EmisariosExtendedForm' WHERE name='TEM';
-- EIELgetEmisario
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELgetEmisario','select clave,codprov,codmunic,tramo_em as orden from eiel_c_saneam_tem where id=?',1,9205);
-- EIELgetListaNucleosEmisario
delete from query_catalog where id='EIELgetListaNucleosEmisario';
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELgetListaNucleosEmisario','select * from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=?',1,9205);
-- EIELgetNucleosEmisario
delete from query_catalog where id='EIELgetNucleosEmisario';
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELgetNucleosEmisario','select * from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);
-- EIELupdateNucleosEmisario
delete from query_catalog where id='EIELupdateNucleosEmisario';
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELupdateNucleosEmisario','update eiel_tr_saneam_tem_pobl set clave_tem=?, codprov_tem=?, codmunic_tem=?, tramo_em=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, pmi=?, pmf=?, observ=?, fecha_revision=?, estado_revision=? where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);
-- EIELinsertNucleosEmisario
delete from query_catalog where id='EIELinsertNucleosEmisario';
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELinsertNucleosEmisario','insert into eiel_tr_saneam_tem_pobl (clave_tem, codprov_tem, codmunic_tem, tramo_em, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, pmi, pmf, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);
-- EIELdeleteNucleosEmisario
delete from query_catalog where id='EIELdeleteNucleosEmisario';
INSERT INTO query_catalog (id,query,acl,idperm) VALUES ('EIELdeleteNucleosEmisario','delete from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);
-- Aseguramos que el codmunic sea el propio del id_municipio
update eiel_c_saneam_tem set codmunic=SUBSTR(id_municipio,3)
update eiel_c_saneam_tcl set codmunic=SUBSTR(id_municipio,3)