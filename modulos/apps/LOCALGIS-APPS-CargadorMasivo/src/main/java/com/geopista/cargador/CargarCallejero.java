/**
 * CargarCallejero.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.cargador;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.DefaultListModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inforeferencia.DatosViasINE;
import com.geopista.app.inforeferencia.GeopistaAsociarCatastroINEViasPanel;
import com.geopista.app.inforeferencia.GeopistaEnlazarPoliciasViasPanel;
import com.geopista.app.inforeferencia.GeopistaMostrarCallejeroPanel;
import com.geopista.editor.GeopistaContext;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaSystemLayers;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.components.FeatureExpressionPanel;
import com.geopista.ui.dialogs.DistanceLinkingPanel;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.plugin.analysis.DistanceLinkingPlugIn;
import com.geopista.ui.plugin.analysis.GeopistaFusionViasPlugIn;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaCommonUtils;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class CargarCallejero extends CargadorBase{
	
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(CargarCallejero.class);
	public static final int TRAMOS_VIAS_INE_LINEA_LONGITUD = 273;
	public static final int VIAS_INE_LINEA_LONGITUD = 108;

	public static final String NOESPECIFICADO = "NE";
	private GeopistaLayer capaTramosVias = null;
	private GeopistaLayer viasLayer = null;
	private GeopistaLayer layer09 = null;
	private Connection con = null;
	private Layer capaTramos = null;
	private Layer capaVias = null;
	private ArrayList listViasINEInsertadas = null;

	String nombreCatastro = null;
	private DefaultListModel listEnlazadosModel = new DefaultListModel();

	DefaultListModel listViaIneModel = new DefaultListModel() {
		public void addElement(Object obj) {
			for (int n = 0; n < this.size(); n++) {
				DatosViasINE currentViaIne = (DatosViasINE) getElementAt(n);
				DatosViasINE newViaIne = (DatosViasINE) obj;
				if (newViaIne.getNombreVia().trim().compareTo(
						currentViaIne.getNombreVia().trim()) < 0) {
					add(n, obj);
					return;
				}
			}
			super.addElement(obj);
		}
	};

	DefaultListModel listCatastroModel = new DefaultListModel() {
		public void addElement(Object obj) {
			for (int n = 0; n < this.size(); n++) {
				GeopistaFeature newFeature = (GeopistaFeature) obj;
				GeopistaSchema schemaViasLayer = (GeopistaSchema) newFeature
						.getSchema();
				String newNombreCatastro = schemaViasLayer
						.getAttributeByColumn("nombrecatastro");
				GeopistaFeature currentViaCatastro = (GeopistaFeature) getElementAt(n);
				GeopistaSchema currentSchemaViasLayer = (GeopistaSchema) currentViaCatastro
						.getSchema();
				String currentNombreCatastro = currentSchemaViasLayer
						.getAttributeByColumn("nombrecatastro");

				if (newFeature.getString(nombreCatastro).trim().compareTo(
						currentViaCatastro.getString(nombreCatastro).trim()) < 0) {
					add(n, obj);
					return;
				}
			}
			super.addElement(obj);
		}
	};
	

	

	
	/**
	 * Arranque del cargador de callejero.
	 *
	 */
	public boolean start(String args[]) {
		
		/*Cargamos los argumentos de entrada*/
		super.start(args);

		//****************************************************
		//Se buscan los ficheros base de cartografia 
		//Deben de estar en el directorio Home por debajo de la carpeta
		//CartografiaBase. (Ver fichero Leeme.txt)
		//****************************************************
		String[] files = new String[7];
		File parentDirectory=null;
		
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
		
		/**
		 * We load the Results Folder
		 */
		if (codResultadosPathOption!=null){
			//if he entered a path we need to check it exists and that we can access the directory
			if (GeopistaCommonUtils.revisarDirectorio(codResultadosPathOption)){
				// we know we can access the folder. We need to check it is write-enabled so that 
				// we can save the results. 
				File fResultTemp = new File(codResultadosPathOption);
				if (fResultTemp.canWrite()){
					CargadorUtil.folderResultadoBase=new String(codResultadosPathOption);
				}else{
					logger.info("No se puede escribir en la carpeta de Resultados. Por favor introduzca una carpeta con permisos de escritura. ");
					return false;
				}
			}else{
				//the directory passed is not existing, we cannot save results in that folder. We stop the execution
				logger.info("No se ha encontrado la carpeta que va a almacenar los resultados.");
				return false;
			}
		}else{
			//user didn't enter information for codCartografiaPath, we load the default directory (after having checked it)
			if (GeopistaCommonUtils.revisarDirectorio(CargadorUtil.homeCartografia)){
				File fResultTemp = new File(CargadorUtil.homeCartografia);
				// We check we can write in that folder
				if (fResultTemp.canWrite()){
					CargadorUtil.folderResultadoBase=new String(CargadorUtil.homeCartografia);
				}else{
					logger.info("No se puede escribir en la carpeta de Resultados. Por favor introduzca una carpeta con permisos de escritura. ");
					return false;
				}
			}else{
				logger.info("No existe la carpeta que esta definida por defecto para los resultados.");
				logger.info("Se ha cancelado la importación.");
				return false;
			}
		}
		
		
		File[] nodesProvincias = parentDirectory.listFiles();
		
		//El primer nivel de indireccion es el codigo de Provincia (32, 34, ...)		
		for (int indiceProvincia=0; indiceProvincia<nodesProvincias.length; indiceProvincia++){
			
			if (nodesProvincias!=null && 
					(!nodesProvincias[indiceProvincia].isDirectory() || (nodesProvincias[indiceProvincia].getName()!=null && 
								nodesProvincias[indiceProvincia].getName().length()!=PROVINCIA_LENGTH))){
				// this is not a directory, we go to the next node
				// same if folder name is not of 2 characters (province code)
				continue;				
			}
			
			//We get the "Municipios" list inside a Province.
			File[] nodesMunicipios = nodesProvincias[indiceProvincia].listFiles();
			for (int indiceMunicipio=0; indiceMunicipio<nodesMunicipios.length; indiceMunicipio++){
			
				if (nodesMunicipios!=null && (!nodesMunicipios[indiceProvincia].isDirectory() || 
							(nodesMunicipios[indiceMunicipio].getName()!=null && nodesMunicipios[indiceMunicipio].getName().length()!=MUNICIPIO_LENGTH))){
					// this is not a directory, we go to the next node
					// same if folder name is not of 5 characters
					continue;
				}else{ 
					// we need to check if we have to load that directory
					logger.info("******************************************* "+nodesMunicipios[indiceMunicipio].getName());
					// if user entered a "diputacion", we need to load all folders 
					// starting with the "diputacion" code
					if (codProvinciaOption!=null){
						if (!nodesMunicipios[indiceMunicipio].getName().substring(0, 2).equals(codProvinciaOption)){
							// if the two first characters are not equals to the "diputación" code, we go to the next folder
							continue;
						}
					}
					if (codMunicipioOption!=null){
						if (!nodesMunicipios[indiceMunicipio].getName().substring(0, 5).equals(codMunicipioOption)){
							// if the five characters entered by user are not equals to the "Municipio" code, we go to the next folder
							continue;
						}
					}
				}
				
				
				
				File[] nodesTipoMunicipio = nodesMunicipios[indiceMunicipio].listFiles();
				blackboard.remove("viasINEInsertadas");
				//Dentro del nodo del municipio puede existir la carpeta cartografia base o la cartografia urbana
				//Este bucle puede ser de una iteracion o de dos.
				for (int indiceTipoMunicipio=0; indiceTipoMunicipio<nodesTipoMunicipio.length; indiceTipoMunicipio++){
					
					// Inicializamos el Callejero para empezar de nuevo
					init_DestroyCallejero();
					
					
					geopistaEditor1 = new GeopistaEditor(aplicacion.getString("fichero.vacio"));
					
					String codMunicipio=nodesMunicipios[indiceMunicipio].getName();
					logger.info("Cargando codigo Municipio:"+codMunicipio);
					
					String sFolderName = nodesTipoMunicipio[indiceTipoMunicipio].getName();
					
					if (sFolderName.equals("Rustico") || sFolderName.equals("Urbano")){
						CargadorUtil.folderMunicipio=
								nodesProvincias[indiceProvincia].getName() + File.separator +
								nodesMunicipios[indiceMunicipio].getName() + File.separator +
								sFolderName;
						
					}else{
						continue;
					}
	
					if (findFiles(CargadorUtil.folderMunicipio, nodesProvincias[indiceProvincia].getName(), codMunicipio,files)) {
						logger.info("Rutas de los ficheros correctas.");
					} else {
						logger.info("La ruta de los ficheros no es correcta.");
						logger.info("La importacion ha sido cancelada.");
						continue;
					}

					//Login
					if (!login(aplicacion,user,pass))
						continue;
					
					//Validate the files
					if (!validarFicheros(files)){
						logger.info("La importación del municipio " + codMunicipio + "\\" + sFolderName + " ha sido cancelada");
						continue;
					}
					//Store the information in the database
					if (!grabarInformacion(indiceTipoMunicipio!=0)){
						continue;
					}
				}
			}	
		}
		
		//Inicializamos el Callejero para empezar de nuevo
		init_DestroyCallejero();
	
		
		/*boolean continuar=true;
		logger.info("Me quedo Garbageando!!!!!! (y no saco más trazas pa no estorbar)");
		while (continuar){			
			try {
				//logger.info("Garbage Collector");
				Thread.sleep(5000);
				Runtime.getRuntime().gc();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		return true;
	}
		
	/**
	 * Verificamos que el directorio home con la cartografia base
	 * existe y que todos los ficheros que se necesitan aparecen en
	 * el mismo.
	 * @param folderMunicipio
	 * @param idMunicipio
	 * @param files Ficheros a utilizar.
	 * @return
	 */
	public boolean findFiles(String folderMunicipio, String indiceProvincia, String idMunicipio,String[] files) {

		String cartografiaCatastro = CargadorUtil.homeCartografia + File.separator + 
							folderMunicipio + File.separator+ "Catastro";
		String cartografiaINE = CargadorUtil.homeCartografia + File.separator + 
							folderMunicipio + File.separator + "INE";
		
		if (GeopistaCommonUtils.revisarDirectorio(CargadorUtil.homeCartografia + File.separator 
				+ folderMunicipio)){
			//Set the "Municipio"
			aplicacion = (AppContext) AppContext.getApplicationContext();
			UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID, idMunicipio.substring(0,5));			
		}
		else{
			logger.info("El directorio:"+folderMunicipio+" no existe");
		}
		
		
		// We implement the files array list with the path of the INE files
		files[0] = cartografiaINE + File.separator + "TRAMP"+ idMunicipio.substring(0, 2);
		files[1] = cartografiaINE + File.separator + "VIASP"+ idMunicipio.substring(0, 2);
		files[2] = cartografiaCatastro + File.separator + "EJES.SHP";
		files[3] = cartografiaCatastro + File.separator + "EJES.DBF";
		files[4] = cartografiaCatastro + File.separator + "Carvia.DBF";
		files[5] = cartografiaCatastro + File.separator + "ELEMTEX.SHP";
		files[6] = cartografiaCatastro + File.separator + "ELEMTEX.DBF";
		
		

		//Verificamos que los ficheros existen
		boolean ok = true;
		
		// We check los INE files first. First we check into the Municipio postal code folder. If they don't exist there 
		// we need to check them a level above (into the Municipio folder)
		// check the Cartografiabase structure into the file CartografiaBase/Leeme.txt
		for (int i=0; i<2; i++){
			if (!GeopistaCommonUtils.revisarDirectorio(files[i])){
				ok = false;
			}
		}
		
		// If they are not located there, we need to check their location a level above
		if (!ok){
			ok = true;
			cartografiaINE = CargadorUtil.homeCartografia + File.separator + indiceProvincia + File.separator + "INE";
			files[0] = cartografiaINE + File.separator + "TRAMP"+ idMunicipio.substring(0, 2);
			files[1] = cartografiaINE + File.separator + "VIASP"+ idMunicipio.substring(0, 2);
			for (int i=0;i<2;i++){
				if (!GeopistaCommonUtils.revisarDirectorio(files[i])){
					ok = false;
				}
			}
		}
		
		// If ok is still at false, then this function needs to return a false value
		// so that user will know files are missing. 
		// if not we need to check if catastro files are misssing or not. 
		if (!ok){
			return false;
		}else{
			// ok is true so we need to check Catastro files are there. 
			for (int i = 0; i < files.length; i++) {
				if (!GeopistaCommonUtils.revisarDirectorio(files[i])) {
					ok = false;
					break;
				}
			}
	
			return ok;
		}
	}
	

	/**
	 * Validate that the files are correct.
	 * @param files
	 * @return
	 */	
	public boolean validarFicheros(String[] files){
		String fichTramoViaINE = files[0];// INE\\TRAMP32
		String fichViaINE = files[1];// INE\\VIASP32
		String fichTramoViaSHP = files[2];// Catastro\\EJES.SHP
		String fichTramoViaDBF = files[3];// Catastro\\EJES.DBF
		String fichCarvia = files[4];// Catastro\\Carvia.DBF
		String fichPoliciaSHP = files[5];// Catastro\\ELEMTEX.DBF
		String fichPoliciaDBF = files[6];// Catastro\\ELEMTEX.SHP
	
		//bCharged = true;
		ValidadorFicheros validador = new ValidadorFicheros(blackboard,	aplicacion, geopistaEditor1);
		logger.info("PASO1. Comienzo validación Tramos via INE.");
		if (!validador.validateTramosViaINE(fichTramoViaINE)){
			logger.info("Tramos de via INE incorrectos.");
			logger.info("La importacion ha sido cancelada.");
			return false;
		}


		logger.info("PASO2. Validando Datos Tramos Via");
		if (!validador.validateTramosVia(fichTramoViaSHP, fichCarvia)){
			//return ok;					
			logger.info("Tramos de via Catastro incorrectos.");
			logger.info("La importacion de tramos de via de catastro ha sido cancelada.");
			return false;
		}

		logger.info("PASO3. Validando Números de policía.");
		if (!validador.validateNumPolicia(fichPoliciaSHP)){
			logger.info("La importacion ha sido cancelada.");
			return false;
		}
		logger.info("PASO4. Validando Vias INE.");
		if (!validador.validateViasINE(fichViaINE)){
			//return ok;					
			logger.info("La importacion ha sido cancelada.");
			return false;
		}
		logger.info("FIN. Validación correcta.");	
		return true;
	}
	
	
	
	/**
	 * Store the information in the files in the database
	 * @param sobreescribir si queremos añadir datos o borrar los ya existentes para hacer una nueva importación
	 * 
	 * @return a boolean a true si se ha realizado corectamente
	 */
	public boolean grabarInformacion(boolean sobreescribir){
		//boolean sobreescribir = true; //si se desea sobreescribir los datos de num_policia cuando el programa detecta que ya hay en la bbdd
		boolean fusion = true; //Si se desea realizar la fusión de tramos de vía en vías
		boolean enlazar = false; //Si se desea enlazar los números de policía con las vías más cercanas

		//************************************************************
		//Comienzo de la grabacion de datos. Inicialmente se 
		//graban todos los registros en las distintas tablas de la Base
		//de Datos y luego se trabaja con esa informacion.
		//************************************************************
		if (grabarDatos(sobreescribir)) {
			
			geopistaEditor1 = (GeopistaEditor) blackboard.get("geopistaEditorEnlazarPoliciaCalles");
			GeopistaFusionViasPlugIn geopistaFusionViasPlugIn = new GeopistaFusionViasPlugIn();
			geopistaEditor1.addPlugIn(geopistaFusionViasPlugIn);
			geopistaEditor1.setVisible(false);

			if (fusion) {
				GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.InfReferencia.FusionTramosVias");
				//******************************************
				//Verificamos los permisos.
				//******************************************
				if (aplicacion.checkPermission(geopistaPerm,"Informacion de Referencia")) {
					capaTramos = geopistaEditor1.getLayerManager().getLayer(GeopistaSystemLayers.TRAMOSVIAS);
					capaVias = geopistaEditor1.getLayerManager().getLayer(GeopistaSystemLayers.VIAS);
					if (capaTramos == null || capaVias == null) {
						logger.info(aplicacion.getI18nString("GeopistaFusionViasPlugIn.noLayersMessage"));
					} else {						
						//****************************************
						//Fusionamos los tramos de via con las vias.
						//****************************************
						try {
							fusionarViasTramos(capaVias, capaTramos);
						} catch (Exception e) {
							logger.info("Ha habido un error. No ha sido posible fusionar las capas Vias y TramosVias.");
						}
					}
				}			
			}
			
			
			//****************************************
			//Conexion de numeros de policia con vias.
			//****************************************
			if (enlazar) {
				if (connectNumPoliciaVias())
					logger.info("Capas conectadas correctamente");
				else
					logger.info("Error al conectar capa Numeros de Policía con Vías");
			}
			
			//******************************************
			//Almacenamos los numeros de policia creados.
			//******************************************
			if (saveNumPolicia()) {
				logger.info("Numeros de policia grabados.");
				if (asociar()) {
					logger.info("Importación callejero correcta.");
				} else {
					logger.info("Importación datos callejero cancelada.");
				}
			}
			
		}
		
		try {
			Connection con = aplicacion.getConnection();
			if (con!=null && !con.isClosed()){
				//con.commit();
				con.close(); 
			}
		}catch (SQLException ex){
			logger.info(aplicacion.getI18nString("NoConexionBaseDatos"));
			ex.printStackTrace();
		}		
		return true;	
	}
	
	
	
	
	/**
	 * Destruccion del callejero
	 */
	private void init_DestroyCallejero(){
		
		if (geopistaEditor1!=null)
			geopistaEditor1.destroy();
		geopistaEditor1=null;
		
		//TODO Verificar si hay que eliminar el blackboard cada vez.
		/*if (blackboard!=null){
			blackboard.removeAll();
		}		
		blackboard=null;*/
		blackboard.remove("geopistaEditorEnlazarPoliciaCalles");

		if (capaTramos!=null)
			capaTramos.dispose();
		capaTramos=null;
				
		if (capaTramosVias!=null)
			capaTramosVias.dispose();
		capaTramosVias = null;
		
		if (capaVias!=null)
			capaVias.dispose();
		capaVias = null;

		if (viasLayer!=null)			
			viasLayer.dispose();
		viasLayer = null;
		
		if (layer09!=null)
			layer09.dispose();
		layer09 = null;
		if (listViasINEInsertadas!=null)
			listViasINEInsertadas.clear();
		listViasINEInsertadas=null;
		
		if (listCatastroModel!=null)
			listCatastroModel.clear();
		//listCatastroModel=null;
		
		if (listEnlazadosModel!=null)
			listEnlazadosModel.clear();
		//listEnlazadosModel=null;
		
		if (listViaIneModel!=null)
			listViaIneModel.clear();
		//listViaIneModel=null;
	}


	/**
	 * Método para insertar los datos de los ficheros en la BBDD
	 * 
	 * @param boolean
	 *            continuar, permite añadir valores de números de policía a los ya existentes
	 * @return boolean true si es correcto, false en caso contrario
	 */
	public boolean grabarDatos(boolean sobreescribir) {
		boolean ok = false;

		
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		
		//int idMunicipio = Integer.parseInt(aplicacion.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
		
		try {
			if (con == null) {
				con = getDBConnection();
			}

			GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();
			String cadenaTexto = "";
			boolean isGraphic = false;
			try {
				// Cargamos el editor
				if (capaTramosVias == null) {
					try {
						
						GeopistaMostrarCallejeroPanel.loadEditorPlugins(aplicacion, geopistaEditor1, geopistaValidatePlugIn);
						
						String rutaPolicia = (String) blackboard.get("ficheroNumerosPolicia");//elemtext.shp
						layer09 = (GeopistaLayer) geopistaEditor1.loadData(rutaPolicia,
										aplicacion.getI18nString("importar.informacion.referencia.tramos.via"));
						capaTramosVias = GeopistaMostrarCallejeroPanel
								.loadFeatures(aplicacion, blackboard,geopistaEditor1, capaTramosVias,
										geopistaValidatePlugIn, layer09,isGraphic, progressDialog);

						if (geopistaEditor1.getLayerManager().getLayer("numeros_policia")
								.getFeatureCollectionWrapper().getFeatures().size() > 0) {
							if (sobreescribir) {
								logger.info("La Capa Números de Policia ya contiene Valores, los nuevos valores se añadirán a los ya existentes");
							} else {
								try {
									//borramos los datos de la base de datos
									GeopistaMostrarCallejeroPanel.borrarNumerosPolicia(aplicacion, blackboard, geopistaEditor1, isGraphic, progressDialog);
									logger.info("Se ha borrado los numeros de policia previamente añadidos en la base de datos para ese municipio.");
								}catch (Exception ex){
									logger.info("Un error ha occurido mientras se intentaba borrar los registros de numero policia.");
									return ok;
								}
							}
						}
						if (!sobreescribir){
							try{
								GeopistaMostrarCallejeroPanel.borrarViasINE(aplicacion, blackboard, con);
								logger.info("Se ha borrado las vias ine previamente añadidos en la base de datos para ese municipio.");
								GeopistaMostrarCallejeroPanel.borrarTramosViasINE(aplicacion, blackboard, con);
								logger.info("Se ha borrado los tramos via ine previamente añadidos en la base de datos para ese municipio.");
							}catch (Exception ex){
								logger.info("Un error ha ocurrido durante el proceso de limpieza de la base de datos.");
								logger.info("Se ha intentado borrar los tramos y las vias INE pero una excepción ha ocurido.");
								return ok;
							}
						}
					} catch (Exception e) {
						logger.info(aplicacion.getI18nString("errorCargaMapa"));
						throw e;
					}
					// Abrir una transaccion
					cadenaTexto = GeopistaMostrarCallejeroPanel.insertLayers(
							aplicacion, blackboard, con,geopistaValidatePlugIn, geopistaEditor1,
							capaTramosVias, layer09, isGraphic, progressDialog);
					logger.info(CargadorUtil.filterText(cadenaTexto));
					blackboard.put("geopistaEditorEnlazarPoliciaCalles",geopistaEditor1);
					ok = true;
					progressDialog.dispose();
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.info(aplicacion.getI18nString("SeHanProducidoErrores"));
			}
		} catch (SQLException e) {
			logger.info(aplicacion.getI18nString("NoConexionBaseDatos"));
		}
		return ok;
	}

	
	/**
	 * Establece la conexion con la base de datos
	 * 
	 * @return Connection, conexion
	 */
	public static Connection getDBConnection() throws SQLException {

		ApplicationContext app = AppContext.getApplicationContext();
		Connection conn = app.getConnection();
		conn.setAutoCommit(false);
		return conn;
	}

	public void fusionarViasTramos(Layer capaVias, Layer capaTramosVias)
			throws Exception {
		Hashtable targetHash = new Hashtable();
		TaskMonitor globalMonitor = null;
		final boolean isGraphic = false;
		
		targetHash = GeopistaFusionViasPlugIn.fusionVias(capaTramosVias,
				capaVias, globalMonitor, targetHash, isGraphic);

		GeopistaFusionViasPlugIn.executeFusion(capaTramosVias, capaVias,
				globalMonitor, targetHash, isGraphic);
	}

	/**
	 * Método para grabar en la BBDD los datos correspondientes a los numeros de policia
	 * @return boolean
	 * 				true si se han grabado correctamente, false en caso contrario
	 * 
	 */
	public boolean saveNumPolicia() {
		boolean ok = false;
		try {
			logger.info(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
			GeopistaEnlazarPoliciasViasPanel.saveNumPolicia(geopistaEditor1,
					aplicacion);
			ok = true;
		} catch (Exception e) {
			logger.info(aplicacion.getI18nString("SeHanProducidoErrores"));
			ok = false;
		}
		return ok;
	}

	/**
	 * Conexion de numeros de policia con vias.
	 * @return
	 */
	public boolean connectNumPoliciaVias() {
		DistanceLinkingPlugIn distanceLinkingPlugIn = new DistanceLinkingPlugIn();
		Blackboard generalBlackboard = geopistaEditor1.getLayerViewPanel().getBlackboard();
		GeopistaLayer capaNumerosPolicia = (GeopistaLayer) geopistaEditor1
				.getLayerManager().getLayer(GeopistaSystemLayers.NUMEROSPOLICIA);
		GeopistaLayer capaVias = (GeopistaLayer) geopistaEditor1
				.getLayerManager().getLayer(GeopistaSystemLayers.VIAS);

		GeopistaSchema capaNumerosPoliciaSchema = (GeopistaSchema) capaNumerosPolicia
				.getFeatureCollectionWrapper().getFeatureSchema();
		GeopistaSchema capaViasSchema = (GeopistaSchema) capaVias
				.getFeatureCollectionWrapper().getFeatureSchema();
		String idViaCapaNumerosPolicia = capaNumerosPoliciaSchema.getAttributeByColumn("id_via");
		String idViaCapaVias = capaViasSchema.getAttributeByColumn("id");

		generalBlackboard.put(DistanceLinkingPanel.class.getName()+ "_sourceLayer", capaNumerosPolicia);
		generalBlackboard.put(DistanceLinkingPanel.class.getName()+ "_targetLayer", capaVias);
		generalBlackboard.put(DistanceLinkingPanel.class.getName()+ "ATTRIBUTE_ID_ON_SOURCE", idViaCapaNumerosPolicia);
		generalBlackboard.put(DistanceLinkingPanel.class.getName()+ "_expresionFormula", idViaCapaVias.replace(' ', '_'));

		geopistaEditor1.addPlugIn(distanceLinkingPlugIn);

		GeopistaContext geoContext = new GeopistaContext(geopistaEditor1);
		PlugInContext localContext = geoContext.createPlugInContext();

		FeatureSchema initialFeatureSchema = new FeatureSchema();
		initialFeatureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
		initialFeatureSchema.addAttribute("Message", AttributeType.STRING);
		FeatureCollection initialFeature = new FeatureDataset(initialFeatureSchema);
		FeatureCollection initialErrorFeature = new FeatureDataset(initialFeatureSchema);
		FeatureCollection initialReportDataset = new FeatureDataset(initialFeatureSchema);
		
		HashMap arrowFeatureRelation = new HashMap();
		String SELECTEDSOURCELAYER = "com.geopista.ui.dialogs.DistanceLinkingPanel_sourceLayer";
		DistanceLinkingPanel.putToBlackboard(SELECTEDSOURCELAYER,
				"Números de policía", localContext);
		String SELECTEDTARGETLAYER = "com.geopista.ui.dialogs.DistanceLinkingPanel_targetLayer";
		DistanceLinkingPanel.putToBlackboard(SELECTEDTARGETLAYER, "Calles",
				localContext);
		DistanceLinkingPanel.resetAll(localContext, blackboard);
		TaskMonitor monitor = null;
		boolean isGraphic = false;
		DistanceLinkingPanel.connect(capaNumerosPolicia, capaVias,
				localContext, initialFeatureSchema, initialErrorFeature,
				initialReportDataset, monitor, arrowFeatureRelation,
				blackboard, isGraphic);
		
		//TODO: Sustituir la comprobación por la posibilidad de deshacer.

		String ATTRIBUTE_ID_ON_SOURCE = "com.geopista.ui.dialogs.DistanceLinkingPanelATTRIBUTE_ID_ON_SOURCE";
		//String ATTRIBUTE_ID_ON_TARGET = "com.geopista.ui.dialogs.DistanceLinkingPanelATTRIBUTE_ID_ON_TARGET";
		FeatureExpressionPanel featureExpressionPanel = new FeatureExpressionPanel();
		String attName = "ID de vía";
		DistanceLinkingPanel.putToBlackboard(ATTRIBUTE_ID_ON_SOURCE, attName,localContext);
		featureExpressionPanel.setLabelText(attName + "=");

		logger.info(aplicacion.getI18nString("TransfiriendoDatosEnlazados"));
		String attributeIDonSource = "ID de vía";
		if (attributeIDonSource == null)
			return false;

		featureExpressionPanel.setText("ID_de_vía");
		/*if (featureExpressionPanel.getExpParser().getErrorInfo()!=null)
		{
			logger.info(aplicacion.getI18nString("HayErrorEnEexpresión"));
			return false;
		}*/
		try {
			DistanceLinkingPanel.set((Layer) capaNumerosPolicia, "ID_de_vía",
					monitor, featureExpressionPanel, attributeIDonSource,
					localContext, null, isGraphic);
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Método para realizar las asociaciones entre capas
	 * @return boolean
	 * 				true si se han grabado correctamente, false en caso contrario
	 * 
	 */
	public boolean asociar() {
		boolean ok = false;
		try {
			if (con == null) {
				con = aplicacion.getConnection();
				con.setAutoCommit(false);
			}
		} catch (SQLException e) {
			logger.info(aplicacion.getI18nString("NoConexionBaseDatos"));
			return false;
		}

		boolean firingEvents = false;
		boolean isGraphic = false;
		logger.info("Preparando las asociaciones");
		geopistaEditor1 = (GeopistaEditor) blackboard.get("geopistaEditorEnlazarPoliciaCalles");
		//changed by Lilian
		viasLayer = (GeopistaLayer) geopistaEditor1.getLayerManager().getLayer(GeopistaSystemLayers.VIAS);
		
//		viasLayer = (GeopistaLayer) geopistaEditor1.getLayerManager().getLayer(
//				GeopistaSystemLayers.VIAS);
		GeopistaSchema schemaViasLayer = (GeopistaSchema) viasLayer
				.getFeatureCollectionWrapper().getFeatureSchema();
		nombreCatastro = schemaViasLayer.getAttributeByColumn("nombrecatastro");
		String codCatastro = schemaViasLayer
				.getAttributeByColumn("codigocatastro");
		String tipovia = schemaViasLayer
				.getAttributeByColumn("tipovianormalizadocatastro");
		//We need to create a new object as the list is cleared 
		// but we need to keep this information in memory (for successive importations)
		// [Lilian Mével]
		listViasINEInsertadas = new ArrayList((ArrayList) blackboard.get("viasINEInsertadas"));
		GeopistaAsociarCatastroINEViasPanel.prepararAsociaciones(blackboard,
				aplicacion, viasLayer, listViasINEInsertadas, firingEvents,
				listViaIneModel, listCatastroModel, isGraphic, CargadorUtil.folderMunicipio);
		//Guarda datos de vias no asociadas en un fichero
		writeToFile(listViaIneModel, listCatastroModel, nombreCatastro,
				tipovia, codCatastro, CargadorUtil.folderMunicipio);

		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				aplicacion.getMainFrame(), null);
		int idMunicipio = Integer.parseInt(aplicacion
				.getString(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID));
		
		logger.info(aplicacion.getI18nString("RealizadoAsociaciones"));
		try {
			
			GeopistaAsociarCatastroINEViasPanel.makeAllAssociations(blackboard,
					aplicacion, viasLayer, listEnlazadosModel, isGraphic);
			GeopistaAsociarCatastroINEViasPanel.makeViasIneAssociations(
					listViasINEInsertadas, idMunicipio, con);
			GeopistaAsociarCatastroINEViasPanel.makeViasCatastroAssociations(
					progressDialog, viasLayer, aplicacion, isGraphic);
			
			ok = true;
			//con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error al realizar las asociaciones");
			ok = false;
		}/**finally{
			
			try{
				// delete Streets that don't have geometry
				GeopistaAsociarCatastroINEViasPanel.deleteStreetsWithoutGeometry(idMunicipio, con);
				//con.commit();
			}catch (Exception ex){
				ex.printStackTrace();
			}
		
		}*/
		
		/*if (ok)
			geopistaEditor1 = null;*/
		
		return ok;
	}

	/**
	 * Escribimos en el fichero de estado.
	 * @param listViaIneModel
	 * @param listCatastroModel
	 * @param nombreCatastro
	 * @param tipovia
	 * @param codCatastro
	 * @param folderMunicipio
	 */
	public static void writeToFile(DefaultListModel listViaIneModel,
			DefaultListModel listCatastroModel, String nombreCatastro,
			String tipovia, String codCatastro, String folderMunicipio) {
		//Guarda datos de vias no asociadas en un fichero
		
		String targetDirectory=CargadorUtil.folderResultadoBase + File.separator + CargadorUtil.folderMunicipio+File.separator+"Resultado";
		String viasINE_NoAsociadas="viasINE_noasociadas.txt";
		String ViasCatastro_noasociadas="ViasCatastro_noasociadas.txt";
		
		
		//**********************************************
		//Vias INE NO ASOCIADAS
		//**********************************************
		try {
			File dir = new File(targetDirectory);
			dir.mkdirs();
			File fileINE = new File(targetDirectory+File.separator+viasINE_NoAsociadas);
			if (!fileINE.exists())
				fileINE.createNewFile();

			FileWriter ficheroINE = new FileWriter(targetDirectory+File.separator+viasINE_NoAsociadas);
			PrintWriter pw = new PrintWriter(ficheroINE);

			for (int i = 0; i < listViaIneModel.size(); i++) {
				if (((DatosViasINE) listViaIneModel.elementAt(i)).getIdMunicipio() == null)
					pw.print(folderMunicipio + "|");
				else
					pw.print(((DatosViasINE) listViaIneModel.elementAt(i)).getIdMunicipio()
							+ "|");
				
				pw.print(((DatosViasINE) listViaIneModel.elementAt(i)).getCodigoViaINE()+ "|");
				pw.print(((DatosViasINE) listViaIneModel.elementAt(i)).getTipoVia()	+ "|");
				pw.print(((DatosViasINE) listViaIneModel.elementAt(i)).getNombreCorto()	+ "|");
				pw.println(((DatosViasINE) listViaIneModel.elementAt(i)).getNombreVia());
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			File dir = new File(targetDirectory);
			dir.mkdirs();
			File fileCat = new File(targetDirectory+File.separator+ViasCatastro_noasociadas);
			if (!fileCat.exists())
				fileCat.createNewFile();

			FileWriter ficheroCat = new FileWriter(targetDirectory+File.separator+ViasCatastro_noasociadas);
			PrintWriter pw = new PrintWriter(ficheroCat);
			pw.print("IdMunicipioINE|");
			pw.print("CodigoCatastro|");
			pw.print("TipoViaCatastro|");
			pw.println("NombreViaCatastro");
			for (int i = 0; i < listCatastroModel.size(); i++) {
				pw.print(folderMunicipio + "|");
				pw.print(((GeopistaFeature) listCatastroModel.elementAt(i))
						.getString(codCatastro)+ "|");
				pw.print(((GeopistaFeature) listCatastroModel.elementAt(i))
						.getString(tipovia)	+ "|");
				pw.println(((GeopistaFeature) listCatastroModel.elementAt(i))
						.getString(nombreCatastro));
			}
			pw.close();
		} catch (Exception e) {
			logger.info("Error al generar los ficheros de Resultados.");
			e.printStackTrace();
		}
	}



	
	public static void main(String args[]) {
		logger.info("Comenzando la carga de callejero");
		boolean ok = new CargarCallejero().start(args);
		if (!ok)
			logger.info("La importación de callejero ha sido cancelada.");		
		else
			logger.info("La importación del callejero ha terminado.");
		
		System.exit(0);
	}
}