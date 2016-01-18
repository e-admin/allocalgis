package org.agil.core.spatialindex;

import java.util.Iterator;

import javax.swing.JDialog;

import org.agil.core.dao.ShapeFileOnDemandDataAccesor;
import org.agil.core.feature.FeatureCollectionOnDemand;
import org.agil.core.jump.feature.datasource.CatalogoLocal;
import org.agil.core.jump.feature.datasource.CatalogoLocalDataSourceQueryChooser;

import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
/**
 * <p>Título: </p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Empresa: </p>
 * @author sin atribuir
 * @version 1.0
 */

public class ShapefileSpatialIndexCreator {
  private CatalogoLocal catalogo;
  public ShapefileSpatialIndexCreator(String catalogoPath) {

    CatalogoLocalDataSourceQueryChooser selector =
        new CatalogoLocalDataSourceQueryChooser(catalogoPath, null);
    selector.getComponent().setVisible(true);
    Iterator listFC = selector.getDataSourceQueries().iterator();
    TaskMonitor monitor=null; // TODO: Usar un Taskmonitor
    DataSourceQuery dataSourceQuery = null;
    com.vividsolutions.jump.io.datasource.Connection connection = null;
    try {
      while (listFC.hasNext()) {
        dataSourceQuery = (DataSourceQuery) listFC.next();
        connection = dataSourceQuery.getDataSource().getConnection();
        FeatureCollectionOnDemand fC = (FeatureCollectionOnDemand)
            connection.executeQuery(dataSourceQuery.getQuery(),monitor);
        ShapeFileOnDemandDataAccesor dataAccesor = (
            ShapeFileOnDemandDataAccesor) fC.getDataAccesor();
        dataAccesor.createIndex();
        System.out.println("Indice espacial creado para "+fC.getName());
      } //while
    }catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      connection.close();
    }//try

  }

  private JDialog createClockDialog(){
    TaskMonitorDialog monitorDialog = new TaskMonitorDialog(null, null);
    return monitorDialog;
  }

  /**
   *  El parametro de linea de comando sera la ruta del catalogo que contiene
   *  las clases cuya definicion se quiere crear.
   *
   *@param  args  The command line arguments
   */
  public static void main(String[] args) {
    if(args.length <= 0){
      System.out.println("Usage: java SpatialIndexCreator ficheroDeCatalogo");
      System.exit( -2);
    }
    ShapefileSpatialIndexCreator spatialIndexCreator = new ShapefileSpatialIndexCreator(args[0]);
    System.exit(0);
  }
}

