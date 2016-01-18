
set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- TRAMOS DE CARRETERA (eiel_c_tramos_carreteras - eiel_t_carreteras)
delete from lcg_nodos_capas_campos where clave_capa='TC';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Ident.TC','id_municipio','localgiseiel.tabla.tc.columna.id_municipio',1,null,'Id_municipio'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Ident.TC','codprov','localgiseiel.tabla.tc.columna.codprov',0,null,'CodINEProvincia');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Ident.TC','cod_carrt','localgiseiel.tabla.tc.columna.codcarretera',0,null,'CodCarretera');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Ident.TC','pki','localgiseiel.tabla.tc.columna.pki',0,null,'PKI');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Ident.TC','pkf','localgiseiel.tabla.tc.columna.pkf',0,null,'PKF');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Ident.TC','titular_via','localgiseiel.tabla.tc.columna.titular_via',4,'eiel_Titular Via','titular_via');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','clase_via','localgiseiel.tabla.tc.columna.clase_via',0,null,'ClaseVia');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','denominacion','localgiseiel.tabla.tc.columna.denominacion',0,null,'Denominacion'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Esta.TC','fecha_revision','localgiseiel.panels.label.fechaAct',2,'TIPO_FECHA','FechaActualizacion'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Esta.TC','observ','localgiseiel.tabla.tc.columna.observ',0,null,'Observaciones'); 	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','firme','localgiseiel.tabla.tc.columna.firme',4,'eiel_Firme Tramos C','firme');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','senaliza','localgiseiel.tabla.tc.columna.senaliza',4,'eiel_Señaliza','senaliza');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','dimensiona','localgiseiel.tabla.tc.columna.dimensiona',4,'eiel_dimensiona','dimensiona');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','gestor','localgiseiel.tabla.tc.columna.gestor',4,'eiel_Gestor Tramos C','gestor');		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','estrechamiento','localgiseiel.tabla.tc.columna.estrechamiento',4,'eiel_fre_estrech','estrechamiento');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','muy_sinuoso','localgiseiel.tabla.tc.columna.muy_sinuoso',4,'eiel_muy_sinuoso','muy_sinuoso');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('TC','Info.TC','pte_excesiva','localgiseiel.tabla.tc.columna.pte_excesiva',4,'eiel_pte_excesiva','pte_excesiva');

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('TC','Esta.TC','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 
	
--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('TC','Info.TC','eiel_c_redviaria_tu.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('TC','Info.TC','eiel_c_redviaria_tu.codpoblamiento',null,-2,null,null,false,false);		

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.TC';
insert into lcg_nodos_capas_traducciones values ('eiel.TC','Tramos de Carretera','es_ES');



