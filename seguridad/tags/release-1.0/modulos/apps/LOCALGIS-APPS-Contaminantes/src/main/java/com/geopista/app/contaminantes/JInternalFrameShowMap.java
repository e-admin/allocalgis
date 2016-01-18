package com.geopista.app.contaminantes;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.StringWriter;
import java.io.PrintWriter;

import com.geopista.model.GeopistaLayer;
import com.geopista.editor.GeopistaEditor;
import com.geopista.feature.GeopistaFeature;
import com.geopista.app.contaminantes.CMainContaminantes;
import com.vividsolutions.jump.feature.Feature;

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
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 28-oct-2004
 * Time: 12:10:06
 */
public class JInternalFrameShowMap extends JInternalFrame{
     protected Logger logger = Logger.getLogger(JInternalFrameShowMap.class);
     protected GeopistaEditor geopistaEditor= null;
     protected GeopistaLayer layer;
     protected Hashtable valoresBusqueda;
     protected String layerBusqueda="numeros_policia";
     protected String atributoBusqueda="ID de número";
     protected boolean refreshFeatureSelection(String layerName,String id) {

		try {
            if (id==null||geopistaEditor==null)return false;
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			geopistaEditor.getSelectionManager().clear();
            GeopistaLayer geopistaLayer = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(layerName);
			Collection collection = searchByAttribute(geopistaLayer, 0, id);
			Iterator it = collection.iterator();
			if (it.hasNext()) {
				Feature feature = (Feature) it.next();
				geopistaEditor.select(geopistaLayer, feature);
			}
			geopistaEditor.zoomToSelected();
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			return true;
		} catch (Exception ex) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}
	}
    public boolean refreshBusquedaSelection(JInternalFrame frame,GeopistaEditor geopistaEditor) {

        try {

            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            geopistaEditor.getSelectionManager().clear();

            GeopistaLayer geopistaLayer = (GeopistaLayer) geopistaEditor.getLayerManager().getLayer(layerBusqueda);

            Enumeration enumerationElement = valoresBusqueda.keys();

            while (enumerationElement.hasMoreElements()) {
                String referenciaCatastral = (String) enumerationElement.nextElement();
                //El numero 1 identifica el id numero de policia
                Collection collection = searchByAttribute(geopistaLayer, 1, referenciaCatastral);
                Iterator it = collection.iterator();
                if (it.hasNext()) {
                    Feature feature = (Feature) it.next();
                    geopistaEditor.select(geopistaLayer, feature);
                }
            }

            geopistaEditor.zoomToSelected();
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return true;
        } catch (Exception ex) {

            frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return false;
        }
    }

    protected String refreshListSelection(String layerName)
    {
    try {

            Collection collection = geopistaEditor.getSelection();
            if  (collection.iterator().hasNext()) {
                GeopistaFeature feature = (GeopistaFeature) collection.iterator().next();
                if (feature == null) {
                    logger.error("feature: " + feature);
                    return null;
                }

                if (layerName!=null && feature.getLayer()!=null)
                {
                    if (!layerName.equals(feature.getLayer().getName()))
                        return null;
                }
                //String id = checkNull(feature.getAttribute(0));
                String id = checkNull(feature.getSystemId());
                logger.info("id: -" + id + "-");
                return id;
            }
        } catch (Exception ex) {

            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return null;
        }
        return null;
    }
    public static Collection searchByAttribute(GeopistaLayer geopistaLayer, int attributeNumber, String value)
    {
            Collection finalFeatures = new ArrayList();
            if (geopistaLayer == null) return finalFeatures;
            java.util.List allFeaturesList = geopistaLayer.getFeatureCollectionWrapper().getFeatures();
            Iterator allFeaturesListIter = allFeaturesList.iterator();
            while (allFeaturesListIter.hasNext()) {
                    Feature localFeature = (Feature) allFeaturesListIter.next();
                    String nombreAtributo = localFeature.getString(attributeNumber).trim();
                    if (nombreAtributo.equals(value)) {
                        finalFeatures.add(localFeature);
                }
            }

            return finalFeatures;
        }
        public static String checkNull(Object o) {
		if (o == null) {
			return "";
		}
    	return o.toString();
	}

    public GeopistaEditor getGeopistaEditor()
    {
        return geopistaEditor;
    }

}
