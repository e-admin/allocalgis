/**
 * GeoMarketingToggleButton.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.app.gestionciudad.plugins.geomarketing.utils;


import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JToggleButton;

public class GeoMarketingToggleButton extends JToggleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3684920488790715305L;

	private ArrayList<String> buttonTextList = null;
	private JLabel arrowLabel = null;	
	
	private String activeText = null;
	private String desactiveText = null;

	public GeoMarketingToggleButton(String activeAditionalText, String desactiveAditionalText){
		super();
		this.activeText = activeAditionalText;
		this.desactiveText = desactiveAditionalText;
		
		initialize();
		
		
		this.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onActionPerformedDo();
			}			
		});
	}
	
	private void onActionPerformedDo() {
		String textToShow = "";
		if (this.isSelected()){
			if (this.desactiveText != null){
				textToShow = desactiveText + " >>";
			}
			this.getArrowLabel().setText(textToShow);
		}else{
			if (this.activeText != null){
				textToShow = activeText + " <<";
			}
			this.getArrowLabel().setText(textToShow);
		}
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());

		this.add(getArrowLabel());
//				, 
//				new GridBagConstraints(0, 0, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
//						GridBagConstraints.NONE, 
//						new Insets(0, 5, 0, 5), 0, 0));
		
//		this.add(new JLabel("Config"), 
//				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
//						GridBagConstraints.NONE, 
//						new Insets(0, 5, 0, 5), 0, 0));

		
//		for(int i = 0; i < buttonTextList.size(); i ++){
//			this.add(new JLabel(buttonTextList.get(i)), 
//					new GridBagConstraints(0, i+1, 2, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
//							GridBagConstraints.HORIZONTAL, 
//							new Insets(0, 5, 0, 5), 0, 0));
//		}
	}

	private JLabel getArrowLabel(){
		if (arrowLabel == null){
			String textToShow = "";
			if (this.activeText != null){
				textToShow = this.activeText + " >>";
			}
			arrowLabel = new JLabel(textToShow);
		}
		return arrowLabel;
	}
}
