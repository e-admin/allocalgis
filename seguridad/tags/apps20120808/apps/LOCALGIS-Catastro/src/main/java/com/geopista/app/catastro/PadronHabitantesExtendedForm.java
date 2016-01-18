/*
 * Created on 13-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro;

/**
 * @author hgarcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.util.FeatureDialogHome;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.app.AppContext;

import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureExtendedForm;

import com.vividsolutions.jump.util.Blackboard;




public class PadronHabitantesExtendedForm implements FeatureExtendedForm
{
    
    public PadronHabitantesExtendedForm()
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
        AppContext app =(AppContext) AppContext.getApplicationContext();
        
        Object obj = fd.getFeature().getAttribute(esquema.getAttributeByColumn("id"));
        if(obj!=null && !obj.equals("") && 
                ((esquema.getGeopistalayer()!=null && !esquema.getGeopistalayer().isExtracted() && !esquema.getGeopistalayer().isLocal()) 
                        || (esquema.getGeopistalayer()==null) && fd.getFeature() instanceof GeopistaFeature && !((GeopistaFeature)fd.getFeature()).getLayer().isExtracted()))
            
        {    
            int idNumPolicia=Integer.parseInt(obj.toString());
            Blackboard Identificadores = app.getBlackboard();
            Identificadores.put ("ID_Policia", idNumPolicia);
            Identificadores.put ("ID_Habitante", 0);
            
            ListadoHabitantesPanel listado = new ListadoHabitantesPanel(fd);
            HabitantePanel habitante = new HabitantePanel(fd);
            DomicilioPanel domicilio= new DomicilioPanel();
            
            fd.addPanel(listado);
            fd.addPanel(habitante);
            fd.addPanel(domicilio);    
            
            if (fd instanceof FeatureDialog)
            {
                ((FeatureDialog)fd).setPanelEnabled(HabitantePanel.class, false);
                ((FeatureDialog)fd).setPanelEnabled(DomicilioPanel.class, false);     
            }
            
        }
        
    }
    
    
}

