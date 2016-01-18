/**
 * ImporterEditorPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.importer.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.geopista.app.AppContext;
import com.geopista.feature.Attribute;
import com.geopista.feature.GeopistaFeature;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.SchemaValidator;
import com.geopista.feature.ValidationError;
import com.geopista.model.GeopistaLayer;
import com.geopista.ui.plugin.importer.FeatureTransformer;
import com.geopista.ui.plugin.importer.ImporterPlugIn;
import com.geopista.ui.plugin.importer.beans.ConstantesImporter;
import com.geopista.ui.plugin.importer.beans.NodoDominio;
import com.geopista.ui.plugin.importer.beans.TableFeaturesValue;
import com.geopista.ui.plugin.importer.panels.renderer.TableFeaturesCellRenderer;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.BasicFeature;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


/**
 * @author rubengomez
 *
 */
public class ImporterEditorPanel extends JPanel implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;

	public static Blackboard bk=new Blackboard();// for testing only

	private SelectFieldPanel selectFieldPanel = null;
	private MapPanel mapPanel = null;
	private static FeatureTransformer transformer;  
	private JTabbedPane jTabbedPane = null;
	private JScrollPane jTestFeaturesScrollPane = null;
	private JScrollPane jReportScrollPane = null;
	
	private JFileChooser chooser = null;
	private JPanel jPanel = null;
	private JButton checkButton = null;
	private JButton exportButton = null;
	private JButton importButton = null;
	private JButton salirButton = null;
	private JButton jTransferButton = null;
	private JScrollPane jScrollPane = null;
	private JTable reportTable = null;
	private JTable resultTable = null;
	private JTable origTable = null;
	private Hashtable<Integer, String> htTargetAttributes = new Hashtable<Integer, String>();

	ApplicationContext appContext=AppContext.getApplicationContext();

	private static final String SELECTEDTARGETLAYER = ImporterPlugIn.class.getName()+"_targetLayer";
	private static final String SELECTEDSOURCELAYER = ImporterPlugIn.class.getName()+"_sourceLayer";
	private static final String ATTRIBUTE_ID_ON_TARGET = ImporterPlugIn.class.getName()+"ATTRIBUTE_ID_ON_TARGET"; 
	private static final String ATTRIBUTE_NAME = ImporterPlugIn.class.getName()+"ATTRIBUTE_NAME"; 
	private static final String FORMULA = ImporterPlugIn.class.getName()+"FORMULA"; 


	private PlugInContext context = null;

	private int geometryIndex;
	private int idIndex;
	private int idMunIndex;

	private boolean markedCells = false;

	private ArrayList<GeopistaFeature> toImportGeopistaFeatures = null;

	private JSplitPane jTablePanel = null;
	//private JTableHeader origTableHeader = null;
	//private JTableHeader resultTableHeader = null;

	private JPanel jResultPanel = null;
	private JPanel jOrigPanel = null;

	private JScrollPane jScrollPaneLeft = null;

	private JScrollPane jScrollPaneRigth;

	private JSplitPane jSplitPane = null;
	private JPanel jPanelLeft = null;
	private JPanel jPanelRigth = null;

	private JTableHeader resultTableHeader = null;
	private JTableHeader origTableHeader = null;

	/**
	 * This is the default constructor
	 */
	public ImporterEditorPanel(PlugInContext context) {
		super();
		this.context = context;
		initialize();		
	}

	public ImporterEditorPanel(FeatureTransformer transf, PlugInContext context) {
		super();
		this.context = context;
		initialize();
		setTransformer(transf);
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize()
	{
		this.setLayout(new GridBagLayout());		

		this.add(getJScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(5, 0, 10, 0),0,0)); 
		this.add(getSelectFieldPanel(), new GridBagConstraints(1, 0, 1, 1, 0.25, 0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(5, 10, 10, 0),0,0));  

		this.add(getJTabbedPane(), new GridBagConstraints(0, 1, 2, 1, 1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0)); 

		this.add(getJPanel(), new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));  
	}

	/**
	 * This method initializes selectFieldPanel1	
	 * 	
	 * @return com.geopista.importer.panels.SelectFieldPanel	
	 */
	private SelectFieldPanel getSelectFieldPanel() {
		if (selectFieldPanel == null) {

			selectFieldPanel = new SelectFieldPanel();
			selectFieldPanel.setBorder(BorderFactory.createTitledBorder(null, I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.camposorigen.titulo"),
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));   

			selectFieldPanel.setPreferredSize(new Dimension (400, 400));
			selectFieldPanel.setMinimumSize(selectFieldPanel.getPreferredSize());
			selectFieldPanel.setMaximumSize(selectFieldPanel.getPreferredSize());			

			selectFieldPanel.addPropertyChangeListener(
					new PropertyChangeListener()
					{
						public void propertyChange(PropertyChangeEvent e)
						{ 							
							if (e.getPropertyName().equals("FIELDVALUE"))
							{
								String attName=(String) e.getNewValue();
								putToBlackboard(ATTRIBUTE_ID_ON_TARGET, attName);
							}
						}}
			);	

		}
		return selectFieldPanel;
	}

	/**
	 * This method initializes mapPanel	
	 * 	
	 * @return com.geopista.importer.panels.MapPanel	
	 */
	private MapPanel getMapPanel() {
		if (mapPanel == null) {
			mapPanel = new MapPanel(context);
			mapPanel.addPropertyChangeListener(new PropertyChangeListener(){

				public void propertyChange(PropertyChangeEvent evt) {
					//eventPropertyAdapter.firePropertyChange(evt);

					if (evt.getPropertyName().equals("ExpressionValid") || 
							evt.getPropertyName().equals("ExpressionInvalid"))
					{
						//calculateRows();
						if (evt.getSource() instanceof MapPanel)
							calculateColumns((MapPanel)evt.getSource());
					}
				}});			

		}
		return mapPanel;
	}

	protected void calculateColumns(MapPanel mappanel) {

		Layer srcLayer = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
		GeopistaLayer targetLayer = (GeopistaLayer) getFromBlackboard(SELECTEDTARGETLAYER);
		Collection features = getToImportFeatures(srcLayer, true);
		Iterator itFeat = features.iterator();
		GeopistaSchema gsch = (GeopistaSchema)targetLayer.getFeatureCollectionWrapper().getFeatureSchema();

		Vector<String> names = new Vector<String>();		
		for (int i=0; i< ((DefaultTableModel)reportTable.getModel()).getColumnCount(); i++)
		{	
			reportTable.getColumnModel().getColumn(i).setCellRenderer(new TableFeaturesCellRenderer());

			String name = ((DefaultTableModel)reportTable.getModel()).getColumnName(i);
			names.add(name);
		}		

		Vector<Vector<TableFeaturesValue>> rows = new Vector<Vector<TableFeaturesValue>>();
		//Vector totalRows = new Vector();
		int numMapFields = mappanel.getComponentCount();

		int j=0;
		while(itFeat.hasNext())
		{			
			Vector<TableFeaturesValue> row = new Vector<TableFeaturesValue>();
			Vector<Object> totalRow = new Vector<Object>();

			Object obj= (Object)itFeat.next();
			
			GeopistaFeature bfeat = (GeopistaFeature)obj;
			GeopistaFeature gfeat = new GeopistaFeature(gsch);

			for (int i=0; i< numMapFields; i++)
			{
				if (((MapFieldPanel)mappanel.getComponent(i)).getExpParser(j).toString().equals(ConstantesImporter.errorNoExpression))
				{
					Object valor = new String("");
						
					TableFeaturesValue tfv = new TableFeaturesValue(
							((MapFieldPanel)mappanel.getComponent(i)).getAttributeName(), 
							valor, 
							null);
					
					if (((MapFieldPanel)mappanel.getComponent(i)).getAttributeType().equals(AttributeType.DATE.toString()))
					{
						tfv.setType(TableFeaturesValue.DATE_TYPE);
						tfv.setObj(null);
					}
					
					row.add(tfv);
					totalRow.add(null);
				}
				
				else
				{
					TableFeaturesValue tfv = new TableFeaturesValue(
							((MapFieldPanel)mappanel.getComponent(i)).getAttributeName(), 
							((MapFieldPanel)mappanel.getComponent(i)).getExpParser(j), 
							null);
					Object valor = ((MapFieldPanel)mappanel.getComponent(i)).getExpParser(j);
					if (((MapFieldPanel)mappanel.getComponent(i)).getAttributeType().equals(AttributeType.DATE.toString()))
					{
						tfv.setType(TableFeaturesValue.DATE_TYPE);
						if (valor.equals(""))
						{
							tfv.setObj(null);
							valor = null;
						}
						else if (valor instanceof String)
						{
							DateFormat dateformat = new SimpleDateFormat(ConstantesImporter.DATEFORMAT);
							Format formatter = new SimpleDateFormat(ConstantesImporter.DATEFORMAT);
	                    	try {
								Date date = (Date)dateformat.parse(tfv.getObj().toString());
								valor = formatter.format(date);
								tfv.setObj(date);
							} catch (ParseException e) {
								//e.printStackTrace();	
								valor =tfv.getObj().toString();
								tfv.setVe(new ValidationError(tfv.getName(), "Formato no válido (use dd-MM-yyyy)", null));
							}
						}							
					}
					
					row.add(tfv);
					totalRow.add(tfv.getObj());					
				}
			}
			j++;

			int tamRow = numMapFields;
			if (geometryIndex >= 0)
				tamRow++;
			if (idIndex >=0)
				tamRow++;
			if (idMunIndex >=0)
				tamRow++;
			
			
			Vector<Object> sortedRow = new Vector<Object>();
			sortedRow.setSize(tamRow);
			int k=0;
			for (int i=0; i<tamRow ;i++)
			{
				if(i==geometryIndex)
				{
					sortedRow.set(i, bfeat.getGeometry());
				}
				else if (i==idIndex || i==idMunIndex)
				{
					sortedRow.set(i, new Integer(0));
				}
				else 
				{
					sortedRow.set(i, totalRow.elementAt(k));
					k++;
				}
			}
						
				
			gfeat.setAttributesRaw(sortedRow.toArray());
			try{
				gfeat.setGeometry(bfeat.getGeometry());	
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(this,
						I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.mensaje.geometrianovalida")						
				);

				firePropertyChange("LAYER_ERROR", null, "InvalidGeometry");
				return;
			}


			SchemaValidator validator = new SchemaValidator(null);
			if(!validator.validateFeature(gfeat))
			{				
				markedCells = updateRow(row, validator.getErrorListIterator());
			}
			rows.add(row);	
		}				

		reportTable.setModel(new DefaultTableModel(rows, names));	

		for (int i=0; i< ((DefaultTableModel)reportTable.getModel()).getColumnCount(); i++)
		{	
			TableColumn tc = reportTable.getColumnModel().getColumn(i);
			tc.setCellRenderer(new TableFeaturesCellRenderer());			
		}

		//si no hay celdas marcadas, se habilita el boton 
		getCheckButton().setEnabled(!markedCells);
		reportTable.setEnabled(false);
		markedCells = false;
	}

	private boolean updateRow(Vector<TableFeaturesValue> row,
			Iterator<ValidationError> errorListIterator) {

		boolean markedCells = false;

		while (errorListIterator.hasNext())
		{
			ValidationError ve = errorListIterator.next();
			//System.out.println("ERROR validacion: " + ve.attName + " -> " + ve.message);			

			Iterator<TableFeaturesValue> itRow = row.iterator();
			while (itRow.hasNext())
			{
				TableFeaturesValue tfv = itRow.next();
				if (tfv.getName().equals(ve.attName))
				{					
					if (tfv.getObj() instanceof String && 
							(tfv.getObj().toString().equals("") ||
									tfv.getObj().toString().equals("FALLO VALIDACION"))
					)
					{
						tfv.setObj(null);						
					}
					//Hay fallos de validación si se introduce una fecha vacía
					else if (tfv.getObj() instanceof Date)
					{
						tfv.setObj(null);
						tfv.setVe(ve);
					}
					else
					{
						tfv.setVe(ve);						
					}

					markedCells = true;
				}
			}
		}	
		return markedCells;
	}

	public FeatureTransformer getTransformer() {
		return transformer;
	}

	public void setTransformer(FeatureTransformer transformer) {
		this.transformer = transformer;
		getSelectFieldPanel().populateList(transformer.getSourceSchema());

		getSelectFieldPanel().setLstCellRenderer(new ListCellRenderer() 
		{
			JLabel label=new JLabel();

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{
				label.setText(((String)value).replaceAll(" ", "_"));
				label.setPreferredSize(new Dimension(100, 18));
				label.setOpaque(true);
				label.setBackground(isSelected|| cellHasFocus ? Color.black : Color.white);
				label.setForeground(isSelected|| cellHasFocus ? Color.white : Color.black);

				return label;
			}
		});

		getMapPanel().configure(transformer);

		getMapPanel().addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				Object src=e.getSource();
				//System.out.println("Action: "+e.getActionCommand()+" en "+e.getSource());
				MapFieldPanel fieldPanel= ((MapFieldPanel)e.getSource());
				fieldPanel.setSchema(ImporterEditorPanel.transformer.getSourceSchema());
				fieldPanel.setFeatures(getToImportFeatures((Layer) getFromBlackboard(SELECTEDSOURCELAYER), true));

				Hashtable htRelations = fieldPanel.getHtRelations();
				if (e.getActionCommand().equals("<<") && htRelations!=null && !htRelations.isEmpty())
				{
					StringBuffer txtClave = new StringBuffer();
					StringBuffer txtValor = new StringBuffer();
					Enumeration claves = htRelations.keys();
					while (claves.hasMoreElements())
					{
						Object clave = claves.nextElement();
						txtClave.append(clave.toString()).append(";");
						txtValor.append(((NodoDominio)htRelations.get(clave)).getPatron()).append(";");
					}

					String strClave = txtClave.substring(0, txtClave.length()-1);
					String strValor = txtValor.substring(0, txtValor.length()-1);

					fieldPanel.appendText("map(" + getSelectFieldPanel().getSelectedFieldName().replaceAll(" ", "_")+","
							+ "\""+ strClave+"\",\""+ strValor + "\")");
				}
				else if (fieldPanel.isDomain())
				{
					fieldPanel.appendText("");
				}
				else
				{		
					if (getSelectFieldPanel().getSelectedFieldName()!=null)
						fieldPanel.appendText(getSelectFieldPanel().getSelectedFieldName().replaceAll(" ", "_"));
				}

			}});
		getMapPanel().addPropertyChangeListener(new PropertyChangeListener(){

			public void propertyChange(PropertyChangeEvent evt) {

				if (evt.getSource() instanceof MapPanel 
						&& ImporterEditorPanel.transformer.getSourceSchema()!=null)
				{
					MapPanel panel = ((MapPanel)evt.getSource());
					for (int i=0; i< panel.getComponentCount(); i++)
					{
						MapFieldPanel fieldPanel= ((MapFieldPanel)panel.getComponent(i));
						fieldPanel.setSchema(ImporterEditorPanel.transformer.getSourceSchema());
						fieldPanel.setFeatures(getToImportFeatures((Layer) getFromBlackboard(SELECTEDSOURCELAYER),true));						
					}					
				}
				//System.out.println("Property: "+evt.getPropertyName()+" en "+evt.getSource());				
			}});
		if(getFromBlackboard(ImporterPlugIn.SELECTEDSOURCELAYER) != null && getFromBlackboard(ImporterPlugIn.SELECTEDTARGETLAYER) != null){
			System.out.println(getFromBlackboard(ImporterPlugIn.SELECTEDSOURCELAYER));
			System.out.println(getFromBlackboard(ImporterPlugIn.SELECTEDTARGETLAYER));
			getJImportButton().setEnabled(true);
		}
	}

	/**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.pestana.featuresprueba.titulo"), null, getJTestFeaturesScrollPane(), null);  
			jTabbedPane.addTab(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.pestana.informeerrores.titulo"), null, getJReportScrollPane(), null);  
			jTabbedPane.setEnabledAt(1, false);

			jTabbedPane.setPreferredSize(new Dimension (300, 300));
			jTabbedPane.setMinimumSize(jTabbedPane.getPreferredSize());
			jTabbedPane.setMaximumSize(jTabbedPane.getPreferredSize());
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jTestFeaturesScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJTestFeaturesScrollPane() {
		if (jTestFeaturesScrollPane == null) {			
			jTestFeaturesScrollPane = new JScrollPane();
			jTestFeaturesScrollPane.setViewportView(getReportTable());  
		}
		return jTestFeaturesScrollPane;
	}

	/**
	 * This method initializes jReportScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJReportScrollPane() {
		if (jReportScrollPane == null) {
			jReportScrollPane = new JScrollPane();//getSplitPanel());
			jReportScrollPane.setViewportView(getSplitPanel());
			jReportScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return jReportScrollPane;
	}

	private JSplitPane getSplitPanel() {
		if (jSplitPane == null){
			jSplitPane  = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					//getResultTable(),getOrigTable());
					getJPanelLeftPosition(), getJPanelRightPosition());

			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setDividerLocation(0.5);
			jSplitPane.setDividerSize(10);	

			//jSplitPane.setPreferredSize(new Dimension(290, 250));
			//jSplitPane.setMaximumSize(jSplitPane.getPreferredSize());
		}		
		return jSplitPane;
	}

	private JPanel getJPanelLeftPosition() {
		if (jPanelLeft == null)
		{
			jPanelLeft = new JPanel();
			jPanelLeft.setLayout(new GridBagLayout());

			jPanelLeft.add(getResultTableHeader(),  new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));

			jPanelLeft.add(getResultTable(),  new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));

		}		
		return jPanelLeft;
	}

	private JTableHeader getResultTableHeader() {
		if (resultTableHeader==null)
		{
			resultTableHeader  = getResultTable().getTableHeader();
		}
		return resultTableHeader;
	}

	private JPanel getJPanelRightPosition() {

		if (jPanelRigth == null)
		{
			jPanelRigth = new JPanel();
			jPanelRigth.setLayout(new GridBagLayout());

			jPanelRigth.add(getOrigTableHeader(),  new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),0,0));

			jPanelRigth.add(getOrigTable(),  new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0,0,0,0),0,0));
		}		
		return jPanelRigth;
	}

	private Component getOrigTableHeader() {
		if (origTableHeader==null)
		{
			origTableHeader  = getOrigTable().getTableHeader();
		}
		return origTableHeader;
	}

	private JScrollPane getJScrollPaneLeftPosition() {
		if (jScrollPaneLeft == null)
		{
			jScrollPaneLeft = new JScrollPane();
			jScrollPaneLeft.setViewportView(getResultTable());
		}		
		return jScrollPaneLeft;
	}

	private JScrollPane getJScrollPaneRightPosition() {

		if (jScrollPaneRigth == null)
		{
			jScrollPaneRigth = new JScrollPane();
			jScrollPaneRigth.setViewportView(getOrigTable());
		}		
		return jScrollPaneRigth;
	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout());   
			jPanel.add(getJImportButton());
			jPanel.add(getCheckButton());   
			jPanel.add(getJExportButton());
			jPanel.add(getJTransferButton());
			jPanel.add(getJSalirButton()); 
			
		}
		return jPanel;
	}

	/**
	 * This method initializes checkButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCheckButton() {
		if (checkButton == null) {
			checkButton = new JButton();
			checkButton.setText(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.boton.validar"));   
			checkButton.setEnabled(false);
			checkButton.setPreferredSize(new Dimension(130, 25));
			checkButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {    

					int answer = JOptionPane.showConfirmDialog(ImporterEditorPanel.this, 
							I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.mensaje.validacion.inicio"));
					if (answer == JOptionPane.YES_OPTION) {
						validateLocalFile(getMapPanel());
					} 					
				}
			});
		}
		return checkButton;
	}
	/**
	 * This method initializes exportXmlButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJExportButton() {
		if (exportButton == null) {
			exportButton = new JButton();
			exportButton.setText(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.boton.exportar"));   
			exportButton.setEnabled(false);
			exportButton.setPreferredSize(new Dimension(130, 25));
			exportButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {  
					JFileChooser fChooser = getChooser();
					int selection = fChooser.showSaveDialog(jOrigPanel);
		            // Get the selected file
					if(selection == JFileChooser.APPROVE_OPTION){
						File file = fChooser.getSelectedFile();
						if(!file.getAbsolutePath().toLowerCase().contains(".xml"))
							file = new File(file.getAbsoluteFile()+".xml");
						saveFile(file);
					}					
				}
			});
		}
		return exportButton;
	}
	protected JFileChooser getChooser() {
		if(chooser == null){
			String base =  AppContext.getApplicationContext().getString("ruta.base.mapas");
			System.out.println(base);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("xml","xml");
			chooser = new JFileChooser(base);
			chooser.setFileFilter(filter);
		}
		return chooser;
	}

	/**
	 * 	Metodo que rellena los campos de fusion desde un archivo de configuracion
	 * @param file - archivo donde se almacena
	 * @throws Exception - En caso de errores o incoherencias en el archivo de configuracion
	 */
	protected void loadFile(File file) throws Exception {
		Hashtable<String,String> ht = readFile(file);
		Component[] components = getMapPanel().getComponents();
		for(int i = 0; i< components.length;i++){
			if(components[i] instanceof MapFieldPanel){
				MapFieldPanel field = (MapFieldPanel)components[i];
				if(ht.get(field.getAttributeName()) != null){
					field.appendText((String)ht.get(field.getAttributeName()));
				}
			}
		}
		
	}

	/**
	 * This method initializes importButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJImportButton() {
		if (importButton == null) {
			importButton = new JButton();
			importButton.setText(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.boton.importar"));   
			importButton.setEnabled(false);
			importButton.setPreferredSize(new Dimension(130, 25));
			importButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {  
					JFileChooser jChooser = getChooser();
					
					int selection = jChooser.showOpenDialog(jOrigPanel);
		            // Get the selected file
					if(selection == JFileChooser.APPROVE_OPTION){
					
		            // Get the selected file
			            File file = jChooser.getSelectedFile();
			            try {
							loadFile(file);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null,I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.error.ficheronovalido"),I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.error.error"), JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return importButton;
	}
	/**Metodo que guarda un archivo de configuracion. Se encarga de obtener los datos
	 * del panel para poder escribirlos en un xml
	 * 
	 * @param file - Ruta del archivo
	 * 
	 */
	protected void saveFile(File file) {
		
		Layer sLayer = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
		GeopistaLayer tLayer = (GeopistaLayer) getFromBlackboard(SELECTEDTARGETLAYER);
		String sourceLayer = sLayer.getName();
		String targetLayer = tLayer.getName();
		Hashtable<String,String> info = new Hashtable<String,String>();
		Component[] components = getMapPanel().getComponents();
		for(int i = 0; i< components.length;i++){
			if(components[i] instanceof MapFieldPanel){
				MapFieldPanel field = (MapFieldPanel)components[i];
				if(field.getText() != null && !field.getText().equals("")){
					info.put(field.getAttributeName(),field.getText());
				}
			}
		}
		try {
			buildFile(file,sourceLayer,targetLayer,info);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Metodo para guardar un archivo de configuración del importador automatico para una capa determinada y un archivo determinado
	 * 
	 * @param file - Ruta y nombre final del archivo
	 * @param sourceLayer - Nombre de la capa origen
	 * @param targetLayer - Nombre de la capa destino
	 * @param info - Hashtable con las claves y los valores de la configuración
	 * @throws ParserConfigurationException - Excepcion producida por error en generacion de los nodos.
	 * @throws TransformerException - Excepcion por error en transformacion a xml
	 * @throws IOException - Error al crear el archivo
	 */
	private void buildFile(File file, String sourceLayer, String targetLayer,Hashtable<String,String> info) throws ParserConfigurationException, TransformerException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement("layerImporter");
        document.appendChild(rootElement);
        //rootElement.appendChild(document.createTextNode(arg0));
        Element source = document.createElement("sourceLayer");
        source.appendChild(document.createTextNode(sourceLayer));
        Element target = document.createElement("targetLayer");
        target.appendChild(document.createTextNode(targetLayer));
        rootElement.appendChild(source);
        rootElement.appendChild(target);
        Element columns = document.createElement("columns");
        Set<String> keys = info.keySet();
        Iterator<String> it = keys.iterator();
        while(it.hasNext()){
        	String sourceData = it.next();
        	String targetData = info.get(sourceData);
        	Element column = document.createElement("column");
        	Element sourceColumn = document.createElement("source");
        	Element targetColumn = document.createElement("target");
        	sourceColumn.appendChild(document.createTextNode(sourceData));
        	targetColumn.appendChild(document.createTextNode(targetData));
        	column.appendChild(sourceColumn);
        	column.appendChild(targetColumn);
        	columns.appendChild(column);
        }
        rootElement.appendChild(columns);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        FileOutputStream out = new FileOutputStream(file);
        StreamResult result =  new StreamResult(out);
        transformer.transform(domSource, result);

  }
		
	/** Metodo para leer un archivo de configuracion previamente guardado
	 * @param file - Archivo de configuracion
	 * @return Hashtable con la relacion de campos
	 * @throws Exception - En caso de haber configuraciones erroneas
	 */
	private Hashtable<String,String> readFile(File file) throws Exception{
		Hashtable<String,String> info = new Hashtable<String,String>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeSourceList = doc.getElementsByTagName("sourceLayer");
		String sourceLayer = nodeSourceList.item(0).getTextContent();
	
		NodeList nodeTargetList = doc.getElementsByTagName("targetLayer");
		String targetLayer = nodeTargetList.item(0).getTextContent();
		  
		Layer sLayer = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
		GeopistaLayer tLayer = (GeopistaLayer) getFromBlackboard(SELECTEDTARGETLAYER);
		if(!sLayer.getName().equals(sourceLayer) || !tLayer.getName().equals(targetLayer))
			throw new Exception("Las capas no coinciden en los archivos");
		NodeList nodeLst = doc.getElementsByTagName("column");
		for (int i = 0; i < nodeLst.getLength(); i++) {
	
		    Node fstNode = nodeLst.item(i);
		    
		    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
		    	String source = "";
		    	String target = "";
		    	Element fstElmnt = (Element) fstNode;
		    	NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("source");
		    	Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
		    	source = fstNmElmnt.getTextContent();
		    	NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("target");
		    	Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		    	target = lstNmElmnt.getTextContent();
		    	info.put(source, target);
	//		    	NodeList fstNm = fstNmElmnt.getChildNodes();
	//			    	System.out.println("First Name : "  + ((Node) fstNm.item(0)).getNodeValue());
		      /*NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("lastname");
		      Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
		      NodeList lstNm = lstNmElmnt.getChildNodes();
		      System.out.println("Last Name : " + ((Node) lstNm.item(0)).getNodeValue());*/
		    }
	
		  }
		return info;
	}

	/**
	 * This method initializes salirButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJSalirButton() {
		if (salirButton == null) {
			salirButton = new JButton();
			salirButton.setText(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.boton.salir"));   
			salirButton.setEnabled(true);
			salirButton.setPreferredSize(new Dimension(130, 25));
			salirButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {    
					JDialog d = ((JDialog)SwingUtilities.getAncestorOfClass(JDialog.class, ImporterEditorPanel.this));
					if (d!=null)
						d.dispose();
				}
			});
		}
		return salirButton;
	}
	

	protected void validateLocalFile(final MapPanel mappanel) {		
		
		toImportGeopistaFeatures = new ArrayList<GeopistaFeature>();

		final TaskMonitorDialog progressDialog = new TaskMonitorDialog((Frame)SwingUtilities.getAncestorOfClass(Frame.class, this), null, true);

		progressDialog.setTitle(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.progreso.validando.titulo"));
		progressDialog.report(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.progreso.validando.mensaje"));
		progressDialog.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{
				// Wait for the dialog to appear before starting the
				// task. Otherwise
				// the task might possibly finish before the dialog
				// appeared and the
				// dialog would never close. [Jon Aquino]
				new Thread(new Runnable()
				{
					public void run()
					{
						try
						{
							Layer srcLayer = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
							GeopistaLayer targetLayer = (GeopistaLayer) getFromBlackboard(SELECTEDTARGETLAYER);
							Collection features = getToImportFeatures(srcLayer);

							int numFeatures = features.size();
							int procFeature = 0;
							if (features!= null)
							{								
								Iterator itFeat = features.iterator();
								GeopistaSchema gsch = (GeopistaSchema)targetLayer.getFeatureCollectionWrapper().getFeatureSchema();

								Vector<String> names = new Vector<String>();		
								for (int i=0; i< ((DefaultTableModel)reportTable.getModel()).getColumnCount(); i++)
								{	
									names.add(((DefaultTableModel)reportTable.getModel()).getColumnName(i));
								}

								Vector<String> origNames = new Vector<String>();
								FeatureSchema fs = srcLayer.getFeatureCollectionWrapper().getFeatureSchema();
								for (int i=0; i< fs.getAttributeCount(); i++)
								{	
									origNames.add(fs.getAttributeName(i));				
								}				

								Vector<Vector<TableFeaturesValue>> rows = new Vector<Vector<TableFeaturesValue>>();
								Vector<Vector> origRows = new Vector<Vector>();

								Vector<GeopistaFeature> vFeatures = new Vector<GeopistaFeature>();

								int numMapFields = mappanel.getComponentCount();
								
								Vector parserValues = new Vector();
								for (int i =0; i<numMapFields; i++)
								{
									((MapFieldPanel)mappanel.getComponent(i)).setFeatures(features);
									parserValues.add(((MapFieldPanel)mappanel.getComponent(i)).getParserValues());
								}
																
								int j=0;
								while(itFeat.hasNext())
								{		
									progressDialog.report(++procFeature, numFeatures, 
											I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.progreso.validando.entidad"));

									Vector row = new Vector();
									Vector origRow = new Vector();
									Vector<Object> totalRow = new Vector<Object>();

									String falloParser = new String ("FALLO VALIDACION");

									GeopistaFeature bfeat = (GeopistaFeature)itFeat.next();
									//BasicFeature bfeat = (BasicFeature)itFeat.next();
									GeopistaFeature gfeat = new GeopistaFeature(gsch);
									vFeatures.add(gfeat);

									
									for (int i=0; i< numMapFields; i++)
									{	
										//Object o=((MapFieldPanel)mappanel.getComponent(i)).getExpParser(j);
										Object o=((Vector)parserValues.get(i)).get(j);
										
										if (o==null)
										{
											String attname =((MapFieldPanel)mappanel.getComponent(i)).getAttributeName();
											TableFeaturesValue tfv = new TableFeaturesValue(
													attname, 
													falloParser,
													new ValidationError(attname, falloParser, null));
											
											if (((MapFieldPanel)mappanel.getComponent(i)).getAttributeType().equals(AttributeType.DATE.toString()))
											{
												tfv.setType(TableFeaturesValue.DATE_TYPE);
											}
											
											row.add(tfv);
											totalRow.add(falloParser);
											
										}
										else if (o.toString().equals(ConstantesImporter.errorNoExpression))
										{
											TableFeaturesValue tfv = new TableFeaturesValue(
													((MapFieldPanel)mappanel.getComponent(i)).getAttributeName(), 
													"", 
													null);
											if (((MapFieldPanel)mappanel.getComponent(i)).getAttributeType().equals(AttributeType.DATE.toString()))
											{
												tfv.setType(TableFeaturesValue.DATE_TYPE);												
											}										
											
											row.add(tfv);
											totalRow.add(null);
										}
										else
										{
											TableFeaturesValue tfv = new TableFeaturesValue(
													((MapFieldPanel)mappanel.getComponent(i)).getAttributeName(), 
													o, 
													null);
											
											if (((MapFieldPanel)mappanel.getComponent(i)).getAttributeType().equals(AttributeType.DATE.toString()))
											{
												tfv.setType(TableFeaturesValue.DATE_TYPE);
												if (o.equals(""))
												{
													o = null;
													tfv.setObj(o);
												}
												else if (o instanceof String)
												{
													DateFormat dateformat = new SimpleDateFormat(ConstantesImporter.DATEFORMAT);
													Format formatter = new SimpleDateFormat(ConstantesImporter.DATEFORMAT);
							                    	try {
														Date date = (Date)dateformat.parse(tfv.getObj().toString());
														o = formatter.format(date);
														tfv.setObj(date);
													} catch (ParseException e) {
														//e.printStackTrace();	
														o =tfv.getObj().toString();
														tfv.setVe(new ValidationError(tfv.getName(), 
																I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.mensaje.formatofechanovalido"), null));
													}
												}			
											}
											
											row.add(tfv);
											totalRow.add(tfv.getObj());					
										}										
									}
									
									
									j++;

																	
									int tamRow = numMapFields;
									if (geometryIndex >= 0)
										tamRow++;
									if (idIndex >=0)
										tamRow++;
									if (idMunIndex >=0)
										tamRow++;
									/*
									totalRow.setSize(tamRow);
									
									totalRow.set(geometryIndex, bfeat.getGeometry());
												
									if (idIndex >=0)									
										totalRow.set(idIndex, new Integer(0));
										
									if (idMunIndex>=0)
										totalRow.set(idMunIndex, new Integer(0));
									*/
									
									Vector<Object> sortedRow = new Vector<Object>();
									sortedRow.setSize(tamRow);
									int k=0;
									for (int i=0; i<tamRow ;i++)
									{
										if(i==geometryIndex)
										{
											sortedRow.set(i, bfeat.getGeometry());
										}
										else if (i==idIndex || i==idMunIndex)
										{
											sortedRow.set(i, new Integer(AppContext.getIdMunicipio()));
										}
										else 
										{
											sortedRow.set(i, totalRow.elementAt(k));
											k++;
										}
									}
									

									boolean markedCell = false;
									SchemaValidator validator = new SchemaValidator(null);

									
									if (sortedRow.contains(falloParser))
									{
										markedCell = true;
										int ind = sortedRow.indexOf(falloParser);
										sortedRow.remove(ind);
										sortedRow.add(ind, null);
									}

									gfeat.setAttributesRaw(sortedRow.toArray());

									try{
										gfeat.setGeometry(bfeat.getGeometry());	
									}
									catch (Exception e)
									{
										JOptionPane.showMessageDialog(ImporterEditorPanel.this,
												I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.mensaje.geometrianocorresponde")																								
										);

										firePropertyChange("LAYER_ERROR", null, "InvalidGeometry");
										return;
									}
									
									if(!validator.validateFeature(gfeat))
									{	
										markedCell = updateRow(row, validator.getErrorListIterator());
									}	
									else
									{
										gfeat.setLayer(targetLayer);
										//gfeat.setNew(true);
										//gfeat.setDirty(true);
										toImportGeopistaFeatures.add(gfeat);
									}
									
									
									origRow.addAll(Arrays.asList(bfeat.getAttributes()));

									if (markedCell)
									{
										rows.add(row);
										origRows.add(origRow);
									}
									
								}

								resultTable.setModel(new DefaultTableModel(rows, names));	
								origTable.setModel(new DefaultTableModel(origRows, origNames));	

								origTableHeader = origTable.getTableHeader();
								resultTableHeader = resultTable.getTableHeader();

								for (int i=0; i< ((DefaultTableModel)resultTable.getModel()).getColumnCount(); i++)
								{	
									TableColumn tc = resultTable.getColumnModel().getColumn(i);
									tc.setCellRenderer(new TableFeaturesCellRenderer());			
								}

								//si hay errores se actualiza la pestaña de errores y si no los hay se habilita el boton de importar
								resultTable.setEnabled(false);
								origTable.setEnabled(false);
								//markedCells = false;	
								
								
								if (rows.size()!=0)
								{
									jTabbedPane.setEnabledAt(1, true);
									jTabbedPane.setSelectedIndex(1);	
									getJTransferButton().setEnabled(false);
									getJExportButton().setEnabled(false);
								}			
								else
								{
									jTabbedPane.setEnabledAt(1, false);
									jTabbedPane.setSelectedIndex(0);
									getJTransferButton().setEnabled(true);
									getJExportButton().setEnabled(true);
									JOptionPane.showMessageDialog(ImporterEditorPanel.this, 
											I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.mensaje.validacion.exito"));
								}	
							}		
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		GUIUtil.centreOnScreen(progressDialog);
		progressDialog.setVisible(true);

		if (context !=null && context.getActiveInternalFrame() !=null)
			context.getActiveInternalFrame().setVisible(true);		
		else if (context !=null && context.getActiveTaskComponent() != null)
			context.getActiveTaskComponent().setVisible(true);

	}
	

	/**
	 * This method initializes jTransferButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJTransferButton() {
		if (jTransferButton == null) {
			jTransferButton = new JButton();
			jTransferButton.setPreferredSize(new Dimension(130, 25));
			jTransferButton.setText(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.boton.transferir"));  
			jTransferButton.setEnabled(false);
			jTransferButton.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {    

					int answer = JOptionPane.showConfirmDialog(ImporterEditorPanel.this, 
							I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.mensaje.importacion.inicio"));
					if (answer == JOptionPane.YES_OPTION) {
						importGeopistaFeatures();
					} 					
				}
			});
		}
		return jTransferButton;
	}

	protected void importGeopistaFeatures() {		

		final TaskMonitorDialog progressDialog = new TaskMonitorDialog((Frame)SwingUtilities.getAncestorOfClass(Frame.class, this), null, true);

		progressDialog.setTitle(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.progreso.importando.titulo")); 
		progressDialog.report(I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.progreso.importando.mensaje")); 
		progressDialog.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{
				// Wait for the dialog to appear before starting the
				// task. Otherwise
				// the task might possibly finish before the dialog
				// appeared and the
				// dialog would never close. [Jon Aquino]
				new Thread(new Runnable()
				{
					public void run()
					{
						try
						{							
							
							//Funciona y refresca la capa, pero no muestra el contador de features insertadas
							
							//progressDialog.report("Importando...");

							
							GeopistaLayer targetLayer = (GeopistaLayer) getFromBlackboard(SELECTEDTARGETLAYER);
							//GeopistaServerDataSource geopistaServerDataSource = 
							//	(GeopistaServerDataSource) targetLayer.getDataSourceQuery().getDataSource();
							//Map driverProperties = geopistaServerDataSource.getProperties();
							//Object lastResfreshValue = driverProperties.get(GeopistaConnection.REFRESH_INSERT_FEATURES);
							//System.out.println(lastResfreshValue);
							
							//driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES, new Boolean(true));
							targetLayer.getFeatureCollectionWrapper().addAll(toImportGeopistaFeatures);
							//geopistaServerDataSource.getConnection().executeUpdate(targetLayer.getDataSourceQuery().getQuery(), 
							//		targetLayer.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);

							toImportGeopistaFeatures.clear();
							JOptionPane.showMessageDialog(ImporterEditorPanel.this, 
									I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.progreso.importando.exito"));

														
							/*
							GeopistaLayer targetLayer = (GeopistaLayer) getFromBlackboard(SELECTEDTARGETLAYER);
							GeopistaServerDataSource geopistaServerDataSource = 
								(GeopistaServerDataSource) targetLayer.getDataSourceQuery().getDataSource();
							Map driverProperties = geopistaServerDataSource.getProperties();
							Object lastResfreshValue = driverProperties.get(GeopistaConnection.REFRESH_INSERT_FEATURES);
							//System.out.println(lastResfreshValue);
							
							driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES, new Boolean(true));
							//targetLayer.getFeatureCollectionWrapper().addAll(toImportGeopistaFeatures);
							geopistaServerDataSource.getConnection().executeUpdate(targetLayer.getDataSourceQuery().getQuery(), 
									targetLayer.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);

							toImportGeopistaFeatures.clear();
							JOptionPane.showMessageDialog(ImporterEditorPanel.this, 
									I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.progreso.importando.exito"));

							*/						
							/*
							GeopistaLayer targetLayer = (GeopistaLayer) getFromBlackboard(SELECTEDTARGETLAYER);
							GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) targetLayer
							.getDataSourceQuery().getDataSource();
							Map driverProperties = geopistaServerDataSource.getProperties();
							Object lastResfreshValue = driverProperties.get(GeopistaConnection.REFRESH_INSERT_FEATURES);
							try
							{
								driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,new Boolean(false));
								geopistaServerDataSource.getConnection().executeUpdate(targetLayer.getDataSourceQuery().getQuery(),
										targetLayer.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
							}
							catch (Exception e){
								e.printStackTrace();
							}
							finally
							{
								if(lastResfreshValue!=null)
								{
									driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,lastResfreshValue);
								}
								else
								{
									driverProperties.remove(GeopistaConnection.REFRESH_INSERT_FEATURES);
								}
							}							
							*/
							/*
							GeopistaLayer targetLayer = (GeopistaLayer) getFromBlackboard(SELECTEDTARGETLAYER);
							GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource) targetLayer
							.getDataSourceQuery().getDataSource();
							Map driverProperties = geopistaServerDataSource.getProperties();
							Object lastResfreshValue = driverProperties.get(GeopistaConnection.REFRESH_INSERT_FEATURES);
							
							 try
                             {
                                 driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,new Boolean(true));
                                 geopistaServerDataSource.getConnection()
                                         .executeUpdate(targetLayer.getDataSourceQuery().getQuery(),
                                        		 targetLayer.getFeatureCollectionWrapper().getUltimateWrappee(), progressDialog);
                             }
							 catch(Exception e)
							 {
								 e.printStackTrace();
							 }
							 finally
							 
                             {
                                 if(lastResfreshValue!=null)
                                 {
                                     driverProperties.put(GeopistaConnection.REFRESH_INSERT_FEATURES,lastResfreshValue);
                                 }
                                 else
                                 {
                                     driverProperties.remove(GeopistaConnection.REFRESH_INSERT_FEATURES);
                                 }
                             }
							*/
							/*
							progressDialog.report(I18N.get("ImporterPlugIn", "ImporterPlugIn.ImporterEditorPanel.progreso.importando.exito"));

							GeopistaLayer targetLayer = (GeopistaLayer)getFromBlackboard(ImporterEditorPanel.SELECTEDTARGETLAYER);
							GeopistaServerDataSource geopistaServerDataSource = (GeopistaServerDataSource)targetLayer.getDataSourceQuery().getDataSource();
							Map driverProperties = geopistaServerDataSource.getProperties();
							Object lastResfreshValue = driverProperties.get("RefreshInsertFeatures");
							driverProperties.put("RefreshInsertFeatures", new Boolean(true));
							targetLayer.getFeatureCollectionWrapper().addAll(toImportGeopistaFeatures);
							
							toImportGeopistaFeatures.clear();
							JOptionPane.showMessageDialog(ImporterEditorPanel.this, 
									I18N.get("ImporterPlugIn", "ImporterPlugIn.ImporterEditorPanel.progreso.importando.exito"));
							progressDialog.setVisible(false);
*/
							

						}
						catch(Exception e)
						{
							ErrorDialog.show(ImporterEditorPanel.this, I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.progreso.importando.error.titulo"), 
									I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.progreso.importando.error.mensaje"),
									StringUtil.stackTrace(e));
						}
						finally
						{
							progressDialog.setVisible(false);
						}
					}
				}).start();
			}
		});
		GUIUtil.centreOnScreen(progressDialog);
		progressDialog.setVisible(true);

		if (context !=null && context.getActiveInternalFrame() !=null)
			context.getActiveInternalFrame().setVisible(true);		
		else if (context !=null && context.getActiveTaskComponent() != null)
			context.getActiveTaskComponent().setVisible(true);

	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBorder(BorderFactory.createTitledBorder(null, I18N.get("ImporterPlugIn","ImporterPlugIn.ImporterEditorPanel.camposdestino.titulo"), 
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));   
			jScrollPane.setPreferredSize(new Dimension(400, 400));   
			jScrollPane.setMinimumSize(jScrollPane.getPreferredSize());
			jScrollPane.setMaximumSize(jScrollPane.getPreferredSize());			

			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			jScrollPane.setViewportView(getMapPanel());   
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);   
		}
		return jScrollPane;
	}

	/**
	 * This method initializes reportTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	public JTable getReportTable() {
		if (reportTable == null) {
			reportTable = new JTable();
			reportTable.setRowSelectionAllowed(false);
			reportTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			reportTable.setDragEnabled(false);
		}
		return reportTable;
	}


	/**
	 * This method initializes resultTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	public JTable getResultTable() {
		if (resultTable == null) {
			resultTable = new JTable();
			resultTable.setRowSelectionAllowed(false);
			resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			resultTable.setDragEnabled(false);
			resultTable.setVisible(true);
		}
		return resultTable;
	}



	/**
	 * This method initializes origTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	public JTable getOrigTable() {
		if (origTable == null) {
			origTable = new JTable();
			origTable.setRowSelectionAllowed(false);
			origTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			origTable.setDragEnabled(false);			
			origTable.setVisible(true);			
		}
		return origTable;
	}

	private Object getFromBlackboard(String key)
	{	
		bk = context.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();

		return bk.get(key);
	}	

	private void calculateRows()
	{		
		
		String nombreAtrib = (String)getFromBlackboard(ATTRIBUTE_NAME);
		Layer srcLayer = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
		Layer tLayer = (Layer) getFromBlackboard(SELECTEDTARGETLAYER);
		Collection features = getToImportFeatures(srcLayer);
		//featureExpressionPanel.getExpParser().setFeatures(features);
		//Vector values = featureExpressionPanel.getExpParser()
		//.getValuesAsObjects();
		Vector<String> names = new Vector<String>();
		FeatureSchema t_sch = (FeatureSchema)((GeopistaLayer)tLayer).getFeatureCollectionWrapper().getFeatureSchema();

		int index = 0;		
		for (int i=0; i< ((DefaultTableModel)reportTable.getModel()).getColumnCount(); i++)
		{	
			String name = ((DefaultTableModel)reportTable.getModel()).getColumnName(i);
			names.add(name);

			//Recupera la posición donde se encuentra el atributo a igualar
			if (name.equals(nombreAtrib))
			{
				index = i;				
			}
		}

		Vector<Vector> rows = new Vector<Vector>();
		Iterator feat = features.iterator();		

		FeatureSchema sch = (FeatureSchema)((GeopistaLayer)srcLayer).getFeatureCollectionWrapper().getFeatureSchema();

		String nombreOrigen = ((String)getFromBlackboard(FORMULA)).replaceAll(" ","_");

		htTargetAttributes.put(new Integer(index), nombreOrigen);


		// Actualiza las filas de la tabla			

		while (feat.hasNext())
		{
			Vector row = new Vector();
			Feature feature = (Feature) feat.next();
			for (int i = 0; i < t_sch.getAttributeCount(); i++)
			{
				Integer iTargetPosition = new Integer(i);  

				if (htTargetAttributes.get(iTargetPosition)!=null)
				{
					try{
						int indexForm = sch.getAttributeIndex((String)htTargetAttributes.get(iTargetPosition));
						row.add(feature.getAttribute(indexForm));
					}
					catch(IllegalArgumentException e)
					{
						System.out.println("** ILEGAL ** " + e.getMessage());
						row.add(null);
					}
				}
				else
				{
					row.add(null);
				}					
			}
			rows.add(row);
		}
		reportTable.setModel(new DefaultTableModel(rows, names));			
	}

	private Collection getAffectedFeatures(Layer layer)
	{
		if (layer==null)
		{
			return  new Vector();
		}
		else
		{
			return layer.getFeatureCollectionWrapper().getFeatures();
		}
	}

	private Collection getToImportFeatures(Layer layer)
	{
		return getToImportFeatures(layer, false);
	}


	private Collection getToImportFeatures(Layer layer, boolean reducedList)
	{
		Collection c = null;

		if (layer!=null && layer.getFeatureCollectionWrapper()!=null)
		{
			List lst = layer.getFeatureCollectionWrapper().getFeatures();
			if (reducedList && lst.size()> ConstantesImporter.NUMTESTFEATURES)
			{				
				c = lst.subList(0, ConstantesImporter.NUMTESTFEATURES);
			}	
			else
			{
				c=lst;
			}
		}

		return c;
	}


	public void updateTable()
	{		
		// obtiene los campos de las Features y sus nuevos valores
		String attName = (String) getFromBlackboard(ATTRIBUTE_ID_ON_TARGET);
		Layer layer = (Layer)getFromBlackboard(SELECTEDTARGETLAYER);
		Layer srcLayer = (Layer) getFromBlackboard(SELECTEDSOURCELAYER);
		Collection features = getToImportFeatures(srcLayer, true);

		if (features!=null && layer.getFeatureCollectionWrapper()!=null)
		{
			Vector names = new Vector();
			Vector rows = new Vector();
			Iterator feat = features.iterator();				
			FeatureSchema sch = layer.getFeatureCollectionWrapper().getFeatureSchema();

			//Busca el índice del atributo id y del idMunicipio:
			idIndex = -1;
			idMunIndex = -1;
			Iterator <Attribute> it = ((GeopistaSchema)sch).getAttributes().iterator();
			int i=0;
			while (it.hasNext())
			{
				Attribute at = it.next();
				if (at.getColumn().getName().equalsIgnoreCase("ID"))
				{
					idIndex = i;				
				}			
				else if (at.getColumn().getName().equalsIgnoreCase("ID_MUNICIPIO")
						|| at.getColumn().getName().equalsIgnoreCase("IDMUNICIPIO") )
				{	
					idMunIndex = i;
				}
				i++;	
			}		

			// construye los nombres		
			for (int j = 0; j < sch.getAttributeCount(); j++)
			{
				geometryIndex = sch.getGeometryIndex();
				//No muestra en la lista de resultados ni la geometría ni el identificador 
				//ni el municipio (se cogerá ?M porque la importación se hará a través del admcar)
				if (j != geometryIndex 
						&& j!= idIndex
						&& j!= idMunIndex
				)			
					names.add(sch.getAttributeName(j));			
			}

			reportTable.setModel(new DefaultTableModel(rows, names));
		}

	}	

	ActionEventSupport adapter=new ActionEventSupport();

	public void actionPerformed(ActionEvent e) {
		adapter.forwardActionEvent(e);			
	}

	public void addActionListener(ActionListener listener) {
		adapter.addActionListener(listener);
	}

	public void removeActionListener(ActionListener listener) {
		adapter.removeActionListener(listener);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());			
	}	

	private void putToBlackboard(String key, Object value)
	{
		Blackboard bk;
		if (context!=null)
			bk=context.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
			bk=this.bk;

		bk.put(key,value);
	}
}  
