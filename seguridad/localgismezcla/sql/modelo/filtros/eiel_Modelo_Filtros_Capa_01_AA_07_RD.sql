set client_encoding to 'utf8';


-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- RED DE DISTRIBUCION (NO TIENE DATOS ALFANUMERICOS)
delete from lcg_nodos_capas_campos where clave_capa='RD';


insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Ident.RD','clave','localgiseiel.panels.label.clave',0,null,null); 
	
-- No tiene ORDEN	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('RD','Ident.RD','orden_rd','localgiseiel.material',0,null,'OrdenPotabilizadora'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','tramo_rd','localgiseiel.panels.label.tramo',0,null,null); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','titular','localgiseiel.titular',4,'eiel_Titularidad','Titular'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','gestor','localgiseiel.gestor',4,'eiel_Gestión','Gestor'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','estado','localgiseiel.panels.label.estado',0,null,null); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','material','localgiseiel.material',4,'eiel_material','Material'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','longitud','localgiseiel.longitud',3,null,null); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','diametro','localgiseiel.diametro',1,null,null); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','sis_trans','localgiseiel.sis_trans',0,null,null); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','cota_z','localgiseiel.cota_z',0,null,null); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Info.RD','obra_ejec','localgiseiel.obra_ejec',0,null,null); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Esta.RD','fecha_inst','localgiseiel.panels.label.fecha',2,null,'FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Esta.RD','observ','localgiseiel.panels.label.observ',0,null,'Observaciones');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('RD','Esta.RD','precision_dig','localgiseiel.precision_dig',1,null,null);	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('RD','Esta.RD','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 	
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('RD','Info.RD','eiel_c_abast_rd.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('RD','Info.RD','eiel_c_abast_rd.codpoblamiento',null,-2,null,null,false,false); 		
		

-- Traducciones de los distintos elementos

delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.RD';
insert into lcg_nodos_capas_traducciones values ('eiel.RD','Red de Distribución','es_ES');
