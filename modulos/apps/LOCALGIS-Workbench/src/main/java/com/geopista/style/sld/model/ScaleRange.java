/**
 * ScaleRange.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 26-jul-2004
 *
 */
package com.geopista.style.sld.model;

import java.io.Serializable;
import java.util.List;

import org.deegree.graphics.sld.Rule;

/**
 * @author enxenio s.l.
 *
 */
public interface ScaleRange extends Serializable, Comparable {

	public Double getMinScale();
	
	public void setMinScale(Double minScale);
	
	public Double getMaxScale();
	
	public void setMaxScale(Double maxScale);
	
	public List getPointList();
	
	public void setPointList(List pointList);
	
	public List getLineList();
	
	public void setLineList(List lineList);

	public List getPolygonList();
	
	public void setPolygonList(List polygonList);

	public List getTextList();
	
	public void setTextList(List textList);

	public Rule getRule(int position, String symbolizerType);
	
	public void addRule(Rule rule);
	
	public void removeRule(int position, String symbolizerType);

	public List getRuleList(String symbolizerType);
	
	int compareTo(Object o);
}
