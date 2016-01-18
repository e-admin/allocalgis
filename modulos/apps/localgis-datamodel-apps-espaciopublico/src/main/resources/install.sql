
delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/software/localgis-apps-espaciopublico.jnlp');
delete from apps WHERE path = '/software/localgis-apps-espaciopublico.jnlp';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'M'||chr(243)||'dulo de Gesti'||chr(243)||'n del Espacio P'||chr(250)||'blico');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'M'||chr(242)||'dul d'||chr(180)||'Espai P'||chr(250)||'blic');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'M'||chr(243)||'dulo de Gesti'||chr(243)||'n del Espacio P'||chr(250)||'blico');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]M'||chr(243)||'dulo de Gesti'||chr(243)||'n del Espacio P'||chr(250)||'blico');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'M'||chr(242)||'dul d'||chr(180)||'Espai P'||chr(250)||'blic');  

INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('Gesti'||chr(243)||'n del Espacio P'||chr(250)||'blico', CURRVAL('seq_dictionary'), 'Gestion de Espacio Publico', 'localgis.espaciopublico.login', 'DESKTOP', '/software/localgis-apps-espaciopublico.jnlp', true, 'EspacioPublicoModule');
