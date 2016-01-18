/**
 * WebServiceURLInstallation.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules.install;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.log4j.Logger;
import org.apache.tools.ant.filters.StringInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleReference;
import com.localgis.tools.modules.XMLModuleSerializer;
import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.exception.ModuleNotFoundException;
import com.localgis.tools.modules.exception.XMLError;
/**
 * Registro de módulos que representa lo devuelto por un WebService
 * tal y como se implementa en el módulo RegistryWebApp
 * Permite descargar el contenido del registro del método getRegistry del Web Service
 * Invoca a lo métodos installModul y removeModule tras cada llamada a los 
 * métodos {@link #installModule(Module)} y {@link #removeModule(Module)} respectivamente.
 * @author juacas
 *
 */
public class WebServiceURLInstallation extends AbstractLocalGISInstallation {

	private static Logger logger= Logger.getLogger(WebServiceURLInstallation.class);
	
	public static final String WS_REGISTRY_NAMESPACE = "http://www.localgis.com/ws";
	
	private URL registryUrl;

	public WebServiceURLInstallation(URL url) {
		this.connect(url, "", "");
	}

	@Override
	public void connect(URL registryUrl, String username, String password){
		//TODO: pendiente seguridad acceso servicio
		this.registryUrl = registryUrl;
	}

	public void loadInstallationFromWebService() throws IOException {
		Exception exception = null;
		try {
			getInstallationFromServiceEndPoint();
		} catch (IOException e) {
			exception = e;
		} catch (URISyntaxException e) {
			exception  = e;
		} catch (XMLError e) {
			exception = e;
		}
		
		if (exception != null) 
			throw new IOException("Can't load descriptor", exception);
	}

	
	@Override
	public void installModule(Module mod) throws DependencyViolationException
	{
		super.installModule(mod);

		// install a module from the webservice's registry
		RPCServiceClient serviceClient = null;
		try
		{
			serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			EndpointReference targetEPR = new EndpointReference(this.registryUrl.toString());
			options.setTo(targetEPR);
			
			QName opGetRegistry = new QName(WS_REGISTRY_NAMESPACE, "installModule");

			StringWriter writer = new StringWriter();
			XMLModuleSerializer.write(mod, writer);

			Object[] args = new Object[]{writer.toString()};
			Class[] returnTypes = new Class[] {};
			
			logger.debug("Registry install module: " + mod.getName());
			serviceClient.invokeRobust(opGetRegistry, args);
		} catch (AxisFault e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Registry install module: "  + mod.getName() + sw.toString());
			throw new RuntimeException(e);
		} finally {
			if (serviceClient != null) {
				try {
					serviceClient.cleanupTransport();
				} catch (Exception e) { e.printStackTrace(); }
				try {
					serviceClient.cleanup();
				} catch (Exception e) { e.printStackTrace(); }
			}
		}	    
	}

	@Override
	public void removeModule(Module mod) throws DependencyViolationException, ModuleNotFoundException
	{
		//super.removeModule(mod);
		// remove a module from the webservice's registry
		RPCServiceClient serviceClient = null;
		try
		{
			serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			EndpointReference targetEPR = new EndpointReference(this.registryUrl.toString());
			options.setTo(targetEPR);
			QName opGetRegistry = new QName(WS_REGISTRY_NAMESPACE, "removeModule");

			StringWriter writer = new StringWriter();
			//XMLModuleSerializer.write(mod, writer);

			//Object[] args = new Object[]{writer.toString()};
			Object[] args = new Object[]{mod.getName()};
			Class[] returnTypes = new Class[] {};
			
			logger.debug("Registry remove module: " + mod.getName());
			serviceClient.invokeRobust(opGetRegistry, args);
		} catch (AxisFault e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Registry remove module: " + mod.getName() + ": " + sw.toString());
			throw new RuntimeException(e);
		} finally {
			if (serviceClient != null) {
				try {
					serviceClient.cleanupTransport();
				} catch (Exception e) { e.printStackTrace(); }
				try {
					serviceClient.cleanup();
				} catch (Exception e) { e.printStackTrace(); }
			}
		}
	}

	private void getInstallationFromServiceEndPoint() throws IOException, URISyntaxException, XMLError
	{
		RPCServiceClient serviceClient = null;
		try {
			// get Installation registry from WebService http://www.localgis.com/ws
			serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();
			EndpointReference targetEPR = new EndpointReference(this.registryUrl.toString());
			options.setTo(targetEPR);
			QName opGetRegistry = new QName(WS_REGISTRY_NAMESPACE, "getRegistry");
			Object[] args = new Object[]{};
			Class[] returnTypes = new Class[] { String.class };
			
			logger.debug("Get registry: " + this.registryUrl.toString());
			Object[] response= serviceClient.invokeBlocking(opGetRegistry, args, returnTypes);
			
			String result = (String)response[0];
			XMLModuleSerializer.getModuleDependencyTreeFromXMLStream( new StringInputStream(result), this);
			
		} catch (AxisFault e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.error("Get registry: " + sw.toString());
			throw e;
		}
	}

	@Deprecated
	private void getInstallationFromURLinstallingPack(URL localGISLocation) throws IOException, URISyntaxException, XMLError
	{
		try
		{

			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.parse(localGISLocation.toURI().toString());
			Node root = doc;
			XPath xpath = XPathFactory.newInstance().newXPath();

			NodeList e = (NodeList) xpath.evaluate("servers/modules/module", root, XPathConstants.NODESET);
			Collection<Module> installedPack = new ArrayList<Module>();
			for (int i = 0; i < e.getLength(); i++)
			{

				Module mod = XMLModuleSerializer.getModuleFromXML(e.item(i));
				installedPack.add(mod);
			}

			installPack(installedPack);

		} catch (ParserConfigurationException e)
		{
			throw new XMLError("Error de configuraci�n de parser.", e);
		} catch (XPathException e)
		{
			throw new XMLError("No se puede analizar el documento.", e);
		} catch (SAXException e)
		{
			throw new XMLError("No se puede analizar el documento.", e);
		} catch (DependencyViolationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("GRAVE ERROR", e);// TODO @todo comunicar el problema bien
		}
	}
	@Override
	public void close() 
	{
	}

	public void updateModule(ModuleReference mod) throws DependencyViolationException {
		throw new NoSuchMethodError();	
	}

	public static WebServiceURLInstallation loadFromURL(URL admcarURL) throws IOException {
		return new WebServiceURLInstallation(admcarURL);
	}

}
