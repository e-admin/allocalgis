/**
 * FactoryToolBar.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
/**
 * 
 */
package es.satec.localgismobile.ui.widgets;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;

/**
 * @author tcarmona
 *
 */
public class FactoryToolBar {
	
	//En este composite se va a añadir toolbarActualVolver + toolbarActual
	Composite compositeToolbarActual = null;
	//Esta toolbar va a estar compuesta de toolitem para darle funcionalidad al canvas
	ToolBar toolbarActual= null;
	//toolbarActualVolver esta compuesta de dos botones uno para esconder el composite y el otro para subir 
	// al compositePadre
	ToolBar toolbarActualVolver = null;
	
	//toolbar padre a la que pertenece la toolbar actual
	ToolBar toolBarPadre = null;
	
	//toolbar que sirve para desplegar la toobar actual una vez que ha sido escondida.
	ToolBar toolbarDesplegar=null;
	
	//toolitem 
	private ToolItem toolItemVolverarriba = null;
	
	private static Logger logger = Global.getLoggerFor(FactoryToolBar.class);
	
	Image imagevolver;
	Image imagevolverPadre;
	Image imageDesplegarComposite;
	
	
	public ToolBar createToolBar(Shell sShell, Canvas canvas, String imagenEsconder, String imagenVolverPadre, boolean tienePadre, ToolBar toolBarpadre, String imagenDesplegar){

		//se crea el composite actual
		compositeToolbarActual = new Composite(canvas, SWT.NONE);
		compositeToolbarActual.setLayout(null);
		
		/**Se comprueba el tamaño de la imagen para que la toolbarVolver vaya creciendo en funcion de las imagenes**/
		Rectangle tamanoImagenEsconder= calculoTamaño(sShell,imagenEsconder);
		
		/**Añado la dos toolbar al composite**/
		toolbarActualVolver= new ToolBar(compositeToolbarActual, SWT.NONE);
		toolbarActualVolver.setBackground(Config.COLOR_APLICACION);
		
		
		toolbarActual = new ToolBar(compositeToolbarActual, SWT.NONE);
		toolbarActual.setBackground(Config.COLOR_APLICACION);
		
		
		toolbarActualVolver.setBounds(new Rectangle(0, 0,tamanoImagenEsconder.width+10,tamanoImagenEsconder.height+10));
		
		/**Item que vale para subir a la toolbar padre con esto creamos la conexion entre la toolbar actual y su padre**/
		final ToolItemSatec toolItemSatecVolverPadre= new ToolItemSatec(toolbarActual, toolBarpadre);
		
		/**Item que esconde el compositeActual*/
		ToolItem toolItemEsconder = new ToolItem(toolbarActualVolver, SWT.PUSH);
		toolItemEsconder.setToolTipText(Messages.getMessage("ToolBarMain.esconder"));
		try {
			
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(imagenEsconder);
			imagevolver = new Image(Display.getCurrent(), is);
			is.close();
			toolItemEsconder.setImage(imagevolver);
		} catch (IOException e) {
			logger.error(Messages.getMessage(e.getMessage()));
		}
		
		toolItemEsconder.addListener(SWT.Selection, new Listener(){
	
			public void handleEvent(Event event) {
				/**El compositeActual lo quito de la vision y añado la toolbar de desplegar**/
				Point compositeActual= compositeToolbarActual.getLocation();
				compositeToolbarActual.setLocation(-1000, -1000);
				toolbarDesplegar.setLocation(compositeActual);
			}
			
		});
		
		Rectangle posicionArriba= calculoTamaño(sShell,imagenVolverPadre);
		compositeToolbarActual.setBounds(new Rectangle(0, 0, sShell.getSize().x, posicionArriba.height+10));
		
		/**Compruebo si la toolbar actual tiene padre**/
		if(tienePadre){

			toolbarActualVolver.setBounds(new Rectangle(0, 0,(tamanoImagenEsconder.width*2)+15, tamanoImagenEsconder.height+10));
			
			/**toolitem imagen para subir al padre**/
			toolItemVolverarriba = new ToolItem(toolbarActualVolver, SWT.PUSH);
			InputStream is2 = this.getClass().getClassLoader().getResourceAsStream(imagenVolverPadre);
			imagevolverPadre = new Image(Display.getCurrent(), is2);
			toolItemVolverarriba.setToolTipText(Messages.getMessage("ToolBarMain.volver"));
			try {
				is2.close();
			} catch (IOException e) {
				logger.error(Messages.getMessage(e.getMessage()));
			}
			toolItemVolverarriba.setImage(imagevolverPadre);
			
			toolItemVolverarriba.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event event) {

					/**El compositeActual lo quito de la vision y añado el compositePadre**/
					Point compositeActual= compositeToolbarActual.getLocation();
					compositeToolbarActual.setLocation(-1000, -1000);
					Composite compositePadre=toolItemSatecVolverPadre.toolBarPadre.getParent();
					compositePadre.setLocation(compositeActual);
				}
			});
			toolbarActual.setBounds(new Rectangle((tamanoImagenEsconder.width*2)+15, 0, sShell.getSize().x, posicionArriba.height+10));
			compositeToolbarActual.setLocation(-1000, -1000);
		}
		
		/**Si el compositeActual no tiene padre le pongo como padre a la toolbar actual**/
		else{
			toolBarPadre= toolbarActual;
			//toolbarActual.setBounds(new Rectangle(0, 0,sShell.getSize().x, posicion.height+10));
			toolbarActual.setBounds(new Rectangle(tamanoImagenEsconder.width+10, 0, sShell.getSize().x, posicionArriba.height+10));
		}
		ToolItem toolItemSeparador = new ToolItem(toolbarActual, SWT.SEPARATOR);
		ToolItem toolItemSeparador1 = new ToolItem(toolbarActual, SWT.SEPARATOR);
		
		/**Creacion de la toolbar desplegar**/
		final Rectangle posicion1= calculoTamaño(sShell,imagenDesplegar);
		toolbarDesplegar = new ToolBar(canvas, SWT.NONE);
		toolbarDesplegar.setBounds(new Rectangle(-1000, -1000, posicion1.width+10, posicion1.height+10));
		toolbarDesplegar.setBackground(Config.COLOR_APLICACION);
		/**Añado el toolitem a la toolbar desplegar**/
		
		
		final ToolItem toolItemDesplegar = new ToolItem(toolbarDesplegar, SWT.PUSH);
		InputStream is1 = this.getClass().getClassLoader().getResourceAsStream(imagenDesplegar);
		imageDesplegarComposite = new Image(Display.getCurrent(), is1);
		toolItemDesplegar.setToolTipText(Messages.getMessage("ToolBarMain.desplegar"));
		toolItemDesplegar.setImage(imageDesplegarComposite);
		try {
			is1.close();
		} catch (IOException e) {

			logger.error(Messages.getMessage(e.getMessage()));
		}
		toolItemDesplegar.addListener(SWT.Selection, new Listener(){
	
			public void handleEvent(Event event) {

				Point localmenu=toolbarDesplegar.getLocation();
				toolbarDesplegar.setLocation(-1000, -1000);
				Composite actual=toolItemSatecVolverPadre.toolbarActual.getParent();
				actual.setLocation(localmenu);
			}
			
		});
		
		return toolbarActual;
	}
	
	
	public ToolItem getToolItemVolverarriba(){
		return toolItemVolverarriba;
	}

	private Rectangle calculoTamaño(Shell sShell,String volver) {
		InputStream is = null;
		Image img = null;
		try {
			is = getClass().getClassLoader().getResourceAsStream(volver);
			img = new Image(Display.getCurrent(), is);
			return img.getBounds();
		} catch (Exception e) {
			logger.error("Error al calcular el tamaño de la imagen", e);
		} finally {
			if (is != null) {
				try { is.close(); } catch (IOException e) {}
			}
			if (img != null) img.dispose();
		}
		return null;
// Comentado por Juanda. Problemas al redimensionar la ventana
//		Composite composite = new Composite(sShell, SWT.NONE);
//		composite.setLayout(new GridLayout());
//		ToolBar toolBar = new ToolBar(composite, SWT.NONE);
//		InputStream is = this.getClass().getClassLoader().getResourceAsStream(volver);
//		Image imgPrev=new Image(Display.getCurrent(),is);
//		ToolItem item = new ToolItem (toolBar, 0);
//		item.setImage(imgPrev);
//		return imgPrev.getBounds();
	}


	public void dispose() {
		if ((toolbarActualVolver!=null) && (!toolbarActualVolver.isDisposed()))
			toolbarActualVolver.dispose();
		
		if ((toolbarActual!=null) && (!toolbarActual.isDisposed()))
			toolbarActual.dispose();
		
		if ((toolbarDesplegar!=null) && (!toolbarDesplegar.isDisposed()))
			toolbarDesplegar.dispose();
		
		if ((imagevolver!=null) && (!imagevolver.isDisposed()))
			imagevolver.dispose();
		
		if ((imagevolverPadre!=null) && (!imagevolverPadre.isDisposed()))
			imagevolverPadre.dispose();			

		if ((imageDesplegarComposite!=null) && (!imageDesplegarComposite.isDisposed()))
			imageDesplegarComposite.dispose();			
		
	}
	

}
