/**
 * FeatureExpressionPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.nfunk.jep.FunctionTable;
import org.nfunk.jep.function.PostfixMathCommand;

import com.geopista.util.expression.FeatureExpresionParser;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
/**
 * 
 * @author cotesa
 *
 */
public class FeatureExpressionPanel extends JPanel
{

	private static final String ATTRIBUTE_NAME = FeatureExpressionPanel.class.getName()+"ATTRIBUTE_NAME"; 
	private static final String FORMULA = FeatureExpressionPanel.class.getName()+"FORMULA"; 
	
	
	Blackboard bk = new Blackboard();
	private PlugInContext context = null;
	private JLabel expressionLabel = null;
	private JTextField expresionTextField = null;
	private JLabel lblResults = null;
 
	protected FeatureExpresionParser expParser=new FeatureExpresionParser(null);
	private Collection currentFeatures; //lista de features a evaluar
	private JButton functionBrowserButton = null;
	private JPanel jContentPane = null;
	private JDialog functionBrowserDialog = null;  //  @jve:decl-index=0:visual-constraint="340,35"
	private JList cbFunctionList = null;
	private JScrollPane jScrollPane = null;  //  @jve:decl-index=0:visual-constraint="27,282"
	
	
	private boolean markAsError = true;
	
	/**
	 * This is the default constructor
	 */
	public FeatureExpressionPanel() {
		this(true);
	}
	
	public FeatureExpressionPanel(boolean markAsError) {
		super();
		this.markAsError = markAsError;
		initialize();
	}
	
	public void setFeatures(Collection features)
	{
		currentFeatures = features;
		if (features.size()>0)
		{
			expParser.setFeature((Feature)features.iterator().next());
		}
	}
	public void setFeature(Feature feature)
	{
		Vector features=new Vector();
		features.add(feature);
		currentFeatures=features;
		expParser.setFeature(feature);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		java.awt.GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		lblResults = new JLabel();
		expressionLabel = new JLabel();
		java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.setSize(300,200);
		expressionLabel.setText("Expression");
		lblResults.setText("");
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints2.gridx = 2;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints3.gridx = 2;
		gridBagConstraints3.gridy = 1;
		gridBagConstraints3.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints11.gridx = 1;
		gridBagConstraints11.gridy = 0;
		this.add(expressionLabel, gridBagConstraints1);
		this.add(getJTextField(), gridBagConstraints2);
		this.add(lblResults, gridBagConstraints3);
		this.add(getFunctionBrowserButton(), gridBagConstraints11);
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJTextField() {
		if (expresionTextField == null) {
			expresionTextField = new JTextField();

			expresionTextField.getDocument().addDocumentListener(
					new DocumentListener()
					{
						String oldFormula;
						public void changedUpdate(DocumentEvent e)
						{
							try
							{
								putToBlackboard(ATTRIBUTE_NAME, expressionLabel.getText());
								String formula=expresionTextField.getText();
								expParser.parseExpression(formula);
								putToBlackboard(FORMULA, formula);
								
								if (markAsError)
								{
									String result = expParser.getValueAsObject()!=null?expParser.getValueAsObject().toString():"No result.";
									lblResults.setText(result);
								}
								if (expParser.getErrorInfo()!=null)
								{
									if (markAsError)
									{
										expresionTextField.setBorder(BorderFactory.createLineBorder(Color.RED));
										lblResults.setText(expParser.getErrorInfo());	
									}									
									
									firePropertyChange("ExpressionInvalid",oldFormula,formula);

								}
								else
								{
									expresionTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
									firePropertyChange("ExpressionValid",oldFormula,formula);
								}
								expresionTextField.setToolTipText(expParser.getErrorInfo());
								oldFormula=formula;
							}catch(Exception ex)
							{
								ex.printStackTrace();
							}
						}

						public void insertUpdate(DocumentEvent e)
						{
							changedUpdate(e);
						}

						public void removeUpdate(DocumentEvent e)
						{
							changedUpdate(e);
						}
					}
			);
		}

		return expresionTextField;
	}
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener)
	{
		expresionTextField.addPropertyChangeListener(propertyName, listener);
	}
	public int getCaretPosition()
	{
		return expresionTextField.getCaretPosition();
	}
	public Document getDocument()
	{
		return expresionTextField.getDocument();
	}
	public String getSelectedText()
	{
		return expresionTextField.getSelectedText();
	}
	public int getSelectionEnd()
	{
		return expresionTextField.getSelectionEnd();
	}
	public int getSelectionStart()
	{
		return expresionTextField.getSelectionStart();
	}
	public String getText()
	{
		return expresionTextField.getText();
	}
	public String getText(int offs, int len) throws BadLocationException
	{
		return expresionTextField.getText(offs, len);
	}
	public void removeCaretListener(CaretListener listener)
	{
		expresionTextField.removeCaretListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		expresionTextField.removePropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener)
	{
		expresionTextField.removePropertyChangeListener(propertyName, listener);
	}
	public void setCaretPosition(int position)
	{
		expresionTextField.setCaretPosition(position);
	}
	public void setSelectionEnd(int selectionEnd)
	{
		expresionTextField.setSelectionEnd(selectionEnd);
	}
	public void setSelectionStart(int selectionStart)
	{
		expresionTextField.setSelectionStart(selectionStart);
	}
	public void setText(String t)
	{
		expresionTextField.setText(t);
	}
	public void setLabelText(String text)
	{
		expressionLabel.setText(text);
	}

	/**
	 * @param string
	 */
	public void pasteText(String string)
	{
		try
		{
			int selStart=expresionTextField.getSelectionStart();
			int selEnd= expresionTextField.getSelectionEnd();

			try {
				if (selStart!=0 && selStart!=selEnd)
				{ // elimina la parte seleccionada
					expresionTextField.getDocument().remove(selStart,selEnd);
					expresionTextField.setCaretPosition(selStart);
				}
			} catch (BadLocationException e) {
				System.out.println("Bad location exception");
				//e.printStackTrace();
			}
			int insertPoint=expresionTextField.getCaretPosition();
			getDocument().insertString(insertPoint,string, null);
			select(insertPoint,insertPoint+string.length());
			//setCaretPosition(getSelectionEnd());

		} catch (BadLocationException e1){
			e1.printStackTrace();
		}

	}
	public void select(int selectionStart, int selectionEnd)
	{
		expresionTextField.select(selectionStart, selectionEnd);
	}
	public void requestFocus()
	{

		//super.requestFocus();
		expresionTextField.requestFocus();
	}
	public FeatureExpresionParser getExpParser()
	{
		return expParser;
	}
	/**
	 * This method initializes functionBrowserButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getFunctionBrowserButton() {
		if (functionBrowserButton == null) {
			functionBrowserButton = new JButton();
			functionBrowserButton.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Flashlight.gif")));
			functionBrowserButton.setPreferredSize(new java.awt.Dimension(21,21));
			functionBrowserButton.setToolTipText("FunctionList");
			functionBrowserButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					getFunctionBrowserDialog().setVisible(true);

				}
			});
		}
		return functionBrowserButton;
	}
	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	/**
	 * This method initializes functionBrowserDialog	
	 * 	
	 * @return javax.swing.JDialog	
	 */    
	private JDialog getFunctionBrowserDialog() {
		if (functionBrowserDialog == null) {
			functionBrowserDialog = new JDialog();
			functionBrowserDialog.setContentPane(getJContentPane());
			functionBrowserDialog.setModal(true);
			functionBrowserDialog.setResizable(true);
			functionBrowserDialog.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			functionBrowserDialog.setTitle("Functions");
			functionBrowserDialog.setSize(250, 150);
			FunctionTable funciones=expParser.getFunctionTable();

			SortedMap sorted= new TreeMap();
			sorted.putAll(funciones);
			Vector funcionesVect=new Vector(sorted.entrySet());
			cbFunctionList.setModel(new DefaultComboBoxModel(funcionesVect));
			cbFunctionList.setCellRenderer(new ListCellRenderer() 
			{
				JLabel label=new JLabel();

				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
				{
					Entry elemento=(Entry)value;
					PostfixMathCommand command = (PostfixMathCommand)elemento.getValue();
					String documentation = expParser.getDocumentation((String)elemento.getKey());
					String description = MessageFormat.format("{0} - ({1} argumentos) {2}",
							new Object[]{
							elemento.getKey(),
							new Integer((command).getNumberOfParameters()),
							documentation==null?"":documentation
					});

					label.setText(description);
					label.setOpaque(true);
					label.setIcon(new ImageIcon(getClass().getResource("/com/geopista/ui/images/Flashlight.gif")));
					label.setBackground(isSelected|| cellHasFocus ? Color.black : Color.white);
					label.setForeground(isSelected|| cellHasFocus ? Color.white : Color.black);

					return label;
				}

			});
			cbFunctionList.setSelectedIndex(0);
		}
		return functionBrowserDialog;
	}
	/**
	 * This method initializes cbFunctionList	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JList getCbFunctionList() {
		if (cbFunctionList == null) {
			cbFunctionList = new JList();
			cbFunctionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			cbFunctionList.addMouseListener(new java.awt.event.MouseAdapter() { 
				public void mouseClicked(java.awt.event.MouseEvent e) {   
					if (e.getClickCount()==2)
					{
						pasteText((String) ((Entry)cbFunctionList.getSelectedValue()).getKey());
						functionBrowserDialog.hide();
						expresionTextField.requestFocus();
					}
				}
			});

		}
		return cbFunctionList;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setSize(41, 60);
			jScrollPane.setViewportView(getCbFunctionList());
		}
		return jScrollPane;
	}

	private void putToBlackboard(String key, Object value)
	{
		bk.put(key,value);
	}
	
	private void setBlackboard(Blackboard blackboard)
	{
		this.bk = blackboard;
	}
	public void setContext (PlugInContext context)
	{	
		this.context = context;
		setBlackboard(this.context.getWorkbenchGuiComponent().getActiveTaskComponent().getLayerViewPanel().getBlackboard());
	}	
}

