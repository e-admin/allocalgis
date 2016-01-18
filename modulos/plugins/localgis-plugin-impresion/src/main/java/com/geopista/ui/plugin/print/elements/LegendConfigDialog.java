/**
 * LegendConfigDialog.java
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

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.legendeFrame.LegendFrame;

import com.geopista.ui.images.IconLoader;
import com.geopista.ui.legend.LegendPanel;
import com.geopista.ui.plugin.print.PrintLayoutPlugIn;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.FontChooser;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class LegendConfigDialog extends JDialog
{
	private int	height;
	private Font font;
	private javax.swing.JPanel jContentPane = null;
	private LegendPanel legendPanel = null;
	private JButton fontButton = null;
	private JPanel jPanel = null;
	private JButton increaseButton = null;
	private JButton decreaseButton = null;
	private JScrollPane jScrollPane = null;
	private JButton addBreakButton = null;
	private JButton deleteButton = null;
	private JCheckBox autoLayoutCheckBox = null;
	private JLabel heightLabel = null;
	private JPanel jPanelAux = null;
	private JComboBox mapLinkedComboBox = null;
	private LegendFrame legendFrame = null;
	/**
	 * This method initializes legendPanel	
	 * 	
	 * @return com.geopista.ui.legend.LegendPanel	
	 */    
	private LegendPanel getLegendPanel() {
		if (legendPanel == null) {
			legendPanel = new LegendPanel();
			legendPanel.setPreferredSize(new java.awt.Dimension(300,300));  // Generated
			LegendPanel.testInicialization(legendPanel);
		}
		return legendPanel;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getFontButton() {
		if (fontButton == null) {
			fontButton = new JButton();
			//fontButton.setText("Font");  // Generated
			fontButton.setText(I18N.get(PrintLayoutPlugIn.name,"legendconfig.font"));
			fontButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					FontChooser.showDialog(LegendConfigDialog.this,"Font",font);

				}
			});
		}
		return fontButton;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			heightLabel = new JLabel();
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));  // Generated
			heightLabel.setText("JLabel");  // Generated
			jPanel.add(getFontButton(), null);  // Generated
			jPanel.add(getIncreaseButton(), null);  // Generated
			jPanel.add(heightLabel, null);  // Generated
			jPanel.add(getDecreaseButton(), null);  // Generated
			jPanel.add(getAutoLayoutCheckBox(), null);  // Generated
			jPanel.add(getBreakButton(), null);  // Generated
			jPanel.add(getDeleteButton(), null);  // Generated
			jPanel.add(getLinkedMapComboBox(), null); 
		}
		return jPanel;
	}
	private JComboBox getLinkedMapComboBox() {
			if (mapLinkedComboBox == null)
			{
				mapLinkedComboBox = new JComboBox();
				List elemd=((LegendFrame)legendFrame).parent.getPage().getGraphicElement();
				int cont=1;
				for (Iterator iter = elemd.iterator(); iter.hasNext();)
				{
					GraphicElements element = (GraphicElements) iter.next();
					if (element instanceof MapFrame)
					{
						MapFrame map = (MapFrame) element;
						String mp="Mapa número "+ cont;
						mapLinkedComboBox.addItem(mp);	
						
						if (legendFrame.linkedToMapNumber == cont)
							mapLinkedComboBox.setSelectedItem(mp);
						cont++;
					}
				}				
							
			}
			return mapLinkedComboBox;
		
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getIncreaseButton() {
		if (increaseButton == null) {
			increaseButton = new JButton(IconLoader.icon("Up.gif"));

			increaseButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {  
					height= (int) Math.ceil(height*1.3);

					getLegendPanel().setSymbolHeight(height);
					heightLabel.setText(Integer.toString(height));
				}
			});
		}
		return increaseButton;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getDecreaseButton() {
		if (decreaseButton == null) {
			decreaseButton = new JButton(IconLoader.icon("Down.gif"));

			decreaseButton.addActionListener(new java.awt.event.ActionListener() { 


				public void actionPerformed(java.awt.event.ActionEvent e) {
					height = (int) Math.max(1,height/1.3);
					getLegendPanel().setSymbolHeight(height);
					heightLabel.setText(Integer.toString(height));
				}
			});
		}
		return decreaseButton;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getLegendPanel());  // Generated
			jScrollPane.setPreferredSize(new java.awt.Dimension(300,300));  // Generated
			jScrollPane.setBackground(Color.white);
			jScrollPane.getViewport().setBackground(Color.white);
		}
		return jScrollPane;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBreakButton() {
		if (addBreakButton == null) {
			addBreakButton = new JButton();
			//addBreakButton.setText("break");  // Generated
			addBreakButton.setText(I18N.get(PrintLayoutPlugIn.name,"legendconfig.break"));  // Generated

			addBreakButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					getLegendPanel().addBreak();
				}
			});
		}
		return addBreakButton;
	}
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton();
			deleteButton.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Delete.gif")));  // Generated
			deleteButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					//borra ultimo breakentry
					getLegendPanel().removeLastBreak();
					validate();
				}
			});
		}
		return deleteButton;
	}
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getAutoLayoutCheckBox() {
		if (autoLayoutCheckBox == null) {
			autoLayoutCheckBox = new JCheckBox();
			//autoLayoutCheckBox.setText("AutoLayout");  // Generated
			autoLayoutCheckBox.setText(I18N.get(PrintLayoutPlugIn.name,"legendconfig.autolayout"));  // Generated
			autoLayoutCheckBox.addChangeListener(new javax.swing.event.ChangeListener() { 
				public void stateChanged(javax.swing.event.ChangeEvent e) {    

					addBreakButton.setEnabled(!autoLayoutCheckBox.isSelected());
					deleteButton.setEnabled(!autoLayoutCheckBox.isSelected());
					getLegendPanel().setAutoLayout(autoLayoutCheckBox.isSelected());
					getLegendPanel().repaint();
				}
			});
		}
		return autoLayoutCheckBox;
	}
	public static void main(String[] args)
	{
		LegendPanel lp=new LegendPanel(30,20);
		LegendPanel.testInicialization(lp);
		LegendConfigDialog dlg= new LegendConfigDialog(lp);
	}
	/**
	 * This is the default constructor
	 */
	public LegendConfigDialog(LegendPanel lp) {
		super();

		this.legendPanel=lp;
		//lp.setSize(300,300);
		height=lp.getSymbolHeight();
		lp.setPreferredSize(lp.getSize());

		initialize();
		getAutoLayoutCheckBox().setSelected(lp.isAutoLayout());
		getLegendPanel().setEditingMode(true);
		heightLabel.setText(Integer.toString(height));
		addBreakButton.setEnabled(!autoLayoutCheckBox.isSelected());
		deleteButton.setEnabled(!autoLayoutCheckBox.isSelected());
		pack();
		setVisible(true);
		legendPanel.setEditingMode(false);
		legendPanel.setBackground(Color.white);
	}
	
	
	public LegendConfigDialog(LegendPanel lp, LegendFrame lframe) {
		super();

		this.legendPanel=lp;
		this.legendFrame = lframe;
		//lp.setSize(300,300);
		height=lp.getSymbolHeight();
		lp.setPreferredSize(lp.getSize());

		initialize();
		getAutoLayoutCheckBox().setSelected(lp.isAutoLayout());
		getLegendPanel().setEditingMode(true);
		heightLabel.setText(Integer.toString(height));
		addBreakButton.setEnabled(!autoLayoutCheckBox.isSelected());
		deleteButton.setEnabled(!autoLayoutCheckBox.isSelected());
		pack();
		setVisible(true);
		legendPanel.setEditingMode(false);
		legendPanel.setBackground(Color.white);
	}
	
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(372,300));  // Generated
		this.setModal(true);  // Generated
		this.setSize(300, 300);		
		this.setContentPane(getJContentPane());		
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);  // Generated
			jContentPane.add(getJPanel(), java.awt.BorderLayout.NORTH);  // Generated
		}
		return jContentPane;
	}
	
	/**
	 * @return
	 */
	public int getSymbolHeight()
	{
		return height;
	}
	
	public int getLinkedMap()
	{
		String selItem = (String)mapLinkedComboBox.getSelectedItem();
		int index = selItem.lastIndexOf(" ");
		return Integer.valueOf(selItem.substring(index).trim());
	}
}
