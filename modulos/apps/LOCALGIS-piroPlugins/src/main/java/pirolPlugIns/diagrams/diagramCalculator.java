/**
 * diagramCalculator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 09.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package pirolPlugIns.diagrams;

import java.util.HashMap;

import com.vividsolutions.jump.feature.Feature;


public abstract class diagramCalculator extends Thread {

		protected boolean done = false;
		protected Feature[] features;
		protected HashMap numbersToClasses = new HashMap();
		
		protected int numAttr = Integer.MIN_VALUE, maxNumAttrPerClass=0;
		
		public diagramCalculator( Feature[] features ){
			this.features = features;
		}
		
		
		public int getNumFeatures(){
			return this.features.length;
		}
		
		public int getMaxNumAttrPerClass() {
			return maxNumAttrPerClass;
		}

		public boolean isDone() {
			return done;
		}

		public int getNumClasses() {
			return this.numbersToClasses.size();
		}

		public HashMap getNumbersToClasses() {
			if (this.done)
				return numbersToClasses;
			return null;
		}
		
		
		protected abstract void calculateValues();
		
	    public void run() {
	        this.calculateValues();
	    }
}
