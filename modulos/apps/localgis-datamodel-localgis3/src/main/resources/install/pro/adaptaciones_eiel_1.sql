SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;



DROP FUNCTION IF EXISTS getvaluesusos_cu(text, numeric, text, text, text, text);

CREATE OR REPLACE FUNCTION getvaluesusos_cu(domainname text, idmunicipio numeric, locale text, orden text, codentidad text,  codpoblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,uso,$2::text,$3) from eiel_t_cu_usos where orden_cu=$4 and codprov=substring($2::text,1,2) and codmunic=substring($2::text,3,5) and codentidad=$5 and codpoblamiento=$6 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getvaluesusos_cu(text, numeric, text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos_cu(text, numeric, text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos_cu(text, numeric, text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesusos_cu(text, numeric, text, text, text, text) TO consultas;


DROP FUNCTION IF EXISTS getvaluesusos_cc(text, numeric, text, text, text, text);

CREATE OR REPLACE FUNCTION getvaluesusos_cc(domainname text, idmunicipio numeric, locale text, orden text, codentidad text,  codpoblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,uso,$2::text,$3) from eiel_t_cc_usos where orden_cc=$4 and codprov=substring($2::text,1,2) and codmunic=substring($2::text,3,5) and codentidad=$5 and codpoblamiento=$6 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getvaluesusos_cc(text, numeric, text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos_cc(text, numeric, text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesusos_cc(text, numeric, text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesusos_cc(text, numeric, text, text, text, text) TO consultas;

DROP FUNCTION IF EXISTS getvaluesnivel_en(text, numeric, text, text, text, text);

CREATE OR REPLACE FUNCTION getvaluesnivel_en(domainname text, idmunicipio numeric, locale text, orden text, codentidad text,  codpoblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,nivel,$2::text,$3) || ' - Unidades: ' || unidades || ' - Plazas: ' || plazas from eiel_t_en_nivel where orden_en=$4 and codprov=substring($2::text,1,2) and codmunic=substring($2::text,3,5) and codentidad=$5 and codpoblamiento=$6 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getvaluesnivel_en(text, numeric, text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesnivel_en(text, numeric, text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesnivel_en(text, numeric, text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesnivel_en(text, numeric, text, text, text, text) TO consultas;

DROP FUNCTION IF EXISTS getvaluesdeportes_id(text, numeric, text, text, text, text);

CREATE OR REPLACE FUNCTION getvaluesdeportes_id(domainname text, idmunicipio numeric, locale text, orden text, codentidad text,  codpoblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select getdictionarydescription($1,tipo_deporte,$2::text,$3) from eiel_t_id_deportes where orden_id=$4 and codprov=substring($2::text,1,2) and codmunic=substring($2::text,3,5) and codentidad=$5 and codpoblamiento=$6 and revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getvaluesdeportes_id(text, numeric, text, text, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesdeportes_id(text, numeric, text, text, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getvaluesdeportes_id(text, numeric, text, text, text, text) TO public;
GRANT EXECUTE ON FUNCTION getvaluesdeportes_id(text, numeric, text, text, text, text) TO consultas;

DROP FUNCTION IF EXISTS getnucleo_tcn(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_tcn(idmunicipio numeric, tramo_tcn text, clave_tcn text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_abast_tcn_pobl WHERE eiel_tr_abast_tcn_pobl."clave_tcn" = $3 AND 
eiel_tr_abast_tcn_pobl."codmunic_tcn" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_tr_abast_tcn_pobl."tramo_tcn" = $2 AND  
eiel_tr_abast_tcn_pobl."codprov_tcn" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_tr_abast_tcn_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_abast_tcn_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_abast_tcn_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_tcn(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tcn(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tcn(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_tcn(numeric, text, text) TO consultas;

DROP FUNCTION IF EXISTS getnucleo_tp(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_tp(idmunicipio numeric, orden_tp text, clave text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_abast_tp_pobl WHERE eiel_tr_abast_tp_pobl."clave_tp" = $3 AND 
eiel_tr_abast_tp_pobl."codmunic_tp" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_tr_abast_tp_pobl."orden_tp" = $2 AND  
eiel_tr_abast_tp_pobl."codprov_tp" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_tr_abast_tp_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_abast_tp_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_abast_tp_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_tp(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tp(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tp(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_tp(numeric, text, text) TO consultas;


DROP FUNCTION IF EXISTS getnucleo_tcl(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_tcl(idmunicipio numeric, tramo_cl text, clave text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_saneam_tcl_pobl WHERE eiel_tr_saneam_tcl_pobl."clave_tcl" = $3 AND 
eiel_tr_saneam_tcl_pobl."codmunic_tcl" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_tr_saneam_tcl_pobl."tramo_cl" = $2 AND  
eiel_tr_saneam_tcl_pobl."codprov_tcl" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_tr_saneam_tcl_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_saneam_tcl_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_tcl(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tcl(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tcl(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_tcl(numeric, text, text) TO consultas;


DROP FUNCTION IF EXISTS getnucleo_tem(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_tem(idmunicipio numeric, tramo_em text, clave_tem text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_saneam_tem_pobl WHERE eiel_tr_saneam_tem_pobl."clave_tem" = $3 AND 
eiel_tr_saneam_tem_pobl."codmunic_tem" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_tr_saneam_tem_pobl."tramo_em" = $2 AND  
eiel_tr_saneam_tem_pobl."codprov_tem" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_tr_saneam_tem_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_saneam_tem_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_saneam_tem_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_tem(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tem(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_tem(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_tem(numeric, text, text) TO consultas;


DROP FUNCTION IF EXISTS getnucleo_ed(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_ed(idmunicipio numeric, orden_ed text, clave_ed text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_saneam_ed_pobl WHERE eiel_tr_saneam_ed_pobl."clave_ed" = $3 AND 
eiel_tr_saneam_ed_pobl."codmunic_ed" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_tr_saneam_ed_pobl."orden_ed" = $2 AND  
eiel_tr_saneam_ed_pobl."codprov_ed" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_tr_saneam_ed_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_saneam_ed_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_saneam_ed_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_ed(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ed(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ed(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_ed(numeric, text, text) TO consultas;

DROP FUNCTION IF EXISTS getnucleo_ca(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_ca(idmunicipio numeric, orden_ed text, clave_ed text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_abast_ca_pobl WHERE eiel_tr_abast_ca_pobl."clave_ca" = $3 AND 
eiel_tr_abast_ca_pobl."codmunic_ca" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_tr_abast_ca_pobl."orden_ca" = $2 AND  
eiel_tr_abast_ca_pobl."codprov_ca" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_tr_abast_ca_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_abast_ca_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_abast_ca_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_ca(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ca(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ca(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_ca(numeric, text, text) TO consultas;

DROP FUNCTION IF EXISTS getnucleo_de(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_de(idmunicipio numeric, orden_de text, clave_de text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_abast_de_pobl WHERE eiel_tr_abast_de_pobl."clave_de" = $3 AND 
eiel_tr_abast_de_pobl."codmunic_de" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_tr_abast_de_pobl."orden_de" = $2 AND  
eiel_tr_abast_de_pobl."codprov_de" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_tr_abast_de_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_abast_de_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_abast_de_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_de(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_de(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_de(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_de(numeric, text, text) TO consultas;

DROP FUNCTION IF EXISTS getnucleo_pv(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_pv(idmunicipio numeric, orden_pv text, clave_pv text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_saneam_pv_pobl WHERE eiel_tr_saneam_pv_pobl."clave_pv" = $3 AND 
eiel_tr_saneam_pv_pobl."codmunic_pv" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_tr_saneam_pv_pobl."orden_pv" = $2 AND  
eiel_tr_saneam_pv_pobl."codprov_pv" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_tr_saneam_pv_pobl.codentidad_pobl = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_saneam_pv_pobl.codpoblamiento_pobl = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_saneam_pv_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_pv(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_pv(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_pv(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_pv(numeric, text, text) TO consultas;

DROP FUNCTION IF EXISTS getnucleo_vt(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_vt(idmunicipio numeric, orden_vt text, clave_Vt text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion, eiel_tr_vt_pobl WHERE eiel_tr_vt_pobl."clave_vt" = $3 AND 
eiel_tr_vt_pobl."codmunic_vt" = eiel_c_nucleo_poblacion."codmunic" and eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_tr_vt_pobl."orden_vt" = $2 AND  
eiel_tr_vt_pobl."codprov_vt" = eiel_c_nucleo_poblacion."codprov" and eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_tr_vt_pobl.codentidad = eiel_c_nucleo_poblacion.codentidad AND eiel_tr_vt_pobl.codpoblamiento = eiel_c_nucleo_poblacion.codpoblamiento AND eiel_tr_vt_pobl.revision_expirada=9999999999 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_vt(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_vt(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_vt(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_vt(numeric, text, text) TO consultas;

DROP FUNCTION IF EXISTS getnucleo_ar(numeric, text, text);

CREATE OR REPLACE FUNCTION getnucleo_ar(idmunicipio numeric, entidad text, poblamiento text)
  RETURNS text AS
$BODY$select array_to_string (ARRAY (select nombre_oficial from eiel_c_nucleo_poblacion WHERE eiel_c_nucleo_poblacion.codmunic=substring($1::text,3,5) AND eiel_c_nucleo_poblacion.codprov=substring($1::text,1,2) AND eiel_c_nucleo_poblacion.codentidad = $2 AND eiel_c_nucleo_poblacion.codpoblamiento = $3 AND eiel_c_nucleo_poblacion.revision_expirada=9999999999),', ' );$BODY$
  LANGUAGE sql VOLATILE;
ALTER FUNCTION getnucleo_ar(numeric, text, text)
  OWNER TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ar(numeric, text, text) TO public;
GRANT EXECUTE ON FUNCTION getnucleo_ar(numeric, text, text) TO postgres;
GRANT EXECUTE ON FUNCTION getnucleo_ar(numeric, text, text) TO consultas;
