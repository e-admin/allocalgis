/*
 * Created on 30-jun-2004
 *
 */
package org.deegree_impl.graphics.sld;

import org.deegree.graphics.sld.Extent;
import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.UserStyle;
import org.deegree.xml.XMLParsingException;
import org.deegree.xml.XMLTools;
import org.w3c.dom.Element;

/**
 * @author enxenio s.l.
 *
 */
public class SLDFactory2 {
	
	private static String sldNS = "http://www.opengis.net/sld";
	
	public SLDFactory2() {}
	
	public Extent createExtent(Element element) {
		
		String name = null;
		String value = null;
		try {
			name = XMLTools.getRequiredStringValue("Name",sldNS,element);
			value = XMLTools.getRequiredStringValue("Value",sldNS,element);
		} catch(XMLParsingException e) {
			e.printStackTrace();
		}
		return new Extent_Impl(name,value);
	}
	
	public UserStyle createUserStyle(String name,String title,String styleAbstract,boolean isDefault, FeatureTypeStyle[] styles) {
		return new UserStyle_Impl(name,title,styleAbstract,isDefault,styles);
	}
	
	public Extent createExtent(String name,String value) {
		return new Extent_Impl(name,value);
	}

}
