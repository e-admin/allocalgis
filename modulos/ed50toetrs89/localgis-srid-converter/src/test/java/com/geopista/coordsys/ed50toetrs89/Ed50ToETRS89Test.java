/**
 * Ed50ToETRS89Test.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.coordsys.ed50toetrs89;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.geopista.coordsys.ed50toetrs89.info.LonLat;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.coordsys.Reprojector;
import com.vividsolutions.jump.coordsys.impl.PredefinedCoordinateSystems;

public class Ed50ToETRS89Test {

	private Ed50ToETRS89 ed50toetrs89;
	@Before
	public void startUp()
	{
		this.ed50toetrs89=new Ed50ToETRS89();
	}
	
	@Test
	public void testEd50toEtrs89() throws IOException, org.apache.commons.cli.ParseException {
		
		assertEqualsLonLat(new LonLat(2.061342157035246, 40.00684188010053), ed50toetrs89.ed50toEtrs89(-2.060085792, 40.0080256526));
		//assertEqualsLonLat(new LonLat(35433d, 3333233d), ed50toetrs89.ed50toEtrs89(4554d, 435d));
		
	}
	
	@Test
	public void testTransformED50ToEtrs89() throws ParseException, IOException, org.apache.commons.cli.ParseException {
		
		
		CoordinateSystem source = PredefinedCoordinateSystems.GEOGRAPHICS_ED50;
		CoordinateSystem destination = PredefinedCoordinateSystems.GEOGRAPHICS_ETRS89;
		
		Coordinate coord = new Coordinate(-2.060085792, 40.0080256526);
		
		Reprojector.instance().transformED50ToEtrs89(source, destination, coord);
		
		assertEquals(-2.061342157035246, coord.x, 1e-6);
		assertEquals(40.00684188010053, coord.y, 1e-6);
		
		source = PredefinedCoordinateSystems.UTM_30N_ED50;
		destination = PredefinedCoordinateSystems.UTM_30N_ETRS89;
		
		coord = new Coordinate(573786.240112311, 4436907.29010009);
		
		Reprojector.instance().transformED50ToEtrs89(source, destination, coord);
		
		assertEquals(573676.82655483, coord.x, 1e-6);
		assertEquals(4436698.828403796, coord.y, 1e-6);
		
		
	}
	
	private void assertEqualsLonLat(LonLat expected, LonLat res) {
		assertEquals(res.getLatitude(),expected.getLatitude(),1e-6);
		assertEquals(res.getLongitude(),expected.getLongitude(),1e-6);
	}

}
