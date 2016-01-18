/*
 * Created on 12.01.2005
 *
 * CVS header information:
 *  $RCSfile: DelaunayFactory.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:55 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/Delaunay/DelaunayFactory.java,v $s
 */
package pirolPlugIns.utilities.Delaunay;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pirolPlugIns.i18n.PirolPlugInMessages;
import pirolPlugIns.utilities.Data2LayerConnector;
import pirolPlugIns.utilities.FeatureCollectionTools;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerAdapter;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * This class follows the sigleton pattern and provides a mapping between layers and their Delaunay graphs
 * 
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 *
 */
public class DelaunayFactory extends LayerAdapter {
    private static DelaunayFactory factory = null;
    
    private Map layer2InterimMapObject;
    
    
    
    private DelaunayFactory(){
        this.layer2InterimMapObject = Collections.synchronizedMap(new HashMap());
    }
    
    public static DelaunayFactory getInstance(){
        if (DelaunayFactory.factory==null){
            DelaunayFactory.factory = new DelaunayFactory();  
        }
        return DelaunayFactory.factory;
    }
    
    public DelaunayCalculator getDelaunayDiagrammFor( Layer layer, TaskMonitorDialog monitor, ErrorHandler handler ){
        return this.getDelaunayDiagrammFor(layer, layer.getFeatureCollectionWrapper().getEnvelope(), FeatureCollectionTools.FeatureCollection2FeatureArray(layer.getFeatureCollectionWrapper().getUltimateWrappee()), monitor, handler);
    }
    
    public DelaunayCalculator getDelaunayDiagrammFor( Layer layer, Envelope env2Use, Feature[] featuresToUse, TaskMonitorDialog monitor, ErrorHandler handler ){
        if ( this.layer2InterimMapObject.containsKey(layer) && ((DelaunayInterimMapObject)this.layer2InterimMapObject.get(layer)).containsKey(env2Use) ){
            return ((DelaunayInterimMapObject)this.layer2InterimMapObject.get(layer)).get(env2Use);
        } 
        
        try {
            DelaunayCalculator dc = DelaunayFactory.calculateDelaunay( featuresToUse, monitor, handler);
            if (!dc.hasErrorOccured()){
                if (!this.layer2InterimMapObject.containsKey(layer)){
                    DelaunayInterimMapObject del2Interim = new DelaunayInterimMapObject();
                    del2Interim.put(env2Use, dc);
                    this.layer2InterimMapObject.put(layer, del2Interim);
                } else {
                    DelaunayInterimMapObject del2Interim = (DelaunayInterimMapObject)this.layer2InterimMapObject.get(layer);
                    del2Interim.put(env2Use, dc);                    
                }
                
                try {
                    // no double registrations!
                    layer.getLayerManager().removeLayerListener(this);
                } catch (Exception e) {
                }
                layer.getLayerManager().addLayerListener(this);
                return dc;
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.handleThrowable(e);
        } 
        
        return null;
    }
    
    public DelaunayCalculator getDelaunayDiagrammFor( Layer layer, Envelope env2Use, DelaunayPunkt[] pointsArray, TaskMonitorDialog monitor, ErrorHandler handler ){
        if ( this.layer2InterimMapObject.containsKey(layer) && ((DelaunayInterimMapObject)this.layer2InterimMapObject.get(layer)).containsKey(env2Use) ){
            return ((DelaunayInterimMapObject)this.layer2InterimMapObject.get(layer)).get(env2Use);
        } 
        
        try {
            DelaunayCalculator dc = DelaunayFactory.calculateDelaunay( pointsArray, monitor, handler);
            if (!dc.hasErrorOccured()){
                if (!this.layer2InterimMapObject.containsKey(layer)){
                    DelaunayInterimMapObject del2Interim = new DelaunayInterimMapObject();
                    del2Interim.put(env2Use, dc);
                    this.layer2InterimMapObject.put(layer, del2Interim);
                } else {
                    DelaunayInterimMapObject del2Interim = (DelaunayInterimMapObject)this.layer2InterimMapObject.get(layer);
                    del2Interim.put(env2Use, dc);                    
                }
                
                try {
                    // no double registrations!
                    layer.getLayerManager().removeLayerListener(this);
                } catch (Exception e) {
                }
                layer.getLayerManager().addLayerListener(this);
                return dc;
            }
        } catch (Exception e) {
            handler.handleThrowable(e); 
        } 
        
        return null;
    }
    
    public void featuresChanged(FeatureEvent e) {
        super.featuresChanged(e);
        if ( this.layer2InterimMapObject.containsKey(e.getLayer())){
            this.layer2InterimMapObject.remove(e.getLayer());
        }
    }
    
    private static DelaunayCalculator calculateDelaunay(DelaunayPunkt[] pointsArray, TaskMonitorDialog monitor, ErrorHandler handler) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
        DelaunayCalculator dc = new DelaunayCalculator(pointsArray, monitor, handler);
        
        dc.start();
	    monitor.report(PirolPlugInMessages.getString("calculating-triangulation") + "...");
		monitor.setVisible(true);
		
		return dc;
    }
    
    private static DelaunayCalculator calculateDelaunay(Feature[] featureArray, TaskMonitorDialog monitor, ErrorHandler handler) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
        DelaunayPunkt[] pointsArray = (DelaunayPunkt[])Data2LayerConnector.featureList2Punktefeld(featureArray, DelaunayPunkt.class);
        return DelaunayFactory.calculateDelaunay(pointsArray, monitor, handler);
    }
    
    private static DelaunayCalculator calculateDelaunay(Layer layer, TaskMonitorDialog monitor, ErrorHandler handler) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
        DelaunayPunkt[] punktefeld;
        punktefeld = (DelaunayPunkt[])Data2LayerConnector.layer2Punktefeld(layer, DelaunayPunkt.class);
        return DelaunayFactory.calculateDelaunay(punktefeld, monitor, handler);
    }
}
