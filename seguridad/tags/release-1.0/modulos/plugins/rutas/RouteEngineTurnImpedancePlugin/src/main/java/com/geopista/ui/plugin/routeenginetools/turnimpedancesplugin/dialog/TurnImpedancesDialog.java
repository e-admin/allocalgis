package com.geopista.ui.plugin.routeenginetools.turnimpedancesplugin.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.structure.Edge;
import org.uva.geotools.graph.structure.Node;
import org.uva.route.graph.structure.impedance.TurnImpedances;
import org.uva.route.graph.structure.phantom.NodeWithTurnImpedances;
import org.uva.route.graph.structure.proxy.ProxyEdge;
import org.uva.route.network.Network;
import org.uva.route.util.NodeUtils;

import com.geopista.app.AppContext;
import com.geopista.editor.TaskComponent;
import com.geopista.ui.plugin.routeenginetools.turnimpedancesplugin.images.IconLoader;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.route.graph.structure.basic.LocalGISTurnImpedance;
import com.localgis.route.graph.structure.basic.TurnImpedance;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorHandler;
import com.vividsolutions.jump.workbench.ui.OKCancelPanel;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;

public class TurnImpedancesDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2181879161683207084L;
	private static String impedancesValues[] = {"No permitido","0.0"};

	private JPanel rootPanel = null;
	private JPanel turnInfoPanel = null;
	private OKCancelPanel okCancelPanel = null;

	private JLabel idEdgeA1Label = null;
	private JLabel idEdgeA2Label = null;
	private JLabel idEdgeB1Label = null;
	private JLabel idEdgeB2Label = null;
	private JLabel nameEdgeA1Label = null;
	private JLabel nameEdgeA2Label = null;
	private JLabel nameEdgeB1Label = null;
	private JLabel nameEdgeB2Label = null;

	private JButton flashFeaturesButton1 = null;
	private JButton zoomFeaturesButton1 = null;

	private JButton flashFeaturesButton2 = null;
	private JButton zoomFeaturesButton2 = null;

	private JCheckBox turnAllow1CheckBox = null;
	private JCheckBox turnAllow2CheckBox = null;

	private JComboBox impedanceValue1ComboBox = null;
	private JComboBox impedanceValue2ComboBox = null;

	private JComboBox turnImpedancesComboBox = null;
	private TurnImpedanceListCellRender turnImpedancesCellRender = null;

	private PlugInContext context = null;
	protected LocalGISNetworkDAO networkDAO = new LocalGISNetworkDAO();
	private Node node = null;
	
	public Node getNode() {
		return node;
	}

	private Network network = null;
	private static AppContext aplicationContext = (AppContext) AppContext.getApplicationContext();
	private TurnImpedance lastTurnImpedance = null;

	private static Logger LOGGER = Logger.getLogger(TurnImpedancesDialog.class);

	private ErrorHandler errorHandler;

	public TurnImpedancesDialog(PlugInContext context, String title, Node node, Network network) {
		super(context.getWorkbenchFrame(), title, true);
		this.taskFrame = (TaskComponent) context.getActiveTaskComponent();
		this.context = context;
		this.network = network;
		
		if (node.getObject() == null){
			node.setObject(new LocalGISTurnImpedance());
		}
		this.node = node;

		this.setSize(700, 230);
		this.setLocationRelativeTo(context.getWorkbenchFrame());
		this.initialize();
		onTurnImpedancesComboBoxChangedDo(null);
		this.setResizable(true);
		this.setEnabled(true);		
		this.setVisible(true);
	}


	private void initialize() {
		this.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				this_componentShown(e);
			}
		});

		this.setLayout(new GridBagLayout());

		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 20));

		this.add(this.getOkCancelPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
	}


	private JPanel getRootPanel() {
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			JPanel impedancias = new JPanel(new GridBagLayout());
			impedancias.add(new JLabel(I18N.get("turnimpedances","routeengine.turnimpedances.turnimpedanceslabel")), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			impedancias.add(this.getTurnImpedancesComboBox(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 5));

			rootPanel.add(impedancias, 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			rootPanel.add(this.getTurnInfoPanel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 20));

		}
		return rootPanel;
	}


	private JPanel getTurnInfoPanel() {
		if (turnInfoPanel == null){	
			turnInfoPanel = new JPanel(new GridBagLayout());
			turnInfoPanel.setBorder(BorderFactory.createEtchedBorder(1));

			//			turnInfoPanel.add(new JLabel("Giro"), 
			//					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
			//							GridBagConstraints.NONE, 
			//							new Insets(0, 5, 0, 5), 0, 0));
			turnInfoPanel.add(new JLabel("Resaltar"), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			turnInfoPanel.add(new JLabel("Zoom"), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			turnInfoPanel.add(new JLabel("Permitido"), 
					new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			turnInfoPanel.add(new JLabel("Valor"), 
					new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 100, 0));


			turnInfoPanel.add(this.getTurnEdgesInfo1Panel(), 
					new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			turnInfoPanel.add(this.getTurnEdgesInfo2Panel(), 
					new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));

			turnInfoPanel.add(this.getFlashFeaturesButton1(), 
					new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			turnInfoPanel.add(this.getFlashFeaturesButton2(), 
					new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));


			turnInfoPanel.add(this.getZoomFeaturesButton1(), 
					new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			turnInfoPanel.add(this.getZoomFeaturesButton2(), 
					new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			turnInfoPanel.add(this.getTurnAllow1CheckBox(),
					new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));
			turnInfoPanel.add(this.getTurnAllow2CheckBox(), 
					new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 0));

			turnInfoPanel.add(this.getImpedanceValue1ComboBox(),
					new GridBagConstraints(4, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));
			turnInfoPanel.add(this.getImpedanceValue2ComboBox(), 
					new GridBagConstraints(4, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.HORIZONTAL, 
							new Insets(0, 5, 0, 5), 0, 0));


		}
		return turnInfoPanel;
	}


	/**
	 * @return
	 */
	private JPanel getTurnEdgesInfo1Panel() {
		JPanel panel1_1 = new JPanel(new GridBagLayout());
		panel1_1.setBorder(BorderFactory.createBevelBorder(2));
		panel1_1.add(this.getNameEdgeA1Label(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel1_1.add(this.getNameEdgeB1Label(), 
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_END, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));

		JPanel panel1_2 = new JPanel(new GridBagLayout());
		panel1_2.add(this.getIdEdgeA1Label(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel1_2.add(new JLabel("---------------------------->"), 
				new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel1_2.add(this.getIdEdgeB1Label(), 
				new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));

		JPanel panel1 = new JPanel(new GridBagLayout());
		panel1.add(panel1_1, 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel1.add(panel1_2, 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		return panel1;
	}

	private JPanel getTurnEdgesInfo2Panel() {
		JPanel panel1_1 = new JPanel(new GridBagLayout());
		panel1_1.setBorder(BorderFactory.createBevelBorder(2));
		panel1_1.add(this.getNameEdgeB2Label(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel1_1.add(this.getNameEdgeA2Label(), 
				new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.FIRST_LINE_END, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));

		JPanel panel1_2 = new JPanel(new GridBagLayout());
		panel1_2.add(this.getIdEdgeB2Label(), 
				new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel1_2.add(new JLabel("---------------------------->"), 
				new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel1_2.add(this.getIdEdgeA2Label(), 
				new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.NONE, 
						new Insets(0, 5, 0, 5), 0, 0));

		JPanel panel1 = new JPanel(new GridBagLayout());
		panel1.add(panel1_1, 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		panel1.add(panel1_2, 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 0));
		return panel1;
	}


	private JLabel getIdEdgeA2Label() {
		if (idEdgeA2Label == null){
			idEdgeA2Label = new JLabel("idEdgeA");
		}
		return idEdgeA2Label;
	}


	private JLabel getIdEdgeA1Label() {
		if (idEdgeA1Label == null){
			idEdgeA1Label = new JLabel("idEdgeA");
		}
		return idEdgeA1Label;
	}

	private JLabel getIdEdgeB2Label() {
		if (idEdgeB2Label == null){
			idEdgeB2Label = new JLabel("idEdgeB");
		}
		return idEdgeB2Label;
	}


	private JLabel getIdEdgeB1Label() {
		if (idEdgeB1Label == null){
			idEdgeB1Label = new JLabel("idEdgeB");
		}
		return idEdgeB1Label;
	}


	private JLabel getNameEdgeA2Label() {
		if (nameEdgeA2Label == null){
			nameEdgeA2Label = new JLabel("nombre edge A");
		}
		return nameEdgeA2Label;
	}


	private JLabel getNameEdgeA1Label() {
		if (nameEdgeA1Label == null){
			nameEdgeA1Label = new JLabel("nombre edge A");
		}
		return nameEdgeA1Label;
	}

	private JLabel getNameEdgeB2Label() {
		if (nameEdgeB2Label == null){
			nameEdgeB2Label = new JLabel("nombre edge B");
		}
		return nameEdgeB2Label;
	}


	private JLabel getNameEdgeB1Label() {
		if (nameEdgeB1Label == null){
			nameEdgeB1Label = new JLabel("nombre edge B");
		}
		return nameEdgeB1Label;
	}

	private JCheckBox getTurnAllow1CheckBox(){
		if (turnAllow1CheckBox == null){
			turnAllow1CheckBox = new JCheckBox();
			turnAllow1CheckBox.addItemListener(new ItemListener(){
				@Override
				synchronized public void itemStateChanged(ItemEvent e) {
					onItemChangedDo(e);	
				}
			});
		}
		return turnAllow1CheckBox;
	}

	private JCheckBox getTurnAllow2CheckBox(){
		if (turnAllow2CheckBox == null){
			turnAllow2CheckBox = new JCheckBox();
			turnAllow2CheckBox.addItemListener(new ItemListener(){
				@Override
				synchronized public void itemStateChanged(ItemEvent e) {
					onItemChangedDo(e);	
				}
			});
		}
		return turnAllow2CheckBox;
	}

	synchronized private void onItemChangedDo(ItemEvent e) {
		if (e.getItem() == turnAllow2CheckBox){
			if (turnAllow2CheckBox != null){
				if (turnAllow2CheckBox.isSelected()){
					getImpedanceValue2ComboBox().setSelectedItem(actualInverseImpedance);
				} else{
					getImpedanceValue2ComboBox().setSelectedItem("No permitido");
				}
			}
		}

		if (e.getItem() == turnAllow1CheckBox){
			if (turnAllow1CheckBox != null){
				if (turnAllow1CheckBox.isSelected()){
					getImpedanceValue1ComboBox().setSelectedItem(actualDirectImpedance);
				} else{
					getImpedanceValue1ComboBox().setSelectedItem("No permitido");
				}
			}
		}
	}

	private JComboBox getImpedanceValue1ComboBox(){
		if (impedanceValue1ComboBox == null){
			impedanceValue1ComboBox = new JComboBox(this.impedancesValues);
			impedanceValue1ComboBox.setEditable(true);
		}
		return impedanceValue1ComboBox;
	}

	private JComboBox getImpedanceValue2ComboBox(){
		if (impedanceValue2ComboBox == null){
			impedanceValue2ComboBox = new JComboBox(this.impedancesValues);
			impedanceValue2ComboBox.setEditable(true);
		}
		return impedanceValue2ComboBox;
	}


	private JComboBox getTurnImpedancesComboBox() {
		if (this.turnImpedancesComboBox == null){
			turnImpedancesComboBox = new JComboBox();

			if (turnImpedancesCellRender == null){
				turnImpedancesCellRender = new TurnImpedanceListCellRender(this.network);
			}

			turnImpedancesComboBox.setRenderer(turnImpedancesCellRender);
			turnImpedancesComboBox.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					onTurnImpedancesComboBoxChangedDo(e);
				}				
			});

			ArrayList<TurnImpedance> lista = new ArrayList<TurnImpedance>();
			ArrayList<Edge> edges = (ArrayList<Edge>) this.node.getEdges();
			edges = rebuildEdgeList(edges);
			for (int i=0; i < edges.size(); i++ ){
				for (int m= 0 +i; m < edges.size(); m++){
					if (m != i){
						TurnImpedance turn = ((LocalGISTurnImpedance)this.node.getObject()).getTurnImpedance(edges.get(i).getID(), edges.get(m).getID());
						if (turn != null){
							lista.add(turn);
						}

					}
				}
			}

			if (lista != null && !lista.isEmpty()){
				com.geopista.app.utilidades.EdicionUtils.cargarLista(turnImpedancesComboBox, lista);
			}


		}
		return turnImpedancesComboBox;
	}


	private ArrayList<Edge> rebuildEdgeList(ArrayList<Edge> edges) {
		Iterator<Edge> it = edges.iterator();
		ArrayList<Edge> finalList = new ArrayList<Edge>();
		while(it.hasNext()){
			Edge edge = it.next();
			if(edge instanceof ProxyEdge)
				edge = (Edge) NodeUtils.unwrapProxies(edge);
			if(edge instanceof ILocalGISEdge)
				finalList.add(edge);
		}
		return finalList;
	}

	private double actualDirectImpedance = 0.0;
	private double actualInverseImpedance = 0.0;

	synchronized private void onTurnImpedancesComboBoxChangedDo(ActionEvent e) {
		if (turnImpedancesComboBox != null){

			TurnImpedance actualTurnImpedance = (TurnImpedance) turnImpedancesComboBox.getSelectedItem();

			if (lastTurnImpedance != null && !lastTurnImpedance.equals(actualTurnImpedance)){
//				saveTurnImpedanceData(lastTurnImpedance);
				showNewTurnImpedanceData(actualTurnImpedance);
				lastTurnImpedance = actualTurnImpedance;
			}
			else{
				showNewTurnImpedanceData(actualTurnImpedance);
				lastTurnImpedance = actualTurnImpedance;
			}
		}
	}

	synchronized private void showNewTurnImpedanceData(TurnImpedance turnImpedance) {
		LocalGISTurnImpedance turnImpedances = null;
		if (this.node instanceof NodeWithTurnImpedances){
			turnImpedances = (LocalGISTurnImpedance) ((NodeWithTurnImpedances)node).getTurnImpedances();
		} else{
			if (this.node.getObject() instanceof TurnImpedances){
				turnImpedances = (LocalGISTurnImpedance) this.node.getObject(); 
			}
		}
		
		
		if (turnImpedances != null){
			
			TurnImpedance directTurnImpedance = turnImpedances.getTurnImpedance(
					turnImpedance.getIdEdgeStart(), 
					turnImpedance.getIdEdgeEnd());
			
			TurnImpedance inverseTurnImpedance = turnImpedances.getTurnImpedance(turnImpedance.getIdEdgeEnd(), 
					turnImpedance.getIdEdgeStart());

			
			this.getIdEdgeA1Label().setText(String.valueOf(turnImpedance.getIdEdgeStart()));
			this.getIdEdgeA2Label().setText(String.valueOf(turnImpedance.getIdEdgeStart()));

			this.getIdEdgeB1Label().setText(String.valueOf(turnImpedance.getIdEdgeEnd()));
			this.getIdEdgeB2Label().setText(String.valueOf(turnImpedance.getIdEdgeEnd()));

			Object turnTextObject = this.turnImpedancesCellRender.getValuesText().get(turnImpedance);
			if (this.turnImpedancesCellRender != null && turnTextObject != null && ((String)turnTextObject).split("'").length > 1){
				String a[] = ((String)turnTextObject).split("'");
				if (a.length > 3){
					String nombre1 = a[1];
					String nombre2 = a[3];
					if (nombre1 == null){
						nombre1 = "No Encontrado";
					}
					if (nombre2 == null){
						nombre2 = "No Encontrado";
					}
					this.getNameEdgeA1Label().setText(nombre1);
					this.getNameEdgeA2Label().setText(nombre1);
					this.getNameEdgeB1Label().setText(nombre2);
					this.getNameEdgeB2Label().setText(nombre2);
				}
			} else{
				this.getNameEdgeA1Label().setText("");
				this.getNameEdgeA2Label().setText("");
				this.getNameEdgeB1Label().setText("");
				this.getNameEdgeB2Label().setText("");
			}

			if (directTurnImpedance!=null && inverseTurnImpedance!=null){
				if (directTurnImpedance != null && (directTurnImpedance.getImpedance() == Double.MAX_VALUE || 
						Double.isInfinite(directTurnImpedance.getImpedance()))){
					this.getTurnAllow1CheckBox().setSelected(false);
					this.getImpedanceValue1ComboBox().getEditor().setItem("No permitido");
				} else{
					this.getTurnAllow1CheckBox().setSelected(true);
					this.getImpedanceValue1ComboBox().setSelectedItem(directTurnImpedance.getImpedance());
					actualDirectImpedance = directTurnImpedance.getImpedance();
				}

				if (inverseTurnImpedance != null && (inverseTurnImpedance.getImpedance() == Double.MAX_VALUE ||
						Double.isInfinite(inverseTurnImpedance.getImpedance()))){
					this.getTurnAllow2CheckBox().setSelected(false);
					this.getImpedanceValue2ComboBox().getEditor().setItem("No permitido");
				} else{
					this.getTurnAllow2CheckBox().setSelected(true);
					this.getImpedanceValue2ComboBox().getEditor().setItem(inverseTurnImpedance.getImpedance());
					actualInverseImpedance = inverseTurnImpedance.getImpedance();
				}
			}
		}
	}


	synchronized public void saveTurnImpedanceData(TurnImpedance turnImpedance) {
		double impedance1 = Double.MAX_VALUE;
		/*try{
			impedance1 = (Double) this.getImpedanceValue1ComboBox().getSelectedItem();
		} catch (ClassCastException e1) {
			System.out.println();*/
		try{
			impedance1 = Double.parseDouble(this.getImpedanceValue1ComboBox().getSelectedItem().toString());
		}catch (ClassCastException e2) {
			//				e2.printStackTrace();
		}catch (NumberFormatException e){
			//				e.printStackTrace();
		}
		//}
		if (this.getTurnAllow1CheckBox().isSelected() && impedance1 != Double.MAX_VALUE){
			((LocalGISTurnImpedance)this.node.getObject()).setTurnImpedance(turnImpedance.getIdEdgeStart(), 
					turnImpedance.getIdEdgeEnd(),impedance1);
		} else{
			((LocalGISTurnImpedance)this.node.getObject()).setTurnImpedance(turnImpedance.getIdEdgeStart(), 
					turnImpedance.getIdEdgeEnd(),Double.MAX_VALUE);
		}

		double impedance2 = Double.MAX_VALUE;
		/*try{
			impedance2 = (Double) this.getImpedanceValue2ComboBox().getSelectedItem();
		} catch (ClassCastException e1) {*/
		try{
			impedance2 = Double.parseDouble(this.getImpedanceValue2ComboBox().getSelectedItem().toString());
		}catch (ClassCastException e2) {
			//				e2.printStackTrace();
		}catch (NumberFormatException e){
			//				e.printStackTrace();
		}
		//}
		if (this.getTurnAllow2CheckBox().isSelected() && (impedance2 != Double.MAX_VALUE) ){
			((LocalGISTurnImpedance)this.node.getObject()).setTurnImpedance(turnImpedance.getIdEdgeEnd(), 
					turnImpedance.getIdEdgeStart(),impedance2);
		} else{
			((LocalGISTurnImpedance)this.node.getObject()).setTurnImpedance(turnImpedance.getIdEdgeEnd(), 
					turnImpedance.getIdEdgeStart(),Double.MAX_VALUE);
		}
	}


	private OKCancelPanel getOkCancelPanel(){
		if (this.okCancelPanel == null){ 
			this.okCancelPanel = new OKCancelPanel();
			this.okCancelPanel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					okCancelPanel_actionPerformed(e);
				}
			});
		} 
		return this.okCancelPanel;
	}

	void okCancelPanel_actionPerformed(ActionEvent e) {
		if (!okCancelPanel.wasOKPressed() || isInputValid()) {
			if (turnImpedancesComboBox != null){
				TurnImpedance actualTurnImpedance = (TurnImpedance) turnImpedancesComboBox.getSelectedItem();
				saveTurnImpedanceData(actualTurnImpedance);
			}
			setVisible(false);
			return;
		}
	}

	protected boolean isInputValid() {
		return true; 
	}

	public boolean wasOKPressed() {
		return okCancelPanel.wasOKPressed();
	}

	private void this_componentShown(ComponentEvent e) {
		okCancelPanel.setOKPressed(false);
	}

	private ZoomToSelectedItemsPlugIn zoomToSelectedItemsPlugIn =
		new ZoomToSelectedItemsPlugIn();
	private TaskComponent taskFrame;

	public void zoomSelectedFeatures(int idEdgeStart, int idEdgeEnd) throws NoninvertibleTransformException {
		ArrayList<Feature> features = getStartEdgesCollectionFeatures(idEdgeStart);
		features.addAll(getEndEdgesCollectionFeatures(idEdgeEnd));
		zoomToSelectedItemsPlugIn.zoom(
				FeatureUtil.toGeometries(features),
				taskFrame.getLayerViewPanel());
	}

	public void flashSelectedFeatures(int idEdgeStart, int idEdgeEnd) throws NoninvertibleTransformException {
		zoomToSelectedItemsPlugIn.flash(
				FeatureUtil.toGeometries(getStartEdgesCollectionFeatures(idEdgeStart)),
				taskFrame.getLayerViewPanel());
		zoomToSelectedItemsPlugIn.flash(
				FeatureUtil.toGeometries(getEndEdgesCollectionFeatures(idEdgeEnd)),
				taskFrame.getLayerViewPanel());
	}

	public ArrayList<Feature> getStartEdgesCollectionFeatures(int idEdgeStart){
		ArrayList<Feature> startEdgeFeatures = new ArrayList<Feature>();
		//		ArrayList<Feature> endEdgeFeatures = new ArrayList<Feature>();

		// Buscamos las layers correspondientes.
		this.context.getLayerManager().getLayers();
		Layer networkLayer = this.context.getLayerManager().getLayer(this.network.getName());
		if (networkLayer != null){
			if (networkLayer.getFeatureCollectionWrapper().getFeatureSchema().hasAttribute("idEje")){
				Iterator<Feature> itNet = networkLayer.getFeatureCollectionWrapper().getFeatures().iterator();
				while (itNet.hasNext()){
					Feature feature = itNet.next();
					if (feature != null){
						Object edgeId = feature.getAttribute("idEje");
						if (edgeId != null){
							if (((Integer)edgeId) == idEdgeStart){
								startEdgeFeatures.add(feature);
							}
						}
					}
				}
			}
		}

		Layer edgesLayer = this.context.getLayerManager().getLayer("Arcos-" + this.network.getName());
		if (edgesLayer != null){
			if (edgesLayer.getFeatureCollectionWrapper().getFeatureSchema().hasAttribute("idEje")){
				Iterator<Feature> itNet = edgesLayer.getFeatureCollectionWrapper().getFeatures().iterator();
				while (itNet.hasNext()){
					Feature feature = itNet.next();
					if (feature != null){
						Object edgeId = feature.getAttribute("idEje");
						if (edgeId != null){
							if (((Integer)edgeId) == idEdgeStart){
								startEdgeFeatures.add(feature);
							}
						}
					}
				}
			}
		}

		return startEdgeFeatures;
	}

	public ArrayList<Feature> getEndEdgesCollectionFeatures(int idEdgeEnd){
		ArrayList<Feature> endEdgeFeatures = new ArrayList<Feature>();
		// Buscamos las layers correspondientes.
		this.context.getLayerManager().getLayers();
		Layer networkLayer = this.context.getLayerManager().getLayer(this.network.getName());
		if (networkLayer != null){
			if (networkLayer.getFeatureCollectionWrapper().getFeatureSchema().hasAttribute("idEje")){
				Iterator<Feature> itNet = networkLayer.getFeatureCollectionWrapper().getFeatures().iterator();
				while (itNet.hasNext()){
					Feature feature = itNet.next();
					if (feature != null){
						Object edgeId = feature.getAttribute("idEje");
						if (edgeId != null){
							if (((Integer)edgeId) == idEdgeEnd){
								endEdgeFeatures.add(feature);
							}
						}
					}
				}
			}
		}

		Layer edgesLayer = this.context.getLayerManager().getLayer("Arcos-" + this.network.getName());
		if (edgesLayer != null){
			if (edgesLayer.getFeatureCollectionWrapper().getFeatureSchema().hasAttribute("idEje")){
				Iterator<Feature> itNet = edgesLayer.getFeatureCollectionWrapper().getFeatures().iterator();
				while (itNet.hasNext()){
					Feature feature = itNet.next();
					if (feature != null){
						Object edgeId = feature.getAttribute("idEje");
						if (edgeId != null){
							if (((Integer)edgeId) == idEdgeEnd){
								endEdgeFeatures.add(feature);
							}
						}
					}
				}
			}
		}

		return endEdgeFeatures;
	}

	private JButton getFlashFeaturesButton1(){
		if (flashFeaturesButton1 == null){
			flashFeaturesButton1 = new JButton();
			flashFeaturesButton1.setIcon(IconLoader.icon(I18N.get("turnimpedancesplugin","routeengine.turnimpedances.iconflashfile")));
			flashFeaturesButton1.setMargin(new Insets(0, 0, 0, 0));
			String internationalzoomToolTip = aplicationContext.getI18nString("Zoom Selected Rows");
			flashFeaturesButton1.setToolTipText(internationalzoomToolTip);

			flashFeaturesButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						int idEdgeStart = -1;
						try{
							idEdgeStart = Integer.parseInt(getIdEdgeA1Label().getText());							
						} catch (NumberFormatException ex) {
						}
						int idEdgeEnd = -1;
						try{
							idEdgeEnd = Integer.parseInt(getIdEdgeB1Label().getText());
						} catch (NumberFormatException ex) {
						}
						flashSelectedFeatures(idEdgeStart, idEdgeEnd);
					} catch (Throwable t) {
						errorHandler.handleThrowable(t);
					}
				}
			});
		}
		return this.flashFeaturesButton1;
	}

	private JButton getZoomFeaturesButton1(){
		if (zoomFeaturesButton1 == null){
			zoomFeaturesButton1 = new JButton();
			zoomFeaturesButton1.setIcon(IconLoader.icon(I18N.get("turnimpedancesplugin","routeengine.turnimpedances.iconzoomfile")));
			zoomFeaturesButton1.setMargin(new Insets(0, 0, 0, 0));
			String internationalzoomToolTip = aplicationContext.getI18nString("Zoom Selected Rows");
			zoomFeaturesButton1.setToolTipText(internationalzoomToolTip);

			zoomFeaturesButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						int idEdgeStart = -1;
						try{
							idEdgeStart = Integer.parseInt(getIdEdgeA1Label().getText());							
						} catch (NumberFormatException ex) {
						}
						int idEdgeEnd = -1;
						try{
							idEdgeEnd = Integer.parseInt(getIdEdgeB1Label().getText());
						} catch (NumberFormatException ex) {
						}
						zoomSelectedFeatures(idEdgeStart, idEdgeEnd);
					} catch (Throwable t) {
						errorHandler.handleThrowable(t);
					}
				}
			});
		}		
		return this.zoomFeaturesButton1;
	}

	private JButton getZoomFeaturesButton2(){
		if (zoomFeaturesButton2 == null){
			zoomFeaturesButton2 = new JButton();
			zoomFeaturesButton2.setIcon(IconLoader.icon(I18N.get("turnimpedancesplugin","routeengine.turnimpedances.iconzoomfile")));
			zoomFeaturesButton2.setMargin(new Insets(0, 0, 0, 0));
			String internationalzoomToolTip = aplicationContext.getI18nString("Zoom Selected Rows");
			zoomFeaturesButton2.setToolTipText(internationalzoomToolTip);

			zoomFeaturesButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						int idEdgeStart = -1;
						try{
							idEdgeStart = Integer.parseInt(getIdEdgeA2Label().getText());							
						} catch (NumberFormatException ex) {
						}
						int idEdgeEnd = -1;
						try{
							idEdgeEnd = Integer.parseInt(getIdEdgeB2Label().getText());
						} catch (NumberFormatException ex) {
						}
						zoomSelectedFeatures(idEdgeStart, idEdgeEnd);
					} catch (Throwable t) {
						errorHandler.handleThrowable(t);
					}
				}
			});
		}
		return this.zoomFeaturesButton2;
	}



	private JButton getFlashFeaturesButton2(){
		if (flashFeaturesButton2 == null){
			flashFeaturesButton2 = new JButton();
			flashFeaturesButton2.setIcon(IconLoader.icon(I18N.get("turnimpedancesplugin","routeengine.turnimpedances.iconflashfile")));
			flashFeaturesButton2.setMargin(new Insets(0, 0, 0, 0));
			String internationalzoomToolTip = aplicationContext.getI18nString("Zoom Selected Rows");
			flashFeaturesButton2.setToolTipText(internationalzoomToolTip);

			flashFeaturesButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						int idEdgeStart = -1;
						try{
							idEdgeStart = Integer.parseInt(getIdEdgeB2Label().getText());							
						} catch (NumberFormatException ex) {
						}
						int idEdgeEnd = -1;
						try{
							idEdgeEnd = Integer.parseInt(getIdEdgeA2Label().getText());
						} catch (NumberFormatException ex) {
						}
						flashSelectedFeatures(idEdgeStart, idEdgeEnd);
					} catch (Throwable t) {
						errorHandler.handleThrowable(t);
					}
				}
			});
		}
		return this.flashFeaturesButton2;
	}
}
