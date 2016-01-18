set client_encoding to 'utf8';


-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- ABASTECIMIENTO AUTONOMO
delete from lcg_nodos_capas_campos where clave_capa='AU';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Ident.AU','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('AU','Ident.AU','codprov','localgiseiel.panels.label.codprovMayus',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('AU','Ident.AU','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('AU','Ident.AU','codentidad','localgiseiel.panels.label.codentidad',0,null,'CodINEEntidad', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('AU','Ident.AU','codpoblamiento','localgiseiel.panels.label.codpoblamiento',0,null,'CodINEPoblamiento', FALSE, TRUE);
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Info.AU','aau_vivien','localgiseiel.panels.label.vivien',1,null,'Viviendas'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Info.AU','aau_pob_re','localgiseiel.panels.label.pobre',1,null,'PoblacionResidente'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Info.AU','aau_pob_es','localgiseiel.panels.label.pobes',1,null,'PoblacionEstacional'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Info.AU','aau_def_vi','localgiseiel.panels.label.defvi',1,null,'ViviendasDeficitarias'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Info.AU','aau_def_re','localgiseiel.panels.label.delre',1,null,'PoblacionResidenteDef'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Info.AU','aau_def_es','localgiseiel.panels.label.defes',1,null,'PoblacionEstacionalDef'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Info.AU','aau_fecont','localgiseiel.panels.label.fecont',1,null,'FuentesControladas'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Info.AU','aau_fencon','localgiseiel.panels.label.fencon',1,null,'FuentesNoControladas'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Info.AU','aau_caudal','localgiseiel.panels.label.caudal',4,'eiel_Disponibilidad de agua','SuficienciaCaudal'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Esta.AU','fecha_revision','localgiseiel.panels.label.fecha',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Esta.AU','estado_revision','localgiseiel.panels.label.estado',7,'eiel_Estado de revisión','EstadoRevision');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('AU','Esta.AU','observ','localgiseiel.panels.label.observ',0,null,'Observaciones');

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('AU','Esta.AU','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('AU','Info.AU','eiel_t_abast_au.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('AU','Info.AU','eiel_t_abast_au.codpoblamiento',null,-2,null,null,false,false);
	
	
	
	
	
-- Traducciones de los distintos elementos

delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.AU';
insert into lcg_nodos_capas_traducciones values ('eiel.AU','Abast. Autónomo','es_ES');
