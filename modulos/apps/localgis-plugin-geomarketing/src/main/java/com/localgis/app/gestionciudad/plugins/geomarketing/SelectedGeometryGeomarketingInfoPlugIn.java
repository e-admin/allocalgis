/**
 * SelectedGeometryGeomarketingInfoPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.plugins.geomarketing;

import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.TaskComponent;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaLayer;
import com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GeoMarketingOT2;
import com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.GeoMarketingDataDialog;
import com.localgis.app.gestionciudad.plugins.geomarketing.images.IconLoader;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.GeoMarketingUtils;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.RangeData;
import com.localgis.app.gestionciudad.plugins.geomarketing.webserviceclient.GeoMarketingWSWrapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.cursortool.DummyTool;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;

public class SelectedGeometryGeomarketingInfoPlugIn extends ThreadedBasePlugIn{

	private Geometry jtsGeometry = null;
	private boolean drawGeometryGeomarketingButtonAdded = false;
	private PlugInContext context = null;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private int[] ageRangeList = {10,20,30,40,50,60};
	
	private GeopistaLayer selectedLayer = null;



	@SuppressWarnings("unchecked")
	public boolean execute(PlugInContext context) throws Exception {

		this.context = context;

		if (context.getLayerViewPanel() == null){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.layerviewpanel"));
			return false;
		}

		// Obtenemos la geatures seleccionadas
		final ArrayList<Object> featuresselected = new ArrayList<Object>(
				context.getLayerViewPanel().getSelectionManager()
				.getFeaturesWithSelectedItems());



		if (featuresselected == null || featuresselected.isEmpty()){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.featuresselecteds"));
			return false;
		}

		if (context.getLayerNamePanel() == null || context.getLayerNamePanel().getSelectedLayers() == null || 
				context.getLayerNamePanel().getSelectedLayers().length == 0){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.nolayersselecteds"));
			return false;
		}
		if (context.getLayerNamePanel()!=null && context.getLayerNamePanel().getSelectedLayers()!= null
				&& context.getLayerNamePanel().getSelectedLayers().length > 0){
			this.selectedLayer = (GeopistaLayer) context.getLayerNamePanel().getSelectedLayers()[0];
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
		this.jtsGeometry = getGeometryFromFeatures(featuresselected);
		
		
		flashSelectedGeometries(jtsGeometry);

		return true;
	}

	private ZoomToSelectedItemsPlugIn zoomToSelectedItemsPlugIn =
		new ZoomToSelectedItemsPlugIn();
	private TaskComponent taskFrame;
	public void flashSelectedGeometries(Geometry ge) throws NoninvertibleTransformException {
		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(ge);
		zoomToSelectedItemsPlugIn.flash(
				geometries,
				(LayerViewPanel)taskFrame.getLayerViewPanel());
	}

	private Geometry getGeometryFromFeatures(Collection<Object> featuresselected) {

		Geometry resultGeometry = null;

		Iterator<Object> featuresIterator = featuresselected.iterator();
		ArrayList<Geometry> selectedGeometries = new ArrayList<Geometry>();
		while (featuresIterator.hasNext()){
			Feature feat = (Feature) featuresIterator.next();
			if (feat != null){
				if (feat.getGeometry() != null){
					selectedGeometries.add(feat.getGeometry());
				}
			}
		}

		// Generamos la geomtry collection a partir de la lista de
		// geometrias seleccionadas del editor.
		GeometryFactory geomFactory = new GeometryFactory();
		GeometryCollection geomCollection = geomFactory.createGeometryCollection(selectedGeometries.toArray(new Geometry[selectedGeometries.size()]));
		resultGeometry = geomCollection.convexHull();

		
		// Se asigna a la geometry un srid si no lo tuviera
		if (resultGeometry != null){
			if (resultGeometry.getSRID() == 0){
				CoordinateSystem coodSys = this.context.getLayerManager().getCoordinateSystem();
				if (coodSys != null){
					resultGeometry.setSRID(coodSys.getEPSGCode());
				}
			}
		}		

		// Se devuelve el convexhull, para poder elegir entre varios puntos.
		return resultGeometry;

	}



	@SuppressWarnings("unchecked")
	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
	throws Exception {		
		
		GeoMarketingOT2 geoMarketingData = null;

		if(jtsGeometry != null && !jtsGeometry.isEmpty() && jtsGeometry.isValid() && selectedLayer != null){
			if (AppContext.getApplicationContext().isOnline()){
				if(!AppContext.getApplicationContext().isLogged()){
					AppContext.getApplicationContext().login();
				}
				
				if(AppContext.getApplicationContext().isLogged()){
					
					if (jtsGeometry != null){
						if (jtsGeometry.getSRID() == 0){
							CoordinateSystem coodSys = this.context.getLayerManager().getCoordinateSystem();
							if (coodSys != null){
								jtsGeometry.setSRID(coodSys.getEPSGCode());
							}
						}
					}		

					String entidad = Integer.toString(AppContext.getIdEntidad());
					String userLogin = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME);
					String idMunicipio = Integer.toString(AppContext.getIdMunicipio());

					if (entidad != null  && !entidad.equals("")){
						if (userLogin != null && !userLogin.equals("")){
							if (idMunicipio != null && !idMunicipio.equals("")){
							try{
								
								monitor.report(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.processdialog.populationdata"));
								geoMarketingData = GeoMarketingWSWrapper.getGeomarketingAndElementsData(
										jtsGeometry.toText(),
										Integer.toString(jtsGeometry.getSRID()),
										selectedLayer.getId_LayerDataBase(), 
										Integer.parseInt(entidad), 
										ageRangeList,
										userLogin,
										Integer.parseInt(idMunicipio));
								
//								if (selectedLayer.isLocal() && geoMarketingData!=null){
								monitor.report(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.processdialog.layerdatalocal"));
								int numLocalLayerElements = 0;
								Iterator<Feature> features = (Iterator<Feature>) selectedLayer.getFeatureCollectionWrapper().getFeatures().iterator();
								while(features.hasNext()){
									Feature feature = features.next();
									if (jtsGeometry.intersects(feature.getGeometry()) || jtsGeometry.contains(feature.getGeometry())){
										numLocalLayerElements ++;
									}
								}
								geoMarketingData.setExternalValue(numLocalLayerElements);

							
							}catch (NumberFormatException e) {
								e.printStackTrace();
								// Devolver el raton y la herramienta por defecto
								context.getLayerViewPanel().setCurrentCursorTool(new DummyTool());
							}catch (Exception e) {
								e.printStackTrace();
								// Devolver el raton y la herramienta por defecto
								context.getLayerViewPanel().setCurrentCursorTool(new DummyTool());
							}
							}
						}
					}
				}
			}
			
			if (geoMarketingData != null){
				showGeoMarketingDataDialog(geoMarketingData);
			}

		} else{
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.geometry"));
		}
	}


	private String sumRangeDatas(String rangeData1, String rangeData2){
		if(rangeData1!=null && rangeData2!=null){
			if (rangeData1.equals("")){
				return rangeData2;
			} else if (rangeData2.equals("")){
				return rangeData1;
			} else{
				String[] strings1 = rangeData1.split(";");
				String[] strings2 = rangeData2.split(";");
				
				ArrayList<RangeData> rangeDatas1 = new ArrayList<RangeData>();
				ArrayList<RangeData> rangeDatas2 = new ArrayList<RangeData>();
				
				RangeData rangeData = null;
				for (int i=0; i < strings1.length; i++){
					rangeData = new RangeData();
					rangeData.readFromString(strings1[i]);
					rangeDatas1.add(rangeData);
				}
				
				for (int i=0; i < strings1.length; i++){
					rangeData = new RangeData();
					rangeData.readFromString(strings2[i]);
					rangeDatas2.add(rangeData);
				}
				
				for (int i=0; i < rangeDatas1.size(); i++){
					for (int m=0; m < rangeDatas2.size(); m++){
						if (rangeDatas1.get(i).compareRangesDatas(rangeDatas2.get(m))){
							rangeDatas2.get(i).setValue(
									rangeDatas1.get(i).getValue() + rangeDatas2.get(m).getValue());
						}
					}
				}
			}
		}
		
		
		return "";
	}

	

	private void showGeoMarketingDataDialog(GeoMarketingOT2 geoMarketingData) {
		GeoMarketingDataDialog dialog = new GeoMarketingDataDialog(this.context, geoMarketingData, this.selectedLayer);
		if (dialog.wasOKPressed()){
			dialog.dispose();
		}
	}



	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("geomarketing","localgis.geomarketing.plugins.select.icon"));
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
		}, I18N.get("geomarketing","localgis.geomarketing.plugins.select.tittle"), false, null,
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
		.add(checkFactory.createAtLeastNFeaturesMustBeSelectedCheck(1));
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



}
