/*
 * Created on 26-jul-2004
 *
 */
package com.geopista.style.sld.model.impl.actions;

import com.geopista.style.sld.model.ScaleRange;
import com.geopista.style.sld.model.impl.ScaleRangeImpl;

/**
 * @author enxenio s.l.
 *
 */
public class CreateDefaultScaleRangeAction {

	public CreateDefaultScaleRangeAction() {}
	
	public Object doExecute() {
		
		ScaleRange scaleRange = new ScaleRangeImpl(null,null);
		return scaleRange;
	}
}
