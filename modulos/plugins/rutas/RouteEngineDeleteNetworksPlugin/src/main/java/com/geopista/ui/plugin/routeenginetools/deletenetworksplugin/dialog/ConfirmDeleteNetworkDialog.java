/**
 * ConfirmDeleteNetworkDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.deletenetworksplugin.dialog;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vividsolutions.jump.workbench.ui.OKCancelPanel;

public class ConfirmDeleteNetworkDialog  extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1104225877051719273L;
	
	private OKCancelPanel okCancelPanel = null;
	private JPanel rootPanel = null;
	private String message = null;
	
	public ConfirmDeleteNetworkDialog(Frame frame, String title, String message){
		super(frame, title, true);
		this.message = message;
		this.setSize(450, 400);
		this.setLocationRelativeTo(frame);
		this.initialize();
		this.setResizable(false);
		this.setEnabled(true);
		this.pack();
		this.setVisible(true);
	}
	
	private void initialize() {
		// TODO Auto-generated method stub
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});
		
		this.setLayout(new GridBagLayout());

		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));


		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}

	private Component getRootPanel() {
		// TODO Auto-generated method stub
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());
					
			rootPanel.add(new JLabel(this.message), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0) );
		}
		return rootPanel;
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
