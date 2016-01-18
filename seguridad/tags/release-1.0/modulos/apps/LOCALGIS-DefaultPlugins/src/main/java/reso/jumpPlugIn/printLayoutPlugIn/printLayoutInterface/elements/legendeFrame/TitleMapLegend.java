/*
 * Package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.legendeFrame pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 27 oct. 2004
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

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.legendeFrame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

/**
 * @author FOUREAU_C
 */
public class TitleMapLegend extends JLabel {
    private Police police = new Police (new Font("Arial", Font.BOLD, 12), Color.BLACK, false, false);
    
    public TitleMapLegend(String s){
        super(s);
    }
    
    public Police getPolice() {
        return police;
    }
    
    public void setPolice(Police police) {
        this.police = police;
        super.setFont(police.getFont());
        super.setForeground(police.getColor());
        if(police.getUnderline()){
            super.setText("<html><u>"+super.getText()+"/html></u>");
        }else{
            super.setText(super.getText().replaceAll("<html><u>", ""));
            super.setText(super.getText().replaceAll("</html></u>", ""));
        }
        if(police.getUpperCase()){
            super.setText(super.getText().toUpperCase());
        }else{
            super.setText(super.getText().toLowerCase());
        }
        repaint();
    }
}
