/**
 * GeopistaXMLSigImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 02-nov-2005 by juacas
 *
 * 
 */
package com.geopista.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.geopista.app.AppContext;

/**
 * TODO Documentación
 * 
 * @author juacas
 * 
 */
public class GeopistaXMLSigImpl
{
	  private static String KEYSTORE_PASSWORD ;
	  private static String KEY_PASSWORD;
	  private static String KEY_ALIAS;
	  private static File KEY_STORE_FILE;
	  private static String KEY_STORE_TYPE;
	
	  private static final Log logger = LogFactory.getLog(GeopistaXMLSigImpl.class);
	  
	  public static void establecerKeyStore(String keyStorePass, String  keyPass,
			  		File  keyStoreFile, String  keyStoreType){
		  
		  KEYSTORE_PASSWORD = keyStorePass;
		  KEY_PASSWORD = keyPass;
		 // KEY_ALIAS = keyAlias;
		  KEY_STORE_FILE = keyStoreFile;
		  KEY_STORE_TYPE = keyStoreType;
	  }
	
	public static Node signDocument(Document documento) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException, CertificateException, FileNotFoundException, IOException, UnrecoverableEntryException, KeyException, MarshalException, XMLSignatureException, SOAPException, 
				ParserConfigurationException, SAXException, TransformerException {
  
	   // se incluye esta traza para obligar a descargar el jar
	 //  System.out.println(" Try to load axis2-saaj-api jar. Factory class:"+ org.apache.axis2.saaj.MessageFactoryImpl.class.getCanonicalName());
	   //MessageFactory factory = (MessageFactory)find("javax.xml.soap.MessageFactory");
	   
	   
	    SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
	    System.out.println(""+soapMessage.getClass());
	    SOAPPart soapPart = soapMessage.getSOAPPart();
	    SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
	    
	    SOAPHeader soapHeader = soapEnvelope.getHeader();
	    soapEnvelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
	    soapEnvelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	    
	    SOAPBody soapBody = soapEnvelope.getBody();
	    soapBody.addAttribute(soapEnvelope.createName("id")	, "msgBody");
	    soapEnvelope.addNamespaceDeclaration("xmlns", "http://www.catastro.meh.es/");
	    
	    soapBody.addDocument(documento);
	    
	    Source source = soapPart.getContent();
	    Node root = null;
	    if (source instanceof DOMSource) 
	    {
	      root = ((DOMSource) source).getNode();

	    } 
	    else if (source instanceof SAXSource) {
	      InputSource inSource = ((SAXSource) source).getInputSource();
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      dbf.setNamespaceAware(true);
	      DocumentBuilder db = null;

	      db = dbf.newDocumentBuilder();
	      Document doc = db.parse(inSource);
	      root = (Node) doc.getDocumentElement();
	    }

	    Node signedRoot=sign(root);
	    
	    return signedRoot;
	}
	
	private static Node sign(Node root) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException, CertificateException, FileNotFoundException, IOException, UnrecoverableEntryException, KeyException, MarshalException, XMLSignatureException {
		
		//En el fichero java.security de JAVA, se necesita tener el siguiente proveedor, para realizar el firmado del
		// mensaje SOAP
		// security.provider.7=org.jcp.xml.dsig.internal.dom.XMLDSigRI
	
		XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance();
		 
		Transform transform = sigFactory.newTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS,(TransformParameterSpec)null);
		
        Reference ref = sigFactory.newReference("#msgBody", sigFactory.newDigestMethod(DigestMethod.SHA1,null),
        		Collections.singletonList(transform), null, null);
       
	    SignedInfo signedInfo = sigFactory.newSignedInfo(sigFactory.newCanonicalizationMethod(
	        CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null), sigFactory
	        .newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));
	    
	    KeyInfoFactory kif = sigFactory.getKeyInfoFactory();
	 // Load the KeyStore and get the signing key and certificate.
////////////////////////////////////////////////////////////////////////////////////
	    KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
	    keyStore.load(new FileInputStream(KEY_STORE_FILE), KEYSTORE_PASSWORD.toCharArray());
	    
	    Enumeration listaAlias = keyStore.aliases();
	    String KEY_ALIAS = null;

        while (listaAlias.hasMoreElements())
        {
        	KEY_ALIAS = (String) listaAlias.nextElement();
        }
  
	    KeyStore.PrivateKeyEntry keyEntry =
	        (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray()));
	    Certificate certificate = keyEntry.getCertificate();
		X509Certificate cert = (X509Certificate) certificate;
		PublicKey publicKey = cert.getPublicKey();		
		Key privateKey = keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
	    // Create the KeyInfo containing the X509Data.
/////////////////////////////////////////////////////////////////////////////////

	    List x509Content = new ArrayList();	
	    x509Content.add(cert); // para poner el certificado X509
	    
	    X509Data x509Data = kif.newX509Data(x509Content);
	    KeyValue publicKeyValue = (kif.newKeyValue(publicKey));
	    
	    List keyInfoList = new ArrayList(); // key Info tendrá dos elementos X509 y la clave pública, aunque como se ve se saca del certificado.
	    keyInfoList.add(x509Data);
	    keyInfoList.add(publicKeyValue);
	    
		KeyInfo keyInfo = kif.newKeyInfo(keyInfoList);
	    
		// prepara el contexto para firmar
	    XMLSignature sig = sigFactory.newXMLSignature(signedInfo, keyInfo);
	    
	    Element envelope = (Element) ("Envelope".equals(root.getLocalName())?root:getFirstChildElement(root));
	    Element header = getFirstChildElement(envelope);
	    DOMSignContext sigContext = new DOMSignContext(privateKey, header);
	    sig.sign(sigContext);
	    
	    return root;
	}
	
	 public static Document createDOMMessage(InputStream is)
     		throws FactoryConfigurationError, ParserConfigurationException, SAXException,IOException, FileNotFoundException
	{
		 // Load the SOAP request
		 org.w3c.dom.Document doc = null;
		 javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		
		 dbf.setNamespaceAware(true);
		 dbf.setValidating(false);
		 try
		 {
		     dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
		
		     DocumentBuilder db = dbf.newDocumentBuilder();
		     db.setErrorHandler(new org.apache.xml.security.utils.IgnoreAllErrorHandler());
		     doc = db.parse(is);
		 } 
		 catch (IllegalArgumentException e) {
			     // BUG en el JDK 1.4 con la versión de Xerces
			 Properties prop = System.getProperties();
			 String home = System.getProperty("sun.boot.library.path");
			 String msg = "Copie las siguientes librerías en el directorio "
			         + System.getProperty("java.endorsed.dirs") + "\n" + "xml-apis.jar\n"
			         + "xercesImpl.jar";
		
		 }
		 return doc;
	}
	 
	
	private static void dumpToFile(Node signedRoot2, String signedFilename2)
			throws FileNotFoundException, TransformerConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {
		OutputStream outputSigned2 = new FileOutputStream(signedFilename2);
	    dumpDocument(signedRoot2, outputSigned2);
	}



	public static boolean validateSignedFile(String signedFilename) {
		/*
	     * Reload and check signature
	     * */
	    
	    DocumentBuilderFactory factory_validate = DocumentBuilderFactory.newInstance ();
	    Document documento_validate = null;

	    try
	    {
	    	factory_validate.setNamespaceAware(true);
	       DocumentBuilder builder_validate = factory_validate.newDocumentBuilder();
	       documento_validate = builder_validate.parse( new File(signedFilename) );
	       Node root2 = (Node)documento_validate.getDocumentElement();
	       
	       System.out.println("Validate the signature mensaje Soap.\n\n\n");
		   
		 // Find Signature element
	        NodeList nl = documento_validate.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
	        if (nl.getLength() == 0) {
	            throw new Exception("Cannot find Signature element");
	        }
	        Element sigElement_validate = (Element) nl.item(0);
	        
		    DOMValidateContext valContext_validate = new DOMValidateContext(new KeyValueKeySelector(), sigElement_validate);
		   // valContext_validate.setIdAttributeNS(getNextSiblingElement(header), // el Body mejor buscarlo por nombre
		    //    "http://schemas.xmlsoap.org/soap/security/2000-12", "id");
		 // Create a DOM XMLSignatureFactory that will be used to unmarshal the
	        // document containing the XMLSignature
	        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
	        // unmarshal the XMLSignature
	        XMLSignature signature = fac.unmarshalXMLSignature(valContext_validate);
		    boolean valid_validate = signature.validate(valContext_validate);

		    System.out.println("Signature valid? " + valid_validate);
		    // Check core validation status
	        if (valid_validate == false) {
	            System.err.println("Signature failed core validation");
	            boolean sv = signature.getSignatureValue().validate(valContext_validate);
	            System.out.println("signature validation status: " + sv);
	            // check the validation status of each Reference
	            Iterator i = signature.getSignedInfo().getReferences().iterator();
	            for (int j=0; i.hasNext(); j++) {
	                Reference reference = (Reference) i.next();
					boolean refValid =
	                    reference.validate(valContext_validate);
	                System.out.println("ref["+j+"] validity status: " + refValid);
	            }
	        } else {
	            System.out.println("Signature passed core validation");
	        }
	       
	       
	       return valid_validate;
	       
	    }
	    catch (Exception spe)
	    {
	    	spe.printStackTrace();
	    	return false;

	    }
	}
	
	 /**
     * KeySelector which retrieves the public key out of the
     * KeyValue element and returns it.
     * NOTE: If the key algorithm doesn't match signature algorithm,
     * then the public key will be ignored.
     */
    private static class KeyValueKeySelector extends KeySelector {
        public KeySelectorResult select(KeyInfo keyInfo,
                                        KeySelector.Purpose purpose,
                                        AlgorithmMethod method,
                                        XMLCryptoContext context)
            throws KeySelectorException {
            if (keyInfo == null) {
                throw new KeySelectorException("Null KeyInfo object!");
            }
            SignatureMethod sm = (SignatureMethod) method;
            List list = keyInfo.getContent();

            for (int i = 0; i < list.size(); i++) {
            	PublicKey pk = null;
                XMLStructure xmlStructure = (XMLStructure) list.get(i);
                if (xmlStructure instanceof KeyValue) {
                    
                    try {
                        pk = ((KeyValue)xmlStructure).getPublicKey();
                    } catch (KeyException ke) {
                        throw new KeySelectorException(ke);
                    }
                    // make sure algorithm is compatible with method
                    if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                        return new SimpleKeySelectorResult(pk);
                    }
                }
                else if (xmlStructure instanceof X509Data)
                {
                	List x509datas = ((X509Data)xmlStructure).getContent();
                	X509Certificate x509cert = (X509Certificate) x509datas.get(0); // TODO iterate and check type
                	pk = x509cert.getPublicKey();
                }
                
                // make sure algorithm is compatible with method
                if (pk!=null && algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                    return new SimpleKeySelectorResult(pk);
                }
            }
            throw new KeySelectorException("No KeyValue element found!");
        }
        
        private static class SimpleKeySelectorResult implements KeySelectorResult {
            private PublicKey pk;
            SimpleKeySelectorResult(PublicKey pk) {
                this.pk = pk;
            }

            public Key getKey() { return pk; }
        }

        //@@@FIXME: this should also work for key types other than DSA/RSA
        static boolean algEquals(String algURI, String algName) {
            if (algName.equalsIgnoreCase("DSA") &&
                algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
                return true;
            } else if (algName.equalsIgnoreCase("RSA") &&
                       algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
                return true;
            } else {
                return false;
            }
        }
    }

	  public static void dumpDocument(Node root) throws TransformerException {
		PrintStream outputStream = System.out;
	    dumpDocument(root, outputStream);
	  }

	private static void dumpDocument(Node root, OutputStream outputStream)
			throws TransformerConfigurationException,
			TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "no");
	    transformer.transform(new DOMSource(root), new StreamResult(outputStream));
	}

	  private static Element getFirstChildElement(Node node) {
	    Node child = node.getFirstChild();
	    while ((child != null) && (child.getNodeType() != Node.ELEMENT_NODE)) {
	      child = child.getNextSibling();
	    }
	    return (Element) child;
	  }

	  public static Element getNextSiblingElement(Node node) {
	    Node sibling = node.getNextSibling();
	    while ((sibling != null) && (sibling.getNodeType() != Node.ELEMENT_NODE)) {
	      sibling = sibling.getNextSibling();
	    }
	    return (Element) sibling;
	  }
	  
	  /**
	     * Sends SOAP through https request
	     * 
	     * @param doc
	     * @param keyStorePath
	     * @param trustStorePath
	     * @param trustStorePassword
	     * @param keyStorePassword
	     * @return
	     * @throws TransformerConfigurationException
	     * @throws TransformerException
	     * @throws IOException
	     * @throws HttpException
	     */
	    public static String sendRequest(Document doc, String keyStorePath,
	    		String keyStorePassword, String trustStorePath, String trustStorePassword)
	    throws TransformerConfigurationException, TransformerException,
	    HttpException, IOException
	    {
	    	String service = AppContext.getApplicationContext().getString("ovc.service");
	    	String action = AppContext.getApplicationContext().getString("ovc.soap.action");
	    	String host = AppContext.getApplicationContext().getString("ovc.host");
	    	String post = AppContext.getApplicationContext().getString("ovc.post");
	    	
	    	return sendRequest(doc,keyStorePath,keyStorePassword,trustStorePath,trustStorePassword,
	    			service,action,host,post).toString();
	    }
	    
	    public static InputStream sendRequest(Document doc, String keyStorePath,
	            String keyStorePassword, String trustStorePath, String trustStorePassword,
	            String service,String action, String host,String post)
	            throws TransformerConfigurationException, TransformerException,
	            HttpException, IOException
	    {
	    	
	        // Vendría el mandar el xml
	        //HttpClient cliente = new HttpClient();
	    	HttpClient cliente = AppContext.getHttpClient();

	        PostMethod metodoPost = new PostMethod(service);
	        metodoPost.setRequestHeader("POST", post);
	        metodoPost.setRequestHeader("Host", host);
	        metodoPost.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
	        metodoPost.setRequestHeader("SOAPAction", action);

	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        XMLUtils.outputDOMc14nWithComments(doc, os);
	        String request = os.toString();
	        metodoPost.setRequestBody(request);

	        System.out.println (" ----- PETICION -----  \n" + metodoPost.getRequestCharSet() +"\n ----- FIN PETICION -----");
	        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
	        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
	        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

	        if (trustStorePath != null)
	        {
	            System.setProperty("javax.net.ssl.trustStore", trustStorePath);
	            if (trustStorePassword != null)
	                System
	                        .setProperty("javax.net.ssl.trustStorePassword",
	                                trustStorePassword);
	        } else if (System.getProperty("javax.net.ssl.trustStore") == null)
	        {
	            logger
	                    .info("sendRequest() - Debe configurar el path y la contraseña del almacén de certificados de autoridades de confianza en el panel de control de Java.\n Claves:  javax.net.ssl.trustStorePassword y javax.net.ssl.trustStore");
	        }
	        // System.setProperty("javax.net.debug","ssl,handshake");

	        
	        //COMENTAMOS la línea de ejecución del envío hasta que se tenga el servicio web activo
	        //int resultado = cliente.executeMethod(metodoPost);
	       
	        /*if (resultado != GeopistaXMLSigImpl.SERVEROK)
	            throw new HttpException(metodoPost.getResponseBodyAsString());
	        
	        if (logger.isDebugEnabled())
	        {
	            logger
	                    .debug("sendRequest(Document, String, String, String, String) - El resultado es");
	        }
	        if (logger.isDebugEnabled())
	        {
	            logger.debug("sendRequest(Document, String, String, String, String)"
	                    + resultado);
	        }
	        if (logger.isDebugEnabled())
	        {
	            logger.debug("sendRequest(Document, String, String, String, String)"
	                    + metodoPost.getResponseBodyAsString());
	        }

	        return metodoPost.getResponseBodyAsStream();*/
	        
	        //Código para obtener la respuesta de prueba como un InputStream
	        InputStream resultado = new FileInputStream("C:\\prueba.xml");
	        return resultado;
	        
	    }

}
