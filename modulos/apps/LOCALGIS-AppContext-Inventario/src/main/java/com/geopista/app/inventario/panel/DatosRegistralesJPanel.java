/**
 * DatosRegistralesJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.protocol.inventario.RegistroBean;

public class DatosRegistralesJPanel  extends javax.swing.JPanel {
    private AppContext aplicacion;

    /**
     * Método que genera el panel de los datos Registrales de un bien inmueble
     */
    public DatosRegistralesJPanel() {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    private void initComponents() {
        notarioJLabel = new InventarioLabel();
        protocoloJLabel = new InventarioLabel();
        registroPropiedadJLabel = new InventarioLabel();
        tomoJLabel = new InventarioLabel();
        libroJLabel = new InventarioLabel();
        folioJLabel = new InventarioLabel();
        fincaJLabel= new InventarioLabel();
        numInscripcionJLabel= new InventarioLabel();

        notarioJTField = new InventarioTextField(254);
        protocoloJTField = new InventarioTextField(50);
        registroPropiedadJTField= new InventarioTextField(50);
        tomoJNField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(9999), true);
        tomoJNField.setSignAllowed(false);
        libroJTField= new InventarioTextField(50);
        folioJTField= new InventarioTextField(50);
        fincaJNField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(999999), true);
        fincaJNField.setSignAllowed(false);
        numInscripcionJTField=  new InventarioTextField(50);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(notarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 20));
        add(protocoloJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 55, 110, 20));
        add(registroPropiedadJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 110, 20));
        add(tomoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 110, 20));
        add(libroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 110, 20));
        add(folioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 155, 110, 20));
        add(fincaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 110, 20));
        add(numInscripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 205, 110, 20));

        add(notarioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 310, -1));
        add(protocoloJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 55, 310, -1));
        add(registroPropiedadJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 160, -1));
        add(tomoJNField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 105, 50, -1));
        add(libroJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 310, -1));
        add(folioJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 155, 310, -1));
        add(fincaJNField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 50, -1));
        add(numInscripcionJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 205, 310, -1));
    }

    /**
     * Método que actualiza los datos registrales para un bien
     * @return
     */
    public RegistroBean actualizarDatos(){
        RegistroBean registro = new RegistroBean();
        registro.setNotario(notarioJTField.getText().trim());
        registro.setProtocolo(protocoloJTField.getText().trim());
        registro.setPropiedad(registroPropiedadJTField.getText().trim());
        try{registro.setTomo(tomoJNField.getNumber().toString().trim());}catch(Exception e){}
        registro.setLibro(libroJTField.getText().trim());
        registro.setFolio(folioJTField.getText().trim());
        try{registro.setFinca(fincaJNField.getNumber().toString().trim());}catch(Exception e){}
        registro.setInscripcion(numInscripcionJTField.getText().trim());
        return registro;
    }

    /**
     * Método que carga en el panel los datos registrales de un bien
     * @param registro a cargar en el panel
     */
    public void load(RegistroBean registro){
        if (registro == null) return;
        notarioJTField.setText(registro.getNotario()!=null?registro.getNotario():"");
        protocoloJTField.setText(registro.getProtocolo()!=null?registro.getProtocolo():"");
        registroPropiedadJTField.setText(registro.getPropiedad()!=null?registro.getPropiedad():"");
        tomoJNField.setText(registro.getTomo()!=null?registro.getTomo():"");
        libroJTField.setText(registro.getLibro()!=null?registro.getLibro():"");
        folioJTField.setText(registro.getFolio()!=null?registro.getFolio():"");
        fincaJNField.setText(registro.getFinca()!=null?registro.getFinca():"");
        numInscripcionJTField.setText(registro.getInscripcion()!=null?registro.getInscripcion():"");


    }

    public void clear(){
        notarioJTField.setText("");
        protocoloJTField.setText("");
        registroPropiedadJTField.setText("");
        tomoJNField.setText("");
        libroJTField.setText("");
        folioJTField.setText("");
        fincaJNField.setText("");
        numInscripcionJTField.setText("");
    }



    public void setEnabled(boolean b){
        notarioJTField.setEnabled(b);
        protocoloJTField.setEnabled(b);
        registroPropiedadJTField.setEnabled(b);
        tomoJNField.setEnabled(b);
        libroJTField.setEnabled(b);
        folioJTField.setEnabled(b);
        fincaJNField.setEnabled(b);
        numInscripcionJTField.setEnabled(b);
    }


    private void renombrarComponentes(){
        try{setBorder(new javax.swing.border.TitledBorder(aplicacion.getI18nString("inventario.datosRegistrales.tag1")));}catch(Exception e){}
        try{notarioJLabel.setText(aplicacion.getI18nString("inventario.datosRegistrales.tag2")+":");}catch(Exception e){}
        try{protocoloJLabel.setText(aplicacion.getI18nString("inventario.datosRegistrales.tag3")+":");}catch(Exception e){}
        try{registroPropiedadJLabel.setText(aplicacion.getI18nString("inventario.datosRegistrales.tag4")+":");}catch(Exception e){}
        try{tomoJLabel.setText(aplicacion.getI18nString("inventario.datosRegistrales.tag5")+":");}catch(Exception e){}
        try{libroJLabel.setText(aplicacion.getI18nString("inventario.datosRegistrales.tag6")+":");}catch(Exception e){}
        try{folioJLabel.setText(aplicacion.getI18nString("inventario.datosRegistrales.tag7")+":");}catch(Exception e){}
        try{fincaJLabel.setText(aplicacion.getI18nString("inventario.datosRegistrales.tag8")+":");}catch(Exception e){}
        try{numInscripcionJLabel.setText(aplicacion.getI18nString("inventario.datosRegistrales.tag9")+":");}catch(Exception e){}
    }


    private javax.swing.JLabel notarioJLabel;
    private javax.swing.JTextField notarioJTField;
    private javax.swing.JLabel protocoloJLabel;
    private javax.swing.JTextField protocoloJTField;
    private javax.swing.JTextField registroPropiedadJTField;
    private javax.swing.JLabel registroPropiedadJLabel;
    private javax.swing.JLabel tomoJLabel;
    private com.geopista.app.utilidades.JNumberTextField tomoJNField;
    private javax.swing.JLabel libroJLabel;
    private javax.swing.JTextField libroJTField;
    private javax.swing.JLabel folioJLabel;
    private javax.swing.JTextField folioJTField;
    private javax.swing.JLabel fincaJLabel;
    private com.geopista.app.utilidades.JNumberTextField fincaJNField;
    private javax.swing.JLabel numInscripcionJLabel;
    private javax.swing.JTextField numInscripcionJTField;

}
