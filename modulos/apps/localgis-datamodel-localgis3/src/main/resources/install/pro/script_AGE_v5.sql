-----------------------------------------------
--TABLA IDIOMAS

DROP TABLE IF EXISTS idioma;
CREATE TABLE idioma (
  idioma NUMERIC(1) NOT NULL,
  d_idioma VARCHAR(40) NOT NULL,
  CONSTRAINT pk_idioma PRIMARY KEY (idioma)
);

--Insercion de valores
INSERT INTO IDIOMA VALUES (1,'Castellano');

--------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--TABLA BLOQUE

DROP TABLE IF EXISTS bloque;
CREATE TABLE BLOQUE(
BLOQUE VARCHAR(2) NOT NULL,
D_BLOQUE VARCHAR(50) NOT NULL,
CONSTRAINT PK_BLOQUE PRIMARY KEY (BLOQUE)
);

--Insercion de valores
INSERT INTO BLOQUE values(' ','');	
INSERT INTO BLOQUE values('nn','Cualquier combinacion valida de 2 digitos');
INSERT INTO BLOQUE values('nA','Cualquier combinacion de numero y letra mayuscula');
INSERT INTO BLOQUE values('A','Cualquier letra mayuscula');
INSERT INTO BLOQUE values('An','Cualquier combinacion de letra mayuscula y numero');
INSERT INTO BLOQUE values('DR','Derecha');
INSERT INTO BLOQUE values('IZ','Izquierda');

--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--TABLA PORTAL

DROP TABLE IF EXISTS portal;
CREATE TABLE PORTAL(
PORTAL VARCHAR(2) NOT NULL,
D_PORTAL VARCHAR(50) NOT NULL,
CONSTRAINT PK_PORTAL PRIMARY KEY (PORTAL)
);

--Insercion de valores.0
INSERT INTO PORTAL VALUES(' ','');	
INSERT INTO PORTAL VALUES('nn','Cualquier combinacion valida de 2 digitos.');
INSERT INTO PORTAL VALUES('nA','Cualquier combinacion de numero y letra mayuscula');
INSERT INTO PORTAL VALUES('A','Cualquier letra mayuscula');
INSERT INTO PORTAL VALUES('An','Cualquier combinacion de letra mayuscula y numero');
INSERT INTO PORTAL VALUES('DR','Derecha');
INSERT INTO PORTAL VALUES('IZ','Izquierda');



--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--TABLA ESCALERA
DROP TABLE IF EXISTS escalera;
CREATE TABLE ESCALERA(
ESCALERA VARCHAR(2) NOT NULL,
D_ESCALERA VARCHAR(50) NOT NULL,
CONSTRAINT PK_ESCALERA PRIMARY KEY (ESCALERA)
);

--Insercion de valores
INSERT INTO ESCALERA values(' ','');	
INSERT INTO ESCALERA values('nn','Cualquier combinacion valida de 2 digitos');
INSERT INTO ESCALERA values('A','Cualquier letra mayuscula');
INSERT INTO ESCALERA values('An','Cualquier combinacion de letra mayuscula y numero');
INSERT INTO ESCALERA values('LL',' ');
INSERT INTO ESCALERA values('CH',' ');
INSERT INTO ESCALERA values('DR','Derecha');
INSERT INTO ESCALERA values('IZ','Izquierda');
INSERT INTO ESCALERA values('CN','Centro');
INSERT INTO ESCALERA values('CD','Centro Derecha');
INSERT INTO ESCALERA values('CE','Centro Exterior Derecha');
INSERT INTO ESCALERA values('CF','Centro Exterior Izquierda');
INSERT INTO ESCALERA values('CI','Centro Izquierda');
INSERT INTO ESCALERA values('CY','Centro Interior Derecha');
INSERT INTO ESCALERA values('CZ','Centro Interior Izquierda');
INSERT INTO ESCALERA values('EX','Exterior');
INSERT INTO ESCALERA values('EC','Exterior Centro');
INSERT INTO ESCALERA values('EI','Exterior Izquierda');
INSERT INTO ESCALERA values('YN','Interior');
INSERT INTO ESCALERA values('YC','Interior Centro');
INSERT INTO ESCALERA values('YD','Interior Derecha');
INSERT INTO ESCALERA values('YI','Interior Izquierda');



--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--TABLA PLANTA
DROP TABLE IF EXISTS planta;
CREATE TABLE PLANTA(
PLANTA VARCHAR(3) NOT NULL,
D_PLANTA VARCHAR(50) NOT NULL,
CONSTRAINT PK_PLANTA PRIMARY KEY (PLANTA)
);

--Insercion de valores
INSERT INTO PLANTA values(' ','');	
INSERT INTO PLANTA values('ALT','ALTILLO');
INSERT INTO PLANTA values('ATI','ATICO');
INSERT INTO PLANTA values('Axx','ATICO xx');
INSERT INTO PLANTA values('ENT','ENTRESUELO');
INSERT INTO PLANTA values('PBE','PLANTA BAJA EXTERIOR');
INSERT INTO PLANTA values('PBI','PLANTA BAJA INTERIOR');
INSERT INTO PLANTA values('PBJ','PLANTA BAJA');
INSERT INTO PLANTA values('PRL','PRINCIPAL');
INSERT INTO PLANTA values('Pxx','PLANTA xx');
INSERT INTO PLANTA values('SOT','SOTANO');
INSERT INTO PLANTA values('SSO','SEMISOTANO');
INSERT INTO PLANTA values('Sxx','SOTANO xx');



--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--TABLA PUERTA
DROP TABLE IF EXISTS puerta;
CREATE TABLE PUERTA(
PUERTA VARCHAR(4) NOT NULL,
D_PUERTA VARCHAR(50) NOT NULL,
CONSTRAINT PK_PUERTA PRIMARY KEY (PUERTA));

--Insrción de valores
INSERT INTO PUERTA values(' ','');	
INSERT INTO PUERTA values('nnnn','Cualquier combinacion valida de 4 digitos');
INSERT INTO PUERTA values('nnA','Con nn cualquier numero de dos digitos');
INSERT INTO PUERTA values('nnB','Con nn cualquier numero de dos digitos');
INSERT INTO PUERTA values('nA','Con n=numero de 1 digito y A=Letra');
INSERT INTO PUERTA values('nDR','Con n cualquier numero de un digito, DR derecha');
INSERT INTO PUERTA values('nIZ','Con n cualquier numero de un digito, IZ izquierda');
INSERT INTO PUERTA values('A','Con A cualquier letra');
INSERT INTO PUERTA values('An','Con A=letra y n=numero de 1 digito');
INSERT INTO PUERTA values('Ann','Con A=letra y nn=numero de dos digitos');
INSERT INTO PUERTA values('AA','Con A cualquier letra de la A a la D');
INSERT INTO PUERTA values('IZ','Izquierda');
INSERT INTO PUERTA values('IZDR','Izquierda Derecha');
INSERT INTO PUERTA values('IZIN','Izquierda Interior');
INSERT INTO PUERTA values('IZEX','Izquierda Exterior');
INSERT INTO PUERTA values('IZCN','Izquierda Centro');
INSERT INTO PUERTA values('IZn','Izquierda numero (n un solo digito)');
INSERT INTO PUERTA values('IZA','Izquierda letra (A cualquier letra)');
INSERT INTO PUERTA values('AIZ','Letra (A cualquier letra) Izquierda ');
INSERT INTO PUERTA values('DR','Derecha');
INSERT INTO PUERTA values('DRIZ','Derecha Izquierda');
INSERT INTO PUERTA values('DRIN','Derecha Interior');
INSERT INTO PUERTA values('DREX','Derecha Exterior');
INSERT INTO PUERTA values('DRCN','Derecha Centro');
INSERT INTO PUERTA values('DRn','Derecha numero (n un solo digito)');
INSERT INTO PUERTA values('DRA','Derecha letra (A cualquier letra)');
INSERT INTO PUERTA values('ADR','Letra (A cualquier letra) Derecha');
INSERT INTO PUERTA values('IN','Interior');
INSERT INTO PUERTA values('INDR','Interior Derecha');
INSERT INTO PUERTA values('INCN','Interior Centro');
INSERT INTO PUERTA values('INn','Interior numero (n un solo digito)');
INSERT INTO PUERTA values('INA','Interior letra (A cualquier letra)');
INSERT INTO PUERTA values('INIZ','Interior Izquierda');
INSERT INTO PUERTA values('AIN','Letra (A cualquier letra) Interior');
INSERT INTO PUERTA values('EX','Exterior');
INSERT INTO PUERTA values('EXDR','Exterior Derecha');
INSERT INTO PUERTA values('EXCN','Exterior Centro');
INSERT INTO PUERTA values('EXn','Exterior numero (n un solo digito)');
INSERT INTO PUERTA values('EXA','Exterior letra (A cualquier letra)');
INSERT INTO PUERTA values('EXIZ','Exterior Izquierda');
INSERT INTO PUERTA values('AEX','Letra (A cualquier letra) Exterior');
INSERT INTO PUERTA values('CN','Centro');
INSERT INTO PUERTA values('CNIZ','Centro Izquierda');
INSERT INTO PUERTA values('CNDR','Centro Derecha');
INSERT INTO PUERTA values('CNDX','Centro Exterior');
INSERT INTO PUERTA values('CNIN','Centro Interior');
INSERT INTO PUERTA values('CNA','Centro letra (A cualquier letra)');
INSERT INTO PUERTA values('ACN','Letra (A cualquier letra) Centro');

-------------------------------------------------
--------------------------------------------------------------------------------------------------
--TABLA TIPOCOLECTIVO

DROP TABLE IF EXISTS tipocolectivo;
CREATE TABLE TIPOCOLECTIVO(
TIPO_COLECTIVO NUMERIC(2) NOT NULL,
D_TIPO_COLECTIVO VARCHAR(50) NOT NULL,
CONSTRAINT PK_TIPOCOLECTIVO PRIMARY KEY (TIPO_COLECTIVO)
);

--Insercion de valores
INSERT INTO TIPOCOLECTIVO values(1,'Hoteles, pensiones, albergues');	
INSERT INTO TIPOCOLECTIVO values(2,'Colegios mayores, residencias de estudiantes');	
INSERT INTO TIPOCOLECTIVO values(3,'Residencias de trabajadores');	
INSERT INTO TIPOCOLECTIVO values(4,'Internados, academias y escuelas militares  .');	
INSERT INTO TIPOCOLECTIVO values(5,'Hospit. generales y especiales de corta estancia');
INSERT INTO TIPOCOLECTIVO values(6,'Hospitales psiquiatricos');	
INSERT INTO TIPOCOLECTIVO values(7,'Hospitales de larga estancia');	
INSERT INTO TIPOCOLECTIVO values(8,'Asilos o residencias de ancianos');	
INSERT INTO TIPOCOLECTIVO values(9,'Instituciones para personas con discapacidades');	
INSERT INTO TIPOCOLECTIVO values(10,'Albergues para marginados sociales');	
INSERT INTO TIPOCOLECTIVO values(11,'Otras instit. asist. social infancia, juventud');
INSERT INTO TIPOCOLECTIVO values(12,'Instit. religiosas (monasterios, abadias)');
INSERT INTO TIPOCOLECTIVO values(13,'Establecimientos militares (cuarteles  .)');	
INSERT INTO TIPOCOLECTIVO values(14,'Instit. penitenciarias (carceles, reformatorios)');	
INSERT INTO TIPOCOLECTIVO values(15,'Colectivo ficticio. Direccion Serv. Sociales');	
INSERT INTO TIPOCOLECTIVO values(16,'Otro tipo de colectivo');	



--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--TABLA TIPOHUECO
DROP TABLE IF EXISTS tipohueco;
CREATE TABLE TIPOHUECO(
TIPO_HUECO VARCHAR(1) NOT NULL,
D_TIPO_HUECO VARCHAR(50) NOT NULL,
CONSTRAINT PK_TIPOHUECO PRIMARY KEY (TIPO_HUECO)
);

--Insercion de valores
INSERT INTO TIPOHUECO values(1,'Vivienda familiar');	
INSERT INTO TIPOHUECO values(2,'Vivienda colectiva (pensiones, residencias, etc.)');	
INSERT INTO TIPOHUECO values(3,'Infraviviendas y cualquier otro alojamiento fijo');
INSERT INTO TIPOHUECO values(4,'Local');	


--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--TABLA TIPOLOCAL
DROP TABLE IF EXISTS tipolocal;
CREATE TABLE TIPOLOCAL(
TIPO_LOCAL VARCHAR(2) NOT NULL,
D_TIPO_LOCAL VARCHAR(50) NOT NULL,
CONSTRAINT PK_TIPOLOCAL PRIMARY KEY (TIPO_LOCAL)
);

--Insercion de valores
INSERT INTO TIPOLOCAL values(2,'Equipamientos de salud');
INSERT INTO TIPOLOCAL values(3,'Equipamientos educativos');
INSERT INTO TIPOLOCAL values(4,'Equipamientos de bienestar social');
INSERT INTO TIPOLOCAL values(5,'Equipamientos culturales o deportivos');
INSERT INTO TIPOLOCAL values(6,'Local comercial');
INSERT INTO TIPOLOCAL values(7,'Oficinas (incluye el resto de los servicios)');
INSERT INTO TIPOLOCAL values(8,'Local industrial');
INSERT INTO TIPOLOCAL values(9,'Local agrario');



--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------
--TABLA DELEGACION
DROP TABLE IF EXISTS delegacion;
CREATE TABLE delegacion (
  niden_deleg numeric(10) NOT NULL,
  niden_cpro numeric(19) not null,
  nedif numeric(2) not null,
  ape1_deleg varchar(33) not null,
  ape2_deleg varchar(33) not null,
  nombre_deleg varchar(33) not null,
  num_telef1 varchar(15),
  num_telef2 varchar(15),
  num_telef3 varchar(15),
  num_fax1 varchar(15),
  num_fax2 varchar(15),
  niden_app_ine numeric(10) not null
);

--Creacion de secuencia
DROP SEQUENCE IF EXISTS seq_delegacion;
CREATE SEQUENCE seq_delegacion INCREMENT 1 START 1;

--Creacion de indices
alter table delegacion add constraint del_niden_deleg_pk primary key (niden_deleg);
create index xif1delegacion on delegacion(niden_cpro);



--------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------

--Anadir los campos de control a las tablas anteriores

Alter table idioma 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table tipovia 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table bloque 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table portal 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table escalera 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table planta 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table puerta 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table tipocolectivo 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table tipohueco 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table tipolocal 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999');

Alter table delegacion 
  add column cvar varchar(1) NOT NULL DEFAULT 'M',
  add column cauv varchar(2) not null default 'AT',
  add column fdoc   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column fgra numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column fbaj   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  add column discrepacia numeric(1) not null default 0,
  add column entramite numeric(1) not null default  0,
  add column expediente_ini varchar(30) not null default ' ',
  add column expediente_fin varchar(30),
  add column usuario varchar(30) not null default ' ',
  add column entidad varchar(20) not null default ' ',
  add column rectificacion varchar(1);

  
  -----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------

--TABLA CCAA

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'com_autonomas') THEN
		ALTER TABLE com_autonomas RENAME TO ccaa;
		ALTER TABLE ccaa 
		  ADD COLUMN niden_ccaa NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN ccaa NUMERIC(2) NOT NULL  DEFAULT 0,
		  ADD COLUMN cod_entidad VARCHAR(21),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8) not null default 0,
		  add column fgra numeric(15) not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1),  
		  DROP CONSTRAINT com_autonomas_pkey CASCADE;

		ALTER TABLE ccaa RENAME COLUMN id TO d_ccaa;
		ALTER TABLE ccaa 
		  ALTER COLUMN d_ccaa TYPE VARCHAR(70),
		  ALTER COLUMN d_ccaa set NOT NULL;

		CREATE SEQUENCE seq_ccaa INCREMENT 1 START 1;

		UPDATE ccaa SET niden_ccaa=NEXTVAL('seq_ccaa');
		update ccaa set ccaa=1  where d_ccaa='ANDALUCIA';
		update ccaa set ccaa=2  where d_ccaa='ARAGON';
		update ccaa set ccaa=3  where d_ccaa='ASTURIAS';
		update ccaa set ccaa=5  where d_ccaa='CANARIAS';
		update ccaa set ccaa=6  where d_ccaa='CANTABRIA';
		update ccaa set ccaa=7  where d_ccaa='CASTILLA-LA MANCHA';
		update ccaa set ccaa=8  where d_ccaa='CASTILLA Y LEON';
		update ccaa set ccaa=9  where d_ccaa='CATALUÃ‘A';
		update ccaa set ccaa=12 where d_ccaa='COMUNIDAD DE MADRID';
		update ccaa set ccaa=17 where d_ccaa='COMUNIDAD VALENCIANA';
		update ccaa set ccaa=10 where d_ccaa='EXTREMADURA';
		update ccaa set ccaa=11 where d_ccaa='GALICIA';
		update ccaa set ccaa=4  where d_ccaa='ISLAS BALEARES';
		update ccaa set ccaa=16 where d_ccaa='LA RIOJA';
		update ccaa set ccaa=13 where d_ccaa='MURCIA';
		update ccaa set ccaa=14 where d_ccaa='NAVARRA';
		update ccaa set ccaa=15 where d_ccaa='PAIS VASCO';


		ALTER TABLE ccaa ADD CONSTRAINT pk_ccaa PRIMARY KEY (niden_ccaa);
		CREATE INDEX ak_ccaa_01 ON ccaa (ccaa);

		CREATE OR REPLACE VIEW com_autonomas AS SELECT ccaa.oid, ccaa.d_ccaa AS id, ccaa.num_prov, ccaa.num_muni, ccaa.poblacion_2001, ccaa.pob_varones_2001, ccaa.pob_mujeres_2001, ccaa."GEOMETRY", ccaa.area, ccaa.length FROM ccaa;

		CREATE RULE insert_com_autonomas AS ON INSERT TO com_autonomas DO INSTEAD INSERT INTO ccaa (niden_ccaa, ccaa, d_ccaa, num_prov, num_muni, poblacion_2001, pob_varones_2001, pob_mujeres_2001, "GEOMETRY", area, length, fdoc, fgra) VALUES (nextval('seq_ccaa'), currval('seq_ccaa'), NEW.id, NEW.num_prov, NEW.num_muni, NEW.poblacion_2001, NEW.pob_varones_2001, NEW.pob_mujeres_2001, NEW."GEOMETRY", NEW.area, NEW.length, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));
		CREATE RULE update_com_autonomas AS ON UPDATE TO com_autonomas DO INSTEAD UPDATE ccaa SET d_ccaa=NEW.id, num_prov= NEW.num_prov, num_muni= NEW.num_muni, poblacion_2001= NEW.poblacion_2001, pob_varones_2001=NEW.pob_varones_2001, pob_mujeres_2001=NEW.pob_mujeres_2001, "GEOMETRY"= NEW."GEOMETRY", area= NEW.area, length= NEW.length, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE d_ccaa=OLD.id;
		CREATE RULE delete_com_autonomas AS ON DELETE TO com_autonomas DO INSTEAD DELETE FROM ccaa WHERE d_ccaa=OLD.id;

			END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla ';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "ccaa";




-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA PROVINCIA
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'provincias') THEN
		ALTER TABLE provincias RENAME TO provincia;

		ALTER TABLE provincia 
		  ADD COLUMN niden_cpro NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN niden_ccaa NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN dfi_prov VARCHAR(25) NOT NULL DEFAULT 0,
		  ADD COLUMN dn_prov VARCHAR(50) NOT NULL DEFAULT 0,
		  ADD COLUMN cod_entidad VARCHAR(21),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8) not null default 0,
		  add column fgra numeric(15) not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1),  
		  DROP CONSTRAINT provincias_pkey CASCADE;
		 

		ALTER TABLE provincia RENAME COLUMN id TO cpro;
		ALTER TABLE provincia
		  ALTER COLUMN cpro TYPE NUMERIC(2) USING cpro::integer,
		  ALTER COLUMN cpro SET NOT NULL;

		ALTER TABLE provincia RENAME COLUMN nombreoficial TO d_prov;
		ALTER TABLE provincia
		  ALTER COLUMN d_prov TYPE VARCHAR(70),
		  ALTER COLUMN d_prov SET NOT NULL;


		ALTER TABLE provincia RENAME COLUMN nombreoficialcorto TO dc_prov;

		UPDATE provincia SET dc_prov='' WHERE dc_prov IS NULL;
		ALTER TABLE PROVINCIA
		  ALTER COLUMN dc_prov TYPE VARCHAR(50),
		  ALTER COLUMN dc_prov SET NOT NULL; 

		CREATE SEQUENCE seq_provincia INCREMENT 1 START 1;

		UPDATE provincia SET niden_cpro=NEXTVAL('seq_provincia');
		UPDATE provincia SET dfi_prov='';
		UPDATE provincia SET dn_prov='';
		update provincia set niden_ccaa=ccaa.niden_ccaa from ccaa where provincia.comunidad = ccaa.d_ccaa;

		ALTER TABLE provincia ADD CONSTRAINT pk_provincia PRIMARY KEY (niden_cpro);
		CREATE INDEX ak_provincia_01 ON provincia(cpro);
		CREATE INDEX xif1provincia ON provincia(niden_ccaa);

		CREATE OR REPLACE VIEW provincias AS SELECT provincia.oid, lpad(provincia.cpro::character varying::text, 2, '0'::text) AS id, provincia.d_prov AS nombreoficial, provincia.dc_prov AS nombreoficialcorto, provincia.nombrecooficial, provincia.comunidad, provincia."GEOMETRY", provincia.area, provincia.length FROM provincia;


		CREATE RULE insert_provincias AS ON INSERT TO provincias DO INSTEAD INSERT INTO PROVINCIA (niden_cpro, niden_ccaa, cpro, d_prov, dc_prov, dfi_prov, dn_prov, nombrecooficial, comunidad, "GEOMETRY", area, length, fdoc, fgra) 
		VALUES (nextval('seq_provincia'),(SELECT NIDEN_CCAA FROM CCAA WHERE UPPER(D_CCAA)=UPPER(NEW.comunidad)), NEW.id::int, NEW.nombreoficial, NEW.nombreoficialcorto,'', '', NEW.nombrecooficial, NEW.comunidad, NEW."GEOMETRY", NEW.area, NEW.length, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_provincias AS ON UPDATE TO provincias DO INSTEAD UPDATE PROVINCIA SET NIDEN_CCAA=(SELECT NIDEN_CCAA FROM CCAA WHERE UPPER(D_CCAA)=UPPER(NEW.comunidad)), CPRO=NEW.id::integer, D_PROV=NEW.nombreoficial, DC_PROV=NEW.nombreoficialcorto, nombrecooficial=NEW.nombrecooficial, comunidad=NEW.comunidad, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE CPRO=OLD.id::integer;

		CREATE RULE delete_provincias AS ON DELETE TO provincias DO INSTEAD DELETE FROM PROVINCIA WHERE cpro=OLD.id::integer;

	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "provincias";






-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA MUNICIPIO

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'municipios') THEN
		ALTER TABLE municipios RENAME TO municipio;

		ALTER TABLE municipio
		  ADD COLUMN niden_cmun  NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN niden_cpro  NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN idioma NUMERIC(1) NOT NULL DEFAULT 0,
		  ADD COLUMN dfi_mun VARCHAR(25) NOT NULL DEFAULT 0,
		  ADD COLUMN dn_mun VARCHAR(50) NOT NULL DEFAULT 0,
		  ADD COLUMN confirmado NUMERIC(1) NOT NULL DEFAULT 0,
		  ADD COLUMN cod_entidad VARCHAR(21),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8) not null default 0,
		  add column fgra numeric(15) not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1),  
		  DROP CONSTRAINT municipios_pkey CASCADE;
		 

		ALTER TABLE municipio RENAME COLUMN id TO niden_cmun_ine;
		ALTER TABLE municipio ALTER COLUMN niden_cmun_ine TYPE NUMERIC(10);

		ALTER TABLE municipio RENAME COLUMN id_provincia TO cpro;
		ALTER TABLE municipio 
		  ALTER COLUMN cpro TYPE NUMERIC(2) USING cpro::integer,
		  ALTER COLUMN cpro set NOT NULL;


		ALTER TABLE municipio RENAME COLUMN id_ine TO cmun;
		UPDATE municipio SET cmun=0 WHERE cmun IS NULL;
		ALTER TABLE municipio 
		  ALTER COLUMN cmun TYPE NUMERIC(3) USING cmun::integer,
		  ALTER COLUMN cmun set NOT NULL;

		ALTER TABLE municipio RENAME COLUMN nombreoficial TO d_cmun;
		ALTER TABLE municipio
		  ALTER COLUMN d_cmun TYPE VARCHAR(70),
		  ALTER COLUMN d_cmun SET NOT NULL;
		  
		ALTER TABLE municipio RENAME COLUMN nombreoficialcorto TO dc_cmun;
		UPDATE municipio SET dc_cmun='' WHERE dc_cmun IS NULL;
		ALTER TABLE municipio
		ALTER COLUMN dc_cmun TYPE VARCHAR(50) ,
		ALTER COLUMN dc_cmun SET NOT NULL; 

		ALTER TABLE municipio RENAME COLUMN id_catastro TO cmun_cat;
		UPDATE municipio SET cmun_cat=0;
		ALTER TABLE municipio
		ALTER COLUMN cmun_cat TYPE NUMERIC(3) USING cmun_cat::integer,
		ALTER COLUMN cmun_cat SET NOT NULL; 


		CREATE SEQUENCE seq_municipio INCREMENT 1 START 1;

		UPDATE municipio SET niden_cpro=provincia.niden_cpro from provincia where municipio.cpro=provincia.cpro;
		UPDATE municipio SET niden_cmun=NEXTVAL('seq_municipio');
		UPDATE municipio SET dn_mun='';
		UPDATE municipio SET dfi_mun='';
		Update municipio set confirmado =0;
		UPDATE municipio SET idioma=1; 

		ALTER TABLE municipio ADD CONSTRAINT pk_municipio PRIMARY KEY (niden_cmun);
		CREATE INDEX ak_municipio_01 ON municipio (cpro,cmun);
		CREATE INDEX xi_municipio_01 ON municipio (idioma);
		CREATE INDEX xif2municipio ON municipio (niden_cpro);


		CREATE OR REPLACE VIEW municipios AS SELECT municipio.oid, municipio.niden_cmun_ine AS id, lpad(municipio.cpro::character varying::text, 2, '0'::text) AS id_provincia, lpad(municipio.cmun_cat::character varying::text, 3, '0'::text) AS id_catastro, municipio.id_mhacienda, lpad(municipio.cmun::character varying::text, 3, '0'::text) AS id_ine, municipio.d_cmun AS nombreoficial, municipio.dc_cmun AS nombreoficialcorto, municipio.nombrecooficial, municipio."GEOMETRY", municipio.area, municipio.length, municipio.srid, municipio.srid_proyeccion FROM municipio;

		CREATE RULE insert_municipios AS ON INSERT TO municipios DO INSTEAD INSERT INTO MUNICIPIO (niden_cmun, niden_cpro, idioma, niden_cmun_ine, cpro, cmun, d_cmun, dc_cmun, dfi_mun, dn_mun, cmun_cat, confirmado, id_mhacienda, nombrecooficial, "GEOMETRY", area, length, srid, srid_proyeccion, fdoc, fgra) 
		VALUES (nextval('seq_municipio'), (SELECT niden_cpro FROM provincia WHERE cpro =NEW.id_provincia::integer), 1 , NEW.id, NEW.id_provincia::integer, trim(NEW.id_ine)::integer, NEW.nombreoficial, NEW.nombreoficialcorto, '', '', NEW.id_catastro::int, 0, NEW.id_mhacienda, NEW.nombrecooficial, NEW."GEOMETRY", NEW.area, NEW.length, NEW.srid, NEW.srid_proyeccion, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_municipios AS ON UPDATE TO municipios DO INSTEAD UPDATE MUNICIPIO SET niden_cpro=(SELECT niden_cpro FROM provincia WHERE cpro=NEW.id_provincia::integer), idioma=1 , cpro=NEW.id_provincia::integer, CMUN=trim(NEW.id_ine)::integer, D_CMUN=NEW.nombreoficial, DC_CMUN=NEW.nombreoficialcorto, CMUN_CAT=NEW.id_catastro::int, id_mhacienda=NEW.id_mhacienda, nombrecooficial=NEW.nombrecooficial, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, srid=NEW.srid, srid_proyeccion=NEW.srid_proyeccion, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999')  WHERE niden_cmun_ine=OLD.id;

		CREATE RULE delete_municipios AS ON DELETE TO municipios DO INSTEAD DELETE FROM MUNICIPIO WHERE niden_cmun_ine=OLD.id;

	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "municipios";


		-----------------------------------------------------------------------------------------------------------
		-----------------------------------------------------------------------------------------------------------
		--TABLA ENTIDADES COLECTIVAS
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'entidadescolectivas') THEN


		ALTER TABLE entidadescolectivas RENAME TO ecolectiva;

		ALTER TABLE ECOLECTIVA
		  ADD COLUMN NIDEN_CMUN NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN NIDEN_CENTCO_INE NUMERIC(10),
		  ADD COLUMN CPRO NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CMUN NUMERIC(3) NOT NULL DEFAULT 0,
		  ADD COLUMN DFI_CENTCO VARCHAR(25) NOT NULL DEFAULT 0,
		  ADD COLUMN DN_CENTCO VARCHAR(50) NOT NULL DEFAULT 0,
		  ADD COLUMN COD_ENTIDAD VARCHAR(21),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8) not null default 0,
		  add column fgra numeric(15) not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1), 
		  DROP CONSTRAINT entidadescolectivas_pkey CASCADE;
		  
		ALTER TABLE ECOLECTIVA RENAME COLUMN id TO NIDEN_CENTCO;
		ALTER TABLE ECOLECTIVA 
		  ALTER COLUMN NIDEN_CENTCO TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CENTCO set NOT NULL;

		ALTER TABLE ECOLECTIVA RENAME COLUMN codigoine TO CENTCO;
		ALTER TABLE ECOLECTIVA 
		  ALTER COLUMN CENTCO TYPE NUMERIC(2) USING CENTCO::integer,
		  ALTER COLUMN CENTCO set NOT NULL;

		ALTER TABLE ECOLECTIVA RENAME COLUMN nombreoficial TO D_CENTCO;
		ALTER TABLE ECOLECTIVA 
		  ALTER COLUMN D_CENTCO TYPE VARCHAR(70),
		  ALTER COLUMN D_CENTCO set NOT NULL;

		ALTER TABLE ECOLECTIVA RENAME COLUMN nombreoficialcorto TO DC_CENTCO;
		ALTER TABLE ECOLECTIVA 
		  ALTER COLUMN DC_CENTCO TYPE VARCHAR(50),
		  ALTER COLUMN DC_CENTCO set NOT NULL;

		UPDATE ECOLECTIVA SET CPRO=substr(lpad(NIDEN_CMUN::varchar, 5, '0'),1,2)::int;
		UPDATE ECOLECTIVA SET CMUN=substr(lpad(id_municipio::varchar, 5, '0'),3,5)::int;
		UPDATE ecolectiva SET niden_cmun=municipio.niden_cmun from municipio where ecolectiva.id_municipio=municipio.niden_cmun_ine;
		UPDATE ecolectiva SET dn_centco='';
		UPDATE ecolectiva SET dfi_centco='';

		ALTER TABLE ECOLECTIVA ADD CONSTRAINT PK_ECOLECTIVA PRIMARY KEY (NIDEN_CENTCO);
		CREATE INDEX AK_ECOLECTIVA_01 ON ECOLECTIVA (CPRO,CMUN,CENTCO);
		CREATE INDEX XIF1ECOLECTIVA ON ECOLECTIVA (NIDEN_CMUN);


		CREATE OR REPLACE VIEW entidadescolectivas AS SELECT ecolectiva.oid, ecolectiva.niden_centco AS id, ecolectiva.id_municipio, lpad(ecolectiva.centco::character varying::text, 2, '0'::text) AS codigoine, ecolectiva.d_centco AS nombreoficial, ecolectiva.dc_centco AS nombreoficialcorto, ecolectiva.nombrecooficial, ecolectiva.descripcion, ecolectiva."GEOMETRY", ecolectiva.area, ecolectiva.length FROM ecolectiva;

		CREATE RULE insert_entidadescolectivas AS ON INSERT TO entidadescolectivas DO INSTEAD INSERT INTO ECOLECTIVA (NIDEN_CENTCO, NIDEN_CMUN, id_municipio, CENTCO, D_CENTCO, DC_CENTCO, nombrecooficial, descripcion, "GEOMETRY", area, length, CPRO,CMUN, DFI_CENTCO, DN_CENTCO, fdoc, fgra) 
		VALUES (nextval('entidadescolectivas'), (Select niden_cmun from municipio where niden_cmun_ine=NEW.id_municipio), New.id_municipio, trim(NEW.codigoine)::int, NEW.nombreoficial, NEW.nombreoficialcorto, NEW.nombrecooficial, NEW.descripcion, NEW."GEOMETRY", NEW.area, NEW.length, substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, '','', to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_ecolectivas AS ON UPDATE TO entidadescolectivas
		DO INSTEAD UPDATE ECOLECTIVA SET NIDEN_CMUN=(Select niden_cmun from municipio where niden_cmun_ine=NEW.id_municipio), id_municipio=NEW.id_municipio, CENTCO=trim(NEW.codigoine)::int, D_CENTCO=NEW.nombreoficial, DC_CENTCO=NEW.nombreoficialcorto, nombrecooficial=NEW.nombrecooficial, descripcion=NEW.descripcion, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, CPRO=substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, CMUN=substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CENTCO=OLD.id;

		CREATE RULE delete_ecolectivas AS ON DELETE TO entidadescolectivas DO INSTEAD DELETE FROM ECOLECTIVA WHERE NIDEN_CENTCO =OLD.id;

	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "entidadescolectivas";



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA ENTIDADES SINGULARES

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'entidadessingulares') THEN
		
		ALTER TABLE entidadessingulares RENAME TO ESINGULAR;

		ALTER TABLE ESINGULAR
		  ADD COLUMN NIDEN_CENTCO NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN NIDEN_CENTSI_INE NUMERIC(10),
		  ADD COLUMN CPRO NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CMUN NUMERIC(3) NOT NULL DEFAULT 0,
		  ADD COLUMN DFI_CENTSI VARCHAR(25) NOT NULL DEFAULT 0,
		  ADD COLUMN DN_CENTSI VARCHAR(50) NOT NULL DEFAULT 0,
		  ADD COLUMN COD_ENTIDAD VARCHAR(21),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8) not null default 0,
		  add column fgra numeric(15) not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1),   
		  DROP CONSTRAINT entidadessingulares_pkey CASCADE;
		  
		ALTER TABLE ESINGULAR RENAME COLUMN id TO NIDEN_CENTSI;
		ALTER TABLE ESINGULAR 
		  ALTER COLUMN NIDEN_CENTSI TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CENTSI set NOT NULL;

		ALTER TABLE ESINGULAR RENAME COLUMN codigo_entidadcolectiva TO CENTCO;
		ALTER TABLE ESINGULAR 
		  ALTER COLUMN CENTCO TYPE NUMERIC(2) USING CENTCO::integer,
		  ALTER COLUMN CENTCO set NOT NULL;

		ALTER TABLE ESINGULAR RENAME COLUMN codigoine TO CENTSI;
		ALTER TABLE ESINGULAR 
		  ALTER COLUMN CENTSI TYPE NUMERIC(2) USING CENTSI::integer,
		  ALTER COLUMN CENTSI set NOT NULL;

		ALTER TABLE ESINGULAR RENAME COLUMN nombreoficial TO D_CENTSI;
		ALTER TABLE ESINGULAR 
		  ALTER COLUMN D_CENTSI TYPE VARCHAR(70),
		  ALTER COLUMN D_CENTSI set NOT NULL;

		ALTER TABLE ESINGULAR RENAME COLUMN nombreoficialcorto TO DC_CENTSI;
		ALTER TABLE ESINGULAR 
		  ALTER COLUMN DC_CENTSI TYPE VARCHAR(50),
		  ALTER COLUMN DC_CENTSI set NOT NULL;

		UPDATE ESINGULAR SET NIDEN_CENTCO=ECOLECTIVA.CENTCO FROM ECOLECTIVA WHERE ESINGULAR.CENTCO=ECOLECTIVA.CENTCO;
		UPDATE ESINGULAR SET CPRO=substr(lpad(id_municipio::varchar, 5, '0'),1,2)::int;
		UPDATE ESINGULAR SET CMUN=substr(lpad(id_municipio::varchar, 5, '0'),3,5)::int;
		UPDATE ESINGULAR SET DFI_CENTSI='';
		UPDATE ESINGULAR SET DN_CENTSI='';

		ALTER TABLE ESINGULAR ADD CONSTRAINT PK_ESINGULAR PRIMARY KEY (NIDEN_CENTSI);
		CREATE INDEX AK_ESINGULAR_01 ON ESINGULAR (CPRO,CMUN,CENTCO,CENTSI);
		CREATE INDEX XIF1ESINGULAR ON ESINGULAR (NIDEN_CENTCO);


		CREATE OR REPLACE VIEW entidadessingulares AS SELECT esingular.oid, esingular.niden_centsi AS id, esingular.id_municipio, lpad(esingular.centsi::character varying::text, 2, '0'::text) AS codigoine, esingular.d_centsi AS nombreoficial, esingular.dc_centsi AS nombreoficialcorto, esingular.nombrecooficial, esingular.descripcion, esingular."GEOMETRY", lpad(esingular.centco::character varying::text, 2, '0'::text) AS codigo_entidadcolectiva, esingular.area, esingular.length FROM esingular;

		CREATE RULE insert_esingular AS ON INSERT TO entidadessingulares DO INSTEAD INSERT INTO ESINGULAR (NIDEN_CENTSI, id_municipio, CENTSI, D_CENTSI, DC_CENTSI, nombrecooficial, descripcion, "GEOMETRY", area, length,CPRO,CMUN,CENTCO, fdoc, fgra)  
		VALUES (nextval('seq_entidadessingulares'), NEW.id_municipio, NEW.codigoine::int, NEW.nombreoficial, NEW.nombreoficialcorto, NEW.nombrecooficial, NEW.descripcion, NEW."GEOMETRY", NEW.area, NEW.length, substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, NEW.codigo_entidadcolectiva::int, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_esingular AS ON UPDATE TO entidadessingulares DO INSTEAD UPDATE ESINGULAR SET id_municipio=NEW.id_municipio, CENTSI=NEW.codigoine::int, D_CENTSI=NEW.nombreoficial, DC_CENTSI=NEW.nombreoficialcorto,  nombrecooficial=NEW.nombrecooficial, descripcion=NEW.descripcion, "GEOMETRY"= NEW."GEOMETRY", area= NEW.area, length= NEW.length, CENTCO=NEW.codigo_entidadcolectiva::int, CPRO=substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, CMUN=substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CENTSI=OLD.id;

		CREATE RULE delete_esingular AS ON DELETE TO entidadessingulares DO INSTEAD DELETE FROM ESINGULAR WHERE NIDEN_CENTSI=OLD.id;


	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "entidadessingulares";



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA NUCLEOS Y DISEMINADOS
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'nucleos_y_diseminados') THEN
		
		ALTER TABLE nucleos_y_diseminados RENAME TO NUCLEO;

		ALTER TABLE NUCLEO
		  ADD COLUMN NIDEN_CNUCLE_INE NUMERIC(10),
		  ADD COLUMN CPRO NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CMUN NUMERIC(3) NOT NULL DEFAULT 0,
		  ADD COLUMN CENTCO NUMERIC(2) DEFAULT 0,
		  ADD COLUMN CENTSI NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN DFI_CNUCLE VARCHAR(25) NOT NULL DEFAULT 0,
		  ADD COLUMN DN_CNUCLE VARCHAR(50) NOT NULL DEFAULT 0,
		  ADD COLUMN COD_ENTIDAD VARCHAR(21),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8) not null default 0,
		  add column fgra numeric(15) not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1),     
		  DROP CONSTRAINT new_index CASCADE;
		  
		ALTER TABLE NUCLEO RENAME COLUMN id TO NIDEN_CNUCLE;
		ALTER TABLE NUCLEO 
		  ALTER COLUMN NIDEN_CNUCLE TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CNUCLE set NOT NULL;

		ALTER TABLE NUCLEO RENAME COLUMN id_entidadsingular TO NIDEN_CENTSI;
		ALTER TABLE NUCLEO 
		  ALTER COLUMN NIDEN_CENTSI TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CENTSI set NOT NULL;

		ALTER TABLE NUCLEO RENAME COLUMN codigoine TO CNUCLE;
		ALTER TABLE NUCLEO 
		  ALTER COLUMN CNUCLE TYPE NUMERIC(2) USING CNUCLE::integer,
		  ALTER COLUMN CNUCLE set NOT NULL;

		ALTER TABLE NUCLEO RENAME COLUMN nombreoficial TO D_CNUCLE;
		ALTER TABLE NUCLEO 
		  ALTER COLUMN D_CNUCLE TYPE VARCHAR(70),
		  ALTER COLUMN D_CNUCLE set NOT NULL;

		ALTER TABLE NUCLEO RENAME COLUMN nombreoficialcorto TO DC_CNUCLE;
		ALTER TABLE NUCLEO 
		  ALTER COLUMN DC_CNUCLE TYPE VARCHAR(50);

			
		UPDATE NUCLEO SET CPRO=ESINGULAR.CPRO FROM ESINGULAR WHERE NUCLEO.NIDEN_CENTSI=ESINGULAR.NIDEN_CENTSI;
		UPDATE NUCLEO SET CMUN=ESINGULAR.CMUN FROM ESINGULAR WHERE NUCLEO.NIDEN_CENTSI=ESINGULAR.NIDEN_CENTSI;
		UPDATE NUCLEO SET CENTCO=ESINGULAR.CENTCO FROM ESINGULAR WHERE NUCLEO.NIDEN_CENTSI=ESINGULAR.NIDEN_CENTSI;
		UPDATE NUCLEO SET CENTSI=ESINGULAR.CENTSI FROM ESINGULAR WHERE NUCLEO.NIDEN_CENTSI=ESINGULAR.NIDEN_CENTSI;
		UPDATE NUCLEO SET DFI_CNUCLE='';
		UPDATE NUCLEO SET DN_CNUCLE='';

		ALTER TABLE NUCLEO ADD CONSTRAINT PK_NUCLEO PRIMARY KEY (NIDEN_CNUCLE);
		CREATE INDEX AK_NUCLEO_01 ON NUCLEO(CPRO,CMUN,CENTCO,CENTSI,CNUCLE);
		CREATE INDEX XI_NUCLEO_01 ON NUCLEO(NIDEN_CENTSI);


		CREATE OR REPLACE VIEW nucleos_y_diseminados AS SELECT nucleo.oid, nucleo.niden_cnucle AS id, nucleo.niden_centsi AS id_entidadsingular, lpad(nucleo.cnucle::character varying::text, 2, '0'::text) AS codigoine, nucleo.d_cnucle AS nombreoficial, nucleo.dc_cnucle AS nombreoficialcorto, nucleo.nombrecooficial, nucleo.descripcion, nucleo.numhabitantes, nucleo.tipo, nucleo."GEOMETRY", nucleo.area, nucleo.length FROM nucleo;

		CREATE RULE insert_nucleo AS ON INSERT TO nucleos_y_diseminados DO INSTEAD INSERT INTO NUCLEO (NIDEN_CNUCLE, NIDEN_CENTSI, CNUCLE, D_CNUCLE, DC_CNUCLE, nombrecooficial, descripcion, numhabitantes, tipo, "GEOMETRY", area, length, CENTSI,CPRO,CMUN,CENTCO, fdoc, fgra)  
		VALUES (nextval('seq_nucleos_y_diseminados'), NEW.id_entidadsingular, trim(NEW.codigoine)::int, NEW.nombreoficial, NEW.nombreoficialcorto, NEW.nombrecooficial, NEW.descripcion, NEW.numhabitantes, NEW.tipo, NEW."GEOMETRY", NEW.area, NEW.length, (SELECT CENTSI FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), (SELECT CPRO FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), (SELECT CMUN FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), (SELECT CENTCO FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_nucleo AS ON UPDATE TO nucleos_y_diseminados DO INSTEAD UPDATE NUCLEO SET NIDEN_CENTSI=NEW.id_entidadsingular, CNUCLE=trim(NEW.codigoine)::int, D_CNUCLE=NEW.nombreoficial, DC_CNUCLE=NEW.nombreoficialcorto, nombrecooficial=NEW.nombrecooficial, numhabitantes=NEW.numhabitantes, descripcion=NEW.descripcion, tipo=NEW.tipo, "GEOMETRY"= NEW."GEOMETRY", area=NEW.area, length=NEW.length, CENTCO=(SELECT CENTCO FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), CENTSI=(SELECT CENTSI FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), CPRO=(SELECT CPRO FROM ESINGULAR WHERE NIDEN_CENTSI=NEW.id_entidadsingular), CMUN=(SELECT CMUN FROM ESINGULAR WHERE NIDEN_CENTSI = NEW.id_entidadsingular), fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999')  WHERE NIDEN_CNUCLE=OLD.id;

		CREATE RULE delete_nucleo AS ON DELETE TO nucleos_y_diseminados DO INSTEAD DELETE FROM NUCLEO WHERE NIDEN_CNUCLE=OLD.id;


	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "nucleos_y_diseminados";




-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA DISTRITOS
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'distritoscensales') THEN

		ALTER TABLE distritoscensales RENAME TO DISTRITO;

		ALTER TABLE DISTRITO
		  ADD COLUMN NIDEN_CMUN NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN NIDEN_CDIS_INE NUMERIC(10),
		  ADD COLUMN CPRO NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CMUN NUMERIC(3) NOT NULL DEFAULT 0,
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8)  not null default 0,
		  add column fgra numeric(15)  not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1),   
		  DROP CONSTRAINT distritoscensales_pkey CASCADE;

		ALTER TABLE DISTRITO RENAME COLUMN id TO NIDEN_CDIS;
		ALTER TABLE DISTRITO 
		  ALTER COLUMN NIDEN_CDIS TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CDIS set NOT NULL;

		ALTER TABLE DISTRITO RENAME COLUMN codigoine TO CDIS;
		ALTER TABLE DISTRITO 
		  ALTER COLUMN CDIS TYPE NUMERIC(2) USING CDIS::integer,
		  ALTER COLUMN CDIS set NOT NULL;

		ALTER TABLE DISTRITO RENAME COLUMN nombre TO D_CDIS;
		ALTER TABLE DISTRITO 
		  ALTER COLUMN D_CDIS TYPE VARCHAR(70),
		  ALTER COLUMN D_CDIS set NOT NULL;

		UPDATE DISTRITO SET CPRO=substr(lpad(id_municipio::varchar, 5, '0'),1,2)::int;
		UPDATE DISTRITO SET CMUN=substr(lpad(id_municipio::varchar, 5, '0'),3,5)::int;
		UPDATE DISTRITO SET NIDEN_CMUN=municipio.niden_cmun from municipio where DISTRITO.id_municipio=municipio.niden_cmun_ine;

		ALTER TABLE DISTRITO ADD CONSTRAINT PK_DISTRITO PRIMARY KEY (NIDEN_CDIS);
		CREATE INDEX AK_DISTRITO_01 ON DISTRITO(CPRO,CMUN,CDIS);
		CREATE INDEX XIF1DISTRITO ON DISTRITO(NIDEN_CMUN);


		CREATE OR REPLACE VIEW distritoscensales AS SELECT distrito.oid, distrito.niden_cdis AS id, distrito.d_cdis AS nombre, lpad(distrito.cdis::character varying::text, 2, '0'::text) AS codigoine, distrito.id_municipio, distrito."GEOMETRY", distrito.area, distrito.length FROM distrito;

		CREATE RULE insert_distrito AS ON INSERT TO distritoscensales DO INSTEAD INSERT INTO DISTRITO (NIDEN_CDIS, D_CDIS, CDIS, id_municipio, "GEOMETRY", area, length, CPRO,CMUN, NIDEN_CMUN, fdoc, fgra) 
		VALUES (nextval('seq_distritoscensales'), NEW.nombre, trim(NEW.codigoine)::int, NEW.id_municipio, NEW."GEOMETRY", NEW.area, NEW.length, substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, (select niden_cmun from municipio where niden_cmun_ine=NEW.id_municipio), to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_distrito AS ON UPDATE TO distritoscensales DO INSTEAD UPDATE DISTRITO SET D_CDIS=NEW.nombre, CDIS=trim(NEW.codigoine)::int, id_municipio=NEW.id_municipio, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, CPRO=substr(lpad(NEW.id_municipio::varchar,5,'0'),1,2)::int, CMUN=substr(lpad(NEW.id_municipio::varchar,5,'0'),3,5)::int, NIDEN_CMUN=(select niden_cmun from municipio where niden_cmun_ine=NEW.id_municipio), fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CDIS = OLD.id;

		CREATE RULE delete_distrito AS ON DELETE TO distritoscensales DO INSTEAD DELETE FROM DISTRITO WHERE NIDEN_CDIS =OLD.id;
	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "distritoscensales";



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA SECCIONES

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'seccionescensales') THEN
		
		ALTER TABLE seccionescensales RENAME TO SECCION;

		ALTER TABLE SECCION
		  ADD COLUMN NIDEN_CSEC_INE NUMERIC(10),
		  ADD COLUMN CPRO NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CMUN NUMERIC(3) NOT NULL DEFAULT 0,
		  ADD COLUMN CDIS NUMERIC(2),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8)  not null default 0,
		  add column fgra numeric(15)  not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1), 
		  DROP CONSTRAINT seccionescensales_pkey CASCADE;

		ALTER TABLE SECCION RENAME COLUMN id TO NIDEN_CSEC;
		ALTER TABLE SECCION 
		  ALTER COLUMN NIDEN_CSEC TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CSEC set NOT NULL;

		ALTER TABLE SECCION RENAME COLUMN id_distrito TO NIDEN_CDIS;
		ALTER TABLE SECCION 
		  ALTER COLUMN NIDEN_CDIS TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CDIS set NOT NULL;

		ALTER TABLE SECCION RENAME COLUMN codigoine TO CSEC;
		ALTER TABLE SECCION 
		  ALTER COLUMN CSEC TYPE NUMERIC(3) USING CSEC::integer,
		  ALTER COLUMN CSEC set NOT NULL;
			
		UPDATE SECCION SET CPRO=DISTRITO.CPRO FROM DISTRITO WHERE DISTRITO.NIDEN_CDIS=SECCION.NIDEN_CDIS;
		UPDATE SECCION SET CMUN=DISTRITO.CMUN FROM DISTRITO WHERE DISTRITO.NIDEN_CDIS=SECCION.NIDEN_CDIS;
		UPDATE SECCION SET CDIS=DISTRITO.CDIS FROM DISTRITO WHERE DISTRITO.NIDEN_CDIS=SECCION.NIDEN_CDIS;

		ALTER TABLE SECCION ADD CONSTRAINT PK_SECCION PRIMARY KEY (NIDEN_CSEC);
		CREATE INDEX AK_SECCION_01 ON SECCION(CPRO,CMUN,CDIS,CSEC);
		CREATE INDEX XI_SECCION_01 ON SECCION(NIDEN_CDIS);


		CREATE OR REPLACE VIEW seccionescensales AS SELECT seccion.oid, seccion.niden_csec AS id, seccion.niden_cdis AS id_distrito, lpad(seccion.csec::character varying::text, 3, '0'::text) AS codigoine, seccion."GEOMETRY", seccion.nombre, seccion.area, seccion.length FROM seccion;

		CREATE RULE insert_seccion AS ON INSERT TO seccionescensales DO INSTEAD INSERT INTO SECCION (NIDEN_CSEC, NIDEN_CDIS, CSEC, "GEOMETRY", nombre, area, length, CPRO,CMUN,CDIS, fdoc, fgra) 
		VALUES (NEXTVAL('seq_seccionescensales'), NEW.id_distrito, trim(NEW.codigoine)::int, NEW."GEOMETRY", NEW.nombre, NEW.area, NEW.length, (SELECT CPRO FROM DISTRITO  WHERE NIDEN_CDIS = NEW.id_distrito), (SELECT CMUN FROM DISTRITO  WHERE NIDEN_CDIS = NEW.id_distrito), (SELECT CDIS FROM DISTRITO WHERE NIDEN_CDIS = NEW.id_distrito), to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_seccion AS ON UPDATE TO seccionescensales DO INSTEAD UPDATE SECCION SET NIDEN_CDIS=NEW.id_distrito, CSEC=trim(NEW.codigoine)::int, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, CPRO=(SELECT CPRO FROM DISTRITO WHERE NIDEN_CDIS=NEW.id_distrito), CMUN=(SELECT CMUN FROM DISTRITO WHERE NIDEN_CDIS=NEW.id_distrito), CDIS=(SELECT CDIS FROM DISTRITO WHERE NIDEN_CDIS=NEW.id_distrito) WHERE NIDEN_CSEC=OLD.id;

		CREATE RULE delete_seccion AS ON DELETE TO seccionescensales  DO INSTEAD DELETE FROM SECCION WHERE NIDEN_CSEC=OLD.id;


	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "seccionescensales";


-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA SUBSECCIÓN
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'subseccionescensales') THEN
		

		ALTER TABLE subseccionescensales RENAME TO SUBSECCION;

		ALTER TABLE SUBSECCION
		  ADD COLUMN NIDEN_CSUB_INE NUMERIC(10),
		  ADD COLUMN CPRO NUMERIC(2)  not null default 0,
		  ADD COLUMN CMUN NUMERIC(3)  not null default 0,
		  ADD COLUMN CDIS NUMERIC(2),
		  ADD COLUMN CSEC NUMERIC(3),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8)  not null default 0,
		  add column fgra numeric(15)  not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1),   
		  DROP CONSTRAINT subseccionescensales_pkey CASCADE;

		ALTER TABLE SUBSECCION RENAME COLUMN id TO NIDEN_CSUB;
		ALTER TABLE SUBSECCION 
		  ALTER COLUMN NIDEN_CSUB TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CSUB set NOT NULL;

		ALTER TABLE SUBSECCION RENAME COLUMN id_seccion TO NIDEN_CSEC;
		ALTER TABLE SUBSECCION 
		  ALTER COLUMN NIDEN_CSEC TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CSEC set NOT NULL;

		ALTER TABLE SUBSECCION RENAME COLUMN codigoine TO CSUB;
		ALTER TABLE SUBSECCION 
		  ALTER COLUMN CSUB TYPE NUMERIC(2) USING CSUB::integer,
		  ALTER COLUMN CSUB set NOT NULL;

		UPDATE SUBSECCION SET CPRO=SECCION.CPRO FROM SECCION WHERE SECCION.NIDEN_CSEC=SUBSECCION.NIDEN_CSEC;
		UPDATE SUBSECCION SET CMUN=SECCION.CMUN FROM SECCION WHERE SECCION.NIDEN_CSEC=SUBSECCION.NIDEN_CSEC;
		UPDATE SUBSECCION SET CDIS=SECCION.CDIS FROM SECCION WHERE SECCION.NIDEN_CSEC=SUBSECCION.NIDEN_CSEC;
		UPDATE SUBSECCION SET CSEC=SECCION.CSEC FROM SECCION WHERE SECCION.NIDEN_CSEC=SUBSECCION.NIDEN_CSEC;

		ALTER TABLE SUBSECCION ADD CONSTRAINT PK_SUBSECCION PRIMARY KEY (NIDEN_CSUB);
		CREATE INDEX AK_SUBSECCION_01 ON SUBSECCION(CPRO,CMUN,CDIS,CSEC,CSUB);
		CREATE INDEX XI_SUBSECCION_01 ON SUBSECCION(NIDEN_CSEC);


			CREATE OR REPLACE VIEW subseccionescensales AS SELECT subseccion.oid, subseccion.niden_csub AS id, lpad(subseccion.csub::character varying::text, 2, '0'::text) AS codigoine, subseccion.niden_csec AS id_seccion, subseccion."GEOMETRY", subseccion.area, subseccion.length FROM subseccion;

		CREATE RULE insert_subseccion AS ON INSERT TO subseccionescensales DO INSTEAD INSERT INTO SUBSECCION (NIDEN_CSUB, CSUB, NIDEN_CSEC, "GEOMETRY", area, length, CPRO,CMUN,CDIS,CSEC, fdoc, fgra) 
		VALUES (nextval('seq_subseccionescensales'), NEW.codigoine::int, NEW.id_seccion, NEW."GEOMETRY", NEW.area, NEW.length, (SELECT CPRO FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), (SELECT CMUN FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), (SELECT CDIS FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), (SELECT CSEC FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_subseccion AS ON UPDATE TO subseccionescensales DO INSTEAD UPDATE SUBSECCION SET CSUB=NEW.codigoine::int, NIDEN_CSEC=NEW.id_seccion, "GEOMETRY"=NEW."GEOMETRY", area=NEW.area, length=NEW.length, CPRO=(SELECT CPRO FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), CMUN=(SELECT CMUN FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), CDIS=(SELECT CDIS FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), CSEC=(SELECT CSEC FROM SECCION WHERE NIDEN_CSEC=NEW.id_seccion), fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999')  WHERE NIDEN_CSUB=OLD.id;

		CREATE RULE delete_subseccion AS ON DELETE TO subseccionescensales DO INSTEAD DELETE FROM SUBSECCION WHERE NIDEN_CSUB=OLD.id;


	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "subseccionescensales";



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA VIA

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'vias') THEN
		
		ALTER TABLE vias RENAME TO via;

		ALTER TABLE VIA
		  ADD COLUMN NIDEN_CMUN NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN NIDEN_CVIA_INE NUMERIC(10),
		  ADD COLUMN CPRO NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CMUN NUMERIC(3) NOT NULL DEFAULT 0,
		  ADD COLUMN CLASE VARCHAR(1) NOT NULL DEFAULT 0,
		  ADD COLUMN DFI_CVIA VARCHAR(25) NOT NULL DEFAULT 0,
		  ADD COLUMN DN_CVIA VARCHAR(50) NOT NULL DEFAULT 0,
		  ADD COLUMN CONFIRMADO NUMERIC(1) NOT NULL DEFAULT 0,
		  ADD COLUMN COD_ENTIDAD VARCHAR(1),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8)  not null default 0,
		  add column fgra numeric(15)  not null default 0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1),   
		  DROP CONSTRAINT vias_pkey CASCADE;

		ALTER TABLE VIA RENAME COLUMN id TO NIDEN_CVIA;
		ALTER TABLE VIA 
		  ALTER COLUMN NIDEN_CVIA TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_CVIA set NOT NULL;

		ALTER TABLE VIA RENAME COLUMN codigoine TO CVIA;
		UPDATE VIA SET CVIA=0 WHERE CVIA IS NULL;
		ALTER TABLE VIA 
		  ALTER COLUMN CVIA TYPE NUMERIC(5),
		  ALTER COLUMN CVIA set NOT NULL,
		ALTER COLUMN CVIA set DEFAULT 0;

		ALTER TABLE VIA RENAME COLUMN tipoviaine TO TVIA;
		ALTER TABLE VIA 
		  ALTER COLUMN TVIA TYPE VARCHAR(6);

		ALTER TABLE VIA RENAME COLUMN nombreviaine TO D_VIA;
		UPDATE VIA SET D_VIA='' WHERE D_VIA IS NULL; 
		ALTER TABLE VIA 
		  ALTER COLUMN D_VIA TYPE VARCHAR(70),
		  ALTER COLUMN D_VIA set NOT NULL;

		ALTER TABLE VIA RENAME COLUMN nombreviacortoine TO DC_VIA;
		UPDATE VIA SET DC_VIA='' WHERE DC_VIA IS NULL; 
		ALTER TABLE VIA 
			ALTER COLUMN DC_VIA TYPE VARCHAR(50),
			ALTER COLUMN DC_VIA set NOT NULL;

		ALTER TABLE VIA RENAME COLUMN codigocatastro TO CVIA_DGC;
		ALTER TABLE VIA 
			ALTER COLUMN CVIA_DGC TYPE NUMERIC(5);

		UPDATE VIA SET NIDEN_CMUN=MUNICIPIO.NIDEN_CMUN FROM MUNICIPIO WHERE MUNICIPIO.niden_cmun_ine=VIA.ID_MUNICIPIO;
		UPDATE VIA SET CPRO=substr(lpad(id_municipio::varchar, 5, '0'),1,2)::int;
		UPDATE VIA SET CMUN=substr(lpad(id_municipio::varchar, 5, '0'),3,5)::int;
		UPDATE VIA SET CLASE='V'; 

		DROP TABLE PSEUDOVIAS; 
			
		ALTER TABLE VIA ADD CONSTRAINT PK_CVIA PRIMARY KEY (NIDEN_CVIA);
		CREATE INDEX AK_VIA_01 ON VIA(CPRO,CMUN,CVIA);
		CREATE INDEX XI_VIA_01 ON VIA(NIDEN_CMUN);


		CREATE OR REPLACE VIEW vias AS SELECT via.oid, via.niden_cvia AS id, via.cvia AS codigoine, via.cvia_dgc AS codigocatastro, via.tipovianormalizadocatastro, via.tvia AS tipoviaine, via.posiciontipovia, via.d_via AS nombreviaine, via.dc_via AS nombreviacortoine, via.nombrecatastro, via.id_municipio, via."GEOMETRY", via.length, via.idalp, via.fechagrabacionayto, via.fechagrabacioncierre, via.fechaejecucion, via.fuente, via.valido, via.tipo FROM via WHERE via.clase::text = 'V'::text;


		CREATE RULE insert_via AS ON INSERT TO vias DO INSTEAD INSERT INTO VIA (NIDEN_CVIA, CVIA, CVIA_DGC, tipovianormalizadocatastro, TVIA, posiciontipovia, D_VIA, DC_VIA, nombrecatastro, id_municipio, "GEOMETRY", length, idalp, fechagrabacionayto, fechagrabacioncierre, fechaejecucion, fuente, valido, tipo, NIDEN_CMUN, CPRO, CMUN, CLASE, CONFIRMADO, fdoc, fgra) 
		VALUES (nextval('seq_vias'), NEW.codigoine, NEW.codigocatastro, NEW.tipovianormalizadocatastro, NEW.tipoviaine, NEW.posiciontipovia, NEW.nombreviaine, NEW.nombreviacortoine, NEW.nombrecatastro, NEW.id_municipio, NEW."GEOMETRY", NEW.length, NEW.idalp, NEW.fechagrabacionayto, NEW.fechagrabacioncierre, NEW.fechaejecucion, NEW.fuente, NEW.valido, NEW.tipo, (SELECT NIDEN_CMUN FROM MUNICIPIO WHERE MUNICIPIO.NIDEN_CMUN_INE=NEW.id_municipio), substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, 'V', 0, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_via AS ON UPDATE TO vias DO INSTEAD UPDATE VIA SET CVIA=NEW.codigoine, CVIA_DGC=NEW.codigocatastro, tipovianormalizadocatastro=NEW.tipovianormalizadocatastro, TVIA=NEW.tipoviaine, posiciontipovia=NEW.posiciontipovia, D_VIA=NEW.nombreviaine, DC_VIA=NEW.nombreviacortoine, nombrecatastro=NEW.nombrecatastro, id_municipio=NEW.id_municipio, "GEOMETRY"=NEW."GEOMETRY", length=NEW.length, idalp=NEW.idalp, fechagrabacionayto=NEW.fechagrabacionayto, fechagrabacioncierre=NEW.fechagrabacioncierre, fechaejecucion=NEW.fechaejecucion, fuente=NEW.fuente, valido=NEW.valido, tipo=NEW.tipo, NIDEN_CMUN=(SELECT NIDEN_CMUN FROM MUNICIPIO WHERE MUNICIPIO.NIDEN_CMUN_INE=NEW.id_municipio), CPRO= substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, CMUN= substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, CLASE='V', CONFIRMADO=0, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CVIA = OLD.id;

		CREATE RULE delete_via AS ON DELETE TO vias DO INSTEAD DELETE FROM VIA WHERE NIDEN_CVIA=OLD.id;

		CREATE OR REPLACE VIEW pseudovias AS SELECT via.oid, via.niden_cvia AS id, via.cvia AS codigoine, via.d_via AS descripcion, via.id_municipio, via."GEOMETRY", via.length FROM via WHERE via.clase::text = 'P'::text;

		CREATE RULE insert_pseudovia AS ON INSERT TO pseudovias DO INSTEAD INSERT INTO VIA (NIDEN_CVIA, CVIA, D_VIA, DC_VIA, id_municipio, "GEOMETRY", length, NIDEN_CMUN, CPRO, CMUN, CLASE, CONFIRMADO, fdoc, fgra) 
		VALUES (new.id, NEW.codigoine, NEW.descripcion, NEW.descripcion, NEW.id_municipio, NEW."GEOMETRY", NEW.length, (SELECT NIDEN_CMUN FROM MUNICIPIO WHERE MUNICIPIO. NIDEN_CMUN_INE=NEW.id_municipio), substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, 'P', 0, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));

		CREATE RULE update_pseudovia AS ON UPDATE TO pseudovias DO INSTEAD UPDATE VIA SET CVIA=NEW.codigoine, D_VIA=NEW.descripcion, DC_VIA=NEW.descripcion, id_municipio=NEW.id_municipio, "GEOMETRY"=NEW."GEOMETRY", length=NEW.length, NIDEN_CMUN=(SELECT NIDEN_CMUN FROM MUNICIPIO WHERE MUNICIPIO. NIDEN_CMUN_INE=NEW.id_municipio), CPRO=substr(lpad(NEW.id_municipio::varchar, 5, '0'),1,2)::int, CMUN=substr(lpad(NEW.id_municipio::varchar, 5, '0'),3,5)::int, CLASE='P', CONFIRMADO=0, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_CVIA = OLD.id;

		CREATE RULE delete_pseudovia AS ON DELETE TO pseudovias DO INSTEAD DELETE FROM VIA WHERE NIDEN_CVIA=OLD.id;


	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "vias";


-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA numero DE POLICÍA (APP)

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'numeros_policia') THEN

		ALTER TABLE numeros_policia RENAME TO APP;

		ALTER TABLE APP
		  ADD COLUMN NIDEN_CSUB  NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN NIDEN_CNUCLE  NUMERIC(10) NOT NULL DEFAULT 0,
		  ADD COLUMN NIDEN_APP_INE  NUMERIC(10),
		  ADD COLUMN CPRO  NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CMUN  NUMERIC(3) NOT NULL DEFAULT 0,
		  ADD COLUMN CENTCO  NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CENTSI  NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CNUCLE  NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CDIS  NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CSEC  NUMERIC(3) NOT NULL DEFAULT 0,
		  ADD COLUMN CSUB  NUMERIC(2) NOT NULL DEFAULT 0,
		  ADD COLUMN CVIA  NUMERIC(5) NOT NULL DEFAULT 0,
		  ADD COLUMN KMT    numeric(4),
		  ADD COLUMN HMT  VARCHAR(1),
		  ADD COLUMN BLOQUE   VARCHAR(2),
		  ADD COLUMN PORTAL   VARCHAR(2),
		  ADD COLUMN NUM_INF   NUMERIC(4),
		  ADD COLUMN NUM_SUP   NUMERIC(4),
		  ADD COLUMN CALIF_INF   VARCHAR (1),
		  ADD COLUMN CALIF_SUP   VARCHAR (1),
		  ADD COLUMN ENTRADA_PRINCIPAL VARCHAR(1),
		  ADD COLUMN TXT_APP   VARCHAR(70),
		  ADD COLUMN CPOS     numeric(5) NOT NULL DEFAULT 0,
		  ADD COLUMN REF_DGC   VARCHAR(14),
		  ADD COLUMN CONFIRMADO     numeric(1) NOT NULL DEFAULT 0,
		  ADD COLUMN COORDENADAX     numeric(10),
		  ADD COLUMN COORDENADAY     numeric(10),
		  ADD COLUMN HUSO_HORARIO  VARCHAR(10),
		  ADD COLUMN SMA_REF  VARCHAR(6),
		  add column cvar varchar(1) NOT NULL DEFAULT 'M',
		  add column cauv varchar(2) not null default 'AT',
		  add column fdoc   numeric(8) not null default  0,
		  add column fgra numeric(15) not null default  0,
		  add column fbaj   numeric(8) not null default 99999999,
		  add column f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
		  add column f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
		  add column discrepacia numeric(1) not null default 0,
		  add column entramite numeric(1) not null default  0,
		  add column expediente_ini varchar(30) not null default ' ',
		  add column expediente_fin varchar(30),
		  add column usuario varchar(30) not null default ' ',
		  add column entidad varchar(20) not null default ' ',
		  add column rectificacion varchar(1),   
		  DROP CONSTRAINT numeros_policia_pkey CASCADE;

		ALTER TABLE APP RENAME COLUMN id TO NIDEN_APP;
		ALTER TABLE APP
		  ALTER COLUMN NIDEN_APP TYPE NUMERIC(10),
		  ALTER COLUMN NIDEN_APP set NOT NULL;

		ALTER TABLE APP RENAME COLUMN id_via TO NIDEN_CVIA;
		UPDATE APP SET NIDEN_CVIA=0 WHERE NIDEN_CVIA IS NULL;
		ALTER TABLE APP
		  ALTER COLUMN NIDEN_CVIA TYPE NUMERIC(5),
		  ALTER COLUMN NIDEN_CVIA set NOT NULL;

		ALTER TABLE APP RENAME COLUMN numero TO numero_localgis;
		ALTER TABLE APP
		  ADD COLUMN numero NUMERIC(4);

		ALTER TABLE APP RENAME COLUMN calificador TO calificador_localgis;
		ALTER TABLE APP
		  ADD COLUMN calificador VARCHAR(1);

		UPDATE APP SET CPRO=substr(lpad(id_municipio::varchar, 5, '0'),1,2)::int;
		UPDATE APP SET CMUN=substr(lpad(id_municipio::varchar, 5, '0'),3,5)::int;
		UPDATE APP SET REF_DGC=PARCELAS.REFERENCIA_CATASTRAL FROM PARCELAS WHERE PARCELAS .ID=APP.PARCELA;

		ALTER TABLE APP ADD CONSTRAINT PK_APP PRIMARY KEY (NIDEN_APP);
		CREATE INDEX AK_APP_01 ON APP (CPRO,CMUN,CENTCO,CENTSI,CNUCLE,NUMERO,KMT,CALIFICADOR,HMT,BLOQUE,PORTAL);
		CREATE INDEX XI_APP_01 ON APP(NIDEN_CNUCLE);
		CREATE INDEX XI_APP_02 ON APP(PORTAL);
		CREATE INDEX XI_APP_03 ON APP(BLOQUE);
		CREATE INDEX XIF4APP ON APP(NIDEN_CVIA);
		CREATE INDEX XIF5APP ON APP(NIDEN_CSUB);


		CREATE OR REPLACE VIEW numeros_policia AS SELECT app.oid, app.niden_app AS id,app.rotulo, app.id_municipio, app.niden_cvia AS id_via, app.fechaalta, app.fechabaja, app."GEOMETRY", app.idalp, app.calificador_localgis AS calificador, app.numero_localgis AS numero, app.fechaejecucion, app.parcela, app.fuente, app.valido FROM app;
		CREATE RULE insert_numeros AS ON INSERT TO numeros_policia DO INSTEAD INSERT INTO APP (NIDEN_APP, rotulo, id_municipio, NIDEN_CVIA, fechaalta, fechabaja, "GEOMETRY", idalp, calificador_localgis, numero_localgis, fechaejecucion, parcela, fuente, valido, fdoc, fgra) VALUES (new.id, new.rotulo, new.id_municipio, new.id_via, new.fechaalta, new.fechabaja, new."GEOMETRY", new.idalp, new.calificador, new.numero, new.fechaejecucion, new.parcela, new.fuente, new.valido, to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'));
		CREATE RULE update_numeros AS ON UPDATE TO numeros_policia DO INSTEAD UPDATE APP SET rotulo=new.rotulo, id_municipio=new.id_municipio, NIDEN_CVIA=new.id_via, fechaalta=new.fechaalta, fechabaja=new.fechabaja, "GEOMETRY"=new."GEOMETRY", idalp=new.idalp, calificador_localgis=new.calificador, numero_localgis=new.numero, fechaejecucion=new.fechaejecucion, parcela=new.parcela, fuente=new.fuente, valido=new.valido, fdoc=to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'), fgra=to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999') WHERE NIDEN_APP = OLD.id;
		CREATE RULE delete_numeros AS ON DELETE TO numeros_policia DO INSTEAD DELETE FROM APP WHERE NIDEN_APP=OLD.id;

		;
	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al actualizar la tabla';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "vias";




-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA HUECO
DROP TABLE IF EXISTS hueco;
CREATE TABLE hueco (
  niden_hueco numeric(10) NOT NULL,
  niden_app numeric(10) NOT NULL,
  escalera varchar(2),
  planta varchar(3),
  puerta varchar(4),
  tipo_hueco numeric(1) not null,
  tipo_local numeric(1) not null,
  tipo_colectivo numeric(1) not null,
  niden_hueco_ine numeric(10),
  txt_hueco varchar(70),
  ref_dgc varchar(14),
  confirmado numeric(1) not null,
  cvar varchar(1) NOT NULL DEFAULT 'M',
  cauv varchar(2) not null default 'AT',
  fdoc   numeric(8)  not null default 0,
  fgra numeric(15)  not null default 0,
  fbaj   numeric(8) not null default 99999999,
  f_doc_cie   numeric(8) not null default to_number(to_char(current_timestamp, 'YYYYMMDD'),'99999999'),
  f_gra_cie numeric(15) not null default to_number(to_char(current_timestamp, 'YYYYMMDDHHMISS'),'99999999999999'),
  discrepacia numeric(1) not null default 0,
  entramite numeric(1) not null default  0,
  expediente_ini varchar(30) not null default ' ',
  expediente_fin varchar(30),
  usuario varchar(30) not null default ' ',
  entidad varchar(20) not null default ' ',
  rectificacion varchar(1)  
);
DROP SEQUENCE IF EXISTS seq_hueco;
CREATE SEQUENCE seq_hueco INCREMENT 1 START 1;

alter table hueco add constraint pk_hueco primary key (niden_hueco);
create index ak_hueco_01 on hueco (niden_app, escalera, planta, puerta);
create index xi_hueco_01 on hueco(niden_app);
create index xi_hueco_02 on hueco(planta);
create index xi_hueco_03 on hueco(puerta);
create index xi_hueco_04 on hueco(tipo_colectivo);
create index xi_hueco_05 on hueco(tipo_hueco);
create index xi_hueco_06 on hueco(tipo_local);
create index xi_hueco_07 on hueco(escalera);

-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA LOCAL
DROP TABLE IF EXISTS local;
CREATE TABLE local (
  niden_local numeric(10) NOT NULL,
  niden_app numeric(10) NOT NULL,
  niden_local_ine numeric(10),
  cpro numeric(2) not null,
  cmun numeric(3) not null,
  proceso numeric(9) not null,
  nlocal varchar(50) not null,
  nlocal_bilingue varchar(50) not null,
  cloc numeric(10) not null,
  nreg numeric(2) not null,
  texto_info varchar(100),
  direccion_etiq varchar(100) not null,
  f_alta_modif numeric(15) not null,
  usuario varchar(30) not null,
  ind_acrptacion varchar(1) not null,
  ind_bloqueo varchar(1) not null,
  ind_consulta varchar(1) not null
);
DROP SEQUENCE IF EXISTS seq_local;
CREATE SEQUENCE seq_local INCREMENT 1 START 1;

alter table local add constraint pk_local primary key (niden_local);
create index ak_local_01 on local (niden_local_ine);
create index xif1local on local(niden_app);



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA MESA
DROP TABLE IF EXISTS mesa;
CREATE TABLE mesa (
  niden_mesa numeric(10) NOT NULL,
  niden_local numeric(10) NOT NULL,
  niden_csub numeric(10) not null,
  niden_mesa_ine numeric(10),
  cpro numeric(2) not null,
  cmun numeric(3) not null,
  cdis numeric(2) not null,
  csec numeric(3) not null,
  csub numeric(2) not null,
  mesa varchar(1) not null,
  proceso   numeric(9) not null,
  nreg numeric(2) not null,
  inicial_ape1 varchar(1) not null,
  final_ape1 varchar(1) not null,
  ncer numeric(6),
  ncere_municipales numeric(6),
  ncere_pe numeric(6),
  inf_adicional varchar(30),
  f_alta_modif   numeric(15) not null,
  usuario varchar(30) not null,
  ind_consulta varchar(1) not null,
  ind_bloqueo varchar(1) not null,
  ind_comunicacion varchar(1) not null
);
DROP SEQUENCE IF EXISTS seq_mesa;
CREATE SEQUENCE seq_mesa INCREMENT 1 START 1;

alter table mesa add constraint pk_mesa primary key (niden_mesa);
create index ak_mesa_01 on mesa (cpro,cmun,cdis,csec,csub,mesa);
create index xif1mesa on mesa(niden_local);
create index xif2mesa on mesa(niden_csub);
-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA TIPOESTRUCTURA.
DROP TABLE IF EXISTS tipoestructura;
CREATE TABLE tipoestructura (
  niden_tipo_estructura numeric(10) NOT NULL,
  d_tipo_estructura varchar(70) not null,
  niden_cmun numeric(10)
);
DROP SEQUENCE IF EXISTS seq_tipoestructura;
CREATE SEQUENCE seq_tipoestructura INCREMENT 1 START 1;

alter table tipoestructura add constraint pk_tipo_estructura primary key (niden_tipo_estructura);
create index xif1tipoestructura on tipoestructura(niden_cmun);



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA AESTRUCTURA.
DROP TABLE IF EXISTS aestructura;
CREATE TABLE aestructura (
  niden_atributo numeric(10) NOT NULL,
  niden_tipo_estructura numeric(10) not null,
  tipo_atributo varchar(1) not null,
  nombre_atributo varchar(40) not null
);
DROP SEQUENCE IF EXISTS seq_aestructura;
CREATE SEQUENCE seq_aestructura INCREMENT 1 START 1;

alter table aestructura add constraint xpkaestructura primary key (niden_atributo);
create index xif1aestructura on aestructura(niden_tipo_estructura);



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA ESTRUCTURA.
DROP TABLE IF EXISTS estructura;
CREATE TABLE estructura (
  niden_estructura numeric(10) NOT NULL,
  niden_cmun numeric(10) not null,
  niden_tipo_estructura numeric(10) not null,
  d_estructura varchar(70) not null
);
DROP SEQUENCE IF EXISTS seq_estructura;
CREATE SEQUENCE seq_estructura INCREMENT 1 START 1;

alter table estructura add constraint pk_estructura primary key (niden_estructura);
create index xif1estructura on estructura(niden_tipo_estructura);
create index xif2estructura on estructura(niden_cmun);



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA VESTRUCTURA.
DROP TABLE IF EXISTS vestructura;
CREATE TABLE vestructura (
  niden_atributo numeric(10) NOT NULL,
  niden_estructura numeric(10) NOT NULL,
  valor varchar(70)
);
DROP SEQUENCE IF EXISTS seq_vestructura;
CREATE SEQUENCE seq_vestructura INCREMENT 1 START 1;

alter table vestructura add constraint xpkvestructura primary key (niden_atributo,niden_estructura);
create index xif1vestructura on vestructura(niden_estructura);
create index xif2vestructura on vestructura(niden_atributo);



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA RELAPP.
DROP TABLE IF EXISTS relapp;
CREATE TABLE relapp (
  niden_app numeric(10) NOT NULL,
  niden_estructura numeric(10) NOT NULL
);

alter table relapp add constraint xpkrelapp primary key (niden_app,niden_estructura);
create index xif1relapp on relapp(niden_estructura);
create index xif2relapp on relapp(niden_app);



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA RELESTRUCTURA.
DROP TABLE IF EXISTS relestructura;
CREATE TABLE relestructura (
  niden_estructura_padre numeric(10) NOT NULL,
  niden_estructura_hijo numeric(10) NOT NULL
);

alter table relestructura add constraint xpkrelestructura primary key (niden_estructura_padre,niden_estructura_hijo);
create index xif1relestructura on relestructura(niden_estructura_hijo);
create index xif2relestructura on relestructura(niden_estructura_padre);



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA OPERACIONESH.
DROP TABLE IF EXISTS operacionesh;
CREATE TABLE operacionesh (
  expediente varchar(30) not null,
  fgra numeric(15) not null,
  entidad varchar(2) not null,
  niden_origen numeric(10) not null,
  cvar varchar(1) not null,
  cauv varchar(2) not null,
  niden_destino numeric(10) not null,
  cpro numeric(2) not null,
  cmun numeric(3) not null
);

alter table operacionesh add constraint pk_operacionesh primary key (expediente, entidad, niden_origen, niden_destino);



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA PROVINCIAH
DROP TABLE IF EXISTS provinciah;
CREATE TABLE provinciah
(
  cpro numeric(2,0) NOT NULL,
  d_prov character varying(70) NOT NULL,
  dc_prov character varying(50) NOT NULL,
  nombrecooficial character varying(50),
  comunidad character varying(50) NOT NULL,
  "GEOMETRY" geometry,
  area numeric,
  length numeric,
  niden_cpro numeric(10,0) NOT NULL DEFAULT 0,
  niden_ccaa numeric(10,0) NOT NULL DEFAULT 0,
  dfi_prov character varying(25) NOT NULL DEFAULT 0,
  dn_prov character varying(50) NOT NULL DEFAULT 0,
  cod_entidad character varying(21),
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table provinciah add constraint pk_provinciah primary key (niden_cpro,fgra);

-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA MUNICIPIOH
DROP TABLE IF EXISTS municipioh;
CREATE TABLE municipioh
(
  niden_cmun_ine numeric(10,0) NOT NULL,
  cpro numeric(2,0) NOT NULL,
  cmun_cat numeric(3,0) NOT NULL,
  id_mhacienda character varying(2),
  cmun numeric(3,0) NOT NULL,
  d_cmun character varying(70) NOT NULL,
  dc_cmun character varying(50) NOT NULL,
  nombrecooficial character varying(50),
  "GEOMETRY" geometry,
  area numeric,
  length numeric,
  srid character varying(10),
  srid_proyeccion numeric(5,0),
  niden_cmun numeric(10,0) NOT NULL DEFAULT 0,
  niden_cpro numeric(10,0) NOT NULL DEFAULT 0,
  idioma numeric(1,0) NOT NULL DEFAULT 0,
  dfi_mun character varying(25) NOT NULL DEFAULT 0,
  dn_mun character varying(50) NOT NULL DEFAULT 0,
  confirmado numeric(1,0) NOT NULL DEFAULT 0,
  cod_entidad character varying(21),
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table municipioh add constraint pk_municipioh primary key (niden_cmun,fgra);

-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA ECOLECTIVA
DROP TABLE IF EXISTS ecolectivah;
CREATE TABLE ecolectivah
(
  niden_centco numeric(10,0) NOT NULL,
  id_municipio numeric(5,0) NOT NULL,
  centco numeric(2,0) NOT NULL,
  d_centoc character varying(70) NOT NULL,
  dc_centco character varying(50) NOT NULL,
  nombrecooficial character varying(50),
  descripcion character varying(70),
  "GEOMETRY" geometry,
  area numeric,
  length numeric,
  niden_cmun numeric(10,0) NOT NULL DEFAULT 0,
  niden_centco_ine numeric(10,0),
  cpro numeric(2,0) NOT NULL DEFAULT 0,
  cmun numeric(3,0) NOT NULL DEFAULT 0,
  dfi_centco character varying(25) NOT NULL DEFAULT 0,
  dn_centco character varying(50) NOT NULL DEFAULT 0,
  cod_entidad character varying(21),
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table ecolectivah add constraint pk_ecolectivah primary key (niden_centco,fgra);


-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA ESINGULAR
DROP TABLE IF EXISTS esingularh;
CREATE TABLE esingularh
(
  niden_centsi numeric(10,0) NOT NULL,
  id_municipio numeric(5,0) NOT NULL,
  centsi numeric(2,0) NOT NULL,
  d_centsi character varying(70) NOT NULL,
  dc_centsi character varying(50) NOT NULL,
  nombrecooficial character varying(50),
  descripcion character varying(70),
  "GEOMETRY" geometry,
  centco numeric(2,0) NOT NULL,
  area numeric,
  length numeric,
  niden_centco numeric(10,0) NOT NULL DEFAULT 0,
  niden_centsi_ine numeric(10,0),
  cpro numeric(2,0) NOT NULL DEFAULT 0,
  cmun numeric(3,0) NOT NULL DEFAULT 0,
  dfi_centsi character varying(25) NOT NULL DEFAULT 0,
  dn_centsi character varying(50) NOT NULL DEFAULT 0,
  cod_entidad character varying(21),
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table esingularh add constraint pk_esingularh primary key (niden_centsi,fgra);

-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA NUCLEOH
DROP TABLE IF EXISTS nucleoh;
CREATE TABLE nucleoh
(
  niden_cnucle numeric(10,0) NOT NULL,
  niden_centsi numeric(10,0) NOT NULL,
  cnucle numeric(2) NOT NULL,
  d_nucle varchar(70) NOT NULL,
  dc_nucle varchar(50) not null,
  nombrecooficial character varying(50),
  descripcion character varying(70),
  numhabitantes numeric(8,0),
  tipo numeric(1,0) NOT NULL,
  "GEOMETRY" geometry,
  area numeric,
  length numeric,
  NIDEN_CNUCLE_INE NUMERIC(10),
  CPRO NUMERIC(2) NOT NULL DEFAULT 0,
  CMUN NUMERIC(3) NOT NULL DEFAULT 0,
  CENTCO NUMERIC(2) DEFAULT 0,
  CENTSI NUMERIC(2) NOT NULL DEFAULT 0,
  DFI_CNUCLE VARCHAR(25) NOT NULL DEFAULT 0,
  DN_CNUCLE VARCHAR(50) NOT NULL DEFAULT 0,
  COD_ENTIDAD VARCHAR(21),
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table nucleoh add constraint pk_nucleoh primary key (niden_cnucle,fgra);


-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA DISTRITOH
DROP TABLE IF EXISTS distritoh;
CREATE TABLE distritoh
(
  niden_cdis numeric(10,0) NOT NULL,
  d_cdis character varying(70) NOT NULL,
  cdis numeric(2,0) NOT NULL,
  id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  area numeric,
  length numeric,
  niden_cmun numeric(10,0) NOT NULL DEFAULT 0,
  niden_cdis_ine numeric(10,0),
  cpro numeric(2,0) NOT NULL DEFAULT 0,
  cmun numeric(3,0) NOT NULL DEFAULT 0,
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table distritoh add constraint pk_distritoh primary key (niden_cdis,fgra);



-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA SECCIONH
DROP TABLE IF EXISTS SECCIONH;
CREATE TABLE SECCIONH
(
  niden_csec numeric(10,0) NOT NULL,
  niden_cdis numeric(10,0) NOT NULL,
  csec character varying(3) NOT NULL,
  "GEOMETRY" geometry,
  nombre character varying(25),
  area numeric,
  length numeric,
  niden_csec_ine numeric(10,0),
  cpro numeric(2,0) NOT NULL DEFAULT 0,
  cmun numeric(3,0) NOT NULL DEFAULT 0,
  cdis numeric(2,0),
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table seccionh add constraint pk_seccionh primary key (niden_csec,fgra);

-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA SUBSECCION


alter table subseccionh add constraint pk_subseccionh primary key (niden_csub,fgra);


-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA VIAH
DROP TABLE IF EXISTS viah;
CREATE TABLE viah
(
  niden_cvia numeric(10,0) NOT NULL,
  cvia numeric(5,0) NOT NULL,
  cvia_dgc numeric(5,0),
  tipovianormalizadocatastro character varying(2),
  tvia character varying(6),
  posiciontipovia numeric(1,0),
  d_via character varying(70) NOT NULL,
  dc_via character varying(50),
  nombrecatastro character varying(50),
  id_municipio numeric(5,0) NOT NULL,
  "GEOMETRY" geometry,
  length numeric,
  idalp numeric(8,0),
  fechagrabacionayto date,
  fechagrabacioncierre date,
  fechaejecucion date,
  fuente numeric(1,0) DEFAULT (0)::numeric,
  valido numeric(1,0) DEFAULT (1)::numeric,
  tipo character varying(20),
  niden_cmun numeric(10,0) NOT NULL DEFAULT 0,
  niden_cvia_ine numeric(10,0),
  cpro numeric(2,0) NOT NULL DEFAULT 0,
  cmun numeric(3,0) NOT NULL DEFAULT 0,
  clase character varying(1) NOT NULL DEFAULT 0,
  dfi_cvia character varying(25) NOT NULL DEFAULT 'V'::character varying,
  dn_cvia character varying(50) NOT NULL DEFAULT 0,
  confirmado numeric(1,0) NOT NULL DEFAULT 0,
  cod_entidad character varying(1),
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table viah add constraint pk_viah primary key (niden_cvia,fgra);


-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA APPH
DROP TABLE IF EXISTS apph;
CREATE TABLE apph
(
  niden_app numeric(10,0) NOT NULL,
  rotulo character varying(100),
  id_municipio numeric(5,0) NOT NULL,
  niden_cvia numeric(5,0) not null,
  fechaalta date,
  fechabaja date,
  "GEOMETRY" geometry,
  idalp numeric(8,0),
  calificador_localgis character varying(100),
  numero_localgis character varying(100),
  fechaejecucion date,
  parcela numeric(8,0),
  fuente numeric(1,0) DEFAULT 0,
  valido numeric(1,0) DEFAULT 1,
  numero NUMERIC(4),
  calificador VARCHAR(1),
  NIDEN_CSUB NUMERIC(10) NOT NULL DEFAULT 0,
  NIDEN_CNUCLE NUMERIC(10) NOT NULL DEFAULT 0,
  NIDEN_APP_INE NUMERIC(10),
  CPRO NUMERIC(2) NOT NULL DEFAULT 0,
  CMUN NUMERIC(3) NOT NULL DEFAULT 0,
  CENTCO NUMERIC(2) NOT NULL DEFAULT 0,
  CENTSI NUMERIC(2) NOT NULL DEFAULT 0,
  CNUCLE NUMERIC(2) NOT NULL DEFAULT 0,
  CDIS NUMERIC(2) NOT NULL DEFAULT 0,
  CSEC NUMERIC(3) NOT NULL DEFAULT 0,
  CSUB NUMERIC(2) NOT NULL DEFAULT 0,
  CVIA NUMERIC(5) NOT NULL DEFAULT 0,
  KMT NUMERIC(4),
  HMT VARCHAR(1),
  BLOQUE  VARCHAR(2),
  PORTAL  VARCHAR(2),
  NUM_INF  NUMERIC(4),
  NUM_SUP  NUMERIC(4),
  CALIF_INF  VARCHAR (1),
  CALIF_SUP  VARCHAR (1),
  ENTRADA_PRINCIPAL VARCHAR(1),
  TXT_APP  VARCHAR(70),
  CPOS  NUMERIC(5) NOT NULL DEFAULT 0,
  REF_DGC  VARCHAR(14),
  CONFIRMADO  NUMERIC(1) NOT NULL DEFAULT 0,
  COORDENADAX  NUMERIC(10),
  COORDENADAY  NUMERIC(10),
  HUSO_HORARIO VARCHAR(10),
  SMA_REF VARCHAR(6),
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table apph add constraint pk_apph primary key (niden_app,fgra);

-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--TABLA HUECOH
DROP TABLE IF EXISTS huecoh;
CREATE TABLE huecoh
(
  niden_hueco numeric(10) NOT NULL,
  niden_app numeric(10) NOT NULL,
  escalera varchar(2),
  planta varchar(3),
  puerta varchar(4),
  tipo_hueco numeric(1) not null,
  tipo_local numeric(1) not null,
  tipo_colectivo numeric(1) not null,
  niden_hueco_ine numeric(10),
  txt_hueco varchar(70),
  ref_dgc varchar(14),
  confirmado numeric(1) not null,
  cvar character varying(1) NOT NULL DEFAULT 'M'::character varying,
  cauv character varying(2) NOT NULL DEFAULT 'AT'::character varying,
  fdoc numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  fgra numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  fbaj numeric(8,0) NOT NULL DEFAULT 99999999,
  f_doc_cie numeric(8,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDD'::text), '99999999'::text),
  f_gra_cie numeric(15,0) NOT NULL DEFAULT to_number(to_char(now(), 'YYYYMMDDHHMISS'::text), '99999999999999'::text),
  discrepacia numeric(1,0) NOT NULL DEFAULT 0,
  entramite numeric(1,0) NOT NULL DEFAULT 0,
  expediente_ini character varying(30) NOT NULL DEFAULT ' '::character varying,
  expediente_fin character varying(30),
  usuario character varying(30) NOT NULL DEFAULT ' '::character varying,
  entidad character varying(20) NOT NULL DEFAULT ' '::character varying,
  rectificacion character varying(1)
);

alter table huecoh add constraint pk_huecoh primary key (niden_hueco,fgra);


-----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA PROVINCIA (PROVINCIAH)

CREATE OR REPLACE FUNCTION provinciah_function()
  RETURNS trigger AS $provinciah_function$
  BEGIN
    update provinciah set fbaj=NEW.fdoc where provinciah.niden_cpro=NEW.niden_cpro and provinciah.fbaj=99999999;
    
    insert into provinciah values (new.cpro, new.d_prov, new.dc_prov, new.nombrecooficial, new.comunidad, new."GEOMETRY", new.area, new.length, 
    new.niden_cpro, new.niden_ccaa, new.dfi_prov, new.dn_prov, new.cod_entidad, new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie,
    new.discrepacia, new.entramite, new.expediente_ini, new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $provinciah_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION provinciah_function()
  OWNER TO geopista;
  
CREATE TRIGGER provincia_trigger
  AFTER INSERT OR UPDATE
  ON provincia
  FOR EACH ROW
  EXECUTE PROCEDURE provinciah_function();
  
  -----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA MUNICIPIO (MUNICIPIO)

CREATE OR REPLACE FUNCTION municipioh_function()
  RETURNS trigger AS $municipioh_function$
  BEGIN
    update municipioh set fbaj=NEW.fdoc where municipioh.niden_cmun=NEW.niden_cmun and municipioh.fbaj=99999999;
    
    insert into municipioh values (new.niden_cmun_ine, new.cpro, new.cmun_cat, new.id_mhacienda, new.cmun, new.d_cmun, new.dc_cmun, new.nombrecooficial, 
    new."GEOMETRY", new.area, new.length, new.srid, new.srid_proyeccion, new.niden_cmun, new.niden_cpro, new.idioma, new.dfi_mun, new.dn_mun,
    new.confirmado, new.cod_entidad, 
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $municipioh_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION municipioh_function()
  OWNER TO geopista;
  
CREATE TRIGGER municipio_trigger
  AFTER INSERT OR UPDATE
  ON municipio
  FOR EACH ROW
  EXECUTE PROCEDURE municipioh_function();
  
  -----------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA ECOLECTIVA (ECOLECTIVAH)

CREATE OR REPLACE FUNCTION ecolectivah_function()
  RETURNS trigger AS $ecolectivah_function$
  BEGIN
    update ecolectivah set fbaj=NEW.fdoc where ecolectivah.niden_centco=NEW.niden_centco and ecolectivah.fbaj=99999999;
    
    insert into ecolectivah values (new.niden_centco, new.id_municipio, new.centco, new.d_centoc, new.dc_centco, new.nombrecooficial, new.descripcion, 
    new."GEOMETRY", new.area, new.length, new.niden_cmun, new.niden_centco_ine, new.cpro, new.cmun, new.dfi_centco, new.dn_centco, new.cod_entidad,  
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $ecolectivah_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION ecolectivah_function()
  OWNER TO geopista;
  
CREATE TRIGGER ecolectiva_trigger
  AFTER INSERT OR UPDATE
  ON ecolectiva
  FOR EACH ROW
  EXECUTE PROCEDURE ecolectivah_function();
  
  -----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA ESINGULAR (ESINGULARH)

CREATE OR REPLACE FUNCTION esingularh_function()
  RETURNS trigger AS $esingularh_function$
  BEGIN
    update esingularh set fbaj=NEW.fdoc where esingularh.niden_centsi=NEW.niden_centsi and esingularh.fbaj=99999999;
    
    insert into esingularh values (new.niden_centsi, new.id_municipio, new.centsi, new.d_centsi, new.dc_centsi, new. nombrecooficial, 
    new.descripcion, new."GEOMETRY", new.centco, new.area, new.length, new.niden_centco, new.niden_centsi_ine, new.cpro, new.cmun, new.dfi_centsi, 
    new.dn_centsi, new.cod_entidad,   
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $esingularh_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION esingularh_function()
  OWNER TO geopista;
  
CREATE TRIGGER esingularh_function
  AFTER INSERT OR UPDATE
  ON esingular
  FOR EACH ROW
  EXECUTE PROCEDURE esingularh_function();
  
  -----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA NUCLEO (NUCLEOH)

CREATE OR REPLACE FUNCTION nucleoh_function()
  RETURNS trigger AS $nucleoh_function$
  BEGIN
    update nucleoh set fbaj=NEW.fdoc where nucleoh.niden_cnucle=NEW.niden_cnucle and nucleoh.fbaj=99999999;
    
    insert into nucleoh values (new.niden_cnucle, new.niden_centsi, new.cnucle, new.d_nucle, new.dc_nucle, new.nombrecooficial, new.descripcion, 
    new.numhabitantes, new.tipo, new."GEOMETRY", new.area, new.length, new.NIDEN_CNUCLE_INE, new.CPRO, new.cmun, new.CENTCO, new.CENTSI, 
    new.DFI_CNUCLE, new.DN_CNUCLE, new.COD_ENTIDAD,    
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $nucleoh_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION nucleoh_function()
  OWNER TO geopista;
  
CREATE TRIGGER nucleoh_function
  AFTER INSERT OR UPDATE
  ON nucleoh
  FOR EACH ROW
  EXECUTE PROCEDURE nucleoh_function();

  
  -----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA DISTRITO (DISTRITOH)

CREATE OR REPLACE FUNCTION distritoh_function()
  RETURNS trigger AS $distritoh_function$
  BEGIN
    update distritoh set fbaj=NEW.fdoc where distritoh.niden_cdis=NEW.niden_cdis and distritoh.fbaj=99999999;
    
    insert into distritoh values (new.niden_cdis, new.d_cdis, new.cdis, new.id_municipio, new."GEOMETRY", new.area, new.length, new.niden_cmun, 
    new.niden_cdis_ine, new.cpro, new.cmun,     
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $distritoh_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION distritoh_function()
  OWNER TO geopista;
  
CREATE TRIGGER distritoh_function
  AFTER INSERT OR UPDATE
  ON distritoh
  FOR EACH ROW
  EXECUTE PROCEDURE distritoh_function();
  
  -----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA SECCION (SECCIONH)

CREATE OR REPLACE FUNCTION seccionh_function()
  RETURNS trigger AS $seccionh_function$
  BEGIN
    update seccionh set fbaj=NEW.fdoc where seccionh.niden_csec=NEW.niden_csec and seccionh.fbaj=99999999;
    
    insert into seccionh values (new.niden_csec, new.niden_cdis, new.csec, new."GEOMETRY", new.nombre, new.area, new.length, new.niden_csec_ine, 
    new.cpro, new.cmun, new.cdis,      
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $seccionh_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION seccionh_function()
  OWNER TO geopista;
  
CREATE TRIGGER seccionh_function
  AFTER INSERT OR UPDATE
  ON seccionh
  FOR EACH ROW
  EXECUTE PROCEDURE seccionh_function();

  
  -----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA SUBSECCION (SUBSECCIONH)

CREATE OR REPLACE FUNCTION subseccionh_function()
  RETURNS trigger AS $subseccionh_function$
  BEGIN
    update subseccionh set fbaj=NEW.fdoc where subseccionh.niden_csec=NEW.niden_csec and subseccionh.fbaj=99999999;
    
    insert into subseccionh values (new.niden_csub, new.csub, new.niden_csec, new."GEOMETRY", new.area, new.length, new.niden_csub_ine, 
    new.cpro, new.cmun, new.cdis, new.csec,       
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $subseccionh_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION subseccionh_function()
  OWNER TO geopista;
  
CREATE TRIGGER subseccionh_function
  AFTER INSERT OR UPDATE
  ON subseccionh
  FOR EACH ROW
  EXECUTE PROCEDURE subseccionh_function();

  -----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA VIA (VIAH)

CREATE OR REPLACE FUNCTION viah_function()
  RETURNS trigger AS $viah_function$
  BEGIN
    update viah set fbaj=NEW.fdoc where viah.niden_csec=NEW.niden_csec and viah.fbaj=99999999;
    
    insert into viah values (new.niden_cvia, new.cvia, new.cvia_dgc, new.tipovianormalizadocatastro, new.tvia, new.posiciontipovia, new.d_via, 
    new.dc_via, new.nombrecatastro, new.id_municipio, new."GEOMETRY", new.length, new.idalp, new.fechagrabacionayto, new.fechagrabacioncierre, 
    new.fechaejecucion, new.fuente, new.valido, new.tipo, new.niden_cmun, new.niden_cvia_ine, new.cpro, new.cmun, new.clase, new.dfi_cvia, 
    new.dn_cvia, new.confirmado, new.cod_entidad, 
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $viah_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION viah_function()
  OWNER TO geopista;
  
CREATE TRIGGER viah_function
  AFTER INSERT OR UPDATE
  ON viah
  FOR EACH ROW
  EXECUTE PROCEDURE viah_function();

  
  

-----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA APP (APPH)

CREATE OR REPLACE FUNCTION apph_function()
  RETURNS trigger AS $apph_function$
  BEGIN
    update apph set fbaj=NEW.fdoc where apph.niden_csec=NEW.niden_csec and apph.fbaj=99999999;
    
    insert into apph values (new.niden_app, new.rotulo, new.id_municipio, new.niden_cvia, new.fechaalta, new.fechabaja, new."GEOMETRY", new.idalp, 
    new.calificador_localgis, new.numero_localgis, new.fechaejecucion, new.parcela, new.fuente, new.valido, new.numero, new.calificador, 
    new.NIDEN_CSUB, new. NIDEN_CNUCLE, new.NIDEN_APP_INE, new.CPRO, new.CMUN, new.CENTCO, new.CENTSI, new.CNUCLE, new.CDIS, new.CSEC, 
    new.CSUB, new.CVIA, new.KMT, new.HMT, new.BLOQUE, new.PORTAL, new.NUM_INF, new.NUM_SUP, new.CALIF_INF, new.CALIF_SUP, new.ENTRADA_PRINCIPAL, 
    new.TXT_APP, new.CPOS, new.REF_DGC, new.CONFIRMADO, new.COORDENADAX, new.COORDENADAY, new.HUSO_HORARIO, new.SMA_REF, 
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $apph_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION apph_function()
  OWNER TO geopista;
  
CREATE TRIGGER apph_function
  AFTER INSERT OR UPDATE
  ON apph
  FOR EACH ROW
  EXECUTE PROCEDURE apph_function();

-----------------------------------------------------------------------------------------------------------
--Creacion DE TRIGGERS Y FUNCIONES PARA TABLA HUECO (HUECOH)

CREATE OR REPLACE FUNCTION huecoh_function()
  RETURNS trigger AS $huecoh_function$
  BEGIN
    update huecoh set fbaj=NEW.fdoc where huecoh.niden_hueco=NEW.niden_hueco and huecoh.fbaj=99999999;
    
    insert into huecoh values (new.niden_hueco, new.niden_app, new.escalera, new.planta, new.puerta, new.tipo_hueco, new.tipo_local, 
    new.tipo_colectivo, new.niden_hueco_ine, new.txt_hueco, new.ref_dgc, new.confirmado, 
    new.cvar, new.cauv, new.fdoc, new.fgra, new.fbaj, new.f_doc_cie, new.f_gra_cie, new.discrepacia, new.entramite, new.expediente_ini, 
    new.expediente_fin, new.usuario, new.entidad, new.rectificacion);
    
    RETURN NULL;
  END;
  $huecoh_function$
  LANGUAGE plpgsql VOLATILE;

ALTER FUNCTION huecoh_function()
  OWNER TO geopista;
  
CREATE TRIGGER huecoh_function
  AFTER INSERT OR UPDATE
  ON huecoh
  FOR EACH ROW
  EXECUTE PROCEDURE huecoh_function();



