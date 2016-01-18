/*
 * Package reso.JumpPlugIn.PrintLayoutPlugIn.PrintLayoutInterface pour JUMP
 *
 * Copyright (C) 2004
 * Olivier Bedel, ingénieur informaticien laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien.
 * Céline Foureau, stagiaire MIAGE, laboratoire Reso UMR ESO 6590.
 * Erwan Bocher, doctorant en géographie, laboratoire Reso UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien
 *
 * Date de création : 1 sept. 2004
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.print.elements.ImageFrame;
import com.geopista.ui.plugin.print.elements.MapFrame;
import com.geopista.ui.plugin.print.elements.ScaleBarFrame;


import reso.jumpPlugIn.printLayoutPlugIn.OpenLegende;
import reso.jumpPlugIn.printLayoutPlugIn.SaveInServer;
import reso.jumpPlugIn.printLayoutPlugIn.SaveLegende;
import reso.jumpPlugIn.printLayoutPlugIn.I18NPlug;
import reso.jumpPlugIn.printLayoutPlugIn.PrintLayoutPlugIn;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.EntirePage;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.PageSetup;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.Print;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.Quit;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.WidthPage;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.NorthFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.graphicText.GraphicText;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.legendeFrame.LegendFrame;



/**
 * @author FOUREAU_C
 */
public class PrintLayoutMenuBar extends JMenuBar {
	private JMenu file = new JMenu(
			I18NPlug.get(PrintLayoutPlugIn.name,
					"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.File"));

	private JMenu add = new JMenu(
			I18NPlug.get(PrintLayoutPlugIn.name,
					"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Add"));

	private JMenu show = new JMenu(
			I18NPlug.get(PrintLayoutPlugIn.name,
					"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Show"));

	private JMenuItem save, saveAs, open, pageSetup, print, quit, saveInServer;

	private JMenuItem map, scaleBar, northSymbol, text, legende;

	private JMenuItem entirePage, widthPage;

	private PrintLayoutFrame parent;

	private JMenuItem	image;

	public PrintLayoutMenuBar(final PrintLayoutFrame parent) {
		//Menu File
		save = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Save"));
		save.addActionListener(new SaveLegende(parent));
		file.add(save);

		saveAs = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.SaveAs..."));
		saveAs.addActionListener(new SaveLegende(parent));
		file.add(saveAs);
		
		saveInServer = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.SaveInServer..."));
		saveInServer.addActionListener(new SaveInServer(parent));
		file.add(saveInServer);

		file.addSeparator();
		
		open = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Open"));
		AppContext.getApplicationContext().getBlackboard().put(AppContext.idAppType, "impresion" );
		open.addActionListener(new OpenLegende(parent));
		file.add(open);

		file.addSeparator();

		pageSetup = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.PageSetup..."));
		pageSetup.addActionListener(new PageSetup(parent));
		file.add(pageSetup);

		print = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Print..."));
		print.addActionListener(new Print(parent));
		file.add(print);

		file.addSeparator();

		quit = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Quit"));
		quit.addActionListener(new Quit(parent));
		file.add(quit);

		add(file);

		//Menu Add
		map = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Map"));

		map.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.setAjouter(new MapFrame(parent));
			}
		});

		add.add(map);

		scaleBar = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.ScaleBar"));
		add.add(scaleBar);
		scaleBar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		parent.setAjouter(new ScaleBarFrame(parent));
	}
});
		scaleBar.setEnabled(true);

		northSymbol = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.NorthSymbol"));
		northSymbol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.setAjouter(new NorthFrame(parent));
			}
		});
		add.add(northSymbol);
		
		image = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Image"));
		image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.setAjouter(new ImageFrame(parent));
			}
		});
		add.add(image);
		
		text = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Text"));
		text.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.setAjouter(new GraphicText(parent));
			}
		});
		add.add(text);

		legende = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Legende"));
		legende.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                parent.setAjouter(new LegendFrame(parent));
            }});
		
		add.add(legende);

		add(add);

		//Menu Show
		entirePage = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.Page"));
		entirePage.addActionListener(new EntirePage(parent));
		show.add(entirePage);

		widthPage = new JMenuItem(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutMenuBar.WidthPage"));
		widthPage.addActionListener(new WidthPage(parent));
		show.add(widthPage);

		add(show);
	}
}