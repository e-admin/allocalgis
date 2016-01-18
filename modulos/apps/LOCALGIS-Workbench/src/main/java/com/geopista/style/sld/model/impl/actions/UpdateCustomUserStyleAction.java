/**
 * UpdateCustomUserStyleAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 27-jul-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deegree.graphics.sld.FeatureTypeStyle;
import org.deegree.graphics.sld.Rule;
import org.deegree.graphics.sld.UserStyle;
import org.deegree_impl.graphics.sld.StyleFactory;

import com.geopista.style.sld.model.ScaleRange;

/**
 * @author enxenio s.l.
 *
 */
public class UpdateCustomUserStyleAction {

	private String _styleName;
	private String _styleTitle;
	private String _styleAbstract;
	private Boolean _isDefault;
	private List _ftsList;
	private List _scaleRangeList;
	private Integer _insert;
	private UserStyle _userStyle;
	private List _userStyleList;
	
	public UpdateCustomUserStyleAction(String styleName,String styleTitle,String styleAbstract,
		Boolean isDefault,List ftsList,List scaleRangeList, Integer insert,UserStyle userStyle, 
		List userStyleList) {
			
		_styleName = styleName;
		_styleTitle = styleTitle;
		_styleAbstract = styleAbstract;
		_isDefault = isDefault;
		_ftsList = ftsList;
		_scaleRangeList = scaleRangeList;
		_insert = insert;
		_userStyle = userStyle;
		_userStyleList = userStyleList;
	}
	
	public Object doExecute() {
		
		if (_styleName != null) {
			_userStyle.setName(_styleName);
		}
		if (_styleTitle != null) {
			_userStyle.setTitle(_styleTitle);
		}
		if (_styleAbstract != null) {
			_userStyle.setAbstract(_styleAbstract);
		}
		if (_isDefault != null) {
			_userStyle.setDefault(_isDefault.booleanValue());
		}
		if (_ftsList != null) {
			FeatureTypeStyle[] ftsArray = createFtsArray(_ftsList,_scaleRangeList);
			_userStyle.setFeatureTypeStyles(ftsArray);
		}
		if (_insert.intValue() == 1) {
			_userStyleList.add(_userStyle);
		}
		return _userStyle;
	}
	
	private FeatureTypeStyle[] createFtsArray(List ftsList, List scaleRangeList) {
		
		FeatureTypeStyle[] ftsArray = new FeatureTypeStyle[1];
		FeatureTypeStyle fts = null;
		if (!ftsList.isEmpty()) {
			fts = (FeatureTypeStyle)ftsList.get(0);
		}
		else {
			Rule[] ruleArray = null;
			fts = StyleFactory.createFeatureTypeStyle(ruleArray);
		}
		List ruleList = new ArrayList();
		Iterator scaleRangeIterator = scaleRangeList.iterator();
		while (scaleRangeIterator.hasNext()) {
			ScaleRange scaleRange = (ScaleRange)scaleRangeIterator.next();
			List polygonList = scaleRange.getPolygonList();
			addRules(polygonList,ruleList,scaleRange);
			List lineList = scaleRange.getLineList();
			addRules(lineList,ruleList,scaleRange);
			List pointList = scaleRange.getPointList();
			addRules(pointList,ruleList,scaleRange);
			List textList = scaleRange.getTextList();
			addRules(textList,ruleList,scaleRange);			
		}
		Rule[] ruleArray = {};
		ruleArray = (Rule[])ruleList.toArray(ruleArray);
		fts.setRules(ruleArray);
		ftsArray[0] = fts;		
		return ftsArray;
	}
	
	private List addRules(List startList, List endList, ScaleRange scaleRange) {
		
		if (startList.isEmpty()) {
			return endList;
		}
		else {
			Iterator listIterator = startList.iterator();
			while (listIterator.hasNext()) {
				Rule rule = (Rule)listIterator.next();
				rule.setMinScaleDenominator(scaleRange.getMinScale().doubleValue());
				rule.setMaxScaleDenominator(scaleRange.getMaxScale().doubleValue());
				endList.add(rule);
			}
			return endList;
		}
	}
}
