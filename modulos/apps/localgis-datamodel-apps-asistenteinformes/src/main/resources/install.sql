
delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/software/localgis-apps-asistenteinformes.jnlp');
delete from apps WHERE path = '/software/localgis-apps-asistenteinformes.jnlp';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Asistente para generaci'||chr(243)||'n de informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Asistente para generaci'||chr(243)||'n de informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Asistente para generaci'||chr(243)||'n de informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', 'Asistente para generaci'||chr(243)||'n de informes');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Asistente para generaci'||chr(243)||'n de informes');  
INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('Asistente Informes', currval('seq_dictionary'), 'App.LocalGIS.Informes', 'LocalGIS.Informes.Login', 'DESKTOP', '/software/localgis-apps-asistenteinformes.jnlp', true, 'AsistenteInformesModule');
