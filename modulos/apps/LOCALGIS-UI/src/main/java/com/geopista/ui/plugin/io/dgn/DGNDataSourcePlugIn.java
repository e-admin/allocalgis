/**
 * DGNDataSourcePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 14-jun-2004
 */
package com.geopista.ui.plugin.io.dgn;

import javax.swing.JFileChooser;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource;
import com.vividsolutions.jump.workbench.datasource.DataSourceQueryChooserManager;
import com.vividsolutions.jump.workbench.datasource.InstallStandardDataSourceQueryChoosersPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * This plugin is a driver for a data source backed by a DGN
 * 
 * @author Enxenio, SL
 */
public class DGNDataSourcePlugIn implements PlugIn {
    
       /**
       * Clase estática que permite leer ficheros DGN.
       * No está implementado el guardar como (por eso el 2º argumento
       * de la llamada a super() es null)
       * (no disponemos de esa funcionalidad)
       *
       */
      public static class DGN extends StandardReaderWriterFileDataSource {
            public DGN() {
                super(null, null, new String[] { "DGN" });
            }
      }
 
   /**
   * Initializes the plugin by creating the data source and data source 
   * query choosers.
   * @see PlugIn#initialize(com.vividsolutions.jump.workbench.plugin.PlugInContext)
   */
  public void initialize(PlugInContext context) {
        
        DataSourceQueryChooserManager chooserManager = DataSourceQueryChooserManager.get(context.getWorkbenchContext().getWorkbench().getBlackboard());
        chooserManager.addLoadDataSourceQueryChooser(
        		new DGNDataSourceQueryChooser(DGN.class, getName(), extensions(DGN.class), context.getWorkbenchContext()) {
                    protected void addFileFilters(JFileChooser chooser) {
                        super.addFileFilters(chooser);
                        InstallStandardDataSourceQueryChoosersPlugIn.addCompressedFileFilter(getName(), chooser);
                    }
        		});         		
  }

  /**
   * This function does nothing, all the setup is completed in initialize().
   */
	  public boolean execute(PlugInContext context) { return(false); }
  
	  /**
   * @see PlugIn#getName()
   */
	  public String getName() { return("GEOPISTA DGN"); }
    
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
