/**
 * ExportImagePlugIn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.plugin;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.ui.ILayerViewPanel;

public abstract class ExportImagePlugIn extends AbstractPlugIn {

    protected static boolean java14OrNewer() {
        String version = System.getProperty("java.version");
        if (version.indexOf("1.0") == 0) {
            return false;
        }
        if (version.indexOf("1.1") == 0) {
            return false;
        }
        if (version.indexOf("1.2") == 0) {
            return false;
        }
        if (version.indexOf("1.3") == 0) {
            return false;
        }
        //Allow 1.4, 1.5, 1.6, 2.0, etc. [Jon Aquino]
        return true;
    }

    protected BufferedImage image(ILayerViewPanel layerViewPanel) {
        //Don't use TYPE_INT_ARGB, which makes JPEGs pinkish (presumably because
        //JPEGs don't support transparency [Jon Aquino 11/6/2003]
        BufferedImage image = new BufferedImage(layerViewPanel.getWidth(),
                layerViewPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        layerViewPanel.paintComponent((Graphics2D) image.getGraphics());
        return image;
    }

}