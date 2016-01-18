package com.geopista.app.eiel.forms;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.EdificiosSinUsoEIEL;
import com.geopista.app.eiel.panels.EdificiosSinUsoPanel;
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



public class EdificiosSinUsoPanelExtendedForm implements FeatureExtendedForm
{
    
    public EdificiosSinUsoPanelExtendedForm()
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
        	
        	String orden_su = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_su"))!=null){
        		orden_su=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_su"))).toString();
        	}

            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("edificiossinuso_panel", operations.getPanelEdificiosSinUsoIEL(clave,codprov,codmunic,entidad,nucleo,orden_su));
            EdificiosSinUsoPanel edificiosSinUsoPanel = new EdificiosSinUsoPanel();  
            edificiosSinUsoPanel.loadDataIdentificacion(clave,codprov,codmunic,entidad,nucleo,orden_su);
            edificiosSinUsoPanel.loadData();
            this.edificiosSinUsoPanel = edificiosSinUsoPanel;           
            fd.addPanel(edificiosSinUsoPanel);     
        }  
        
}
    
    private EdificiosSinUsoPanel edificiosSinUsoPanel = null;

    public void execute() throws Exception 
    {
    	if (edificiosSinUsoPanel != null) {
    		if (edificiosSinUsoPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			edificiosSinUsoPanel.okPressed();
    			EdificiosSinUsoEIEL edificioSinUso = (EdificiosSinUsoEIEL) AppContext.getApplicationContext().getBlackboard().get("edificiossinuso_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(edificiosSinUsoPanel.getEdificiosSinUso(edificioSinUso), idLayer, ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
     				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){
     					EditingInfoPanel.getInstance().listarDatosTabla();
     				}
     			}
    		}
    	}    	
	}

}
