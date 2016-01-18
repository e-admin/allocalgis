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
import java.awt.Cursor;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jump.workbench.ui.cursortool.AbstractCursorTool;


/**
 *  Code used by both CreateDelineationTool and EditDelineationTool.
 */
public class DelineationUtil {
    public final static String DELINEATION_LAYER_NAME = "Delineations";
    public final static String DESTINATION_SNAPPED_ATTRIBUTE_NAME = "DESTINATION_SNAPPED";
    public final static String SOURCE_SNAPPED_ATTRIBUTE_NAME = "SOURCE_SNAPPED";
    public final static Color TOOL_COLOR = Color.black;
    public final static int TOOL_STROKE_WIDTH = 5;
    public final static Icon ICON = new ImageIcon(DelineationUtil.class.getResource(
                "BigPin.gif"));
    public final static Cursor CURSOR = AbstractCursorTool.createCursor(new ImageIcon(
                DelineationUtil.class.getResource("Pin.gif")).getImage());

    public static void checkDelineationCoordinates(Coordinate[] coordinates)
        throws Exception {
        if (coordinates.length != 2) {
            throw new Exception(
                "Delineation layer contains features with a coordinate-count other than 2");
        }
    }

    public static String toString(boolean b) {
        return b ? "Y" : "N";
    }
}
