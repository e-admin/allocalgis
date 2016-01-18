package com.geopista.app.catastro;


import com.geopista.util.FeatureDialogHome;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaLayer;
import com.geopista.app.AppContext;

import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureExtendedForm;

import com.vividsolutions.jump.util.Blackboard;



public class CatastroExtendedForm implements FeatureExtendedForm
{
    
    public CatastroExtendedForm()
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
        
        //int ID=Integer.parseInt(fd.getFeature().getAttribute("ID_Parcela").toString());
        GeopistaSchema esquema = (GeopistaSchema)fd.getFeature().getSchema();
        
        Object obj = fd.getFeature().getAttribute(esquema.getAttributeByColumn("id"));
        if(obj!=null && !obj.equals("") && 
                ((esquema.getGeopistalayer()!=null && !esquema.getGeopistalayer().isExtracted() && !esquema.getGeopistalayer().isLocal()) 
                        || (esquema.getGeopistalayer()==null) && fd.getFeature() instanceof GeopistaFeature && !((GeopistaFeature)fd.getFeature()).getLayer().isExtracted()))
        {  
            int ID=Integer.parseInt(fd.getFeature().getAttribute(esquema.getAttributeByColumn("id")).toString());
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            Identificadores.put ("ID_Parcela", ID);           
            //SujetoPasivoPanel sujetoPasivo = new SujetoPasivoPanel();
            TitularesPanel titular= new TitularesPanel();            
            //fd.addPanel(sujetoPasivo);
            fd.addPanel(titular);     
        }  
        
}

}
