/**
 * ImportarInfoIntercambio.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.w3c.dom.Document;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.dialogs.InformationImportSelectPanel;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarResponsePruebas;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils_LCGIII;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.server.catastro.intercambio.importacion.utils.ImportarUtilsServerCatastro;
import com.geopista.server.catastro.servicioWebCatastro.UtilXMLRegistroExp;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaXMLSigImpl;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ImportarInfoIntercambio extends JPanel implements WizardPanel
{
    private boolean permiso = true;
    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    private WizardContext wizardContext; 
    private String nextID = "2";
    
    private JFileChooser fc = new JFileChooser();
    private JTextField jTextFieldCertDigital = null;
    private JTextField jTextFieldPassword = null;
    
    private static final String TIPO_FICHERO = "consultacatastrorequest";
    
    //  Variables utilizadas para las validaciones
    
    
    public ImportarInfoIntercambio(String title)
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
        
        progressDialog.setTitle(I18N.get("Importacion","importar.intercambio.CargandoDatosIniciales"));
        progressDialog.report(I18N.get("Importacion","importar.intercambio.CargandoDatosIniciales"));
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
                            InformationImportSelectPanel panel = 
                                new InformationImportSelectPanel(title);
                            add(panel,  
                                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
                            
                            jTextFieldCertDigital = panel.getJTextFieldCertDigital();
                            jTextFieldPassword = panel.getJPasswordFieldPassword();
                           
                            panel.getLabelImagen().setIcon(IconLoader.icon(MainCatastro.SMALL_PICTURE_LOCATION));
                            panel.getJButtonCertDigital().addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {
                                    try
                                    {
                                        btnOpen_actionPerformed(e);
                                    }
                                    catch(Exception ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }
                                    }); 
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
        
        try
        {
            // Iniciamos la ayuda
        	String helpHS = "help/catastro/importadores/importadoresHelp_es.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this,"InformacionTitularidad", hs);
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        blackboard.put(ImportarUtils_LCGIII.FILE_TO_IMPORT, jTextFieldCertDigital.getText());
        
        blackboard.put("ConexionOVC",false);
		blackboard.put("Validacion",false);
        //Si hay algun fallo en la importación se pondrá a false
		blackboard.put("Importacion",true);
		
        //Línea de prueba. BORRAR
        ImportarResponsePruebas.setExpediente(blackboard);
        setNextID("2");
        
        try{
        	
            File keystoreFile = new File(jTextFieldCertDigital.getText());            
            FileInputStream is = new FileInputStream(keystoreFile);    
                        
            KeyStore keystore = KeyStore.getInstance("PKCS12");            
            String password = jTextFieldPassword.getText();     
            //char[] clave = password.toCharArray();            
            //is = new FileInputStream(keystoreFile);
            keystore.load(is, password.toCharArray());
            
            // Obtenemos el Alias del Certificado.
            Enumeration keystoreAliases = keystore.aliases();
            ArrayList lstAlias = new ArrayList();
            String alias = new String();
            while (keystoreAliases.hasMoreElements())
            {
                alias = (String) keystoreAliases.nextElement();
                lstAlias.add(alias);
            }
            
            //TODO: Cadena SOAP con la peticion
            //String sCadena = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soap:Header></soap:Header><soap:Body Id=\"MsgBody\">";
            final String truststorePath = AppContext.getApplicationContext().getPath(
                    UserPreferenceConstants.TRUST_CERT_STORE_PATH, null);
            
            final String keystorePass = password;
            final String privateKeyAlias = alias;
            final KeyStore ks = keystore;
            final File keyStoreFile = keystoreFile;
            //final String finalXml = sCadena;
                                    
            Expediente expediente = (Expediente) blackboard.get("expediente");
            //ArrayList lstFincas = (ArrayList) blackboard.get("lstFincas");
            ArrayList lstFincas = expediente.getListaReferencias();
            
            final String fileXML = ImportarUtilsServerCatastro.crearXMLConsultaCatastro(expediente, lstFincas);
                        
            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application
                    .getMainFrame(), null);
            
            //ImportarUtils.validateXMLwithXSD("D:\\Trabajos\\Geocatastro\\request.xml", "ConsultaCatastroRequest");
            //ImportarUtils.validateSAXdocumentWithXSD(fileXML, TIPO_FICHERO);
            
            progressDialog.allowCancellationRequests();
            progressDialog.setTitle(I18N.get("Importacion","importar.intercambio.PeticionDatosCatastro"));
            progressDialog.report(I18N.get("Importacion","importar.intercambio.PeticionDatosCatastro"));
            progressDialog.addComponentListener(new ComponentAdapter()
                {
                    public void componentShown(ComponentEvent e)
                    {                       
                        new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    try
                                    {
                                        // Cargar la petición SOAP
                                        Document doc = UtilXMLRegistroExp
                                                .createDOMMessage(new StringBufferInputStream(
                                                        fileXML));
                                        // Firmamos el documento
                                        doc = UtilXMLRegistroExp.signDoc(privateKeyAlias,
                                                ks, keystorePass, doc);
                                        
                                        progressDialog.report(I18N.get("Importacion","importar.intercambio.AlmacenarDatosCatastro"));
                                        
                                        String service = AppContext.getApplicationContext().getString("ovc.service");
                                    	String action = AppContext.getApplicationContext().getString("ovc.soap.action");
                                    	String host = AppContext.getApplicationContext().getString("ovc.host");
                                    	String post = AppContext.getApplicationContext().getString("ovc.post");
                                                                            	
                                        InputStream responseString = GeopistaXMLSigImpl
                                                .sendRequest(doc, keyStoreFile
                                                        .getAbsolutePath(), keystorePass,
                                                        truststorePath, null,service, action, host, post);
                                        //progressDialog.setVisible(false);
                                        blackboard.put("ConexionOVC", true);
                                        
                                        progressDialog.report(I18N.get("Importacion","importar.intercambio.RecibirDatosCatastro"));
                                        
                                        ImportarUtilsServerCatastro impUtils = new ImportarUtilsServerCatastro();
                                        impUtils.cargarResponseXML(responseString, progressDialog);
                                        
                                    } catch (Exception e)
                                    {
                                        ErrorDialog
                                                .show(
                                                		application.getMainFrame(),
                                                		I18N.get("Importacion","importar.intercambio.ProblemaConOVC"),
                                                		I18N.get("Importacion","importar.intercambio.ProblemaConOVC"),
                                                        StringUtil.stackTrace(e));
                                    } finally
                                    {
                                        progressDialog.setVisible(false);
                                    }
                                }
								
                            }).start();
                    }
                });
            if (application.getMainFrame() == null) // sin ventana de referencia
                GUIUtil.centreOnScreen(progressDialog);
            else
                GUIUtil.centreOnWindow(progressDialog);
            progressDialog.setVisible(true);
                        
        }
        catch(CertificateException ex)
        {
            //ex.printStackTrace();
        	ErrorDialog.show(this, "ERROR", "ERROR CertificateException", StringUtil.stackTrace(ex));
            jTextFieldPassword.setText(""); 
            setNextID("1");
        }       
        catch(NoSuchAlgorithmException ex)
        {
            //ex.printStackTrace();
            ErrorDialog.show(this, "ERROR", "ERROR NoSuchAlgorithmException", StringUtil.stackTrace(ex));
            jTextFieldPassword.setText("");  
            setNextID("1");
        }
        catch(IOException ex)
        {
            //ex.printStackTrace();
        	//logger.error("Error ", ex);
            ErrorDialog.show(this, "ERROR", "ERROR IOException", StringUtil.stackTrace(ex));
            jTextFieldPassword.setText("");    
            setNextID("1");
        }
                
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
        if (!permiso)
        {
            JOptionPane.showMessageDialog(application.getMainFrame(), application
                    .getI18nString("NoPermisos"));
            return false;
        } 
        else
        {
            if (jTextFieldCertDigital.getText().length()!= 0)                
                return true;                 
            else                
                return false;   
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
    
    private void btnOpen_actionPerformed(ActionEvent e) throws KeyStoreException, FileNotFoundException
    {   
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension("pfx");
        filter.setDescription(I18N.get("Importacion","importar.general.tiposfichero.pkcs12"));
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false); // QUITA LA OPCION ALL FILES(*.*)
        
        File currentDirectory = (File) blackboard.get(ImportarUtils_LCGIII.LAST_IMPORT_DIRECTORY);
        
        fc.setCurrentDirectory(currentDirectory);
        
        int returnVal = fc.showOpenDialog(this);
        
        blackboard.put(ImportarUtils_LCGIII.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory());
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            String fichero = fc.getSelectedFile().getPath();
            jTextFieldCertDigital.setText(fichero);
        }    
        wizardContext.inputChanged();
    }
    
    private void cmbTipoFichero_actionPerformed(ActionEvent e)
    {
        //txtFile.setText("");
        jTextFieldCertDigital.setText("");
    }
    
    
    public void exiting()
    {   
    }
}  
