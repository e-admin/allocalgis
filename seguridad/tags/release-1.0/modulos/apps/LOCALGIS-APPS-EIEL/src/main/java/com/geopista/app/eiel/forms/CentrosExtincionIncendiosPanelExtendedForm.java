package com.geopista.app.eiel.forms;


import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.IncendiosProteccionEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.IncendiosProteccionPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class CentrosExtincionIncendiosPanelExtendedForm implements FeatureExtendedForm
{
    
    public CentrosExtincionIncendiosPanelExtendedForm()
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
        	
        	String orden_ip = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_ip"))!=null){
        		orden_ip=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_ip"))).toString();
        	}
        	
        	
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("incendiosproteccion_panel", operations.getPanelCentrosIncendiosEIEL(clave,codprov,codmunic,entidad,nucleo,orden_ip));
            IncendiosProteccionPanel incendiosProteccion= new IncendiosProteccionPanel();  
            incendiosProteccion.loadDataIdentificacion(clave,codprov,codmunic,entidad,nucleo,orden_ip);
            incendiosProteccion.loadData();
            this.incendiosProteccionPanel = incendiosProteccion;           
            fd.addPanel(incendiosProteccion);     
        }  
        
}
    
    private IncendiosProteccionPanel incendiosProteccionPanel = null;

    public void execute() throws Exception 
    {
    	if (incendiosProteccionPanel != null) {
    		if (incendiosProteccionPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			incendiosProteccionPanel.okPressed();
    			IncendiosProteccionEIEL centroIncendios = (IncendiosProteccionEIEL) AppContext.getApplicationContext().getBlackboard().get("incendiosproteccion_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(incendiosProteccionPanel.getIncendiosProteccion(centroIncendios), idLayer, ConstantesLocalGISEIEL.INCENDIOS_PROTECCION);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
     				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){
     					EditingInfoPanel.getInstance().listarDatosTabla();
     				}
     			}
    		}
    	}
	}

}
