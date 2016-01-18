package com.geopista.app.eiel.forms;


import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.TratamientosPotabilizacionPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class TratamientosPotabilizacionPanelExtendedForm implements FeatureExtendedForm
{
    
    public TratamientosPotabilizacionPanelExtendedForm()
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
        	
        	String orden_tp = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_tp"))!=null){
        		orden_tp=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("orden_tp"))).toString();
        	}
        	
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("tratamientospotabilizacion_panel", operations.getPanelTratamientosPotabilizacionEIEL(clave,codprov,codmunic,orden_tp));
            TratamientosPotabilizacionPanel tratamientos= new TratamientosPotabilizacionPanel();  
            tratamientos.loadData();
            tratamientos.loadDataIdentificacion(clave,codprov,codmunic,orden_tp);
            this.tratamientosPanel = tratamientos;           
            fd.addPanel(tratamientos);     
        }  
        
}
    
    private TratamientosPotabilizacionPanel tratamientosPanel = null;

    public void execute() throws Exception 
    {
    	if (tratamientosPanel != null) {
    		if (tratamientosPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			tratamientosPanel.okPressed();
    			TratamientosPotabilizacionEIEL tratamientos = (TratamientosPotabilizacionEIEL) AppContext.getApplicationContext().getBlackboard().get("tratamientospatabilizacion_panel");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(tratamientosPanel.getTratamientosPotabilizacion(tratamientos), idLayer, ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    			
    		}
    	}
        
	}

}
