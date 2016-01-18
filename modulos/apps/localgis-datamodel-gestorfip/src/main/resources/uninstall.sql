
DROP SCHEMA gestorfip CASCADE;
/*
DROP TABLE IF EXISTS gestorfip.tramite_determinacion_valoresreferencia CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinacion_gruposaplicacion CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinacion_regulacionesespecificas CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinacion_determinacionesreguladoras CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinacion_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_entidad_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_adscripciones CASCADE;
DROP TABLE IF EXISTS gestorfip.planeamientoencargado CASCADE;
DROP TABLE IF EXISTS gestorfip.planeamientovigente CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_aplicacionambitos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_unidades CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_operacionesentidades CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_operacionesdeterminaciones CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_caso_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_caso_vinculos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_caso_regimen_regimenesespecificos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_caso_regimenes CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionurbanistica_casos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_condicionesurbanisticas CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_determinaciones CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_entidades CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_documento_hojas CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.tramite CASCADE;
DROP TABLE IF EXISTS gestorfip.catalogosistematizado CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_instrumentos CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tipostramite CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_gruposdocumento CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposdocumento CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_operacionescaracteres CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposoperaciondeterminacion CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposoperacionentidad CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposentidad CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_caracteresdeterminacion CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_organigramaambitos CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_ambitos CASCADE;
DROP TABLE IF EXISTS gestorfip.diccionario_tiposambito CASCADE;
DROP TABLE IF EXISTS gestorfip.fip CASCADE;
DROP TABLE IF EXISTS gestorfip.ccaa_path_documentos CASCADE;
DROP TABLE IF EXISTS gestorfip.ccaa CASCADE;
DROP TABLE IF EXISTS gestorfip.config CASCADE;
DROP TABLE IF EXISTS gestorfip.versiones_uer CASCADE;
DROP TABLE IF EXISTS gestorfip.relacionambitomunicipio CASCADE;


DROP SEQUENCE IF EXISTS gestorfip.seq_catalogosistematizado CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_ambitos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_caracteresdeterminacion CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_gruposdocumento CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_instrumentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_operacionescaracteres CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_organigramaambitos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposambito CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposdocumento CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposentidad CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposoperaciondeterminacion CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tiposoperacionentidad CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_diccionario_tipostramite CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_fip CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_planeamientoencargado CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_planeamientovigente CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_adscripciones CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_aplicacionambitos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionesurbanisticas CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_caso_documentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_caso_regimen_regimenesespecifi CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_caso_regimenes CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_caso_vinculos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_condicionurbanistica_casos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_determinacionesreguladoras CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_documentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_gruposaplicacion CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_regulacionesespecificas CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinacion_valoresreferencia CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_determinaciones CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_documento_hojas CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_documentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_entidad_documentos CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_entidades CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_operacionesdeterminaciones CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_operacionesentidades CASCADE;
DROP SEQUENCE IF EXISTS gestorfip.seq_tramite_unidades CASCADE;
*/

/*
DROP TABLE IF EXISTS public.planeamiento_acciones_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_afecciones_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_alineacion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_ambito_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_categoria_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_clasificacion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_desarrollo_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_equidistribucion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_gestion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_otras_indicaciones_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_proteccion_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_sistemas_gen CASCADE;
DROP TABLE IF EXISTS public.planeamiento_zona_gen CASCADE;

DROP SEQUENCE IF EXISTS public.seq_planeamiento_acciones_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_afecciones_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_alineacion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_ambito_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_categoria_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_clasificacion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_desarrollo_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_equidistribucion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_gestion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_otras_indicaciones_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_proteccion_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_sistemas_gen CASCADE;
DROP SEQUENCE IF EXISTS public.seq_planeamiento_zona_gen CASCADE;
*/

--select * from public.MAPS where ID_MAP=151;
--select * from public.maps_layerfamilies_relations  where ID_MAP=151;
--select * from public.LAYERFAMILIES where id_layerfamily IN (select ID_LAYERFAMILY from public.maps_layerfamilies_relations  where ID_MAP=151);
delete from public.maps_layerfamilies_relations  where ID_MAP=151;
delete from public.LAYERFAMILIES where id_layerfamily IN (select ID_LAYERFAMILY from public.maps_layerfamilies_relations  where ID_MAP=151);
delete from public.MAPS where ID_MAP=151;

--select * from public.DICTIONARY  where TRADUCCION like '%Gestor FIP';
--select * from public.DICTIONARY where TRADUCCION like '%Familia Capas Gestor de Planeamiento - FIP';
--select * from public.DICTIONARY where TRADUCCION like '%Map Gestor Planeamiento FIP';

delete from public.DICTIONARY  where TRADUCCION like '%Gestor FIP';
delete from public.DICTIONARY where TRADUCCION like '%Familia Capas Gestor de Planeamiento - FIP';
delete from public.DICTIONARY where TRADUCCION like '%Map Gestor Planeamiento FIP';


--select * from public.DOMAINS where NAME = 'Gestor Planeamiento FIP';
--select * from public.DOMAINCATEGORY where ID = (select ID_CATEGORY from public.DOMAINS where NAME = 'Gestor Planeamiento FIP');
--select * from public.DICTIONARY where TRADUCCION like '%]Gestor Planeamiento FIP';
delete from public.DOMAINCATEGORY where ID = (select ID_CATEGORY from public.DOMAINS where NAME = 'Gestor Planeamiento FIP');
delete from public.DOMAINS where NAME = 'Gestor Planeamiento FIP';
delete from public.DICTIONARY where TRADUCCION like '%Gestor Planeamiento FIP';


--select * from public.r_group_perm where idperm in (select aux.idperm from public.r_acl_perm aux where aux.idacl = (select aux2.idacl from public.acl aux2 where aux2.name = 'GestorFip'));
delete from public.r_group_perm where idperm in (select aux.idperm from public.r_acl_perm aux where aux.idacl = (select aux2.idacl from public.acl aux2 where aux2.name = 'GestorFip'));


--select * from public.r_usr_perm where idacl = (select idacl from public.acl where name = 'GestorFip');
delete from public.r_usr_perm where idacl = (select idacl from public.acl where name = 'GestorFip');

--select * from public.iusergrouphdr where name = 'Gestor Fip';
--select * from public.iusergrouphdr where name = 'ConsultaGestorFip';
delete from public.iusergrouphdr where name = 'Gestor Fip';
delete from public.iusergrouphdr where name = 'ConsultaGestorFip';

--select * from public.r_acl_perm where idacl = (select idacl from public.acl where name = 'GestorFip');
delete from public.r_acl_perm where idacl = (select idacl from public.acl where name = 'GestorFip');

--SELECT * FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Login';
--SELECT * FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Importador';
--SELECT * FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Consulta';
--SELECT * FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Consulta';
delete FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Login';
delete FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Importador';
delete FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Consulta';
delete FROM public.usrgrouperm WHERE def='LocalGIS.GestorFip.Consulta';

--select * from public.app_acl where acl = (select idacl from public.acl where name = 'GestorFip');
delete from public.app_acl where acl = (select idacl from public.acl where name = 'GestorFip');
delete from public.appgeopista where def = 'Gestor Fip';

--select * from public.acl where name = 'GestorFip';
delete from public.acl where name = 'GestorFip';

--select * from public.apps where app = 'Fip';
delete from public.apps where app = 'Fip';
  