package com.geopista.app.catastro;

import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Rectangle;
import javax.swing.JLabel;
import com.geopista.util.FeatureDialogHome;
import com.geopista.feature.AbstractValidator;
import com.geopista.app.AppContext;
import com.geopista.app.catastro.*;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureExtendedForm;
import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.Blackboard;

import com.geopista.feature.*;



public class CatastroRusticaExtendedForm implements FeatureExtendedForm
{

     public CatastroRusticaExtendedForm()
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
        int ID=Integer.parseInt(fd.getFeature().getAttribute("ID_Parcela").toString());
        AppContext app =(AppContext) AppContext.getApplicationContext();
        Blackboard Identificadores = app.getBlackboard();
        Identificadores.put ("ID_ParcelaRustica", ID);
        SubparcelaRusticaPanel subparcela= new SubparcelaRusticaPanel();
        ConstruccionRusticaPanel construccion= new ConstruccionRusticaPanel();
        TitularRusticaPanel titular= new TitularRusticaPanel();


        fd.addPanel(subparcela); 
        fd.addPanel(construccion); 
        fd.addPanel(titular);     

    }
   
  }
