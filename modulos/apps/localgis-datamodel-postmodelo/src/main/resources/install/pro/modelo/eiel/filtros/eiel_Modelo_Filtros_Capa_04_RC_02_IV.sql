
set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- INFRAESTRUCTURA VIARIA (eiel_c_tramos_carreteras - eiel_t_carreteras)
delete from lcg_nodos_capas_campos where clave_capa='IV';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IV','Ident.IV','id_municipio','localgiseiel.tabla.tc.columna.id_municipio',1,null,'Id_municipio'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IV','Ident.IV','titular_via','localgiseiel.tabla.tc.columna.titular_via',4,'eiel_Titular Via','Titularidad');	

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IV','Info.IV','tipo','localgiseiel.tabla.tc.columna.tipo',4,'eiel_Tipo Red Viaria','Tipo');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IV','Info.IV','denominacion','localgiseiel.tabla.tc.columna.denominacion',0,null,'Denominacion'); 

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IV','Info.IV','firme','localgiseiel.tabla.tc.columna.firme',4,'eiel_Firme Tramos C','firme');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IV','Esta.IV','observ','localgiseiel.tabla.tc.columna.observ',0,null,'Observaciones'); 	
	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('IV','Esta.IV','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('IV','Info.IV','eiel_c_redviaria_tu.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('IV','Info.IV','eiel_c_redviaria_tu.codpoblamiento',null,-2,null,null,false,false);		

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.IV';
insert into lcg_nodos_capas_traducciones values ('eiel.IV','Infraestructura Viaria','es_ES');



