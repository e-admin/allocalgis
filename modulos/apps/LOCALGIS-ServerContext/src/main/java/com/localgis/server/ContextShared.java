/**
 * ContextShared.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.server;

import javax.servlet.ServletContext;

import com.geopista.global.WebAppConstants;
import com.thoughtworks.xstream.XStream;

public abstract class ContextShared {
	
	
	public Object getSharedAttribute(ServletContext servletContext, String contexto,String attributeName){
		ServletContext context=servletContext.getContext(contexto);
		if (context!=null){
			Object attribute=context.getAttribute(attributeName);
			if (attribute!=null)
				return decode((String)attribute);				
		}
		return null;
	}
	public Object getSharedAttribute(ServletContext servletContext, String attributeName){
		ServletContext context=servletContext.getContext(WebAppConstants.PRINCIPAL_WEBAPP_NAME);
		if (context!=null){
			Object attribute=context.getAttribute(attributeName);
			if (attribute!=null)
				return decode((String)attribute);				
		}
		return null;
		
		//return decode(((String)servletContext.getContext(WebAppConstants.PRINCIPAL_WEBAPP_NAME).getAttribute(attributeName)));
	}
	
	public void setSharedAttribute(ServletContext servletContext, String attributeName, Object attributeValue){
		servletContext.getContext(WebAppConstants.PRINCIPAL_WEBAPP_NAME).setAttribute(attributeName, encode(attributeValue));
	}
	
	public void setSharedAttribute(ServletContext servletContext, String context,String attributeName, Object attributeValue){
		servletContext.getContext(context).setAttribute(attributeName, encode(attributeValue));
	}
	
	
	private String encode(Object attributeValue){		
		return getXStreamSerializer().toXML(attributeValue); 
	}
	
	private Object decode(String xml){
		if (xml!=null)
			return getXStreamSerializer().fromXML(xml);
		else
			return null;
	}
	
	public abstract XStream getXStreamSerializer();
		
}
