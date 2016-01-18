package com.geopista.app.reports;
import javax.swing.JFrame;

import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
public class inicio extends JFrame 
{
  public inicio()
  {
         WizardDialog d = new WizardDialog(this,
               "Generador de Listados", null);
              d.init(new WizardPanel[] {
                     new GeopistaGeneradorListadosDatosGenerales(), new GeopistaGeneradorListadosEncabezado() , new GeopistaGeneradorListadosCampos()  , new GeopistaGeneradorListadosWhere() ,new GeopistaGeneradorListadosUnionesTablas(),new GeopistaLanzarDisenadorInformes()}
                );
                d.setSize(750,600);
               d.setVisible(true); 
  }

  public static void main(String[] args)
  {
    inicio inicio = new inicio();
   
  }
}