-- PRO --------\i ---------------------------------------

-- Start Transaction block
BEGIN;


\i 'install/pro/2. 2_1 a MODELO.sql'
\i 'install/pro/3. MODELO a PRE-EIEL.sql'
\i 'install/pro/modelo/publicador_guiaurbana.sql'
\i 'install/pro/modelo/entidad_supramunicipal.sql'
\i 'install/pro/modelo/informes/modelo_informes.sql'
\i 'install/pro/4.actualizaciones.sql'
--\i 'install/pro/EstadoObras.sql'
--\i 'install/pro/InvMediaAnual.sql'

COMMIT;
-- TEST ----------------------------------------------

--\i 'install/test/DO.sql'
--\i 'install/test/SET.sql'
--\i 'install/test/DO.sql'

------------------------------------------------------