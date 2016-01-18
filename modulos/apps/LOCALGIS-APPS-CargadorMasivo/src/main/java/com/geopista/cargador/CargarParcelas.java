/**
 * CargarParcelas.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.cargador;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.GeopistaMostrarParcelas;
import com.geopista.editor.GeopistaEditor;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.util.GeopistaCommonUtils;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class CargarParcelas extends CargadorBase{
	



	private GeopistaLayer capaParcelasFuente = null;
	private GeopistaLayer capaParcelas = null;
	private GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();

	/**
	 * Ejemplo uso de la aplicacion para indicar una sola provincia o un sol
	 * municipio. Si no incluimos nada por defecto se cargan todos. Estos
	 * parametros en el eclipse hay que incluirlos en la seccion Program
	 * Arguments. -provincia 32 -municipio 32001
	 */


	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(CargarParcelas.class);

	/**
	 * Arranque del cargador de parcelas.
	 * 
	 */
	public boolean start(String args[]) {
		boolean sobreescribir = true;
		File parentDirectory=null;
		
		
		/*Cargamos los argumentos de entrada*/
		super.start(args);
			
		
		/**
		 * we load the cartography folder
		 */
		if (codCartografiaPathOption!=null){
			//if he entered a path we need to check it exists and that we can access the directory
			if (GeopistaCommonUtils.revisarDirectorio(codCartografiaPathOption)){
				parentDirectory = new File(codCartografiaPathOption);
			}else{
				//the directory passed is not existing, we need to stop the importation
				logger.info("No se ha encontrado la carpeta conteniendo todos los datos de cartografia.");
				return false;
			}
			CargadorUtil.homeCartografia = codCartografiaPathOption;
		}else{
			//user didn't enter information for codCartografiaPath, we load the default directory (after having checked it)
			if (GeopistaCommonUtils.revisarDirectorio(CargadorUtil.homeCartografia)){
				parentDirectory = new File(CargadorUtil.homeCartografia);
			}else{
				logger.info("No se ha encontrado la carpeta conteniendo todos los datos de cartografia.");
				return false;
			}
		}
		
		
		// Si el usuario no introduce ningún código en los argumentos de entrada
		// Cargamos todos los directorios contenidos en el directorio Municipio
		File[] nodesProvincias = parentDirectory.listFiles();
		String[] files = new String[2];
		// Si el usuario introduce un código de municipio concreto...

		// El primer nivel de indireccion es el codigo de Provincia (32, 34,
		// ...)
		for (int indiceProvincia = 0; indiceProvincia < nodesProvincias.length; indiceProvincia++) {

			if (nodesProvincias != null
					&& (!nodesProvincias[indiceProvincia].isDirectory() || (nodesProvincias[indiceProvincia]
							.getName() != null && nodesProvincias[indiceProvincia]
							.getName().length() != PROVINCIA_LENGTH))) {
				// this is not a directory, we go to the next node
				// same if folder name is not of 2 characters (province code)
				continue;
			}

			// We get the "Municipios" list inside a Province.
			File[] nodesMunicipios = nodesProvincias[indiceProvincia]
					.listFiles();
			for (int indiceMunicipio = 0; indiceMunicipio < nodesMunicipios.length; indiceMunicipio++) {

				if (nodesMunicipios != null
						&& (!nodesMunicipios[indiceProvincia].isDirectory() || (nodesMunicipios[indiceMunicipio]
								.getName() != null && nodesMunicipios[indiceMunicipio]
								.getName().length() != MUNICIPIO_LENGTH))) {
					// this is not a directory, we go to the next node
					// same if folder name is not of 5 characters
					continue;
				} else {
					// we need to check if we have to load that directory
					logger.info("*********************************** "
							+ nodesMunicipios[indiceMunicipio].getName());
					// if user entered a "diputacion", we need to load all folders
					// starting with the "diputacion" code
					if (codProvinciaOption!=null) {
						if (!nodesMunicipios[indiceMunicipio].getName()
								.substring(0, 2).equals(codProvinciaOption)) {
							// if the two first characters are not equals to the
							// "diputación" code, we go to the next folder
							continue;
						}
					}
					if (codMunicipioOption!=null) {
						if (!nodesMunicipios[indiceMunicipio].getName()
								.substring(0, 5).equals(codMunicipioOption)) {
							// if the five characters entered by user are not
							// equals to the "Municipio" code, we go to the next
							// folder
							continue;
						}
					}
				}

				File[] nodesTipoMunicipio = nodesMunicipios[indiceMunicipio].listFiles();

				// Dentro del nodo del municipio puede existir la carpeta
				// cartografia base o la cartografia urbana
				// Este bucle puede ser de una iteracion o de dos.
				for (int indiceTipoMunicipio = 0; indiceTipoMunicipio < nodesTipoMunicipio.length; indiceTipoMunicipio++) {
					init_DestroyParcelario();
					geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
					
					
					String codMunicipio = nodesMunicipios[indiceMunicipio]
							.getName();
					String sFolderName = nodesTipoMunicipio[indiceTipoMunicipio]
					            							.getName();
					logger.info("Cargando codigo Municipio:" + codMunicipio+" ("+sFolderName+")");


					if (sFolderName.equals("Rustico")
							|| sFolderName.equals("Urbano")) {
						//logger.info("Rutas de los ficheros urbano/rustica correctas");
						CargadorUtil.folderMunicipio = nodesProvincias[indiceProvincia]
								.getName()
								+ File.separator
								+ nodesMunicipios[indiceMunicipio].getName()
								+ File.separator + File.separator + sFolderName;
					} else {
						continue;
					}
					// ****************************************************
					// Se buscan los ficheros base de cartografia
					// Deben de estar en el directorio Home por debajo de la
					// carpeta
					// CartografiaBase. (Ver fichero Leeme.txt)
					// ****************************************************
					if (findFiles(CargadorUtil.folderMunicipio, codMunicipio,files)) {
						logger.info("Rutas de los ficheros correctas.");
					} else {
						logger.info("La ruta de los ficheros no es correcta.");
						logger.info("La importacion ha sido cancelada.");
						continue;
					}
					String  fichSHP=files[0]; 
					String fichDBF=files[1];
					
					//Login
					if (!login(aplicacion,user,pass))
						continue;
					try{
					  
					  blackboard.put("capaParcelasInfoReferencia", null);
					  blackboard.put("geopistaEditorInfoReferencia", geopistaEditor1); 
					  if(validacion(fichSHP))
						  grabarFichero(sobreescribir); 
					  	else 
					  logger.info("El  fichero de parcelas no es correcto."); 
					}
					catch(Exception e){
						logger.info("Error en la carga de Parcelas."); 
					}					
				}
			}
		}
		//Inicializamos el Callejero para empezar de nuevo
		init_DestroyParcelario();
		return true;
	}

	/**
	 * Verificamos que el directorio home con la cartografia base existe y que
	 * todos los ficheros que se necesitan aparecen en el mismo.
	 * 
	 * @param folderMunicipio
	 * @param idMunicipio
	 * @param files
	 *            Ficheros a utilizar.
	 * @return
	 */
	public boolean findFiles(String folderMunicipio, String idMunicipio,
			String[] files) {

		String cartografiaCatastro = CargadorUtil.homeCartografia
				+ File.separator + folderMunicipio + File.separator
				+ "Catastro";

		if (GeopistaCommonUtils.revisarDirectorio(CargadorUtil.homeCartografia
				+ File.separator + folderMunicipio)) {
			// Set the "Municipio"
			aplicacion = (AppContext) AppContext.getApplicationContext();
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID,
					idMunicipio.substring(0, 5));
		} else {
			logger.info("El directorio:" + folderMunicipio + " no existe");
		}

		files[0] = cartografiaCatastro + File.separator + "PARCELA.SHP";
		files[1] = cartografiaCatastro + File.separator + "PARCELA.DBF";
		boolean ok = true;
		for (int i = 0; i < files.length; i++) {
			if (!GeopistaCommonUtils.revisarDirectorio(files[i])) {
				ok = false;
				break;
			}
		}
		return ok;
	}

	/**
	 * Método que valida el fichero parcelas.shp
	 * 
	 * @param File
	 *            shp, fichero parcelas.shp
	 * @return boolean true si es correcto, false en caso contrario
	 */
	private boolean validacion(String shp) {
		// inicializamos los valores para cada proceso de importacion
		boolean hayErroresFila = false;
		boolean hayErroresFilas = false;
		String texto[] = new String[1];
		texto[0] = "";
		GeopistaLayer layer08 = null;
		try {
			layer08 = com.geopista.app.catastro.GeopistaSeleccionarFicheroParcelas
					.loadLayer(geopistaEditor1, shp, blackboard, aplicacion);
			blackboard.put("capaParcelasInfoReferencia", layer08);
			hayErroresFilas = com.geopista.app.catastro.GeopistaSeleccionarFicheroParcelas
					.validate(layer08, texto, aplicacion);
		} catch (Exception e) {
			hayErroresFilas = true;
		}
		if (hayErroresFilas == true) {
			hayErroresFila = true;
			texto[0] = texto[0]
					+ "\nEl fichero no es correcto. No se podrán importar estos datos.";
		} else {
			hayErroresFila = false;
			texto[0] = texto[0]
					+ "\nEl proceso de validación ha finalizado correctamente.";
		}
		System.out.println(CargadorUtil.filterText(texto[0]));

		return !hayErroresFila && !hayErroresFilas;
	}

	/**
	 * Guarda en la BBDD los datos extraidos del fichero parcelas.shp
	 * 
	 * @param boolean
	 *            sobreescribir, sobreescribe los datos si la capa ya contiene
	 *            valores
	 */
	public void grabarFichero(boolean sobreescribir) {
		geopistaEditor1 = (GeopistaEditor) blackboard
				.get("geopistaEditorInfoReferencia");
		boolean lastValue = geopistaValidatePlugIn.isMakeInsertion();
		boolean manualModification = false;
		boolean firingEvents = false;
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), geopistaEditor1.getContext()
						.getErrorHandler());
		try {
			System.out.println(CargadorUtil.filterText(aplicacion
					.getI18nString("CargandoMapaInforeferencia")));
			// Leemos el mapa
			capaParcelasFuente = (GeopistaLayer) blackboard
					.get("capaParcelasInfoReferencia");
			boolean isGraphic = false;
			boolean cancelar[] = { false };
			capaParcelas = GeopistaMostrarParcelas.getLayerFuente(
					capaParcelasFuente, isGraphic, sobreescribir, aplicacion,
					geopistaEditor1, cancelar);
			if (cancelar[0])
				return;

			try {
				// Leemos el mapa
				String[] cadenaTexto = new String[1];
				firingEvents = GeopistaMostrarParcelas.insertFeatures(
						capaParcelas, capaParcelasFuente,
						geopistaValidatePlugIn, aplicacion, progressDialog,
						manualModification, cadenaTexto, isGraphic);
				System.out.println(CargadorUtil.filterText(cadenaTexto[0]));
				// fin pasar los valores
			} catch (Exception e) {
				e.printStackTrace();
				System.out
						.println("Se han producido errores que no permiten continuar con la importación.");
				return;
			} finally {
				try {
					geopistaValidatePlugIn.setMakeInsertion(lastValue);
					capaParcelas.getLayerManager()
							.setFiringEvents(firingEvents);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			System.out
					.println("Se han producido errores que no permiten continuar con la importación.");
			return;
		}
	}

	/**
	 * Destruccion del parcelario
	 */
	private void init_DestroyParcelario() {

		if (geopistaEditor1 != null)
			geopistaEditor1.destroy();
		geopistaEditor1 = null;
		

		blackboard.remove("geopistaEditorInfoReferencia");
		if (capaParcelasFuente != null)
			capaParcelasFuente.dispose();
		capaParcelasFuente = null;

		if (capaParcelas != null)
			capaParcelas.dispose();
		capaParcelas = null;

	}



	public static void main(String args[]) {
		logger.info("Comenzando la carga de parcelario");
		boolean ok = new CargarParcelas().start(args);
		if (!ok)
			logger.info("La importación de parcelas ha sido cancelada.");
		else
			logger.info("La importación de parcelas ha sido correcta.");
		System.exit(0);
		
	}
}
