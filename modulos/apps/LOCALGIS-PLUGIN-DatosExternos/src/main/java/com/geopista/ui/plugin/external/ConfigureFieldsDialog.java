/**
 * ConfigureFieldsDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;



public class ConfigureFieldsDialog extends JDialog {
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	
    private final static String OK = aplicacion.getI18nString("btnAceptar");
    private final static String CANCEL = aplicacion.getI18nString("btnCancelar");
    private final static String TABLE = aplicacion.getI18nString("ExternalDataSourcePlugin.Table");
    private final static String COLUMN = aplicacion.getI18nString("ExternalDataSourcePlugin.Column");
    private final static String LAYER = aplicacion.getI18nString("ExternalDataSourcePlugin.Layer");
    private final static String FIELD = aplicacion.getI18nString("ExternalDataSourcePlugin.Field");
    private final static String SYSTEMDATA = aplicacion.getI18nString("ExternalDataSourcePlugin.SystemData");
    private final static String EXTERNALDATA = aplicacion.getI18nString("ExternalDataSourcePlugin.ExternalData");
    private final static String ERRORCAMPOS = I18N.get("ConfigureQueryExternalDataSource.dataSource.errorCampos");
    private final static String ERROR = I18N.get("ConfigureQueryExternalDataSource.dataSource.error");
    


    public ConfigureFieldsDialog(final Dialog dialog, String title, boolean modal) {
        super(dialog,title, modal);
    	initComponents();
    	setEvents();
    }

    private void setEvents() {
    	externalDataTablejComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
            	externalDataTablejComboBoxItemStateChanged(evt);
            }
        });
    	internalDataTablejComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
            	internalDataTablejComboBoxItemStateChanged(evt);
            }
        });
    	okjButton.addActionListener( new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			okjButtonActionPerformed();
    			
    		}
    	});
       	canceljButton.addActionListener( new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			canceljButtonButtonActionPerformed();
    			
    		}
    	});
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                dialogClosing(evt);	                
            }	            
        });
    }
    




	protected void okjButtonButtonAcionPerformed() {
		// TODO Auto-generated method stub
		
	}

	private void initComponents() {
    	
        internalDatajPanel = new javax.swing.JPanel();
        layerjLabel = new javax.swing.JLabel();
        fieldjLabel = new javax.swing.JLabel();
        internalDataColumnjComboBox = new javax.swing.JComboBox();
        internalDataTablejComboBox = new javax.swing.JComboBox();
        externalDatajPanel = new javax.swing.JPanel();
        tablejLabel = new javax.swing.JLabel();
        columnjLabel = new javax.swing.JLabel();
        externalDataTablejComboBox = new javax.swing.JComboBox();
        externalDataColumnjComboBox = new javax.swing.JComboBox();
        systemDatajLabel = new javax.swing.JLabel();
        extenalDatajLabel = new javax.swing.JLabel();
        okjButton = new javax.swing.JButton();
        canceljButton = new javax.swing.JButton();

        getContentPane().setLayout(null);
 
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        internalDatajPanel.setLayout(null);
        setResizable(false);
        internalDatajPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        layerjLabel.setText(LAYER);
        internalDatajPanel.add(layerjLabel);
        layerjLabel.setBounds(20, 20, 40, 14);

        fieldjLabel.setText(FIELD);
        internalDatajPanel.add(fieldjLabel);
        fieldjLabel.setBounds(20, 60, 40, 14);

        
        internalDatajPanel.add(internalDataColumnjComboBox);
        internalDataColumnjComboBox.setBounds(90, 57, 185, 18);

        
        internalDatajPanel.add(internalDataTablejComboBox);
        internalDataTablejComboBox.setBounds(90, 18, 185, 18);

        getContentPane().add(internalDatajPanel);
        internalDatajPanel.setBounds(30, 50, 290, 90);

        externalDatajPanel.setLayout(null);

        externalDatajPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablejLabel.setText(TABLE);
        externalDatajPanel.add(tablejLabel);
        tablejLabel.setBounds(20, 20, 40, 14);

        columnjLabel.setText(COLUMN);
        externalDatajPanel.add(columnjLabel);
        columnjLabel.setBounds(20, 60, 50, 14);

       
        externalDatajPanel.add(externalDataTablejComboBox);
        externalDataTablejComboBox.setBounds(90, 18, 185, 18);
        

        
        externalDatajPanel.add(externalDataColumnjComboBox);
        externalDataColumnjComboBox.setBounds(90, 57, 185, 18);

        getContentPane().add(externalDatajPanel);
        externalDatajPanel.setBounds(380, 50, 290, 90);

        systemDatajLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        systemDatajLabel.setText(SYSTEMDATA);
        getContentPane().add(systemDatajLabel);
        systemDatajLabel.setBounds(40, 10, 120, 20);

        extenalDatajLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        extenalDatajLabel.setText(EXTERNALDATA);
        getContentPane().add(extenalDatajLabel);
        extenalDatajLabel.setBounds(410, 10, 120, 20);

        okjButton.setText(OK);
        getContentPane().add(okjButton);
        okjButton.setBounds(260, 170, 80, 23);

        canceljButton.setText(CANCEL);
        getContentPane().add(canceljButton);
        canceljButton.setBounds(360, 170, 80, 23);

        pack();
        setSize(new Dimension(700,240));

    }
	
    private void canceljButtonButtonActionPerformed() {
    	closed = true;
		dispose();		
	}
	
    private void okjButtonActionPerformed() {
    	
    	String sInternalLayerName = (String) internalDataTablejComboBox.getSelectedItem();
    	String sInternalAttributeName = (String) internalDataColumnjComboBox.getSelectedItem();
    	String sExternalTableName = (String) externalDataTablejComboBox.getSelectedItem();
    	String sExternalColumnName = (String) externalDataColumnjComboBox.getSelectedItem();
    	
    	if (sInternalLayerName==null || sInternalAttributeName==null || sExternalTableName==null || sExternalColumnName==null) {
    		ErrorDialog.show(this, ERROR, ERRORCAMPOS, "");
			return;
    	}else{
	    	internalLayerName = sInternalLayerName;
	    	internalAttributeName = sInternalAttributeName;
	    	externalTableName = sExternalTableName;
	    	externalColumnName = sExternalColumnName;
			dispose();
    	}
	}
    
    protected void dialogClosing(WindowEvent evt) {
    	closed = true;
    }
	
	private void internalDataTablejComboBoxItemStateChanged(ItemEvent evt) {
		String selectedItem = (String) internalDataTablejComboBox.getSelectedItem();
		setInternalColumnModel(selectedItem);
	}

	private void externalDataTablejComboBoxItemStateChanged(ItemEvent evt) {
		String selectedItem = (String) externalDataTablejComboBox.getSelectedItem();
		setExternalColumnModel(selectedItem);
		
	}
	
    public void setExternalModels() {
    	setExternalTableModel();
    	String tableName = (String) externalDataTablejComboBox.getSelectedItem();
    	setExternalColumnModel(tableName);
    }
    
    private void setExternalTableModel() {
    	Set set = externalMetaData.keySet();
    	int length = set.size();
    	length++; // for space addition
    	String[] tableNames = new String[length];
    	int i = 0;
    	tableNames[i]=null;
    	i++;
    	for (Iterator iterator = set.iterator(); iterator.hasNext();i++) {
			tableNames[i] = (String) iterator.next();			
		}
    	externalDataTablejComboBox.setModel(new DefaultComboBoxModel(tableNames));
    	externalDataTablejComboBox.setSelectedIndex(0);
    }
    
    private void setExternalColumnModel(String tableName) {
		Vector fieldNames = new Vector();
		
		if (tableName!=null && !tableName.equals("")){
			fieldNames = (Vector) externalMetaData.get(tableName);
		}

		externalDataColumnjComboBox.setModel(
				new DefaultComboBoxModel(
										(String[]) fieldNames.toArray(new String[fieldNames.size()])
										)
				);
	}
    
	public void setExternalData(Hashtable hashtableExternalData) {
			externalMetaData = hashtableExternalData;			
		}
		

    public void setInternalModels() {
    	setInternalTableModel();
    	String tableName = (String) internalDataTablejComboBox.getSelectedItem();
    	setInternalColumnModel(tableName);
    }
    
    private void setInternalTableModel() {
		Set set = internalMetaData.keySet();
		int length = set.size();
		length++; // for space addition
		String[] tableNames = new String[length];
		int i=0;
		tableNames[i] = null;
		i++;
		for (Iterator iterator = set.iterator(); iterator.hasNext();i++) {
			tableNames[i] = (String) iterator.next();			
		}
		internalDataTablejComboBox.setModel(new DefaultComboBoxModel(tableNames));
		internalDataTablejComboBox.setSelectedIndex(0);
	}
    private void setInternalColumnModel(String tableName) {
    	Vector fieldNames = new Vector();
    	if (tableName!=null && !tableName.equals("")){
    		fieldNames = (Vector) internalMetaData.get(tableName);
    	}

    	internalDataColumnjComboBox.setModel(
				new DefaultComboBoxModel(
										(String[]) fieldNames.toArray(new String[fieldNames.size()])
										)
				);
	}
    

	public void setInternalData(TreeMap hashtableExternalData) {
			internalMetaData = hashtableExternalData;			
		}
		

	public String getInternalLayerName() {
		return internalLayerName;
	}

	public String getInternalAttributeName() {
		return internalAttributeName;
	}

	public String getExternalTableName() {
		return externalTableName;
	}

	public String getExternalColumnName() {
		return externalColumnName;
	}  
	
	public boolean isInternalDataTablejComboBoxSelected(){
		if (this.internalDataTablejComboBox.getSelectedItem()==null){
			return false;
		}
		return true;
	}
	
	public boolean isExternalDataTablejComboBoxSelected(){
		if (this.externalDataTablejComboBox.getSelectedItem()==null){
			return false;
		}
		return true;
	}
	
	public void setInternalDataTablejComboBox(Object oInternalDataTable){
		this.internalDataTablejComboBox.setSelectedItem(oInternalDataTable);
	}
	
	public void setInternalDataColumnjCombobBox(Object oInternalDataColumn){
		this.internalDataColumnjComboBox.setSelectedItem(oInternalDataColumn);
	}
	
	public void setExternalDataColumnjComboBox(Object oExternalDataColumn){
		this.externalDataColumnjComboBox.setSelectedItem(oExternalDataColumn);
	}
	
	public void setExternalDataTablejCombobox(Object oExternalDataTablejComboBox){
		this.externalDataTablejComboBox.setSelectedItem(oExternalDataTablejComboBox);
	}
	
    private javax.swing.JComboBox externalDataColumnjComboBox;
    private javax.swing.JComboBox externalDataTablejComboBox;
    private javax.swing.JPanel externalDatajPanel;
    private javax.swing.JComboBox internalDataColumnjComboBox;
    private javax.swing.JComboBox internalDataTablejComboBox;
    private javax.swing.JPanel internalDatajPanel;
    private javax.swing.JButton okjButton;
    private javax.swing.JButton canceljButton;
    private javax.swing.JLabel layerjLabel;
    private javax.swing.JLabel fieldjLabel;
    private javax.swing.JLabel tablejLabel;
    private javax.swing.JLabel columnjLabel;
    private javax.swing.JLabel systemDatajLabel;
    private javax.swing.JLabel extenalDatajLabel;
             
    private Hashtable externalMetaData;
    private TreeMap internalMetaData;
    
    private String internalLayerName;
    private String internalAttributeName;

    private String externalTableName;
    private String externalColumnName;

	private boolean closed;



	public boolean isClosed() {
		return closed;
	}
}

