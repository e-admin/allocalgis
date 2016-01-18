/**
 * ScaleRangeImpl.java
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
package com.geopista.style.sld.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.deegree.graphics.sld.LineSymbolizer;
import org.deegree.graphics.sld.PointSymbolizer;
import org.deegree.graphics.sld.PolygonSymbolizer;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.Symbolizer;

import com.geopista.style.sld.model.ScaleRange;

/**
 * @author enxenio s.l.
 *
 */
public class ScaleRangeImpl implements ScaleRange {
	
	private Double _minScale;
	private Double _maxScale;
	private List _pointList;
	private List _lineList;
	private List _polygonList;
	private List _textList;
	
	public ScaleRangeImpl(Double minScale, Double maxScale) {
		
		_minScale = minScale;
		_maxScale = maxScale;
		_pointList = new ArrayList();
		_lineList = new ArrayList();
		_polygonList = new ArrayList();
		_textList = new ArrayList();
	}
	
	public ScaleRangeImpl(Double minScale, Double maxScale, List pointList, List lineList,
		List polygonList, List textList) {
	
		_minScale = minScale;
		_maxScale = maxScale;
		_pointList = pointList;
		_lineList = lineList;
		_polygonList = polygonList;
		_textList = textList;			
	}
	
	public Double getMinScale() {
		
		return _minScale;
	}
	
	public void setMinScale(Double minScale) {
		
		_minScale = minScale;
	}
	
	public Double getMaxScale() {
		
		return _maxScale;
	}
	
	public void setMaxScale(Double maxScale) {
		
		_maxScale = maxScale;
	}
	
	public List getPointList() {
		
		return _pointList;
	}
	
	public void setPointList(List pointList) {
		
		_pointList = pointList;
	}
	
	public List getLineList() {
		
		return _lineList;
	}
	
	public void setLineList(List lineList) {
		
		_lineList = lineList;
	}

	public List getPolygonList() {
		
		return _polygonList;
	}
	
	public void setPolygonList(List polygonList) {
		
		_polygonList = polygonList;
	}
	
	public List getTextList() {
		
		return _textList;
	}
	
	public void setTextList(List textList) {
		
		_textList = textList;
	}

	public Rule getRule(int position, String symbolizerType) {
		
		if ((symbolizerType.toLowerCase()).equals("point")) {
			return (Rule)_pointList.get(position);
		}
		else if ((symbolizerType.toLowerCase()).equals("line")) {
			return (Rule)_lineList.get(position);
		}
		else if ((symbolizerType.toLowerCase()).equals("polygon")) {
			return (Rule)_polygonList.get(position);
		}
		else {
			return (Rule)_textList.get(position);
		}
	}
	
	public void addRule(Rule rule) {
	
		Symbolizer[] symbolizers = rule.getSymbolizers();	
		Symbolizer symbolizer = symbolizers[0];
		if (symbolizer instanceof PointSymbolizer) {
			_pointList.add(rule);
		}
		else if (symbolizer instanceof LineSymbolizer) {
			_lineList.add(rule);
		}
		else if (symbolizer instanceof PolygonSymbolizer) {
			_polygonList.add(rule);
		}
		else {
			_textList.add(rule);
		}
	}
	
	public void removeRule(int position, String symbolizerType) {
		
		if ((symbolizerType.toLowerCase()).equals("point")) {
			_pointList.remove(position);
		}
		else if ((symbolizerType.toLowerCase()).equals("line")) {
			_lineList.remove(position);
		}
		else if ((symbolizerType.toLowerCase()).equals("polygon")) {
			_polygonList.remove(position);
		}
		else {
			_textList.remove(position);
		}
	}

	public List getRuleList(String symbolizerType) {
		
		if ((symbolizerType.toLowerCase()).equals("point")) {
			return _pointList;
		}
		else if ((symbolizerType.toLowerCase()).equals("line")) {
			return _lineList;
		}
		else if ((symbolizerType.toLowerCase()).equals("polygon")) {
			return _polygonList;
		}
		else {
			return _textList;
		}
	}

	public int compareTo(Object o) {
		
		ScaleRange target = (ScaleRange)o;
		if (getMinScale().compareTo(target.getMinScale()) == 0) {
			return getMaxScale().compareTo(target.getMaxScale()); 		
		}
		else{
			return getMinScale().compareTo(target.getMinScale());
		}
	}
}
