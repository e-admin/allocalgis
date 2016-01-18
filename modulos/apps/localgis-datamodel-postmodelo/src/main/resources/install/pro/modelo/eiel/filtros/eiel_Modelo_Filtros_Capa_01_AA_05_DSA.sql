set client_encoding to 'utf8';


-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- DATOS DE SERVICIO DE ABASTECIMIENTO (eiel_t_abast_serv)
delete from lcg_nodos_capas_campos where clave_capa='DSA';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('DSA','Ident.DSA','codprov','localgiseiel.panels.label.codprovMayus',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('DSA','Ident.DSA','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('DSA','Ident.DSA','codentidad','localgiseiel.panels.label.codentidad',0,null,'CodINEEntidad', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('DSA','Ident.DSA','codpoblamiento','localgiseiel.panels.label.codpoblamiento',0,null,'CodINEPoblamiento', FALSE, TRUE);

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','viviendas_c_conex','localgiseiel.panels.label.viv_con_conex',1,null,'ViviendasConectadas'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','viviendas_s_conexion','localgiseiel.panels.label.viv_sin_conex',1,null,'ViviendasNoConectadas'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','consumo_inv','localgiseiel.panels.label.consumo_inv',1,null,'ConsumoInvierno'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','consumo_verano','localgiseiel.panels.label.consumo_verano',1,null,'ConsumoVerano'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','viv_exceso_pres','localgiseiel.panels.label.viv_exc_presion',1,null,'ViviendasExcesoPresion'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','viv_defic_presion','localgiseiel.panels.label.viv_def_presion',1,null,'ViviendasDeficitPresion'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','perdidas_agua','localgiseiel.panels.label.perdidas_agua',1,null,'PerdidasAgua'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','calidad_serv','localgiseiel.panels.label.calidad_serv',4,'eiel_Calidad del servicio','CalidadServicio'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','long_deficit','localgiseiel.panels.label.long_def',1,null,'LongitudDeficitaria'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','viv_deficitarias','localgiseiel.panels.label.viv_def',1,null,'ViviendasDeficitarias'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','pobl_res_afect','localgiseiel.panels.label.pobl_res_def',1,null,'PoblacionResidenteDeficitaria'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Info.DSA','pobl_est_afect','localgiseiel.panels.label.pobl_est_def',1,null,'PoblacionEstacionalDeficitaria'); 	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Esta.DSA','fecha_revision','localgiseiel.panels.label.fecha',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Esta.DSA','estado_revision','localgiseiel.panels.label.estado',7,'eiel_Estado de revisión','EstadoRevision');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('DSA','Esta.DSA','observ','localgiseiel.panels.label.observ',0,null,'Observaciones');	
	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('DSA','Esta.DSA','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('DSA','Info.DSA','eiel_t_abast_serv.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('DSA','Info.DSA','eiel_t_abast_serv.codpoblamiento',null,-2,null,null,false,false); 		
	

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.DSA';
insert into lcg_nodos_capas_traducciones values ('eiel.DSA','Datos Serv. Abast.','es_ES');
