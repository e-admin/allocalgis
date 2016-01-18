/**
 * LocalGISWindow.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
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
