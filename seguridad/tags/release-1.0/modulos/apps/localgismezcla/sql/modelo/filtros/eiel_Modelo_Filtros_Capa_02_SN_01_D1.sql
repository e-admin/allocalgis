set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- DEPURADORA ((eiel_t1_saneam_ed + eiel_t2_saneam_ed))
delete from lcg_nodos_capas_campos where clave_capa='D1';

----- DATOS DE LA DEPURADORA 1

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','clave','localgiseiel.panels.label.clave',0,null,'Clave','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','orden_ed','localgiseiel.panels.label.orden',0,null,'CodOrden','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, tabla, bean, aplicaInformes, aplicaMovilidad)
	values ('D1','Ident.D1','codprov','localgiseiel.panels.label.codprovMayus',0,null,'CodINEProvincia','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, tabla, bean, aplicaInformes, aplicaMovilidad)
	values ('D1','Ident.D1','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL', FALSE, TRUE);
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_pr_1','localgiseiel.panels.label.tratpr1',4,'eiel_Sist. de depuración, Tratamiento primario','TratPrimario1','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_pr_2','localgiseiel.panels.label.tratpr2',4,'eiel_Sist. de depuración, Tratamiento primario','TratPrimario2','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_pr_3','localgiseiel.panels.label.tratpr2',4,'eiel_Sist. de depuración, Tratamiento primario','TratPrimario3','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_sc_1','localgiseiel.panels.label.tratsc1',4,'eiel_Sist. de depuración, Tratamiento secundario','TratSecundario1','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_sc_2','localgiseiel.panels.label.tratsc2',4,'eiel_Sist. de depuración, Tratamiento secundario','TratSecundario2','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_sc_3','localgiseiel.panels.label.tratsc3',4,'eiel_Sist. de depuración, Tratamiento secundario','TratSecundario3','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_av_1','localgiseiel.panels.label.tratav1',4,'eiel_Sist. de depuración, Tratamientos avanzados','TratAvanzado1','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_av_2','localgiseiel.panels.label.tratav2',4,'eiel_Sist. de depuración, Tratamientos avanzados','TratAvanzado2','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_av_3','localgiseiel.panels.label.tratav3',4,'eiel_Sist. de depuración, Tratamientos avanzados','TratAvanzado3','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','proc_cm_1','localgiseiel.panels.label.proccm1',4,'eiel_Sist. de depuración, Procesos complementarios','ProcComplementario1','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','proc_cm_2','localgiseiel.panels.label.proccm2',4,'eiel_Sist. de depuración, Procesos complementarios','ProcComplementario2','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','proc_cm_3','localgiseiel.panels.label.proccm3',4,'eiel_Sist. de depuración, Procesos complementarios','ProcComplementario3','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_ld_1','localgiseiel.panels.label.tratld1',4,'eiel_Sist. de depuración, Tratamientos de fangos o lodos','TratLodos1','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_ld_2','localgiseiel.panels.label.tratld2',4,'eiel_Sist. de depuración, Tratamientos de fangos o lodos','TratLodos2','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','trat_ld_3','localgiseiel.panels.label.tratld3',4,'eiel_Sist. de depuración, Tratamientos de fangos o lodos','TratLodos3','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','titular','localgiseiel.tabla.depuradora2.columna.titular_ed',4,'eiel_Titularidad','Titular','eiel_t2_saneam_ed','com.geopista.app.eiel.beans.Depuradora2EIEL'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Ident.D1','gestor','localgiseiel.tabla.depuradora2.columna.gestor_ed',4,'eiel_Gestión','Gestor','eiel_t2_saneam_ed','com.geopista.app.eiel.beans.Depuradora2EIEL'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Info.D1','capacidad','localgiseiel.tabla.depuradora2.columna.capacidad_ed',1,null,'Capacidad','eiel_t2_saneam_ed','com.geopista.app.eiel.beans.Depuradora2EIEL'); 		
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Esta.D1','observ','localgiseiel.panels.label.observ',0,null,'Observaciones','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Esta.D1','fecha_revision','localgiseiel.panels.label.fecha',2,null,'FechaRevision','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,tabla,bean)
	values ('D1','Esta.D1','estado_revision','localgiseiel.panels.label.estado',7,'eiel_Estado de revisión','EstadoRevision','eiel_t1_saneam_ed','com.geopista.app.eiel.beans.Depuradora1EIEL'); 

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('D1','Esta.D1','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('D1','Info.D1','eiel_tr_saneam_ed_pobl.codentidad_pobl',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('D1','Info.D1','eiel_tr_saneam_ed_pobl.codpoblamiento_pobl',null,-2,null,null,false,false); 		
	
	
-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.D1';
insert into lcg_nodos_capas_traducciones values ('eiel.D1','Depuradoras 1','es_ES');



