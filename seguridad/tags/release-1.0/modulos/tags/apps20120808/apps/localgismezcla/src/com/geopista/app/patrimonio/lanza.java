package com.geopista.app.patrimonio;
import javax.swing.JFrame;

import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
public class lanza extends JFrame 
{
  public lanza()
  {
         WizardDialog d = new WizardDialog(this,
               "Generador de Listados", null);
              d.init(new WizardPanel[] {
                     new GeopistaDiferentesSuperficiesPanel(), new GeopistaSupfNoCoincidentesPanel()}
                );
                d.setSize(750,600);
               d.setVisible(true); 
  }

  public static void main(String[] args)
  {
    lanza lanza = new lanza();
   
  }
}