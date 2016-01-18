/*
 * Package reso.JumpPlugIn.PrintLayoutPlugIn.PrintLayoutInterface pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 3 sept. 2004
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

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.print.PageFormat;

import javax.swing.JPanel;
 


import reso.jumpPlugIn.printLayoutPlugIn.Conversion;

/**
 * @author FOUREAU_C
 */
public class PreviewPanel extends JPanel {
	private Page page = null;
	private int unit, cm, inch;

	public PreviewPanel(PrintLayoutFrame parent) {
		page = new Page(parent);
		setUnits(parent);
		setLayout(new GridBagLayout());
		add(
			page.getPageDrawOnScreen(),
			new GridBagConstraints(
				1,
				1,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE,
				new Insets(unit, unit, unit, unit),
				0,
				0));
	}
	
	public PreviewPanel(PrintLayoutFrame parent, Page page) {
		this.page = page;
		setUnits(parent);
		setLayout(new GridBagLayout());
		add(
			page.getPageDrawOnScreen(),
			new GridBagConstraints(
				1,
				1,
				1,
				1,
				0.0,
				0.0,
				GridBagConstraints.CENTER,
				GridBagConstraints.NONE,
				new Insets(unit, unit, unit, unit),
				0,
				0));
	}

	public Page getPage() {
		return page;
	}

	public int getCmUnit() {
		return cm;
	}

	public int getInchUnit() {
		return inch;
	}

	public void setUnits(PrintLayoutFrame parent) {
		if (parent.getZoomActif() == PrintLayoutFrame.PAGE_ENTIERE) {
			switch (parent.getPageFormat().getOrientation()) {
				case PageFormat.LANDSCAPE :
					cm =
						Math.round(
							(float) (page.getPageDrawOnScreen().getWidth()
								/ Conversion.SoixanteDouxième_Inch_To_cm(
									parent.getPageFormat().getWidth())));
					inch =
						Math.round(
							(float) (page.getPageDrawOnScreen().getWidth()
								/ Conversion.SoixanteDouxième_Inch_To_Inch(
									parent.getPageFormat().getWidth())));
					break;
				case PageFormat.PORTRAIT :
					cm =
						Math.round(
							(float) (page.getPageDrawOnScreen().getHeight()
								/ Conversion.SoixanteDouxième_Inch_To_cm(
									parent.getPageFormat().getHeight())));
					inch =
						Math.round(
							(float) (page.getPageDrawOnScreen().getHeight()
								/ Conversion.SoixanteDouxième_Inch_To_Inch(
									parent.getPageFormat().getHeight())));
					break;
			}
		} else {
			//inch = Toolkit.getDefaultToolkit().getScreenResolution();
		    //on ne prend pas la résolution de l'écran mais la résolution du papier.
		    inch = 72;
			cm = Math.round((float) (inch / 2.54));
		}
		if (parent.getPrintLayoutPreviewPanel().isMetric()) {
			unit = cm;
		} else {
			unit = inch;
		}
	}

	public boolean isOptimizedDrawingEnabled()
	{
	
	return false;
	}
}
