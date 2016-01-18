

delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/localgis-guiaurbana/private/');
delete from apps WHERE path = '/localgis-guiaurbana/private/';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'LocalGIS Gu'||chr(237)||'a Urbana Privada');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]LocalGIS Gu'||chr(237)||'a Urbana Privada');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]LocalGIS Gu'||chr(237)||'a Urbana Privada');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]LocalGIS Gu'||chr(237)||'a Urbana Privada');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', '[va]LocalGIS Gu'||chr(237)||'a Urbana Privada');  

INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('Gu'||chr(237)||'a Urbana', CURRVAL('seq_dictionary'), 'Visualizador', 'Geopista.Visualizador.Login', 'WEB', '/localgis-guiaurbana/private/', true, 'GuiaUrbanaModule');

