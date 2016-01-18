

delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/software/localgis-apps-ireport.jnlp');
delete from apps WHERE path = '/software/localgis-apps-ireport.jnlp';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'M'||chr(243)||'dulo de Generaci'||chr(243)||'n de Informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'M'||chr(242)||'dul de Generaci'||chr(243)||' d'||chr(180)||'Informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'M'||chr(243)||'dulo de Xeraci'||chr(243)||'n de Informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]M'||chr(243)||'dulo de Generaci'||chr(243)||'n de Informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'M'||chr(242)||'dul de Generaci'||chr(243)||' d'||chr(180)||'Informes');  

INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('Generador de Informes', CURRVAL('seq_dictionary'), 'App.LocalGIS.Informes', 'LocalGIS.Informes.Login', 'DESKTOP', '/software/localgis-apps-ireport.jnlp', true, 'IReportModule');

