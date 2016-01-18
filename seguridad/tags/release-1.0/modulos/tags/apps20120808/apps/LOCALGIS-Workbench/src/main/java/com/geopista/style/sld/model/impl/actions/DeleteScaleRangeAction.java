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
