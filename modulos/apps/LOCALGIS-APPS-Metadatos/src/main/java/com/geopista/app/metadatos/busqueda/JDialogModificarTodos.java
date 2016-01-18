/**
 * JDialogModificarTodos.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.busqueda;

import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.geopista.app.metadatos.init.Constantes;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.metadatos.OperacionesMetadatos;




/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 11-oct-2004
 * Time: 13:15:30
 */
public class JDialogModificarTodos  extends javax.swing.JDialog  {
	Logger logger = Logger.getLogger(JDialogModificarTodos.class);
    ResourceBundle messages;


    /**
	 * Creates new form JSearch
	 */
	public JDialogModificarTodos( java.awt.Frame parent, boolean modal,
                        ResourceBundle messages) {
		super(parent, modal);
        this.messages=messages;
  		initComponents();
        changeScreenLang(messages);

	}

    private void initComponents() {//GEN-BEGIN:initComponents
        jButtonSalir = new JButton();
        jButtonAceptar= new JButton();
        jPanelPrincipal = new JPanel();
        jLabelCampo = new JLabel();
        jComboCampo = new JComboBox();
        jLabelValor =new JLabel();
        jPanelBotonera = new JPanel();
        jTextFieldValor = new javax.swing.JTextField();
        jButtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salir();
            }
        });
        jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptar();
            }
        });
         jPanelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
         jPanelPrincipal.add(jLabelCampo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 100, -1));
         jPanelPrincipal.add(jComboCampo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 120, -1));
         jPanelPrincipal.add(jLabelValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 100, -1));
         jPanelPrincipal.add(jTextFieldValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 120, -1));

        jPanelBotonera.add(jButtonAceptar);
         jPanelBotonera.add(jButtonSalir);


         getContentPane().setLayout(new java.awt.BorderLayout());
         getContentPane().add(jPanelPrincipal, java.awt.BorderLayout.CENTER);
         getContentPane().add(jPanelBotonera, java.awt.BorderLayout.SOUTH);



        pack();
    }

    private void salir() {
        dispose();
    }
     public void changeScreenLang(ResourceBundle messages) {
         setTitle(messages.getString("JDialogModificarTodos.title"));
         jPanelPrincipal.setToolTipText(messages.getString("JDialogModificarTodos.title"));
         jButtonSalir.setText(messages.getString("JDialogModificarTodos.jButtonSalir"));
         jButtonAceptar.setText(messages.getString("JDialogModificarTodos.jButtonAceptar"));
         jLabelCampo.setText(messages.getString("JDialogModificarTodos.jLabelCampo"));
         jLabelValor.setText(messages.getString("JDialogModificarTodos.jLabelValor"));
         jComboCampo.removeAllItems();
         jComboCampo.addItem(messages.getString("JPanelMetadato.jLabelStdName"));
         jComboCampo.addItem(messages.getString("JPanelMetadato.jLabelVersion"));
         jComboCampo.addItem(messages.getString("JPanelMetadato.jLabelCodificacion"));
     }
    private void aceptar()
    {
        if (jTextFieldValor.getText().length()<=0)
        {
             JOptionPane optionPane= new JOptionPane(messages.getString("JDialogModificarTodos.mensaje.novalor"),JOptionPane.INFORMATION_MESSAGE);
             JDialog dialog =optionPane.createDialog(this,"");
             dialog.show();
        }
         int n = JOptionPane.showOptionDialog(this,
                 messages.getString("JDialogModificarTodos.mensaje.pregunta"),"",
                                                       JOptionPane.YES_NO_OPTION,
                                                       JOptionPane.QUESTION_MESSAGE,null,null,null);
         if (n==JOptionPane.NO_OPTION)
                  return;
         else
         {
              String sCampo="";
              switch (jComboCampo.getSelectedIndex())
              {
                case 0 : sCampo="metadatastandardname"; break;
                case 1 : sCampo="metadatastandardversion";break;
                case 2 : sCampo="characterset";break;

              }
              try
              {
                    CResultadoOperacion result=(new OperacionesMetadatos(Constantes.url)).updateAll(sCampo, jTextFieldValor.getText());
                    if (result.getResultado())
                    {
                        JOptionPane optionPane= new JOptionPane(messages.getString("JDialogModificarTodos.mensaje.ok"),JOptionPane.INFORMATION_MESSAGE);
                        JDialog dialog =optionPane.createDialog(this,"");
                        dialog.show();
                    }else
                    {
                        JOptionPane optionPane= new JOptionPane(result.getDescripcion(),JOptionPane.ERROR_MESSAGE);
                        JDialog dialog =optionPane.createDialog(this,"");
                        dialog.show();
                    }
              }catch(Exception e)
              {
                    JOptionPane optionPane= new JOptionPane(e.getMessage(),JOptionPane.ERROR_MESSAGE);
                    JDialog dialog =optionPane.createDialog(this,"");
                    dialog.show();
              }

         }
         dispose();
    }

    private javax.swing.JButton jButtonSalir;
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JLabel jLabelCampo;
    private javax.swing.JLabel jLabelValor;
    private javax.swing.JPanel jPanelBotonera;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JTextField jTextFieldValor;
    private javax.swing.JComboBox jComboCampo;


}
