package com.satec.gpx.utils;


public class GpxNamespacePrefixMapper extends
		com.sun.xml.bind.marshaller.NamespacePrefixMapper {

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		if(namespaceUri.equals("http://www.w3.org/2001/XMLSchema-instance")) {
			return suggestion;
		}
		
		if(namespaceUri.equals("http://www.topografix.com/GPX/1/1")) {
			return Constants.XML_PREFIX;
		}
		return "";
	}

}
