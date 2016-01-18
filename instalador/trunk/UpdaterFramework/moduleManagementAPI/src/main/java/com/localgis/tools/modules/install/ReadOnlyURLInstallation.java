package com.localgis.tools.modules.install;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.XMLModuleSerializer;
import com.localgis.tools.modules.exception.DependencyViolationException;
import com.localgis.tools.modules.exception.XMLError;

public class ReadOnlyURLInstallation extends AbstractLocalGISInstallation
{

	public ReadOnlyURLInstallation(URL url) throws IOException {
		this.connect(url, "", "");
	}

	@Override
	public void connect(URL localGISLocation, String username, String password)
			throws IOException {
		try {
			getInstallationFromURL(localGISLocation);
		} catch (URISyntaxException e) {
			throw new IOException("Can't load descriptor",e);
		} catch (XMLError e) {
			
			e.printStackTrace();
			throw new IOException("Can't load descriptor",e);
		}

	}

	private void getInstallationFromURL(URL localGISLocation) throws IOException, URISyntaxException, XMLError
	{
		// get Installation registry
		XMLModuleSerializer.getModuleDependencyTreeFromXMLStream( localGISLocation.openStream(), this);
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
		throw new XMLError("Error de configuraciï¿½n de parser.", e);
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

	public static ReadOnlyURLInstallation loadFromURL(URL installationURL) throws IOException {
		return new ReadOnlyURLInstallation(installationURL);
	}

}
/**
 * ReadOnlyURLInstallation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
