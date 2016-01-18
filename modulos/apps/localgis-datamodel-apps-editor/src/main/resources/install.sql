
delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/software/localgis-apps-editor.jnlp');
delete from apps WHERE path = '/software/localgis-apps-editor.jnlp';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'Editor GIS');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'Editor GIS');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'Editor GIS');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', 'Editor GIS');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'Editor GIS');  

INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('Editor GIS', CURRVAL('seq_dictionary'), 'App.LocalGIS.Editor', 'LocalGIS.Editor.Login', 'DESKTOP', '/software/localgis-apps-editor.jnlp', true, 'EditorModule');

