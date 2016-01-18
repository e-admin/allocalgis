package com.geopista.app.eiel.forms;


import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.panels.CaptacionesPanel;
import com.geopista.app.eiel.panels.ColectoresPanel;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.TramosConduccionNucleosPanel;
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



public class TramosConduccionExtendedForm implements FeatureExtendedForm
{
    
    public TramosConduccionExtendedForm()
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
    private TramosConduccionPanel tramosConduccionPanel = null;

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
        	
        	String tramo_cn = null;
        	if (fd.getFeature().getAttribute(esquema.getAttributeByColumn("tramo_cn"))!=null){
        		tramo_cn=(fd.getFeature().getAttribute(esquema.getAttributeByColumn("tramo_cn"))).toString();
        	}
            int ID=Integer.parseInt(fd.getFeature().getAttribute(esquema.getAttributeByColumn("id")).toString());
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            Identificadores.put ("ID_Conduccion", ID);  
            
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("conduccion", operations.getTramosConduccionEIEL(ID));
            TramosConduccionPanel tramosConduccion= new TramosConduccionPanel();
            tramosConduccion.loadDataIdentificacion(clave, codprov, codmunic, tramo_cn);
            tramosConduccion.loadData();
            tramosConduccionPanel=tramosConduccion;
            fd.addPanel(tramosConduccion);   
            
        }  
        
}

	public void execute() throws Exception {
		if (tramosConduccionPanel != null) {
    		if (tramosConduccionPanel.datosMinimosYCorrectos()){
    			GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
    			String idLayer = geopistaLayer.getSystemId();
    			tramosConduccionPanel.okPressed();
    			TramosConduccionEIEL tramoConduccion = (TramosConduccionEIEL) AppContext.getApplicationContext().getBlackboard().get("conduccion");
    			ConstantesLocalGISEIEL.clienteLocalGISEIEL.insertarElemento(tramosConduccionPanel.getTramosConduccion(tramoConduccion), idLayer, ConstantesLocalGISEIEL.TCONDUCCION);
    			
    			if (EditingInfoPanel.getInstance() != null && EditingInfoPanel.getInstance().getJPanelTree()!=null && EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected()!= null){
    				if (EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected().equals(ConstantesLocalGISEIEL.TCONDUCCION)){
    					EditingInfoPanel.getInstance().listarDatosTabla();
    				}
    			}
    		}
    	}
	}

}
