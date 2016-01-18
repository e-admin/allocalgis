
set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- VERTEDEROS (eiel_c_vt - eiel_t_vt)
delete from lcg_nodos_capas_campos where clave_capa='VT';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Ident.VT','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('VT','Ident.VT','codprov','localgiseiel.panels.label.codprovMayus',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('VT','Ident.VT','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Ident.VT','orden_vt','localgiseiel.panels.label.orden',0,null,'CodOrden');	
		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','tipo','localgiseiel.tabla.vertederos.columna.tipo_vt',4,'eiel_Tipo de vertedero','Tipo');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','gestor','localgiseiel.tabla.vertederos.columna.gestor',4,'eiel_Gestor del Vertedero','Gestion');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','titular','localgiseiel.tabla.vertederos.columna.titular',4,'eiel_Titular del vertedero','Titularidad');		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','olores','localgiseiel.tabla.vertederos.columna.olores',4,'eiel_olor','Olores');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','humos','localgiseiel.tabla.vertederos.columna.humos',4,'eiel_humo','Humos');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','cont_anima','localgiseiel.tabla.vertederos.columna.cont_anima',4,'eiel_cont animal','ContAnimal'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','r_inun','localgiseiel.tabla.vertederos.columna.r_inun',4,'eiel_riesgo inundación','RsgoInundacion');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','filtracion','localgiseiel.tabla.vertederos.columna.filtracion',4,'eiel_filtraciones','Filtraciones');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','impacto_v','localgiseiel.tabla.vertederos.columna.impacto_v',4,'eiel_impacto visual','ImptVisual'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','frec_averia','localgiseiel.tabla.vertederos.columna.frec_averia',4,'eiel_frecuentes averías','FrecAverias'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','estado','localgiseiel.tabla.vertederos.columna.estado',4,'eiel_Estado de conservación','Estado');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','inestable','localgiseiel.tabla.vertederos.columna.inestable',4,'eiel_inestabilidad','Inestabilidad');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','saturacion','localgiseiel.tabla.vertederos.columna.saturacion',4,'eiel_saturación','Saturacion'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','otros','localgiseiel.tabla.vertederos.columna.otros',4,'eiel_Otros vertedero','Otros'); 			
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','categoria','localgiseiel.tabla.vertederos.columna.categoria',4,'eiel_Categoria del vertedero','Categoria');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','actividad','localgiseiel.tabla.vertederos.columna.actividad',4,'eiel_Situación de la actividad de la instalación','Actividad');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','capac_ampl','localgiseiel.tabla.vertederos.columna.capac_ampl',4,'eiel_Posibilidad de ampliación','PosbAmpliacion'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','capac_tot','localgiseiel.tabla.vertederos.columna.capac_tot',1,null,'CapTotal'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','capac_tot_porc','localgiseiel.tabla.vertederos.columna.capac_tot_porc',1,null,'CapOcupada'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','vida_util','localgiseiel.tabla.vertederos.columna.vida_util',7,null,'VidaUtil');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','capac_transf','localgiseiel.tabla.vertederos.columna.capac_transf',1,null,'CapTransform');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','fecha_apertura','localgiseiel.tabla.vertederos.columna.fecha_apertura',1,null,'FechaApertura');		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Info.VT','obra_ejec','localgiseiel.tabla.vertederos.columna.obra_ejec',0,'eiel_Obra ejecutada','Obra_ejecutada');
		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Esta.VT','observ','localgiseiel.tabla.vertederos.columna.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Esta.VT','estado_revision','localgiseiel.tabla.vertederos.columna.estado_revision',7,'eiel_Estado de revisión','EstadoRevision');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('VT','Esta.VT','fecha_revision','localgiseiel.tabla.vertederos.columna.fecha_revision',2,'TIPO_FECHA','FechaRevision');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('VT','Esta.VT','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 	

--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('VT','Info.VT','eiel_tr_vt.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('VT','Info.VT','eiel_tr_vt.codpoblamiento',null,-2,null,null,false,false); 
	
	
-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.VT';
insert into lcg_nodos_capas_traducciones values ('eiel.VT','Vertederos','es_ES');



