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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import com.vividsolutions.jump.workbench.ui.EnableableToolBar;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomTool;



import reso.jumpPlugIn.printLayoutPlugIn.I18NPlug;
import reso.jumpPlugIn.printLayoutPlugIn.PrintLayoutPlugIn;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.Delete;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.EntirePage;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.PageSetup;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.Print;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.actions.WidthPage;

/**
 * @author FOUREAU_C
 */
public class PrintLayoutToolBar extends EnableableToolBar {
    private ToolBarButton deleteButton = new ToolBarButton(
			"Delete16.gif",
			I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.Delete"));
	private ToolBarButton	upbutton;
	private ToolBarButton	downbutton;
	private ToolBarButton	nextPageButton;
	private ToolBarButton	prevPageButton;
	private JLabel	currPageLabel;
	public PrintLayoutToolBar(final PrintLayoutFrame parent) {
		JButton button = null;
		//Save button
		button = new ToolBarButton(
				"Save16.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.Save"));
		add(button);

		//Save As button
		button = new ToolBarButton(
				"SaveAs16.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.SaveAs..."));
		add(button);

		addSeparator();

/*		//Open button
		button = new ToolBarButton(
				"../images/Open16.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.Open"));
		add(button);

		addSeparator();
*/		
		//PageSetup button
		button = new ToolBarButton(
				"PageSetup16.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.PageSetup..."));
		button.addActionListener(new PageSetup(parent));
		add(button);

		//Print button
		button = new ToolBarButton(
				"Print16.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.Print..."));
		button.addActionListener(new Print(parent));
		add(button);

		addSeparator();

		//Page button
		button = new ToolBarButton(
				"ZoomOut16.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.Page"));
		button.addActionListener(new EntirePage(parent));
		add(button);

		//widthPage button
		button = new ToolBarButton(
				"ZoomIn16.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.WidthPage"));
		button.addActionListener(new WidthPage(parent));
		add(button);
		
		addSeparator();

		//Suppresion button
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(new Delete(parent));
		add(deleteButton);
		
		/*
        //modif obedel
		ZoomTool zoomTool = new ZoomTool(); 
		add(button, "Zoom in/out", zoomTool.getIcon(),
	            new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    //It's null when the Workbench starts up. [Jon Aquino]
	                    //Or the active frame may not have a LayerViewPanel. [Jon Aquino]
	                    if (parent.getPlugInContext().getLayerViewPanel() != null) {
	                        layerViewPanelProxy.getLayerViewPanel().setCurrentCursorTool(zoomTool);
	                    }

	                    //<<TODO:DESIGN>> We really shouldn't create a new LeftClickFilter on each
	                    //click of the tool button. Not a big deal though. [Jon Aquino]
	                }
	            }, cursorToolEnableCheck);
	    */
//		widthPage button
		upbutton = new ToolBarButton(
				"Up.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.Up"));
		upbutton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0)
			{
			// TODO Auto-generated method stub
			parent.popSelected();
			}});
		add(upbutton);
		downbutton = new ToolBarButton(
				"Down.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.Down"));
		downbutton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0)
			{
			parent.pushSelected();
			
			}});
		add(downbutton);
		prevPageButton = new ToolBarButton(
				"Prev.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.Previous"));
		prevPageButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0)
			{
			parent.prevPage();
			currPageLabel.setText(""+(parent.getCurrentPage()+1));
			}});
		add(prevPageButton);
		
		currPageLabel=new JLabel("1");
		add(currPageLabel);
		
		nextPageButton = new ToolBarButton(
				"Next.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutToolBar.Next"));
		nextPageButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0)
			{
			parent.nextPage();
			currPageLabel.setText(""+(parent.getCurrentPage()+1));
			}});
		add(nextPageButton);
		
	}
	
	public ToolBarButton getDeleteButton(){
	    return deleteButton;
	}

	public class ToolBarButton extends JButton {
		private final Insets margins = new Insets(0, 0, 0, 0);

		public ToolBarButton(String imageFile, String text) {
			
			setActionCommand(text);
			setToolTipText(text);
			// modif obedel
			ImageIcon icone = com.geopista.ui.plugin.print.images.IconLoader.icon(imageFile);
			if (icone!=null)
			    setIcon(icone);
			else
			    System.err.println("Resource not found: " + imageFile);
			/*
			URL imageURL = reso.jumpPlugIn.printLayoutPlugIn.images.IconLoader.class
			.getResource(imageFile);
			if (imageURL != null) { //image found
				setIcon(new ImageIcon(imageURL, text));
			} else { //no image found
				setText(text);
				System.err.println("Resource not found: " + imageFile);
			}*/
			setMargin(margins);
			setVerticalTextPosition(BOTTOM);
			setHorizontalTextPosition(CENTER);
		}
	}
	public ToolBarButton getDownbutton()
	{
	return downbutton;
	}
	public ToolBarButton getUpbutton()
	{
	return upbutton;
	}
}