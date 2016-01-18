package com.geopista.ui.plugin.analysis;


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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureDatasetFactory;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;


public class GeopistaBufferPlugIn
    extends AbstractPlugIn
    implements ThreadedPlugIn
{

  private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
  
  private final static String LAYER = aplicacion.getI18nString("GeopistaBufferPlugIn.Layer");
  private final static String DISTANCE = aplicacion.getI18nString("GeopistaBufferPlugIn.DistanciaBuffer");

  private MultiInputDialog dialog;
  private Layer layer;
  private double bufferDistance = 1.0;
  private boolean exceptionThrown = false;
  

  public GeopistaBufferPlugIn()
  {
  }

  public void initialize(PlugInContext context) throws Exception {

    
    FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
    EnableCheckFactory checkFactory = new EnableCheckFactory(context.getWorkbenchContext());
    featureInstaller.addMainMenuItem(this,
              new String[] { aplicacion.getI18nString("Tools"), aplicacion.getI18nString("Analysis") },
              aplicacion.getI18nString(this.getName()) + "...", false, null,
              new MultiEnableCheck().add(
                  checkFactory.createWindowWithLayerNamePanelMustBeActiveCheck())
                                    .add(checkFactory.createAtLeastNLayersMustExistCheck(
                      1)));
  }

  public boolean execute(PlugInContext context) throws Exception {
    MultiInputDialog dialog = new MultiInputDialog(
    		GeopistaFunctionUtils.getFrame(context.getWorkbenchGuiComponent()), getName(), true);
    setDialogValues(dialog, context);
    GUIUtil.centreOnWindow(dialog);
    dialog.setVisible(true);
    if (! dialog.wasOKPressed()) { return false; }
    getDialogValues(dialog);
    return true;
  }

  public void run(TaskMonitor monitor, PlugInContext context)
      throws Exception
  {
    FeatureSchema featureSchema = new FeatureSchema();
    featureSchema.addAttribute("GEOMETRY", AttributeType.GEOMETRY);
    FeatureCollection resultFC = new FeatureDataset(featureSchema);

    Collection resultColl = runBuffer(layer.getFeatureCollectionWrapper());
    resultFC = FeatureDatasetFactory.createFromGeometry(resultColl);
    context.addLayer(StandardCategoryNames.WORKING, aplicacion.getI18nString("GeopistaBufferPlugIn.Buffer") + layer.getName(), resultFC);
    if (exceptionThrown)
      context.getWorkbenchGuiComponent().warnUser(aplicacion.getI18nString("GeopistaBufferPlugIn.ErroresEnBuffer"));
  }

  private Collection runBuffer(FeatureCollection fcA)
  {
    exceptionThrown = false;
    Collection resultColl = new ArrayList();
    for (Iterator ia = fcA.iterator(); ia.hasNext(); ) {
      Feature fa = (Feature) ia.next();
      Geometry ga = fa.getGeometry();
      Geometry result = runBuffer(ga);
      if (result != null)
        resultColl.add(result);
    }
    return resultColl;
  }

  private Geometry runBuffer(Geometry a)
  {
    Geometry result = null;
    try {
      result = a.buffer(bufferDistance);
      return result;
    }
    catch (RuntimeException ex) {
      // simply eat exceptions and report them by returning null
      exceptionThrown = true;
    }
    return null;
  }

  private void setDialogValues(MultiInputDialog dialog, PlugInContext context)
  {
    //dialog.setSideBarImage(new ImageIcon(getClass().getResource("DiffSegments.png")));
    dialog.setSideBarDescription(
        aplicacion.getI18nString("GeopistaBufferPlugIn.BuffersGeometrias"));
    //Initial layer value is null
    dialog.addLayerComboBox(LAYER, layer, context.getLayerManager());
    dialog.addDoubleField(DISTANCE, bufferDistance, 10, null);
  }

  private void getDialogValues(MultiInputDialog dialog) {
    layer = dialog.getLayer(LAYER);
    bufferDistance = dialog.getDouble(DISTANCE);
  }




}
