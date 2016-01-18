/*
 * Package reso.JumpPlugIn.PrintLayoutPlugIn pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 31 août 2004
 *
 * Développé dans le cadre du Projet APARAD 
 *  (Laboratoire Reso UMR ESO 6590 CNRS / Bassin Versant du Jaudy-Guindy-Bizien)
 *    Responsable : Erwan BOCHER
 *    Développeurs : Céline FOUREAU, Olivier BEDEL
 *
 * olivier.bedel@uhb.fr ou olivier.bedel@yahoo.fr
 * erwan.bocher@uhb.fr ou erwan.bocher@free.fr
 * celine.foureau@uhb.fr ou celine.foureau@wanadoo.fr
 * 
 * Ce package hérite de la licence GPL de JUMP. Il est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. (http://www.cecill.info)
 * 
 */

package reso.jumpPlugIn.printLayoutPlugIn;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;

import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 * @author FOUREAU_C
 */
public class PrintLayoutPlugIn extends AbstractPlugIn {
	public static String name = "printLayout"; 

	public void initialize(PlugInContext context) throws Exception {
		//initialisation I18N
		I18NPlug.setPlugInRessource(PrintLayoutPlugIn.name, "language.printLayout");
		// chemin de la commande dans l'interface
		// on s'appuie sur les noms de menus I18N quand ils existent
		String pathMenuNames[] = new String[] {I18NPlug.get(PrintLayoutPlugIn.name, "PrintLayout.MenuName")};

		context.getFeatureInstaller().addMenuSeparator(pathMenuNames[0]);
		context.getFeatureInstaller().addMainMenuItem(this, pathMenuNames, getName(), false, GUIUtil.toSmallIcon((ImageIcon) getIcon()), createEnableCheck(context.getWorkbenchContext()));
		context.getWorkbenchContext().getWorkbench().getFrame().getToolBar().addPlugIn(getIcon(), this, PrintLayoutPlugIn.createEnableCheck(context.getWorkbenchContext()), context.getWorkbenchContext());
		context.getWorkbenchContext().getWorkbench().getFrame().getToolBar().addSeparator();
	}

	public static MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
		EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);

		return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck())
									 .add(checkFactory.createAtLeastNLayersMustExistCheck(1));
	}

	public boolean execute(PlugInContext context) throws Exception {
		PrintLayoutFrame frame = new PrintLayoutFrame(context);
		frame.show();
		return true;
	}

	public String getName() {
		return name;
	}

	public Icon getIcon() {
		String imageFile = "images/Edit16.gif";
		URL imageURL = PrintLayoutPlugIn.class.getResource(imageFile);
		if (imageURL != null) { //image found
			return new ImageIcon(imageURL);
		} else { //no image found
			System.err.println("Resource not found: " + imageFile);
		}
		return null;
	}
}