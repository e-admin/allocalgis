package org.agil.core.jump.plugin;

import javax.swing.JFileChooser;

import org.agil.core.jump.io.ScalableShapefileReader;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.io.JUMPReader;
import com.vividsolutions.jump.io.JUMPWriter;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.DataSourceQueryChooserManager;
import com.vividsolutions.jump.workbench.datasource.InstallStandardDataSourceQueryChoosersPlugIn;
import com.vividsolutions.jump.workbench.datasource.SaveFileDataSourceQueryChooser;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * <p>
 * Título: ScalableShapeFilePlugIn
 * </p>
 *
 * <p>Descripción:
 * Registra un origen de datos que permite leer shapes muy pesados.
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Empresa: </p>
 * @author sin atribuir
 * @version 1.0
 */

public class ScalableShapeFilePlugIn implements PlugIn {
  /**
   * Constructor por defecto
   *
   */
  public ScalableShapeFilePlugIn() {
  }
  /**
   * Clase estática que permite leer y escribir shapes de forma escalable
   * TODO Implementar escritura de shapes escalable (lo veo mas complicada)
   * @author Alvaro Zabala (AGIL)
   *
   */
  public static class ShapefileScalable extends StandardReaderWriterFileDataSource {
        public ShapefileScalable() {
            super(new ScalableShapefileReader(), null, new String[] { "shp" });
        }
  }

  /**
   * Este metodo es llamado por el workbench al cargar el plugin
   */
  public void initialize(PlugInContext context) {
  	
   Blackboard blackboard = context.getWorkbenchContext().getWorkbench().getBlackboard();
   addFileDataSourceQueryChoosers(new ScalableShapefileReader(context.getWorkbenchGuiComponent()), 
   											(JUMPWriter)null,
   											 "SHP Escalable",
   											context.getWorkbenchContext(),
                                  			ShapefileScalable.class );


  }
  /**
   * Este plugin no se ejecuta (solo se inicializa registrando nuevos origenes
   * de datos)
   */
  public boolean execute(PlugInContext context) {
    return false;
  }
  public String getName() {
    return "Scalable Shapefile Driver";
  }
  
  /**
   * Añade un DataSourceQueryChooser (dialogo de selección de orígenes de datos)
   * al sistema.
   * 
   * @param reader
   * @param writer
   * @param description
   * @param context
   * @param readerWriterDataSourceClass
   */
  private void addFileDataSourceQueryChoosers(JUMPReader reader,
                                              JUMPWriter writer,
                                              final String description,
                                              WorkbenchContext context,
                                              Class readerWriterDataSourceClass) {
                                              	
	DataSourceQueryChooserManager.get(context.getBlackboard()).
				 addLoadDataSourceQueryChooser(
		   			new ScalableShapefileQueryChooser(readerWriterDataSourceClass,
		   			description,
		   			extensions(readerWriterDataSourceClass), context) {
		   				protected void addFileFilters(JFileChooser chooser) {
			   				super.addFileFilters(chooser);
			   				InstallStandardDataSourceQueryChoosersPlugIn.
			   					addCompressedFileFilter(description,chooser);
		   				}//addFileFilters
	   				}).addSaveDataSourceQueryChooser(
		   				new SaveFileDataSourceQueryChooser(
			   				readerWriterDataSourceClass,
			   				description,
			   				extensions(readerWriterDataSourceClass),
			   								context));                           	
                                              	
    }//addFileDataSourceQueryChoosers

    public static String[] extensions(Class readerWriterDataSourceClass) {
        try {
            return (
                (StandardReaderWriterFileDataSource) readerWriterDataSourceClass
                    .newInstance())
                .getExtensions();
        } catch (Exception e) {
            Assert.shouldNeverReachHere(e.toString());
            return null;
        }
    }


}