/**
 * ValidarFichero.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileValidationPanel;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ValidarFichero extends JPanel implements WizardPanel
{   
    private AppContext application = (AppContext) AppContext.getApplicationContext();

    private WizardContext wizardContext; 
    private String nextID = null;
    

    //private JTextField jTextFieldFileName = null;
    
    //  Variables utilizadas para las validaciones
    boolean isMalformedRow = false;
    boolean campoMasaCorrectos = true;
    boolean campoHojaCorrecto = true;
    boolean campoParcelaCorrecto = true;
    boolean campoTipoCorrecto = true;
    boolean campoFechaAltaCorrecto = true;
    boolean campoFechaBajaCorrecto = true;
    
    private JButton jButtonVerExpediente= null;
    
    public ValidarFichero()
    {   
        try
        {     
            Locale loc=I18N.getLocaleAsObject();         
            ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",loc,this.getClass().getClassLoader());
            I18N.plugInsResourceBundle.put("Importacion",bundle);
            jbInit();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception
    {  
        this.setLayout(new GridBagLayout());        
        
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
                            FileValidationPanel panel = new FileValidationPanel(true);
                            jButtonVerExpediente = panel.getJButtonVerExpediente();
                            add(panel,  
                                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(10,10,10,10),0,0));
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
    public JButton getJButtonVerExpediente()
    {
        return jButtonVerExpediente;
    }
}  
