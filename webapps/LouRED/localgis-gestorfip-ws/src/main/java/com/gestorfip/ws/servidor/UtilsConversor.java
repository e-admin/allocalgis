package com.gestorfip.ws.servidor;


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jump.coordsys.CoordinateSystem;

import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;

public class UtilsConversor {

	
	/**  Devuelve el SRID al cual se desea convertir las geometrias
	 * @param sourceSRID
	 * @return
	 */
	public static int getTargetSrid(int sourceSRID) {
		int targetSRID = 0;
	
		if (sourceSRID == 4230) {
			targetSRID = 4258;
		} else if (sourceSRID == 23030) {
			targetSRID = 25830;
		} else if (sourceSRID == 23029) {
			targetSRID = 25829;
		} else if (sourceSRID == 23028) {
			targetSRID = 25828;
		}
		return targetSRID;
	}
	
	public static int getTargesSridGeographic(int sourceSRID){
		
		int targetSRIDGeofraphic = 0;
		
		if (sourceSRID == 23030 || sourceSRID == 23029 || sourceSRID == 23028) {
			targetSRIDGeofraphic = 4230;
		} 
		else if (sourceSRID == 25830 || sourceSRID == 25829 || sourceSRID == 25828) {
			targetSRIDGeofraphic = 4258;
		} 
		return targetSRIDGeofraphic;
	}
	
	

}
