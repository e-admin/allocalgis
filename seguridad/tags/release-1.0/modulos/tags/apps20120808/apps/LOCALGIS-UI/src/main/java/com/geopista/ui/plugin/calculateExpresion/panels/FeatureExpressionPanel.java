/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 03-oct-2004 by juacas
 *
 * 
 */
package com.geopista.ui.plugin.calculateExpresion.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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

import com.geopista.ui.dialogs.DistanceLinkingPanel;
import com.geopista.ui.plugin.calculateExpresion.util.FeatureExpresionParser;

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

	private JLabel expressionLabel = null;
	private JLabel whereLabel = null;
	private JLabel fromLabel = null;
	private JTextField expresionTextField = null;
	
	private JTextArea expresionTestTextField = null;
	private JScrollPane expresionJScrollPane = null;

	private JTextArea whereTextField = null;
	private JScrollPane whereJScrollPane = null;
	private JTextArea fromTextField = null;
	private JScrollPane fromJScrollPane = null;
	private JLabel lblequeal = null;
	private JLabel lblestado = null;


	protected FeatureExpresionParser expParser=new FeatureExpresionParser(null);
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
	

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
		
		lblequeal = new JLabel();
		expressionLabel = new JLabel();
		whereLabel  = new JLabel();
		fromLabel = new JLabel();
		lblestado = new JLabel();
		java.awt.GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
				
		java.awt.GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
		
		java.awt.GridBagConstraints gridBagConstraints02 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
		java.awt.GridBagConstraints gridBagConstraints03 = new GridBagConstraints();
		

		this.setLayout(new GridBagLayout());
		this.setSize(300,200);
		expressionLabel.setText("Expression");
		whereLabel.setText("CONDICION");
		lblequeal.setText("=");
		fromLabel.setText("DE");
		
		gridBagConstraints00.gridx = 0;
		gridBagConstraints00.gridy = 0;
		gridBagConstraints00.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints00.anchor = java.awt.GridBagConstraints.WEST;
		
		gridBagConstraints10.gridx = 1;
		gridBagConstraints10.gridy = 0;
		
		gridBagConstraints20.gridx = 2;
		gridBagConstraints20.gridy = 0;

		gridBagConstraints30.gridx = 3;
		gridBagConstraints30.gridy = 0;
		gridBagConstraints30.weightx = 1.0;
		gridBagConstraints30.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints30.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
		
		
		gridBagConstraints01.gridx = 0;
		gridBagConstraints01.gridy = 1;
		gridBagConstraints01.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints01.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints01.gridwidth = 3;
		
		gridBagConstraints31.gridx = 3;
		gridBagConstraints31.gridy = 1;
		gridBagConstraints31.weightx = 1.0;
		gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints31.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
	
		
		gridBagConstraints02.gridx = 0;
		gridBagConstraints02.gridy = 2;
		gridBagConstraints02.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints02.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints02.gridwidth = 3;
		
		gridBagConstraints32.gridx = 3;
		gridBagConstraints32.gridy = 2;
		gridBagConstraints32.weightx = 1.0;
		gridBagConstraints32.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints32.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints32.anchor = java.awt.GridBagConstraints.WEST;
		
		gridBagConstraints03.gridx = 0;
		gridBagConstraints03.gridy = 3;
		gridBagConstraints03.insets = new java.awt.Insets(5,5,5,5);
		gridBagConstraints03.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints03.gridwidth = 4;
		
		
		
		
		this.add(expressionLabel, gridBagConstraints00);
		this.add(lblequeal, gridBagConstraints10);
		this.add(getFunctionBrowserButton(), gridBagConstraints20);
		this.add(getExpresionJScrollPane(), gridBagConstraints30);

		this.add(fromLabel, gridBagConstraints01);
		this.add(getFromJScrollPane(), gridBagConstraints31);
		
		this.add(whereLabel, gridBagConstraints02);
		this.add(getWhereJScrollPane(), gridBagConstraints32);
		
		this.add(lblestado, gridBagConstraints03);
		
	}
	
	
	public JScrollPane getWhereJScrollPane() {
		if(whereJScrollPane == null){
			whereJScrollPane = new JScrollPane(getWhereTextArea());
			whereJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			whereJScrollPane.setPreferredSize(new Dimension(200,200));
		}
		return whereJScrollPane;
	}

	public void setWhereJScrollPane(JScrollPane whereJScrollPane) {
		this.whereJScrollPane = whereJScrollPane;
	}
	public JScrollPane getFromJScrollPane() {
		if(fromJScrollPane == null){
			fromJScrollPane = new JScrollPane(getFromTextArea());
			fromJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		}
		return fromJScrollPane;
	}

	public void setFromJScrollPane(JScrollPane fromJScrollPane) {
		this.fromJScrollPane = fromJScrollPane;
	}

	
	public JScrollPane getExpresionJScrollPane() {
		if(expresionJScrollPane == null){
			expresionJScrollPane = new JScrollPane(getExpresionTestTextArea());
			expresionJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		}
		return expresionJScrollPane;
	}

	public void setExpresionJScrollPane(JScrollPane expresionJScrollPane) {
		this.expresionJScrollPane = expresionJScrollPane;
	}

	
	public JTextArea getExpresionTestTextArea() {
		if (expresionTestTextField == null) {
			expresionTestTextField= new JTextArea(100,3);
			expresionTestTextField.setLineWrap(true);
			expresionTestTextField.setWrapStyleWord(true);
			expresionTestTextField.setRows(3);

		}
		return expresionTestTextField;
	}

	public void setExpresionTestTextField(JTextArea expresionTestTextField) {
		this.expresionTestTextField = expresionTestTextField;
	}
	
	

	public JTextArea getWhereTextArea() {
		if (whereTextField == null) {
			whereTextField = new JTextArea();
			whereTextField= new JTextArea(100,3);
			whereTextField.setLineWrap(true);

		}
		return whereTextField;
	}

	public void setWhereTextField(JTextArea whereTextField) {
		this.whereTextField = whereTextField;
	}
	

	public JTextArea getFromTextArea() {
		if (fromTextField == null) {
			fromTextField = new JTextArea();
			fromTextField= new JTextArea(100,300);
			fromTextField.setLineWrap(true);
			fromTextField.setWrapStyleWord(true);
			fromTextField.setRows(3);

		}
		return fromTextField;
	}

	public void setFromTextField(JTextArea fromTextField) {
		this.fromTextField = fromTextField;
	}
	
	
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	public JTextField getJTextField() {
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
								
								//putToBlackboard(ATTRIBUTE_NAME, expressionLabel.getText());
								//putToBlackboard(FORMULA, expresionTestTextField.getText());
								
								
								//String formula=expresionTextField.getText();
								//expParser.parseExpression(formula);
							//	putToBlackboard(FORMULA, formula);
								
//								if (markAsError)
//								{
//									String result = expParser.getValueAsObject()!=null?expParser.getValueAsObject().toString():"No result.";
//									lblResults.setText(result);
//								}
//								if (expParser.getErrorInfo()!=null)
//								{
//									if (markAsError)
//									{
//										expresionTextField.setBorder(BorderFactory.createLineBorder(Color.RED));
//										lblResults.setText(expParser.getErrorInfo());	
//									}									
//									
//									firePropertyChange("ExpressionInvalid",oldFormula,formula);
//
//								}
//								else
//								{
//									expresionTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
//									firePropertyChange("ExpressionValid",oldFormula,formula);
//								}
//								expresionTextField.setToolTipText(expParser.getErrorInfo());
//								oldFormula=formula;
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
		expresionTestTextField.addPropertyChangeListener(propertyName, listener);
	}
	public int getCaretPosition()
	{
		return expresionTestTextField.getCaretPosition();
	}
	public Document getDocument()
	{
		return expresionTestTextField.getDocument();
	}
	public String getSelectedText()
	{
		return expresionTestTextField.getSelectedText();
	}
	public int getSelectionEnd()
	{
		return expresionTestTextField.getSelectionEnd();
	}
	public int getSelectionStart()
	{
		return expresionTestTextField.getSelectionStart();
	}
	public String getText()
	{
		return expresionTestTextField.getText();
	}
	public String getText(int offs, int len) throws BadLocationException
	{
		return expresionTestTextField.getText(offs, len);
	}
	public void removeCaretListener(CaretListener listener)
	{
		expresionTestTextField.removeCaretListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		expresionTestTextField.removePropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener)
	{
		expresionTestTextField.removePropertyChangeListener(propertyName, listener);
	}
	public void setCaretPosition(int position)
	{
		expresionTestTextField.setCaretPosition(position);
	}
	public void setSelectionEnd(int selectionEnd)
	{
		expresionTestTextField.setSelectionEnd(selectionEnd);
	}
	public void setSelectionStart(int selectionStart)
	{
		expresionTestTextField.setSelectionStart(selectionStart);
	}
	public void setText(String t)
	{
		expresionTestTextField.setText(t);
	}
	public void setLabelText(String text)
	{
		expressionLabel.setText(text);
	}
	
	public String getLabelText()
	{
		return expressionLabel.getText();
	}

	/**
	 * @param string
	 */
	public void pasteText(String string)
	{
		try
		{
			int selStart=expresionTestTextField.getSelectionStart();
			int selEnd= expresionTestTextField.getSelectionEnd();

			try {
				if (selStart!=0 && selStart!=selEnd)
				{ // elimina la parte seleccionada
					expresionTestTextField.getDocument().remove(selStart,selEnd);
					expresionTestTextField.setCaretPosition(selStart);
				}
			} catch (BadLocationException e) {
				System.out.println("Bad location exception");
				//e.printStackTrace();
			}
			int insertPoint=expresionTestTextField.getCaretPosition();
			getDocument().insertString(insertPoint,string, null);
			select(insertPoint,insertPoint+string.length());
			//setCaretPosition(getSelectionEnd());

		} catch (BadLocationException e1){
			e1.printStackTrace();
		}

	}
	public void select(int selectionStart, int selectionEnd)
	{
		expresionTestTextField.select(selectionStart, selectionEnd);
	}
	public void requestFocus()
	{

		//super.requestFocus();
		expresionTestTextField.requestFocus();
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
						expresionTestTextField.requestFocus();
						
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
	
	public JLabel getLblestado() {
		return lblestado;
	}

	public void setLblestado(JLabel lblestado) {
		this.lblestado = lblestado;
	}
	
}

