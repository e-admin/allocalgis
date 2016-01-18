package com.geopista.ui.plugin.routeenginetools.routeutil;

import javax.swing.JComponent;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;

public class MiEnableCheckFactory extends EnableCheckFactory {
	WorkbenchContext workbenchContext;

	public MiEnableCheckFactory(WorkbenchContext workbenchContext) {
		super(workbenchContext);
		this.workbenchContext = workbenchContext;

	}

	public EnableCheck createBlackBoardMustBeElementsCheck() {
		return new EnableCheck() {
			public String check(JComponent component) {
				return ((workbenchContext.getWorkbench().getBlackboard().get(
						"RedesDefinidas") == null)) ? "No hay ninguna red en el blackboard"
						: null;
			}
		};
	}

}
