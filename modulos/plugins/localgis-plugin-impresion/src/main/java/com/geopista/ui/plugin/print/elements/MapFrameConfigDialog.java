/**
 * MapFrameConfigDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * 
 * Created on 12-ago-2005 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.print.elements;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import reso.jumpPlugIn.printLayoutPlugIn.I18NPlug;
import reso.jumpPlugIn.printLayoutPlugIn.PrintLayoutPlugIn;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.graphicText.ColorChooserDialog;

import com.geopista.reports.gui.MapScaleDialog;
import com.geopista.ui.plugin.print.PrintLayoutFrame;
import com.geopista.ui.plugin.print.images.IconLoader;
import com.geopista.util.UtilsPrintPlugin;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
/**
 * TODO Documentación
 * @author juacas
 *
 */
public class MapFrameConfigDialog extends JDialog
{
	private Map infoEscalas = null;  
	private javax.swing.JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JLabel demolabel = null;
	private OKCancelPanel OKCancelPanel = null;
	private JComboBox jComboBox = null;
	private JLabel scaleLabel = null;
	
	
	private javax.swing.JButton jButtonAddScale;
    private MapScaleDialog mapScaleDialog = new MapScaleDialog();  
	
	
	/**
	 * 
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new BorderPanel();
			jPanel.setPreferredSize(new java.awt.Dimension(200,100));  // Generated
		}
		return jPanel;
	}
	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			demolabel = new JLabel();
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.X_AXIS));  // Generated
			demolabel.setText("JLabel");  // Generated
			demolabel.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/EditWMSLayer.jpg")));  // Generated
			demolabel.setPreferredSize(new java.awt.Dimension(100,100));  // Generated
			demolabel.setMaximumSize(new java.awt.Dimension(100,100));  // Generated
			demolabel.setMinimumSize(new java.awt.Dimension(100,100));  // Generated
			jPanel1.add(demolabel, null);  // Generated
		}
		return jPanel1;
	}
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
	private JComboBox getJComboBox() {
		if (jComboBox == null)
			jComboBox = new JComboBox(infoEscalas.values().toArray());
		return jComboBox;
	}

	public static void main(String[] args)
	{
		I18N.setPlugInRessource(PrintLayoutPlugIn.name,"language.printLayout");
		MapFrameConfigDialog dlg=new MapFrameConfigDialog(null);
		dlg.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		dlg.setVisible(true);

	}
	/**
	 * This is the default constructor
	 */
	public MapFrameConfigDialog(Frame fr) {
		super(fr);
		infoEscalas = (Map)((PrintLayoutFrame) fr).getExtentManager().getParameters().get(UtilsPrintPlugin.PARAM_CONFIG_INFO_ESCALAS);
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setModal(true);
		this.setSize(345, 222);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 1;  // Generated
			gridBagConstraints1.gridy = 2;  // Generated
			scaleLabel = new JLabel();
			scaleLabel.setText(I18N.get(PrintLayoutPlugIn.name,"PrintLayoutPlugIn.Scale"));  // Generated
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints.gridy = 2;  // Generated
			gridBagConstraints.weightx = 1.0;  // Generated
			gridBagConstraints.gridx = 2;  // Generated
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new GridBagLayout());
			gridBagConstraints3.gridx = 2;  // Generated
			gridBagConstraints3.gridy = 0;  // Generated
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;  // Generated
			gridBagConstraints4.gridx = 1;  // Generated
			gridBagConstraints4.gridy = 0;  // Generated
			gridBagConstraints7.gridx = 1;  // Generated
			gridBagConstraints7.gridy = 3;  // Generated
			gridBagConstraints7.gridwidth = 3;  // Generated
			jContentPane.add(getJPanel(), gridBagConstraints3);  // Generated
			jContentPane.add(getJPanel1(), gridBagConstraints4);  // Generated
			jContentPane.add(getOKCancelPanel(), gridBagConstraints7);  // Generated
			jContentPane.add(getJComboBox(), gridBagConstraints);  // Generated
			jContentPane.add(scaleLabel, gridBagConstraints1);  // Generated
			
			//YR--
	        
			jButtonAddScale = new javax.swing.JButton();
	        jButtonAddScale.setText("+");
	        jButtonAddScale.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButtonAddScaleActionPerformed(evt);
	            }
	        });
	        GridBagConstraints gridBagConstraints10 = new java.awt.GridBagConstraints();
	        gridBagConstraints10.gridx = 3;
	        gridBagConstraints10.gridy = 2;
	        gridBagConstraints10.insets = new java.awt.Insets(5, 5, 5, 5);
	        jContentPane.add(jButtonAddScale, gridBagConstraints10);			
			//--
		}
		return jContentPane;
	}

	private class BorderPanel extends JPanel {
		private JPanel borderPanel = new JPanel();

		private JPanel choisePanel = new JPanel();

		private CheckboxGroup borderChoise = new CheckboxGroup();

		private Checkbox yesCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Yes"),
				borderChoise, false);

		private Checkbox noCheckBox = new Checkbox(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.No"),
				borderChoise, true);


		private JLabel borderColor = new JLabel(
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Color"));

		private JPanel color = new JPanel();

		private ColorButton borderColorButton = new ColorButton(
				"images/ColorPalette.gif",
				I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.ColorPalette"));

		private JPanel borderThicknessPanel = new JPanel();

		private CheckboxGroup borderThickness = new CheckboxGroup();

		private Checkbox oneCheckBox = new Checkbox(
				I18N.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.One"),
				borderThickness, true);

		private Checkbox twoCheckBox = new Checkbox(
				I18N.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Two"),
				borderThickness, false);

		private Checkbox threeCheckBox = new Checkbox(
				I18N.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Three"),
				borderThickness, false);

		private Checkbox fourCheckBox = new Checkbox(
				I18N.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Four"),
				borderThickness, false);

		private Checkbox fiveCheckBox = new Checkbox(
				I18N.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Five"),
				borderThickness, false);

		private Checkbox sixCheckBox = new Checkbox(
				I18N.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Six"),
				borderThickness, false);

		public Border	border=null;

		public BorderPanel() {
			setBorder(BorderFactory
					.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.Name")));
			choisePanel.setPreferredSize(new Dimension(400, 20));
			choisePanel.setLayout(new GridLayout(1,2));
			choisePanel.add(yesCheckBox);
			yesCheckBox.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent arg0) {
					setEnabled(true);
				}});
			choisePanel.add(noCheckBox);
			noCheckBox.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent arg0) {
					setEnabled(false);
					border=null;
					demolabel.setBorder(null);
				}});

			color.setOpaque(true);
			color.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.RAISED));
			color.setPreferredSize(new Dimension(160, 20));
			color.setBackground(Color.WHITE);
			color.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					border=BorderFactory.createLineBorder(color
							.getBackground(), Integer.valueOf(
									borderThickness.getSelectedCheckbox().getLabel())
									.intValue());
					demolabel.setBorder(border);
				}
			});

			borderColorButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new ColorChooserDialog(
							I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.BorderColor"),
							color);
				}
			});

			borderThicknessPanel
			.setBorder(BorderFactory
					.createTitledBorder(I18NPlug.get(PrintLayoutPlugIn.name,"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.options.TextStyleChooserDialog.BorderPanel.BorderThickness")));
			borderThicknessPanel.setLayout(new GridLayout(1, 6));
			borderThicknessPanel.add(oneCheckBox);
			oneCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(twoCheckBox);
			twoCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(threeCheckBox);
			threeCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(fourCheckBox);
			fourCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(fiveCheckBox);
			fiveCheckBox.addItemListener(new ItemChange());
			borderThicknessPanel.add(sixCheckBox);
			sixCheckBox.addItemListener(new ItemChange());

			borderThicknessPanel.setPreferredSize(new Dimension(395, 45));

			borderPanel.setPreferredSize(new Dimension(400, 70));
			borderPanel.setLayout(new GridBagLayout());

			borderPanel.add(borderColor, new GridBagConstraints(1, 1, 1, 1,
					0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			borderPanel.add(color, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0), 0, 0));
			borderPanel.add(borderColorButton, new GridBagConstraints(3, 1, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			borderPanel
			.add(borderThicknessPanel, new GridBagConstraints(1, 2, 3,
					1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.REMAINDER,
					new Insets(0, 0, 0, 0), 0, 0));
			setLayout(new GridBagLayout()); 

			add(choisePanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.REMAINDER,
					new Insets(0, 0, 0, 0), 0, 0));

			add(borderPanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.REMAINDER,
					new Insets(0, 0, 0, 0), 0, 0));
		}

		public void setEnabled(boolean isEnabled){
			borderPanel.setEnabled(isEnabled);
			color.setEnabled(isEnabled);
			borderColor.setEnabled(isEnabled);
			borderColorButton.setEnabled(isEnabled);
			borderThicknessPanel.setEnabled(isEnabled);
			oneCheckBox.setEnabled(isEnabled);
			twoCheckBox.setEnabled(isEnabled);
			threeCheckBox.setEnabled(isEnabled);
			fourCheckBox.setEnabled(isEnabled);
			fiveCheckBox.setEnabled(isEnabled);
			sixCheckBox.setEnabled(isEnabled);
		}

		private class ItemChange implements ItemListener {
			public void itemStateChanged(ItemEvent e) {
				demolabel.setBorder(BorderFactory.createLineBorder(color
						.getBackground(), Integer.valueOf(
								borderThickness.getSelectedCheckbox().getLabel())
								.intValue()));
			}
		}

		public void setPanelBorder(LineBorder border)
		{
//			initialisation de la bordure
			if (border instanceof LineBorder) {
				yesCheckBox.setState(true);
				setEnabled(true);
				color.setBackground(((LineBorder) border)
						.getLineColor());
				switch (((LineBorder) border).getThickness()) {
				case 2:
					twoCheckBox.setState(true);
					break;
				case 3:
					threeCheckBox.setState(true);
					break;
				case 4:
					fourCheckBox.setState(true);
					break;
				case 5:
					fiveCheckBox.setState(true);
					break;
				case 6:
					sixCheckBox.setState(true);
					break;
				default:
					oneCheckBox.setState(true);
				break;
				}
			}else if (border == null){
				noCheckBox.setState(true);
				setEnabled(false);
			}
			demolabel.setBorder(border);
		}
	}
	private class ColorButton extends JButton {
		private final Insets margins = new Insets(0, 0, 0, 0);

		public ColorButton(String imageFile, String text) {

			setActionCommand(text);
			setToolTipText(text);

			setIcon(IconLoader.icon("ColorPalette.gif"));
			setMargin(margins);
			setVerticalTextPosition(BOTTOM);
			setHorizontalTextPosition(CENTER);
		}
	}
	/**
	 * @param extentType 1= full extent 2=current
	 */
	public void setExtentType(int extentType)
	{
		Object elem=null;
		if (extentType==MapFrame.CURRENT_EXTENT) // current
			elem = infoEscalas.get(new Integer(-2));
		else if (extentType==MapFrame.FULL_EXTENT) // full
				elem = infoEscalas.get(new Integer(-1));
		else
			elem = infoEscalas.get(new Integer(extentType));
		
		getJComboBox().setSelectedItem(elem);
	}
	/**
	 * 
	 * @return 1=full extent 2=current extent 0=user defined
	 */
	public int getExtentType() {
		int extendType = -1;
		String sel = UtilsPrintPlugin.getClaveMapaPorValor(infoEscalas, getJComboBox().getSelectedItem().toString());
		if ("-1".equals(sel))
			extendType = MapFrame.FULL_EXTENT;
		else if ("-2".equals(sel))
			extendType = MapFrame.CURRENT_EXTENT;
		else 
			extendType = Integer.parseInt(sel);
		return extendType;
	}
	
	private int processExtentType(String sel)
	{
		int extendType = -1;
		if ("-1".equals(sel))
			extendType = MapFrame.FULL_EXTENT;
		else if ("-2".equals(sel))
			extendType = MapFrame.CURRENT_EXTENT;
		else 
			extendType = Integer.parseInt(sel);
		return extendType;
	}
	
	/**
	 * @return
	 */
	public boolean isOkpressed()
	{

		return getOKCancelPanel().wasOKPressed();
	}

	/**
	 * @return
	 */
	public LineBorder getBorder()
	{

		return (LineBorder) ((BorderPanel)getJPanel()).border;
	}
	/**
	 * @param border
	 */
	public void setPanelBorder(LineBorder border)
	{
		((BorderPanel)getJPanel()).setPanelBorder(border);

	}
	
	//YR--
    private void jButtonAddScaleActionPerformed(java.awt.event.ActionEvent evt) {                                                      
        mapScaleDialog.setVisible(true);
        String descEscala = mapScaleDialog.getScale();
        if (!descEscala.equals("")) {
            mapScaleDialog.setScale("");
            String idEscala = UtilsPrintPlugin.getClaveMapaPorValor(infoEscalas, descEscala);
            if (idEscala == null) {
            	String datosEscala[] =  descEscala.split(":");
            	jComboBox.addItem(descEscala);
            	infoEscalas.put(Integer.parseInt(datosEscala[1]), descEscala);
            }
            //Seleccionar escala
            jComboBox.setSelectedItem(descEscala);
        }
    }
	//--
	
}  //  @jve:decl-index=0:visual-constraint="14,13"
