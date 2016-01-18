package com.ws.cliente;

/**
* <p>Fichero: TestClient.java</p>
* <p>Descripción: </p>
* <p>Empresa: Telvent Interactiva </p>
* <p>Fecha creación: 26-jul-2006</p>
* @author SERYS
* @version 1.0
*/
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axis.Constants;
import org.apache.axis.client.Service;
import org.apache.axis.message.MessageElement;
import org.apache.axis.utils.Mapping;
import org.apache.log4j.Logger;
import org.apache.xml.security.signature.XMLSignature;
import org.tempuri.OVCServWeb.OVCExpediente2.OVCExpedienteSoapStub;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.model.beans.Expediente;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.servicioWebCatastro.beans.DatosErrorWS;
import com.geopista.app.catastro.servicioWebCatastro.beans.DatosWSResponseBean;
import com.geopista.app.catastro.servicioWebCatastro.utils.ParsearDatosWS;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaXMLSigImpl;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

import es.meh.catastro.www.ActualizaCatastro_In;
import es.meh.catastro.www.ActualizaCatastro_Out;
import es.meh.catastro.www.ConsultaCatastro_In;
import es.meh.catastro.www.ConsultaCatastro_Out;
import es.meh.catastro.www.ConsultaExpediente_In;
import es.meh.catastro.www.ConsultaExpediente_Out;
import es.meh.catastro.www.Expedientes_In;
import es.meh.catastro.www.Expedientes_Out;

public class TestClient{
	
	private static final Logger logger= Logger.getLogger(TestClient.class);

	private static java.net.URL endpointURL;
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	private static String keystorePass = null;
	private static File keyStoreFile = null;
	
	private static String fichTrustStore = null;
	private static String passTrustStore = null;



	public TestClient(HashMap certificadoHashMap)
	{

		try {
			
			establecerCertificado(certificadoHashMap);
			
		}
		catch (Exception e)
		{
			System.err.println("Error cargando el fichero de properties securityConfiguration.properties");
			System.exit(-1);
		}
	
	}
	
	
	private void establecerCertificado(HashMap certificadoHashMap ) throws KeyStoreException, FileNotFoundException, MalformedURLException{

		this.keystorePass = (String) certificadoHashMap.get(ConstantesRegistroExp.CERTIFICADO_PASSWORD);
        this.keyStoreFile = (File) certificadoHashMap.get(ConstantesRegistroExp.CERTIFICADO_KEYSTOREFILE);

	}
	
	private void establecerUrl() throws MalformedURLException{
		endpointURL = new URL("https://ovc2.catastro.meh.es/ovcservwebpre/OVCSWExpedientes/OVCExpediente.asmx");
		//endpointURL = new URL("https://ovc2.catastro.meh.es/ovcservweb/OVCSWExpedientes/OVCExpediente.asmx");
	}
	
	public DatosWSResponseBean asociacionExpediente(final String XML,  ApplicationContext aplicacion, final Expediente exp) throws Exception {
		
		final ArrayList lst = new ArrayList();
		
		try {	

            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                    .getMainFrame(), null);
            progressDialog.allowCancellationRequests();
            progressDialog.setTitle(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
            progressDialog.report(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
            progressDialog.addComponentListener(new ComponentAdapter()
                {
					public void componentShown(ComponentEvent e)
                    {
                    	
                    	 Document doc = null;
						try {
							establecerUrl();
							GeopistaXMLSigImpl.establecerKeyStore(
										keystorePass, keystorePass, keyStoreFile, "PKCS12");
							
							ByteArrayInputStream bais = new ByteArrayInputStream(XML.toString().getBytes("UTF-8")); 
							
							doc = GeopistaXMLSigImpl.createDOMMessage(bais);
							Node node = GeopistaXMLSigImpl.signDocument(doc);
							
							MessageElement destElement = new MessageElement();
							copyNode(destElement, (Element)node.getFirstChild().getOwnerDocument().getDocumentElement());
					
							System.out.println("\n\n*******************************");	
							System.out.println(destElement.getAsString());	
							System.out.println("\n\n*******************************");	
							
							MessageElement[] messageElement = new MessageElement[1];
							messageElement[0] = new MessageElement(destElement);
							
							Expedientes_In xmlPeticion = new Expedientes_In();
							xmlPeticion.set_any(messageElement);
							
			     			OVCExpedienteSoapStub customer = null;
			     			customer = new OVCExpedienteSoapStub(endpointURL, new Service());
			     			Expedientes_Out expedientes_out = customer.generaExpediente(xmlPeticion);
			     			System.out.println(expedientes_out.get_any()[0].getAsString());	     			
			     			Element elementOut = expedientes_out.get_any()[0].getAsDOM();
			    			Document XMLDoc_out = elementOut.getOwnerDocument();
			    			
			    			ParsearDatosWS parsearDatosWS = new ParsearDatosWS();
			    			DatosWSResponseBean datosWSResponse = null;
			    			datosWSResponse = 	parsearDatosWS.parsearDatosWSResponse(XMLDoc_out, exp);
			    			
			    			lst.add(datosWSResponse );
			    			
			    			
						} catch (Exception e1) {

							e1.printStackTrace();
							System.err.println(e1.toString());
							logger.error(e1.getMessage());
							if(e1 instanceof org.apache.axis.AxisFault){
								org.apache.axis.AxisFault axisFault = (org.apache.axis.AxisFault)e1;
								Element [] elements = axisFault.getFaultDetails();
								
								DatosErrorWS datosError = ParsearDatosWS.ParsearDatosWSError(elements);
								
								JOptionPane.showMessageDialog(((AppContext)AppContext.getApplicationContext()).getMainFrame(),
										I18N.get("RegistroExpedientes","Catastro.webservices.catastro.excepcion")+"\n"+ 
										I18N.get("RegistroExpedientes","Catastro.webservices.catastro.codigo") +" : "+datosError.getCodigoEstado()+ "\n"+
										I18N.get("RegistroExpedientes","Catastro.webservices.catastro.mensaje") +" : "+datosError.getMensaje()
										, "Error",	JOptionPane.ERROR_MESSAGE);
							}
							
						} 
						finally{
							progressDialog.setVisible(false);
						}
                    }
                });
            if (aplicacion.getMainFrame() == null) // sin ventana de referencia
                GUIUtil.centreOnScreen(progressDialog);
            else
                GUIUtil.centreOnWindow(progressDialog);
            progressDialog.setVisible(true);
			
		} catch (Exception excepcion) {
			throw excepcion;
		}		

		DatosWSResponseBean response = null;
		if(lst != null && !lst.isEmpty()){
			response = (DatosWSResponseBean)lst.get(0);
		}

		return response;
	}
	
	
	public DatosWSResponseBean consultaCatastro(final String XML, ApplicationContext aplicacion, final Expediente exp) throws Exception{
		
		final ArrayList lst = new ArrayList();

		try {	

            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                    .getMainFrame(), null);
            progressDialog.allowCancellationRequests();
            progressDialog.setTitle(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
            progressDialog.report(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
            progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
         	
                	Document doc = null;
					try {
						
						establecerUrl();
						GeopistaXMLSigImpl.establecerKeyStore(
									keystorePass, keystorePass, keyStoreFile, "PKCS12");
						ByteArrayInputStream bais = new ByteArrayInputStream(XML.toString().getBytes("UTF-8")); 
						
						doc = GeopistaXMLSigImpl.createDOMMessage(bais);
						Node node = GeopistaXMLSigImpl.signDocument(doc);
					
						MessageElement destElement = new MessageElement();
						copyNode(destElement, (Element)node.getFirstChild().getOwnerDocument().getDocumentElement());
						
						System.out.println("\n\n*******************************");	
						System.out.println(destElement.getAsString());	
						System.out.println("\n\n*******************************");	
						
						MessageElement[] messageElement = new MessageElement[1];
						messageElement[0] = new MessageElement(destElement);
						
						ConsultaCatastro_In xmlPeticion = new ConsultaCatastro_In();
						xmlPeticion.set_any(messageElement);
						
		     			OVCExpedienteSoapStub customer = null;
		     			customer = new OVCExpedienteSoapStub(endpointURL, new Service());
		     			ConsultaCatastro_Out consultaCatastro_out = customer.consultaCatastro(xmlPeticion);
		     			System.out.println(consultaCatastro_out.get_any()[0].getAsString());	    
		     			Element elementOut = consultaCatastro_out.get_any()[0].getAsDOM();
		    			Document XMLDoc_out = elementOut.getOwnerDocument();
		    			
		    			GeopistaXMLSigImpl.dumpDocument(XMLDoc_out.getFirstChild());
		    		
		    			ParsearDatosWS parsearDatosWS = new ParsearDatosWS();
		    			DatosWSResponseBean datosWSResponse = null;
		    			datosWSResponse = 	parsearDatosWS.parsearDatosWSResponse(XMLDoc_out, exp);				
		    			
		    			lst.add(datosWSResponse );
		    			
					} catch (Exception e1) {

						e1.printStackTrace();
						System.err.println(e1.toString());
						logger.error(e1.getMessage());
						if(e1 instanceof org.apache.axis.AxisFault){
							org.apache.axis.AxisFault axisFault = (org.apache.axis.AxisFault)e1;
							Element [] elements = axisFault.getFaultDetails();
							
							DatosErrorWS datosError = ParsearDatosWS.ParsearDatosWSError(elements);
							
							JOptionPane.showMessageDialog(((AppContext)AppContext.getApplicationContext()).getMainFrame(),
									I18N.get("RegistroExpedientes","Catastro.webservices.catastro.excepcion")+"\n"+ 
									I18N.get("RegistroExpedientes","Catastro.webservices.catastro.codigo") +" : "+datosError.getCodigoEstado()+ "\n"+
									I18N.get("RegistroExpedientes","Catastro.webservices.catastro.mensaje") +" : "+datosError.getMensaje()
									, "Error",	JOptionPane.ERROR_MESSAGE);
						}
						
					} 
					finally{
						progressDialog.setVisible(false);
					}

                }
            });
	        if (aplicacion.getMainFrame() == null) // sin ventana de referencia
	            GUIUtil.centreOnScreen(progressDialog);
	        else
	            GUIUtil.centreOnWindow(progressDialog);
	        progressDialog.setVisible(true);
  
		} catch (Exception excepcion) {
			throw excepcion;
		}		

		DatosWSResponseBean response = null;
		if(lst != null && !lst.isEmpty()){
			response = (DatosWSResponseBean)lst.get(0);
		}

		return response;
	}
	
	public DatosWSResponseBean actualizacionCatastro(final String XML,  ApplicationContext aplicacion, final Expediente exp) throws Exception {

		final ArrayList lst = new ArrayList();
			
		try {	

            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                    .getMainFrame(), null);
            progressDialog.allowCancellationRequests();
            progressDialog.setTitle(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
            progressDialog.report(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
            progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                	
                	 Document doc = null;
						try {
							
							establecerUrl();
							GeopistaXMLSigImpl.establecerKeyStore(
										keystorePass, keystorePass, keyStoreFile, "PKCS12");
							ByteArrayInputStream bais = new ByteArrayInputStream(XML.toString().getBytes("UTF-8")); 
							
							doc = GeopistaXMLSigImpl.createDOMMessage(bais);
							Node node = GeopistaXMLSigImpl.signDocument(doc);
							
							MessageElement destElement = new MessageElement();
							copyNode(destElement, (Element)node.getFirstChild().getOwnerDocument().getDocumentElement());
							
							System.out.println("\n\n*******************************");	
							System.out.println(destElement.getAsString());	
							System.out.println("\n\n*******************************");	
							
							MessageElement[] messageElement = new MessageElement[1];
							messageElement[0] = new MessageElement(destElement);
							
							ActualizaCatastro_In xmlPeticion = new ActualizaCatastro_In();
							xmlPeticion.set_any(messageElement);
							//System.out.println(xmlPeticion.get_any()[0].getAsString());
			     			OVCExpedienteSoapStub customer = null;
			     			customer = new OVCExpedienteSoapStub(endpointURL, new Service());
			     			ActualizaCatastro_Out actualizaCatastro_out = customer.actualizaCatastro(xmlPeticion);
			     			System.out.println(actualizaCatastro_out.get_any()[0].getAsString());
			     			Element elementOut = actualizaCatastro_out.get_any()[0].getAsDOM();
			    			Document XMLDoc_out = elementOut.getOwnerDocument();
			    			
			    			ParsearDatosWS parsearDatosWS = new ParsearDatosWS();
			    			DatosWSResponseBean datosWSResponse = null;
			    			datosWSResponse = 	parsearDatosWS.parsearDatosWSResponse(XMLDoc_out, exp);

			    			lst.add(datosWSResponse );
			    			
						} catch (Exception e1) {

							e1.printStackTrace();
							System.err.println(e1.toString());
							logger.error(e1.getMessage());
							if(e1 instanceof org.apache.axis.AxisFault){
								org.apache.axis.AxisFault axisFault = (org.apache.axis.AxisFault)e1;
								Element [] elements = axisFault.getFaultDetails();
								
								DatosErrorWS datosError = ParsearDatosWS.ParsearDatosWSError(elements);
								
								JOptionPane.showMessageDialog(((AppContext)AppContext.getApplicationContext()).getMainFrame(),
										I18N.get("RegistroExpedientes","Catastro.webservices.catastro.excepcion")+"\n"+ 
										I18N.get("RegistroExpedientes","Catastro.webservices.catastro.codigo") +" : "+datosError.getCodigoEstado()+ "\n"+
										I18N.get("RegistroExpedientes","Catastro.webservices.catastro.mensaje") +" : "+datosError.getMensaje()
										, "Error",	JOptionPane.ERROR_MESSAGE);
							}
							
						} 
						finally{
							progressDialog.setVisible(false);
						}

                }
            });
            if (aplicacion.getMainFrame() == null) // sin ventana de referencia
                GUIUtil.centreOnScreen(progressDialog);
            else
                GUIUtil.centreOnWindow(progressDialog);
            progressDialog.setVisible(true);
		

		} catch (Exception excepcion) {
			
			throw excepcion;

		}
		DatosWSResponseBean response = null;
		if(lst != null && !lst.isEmpty()){
			response = (DatosWSResponseBean)lst.get(0);
		}

		return response;
	}
	
	public DatosWSResponseBean consultaEstadoExpediente(final String XML,  ApplicationContext aplicacion, final Expediente exp) throws Exception{
		
		final ArrayList lst = new ArrayList();		
		
		try {	

            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                    .getMainFrame(), null);
            progressDialog.allowCancellationRequests();
            progressDialog.setTitle(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
            progressDialog.report(aplicacion.getI18nString("GeopistaConsultaInformacionParcelaOVC.PeticionDatosCatastro"));
            progressDialog.addComponentListener(new ComponentAdapter()
            {
                public void componentShown(ComponentEvent e)
                {
                	
                	 Document doc = null;
						try {
							establecerUrl();
							GeopistaXMLSigImpl.establecerKeyStore(
										keystorePass, keystorePass, keyStoreFile, "PKCS12");
							ByteArrayInputStream bais = new ByteArrayInputStream(XML.toString().getBytes("UTF-8")); 
							
							doc = GeopistaXMLSigImpl.createDOMMessage(bais);
							Node node = GeopistaXMLSigImpl.signDocument(doc);
							
							MessageElement destElement = new MessageElement();
							copyNode(destElement, (Element)node.getFirstChild().getOwnerDocument().getDocumentElement());
							
							System.out.println("\n\n*******************************");	
							System.out.println(destElement.getAsString());	
							System.out.println("\n\n*******************************");	
							
							MessageElement[] messageElement = new MessageElement[1];
							messageElement[0] = new MessageElement(destElement);
							
							ConsultaExpediente_In xmlPeticion = new ConsultaExpediente_In();
							xmlPeticion.set_any(messageElement);

			     			OVCExpedienteSoapStub customer = null;
			     			customer = new OVCExpedienteSoapStub(endpointURL, new Service());
			     			ConsultaExpediente_Out consultaExpediente_out = customer.consultaExpediente(xmlPeticion);
			     			
			     			System.out.println(consultaExpediente_out.get_any()[0].getAsString());	    
			     			Element elementOut = consultaExpediente_out.get_any()[0].getAsDOM();
			    			Document XMLDoc_out = elementOut.getOwnerDocument();
			    			
			    			ParsearDatosWS parsearDatosWS = new ParsearDatosWS();
			    			DatosWSResponseBean datosWSResponse = null;
			    			datosWSResponse = 	parsearDatosWS.parsearDatosWSResponse(XMLDoc_out, exp);
			    			
			    			lst.add(datosWSResponse );
			    			
						} catch (Exception e1) {

							e1.printStackTrace();
							System.err.println(e1.toString());
							logger.error(e1.getMessage());
							
							
							if(e1 instanceof org.apache.axis.AxisFault){
								org.apache.axis.AxisFault axisFault = (org.apache.axis.AxisFault)e1;
								Element [] elements = axisFault.getFaultDetails();
								
								DatosErrorWS datosError = ParsearDatosWS.ParsearDatosWSError(elements);
								
								JOptionPane.showMessageDialog(((AppContext)AppContext.getApplicationContext()).getMainFrame(),
										I18N.get("RegistroExpedientes","Catastro.webservices.catastro.excepcion")+"\n"+ 
										I18N.get("RegistroExpedientes","Catastro.webservices.catastro.codigo") +" : "+datosError.getCodigoEstado()+ "\n"+
										I18N.get("RegistroExpedientes","Catastro.webservices.catastro.mensaje") +" : "+datosError.getMensaje()
										, "Error",	JOptionPane.ERROR_MESSAGE);
							}
							
						} 
						finally{
							progressDialog.setVisible(false);
						}

                }
            });
            if (aplicacion.getMainFrame() == null) // sin ventana de referencia
                GUIUtil.centreOnScreen(progressDialog);
            else
                GUIUtil.centreOnWindow(progressDialog);
            progressDialog.setVisible(true);
	
		} catch (Exception e) {
			throw e;
		} 

		DatosWSResponseBean response = null;
		if(lst != null && !lst.isEmpty()){
			response = (DatosWSResponseBean)lst.get(0);
		}

		return response;
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
	 public static Map pedirCertificado() throws KeyStoreException, FileNotFoundException
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
	        //KeyStore keystore = KeyStore.getInstance("JKS");
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
	 
	 	public  String getFichTrustStore() {
			return fichTrustStore;
		}

		public  void setFichTrustStore(String fichTrustStore) {
			this.fichTrustStore = fichTrustStore;
		}

		public  String getPassTrustStore() {
			return passTrustStore;
		}

		public  void setPassTrustStore(String passTrustStore) {
			this.passTrustStore = passTrustStore;
		}
		
	
		public static void validate(Document doc) throws Exception {  
	        org.apache.xml.security.Init.init();  
	  
	        String signatureFileName = "signature.xml";  
	  
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
	  
	        dbf.setNamespaceAware(true);  
	        dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);  
	          
	       // File            f   = new File(signatureFileName);  
	        DocumentBuilder db  = dbf.newDocumentBuilder();  
	       // Document     doc    = db.parse(new java.io.FileInputStream(f));  
	        Element      sigElement = (Element) doc.getElementsByTagName("Signature").item(0);  
	        XMLSignature signature  = new XMLSignature(sigElement, "");  
	  
	        org.apache.xml.security.keys.KeyInfo keyInfo = signature.getKeyInfo();  
	        if (keyInfo != null) {  
	            X509Certificate cert = keyInfo.getX509Certificate();  
	            if (cert != null) {  
	                // Validamos la firma usando un certificado X509  
	                if (signature.checkSignatureValue(cert)){  
	                    System.out.println("Válido según el certificado");    
	                } else {  
	                    System.out.println("Inválido según el certificado");      
	                }  
	            } else {  
	                // No encontramos un Certificado intentamos validar por la cláve pública  
	                PublicKey pk = keyInfo.getPublicKey();  
	                if (pk != null) {  
	                    // Validamos usando la clave pública  
	                    if (signature.checkSignatureValue(pk)){  
	                        System.out.println("Válido según la clave pública");      
	                    } else {  
	                        System.out.println("Inválido según la clave pública");    
	                    }  
	                } else {  
	                    System.out.println("No podemos validar, tampoco hay clave pública");  
	                }  
	            }  
	        } else {  
	            System.out.println("No ha sido posible encontrar el KeyInfo");  
	        }  
	    }  

		
		// debido a un problema con los namespaces cuando se hace la copia del objeto 	MessageElement, cuando 
		//se declara un objeto de tipo MessageElement a partir de un objeto Element
		// se cambia el metodo copyNode de la clase MessageElement
		private void copyNode(MessageElement dest, org.w3c.dom.Node source)
		   {
			
			  dest.setPrefix(source.getPrefix());
		       if(source.getLocalName() != null) {
		           dest.setQName(new QName(source.getNamespaceURI(), source.getLocalName()));
		       }
		       else
		       {
		           dest.setQName(new QName(source.getNamespaceURI(), source.getNodeName()));
		       }

		       NamedNodeMap attrs = source.getAttributes();
		       for(int i = 0; i < attrs.getLength(); i++){
		           Node att = attrs.item(i);
		       if (att.getNamespaceURI() != null &&
		               att.getPrefix() != null &&
		               att.getNamespaceURI().equals(Constants.NS_URI_XMLNS) &&
		               "xmlns".equals(att.getPrefix())) {
			    	   if(!att.getLocalName().equals("xmlns")){
			               Mapping map = new Mapping(att.getNodeValue(), att.getLocalName());
			               dest.addMapping(map);
			    	   }
		           }
			       if(att.getLocalName() != null) {

			    	   if(!att.getLocalName().equals("xmlns")){
			    		   dest.addAttribute(att.getPrefix(),
		                       (att.getNamespaceURI() != null ? att.getNamespaceURI() : ""),
		                       att.getLocalName(),
		                       att.getNodeValue());
			    	   }
		           } else if (att.getNodeName() != null) {
		               dest.addAttribute(att.getPrefix(),
		                       (att.getNamespaceURI() != null ? att.getNamespaceURI() : ""),
		                       att.getNodeName(),
		                       att.getNodeValue());
		           }
		       }

		       NodeList children = source.getChildNodes();
		       for(int i = 0; i < children.getLength(); i++){
		           Node child = children.item(i);
		           if(child.getNodeType()==Node.TEXT_NODE ||
		              child.getNodeType()==Node.CDATA_SECTION_NODE ||
		              child.getNodeType()==Node.COMMENT_NODE ) {
		               org.apache.axis.message.Text childElement = 
		                   new org.apache.axis.message.Text((CharacterData)child);
		               dest.appendChild(childElement);
		           } else {
		               MessageElement childElement = new MessageElement();
		               dest.appendChild(childElement);
		               copyNode(childElement, child);
		           }
		       }
		   }
		
		

}
