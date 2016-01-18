/*
* The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 */

package com.geopista.ui.plugin.print;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.geopista.app.AppContext;
import com.geopista.ui.GEOPISTAWorkbench;
import com.geopista.ui.GEOPISTAWorkbenchContext;
import com.geopista.ui.plugin.print.images.IconLoader;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MenuNames;

/**
 * @author juacas
 */
public class PrintLayoutPlugIn extends AbstractPlugIn {
    public static String name = "printLayout"; 
 
    public static final String toolBarCategory = "GeopistaPrintPlugIn.category";

	public static final String	GEOPISTA_PRINT_MAPDESCRIPTION	= "geopista.print.mapsubtitle";

	public static final String	GEOPISTA_PRINT_MAPTITLE	= "geopista.print.maptitle";

	static public final String	GEOPISTA_PRINT_MUNICIPALITYNAME	= "geopista.print.municipalityname";

	public static final String	GEOPISTA_PRINT_CURRENTPAGE	= "geopista.print.currentpage";

	public static final String	GEOPISTA_PRINT_PAGECOUNT	= "geopista.print.pagecount";
	
	private static AppContext aplicacion = (AppContext) AppContext
    .getApplicationContext();

    public void initialize(PlugInContext context) throws Exception {
        //initialisation I18N
    Locale loc=Locale.getDefault();
	 
	ResourceBundle bundle2 = ResourceBundle.getBundle("language.printLayout",loc,this.getClass().getClassLoader());
	
    I18N.plugInsResourceBundle.put(PrintLayoutPlugIn.name,bundle2);
		//I18N.setPlugInRessource(PrintLayoutPlugIn.name, "language.printLayout");
		// chemin de la commande dans l'interface
		// on s'appuie sur les noms de menus I18N quand ils existent
		String pathMenuNames[] =new String[] { MenuNames.FILE };
        
		String pluginCategory = aplicacion.getString(toolBarCategory);
        //context.getFeatureInstaller().addMenuSeparator(pathMenuNames[0]);
		context.getFeatureInstaller().addMainMenuItem(this, pathMenuNames, getName(),
			false, GUIUtil.toSmallIcon((ImageIcon) getIcon()),
			createEnableCheck(context.getWorkbenchContext()));
				context.getWorkbenchContext().getWorkbench().getFrame().getToolBar(pluginCategory)
					.addPlugIn(
						getIcon(),
						this,
						PrintLayoutPlugIn.createEnableCheck(context
								.getWorkbenchContext()),
						context.getWorkbenchContext());
		/*context.getWorkbenchContext().getWorkbench().getFrame().getToolBar(pluginCategory)
				.addSeparator();*/
	}

	public static MultiEnableCheck createEnableCheck(
			final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(
				workbenchContext);

		return new MultiEnableCheck().add(
				checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
				.add(checkFactory.createAtLeastNLayersMustExistCheck(1));
	}

	public boolean execute(PlugInContext context) throws Exception {
		PrintLayoutFrame frame = new PrintLayoutFrame(context);
		
		frame.setVisible(true);
		frame.forceOpen();
		return true;
	}

	public String getName() {
		return name;
	}
	


	public Icon getIcon() {
	    return IconLoader.icon("print.gif");
	}
}