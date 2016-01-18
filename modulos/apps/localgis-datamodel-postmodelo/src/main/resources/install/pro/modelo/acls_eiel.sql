-- PRO -----------------------------------------------
SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;



CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN

	RAISE NOTICE 'Verificando ACLS de capas de la EIEL';
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Captacion') THEN
	
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Captacion');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;
	
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Deposito') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Deposito');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;


	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Tramos de Conduccion') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Tramos de Conduccion');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Tratamientos de Potabilizacion') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Tratamientos de Potabilizacion');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Red de distribucion domiciliaria') THEN

		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Red de distribucion domiciliaria');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Elemento puntual de abastecimiento') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Elemento puntual de abastecimiento');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Caseta bombeo CBA') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Caseta bombeo CBA');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;


	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Emisario') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Emisario');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Colector') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Colector');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Depuradora') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Depuradora');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Red de ramales') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Red de ramales');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Punto de vertido') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Punto de vertido');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Elementos puntuales') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Elementos puntuales');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;


	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Caseta bombeo CBS') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Caseta bombeo CBS');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Aliviaderos') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Aliviaderos');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Instalacion deportiva') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Instalacion deportiva');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Centros culturales o de esparcimiento') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centros culturales o de esparcimiento');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Parques jardines y áreas naturales') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Parques jardines y áreas naturales');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

				
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Lonjas, mercados y recintos feriales') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Lonjas, mercados y recintos feriales');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;


	RAISE NOTICE 'Verificando ACLS de capas de la EIEL Mataderos';
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Mataderos') THEN
	
	RAISE NOTICE 'Insertando mataderos';
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Mataderos');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Cementerios') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Cementerios');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;
	IF EXISTS (SELECT * FROM acl WHERE name = 'Cementerios') THEN
	
		IF NOT EXISTS (SELECT * FROM r_acl_perm WHERE idperm = 4000 and idacl=(select idacl from acl where name='Cementerios')) THEN
			INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,(select idacl from acl where name='Cementerios'));
			INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,(select idacl from acl where name='Cementerios'));
			INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,(select idacl from acl where name='Cementerios'));
			INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,(select idacl from acl where name='Cementerios'));
		END IF;
	END IF;



	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Tanatorios') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Tanatorios');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;


	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Centros Sanitarios') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centros Sanitarios');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));

	END IF;
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Centros Asistenciales') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centros Asistenciales');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Centros de enseñanza') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centros de enseñanza');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;


	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Centro de protección civil') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Centro de protección civil');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Casas consistoriales') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Casas consistoriales');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Edificios de titularidad publica sin uso') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Edificios de titularidad publica sin uso');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;


	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Infraestructura viaria') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Infraestructura viaria');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Tramos de carretera') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Tramos de carretera');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Cuadro de mando') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Cuadro de mando');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Estabilizador') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Estabilizador');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Punto de luz') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Punto de luz');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Vertedero') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Vertedero');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Parroquias') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Parroquias');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Edificaciones singulares') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Edificaciones singulares');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Comarcas EIEL') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Comarcas EIEL');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Municipios EIEL') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Municipios EIEL');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Municipios EIEL puntos') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Municipios EIEL puntos');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Nucleos Poblacion EIEL') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Nucleos Poblacion EIEL');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;


	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Nucleos Poblacion EIEL Puntos') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Nucleos Poblacion EIEL Puntos');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Parcelas EIEL') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Parcelas EIEL');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));
	END IF;

	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'Provincias EIEL') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'Provincias EIEL');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));	
	END IF;
	
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'EIEL') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'EIEL');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));	
	END IF;
	
	IF NOT EXISTS (SELECT * FROM acl WHERE name = 'EIEL_PMR_Enrutamiento') THEN
		INSERT INTO acl(idacl,name) VALUES(nextval('seq_acl'),'EIEL_PMR_Enrutamiento');

		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4000,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4010,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4020,currval('seq_acl'));
		INSERT INTO r_acl_perm(idperm,idacl) VALUES(4030,currval('seq_acl'));	
	END IF;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "ACL_EIEL";
