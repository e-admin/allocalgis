package fr.michaelm.jump.bsheditor;

import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class BshEditorPlugInExtension extends Extension {
    public void configure(PlugInContext context) throws Exception {
        new BeanShellEditorPlugIn().initialize(context);
    }
    public String getName() {return "BeanShell Editor";}
    public String getVersion() {return "0.1.1 (20/11/2004)";}
}
