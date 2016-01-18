/**
 * GeopistaXMLSigImpl2.java
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.geopista.app.AppContext;

/**
 * TODO Documentación
 * 
 * @author juacas
 * 
 */
public class GeopistaXMLSigImpl2
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(GeopistaXMLSigImpl2.class);

    private static final int SERVEROK = 200;

    /**
     * 
     */
    public GeopistaXMLSigImpl2()
        {
            super();
            // TODO Auto-generated constructor stub
        }

    /**
     * Cleans \n from all text elements. Included for compatibility with
     * Microsoft's implementation. .NET misunderstanding of base64binary blocks
     * has been observed when lines are wrapped
     * 
     * @param el
     */
    private static void cleanNewLinesFromData(Node el)
    {
        Node nod = el.getFirstChild();
        Node nextnod = null;
        if (logger.isDebugEnabled())
        {
            logger.debug("cleanNewLinesFromData(Node) - Scanning: " + el.getNodeName());
        }
        do
        {
            if (nod == null)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("cleanNewLinesFromData(Node) -  ---. ");
                }
                continue;
            }
            nextnod = nod.getNextSibling();

            if (nod.getNodeType() == Node.TEXT_NODE)
            {
                String val = nod.getNodeValue();
                if (val.indexOf("\n") > -1)
                {
                    String newval = val.replaceAll("\n", "");
                    nod.setNodeValue(newval);
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("cleanNewLinesFromData(Node) - Filtered... "
                                + (val.length() - newval.length()));
                    }
                }

            }

            cleanNewLinesFromData(nod);
            nod = nextnod;
        } while (nod != null);
    }

    /**
     * Remove all text elements with only "\n" Included for compatibility with
     * Microsoft's implementation. .NET has a different interpretation of
     * exc_c14n transform than Apache XMLSec (And other Java providers)
     * 
     * @param el
     */
    private static void cleanNewLinesFromDOM(Node el)
    {
        Node nod = el.getFirstChild();
        Node nextnod = null;
        if (logger.isDebugEnabled())
        {
            logger.debug("cleanNewLinesFromDOM(Node) - Scanning: " + el.getNodeName());
        }
        do
        {
            if (nod == null)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("cleanNewLinesFromDOM(Node) -  ---. ");
                }
                continue;
            }
            nextnod = nod.getNextSibling();

            if (nod.getNodeType() == Node.TEXT_NODE)
            {
                String val = nod.getNodeValue();
                if ("\n".equals(val))
                {
                    if (logger.isDebugEnabled())
                    {
                        logger
                                .debug("cleanNewLinesFromDOM(Node) - Erased text node: \n  from: "
                                        + el.getLocalName());
                    }
                    el.removeChild(nod);
                }
            } else if (logger.isDebugEnabled())
            {
                logger.debug("cleanNewLinesFromDOM(Node)");
            }

            cleanNewLinesFromDOM(nod);
            nod = nextnod;
        } while (nod != null);
    }

    /**
     * Signs document and dump contents to a file Uses Apache XMLsec
     * implementation
     * 
     * @param signedFileName
     * @param certificateAlias
     * @param ks
     * @param privateKey
     * @param doc
     * @throws XMLSecurityException
     * @throws TransformationException
     * @throws XMLSignatureException
     * @throws KeyStoreException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws TransformerException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     */
    public static void createSignedXML(String signedFileName, String certificateAlias,
            KeyStore ks, String privateKeyPass, org.w3c.dom.Document doc)
            throws XMLSecurityException, TransformationException, XMLSignatureException,
            KeyStoreException, FileNotFoundException, IOException, TransformerException,
            NoSuchAlgorithmException, UnrecoverableKeyException
    {
        doc = signDoc(certificateAlias, ks, privateKeyPass, doc);

        // Dump the signed request to file
        FileOutputStream f = new FileOutputStream(signedFileName);
        // XMLUtils.outputDOM(doc, f);
        dumpDOM2(doc, f);

        f.close();
        if (logger.isDebugEnabled())
        {
            logger
                    .debug("createSignedXML(String, String, KeyStore, String, org.w3c.dom.Document) - Wrote signature to "
                            + signedFileName);
        }
    }

    public static void dumpDOM2(Document doc, OutputStream os)
            throws TransformerException
    {

        org.apache.xml.security.Init.init();
        // Dump the signed request to file
        XMLUtils.outputDOM(doc, os);
    }

    public static void main(String unused[]) throws Exception
    {

        // The file from which we will load the request SOAP message
        // String fileName =
        // "C:/desarrollo/eclipse_old/OVCConnect/lib/request.xml";
        String fileName = "C:/desarrollo/eclipse_old/OVCConnect/lib/request.xml";
        // Store the signed request here
        String signedFileName = "C:/desarrollo/eclipse_old/OVCConnect/lib/SignedRequest.xml";

        // Keystore parameters
        String keystoreType = "PKCS12";
        String keystoreFile = "C:/desarrollo/eclipse_old/OVCConnect/lib/txomin.pfx";
        String keystorePass = "txomin";
        String privateKeyAlias = "{98a1441b-e66d-4c0b-8c44-4781789d8570}";
        String privateKeyPass = "txomin";
        String certificateAlias = "{98a1441b-e66d-4c0b-8c44-4781789d8570}";

        // Load the keystore
        KeyStore ks = KeyStore.getInstance(keystoreType);
        FileInputStream fis = new FileInputStream(keystoreFile);
        ks.load(fis, keystorePass.toCharArray());

        org.w3c.dom.Document doc = createSOAPMessage(fileName);
        // byte[] c14z = testMSSignedInfoBug();
        // doc = signDoc(certificateAlias,ks,privateKey,doc);
        // String fakedSignatureValue= makeFakedSignatureValue(doc,privateKey);
        // System.out.println(fakedSignatureValue);
        createSignedXML(signedFileName, certificateAlias, ks, privateKeyPass, doc);
        // sendRequest(doc);
    }

    /**
     * @param fileName
     * @return
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static org.w3c.dom.Document createSOAPMessage(String fileName)
            throws FactoryConfigurationError, ParserConfigurationException, SAXException,
            IOException, FileNotFoundException
    {
        return createDOMMessage(new java.io.FileInputStream(new File(fileName)));
    }

    public static org.w3c.dom.Document createDOMMessage(InputStream is)
            throws FactoryConfigurationError, ParserConfigurationException, SAXException,
            IOException, FileNotFoundException

    {
        // Load the SOAP request
        org.w3c.dom.Document doc = null;
        javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory
                .newInstance();

        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        try
        {
            dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);

            javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new org.apache.xml.security.utils.IgnoreAllErrorHandler());
            doc = db.parse(is);
        } catch (IllegalArgumentException e)
        {
            // BUG en el JDK 1.4 con la versión de Xerces
            Properties prop = System.getProperties();
            String home = System.getProperty("sun.boot.library.path");
            String msg = "Copie las siguientes librerías en el directorio "
                    + System.getProperty("java.endorsed.dirs") + "\n" + "xml-apis.jar\n"
                    + "xercesImpl.jar";
            if (logger.isDebugEnabled())
            {
                logger.debug("createSOAPMessage(InputStream)" + msg);
            }

        }
        return doc;
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
    	
    	service="http://ovc.catastro.meh.es/ovcservweb/OVCSWLocalizacionRC/OVCCallejero.asmx";
    	host="ovc.catastro.meh.es";
    	post="/ovcservweb/OVCSWLocalizacionRC/OVCCallejero.asmx";
    	action="http://tempuri.org/OVCServWeb/OVCCallejero/Consulta_DNPPP";
    	
    	return sendRequest(doc,keyStorePath,keyStorePassword,trustStorePath,trustStorePassword,
    			service,action,host,post).toString();
    }
    
    public static String sendRequest(String request)
    throws TransformerConfigurationException, TransformerException,
    HttpException, IOException
    {
    	String service = AppContext.getApplicationContext().getString("ovc.service");
    	String action = AppContext.getApplicationContext().getString("ovc.soap.action");
    	String host = AppContext.getApplicationContext().getString("ovc.host");
    	String post = AppContext.getApplicationContext().getString("ovc.post");
    	
    	service="http://ovc.catastro.meh.es/ovcservweb/OVCSWLocalizacionRC/OVCCallejero.asmx";
    	host="ovc.catastro.meh.es";
    	post="/ovcservweb/OVCSWLocalizacionRC/OVCCallejero.asmx";
    	action="http://tempuri.org/OVCServWeb/OVCCallejero/Consulta_DNPRC";
    	
    	return sendRequest(request,"","","","",service,action,host,post).toString();
    }

    
    public static String sendRequest(String request, String keyStorePath,
            String keyStorePassword, String trustStorePath, String trustStorePassword,
            String service,String action, String host,String post)
            throws TransformerConfigurationException, TransformerException,
            HttpException, IOException
    {
    	
        // Vendría el mandar el xml
        //HttpClient cliente = new HttpClient();
    	HttpClient cliente = AppContext.getHttpClient();

        PostMethod metodoPost = new PostMethod(service);
        //metodoPost.setRequestHeader("POST", post);
        metodoPost.setRequestHeader("Host", host);
        metodoPost.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
        metodoPost.setRequestHeader("SOAPAction", action);

        metodoPost.setRequestBody(request);

        //System.out.println (" ----- PETICION -----  \n" + metodoPost.getRequestCharSet() +"\n ----- FIN PETICION -----");
        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");

        if (trustStorePath != null)
        {
            //System.setProperty("javax.net.ssl.trustStore", trustStorePath);
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
        int resultado = cliente.executeMethod(metodoPost);
       
        if (resultado != GeopistaXMLSigImpl2.SERVEROK)
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

        String cadenaResultado=metodoPost.getResponseBodyAsString();
        return cadenaResultado;
        
        //Código para obtener la respuesta de prueba como un InputStream
        //InputStream resultado = new FileInputStream("C:\\prueba.xml");
        //return resultado;
        
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
        //metodoPost.setRequestHeader("POST", post);
        metodoPost.setRequestHeader("Host", host);
        metodoPost.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
        metodoPost.setRequestHeader("SOAPAction", action);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLUtils.outputDOMc14nWithComments(doc, os);
        String request = os.toString();
        metodoPost.setRequestBody(request);

        //System.out.println (" ----- PETICION -----  \n" + metodoPost.getRequestCharSet() +"\n ----- FIN PETICION -----");
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
        int resultado = cliente.executeMethod(metodoPost);
       
        if (resultado != GeopistaXMLSigImpl2.SERVEROK)
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

        return metodoPost.getResponseBodyAsStream();
        
        //Código para obtener la respuesta de prueba como un InputStream
        //InputStream resultado = new FileInputStream("C:\\prueba.xml");
        //return resultado;
        
    }

    /**
     * @param certificateAlias
     * @param ks
     * @param privateKey
     * @param doc
     * @throws XMLSecurityException
     * @throws TransformationException
     * @throws XMLSignatureException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     */
    public static Document signDoc(String certificateAlias, KeyStore ks,
            String privateKeyPass, org.w3c.dom.Document doc) throws XMLSecurityException,
            TransformationException, XMLSignatureException, KeyStoreException,
            NoSuchAlgorithmException, UnrecoverableKeyException
    {
        // Initialize the library
        org.apache.xml.security.Init.init();
        Constants.setSignatureSpecNSprefix("");

        // Look for the SOAP header
        Element headerElement = null;
        NodeList nodes = doc.getElementsByTagNameNS(
                "http://schemas.xmlsoap.org/soap/envelope/", "Header");
        if (nodes.getLength() == 0)
        {
            if (logger.isDebugEnabled())
            {
                logger
                        .debug("signDoc(String, KeyStore, String, org.w3c.dom.Document) - Adding a SOAP Header Element");
            }
            headerElement = doc.createElementNS(
                    "http://schemas.xmlsoap.org/soap/envelope/", "Header");
            nodes = doc.getElementsByTagNameNS(
                    "http://schemas.xmlsoap.org/soap/envelope/", "Envelope");
            if (nodes != null)
            {
                Element envelopeElement = (Element) nodes.item(0);
                headerElement.setPrefix(envelopeElement.getPrefix());
                envelopeElement.appendChild(headerElement);
            }
        } else
        {
            if (logger.isDebugEnabled())
            {
                logger
                        .debug("signDoc(String, KeyStore, String, org.w3c.dom.Document) - Found "
                                + nodes.getLength() + " SOAP Header elements.");
            }
            headerElement = (Element) nodes.item(0);
        }

        // Create an XMLSignature instance

        XMLSignature sig = new XMLSignature(doc, null,
                XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1,
                Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        Element el = sig.getElement();

        headerElement.appendChild(sig.getElement());

        // Specify the transforms
        Transforms transforms = new Transforms(doc);
        // transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
        transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
        sig.addDocument("#MsgBody", transforms,
                org.apache.xml.security.utils.Constants.ALGO_ID_DIGEST_SHA1);

        // Add the certificate and public key information from the keystore;
        // this will be needed by the verifier
        X509Certificate cert = (X509Certificate) ks.getCertificate(certificateAlias);
        // Reference ref=new Reference()
        // sig.getSignedInfo().;
        // And get the private key that will be used to sign the request
        PrivateKey privateKey = (PrivateKey) ks.getKey(certificateAlias, privateKeyPass
                .toCharArray());
        sig.addKeyInfo(cert);
        sig.addKeyInfo(cert.getPublicKey());

        if (logger.isDebugEnabled())
        {
            logger
                    .debug("signDoc(String, KeyStore, String, org.w3c.dom.Document) - Cleaning");
        }
        // clean
        cleanNewLinesFromDOM(el);

        if (logger.isDebugEnabled())
        {
            logger
                    .debug("signDoc(String, KeyStore, String, org.w3c.dom.Document) - Starting to sign SOAP Request");
        }
        sig.sign(privateKey);
        if (logger.isDebugEnabled())
        {
            logger
                    .debug("signDoc(String, KeyStore, String, org.w3c.dom.Document) - Finished signing. Canonicalized.");
        }
        byte[] c14nBytes = sig.getSignedInfo().getCanonicalizedOctetStream();
        if (logger.isDebugEnabled())
        {
            logger.debug("signDoc(String, KeyStore, String, org.w3c.dom.Document)"
                    + new String(c14nBytes));
        }
        if (logger.isDebugEnabled())
        {
            logger
                    .debug("signDoc(String, KeyStore, String, org.w3c.dom.Document) - re-Cleaning");
        }
        // clean
        //cleanNewLinesFromData(sig.getElement());
        return doc;
    }

}
