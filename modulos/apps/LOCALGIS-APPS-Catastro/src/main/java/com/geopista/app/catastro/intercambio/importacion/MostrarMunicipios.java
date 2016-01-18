/**
 * MostrarMunicipios.java
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileImportResultPanel;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class MostrarMunicipios extends JPanel implements WizardPanel
{
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    
    
    private JEditorPane jEditorPaneResultadoImportacion = null;   
    
    private StringBuffer sbMessage = null;    
    
    private int totalRows=0;    
    private int insertedRows=0;
    private int notInsertedRows=0;
    
    private String nextID = null;
    
    
    public MostrarMunicipios()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Importacion",bundle);
        
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application
                .getMainFrame(), null);
        
        progressDialog.setTitle(application.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(application.getI18nString("CargandoDatosIniciales"));
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
                            jbInit();
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
    
    private void jbInit() throws Exception
    {     
        this.setLayout(new GridBagLayout());
        FileImportResultPanel panel = new FileImportResultPanel();
        jEditorPaneResultadoImportacion = panel.getJEditorPaneResultadoImportacion();
        panel.getLabelImagen().setIcon(IconLoader.icon(MainCatastro.BIG_PICTURE_LOCATION));
        this.add(panel,  
                new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
        
    }
    
    /**
     * Realiza el proceso de importación en cuanto se accede a esta pantalla 
     */    
    public void enteredFromLeft(Map dataMap)
    {
        sbMessage = new StringBuffer();
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application
                .getMainFrame(), null);   
        
        progressDialog.setTitle(I18N.get("Importacion","importar.general.proceso.importando"));
        progressDialog.report(I18N.get("Importacion","importar.general.proceso.importando"));
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
                            String rutaImp = (String) blackboard.get(ImportarUtils_LCGIII.FILE_TO_IMPORT);
                            String linea;
                            int cuenta_lineas=0;
                            boolean correctoImp=true;
                            File file = new File(rutaImp);
                            
                            FileReader fr = null;
                            fr = new FileReader(file);
                            BufferedReader br = new BufferedReader (fr);
                            while ( (linea = br.readLine()) != null )
                            {
                                cuenta_lineas++;
                            }
                            
                            
                            fr = null;
                            fr = new FileReader(file);
                            br = null;
                            br = new BufferedReader (fr);
                            /*
                             int n=0;
                             while ((linea = br.readLine())!=null)
                             {
                             
                             progressDialog.report(n++, cuenta_lineas-2,
                             aplicacion.getI18nString("ImportandoDatos").toString());
                             
                             if(linea.substring(0,2).equals("01"))//Cabecera
                             {
                             fecha=linea.substring(39,53);
                             //System.out.println("fecha: " + fecha);
                              codigo=linea.substring(3,12);
                              procedencia=linea.substring(12,39);
                              descripcion=linea.substring(57,96);
                              anyo=linea.substring(120,124);
                              
                              cadenafecha =  fecha.substring(6,8) + "/" + fecha.substring(4,6) 
                              + "/" +fecha.substring(0,4)+ " --> " +  fecha.substring(8,10)+":"+fecha.substring(10,12)+":"+ fecha.substring(12,14);
                              //System.out.println("Fecha: " + cadenafecha);
                               }
                               
                               else if(linea.substring(0,2).equals("53")) //Bien inmueble / sujeto pasivo
                               {
                               totalRegistros++;
                               correctoImp = true;//Validar.IncluirParcela(linea);
                               if (correctoImp)
                               {
                               insertedRows++;
                               }
                               else
                               {
                               notInsertedRows++;
                               }
                               
                               }
                               
                               else if(linea.substring(0,2).equals("54")) //Titular catastral
                               {
                               totalRegistros++;
                               correctoImp = true;//Validar.IncluirTitular(linea);
                               if (correctoImp)
                               {
                               insertedRows++;
                               }
                               else
                               {
                               notInsertedRows++;
                               }
                               
                               }
                               
                               else if(linea.substring(0,2).equals("90"))//Cola
                               {
                               total=Long.parseLong(linea.substring(2,9));
                               valorcatastral=Long.parseLong(linea.substring(44,58));
                               valorsuelo=Long.parseLong(linea.substring(58,72));
                               valorconstruccion=Long.parseLong(linea.substring(72,86));
                               baseliquidable=Long.parseLong(linea.substring(86,100));
                               }
                               }
                               */
                            progressDialog.report(I18N.get("Importacion","importar.general.proceso.grabando"));
                            
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                        }).start();
            }
                });
        GUIUtil.centreOnWindow(progressDialog);
        
        progressDialog.setVisible(true);
        
        printFinalMessage();
    }
    
    private void printFinalMessage()
    { 
        
        sbMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.fecha.finalizacion"), ImportarUtils.getDate()))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.unidadesdatos.procesados"), totalRows))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.unidadesdatos.insertados"), insertedRows))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.unidadesdatos.no.insertados"), notInsertedRows));
        
        jEditorPaneResultadoImportacion.setText(sbMessage.toString());
        jEditorPaneResultadoImportacion.setVisible(true);
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
        return "";
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
    }
    
    public void exiting()
    {
        
    }    
}
