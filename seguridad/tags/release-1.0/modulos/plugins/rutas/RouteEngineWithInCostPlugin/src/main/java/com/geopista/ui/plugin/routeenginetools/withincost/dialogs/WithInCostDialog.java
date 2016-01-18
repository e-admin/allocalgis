/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.withincost.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.geopista.ui.plugin.routeenginetools.routeutil.CharacterLengthControler;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

/**
 * @author javieraragon
 *
 */
public class WithInCostDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1577728430998779421L;
	
	private JPanel rootPanel = null;
	private JPanel costPanel = null;
	private JPanel drawPanel = null;
	private OKCancelPanel okCancelPanel = null;
	private JRadioButton drawZoneRadioButton = null;
	private JRadioButton drawPathRadioButton = null;
	private JTextField costField = null;
	private ButtonGroup buttonsGroup = new ButtonGroup();
	
	
	
	public WithInCostDialog(PlugInContext context){
		super(context.getWorkbenchFrame(), 
				I18N.get("withincost","routeengine.wic.dialogtitle"), 
				true);
		this.setSize(250, 170);
		this.setLocationRelativeTo(context.getWorkbenchFrame());
		this.initialize();
		this.setResizable(false);
		this.setEnabled(true);
		this.setVisible(true);
	}



	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		
		this.setLayout(new GridBagLayout());
		
		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 20));
		
		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}
	
	private JPanel getRootPanel() {
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			rootPanel.add(getCostPanel(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			
			rootPanel.add(getDrawPanel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			

		}
		return rootPanel;
	}
	
	private JPanel getCostPanel(){
		if (costPanel == null){
			costPanel = new JPanel(new GridBagLayout());

			costPanel.add(new JLabel("Coste:"), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			costPanel.add(getCostField(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 90, 0));
		}
		return costPanel;
	}
	
	private JPanel getDrawPanel(){
		if (drawPanel == null){
			drawPanel = new JPanel(new GridBagLayout());
			
			drawPanel.add(getDrawZoneRadioButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			
			drawPanel.add(getDrawPathRadioButton(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
		}
		
		return drawPanel;
	}

	
	public JTextField getCostField() {
		if (costField == null){
			costField = new JTextField(10);
			costField.setDocument(new CharacterLengthControler(costField, 10));
			costField.setHorizontalAlignment(JTextField.RIGHT);
			costField.setText(String.valueOf(0.0));
		}
		return costField;
	}


	public JRadioButton getDrawZoneRadioButton() {
		if (drawZoneRadioButton == null) {
			drawZoneRadioButton = new JRadioButton(I18N.get("withincost","routeengine.wic.zoneradiobuttonlabel"));
			buttonsGroup.add(drawZoneRadioButton);
			drawZoneRadioButton.setEnabled(true);
			drawZoneRadioButton.setSelected(true);
			
		}
		return drawZoneRadioButton;
	}
	
	public JRadioButton getDrawPathRadioButton() {
		if (drawPathRadioButton == null){
			drawPathRadioButton = new JRadioButton(I18N.get("withincost","routeengine.wic.pathradiobuttonlabel"));
			buttonsGroup.add(drawPathRadioButton);
			setEnabled(false);
		}
		return drawPathRadioButton;
	}




	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();

			this.okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}
	
	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			setVisible(false);
			return;
		}
	}
	
	protected boolean isInputValid() {

		return true; 
	}
	
	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}
	
	void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}
	

}
