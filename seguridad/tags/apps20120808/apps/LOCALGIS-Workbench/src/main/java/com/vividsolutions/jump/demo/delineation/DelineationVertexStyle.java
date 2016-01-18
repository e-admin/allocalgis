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

package com.vividsolutions.jump.demo.delineation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.Viewport;
import com.vividsolutions.jump.workbench.ui.renderer.style.VertexStyle;


/**
 *  Paints anchored vertices green and unanchored vertices red.
 */
public class DelineationVertexStyle extends VertexStyle {
    public DelineationVertexStyle() {
        super(new Ellipse2D.Double());
    }

    public void paint(Feature f, Graphics2D g, Viewport viewport)
        throws Exception {
        Coordinate[] coordinates = f.getGeometry().getCoordinates();
        DelineationUtil.checkDelineationCoordinates(coordinates);
        g.setColor(color(f.getAttribute(
                    DelineationUtil.SOURCE_SNAPPED_ATTRIBUTE_NAME).equals("Y")));
        paint(g,
            viewport.toViewPoint(
                new Point2D.Double(coordinates[0].x, coordinates[0].y)));
        g.setColor(color(f.getAttribute(
                    DelineationUtil.DESTINATION_SNAPPED_ATTRIBUTE_NAME).equals("Y")));
        paint(g,
            viewport.toViewPoint(
                new Point2D.Double(coordinates[1].x, coordinates[1].y)));
    }

    private Color color(boolean snapped) {
        return snapped ? Color.green : Color.red;
    }
}
