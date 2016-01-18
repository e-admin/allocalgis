/**
 * CSearchLicenciasObraJDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.ocupaciones.panel;

import java.awt.Cursor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.geopista.app.ocupaciones.CConstantesOcupaciones;
import com.geopista.app.ocupaciones.CMainOcupaciones;
import com.geopista.app.ocupaciones.CSearchDialogTableModel;
import com.geopista.app.ocupaciones.CUtilidades;
import com.geopista.app.ocupaciones.CUtilidadesComponentes;
import com.geopista.app.ocupaciones.ColorTableCellRenderer;
import com.geopista.app.ocupaciones.Estructuras;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.protocol.licencias.CExpedienteLicencia;
import com.geopista.protocol.licencias.COperacionesLicencias;
import com.geopista.protocol.licencias.CPersonaJuridicoFisica;

/**
 * @author avivar
 */
public class CSearchLicenciasObraJDialog extends javax.swing.JDialog {
	Logger logger = Logger.getLogger(CSearchLicenciasObraJDialog.class);
	private String selectedExpediente= "";
    private String selectedPatronTipoLicencia= null;
    private String selectedPatronTipoObra= null;

    private JFrame desktop;

    Vector expedientesLicencia= new Vector();
    TableSorted expedientesJTModelSorted= new TableSorted();
    int hiddenCol= 9;



	/**
	 * Creates new form JSearch
	 */
	public CSearchLicenciasObraJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        desktop= (JFrame)parent;

        initComponents();
        initComboBoxesEstructuras();
        configureComponents();
        renombrarComponentes();
	}

	public void initComboBoxesEstructuras(){
		while (!Estructuras.isCargada())
		{
			if (!Estructuras.isIniciada()) Estructuras.cargarEstructuras();
			try {Thread.sleep(500);}catch(Exception e){}
		}

        /** Inicializamos los comboBox que llevan estructuras */
        tipoLicenciaEJCBox= new ComboBoxEstructuras(Estructuras.getListaLicencias(), null, CMainOcupaciones.currentLocale.toString(), true);
        if (selectedPatronTipoLicencia != null){
            tipoLicenciaEJCBox.setSelectedPatron(selectedPatronTipoLicencia);
        }else tipoLicenciaEJCBox.setSelectedIndex(0);
        jPanel1.add(tipoLicenciaEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 340, 20));

        ((JComboBox)tipoLicenciaEJCBox).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoLicenciaEJCBoxActionPerformed(evt);
            }
        });


        if (tipoLicenciaEJCBox.getSelectedIndex() == 0) {
            tipoObraEJCBox= new ComboBoxEstructuras(new ListaEstructuras(), null, CMainOcupaciones.currentLocale.toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(false);
        }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesOcupaciones.ObraMayor).toString())){
            tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposObra(), null, CMainOcupaciones.currentLocale.toString(), true);
            if (selectedPatronTipoObra != null){
                tipoObraEJCBox.setSelectedPatron(selectedPatronTipoObra);
            }else tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(true);
        }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesOcupaciones.ObraMenor).toString())){
            tipoObraEJCBox= new ComboBoxEstructuras(Estructuras.getListaTiposObraMenor(), null, CMainOcupaciones.currentLocale.toString(), true);
            if (selectedPatronTipoObra != null){
                tipoObraEJCBox.setSelectedPatron(selectedPatronTipoObra);
            }else tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(true);
        }
        jPanel1.add(tipoObraEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 40, 340, 20));

		estadoEJCBox= new ComboBoxEstructuras(Estructuras.getListaEstados(), null, CMainOcupaciones.currentLocale.toString(), true);
        estadoEJCBox.setSelectedIndex(0);
		jPanel1.add(estadoEJCBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, 340, 20));



	}

    private void tipoLicenciaEJCBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (tipoLicenciaEJCBox.getSelectedIndex() == 0) {
            tipoObraEJCBox.removeAllItems();
            tipoObraEJCBox.setEstructuras(new ListaEstructuras(), null, CMainOcupaciones.currentLocale.toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(false);
        }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesOcupaciones.ObraMayor).toString())){
            tipoObraEJCBox.removeAllItems();
            tipoObraEJCBox.setEstructuras(Estructuras.getListaTiposObra(), null, CMainOcupaciones.currentLocale.toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(true);
        }else if (tipoLicenciaEJCBox.getSelectedPatron().equalsIgnoreCase(new Integer(CConstantesOcupaciones.ObraMenor).toString())){
            tipoObraEJCBox.removeAllItems();
            tipoObraEJCBox.setEstructuras(Estructuras.getListaTiposObraMenor(), null, CMainOcupaciones.currentLocale.toString(), true);
            tipoObraEJCBox.setSelectedIndex(0);
            tipoObraEJCBox.setEnabled(true);
        }

    }



	private void configureComponents() {

        expedientesJTableModel = new CSearchDialogTableModel(new String[]{CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.expedientesJTableModel.text.column1"),
                                                                          CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.expedientesJTableModel.text.column9"),
                                                                          CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.expedientesJTableModel.text.column2"),
                                                                          CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.expedientesJTableModel.text.column3"),
                                                                          CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.expedientesJTableModel.text.column4"),
                                                                          CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.expedientesJTableModel.text.column5"),
                                                                          CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.expedientesJTableModel.text.column6"),
                                                                          CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.expedientesJTableModel.text.column7"),
                                                                          CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.expedientesJTableModel.text.column8"),
                                                                          "HIDDEN"});

        expedientesJTModelSorted= new TableSorted(expedientesJTableModel);
        expedientesJTModelSorted.setTableHeader(expedientesJTable.getTableHeader());
        expedientesJTable.setModel(expedientesJTModelSorted);
        expedientesJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        expedientesJTable.setCellSelectionEnabled(false);
        expedientesJTable.setColumnSelectionAllowed(false);
        expedientesJTable.setRowSelectionAllowed(true);
        expedientesJTable.getTableHeader().setReorderingAllowed(false);
        ((TableSorted)expedientesJTable.getModel()).getTableHeader().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                reColorearBloqueo();
            }
        });
        ((TableSorted)expedientesJTable.getModel()).getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reColorearBloqueo();
            }
        });
        TableColumn col= expedientesJTable.getColumnModel().getColumn(hiddenCol);
        col.setResizable(false);
        col.setWidth(0);
        col.setMaxWidth(0);
        col.setMinWidth(0);
        col.setPreferredWidth(0);


		tipoLicenciaEJCBox.setSelectedPatron(CConstantesOcupaciones.selectedSubApp);
		tipoLicenciaEJCBox.setEnabled(true);
		estadoEJCBox.setSelectedIndex(0);
	}

    private void reColorearBloqueo(){
          Vector redRows= new Vector();
          for (int i=0; i < expedientesJTModelSorted.getRowCount(); i++){
              int pos= ((Integer)expedientesJTModelSorted.getValueAt(i, hiddenCol)).intValue();
              CExpedienteLicencia e= (CExpedienteLicencia)expedientesLicencia.get(pos);
              if (e.bloqueado()){
                  redRows.add(new Integer(i));
              }else redRows.add(null);
          }

        /** Re-Pintamos en rojo, los expedientes que esten bloqueados */
        for (int j=0; j < expedientesJTableModel.getColumnCount(); j++){
            // Annadimos a cada columna el renderer ColorTableCellRenderer
            TableColumn col= expedientesJTable.getColumnModel().getColumn(j);
            ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
            col.setCellRenderer(colorTableCellRenderer);
        }

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
        nifPropietarioJTextField = new javax.swing.JTextField();
        estadoExpedienteJLabel = new javax.swing.JLabel();
        expedientesJScrollPane = new javax.swing.JScrollPane();
        expedientesJTable = new javax.swing.JTable();
        fechaAperturaDesdeJTextField = new javax.swing.JTextField();
        fechaAperturaJLabel = new javax.swing.JLabel();
        tipoLicenciaJLabel = new javax.swing.JLabel();
        nifPropietarioJLabel = new javax.swing.JLabel();
        numeroExpedienteJTextField = new javax.swing.JTextField();
        numeroExpedienteJLabel = new javax.swing.JLabel();
        fechaAperturaHastaJTextField = new javax.swing.JTextField();
        borrarFechaDesdeJButton = new javax.swing.JButton();
        fechaDesdeJButton = new javax.swing.JButton();
        borrarFechaHastaJButton = new javax.swing.JButton();
        fechaHastaJButton = new javax.swing.JButton();
        buscarDNITitularJButton = new javax.swing.JButton();
        tipoObraJLabel = new javax.swing.JLabel();

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

        jPanel1.add(aceptarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 380, 90, -1));

        cancelarJButton.setText("Cancelar");
        cancelarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(cancelarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 380, 90, -1));

        buscarJButton.setText("Buscar");
        buscarJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(buscarJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 160, -1, -1));

        jPanel1.add(nifPropietarioJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 320, 20));

        estadoExpedienteJLabel.setText("Estado actual del expediente:");
        jPanel1.add(estadoExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 310, 20));

        expedientesJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Tipo de licencia", "N.I.F. del propietario", "Núm. expediente", "Estado actual del expediente", "Fecha de apertura", "Tipo de la vía", "Nombre de la vía", "Número de la vía"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        expedientesJTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                expedientesJTableKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                expedientesJTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                expedientesJTableKeyReleased(evt);
            }
        });


        expedientesJTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                expedientesJTableFocusGained();
            }
        });
        expedientesJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expedientesJTableMouseClicked();
            }
        });
        expedientesJTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                expedientesJTableMouseDragged();
            }
        });

        expedientesJScrollPane.setViewportView(expedientesJTable);

        jPanel1.add(expedientesJScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 760, 170));

        fechaAperturaDesdeJTextField.setEnabled(false);
        jPanel1.add(fechaAperturaDesdeJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 120, 100, 20));

        fechaAperturaJLabel.setText("Fecha de apertura (desde/hasta):");
        jPanel1.add(fechaAperturaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 310, 20));

        tipoLicenciaJLabel.setText("Tipo de licencia:");
        jPanel1.add(tipoLicenciaJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 310, 20));

        nifPropietarioJLabel.setText("N.I.F. del propietario:");
        jPanel1.add(nifPropietarioJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 310, 20));

        jPanel1.add(numeroExpedienteJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 340, 20));

        numeroExpedienteJLabel.setText("N\u00fam. expediente:");
        jPanel1.add(numeroExpedienteJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 310, 20));

        fechaAperturaHastaJTextField.setEnabled(false);
        jPanel1.add(fechaAperturaHastaJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 120, 110, 20));

        borrarFechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaDesdeJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(borrarFechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 120, 20, 20));

        fechaDesdeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaDesdeJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(fechaDesdeJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, 20, 20));

        borrarFechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarFechaHastaJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(borrarFechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 120, 20, 20));

        fechaHastaJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaHastaJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(fechaHastaJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 120, 20, 20));

        buscarDNITitularJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarDNITitularJButtonActionPerformed(evt);
            }
        });

        jPanel1.add(buscarDNITitularJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 60, 20, 20));

        tipoObraJLabel.setText("Tipo Obra:");
        jPanel1.add(tipoObraJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 310, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 410));

        pack();
    }//GEN-END:initComponents

    private void buscarDNITitularJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarDNITitularJButtonActionPerformed
        // TODO add your handling code here:
        CPersonaJDialog dialog= CUtilidadesComponentes.showPersonaDialog(desktop);
        CPersonaJuridicoFisica persona= dialog.getPersona();
        if (persona != null) {
            nifPropietarioJTextField.setText(persona.getDniCif());
        }

    }//GEN-LAST:event_buscarDNITitularJButtonActionPerformed

    private void fechaHastaJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaHastaJButtonActionPerformed
        // TODO add your handling code here:
        CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(desktop);
        String fechaHasta= dialog.getFechaSelected();
        if ((fechaHasta != null) && (!fechaHasta.trim().equals(""))) {
            fechaAperturaHastaJTextField.setText(fechaHasta);
        }

    }//GEN-LAST:event_fechaHastaJButtonActionPerformed

    private void borrarFechaHastaJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarFechaHastaJButtonActionPerformed
        // TODO add your handling code here:
        fechaAperturaHastaJTextField.setText("");
    }//GEN-LAST:event_borrarFechaHastaJButtonActionPerformed

    private void fechaDesdeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaDesdeJButtonActionPerformed
        // TODO add your handling code here:
        CCalendarJDialog dialog= CUtilidadesComponentes.showCalendarDialog(desktop);
        String fechaDesde= dialog.getFechaSelected();
        if ((fechaDesde != null) && (!fechaDesde.trim().equals(""))) {
            fechaAperturaDesdeJTextField.setText(fechaDesde);
        }

    }//GEN-LAST:event_fechaDesdeJButtonActionPerformed

    private void borrarFechaDesdeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarFechaDesdeJButtonActionPerformed
        // TODO add your handling code here:
        fechaAperturaDesdeJTextField.setText("");
    }//GEN-LAST:event_borrarFechaDesdeJButtonActionPerformed

    private void getExpedienteSeleccionado(){
        int seleccion = expedientesJTable.getSelectedRow();
        if (seleccion != -1){
            selectedExpediente= (String) expedientesJTModelSorted.getValueAt(seleccion, 3);
            numeroExpedienteJTextField.setText(selectedExpediente);
        }
    }

	private void expedientesJTableMouseClicked() {//GEN-FIRST:event_expedientesJTableMouseClicked
        getExpedienteSeleccionado();
	}//GEN-LAST:event_expedientesJTableMouseClicked
    private void expedientesJTableMouseDragged() {//GEN-FIRST:event_resultadosJTableMouseDragged
        getExpedienteSeleccionado();
    }//GEN-LAST:event_resultadosJTableMouseDragged

    private void expedientesJTableFocusGained() {//GEN-FIRST:event_resultadosJTableFocusGained
        getExpedienteSeleccionado();
    }//GEN-LAST:event_resultadosJTableFocusGained

    private void expedientesJTableKeyTyped(java.awt.event.KeyEvent evt) {
        getExpedienteSeleccionado();
    }
    private void expedientesJTableKeyPressed(java.awt.event.KeyEvent evt){
        getExpedienteSeleccionado();
    }
    private void expedientesJTableKeyReleased(java.awt.event.KeyEvent evt){
        getExpedienteSeleccionado();
    }
    

	private void buscarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarJButtonActionPerformed

		CUtilidadesComponentes.clearTable(expedientesJTableModel);
		selectedExpediente= "";
		Hashtable hash = new Hashtable();

		if (tipoLicenciaEJCBox.getSelectedIndex() != 0) {
			hash.put("E.ID_TIPO_LICENCIA", "" + tipoLicenciaEJCBox.getSelectedPatron().toString());
		}
		if (tipoObraEJCBox.getSelectedIndex() != 0) {
			hash.put("E.ID_TIPO_OBRA", tipoObraEJCBox.getSelectedPatron());
		}
		hash.put("F.DNI_CIF", "" + nifPropietarioJTextField.getText());
		hash.put("A.NUM_EXPEDIENTE", numeroExpedienteJTextField.getText());
		if (estadoEJCBox.getSelectedIndex() != 0) {
			hash.put("A.ID_ESTADO", "" + estadoEJCBox.getSelectedPatron() );
		}

        /** comprobamos que la fecha tiene formato valido */
        if ((CUtilidades.parseFechaStringToString(fechaAperturaDesdeJTextField.getText()) == null) || (CUtilidades.parseFechaStringToString(fechaAperturaHastaJTextField.getText()) == null)) {
            mostrarMensaje(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.mensaje2"));
            return;
        } else {

            //Between entre fechas
            Date fechaDesde = CUtilidades.parseFechaStringToDate(fechaAperturaDesdeJTextField.getText());
            if (fechaAperturaDesdeJTextField.getText().trim().equals("")) {
                fechaDesde = new Date(1);
            }
            Date fechaHasta = CUtilidades.parseFechaStringToDate(fechaAperturaHastaJTextField.getText().trim());
            if (fechaAperturaHastaJTextField.getText().trim().equals("")) {
                fechaHasta = new Date();
            }

            if ((fechaDesde != null) && (fechaHasta != null)) {
                /** comprobamos que fechaDesde sea menor que fechaHasta */
                long millisDesde= fechaDesde.getTime();
                long millisHasta= fechaHasta.getTime();
                if (millisDesde > millisHasta){
                    mostrarMensaje(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.mensaje1"));
                    return;
                }
                String fechaDesdeFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaDesde);
                long millis = fechaHasta.getTime();
                /** annadimos un dia */
                millis += 24 * 60 * 60 * 1000;
                fechaHasta = new Date(millis);
                String fechaHastaFormatted = new SimpleDateFormat("yyyy-MM-dd").format(fechaHasta);

                hash.put("BETWEEN*A.FECHA_APERTURA", fechaDesdeFormatted + "*" + fechaHastaFormatted);
            }
        }

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		expedientesLicencia= COperacionesLicencias.getSearchedLicenciasObra(hash);

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));


		if ((expedientesLicencia == null) || (expedientesLicencia.size() == 0)) {
			logger.info("expedientesLicencia: " + expedientesLicencia);
			mostrarMensaje(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.mensaje3"));
			return;
		}


		logger.info("expedientesLicencia.size(): " + expedientesLicencia.size());


		for (int i = 0; i < expedientesLicencia.size(); i++) {
			CExpedienteLicencia expedienteLicencia = (CExpedienteLicencia) expedientesLicencia.elementAt(i);
			logger.info("expedienteLicencia.getNumExpediente(): " + expedienteLicencia.getNumExpediente());

            String tipoVia= "";
            try{
                if ((expedienteLicencia.getTipoViaAfecta() != null) && (!expedienteLicencia.getTipoViaAfecta().trim().equalsIgnoreCase(""))){
                    tipoVia= Estructuras.getListaTiposViaINE().getDomainNode(expedienteLicencia.getTipoViaAfecta()).getTerm(CMainOcupaciones.currentLocale.toString());
                }
            }catch(Exception e){
                logger.error("ERROR. No encuentra el tipo de Via="+expedienteLicencia.getTipoViaAfecta());
            }

            String patronTipoLicencia= expedienteLicencia.getTipoLicenciaDescripcion();
            String tipoObra= "";
            String sTipoLicencia="";
            try{
                if (patronTipoLicencia != null){
                    sTipoLicencia= ((DomainNode)Estructuras.getListaLicencias().getDomainNode(new Integer(expedienteLicencia.getTipoLicenciaDescripcion()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
                    if (patronTipoLicencia.equalsIgnoreCase(new Integer(CConstantesOcupaciones.ObraMayor).toString())) {
                        tipoObra= ((DomainNode)Estructuras.getListaTiposObra().getDomainNode(new Integer(expedienteLicencia.getPatronTipoObraSolicitud()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
                    }else if (patronTipoLicencia.equalsIgnoreCase(new Integer(CConstantesOcupaciones.ObraMenor).toString())) {
                        tipoObra= ((DomainNode)Estructuras.getListaTiposObraMenor().getDomainNode(new Integer(expedienteLicencia.getPatronTipoObraSolicitud()).toString())).getTerm(CMainOcupaciones.currentLocale.toString());
                    }
                }
            }catch(Exception e){
                logger.error("ERROR. Al sacar los tipos de Obra.");
            }
			expedientesJTableModel.addRow(new Object[]{sTipoLicencia,
                                                       tipoObra,
													   "" + expedienteLicencia.getNifPropietario(),
													   expedienteLicencia.getNumExpediente(),
													   ((DomainNode)Estructuras.getListaEstados().getDomainNode(new Integer(expedienteLicencia.getEstado().getIdEstado()).toString())).getTerm(CMainOcupaciones.currentLocale.toString()),
													   CConstantesOcupaciones.df.format(expedienteLicencia.getFechaApertura()),
													   tipoVia,
													   expedienteLicencia.getNombreViaAfecta(),
													   expedienteLicencia.getNumeroViaAfecta(),
                                                       new Integer(i)});

		}

	}//GEN-LAST:event_buscarJButtonActionPerformed

	private void cancelarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarJButtonActionPerformed

		selectedExpediente= "";
		dispose();

	}//GEN-LAST:event_cancelarJButtonActionPerformed

	private void aceptarJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aceptarJButtonActionPerformed
		dispose();
	}//GEN-LAST:event_aceptarJButtonActionPerformed

    public String getNumExpedienteSeleccionado(){
        return selectedExpediente;
    }

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		new CSearchJDialog(new javax.swing.JFrame(), true).show();
	}

	private ComboBoxEstructuras tipoLicenciaEJCBox;
    private ComboBoxEstructuras estadoEJCBox;
    private ComboBoxEstructuras tipoObraEJCBox;

	private DefaultTableModel expedientesJTableModel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarJButton;
    private javax.swing.JButton borrarFechaDesdeJButton;
    private javax.swing.JButton borrarFechaHastaJButton;
    private javax.swing.JButton buscarDNITitularJButton;
    private javax.swing.JButton buscarJButton;
    private javax.swing.JButton cancelarJButton;
    private javax.swing.JLabel estadoExpedienteJLabel;
    private javax.swing.JScrollPane expedientesJScrollPane;
    private javax.swing.JTable expedientesJTable;
    private javax.swing.JTextField fechaAperturaDesdeJTextField;
    private javax.swing.JTextField fechaAperturaHastaJTextField;
    private javax.swing.JLabel fechaAperturaJLabel;
    private javax.swing.JButton fechaDesdeJButton;
    private javax.swing.JButton fechaHastaJButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nifPropietarioJLabel;
    private javax.swing.JTextField nifPropietarioJTextField;
    private javax.swing.JLabel numeroExpedienteJLabel;
    private javax.swing.JTextField numeroExpedienteJTextField;
    private javax.swing.JLabel tipoLicenciaJLabel;
    private javax.swing.JLabel tipoObraJLabel;
    // End of variables declaration//GEN-END:variables

	private void renombrarComponentes() {

		try {
			setTitle(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.JInternalFrame.title"));

			tipoLicenciaJLabel.setText(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.tipoLicenciaJLabel.text"));
            tipoObraJLabel.setText(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.tipoObraJLabel.text"));
			nifPropietarioJLabel.setText(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.nifPropietarioJLabel.text"));
			numeroExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.numeroExpedienteJLabel.text"));
			estadoExpedienteJLabel.setText(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.estadoExpedienteJLabel.text"));
			fechaAperturaJLabel.setText(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.fechaAperturaJLabel.text"));

			buscarJButton.setText(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.buscarJButton.text"));
			cancelarJButton.setText(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.cancelarJButton.text"));
			aceptarJButton.setText(CMainOcupaciones.literales.getString("CSearchLicenciasObraJDialog.aceptarJButton.text"));


            fechaDesdeJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoZoom);
            fechaHastaJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoZoom);
            borrarFechaDesdeJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
            borrarFechaHastaJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoDeleteParcela);
            buscarDNITitularJButton.setIcon(com.geopista.app.ocupaciones.CUtilidadesComponentes.iconoZoom);


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
		}
	}

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(desktop, mensaje);
    }


}
