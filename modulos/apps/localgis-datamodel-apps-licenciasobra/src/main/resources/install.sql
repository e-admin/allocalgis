
delete from dictionary where id_vocablo = (select id_dictionary from apps WHERE path = '/software/localgis-apps-licenciasobra.jnlp');
delete from apps WHERE path = '/software/localgis-apps-licenciasobra.jnlp';

select setval('public.seq_dictionary',(SELECT cast(max(id_vocablo)as bigint) FROM public.dictionary));

INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (NEXTVAL('seq_dictionary'), 'es_ES', 'M'||chr(243)||'dulo de Licencias de Obra Mayor y Obra Menor');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'ca_ES', 'M'||chr(242)||'dul de Llic'||chr(232)||'ncies d'||chr(180)||'Obra Major i Obra Menor');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'gl_ES', 'M'||chr(243)||'dulo de Licencias de Obra Mayor y Obra Menor');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'eu_ES', '[eu]M'||chr(243)||'dulo de Licencias de Obra Mayor y Obra Menor');
INSERT INTO dictionary (id_vocablo, locale, traduccion) VALUES (CURRVAL('seq_dictionary'), 'va_ES', 'M'||chr(242)||'dul de Llic'||chr(232)||'ncies d'||chr(180)||'Obra Major i Obra Menor');  

INSERT INTO apps(app, id_dictionary, acl, perm, app_type, path, active, install_name)  VALUES 
('Licencias de Obra', CURRVAL('seq_dictionary'), 'App.LocalGIS.LicenciasObra', 'LocalGIS.LicenciasObra.Login', 'DESKTOP', '/software/localgis-apps-licenciasobra.jnlp', true, 'LicenciasObraModule');

