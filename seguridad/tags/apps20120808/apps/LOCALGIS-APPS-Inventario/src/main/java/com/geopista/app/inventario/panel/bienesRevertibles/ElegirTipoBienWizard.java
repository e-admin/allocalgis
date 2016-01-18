package com.geopista.app.inventario.panel.bienesRevertibles;

import com.geopista.ui.wizard.WizardPanel;
import com.geopista.ui.wizard.WizardContext;

import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.Estructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;

import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


import javax.swing.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.*;



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


