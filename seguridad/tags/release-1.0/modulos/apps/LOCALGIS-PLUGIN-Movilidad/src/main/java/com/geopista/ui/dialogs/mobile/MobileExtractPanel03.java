package com.geopista.ui.dialogs.mobile;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
 
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.feature.Column;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.beans.CheckBoxNode;
import com.geopista.ui.dialogs.beans.CheckBoxNodeEditor;
import com.geopista.ui.dialogs.beans.CheckBoxNodeRenderer;
import com.geopista.ui.dialogs.beans.SortedFeature;
import com.geopista.ui.dialogs.eiel.EIELFilesUtils;
import com.geopista.ui.dialogs.global.Constants;
import com.geopista.ui.dialogs.global.Utils;
import com.geopista.ui.plugin.mobile.MobileExtractPlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geostaf.ui.plugin.generate.GraticuleCreatorEngine;
import com.geostaf.ui.plugin.generate.GraticuleCreatorPlugIn;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollectionWrapper;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.IViewport;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.Viewport;
import com.vividsolutions.jump.workbench.ui.renderer.style.LabelStyle;


public class MobileExtractPanel03  extends JPanel implements WizardPanel
{
	private static Logger log = Logger.getLogger(MobileExtractPanel03.class);  
	private static final String GEOMETRY_ATR = "geometry";
    private Blackboard blackboard  = Constants.APLICACION.getBlackboard();
    private String localId = null;
    private WizardContext wizardContext;
    private PlugInContext context;
    private JButton jButtonGraticule;
    //capas desconectadas con permiso de modificación
    private JTree writeableAtributesTree;
    private NamedVector[] writeableRootNodes;
    public static final String MOBILE_WRITEABLE_LAYERS_ATRIB = "MOBILE_WRITEABLE_LAYERS_ATRIB";
	private JScrollPane writeableLayerScrollPane = null;
    //capas desconectadas con permiso de lectura
    private JTree readableAtributesTree;
    private NamedVector[] readableRootNodes;
    public static final String MOBILE_READABLE_LAYERS_ATRIB = "MOBILE_READABLE_LAYERS_ATRIB";
	private JScrollPane readableLayerScrollPane = null;
    
	public static final String MOBILE_PROJECT_NAME = "MOBILE_PROJECT_NAME";
	
    /**
     * @return null to turn the Next button into a Finish button
     */
    
    private String nextID = null;
	private boolean bCuadriculaCreada;

    public MobileExtractPanel03 (String id, String nextId, PlugInContext context)
    {
        this.nextID = nextId;
        this.localId = id;
        this.context = context; 
        writeableAtributesTree = null;
        readableAtributesTree = null;
        bCuadriculaCreada = false;
    }
    
	  public void enteredFromLeft(Map dataMap)
	  {
	      if(!Constants.APLICACION.isLogged())
	      {
	    	  Constants.APLICACION.login();
	      }
	      if(!Constants.APLICACION.isLogged())
	      {
	          wizardContext.cancelWizard();
	          return;
	      }
	      
	      writeableLayerScrollPane=null;
	      readableLayerScrollPane=null;
	      
	      try
	      {
	           jbInit();
	      }
	      catch(Exception e)
	      {
	          e.printStackTrace();
	      }
	  }
    
    private void jbInit() throws Exception
    {
    	this.removeAll();
    	this.setLayout(new GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.ipadx = -21;
        gridBagConstraints1.ipady = 141;
        gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
        java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);
        java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 3;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.weighty = 1.0;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints3.ipadx = -21;
        gridBagConstraints3.ipady = 141;
        gridBagConstraints3.insets = new java.awt.Insets(5,5,5,5);
        
        java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 0;

        
        
        String projectName=(String)blackboard.get(MOBILE_PROJECT_NAME);
        JLabel extractionProjectNameLabel = new JLabel(projectName);       
        
        this.add(extractionProjectNameLabel, gridBagConstraints4); 
        
        this.add(getWriteableLayerScrollPane(), gridBagConstraints1);
        this.add(getReadableLayerScrollPane(), gridBagConstraints3); 
        this.add(getGraticuleButton(), gridBagConstraints2);
    }
    

	private JButton getGraticuleButton() {
		jButtonGraticule = new JButton(I18N.get(MobileExtractPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileExtractPanel03_crearCuadricula));
		
		LayerManager layerManager = context.getLayerManager();
		Layer graticuleLayer = layerManager.getLayer(GraticuleCreatorEngine.getGraticuleName());
		
		//si ya existe una capa cuadrícula significa que se trata de un fichero importado previamente a ejecutar el asistente
		if(graticuleLayer!=null){
			jButtonGraticule.setEnabled(false);
			
			if(bCuadriculaCreada == true){
				//solo ejecutamos este código una vez
				return jButtonGraticule;
			}

			FeatureCollectionWrapper graticuleCollectionWrapper = graticuleLayer.getFeatureCollectionWrapper();
			FeatureSchema featureSchema = graticuleCollectionWrapper.getFeatureSchema();
			
		    //cuadriculas visionadas
		    IViewport viewport = (IViewport) context.getLayerViewPanel().getViewport();
			Envelope viewEnvelope = viewport.getEnvelopeInModelCoordinates();
			List<Feature> graticuleFeatures = graticuleCollectionWrapper.query(viewEnvelope);
			//ordenamos por distancia al origen las features
			List<SortedFeature> sortedGraticuleFeatures = new ArrayList<SortedFeature>();
			Feature feature = null;
			SortedFeature sortFeat = null;
			for (Iterator iterator = graticuleFeatures.iterator(); iterator.hasNext();) {
				feature = (Feature) iterator.next();
				sortFeat = new SortedFeature(feature);
				sortedGraticuleFeatures.add(sortFeat);
			}
			Collections.sort(sortedGraticuleFeatures);
			
			//borramos el resto de cuadrículas
			graticuleCollectionWrapper.clear();
			graticuleCollectionWrapper.addAll(graticuleFeatures);
			Envelope newWrapperEnvelope = graticuleCollectionWrapper.getEnvelope();
			
			//zoom al envelope actual
			try {
				viewport.zoom(newWrapperEnvelope);
			} catch (NoninvertibleTransformException e) {
				log.warn("No se ha podido alcanzar el zoom " +newWrapperEnvelope);
			}
	        
	        //ajustamos la ventana al envelope de la cuadrícula
	        resizeViewToEnvelope(context, newWrapperEnvelope, viewport);
	        
			//guardamos las propiedades gráficas de la extracción
			Envelope featEnvelope = sortedGraticuleFeatures.get(0).getFeature().getGeometry().getEnvelopeInternal();
			Double featWidth = featEnvelope.getWidth();
			Double featHeight = featEnvelope.getHeight();
			Double minX = newWrapperEnvelope.getMinX();
			Double minY = newWrapperEnvelope.getMinY();
			Coordinate coordCorner = new Coordinate(minX, minY); 
			blackboard.put(GraticuleCreatorPlugIn.SOUTHWEST_CORNER_OF_LEFT_LAYER, coordCorner);
			blackboard.put(GraticuleCreatorPlugIn.CELL_SIDE_LENGTH_X, featWidth);
			blackboard.put(GraticuleCreatorPlugIn.CELL_SIDE_LENGTH_Y, featHeight);
			Integer numCeldasAncho = (int) (newWrapperEnvelope.getWidth() / featWidth);
			Integer numCeldasAlto = (int) (newWrapperEnvelope.getHeight() / featHeight);
			blackboard.put(GraticuleCreatorPlugIn.LAYER_WIDTH_IN_CELLS, numCeldasAncho);
			blackboard.put(GraticuleCreatorPlugIn.LAYER_HEIGHT_IN_CELLS, numCeldasAlto);
	        
			//creamos celdas para los espacios en blanco
			if(sortedGraticuleFeatures.size() < numCeldasAncho*numCeldasAlto){
				Double curMinX = minX;
				Double curMinY = minY;
				Double fMinX = 0.0;
				GeometryFactory geoFact = new GeometryFactory();
				Feature newFeature = null;
				for(int i=0; i<sortedGraticuleFeatures.size(); i++){
					sortFeat = sortedGraticuleFeatures.get(i);
					feature = sortFeat.getFeature();
					fMinX = feature.getGeometry().getEnvelopeInternal().getMinX();
					if(curMinX.doubleValue()!=fMinX.doubleValue()){
						//creamos la feature
						newFeature = (Feature) feature.clone();
						Coordinate[] coordArray = {new Coordinate(curMinX, curMinY), new Coordinate(curMinX+featWidth, curMinY),
								new Coordinate(curMinX+featWidth, curMinY+featHeight), new Coordinate(curMinX, curMinY+featHeight),  
								new Coordinate(curMinX, curMinY)};
						
						newFeature.setGeometry(geoFact.createPolygon(geoFact.createLinearRing(coordArray), null));
						sortedGraticuleFeatures.add(i, new SortedFeature(newFeature));
						graticuleCollectionWrapper.add(newFeature);
					}
					//para lo último
					else if((i==sortedGraticuleFeatures.size()-1) && ((minX+(featWidth*numCeldasAncho))!=(fMinX+featWidth))){
						curMinX = fMinX+featWidth;
						newFeature = (Feature) feature.clone();
						Coordinate[] coordArray = {new Coordinate(curMinX, curMinY), new Coordinate(curMinX+featWidth, curMinY),
								new Coordinate(curMinX+featWidth, curMinY+featHeight), new Coordinate(curMinX, curMinY+featHeight),  
								new Coordinate(curMinX, curMinY)};
						
						newFeature.setGeometry(geoFact.createPolygon(geoFact.createLinearRing(coordArray), null));
						sortedGraticuleFeatures.add(sortedGraticuleFeatures.size(), new SortedFeature(newFeature));
						graticuleCollectionWrapper.add(newFeature);
						continue;
					}
					
					if(((i+1)%numCeldasAncho)!=0){
						curMinX = curMinX+featWidth;
					} else { //ultima celda de cada fila
						curMinX = minX;
						curMinY += featHeight; 
					}
				}  
			}
	        
			//añadimos el atributo cellid
			featureSchema.addAttribute(GraticuleCreatorEngine.ATR_CELL_ID, AttributeType.INTEGER);
			int k=1;
			Object[] featAttribs = null;
			Object[] newFeatAttribs = null;
			for (Iterator iterator = sortedGraticuleFeatures.iterator(); iterator.hasNext();k++) {
				feature = ((SortedFeature) iterator.next()).getFeature();
				featAttribs = feature.getAttributes();
				newFeatAttribs = new Object[featAttribs.length+1];
				for (int i = 0; i < featAttribs.length; i++) {
					newFeatAttribs[i] = featAttribs[i];
				}
				newFeatAttribs[newFeatAttribs.length-1] = k;
				feature.setAttributes(newFeatAttribs);
			}
			//pintamos el atributo en la capa
	        LabelStyle labelStyle = graticuleLayer.getLabelStyle();
	        labelStyle.setColor(Color.RED);
	        labelStyle.setHeight(labelStyle.getHeight() * 4);
	        labelStyle.setAttribute(GraticuleCreatorEngine.ATR_CELL_ID);
	        labelStyle.setEnabled(true);
	        
			//evitamos que se modifiquen las cuadrículas
			graticuleLayer.setEditable(false);
			graticuleLayer.fireAppearanceChanged();
			
			bCuadriculaCreada = true;
		}
		//llamada al graticulePlugin
		else {
			jButtonGraticule.addActionListener(new ActionListener(){
	
				public void actionPerformed(ActionEvent e) {
			    	GraticuleCreatorPlugIn graticulePlugin = new GraticuleCreatorPlugIn();
			    	try {
						graticulePlugin.execute(context);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					wizardContext.inputChanged(); //indicamos que ya se puede habilitar el boton
				}
				
			});
		}
		
		return jButtonGraticule;
	}
	
	/**
	 * Ajusta el tamaño de la ventana al envelope pasado como parámetro
	 * @param ctx
	 * @param newWrapperEnvelope
	 * @param viewport
	 */
	public static void resizeViewToEnvelope(PlugInContext ctx, Envelope newWrapperEnvelope, IViewport viewport){
        //ajustamos las proporciones de la ventana a las de la feature
		JInternalFrame activeInternalFrame = ctx.getActiveInternalFrame();
		Dimension dimView=((Dimension) ctx.getLayerViewPanel()).getSize();
		Double widthView = dimView.getWidth();
		Double heightView = dimView.getHeight();
		int widthDiff = (int) (activeInternalFrame.getWidth() - widthView);
		int heightDiff = (int) (activeInternalFrame.getHeight() - heightView);
		Integer newWidth = (int) ((newWrapperEnvelope.getWidth() / viewport.getEnvelopeInModelCoordinates().getWidth()) * widthView);
		Integer newHeight = (int) ((newWrapperEnvelope.getHeight() / viewport.getEnvelopeInModelCoordinates().getHeight()) * heightView);
        Dimension newDimensionView = new Dimension(newWidth + widthDiff, newHeight + heightDiff);
		activeInternalFrame.setSize(newDimensionView);        
		//zoom al envelope actual
		try {
			viewport.zoom(newWrapperEnvelope);
		} catch (NoninvertibleTransformException e) {
			log.warn("No se ha podido alcanzar el zoom " +newWrapperEnvelope);
		}
	}
    
	/**
	 * This method initializes availableLayerScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getWriteableLayerScrollPane() {
		if (writeableLayerScrollPane == null) {
			writeableLayerScrollPane = new JScrollPane();
			writeableAtributesTree = getWriteableLayerTree();
			writeableLayerScrollPane.setViewportView(writeableAtributesTree);
			writeableLayerScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return writeableLayerScrollPane;
	}
	/**
	 * Retorna un árbol con las capas seleccionadas y sus atributos
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JTree getWriteableLayerTree() {
		ArrayList<GeopistaLayer> extractLayers = (ArrayList<GeopistaLayer>) blackboard.get(MobileExtractPanel01.MOBILE_WRITEABLE_LAYERS);
		writeableRootNodes = getRootLayerNodes(extractLayers);
		Vector rootVector = new NamedVector(I18N.get(MobileExtractPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileExtractPanel03_capasEdit), writeableRootNodes);
		JTree tree = new JTree(rootVector);
		tree.setFont(new Font(tree.getFont().getName(), Font.BOLD, tree.getFont().getSize()));
		CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer(Color.BLUE);
		tree.setCellRenderer(renderer);
		tree.setCellEditor(new CheckBoxNodeEditor(tree));
		tree.setEditable(true);
		
//		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Capas");
//	    JTree tree = new JTree(root);
//	    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
//	    DefaultMutableTreeNode chosen = null;
//	    if(extractLayers!=null){
//			for (int i = 0; i < extractLayers.size(); i++) {
//				geopistaLayer = extractLayers.get(i);
//				chosen = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//				if (chosen == null){
//					chosen = root;
//				}
//				model.insertNodeInto(getTreeNodeFromLayer(geopistaLayer), chosen, 0);
//			}
//	    }
		
		return tree;
	}
	
	/**
	 * This method initializes availableLayerScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getReadableLayerScrollPane() {
		if (readableLayerScrollPane == null) {
			readableLayerScrollPane = new JScrollPane();
			readableAtributesTree = getReadableLayerTree();
			readableLayerScrollPane.setViewportView(readableAtributesTree);
			readableLayerScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return readableLayerScrollPane;
	}
	/**
	 * Retorna un árbol con las capas seleccionadas y sus atributos
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JTree getReadableLayerTree() {
		ArrayList<GeopistaLayer> extractLayers = (ArrayList<GeopistaLayer>) blackboard.get(MobileExtractPanel01.MOBILE_READABLE_LAYERS);
		readableRootNodes = getRootLayerNodes(extractLayers);
		Vector rootVector = new NamedVector(I18N.get(MobileExtractPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileExtractPanel03_capasLect), readableRootNodes);
		JTree tree = new JTree(rootVector);
		//tree.setFont(new Font(tree.getFont().getName(), Font.BOLD, tree.getFont().getSize()));
		CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer(Color.RED);
		tree.setCellRenderer(renderer);
		tree.setCellEditor(new CheckBoxNodeEditor(tree));
		tree.setEditable(true);
		return tree;
	}
	
	/**
	 * Añade todos los atributos de las capas seleccionadas
	 * @param extractLayers
	 * @return
	 */
	private NamedVector[] getRootLayerNodes(ArrayList<GeopistaLayer> extractLayers) {
		NamedVector[] returnVector = null;
		if(extractLayers!=null){
			GeopistaLayer geopistaLayer = null;
			String layerSystemId;
			EIELFilesUtils eielFilesUtils;
			String layerName;
			returnVector = new NamedVector[extractLayers.size()];
			//capas
			for (int i = 0; i < extractLayers.size(); i++) {
				geopistaLayer = extractLayers.get(i);				
				layerSystemId = geopistaLayer.getSystemId();
				eielFilesUtils = new EIELFilesUtils(layerSystemId);
				layerName = geopistaLayer.getName();
				
				//buscamos el identificador de cada capa
                GeopistaSchema featureGeoSchema = (GeopistaSchema) geopistaLayer.getFeatureCollectionWrapper().getFeatureSchema();
				//atributos
				NamedVector namedVector = new NamedVector(layerName);
				Column columnByAttribute = null;
				String colName = null;
				for (int j = 0; j < featureGeoSchema.getAttributeCount(); j++){
					columnByAttribute = featureGeoSchema.getColumnByAttribute(j);
					colName = columnByAttribute.getName();
					if(colName.toLowerCase().equals(GEOMETRY_ATR)){ //no lo mostramos
						continue;
					}
					//CAMBIAR: COMPROBAR SI SE DEBE SELECCIONAR id SI ES TIPO EIEL
					else if(colName.toLowerCase().equals(Constants.FIELD_ID) ){ //lo ponemos al principio 
						namedVector.add(0, new CheckBoxNode(featureGeoSchema.getAttributeName(j), true, false));
					}
					else if(colName.toLowerCase().equals(Constants.FIELD_ID_MUNICIPIO)){
						namedVector.add(1, new CheckBoxNode(featureGeoSchema.getAttributeName(j), true, false));
					}
					//caso especial para licencias
					//la referencia castastral será el atributo clave para localizar la licencia
					else if(colName.toLowerCase().contains(Constants.CLAVE_METAINFO) && Utils.isInArray(Constants.TIPOS_LICENCIAS, layerSystemId)){
						namedVector.add(new CheckBoxNode(featureGeoSchema.getAttributeName(j), true, false));
					}
					//caso especial para inventario de patrimonio
					else if(colName.toLowerCase().equals(Constants.NUM_BIENES_ID) && Utils.isInArray(Constants.TIPOS_INVENTARIO, layerSystemId)){
						namedVector.add(new CheckBoxNode(featureGeoSchema.getAttributeName(j), true, false));						
					}
					//caso especial para EIEL				
					else if(Utils.isInArray(Constants.TIPOS_EIEL, layerSystemId)){						
						if(isPkFieldEIEL(eielFilesUtils, layerSystemId, colName))
							namedVector.add(new CheckBoxNode(featureGeoSchema.getAttributeName(j), true, false));
						else			
							namedVector.add(new CheckBoxNode(featureGeoSchema.getAttributeName(j), false, true));
					}
					else { //simplemente lo añadimos
						//if(!(licUtils.isInArray(Constants.TIPOS_LICENCIAS, layerSystemId) || licUtils.isInArray(Constants.TIPOS_INVENTARIO, layerSystemId))){
						if(!Utils.isInArray(Constants.TIPOS_LICENCIAS, layerSystemId)){
							namedVector.add(new CheckBoxNode(featureGeoSchema.getAttributeName(j), false, true));
						}
					}
				}
				returnVector[i] = namedVector;
			}
		}
		
	    return returnVector;
	
	}
	
	private boolean isPkFieldEIEL(EIELFilesUtils eielFilesUtils, String layerSystemId, String colName){
		Iterator it = eielFilesUtils.getEIELLayerBean(layerSystemId).getRelacionFields().iterator();
		while(it.hasNext()){
			LCGCampoCapaTablaEIEL lcgCampoCapa = (LCGCampoCapaTablaEIEL) it.next();							
			if(lcgCampoCapa.getCampoBD().equals(colName.toLowerCase())){ 
				return true;
			}
		}
		return false;
	}

//	/**
//	 * Obtiene un nodo del árbol compuesto por el nombre de la capa y sus atributos
//	 * @param layer
//	 * @return
//	 */
//	private DefaultMutableTreeNode getTreeNodeFromLayer(GeopistaLayer layer) {
//	    DefaultMutableTreeNode r = new DefaultMutableTreeNode(layer.getName());
//	    FeatureSchema featureSchema = layer.getFeatureCollectionWrapper().getFeatureSchema();
//	    
//	    for (int i = 1; i < featureSchema.getAttributeCount(); i++){
//	      r.add((MutableTreeNode) new CheckBoxNode(featureSchema.getAttributeName(i), false));
//	    }
//	    
//	    return r;
//	}
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
    	//Recorremos el árbol y guardamos una lista con las capas y sus atributos seleccionados
    	Map<String, List<String>> tablaCapasAtributosEscritura = new HashMap<String, List<String>>();
    	if(writeableRootNodes.length>0){
    		tablaCapasAtributosEscritura =  getLayersAtributesList(writeableAtributesTree);
    	}
    	//guardamos los atributos seleccionados de escritura
	    blackboard.put(MOBILE_WRITEABLE_LAYERS_ATRIB, tablaCapasAtributosEscritura);
    	Map<String, List<String>> tablaCapasAtributosLectura = new HashMap<String, List<String>>();
    	if(readableRootNodes.length>0){
    		tablaCapasAtributosLectura = getLayersAtributesList(readableAtributesTree);
    	}
    	//guardamos los atributos seleccionados de escritura
	    blackboard.put(MOBILE_READABLE_LAYERS_ATRIB, tablaCapasAtributosLectura);
    }
    
    /**
     * Obtiene los nodos seleccionados de un jtree creando una hash de Capas/Atributos
     * @param jtreeAtributes
     * @return
     */
    private Map<String, List<String>> getLayersAtributesList(JTree jtreeAtributes){
    	Map<String, List<String>> tablaCapasAtributos = new HashMap<String, List<String>>();
    	List<String> listaAtr = null;
    	String nombreCapa = null;
    	String nombreAtributo = null;
	    DefaultTreeModel model = (DefaultTreeModel) jtreeAtributes.getModel();
	    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
	    DefaultMutableTreeNode chosen = (DefaultMutableTreeNode) root.getFirstChild();
	    DefaultMutableTreeNode leaf = null;
	    CheckBoxNode checkNode = null;
	    for (int i = 0; i < root.getChildCount(); i++) {
		    nombreCapa = chosen.toString();
			leaf = chosen.getFirstLeaf();
			for (int j = 0; j < chosen.getLeafCount(); j++) {
				checkNode = (CheckBoxNode) leaf.getUserObject();
				nombreAtributo = checkNode.getText();
				//testeamos si el atributo está seleccionado
				if(checkNode.isSelected()){
					if(tablaCapasAtributos.containsKey(nombreCapa)){
						listaAtr = tablaCapasAtributos.get(nombreCapa);
					}
					else {
						listaAtr = new ArrayList<String>();
					}
					listaAtr.add(nombreAtributo);
					tablaCapasAtributos.put(nombreCapa, listaAtr);
				}
				leaf = leaf.getNextLeaf();
			}
			//si no hay ningun atributo seleccionado guardamos unicamente la capa
			if(!tablaCapasAtributos.containsKey(nombreCapa)){
				tablaCapasAtributos.put(nombreCapa, null);
			}
			chosen = chosen.getNextSibling();
	    }
	    
	    return tablaCapasAtributos;
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     * @param listener a party to notify when the input changes (usually the
     * WizardDialog, which needs to know when to update the enabled state of
     * the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }
    
    public void remove(InputChangedListener listener)
    {
        
    }
    
    public String getTitle()
    {
        return this.getName();
    }
    
    public String getID()
    {
        return localId;
    }
    
    public String getInstructions()
    {
        return I18N.get(MobileExtractPlugin.PluginMobileExtracti18n,MobilePluginI18NResource.MobileExtractPanel03_selecAtrib);
    }
    
    public boolean isInputValid()
    {        
    	LayerManager layerManager = context.getLayerManager();
        Layer graticuleLayer = layerManager.getLayer(GraticuleCreatorEngine.getGraticuleName());
        if(graticuleLayer==null){
        	return false;
        }
        return true;
    }
    
    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }

    public void setNextID(String nextID)
    {
        this.nextID=nextID;
    }
    public String getNextID()
    {
        return nextID;
    }
       
    
    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
    	
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"  

