package com.localgis.server;

import javax.servlet.ServletContext;

import com.geopista.global.WebAppConstants;
import com.thoughtworks.xstream.XStream;

public abstract class ContextShared {
	
	public Object getSharedAttribute(ServletContext servletContext, String attributeName){
		return decode(((String)servletContext.getContext("/" + WebAppConstants.PRINCIPAL_WEBAPP_NAME).getAttribute(attributeName)));
	}
	
	public void setSharedAttribute(ServletContext servletContext, String attributeName, Object attributeValue){
		servletContext.getContext("/" + WebAppConstants.PRINCIPAL_WEBAPP_NAME).setAttribute(attributeName, encode(attributeValue));
	}
	
	private String encode(Object attributeValue){		
		return getXStreamSerializer().toXML(attributeValue); 
	}
	
	private Object decode(String xml){
		return getXStreamSerializer().fromXML(xml);
	}
	
//	public Object getSharedAttribute(ServletContext servletContext, String attributeName){
//		return decode(((byte [])servletContext.getContext("/" + WebAppConstants.ADMCAR_WEBAPP_NAME).getAttribute(attributeName)));
//	}
//	
//	public void setSharedAttribute(ServletContext servletContext, String attributeName, Object attributeValue){
//		servletContext.getContext("/" + WebAppConstants.ADMCAR_WEBAPP_NAME).setAttribute(attributeName, encode(attributeValue));
//	}
//	
//	private byte [] encode(Object attributeValue){
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		getXStreamSerializer().toXML(attributeValue, baos);			
//		return baos.toByteArray();
//	}
//	
//	private Object decode(byte [] attributeValueByteArray){
//		ByteArrayInputStream bais = new ByteArrayInputStream(attributeValueByteArray);
//		Object o = getXStreamSerializer().fromXML(bais);
//		return o;
//	}
	
	public abstract XStream getXStreamSerializer();
		
}
