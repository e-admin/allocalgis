/**
 * MenuScreenComposite.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import es.satec.localgismobile.fw.Config;

public class MenuScreenComposite extends Composite {

	private Canvas imageCanvas = null;

	public MenuScreenComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		gridData.verticalAlignment = GridData.CENTER;
		setLayout(new GridLayout());
		/*label = new Label(this, SWT.NONE);
		Font font = new Font(Display.getCurrent(), "Arial", 20, SWT.BOLD);
		label.setText(Messages.getMessage("MenuScreenComposite_NombreProyecto"));
		label.setLayoutData(gridData);
		label.setBackground(new Color(Display.getCurrent(), new RGB(198,226,255)));//TODO puesto el backgroung a mano*/
		createImageCanvas();
	}

	/**
	 * This method initializes imageCanvas	
	 *
	 */
	private void createImageCanvas() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		this.setBackground(Config.COLOR_APLICACION);
		imageCanvas = new Canvas(this, SWT.NONE);
		imageCanvas.setBackground(Config.COLOR_APLICACION);
		imageCanvas.setLayoutData(gridData);
		imageCanvas.addPaintListener(new org.eclipse.swt.events.PaintListener() {
			public void paintControl(org.eclipse.swt.events.PaintEvent e) {
				if(ClassLoader.getSystemClassLoader().getResourceAsStream(Config.prResources.getProperty("LogoLocagis"))!=null){
					int tamanoLogoWidth=0;
					int tamanoLogoHeigth=0;
					/*Imagen con el logo del proyecto*/
					ImageData logoImageData= new ImageData(ClassLoader.getSystemClassLoader().getResourceAsStream(Config.prResources.getProperty("LogoLocagis")));
					/*Tamano de la imagen originalmente*/
					int imagenWidth=logoImageData.width;
					int imagenHeight=logoImageData.height;
					
					/*Tamano del shell*/
					int shellWidth=getShell().getBounds().width;
					int shellHeigth=getShell().getBounds().height;
					
					/*Se comprueba si la imagen es mas grande que el tamano del shell*/
					if(imagenWidth>shellWidth){
						tamanoLogoWidth=shellWidth;
					}
					else{
						tamanoLogoWidth=imagenWidth;
					}
					if(imagenHeight>shellHeigth){
						tamanoLogoHeigth=shellHeigth;
					}
					else{
						tamanoLogoHeigth=imagenHeight;
					}
					/*Escalo la imagen para que salga dentro del shell*/
					ImageData id=logoImageData.scaledTo(tamanoLogoWidth, tamanoLogoHeigth);
					
					Image logo = new Image(Display.getCurrent(), id);
					
					// Centrar a lo ancho
					int logoWidth = logo.getBounds().width;
					int screenWidth = e.width;
					int x = screenWidth/2 - logoWidth/2;
					if (x<0) x=0;
					
					e.gc.drawImage(logo, x, 0);	
					logo.dispose();
				}
			}
		});
		

	}

}
