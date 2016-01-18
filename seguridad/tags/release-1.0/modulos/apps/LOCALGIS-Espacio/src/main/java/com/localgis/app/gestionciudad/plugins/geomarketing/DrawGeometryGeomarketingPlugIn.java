package com.localgis.app.gestionciudad.plugins.geomarketing;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaLayer;
import com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.GeoMarketingDataDialog;
import com.localgis.app.gestionciudad.plugins.geomarketing.images.IconLoader;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.DrawFenceToolGeoMarketing;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.GeoMarketingUtils;
import com.localgis.app.gestionciudad.plugins.geomarketing.webserviceclient.GeoMarketingWSWrapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.cursortool.DummyTool;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;

public class DrawGeometryGeomarketingPlugIn extends ThreadedBasePlugIn {

	private static Geometry jtsGeometry = null;
	private boolean drawGeometryGeomarketingButtonAdded = false;
	private static PlugInContext pluginContext = null;
	private static GeopistaLayer selectedLayer = null;
	private static DrawFenceToolGeoMarketing cursorTool = null;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private static int[] ageRangeList = {10,20,30,40,50,60};


	public boolean execute(PlugInContext context) throws Exception {
		//TODO implementar algo en execute???
		// obtener la geometria de pintar....
		pluginContext = context;

		if (context.getLayerViewPanel() == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.layerviewpanel"));
			return false;
		}

		if (context.getLayerNamePanel() == null || context.getLayerNamePanel().getSelectedLayers() == null || 
				context.getLayerNamePanel().getSelectedLayers().length == 0){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.nolayersselecteds"));
			return false;
		}
		if (context.getLayerNamePanel()!=null && context.getLayerNamePanel().getSelectedLayers()!= null
				&& context.getLayerNamePanel().getSelectedLayers().length > 0){
			selectedLayer = (GeopistaLayer) context.getLayerNamePanel().getSelectedLayers()[0];
		} else{
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.nolayersselecteds"));
			return false;
		}

//		if (selectedLayer!=null && selectedLayer.isLocal()){
//			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.locallayerselected"));
//			return false;
//		}
		
		/*if (selectedLayer!=null && selectedLayer.getFeatureCollectionWrapper().getFeatures().size() > GeoMarketingConstat.NUM_MAX_FEATURES){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.maxnumfeatures"));
			return false;
		}*/

		this.taskFrame = (TaskComponent) context.getActiveTaskComponent();

		cursorTool = new DrawFenceToolGeoMarketing(context, true);
		cursorTool.activate(context.getLayerViewPanel());	 
		context.getLayerViewPanel().setCurrentCursorTool(cursorTool);

		return false;
	}


	@Override
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
	}


	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("geomarketing","localgis.geomarketing.plugins.draw.icon"));
	}

	@SuppressWarnings("static-access")
	public void initialize(PlugInContext context) throws Exception {
		//		Locale loc=I18N.getLocaleAsObject();    
		//		ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.ui.plugin.routeenginetools.maxspeedplugin.language.RouteEngine_MaxSpeedi18n",loc,this.getClass().getClassLoader());
		//		I18N.plugInsResourceBundle.put("maxspeedplugin",bundle);
		GeoMarketingUtils.inicializarIdiomaGeoMarketing();

		FeatureInstaller featureInstaller = new FeatureInstaller(context
				.getWorkbenchContext());

		featureInstaller.addMainMenuItem(this, new String[] { 
				this.aplicacion.getI18nString("ui.MenuNames.TOOLS"),
				I18N.get("geomarketing","localgis.geomarketing.plugins.menu.title")
		}, I18N.get("geomarketing","localgis.geomarketing.plugins.draw.tittle"), false, null,
		createEnableCheck(context.getWorkbenchContext()));

		((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(this.aplicacion.getI18nString("ui.MenuNames.TOOLS.ROUTEENGINEOPERATIONS")).addPlugIn(this.getIcon(),
				this,
				createEnableCheck(context.getWorkbenchContext()),
				context.getWorkbenchContext());

	}

	public static MultiEnableCheck createEnableCheck(
			WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(
				workbenchContext);
		return new MultiEnableCheck()
		.add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck())
		.add(checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck())
		//		.add(checkFactory.createTaskWindowMustBeActiveCheck())
		//		.add(checkFactory.createBlackBoardMustBeElementsCheck())
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1))
		//		.add(checkFactory.createExactlyNFeaturesMustBeSelectedCheck(1))
		;
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!drawGeometryGeomarketingButtonAdded )
		{
			toolbox.addToolBar();
			DrawGeometryGeomarketingPlugIn explode = new DrawGeometryGeomarketingPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			drawGeometryGeomarketingButtonAdded = true;
		}
	}

	private static ZoomToSelectedItemsPlugIn zoomToSelectedItemsPlugIn =
		new ZoomToSelectedItemsPlugIn();
	private static TaskComponent taskFrame;
	public static void flashSelectedGeometries(Geometry ge) throws NoninvertibleTransformException {
		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(ge);
		zoomToSelectedItemsPlugIn.flash(
				geometries,
				(LayerViewPanel)taskFrame.getLayerViewPanel());
	}


	public static void onDrawGeometryFinish(){
		
		jtsGeometry = cursorTool.getPolygonGeometry();

		if (jtsGeometry != null){
			
			// if the geometry is not valid (maybe cause it intersetcs ists self)
			// obteain convexhull to made the calcs
//			if (!jtsGeometry.isValid()){
//				ArrayList<Geometry> geometries = new ArrayList<Geometry>();
//				geometries.add(jtsGeometry);
////				jtsGeometry = jtsGeometry.getInteriorPoint();
//				jtsGeometry = new GeometryFactory().createGeometryCollection((Geometry[]) geometries.toArray(new Geometry[] {})).convexHull();			
//			}
			
			// The geometry is not null
			// Try to put SRID if it's equals to 0
			if (jtsGeometry.getSRID() == 0){
				CoordinateSystem coodSys = pluginContext.getLayerManager().getCoordinateSystem();
				if (coodSys != null){
					jtsGeometry.setSRID(coodSys.getEPSGCode());
				}
			}
			try {
				flashSelectedGeometries(jtsGeometry);
			} catch (NoninvertibleTransformException e) {
				e.printStackTrace();
			}
			pluginContext.getLayerViewPanel().setCurrentCursorTool(new DummyTool());

			startGeoMarketingDataProcess();
		} else{
			pluginContext.getLayerViewPanel().getContext().warnUser("Error en la Geometría");
		}

	}


	

	private static void startGeoMarketingDataProcess(){
		
		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
		progressDialog.setTitle(I18N.get("geomarketing","localgis.geomarketing.plugins.draw.tittle"));
		progressDialog.setTitle("TaskMonitorDialog.Wait");
		progressDialog.addComponentListener(new ComponentAdapter()
		{public void componentShown(ComponentEvent e)
		{new Thread(new Runnable()
		{public void run()
		{try {
			
			com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GeoMarketingOT2 geoMarketingData = null;
			
			if (jtsGeometry!=null && !jtsGeometry.isEmpty() 
//					&& jtsGeometry.isValid() 
					&& jtsGeometry!=null){
				if (AppContext.getApplicationContext().isOnline()){
					if(!AppContext.getApplicationContext().isLogged()){
						AppContext.getApplicationContext().login();
					}
					
					if(AppContext.getApplicationContext().isLogged()){
						String entidad = Integer.toString(AppContext.getIdEntidad());
						String userLogin = AppContext.getApplicationContext().getString(AppContext.USER_LOGIN);
						String idMunicipio = Integer.toString(AppContext.getIdMunicipio());

						if (entidad != null  && !entidad.equals("")){
							if (userLogin != null && !userLogin.equals("")){
								if (idMunicipio != null && !idMunicipio.equals("")){
									try{

										progressDialog.report(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.processdialog.populationdata"));
										geoMarketingData = GeoMarketingWSWrapper.getGeomarketingAndElementsData(
												jtsGeometry.toText(),
												Integer.toString(jtsGeometry.getSRID()),
												selectedLayer.getId_LayerDataBase(), 
												Integer.parseInt(entidad), 
												ageRangeList,
												userLogin,
												Integer.parseInt(idMunicipio));
			
										if (selectedLayer.isLocal() && geoMarketingData!=null){
											progressDialog.report(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.processdialog.layerdatalocal"));
											int numLocalLayerElements = 0;
											Iterator<Feature> features = (Iterator<Feature>) selectedLayer.getFeatureCollectionWrapper().getFeatures().iterator();
											while(features.hasNext()){
												Feature feature = features.next();
												if (jtsGeometry.intersects(feature.getGeometry()) || jtsGeometry.contains(feature.getGeometry())){
													numLocalLayerElements ++;
												}
											}
											geoMarketingData.setExternalValue(numLocalLayerElements);
										}
										
									}catch (NumberFormatException e) {
										e.printStackTrace();
										// Devolver el raton y la herramienta por defecto
										pluginContext.getLayerViewPanel().setCurrentCursorTool(new DummyTool());
									}catch (Exception e) {
										e.printStackTrace();
										// Devolver el raton y la herramienta por defecto
										pluginContext.getLayerViewPanel().setCurrentCursorTool(new DummyTool());
									}
								}
							}
						}
					}
				}

				if (geoMarketingData != null){
					GeoMarketingDataDialog dialog = new GeoMarketingDataDialog(pluginContext, geoMarketingData, selectedLayer);
					if (dialog.wasOKPressed()){
						//TODO Implementar.	
						dialog.dispose();
					}

				}

			} else{
				pluginContext.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.notvalidgeometry"));
			}

		} catch (Exception e){
			e.printStackTrace();
		} finally{
			progressDialog.setVisible(false);
		}
		}
		}).start();
		}
		});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);

	}



	
	
	

}

