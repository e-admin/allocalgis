package com.geopista.app.eiel.forms;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.ServiciosRecogidaBasuraEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.ServiciosRecogidaBasurasPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class ServiciosRecogidaBasuraPanelExtendedForm implements FeatureExtendedForm
{
    
    public ServiciosRecogidaBasuraPanelExtendedForm()
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
        	
        	String entidad = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("codentidad"))!=null){
        		entidad=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codentidad"))).toString();
        	}
        	
        	String nucleo = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("codpoblamiento"))!=null){
        		nucleo=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codpoblamiento"))).toString();
        	}

            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("serviciosrecogidabasura_panel", operations.getPanelServiciosRecogidaBasuraEIEL(codprov,codmunic,entidad,nucleo));
            ServiciosRecogidaBasurasPanel serviciosRecogidabasuraPanel = new ServiciosRecogidaBasurasPanel();  
            serviciosRecogidabasuraPanel.loadDataIdentificacion(codprov,codmunic,entidad,nucleo);
            serviciosRecogidabasuraPanel.loadData();
            this.serviciosRecogidabasuraPanel = serviciosRecogidabasuraPanel;           
            fd.addPanel(serviciosRecogidabasuraPanel);     
        }  
        
}
    
    private ServiciosRecogidaBasurasPanel serviciosRecogidabasuraPanel = null;

    public void execute() throws Exception 
    {
    	if (serviciosRecogidabasuraPanel != null) {
    		if (serviciosRecogidabasuraPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			serviciosRecogidabasuraPanel.okPressed();
    			ServiciosRecogidaBasuraEIEL servicioReogidaBsura = (ServiciosRecogidaBasuraEIEL) AppContext.getApplicationContext().getBlackboard().get("serviciosrecogidabasura_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(serviciosRecogidabasuraPanel.getServiciosRecogidaBasuras(servicioReogidaBsura), idLayer, ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
     				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){
     					EditingInfoPanel.getInstance().listarDatosTabla();
     				}
     			}
    		}
    	}
	}

}
