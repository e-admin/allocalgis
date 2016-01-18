/**
 * StyleScreen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.io.InputStream;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.widgets.ScrolledComposite;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;
import es.satec.svgviewer.localgis.sld.LayerSLDInfo;

public class StyleScreen extends LocalGISWindow {

	private ScrolledComposite scrolledComposite = null;
	private Composite composite = null;
	private Composite compositeTitulo = null;
	private Label labelAct = null;
	private Label labelNameCap = null;
	private Vector layers= null;  //  @jve:decl-index=0:
	private ToolBar toolBarMenuVolver = null;
	SVGLocalGISViewer viewer;
	
	public StyleScreen(Shell parent, SVGLocalGISViewer viewer){
		super(parent);
		shell.setBackground(Config.COLOR_APLICACION);
		this.viewer=viewer;
		init();
		show();
		/*for(int i=0;i<layers.size();i++){
			Composite comp1= (Composite)layers.elementAt(i);
			Control [] cont=comp1.getChildren();
			FontData [] val=(((Label)cont[1]).getFont()).getFontData();
			System.out.println("x "+((Label)cont[1]).getSize().x+" y "+((Label)cont[1]).getSize().y);
		}*/
		
	}

	/**
	 * This method initializes sShell
	 */
	private void init() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 1;
		shell.setText(Messages.getMessage("StyleScreen_Titulo"));
		shell.setLayout(gridLayout1);
		createScrolledComposite();
		//shell.setSize(Display.getDefault().getClientArea().width,Display.getDefault().getClientArea().height);
		createToolBarMenuVolver();
		leerNodo();
		Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(pSize);
		scrolledComposite.setMinSize(pSize);
	}

	private void leerNodo() {

		
	}

	/**
	 * This method initializes scrolledComposite	
	 *
	 */
	private void createScrolledComposite() {
		scrolledComposite=new ScrolledComposite(shell,SWT.H_SCROLL|SWT.V_SCROLL);
		scrolledComposite.setBackground(Config.COLOR_APLICACION);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setLayout(new GridLayout());		
		scrolledComposite.setExpandHorizontal(true);
        GridData gridData1=new GridData();
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.verticalAlignment = GridData.FILL;
        gridData1.grabExcessVerticalSpace = true;
        scrolledComposite.setLayoutData(gridData1);
        scrolledComposite.setLayout(new FillLayout());
        createComposite();
        scrolledComposite.setContent(composite);
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 4;
		gridLayout.verticalSpacing = 4;
		gridLayout.marginWidth = 4;
		gridLayout.marginHeight = 4;
		gridLayout.makeColumnsEqualWidth = true;
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setBackground(Config.COLOR_APLICACION);
		composite.setLayout(gridLayout);
		createCompositeTitulo();
		layers= new Vector();
		cargaDatos();
		
	}

	private void cargaDatos() {

		//para que aparezca todo en la misma columna
//		Listados de los estilos
		Vector layerSLD=viewer.getLayerSLDs();
		for(int j=0;j<viewer.getSVGLayers().length;j++){
					
			GridData gridData9 = new GridData();
			gridData9.horizontalAlignment = GridData.CENTER;
			gridData9.grabExcessHorizontalSpace = true;
			GridData gridData8 = new GridData();
			gridData8.horizontalAlignment = GridData.CENTER;
			gridData8.grabExcessHorizontalSpace = true;
			
			GridData gridData7 = new GridData();
			gridData7.horizontalAlignment = GridData.CENTER;
			gridData7.grabExcessHorizontalSpace = true;
		

			GridLayout gridLayout3 = new GridLayout();
			gridLayout3.numColumns = 3;
			gridLayout3.verticalSpacing = 5;
			gridLayout3.marginWidth = 5;
			gridLayout3.marginHeight = 5;
			gridLayout3.horizontalSpacing = 20;
			//gridLayout3.makeColumnsEqualWidth = true;
			GridData gridData6 = new GridData();
			gridData6.horizontalSpan = 3;
			gridData6.grabExcessVerticalSpace = false;
			//gridData6.horizontalAlignment = GridData.FILL_BOTH;
			gridData6.verticalAlignment = GridData.BEGINNING;
			gridData6.grabExcessHorizontalSpace = true;
			
			final Composite comp = new Composite(composite, SWT.NONE);
			comp.setBackground(Config.COLOR_APLICACION);
			comp.setLayoutData(gridData6);
			comp.setLayout(gridLayout3);
					
			
			final Button radioButton = new Button(comp, SWT.CHECK);
			radioButton.setLayoutData(gridData7);
//			campo id capa
			final Label label = new Label(comp, SWT.NONE);	
			label.setText("   "+viewer.getSVGLayers()[j]);
			label.setBackground(Config.COLOR_APLICACION);
			label.setLayoutData(gridData8);
			
			for(int i=0;i<layerSLD.size();i++){
				LayerSLDInfo layerSLDInfo = (LayerSLDInfo) layerSLD.elementAt(i);
				if(layerSLDInfo.getLayerName().equals(viewer.getSVGLayers()[j])){
					if(!layerSLDInfo.isSearch()){
						radioButton.setSelection(layerSLDInfo.isActive());	
					}
					  
				}	 
			}
			layers.addElement(comp);
		}
		
	}

	/**
	 * This method initializes compositeTitulo	
	 *
	 */
	private void createCompositeTitulo() {
		GridData gridData10 = new GridData();
		//gridData10.horizontalAlignment = GridData.FILL;
		gridData10.grabExcessHorizontalSpace = true;
		//gridData10.grabExcessVerticalSpace = false;
		gridData10.verticalAlignment = GridData.CENTER;
		gridData10.horizontalAlignment = GridData.FILL;
		//gridData10.verticalAlignment = GridData.FILL;
		GridData gridData4 = new GridData();
		//gridData4.horizontalAlignment = GridData.CENTER;
		gridData4.grabExcessHorizontalSpace = true;
		//gridData4.verticalAlignment = GridData.CENTER;
		gridData4.verticalAlignment = GridData.CENTER;
		gridData4.horizontalAlignment = GridData.BEGINNING;
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = false;
		//gridData2.verticalAlignment = GridData.CENTER;
		gridData2.verticalAlignment = GridData.CENTER;
		gridData2.horizontalAlignment = GridData.CENTER;
		//gridData2.horizontalAlignment = GridData.CENTER;
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 2;
		gridLayout4.verticalSpacing = 5;
		gridLayout4.marginWidth = 5;
		gridLayout4.marginHeight = 5;
		gridLayout4.horizontalSpacing = 10;
		compositeTitulo = new Composite(composite, SWT.BORDER);
		compositeTitulo.setBackground(Config.COLOR_APLICACION);
		compositeTitulo.setLayout(gridLayout4);
		compositeTitulo.setLayoutData(gridData10);
		
		String imagenEstilo=Config.prResources.getProperty("StyleScreen_disenio");
		InputStream isEstilo = this.getClass().getClassLoader().getResourceAsStream(imagenEstilo);
		Image imageEstilo  = new Image(Display.getCurrent(), isEstilo );
		
		labelAct = new Label(compositeTitulo, SWT.NONE);
		
		labelAct.setImage(imageEstilo);
		//labelAct.setText(Messages.getMessage("StyleScreen_Estilo"));
		
		labelAct.setLayoutData(gridData2);
		labelNameCap = new Label(compositeTitulo, SWT.NONE);
		labelNameCap.setBackground(Config.COLOR_APLICACION);
		labelNameCap.setText(Messages.getMessage("StyleScreen_NombreCapa"));
		labelNameCap.setLayoutData(gridData4);
		
	}

	/**
	 * This method initializes toolBarMenuVolver	
	 *
	 */
	private void createToolBarMenuVolver() {
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.CENTER;
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.verticalAlignment = GridData.CENTER;
		toolBarMenuVolver = new ToolBar(shell, SWT.NONE);
		toolBarMenuVolver.setBackground(Config.COLOR_APLICACION);
		toolBarMenuVolver.setLayoutData(gridData11);
		ToolItem toolItemOk = new ToolItem(toolBarMenuVolver, SWT.PUSH);
		String imagenOk=Config.prResources.getProperty("StyleScreen_Ok");
		InputStream isOk = this.getClass().getClassLoader().getResourceAsStream(imagenOk);
		Image imageOk = new Image(Display.getCurrent(), isOk);
		toolItemOk.setImage(imageOk);
		toolItemOk.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				for(int i=0; i<layers.size();i++){
					Control[] controlChil= ((Composite)layers.elementAt(i)).getChildren();
					Label labelLay=(Label)controlChil[1];
					Button botonVisible=(Button)controlChil[0];
					boolean sele=botonVisible.getSelection();
					viewer.setLayerSLDActive(labelLay.getText().trim(), sele);
					viewer.drawSVG();
				}
				shell.dispose();
			}});
		ToolItem toolItemCancel = new ToolItem(toolBarMenuVolver, SWT.PUSH);
		String imagenCancel=Config.prResources.getProperty("StyleScreen_cancel");
		InputStream isCancel = this.getClass().getClassLoader().getResourceAsStream(imagenCancel);
		Image imageCancel = new Image(Display.getCurrent(), isCancel);
		toolItemCancel.setImage(imageCancel);
		toolItemCancel.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				shell.dispose();
			}});
	}

}
