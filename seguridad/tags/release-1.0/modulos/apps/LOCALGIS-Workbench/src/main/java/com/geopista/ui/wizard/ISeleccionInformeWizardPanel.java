package com.geopista.ui.wizard;

import java.util.Map;

public interface ISeleccionInformeWizardPanel {

	public abstract void enteredPanelFromLeft(Map dataMap);

	public abstract void exitingToRight() throws Exception;

	public abstract boolean isInputValid();

}