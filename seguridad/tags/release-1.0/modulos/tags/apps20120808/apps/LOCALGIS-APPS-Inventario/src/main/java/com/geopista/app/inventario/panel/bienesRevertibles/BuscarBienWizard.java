package com.geopista.app.inventario.panel.bienesRevertibles;



import com.geopista.protocol.inventario.Const;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.app.AppContext;
import com.geopista.app.browser.GeopistaBrowser;
import com.geopista.app.inventario.panel.FiltroJPanel;


import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;


import javax.swing.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionEvent;

import java.util.*;


/**
 * Creado por SATEC.
 * User: angeles
 * Date: 17-mayo-2010
 * Time: 10:27:17
 * Clase que se utiliza para elegir el tipo de bien a añadir
 *
 */

public class BuscarBienWizard  extends JPanel implements  WizardPanel
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
    private String tipoBien;
    private JFrame frame;   
    private FiltroJPanel filtroJPanel;
  
   
    /**
     * Constructor de la clase
     */
    public BuscarBienWizard(JFrame frame, String locale, String tipoBien){
    	   this.locale=locale;
    	   this.frame=frame;
    	   this.tipoBien=tipoBien;
           try{
                initComponents();
                renombrarComponentes();
                
            } catch (Exception e){
                logger.error("Error al importar actividades economicas",e);
            }
           
    }

    /**
     * Constructor de la clase
     */
    public BuscarBienWizard(JFrame frame, String locale)  {
    	this(frame,locale, null);
    }
    /**
     * Inicializa los componenetes de la pantalla
     * @throws Exception
     */
    private void initComponents() throws Exception
    {
        setName(aplicacion.getI18nString("inventario.bienesrevertibles.buscarbienes"));
        setLayout(new FlowLayout());
        //add(new FiltroJPanel(frame,"3", locale));
        //addAyudaOnline();
    }
    public void renombrarComponentes(){
    
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
    	try{
    		//wizardContext.previousEnabled(false);
    		if (filtroJPanel!=null){
    			filtroJPanel.setVisible(false);
    			filtroJPanel.removeAll();
    			filtroJPanel=null;
    		}
    		filtroJPanel=new FiltroJPanel(frame,Const.SUPERPATRON_BIENES,(String)dataMap.get("patron"), locale, false);
    		add(filtroJPanel);
    	}catch(Exception ex){
    		logger.error("Error al inicializar el panel de búsqueda", ex);
    	} 
     }

       /**
     * Lo que queremos hacer antes de salir de la pantalla
     */
    public void exitingToRight() throws Exception
    {
        // Salimos y ponemos en el blackBoard la lista de actividades
       wizardContext.setData("filtro",filtroJPanel.getFiltro());
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
        return "2";
    }

    public String getInstructions()
    {
        return " ";
    }

    public boolean isInputValid()
    {
        return true;
    }

    private String nextID = "3";
    
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
        if (tipoBien!=null && wizardContext.getData("patron")==null){
       	 wizardContext.setData("patron", tipoBien);
       }
    }




     /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
    	 //logger =null;
         //aplicacion = null;
         //blackboard = null;
         //wizardContext=null;
         //tipoBienJLabel = null;
         //tipoBienEJCBox = null;
    	 //filtroJPanel=null;
    }
  

} // de la clase general.


