/**
 * EqualIntervalClassifier.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
