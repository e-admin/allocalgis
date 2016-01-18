/**
 * DatosExternosPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.geopista.app.layerutil.datosexternos;


/**
 * Panel que permite realizar operaciones de manipulación sobre las layers
 * de GeoPISTA
 * 
 * @author cotesa
 *
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import nickyb.sqleonardo.querybuilder.QueryModel;

import com.geopista.app.AppContext;
import com.geopista.app.layerutil.IGestorCapas;
import com.geopista.app.layerutil.dbtable.TablesDBOperations;
import com.geopista.feature.Table;
import com.geopista.ui.plugin.external.ConfigureQueryHelper;
import com.geopista.ui.plugin.external.ExternalDataSource;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class DatosExternosPanel extends JPanel implements FeatureExtendedPanel, TreeSelectionListener{
      
	private JPanelCamposExternos jPanelCE = null;
	private JPanelFuentesDatos jPanelFD = null;
	private JRadioButton rdbModoCreacion = null;
    private JRadioButton rdbModoModificacion = null;
    private JRadioButton rdbModoEliminacion = null;
    private ButtonGroup btnGroup = null;
    private JPanel jPanelRadio = null;
    
    private JComboBox cmbAsociacion = null;
    private JLabel lblAsociacion = null;
    
    private JLabel lblNombreAsociacion = null;
    private JTextField txtNombreAsociacion = null;
           
    private boolean tablaExternaNueva = false;
    
    private JButton btnGrabar = null;
    private JButton btnSalir = null;
    private JButton btnEliminarLayer = null;
    
    private JTextArea txtAreaSQL = null;
    private JScrollPane scrollAreaSQL = null;
        
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private ExternalDataSource externalDS = null;
    
    private static Color COLOR_HABILITADO = Color.BLACK;
    private static Color COLOR_DESHABILITADO = Color.GRAY;
    
    String string1 = I18N.get("GestorCapas","general.si"); 
    String string2 = I18N.get("GestorCapas","general.no"); 
    Object[] options = {string1, string2};
    
    /**
     * Evita que al salir de la pestaña se pregunte dos veces si se desea abandonar
     */    
    private boolean nosale = false;
    
    private Blackboard Identificadores = aplicacion.getBlackboard();
    
    /**
     * This is the default constructor
     */
    public DatosExternosPanel()
    {
        super();
        initialize();
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
        
    public void initialize(){        
        
        lblAsociacion = new JLabel();
        lblAsociacion.setBounds(new Rectangle(370,19,155,24));
        lblAsociacion.setText(I18N.get("GestorCapas","datosExternos.lblAsociacion"));       
        
        lblNombreAsociacion = new JLabel();
        lblNombreAsociacion.setBounds(new Rectangle(370,60,155,24));
        lblNombreAsociacion.setText(I18N.get("GestorCapas","datosExternos.lblNombreAsociacion"));
                
        //Asocia los botones en un ButtonGroup
        btnGroup = new ButtonGroup();
        btnGroup.add(getRdbModoCreacion());
        btnGroup.add(getRdbModoModificacion());
        btnGroup.add(getRdbModoEliminacion());
                        
        jPanelRadio = getJPanelRadio();
        jPanelRadio.setLayout(null);
        jPanelRadio.setBounds(new Rectangle(14,15,300,57));
        jPanelRadio.add(getRdbModoCreacion(), null);
        jPanelRadio.add(getRdbModoModificacion(), null);
        jPanelRadio.add(getRdbModoEliminacion(), null);

        txtAreaSQL = getTxtAreaSQL();
 
        scrollAreaSQL = getScrollSql();
                
        this.setLayout(null);
        this.setSize(937, 607);
        

        this.add(lblNombreAsociacion, null);   
       
        this.add(jPanelRadio, null);
        this.add(getTxtNombreAsociacion(), null);

        this.add(getScrollSql(), null);
        
        this.add(getBtnGrabar(), null);
        this.add(getBtnEliminarLayer(), null);
               
        this.add(getJPanelRadio(), null);
        this.add(getBtnSalir(), null);

        this.add(getCmbAsociacion(), null);

        this.add(lblAsociacion, null);
        
        jPanelFD = null;
        jPanelFD = getJPanelFuentesDatos();
        jPanelCE = null;
        jPanelCE = getJPanelCamposExternos();
        
        this.add(jPanelCE);      
        this.add(jPanelFD);
        
        apariencia();
    }
    
    private void reset(){
    	
    	lblAsociacion = null;

    	lblNombreAsociacion = null;

    	btnGroup = null;
    	getRdbModoCreacion().removeAll();
    	rdbModoCreacion = null;
    	getRdbModoModificacion().removeAll();
    	rdbModoModificacion = null;
    	getRdbModoEliminacion().removeAll();
    	rdbModoEliminacion = null;
    	getJPanelRadio().removeAll();
    	jPanelRadio = null;
    	txtAreaSQL = null;


    	getCmbAsociacion().removeAll();
    	cmbAsociacion = null;

    	getTxtNombreAsociacion().removeAll();
    	txtNombreAsociacion = null;

    	getScrollSql().removeAll();
    	scrollAreaSQL = null;
    	   
    	jPanelCE.removeAll();
    	jPanelFD.removeAll();
    	this.removeAll();
    	
    }
    
    
    /**
     * This method initializes rdbModo  
     *  
     * @return javax.swing.JRadioButton 
     */
    private JRadioButton getRdbModoCreacion(){
        if (rdbModoCreacion == null){
            rdbModoCreacion = new JRadioButton();
            rdbModoCreacion.setText(I18N.get("GestorCapas","datosExternos.crear"));
            rdbModoCreacion.setBounds(new Rectangle(1,1,300,15));
            rdbModoCreacion.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    //Llamar al metodo correspondiente
                    tablaExternaNueva = true;                    
                    controlarAspecto(tablaExternaNueva);
                }
            });
        }
        return rdbModoCreacion;
    }
    
    /**
     * This method initializes rdbModoModificacion  
     *  
     * @return javax.swing.JRadioButton 
     */
    private JRadioButton getRdbModoModificacion(){
        if (rdbModoModificacion == null){
            rdbModoModificacion = new JRadioButton();
            rdbModoModificacion.setText(I18N.get("GestorCapas","datosExternos.modificar"));
            rdbModoModificacion.setBounds(new Rectangle(1,21,300,15));
            rdbModoModificacion.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){   
                    tablaExternaNueva = false;
                    controlarAspecto(tablaExternaNueva);
                }
            });
        }
        return rdbModoModificacion;
    }
    
    /**
     * This method initializes rdbModoEliminacion  
     *  
     * @return javax.swing.JRadioButton 
     */
    private JRadioButton getRdbModoEliminacion(){
        if (rdbModoEliminacion == null){
            rdbModoEliminacion = new JRadioButton();
            rdbModoEliminacion.setText(I18N.get("GestorCapas","datosExternos.eliminar"));
            rdbModoEliminacion.setBounds(new Rectangle(1,41,300,15));
            rdbModoEliminacion.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){                    
                	tablaExternaNueva = false;
                    controlarAspecto(tablaExternaNueva);                    
                }
            });
        }
        return rdbModoEliminacion;
    }
    
    
    /**
     * This method initializes jPanelRadio  
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelRadio(){
        if (jPanelRadio == null){
            jPanelRadio = new JPanel();
            jPanelRadio.setBounds(new Rectangle(266,5,10,10));
        }
        return jPanelRadio;
    }
    
    public JPanelFuentesDatos getJPanelFuentesDatos(){
        if (jPanelFD == null){
        	jPanelFD = new JPanelFuentesDatos(this);
        	jPanelFD.setBounds(new Rectangle(20,90,850, 175));
        	jPanelFD.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","datosExternos.panelFuentesDatos.titulo")));
        }
        return jPanelFD;
    }
    
    
    public JPanelCamposExternos getJPanelCamposExternos(){
        if (jPanelCE == null){
        	jPanelCE = new JPanelCamposExternos(this);
        	jPanelCE.setBounds(new Rectangle(20,275,850,185));
        	jPanelCE.setBorder(BorderFactory.createTitledBorder(I18N.get("GestorCapas","datosExternos.panelCamposExternos.titulo")));
        }
        return jPanelCE;
    }
    
    /**
     * This method initializes cmbAsociacion   
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getCmbAsociacion(){
        if (cmbAsociacion == null){
            cmbAsociacion = new JComboBox();
            cmbAsociacion.setBounds(new Rectangle(500,19,180,20));
            
            /*cmbAsociacion.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){   
                    cmbAsociacion_actionPerformed(e);
                }
            });*/
        }
        return cmbAsociacion;
    }
    
    /**
     * This method initializes txtNombreAsociacion    
     *  
     * @return javax.swing.JTextField   
     */
    public JTextField getTxtNombreAsociacion(){
        if (txtNombreAsociacion == null){
            txtNombreAsociacion = new JTextField();
            txtNombreAsociacion.setText("");
            txtNombreAsociacion.setBounds(new java.awt.Rectangle(500,60,180,20));
            
            txtNombreAsociacion.addCaretListener(new CaretListener() {
                public void caretUpdate(CaretEvent evt) {
                    txtNombreAsociacion.getText();
                }
            });
        }
        return txtNombreAsociacion;
    }
       
    /**
     * This method initializes scrollSQL  
     *  
     * @return javax.swing.JScrollPane  
     */
    private JScrollPane getScrollSql()
    {
        scrollAreaSQL = new JScrollPane();
        scrollAreaSQL.setBounds(new Rectangle(21,470,850,110));
        
        JTextArea txtAreaSQL = getTxtAreaSQL();
        scrollAreaSQL.setViewportView(txtAreaSQL);
        
        return scrollAreaSQL;
    }
    
    /**
     * This method initializes txtAreaSQL   
     *  
     * @return javax.swing.JTextArea    
     */
    public JTextArea getTxtAreaSQL(){
        if (txtAreaSQL == null)
        {
            txtAreaSQL = new JTextArea();
            txtAreaSQL.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        return txtAreaSQL;
    }      
        
    /**
     * This method initializes btnGrabar    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnGrabar(){
        if (btnGrabar == null){
            btnGrabar = new JButton();
            btnGrabar.setBounds(new Rectangle(875,523,100,25));
            btnGrabar.setText(I18N.get("GestorCapas","general.boton.grabar"));
            btnGrabar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	if ((!txtNombreAsociacion.getText().equals(""))&&(!txtAreaSQL.getText().equals(""))&&(externalDS!=null)){
                		btnGrabar_actionPerformed(e);
                	}
                	else{
        				lanzarMensaje("", I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgGrabar.contenido2"), 2);
                	}
                }
            });
        }
        return btnGrabar;
    }
    
    /**
     * This method initializes btnGrabar    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnEliminarLayer(){
        if (btnEliminarLayer == null){
            btnEliminarLayer = new JButton();
            btnEliminarLayer.setBounds(new Rectangle(875,491,100,25));
            btnEliminarLayer.setText(I18N.get("GestorCapas","general.boton.eliminar"));
            btnEliminarLayer.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    btnEliminarLayer_actionPerformed(e);
                }
            });
        }
        return btnEliminarLayer;
    }
    
    private void btnEliminarLayer_actionPerformed(ActionEvent e){
    	
    	int n = JOptionPane.showOptionDialog(this, I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgEliminar.contenido2"),
                "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
               
        if (n==JOptionPane.YES_OPTION){  	
			QueryUtility queryUtility = new QueryUtility();
			
			String nombreTabla = txtNombreAsociacion.getText().toString().replace(" ", "");		
			
			Table table = queryUtility.obtenerTabla(nombreTabla);
			
			
			DataSourceTables dataSourceTables = new DataSourceTables();
			dataSourceTables.getDataSourceTables(nombreTabla);
			dataSourceTables.setTable(table);
			
			boolean eliminado = dataSourceTables.eliminarDataSourceTablesCompleto(dataSourceTables.getId_datasource_tables());
			cargarAsociacion(true);
			
			Identificadores.put("ColumnasBorradas", true);
			Identificadores.put("TablasModificadas", true);
			Identificadores.put("LayersActualizada", false);
	        Identificadores.put("TablasDominiosActualizada", false);
	        
			if (eliminado)
				lanzarMensaje("", I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgEliminar.contenido"), 1);
			else
				lanzarMensaje("", I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgEliminar.fallo"), 2);
        }
    }
    
    /**
     * This method initializes btnSalir 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getBtnSalir(){
        if (btnSalir == null){
            btnSalir = new JButton();
            btnSalir.setBounds(new Rectangle(875,555,100,25));
            btnSalir.setText(I18N.get("GestorCapas","general.boton.salir"));
            btnSalir.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    jButtonSalirActionPerformed(); 
                }
            });
        }
        return btnSalir;
    }
    
    /**
     * Controla el aspecto de la pantalla, de acuerdo al tipo de operación que 
     * se esté realizando en cada momento
     * @param esCreacion Verdadero si la operación a realizar es la de creación de una
     * nueva capa
     */
    private void controlarAspecto (boolean esCreacion){
      
        if (cmbAsociacion.getSelectedIndex()!=-1)
            cmbAsociacion.setSelectedIndex(0);
        
        apariencia();

        txtNombreAsociacion.setText("");
        txtAreaSQL.setText("");
        jPanelCE.remove();
        jPanelCE.cargarAtributos();
        
        cargarAsociacion(!esCreacion);        
    }
    
    /**
     * Carga las layerfamilies en el combo de layerfamilies
     * @param realizarOperacion Verdadero si se desea realizar la operación
     */
    private void cargarAsociacion(boolean realizarOperacion)
    {
        
        ArrayList lstDSExternal = new ArrayList();
        
        if (realizarOperacion){
            
            DataSourceTables dsTables = new DataSourceTables(); 
           	lstDSExternal = dsTables.getDataSourceTables();      
            cmbAsociacion = getCmbAsociacion();
            cmbAsociacion.removeAllItems();         

            cmbAsociacion.addItem("");	
			//cmbAsociacion.addItem(dsTables);
			Iterator it = lstDSExternal.iterator();
			while (it.hasNext()){        
               //cmbAsociacion.addItem(((DataSourceTables) it.next()));
				String tableName=((DataSourceTables) it.next()).getTable().getName();
               cmbAsociacion.addItem(tableName);	
            } 
			if (lstDSExternal.size()>0)
				cmbAsociacion.setSelectedIndex(0);
			
		   cmbAsociacion.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){   
                    cmbAsociacion_actionPerformed(e);
                }
            });
        }
    }
 
    /**
     * Acción que se lanza cada vez que se modifica la selección de una Asociacion
     * dentro del combo de layerfamilies
     * @param e
     */
    private void cmbAsociacion_actionPerformed(ActionEvent e){
    	if (cmbAsociacion.getSelectedItem()!=null && !(cmbAsociacion.getSelectedItem().toString().equals(""))){
	   		txtNombreAsociacion.setText(cmbAsociacion.getSelectedItem().toString());
			
			
			DataSourceTables dsTables = new DataSourceTables(); 
			dsTables.getDataSourceTables(cmbAsociacion.getSelectedItem().toString());
			externalDS = dsTables.getExternalDS();
			jPanelFD.getTxtCadenaConexion().setText(dsTables.getExternalDS().getConnectString());
			jPanelFD.getTxtDriver().setText(dsTables.getExternalDS().getDriver());
			jPanelFD.getTxtPassword().setText(dsTables.getExternalDS().getPassword());
			jPanelFD.getTxtUserName().setText(dsTables.getExternalDS().getUserName());
	   
			ConfigureQueryHelper query = new ConfigureQueryHelper();
			QueryModel queryModel = query.buildQueryModel(dsTables.getFetchQuery());
			Hashtable ht = jPanelCE.getExternalMetaData(queryModel);
			if (ht!=null){
				jPanelCE.asignaColumnas(ht, dsTables.getFetchQuery(), dsTables.getExternalDS());
				jPanelCE.cargarAtributos();
				txtAreaSQL.setText(dsTables.getFetchQuery());
			}
    	}
    }

    /**
     * Acción realizada al pulsar el botón Salir
     *
     */
    private void jButtonSalirActionPerformed()
    {
        int n = JOptionPane.showOptionDialog(this,
                I18N.get("GestorCapas","general.salir.mensaje"),
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if (n==JOptionPane.NO_OPTION) return;
        
        aplicacion.getMainFrame().dispose();
        System.exit(0);        
    }
    
 
    /**
     * Acción realizada al pulsar el botón Grabar
     * @param e
     */
    private void btnGrabar_actionPerformed(ActionEvent e){
    	
    	 final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
         progressDialog.setTitle(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.reloj.titulo"));
         progressDialog.report(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.reloj.contenido"));

         progressDialog.addComponentListener(new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 new Thread(new Runnable(){
                     public void run(){
                         try{

                         	QueryUtility queryUtility = new QueryUtility();
                         	TablesDBOperations operaciones = new TablesDBOperations();
                         	String nombreTabla = txtNombreAsociacion.getText().toString().replace(" ", "");
                         	if (getSelectedRadioButton(btnGroup) == rdbModoModificacion){
                         		if (operaciones.existeTabla(nombreTabla)){
                         			DataSourceTables dataSourceTables = new DataSourceTables();
                         			
                         			Table table = queryUtility.obtenerTabla(nombreTabla);
                         			dataSourceTables.getDataSourceTables(nombreTabla);
                         			dataSourceTables.setTable(table);
                         			boolean eliminado = dataSourceTables.eliminarDataSourceTablesCompleto(dataSourceTables.getId_datasource_tables());
                         		}
                         	}
                         	if (!operaciones.existeTabla(nombreTabla)){
                     	
                     			queryUtility.creaTabla(nombreTabla);
                     	
                     			if (queryUtility.creaColumnas(txtAreaSQL.getText(), nombreTabla, externalDS)){		
                     				Table table = queryUtility.obtenerTabla(nombreTabla);
                     				DataSourceTables dataSourceTables = new DataSourceTables();
                     				dataSourceTables.setExternalDS(externalDS);
                     				dataSourceTables.setTable(table);
                     				dataSourceTables.setFetchQuery(txtAreaSQL.getText());
                     				dataSourceTables.insertDataSourceTables();
                     						
                     				Identificadores.put("ColumnasBorradas", true);
                     				Identificadores.put("TablasModificadas", true);
                     				Identificadores.put("LayersActualizada", false);
                     		        Identificadores.put("TablasDominiosActualizada", false);
                     		        
                     		    	lanzarMensaje("", I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgGrabar.contenido"), 1);
                     		    	enter();
                     			}
                     			else{
                     				lanzarMensaje("", I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgGrabar.error"), 2);
                     			}
                         	}
                         	else{
                         		lanzarMensaje("", I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgGrabar.error"), 2);
                         	}
                         }
                         catch(Exception e){
                             ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
                             return;
                         }
                         finally{
                             progressDialog.setVisible(false);
                         }
                     }
               }).start();
           }
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        
        

    } 
    
    /**
     *  Lanza un mensaje 
     * @param titulo Título de la pantalla 
     * @param mensaje Mensaje a mostrar
     * @param tipo Tipo de ventana a lanzar
     */
    public void lanzarMensaje(String titulo,String mensaje, int tipo){
        JOptionPane optionPane= new JOptionPane(mensaje, tipo);
        JDialog dialog =optionPane.createDialog(this, "");
        dialog.setTitle(titulo);
        dialog.show();        
    }
 
    
    /**
     * Habilita y deshabilita la zona inferior de la pantalla
     * @param isEnabled Verdadero si se desea habilitarla
     */
    
    private void apariencia(){

        if (getSelectedRadioButton(btnGroup) == rdbModoCreacion){
        	getBtnEliminarLayer().setEnabled(false); 
            getBtnGrabar().setEnabled(true);
        	getTxtAreaSQL().setEnabled(true);
        	getTxtNombreAsociacion().setEnabled(true);
        	lblAsociacion.setForeground(COLOR_DESHABILITADO);
            lblNombreAsociacion.setForeground(COLOR_HABILITADO); 
            cmbAsociacion.setEnabled(false);
            
        	jPanelFD.getBtnDataSource().setEnabled(true);
            jPanelFD.getJListFuentesDatos().setForeground(COLOR_HABILITADO);
            jPanelFD.getJListFuentesDatos().setEnabled(true);
            
        	jPanelCE.getBtnQueryDSLayer().setEnabled(true);
        	jPanelCE.getTblCamposExternos().setEnabled(false);

        }
        else{
            if (getSelectedRadioButton(btnGroup) == rdbModoEliminacion){
                getBtnEliminarLayer().setEnabled(true); 
                getBtnGrabar().setEnabled(false);
            	getTxtAreaSQL().setEnabled(false);
            	getTxtNombreAsociacion().setEnabled(false);
            	lblAsociacion.setForeground(COLOR_DESHABILITADO);
                lblNombreAsociacion.setForeground(COLOR_DESHABILITADO);
                cmbAsociacion.setEnabled(true);
                
            	jPanelFD.getBtnDataSource().setEnabled(false);
                jPanelFD.getJListFuentesDatos().setForeground(COLOR_DESHABILITADO);
                jPanelFD.getJListFuentesDatos().setEnabled(false);
                
            	jPanelCE.getBtnQueryDSLayer().setEnabled(false);
            	jPanelCE.getTblCamposExternos().setEnabled(false);
            }
            else{
                if (getSelectedRadioButton(btnGroup) == rdbModoModificacion){
                	getBtnEliminarLayer().setEnabled(false);
                	getBtnGrabar().setEnabled(true);
                	getTxtAreaSQL().setEnabled(true);
                	getTxtNombreAsociacion().setEnabled(false);
                	lblAsociacion.setForeground(COLOR_HABILITADO);
                    lblNombreAsociacion.setForeground(COLOR_DESHABILITADO);
                    cmbAsociacion.setEnabled(true);
                	
                	jPanelFD.getBtnDataSource().setEnabled(false);
                	jPanelFD.getJListFuentesDatos().setForeground(COLOR_DESHABILITADO);
                    jPanelFD.getJListFuentesDatos().setEnabled(false);
                    
                	jPanelCE.getBtnQueryDSLayer().setEnabled(true);
                	jPanelCE.getTblCamposExternos().setEnabled(false);
                }
                else{
                	getBtnEliminarLayer().setEnabled(false);
                	getBtnGrabar().setEnabled(false);
                	getTxtNombreAsociacion().setEnabled(false);
                	lblAsociacion.setForeground(COLOR_DESHABILITADO);
                    lblNombreAsociacion.setForeground(COLOR_DESHABILITADO);
                    cmbAsociacion.setEnabled(false);
                }
            }        	
        }
    }
    
    /**
     * Devuelve el radiobutton seleccionado del buttongrop
     * @param group ButtonGroup
     * @return JRadioButton seleccionado dentro del grupo de botones
     */
    public static JRadioButton getSelectedRadioButton(ButtonGroup group){
        if (group !=null){
        	for (Enumeration e=group.getElements(); e.hasMoreElements(); ){
        		JRadioButton b = (JRadioButton)e.nextElement();
        		if (b.getModel() == group.getSelection()){
        			return b;
        		}
        	}
        }
       	return null;
        
    }
    
    
    /**
     * Acciones realizadas al entrar en el panel
     */
    public void enter(){
        nosale = true;
        
        if (!(this.getComponentCount() > 0)){
            initialize();
        }
        
        try{
            // Iniciamos la ayuda
        	String helpHS = "help/catastro/gestordecapas/GestorCapasHelp_es.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this,"Pestania4Capas", hs);
        } 
        catch (Exception excp){
            excp.printStackTrace();
        }
    }
    
    /**
     * Acciones realizadas al salir del panel
     */
    public void exit(){ 
        if (nosale && getSelectedRadioButton(btnGroup)!=null){
            int n = JOptionPane.showOptionDialog(this, I18N.get("GestorCapas","datosExternos.panelFuentesDatos.msgSalir"),
                    "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
            
            //Sale de la pestaña
           // LayersPanel lp = null;
            
            if (n==JOptionPane.YES_OPTION){
                Container c = this.getRootPane().getParent();
                
                if (c!=null && c instanceof IGestorCapas){
                    reset();                    
                }                
                return;                
            }
            //Se queda en esta pestaña
            else{            
                Container c = this.getRootPane().getParent();
                
                if (c!=null && c instanceof IGestorCapas){
                	//int indice =   ((GestorCapas)c).getPestanaTables().indexOfComponent(this);
                	int indice =   ((IGestorCapas)c).indexOfComponent(this);
                	
                    c.getPropertyChangeListeners();
                    nosale=false;
                    //((GestorCapas)c).getPestanaTables().setSelectedIndex(indice);
                    ((IGestorCapas)c).setSelectedIndex(indice);
                                        
                    //((GestorCapas)c).getPestanaTables().updateUI();
                    ((IGestorCapas)c).updateUI();
                    
                    //((GestorCapas)c).getPestanaTables().repaint();
                    ((IGestorCapas)c).repaint();
                    this.updateUI();
                    this.repaint();                    
                }
            }
        }
        
    }

	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public ExternalDataSource getExternalDS() {
		return externalDS;
	}

	public void setExternalDS(ExternalDataSource externalDS) {
		this.externalDS = externalDS;
	}

}
