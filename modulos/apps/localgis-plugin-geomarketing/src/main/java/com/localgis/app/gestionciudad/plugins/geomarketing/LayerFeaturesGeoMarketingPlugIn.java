/**
 * LayerFeaturesGeoMarketingPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.plugins.geomarketing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.model.GeopistaLayer;
import com.geopista.webservices.geomarketing.client.protocol.GeoMarketingWSStub.GeoMarketingOT2;
import com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.LayerAttributesDialog;
import com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.SeletedLayerGeomarketingDataDialog;
import com.localgis.app.gestionciudad.plugins.geomarketing.images.IconLoader;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.GeoMarketingConstat;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.GeoMarketingUtils;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.RangeData;
import com.localgis.app.gestionciudad.plugins.geomarketing.webserviceclient.GeoMarketingWSWrapper;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.cursortool.DummyTool;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;

public class LayerFeaturesGeoMarketingPlugIn extends ThreadedBasePlugIn{

	private boolean layerFeatureGeoMarketingButtonAdded = false;
	private PlugInContext context = null;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private int[] ageRangeList = {10,20,30,40,50,60};
	
	private GeopistaLayer selectedLayer = null;
	private GeopistaLayer geoMarketingLayer = null;
	private String geoMarketingAttribute = null;

		
	public boolean execute(PlugInContext context) throws Exception {

		this.context = context;
		GeoMarketingUtils.inicializarIdiomaGeoMarketing();
		
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
			this.selectedLayer = (GeopistaLayer) context.getLayerNamePanel().getSelectedLayers()[0];
		} else{
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.nolayersselecteds"));
			return false;
		}
		
		/*if (selectedLayer!=null && selectedLayer.getFeatureCollectionWrapper().getFeatures().size() > GeoMarketingConstat.NUM_MAX_FEATURES){
			context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.maxnumfeatures"));
			return false;
		}*/
				
		LayerAttributesDialog dialog = new LayerAttributesDialog(
				this.aplicacion.getMainFrame(),
				I18N.get("geomarketing","localgis.geomarketing.plugins.messages.layerattributesdialog.title"),
				context);
		if (dialog.wasOKPressed()){		
			this.geoMarketingLayer = (GeopistaLayer) dialog.getSelectedLayer();
			this.geoMarketingAttribute = dialog.getSelectedAttribute();
			
			if (geoMarketingLayer!=null && geoMarketingLayer.getFeatureCollectionWrapper().getFeatures().size() > GeoMarketingConstat.NUM_MAX_FEATURES){
				context.getLayerViewPanel().getContext().warnUser(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.error.maxnumfeatures"));
				return false;
			}

			return true;	
		} else{
			return false;
		}
				
	}
	
	@Override
	public void run(TaskMonitor monitor, PlugInContext context) throws Exception {

		ArrayList<GeoMarketingOT2> geoMarketingData = 
			new ArrayList<GeoMarketingOT2>();;

		if (this.geoMarketingLayer !=null 
				&& (this.geoMarketingLayer instanceof GeopistaLayer)
				&& !((GeopistaLayer)this.geoMarketingLayer).isLocal() 
				&& this.geoMarketingAttribute!=null 
				&& !this.geoMarketingAttribute.equals("") 
				&& this.geoMarketingLayer.getFeatureCollectionWrapper().getFeatureSchema().hasAttribute(this.geoMarketingAttribute)){


			if (AppContext.getApplicationContext().isOnline()){
				if(!AppContext.getApplicationContext().isLogged()){
					AppContext.getApplicationContext().login();
				}

				if(AppContext.getApplicationContext().isLogged()){

					String entidad = Integer.toString(AppContext.getIdEntidad());
					String userLogin = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_USERNAME);
					String idMunicipio = Integer.toString(AppContext.getIdMunicipio());

					if (entidad != null  && !entidad.equals("")){
						if (userLogin != null && !userLogin.equals("")){
							if (idMunicipio != null && !idMunicipio.equals("")){
								try{

									Locale loc=I18N.getLocaleAsObject(); 
									
									monitor.report(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.processdialog.populationdata"));
									//TODO Obtener los datos de GeoMarketing
									GeoMarketingOT2[] geoData = GeoMarketingWSWrapper.getGeomarketingAndElementsDataFromLayerGroupByAttribute(
											this.selectedLayer.getId_LayerDataBase(), 
											Integer.parseInt(entidad), 
											this.ageRangeList, 
											userLogin, 
											Integer.parseInt(idMunicipio), 
											this.geoMarketingAttribute, 
											this.geoMarketingLayer.getId_LayerDataBase(), 
											loc.toString());
									
									if (geoData != null){
										for(int i=0; i < geoData.length; i++){
											geoMarketingData.add(geoData[i]);
										}
									}
									
									
									// Creamos unos cuantos datos falsos de geomarketing para pruebas.
//									int numelems = 12;
//									for (int param = 0; param < numelems; param++){
//										GeoMarketingOT2 ot = new GeoMarketingOT2();
//										ot.setAttName("attributename " + param);
//										ot.setExternalValue(param);
//										ot.setForeignHabitants(param);
////										ot.setIdFeatures("feauresids" + param);
//										ot.setNumFemales(param);
//										ot.setNumHabitants(param);
//										ot.setNumMales(param);
//										ot.setRanges("ranges" + param);
//										ot.setS10(Long.toString(param));
//										ot.setS20(Integer.toString(param));
//										ot.setS30(Integer.toString(param));
//										ot.setS40(Integer.toString(param));
//										ot.setSpanishHabitants(param);
//										
//										geoMarketingData.add(ot);
//									}
								
									addTotalItemToGeoMarketingDataList(geoMarketingData);
									

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

				if (geoMarketingData != null && !geoMarketingData.isEmpty()){
					showGeoMarketingDataDialog(geoMarketingData);
				}
			}
		} else{
			//Los atributo y layer seleccionados no son validos
		}
	}
	
	
	private String sumRangeDatas(String stringRangeData1, String stringRangeData2){
		if(stringRangeData1!=null && stringRangeData2!=null){
			if (stringRangeData1.equals("")){
				return stringRangeData2;
			} else if (stringRangeData2.equals("")){
				return stringRangeData1;
			} else{
				String[] strings1 = stringRangeData1.split(";");
				String[] strings2 = stringRangeData2.split(";");
				
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
				
				return transformRanges(rangeDatas2.toArray(new RangeData[rangeDatas2.size()]));
			}
		}
		
		
		return "";
	}
	
	
	private String transformRanges(RangeData[] ranges) {
		String result = "";//:fin:valor;inicio:fin:valor;inicio:fin:valor:inicio::valor 
		for (int i = 0;i<ranges.length;i++){
			RangeData range = ranges[i];
			if(range.getStartRange()!=null && range.getEndRange()!=null)
				result +=range.getStartRange()+":"+range.getEndRange()+":"+range.getValue();
			else if (range.getStartRange()!=null)
				result +=range.getStartRange()+"::"+range.getValue();
			else if (range.getEndRange()!=null)
				result +=":"+range.getEndRange()+":"+range.getValue();
			if(i+1<ranges.length)
				result+=";";
		}
		return result;
	}
	private int[] appendIdFeatures(int[] idFeatureA, int[] idFeature2A) {
		ArrayList<Integer> idFeature = getInteger(idFeatureA);
		ArrayList<Integer> idFeature2 = getInteger(idFeature2A);
		idFeature.addAll(idFeature2);	
		return getInt(idFeature);
	}
	
	private int[] getInt(ArrayList<Integer> idFeature) {
		Iterator<Integer> it = idFeature.iterator();
		int[] data = new int[idFeature.size()];
		for(int i = 0;i<idFeature.size();i++ ){
			data[i] = it.next().intValue();
		}
		return data;
	}

	private ArrayList<Integer> getInteger(int[] idFeatureA) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i<idFeatureA.length;i++){
			list.add(new Integer(idFeatureA[i]));
		}
			
		return list;
	}

	private void addTotalItemToGeoMarketingDataList(
			ArrayList<GeoMarketingOT2> geoMarketingData) {
		// TODO Auto-generated method stub
		GeoMarketingOT2 totalElement = new GeoMarketingOT2();
		totalElement.setAttName(I18N.get("geomarketing","localgis.geomarketing.plugins.messages.featurelistdialog.totalitem"));
		totalElement.setIdFeature(new int[]{-1});
		totalElement.setS10("0");
		totalElement.setS20("0");
		totalElement.setS30("0");
		totalElement.setS40("0");
		totalElement.setRanges("");
		
		Iterator<GeoMarketingOT2> it = geoMarketingData.iterator();
		while(it.hasNext()){
			GeoMarketingOT2 next = it.next();
			totalElement.setIdFeature(appendIdFeatures(totalElement.getIdFeature(),next.getIdFeature()));
			totalElement.setExternalValue(
					totalElement.getExternalValue() + next.getExternalValue());
			
			totalElement.setNumHabitants(
					totalElement.getNumHabitants() + next.getNumHabitants());
			
			totalElement.setForeignHabitants(
					totalElement.getForeignHabitants() + next.getForeignHabitants());
			
			totalElement.setSpanishHabitants(
					totalElement.getSpanishHabitants() + next.getSpanishHabitants());
			
			totalElement.setNumFemales(
					totalElement.getNumFemales() + next.getNumFemales());
			
			totalElement.setNumMales(
					totalElement.getNumMales() + next.getNumMales());
			
			try{
			totalElement.setS10(
					Integer.toString(Integer.parseInt(totalElement.getS10()) + Integer.parseInt(next.getS10())));
			}catch (NumberFormatException e) {
			}
			
			try{
			totalElement.setS20( 
					Integer.toString(Integer.parseInt(totalElement.getS20()) + Integer.parseInt(next.getS20())));
			}catch (NumberFormatException e) {
			}
			
			try{
			totalElement.setS30(
					Integer.toString(Integer.parseInt(totalElement.getS30()) + Integer.parseInt(next.getS30())));
			}catch (NumberFormatException e) {
			}
			
			try{
			totalElement.setS40(
					Integer.toString(Integer.parseInt(totalElement.getS40()) + Integer.parseInt(next.getS40())));
			}catch (NumberFormatException e) {
			}
			
			totalElement.setMunicipio(next.getMunicipio());
			
			
			totalElement.setRanges(sumRangeDatas(totalElement.getRanges(), next.getRanges()));
			
		}
		
		geoMarketingData.add(totalElement);
	}

	private void showGeoMarketingDataDialog(ArrayList<GeoMarketingOT2> geoMarketingData) {

		GeoMarketingOT2[] geoMarketingDataArray = geoMarketingData.toArray(new GeoMarketingOT2[geoMarketingData.size()]);
		
		SeletedLayerGeomarketingDataDialog dialog = new SeletedLayerGeomarketingDataDialog(
				this.aplicacion.getMainFrame(),
				I18N.get("geomarketing","localgis.geomarketing.plugins.messages.featurelistdialog.title"),
				geoMarketingDataArray, 
				(Layer)this.selectedLayer,
				(Layer)this.geoMarketingLayer,
				this.context);
		if (dialog.wasOKPressed()){
			dialog.dispose();
		}
	}
	
	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("geomarketing","localgis.geomarketing.plugins.layerattributeselect.icon"));
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
		}, I18N.get("geomarketing","localgis.geomarketing.plugins.layerattributeselect.tittle"), false, null,
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
		.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
//		.add(checkFactory.createAtLeastNFeaturesMustBeSelectedCheck(1));
	}

	public void addButton(final ToolboxDialog toolbox)
	{
		if (!this.layerFeatureGeoMarketingButtonAdded )
		{
			toolbox.addToolBar();
			LayerFeaturesGeoMarketingPlugIn explode = new LayerFeaturesGeoMarketingPlugIn();                 
			toolbox.addPlugIn(explode, null, explode.getIcon());
			toolbox.finishAddingComponents();
			toolbox.validate();
			this.layerFeatureGeoMarketingButtonAdded = true;
		}
	}

}
