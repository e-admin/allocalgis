/**
 * CreateNewSearchScreen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.geopista.feature.Attribute;
import com.geopista.feature.GeopistaSchema;
import com.tinyline.svg.SVGDocument;
import com.tinyline.svg.SVGNode;
import com.tinyline.tiny2d.TinyString;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.utils.SearchBean;
import es.satec.localgismobile.ui.utils.SearchDataBean;
import es.satec.localgismobile.ui.widgets.ScrolledComposite;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;

public class CreateNewSearchScreen extends LocalGISWindow{

	private SVGLocalGISViewer viewer=null;
	private Group groupFiltro = null;
	private Composite composite = null;
	private Label labelCapa = null;
	private Combo comboCapa = null;
	private Label labelAtributo = null;
	private Combo comboAtributo = null;
	private Label labelOperador = null;
	private Combo comboOperador = null;
	private Label labelValor = null;
	private Text textValor = null;
	private Group groupVisualizacion = null;
	private Composite composite1 = null;
	private Label labelFill = null;
	private Label colorLabel = null;
	private Color colorFill=null;
	private Color colorStroke=null;
	private Button button = null;
	private Label labelStroke = null;
	private Label strokelabel = null;
	private Button button1 = null;
	private Composite composite2 = null;
	private Button buttonAlmacenar = null;
	private Button buttonAlmacenarCancel = null;
	SearchBean busqueda=new SearchBean();
	private Vector bbddInfor=null;
	private int selecIndice=-1;
	private Label labelBorder = null;
	private Scale scaleBorder = null;
	private Text labelValStroke = null;
	private Label labelValor2 = null;
	private Text textValor2 = null;
	private Point tamanoCombo=null;
	private ScrolledComposite scrolledComposite;
	private Composite scrolledCompositeContent;
	
	private static Logger logger = Global.getLoggerFor(CreateNewSearchScreen.class);  //  @jve:decl-index=0:
	
	/**
	 * This method initializes sShell
	 * @param inforBusq 
	 */
	public CreateNewSearchScreen(Shell parent,SVGLocalGISViewer viewer, Vector inforBusq){
		super(parent);
		this.viewer=viewer;
		this.bbddInfor=inforBusq;
		shell.setBackground(Config.COLOR_APLICACION);
		init();
		show();
		/*tamanoCombo=comboOperador.getSize();
		comboAtributo.setSize(tamanoCombo);
		comboCapa.setSize(tamanoCombo);
		textValor.setSize(tamanoCombo);
		textBetween.setSize(tamanoCombo);*/
	}
	
	public CreateNewSearchScreen(Shell parent,Vector inforBusq, int selectionIndex, SVGLocalGISViewer viewer2) {

		super(parent);
		this.bbddInfor=inforBusq;
		this.selecIndice=selectionIndex;
		shell.setBackground(Config.COLOR_APLICACION);
		this.viewer=viewer2;
		init();
		buttonAlmacenarCancel = new Button(composite2, SWT.NONE);
		buttonAlmacenarCancel.setText(Messages.getMessage("CreateNewSearchScreen_AlmacenarCancelar"));
		buttonAlmacenarCancel.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event event) {

				shell.dispose();
			}});
		//Se va a realizar la recarga de los combo
		SearchDataBean dB=(SearchDataBean)bbddInfor.elementAt(selecIndice);
		
		comboCapa.setItems(viewer.getAllIDLayers());
		for(int i=0;i<viewer.getAllIDLayers().length;i++){
			if(viewer.getAllIDLayers()[i].equals(dB.getCapa())){
				comboCapa.select(i);
			}
		}
		SVGDocument document =  viewer.getSVGDocument();
		SVGNode hijo = SVGNode.getNodeById(document.root, new TinyString(dB.getCapa().toCharArray()));
		Vector atri=hijo.nameAtts;
		String [] atributo=new String[atri.size()];
		for(int i=0; i<atri.size();i++){
			atributo[i]=(String) atri.elementAt(i);
		}
		comboAtributo.setItems(atributo);
		for(int i=0;i<atributo.length;i++){
			if(atributo[i].equals(dB.getAtributo())){
				comboAtributo.select(i);
			}
		}
		
		comboOperador.setItems(busqueda.getDatosDouble());
		for(int i=0;i<busqueda.getDatosDouble().length;i++){
			if(busqueda.getDatosDouble()[i].equals(dB.getOperador())){
				comboOperador.select(i);
			}
		}
		if(comboOperador.getText().equals("between")){
			int val=dB.getValor().indexOf(" - ");
			String ini=dB.getValor().substring(0, val);
			String fin=dB.getValor().substring(val+3, dB.getValor().length());
			textValor.setText(ini);
			textValor2.setText(fin);
			labelValor2.setVisible(true);
			textValor2.setVisible(true); //setEditable(true);
		}
		else{
			textValor.setText(dB.getValor());
		}
		colorLabel.setBackground(dB.getFill());
		strokelabel.setBackground(dB.getStroke());
		labelValStroke.setText(dB.getstrokeWidth());
		scaleBorder.setSelection(Integer.parseInt(dB.getstrokeWidth()));
		show();
		tamanoCombo=comboOperador.getSize();
		//comboAtributo.setSize(tamanoCombo);
		//comboCapa.setSize(tamanoCombo);
		textValor.setSize(tamanoCombo);
		textValor2.setSize(tamanoCombo);
	}

	private void init() {
		shell.setLayout(new GridLayout());
		shell.setText(Messages.getMessage("CreateNewSearchScreen_Titulo"));
		createScrolledComposite();
		createGroupFiltro();
		createGroupVisualizacion();
		Point size = scrolledCompositeContent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrolledComposite.setMinSize(size);
	}
	
	private void createScrolledComposite() {
		scrolledComposite=new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBackground(Config.COLOR_APLICACION);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setLayout(new GridLayout());
		scrolledComposite.setExpandHorizontal(true);
        GridData gridData1=new GridData();
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.verticalAlignment = GridData.FILL;
        gridData1.grabExcessVerticalSpace = true;
        scrolledComposite.setLayoutData(gridData1);
        scrolledComposite.setLayout(new FillLayout());
		scrolledCompositeContent = new Composite(scrolledComposite, SWT.BORDER);
		scrolledCompositeContent.setBackground(Config.COLOR_APLICACION);
		scrolledCompositeContent.setLayout(new GridLayout());
        scrolledComposite.setContent(scrolledCompositeContent);
	}

	/**
	 * This method initializes groupFiltro	
	 *
	 */
	private void createGroupFiltro() {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		//gridLayout.makeColumnsEqualWidth = true;
		groupFiltro = new Group(scrolledCompositeContent, SWT.NONE);
		groupFiltro.setBackground(Config.COLOR_APLICACION);
		groupFiltro.setText(Messages.getMessage("CreateNewSearchScreen_Filtro"));
		groupFiltro.setSize(Display.getDefault().getClientArea().width,Display.getDefault().getClientArea().height);
		groupFiltro.setLayoutData(gridData);
		groupFiltro.setLayout(gridLayout);
		createComposite();
	}

	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		GridData gdSpan2 = new GridData();
		gdSpan2.horizontalSpan = 2;
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = GridData.BEGINNING;
		gridData10.verticalAlignment = GridData.CENTER;
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 3;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.marginHeight = 0;
		gridLayout1.horizontalSpacing = 0;
		composite = new Composite(groupFiltro, SWT.NONE);
		composite.setBackground(Config.COLOR_APLICACION);
		composite.setLayout(gridLayout1);
		composite.setLayoutData(gridData3);
		labelCapa = new Label(composite, SWT.NONE);
		labelCapa.setText(Messages.getMessage("CreateNewSearchScreen_Capa"));
		labelCapa.setBackground(Config.COLOR_APLICACION);
		labelCapa.setLayoutData(gdSpan2);
		labelAtributo = new Label(composite, SWT.NONE);
		labelAtributo.setBackground(Config.COLOR_APLICACION);
		labelAtributo.setText(Messages.getMessage("CreateNewSearchScreen_Atributo"));
		createComboCapa();
		createComboAtributo();
		labelOperador = new Label(composite, SWT.NONE);
		labelOperador.setBackground(Config.COLOR_APLICACION);
		labelOperador.setText(Messages.getMessage("CreateNewSearchScreen_Operador"));
		labelValor = new Label(composite, SWT.NONE);
		labelValor.setBackground(Config.COLOR_APLICACION);
		labelValor.setText(Messages.getMessage("CreateNewSearchScreen_Valor"));
		labelValor2 = new Label(composite, SWT.NONE);
		labelValor2.setBackground(Config.COLOR_APLICACION);
		labelValor2.setText(Messages.getMessage("CreateNewSearchScreen_Valor2"));
		labelValor2.setVisible(false);
		createComboOperador();
		textValor = new Text(composite, SWT.BORDER);
		textValor.setLayoutData(gridData10);
		textValor.addFocusListener(new FocusListener(){
			
			public void focusGained(FocusEvent e) {

				//System.out.println("focusGained");
				//System.out.println(text1.getText());
				textValor.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			}

			public void focusLost(FocusEvent e) {
				
			}
		});
		textValor2 = new Text(composite, SWT.BORDER);
		textValor2.setLayoutData(gridData10);
		textValor2.setVisible(false); //setEditable(false);
	}

	/**
	 * This method initializes comboCapa	
	 *
	 */
	private void createComboCapa() {
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = GridData.FILL;
		gridData7.verticalAlignment = GridData.CENTER;
		gridData7.grabExcessHorizontalSpace = true;
		gridData7.horizontalSpan = 2;
		comboCapa = new Combo(composite, SWT.READ_ONLY);
		
		comboCapa.setItems(viewer.getAllIDLayers());
		comboCapa.setLayoutData(gridData7);
		comboCapa.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			public void widgetSelected(SelectionEvent arg0) {

				String sele=((Combo)arg0.widget).getText();
				SVGDocument document =  viewer.getSVGDocument();
				SVGNode hijo = SVGNode.getNodeById(document.root, new TinyString(sele.toCharArray()));
				Vector atri=hijo.nameAtts;
				String [] atributo=new String[atri.size()];
				for(int i=0; i<atri.size();i++){
					atributo[i]=(String) atri.elementAt(i);
				}
				comboAtributo.setItems(atributo);
			}
			
		});
	}

	/**
	 * This method initializes comboAtributo	
	 *
	 */
	private void createComboAtributo() {
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.verticalAlignment = GridData.CENTER;
		comboAtributo = new Combo(composite, SWT.READ_ONLY);
		comboAtributo.setLayoutData(gridData8);
		comboAtributo.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			public void widgetSelected(SelectionEvent arg0) {
				String capa = comboCapa.getText();
				String atributo = comboAtributo.getText();
				GeopistaSchema geoSche=viewer.getGeopistaSchema(capa);
				//Domain domain=geoSche.getAttributeDomain(atributo);
				Attribute atrib=geoSche.getAttribute(atributo);
				if(atrib == null){
					comboOperador.setItems(busqueda.getDatosString());
				}
				else{
					logger.info("Tipo de dominio->" + atrib.getType() + " nombre" + atrib.getName());
					if(atrib.getType().equals("INTEGER") || atrib.getType().equals("NUMERIC") || atrib.getType().equals("DOUBLE")){
						comboOperador.setItems(busqueda.getDatosDouble());
					}
					else{
						comboOperador.setItems(busqueda.getDatosString());
					}
				}
			}
			
		});
	}

	/**
	 * This method initializes comboOperador	
	 *
	 */
	private void createComboOperador() {
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = GridData.FILL;
		gridData9.verticalAlignment = GridData.CENTER;
		gridData9.grabExcessHorizontalSpace = true;
		comboOperador = new Combo(composite, SWT.READ_ONLY);
		comboOperador.setLayoutData(gridData9);
		comboOperador.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent arg0) {

			}

			public void widgetSelected(SelectionEvent arg0) {

				String sele=((Combo)arg0.widget).getText();
				if(sele.equals("between")){
					labelValor2.setVisible(true);
					textValor2.setVisible(true); //setEditable(true);
				}
				else{
					labelValor2.setVisible(false);
					textValor2.setVisible(false); //setEditable(false);
				}
			}
			
		});
	}

	/**
	 * This method initializes groupVisualizacion	
	 *
	 */
	private void createGroupVisualizacion() {
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.horizontalSpacing = 0;
		gridLayout4.marginWidth = 0;
		gridLayout4.marginHeight = 0;
		gridLayout4.verticalSpacing = 0;
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = GridData.FILL;
		groupVisualizacion = new Group(scrolledCompositeContent, SWT.NONE);
		groupVisualizacion.setBackground(Config.COLOR_APLICACION);
		groupVisualizacion.setLayout(new GridLayout());
		groupVisualizacion.setLayoutData(gridData2);
		groupVisualizacion.setText(Messages.getMessage("CreateNewSearchScreen_Visualizacion"));
		createComposite1();
		groupVisualizacion.setLayout(gridLayout4);
		createComposite2();
	}

	/**
	 * This method initializes composite1	
	 *
	 */
	private void createComposite1() {
		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = GridData.FILL;
		gridData12.grabExcessHorizontalSpace = true;
		gridData12.verticalAlignment = GridData.CENTER;
		GridData gridData13 = new GridData();
		gridData13.horizontalAlignment = GridData.FILL;
		gridData13.grabExcessHorizontalSpace = true;
		gridData13.verticalAlignment = GridData.CENTER;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.CENTER;
		gridData5.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		gridLayout2.horizontalSpacing = 0;
		gridLayout2.verticalSpacing = 0;
		gridLayout2.marginWidth = 0;
		gridLayout2.marginHeight = 0;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = GridData.FILL;
		composite1 = new Composite(groupVisualizacion, SWT.NONE);
		composite1.setBackground(Config.COLOR_APLICACION);
		composite1.setLayoutData(gridData1);
		composite1.setLayout(gridLayout2);
		labelFill = new Label(composite1, SWT.NONE);
		labelFill.setBackground(Config.COLOR_APLICACION);
		labelFill.setText(Messages.getMessage("CreateNewSearchScreen_Fill"));
		colorFill = new Color(shell.getDisplay(), new RGB(0, 255, 0));
		labelStroke = new Label(composite1, SWT.NONE);
		labelStroke.setBackground(Config.COLOR_APLICACION);
		labelStroke.setText(Messages.getMessage("CreateNewSearchScreen_Stroke"));
		colorLabel = new Label(composite1, SWT.NONE);
		colorLabel.setText("           ");
	    colorLabel.setBackground(colorFill);
	    colorLabel.setLayoutData(gridData12);
	    colorStroke = new Color(shell.getDisplay(), new RGB(0, 0, 255));
	    strokelabel = new Label(composite1, SWT.NONE);
	    
	    strokelabel.setText("           ");
	    strokelabel.setBackground(colorStroke);
	    strokelabel.setLayoutData(gridData13);
	    button = new Button(composite1, SWT.PUSH);
	    button.setText(Messages.getMessage("CreateNewSearchScreen_Color"));
	    button1 = new Button(composite1, SWT.NONE);
	    button1.setText(Messages.getMessage("CreateNewSearchScreen_Color"));
	    labelBorder = new Label(composite1, SWT.NONE);
	    labelBorder.setBackground(Config.COLOR_APLICACION);
	    labelBorder.setText(Messages.getMessage("CreateNewSearchScreen_Borde"));
	    labelValStroke = new Text(composite1, SWT.NONE);
	    labelValStroke.setLayoutData(gridData5);
	    
	    GridData gridData6 = new GridData();
	    gridData6.horizontalAlignment = GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalSpan = 2;
		gridData6.verticalAlignment = GridData.CENTER;
	    scaleBorder = new Scale(composite1, SWT.NONE);
	    scaleBorder.setBackground(Config.COLOR_APLICACION);
	    scaleBorder.setMaximum (10);
	    scaleBorder.setMaximum(0);
	    scaleBorder.setPageIncrement (1);
	    scaleBorder.setLayoutData(gridData6);
	    scaleBorder.setSelection(5);
	    labelValStroke.setText(scaleBorder.getSelection()+"");
	    labelValStroke.setBackground(Config.COLOR_APLICACION);
	    scaleBorder.addMouseMoveListener(new MouseMoveListener (){

			public void mouseMove(MouseEvent arg0) {

				labelValStroke.setText(scaleBorder.getSelection()+"");
			}
	    	
	    });
	    
	    button1.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		        // Create the color-change dialog
		        ColorDialog dlg = new ColorDialog(shell);

		        // Set the selected color in the dialog from
		        // user's selected color
		        dlg.setRGB(colorLabel.getBackground().getRGB());

		        // Change the title bar text
		        dlg.setText(Messages.getMessage("CreateNewSearchScreen_Cambio"));

		        // Open the dialog and retrieve the selected color
		        RGB rgb = dlg.open();
		        if (rgb != null) {
		          // Dispose the old color, create the
		          // new one, and set into the label
		        	colorStroke.dispose();
		        	colorStroke = new Color(shell.getDisplay(), rgb);
		        	strokelabel.setBackground(colorStroke);
		        }
		      }
		    });
	    button.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent event) {
	        // Create the color-change dialog
	        ColorDialog dlg = new ColorDialog(shell);

	        // Set the selected color in the dialog from
	        // user's selected color
	        dlg.setRGB(colorLabel.getBackground().getRGB());

	        // Change the title bar text
	        dlg.setText(Messages.getMessage("CreateNewSearchScreen_Cambio"));

	        // Open the dialog and retrieve the selected color
	        RGB rgb = dlg.open();
	        if (rgb != null) {
	          // Dispose the old color, create the
	          // new one, and set into the label
	          colorStroke.dispose();
	          colorStroke = new Color(shell.getDisplay(), rgb);
	          colorLabel.setBackground(colorStroke);
	        }
	      }
	    });
	}

	/**
	 * This method initializes composite2	
	 *
	 */
	private void createComposite2() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.CENTER;
		gridData1.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		gridLayout3.verticalSpacing = 0;
		gridLayout3.marginWidth = 0;
		gridLayout3.marginHeight = 0;
		gridLayout3.horizontalSpacing = 0;
		composite2 = new Composite(shell, SWT.NONE);
		composite2.setBackground(Config.COLOR_APLICACION);
		composite2.setLayout(gridLayout3);
		composite2.setLayoutData(gridData1);
		buttonAlmacenar = new Button(composite2, SWT.NONE);
		buttonAlmacenar.setText(Messages.getMessage("CreateNewSearchScreen_Almacenar"));
		buttonAlmacenar.addListener(SWT.Selection, new Listener(){

			public void handleEvent(Event arg0) {

				//se comprueba si el campo valor es vacio
				if(datoCorrectos()){
					
					boolean ejecutado = false;
					if (selecIndice != -1) {
						try {
							SearchDataBean old = (SearchDataBean) bbddInfor.elementAt(selecIndice);
							ejecutado = old.getEjecutado();
						} catch (Exception e) {
							logger.error(e);
						}
					}
					
					SearchDataBean dtBusq=null;
					if(comboOperador.getText().equals("between")){
						dtBusq= new SearchDataBean(comboCapa.getText(), comboAtributo.getText(), comboOperador.getText(),textValor.getText().trim()+" - "+textValor2.getText().trim(),labelValStroke.getText(), colorLabel.getBackground(), strokelabel.getBackground(), ejecutado);
					}
					else{
						dtBusq= new SearchDataBean(comboCapa.getText(), comboAtributo.getText(), comboOperador.getText(),textValor.getText().trim(),labelValStroke.getText(), colorLabel.getBackground(), strokelabel.getBackground(), ejecutado);
					}
					if(selecIndice!=-1){
						bbddInfor.add(selecIndice,dtBusq);
						bbddInfor.remove(selecIndice+1);
	
					}
					else{
						bbddInfor.add(dtBusq);
					}
					selecIndice=-1;
					shell.dispose();
				}
			}

			private boolean datoCorrectos() {

				String mensaje=null;
				boolean correcto=true;
				if(correcto){
					if(comboCapa.getSelectionIndex()!=-1){
						comboCapa.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
						if(comboAtributo.getSelectionIndex()!=-1){
							comboAtributo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							if(comboOperador.getSelectionIndex()!=-1){
								comboOperador.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
							else{
								comboOperador.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
								mensaje=Messages.getMessage("CreateNewSearchScreen_SmsErrorValor");
								correcto=false;
							}
						}
						else{
							comboAtributo.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
							mensaje=Messages.getMessage("CreateNewSearchScreen_SmsErrorValor");
							correcto=false;
						}
					}
					else{
						comboCapa.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						mensaje=Messages.getMessage("CreateNewSearchScreen_SmsErrorValor");
						correcto=false;
					}
				}
				if(correcto & comprobarVacio()){
					mensaje=Messages.getMessage("CreateNewSearchScreen_SmsErrorValor");
					textValor.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					correcto=false;
				}
				if(correcto & !esNumero()){
					mensaje=Messages.getMessage("CreateNewSearchScreen_SmsErrorNumero");
					textValor.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					correcto=false;
				}
				if(!correcto){
					mensajeError(mensaje);
				}
				return correcto;
			}

			private boolean esNumero() {

				boolean res=false;
				if(comboOperador.getSelectionIndex()!=-1){
					String sel=comboOperador.getItem(comboOperador.getSelectionIndex());
					try{ 
						//Cuando los operadores son "=","<",">","<=",">="  deben ser un numero
						if(sel.equals("=") || sel.equals("!=") || sel.equals("<") || sel.equals(">") || sel.equals("<=") || sel.equals(">=")){
							if(sel.equals("=") || sel.equals("!=")){
								if(textValor.getText().trim().equals("")){
									res=true;
								}
								else{
									Double.parseDouble(textValor.getText().trim()); 
									res=true;
								}
							}
							else{
								Double.parseDouble(textValor.getText().trim()); 
								res=true;
							}
						}
						else{
							res=true;
						}
					     
					}catch(NumberFormatException e){ 
					     //La cadena no se puede convertir a entero
						textValor.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						res=false;
					}  
				}

				return res;
			}

			private boolean comprobarVacio() {

				boolean res=false;
				if(comboOperador.getSelectionIndex()!=-1){
					String sel=comboOperador.getItem(comboOperador.getSelectionIndex());
					
					//Cuando los operadores son =, !=, se permite el campo valor este vacio
					if(sel.equals("=") || sel.equals("!=")){
						res=false;
						textValor.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
					}
					else{
						if(sel.equals("between")){
							if(textValor2.getText().trim().equals("") || textValor.getText().trim().equals("")){
								res=true;
							}
						}
						else{
							if(textValor.getText().trim().equals("")){
								res=true;
							}
						}
					}
				}
				else{
					res=true;
				}
				return res;
			}
		});
		
		
	}

	public Widget getSShell() {

		return shell;
	}

}
