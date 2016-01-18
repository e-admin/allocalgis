package com.vividsolutions.jump.workbench.plugin;

import java.awt.event.MouseMotionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.deegree.graphics.sld.NamedLayer;
import org.deegree.graphics.sld.StyledLayerDescriptor;
import org.deegree.xml.XMLParsingException;
import org.deegree_impl.graphics.sld.SLDFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.StringDomain;
import com.geopista.feature.Table;
import com.geopista.model.GeopistaLayer;
import com.geopista.style.sld.model.SLDStyle;
import com.geopista.style.sld.model.impl.SLDStyleImpl;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.CoordinateArrays;
import com.vividsolutions.jump.workbench.model.ITask;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.cursortool.CursorTool;
import com.vividsolutions.jump.workbench.ui.cursortool.editing.EditingPlugIn;


public class PlugInAutoImporter extends ThreadedBasePlugIn{

	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	private ImageIcon imageIcon;
	private PlugInContext context = null;
	private GeopistaLayer capa = null;  


	public PlugInContext getContext() {
		return context;
	}


	public void setContext(PlugInContext context) {
		this.context = context;
	}


	@Override
	public void run(TaskMonitor monitor, PlugInContext context)
			throws Exception {
		// TODO Auto-generated method stub
		
		
		
	}
	
	
	/**
	 * anade en el menú de la zona de capas el plugin
	 * @param context
	 * @param nameResourceBundle
	 * @param title
	 */
	protected void addPopupMenuItem(PlugInContext context, String nameResourceBundle, String title, 
			MultiEnableCheck enableCheck){
	
	 context.getFeatureInstaller().addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
             .getGuiComponent()
             .getCategoryPopupMenu(),
 			this,
     		  I18N.get(nameResourceBundle, title) ,
     		  false,null,enableCheck);
	
	}
	
	/**
	 * Anade a la barra de herramientas el plugin 
	 * @param context
	 * @param toolBarCategory
	 */
	protected void addToolBar(PlugInContext context, String toolBarCategory, MultiEnableCheck enableCheck){
		 String pluginCategory = aplicacion.getString(toolBarCategory);
	     ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addPlugIn(getImageIcon(),
	             this,enableCheck,context.getWorkbenchContext());
	}	
	
	/**
	 * Anade un Cursor Tool en la Barra de herammientas
	 * @param context
	 * @param toolBarCategory
	 * @param cursorTool
	 */
	protected void addCursorTool(PlugInContext context, String toolBarCategory, CursorTool cursorTool){
		 String pluginCategory = aplicacion.getString(toolBarCategory);
	     ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent()).getToolBar(pluginCategory).addCursorTool(cursorTool);
	}	
	
	
	protected ImageIcon getImageIcon() {
		return imageIcon;
	}


	protected void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}
	
	public String getUsuario(){
		//Principal principal = null;
		//Group roleGroup = null;
		String user = aplicacion.getApplicationContext().getString(AppContext.USER_LOGIN);
		
		
	/*	if(  PasarelaAdmcar.listaSesiones != null &&
				PasarelaAdmcar.listaSesiones.existeSesion(user)){
			Sesion sesion =PasarelaAdmcar.listaSesiones.getSesion(user);
			
			principal = sesion.getUserPrincipal();
			roleGroup = sesion.getRoleGroup();
		}*/
		
		
		return user;
	}
	
	public JFrame getFrameRoot(){
		return aplicacion.getMainFrame();
	}
	
	public ITask getMapa(){
		
		return context.getTask();
	}
	
	public List getCapas(){
		return context.getLayerManager().getLayers();
	}
	
	public List getEntidades(String idCapa){
		List lstFeatures = null;
		FeatureCollectionWrapper featureCollection = null;
		for (Iterator iterator = context.getLayerManager().getLayers().iterator(); iterator.hasNext();) {
			GeopistaLayer geopistaLayer = (GeopistaLayer) iterator.next();

			if(idCapa.equals(geopistaLayer.getName())){
				featureCollection = geopistaLayer.getFeatureCollectionWrapper();	
			}
		}
		if(featureCollection != null){
			
			lstFeatures = featureCollection.getFeatures();
		}
		return lstFeatures;
	}
	
	public Collection getEntidadesSeleccionadas(){
		return context.getLayerViewPanel().getSelectionManager().getFeatureSelection().getFeaturesWithSelectedItems();
	}
	
	public void getEntidad(String idCapa, double x, double y){
		
		List lstEntidades = getEntidades(idCapa);

	}
	
	
	public double getEscala(){
		return context.getLayerViewPanel().getViewport().getScale();
	}
	
	public void centrarMapa(double x, double y, double escala){
		
		Coordinate coordinate = new Coordinate(x, y);

		try {
			
			context.getLayerViewPanel().getViewport().
						zoom(toEnvelope(coordinate, context.getLayerManager(), escala));
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Envelope toEnvelope(Coordinate coordinate, LayerManager layerManager, double escala) {
		int segments = 0;
		int segmentSum = 0;
		outer : for (Iterator i = layerManager.iterator(); i.hasNext(); ) {
			Layer layer = (Layer) i.next();
			for (Iterator j = layer.getFeatureCollectionWrapper().iterator(); j
					.hasNext(); ) {
				Feature feature = (Feature) j.next();
                Collection coordinateArrays = CoordinateArrays.toCoordinateArrays(feature.getGeometry(), false);
                for (Iterator k = coordinateArrays.iterator(); k.hasNext(); ) {
                	Coordinate[] coordinates = (Coordinate[]) k.next();
                    for (int a = 1; a < coordinates.length; a++) {
                        segments++;
                    	segmentSum += coordinates[a].distance(coordinates[a-1]);
                        if (segments > 100) { break outer; }
                    }
                }
			}
		}
		Envelope envelope = new Envelope(coordinate);
        //Choose a reasonable magnification [Jon Aquino 10/22/2003]
		if (segmentSum > 0) {
			envelope = EnvelopeUtil.expand(envelope,
					segmentSum / (double) segments);
		} else {
			envelope = EnvelopeUtil.expand(envelope, escala);
		}
		return envelope;
	}
	
	public void seleccionar(String idCapa, Collection switchCollection){
		
		GeopistaLayer currentLayer = null; 

		for (Iterator iterator = context.getLayerManager().getLayers().iterator(); iterator.hasNext();) {
			GeopistaLayer geopistaLayer = (GeopistaLayer) iterator.next();

			if(idCapa.equals(geopistaLayer.getName())){
				currentLayer = geopistaLayer;
				
			}
		}
		
		Collection selectFeatures = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(currentLayer);
		
		if(currentLayer != null){
			 context.getLayerViewPanel().getSelectionManager().unselectItems(currentLayer,selectFeatures);
			context.getLayerViewPanel().getSelectionManager().getFeatureSelection().selectItems(currentLayer,switchCollection);
			context.getLayerViewPanel().getSelectionManager().updatePanel();
		}
		 
	}
	
	public MouseMotionListener[] getEventMap(){
		return ((LayerViewPanel) context.getLayerViewPanel()).getMouseMotionListeners();
	}
	
	
	public String getSLD(String idCapa){
		String xmlSLD = "";
		FeatureCollectionWrapper featureCollection = null;
		for (Iterator iterator = context.getLayerManager().getLayers().iterator(); iterator.hasNext();) {
			GeopistaLayer geopistaLayer = (GeopistaLayer) iterator.next();

			if(idCapa.equals(geopistaLayer.getName())){
				featureCollection = geopistaLayer.getFeatureCollectionWrapper();
				xmlSLD = ((SLDStyleImpl)geopistaLayer.getStyle(SLDStyle.class)).getSLD();	
			}
		}
		
		return xmlSLD;
	}
	
	public void setSLD(String idCapa, String XML){
		FeatureCollectionWrapper featureCollection = null;
		for (Iterator iterator = context.getLayerManager().getLayers().iterator(); iterator.hasNext();) {
			GeopistaLayer geopistaLayer = (GeopistaLayer) iterator.next();

			if(idCapa.equals(geopistaLayer.getName())){
				featureCollection = geopistaLayer.getFeatureCollectionWrapper();
				SLDStyleImpl imp = ((SLDStyleImpl)geopistaLayer.getStyle(SLDStyle.class));
				
				String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><StyledLayerDescriptor version=\"1.0.0\" xmlns=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><NamedLayer><Name>vias</Name><UserStyle><Name>vias:_:default:vias</Name><Title>vias:_:vias:_:default:vias</Title><Abstract>vias:_:vias:_:default:vias</Abstract><FeatureTypeStyle><Name>vias</Name><Rule><Name>default</Name><MinScaleDenominator>0.0</MinScaleDenominator><MaxScaleDenominator>9.0E99</MaxScaleDenominator><LineSymbolizer><Stroke><CssParameter name='stroke'>#cc0033</CssParameter><CssParameter name='stroke-linejoin'>mitre</CssParameter><CssParameter name='stroke-opacity'>1.0</CssParameter><CssParameter name='stroke-width'><ogc:Literal>1.0</ogc:Literal></CssParameter><CssParameter name='stroke-linecap'>square</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
					
				
				StyledLayerDescriptor sld = null;
				try {
					sld = SLDFactory.createSLD(xml);
				} catch (XMLParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Collections.addAll(imp.getStyles(), ((NamedLayer)sld.getNamedLayers()[0]).getStyles());

				imp.setCurrentStyleName(((NamedLayer)sld.getNamedLayers()[0]).getStyles()[0].getName());
			}
		}	
	}
	
	public void nuevaCapa(String nombreCapa){
		
		Collection selectedCategories = context.getLayerNamePanel().getSelectedCategories();
		
		GeopistaLayer layer = new GeopistaLayer(nombreCapa, context.getLayerManager()
	                .generateLayerFillColor(), createBlankFeatureCollection(), context
	                .getLayerManager());
		
		  
	     // Como es una capa creada localmente asignamos el flag isLocal a true
        layer.setLocal(true);

        context.getLayerManager().addLayer(
                selectedCategories.isEmpty() ? StandardCategoryNames.WORKING
                        : selectedCategories.iterator().next().toString(), layer);

        ((EditingPlugIn) context.getWorkbenchContext().getBlackboard().get(
	                EditingPlugIn.KEY)).getToolbox(context.getWorkbenchContext()).setVisible(
	                true);
		  
		  
	}
	
	 private static FeatureCollection createBlankFeatureCollection()
	    {
	        GeopistaSchema featureSchema = new GeopistaSchema();

	        Domain domainGeometry = new StringDomain("", "");
	        Table tableGeometry = new Table("Dummy", "Dummy");
	        Column columnGeometry = new Column("Dummy", "Dummy", tableGeometry,
	                domainGeometry);
	        featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY, columnGeometry,
	                GeopistaSchema.READ_WRITE);

	        return new FeatureDataset(featureSchema);
	    }
}
