package com.localgis.tools.modules.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.localgis.tools.modules.Module;
import com.localgis.tools.modules.ModuleDependencyTree;
import com.localgis.tools.modules.XMLModuleSerializer;
import com.localgis.tools.modules.exception.XMLError;
import com.localgis.tools.modules.impl.ModuleImpl;

public class TestXMLModuleSerializer
{

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testLoadDependenciesFromXML()
    {
	Module mod = new ModuleImpl("TestModule", "1.0.0");
	URL testFileURL = this.getClass().getClassLoader().getResource("localGISDependencies.xml");
	File xmlFile;
	try
	    {
		xmlFile = new File(testFileURL.toURI());
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlFile);
		Node root = doc.getFirstChild();
		XMLModuleSerializer loader = new XMLModuleSerializer(mod);
		loader.loadDependenciesFromXML(root);

	    } catch (SAXException e)
	    {
		fail("Mal formato XML");
	    } catch (IOException e)
	    {
		fail("Falta fichero ejemplo");
	    } catch (ParserConfigurationException e)
	    {
		fail("Fallo en la configuración de la prueba");
	    } catch (URISyntaxException e1)
	    {
		fail("Falta fichero ejemplo. URL:" + testFileURL);
	    } catch (XMLError e)
	    {
		fail("Error en el análisis del documento." + testFileURL);
	    }
    }

    @Test
    public void testLoadModuleFromXML()
    {

	URL testFileURL = this.getClass().getClassLoader().getResource("localGISDependencies.xml");
	File xmlFile;
	try
	    {
		xmlFile = new File(testFileURL.toURI());
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlFile);

		XPath xpath = XPathFactory.newInstance().newXPath();
		Node root = (Node) xpath.evaluate("module", doc, XPathConstants.NODE);

		Module mod = XMLModuleSerializer.getModuleFromXML(root);
		moduleAssertions(mod);
	    } catch (SAXException e)
	    {
		fail("Mal formato XML");
	    } catch (IOException e)
	    {
		fail("Falta fichero ejemplo");
	    } catch (ParserConfigurationException e)
	    {
		fail("Fallo en la configuración de la prueba");
	    } catch (URISyntaxException e1)
	    {
		fail("Falta fichero ejemplo. URL:" + testFileURL);
	    } catch (XPathException e)
	    {
		fail("Falla busqueda XML");
	    } catch (XMLError e)
	    {
		fail("Mal formato XML");
	    }
    }

    @Test
    public void testLoadRegistryFromStream() throws XMLError
    {
	InputStream is = this.getClass().getClassLoader().getResourceAsStream("pack.xml");
	ModuleDependencyTree registry = XMLModuleSerializer.getModuleDependencyTreeFromXMLStream(is);
	assertEquals(3, registry.getModules().size());
    }
    private void moduleAssertions(Module mod)
    {
	assertTrue(mod.dependsOn().size() == 1);
	assertNotNull(mod.getArtifact());
    }

    @Test
    public void testLoadModuleFromURL()
    {
	Module mod;
	mod = loadTestModule();
	moduleAssertions(mod);
    }

    public Module loadTestModule()
    {
	URL testURL = this.getClass().getClassLoader().getResource("localGISDependencies.xml");
	
	try
	    {

		Module mod = XMLModuleSerializer.getModuleFromXMLURL(testURL);
		return mod;

	    } catch (IOException e)
	    {
		fail("Falta fichero ejemplo");
	    } catch (URISyntaxException e1)
	    {
		fail("Falta fichero ejemplo. URL:" + testURL);
	    } catch (XMLError e)
	    {
		fail("Error en el XML del modulo:" + testURL);
	    }
	return null;
    }

    @Test
    public void testXMLWriter()
    {
	Module mod=loadTestModule();
	StringWriter writer = new StringWriter();
	XMLModuleSerializer.write(mod, writer);
	String doc=writer.toString();
	assertTrue(doc.contains("module"));
	//System.out.print(doc);
    }
}
/**
 * TestXMLModuleSerializer.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
