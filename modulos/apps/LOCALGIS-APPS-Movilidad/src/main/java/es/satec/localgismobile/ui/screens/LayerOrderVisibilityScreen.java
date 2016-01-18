/**
 * LayerOrderVisibilityScreen.java
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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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

import com.tinyline.svg.SVG;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGNode;
import com.tinyline.tiny2d.TinyString;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.global.Constants;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.widgets.ScrolledComposite;
import es.satec.svgviewer.localgis.LayerVisibility;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;

public class LayerOrderVisibilityScreen extends LocalGISWindow{

	private ScrolledComposite scrolledComposite = null;
	private Composite composite = null;
	private Composite compositeFlechas = null;
	private ToolBar toolBar = null;
	//private Composite composite1 = null;
	//private Composite composite2 = null;
	
	private Composite compositeTitulo = null;
	private Label labelAct = null;
	private Label labelNameCap = null;
	private Label labelVisibl = null;
	private Label labelEditable = null;
	private Composite compSeleccionada= null;
	private Label labelSeleccionada= null;
	private Vector layers= null;  //  @jve:decl-index=0:
	private ToolBar toolBarMenuVolver = null;
	private String [] layersId=null;
	private SVGLocalGISViewer viewer;
	//private Composite compositeGen = null;
	//private Composite compositeGeneral = null;
	
	public LayerOrderVisibilityScreen(Shell parent,String[] stringsId, SVGLocalGISViewer viewer){
		super(parent);
		//createCompositeGeneral();
		this.layersId= stringsId;
		this.viewer=viewer;
		init();
		show();
		
		
	}

	/*private void createCompositeGeneral() {
		
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 1;
		gridLayout2.marginWidth = 1;
		gridLayout2.marginHeight = 1;
		gridLayout2.verticalSpacing = 1;
		compositeGen = new Composite(shell, SWT.NONE);
		compositeGen.setLayout(gridLayout2);
		compositeGen.setBackground(Config.COLOR_APLICACION);
		createComposite1();
	}

	private void createComposite1() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		gridLayout1.verticalSpacing = 1;
		gridLayout1.marginWidth = 1;
		gridLayout1.marginHeight = 1;
		gridLayout1.horizontalSpacing = 1;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		compositeGeneral = new Composite(compositeGen, SWT.NONE);
		compositeGeneral.setBackground(Config.COLOR_APLICACION);
		compositeGeneral.setLayout(gridLayout1);
		compositeGeneral.setLayoutData(gridData);
		
	}*/

	/**
	 * This method initializes sShell
	 */
	private void init() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 2;
		shell.setText(Messages.getMessage("LayerOrderVisibilityScreen_Titulo"));
		shell.setLayout(gridLayout1);
		shell.setBackground(Config.COLOR_APLICACION);
		createScrolledComposite();
		//shell.setSize(Display.getDefault().getClientArea().width,Display.getDefault().getClientArea().height);
		createCompositeFlechas();
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
		gridLayout.horizontalSpacing = 1;
		gridLayout.verticalSpacing = 1;
		gridLayout.marginWidth = 1;
		gridLayout.marginHeight = 1;
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
		String accesibleLayer=viewer.getActiveLayer();
		for(int i=0;i<this.layersId.length;i++){
			
			//La capa de la graticula no la mostramos nunca.
			if (layersId[i].equals(Constants.GRATICULE_LAYER_NAME))
				continue;
			
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
			gridLayout3.numColumns = 4;
			gridLayout3.verticalSpacing = 1;
			gridLayout3.marginWidth = 1;
			gridLayout3.marginHeight = 1;
			gridLayout3.horizontalSpacing = 13;
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
					
			
			final Button radioButton = new Button(comp, SWT.RADIO);
			radioButton.setLayoutData(gridData7);
			
			//radioButton.setSelection(false);
			Button checkBox1 = null;
//			Campo visibilidad
			checkBox1 = new Button(comp, SWT.CHECK);
			checkBox1.setLayoutData(gridData9);
			
			//Ver si una capa es editable
			final Button radioButtonEditable = new Button(comp, SWT.CHECK);
			radioButton.setLayoutData(gridData7);
			radioButtonEditable.setEnabled(false);

			//campo id capa
			final Label label = new Label(comp, SWT.NONE);	
			label.setText(this.layersId[i]);
			label.setLayoutData(gridData8);
			label.setBackground(Config.COLOR_APLICACION);
			label.addMouseListener(new MouseListener(){

				public void mouseDoubleClick(MouseEvent arg0) {

					//System.out.println("DoubleClick");
				}

				public void mouseDown(MouseEvent event) {

					//System.out.println("mousedown");
					if(compSeleccionada!=null){
						compSeleccionada.setBackground(comp.getBackground());
						labelSeleccionada.setBackground(comp.getBackground());
					}
					compSeleccionada=comp;
					labelSeleccionada=label;
					comp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
					label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
				}

				public void mouseUp(MouseEvent arg0) {

					//System.out.println("mouseUp");
				}
				
			});
			radioButton.addSelectionListener(new SelectionListener(){

				public void widgetDefaultSelected(SelectionEvent arg0) {

				}

				public void widgetSelected(SelectionEvent arg0) {

					
					Composite padre= radioButton.getParent();
					Control [] contPadre=padre.getChildren();
					for(int i=0;i<layers.size();i++){
						Composite comp1= (Composite)layers.elementAt(i);
						Control [] cont=comp1.getChildren();
						if((((Label)contPadre[3]).getText()).equals(((Label)cont[3]).getText())){
							((Button)cont[0]).setSelection(true);
						}
						else{
							((Button)cont[0]).setSelection(false);
						}
					}
				}
				
			});
			comp.addMouseListener(new MouseListener(){

				public void mouseDoubleClick(MouseEvent arg0) {

					//System.out.println("DoubleClick");
				}

				public void mouseDown(MouseEvent event) {

					//System.out.println("mousedown");
					if(compSeleccionada!=null){
						compSeleccionada.setBackground(comp.getBackground());
						labelSeleccionada.setBackground(comp.getBackground());
						
					}
					compSeleccionada=comp;
					labelSeleccionada=label;
					comp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
					label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
				}

				public void mouseUp(MouseEvent arg0) {

					//System.out.println("mouseUp");
				}
				
			});

			//Saber cuales son las capas visibles
			SVGDocument document =  viewer.getSVGDocument();
   		    SVGNode hijo = SVGNode.getNodeById(document.root, new TinyString(this.layersId[i].toCharArray()));
   		    SVGNode nodoPadre=hijo.parent;
   		    //Este nodo hereda la visibilidad de su padre
   		    if(hijo.visibility==SVG.VAL_INHERIT){
   		    	while(nodoPadre!=null && nodoPadre.visibility!=SVG.VAL_INHERIT){
   		    		hijo=nodoPadre;
   	   		    	nodoPadre=nodoPadre.parent;
   	   		    }
   		    }
   		    
   		    if(hijo.visibility==SVG.VAL_VISIBLE){
   		    	checkBox1.setSelection(true);
			}
   		    SVGNode hijo1 = SVGNode.getNodeById(document.root, new TinyString(this.layersId[i].toCharArray()));
   		    if(hijo1.isEditable()){
   		    	radioButtonEditable.setSelection(true);
   		    }
   		    //para saber cual es la capa activa
   		    if(accesibleLayer!=null){
   		    	if(accesibleLayer.equals(this.layersId[i])){
   	   		    	radioButton.setSelection(true);
   	   		    }
   		    }
   		    
   		 layers.addElement(comp);
		}
		
	}

	/**
	 * This method initializes compositeFlechas	
	 *
	 */
	private void createCompositeFlechas() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		gridLayout2.verticalSpacing = 0;
		gridLayout2.marginWidth = 0;
		gridLayout2.marginHeight = 0;
		gridLayout2.horizontalSpacing = 0;
		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalAlignment = GridData.CENTER;
		gridData.verticalAlignment = GridData.CENTER;
		gridData.grabExcessHorizontalSpace = false;
		compositeFlechas = new Composite(shell, SWT.NONE);
		compositeFlechas.setBackground(Config.COLOR_APLICACION);
		compositeFlechas.setLayoutData(gridData);
		compositeFlechas.setLayout(gridLayout2);
		createToolBar();
	}

	/**
	 * This method initializes toolBar	
	 *
	 */
	private void createToolBar() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 1;
		gridLayout2.verticalSpacing = 1;
		gridLayout2.marginWidth = 1;
		gridLayout2.marginHeight = 1;
		gridLayout2.horizontalSpacing = 1;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.FILL;
		gridData5.verticalAlignment = GridData.FILL;
		toolBar = new ToolBar(compositeFlechas, SWT.VERTICAL);
		toolBar.setBackground(Config.COLOR_APLICACION);
		toolBar.setLayoutData(gridData5);
		toolBar.setLayout(gridLayout2);
		ToolItem toolItemArriba = new ToolItem(toolBar, SWT.PUSH);
		String imagenArriba=Config.prResources.getProperty("LayerOrderVisibilityScreen.scrol_up");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(imagenArriba);
		Image imagepadre = new Image(Display.getCurrent(), is);
		toolItemArriba.setImage(imagepadre);
		//Cuando se selecciona la capa y se pinxa en este boton sube la capa hacia arriba
		toolItemArriba.addListener(SWT.Selection,new Listener(){

			public void handleEvent(Event event) {

				if(compSeleccionada!=null){
					int pos= layers.indexOf(compSeleccionada);
					if(pos!=0){
						Rectangle sele=compSeleccionada.getBounds();
						Composite ante=(Composite)layers.elementAt(pos-1);
						Rectangle antesele=ante.getBounds();
						compSeleccionada.setBounds(sele.x,antesele.y,sele.width,sele.height);
						ante.setBounds(antesele.x,sele.y,antesele.width,antesele.height);
						layers.removeElementAt(pos-1);
						layers.add(pos-1, compSeleccionada);
						layers.removeElementAt(pos);
						layers.add(pos,ante);
					}
				}

			}});
		ToolItem toolItemAbajo = new ToolItem(toolBar, SWT.PUSH);
		String imagenAbajo=Config.prResources.getProperty("LayerOrderVisibilityScreen.scrol_down");
		InputStream isAbajo = this.getClass().getClassLoader().getResourceAsStream(imagenAbajo);
		Image imageAbajo = new Image(Display.getCurrent(), isAbajo);
		toolItemAbajo.setImage(imageAbajo);
		toolItemAbajo.addListener(SWT.Selection,new Listener(){

			public void handleEvent(Event event) {

				if(compSeleccionada!=null){
					int pos= layers.indexOf(compSeleccionada);
					//System.out.println(pos);
					//System.out.println(layers.size()-1);
					if(pos!=(layers.size()-1)){
						Rectangle sele=compSeleccionada.getBounds();
						Composite ante=(Composite)layers.elementAt(pos+1);
						Rectangle antesele=ante.getBounds();
						compSeleccionada.setBounds(sele.x,antesele.y,sele.width,sele.height);
						ante.setBounds(antesele.x,sele.y,antesele.width,antesele.height);
						layers.removeElementAt(pos+1);
						layers.add(pos+1,compSeleccionada);
						layers.removeElementAt(pos);
						layers.add( pos,ante);
					}
				}

			}});
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
		//gridData10.verticalAlignment = GridData.FILL;
		GridData gridData4 = new GridData();
		//gridData4.horizontalAlignment = GridData.CENTER;
		gridData4.grabExcessHorizontalSpace = true;
		//gridData4.verticalAlignment = GridData.CENTER;
		GridData gridData3 = new GridData();
		//gridData3.horizontalAlignment = GridData.CENTER;
		gridData3.grabExcessHorizontalSpace = true;
		//gridData3.verticalAlignment = GridData.CENTER;
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		//gridData2.verticalAlignment = GridData.CENTER;
		//gridData2.horizontalAlignment = GridData.CENTER;
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.numColumns = 4;
		gridLayout4.verticalSpacing = 1;
		gridLayout4.marginWidth = 1;
		gridLayout4.marginHeight = 1;
		gridLayout4.horizontalSpacing = 1;
		compositeTitulo = new Composite(composite, SWT.BORDER);
		compositeTitulo.setBackground(Config.COLOR_APLICACION);
		compositeTitulo.setLayout(gridLayout4);
		compositeTitulo.setLayoutData(gridData10);
		
		//para ver si una capa esta activa o no, solo podrá ser una por proyecto
		labelAct = new Label(compositeTitulo, SWT.NONE);
		labelAct.setLayoutData(gridData2);
		labelAct.setBackground(Config.COLOR_APLICACION);
		String imagenActiva=Config.prResources.getProperty("LayerOrderVisibilityScreen.activo");
		InputStream isActiva = this.getClass().getClassLoader().getResourceAsStream(imagenActiva);
		Image imageActiva = new Image(Display.getCurrent(), isActiva);
		labelAct.setImage(imageActiva);
		
		//Para ver si una capa es visible o no
		labelVisibl = new Label(compositeTitulo, SWT.NONE);
		labelVisibl.setBackground(Config.COLOR_APLICACION);
		String imagenVisible=Config.prResources.getProperty("LayerOrderVisibilityScreen.capavisible");
		InputStream isVisible = this.getClass().getClassLoader().getResourceAsStream(imagenVisible);
		Image imageAbajo = new Image(Display.getCurrent(), isVisible);
		labelVisibl.setImage(imageAbajo);
		//labelVisibl.setText(PantallaPrincip.getMessage("LayerOrderVisibilityScreen_Visible"));
		labelVisibl.setLayoutData(gridData3);
		
		//Si una capa es editable o no
		labelEditable = new Label(compositeTitulo, SWT.NONE);
		labelEditable.setBackground(Config.COLOR_APLICACION);
		labelEditable.setLayoutData(gridData2);
		String imagenEditable=Config.prResources.getProperty("LayerOrderVisibilityScreen.editar_on");
		InputStream isEditable = this.getClass().getClassLoader().getResourceAsStream(imagenEditable);
		Image imageEditable = new Image(Display.getCurrent(), isEditable);
		labelEditable.setImage(imageEditable);

		
		labelNameCap = new Label(compositeTitulo, SWT.NONE);
		labelNameCap.setBackground(Config.COLOR_APLICACION);
		labelNameCap.setText("   "+Messages.getMessage("LayerOrderVisibilityScreen_NombreCapa")+"        ");
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
		String imagenOk=Config.prResources.getProperty("LayerOrderVisibilityScreen_ok");
		InputStream isOk = this.getClass().getClassLoader().getResourceAsStream(imagenOk);
		Image imageOk = new Image(Display.getCurrent(), isOk);
		toolItemOk.setImage(imageOk);
		toolItemOk.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				Vector lay= new Vector();
				for(int i=0; i<layers.size();i++){
					Control[] controlChil= ((Composite)layers.elementAt(i)).getChildren();
					Label labelLay=(Label)controlChil[3];
					Button botonVisible=(Button)controlChil[1];
					Button botonActiva=(Button)controlChil[0];
					boolean sele=botonVisible.getSelection();
					LayerVisibility layer1= new LayerVisibility(labelLay.getText(),sele);
					if(botonActiva.getSelection() && !labelLay.getText().equals(Constants.GRATICULE_LAYER_NAME)){
						viewer.setActiveLayer(labelLay.getText());
					}
					lay.addElement(layer1);
				}
				
				viewer.setLayerOrderAndVisibility(lay);
				shell.dispose();
			}});
		ToolItem toolItemCancel = new ToolItem(toolBarMenuVolver, SWT.PUSH);
		String imagenCancel=Config.prResources.getProperty("LayerOrderVisibilityScreen_cancel");
		InputStream isCancel = this.getClass().getClassLoader().getResourceAsStream(imagenCancel);
		Image imageCancel = new Image(Display.getCurrent(), isCancel);
		toolItemCancel.setImage(imageCancel);
		toolItemCancel.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				shell.dispose();
			}});
	}

	/**
	 * Mantiene el flujo en esta ventana hasta que se cierra y devuelve la capa activa
	 * @return La capa activa seleccionada. Null si no hay.
	 */
	public String getSelectedLayer() {
		// Ventana bloqueante
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		return viewer.getActiveLayer();
	}

}
