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
