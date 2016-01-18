/*
 * Created on 10.11.2004
 *
 * CVS header information:
 *  $RCSfile: CsvRawPointDataset.java,v $
 *  $Revision: 1.1 $
 *  $Date: 2009/07/03 12:32:06 $
 *  $Source: /usr/cvslocalgis/.CVSROOT/localgisdos/core/src/pirolPlugIns/utilities/AsciiFileParsing/CsvRawPointDataset.java,v $
 */
package pirolPlugIns.utilities.AsciiFileParsing;

import java.util.HashMap;

/**
 * @author Ole Rahn
 * <br>
 * <br>FH Osnabr&uuml;ck - University of Applied Sciences Osnabr&uuml;ck,
 * <br>Project: PIROL (2005),
 * <br>Subproject: Daten- und Wissensmanagement
 * 
 * @version $Revision: 1.1 $
 */
public class CsvRawPointDataset extends HashMap implements Comparable {
	
    private static final long serialVersionUID = 7310544847374925067L;

    private int index = -1;
	
	public static double INVALID_COORD = Double.NaN;
	
	private double x=.0,y=.0,z=.0;

	public int getIndex(){
		return index;
	}
	
	/**
	 * @return Returns the x.
	 */
	public double getX() {
		return x;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * @return Returns the y.
	 */
	public double getY() {
		return y;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * @return Returns the z.
	 */
	public double getZ() {
		return z;
	}
	/**
	 * @param z The z to set.
	 */
	public void setZ(double z) {
		this.z = z;
	}


	/**
	 * @param index index of the dataset
	 */
	public CsvRawPointDataset(int index) {
		super();
		this.index = index;
		x = y = z = CsvRawPointDataset.INVALID_COORD;
	}

	/**
	 * not to be used... 
	 */
	private CsvRawPointDataset() {
		super();
	}


	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0) {
		CsvRawPointDataset datas = (CsvRawPointDataset)arg0;
		
		if ( this.getIndex() == datas.getIndex() )
			return 0;
		else if ( this.getIndex() >= datas.getIndex() )
			return 1;
		else
			return -1;
	}

}
