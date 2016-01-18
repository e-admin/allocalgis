set client_encoding to 'utf8';


-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- RED DE DISTRIBUCION (eiel_c_abast_tcn - eiel_t_abast_tcn)
delete from lcg_nodos_capas_campos where clave_capa='CN';


insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Ident.CN','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('CN','Ident.CN','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('CN','Ident.CN','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Ident.CN','tramo_cn','localgiseiel.panels.label.tramo',0,null,'Tramo_cn'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Ident.CN','pmi','localgiseiel.panels.label.pmi',3,null,'Pmi');		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Ident.CN','pmf','localgiseiel.panels.label.pmf',3,null,'Pmf');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Info.CN','titular','localgiseiel.panels.label.titular',4,'eiel_Titularidad','Titular'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Info.CN','gestor','localgiseiel.panels.label.gestor',4,'eiel_Gestor Tramos C','Gestor'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Info.CN','material','localgiseiel.panels.label.material',4,'eiel_material','Material'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Info.CN','longitud','localgiseiel.panels.label.long',3,null,'Longitud'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Info.CN','diametro','localgiseiel.panels.label.diam',1,null,'Diametro'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Info.CN','sis_trans','localgiseiel.panels.label.sist_trans',4,'eiel_sist_trans','Sist_trans'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('CN','Info.CN','cota_z','localgiseiel.panels.label.cota_z',0,null,null,TRUE,FALSE); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('CN','Info.CN','obra_ejec','localgiseiel.panels.label.obra_ejec',0,null,null,TRUE,FALSE); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('CN','Info.CN','precision_dig','localgiseiel.panels.label.precision_dig',1,null,null,TRUE,FALSE);	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Esta.CN','fecha_inst','localgiseiel.panels.label.fecha',2,'TIPO_FECHA','Fecha_revision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Esta.CN','observ','localgiseiel.panels.label.observ',0,null,'Observaciones');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CN','Esta.CN','estado','localgiseiel.panels.label.estado',4,'eiel_Estado de revisión','Estado'); 

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('CN','Esta.CN','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('CN','Info.CN','eiel_tr_abast_tcn_pobl.codentidad_pobl',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('CN','Info.CN','eiel_tr_abast_tcn_pobl.codpoblamiento_pobl',null,-2,null,null,false,false); 	
	
-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.CN';
insert into lcg_nodos_capas_traducciones values ('eiel.CN','Tramos de conducción','es_ES');
