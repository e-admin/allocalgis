package com.geopista.app.layerutil.datosexternos;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nickyb.sqleonardo.querybuilder.QueryModel;
import nickyb.sqleonardo.querybuilder.syntax.QueryExpression;
import nickyb.sqleonardo.querybuilder.syntax.QuerySpecification;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens.Column;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens._Expression;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.ui.plugin.external.ConfigureQueryDialog;
import com.geopista.ui.plugin.external.ConfigureQueryHelper;
import com.geopista.ui.plugin.external.ConnectionUtility;
import com.geopista.ui.plugin.external.ExternalDataSource;
import com.vividsolutions.jump.I18N;

public class JPanelCamposExternos extends JPanel {
	
    private JScrollPane scrollCamposExternos = null;
    private JTable tblCamposExternos = null;
    private TablaCamposExternosModel tablemodel= new TablaCamposExternosModel();
    private String attGeometryName;
    private DatosExternosPanel datosExternosPanel;
    private Vector vectorTabla = new Vector();
    
    private JButton btnQueryDSLayer = null;
    
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    public JPanelCamposExternos(DatosExternosPanel datosExternosPanel){
    	super();
    	
    	// Cargamos los drivers de las BBDD para las conexiones a los datos externos:
    	Hashtable drivers = ConnectionUtility.findAllDrivers();
		ConnectionUtility.loadDrivers(drivers);
    	this.datosExternosPanel = datosExternosPanel;
    	initialize();
    }
	
    public void remove(){
    	vectorTabla.removeAllElements();
    }
    
    private void initialize(){  
    	this.setLayout(null);
        
        scrollCamposExternos = getScrollCamposExternos();
        this.add(scrollCamposExternos, null);
        this.add(getBtnQueryDSLayer(), null);
        getBtnQueryDSLayer().setEnabled(false);
        
    }
    
    /**
     * This method initializes btnQueryDSLayer    
     *  
     * @return javax.swing.JButton  
     */
    public JButton getBtnQueryDSLayer(){
        if (btnQueryDSLayer == null){
            btnQueryDSLayer = new JButton();
            btnQueryDSLayer.setBounds(new Rectangle(400,145,100,25));
            btnQueryDSLayer.setText(I18N.get("GestorCapas","datosExternos.boton.queryDS"));
            btnQueryDSLayer.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	if (datosExternosPanel.getExternalDS()!=null){
                		btnQueryDSLayer_actionPerformed(e);
                	}
                	else{
                		datosExternosPanel.lanzarMensaje("", I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.contenido"), 2);
                	}
                }
                    });
        }
        return btnQueryDSLayer;
    }   
    
    /**
     * Acción realizada al pulsar el botón de añadir datos externos
     * @param e
     */
    private void btnQueryDSLayer_actionPerformed(ActionEvent e){
//    	JPanelFuentesDatos jPanelFD = new JPanelFuentesDatos();
    	
		QueryModel queryModel = null;

		ExternalDataSource externalDS = datosExternosPanel.getExternalDS();
		if (ConnectionUtility.testConnection(externalDS.getDriver(), externalDS.getConnectString(), externalDS.getUserName(), externalDS.getPassword())){
			ConfigureQueryHelper helper = new ConfigureQueryHelper();
			ConfigureQueryDialog configureQD = new ConfigureQueryDialog(aplicacion.getMainFrame(), "Configure Query", true, ConfigureQueryDialog.GESTORCAPAS);
			if (datosExternosPanel.getTxtAreaSQL().getText().trim().equals("")) {
	        	queryModel = helper.designQuery(configureQD, externalDS);
	        }
	        else {
	    		QueryModel oldModel = helper.buildQueryModel(datosExternosPanel.getTxtAreaSQL().getText());
	    		queryModel = helper.designQuery(configureQD,externalDS,oldModel);
	        }
	
	        if (queryModel!=null) {
	        	String queryText = queryModel.toString(true); 
	        	Hashtable ht = getExternalMetaData(queryModel);
	        	datosExternosPanel.getTxtAreaSQL().setText(queryText);       
	        	//asignaColumnas(queryText, externalDS);
	        	//asignaColumnas(ht);
	        	asignaColumnas(ht, queryText, externalDS);
	        	cargarAtributos();
	        }
		}
        else{
        	datosExternosPanel.lanzarMensaje("", I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgDS.error"), 2);
        }
    	
    }
    /**
     * Es una copia del método que aparece en ConfigureQueryHelper, se ha copiado porque es privado
     * @param queryModel
     * @return
     */
	public Hashtable getExternalMetaData(QueryModel queryModel) {
		 QueryExpression queryExpression = queryModel.getQueryExpression();
		 QuerySpecification querySpecification = queryExpression.getQuerySpecification();
		 _Expression[] expressions = querySpecification.getSelectList();
		 Hashtable hashtable = new Hashtable();
		 for (int i = 0; i < expressions.length; i++) {
			_Expression expression = expressions[i];
			if (expression instanceof QueryTokens.Column) {
				QueryTokens.Column column = (Column) expression;
				String tableIdentifier = column.getTable().getAlias();
				String columnName = column.getName();
				if (!hashtable.containsKey(tableIdentifier)) {
					Vector vector = new Vector();
					vector.add(null);
					vector.add(columnName);
					hashtable.put(tableIdentifier, vector);
				}
				else {
					Vector vector = (Vector) hashtable.get(tableIdentifier);
					vector.add(columnName);					
				}
			}			
		 }
		 if (hashtable.size() != 0){
			 return hashtable;
		 }
		 else {
			 return null;
		 }			 
	}
	
	public void asignaColumnas(Hashtable ht, String select, ExternalDataSource externalDataSource){
		remove();
		TablesDBOperations operaciones = new TablesDBOperations();
		Statement stmt;
		ResultSet rs;
		Connection connection = ConnectionUtility.getConnection(externalDataSource);
		if (connection!=null){
			try {
				stmt = connection.createStatement();
				rs = stmt.executeQuery(select);
				ResultSetMetaData rsmd = rs.getMetaData();
	
				if (rsmd!=null){
					int h = 1;
			    	Set set = ht.keySet();
			    	int i = 0;
			    	String nombreTabla;
			    	for (Iterator iterator = set.iterator(); iterator.hasNext();i++) {
						nombreTabla = (String) iterator.next();			
						Vector nombreColumnas = new Vector();
						
						if (nombreTabla!=null && !nombreTabla.equals("")){
							nombreColumnas = (Vector) ht.get(nombreTabla);
							for (int j=0; j<nombreColumnas.size(); j++){
					    		TablaCamposExternosRow tabla = new TablaCamposExternosRow();
								String nombreColumna = (String) nombreColumnas.get(j);
								if ((nombreColumna!=null) && (!nombreColumna.equals(""))){
									tabla.setOrigen(nombreTabla);
									tabla.setNombre(nombreColumna);
									tabla.setTipo(rsmd.getColumnTypeName(h));
									vectorTabla.add(tabla);
									h++;
								}
							}
						}
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
 
    /**
     * This method initializes scrollCamposExternos  
     *  
     * @return javax.swing.JScrollPane  
     */
    private JScrollPane getScrollCamposExternos()
    {
        if (scrollCamposExternos == null)
        {
            scrollCamposExternos = new JScrollPane();
            scrollCamposExternos.setBounds(new Rectangle(20,20,810,115));
            
            JTable table = getTblCamposExternos();
            
            scrollCamposExternos.setViewportView(table);
            //scrollCamposExternos.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","datosExternos.panelCamposExternos.titulo")));
        }
        return scrollCamposExternos;
    }  
    
    /**
     * This method initializes tblCamposExternos 
     *  
     * @return javax.swing.JTable   
     */
    public JTable getTblCamposExternos(){
        if (tblCamposExternos == null)
        {
            tblCamposExternos = new JTable(tablemodel);
        }

        tblCamposExternos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       
        ListSelectionModel rowSM = tblCamposExternos.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();

            }
        });       
        
        return tblCamposExternos;
    }  
    
    /**
     * Carga los atributos en la lista de atributos
     * @param vecAtributos Vector que contiene los atributos a cargar
     */
    public void cargarAtributos(){ 
    	
        ((TablaCamposExternosModel)tblCamposExternos.getModel()).setData(new TablaCamposExternosRow[0]);       
        ((TablaCamposExternosModel)tblCamposExternos.getModel()).setData((TablaCamposExternosRow[])vectorTabla.toArray(new TablaCamposExternosRow[vectorTabla.size()]));
        this.remove(scrollCamposExternos);
        this.add(getScrollCamposExternos());
        
        getScrollCamposExternos().updateUI();
        this.updateUI();
        this.repaint();
    }

}
