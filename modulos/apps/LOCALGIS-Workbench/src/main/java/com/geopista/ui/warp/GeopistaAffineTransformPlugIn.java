/**
 * GeopistaAffineTransformPlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.warp;



import java.util.Iterator;

import com.geopista.app.AppContext;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.warp.AffineTransform;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.warp.WarpingVectorLayerFinder;


/**
 *  Applies an affine transform to the selected layers. The affine transform is
 *  specified using three vectors drawn by the user.
 */
public class GeopistaAffineTransformPlugIn extends AbstractPlugIn {


    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    public GeopistaAffineTransformPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        context.getFeatureInstaller().addMainMenuItem(this,
            new String[] { aplicacion.getI18nString("Tools"), "GeopistaAffineTransformPlugIn.Warping" }, aplicacion.getI18nString(getName()), false, null,
            new MultiEnableCheck().add(context.getCheckFactory()
                                              .createWindowWithLayerViewPanelMustBeActiveCheck())
                                  .add(context.getCheckFactory()
                                              .createWindowWithLayerNamePanelMustBeActiveCheck())
                                  .add(context.getCheckFactory()
                                              .createExactlyNLayersMustBeSelectedCheck(1))
                                  .add(context.getCheckFactory()
                                              .createBetweenNAndMVectorsMustBeDrawnCheck(1,
                    3)));
    }

    public boolean execute(PlugInContext context) throws Exception {
        AffineTransform transform = affineTransform(context);
        FeatureCollection featureCollection = transform.transform(context.getSelectedLayer(
                    0).getFeatureCollectionWrapper());
        context.getLayerManager().addLayer(StandardCategoryNames.WORKING,
            "Affined " + context.getSelectedLayer(0).getName(),
            featureCollection);
        checkValid(featureCollection, context);

        return true;
    }

    public static void checkValid(FeatureCollection featureCollection,
        PlugInContext context) {
        for (Iterator i = featureCollection.iterator(); i.hasNext();) {
            Feature feature = (Feature) i.next();

            if (!feature.getGeometry().isValid()) {
                context.getLayerViewPanel().getContext().warnUser("Some geometries are not valid");

                return;
            }
        }
    }

    /**
     *@return either the tip or the tail coordinate of the nth vector
     */
    private Coordinate vectorCoordinate(int n, boolean tip,
        PlugInContext context, WarpingVectorLayerFinder vectorLayerManager) {
        LineString vector = (LineString) vectorLayerManager.getVectors().get(n);

        return tip ? vector.getCoordinateN(1) : vector.getCoordinateN(0);
    }

    private AffineTransform affineTransform(PlugInContext context) {
        WarpingVectorLayerFinder vlm = new WarpingVectorLayerFinder(context);

        switch (vlm.getVectors().size()) {
        case 1:
            return new AffineTransform(vectorCoordinate(0, false, context, vlm),
                vectorCoordinate(0, true, context, vlm));

        case 2:
            return new AffineTransform(vectorCoordinate(0, false, context, vlm),
                vectorCoordinate(0, true, context, vlm),
                vectorCoordinate(1, false, context, vlm),
                vectorCoordinate(1, true, context, vlm));

        case 3:
            return new AffineTransform(vectorCoordinate(0, false, context, vlm),
                vectorCoordinate(0, true, context, vlm),
                vectorCoordinate(1, false, context, vlm),
                vectorCoordinate(1, true, context, vlm),
                vectorCoordinate(2, false, context, vlm),
                vectorCoordinate(2, true, context, vlm));
        }

        Assert.shouldNeverReachHere();

        return null;
    }
}
