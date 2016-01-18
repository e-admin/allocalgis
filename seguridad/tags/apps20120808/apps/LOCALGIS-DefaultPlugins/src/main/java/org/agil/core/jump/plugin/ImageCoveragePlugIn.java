/*
 * Creado el 23-abr-2004
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.agil.core.coverage.AgilImageDataAccesor;
import org.agil.core.coverage.C_Function;
import org.agil.core.coverage.EarthImage;
import org.agil.core.coverage.GridCoverage;
import org.agil.core.coverage.ImageDataAccesor;
import org.agil.core.jump.coverage.CoverageLayer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.GeopistaEnableCheckFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.geom.EnvelopeUtil;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.io.JUMPReader;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.datasource.DataSourceQueryChooserDialog;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

/**
 * 
 * PlugIn que se encarga de registrar los codecs para cargar coberturas
 * de imágenes 
 * 
 * 
 * @author Alvaro Zabala (AGIL)
 *
 */
public class ImageCoveragePlugIn extends ThreadedBasePlugIn {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ImageCoveragePlugIn.class);
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
		
		/**
		 * Implementación de JUMPReader que no lee Features. Es necesaria
		 * por el API de QueryChooser, pero no lee nada porque en este PlugIn
		 * estamos leyendo Coverages, no Features
		 */
		private static final JUMPReader dummyReader = new JUMPReader(){
			public FeatureCollection read(DriverProperties dp) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		/**
		 * Los QueryChooser estan diseñados para obtener conexiones a FeatureCollection.
		 * Por tanto, este DataSource se le proporciona, pero no hace nada.
		 * 
		 * La alternativa seria integrar Coverage y FeatureCollection
		 * @author Alvaro Zabala (AGIL)
		 *
		 */
		public static class JAI extends StandardReaderWriterFileDataSource {
            /**
             * Logger for this class
             */
            private static final Log logger = LogFactory.getLog(JAI.class);

			public JAI() {
				super(dummyReader, null, new String[] { "tif", "png", "ptif", "bmp", "jpg" });
			}
		}
	
		/**
		 * Instalador del plugin
		 */
		 public void initialize(PlugInContext context) throws Exception {
		 		
		      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
		      
		      JPopupMenu layerNamePopupMenu = context.getWorkbenchContext().getWorkbench()
		                                                        .getGuiComponent()
		                                                        .getLayerNamePopupMenu();
		      featureInstaller.addPopupMenuItem(context.getWorkbenchContext().getWorkbench()
		                                                          .getGuiComponent()
		                                                          .getCategoryPopupMenu(),
		            this, I18N.get(this.getName()) + "...", false,
		            null, this.createEnableCheck(context.getWorkbenchContext()));
		      
		      featureInstaller.addMainMenuItem(this, new String[]{
		      		MenuNames.LAYER,
    				"ImageCoverage"},
		      		AppContext.getMessage(this.getName()) + "...",false, null, this.createEnableCheck(context.getWorkbenchContext()));
		      
		    }
	     /**
		 *  Este metodo se ejecuta normalmente dentro del thread de la GUI. (Es
		 *  decir, como consecuencia de que el usuario pulse un boton, o algo así).
		 *  En este caso, lo que hará es mostrar un dialogo del que se podrán elegir
		 *  orígenes de datos para cargar.
		 *
		 *@param  context        contexto del plugin
		 *@return                booleano que indica si se pulso aceptar o cancelar
		 *@exception  Exception  Description of Exception
		 */
		public boolean execute(PlugInContext context) throws Exception {
			return true;
			
		}


		/**
		 *  Todos los plugines que extiendan de ThreadedBase realizan su proceso en
		 *  este metodo, que se ejecuta en un thread dedicado.
		 *
		 *@param  monitor        supervisa las tareas
		 *@param  context        contexto de ejecucion del plugin (como parte de
		 *      JUMP)
		 *@exception  Exception  Description of Exception
		 *@see                   com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn#run(com.vividsolutions.jump.task.TaskMonitor,
		 *      com.vividsolutions.jump.workbench.plugin.PlugInContext)
		 */
		public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
			DataSourceQueryChooserDialog dialog = getDialog(context);
			dialog.setVisible(true);
			if(dialog.wasOKPressed()==false) return;
			
			JaiImageQueryChooser currentChooser = (JaiImageQueryChooser) dialog.getCurrentChooser();
			Collection coverages = currentChooser.getDataSourceQueries();
			Iterator covIt = coverages.iterator();
			while(covIt.hasNext()){
				GridCoverage coverage = (GridCoverage) covIt.next();
				C_Function objectImage = coverage.getFunction();
				String layerName = aplicacion.getI18nString("Layer")+" " + coverage.getIdentificador();
				if(objectImage instanceof EarthImage)
				{
				    ImageDataAccesor dataAccesor = ((EarthImage) objectImage).getDataAccesor();
				    if(dataAccesor instanceof AgilImageDataAccesor)
				    {
				        try
				        {
				            File sourceFile = new File(((AgilImageDataAccesor) dataAccesor).getImagePath());
				            layerName = sourceFile.getName();
				        }catch(Exception e)
				        {
				            //Capturo Exception para que en caso de que se produzca cualquier fallo
				            //se asigne el nombre por defecto a la capa raster
                            logger.error("run(TaskMonitor, PlugInContext)", e);
				        }
				    }
				}
				CoverageLayer layer = new CoverageLayer(coverage,
				        layerName,
						context.getLayerManager());
				context.getLayerManager().addLayerable(chooseCategory(context), layer);
				
				//Nuevo carga de la imagen raster
				Envelope envelope = new Envelope();
				Envelope envelope2 = null;
				envelope2 = layer.getEnvelope();
				if (envelope2!=null){
				envelope.expandToInclude(envelope2);
				}
				context.getLayerViewPanel().getViewport().zoom(EnvelopeUtil.bufferByFraction(
		                envelope, 0.03));
			}
			
			boolean hasExceptions = currentChooser.hasExceptions();
			if(hasExceptions){
			    
			    Iterator exceptions = currentChooser.getExceptions().iterator();
			    while(exceptions.hasNext()){
				
					Exception ex = (Exception) exceptions.next();
					JOptionPane.showMessageDialog(aplicacion.getMainFrame(),ex.getMessage());
				}
			    
				/*HTMLFrame frame = context.getWorkbenchFrame().getOutputFrame();
				frame.setTitle("Informe de Errores en la Carga de Imágenes");
				frame.createNewDocument();
				frame.addHeader(2, "Errores durante la carga");
				frame.append("<table>");
				Iterator exceptions = currentChooser.getExceptions().iterator();
				while(exceptions.hasNext()){
					frame.append("<tr><td>");
					Exception ex = (Exception) exceptions.next();
					frame.append(ex.getMessage());
					frame.append("</tr></td>");
				}
				frame.append("</table>");
				frame.surface();*/
			}//if
						
		}
		
	
		private String chooseCategory(PlugInContext context) {
			return context.getLayerNamePanel().getSelectedCategories().isEmpty()
					? StandardCategoryNames.WORKING
					: context.getLayerNamePanel().getSelectedCategories().iterator().next()
					.toString();
		}


	/**
	 * Muestra el dialogo que permite seleccionar ficheros de imágenes o bien directorios
	 * que contienen ficheros
	 * @param context
	 * @return
	 */
    private DataSourceQueryChooserDialog getDialog(PlugInContext context) {

        //primero mira si ya existe en memoria
        String KEY = getClass().getName() + " - DIALOG";
        if (null == context.getWorkbenchContext().getWorkbench().getBlackboard().get(KEY)) {
        	//Primero obtenemos la coleccion de QueryChoosers para cargar imagenes
        	ArrayList queryChoosers = new ArrayList();
        	queryChoosers.add(new JaiImageQueryChooser(JAI.class,
														"Imágenes JAI",
						context.getWorkbenchContext()));
						
			DataSourceQueryChooserDialog dialog = new DataSourceQueryChooserDialog(queryChoosers,
					aplicacion.getMainFrame(),aplicacion.getI18nString(getName()),true);
						
            context.getWorkbenchContext().getWorkbench().getBlackboard().put(KEY,
                			dialog);
        }
        return (DataSourceQueryChooserDialog) context
            .getWorkbenchContext()
            .getWorkbench()
            .getBlackboard()
            .get(KEY);
    }
    public static MultiEnableCheck createEnableCheck(
            final WorkbenchContext workbenchContext) {
            GeopistaEnableCheckFactory checkFactory = new GeopistaEnableCheckFactory(workbenchContext);

            return new MultiEnableCheck().add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck());

        }

}
