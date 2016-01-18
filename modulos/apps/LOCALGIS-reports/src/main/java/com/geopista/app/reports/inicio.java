/**
 * inicio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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