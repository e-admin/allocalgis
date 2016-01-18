set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- RAMAL
delete from lcg_nodos_capas_campos where clave_capa='RS';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','clave','localgiseiel.panels.label.clave',0,null,null); 
	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('RS','Ident.RS','orden_ed','localgiseiel.tabla.pv.columna.orden',0,null,'Orden'); 
	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','tramo_rs','localgiseiel.panels.label.tramo',0,null,null); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','titular','localgiseiel.panels.label.titular',4,'eiel_Titularidad',null); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','gestor','localgiseiel.panels.label.gestor',4,'eiel_Gestión',null); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','estado','localgiseiel.panels.label.estado',4,'eiel_Estado de revisión',null); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','material','localgiseiel.panels.label.material',4,'eiel_material',null); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','sist_impulsion','localgiseiel.tabla.emisarios.columna.sist_impulsion',4,'eiel_sist_impulsion',null); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','tipo_red_interior','localgiseiel.tabla.emisarios.columna.tipo_red_interior',4,'eiel_tipo_red_interior',null); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','longitud','localgiseiel.panels.label.long',3,null,null);	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','diametro ','localgiseiel.panels.label.diametro ',1,null,null);	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','cota_z ','localgiseiel.panels.label.cota_z ',1,null,null);	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Ident.RS','obra_ejec ','localgiseiel.panels.label.obra_ejec ',0,null,null);	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Info.RS','fecha_inst','localgiseiel.panels.label.fecha_inst',2,null,null); 		
		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Esta.RS','observ','localgiseiel.panels.label.observ',0,null,null); 	

-- NO TIENE (ES RARO)	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('RS','Esta.RS','fecha_revision','localgiseiel.fecha_revision',2,null,'FechaRevision'); 	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('RS','Esta.RS','estado_revision','localgiseiel.tabla.saneamautonomo.columna.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RS','Info.RS','precision_dig','localgiseiel.panels.label.precision_dig',0,null,null);	

	
	insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('RS','Esta.RS','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('RS','Info.RS','eiel_c_saneam_rs.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('RS','Info.RS','eiel_c_saneam_rs.codpoblamiento',null,-2,null,null,false,false); 		
	
-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.RS';
insert into lcg_nodos_capas_traducciones values ('eiel.RS','Red de Ramales','es_ES');



