set client_encoding to 'utf8';

-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- TRATAMIENTOS DE POTABILIZACION (eiel_c_abast_tp - eiel_t_abast_tp)
delete from lcg_nodos_capas_campos where clave_capa='TP';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Ident.TP','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('TP','Ident.TP','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('TP','Ident.TP','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Ident.TP','orden_tp','localgiseiel.panels.label.orden',0,null,'OrdenPotabilizadora'); 
	
	--WARNING ESTE NO TIENE METODO EN EL BEAN	!!!
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','tipo','localgiseiel.tabla.tp.columna.tipo_tp',4,'eiel_Automatización del equipamiento','Tipo'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','ubicacion','localgiseiel.tabla.tp.columna.s_desinf',4,'eiel_Ubicación del tratamiento de potabilización','Ubicacion'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','s_desinf','localgiseiel.panels.label.ubicacion',4,'eiel_s_desinf','SoloDesinfeccion'); 
	insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','categoria_a1','localgiseiel.tabla.tp.columna.categoria_a1',4,'eiel_categoria_a1','CategoriaA1'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','categoria_a2','localgiseiel.tabla.tp.columna.categoria_a2',4,'eiel_categoria_a2','CategoriaA3'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','categoria_a3','localgiseiel.tabla.tp.columna.categoria_a3',4,'eiel_categoria_a3','CategoriaA3'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','desaladora','localgiseiel.tabla.tp.columna.desaladora',4,'eiel_desaladora','Desaladora'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','otros','localgiseiel.tabla.tp.columna.otros_tp',4,'eiel_Otras','Otros'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','desinf_1','localgiseiel.tabla.tp.columna.desinf_1',4,'eiel_MÃ©todo de desinfección','MetodoDesinfeccion1'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','desinf_2','localgiseiel.tabla.tp.columna.desinf_2',4,'eiel_MÃ©todo de desinfección','MetodoDesinfeccion2'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','desinf_3','localgiseiel.tabla.tp.columna.desinf_3',4,'eiel_MÃ©todo de desinfección','MetodoDesinfeccion3'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','periodicidad','localgiseiel.tabla.tp.columna.periodicidad',4,'eiel_Periodicidad','Perioricidad'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','organismo_control','localgiseiel.tabla.tp.columna.organismo_control',4,'eiel_Control de calidad: Organismo','OrganismoControl'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','estado','localgiseiel.panels.label.estado',4,'eiel_Estado de conservación','Estado'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Info.TP','fecha_inst','localgiseiel.panels.label.fechainst',2,'TIPO_FECHA','FechaInstalacion');
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Esta.TP','fecha_revision','localgiseiel.panels.label.fecha',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Esta.TP','estado_revision','localgiseiel.panels.label.estado',7,'eiel_Estado de revisión','EstadoRevision');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TP','Esta.TP','observ','localgiseiel.panels.label.observ',0,null,'Observaciones');

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('TP','Esta.TP','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('TP','Info.TP','eiel_tr_abast_tp_pobl.codentidad_pobl',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('TP','Info.TP','eiel_tr_abast_tp_pobl.codpoblamiento_pobl',null,-2,null,null,false,false); 	

-- Traducciones de los distintos elementos
insert into lcg_nodos_capas_traducciones values ('eiel.TP','Tratamientos de Potabilización','es_ES');
