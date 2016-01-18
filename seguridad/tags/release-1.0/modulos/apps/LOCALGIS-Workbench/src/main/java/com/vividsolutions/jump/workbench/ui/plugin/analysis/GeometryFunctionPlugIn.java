
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

package com.vividsolutions.jump.workbench.ui.plugin.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.feature.FeatureDatasetFactory;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.feature.IndexedFeatureCollection;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

/** 
* Provides basic functions for computation with {@link Geometry} objects.
* <p> Includes:
* <ul>
* <li> Intersection </li> 
* <li> Union </li> 
* <li> Difference (A-B) </li> 
* <li> Difference (B-A) </li> 
* <li> Symmetric Difference  </li> 
* </ul></p>
*/

public class GeometryFunctionPlugIn
    extends AbstractPlugIn
    implements ThreadedPlugIn
{
  private final static String LAYER1 = "Layer A";
  private final static String LAYER2 = "Layer B";
  private final static String METHODS = "Function";

  private static final String METHOD_INTERSECTION = "Intersection";
  private static final String METHOD_UNION = "Union";
  private static final String METHOD_DIFFERENCE_AB = "Difference (A-B)";
  private static final String METHOD_DIFFERENCE_BA = "Difference (B-A)";
  private static final String METHOD_SYMDIFF = "Symmetric Difference";
  //private static final String METHOD_CENTROID_A = "Centroid of A";

  private static Collection getGeometryMethodNames()
  {
    Collection names = new ArrayList();
    names.add(METHOD_INTERSECTION);
    names.add(METHOD_UNION);
    names.add(METHOD_DIFFERENCE_AB);
    names.add(METHOD_DIFFERENCE_BA);
    names.add(METHOD_SYMDIFF);
    //names.add(METHOD_CENTROID_A);
    return names;
  }

  private Collection geometryMethodNames;
  private MultiInputDialog dialog;
  private Layer layer1, layer2;
  private String methodNameToRun;
  private boolean exceptionThrown = false;

/** 
* Sets geomentryMethodNames variable using getGeometryMethodNames function.
*/
  public GeometryFunctionPlugIn()
  {
    geometryMethodNames = getGeometryMethodNames();
  }


  /*
    public void initialize(PlugInContext context) throws Exception {
      context.getFeatureInstaller().addMainMenuItem(
          this, "Tools", "Find Unaligned Segments...", null, new MultiEnableCheck()
        .add(context.getCheckFactory().createWindowWithLayerNamePanelMustBeActiveCheck())
          .add(context.getCheckFactory().createAtLeastNLayersMustExistCheck(1)));
    }
  */

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


    Collection resultColl = runGeometryMethod(layer1.getFeatureCollectionWrapper(),
        layer2.getFeatureCollectionWrapper(),
        methodNameToRun);
    resultFC = FeatureDatasetFactory.createFromGeometry(resultColl);
    context.addLayer(StandardCategoryNames.WORKING, "Result-" + methodNameToRun, resultFC);
    if (exceptionThrown)
      context.getWorkbenchGuiComponent().warnUser("Errors found while executing function");
  }

  private Collection runGeometryMethod(FeatureCollection fcA,
                                     FeatureCollection fcB,
                                     String methodName
                                     )
  {
    exceptionThrown = false;
    Collection resultColl = new ArrayList();
    FeatureCollection index = new IndexedFeatureCollection(fcB);
    for (Iterator ia = fcA.iterator(); ia.hasNext(); ) {
      Feature fa = (Feature) ia.next();
      Geometry ga = fa.getGeometry();
      Collection queryResult = index.query(ga.getEnvelopeInternal());
      for (Iterator ib = queryResult.iterator(); ib.hasNext(); ) {
        Feature fb = (Feature) ib.next();
        Geometry gb = fb.getGeometry();
        Geometry result = runGeometryMethod(ga, gb, methodName);
        if (result != null)
          resultColl.add(result);
      }
    }
    return resultColl;
  }

  private Geometry runGeometryMethod(Geometry a, Geometry b, String methodName)
  {
    Geometry result = null;
    try {
      if (methodName.equals(METHOD_INTERSECTION)) {
        result = a.intersection(b);
      }
      else if (methodName.equals(METHOD_UNION)) {
        result = a.union(b);
      }
      else if (methodName.equals(METHOD_DIFFERENCE_AB)) {
        result = a.difference(b);
      }
      else if (methodName.equals(METHOD_DIFFERENCE_BA)) {
        result = b.difference(a);
      }
      else if (methodName.equals(METHOD_SYMDIFF)) {
        result = a.symDifference(b);
      }
      /* To be developed...
      else if (methodName.equals(METHOD_CENTROID_A)) {
        result = a.getCentroid();
      }
      */
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
        "Computes a spatial function on all (pairs of) geometries from the input layer(s)");
    //Set initial layer values to the first and second layers in the layer list.
    //In #initialize we've already checked that the number of layers >= 2. [Jon Aquino]
    dialog.addLayerComboBox(LAYER1, layer1, context.getLayerManager());
    dialog.addLayerComboBox(LAYER2, layer2, context.getLayerManager());
    dialog.addComboBox(METHODS, methodNameToRun, geometryMethodNames, null);
  }

  private void getDialogValues(MultiInputDialog dialog) {
    layer1 = dialog.getLayer(LAYER1);
    layer2 = dialog.getLayer(LAYER2);
    methodNameToRun = dialog.getText(METHODS);
  }




}
