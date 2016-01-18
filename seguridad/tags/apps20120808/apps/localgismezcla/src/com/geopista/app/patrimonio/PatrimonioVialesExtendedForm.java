package com.geopista.app.patrimonio;

import com.geopista.app.AppContext;
import com.geopista.feature.AbstractValidator;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



public class PatrimonioVialesExtendedForm implements FeatureExtendedForm
{

     public PatrimonioVialesExtendedForm()
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
        int ID=Integer.parseInt(fd.getFeature().getAttribute("ID_Bien").toString());
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        Identificadores.put ("ID_Bien", ID);
        VialesPatrimonioGeneral General= new VialesPatrimonioGeneral();
        SegurosPatrimonioPanel SeguroPatrimonio= new SegurosPatrimonioPanel();         
        ReformasPatrimonioPanel ReformasPatrimonioPanel= new ReformasPatrimonioPanel();    
        ObservacionesPatrimonio ObservacionesPatrimonio= new ObservacionesPatrimonio();

        fd.addPanel(General);
        fd.addPanel(SeguroPatrimonio); 
        fd.addPanel(ReformasPatrimonioPanel);
        fd.addPanel(ObservacionesPatrimonio);     

    }
   
  }
