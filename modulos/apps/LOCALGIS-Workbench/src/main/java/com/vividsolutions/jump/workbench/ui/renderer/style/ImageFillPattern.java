/**
 * ImageFillPattern.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.renderer.style;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.CollectionUtil;


/**
 * You can set the alpha by calling #setColor (only the alpha will be read)
 */
public class ImageFillPattern extends BasicFillPattern {
    private static final String FILENAME_KEY = "FILENAME";
    private static final String CLASS_KEY = "CLASS";

    /**
     * @param resourceName name of a resource associated with the given class
     * (e.g. the name of a .png, .gif, or .jpg file in the same package as the class) 
     */
    public ImageFillPattern(Class c, String resourceName) {
        super(new Blackboard().putAll(CollectionUtil.createMap(
                    new Object[] {
                        BasicFillPattern.COLOR_KEY, Color.black, CLASS_KEY, c,
                        FILENAME_KEY, resourceName
                    })));
    }

    /**
     * Parameterless constructor for Java2XML
     */
    public ImageFillPattern() {
    }

    public BufferedImage createImage(Blackboard properties) {
        ImageIcon imageIcon = new ImageIcon(((Class) properties.get(CLASS_KEY)).getResource(
                    properties.get(FILENAME_KEY).toString()));
        BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(),
                imageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                ((Color) getProperties().get(COLOR_KEY)).getAlpha() / 255f));
        g.drawImage(imageIcon.getImage(), 0, 0, null);

        return bufferedImage;
    }
}
