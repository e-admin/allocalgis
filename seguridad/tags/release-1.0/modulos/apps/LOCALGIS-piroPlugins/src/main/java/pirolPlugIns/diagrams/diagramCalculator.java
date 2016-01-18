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
