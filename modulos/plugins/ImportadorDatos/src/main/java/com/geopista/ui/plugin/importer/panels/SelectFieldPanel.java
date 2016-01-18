/**
 * SelectFieldPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.importer.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.feature.GeopistaSchema;
import com.geopista.ui.dialogs.SelectLayerFieldPanel;
import com.vividsolutions.jump.feature.FeatureSchema;

public class SelectFieldPanel extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private GeopistaSchema schema;
	private JList lstTargetField;  //  @jve:decl-index=0:
	private String selectedFieldName;
	private ArrayList actionListeners = new ArrayList();

	/**
	 * This is the default constructor
	 */
	public SelectFieldPanel() {
		super();
		initialize();
	}
	public SelectFieldPanel(GeopistaSchema sch)
	{
		super();
		this.schema=sch;
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 500);
		this.setMinimumSize(new Dimension(300,500));

		this.getViewport().add(getLstField());
		this.add(new JLabel("prueba panel"));
		if (schema!=null)
			populateList(schema);
	}
	public void populateList(FeatureSchema fsch) {
		Vector<String> vect=new Vector();
		
		if (fsch!=null)
		{
			for (int i =0;i<fsch.getAttributeCount();i++)
			{
				//	if (i!=fsch.getGeometryIndex())
				vect.addElement(fsch.getAttributeName(i));
			}
		}
		

		getLstField().setListData(vect);
		//if (vect.size()!=0)
		//	getLstField().setSelectedValue(vect.firstElement(),true);


	}
	private JList getLstField() {
		if (lstTargetField == null) {
			lstTargetField = new JList();


			lstTargetField.setVisibleRowCount(3);

			lstTargetField.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstTargetField.addMouseListener(new java.awt.event.MouseAdapter() {
				private String selectedFieldName;

				public void mouseClicked(java.awt.event.MouseEvent e) {

					if (e.getClickCount()==2)
					{
						selectedFieldName= (String) lstTargetField.getSelectedValue();
						fire(SelectFieldPanel.this,SelectLayerFieldPanel.FIELD_ACTIONED,"FieldActioned");
					}
				}
			});

			lstTargetField.addListSelectionListener(new ListSelectionListener() {
				private String oldValue=null;

				public void valueChanged(ListSelectionEvent e)
				{
					firePropertyChange("FIELDVALUE",oldValue,(String) ((JList)e.getSource()).getSelectedValue());
					selectedFieldName=oldValue=(String) lstTargetField.getSelectedValue();
				}
			});
		}
		
		return lstTargetField;
	}
	
	
	public void setLstCellRenderer (ListCellRenderer cellRenderer)
	{
		lstTargetField.setCellRenderer(cellRenderer);
	}
	
	
	public void addActionListener(ActionListener listener) {
		actionListeners.add(listener);
	}

	public void remove(ActionListener listener) {
		actionListeners.remove(listener);
	}

	public void fire(Object source, int id, String command) {
		for (Iterator i = actionListeners.iterator(); i.hasNext();) {
			ActionListener listener = (ActionListener) i.next();
			listener.actionPerformed(new ActionEvent(source, id, command));
		}
	}
	public String getSelectedFieldName() {
		return selectedFieldName;
	}
}
