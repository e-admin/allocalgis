/**
 * TestParser.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg;


import org.jdom.input.SAXBuilder;
import java.io.File;
import org.jdom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import org.jdom.input.DOMBuilder;

	
public class TestParser{
	
	public TestParser(){
		//testSAX();
		testDOM();
	}
	
	public void testSAX(){
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		
		//sXMLBody=sXMLBody.replaceAll("xmlns", "wfs-mne");
		
		
		//StringReader sr = new StringReader(sXMLBody);
		
		try {
		
			//String xXMLBody=readFile(new File("capabilities.xml"));
			//StringReader sr = new StringReader(sXMLBody);
			
			builder.setFeature("http://xml.org/sax/features/namespaces", false);			
			//builder.setFeature("http://apache.org/xml/features/validation/schema",false);
			//builder.setFeature("http://xml.org/sax/features/namespace-prefixes",false);
			//builder.setFeature("http://xml.org/sax/features/allow-java-encodings",true);
			//builder.setFeature("http://xml.org/sax/features/external-parameter-entities",false);
			//builder.setFeature("http://xml.org/sax/features/use-entity-resolver2",false);
			doc = builder.build(new File("capabilities.xml"));
			//doc = builder.build(xXMLBody);
		}
		catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	public String readFile(File fichero) throws Exception{
	
		BufferedReader br = new BufferedReader(new FileReader(fichero));
		StringBuffer sb=new StringBuffer();
		String buffer;
		while ((buffer = br.readLine()) != null) {
			sb.append(buffer);
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public void testDOM(){
	
		org.w3c.dom.Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc=db.parse(new File("capabilities.xml").toURI().toString());
			
			//DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
			//domfactory.setNamespaceAware(true);
			//DocumentBuilder dombuilder = domfactory.newDocumentBuilder();
			DOMBuilder dombuilder = new DOMBuilder();
			org.jdom.Document doc2=dombuilder.build(doc);
 
		}
		catch (Exception e){
			e.printStackTrace();
		}		
		System.out.println("TODO OK");

	}

	public static void main(String args[]){
		new TestParser();
	}

}