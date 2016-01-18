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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileImportResultPanel;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaImportarPadronFin2006 extends JPanel implements WizardPanel
{
    private ApplicationContext aplicacion = AppContext.getApplicationContext();
    
    private Blackboard blackImportar = aplicacion.getBlackboard();
    
       
    private JEditorPane jEditorPaneResultadoImportacion = new JEditorPane();   
   
    
    private String cadenaTexto;    
   
    
    private int totalRegistros=0;
    
    private int registrosInsertados=0;
    private int registrosNoInsertados=0;
    
    private GeopistaImportarUrbana2006Parcelas Validar = new GeopistaImportarUrbana2006Parcelas();
    
    private String fecha;
    private String codigo;
    private String procedencia;
    private String descripcion;
    private String anyo;
    private String cadenafecha;
    private long total;
    private long valorcatastral;
    private long valorsuelo;
    private long valorconstruccion;
    private long baseliquidable;
    
    public GeopistaImportarPadronFin2006()
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
        
        this.setLayout(new GridBagLayout());
        FileImportResultPanel panel = new FileImportResultPanel();
        jEditorPaneResultadoImportacion= panel.getJEditorPaneResultadoImportacion();
        panel.getLabelImagen().setIcon(IconLoader.icon(MainCatastro.BIG_PICTURE_LOCATION));
        add(panel,  
                new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
             
        
    }
    
    public void enteredFromLeft(Map dataMap)
    {
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);   
        
        progressDialog.setTitle(aplicacion.getI18nString("ImportandoDatos"));
        progressDialog.report(aplicacion.getI18nString("ImportandoDatos"));
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
                                    correctoImp = Validar.IncluirParcela(linea);
                                    if (correctoImp)
                                    {
                                        registrosInsertados++;
                                    }
                                    else
                                    {
                                        registrosNoInsertados++;
                                    }
                                    
                                }
                                
                                else if(linea.substring(0,2).equals("54")) //Titular catastral
                                {
                                    totalRegistros++;
                                    correctoImp = Validar.IncluirTitular(linea);
                                    if (correctoImp)
                                    {
                                        registrosInsertados++;
                                    }
                                    else
                                    {
                                        registrosNoInsertados++;
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
                            
                            progressDialog.report(aplicacion.getI18nString("GrabandoDatosBaseDatos"));
                            
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
        //progressDialog.setVisible(true);
        
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
        String date = (String) formatter.format(new Date());
        String cadena="";
        blackImportar = aplicacion.getBlackboard();
        cadena = cadena + "<p><b>"+ aplicacion.getI18nString("importar.log.parcelas")+": </b>" + blackImportar.get("totalParcelas") +"</p>";
        cadena = cadena + "<p><b>"+ aplicacion.getI18nString("importar.log.titulares")+": </b>" + blackImportar.get("totalTitulares") +"</p>";
        
        
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        
        
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
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.cabecera.anyo") + ":</b>" + anyo + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.cola.total") + ":</b>" + total + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.valorcatastral") + ":</b>" + valorcatastral + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.valorcatastralsuelo") + ":</b>" + valorsuelo + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.valorconstruccion") + ":</b>" + valorconstruccion + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("importar.log.baseliquidable") + ":</b>" + baseliquidable + "</p>";
        
        /*cadenaTexto = cadenaTexto + "<p><b>" + aplicacion.getI18nString("Fecha")
         + ":</b>" + date + "</p>";*/
        cadenaTexto = cadenaTexto + "<p><b>"
        + aplicacion.getI18nString("importar.usuario.paso4.Registros")
        + ":</b>" + totalRegistros
        + "</p>";
        cadenaTexto = cadenaTexto + "<p><b>"
        + aplicacion.getI18nString("importar.usuario.paso4.Insertados")
        + ":</b>" + registrosInsertados + "</p>";
        cadenaTexto = cadenaTexto
        + "<p><b>"
        + aplicacion
        .getI18nString("importar.usuario.paso4.NoInsertados")
        + ":</b>" + registrosNoInsertados + "</p>";
        
        
        int numSujetosSinDNI = new Integer(blackImportar.get("SujetoSinDNI").toString()).intValue();
        if (numSujetosSinDNI>0)
        {
            cadenaTexto = cadenaTexto
            + "<p><b>"
            + aplicacion.getI18nString("importar.log.titulares.sinnif")
            + ":</b>" + numSujetosSinDNI + "</p>";
        }      
        
        jEditorPaneResultadoImportacion.setText(cadenaTexto);
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
