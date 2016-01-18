
delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/software/localgis-apps-licenciasactividad.jnlp');
delete from apps WHERE path = '/software/localgis-apps-licenciasactividad.jnlp';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'M'||chr(243)||'dulo de Licencias de Actividad');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'M'||chr(242)||'dul de Llic'||chr(232)||'ncies d'||chr(180)||'Activitat');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'M'||chr(243)||'dulo de Licencias de Actividade');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]M'||chr(243)||'dulo de Licencias de Actividad');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'M'||chr(242)||'dul de Llic'||chr(232)||'ncies d'||chr(180)||'Activitat');  

INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('Licencias de Actividad', CURRVAL('seq_dictionary'), 'App.LocalGIS.LicenciasActividad', 'LocalGIS.LicenciasActividad.Login', 'DESKTOP', '/software/localgis-apps-licenciasactividad.jnlp', true, 'LicenciasActividadModule');

