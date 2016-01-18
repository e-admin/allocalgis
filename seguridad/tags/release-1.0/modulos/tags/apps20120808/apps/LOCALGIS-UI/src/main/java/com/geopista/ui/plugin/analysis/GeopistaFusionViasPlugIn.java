/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
 */

package com.geopista.ui.plugin.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.editor.WorkbenchGuiComponent;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.io.datasource.GeopistaConnection;
import com.geopista.io.datasource.GeopistaServerDataSource;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.GeopistaSystemLayers;
import com.geopista.protocol.control.ISesion;
import com.geopista.ui.images.IconLoader;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaFunctionUtils;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.io.DriverProperties;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

/**
 * GeopistaFusionViasPlugIn clase que sirve para fusionar la capa de tramos vía
 * con vías
 */

public class GeopistaFusionViasPlugIn extends AbstractPlugIn implements ThreadedPlugIn
{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(GeopistaFusionViasPlugIn.class);

    private static final String NO_ESPECIFICADO = "NE";

    private static ApplicationContext aplicacion = AppContext.getApplicationContext();

    private Layer capaTramosVias = null;

    private Layer capaVias = null;

    private String numViaTramosViaAttributeName = null;

    private String idViaTramosViaAttributeName = null;

    private String idRoutesAttributeName = null;

    private Hashtable targetHash = null;

    private TaskMonitor globalMonitor = null;

    public GeopistaFusionViasPlugIn()
        {
        }

    public void initialize(PlugInContext context) throws Exception
    {
        String toolBarCategory = "GeopistaFusionViasPlugIn.category";
        String pluginCategory = aplicacion.getString(toolBarCategory);
        ((WorkbenchGuiComponent) context.getWorkbenchContext().getIWorkbench().getGuiComponent())
                .getToolBar(pluginCategory).addPlugIn(this.getIcon(), this,
                        createEnableCheck(context.getWorkbenchContext()),
                        context.getWorkbenchContext());
    }

    public boolean execute(PlugInContext context) throws Exception
    {

        capaTramosVias = context.getLayerManager().getLayer(
                GeopistaSystemLayers.TRAMOSVIAS);
        capaVias = context.getLayerManager().getLayer(GeopistaSystemLayers.VIAS);

        if (capaTramosVias == null || capaVias == null)
        {
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("GeopistaFusionViasPlugIn.noLayersMessage"));
            return false;
        }

        MultiInputDialog dialog = initDialog(context);
        dialog.setVisible(true);

        if (!dialog.wasOKPressed())
        {
            return false;
        }

        return true;
    }

    /**
     * initDialog(PlugInContext context) Inicializa un cuadro de diálogo que
     * avisa al usuario de la operación a realizar
     *
     * @param context :
     *            Conexto de PlugIn
     * @return No devuelve nada
     */

    private MultiInputDialog initDialog(PlugInContext context)
    {

        MultiInputDialog dialog = new MultiInputDialog(GeopistaFunctionUtils.getFrame(context
                .getWorkbenchGuiComponent()), GeopistaUtil.i18n_getname("Union"), true);

        dialog.setSideBarImage(IconLoader.icon("fusionar_features.gif"));
        dialog.setSideBarDescription(aplicacion
                .getI18nString("geopistaFusionViasDialogText"));

        GUIUtil.centreOnWindow(dialog);
        return dialog;
    }

        /**
	     * run(TaskMonitor monitor, PlugInContext context) Ejecuta el plugin
	     * mostrando un contador dado que es una operación que requiere bastante
	     * tiempo
	     *
	     * @param monitor :
	     *            cuadro que monitoriza la operación y se muestra durante su
	     *            ejecución
	     * @param context :
	     *            Contexto del PlugIn
	     * @return No devuelve nada
	     */
	    public void run(TaskMonitor monitor, PlugInContext context) throws Exception
	    {
	        globalMonitor = monitor;
	        // Hashtable con las vias para insertar en la base de datos
	        targetHash = new Hashtable();


	        if (capaTramosVias.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema && capaVias.getFeatureCollectionWrapper().getFeatureSchema() instanceof GeopistaSchema)
	        {
	            // lista de features de la capa tramos vias
	            List sourceFeatures = capaTramosVias.getFeatureCollectionWrapper()
	                    .getFeatures();
	            // sacamos el esquema de la capa origen
	            GeopistaSchema sourceFeatureSchema = (GeopistaSchema) capaTramosVias
	                    .getFeatureCollectionWrapper().getFeatureSchema();
	            // sacamos el esquema de la capa vias
	            GeopistaSchema targetSchema = (GeopistaSchema) capaVias
	                    .getFeatureCollectionWrapper().getFeatureSchema();

	            // buscamos el nombre de los attributos que vamos a utilizar
	            String numviaAttributeName = sourceFeatureSchema
	                    .getAttributeByColumn("numvia");
	            String codigoCatastroAttributeName = targetSchema
	                    .getAttributeByColumn("codigocatastro");
	            String nombreCatastroAttributeName = targetSchema
	                    .getAttributeByColumn("nombrecatastro");
	            String tipoViaNormalizadoCatastroAttributeName = targetSchema
	                    .getAttributeByColumn("tipovianormalizadocatastro");
	            String tipoViaIneAttributeName = targetSchema
	                    .getAttributeByColumn("tipoviaine");
	            numViaTramosViaAttributeName = sourceFeatureSchema
	                    .getAttributeByColumn("numvia");
	            idViaTramosViaAttributeName = sourceFeatureSchema
	                    .getAttributeByColumn("id_via");
	            idRoutesAttributeName = targetSchema.getAttributeByColumn("id");

	            // comenzamos la iteracion por cada uno de los tramos de vias
	            Iterator sourceFeaturesIter = sourceFeatures.iterator();
	            int currentFusion = 1;
	            monitor.report(aplicacion
	                    .getI18nString("ProcesandoTramosVia"));
	            while (sourceFeaturesIter.hasNext())
	            {
	                monitor.report(currentFusion++, sourceFeatures.size(), aplicacion
	                        .getI18nString("GeopistaFusionVias"));
	                Object currentSourceFeature = sourceFeaturesIter.next();

	                if (currentSourceFeature instanceof GeopistaFeature)
	                {

	                    GeopistaFeature currentSourceGeopistaFeature = (GeopistaFeature) currentSourceFeature;
	                    String currentNumViaValue = currentSourceGeopistaFeature
	                            .getString(numviaAttributeName);

	                    try
	                    {
	                        // comprobamos con esta conversión si el Numero de vía
	                        // es realmente
	                        // un entero y si no nos saltamos la iteración
	                        Integer.parseInt(currentNumViaValue);

	                        // buscamos el numero de via en el hashtable y si no
	                        // existe lo insertamos, si ya existe realizamos
	                        // la fusion de vias.

	                        Hashtable featureData = (Hashtable) targetHash
	                                .get(currentNumViaValue);
	                        if (featureData == null)
	                        {
	                            	ArrayList geometryList = new ArrayList();
	                            	featureData = new Hashtable();
	                            	Geometry sourceGeom1 = currentSourceGeopistaFeature.getGeometry();
	                            	String denominacion = (String) currentSourceGeopistaFeature.getAttribute(sourceFeatureSchema.getAttributeByColumn("denominacion"));
	                            	if(denominacion!=null)
	                            	{
	                            	    featureData.put("denominacion",denominacion);
	                            	}
	                            	geometryList.add(sourceGeom1);

	                            	featureData.put("geometria",geometryList);

	                                targetHash.put(currentNumViaValue, featureData);
	                        } else
	                        {
	                            Geometry sourceGeom1 = currentSourceGeopistaFeature
	                                    .getGeometry();
	                            ArrayList geometryList = (ArrayList) featureData.get("geometria");
	                            geometryList.add(sourceGeom1);
	                        }
	                    } catch (Exception e)
	                    {
	                        // si encontramos algún error pasamos a la siguienet
	                        // feature
	                        logger.error("run(TaskMonitor, PlugInContext)", e);
	                    }

	                }
	            }


	            Set targetHashKeys = targetHash.keySet();
	            Iterator targetHashIterator =  targetHashKeys.iterator();
	            GeometryFactory gf=AppContext.getApplicationContext().getGeometryFactory();

	            while(targetHashIterator.hasNext())
	            {
	                String currentIdVia = null;
	                try
	                {
	                currentIdVia = (String) targetHashIterator.next();

	                Hashtable currentFeatureData = (Hashtable) targetHash.get(currentIdVia);
	                ArrayList currentGeometryList = (ArrayList) currentFeatureData.get("geometria");



	                /**En postgres las geometría de TramosVia son de tipo LineString, mientras que en oracle son MultiLineString.
	                 * Por tanto cuando llegamos a este punto del código en el caso de postgres la currentGeometryList será una lista
	                 * de LineString, mientras que en el caso de oracle será una lista de MultiLineString.
	                 */
	                MultiLineString calle=null;
	                try{
	                	//PARA POSTGRES
	                LineString[] calleArray=new LineString[0];
	            	calle=gf.createMultiLineString((LineString[]) currentGeometryList.toArray(calleArray));
	                }catch(ArrayStoreException e){
	                	//PARA ORACLE
	                	 MultiLineString[] calleArray=new MultiLineString[0];
	                	 calle=((MultiLineString[]) currentGeometryList.toArray(calleArray))[0];
	                }






	                GeopistaFeature targetFeature = new GeopistaFeature(targetSchema);

	                // copiamos los atributos de una capa a otra
	                // numvia pasa a Codigo Catastro

	                targetFeature.setAttribute(codigoCatastroAttributeName,
	                        currentIdVia);


	                // Denominacion pasa a Nombre Catastro
	                targetFeature.setAttribute(nombreCatastroAttributeName,
	                        currentFeatureData.get("denominacion"));

	                // En tipo via normalizado catastro introducimos
	                // el valor NE (No Especificado) hasta
	                // que sepamos el valor real
	                targetFeature.setAttribute(
	                        tipoViaNormalizadoCatastroAttributeName,
	                        NO_ESPECIFICADO);

	                // En tipo via INEdo catastro introducimos el
	                // valor NE (No Especificado) hasta
	                // que sepamos el valor real
	                targetFeature.setAttribute(tipoViaIneAttributeName,
	                        NO_ESPECIFICADO);

	                // La geometria es la misma
	                targetFeature.setGeometry(calle);
	                targetHash.put(currentIdVia,targetFeature);
	                if (capaVias instanceof GeopistaLayer)
	                {
	                    targetFeature.setLayer((GeopistaLayer) capaVias);
	                }
	                } catch (Exception e)
	                {
	                    targetHash.remove(currentIdVia);
	                    // si encontramos algún error pasamos a la siguienet
	                    // feature
	                    logger.error("run(TaskMonitor, PlugInContext)", e);
	                }

	            }



	        }

	        execute(new UndoableCommand(getName())
	            {
	                // creamos un nuevo hashtable con los elementos para poder
	                // tenerlos disponibles
	                // a la hora de deshacer la operacion

	                Hashtable localTargetHash = new Hashtable(targetHash);

	                public void execute()
	                {
	                    int currentPosition = 0;
	                    Collection insertKeysFeatures = localTargetHash.keySet();
	                    Iterator insertKeysFeaturesIter = insertKeysFeatures.iterator();
	                    boolean isFiringEventsCapaVias = capaVias.getLayerManager()
	                            .isFiringEvents();
	                    capaVias.getLayerManager().setFiringEvents(false);
	                    try
	                    {
	                        while (insertKeysFeaturesIter.hasNext())
	                        {
	                            if (globalMonitor != null)
	                            {
	                                globalMonitor
	                                        .report(
	                                                currentPosition++,
	                                                insertKeysFeatures.size(),
	                                                aplicacion
	                                                        .getI18nString("GeopistaMostrarCallejeroPanel.CreandoVia"));
	                            }
	                            Object currentKey = insertKeysFeaturesIter.next();
	                            GeopistaFeature currentInsertFeature = (GeopistaFeature) localTargetHash
	                                    .get(currentKey);

	                            capaVias.getFeatureCollectionWrapper().add(
	                                    currentInsertFeature);

	                        }

	                    } finally
	                    {
	                        capaVias.getLayerManager()
	                                .setFiringEvents(isFiringEventsCapaVias);
	                    }

	                    GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capaTramosVias
	                            .getDataSourceQuery().getDataSource();

	                    try
	                    {
	                    	GeopistaConnection geoConn = (GeopistaConnection)geopistaServerDataSource.getConnection();
							ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
	                    	String sridDestino = geoConn.getSRIDDefecto(true, Integer.parseInt(iSesion.getIdEntidad()));
	                    	DriverProperties driverPropertiesUpdate = geoConn.getDriverProperties();
	                    	driverPropertiesUpdate.put("srid_destino",new Integer(sridDestino));
	                    	driverPropertiesUpdate.put("srid_inicial",new Integer(sridDestino));
	                    	new GeopistaConnection(driverPropertiesUpdate).executeUpdate(
	                                capaVias.getDataSourceQuery().getQuery(),
	                                capaVias.getFeatureCollectionWrapper(), globalMonitor);
	                    } catch (Exception e)
	                    {
	                        // como no podemos lanzar Exception la paramos aqui.
	                        logger.error("execute()", e);
	                    }

	                    if (globalMonitor != null)
	                    {
	                        globalMonitor
	                                .report(
	                                        currentPosition++,
	                                        insertKeysFeatures.size(),
	                                        aplicacion
	                                                .getI18nString("GeopistaMostrarCallejeroPanel.ProcesandoVias"));
	                    }

	                    currentPosition = 0;
	                    insertKeysFeatures = localTargetHash.keySet();
	                    insertKeysFeaturesIter = insertKeysFeatures.iterator();

	                    while (insertKeysFeaturesIter.hasNext())
	                    {
	                        Object currentKey = insertKeysFeaturesIter.next();

	                        GeopistaFeature currentInsertFeature = (GeopistaFeature) localTargetHash
	                                .get(currentKey);

	                        // buscamos todos los tramos con el mismo numero de via
	                        // para insertar en el
	                        // attributo id de via el valor devuelto por el
	                        // administrador de cartografia
	                        Collection sourceFeaturesWithEqualsIdVia = GeopistaFunctionUtils
	                                .searchByAttribute(capaTramosVias,
	                                        numViaTramosViaAttributeName, currentKey
	                                                .toString());
	                        Iterator sourceFeaturesWithEqualsIdViaIter = sourceFeaturesWithEqualsIdVia
	                                .iterator();
	                        boolean isFiringEvents = capaTramosVias.getLayerManager()
	                                .isFiringEvents();
	                        capaTramosVias.getLayerManager().setFiringEvents(false);
	                        try
	                        {
	                            while (sourceFeaturesWithEqualsIdViaIter.hasNext())
	                            {
	                                Object currentSectionsRoutesFeatureObject = sourceFeaturesWithEqualsIdViaIter
	                                        .next();
	                                if (currentSectionsRoutesFeatureObject instanceof GeopistaFeature)
	                                {
	                                    GeopistaFeature currentSectionsRoutesFeature = (GeopistaFeature) currentSectionsRoutesFeatureObject;
	                                    currentSectionsRoutesFeature.setAttribute(idViaTramosViaAttributeName,
	                                            currentInsertFeature.getAttribute(idRoutesAttributeName));
	                                    currentSectionsRoutesFeature.setDirty(true);
	                                    currentSectionsRoutesFeature.setNew(false);
	                                }

	                            }

	                        } finally
	                        {
	                            capaTramosVias.getLayerManager().setFiringEvents(
	                                    isFiringEvents);
	                        }
	                    }

	                    try
	                    {
	                    	GeopistaConnection geoConn = (GeopistaConnection)geopistaServerDataSource.getConnection();
							ISesion iSesion = (ISesion)AppContext.getApplicationContext().getBlackboard().get(AppContext.SESION_KEY);
	                    	String sridDestino = geoConn.getSRIDDefecto(true, Integer.parseInt(iSesion.getIdEntidad()));
	                    	DriverProperties driverPropertiesUpdate = geoConn.getDriverProperties();
	                    	driverPropertiesUpdate.put("srid_destino",sridDestino);
	                    	driverPropertiesUpdate.put("srid_inicial",new Integer(sridDestino));
	                    	new GeopistaConnection(driverPropertiesUpdate).executeUpdate(
	                                capaTramosVias.getDataSourceQuery().getQuery(),
	                                capaTramosVias.getFeatureCollectionWrapper(),
	                                globalMonitor);
	                    } catch (Exception e)
	                    {
	                        // como no podemos lanzar Exception la paramos aqui.
	                        logger.error("execute()", e);
	                    }
	                }

	                public void unexecute()
	                {
	                    Collection insertKeysFeatures = localTargetHash.keySet();
	                    Iterator insertKeysFeaturesIter = insertKeysFeatures.iterator();
	                    while (insertKeysFeaturesIter.hasNext())
	                    {
	                        Object currentKey = insertKeysFeaturesIter.next();
	                        GeopistaFeature currentInsertFeature = (GeopistaFeature) localTargetHash
	                                .get(currentKey);

	                        capaVias.getFeatureCollectionWrapper().remove(
	                                currentInsertFeature);
	                        // buscamos todos los tramos con el mismo numero de via
	                        // para insertar en el
	                        // attributo id de via el valor devuelto por el
	                        // administrador de cartografia
	                        Collection sourceFeaturesWithEqualsIdVia = GeopistaFunctionUtils
	                                .searchByAttribute(capaTramosVias,
	                                        numViaTramosViaAttributeName, currentKey
	                                                .toString());
	                        Iterator sourceFeaturesWithEqualsIdViaIter = sourceFeaturesWithEqualsIdVia
	                                .iterator();
	                        boolean isFiringEvents = capaTramosVias.getLayerManager()
	                                .isFiringEvents();
	                        capaTramosVias.getLayerManager().setFiringEvents(false);
	                        while (sourceFeaturesWithEqualsIdViaIter.hasNext())
	                        {
	                            Object currentSectionsRoutesFeatureObject = sourceFeaturesWithEqualsIdViaIter
	                                    .next();
	                            if (currentSectionsRoutesFeatureObject instanceof GeopistaFeature)
	                            {
	                                GeopistaFeature currentSectionsRoutesFeature = (GeopistaFeature) currentSectionsRoutesFeatureObject;
	                                currentSectionsRoutesFeature.setAttribute(
	                                        idViaTramosViaAttributeName, null);
	                            }

	                        }
	                        capaTramosVias.getLayerManager().setFiringEvents(isFiringEvents);

	                        GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) capaTramosVias
	                                .getDataSourceQuery().getDataSource();
	                        FeatureCollection featureCollection = new FeatureDataset(
	                                sourceFeaturesWithEqualsIdVia, capaTramosVias
	                                        .getFeatureCollectionWrapper().getFeatureSchema());

	                        try
	                        {
	                            geopistaServerDataSource.getConnection().executeUpdate(
	                                    capaTramosVias.getDataSourceQuery().getQuery(),
	                                    featureCollection, null);
	                        } catch (Exception e)
	                        {
	                            // como no podemos lanzar Exception la paramos aqui.
	                            logger.error("execute()", e);
	                        }

	                    }
	                }
	            }, context);
	        JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
	                .getI18nString("Inforeferencia.GuardarCambios2"));  

	    }

    /**
     * Este Método comprueba los permisos para uso del PLUGIN
     *
     * @param workbenchContext
     *            Es el contexto de aplicación
     * @return Devuelve null si no se cumple las condiciones
     */
    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext)
    {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerManagerMustBeActiveCheck()).add(
                checkFactory.createWindowWithAssociatedTaskFrameMustBeActiveCheck()).add(
                checkFactory.createAtLeastNLayersMustExistCheck(2));
    }

    public ImageIcon getIcon()
    {
        return IconLoader.icon("fusionar_features.gif");
    }
}
