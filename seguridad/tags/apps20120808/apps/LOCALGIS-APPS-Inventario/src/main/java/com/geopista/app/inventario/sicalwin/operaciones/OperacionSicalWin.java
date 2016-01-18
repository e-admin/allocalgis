package com.geopista.app.inventario.sicalwin.operaciones;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.CiServiceLocator;
import com.geopista.app.inventario.sicalwin.cliente.accede.services.Ci.CiSoapBindingStub;
import com.geopista.app.inventario.sicalwin.util.ConfigSicalwin;
import com.geopista.app.inventario.sicalwin.util.DateUtils;
import com.geopista.app.inventario.sicalwin.util.DecodeUtils;

public class OperacionSicalWin {

	private static org.apache.log4j.Logger Logger = org.apache.log4j.Logger.getLogger(OperacionSicalWin.class);
	
	protected String nonce;
	protected String tokenSha512;
	protected String fechaFormateada;
	protected Document doc;
	protected Element rootElement;

	
	/* Datos de acceso */
	protected final String USUARIO = "MODELO";
	protected final String CLAVE = "MDL324.";
	protected final String CODIGO_ENTIDAD = "0000000001";
	protected final String CODIGO_ORG = "0000000001";

	
	/**
	 * Inicializamos la peticion
	 * @throws ParserConfigurationException
	 */
	protected void initRequest() throws ParserConfigurationException {
		
		generaFecha();
		calcularCamposSeguridad();
		
		doc = createDocument();
		rootElement = (Element) doc.createElement("e");
		doc.appendChild(rootElement);
		
	}
	
	protected void creaNodo(Element element, String newElement,
			String valorNewElement) {
		Element elementTMP = (Element) doc.createElement(newElement);
		element.appendChild(elementTMP);
		//DESCOMENTAR - SIN DEPENDENCIA
		//elementTMP.setTextContent(valorNewElement);
		//TEMPORAL - BORRAR
		element.setNodeValue(valorNewElement);
	}

	protected void creaOPE(Element e, String apl, String tobj, String cmd,
			String ver) {
		Element ope = (Element) doc.createElement("ope");
		e.appendChild(ope);

		creaNodo(ope, "apl", apl);
		creaNodo(ope, "tobj", tobj);
		creaNodo(ope, "cmd", cmd);
		creaNodo(ope, "ver", ver);
	}

	protected void creaSEC(Element e, String cli, String org, String ent,
			String eje, String usu, String pwd) {
		Element sec = (Element) doc.createElement("sec");
		e.appendChild(sec);

		creaNodo(sec, "cli", cli);
		creaNodo(sec, "org", org);
		creaNodo(sec, "ent", ent);
		creaNodo(sec, "usu", usu);
		creaNodo(sec, "pwd", DecodeUtils.codificarSha1Base64(pwd));
		creaNodo(sec, "eje", eje);
		creaNodo(sec, "fecha", fechaFormateada);
		creaNodo(sec, "nonce", nonce);
		creaNodo(sec, "token", tokenSha512);
	}

	protected void creaFixedPAR(Element e) {
		Element par = (Element) doc.createElement("par");
		e.appendChild(par);

		Element l_tercero = (Element) doc.createElement("l_tercero");
		par.appendChild(l_tercero);

		Element tercero = (Element) doc.createElement("tercero");
		l_tercero.appendChild(tercero);

		creaNodo(tercero, "idenTercero", "1");
	}

	protected void creaPAR(Element e, HashMap mapPar) {
		Element par = (Element) doc.createElement("par");
		e.appendChild(par);

		// Iterator iterator = mapPar.keySet().iterator();

		// while(iterator. hasNext()){
		// Map.Entry parTMP = (Map.Entry) iterator.next();

		Iterator it = mapPar.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry parTMP = (Map.Entry) it.next();
			// System.out.println(e.getKey() + " " + e.getValue());

			if (parTMP.getValue() != null) {
				if (parTMP.getValue() instanceof String) {
					// creaNodo(par, (String) parTMP.getKey(), base64((String)
					// parTMP.getValue()));
					creaNodo(par, (String) parTMP.getKey(),
							(String) parTMP.getValue());
				} else {
					if (parTMP.getValue() instanceof GregorianCalendar) {
						creaNodo(par, (String) parTMP.getKey(),
								DateUtils.formateaFecha((GregorianCalendar) parTMP
										.getValue()));
					} else {
						if (parTMP.getValue() instanceof Float) {
							creaNodo(par, (String) parTMP.getKey(),
									DateUtils.formateaFloat((Float) parTMP.getValue()));
						} else {
							if (parTMP.getValue() instanceof Boolean) {
								if ((Boolean) parTMP.getValue()) {
									creaNodo(par, (String) parTMP.getKey(),
											"-1");
								} else
									creaNodo(par, (String) parTMP.getKey(), "0");
							}
						}
					}
				}

			}
		}
	}

	/**
	 * Crea un documento xml
	 * 
	 * @throws ParserConfigurationException
	 */
	private  Document createDocument()
			throws ParserConfigurationException {
		// get the factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// Using factory get an instance of document builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		DOMImplementation impl = builder.getDOMImplementation();

		Document doc = impl.createDocument(null, null, null);

		return doc;
	}

	protected String parseDocumentToXML(Document doc)
			throws TransformerException {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();

			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
			transformer.clearParameters();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private void generaFecha() {
		// Obtener la fecha, que debe coincidir con la enviada en el campo fecha
		// de Sec

		Date fecha = new Date();
		GregorianCalendar calendario = new GregorianCalendar();
		calendario.setTime(fecha);

		fechaFormateada = DateUtils.formateaFecha(calendario);
	}
	

	/*
	 * El parámetro clavePublica es una clave conocida solamente por el
	 * aplicativo que genera el SML y por el aplicativo que recibe el SML para
	 * asegurar la fidelidad del mensaje. Esta clave a de estar parametrizada en
	 * los aplicativos para poder sincronizarla en los aplicativos llamantes y
	 * receptores.
	 */
	protected void calcularCamposSeguridad() {
		// Obtener el nonce
		SecureRandom random = new SecureRandom();
		nonce =String.valueOf(random.nextLong());		
		tokenSha512 = DecodeUtils.codificarSha512Base64(nonce + fechaFormateada+ ConfigSicalwin.CLAVE_PUBLICA);
	}
	
	/**
	 * Envio al servidor de la peticion
	 * @param sml
	 */
	protected Document sendToServer(String sml){
		
		Document document=null;
		try {
			
			Logger.info("SML:" + sml.toString());
			CiSoapBindingStub binding = null;

			try {
				binding = (CiSoapBindingStub) new CiServiceLocator().getCi();
			} catch (ServiceException e1) {
				e1.printStackTrace();
			}
			binding.setTimeout(60000);// esperamos 1 minuto
			String resultado = binding.servicio(sml.toString());
			
			Logger.info(resultado);
			
			resultado="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+resultado;
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(new InputSource(new ByteArrayInputStream(resultado.getBytes("utf-8"))));
			document.getDocumentElement().normalize();
			

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}
	
	private String getNodeValue(Document doc,String pattern){
		String value=null;
		
		NodeList nodeLst = doc.getElementsByTagName(pattern);
		if (nodeLst.getLength()>0){
			Node node=nodeLst.item(0);
			if (node!=null){
				value=node.getFirstChild().getNodeValue();				
			}				
		}
		return value;
	}
	
	protected boolean isExito(Document doc){
		boolean exito=false;
		String value=getNodeValue(doc,"exito");
		if ((value!=null) && (value.equals("-1")))
			exito=true;
		
		return exito;
	}
	
	protected String getInfo(Document doc,String pattern){
		return getNodeValue(doc,pattern);
	}
		
}
