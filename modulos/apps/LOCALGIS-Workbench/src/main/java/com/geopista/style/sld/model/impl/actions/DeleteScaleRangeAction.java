/**
 * DeleteScaleRangeAction.java
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

import java.util.List;

import com.geopista.style.sld.model.ScaleRange;
/**
 * @author enxenio s.l.
 *
 */
public class DeleteScaleRangeAction {

	private int _position;
	private List _scaleRangeList;
	
	public DeleteScaleRangeAction(int position, List scaleRangeList) {
		
		_position = position;
		_scaleRangeList = scaleRangeList;
	}
	
	public void doExecute() {
		
		int size = _scaleRangeList.size();
		ScaleRange scaleRange = null;
		if ((_position>=0)&&(_position<size)) {
			scaleRange = (ScaleRange) _scaleRangeList.get(_position);
			_scaleRangeList.remove(_position);
		}

	}
}
