/**
 * JPanelExpediente.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.licencias.utilidades;

import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.geopista.app.licencias.CUtilidadesComponentes;
import com.geopista.app.licencias.estructuras.Estructuras;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.Resolucion;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 10-jun-2005
 * Time: 14:17:19
 */
public class JPanelExpediente extends JPanel {
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JPanelResolucion.class);
    private Resolucion resolucion;
    private JFrame padre;
    private ResourceBundle messages;

    /** Creates new form JPanelResolucion */
    public JPanelExpediente(JFrame padre,ResourceBundle messages) {
        this.messages=messages;
        this.padre=padre;
        initComponents();
        changeScreenLang(messages);
        setEnabled(false);
    }

     private void initComponents() {
         responsableTField= new com.geopista.app.utilidades.TextField(68);
         estadoExpedienteJLabel = new javax.swing.JLabel();
         numExpedienteJLabel = new javax.swing.JLabel();
         jLabelEstadoActual=new javax.swing.JLabel();
         servicioExpedienteJLabel = new javax.swing.JLabel();
         tramitacionJLabel = new javax.swing.JLabel();



         setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
         setAutoscrolls(true);
         add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 170, 20));
         add(numExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 170, 20));
         add(responsableTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 260, 300, -1));
         add(jLabelEstadoActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 170, 20));
         add(servicioExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 170, 20));
         add(tramitacionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 170, 20));
         add(asuntoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 170, 20));
         add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 170, 20));
         add(observacionesExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 170, 20));
         add(jPanelResolucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 380,500, 175));
         add(servicioExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 130, 300, -1));
         add(numExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 280, -1));
         add(asuntoExpedienteJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 300, -1));
         add(fechaAperturaJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 300, -1));
         add(inicioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 170, 20));
         add(inicioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 210, 300, -1));
         add(silencioJCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 30, -1));
         add(buscarExpedienteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 30, 20, 20));
         add(consultarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 130, -1));
         add(silencioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 170, 20));
         add(notaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, 260, 20));
         add(finalizacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 560, 300, -1));
         add(finalizaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, -1, -1));
         add(tramitacionEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 300, 20));
         add(observacionesExpedienteJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 290, 300, 90));
         add(estadoExpedienteJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 300, 20));
         add(responsableJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 170, 20));

     }
     public void changeScreenLang(ResourceBundle mensajes)
     {
       try
       {
           this.messages=mensajes;
           setBorder(new javax.swing.border.TitledBorder(mensajes.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.expedienteJPanel.TitleBorder")));
           estadoExpedienteJLabel.setText(CUtilidadesComponentes.getLabelConAsterisco(mensajes.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.estadoExpedienteJLabel.text")));
           numExpedienteJLabel.setText(mensajes.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.numExpedienteJLabel.text"));
           servicioExpedienteJLabel.setText(mensajes.getString("CModificacionLicenciasForm.ObraMenorJMenuItem.servicioExpedienteJLabel.text"));


       }catch(Exception e)
       {
           logger.error("Error al cargar los recuros:", e);
       }
    }
    public void load(CExpedienteLicencia expediente)
    {
        responsableTField.setText((expediente==null||expediente.getResponsable()==null?"":expediente.getResponsable()));
        jLabelEstadoActual.setText(((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(expediente.getEstado().getIdEstado()).toString())).getTerm(messages.getLocale().toString()));


    }
    public CExpedienteLicencia save(CExpedienteLicencia expediente)
    {
        expediente.setResponsable(responsableTField.getText().trim());
        return expediente;
    }
    private javax.swing.JLabel estadoExpedienteJLabel;
    private com.geopista.app.utilidades.TextField responsableTField;
    private javax.swing.JLabel numExpedienteJLabel;
    private javax.swing.JLabel jLabelEstadoActual;
    private javax.swing.JLabel servicioExpedienteJLabel;
    private javax.swing.JLabel tramitacionJLabel;
    private javax.swing.JLabel asuntoExpedienteJLabel;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JLabel observacionesExpedienteJLabel;
    private JPanelResolucion jPanelResolucion;
    private javax.swing.JTextField servicioExpedienteJTField;
    private javax.swing.JTextField numExpedienteJTField;
    private javax.swing.JTextField asuntoExpedienteJTField;
    private javax.swing.JTextField fechaAperturaJTField;
    private javax.swing.JLabel inicioJLabel;
    private javax.swing.JTextField inicioJTField;
    private javax.swing.JCheckBox silencioJCheckBox;
    private javax.swing.JButton buscarExpedienteJButton;
    private javax.swing.JButton consultarJButton;
    private javax.swing.JLabel silencioJLabel;
    private javax.swing.JLabel notaJLabel;
    private ComboBoxEstructuras finalizacionEJCBox;
    private javax.swing.JLabel finalizaJLabel;
    private ComboBoxEstructuras tramitacionEJCBox;
    private javax.swing.JScrollPane observacionesExpedienteJScrollPane;
    private javax.swing.JComboBox estadoExpedienteJCBox;
    private javax.swing.JLabel responsableJLabel;
    

}
