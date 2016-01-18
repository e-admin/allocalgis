/*
 * Package reso.jump.legende pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 25 nov. 2004
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javax.swing.JFileChooser;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.Page;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.util.FileUtil;
import com.vividsolutions.jump.util.java2xml.XML2Java;



/**
 * @author BREMOND_I
 */


public class OpenLegendeEiel implements ActionListener {

    private PrintLayoutFrame frame;
    AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

    private JFileChooser fileChooser;
    
    public OpenLegendeEiel(PrintLayoutFrame parent, String idApp) {
        int i;
        i=0;
		frame = parent;
		i = 1;
    }
    
    public void actionPerformed(ActionEvent e) {
        
    	try{
    		DownloadFromServerEiel dfs = new DownloadFromServerEiel();
    		dfs.getServerPlantillas();	
    	}
    	catch (Exception ex) {
			ex.printStackTrace();
		}
    	
	    fileChooser =getFileChooser();
	    
	    if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(aplicacion.getMainFrame()))
	    {
	        File file = fileChooser.getSelectedFile();
	        file = FileUtil.addExtensionIfNone(file, "jmp");

			FileReader reader = null;
            try {
                reader = new FileReader(file);
            } catch (FileNotFoundException e1) {
                // TODO Bloc catch auto-généré
                e1.printStackTrace();
            }
            Page page;
            page = null;
            try {
                page = (Page) new XML2Java().read(reader, Page.class);
            } catch (Exception e2) {
                // TODO Bloc catch auto-généré
                e2.printStackTrace();
            }
            
            if (page != null)
            {
            List list = page.getGraphicElement();
                for (int i=1; i<=list.size(); i++)
                {
                    frame.setAjouter((GraphicElements) list.get(i-1));
                    frame.getPage().posGraphicElement(frame);
                }
            }
            frame.pack();
            frame.repaint();
            frame.show();
 }
}
    
	public JFileChooser getFileChooser()
	{
	if (fileChooser==null)
		{
		 fileChooser=new JFileChooser();
		}
	return fileChooser;
	}
}