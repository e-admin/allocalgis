package com.geopista.ui.plugin.routeenginetools.calculaterouteplugin.dialog;

import javax.swing.JDialog;

import com.vividsolutions.jump.workbench.plugin.PlugInContext;

public class CalcRutaDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7949170298864511799L;
	
	private PlugInContext context = null;

	public CalcRutaDialog(PlugInContext context, String title){
		super(context.getWorkbenchFrame(),title,false);
		this.context = context;
		
	}
	
	
}
