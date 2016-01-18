/*
 * NewJPanel.java
 *
 * Created on 18 de septiembre de 2007, 19:17
 */

package com.geopista.ui.plugin.external;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;

/**
 *
 * @author  arubio
 */


public class ExternalDataSourceDialog extends JDialog {    
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	
    private final static String NAME =aplicacion.getI18nString("ExternalDataSourcePlugin.Nombre");
    private final static String DRIVER = aplicacion.getI18nString("ExternalDataSourcePlugin.Driver");
    private final static String CONNECT_STRING = aplicacion.getI18nString("ExternalDataSourcePlugin.Conexion");
    private final static String USERNAME = aplicacion.getI18nString("ExternalDataSourcePlugin.Usuario");
    private final static String PASSWORD = aplicacion.getI18nString("ExternalDataSourcePlugin.Password");
    private final static String OK = aplicacion.getI18nString("btnAceptar");
    private final static String CANCEL = aplicacion.getI18nString("btnCancelar");
    private final static String EXTDSDIALOGTITLE = aplicacion.getI18nString("ExternalDataSourcePlugin.ExternalDataSourceDialog.Title");
    private final static String ERROR = aplicacion.getI18nString("ConfigureQueryExternalDataSource.dataSource.error");
    private final static String ERRORNOMBRE = aplicacion.getI18nString("ConfigureQueryExternalDataSource.dataSource.errorNombre");
    private final static String ERRORCADENACONEXION = aplicacion.getI18nString("ConfigureQueryExternalDataSource.dataSource.errorCadenaConexion");
    private Hashtable drivers;

	private boolean cancelButtonPressed;

	private boolean closed;
	
	public ExternalDataSourceDialog(final Frame frame, boolean modal,Hashtable drivers) {
	        super(frame, EXTDSDIALOGTITLE, modal);
	        try {
	            initComponents();
	            this.drivers = drivers;
	            generateDriverNames(drivers);
	            setActions();
	            setDrivers(driverNames);

	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
    

	private void generateDriverNames(Hashtable drivers) {
		Enumeration enumeration = drivers.keys();
		Vector driversName = new Vector();
		while (enumeration.hasMoreElements()) {
			String driver = (String) enumeration.nextElement();
			driversName.add(driver);
		}
		this.driverNames = (String[])driversName.toArray(new String[driversName.size()]);
	}

	private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField = new javax.swing.JTextField();

        getContentPane().setLayout(new java.awt.GridBagLayout());
        setSize(new Dimension(450,260));
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(null);

        jLabel1.setText(NAME);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(30, 30, 60, 30);

        jLabel2.setText(DRIVER);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(30, 60, 60, 30);

        jLabel3.setText(CONNECT_STRING);
        getContentPane().add(jLabel3);
        jLabel3.setBounds(30, 90, 80, 30);

        jLabel4.setText(USERNAME);
        getContentPane().add(jLabel4);
        jLabel4.setBounds(30, 120, 60, 30);

        jLabel5.setText(PASSWORD);
        getContentPane().add(jLabel5);
        jLabel5.setBounds(30, 150, 60, 30);

        getContentPane().add(jTextField1);
        jTextField1.setBounds(110, 35, 300, 20);

        getContentPane().add(jTextField2);
        jTextField2.setBounds(110, 125, 300, 20);

        getContentPane().add(jPasswordField1);
        jPasswordField1.setBounds(110, 155, 300, 20);

        jComboBox1.setMaximumSize(new java.awt.Dimension(163, 18));
        getContentPane().add(jComboBox1);
        jComboBox1.setBounds(110, 65, 300, 20);

        jButton1.setText(OK);
        getContentPane().add(jButton1);
        jButton1.setBounds(230, 190, 80, 23);

        jButton2.setText(CANCEL);
        getContentPane().add(jButton2);
        jButton2.setBounds(330, 190, 80, 23);

        getContentPane().add(jTextField);
        jTextField.setBounds(110, 95, 300, 20);
	    }

		private void setActions() {
	        addWindowListener(new java.awt.event.WindowAdapter() {
	            public void windowClosing(java.awt.event.WindowEvent evt) {
	                dialogClosing(evt);	                
	            }	            
	        });
	        
	        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
	            public void itemStateChanged(java.awt.event.ItemEvent evt) {
	                jComboBox1ItemStateChanged(evt);
	            }
	        });
	        
	        jButton1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton1ActionPerformed(evt);
	            }
	        });
	        
	        jButton2.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton2ActionPerformed(evt);
	            }
	        });
		}
	
	
	    protected void dialogClosing(WindowEvent evt) {
	    	closed = true;
	    }

		protected void jButton2ActionPerformed(ActionEvent evt) {
	    	cancelButtonPressed = true;
	    	dispose();
		
	    }

		protected void jComboBox1ItemStateChanged(ItemEvent evt) {
	    	String driverClassName = (String) evt.getItem();
	    	String connectStringExample = (String) drivers.get(driverClassName);
	    	jTextField.setText(connectStringExample);

		}

	    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        if (evt.getActionCommand().equals(OK)) {
	        	ExternalDataSource externalDataSource = new ExternalDataSource(); 
	        	String driver = (String) jComboBox1.getSelectedItem();
	        	String name = jTextField1.getText();
	        	String userName = jTextField2.getText();
	        	String password = new String(jPasswordField1.getPassword());   	
	        	String connectString = (String) jTextField.getText();

	        	if (name.equals("")){
	        		ErrorDialog.show(this, ERROR, ERRORNOMBRE, "");
                    return;
	        	}       

                if (name.equals("") || connectString.equals("")){
	        		ErrorDialog.show(this, ERROR, ERRORCADENACONEXION, "");
	        		return;
	        	}

                externalDataSource.setDriver(driver);
				externalDataSource.setName(name);
				externalDataSource.setUserName(userName);
				externalDataSource.setPassword(password);
				externalDataSource.setConnectString(connectString);
				setExternalDataSource(externalDataSource);				
	        }
	        dispose();	        
	    }
	    
	public ExternalDataSource getExternalDataSource() {
		return externalDataSource;
	}

	public void setExternalDataSource(ExternalDataSource externalDataSource) {
		this.externalDataSource = externalDataSource;
	}
	
	public void setInfo(){
		jTextField1.setText(externalDataSource.getName());
		jTextField2.setText(externalDataSource.getUserName());
		jComboBox1.setSelectedItem(externalDataSource.getDriver());
		jTextField.setText(externalDataSource.getConnectString());
		jPasswordField1.setText(externalDataSource.getPassword());

	}
	
	public boolean isCancelButtonPressed() {
		return cancelButtonPressed;
	}


	public boolean isClosed() {
		return closed;
	}


	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
    private ExternalDataSource externalDataSource;
	
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JTextField jTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private String[] driverNames;
    
	private void setDrivers(String[] driverNames) {
		jComboBox1.setModel(new DefaultComboBoxModel(driverNames));
		jComboBox1.setSelectedIndex(0);
		String driverClassName = (String)jComboBox1.getSelectedItem();
	   	String connectStringExample = (String) drivers.get(driverClassName);
    	jTextField.setText(connectStringExample);    	
	}
                

}
