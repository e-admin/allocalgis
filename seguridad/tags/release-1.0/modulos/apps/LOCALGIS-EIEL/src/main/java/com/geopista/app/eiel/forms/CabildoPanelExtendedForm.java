package com.geopista.app.eiel.forms;


import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.CabildoConsejoEIEL;
import com.geopista.app.eiel.panels.CabildoConsejoPanel;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class CabildoPanelExtendedForm implements FeatureExtendedForm
{
    
    public CabildoPanelExtendedForm()
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
        	
        	String codisla = null;
        	if (fd.getFeature().getSchema().hasAttribute("cod_isla")){
        		if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("cod_isla"))!=null){
        			codisla=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("cod_isla"))).toString();
        		}
        	}
        	
        	AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("cabildo_panel", operations.getPanelCabildoConsejoEIEL(codprov,codisla));
            CabildoConsejoPanel cabildoPanel= new CabildoConsejoPanel();  
            cabildoPanel.loadData();
            cabildoPanel.loadDataIdentificacion(codprov,codisla);
            this.cabildoConsejoPanel = cabildoPanel;           
            fd.addPanel(cabildoPanel);     
        }  
        
}
    
    private CabildoConsejoPanel cabildoConsejoPanel = null;

    public void execute() throws Exception 
    {
    	if (cabildoConsejoPanel != null) {
    		if (cabildoConsejoPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PROVINCIA_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			cabildoConsejoPanel.okPressed();
    			CabildoConsejoEIEL cabildo = (CabildoConsejoEIEL) AppContext.getApplicationContext().getBlackboard().get("cabildo_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(cabildoConsejoPanel.getCabildoConsejo(cabildo), idLayer, ConstantesLocalGISEIEL.CABILDO);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
     				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.CABILDO)){
     					EditingInfoPanel.getInstance().listarDatosTabla();
     				}
     			}
    		}
    	}
	}

}
