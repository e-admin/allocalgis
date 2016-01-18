delete from query_catalog where id='EIELgetNucleosCaptacion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosCaptacion','select * from eiel_tr_abast_ca_pobl where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetListaNucleosCaptacion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosCaptacion','select * from eiel_tr_abast_ca_pobl where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=? and revision_expirada=9999999999',1,9205);



delete from query_catalog where id='EIELgetNucleosDepositos';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosDepositos','select * from eiel_tr_abast_de_pobl where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetListaNucleosDepositos';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosDepositos','select * from eiel_tr_abast_de_pobl where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetNucleosTratamientosPotabilizacion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosTratamientosPotabilizacion','select * from eiel_tr_abast_tp_pobl where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetListaNucleosColector';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosColector','select * from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetListaNucleosTratamientosPotabilizacion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosTratamientosPotabilizacion','select * from eiel_tr_abast_tp_pobl where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=? and revision_expirada=9999999999',1,9205);


delete from query_catalog where id='EIELgetNucleosPuntosVertido';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosPuntosVertido','select * from eiel_tr_saneam_pv_pobl where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetListaNucleosPuntosVertido';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosPuntosVertido','select * from eiel_tr_saneam_pv_pobl where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetNucleosDepuradora1';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosDepuradora1','select * from eiel_tr_saneam_ed_pobl where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=? and revision_expirada=9999999999',1,9205);


delete from query_catalog where id='EIELgetListaNucleosDepuradora1';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosDepuradora1','select * from eiel_tr_saneam_ed_pobl where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=? and revision_expirada=9999999999',1,9205);



delete from query_catalog where id='EIELgetNucleosVertedero';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosVertedero','select * from eiel_tr_vt_pobl where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetListaNucleosVertedero';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosVertedero','select * from eiel_tr_vt_pobl where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=? and revision_expirada=9999999999',1,9205);


delete from query_catalog where id='EIELgetPanelPuntosVertido';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPuntosVertido','select * from eiel_t_saneam_pv where clave=? and codprov=? and codmunic=? and orden_pv=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetNucleosTramosConduccion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosTramosConduccion','select * from eiel_tr_abast_tcn_pobl where clave_tcn=? and codprov_tcn=? and codmunic_tcn=? and tramo_tcn=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=? and revision_expirada=9999999999',1,9205);


delete from query_catalog where id='EIELgetListaNucleosTramosConduccion';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosTramosConduccion','select * from eiel_tr_abast_tcn_pobl where clave_tcn=? AND codprov_tcn=? and codmunic_tcn=? and tramo_tcn=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetNucleosColector';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosColector','select * from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=? and revision_expirada=9999999999',1,9205);


delete from query_catalog where id='EIELgetNucleosEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosEmisario','select *  from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and  tramo_em=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=? and revision_expirada=9999999999',1,9205);

delete from query_catalog where id='EIELgetListaNucleosEmisario';
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosEmisario','select * from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and revision_expirada=9999999999',1,9205);

