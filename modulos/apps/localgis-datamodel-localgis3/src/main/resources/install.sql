-- PRO -----------------------------------------------
BEGIN;

-- El centralizadorsso.sql pasa al modulo localgis-datamodel-prelocalgis3
--\i 'install/pro/centralizadorsso.sql'
\i 'install/pro/administracion.sql'
\i 'install/pro/alfresco.sql'
\i 'install/pro/release1.sql'
\i 'install/pro/adaptaciones_eiel_1.sql'
\i 'install/pro/adaptaciones_eiel_2.sql'
\i 'install/pro/backup.sql'
\i 'install/pro/script_AGE_v5.sql'

-- PARCHES MODELO
\i 'install/pro/R38/release38c.sql'
\i 'install/pro/R39/release39a.sql'
\i 'install/pro/R40/release40a.sql'
\i 'install/pro/R40/release40b.sql'
\i 'install/pro/R41/release41a.sql'
\i 'install/pro/R41/release41b.sql'
\i 'install/pro/R41/release41c.sql'
\i 'install/pro/R42/release42a.sql'
\i 'install/pro/R42/release42b.sql'
\i 'install/pro/R43/release43a.sql'
\i 'install/pro/R43.CAST/operators_workaround.sql'
\i 'install/pro/R44/release44a.sql'
\i 'install/pro/R44/release44b.sql'
\i 'install/pro/R45/release45a.sql'

\i 'install/pro/RExtra/script_AGE_RULES_VIEW_v1.sql'
\i 'install/pro/RExtra/script_otras_correcciones.sql'
COMMIT;

------------------------------------------------------


-- TEST ----------------------------------------------

--\i 'install/test/DO.sql'
--\i 'install/test/SET.sql'
--\i 'install/test/DO.sql'

------------------------------------------------------