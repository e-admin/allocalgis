package es.satec.localgismobile.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public abstract class LocalGISWindow {
	
	protected Shell shell;
	protected Menu menuBar;
	
	public LocalGISWindow(Display display, int style) {
		shell = new Shell(display, style | SWT.CLOSE | SWT.RESIZE);
		createMenuBar();
		shell.setMaximized(true);
	}
	
	public LocalGISWindow(Display display) {
		this(display, SWT.NONE);
	}
	
	public LocalGISWindow(Shell parent, int style) {
		shell = new Shell(parent, style | SWT.CLOSE | SWT.RESIZE | SWT.APPLICATION_MODAL);
		createMenuBar();
		shell.setMaximized(true);
	}
	
	public LocalGISWindow(Shell parent) {
		this(parent, SWT.NONE);
	}
	
	private void createMenuBar() {
		menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
	}

	public void show() {
		shell.setVisible(true);
	}
	
	public Menu getMenuBar() {
		return menuBar;
	}

	public Shell getShell() {
		return shell;
	}
	protected void mensajeError(String mensaje){
		MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		mb.setMessage(mensaje);
		mb.open();
	}
	
	
	public void dispose(){

		if ((menuBar!=null) && (!menuBar.isDisposed()))
			menuBar.dispose();
		if ((shell!=null) && (!shell.isDisposed()))
			shell.dispose();
		
		
	}
}
