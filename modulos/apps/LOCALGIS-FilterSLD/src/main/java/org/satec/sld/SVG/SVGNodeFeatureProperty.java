/**
 * SVGNodeFeatureProperty.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
