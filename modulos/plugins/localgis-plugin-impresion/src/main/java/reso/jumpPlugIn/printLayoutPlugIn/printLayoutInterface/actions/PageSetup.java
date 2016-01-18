/*
 * Package reso.JumpPlugIn.PrintLayoutPlugIn.PrintLayoutInterface.Actions pour JUMP
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

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PreviewPanel;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;

/**
 * @author FOUREAU_C
 */

public class PageSetup implements ActionListener {
    private PrintLayoutFrame frame;

    public PageSetup(PrintLayoutFrame parent) {
        frame = parent;
    }

    public void actionPerformed(ActionEvent e) {
        int newX = 0, newY = 0, newWidth = 0, newHeight = 0;

        frame.setPageFormat(frame.getPrinterJob().pageDialog(frame.getPageFormat()));
        frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage().resize(frame.getPageFormat());
        frame.getPrintLayoutPreviewPanel().setPreview(new PreviewPanel(frame, frame.getPrintLayoutPreviewPanel().getPreviewPanel().getPage()));
    }
}