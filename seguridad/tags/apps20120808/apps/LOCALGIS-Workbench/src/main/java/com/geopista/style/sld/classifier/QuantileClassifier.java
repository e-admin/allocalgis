/*
 * Created on 01-oct-2004
 */
package com.geopista.style.sld.classifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Enxenio, SL
 */
public class QuantileClassifier implements Classifier {

	public boolean canBeApplied(Object aValue) {
		return true;
	}

	public Set classify(SortedSet values, int numberOfClasses) {

		List originalList = new ArrayList(values);
		Set resultValues = new TreeSet();
		double elementsInQuantile = originalList.size() / (double) numberOfClasses;
		for (int i = 0; i < numberOfClasses; i++) {
			resultValues.add(originalList.get((int) Math.round(i * elementsInQuantile)));
		}
		return resultValues;
	}
}