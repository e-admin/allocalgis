

set client_encoding to 'utf8';

-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- Si el valor del campo BD es distinto de estos valores lo vamos a marcar como el campo codigo entidad y codigo nucleo
-- CAMPO_ENTIDAD=-1, CAMPO_NUCLEO=-2
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- CAPTACIONES (eiel_c_abast_ca - eiel_t_abast_ca)
delete from lcg_nodos_capas_campos where clave_capa='CA';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Ident.CA','clave','localgiseiel.panels.label.clave',0,null,'Clave');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('CA','Ident.CA','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('CA','Ident.CA','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Ident.CA','orden_ca','localgiseiel.panels.label.orden',0,null,'CodOrden'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','nombre','localgiseiel.panels.label.nombre',0,null,'Nombre'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','tipo','localgiseiel.panels.label.tipo',4,'eiel_Tipo de Captación','Tipo'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','titular','localgiseiel.panels.label.titular',4,'eiel_Titularidad','Titular'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','gestor','localgiseiel.panels.label.gestor',4,'eiel_Gestión','Gestor'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','sist_impulsion','localgiseiel.panels.label.sistcapta',4,'eiel_Sistema de impulsión','Sistema'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','estado','localgiseiel.panels.label.estado',4,'eiel_Estado de conservación','Estado'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','uso','localgiseiel.panels.label.uso',4,'eiel_Tipo de uso CA','TipoUso'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','proteccion','localgiseiel.panels.label.proteccion',4,'eiel_Proteccion CA','Proteccion'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','contador','localgiseiel.panels.label.contador',4,'eiel_Contador Abast','Contador'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','fecha_inst','localgiseiel.panels.label.fechainst',2,'TIPO_FECHA','FechaInst'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','cuenca','localgiseiel.tabla.captaciones.columna.cuenca',0,null,'Cuenca'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','n_expediente','localgiseiel.tabla.captaciones.columna.n_expediente',0,null,'N_expediente'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','n_inventario','localgiseiel.tabla.captaciones.columna.n_inventari',0,null,'N_inventario'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','cota','localgiseiel.tabla.captaciones.columna.cota',1,null,'Cota'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','profundidad','localgiseiel.tabla.captaciones.columna.profundidad',1,null,'Profundidad'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Info.CA','max_consumo','localgiseiel.tabla.captaciones.columna.max_consumo',1,null,'Max_consumo'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Esta.CA','observ','localgiseiel.panels.label.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Esta.CA','fecha_revision','localgiseiel.panels.label.fecha',2,'TIPO_FECHA','FechaRevision'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('CA','Esta.CA','estado_revision','localgiseiel.panels.label.estado',7,'eiel_Estado de revisión','EstadoRevision'); 
	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('CA','Esta.CA','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('CA','Info.CA','eiel_tr_abast_ca_pobl.codentidad_pobl',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('CA','Info.CA','eiel_tr_abast_ca_pobl.codpoblamiento_pobl',null,-2,null,null,false,false); 


-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.CA';
insert into lcg_nodos_capas_traducciones values ('eiel.CA','Captación','es_ES');
insert into lcg_nodos_capas_traducciones values ('localgiseiel.panels.label.tipo_cc','Tipo de Captación','es_ES');


