/**
 * ShowRecursosEnLinea.java
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

import com.geopista.app.metadatos.componentes.ComboBoxEstructurasOptativo;
import com.geopista.app.metadatos.componentes.TextFieldObligatorio;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.CI_OnLineResource;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 19-ago-2004
 * Time: 14:18:17
 */
public class ShowRecursosEnLinea   extends javax.swing.JDialog {
	Logger logger = Logger.getLogger(ShowRecursosEnLinea.class);
    ResourceBundle messages;
    CI_OnLineResource recurso;

    public CI_OnLineResource getRecurso() {
        return recurso;
    }

    public void setRecurso(CI_OnLineResource recurso) {
        this.recurso = recurso;
    }

	/**
	 * Creates new form JSearch
	 */
	public ShowRecursosEnLinea(java.awt.Frame parent, boolean modal,
                        ResourceBundle messages) {
		super(parent, modal);
        this.messages=messages;
		initComponents();
        changeScreenLang(messages);
	}

    private void initComponents() {//GEN-BEGIN:initComponents
        jComboBoxFuncion = new ComboBoxEstructurasOptativo(Estructuras.getListaFunctionCode(),null);
        jLabelEnlace = new javax.swing.JLabel();
        jLabelFuncion = new javax.swing.JLabel();
        jTextFieldEnlace = new TextFieldObligatorio(255);
        jButtonAceptar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(jLabelEnlace, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));
        getContentPane().add(jTextFieldEnlace, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 380, -1));
        getContentPane().add(jComboBoxFuncion, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 160, -1));
        getContentPane().add(jLabelFuncion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

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
        recurso=null;
        dispose();
    } public boolean validar()
    {
         if ((jTextFieldEnlace.getText().length()<=0))
             return false;
         return true;
    }
    private void aceptar() {
         if (!validar())
        {
                JOptionPane optionPane= new JOptionPane(messages.getString("ShowRecursosEnLinea.novalido"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return;
        }
        else
        {
                DomainNode tipo=(DomainNode) jComboBoxFuncion.getSelectedItem();
                if (recurso==null)
                    recurso=new CI_OnLineResource(jTextFieldEnlace.getText(),tipo.getIdNode());
                else
                {
                    recurso.setLinkage(jTextFieldEnlace.getText());
                    recurso.setIdOnLineFunctionCode(tipo.getIdNode());
                }
                dispose();
        }
    }
     public void load(CI_OnLineResource recurso)
     {
         this.recurso=recurso;
         if (recurso==null) return;
         jTextFieldEnlace.setText(recurso.getLinkage()!=null?recurso.getLinkage():"");
         jComboBoxFuncion.setSelected(recurso.getIdOnLineFunctionCode());
     }
     public void changeScreenLang(ResourceBundle messages) {
         setTitle(messages.getString("ShowRecursosEnLinea.title"));
         jButtonAceptar.setText(messages.getString("ShowRecursosEnLinea.jButtonAceptar"));
         jButtonCancelar.setText(messages.getString("ShowRecursosEnLinea.jButtonCancelar"));
         jLabelEnlace.setText(messages.getString("ShowRecursosEnLinea.jLabelEnlace"));//"Enlace:");
         jLabelFuncion.setText(messages.getString("ShowRecursosEnLinea.jLabelFuncion"));//"Funci\u00f3n:");
     }

    private ComboBoxEstructurasOptativo jComboBoxFuncion;
    private TextFieldObligatorio jTextFieldEnlace;
    private javax.swing.JLabel jLabelEnlace;
    private javax.swing.JLabel jLabelFuncion;
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancelar;

}
