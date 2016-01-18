
set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- LONJAS Y MERCADOS (eiel_c_lm - eiel_t_lm)
delete from lcg_nodos_capas_campos where clave_capa='LM';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Ident.LM','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('LM','Ident.LM','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('LM','Ident.LM','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('LM','Ident.LM','codentidad','localgiseiel.panels.label.codineentidad',0,null,'CodINEEntidad', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('LM','Ident.LM','codpoblamiento','localgiseiel.panels.label.codinepoblamiento',0,null,'CodINEPoblamiento', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Ident.LM','orden_lm','localgiseiel.panels.label.orden',0,null,'Orden');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','nombre','localgiseiel.panels.label.nombre',0,null,'Nombre'); 		 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','tipo','localgiseiel.panels.label.tipo',4,'eiel_Lonja/Mercado/Feria','Tipo'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','titular','localgiseiel.panels.label.titular',4,'eiel_Titularidad Lonja Mercado','Titular');	
	insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','gestor','localgiseiel.panels.label.gestor',4,'eiel_Gestor Lonja Mercado','Gestion');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','s_cubierta','localgiseiel.panels.label.s_cubierta_id',1,null,'SuperficieCubierta'); 			
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','s_solar','localgiseiel.panels.label.s_solar_id',1,null,'SuperficieSolar'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','s_aire','localgiseiel.panels.label.s_aire_id',1,null,'SuperficieAireLibre');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','estado','localgiseiel.panels.label.estado_conserv',4,'eiel_Estado de conservación','Estado');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','acceso_s_ruedas','localgiseiel.panels.label.accesoruedas',4,'eiel_Acceso con silla de ruedas','Acceso_s_ruedas');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','obra_ejec','localgiseiel.panels.label.obraejec',4,'eiel_Obra ejecutada','Obra_ejec');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Info.LM','fecha_inst','localgiseiel.panels.label.fecha_instal',2,'TIPO_FECHA','FechaInstalacion'); 	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Esta.LM','observ','localgiseiel.panels.label.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Esta.LM','fecha_revision','localgiseiel.panels.label.fecha_revision',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('LM','Esta.LM','estado_revision','localgiseiel.panels.label.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('LM','Esta.LM','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('LM','Info.LM','eiel_t_lm.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('LM','Info.LM','eiel_t_lm.codpoblamiento',null,-2,null,null,false,false);	
	
-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.LM';
insert into lcg_nodos_capas_traducciones values ('eiel.LM','Lonjas y Mercados','es_ES');



