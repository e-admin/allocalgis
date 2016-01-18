package fr.michaelm.jump.query;

import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * QueryPlugInExtension
 * @author Michaël MICHAUD
 * @version 0.1.0 (11 Dec 2004)
 */ 
public class QueryExtension extends Extension {

    public void configure(PlugInContext context) throws Exception {
        new QueryPlugIn().initialize(context);
    }
    
    public String getName() {return "Simple Query";}
    
    public String getVersion() {return "0.1.1 (19.01.2005)";}
    
}
