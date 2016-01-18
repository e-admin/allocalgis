set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- ELEMENTOS PUNTUALES
delete from lcg_nodos_capas_campos where clave_capa='PZ';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PZ','Ident.PZ','clave','localgiseiel.panels.label.clave',0,null,null); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PZ','Ident.PZ','orden_pr','localgiseiel.panels.label.orden',0,null,'Orden'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PZ','Ident.PZ','cota_z ','localgiseiel.panels.label.cota_z ',1,null,null);	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PZ','Ident.PZ','obra_ejec ','localgiseiel.panels.label.obra_ejec ',0,null,null);	
	insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PZ','Info.PZ','estado','localgiseiel.panels.label.estado',0,null,null); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PZ','Info.PZ','fecha_inst','localgiseiel.panels.label.fecha_inst',2,null,null); 		
	

-- NO TIENE (ES RARO)	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('PZ','Info.PZ','fecha_revision','localgiseiel.fecha_revision',2,null,'FechaRevision'); 	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('PZ','Info.PZ','estado_revision','localgiseiel.tabla.saneamautonomo.columna.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PZ','Info.PZ','precision_dig','localgiseiel.panels.label.precision_dig',0,null,null);

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('PZ','Esta.PZ','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('PZ','Info.PZ','eiel_t_nucl_encuest_2.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('PZ','Info.PZ','eiel_t_nucl_encuest_2.codpoblamiento',null,-2,null,null,false,false); 	

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.PZ';
insert into lcg_nodos_capas_traducciones values ('eiel.PZ','Elementos Puntuales','es_ES');



