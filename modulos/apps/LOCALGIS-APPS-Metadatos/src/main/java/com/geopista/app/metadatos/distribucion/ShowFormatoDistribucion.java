/**
 * ShowFormatoDistribucion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.distribucion;

import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.metadatos.componentes.TextFieldObligatorio;
import com.geopista.protocol.metadatos.MD_Format;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 19-ago-2004
 * Time: 15:35:34
 */
public class ShowFormatoDistribucion  extends javax.swing.JDialog {
	Logger logger = Logger.getLogger(ShowFormatoDistribucion.class);
    ResourceBundle messages;
    MD_Format formato;

	/**
	 * Creates new form JSearch
	 */
	public ShowFormatoDistribucion(java.awt.Frame parent, boolean modal,
                        ResourceBundle messages) {
		super(parent, modal);
        this.messages=messages;
		initComponents();
        changeScreenLang(messages);
	}

    private void initComponents() {//GEN-BEGIN:initComponents
        jLabelFormatoNombre= new javax.swing.JLabel();
        jLabelFormatoVersion= new javax.swing.JLabel();
        jTextFieldFormatoNombre= new TextFieldObligatorio(50);
        jTextFieldFormatoVersion= new TextFieldObligatorio(255);
        jButtonAceptar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(jLabelFormatoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        getContentPane().add(jTextFieldFormatoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 300, -1));
        getContentPane().add(jLabelFormatoVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));
        getContentPane().add(jTextFieldFormatoVersion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, -1));

        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptar();
            }
        });
        getContentPane().add(jButtonAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
                   public void actionPerformed(java.awt.event.ActionEvent evt) {
                       cancelar();
                   }
               });

        getContentPane().add(jButtonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, -1, -1));
        pack();
    }

    private void cancelar() {
        formato=null;
        dispose();
    }
    public boolean validar()
    {
         if ((jTextFieldFormatoNombre.getText().length()<=0)||(jTextFieldFormatoVersion.getText().length()<=0))
             return false;
         return true;
    }
    private void aceptar() {
         if (!validar())
        {
                JOptionPane optionPane= new JOptionPane(messages.getString("ShowFormatoDistribucion.novalido"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return;
        }
        else
        {       if (formato==null)
                    formato=new MD_Format(null, jTextFieldFormatoNombre.getText(),jTextFieldFormatoVersion.getText());
                else
                {
                    formato.setName(jTextFieldFormatoNombre.getText());
                    formato.setVersion(jTextFieldFormatoVersion.getText());
                }
                dispose();
        }
    }
    public void load(MD_Format formato)
    {
        this.formato=formato;
        if (formato==null) return;
        jTextFieldFormatoNombre.setText(formato.getName());
        jTextFieldFormatoVersion.setText(formato.getVersion());
    }
     public void changeScreenLang(ResourceBundle messages) {
         setTitle(messages.getString("ShowFormatoDistribucion.title"));
         jButtonAceptar.setText(messages.getString("ShowFormatoDistribucion.jButtonAceptar"));
         jButtonCancelar.setText(messages.getString("ShowFormatoDistribucion.jButtonCancelar"));
         jLabelFormatoNombre.setText(messages.getString("ShowFormatoDistribucion.jLabelFormatoNombre"));//"Nombre:");
         jLabelFormatoVersion.setText(messages.getString("ShowFormatoDistribucion.jLabelFormatoVersion"));//"Version:");
     }
    public String getFormatoNombre()
    {
        return jTextFieldFormatoNombre.getText();
    }
    public String getFormatoVersion()
    {
           return jTextFieldFormatoVersion.getText();
    }

    public MD_Format getFormato() {
        return formato;
    }

    public void setFormato(MD_Format formato) {
        this.formato = formato;
    }


    private javax.swing.JLabel jLabelFormatoNombre;
    private javax.swing.JLabel jLabelFormatoVersion;
    private TextFieldObligatorio jTextFieldFormatoNombre;
    private TextFieldObligatorio jTextFieldFormatoVersion;
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancelar;

}

