/**
 * ColumnPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.autoforms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.geopista.ui.components.DateField;
import com.geopista.ui.components.UrlTextField;
import com.vividsolutions.jump.util.CollectionMap;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;

public class ColumnPanel extends JPanel {
	int rowCount = 0;
	JPanel currentGroupingPanel=null;
	protected HashMap fieldNameToComponentMap = new HashMap();
	protected HashMap fieldNameToLabelMap = new HashMap();
	protected CollectionMap fieldNameToEnableCheckListMap = new CollectionMap();
	JPanel helper=new JPanel();// evita el centrado vertical
	public ColumnPanel() {
		setLayout(new BorderLayout());
		add(helper, BorderLayout.NORTH);
		helper.setLayout(new BoxLayout(helper, BoxLayout.PAGE_AXIS));
	helper.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
	}
	public JPanel createGroupingPanel(String name) {
		 currentGroupingPanel = new JPanel(new GridBagLayout());

        currentGroupingPanel.setBorder(BorderFactory.createTitledBorder(name));
        setCurrentGroupingPanel(currentGroupingPanel);
        return currentGroupingPanel;
	} 
	
	public void addRow(String fieldName, JComponent label, JComponent component, EnableCheck[] enableChecks, String toolTipText) {
	    if (toolTipText != null)
	    {
	        label.setToolTipText(toolTipText);
	    }
	    if (currentGroupingPanel==null) 
	    	createGroupingPanel(null); // Puede que no se definan paneles agrupadores internos.
	    fieldNameToLabelMap.put(fieldName, label);
	    fieldNameToComponentMap.put(fieldName, component);
	    if (enableChecks != null)
	    {
	        addEnableChecks(fieldName, Arrays.asList(enableChecks));
	    }
	    int componentX;
	    int componentWidth;
	    int labelX;
	    int labelWidth;
	    if (component instanceof JCheckBox || component instanceof JLabel
	            || component instanceof JPanel && !(component instanceof DateField)
	            && !(component instanceof UrlTextField))
	    {
	        componentX = 0;
	        componentWidth = 2;
	        labelX = 1;
	        labelWidth = 1;
	    } 
	    else if(component instanceof JScrollPane){
	    	labelX = 0;
	    	labelWidth = 2;
	    	componentX = 0;
	    	componentWidth = 2;
	    }
	    else
	    {
	        labelX = 0;
	        labelWidth = 1;
	        componentX = 1;
	        componentWidth = 3;
	    }

	    
    	currentGroupingPanel.add(label, new GridBagConstraints(labelX, getCurrentRowCount(),
            labelWidth, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
            GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 10), 0, 0));

	    // HORIZONTAL especially needed by separator. [Jon Aquino]
    	if(component instanceof JScrollPane){
    		incrementCurrentRowCount();
    	}
	    	
	    currentGroupingPanel
	            .add(
	                    component,
	                    new GridBagConstraints(
	                            componentX,
	                            getCurrentRowCount(),
	                            componentWidth,
	                            1,
	                            1.0,
	                            0.0,
	                            GridBagConstraints.WEST,
	                            (component instanceof DateField || component instanceof JComboBox || 
	                            		component instanceof JCheckBox) 
	                            		? GridBagConstraints.NONE
	                                    : GridBagConstraints.HORIZONTAL,
	                            new Insets(0, 0, 5, 0), 0, 0));

	    incrementCurrentRowCount();
	
	}
	private void addEnableChecks(String fieldName, Collection enableChecks) {
	    fieldNameToEnableCheckListMap.addItems(fieldName, enableChecks);
	}
	/**
	 * 
	 */
	int getCurrentRowCount() {
		return rowCount;
	}

	/**
	 * 
	 *
	 */
	void incrementCurrentRowCount() {
		rowCount++;
		
	}


	public JPanel getCurrentTargetPanel() {
		return currentGroupingPanel;
	}

/**
 * Si existe el panel lo reutiliza.
 * En caso contrario lo añade
 * @param panel
 */
	public void setCurrentGroupingPanel(JPanel panel) {
		
		    	Component[] comps=getComponents();
		    	for (int i = 0; i < comps.length; i++) {
					Component component = comps[i];
					if (component==panel)
					{
						this.currentGroupingPanel = panel;
						return;
					}
				}
		    	helper.add(panel);
		    	this.currentGroupingPanel = panel;
			
		
	}

}
