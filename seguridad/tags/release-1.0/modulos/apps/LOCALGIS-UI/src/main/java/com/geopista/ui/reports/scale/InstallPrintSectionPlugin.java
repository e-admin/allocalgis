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
