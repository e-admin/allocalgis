/**
 * IRenderingManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.workbench.ui.renderer;

import java.awt.Graphics2D;

public interface IRenderingManager {

	/**
	 * @see #multiRendererThreadQueue
	 */
	public static final String USE_MULTI_RENDERING_THREAD_QUEUE_KEY = IRenderingManager.class
			.getName() + " - USE MULTI RENDERING THREAD QUEUE";

	public abstract void putBelowLayerables(Object contentID,
			Renderer.Factory factory);

	public abstract void putAboveLayerables(Object contentID,
			Renderer.Factory factory);

	/**
	 * Render All without refreshing caches
	 *
	 */
	public abstract void renderAll();

	/**
	 * Render All
	 * @param clearImageCache whether to discard cache or not
	 *
	 */
	public abstract void renderAll(boolean clearImageCache);

	public abstract Renderer getRenderer(Object contentID);

	public abstract void render(Object contentID);

	public abstract void render(Object contentID, boolean clearImageCache);

	public abstract void repaintPanel();

	public abstract void setPaintingEnabled(boolean paintingEnabled);

	public abstract void copyTo(Graphics2D destination);

	public abstract ThreadQueue getDefaultRendererThreadQueue();

	public abstract void dispose();

	public abstract void destroy();

}