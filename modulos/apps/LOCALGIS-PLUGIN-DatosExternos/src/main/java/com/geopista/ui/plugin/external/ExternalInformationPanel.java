/**
 * ExternalInformationPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;

public class ExternalInformationPanel extends JPanel implements FeatureExtendedPanel{
	


	private JLabel jLabel1;
	private JLabel jLabel2;
	private JComboBox dataSourcejComboBox;
	private JComboBox queryjComboBox;
	private JButton jButton1;
	private JScrollPane jScrollPane1;
	private JTable jTable1;
	private JPanel jPanel1;
    
    public ExternalInformationPanel() {
    	jbInit();
    	setEvents();
	}
    
    private void setEvents() {
        dataSourcejComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dataSourcejComboBoxItemStateChanged(evt);
            }
        });
        
        jButton1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				okButtonActionPerformed(e);
			}			
        	
        });
        
    }
    private void jbInit() 
    {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dataSourcejComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        queryjComboBox = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        this.setLayout(null);

        this.setName(I18N.get("ExternalDataSourcePlugin.ExternalData"));
        this.setSize(new Dimension(435, 450));
        this.setLocation(5, 10);
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dataSourcejComboBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        queryjComboBox = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(null);

        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setText(I18N.get("ExternalDataSourcePlugin.DataSource"));
        jPanel1.add(jLabel1);
        jLabel1.setBounds(30, 30, 150, 14);

        dataSourcejComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Art\u00edculo 1", "Art\u00edculo 2", "Art\u00edculo 3", "Art\u00edculo 4" }));
        jPanel1.add(dataSourcejComboBox);
        dataSourcejComboBox.setBounds(150, 25, 190, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText(I18N.get("ExternalDataSourcePlugin.Consulta"));
        jPanel1.add(jLabel2);
        jLabel2.setBounds(370, 30, 150, 14);

        jPanel1.add(queryjComboBox);
        queryjComboBox.setBounds(444, 25, 180, 20);

        jButton1.setText(I18N.get("ExternalDataSourcePlugin.obtenerDatos"));
        jPanel1.add(jButton1);
        jButton1.setBounds(277, 74, 110, 23);

        add(jPanel1);
        jPanel1.setBounds(5, 0, 648, 130);

        
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1);
        jScrollPane1.setBounds(57, 142, 550, 390);

    }
 

	private void dataSourcejComboBoxItemStateChanged(ItemEvent evt) {
    	String dataSourceName = (String) evt.getItem();
    	setQueryModel(dataSourceName);
		
	}

	public void enter() {
		//System.out.println("Enter ....");
		
	}

	public void exit() {
		//System.out.println("Exit ......");
		
	}


    public void setModels(Hashtable queries) {
    	this.queries = queries;
    	setDataSourceModel();
    	String dataSourceName = (String) dataSourcejComboBox.getSelectedItem();
    	setQueryModel(dataSourceName);
    }
    
    private void setDataSourceModel() {
    	Set set = queries.keySet();
    	int length = set.size();
    	String[] dataSourceNames = new String[length];
    	int i = 0;
    	for (Iterator iterator = set.iterator(); iterator.hasNext();i++) {
    		dataSourceNames[i] = (String) iterator.next();			
		}
    	dataSourcejComboBox.setModel(new DefaultComboBoxModel(dataSourceNames));
    	dataSourcejComboBox.setSelectedIndex(0);
    }
    
    
    private void setQueryModel(String dataSourceName) {
    	Vector fieldNames = (Vector) queries.get(dataSourceName);
    	queryjComboBox.setModel(
    			new DefaultComboBoxModel(
    									(String[]) fieldNames.toArray(new String[fieldNames.size()])
    									)
    			);
    	
    }

    private void okButtonActionPerformed(ActionEvent e) {
		String dataSourceName = (String) dataSourcejComboBox.getSelectedItem();		
		String queryName = (String) queryjComboBox.getSelectedItem();
		ExternalDataSourceDAO externalDataSourceDAO = new ExternalDataSourceDAO();
		ExternalDataSource externalDataSource = externalDataSourceDAO.find(dataSourceName);
		QueryDAO queryDAO = new QueryDAO();
		Query query = queryDAO.findQuery(queryName, dataSourceName);
		
		Connection connection = ConnectionUtility.getConnection(externalDataSource);
		String queryText = query.getText().replaceAll("\n", " ");
		String internalAtribute = query.getInternalAttribute();
		Object value = attributes.get(internalAtribute);
		String queryToExecute = buildQuery(queryText, query.getExternalTable()
				,query.getExternalColumn(), value);
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(queryToExecute);
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();
			String[] columnNames = new String[columnCount];

			CustomDefaultTableModel defaultTableModel = new CustomDefaultTableModel();
			
			for (int i = 0; i < columnCount; i++) {
				columnNames[i] = resultSetMetaData.getColumnName(i+1);
			}
			defaultTableModel.setColumnIdentifiers(columnNames);
			while (resultSet.next()) {
				Vector rowData = new Vector();
				for (int j = 0; j < columnNames.length; j++) {
					rowData.add(resultSet.getObject(columnNames[j]));
				}
				defaultTableModel.addRow(rowData);
			}
			if (defaultTableModel.getRowCount()>0) {
				jTable1.setModel(defaultTableModel);
				jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
				jScrollPane1.setViewportView(jTable1);

			}
			else {
				jTable1.setModel(defaultTableModel);
				jTable1.removeAll();
			}
			//System.out.println(queryToExecute);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		

	}
	
	private String buildQuery(String query,String externalTable,String externalColumn,Object attributeValue) {
		//System.out.println(attributeValue.getClass().getName());
		String sqlValue = new String();
		if (attributeValue instanceof Integer) {
			sqlValue = attributeValue.toString();
		}
		else {
			if (attributeValue instanceof String){		
				sqlValue = "'" + attributeValue.toString() + "'";
			}
			else {
				sqlValue = "'"  + attributeValue.toString() + "'";
			}
			
		}
		
		if (query.toUpperCase().indexOf("WHERE")<0) {
			String result = new String(query);
			result = result + " WHERE " + externalTable + "." + externalColumn + " = " + sqlValue ;
			return result;
		}
		else {
			String result = new String(query);
			result = result + " AND " + externalTable + "." + externalColumn  + " = " + sqlValue ;
			return result;
		}
		
	}

	private Hashtable queries;

	private Hashtable attributes;
	
	public Hashtable getAttributes() {
		return attributes;
	}

	public void setAttributes(Hashtable attributes) {
		this.attributes = attributes;	
	}
	
	private class CustomDefaultTableModel extends DefaultTableModel{

		public boolean isCellEditable(int row, int column) {
			return false;
		}
		
	}
}

