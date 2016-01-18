package com.geopista.ui.plugin.routeenginetools.routeutil;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vividsolutions.jump.workbench.ui.MultiInputDialog;

public class MultiInputDialogWithOutImage extends MultiInputDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4180228556502659089L;
	private JPanel imagePanel;


	public MultiInputDialogWithOutImage(final Frame frame, String title, boolean modal) {
		super(frame, title, modal);
	}
	public MultiInputDialogWithOutImage() {
		super(null, "", false);
	}


	@Override
	public void setSideBarImage(Icon icon) {
		//Add imageLabel only if #setSideBarImage is called. Otherwise the margin
		//above the description will be too tall. [Jon Aquino]
		imagePanel.add(
				new JLabel(),
				new GridBagConstraints(
						0,
						0,
						1,
						1,
						0.0,
						0.0,
						GridBagConstraints.NORTHWEST,
						GridBagConstraints.HORIZONTAL,
						new Insets(10, 10, 0, 10),
						0,
						0));
		imagePanel.setVisible(false);
		//	        imageLabel.setIcon(icon);
	}

	@Override
	public void setSideBarDescription(String description) {
		imagePanel.setVisible(false);
		//	        descriptionTextArea.setText(description);
	}

}
