/*
 * Creado el 24-abr-2004
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
package org.agil.core.jump.plugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.plaf.basic.BasicFileChooserUI;

import org.agil.core.coverage.GridCoverage;
import org.agil.core.coverage.GridCoverageExchange;
import org.agil.core.coverage.TfwNoAvailableException;

import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.LoadFileDataSourceQueryChooser;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 * @author Alvaro Zabala (AGIL)
 *
 */
public class JaiImageQueryChooser extends LoadFileDataSourceQueryChooser {
	
		/**
		 * Cache donde se guardan temporalmente los componentes graficos,
		 * para evitar construirlos dos veces.
		 * @todo revisar si usar referencias weak, para no consumir demasiados
		 * recursos.
		 */
		private Blackboard blackboard;
		/**
		 * Clave del componente grafico cacheado, para acceder a el.
		 */
		public final static String JAI_FILE_CHOOSER_PANEL = JaiImageQueryChooser.class.getName();
		/**
		 * Extensiones de ficheros que se pueden cargar con este tipo 
		 */
		public final static String[] extensions = {"tiff", "tif", "ptiff", "ptif","bmp", "png"};
		/**
		 * Excepciones que se producen durante la carga de las imágenes seleccionadas.
		 * Se muestran al usuario al final, para que sepa que imágenes causaron conflicto
		 * (mientras continua la carga)
		 */
		private ArrayList exceptions;
		/**
		 * @param dataSourceClass ScalableShapefileReader que nos permite
		 * construir FeatureCollection que accede a los ficheros shape seleccionados
		 * 
		 * @param description descripcion del origen 
		 * 
		 * @param extensions extensiones que reconoce (tiff, ptiff, etc)
		 */
		public JaiImageQueryChooser(Class dataSourceClass, 
									String description, 
									WorkbenchContext context) {
										
			super(dataSourceClass, description, extensions, context);
			this.blackboard = context.getBlackboard();
			exceptions = new ArrayList();
		}
		
		public boolean hasExceptions(){
			return exceptions.size() != 0;
		}
		
		public List getExceptions(){
			return exceptions;
		}
		
		/**
		 * 
		 * A partir de la seleccion del usuario en el dialogo FileChooser,
		 * devuelve una coleccion de GridCoverage asociada a los ficheros seleccionados
		 *
		 * @return colección de GridCoverages
		 * 
		 * @todo ESTO SE ESTÁ LLAMANDO 2 VECES. CACHEAR
		 */
		public Collection getDataSourceQueries() {
			//aquí se guardaran todas Coverage que se vayan creando
			ArrayList solucion = new ArrayList();
			exceptions.clear();
			//Primero obtenemos los ficheros / directorios seleccionados
			Collection dataSourceQuerysFile = super.getDataSourceQueries();
			Iterator iterator = dataSourceQuerysFile.iterator();
			while(iterator.hasNext()){
				DataSourceQuery dataSourceQuery = (DataSourceQuery) iterator.next();
				DataSource dataSource = dataSourceQuery.getDataSource();
				final String selectedFile = (String)dataSource.
								getProperties().get(DataSource.FILE_KEY);
				//Ahora puede ser un Directorio o un Fichero
				File file = new File(selectedFile);
				if(file.isDirectory()){//vamos a construir un GridCoverageCollection
					//Migrar esto a una arquitectura de properties (es gilipollezco parsear)
					String url = "multi-earth-image|local|jai_images|rgb|"+file.toString();
					GridCoverage gridCoverage = null;
					try {
						gridCoverage =
							GridCoverageExchange.getInstance().createFromName(url);
						solucion.add(gridCoverage);
					} catch (TfwNoAvailableException e) {
						exceptions.add(e);
					}
					
					
				}else{//GridCoverage 
					String url = "earth-image|local|jai_images|rgb|"+file.toString();
					GridCoverage gridCoverage;
					try {
						gridCoverage =
							GridCoverageExchange.getInstance().createFromName(url);
						solucion.add(gridCoverage);
							
					} catch (TfwNoAvailableException e) {
						exceptions.add(e);
					}
				}//else
			}//while
			
			return solucion;
		}
		
	    public boolean isInputValid() {
		   //Trick to allow inner class to modify an outside variable:
		   //stick the variable in an array. [Jon Aquino]
		   final Boolean[] actionPerformed = new Boolean[] { Boolean.FALSE };
		   ActionListener listener = new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   actionPerformed[0] = Boolean.TRUE;
			   }
		   };
		   getFileChooserPanel().getChooser().addActionListener(listener);
		   try {
			   //Workaround for Java Bug 4528663 "JFileChooser doesn't return what is
			   //typed in the file name text field." [Jon Aquino]
			   if (getFileChooserPanel().getChooser().getUI()
				   instanceof BasicFileChooserUI) {
				   BasicFileChooserUI ui =
					   (BasicFileChooserUI) getFileChooserPanel().getChooser().getUI();
				   ui.getApproveSelectionAction().actionPerformed(null);
			   }
		   } finally {
			   getFileChooserPanel().getChooser().removeActionListener(listener);
		   }
		   return actionPerformed[0] == Boolean.TRUE;
	   }
	   
	   
		
		/**
		 * Sobreescribiendo este metodo heredado de LoadFileDataSourceQueryChooser,
		 * y este de FileDataSourceQueryChooser, podemos personalizar el componente
		 * de seleccion de ficheros (en este caso nos interesa poder seleccionar
		 * directorios)
		 */
		protected FileChooserPanel getFileChooserPanel() {
			if (blackboard.get(JAI_FILE_CHOOSER_PANEL) == null) {
				JFileChooser fileChooser = 
					GUIUtil.createJFileChooserWithExistenceChecking();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setControlButtonsAreShown(false);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				//permitimos la seleccion de ficheros y directorios
				blackboard.put(JAI_FILE_CHOOSER_PANEL,
						new FileChooserPanel(fileChooser,blackboard));
			}
			return (FileChooserPanel) blackboard.get(JAI_FILE_CHOOSER_PANEL);
		}
}
