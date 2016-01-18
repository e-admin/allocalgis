/*
 * Created on 07.12.2004
 *
 * CVS header information:
 *  $RCSfile: idComparableFeature.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/idComparableFeature.java,v $
 */
package pirolPlugIns.utilities;

import com.vividsolutions.jump.feature.Feature;

/**
 * @author orahn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class idComparableFeature implements Comparable {
	
	//private FeatureUtil.IDComparator idComparator;
	private Feature feature;

	public idComparableFeature(Feature feat) {
		//idComparator = new FeatureUtil.IDComparator();
		this.feature = feat;
	}
	
	public int getID(){
		return this.feature.getID();
	}

	public int compareTo(Object o) {
		idComparableFeature obj = (idComparableFeature)o;
		if (this.feature.getID() < obj.getID())
			return -1;
		else if (this.feature.getID() > obj.getID())
			return 1;
		else
			return 0;
	}
	
	public Feature getFeature() {
		return feature;
	}
}
