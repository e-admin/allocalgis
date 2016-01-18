/*
 * Created on 01-oct-2004
 */
package com.geopista.style.sld.classifier;

import java.util.Set;
import java.util.SortedSet;

/**
 * @author Enxenio, SL
 */
public interface Classifier {
	
	public boolean canBeApplied(Object aValue);
	public Set classify(SortedSet values, int numberOfClasses);
}
