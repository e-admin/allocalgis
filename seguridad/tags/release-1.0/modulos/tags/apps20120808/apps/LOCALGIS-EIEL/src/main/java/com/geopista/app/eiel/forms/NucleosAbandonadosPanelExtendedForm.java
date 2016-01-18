package com.geopista.app.eiel.forms;


import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.NucleosAbandonadosPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class NucleosAbandonadosPanelExtendedForm implements FeatureExtendedForm
{
    
    public NucleosAbandonadosPanelExtendedForm()
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
    	NucleosAbandonadosPanel.CARGARLISTANUCLEOS = true;
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
        	if ( esquema.hasAttribute("codentidad") && fd.getFeature().getAttribute(esquema.getAttributeByColumn("codentidad"))!=null){
        		entidad=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codentidad"))).toString();
        	}
        	
        	String nucleo = null;
        	if (esquema.hasAttribute("codpoblamiento") && fd.getFeature().getAttribute(esquema.getAttributeByColumn("codpoblamiento"))!=null){
        		nucleo=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codpoblamiento"))).toString();
        	}

            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("nucleosabandonados_panel", operations.getPanelNucleosAbandonadosEIEL(codprov,codmunic));
            NucleosAbandonadosPanel nucleaosAbandonadosPanel = new NucleosAbandonadosPanel();  
            nucleaosAbandonadosPanel.loadDataIdentificacion(codprov,codmunic,entidad,nucleo);
            nucleaosAbandonadosPanel.loadData();
            this.nucelosAbandonadosPanel = nucleaosAbandonadosPanel;           
            fd.addPanel(nucleaosAbandonadosPanel);     
        }  
        
}
    
    private NucleosAbandonadosPanel nucelosAbandonadosPanel = null;

    public void execute() throws Exception 
    {
    	if (nucelosAbandonadosPanel != null) {
    		if (nucelosAbandonadosPanel.datosMinimosYCorrectos()) {
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			nucelosAbandonadosPanel.okPressed();
    			NucleosAbandonadosEIEL nucleoAbandonado = (NucleosAbandonadosEIEL) AppContext.getApplicationContext().getBlackboard().get("nucleosabandonados_panel");
    			
    			Iterator nucleosIterator = nucelosAbandonadosPanel.getListaNucleosAbandonados().iterator();
    			while(nucleosIterator.hasNext()){
    				ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(nucleosIterator.next(), idLayer, ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS);
    			}
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
