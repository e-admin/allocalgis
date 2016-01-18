/**
 * ConfigureQueryDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import nickyb.sqleonardo.querybuilder.QueryModel;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.datosexternos.QueryUtility;
import com.vividsolutions.jump.I18N;


public class ConfigureQueryDialog extends JDialog {
	JTextField queryNameTextField;
    JList queryList;
    private Connection connection;
    

    private ConfigureQueryHelper helper;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
    private ExternalDataSource[] externalDataSources;
    private Query[] queries;
	private boolean listValueChanged;
	private boolean deleteAction;
	private String origen;
	
	// Constantes para indicar desde donde se usa la clase:
	public static final String EDITORGIS = "editorGIS";
	public static final String GESTORCAPAS = "gestorCapas";
    
	/*
	 * Creamos 1 nuevo constructor para indicarle desde donde accedemos a esta ventana (EditorGIS o Gestor de Capas)
	 * para que haga 1 cosa u otra.
	 * */
    public ConfigureQueryDialog(final Frame frame,String title, boolean modal, String origen) {
    	super(frame, title, modal);
        helper = new ConfigureQueryHelper();
        this.origen = origen;
        initComponents();
        setEvents();
        setModels();        
    }
        
    /*
     * Este es el constructor antiguo que lo mantenemos por si se utiliza en alguna otra clase, lo unico que hemos
     * añadido ha sido lo de añadir la variable origen, ya que eta ventana antes solo aparecia en el EditorGIS.
     * */
    public ConfigureQueryDialog(final Frame frame,String title, boolean modal) {
    	super(frame, title, modal);
        helper = new ConfigureQueryHelper();
        this.origen = EDITORGIS;
        initComponents();
        setEvents();
        setModels();        
    }  

	private void setModels() {
		setDatasourceComboBox();
	}

	private void setEvents() {
    	
        queryList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                queryListValueChanged(evt);
            }
        });
        
        dataSourceComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dataSourceComboBoxItemStateChanged(evt);
            }
        });
        
        newQueryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newQueryButtonActionPerformed(evt);
            }
        });
        
        deleteQueryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteQueryButtonActionPerformed(evt);
            }
        });
        
        designQueryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                designQueryButtonActionPerformed(evt);
            }
        });
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        MyDocumentListener listener = new MyDocumentListener();
        queryNameTextField.getDocument().addDocumentListener(listener);        

    }
 

	private void initComponents() {
		

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        dataSourceComboBox = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        queryList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        queryTextArea = new javax.swing.JTextArea();
        newQueryButton = new javax.swing.JButton();
        deleteQueryButton = new javax.swing.JButton();
        designQueryButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        queryNameTextField = new javax.swing.JTextField();

        getContentPane().setLayout(null);


        setResizable(false);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText(I18N.get("ExternalDataSourcePlugin.DataSource"));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 30, 150, 15);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel2.setText(I18N.get("ConfigureQueryExternalDataSource.Consulta"));
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 60, 150, 15);

        getContentPane().add(dataSourceComboBox);
        dataSourceComboBox.setBounds(150, 25, 220, 22);

        getContentPane().add(jSeparator1);
        jSeparator1.setBounds(20, 90, 770, 10);


        queryList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(queryList);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 110, 160, 300);

        queryTextArea.setColumns(20);
        queryTextArea.setRows(5);
        jScrollPane2.setViewportView(queryTextArea);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(220, 140, 570, 270);

        newQueryButton.setText(I18N.get("ConfigureQueryExternalDataSource.nuevo"));
        getContentPane().add(newQueryButton);
        newQueryButton.setBounds(20, 420, 70, 23);

        deleteQueryButton.setText(I18N.get("ConfigureQueryExternalDataSource.eliminar"));
        getContentPane().add(deleteQueryButton);
        deleteQueryButton.setBounds(99, 420, 70, 23);

        designQueryButton.setText(I18N.get("ConfigureQueryExternalDataSource.consulta.titulo"));
        getContentPane().add(designQueryButton);
        designQueryButton.setBounds(410, 420, 140, 23);

        okButton.setText(I18N.get("ConfigureQueryExternalDataSource.ok"));
        

        getContentPane().add(okButton);
        okButton.setBounds(330, 470, 70, 23);

        cancelButton.setText(I18N.get("ConfigureQueryExternalDataSource.botonCancelar"));

        getContentPane().add(cancelButton);
        cancelButton.setBounds(410, 470, 75, 23);
        
        queryNameTextField.setBackground(java.awt.Color.white);
        queryNameTextField.setDisabledTextColor(java.awt.Color.white);
        getContentPane().add(queryNameTextField);
        queryNameTextField.setBounds(220, 110, 570, 19);

        pack();
        
        setSize(new Dimension(820,580));
    	
    }  
	
	private void setQueryList(String dataSourceName) {
		boolean first = hashtableDataSource.get(dataSourceName) == null ? true : false ;
		if (first) {
			Vector vector =helper.getQueries(dataSourceName);
			
			if (vector != null) {
				queries = (Query[]) vector.toArray(new Query[vector.size()]); 
				DefaultListModel defaultListModel = new DefaultListModel();
				for (int i = 0; i < queries.length; i++) {
					defaultListModel.add(i,new QueryListItem(queries[i],false,false));
				}
				hashtableDataSource.put(dataSourceName, defaultListModel);
		        queryList.setModel(defaultListModel);
		        queryList.setSelectedIndex(0);
		        if (vector.size()==0) {
		        	queryNameTextField.setText("");
		        	queryTextArea.setText("");
		        	designQueryButton.setEnabled(false);
		        	deleteQueryButton.setEnabled(false);
		        	queryNameTextField.setEditable(false);
		        	queryTextArea.setEditable(false);		        	
		        }
			}
		} else {
			DefaultListModel listModel = (DefaultListModel) hashtableDataSource.get(dataSourceName);
			queryList.setModel(listModel);
	        if (listModel.size()==0) {
	        	queryNameTextField.setText("");
	        	queryTextArea.setText("");
	        	designQueryButton.setEnabled(false);
	        	deleteQueryButton.setEnabled(false);
	        	queryNameTextField.setEditable(false);
	        	queryTextArea.setEditable(false);
	        	
	        }
		}
		
	}



	private void setDatasourceComboBox() {
		hashtableDataSource = new Hashtable();
		Vector vector =helper.getDataSources();
		if (vector != null) {
			externalDataSources = (ExternalDataSource[]) vector.toArray(new ExternalDataSource[vector.size()]); 
			String[] externalDataSourcesNames = new String[externalDataSources.length];
			for (int i = 0; i < externalDataSources.length; i++) {
				externalDataSourcesNames[i] =  externalDataSources[i].getName();				
			}
			dataSourceComboBox.setModel( new DefaultComboBoxModel(externalDataSourcesNames));
			dataSourceComboBox.setSelectedItem(externalDataSourcesNames[0]);
			setQueryList((String)dataSourceComboBox.getSelectedItem());
		}
	}
	

    protected void okButtonActionPerformed(ActionEvent evt) {
    	
    	Set keys = hashtableDataSource.keySet();
    	Vector queriesToInsert = new Vector();
    	Vector queriesToModify = new Vector();

    	// Actualizamos la query en el caso de que sea distinta la almacenada de la del cuadro de texto:
    	QueryListItem itemFinal = (QueryListItem) queryList.getSelectedValue();
    	Query query = itemFinal.getQuery();
    	if (!query.getText().equals(queryTextArea.getText())){
    		query.setText(queryTextArea.getText());
    		helper.updateQuery(query);
    	}
    	
    	for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String dataSourceName = (String) iterator.next();
			DefaultListModel listModel = (DefaultListModel) hashtableDataSource.get(dataSourceName);
			if (listModel!=null) {
				int length  = listModel.getSize();

				for (int i = 0; i < length; i++) {
					QueryListItem item = (QueryListItem) listModel.get(i);
								
					if (item.isNewQuery()) {								
						helper.insertQuery(item.getQuery(), dataSourceName);						
					}
				}
			}
		}
    	dispose();		
	}



	protected void cancelButtonActionPerformed(ActionEvent evt) {
		dispose();
		
	}

	private void designQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {
		QueryModel queryModel = null;

		int index = dataSourceComboBox.getSelectedIndex();
		
		int querySize = queryList.getModel().getSize();
		QueryListItem item = null;
		if (querySize != queryList.getSelectedIndex()){
			item = (QueryListItem) queryList.getSelectedValue();
		}
		
        if (queryTextArea.getText().trim().equals("")) {
        	queryModel = helper.designQuery(this,externalDataSources[index]);
        }
        else {
    		QueryModel oldModel = helper.buildQueryModel(queryTextArea.getText());
    		queryModel = helper.designQuery(this,externalDataSources[index],oldModel);
        }

        if (queryModel!=null) {
        	if (origen.equals(EDITORGIS)){
	        	Query query = helper.configureFields(this,queryModel, item);
	        	if (query==null) {
	        		dispose();
	        		return;
	        	}
	        	String queryText = queryModel.toString(true);
	        	queryTextArea.setText(queryText);
	        	QueryListItem itemFinal = (QueryListItem) queryList.getSelectedValue();
	        	itemFinal.getQuery().setExternalTable(query.getExternalTable());
	        	itemFinal.getQuery().setExternalColumn(query.getExternalColumn());
	        	itemFinal.getQuery().setInternalLayer(query.getInternalLayer());
	        	itemFinal.getQuery().setInternalAttribute(query.getInternalAttribute());
	        	itemFinal.getQuery().setText(queryText);
	        	itemFinal.setModifyQuery(true);
        	}
        	else{
        		if (origen.equals(GESTORCAPAS)){
        			String queryText = queryModel.toString(true);
    	        	queryTextArea.setText(queryText);
    	        	
        			QueryUtility queryUtility = new QueryUtility();
        			
        			String nombreTabla = item.toString().replace(" ", "");

        			queryUtility.creaTabla(nombreTabla);
        			queryUtility.creaColumnas(queryModel.getQueryExpression().toString(), nombreTabla, externalDataSources[index]);
        			
        			
        		}
        	}
        }
        
    }                                                 

    private void deleteQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                  
    	QueryListItem item = (QueryListItem) queryList.getSelectedValue();
    	if (!item.isNewQuery()) {
      		 int option = JOptionPane.showConfirmDialog(this, I18N.get("ConfigureQueryExternalDataSource.consulta.advertencia"));
       		 if (option == JOptionPane.OK_OPTION) {
					//System.out.println(item.getQuery().getDataSourceId());
					//System.out.println(item.getQuery().getQueryId());
					QueryDAO queryDAO = new QueryDAO();
					queryDAO.deleteQuery(item.getQuery().getQueryId());
					//System.out.println("Borrar Query" + item);
       		 } 
       		 else {
       			 return;
       		 }
       		 
    	}
    	DefaultListModel model = (DefaultListModel) queryList.getModel();
    	int index = queryList.getSelectedIndex();
    	deleteAction = true;
    	model.remove(index);
    	deleteAction = false;
    	queryList.setModel(model);
    	int size = queryList.getModel().getSize();
    	if (size>0) {
    		if (index>0) {
    			queryList.setSelectedIndex(index - 1);
    		}
    		else if (index==0){
    			queryList.setSelectedIndex(0);
    		}
    	}
 
    }                                                 

    private void newQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {    
    	
    	DefaultListModel model = (DefaultListModel) queryList.getModel();
    	int size = model.getSize();
    	Query query = new Query();
    	query.setName(I18N.get("ConfigureQueryExternalDataSource.consulta.nueva"));
    	query.setText("");
    	

    	model.add(size, new QueryListItem(query,true,false)); 
    	queryList.setSelectedIndex(size);
    	queryNameTextField.setText(I18N.get("ConfigureQueryExternalDataSource.consulta.nueva"));
    	queryNameTextField.requestFocus();
    	designQueryButton.setEnabled(true);
    	deleteQueryButton.setEnabled(true);
    	queryNameTextField.setEditable(true);
    	queryTextArea.setEditable(true);
    }                                              

    private void queryListValueChanged(javax.swing.event.ListSelectionEvent evt) {
    	
    	int querySize = queryList.getModel().getSize();
    	listValueChanged = true;
    	if ((queryList.getModel().getSize() != 0)&&(!deleteAction)) {
    			//System.out.println(queryList.getSelectedIndex());
    			QueryListItem item = null;
    			if (querySize != queryList.getSelectedIndex())
    				item = (QueryListItem) queryList.getSelectedValue();
    			
			    if (item != null) {
			    	queryNameTextField.setText(item.getQuery().getName());
			    	listValueChanged = false;
			    	queryTextArea.setText(item.getQuery().getText());
			    }
		}else {
			if ((queryList.getModel().getSize() == 0)) {   
    		    queryNameTextField.setText("");
    		    queryNameTextField.setEditable(false);
    		    listValueChanged = false;
    		    queryTextArea.setText("");
    		    queryTextArea.setEditable(false);
    		    designQueryButton.setEnabled(false);
    		    deleteQueryButton.setEnabled(false);
			}
		}

    	

    	
    	
    }                                      

    private void dataSourceComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {    
    	String dataSourceName = (String) evt.getItem();
    	setQueryList(dataSourceName);
    	if (queryList.getModel().getSize() > 0) {
    		queryList.setSelectedIndex(0);
        	queryNameTextField.setEditable(true);
        	queryTextArea.setEditable(true);
		    designQueryButton.setEnabled(true);
		    deleteQueryButton.setEnabled(true);
    	}
    	else {
        	queryNameTextField.setEditable(false);
        	queryTextArea.setEditable(false);
		    designQueryButton.setEnabled(false);
		    deleteQueryButton.setEnabled(false);		    
    	}
    }                                           
    
                      
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton deleteQueryButton;
    private javax.swing.JButton designQueryButton;
    private javax.swing.JComboBox dataSourceComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton newQueryButton;
    private javax.swing.JButton okButton;
    private javax.swing.JTextArea queryTextArea;

    private Hashtable hashtableDataSource;
    


    class MyDocumentListener implements DocumentListener {
    	public void insertUpdate(DocumentEvent e) {
    		if (!listValueChanged)
    			update(e);
    	}
    	public void removeUpdate(DocumentEvent e) {
    		if (!listValueChanged)
    			update(e);
    	}

    	private void update(DocumentEvent e) {
    		Document document = e.getDocument();
    		//System.out.println(document.getLength());
    		try {
    			if (queryList.getModel().getSize()>0) {
    				String newText = document.getText(0, document.getLength());
    				int index =  queryList.getSelectedIndex();
    				DefaultListModel listModel = (DefaultListModel) queryList.getModel();
    				QueryListItem item = (QueryListItem)listModel.get(index);
    				if (newText.equals("")){
						newText = " ";
    				}
    				item.setModifyQuery(true);
    				item.getQuery().setName(newText);
    				//System.out.println(item);
    				listModel.set(index, item);
    			}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
		public void changedUpdate(DocumentEvent arg0) {

			
		}
    }    
}