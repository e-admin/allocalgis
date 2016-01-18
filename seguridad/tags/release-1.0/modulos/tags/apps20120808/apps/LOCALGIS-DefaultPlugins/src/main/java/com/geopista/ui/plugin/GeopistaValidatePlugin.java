package com.geopista.ui.plugin;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.inventario.ConstantesEIEL;
import com.geopista.editor.GeopistaEditor;
import com.geopista.editor.WorkBench;
import com.geopista.feature.AbstractValidator;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.SchemaValidator;
import com.geopista.feature.ValidationError;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.DynamicLayer;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaMapCustomConverter;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.server.administradorCartografia.ACLayerSLD;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.server.administradorCartografia.LockException;
import com.geopista.server.administradorCartografia.NoIDException;
import com.geopista.ui.GEOPISTAWorkbench;
import com.geopista.ui.dialogs.FeatureDialog;
import com.geopista.ui.dialogs.ShowEventsLogPanel;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardDialog;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.util.java2xml.XML2Java;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.CategoryEvent;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.ILayerManager;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerEvent;
import com.vividsolutions.jump.workbench.model.LayerListener;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.GeometryEditor;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class GeopistaValidatePlugin extends AbstractPlugIn
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(GeopistaValidatePlugin.class);

    /**
     * Logger for this class
     */

    private GeometryEditor editor = new GeometryEditor();

    private PlugInContext globalContext = null;

    private AbstractValidator validator = null;

    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private Blackboard blackboard = aplicacion.getBlackboard();

    private WorkbenchContext geopistaContext = null;

    private String sUrlPrefix = null;

    private AdministradorCartografiaClient administradorCartografiaClient = null;

    private int n = 0;

    private PlugInContext generalContext = null;

    private LogEvent logEvent = null;

    private HashMap events = null;

    private HashMap totalMapsLogs = new HashMap();

    private String dirBase = null;

    private boolean isDirty = false;
    
    private boolean makeInsertion = true;
    private boolean deshacerCambios = false;

    private static FeatureEventType actualEvent = null;
    
    

    /**
     * Plug-in cuya utilidad es interceptar los eventos de cambio de Features,
     * capas o categorias y aplicar reglas de validacion, en caso de que uno a
     * varios elementos implicados en la ultima validacion no cumpla algunas de
     * las validaciones impuestas se deshace toda la operacion
     */

    public void initialize(PlugInContext context) throws Exception
    {

        if (!isRegisteredPlugin(this, context))
        {

            //Registramos el manejador de eventos
            
            
            // pasamos null mientras solucionamos el problema al ser diferentes
            // el
            // GeopistaContext del
            // GeopistaWorkbenchContext
            validator = new SchemaValidator(null);

            globalContext = context;

            WorkBench frame = context.getWorkbenchContext().getIWorkbench();

            FeatureInstaller featureInstaller = new FeatureInstaller(context
                    .getWorkbenchContext());
            /*featureInstaller.addMainMenuItem(this, aplicacion.getI18nString("File"),
                    aplicacion.getI18nString(this.getName()), null,
                    createEnableCheck(context.getWorkbenchContext()));*/

            // hallamos el directorio base para almacenar los archivos log
            dirBase = aplicacion.getString("ruta.base.mapas");

            String sUrlPrefix = aplicacion.getString(Constantes.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA);
            administradorCartografiaClient = new AdministradorCartografiaClient(sUrlPrefix);

            geopistaContext = context.getWorkbenchContext();

            generalContext = context;
            
            

            LayerListener layerListener = new LayerListener()
                {

                    public void featuresChanged(FeatureEvent e)
                    {
                        Feature feature = null;
                        final Collection arlFeaturesOK = new ArrayList();
                        Collection featuresCollectionLocal = e.getFeatures();
                        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
                        
                        
                        if (e.getType() != FeatureEventType.DELETED)
                        {

                            // Iteramos sobre cada una la features para
                            // comprobar si
                            // cumple las validaciones
                            Iterator featuresCollectionLocalIter = featuresCollectionLocal
                                    .iterator();
                            ILayer capaActual = e.getLayer();
                            ILayerManager layerManager = e.getLayer().getLayerManager();
                            
                            while (featuresCollectionLocalIter.hasNext())
                            {
                                // obtenemos la feature actual y operamos sobre
                                // ella
                                feature = (Feature) featuresCollectionLocalIter.next();
                                actualEvent = e.getType();
                              
                                try
                                {
//                                    LayerManager layerManager = ((GeopistaFeature) feature).getLayer().getLayerManager();
                                    
                                    if (feature instanceof GeopistaFeature)
                                    {
                                       // Layer capaActual = ((GeopistaFeature) feature).getLayer();
                                        
                                        boolean loopContinue = true;
                                        while (loopContinue)
                                        {
                                            boolean validateResult = false;
                                            boolean geometryError = false;
                                            if (!(validateResult = validator
                                                    .validateFeature(feature)))
                                            {
                                                Iterator errorsIterator = validator
                                                        .getErrorListIterator();
                                                while (errorsIterator.hasNext())
                                                {
                                                    ValidationError actualError = (ValidationError) errorsIterator
                                                            .next();

                                                    AttributeType attributeType = feature
                                                            .getSchema()
                                                            .getAttributeType(
                                                                    actualError.attName);

                                                    if (attributeType == AttributeType.GEOMETRY)
                                                    {
                                                        geometryError = true;
                                                        break;
                                                    }
                                                }
                                                if (geometryError == false)
                                                {
                                                	boolean resultAttributes = showFeatureDialog(
                                                            feature, capaActual);
                                                    if (resultAttributes == false)
                                                    {
                                                        layerManager
                                                                .getUndoableEditReceiver()
                                                                .setAborted(
                                                                        true);
                                                        break;
                                                    }

                                                } else
                                                {
                                                    JOptionPane
                                                            .showMessageDialog(
                                                                    (Component) aplicacion
                                                                            .getMainFrame(),
                                                                    aplicacion
                                                                            .getI18nString("GeometriaNoValida"));
                                                    layerManager
                                                    .getUndoableEditReceiver()
                                                    .setAborted(
                                                            true);
                                                    break;
                                                }
                                            }

                                            if (!((GeopistaLayer) capaActual).isLocal()&&makeInsertion)
                                            {

                                                if (e.getType() == FeatureEventType.ADDED)
                                                {
                                                    Collection updateCollection = new ArrayList();
                                                    updateCollection.add(feature);
                                                    arlFeaturesOK.add(feature);
                                                    //addFeatures(updateCollection.toArray());
                                                    
                                                    break;
                                                }
                                                
                                                if (e.getType() == FeatureEventType.GEOMETRY_MODIFIED
                                                        || e.getType() == FeatureEventType.ATTRIBUTES_MODIFIED)
                                                {
                                                    try
                                                    {
                                                        Collection updateCollection = new ArrayList();
                                                        updateCollection.add(feature);
                                                        ArrayList features=updateFeatures(updateCollection.toArray());
                                                        actualizarFeaturePorRevisionExpirada(feature,features,capaActual,layerManager);

                                                        
                                                        break;
                                                    } catch (ACException e1)
                                                    {
                                                    	//Si falla al actualizar el elemento es necesario marcar
                                                    	//el elemento como dirty=false para que puedas volver 
                                                    	//a actualizarlo en caso contrario la unica solucion es cerrar
                                                    	//el editor
                                                    	 ((GeopistaFeature) feature).setDirty(false);                                                        
                                                        ErrorDialog
                                                                .show(
                                                                        (Component) generalContext.getWorkbenchGuiComponent(),
                                                                        aplicacion.getI18nString("ErrorAlActualizarFeature"),
                                                                        e1.getCause().getMessage(),StringUtil.stackTrace(e1));
                                                        try
                                                        {
                                                            throw e1.getCause();

                                                        } catch (LockException e2) // si es una lockException deshacemos la accion
                                                        {
                                                            logger.debug("featuresChanged(e = " + e + ") - Feature Bloqueada");

                                                            GeopistaFeature tempFeature = (GeopistaFeature)e.getFeatures().iterator().next();
                                                            
                                                            layerManager
                                                                    .getUndoableEditReceiver()
                                                                    .setAborted(true);
                                                            loopContinue = false;
                                                            break;

                                                        } catch (Throwable e2) // Manejo las exception que no son de bloqueo
                                                        {

                                                            boolean resultAttributes = showFeatureDialog(
                                                                    feature, capaActual);
                                                            if (resultAttributes == false)
                                                            {
                                                                loopContinue = false;
                                                                break;
                                                            }
                                                        }

                                                    }
                                                    catch (Exception e2){
                                                    	e2.printStackTrace();
                                                    }
                                                }
                                            } else
                                            {
                                                LogFeatutesEvents logger=((GeopistaLayer) capaActual).getLogger();
                                                if (logger!=null)
                                                	logger.processEvent(e);
                                                
                                                break;
                                            }
                                                
                                        }
                                    } else
                                    {
                                        if (logger.isDebugEnabled())
                                        {
                                            logger
                                                    .debug("featuresChanged(FeatureEvent e = "
                                                            + e
                                                            + ") - La Feature no es del tipo GeopistaFeature : Feature feature = "
                                                            + feature
                                                            );
                                        }
                                    }

                                } catch (Exception ex)
                                {
                                    logger.error("featuresChanged(FeatureEvent)", ex);
                                }

                            } // while
                            
                            try {
                            	
                                
                            	if (arlFeaturesOK!=null && arlFeaturesOK.size()>0){
                            		try {
                            			progressDialog.setTitle("Copiando Features...");
                            			progressDialog.report("Copiando features en la capa seleccionada");
                            			
                            			final ILayer capaActualTemp=capaActual;
                            			final ILayerManager layerManagerTemp=layerManager;
                            			progressDialog.addComponentListener(new ComponentAdapter()
                            	        {
                            	            public void componentShown(ComponentEvent e)
                            	            {
                            	                new Thread(new Runnable()
                            	                {
                            	                    public void run()
                            	                    {
                            	                        try
                            	                        {
                            	                        	ArrayList features=addFeatures(arlFeaturesOK.toArray());
                            	                        	
                            	                        	
                            	                        	//La operativa de revision expirada solo es valida cuando se actualiza un unico elemento
                            	                        	if (arlFeaturesOK.size()==1){
                            	                        		Feature feature=(Feature)((ArrayList)arlFeaturesOK).get(0);
                            	                        		actualizarFeaturePorRevisionExpirada(feature,features,capaActualTemp,layerManagerTemp);
                            	                        	}
                            	                        	deshacerCambios=false;
                            	                        }
                            	                        catch(Exception ex)
                            	                        {
                            	                            logger.error("Error al copiar los features", ex);
                    		                                ErrorDialog.show((Component) generalContext
                    		                                        .getWorkbenchGuiComponent(), aplicacion
                    		                                        .getI18nString("ErrorAlActualizarFeature"), ex
                    		                                        .getCause().getMessage(), StringUtil
                    		                                        .stackTrace(ex));
                    		                                deshacerCambios = true;
                            	                        }finally{
                            	                        	progressDialog.setVisible(false);
                            	                        }
                            	                        
                            	                    }
                            	                }).start();
                            	            }
                            	        });
                            			
                            			GUIUtil.centreOnWindow(progressDialog);
                            			progressDialog.setVisible(true);
                            			if (deshacerCambios)
                                			layerManager.getUndoableEditReceiver().setAborted(true);
                            		}catch (Exception ex){
                            			
                            		}finally {
                            			progressDialog.setVisible(false);
                            		}
                            	}
                                
                            } catch (Exception ex)
                            {
                                logger.error("featuresChanged(FeatureEvent)", ex);
                            }
                            
                            
                        } else
                        { // no es FEATUREDELETED
                            ILayer capaActual = e.getLayer();
                            if(capaActual instanceof GeopistaLayer && !((GeopistaLayer) capaActual).isLocal())
                            {
                            if(makeInsertion)
                            {
                            	logger.info("Borrando un total de :"+e.getFeatures().size()+ "elementos");
                            Iterator deleteIterator = e.getFeatures().iterator();
                            while(deleteIterator.hasNext())
                            {	
                                Feature currentDeleteFeature = (Feature) deleteIterator.next();
                                if(currentDeleteFeature instanceof GeopistaFeature)
                                {
                                     try
		                            {
		                                
		                                Collection deleteCollection = new ArrayList();
		                                deleteCollection.add(currentDeleteFeature);
	                                    deleteFeatures(deleteCollection.toArray());
		                            } catch (NoIDException e1)
		                            {
		                                // TODO: Informar al usuario del error
		                                ErrorDialog.show((Component) generalContext
		                                        .getWorkbenchGuiComponent(), aplicacion
		                                        .getI18nString("ErrorAlBorrarFeature"), e1
		                                        .getCause().getMessage(), StringUtil
		                                        .stackTrace(e1));
		                                LayerManager layerManager = (LayerManager)e.getLayer().getLayerManager();
				                           
		                                layerManager
		                                        .getUndoableEditReceiver().setAborted(
		                                                true);
		                            } catch (Exception e1)
		                            {
		                                ErrorDialog.show((Component) generalContext
		                                        .getWorkbenchGuiComponent(), aplicacion
		                                        .getI18nString("ErrorAlBorrarFeature"), e1
		                                        .getCause().getMessage(), StringUtil
		                                        .stackTrace(e1));
		                                LayerManager layerManager = (LayerManager)e.getLayer().getLayerManager();
				                           
		                                layerManager
		                                        .getUndoableEditReceiver().setAborted(
		                                                true);
		                            }
		                        }
                            }
                        }
                        }
                        
                        else
                        {
                            if (((GeopistaLayer) capaActual).getLogger()!=null)
                                ((GeopistaLayer) capaActual).getLogger().processEvent(e);

                        }
                        }

                    }

                    public void layerChanged(LayerEvent e)
                    {

                    }

                    public void categoryChanged(CategoryEvent e)
                    {
                    }
                };

            if (frame instanceof GeopistaEditor)
            {
                ((GeopistaEditor) frame).addLayerListener(layerListener);
            }

            if (frame instanceof GEOPISTAWorkbench)
            {
                ((GEOPISTAWorkbench) frame).addLayerListener(layerListener);
            }

            registerPlugin(this, context);
        }
    }
    
    
    private void actualizarFeaturePorRevisionExpirada(Feature feature, ArrayList features, ILayer capaActualTemp, ILayerManager layerManagerTemp){
        //Puede ser que la feature que enviemos haya cambiado en el servidor
        //por ejemplo par el caso de elementos temporales y publicables te devuelve
        //el elemento pero el temporal o el publicable
        try {
			String revisionExpiradaEnviada=String.valueOf((BigDecimal)feature.getAttribute("Revision Expirada"));
			if (features.size()==0)
				return;
			GeopistaFeature featureRecibida=(GeopistaFeature)features.get(0);
			String revisionExpiradaRecibida=String.valueOf((BigDecimal)featureRecibida.getAttribute("Revision Expirada"));
			
			//Si la revision va como expirada pero vuelve como temporal, borramos de la capa la version anterior
			//para reflejar el cambio en caso contrario la feature se queda exactamente igual.
			boolean actualizar=false;
			
			if (  ((revisionExpiradaEnviada==null) || (revisionExpiradaEnviada.equals("null"))) && (revisionExpiradaRecibida.equals(Const.REVISION_TEMPORAL))){
				//Inserciones
				actualizar=true;
			}
			else if ((revisionExpiradaEnviada!=null) && (revisionExpiradaEnviada.equals(Const.REVISION_VALIDA)
					&& (revisionExpiradaRecibida!=null) && (revisionExpiradaRecibida.equals(Const.REVISION_TEMPORAL)))){
				actualizar=true;				
			}
			else if ((revisionExpiradaEnviada!=null) && (revisionExpiradaEnviada.equals(Const.REVISION_TEMPORAL)
					&& (revisionExpiradaRecibida!=null) && (revisionExpiradaRecibida.equals(Const.REVISION_TEMPORAL)))){
				actualizar=true;				
			}
			else if ((revisionExpiradaEnviada!=null) && (revisionExpiradaEnviada.equals(Const.REVISION_TEMPORAL)
					&& (revisionExpiradaRecibida!=null) && (revisionExpiradaRecibida.equals(Const.REVISION_PUBLICABLE)))){
				actualizar=true;				
			}
			else if ((revisionExpiradaEnviada!=null) && (revisionExpiradaEnviada.equals(Const.REVISION_PUBLICABLE)
					&& (revisionExpiradaRecibida!=null) && (revisionExpiradaRecibida.equals(Const.REVISION_VALIDA)))){
				actualizar=true;			
			}
			else if ((revisionExpiradaEnviada!=null) && (revisionExpiradaEnviada.equals(Const.REVISION_PUBLICABLE)
					&& (revisionExpiradaRecibida!=null) && (revisionExpiradaRecibida.equals(Const.REVISION_TEMPORAL)))){
				actualizar=true;
				
			}
			
			if (actualizar){
				boolean firingEvents = layerManagerTemp.isFiringEvents();
				layerManagerTemp.setFiringEvents(false);
				
				capaActualTemp.getFeatureCollectionWrapper().remove(feature);
				capaActualTemp.getFeatureCollectionWrapper().add(featureRecibida);													 
				 									
				ACLayerSLD.actualizarReglaPintado((GeopistaLayer)capaActualTemp);
				layerManagerTemp.setFiringEvents(firingEvents);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }

    public boolean execute(PlugInContext context) throws Exception
    {
        String mapName = context.getWorkbenchContext().getTask().getName();

        try
        {
            HashMap showEvents = (HashMap) totalMapsLogs.get(mapName);

            if (showEvents == null)
            {
                File dirBaseMake = new File(dirBase, mapName);

                File file = new File(dirBaseMake, "events.log");

                PersistentLog loadEvents = null;
                FileReader reader = null;
                try
                {

                    reader = new FileReader(file);
                    XML2Java converter = new XML2Java();
                    converter.addCustomConverter(Date.class, GeopistaMapCustomConverter
                            .getMapDateCustomConverter());
                    loadEvents = (PersistentLog) converter.read(reader,
                            com.geopista.ui.plugin.PersistentLog.class);
                    showEvents = (HashMap) loadEvents.getListEvents();
                } catch (Exception ex)
                {

                } finally
                {
                    if (reader != null)
                        reader.close();
                }
                if (showEvents != null)
                {
                    totalMapsLogs.put(mapName, events);
                }
            }

            if (showEvents != null)
            {
                WizardDialog d = new WizardDialog(GeopistaFunctionUtils.getFrame(context
                        .getWorkbenchGuiComponent()), "Print Map", context.getErrorHandler());
                d.init(new WizardPanel[] { new ShowEventsLogPanel(showEvents) });

                d.setSize(750, 500);
                GUIUtil.centreOnWindow(d);
                d.setVisible(true);
                if (!d.wasFinishPressed())
                {
                    return false;
                }
            } else
            {
                JOptionPane.showMessageDialog((Component) context.getWorkbenchGuiComponent(),
                        aplicacion
                                .getI18nString("ShowEventsLogPlugIn.NoExisteArchivoLog"));
            }

        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog((Component) context.getWorkbenchGuiComponent(),
                    aplicacion.getI18nString("ShowEventsLogPlugIn.NoExisteArchivoLog"));
        }

        return true;
    }
 
    public static boolean showFeatureDialog(Feature feature, ILayer capaActual)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
    	 boolean result;
        // FeatureDialog featureDialog = new
        // FeatureDialog(GeopistaUtil.getFrame(globalContext.getWorkbenchFrame()),"Introduzca
        // el Valor de los Atributos",true,feature);
        FeatureDialog featureDialog = new FeatureDialog(AppContext
                .getApplicationContext().getMainFrame(),
                "Introduzca el Valor de los Atributos", true, feature,null,(Layer)capaActual);

        if (capaActual instanceof GeopistaLayer)
        {

            String extendedForm = ((GeopistaLayer) capaActual).getFieldExtendedForm();
            if (extendedForm == null)
                extendedForm = "";
            if (!extendedForm.equals(""))
            {
                featureDialog.setExtendedForm(extendedForm);
            }
        }
        ImageIcon icon = IconLoader.icon("logo_geopista.png");
        featureDialog.setSideBarImage(null);
        featureDialog.buildDialog();
        featureDialog.setVisible(true);
     
        if (featureDialog.wasOKPressed())
        {
            // obtenemos la feature con los cambios introducidos por el usuario
            Feature clonefeature = featureDialog.getModifiedFeature();
            // Actualiza los parámetros
            if (actualEvent == FeatureEventType.ADDED)
            {
                ((GeopistaFeature) feature).setFireDirtyEvents(false);
            }
            feature.setAttributes(clonefeature.getAttributes());
            if (actualEvent == FeatureEventType.ADDED)
            {
                ((GeopistaFeature) feature).setFireDirtyEvents(true);
            }
            result=true;
        } else
        {
            result= false;
        }

        if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_CLAVE)!=null)
				 AppContext.getApplicationContext().getBlackboard().remove(ConstantesEIEL.KEY_CLAVE);
		if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_PROV)!=null)
				AppContext.getApplicationContext().getBlackboard().remove(ConstantesEIEL.KEY_COD_PROV);
		if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_MUNIC)!=null)
			AppContext.getApplicationContext().getBlackboard().remove(ConstantesEIEL.KEY_COD_MUNIC);
		if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ORDEN)!=null)
			AppContext.getApplicationContext().getBlackboard().remove(ConstantesEIEL.KEY_COD_ORDEN);
		if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_ENTIDAD)!=null)
				AppContext.getApplicationContext().getBlackboard().remove(ConstantesEIEL.KEY_COD_ENTIDAD);
		if(AppContext.getApplicationContext().getBlackboard().get(ConstantesEIEL.KEY_COD_POBLAMIENTO)!=null)
				AppContext.getApplicationContext().getBlackboard().remove(ConstantesEIEL.KEY_COD_POBLAMIENTO);
	
        return result;

    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck());
    }

    private ArrayList addFeatures(Object[] newFeatures) throws NoIDException, ACException,
            Exception
    {
        retrylogin();
        if (aplicacion.isLogged())
        {

            Collection newFeaturesCollection = Arrays.asList(newFeatures);
            GeopistaLayer localLayer = (GeopistaLayer)((GeopistaFeature) newFeatures[0]).getLayer();
            if (localLayer instanceof DynamicLayer){
            	DynamicLayer dynamicLayer = (DynamicLayer)((GeopistaFeature) newFeatures[0]).getLayer();
	            if (dynamicLayer.isFiringEvents() == false){	
	            	return null;
	            }
            }	
	        Iterator newFeaturesCollectionIter = newFeaturesCollection.iterator();
	        while(newFeaturesCollectionIter.hasNext())
	        {
	            Feature currentFeature = (Feature) newFeaturesCollectionIter.next();
	            if(currentFeature  instanceof GeopistaFeature)
	            {
	                ((GeopistaFeature) currentFeature).setNew(true);
	                ((GeopistaFeature) currentFeature).setDeleted(false);
	                ((GeopistaFeature) currentFeature).setDirty(false);
	                //((GeopistaFeature) currentFeature).setSystemId(GeopistaFeature.SYSTEM_ID_FEATURE_SIN_INICIALIZAR);
	                    
	            }
	        }
	
	        FeatureCollection featureCollection = new FeatureDataset(
	                    newFeaturesCollection, localLayer.getFeatureCollectionWrapper()
	                            .getFeatureSchema());
	
	        GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localLayer
	                    .getDataSourceQuery().getDataSource();    
	            
	        if (geopistaServerDataSource.getProperties() != null) {
	            geopistaServerDataSource.getProperties().put("srid_destino", Integer.valueOf(localLayer.getLayerManager().getCoordinateSystem().getEPSGCode()));
	        } else {
	            Map properties = new HashMap();
	            properties.put("srid_destino", Integer.valueOf(localLayer.getLayerManager().getCoordinateSystem().getEPSGCode()));
	            geopistaServerDataSource.setProperties(properties);
	        }
	            
	        ArrayList features=geopistaServerDataSource.getConnection().executeUpdate(
	                    localLayer.getDataSourceQuery().getQuery(), featureCollection, null);
	
	        // administradorCartografiaClient.uploadFeatures(aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY,
	        // "es_ES", true), newFeatures);
	        for (int n = 0; n < newFeatures.length; n++)
	        {
	            GeopistaFeature actualFeature = (GeopistaFeature) newFeatures[n];
	            actualFeature.setNew(false);
	            actualFeature.setDirty(false);
	            if(actualFeature.isTempID())
	            {
	                ErrorDialog.show((Component) generalContext
	                            .getWorkbenchGuiComponent(), aplicacion
	                            .getI18nString("GeopistaValidatePlugin.ErrorInsertarFeature"), aplicacion
	                            .getI18nString("GeopistaValidatePlugin.ErrorAlInsertarFeature"), aplicacion
	                            .getI18nString("GeopistaValidatePlugin.FeatureOtraCapa"));
	            }
            }
	        return features;
        }
        
        return null;

    }

    public static void deleteFeatures(Object[] deleteFeatures) throws NoIDException, Exception,
            ACException
    {
        ArrayList deleteArrayListFeatures = new ArrayList();
        for (int n = 0; n < deleteFeatures.length; n++)
        {
            ((GeopistaFeature) deleteFeatures[n]).setDeleted(true);
            ((GeopistaFeature) deleteFeatures[n]).setNew(false);
            // si la feature no tiene idSystem no se efectua el borrado
            // porque la feature aun no
            // ha sido introducida en la base de datos
            if (((GeopistaFeature) deleteFeatures[n]).getSystemId() != null
                    && (!((GeopistaFeature) deleteFeatures[n]).isTempID()))
                deleteArrayListFeatures.add((GeopistaFeature) deleteFeatures[n]);

        }
        // Object[] finalDeleteFeatures = deleteArrayListFeatures.toArray();
        if (deleteArrayListFeatures.size() > 0)
        {
            retrylogin();

            GeopistaLayer localLayer = (GeopistaLayer)((GeopistaFeature) deleteArrayListFeatures
                    .iterator().next()).getLayer();

            FeatureCollection featureCollection = new FeatureDataset(
                    deleteArrayListFeatures, localLayer.getFeatureCollectionWrapper()
                            .getFeatureSchema());

            CoordinateSystem inCoord = localLayer.getLayerManager().getCoordinateSystem();
            GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localLayer
            .getDataSourceQuery().getDataSource();
            DriverProperties driverProperties = ((GeopistaConnection)geopistaServerDataSource.getConnection()).getDriverProperties();
            driverProperties.put("srid_destino",inCoord.getEPSGCode());
            new GeopistaConnection(driverProperties).executeUpdate(
                    localLayer.getDataSourceQuery().getQuery(), featureCollection, null);

            // administradorCartografiaClient.uploadFeatures(aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY,
            // "es_ES", true), finalDeleteFeatures);
        }
    }

    public static ArrayList updateFeatures(Object[] updateFeatures) throws NoIDException,
            ACException, Exception
    {
        AppContext aplicacionLocal = (AppContext) AppContext.getApplicationContext();
        if (aplicacionLocal.isLogged())
        {

            retrylogin();
            if (aplicacionLocal.isLogged())
            { 
                Collection newFeaturesCollection = Arrays.asList(updateFeatures);
                GeopistaLayer localLayer = (GeopistaLayer)((GeopistaFeature) updateFeatures[0])
                        .getLayer();
                
                Iterator newFeaturesCollectionIter = newFeaturesCollection.iterator();
                while(newFeaturesCollectionIter.hasNext())
                {
                    Feature currentFeature = (Feature) newFeaturesCollectionIter.next();
                    if(currentFeature  instanceof GeopistaFeature)
                    {
                        ((GeopistaFeature) currentFeature).setNew(false);
                        ((GeopistaFeature) currentFeature).setDeleted(false);
                        ((GeopistaFeature) currentFeature).setDirty(true);
                    }
                }

                FeatureCollection featureCollection = new FeatureDataset(
                        newFeaturesCollection, localLayer.getFeatureCollectionWrapper()
                                .getFeatureSchema());

                GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) localLayer
                        .getDataSourceQuery().getDataSource();
                
                
                if (geopistaServerDataSource.getProperties() != null) {
                    geopistaServerDataSource.getProperties().put("srid_destino", Integer.valueOf(localLayer.getLayerManager().getCoordinateSystem().getEPSGCode()));
                } else {
                    Map properties = new HashMap();
                    properties.put("srid_destino", Integer.valueOf(localLayer.getLayerManager().getCoordinateSystem().getEPSGCode()));
                    geopistaServerDataSource.setProperties(properties);
                }
                
                return geopistaServerDataSource.getConnection().executeUpdate(
                        localLayer.getDataSourceQuery().getQuery(), featureCollection,
                        null);

               /* aplicacionLocal.getClient().uploadFeatures(
                        aplicacionLocal.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY,
                                "es_ES", true), updateFeatures);*/
            }

        }
        return null;

    }

    private static void retrylogin()
    {
        AppContext aplicacionLocal = (AppContext) AppContext.getApplicationContext();
        boolean userLogged = false;
        while (true)
        {
            aplicacionLocal.login();
            if (aplicacionLocal.isLogged())
            {
                break;
            } else
            {
                Object[] options = { aplicacionLocal.getI18nString("OKCancelPanel.OK"),
                        aplicacionLocal.getI18nString("OKCancelPanel.Cancel") };
                int confirmResult = JOptionPane.showOptionDialog(
                        (Component) aplicacionLocal.getMainFrame(), aplicacionLocal
                                .getI18nString("ReintentarAnularOperacion"), null,
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        options, options[0]);
                if (confirmResult == 1)
                    throw new SecurityException();
            }
        }
    }

    /**
     * @return Returns the makeInsertion.
     */
    public boolean isMakeInsertion()
    {
        return makeInsertion;
    }
    /**
     * @param makeInsertion The makeInsertion to set.
     */
    public void setMakeInsertion(boolean makeInsertion)
    {
        this.makeInsertion = makeInsertion;
    }
}