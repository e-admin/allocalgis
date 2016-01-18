/**
 * 
 */
package com.geopista.ui.plugin.geometrytousepoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.geometrytousepoint.images.IconLoader;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;

/**
 * @author seilagamo
 * 
 */
public class GeometryToUsePointPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
//    private String toolBarCategory = "GeometryToUsePointPlugIn.category";
    
    public GeometryToUsePointPlugIn() {
        Locale loc = Locale.getDefault();
        ResourceBundle bundle2 = ResourceBundle.getBundle(
                "com.geopista.ui.plugin.geometrytousepoint.languages.GeometryToUsePointi18n", loc,
                this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("GeometryToUsePoint", bundle2);
    }
    
    
    public void initialize(PlugInContext context) throws Exception {
        
        String pluginCategory = aplicacion.getString("toolBarAnalisys");
        //FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        context.getWorkbenchContext().getIWorkbench().getGuiComponent().getToolBar(pluginCategory)
                .addPlugIn(this.getIcon(), this, createEnableCheck(context.getWorkbenchContext()), 
                        context.getWorkbenchContext());
    }
    
    /**
     * Método que establece las condiciones para que se vea el plugin
     * 
     * @param workbenchContext
     * @return
     */
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {

        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck()).add(
                checkFactory.createAtLeastNLayersMustBeSelectedCheck(1));
    }

    public Icon getIcon() {
        return IconLoader.icon("GeometryToUsePoint.png");
    }

    public String getName() {
        String name = I18N.get("GeometryToUsePoint", "geometryToUsePointPlugIn");
        return name;
    }

    public boolean execute(PlugInContext context) throws Exception {
        
        GeometryToUsePointDialog geometryToUsePointDialog = new GeometryToUsePointDialog(context);
        if (context.getLayerViewPanel() != null) {
            context.getLayerViewPanel().repaint();
        }
        return geometryToUsePointDialog.wasOKPressed();
    }
    
    /**
     * Realiza el procesamiento que consume tiempo
     */
    public void run(final TaskMonitor monitor, PlugInContext context) throws Exception {
        final PlugInContext localPluginContext = context;
        final Hashtable layersFeatures= new Hashtable();
        
        final boolean todasEntidadesSeleccionadas = localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToUsePointUsePanel.CHECKTODOSELEMENTOS, true);        
        final Layer layerOrigen = (Layer) localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToUsePointLayerPanel.SELECTEDSOURCELAYER);
        final Layer layerDestino = (Layer) localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToUsePointLayerPanel.SELECTEDTARGETLAYER);
        final String atributoDestino = (String) localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToUsePointLayerPanel.SELECTEDCAMPODESTINO); 
        final String codigoUso = (String) localPluginContext.getWorkbenchGuiComponent()
                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                        GeometryToUsePointUsePanel.TEXTCODIGOUSO, "");
        
        
        execute(new UndoableCommand(getName()) {

            public void execute() {
                // Averiguamos si había elementos seleccionados en la capa
            	HashMap hsAreas = new HashMap();
                boolean tieneEntidadesSeleccionadas = false;
                Collection featuresSeleccionadas = localPluginContext.getWorkbenchContext()
                        .getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(
                                layerOrigen);

                Iterator featuresSeleccionadasIter = featuresSeleccionadas.iterator();
                if (featuresSeleccionadasIter.hasNext()) { // Esto es que tiene al
                                                           // menos una feature seleccionada
                    tieneEntidadesSeleccionadas = true;
                }
                Hashtable newGeometries = null;
                if (tieneEntidadesSeleccionadas) { // Si tiene entidades seleccionadas
                    
                	ArrayList lstFeaturesSelected = new ArrayList();
                	for (Iterator fsi = featuresSeleccionadasIter; fsi.hasNext(); ) {
                		lstFeaturesSelected.add((Feature)fsi.next());
                	}   	
                	
                	ArrayList lst = intersectionGeometries(lstFeaturesSelected);
                	ArrayList<Feature> lstFeatures = new ArrayList();
                	for (Iterator it = lst.iterator(); it.hasNext(); ) {
                		Feature faux = (Feature)it.next();
                		lstFeatures.add(faux);
                		System.out.println("--"+faux.getGeometry().getArea());
                	}
                	
                	
                	Collections.sort(lstFeatures, new Comparator() {  
                		  
                        public int compare(Object o1, Object o2) {  
                        	Geometry g1 = ((Feature) o1).getGeometry();  
                        	Geometry g2 = ((Feature) o2).getGeometry();  
                            double area1 = g1.getArea();  
                            double area2 = g2.getArea();  
              
                            if (area1 > area2) {  
                                return -1;  
                            } else if (area1 < area2) {  
                                return 1;  
                            } else {  
                                return 0;  
                            }  
                        }  
                    });  
                	
                	ArrayList lstFeatureSort = new ArrayList();
	            	 for (Feature feature : lstFeatures) {  
	            		 lstFeatureSort.add(feature);  
//	            		 System.out.println("-ordenada-"+feature.getGeometry().getArea());
	                 }  
                		            	 
	            	 for(int h=0; h<lstFeatureSort.size(); h++){
	            		 Feature fh= (Feature)lstFeatureSort.get(h);
//	            		 System.out.println("tabajando="+fh.getGeometry().getArea());
	            		 ArrayList lstContenidas = devolverContenidas(fh, lstFeatureSort);
	            		 ArrayList featuresEliminar = new ArrayList();
	            		 for (Iterator iterator = lstContenidas.iterator(); iterator.hasNext();) {
	            			 Feature valueFeature = (Feature) iterator.next();
	            			
	            			 ArrayList featuresEliminarFeature = devolverContenidas(valueFeature, lstContenidas);
	            			 featuresEliminar.addAll(featuresEliminarFeature);
	            		 }
	            		 
	            		 for(int t=0; t<featuresEliminar.size(); t++){
//	            			 System.out.println("eliminadas="+((Feature)featuresEliminar.get(t)).getGeometry().getArea());
	            			 lstContenidas.remove((Feature)featuresEliminar.get(t));
	            		 }
	            		 
	            		 
	            		 Double areaRestar = new Double(0);
	            		 for(int t=0; t<lstContenidas.size(); t++){
	            			 areaRestar += ((Feature)lstContenidas.get(t)).getGeometry().getArea();
//	            			 System.out.println("-quedan="+((Feature)lstContenidas.get(t)).getGeometry().getArea());
	            		 }
	            		
	            		 Double areaTotal = fh.getGeometry().getArea() - areaRestar;
	            		 hsAreas.put(fh, areaTotal.intValue());
	            	 }

	            	 newGeometries = convertGeometryToPoint(monitor, lstFeatureSort, layerOrigen);
                	
                	
//                    newGeometries = convertGeometryToPoint(monitor, new ArrayList(
//                            featuresSeleccionadas), layerOrigen);
                } else if (!tieneEntidadesSeleccionadas) { // Si no tiene ninguna entonces
                                                           // se cogen todas las de la capa

                	ArrayList lstFeaturesLayer = new ArrayList();
                	FeatureCollection fcA = layerOrigen.getFeatureCollectionWrapper();
                	 for (Iterator ia = fcA.iterator(); ia.hasNext(); ) {
                		 lstFeaturesLayer.add((Feature)ia.next());
                	}
                	
                	ArrayList lst = intersectionGeometries(lstFeaturesLayer);
                 	ArrayList<Feature> lstFeatures = new ArrayList();
                 	for (Iterator it = lst.iterator(); it.hasNext(); ) {
                 		Feature faux = (Feature)it.next();
                 		lstFeatures.add(faux);
                 		System.out.println("--"+faux.getGeometry().getArea());
                 	}
                	
                	
                 	Collections.sort(lstFeatures, new Comparator() {  
              		  
                        public int compare(Object o1, Object o2) {  
                        	Geometry g1 = ((Feature) o1).getGeometry();  
                        	Geometry g2 = ((Feature) o2).getGeometry();  
                            double area1 = g1.getArea();  
                            double area2 = g2.getArea();  
              
                            if (area1 > area2) {  
                                return -1;  
                            } else if (area1 < area2) {  
                                return 1;  
                            } else {  
                                return 0;  
                            }  
                        }  
                    });  
                	
                	ArrayList lstFeatureSort = new ArrayList();
                	for (Feature feature : lstFeatures) {  
                		lstFeatureSort.add(feature);  
                		System.out.println("-ordenada-"+feature.getGeometry().getArea());
                	}  
	            	 
                	for(int h=0; h<lstFeatureSort.size(); h++){
	            		 Feature fh= (Feature)lstFeatureSort.get(h);
	            		 System.out.println("tabajando="+fh.getGeometry().getArea());
	            		 ArrayList lstContenidas = devolverContenidas(fh, lstFeatureSort);
	            		 ArrayList featuresEliminar = new ArrayList();
	            		 for (Iterator iterator = lstContenidas.iterator(); iterator.hasNext();) {
	            			 Feature valueFeature = (Feature) iterator.next();
	            			
	            			 ArrayList featuresEliminarFeature = devolverContenidas(valueFeature, lstContenidas);
	            			 featuresEliminar.addAll(featuresEliminarFeature);
	            		 }
	            		 
	            		 for(int t=0; t<featuresEliminar.size(); t++){
	            			 System.out.println("eliminadas="+((Feature)featuresEliminar.get(t)).getGeometry().getArea());
	            			 lstContenidas.remove((Feature)featuresEliminar.get(t));
	            		 }
	            		 
	            		 
	            		 Double areaRestar = new Double(0);
	            		 for(int t=0; t<lstContenidas.size(); t++){
	            			 areaRestar += ((Feature)lstContenidas.get(t)).getGeometry().getArea();
	            			 System.out.println("-quedan="+((Feature)lstContenidas.get(t)).getGeometry().getArea());
	            		 }
	            		
	            		 Double areaTotal = fh.getGeometry().getArea() - areaRestar;
	            		 hsAreas.put(fh, areaTotal.intValue());
	            	 
	            	 
	            	 
                	}
                	newGeometries = convertGeometryToPoint(monitor, lstFeatureSort, layerOrigen);
                
                	
                	
//                    List allLayerFeatures = layerOrigen.getFeatureCollectionWrapper().getFeatures();
//                    newGeometries = convertGeometryToPoint(monitor, allLayerFeatures, layerOrigen);
                }

                FeatureSchema featureSchemaTarget = layerDestino.getFeatureCollectionWrapper()
                        .getFeatureSchema();
                Enumeration enumeration = newGeometries.keys();
                FeatureDataset fc = new FeatureDataset(featureSchemaTarget);
                String codigoUsoLocal = codigoUso;
                while (enumeration.hasMoreElements()) {
                    Feature f = (Feature) enumeration.nextElement();
                    Geometry geom = (Geometry) newGeometries.get(f);
                    if (!todasEntidadesSeleccionadas) {
                        codigoUsoLocal = (String) localPluginContext.getWorkbenchGuiComponent()
                                .getActiveTaskComponent().getLayerViewPanel().getBlackboard().get(
                                        Integer.toString(f.getID()), "");
                    }
                    if (geom != null) {
                        Feature fNew = new BasicFeature(fc.getFeatureSchema());
                        fNew.setGeometry(geom);
                        fNew.setAttribute(atributoDestino, codigoUsoLocal);
                        fc.add(fNew);
                    }
                }
                layersFeatures.put(layerDestino, fc.getFeatures());
                layerDestino.getFeatureCollectionWrapper().addAll(fc.getFeatures());
                try {
                    localPluginContext.getLayerViewPanel().getViewport().update();
                } catch (Exception e) {

                }
            }

            public void unexecute() {
                layerDestino.getFeatureCollectionWrapper().removeAll(
                        (List) layersFeatures.get(layerDestino));
                try {
                    localPluginContext.getLayerViewPanel().getViewport().update();
                } catch (Exception e) {

                }
            }
        }, context);
    }
    
    
    private ArrayList devolverContenidas(Feature ftrabajo, ArrayList lstFeatureSort){
    	
    	ArrayList lstFeaturesContenidas = new ArrayList();
    	for (int i=0; i<lstFeatureSort.size(); i++){
    		Feature feature = (Feature)lstFeatureSort.get(i);
    		if(ftrabajo != feature){
    			
    			if(ftrabajo.getGeometry().contains(feature.getGeometry())){
    				lstFeaturesContenidas.add(feature);
    			}
    		}
    		
    	}
    	return lstFeaturesContenidas;
    }
 
    private ArrayList intersectionGeometries(ArrayList lstFeatures){
    	ArrayList lst = new ArrayList();
    	try{
    		ArrayList lstGeometriasDEL = new ArrayList();
    		ArrayList lstGeometriasADD = new ArrayList();
    		for(int i=0; i<lstFeatures.size(); i++){
    			Feature fa = (Feature)lstFeatures.get(i);
    			if(fa.getGeometry().getGeometryType().equals("MultiPolygon")){
    				int dimension = fa.getGeometry().getDimension();
    	        	for(int h=0; h<dimension; h++){
    	        		Geometry g = fa.getGeometry().getGeometryN(h);
    	        		Feature fb = (Feature)fa.clone();
    	        		fb.setGeometry(g);
    	        		lstGeometriasADD.add(fb);
    	        		if(!lstGeometriasDEL.contains(fa)){
    	        			lstGeometriasDEL.add(fa);
    	        		}
    	        	}
    			}
    		}
    		lstFeatures.removeAll(lstGeometriasDEL);
    		lstFeatures.addAll(lstGeometriasADD);
    		
    		boolean intersections = false;
	    	HashMap hm = new HashMap();
	    	ArrayList lstFeaturesEliminar = new ArrayList ();
	    	ArrayList lstNew = new ArrayList();
	    	for(int i=0; i<lstFeatures.size(); i++){
	    		
	    		Feature fa = (Feature)lstFeatures.get(i);
	    		Geometry ga = convertGeometryToPolygon(fa.getGeometry());
	    		
	    		if (ga != null){
	    			
	    			for(int j=(i+1); j<lstFeatures.size() && !intersections; j++){
	    				
		    			Feature fb= (Feature)lstFeatures.get(j);
		    			Geometry gb = convertGeometryToPolygon(fb.getGeometry());
		    			
		    			if(gb != null){
		    				
			    			if(!ga.contains(gb) && !gb.contains(ga)){
			    				
			    				if(ga.intersects(gb)){
			    					Geometry intersect = ga.intersection(gb);
			    					if(!intersect.getGeometryType().equals("Polygon") &&
			    							!intersect.getGeometryType().equals("MultiPolygon")){
			    						continue;
			    					}
			    					Geometry differenceab = ga.difference(gb);
			    					Geometry differenceba = gb.difference(ga);
			    					
			    					Feature fIntersect = (Feature)fa.clone();
			    					fIntersect.setGeometry(intersect);
			    					Feature fDiferenceab = (Feature)fa.clone();
			    					fDiferenceab.setGeometry(differenceab);
			    					Feature fDiferenceba = (Feature)fa.clone();
			    					fDiferenceba.setGeometry(differenceba);
			    					intersections = true;
			    					lstNew.add(fIntersect);
			    					lstNew.add(fDiferenceab);
			    					lstNew.add(fDiferenceba);
			    					
			    					lstFeaturesEliminar.add(fa);
			    					lstFeaturesEliminar.add(fb);
			    				}
		    				  				
			    				
			    			}

		    			}
		        	}
	    			lstNew.add(fa);

	    		}
	    	}
	    	
	    	if (!lstFeaturesEliminar.isEmpty()){
		    	for(int i=0; i<lstFeaturesEliminar.size(); i++){
		    		lstNew.remove((Feature)lstFeaturesEliminar.get(i));
		    	}
	    	}

	    	if (intersections){
	    		lst = intersectionGeometries (lstNew);
	    	}
	    	if(lst.isEmpty()){
	    		lst = lstNew;
	    	}
	    	
    	}
    	catch (Exception e) {
    		e.printStackTrace();
			// TODO: handle exception
		}
    	return lst;
    	
    }
    
    /**
     * Convierte la geometria a un poligono.
     * @param geo
     * @return
     */
    private Geometry convertGeometryToPolygon(Geometry geo){
    	
    	Geometry geom = null;  
        if(geo.getGeometryType().equals("Polygon")){
        	geom = geo;  
        }
        else if(geo.getGeometryType().equals("LineString")){
        	Coordinate [] coordinate = geo.getCoordinates(); 
        	try{
        		LinearRing linearRing = new GeometryFactory().createLinearRing(coordinate);
        		LinearRing[] lrs = new LinearRing[0];

        		ArrayList innerBoundaries = new ArrayList();
        		lrs = (LinearRing[]) innerBoundaries.toArray(lrs);
        		Polygon polygon =  new GeometryFactory().createPolygon(linearRing, lrs);
        		geom = (Geometry)polygon;
        	}
        	catch (Exception e) {
				// La geometria no puede ser tratada como un poligono
        		return null;
			}
            
           
        }
    	return geom;
    }
    
    /**
     * Calcula todos los centroides de las features y los devuelve en un Hashtable
     * @param monitor
     * @param allLayerFeatures
     * @param currentLayer
     * @return Un HashTable que contiene los centroides
     */
    private Hashtable convertGeometryToPoint(TaskMonitor monitor, List allLayerFeatures,
            Layer currentLayer) {

        monitor.allowCancellationRequests();
        monitor.report(I18N.get("GeometryToUsePoint", "geometryToUsePointPlugIn.CalculandoCentroides"));

        Hashtable newGeometries = new Hashtable();

        int size = allLayerFeatures.size();
        int count = 1;

        for (Iterator i = allLayerFeatures.iterator(); i.hasNext();) {
            monitor.report(count++, size, I18N.get("GeometryToUsePoint", "geometryToUsePointPlugIn.features"));

            Feature f = (Feature) i.next();
            Geometry geom = f.getGeometry();
            if (geom.getCentroid() != null) {
                newGeometries.put(f, geom.getCentroid());
            }
        }

        return newGeometries;

    }
}
