CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
		CREATE ROLE localgisbackup LOGIN  ENCRYPTED PASSWORD 'md501560d66441e3022c7860e26cb421878'  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE;
		  
exception when others then
       RAISE NOTICE 'El rol localgis backup ya existe';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "ROL";

  
GRANT ALL ON TABLE entidad_supramunicipal TO geopista;
GRANT SELECT ON TABLE entidad_supramunicipal TO consultas;





CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
		GRANT SELECT ON TABLE entidad_supramunicipal TO localgisbackup;
		GRANT SELECT ON TABLE entidades_municipios TO localgisbackup;

		GRANT USAGE ON SCHEMA localgisguiaurbana TO localgisbackup;
		GRANT SELECT ON TABLE localgisguiaurbana.layer TO localgisbackup;


		GRANT USAGE ON SCHEMA visualizador TO localgisbackup;
		GRANT USAGE ON SCHEMA cementerio TO localgisbackup;
		GRANT USAGE ON SCHEMA padron TO localgisbackup;
		  
exception when others then
       RAISE NOTICE 'Excepcion al dar grant a los roles';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "ROL";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	-- Se pueden dar grant de todas las tablas a partir de Postgres 9. En Postgres 8 no funciona
	GRANT SELECT ON ALL TABLES IN SCHEMA public TO localgisbackup;
	GRANT SELECT ON ALL TABLES IN SCHEMA localgisguiaurbana TO localgisbackup;
	GRANT SELECT ON ALL TABLES IN SCHEMA visualizador TO localgisbackup;
	GRANT SELECT ON ALL TABLES IN SCHEMA cementerio TO localgisbackup;
	GRANT SELECT ON ALL TABLES IN SCHEMA padron TO localgisbackup;
		  
exception when others then
       RAISE NOTICE 'Excepcion al realizar un grant select';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "ROL";

CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	update iuseruserhdr set password='{TYPE2}pRTw9dv91t6nVfqqZd1ALQ==' where name='SATEC_PRO';
	update iuseruserhdr set fecha_proxima_modificacion='2016-10-01' where name='SATEC_PRO';
		  
exception when others then
       RAISE NOTICE 'Excepcion al cambiar la contraseña al usuario';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "ROL";


CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	IF EXISTS (SELECT table_name FROM information_schema.tables WHERE table_schema = 'cementerio' AND table_name = 'tarifa') THEN
		ALTER TABLE cementerio.tarifa ALTER COLUMN tipo_tarifa TYPE character varying;
		ALTER TABLE cementerio.tarifa ALTER COLUMN tipo_calculo TYPE character varying;
	END IF;

exception when others then
       RAISE NOTICE 'Excepcion al cambiar los tipos de columna de cementerio';
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "Cementerios";


