package com.geopista.ui.reports.scale;

import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class PrintSectionPlugin extends AbstractPlugIn {
	
	public boolean execute(PlugInContext context) throws Exception {
		//reportNothingToUndoYet(context);
		//PrintSectionRenderer.setEnabled(true, context.getLayerViewPanel());
		context.getLayerViewPanel().getRenderingManager().render(PrintSectionRenderer.CONTENT_ID);

		return true;
	}
	
}
