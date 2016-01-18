
set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- VERTEDEROS (eiel_c_vt - eiel_t_vt)
delete from lcg_nodos_capas_campos where clave_capa='ALUM';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Ident.ALUM','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ALUM','Ident.ALUM','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ALUM','Ident.ALUM','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Ident.ALUM','orden_vt','localgiseiel.panels.label.orden',0,null,'CodOrden');	
		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Info.ALUM','reductor_flujo','localgiseiel.tabla.vertederos.columna.tipo_vt',4,'eiel_Reductor de flujo en la instalación','reductor_flujo');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ALUM','Info.ALUM','tipo_luminaria','localgiseiel.tabla.vertederos.columna.tipo_vt',4,'eiel_tipo_luminaria','tipo_luminaria');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('ALUM','Esta.ALUM','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
	--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('ALUM','Info.ALUM','eiel_c_alum_pl.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('ALUM','Info.ALUM','eiel_c_alum_pl.codpoblamiento',null,-2,null,null,false,false); 


-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.ALUM';
insert into lcg_nodos_capas_traducciones values ('eiel.ALUM','Alumbrado','es_ES');



