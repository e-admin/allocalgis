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

package com.geopista.app.catastro.intercambio.importacion;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.XMLReader;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileImportResultPanel;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportacionOperations;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.intercambio.importacion.xml.handlers.UnidadDatosIntercambioXMLHandler;
import com.geopista.app.catastro.model.beans.PadronFile;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class MostrarPadronMunicipal extends JPanel implements WizardPanel
{
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    
    private JEditorPane jEditorPaneResultadoImportacion = null;   
    
    private StringBuffer sbMessage = null;    
    
    private int totalRows=0;    
    private int insertedRows=0;
    private int notInsertedRows=0;
    
    
    private String nextID = null;
    private PadronFile pf;
    
    
    public MostrarPadronMunicipal()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",
                loc,this.getClass().getClassLoader());
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
                            
                            //La importación es diferente según se trate de un fichero txt o xml
                            //TXT
                            if (blackboard.get(ImportarUtils_LCGIII.FILE_TYPE).toString().equals(I18N.get("Importacion","importar.general.tiposfichero.texto")))
                            {
                                pf = new PadronFile();
                                
                                String linea;
                                File file = new File(rutaImp);
                                
                                //UNA O VARIAS LINEAS
                                InputStream is = new FileInputStream(file); 
                                int numRegistros = (int)file.length()/ImportarPadronMunicipal.LONGITUD_REGISTRO;
                                
                                byte[] bytes = new byte[ImportarPadronMunicipal.LONGITUD_REGISTRO];                                                
                                int len;    
                                totalRows = 0;
                                
                                while ((len = is.read(bytes)) > 0 )
                                {
                                    linea = new String(bytes);
                                    progressDialog.report(++totalRows, numRegistros,
                                            application.getI18nString("ImportandoDatos").toString());
                                    
                                    //cuenta_lineas ++;
                                    if (totalRows==1 &&
                                            bytes[0]==ImportarPadronMunicipal.inicioCabecera[0] && 
                                            bytes[1]==ImportarPadronMunicipal.inicioCabecera[1])
                                        pf.setHeaderInformation(linea);
                                    
                                    
                                    else if (totalRows == numRegistros && 
                                            bytes[0]==ImportarPadronMunicipal.inicioCola[0] && 
                                            bytes[1]==ImportarPadronMunicipal.inicioCola[1])
                                        pf.setTailInformation(linea);
                                    
                                    else if (bytes[0]==ImportarPadronMunicipal.inicioBienInmueble[0] && 
                                            bytes[1]==ImportarPadronMunicipal.inicioBienInmueble[1])
                                    {      
                                        try
                                        {                                            
                                            ImportacionOperations oper = new ImportacionOperations();
                                            oper.insertarRegistroBI(linea);
                                            insertedRows++;
                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                            notInsertedRows++;
                                        }
                                    }
                                    else if (bytes[0]==ImportarPadronMunicipal.inicioTitular[0] && 
                                            bytes[1]==ImportarPadronMunicipal.inicioTitular[1])
                                    {  
                                        try
                                        {                                            
                                            ImportacionOperations oper = new ImportacionOperations();
                                            oper.insertarRegistroTitular(linea);
                                            insertedRows++;
                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                            notInsertedRows++;
                                        }
                                        
                                    }
                                  
                                    //si tiene retornos de carro, se salta la lectura de estos 2 bytes
                                    if((new Boolean(blackboard.get(ImportarUtils_LCGIII.FILE_TXT_MULTILINE).toString()))
                                            .booleanValue())
                                        is.skip(2);
                                }     
                            }
                            //XML
                            else
                            { 
                                pf = new PadronFile();
                                notInsertedRows =0;                           
                                insertedRows=0;
                                totalRows=0;
                                blackboard.put("ValorTotal", new Long(0));
                                blackboard.put("ValorSuelo", new Long(0));
                                blackboard.put("ValorConstruccion", new Long(0));
                                blackboard.put("BaseLiquidable", new Long(0));
                                
                                
                                XMLReader parser = new SAXParser();                            
                                parser.setFeature("http://apache.org/xml/features/validation/schema", true);
                                parser.setFeature("http://xml.org/sax/features/validation", true);
                                
                                //ArrayList instancias = new ArrayList ();
                                blackboard.put("cancel", new Boolean(false));
                                parser.setContentHandler(
                                        new UnidadDatosIntercambioXMLHandler(parser, progressDialog, pf));
                                parser.parse(rutaImp);
                                
                                                                
                                insertedRows = ((Integer)blackboard.get("UnidadesInsertadas")).intValue();
                                notInsertedRows = ((Integer)blackboard.get("UnidadesNoInsertadas")).intValue();
                                totalRows = insertedRows + notInsertedRows;
                                
                                pf.setValorTotal(((Long)blackboard.get("ValorTotal")).longValue());
                                pf.setValorSuelo(((Long)blackboard.get("ValorSuelo")).longValue());
                                pf.setValorConstruccion(((Long)blackboard.get("ValorConstruccion")).longValue());
                                pf.setBaseLiquidable(((Long)blackboard.get("BaseLiquidable")).longValue());
                                
                                progressDialog.report(I18N.get("Importacion","importar.general.proceso.grabando"));
                                
                            }
                            
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
        
        try
        {
            // Iniciamos la ayuda
        	String helpHS = "help/catastro/importadores/importadoresHelp_es.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this,"PadronCatastral", hs);
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
    }
    
    private void printFinalMessage()
    { 
        String cadFecha ="";
        if (pf.getFecha()!=null)
        {
            if(pf.getFecha().length()==8)
                cadFecha = pf.getFecha().substring(6,8) + "/" + pf.getFecha().substring(4,6) 
                + "/" +pf.getFecha().substring(0,4);
            else
                cadFecha = pf.getFecha();
        }
        
        if (pf.getHora()!=null)
        {
            if (pf.getHora().length()==6)
                cadFecha = cadFecha + " --> " +  pf.getHora().substring(0,2)+":"+
                pf.getHora().substring(2,4)+":"+ pf.getHora().substring(4,6);
            else
                cadFecha = cadFecha + " --> "+ pf.getHora();
        }
        
        NumberFormat formatter = new DecimalFormat("#.##");

        sbMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.fechahora"), cadFecha))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.codigoentidad"), pf.getCodigo()))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.descripcion"), pf.getDescripcion()))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.aniopadronibi"), pf.getAnio()))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.numeroregistros.total"), totalRows ));
        
        //if (pf.getValorTotal()!=0 && pf.getValorSuelo()!=0 && pf.getValorConstruccion()!=0 && pf.getBaseLiquidable()!=0)
        //{
            sbMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(
                    I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.valorcatastral.total"),
                    formatter.format((new Double(pf.getValorTotal()).doubleValue() /100))))
            .append(ImportarUtils.getStringBufferHtmlFormattedText(
                    I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.valorcatastral.suelo"),
                    formatter.format((new Double(pf.getValorSuelo()).doubleValue()/100))))
            .append(ImportarUtils.getStringBufferHtmlFormattedText(
                    I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.valorcatastral.construccion"), 
                    formatter.format((new Double(pf.getValorConstruccion()).doubleValue()/100))))
            .append(ImportarUtils.getStringBufferHtmlFormattedText(
                    I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.baseliquidable.total"), 
                    formatter.format((new Double(pf.getBaseLiquidable()).doubleValue()/100))));
        //}
      
        
        sbMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.numeroregistros.procesados"), insertedRows+notInsertedRows))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.numeroregistros.insertados"), insertedRows))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.asistente.padronmunicipal.finmensaje.numeroregistros.noinsertados"), notInsertedRows));
        
        insertedRows =0;
        notInsertedRows=0;
        
        jEditorPaneResultadoImportacion.setText(sbMessage.toString());
        jEditorPaneResultadoImportacion.setVisible(true);
        jEditorPaneResultadoImportacion.setEditable(false);
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
