/**
 * DatosEIELJPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.panel;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.inventario.Constantes;
import com.geopista.app.inventario.IntegracionEIELTableModel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.inventario.BienBean;
import com.geopista.protocol.inventario.InventarioClient;
import com.geopista.protocol.inventario.InventarioEIELBean;
import com.geopista.protocol.inventario.ListaEIEL;


public class DatosEIELJPanel  extends javax.swing.JPanel {
    private AppContext aplicacion;
    private InventarioClient inventarioClient = null;
	private String locale;
	private InventarioEIELBean eielSelected = null;
	BienBean bien = null;

    /**
     * Método que genera el panel de los datos Registrales de un bien inmueble
     */
    public DatosEIELJPanel(String locale) {
        aplicacion= (AppContext) AppContext.getApplicationContext();
    	inventarioClient = new InventarioClient(
				aplicacion
						.getString(UserPreferenceConstants.LOCALGIS_SERVER_ADMCAR_SERVLET_URL)
						+ Constantes.INVENTARIO_SERVLET_NAME);
        this.locale = locale;
        initComponents();
		actualizarModelo();
		renombrarComponentes();
    }

    public void load(BienBean bien){
        if (bien == null) return;
        this.bien = bien;
        inicializaListaEIEL();

    }
    
	private void inicializaListaEIEL() {
		listaEIEL = new ListaEIEL(); 
		try {
			listaEIEL.sethEIEL(inventarioClient.getDatosEIELBien(bien.getId()));
			comprobarElementosEIEL();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actualizarModelo();
	}
	   
    private void initComponents() {
    	

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jButtonComprobar= new javax.swing.JButton();

        jButtonComprobar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				comprobarActionPerformed();
			}
		});
        jButtonComprobar.setEnabled(false);
		
		jTableIntegracionEIEL = new javax.swing.JTable();
		listaEIELJScrollPane = new javax.swing.JScrollPane();
		
	
		listaEIELJScrollPane.setViewportView(jTableIntegracionEIEL);

		add(listaEIELJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 430, 500));		
		add(jButtonComprobar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 525, 230, 20));
    }
    
	private void actualizarModelo() {
		modelIntegracionEIEL = new IntegracionEIELTableModel(locale);
		modelIntegracionEIEL.setModelData(listaEIEL);
		sorter = new TableSorted(modelIntegracionEIEL);
		sorter.setTableHeader(jTableIntegracionEIEL.getTableHeader());
		jTableIntegracionEIEL.setModel(sorter);
		TableColumn column = jTableIntegracionEIEL.getColumnModel().getColumn(
				IntegracionEIELTableModel.idIndex);
		column.setPreferredWidth(5);
		column = jTableIntegracionEIEL.getColumnModel().getColumn(
				IntegracionEIELTableModel.idNombre);
		column.setPreferredWidth(15);
		jTableIntegracionEIEL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
    private void renombrarComponentes(){

		TableColumn tableColumn = jTableIntegracionEIEL.getColumnModel().getColumn(0);
		tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.datosEIEL.menu.lista.elemento1"));
		tableColumn = jTableIntegracionEIEL.getColumnModel().getColumn(1);
		tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.datosEIEL.menu.lista.elemento2"));
		tableColumn = jTableIntegracionEIEL.getColumnModel().getColumn(2);
		tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.datosEIEL.menu.lista.elemento3"));
		tableColumn = jTableIntegracionEIEL.getColumnModel().getColumn(3);
		tableColumn.setHeaderValue(aplicacion.getI18nString("inventario.datosEIEL.menu.lista.elemento4"));
		jButtonComprobar.setText(aplicacion.getI18nString("inventario.datosEIEL.menu.comprobar"));
    }
	
private void comprobarElementosEIEL() {

		if ((listaEIEL.gethEIEL().isEmpty())){
			jButtonComprobar.setEnabled(false);
		}
		else{
			jButtonComprobar.setEnabled(true);
		}
		
	}

private void comprobarActionPerformed() {
	
	try {
		Hashtable hs = inventarioClient.getComprobarIntegEIELInventario(bien);
		if (!hs.isEmpty()){
			StringBuffer elementosNoContenidos = new StringBuffer(aplicacion.getI18nString("inventario.datosEIEL.geometriaNoContenida.mensaje"));
			elementosNoContenidos.append("\n");
			for (Enumeration e=hs.elements();e.hasMoreElements();) {
				Vector vComp =(Vector) e.nextElement();
				elementosNoContenidos.append(vComp.get(0)+" - "+vComp.get(2)+" - "+vComp.get(1));
			}
			JOptionPane.showMessageDialog(this, elementosNoContenidos.toString(), aplicacion.getI18nString("inventario.datosEIEL.geometriaNoContenida.aviso"), JOptionPane.INFORMATION_MESSAGE);
		}
		else
			JOptionPane.showMessageDialog(this, aplicacion.getI18nString("inventario.datosEIEL.geometriaNoContenida.mensaje2"), aplicacion.getI18nString("inventario.datosEIEL.geometriaNoContenida.info"), JOptionPane.INFORMATION_MESSAGE);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	private javax.swing.JButton jButtonComprobar;
    private ListaEIEL listaEIEL = null;
    private javax.swing.JScrollPane listaEIELJScrollPane;
    private IntegracionEIELTableModel modelIntegracionEIEL;
	private TableSorted sorter;
	private javax.swing.JTable jTableIntegracionEIEL; 

}
