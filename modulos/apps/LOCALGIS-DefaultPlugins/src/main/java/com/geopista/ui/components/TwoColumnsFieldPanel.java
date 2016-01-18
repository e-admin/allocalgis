/**
 * TwoColumnsFieldPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.feature.Domain;
import com.geopista.feature.ValidationError;
import com.geopista.ui.autoforms.ColumnPanel;
import com.geopista.ui.images.IconLoader;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;

public class TwoColumnsFieldPanel extends JPanel implements
		PropertyChangeListener, ItemListener {

	

	public TwoColumnsFieldPanel() {
		initialize();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4321074705484137259L;

	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(TwoColumnsFieldPanel.class);

	protected JPanel currentTargetPanel;

	ColumnPanel firstColumnPanel = new ColumnPanel();

	ColumnPanel secondColumnPanel = new ColumnPanel();

	JPanel verticalSeparatorPanel = new JPanel();

	ColumnPanel currentColumnPanel = firstColumnPanel;

	protected HashMap fieldNameToComponentMap = new HashMap();

	protected CollectionMap fieldNameToEnableCheckListMap = new CollectionMap();

	protected HashMap fieldNameToLabelMap = new HashMap();

	protected int rowCount = 0;

	/**
	 * @Override
	 */
	public int getCurrentRowCount() {
		return rowCount;
	}

	private JCheckBox addCheckBox(String fieldName, boolean initialValue) {
		return addCheckBox(fieldName, initialValue, null);
	}

	public JCheckBox addCheckBox(String fieldName, boolean initialValue,
			String toolTipText) {
		JCheckBox checkBox = new JCheckBox(fieldName, initialValue);
		checkBox.addPropertyChangeListener("value", this);
		addRow(fieldName, new JLabel(""), checkBox, null, toolTipText);
		return checkBox;
	}

	public JComboBox addComboBox(String fieldName, Object selectedItem,
			Collection items, String toolTipText) {
		JComboBox comboBox = new JComboBox(new Vector(items));
		comboBox.setSelectedItem(selectedItem);
		comboBox.addItemListener(this);
		addRow(fieldName, new JLabel(fieldName), comboBox, null, toolTipText);
		return comboBox;
	}

	/**
	 * @param fieldName
	 * @param fecha
	 * @param aproxLenght
	 */
	public void addDateField(String fieldName, Date fecha, int aproxLenght,
			String toolTipText) 
	{
	addDateField(fieldName,fecha,toolTipText,null,null);
	}
	public void addDateField(String fieldName, Date fecha,
			String toolTipText, Date initialDate, Date finalDate) 
	{
		DateField dfield = createDateField(fecha, initialDate,finalDate);
		addRow(fieldName, new JLabel(fieldName), dfield, null, toolTipText);

	}
	
	public void addDateField(String fieldName, Date fecha,
			String toolTipText, Date initialDate, Date finalDate, boolean editable) 
	{
		DateField dfield = createDateField(fecha, initialDate,finalDate, editable);
		addRow(fieldName, new JLabel(fieldName), dfield, null, toolTipText);

	}

	private DateField createDateField(Date fecha, Date initialDate,Date finalDate) {
		
		return createDateField(fecha, initialDate, finalDate,true);
		
	}
	
	private DateField createDateField(Date fecha, Date initialDate,Date finalDate, boolean editable) {
		DateField dfield = new DateField(fecha, initialDate,finalDate);
		dfield.setEditable(editable);
		dfield.addPropertyChangeListener("date", this);
		
		return dfield;
	}

	private JTextField addDoubleField(String fieldName, double initialValue,
			int approxWidthInChars) {
		return createNumericField(fieldName, String.valueOf(initialValue),
				approxWidthInChars,
				new EnableCheck[] { createDoubleCheck(fieldName) }, null);
	}

	private JTextField addDoubleField(String fieldName, double initialValue,
			int approxWidthInChars, String toolTipText) {
		return createNumericField(fieldName, String.valueOf(initialValue),
				approxWidthInChars,
				new EnableCheck[] { createDoubleCheck(fieldName) }, toolTipText);
	}

	private void addEnableChecks(String fieldName, Collection enableChecks) {
		fieldNameToEnableCheckListMap.addItems(fieldName, enableChecks);
	}

	private JTextField addIntegerField(String fieldName, int initialValue,
			int approxWidthInChars, String toolTipText) {
		return createNumericField(fieldName, String.valueOf(initialValue),
				approxWidthInChars,
				new EnableCheck[] { createIntegerCheck(fieldName) },
				toolTipText);
	}

	private JLabel addLabel(String text) {
		// Take advantage of #addRow's special rule for JLabels: they span all
		// the columns of the GridBagLayout. [Jon Aquino]
		JLabel lbl = new JLabel(text);
		addRow(lbl);
		return lbl;
	}

	private JTextField addNonNegativeDoubleField(String fieldName,
			double initialValue, int approxWidthInChars) {
		return createNumericField(fieldName, String.valueOf(initialValue),
				approxWidthInChars, new EnableCheck[] {
						createDoubleCheck(fieldName),
						createNonNegativeCheck(fieldName) }, null);
	}

	private JTextField createNumericField(String fieldName,
			String initialValue, int approxWidthInChars,
			EnableCheck[] enableChecks, String toolTipText) {
		JTextField fld = addTextField(fieldName, initialValue,
				approxWidthInChars, enableChecks, toolTipText);
		fld.setHorizontalAlignment(JTextField.RIGHT);
		return fld;
	}

	private JTextField addPositiveDoubleField(String fieldName,
			double initialValue, int approxWidthInChars) {
		return createNumericField(fieldName, String.valueOf(initialValue),
				approxWidthInChars, new EnableCheck[] {
						createDoubleCheck(fieldName),
						createPositiveCheck(fieldName) }, null);
	}

	private JTextField addPositiveIntegerField(String fieldName,
			int initialValue, int approxWidthInChars) {
		return createNumericField(fieldName, String.valueOf(initialValue),
				approxWidthInChars, new EnableCheck[] {
						createIntegerCheck(fieldName),
						createPositiveCheck(fieldName) }, null);
	}

	private void addRow(JComponent c) {
		addRow("DUMMY", new JLabel(""), c, null, null);
	}

	/**
	 * Añade una fila nueva con un campo al panel agrupador actual.
	 * 
	 * @param fieldName
	 * @param label
	 * @param component
	 * @param enableChecks
	 * @param toolTipText
	 */
	public void addRow(String fieldName, JComponent label,
			JComponent component, EnableCheck[] enableChecks, String toolTipText) {

		currentColumnPanel.addRow(fieldName, label, component, enableChecks,
				toolTipText);

		fieldNameToLabelMap.put(fieldName, label);
		fieldNameToComponentMap.put(fieldName, component);
		incrementCurrentRowCount();

	}

	/**
	 * Añade directamente un componente a la columna No utilizar para campos.
	 * 
	 * @param comp
	 */
	public void addColumnComponent(JComponent comp) {
		currentColumnPanel.add(comp);
	}

	public void incrementCurrentRowCount() {
		rowCount++;

	}

	private void addSeparator() {
		JPanel separator = new JPanel();
		separator.setBackground(Color.black);
		separator.setPreferredSize(new Dimension(1, 1));
		addRow(separator);
	}

	public JTextField addTextField(String fieldName, String initialValue,
			int approxWidthInChars, EnableCheck[] enableChecks,
			boolean editable, String pattern, String toolTipText) {
		// final JFormattedTextField textField = new JFormattedTextField();
		// //JFormattedText solo permitía reemplazar caracteres y no insertar.
		// Ignoro por qué.
		final JTextField textField = createTextField(initialValue,
				approxWidthInChars, editable, toolTipText);
		addRow(fieldName, new JLabel(fieldName), textField, enableChecks,
				toolTipText);
		return textField;
	}

	public JTextField createTextField(String initialValue,
			int approxWidthInChars, boolean editable, String toolTipText) {
		final JTextField textField = new JTextField();

		textField.setText(initialValue);
		textField.setColumns(approxWidthInChars);
		// textField.setMinimumSize(new Dimension(80,20));
		// textField.setPreferredSize(new Dimension(80,20));
		textField.addPropertyChangeListener("value", this);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			private boolean processingEvent = false;

			public void changedUpdate(DocumentEvent arg0) {

			}

			public void insertUpdate(DocumentEvent arg0) {

				// disable listener to avoid cascade events
				if (processingEvent)
					return;
				processingEvent = true;

				// firePropertyChange("value",textField.getText(),textField.getText());
				// Ignoro porqué no funciona el PropertySupport
				PropertyChangeListener prolst[] = ((PropertyChangeListener[]) textField
						.getPropertyChangeListeners("value"));
				for (int i = 0; i < prolst.length; i++) {
					PropertyChangeListener listener = prolst[i];
					listener.propertyChange(new PropertyChangeEvent(this,
							"value", textField.getText(), textField.getText()));
				}
				processingEvent = false;
			}

			public void removeUpdate(DocumentEvent arg0) {
				// disable listener to avoid cascade events
				if (processingEvent)
					return;
				processingEvent = true;

				PropertyChangeListener prolst[] = ((PropertyChangeListener[]) textField
						.getPropertyChangeListeners("value"));
				for (int i = 0; i < prolst.length; i++) {
					PropertyChangeListener listener = prolst[i];
					listener.propertyChange(new PropertyChangeEvent(this,
							"value", textField.getText(), textField.getText()));
				}
				processingEvent = false;
			}
		});

		textField.setEditable(editable);

		textField.setToolTipText(toolTipText);
		return textField;
	}

	private JTextField addTextField(String fieldName, String initialValue,
			int approxWidthInChars, EnableCheck[] enableChecks,
			String toolTipText) {
		return addTextField(fieldName, initialValue, approxWidthInChars,
				enableChecks, true, null, toolTipText);
	}

	public UrlTextField addUrlTextField(String fieldName, String initialValue,
			int approxWidthInChars, boolean editable, String pattern,
			String toolTipText) {
		UrlTextField textField = createUrlTextField(initialValue,
				approxWidthInChars, editable);
		addRow(fieldName, new JLabel(fieldName), textField, null, toolTipText);
		return textField;
	}

	private UrlTextField createUrlTextField(String initialValue,
			int approxWidthInChars, boolean editable) {
		UrlTextField textField = new UrlTextField();
		textField.setValue(initialValue);
		textField.setColumns(approxWidthInChars);

		textField.addPropertyChangeListener("value", this);
		textField.setEditable(editable);
		return textField;
	}
	
	public JScrollPane addJScrollPane(String fieldName, String initialValue,
			int approxWidthInChars, boolean editable, String pattern,
			String toolTipText) {
		JScrollPane scrollPane = createJScrollPane(initialValue,
				approxWidthInChars, editable);
		addRow(fieldName, new JLabel(fieldName), scrollPane, null, toolTipText);
		return scrollPane;
	}
	
	private JScrollPane createJScrollPane(String initialValue,
			int approxWidthInChars, boolean editable) {
		final JTextArea jTextArea = new JTextArea();
		jTextArea.setLineWrap(true);
		//jTextArea.setRows(3);
		int filas = (initialValue.length()/36);
		if(filas >5){
			jTextArea.setRows(6);
		}
		else{
			jTextArea.setRows(filas+1);
		}
		jTextArea.setWrapStyleWord(true);
		jTextArea.setBorder(null);
		jTextArea.setText(initialValue);
		jTextArea.setColumns(approxWidthInChars);
		jTextArea.setEditable(editable);
		
		//JScrollPane apunteJScrollPane = new JScrollPane();
		final JScrollPane apunteJScrollPane = new JScrollPane();

        apunteJScrollPane.setViewportView(jTextArea);

        jTextArea.addPropertyChangeListener("value", this);
        jTextArea.getDocument().addDocumentListener(new DocumentListener() {
			private boolean processingEvent = false;

			public void changedUpdate(DocumentEvent arg0) {

			}

			public void insertUpdate(DocumentEvent arg0) {

				// disable listener to avoid cascade events
				if (processingEvent)
					return;
				processingEvent = true;

				// firePropertyChange("value",textField.getText(),textField.getText());
				// Ignoro porqué no funciona el PropertySupport
				PropertyChangeListener prolst[] = ((PropertyChangeListener[]) jTextArea
						.getPropertyChangeListeners("value"));
				for (int i = 0; i < prolst.length; i++) {
					PropertyChangeListener listener = prolst[i];
					listener.propertyChange(new PropertyChangeEvent(this,
							"value", jTextArea.getText(), jTextArea.getText()));
				}
				processingEvent = false;
			}

			public void removeUpdate(DocumentEvent arg0) {
				// disable listener to avoid cascade events
				if (processingEvent)
					return;
				processingEvent = true;

				PropertyChangeListener prolst[] = ((PropertyChangeListener[]) jTextArea
						.getPropertyChangeListeners("value"));
				for (int i = 0; i < prolst.length; i++) {
					PropertyChangeListener listener = prolst[i];
					listener.propertyChange(new PropertyChangeEvent(this,
							"value", jTextArea.getText(), jTextArea.getText()));
				}
				processingEvent = false;
			}
		});

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
     	   public void run() { 
     		   apunteJScrollPane.getVerticalScrollBar().setValue(0);
     	   }
     	});
		return apunteJScrollPane;
	}

	private EnableCheck createDoubleCheck(final String fieldName) {
		return new EnableCheck() {
			public String check(JComponent component) {
				try {
					Double.parseDouble(getText(fieldName).trim());
					return null;
				} catch (NumberFormatException e) {
					return "\"" + getText(fieldName).trim()
							+ "\" is an invalid double (" + fieldName + ")";
				}
			}
		};
	}

	private EnableCheck createIntegerCheck(final String fieldName) {
		return new EnableCheck() {
			public String check(JComponent component) {
				try {
					Integer.parseInt(getText(fieldName).trim());
					return null;
				} catch (NumberFormatException e) {
					return "\"" + getText(fieldName).trim()
							+ "\" is an invalid integer (" + fieldName + ")";
				}
			}
		};
	}

	private EnableCheck createNonNegativeCheck(final String fieldName) {
		return new EnableCheck() {
			public String check(JComponent component) {
				if (Double.parseDouble(getText(fieldName).trim()) >= 0) {
					return null;
				}
				return "\"" + getText(fieldName).trim() + "\" must be >= 0 ("
						+ fieldName + ")";
			}
		};
	}

	private EnableCheck createPositiveCheck(final String fieldName) {
		return new EnableCheck() {
			public String check(JComponent component) {
				if (Double.parseDouble(getText(fieldName).trim()) > 0) {
					return null;
				}
				return "\"" + getText(fieldName).trim() + "\" must be > 0 ("
						+ fieldName + ")";
			}
		};
	}

	public JComponent getComponent(String fieldName) {
		return (JComponent) fieldNameToComponentMap.get(fieldName);
	}

	public JComponent getComponentByFieldName(String attName) {
		return (JComponent) fieldNameToComponentMap.get(attName);
	}

	/**
	 * @return
	 */
	public Collection getFieldComponents() {
		return fieldNameToComponentMap.values();
	}

	/**
	 * @return
	 */
	public Collection getFieldLabels() {
		return fieldNameToLabelMap.values();
	}

	/**
	 * @return
	 */
	public Set getFieldNames() {
		return fieldNameToComponentMap.keySet();
	}

	/**
	 * @param err
	 * @return true si tiene un componente con error
	 */
	public boolean highLightFieldError(ValidationError err) {
		JLabel lbl = (JLabel) getLabel(err.attName);
		if (lbl == null)
			return false;
		lbl.setIcon(IconLoader.icon("Editar_datos.gif"));

		StringBuffer msg = new StringBuffer();
		msg.append("\r\n");
		msg.append(MessageFormat.format(AppContext.getApplicationContext()
				.getI18nString("FeatureDialog.FormattedErrorReportMessage"),
				new Object[] { err.attName, err.message }));

		lbl.setToolTipText(lbl.getToolTipText() + ":" + msg);
		JComponent comp = getComponentByFieldName(err.attName);
		if (comp instanceof JPanel 	&& comp.getBorder() instanceof TitledBorder) {
			
		
			
			TitledBorder bor = (TitledBorder) comp.getBorder();
			if (bor != null)
				comp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED,2),bor.getTitle()));

		} else
			comp.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		return true;
	}

	public void indentLabel(String comboBoxFieldName) {
		getLabel(comboBoxFieldName).setBorder(
				BorderFactory.createMatteBorder(0, (int) new JCheckBox()
						.getPreferredSize().getWidth(), 0, 0, getLabel(
						comboBoxFieldName).getBackground()));
	}

	public JComponent getLabel(String fieldName) {
		return (JComponent) fieldNameToLabelMap.get(fieldName);
	}

	/**
	 * Devuelve en forma de Date el valor de un campo.
	 * 
	 * Debe ser un DateField, en caso contrario intenta parsear la cadena
	 * 
	 * @param attName
	 * @return
	 */
	public Date getDate(String attName) {
		JComponent comp = getComponent(attName);

		if (comp instanceof DateField)
			return ((DateField) comp).getValue();
		else
			try {
				return DateFormat.getDateInstance().parse(getText(attName));
			} catch (ParseException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("getDate(attName = " + attName
							+ ") - Fecha incorrecta." + getText(attName));
				}

				// Supone que si la fecha es incorrecta el Date es null
				return null;
			}

	} 

	public Double getDouble(String fieldName) {
		return "".equals(getText(fieldName)) ? null : new Double(getText(
				fieldName).trim());
	}

	public Integer getInteger(String fieldName) {
		try {
			return "".equals(getText(fieldName)) ? null : new Integer(getText(
					fieldName).trim());
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("getInteger(String) - Exception e=" + e); //$NON-NLS-1$
			}

			return null;
		}
	}

	public boolean getBoolean(String fieldName) {
		JCheckBox checkbox = (JCheckBox) fieldNameToComponentMap.get(fieldName);
		return checkbox.isSelected();
	}

	public JCheckBox getCheckBox(String fieldName) {
		return (JCheckBox) getComponent(fieldName);
	}

	public JComboBox getComboBox(String fieldName) {
		return (JComboBox) getComponent(fieldName);
	}

	public String getText(String fieldName) {
		JComponent fieldcomp = getComponent(fieldName);
		if (fieldcomp instanceof JTextField) {
			return ((JTextField) fieldcomp).getText();
		}
		if(fieldcomp instanceof JScrollPane){
			return ((JTextArea)(((JScrollPane) fieldcomp).getViewport().getComponent(0))).getText();
		}
		if (fieldcomp instanceof UrlTextField) {
			return ((UrlTextField) fieldcomp).getText();
		} else if (fieldcomp instanceof JComboBox) {
			if (((JComboBox) fieldcomp).getSelectedItem() == null)
				return "";
			if (((JComboBox) fieldcomp).getSelectedItem() instanceof Domain){
			Domain content = ((Domain) ((JComboBox) fieldcomp)
					.getSelectedItem());
			return content == null ? "" : content.getPattern();
			}else return ((String) ((JComboBox) fieldcomp).getSelectedItem());
		} else if (fieldcomp instanceof JCheckBox) {
			return ((JCheckBox) fieldcomp).isSelected() ? "1" : "0";
		} else if (fieldcomp instanceof DateField) {
			return ((DateField) fieldcomp).getText();
		}
		Assert.shouldNeverReachHere(fieldName);
		return null;
	}

	/**
	 * 
	 */
	public void restoreFieldsDecoration() {
		// restore all fields
		for (Iterator i = getFieldComponents().iterator(); i.hasNext();) {
			JComponent comp = (JComponent) i.next();
			if (comp instanceof JPanel 	&& comp.getBorder() instanceof TitledBorder) {
				TitledBorder bor = (TitledBorder) comp.getBorder();
				if (bor != null)
					comp.setBorder(BorderFactory.createTitledBorder(bor
							.getTitle()));
			}

			else {
				comp.setBorder(javax.swing.plaf.basic.BasicBorders
						.getTextFieldBorder());
			}
			comp.setToolTipText(null);

		}
		for (Iterator i = getFieldLabels().iterator(); i.hasNext();) {
			JLabel comp = (JLabel) i.next();
			comp.setIcon(null);
			String tt = comp.getToolTipText();
			if (tt == null)
				continue;

			int pos = tt.indexOf(":");
			if (pos == 0)
				comp.setToolTipText("");
			else if (pos > -1)
				comp
						.setToolTipText(comp.getToolTipText().substring(0,
								pos - 1));
		}
	}

	/**
	 * Crea un panel para agrupar campos y lo define como actual
	 * 
	 * @param name
	 * @return
	 */
	public JPanel createGroupingPanel(String name) {
		return currentColumnPanel.createGroupingPanel(name);
	}

	/**
	 * This method can be called once only.
	 */
	public void startNewColumn() {
		if (secondColumnPanel.isVisible()) {
			Assert
					.shouldNeverReachHere("#startNewColumn can be called once only");
		}
		secondColumnPanel.setVisible(true);
		verticalSeparatorPanel.setVisible(false);
		String name = currentColumnPanel.getCurrentTargetPanel().getName();
		currentColumnPanel = secondColumnPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geopista.ui.AutoForm#disableAll()
	 */
	public void disableAll() {
		Collection comps = getFieldComponents();
		for (Iterator compIter = comps.iterator(); compIter.hasNext();) {
			JComponent element = (JComponent) (compIter.next());
			element.setEnabled(false);
			if (element instanceof JTextComponent)
				((JTextComponent) element).setEditable(false);

			if (element instanceof UrlTextField)
				((UrlTextField) element).setEditable(false);

			if (element instanceof DateField)
				((DateField) element).setEditable(false);
			
			if (element instanceof JScrollPane){
				((JTextArea)(((JScrollPane) element).getViewport().getComponent(0))).setEnabled(false);
				//((JTextArea)(((JScrollPane) element).getViewport().getComponent(0))).setEditable(false);
				//((JScrollPane) element).setEnabled(false);
			}
				
		}
	}

	/**
	 * This method initializes this create two panels to handle two columns of
	 * fields.
	 */
	protected void initialize() {

		//

		firstColumnPanel.setVisible(true);
		secondColumnPanel.setVisible(false);

		verticalSeparatorPanel.setBackground(Color.gray);
		verticalSeparatorPanel.setVisible(false);
		verticalSeparatorPanel.setPreferredSize(new Dimension(1, 1));

		JPanel helper = new JPanel(); // Este panel sirve para agrupar los
										// componentes al norte y evitar el
										// centrado vertical.

		setLayout(new BorderLayout());
		// setLayout(new GridBagLayout());

		add(helper, BorderLayout.NORTH);

		 helper.setLayout(new BoxLayout(helper,BoxLayout.LINE_AXIS));
		 helper.add(firstColumnPanel);
		 helper.add(secondColumnPanel);

//		helper.setLayout(new GridBagLayout());
//		helper.add(firstColumnPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0,
//				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
//				new Insets(0, 10, 0, 0), 0, 0));
//		helper.add(secondColumnPanel, new GridBagConstraints(1, 0, 1, 1, 1.0,
//				0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
//				new Insets(0, 10, 0, 10), 0, 0));

	}

	public void setCurrentGroupingPanel(JPanel panel) {
		currentColumnPanel.setCurrentGroupingPanel(panel);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		this.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt
				.getNewValue());
	}

	public void itemStateChanged(ItemEvent e) {
		this.firePropertyChange("value", null, e.getItem());
	}

} // @jve:decl-index=0:visual-constraint="10,10"
