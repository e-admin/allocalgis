GRANT ALL ON TABLE v_emisario TO geopista;
GRANT SELECT ON TABLE v_emisario TO consultas;

GRANT ALL ON TABLE v_emisario_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_emisario_enc_m50 TO consultas;

GRANT ALL ON TABLE v_captacion_agua TO geopista;
GRANT SELECT ON TABLE v_captacion_agua TO consultas;

GRANT ALL ON TABLE v_captacion_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_captacion_enc_m50 TO consultas;

GRANT ALL ON TABLE v_colector TO geopista;
GRANT SELECT ON TABLE v_colector TO consultas;

GRANT ALL ON TABLE v_colector_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_colector_enc_m50 TO consultas;

GRANT ALL ON TABLE v_conduccion_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_conduccion_enc_m50 TO consultas;

GRANT ALL ON TABLE v_deposito TO geopista;
GRANT SELECT ON TABLE v_deposito TO consultas;

GRANT ALL ON TABLE v_deposito_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_deposito_enc_m50 TO consultas;

GRANT ALL ON TABLE v_depuradora TO geopista;
GRANT SELECT ON TABLE v_depuradora TO consultas;

GRANT ALL ON TABLE v_depuradora_enc_2_m50 TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc_2_m50 TO consultas;

GRANT ALL ON TABLE v_depuradora_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_depuradora_enc_m50 TO consultas;

GRANT ALL ON TABLE v_integ_casaconsistorial TO geopista;
GRANT SELECT ON TABLE v_integ_casaconsistorial TO consultas;

GRANT ALL ON TABLE v_integ_centroasistencial TO geopista;
GRANT SELECT ON TABLE v_integ_centroasistencial TO consultas;

GRANT ALL ON TABLE v_integ_centrocultural TO geopista;
GRANT SELECT ON TABLE v_integ_centrocultural TO consultas;

GRANT ALL ON TABLE v_integ_centroensenianza TO geopista;
GRANT SELECT ON TABLE v_integ_centroensenianza TO consultas;

GRANT ALL ON TABLE v_integ_centrosanitario TO geopista;
GRANT SELECT ON TABLE v_integ_centrosanitario TO consultas;

GRANT ALL ON TABLE v_integ_depuradoras TO geopista;
GRANT SELECT ON TABLE v_integ_depuradoras TO consultas;

GRANT ALL ON TABLE v_integ_edificiosinuso TO geopista;
GRANT SELECT ON TABLE v_integ_edificiosinuso TO consultas;

GRANT ALL ON TABLE v_integ_incendiosproteccion TO geopista;
GRANT SELECT ON TABLE v_integ_incendiosproteccion TO consultas;

GRANT ALL ON TABLE v_integ_lonjasmercados TO geopista;
GRANT SELECT ON TABLE v_integ_lonjasmercados TO consultas;

GRANT ALL ON TABLE v_integ_mataderos TO geopista;
GRANT SELECT ON TABLE v_integ_mataderos TO consultas;

GRANT ALL ON TABLE v_integ_parquesjardines TO geopista;
GRANT SELECT ON TABLE v_integ_parquesjardines TO consultas;

GRANT ALL ON TABLE v_integ_tanatorio TO geopista;
GRANT SELECT ON TABLE v_integ_tanatorio TO consultas;

GRANT ALL ON TABLE v_lonja_merc_feria TO geopista;
GRANT SELECT ON TABLE v_lonja_merc_feria TO consultas;

GRANT ALL ON TABLE v_tramo_colector_m50 TO geopista;
GRANT SELECT ON TABLE v_tramo_colector_m50 TO consultas;

GRANT ALL ON TABLE v_tra_potabilizacion TO geopista;
GRANT SELECT ON TABLE v_tra_potabilizacion TO consultas;

GRANT ALL ON TABLE v_tramo_conduccion_m50 TO geopista;
GRANT SELECT ON TABLE v_tramo_conduccion_m50 TO consultas;

GRANT ALL ON TABLE v_potabilizacion_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_potabilizacion_enc_m50 TO consultas;

GRANT ALL ON TABLE v_tramo_emisario_m50 TO geopista;
GRANT SELECT ON TABLE v_tramo_emisario_m50 TO consultas;

GRANT ALL ON TABLE v_potabilizacion_enc_m50 TO geopista;
GRANT SELECT ON TABLE v_potabilizacion_enc_m50 TO consultas;

CREATE OR REPLACE VIEW v_provincia AS 
SELECT DISTINCT ON (eiel_c_provincia.codprov, eiel_c_provincia.nombre) eiel_c_provincia.codprov AS provincia, eiel_c_provincia.nombre AS denominaci
   FROM eiel_c_provincia
  WHERE eiel_c_provincia.revision_expirada = 9999999999::bigint::numeric;

ALTER TABLE v_provincia
  OWNER TO geopista;
GRANT ALL ON TABLE v_provincia TO geopista;
GRANT SELECT ON TABLE v_provincia TO consultas;