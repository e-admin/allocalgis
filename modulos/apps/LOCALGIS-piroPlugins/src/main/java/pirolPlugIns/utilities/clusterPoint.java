/**
 * clusterPoint.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 09.12.2004
 *
  * CVS header information:
 *  $RCSfile: clusterPoint.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/clusterPoint.java,v $
 */
package pirolPlugIns.utilities;


/**
 * 
 * Point object, specialized to be used in cluster center calculations: Can
 * hold information on the ratio (weight) that defines how strong this point to a cluster center.
 *
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 *
 */
public class clusterPoint extends punkt {
	
	protected double[] dependencies;
	protected int numCenteres = 0;
	
	protected int indexOfMaxDependency = 0;
	protected boolean iomdValid = false;

	public clusterPoint(double[] coords, int index) {
		super(coords, index);
	}
	
	public void setupDependencies( int num ){
		//System.out.println( this.getIndex() + " -> setupDependencies("+num+")");
		this.dependencies = new double[num];
		this.numCenteres = num;
		
		for ( int i=0; i<num; i++ ){
			this.dependencies[i] = 0;
		}
		
		iomdValid = false;
	}
	

	public double getDependency( int nr ) throws Exception {
		if (nr < this.dependencies.length){
			return dependencies[nr];
		}
		throw new Exception("invalid dependency: "+nr);
	}

	public void setDependency(double dependency, int nr) throws Exception {
		if (nr < this.dependencies.length){
			this.dependencies[nr] = dependency;
			iomdValid = false;
		} else {
		    throw new Exception("invalid dependency: "+nr);
		}
	}
	
	public int getIndexOfMaxDependency() throws Exception{
		double maxDep = -1.0;
		
		if (!this.iomdValid){
			for (int i=0; i<this.numCenteres; i++){
				if (this.getDependency(i)>maxDep){
					this.indexOfMaxDependency = i;
					maxDep = this.getDependency(i);
				}
			}
			iomdValid = true;
		}
		
		return this.indexOfMaxDependency;
	}

	public int getNumCenteres() {
		return numCenteres;
	}

	public void setNumCenteres(int numCenteres) {
		this.setupDependencies(numCenteres);
	}
	
	public clusterPoint(double[] coords, int index, ScaleChanger scaler) {
		super(coords, index, scaler);
	}

}
