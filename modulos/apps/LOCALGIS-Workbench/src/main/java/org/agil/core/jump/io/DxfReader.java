/**
 * DxfReader.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Creado el 17-abr-2004
 *
 * Este codigo se distribuye bajo licencia GPL
 * de GNU. Para obtener una cópia integra de esta
 * licencia acude a www.gnu.org.
 * 
 * Este software se distribuye "como es". AGIL
 * solo  pretende desarrollar herramientas para
 * la promoción del GIS Libre.
 * AGIL no se responsabiliza de las perdidas económicas o de 
 * información derivadas del uso de este software.
 */
package org.agil.core.jump.io;

import com.geopista.ui.plugin.io.dxf.core.jump.io.FeatureCollectionDxfConverter;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.JUMPReader;

/**
 * Clase encargada de cargar FeatureCollection en memoria a partir
 * de la información geográfica definida en un fichero Dxf.
 * 
 * 
 * 
 * 
 * @author Alvaro Zabala (AGIL)
 *
 */
public class DxfReader implements JUMPReader {


  public DxfReader() {
  }
  
  public FeatureCollection read(DriverProperties dp) throws Exception {
    
    String dxfPath = (String) dp.get("File");
    return createFeatureCollectionFromSelection(dxfPath);
  }
  
  /**
   * TODO Estudiar bien los Query Chooser para que se puedan devolver
   * todos los FeatureCollection implicitamente contenidos en el.
   * @param selectedFile
   * @return
   */
  private FeatureCollection createFeatureCollectionFromSelection(String selectedFile){
	FeatureCollectionDxfConverter dxfConverter = new FeatureCollectionDxfConverter();
	dxfConverter.convert(selectedFile);
	return (FeatureCollection) dxfConverter.getLineFeatureCollection().iterator().next(); 
  }
	
	 
}
