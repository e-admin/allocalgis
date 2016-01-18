/**
 * MostrarFINRetornoMasivo.java
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.DatosRegistroExpedientes;
import com.geopista.app.catastro.model.beans.Fichero;
import com.geopista.app.catastro.model.intercambio.importacion.xml.handlers.FinRetornoXMLHandler;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class MostrarFINRetornoMasivo extends JPanel implements WizardPanel
{
    private ApplicationContext application = AppContext.getApplicationContext();    
    private Blackboard blackboard = application.getBlackboard();
    
    
    private JEditorPane jEditorPaneResultadoImportacion = null;   
    
    private StringBuffer sbMessage = new StringBuffer();    
    
    private int totalRows=0;    
    private int insertedRows=0;
    private int notInsertedRows=0;
    
    private String nextID = "3";
    
    
    public MostrarFINRetornoMasivo()
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
                            notInsertedRows =0;                           
                            insertedRows=0;
                            totalRows=0;
                            
                            XMLReader parser = new SAXParser();                            
                            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
                            parser.setFeature("http://xml.org/sax/features/validation", true);
                            

                            blackboard.put("UnidadesInsertadas",new Integer(0));
                            blackboard.put("UnidadesNoInsertadas",new Integer(0));
                            blackboard.put("ListaDatosRegistro", new ArrayList());
                            
                            Fichero fich = new Fichero();
                            fich.setIdTipoFichero(Fichero.FIN_RETORNO);
                            
                            //Inicializar la lista total de parcelas importadas (se almacenan las ref.catastrales)
                            blackboard.put("ListaTotalFincasImportadas", new ArrayList());
                            parser.setContentHandler(
                                    new FinRetornoXMLHandler(parser, progressDialog, fich, true));
                            parser.parse(rutaImp);
                            
                            insertedRows = ((Integer)blackboard.get("UnidadesInsertadas")).intValue();
                            notInsertedRows = ((Integer)blackboard.get("UnidadesNoInsertadas")).intValue();
                            totalRows = insertedRows + notInsertedRows;                            
                     
                            progressDialog.report(I18N.get("Importacion","importar.general.proceso.grabando"));
                            ArrayList lstDatosRegistro = (ArrayList)blackboard.get("ListaDatosRegistro");
                            
                            // Llamada a método de REGISTRO DE EXPEDIENTES
                            Iterator itRegistro = lstDatosRegistro.iterator();
                            while(itRegistro.hasNext())
                            {
                                Object o = itRegistro.next();
                                ArrayList lst=null;
                                if (((DatosRegistroExpedientes)o).getLstFincas()!=null
                                        && ((DatosRegistroExpedientes)o).getLstFincas().size()!=0)
                                    lst =((DatosRegistroExpedientes)o).getLstFincas();
                                else if (((DatosRegistroExpedientes)o).getLstBienes()!=null
                                        && ((DatosRegistroExpedientes)o).getLstBienes().size()!=0)
                                    lst =((DatosRegistroExpedientes)o).getLstBienes();
                                
                                
                                if (ConstantesRegExp.clienteCatastro!=null)
                                {                                  
                                    fich.setContenido(fileToString(new File(rutaImp)));
                                    ArrayList lstExp = new ArrayList();
                                    lstExp.add(((DatosRegistroExpedientes)o).getExpediente().getNumeroExpediente());
                                    //A crearFichero se le pasa la lista de expedientes
                                    ConstantesRegExp.clienteCatastro.crearFichero(
                                            lstExp, fich);
                                }
                               
                            }
                            
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
            hb.enableHelpKey(this,"FinRetorno", hs);
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
    }
    
    private void printFinalMessage()
    { 
        
        sbMessage.append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.fecha.finalizacion"), ImportarUtils.getDate()))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.unidadesdatos.procesados"), totalRows))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.unidadesdatos.insertados"), insertedRows))
        .append(ImportarUtils.getStringBufferHtmlFormattedText(I18N.get("Importacion","importar.general.unidadesdatos.no.insertados"), notInsertedRows))
        .append(ImportarUtils.getEnhancedInformationMessage(I18N.get("Importacion", "importar.asistente.fin.mensaje.expedientesactivos")));
        
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
        // TODO Auto-generated method stub
        
    }    
    
    private String fileToString(File file)
    {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
           
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
