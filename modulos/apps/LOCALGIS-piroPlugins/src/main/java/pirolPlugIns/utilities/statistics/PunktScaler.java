/**
 * PunktScaler.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 18.05.2005 for PIROL
 *
 * CVS header information:
 *  $RCSfile: PunktScaler.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:31:57 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/statistics/PunktScaler.java,v $
 */
package pirolPlugIns.utilities.statistics;

import pirolPlugIns.utilities.ScaleChanger;
import pirolPlugIns.utilities.punkt;
import pirolPlugIns.utilities.debugOutput.DebugUserIds;
import pirolPlugIns.utilities.debugOutput.PersonalLogger;

/**
 * class to scale the "coordinates" of a punkt object, often needed for statistical calculations.
 * Scales the given "coordinates" to values between 0 and 1.
 *
 * @author Ole Rahn
 * @author FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * Project: PIROL (2005),
 * Subproject: Daten- und Wissensmanagement
 * 
 * @see pirolPlugIns.utilities.statistics.CorrelationCoefficients
 */
public class PunktScaler implements ScaleChanger {


    protected int dimension = 0;
    protected double[] mins = null;
    protected double[] ranges = null;
    
    protected PersonalLogger logger = new PersonalLogger(DebugUserIds.USER_Ole);

    public PunktScaler(int dimension, double[] mins, double[] ranges) {
        super();
        this.dimension = dimension;
        this.mins = mins;
        this.ranges = ranges;
    }
    
    public PunktScaler(punkt[] punktArray) {
        super();
        try {
            this.getValuesFromArray(punktArray);
        } catch (Exception e) {
            logger.printWarning(e.getMessage());
        }
    }
    
    protected void getValuesFromArray(punkt[] array) throws Exception{
        if (array.length == 0){
            logger.printError("no points in array - can not scale!");
            return;
        }
        punkt pkt = array[0];
        this.dimension = pkt.getDimension();
        
        this.mins = new double[this.dimension];
        this.ranges = new double[this.dimension];
        double[] maxs = new double[this.dimension];
        
        for (int dim=0; dim<this.dimension; dim++){
            this.mins[dim] = Double.MAX_VALUE;
            this.ranges[dim] = 0;
            maxs[dim] = -1.0 * (Double.MAX_VALUE - 1);
        }
        
        double value;
        
        for (int i=0; i<array.length; i++){
            pkt = array[i];
            pkt.setScaler(this);
            for (int dim=0; dim<this.dimension; dim++){
                value = pkt.getCoordinate(dim);
                
                if (value < this.mins[dim])
                    this.mins[dim] = value;
                if (value > maxs[dim])
                    maxs[dim] = value;
            }
        }
        
        for (int dim=0; dim<this.dimension; dim++){
            this.ranges[dim] = maxs[dim] - this.mins[dim];
        }
    }
    
    public double scale(double value, int dimension) {
		if (dimension < this.dimension){
			return (value - this.mins[dimension])/this.ranges[dimension];
		}
		return Double.NaN;
	}

	public double unScale(double value, int dimension) {
		if (dimension < this.dimension){
			return (value * this.ranges[dimension]) + this.mins[dimension];
		}
		return Double.NaN;
	}

}
