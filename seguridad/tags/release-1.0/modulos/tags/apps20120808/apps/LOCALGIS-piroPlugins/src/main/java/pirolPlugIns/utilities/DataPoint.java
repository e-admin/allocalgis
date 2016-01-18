/*
 * Created on 09.03.2005 for Pirol
 *
 * CVS header information:
 * $RCSfile: DataPoint.java,v $
 * $Revision: 1.1 $
 * $Date: 2009/07/03 12:31:54 $
 * $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/DataPoint.java,v $
 */
package pirolPlugIns.utilities;

/**
 * @author oster
 * Extends the class "punkt" and adds a value for the data which will be interpolated.
 */
public class DataPoint extends punkt {
	protected double value = 0;
	private boolean isValueSet = false;
	
	public DataPoint() {
		super();
	}
	
	public DataPoint( double[] coords ){
		super(coords);
	}
	
	public DataPoint( double[] coords, int index){
		super (coords, index);
	}
	
	public DataPoint( double[] coords, int index, ScaleChanger scaler, boolean prescaled){
		super(coords, index, scaler, prescaled);
	}
	
	public void setValue(double _value) {
		value = _value;
		isValueSet = true;
	}
	
	public double getValue() {
		return value;
	}

	public boolean valueSet() {
		return isValueSet;
	}
	
    public boolean equals(Object obj) {
        DataPoint p;
        try {
            p = (DataPoint)obj;
        } catch (Exception e ){
            return false;
        }
        
        try {
            if (this == p){
                return true;
            } else if (p.getIndex() == this.getIndex() && !(this.getIndex()<0)){
	            return true;
	        } else if (p.value == this.value && p.getX()==this.getX() && p.getY() == this.getY() && p.getZ() == this.getZ()) {
	            return true;
	        }
        } catch (Exception e) {}
        return false;
    }
    
    public static DataPoint clone(DataPoint dataPoint) {
    	DataPoint newDataPoint = new DataPoint(dataPoint.coordinates);
    	newDataPoint.setValue(dataPoint.value);
    	return newDataPoint;
    }
    
}
