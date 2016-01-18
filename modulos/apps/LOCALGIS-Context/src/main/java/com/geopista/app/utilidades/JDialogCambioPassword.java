/**
 * JDialogCambioPassword.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.utilidades;



import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.geopista.app.AppConstants;
import com.geopista.app.AppContext;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.administrador.EncriptarPassword;

public class JDialogCambioPassword extends JDialog
{
    
    private JPanel jContentPane = null;
    private JButton btnAceptar = null;
    private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
    
    private JLabel lblContraseniaAntigua = null;
    private JPasswordField txtContraseniaAntigua = null;
    private JLabel lblContraseniaNueva = null;
	private JPasswordField txtContraseniaNueva = null;
    private JLabel lblRepContraseniaNueva = null;
	private JPasswordField txtRepContraseniaNueva = null;
	private String usuario;
    private String titulo ="";
    private Integer id_entidad;
    private boolean valido;
    
    /**
     * Constructor de la clase
     * @param titulo Título de la pantalla
     */
    public JDialogCambioPassword(String usuario)
    {
        super();
        this.titulo = getMessage("JDialogCambioPassword.titulo");
        this.usuario = usuario;
        initialize();
    }
    
    
    
    public boolean isValido() {
		return valido;
	}



	/**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setTitle(titulo);
    }
    
    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane()
    {
        if (jContentPane == null)
        {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getLblContraseniaAntigua(), null);
            jContentPane.add(getTextContraseniaAntigua(), null);
            jContentPane.add(getLblContraseniaNueva(), null);
            jContentPane.add(getTextContraseniaNueva(), null);
            jContentPane.add(getLblRepContraseniaNueva(), null);
            jContentPane.add(getTextRepContraseniaNueva(), null);
            jContentPane.add(getBtnAceptar(), null);

        }
        return jContentPane;
    }
    
    /**
     * This method initializes btnAceptar	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBtnAceptar()
    {
        if (btnAceptar == null)
        {
            btnAceptar = new JButton();
            btnAceptar.setText(getMessage("JDialogCambioPassword.aceptar"));
            btnAceptar.setBounds(new java.awt.Rectangle(110,120,94,26));
            btnAceptar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	valido = cambioPassword();
                }
            });
        }
        return btnAceptar;
    }
   
    private JPasswordField getTextContraseniaAntigua(){
		if (txtContraseniaAntigua == null) {
			txtContraseniaAntigua = new JPasswordField();
			txtContraseniaAntigua.setText("");
			txtContraseniaAntigua.setBounds(new java.awt.Rectangle(160, 19, 98, 20));
		}
		return txtContraseniaAntigua;
	}
    
    private JPasswordField getTextContraseniaNueva(){
		if (txtContraseniaNueva == null) {
			txtContraseniaNueva = new JPasswordField();
			txtContraseniaNueva.setText("");
			txtContraseniaNueva.setBounds(new java.awt.Rectangle(160, 49, 98, 20));
		}
		return txtContraseniaNueva;
	}
    
    private JPasswordField getTextRepContraseniaNueva(){
		if (txtRepContraseniaNueva == null) {
			txtRepContraseniaNueva = new JPasswordField();
			txtRepContraseniaNueva.setText("");
			txtRepContraseniaNueva.setBounds(new java.awt.Rectangle(160, 79, 98, 20));
		}
		return txtRepContraseniaNueva;
	}

    private JLabel getLblContraseniaAntigua(){
    	if (lblContraseniaAntigua == null){
    		lblContraseniaAntigua = new JLabel();
    		lblContraseniaAntigua.setBounds(new Rectangle(19, 19, 155, 24));
    		lblContraseniaAntigua.setText(getMessage("JDialogCambioPassword.passwordAntigua"));
    	}
    	return lblContraseniaAntigua;
    }

    private JLabel getLblContraseniaNueva(){
    	if (lblContraseniaNueva == null){
    		lblContraseniaNueva = new JLabel();
    		lblContraseniaNueva.setBounds(new Rectangle(19, 49, 155, 24));
    		lblContraseniaNueva.setText(getMessage("JDialogCambioPassword.passwordNueva"));
    	}
    	return lblContraseniaNueva;
    }

    private JLabel getLblRepContraseniaNueva(){
    	if (lblRepContraseniaNueva == null){
    		lblRepContraseniaNueva = new JLabel();
    		lblRepContraseniaNueva.setBounds(new Rectangle(19, 79, 155, 24));
    		lblRepContraseniaNueva.setText(getMessage("JDialogCambioPassword.repitePasswordNueva"));
    	}
    	return lblRepContraseniaNueva;
    }
    
	public String getMessage(String id)
    {		    
        return aplicacion.getI18nString(id);
    }
	
	private boolean cambioPassword(){
		EncriptarPassword  ep=new EncriptarPassword(EncriptarPassword.TYPE2_ALGORITHM);
		String contraseniaAntigua = new String(txtContraseniaAntigua.getPassword());
		String contraseniaAntiguaEncriptada = ""; 
		try {
			contraseniaAntiguaEncriptada = ep.encriptar(contraseniaAntigua);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean passwordAntiguaValida = passwordValida(contraseniaAntiguaEncriptada);
		JOptionPane optionPane = null;
		boolean valido = true;
		String password = new String(txtContraseniaNueva.getPassword());
	    if (!passwordAntiguaValida){
	    	optionPane = new JOptionPane(getMessage("JDialogCambioPassword.passwordantigua"), JOptionPane.INFORMATION_MESSAGE);
			valido = false;
	    }
	    else{
			if (password == null || (!new String(txtContraseniaNueva.getPassword()).equals(new String(txtRepContraseniaNueva.getPassword())))){
				optionPane = new JOptionPane(getMessage("JDialogCambioPassword.nopassword"), JOptionPane.INFORMATION_MESSAGE);
				valido = false;
			}
			else{
				if (password.length() < AppConstants.TAMANIO_MINIMO_PASSWORD){
					optionPane = new JOptionPane(getMessage("JDialogCambioPassword.minimocaracterespass"), JOptionPane.INFORMATION_MESSAGE);
					valido = false;
				}
				else{
					Pattern p = Pattern.compile("[A-Z]+");				
					Matcher m = p.matcher(password);
				    StringBuffer sb = new StringBuffer();
				    boolean resultado = m.find();
				    if (!resultado){
				    	optionPane = new JOptionPane(getMessage("JDialogCambioPassword.caracteresenpassword"), JOptionPane.INFORMATION_MESSAGE);
				    	valido = false;
				    }
				    else{
				    	p = Pattern.compile("[a-z]+");
				    	m = p.matcher(password);
				    	sb = new StringBuffer();
					    resultado = m.find();
					    if (!resultado){
					    	optionPane = new JOptionPane(getMessage("JDialogCambioPassword.caracteresenpassword"), JOptionPane.INFORMATION_MESSAGE);
					    	valido = false;
					    }
					    else{
					    	p = Pattern.compile("[0-9]+");
					    	m = p.matcher(password);
					    	sb = new StringBuffer();
					    	resultado = m.find();
					    	if (!resultado){
					    		optionPane = new JOptionPane(getMessage("JDialogCambioPassword.caracteresenpassword"), JOptionPane.INFORMATION_MESSAGE);
					    		valido = false;
					    	}
					    	else{
					    		p = Pattern.compile("[-_@?¿¡*+.,:;<>\\!\"#%&/()={}]+");
						    	m = p.matcher(password);
						    	sb = new StringBuffer();
						    	resultado = m.find();
						    	if (!resultado){
						    		optionPane = new JOptionPane(getMessage("JDialogCambioPassword.caracteresenpassword"), JOptionPane.INFORMATION_MESSAGE);
						    		valido = false;
						    	}
						    	else{
						    		if (password.equals(contraseniaAntigua)){
						    			optionPane = new JOptionPane(getMessage("JDialogCambioPassword.passworddistintaalaultima"), JOptionPane.INFORMATION_MESSAGE);
							    		valido = false;
						    		}
						    	}
					    	}
					    }
				    }
	
				}
			}
		}
		if (!valido){
			JDialog dialog = optionPane.createDialog(this, "");
			dialog.show();
		}
		else{
			String passwordEncriptada;
			try {
				
				passwordEncriptada = ep.encriptar(password);
				if (actualizarPasswordBBDD(passwordEncriptada)){
					optionPane = new JOptionPane(getMessage("JDialogCambioPassword.passwordcambiada"), JOptionPane.INFORMATION_MESSAGE);
					JDialog dialog = optionPane.createDialog(this, "");
					dialog.show();
					dispose();
					valido = true;
				}
				else{
					optionPane = new JOptionPane(getMessage("JDialogCambioPassword.erroralcambiarpassword"), JOptionPane.INFORMATION_MESSAGE);
					JDialog dialog = optionPane.createDialog(this, "");
					dialog.show();
					valido = false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				optionPane = new JOptionPane(getMessage("JDialogCambioPassword.erroralcambiarpassword"), JOptionPane.INFORMATION_MESSAGE);
				JDialog dialog = optionPane.createDialog(this, "");
				dialog.show();
				valido = false;
			}
			
		}
		return valido;
	}
	
	public boolean actualizarPasswordBBDD(String contraseniaEncriptada){
		
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        CResultadoOperacion resultado;
        try
        {

            connection = aplicacion.getConnection(false);

        	// "UPDATE iuseruserhdr set password = ?, fecha_proxima_modificacion = current_date + (select cast (periodicidad as integer) from entidad_supramunicipal where id_entidad = ? ) where name = ?"
            if (id_entidad != 0){
            	preparedStatement = connection.prepareStatement("updatePassword");
            	preparedStatement.setString(1, contraseniaEncriptada);
            	preparedStatement.setInt(2, id_entidad);
            	preparedStatement.setString(3, usuario.toUpperCase());
        	}else{
        		preparedStatement = connection.prepareStatement("updatePasswordEntidadCero");
            	preparedStatement.setString(1, contraseniaEncriptada);
            	Calendar cal=Calendar.getInstance();
            	cal.add(Calendar.DAY_OF_MONTH, AppConstants.PERIODICIDAD);
            	Date fechaActual = new java.sql.Date(cal.getTime().getTime());
            	preparedStatement.setDate(2, fechaActual);
            	preparedStatement.setString(3, usuario.toUpperCase());
        	}
            		
            int iResult = preparedStatement.executeUpdate();
            if (iResult<1)
            {
                return false;
            }
            else
            {
               return true;
            }
        } catch (Exception e) {
            java.io.StringWriter sw=new java.io.StringWriter();
		    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
	    	e.printStackTrace(pw);
            return false;
        }finally{
     	   	try{preparedStatement.close();}catch(Exception e){};
     	   	try{connection.close();}catch(Exception e){};
        }
	}
	
	public boolean passwordValida(String contrasenia){
		if ((usuario != null) && !(usuario.equals("")) && (contrasenia != null) && !(contrasenia.equals(""))){
	    	ResultSet  rs = null;
	    	PreparedStatement ps = null;
	    	Connection conn = null;
	    	try{
		    	conn = aplicacion.getConnection(false);
	        	// "SELECT id_entidad FROM iuseruserhdr WHERE borrado!=1 and name = ?  and password = ?"
		        ps = conn.prepareStatement("existeUsuario");
	            ps.setString(1, usuario.toUpperCase());
	            ps.setString(2, contrasenia);
	            rs =ps.executeQuery();
	            
	            if (rs.next()){
	            	id_entidad = rs.getInt("id_entidad");
	            	return true;
	            }            
	            else
	            	return false;
	       } catch (Exception e) {
	            java.io.StringWriter sw=new java.io.StringWriter();
			    java.io.PrintWriter pw=new java.io.PrintWriter(sw);
		    	e.printStackTrace(pw);
	            return false;
	        }finally{
	        	try{rs.close();}catch(Exception e){};
	     	   	try{ps.close();}catch(Exception e){};
	     	   	try{conn.close();}catch(Exception e){};
	        }
		}
		else
			return false;
    }
}
    

