set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- COLECTOR (eiel_c_saneam_tcl - eiel_t_saneam_tcl)
delete from lcg_nodos_capas_campos where clave_capa='CL';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Ident.CL','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('CL','Ident.CL','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('CL','Ident.CL','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Ident.CL','tramo_cl','localgiseiel.panels.label.tramo',0,null,'CodOrden'); 	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('CL','Ident.CL','pmi','localgiseiel.panels.label.pmi',0,null,''); 	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('CL','Ident.CL','pmf','localgiseiel.panels.label.pmf',0,null,'');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Info.CL','titular','localgiseiel.panels.label.titular',4,'eiel_Titularidad','Titularidad'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Info.CL','gestor','localgiseiel.panels.label.gestor',4,'eiel_Gestión','Gestion'); 	
	insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Info.CL','estado','localgiseiel.panels.label.estado_conserv',4,'eiel_Estado de conservación','Estado'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Info.CL','material','localgiseiel.panels.label.material',4,'eiel_material','Material'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Info.CL','sist_impulsion','localgiseiel.panels.label.sist_impul',4,'eiel_sist_impulsion','Sist_impulsion'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Info.CL','tipo_red_interior','localgiseiel.panels.label.tipo_red',4,'eiel_tipo_red_interior','Tipo_red'); 	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('CL','Info.CL','longitud','localgiseiel.panels.label.long',3,null,null);	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('CL','Info.CL','diametro ','localgiseiel.panels.label.diam',1,null,null);	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('CL','Info.CL','cota_z ','localgiseiel.panels.label.cota_z',1,null,null);	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('CL','Info.CL','obra_ejec ','localgiseiel.panels.label.obra_ejec',0,null,null);	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Info.CL','tip_interceptor ','localgiseiel.panels.label.tip_interceptor',0,'eiel_Colector de Tipo interceptor','Tip_interceptor');		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Esta.CL','estado_revision','localgiseiel.panels.label.estado',4,'eiel_Estado de revisión','Estado_Revision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Esta.CL','fecha_inst','localgiseiel.panels.label.instalacion',2,'TIPO_FECHA','Fecha_inst'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CL','Esta.CL','observ','localgiseiel.panels.label.observ',0,null,'Observaciones'); 
-- NO TIENE (ES RARO)	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('CL','Info.CL','fecha_revision','localgiseiel.fecha_revision',2,null,'FechaRevision'); 	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('CL','Info.CL','estado_revision','localgiseiel.tabla.saneamautonomo.columna.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('CL','Info.CL','precision_dig','localgiseiel.panels.label.precision_dig',0,null,null);	


insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('CL','Esta.CL','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('CL','Info.CL','eiel_tr_saneam_tcl_pobl.codentidad_pobl',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('CL','Info.CL','eiel_tr_saneam_tcl_pobl.codpoblamiento_pobl',null,-2,null,null,false,false); 	

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.CL';
insert into lcg_nodos_capas_traducciones values ('eiel.CL','Colectores','es_ES');



