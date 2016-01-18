INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerEntidades','select codentidad, codprov, codmunic from eiel_t_entidad_singular where codprov=? and codmunic=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelDiseminados','select * from eiel_t_mun_diseminados where codprov=? and codmunic=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosCaptacion','select * from eiel_tr_abast_ca_pobl where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosCaptacion','select * from eiel_tr_abast_ca_pobl where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPadronNucleos','select * from eiel_t_padron_nd where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetCaptacion','select clave, codprov,codmunic,orden_ca from eiel_c_abast_ca where id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosCaptacion','update eiel_tr_abast_ca_pobl set clave_ca=?, codprov_ca=?, codmunic_ca=?, orden_ca=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_inicio_serv=?, fecha_revision=?, estado_revision=? where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPadronMunicipios','select * from eiel_t_padron_ttmm where codprov=? and codmunic=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosCaptacion','insert into eiel_tr_abast_ca_pobl (clave_ca, codprov_ca, codmunic_ca, orden_ca, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_inicio_serv, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosCaptacion','delete from eiel_tr_abast_ca_pobl where clave_ca=? and codprov_ca=? and codmunic_ca=? and orden_ca=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetColector','select clave,codprov,codmunic,tramo_cl as orden from eiel_c_saneam_tcl where id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetDepositos','select clave, codprov,codmunic,orden_de from eiel_c_abast_de where id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosDepositos','select * from eiel_tr_abast_de_pobl where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosDepositos','insert into eiel_tr_abast_de_pobl (clave_de, codprov_de, codmunic_de, orden_de, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosDepositos','update eiel_tr_abast_de_pobl set clave_de=?, codprov_de=?, codmunic_de=?, orden_de=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_revision=?, estado_revision=? where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosDepositos','select * from eiel_tr_abast_de_pobl where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosDepositos','delete from eiel_tr_abast_de_pobl where clave_de=? and codprov_de=? and codmunic_de=? and orden_de=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetTratamientosPotabilizacion','select clave, codprov,codmunic,orden_tp from eiel_c_abast_tp where id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetEmisor','select clave,codprov,codmunic,tramo_em as orden from eiel_c_saneam_tem where id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosTratamientosPotabilizacion','select * from eiel_tr_abast_tp_pobl where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosTratamientosPotabilizacion','insert into eiel_tr_abast_tp_pobl (clave_tp, codprov_tp, codmunic_tp, orden_tp, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosColector','select * from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosTratamientosPotabilizacion','update eiel_tr_abast_tp_pobl set clave_tp=?, codprov_tp=?, codmunic_tp=?, orden_tp=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_revision=?, estado_revision=? where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosTratamientosPotabilizacion','select * from eiel_tr_abast_tp_pobl where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosTratamientosPotabilizacion','delete from eiel_tr_abast_tp_pobl where clave_tp=? and codprov_tp=? and codmunic_tp=? and orden_tp=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPuntosVertido','select clave, codprov,codmunic,orden_pv from eiel_c_saneam_pv where id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosPuntosVertido','select * from eiel_tr_saneam_pv_pobl where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosPuntosVertido','insert into eiel_tr_saneam_pv_pobl (clave_pv, codprov_pv, codmunic_pv, orden_pv, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosPuntosVertido','update eiel_tr_saneam_pv_pobl set clave_pv=?, codprov_pv=?, codmunic_pv=?, orden_pv=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_revision=?, estado_revision=? where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosPuntosVertido','select * from eiel_tr_saneam_pv_pobl where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosPuntosVertido','delete from eiel_tr_saneam_pv_pobl where clave_pv=? and codprov_pv=? and codmunic_pv=? and orden_pv=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetDepuradora1','select clave, codprov,codmunic,orden_ed from eiel_c_saneam_ed where id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosDepuradora1','select * from eiel_tr_saneam_ed_pobl where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosDepuradora1','insert into eiel_tr_saneam_ed_pobl (clave_ed, codprov_ed, codmunic_ed, orden_ed, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosDepuradora1','update eiel_tr_saneam_ed_pobl set clave_ed=?, codprov_ed=?, codmunic_ed=?, orden_ed=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, fecha_revision=?, estado_revision=? where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosDepuradora1','select * from eiel_tr_saneam_ed_pobl where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosDepuradora1','delete from eiel_tr_saneam_ed_pobl where clave_ed=? and codprov_ed=? and codmunic_ed=? and orden_ed=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetVertedero','select clave, codprov,codmunic,orden_vt from eiel_c_vt where id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosVertedero','select * from eiel_tr_vt_pobl where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosVertedero','select * from eiel_tr_vt_pobl where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosVertedero','delete from eiel_tr_vt_pobl where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelDeposito','select * from eiel_t_abast_de where clave=? and codprov=? and codmunic=? and orden_de=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCentrosEnsenianza','select * from eiel_t_en where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_en=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCentrosSanitarios','select * from eiel_t_sa where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_sa=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelEdificiosSinUso','select * from eiel_t_su where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_su=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelInfoTerminosMunicipales','select * from eiel_t_inf_ttmm where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelInstalacionesDeportivas','select * from eiel_t_id where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelMataderos','select * from eiel_t_mt where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_mt=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPlaneamientoUrbano','select * from eiel_t_planeam_urban where codprov=? and codmunic=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPoblamiento','select * from eiel_t_poblamiento where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelPuntosVertido','select * from eiel_t_saneam_pv where clave=? and codprov=? and codmunic=? and orden_pv=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelTanatorios','select * from eiel_t_ta where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_ta=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelVertederos','select * from eiel_t_vt where clave=? and codprov=? and codmunic=? and orden_vt=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelTratamientosPotabilizacion','select * from eiel_t_abast_tp where clave=? and codprov=? and codmunic=? and orden_tp=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCabildoConsejo','select * from eiel_t_cabildo_consejo where codprov=? and cod_isla=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCarreteras','select * from eiel_t_cabildo_consejo where codprov=? and cod_carrt=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCasasConsistoriales','select * from eiel_t_cc where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_cc=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCementerios','select * from eiel_t_ce where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_ce=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCentrosCulturales','select * from eiel_t_cu where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_cu=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCentrosAsistenciales','select * from eiel_t_as where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_as=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetCentrosIncendios','select * from eiel_t_ip where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_ip=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelCaptacion','select * from eiel_t_abast_ca where clave=? and codprov=? and codmunic=? and orden_ca=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelLonjasMercados','select * from eiel_t_lm where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_lm=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelParquesJardines','select * from eiel_t_pj where clave=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=? and orden_pj=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNucleosEncuestados1','select * from eiel_t_nucl_encuest_1 where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNucleosEncuestados2','select * from eiel_t_nucl_encuest_2 where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNucleosPoblacion','select * from eiel_t_nucleos_poblacion where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelServiciosSaneamiento','select * from eiel_t_saneam_serv where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelServiciosAbastecimiento','select * from eiel_t_abast_serv where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelOtrosServiciosMunicipales','select * from eiel_t_otros_serv_munic where codprov=? and codmunic=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelAbastecimientoAutonomo','select * from eiel_t_abast_au where codprov=? and codmunic=?  and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelEntidadesSingulares','select * from eiel_t_entidad_singular where codprov=? and codmunic=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelTipoInstalacionesDeportivas','select * from eiel_t_id_deportes where eiel_t_id_deportes.clave=? and eiel_t_id_deportes.codprov=? and eiel_t_id_deportes.codmunic=? and eiel_t_id_deportes.codentidad=? and eiel_t_id_deportes.codpoblamiento=? and eiel_t_id_deportes.orden_id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelUsosCentrosCulturales','select * from eiel_t_cu_usos where eiel_t_cu_usos.clave=? and eiel_t_cu_usos.codprov=? and eiel_t_cu_usos.codmunic=? and eiel_t_cu_usos.codentidad=? and eiel_t_cu_usos.codpoblamiento=? and eiel_t_cu_usos.orden_cu=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelUsosCasasConsistoriales','select * from eiel_t_cc_usos where eiel_t_cc_usos.clave=? and eiel_t_cc_usos.codprov=? and eiel_t_cc_usos.codmunic=? and eiel_t_cc_usos.codentidad=? and eiel_t_cc_usos.codpoblamiento=? and eiel_t_cc_usos.orden_cc=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNivelCentrosEnsenianza','select * from eiel_t_en_nivel where eiel_t_en_nivel.clase=? and eiel_t_en_nivel.codprov=? and eiel_t_en_nivel.codmunic=? and eiel_t_en_nivel.codentidad=? and eiel_t_en_nivel.codpoblamiento=? and eiel_t_en_nivel.orden_en=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelServiciosRecogidaBasura','select * from eiel_t_rb_serv where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelRecogidaBasuras','select * from eiel_t_rb where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelSaneamientoAutonomo','select * from eiel_t_saneam_au where codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelNucleosAbandonados','select * from eiel_t_nucleo_aband where codprov=? and codmunic=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelDepuradora1','select * from eiel_t1_saneam_ed where clave=? and codprov=? and codmunic=? and orden_ed=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPanelDepuradora2','select * from eiel_t2_saneam_ed where clave=? and codprov=? and codmunic=? and orden_ed=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetTramosConduccion','select clave, codprov,codmunic,tramo_cn from eiel_c_abast_tcn where id=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosTramosConduccion','select * from eiel_tr_abast_tcn_pobl where clave_tcn=? and codprov_tcn=? and codmunic_tcn=? and tramo_tcn=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosTramosConduccion','insert into eiel_tr_abast_tcn_pobl (clave_tcn,codprov_tcn, codmunic_tcn, tramo_tcn, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, observ, pmi, pmf, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosTramosConduccion','select * from eiel_tr_abast_tcn_pobl where clave_tcn=? AND codprov_tcn=? and codmunic_tcn=? and tramo_tcn=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosTramosConduccion','delete from eiel_tr_abast_tcn_pobl where clave_tcn=? and codprov_tcn=? and codmunic_tcn=? and tramo_tcn=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerEntidades2','select codentidad from eiel_c_nucleos_puntos where codprov=? and codmunic=? group by codentidad order by codentidad',1,9205);

  INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('MCEIELobtenerEntidades','select codentidad, denominacion from eiel_t_entidad_singular where codprov =? AND codmunic=? group by codentidad,denominacion order by codentidad',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerNucleos3','select codpoblamiento,nombre_oficial from eiel_c_nucleos_puntos where codprov=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerNucleos2','select codpoblamiento,nombre_oficial from eiel_c_nucleos_puntos where codprov=? and codmunic=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerNucleos','select codpoblamiento,nombre_oficial from eiel_c_nucleos_puntos where codprov=? and codmunic=? and codentidad=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosColector','select * from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosColector','insert into eiel_tr_saneam_tcl_pobl (clave_tcl, codprov_tcl, codmunic_tcl, tramo_cl, codprov_pobl, codmunic_pobl, codentidad_pobl, codpoblamiento_pobl, pmi, pmf, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosColector','update eiel_tr_saneam_tcl_pobl set clave_tcl=?, codprov_tcl=?, codmunic_tcl=?, tramo_cl=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, pmi=?, pmf=?, observ=?, fecha_revision=?, estado_revision=? where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosColector','delete from eiel_tr_saneam_tcl_pobl where clave_tcl=? and codprov_tcl=? and codmunic_tcl=? and tramo_cl=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosTramosConduccion','update eiel_tr_abast_tcn_pobl set codprov_tcn=?, codmunic_tcn=?, tramo_tcn=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, observ=?, pmi=?, pmf=?, fecha_revision=?, estado_revision=? where codprov_tcn=? and codmunic_tcn=? and tramo_cn=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosVertedero','insert into eiel_tr_vt_pobl (clave_vt, codprov_vt, codmunic_vt, orden_vt, codprov, codmunic, codentidad, codpoblamiento, observ, fecha_alta, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosVertedero','update eiel_tr_vt_pobl set clave_vt=?, codprov_vt=?, codmunic_vt=?, orden_vt=?, codprov=?, codmunic=?, codentidad=?, codpoblamiento=?, observ=?, fecha_alta=?, fecha_revision=?, estado_revision=? where clave_vt=? and codprov_vt=? and codmunic_vt=? and orden_vt=? and codprov=? and codmunic=? and codentidad=? and codpoblamiento=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELobtenerOrdenPuntosVertido','select orden_pv from eiel_t_saneam_pv where clave=? and codprov=? and codmunic=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetPuntoVertidoEmisario','select *  from eiel_tr_saneam_tem_pv where clave_tem=? and codprov_tem=? and codmunic_tem=? and  tramo_em=? and codprov_pv=? and codmunic_pv=? and clave_pv=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdatePuntoVertidoEmisario','update eiel_tr_saneam_tem_pv set clave_tem=?, codprov_tem=?, codmunic_tem=?, tramo_em=?, codprov_pv=?, codmunic_pv=?, clave_pv=?, pmi=?, pmf=?, observ=?, fecha_revision=?, estado_revision=? where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pv=? and codmunic_pv=? and clave_pv=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaPuntoVertidoEmisario','select * from eiel_tr_saneam_tem_pv where clave_tem=? and codprov_tem=? and codmunic_tem=? and  tramo_em=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertPuntoVertidoEmisario','insert into eiel_tr_saneam_tem_pv (clave_tem, codprov_tem, codmunic_tem, tramo_em, codprov_pv, codmunic_pv, clave_pv,orden_pv, pmi, pmf, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?,?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeletePuntoVertidoEmisario','delete from eiel_tr_saneam_tem_pv where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pv=? and codmunic_pv=? and clave_pv=? and orden_pv=?',1,9205);

  
INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetNucleosEmisario','select *  from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and  tramo_em=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELupdateNucleosEmisario','update eiel_tr_saneam_tem_pobl set clave_tem=?, codprov_tem=?, codmunic_tem=?, tramo_em=?, codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?, pmi=?, pmf=?, observ=?, fecha_revision=?, estado_revision=? where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pobl=?, codmunic_pobl=?, codentidad_pobl=?, codpoblamiento_pobl=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELgetListaNucleosEmisario','select * from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=?',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELinsertNucleosEmisario','insert into eiel_tr_saneam_tem_pobl (clave_tem, codprov_tem, codmunic_tem, tramo_em, codprov_pv, codmunic_pv, clave_pv, pmi, pmf, observ, fecha_revision, estado_revision) values (?,?,?,?,?,?,?,?,?,?, ?,?,?)',1,9205);

INSERT INTO query_catalog ("id", "query", "acl", "idperm")
VALUES
  ('EIELdeleteNucleosEmisario','delete from eiel_tr_saneam_tem_pobl where clave_tem=? and codprov_tem=? and codmunic_tem=? and tramo_em=? and codprov_pobl=? and codmunic_pobl=? and codentidad_pobl=? and codpoblamiento_pobl=?',1,9205);


COMMIT;
