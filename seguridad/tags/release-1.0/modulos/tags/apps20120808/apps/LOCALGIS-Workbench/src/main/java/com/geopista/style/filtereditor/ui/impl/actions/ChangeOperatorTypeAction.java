/*
 * Created on 22-sep-2004
 */
package com.geopista.style.filtereditor.ui.impl.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * @author Enxenio, SL
 */
public class ChangeOperatorTypeAction extends AbstractAction {


	public ChangeOperatorTypeAction(String name, int targetOperatorType) {
		super(name);
		_targetOperatorType = targetOperatorType;
	}

	public void actionPerformed(ActionEvent actionEvent) {
	}
	
	public int getTargetOperatorType() {
		return _targetOperatorType;
	}
	
	private int _targetOperatorType;

}
