delete from lcg_nodos_capas where nodo ilike '%eiel%';
------------------------------	
-- ABASTECIMIENTO AUTONOMO (9)
------------------------------	
--Captaciones (Informes=Captaciones)
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('CA','AA','CA','eiel.CA','eiel_t_abast_ca','filtro_ca','com.geopista.app.eiel.beans.CaptacionesEIEL',true);
--Depositos (Informes=Depositos)
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('DE','AA','DE','eiel.DE','eiel_t_abast_de','filtro_de','com.geopista.app.eiel.beans.DepositosEIEL',true);
	
--Puntos de Potabilizacion (Informes=Tratamientos de Potabilizacion)
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('TP','AA','TP','eiel.TP','eiel_t_abast_tp','filtro_tp','com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL',true);

-- Informacion Relativa al Abastecimiento 
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('P2','AA','P2','eiel.P2','eiel_t_nucl_encuest_2','filtro_p2','com.geopista.app.eiel.beans.Encuestados2EIEL');

-- Datos del Servicio de Abastecimiento (Informes=Caracteristicas  del Servicio)
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('DSA','AA','DSA','eiel.DSA','eiel_t_abast_serv','filtro_dsa','com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL');

-- Abastecimiento Autonomo (Informes=Deficit)
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('AU','AA','AU','eiel.AU','eiel_t_abast_au','filtro_au','com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL',true);

-- Red de Distribucion Domiciliaria (Informes=Red de Distribucion)
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('RD','AA','RD','eiel.RD','eiel_c_abast_rd','filtro_rd','com.geopista.app.eiel.beans.RedDistribucionEIEL',true);

-- Tramos de conducciÃ³n (Informes=Conduccion)
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('CN','AA','CN','eiel.CN','eiel_t_abast_tcn','filtro_cn','com.geopista.app.eiel.beans.TramosConduccionEIEL',true);	
	
-- Elemento puntual de abastecimiento (Informes=Elementos accesorios)
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('AR','AA','AR','eiel.AR','eiel_c_abast_ar','filtro_ar','com.geopista.app.eiel.beans.ElementoPuntualAbastecimientoEIEL',true);	
	
--------------------------	
-- SANEAMIENTO (9)
--------------------------	
--(Las depuradoras son especiales porque al haber dos es necesario gestionarlas en los filtros como una sola)
--Depuradoras 1 y Depuradora2 
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('ED','SA','D1-D2','eiel.ED',null,'filtro_ed',null,true);
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('D1','SA','D1','eiel.D1','eiel_t1_saneam_ed','filtro_ed','com.geopista.app.eiel.beans.Depuradora1EIEL',true);
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('D2','SA','D2','eiel.D2','eiel_t2_saneam_ed','filtro_ed','com.geopista.app.eiel.beans.Depuradora2EIEL',true);
--Depuradoras2
--insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,activo)
--	values ('ED-1','SA','D2','eiel.ED',c,false);
	
--Servicios Saneamiento
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('DSS','SA','DSS','eiel.DSS','eiel_t_saneam_serv','filtro_dss','com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL');
	
--Puntos de Vertido
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('PV','SA','PV','eiel.PV','eiel_t_saneam_pv','filtro_pv','com.geopista.app.eiel.beans.PuntosVertidoEIEL',true);
--Saneamiento Autonomo
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,conectividad)
	values ('SN','SA','SN','eiel.SN','eiel_t_saneam_au','filtro_sn','com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL',true);
--Emisarios
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer,conectividad)
	values ('EM','SA','EM','eiel.EM','eiel_c_saneam_tem','filtro_em','com.geopista.app.eiel.beans.EmisariosEIEL','TEM',true);
--Colectores
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer,conectividad)
	values ('CL','SA','CL','eiel.CL','eiel_t_saneam_tcl','filtro_cl','com.geopista.app.eiel.beans.ColectorEIEL','TCL',true);
--Red de Ramales
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer,conectividad)
	values ('RM','SA','RM','eiel.RM','eiel_c_saneam_rs','filtro_rm','com.geopista.app.eiel.beans.RedDeRamalesEIEL','RS',true);
--Elementos Puntuales
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer,conectividad)
	values ('PZ','SA','PZ','eiel.PZ','eiel_c_saneam_pr','filtro_pz','com.geopista.app.eiel.beans.ElementoPuntualSaneamientoEIEL','PR',true);
	

--------------------------	
-- EQUIPAMIENTO (9)
--------------------------	
--Casas Consitoriales
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('CC','EQ','CC','eiel.CC','eiel_t_cc','filtro_cc','com.geopista.app.eiel.beans.CasasConsistorialesEIEL');	
--Centros Culturales
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('CU','EQ','CU','eiel.CU','eiel_t_cu','filtro_cu','com.geopista.app.eiel.beans.CentrosCulturalesEIEL');	
--Centros Asistenciales
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer)
	values ('AS','EQ','AS','eiel.AS','eiel_t_as','filtro_as','com.geopista.app.eiel.beans.CentrosAsistencialesEIEL','ASL');	
--insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
--	values ('ASL','EQ','ASL','eiel.AS','eiel_t_as','filtro_as','com.geopista.app.eiel.beans.CentrosAsistencialesEIEL');	
--Centros EnseÃ±anza	
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('EN','EQ','EN','eiel.EN','eiel_t_en','filtro_en','com.geopista.app.eiel.beans.CentrosEnsenianzaEIEL');	
--Cementerios
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('CE','EQ','CE','eiel.CE','eiel_t_ce','filtro_ce','com.geopista.app.eiel.beans.CementeriosEIEL');	
--Centros Sanitarios
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer)
	values ('CSAN','EQ','CSAN','eiel.CSAN','eiel_t_sa','filtro_sa','com.geopista.app.eiel.beans.CentrosSanitariosEIEL','SA');
--Edificios Sin Uso
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('SU','EQ','SU','eiel.SU','eiel_t_su','filtro_su','com.geopista.app.eiel.beans.EdificiosSinUsoEIEL');	
--Incendio Proteccion
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('IP','EQ','IP','eiel.IP','eiel_t_ip','filtro_ip','com.geopista.app.eiel.beans.IncendioProteccionEIEL');		
--Instalaciones Deportivas
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('ID','EQ','ID','eiel.ID','eiel_t_id','filtro_id','com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL');	
--Lonjas y Mercados
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('LM','EQ','LM','eiel.LM','eiel_t_lm','filtro_lm','com.geopista.app.eiel.beans.LonjasMercadosEIEL');	
--Mataderos
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('MT','EQ','MT','eiel.MT','eiel_t_mt','filtro_mt','com.geopista.app.eiel.beans.MataderosEIEL');		
--Parques y Jardines
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('PJ','EQ','PJ','eiel.PJ','eiel_t_pj','filtro_pj','com.geopista.app.eiel.beans.ParquesJardinesEIEL');	
--Tanatorios
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('TA','EQ','TA','eiel.TA','eiel_t_ta','filtro_ta','com.geopista.app.eiel.beans.TanatoriosEIEL');	


	
--------------------------	
-- INFRAESTRUCTURA VIARIA (9)
--------------------------	
--Tramos Carretera
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer)
	values ('TC','RC','TC','eiel.TC','eiel_t_carreteras','filtro_tc','com.geopista.app.eiel.beans.TramosCarreterasEIEL','carretera');		
--Infraestructura viaria
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer)
	values ('IV','RC','IV','eiel.IV','eiel_c_redviaria_tu','filtro_iv','com.geopista.app.eiel.beans.InfraestructuraViariaEIEL','TU');		
	
	
--------------------------	
-- LUZ (9)
--------------------------	

--Infraestructura Luminaria
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean,layer)
	values ('ALUM','ALUM','ALUM','eiel.ALUM','eiel_c_alum_pl','filtro_alum','com.geopista.app.eiel.beans.InfraestructuraLuminariaEIEL','PL');	

	
--------------------------	
-- VERTEDERO (9)
--------------------------	
--Vertedero
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('VT','RVT','VT','eiel.VT','eiel_t_vt','filtro_vt','com.geopista.app.eiel.beans.VertederosEIEL');	
-- Recogida Basuras
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('RB','RVT','RB','eiel.RB','eiel_t_rb','filtro_rb','com.geopista.app.eiel.beans.RecogidaBasurasEIEL');	
--Servicios Recogida Basura
insert into lcg_nodos_capas  (clave, categoria,nodo, tag_traduccion, tabla,nombre_filtro,bean)
	values ('SR','RVT','SR','eiel.SR','eiel_t_rb_serv','filtro_sr','com.geopista.app.eiel.beans.ServiciosRecogidaBasurasEIEL');	
	
	
delete from lcg_nodos_app where app ilike '%eiel%';
-- Capas por Aplicacion
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','AU');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','CA');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','DE');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','TP');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','P2');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','DSA');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','RD');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','CN');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','AR');

insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','ED');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','D1');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','D2');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','DSS');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','PV');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','SN');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','EM');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','CL');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','RM');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','PZ');

insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','CC');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','CU');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','AS');
--insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','ASL');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','EN');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','CE');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','CSAN');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','SU');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','IP');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','ID');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','LM');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','MT');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','PJ');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','TA');

insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','TC');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','IV');

insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','VT');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','RB');
insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','SR');

insert into lcg_nodos_app  ( app, clave_capa) values ('EIEL','ALUM');
  
-- Posibilidades de agrupar campos en grupos
delete from lcg_nodos_capas_grupos where tag_traduccion ilike '%eiel%';
-- id,orden,traduccion,Bean
insert into lcg_nodos_capas_grupos values ('Ident.AU',1,'eiel.Identificacion','AbstecimientoAutonomoEIEL');
insert into lcg_nodos_capas_grupos values ('Info.AU',2,'eiel.Informacion','AbstecimientoAutonomoEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.AU',3,'eiel.Estado','AbstecimientoAutonomoEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.CA',1,'eiel.Identificacion','CaptacionEIEL');
insert into lcg_nodos_capas_grupos values ('Info.CA',2,'eiel.Informacion','CaptacionEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.CA',3,'eiel.Estado','CaptacionEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.DE',1,'eiel.Identificacion','DepositosEIEL');
insert into lcg_nodos_capas_grupos values ('Info.DE',2,'eiel.Informacion','DepositosEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.DE',3,'eiel.Estado','DepositosEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.TP',1,'eiel.Identificacion','TratamientosPotabilizacionEIEL');
insert into lcg_nodos_capas_grupos values ('Info.TP',2,'eiel.Informacion','TratamientosPotabilizacionEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.TP',3,'eiel.Estado','TratamientosPotabilizacionEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.P2',1,'eiel.Identificacion','Encuestados2EIEL');
insert into lcg_nodos_capas_grupos values ('Info.P2',2,'eiel.Informacion','Encuestados2EIEL');
insert into lcg_nodos_capas_grupos values ('Esta.P2',3,'eiel.Estado','Encuestados2EIEL');
insert into lcg_nodos_capas_grupos values ('Ident.DSA',1,'eiel.Identificacion','ServiciosAbastecimientosEIEL');
insert into lcg_nodos_capas_grupos values ('Info.DSA',2,'eiel.Informacion','ServiciosAbastecimientosEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.DSA',3,'eiel.Estado','ServiciosAbastecimientosEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.RD',1,'eiel.Identificacion',null);
insert into lcg_nodos_capas_grupos values ('Info.RD',2,'eiel.Informacion',null);
insert into lcg_nodos_capas_grupos values ('Esta.RD',3,'eiel.Estado',null);
insert into lcg_nodos_capas_grupos values ('Ident.CN',1,'eiel.Identificacion','TramosConduccionEIEL');
insert into lcg_nodos_capas_grupos values ('Info.CN',2,'eiel.Informacion','TramosConduccionEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.CN',3,'eiel.Estado','TramosConduccionEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.AR',1,'eiel.Identificacion',null);
insert into lcg_nodos_capas_grupos values ('Info.AR',2,'eiel.Informacion',null);
insert into lcg_nodos_capas_grupos values ('Esta.AR',3,'eiel.Estado',null);

-- Problema al filtrar la informacion de Depuradora1 y Depuradora 2
insert into lcg_nodos_capas_grupos values ('Ident.ED',1,'eiel.Identificacion','DepuradoraEIEL');
insert into lcg_nodos_capas_grupos values ('Info.ED',2,'eiel.Informacion','DepuradoraEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.ED',3,'eiel.Estado','DepuradoraEIEL');

insert into lcg_nodos_capas_grupos values ('Ident.D1',1,'eiel.Identificacion','Depuradora1EIEL');
insert into lcg_nodos_capas_grupos values ('Info.D1',2,'eiel.Informacion','Depuradora1EIEL');
insert into lcg_nodos_capas_grupos values ('Esta.D1',3,'eiel.Estado','Depuradora1EIEL');
insert into lcg_nodos_capas_grupos values ('Ident.D2',1,'eiel.Identificacion','Depuradora2EIEL');
insert into lcg_nodos_capas_grupos values ('Info.D2',2,'eiel.Informacion','Depuradora2EIEL');
insert into lcg_nodos_capas_grupos values ('Esta.D2',3,'eiel.Estado','Depuradora2EIEL');

--insert into lcg_nodos_capas_grupos values ('Ident.D1',1,'eiel.Identificacion','Depuradora1EIEL');
--insert into lcg_nodos_capas_grupos values ('Info.D1',2,'eiel.Informacion','Depuradora1EIEL');
--insert into lcg_nodos_capas_grupos values ('Ident.D2',1,'eiel.Identificacion','Depuradora2EIEL');
--insert into lcg_nodos_capas_grupos values ('Info.D2',2,'eiel.Informacion','Depuradora2EIEL');


insert into lcg_nodos_capas_grupos values ('Ident.DSS',1,'eiel.Identificacion','ServiciosSaneamientoEIEL');
insert into lcg_nodos_capas_grupos values ('Info.DSS',2,'eiel.Informacion','ServiciosSaneamientoEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.DSS',3,'eiel.Estado','ServiciosSaneamientoEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.PV',1,'eiel.Identificacion','PuntosVertidoEIEL');
insert into lcg_nodos_capas_grupos values ('Info.PV',2,'eiel.Informacion','PuntosVertidoEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.PV',3,'eiel.Estado','PuntosVertidoEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.SN',1,'eiel.Identificacion','SaneamientoAutonomoEIEL');
insert into lcg_nodos_capas_grupos values ('Info.SN',2,'eiel.Informacion','SaneamientoAutonomoEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.SN',3,'eiel.Estado','SaneamientoAutonomoEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.EM',1,'eiel.Identificacion','EmisariosEIEL');
insert into lcg_nodos_capas_grupos values ('Info.EM',2,'eiel.Informacion','EmisariosEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.EM',3,'eiel.Estado','EmisariosEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.CL',1,'eiel.Identificacion','ColectoresEIEL');
insert into lcg_nodos_capas_grupos values ('Info.CL',2,'eiel.Informacion','ColectoresEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.CL',3,'eiel.Estado','ColectoresEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.RM',1,'eiel.Identificacion',null);
insert into lcg_nodos_capas_grupos values ('Info.RM',2,'eiel.Informacion',null);
insert into lcg_nodos_capas_grupos values ('Esta.RM',3,'eiel.Estado',null);
insert into lcg_nodos_capas_grupos values ('Ident.PZ',1,'eiel.Identificacion',null);
insert into lcg_nodos_capas_grupos values ('Info.PZ',2,'eiel.Informacion',null);
insert into lcg_nodos_capas_grupos values ('Esta.PZ',3,'eiel.Estado',null);

insert into lcg_nodos_capas_grupos values ('Ident.CC',1,'eiel.Identificacion','CasasConsistorialesEIEL');
insert into lcg_nodos_capas_grupos values ('Info.CC',2,'eiel.Informacion','CasasConsistorialesEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.CC',3,'eiel.Estado','CasasConsistorialesEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.CU',1,'eiel.Identificacion','CentrosCulturalesEIEL');
insert into lcg_nodos_capas_grupos values ('Info.CU',2,'eiel.Informacion','CentrosCulturalesEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.CU',3,'eiel.Estado','CentrosCulturalesEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.AS',1,'eiel.Identificacion','CentrosAsistencialesEIEL');
insert into lcg_nodos_capas_grupos values ('Info.AS',2,'eiel.Informacion','CentrosAsistencialesEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.AS',3,'eiel.Estado','CentrosAsistencialesEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.EN',1,'eiel.Identificacion','CentrosEnsenianzaEIEL');
insert into lcg_nodos_capas_grupos values ('Info.EN',2,'eiel.Informacion','CentrosEnsenianzaEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.EN',3,'eiel.Estado','CentrosEnsenianzaEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.CE',1,'eiel.Identificacion','CementeriosEIEL');
insert into lcg_nodos_capas_grupos values ('Info.CE',2,'eiel.Informacion','CementeriosEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.CE',3,'eiel.Estado','CementeriosEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.CSAN',1,'eiel.Identificacion','CentrosSanitariosEIEL');
insert into lcg_nodos_capas_grupos values ('Info.CSAN',2,'eiel.Informacion','CentrosSanitariosEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.CSAN',3,'eiel.Estado','CentrosSanitariosEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.SU',1,'eiel.Identificacion','EdificiosSinUsoEIEL');
insert into lcg_nodos_capas_grupos values ('Info.SU',2,'eiel.Informacion','EdificiosSinUsoEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.SU',3,'eiel.Estado','EdificiosSinUsoEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.IP',1,'eiel.Identificacion','IncendioProteccionEIEL');
insert into lcg_nodos_capas_grupos values ('Info.IP',2,'eiel.Informacion','IncendioProteccionEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.IP',3,'eiel.Estado','IncendioProteccionEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.ID',1,'eiel.Identificacion','InstalacionesDeportivasEIEL');
insert into lcg_nodos_capas_grupos values ('Info.ID',2,'eiel.Informacion','InstalacionesDeportivasEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.ID',3,'eiel.Estado','InstalacionesDeportivasEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.LM',1,'eiel.Identificacion','LonjasMercadosEIEL');
insert into lcg_nodos_capas_grupos values ('Info.LM',2,'eiel.Informacion','LonjasMercadosEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.LM',3,'eiel.Estado','LonjasMercadosEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.MT',1,'eiel.Identificacion','MataderosEIEL');
insert into lcg_nodos_capas_grupos values ('Info.MT',2,'eiel.Informacion','MataderosEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.MT',3,'eiel.Estado','MataderosEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.PJ',1,'eiel.Identificacion','ParquesJardinesEIEL');
insert into lcg_nodos_capas_grupos values ('Info.PJ',2,'eiel.Informacion','ParquesJardinesEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.PJ',3,'eiel.Estado','ParquesJardinesEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.TA',1,'eiel.Identificacion','TanatoriosEIEL');
insert into lcg_nodos_capas_grupos values ('Info.TA',2,'eiel.Informacion','TanatoriosEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.TA',3,'eiel.Estado','TanatoriosEIEL');

insert into lcg_nodos_capas_grupos values ('Ident.TC',1,'eiel.Identificacion','TramosCarreterasEIEL');
insert into lcg_nodos_capas_grupos values ('Info.TC',2,'eiel.Informacion','TramosCarreterasEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.TC',3,'eiel.Estado','TramosCarreterasEIEL');

insert into lcg_nodos_capas_grupos values ('Ident.IV',1,'eiel.Identificacion',null);
insert into lcg_nodos_capas_grupos values ('Info.IV',2,'eiel.Informacion',null);
insert into lcg_nodos_capas_grupos values ('Esta.IV',3,'eiel.Estado',null);


insert into lcg_nodos_capas_grupos values ('Ident.VT',1,'eiel.Identificacion','VertederosEIEL');
insert into lcg_nodos_capas_grupos values ('Info.VT',2,'eiel.Informacion','VertederosEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.VT',3,'eiel.Estado','VertederosEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.RB',1,'eiel.Identificacion','RecogidaBasurasEIEL');
insert into lcg_nodos_capas_grupos values ('Info.RB',2,'eiel.Informacion','RecogidaBasurasEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.RB',3,'eiel.Estado','RecogidaBasurasEIEL');
insert into lcg_nodos_capas_grupos values ('Ident.SR',1,'eiel.Identificacion','ServiciosRecogidaBasurasEIEL');
insert into lcg_nodos_capas_grupos values ('Info.SR',2,'eiel.Informacion','ServiciosRecogidaBasurasEIEL');
insert into lcg_nodos_capas_grupos values ('Esta.SR',3,'eiel.Estado','ServiciosRecogidaBasurasEIEL');

insert into lcg_nodos_capas_grupos values ('Ident.ALUM',1,'eiel.Identificacion',null);
insert into lcg_nodos_capas_grupos values ('Info.ALUM',2,'eiel.Informacion',null);
insert into lcg_nodos_capas_grupos values ('Esta.ALUM',3,'eiel.Estado',null);

-- Traducciones de los distintos elementos genericas

insert into lcg_nodos_capas_traducciones values ('eiel.Identificacion','Identificación','es_ES');
insert into lcg_nodos_capas_traducciones values ('eiel.Informacion','Información','es_ES');
insert into lcg_nodos_capas_traducciones values ('eiel.Estado','Estado','es_ES');

