/**
 * ForPrintLegendPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 16-jul-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print.elements;

import java.awt.BorderLayout;
import java.awt.Graphics;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.Page;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.legendeFrame.LegendFrame;

/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ForPrintLegendPanel extends OnScreenLegendPanel
{

	/**
	 * This is the default constructor
	 */
	public ForPrintLegendPanel() {
		super();
		
	}
	/**
	 * @param frame
	 */
	public ForPrintLegendPanel(LegendFrame frame)
	{
	
	super(frame);
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	protected  void initialize() {
	getLegendPanel().setOpaque(false);
	getLegendPanel().setEditingMode(false);
	getLegendPanel().setAutoLayout(true);
	
		BorderLayout borderLayout9 = new BorderLayout();
		this.setLayout(borderLayout9);  // Generated
		

		this.setBackground(java.awt.SystemColor.text);  // Generated
		this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.SystemColor.controlShadow,1));  // Generated
		
		
		this.add(getLegendPanel(), java.awt.BorderLayout.CENTER); 
	}
	public void fixerDimensions()
	   {
	   if (legendFrame==null)return;
	   PrintLayoutFrame parent=legendFrame.parent;
	   switch (legendFrame.parent.getZoomActif())
	   {
	   case PrintLayoutFrame.PAGE_ENTIERE :
	   super
	   .setBounds(
	   		(int) ((legendFrame.getGraphicElementsOnScreen()
	   				.getBounds().getX() * parent
					.getPrintLayoutPreviewPanel()
					.getPreviewPanel().getPage()
					.getPageForPrint().getWidth()) / ((Page.PageDrawOnScreen) parent
							.getPrintLayoutPreviewPanel()
							.getPreviewPanel().getPage()
							.getPageDrawOnScreen()).getCenter()
							.getWidth()),
							(int) ((legendFrame.getGraphicElementsOnScreen()
									.getBounds().getY() * parent
									.getPrintLayoutPreviewPanel()
									.getPreviewPanel().getPage()
									.getPageForPrint().getHeight()) / ((Page.PageDrawOnScreen) parent
											.getPrintLayoutPreviewPanel()
											.getPreviewPanel().getPage()
											.getPageDrawOnScreen()).getCenter()
											.getHeight()),
											(int) ((legendFrame.getGraphicElementsOnScreen()
													.getBounds().getWidth() * parent
													.getPrintLayoutPreviewPanel()
													.getPreviewPanel().getPage()
													.getPageForPrint().getWidth()) / ((Page.PageDrawOnScreen) parent
															.getPrintLayoutPreviewPanel()
															.getPreviewPanel().getPage()
															.getPageDrawOnScreen()).getCenter()
															.getWidth()),
															(int) ((legendFrame.getGraphicElementsOnScreen()
																	.getBounds().getHeight() * parent
																	.getPrintLayoutPreviewPanel()
																	.getPreviewPanel().getPage()
																	.getPageForPrint().getHeight()) / 
																	((Page.PageDrawOnScreen) parent.getPrintLayoutPreviewPanel()
																			.getPreviewPanel().getPage()
																			.getPageDrawOnScreen()).getCenter()
																			.getHeight()));
	   
	   //Calcula alto de símbolo

		int symbh= (int) ((((OnScreenLegendPanel)legendFrame.getGraphicElementsOnScreen())
				.getSymbolHeight() * parent
				.getPrintLayoutPreviewPanel()
				.getPreviewPanel().getPage()
				.getPageForPrint().getHeight()) / 
				((Page.PageDrawOnScreen) parent.getPrintLayoutPreviewPanel()
						.getPreviewPanel().getPage()
						.getPageDrawOnScreen()).getCenter()
						.getHeight());
		setSymbolHeight(symbh);
	   break;
	   case PrintLayoutFrame.LARGEUR_PAGE :
	   super.setBounds((int) legendFrame.getGraphicElementsOnScreen()
	   		.getBounds().getX(), (int) legendFrame
			.getGraphicElementsOnScreen().getBounds().getY(),
			(int) legendFrame.getGraphicElementsOnScreen().getBounds()
			.getWidth(), (int) legendFrame
			.getGraphicElementsOnScreen().getBounds()
			.getHeight());
	   setSymbolHeight(((OnScreenLegendPanel)legendFrame.getGraphicElementsOnScreen())
				.getSymbolHeight());
	   break;
	   }
	   super.setLocation((int) super.getBounds().getX(), (int) super.getBounds()
	   		.getY());
	   super.repaint();
	   }
	public void print(Graphics arg0)
	{
	// TODO Auto-generated method stub
	super.print(arg0);
	}
	public void printAll(Graphics arg0)
	{
	// TODO Auto-generated method stub
	super.printAll(arg0);
	}
	protected void printChildren(Graphics arg0)
	{
	// TODO Auto-generated method stub
	super.printChildren(arg0);
	}
	protected void printComponent(Graphics arg0)
	{
	// TODO Auto-generated method stub
	super.printComponent(arg0);
	}
	public void paintAll(Graphics arg0)
	{
	// TODO Auto-generated method stub
	super.paintAll(arg0);
	}
}
