/**
 * BasicFillPattern.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.renderer.style;

import java.awt.Color;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Iterator;

import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.Blackboard;


public abstract class BasicFillPattern implements Paint, Cloneable {
    public static final String COLOR_KEY = "COLOR";

    //Lazily initialize paint -- it may be too slow to create paint for all fill patterns
    //at once. [Jon Aquino]
    private Paint paint;
    private Blackboard properties;

    /**
     * Parameterless constructor for Java2XML
     */
    public BasicFillPattern() {
    }

    public BasicFillPattern(Blackboard properties) {
        setProperties(properties);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        //Allow checking for equality. Otherwise, lose selected combo box value
        //on re-entering Change Styles dialog. [Jon Aquino]
        if (getClass() != obj.getClass()) {
            return false;
        }

        BasicFillPattern other = (BasicFillPattern) obj;

        if (getProperties().getProperties().size() != other.getProperties()
                                                               .getProperties()
                                                               .size()) {
            return false;
        }

        for (Iterator i = getProperties().getProperties().keySet().iterator();
                i.hasNext();) {
            String key = (String) i.next();

            if (other.getProperties().getProperties().get(key) == null) {
                return false;
            }

            if (!getProperties().getProperties().get(key).equals(other.getProperties()
                                                                          .getProperties()
                                                                          .get(key))) {
                return false;
            }
        }

        return true;
    }

    private Paint getPaint() {
        if (paint == null) {
            BufferedImage image = createImage(properties);
            paint = new TexturePaint(image,
                    new Rectangle2D.Double(0, 0, image.getWidth(),
                        image.getHeight()));
        }

        return paint;
    }

    public Blackboard getProperties() {
        return properties;
    }

    public BasicFillPattern setProperties(Blackboard properties) {
        this.properties = properties;
        paint = null;

        return this;
    }

    public abstract BufferedImage createImage(Blackboard properties);

    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds,
        Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
        return getPaint().createContext(cm, deviceBounds, userBounds, xform,
            hints);
    }

    public int getTransparency() {
        return getPaint().getTransparency();
    }

    public BasicFillPattern setColor(Color color) {
        setProperties(getProperties().put(COLOR_KEY, color));

        return this;
    }
    public Object clone() {
        try {
            return ((BasicFillPattern)getClass().newInstance()).setProperties((Blackboard)(properties.clone()));
        } catch (InstantiationException e) {
            Assert.shouldNeverReachHere();
        } catch (IllegalAccessException e) {
            Assert.shouldNeverReachHere();
        }
        return null;
    }
}
