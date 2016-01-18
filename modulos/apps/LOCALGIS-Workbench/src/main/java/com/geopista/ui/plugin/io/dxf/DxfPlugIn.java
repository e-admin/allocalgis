/**
 * DxfPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Creado el 18-abr-2004
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
package com.geopista.ui.plugin.io.dxf;

import javax.swing.JFileChooser;

import org.agil.core.jump.io.DxfReader;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.io.JUMPReader;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.DataSourceQueryChooserManager;
import com.vividsolutions.jump.workbench.datasource.InstallStandardDataSourceQueryChoosersPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * PlugIn que nos permite cargar ficheros Dxf en Jump.
 * Para tal fin, añade una entrada mas en el QueryChooser manager
 * (componente que se nos muestra al seleccionar la opción de menú
 * LoadDataset) que permite reconocer ficheros geopistadxf.
 * 
 * 
 * @author Alvaro Zabala (AGIL)
 *
 */
public class DxfPlugIn implements PlugIn {

	   /**
	   * Clase estática que permite leer ficheros DXF.
	   * No está implementado el guardar como (por eso el 2º argumento
	   * de la llamada a super() es null)
	   * (no disponemos de esa funcionalidad)
	   *
	   */
	  public static class Dxf extends StandardReaderWriterFileDataSource {
			public Dxf() {
				super(/*new DxfReader()*/null, /*new DxfWriter()*/null, new String[] { "dxf" });
			}
	  }
	
	/**
	 * Este metodo es llamado por el Workbench cuando está cargando los
	 * plugines del sistema.
	 */
	public void initialize(PlugInContext context) throws Exception {
 		addFileDataSourceQueryChoosers(new DxfReader(), context.getWorkbenchContext(), "GEOPISTA dxf", Dxf.class);
	}

	/**
	 * Este plugin no se ejecuta (solo se instala).
	 * Por tanto, este metodo no hace nada
	 */
	public boolean execute(PlugInContext context) throws Exception {
		return false;
	}

	/**
	 * Devuelve el nombre del plugin
	 * @return nombre descriptivo del plugin
	 */
	public String getName() {
		return "GEOPISTA Dxf Driver";
	}
	
/**
   * Añade un DataSourceQueryChooser (dialogo de selección de orígenes de datos)
   * al sistema.
   * 
   * @param reader
   * @param description
   * @param readerWriterDataSourceClass
   */
	 private void addFileDataSourceQueryChoosers(JUMPReader reader,
	 		WorkbenchContext context,
												  final String description,
												  Class readerWriterDataSourceClass) {
                                              	
		DataSourceQueryChooserManager.get(context.getBlackboard()).addLoadDataSourceQueryChooser(
						new GeopistaLoadDxfQueryChooser(readerWriterDataSourceClass,
													description,
											extensions(readerWriterDataSourceClass),
											 context) {
							
							protected void addFileFilters(JFileChooser chooser) {
								super.addFileFilters(chooser);
								InstallStandardDataSourceQueryChoosersPlugIn.
									addCompressedFileFilter(description,chooser);
							}//addFileFilters
						}).addSaveDataSourceQueryChooser(
							new GeopistaSaveDxfQueryChooser(
								readerWriterDataSourceClass,
								description,
								extensions(readerWriterDataSourceClass),
												context));
                                              	
	}//addFileDataSourceQueryChoosers
	
	/**
	 * Devuelve las extensiones que reconoce la clase ReaderWriter especificada
	 * (que no es mas que un wrapper de 1 JumpReader y 1 JumpWriter)
	 * 
	 * @param readerWriterDataSourceClass
	 * @return extensiones que reconoce
	 */
	public static String[] extensions(Class readerWriterDataSourceClass) {
		try {
			return ((StandardReaderWriterFileDataSource) readerWriterDataSourceClass
						.newInstance()).getExtensions();
		} catch (Exception e) {
			Assert.shouldNeverReachHere(e.toString());
			return null;
		}
	}
}
