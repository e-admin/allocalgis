/**
 * punkt.java
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
 *  $RCSfile: punkt.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:54 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/punkt.java,v $
 */
package pirolPlugIns.utilities;

import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;


/** 
 * Base class for objects that describe an n-dimensional point.
 * There are derived classes e.g. to suit the requirements of a
 * triangulation or an interpolation.
 * Offers methods e.g. to determine distances between different
 * punkt objects or to mark and unmark punkt objects.
 * 
 * punkt objects implement Sortable and have a choseable natural
 * order (implement Comparable), so they can be sorted with the tools
 * from the Java Collection Framework.
 * 
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 * 
 * @see pirolPlugIns.utilities.Kante
 * @see pirolPlugIns.utilities.Data2LayerConnector
 */
public class punkt extends Sortable {
	protected double[] coordinates = null;
	protected int dimension = 0, index = -1;
	
	public static final punkt NULLPUNKT = new punkt(new double[]{0.0,0.0,0.0});
	
	protected ScaleChanger scaler = null;
	
	protected boolean scaled = false;
	
	protected boolean marked = false;
	
	protected static PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);
	
	
	/**
	 * For classLoaders
	 */
	public punkt(){
		super();
		}  
	
	public punkt( double[] coords ){
		this.coordinates = coords;
		this.index = -1;
		
		this.dimension = coords.length;
	}
	
	public punkt( double[] coords, int index){
		this.coordinates = coords;
		this.index = index;
		
		this.dimension = coords.length;
	}
	
	public punkt( double[] coords, int index, ScaleChanger scaler, boolean prescaled){
		this.coordinates = coords;
		this.index = index;
		this.scaler = scaler;
		
		this.dimension = coords.length;
		this.scaled = prescaled;
	}
	
	public punkt( double[] coords, int index, ScaleChanger scaler){
		this.coordinates = coords;
		this.index = index;
		this.scaler = scaler;
		
		this.dimension = coords.length;
	}
	
	/**
	 * creates a punkt object that represents a vector (x,y(,z)) that shows the direction
	 * that leads from point "from" to point "to". 
	 *@param from source points of the vector
	 *@param to destination point of the vector
	 *@return a vector from from to to ;-)
	 */
	public static punkt createVector(punkt from, punkt to){
	    int dim = Math.min(from.getDimension(), to.getDimension());
	    double[] vectorComponents = new double[dim];
	    
	    for (int i=0; i<dim; i++){
	        try {
                vectorComponents[i] = to.getCoordinate(i) - from.getCoordinate(i);
            } catch (Exception e) {
                logger.printError(e.getMessage());
            }
	    }
	    
	    return new punkt(vectorComponents);
	}
	
	/**
	 * Method to create a deep copy of the given punkt object, that doesn't share any references with the original
	 *@param pkt the punkt object to clone
	 *@return copy of the given punkt object
	 *@since 1.15
	 */
	public static punkt clone(punkt pkt){
	    double[] newCoords = new double[pkt.getDimension()];
	    
	    for (int i=0; i<pkt.getDimension(); i++){
	        try {
                newCoords[i] = pkt.getCoordinate(i);
            } catch (Exception e) {
                logger.printError(e.getMessage());
            }
	    }
	    punkt cloned = new punkt(newCoords, pkt.index);
	    cloned.setMarked(pkt.isMarked());
	    cloned.setScaler(pkt.scaler);
	    cloned.setSortFor(pkt.sortFor);
	    
	    return cloned;
	}
	
	/**
	 * Method to create a deep copy of the calling punkt object, that doesn't share any references with the original
	 *@return copy of the punkt object
	 *@since 1.15
	 */
	public punkt clonePunkt(){
	    return punkt.clone(this);
	}

	/**
	 * check if <code>marked</code> was set or not 
	 *@return true, if <code>marked</code> is true
	 */
    public boolean isMarked() {
        return marked;
    }
    /**
     * set boolean flag of the object, to e.g. mark if this point was already passed or whatever.
     *@param marked boolean value to set the internal flag to
     */
    public void setMarked(boolean marked) {
        this.marked = marked;
    }
    public boolean liegtAuf(punkt p) {
        try {
            if (this == p){
                return true;
            } else if (p.getX()==this.getX() && p.getY() == this.getY()) {
	            return true;
	        }
        } catch (Exception e) {
            return false;            
        }
        return false;
    }
    
    public boolean equals(Object obj) {
        punkt p;
        try {
            p = (punkt)obj;
        } catch (Exception e ){
            return false;
        }
        
        try {
            if (this == p){
                return true;
            } else if (p.getIndex() == this.getIndex() && !(this.getIndex()<0)){
	            return true;
	        } else if (Math.min(p.getDimension(), this.getDimension())>=3 && p.getX()==this.getX() && p.getY() == this.getY() && p.getZ() == this.getZ()) {
	            return true;
	        } else if (p.getX()==this.getX() && p.getY() == this.getY()) {
	            return true;
	        }
        } catch (Exception e) {}
        return false;
    }
    
    
    public int getSortFor() {
        return sortFor;
    }
    public void setSortFor(int sortFor) {
        this.sortFor = sortFor;
    }
	public void setScaler(ScaleChanger scaler) {
		this.scaler = scaler;
	}
	
	public void scale( ) throws Exception{
		if ( scaled || this.scaler == null ) return;
		for ( int i=0; i<this.dimension; i++ ){
			this.setCoordinate(this.scaler.scale(this.getCoordinate(i),i),i);
		}
		scaled = true;
	}
	
	public void unScale( ) throws Exception{
		if ( !scaled || this.scaler == null ) return;
		for ( int i=0; i<this.dimension; i++ ){
			this.setCoordinate(this.scaler.unScale(this.getCoordinate(i),i),i);
		}
		scaled = false;
	}
	
	public double[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
		this.dimension = coordinates.length;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getDimension() {
		return dimension;
	}
	
	/**
	 * get the <code>nr</code>-th coordinate. This method is used within getX(), getY(), getZ().
	 *@return the <code>nr</code>-th coordinate
	 *@throws Exception if the point has less coordniates than <code>nr</code> or if <code>nr</code> is less than zero
	 */
	public double getCoordinate( int nr ) throws Exception{
		if (dimension > nr){
			return this.coordinates[nr];
		} 
		throw new Exception("invalid dimension: "+nr + " (vs. " + this.dimension +")");
	}
	
	/**
	 * set the <code>nr</code>-th coordinate. This method is used within setX(), setY(), setZ().
	 *@param newCoord new Coordinate
	 *@param nr number of coordinate to set
	 */
	public void setCoordinate( double newCoord, int nr ){
		if (dimension > nr){
			this.coordinates[nr] = newCoord;
		} 
	}
	
	/**
	 * get 0-th coordinate
	 *@return 0-th coordinate
	 *@throws Exception
	 */
	public double getX() throws Exception{
		return this.getCoordinate(0);			
	}
	/**
	 * get 1-st coordinate
	 *@return 1-st coordinate
	 *@throws Exception
	 */
	public double getY() throws Exception{
		return this.getCoordinate(1);			
	}
	/**
	 * get 2-nd coordinate
	 *@return 2-nd coordinate
	 *@throws Exception
	 */
	public double getZ() throws Exception{
		return this.getCoordinate(2);			
	}

	/**
	 * set the 0-th coordinate
	 *@param val 0-th coordinate
	 */
	public void setX(double val){
	    this.setCoordinate(val, 0);
	}
	/**
	 * set the 1-st coordinate
	 *@param val 1-st coordinate
	 */
	public void setY(double val){
	    this.setCoordinate(val, 1);
	}
	/**
	 * set the 2-nd coordinate
	 *@param val 2-nd coordinate
	 */
	public void setZ(double val){
	    this.setCoordinate(val, 2);
	}
	
	/**
	 * calculates the distance of <code>this</code> point to the other point.
	 * The distance will be calculated in as many dimensions as both points have.
	 * If the two points have a different amount of coordinates, this will happen: <code>int checkDim = Math.min(this.dimension, p.getDimension());</code> 
	 *@param p the other point
	 *@return distance between the two points
	 *@throws Exception
	 */
	public double distanceTo(punkt p) throws Exception{
		return punkt.distanceBetween(this, p);
	}
	
	/**
	 * calculates the distance of point <b>p1</b> to the other point <b>p2</b>.
	 * The distance will be calculated in as many dimensions as both points have.
	 * If the two points have a different amount of coordinates, this will happen: <code>int checkDim = Math.min(this.dimension, p.getDimension());</code> 
	 *@param p1 one point
	 *@param p2 the other point
	 *@return distance between the two points
	 *@throws Exception
	 */
	public static double distanceBetween(punkt p1, punkt p2) throws Exception{
		int checkDim = Math.min(p2.dimension, p1.getDimension());

		double checkSum = 0;
		
		for ( int i=0; i<checkDim; i++ ){
			checkSum += Math.pow( p2.getCoordinate(i) - p1.getCoordinate(i), 2 );
		}
		return Math.sqrt(checkSum);
	}
	
	/**
	 * @inheritDoc
	 */
	public String toString(){
        String className = this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
		String str =  className + " #"+this.index+"(";
		String tmp;
		double c;
		for ( int i=0; i<this.dimension;i++){
			try {
			    c = this.getCoordinate(i);
			    if (this.scaled && this.scaler!=null){
			        c = this.scaler.unScale(c,i);
			    }
			    tmp = Double.toString(c);

				str += tmp;
			} catch (Exception e) {
				str += "NAN";
				e.printStackTrace();
			}
			if (i!=this.dimension-1) str += ",";
		}
		str += ")";
		return str;
	}

	/**
	 * @inheritDoc
	 */
    public int compareTo(Object arg0) {
        if ( this.getSortFor() != ((punkt)arg0).getSortFor() ){
            logger.printError("Can't compare for coordinates "+this.sortFor+" and "+((punkt)arg0).getSortFor() );
            return 0;
        }
        
        double local, remote;
        /* changed by oster 
         * if getSortFor is SORTFOR_XY then sum x and y coordinate!
         * */
        try {
        	if (this.getSortFor()!=CoordinateComparator.SORTFOR_XY) {
        		local = this.getCoordinate(this.sortFor);
            	remote = ((punkt)arg0).getCoordinate(((punkt)arg0).getSortFor());
        	} else {
        		local = this.getCoordinate(CoordinateComparator.SORTFOR_X) +
					this.getCoordinate(CoordinateComparator.SORTFOR_Y);
        		remote = ((punkt)arg0).getCoordinate(CoordinateComparator.SORTFOR_X) +
					((punkt)arg0).getCoordinate(CoordinateComparator.SORTFOR_Y);
        	}
        } catch (Exception e) {
            logger.printError("Can't compare for coordinate "+this.sortFor);
            e.printStackTrace();
            return 0;
        }
        
        if (local < remote){
            return -1;
        } else if (local > remote){
            return 1;
        }
        return 0;
    }
}
