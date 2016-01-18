/*
 * The Unified Mapping Platform (JUMP) is an extensible, interactive GUI
 * for visualizing and manipulating spatial features with geometry and attributes.
 *
 * Copyright (C) 2003 Vivid Solutions
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * Vivid Solutions
 * Suite #1A
 * 2328 Government Street
 * Victoria BC  V8T 5G5
 * Canada
 *
 * (250)385-6040
 * www.vividsolutions.com
 */
package com.vividsolutions.jump.workbench.datasource;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.model.IGeopistaLayer;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jts.util.AssertionFailedException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.coordsys.CoordinateSystemRegistry;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.CollectionUtil;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedBasePlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrameImpl;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.plugin.PersistentBlackboardPlugIn;

/**
 * Prompts the user to pick a dataset to load.
 * @see DataSourceQueryChooserDialog
 */
public class LoadDatasetPlugIn extends ThreadedBasePlugIn {
    private static String LAST_FORMAT_KEY = LoadDatasetPlugIn.class.getName() +
        " - LAST FORMAT";
    private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();


    /**
     * Crea un dialogo (FileChooser) que permite elegir un fichero
     * que represente el origen de datos (Dataset) que se quiere
     * añadir al proyecto.
     * */
    private DataSourceQueryChooserDialog getDialog(PlugInContext context) {

        //primero mira si ya existe en memoria
        String KEY = getClass().getName() + " - DIALOG";
        if (null == context.getWorkbenchContext().getIWorkbench().getBlackboard()
                               .get(KEY)) {
            context.getWorkbenchContext().getIWorkbench().getBlackboard().put(KEY,
                new DataSourceQueryChooserDialog(DataSourceQueryChooserManager.get(
                        context.getWorkbenchContext().getIWorkbench()
                               .getBlackboard()).getLoadDataSourceQueryChoosers(),
                    aplicacion.getMainFrame(), aplicacion.getI18nString(getName()), true));
        }

        return (DataSourceQueryChooserDialog) context.getWorkbenchContext()
                                                     .getIWorkbench()
                                                     .getBlackboard().get(KEY);
    }
    
    public String getName() {
        //Suggest that multiple datasets may be loaded [Jon Aquino 11/10/2003]
        return "Load Dataset(s)";
    }

    public void initialize(final PlugInContext context) throws Exception {
        //Give other plug-ins a chance to add DataSourceQueryChoosers
        //before the dialog is realized. [Jon Aquino]
        
    	if (!(context.getWorkbenchGuiComponent() instanceof GeopistaEditor)){
    		context.getWorkbenchGuiComponent().addWindowListener(new WindowAdapter() {
    			public void windowOpened(WindowEvent e) {
    				String format = (String) PersistentBlackboardPlugIn.get(context.getWorkbenchContext())
    				.get(LAST_FORMAT_KEY);
    				if (format != null) {
    					getDialog(context).setSelectedFormat(format);
    				}
    			}
    		});
    	}
    	else{

    		FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());

    		featureInstaller.addPopupMenuItem(context.getWorkbenchContext().getIWorkbench()
    				.getGuiComponent()
    				.getCategoryPopupMenu(),
    				this, I18N.get(this.getName()) + "...", false,
    				null, null);

    		featureInstaller.addLayerViewMenuItem(this,  
    				new String[]{MenuNames.LAYER,
    		"ImageCoverage"}, 
    		I18N.get(this.getName()));      
    	}
    }

    /**
     * Implementacion de la interfaz PlugIn.
     * En este caso, simplemente crea un dialogo modal de seleccion de origen de datos,
     * y lo centra en pantalla (retornando finalmente si se selecciono un fichero)
     * */
    public boolean execute(PlugInContext context) throws Exception {
        GUIUtil.centreOnWindow(getDialog(context));
        getDialog(context).setVisible(true);
        if (getDialog(context).wasOKPressed()) {
            PersistentBlackboardPlugIn.get(context.getWorkbenchContext()).put(LAST_FORMAT_KEY,
			getDialog(context).getSelectedFormat());
        }

        return getDialog(context).wasOKPressed();
    }

    public void run(TaskMonitor monitor, PlugInContext context)
        throws Exception {
        try
			{
			Assert.isTrue(!getDialog(context).getCurrentChooser()
					.getDataSourceQueries().isEmpty());
			}
		catch (AssertionFailedException e)
			{
			   throw new AssertionFailedException(I18N.get("FileEmpty"));
			   
			}

     
        boolean exceptionsEncountered = false;
        for (Iterator i = getDialog(context).getCurrentChooser()
                              .getDataSourceQueries().iterator(); i.hasNext();) {
            DataSourceQuery dataSourceQuery = (DataSourceQuery) i.next();
            boolean layerRepeated = false;
            List allLayerList = context.getLayerManager().getLayers();
            Iterator allLayerListIterator = allLayerList.iterator();
            while(allLayerListIterator.hasNext())
            {
                Layer currentLayer = (Layer) allLayerListIterator.next();
                if(currentLayer.getDataSourceQuery() == null) continue;
                if(currentLayer.getDataSourceQuery().getDataSource() == null) continue;
                Map currentLayerProperties = currentLayer.getDataSourceQuery().getDataSource().getProperties();
                String currentFileKey = (String) currentLayerProperties.get(DataSource.FILE_KEY);
                Map insertLayerProperties = dataSourceQuery.getDataSource().getProperties();
                String insertFileKey = (String) insertLayerProperties.get(DataSource.FILE_KEY);
                if(insertFileKey!=null && currentFileKey!=null && insertFileKey.trim().equals(currentFileKey.trim()))
                {
                    layerRepeated=true;
                    break;
                }
                
            }
            
            if(layerRepeated) continue;
            
            ArrayList exceptions = new ArrayList();
            Assert.isTrue(dataSourceQuery.getDataSource().isReadable());
            monitor.report(aplicacion.getI18nString("LoadDatasetPlugIn.Loading") + " " + dataSourceQuery.toString() + "...");

            Connection connection = dataSourceQuery.getDataSource()
                                                   .getConnection();
            try {
                FeatureCollection dataset = dataSourceQuery.getDataSource().installCoordinateSystem(connection.executeQuery(dataSourceQuery.getQuery(),
                        exceptions, monitor), CoordinateSystemRegistry.instance(context.getWorkbenchContext().getBlackboard()));
                if (dataset != null) {
                    ILayer currentLayer = context.getLayerManager()
                           .addLayer(chooseCategory(context),
                        dataSourceQuery.toString(), dataset)
                           .setDataSourceQuery(dataSourceQuery)
                           .setFeatureCollectionModified(false);
                    
                    for(Iterator features = dataset.getFeatures().iterator();features.hasNext();){
                    	GeopistaFeature feature = (GeopistaFeature)features.next();
                    	feature.setLayer((IGeopistaLayer)currentLayer);
                    }
                    
                    
                    if(getDialog(context).getCurrentChooser().getClass().getName().equals("GeopistaLoadDxfQueryChooser.class"))
                    {
                        if(currentLayer instanceof GeopistaLayer)
                        {
                            IGeopistaLayer currentGeopistaLayer = (IGeopistaLayer) currentLayer;
                            String logFilePath = (String)dataSourceQuery.getDataSource().getProperties().get(Constantes.ORIGINAL_FILE_KEY);
                            currentGeopistaLayer.activateLogger(logFilePath);
                         }
                    }
                }
            } finally {
                connection.close();
            }
            if (!exceptions.isEmpty()) {
                if (!exceptionsEncountered) {
                    context.getOutputFrame().createNewDocument();
                    exceptionsEncountered = true;
                }
                reportExceptions(exceptions, dataSourceQuery, context);
            }
        }
        if (exceptionsEncountered) {
            context.getWorkbenchGuiComponent().warnUser("Problems were encountered. See Output Window for details.");
        }
    }

    private void reportExceptions(ArrayList exceptions,
        DataSourceQuery dataSourceQuery, PlugInContext context) {
        context.getOutputFrame().addHeader(1,
            exceptions.size() + " problem" + StringUtil.s(exceptions.size()) +
            " loading " + dataSourceQuery.toString() + "." +
            ((exceptions.size() > 10) ? " First and last five:" : ""));
        context.getOutputFrame().addText("See View / Log for stack traces");
        context.getOutputFrame().append("<ul>");

        Collection exceptionsToReport = exceptions.size() <= 10 ? exceptions
                                                                : CollectionUtil.concatenate(Arrays.asList(
                    new Collection[] {
                        exceptions.subList(0, 5),
                        exceptions.subList(exceptions.size() - 5,
                            exceptions.size())
                    }));
        for (Iterator j = exceptionsToReport.iterator(); j.hasNext();) {
            Exception exception = (Exception) j.next();
            context.getWorkbenchGuiComponent().log(StringUtil.stackTrace(exception));
            context.getOutputFrame().append("<li>");
            context.getOutputFrame().append(GUIUtil.escapeHTML(
                    WorkbenchFrameImpl.toMessage(exception), true, true));
            context.getOutputFrame().append("</li>");
        }
        context.getOutputFrame().append("</ul>");
    }

    private String chooseCategory(PlugInContext context) {
        return context.getLayerNamePanel().getSelectedCategories().isEmpty()
        ? StandardCategoryNames.WORKING
        : context.getLayerNamePanel().getSelectedCategories().iterator().next()
                 .toString();
    }

    public static MultiEnableCheck createEnableCheck(
        final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerManagerMustBeActiveCheck());
    }
}
