set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- SANEAMIENTO AUTONOMO (eiel_t_saneam_au)
delete from lcg_nodos_capas_campos where clave_capa='SN';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','clave','localgiseiel.tabla.saneamautonomo.columna.clave',0,null,'Clave'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('SN','Ident.SN','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('SN','Ident.SN','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('SN','Ident.SN','codentidad','localgiseiel.panels.label.codentidad',0,null,'CodINEEntidad', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('SN','Ident.SN','codpoblamiento','localgiseiel.panels.label.codpoblamiento',0,null,'CodINEPoblamiento', FALSE, TRUE);

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','tipo','localgiseiel.tabla.saneamautonomo.columna.tipo_sau',4,'eiel_Tipos de saneamiento autónomo','Tipo'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','estado_sau','localgiseiel.tabla.saneamautonomo.columna.estado_sau',4,'eiel_Estado de conservación','Estado'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','adecuacion_sau','localgiseiel.tabla.saneamautonomo.columna.adecuacion_sau',4,'eiel_Adecuación','Adecuacion'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','sau_vivien','localgiseiel.tabla.saneamautonomo.columna.sau_vivien',1,null,'Viviendas'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','sau_pob_re','localgiseiel.tabla.saneamautonomo.columna.sau_pob_re',1,null,'PoblResidente'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','sau_pob_es','localgiseiel.tabla.saneamautonomo.columna.sau_pob_es',1,null,'PoblEstacional'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','sau_vi_def','localgiseiel.tabla.saneamautonomo.columna.sau_vi_def',1,null,'VivDeficitarias'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','sau_pob_re_def','localgiseiel.tabla.saneamautonomo.columna.sau_pob_re_def',1,null,'PoblResDeficitaria'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Ident.SN','sau_pob_es_def','localgiseiel.tabla.saneamautonomo.columna.sau_pob_es_def',1,null,'PoblEstDeficitaria'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Info.SN','fecha_inst','localgiseiel.tabla.saneamautonomo.columna.fecha_inst',2,'TIPO_FECHA','FechaInstalacion'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Esta.SN','observ','localgiseiel.tabla.saneamautonomo.columna.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Esta.SN','fecha_revision','localgiseiel.tabla.saneamautonomo.columna.fecha_revision',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SN','Esta.SN','estado_revision','localgiseiel.tabla.saneamautonomo.columna.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('SN','Esta.SN','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('SN','Info.SN','eiel_t_saneam_au.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('SN','Info.SN','eiel_t_saneam_au.codpoblamiento',null,-2,null,null,false,false);	

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.SN';
insert into lcg_nodos_capas_traducciones values ('eiel.SN','Saneamiento Autónomo','es_ES');



