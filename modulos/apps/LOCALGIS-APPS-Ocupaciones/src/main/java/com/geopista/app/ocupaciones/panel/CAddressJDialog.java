/**
 * CAddressJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * JSearch.java
 *
 * Created on 4 de mayo de 2004, 12:04
 */

package com.geopista.app.ocupaciones.panel;

import java.awt.Cursor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.geopista.app.ocupaciones.CMainOcupaciones;
import com.geopista.app.ocupaciones.CSearchDialogTableModel;
import com.geopista.app.ocupaciones.CUtilidadesComponentes;
import com.geopista.app.ocupaciones.Estructuras;
import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CReferenciaCatastral;

/**
 * @author avivar
 */
public class CAddressJDialog extends javax.swing.JDialog {
	Logger logger = Logger.getLogger(CAddressJDialog.class);

    Hashtable hReferencias= new Hashtable();

    TableSorted addressesJTModelSorted= new TableSorted();
    private ResourceBundle literales= null;
    private String locale= null;

	/**
	 * Creates new form JSearch
	 */
	public CAddressJDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		configureComponents();
	}

    public CAddressJDialog(java.awt.Frame parent, boolean modal, ResourceBundle literales, String locale) {
        super(parent, modal);
        this.literales= literales;
        this.locale= locale;
        initComponents();
        configureComponents();
    }

	private void configureComponents() {
        if (literales == null) literales= CMainOcupaciones.literales;
        if (locale == null) locale= CMainOcupaciones.currentLocale.toString();
        addressesJTableModel = new CSearchDialogTableModel(new String[]{literales.getString("CAddressJDialog.addressesJTableModel.text.column1"),
                                                                        literales.getString("CAddressJDialog.addressesJTableModel.text.column2"),
                                                                        literales.getString("CAddressJDialog.addressesJTableModel.text.column3"),
                                                                        literales.getString("CAddressJDialog.addressesJTableModel.text.column4"),
                                                                        literales.getString("CAddressJDialog.addressesJTableModel.text.column5"),
                                                                        literales.getString("CAddressJDialog.addressesJTableModel.text.column6"),
                                                                        literales.getString("CAddressJDialog.addressesJTableModel.text.column7"),
                                                                        literales.getString("CAddressJDialog.addressesJTableModel.text.column8"),
                                                                        literales.getString("CAddressJDialog.addressesJTableModel.text.column9")});

        addressesJTable.setModel(addressesJTableModel);
        addressesJTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addressesJTable.setRowSelectionAllowed(true);
        addressesJTable.setColumnSelectionAllowed(false);
        addressesJTable.getTableHeader().setReorderingAllowed(false);
        addressesJTModelSorted= new TableSorted(addressesJTableModel);
        addressesJTModelSorted.setTableHeader(addressesJTable.getTableHeader());
        addressesJTable.setModel(addressesJTModelSorted);


        numeroViaNumberTField=  new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Integer(99999), true);
        numeroViaNumberTField.setEditable(true);
        jPanel1.add(numeroViaNumberTField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 100, -1));

		renombrarComponentes();
	}


	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        aceptarJButton = new javax.swing.JButton();
        cancelarJButton = new javax.swing.JButton();
        buscarJButton = new javax.swing.JButton();
        addressesJScrollPane = new javax.swing.JScrollPane();
        addressesJTable = new javax.swing.JTable();
        nombreViaJLabel = new javax.swing.JLabel();
        numeroViaJLabel = new javax.swing.JLabel();
        nombreViaJTextField = new javax.swing.JTextField();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Di\u00e1logo de busqueda");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        aceptarJButton.setText("Aceptar");
        aceptarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aceptarJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 320, -1, -1));

        cancelarJButton.setText("Cancelar");
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 320, -1, -1));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed();
            }
        });

        jPanel1.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, -1, -1));

        addressesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo vía", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Nombre vía", "Nº"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        addressesJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        addressesJTable.setFocusCycleRoot(true);
        addressesJTable.setSurrendersFocusOnKeystroke(true);
        addressesJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                addressesJTableFocusGained(evt);
            }
        });
        addressesJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                addressesJTableKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addressesJTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                addressesJTableKeyReleased(evt);
            }
        });

        addressesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addressesJTableMouseClicked(evt);
            }
        });
        addressesJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                addressesJTableMouseDragged(evt);
            }
        });


        addressesJScrollPane.setViewportView(addressesJTable);

        jPanel1.add(addressesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 680, 180));

        nombreViaJLabel.setText("Nombre v\u00eda:");
        jPanel1.add(nombreViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 230, 20));

        numeroViaJLabel.setText("N\u00famero:");
        jPanel1.add(numeroViaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 230, 20));

        jPanel1.add(nombreViaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 400, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 350));

        pack();
    }//GEN-END:initComponents

    private void mostrarAddressSelected(){
        if(addressesJTable.getSelectedRowCount() == 1){
            int seleccion = addressesJTable.getSelectedRow();
            if (seleccion != -1){
                nombreViaJTextField.setText((String) addressesJTModelSorted.getValueAt(seleccion, 2));
                numeroViaNumberTField.setText((String) addressesJTModelSorted.getValueAt(seleccion, 3));
            }
        }

    }

	private void addressesJTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addressesJTableMouseClicked
        mostrarAddressSelected();
	}//GEN-LAST:event_addressesJTableMouseClicked
    private void addressesJTableMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resultadosJTableMouseDragged
        // TODO add your handling code here:
        mostrarAddressSelected();
    }//GEN-LAST:event_resultadosJTableMouseDragged

    private void addressesJTableFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resultadosJTableFocusGained
        // TODO add your handling code here:
        mostrarAddressSelected();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void addressesJTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resultadosJTableKeyReleased
        // TODO add your handling code here:
        mostrarAddressSelected();

    }//GEN-LAST:event_resultadosJTableKeyReleased

    private void addressesJTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resultadosJTableKeyPressed
        // TODO add your handling code here:
        mostrarAddressSelected();

    }//GEN-LAST:event_resultadosJTableKeyPressed

    private void addressesJTableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resultadosJTableKeyTyped
        // TODO add your handling code here:
        mostrarAddressSelected();

    }//GEN-LAST:event_resultadosJTableKeyTyped


	private void buscarJButtonActionPerformed() {//GEN-FIRST:event_buscarJButtonActionPerformed


		try {

			CUtilidadesComponentes.clearTable(addressesJTableModel);
			this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String numero= "";
            try{
                numero= numeroViaNumberTField.getNumber().toString();
            }catch(Exception e){}
			Hashtable hash = new Hashtable();
			hash.put("b.nombrecatastro", nombreViaJTextField.getText());
			hash.put("a.rotulo", numero);


			Vector referenciasCatastrales = COperacionesLicencias.getSearchedAddresses(hash);
			logger.info("referenciasCatastrales.size(): " + referenciasCatastrales.size());

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (referenciasCatastrales.size() > 0){
                for (int i = 0; i < referenciasCatastrales.size(); i++) {
                    CReferenciaCatastral referenciaCatastral = (CReferenciaCatastral) referenciasCatastrales.elementAt(i);

                    String tipoVia= "";
                    if ((referenciaCatastral.getTipoVia() != null) && (!referenciaCatastral.getTipoVia().trim().equalsIgnoreCase(""))){
                        tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(referenciaCatastral.getTipoVia()).getTerm(locale);
                    }

                    //"Ref.", "Tipo vía", "Nombre vía", "Nº", "Letra","Bloque","Esc.","Planta","Puerta"
                    addressesJTableModel.addRow(new String[]{referenciaCatastral.getReferenciaCatastral(),
                                                             tipoVia,
                                                             referenciaCatastral.getNombreVia(),
                                                             referenciaCatastral.getPrimerNumero(),
                                                             referenciaCatastral.getPrimeraLetra(),
                                                             referenciaCatastral.getBloque(),
                                                             referenciaCatastral.getEscalera(),
                                                             referenciaCatastral.getPlanta(),
                                                             referenciaCatastral.getPuerta()});

                }
            }else{
                mostrarMensaje(literales.getString("CAddressJDialog.mensaje1"));
            }
		} catch (Exception ex) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			logger.error("Exception: " + ex.toString());
		}

	}//GEN-LAST:event_buscarJButtonActionPerformed

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this.getParent(), mensaje);
    }


	private void cancelarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarJButtonActionPerformed

		dispose();

	}//GEN-LAST:event_cancelarJButtonActionPerformed

	private void aceptarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarJButtonActionPerformed

		for (int i = 0; i < addressesJTable.getSelectedRows().length; i++) {
			int selectedRow = addressesJTable.getSelectedRows()[i];

			CReferenciaCatastral referenciaCatastral= new CReferenciaCatastral((String) addressesJTableModel.getValueAt(selectedRow, 0),
					(String) addressesJTableModel.getValueAt(selectedRow, 1),
					(String) addressesJTableModel.getValueAt(selectedRow, 2),
					(String) addressesJTableModel.getValueAt(selectedRow, 3),
					(String) addressesJTableModel.getValueAt(selectedRow, 4),
					(String) addressesJTableModel.getValueAt(selectedRow, 5),
					(String) addressesJTableModel.getValueAt(selectedRow, 6),
					(String) addressesJTableModel.getValueAt(selectedRow, 7),
					(String) addressesJTableModel.getValueAt(selectedRow, 8));

            hReferencias.put(addressesJTableModel.getValueAt(selectedRow, 0), referenciaCatastral);
		}
		dispose();
	}//GEN-LAST:event_aceptarJButtonActionPerformed

    public Hashtable getReferencias(){
        return hReferencias;
    }

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		new CAddressJDialog(new javax.swing.JFrame(), true).show();
	}

	private DefaultTableModel addressesJTableModel;
    private com.geopista.app.utilidades.JNumberTextField numeroViaNumberTField;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JScrollPane addressesJScrollPane;
    private javax.swing.JTable addressesJTable;
    private javax.swing.JButton buscarJButton;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nombreViaJLabel;
    private javax.swing.JTextField nombreViaJTextField;
    private javax.swing.JLabel numeroViaJLabel;
    // End of variables declaration//GEN-END:variables


	private void renombrarComponentes() {

		try {
			setTitle(literales.getString("CAddressJDialog.JInternalFrame.title"));

			nombreViaJLabel.setText(literales.getString("CAddressJDialog.nombreViaJLabel.text"));
			numeroViaJLabel.setText(literales.getString("CAddressJDialog.numeroViaJLabel.text"));

			buscarJButton.setText(literales.getString("CAddressJDialog.buscarJButton.text"));
			cancelarJButton.setText(literales.getString("CAddressJDialog.cancelarJButton.text"));
			aceptarJButton.setText(literales.getString("CAddressJDialog.aceptarJButton.text"));

		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

}
