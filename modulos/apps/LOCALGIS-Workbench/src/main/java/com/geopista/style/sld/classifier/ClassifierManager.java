/**
 * ClassifierManager.java
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Enxenio, SL
 */
public class ClassifierManager {

	private static HashMap _methods;
	
	static {
		_methods = new HashMap();
		_methods.put("Cuantiles", QuantileClassifier.class);
		_methods.put("Intervalos iguales", EqualIntervalClassifier.class);
		_methods.put("Clases naturales", NaturalBreaks.class);
	}

	public static String[] getMethodNames(Object aValue) {
		
		List result = new ArrayList();
		Set methodNames = _methods.keySet();
		Iterator methodNamesIterator = methodNames.iterator();
		
		while (methodNamesIterator.hasNext()) {
			String aMethod = (String)methodNamesIterator.next();
			Class classifierClass = (Class)_methods.get(aMethod);
			try {
				Classifier aClassifier = (Classifier)classifierClass.newInstance();
				if (aClassifier.canBeApplied(aValue)) {
					result.add(aMethod);
				}
			}
			catch (Exception e) {
			}
		}
		return (String[])result.toArray(new String[]{});
	}
	
	public static Classifier getClassifier(String method) {
		Class classifierClass = (Class)_methods.get(method);
		try {
			return (Classifier)classifierClass.newInstance();
		}
		catch (Exception e) {
			return null;
		}
	}
	
}
