/**
 * GeopistaImportarUrbana2006.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileImportSelectPanel;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.plugin.Constantes;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarUrbana2006 extends JPanel implements WizardPanel
{
    //AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


    private String rutaFichero;
    private String nextID = "2";

    private JTextField txtFichero = new JTextField();
    private JButton jButtonAbrir = new JButton();   
    private JEditorPane jEditorPaneErrores = new JEditorPane();
    private StringBuffer textoEditor = null;    
    public int errorFich = 0;
    public int permisoAcceso = 0;
    public boolean acceso=true;
    private boolean continuar;
    private JTextField jTextFieldPersona = new JTextField();
    private JTextField jTextFieldOrganismo = new JTextField();  
    private JTextField jTextFieldFuente = new JTextField();
   
    private ApplicationContext aplicacion = AppContext.getApplicationContext();
    private Blackboard blackImportar = aplicacion.getBlackboard();

    private int returnVal = 0;

    private JFileChooser fc = new JFileChooser();
    private JTextField jTextFieldFicheroImportar= null;
    
    public static int LONGITUD_REGISTRO = 1000;
    
    public static byte[] inicioCabecera = new byte[]{0x30, 0x31}; //01
    
    public static byte[] inicioRegistro1 = new byte[]{0x35, 0x33}; //53
    public static byte[] inicioRegistro2 = new byte[]{0x35, 0x34}; //54
    
    public static byte[] inicioCola = new byte[]{0x39, 0x30}; //90
    
    
    public GeopistaImportarUrbana2006()
        {
   
            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                    .getMainFrame(), null);

            progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
            progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
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
        }

    private void jbInit() throws Exception
    {        
        FileImportSelectPanel panel = new FileImportSelectPanel();        
        setName(aplicacion
                .getI18nString("importar.asistente.urbana.2006.titulo1"));
        this.setLayout(new GridBagLayout());        
        panel.getLabelImagen().setIcon(IconLoader.icon("catastro.png"));
        jEditorPaneErrores = panel.getJEditorPaneErrores();   
        jButtonAbrir = panel.getJButtonFicheroImportar();
        jTextFieldFicheroImportar = panel.getJTextFieldFicheroImportar();
        
        jButtonAbrir.addActionListener(new ActionListener()
                {
            public void actionPerformed(ActionEvent e)
            {
                btnAbrir_actionPerformed(e);
            }
                });        
        
        panel.getJComboBoxDatosImportar().addItem(aplicacion.getI18nString("importar.asistente.urbana.tipocmb.padron2006"));        
        panel.getJComboBoxTipoFichero().addItem(aplicacion.getI18nString("fichero.texto"));
        add(panel,  
                new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
       
    } 

    public void enteredFromLeft(Map dataMap)
    {
        if(!aplicacion.isLogged())
        {
            aplicacion.login();
        }
        if(!aplicacion.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }
        GeopistaPermission geopistaPerm = new GeopistaPermission("Geopista.Importar.Padron.Urbana.2006");
        
        acceso = aplicacion.checkPermission(geopistaPerm, "Catastro");
        
        if (acceso)
        {
            jButtonAbrir.setEnabled(true);
            jTextFieldPersona.setEnabled(true);
            jTextFieldOrganismo.setEnabled(true);
            jTextFieldFuente.setEnabled(true);
        } else
        {
            jButtonAbrir.setEnabled(false);
            jTextFieldPersona.setEnabled(false);
            jTextFieldOrganismo.setEnabled(false);
            jTextFieldFuente.setEnabled(false);
        }
    }

    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        blackImportar.put("SujetoSinDNI",0);        
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
        return "1";
    }

    public String getInstructions()
    {
        return " ";
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

    private WizardContext wizardContext;

    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }

    public boolean isInputValid()
    {
        System.out.println("continuarvalor: " + continuar);
        if (acceso)
        {
            if (continuar)
            {
                return true;
            } else
            {
                return false;
            }
        } else
        {
            JOptionPane.showMessageDialog(this, aplicacion.getI18nString("SinAcceso"));
            return false;
        }


    }

    private void btnAbrir_actionPerformed(ActionEvent e)
    {

        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension("txt");
        filter.setDescription(aplicacion.getI18nString("importar.informacion.referencia.fichero.texto"));

        fc.setFileFilter(filter);
        fc.setFileSelectionMode(0);
        fc.setAcceptAllFileFilterUsed(false);

        File currentDirectory = (File) blackImportar
                .get(Constantes.LAST_IMPORT_DIRECTORY);

        fc.setCurrentDirectory(currentDirectory);

        returnVal = fc.showOpenDialog(this);

        blackImportar.put(Constantes.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("ValidandoDatos"));
        progressDialog.report(aplicacion.getI18nString("ValidandoDatos"));
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
                                    if (returnVal == JFileChooser.APPROVE_OPTION)
                                    {
                                        String cadenaTexto = "";
                                        textoEditor = new StringBuffer();
                                        rutaFichero = fc.getSelectedFile().getPath();
                                        txtFichero.setText(rutaFichero);

                                        /* Recorro el fichero */                                       
                                        String Ruta = fc.getSelectedFile().getPath();
                                        int cuenta_lineas=0;
                                        File file = new File(Ruta);
                                       
                                        try
                                        {   
                                            InputStream is = new FileInputStream(file);   
                                            long numRegistros = file.length()/LONGITUD_REGISTRO;
                                             
                                            if (file.length()%LONGITUD_REGISTRO!=0)
                                            {
                                                cadenaTexto = cadenaTexto
                                                + "<p><font face=SansSerif size=3 color=#ff0000><b>"
                                                + aplicacion
                                                .getI18nString("importar.asistente.urbana.longitud.registros")
                                                + "</b></font></p>";
                                                continuar = false;
                                            }
                                            else
                                            {                                                
                                                continuar = true;
                                                
                                                byte[] bytes = new byte[LONGITUD_REGISTRO];                                                
                                                int len;
                                                
                                                while ((len = is.read(bytes)) > 0 && continuar)
                                                {                                              
                                                    cuenta_lineas ++;
                                                    if (cuenta_lineas==1)
                                                    {
                                                        if( bytes[0]!=inicioCabecera[0] || bytes[1]!=inicioCabecera[1])
                                                        {
                                                            cadenaTexto = cadenaTexto
                                                            + "<p><font face=SansSerif size=3 color=#ff0000><b>"
                                                            + aplicacion
                                                            .getI18nString("importar.asistente.urbana.cabecera")
                                                            + "</b></font></p>";
                                                            continuar = false; //El registro de cabecera no comienza x 01
                                                            break;
                                                        }
                                                    }
                                                    else if (cuenta_lineas == numRegistros)
                                                    {
                                                        if ( bytes[0]!=inicioCola[0] || bytes[1]!=inicioCola[1])
                                                        {                                                        
                                                            cadenaTexto = cadenaTexto
                                                            + "<p><font face=SansSerif size=3 color=#ff0000><b>"
                                                            + aplicacion
                                                            .getI18nString("importar.asistente.urbana.cola")
                                                            + "</b></font></p>";
                                                            continuar = false;  //No ha encontrado el registro de Cola
                                                            break;
                                                        }    
                                                    }
                                                    else if ((bytes[0]!=inicioRegistro1[0] || bytes[1]!=inicioRegistro1[1])
                                                            && (bytes[0]!=inicioRegistro2[0] || bytes[1]!=inicioRegistro2[1]))
                                                    {                                                  
                                                        cadenaTexto = cadenaTexto
                                                        + "<p><font face=SansSerif size=3 color=#ff0000><b>"
                                                        + aplicacion
                                                        .getI18nString("importar.asistente.urbana.incorrecto")
                                                        + " " + cuenta_lineas + ")"
                                                        + "</b></font></p>";
                                                        continuar = false;
                                                        break;
                                                    }        
                                                   
                                                }//while
                                                
                                               
                                                
                                            } 
                                        if (continuar == true)
                                        {
                                            cadenaTexto = cadenaTexto
                                            + aplicacion
                                                    .getI18nString("validar.fin.correcto");
                                            blackImportar.put("rutaFicheroImportar", rutaFichero);
                                        }
                                        
                                        jTextFieldFicheroImportar.setText(rutaFichero);
                                        wizardContext.inputChanged();
                                        cadenaTexto = cadenaTexto + aplicacion.getI18nString("validacion.finalizada");
                                        jEditorPaneErrores.setText(cadenaTexto);
                                        }
                                        catch (Exception ex)
                                        {
                                            ex.printStackTrace();
                                        }
                                    }
                                    else
                                    {
                                        //Aprove option
                                    }
                                }
                               catch(Exception e)
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

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
} // de la clase general.

