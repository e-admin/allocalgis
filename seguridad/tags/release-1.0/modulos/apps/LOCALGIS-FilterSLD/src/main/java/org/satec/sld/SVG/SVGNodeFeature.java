package org.satec.sld.SVG;

import java.util.List;
import java.util.Map;

import org.deegree.datatypes.QualifiedName;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.feature.schema.FeatureType;
import org.deegree.ogcbase.ElementStep;
import org.deegree.ogcbase.PropertyPath;
import org.deegree.ogcbase.PropertyPathResolvingException;


import com.tinyline.svg.SVGNode;

public class SVGNodeFeature implements Feature {
	
	
	SVGNode node;
	
	public SVGNodeFeature(SVGNode node) {
		this.node= node;
	}

	public void addProperty(FeatureProperty property) {
		// TODO Auto-generated method stub

	}

	public String getAttribute(String nameProp) {
		int pos = this.node.parent.getPosByNameLayertAtt(nameProp);
    	if (pos != -1) {
    		String val = this.node.getValueLayertAtt(pos);
    		return  val;
    	}
    	else return null;
	}

	public Map getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getBoundedBy() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getDefaultGeometryPropertyValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public FeatureProperty getDefaultProperty(QualifiedName name) {
		// TODO Auto-generated method stub
		return null;
	}

	public FeatureProperty getDefaultProperty(PropertyPath path)
			throws PropertyPathResolvingException {
		// TODO Auto-generated method stub
		
		List steps = path.getAllSteps();
		String nameProp;
	    for ( int i = 0; i < steps.size(); i++ ) {
	    	ElementStep  step =(ElementStep) steps.get(i);
	    	nameProp = step.getPropertyName().getLocalName();
	    	int pos = this.node.parent.getPosByNameLayertAtt(nameProp);
	    	if (pos != -1) {
	    		String val = this.node.getValueLayertAtt(pos);
	    		return new SVGNodeFeatureProperty	(nameProp, val);
	    	}
	     
	    }       
		return null;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	public FeatureType getFeatureType() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] getGeometryPropertyValues() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		// TODO Auto-generated method stub
		if (this.node.parent.id !=null)
			return new String (this.node.parent.id.data);
		else return null;
	}

	public QualifiedName getName() {
		// TODO Auto-generated method stub
		return  null;
	}

	public FeatureProperty getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	public FeatureProperty[] getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	public FeatureProperty[] getProperties(QualifiedName name) {
		// TODO Auto-generated method stub
		return null;
	}

	public FeatureProperty[] getProperties(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeProperty(QualifiedName propertyName) {
		// TODO Auto-generated method stub

	}

	public void replaceProperty(FeatureProperty oldProperty,
			FeatureProperty newProperty) {
		// TODO Auto-generated method stub

	}

	public void setAttribute(String name, String value) {
		// TODO Auto-generated method stub

	}

	public void setEnvelopesUpdated() {
		// TODO Auto-generated method stub

	}

	public void setFeatureType(FeatureType ft) {
		// TODO Auto-generated method stub

	}

	public void setId(String fid) {
		// TODO Auto-generated method stub

	}

	public void setProperty(FeatureProperty property, int index) {
		// TODO Auto-generated method stub

	}

}
