/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/
package com.geopista.app.catastro;

/**
 * @author hgarcia
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.plugin.Constantes;
import com.geopista.security.GeopistaPermission;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarPadron extends JPanel implements WizardPanel
{
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private String cadenaTexto = null;
    
    private Blackboard blackboard = aplicacion.getBlackboard();
    
    private JScrollPane scpErrores = new JScrollPane();
    
    private JLabel lblImagen = new JLabel();
    
    private JLabel lblFichero = new JLabel();
    
    private JTextField txtFichero = new JTextField();
    
    private JButton btnAbrir = new JButton();
    
    private JLabel lblErrores = new JLabel();
    
    private boolean hayErroresFilas = false;
    
    private JLabel lblTipoFichero = new JLabel();
    
    private JComboBox cmbTipoInfo = new JComboBox();
    
    private JEditorPane ediError = new JEditorPane();
    
    private boolean permiso;
    
    private JFileChooser fc = new JFileChooser();
    
    private int returnVal = 0;

    private WizardContext wizardContext;
        
    private JComboBox jComboBox1 = new JComboBox();
    
    private JLabel lblComent = new JLabel();
    
    private String errorMessage = "";
    
    private Connection con = null;
    
    private Hashtable htMunicipios = new Hashtable();
    
    private final int LONG_FICHERO=554;
    private final int COD_EXTRANJERO=66;
    
    public GeopistaImportarPadron()
    {
        try
        {
            jbInit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception
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
                            permiso = true;
                            setLayout(null);
                            setName(aplicacion
                                    .getI18nString("importar.asistente.elementos.padron.titulo1"));
                            jComboBox1.addItem(aplicacion
                                    .getI18nString("fichero.texto"));
                            
                            hayErroresFilas = true;
                            scpErrores
                            .setBounds(new Rectangle(135, 200, 595, 300));
                            
                            cmbTipoInfo.addItem(aplicacion.getI18nString("importar.padron"));
                            cmbTipoInfo.setSelectedIndex(0);
                            
                            btnAbrir.setIcon(IconLoader.icon("abrir.gif"));
                            
                            lblImagen.setIcon(IconLoader
                                    .icon("inf_referencia.png"));
                            lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                            lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                            lblFichero.setText(aplicacion.getI18nString("importar.informacion.referencia.fichero.importar"));
                            lblFichero.setBounds(new Rectangle(135, 115, 240, 20));
                            txtFichero.setBounds(new Rectangle(375, 115, 280, 20));
                            txtFichero.setEditable(false);
                            
                            btnAbrir.setBounds(new Rectangle(665, 115, 25, 20));
                            btnAbrir.addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {
                                    btnAbrir_actionPerformed();
                                }
                                    });
                            
                            lblErrores.setText(aplicacion.getI18nString("importar.informacion.referencia.errores.validacion"));
                            lblErrores.setBounds(new Rectangle(135, 180, 510, 20));
                            lblTipoFichero.setText(aplicacion.getI18nString("importar.informacion.referencia.tipo.fichero"));
                            lblTipoFichero.setBounds(new Rectangle(140, 85, 130, 20));
                            cmbTipoInfo.setBackground(new Color(255, 255, 255));
                            cmbTipoInfo.setBounds(new Rectangle(375, 85, 215, 20));
                            cmbTipoInfo.addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {
                                    cmbTipoInfo_actionPerformed();
                                }
                                    });
                            
                            ediError.setText("jEditorPane1");
                            ediError.setContentType("text/html");
                            ediError.setEditable(false);
                            add(lblComent, null);
                            add(jComboBox1, null);
                            add(cmbTipoInfo, null);
                            add(lblTipoFichero, null);
                            add(lblErrores, null);
                            add(btnAbrir, null);
                            add(txtFichero, null);
                            add(lblFichero, null);
                            add(lblImagen, null);
                            
                            scpErrores.getViewport().add(ediError, null);
                            add(scpErrores, null);
                            
                            
                        } catch (Exception e)
                        {
                            
                        } finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                        }).start();
            }
                });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        
    }// jbinit
    
    public void enteredFromLeft(Map dataMap)
    {
        
        if (!aplicacion.isLogged())
        {
            aplicacion.login();
        }
        if (!aplicacion.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }
        
        
        //permisos
        
        GeopistaPermission paso = new GeopistaPermission(
        "Geopista.InfReferencia.Importar Padron");
        permiso = aplicacion.checkPermission(paso, "Informacion de Referencia");
        
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        // Salimos y ponemos en el blackBoard el mapaCagarTramosVias
        blackboard.put("ficheroPadron", txtFichero.getText());
        //blackboard.put("rutaFicheroCarVia", txtCardVia.getText());
        blackboard.put("tipoImportarPadron", cmbTipoInfo.getSelectedItem());
        /*String Huso = (String) jComboSrid.getSelectedItem();
         String[] srid = Huso.split(">");
         
         
         blackboard.put(MUNICIPIOSRID, srid[srid.length-1].trim());*/
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
    
    public boolean isInputValid()
    {   
        if (permiso)
        {           
            return (!hayErroresFilas);
        } 
        else
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("NoPermisos"));
            return false;
        }
    }
    
    private String nextID = "2";
    
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
    /**
     * Función que recoge el evento del boton abrir
     */
    private void btnAbrir_actionPerformed()
    {
        
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();

        filter.addExtension("txt");
        filter.setDescription(aplicacion
                .getI18nString("importar.informacion.referencia.fichero.texto"));
        
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);
        
        File currentDirectory = (File) blackboard
        .get(Constantes.LAST_IMPORT_DIRECTORY);
        
        fc.setCurrentDirectory(currentDirectory);
        returnVal = fc.showOpenDialog(this);
        blackboard.put(Constantes.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());
        
        if (returnVal != JFileChooser.APPROVE_OPTION)
            return;
        
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
                            if(con==null)
                            {
                                con = getDBConnection();
                            }
                            errorMessage = "";
                            txtFichero.setText(fc.getSelectedFile().getPath());
                            
                            cadenaTexto = "<font face=SansSerif size=3>"
                                + aplicacion
                                .getI18nString("ImportacionComenzar")
                                + "<b>" + " "
                                + fc.getSelectedFile().getName() + "</b>";
                            cadenaTexto = cadenaTexto
                                + "<p>"
                                + aplicacion
                                .getI18nString("OperacionMinutos")
                                + " ...</p></font>";
                                ediError.setText(cadenaTexto);
                            cadenaTexto = "";
                            
                            boolean validado = validarDatosPadron(txtFichero.getText(), progressDialog);
                            
                            if (validado)
                            {
                                hayErroresFilas = false;
                                blackboard.put("rutaFicheroImportar", txtFichero.getText());
                            
                                cadenaTexto = cadenaTexto
                                    + aplicacion.getI18nString("importar.datos.padron.correctos");
                               
                            } else
                            {
                                showErrorMessage();
                            }
                           
                        } catch (Exception e)
                        {
                            
                        } finally
                        {
                            aplicacion.closeConnection(con, null, null, null);
                            progressDialog.setVisible(false);
                            
                        }
                    }
                        }).start();
            }
                });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        
        cadenaTexto = cadenaTexto + aplicacion.getI18nString("validacion.finalizada");
        ediError.setText(cadenaTexto);
        wizardContext.inputChanged();
        
    }
    /**
     * Función que recoge el evento de cambiio en el combo
     */
    private void cmbTipoInfo_actionPerformed()
    {   
        this.txtFichero.setText(null);
        this.ediError.setText(null);
    }
    
    
    /**
     * Método para determinar si las filas del fichero de texto cumplen las
     * validaciones 
     * 
     */
    
    public boolean validarDatosPadron(String ruta, TaskMonitorDialog  progressDialog)
    {
        String str = null;
        boolean resultado = true;
        int numFila = 0 ;
        
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            // Se comprueba el tamaño de la linea         
            while ((str = reader.readLine()) != null)
            {
                int idMunicipio = 0;
                int idProvincia = 0;
                
                if (str.length()!= LONG_FICHERO)
                {                    
                    return false;                    
                }
                else
                {                    
                     //Extraer variables para validarlas
                    //   - El municipio tiene que existir en la tabla de municipios
                    try
                    {
                        idMunicipio = Integer.parseInt(str.substring(0, 5).trim());
                        idProvincia = Integer.parseInt(str.substring(0, 2).trim());
                       
                        if (idMunicipio!=0 && idProvincia!=COD_EXTRANJERO){
                            
                            resultado = comprobarMunicipio(idMunicipio);
                            if (!resultado)
                            {
                                makeErrorMessage(numFila, "importar.municipio.no.identificador");
                            }
                        }  
                        
                    }
                    catch(Exception e)
                    {
                        return false;
                    }
                                        
                    // no se realizan de momento mas comprobaciones
                    //TODO Se puede comprobar que los idprovincia e idmunicipio corresponden al que realiza la importación
                    
                }                
                progressDialog.report(aplicacion.getI18nString("ValidandoDatos")+" "+numFila);
                numFila++;
            }
            
            aplicacion.getBlackboard().put("numFilas", numFila);
            
        } 
        catch (Exception e)
        {
            errorMessage = errorMessage + aplicacion.getI18nString("importar.en.la.fila")
                + " " + numFila + " "
                + aplicacion.getI18nString("importar.padron.incorrecto");
            return false;
        }       
        
        return true;
        
    }    
    
    /**
     * Realiza la conexión con la base de datos
     * @return Devuelve la conexión establecida con la base de datos
     */ 
    public static Connection getDBConnection () throws SQLException
    {
        Connection conn= aplicacion.getConnection();
        return conn;
    }
        
    /**
     * Método que comprueba si existe un municipio
     * 
     * @param  idMunicipio
     * @return boolean true si el municipio existe
     */
    public boolean comprobarMunicipio(int idMunicipio)
    {
        
        if (htMunicipios.get(new Integer(idMunicipio))!=null)
        {
            return true;
        }
        
        
        PreparedStatement ps = null;
        ResultSet r = null;
        
        try
        {
            ps = con.prepareStatement("comprobarIdIneMunicipio");
            ps.setInt(1, idMunicipio);
            if (!ps.execute())
            {
                return false;
            } 
            else
            {
                r = ps.getResultSet();
                if (r.next())
                {
                    htMunicipios.put(new Integer(idMunicipio), r.getObject(1)); 
                    return true;
                }
            }          
            
            
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
            
        } finally
        {
            aplicacion.closeConnection(null, ps, null, r);
        }
        return false;
    }
    
    
    private void showErrorMessage()
    {
        hayErroresFilas = true;
        cadenaTexto = cadenaTexto + errorMessage
        + aplicacion.getI18nString("importar.informacion.ficheros.no.correctos");
        
    }
    
    private void makeErrorMessage(int fila, String localErrorMessage)
    {
        errorMessage = errorMessage + aplicacion.getI18nString("importar.en.la.fila")
        + " " + fila + " " + aplicacion.getI18nString(localErrorMessage);
    }
    
    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {

    }
    
} // de la clase general.

