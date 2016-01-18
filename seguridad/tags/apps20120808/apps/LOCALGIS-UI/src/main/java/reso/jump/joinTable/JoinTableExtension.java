/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	27 oct. 2004
 * 
 */
package reso.jump.joinTable;

import com.vividsolutions.jump.workbench.plugin.Extension;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * @author Olivier BEDEL
 * 	Laboratoire RESO UMR 6590 CNRS
 * 	Bassin Versant du Jaudy-Guindy-Bizien
 * 	27 oct. 2004
 * 
 */
public class JoinTableExtension extends Extension {

	public void configure(PlugInContext context) throws Exception {
		JoinTablePlugIn joinTablePlugIn = new JoinTablePlugIn();
		joinTablePlugIn.initialize(context);
	}
}
