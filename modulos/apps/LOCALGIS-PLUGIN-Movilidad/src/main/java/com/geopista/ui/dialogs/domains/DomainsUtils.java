/**
 * DomainsUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.domains;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.geopista.feature.Attribute;
import com.geopista.feature.Column;
import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.Table;
import com.geopista.ui.dialogs.domains.eiel.EIELHashAttDomain;
import com.geopista.ui.dialogs.domains.licencias.LicenseHashAttDomain;
import com.geopista.ui.dialogs.global.Constants;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.Java2XML;

public class DomainsUtils {
	
	/**
	 * Genera un fichero sch para el skeleton pasado como entrada en el idioma indicado
	 * @param skeletonInput
	 * @param sLocale
	 * @param schFile
	 * @throws Exception 
	 */
	public void generateSCHFiles(InputStream skeletonInput, String sLocale, File schFile, String type) throws Exception{
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(skeletonInput);
		Element rootElement = doc.getRootElement();	
		List children = rootElement.getChildren();
		Element elem = null;
		List items = null;
		List itemLists = null;
		List<String> attListNames = new ArrayList<String>();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			elem = (Element) iterator.next();
			items = elem.getChildren(Constants.TAG_ITEM);
			attListNames.addAll(getAttList(items));
			itemLists = elem.getChildren(Constants.TAG_ITEMLIST);
			attListNames.addAll(getAttList(itemLists));
		}	

		if(attListNames.size()>0)
			generateSCHFile(attListNames, sLocale, schFile, type);		
	}
	
	/**
	 * Obtiene una lista de nombres de atributos para sacar posteriormente sus dominios
	 * @param items
	 * @return
	 */
	private static List<String> getAttList(List items) {
		Element elem = null;
		List<String> listAtt = new ArrayList<String>();
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			elem = (Element) iterator.next();
			org.jdom.Attribute attName = elem.getAttribute(Constants.ATT_NAME);
			if(attName!=null){
				String attNameValue = attName.getValue();
				if(attNameValue!=null && attNameValue.length()>0){
					listAtt.add(attNameValue);
				}
			}
		}
		return listAtt;
	}

	/**
	 * Devuelve un string que corresponde al fichero SCH para los atributos pasados como parámetro
	 * @param attListNames
	 * @param sLocale
	 * @return
	 * @throws Exception
	 */
	public void generateSCHFile(List<String> attListNames, String sLocale, File schFile, String type) throws Exception{
		List<Attribute> listaAtr = getAttList(attListNames, sLocale, type);
		
		GeopistaSchema geoSchema = new GeopistaSchema();
		Attribute attribute = null;
		for (int i = 0; i < listaAtr.size(); i++) {
			attribute = listaAtr.get(i);
			geoSchema.addAttribute(attribute);
		}
		
		java2xmlschema(geoSchema, schFile);
	}
	
	/**
	 * Retorna una lista de atributos con sus dominios asociados
	 * @param attListNames
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private static List<Attribute> getAttList(List<String> attListNames, String sLocale, String type) throws Exception{
		List<Attribute> listaAtr = new ArrayList<Attribute>();
		
		Domain domain = null;
		String attStr = null;
		Attribute att = null;
		for (int i = 0; i < attListNames.size(); i++) {
			attStr = attListNames.get(i);
			//obtención del dominio asociado al atributo
				if(type.equals(Constants.LICENCIAS))
					domain = LicenseHashAttDomain.getEstructuraAtt(attStr, sLocale);
				else if(type.equals(Constants.EIEL))
					domain = EIELHashAttDomain.getEstructuraAtt(attStr, sLocale);
			//creación del atributo en si
			att = new Attribute();
			att.setName(attStr);
			if(attStr.toLowerCase().contains("fecha")){
				att.setType(AttributeType.DATE.toString());
			}
			else {
				att.setType(AttributeType.STRING.toString());
			}
			Column column = new Column(attStr, attStr, domain);
			column.setTable(new Table(attStr, attStr));
			att.setColumn(column);
			listaAtr.add(att);
		}		
		
		return listaAtr;
	}
	
	private void java2xmlschema(GeopistaSchema geoSchema, File schFile) throws Exception {
        StringWriter stringWriterSch = new StringWriter();
        
        try {
	        Java2XML converter = new Java2XML();
		    converter.write(geoSchema, "GeopistaSchema", stringWriterSch,"UTF-8");
		    FileUtil.setContentsUTF8(schFile.getAbsolutePath(), stringWriterSch.toString());
        }
	    finally
	    {
	    	stringWriterSch.flush();
	        stringWriterSch.close();
	    }
	}
	
	public static void main(String[] args) {
		try {
			List<String> attListNames = new ArrayList<String>();
			attListNames.add("idTipoObra");
			attListNames.add("idEstado");
			attListNames.add("tipotramitacion");
			attListNames.add("fechaAperturaString");
			attListNames.add("fechaResolucion");
			attListNames.add("fechaString");
			attListNames.add("fechaLimiteObraString");
			File fSch = new File("C:\\licencias.sch");
			String sLocale = "es_ES";
			DomainsUtils domUtils = new DomainsUtils();
			domUtils.generateSCHFile(attListNames, sLocale, fSch, Constants.LICENCIAS);
			System.out.println("Fichero SCH generado en: " + fSch.getAbsolutePath());
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
