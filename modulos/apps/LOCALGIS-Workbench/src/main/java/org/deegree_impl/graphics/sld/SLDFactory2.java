/**
 * SLDFactory2.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
