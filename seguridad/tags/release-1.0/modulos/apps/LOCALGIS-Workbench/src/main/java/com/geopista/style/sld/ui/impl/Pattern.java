/*
 * Created on 29-jul-2004
 */
package com.geopista.style.sld.ui.impl;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Enxenio S.L.
 */
public class Pattern {
		
	public static List createPatterns() {
		List result = new ArrayList(); 
		result.add(new Pattern(new float[]{}));
		result.add(new Pattern(new float[]{2,2}));
		result.add(new Pattern(new float[]{4,4}));
		result.add(new Pattern(new float[]{6,6}));
		result.add(new Pattern(new float[]{8,8}));
		result.add(new Pattern(new float[]{10,10}));
		result.add(new Pattern(new float[]{10,10,2,10}));
		return result;
	}

	public static String toString(float[] dashArray) {
		StringBuffer result;
			
		result = new StringBuffer();
		if (dashArray.length > 0) {
			result.append(dashArray[0]);
			for (int i = 1; i<dashArray.length; i++) {
				result.append("-");
				result.append(dashArray[i]); 
			}
		}
		else {
			result.append("Continuo");
		}
		return result.toString();
	}	
	
	public Pattern(float[] dashArray) {
		_dashArray = dashArray;
	}
		
	public float[] getDashArray() {
		return _dashArray;
	}
		
	public String toString() {
		return toString(_dashArray);
	}

	public Stroke getStroke() {
		if (_dashArray.length > 0) {
			return new BasicStroke( 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, _dashArray, 0);
		}
		else {
			return new BasicStroke( 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		}
	}
			
	private float[] _dashArray;
}
