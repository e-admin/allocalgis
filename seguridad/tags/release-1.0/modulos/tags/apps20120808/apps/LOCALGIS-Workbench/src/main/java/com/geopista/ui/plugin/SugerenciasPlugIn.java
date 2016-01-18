package com.geopista.ui.plugin;

import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;


public class SugerenciasPlugIn extends AbstractPlugIn {

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);                
		com.geopista.app.sugerencias.SugerenciasForm sform = new com.geopista.app.sugerencias.SugerenciasForm();
		sform.setVisible(true);

        return true;
    }
    
}

