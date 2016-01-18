/**
 * SugerenciasPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SugerenciasPanel.java
 *
 * Created on 05-dic-2011, 17:19:59
 */
package com.geopista.app.sugerencias;

import java.io.File;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.app.mantis.MantisConstants;

/**
 *
 * @author osantos
 */
public class SugerenciasPanel extends javax.swing.JPanel {

    
	/**
	 * Descripción proporcionada externamente (ErrorDialog)
	 */
	private String externalDescription="";
	private String externalType="SUGERENCIA";
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext(); 

	
	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		if (externalType.equalsIgnoreCase("INCIDENCIA")) {
			this.jrbIncidencia.setSelected(true);
		}else {
			this.jrbSugerencia.setSelected(true);
		}
			 
	}

	public String getExternalDescription() {
		return externalDescription;
	}

	public void setExternalDescription(String externalDescription) {
		this.externalDescription = externalDescription;
		this.taDescripcion.setText(externalDescription);
	}

	private Logger logger=Logger.getLogger(SugerenciasPanel.class);
	/** Creates new form SugerenciasPanel */
    public SugerenciasPanel() {
        initComponents();
        this.tfMantisLogin.setEnabled(false);
        this.tfMantisPassword.setEnabled(false);
    }
    
    public Sugerencia getSugerencia() {
        Sugerencia s=new Sugerencia();
        if (this.jrbSugerencia.isSelected())
            s.setTipo("SUGERENCIA");
        else
            s.setTipo("INCIDENCIA");
        
        s.setAsunto(this.tfAsunto.getText());
        s.setDescripcion(this.taDescripcion.getText());
        s.setEntorno(this.taEntornoCliente.getText());
        if (this.jcbMantisUser.isSelected()) {
            s.setUsuario(this.tfMantisLogin.getText());
            s.setPassword(this.tfMantisPassword.getText());
        }
        
        s.setFicheroAdjunto(tfFileName.getText());
        return s;
    }
    
   static final String[] browsers = { "google-chrome", "firefox", "opera",
      "epiphany", "konqueror", "conkeror", "midori", "kazehakase", "mozilla" };
   static final String errMsg = "Error attempting to launch web browser";

   public static void openURL(String url) {
      try {  //attempt to use Desktop library from JDK 1.6+
         Class<?> d = Class.forName("java.awt.Desktop");
         d.getDeclaredMethod("browse", new Class[] {java.net.URI.class}).invoke(
            d.getDeclaredMethod("getDesktop").invoke(null),
            new Object[] {java.net.URI.create(url)});
         //above code mimicks:  java.awt.Desktop.getDesktop().browse()
         }
      catch (Exception ignore) {  //library not available or failed
         String osName = System.getProperty("os.name");
         try {
            if (osName.startsWith("Mac OS")) {
               Class.forName("com.apple.eio.FileManager").getDeclaredMethod(
                  "openURL", new Class[] {String.class}).invoke(null,
                  new Object[] {url});
               }
            else if (osName.startsWith("Windows"))
               Runtime.getRuntime().exec(
                  "rundll32 url.dll,FileProtocolHandler " + url);
            else { //assume Unix or Linux
               String browser = null;
               for (String b : browsers)
                  if (browser == null && Runtime.getRuntime().exec(new String[]
                        {"which", b}).getInputStream().read() != -1)
                     Runtime.getRuntime().exec(new String[] {browser = b, url});
               if (browser == null)
                  throw new Exception(Arrays.toString(browsers));
               }
            }
         catch (Exception e) {
            JOptionPane.showMessageDialog(null, errMsg + "\n" + e.toString());
            }
         }
      }    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jrbSugerencia = new javax.swing.JRadioButton();
        jrbIncidencia = new javax.swing.JRadioButton();
        tfAsunto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDescripcion = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        taEntornoCliente = new javax.swing.JTextArea();
        tfFileName = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jcbMantisUser = new javax.swing.JCheckBox();
        tfMantisLogin = new javax.swing.JTextField();
        lblMantisLogin = new javax.swing.JLabel();
        lblMantisPassword = new javax.swing.JLabel();
        tfMantisPassword = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();

        jLabel1.setText("Tipo");

        jLabel2.setText("Asunto");

        jLabel3.setText("Descripción");

        jLabel4.setText("Entorno cliente");

        jLabel5.setText("Anexar archivo");

        buttonGroup1.add(jrbSugerencia);
        jrbSugerencia.setSelected(true);
        jrbSugerencia.setText("Sugerencia");
        jrbSugerencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbSugerenciaActionPerformed(evt);
            }
        });

        buttonGroup1.add(jrbIncidencia);
        jrbIncidencia.setText("Incidencia");
        jrbIncidencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbIncidenciaActionPerformed(evt);
            }
        });

        tfAsunto.setText("Descripción corta de la sugerencia/incidencia");

        taDescripcion.setColumns(20);
        taDescripcion.setRows(5);
        taDescripcion.setLineWrap(true);
        taDescripcion.setText("Descripción completa de la sugerencia");
        jScrollPane1.setViewportView(taDescripcion);

        taEntornoCliente.setColumns(20);
        taEntornoCliente.setRows(5);
        taEntornoCliente.setLineWrap(true);
        taEntornoCliente.setText("Proporcione también aquella información que permita reproducir la información y la descripción de su entorno de trabajo: Aplicación LocalGIS/Características de su PC\nNavegador/etc.");
        
        jScrollPane2.setViewportView(taEntornoCliente);



        jButton1.setText("...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.setToolTipText("<html>Si dispone de credenciales en el sistema de gesitón de incidencias,<br> la sugerencia/incidencia se registrará en su nombre y podrá recibir notificaciones sobre su resolución</html>"); 
        jcbMantisUser.setText("Usuario Mantis");
        jcbMantisUser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jcbMantisUserStateChanged(evt);
            }
        });

        lblMantisLogin.setText("Login");

        lblMantisPassword.setText("Password");

        jLabel6.setForeground(new java.awt.Color(0, 51, 255));
        jLabel6.setText("Solicitar usuario Mantis...");
        jLabel6.setToolTipText("Pulse aquí para solicitar un usuario en el sistema de gestión de incidencias");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addComponent(lblMantisLogin)
                            .addGap(46, 46, 46)
                            .addComponent(tfMantisLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                            .addComponent(lblMantisPassword)
                            .addGap(18, 18, 18)
                            .addComponent(tfMantisPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(36, 36, 36))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jcbMantisUser)
                            .addContainerGap(411, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbMantisUser)
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMantisLogin)
                    .addComponent(tfMantisLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMantisPassword)
                    .addComponent(tfMantisPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jrbSugerencia)
                                        .addGap(36, 36, 36)
                                        .addComponent(jrbIncidencia))
                                    .addComponent(tfAsunto, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
                                .addGap(19, 19, 19))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jrbSugerencia)
                    .addComponent(jrbIncidencia))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfAsunto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(86, 86, 86)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(tfFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>

private void jrbSugerenciaActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
}

private void jrbIncidenciaActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
}



private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
	logger.info("Botón fichero pulsado");
	JFileChooser fc = new JFileChooser();
	if (evt.getSource() == jButton1) {
		//int returnVal = fc.showOpenDialog(SugerenciasPanel.this);
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			//This is where a real application would open the file.
			try {
			this.tfFileName.setText(file.getCanonicalPath());
			}catch(Exception ex) {ex.printStackTrace();}
		}
	}
}

private void jcbMantisUserStateChanged(javax.swing.event.ChangeEvent evt) {
  if (jcbMantisUser.isSelected()) {
      this.tfMantisLogin.setEnabled(true);
      this.tfMantisPassword.setEnabled(true);
  }else {
      this.tfMantisLogin.setEnabled(false);
      this.tfMantisPassword.setEnabled(false);
      
  }
}

private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {
	String url = null;
	if (aplicacion.getString(MantisConstants.URL_MANTIS) == null)
		url = MantisConstants.DEFAULT_URL_MANTIS;
	else
		url = aplicacion.getString(MantisConstants.URL_MANTIS);

	url=url+Constantes.MANTIS_URL_SIGNUP;
	this.openURL(url);
}

    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBox jcbMantisUser;
    private javax.swing.JRadioButton jrbIncidencia;
    private javax.swing.JRadioButton jrbSugerencia;
    private javax.swing.JLabel lblMantisLogin;
    private javax.swing.JLabel lblMantisPassword;
    private javax.swing.JTextArea taDescripcion;
    private javax.swing.JTextArea taEntornoCliente;
    private javax.swing.JTextField tfAsunto;
    private javax.swing.JTextField tfFileName;
    private javax.swing.JTextField tfMantisLogin;
    private javax.swing.JPasswordField tfMantisPassword;
    // End of variables declaration
}
