/**
 * 
 */
package com.geopista.ui.autoforms;

import com.geopista.feature.ValidationError;
import com.vividsolutions.jump.feature.Feature;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.UIManager;

import com.geopista.editor.GeopistaEditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.geopista.ui.components.TwoColumnsFieldPanel;
import com.geopista.util.ApplicationContext;

/**
 * Panel con pestañas que alberga hojas de formulario con dos posibles columnas.
 * @author juacas
 */
public class TabbedFieldPanel extends JPanel implements AutoForm {

	private JTabbedPane jTabbedPane = null;

private FormController controller;
	private TwoColumnsFieldPanel currentTabPanel;

	/**
	 * 
	 */
	public TabbedFieldPanel() {
		super();
		initialize();
	
	}

	/**
	 * @Override
	 */
	public void disableAll() {
		for (Iterator iter = tabNameToPanel.values().iterator(); iter.hasNext();) {
			TwoColumnsFieldPanel panel = (TwoColumnsFieldPanel) iter.next();
			panel.disableAll();
		}
	}

	/**
	 * @Override
	 */
	public boolean highLightFieldError(ValidationError err) {
		boolean found=false;
		for (Iterator iter = tabNameToPanel.values().iterator(); iter.hasNext();) {
			TwoColumnsFieldPanel panel = (TwoColumnsFieldPanel) iter.next();
			if (panel.highLightFieldError(err))
				{
				found=true;
				for(int i=0;i<this.jTabbedPane.getComponentCount();i++)
					{
					if(this.jTabbedPane.getComponent(i)==panel)
						jTabbedPane.setBackgroundAt(i,Color.RED);/////TODO: highlight TABS!!!
					}
				return found;
				}
		}
		return found;
	}

	/**
	 * @Override
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.weighty = 1.0;
		gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(579, 278));
		this.add(getJTabbedPane(), gridBagConstraints4);
	}

	


	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			//addTab("First", null, null);
			jTabbedPane.addTab(null, null, getFirstDefaultPanel(), null);  // Generated
		}
		return jTabbedPane;
	}

	public void setCurrentTab(String name) {
		currentTabPanel = tabNameToPanel.get(name)!=null?(TwoColumnsFieldPanel) tabNameToPanel.get(name):currentTabPanel;
	}
public TwoColumnsFieldPanel getCurrentPanel()
{
	return currentTabPanel;
}
	Hashtable tabNameToPanel = new Hashtable(); // @jve:decl-index=0:

	private JPanel firstDefaultPanel = null;

	private JLabel defaultLabel = null;

	
/**
 * Crea un a nueva pestaña y pasa a se la pestaña actual apra las operaciones de rellenado de campos
 * @param title
 * @param icon
 * @param tip
 */
	public void addTab(String title, Icon icon, String tip) {
		
		jTabbedPane.remove(firstDefaultPanel);
		TwoColumnsFieldPanel component = new TwoColumnsFieldPanel();
		//component.setBorder(BorderFactory.createTitledBorder("twocolumns"));
		tabNameToPanel.put(title, component);
		jTabbedPane.addTab(title, icon, component, tip);
		setCurrentTab(title);
	}

	/**
 * This method initializes firstDefaultPanel	
 * 	
 * @return javax.swing.JPanel	
 */
private JPanel getFirstDefaultPanel()
{
if (firstDefaultPanel == null)
	{
	GridBagConstraints gridBagConstraints = new GridBagConstraints();
	gridBagConstraints.gridx = 0;  // Generated
	gridBagConstraints.gridy = 0;  // Generated
	defaultLabel = new JLabel();
	defaultLabel.setText("First default Panel");  // Generated
	firstDefaultPanel = new JPanel();
	firstDefaultPanel.setLayout(new GridBagLayout());  // Generated
	firstDefaultPanel.setName("Default");  // Generated
	firstDefaultPanel.add(defaultLabel, gridBagConstraints);  // Generated
	}
return firstDefaultPanel;
}

	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception e){};
		JDialog dlg = new JDialog();
		TabbedFieldPanel tab=new TabbedFieldPanel();
		tab.addTab("second", null, "otra pestaña");
		tab.addTab("third",null,"ultima");
		tab.setCurrentTab("second");
		tab.getCurrentPanel().createGroupingPanel("test");
		
		tab.getCurrentPanel().addTextField("prueba", "este campo tiene valor", 30, null, true, ".*", "Rellenar cuanto antes.");
		tab.getCurrentPanel().addCheckBox("checkbox", true, "narcar rapido");
		tab.getCurrentPanel().startNewColumn();
		tab.getCurrentPanel().addTextField("prueba3", "este cam32po tiene valor", 30, null, true, ".*", "Rellenar cuanto antes.");
		tab.getCurrentPanel().addCheckBox("checkbox3", true, "narca32r rapido");
		tab.setCurrentTab("third");
		tab.getCurrentPanel().addUrlTextField("URLs", "http://www.grupotecopy.es", 40, false, "http://.*", "URLd lksj dl2");
		dlg.getContentPane().add(tab);
		dlg.setSize(400, 500);
		dlg.setModal(true);
		dlg.setVisible(true);
		System.exit(0);
	}

	public boolean checkPanel(boolean updateView) {
		
		return controller.checkPanel(updateView);
	}

	public boolean checkPanels() {
		// TODO Auto-generated method stub
		return false;
	}

	public void enter() {
		// TODO Auto-generated method stub
		
	}

	public void exit() {
		// TODO Auto-generated method stub
		
	}

	public boolean getBoolean(String fieldName) {
		// TODO Auto-generated method stub
		return false;
	}

	public JComponent getComponent(String fieldName) {
		
		for (Iterator iter = tabNameToPanel.values().iterator(); iter.hasNext();) {
			TwoColumnsFieldPanel panel = (TwoColumnsFieldPanel) iter.next();
			JComponent comp=panel.getComponent(fieldName);
			if(comp!=null)return comp;
		}
		return null;
	}

	public JComponent getComponentByFieldName(String attName) {
		for (Iterator iter = tabNameToPanel.values().iterator(); iter.hasNext();) {
			TwoColumnsFieldPanel panel = (TwoColumnsFieldPanel) iter.next();
			JComponent comp=panel.getComponentByFieldName(attName);
			if(comp!=null)return comp;
		}
		return null;
	}

	public Double getDouble(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getFieldComponents() {
		ArrayList lista=new ArrayList();
		for (Iterator iter = tabNameToPanel.values().iterator(); iter.hasNext();) {
			TwoColumnsFieldPanel panel = (TwoColumnsFieldPanel) iter.next();
			lista.addAll(panel.getFieldComponents());
		}
		return lista;
	}

	public Collection getFieldLabels() {
		ArrayList lista=new ArrayList();
		for (Iterator iter = tabNameToPanel.values().iterator(); iter.hasNext();) {
			TwoColumnsFieldPanel panel = (TwoColumnsFieldPanel) iter.next();
			lista.addAll(panel.getFieldLabels());
		}
		return lista;
	}

	public Set getFieldNames() {
		HashSet lista=new HashSet();
		for (Iterator iter = tabNameToPanel.values().iterator(); iter.hasNext();) {
			TwoColumnsFieldPanel panel = (TwoColumnsFieldPanel) iter.next();
			lista.addAll(panel.getFieldNames());
		}
		return lista;
	}

	public Integer getInteger(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getText(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(String attName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void restoreFieldsDecoration() {
		
		for (Iterator iter = tabNameToPanel.values().iterator(); iter.hasNext();) {
			TwoColumnsFieldPanel panel = (TwoColumnsFieldPanel) iter.next();
			panel.restoreFieldsDecoration();
		}
		for(int i=0;i<this.jTabbedPane.getComponentCount();i++)
		{
			jTabbedPane.setBackgroundAt(i,jTabbedPane.getBackground());/////TODO: highlight TABS!!!
		}// TODO: Restore TABS decoration
		
	}

	public void setApplicationContext(ApplicationContext context) {
		// TODO Auto-generated method stub
		
	}

	public JComponent getLabel(String fieldName) {
		for (Iterator iter = tabNameToPanel.values().iterator(); iter.hasNext();) {
			TwoColumnsFieldPanel panel = (TwoColumnsFieldPanel) iter.next();
			JComponent comp=panel.getLabel(fieldName);
			if(comp!=null)return comp;
		}
		return null;
	}	
} // @jve:decl-index=0:visual-constraint="10,10"
