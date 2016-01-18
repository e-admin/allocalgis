/**
 * UsosFuncionalesJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.UsoFuncional;
/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 23-ago-2006
 * Time: 10:07:03
 * To change this template use File | Settings | File Templates.
 */
public class UsosFuncionalesJPanel  extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppContext aplicacion;

    private ListaPane usosListaPane;
    private UsoFuncional selected;
    private String operacion;

    /**
     * Método que genera el panel de los Usos funcionales de un bien
     */
    public UsosFuncionalesJPanel() throws Exception {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    public void initComponents() throws Exception{

        usoJLabel= new InventarioLabel();
        superficieJLabel= new InventarioLabel();

      //  usoEJCBox= new ComboBoxEstructuras(Estructuras.getListaUsosFuncionales(), null, locale, false);
        usoEJCBox = new com.geopista.app.utilidades.TextField(100);
        usoEJCBox.setEnabled(false);
        superficieJTField= new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.REAL, new Long(999999), true, 2);
        superficieJTField.setSignAllowed(false);
        superficieJTField.setEnabled(false);

        addJButton= new JButton();
        addJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoAdd);
        addJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJButtonActionPerformed();
            }
        });

        deleteJButton= new JButton();
        deleteJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoBorrar);
        deleteJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJButtonActionPerformed();
            }
        });
        deleteJButton.setEnabled(false);

        editJButton= new JButton();
        editJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoEditar);
        editJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editJButtonActionPerformed();
            }
        });
        editJButton.setEnabled(false);

        okJButton= new JButton();
        okJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoOK);
        okJButton.setEnabled(false);
        okJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okJButtonActionPerformed();
            }
        });
        okJButton.setEnabled(false);


        usosListaPane= new ListaPane(null);
        usosListaPane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaPaneActionPerformed();
            }
        });

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(usosListaPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 400, 350));
        add(addJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 20, 20));
        add(deleteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 20, 20));
        add(editJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 20, 20));
        add(usoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 150, 20));
        add(superficieJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 300, 20));

        add(usoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, 180, 20));
        add(superficieJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 390, 180, 20));
        add(okJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 390, 20, 20));

    }

    /**
     * Método que carga la lista de usos funcionales de un bien
     * @param obj
     */
    public void load(InmuebleBean inmueble){
        if (inmueble==null) return;
       	load(inmueble.getUsosFuncionales());
    }

    /**
     * Método que carga la lista de Observaciones
     * @param bien
     */
    public void load(Collection<UsoFuncional> usosFuncionales){
    	
        usosListaPane.setCollection(usosFuncionales);
        usosListaPane.actualizarModelo();
    }
    /**
     * Método que visualiza el uso funcional seleccionado de la lista
     * @param uso seleccionado
     */
    public void load(UsoFuncional uso){
        if (uso==null) return;
        usoEJCBox.setText(uso.getUso()!=null?uso.getUso():"");
        if (uso.getSuperficie()!=-1)
            try{superficieJTField.setNumber(new Double(uso.getSuperficie()));}catch(Exception e){}
        else superficieJTField.setText("");
    }

    public void actualizarDatos(InmuebleBean inmueble){
        if (inmueble == null) return;
          inmueble.setUsosFuncionales(usosListaPane.getCollection());
    }

    private void listaPaneActionPerformed(){
        clear();
        usoEJCBox.setEnabled(false);
        superficieJTField.setEnabled(false);
        UsoFuncional uso= (UsoFuncional)usosListaPane.getSelected();
        if (uso!=null && operacion!=null && !operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            /* descomentar en caso de que se pueda editar un uso */
            editJButton.setEnabled(true);
            deleteJButton.setEnabled(true);
        }
        load(uso);
    }

    public void clear(){
        usoEJCBox.setText("");
        superficieJTField.setText("");

    }

    public void setEnabled(boolean b){
        usoEJCBox.setEnabled(b);
        superficieJTField.setEnabled(b);
        addJButton.setEnabled(b);
        deleteJButton.setEnabled(false);
        okJButton.setEnabled(false);
        editJButton.setEnabled(false);

    }

    public void renombrarComponentes(){
        renombrarJListComponent();
        try{usoJLabel.setText(aplicacion.getI18nString("inventario.usosFuncionales.tag2"));}catch(Exception e){}
        try{superficieJLabel.setText(aplicacion.getI18nString("inventario.usosFuncionales.tag3"));}catch(Exception e){}
        try{addJButton.setToolTipText(aplicacion.getI18nString("inventario.usosFuncionales.tag4"));}catch(Exception e){}
        try{deleteJButton.setToolTipText(aplicacion.getI18nString("inventario.usosFuncionales.tag5"));}catch(Exception e){}
        try{editJButton.setToolTipText(aplicacion.getI18nString("inventario.usosFuncionales.tag6"));}catch(Exception e){}
        try{okJButton.setToolTipText(aplicacion.getI18nString("inventario.usosFuncionales.tag7"));}catch(Exception e){}
    }

    /**
     * Método que renombra el panel de la lista
     */
    public void renombrarJListComponent(){
        try{usosListaPane.renombrar(aplicacion.getI18nString("inventario.usosFuncionales.tag1"));}catch(Exception e){};
    }

    /**
     * Método que recoge el uso funcional añadido por el usuario
     */
    private UsoFuncional getUso(){
        return updateUso(new UsoFuncional());

    }

    /**
     * Método que recoge el uso funcional actualizado por el usuario
     */
    private UsoFuncional updateUso(UsoFuncional uso){
        if (uso==null) return null;
        if (usoEJCBox.getText()==null){
            clear();
            return null;
        }
        uso.setUso(usoEJCBox.getText());
        try{uso.setSuperficie(((Double)superficieJTField.getNumber()).doubleValue());}catch(Exception e){}
        return uso;
    }

    private void addJButtonActionPerformed(){
        usosListaPane.clearSeleccion();
        clear();
        selected= null;
        editJButton.setEnabled(false);
        deleteJButton.setEnabled(false);
        okJButton.setEnabled(true);
        usoEJCBox.setEnabled(true);
        superficieJTField.setEnabled(true);
        usoEJCBox.requestFocusInWindow();

    }

    private void deleteJButtonActionPerformed(){
        if (usosListaPane.getSelected() == null) return;
        if (confirmDelete()){
            usosListaPane.borrar();
            editJButton.setEnabled(false);
            okJButton.setEnabled(false);
        }
    }


    private void editJButtonActionPerformed(){
        selected= (UsoFuncional)usosListaPane.getSelected();
        if (selected!=null){
            superficieJTField.setEnabled(true);
            usoEJCBox.setEnabled(true);
            usoEJCBox.requestFocusInWindow();
            okJButton.setEnabled(true);
        }

    }

    private void okJButtonActionPerformed(){
        if (selected == null){
            usosListaPane.add(getUso());
        }else{
            usosListaPane.update(updateUso(selected));
            usosListaPane.seleccionar(selected);

        }
        selected= null;
        okJButton.setEnabled(false);
        usoEJCBox.setEnabled(false);
        superficieJTField.setEnabled(false);
    }

    /**
     * Método que abre una ventana de confirmacion de borrado de un elemento de la lista
     * @return false si la confirmacion es negativa, true en caso contrario
     */
    private boolean confirmDelete(){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("inventario.usosFuncionales.tag8"), aplicacion.getI18nString("inventario.usosFuncionales.tag9"), JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    private javax.swing.JLabel usoJLabel;
    private javax.swing.JLabel superficieJLabel;

    private com.geopista.app.utilidades.JNumberTextField superficieJTField;
    private com.geopista.app.utilidades.TextField usoEJCBox;
    private JButton addJButton;
    private JButton deleteJButton;
    private JButton editJButton;
    private JButton okJButton;


}
