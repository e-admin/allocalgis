@ECHO OFF


setlocal ENABLEDELAYEDEXPANSION 

set VARIABLES=-DappInstallerPath=%appInstallerPath% -DappModulesPath=%appModulesPath% -DappWebAppsPath=%appWebAppsPath% -DappPluginsPath=%appPluginsPath%

set PROFILE=-P localgis3_default


SET TODAYSDATE=%date:~6,8%-%date:~3,2%-%date:~0,2%

set PATH_LIB_LOCALGIS=%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.localgis\libreriasLocalGIS.bat
if exist "%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.localgis\libreriasLocalGIS.bat" (
	del %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.localgis\libreriasLocalGIS.bat
)
if not exist "%USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.localgis\" (
	mkdir %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.localgis\
)
copy settings\settings_lcg3_batch_deploy.xml %USERPROFILE%\Documents\0.Localgis.Patches\%TODAYSDATE%\minetur.sinfirma\jar.librerias.localgis
echo @ECHO OFF >%PATH_LIB_LOCALGIS%
echo set URL=%MAVEN_SIGN_REPO% >>%PATH_LIB_LOCALGIS%

For /f "tokens=1-4 delims=/ " %%a in ('date /t') do (set mydate_inicio=%%c-%%a-%%b)
For /f "tokens=1-2 delims=/:" %%a in ("%TIME%") do (set mytime_inicio=%%a:%%b)

IF "%1"=="" GOTO NOSIGN
IF "%1"=="clean" GOTO CLEAN
IF "%1"=="sign" GOTO SIGN
IF "%1"=="signext" GOTO SIGNEXT
IF "%1"=="nosignext" GOTO NOSIGNEXT
IF "%1"=="nosign" GOTO NOSIGN
IF "%1"=="check" GOTO CHECK
REM Esta opcion es solo para artefactos individuales
REM IF "%1"=="sign_nosign" GOTO SIGN_NOSIGN
IF "%1"=="help" GOTO HELP

REM En caso contrario artefacto simple
GOTO SIMPLE

:HELP
echo Usage lcg_compile/lcg_deploy/lcg_clean 
echo Usage lcg_compile/lcg_deploy sign
echo Usage lcg_compile/lcg_deploy sign user
echo Usage lcg_compile/lcg_deploy nosign user
echo Usage lcg_compile/lcg_deploy sign install
echo Usage lcg_compile/lcg_deploy nosign install
echo Usage lcg_compile/lcg_deploy sign lcg_const
echo Usage lcg_compile/lcg_deploy nosign lcg_const

GOTO END

:SIGNDEFAULT
set OPTIONS=-Dmaven.clean.failOnError=false --strict-checksums
set OPTIONS_SIGN=-Djarsigner.skip=false
set SETTINGS=settings\settings_lcg3.xml
set REPOSITORY_MAVEN=%MAVEN_SIGN_REPO%
set DATOS_DEPLOY=-DaltDeploymentRepository=LocalGISIII::default::%REPOSITORY_MAVEN%
set OPTIONS_SIGN=%OPTIONS_SIGN% -Durl=%REPOSITORY_MAVEN%
GOTO CONTINUE


:SIGN
set OPTIONS=-Dmaven.clean.failOnError=false --strict-checksums
set OPTIONS_SIGN=-Djarsigner.skip=false
set SETTINGS=settings\settings_lcg3.xml
set REPOSITORY_MAVEN=%MAVEN_SIGN_REPO%
set DATOS_DEPLOY=-DaltDeploymentRepository=LocalGISIII::default::%REPOSITORY_MAVEN%
set OPTIONS_SIGN=%OPTIONS_SIGN% -Durl=%REPOSITORY_MAVEN%
GOTO CONTINUE






:SIGNEXT
set OPTIONS=-Dmaven.clean.failOnError=false --strict-checksums
call perfiles/env_ext_%USERNAME%.bat
set VARIABLES=-DappInstallerPath=%appInstallerPath% -DappModulesPath=%appModulesPath% -DappWebAppsPath=%appWebAppsPath% -DappPluginsPath=%appPluginsPath%
set OPTIONS_SIGN=-Djarsigner.skip=false
set SETTINGS=settings\settings_lcg3_ext.xml
set REPOSITORY_MAVEN=%MAVEN_SIGN_REPO%
set DATOS_DEPLOY=-DaltDeploymentRepository=LocalGISIII::default::%REPOSITORY_MAVEN%
set OPTIONS_SIGN=%OPTIONS_SIGN% -Durl=%REPOSITORY_MAVEN%
GOTO CONTINUE

:NOSIGNEXT
set OPTIONS=-Dmaven.clean.failOnError=false --strict-checksums
call perfiles/env_ext_%USERNAME%.bat
set VARIABLES=-DappInstallerPath=%appInstallerPath% -DappModulesPath=%appModulesPath% -DappWebAppsPath=%appWebAppsPath% -DappPluginsPath=%appPluginsPath%
set OPTIONS_SIGN=-Djarsigner.skip=true
set SETTINGS=settings\settings_lcg3_ext_nosign.xml
set REPOSITORY_MAVEN=%MAVEN_NO_SIGN_REPO%
set DATOS_DEPLOY=-DaltDeploymentRepository=LocalGISIII::default::%REPOSITORY_MAVEN%
set OPTIONS_SIGN=%OPTIONS_SIGN% -Durl=%REPOSITORY_MAVEN%
GOTO CONTINUE

:NOSIGN
set OPTIONS=-Dmaven.clean.failOnError=false --strict-checksums
set OPTIONS_SIGN=-Djarsigner.skip=true
set SETTINGS=settings\settings_lcg3_nosign.xml
set REPOSITORY_MAVEN=%MAVEN_NO_SIGN_REPO%
set DATOS_DEPLOY=-DaltDeploymentRepository=LocalGISIII::default::%REPOSITORY_MAVEN%
set OPTIONS_SIGN=%OPTIONS_SIGN% -Durl=%REPOSITORY_MAVEN%
GOTO CONTINUE

:CLEAN
set OPTIONS=-Dmaven.clean.failOnError=false --strict-checksums
set OPTIONS_SIGN=-Djarsigner.skip=false
set SETTINGS=settings\settings_lcg3.xml
set REPOSITORY_MAVEN=%MAVEN_SIGN_REPO%
set DATOS_DEPLOY=-DaltDeploymentRepository=LocalGISIII::default::%REPOSITORY_MAVEN%
set OPTIONS_SIGN=%OPTIONS_SIGN% -Durl=%REPOSITORY_MAVEN%
GOTO CONTINUE

:CONTINUE
IF "%2"=="" GOTO ALL
IF "%2"=="user" GOTO USER
IF "%2"=="install" GOTO INSTALL
IF "%2"=="installmodules" GOTO INSTALLMODULES
IF "%2"=="module" GOTO MODULE
IF "%2"=="rutas" GOTO RUTAS
GOTO SIMPLE



:ALL
echo Compilando todos los modulos.Firmado %OPTIONS_SIGN%
call perfiles/lcg3_artifacts.bat
GOTO END

:USER
echo Compilando los modulos especificos del usuario.Firmado %OPTIONS_SIGN%
call perfiles/lcg3_artifacts_%USERNAME%.bat
GOTO END

:MODULE
echo Compilando los modulos especificos .Firmado %OPTIONS_SIGN%
IF "%3"=="" GOTO END
set MODULE=%3%
call perfiles/lcg3_artifacts_modules.bat
GOTO END

:INSTALL
echo Compilando los modulos de instalacion.Firmado %OPTIONS_SIGN%
call perfiles/lcg3_artifacts_install.bat
GOTO END

:INSTALLMODULES
echo Compilando los modulos de instalacion.Firmado %OPTIONS_SIGN%
call perfiles/lcg3_artifacts_install_modules.bat
GOTO END

:RUTAS
echo Compilando los modulos de instalacion.Firmado %OPTIONS_SIGN%
call perfiles/lcg3_artifacts_rutas.bat
GOTO END

:SIMPLE
REM set SETTINGS=settings\settings_lcg3.xml
set ARTIFACT=%2
echo Compilando/Limpiando el artifact %ARTIFACT%
call perfiles/lcg3_simple_artifact.bat
GOTO END


:END
echo Fin de compilacion/despliegue/limpieza



For /f "tokens=1-4 delims=/ " %%a in ('date /t') do (set mydate_fin=%%c-%%a-%%b)
For /f "tokens=1-2 delims=/:" %%a in ("%TIME%") do (set mytime_fin=%%a:%%b)

echo -Tiempo Inicio: %mydate_inicio% %mytime_inicio% 
echo -Tiempo Fin   : %mydate_fin% %mytime_fin% 

endlocal