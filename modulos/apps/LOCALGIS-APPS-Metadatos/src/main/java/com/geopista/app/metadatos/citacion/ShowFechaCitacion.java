/**
 * ShowFechaCitacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.citacion;


import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.metadatos.componentes.ComboBoxEstructurasObligatorio;
import com.geopista.app.metadatos.componentes.FormattedTextFieldObligatorio;
import com.geopista.app.metadatos.estructuras.Estructuras;
import com.geopista.app.utilidades.CalendarButton;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.metadatos.CI_Date;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 17-ago-2004
 * Time: 14:31:31
 */
public class ShowFechaCitacion  extends javax.swing.JDialog {
	Logger logger = Logger.getLogger(ShowFechaCitacion.class);
    ResourceBundle messages;
    CI_Date ci_date;

	/**
	 * Creates new form JSearch
	 */
	public ShowFechaCitacion(java.awt.Frame parent, boolean modal,
                        ResourceBundle messages) {
		super(parent, modal);
        this.messages=messages;
		initComponents();
        changeScreenLang(messages);
	}

    public CI_Date getCi_date() {
        return ci_date;
    }

    public void setCi_date(CI_Date ci_date) {
        this.ci_date = ci_date;
    }

    private void initComponents() {//GEN-BEGIN:initComponents

        jLabelFecha = new javax.swing.JLabel();
        jLabelTipoFecha = new javax.swing.JLabel();
        jComboBoxTipoFecha =  new ComboBoxEstructurasObligatorio(Estructuras.getListaDateType(),null);
        jButtonAceptar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jTextFieldFecha = new FormattedTextFieldObligatorio();
        jCalendarButtonFecha= new CalendarButton(jTextFieldFecha);

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(jLabelFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 100, 20));
        getContentPane().add(jTextFieldFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 130, -1));
        jTextFieldFecha.setEditable(false);
        getContentPane().add(jCalendarButtonFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, 20, 20));
        getContentPane().add(jLabelTipoFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        getContentPane().add(jComboBoxTipoFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 150, -1));
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
    public boolean validar()
    {
         if((jComboBoxTipoFecha.getSelectedIndex()==0)||(jTextFieldFecha.getText().length()==0)) return false;
         return true;
    }

    private void cancelar() {
        ci_date=null;
        dispose();
    }

    private void aceptar() {
        if (!validar())
        {
                JOptionPane optionPane= new JOptionPane(messages.getString("ShowFechaCitacion.novalida"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return;
        }
        else
        {
                DomainNode tipo=(DomainNode) jComboBoxTipoFecha.getSelectedItem();
                ci_date=new CI_Date(null, jCalendarButtonFecha.getCalendar().getTime(),tipo);
                dispose();
        }
    }
     public void changeScreenLang(ResourceBundle messages) {
         setTitle(messages.getString("ShowFechaCitacion.title"));
         jLabelFecha.setText(messages.getString("ShowFechaCitacion.jLabelFecha"));
         jLabelTipoFecha.setText(messages.getString("ShowFechaCitacion.jLabelTipoFecha"));
         jButtonAceptar.setText(messages.getString("ShowFechaCitacion.jButtonAceptar"));
         jButtonCancelar.setText(messages.getString("ShowFechaCitacion.jButtonCancelar"));
     }

    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JComboBox jComboBoxTipoFecha;
    private javax.swing.JLabel jLabelFecha;
    private javax.swing.JLabel jLabelTipoFecha;
    private FormattedTextFieldObligatorio jTextFieldFecha;
    private CalendarButton jCalendarButtonFecha;

}
