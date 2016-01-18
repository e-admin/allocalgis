
delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/software/localgis-apps-inicio.jnlp');
delete from apps WHERE path = '/software/localgis-apps-inicio.jnlp';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Informaci'||chr(243)||'n de referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Informaci'||chr(243)||'n de referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Informaci'||chr(243)||'n de referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', 'Informaci'||chr(243)||'n de referencia');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Informaci'||chr(243)||'n de referencia');  

INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('Informaci'||chr(243)||'n de referencia', CURRVAL('seq_dictionary'), 'App.LocalGIS.InfoReferencia', 'LocalGIS.InfoReferencia.Login', 'DESKTOP', '/software/localgis-apps-inicio.jnlp', true, 'InicioModule');

