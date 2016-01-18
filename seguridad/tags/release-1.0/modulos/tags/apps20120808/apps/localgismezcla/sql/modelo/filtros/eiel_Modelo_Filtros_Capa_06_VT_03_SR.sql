
set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- SERVICIOS RECOGIDA BASURAS (eiel_c_rb_serv - eiel_t_rb_serv)
delete from lcg_nodos_capas_campos where clave_capa='SR';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('SR','Ident.SR','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('SR','Ident.SR','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('SR','Ident.SR','codentidad','localgiseiel.panels.label.codineentidad',0,null,'CodINEEntidad', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('SR','Ident.SR','codpoblamiento','localgiseiel.panels.label.codinepoblamiento',0,null,'CodINEPoblamiento', FALSE, TRUE);
		 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SR','Info.SR','srb_viviendas_afect','localgiseiel.tabla.servbasura.columna.srb_viviendas_afec',1,null,'VivSinServicio');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SR','Info.SR','srb_pob_res_afect','localgiseiel.tabla.servbasura.columna.srb_pob_res_afect',1,null,'PoblResSinServicio');		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SR','Info.SR','srb_pob_est_afect','localgiseiel.tabla.servbasura.columna.srb_pob_est_afect',1,null,'PoblEstSinServicio'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SR','Info.SR','serv_limp_calles','localgiseiel.tabla.servbasura.columna.serv_limp_calles',0,'eiel_Servicio de limpieza de calles','ServLimpCalles'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SR','Info.SR','plantilla_serv_limp','localgiseiel.tabla.servbasura.columna.plantilla_serv_limp',1,null,'Plantilla');
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SR','Esta.SR','observ','localgiseiel.panels.label.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SR','Esta.SR','fecha_revision','localgiseiel.panels.label.fecha_revision',2,'TIPO_FECHA','Fecharevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('SR','Esta.SR','estado_revision','localgiseiel.panels.label.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('SR','Esta.SR','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.SR';
insert into lcg_nodos_capas_traducciones values ('eiel.SR','Servicios Recogida Basuras','es_ES');



