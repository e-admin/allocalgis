
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
CREATE OR REPLACE RULE insert_com_autonomas AS ON INSERT TO com_autonomas DO INSTEAD INSERT INTO ccaa (niden_ccaa, ccaa, d_ccaa, num_prov, num_muni, poblacion_2001, pob_varones_2001, pob_mujeres_2001, "GEOMETRY", area, length, fdoc, fgra) VALUES (nextval('seq_ccaa'), currval('seq_ccaa'), NEW.id, NEW.num_prov, NEW.num_muni, NEW.poblacion_2001, NEW.pob_varones_2001, NEW.pob_mujeres_2001, NEW."GEOMETRY", NEW.area, NEW.length, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));
CREATE OR REPLACE RULE update_com_autonomas AS ON UPDATE TO com_autonomas DO INSTEAD UPDATE ccaa SET d_ccaa=NEW.id, num_prov= NEW.num_prov, num_muni= NEW.num_muni, poblacion_2001= NEW.poblacion_2001, pob_varones_2001=NEW.pob_varones_2001, pob_mujeres_2001=NEW.pob_mujeres_2001, "GEOMETRY"= NEW."GEOMETRY", area= NEW.area, length= NEW.length, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE d_ccaa=OLD.id;
CREATE OR REPLACE RULE delete_com_autonomas AS ON DELETE TO com_autonomas DO INSTEAD DELETE FROM ccaa WHERE d_ccaa=OLD.id;



CREATE OR REPLACE RULE insert_provincias AS ON INSERT TO provincias DO INSTEAD INSERT INTO PROVINCIA (niden_cpro, niden_ccaa, cpro, d_prov, dc_prov, dfi_prov, dn_prov, nombrecooficial, comunidad, "GEOMETRY", area, length, fdoc, fgra) 
VALUES (nextval('seq_provincia'),(SELECT NIDEN_CCAA FROM CCAA WHERE UPPER(D_CCAA)=UPPER(NEW.comunidad)), NEW.id::int, NEW.nombreoficial, NEW.nombreoficialcorto,'', '', NEW.nombrecooficial, NEW.comunidad, NEW."GEOMETRY", NEW.area, NEW.length, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_provincias AS ON UPDATE TO provincias DO INSTEAD UPDATE PROVINCIA SET NIDEN_CCAA=(SELECT NIDEN_CCAA FROM CCAA WHERE UPPER(D_CCAA)=UPPER(NEW.comunidad)), CPRO=NEW.id::integer, D_PROV=NEW.nombreoficial, DC_PROV=NEW.nombreoficialcorto, nombrecooficial=NEW.nombrecooficial, comunidad=NEW.comunidad, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE CPRO=OLD.id::integer;

CREATE OR REPLACE RULE delete_provincias AS ON DELETE TO provincias DO INSTEAD DELETE FROM PROVINCIA WHERE cpro=OLD.id::integer;


CREATE OR REPLACE RULE insert_municipios AS ON INSERT TO municipios DO INSTEAD INSERT INTO MUNICIPIO (niden_cmun, niden_cpro, idioma, niden_cmun_ine, cpro, cmun, d_cmun, dc_cmun, dfi_mun, dn_mun, cmun_cat, confirmado, id_mhacienda, nombrecooficial, "GEOMETRY", area, length, srid, srid_proyeccion, fdoc, fgra) 
VALUES (nextval('seq_municipio'), (SELECT niden_cpro FROM provincia WHERE cpro =NEW.id_provincia::integer), 1 , NEW.id, NEW.id_provincia::integer, trim(NEW.id_ine)::integer, NEW.nombreoficial, NEW.nombreoficialcorto, '', '', NEW.id_catastro::int, 0, NEW.id_mhacienda, NEW.nombrecooficial, NEW."GEOMETRY", NEW.area, NEW.length, NEW.srid, NEW.srid_proyeccion, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_municipios AS ON UPDATE TO municipios DO INSTEAD UPDATE MUNICIPIO SET niden_cpro=(SELECT niden_cpro FROM provincia WHERE cpro=NEW.id_provincia::integer), idioma=1 , cpro=NEW.id_provincia::integer, CMUN=trim(NEW.id_ine)::integer, D_CMUN=NEW.nombreoficial, DC_CMUN=NEW.nombreoficialcorto, CMUN_CAT=NEW.id_catastro::int, id_mhacienda=NEW.id_mhacienda, nombrecooficial=NEW.nombrecooficial, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, srid=NEW.srid, srid_proyeccion=NEW.srid_proyeccion, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999')  WHERE niden_cmun_ine=OLD.id;

CREATE OR REPLACE RULE delete_municipios AS ON DELETE TO municipios DO INSTEAD DELETE FROM MUNICIPIO WHERE niden_cmun_ine=OLD.id;


CREATE OR REPLACE RULE insert_entidadescolectivas AS ON INSERT TO entidadescolectivas DO INSTEAD INSERT INTO ECOLECTIVA (NIDEN_CENTCO, NIDEN_CMUN, id_municipio, CENTCO, D_CENTCO, DC_CENTCO, nombrecooficial, descripcion, "GEOMETRY", area, length, CPRO,CMUN, DFI_CENTCO, DN_CENTCO, fdoc, fgra) 
VALUES (nextval('seq_entidadescolectivas'), (Select niden_cmun from municipio where niden_cmun_ine=NEW.id_municipio), New.id_municipio, trim(NEW.codigoine)::int, NEW.nombreoficial, NEW.nombreoficialcorto, NEW.nombrecooficial, NEW.descripcion, NEW."GEOMETRY", NEW.area, NEW.length, substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, '','', to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_ecolectivas AS ON UPDATE TO entidadescolectivas
DO INSTEAD UPDATE ECOLECTIVA SET NIDEN_CMUN=(Select niden_cmun from municipio where niden_cmun_ine=NEW.id_municipio), id_municipio=NEW.id_municipio, CENTCO=trim(NEW.codigoine)::int, D_CENTCO=NEW.nombreoficial, DC_CENTCO=NEW.nombreoficialcorto, nombrecooficial=NEW.nombrecooficial, descripcion=NEW.descripcion, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, CPRO=substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, CMUN=substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CENTCO=OLD.id;

CREATE OR REPLACE RULE delete_ecolectivas AS ON DELETE TO entidadescolectivas DO INSTEAD DELETE FROM ECOLECTIVA WHERE NIDEN_CENTCO =OLD.id;

CREATE OR REPLACE RULE insert_esingular AS ON INSERT TO entidadessingulares DO INSTEAD INSERT INTO ESINGULAR (NIDEN_CENTSI, id_municipio, CENTSI, D_CENTSI, DC_CENTSI, nombrecooficial, descripcion, "GEOMETRY", area, length,CPRO,CMUN,CENTCO, fdoc, fgra)  
VALUES (nextval('seq_entidadessingulares'), NEW.id_municipio, NEW.codigoine::int, NEW.nombreoficial, NEW.nombreoficialcorto, NEW.nombrecooficial, NEW.descripcion, NEW."GEOMETRY", NEW.area, NEW.length, substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, NEW.codigo_entidadcolectiva::int, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_esingular AS ON UPDATE TO entidadessingulares DO INSTEAD UPDATE ESINGULAR SET id_municipio=NEW.id_municipio, CENTSI=NEW.codigoine::int, D_CENTSI=NEW.nombreoficial, DC_CENTSI=NEW.nombreoficialcorto,  nombrecooficial=NEW.nombrecooficial, descripcion=NEW.descripcion, "GEOMETRY"= NEW."GEOMETRY", area= NEW.area, length= NEW.length, CENTCO=NEW.codigo_entidadcolectiva::int, CPRO=substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, CMUN=substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CENTSI=OLD.id;

CREATE OR REPLACE RULE delete_esingular AS ON DELETE TO entidadessingulares DO INSTEAD DELETE FROM ESINGULAR WHERE NIDEN_CENTSI=OLD.id;

CREATE OR REPLACE RULE insert_nucleo AS ON INSERT TO nucleos_y_diseminados DO INSTEAD INSERT INTO NUCLEO (NIDEN_CNUCLE, NIDEN_CENTSI, CNUCLE, D_CNUCLE, DC_CNUCLE, nombrecooficial, descripcion, numhabitantes, tipo, "GEOMETRY", area, length, CENTSI,CPRO,CMUN,CENTCO, fdoc, fgra)  
VALUES (nextval('seq_nucleos_y_diseminados'), NEW.id_entidadsingular, trim(NEW.codigoine)::int, NEW.nombreoficial, NEW.nombreoficialcorto, NEW.nombrecooficial, NEW.descripcion, NEW.numhabitantes, NEW.tipo, NEW."GEOMETRY", NEW.area, NEW.length, (SELECT CENTSI FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), (SELECT CPRO FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), (SELECT CMUN FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), (SELECT CENTCO FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_nucleo AS ON UPDATE TO nucleos_y_diseminados DO INSTEAD UPDATE NUCLEO SET NIDEN_CENTSI=NEW.id_entidadsingular, CNUCLE=trim(NEW.codigoine)::int, D_CNUCLE=NEW.nombreoficial, DC_CNUCLE=NEW.nombreoficialcorto, nombrecooficial=NEW.nombrecooficial, numhabitantes=NEW.numhabitantes, descripcion=NEW.descripcion, tipo=NEW.tipo, "GEOMETRY"= NEW."GEOMETRY", area=NEW.area, length=NEW.length, CENTCO=(SELECT CENTCO FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), CENTSI=(SELECT CENTSI FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), CPRO=(SELECT CPRO FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), CMUN=(SELECT CMUN FROM ESINGULAR WHERE NIDEN_CENTSI = NEW.id_entidadsingular), fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999')  WHERE NIDEN_CNUCLE=OLD.id;

CREATE OR REPLACE RULE delete_nucleo AS ON DELETE TO nucleos_y_diseminados DO INSTEAD DELETE FROM NUCLEO WHERE NIDEN_CNUCLE=OLD.id;


CREATE OR REPLACE RULE insert_distrito AS ON INSERT TO distritoscensales DO INSTEAD INSERT INTO DISTRITO (NIDEN_CDIS, D_CDIS, CDIS, id_municipio, "GEOMETRY", area, length, CPRO,CMUN, NIDEN_CMUN, fdoc, fgra) 
VALUES (nextval('seq_distritoscensales'), NEW.nombre, trim(NEW.codigoine)::int, NEW.id_municipio, NEW."GEOMETRY", NEW.area, NEW.length, substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, (select niden_cmun from municipio where niden_cmun_ine=NEW.id_municipio), to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_distrito AS ON UPDATE TO distritoscensales DO INSTEAD UPDATE DISTRITO SET D_CDIS=NEW.nombre, CDIS=trim(NEW.codigoine)::int, id_municipio=NEW.id_municipio, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, CPRO=substr(lpad(NEW.id_municipio::varchar,5,'0'),1,2)::int, CMUN=substr(lpad(NEW.id_municipio::varchar,5,'0'),3,5)::int, NIDEN_CMUN=(select niden_cmun from municipio where niden_cmun_ine=NEW.id_municipio), fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CDIS = OLD.id;

CREATE OR REPLACE RULE delete_distrito AS ON DELETE TO distritoscensales DO INSTEAD DELETE FROM DISTRITO WHERE NIDEN_CDIS =OLD.id;

CREATE OR REPLACE RULE insert_seccion AS ON INSERT TO seccionescensales DO INSTEAD INSERT INTO SECCION (NIDEN_CSEC, NIDEN_CDIS, CSEC, "GEOMETRY", nombre, area, length, CPRO,CMUN,CDIS, fdoc, fgra) 
VALUES (NEXTVAL('seq_seccionescensales'), NEW.id_distrito, trim(NEW.codigoine)::int, NEW."GEOMETRY", NEW.nombre, NEW.area, NEW.length, (SELECT CPRO FROM DISTRITO  WHERE NIDEN_CDIS = NEW.id_distrito), (SELECT CMUN FROM DISTRITO  WHERE NIDEN_CDIS = NEW.id_distrito), (SELECT CDIS FROM DISTRITO WHERE NIDEN_CDIS = NEW.id_distrito), to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_seccion AS ON UPDATE TO seccionescensales DO INSTEAD UPDATE SECCION SET NIDEN_CDIS=NEW.id_distrito, CSEC=trim(NEW.codigoine)::int, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, CPRO=(SELECT CPRO FROM DISTRITO WHERE NIDEN_CDIS=NEW.id_distrito), CMUN=(SELECT CMUN FROM DISTRITO WHERE NIDEN_CDIS=NEW.id_distrito), CDIS=(SELECT CDIS FROM DISTRITO WHERE NIDEN_CDIS=NEW.id_distrito) WHERE NIDEN_CSEC=OLD.id;

CREATE OR REPLACE RULE delete_seccion AS ON DELETE TO seccionescensales  DO INSTEAD DELETE FROM SECCION WHERE NIDEN_CSEC=OLD.id;

CREATE OR REPLACE RULE insert_subseccion AS ON INSERT TO subseccionescensales DO INSTEAD INSERT INTO SUBSECCION (NIDEN_CSUB, CSUB, NIDEN_CSEC, "GEOMETRY", area, length, CPRO,CMUN,CDIS,CSEC, fdoc, fgra) 
VALUES (nextval('seq_subseccionescensales'), NEW.codigoine::int, NEW.id_seccion, NEW."GEOMETRY", NEW.area, NEW.length, (SELECT CPRO FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), (SELECT CMUN FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), (SELECT CDIS FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), (SELECT CSEC FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_subseccion AS ON UPDATE TO subseccionescensales DO INSTEAD UPDATE SUBSECCION SET CSUB=NEW.codigoine::int, NIDEN_CSEC=NEW.id_seccion, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, CPRO=(SELECT CPRO FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), CMUN=(SELECT CMUN FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), CDIS=(SELECT CDIS FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), CSEC=(SELECT CSEC FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999')  WHERE NIDEN_CSUB=OLD.id;

CREATE OR REPLACE RULE delete_subseccion AS ON DELETE TO subseccionescensales DO INSTEAD DELETE FROM SUBSECCION WHERE NIDEN_CSUB=OLD.id;


CREATE OR REPLACE RULE insert_via AS ON INSERT TO vias DO INSTEAD INSERT INTO VIA (NIDEN_CVIA, CVIA, CVIA_DGC, tipovianormalizadocatastro, TVIA, posiciontipovia, D_VIA, DC_VIA, nombrecatastro, id_municipio, "GEOMETRY", length, idalp, fechagrabacionayto, fechagrabacioncierre, fechaejecucion, fuente, valido, tipo, NIDEN_CMUN, CPRO, CMUN, CLASE, CONFIRMADO, fdoc, fgra) 
VALUES (nextval('seq_vias'), NEW.codigoine, NEW.codigocatastro, NEW.tipovianormalizadocatastro, NEW.tipoviaine, NEW.posiciontipovia, NEW.nombreviaine, NEW.nombreviacortoine, NEW.nombrecatastro, NEW.id_municipio, NEW."GEOMETRY", NEW.length, NEW.idalp, NEW.fechagrabacionayto, NEW.fechagrabacioncierre, NEW.fechaejecucion, NEW.fuente, NEW.valido, NEW.tipo, (SELECT NIDEN_CMUN FROM MUNICIPIO WHERE MUNICIPIO.NIDEN_CMUN_INE=NEW.id_municipio), substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, 'V', 0, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_via AS ON UPDATE TO vias DO INSTEAD UPDATE VIA SET CVIA=NEW.codigoine, CVIA_DGC=NEW.codigocatastro, tipovianormalizadocatastro=NEW.tipovianormalizadocatastro, TVIA=NEW.tipoviaine, posiciontipovia=NEW.posiciontipovia, D_VIA=NEW.nombreviaine, DC_VIA=NEW.nombreviacortoine, nombrecatastro=NEW.nombrecatastro, id_municipio=NEW.id_municipio, "GEOMETRY"=NEW."GEOMETRY", length=NEW.length, idalp=NEW.idalp, fechagrabacionayto=NEW.fechagrabacionayto, fechagrabacioncierre=NEW.fechagrabacioncierre, fechaejecucion=NEW.fechaejecucion, fuente=NEW.fuente, valido=NEW.valido, tipo=NEW.tipo, NIDEN_CMUN=(SELECT NIDEN_CMUN FROM MUNICIPIO WHERE MUNICIPIO.NIDEN_CMUN_INE=NEW.id_municipio), CPRO= substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, CMUN= substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, CLASE='V', CONFIRMADO=0, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CVIA = OLD.id;

CREATE OR REPLACE RULE delete_via AS ON DELETE TO vias DO INSTEAD DELETE FROM VIA WHERE NIDEN_CVIA=OLD.id;


CREATE OR REPLACE RULE insert_pseudovia AS ON INSERT TO pseudovias DO INSTEAD INSERT INTO VIA (NIDEN_CVIA, CVIA, D_VIA, DC_VIA, id_municipio, "GEOMETRY", length, NIDEN_CMUN, CPRO, CMUN, CLASE, CONFIRMADO, fdoc, fgra) 
VALUES (new.id, NEW.codigoine, NEW.descripcion, NEW.descripcion, NEW.id_municipio, NEW."GEOMETRY", NEW.length, (SELECT NIDEN_CMUN FROM MUNICIPIO WHERE MUNICIPIO. NIDEN_CMUN_INE=NEW.id_municipio), substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, 'P', 0, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_pseudovia AS ON UPDATE TO pseudovias DO INSTEAD UPDATE VIA SET CVIA=NEW.codigoine, D_VIA=NEW.descripcion, DC_VIA=NEW.descripcion, id_municipio=NEW.id_municipio, "GEOMETRY"=NEW."GEOMETRY", length=NEW.length, NIDEN_CMUN=(SELECT NIDEN_CMUN FROM MUNICIPIO WHERE MUNICIPIO. NIDEN_CMUN_INE=NEW.id_municipio), CPRO=substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, CMUN=substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, CLASE='P', CONFIRMADO=0, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CVIA = OLD.id;

CREATE OR REPLACE RULE delete_pseudovia AS ON DELETE TO pseudovias DO INSTEAD DELETE FROM VIA WHERE NIDEN_CVIA=OLD.id;



CREATE OR REPLACE RULE insert_numeros AS ON INSERT TO numeros_policia DO INSTEAD INSERT INTO APP (NIDEN_APP, rotulo, id_municipio, NIDEN_CVIA, fechaalta, fechabaja, "GEOMETRY", idalp, calificador_localgis, numero_localgis, fechaejecucion, parcela, fuente, valido, fdoc, fgra) VALUES (nextval('seq_numeros_policia'), new.rotulo, new.id_municipio, new.id_via, new.fechaalta, new.fechabaja, new."GEOMETRY", new.idalp, new.calificador, new.numero, new.fechaejecucion, new.parcela, new.fuente, new.valido, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

CREATE OR REPLACE RULE update_numeros AS ON UPDATE TO numeros_policia DO INSTEAD UPDATE APP SET rotulo=new.rotulo, id_municipio=new.id_municipio, NIDEN_CVIA=new.id_via, fechaalta=new.fechaalta, fechabaja=new.fechabaja, "GEOMETRY"=new."GEOMETRY", idalp=new.idalp, calificador_localgis=new.calificador, numero_localgis=new.numero, fechaejecucion=new.fechaejecucion, parcela=new.parcela, fuente=new.fuente, valido=new.valido, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_APP = OLD.id;

CREATE OR REPLACE RULE delete_numeros AS ON DELETE TO numeros_policia DO INSTEAD DELETE FROM APP WHERE NIDEN_APP=OLD.id;


	exception when others then
       RAISE NOTICE 'La tabla ccaa no existe';
END;
$Q$
LANGUAGE plpgsql;
select localgisDatamodelInstall() as "CREATE RULES";

