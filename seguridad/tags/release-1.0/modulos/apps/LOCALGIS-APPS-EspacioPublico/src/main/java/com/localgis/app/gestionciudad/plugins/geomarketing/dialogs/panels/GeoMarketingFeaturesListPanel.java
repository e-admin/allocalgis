package com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.localgis.app.gestionciudad.dialogs.interventions.utils.UtilidadesAvisosPanels;
import com.localgis.app.gestionciudad.plugins.geomarketing.dialogs.renderers.GeoMarketingDataJListRenderer;
import com.localgis.app.gestionciudad.plugins.geomarketing.utils.GeoMarketingToggleButton;
import com.localgis.webservices.geomarketing.client.GeoMarketingWSStub.GeoMarketingOT2;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;

public class GeoMarketingFeaturesListPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3175942219674108184L;
	
	private JList geomarketingDataJList = null;
	private JScrollPane listScroller = null;
	private GeoMarketingTextDataPanel dataPanel = null;
	
	private JPanel layerInfoPanel = null;
	private JPanel listPanel = null;
	
	private GeoMarketingToggleButton showGeomarketingDataJButton = null;
	
	private Layer selectedLayer = null;

	private Layer geoMarketingLayer = null;

	private JDialog contentDialog = null;

	private PlugInContext context = null;
		
	public GeoMarketingFeaturesListPanel() {
		super(new GridBagLayout());
		initialize();
	}
	
	public GeoMarketingFeaturesListPanel(Layer selectedLayer) {
		super(new GridBagLayout());
		this.selectedLayer = selectedLayer;
		initialize();
	}
	
	
	public GeoMarketingFeaturesListPanel(Layer selectedLayer, Layer geoMarketingLayer, GeoMarketingOT2[] geomarketingData,
			JDialog parentDialog, PlugInContext context) {
		super(new GridBagLayout());
		
		this.contentDialog = parentDialog;
		this.selectedLayer = selectedLayer;
		this.geoMarketingLayer  = geoMarketingLayer;
		this.context  = context;
		loadGeoMarketingDataAtList(geomarketingData);
			
		initialize();
		
		if (geomarketingData != null && geomarketingData.length > 0){
			this.getGeoMarketingDataJList().setSelectedIndex(0);
		}
	}
	
	private void initialize() {
		this.add(getListPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.VERTICAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		
		this.add(getDataPanel(), 
				new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	private JPanel getListPanel(){
		if (listPanel == null){
			listPanel = new JPanel(new GridBagLayout());
			
			listPanel.add(getLayerInfoPanel(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			listPanel.add(getListScroller(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.BOTH, 
							new Insets(0, 5, 0, 5), 0, 202));
			
			listPanel.add(getShowGeomarketingDataJButton(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.LAST_LINE_END, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		return listPanel;
	}
	
	private JPanel getLayerInfoPanel(){
		if ( layerInfoPanel == null){
			layerInfoPanel = new JPanel(new GridBagLayout());
			if (geoMarketingLayer != null){
				layerInfoPanel.add(new JLabel(geoMarketingLayer.getName() + " "+ I18N.get("geomarketing","localgis.geomarketing.plugins.messages.featurelistdialog.label.geoinfo")));
			}
		}
		return layerInfoPanel;
	}

	
	private void loadGeoMarketingDataAtList(GeoMarketingOT2[] geomarketingData){
		((DefaultListModel)getGeoMarketingDataJList().getModel()).clear();
		if (this.getGeoMarketingDataJList() != null && geomarketingData.length>0){
			for(int i=0; i < geomarketingData.length; i++){
				((DefaultListModel)getGeoMarketingDataJList().getModel()).addElement(geomarketingData[i]);		
			}
		}
	}

	private JList getGeoMarketingDataJList (){
		if (geomarketingDataJList == null){
			geomarketingDataJList = new JList();
			geomarketingDataJList.setModel(new DefaultListModel());
			geomarketingDataJList.setSelectionModel(new DefaultListSelectionModel());
			geomarketingDataJList.setLayoutOrientation(JList.VERTICAL);
			geomarketingDataJList.setCellRenderer( new GeoMarketingDataJListRenderer());
			geomarketingDataJList.addListSelectionListener(new ListSelectionListener(){
				@Override
				public void valueChanged(ListSelectionEvent e) {
					onSelectionListChangedDo();
				}});
			
		}
		return geomarketingDataJList;
	}
	
	private void onSelectionListChangedDo() {
		if (getGeoMarketingDataJList() != null){
			if (getGeoMarketingDataJList().getSelectedValue() != null){
				if (getGeoMarketingDataJList().getSelectedValue() instanceof GeoMarketingOT2
						&& selectedLayer instanceof GeopistaLayer){
					this.getDataPanel().loadGeomarketingData((GeoMarketingOT2)getGeoMarketingDataJList().getSelectedValue(),
							(GeopistaLayer) this.selectedLayer);
					
					selectAndZoomToLayerFeature((GeoMarketingOT2)getGeoMarketingDataJList().getSelectedValue(), (GeopistaLayer) this.geoMarketingLayer);
					
				}
			}
		}
	}

	/**
	 * @param geoMarketingOT2 
	 * @param layer 
	 * 
	 */
	private void selectAndZoomToLayerFeature(GeoMarketingOT2 geoMarketingOT2, GeopistaLayer layer) {
		if (this.context!=null){
			
			if (context.getLayerViewPanel()!=null && context.getLayerViewPanel().getSelectionManager()!=null){
				context.getLayerViewPanel().getSelectionManager().clear();
			}
			
			if (context.getLayerManager()!=null && context.getLayerManager().getLayers()!=null &&
					!context.getLayerManager().getLayers().isEmpty()){
				
				Iterator<GeopistaLayer> geopistaLayers = context.getLayerManager().getLayers().iterator();
				while(geopistaLayers.hasNext()){
					GeopistaLayer geopistaLayer = geopistaLayers.next();
					int idLayer = geopistaLayer.getId_LayerDataBase();
					Iterator<Feature> allFeatures= geopistaLayer.getFeatureCollectionWrapper().getFeatures().iterator();
					while (allFeatures.hasNext()) {
						Feature feature= allFeatures.next();
						if (idLayer == layer.getId_LayerDataBase()){
							for(int i = 0 ;i< geoMarketingOT2.getIdFeature().length; i++){
								if (UtilidadesAvisosPanels.getFeatureSystemId((GeopistaFeature)feature) == geoMarketingOT2.getIdFeature()[i]){
									selectFeature(context.getLayerViewPanel(),geopistaLayer, feature);
								}
							}
						}
					}
				}
				
			}
			
			if (context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems()!=null
					&& !context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems().isEmpty()){

				Geometry allFeaturesGeometry = getGeometryFromFeatures(context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems());
				
				try {
					flashZoomSelectedGeometries(allFeaturesGeometry, context.getLayerViewPanel());
				} catch (NoninvertibleTransformException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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
	
	private static ZoomToSelectedItemsPlugIn zoomToSelectedItemsPlugIn =
		new ZoomToSelectedItemsPlugIn();
	
	public static void flashZoomSelectedGeometries(Geometry ge, ILayerViewPanel panel) throws NoninvertibleTransformException {
		ArrayList<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(ge);
		
		zoomToSelectedItemsPlugIn.zoom(
				geometries,
				panel);
		
//		zoomToSelectedItemsPlugIn.flash(
//				geometries,
//				panel);
	}
	
	private void selectFeature(ILayerViewPanel layerViewPanel, GeopistaLayer geopistaLayer, Feature feature) {
		boolean originalPanelUpdatesEnabled = layerViewPanel.getSelectionManager().arePanelUpdatesEnabled();
        try
        {
          layerViewPanel.getSelectionManager().setPanelUpdatesEnabled(false);
          layerViewPanel.getSelectionManager().getFeatureSelection().selectItems(geopistaLayer, feature);
          
        }finally
        {
          layerViewPanel.getSelectionManager().setPanelUpdatesEnabled(originalPanelUpdatesEnabled);
          
        }
          layerViewPanel.getSelectionManager().updatePanel();
		
	}
	

	private JScrollPane getListScroller(){
		if (listScroller == null){
			listScroller = new JScrollPane(this.getGeoMarketingDataJList());
			listScroller.setViewportView(this.getGeoMarketingDataJList());
			listScroller.setAutoscrolls(true);
			listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		return listScroller;
	}
	
	private GeoMarketingTextDataPanel getDataPanel(){
		if (dataPanel == null){
			dataPanel = new GeoMarketingTextDataPanel();
		}
		return dataPanel;
	}
	
	private GeoMarketingToggleButton getShowGeomarketingDataJButton(){
		if (showGeomarketingDataJButton == null){
			this.showGeomarketingDataJButton = new GeoMarketingToggleButton(
					I18N.get("geomarketing","localgis.geomarketing.plugins.messages.featurelistdialog.togglebutton.deactivate"),
					I18N.get("geomarketing","localgis.geomarketing.plugins.messages.featurelistdialog.togglebutton.activate")
					);
				
			
			showGeomarketingDataJButton.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					onshowGeomarketingDataJButtonDo();
				}
			});
		}
		return showGeomarketingDataJButton;
	}
	
	private void onshowGeomarketingDataJButtonDo() {
		if (this.getDataPanel().isVisible()){
			this.getDataPanel().setVisible(false);
			this.contentDialog.setSize(
					this.contentDialog.getWidth()-this.getDataPanel().getWidth(),
					this.contentDialog.getHeight());
		}else{
			this.getDataPanel().setVisible(true);
			this.contentDialog.setSize( 
					this.contentDialog.getWidth()+this.getDataPanel().getWidth(),
					this.contentDialog.getHeight());
		}
		
	}

}
