package com.geopista.app.eiel.forms;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.SaneamientoAutonomoPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class SaneamientoAutonomoPanelExtendedForm implements FeatureExtendedForm
{
    
    public SaneamientoAutonomoPanelExtendedForm()
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
            SaneamientoAutonomoEIEL element = operations.getPanelSaneamientoAutonomoEIEL(codprov,codmunic,entidad,nucleo);
            Identificadores.put("saneamientoautonomo_panel", element);
            SaneamientoAutonomoPanel saneamientoAutonomoPanel = new SaneamientoAutonomoPanel();  
            if (element !=null){
            	saneamientoAutonomoPanel.loadDataIdentificacion(element.getClave(),element.getCodINEProvincia(),element.getCodINEMunicipio(),element.getCodINEEntidad(),element.getCodINENucleo());
            }
            saneamientoAutonomoPanel.loadData();
            this.saneamientoAutonomoPanel = saneamientoAutonomoPanel;           
            fd.addPanel(saneamientoAutonomoPanel);     
        }  
        
}
    
    private SaneamientoAutonomoPanel saneamientoAutonomoPanel = null;

    public void execute() throws Exception 
    {
    	if (saneamientoAutonomoPanel != null) {
    		if (saneamientoAutonomoPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			saneamientoAutonomoPanel.okPressed();
    			SaneamientoAutonomoEIEL saneamientoAutonomo = (SaneamientoAutonomoEIEL) AppContext.getApplicationContext().getBlackboard().get("saneamientoautonomo_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(saneamientoAutonomoPanel.getSaneamientoAutonomo(saneamientoAutonomo), idLayer, ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
