
set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- INSTALACIONES DEPORTIVAS (eiel_c_id - eiel_t_id)
delete from lcg_nodos_capas_campos where clave_capa='ID';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Ident.ID','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ID','Ident.ID','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ID','Ident.ID','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ID','Ident.ID','codentidad','localgiseiel.panels.label.codineentidad',0,null,'CodINEEntidad', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('ID','Ident.ID','codpoblamiento','localgiseiel.panels.label.codinepoblamiento',0,null,'CodINEPoblamiento', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Ident.ID','orden_id','localgiseiel.panels.label.orden',0,null,'OrdenIdDeportes');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','nombre','localgiseiel.panels.label.nombre',0,null,'Nombre'); 		 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','tipo','localgiseiel.panels.label.tipo',4,'eiel_Tipo de instalaciones deportivas','Tipo'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','titular','localgiseiel.panels.label.titular',4,'eiel_Titularidad_General_equip','Titular');	
	insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','gestor','localgiseiel.panels.label.gestor',4,'eiel_Gestor_General_equip','Gestor');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','s_cubierta','localgiseiel.panels.label.s_cubierta_id',1,null,'SupCubierta'); 			
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','s_solar','localgiseiel.panels.label.s_solar_id',1,null,'SupSolar'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','s_aire','localgiseiel.panels.label.s_aire_id',1,null,'SupAire');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','estado','localgiseiel.panels.label.estado_conserv',4,'eiel_Estado de conservación','Estado');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','acceso_s_ruedas','localgiseiel.panels.label.accesoruedas',4,'eiel_Acceso con silla de ruedas','Acceso_s_ruedas');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','obra_ejec','localgiseiel.panels.label.obraejec',4,'eiel_Obra ejecutada','Obra_ejec');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Info.ID','fecha_inst','localgiseiel.panels.label.fecha_instal',2,'TIPO_FECHA','FechaInstalacion'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Esta.ID','observ','localgiseiel.panels.label.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Esta.ID','fecha_revision','localgiseiel.panels.label.fecha_revision',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('ID','Esta.ID','estado_revision','localgiseiel.panels.label.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

-- Gestion de Usos
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes, aplicaMovilidad)
	values ('ID','Info.ID','uso','localgiseiel.panels.label.uso',4,'eiel_Tipo Deporte','ListaUsos',TRUE,FALSE);

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('ID','Esta.ID','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('ID','Info.ID','eiel_t_id.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('ID','Info.ID','eiel_t_id.codpoblamiento',null,-2,null,null,false,false);		

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.ID';
insert into lcg_nodos_capas_traducciones values ('eiel.ID','Instalaciones Deportivas','es_ES');



