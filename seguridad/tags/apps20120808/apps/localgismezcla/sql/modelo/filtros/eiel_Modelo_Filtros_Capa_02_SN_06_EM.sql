set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- EMISARIO (eiel_c_saneam_tem - eiel_t_saneam_tem)
delete from lcg_nodos_capas_campos where clave_capa='EM';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Ident.EM','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('EM','Ident.EM','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('EM','Ident.EM','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Ident.EM','tramo_em','localgiseiel.panels.label.tramo',0,null,'CodOrden'); 
--DESCOMENTAR CUANDO SE USE COMO CLAVE PRIMARIA
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
--	values ('EM','Ident.EM','pmi','localgiseiel.panels.label.pmi',3,null,'PMI');	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('EM','Ident.EM','pmf','localgiseiel.panels.label.pmf',3,null,'PMF'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','titular','localgiseiel.panels.label.titular',4,'eiel_Titularidad','Titularidad'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','gestor','localgiseiel.panels.label.gestor',4,'eiel_Titularidad','Gestion'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','estado','localgiseiel.panels.label.estado_conserv',4,'eiel_Estado de conservación','Estado'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','material','localgiseiel.panels.label.material',4,'eiel_material','Material'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','sist_impulsion','localgiseiel.tabla.emisarios.columna.sist_impulsion',0,'eiel_sist_impulsion','Sistema'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','tipo_red_interior','localgiseiel.tabla.emisarios.columna.tipo_red_interior',4,'eiel_tipo_red_interior','Tipo_red'); 	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('EM','Info.EM','longitud','localgiseiel.panels.label.longitud',3,null,null);	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('EM','Info.EM','long_terre','localgiseiel.panels.label.long_terre',3,null,null);	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('EM','Info.EM','long_marit','localgiseiel.panels.label.long_marit',3,null,null);	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('EM','Info.EM','diametro ','localgiseiel.panels.label.diametro ',1,null,null);	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('EM','Info.EM','cota_z ','localgiseiel.panels.label.cota_z',1,null,null);	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('EM','Info.EM','obra_ejec ','localgiseiel.panels.label.obraejec',0,null,null);		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','fecha_inst','localgiseiel.tabla.emisarios.columna.fecha_inst',2,'TIPO_FECHA','Fecha_inst'); 	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Esta.EM','estado_revision','localgiseiel.panels.label.estado',4,'eiel_Estado de revisión','Estado_Revision'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Esta.EM','observ','localgiseiel.panels.label.observ',0,null,'Observaciones'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Esta.EM','fecha_revision','localgiseiel.tabla.emisarios.columna.fecha_revision',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','tipo','localgiseiel.panels.label.tipo',4,'eiel_Tipo Vertido','tipo'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','zona','localgiseiel.panels.label.zona',4,'eiel_Zona del punto de vertido','zona'); 
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Ident.EM','pozos_registro','localgiseiel.tabla.servsaneam.columna.pozos_registo',4,'eiel_pozos_registro','Pozos');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Ident.EM','sumideros','localgiseiel.tabla.servsaneam.columna.sumideros',4,'eiel_sumideros','Sumideros'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','aliv_c_acum','localgiseiel.tabla.servsaneam.columna.aliv_c_acum',4,'eiel_aliv_c_acum','AlivAcumulacion'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('EM','Info.EM','aliv_s_acum','localgiseiel.tabla.servsaneam.columna.aliv_s_acum',4,'eiel_aliv_s_acum','AlivSinAcumulacion'); 		
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('EM','Esta.EM','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 	
	
	

--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('EM','Info.EM','eiel_tr_saneam_tem_pobl.codentidad_pobl',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('EM','Info.EM','eiel_tr_saneam_tem_pobl.codpoblamiento_pobl',null,-2,null,null,false,false); 	
	
--insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
--	values ('EM','Info.EM','precision_dig','localgiseiel.panels.label.precision_dig',0,null,null);	

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.EM';
insert into lcg_nodos_capas_traducciones values ('eiel.EM','Tramos Emisarios','es_ES');



