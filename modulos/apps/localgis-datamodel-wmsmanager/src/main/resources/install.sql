
delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/localgis-wmsmanager/admin/index.jsp');
delete from apps WHERE path = '/localgis-wmsmanager/admin/index.jsp';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'LocalGIS WMS Manager');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', '[ca]LocalGIS WMS Manager');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', '[gl]LocalGIS WMS Manager');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]LocalGIS WMS Manager');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', '[va]LocalGIS WMS Manager');  

INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('WMS Manager', CURRVAL('seq_dictionary'), 'Visualizador', 'Geopista.Visualizador.Login', 'WEB', '/localgis-wmsmanager/admin/index.jsp', true, 'LocalgisWmsManagerModule');
