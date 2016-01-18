/**
 * MobileExtractPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.mobile;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.security.acl.AclNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.prefs.Preferences;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.deegree.graphics.sld.UserStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.global.ServletConstants;
import com.geopista.global.WebAppConstants;
import com.geopista.io.datasource.GeopistaStandarReaderWriteFileDataSource.GeoGML;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaLayerManager;
import com.geopista.model.GeopistaMap;
import com.geopista.model.IGeopistaLayer;
import com.geopista.protocol.control.ISesion;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.geopista.style.sld.ui.impl.Texture;
import com.geopista.ui.dialogs.beans.ExtractionProject;
import com.geopista.ui.dialogs.beans.LayerInfo;
import com.geopista.ui.dialogs.beans.eiel.EIELLayerBean;
import com.geopista.ui.dialogs.domains.DomainsUtils;
import com.geopista.ui.dialogs.eiel.EIELDBAccessUtils;
import com.geopista.ui.dialogs.eiel.EIELFilesUtils;
import com.geopista.ui.dialogs.eiel.EIELMetadataSVG;
import com.geopista.ui.dialogs.eiel.EIELSvgToBeans;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.Utils;
import com.geopista.ui.dialogs.licencias.LicenseDBAccessUtils;
import com.geopista.ui.dialogs.licencias.LicenseFilesUtils;
import com.geopista.ui.dialogs.licencias.LicenseMetadataSVG;
import com.geopista.ui.dialogs.licencias.LicenseSvgToBeans;
import com.geopista.ui.dialogs.mobile.MobileExtractPanel01;
import com.geopista.ui.dialogs.mobile.MobileExtractPanel02;
import com.geopista.ui.dialogs.mobile.MobileExtractPanel03;
import com.geopista.ui.dialogs.mobile.MobilePluginI18NResource;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaAbstractSaveMapPlugIn;
import com.geopista.ui.plugin.mobile.util.MobileUtils;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.config.UserPreferenceStore;
import com.geostaf.ui.plugin.generate.GraticuleCreatorEngine;
import com.geostaf.ui.plugin.generate.GraticuleCreatorPlugIn;
import com.ice.tar.TarEntry;
import com.ice.tar.TarGzOutputStream;
import com.localgis.mobile.svg.beans.Coordinates;
import com.localgis.mobile.svg.beans.FeatureGML;
import com.localgis.mobile.svg.gml.GMLConversor;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.java2xml.Java2XML;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.Layerable;
import com.vividsolutions.jump.workbench.model.WMSLayer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.IViewport;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/**
 * Plugin de extracción de mapas en rejilla para dispositivos móviles
 * 
 * @author irodriguez
 * 
 */
public class MobileExtractPlugin extends GeopistaAbstractSaveMapPlugIn {
	private static final String WMS_FILE_NAME = "wms.properties";

	private static final String PROJECT_FILE_NAME = "project.prj";

	private static final String THUMB_FILE_NAME = "thumb.png";

	private static final String _SCH_EXT = ".sch";

	private static final String _SLD_EXT = ".sld";

	private static final String _ZIP_EXT = ".zip";

	private static final String _SVG_EXT = ".svg";

	private static final String _GML_EXT = ".gml";

	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(MobileExtractPlugin.class);

	private Blackboard blackboard = Constants.APLICACION.getBlackboard();

	public static final String PluginMobileExtracti18n = "PluginMobileExtracti18n";

	// conversor de GML a SVG Tiny
	private GMLConversor gmlConv;
	// listado de ficheros a desconectar
	private List<File> ficherosAZipear;
	// private List<File> ficherosSVGCuadriculas;
	// private List<File> ficherosComunes;

	private Map<String, List<String>> tablaCapasEditablesAtributos;

	private Map<String, List<String>> tablaCapasSoloLecturaAtributos;

	private boolean hasWMSLayers;

	private static final String ICON_LIB_PREFIX = "iconlib/";

	private static final String GEOPISTA_PACKAGE = "/com/geopista/app";

	HashMap<Integer, FeatureGML> graticuleCoordinates = new HashMap<Integer, FeatureGML>();

	public static final String SELECTED_LAYER = "SELECTED_LAYER";

	private ArrayList<GeopistaFeature> disconnectedFeatures = null;
	private ArrayList<String> extractLayersNames = null;
	
	/**
	 * Inicialización del plugin para cargarlo desde el fichero
	 * workbench.properties
	 */
	public void initialize(PlugInContext context) throws Exception {
		// para internacionalización
		Locale currentLocale = I18N.getLocaleAsObject();
		ResourceBundle bundle = ResourceBundle
				.getBundle(
						"com.geopista.ui.plugin.mobile.language.PluginMobileExtracti18n",
						currentLocale);
		I18N.plugInsResourceBundle.put(PluginMobileExtracti18n, bundle);

		FeatureInstaller featureInstaller = new FeatureInstaller(
				context.getWorkbenchContext());

		EnableCheckFactory checkFactory = context.getCheckFactory();
		featureInstaller
				.addMainMenuItem(
						this,
						new String[] {
								"Tools",
								MobilePluginI18NResource.GEOPISTAConfiguration_proyectMovil },
						I18N.get(
								PluginMobileExtracti18n,
								MobilePluginI18NResource.MobileExtractPlugin_nuevo),
						false,
						null,
						new MultiEnableCheck()
								.add(checkFactory
										.createWindowWithLayerNamePanelMustBeActiveCheck())
								.add(checkFactory.createAdminUserCheck()));
		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar("Movilidad").addPlugIn(this.getIcon(),
				this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());		
	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
				.add(checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck());
	}
	public ImageIcon getIcon() {
		return IconLoader.icon("/com/geopista/ui/images/movilidad_crear.png");
	}

	public String getName() {
		return I18N.get(PluginMobileExtracti18n,
				MobilePluginI18NResource.MobileExtractPlugin_nuevo);
	}

	public boolean execute(PlugInContext context) throws Exception {
		// context.getActiveInternalFrame().setSize(640, 480);
		reportNothingToUndoYet(context);

		// limpiamos lo que haya podido quedar de ejecuciones anteriores
		emptyBlackboard();

		WizardDialog d = new WizardDialog(GeopistaUtil.getFrame(context
				.getWorkbenchGuiComponent()),
				Constants.APLICACION.getI18nString("ExtractDialog"),
				context.getErrorHandler());

		LayerManager layerManager = context.getLayerManager();

		// si tenemos una capa WMS añadiremos una ventana de selección en el
		// wizard
		Vector orderLayers = layerManager.getOrderLayers();
		Layerable layerable = null;
		hasWMSLayers = false;
		for (int i = 0; i < orderLayers.size() && hasWMSLayers == false; i++) {
			layerable = (Layerable) orderLayers.get(i);
			if (layerable instanceof WMSLayer && layerable.isVisible() == true) {
				hasWMSLayers = true;
			}
		}
		if (hasWMSLayers) {
			d.init(new WizardPanel[] {
					new MobileExtractPanel01("MobileExtractPanel01",
							"MobileExtractPanel02", context),
					new MobileExtractPanel02("MobileExtractPanel02",
							"MobileExtractPanel03", context),
					new MobileExtractPanel03("MobileExtractPanel03", null,
							context) });
		} else {
			d.init(new WizardPanel[] {
					new MobileExtractPanel01("MobileExtractPanel01",
							"MobileExtractPanel03", context),
					new MobileExtractPanel03("MobileExtractPanel03", null,
							context) });
		}

		// Set size after #init, because #init calls #pack.
		d.setSize(520, 650);
		d.setLocation(10, 20);
		GUIUtil.centreOnWindow(d);
		d.setVisible(true);

		if (!d.wasFinishPressed()) {

			Layer graticuleLayer = layerManager.getLayer(GraticuleCreatorEngine
					.getGraticuleName());
			// si existe una cuadrícula la borramos
			if (graticuleLayer != null
					&& layerManager.indexOf(graticuleLayer) != -1) {
				layerManager.remove(graticuleLayer);
			}
			return false;
		}

		return true;

	}

	/**
	 * Borra lo que haya utilizado del blackboard en sus anteriores ejecuciones
	 */
	private void emptyBlackboard() {
		blackboard.remove(MobileExtractPanel01.MOBILE_WRITEABLE_LAYERS);
		blackboard.remove(MobileExtractPanel01.MOBILE_READABLE_LAYERS);
		blackboard.remove(MobileExtractPanel01.SELECTED_LAYER);
		blackboard.remove(MobileExtractPanel02.MOBILE_WMS_LAYERS);
		blackboard.remove(MobileExtractPanel03.MOBILE_WRITEABLE_LAYERS_ATRIB);
		blackboard.remove(MobileExtractPanel03.MOBILE_READABLE_LAYERS_ATRIB);
		blackboard.remove("MOBILE_PROJECT_NAME");
	}

	/*
	 * Se ejecuta una vez finalizado el interfaz (non-Javadoc)
	 * 
	 * @see com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn#run(com.
	 * vividsolutions.jump.task.TaskMonitor,
	 * com.vividsolutions.jump.workbench.plugin.PlugInContext)
	 */
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		// obtenemos la lista de atributos seleccionados de cada capa
		tablaCapasEditablesAtributos = (Map<String, List<String>>) blackboard
				.get(MobileExtractPanel03.MOBILE_WRITEABLE_LAYERS_ATRIB);
		tablaCapasSoloLecturaAtributos = (Map<String, List<String>>) blackboard
				.get(MobileExtractPanel03.MOBILE_READABLE_LAYERS_ATRIB);

		String dirBase = UserPreferenceStore.getUserPreference(
				UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY,
				UserPreferenceConstants.DEFAULT_DATA_PATH, true);
		dirBase += File.separator + "maps";
		String idProyecto = String.valueOf(System.currentTimeMillis());
		GeopistaMap geoMap = (GeopistaMap) context.getTask();

		String projectName = (String) blackboard.get("MOBILE_PROJECT_NAME");
		// Reemplazamos el punto dentro del nombre del proyecto por el caracter
		// "_"
		if (projectName != null) {
			projectName = projectName.replaceAll("\\.", "_");
		}
		String geoMapName = geoMap.getName();
		if (geoMapName != null) {
			geoMapName = geoMapName.replaceAll("\\.", "_");
		}

		int idEntidadTemp = 1;
		try {
			idEntidadTemp = Integer.parseInt(((ISesion) AppContext
					.getApplicationContext().getBlackboard()
					.get(UserPreferenceConstants.SESION_KEY)).getIdEntidad());
		} catch (Exception e) {
		}

		int idEntidad = geoMap.getIdEntidad();
		if (idEntidad == -1) {
			idEntidad = idEntidadTemp;
		}

		String mapName = null;
		if ((projectName == null) || (projectName.trim().equals("")))
			mapName = geoMapName + "." + idEntidad;
		else
			mapName = projectName + "_" + geoMapName + "." + idEntidad;

		String dirMapName = mapName + "." + idProyecto;
		File dirBaseMake = new File(dirBase, dirMapName);

		final String sUrlPrefix = Constants.APLICACION.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL);

		reportNothingToUndoYet(context);
		ILayerViewPanel layerViewPanel = (ILayerViewPanel) context.getLayerViewPanel();
		// ficherosSVGCuadriculas = new ArrayList<File>();
		// ficherosComunes = new ArrayList<File>();
		ficherosAZipear = new ArrayList<File>();
		layerViewPanel.getSelectionManager().clear();

		// ***************************************
		// comprobamos si el directorio ya existe
		// C:\LOCALGIS\Datos o similar en Linux
		// ***************************************
		if (dirBaseMake.exists()) {
			String string1 = Constants.APLICACION
					.getI18nString("ExtractMapPlugin.sobreescribir");
			String string2 = Constants.APLICACION
					.getI18nString("ExtractMapPlugin.cancelar");
			Object[] options = { string1, string2 };

			int n = JOptionPane.showOptionDialog(Constants.APLICACION
					.getMainFrame(), Constants.APLICACION
					.getI18nString("ExtractMapPlugin.mensaje.yaexiste"), "",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, options, options[1]);

			// sobreescribir
			if (n == JOptionPane.YES_OPTION) {
				MobileUtils.deleteDir(dirBaseMake);
			}
			// cancelar (lanza excepcion para deshacer el bloqueo de layers)
			else {
				return;
			}
		}

		// ***************************************
		// creamos el directorio
		// ***************************************
		dirBaseMake.mkdirs();

		/** CREACIÓN DE THUMBNAIL **/
		monitor.report(I18N.get(PluginMobileExtracti18n,
				MobilePluginI18NResource.MobileExtractPlugin_creaThumb));
		Image thumbnail = null;
		try {
			thumbnail = GeopistaUtil.printMap(layerViewPanel.getWidth() / 4,
					layerViewPanel.getHeight() / 4, layerViewPanel);
			File thumbnailFile = new File(dirBaseMake, THUMB_FILE_NAME);
			ImageIO.write((BufferedImage) thumbnail, "png", thumbnailFile);
			ficherosAZipear.add(thumbnailFile);
		} catch (Exception e) {
			logger.error("localSave(PlugInContext, int, int, TaskMonitor)", e);
		}

		// *******************************
		// obtenemos las cuadrículas
		// *******************************
		String layerGraticuleName = GraticuleCreatorEngine.getGraticuleName();
		LayerManager layerManager = context.getLayerManager();
		Layer graticuleLayer = layerManager.getLayer(layerGraticuleName);
		IViewport viewport = layerViewPanel.getViewport();
		// obtenemos solamente las celdas que están dentro del área de visionado
		List<Feature> graticuleFeatures = graticuleLayer
				.getFeatureCollectionWrapper().query(
						viewport.getEnvelopeInModelCoordinates());
		Feature feature = null;
		Map<Layer, HashSet<Feature>> layerFeatures = null;
		Layer layer = null; // layer de la cuadrícula actual
		HashSet<Feature> features = null; // features de la layer
		GeopistaLayer auxLayer = null; // copia para modificar de la layer
		GeopistaSchema geopistaSchema = null;
		int geometryIndex = 0;
		int layerGeometryType = 0;
		FeatureDataset featDataSetCol = null;
		String layerFileName = null;
		// clave: id_cuadricula , valor: lista de capas
		HashMap<Integer, List<LayerInfo>> graticuleFileNameMap = new HashMap<Integer, List<LayerInfo>>();
		List<LayerInfo> listLayersGraticule = null;
		Integer graticuleFileNumber = null;

		try {

			// iteramos sobre cada cuadrícula
			/*
			 * |_25|_26|_27|_28| 
			 * |_21|_22|_23|_24|
			 * |_17|_18|_19|_20| 
			 * |_13|_14|_15|_16| 
			 * |_9_|_10|_11|_12|
			 * |_5_|_6_|_7_|_8_| 
			 * |_1_|_2_|_3_|_4_|
			 */
			/** GENERACIÓN DE GMLs **/
			monitor.report(I18N.get(PluginMobileExtracti18n,
					MobilePluginI18NResource.MobileExtractPlugin_generaGML));
			for (int i = 0; i < graticuleFeatures.size(); i++) {
				feature = graticuleFeatures.get(i);
				// obtenemos las features de cada capa que se encuentran dentro
				// de la celda
				layerFeatures = layerViewPanel
						.visibleLayerToFeaturesInFenceMap(feature.getGeometry());

				graticuleFileNumber = (Integer) feature
						.getAttribute(GraticuleCreatorEngine.ATR_CELL_ID);

				// coordenadas del viewbox
				Coordinate[] coordinates = feature.getGeometry()
						.getCoordinates();
				List<Coordinates> gmlCoordinates = new ArrayList<Coordinates>();
				for (int j = 0; j < coordinates.length; j++) {
					gmlCoordinates.add(new Coordinates(coordinates[j].x,
							coordinates[j].y));
				}
				FeatureGML featureGML = new FeatureGML(gmlCoordinates);
				graticuleCoordinates.put(graticuleFileNumber, featureGML);

				// llamamos al plugin de extracción únicamente con las features
				// que están dentro de la cuadrícula
				Set<Layer> keySet = layerFeatures.keySet();
				// completamos las capas que no tengan features
				completaCapasSinFeatures(layerFeatures, layerManager);
				int j = 0;

				//NUEVO
				disconnectedFeatures = new ArrayList<GeopistaFeature>();
				//FIN NUEVO
				
				// iteramos sobre las capas dentro de la cuadrícula
				for (Iterator iterator = keySet.iterator(); iterator.hasNext(); j++) {
					layer = (Layer) iterator.next();
					features = layerFeatures.get(layer);
					if (!layer.getName().equals(layerGraticuleName) /*
																	 * &&
																	 * !features
																	 * .
																	 * isEmpty()
																	 */) {

						// solo exportamos las capas que hayamos previamente
						// seleccionado bien modificables o bien de solo lectura
						if (tablaCapasEditablesAtributos.containsKey(layer
								.getName())
								|| tablaCapasSoloLecturaAtributos
										.containsKey(layer.getName())) {
							auxLayer = new GeopistaLayer();
							geopistaSchema = (GeopistaSchema) layer
									.getFeatureCollectionWrapper()
									.getFeatureSchema();
							featDataSetCol = new FeatureDataset(geopistaSchema);
							// --- inicio calcula la geometria de la capa
							geometryIndex = geopistaSchema.getGeometryIndex();
							layerGeometryType = geopistaSchema
									.getColumnByAttribute(geometryIndex)
									.getTable().getGeometryType();
							// --- fin de calcula geometria de la capa
							featDataSetCol.addAll(features);
							auxLayer.setFeatureCollection(featDataSetCol);
							layerFileName = layer.getName() + "_"
									+ graticuleFileNumber;
							auxLayer.setName(layerFileName);
							// guardamos las capas que se van a exportar
							if (graticuleFileNameMap
									.containsKey(graticuleFileNumber)) {
								listLayersGraticule = graticuleFileNameMap
										.get(graticuleFileNumber);
								listLayersGraticule.add(new LayerInfo(layer
										.getName(), ((GeopistaLayer) layer)
										.getSystemId(), layerGeometryType));
							} else {
								listLayersGraticule = new ArrayList<LayerInfo>();
								listLayersGraticule.add(new LayerInfo(layer
										.getName(), ((GeopistaLayer) layer)
										.getSystemId(), layerGeometryType));
								graticuleFileNameMap.put(graticuleFileNumber,
										listLayersGraticule);
							}
							// ((GeopistaLayer)layer).getSystemId()

							// creamos el fichero GML para cada capa de cada
							// cuadrícula
							createGMLFile(auxLayer, dirBaseMake, context,
									monitor);
							//NUEVO			
							disconnectedFeatures.addAll(featDataSetCol.getFeatures());
							//FIN NUEVO
						}					
					}
				}
			}

			
			// **************
			// GML CUADRÍCULA
			// **************
			// modificación para ajustar el origen de coordenadas la cuadrícula
			translateCoordOrigin(graticuleLayer.getFeatureCollectionWrapper(),
					(Integer) blackboard
							.get(GraticuleCreatorPlugIn.LAYER_WIDTH_IN_CELLS));
			createGMLFile(graticuleLayer, dirBaseMake, context, monitor);

			/** GENERACIÓN DE SVGs **/
			// recorremos las cuadrículas exportadas para transformar a SVG los
			// GML creados
			monitor.report(I18N.get(PluginMobileExtracti18n,
					MobilePluginI18NResource.MobileExtractPlugin_convSVG));
			Set<Integer> keySetGraticuleFile = graticuleFileNameMap.keySet();
			for (Iterator graticuleFileIterator = keySetGraticuleFile
					.iterator(); graticuleFileIterator.hasNext();) {
				graticuleFileNumber = (Integer) graticuleFileIterator.next();
				listLayersGraticule = graticuleFileNameMap
						.get(graticuleFileNumber);
				// convertimos cada uno de los GML de cada capa de cada
				// cuadrícula a SVGTiny
				generateLayerGraticuleSVGTiny(listLayersGraticule, dirBaseMake,
						graticuleFileNumber, mapName);
			}

			/** GENERACIÓN DE SLDs y SCHs **/
			monitor.report(I18N.get(PluginMobileExtracti18n,
					MobilePluginI18NResource.MobileExtractPlugin_generaEstilo));
			List<GeopistaLayer> writeableLayers = (List<GeopistaLayer>) blackboard
					.get(MobileExtractPanel01.MOBILE_WRITEABLE_LAYERS);
			List<GeopistaLayer> readableLayers = (List<GeopistaLayer>) blackboard
					.get(MobileExtractPanel01.MOBILE_READABLE_LAYERS);
			List<IGeopistaLayer> allExtractLayers = new ArrayList<IGeopistaLayer>();
			allExtractLayers.addAll(writeableLayers);
			allExtractLayers.addAll(readableLayers);
			extraerSLDySCH(dirBaseMake, allExtractLayers);

			
			/** EXTRACCIÓN DE LAS IMÁGENES EN EL ESTILO SLD **/
			// obtenemos los archivos de imágenes utilizados en el SLD de las
			// capas desconectadas
			for (int j = 0; j < allExtractLayers.size(); j++) {
				getSourceFilesFromSLD(allExtractLayers.get(j));
			}
			
			//NUEVO
			extractLayersNames = Utils.getExtractLayers(allExtractLayers);
			//FIN NUEVO

			/** CREACIÓN DE CUADRICULA SVG **/
			monitor.report(I18N
					.get(PluginMobileExtracti18n,
							MobilePluginI18NResource.MobileExtractPlugin_creaCuadricula));
			gmlConv = new GMLConversor();
			File gmlFile = new File(dirBaseMake, layerGraticuleName + _GML_EXT);
			gmlConv.addLayer(gmlFile, layerGraticuleName, layerGraticuleName,
					null, false, false, 5);
			File svgGraticuleFile = new File(dirBaseMake, layerGraticuleName
					+ _SVG_EXT);
			String strSVG = gmlConv.transformToSVG();
			FileOutputStream fOut = new FileOutputStream(svgGraticuleFile);
			fOut.write(strSVG.getBytes(GMLConversor.CHAR_ENCODING_UTF_8)); // codificamos
																			// el
																			// fichero
			fOut.flush();
			fOut.close();
			addStyleGraticuleSVG(svgGraticuleFile); // añadimos estilo a la
													// cuadrícula
			ficherosAZipear.add(svgGraticuleFile);

			// Desconectamos la informacion especifica de las aplicaciones
			desconectarAplicacionesEspecificas(new LicenseSvgToBeans(),
				extractLayersNames, layerGraticuleName, dirBaseMake, monitor);
		
			// ----NUEVO---->			
			desconectarAplicacionesEspecificasEIEL(new EIELSvgToBeans(),
				extractLayersNames, layerGraticuleName, dirBaseMake, monitor);
			// --FIN NUEVO-->

			// ******************************************************************
			// CREACIÓN DEL FICHERO DE CAPAS WMS
			// ******************************************************************
			if (hasWMSLayers) {
				File wmsPropFile = new File(dirBaseMake, WMS_FILE_NAME);
				ArrayList<WMSLayer> extractWmsLayers = (ArrayList<WMSLayer>) blackboard
						.get(MobileExtractPanel02.MOBILE_WMS_LAYERS);
				if (extractWmsLayers != null && extractWmsLayers.size() > 0) {
					createWmsPropFile(wmsPropFile, extractWmsLayers);
					ficherosAZipear.add(wmsPropFile);
				}
			}

			/** SALVAMOS EL MAPA EN BBDD **/
			// borramos la cuadrícula
			if (graticuleLayer != null
					&& layerManager.indexOf(graticuleLayer) != -1) {
				layerManager.remove(graticuleLayer);
			}
			AdministradorCartografiaClient admClient = new AdministradorCartografiaClient(
					sUrlPrefix + WebAppConstants.GEOPISTA_WEBAPP_NAME + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME);
			//CAMBIAR : Salvado del mapa previo a la desconexion
			//saveMapDataBase(context, admClient);

			// ******************************************************************
			/** CREACIÓN DEL PRJ **/
			// ******************************************************************
			monitor.report(I18N.get(PluginMobileExtracti18n,
					MobilePluginI18NResource.MobileExtractPlugin_creaFichProy));
			// proyecto de extracción
			ExtractionProject eProject = new ExtractionProject();
			eProject.setAltoCeldas((Double) blackboard
					.get(GraticuleCreatorPlugIn.CELL_SIDE_LENGTH_Y));
			eProject.setAnchoCeldas((Double) blackboard
					.get(GraticuleCreatorPlugIn.CELL_SIDE_LENGTH_X));
			eProject.setCeldasX((Integer) blackboard
					.get(GraticuleCreatorPlugIn.LAYER_WIDTH_IN_CELLS));
			eProject.setCeldasY((Integer) blackboard
					.get(GraticuleCreatorPlugIn.LAYER_HEIGHT_IN_CELLS));
			eProject.setFechaExtraccion(new Date());
			eProject.setIdProyecto(idProyecto);
			eProject.setNombreProyecto(mapName);
			Coordinate coordCorner = (Coordinate) blackboard
					.get(GraticuleCreatorPlugIn.SOUTHWEST_CORNER_OF_LEFT_LAYER);
			eProject.setPosEsquinaX(coordCorner.x);
			eProject.setPosEsquinaY(coordCorner.y);
			eProject.setIdMapa(geoMap.getSystemId());
			eProject.setSrid(geoMap.getMapCoordinateSystem().getEPSGCode());
			List<Integer> idExtractLayersList = new ArrayList<Integer>();
			for (int i = 0; i < allExtractLayers.size(); i++) {
				idExtractLayersList.add(allExtractLayers.get(i)
						.getId_LayerDataBase());
			}
			eProject.setIdExtractLayersList(idExtractLayersList);
			
			//eProject.setIdMunicipio("33001");

			if (Utils.isInArray(extractLayersNames.toArray(),Constants.TIPOS_LICENCIAS)
					|| Utils.isInArray(extractLayersNames.toArray(),Constants.TIPOS_INVENTARIO) || Utils.isInArray(extractLayersNames.toArray(),Constants.TIPOS_EIEL)) {
				String bloqueMetaInfo = "";
				bloqueMetaInfo += "<appsconfig>\n";
				Iterator it = extractLayersNames.iterator();
				while(it.hasNext()){
					String layerName = (String) it.next();
					bloqueMetaInfo += "<" + layerName
							+ "_meta enabled=\"true\"/>\n";
				}				
				bloqueMetaInfo += "</appsconfig>\n";
				eProject.setBloqueMetaInfo(bloqueMetaInfo);
			}

			File prjFile = new File(dirBaseMake, PROJECT_FILE_NAME);
			createInfoProjectFile(prjFile, layerGraticuleName + _SVG_EXT,
					MobileAssignCellsPlugin.PERMISOS_CELDAS_PROP, eProject);

			// ******************************************************************
			/** COMPRESIÓN DE FICHEROS **/
			// ******************************************************************

			monitor.report(I18N.get(PluginMobileExtracti18n,
					MobilePluginI18NResource.MobileExtractPlugin_comprime));
			String ficheroTarGZip = dirMapName + _ZIP_EXT;
			// tarGZipFiles(ficherosAZipear, dirBaseMake + File.separator +
			// ficheroTarGZip);
			trueZipFiles(ficherosAZipear, dirBaseMake + File.separator
					+ ficheroTarGZip);

			// ******************************************************************
			/** SUBIDA HTTP **/
			// ******************************************************************
			monitor.report(I18N.get(PluginMobileExtracti18n,
					MobilePluginI18NResource.MobileExtractPlugin_subida));
			List<File> listaRutasFicherosUpload = new ArrayList<File>();
			listaRutasFicherosUpload.add(new File(dirBaseMake, ficheroTarGZip));
			List<String> paramNames = new ArrayList<String>();
			List<String> paramValues = new ArrayList<String>();
			paramNames.add(GeopistaUtil.HTTP_FILE_TYPE_HEADER);
			paramValues.add(GeopistaUtil.HTTP_ZIP_HEADER);
			GeopistaUtil.httpUploadFiles(Constants.URL_SERVER,
					listaRutasFicherosUpload, paramNames, paramValues);

			// ******************************************************************
			/** GUARDAMOS EL PROYECTO DE EXTRACCIÓN EN BBDD **/
			// ******************************************************************

			monitor.report(I18N.get(PluginMobileExtracti18n,
					MobilePluginI18NResource.MobileExtractPlugin_salvaDatos));
			// escritura en bbdd
			admClient.createExtractProject(eProject);
		} catch (Exception e) {
			logger.error(
					"Se ha producido un error en la extraccion movil del mapa. "
							+ e, e);
			MobileUtils.deleteDir(dirBaseMake);
			throw e;
		} finally {
			// borrado de ficheros temporales
			deleteTempFiles(dirBaseMake);

			// borramos la cuadrícula
			if (graticuleLayer != null
					&& layerManager.indexOf(graticuleLayer) != -1) {
				layerManager.remove(graticuleLayer);
			}
		}
	}

	private double transformXOffset(double x, double minX) {
		double newX = (x - minX);
		return com.localgis.mobile.svg.Utils.redondear(newX, 4);
	}

	private double transformYOffset(double y, double maxY) {
		double newY = (y - maxY);
		if (newY < 0) {
			newY = (-1) * newY;
		}
		return com.localgis.mobile.svg.Utils.redondear(newY, 4);
	}

	/**
	 * Desconectamos la informacion de las aplicaciones especificas.
	 * 
	 * @param svgToBeansUtils
	 * @param monitor
	 * @param allExtractLayers
	 * @param layerGraticuleName
	 * @param dirBaseMake
	 */
	private void desconectarAplicacionesEspecificasEIEL(
			EIELSvgToBeans eielSvgToBeans,
			ArrayList<String> extractLayersNames, String layerGraticuleName,
			File dirBaseMake, TaskMonitor monitor) throws Exception {

		if (eielSvgToBeans.existenEIEL(extractLayersNames)) {
			/** CREACIÓN DE ARCHIVOS DE EIEL **/
			EIELDBAccessUtils dbAccessUtils = new EIELDBAccessUtils();
			String locale = Constants.APLICACION.getString(
					UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES");
			EIELFilesUtils eielFilesUtils = new EIELFilesUtils(extractLayersNames, locale);		
			monitor.report(I18N.get(PluginMobileExtracti18n,
					MobilePluginI18NResource.MobileExtractPlugin_creaEIEL));
			List<File> ficherosSVG = new ArrayList<File>();
			for (Iterator iterator = ficherosAZipear.iterator(); iterator
					.hasNext();) {
				File file = (File) iterator.next();
				if (file.getName().endsWith(".svg")
						&& !file.getName()
								.equals(layerGraticuleName + _SVG_EXT)) {
					ficherosSVG.add(file);
				}
			}

			// tipos de metadatos desconectados
			List<String> capasMetadata = new ArrayList<String>();
			HashMap<String,EIELLayerBean> capasBean = new HashMap<String,EIELLayerBean>();
			
			Iterator it = extractLayersNames.iterator();
			while(it.hasNext()){
				String layerName = (String) it.next();
				if (eielSvgToBeans
						.esEIEL(layerName)) {
					capasMetadata.add(layerName);
					capasBean.put(layerName,eielFilesUtils.getEIELLayerBean(layerName));
				}
			}

			// ******************************************************************
			// tenemos los ficheros de elementos EIEL tras parsear las cuadriculas
			// SVG
			// ******************************************************************
			Map<String, List<EIELMetadataSVG>> parseEIEL = eielSvgToBeans
					.parseEIEL(ficherosSVG);
			HashMap<File, String> hashSvgCellsHeader = eielSvgToBeans.getSvgCellsSkeleton(ficherosSVG);
			// creación de ficheros EIEL
			List<File> eielFiles = eielFilesUtils.createEIELFilesSkeleton(
					ficherosSVG, capasMetadata, hashSvgCellsHeader,
					parseEIEL);

			if(disconnectedFeatures.size()>0){
				String urlEIEL = Constants.APLICACION
				.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)
				+ WebAppConstants.GEOPISTA_WEBAPP_NAME + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME + ServletConstants.EIEL_SERVLET_NAME;
				it = capasMetadata.iterator();
				while(it.hasNext()){
					String layerName = (String) it.next();
						dbAccessUtils.addDataBaseInfoEIEL(parseEIEL, urlEIEL, locale,
								layerName,disconnectedFeatures, capasBean);		
				}
				
			}
			// ******************************************************************
			// relleno de ficheros de elementos EIEL
			// ******************************************************************
			 eielFilesUtils.fillEIELFiles(parseEIEL, dirBaseMake);

			// ******************************************************************
			// creación de ficheros SCH de dominios para elementos EIEL
			// ******************************************************************
			it = capasMetadata.iterator();
			while(it.hasNext()){
				String layerName = (String) it.next();			
				File schEIELFile = new File(dirBaseMake,
						layerName + "_meta.sch");
				createEIELSCHDomainFile(layerName,
						schEIELFile, locale, eielFilesUtils);
				if (schEIELFile.exists()) {
					ficherosAZipear.add(schEIELFile);
				}			
			}
			
			ficherosAZipear.addAll(eielFiles);
		}
	}

	/**
	 * Desconectamos la informacion de las aplicaciones especificas.
	 * 
	 * @param licenseSvgToBeans
	 * @param monitor
	 * @param extractLayersNames
	 * @param layerGraticuleName
	 * @param dirBaseMake
	 */
	private void desconectarAplicacionesEspecificas(
			LicenseSvgToBeans licenseSvgToBeans,
			ArrayList<String> extractLayersNames, String layerGraticuleName,
			File dirBaseMake, TaskMonitor monitor) throws Exception {

		if (licenseSvgToBeans.existenLicencias(extractLayersNames)
				|| licenseSvgToBeans.existenInvPatrimonio(extractLayersNames)
				|| licenseSvgToBeans
						.existenTiposActividadesContaminantes(extractLayersNames)) {
			/** CREACIÓN DE ARCHIVOS DE LICENCIAS E INVENTARIO **/
			LicenseDBAccessUtils dbAccessUtils = new LicenseDBAccessUtils();
			String locale = Constants.APLICACION.getString(
					UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES");
			LicenseFilesUtils licenseFilesUtils = new LicenseFilesUtils(locale);		
			monitor.report(I18N.get(PluginMobileExtracti18n,
					MobilePluginI18NResource.MobileExtractPlugin_creaLicencias));
			List<File> ficherosSVG = new ArrayList<File>();
			for (Iterator iterator = ficherosAZipear.iterator(); iterator
					.hasNext();) {
				File file = (File) iterator.next();
				if (file.getName().endsWith(".svg")
						&& !file.getName()
								.equals(layerGraticuleName + _SVG_EXT)) {
					ficherosSVG.add(file);
				}
			}

			// tipos de metadatos desconectados
			boolean licObraMayor = false;
			boolean licObraMenor = false;
			boolean licActividad = false;
			boolean invParcelas = false;
			boolean invVias = false;
			boolean actividadesContaminantes = false;
			boolean arboleda = false;
			boolean vertedero = false;

			List<String> capasMetadata = new ArrayList<String>();
			if (licenseSvgToBeans.existenLicObraMayor(extractLayersNames)) {
				licObraMayor = true;
				capasMetadata.add(Constants.LIC_OBRA_MAYOR);
			}
			if (licenseSvgToBeans.existenLicObraMenor(extractLayersNames)) {
				licObraMenor = true;
				capasMetadata.add(Constants.LIC_OBRA_MENOR);
			}
			if (licenseSvgToBeans.existenLicActividad(extractLayersNames)) {
				licActividad = true;
				capasMetadata.add(Constants.LIC_ACTIVIDAD);
			}
			if (licenseSvgToBeans.existenInvParcelas(extractLayersNames)) {
				invParcelas = true;
				capasMetadata.add(Constants.INV_PARCELAS);
			}
			if (licenseSvgToBeans.existenInvVias(extractLayersNames)) {
				invVias = true;
				capasMetadata.add(Constants.INV_VIAS);
			}

			// Informacion sobre actividades contaminantes.
			if (licenseSvgToBeans
					.existenActividadesContaminantes(extractLayersNames)) {
				actividadesContaminantes = true;
				capasMetadata.add(Constants.ACTIVIDADES_CONTAMINANTES);
			}
			if (licenseSvgToBeans.existenArboledas(extractLayersNames)) {
				arboleda = true;
				capasMetadata.add(Constants.ARBOLEDA);
			}
			if (licenseSvgToBeans.existenVertederos(extractLayersNames)) {
				vertedero = true;
				capasMetadata.add(Constants.VERTEDERO);
			}

			// ******************************************************************
			// tenemos los ficheros de licencias tras parsear las cuadriculas
			// SVG
			// ******************************************************************
			int numFichLic = Integer.parseInt(Constants.APLICACION
					.getString(UserPreferenceConstants.NUMERO_FICHEROS_LICENCIAS_XML));
			Map<String, List<LicenseMetadataSVG>> parseLicencias = licenseSvgToBeans
					.parseLicencias(ficherosSVG, numFichLic);
			HashMap<File, String> hashSvgCellsHeader = licenseSvgToBeans
					.getSvgCellsSkeleton(ficherosSVG);
			// creación de ficheros de licencias
			List<File> licensesFiles = licenseFilesUtils
					.createLicensesFilesSkeleton(ficherosSVG, numFichLic,
							capasMetadata, hashSvgCellsHeader, parseLicencias);

			// Creacion de ficheros de licencias
			if (licenseSvgToBeans
					.existenLicencias(extractLayersNames)) {
				String urlLicencias = Constants.APLICACION
						.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)
						+ "licencias";
				dbAccessUtils.addDataBaseInfoLicenses(parseLicencias,
						urlLicencias, locale);
			}
			// Creacion de ficheros de contaminantes
			if (licenseSvgToBeans
					.existenTiposActividadesContaminantes(extractLayersNames)) {
				dbAccessUtils.addDataBaseInfoContaminantes(parseLicencias,
						locale);
			}

			// creación de ficheros de inventario
			if (licenseSvgToBeans.existenInvPatrimonio(extractLayersNames)) {
				String urlInventario = Constants.APLICACION
						.getString(UserPreferenceConstants.LOCALGIS_SERVER_URL)
						+ WebAppConstants.GEOPISTA_WEBAPP_NAME + ServletConstants.ADMINISTRADOR_CARTOGRAFIA_SERVLET_NAME + ServletConstants.INVENTARIO_SERVLET_NAME;
				dbAccessUtils.addDataBaseInfoInventary(parseLicencias,
						urlInventario, locale);
			}

			// ******************************************************************
			// relleno de ficheros de licencias e inventario
			// ******************************************************************
			licenseFilesUtils.fillLicenseFiles(parseLicencias, dirBaseMake);

			// ******************************************************************
			// creación de ficheros SCH de dominios para licencias
			// ******************************************************************
			if (licObraMayor) {
				File schLicObraMayorFile = new File(dirBaseMake,
						Constants.LIC_OBRA_MAYOR + "_meta.sch");
				createLicenseSCHDomainFile(Constants.LIC_OBRA_MAYOR,
						schLicObraMayorFile, locale, licenseFilesUtils);
				if (schLicObraMayorFile.exists()) {
					ficherosAZipear.add(schLicObraMayorFile);
				}
			}
			if (licObraMenor) {
				File schLicObraMenorFile = new File(dirBaseMake,
						Constants.LIC_OBRA_MENOR + "_meta.sch");
				createLicenseSCHDomainFile(Constants.LIC_OBRA_MENOR,
						schLicObraMenorFile, locale, licenseFilesUtils);
				if (schLicObraMenorFile.exists()) {
					ficherosAZipear.add(schLicObraMenorFile);
				}
			}
			if (licActividad) {
				File schLicActividadFile = new File(dirBaseMake,
						Constants.LIC_ACTIVIDAD + "_meta.sch");
				createLicenseSCHDomainFile(Constants.LIC_ACTIVIDAD,
						schLicActividadFile, locale, licenseFilesUtils);
				if (schLicActividadFile.exists()) {
					ficherosAZipear.add(schLicActividadFile);
				}
			}
			if (invParcelas) {
				File schInvParcelasFile = new File(dirBaseMake,
						Constants.INV_PARCELAS + "_meta.sch");
				createLicenseSCHDomainFile(Constants.INV_PARCELAS,
						schInvParcelasFile, locale, licenseFilesUtils);
				if (schInvParcelasFile.exists()) {
					ficherosAZipear.add(schInvParcelasFile);
				}
			}
			if (invVias) {
				File schInvViasFile = new File(dirBaseMake, Constants.INV_VIAS
						+ "_meta.sch");
				createLicenseSCHDomainFile(Constants.INV_VIAS, schInvViasFile,
						locale, licenseFilesUtils);
				if (schInvViasFile.exists()) {
					ficherosAZipear.add(schInvViasFile);
				}
			}

			ficherosAZipear.addAll(licensesFiles);
		}
	}

	/**
	 * Método para añadir las capas que no se tengan features a la colección de
	 * manera que posteriormente se creen sus ficheros GML y de este modo
	 * también los SVG para poder insertar en la PDA features de capas en
	 * cuadrículas que no las tengan
	 * 
	 * @param layerFeatures
	 * @param layerManager
	 */
	private void completaCapasSinFeatures(
			Map<Layer, HashSet<Feature>> layerFeatures,
			LayerManager layerManager) {
		Object[] layers = layerFeatures.keySet().toArray();
		List layerListManag = layerManager.getLayers();

		Set<String> keySet = tablaCapasEditablesAtributos.keySet();
		Layer curLayer = null;
		String layerName = null;
		Layer managLayer = null;
		boolean findLayer = false;
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			layerName = (String) iterator.next();
			for (int i = 0; i < layers.length && !findLayer; i++) {
				curLayer = (Layer) layers[i];
				if (layerName.equals(curLayer.getName())) {
					findLayer = true;
				}
			}
			if (!findLayer) {
				for (int j = 0; j < layerListManag.size() && !findLayer; j++) {
					managLayer = (Layer) layerListManag.get(j);
					if (layerName.equals(managLayer.getName())) {
						layerFeatures.put(managLayer, new HashSet<Feature>());
						findLayer = true;
					}
				}
			}
			findLayer = false;
		}

		keySet = tablaCapasSoloLecturaAtributos.keySet();
		curLayer = null;
		layerName = null;
		managLayer = null;
		findLayer = false;
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			layerName = (String) iterator.next();
			for (int i = 0; i < layers.length && !findLayer; i++) {
				curLayer = (Layer) layers[i];
				if (layerName.equals(curLayer.getName())) {
					findLayer = true;
				}
			}
			if (!findLayer) {
				for (int j = 0; j < layerListManag.size() && !findLayer; j++) {
					managLayer = (Layer) layerListManag.get(j);
					if (layerName.equals(managLayer.getName())) {
						layerFeatures.put(managLayer, new HashSet<Feature>());
						findLayer = true;
					}
				}
			}
			findLayer = false;
		}

	}

	/**
	 * Translada las coordenadas al origen y decrementa su valor para eliminar
	 * la limitación de la librería TinyLine referente a que un mapa sólo puede
	 * tener 32767m de amplitud entre coordenadas. De este modo podrá tener 32km
	 * por cada una de las cuadrículas que lo formen
	 * 
	 * @param featureCollectionWrapper
	 */
	private void translateCoordOrigin(
			FeatureCollectionWrapper featureCollectionWrapper, int numColums) {
		List fList = featureCollectionWrapper.getFeatures();
		Feature f = (Feature) fList.get(0);
		double fAlto = f.getGeometry().getEnvelopeInternal().getHeight();
		double fAncho = f.getGeometry().getEnvelopeInternal().getWidth();
		double P = fAlto / fAncho;
		GeometryFactory geoFact = new GeometryFactory();
		Integer cellId = null;
		int col = 0;
		int fil = 0;
		int K = 1000;
		for (Iterator iterator = fList.iterator(); iterator.hasNext();) {
			f = (Feature) iterator.next();
			cellId = (Integer) f
					.getAttribute(GraticuleCreatorEngine.ATR_CELL_ID);
			cellId--;
			col = cellId % numColums;
			fil = cellId / numColums;

			// creamos una nueva geometría equivalente para la celda
			Coordinate[] coordArray = { new Coordinate(col * K, fil * K * P),
					new Coordinate((col + 1) * K, fil * K * P),
					new Coordinate((col + 1) * K, (fil + 1) * K * P),
					new Coordinate(col * K, (fil + 1) * K * P),
					new Coordinate(col * K, fil * K * P) };

			// sustituimos la celda por la nueva creada
			f.setGeometry(geoFact.createPolygon(
					geoFact.createLinearRing(coordArray), null));
		}
	}

	/**
	 * Crea el archivo sch de dominios para los atributos de licencias de obra
	 * 
	 * @param licObraMayor
	 * @param dirBaseMake
	 * @param locale
	 * @param licenseFilesUtils
	 * @return
	 * @throws Exception
	 */
	private File createLicenseSCHDomainFile(String licObra, File licObraSch,
			String locale, LicenseFilesUtils licenseFilesUtils)
			throws Exception {
		InputStream licObraSk = licenseFilesUtils.locateSkeletonInput(licObra);
		DomainsUtils domainUtils = new DomainsUtils();
		domainUtils.generateSCHFiles(licObraSk, locale, licObraSch, Constants.LICENCIAS);
		licObraSk.close();
		return licObraSch;
	}

	private File createEIELSCHDomainFile(String eiel, File eielSch,
			String locale, EIELFilesUtils eielFilesUtils) throws Exception {
		InputStream eielSk = eielFilesUtils.getSkeletonInput(eiel);
		DomainsUtils domainUtils = new DomainsUtils();
		domainUtils.generateSCHFiles(eielSk, locale, eielSch, Constants.EIEL);
		eielSk.close();
		return eielSch;
	}

	/**
	 * Salva el mapa actual en BBDD
	 * 
	 * @param context
	 * @param thumbnail
	 * @param administradorCartografiaClient
	 * @throws ACException
	 * @throws AclNotFoundException
	 */
	private void saveMapDataBase(PlugInContext context,
			AdministradorCartografiaClient administradorCartografiaClient)
			throws ACException, AclNotFoundException {
		if (!Constants.APLICACION.isLogged()) {
			Constants.APLICACION.setProfile("Geopista");
			Constants.APLICACION.login();
		}
		if (Constants.APLICACION.isLogged()) {
			LayerViewPanel layerViewPanel = (LayerViewPanel) context.getWorkbenchContext()
					.getLayerViewPanel();

			GeopistaMap saveActualMap = (GeopistaMap) context.getTask();

			// comprobamos si el mapa es local y se va a grabar en la base de
			// datos y en ese caso
			// borramos el systemId para que el administrador de cartografia
			// sepa que es nuevo
			if (!saveActualMap.isSystemMap()) {
				saveActualMap.setSystemId("");
				saveActualMap.setSystemMap(true);
			}

			int thumbSizeX = Integer.parseInt(Constants.APLICACION
					.getString("thumbSizeX"));
			int thumbSizeY = Integer.parseInt(Constants.APLICACION
					.getString("thumbSizeY"));
			Image thumbnail = GeopistaUtil.printMap(thumbSizeX, thumbSizeY,
					layerViewPanel);
			saveActualMap.setThumbnail(thumbnail);
			administradorCartografiaClient.saveMap(saveActualMap,
					UserPreferenceStore.getUserPreference(
							UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true));

			List allLayers = saveActualMap.getLayerManager().getLayers();
			Iterator allLayersIter = allLayers.iterator();

			if (saveActualMap.getLayerManager() instanceof GeopistaLayerManager) {
				((GeopistaLayerManager) saveActualMap.getLayerManager())
						.setDirty(false);
			}
			while (allLayersIter.hasNext()) {
				GeopistaLayer actualLayer = (GeopistaLayer) allLayersIter
						.next();
				String systemId = actualLayer.getSystemId();
				if (systemId != null) {
					administradorCartografiaClient.uploadStyle(actualLayer);
				}
				actualLayer.setFeatureCollectionModified(false);
				if (actualLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl) {
					SLDStyleImpl sldStyle = (SLDStyleImpl) actualLayer
							.getStyle(SLDStyle.class);
					sldStyle.setPermanentChanged(false);
				}
			}

			updateTitleToNoModified((LayerManager) saveActualMap.getLayerManager(),
					saveActualMap.getTaskComponent());

			return;
		} else {
			return;
		}
	}

	private void updateTitleToNoModified(LayerManager layerManager,
			TaskComponent taskFrame) {

		if (layerManager instanceof GeopistaLayerManager) {
			((GeopistaLayerManager) layerManager).setDirty(false);
			if (taskFrame instanceof JInternalFrame) {
				String newTitle = taskFrame.getTitle();
				if (newTitle.charAt(0) == '*') {
					newTitle = newTitle.substring(1);
				}
				((JInternalFrame) taskFrame).setTitle(newTitle);
			}
		}
	}

	/**
	 * Crea el fichero de properties con la información necesaria para hacer las
	 * consultas WMS desde la PDA
	 * 
	 * @param wmsPropFile
	 * @param extractWmsLayers
	 * @throws IOException
	 */
	private void createWmsPropFile(File wmsPropFile,
			ArrayList<WMSLayer> extractWmsLayers) throws IOException {
		FileOutputStream fOutWms = new FileOutputStream(wmsPropFile);
		Properties propWms = new Properties();
		WMSLayer wmsLayer = null;
		String layerNames = null;
		List layerNamesList = null;
		int numeroCapasWMS = extractWmsLayers.size();
		propWms.put("NumberOfLayers", String.valueOf(numeroCapasWMS));
		for (int i = 0; i < numeroCapasWMS; i++) {
			wmsLayer = extractWmsLayers.get(i);
			propWms.put("WMS.baseURL." + i, wmsLayer.getServerURL());
			propWms.put("WMS.GetMapVersion." + i, wmsLayer.getWmsVersion());
			layerNamesList = wmsLayer.getLayerNames();
			layerNames = "";
			for (int j = 0; j < layerNamesList.size(); j++) {
				layerNames += layerNamesList.get(j);
				if (j < layerNamesList.size() - 1) {
					layerNames += ",";
				}
			}
			propWms.put("WMS.GetMapLayers." + i, layerNames);
			propWms.put("WMS.GetMapFormat." + i, wmsLayer.getFormat());
			propWms.put("WMS.GetMapSRS." + i, wmsLayer.getSRS());
		}
		propWms.store(fOutWms, "Fichero de properties WMS");
		fOutWms.close();
	}

	/**
	 * Borra los ficheros temporales (todos excepto el zip)
	 * 
	 * @param dirBaseMake
	 */
	private void deleteTempFiles(File dirBaseMake) {
		if (dirBaseMake.isDirectory()) {
			File[] listFiles = dirBaseMake.listFiles();
			File curFile = null;
			String fileName = null;
			for (int i = 0; i < listFiles.length; i++) {
				curFile = listFiles[i];
				fileName = curFile.getName();
				// borramos todo lo que no sea el zip
				if (!fileName.substring(fileName.length() - 4,
						fileName.length()).equals(_ZIP_EXT)) {
					curFile.delete();
				}
			}
		}

	}

	/**
	 * Crea el fichero .prj con información relevante sobre el proyecto y los
	 * archivos que contiene
	 * 
	 * @param prjFile
	 * @param string
	 * @param permisosCeldasXml
	 * @param proyect
	 * @throws IOException
	 */
	private void createInfoProjectFile(File prjFile, String graticuleSVG,
			String permisosCeldasXml, ExtractionProject project)
			throws IOException {

		int numFichLic = Integer.parseInt(Constants.APLICACION
				.getString(UserPreferenceConstants.NUMERO_FICHEROS_LICENCIAS_XML));

		StringBuffer strBuff = new StringBuffer();
		strBuff.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		strBuff.append("<Localgis2Me>\n");
		strBuff.append("<gridfile>" + graticuleSVG + "</gridfile>\n");
		strBuff.append("<users2grid>" + permisosCeldasXml + "</users2grid>\n");
		strBuff.append("<srid>" + project.getSrid() + "</srid>\n");
		strBuff.append("<numFichLic>" + numFichLic + "</numFichLic>\n");
		strBuff.append("<detail>\n");
		strBuff.append("<item>\n");
		strBuff.append("<label>Identificador del proyecto</label>\n");
		strBuff.append("<value>" + project.getIdProyecto() + "</value>\n");
		strBuff.append("</item>\n");
		strBuff.append("<item>\n");
		strBuff.append("<label>Fecha de extracción</label>\n");
		strBuff.append("<value>" + project.getFechaExtraccionFormateada()
				+ "</value>\n");
		strBuff.append("</item>\n");
		strBuff.append("<item>\n");
		strBuff.append("<label>Identificador del mapa</label>\n");
		strBuff.append("<value>" + project.getIdMapa() + "</value>\n");
		strBuff.append("</item>\n");
		strBuff.append("<item>\n");
		strBuff.append("<label>Nombre del mapa</label>\n");
		strBuff.append("<value>" + project.getNombreProyecto() + "</value>\n");
		strBuff.append("</item>\n");
//		strBuff.append("<item>\n");
//		strBuff.append("<label>Id Municipio</label>\n");
//		strBuff.append("<value>" + project.getIdMunicipio() + "</value>\n");
//		strBuff.append("</item>\n");
		strBuff.append("</detail>\n");
		String metaInf = project.getBloqueMetaInfo();
		if (metaInf != null) {
			strBuff.append(metaInf);
		}
		strBuff.append("</Localgis2Me>\n");
		FileOutputStream fOut = new FileOutputStream(prjFile);
		fOut.write(strBuff.toString().getBytes("UTF-8"));
		fOut.flush();
		fOut.close();
		ficherosAZipear.add(prjFile);
	}

	/**
	 * Crea los ficheros SLD y SCH correspondientes a cada capa
	 * 
	 * @param context
	 * @param dirBaseMake
	 * @param monitor
	 * @throws Exception
	 */
	private void extraerSLDySCH(File dirBaseMake, List<IGeopistaLayer> layers)
			throws Exception {
		Iterator layersIter = layers.iterator();
		String sldFileStr = null;
		while (layersIter.hasNext()) {
			IGeopistaLayer currentLayer = (IGeopistaLayer) layersIter.next();

			// SCH
			FeatureSchema featureSchema = currentLayer
					.getFeatureCollectionWrapper().getFeatureSchema();
			File saveSchemaLayer = new File(dirBaseMake, currentLayer.getName()
					+ _SCH_EXT);
			if (featureSchema instanceof GeopistaSchema) {
				//NUEVO
//				if(Utils.isInArray(Constants.TIPOS_EIEL,currentLayer.getSystemId())){
//					Iterator it = ((GeopistaSchema) featureSchema).getAttributes().iterator();
//					while(it.hasNext()){
//						Attribute attrib = (Attribute) it.next();		
//						System.out.println(attrib.getColumn().getName());
//						if(Utils.isInArray(Constants.EIEL_ONLY_READ_FIELDS,attrib.getColumn().getName()))
//							((GeopistaSchema) featureSchema).setAttributeAccess(attrib.getName(), GeopistaSchema.READ_ONLY);
//					}	
//				}
				//FIN NUEVO
				StringWriter stringWriterSch = new StringWriter();
				try {
					Java2XML converter = new Java2XML();
					converter.write(featureSchema, "GeopistaSchema",
							stringWriterSch, "UTF-8");

					// parche para mostrar el combo con municipios en la pda
					String str = sustituyeDominioMunicipios(stringWriterSch
							.toString());

					saveSchemaUTF8(str, saveSchemaLayer);
					// saveSchemaUTF8(stringWriterSch, saveSchemaLayer);
					// saveSchema(str, saveSchemaLayer); //para que el japi
					// parser lea el Área
					ficherosAZipear.add(saveSchemaLayer);

				} finally {
					stringWriterSch.flush();
					stringWriterSch.close();
				}
			}

			// SLD
			if (currentLayer.getStyle(SLDStyle.class) instanceof SLDStyleImpl) {
				sldFileStr = dirBaseMake.getAbsolutePath() + File.separator
						+ currentLayer.getName() + _SLD_EXT;
				SLDStyleImpl sldStyle = (SLDStyleImpl) currentLayer
						.getStyle(SLDStyle.class);
				//NUEVO (SOLO DESCONECTA ESTILOS SLD NECESARIOS)
				UserStyle us = sldStyle.getUserStyle(sldStyle.getCurrentStyleName());							
				List userStyles = new ArrayList();
				userStyles.add(us);
				sldStyle.setStyles(userStyles);		
				//FIN NUEVO
				sldStyle.setPermanentChanged(false);
				sldStyle.setSLDFileName(sldFileStr);
				sldStyle.createSLDFile("UTF-8");
				ficherosAZipear.add(new File(sldFileStr));
			}
		}

	}

	/**
	 * Función que elimina el dominio actual del idmunicipio para sustituirlo
	 * por un campo multievaluado
	 * 
	 * @param str
	 * @return
	 */
	private String sustituyeDominioMunicipios(String str) {
		// int ini = str.indexOf("<name>id_municipio</name>"); //buscamos el pto
		// de inicio

		// Nos interesa el atributo que esta dentro del tag column no el primero
		int ini = str.lastIndexOf("<name>id_municipio</name>"); // buscamos el
																// pto de inicio
		if (ini < 0) {
			return str;
		}
		String firstPart = str.substring(0, ini); // desde el inicio hasta
													// id_municipio
		String lastPart = str.substring(ini);
		int fin = lastPart.indexOf("</domain>");
		lastPart = lastPart.substring(fin); // desde el final del dominio hasta
											// el final
		// en este pto nos queda la parte central a añadir
		String middlePart = "<name>id_municipio</name>\n<description>id_municipio</description>"
				+ "\n<domain class=\"com.geopista.feature.TreeDomain\">\n<name>municipio_domain</name>\n<pattern null=\"true\" />"
				+ "\n<description>municipio_domain</description>\n";
		List alMunicipios = AppContext.getAlMunicipios();
		Iterator itMuni = alMunicipios.iterator();
		Municipio municipio = null;
		String childDomain = null;
		while (itMuni.hasNext()) {
			municipio = (Municipio) itMuni.next();
			childDomain = "<child class=\"com.geopista.feature.CodedEntryDomain\">\n<name>"
					+ municipio.getId()
					+ "</name>\n"
					+ "<pattern>"
					+ municipio.getId()
					+ "</pattern>\n<description>"
					+ municipio.getNombreOficial() + "</description>";
			middlePart += childDomain + "\n" + childDomain
					+ "\n</child>\n</child>\n";
		}

		return firstPart + middlePart + lastPart;
	}

	/** FUNCIONES AUXILIARES **/
	/**
	 * Crea un archivo GML con los parámetros pasados
	 * 
	 * @param currentLayer
	 * @param currentLayerIsLocalLayer
	 * @param dirBaseMake
	 * @param context
	 * @param monitor
	 * @param dispMovilExt
	 * @return
	 * @throws Exception
	 */
	public File createGMLFile(Layer currentLayer, File dirBaseMake,
			PlugInContext context, TaskMonitor monitor) throws Exception {
		DataSourceQuery dataSourceQuery = currentLayer.getDataSourceQuery();

		File saveFileLayer = null;
		Connection connection = null;
		try {
			GeoGML newDatasource = new GeoGML();
			dataSourceQuery = new DataSourceQuery();

			saveFileLayer = new File(dirBaseMake, currentLayer.getName()
					+ _GML_EXT);

			HashMap properties = new HashMap();
			properties
					.put(DataSource.FILE_KEY, saveFileLayer.getAbsolutePath());
			properties.put(DataSource.COORDINATE_SYSTEM_KEY, context.getTask()
					.getLayerManager().getCoordinateSystem().getName());

			newDatasource.setProperties(properties);
			dataSourceQuery.setDataSource(newDatasource);
			((GeopistaLayer) currentLayer).setLocal(true);

			connection = dataSourceQuery.getDataSource().getConnection();
			ArrayList list = connection.executeUpdate(dataSourceQuery.getQuery(), currentLayer.getFeatureCollectionWrapper(), monitor);
			Layer layer = currentLayer.setDataSourceQuery(dataSourceQuery)
					.setFeatureCollectionModified(false);
		}catch(Exception ex){			
			System.out.println(ex.getMessage());	
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return saveFileLayer;
	}

	/**
	 * Realiza la transformación de GML a SVG Tiny de cada cuadrícula
	 * 
	 * @param graticuleFileNumber
	 * @param dirBaseMake
	 * @param listLayersGraticule
	 * @param ficheroSVG
	 * @param dirBaseMake
	 * @param mapName
	 * @param tablaCapasAtributos
	 * @param tablaCapasSoloLecturaAtributos
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	private void generateLayerGraticuleSVGTiny(
			List<LayerInfo> listLayersGraticule, File dirBaseMake,
			Integer graticuleFileNumber, String mapName) throws SAXException,
			IOException, ParserConfigurationException {

		LayerInfo layerInfo = null;
		String layerFileName = null;
		File gmlFile = null;
		File svgFile = null;
		List<String> listaAtrbDesconect = null;
		boolean capaEditable = false;

		boolean capaActiva = false;

		gmlConv = new GMLConversor();
		gmlConv.setCoordinatesGraticule(graticuleCoordinates
				.get(graticuleFileNumber));

		LicenseSvgToBeans licUtils = new LicenseSvgToBeans();
		for (int i = 0; i < listLayersGraticule.size(); i++) {
			capaActiva = false;
			layerInfo = listLayersGraticule.get(i);
			layerFileName = layerInfo.getName();
			gmlFile = new File(dirBaseMake, layerFileName + "_"
					+ graticuleFileNumber + _GML_EXT);
			if (gmlFile.exists()) {

				String selectedLayer = null;
				if (blackboard.get(SELECTED_LAYER) != null) {
					selectedLayer = (String) blackboard.get(SELECTED_LAYER);
					if (selectedLayer.equals(layerFileName))
						capaActiva = true;
				}

				// indicamos al conversor si la capa es editable o de sólo
				// lectura
				if (tablaCapasEditablesAtributos.containsKey(layerFileName)) {
					capaEditable = true;
					listaAtrbDesconect = tablaCapasEditablesAtributos
							.get(layerFileName);
				} else if (tablaCapasSoloLecturaAtributos
						.containsKey(layerFileName)) {
					capaEditable = false;
					listaAtrbDesconect = tablaCapasSoloLecturaAtributos
							.get(layerFileName);
				}

				// añadido para licencias
				if (Utils.isInArray(Constants.TIPOS_LICENCIAS,
						layerInfo.getName())) {
					capaEditable = false;
				}

				// si es null creamos uno vacío porque si le pasásemos un null
				// desconectaría TODOS los atributos
				if (listaAtrbDesconect == null) {
					listaAtrbDesconect = new ArrayList<String>();
				}
				gmlConv.addLayer(gmlFile, layerFileName,
						layerInfo.getSystemId(), listaAtrbDesconect,
						capaEditable, capaActiva, layerInfo.getGeometryType());
			}
		}
		// generamos un SVG por cada cuadrícula que agrupe todas features de las
		// capas que interseccionan con ella
		svgFile = new File(dirBaseMake, graticuleFileNumber + _SVG_EXT);
		String strSVG = gmlConv.transformToSVG();
		FileOutputStream fOut = new FileOutputStream(svgFile);
		fOut.write(strSVG.getBytes(GMLConversor.CHAR_ENCODING_UTF_8)); // codificamos
																		// el
																		// fichero
		fOut.flush();
		fOut.close();
		ficherosAZipear.add(svgFile);
	}

	/**
	 * Selecciona los ficheros necesarios para la correcta visualización de la
	 * capa pasada como parámetro
	 * 
	 * @param layer
	 */
	private void getSourceFilesFromSLD(ILayer layer) {
		SLDStyle sldStyle = (SLDStyle) layer.getStyle(SLDStyle.class);
		String rawSLD = sldStyle.getSLD();
		String initMask = "href='file:";
		String endMask = "'/>";

		// para iconos
		Preferences pref = Preferences.userRoot().node(GEOPISTA_PACKAGE);
		String textDirStr = pref.get(Texture.TEXTURES_DIRECTORY_PARAMETER, ".");
		File dir = new File(textDirStr + File.separator + ICON_LIB_PREFIX);
		String sourceHrefIcon = null;

		if (rawSLD.contains(initMask)) {
			int initIndex = -1;
			int endIndex = -1;
			String rawSLDAux = rawSLD;
			String sourceHref = null;
			while ((initIndex = rawSLDAux.indexOf(initMask)) != -1) {
				rawSLDAux = rawSLDAux.substring(initIndex + initMask.length(),
						rawSLDAux.length());
				endIndex = rawSLDAux.indexOf(endMask);
				sourceHref = rawSLDAux.substring(0, endIndex);

				// caso especial para iconos
				if (rawSLDAux.toLowerCase().contains(ICON_LIB_PREFIX)) {
					if (!dir.exists()) {
						continue;
					}
					sourceHrefIcon = sourceHref.substring(
							ICON_LIB_PREFIX.length(), sourceHref.length());
					ficherosAZipear.add(new File(dir, sourceHrefIcon));
				} else {
					ficherosAZipear.add(new File(sourceHref)); // metemos el
																// fichero
																// externo que
																// necesitaremos
																// en el disp
																// movil
				}
			}
		}
	}

	/**
	 * Comprime los ficheros necesarios para la visualización del SVG en el
	 * dispositivo movil en formato TAR + GZ
	 * 
	 * @param listaNombresFicheros
	 * @param ficheroSVG
	 * @param ficheroGZip
	 */
	private void tarGZipFiles(List<File> listaFicheros, String ficheroTarGZip) {
		byte[] buffer = new byte[18024];
		// especificamos el nombre del fichero zip
		File tarGZipFile = new File(ficheroTarGZip);
		File currentFile = null;
		String currentGZipEntry = null;
		try {
			TarGzOutputStream out = new TarGzOutputStream(new FileOutputStream(
					tarGZipFile));
			// añadimos cada fichero al zip
			for (int i = 0; i < listaFicheros.size(); i++) {
				currentFile = listaFicheros.get(i);
				FileInputStream in = new FileInputStream(currentFile);
				currentGZipEntry = currentFile.getName();
				out.putNextEntry(new TarEntry(currentGZipEntry));
				// transferimos los bytes al fichero
				int len;
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Comprime los ficheros necesarios para la visualización del SVG en el
	 * dispositivo movil
	 * 
	 * @param listaNombresFicheros
	 * @param ficheroSVG
	 * @param ficheroZip
	 */
	private void zipFiles(List<File> listaFicheros, String ficheroZip) {
		byte[] buffer = new byte[18024];
		// especificamos el nombre del fichero zip
		File zipFile = new File(ficheroZip);
		File currentFile = null;
		String currentZipEntry = null;
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipFile));
			// ajustamos el ratio de compresión
			out.setLevel(Deflater.DEFAULT_COMPRESSION);
			// añadimos cada fichero al zip
			for (int i = 0; i < listaFicheros.size(); i++) {
				currentFile = listaFicheros.get(i);
				FileInputStream in = new FileInputStream(currentFile);
				currentZipEntry = currentFile.getName();
				out.putNextEntry(new ZipEntry(currentZipEntry));
				// transferimos los bytes al fichero
				int len;
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Compresión de ficheros que pueden estar acentuados ya que java tiene un
	 * bug con los mismos
	 * 
	 * @param listaFicheros
	 * @param ficheroZip
	 * @throws IOException
	 */
	private void trueZipFiles(List<File> listaFicheros, String ficheroZip)
			throws IOException {

		try {
			de.schlichtherle.io.File zipFile = null;

			File fAux = null;
			for (int i = 0; i < listaFicheros.size(); i++) {
				fAux = listaFicheros.get(i);
				// añadimos nuevas entradas al fichero zip
				zipFile = new de.schlichtherle.io.File(ficheroZip
						+ File.separator
						+ URLEncoder.encode(fAux.getName(), "UTF-8"));
				zipFile.archiveCopyFrom(fAux);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error(e, e);
		} finally {
			// desmontamos el sistema virtual de ficheros
			de.schlichtherle.io.File.umount();
		}

		// //método viejo
		// byte[] buffer = new byte[18024];
		// String currentZipEntry = null;
		// de.schlichtherle.io.File currentFile = null;
		// de.schlichtherle.io.FileInputStream in = null;
		// de.schlichtherle.io.FileOutputStream out = null;
		// try {
		// //añadimos cada fichero al zip
		// for (int i = 0; i < listaFicheros.size(); i++) {
		// currentFile = new de.schlichtherle.io.File(listaFicheros.get(i));
		// in = new de.schlichtherle.io.FileInputStream(currentFile);
		// currentZipEntry = currentFile.getName();
		// out = new
		// de.schlichtherle.io.FileOutputStream(ficheroZip+"/"+currentZipEntry);
		// //transferimos los bytes al fichero
		// int len;
		// while ((len = in.read(buffer)) > 0){
		// out.write(buffer, 0, len);
		// }
		// in.close();
		// out.close();
		// }
		// }
		// catch (IllegalArgumentException iae) {
		// iae.printStackTrace();
		// }
		// catch (FileNotFoundException fnfe) {
		// fnfe.printStackTrace();
		// }
		// catch (IOException ioe)
		// {
		// ioe.printStackTrace();
		// }
	}

	/**
	 * Añade estilo y una imagen de fondo al fichero de cuadrícula SVG
	 * 
	 * @param fGraticuleSvg
	 */
	private void addStyleGraticuleSVG(File fGraticuleSvg) {
		FileOutputStream fOutStr = null;
		try {
			// parseamos el documento para obtener su representación del árbol
			// DOM
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// obtenemos una instancia del documento
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(fGraticuleSvg);
			Element docEle = dom.getDocumentElement();
			// leemos el viewbox
			String viewboxAtr = docEle.getAttribute("viewBox");
			String[] splitViewbox = viewboxAtr.split(" ");
			// añadimos atributos a la cuadrícula
			NodeList nlAtrG = docEle.getElementsByTagName("g");
			Element elemGraticule = (Element) nlAtrG.item(0);
			elemGraticule.setAttribute("stroke", "blue");
			elemGraticule.setAttribute("stroke-width", "5");
			elemGraticule.setAttribute("fill", "none");
			// añadimos la imagen de fondo
			Element elemImage = dom.createElement("image");
			elemImage.setAttribute("x", splitViewbox[0]);
			elemImage.setAttribute("y", splitViewbox[1]);
			elemImage.setAttribute("width", splitViewbox[2]);
			elemImage.setAttribute("height", splitViewbox[3]);
			elemImage.setAttribute("xlink:href", THUMB_FILE_NAME);
			elemGraticule
					.insertBefore(elemImage, elemGraticule.getFirstChild()); // ojo,
																				// al
																				// principio
			// sacamos a fichero
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			fOutStr = new FileOutputStream(fGraticuleSvg);
			StreamResult result = new StreamResult(fOutStr);
			DOMSource source = new DOMSource(dom);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fOutStr.close();
			} catch (IOException e) {
			}
		}
	}

}
