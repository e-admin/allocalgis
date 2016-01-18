package com.geopista.app.eiel.forms;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.InstalacionDeportivaPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class InstalacionesDeportivasPanelExtendedForm implements FeatureExtendedForm
{
    
    public InstalacionesDeportivasPanelExtendedForm()
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
        	
        	String entidad = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("codentidad"))!=null){
        		entidad=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codentidad"))).toString();
        	}
        	
        	String nucleo = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("codpoblamiento"))!=null){
        		nucleo=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codpoblamiento"))).toString();
        	}
        	
        	String orden_id = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_id"))!=null){
        		orden_id=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_id"))).toString();
        	}
        	
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("intalacionesdeportivas_panel", operations.getPanelInstalacionesDeportivasEIEL(clave,codprov,codmunic,entidad,nucleo,orden_id));
            InstalacionDeportivaPanel instalacionesDeportivasPanel= new InstalacionDeportivaPanel();  
            instalacionesDeportivasPanel.loadDataIdentificacion(clave,codprov,codmunic,entidad,nucleo,orden_id);
            instalacionesDeportivasPanel.loadData();
            this.instalacionesDeportivasPanel = instalacionesDeportivasPanel;           
            fd.addPanel(instalacionesDeportivasPanel);     
        }  
        
}
    
    private InstalacionDeportivaPanel instalacionesDeportivasPanel = null;

    public void execute() throws Exception 
    {
    	if (instalacionesDeportivasPanel != null) {
    		if (instalacionesDeportivasPanel.datosMinimosYCorrectos()) {
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			instalacionesDeportivasPanel.okPressed();
    			InstalacionesDeportivasEIEL instalacionDeportiva = (InstalacionesDeportivasEIEL) AppContext.getApplicationContext().getBlackboard().get("instalacionesdeportivas_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(instalacionesDeportivasPanel.getInstalacionDeportiva(instalacionDeportiva), idLayer, ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
