package com.geopista.ui.plugin.inventario.edit.compare.geometries;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.InventarioInternalFrame;
import com.geopista.app.inventario.MainInventario;
import com.geopista.app.inventario.panel.BienesJPanel;
import com.geopista.app.inventario.panel.BienesPreAltaJPanel;
import com.geopista.app.inventario.panel.InventarioJPanel;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.global.ServletConstants;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.geopista.ui.plugin.edit.InventarioManagerPlugIn;
import com.geopista.ui.plugin.inventario.edit.compare.geometries.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;

import fr.michaelm.jump.query.QueryPlugIn;

public class CompareGeometriesPlugIn extends ThreadedBasePlugIn {

	private String toolBarCategory = "GeopistaNumerosPoliciaPlugIn.category";
	public static final String SUPERFICIE = "GEOMETRY";
	private ApplicationContext appContext = AppContext.getApplicationContext();
	private CompareGeometriesDialog compareGeometriesDialog;
	public static final String CompareGeometriesI18N = "CompareGeometriesI18N";
	private InventarioClient inventarioClient = null;
	private static AppContext aplicacion = (AppContext) AppContext
			.getApplicationContext();

	private static final Log logger = LogFactory
			.getLog(CompareGeometriesPlugIn.class);

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {

		GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
				.add(checkFactory
						.createWindowWithLayerManagerMustBeActiveCheck())
				.add(checkFactory
						.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
				.add(checkFactory
						.createAtLeastLayerWithColumnReferenciaCatastralBeExits());

	}

	public boolean execute(PlugInContext context) throws Exception {

		Collection totalLayers = new ArrayList();
		Collection layers = context.getLayerManager().getLayers();
		Iterator layersIterator = layers.iterator();

		GeopistaLayer currentLayer = null;
		// Buscamos la layer
		while (layersIterator.hasNext()) {
			Layer layer = (Layer) layersIterator.next();
			if (layer.getName().equalsIgnoreCase("Inventario de Parcelas")) {
				currentLayer = (GeopistaLayer) layer;
			}
		}

		compareGeometriesDialog = new CompareGeometriesDialog(
				appContext.getMainFrame(), true);
		compareGeometriesDialog.setVisible(true);

		if (compareGeometriesDialog.wasOkPressed()) {

			BienesJPanel bienesPanel = getInventarioFrame()
					.getInventarioJPanel().getBienesPanel();

			// Lista de Bienes visibles en el panel
			List<BienBean> bienes = (List<BienBean>) bienesPanel
					.getListaBienes();

			if (bienes==null || bienes.size() == 0) {
				JOptionPane.showMessageDialog(aplicacion.getMainFrame(),
						"No ha seleccionado ningun bien");
				return false;
			}

			if (!(bienes.get(0) instanceof InmuebleBean)) {
				JOptionPane
						.showMessageDialog(aplicacion.getMainFrame(),
								"Los bienes seleciconados no tienes superficies que comparar");
				return false;
			}

			double tol = Double.parseDouble(compareGeometriesDialog
					.getToleranciaJTextField().getText().replaceAll(",", "."));

			// Lista de features de la capa
			List<GeopistaFeature> features = currentLayer
					.getFeatureCollectionWrapper().getFeatures();

			ArrayList<InmuebleBean> bienesFiltrados = new ArrayList<InmuebleBean>();
			ArrayList<Double> featuresFiltrados = new ArrayList<Double>();
			GeopistaFeature f = null;
			double total;

			for (int i = 0; i < bienes.size(); i++) {
				if (bienes.get(i).getIdFeatures().length > 0) {
					bienesFiltrados.add((InmuebleBean) bienes.get(i));
					// Obtenemos la feature con el mismo id y calcular sus
					// geometrias
					try {
						total = 0;
						for (int j = 0; j < bienes.get(i).getIdFeatures().length; j++) {
							f = obtenerFeature(features, (String) bienes.get(i)
									.getIdFeatures()[j]);
							total += f.getGeometry().getArea();
							featuresFiltrados.add(total);
						}
					} catch (Exception e) {
						logger.error("No se ha podido obtener las features del bien: "
								+ bienes.get(i).getNumInventario());
						e.printStackTrace();
					}

				}
			}

			logger.info("Tenemos " + bienesFiltrados.size() + " bienes y "
					+ featuresFiltrados.size() + " features");
			CompareJPanel compareJPanel = new CompareJPanel(
					aplicacion.getMainFrame(), tol, bienesFiltrados,
					featuresFiltrados, aplicacion.getI18NResource());

			boolean datos = compareJPanel.rellenaTabla();
			if (datos) {
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				compareJPanel.setLocation(d.width / 7, d.height / 7);
				compareJPanel.setVisible(true);
			} else {
				JOptionPane
						.showMessageDialog(aplicacion.getMainFrame(),
								"En este momento no existen bienes con features asociadas");
				return false;
			}

			return true;
		}

		return false;
	}

	private GeopistaFeature obtenerFeature(List<GeopistaFeature> features,
			String id) throws Exception {
		try {
			GeopistaFeature feat = null;
			for (int i = 0; i < features.size(); i++) {
				if (features.get(i).getSystemId().equalsIgnoreCase(id)) {
					feat = features.get(i);
					break;
				}
			}
			return feat;
		} catch (Exception e) {
			logger.error("Se ha producido un error al obtener la feature");
			throw new Exception(
					"Se ha producido un error al obtener la feature: "
							+ e.getMessage());
		}

	}

	public void initialize(PlugInContext context) throws Exception {
		try {
			Locale currentLocale = I18N.getLocaleAsObject();

			inventarioClient = new InventarioClient(
					aplicacion
							.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)
							+ ServletConstants.INVENTARIO_SERVLET_NAME);

			ResourceBundle bundle2 = ResourceBundle
					.getBundle(
							"com.geopista.ui.plugin.inventario.edit.compare.geometries.language.CompareGeometriesPlugIni18n",
							currentLocale);

			I18N.plugInsResourceBundle.put(CompareGeometriesI18N, bundle2);

			String pluginCategory = appContext.getString(toolBarCategory);
			((WorkbenchGuiComponent) context.getWorkbenchContext()
					.getIWorkbench().getGuiComponent()).getToolBar(
					pluginCategory).addPlugIn(getIcon(), this,
					createEnableCheck(context.getWorkbenchContext()),
					context.getWorkbenchContext());

			context.getFeatureInstaller()
					.addMainMenuItem(
							this,
							new String[] {
									"Tools",
									AppContext.getApplicationContext()
											.getI18nString("SimpleQuery") },
							I18N.get(CompareGeometriesI18N,
									"CompareGeometriesPlugIn.SearchCompareGeometries"),
							false,
							null,
							QueryPlugIn.createEnableCheck(context
									.getWorkbenchContext()));

		} catch (Exception e) {
			logger.error("Ha fallado la inicializacion del PlugInCOmpareGeometries");
			e.printStackTrace();
		}
	}

	public String getName() {
		return I18N.get(CompareGeometriesI18N, "CompareGeometriesPlugIn.name");
	}

	/**
	 * @return the referenciaCatastralDialog
	 */
	private CompareGeometriesDialog getCompareGeometriesDialog() {
		return compareGeometriesDialog;
	}

	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {

	}

	public ImageIcon getIcon() {
		return IconLoader.icon("compareGeometries.gif");
	}

	private InventarioInternalFrame getInventarioFrame() {
		try {
			return ((InventarioInternalFrame) ((MainInventario) AppContext
					.getApplicationContext().getMainFrame()).getIFrame());
		} catch (Exception ex) {
			return null;
		}
	}
}
