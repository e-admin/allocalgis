/*
 * Package reso.JumpPlugIn.PrintLayoutPlugIn pour JUMP
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

package reso.jumpPlugIn.printLayoutPlugIn;

import java.awt.Toolkit;

/**
 * @author FOUREAU_C
 */
public class Conversion {
	private final static double cm_In_inch = 2.54;

	private final static double inch_In_cm = 0.3937;

	private final static int pt_In_inch = Toolkit.getDefaultToolkit().getScreenResolution();

	private final static int pt_In_cm = Math.round((float) (pt_In_inch / cm_In_inch));

	public static int inch_To_cm(double value) {
		return Math.round((float) (value * cm_In_inch));
	}

	public static int cm_To_inch(double value) {
		return Math.round((float) (value * inch_In_cm));
	}

	public static int SoixanteDouxième_Inch_To_cm(double value) {
		return Math.round((float) ((value / 72) * cm_In_inch));
	}

	public static int SoixanteDouxième_Inch_To_Inch(double value) {
		return Math.round((float) (value / 72));
	}

	public static int Inch_To_Pixel(double value) {
		return Math.round((float) (value * pt_In_inch));
	}

	public static int Pixel_To_Inch(double value) {
		return Math.round((float) (value / pt_In_inch));
	}

	public static int SoixanteDouxième_Inch_To_Pixel(double value) {
	    return Math.round((float) ((value * 72) / pt_In_inch));
	}

	public static int cm_To_Pixel(double value) {
		return Math.round((float) value * pt_In_cm);
	}
}