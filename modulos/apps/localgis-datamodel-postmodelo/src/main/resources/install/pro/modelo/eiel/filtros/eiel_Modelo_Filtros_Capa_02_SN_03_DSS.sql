set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- SERVICIOS DE SANEAMIENTO (eiel_t_saneam_serv)
delete from lcg_nodos_capas_campos where clave_capa='DSS';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('DSS','Ident.DSS','codprov','localgiseiel.panels.label.codprovMayus',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('DSS','Ident.DSS','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('DSS','Ident.DSS','codentidad','localgiseiel.panels.label.codentidad',0,null,'CodINEEntidad', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('DSS','Ident.DSS','codpoblamiento','localgiseiel.panels.label.codpoblamiento',0,null,'CodINEPoblamiento', FALSE, TRUE);
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Ident.DSS','pozos_registro','localgiseiel.tabla.servsaneam.columna.pozos_registo',4,'eiel_pozos_registro','Pozos'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Ident.DSS','sumideros','localgiseiel.tabla.servsaneam.columna.sumideros',4,'eiel_sumideros','Sumideros'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','aliv_c_acum','localgiseiel.tabla.servsaneam.columna.aliv_c_acum',4,'eiel_aliv_c_acum','AlivAcumulacion'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','aliv_s_acum','localgiseiel.tabla.servsaneam.columna.aliv_s_acum',4,'eiel_aliv_s_acum','AlivSinAcumulacion'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','calidad_serv','localgiseiel.tabla.servsaneam.columna.calidad_serv',4,'eiel_Calidad del servicio','Calidad'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','viviendas_s_conex','localgiseiel.tabla.servsaneam.columna.viviendas_s_conex',1,null,'VivNoConectadas'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','viviendas_c_conex','localgiseiel.tabla.servsaneam.columna.viviendas_c_conex',1,null,'VivNoConectadas'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','long_rs_deficit','localgiseiel.tabla.servsaneam.columna.long_rs_deficit',1,null,'LongDeficitaria');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','viviendas_def_conex','localgiseiel.tabla.servsaneam.columna.viviendas_def_conex',1,null,'VivDeficitarias'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','pobl_res_def_afect','localgiseiel.tabla.servsaneam.columna.pobl_res_def_afect',1,null,'PoblResDeficitaria'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','pobl_est_def_afect','localgiseiel.tabla.servsaneam.columna.pobl_est_def_afect',1,null,'PoblEstDeficitaria'); 			
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','caudal_total','localgiseiel.tabla.servsaneam.columna.caudal_total',1,null,'CaudalTotal');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','caudal_tratado','localgiseiel.tabla.servsaneam.columna.caudal_tratado',1,null,'CaudalTratado');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','c_reutilizado_urb','localgiseiel.tabla.servsaneam.columna.c_reutilizado_urb',1,null,'CaudalUrbano'); 			
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','c_reutilizado_rust','localgiseiel.tabla.servsaneam.columna.c_reutilizado_rust',1,null,'CaudalRustico'); 			
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Info.DSS','c_reutilizado_ind','localgiseiel.tabla.servsaneam.columna.c_reutilizado_ind',1,null,'CaudalIndustrial'); 	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Esta.DSS','observ','localgiseiel.tabla.servsaneam.columna.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Esta.DSS','fecha_revision','localgiseiel.tabla.servsaneam.columna.fecha_revision',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSS','Esta.DSS','estado_revision','localgiseiel.tabla.servsaneam.columna.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('DSS','Esta.DSS','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('DSS','Info.DSS','eiel_t_saneam_serv.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('DSS','Info.DSS','eiel_t_saneam_serv.codpoblamiento',null,-2,null,null,false,false); 

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.DSS';
insert into lcg_nodos_capas_traducciones values ('eiel.DSS','Servicios Saneamiento','es_ES');



