set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- SERVICIOS DE SANEAMIENTO (eiel_c_saneam_pv - eiel_t_saneam_pv)
delete from lcg_nodos_capas_campos where clave_capa='PV';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Ident.PV','clave','localgiseiel.tabla.pv.columna.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('PV','Ident.PV','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('PV','Ident.PV','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Ident.PV','orden_ed','localgiseiel.panels.label.orden',0,null,'Orden'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Info.PV','tipo','localgiseiel.tabla.pv.columna.tipo_pv',4,'eiel_Tipo Vertido','Tipo'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Info.PV','zona','localgiseiel.tabla.pv.columna.zona_pv',4,'eiel_Zona del punto de vertido','Zona'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Info.PV','distancia_nucleo','localgiseiel.tabla.pv.columna.distancia_nucleo',1,null,'DistanciaNucleo'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Info.PV','fecha_ini_vertido','localgiseiel.tabla.pv.columna.fecha_ini_vertido',2,'TIPO_FECHA','FechaInicio'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Esta.PV','observ','localgiseiel.tabla.pv.columna.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Esta.PV','fecha_revision','localgiseiel.tabla.pv.columna.fecha_revision',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Esta.PV','estado_revision','localgiseiel.tabla.pv.columna.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('PV','Esta.PV','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Ident.PV','pozos_registro','localgiseiel.tabla.servsaneam.columna.pozos_registo',4,'eiel_pozos_registro','Pozos');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Ident.PV','sumideros','localgiseiel.tabla.servsaneam.columna.sumideros',4,'eiel_sumideros','Sumideros'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Info.PV','aliv_c_acum','localgiseiel.tabla.servsaneam.columna.aliv_c_acum',4,'eiel_aliv_c_acum','AlivAcumulacion'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PV','Info.PV','aliv_s_acum','localgiseiel.tabla.servsaneam.columna.aliv_s_acum',4,'eiel_aliv_s_acum','AlivSinAcumulacion'); 	
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('PV','Info.PV','eiel_tr_saneam_tem_pobl.codentidad_pobl',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('PV','Info.PV','eiel_tr_saneam_tem_pobl.codpoblamiento_pobl',null,-2,null,null,false,false); 	

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.PV';
insert into lcg_nodos_capas_traducciones values ('eiel.PV','Puntos de Vertido','es_ES');



