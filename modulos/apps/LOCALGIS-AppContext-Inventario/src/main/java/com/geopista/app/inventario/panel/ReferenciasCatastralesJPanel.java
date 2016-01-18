/**
 * ReferenciasCatastralesJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.utilidades.TextPane;
import com.geopista.protocol.inventario.InmuebleBean;
import com.geopista.protocol.inventario.ReferenciaCatastral;
import com.geopista.protocol.inventario.ViaBean;
import com.geopista.protocol.licencias.CReferenciaCatastral;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

/**
 * Created by IntelliJ IDEA.
 * User: charo
 * Date: 10-ago-2006
 * Time: 9:42:49
 * To change this template use File | Settings | File Templates.
 */
public class ReferenciasCatastralesJPanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AppContext aplicacion;

    private ListaPane referenciasListaPane;
    private ReferenciaCatastral selected;
    private String operacion;
   
	private CReferenciaCatastral referenciaCatastral = null;

    /**
     * Método que genera el panel de las Referencias Catastrales de un bien
     */
    public ReferenciasCatastralesJPanel() throws Exception {
        aplicacion= (AppContext) AppContext.getApplicationContext();
        initComponents();
        renombrarComponentes();
    }

    public void initComponents() throws Exception{

        referenciaCatastralJLabel= new InventarioLabel();
        descripcionJLabel= new InventarioLabel();

        descripcionJScrollPane= new javax.swing.JScrollPane();
        descripcionJTArea= new InventarioTextPane(999);
        descripcionJTArea.setEnabled(false);
        referenciaCatastralJTField= new InventarioTextField(14);
        referenciaCatastralJTField.setEnabled(false);

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

        buscarRefJButton= new JButton();
        buscarRefJButton.setIcon(com.geopista.app.inventario.UtilidadesComponentes.iconoBuscar);
        buscarRefJButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarRefJButtonActionPerformed();
            }
        });
        buscarRefJButton.setEnabled(false);

        referenciasListaPane= new ListaPane(null);
        referenciasListaPane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaPaneActionPerformed();
            }
        });

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(referenciasListaPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 400, 350));
        add(addJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 30, 20, 20));
        add(deleteJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, 20, 20));
        add(editJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 20, 20));
        add(referenciaCatastralJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 150, 20));
        add(descripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 300, 20));

        add(referenciaCatastralJTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 370, 160, 20));
        add(buscarRefJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 370, 20, 20));
        descripcionJScrollPane.setViewportView(descripcionJTArea);
        add(descripcionJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, 400, 60));
        add(okJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 410, 20, 20));

    }

    /**
     * Método que carga la lista de Referencias Catastrales de un bien
     * @param obj
     */
    public void load(Object obj){
        if (obj==null) return;
        if (obj instanceof InmuebleBean){
            referenciasListaPane.setCollection(((InmuebleBean)obj).getReferenciasCatastrales());
        }
        if (obj instanceof ViaBean){
            referenciasListaPane.setCollection(((ViaBean)obj).getReferenciasCatastrales());
        }
        referenciasListaPane.actualizarModelo();
    }

    /**
     * Método que visualiza la referencia catastral seleccionada de la lista
     * @param ref seleccionada
     */
    public void load(ReferenciaCatastral ref){
        if (ref==null) return;
        referenciaCatastralJTField.setText(ref.getRefCatastral()!=null?ref.getRefCatastral():"");
        descripcionJTArea.setText(ref.getDescripcion()!=null?ref.getDescripcion():"");
    }

    public void actualizarDatos(Object obj){
        if (obj == null) return;
        if (obj instanceof InmuebleBean)
            ((InmuebleBean)obj).setReferenciasCatastrales(referenciasListaPane.getCollection());
        else{
        	if (obj instanceof ViaBean)
        		((ViaBean)obj).setReferenciasCatastrales(referenciasListaPane.getCollection());
        }
    }

    private void listaPaneActionPerformed(){
        clear();
        descripcionJTArea.setEnabled(false);
        ReferenciaCatastral ref= (ReferenciaCatastral)referenciasListaPane.getSelected();
        if (ref!=null && operacion!=null && !operacion.equalsIgnoreCase(Constantes.OPERACION_CONSULTAR)){
            editJButton.setEnabled(true);
            deleteJButton.setEnabled(true);
        }
        load(ref);
    }

    public void clear(){
        referenciaCatastralJTField.setText("");
        descripcionJTArea.setText("");

    }

    public void setEnabled(boolean b){
        descripcionJTArea.setEnabled(b);
        addJButton.setEnabled(b);
        deleteJButton.setEnabled(false);
        okJButton.setEnabled(false);
        editJButton.setEnabled(false);
        buscarRefJButton.setEnabled(false);

    }

    public void renombrarComponentes(){
        renombrarJListComponent();
        try{referenciaCatastralJLabel.setText(aplicacion.getI18nString("inventario.refCatastrales.tag2"));}catch(Exception e){}
        try{descripcionJLabel.setText(aplicacion.getI18nString("inventario.refCatastrales.tag3"));}catch(Exception e){}
        try{addJButton.setToolTipText(aplicacion.getI18nString("inventario.refCatastrales.tag4"));}catch(Exception e){}
        try{deleteJButton.setToolTipText(aplicacion.getI18nString("inventario.refCatastrales.tag5"));}catch(Exception e){}
        try{editJButton.setToolTipText(aplicacion.getI18nString("inventario.refCatastrales.tag6"));}catch(Exception e){}
        try{okJButton.setToolTipText(aplicacion.getI18nString("inventario.refCatastrales.tag7"));}catch(Exception e){}
        try{buscarRefJButton.setToolTipText(aplicacion.getI18nString("inventario.refCatastrales.tag10"));}catch(Exception e){}
    }

    /**
     * Método que renombra el panel de la lista
     */
    public void renombrarJListComponent(){
        try{referenciasListaPane.renombrar(aplicacion.getI18nString("inventario.refCatastrales.tag1"));}catch(Exception e){};
    }

    /**
     * Método que recoge la referencia catastral añadida por el usuario
     */
    private ReferenciaCatastral getReferenciaCatastral(){
        return updateReferenciaCatastral(new ReferenciaCatastral());

    }

    /**
     * Método que recoge la referencia catastral actualizada por el usuario
     */
    private ReferenciaCatastral updateReferenciaCatastral(ReferenciaCatastral ref){
        if (ref==null) return null;
        if (referenciaCatastralJTField.getText().trim().equalsIgnoreCase("")){
            clear();
            return null;
        }
        ref.setRefCatastral(referenciaCatastralJTField.getText().trim());
        ref.setDescripcion(descripcionJTArea.getText().trim());
        return ref;
    }

    private void buscarRefJButtonActionPerformed(){
        
    	CReferenciaCatastralInventarioJDialog dialog = new CReferenciaCatastralInventarioJDialog(aplicacion.getMainFrame(), true, aplicacion.getI18NResource(), ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    	//dialog.setLocation(20, 20);
    	GUIUtil.centreOnWindow(dialog);
        dialog.setResizable(false);
        dialog.setVisible(true);
        
        try {
        	
        	referenciaCatastral  = dialog.getReferenciasCatastrales();
        	
        	if (referenciaCatastral!= null && referenciaCatastral.getReferenciaCatastral()!=null){
        		referenciaCatastralJTField.setText(referenciaCatastral.getReferenciaCatastral());
        	}

        }catch (Exception e){}

    }
    
	private void addJButtonActionPerformed(){
        referenciasListaPane.clearSeleccion();
        clear();
        selected= null;
        editJButton.setEnabled(false);
        deleteJButton.setEnabled(false);
        okJButton.setEnabled(true);
        buscarRefJButton.setEnabled(true);
        descripcionJTArea.setEnabled(true);
        referenciaCatastralJTField.setEnabled(true);
        descripcionJTArea.requestFocusInWindow();

    }

    private void deleteJButtonActionPerformed(){
        if (referenciasListaPane.getSelected() == null) return;
        if (confirmDelete()){
            referenciasListaPane.borrar();
            editJButton.setEnabled(false);
            okJButton.setEnabled(false);
            buscarRefJButton.setEnabled(false);
        }
    }


    private void editJButtonActionPerformed(){
        selected= (ReferenciaCatastral)referenciasListaPane.getSelected();
        if (selected!=null){
            descripcionJTArea.setEnabled(true);
            referenciaCatastralJTField.setEnabled(false);
            descripcionJTArea.requestFocusInWindow();
            okJButton.setEnabled(true);
        }

    }

    private void okJButtonActionPerformed(){
        if (selected == null){
            referenciasListaPane.add(getReferenciaCatastral());
        }else{
            referenciasListaPane.update(updateReferenciaCatastral(selected));
            referenciasListaPane.seleccionar(selected);

        }
        selected= null;
        okJButton.setEnabled(false);
        buscarRefJButton.setEnabled(false);
        descripcionJTArea.setEnabled(false);
        referenciaCatastralJTField.setEnabled(false);
    }

    /**
     * Método que abre una ventana de confirmacion de borrado de un elemento de la lista
     * @return false si la confirmacion es negativa, true en caso contrario
     */
    private boolean confirmDelete(){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, aplicacion.getI18nString("inventario.refCatastrales.tag8"), aplicacion.getI18nString("inventario.refCatastrales.tag9"), JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    private javax.swing.JLabel referenciaCatastralJLabel;
    private javax.swing.JLabel descripcionJLabel;

    private javax.swing.JTextField referenciaCatastralJTField;
    private javax.swing.JScrollPane descripcionJScrollPane;
    private TextPane descripcionJTArea;
    private JButton addJButton;
    private JButton deleteJButton;
    private JButton editJButton;
    private JButton okJButton;
    private JButton buscarRefJButton;


}
