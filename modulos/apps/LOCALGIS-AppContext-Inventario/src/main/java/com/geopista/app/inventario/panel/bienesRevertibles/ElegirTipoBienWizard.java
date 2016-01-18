/**
 * ElegirTipoBienWizard.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel.bienesRevertibles;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;



/**
 * Creado por SATEC.
 * User: angeles
 * Date: 17-mayo-2010
 * Time: 10:27:17
 * Clase que se utiliza para elegir el tipo de bien a añadir
 *
 */

public class ElegirTipoBienWizard  extends JPanel implements  WizardPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger =Logger.getLogger(ElegirTipoBienWizard.class);
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = aplicacion.getBlackboard();
    private WizardContext wizardContext;
    private String locale;
    
   

    /**
     * Constructor de la clase
     */
    public ElegirTipoBienWizard(String locale)
    {
    	   this.locale=locale;
           try{
                initComponents();
                renombrarComponentes();
                
            } catch (Exception e){
                logger.error("Error al importar actividades economicas",e);
            }
    }
    /**
     * Inicializa los componenetes de la pantalla
     * @throws Exception
     */
    private void initComponents() throws Exception
    {
        setLayout(new FlowLayout());
        setName(aplicacion.getI18nString("inventario.bienesrevertibles.elegirtipobien"));
        tipoBienJLabel = new javax.swing.JLabel();
        tipoBienEJCBox = new ComboBoxEstructuras(Estructuras.getListaSubtipoBienesPatrimonio(), null, locale, false);
        add(tipoBienJLabel, BorderLayout.CENTER);
        add(tipoBienEJCBox, BorderLayout.SOUTH);
        addAyudaOnline();
    }
    public void renombrarComponentes(){
    	
    	tipoBienJLabel.setText(aplicacion.getI18nString("inventario.bienesrevertibles.tipobien")+":");//"Tipo bien"+":");
     }
 	/**
  	 * Ayuda Online
  	 * 
  	 */
  	private void addAyudaOnline() {
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("F1"), "action F1");

		this.getActionMap().put("action F1", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
	 			String uriRelativa = "/Geocuenca:ImportarActividadesEconomicas#Asociar_direcciones";
				GeopistaBrowser.openURL(aplicacion.getString("ayuda.geopista.web")
						+ uriRelativa);
			}
		});
  	}    
    /**
     * Metodo que se ejecuta al inicio y que intenta georrefenrenciar las
     * actividades economicas
     * @param dataMap
     */
    public void enteredFromLeft(Map dataMap)
    {
         wizardContext.previousEnabled(false);
        
    }

       /**
     * Lo que queremos hacer antes de salir de la pantalla
     */
    public void exitingToRight() throws Exception
    {
        // Salimos y ponemos en el blackBoard la lista de actividades
       wizardContext.setData("patron",tipoBienEJCBox.getSelectedPatron() );
       exiting();
    }

    public void add(InputChangedListener listener)
    {

    }

    public void remove(InputChangedListener listener)
    {

    }

    public String getTitle()
    {
        return this.getName();
    }

    public String getID()
    {
        return "1";
    }

    public String getInstructions()
    {
        return " ";
    }

    public boolean isInputValid()
    {
         return true;
    }

    private String nextID = "2";
  
    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }
    public String getNextID()
    {
        return nextID;
    }

    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }




     /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
    	// logger =null;
        // aplicacion = null;
        // blackboard = null;
        // wizardContext=null;
        // tipoBienJLabel = null;
        // tipoBienEJCBox = null;
    }
    
    private javax.swing.JLabel tipoBienJLabel;
    private ComboBoxEstructuras tipoBienEJCBox;

} // de la clase general.


