
set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- PARQUES Y JARDINES (eiel_c_pj - eiel_t_pj)
delete from lcg_nodos_capas_campos where clave_capa='PJ';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Ident.PJ','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('PJ','Ident.PJ','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('PJ','Ident.PJ','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('PJ','Ident.PJ','codentidad','localgiseiel.panels.label.codineentidad',0,null,'CodINEEntidad', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('PJ','Ident.PJ','codpoblamiento','localgiseiel.panels.label.codinepoblamiento',0,null,'CodINEPoblamiento', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Ident.PJ','orden_pj','localgiseiel.panels.label.orden',0,null,'CodOrden');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','nombre','localgiseiel.panels.label.nombre',0,null,'Nombre'); 		 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','clase','localgiseiel.panels.label.tipo',4,'eiel_Tipo de parque','Tipo'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','titular','localgiseiel.panels.label.titular',4,'eiel_Titularidad_General_equip','Titularidad');	
	insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','gestor','localgiseiel.panels.label.gestor',4,'eiel_Gestor_General_equip','Gestion');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','s_cubierta','localgiseiel.panels.label.s_cubierta_id',1,null,'SupCubierta'); 			
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','s_solar','localgiseiel.panels.label.s_solar_id',1,null,'SupSolar'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','s_aire','localgiseiel.panels.label.s_aire_id',1,null,'SupLibre');		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','agua','localgiseiel.tabla.pj.columna.agua',0,'eiel_Disponibilidad de agua','Agua');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','saneamiento','localgiseiel.tabla.pj.columna.saneamiento',0,'eiel_saneamiento','Saneamiento');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','electricidad','localgiseiel.tabla.pj.columna.electricidad',0,'eiel_electricidad','Electricidad');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','comedor','localgiseiel.tabla.pj.columna.comedor',0,'eiel_comedor','Comedor');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','juegos_inf','localgiseiel.tabla.pj.columna.juegos_inf',0,'eiel_juegos_inf','JuegosInf');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','otras','localgiseiel.tabla.pj.columna.otras_pj',0,'eiel_Otros Nivel','Otros');		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','estado','localgiseiel.panels.label.estado_conserv',4,'eiel_Estado de conservación','Estado');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','acceso_s_ruedas','localgiseiel.panels.label.accesoruedas',4,'eiel_Acceso con silla de ruedas','AccesoSilla');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Info.PJ','obra_ejec','localgiseiel.panels.label.obraejec',4,'eiel_Obra ejecutada','Obra_ejec');
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Esta.PJ','observ','localgiseiel.panels.label.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Esta.PJ','fecha_revision','localgiseiel.panels.label.fecha_revision',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('PJ','Esta.PJ','estado_revision','localgiseiel.panels.label.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('PJ','Esta.PJ','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('PJ','Info.PJ','eiel_t_pj.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('PJ','Info.PJ','eiel_t_pj.codpoblamiento',null,-2,null,null,false,false);		

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.PJ';
insert into lcg_nodos_capas_traducciones values ('eiel.PJ','Parques y Jardines','es_ES');



