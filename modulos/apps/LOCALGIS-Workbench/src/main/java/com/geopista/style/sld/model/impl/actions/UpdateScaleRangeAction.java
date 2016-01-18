/**
 * UpdateScaleRangeAction.java
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
package com.geopista.style.sld.model.impl.actions;

import java.util.Collections;
import java.util.List;

import com.geopista.style.sld.model.ScaleRange;
/**
 * @author enxenio s.l.
 *
 */
public class UpdateScaleRangeAction {

	private Double _minScale;
	private Double _maxScale;
	private Integer _insert;
	private ScaleRange _scaleRange;
	private List _scaleRangeList;
	
	public UpdateScaleRangeAction(Double minScale,Double maxScale,Integer insert,
		 ScaleRange scaleRange, List scaleRangeList) {
		
		_minScale = minScale;
		_maxScale = maxScale;
		_insert = insert;
		_scaleRange = scaleRange;
		_scaleRangeList = scaleRangeList;	
	}
	
	public Object doExecute() {
		
		if (_minScale != null) {
			_scaleRange.setMinScale(_minScale);
		}
		else {
			_scaleRange.setMinScale(new Double(0));
		}
		if (_maxScale != null) {
			_scaleRange.setMaxScale(_maxScale);
		}
		else {
			_scaleRange.setMaxScale(new Double(9E99));
		}
		if (_insert.intValue() == 1) {
			_scaleRangeList.add(_scaleRange);
		}
		Collections.sort(_scaleRangeList);
		return _scaleRange;

	}
}
