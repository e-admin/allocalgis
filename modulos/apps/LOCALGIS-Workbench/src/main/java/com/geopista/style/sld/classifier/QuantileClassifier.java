/**
 * QuantileClassifier.java
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