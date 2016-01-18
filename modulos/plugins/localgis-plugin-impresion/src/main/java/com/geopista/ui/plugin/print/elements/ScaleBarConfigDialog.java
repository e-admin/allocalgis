/**
 * ScaleBarConfigDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 14-ago-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print.elements;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;

import reso.jumpPlugIn.printLayoutPlugIn.I18NPlug;
import reso.jumpPlugIn.printLayoutPlugIn.PrintLayoutPlugIn;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;

import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class ScaleBarConfigDialog extends JDialog
{

	private javax.swing.JPanel jContentPane = null;
	private JCheckBox transparencyCheckBox = null;
	private JCheckBox areasCheckBox = null;
	private JSlider numDivisionsSlider = null;
	private JLabel jLabel = null;
	private JComboBox styleComboBox = null;
	private JLabel jLabel1 = null;
	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getTransparencyCheckBox() {
		if (transparencyCheckBox == null) {
			transparencyCheckBox = new JCheckBox();
			//transparencyCheckBox.setText("ScaleBarDialog.Transparency");  // Generated
			transparencyCheckBox.setText(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.ScaleBarDialog.Transparency"));
		}
		return transparencyCheckBox;
	}
	/**
	 * This method initializes jCheckBox1	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getAreasCheckBox() {
		if (areasCheckBox == null) {
			areasCheckBox = new JCheckBox();
			//areasCheckBox.setText("ScaleBarDialog.Areas");  // Generated
			areasCheckBox.setText(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.ScaleBarDialog.Areas"));
		}
		return areasCheckBox;
	}
	/**
	 * This method initializes jSlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */    
	private JSlider getNumDivisionsSlider() {
		if (numDivisionsSlider == null) {
			numDivisionsSlider = new JSlider();
			numDivisionsSlider.setMaximum(9);  // Generated
			numDivisionsSlider.setMinimum(2);  // Generated
			numDivisionsSlider.setPaintTicks(true);  // Generated
			numDivisionsSlider.setPaintLabels(true);  // Generated
			numDivisionsSlider.setSnapToTicks(true);  // Generated
			numDivisionsSlider.setValue(3);  // Generated
			numDivisionsSlider.setMinorTickSpacing(1);  // Generated
			numDivisionsSlider.setPaintTrack(true);  // Generated
		}
		return numDivisionsSlider;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getStyleComboBox() {
		if (styleComboBox == null) {
			styleComboBox = new JComboBox();
			initCombo(styleComboBox);
			styleComboBox.setRenderer(new ListCellRenderer()
			{
				JLabel rep=new JLabel();
				public Component getListCellRendererComponent(JList arg0,
						Object arg1, int arg2, boolean arg3, boolean arg4)
				{
					ImageIcon oc=(ImageIcon) arg1;
					Image im=oc.getImage().getScaledInstance(oc.getIconWidth()*30/oc.getIconHeight(),30,Image.SCALE_SMOOTH);
					rep.setIcon(new ImageIcon(im));
					return (JLabel) rep;
				}
			});
			styleComboBox.setEnabled(false);  // Generated

		}
		return styleComboBox;
	}
	/**
	 * @param comboBox
	 */
	private void initCombo(JComboBox comboBox)
	{

		comboBox.setEnabled(false);
		DefaultComboBoxModel cmodel=new DefaultComboBoxModel(scaleBarTypes);
		comboBox.setModel(cmodel);
	}
	public static Icon scaleBarTypes[]=
		new Icon[]
		         {
		IconLoader.icon("escala1.jpg"),
		IconLoader.icon("escala2.jpg"),
		IconLoader.icon("escala3.jpg"),
		IconLoader.icon("escala4.jpg"),
		IconLoader.icon("escala5.jpg"),

		         };
	private OKCancelPanel OKCancelPanel = null;
	private ScaleBarFrame	sbframe;
	private JComboBox mapLinkedBomboBox = null;
	/**
	 * This method initializes OKCancelPanel	
	 * 	
	 * @return com.vividsolutions.jump.workbench.ui.OKCancelPanel	
	 */    
	private OKCancelPanel getOKCancelPanel() {
		if (OKCancelPanel == null) {
			OKCancelPanel = new OKCancelPanel();
			OKCancelPanel.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					if (OKCancelPanel.wasOKPressed())
						setFields();
					setVisible(false);
				}
			});
		}
		return OKCancelPanel;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getMapLinkedBomboBox()
	{
		if (mapLinkedBomboBox == null)
		{
			mapLinkedBomboBox = new JComboBox();

			List elemd=sbframe.getParent().getPage().getGraphicElement();
			int cont=1;
			for (Iterator iter = elemd.iterator(); iter.hasNext();)
			{
				GraphicElements element = (GraphicElements) iter.next();
				if (element instanceof MapFrame)
				{
					MapFrame map = (MapFrame) element;
					String mp="Mapa número"+ cont;
					mapLinkedBomboBox.addItem(mp);	
					if (sbframe.linkedToMapNumber == cont)
						mapLinkedBomboBox.setSelectedItem(mp);
					cont++;
				}

			}
		}
		return mapLinkedBomboBox;
	}
	public static void main(String[] args)
	{
		ScaleBarConfigDialog dlg=new ScaleBarConfigDialog(null);
		dlg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dlg.setVisible(true);
		System.exit(0);

	}
	/**
	 * This is the default constructor
	 */
	public ScaleBarConfigDialog() {
		super();
		initialize();
	}
	/**
	 * @param object
	 */
	public ScaleBarConfigDialog(Frame object)
	{
		super(object);
		initialize();
		initCombo(getStyleComboBox());
	}
	public ScaleBarConfigDialog(Frame object, ScaleBarFrame sbframe)
	{
		super(object);
		this.sbframe=sbframe;
		initialize();
		initCombo(getStyleComboBox());
		initFields();
	}
	/**
	 * 
	 */
	private void initFields()
	{
		getTransparencyCheckBox().setSelected(sbframe.getTransparency()<255);
		getAreasCheckBox().setSelected(sbframe.isPaintingArea());
		getNumDivisionsSlider().setValue(sbframe.getIncrementCount());

	}
	private void setFields()
	{
		sbframe.setTransparency(getTransparencyCheckBox().isSelected()?128:255);
		sbframe.setPaintingArea(getAreasCheckBox().isSelected());
		sbframe.setIncrementCount(getNumDivisionsSlider().getValue());
		sbframe.setLinkedToMapNumber(getMapLinkedBomboBox().getSelectedIndex()+1);
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setResizable(false);  // Generated
		this.setModal(true);  // Generated
		this.setSize(449, 210);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints.gridy = 4;  // Generated
			gridBagConstraints.weightx = 1.0;  // Generated
			gridBagConstraints.gridx = 2;  // Generated
			jLabel1 = new JLabel();
			jLabel = new JLabel();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 1;  // Generated
			gridBagConstraints1.gridy = 0;  // Generated
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;  // Generated
			gridBagConstraints1.gridwidth = 2;  // Generated
			gridBagConstraints2.gridx = 1;  // Generated
			gridBagConstraints2.gridy = 1;  // Generated
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;  // Generated
			gridBagConstraints2.gridwidth = 2;  // Generated
			gridBagConstraints3.gridx = 2;  // Generated
			gridBagConstraints3.gridy = 2;  // Generated
			gridBagConstraints3.weightx = 1.0;  // Generated
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints4.gridx = 1;  // Generated
			gridBagConstraints4.gridy = 2;  // Generated
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;  // Generated
			jLabel.setText(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.ScaleBarDialog.NumDivision"));  // Generated
			gridBagConstraints5.gridx = 2;  // Generated
			gridBagConstraints5.gridy = 3;  // Generated
			gridBagConstraints5.weightx = 1.0;  // Generated
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints6.gridx = 1;  // Generated
			gridBagConstraints6.gridy = 3;  // Generated
			jLabel1.setText(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.ScaleBarDialog.Style"));  // Generated
			gridBagConstraints7.gridx = 1;  // Generated
			gridBagConstraints7.gridy = 5;  // Generated
			gridBagConstraints7.gridwidth = 2;  // Generated
			jContentPane.add(getTransparencyCheckBox(), gridBagConstraints1);  // Generated
			jContentPane.add(getAreasCheckBox(), gridBagConstraints2);  // Generated
			jContentPane.add(getNumDivisionsSlider(), gridBagConstraints3);  // Generated
			jContentPane.add(jLabel, gridBagConstraints4);  // Generated
			jContentPane.add(getStyleComboBox(), gridBagConstraints5);  // Generated
			jContentPane.add(jLabel1, gridBagConstraints6);  // Generated
			jContentPane.add(getOKCancelPanel(), gridBagConstraints7);  // Generated
			jContentPane.add(getMapLinkedBomboBox(), gridBagConstraints);  // Generated
		}
		return jContentPane;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
