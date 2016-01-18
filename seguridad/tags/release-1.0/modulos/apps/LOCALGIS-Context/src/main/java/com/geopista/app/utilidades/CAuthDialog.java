package com.geopista.app.utilidades;

/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public Licensee for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *
 * www.geopista.com
 *
 *
*/
/*
 * CAuthDialog.java
 *
 * Created on 19 de abril de 2004, 17:23
 */

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.StringReader;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;

import javax.net.ssl.KeyManagerFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.app.administrador.init.Constantes;
import com.geopista.protocol.administrador.SegPassword;
import com.geopista.protocol.net.EnviarSeguro;
import com.geopista.security.dnie.DNIeManager;
import com.geopista.security.dnie.dialogs.CertificateLoginDialog;
import com.geopista.security.dnie.dialogs.PasswordDialog;
import com.geopista.security.dnie.utils.CertificateOperations;
import com.geopista.security.dnie.utils.CertificateUtils;
import com.geopista.ui.dialogs.LoginDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 *
 * @author  avivar
 */
public class CAuthDialog extends javax.swing.JDialog {
    LoginDialog dial=null;
    private String url;
    private String idApp;
    private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    private int idEntidad;
    private boolean autenticado=false;
    private boolean cancel=false;
    boolean noconectar=false;
    private Frame parent;
	private boolean fromInicio=false;
	private SegPassword segPassword = null;
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(com.geopista.security.SecurityManager.class);
    public static ResourceBundle literales= null;
    private int dias;
    /** Creates new form CAuthDialog */
     public CAuthDialog(java.awt.Frame parent, boolean modal,
                       String url, String idApp, int idEntidad
                       ,ResourceBundle messages, boolean noconectar) {
        //super(parent, modal);
         this.parent=parent;
         //----NUEVO----> 
        // dial = new LoginDialog(parent,idApp);
         dial = new LoginDialog(parent,idApp,noconectar);
         GUIUtil.centreOnWindow(dial);
         //--FIN NUEVO-->        
        this.url=url;
          com.geopista.security.SecurityManager.setsUrl(url);
        this.idApp = idApp;
        this.idEntidad= idEntidad;
        this.noconectar=noconectar;
        //initComponents();
        changeScreenLang(messages);
        literales = messages;       
        segPassword = new SegPassword();  //BUG SOLUCIONADO   
    }
    public CAuthDialog(java.awt.Frame parent, boolean modal,
                       String url, String idApp, int idEntidad
                       ,ResourceBundle messages) {

        //super(parent, modal);
        this.parent=parent;
        //----NUEVO----> 
       // dial = new LoginDialog(parent,idApp);
        dial = new LoginDialog(parent,idApp,false);
        //--FIN NUEVO--> 
        GUIUtil.centreOnWindow(dial);
        this.url=url;
        com.geopista.security.SecurityManager.setsUrl(url);
        this.idApp = idApp;
        this.idEntidad = idEntidad;
        
        //initComponents();
        changeScreenLang(messages);
        segPassword = new SegPassword();
    }
    public void setFromInicio(boolean fromInicio){
    	this.fromInicio=fromInicio;
    }
    

    public void show()
    {
        if (dial==null)
        {
            super.show();
            return;
        }
        while(!autenticado)
        {
        	aplicacion.setPartialLogged(true);
        	/*logger.info("Setting Logged=true");
        	for( StackTraceElement ste : Thread.currentThread().getStackTrace() ) {
        		logger.info( ste + "\n" );
        		}*/
            dial.show();
            if (dial.isCanceled())
                cancelar();    
            //--NUEVO-->
            if (dial.isAutenticated()) autenticado = true;
            //--FIN NUEVO-->        
            else{
            	aceptar();            	
                dial.dispose();
                verificacionContraseña();                
                //dial=null; ASO comenta 6-04-2010 porque falla si no se autentida correctamente
            }
        }
    	//logger.info("Setting Logged=false");
        aplicacion.setPartialLogged(false);
        dial.dispose();
        dial=null;
    }
    
    public LoginDialog showLogPass()
    {
        if (dial==null)
        {
            super.show();
            return dial;
        }
        while(!autenticado)
        {
        	aplicacion.setPartialLogged(true);
        	/*logger.info("Setting Logged=true");
        	for( StackTraceElement ste : Thread.currentThread().getStackTrace() ) {
        		logger.info( ste + "\n" );
        		}*/
            dial.show();
            if (dial.isCanceled())
                cancelar();    
            //--NUEVO-->
            if (dial.isAutenticated()) autenticado = true;
            //--FIN NUEVO-->        
            else{
            	aceptar();            	
                dial.dispose();
                verificacionContraseña();                
                //dial=null; ASO comenta 6-04-2010 porque falla si no se autentida correctamente
            }
        }
    	//logger.info("Setting Logged=false");
        aplicacion.setPartialLogged(false);
        dial.dispose();
        //dial=null;
        return dial;
    }

    private void verificacionContraseña() {
    	if (AppContext.getApplicationContext().getBlackboard().get("intentos") != null){
    		segPassword.setIntentos(((Integer) AppContext.getApplicationContext().getBlackboard().get("intentos")).intValue());
    	}
    	if (AppContext.getApplicationContext().getBlackboard().get("bloqueado") != null) 
    		segPassword.setBloqueado(Boolean.valueOf((String) AppContext.getApplicationContext().getBlackboard().get("bloqueado")).booleanValue());
    	
    	if (AppContext.getApplicationContext().getBlackboard().get("intentos_reiterados") != null) 
    		segPassword.setIntentos_reiterados(((Integer) AppContext.getApplicationContext().getBlackboard().get("intentos_reiterados")).intValue());

    	if (AppContext.getApplicationContext().getBlackboard().get("usuarioNoExiste") != null) 
    		segPassword.setUsuarioNoExiste(Boolean.valueOf((String) AppContext.getApplicationContext().getBlackboard().get("usuarioNoExiste")).booleanValue());
    	if (!segPassword.isUsuarioNoExiste() && !autenticado){
	    	if ((segPassword.getIntentos_reiterados() >= 0) && (segPassword.getIntentos() != 0) && (segPassword.getIntentos_reiterados() < segPassword.getIntentos()-1) && (!autenticado) && (!segPassword.isBloqueado())){
	    		try {
					StringReader result= EnviarSeguro.enviarPlano(url+"/CServletIntentos","actualizaIntentos");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int intentos = segPassword.getIntentos_reiterados()+1;
				JOptionPane optionPane = new JOptionPane(literales.getString("CAuthDialog.descripcion1.intentos")+" "+intentos+" "+literales.getString("CAuthDialog.descripcion2.intentos")+" "+segPassword.getIntentos()+" "+literales.getString("CAuthDialog.descripcion3.intentos"), JOptionPane.INFORMATION_MESSAGE);
	            
	            JDialog dialog = optionPane.createDialog(literales.getString("CAuthDialog.title.intentos"));
	            dialog.setVisible(true);
			}
	    	else{ 
		    	if (segPassword.isBloqueado()){
		        	autenticado = false;
		            JOptionPane optionPane = new JOptionPane(literales.getString("CAuthDialog.descripcion.usuarioBloqueado"), JOptionPane.INFORMATION_MESSAGE);
		                       
		            JDialog dialog = optionPane.createDialog(literales.getString("CAuthDialog.title.usuarioBloqueado"));
		            
		            dialog.setVisible(true);
		        }
		    	else{
		    		if (!idApp.equals(AppConstants.idApp)){
		    			if (segPassword.isCaducado()){
				        	autenticado = false;
				            JOptionPane optionPane = new JOptionPane(literales.getString("CAuthDialog.descripcion.contraseniaCaducada"), JOptionPane.INFORMATION_MESSAGE);
				                       
				            JDialog dialog = optionPane.createDialog(literales.getString("CAuthDialog.title.contraseniaCaducada"));			            
				            dialog.setVisible(true);
				            		            
				            lanzarAdministracion();
		    			}    			
		    			else{
		    				if (segPassword.isAviso()){
		    					JOptionPane optionPane = null;
		    					if (dias == 0)
		    						optionPane = new JOptionPane(literales.getString("CAuthDialog.descripcion.cambioContrasenia"), JOptionPane.INFORMATION_MESSAGE);
		    					else
		    						optionPane = new JOptionPane(literales.getString("CAuthDialog.descripcion1.cambioContrasenia")+" "+dias+" "+literales.getString("CAuthDialog.descripcion2.cambioContrasenia"), JOptionPane.INFORMATION_MESSAGE);
		    		                       
		    		            JDialog dialog = optionPane.createDialog(literales.getString("CAuthDialog.title.cambioContrasenia"));
		    		            dialog.setVisible(true);
		    		            
		    		            lanzarAdministracion();
		        			}
		    			}
			        }
		    	}
	    	}
    	}
    	if (autenticado){
    		try {
    			StringReader result= EnviarSeguro.enviarPlano(url+"/CServletIntentos","reiniciaIntentos");
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
	}
	public boolean showD(boolean fromInicio)
    {
    	this.fromInicio=fromInicio;
    	boolean resultado=false;
     	//---NUEVO--->
    	if (dial.isAutenticated()){
    		autenticado = true;
    		resultado=true;
            dial.dispose();
            dial=null;
    	}
    	//--FIN NUEVO-->
        if (dial==null)
        {
        	System.out.println("Mostrando dialogo");
            super.show();
            return resultado;
        }
        while((!autenticado) && (!cancel))
        {
        	System.out.println("Mostrando dialogo");
            dial.show();
            if (dial.isCanceled()){            	
            	resultado=false;
                cancelar();
            }
            else{
            	aceptar();            	
                resultado=true;
                dial.dispose();
                dial=null;
            }
        }
        
        return resultado;
    }    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jButtonAceptar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jButtonNoConectar= new javax.swing.JButton();
        jLabelNombre = new javax.swing.JLabel();
        jLabelPassword = new javax.swing.JLabel();
        userJTextField = new javax.swing.JTextField();
        passwdJTextField = new javax.swing.JPasswordField();
        jLabelControl = new javax.swing.JLabel();
        //----NUEVO---->
		jButtonCertificate = new javax.swing.JButton();
        //--FIN NUEVO-->
		
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
    
        setResizable(false);
        setUndecorated(true);
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptar();
            }
        });
        jButtonAceptar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    aceptar();
                }
            }
        });

        //----NUEVO---->
        //Introduccion de botones en un panel centrado 
        	JPanel jPanelButton = new JPanel();      	
        	FlowLayout flowLayoutButton = new FlowLayout();
			jPanelButton.setLayout(flowLayoutButton);
			flowLayoutButton.setAlignment(java.awt.FlowLayout.CENTER);
			
			jPanelButton.add(jButtonAceptar, null);
		//----FIN NUEVO---->
			
        getRootPane().setDefaultButton(jButtonAceptar);

         if (!noconectar)
         {
            jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cancelar();
                }
            });

            //----NUEVO---->
            //Centrado del boton
            jPanelButton.add(jButtonCancelar, null);
            //----FIN NUEVO---->
         }
         else
        {
            jButtonNoConectar.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            noconectar();
                        }
                    });

            //----NUEVO---->
            jPanelButton.add(jButtonNoConectar, null);
            //----FIN NUEVO---->
        }
        //----NUEVO---->
     	JPanel jPanelLabelNombre = new JPanel();
    	FlowLayout flowLayoutLabelNombre = new FlowLayout();
    	jPanelLabelNombre.setLayout(flowLayoutLabelNombre);
		flowLayoutLabelNombre.setAlignment(java.awt.FlowLayout.RIGHT);
				
		jPanelLabelNombre.add(jLabelNombre, null);
				
     	JPanel jPanelLabelPassword = new JPanel();
    	FlowLayout flowLayoutLabelPassword = new FlowLayout();
    	jPanelLabelPassword.setLayout(flowLayoutLabelPassword);
		flowLayoutLabelPassword.setAlignment(java.awt.FlowLayout.RIGHT);
		
		jPanelLabelPassword.add(jLabelPassword, null);
		
		
     	JPanel jPanelTextNombre = new JPanel();
    	FlowLayout flowLayoutTextNombre = new FlowLayout();
    	jPanelTextNombre.setLayout(flowLayoutTextNombre);
		flowLayoutTextNombre.setAlignment(java.awt.FlowLayout.LEFT);
				
		userJTextField.setPreferredSize(new Dimension(100,20));
		jPanelTextNombre.add(userJTextField, null);
		
		
     	JPanel jPanelTextPassword = new JPanel();
    	FlowLayout flowLayoutTextPassword = new FlowLayout();
    	jPanelTextPassword.setLayout(flowLayoutTextPassword);
		flowLayoutTextPassword.setAlignment(java.awt.FlowLayout.LEFT);
		
		passwdJTextField.setPreferredSize(new Dimension(100,20));
		jPanelTextPassword.add(passwdJTextField, null);
		//----FIN NUEVO---->
       // getContentPane().add(jLabelNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 100, 20));

       // getContentPane().add(jLabelPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 100, 20));

        //getContentPane().add(userJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 150, 20));
        //userJTextField.setText("SYSSUPERUSER");

        //getContentPane().add(passwdJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 150, -1));
        //passwdJTextField.setText("SYSPASSWORD");
 
        jLabelControl.setFont(new java.awt.Font("MS Sans Serif", 1, 18));
        jLabelControl.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(jLabelControl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 200, 20));
        
      //----NUEVO---->
		if(CertificateUtils.isDNIeActive()){   
			
			jButtonCertificate.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	certificateButtonClick(evt);
                }
            });
			jButtonCertificate.setIcon(new ImageIcon(CAuthDialog.class.getResource(IMAGE_BTN_DNIE)));
			jButtonCertificate.setPreferredSize(new Dimension(175,45));
			
	     	JPanel jPanelCertificate = new JPanel();
	    	FlowLayout flowLayoutCertificate  = new FlowLayout();
	    	jPanelCertificate.setLayout(flowLayoutCertificate);
			flowLayoutCertificate.setAlignment(java.awt.FlowLayout.CENTER);
			jPanelCertificate.add(jButtonCertificate, null); 
			
			
			getContentPane().add(jPanelCertificate, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 270, 50));
			getContentPane().add(jPanelLabelNombre,new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 103, 118, 25));
			getContentPane().add(jPanelTextNombre,new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 100, 150, 25));
			getContentPane().add(jPanelLabelPassword,new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 133, 118, 25));
			getContentPane().add(jPanelTextPassword,new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 130, 150, 25));
			getContentPane().add(jPanelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 270, 30));
		}else{				
			getContentPane().add(jPanelLabelNombre,new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 53, 118, 25));
			getContentPane().add(jPanelTextNombre,new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 50, 150, 25));
			getContentPane().add(jPanelLabelPassword,new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 83, 118, 25));
			getContentPane().add(jPanelTextPassword,new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 80, 150, 25));
			//getContentPane().add(jPanelLabel,new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 134, 25));
			//getContentPane().add(jPanelText,new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 80, 134, 25));
			getContentPane().add(jPanelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 270, 30));
		}
		//----FIN NUEVO---->
                
        pack();
    }

    //----NUEVO---->
	private void certificateButtonClick(java.awt.event.ActionEvent e) {
		DNIeManager dnieManager = new DNIeManager();
		if (dnieManager.isDNIe()) {
			passwordDialog = new PasswordDialog(
					this,
					literales.getString("dnie.CAuthDialog.certificateButtonClick.passwordDialog.title"));
			String pin = passwordDialog.showDialog();
			if (!passwordDialog.isCanceled()) {
				if (dnieManager.accessPin(pin)) {
					passwordDialog.setVisible(false);
					certificateLogin(dnieManager.getClientCert(),
							CertificateUtils.generateKeyManagerFactory(
									dnieManager.getKeyStore(),
									pin.toCharArray()));
				} else
					JOptionPane
					.showMessageDialog(
							this,
							literales.getString("dnie.CAuthDialog.certificateButtonClick.pin.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
				}
		} else {
			certificateLoginDialog = new CertificateLoginDialog(this, idApp);
			certificateLoginDialog.showDialog();
			if (!certificateLoginDialog.isCanceled()
					&& certificateLoginDialog.isAutenticated()) {
				dispose();
			}
		}
	}
	
	private void certificateLogin(X509Certificate certificate,
			KeyManagerFactory kmf) {
		if (CertificateOperations.certificateLogin(certificate, kmf, idApp)) {
			JOptionPane.showMessageDialog(this,
					literales.getString("dnie.sessionCreated.info"),infoMessageTitle,JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else
			JOptionPane.showMessageDialog(this,
					literales.getString("dnie.autentication.error"),errorMessageTitle,JOptionPane.ERROR_MESSAGE);
	}
	//--FIN NUEVO-->
    
    
    private void cancelar(){
    	if (fromInicio){
        	cancel=true;
        	dispose();
    	}
    	else{
    		System.exit(0);
    	}
    }
    private void noconectar()
    {
        dispose();
    }
    private void aceptar() {//GEN-FIRST:event_jButton1ActionPerformed
        //***para sacar la ventana de espera**
     final TaskMonitorDialog progressDialog = new TaskMonitorDialog(parent, null);
     progressDialog.setTitle(literales.getString("CAuthDialog.title"));
     progressDialog.addComponentListener(new ComponentAdapter()
     {
         public void componentShown(ComponentEvent e)
         {
             new Thread(new Runnable()
             {
                 public void run()
                 {

		try
        {
//			intentos++;
			if (segPassword.getIntentos_reiterados() >= (segPassword.getIntentos()-1) && (!segPassword.isBloqueado())){

				StringReader result= EnviarSeguro.enviarPlano(url+"/CServletIntentos","bloquea");
				autenticado = false;	            
			}

            parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            com.geopista.security.SecurityManager.setsUrl(url);
            
            PasswordManager passwordManager=new PasswordManager();
            if (dial==null) //modo antiguo
            {
	 	        com.geopista.security.SecurityManager.login(userJTextField.getText().toLowerCase(), new String(passwdJTextField.getPassword()),
                                                   idApp);
	 	        //el usuario ya está autenticado en el sistema vamos a
                //guardarlo como una constante:
                String nombreUsuario=userJTextField.getText().toLowerCase();
                Constantes.nombreUsuario=nombreUsuario;                
                dispose();
            }else
            {

                parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                com.geopista.security.SecurityManager.login(dial.getLogin(), dial.getPassword(),
                                                   idApp);
                autenticado=true;
                //el usuario ya está autenticado en el sistema vamos a
                //guardarlo como una constante:
                String nombreUsuario=dial.getLogin();
                Constantes.nombreUsuario=nombreUsuario;
                
                ((AppContext)AppContext.getApplicationContext()).login2(nombreUsuario,idApp);
                
                //passwordManager.seguridadContrasenia(nombreUsuario);
                
            }          

            //-------NUEVO----------->
//            if(((AppContext)AppContext.getApplicationContext()).isLogged()){
//            	System.out.println("GUARDADO REGISTRO: " + SecurityManager.getIdSesion());
//            	if(SSOAuthManager.isSSOActive()) SSOAuthManager.saveSSORegistry();  
//            }
            //-----FIN NUEVO--------->   
        }catch(Exception e)
        {        
            try
            {
            	logger.error("login()", e);
                dial.setErrorLabel(aplicacion.getI18nString("AppContext.IntentoErroneo"));
                AppContext.getApplicationContext().setUserPreference(aplicacion.USER_LOGIN, null);
                 //com.geopista.security.SecurityManager.logout();
            }catch(Exception ex)
            {
                logger.error("Error al realizar la desconexión:",ex);                
            }
           /* try
            {
                logger.error("ERROR al autenticar al usuario ",e);
                JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.ERROR_MESSAGE);
                JDialog dialog =optionPane.createDialog(parent,literales.getString("CAuthDialog.message1"));
                dialog.show();
            }catch(Exception ex)
            {
                logger.error("Error al mostrar mensaje:",ex);
            }*/

        }
        finally
                        {
                            progressDialog.setVisible(false);
                            parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                            progressDialog.dispose();
                        }
                    }
              }).start();
          }
       });
       GUIUtil.centreOnWindow(progressDialog);
       progressDialog.setVisible(true);
       //System.out.println("EL NOMBRE DE USUARIO VALE: "+Constantes.nombreUsuario);
       
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void changeScreenLang(ResourceBundle messages)
    {
        try
        {
            literales= messages;
            setTitle(messages.getString("CAuthDialog.title"));//Autenticación
            if (dial!=null)
                dial.setTitle(messages.getString("CAuthDialog.title"));
            else
            {
                jButtonAceptar.setText(messages.getString("CAuthDialog.jButtonAceptar"));//Aceptar
                jButtonCancelar.setText(messages.getString("CAuthDialog.jButtonCancelar"));//Cancelar
                jButtonNoConectar.setText(messages.getString("CAuthDialog.jButtonNoConectar"));//Cancelar
                jLabelNombre.setText(messages.getString("CAuthDialog.jLabelNombre"));//Usuario:
                jLabelPassword.setText(messages.getString("CAuthDialog.jLabelPassword"));//Contraseña:
                jLabelControl.setText(messages.getString("CAuthDialog.jLabelControl"));//Control de acceso
                //----NUEVO---->
                if(CertificateUtils.isDNIeActive()){
                	jButtonCertificate.setText(messages.getString("dnie.certButton.text"));//DNIe
                	errorMessageTitle = messages.getString("dnie.errorMessage.title");
        			infoMessageTitle = messages.getString("dnie.infoMessage.title");
                }
                //--FIN NUEVO-->
            }
        }catch(Exception e)
        {
            logger.error("Error al cargar las etiquetas: ",e);
        }
    }

    /*public void seguridadContrasenia(String user) throws Exception{

    	AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    	Connection conn = aplicacion.getConnection(false);

        PreparedStatement ps = conn.prepareStatement("seguridadContrasenia");
        ResultSet  rs = null;
        try {             
            ps.setString(1, user.toUpperCase());
            rs =ps.executeQuery();
            if (rs.next()){
            	
            	segPassword.setUser(user);
            	segPassword.setBloqueado(rs.getBoolean("bloqueado"));
            	Date fechaModificacion = rs.getDate("fecha_proxima_modificacion");
            	Date fechaActual = new Date();
            	// Si la fecha de modificación es mayor:
            	if (fechaModificacion.compareTo(fechaActual) > 0){
            		segPassword.setCaducado(false);
            	}
            	else{
            		segPassword.setCaducado(true);
            	}
            	long diferencia = fechaModificacion.getTime() - fechaActual.getTime();
            	dias = (int) Math.floor(diferencia / (1000 * 60 * 60 * 24));
            	//System.out.println(dias);
            	if (dias > rs.getInt("aviso")){
            		segPassword.setAviso(false);
            	}
            	else{
            		segPassword.setAviso(true);
            	}
            	segPassword.setIntentos(rs.getInt("intentos"));
            	segPassword.setIntentos_reiterados(rs.getInt("intentos_reiterados"));  
            }
            else{
            	segPassword.setUser(user);
            	segPassword.setBloqueado(false);
            	segPassword.setCaducado(false);
            	segPassword.setAviso(false);
            }

       } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
		    logger.error("ERROR en seguridadContrasenia :"+sw.toString());
            throw e;

        }finally{
        	try{rs.close();}catch(Exception e){};
     	   	try{ps.close();}catch(Exception e){};
     	   	try{conn.close();}catch(Exception e){};
        }
    }*/
    
    public void lanzarAdministracion(){
    	
    	Object[] options = {literales.getString("CAuthDialog.descripcion.si"), literales.getString("CAuthDialog.descripcion.no")};
        
        int opcionElegida = JOptionPane
		.showOptionDialog(this, literales.getString("CAuthDialog.descripcion.irAdministracion"), literales.getString("CAuthDialog.title.irAdministracion"),
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options,
				options[0]);
        if (opcionElegida == 0) {
        	Runtime ejecutor=Runtime.getRuntime();	
        	try {
        		AppContext appContext = (AppContext) AppContext.getApplicationContext();
        		String comando = "javaws "+appContext.getString(AppContext.URL_TOMCAT)+"/software/localgis_administrador.jnlp";
        		Process proceso=ejecutor.exec(comando);
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonNoConectar;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelControl;
    private javax.swing.JPasswordField passwdJTextField;
    private javax.swing.JTextField userJTextField;
    //----NUEVO---->
    private javax.swing.JButton jButtonCertificate;
    private static final String IMAGE_BTN_DNIE = "/img/btn_dnie.gif";
	private CertificateLoginDialog certificateLoginDialog = null;
	private PasswordDialog passwordDialog = null;
	private String infoMessageTitle = null;
	private String errorMessageTitle = null;
    //--FIN NUEVO-->
    // End of variables declaration//GEN-END:variables

}