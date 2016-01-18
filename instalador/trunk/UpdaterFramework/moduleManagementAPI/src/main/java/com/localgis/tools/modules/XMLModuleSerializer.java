/**
 * XMLModuleSerializer.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.localgis.tools.modules.exception.XMLError;
import com.localgis.tools.modules.impl.ArtifactImpl;
import com.localgis.tools.modules.impl.ModuleImpl;
import com.localgis.tools.modules.impl.VersionImpl;

/**
 * Populates the {@link Module} dependencies from an XML document
 * 
 * @author juacas
 * 
 */
public class XMLModuleSerializer
{
    private final Module module;

    public XMLModuleSerializer(Module mod) {
	this.module = mod;
    }

    public void loadDependenciesFromXML(Node root) throws XMLError
    {
	// read dependencies
	try
	    {
		readDependencies(root);
		// load incompatibilities
		// addIncompatibilities(root);
	    } catch (XPathException e)
	    {
		throw new XMLError("XPath error.", e);
	    }

    }

    /**
     * Read Module's incompatibilities from XML Document NOTE: Currently unimplemented
     * 
     * @param root
     * @throws XPathExpressionException
     */
    @SuppressWarnings("unused")
    private void addIncompatibilities(Node root) throws XPathExpressionException
    {
	NodeList e;
	XPath xpath = XPathFactory.newInstance().newXPath();
	e = (NodeList) xpath.evaluate("incompatibilities/module", root, XPathConstants.NODESET);
	for (int i = 0; i < e.getLength(); i++)
	    {
		Element name = (Element) xpath.evaluate("name", e.item(i), XPathConstants.NODE);
		Element version = (Element) xpath.evaluate("version", e.item(i), XPathConstants.NODE);
		// module.addIncompatibility(new ModuleImpl(name.getTextContent(), version.getTextContent()));
	    }
    }

    private XPath readDependencies(Node root) throws XMLError, XPathException
    {
	XPath xpath = XPathFactory.newInstance().newXPath();

	NodeList e = (NodeList) xpath.evaluate("dependencies/module", root, XPathConstants.NODESET);
	if (e.getLength() == 0)
	    {
		this.module.hasNoDependencies();
	    } else
	    {
		for (int i = 0; i < e.getLength(); i++)
		    {
			Module dep = getModuleFromXML(e.item(i));
			this.module.addDependency(dep);
			dep.addDependent(this.module);
		    }
	    }
	return xpath;
    }

    public static Module getModuleFromXMLURL(URL url) throws IOException, URISyntaxException, XMLError
    {

	try
	    {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.parse(url.toURI().toString());
		return parseModule(doc);
	    } catch (ParserConfigurationException e)
	    {
		throw new XMLError("Error de configuración de parser.", e);
	    } catch (XPathException e)
	    {
		throw new XMLError("No se puede analizar el documento.", e);
	    } catch (SAXException e)
	    {
		throw new XMLError("No se puede analizar el documento.", e);
	    }

    }

    public static ModuleDependencyTree getModuleDependencyTreeFromXMLStream(InputStream is) throws XMLError
    {
	ModuleDependencyTree registry = new ModuleDependencyTreeImpl();
	return getModuleDependencyTreeFromXMLStream(is,registry);
    }
    public static ModuleDependencyTree getModuleDependencyTreeFromXMLStream(InputStream is, ModuleDependencyTree registry) throws XMLError
    {
	try
	    {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.parse(is);

		XPath xpath = XPathFactory.newInstance().newXPath();
		Node root = (Node) xpath.evaluate("modules", doc, XPathConstants.NODE);

		NodeList moduleNodes = (NodeList) xpath.evaluate("module", root, XPathConstants.NODESET);;
		int size = moduleNodes.getLength();
		for (int i = 0; i < size; i++)
		    {
			Module mod = getModuleFromXML(moduleNodes.item(i));
			registry.register(mod);
		    }
		return registry;

	    } catch (ParserConfigurationException e)
	    {
		throw new XMLError("Error de configuración de parser.", e);
	    } catch (XPathException e)
	    {
		throw new XMLError("No se puede analizar el documento.", e);
	    } catch (SAXException e)
	    {
		throw new XMLError("No se puede analizar el documento.", e);
	    } catch (IOException e)
	    {
		throw new XMLError("No se puede leer el documento.", e);
	    }

    }

    public static Module getModuleFromXMLStream(InputStream is) throws IOException, XMLError
    {

	try
	    {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.parse(is);
		return parseModule(doc);
	    } catch (ParserConfigurationException e)
	    {
		throw new XMLError("Error de configuración de parser.", e);
	    } catch (XPathException e)
	    {
		throw new XMLError("No se puede analizar el documento.", e);
	    } catch (SAXException e)
	    {
		throw new XMLError("No se puede analizar el documento.", e);
	    }

    }

    public static Module parseModule(Document doc) throws XPathExpressionException, XMLError
    {
	XPath xpath = XPathFactory.newInstance().newXPath();

	Node root = (Node) xpath.evaluate("module", doc, XPathConstants.NODE);

	return getModuleFromXML(root);
    }

    public static Module getModuleFromXML(Node root) throws XMLError
    {
	// read dependencies
	try
	    {
		XPath xpath = XPathFactory.newInstance().newXPath();

		String name = (String) xpath.evaluate("name", root, XPathConstants.STRING);
		String version = (String) xpath.evaluate("version", root, XPathConstants.STRING);
		String description = (String) xpath.evaluate("description", root, XPathConstants.STRING);
		Node nodeArtifact = (Node) xpath.evaluate("artifact", root, XPathConstants.NODE);
		Node nodeDependency = (Node) xpath.evaluate("dependencyInfo", root, XPathConstants.NODE);
		Node upgradableFrom = (Node) xpath.evaluate("upgrades/module", root, XPathConstants.NODE);
		
		Module mod = new ModuleImpl(name, version);
		mod.setDescription(description);
		XMLModuleSerializer loader = new XMLModuleSerializer(mod);

		if (nodeArtifact != null)
		    loader.loadArtifactFromXML(nodeArtifact);
		if (nodeDependency != null)
		    loader.loadDependenciesFromXML(nodeDependency);
		    
		if (upgradableFrom !=null)
		    {
			Module upgradableMod = XMLModuleSerializer.getModuleFromXML(upgradableFrom);
			mod.setUpgrades(upgradableMod);
		    }
		return mod;

	    } catch (XPathException e)
	    {
		throw new XMLError("Xpath error.", e);
	    }
    }

    private void loadArtifactFromXML(Node nodeArtifact) throws XPathException
    {
	XPath xpath = XPathFactory.newInstance().newXPath();

	String groupId = (String) xpath.evaluate("groupId", nodeArtifact, XPathConstants.STRING);
	String artifactId = (String) xpath.evaluate("artifactId", nodeArtifact, XPathConstants.STRING);
	String version = (String) xpath.evaluate("version", nodeArtifact, XPathConstants.STRING);
	String install = (String) xpath.evaluate("install", nodeArtifact, XPathConstants.STRING);
	String finalName = (String) xpath.evaluate("finalName", nodeArtifact, XPathConstants.STRING);

	Artifact art = new ArtifactImpl(groupId, artifactId, new VersionImpl(version), install, finalName);
	
	this.module.setArtifact(art);
    }

    public static Module readModuleFromJar(File depJar) throws IOException, XMLError
    {
	InputStream is;
	if (depJar.isDirectory()) // is unzipped
	    {
		File definition = new File(depJar, "module.xml");
		is = new FileInputStream(definition);
	    } else
	    {
		JarFile jar = new JarFile(depJar);
		ZipEntry entry = jar.getEntry("module.xml");
		is = jar.getInputStream(entry);
	    }

	Module mod = XMLModuleSerializer.getModuleFromXMLStream(is);
	return mod;
    }

    /**
     * Writes a module and the first level of dependencies
     * 
     * @param thisMod
     * @param writer
     */
    public static void write(Module thisMod, Writer writer)
    {
	try
	    {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element root = buildModuleElementWithDependencies(thisMod, doc);
		doc.appendChild(root);
		dumpDocument(doc, writer);
	    } catch (Exception e)
	    {
		throw new RuntimeException(e);
	    }
    }

    public static Element buildModuleElementWithDependencies(Module thisMod, Document doc)
    {
	Element root = buildModuleElement(thisMod, doc);
	Element depInfo = buildDependenciesElement(thisMod, doc);
	root.appendChild(depInfo);
	Element upgradableFromInfo= buildUpgradableFromElement(thisMod,doc);
	if (upgradableFromInfo!=null)
	    root.appendChild(upgradableFromInfo);
	return root;
    }
    public static Element buildUpgradableFromElement(Module thisMod, Document doc)
    {
	if (thisMod.getUpgradableMod()!=null)
	    {
		Element upgradesInfo = doc.createElement("upgrades");
		Element upgradesModElement = buildModuleElementWithDependencies(thisMod.getUpgradableMod(),doc);
		upgradesInfo.appendChild(upgradesModElement);
		return upgradesInfo;
	    }
	else
	    return null;
    }
    public static Element buildDependenciesElement(Module thisMod, Document doc)
    {
	Element depInfo = doc.createElement("dependencyInfo");
	Element dependenciesElement = doc.createElement("dependencies");
	depInfo.appendChild(dependenciesElement);
	// Iterate the dependencies
	for (Module dep : thisMod.dependsOn())
	    {
		Element depDom = buildModuleElement(dep, doc);
		dependenciesElement.appendChild(depDom);
	    }
	return depInfo;
    }

    public static void dumpDocument(Document doc, Writer writer) throws TransformerFactoryConfigurationError, TransformerConfigurationException,
	    TransformerException
    {
	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	DOMSource source = new DOMSource(doc);
	StreamResult result = new StreamResult(writer);
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	transformer.transform(source, result);
    }

    public static Element buildModuleElement(Module module, Document doc)
    {
	Element root = doc.createElement("module");

	addElementValue(doc, root, "name", module.getName());
	addElementValue(doc, root, "version", module.getVersion().toString());
	Artifact artifact = module.getArtifact();
	if (artifact != null)
	    {
		Element artifactElem = doc.createElement("artifact");
		root.appendChild(artifactElem);
		addElementValue(doc, artifactElem, "groupId", artifact.getGroupId());
		addElementValue(doc, artifactElem, "artifactId", artifact.getArtifactId());
		addElementValue(doc, artifactElem, "version", artifact.getVersion().toString());
		addElementValue(doc, artifactElem, "install", artifact.getInstall());
		if (artifact.getFinalName() != null && !artifact.getFinalName().equals(""))
			addElementValue(doc, artifactElem, "finalName", artifact.getFinalName());
	    }
	return root;
    }

    public static void addElementValue(Document doc, Element root, String tagName, String value)
    {
	Element newElement = doc.createElement(tagName);
	newElement.appendChild(doc.createTextNode(value));
	root.appendChild(newElement);
    }

    /**
     * Write a {@link ModuleDependencyTree} catalog of modules. Each module contains its module-dependencies
     */
    public static void write(ModuleDependencyTree registry, Writer writer)
    {
	try
	    {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		Element root = doc.createElement("modules");
		doc.appendChild(root);

		Collection<Module> mods = registry.getModules();
		for (Module mod : mods)
		    {
			Element module = buildModuleElementWithDependencies(mod, doc);
			root.appendChild(module);
		    }
		dumpDocument(doc, writer);
	    } catch (Exception e)
	    {
		throw new RuntimeException(e);
	    }
    }

    /**
     * Dump registry to standard output of to a file
     * @param registry
     * @param outputfile
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     */
    public static void writeDescriptor(ModuleDependencyTree registry, File outputfile) throws UnsupportedEncodingException, FileNotFoundException
    {
	Writer writer;
	if (outputfile == null) // dump to console
	    {
		writer = new OutputStreamWriter(System.out);
	    } else
	    {
		writer = new OutputStreamWriter(new FileOutputStream(outputfile));// , this.outputEncoding);
	    }
	XMLModuleSerializer.write(registry, writer);
    }
}
