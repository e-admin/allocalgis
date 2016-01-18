set client_encoding to 'utf8';


-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- NUCLEOS ENCUESTADOS 2
delete from lcg_nodos_capas_campos where clave_capa='P2';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('P2','Ident.P2','codprov','localgiseiel.panels.label.codprovMayus',0,null,'CodINEProvincia', FALSE, TRUE);

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_caudal','localgiseiel.panels.label.vivien',4,'eiel_Suficiencia de caudal para el abastecimiento autónomo','DisponibilidadCaudal'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_restri','localgiseiel.panels.label.restricciones',4,'eiel_Restricciones de agua','RestriccionesAgua'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_contad','localgiseiel.panels.label.cont',4,'eiel_Contador Abast','Contadores'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_tasa','localgiseiel.panels.label.tasa',4,'eiel_Tasa','Tasa'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_instal','localgiseiel.panels.label.annoInstal',2,'TIPO_FECHA','AnnoInstalacion'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_hidran','localgiseiel.panels.label.hidrantes',4,'eiel_Suficiencia de caudal para el abastecimiento autónomo','Hidrantes'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_est_hi','localgiseiel.panels.label.estadoHid',4,'eiel_Estado de conservación','EstadoHidrantes');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_valvul','localgiseiel.panels.label.valvulas',4,'eiel_Suficiencia de caudal para el abastecimiento autónomo','Valvulas'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_est_va','localgiseiel.panels.label.estadoVal',4,'eiel_Estado de conservación','EstadoValvulas');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_bocasr','localgiseiel.panels.label.bocasRiego',4,'eiel_Suficiencia de caudal para el abastecimiento autónomo','BocasRiego'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','aag_est_bo','localgiseiel.panels.label.estadoBoc',4,'eiel_Estado de conservación','EstadoBocasRiego');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Info.P2','cisterna','localgiseiel.panels.label.cisterna',4,'eiel_Cisterna','Cisterna');
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Esta.P2','fecha_revision','localgiseiel.panels.label.fecha',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Esta.P2','estado_revision','localgiseiel.panels.label.estado',7,'eiel_Estado de revisión','EstadoRevision');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('P2','Esta.P2','observ','localgiseiel.panels.label.observ',0,null,'Observaciones');	
	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('P2','Esta.P2','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 	
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('P2','Info.P2','eiel_t_nucl_encuest_2.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('P2','Info.P2','eiel_t_nucl_encuest_2.codpoblamiento',null,-2,null,null,false,false); 		
	
	
	

-- Traducciones de los distintos elementos

delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.P2';
insert into lcg_nodos_capas_traducciones values ('eiel.P2','Información Abast.','es_ES');
