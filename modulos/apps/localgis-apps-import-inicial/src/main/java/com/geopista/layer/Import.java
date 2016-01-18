/**
 * Import.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.layer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.geopista.app.AppContext;

public class Import {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		InputStream is = Import.class.getResourceAsStream("/localgis.properties");		
		Properties props=new Properties();
		props.load(is);
				
		File dir = new File("src"+File.separator+"main"+File.separator+"resources"+File.separator+"xml");
		AppContext.setHeartBeat(false);
		boolean resultado=Utils.importLayerFromXml(Utils.getFiles(dir), true, null, true, true, null, true, null, false,props);
		System.out.println("Resultado de Importacion:"+resultado);
	}

}
