package com.geopista.app.eiel.forms;


import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.panels.CaptacionesNucleosPanel;
import com.geopista.app.eiel.panels.CaptacionesPanel;
import com.geopista.app.eiel.panels.DepositosPanel;
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



public class CaptacionesExtendedForm implements FeatureExtendedForm
{
    
    public CaptacionesExtendedForm()
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
    private CaptacionesPanel captacionesPanel = null;

    public void initialize(FeatureDialogHome fd)
    {
       
        GeopistaSchema esquema = (GeopistaSchema)fd.getFeature().getSchema();
        
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
        	
        	String orden_ca = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_ca"))!=null){
        		orden_ca=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_ca"))).toString();
        	}
        	
            int ID=Integer.parseInt(fd.getFeature().getAttribute(esquema.getAttributeByColumn("id")).toString());
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            Identificadores.put ("ID_Captacion", ID);  
            
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("captacion", operations.getCaptacionEIEL(ID));
            CaptacionesPanel captaciones= new CaptacionesPanel();     
            captaciones.loadDataIdentificacion(clave,codprov,codmunic,orden_ca);
            captaciones.loadData();
            this.captacionesPanel = captaciones; 
            fd.addPanel(captaciones);      
        }  
        
}

	public void execute() throws Exception {
		if (captacionesPanel != null) {
    		if (captacionesPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			captacionesPanel.okPressed();
    			CaptacionesEIEL captacion = (CaptacionesEIEL) AppContext.getApplicationContext().getBlackboard().get("captacion");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(captacionesPanel.getCaptacion(captacion), idLayer, ConstantesLocalGISEIEL.CAPTACIONES);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.CAPTACIONES)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
