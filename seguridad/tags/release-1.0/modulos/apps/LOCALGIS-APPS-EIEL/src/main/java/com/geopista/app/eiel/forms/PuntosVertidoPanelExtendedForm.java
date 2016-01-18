package com.geopista.app.eiel.forms;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.PuntosVertidoEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.PuntosVertidoPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class PuntosVertidoPanelExtendedForm implements FeatureExtendedForm
{
    
    public PuntosVertidoPanelExtendedForm()
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
        	String clave = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("clave"))!=null){
        		clave=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("clave"))).toString();
        	}
        	
        	String codprov = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("codprov"))!=null){
        		codprov=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codprov"))).toString();
        	}
        	
        	String codmunic = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("codmunic"))!=null){
        		codmunic=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codmunic"))).toString();
        	}
        	
        	String orden_pv = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_pv"))!=null){
        		orden_pv=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_pv"))).toString();
        	}
        	
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("puntosvertido_panel", operations.getPanelPuntosVertidoEIEL(clave,codprov,codmunic,orden_pv));
            PuntosVertidoPanel puntosVertidosPanel = new PuntosVertidoPanel();  
            puntosVertidosPanel.loadDataIdentificacion(clave,codprov,codmunic,orden_pv);
            puntosVertidosPanel.loadData();
            this.puntosVertidoPanel = puntosVertidosPanel;           
            fd.addPanel(puntosVertidosPanel);     
        }  
        
}
    
    private PuntosVertidoPanel puntosVertidoPanel = null;

    public void execute() throws Exception 
    {
    	if (puntosVertidoPanel != null) {
    		if (puntosVertidoPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			puntosVertidoPanel.okPressed();
    			PuntosVertidoEIEL puntoVertido = (PuntosVertidoEIEL) AppContext.getApplicationContext().getBlackboard().get("puntosvertido_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(puntosVertidoPanel.getPuntosVertido(puntoVertido), idLayer, ConstantesLocalGISEIEL.PUNTOS_VERTIDO);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
