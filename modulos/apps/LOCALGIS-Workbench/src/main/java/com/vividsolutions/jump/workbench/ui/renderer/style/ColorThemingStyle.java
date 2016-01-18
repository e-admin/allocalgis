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
package com.vividsolutions.jump.workbench.ui.renderer.style;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.workbench.model.ILayer;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.IViewport;
import com.vividsolutions.jump.workbench.ui.Viewport;

public class ColorThemingStyle implements Style {

    public ColorThemingStyle() {
        //Parameterless constructor for Java2XML. [Jon Aquino]
    }

    /**
     * Call this method after calling #setAttributeValueToBasicStyleMap
     * rather than before.
     */
    public void setAlpha(int alpha) {
        defaultStyle.setAlpha(alpha);
        for (Iterator i = attributeValueToBasicStyleMap.values().iterator();
            i.hasNext();
            ) {
            BasicStyle style = (BasicStyle) i.next();
            style.setAlpha(alpha);
        }
    }

    /**
     * Call this method after calling #setAttributeValueToBasicStyleMap
     * rather than before.
     */
    public void setLineWidth(int lineWidth) {
        defaultStyle.setLineWidth(lineWidth);
        for (Iterator i = attributeValueToBasicStyleMap.values().iterator();
            i.hasNext();
            ) {
            BasicStyle style = (BasicStyle) i.next();
            style.setLineWidth(lineWidth);
        }
    }

    /**
     * @param defaultStyle <code>null</code> to prevent drawing features with a null
     * attribute value
     */
    public ColorThemingStyle(
        String attributeName,
        Map attributeValueToBasicStyleMap,
        BasicStyle defaultStyle) {
        setAttributeName(attributeName);
        setAttributeValueToBasicStyleMap(attributeValueToBasicStyleMap);
        setDefaultStyle(defaultStyle);
    }

    private BasicStyle defaultStyle;

    public void paint(Feature f, Graphics2D g, IViewport viewport)
        throws Exception {
        getStyle(f).paint(f, g, (Viewport)viewport);
    }

    private BasicStyle getStyle(Feature feature) {
        //Attribute name will be null if a layer has only a spatial attribute. [Jon Aquino]
        //If we can't find an attribute with this name, just use the
        //defaultStyle. The attribute may have been deleted. [Jon Aquino]
        BasicStyle style =
            attributeName != null
                && feature.getSchema().hasAttribute(attributeName)
                && feature.getAttribute(attributeName) != null
                    ? (BasicStyle) attributeValueToBasicStyleMap.get(
                        trimIfString(feature.getAttribute(attributeName)))
                    : defaultStyle;
        return style == null ? defaultStyle : style;
    }

    public static Object trimIfString(Object object) {
        return object != null && object instanceof String ? ((String)object).trim() : object;
    }

    private Layer layer;

    private Map attributeValueToBasicStyleMap = new HashMap();

    private String attributeName;

    public Object clone() {
        try {
            ColorThemingStyle clone = (ColorThemingStyle) super.clone();
            //Deep-copy the map, to facilitate undo. [Jon Aquino]
            Map mapClone =
                (Map) attributeValueToBasicStyleMap.getClass().newInstance();
            for (Iterator i = attributeValueToBasicStyleMap.keySet().iterator();
                i.hasNext();
                ) {
                Object attribute = (Object) i.next();
                mapClone.put(
                    attribute,
                    ((BasicStyle) attributeValueToBasicStyleMap.get(attribute))
                        .clone());
            }
            clone.attributeValueToBasicStyleMap = mapClone;
            return clone;
        } catch (InstantiationException e) {
            Assert.shouldNeverReachHere();
            return null;
        } catch (IllegalAccessException e) {
            Assert.shouldNeverReachHere();
            return null;
        } catch (CloneNotSupportedException e) {
            Assert.shouldNeverReachHere();
            return null;
        }
    }

    /**
     * @return null if the layer has no non-spatial attributes
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * You can set the keys to Ranges if the Map is a Range.RangeTreeMap.
     * But don't mix Ranges and non-Ranges -- the UI expects homogeneity
     * in this regard (i.e. to test whether or not there are ranges, only the first
     * attribute value is tested).
     */
    public void setAttributeValueToBasicStyleMap(Map attributeValueToStyleMap) {
        this.attributeValueToBasicStyleMap = attributeValueToStyleMap;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Map getAttributeValueToBasicStyleMap() {
        return attributeValueToBasicStyleMap;
    }

    private boolean enabled = false;

    public void initialize(ILayer layer) {
        this.layer = (Layer)layer;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static ColorThemingStyle get(Layer layer) {
        if ((ColorThemingStyle) layer.getStyle(ColorThemingStyle.class)
            == null) {
            ColorThemingStyle colorThemingStyle =
                new ColorThemingStyle(
                    pickNonSpatialAttributeName(
                        layer.getFeatureCollectionWrapper().getFeatureSchema()),
                    new HashMap(),
                    new BasicStyle(Color.lightGray));
            layer.addStyle(colorThemingStyle);
        }
        return (ColorThemingStyle) layer.getStyle(ColorThemingStyle.class);
    }

    private static String pickNonSpatialAttributeName(FeatureSchema schema) {
        for (int i = 0; i < schema.getAttributeCount(); i++) {
            if (schema.getGeometryIndex() != i) {
                return schema.getAttributeName(i);
            }
        }
        return null;
    }

    public BasicStyle getDefaultStyle() {
        return defaultStyle;
    }

    public void setDefaultStyle(BasicStyle defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

}
