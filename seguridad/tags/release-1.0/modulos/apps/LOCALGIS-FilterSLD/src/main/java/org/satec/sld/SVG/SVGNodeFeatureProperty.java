package org.satec.sld.SVG;

import org.deegree.datatypes.QualifiedName;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureProperty;

public class SVGNodeFeatureProperty implements FeatureProperty {
	
	
	QualifiedName qName;
	private Object value;
	
	
	public SVGNodeFeatureProperty(String name, Object value) {
		qName = new QualifiedName(name);
		this.value = value;
	}

	public QualifiedName getName() {
		// TODO Auto-generated method stub
		return qName;
	}

	public Feature getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	public Object getValue(Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setValue(Object value) {
		// TODO Auto-generated method stub
		this.value = value;
	}

}
