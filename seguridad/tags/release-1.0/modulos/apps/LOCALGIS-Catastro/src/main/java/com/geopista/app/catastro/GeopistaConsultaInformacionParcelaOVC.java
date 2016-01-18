/*
 * Created on 08-ago-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.app.catastro;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.XMLUtils;
import org.jdom.JDOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.model.GeopistaMap;
import com.geopista.util.GeopistaUtil;
import com.geopista.util.GeopistaXMLSigImpl;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.plugin.PersistentBlackboardPlugIn;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * Comento el método crearXMLConsultaCatastro por estar desactualizado
 */
/**
 * @author dbaeza
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GeopistaConsultaInformacionParcelaOVC

{
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private Blackboard blackboard = aplicacion.getBlackboard();

    private static final String CLAVESMAP = "Claves Map";
    
    private static final String FILE_CHOOSER_DIRECTORY_OVC = "File Chooser Directory OVC";

    /**
     * Metodo para abrir la conexion
     */
    public Connection abrirConexion() throws SQLException
    {
        Connection conn = null;
        try
        {
            AppContext app = (AppContext) AppContext.getApplicationContext();
            conn = app.getConnection();
            conn.setAutoCommit(true);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return conn;

    }

    /**
     * Consulta la información de la parcela seleccionada en la base de datos
     * 
     * @param refCat
     * @return datos con los datos de consulta para generar el XML
     * @throws SQLException
     */
    public GeopistaPeticionDatosConsultaCatastro obtenerInformacionOVC(
            GeopistaFeature featureRefCat)
    {
        GeopistaPeticionDatosConsultaCatastro datos = new GeopistaPeticionDatosConsultaCatastro();

        GeopistaSchema nombreEsquema = (GeopistaSchema) featureRefCat.getSchema();
//        String tipo = (String) featureRefCat.getAttribute(nombreEsquema
//                .getAttributeByColumn("tipo"));
        int idMunicipio = Integer.parseInt(AppContext.getApplicationContext().getString(
                "geopista.DefaultCityId"));
//        datos.setTipo(tipo);
//        if (tipo.equals("U"))
//        {
//            String numeroCargo = (String) featureRefCat.getAttribute(nombreEsquema
//                    .getAttributeByColumn("numeroCargo"));
//            String primerCaracterControl = (String) featureRefCat
//                    .getAttribute(nombreEsquema
//                            .getAttributeByColumn("primerCaracterControl"));
//            String segundoCaracterControl = (String) featureRefCat
//                    .getAttribute(nombreEsquema
//                            .getAttributeByColumn("SegundoCaracterControl"));
//            String referenciaCatastral = (String) featureRefCat
//                    .getAttribute(nombreEsquema
//                            .getAttributeByColumn("referencia_catastral"));
//            // Datos de urbana
//            
//            if(numeroCargo.length()<4)
//            {
//                int faltan = 4-numeroCargo.length();
//                for(int contador=0; contador<faltan;contador++)
//                {
//                    numeroCargo="0" + numeroCargo;
//                }
//            }
//            
//            datos.setCargo(numeroCargo);
//            datos.setCc1(primerCaracterControl);
//            datos.setCc2(segundoCaracterControl);
//            datos.setParcelaCatastral1(referenciaCatastral.substring(0, 7));
//            datos.setParcelaCatastral2(referenciaCatastral.substring(7, 14));
//            datos.setCodigoParcela("");
//            datos.setCodigoPoligono("");
//            datos.setIneMunicipio("");
//            datos.setIneProvincia("");
//            AppContext.getApplicationContext().getString("geopista.DefaultCityId");
//            String s = String.valueOf(idMunicipio % 100);
//            datos.setIneMunicipio(s);
//
//            s = String.valueOf(idMunicipio / 1000);
//            datos.setIneProvincia(s);
//
//        } else
//        {
            String codigoParcela = (String) featureRefCat.getAttribute(nombreEsquema
                    .getAttributeByColumn("CodigoParcela"));
            String codigoPoligono = (String) featureRefCat.getAttribute(nombreEsquema
                    .getAttributeByColumn("codigoPoligono"));
            datos.setCargo("");
            datos.setCc1("");
            datos.setCc2("");
            datos.setParcelaCatastral1("");
            datos.setParcelaCatastral2("");
            datos.setCodigoParcela(codigoParcela);
            datos.setCodigoPoligono(codigoPoligono);

            String s = String.valueOf(idMunicipio % 100);
            datos.setIneMunicipio(s);

            s = String.valueOf(idMunicipio / 1000);
            datos.setIneProvincia(s);
//        }

        return datos;
    }

    /**
     * 
     * Pide al usuario el fichero que contiene el certificado y la clave privada
     * en formcato PKCS12 y pide la contraseña para extraes la clave y el alias
     * del certificado
     * 
     * Devuelve el último alias del almacen de certificados aunque en una
     * almacén PKCS12 lo común es que haya solo una clave.
     * 
     * @return Map con los valores en las claves "password" "keystore" y "alias"
     *         y "keystorefile"
     * @throws KeyStoreException
     * @throws FileNotFoundException
     */
    public Map pedirCertificado() throws KeyStoreException, FileNotFoundException
    {
        Hashtable valores = new Hashtable();

        // Lanzar con el filtro de los certificados.
        JFileChooser fc = new JFileChooser();
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        filter.addExtension("pfx");
        filter.setDescription("Certificados PKCS12");
        fc.setFileFilter(filter);
        fc.setFileSelectionMode(0);
        fc.setAcceptAllFileFilterUsed(false);
        int returnVal = fc.showOpenDialog(aplicacion.getMainFrame());
        // Se ha pulsado Aceptar
        
        /*PersistentBlackboardPlugIn.get(context).put(FILE_CHOOSER_DIRECTORY_OVC,
                getFileChooserPanel().getChooser().getCurrentDirectory().toString());*/

        if (returnVal != JFileChooser.APPROVE_OPTION)
        {
            return valores;
        }

        File keystoreFile = new File(fc.getSelectedFile().getPath());
        FileInputStream is = new FileInputStream(keystoreFile);
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        String password = null;
        boolean pedirClaveOtraVez = true;
        while (pedirClaveOtraVez)
        {
            try
            {

                com.geopista.app.catastro.GeopistaPedirPasswordCertificado pedirClave = new com.geopista.app.catastro.GeopistaPedirPasswordCertificado(
                        aplicacion.getMainFrame());
                GUIUtil.centreOnScreen(pedirClave);
                pedirClave.setSize(300, 150);
                pedirClave.requestFocus();

                pedirClave.setVisible(true);

                if (!pedirClave.isOkPressed())
                {
                    return valores;
                }

                pedirClave.setVisible(false);

                char[] clave;
                password = pedirClave.getPassword();
                clave = password.toCharArray();
                
                is = new FileInputStream(keystoreFile);
                keystore.load(is, password.toCharArray());
                
                valores.put("password", password);
                valores.put("keystore", keystore);
                valores.put("keystorefile", keystoreFile);
                pedirClaveOtraVez = false;

            } catch (Exception e)
            {
                continue;
            }
        }

        // Obtenemos el Alias del Certificado.

        Enumeration listaAlias = keystore.aliases();

        while (listaAlias.hasMoreElements())
        {
            valores.put("alias", (String) listaAlias.nextElement());
        }

        return valores;
    }

    /**
     * 
     * @param datos
     *            Estructura con los datos de Entrada de la consulta de OVC
     * @throws FileNotFoundException
     * @throws KeyStoreException
     */
/*    public void crearXMLConsultaCatastro(GeopistaPeticionDatosConsultaCatastro datos) throws KeyStoreException, FileNotFoundException
            
    {

        Map claves = new Hashtable();
        Collection responsestringCollection = new ArrayList();

        String sCadena = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soap:Header></soap:Header><soap:Body Id=\"MsgBody\">";
        sCadena = sCadena + "<Consultar_RC_In xmlns=\"http://www.catastro.meh.es/\">";
        sCadena = sCadena + "<ovc_consulta_datos_in><Atributos>";

        // TimeStamp
        Date fecha = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaFormat = formatter.format(fecha);
        sCadena = sCadena + "<TimeStamp>" + fechaFormat + "</TimeStamp>";
        // Estado
        sCadena = sCadena
                + "<Estado><CodigoEstado></CodigoEstado><LiteralError></LiteralError><TiempoEstimadoRespuesta></TiempoEstimadoRespuesta></Estado></Atributos>";
        // Finalidad
        sCadena = sCadena + "<finalidad>PRUEBAS CATASTRO</finalidad>";
        // Lden
        sCadena = sCadena + "<lden><den><rc_in>";
        // pc1
        sCadena = sCadena + "<pc1>" + datos.getParcelaCatastral1() + "</pc1>";
        // pc2
        sCadena = sCadena + "<pc2>" + datos.getParcelaCatastral2() + "</pc2>";
        // car
        sCadena = sCadena + "<car>" + datos.getCargo() + "</car>";
        // cc1
        sCadena = sCadena + "<cc1>" + datos.getCc1() + "</cc1>";
        // cc2
        sCadena = sCadena + "<cc2>" + datos.getCc2() + "</cc2>";

        // Fin del Mensaje
        sCadena = sCadena
                + "</rc_in></den></lden></ovc_consulta_datos_in></Consultar_RC_In></soap:Body></soap:Envelope>";

        final String truststorePath = AppContext.getApplicationContext().getPath(
                AppContext.TRUST_CERT_STORE_PATH, null);

        claves = (Map) blackboard.get(GeopistaConsultaInformacionParcelaOVC.CLAVESMAP);
        if (claves == null || claves.size()==0)
        {
            claves = pedirCertificado();
            blackboard.put(GeopistaConsultaInformacionParcelaOVC.CLAVESMAP, claves);
        }

        if (claves.size() != 0)
        {
            // Tenemos el password y el alias
            // Cargamos el KeyStore PRIVADO con los valores del certificado.
            final String keystorePass = (String) claves.get("password");
            final String privateKeyAlias = (String) claves.get("alias");
            final KeyStore ks = (KeyStore) claves.get("keystore");
            final File keyStoreFile = (File) claves.get("keystorefile");
            final String finalXml = sCadena;

            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                    .getMainFrame(), null);
            progressDialog.allowCancellationRequests();
            progressDialog.setTitle(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
            progressDialog.report(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
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
                                        // Cargar la petición SOAP
                                        Document doc = GeopistaXMLSigImpl
                                                .createDOMMessage(new StringBufferInputStream(
                                                        finalXml));
                                        // Firmamos el documento
                                        doc = GeopistaXMLSigImpl.signDoc(privateKeyAlias,
                                                ks, keystorePass, doc);
                                        // hacemos la petición a OVC Suponemos
                                        // que el almacen de confianza no tiene
                                        // password??
                                        // Creo que lo mejor es dejar que el
                                        // usuario configure esto a nivel de
                                        // sistema Java
                                        // con las variables de ambiente en el
                                        // panel de control de Java

                                        String responseString = GeopistaXMLSigImpl
                                                .sendRequest(doc, keyStoreFile
                                                        .getAbsolutePath(), keystorePass,
                                                        truststorePath, null);
                                        progressDialog.setVisible(false);

                                        // ////////////////////////////////////////////////////////////////////////
                                        // Aqui usa un constructor que no existe
                                        // en la versión del CVS
                                        // JP //propongo usar el DOM para ello
                                        // pero ignoro que version es la
                                        // correcta.
                                        // Document resp=
                                        // GeopistaXMLSigImpl.createDOMMessage(new
                                        // StringBufferInputStream(responseString));
                                        // GeopistaMostrarDatosCatastroXML clase
                                        // = new
                                        // GeopistaMostrarDatosCatastroXML(
                                        // AppContext.getApplicationContext().getMainFrame());

                                        //                                       
                                        GeopistaMostrarDatosCatastroXML clase = new GeopistaMostrarDatosCatastroXML(
                                                AppContext.getApplicationContext()
                                                        .getMainFrame(), responseString);
                                        GUIUtil.centreOnScreen(clase);

                                        clase.setVisible(true);

                                    } catch (Exception e)
                                    {
                                        ErrorDialog
                                                .show(
                                                        aplicacion.getMainFrame(),
                                                        aplicacion
                                                                .getI18nString("ConsultarOVCPlugIn.ProblemaConOVC"),
                                                        aplicacion
                                                                .getI18nString("ConsultarOVCPlugIn.ProblemaConOVC"),
                                                        StringUtil.stackTrace(e));
                                    } finally
                                    {
                                        progressDialog.setVisible(false);
                                    }
                                }
                            }).start();
                    }
                });
            if (aplicacion.getMainFrame() == null) // sin ventana de referencia
                GUIUtil.centreOnScreen(progressDialog);
            else
                GUIUtil.centreOnWindow(progressDialog);
            progressDialog.setVisible(true);

        }

    }*/

}
