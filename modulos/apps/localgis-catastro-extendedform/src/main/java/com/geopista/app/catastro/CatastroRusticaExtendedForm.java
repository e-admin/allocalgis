/**
 * CatastroRusticaExtendedForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

import com.geopista.app.AppContext;
import com.geopista.feature.AbstractValidator;
import com.geopista.util.ApplicationContext;
import com.geopista.util.FeatureDialogHome;
import com.geopista.util.FeatureExtendedForm;
import com.vividsolutions.jump.util.Blackboard;



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
