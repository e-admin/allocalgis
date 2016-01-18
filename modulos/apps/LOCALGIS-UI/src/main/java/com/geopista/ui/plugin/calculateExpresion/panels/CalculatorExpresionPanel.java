/**
 * CalculatorExpresionPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.calculateExpresion.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.geopista.app.AppContext;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.ui.dialogs.DistanceLinkingPanel;
import com.geopista.ui.plugin.calculateExpresion.beans.TestCalculatorExpressionBean;
import com.geopista.ui.plugin.calculateExpresion.ws.client.ClienteWSCalculateExpression;
import com.geopista.util.ApplicationContext;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
/**
 * CalculatorPanel
 * Clase que implementa una calculadora de expresiones matemáticas que permite
 * establecer el valor de los diferentes atributos de las features seleccionadas
 * o de todas las features de una capa.
 */
public class CalculatorExpresionPanel extends JPanel
{
	/**
	 * Logger for this class
	 */
	private static final Log	logger	= LogFactory.getLog(CalculatorExpresionPanel.class);

	ApplicationContext appContext=AppContext.getApplicationContext();
	 
	private static final String ATTRIBUTE_ID_ON_TARGET_DESTINO = DistanceLinkingPanel.class.getName()+"ATTRIBUTE_ID_ON_TARGET_DESTINO";
	private static final String ATTRIBUTE_RESULT = DistanceLinkingPanel.class.getName()+"ATTRIBUTE_RESULT";
	private static final String DESTINO = DistanceLinkingPanel.class.getName()+"DESTINO";
	
	private PlugInContext localContext;
	private FeatureExpressionPanel featureExpressionPanel;
	private JButton btnSetField;
	private JButton btnValidate;
	private JButton btnSaveExpresion;
	private JButton btnLoadExpresion;
	private JTable reportTable = null;

	private JScrollPane jScrollPane1 = null;

	private DefaultTableModel defaultTableModel = null;   //  @jve:decl-index=0:
	
	private JLabel destinoLabel = null;
	private JPanel destinoPanel = null;
	
	private Document doc;
	protected static File lastDirectory;
	private String fileText="";
	
	private ClienteWSCalculateExpression cwsce = null;
	
	private String textExpressionValue = "";
	private String textWhereValue = "";
	private String textFromValue = "";
	private String textDestinoValue = "";
	
	/**
	 * This is the default constructor
	 */
	public CalculatorExpresionPanel() {
		super();
		initialize();
		putToBlackboard(ATTRIBUTE_RESULT, new ArrayList());
	    updateDestinoTable();
	   // updateOrgienTable();
	}
	public CalculatorExpresionPanel(PlugInContext context)
	{
		super();
		this.localContext=context;
	    initialize();
	    putToBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO, null);
	    putToBlackboard(ATTRIBUTE_RESULT, new ArrayList());
	    updateDestinoTable(); 
	  //  updateOrgienTable();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		
		cwsce = new ClienteWSCalculateExpression();
		
		destinoLabel = new JLabel();
		destinoLabel.setText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.destino"));
		
		java.awt.GridBagConstraints gridBagConstraints17 = new GridBagConstraints();

		java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints14 = new GridBagConstraints();

		java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		
		java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		
		this.setLayout(new GridBagLayout());
		this.setSize(300,200);
		gridBagConstraints16.gridx = 0;
		gridBagConstraints16.gridy = 6;
		gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 4;
		gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints11.gridwidth = 2;
		gridBagConstraints14.gridx = 1;
		gridBagConstraints14.gridy = 0;
		gridBagConstraints14.weightx = 1.0;
		gridBagConstraints14.weighty = 1.0;
		gridBagConstraints14.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints14.gridheight = 2;
		
		gridBagConstraints14.gridx = 1;
		gridBagConstraints14.gridy = 1;
		gridBagConstraints14.weightx = 1.0;
		gridBagConstraints14.weighty = 1.0;
		gridBagConstraints14.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints14.gridheight = 1;
		
		getFeatureExpressionPanel().setLabelText("");
		
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridy = 0;
		gridBagConstraints3.weightx = 0.01;
		gridBagConstraints3.weighty = 0.01;
		gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints3.gridheight = 1;
		gridBagConstraints3.gridwidth = 2;
		

		this.add(getDestinoPanel(),gridBagConstraints3);
		
		this.add(getFeatureExpressionPanel(), gridBagConstraints11);
		this.add(getJScrollPane1(), gridBagConstraints14);
		this.add(getJPanel(), gridBagConstraints16);

	    this.setName(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn"));
	    gridBagConstraints17.gridx = 0;
	    gridBagConstraints17.gridy = 1;
	    gridBagConstraints17.weighty = 1.0;
	    gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
	  
	    this.add(getSelectLayerFieldPanel(), gridBagConstraints17);

	    
	    
	    getFeatureExpressionPanel().getExpresionTestTextArea().addCaretListener(new CaretListener()
		{
			public void caretUpdate(CaretEvent evt)
			{
				if((textExpressionValue.equals(getFeatureExpressionPanel().getExpresionTestTextArea().getText()) ||
						textFromValue.equals(getFeatureExpressionPanel().getFromTextArea().getText()) ||
						textWhereValue.equals(getFeatureExpressionPanel().getWhereTextArea().getText()) ||
						textDestinoValue.equals(getFeatureExpressionPanel().getWhereTextArea().getText())) &&
						(!textFromValue.equals("") ||
						!textDestinoValue.equals(""))){
					getBtnSetField().setEnabled(true);
				}
				else{
					getBtnSetField().setEnabled(false);
				}
			}
		});
    
	}
	
	public static Blackboard bk=new Blackboard();// for testing only
	//private JButton closeButton = null;
	private JPanel jPanel = null;

	protected boolean showOnlySelected;

	
	private SelectLayerFieldDestinoPanel selectLayerFieldPanel = null;

	private Object getFromBlackboard(String key)
	{
		Blackboard bk;
		if (localContext!=null)
		bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
		bk=this.bk;
		
	return bk.get(key);
		
	}
	private void putToBlackboard(String key, Object value)
	{
		Blackboard bk;
		if (localContext!=null)
			bk=localContext.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard();
		else
			bk=this.bk;
		
		bk.put(key,value);
		
	}
	/**
	 * This method initializes targetFieldScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	
	/**
	 * This method initializes featureExpressionPanel	
	 * 	
	 * @return com.geopista.ui.FeatureExpressionPanel	
	 */    
	private FeatureExpressionPanel getFeatureExpressionPanel() {
		if (featureExpressionPanel == null) {
			featureExpressionPanel = new FeatureExpressionPanel();
			featureExpressionPanel.setPreferredSize(new java.awt.Dimension(100,200));
		}
    
		return featureExpressionPanel;
	}
	/**
	 * 
	 */
	protected void updateDestinoTable()
	{
		
		
		// obtiene los campos de las Features y sus nuevos valores
		String attName = (String) getFromBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO);
		ArrayList lstResult = (ArrayList)getFromBlackboard(ATTRIBUTE_RESULT);
		
		String nombre = "";

		Vector names = new Vector();
		Vector rows = new Vector();
		
		if (attName == null)
		{
			nombre=(appContext.getI18nString("lblNuevoValor"));
		}
		else
		{
			nombre=(attName + " " + appContext.getI18nString("lblNuevoValor"));
		}
		names.add(nombre);
		
		if(lstResult != null && !lstResult.isEmpty()){
			if((String) getFromBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO) != null ||
					!((String) getFromBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO)).equals("")){
				
				names.add((String) getFromBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO));
			}
		}
		
		if(lstResult != null && !lstResult.isEmpty()){
			for(int i=0; i<lstResult.size();i++){
			
				TestCalculatorExpressionBean tceb = (TestCalculatorExpressionBean)lstResult.get(i);
				
				Vector row = new Vector();
				row.add(tceb.getResultado());
				row.add(tceb.getDestino());
				rows.add(row);
			}
		}
		
		reportTable.setModel(new DefaultTableModel(rows, names));
		reportTable.getColumn(nombre).setPreferredWidth(250);
		if(lstResult != null && !lstResult.isEmpty()){
			reportTable.getColumn((String) getFromBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO)).setPreferredWidth(250);
		}
	}
	

	/**
	 * @return
	 */
	private Collection getAffectedFeatures(Layer layer)
	{
		if (showOnlySelected && localContext!=null)
		{
			return localContext.getActiveTaskComponent().getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems(layer);
		}
		else
			if (layer==null)
				return  new Vector();
			else
			return layer.getFeatureCollectionWrapper().getFeatures();
	}
	
	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnSetField() {
		if (btnSetField == null) {
			btnSetField = new JButton();
			btnSetField.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/smalldown.gif")));
			btnSetField.setEnabled(false);
			btnSetField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			btnSetField.setToolTipText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.transferir"));
			btnSetField.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					final String destino = (String)getFromBlackboard(DESTINO);
					final String formula = featureExpressionPanel.getExpresionTestTextArea().getText();
					
					final String from = featureExpressionPanel.getFromTextArea().getText();
					final String where = featureExpressionPanel.getWhereTextArea().getText();
					
							
					final TaskMonitorDialog progressDialog= new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
					progressDialog.setTitle(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.exportar.title"));
					progressDialog.addComponentListener(new ComponentAdapter()
					{
						public void componentShown(ComponentEvent e)
						{
							new Thread(new Runnable()
							{
								public void run()
								{
									
									try
									{      
										progressDialog.report(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.exportar.datos"));
									
										cwsce.updateDataExpression(destino, formula, from, where);
										updateDestinoTable();
										getBtnSetField().setEnabled(false);
										
										
										ArrayList lstResultado = cwsce.testDataExpression(destino, formula, from, where);
										putToBlackboard(ATTRIBUTE_RESULT, lstResultado);
										updateDestinoTable();
									}
									catch(Exception e)
									{
										logger.error("Error ", e);
										ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
										return;
									}
									finally
									{
										progressDialog.setVisible(false);                                
										progressDialog.dispose();								
						
									}
								}
							}).start();
						}
					});
					GUIUtil.centreOnWindow(progressDialog);
					progressDialog.setVisible(true);

					show();

				}
			});
		}
		return btnSetField;
	}
	
	
	public JButton getBtnValidate() {
		if (btnValidate == null) {
			btnValidate = new JButton();
			btnValidate.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/btn_test.gif")));
			btnValidate.setEnabled(true);
			btnValidate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			btnValidate.setToolTipText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.validar"));
			btnValidate.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					
					final String destino = (String)getFromBlackboard(DESTINO);
					final String formula = featureExpressionPanel.getExpresionTestTextArea().getText();
					
					final String from = featureExpressionPanel.getFromTextArea().getText();
					final String where = featureExpressionPanel.getWhereTextArea().getText();
					
							
					final TaskMonitorDialog progressDialog= new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
					progressDialog.setTitle(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.validate.expression"));
					progressDialog.addComponentListener(new ComponentAdapter()
					{
						public void componentShown(ComponentEvent e)
						{
							new Thread(new Runnable()
							{
								public void run()
								{
									
									try
									{      
										progressDialog.report(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.validando.expression"));
									
										if(destino== null || destino.equals("") ){
											featureExpressionPanel.getLblestado().setText(
														appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.expresion.nook.destino"));
											featureExpressionPanel.getLblestado().setForeground(Color.red);
										}
										else if( from == null || from.equals("")){
											featureExpressionPanel.getLblestado().setText(
														appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.expresion.nook.from"));
											featureExpressionPanel.getLblestado().setForeground(Color.red);
										}
										else{
											String message = cwsce.validateExpression(destino, formula, from, where);
											ArrayList lstResultado = null;
										
											if(message == null ){
												progressDialog.report(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.obteniendoDatos"));
												lstResultado = cwsce.testDataExpression(destino, formula, from, where);
												getBtnSetField().setEnabled(true);
												textExpressionValue = formula;
											}
											else {
												getBtnSetField().setEnabled(false);
												featureExpressionPanel.getLblestado().setText("");
											}
											featureExpressionPanel.getLblestado().setText(message);
											
											putToBlackboard(ATTRIBUTE_RESULT, lstResultado);
											updateDestinoTable();
										}
										
										
									}
									catch(Exception e)
									{
										logger.error("Error ", e);
										ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), "ERROR", "ERROR", StringUtil.stackTrace(e));
										return;
									}
									finally
									{
										progressDialog.setVisible(false);                                
										progressDialog.dispose();								
						
									}
								}
							}).start();
						}
					});
					GUIUtil.centreOnWindow(progressDialog);
					progressDialog.setVisible(true);

					show();
				
					
					
				}
			});
		}
		return btnValidate;
	}
	
	public void setBtnTest(JButton btnTest) {
		this.btnValidate = btnTest;
	}

	
	/**
	 * This method initializes reportTable	
	 * 	
	 * @return javax.swing.JTable	
	 */    
	private JTable getReportTable() {
		if (reportTable == null) {
			reportTable = new JTable();
			reportTable.setRowSelectionAllowed(false);
			reportTable.setAutoResizeMode(0);
			
		}
		return reportTable;
	}

	

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getReportTable());
			jScrollPane1.setHorizontalScrollBarPolicy(jScrollPane1.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollPane1.setPreferredSize(new java.awt.Dimension(800,300));
		}
		return jScrollPane1;
	}
	
	
	
	/**
	 * This method initializes defaultTableModel	
	 * 	
	 * @return javax.swing.table.DefaultTableModel	
	 */    
	private DefaultTableModel getDefaultTableModel() {
		if (defaultTableModel == null) {
			defaultTableModel = new DefaultTableModel();
			defaultTableModel.setColumnCount(1);
		}
		return defaultTableModel;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getBtnSetField(), null);
			jPanel.add(getBtnValidate(), null);
			jPanel.add(getBtnSaveExpresion(), null);
			jPanel.add(getBtnLoadExpresion(), null);

		}
		return jPanel;
	}

   	
	/**
	 * This method initializes selectLayerFieldPanel	
	 * 	
	 * @return 
	 */    
	private JPanel getSelectLayerFieldPanel() {
		if (selectLayerFieldPanel == null) {
			selectLayerFieldPanel = new SelectLayerFieldDestinoPanel(localContext);
			selectLayerFieldPanel.addPropertyChangeListener(
					new PropertyChangeListener()
					{

					public void propertyChange(PropertyChangeEvent e)
							{ 
								if (logger.isDebugEnabled())
									{
									logger.debug("propertyChange(PropertyChangeEvent)"+ e.getPropertyName());
									}
								 if (e.getPropertyName().equals("FIELDVALUE"))
								{

									String attName=(String) e.getNewValue();
									putToBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO, attName);
									putToBlackboard(DESTINO, attName);
									updateDestinoTable();
									getFeatureExpressionPanel().setLabelText(attName);
									getBtnValidate().setEnabled(true);
								}
								else if (e.getPropertyName().equals("FIELDEXPRESIONVALUE"))
								{
									String attName=(String) e.getNewValue();
									featureExpressionPanel.pasteText(attName);
									featureExpressionPanel.getExpresionTestTextArea().requestFocus();
									getBtnValidate().setEnabled(true);
								}		
						}	
						
					});
			selectLayerFieldPanel.addActionListener( new ActionListener() { 
								public void actionPerformed(ActionEvent e)
				{
					SelectLayerFieldDestinoPanel pan=(SelectLayerFieldDestinoPanel) e.getSource();
					featureExpressionPanel.pasteText(pan.getSelectedFieldName());
					featureExpressionPanel.requestFocus();
					
				}
			});
		
		}
		return selectLayerFieldPanel;
	}
	
	public JPanel getDestinoPanel() {
		if(destinoPanel == null){
			destinoPanel = new JPanel();
			destinoPanel.add(destinoLabel);
			
		}
		return destinoPanel;
	}
	public void setDestinoPanel(JPanel destinoPanel) {
		this.destinoPanel = destinoPanel;
	}
	
	public JButton getBtnSaveExpresion() {
		if(btnSaveExpresion == null){
			btnSaveExpresion = new JButton();
			btnSaveExpresion.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/guardar.gif")));
			btnSaveExpresion.setEnabled(true);
			btnSaveExpresion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			btnSaveExpresion.setToolTipText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.guardarExpresion"));
			btnSaveExpresion.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					saveFile();
				}
			}); 
			
		}
		return btnSaveExpresion;
	}
	
	public void setBtnSaveExpresion(JButton btnSaveExpresion) {
		this.btnSaveExpresion = btnSaveExpresion;
	}
	public JButton getBtnLoadExpresion() {
		if(btnLoadExpresion == null){
			btnLoadExpresion = new JButton();
			btnLoadExpresion.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/abrir.gif")));
			btnLoadExpresion.setEnabled(true);
			btnLoadExpresion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			btnLoadExpresion.setToolTipText(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.cargarExpresion"));
			btnLoadExpresion.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					loadFile();
				}
			}); 
			
		}
		return btnLoadExpresion;
	}
	public void setBtnOpenExpresion(JButton btnOpenExpresion) {
		this.btnLoadExpresion = btnOpenExpresion;
	}
	
	private void loadFile(){
		try {
			GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
			filter.addExtension("xml");
			filter.setDescription(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.ficheroCalculadoraExpresiones"));
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setApproveButtonText("Abrir");
			if (lastDirectory != null) {
				File currentDirectory = lastDirectory;
				fc.setCurrentDirectory(currentDirectory);
			}
			fc.setName(filter.getDescription());
			if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
				
			StringBuffer sb = new StringBuffer();
			  File archivo = null;
		      FileReader fr = null;
		      BufferedReader br = null;

		      try {
		         // Apertura del fichero y creacion de BufferedReader para poder
		         // hacer una lectura comoda (disponer del metodo readLine()).
		         archivo = fc.getSelectedFile();

		         fr = new FileReader (archivo);
		         br = new BufferedReader(fr);

		         // Lectura del fichero
		         String linea;
		         while((linea=br.readLine())!=null)
		           sb.append(linea);
		      }
		      catch(Exception e){
		         e.printStackTrace();
		      }finally{
		         // En el finally cerramos el fichero, para asegurarnos
		         // que se cierra tanto si todo va bien como si salta 
		         // una excepcion.
		         try{                    
		            if( null != fr ){   
		               fr.close();     
		            }                  
		         }catch (Exception e2){ 
		            e2.printStackTrace();
		         }
		      }
			

			Boolean valido = cwsce.validarFicheroExpresion(sb.toString());
			
			
			if (!valido){
				JOptionPane optionOk = new JOptionPane(
						appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.fichero.load.nook")
								+ ":\n ",JOptionPane.INFORMATION_MESSAGE);
				JDialog dialogOk = optionOk.createDialog(this.getParent(),
						appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.fichero.load.title"));
				dialogOk.setVisible(true);
			}
			else{
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );
				Document documento = null;
				
				try
				{
				   DocumentBuilder builder = factory.newDocumentBuilder();
				   documento = builder.parse(archivo);

				   putToBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO, "");
				   putToBlackboard(DESTINO,"");
				   featureExpressionPanel.setLabelText("");
				   featureExpressionPanel.getExpresionTestTextArea().setText("");
				   featureExpressionPanel.getFromTextArea().setText("");
				   featureExpressionPanel.getWhereTextArea().setText("");
				   
				// Devuelve nodos hijos de un nodo dado
				   NodeList listaNodosHijos = documento.getChildNodes();
				   for (int i=0; i<listaNodosHijos.getLength(); i++){
					   Node childNode =  listaNodosHijos.item(i);
				      if (childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getNodeName().equals("calculateexpresion")){
			    		 NodeList listaDatosEstado = childNode.getChildNodes();
			    		 for (int h = 0; h < listaDatosEstado.getLength(); h++) {
			    			 Node childNodeEstado = listaDatosEstado.item(h);
			    			 if (childNodeEstado.getNodeType() == Node.ELEMENT_NODE && childNodeEstado.getNodeName().equals("destino")){
			    				 if(childNodeEstado.getFirstChild() != null){
			    					 putToBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO, (String)childNodeEstado.getFirstChild().getNodeValue());
			    					 putToBlackboard(DESTINO,(String)childNodeEstado.getFirstChild().getNodeValue());
			    					 featureExpressionPanel.setLabelText(childNodeEstado.getFirstChild().getNodeValue());
			    				 }
			    				 else{
			    					 putToBlackboard(ATTRIBUTE_ID_ON_TARGET_DESTINO, "");
			    					 putToBlackboard(DESTINO,"");
			    					 featureExpressionPanel.setLabelText("");
			    				 }
					    	 }
			    			 else if (childNodeEstado.getNodeType() == Node.ELEMENT_NODE && childNodeEstado.getNodeName().equals("expresion")){
			    				 if(childNodeEstado.getFirstChild() != null){
			    					 featureExpressionPanel.getExpresionTestTextArea().setText((String)childNodeEstado.getFirstChild().getNodeValue());
			    				 }
			    				 else{
			    					 featureExpressionPanel.getExpresionTestTextArea().setText("");
			    				 }
					    	 }
			    			 else if (childNodeEstado.getNodeType() == Node.ELEMENT_NODE && childNodeEstado.getNodeName().equals("de")){
			    				 if(childNodeEstado.getFirstChild() != null){
			    			 		 featureExpressionPanel.getFromTextArea().setText((String)childNodeEstado.getFirstChild().getNodeValue());
			    				 }
			    				 else{
			    					 featureExpressionPanel.getFromTextArea().setText("");
			    				 }
					    	 }
			    			 else if (childNodeEstado.getNodeType() == Node.ELEMENT_NODE && childNodeEstado.getNodeName().equals("condicion")){
			    				 if(childNodeEstado.getFirstChild() != null){
			    					 featureExpressionPanel.getWhereTextArea().setText((String)childNodeEstado.getFirstChild().getNodeValue());
			    				 }
			    				 else{
			    					 featureExpressionPanel.getWhereTextArea().setText("");
			    				 }
					    	 }
			    		 }
				      }
				   }
				   JOptionPane optionOk = new JOptionPane(
							appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.fichero.load.ok")
									+ ":\n ",JOptionPane.INFORMATION_MESSAGE);
					JDialog dialogOk = optionOk.createDialog(this.getParent(),
							appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.fichero.load.title"));
					dialogOk.setVisible(true);
				}
				catch (Exception spe)
				{
				   // Algún tipo de error: fichero no accesible, formato de XML incorrecto, etc.
				}
				
			}
			getBtnSetField().setEnabled(false);
			getBtnValidate().setEnabled(true);
			
		} catch (Exception ex) {
			logger.error(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.save.error"));
			ErrorDialog.show(this, "ERROR",
					appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.save.error"),
					StringUtil.stackTrace(ex));
		} finally {
			try {

			} catch (Exception ex) {
			}
		}
		
	}
	
	private void saveFile(){
		Writer output = null;

		try {
			
			GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
			filter.addExtension("xml");
			filter.setDescription(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.ficheroCalculadoraExpresiones"));
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			if (lastDirectory != null) {
				File currentDirectory = lastDirectory;
				fc.setCurrentDirectory(currentDirectory);
			}
			fc.setName(filter.getDescription());
			if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
			// Si pulsa guardar-->

			lastDirectory = fc.getCurrentDirectory();
			String contenidoString = "";
			contenidoString = saveExpresion();
	
			 String nombreFichero= fc.getSelectedFile().getName();
	         /** quitamos la extension que haya puesto el usuario */
	         int index= nombreFichero.indexOf(".");
	         if (index != -1){
	        	 nombreFichero= nombreFichero.substring(0, index);
	         }
	         /** nos quedamos sólo con el path, sin el nombre del fichero */
	         String pathDestino= fc.getSelectedFile().getPath();
	         index= pathDestino.indexOf(nombreFichero);
	         if (index != -1){
	             pathDestino= pathDestino.substring(0, index);
	         }
	         /** al nombre del fichero le añadimos la extension correspondiente al formato del fichero xml*/
	         nombreFichero+=".xml";
	       /** añadimos al path, el nombre del fichero con extension */
	         pathDestino+= nombreFichero;
	         
			output = new java.io.BufferedWriter(new java.io.FileWriter(pathDestino,false));//TODO: Aqui se añade la extension fija ".xml"
			output.write(contenidoString);
			
			JOptionPane optionOk = new JOptionPane(
					appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.fichero.saved.ok")
							+ ":\n " + pathDestino,
					JOptionPane.INFORMATION_MESSAGE);
			JDialog dialogOk = optionOk.createDialog(this.getParent(),
					appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.save.fichero.title"));
			dialogOk.setVisible(true);
			
		} catch (Exception ex) {
			logger.error(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.save.error"));
			ErrorDialog.show(this, "ERROR",
					appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.save.error"),
					StringUtil.stackTrace(ex));
		} finally {
			try {
				output.close();
			} catch (Exception ex) {
			}
		}
	}
	
	private String saveExpresion(){
	
		initProgressDialog(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.save.runDialogo.title"));

		return getContentOfDoc();
		
	}
	

	protected void setContentOfDoc(String fichero) {
		this.fileText = fichero;

	}

	protected String getContentOfDoc() {
		return fileText;
	}

	protected String getContentOfDocument(TaskMonitorDialog progressDialog) throws ParserConfigurationException, TransformerException {
		doc = createDocument();
		
		Element calculateexpresion = (Element) doc.createElement("calculateexpresion");
		doc.appendChild(calculateexpresion);
		
		calculateexpresion.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		
		Element destino = (Element) doc.createElement("destino");
		calculateexpresion.appendChild(destino);
		destino.setTextContent((String)getFromBlackboard(DESTINO));
	
		Element expresion = (Element) doc.createElement("expresion");
		calculateexpresion.appendChild(expresion);
		expresion.setTextContent(featureExpressionPanel.getExpresionTestTextArea().getText());
		
		Element from = (Element) doc.createElement("de");
		calculateexpresion.appendChild(from);
		from.setTextContent(featureExpressionPanel.getFromTextArea().getText());
		
		Element where = (Element) doc.createElement("condicion");
		calculateexpresion.appendChild(where);
		where.setTextContent(featureExpressionPanel.getWhereTextArea().getText());
		
		return parseDocumentToXML(doc);
		
	}
	
	protected String parseDocumentToXML(Document doc)
	throws TransformerException {
		// transform the Document into a String
		DOMSource domSource = new DOMSource(doc);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		// transformer.setOutputProperty
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		// transformer.setOutputProperty
		// ("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		java.io.StringWriter sw = new java.io.StringWriter();
		StreamResult sr = new StreamResult(sw);
		transformer.transform(domSource, sr);
		String xml = sw.toString();
		return xml;
	}

	/**
	 * Crea un documento xml
	 * 
	 * @throws ParserConfigurationException
	 */
	protected Document createDocument() throws ParserConfigurationException {
		// get the factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// Using factory get an instance of document builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		DOMImplementation impl = builder.getDOMImplementation();

		Document doc = impl.createDocument(null, null, null);

		return doc;
	}
	
	/**
	 * Muestra la pantalla de espera
	 * 
	 * @param titulo
	 */
	protected void initProgressDialog(String titulo){

		final TaskMonitorDialog progressDialog = new TaskMonitorDialog(
				appContext.getMainFrame(), null);

		progressDialog.setTitle(titulo);
		progressDialog.report(appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.save.dialogo.report1"));
		progressDialog
				.addComponentListener(new java.awt.event.ComponentAdapter() {
					public void componentShown(java.awt.event.ComponentEvent e) {
						new Thread(new Runnable() {
							public void run() {
								String fileTextAux="";
									try {
										fileTextAux = getContentOfDocument(progressDialog);
										setContentOfDoc(fileTextAux);
									} catch (ParserConfigurationException e) {
										logger.error("Se ha producido un error al guardar la expresión",e);
										ErrorDialog
												.show(progressDialog,
														"ERROR",
														appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.save.error"),
														StringUtil.stackTrace(e));
									} catch (TransformerException e) {
										logger.error("Se ha producido un error al guardar la expresión",e);
										ErrorDialog
												.show(progressDialog,
														"ERROR",
														appContext.getI18nString("GeopistaFeatureCalculateExpresionPlugIn.save.error"),
														StringUtil.stackTrace(e));								
									}finally {
										progressDialog.setVisible(false);
								}
							}

						}).start();
					}
				});
		GUIUtil.centreOnWindow(progressDialog);
		progressDialog.setVisible(true);
	}
	
	
	private void openExpresion(){
		
	}
	
 }
