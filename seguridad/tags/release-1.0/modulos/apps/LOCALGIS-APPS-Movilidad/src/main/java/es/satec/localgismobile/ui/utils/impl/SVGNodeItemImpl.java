package es.satec.localgismobile.ui.utils.impl;

import org.apache.log4j.Logger;

import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.fw.Global;

public class SVGNodeItemImpl extends ItemNode {

	SVGNode node;
	private static Logger logger = Global.getLoggerFor(SVGNodeItemImpl.class);
	
	
	public SVGNodeItemImpl(String name) {
		super(name);
	}
	public SVGNodeItemImpl (SVGNode node, String nameAttr) {
	
		super(nameAttr);
		this.node = node;
	}
	
	public void setContent(String value) {
	 
		try {
			int pos = node.parent.getPosByNameLayertAtt(name);
			node.setExtendedAttributeAndRecordEvent(pos, value);
		} catch (Exception e) {
		 
			logger.error(e);
		}
	 

	}

}
