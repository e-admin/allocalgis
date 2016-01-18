
set client_encoding to 'utf8';



-- Campos por cada elemento del arbol.
-- VARCHAR_CODE=0;NUMERIC_CODE=1;DATE_CODE=2;DOUBLE_CODE=3;DOMINIO_CODE=4;BOOLEAN_CODE=5;COMPUESTO_CODE=6;DOMINIO_CODE_INTEGER=7;
-- La diferencia entre DOMINIO_CODE y DOMINIO_CODE_INTEGER es el tipo de elementos el primero puede almacenar string y 
-- el segundo almacena integer, seguramente haya mas posibilidades pero por ahora gestionamos estas dos.

-- INCENDIO PROTECCION (eiel_c_ip - eiel_t_ip)
delete from lcg_nodos_capas_campos where clave_capa='IP';

insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Ident.IP','clave','localgiseiel.panels.label.clave',0,null,'Clave'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('IP','Ident.IP','codprov','localgiseiel.panels.label.codprov',0,null,'CodINEProvincia', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('IP','Ident.IP','codmunic','localgiseiel.panels.label.codmunic',0,null,'CodINEMunicipio', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('IP','Ident.IP','codentidad','localgiseiel.panels.label.codineentidad',0,null,'CodINEEntidad', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo, aplicaInformes, aplicaMovilidad)
	values ('IP','Ident.IP','codpoblamiento','localgiseiel.panels.label.codinepoblamiento',0,null,'CodINEPoblamiento', FALSE, TRUE);
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Ident.IP','orden_ip','localgiseiel.panels.label.orden',0,null,'Orden');	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','nombre','localgiseiel.panels.label.nombre',0,null,'Nombre'); 		 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','tipo','localgiseiel.panels.label.tipo',4,'eiel_Tipo de Centro de Protección Civil','Tipo'); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','titular','localgiseiel.panels.label.titular',4,'eiel_Titularidad del Centro de Protección Civil','Titular');	
	insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','gestor','localgiseiel.panels.label.gestor',4,'eiel_Gestor Extincion de Incendios y Proteccion Civil','Gestor');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','s_cubierta','localgiseiel.panels.label.s_cubierta_id',1,null,'SuperficieCubierta'); 			
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','s_solar','localgiseiel.panels.label.s_solar_id',1,null,'SuperficieSolar'); 		
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','s_aire','localgiseiel.panels.label.s_aire_id',1,null,'SuperficieAireLibre');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','plan_profe','localgiseiel.panels.label.plan_prof',1,null,'PlantillaProfesionales');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','plan_volun','localgiseiel.panels.label.plan_volun',1,null,'PlantillaVoluntarios');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','estado','localgiseiel.panels.label.estado',4,'eiel_Estado de conservación','Estado');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','acceso_s_ruedas','localgiseiel.panels.label.accesoruedas',4,'eiel_Acceso con silla de ruedas','Acceso_s_ruedas');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','obra_ejec','localgiseiel.panels.label.obraejec',4,'eiel_Obra ejecutada','Obra_ejec');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','vehic_incendio','localgiseiel.tabla.pi.columna.vehic_incendio',1,null,'VechiculosIncendios');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','vehic_rescate','localgiseiel.tabla.pi.columna.vehic_rescate',1,null,'VechiculosRescate');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','ambulancia','localgiseiel.tabla.pi.columna.ambulancia',1,null,'Ambulancias');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','medios_aereos','localgiseiel.tabla.pi.columna.medios_aereos',1,null,'MediosAereos');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','otros_vehc','localgiseiel.tabla.pi.columna.otros_vehic',1,null,'OtrosVehiculos');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','quitanieves','localgiseiel.tabla.pi.columna.quitanieve',1,null,'Quitanieves');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','detec_ince','localgiseiel.tabla.pi.columna.detec_ince',1,null,'SistemasDeteccionIncencios');
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','otros','localgiseiel.tabla.pi.columna.otros',1,null,'Otros');	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Info.IP','fecha_inst','localgiseiel.panels.label.fecha_instal',2,'TIPO_FECHA','FechaInstalacion'); 	
	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Esta.IP','observ','localgiseiel.panels.label.observ',0,null,'Observaciones'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Esta.IP','fecha_revision','localgiseiel.panels.label.fecha_revision',2,'TIPO_FECHA','FechaRevision'); 	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo)
	values ('IP','Esta.IP','estado_revision','localgiseiel.panels.label.estado_revision',7,'eiel_Estado de revisión','EstadoRevision'); 

	
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaMovilidad)
	values ('IP','Esta.IP','revision_expirada','localgiseiel.panels.label.estado_publicacion',7,'eiel_Estado de publicacion','EstadoPublicacion',FALSE); 	

	--------------------------------------------------------------------------------------------	
-- Campos que almacen la informacion de poblamiento (entidad y nucleo) -1=Entidad,-2=Nucleo
--------------------------------------------------------------------------------------------
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('IP','Info.IP','eiel_t_ip.codentidad',null,-1,null,null,false,false); 
insert into lcg_nodos_capas_campos (clave_capa, clave_grupo, campo_bd, tag_traduccion,tipo_bd, dominio, metodo,aplicaInformes,aplicaMovilidad)
	values ('IP','Info.IP','eiel_t_ip.codpoblamiento',null,-2,null,null,false,false);	

-- Traducciones de los distintos elementos
delete from lcg_nodos_capas_traducciones where tag_traduccion='eiel.IP';
insert into lcg_nodos_capas_traducciones values ('eiel.IP','Incendio Protección','es_ES');



