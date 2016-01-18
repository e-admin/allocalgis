/**
 * EditorGenericoWizardPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.wizard.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.jasperreports.engine.JRParameter;

import com.geopista.app.AppContext;
import com.toedter.calendar.JTextFieldDateEditor;

public class EditorGenericoWizardPanel extends ReportWizardPanel {

	protected List parametersToEdit;
	protected Map filledParameters;
 
	private JPanel pnlParameters;
	private List lblParameterNameList;
	private List parameterEditorList;
	private JLabel lblIntroduzcaParametros;
	
	// Contexto de la aplicaicon geopista
	AppContext appContext = (AppContext) AppContext.getApplicationContext();
	
	public EditorGenericoWizardPanel(){ 
		super();		
	}  
	
	public void enteredPanelFromLeft(Map dataMap) {
		try {
			filledParameters = new HashMap();			
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void exitingToRight() throws Exception {
		reportWizard.fillParameters(filledParameters);		
	}

	public boolean isInputValid() {
		if (filledParameters != null && filledParameters.size() == parametersToEdit.size()){
			return true;
		}
		return false;
	}
	
	// Devuelve la lista de parametros que no caben en el panel
	public List loadParameters(List reportParameters){
		int numberOfComponents = 15;
		
		parametersToEdit = new ArrayList();		
		int i;
		
		for (i = 0; i < numberOfComponents && i < reportParameters.size(); i++){
			parametersToEdit.add(reportParameters.get(i));
		}
		
		ArrayList remainingParameters = new ArrayList();
		for (; i < reportParameters.size(); i++){
			remainingParameters.add(reportParameters.get(i));
		}
		
		initForm();
		
		return remainingParameters;
	}
	
	protected void initForm(){
		BorderLayout borderLayout = new BorderLayout();
		pnlGeneral.setLayout(borderLayout);
		
		JPanel panelTitle = new JPanel();
		FlowLayout flowLayoutTitle = new FlowLayout();
		flowLayoutTitle.setAlignment(FlowLayout.LEFT);
		panelTitle.setLayout(flowLayoutTitle);
		pnlGeneral.add(panelTitle, BorderLayout.NORTH);
		
		lblIntroduzcaParametros = new JLabel();		
		Font fontSeleccioneParcela = lblIntroduzcaParametros.getFont();
		fontSeleccioneParcela = fontSeleccioneParcela.deriveFont(
				fontSeleccioneParcela.getStyle() ^ Font.BOLD);
		lblIntroduzcaParametros.setFont(fontSeleccioneParcela);
		panelTitle.add(lblIntroduzcaParametros);
		String text = appContext.getI18nString("informes.wizard.edicionparametros.introduzcaparametros");
		lblIntroduzcaParametros.setText(text);
		
		JPanel pnlParametersContainer = new JPanel();
		BorderLayout pnlParametersContainerBorderLayout = new BorderLayout();
		pnlParametersContainer.setLayout(pnlParametersContainerBorderLayout);
		pnlGeneral.add(pnlParametersContainer, BorderLayout.CENTER);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		pnlParameters = new JPanel();
		pnlParameters.setLayout(gridBagLayout);
		pnlParametersContainer.add(pnlParameters, BorderLayout.NORTH);
		
		lblParameterNameList = new ArrayList();
		parameterEditorList = new ArrayList();
		
		GridBagConstraints gridBagConstraints;
		Insets insets = new Insets(5,10,5,10);
		
		for (int i = 0; i < parametersToEdit.size(); i++){
			JRParameter parameter = (JRParameter) parametersToEdit.get(i);
			
			JLabel lblParameterName = new JLabel();
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			gridBagConstraints.insets = insets;
			if (parameter.getDescription() != null && parameter.getDescription().length() > 0){
				lblParameterName.setText(parameter.getDescription());
			}
			else {
				lblParameterName.setText(parameter.getName());
			}
			pnlParameters.add(lblParameterName, gridBagConstraints);
			lblParameterNameList.add(lblParameterNameList);
			
			if (parameter.getClass().isInstance(Date.class) ){  
				gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.weightx = 3.0;
				gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
				gridBagConstraints.insets = insets;
				JTextFieldDateEditor parameterEditor = new JTextFieldDateEditor();
				parameterEditor.setDate(new Date());
				parameterEditor.setName(String.valueOf(i));
				pnlParameters.add(parameterEditor, gridBagConstraints);
				
				filledParameters.put(parameter.getName(), parameterEditor.getDate());
				
				parameterEditor.addFocusListener(new FocusAdapter() {
		            public void focusLost(FocusEvent evt) {
		            	String indexName = evt.getComponent().getName();
		            	int editorIndex = Integer.valueOf(indexName).intValue();
		            	dateFieldChangeDetected(editorIndex);
		            }
		        });
				
				parameterEditor.addKeyListener(new KeyAdapter() {
		            public void keyTyped(KeyEvent evt) {
		            	String indexName = evt.getComponent().getName();
		            	int editorIndex = Integer.valueOf(indexName).intValue();
		            	dateFieldChangeDetected(editorIndex);
		            }
		        });
				
				parameterEditorList.add(parameterEditor);
			}
			else{
				gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.weightx = 3.0;
				gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
				gridBagConstraints.insets = insets;
				JTextField parameterEditor = new JTextField();
				parameterEditor.setText("");
				parameterEditor.setName(String.valueOf(i));
				pnlParameters.add(parameterEditor, gridBagConstraints);
				
				parameterEditor.addFocusListener(new FocusAdapter() {
		            public void focusLost(FocusEvent evt) {
		            	String indexName = evt.getComponent().getName();
		            	int editorIndex = Integer.valueOf(indexName).intValue();
		            	textFieldChangeDetected(editorIndex);
		            }
		        });
				
				parameterEditor.addKeyListener(new KeyAdapter() {
		            public void keyTyped(KeyEvent evt) {
		            	String indexName = evt.getComponent().getName();
		            	int editorIndex = Integer.valueOf(indexName).intValue();
		            	textFieldChangeDetected(editorIndex);
		            }
		        });
				
				parameterEditorList.add(parameterEditor);
			}
		}
	}

	protected void textFieldChangeDetected(int editorIndex){
		JRParameter parameter = (JRParameter) parametersToEdit.get(editorIndex);
		JTextField parameterEditor = (JTextField) parameterEditorList.get(editorIndex);
		
		if (parameterEditor.getText() != ""){
			filledParameters.put(parameter.getName(), parameterEditor.getText());
		}
		
		getWizardContext().inputChanged();
	}
	
	protected void dateFieldChangeDetected(int editorIndex){
		JRParameter parameter = (JRParameter) parametersToEdit.get(editorIndex);
		JTextFieldDateEditor parameterEditor = (JTextFieldDateEditor) parameterEditorList.get(editorIndex);
		
		if (parameterEditor.getDate() != null){
			filledParameters.put(parameter.getName(), parameterEditor.getDate());
		}
		
		getWizardContext().inputChanged();
	}
}
