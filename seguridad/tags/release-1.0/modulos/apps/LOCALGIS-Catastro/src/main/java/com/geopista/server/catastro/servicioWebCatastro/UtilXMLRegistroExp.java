package com.geopista.server.catastro.servicioWebCatastro;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.security.*;
import java.security.cert.X509Certificate;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrillo
 * Date: 06-feb-2007
 * Time: 16:34:12
 * To change this template use File | Settings | File Templates.
 */
public class UtilXMLRegistroExp
{

    private static final Log logger = LogFactory.getLog(UtilXMLRegistroExp.class);

    public static org.w3c.dom.Document createDOMMessage(InputStream is)
            throws FactoryConfigurationError, ParserConfigurationException, SAXException,
            IOException

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
        }
        catch (IllegalArgumentException e)
        {
            // BUG en el JDK 1.4 con la versión de Xerces
            Properties prop = System.getProperties();
            String home = System.getProperty("sun.boot.library.path");
            String msg = "Copie las siguientes librerías en el directorio "
                    + System.getProperty("java.endorsed.dirs") + "\n" + "xml-apis.jar\n"
                    + "xercesImpl.jar";
        }
        return doc;
    }


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
        }
        else
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
            }
            else if (logger.isDebugEnabled())
            {
                logger.debug("cleanNewLinesFromDOM(Node)");
            }
            cleanNewLinesFromDOM(nod);
            nod = nextnod;
        } while (nod != null);
    }
}
