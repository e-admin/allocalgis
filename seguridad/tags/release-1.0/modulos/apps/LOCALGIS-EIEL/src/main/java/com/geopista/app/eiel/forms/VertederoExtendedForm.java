package com.geopista.app.eiel.forms;


import com.geopista.app.AppContext;
import com.geopista.app.eiel.panels.VertederoNucleosPanel;
import com.geopista.app.eiel.utils.EdicionOperations;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class VertederoExtendedForm implements FeatureExtendedForm
{
    
    public VertederoExtendedForm()
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
        
        Object obj = fd.getFeature().getAttribute(esquema.getAttributeByColumn("id"));
        if(obj!=null && !obj.equals("") && 
                ((esquema.getGeopistalayer()!=null && !esquema.getGeopistalayer().isExtracted() && !esquema.getGeopistalayer().isLocal()) 
                        || (esquema.getGeopistalayer()==null) && fd.getFeature() instanceof GeopistaFeature && !((GeopistaFeature)fd.getFeature()).getLayer().isExtracted()))
        {  
            int ID=Integer.parseInt(fd.getFeature().getAttribute(esquema.getAttributeByColumn("id")).toString());
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            Identificadores.put ("ID_Vertedero", ID);  
            
            EdicionOperations operations = new EdicionOperations();
            Identificadores.put("vertedero", operations.getVertederoEIEL(ID));
            VertederoNucleosPanel titular= new VertederoNucleosPanel();            
            fd.addPanel(titular);     
        }  
        
}

	public void execute() {
		
	}

}
