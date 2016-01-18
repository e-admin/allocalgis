package com.geopista.app.eiel.forms;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.OtrosServMunicipalesEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.OtrosServMunicipalesPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class OtrosServMunicipalesPanelExtendedForm implements FeatureExtendedForm
{
    
    public OtrosServMunicipalesPanelExtendedForm()
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
            Identificadores.put("otrosserviciosmunicipales_panel", operations.getPanelOtrosServiciosMunicipalesEIEL(codprov,codmunic));
            OtrosServMunicipalesPanel otrosServiciosMunicipalesPanel = new OtrosServMunicipalesPanel();  
            otrosServiciosMunicipalesPanel.loadDataIdentificacion(codprov,codmunic);
            otrosServiciosMunicipalesPanel.loadData();
            this.otrosServiciosMunicipalesPanel = otrosServiciosMunicipalesPanel;           
            fd.addPanel(otrosServiciosMunicipalesPanel);     
        }  
        
}
    
    private OtrosServMunicipalesPanel otrosServiciosMunicipalesPanel = null;

    public void execute() throws Exception 
    {
    	if (otrosServiciosMunicipalesPanel != null) {
    		if (otrosServiciosMunicipalesPanel.datosMinimosYCorrectos()) {
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			otrosServiciosMunicipalesPanel.okPressed();
    			OtrosServMunicipalesEIEL otroServicioMunicipal = (OtrosServMunicipalesEIEL) AppContext.getApplicationContext().getBlackboard().get("otrosserviciosmunicipales_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(otrosServiciosMunicipalesPanel.getOtrosServMunicipales(otroServicioMunicipal), idLayer, ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
