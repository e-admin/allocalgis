/**
 * ShowCitacion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.metadatos.citacion;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.geopista.app.metadatos.componentes.ListObligatorio;
import com.geopista.app.metadatos.componentes.TextFieldObligatorio;
import com.geopista.protocol.metadatos.CI_Citation;





/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 30-jul-2004
 * Time: 12:53:51
 */
public class ShowCitacion extends javax.swing.JDialog {
	Logger logger = Logger.getLogger(ShowCitacion.class);
    ResourceBundle messages;
     private java.awt.Frame framePadre;
    private CI_Citation citation;
	/**
	 * Creates new form JSearch
	 */
	public ShowCitacion(java.awt.Frame parent, boolean modal,
                        ResourceBundle messages) {
		super(parent, modal);
        this.framePadre=parent;
        this.messages=messages;
		initComponents();
        changeScreenLang(messages);
	}

    private void initComponents() {//GEN-BEGIN:initComponents
        jPanelCitacion = new javax.swing.JPanel();
        jButtonCancelar = new javax.swing.JButton();
        jLabelTitulo = new javax.swing.JLabel();
        jTextFieldTitulo = new TextFieldObligatorio(255);

        jTextPaneFechas = new ListObligatorio(messages,new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        mostrarFechaCitacion();
                    }
        });

        jTextPaneFechas.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
                    JList list = (JList)evt.getSource();
                    if (evt.getClickCount() == 2) {          // Double-click
                        // Get item index
                      mostrarFechaCitacion ();
                    }
                }
            });

        jLabelFechas = new javax.swing.JLabel();
        jButtonSalvar = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanelCitacion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelCitacion.setBorder(new javax.swing.border.EtchedBorder());

        jPanelCitacion.setDoubleBuffered(false);

        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed();
            }
        });

        jPanelCitacion.add(jButtonCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, -1, -1));

        jLabelTitulo.setFocusCycleRoot(true);
        jPanelCitacion.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, -1));

        jPanelCitacion.add(jTextFieldTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 280, -1));
        jPanelCitacion.add(jTextPaneFechas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 200, 60));

        jLabelFechas.setFocusCycleRoot(true);
        jPanelCitacion.add(jLabelFechas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 200, -1));

        jButtonSalvar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    salvar();
                }
            });

        jPanelCitacion.add(jButtonSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, -1, -1));

        getContentPane().add(jPanelCitacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 370, 200));

        pack();
    }

    private void cancelarJButtonActionPerformed() {
        dispose();
    }
    private boolean validar()
    {
        if((getTitle().length()<=0)||(getFechas().size()<=0))
            return false;
        return true;
  }
    private void salvar() {
        if (validar())
        {
            if (citation==null) citation= new CI_Citation();
            citation.setTitle(getTitle());
            citation.setCI_Dates(getFechas());
            dispose();
        }
        else
        {
                JOptionPane optionPane= new JOptionPane(messages.getString("ShowCitacion.novalida"),JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog =optionPane.createDialog(this,"");
                dialog.show();
                return;
        }
    }
    private void mostrarFechaCitacion() {
            // Aqui es donde tengo que mostrar la información de la cita
       if (this.isEnabled())
       {
            com.geopista.app.metadatos.citacion.ShowFechaCitacion fechaDialog = new com.geopista.app.metadatos.citacion.ShowFechaCitacion(framePadre, true, messages);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            fechaDialog.setLocation(d.width/2 - 200/2, d.height/2 - 75/2);
            fechaDialog.setSize(250,200);
            fechaDialog.setResizable(false);
            fechaDialog.show();
            if (fechaDialog.getCi_date()!=null)
               jTextPaneFechas.addElement(fechaDialog.getCi_date());
            fechaDialog=null;
       }
     }
     public void changeScreenLang(ResourceBundle messages) {
         setTitle(messages.getString("ShowCitacion.title"));
         jPanelCitacion.setToolTipText(messages.getString("ShowCitacion.title"));
         jButtonCancelar.setText(messages.getString("ShowCitacion.jButtonCancelar"));
         jLabelTitulo.setText(messages.getString("ShowCitacion.jLabelTitulo"));
         jLabelFechas.setText(messages.getString("ShowCitacion.jLabelFechas"));
         jButtonSalvar.setText(messages.getString("ShowCitacion.jButtonSalvar"));
     }
     public void setEnabled(boolean bEnabled)
     {
        super.setEnabled(bEnabled);
        jTextFieldTitulo.setEditable(bEnabled);
        jTextPaneFechas.setEnabled(bEnabled);
        jButtonSalvar.setEnabled(bEnabled);
     }
    public String getTitle()
    {
         return jTextFieldTitulo.getText();
    }

    public Vector getFechas()
    {
        return jTextPaneFechas.getVectorModel();
    }
    public CI_Citation getCitation()
    {
        return citation;
    }

    public void setCitation(CI_Citation citation) {

        this.citation = citation;
        jTextFieldTitulo.setText((citation!=null&&citation.getTitle()!=null)?citation.getTitle():"");
        if (citation!=null&&citation.getCI_Dates()!=null)
            jTextPaneFechas.setModel(citation.getCI_Dates());
        else
            jTextPaneFechas.removeAll();
    }

    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonSalvar;
    private javax.swing.JLabel jLabelFechas;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanelCitacion;
    private ListObligatorio jTextPaneFechas;
    private javax.swing.JTextField jTextFieldTitulo;

}
