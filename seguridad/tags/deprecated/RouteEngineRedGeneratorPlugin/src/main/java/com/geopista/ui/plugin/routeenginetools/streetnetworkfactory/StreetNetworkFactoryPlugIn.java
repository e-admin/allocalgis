package com.geopista.ui.plugin.routeenginetools.streetnetworkfactory;


import java.util.HashMap;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.build.GraphGenerator;
import org.uva.geotools.graph.structure.basic.BasicNode;
import org.uva.graph.build.UIDgenerator.SequenceUIDGenerator;
import org.uva.routeserver.street.StreetTrafficRegulation;

import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.IGeopistaLayer;
import com.geopista.ui.plugin.routeenginetools.genredplugin.GenerarRedPlugIn;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.BasicFeaturePropertiesDialog;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.StreetFeaturePropertiesDialog;
import com.geopista.ui.plugin.routeenginetools.networkfactorydialogs.StreetNetworkFactoryDialog;
import com.geopista.ui.plugin.routeenginetools.routeutil.NetworkModuleUtilWorkbench;
import com.geopista.ui.plugin.routeenginetools.streetnetworkfactory.images.IconLoader;
import com.localgis.route.graph.build.LocalGisGraphGenerator;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISStreetDynamicEdge;
import com.localgis.route.network.NetworkProperty;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class StreetNetworkFactoryPlugIn extends GenerarRedPlugIn {


	private static Logger LOGGER = Logger.getLogger(StreetNetworkFactoryPlugIn.class);

	public boolean execute(PlugInContext context)
	{
		if(context.getLayerViewPanel() == null)
			return false;
		this.context = context;

		StreetNetworkFactoryDialog dialog = new StreetNetworkFactoryDialog(
				context.getWorkbenchFrame(), "", context);

		basicDialog = dialog;

		super.execute(context);

		return false;

	}



    @Override
    protected void createLocalGisDynamicEdgeFromSelectedFeatures(Feature element, int idSystemLayer, int lastSelectedLayer,
	    LocalGisGraphGenerator linegenerator,
	    GeopistaLayer selectedLayer, BasicFeaturePropertiesDialog dialog, HashMap<String, Object> networkProperties)
    {
	StreetFeaturePropertiesDialog dialogo = (StreetFeaturePropertiesDialog) dialog;
		CoordinateSystem coordSys =context.getLayerManager().getCoordinateSystem();				
		if (coordSys != null){
			element.getGeometry().setSRID(coordSys.getEPSGCode());
		} else{
			element.getGeometry().setSRID(23030);
		}

		int featureID = -1;
		try{
			if (element instanceof GeopistaFeature){
				featureID = Integer.parseInt(((GeopistaFeature)element).getSystemId());
			} else{
				featureID = element.getID();
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			LOGGER.error(e);
			featureID = -1;
		}

		LocalGISStreetDynamicEdge streetEdge;
		try {
		streetEdge = (LocalGISStreetDynamicEdge) linegenerator.add(element.getGeometry(), featureID, idSystemLayer, element.getGeometry().getSRID(),
			element);


			streetEdge.setTrafficRegulation(
					(StreetTrafficRegulation) 
					dialogo.getSentidoCirculacionComboBox().getSelectedItem()
			);
			if (streetEdge != null){
				if (dialogo.wasOKPressed()){

					if (dialogo.getDescriptionComboBox().getSelectedIndex() > 0){
						NetworkProperty networkColumnDescProperties = new NetworkProperty();
						networkColumnDescProperties.setNetworkManagerProperty(
								Integer.toString(selectedLayer.getId_LayerDataBase()), 
								dialogo.getDescriptionComboBox().getSelectedItem().toString()
						);
						networkProperties.put("ColumnDescriptor", networkColumnDescProperties);
					}

					if (dialogo.getTipoCallejeroComboBox().getSelectedIndex() > 0){
						NetworkProperty networkColumnDescProperties = new NetworkProperty();
						networkColumnDescProperties.setNetworkManagerProperty(
								Integer.toString(selectedLayer.getId_LayerDataBase()), 
								dialogo.getTipoCallejeroComboBox().getSelectedItem().toString()
						);
						networkProperties.put("TypeColumnDescriptor", networkColumnDescProperties);
					}

					if (dialogo.getVelocidadViaComboBox().getSelectedItem().equals("50 Km/h")){
						streetEdge.setNominalMaxSpeed(50.0 * 1000.0 / 3600.0);
					} else{
						streetEdge.setNominalMaxSpeed(Double.parseDouble(element.getAttribute(
								(dialogo).getVelocidadViaComboBox().getSelectedItem().toString()).toString()
						));
					}


					if (dialogo.getBidirectionRadioButton().isSelected()){

						streetEdge.setImpedanceAToB(createNewLocalGisSimpleimpedance(element.getGeometry(), 
								dialogo.getImpedanciaAB(),
								element));
						streetEdge.setImpedanceBToA(createNewLocalGisSimpleimpedance(element.getGeometry(), 
								dialogo.getImpedanciaBA(), 
								element));

					} else if (dialogo.getUniqueDirectionAtoBRadioButton().isSelected()){

						streetEdge.setImpedanceAToB(createNewLocalGisSimpleimpedance(element.getGeometry(), 
								dialogo.getImpedanciaAB(),
								element));

					} else if (dialogo.getUniqueDirectionBtoARadioButton().isSelected()){

						streetEdge.setImpedanceBToA(createNewLocalGisSimpleimpedance(element.getGeometry(), 
								dialogo.getImpedanciaBA(), 
								element));

					} 

				} else {
					streetEdge.setImpedanceBidirecccional(element.getGeometry().getLength());
					streetEdge.setNominalMaxSpeed(50.0 * 1000.0 / 3600.0);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param context
	 * @param linegenerator
	 * @param networkProperties
	 */
    protected void generarRedDeUnaCapa(PlugInContext context, GraphGenerator linegenerator, HashMap<String, Object> networkProperties)
    {

	Object object = basicDialog.getLayerComboBox().getSelectedItem();

	super.generarRedDeUnaCapa(context, linegenerator, networkProperties);
	if (((StreetNetworkFactoryDialog) basicDialog).getTipoCallejeroComboBox().getSelectedIndex() >= 0)
	    {
		NetworkProperty networkTypeColumnDescProperties = new NetworkProperty();
		networkTypeColumnDescProperties.setNetworkManagerProperty(Integer.toString(((GeopistaLayer) object).getId_LayerDataBase()),
			((StreetNetworkFactoryDialog) basicDialog).getTipoCallejeroComboBox().getSelectedItem().toString());
		networkProperties.put("TypeColumnDescriptor", networkTypeColumnDescProperties);
	    }

    }


	/**
	 * @param lstring
	 * @param linegenerator
	 * @param object
	 * @param element
	 * @param mls
	 * @param i
	 */
    @Override
    protected ILocalGISEdge createNewLocalGisDinamycEdges(LocalGisGraphGenerator linegenerator, int systemIDLayer,Feature element, Geometry mls)
    {

		int featureID = -1;
		try{
		    IGeopistaLayer geoLayer=((GeopistaFeature)element).getLayer();
		    if (geoLayer!=null)// es un geopistaLayer
			{
			if (element instanceof GeopistaFeature && !((GeopistaFeature) element).isTempID() && !geoLayer.isLocal())
			{
				featureID = Integer.parseInt(((GeopistaFeature)element).getSystemId());
				systemIDLayer=geoLayer.getId_LayerDataBase();
			} else{
				featureID = element.getID();
				systemIDLayer=geoLayer.getId_layer();
			}
			}
		    else
			{// Una capa local del GIS
			    featureID = element.getID();
			}
		} catch (NumberFormatException e) {
			// TODO: handle exception
			LOGGER.error(e);
			featureID = -1;
		}

		LocalGISStreetDynamicEdge edge;
		try {
		edge = (LocalGISStreetDynamicEdge) linegenerator.add(mls, featureID, systemIDLayer, element.getGeometry().getSRID(), element);

			if (edge != null){
				if (((StreetNetworkFactoryDialog)basicDialog).getVelocidadViaComboBox().getSelectedItem().equals("50 Km/h")){
					edge.setNominalMaxSpeed(50.0 * 1000 / 3600);
				} else{
					edge.setNominalMaxSpeed(Double.parseDouble(element.getAttribute(
							((StreetNetworkFactoryDialog)basicDialog).getVelocidadViaComboBox().getSelectedItem().toString()).toString()
					));
				}

				edge.setTrafficRegulation(
						(StreetTrafficRegulation) 
						((StreetNetworkFactoryDialog)basicDialog).getSentidoCirculacionComboBox().getSelectedItem()
				);



				this.setImpedanceToLocalGisDynamicEdge( mls, element, edge);
			}
			return edge;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new LocalGISStreetDynamicEdge(new BasicNode(), new BasicNode(), -1,-1,new SequenceUIDGenerator(),element);
		}


	}
	

	protected boolean setImpedanceToLocalGisDynamicEdge(Geometry mls, Feature elementFeature,
			LocalGISStreetDynamicEdge lgDynamicEdge){

		if (basicDialog.getBidirectionRadioButton().isSelected()){

			lgDynamicEdge.setImpedanceAToB(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaAB(),
					elementFeature));
			lgDynamicEdge.setImpedanceBToA(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaBA(), 
					elementFeature));

		} else if (basicDialog.getUniqueDirectionAtoBRadioButton().isSelected()){

			lgDynamicEdge.setImpedanceAToB(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaAB(),
					elementFeature));

		} else if (basicDialog.getUniqueDirectionBtoARadioButton().isSelected()){

			lgDynamicEdge.setImpedanceBToA(createNewLocalGisSimpleimpedance(mls, 
					basicDialog.getImpedanciaBA(), 
					elementFeature));

		} else{
			return false;
		}

		return true;
	}

	public ImageIcon getIcon() {
		return IconLoader.icon(I18N.get("genred","routeengine.genred.iconfile"));
	}



    public void run(TaskMonitor monitor, PlugInContext context)
    {
	super.run(monitor, context);
    }
    @Override
    protected GraphGenerator getGraphGenerator()
	{
	    return NetworkModuleUtilWorkbench.getLocalGISStreetBasicLineGraphGenerator();
	}
    @Override
    protected BasicFeaturePropertiesDialog createPropertiesDialog(GeopistaLayer selectedLayer)
    {
	return new StreetFeaturePropertiesDialog(selectedLayer, "Descripcion Básica para Capa " + selectedLayer.getName(), context);
    }

}