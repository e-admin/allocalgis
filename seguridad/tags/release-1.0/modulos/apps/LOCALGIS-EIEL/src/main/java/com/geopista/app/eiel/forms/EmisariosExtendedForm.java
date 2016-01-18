package com.geopista.app.eiel.forms;


import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.EmisariosPanel;
import com.geopista.app.eiel.panels.EmisariosPuntosVertidoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.TramosConduccionPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class EmisariosExtendedForm implements FeatureExtendedForm
{
    
    public EmisariosExtendedForm()
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
    private EmisariosPanel emisariosPanel = null;

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
        		codmunic =(fd.getFeature().getAttribute(esquema.getAttributeByColumn("codmunic"))).toString();
        	}
        	
        	String tramo_em = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("tramo_em"))!=null){
        		tramo_em=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("tramo_em"))).toString();
        	}
        	
            int ID=Integer.parseInt(fd.getFeature().getAttribute(esquema.getAttributeByColumn("id")).toString());
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            Identificadores.put ("ID_Emisario", ID);  
            
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("emisario", operations.getEmisarioEIEL(ID));
            EmisariosPanel titular= new EmisariosPanel();
            titular.loadDataIdentificacion(clave, codprov, codmunic, tramo_em);
            titular.loadData();
            this.emisariosPanel = titular; 
            fd.addPanel(titular);     
        }  
        
}

	public void execute() throws Exception {
		if (emisariosPanel != null) {
    		if (emisariosPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			emisariosPanel.okPressed();
    			EmisariosEIEL emisario = (EmisariosEIEL) AppContext.getApplicationContext().getBlackboard().get("emisario");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(emisariosPanel.getEmisor(emisario), idLayer, ConstantesLocalGISEIEL.EMISARIOS);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.EMISARIOS)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
