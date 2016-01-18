package com.localgis.app.gestionciudad.dialogs.printsearch.utils;


import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JToggleButton;

public class ConfigurationToggleButton extends JToggleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3684920488790715305L;

	private ArrayList<String> buttonTextList = null;
	private JLabel arrowLabel = null;	
	private String buttonText = null;

	public ConfigurationToggleButton(){
		super();
		initialize();
		
		this.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onActionPerformedDo();
			}			
		});
	}
	
	public ConfigurationToggleButton(String text){
		super();
		this.buttonText = text;
		initialize();
		
		this.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onActionPerformedDo();
			}			
		});
	}
	
	private void onActionPerformedDo() {
		if (this.isSelected()){
			this.getArrowLabel().setText("<<");
		}else{
			this.getArrowLabel().setText(">>");
		}
	}

	private void initialize() {
		this.setLayout(new GridBagLayout());

		if (this.buttonText != null && !this.buttonText.equals("")){
			this.add(new JLabel(this.buttonText));
		}
		
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
			arrowLabel = new JLabel(">>");
		}
		return arrowLabel;
	}
}
