package com.geopista.app.inventario.panel.bienesRevertibles;

import com.geopista.protocol.inventario.Const;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.panel.BienesJPanel;



import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
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
public class MostrarBienesWizard  extends JPanel implements  WizardPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger =Logger.getLogger(ElegirTipoBienWizard.class);
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = aplicacion.getBlackboard();
    private WizardContext wizardContext;
    private BienesJPanel bienesJPanel;
    private String locale;
    private String patron;
    private InventarioClient inventarioClient= null;
    private ArrayList filtro;
    private Collection bienesSeleccionados=null;   
   

    /**
     * Constructor de la clase
     */
    public MostrarBienesWizard(String locale)
    {
    	    this.locale=locale;
    	    try{
                initComponents();
                renombrarComponentes();
                inventarioClient= new InventarioClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA) +
                		Constantes.INVENTARIO_SERVLET_NAME);
                
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
        setName(aplicacion.getI18nString("inventario.bienesrevertibles.seleccionarbienes"));
        setLayout(new BorderLayout());
        bienesJPanel= new BienesJPanel(locale,true,true);
        add(bienesJPanel, BorderLayout.CENTER);
        addAyudaOnline();
    }
    public void renombrarComponentes(){
    
     }
    
 	
	/**
  	 * Ayuda Online
  	 * 
  	 */
    
  	private void addAyudaOnline() {
		/*this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke("F1"), "action F1");

		this.getActionMap().put("action F1", new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
	 			String uriRelativa = "/Geocuenca:ImportarActividadesEconomicas#Asociar_direcciones";
				GeopistaBrowser.openURL(aplicacion.getString("ayuda.geopista.web")
						+ uriRelativa);
			}
		});*/
  	}    
    /**
     * Metodo que se ejecuta al inicio y que intenta georrefenrenciar las
     * actividades economicas
     * @param dataMap
     */
    public void enteredFromLeft(Map dataMap)
    {
    	try{
    		patron=(String)dataMap.get("patron");
    		if (dataMap.get("filtro")!=null)
    			filtro= new ArrayList((Collection)dataMap.get("filtro"));
    		else
    			filtro=null;
    		cargarTablaBienesInventario();
    		
    	}catch(Exception ex){
    		logger.error("Error al inicializar el panel de búsqueda", ex);
    	} 
     }
    
    /**
     * Recarga la tabla de bienes de inventario cuando el usuario selecciona un tipo de bien del arbol,
     * o finaliza algun tipo de operacion sobre algun bien de la tabla.
     * @throws Exception
     */
    private void cargarTablaBienesInventario() throws Exception{
        
        AppContext app =(AppContext) AppContext.getApplicationContext();
        final JFrame desktop= (JFrame)app.getMainFrame();
        final TaskMonitorDialog progressDialog= new TaskMonitorDialog(desktop, null);
        progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.addComponentListener(new ComponentAdapter()
        {
            public void componentShown(ComponentEvent e) 
            {
                new Thread(new Runnable()
                {
                    public void run()  //throws Exception
                    {
                        try{
                            progressDialog.report(aplicacion.getI18nString("inventario.app.tag3"));

                            Collection c= null;
                            boolean b= true;
                            
                            bienesJPanel.clearTable();

                            if (patron.equals(Const.PATRON_INMUEBLES_URBANOS) ||
                            		patron.equals(Const.PATRON_INMUEBLES_RUSTICOS)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_INMUEBLES, Const.SUPERPATRON_BIENES, patron, null, filtro, null,null);
                            }else if (patron.equals(Const.PATRON_MUEBLES_HISTORICOART) ||
                            		patron.equals(Const.PATRON_BIENES_MUEBLES)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_MUEBLES,  Const.SUPERPATRON_BIENES, patron, null, filtro, null,null);
                            }else if (patron.equals(Const.PATRON_DERECHOS_REALES)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_DERECHOS_REALES,  Const.SUPERPATRON_BIENES, patron, null, filtro, null,null);
                            }else if (patron.equals(Const.PATRON_VALOR_MOBILIARIO)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_VALORES_MOBILIARIOS,  Const.SUPERPATRON_BIENES, patron, null, filtro, null,null);
                            }else if (patron.equals(Const.PATRON_CREDITOS_DERECHOS_PERSONALES)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_CREDITOS_DERECHOS,  Const.SUPERPATRON_BIENES, patron, null, filtro, null,null);
                            }else if (patron.equals(Const.PATRON_SEMOVIENTES)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_SEMOVIENTES, Const.SUPERPATRON_BIENES, patron, null, filtro, null,null);
                            }else if (patron.equals(Const.PATRON_VIAS_PUBLICAS_URBANAS) ||
                            		patron.equals(Const.PATRON_VIAS_PUBLICAS_RUSTICAS)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_VIAS, Const.SUPERPATRON_BIENES, patron, null, filtro, null,null);
                            }else if (patron.equals(Const.PATRON_VEHICULOS)){
                            	c= inventarioClient.getBienesInventario(Const.ACTION_GET_VEHICULOS, Const.SUPERPATRON_BIENES, patron, null, filtro, null,null);
                            }else{
                            	/** No es ningun tipo reconocido. */
                            	b= false;
                            }

                            /** Cargamos la coleccion */
                            bienesJPanel.loadListaBienes(c);
                           
                        }
                        catch(Exception e){
                        	ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("inventario.SQLError.Titulo"),
                        			aplicacion.getI18nString("inventario.SQLError.Aviso"), StringUtil.stackTrace(e));
                        }
                        finally
    					{
    						progressDialog.setVisible(false);
    					}
                    }
                }).start();
            }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);

        
    }

       /**
     * Lo que queremos hacer antes de salir de la pantalla
     */
    public void exitingToRight() throws Exception
    {
        bienesSeleccionados= bienesJPanel.getBienesSeleccionados();
    	if (bienesSeleccionados.size()==0 ){
    	   JOptionPane optionPane= new JOptionPane(aplicacion.getI18nString("inventario.bienesrevertibles.seleccionarunbien"),JOptionPane.WARNING_MESSAGE);
      	   JDialog dialog =optionPane.createDialog(this,aplicacion.getI18nString("inventario.bienesrevertibles.seleccionarbienes"));
      	   dialog.setVisible(true);
      	   setNextID("");
      	   bienesSeleccionados=null;
      	   return;   
  	   	}
    	setNextID(null);
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
        return "3";
    }

    public String getInstructions()
    {
        return " ";
    }

    public boolean isInputValid()
    {
    	return true;
    }

    private String nextID = null;
    
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
    	 //logger =null;
         //aplicacion = null;
         //blackboard = null;
         //wizardContext=null;
         //tipoBienJLabel = null;
         //tipoBienEJCBox = null;
    	 bienesJPanel=null;
    }
	public Collection getBienesSeleccionados() {
		return bienesSeleccionados;
	}
	public void setBienesSeleccionados(Collection bienesSeleccionados) {
		this.bienesSeleccionados = bienesSeleccionados;
	}
    

} // de la clase general.
