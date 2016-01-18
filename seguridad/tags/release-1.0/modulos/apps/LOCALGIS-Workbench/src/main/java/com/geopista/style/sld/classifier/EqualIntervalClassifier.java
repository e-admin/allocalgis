/*
 * Created on 01-oct-2004
 */
package com.geopista.style.sld.classifier;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Enxenio, SL
 */
public class EqualIntervalClassifier implements Classifier {

	public boolean canBeApplied(Object aValue) {
		try {
			new Double((aValue.toString()));
			return true;
		}
		catch (NumberFormatException e) {
			return false; 
		}
	}
	
	public Set classify(SortedSet values, int numberOfClasses) {
		double minValue = Double.parseDouble(values.first().toString());
		double maxValue = Double.parseDouble(values.last().toString());
		
		if (minValue > maxValue) {
			double swap = minValue;
			minValue = maxValue;
			maxValue = swap;  
		}
		double intervalSize = (maxValue - minValue) / (double) numberOfClasses;
		Set resultValues = new TreeSet();
		for (int i = 0; i < numberOfClasses; i++) {
			resultValues.add(new Double(minValue+ (i * intervalSize)));
		}
		return resultValues;
	}
}
