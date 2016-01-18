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
