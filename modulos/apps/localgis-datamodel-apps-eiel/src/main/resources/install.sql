
CREATE OR REPLACE FUNCTION localgisDatamodelInstall() RETURNS VOID AS
$Q$
BEGIN
	CREATE TABLE apps
	(
	  app character varying(30) NOT NULL,
	  id_dictionary numeric,
	  acl character varying(50) NOT NULL,
	  perm character varying(150) NOT NULL,
	  app_type character varying(10) NOT NULL, 
	  path character varying(200) NOT NULL,
	  install_name character varying(50) NOT NULL,
	  active boolean DEFAULT true NOT NULL,
	  CONSTRAINT app_pk PRIMARY KEY (app)
	)
	WITH (
	  OIDS=FALSE
	);
	ALTER TABLE apps OWNER TO postgres;
	
END;
$Q$
LANGUAGE plpgsql;

select localgisDatamodelInstall() as "create table apps";

delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/software/localgis-apps-eiel.jnlp');
delete from apps WHERE path = '/software/localgis-apps-eiel.jnlp';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'M'||chr(243)||'dulo de la EIEL');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'M'||chr(242)||'dul de la EIEL');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'M'||chr(243)||'dulo de la EIEL');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]M'||chr(243)||'dulo de la EIEL');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'M'||chr(242)||'dul de la EIEL');  

INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('EIEL', CURRVAL('seq_dictionary'), 'EIEL', 'LocalGis.EIEL.Login', 'DESKTOP', '/software/localgis-apps-eiel.jnlp', true, 'EIELModule');

