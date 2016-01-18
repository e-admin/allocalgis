package com.geopista.app.eiel.forms;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.NucleoEncuestado7EIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.InfoTerminosMunicipalesPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class InfoTerminosMunicipalesPanelExtendedForm implements FeatureExtendedForm
{
    
    public InfoTerminosMunicipalesPanelExtendedForm()
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
            Identificadores.put("infoterminosmunicipales_panel", operations.getPanelInfoTerminosMunicipalesEIEL(codprov,codmunic,entidad,nucleo));
            InfoTerminosMunicipalesPanel infoTerminosMunicipalesPanel = new InfoTerminosMunicipalesPanel();  
            infoTerminosMunicipalesPanel.loadDataIdentificacion(codprov,codmunic,entidad,nucleo);
            infoTerminosMunicipalesPanel.loadData();
            this.infoTerminosMunicipalesPanel = infoTerminosMunicipalesPanel;           
            fd.addPanel(infoTerminosMunicipalesPanel);     
        }  
        
}
    
    private InfoTerminosMunicipalesPanel infoTerminosMunicipalesPanel = null;

    public void execute() throws Exception 
    {
    	if (infoTerminosMunicipalesPanel != null) {
    		if (infoTerminosMunicipalesPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			infoTerminosMunicipalesPanel.okPressed();
    			NucleoEncuestado7EIEL infoTerminoMunicipal = (NucleoEncuestado7EIEL) AppContext.getApplicationContext().getBlackboard().get("infoterminosmunicipales_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(infoTerminosMunicipalesPanel.getInfoTerminosMunicipales(infoTerminoMunicipal), idLayer, ConstantesLocalGISEIEL.NUCLEO_ENCT_7);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
