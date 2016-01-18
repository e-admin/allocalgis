/**
 * Renderer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.renderer;

import java.awt.Graphics2D;

/**
 * First call #createRunnable. If it returns null, get the image using #copyTo.
 * Otherwise, run the Runnable in a separate thread. You can call #copyTo while
 * it's drawing to get the partially drawn image. Drawing is done when
 * #isRendering returns false.
 */
public interface Renderer {
	public abstract void clearImageCache();
	public abstract boolean isRendering();
    /**
     *@return identifies this Renderer by what it draws
     */    
	public abstract Object getContentID();
	public abstract void copyTo(Graphics2D graphics);
    /**
     * @return null if no rendering work needs to be done
     */
	public abstract Runnable createRunnable();
	public abstract void cancel();
    
    public static interface Factory {
        public Renderer create();
    }
    
    public static interface ContentDependendFactory {
        public Renderer create(Object contentID);
    }
}