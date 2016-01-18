-- PRO -----------------------------------------------
SET statement_timeout = 0;
SET client_encoding = 'UTF-8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = notice;
SET default_with_oids = true;


BEGIN;

-- Creamos en primer lugar las tablas que no se han creado al importar las capas
\i 'install/pro/modelo/ModeloDatosExtendido.sql'

\i 'install/pro/4. PRE-EIEL a LocalGIS III.sql'
\i 'install/pro/modelo/eiel/indicadores/datamodel/EIEL_Indicadores_DBModel.sql'

\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Datos_Filtros_Schema.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_ADatos_Basicos.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_APermisos.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_01_AA_01_CA.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_01_AA_02_DE.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_01_AA_03_TP.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_01_AA_04_P2.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_01_AA_05_DSA.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_01_AA_06_AU.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_01_AA_07_RD.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_01_AA_08_CN.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_01_AA_09_AR.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_01_02_ED.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_01_D1.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_01_D2.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_03_DSS.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_04_PV.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_05_SN.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_06_EM.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_07_CL.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_08_RM.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_02_SN_09_PZ.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_01_CC.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_02_CU.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_03_AS.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_04_EN.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_05_CSAN.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_06_CE.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_07_SU.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_08_IP.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_09_ID.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_10_LM.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_11_MT.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_12_PJ.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_03_EQ_13_TA.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_04_RC_01_TC.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_04_RC_02_IV.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_06_VT_01_VT.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_06_VT_02_RB.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_06_VT_03_SR.sql'
\i 'install/pro/modelo/eiel/filtros/eiel_Modelo_Filtros_Capa_07_ALUM_01_ALUM.sql'
\i 'install/pro/modelo/eiel/filtros/getDomainValues.sql'

\i 'install/pro/modelo/release25.sql'
\i 'install/pro/modelo/release25.sql'
\i 'install/pro/modelo/release26.sql'
\i 'install/pro/modelo/release29.sql'
\i 'install/pro/modelo/release30.sql'
\i 'install/pro/modelo/release31.sql'
\i 'install/pro/modelo/release32.sql'
\i 'install/pro/modelo/release32a.sql'
\i 'install/pro/modelo/release32b.sql'

-- El release32c.sql habria que verificar que las tablas de indicadores se generan bien
\i 'install/pro/modelo/release32c.sql'
\i 'install/pro/modelo/release32d.sql'
\i 'install/pro/modelo/release34.sql'
\i 'install/pro/modelo/release34a.sql'
\i 'install/pro/modelo/release35.sql'
\i 'install/pro/modelo/release35a.sql'
\i 'install/pro/modelo/release36.sql'
\i 'install/pro/modelo/release36a.sql'
\i 'install/pro/modelo/release36b.sql'
\i 'install/pro/modelo/Cambio_Nombre_Capas_Eiel.sql'
\i 'install/pro/modelo/acls_eiel.sql'



\i 'install/pro/modelo/Dominios.sql'
\i 'install/pro/modelo/ForenkeysMPT.sql'

\i 'install/pro/modelo/Insert_Agrupaciones_6000.sql'
\i 'install/pro/modelo/InsertH_29_30.sql'
\i 'install/pro/modelo/integracionEIEL_Inventario.sql'
\i 'install/pro/modelo/validacionesMPT.sql'
\i 'install/pro/modelo/reports.sql'


\i 'install/pro/modelo/informes/modelo_informes.sql'

\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_All.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_I_Poblacion_Densidad.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_II_Captaciones.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_II_Depositos.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_II_Potabilizacion.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_II_RDistribucion.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_II_RSaneam.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_II_RSaneam_saneamau.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_III_Alumbrado.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_III_Comunicaciones.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_III_Suministros.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_IV_RBLimpieza.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_V_CEnsenanzaDeporteCultura.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_VI_SanitarioAsistencial.sql'
\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_VII_OtrosServicios.sql'


\i 'install/pro/modelo/eiel/maplayers/EIEL_Data_Layers_Incidencias.sql'


\i 'install/pro/modelo/publicador/1.script_permisos_especificos.sql'
\i 'install/pro/modelo/publicador/2.script_creacion_usuario_publicador.sql'
\i 'install/pro/modelo/publicador/2.script_creacion_usuario_validador.sql'
\i 'install/pro/modelo/publicador/3.script_permisos_role_eiel_indicador.sql'
\i 'install/pro/modelo/publicador/3.script_permisos_role_eiel_publicador.sql'
\i 'install/pro/modelo/publicador/3.script_permisos_role_eiel_validador.sql'
\i 'install/pro/modelo/publicador/4.script_permisos_asociacion_a_usuarios.sql'

\i 'install/pro/modelo/eiel/superlayers/SuperLayer AA_TCN_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer AA_TP_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer ASL_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer CA_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer carreteras_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer CC_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer CE_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer CU_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer EN_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer ID_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer IP_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer LM_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer MT_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer PJ_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer PV_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer SA_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer SAN_DE_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer SAN_PV_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer SAN_TCL_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer SAN_TEM_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer SU_TC.sql'
\i 'install/pro/modelo/eiel/superlayers/SuperLayer TN_TC.sql'
-- Esta no la utilizamos
--\i 'install/pro/modelo/eiel/superlayers/SuperLayer_GeneradorSQL.sql'




COMMIT;

-- TEST ----------------------------------------------

--\i 'install/test/DO.sql'
--\i 'install/test/SET.sql'
--\i 'install/test/DO.sql'

------------------------------------------------------