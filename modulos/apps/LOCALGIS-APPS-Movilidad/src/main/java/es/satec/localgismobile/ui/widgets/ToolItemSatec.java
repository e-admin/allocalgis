/**
 * ToolItemSatec.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.widgets;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ToolItemSatec{
	ToolBar toolbarActual=null;
	ToolBar toolbarHijos= null;
	ToolBar toolBarPadre = null;
	ToolBar toolbarVolver = null;
	ToolItem itemHijo = null;
	
	Image imagepadre;

	
	public ToolItemSatec(ToolBar toolbarPert, ToolBar toolbarHi, ToolBar toolBarPa) {
		toolbarActual=toolbarPert;
		toolbarHijos= toolbarHi;
		toolBarPadre = toolBarPa;
	}
	public ToolItemSatec(ToolBar toolbarAct, ToolBar toolBarPa) {
		toolbarActual= toolbarAct;
		toolBarPadre = toolBarPa;
	}
	
	public void makeItem(Shell sShell,int estilo, String imagen, Listener listener,String toolTipText){
		//final Rectangle posicion= calculoTamaño(sShell,imagen);
		//Rectangle posicion1=Display.getDefault().getClientArea();
		toolbarActual.setBounds(new Rectangle(toolbarActual.getBounds().x, toolbarActual.getBounds().y,Display.getDefault().getClientArea().width, toolbarActual.getBounds().height));
		//Rectangle comp=toolbarActual.getParent().getBounds();
		//toolbarActual.getParent().setBounds(comp.x, comp.y, comp.width, posicion.height+5);
		itemHijo = new ToolItem(toolbarActual, estilo);
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(imagen);
		imagepadre = new Image(Display.getCurrent(), is);
		itemHijo.setImage(imagepadre);
		itemHijo.setToolTipText(toolTipText);
		/*InputStream is1 = this.getClass().getClassLoader().getResourceAsStream(PantallaPrincip.getProperty("prueba1"));
		Image imagepadre1 = new Image(Display.getCurrent(), is1);
		itemHijo.setDisabledImage(imagepadre1);*/
		//itemHijo.setHotImage(imagepadre1);
		
		itemHijo.addListener(SWT.Selection, listener);
	}
	
	/*private Rectangle calculoTamaño(Shell sShell,String volver) {

		Composite composite = new Composite(sShell, SWT.NONE);
		composite.setLayout(new GridLayout());
		ToolBar toolBar = new ToolBar(composite, SWT.NONE);
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(volver);
		Image imgPrev=new Image(Display.getCurrent(),is);
		ToolItem item = new ToolItem (toolBar, 0);
		item.setImage(imgPrev);
		return imgPrev.getBounds();
	}*/
	
	public void dispose(){	
		
		if (itemHijo!=null && !itemHijo.isDisposed()) {
			if (itemHijo.getImage()!=null)
				itemHijo.getImage().dispose();
			
			/*Listener[] listaListener=itemHijo.getListeners(SWT.Selection);
			for (int i=0;i<listaListener.length;i++){
				Listener listener=listaListener[i];
				itemHijo.removeListener(SWT.Selection, listener);
			}*/
			itemHijo.dispose();
		}
		if ((imagepadre!=null) && (!imagepadre.isDisposed()))
			imagepadre.dispose();

	}
	
}
