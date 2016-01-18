package com.geopista.app.patrimonio;

import com.geopista.app.AppContext;

import com.geopista.app.patrimonio.estructuras.Estructuras;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;
import com.geopista.ui.dialogs.ExtendedPanelsDialog;
import com.geopista.ui.dialogs.FeatureDialog;



public class PatrimonioInmueblesExtendedForm implements FeatureExtendedForm
{
    
    public PatrimonioInmueblesExtendedForm()
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
        //Obtener identificador de parcela        
        GeopistaSchema esquema = (GeopistaSchema)fd.getFeature().getSchema();
        Object obj = fd.getFeature().getAttribute(esquema.getAttributeByColumn("id"));
        if(obj!=null && !obj.equals("") && 
                ((esquema.getGeopistalayer()!=null && !esquema.getGeopistalayer().isExtracted() && !esquema.getGeopistalayer().isLocal()) 
                        || (esquema.getGeopistalayer()==null) && fd.getFeature() instanceof GeopistaFeature && !((GeopistaFeature)fd.getFeature()).getLayer().isExtracted()))
        {   
            int ID_Parcela=Integer.parseInt(obj.toString());
            
            
            //Obtener referencia catastral = referencia parcela + referencia plano 
            PatrimonioPostgre Parcela = new PatrimonioPostgre();
            Parcela.Referencias (ID_Parcela);
            
            AppContext app =(AppContext) AppContext.getApplicationContext();
            Blackboard Identificadores = app.getBlackboard();
            Identificadores.put("IdParcela", ID_Parcela);
            
            while (!Estructuras.isCargada())
            {
                if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
                try {Thread.sleep(500);}catch(Exception e){}
            }
            
            
            ListadoInmueblesPanel ListadoInmuebles = new ListadoInmueblesPanel(fd);
            General General= new General();
            RegistroPatrimonioPanel RegistroPatrimonioPanel= new RegistroPatrimonioPanel();
            ConstruccionPatrimonioPanel construccion= new ConstruccionPatrimonioPanel();
            ValoracionesPatrimonioPanel ValoracionesPatrimonioPanel= new ValoracionesPatrimonioPanel();    
            DerechosPatrimonioPanel DerechosPatrimonioPanel= new DerechosPatrimonioPanel();    
            
            //UsosPatrimonioPanel UsosPatrimonioPanel= new UsosPatrimonioPanel();            
            //SegurosPatrimonioPanel SeguroPatrimonio= new SegurosPatrimonioPanel();         
            //ReformasPatrimonioPanel ReformasPatrimonioPanel= new ReformasPatrimonioPanel();  
            
            ObservacionesPatrimonio ObservacionesPatrimonio= new ObservacionesPatrimonio();
            
            fd.addPanel(ListadoInmuebles);        
            fd.getFeature().getAttributes();       
            fd.addPanel(General);
            fd.addPanel(RegistroPatrimonioPanel); 
            fd.addPanel(construccion);
            fd.addPanel(ValoracionesPatrimonioPanel); 
            fd.addPanel(DerechosPatrimonioPanel); 
            fd.addPanel(ObservacionesPatrimonio);  
       
            
            if (fd instanceof FeatureDialog)
            {             
                ((FeatureDialog)fd).setPanelEnabled(General.class, false);
                ((FeatureDialog)fd).setPanelEnabled(RegistroPatrimonioPanel.class, false);
                ((FeatureDialog)fd).setPanelEnabled(ConstruccionPatrimonioPanel.class, false);
                ((FeatureDialog)fd).setPanelEnabled(ValoracionesPatrimonioPanel.class, false);
                ((FeatureDialog)fd).setPanelEnabled(DerechosPatrimonioPanel.class, false);
                ((FeatureDialog)fd).setPanelEnabled(ObservacionesPatrimonio.class, false);
                          
                ((FeatureDialog)fd).setSideBarDescription("");
                ((FeatureDialog)fd).setTitle(app.getI18nString("patrimonio.inmueblesdisponibles")+" "+ID_Parcela);
                
            }
            
        }
        
    }
    
}
