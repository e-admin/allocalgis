/*
 * Created on 22-sep-2004
 *
 */
package com.geopista.style.filtereditor.ui.impl.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * @author enxenio s.l.
 *
 */
public class AddOperatorChildAction extends AbstractAction {

	private int _targetOperatorType;

	public AddOperatorChildAction(String name, int targetOperatorType) {
		super(name);
		_targetOperatorType = targetOperatorType;
	}

	public void actionPerformed(ActionEvent e) {}

	public int getTargetOperatorType() {
		return _targetOperatorType;
	}
}
