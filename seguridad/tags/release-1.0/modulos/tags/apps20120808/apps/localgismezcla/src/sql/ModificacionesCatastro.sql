insert into dictionary (id_vocablo, locale, traduccion) values (nextval('seq_dictionary'),'es_ES','S1');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'va_ES','S1');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'eu_ES','S1');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'gl_ES','S1');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'ca_ES','S1');
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain) values (nextval('seq_domainnodes'), 10080, 'S1', currval('seq_dictionary'), 7, 1022);

insert into dictionary (id_vocablo, locale, traduccion) values (nextval('seq_dictionary'),'es_ES','S2');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'va_ES','S2');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'eu_ES','S2');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'gl_ES','S2');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'ca_ES','S2');
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain) values (nextval('seq_domainnodes'), 10080, 'S2', currval('seq_dictionary'), 7, 1022);

insert into dictionary (id_vocablo, locale, traduccion) values (nextval('seq_dictionary'),'es_ES','S3');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'va_ES','S3');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'eu_ES','S3');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'gl_ES','S3');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'ca_ES','S3');
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain) values (nextval('seq_domainnodes'), 10080, 'S3', currval('seq_dictionary'), 7, 1022);

insert into dictionary (id_vocablo, locale, traduccion) values (nextval('seq_dictionary'),'es_ES','S4');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'va_ES','S4');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'eu_ES','S4');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'gl_ES','S4');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'ca_ES','S4');
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain) values (nextval('seq_domainnodes'), 10080, 'S4', currval('seq_dictionary'), 7, 1022);

insert into dictionary (id_vocablo, locale, traduccion) values (nextval('seq_dictionary'),'es_ES','S5');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'va_ES','S5');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'eu_ES','S5');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'gl_ES','S5');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'ca_ES','S5');
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain) values (nextval('seq_domainnodes'), 10080, 'S5', currval('seq_dictionary'), 7, 1022);

insert into dictionary (id_vocablo, locale, traduccion) values (nextval('seq_dictionary'),'es_ES','S6');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'va_ES','S6');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'eu_ES','S6');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'gl_ES','S6');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'ca_ES','S6');
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain) values (nextval('seq_domainnodes'), 10080, 'S6', currval('seq_dictionary'), 7, 1022);

insert into dictionary (id_vocablo, locale, traduccion) values (nextval('seq_dictionary'),'es_ES','S7');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'va_ES','S7');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'eu_ES','S7');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'gl_ES','S7');
insert into dictionary (id_vocablo, locale, traduccion) values (currval('seq_dictionary'),'ca_ES','S7');
insert into domainnodes (id, id_domain, pattern, id_description, type, parentdomain) values (nextval('seq_domainnodes'), 10080, 'S7', currval('seq_dictionary'), 7, 1022);

--Correcciones del modelo de datos



-- Create a temporary table

CREATE LOCAL TEMPORARY TABLE "representante0xpfue" (
  "anno_expediente" NUMERIC(4,0), 
  "referencia_expediente" VARCHAR(13), 
  "codigo_entidad_colaboradora" NUMERIC(3,0), 
  "id_bieninmueble" VARCHAR(20), 
  "nifrepresentante" VARCHAR(9), 
  "razonsocial_representante" VARCHAR(60), 
  "codigo_provincia_ine" NUMERIC(2,0), 
  "codigo_municipio_dgc" NUMERIC(3,0), 
  "codigo_municipio_ine" NUMERIC(3,0), 
  "entidad_menor" VARCHAR(30), 
  "id_via" NUMERIC(8,0), 
  "primer_numero" NUMERIC(4,0), 
  "primera_letra" VARCHAR(1), 
  "segundo_numero" NUMERIC(4,0), 
  "kilometro" NUMERIC(5,2), 
  "segunda_letra" VARCHAR(1), 
  "bloque" VARCHAR(4), 
  "escalera" VARCHAR(2), 
  "planta" VARCHAR(2), 
  "puerta" VARCHAR(2), 
  "direccion_no_estructurada" VARCHAR(25), 
  "codigo_postal" VARCHAR(5), 
  "apartado_correos" NUMERIC(5,0)
) WITH OIDS;

-- Copy the source table's data to the temporary table

INSERT INTO "representante0xpfue" ("anno_expediente", "referencia_expediente", "codigo_entidad_colaboradora", "id_bieninmueble", "nifrepresentante", "razonsocial_representante", "codigo_provincia_ine", "codigo_municipio_dgc", "codigo_municipio_ine", "entidad_menor", "id_via", "primer_numero", "primera_letra", "segundo_numero", "kilometro", "segunda_letra", "bloque", "escalera", "planta", "puerta", "direccion_no_estructurada", "codigo_postal", "apartado_correos")
SELECT "anno_expediente", "referencia_expediente", "codigo_entidad_colaboradora", "id_bieninmueble", "nifrepresentante", "razonsocial_representante", "codigo_provincia_ine", "codigo_municipio_dgc", "codigo_municipio_ine", "entidad_menor", "id_via", "primer_numero", "primera_letra", "segundo_numero", "kilometro", "segunda_letra", "bloque", "escalera", "planta", "puerta", "direccion_no_estructurada", "codigo_postal", "apartado_correos" FROM "public"."representante";

-- Drop the source table

DROP TABLE "public"."representante";

-- Create the destination table

CREATE TABLE "public"."representante" (
  "anno_expediente" NUMERIC(4,0), 
  "referencia_expediente" VARCHAR(13), 
  "codigo_entidad_colaboradora" NUMERIC(3,0), 
  "id_bieninmueble" VARCHAR(20) NOT NULL, 
  "nifrepresentante" VARCHAR(9) NOT NULL, 
  "razonsocial_representante" VARCHAR(60), 
  "codigo_provincia_ine" NUMERIC(2,0), 
  "codigo_municipio_dgc" NUMERIC(3,0), 
  "codigo_municipio_ine" NUMERIC(3,0), 
  "entidad_menor" VARCHAR(30), 
  "id_via" NUMERIC(8,0), 
  "primer_numero" NUMERIC(4,0), 
  "primera_letra" VARCHAR(1), 
  "segundo_numero" NUMERIC(4,0), 
  "kilometro" NUMERIC(5,2), 
  "segunda_letra" VARCHAR(1), 
  "bloque" VARCHAR(4), 
  "escalera" VARCHAR(2), 
  "planta" VARCHAR(3), 
  "puerta" VARCHAR(3),
  "direccion_no_estructurada" VARCHAR(25), 
  "codigo_postal" VARCHAR(5), 
  "apartado_correos" NUMERIC(5,0), 
  CONSTRAINT "representante_pkey" PRIMARY KEY("id_bieninmueble", "nifrepresentante")
) WITH OIDS;


-- Copy the temporary table's data to the destination table

INSERT INTO "public"."representante" ("anno_expediente", "referencia_expediente", "codigo_entidad_colaboradora", "id_bieninmueble", "nifrepresentante", "razonsocial_representante", "codigo_provincia_ine", "codigo_municipio_dgc", "codigo_municipio_ine", "entidad_menor", "id_via", "primer_numero", "primera_letra", "segundo_numero", "kilometro", "segunda_letra", "bloque", "escalera", "planta", "puerta", "direccion_no_estructurada", "codigo_postal", "apartado_correos")
SELECT "anno_expediente", "referencia_expediente", "codigo_entidad_colaboradora", "id_bieninmueble", "nifrepresentante", "razonsocial_representante", "codigo_provincia_ine", "codigo_municipio_dgc", "codigo_municipio_ine", "entidad_menor", "id_via", "primer_numero", "primera_letra", "segundo_numero", "kilometro", "segunda_letra", "bloque", "escalera", CAST("planta" AS varchar(3)), "puerta", "direccion_no_estructurada", "codigo_postal", "apartado_correos" FROM "representante0xpfue";

-- Modificaciones en el modelo de datos de la tabla vias, se modifica la longitud del tipodevianormalizadocatastro de 2 a 5
ALTER TABLE vias ALTER tipovianormalizadocatastro TYPE varchar(5);


update municipios set id_catastro=municipios.id_ine;
update municipios set id_catastro=900 where id='16078';
update query_catalog set query='select distinct substring(bien_inmueble.numero_fincaregistral,3,3) as numero_fincaregistral from bien_inmueble inner join municipios on (bien_inmueble.codigo_municipiodgc=municipios.id_catastro and numero_fincaregistral is not null) where municipios.id_provincia=?' where id='MCobtenerListaCodigoRegistro';

--Elimina las limitaciones de rutas al generar el fin de entrada y el varpad
ALTER TABLE ficheros ALTER nombre TYPE varchar(200);
ALTER TABLE ficheros ALTER url TYPE varchar(250);

-- Correcciones en las validaciones

--INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCobtenerDuplicadosConstruccion','select numero_orden_construccion from construccion where numero_orden_construccion=? and parcela_catastral=?,1,9025);

--INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCexisteDuplicadosUC','select codigo_unidad_constructiva from unidad_constructiva where codigo_unidad_constructiva=? and parcela_catastral=?',1,9025);

-- Sentencias de Miriam revisar y corregir
INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCgetCodPoligono','select * from ponencia_poligono where codigo_poligono=?',1,9025);

INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCgetCoefCoordPlan','select coef_coordplan from ponencia_poligono where codigo_poligono=?',1,9025);

INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCbuscaridvia','select id from vias where tipovianormalizadocatastro=? and nombrecatastro=? and codigocatastro=?',1,9025);

INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCobtenerCodViaTramo','select codigo_via from ponencia_tramos where codigo_tramo=?',1,9025);

INSERT INTO QUERY_CATALOG(id,query,acl,idperm) values ('MCcompruebaCodVia','select codigo_via from ponencia_tramos where codigo_via=?',1,9025);

UPDATE QUERY_CATALOG SET query='select id, tipovianormalizadocatastro,nombrecatastro,codigocatastro from vias where id_municipio=? and upper(nombrecatastro) like upper(?)', acl=1,idperm=9025 WHERE id='MCobtenerViasCatastro';

UPDATE QUERY_CATALOG SET query='select id, tipovianormalizadocatastro,nombrecatastro,codigocatastro from vias where id_municipio=? and upper(nombrecatastro) like upper(?) and tipovianormalizadocatastro=?',acl=1,idperm=9025 WHERE id='MCobtenerViasPorTipoCatastro';
-------------------------------------

-- Modificar esta sentencia en base de datos
update query_catalog set query='select * from conservacion where aanormas=?', acl=1, idperm=9025 where id='MCobtenerCoefCorrectorEstadoConservacion';

update query_catalog set query='select anno_normas from cntponurb where anno_aprobacion=? and id_municipio=?', acl=1, idperm=9025 where id='MCobtenerAnnoDeNormasPonencia';

update query_catalog set query='select * from parcelas where codigopoligono=?', acl=1, idperm=9025 where id='MCgetCodigoPoligono';


-- Create a temporary table

CREATE LOCAL TEMPORARY TABLE "ruevaluatorio0vetur" (
  "id_municipio" NUMERIC(5,0), 
  "codigo_delegacionmeh" VARCHAR(2), 
  "codigo_municipio_meh" VARCHAR(3), 
  "codigo_municipio_agregado" VARCHAR(3), 
  "cc" VARCHAR(2), 
  "ip" VARCHAR(2), 
  "tipo" NUMERIC(12,6), 
  "aatipo" NUMERIC(4,0), 
  "ptsconc1" NUMERIC(12,6), 
  "ejeconc1" VARCHAR(1), 
  "ptsconc2" NUMERIC(12,6), 
  "ejeconc2" NUMERIC(4,0), 
  "ptsconc3" NUMERIC(12,6), 
  "ejeconc3" NUMERIC(4,0), 
  "j_teoricas" NUMERIC(8,2), 
  "exento" VARCHAR(1), 
  "condonado" VARCHAR(1), 
  "condonado_jt" VARCHAR(1), 
  "vuelo" VARCHAR(1), 
  "usuario" VARCHAR(12), 
  "modificacion" DATE, 
  "desde" DATE, 
  "tipo_movimiento" VARCHAR(1), 
  "motivo_movimiento" VARCHAR(4), 
  "estado" VARCHAR(1), 
  "ipp" NUMERIC(2,0), 
  "tipo_ant" NUMERIC(12,6)
) WITH OIDS;

-- Copy the source table's data to the temporary table

INSERT INTO "ruevaluatorio0vetur" ("id_municipio", "codigo_delegacionmeh", "codigo_municipio_meh", "codigo_municipio_agregado", "cc", "ip", "tipo", "aatipo", "ptsconc1", "ejeconc1", "ptsconc2", "ejeconc2", "ptsconc3", "ejeconc3", "j_teoricas", "exento", "condonado", "condonado_jt", "vuelo", "usuario", "modificacion", "desde", "tipo_movimiento", "motivo_movimiento", "estado", "ipp", "tipo_ant")
SELECT "id_municipio", "codigo_delegacionmeh", "codigo_municipio_meh", "codigo_municipio_agregado", "cc", "ip", "tipo", "aatipo", "ptsconc1", "ejeconc1", "ptsconc2", "ejeconc2", "ptsconc3", "ejeconc3", "j_teoricas", "exento", "condonado", "condonado_jt", "vuelo", "usuario", "modificacion", "desde", "tipo_movimiento", "motivo_movimiento", "estado", "ipp", "tipo_ant" FROM "public"."ruevaluatorio";

-- Drop the source table

DROP TABLE "public"."ruevaluatorio";

-- Create the destination table

CREATE TABLE "public"."ruevaluatorio" (
  "id_municipio" NUMERIC(5,0) NOT NULL, 
  "codigo_delegacionmeh" VARCHAR(2), 
  "codigo_municipio_meh" VARCHAR(3), 
  "codigo_municipio_agregado" VARCHAR(3), 
  "cc" VARCHAR(2), 
  "ip" NUMERIC(2,0), 
  "tipo" NUMERIC(12,6), 
  "aatipo" NUMERIC(4,0), 
  "ptsconc1" NUMERIC(12,6), 
  "ejeconc1" VARCHAR(1), 
  "ptsconc2" NUMERIC(12,6), 
  "ejeconc2" NUMERIC(4,0), 
  "ptsconc3" NUMERIC(12,6), 
  "ejeconc3" NUMERIC(4,0), 
  "j_teoricas" NUMERIC(8,2), 
  "exento" VARCHAR(1), 
  "condonado" VARCHAR(1), 
  "condonado_jt" VARCHAR(1), 
  "vuelo" VARCHAR(1), 
  "usuario" VARCHAR(12), 
  "modificacion" DATE, 
  "desde" DATE, 
  "tipo_movimiento" VARCHAR(1), 
  "motivo_movimiento" VARCHAR(4), 
  "estado" VARCHAR(1), 
  "ipp" NUMERIC(2,0), 
  "tipo_ant" NUMERIC(12,6)
) WITH OIDS;


-- Copy the temporary table's data to the destination table

INSERT INTO "public"."ruevaluatorio" ("id_municipio", "codigo_delegacionmeh", "codigo_municipio_meh", "codigo_municipio_agregado", "cc", "ip", "tipo", "aatipo", "ptsconc1", "ejeconc1", "ptsconc2", "ejeconc2", "ptsconc3", "ejeconc3", "j_teoricas", "exento", "condonado", "condonado_jt", "vuelo", "usuario", "modificacion", "desde", "tipo_movimiento", "motivo_movimiento", "estado", "ipp", "tipo_ant")
SELECT "id_municipio", "codigo_delegacionmeh", "codigo_municipio_meh", "codigo_municipio_agregado", "cc", CAST(TRIM("ip") AS numeric(2,0)), "tipo", "aatipo", "ptsconc1", "ejeconc1", "ptsconc2", "ejeconc2", "ptsconc3", "ejeconc3", "j_teoricas", "exento", "condonado", "condonado_jt", "vuelo", "usuario", "modificacion", "desde", "tipo_movimiento", "motivo_movimiento", "estado", "ipp", "tipo_ant" FROM "ruevaluatorio0vetur";

-- Modificacion de obtención de ruevaluatorio

update query_catalog set query='select id_municipio from ruevaluatorio where codigo_delegacionmeh=? AND codigo_municipio_meh=? AND cc=? AND ipp=?', acl=1,idperm=9025 where id ='MCgetTipoEvaluatorio'; 

   