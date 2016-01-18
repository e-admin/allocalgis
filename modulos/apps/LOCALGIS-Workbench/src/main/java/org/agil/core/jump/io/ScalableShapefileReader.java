package org.agil.core.jump.io;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import org.agil.core.dao.ShapeFileOnDemandDataAccesor;
import org.agil.core.feature.FeatureCollectionOnDemand;

import com.geopista.editor.WorkbenchGuiComponent;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.JUMPReader;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

/**
 * <p>Título: ScalableShapeFileReader</p>
 * <p>Descripción:
 * JUMPReader que se encarga de leer FeatureCollection a partir de Shapes, pero
 * de forma escalable.
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Empresa: </p>
 * @author sin atribuir
 * @version 1.0
 */

public class ScalableShapefileReader implements JUMPReader {
  private WorkbenchGuiComponent workbenchGUIComponent;
  

  public ScalableShapefileReader(WorkbenchFrame workbenchFrame) {
    this((WorkbenchGuiComponent)workbenchFrame);
  }
  
  public ScalableShapefileReader(){}

  /**
   * Usa un componente principal que no tiene por qué ser un Frame.
 * @param workbenchFrame2
 */
public ScalableShapefileReader(WorkbenchGuiComponent workbenchFrame2)
{
	this.workbenchGUIComponent=workbenchFrame2;
}

public FeatureCollection read(DriverProperties dp) throws Exception {
    
    String shapePath = (String) dp.get("File");
    return createFeatureCollectionFromSelection(shapePath);
  }
  
  private FeatureCollectionOnDemand createFeatureCollectionFromSelection(String selectedFile){
			  String shapeDirectory = null;
			  //los indices espaciales deben residir en el mismo directorio que los shapes
			  int lastSlash = selectedFile.lastIndexOf(File.separator);
			  if(lastSlash == -1){
				shapeDirectory = "./";
			  }else{
				shapeDirectory = selectedFile.substring(0,lastSlash);
			  }  
			  String shapePathWithoutExt = selectedFile.substring(0, selectedFile.length() - 4);
			  ShapeFileOnDemandDataAccesor dataAccesor =
						  new ShapeFileOnDemandDataAccesor(shapeDirectory,shapePathWithoutExt);
			  FeatureSchema schema = dataAccesor.getShapefileSchema();
			  dataAccesor.setSchema(schema);
			  if(!dataAccesor.isSpatialIndexed()){
				 // boolean generar = askForSpatialIndex();
			  	boolean generar=true;
				  if(generar){
					  try {
						  dataAccesor.createIndex();
					  } catch (IOException e) {
						  e.printStackTrace();
					  } catch (Exception e) {
						  e.printStackTrace();
					  }//try	
				  }//if generar
			  }//if isSpatialIndexed
			  final FeatureCollectionOnDemand fC = new FeatureCollectionOnDemand(schema);
			  fC.setDataAccesor(dataAccesor);
			  return fC;
	  }
	  private MultiInputDialog askForSpatialIndexDialog = null;
	  /**
	   * Muestra un dialogo preguntando por la generacion de índices espaciales para el fichero
	   * seleccionado.
	   * @return Devuelve si el usuario pulsó aceptar o no
	   */
	  private boolean askForSpatialIndex(){
			if(askForSpatialIndexDialog == null){
			  askForSpatialIndexDialog = new MultiInputDialog(workbenchGUIComponent.getMainFrame(), "Generación de Indices Espaciales", true);
			  askForSpatialIndexDialog.setSideBarImage(new ImageIcon(IconLoader.icon("World2.gif").getImage()));
			  askForSpatialIndexDialog.setSideBarDescription("Genera un fichero de índice espacial para el shapefile seleccionado.");
			  askForSpatialIndexDialog.addLabel("El fichero seleccionado  \n no se encuentra indexado espacialmente.");
			  askForSpatialIndexDialog.addLabel("¿Desea crear su fichero de indice? \n Esta operacion puede tomar varios minutos.");
			  askForSpatialIndexDialog.setSize(600,200);
			}
		    GUIUtil.centreOnScreen(askForSpatialIndexDialog);
			askForSpatialIndexDialog.show();
		   
			return askForSpatialIndexDialog.wasOKPressed();
	  }
    
	

}