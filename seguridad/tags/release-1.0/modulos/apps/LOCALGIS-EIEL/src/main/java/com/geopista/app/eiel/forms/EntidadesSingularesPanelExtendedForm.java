package com.geopista.app.eiel.forms;

import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.EntidadesSingularesPanel;
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



public class EntidadesSingularesPanelExtendedForm implements FeatureExtendedForm
{
    
    public EntidadesSingularesPanelExtendedForm()
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
    	EntidadesSingularesPanel.CARGARLISTAENTIDADES = true;
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
            Identificadores.put("entidadessingulares_panel", operations.getPanelEntidadesSingularesEIEL(codprov,codmunic));
            EntidadesSingularesPanel entidadesSingularesPanel= new EntidadesSingularesPanel();  
            entidadesSingularesPanel.loadDataIdentificacion(codprov,codmunic);
            entidadesSingularesPanel.loadData();
            this.entidadesSingualresPanel = entidadesSingularesPanel;           
            fd.addPanel(entidadesSingularesPanel);     
        }  
        
}
    
    private EntidadesSingularesPanel entidadesSingualresPanel = null;

    public void execute() throws Exception 
    {
    	if (entidadesSingualresPanel != null) {
    		if (entidadesSingualresPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			entidadesSingualresPanel.okPressed();
    			EntidadesSingularesEIEL entidadSingular = (EntidadesSingularesEIEL) AppContext.getApplicationContext().getBlackboard().get("entidadessingulares_panel");
    			
    			Iterator nucleosIterator = entidadesSingualresPanel.getListaEntidadesSingulares().iterator();
    			while(nucleosIterator.hasNext()){
    				ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(nucleosIterator.next(), idLayer, ConstantesLocalGISEIEL.ENTIDADES_SINGULARES);
    			}
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
