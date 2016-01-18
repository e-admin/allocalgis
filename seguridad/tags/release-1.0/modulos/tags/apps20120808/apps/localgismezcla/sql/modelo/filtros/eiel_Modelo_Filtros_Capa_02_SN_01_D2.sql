set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- DEPURADORA2
delete from lcg_nodos_capas_campos where clave_capa='D2';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','orden_ed','localgiseiel.panels.label.orden',0,null,'CodOrden'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','titular','localgiseiel.tabla.depuradora2.columna.titular_ed',4,'eiel_Titularidad','Titular'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','gestor','localgiseiel.tabla.depuradora2.columna.gestor_ed',4,'eiel_Gestión','Gestor'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Info.D2','capacidad','localgiseiel.tabla.depuradora2.columna.capacidad_ed',1,null,'Capacidad'); 		
	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','problem_1','localgiseiel.tabla.depuradora2.columna.problem_1',4,'eiel_EDAR problemas existentes','Problemas1'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','problem_2','localgiseiel.tabla.depuradora2.columna.problem_2',4,'eiel_EDAR problemas existentes','Problemas2'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','problem_3','localgiseiel.tabla.depuradora2.columna.problem_3',4,'eiel_EDAR problemas existentes','Problemas3'); 

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','lodo_gest','localgiseiel.tabla.depuradora2.columna.lodo_gest',4,'eiel_EDAR Gestión de lodos','GestionLodos'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','lodo_vert','localgiseiel.tabla.depuradora2.columna.lodo_vert',1,null,'LodosVertedero'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','lodo_inci','localgiseiel.tabla.depuradora2.columna.lodo_inci',1,null,'LodosIncineracion'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','lodo_con_agri','localgiseiel.tabla.depuradora2.columna.lodo_con_agri',1,null,'LodosAgrConCompostaje');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','lodo_sin_agri','localgiseiel.tabla.depuradora2.columna.lodo_sin_agri',1,null,'lodosAgrConCompostaje');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Ident.D2','lodo_ot','localgiseiel.tabla.depuradora2.columna.lodo_ot',1,null,'LodosOtroFinal');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Info.D2','fecha_inst','localgiseiel.tabla.depuradora2.columna.fecha_inst',2,null,'FechaInstalacion'); 	

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Esta.D2','observ','localgiseiel.tabla.depuradora2.columna.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Esta.D2','fecha_revision','localgiseiel.tabla.depuradora2.columna.fecha_revision',2,null,'FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('D2','Esta.D2','estado_revision','localgiseiel.tabla.depuradora2.columna.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('D2','Esta.D2','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 	
	--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('D2','Info.D2','eiel_tr_saneam_ed_pobl.codentidad_pobl',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('D2','Info.D2','eiel_tr_saneam_ed_pobl.codpoblamiento_pobl',null,-2,null,null,false,false); 	

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.D2';
insert into lcg_nodos_capas_traducciones values ('eiel.D2','Depuradoras 2','es_ES');



