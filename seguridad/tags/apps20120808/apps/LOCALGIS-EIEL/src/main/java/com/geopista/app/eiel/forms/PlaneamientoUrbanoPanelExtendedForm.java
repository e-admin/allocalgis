package com.geopista.app.eiel.forms;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.PlaneamientoUrbanoEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.PlaneamientoUrbanoPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class PlaneamientoUrbanoPanelExtendedForm implements FeatureExtendedForm
{
    
    public PlaneamientoUrbanoPanelExtendedForm()
    {  
        
    }
    
    public void setApplicationContext(ApplicationContext context)
    {
        
    }
    
    public void flush()
    {
    }
    
    public boolean checkPanels()
    {
        return true;
    }
    
    
    public AbstractValidator getValidator()
    {
        return null;
    }

    public void disableAll() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void initialize(FeatureDialogHome fd)
    {      
        GeopistaSchema esquema = (GeopistaSchema)fd.getFeature().getSchema();        
        
        AppContext.getApplicationContext().getBlackboard().put("featureDialog", fd);
        
        Object obj = fd.getFeature().getAttribute(esquema.getAttributeByColumn("id"));
        if(obj!=null && !obj.equals("") && 
                ((esquema.getGeopistalayer()!=null && !esquema.getGeopistalayer().isExtracted() && !esquema.getGeopistalayer().isLocal()) 
                        || (esquema.getGeopistalayer()==null) && fd.getFeature() instanceof GeopistaFeature && !((GeopistaFeature)fd.getFeature()).getLayer().isExtracted()))
        {              

        	String codprov = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("codprov"))!=null){
        		codprov=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codprov"))).toString();
        	}
        	
        	String codmunic = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("codmunic"))!=null){
        		codmunic=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codmunic"))).toString();
        	}

            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("planeamientourbano_panel", operations.getPanelPlaneamientoUrbanosEIEL(codprov,codmunic));
            PlaneamientoUrbanoPanel planeamientoUrbanoPanel = new PlaneamientoUrbanoPanel();  
            planeamientoUrbanoPanel.loadDataIdentificacion(codprov,codmunic);
            planeamientoUrbanoPanel.loadData();
            this.planeamientoUrbanoPanel = planeamientoUrbanoPanel;           
            fd.addPanel(planeamientoUrbanoPanel);     
        }  
        
}
    
    private PlaneamientoUrbanoPanel planeamientoUrbanoPanel = null;

    public void execute() throws Exception 
    {
    	if (planeamientoUrbanoPanel != null) {
    		if (planeamientoUrbanoPanel.datosMinimosYCorrectos()) {
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			planeamientoUrbanoPanel.okPressed();
    			PlaneamientoUrbanoEIEL planeamientoUrbano = (PlaneamientoUrbanoEIEL) AppContext.getApplicationContext().getBlackboard().get("planeamientourbano_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(planeamientoUrbanoPanel.getPlaneamientoUrbano(planeamientoUrbano), idLayer, ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
     				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){
     					EditingInfoPanel.getInstance().listarDatosTabla();
     				}
     			}
    		}
    	}
	}

}
