package es.satec.localgismobile.ui.screens;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import es.satec.localgismobile.ui.LocalGISWindow;

public class LoginScreen extends LocalGISWindow {

	public LoginScreen(Shell parent, int style) {
		super(parent, style);
		init();
		show();
	}

	private void init() {
		shell.setLayout(new GridLayout());
		Composite composite = new LoginScreenComposite(shell, SWT.NONE);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		composite.setLayoutData(gridData);
	}
}
