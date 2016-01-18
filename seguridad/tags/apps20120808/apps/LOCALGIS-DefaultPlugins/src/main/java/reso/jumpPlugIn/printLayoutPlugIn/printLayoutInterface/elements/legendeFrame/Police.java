/*
 * Package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.legendeFrame pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 28 oct. 2004
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

/**
 * @author FOUREAU_C
 */
public class Police {
    private Font font;

    private Color color;

    private boolean isUnderline;

    private boolean isUpperCase;

    public Police() {
        font = new Font("Arial", Font.PLAIN, 12);
        color = Color.BLACK;
        isUnderline = false;
        isUpperCase = false;
    }

    public Police(Font f, Color c, boolean underline, boolean upperCase) {
        font = f;
        color = c;
        isUnderline = underline;
        isUpperCase = upperCase;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public boolean getUnderline() {
        return isUnderline;
    }

    public void setUnderline(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    public boolean getUpperCase() {
        return isUpperCase;
    }

    public void setUpperCase(boolean isUpperCase) {
        this.isUpperCase = isUpperCase;
    }
    public String getFontName(){
        return(font.getName());
    }
    
    public void setFontName(String fn){
        int style = font.getStyle();
        int size = font.getSize();
        Font nf = new Font(fn, style, size);
		 font = nf;
    }
    
    public int getFontStyle(){
        return(font.getStyle());
    }
    
    public void setFontStyle(int st){
        String name = font.getName();
        int size = font.getSize();
        Font nf = new Font(name,st,size);
        font = nf;
    }
    
    public int getFontSize(){
        return(font.getSize());
    }

    public void setFontSize(int s){
        String name = font.getName();
        int style = font.getStyle();
        Font nf = new Font(name,style,s);
        font = nf;
     }

}