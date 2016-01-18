package com.geopista.app.catastro.intercambio.importacion;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.dialogs.InformationImportProgressPanel;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class MostrarProgresoIntercambio extends JPanel implements WizardPanel
{
    private boolean permiso = true;
    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    private WizardContext wizardContext; 
    private String nextID = null;
    private InformationImportProgressPanel panel;
     
    public MostrarProgresoIntercambio(String title)
    {   
        try
        {     
            Locale loc=I18N.getLocaleAsObject();         
            ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",loc,this.getClass().getClassLoader());
            I18N.plugInsResourceBundle.put("Importacion",bundle);
            jbInit(title);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit(final String title) throws Exception
    {  
        this.setLayout(new GridBagLayout());
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application.getMainFrame(), null);
        
        progressDialog.setTitle(I18N.get("Importacion","CargandoDatosIniciales"));
        progressDialog.report(I18N.get("Importacion","CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter()
                {
            public void componentShown(ComponentEvent e)
            {                
                // Wait for the dialog to appear before starting the
                // task. Otherwise
                // the task might possibly finish before the dialog
                // appeared and the
                // dialog would never close. [Jon Aquino]
                new Thread(new Runnable()
                        {
                    public void run()
                    {
                        try
                        {   
                            panel = 
                                new InformationImportProgressPanel(title);
                            add(panel,  
                                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
                            
                            panel.getLabelImagen().setIcon(IconLoader.icon(MainCatastro.SMALL_PICTURE_LOCATION));
                                                     
                        } 
                        catch (Exception e)
                        {
                            e.printStackTrace();
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
    
    public void enteredFromLeft(Map dataMap)
    {
        if(!application.isLogged())
        {
            application.login();
        }
        if(!application.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }
        
        GeopistaPermission paso = new GeopistaPermission("Geopista.InfReferencia.ImportarDatosIne");
        
        permiso = application.checkPermission(paso, "Informacion de Referencia");
        
        if(((boolean)blackboard.getBoolean("ConexionOVC"))){
        	panel.loadIconChecked(panel.getJLabelConectarOVC());
        }
        else{
        	panel.loadIconNoChecked(panel.getJLabelConectarOVC());
        }
        if(((boolean)blackboard.getBoolean("Validacion"))){
        	panel.loadIconChecked(panel.getJLabelValidarFichero());
        }
        else{
        	panel.loadIconNoChecked(panel.getJLabelValidarFichero());
        }
        if(((boolean)blackboard.getBoolean("Importacion"))){
        	panel.loadIconChecked(panel.getJLabelActualizarSistema());
        }
        else{
        	panel.loadIconNoChecked(panel.getJLabelActualizarSistema());
        }
        if(((boolean)blackboard.getBoolean("ConexionOVC"))&&
    			((boolean)blackboard.getBoolean("Validacion")) &&
        		((boolean)blackboard.getBoolean("Importacion"))){
            	
        	panel.loadIconChecked(panel.getJLabelProcesoFinalizado());
        }
        else{
        	panel.loadIconNoChecked(panel.getJLabelProcesoFinalizado());
        }       
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
               
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
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
        if (!permiso)
        {
            JOptionPane.showMessageDialog(application.getMainFrame(), application
                    .getI18nString("NoPermisos"));
            return false;
        } 
        else
        {
           return true;   
        }
    }
    
    
    
    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }
    
    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID()
    {
        return nextID;
    }
    
    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }       
    
    public void exiting()
    {   
    }
}  
