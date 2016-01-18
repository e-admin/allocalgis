/**
 * InstallPrintSectionPlugin.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.reports.scale;

import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.renderer.Renderer;

public class InstallPrintSectionPlugin extends AbstractPlugIn {
	private int width;
	private int height;
	private LayerViewPanel layerViewPanel;	
	
	public InstallPrintSectionPlugin(LayerViewPanel layerViewPanel, int width, int height) {
		this.layerViewPanel = layerViewPanel;
		this.width = width;
		this.height = height;		
	}
	
	protected Renderer.Factory createFactory() {
		return new Renderer.Factory() {
			public Renderer create() {
				return new PrintSectionRenderer(layerViewPanel, width, height);
			}
		};
	}

	public void initialize(PlugInContext context) throws Exception {
		layerViewPanel.getRenderingManager().putAboveLayerables(
				PrintSectionRenderer.CONTENT_ID,
				createFactory());
	}
}
