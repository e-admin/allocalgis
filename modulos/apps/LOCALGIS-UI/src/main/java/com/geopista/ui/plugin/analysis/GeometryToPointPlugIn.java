/**
 * GeometryToPointPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 11-ago-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.geopista.ui.plugin.analysis;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import com.geopista.app.AppContext;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;

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

/**
 * @author jalopez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GeometryToPointPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    
    private MultiInputDialog dialog;

    public GeometryToPointPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
    I18N.setPlugInRessource(this.getName(),"GeoPistai18n");
      FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
      EnableCheckFactory checkFactory = new EnableCheckFactory(context.getWorkbenchContext());
           featureInstaller.addMainMenuItem(this,
            MenuNames.TOOLS_ANALYSIS,
            I18N.get(this.getName(),"GeometryToPoint") + "...", false, null,
            new MultiEnableCheck().add(
                checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                  .add(checkFactory.createAtLeastNLayersMustBeSelectedCheck(
                    1)));
    }
   
    public boolean execute(PlugInContext context) throws Exception {
    Layer[] selectedLayers = context.getSelectedLayers();
    String layers=selectedLayers[0].getName();
    for (int i = 1; i < selectedLayers.length; i++)
		{
		Layer layer = selectedLayers[i];
		layers=layers+","+layer.getName();
		}
    	int resp=JOptionPane.showConfirmDialog(context.getWorkbenchFrame(),
    			I18N.getMessage(this.getName(),"GeometryToPointPlugIn.RealizandoCoversionAPuntosConfirm",
    			new Object[]{new Integer(selectedLayers.length),layers}),
    			I18N.get(this.getName(),"GeometryToPoint"),JOptionPane.OK_CANCEL_OPTION);
    	if (resp==JOptionPane.YES_OPTION)
    		return true;
    	else
        return false;
    }

    

    public void run(final TaskMonitor monitor, PlugInContext context)
        throws Exception {
        
        final Layer[] selectedLayers = context.getSelectedLayers();
        
        final Hashtable layersGeometries = new Hashtable();
        
        final PlugInContext localPluginContext = context;
        
        execute(new UndoableCommand(getName())
        {
            

            public void execute()
            {
                for(int n=0;n<selectedLayers.length;n++)
                {
                    Layer currentLayer = selectedLayers[n];
                    List allLayerFeatures = currentLayer.getFeatureCollectionWrapper().getFeatures();
                    
                    Hashtable oldGeometries = convertGeometryToPoint(monitor, allLayerFeatures, currentLayer);
                    layersGeometries.put(currentLayer,oldGeometries);
                }
                
                try
                {
                    localPluginContext.getLayerViewPanel().getViewport().update();
                }catch(Exception e)
                {
                    
                }
            }

            public void unexecute()
            {
                Iterator selectLayersIterator = layersGeometries.keySet().iterator();
                while(selectLayersIterator.hasNext())
                {
                    Layer currentSelectLayer = (Layer) selectLayersIterator.next();
                    if(currentSelectLayer!=null)
                    {
                        Hashtable oldGeometries = (Hashtable) layersGeometries.get(currentSelectLayer);
                        Iterator oldGeometriesIterator = oldGeometries.keySet().iterator();
                        while(oldGeometriesIterator.hasNext())
                        {
                            Feature currentFeature = (Feature) oldGeometriesIterator.next();
                            if(currentFeature!=null)
                            {
                                currentFeature.setGeometry((Geometry) oldGeometries.get(currentFeature));
                            }
                        }
                        
                    }
                    try
                    {
                        localPluginContext.getLayerViewPanel().getViewport().update();
                    }catch(Exception e)
                    {
                        
                    }
                }
            }
        }, context);
        
    }

    private Hashtable convertGeometryToPoint(TaskMonitor monitor, List allLayerFeatures, Layer currentLayer) {
        monitor.allowCancellationRequests();
        monitor.report(I18N.getMessage(this.getName(),"GeometryToPointPlugIn.RealizandoCoversionAPuntos",new Object[]{currentLayer.getName()}));

        
        Hashtable oldGeometries = new Hashtable();
        
        int size = allLayerFeatures.size();
        int count = 1;
        Point currentConversion = null;
        
        

        for (Iterator i = allLayerFeatures.iterator(); i.hasNext();) {
            monitor.report(count++, size, I18N.get(getName(),"GeometryToPointPlugIn.features"));
            
            Feature f = (Feature) i.next();
            Geometry geom = f.getGeometry();
            
            f.setGeometry(geom.getCentroid());
            oldGeometries.put(f,geom);
        }

        return oldGeometries;
        
    }
    

}
