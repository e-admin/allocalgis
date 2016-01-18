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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.geopista.app.AppContext;

import com.geopista.ui.images.IconLoader;
import com.geopista.ui.plugin.GeopistaValidatePlugin;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;

import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarPadronRusticaFin extends JPanel implements WizardPanel
{
    private ApplicationContext aplicacion = AppContext.getApplicationContext();
    
    private Blackboard blackImportar = aplicacion.getBlackboard();
    
    private JScrollPane scpErrores = new JScrollPane();
    
    private JEditorPane ediError = new JEditorPane();
    
    private JPanel pnlVentana = new JPanel();
    
    private JLabel lblImportar = new JLabel();
    
    private GeopistaValidatePlugin geopistaValidatePlugIn = new GeopistaValidatePlugin();
    
    private JLabel lblImagen = new JLabel();
    
    private JSeparator jSeparator4 = new JSeparator();
    
    private JSeparator jSeparator5 = new JSeparator();
    
    private JLabel lblDatos = new JLabel();
    
    private boolean validaFeature;
    
    private int totalInsertados = 0;
    
    private int totalNoInsertados = 0;
    
    private String cadenaTexto;
    
    private Collection totalFeaturesToInsert = null;
    
    private static final int DUPLICAR = 2;
    
    private static final int BORRAR = 0;
    
    private static final int CANCELAR = 1;
    private int parcelasInsertadas=0;
    private int parcelasNoInsertadas=0;
    private int totalRegistros=0;
    private GeopistaImportarRustica2005Parcelas Validar = new GeopistaImportarRustica2005Parcelas();
    
    private String fecha;
    private String codigo;
    private String procedencia;
    private String descripcion;
    private String anyo;
    private String cadenafecha;
    private String envio;
    private long total;
    private long valorcatastral;
    private long valorsuelo;
    private long valorconstruccion;
    private long baseliquidable;
    
    public GeopistaImportarPadronRusticaFin()
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
                            setName(aplicacion.getI18nString("importar.log.titulo"));
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
        
        // String tipoImp = (String) blackImportar.get("tipoImport");
        
        jSeparator4.setBounds(new Rectangle(135, 20, 605, 2));
        scpErrores.setBounds(new Rectangle(134, 52, 595, 442));
        this.setLayout(null);
        lblDatos.setBounds(new Rectangle(135, 25, 260, 20));
        
        lblDatos.setText(aplicacion.getI18nString("importar.usuario.paso4.log"));
        lblImagen.setIcon(IconLoader.icon("catastro.png"));
        lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        
        ediError.setContentType("text/html");
        this.setSize(750, 600);
        
        jSeparator5.setBounds(new Rectangle(135, 505, 605, 2));
        scpErrores.getViewport().add(ediError, null);
        this.add(scpErrores, null);
        this.add(lblDatos, null);
        this.add(lblImagen, null);
        this.add(jSeparator4, null);
        this.add(jSeparator5, null);
    }
    
    public void enteredFromLeft(Map dataMap)
    {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);
        
        progressDialog.setTitle(aplicacion.getI18nString("ImportandoDatos"));
        // progressDialog.report(aplicacion.getI18nString("ImportandoDatos"));
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
                            String rutaImp = (String) blackImportar.get("rutaFicheroImportar");
                            String linea;
                            
                            boolean correctoImp=true;
                            File file = new File(rutaImp);                                     
                            InputStream is = new FileInputStream(file);
                            
                            int len=0;
                            byte[] bytes = new byte[GeopistaImportarRustica2005.LONGITUD_REGISTRO];    
                            while ((len = is.read(bytes)) > 0 )
                            {
                                linea = new String(bytes);
                                
                                if( bytes[0]==GeopistaImportarRustica2005.inicioCabecera[0] &&
                                        bytes[1]==GeopistaImportarRustica2005.inicioCabecera[1])
                                {
                                    fecha=linea.substring(2,16);
                                    
                                    codigo=linea.substring(16,19);
                                    procedencia=linea.substring(19,49);
                                    descripcion=linea.substring(49,89);
                                    anyo=linea.substring(89,93);
                                    envio=linea.substring(93,104);
                                    cadenafecha =  fecha.substring(6,8) + "/" + fecha.substring(4,6) + "/" +fecha.substring(0,4)+ " --> " +  fecha.substring(8,10)+":"+fecha.substring(10,12)+":"+ fecha.substring(12,14);                                    
                                }
                                
                                else if (bytes[0]==GeopistaImportarRustica2005.inicioRegistro[0] && 
                                        bytes[1]==GeopistaImportarRustica2005.inicioRegistro[1])
                                {
                                    totalRegistros = totalRegistros + 1;
                                    progressDialog.report(aplicacion.getI18nString("ImportandoDatos")+ " Registro nº :  " + totalRegistros);
                                    correctoImp = Validar.IncluirParcela(linea);
                                    if (correctoImp == true)
                                    {
                                        parcelasInsertadas = parcelasInsertadas + 1;
                                    }
                                    else
                                    {
                                        parcelasNoInsertadas = parcelasNoInsertadas + 1;
                                    }                                    
                                }
                                
                                
                                else if ( bytes[0]==GeopistaImportarRustica2005.inicioCola[0] &&
                                        bytes[1]==GeopistaImportarRustica2005.inicioCola[1])
                                {
                                    total=Long.parseLong(linea.substring(2,9));
                                    valorcatastral=Long.parseLong(linea.substring(9,24));                                    
                                }
                            }
                            
                            progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
                            
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
        
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
        String date = (String) formatter.format(new Date());
        String cadena="";
        blackImportar = aplicacion.getBlackboard();
        cadena = cadena + "<p><b>"+ aplicacion.getI18nString("importar.log.parcelas")+": </b>" + blackImportar.get("totalParcelas") +"</p>";
        cadena = cadena + "<p><b>"+ aplicacion.getI18nString("importar.log.titulares")+": </b>" + blackImportar.get("totalTitulares") +"</p>";
        
        int numSujetosSinDNI = new Integer(blackImportar.get("SujetoSinDNI").toString()).intValue();
        if (numSujetosSinDNI>0)
        {
            cadena = cadena + "<p><b>"+ aplicacion.getI18nString("importar.log.titulares.sinnif")+": </b>" + numSujetosSinDNI +"</p>";
        }
        
        finalMessage();
    }
    
    private void finalMessage()
    {
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
        String date = (String) formatter.format(new Date());
        /*cadenaTexto = "<p><b>" + aplicacion.getI18nString(modulo)
         + "</b></p>";*/
        
        cadenaTexto = "<p><b>" + aplicacion.getI18nString("importar.log.cabecera.fecha") + ":</b>" + cadenafecha + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.cabecera.codigo") + ":</b>" + codigo + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.cabecera.procedencia") + ":</b>" + procedencia + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.cabecera.descripcion") + ":</b>" + descripcion + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.cabecera.envio") + ":</b>" + envio + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.cabecera.anyo") + ":</b>" + anyo + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.cola.total") + ":</b>" + total + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.valorcatastral") + ":</b>" + valorcatastral + "</p>";
        
        /*cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("Fecha")
         + ":</b>" + date + "</p>";*/
        cadenaTexto = cadenaTexto + "<p><b>"
        + aplicacion.getI18nString("importar.usuario.paso4.Registros")
        + ":</b>" + totalRegistros
        + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>"
        + aplicacion.getI18nString("importar.usuario.paso4.Insertados")
        + ":</b>" + parcelasInsertadas + "</p>";
        cadenaTexto = cadenaTexto
        + "<p><b>"
        + aplicacion
        .getI18nString("importar.usuario.paso4.NoInsertados")
        + ":</b>" + parcelasNoInsertadas + "</p>";
        
        int numSujetosSinDNI = new Integer(blackImportar.get("SujetoSinDNI").toString()).intValue();
        if (numSujetosSinDNI>0)
        {
            cadenaTexto = cadenaTexto
            + "<p><b>"
            + aplicacion.getI18nString("importar.log.titulares.sinnif")
            + ":</b>" + numSujetosSinDNI + "</p>";
        }
        
        ediError.setText(cadenaTexto);
        ediError.setVisible(true);
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
    
    private String nextID = null;
    
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
    
    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting()
    {
        // TODO Auto-generated method stub
        
    }
    
}
