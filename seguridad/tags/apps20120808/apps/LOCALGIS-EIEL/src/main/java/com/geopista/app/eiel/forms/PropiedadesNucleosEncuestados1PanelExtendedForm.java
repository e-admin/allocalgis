package com.geopista.app.eiel.forms;


import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.Encuestados1EIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.Encuestados1Panel;
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



public class PropiedadesNucleosEncuestados1PanelExtendedForm implements FeatureExtendedForm
{
    
    public PropiedadesNucleosEncuestados1PanelExtendedForm()
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
            Identificadores.put("propiedadesnucleosencuestados1_panel", operations.getPanelNucleosEncuestados1EIEL(codprov,codmunic,entidad,nucleo));
            Encuestados1Panel encuestados1Panel= new Encuestados1Panel();  
            encuestados1Panel.loadDataIdentificacion(codprov,codmunic,entidad,nucleo);
            encuestados1Panel.loadData();
            this.encuestados1Panel = encuestados1Panel;           
            fd.addPanel(encuestados1Panel);     
        }  
        
}
    
    private Encuestados1Panel encuestados1Panel = null;

    public void execute() throws Exception 
    {
    	if (encuestados1Panel != null) {
    		if (encuestados1Panel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.ENCUESTADOS1_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			encuestados1Panel.okPressed();
    			Encuestados1EIEL encuestado1 = (Encuestados1EIEL) AppContext.getApplicationContext().getBlackboard().get("propiedadesnucleosencuestados1_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(encuestados1Panel.getEncuestados1(encuestado1), idLayer, ConstantesLocalGISEIEL.ENCUESTADOS1);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
