package com.geopista.app.inventario;

import com.geopista.util.FeatureExtendedForm;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.dialogs.InventarioDialog;
import com.geopista.app.AppContext;
import com.geopista.app.inventario.panel.InventarioJPanel;
import com.vividsolutions.jump.util.Blackboard;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 11-jul-2006
 * Time: 11:28:07
 * To change this template use File | Settings | File Templates.
 */
public class InventarioExtendedForm implements FeatureExtendedForm{
    private InventarioJPanel inventarioPanel;

    public InventarioExtendedForm(){
    }

    public void setApplicationContext(ApplicationContext context){
    }

    public void flush(){
    }

    public boolean checkPanels(){
        return true;
    }

    public AbstractValidator getValidator(){
        return null;
    }

    public void disableAll() {
        if (inventarioPanel!=null) inventarioPanel.setEnabled(false);
    }

    public void initialize(FeatureDialogHome fd){
        if (!AppContext.getApplicationContext().isOnline()) return;

        //GeopistaSchema esquema = (GeopistaSchema)fd.getFeature().getSchema();
        Object[] features;
        if (fd instanceof InventarioDialog)
            features=(((InventarioDialog)fd).getFeatures()).toArray();
        else{
            features= new Object[1];
            features[0]= (GeopistaFeature)fd.getFeature();
            
            if (features[0] == null){
				return;
			}
			else if (features[0] instanceof GeopistaFeature){
				GeopistaFeature geopistaFeature = (GeopistaFeature)features[0];
				if (((GeopistaFeature)features[0]).getLayer()!= null && 
				((GeopistaFeature)features[0]).getLayer() instanceof GeopistaLayer ){
					GeopistaLayer layer = (GeopistaLayer)((GeopistaFeature)features[0]).getLayer();
					if (layer.isLocal() || layer.isExtracted()){
						return;
					}
				}
				else{
					return;
				}
			}
            
            try{ //Si se esta insertando no dejamos meter documentos.
               new Long(((GeopistaFeature)fd.getFeature()).getSystemId());
            }catch(Exception e){return;}//Si es una inserción no mostramos la pantalla
        }
        AppContext app= (AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores= app.getBlackboard();
        Identificadores.put("feature", features);

        inventarioPanel= new InventarioJPanel(true);
        inventarioPanel.setPreferredSize(new Dimension(600,450));
        inventarioPanel.setMinimumSize(new Dimension(600,450));
        fd.addPanel(inventarioPanel);
    }

    public GeopistaFeature getSelectedFeature(){
         if (inventarioPanel==null) return null;
         return inventarioPanel.getSelectedFeature();
    }

}
